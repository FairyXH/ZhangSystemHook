package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class KeyValueAdbBackupEngine {
    private static final java.lang.String BACKUP_KEY_VALUE_BACKUP_DATA_FILENAME_SUFFIX = ".data";
    private static final java.lang.String BACKUP_KEY_VALUE_BLANK_STATE_FILENAME = "blank_state";
    private static final java.lang.String BACKUP_KEY_VALUE_DIRECTORY_NAME = "key_value_dir";
    private static final java.lang.String BACKUP_KEY_VALUE_NEW_STATE_FILENAME_SUFFIX = ".new";
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "KeyValueAdbBackupEngine";
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private android.os.ParcelFileDescriptor mBackupData;
    private final java.io.File mBackupDataName;
    private com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final java.io.File mBlankStateName;
    private final android.content.pm.PackageInfo mCurrentPackage;
    private final java.io.File mDataDir;
    private final java.io.File mManifestFile;
    private android.os.ParcelFileDescriptor mNewState;
    private final java.io.File mNewStateName;
    private final java.io.OutputStream mOutput;
    private final android.content.pm.PackageManager mPackageManager;
    private android.os.ParcelFileDescriptor mSavedState;
    private final java.io.File mStateDir;

    public KeyValueAdbBackupEngine(java.io.OutputStream output, android.content.pm.PackageInfo packageInfo, com.android.server.backup.UserBackupManagerService backupManagerService, android.content.pm.PackageManager packageManager, java.io.File baseStateDir, java.io.File dataDir) {
        this.mOutput = output;
        this.mCurrentPackage = packageInfo;
        this.mBackupManagerService = backupManagerService;
        this.mPackageManager = packageManager;
        this.mDataDir = dataDir;
        this.mStateDir = new java.io.File(baseStateDir, BACKUP_KEY_VALUE_DIRECTORY_NAME);
        this.mStateDir.mkdirs();
        java.lang.String pkg = this.mCurrentPackage.packageName;
        this.mBlankStateName = new java.io.File(this.mStateDir, BACKUP_KEY_VALUE_BLANK_STATE_FILENAME);
        this.mBackupDataName = new java.io.File(this.mDataDir, pkg + ".data");
        this.mNewStateName = new java.io.File(this.mStateDir, pkg + ".new");
        this.mManifestFile = new java.io.File(this.mDataDir, com.android.server.backup.UserBackupManagerService.BACKUP_MANIFEST_FILENAME);
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
    }

    public void backupOnePackage() throws java.io.IOException {
        android.app.IBackupAgent agent;
        android.content.pm.ApplicationInfo targetApp = this.mCurrentPackage.applicationInfo;
        try {
            try {
                prepareBackupFiles(this.mCurrentPackage.packageName);
                agent = bindToAgent(targetApp);
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.e(TAG, "Failed creating files for package " + this.mCurrentPackage.packageName + " will ignore package. " + e);
            }
            if (agent == null) {
                android.util.Slog.e(TAG, "Failed binding to BackupAgent for package " + this.mCurrentPackage.packageName);
            } else if (invokeAgentForAdbBackup(this.mCurrentPackage.packageName, agent)) {
                writeBackupData();
            } else {
                android.util.Slog.e(TAG, "Backup Failed for package " + this.mCurrentPackage.packageName);
            }
        } finally {
            cleanup();
        }
    }

    private void prepareBackupFiles(java.lang.String packageName) throws java.io.FileNotFoundException {
        this.mSavedState = android.os.ParcelFileDescriptor.open(this.mBlankStateName, android.hardware.audio.common.V2_0.AudioFormat.MP2);
        this.mBackupData = android.os.ParcelFileDescriptor.open(this.mBackupDataName, 1006632960);
        if (!android.os.SELinux.restorecon(this.mBackupDataName)) {
            android.util.Slog.e(TAG, "SELinux restorecon failed on " + this.mBackupDataName);
        }
        this.mNewState = android.os.ParcelFileDescriptor.open(this.mNewStateName, 1006632960);
    }

    private android.app.IBackupAgent bindToAgent(android.content.pm.ApplicationInfo targetApp) {
        try {
            return this.mBackupManagerService.bindToAgentSynchronous(targetApp, 0, 0);
        } catch (java.lang.SecurityException e) {
            android.util.Slog.e(TAG, "error in binding to agent for package " + targetApp.packageName + ". " + e);
            return null;
        }
    }

    private boolean invokeAgentForAdbBackup(java.lang.String packageName, android.app.IBackupAgent agent) {
        int token = this.mBackupManagerService.generateRandomIntegerToken();
        long kvBackupAgentTimeoutMillis = this.mAgentTimeoutParameters.getKvBackupAgentTimeoutMillis();
        try {
            this.mBackupManagerService.prepareOperationTimeout(token, kvBackupAgentTimeoutMillis, null, 0);
            agent.doBackup(this.mSavedState, this.mBackupData, this.mNewState, Long.MAX_VALUE, new com.android.server.backup.remote.ServiceBackupCallback(this.mBackupManagerService.getBackupManagerBinder(), token), 0);
            if (!this.mBackupManagerService.waitUntilOperationComplete(token)) {
                android.util.Slog.e(TAG, "Key-value backup failed on package " + packageName);
                return false;
            }
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error invoking agent for backup on " + packageName + ". " + e);
            return false;
        }
    }

    class KeyValueAdbBackupDataCopier implements java.lang.Runnable {
        private final android.content.pm.PackageInfo mPackage;
        private final android.os.ParcelFileDescriptor mPipe;
        private final int mToken;

        KeyValueAdbBackupDataCopier(android.content.pm.PackageInfo pack, android.os.ParcelFileDescriptor pipe, int token) throws java.io.IOException {
            this.mPackage = pack;
            this.mPipe = android.os.ParcelFileDescriptor.dup(pipe.getFileDescriptor());
            this.mToken = token;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    android.app.backup.FullBackupDataOutput output = new android.app.backup.FullBackupDataOutput(this.mPipe);
                    com.android.server.backup.fullbackup.AppMetadataBackupWriter writer = new com.android.server.backup.fullbackup.AppMetadataBackupWriter(output, com.android.server.backup.KeyValueAdbBackupEngine.this.mPackageManager);
                    writer.backupManifest(this.mPackage, com.android.server.backup.KeyValueAdbBackupEngine.this.mManifestFile, com.android.server.backup.KeyValueAdbBackupEngine.this.mDataDir, "k", null, false);
                    com.android.server.backup.KeyValueAdbBackupEngine.this.mManifestFile.delete();
                    android.app.backup.FullBackup.backupToTar(this.mPackage.packageName, "k", (java.lang.String) null, com.android.server.backup.KeyValueAdbBackupEngine.this.mDataDir.getAbsolutePath(), com.android.server.backup.KeyValueAdbBackupEngine.this.mBackupDataName.getAbsolutePath(), output);
                    try {
                        java.io.FileOutputStream out = new java.io.FileOutputStream(this.mPipe.getFileDescriptor());
                        byte[] buf = new byte[4];
                        out.write(buf);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.backup.KeyValueAdbBackupEngine.TAG, "Unable to finalize backup stream!");
                    }
                    try {
                        com.android.server.backup.KeyValueAdbBackupEngine.this.mBackupManagerService.getBackupManagerBinder().opComplete(this.mToken, 0L);
                    } catch (android.os.RemoteException e2) {
                    }
                } catch (java.io.IOException e3) {
                    android.util.Slog.e(com.android.server.backup.KeyValueAdbBackupEngine.TAG, "Error running full backup for " + this.mPackage.packageName + ". " + e3);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(this.mPipe);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00bc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void writeBackupData() throws java.lang.Throwable {
        /*
            r13 = this;
            java.lang.String r0 = "KeyValueAdbBackupEngine"
            com.android.server.backup.UserBackupManagerService r1 = r13.mBackupManagerService
            int r1 = r1.generateRandomIntegerToken()
            com.android.server.backup.BackupAgentTimeoutParameters r2 = r13.mAgentTimeoutParameters
            long r8 = r2.getKvBackupAgentTimeoutMillis()
            r2 = 0
            r10 = 0
            r11 = 1
            android.os.ParcelFileDescriptor[] r3 = android.os.ParcelFileDescriptor.createPipe()     // Catch: java.lang.Throwable -> L7b java.io.IOException -> L7d
            r12 = r3
            com.android.server.backup.UserBackupManagerService r2 = r13.mBackupManagerService     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r6 = 0
            r7 = 0
            r3 = r1
            r4 = r8
            r2.prepareOperationTimeout(r3, r4, r6, r7)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            com.android.server.backup.KeyValueAdbBackupEngine$KeyValueAdbBackupDataCopier r2 = new com.android.server.backup.KeyValueAdbBackupEngine$KeyValueAdbBackupDataCopier     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            android.content.pm.PackageInfo r3 = r13.mCurrentPackage     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r4 = r12[r11]     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r2.<init>(r3, r4, r1)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r3 = r12[r11]     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r3.close()     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r3 = 0
            r12[r11] = r3     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.lang.Thread r3 = new java.lang.Thread     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.lang.String r4 = "key-value-app-data-runner"
            r3.<init>(r2, r4)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r3.start()     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r4 = r12[r10]     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.io.OutputStream r5 = r13.mOutput     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            com.android.server.backup.utils.FullBackupUtils.routeSocketDataToOutput(r4, r5)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            com.android.server.backup.UserBackupManagerService r4 = r13.mBackupManagerService     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            boolean r4 = r4.waitUntilOperationComplete(r1)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            if (r4 != 0) goto L64
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            r4.<init>()     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.lang.String r5 = "Full backup failed on package "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            android.content.pm.PackageInfo r5 = r13.mCurrentPackage     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.lang.String r5 = r5.packageName     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
            android.util.Slog.e(r0, r4)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L79
        L64:
            java.io.OutputStream r0 = r13.mOutput
            r0.flush()
            if (r12 == 0) goto Lb4
            r0 = r12[r10]
            libcore.io.IoUtils.closeQuietly(r0)
            r0 = r12[r11]
        L72:
            libcore.io.IoUtils.closeQuietly(r0)
            goto Lb4
        L76:
            r0 = move-exception
            r2 = r12
            goto Lb5
        L79:
            r2 = move-exception
            goto L80
        L7b:
            r0 = move-exception
            goto Lb5
        L7d:
            r3 = move-exception
            r12 = r2
            r2 = r3
        L80:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L76
            r3.<init>()     // Catch: java.lang.Throwable -> L76
            java.lang.String r4 = "Error backing up "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L76
            android.content.pm.PackageInfo r4 = r13.mCurrentPackage     // Catch: java.lang.Throwable -> L76
            java.lang.String r4 = r4.packageName     // Catch: java.lang.Throwable -> L76
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L76
            java.lang.String r4 = ": "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L76
            java.lang.StringBuilder r3 = r3.append(r2)     // Catch: java.lang.Throwable -> L76
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L76
            android.util.Slog.e(r0, r3)     // Catch: java.lang.Throwable -> L76
            java.io.OutputStream r0 = r13.mOutput
            r0.flush()
            if (r12 == 0) goto Lb4
            r0 = r12[r10]
            libcore.io.IoUtils.closeQuietly(r0)
            r0 = r12[r11]
            goto L72
        Lb4:
            return
        Lb5:
            java.io.OutputStream r3 = r13.mOutput
            r3.flush()
            if (r2 == 0) goto Lc6
            r3 = r2[r10]
            libcore.io.IoUtils.closeQuietly(r3)
            r3 = r2[r11]
            libcore.io.IoUtils.closeQuietly(r3)
        Lc6:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.KeyValueAdbBackupEngine.writeBackupData():void");
    }

    private void cleanup() {
        this.mBackupManagerService.tearDownAgentAndKill(this.mCurrentPackage.applicationInfo);
        this.mBlankStateName.delete();
        this.mNewStateName.delete();
        this.mBackupDataName.delete();
    }
}
