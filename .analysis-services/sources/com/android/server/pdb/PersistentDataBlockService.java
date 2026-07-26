package com.android.server.pdb;

/* JADX INFO: loaded from: classes2.dex */
public class PersistentDataBlockService extends com.android.server.SystemService {
    public static final java.lang.String BOOTLOADER_LOCK_STATE = "ro.boot.vbmeta.device_state";
    public static final int DIGEST_SIZE_BYTES = 32;
    private static final java.lang.String FLASH_LOCK_LOCKED = "1";
    private static final java.lang.String FLASH_LOCK_PROP = "ro.boot.flash.locked";
    private static final java.lang.String FLASH_LOCK_UNLOCKED = "0";
    static final int FRP_CREDENTIAL_RESERVED_SIZE = 1000;
    private static final java.lang.String FRP_SECRET_FILE = "/data/system/frp_secret";
    static final int FRP_SECRET_SIZE = 32;
    private static final java.lang.String FRP_SECRET_TMP_FILE = "/data/system/frp_secret_tmp";
    private static final java.lang.String GSI_RUNNING_PROP = "ro.gsid.image_running";
    private static final java.lang.String GSI_SANDBOX = "/data/gsi_persistent_data";
    static final int HEADER_SIZE = 8;
    public static final int INIT_WAIT_TIMEOUT = 10;
    static final int MAX_DATA_BLOCK_SIZE = 102400;
    static final int MAX_FRP_CREDENTIAL_HANDLE_SIZE = 996;
    static final int MAX_TEST_MODE_DATA_SIZE = 9996;
    private static final java.lang.String OEM_UNLOCK_PROP = "sys.oem_unlock_allowed";
    private static final int PARTITION_TYPE_MARKER = 428873843;
    private static final java.lang.String PERSISTENT_DATA_BLOCK_PROP = "ro.frp.pst";
    static final int TEST_MODE_RESERVED_SIZE = 10000;
    public static final java.lang.String VERIFIED_BOOT_STATE = "ro.boot.verifiedbootstate";
    private int mAllowedUid;
    private long mBlockDeviceSize;
    private final android.content.Context mContext;
    private final java.lang.String mDataBlockFile;
    private boolean mFrpActive;
    private final boolean mFrpEnforced;
    private final java.lang.String mFrpSecretFile;
    private final java.lang.String mFrpSecretTmpFile;
    private final java.util.concurrent.CountDownLatch mInitDoneSignal;
    private com.android.server.pdb.PersistentDataBlockService.InternalService mInternalService;
    private final boolean mIsFileBacked;
    private boolean mIsWritable;
    private final java.lang.Object mLock;
    private final android.os.IBinder mService;
    private static final java.lang.String TAG = com.android.server.pdb.PersistentDataBlockService.class.getSimpleName();
    static final byte[] FRP_SECRET_MAGIC = {-38, -62, -4, -51, -71, 27, 9, -120};

    private native long nativeGetBlockDeviceSize(java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeWipe(java.lang.String str);

    public PersistentDataBlockService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mInitDoneSignal = new java.util.concurrent.CountDownLatch(1);
        this.mAllowedUid = -1;
        this.mBlockDeviceSize = -1L;
        this.mFrpActive = false;
        this.mIsWritable = true;
        this.mService = new com.android.server.pdb.PersistentDataBlockService.AnonymousClass1();
        this.mInternalService = new com.android.server.pdb.PersistentDataBlockService.InternalService();
        this.mContext = context;
        this.mFrpEnforced = android.security.Flags.frpEnforcement();
        this.mFrpActive = this.mFrpEnforced;
        this.mFrpSecretFile = FRP_SECRET_FILE;
        this.mFrpSecretTmpFile = FRP_SECRET_TMP_FILE;
        if (android.os.SystemProperties.getBoolean(GSI_RUNNING_PROP, false)) {
            this.mIsFileBacked = true;
            this.mDataBlockFile = GSI_SANDBOX;
        } else {
            this.mIsFileBacked = false;
            this.mDataBlockFile = android.os.SystemProperties.get(PERSISTENT_DATA_BLOCK_PROP);
        }
    }

    PersistentDataBlockService(android.content.Context context, boolean isFileBacked, java.lang.String dataBlockFile, long blockDeviceSize, boolean frpEnabled, java.lang.String frpSecretFile, java.lang.String frpSecretTmpFile) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mInitDoneSignal = new java.util.concurrent.CountDownLatch(1);
        this.mAllowedUid = -1;
        this.mBlockDeviceSize = -1L;
        this.mFrpActive = false;
        this.mIsWritable = true;
        this.mService = new com.android.server.pdb.PersistentDataBlockService.AnonymousClass1();
        this.mInternalService = new com.android.server.pdb.PersistentDataBlockService.InternalService();
        this.mContext = context;
        this.mIsFileBacked = isFileBacked;
        this.mDataBlockFile = dataBlockFile;
        this.mBlockDeviceSize = blockDeviceSize;
        this.mFrpEnforced = frpEnabled;
        this.mFrpActive = this.mFrpEnforced;
        this.mFrpSecretFile = frpSecretFile;
        this.mFrpSecretTmpFile = frpSecretTmpFile;
    }

    private int getAllowedUid() {
        com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        int mainUserId = umInternal.getMainUserId();
        if (mainUserId < 0) {
            mainUserId = 0;
        }
        java.lang.String allowedPackage = this.mContext.getResources().getString(android.R.string.config_qualified_networks_service_class);
        if (android.text.TextUtils.isEmpty(allowedPackage)) {
            return -1;
        }
        try {
            int allowedUid = this.mContext.getPackageManager().getPackageUidAsUser(allowedPackage, 1048576, mainUserId);
            return allowedUid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "not able to find package " + allowedPackage, e);
            return -1;
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.pdb.PersistentDataBlockService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStart$0();
            }
        }, TAG + ".onStart");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0() {
        enforceChecksumValidity();
        if (this.mFrpEnforced) {
            automaticallyDeactivateFrpIfPossible();
            setOemUnlockEnabledProperty(doGetOemUnlockEnabled());
            setOldSettingForBackworkCompatibility(this.mFrpActive);
        } else {
            formatIfOemUnlockEnabled();
        }
        publishBinderService("persistent_data_block", this.mService);
        signalInitDone();
    }

    void signalInitDone() {
        this.mInitDoneSignal.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOldSettingForBackworkCompatibility(boolean isActive) {
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "secure_frp_mode", isActive ? 1 : 0);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    private void setOemUnlockEnabledProperty(boolean oemUnlockEnabled) {
        setProperty(OEM_UNLOCK_PROP, oemUnlockEnabled ? FLASH_LOCK_LOCKED : FLASH_LOCK_UNLOCKED);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            waitForInitDoneSignal();
            this.mAllowedUid = getAllowedUid();
            com.android.server.LocalServices.addService(com.android.server.pdb.PersistentDataBlockManagerInternal.class, this.mInternalService);
        }
        super.onBootPhase(phase);
    }

    private void waitForInitDoneSignal() {
        try {
            if (!this.mInitDoneSignal.await(10L, java.util.concurrent.TimeUnit.SECONDS)) {
                throw new java.lang.IllegalStateException("Service " + TAG + " init timeout");
            }
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException("Service " + TAG + " init interrupted", e);
        }
    }

    void setAllowedUid(int uid) {
        this.mAllowedUid = uid;
    }

    private void formatIfOemUnlockEnabled() {
        boolean enabled = doGetOemUnlockEnabled();
        if (enabled) {
            synchronized (this.mLock) {
                formatPartitionLocked(true);
            }
        }
        setOemUnlockEnabledProperty(enabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceOemUnlockReadPermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_OEM_UNLOCK_STATE") == -1 && this.mContext.checkCallingOrSelfPermission("android.permission.OEM_UNLOCK_STATE") == -1) {
            throw new java.lang.SecurityException("Can't access OEM unlock state. Requires READ_OEM_UNLOCK_STATE or OEM_UNLOCK_STATE permission.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceOemUnlockWritePermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.OEM_UNLOCK_STATE", "Can't modify OEM unlock state");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceConfigureFrpPermission() {
        if (this.mFrpEnforced && this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_FACTORY_RESET_PROTECTION") == -1) {
            throw new java.lang.SecurityException("Can't configure Factory Reset Protection. Requires CONFIGURE_FACTORY_RESET_PROTECTION");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceUid(int callingUid) {
        if (callingUid != this.mAllowedUid && callingUid != 0) {
            throw new java.lang.SecurityException("uid " + callingUid + " not allowed to access PDB");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceIsAdmin() {
        int userId = android.os.UserHandle.getCallingUserId();
        boolean isAdmin = android.os.UserManager.get(this.mContext).isUserAdmin(userId);
        if (!isAdmin) {
            throw new java.lang.SecurityException("Only the Admin user is allowed to change OEM unlock state");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceUserRestriction(java.lang.String userRestriction) {
        if (android.os.UserManager.get(this.mContext).hasUserRestriction(userRestriction)) {
            throw new java.lang.SecurityException("OEM unlock is disallowed by user restriction: " + userRestriction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTotalDataSizeLocked(java.io.DataInputStream inputStream) throws java.io.IOException {
        inputStream.skipBytes(32);
        int blockId = inputStream.readInt();
        if (blockId == PARTITION_TYPE_MARKER) {
            int totalDataSize = inputStream.readInt();
            return totalDataSize;
        }
        return 0;
    }

    long getBlockDeviceSize() {
        synchronized (this.mLock) {
            if (this.mBlockDeviceSize == -1) {
                if (this.mIsFileBacked) {
                    this.mBlockDeviceSize = 102400L;
                } else {
                    this.mBlockDeviceSize = nativeGetBlockDeviceSize(this.mDataBlockFile);
                }
            }
        }
        return this.mBlockDeviceSize;
    }

    int getMaximumFrpDataSize() {
        long frpSecretSize = this.mFrpEnforced ? FRP_SECRET_MAGIC.length + 32 : 0L;
        return (int) (((getTestHarnessModeDataOffset() - 32) - 8) - frpSecretSize);
    }

    long getFrpCredentialDataOffset() {
        return getOemUnlockDataOffset() - 1000;
    }

    long getFrpSecretMagicOffset() {
        return getFrpSecretDataOffset() - ((long) FRP_SECRET_MAGIC.length);
    }

    long getFrpSecretDataOffset() {
        return getTestHarnessModeDataOffset() - 32;
    }

    long getTestHarnessModeDataOffset() {
        return getFrpCredentialDataOffset() - 10000;
    }

    long getOemUnlockDataOffset() {
        return getBlockDeviceSize() - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enforceChecksumValidity() {
        byte[] storedDigest = new byte[32];
        synchronized (this.mLock) {
            byte[] digest = computeDigestLocked(storedDigest);
            if (digest != null && java.util.Arrays.equals(storedDigest, digest)) {
                return true;
            }
            android.util.Slog.i(TAG, "Formatting FRP partition...");
            formatPartitionLocked(false);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.nio.channels.FileChannel getBlockOutputChannel() throws java.io.IOException {
        enforceFactoryResetProtectionInactive();
        return getBlockOutputChannelIgnoringFrp();
    }

    private java.nio.channels.FileChannel getBlockOutputChannelIgnoringFrp() throws java.io.FileNotFoundException {
        return new java.io.RandomAccessFile(this.mDataBlockFile, "rw").getChannel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean computeAndWriteDigestLocked() {
        byte[] digest = computeDigestLocked(null);
        if (digest == null) {
            return false;
        }
        try {
            java.nio.channels.FileChannel channel = getBlockOutputChannel();
            try {
                java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(32);
                buf.put(digest);
                buf.flip();
                channel.write(buf);
                channel.force(true);
                if (channel != null) {
                    channel.close();
                }
                return true;
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "failed to write block checksum", e);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025 A[Catch: all -> 0x0045, IOException -> 0x0047, Merged into TryCatch #2 {all -> 0x0045, IOException -> 0x0047, blocks: (B:7:0x001e, B:9:0x0021, B:11:0x0028, B:12:0x0030, B:14:0x0038, B:10:0x0025, B:21:0x0048), top: B:32:0x001e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private byte[] computeDigestLocked(byte[] r9) {
        /*
            r8 = this;
            r0 = 0
            java.io.DataInputStream r1 = new java.io.DataInputStream     // Catch: java.io.FileNotFoundException -> L64
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.io.FileNotFoundException -> L64
            java.io.File r3 = new java.io.File     // Catch: java.io.FileNotFoundException -> L64
            java.lang.String r4 = r8.mDataBlockFile     // Catch: java.io.FileNotFoundException -> L64
            r3.<init>(r4)     // Catch: java.io.FileNotFoundException -> L64
            r2.<init>(r3)     // Catch: java.io.FileNotFoundException -> L64
            r1.<init>(r2)     // Catch: java.io.FileNotFoundException -> L64
            java.lang.String r2 = "SHA-256"
            java.security.MessageDigest r2 = java.security.MessageDigest.getInstance(r2)     // Catch: java.security.NoSuchAlgorithmException -> L58
            r3 = 32
            if (r9 == 0) goto L25
            int r4 = r9.length     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
            if (r4 != r3) goto L25
            r1.read(r9)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
            goto L28
        L25:
            r1.skipBytes(r3)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
        L28:
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
            r5 = 0
            r2.update(r4, r5, r3)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
        L30:
            int r3 = r1.read(r4)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
            r6 = r3
            r7 = -1
            if (r3 == r7) goto L3c
            r2.update(r4, r5, r6)     // Catch: java.lang.Throwable -> L45 java.io.IOException -> L47
            goto L30
        L3c:
            libcore.io.IoUtils.closeQuietly(r1)
            byte[] r0 = r2.digest()
            return r0
        L45:
            r0 = move-exception
            goto L54
        L47:
            r3 = move-exception
            java.lang.String r4 = com.android.server.pdb.PersistentDataBlockService.TAG     // Catch: java.lang.Throwable -> L45
            java.lang.String r5 = "failed to read partition"
            android.util.Slog.e(r4, r5, r3)     // Catch: java.lang.Throwable -> L45
            libcore.io.IoUtils.closeQuietly(r1)
            return r0
        L54:
            libcore.io.IoUtils.closeQuietly(r1)
            throw r0
        L58:
            r2 = move-exception
            java.lang.String r3 = com.android.server.pdb.PersistentDataBlockService.TAG
            java.lang.String r4 = "SHA-256 not supported?"
            android.util.Slog.e(r3, r4, r2)
            libcore.io.IoUtils.closeQuietly(r1)
            return r0
        L64:
            r1 = move-exception
            java.lang.String r2 = com.android.server.pdb.PersistentDataBlockService.TAG
            java.lang.String r3 = "partition not available?"
            android.util.Slog.e(r2, r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pdb.PersistentDataBlockService.computeDigestLocked(byte[]):byte[]");
    }

    void formatPartitionLocked(boolean setOemUnlockEnabled) {
        try {
            java.nio.channels.FileChannel channel = getBlockOutputChannelIgnoringFrp();
            try {
                java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(40);
                buf.put(new byte[32]);
                buf.putInt(PARTITION_TYPE_MARKER);
                buf.putInt(0);
                buf.flip();
                channel.write(buf);
                channel.force(true);
                int payload_size = ((int) getBlockDeviceSize()) - 40;
                channel.write(this.mFrpEnforced ? java.nio.ByteBuffer.allocate(((((payload_size - 10000) - FRP_SECRET_MAGIC.length) - 32) - 1000) - 1) : java.nio.ByteBuffer.allocate(((payload_size - 10000) - 1000) - 1));
                channel.force(true);
                if (this.mFrpEnforced) {
                    android.util.Slog.i(TAG, "Writing FRP secret magic");
                    channel.write(java.nio.ByteBuffer.wrap(FRP_SECRET_MAGIC));
                    android.util.Slog.i(TAG, "Writing default FRP secret");
                    channel.write(java.nio.ByteBuffer.allocate(32));
                    channel.force(true);
                    this.mFrpActive = false;
                }
                channel.position(channel.position() + 10000);
                channel.write(java.nio.ByteBuffer.allocate(1000));
                channel.force(true);
                java.nio.ByteBuffer buf2 = java.nio.ByteBuffer.allocate(1000);
                buf2.put((byte) 0);
                buf2.flip();
                channel.write(buf2);
                channel.force(true);
                if (channel != null) {
                    channel.close();
                }
                doSetOemUnlockEnabledLocked(setOemUnlockEnabled);
                computeAndWriteDigestLocked();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "failed to format block", e);
        }
    }

    boolean automaticallyDeactivateFrpIfPossible() {
        synchronized (this.mLock) {
            if (deactivateFrpWithFileSecret(this.mFrpSecretFile)) {
                return true;
            }
            android.util.Slog.w(TAG, "Failed to deactivate with primary secret file, trying backup.");
            if (deactivateFrpWithFileSecret(this.mFrpSecretTmpFile)) {
                moveFrpTempFileToPrimary();
                return true;
            }
            android.util.Slog.w(TAG, "Failed to deactivate with backup secret file, trying default secret.");
            if (deactivateFrp(new byte[32])) {
                return true;
            }
            if (isUpgradingFromPreVRelease()) {
                android.util.Slog.w(TAG, "Upgrading from Android 14 or lower, defaulting FRP secret");
                writeFrpMagicAndDefaultSecret();
                this.mFrpActive = false;
                setOldSettingForBackworkCompatibility(this.mFrpActive);
                return true;
            }
            android.util.Slog.e(TAG, "Did not find valid FRP secret, FRP remains active.");
            return false;
        }
    }

    private boolean deactivateFrpWithFileSecret(java.lang.String frpSecretFile) {
        try {
            return deactivateFrp(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(frpSecretFile, new java.lang.String[0])));
        } catch (java.io.IOException e) {
            android.util.Slog.i(TAG, "Failed to read FRP secret file: " + frpSecretFile + " " + e.getClass().getSimpleName());
            return false;
        }
    }

    private void moveFrpTempFileToPrimary() {
        try {
            java.nio.file.Files.move(java.nio.file.Paths.get(this.mFrpSecretTmpFile, new java.lang.String[0]), java.nio.file.Paths.get(this.mFrpSecretFile, new java.lang.String[0]), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error moving FRP backup file to primary (ignored)", e);
        }
    }

    boolean isFrpActive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mFrpActive;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateFrpSecret(byte[] secret) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(this.mFrpSecretTmpFile, new java.lang.String[0]), secret, java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING, java.nio.file.StandardOpenOption.SYNC);
            if (!this.mInternalService.writeDataBuffer(getFrpSecretDataOffset(), java.nio.ByteBuffer.wrap(secret))) {
                return false;
            }
            moveFrpTempFileToPrimary();
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write FRP secret file", e);
            return false;
        }
    }

    void activateFrp() {
        synchronized (this.mLock) {
            this.mFrpActive = true;
            setOldSettingForBackworkCompatibility(this.mFrpActive);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasFrpSecretMagic() {
        byte[] frpMagic = readDataBlock(getFrpSecretMagicOffset(), FRP_SECRET_MAGIC.length);
        if (frpMagic == null) {
            android.util.Slog.e(TAG, "Failed to read FRP magic region.");
            return false;
        }
        return java.util.Arrays.equals(frpMagic, FRP_SECRET_MAGIC);
    }

    private byte[] getFrpSecret() {
        return readDataBlock(getFrpSecretDataOffset(), 32);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean deactivateFrp(byte[] secret) {
        if (secret == null || secret.length != 32) {
            android.util.Slog.w(TAG, "Attempted to deactivate FRP with a null or incorrectly-sized secret");
            return false;
        }
        synchronized (this.mLock) {
            if (!hasFrpSecretMagic()) {
                android.util.Slog.i(TAG, "No FRP secret magic, system must have been upgraded.");
                writeFrpMagicAndDefaultSecret();
            }
        }
        byte[] partitionSecret = getFrpSecret();
        if (partitionSecret == null || partitionSecret.length != 32) {
            android.util.Slog.e(TAG, "Failed to read FRP secret from persistent data partition");
            return false;
        }
        if (java.security.MessageDigest.isEqual(secret, partitionSecret)) {
            this.mFrpActive = false;
            android.util.Slog.i(TAG, "FRP secret matched, FRP deactivated.");
            setOldSettingForBackworkCompatibility(this.mFrpActive);
            return true;
        }
        android.util.Slog.e(TAG, "FRP deactivation failed with secret " + java.util.HexFormat.of().formatHex(secret));
        return false;
    }

    private void writeFrpMagicAndDefaultSecret() {
        try {
            java.nio.channels.FileChannel channel = getBlockOutputChannelIgnoringFrp();
            try {
                synchronized (this.mLock) {
                    android.util.Slog.i(TAG, "Writing default FRP secret");
                    channel.position(getFrpSecretDataOffset());
                    channel.write(java.nio.ByteBuffer.allocate(32));
                    channel.force(true);
                    android.util.Slog.i(TAG, "Writing FRP secret magic");
                    channel.position(getFrpSecretMagicOffset());
                    channel.write(java.nio.ByteBuffer.wrap(FRP_SECRET_MAGIC));
                    channel.force(true);
                    this.mFrpActive = false;
                }
                if (channel != null) {
                    channel.close();
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write FRP magic and default secret", e);
        }
        computeAndWriteDigestLocked();
    }

    byte[] readDataBlock(long offset, int length) {
        byte[] bytes;
        try {
            java.io.DataInputStream inputStream = new java.io.DataInputStream(new java.io.FileInputStream(new java.io.File(this.mDataBlockFile)));
            try {
                synchronized (this.mLock) {
                    inputStream.skip(offset);
                    bytes = new byte[length];
                    inputStream.readFully(bytes);
                }
                inputStream.close();
                return bytes;
            } finally {
            }
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("persistent partition not readable", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSetOemUnlockEnabledLocked(boolean enabled) {
        try {
            java.nio.channels.FileChannel channel = getBlockOutputChannel();
            try {
                channel.position(getBlockDeviceSize() - 1);
                java.nio.ByteBuffer data = java.nio.ByteBuffer.allocate(1);
                data.put(enabled ? (byte) 1 : (byte) 0);
                data.flip();
                channel.write(data);
                channel.force(true);
                if (channel != null) {
                    channel.close();
                }
            } catch (java.lang.Throwable th) {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "unable to access persistent partition", e);
        } finally {
            setOemUnlockEnabledProperty(enabled);
        }
    }

    void setProperty(java.lang.String name, java.lang.String value) {
        android.os.SystemProperties.set(name, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean doGetOemUnlockEnabled() {
        boolean z;
        try {
            java.io.DataInputStream inputStream = new java.io.DataInputStream(new java.io.FileInputStream(new java.io.File(this.mDataBlockFile)));
            try {
                synchronized (this.mLock) {
                    inputStream.skip(getBlockDeviceSize() - 1);
                    z = inputStream.readByte() != 0;
                }
                return z;
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "unable to access persistent partition", e);
                return false;
            } finally {
                libcore.io.IoUtils.closeQuietly(inputStream);
            }
        } catch (java.io.FileNotFoundException e2) {
            android.util.Slog.e(TAG, "partition not available");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long doGetMaximumDataBlockSize() {
        long frpSecretSize = this.mFrpEnforced ? FRP_SECRET_MAGIC.length + 32 : 0L;
        long actualSize = (((((getBlockDeviceSize() - 8) - 32) - 10000) - frpSecretSize) - 1000) - 1;
        if (actualSize <= 102400) {
            return actualSize;
        }
        return 102400L;
    }

    android.service.persistentdata.IPersistentDataBlockService getInterfaceForTesting() {
        return android.service.persistentdata.IPersistentDataBlockService.Stub.asInterface(this.mService);
    }

    com.android.server.pdb.PersistentDataBlockManagerInternal getInternalInterfaceForTesting() {
        return this.mInternalService;
    }

    /* JADX INFO: renamed from: com.android.server.pdb.PersistentDataBlockService$1, reason: invalid class name */
    class AnonymousClass1 extends android.service.persistentdata.IPersistentDataBlockService.Stub {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int printFrpStatus(java.io.PrintWriter pw, boolean printSecrets) {
            com.android.server.pdb.PersistentDataBlockService.this.enforceUid(android.os.Binder.getCallingUid());
            pw.println("FRP state");
            pw.println("=========");
            pw.println("Enforcement enabled: " + com.android.server.pdb.PersistentDataBlockService.this.mFrpEnforced);
            pw.println("FRP state: " + com.android.server.pdb.PersistentDataBlockService.this.mFrpActive);
            printFrpDataFilesContents(pw, printSecrets);
            printFrpSecret(pw, printSecrets);
            pw.println("OEM unlock state: " + getOemUnlockEnabled());
            pw.println("Bootloader lock state: " + getFlashLockState());
            pw.println("Verified boot state: " + getVerifiedBootState());
            pw.println("Has FRP credential handle: " + hasFrpCredentialHandle());
            pw.println("FRP challenge block size: " + getDataBlockSize());
            return 1;
        }

        private void printFrpSecret(java.io.PrintWriter pw, boolean printSecret) {
            if (com.android.server.pdb.PersistentDataBlockService.this.hasFrpSecretMagic()) {
                if (printSecret) {
                    pw.println("FRP secret in PDB: " + java.util.HexFormat.of().formatHex(com.android.server.pdb.PersistentDataBlockService.this.readDataBlock(com.android.server.pdb.PersistentDataBlockService.this.getFrpSecretDataOffset(), 32)));
                    return;
                } else {
                    pw.println("FRP secret present but omitted.");
                    return;
                }
            }
            pw.println("FRP magic not found");
        }

        private void printFrpDataFilesContents(java.io.PrintWriter pw, boolean printSecrets) {
            printFrpDataFileContents(pw, com.android.server.pdb.PersistentDataBlockService.this.mFrpSecretFile, printSecrets);
            printFrpDataFileContents(pw, com.android.server.pdb.PersistentDataBlockService.this.mFrpSecretTmpFile, printSecrets);
        }

        private void printFrpDataFileContents(java.io.PrintWriter pw, java.lang.String frpSecretFile, boolean printSecret) {
            if (java.nio.file.Files.exists(java.nio.file.Paths.get(frpSecretFile, new java.lang.String[0]), new java.nio.file.LinkOption[0])) {
                if (printSecret) {
                    try {
                        pw.println("FRP secret in " + frpSecretFile + ": " + java.util.HexFormat.of().formatHex(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(frpSecretFile, new java.lang.String[0]))));
                        return;
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "Failed to read " + frpSecretFile, e);
                        return;
                    }
                }
                pw.println("FRP secret file " + frpSecretFile + " exists, contents omitted.");
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0, types: [com.android.server.pdb.PersistentDataBlockService$1$1] */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            if (!com.android.server.pdb.PersistentDataBlockService.this.mFrpEnforced) {
                super.onShellCommand(in, out, err, args, callback, resultReceiver);
            } else {
                new android.os.ShellCommand() { // from class: com.android.server.pdb.PersistentDataBlockService.1.1
                    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
                    /* JADX WARN: Removed duplicated region for block: B:23:0x0048  */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public int onCommand(java.lang.String r8) {
                        /*
                            Method dump skipped, instruction units count: 384
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pdb.PersistentDataBlockService.AnonymousClass1.ShellCommandC00141.onCommand(java.lang.String):int");
                    }

                    public void onHelp() {
                        java.io.PrintWriter pw = getOutPrintWriter();
                        pw.println("Commands");
                        pw.println("status: Print the FRP state and associated information.");
                        pw.println("activate:  Put FRP into \"active\" mode.");
                        pw.println("deactivate <secret>:  Deactivate with a hash of 'secret'.");
                        pw.println("auto_deactivate: Deactivate with the stored secret or the default");
                        pw.println("set_secret <secret>:  Set the stored secret to a hash of `secret`");
                    }

                    private static byte[] hashSecretString(java.lang.String secretInput) {
                        try {
                            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                            return md.digest(secretInput.getBytes());
                        } catch (java.security.NoSuchAlgorithmException e) {
                            android.util.Slog.e("ShellCommand", "Can't happen", e);
                            return new byte[32];
                        }
                    }
                }.exec(this, in, out, err, args, callback, resultReceiver);
            }
        }

        public int write(byte[] data) throws android.os.RemoteException {
            com.android.server.pdb.PersistentDataBlockService.this.enforceUid(android.os.Binder.getCallingUid());
            long maxBlockSize = com.android.server.pdb.PersistentDataBlockService.this.doGetMaximumDataBlockSize();
            if (data.length > maxBlockSize) {
                return (int) (-maxBlockSize);
            }
            java.nio.ByteBuffer headerAndData = java.nio.ByteBuffer.allocate(data.length + 8 + 32);
            headerAndData.put(new byte[32]);
            headerAndData.putInt(com.android.server.pdb.PersistentDataBlockService.PARTITION_TYPE_MARKER);
            headerAndData.putInt(data.length);
            headerAndData.put(data);
            headerAndData.flip();
            synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                if (!com.android.server.pdb.PersistentDataBlockService.this.mIsWritable) {
                    return -1;
                }
                try {
                    java.nio.channels.FileChannel channel = com.android.server.pdb.PersistentDataBlockService.this.getBlockOutputChannel();
                    try {
                        channel.write(headerAndData);
                        channel.force(true);
                        if (channel != null) {
                            channel.close();
                        }
                        if (!com.android.server.pdb.PersistentDataBlockService.this.computeAndWriteDigestLocked()) {
                            return -1;
                        }
                        return data.length;
                    } catch (java.lang.Throwable th) {
                        if (channel != null) {
                            try {
                                channel.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed writing to the persistent data block", e);
                    return -1;
                }
            }
        }

        public byte[] read() {
            java.io.DataInputStream inputStream;
            com.android.server.pdb.PersistentDataBlockService.this.enforceUid(android.os.Binder.getCallingUid());
            if (!com.android.server.pdb.PersistentDataBlockService.this.enforceChecksumValidity()) {
                return new byte[0];
            }
            try {
                try {
                    inputStream = new java.io.DataInputStream(new java.io.FileInputStream(new java.io.File(com.android.server.pdb.PersistentDataBlockService.this.mDataBlockFile)));
                    try {
                        synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                            int totalDataSize = com.android.server.pdb.PersistentDataBlockService.this.getTotalDataSizeLocked(inputStream);
                            if (totalDataSize == 0) {
                                return new byte[0];
                            }
                            byte[] data = new byte[totalDataSize];
                            int read = inputStream.read(data, 0, totalDataSize);
                            if (read >= totalDataSize) {
                                try {
                                    inputStream.close();
                                } catch (java.io.IOException e) {
                                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to close OutputStream");
                                }
                                return data;
                            }
                            android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to read entire data block. bytes read: " + read + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + totalDataSize);
                            try {
                                inputStream.close();
                            } catch (java.io.IOException e2) {
                                android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to close OutputStream");
                            }
                            return null;
                        }
                    } catch (java.io.IOException e3) {
                        android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to read data", e3);
                        try {
                            inputStream.close();
                        } catch (java.io.IOException e4) {
                            android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to close OutputStream");
                        }
                        return null;
                    }
                } catch (java.io.FileNotFoundException e5) {
                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "partition not available?", e5);
                    return null;
                }
            } finally {
                try {
                    inputStream.close();
                } catch (java.io.IOException e6) {
                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to close OutputStream");
                }
            }
        }

        public void wipe() {
            int ret;
            com.android.server.pdb.PersistentDataBlockService.this.enforceFactoryResetProtectionInactive();
            com.android.server.pdb.PersistentDataBlockService.this.enforceOemUnlockWritePermission();
            synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                if (com.android.server.pdb.PersistentDataBlockService.this.mIsFileBacked) {
                    try {
                        java.nio.file.Files.write(java.nio.file.Paths.get(com.android.server.pdb.PersistentDataBlockService.this.mDataBlockFile, new java.lang.String[0]), new byte[com.android.server.pdb.PersistentDataBlockService.MAX_DATA_BLOCK_SIZE], java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
                        ret = 0;
                    } catch (java.io.IOException e) {
                        ret = -1;
                    }
                } else {
                    ret = com.android.server.pdb.PersistentDataBlockService.this.nativeWipe(com.android.server.pdb.PersistentDataBlockService.this.mDataBlockFile);
                }
                if (ret < 0) {
                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "failed to wipe persistent partition");
                } else {
                    com.android.server.pdb.PersistentDataBlockService.this.mIsWritable = false;
                    android.util.Slog.i(com.android.server.pdb.PersistentDataBlockService.TAG, "persistent partition now wiped and unwritable");
                }
            }
        }

        public void setOemUnlockEnabled(boolean enabled) throws java.lang.SecurityException {
            if (android.app.ActivityManager.isUserAMonkey()) {
                return;
            }
            com.android.server.pdb.PersistentDataBlockService.this.enforceOemUnlockWritePermission();
            com.android.server.pdb.PersistentDataBlockService.this.enforceIsAdmin();
            if (enabled) {
                com.android.server.pdb.PersistentDataBlockService.this.enforceUserRestriction("no_oem_unlock");
                com.android.server.pdb.PersistentDataBlockService.this.enforceUserRestriction("no_factory_reset");
            }
            synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                com.android.server.pdb.PersistentDataBlockService.this.doSetOemUnlockEnabledLocked(enabled);
                com.android.server.pdb.PersistentDataBlockService.this.computeAndWriteDigestLocked();
            }
        }

        public boolean getOemUnlockEnabled() {
            com.android.server.pdb.PersistentDataBlockService.this.enforceOemUnlockReadPermission();
            return com.android.server.pdb.PersistentDataBlockService.this.doGetOemUnlockEnabled();
        }

        public int getFlashLockState() {
            byte b;
            com.android.server.pdb.PersistentDataBlockService.this.enforceOemUnlockReadPermission();
            java.lang.String locked = android.os.SystemProperties.get(com.android.server.pdb.PersistentDataBlockService.FLASH_LOCK_PROP);
            switch (locked.hashCode()) {
                case 48:
                    b = !locked.equals(com.android.server.pdb.PersistentDataBlockService.FLASH_LOCK_UNLOCKED) ? (byte) -1 : (byte) 1;
                    break;
                case 49:
                    b = !locked.equals(com.android.server.pdb.PersistentDataBlockService.FLASH_LOCK_LOCKED) ? (byte) -1 : (byte) 0;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                default:
                    return -1;
            }
        }

        private static java.lang.String getVerifiedBootState() {
            return android.os.SystemProperties.get(com.android.server.pdb.PersistentDataBlockService.VERIFIED_BOOT_STATE);
        }

        public int getDataBlockSize() {
            int totalDataSizeLocked;
            enforcePersistentDataBlockAccess();
            try {
                java.io.DataInputStream inputStream = new java.io.DataInputStream(new java.io.FileInputStream(new java.io.File(com.android.server.pdb.PersistentDataBlockService.this.mDataBlockFile)));
                try {
                    synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                        totalDataSizeLocked = com.android.server.pdb.PersistentDataBlockService.this.getTotalDataSizeLocked(inputStream);
                    }
                    return totalDataSizeLocked;
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "error reading data block size");
                    return 0;
                } finally {
                    libcore.io.IoUtils.closeQuietly(inputStream);
                }
            } catch (java.io.FileNotFoundException e2) {
                android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "partition not available");
                return 0;
            }
        }

        private void enforcePersistentDataBlockAccess() {
            if (com.android.server.pdb.PersistentDataBlockService.this.mContext.checkCallingPermission("android.permission.ACCESS_PDB_STATE") != 0) {
                com.android.server.pdb.PersistentDataBlockService.this.enforceUid(android.os.Binder.getCallingUid());
            }
        }

        private void enforceConfigureFrpPermissionOrPersistentDataBlockAccess() {
            if (!com.android.server.pdb.PersistentDataBlockService.this.mFrpEnforced) {
                enforcePersistentDataBlockAccess();
            } else if (com.android.server.pdb.PersistentDataBlockService.this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_FACTORY_RESET_PROTECTION") == -1) {
                enforcePersistentDataBlockAccess();
            }
        }

        public long getMaximumDataBlockSize() {
            com.android.server.pdb.PersistentDataBlockService.this.enforceUid(android.os.Binder.getCallingUid());
            return com.android.server.pdb.PersistentDataBlockService.this.doGetMaximumDataBlockSize();
        }

        public boolean hasFrpCredentialHandle() {
            enforceConfigureFrpPermissionOrPersistentDataBlockAccess();
            try {
                return com.android.server.pdb.PersistentDataBlockService.this.mInternalService.getFrpCredentialHandle() != null;
            } catch (java.lang.IllegalStateException e) {
                android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "error reading frp handle", e);
                throw new java.lang.UnsupportedOperationException("cannot read frp credential");
            }
        }

        public java.lang.String getPersistentDataPackageName() {
            enforcePersistentDataBlockAccess();
            return com.android.server.pdb.PersistentDataBlockService.this.mContext.getString(android.R.string.config_qualified_networks_service_class);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.pdb.PersistentDataBlockService.this.mContext, com.android.server.pdb.PersistentDataBlockService.TAG, pw)) {
                pw.println("mDataBlockFile: " + com.android.server.pdb.PersistentDataBlockService.this.mDataBlockFile);
                pw.println("mIsFileBacked: " + com.android.server.pdb.PersistentDataBlockService.this.mIsFileBacked);
                pw.println("mInitDoneSignal: " + com.android.server.pdb.PersistentDataBlockService.this.mInitDoneSignal);
                pw.println("mAllowedUid: " + com.android.server.pdb.PersistentDataBlockService.this.mAllowedUid);
                pw.println("mBlockDeviceSize: " + com.android.server.pdb.PersistentDataBlockService.this.mBlockDeviceSize);
                synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                    pw.println("mIsWritable: " + com.android.server.pdb.PersistentDataBlockService.this.mIsWritable);
                }
                printFrpStatus(pw, false);
            }
        }

        public boolean isFactoryResetProtectionActive() {
            return com.android.server.pdb.PersistentDataBlockService.this.isFrpActive();
        }

        public boolean deactivateFactoryResetProtection(byte[] secret) {
            com.android.server.pdb.PersistentDataBlockService.this.enforceConfigureFrpPermission();
            return com.android.server.pdb.PersistentDataBlockService.this.deactivateFrp(secret);
        }

        public boolean setFactoryResetProtectionSecret(byte[] secret) {
            com.android.server.pdb.PersistentDataBlockService.this.enforceConfigureFrpPermission();
            com.android.server.pdb.PersistentDataBlockService.this.enforceUid(android.os.Binder.getCallingUid());
            if (secret == null || secret.length != 32) {
                throw new java.lang.IllegalArgumentException("Invalid FRP secret: " + java.util.HexFormat.of().formatHex(secret));
            }
            com.android.server.pdb.PersistentDataBlockService.this.enforceFactoryResetProtectionInactive();
            return com.android.server.pdb.PersistentDataBlockService.this.updateFrpSecret(secret);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceFactoryResetProtectionInactive() {
        if (this.mFrpEnforced && isFrpActive()) {
            android.util.Slog.w(TAG, "Attempt to update PDB was blocked because FRP is active.");
            throw new java.lang.SecurityException("FRP is active");
        }
    }

    boolean isUpgradingFromPreVRelease() {
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (packageManagerInternal == null) {
            android.util.Slog.e(TAG, "Unable to retrieve PackageManagerInternal");
            return false;
        }
        return packageManagerInternal.isUpgradingFromLowerThan(35);
    }

    private class InternalService implements com.android.server.pdb.PersistentDataBlockManagerInternal {
        private InternalService() {
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public void setFrpCredentialHandle(byte[] handle) {
            writeInternal(handle, com.android.server.pdb.PersistentDataBlockService.this.getFrpCredentialDataOffset(), com.android.server.pdb.PersistentDataBlockService.MAX_FRP_CREDENTIAL_HANDLE_SIZE);
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public byte[] getFrpCredentialHandle() {
            return readInternal(com.android.server.pdb.PersistentDataBlockService.this.getFrpCredentialDataOffset(), com.android.server.pdb.PersistentDataBlockService.MAX_FRP_CREDENTIAL_HANDLE_SIZE);
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public void setTestHarnessModeData(byte[] data) {
            writeInternal(data, com.android.server.pdb.PersistentDataBlockService.this.getTestHarnessModeDataOffset(), com.android.server.pdb.PersistentDataBlockService.MAX_TEST_MODE_DATA_SIZE);
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public byte[] getTestHarnessModeData() {
            byte[] data = readInternal(com.android.server.pdb.PersistentDataBlockService.this.getTestHarnessModeDataOffset(), com.android.server.pdb.PersistentDataBlockService.MAX_TEST_MODE_DATA_SIZE);
            if (data == null) {
                return new byte[0];
            }
            return data;
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public void clearTestHarnessModeData() {
            int size = java.lang.Math.min(com.android.server.pdb.PersistentDataBlockService.MAX_TEST_MODE_DATA_SIZE, getTestHarnessModeData().length) + 4;
            writeDataBuffer(com.android.server.pdb.PersistentDataBlockService.this.getTestHarnessModeDataOffset(), java.nio.ByteBuffer.allocate(size));
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public int getAllowedUid() {
            return com.android.server.pdb.PersistentDataBlockService.this.mAllowedUid;
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public boolean deactivateFactoryResetProtectionWithoutSecret() {
            synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                com.android.server.pdb.PersistentDataBlockService.this.mFrpActive = false;
                com.android.server.pdb.PersistentDataBlockService.this.setOldSettingForBackworkCompatibility(com.android.server.pdb.PersistentDataBlockService.this.mFrpActive);
            }
            return true;
        }

        private void writeInternal(byte[] data, long offset, int dataLength) {
            boolean z = true;
            com.android.internal.util.Preconditions.checkArgument(data == null || data.length > 0, "data must be null or non-empty");
            if (data != null && data.length > dataLength) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkArgument(z, "data must not be longer than " + dataLength);
            java.nio.ByteBuffer dataBuffer = java.nio.ByteBuffer.allocate(dataLength + 4);
            dataBuffer.putInt(data != null ? data.length : 0);
            if (data != null) {
                dataBuffer.put(data);
            }
            dataBuffer.flip();
            writeDataBuffer(offset, dataBuffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean writeDataBuffer(long offset, java.nio.ByteBuffer dataBuffer) {
            synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                if (!com.android.server.pdb.PersistentDataBlockService.this.mIsWritable) {
                    return false;
                }
                try {
                    java.nio.channels.FileChannel channel = com.android.server.pdb.PersistentDataBlockService.this.getBlockOutputChannel();
                    try {
                        channel.position(offset);
                        channel.write(dataBuffer);
                        channel.force(true);
                        if (channel != null) {
                            channel.close();
                        }
                        return com.android.server.pdb.PersistentDataBlockService.this.computeAndWriteDigestLocked();
                    } catch (java.lang.Throwable th) {
                        if (channel != null) {
                            try {
                                channel.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.pdb.PersistentDataBlockService.TAG, "unable to access persistent partition", e);
                    return false;
                }
            }
        }

        private byte[] readInternal(long offset, int maxLength) {
            if (!com.android.server.pdb.PersistentDataBlockService.this.enforceChecksumValidity()) {
                throw new java.lang.IllegalStateException("invalid checksum");
            }
            try {
                java.io.DataInputStream inputStream = new java.io.DataInputStream(new java.io.FileInputStream(new java.io.File(com.android.server.pdb.PersistentDataBlockService.this.mDataBlockFile)));
                try {
                    try {
                        synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                            inputStream.skip(offset);
                            int length = inputStream.readInt();
                            if (length > 0 && length <= maxLength) {
                                byte[] bytes = new byte[length];
                                inputStream.readFully(bytes);
                                return bytes;
                            }
                            libcore.io.IoUtils.closeQuietly(inputStream);
                            return null;
                        }
                    } finally {
                        libcore.io.IoUtils.closeQuietly(inputStream);
                    }
                } catch (java.io.IOException e) {
                    throw new java.lang.IllegalStateException("persistent partition not readable", e);
                }
            } catch (java.io.FileNotFoundException e2) {
                throw new java.lang.IllegalStateException("persistent partition not available");
            }
        }

        @Override // com.android.server.pdb.PersistentDataBlockManagerInternal
        public void forceOemUnlockEnabled(boolean enabled) {
            synchronized (com.android.server.pdb.PersistentDataBlockService.this.mLock) {
                com.android.server.pdb.PersistentDataBlockService.this.doSetOemUnlockEnabledLocked(enabled);
                com.android.server.pdb.PersistentDataBlockService.this.computeAndWriteDigestLocked();
            }
        }
    }
}
