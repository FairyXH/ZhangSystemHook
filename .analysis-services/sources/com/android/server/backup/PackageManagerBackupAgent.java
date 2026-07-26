package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class PackageManagerBackupAgent extends android.app.backup.BackupAgent {
    static final java.lang.String ANCESTRAL_RECORD_KEY = "@ancestral_record@";
    static final int ANCESTRAL_RECORD_VERSION = 1;
    private static final boolean DEBUG = false;
    private static final java.lang.String DEFAULT_HOME_KEY = "@home@";
    static final java.lang.String GLOBAL_METADATA_KEY = "@meta@";
    static final java.lang.String STATE_FILE_HEADER = "=state=";
    static final int STATE_FILE_VERSION = 2;
    private static final java.lang.String TAG = "PMBA";
    private static final int UNDEFINED_ANCESTRAL_RECORD_VERSION = -1;
    private java.util.List<android.content.pm.PackageInfo> mAllPackages;
    private boolean mHasMetadata;
    private android.content.pm.PackageManager mPackageManager;
    private android.content.ComponentName mRestoredHome;
    private java.lang.String mRestoredHomeInstaller;
    private java.util.ArrayList<byte[]> mRestoredHomeSigHashes;
    private long mRestoredHomeVersion;
    private java.util.HashMap<java.lang.String, com.android.server.backup.PackageManagerBackupAgent.Metadata> mRestoredSignatures;
    private int mStoredAncestralRecordVersion;
    private android.content.ComponentName mStoredHomeComponent;
    private java.util.ArrayList<byte[]> mStoredHomeSigHashes;
    private long mStoredHomeVersion;
    private java.lang.String mStoredIncrementalVersion;
    private int mStoredSdkVersion;
    private int mUserId;
    private java.util.HashMap<java.lang.String, com.android.server.backup.PackageManagerBackupAgent.Metadata> mStateVersions = new java.util.HashMap<>();
    private final java.util.HashSet<java.lang.String> mExisting = new java.util.HashSet<>();

    interface RestoreDataConsumer {
        void consumeRestoreData(android.app.backup.BackupDataInput backupDataInput) throws java.io.IOException;
    }

    public class Metadata {
        public java.util.ArrayList<byte[]> sigHashes;
        public long versionCode;

        Metadata(long version, java.util.ArrayList<byte[]> hashes) {
            this.versionCode = version;
            this.sigHashes = hashes;
        }
    }

    public PackageManagerBackupAgent(android.content.pm.PackageManager packageMgr, java.util.List<android.content.pm.PackageInfo> packages, int userId) {
        init(packageMgr, packages, userId);
    }

    public PackageManagerBackupAgent(android.content.pm.PackageManager packageMgr, int userId, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        init(packageMgr, null, userId);
        evaluateStorablePackages(backupEligibilityRules);
    }

    private void init(android.content.pm.PackageManager packageMgr, java.util.List<android.content.pm.PackageInfo> packages, int userId) {
        this.mPackageManager = packageMgr;
        this.mAllPackages = packages;
        this.mRestoredSignatures = null;
        this.mHasMetadata = false;
        this.mStoredSdkVersion = android.os.Build.VERSION.SDK_INT;
        this.mStoredIncrementalVersion = android.os.Build.VERSION.INCREMENTAL;
        this.mUserId = userId;
    }

    public void evaluateStorablePackages(com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        this.mAllPackages = getStorableApplications(this.mPackageManager, this.mUserId, backupEligibilityRules);
    }

    public static java.util.List<android.content.pm.PackageInfo> getStorableApplications(android.content.pm.PackageManager pm, int userId, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        java.util.List<android.content.pm.PackageInfo> pkgs = pm.getInstalledPackagesAsUser(134217728, userId);
        int N = pkgs.size();
        for (int a = N - 1; a >= 0; a--) {
            android.content.pm.PackageInfo pkg = pkgs.get(a);
            if (!backupEligibilityRules.appIsEligibleForBackup(pkg.applicationInfo)) {
                pkgs.remove(a);
            }
        }
        return pkgs;
    }

    public boolean hasMetadata() {
        return this.mHasMetadata;
    }

    public int getSourceSdk() {
        return this.mStoredSdkVersion;
    }

    public com.android.server.backup.PackageManagerBackupAgent.Metadata getRestoredMetadata(java.lang.String packageName) {
        if (this.mRestoredSignatures == null) {
            android.util.Slog.w(TAG, "getRestoredMetadata() before metadata read!");
            return null;
        }
        return this.mRestoredSignatures.get(packageName);
    }

    public java.util.Set<java.lang.String> getRestoredPackages() {
        if (this.mRestoredSignatures == null) {
            android.util.Slog.w(TAG, "getRestoredPackages() before metadata read!");
            return null;
        }
        return this.mRestoredSignatures.keySet();
    }

    @Override // android.app.backup.BackupAgent
    public void onBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState) {
        java.io.ByteArrayOutputStream outputBuffer = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream outputBufferStream = new java.io.DataOutputStream(outputBuffer);
        parseStateFile(oldState);
        if (this.mStoredIncrementalVersion == null || !this.mStoredIncrementalVersion.equals(android.os.Build.VERSION.INCREMENTAL)) {
            android.util.Slog.i(TAG, "Previous metadata " + this.mStoredIncrementalVersion + " mismatch vs " + android.os.Build.VERSION.INCREMENTAL + " - rewriting");
            this.mExisting.clear();
        }
        boolean upgradingAncestralRecordVersion = false;
        try {
            if (!this.mExisting.contains(ANCESTRAL_RECORD_KEY)) {
                android.util.Slog.v(TAG, "No ancestral record version in the old state. Storing ancestral record version key");
                outputBufferStream.writeInt(1);
                writeEntity(data, ANCESTRAL_RECORD_KEY, outputBuffer.toByteArray());
                upgradingAncestralRecordVersion = true;
            } else if (this.mStoredAncestralRecordVersion != 1) {
                android.util.Slog.v(TAG, "Ancestral record version has changed from old state. Storingancestral record version key");
                outputBufferStream.writeInt(1);
                writeEntity(data, ANCESTRAL_RECORD_KEY, outputBuffer.toByteArray());
                upgradingAncestralRecordVersion = true;
                this.mExisting.remove(ANCESTRAL_RECORD_KEY);
            } else {
                this.mExisting.remove(ANCESTRAL_RECORD_KEY);
            }
            outputBuffer.reset();
            if (!this.mExisting.contains(GLOBAL_METADATA_KEY)) {
                outputBufferStream.writeInt(android.os.Build.VERSION.SDK_INT);
                outputBufferStream.writeUTF(android.os.Build.VERSION.INCREMENTAL);
                writeEntity(data, GLOBAL_METADATA_KEY, outputBuffer.toByteArray());
            } else {
                this.mExisting.remove(GLOBAL_METADATA_KEY);
            }
            for (android.content.pm.PackageInfo pkg : this.mAllPackages) {
                java.lang.String packName = pkg.packageName;
                if (!packName.equals(GLOBAL_METADATA_KEY)) {
                    try {
                        android.content.pm.PackageInfo info = this.mPackageManager.getPackageInfoAsUser(packName, 134217728, this.mUserId);
                        if (this.mExisting.contains(packName)) {
                            this.mExisting.remove(packName);
                            if (upgradingAncestralRecordVersion || info.getLongVersionCode() != this.mStateVersions.get(packName).versionCode) {
                            }
                        }
                        android.content.pm.SigningInfo signingInfo = info.signingInfo;
                        if (signingInfo == null) {
                            android.util.Slog.w(TAG, "Not backing up package " + packName + " since it appears to have no signatures.");
                        } else {
                            outputBuffer.reset();
                            if (info.versionCodeMajor != 0) {
                                outputBufferStream.writeInt(Integer.MIN_VALUE);
                                outputBufferStream.writeLong(info.getLongVersionCode());
                            } else {
                                outputBufferStream.writeInt(info.versionCode);
                            }
                            android.content.pm.Signature[] infoSignatures = signingInfo.getApkContentsSigners();
                            writeSignatureHashArray(outputBufferStream, com.android.server.backup.BackupUtils.hashSignatureArray(infoSignatures));
                            writeEntity(data, packName, outputBuffer.toByteArray());
                        }
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        this.mExisting.add(packName);
                    }
                }
            }
            if (!this.mExisting.isEmpty() && upgradingAncestralRecordVersion) {
                for (java.lang.String pkgName : this.mExisting) {
                    android.util.Slog.i(TAG, "Ancestral state updated - Deleting uninstalled package: " + pkgName + " from existing backup");
                    data.writeEntityHeader(pkgName, -1);
                }
                this.mExisting.clear();
            }
            writeStateFile(this.mAllPackages, newState);
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Unable to write package backup data file!");
        }
    }

    private static void writeEntity(android.app.backup.BackupDataOutput data, java.lang.String key, byte[] bytes) throws java.io.IOException {
        data.writeEntityHeader(key, bytes.length);
        data.writeEntityData(bytes, bytes.length);
    }

    @Override // android.app.backup.BackupAgent
    public void onRestore(android.app.backup.BackupDataInput data, int appVersionCode, android.os.ParcelFileDescriptor newState) throws java.io.IOException {
        int ancestralRecordVersion = getAncestralRecordVersionValue(data);
        com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer consumer = getRestoreDataConsumer(ancestralRecordVersion);
        if (consumer == null) {
            android.util.Slog.w(TAG, "Ancestral restore set version is unknown to this Android version; not restoring");
        } else {
            consumer.consumeRestoreData(data);
        }
    }

    private int getAncestralRecordVersionValue(android.app.backup.BackupDataInput data) throws java.io.IOException {
        if (!data.readNextHeader()) {
            return -1;
        }
        java.lang.String key = data.getKey();
        int dataSize = data.getDataSize();
        if (!ANCESTRAL_RECORD_KEY.equals(key)) {
            return -1;
        }
        byte[] inputBytes = new byte[dataSize];
        data.readEntityData(inputBytes, 0, dataSize);
        java.io.ByteArrayInputStream inputBuffer = new java.io.ByteArrayInputStream(inputBytes);
        java.io.DataInputStream inputBufferStream = new java.io.DataInputStream(inputBuffer);
        int ancestralRecordVersionValue = inputBufferStream.readInt();
        return ancestralRecordVersionValue;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer getRestoreDataConsumer(int i) {
        java.lang.Object[] objArr = 0;
        switch (i) {
            case -1:
                return new com.android.server.backup.PackageManagerBackupAgent.LegacyRestoreDataConsumer();
            case 0:
            default:
                android.util.Slog.e(TAG, "Unrecognized ANCESTRAL_RECORD_VERSION: " + i);
                return null;
            case 1:
                return new com.android.server.backup.PackageManagerBackupAgent.AncestralVersion1RestoreDataConsumer();
        }
    }

    private static void writeSignatureHashArray(java.io.DataOutputStream out, java.util.ArrayList<byte[]> hashes) throws java.io.IOException {
        out.writeInt(hashes.size());
        for (byte[] buffer : hashes) {
            out.writeInt(buffer.length);
            out.write(buffer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.ArrayList<byte[]> readSignatureHashArray(java.io.DataInputStream in) {
        try {
            try {
                int num = in.readInt();
                if (num > 20) {
                    android.util.Slog.e(TAG, "Suspiciously large sig count in restore data; aborting");
                    throw new java.lang.IllegalStateException("Bad restore state");
                }
                boolean nonHashFound = false;
                java.util.ArrayList<byte[]> sigs = new java.util.ArrayList<>(num);
                for (int i = 0; i < num; i++) {
                    int len = in.readInt();
                    byte[] readHash = new byte[len];
                    in.read(readHash);
                    sigs.add(readHash);
                    if (len != 32) {
                        nonHashFound = true;
                    }
                }
                if (nonHashFound) {
                    return com.android.server.backup.BackupUtils.hashSignatureArray(sigs);
                }
                return sigs;
            } catch (java.io.EOFException e) {
                android.util.Slog.w(TAG, "Read empty signature block");
                return null;
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Unable to read signatures");
            return null;
        }
    }

    private void parseStateFile(android.os.ParcelFileDescriptor stateFile) {
        long versionCode;
        this.mExisting.clear();
        this.mStateVersions.clear();
        this.mStoredSdkVersion = 0;
        this.mStoredIncrementalVersion = null;
        this.mStoredHomeComponent = null;
        this.mStoredHomeVersion = 0L;
        this.mStoredHomeSigHashes = null;
        this.mStoredAncestralRecordVersion = -1;
        java.io.FileInputStream instream = new java.io.FileInputStream(stateFile.getFileDescriptor());
        java.io.BufferedInputStream inbuffer = new java.io.BufferedInputStream(instream);
        java.io.DataInputStream in = new java.io.DataInputStream(inbuffer);
        boolean ignoreExisting = false;
        try {
            java.lang.String pkg = in.readUTF();
            if (pkg.equals(STATE_FILE_HEADER)) {
                int stateVersion = in.readInt();
                if (stateVersion > 2) {
                    android.util.Slog.w(TAG, "Unsupported state file version " + stateVersion + ", redoing from start");
                    return;
                }
                pkg = in.readUTF();
            } else {
                android.util.Slog.i(TAG, "Older version of saved state - rewriting");
                ignoreExisting = true;
            }
            if (pkg.equals(ANCESTRAL_RECORD_KEY)) {
                this.mStoredAncestralRecordVersion = in.readInt();
                if (!ignoreExisting) {
                    this.mExisting.add(ANCESTRAL_RECORD_KEY);
                }
                pkg = in.readUTF();
            } else {
                android.util.Slog.i(TAG, "Older version of saved state - does not contain ancestral record version");
            }
            if (pkg.equals(DEFAULT_HOME_KEY)) {
                this.mStoredHomeComponent = android.content.ComponentName.unflattenFromString(in.readUTF());
                this.mStoredHomeVersion = in.readLong();
                this.mStoredHomeSigHashes = readSignatureHashArray(in);
                pkg = in.readUTF();
            }
            if (pkg.equals(GLOBAL_METADATA_KEY)) {
                this.mStoredSdkVersion = in.readInt();
                this.mStoredIncrementalVersion = in.readUTF();
                if (!ignoreExisting) {
                    this.mExisting.add(GLOBAL_METADATA_KEY);
                }
                while (true) {
                    java.lang.String pkg2 = in.readUTF();
                    int versionCodeInt = in.readInt();
                    if (versionCodeInt == Integer.MIN_VALUE) {
                        versionCode = in.readLong();
                    } else {
                        versionCode = versionCodeInt;
                    }
                    if (!ignoreExisting) {
                        this.mExisting.add(pkg2);
                    }
                    this.mStateVersions.put(pkg2, new com.android.server.backup.PackageManagerBackupAgent.Metadata(versionCode, null));
                }
            } else {
                android.util.Slog.e(TAG, "No global metadata in state file!");
            }
        } catch (java.io.EOFException e) {
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Unable to read Package Manager state file: " + e2);
        }
    }

    private android.content.ComponentName getPreferredHomeComponent() {
        return this.mPackageManager.getHomeActivities(new java.util.ArrayList());
    }

    static void writeStateFile(java.util.List<android.content.pm.PackageInfo> pkgs, android.os.ParcelFileDescriptor stateFile) {
        java.io.FileOutputStream outstream = new java.io.FileOutputStream(stateFile.getFileDescriptor());
        java.io.BufferedOutputStream outbuf = new java.io.BufferedOutputStream(outstream);
        java.io.DataOutputStream out = new java.io.DataOutputStream(outbuf);
        try {
            out.writeUTF(STATE_FILE_HEADER);
            out.writeInt(2);
            out.writeUTF(ANCESTRAL_RECORD_KEY);
            out.writeInt(1);
            out.writeUTF(GLOBAL_METADATA_KEY);
            out.writeInt(android.os.Build.VERSION.SDK_INT);
            out.writeUTF(android.os.Build.VERSION.INCREMENTAL);
            for (android.content.pm.PackageInfo pkg : pkgs) {
                out.writeUTF(pkg.packageName);
                if (pkg.versionCodeMajor != 0) {
                    out.writeInt(Integer.MIN_VALUE);
                    out.writeLong(pkg.getLongVersionCode());
                } else {
                    out.writeInt(pkg.versionCode);
                }
            }
            out.flush();
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Unable to write package manager state file!");
        }
    }

    private class LegacyRestoreDataConsumer implements com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer {
        private LegacyRestoreDataConsumer() {
        }

        @Override // com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer
        public void consumeRestoreData(android.app.backup.BackupDataInput data) throws java.io.IOException {
            java.util.List<android.content.pm.ApplicationInfo> restoredApps;
            long versionCode;
            java.util.List<android.content.pm.ApplicationInfo> restoredApps2;
            java.util.List<android.content.pm.ApplicationInfo> restoredApps3 = new java.util.ArrayList<>();
            java.util.HashMap<java.lang.String, com.android.server.backup.PackageManagerBackupAgent.Metadata> sigMap = new java.util.HashMap<>();
            while (true) {
                java.lang.String key = data.getKey();
                int dataSize = data.getDataSize();
                byte[] inputBytes = new byte[dataSize];
                data.readEntityData(inputBytes, 0, dataSize);
                java.io.ByteArrayInputStream inputBuffer = new java.io.ByteArrayInputStream(inputBytes);
                java.io.DataInputStream inputBufferStream = new java.io.DataInputStream(inputBuffer);
                if (key.equals(com.android.server.backup.PackageManagerBackupAgent.GLOBAL_METADATA_KEY)) {
                    int storedSdkVersion = inputBufferStream.readInt();
                    com.android.server.backup.PackageManagerBackupAgent.this.mStoredSdkVersion = storedSdkVersion;
                    com.android.server.backup.PackageManagerBackupAgent.this.mStoredIncrementalVersion = inputBufferStream.readUTF();
                    com.android.server.backup.PackageManagerBackupAgent.this.mHasMetadata = true;
                    restoredApps = restoredApps3;
                } else if (key.equals(com.android.server.backup.PackageManagerBackupAgent.DEFAULT_HOME_KEY)) {
                    java.lang.String cn = inputBufferStream.readUTF();
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHome = android.content.ComponentName.unflattenFromString(cn);
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHomeVersion = inputBufferStream.readLong();
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHomeInstaller = inputBufferStream.readUTF();
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHomeSigHashes = com.android.server.backup.PackageManagerBackupAgent.readSignatureHashArray(inputBufferStream);
                    restoredApps = restoredApps3;
                } else {
                    int versionCodeInt = inputBufferStream.readInt();
                    if (versionCodeInt == Integer.MIN_VALUE) {
                        versionCode = inputBufferStream.readLong();
                    } else {
                        versionCode = versionCodeInt;
                    }
                    java.util.ArrayList<byte[]> sigs = com.android.server.backup.PackageManagerBackupAgent.readSignatureHashArray(inputBufferStream);
                    if (sigs == null) {
                        restoredApps2 = restoredApps3;
                    } else if (sigs.size() == 0) {
                        restoredApps2 = restoredApps3;
                    } else {
                        android.content.pm.ApplicationInfo app = new android.content.pm.ApplicationInfo();
                        app.packageName = key;
                        restoredApps3.add(app);
                        restoredApps = restoredApps3;
                        sigMap.put(key, com.android.server.backup.PackageManagerBackupAgent.this.new Metadata(versionCode, sigs));
                    }
                    android.util.Slog.w(com.android.server.backup.PackageManagerBackupAgent.TAG, "Not restoring package " + key + " since it appears to have no signatures.");
                    if (!data.readNextHeader()) {
                        break;
                    } else {
                        restoredApps3 = restoredApps2;
                    }
                }
                if (!data.readNextHeader()) {
                    break;
                } else {
                    restoredApps3 = restoredApps;
                }
            }
            com.android.server.backup.PackageManagerBackupAgent.this.mRestoredSignatures = sigMap;
        }
    }

    private class AncestralVersion1RestoreDataConsumer implements com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer {
        private AncestralVersion1RestoreDataConsumer() {
        }

        @Override // com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer
        public void consumeRestoreData(android.app.backup.BackupDataInput data) throws java.io.IOException {
            java.util.List<android.content.pm.ApplicationInfo> restoredApps;
            long versionCode;
            java.util.List<android.content.pm.ApplicationInfo> restoredApps2 = new java.util.ArrayList<>();
            java.util.HashMap<java.lang.String, com.android.server.backup.PackageManagerBackupAgent.Metadata> sigMap = new java.util.HashMap<>();
            while (data.readNextHeader()) {
                java.lang.String key = data.getKey();
                int dataSize = data.getDataSize();
                byte[] inputBytes = new byte[dataSize];
                data.readEntityData(inputBytes, 0, dataSize);
                java.io.ByteArrayInputStream inputBuffer = new java.io.ByteArrayInputStream(inputBytes);
                java.io.DataInputStream inputBufferStream = new java.io.DataInputStream(inputBuffer);
                if (key.equals(com.android.server.backup.PackageManagerBackupAgent.GLOBAL_METADATA_KEY)) {
                    int storedSdkVersion = inputBufferStream.readInt();
                    com.android.server.backup.PackageManagerBackupAgent.this.mStoredSdkVersion = storedSdkVersion;
                    com.android.server.backup.PackageManagerBackupAgent.this.mStoredIncrementalVersion = inputBufferStream.readUTF();
                    com.android.server.backup.PackageManagerBackupAgent.this.mHasMetadata = true;
                    restoredApps = restoredApps2;
                } else if (key.equals(com.android.server.backup.PackageManagerBackupAgent.DEFAULT_HOME_KEY)) {
                    java.lang.String cn = inputBufferStream.readUTF();
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHome = android.content.ComponentName.unflattenFromString(cn);
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHomeVersion = inputBufferStream.readLong();
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHomeInstaller = inputBufferStream.readUTF();
                    com.android.server.backup.PackageManagerBackupAgent.this.mRestoredHomeSigHashes = com.android.server.backup.PackageManagerBackupAgent.readSignatureHashArray(inputBufferStream);
                    restoredApps = restoredApps2;
                } else {
                    int versionCodeInt = inputBufferStream.readInt();
                    if (versionCodeInt == Integer.MIN_VALUE) {
                        versionCode = inputBufferStream.readLong();
                    } else {
                        versionCode = versionCodeInt;
                    }
                    java.util.ArrayList<byte[]> sigs = com.android.server.backup.PackageManagerBackupAgent.readSignatureHashArray(inputBufferStream);
                    if (sigs == null || sigs.size() == 0) {
                        android.util.Slog.w(com.android.server.backup.PackageManagerBackupAgent.TAG, "Not restoring package " + key + " since it appears to have no signatures.");
                        restoredApps2 = restoredApps2;
                    } else {
                        android.content.pm.ApplicationInfo app = new android.content.pm.ApplicationInfo();
                        app.packageName = key;
                        restoredApps2.add(app);
                        restoredApps = restoredApps2;
                        sigMap.put(key, com.android.server.backup.PackageManagerBackupAgent.this.new Metadata(versionCode, sigs));
                    }
                }
                restoredApps2 = restoredApps;
            }
            com.android.server.backup.PackageManagerBackupAgent.this.mRestoredSignatures = sigMap;
        }
    }
}
