package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public class FullBackupEngine {
    private static com.android.server.backup.fullbackup.IFullBackupEngineExt.IStaticExt sStaticFullBackupEngineExt = (com.android.server.backup.fullbackup.IFullBackupEngineExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.fullbackup.IFullBackupEngineExt.IStaticExt.class).create();
    private com.android.server.backup.UserBackupManagerService backupManagerService;
    private android.app.IBackupAgent mAgent;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    private final com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender;
    private com.android.server.backup.fullbackup.IFullBackupEngineExt mFullBackupEngineExt = (com.android.server.backup.fullbackup.IFullBackupEngineExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.fullbackup.IFullBackupEngineExt.class).base(this).create();
    private com.android.server.backup.fullbackup.IFullBackupEngineWrapper mFullBackupEngineWrapper = new com.android.server.backup.fullbackup.FullBackupEngine.FullBackupEngineWrapper();
    private boolean mIncludeApks;
    private final int mOpToken;
    private java.io.OutputStream mOutput;
    private final android.content.pm.PackageInfo mPkg;
    private com.android.server.backup.fullbackup.FullBackupPreflight mPreflightHook;
    private final long mQuota;
    private com.android.server.backup.BackupRestoreTask mTimeoutMonitor;
    private final int mTransportFlags;

    class FullBackupRunner implements java.lang.Runnable {
        private final android.app.IBackupAgent mAgent;
        private final java.io.File mFilesDir;
        private final boolean mIncludeApks;
        private final android.content.pm.PackageInfo mPackage;
        private final android.content.pm.PackageManager mPackageManager;
        private final android.os.ParcelFileDescriptor mPipe;
        private final int mToken;
        private final int mUserId;

        FullBackupRunner(com.android.server.backup.UserBackupManagerService userBackupManagerService, android.content.pm.PackageInfo packageInfo, android.app.IBackupAgent agent, android.os.ParcelFileDescriptor pipe, int token, boolean includeApks) throws java.io.IOException {
            this.mUserId = userBackupManagerService.getUserId();
            this.mPackageManager = com.android.server.backup.fullbackup.FullBackupEngine.this.backupManagerService.getPackageManager();
            this.mPackage = packageInfo;
            this.mAgent = agent;
            this.mPipe = android.os.ParcelFileDescriptor.dup(pipe.getFileDescriptor());
            this.mToken = token;
            this.mIncludeApks = includeApks;
            this.mFilesDir = userBackupManagerService.getDataDir();
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    try {
                        android.app.backup.FullBackupDataOutput output = new android.app.backup.FullBackupDataOutput(this.mPipe, -1L, com.android.server.backup.fullbackup.FullBackupEngine.this.mTransportFlags);
                        com.android.server.backup.fullbackup.AppMetadataBackupWriter appMetadataBackupWriter = new com.android.server.backup.fullbackup.AppMetadataBackupWriter(output, this.mPackageManager);
                        java.lang.String packageName = this.mPackage.packageName;
                        boolean isSharedStorage = com.android.server.backup.UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE.equals(packageName);
                        boolean writeApk = shouldWriteApk(this.mPackage.applicationInfo, this.mIncludeApks, isSharedStorage);
                        if (!isSharedStorage) {
                            java.io.File manifestFile = new java.io.File(this.mFilesDir, com.android.server.backup.UserBackupManagerService.BACKUP_MANIFEST_FILENAME);
                            appMetadataBackupWriter.backupManifest(this.mPackage, manifestFile, this.mFilesDir, writeApk);
                            manifestFile.delete();
                            byte[] widgetData = com.android.server.AppWidgetBackupBridge.getWidgetState(packageName, this.mUserId);
                            if (widgetData != null && widgetData.length > 0) {
                                java.io.File metadataFile = new java.io.File(this.mFilesDir, com.android.server.backup.UserBackupManagerService.BACKUP_METADATA_FILENAME);
                                appMetadataBackupWriter.backupWidget(this.mPackage, metadataFile, this.mFilesDir, widgetData);
                                metadataFile.delete();
                            }
                        }
                        if (writeApk) {
                            appMetadataBackupWriter.backupApk(this.mPackage);
                            appMetadataBackupWriter.backupObb(this.mUserId, this.mPackage);
                        }
                        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Calling doFullBackup() on " + packageName);
                        long timeout = isSharedStorage ? com.android.server.backup.fullbackup.FullBackupEngine.this.mAgentTimeoutParameters.getSharedBackupAgentTimeoutMillis() : com.android.server.backup.fullbackup.FullBackupEngine.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
                        if (com.android.server.backup.fullbackup.FullBackupEngine.this.mBackupEligibilityRules.getBackupDestination() == 3) {
                            com.android.server.backup.fullbackup.FullBackupEngine.this.backupManagerService.getWrapper().getExtImpl().registerOperationWithPackageForOplus(this.mToken, timeout, this.mPackage.packageName, com.android.server.backup.fullbackup.FullBackupEngine.this.mTimeoutMonitor, true);
                        } else {
                            com.android.server.backup.fullbackup.FullBackupEngine.this.backupManagerService.prepareOperationTimeout(this.mToken, timeout, com.android.server.backup.fullbackup.FullBackupEngine.this.mTimeoutMonitor, 0);
                        }
                        this.mAgent.doFullBackup(this.mPipe, com.android.server.backup.fullbackup.FullBackupEngine.this.mQuota, this.mToken, com.android.server.backup.fullbackup.FullBackupEngine.this.backupManagerService.getBackupManagerBinder(), com.android.server.backup.fullbackup.FullBackupEngine.this.mTransportFlags);
                        this.mPipe.close();
                    } catch (java.io.IOException e) {
                    }
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Remote agent vanished during full backup of " + this.mPackage.packageName, e2);
                    this.mPipe.close();
                } catch (java.io.IOException e3) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Error running full backup for " + this.mPackage.packageName, e3);
                    this.mPipe.close();
                }
            } finally {
            }
        }

        private boolean shouldWriteApk(android.content.pm.ApplicationInfo applicationInfo, boolean includeApks, boolean isSharedStorage) {
            boolean isSystemApp = (applicationInfo.flags & 1) != 0;
            boolean isUpdatedSystemApp = (applicationInfo.flags & 128) != 0;
            return includeApks && !isSharedStorage && (!isSystemApp || isUpdatedSystemApp);
        }
    }

    public FullBackupEngine(com.android.server.backup.UserBackupManagerService backupManagerService, java.io.OutputStream output, com.android.server.backup.fullbackup.FullBackupPreflight preflightHook, android.content.pm.PackageInfo pkg, boolean alsoApks, com.android.server.backup.BackupRestoreTask timeoutMonitor, long quota, int opToken, int transportFlags, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules, com.android.server.backup.utils.BackupManagerMonitorEventSender backupManagerMonitorEventSender) {
        this.backupManagerService = backupManagerService;
        this.mOutput = output;
        this.mPreflightHook = preflightHook;
        this.mPkg = pkg;
        this.mIncludeApks = alsoApks;
        this.mTimeoutMonitor = timeoutMonitor;
        this.mQuota = quota;
        this.mOpToken = opToken;
        this.mTransportFlags = transportFlags;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
        this.mBackupEligibilityRules = backupEligibilityRules;
        this.mBackupManagerMonitorEventSender = backupManagerMonitorEventSender;
    }

    public int preflightCheck() throws android.os.RemoteException {
        if (this.mPreflightHook == null) {
            return 0;
        }
        if (initializeAgent()) {
            int result = this.mPreflightHook.preflightFullBackup(this.mPkg, this.mAgent);
            this.mAgent.clearBackupRestoreEventLogger();
            return result;
        }
        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unable to bind to full agent for " + this.mPkg.packageName);
        return -1003;
    }

    public int backupOnePackage() throws android.os.RemoteException {
        int result = -1003;
        if (initializeAgent()) {
            android.os.ParcelFileDescriptor[] pipes = null;
            try {
                try {
                    try {
                        pipes = android.os.ParcelFileDescriptor.createPipe();
                        com.android.server.backup.fullbackup.FullBackupEngine.FullBackupRunner runner = new com.android.server.backup.fullbackup.FullBackupEngine.FullBackupRunner(this.backupManagerService, this.mPkg, this.mAgent, pipes[1], this.mOpToken, this.mIncludeApks);
                        pipes[1].close();
                        pipes[1] = null;
                        java.lang.Thread t = new java.lang.Thread(runner, "app-data-runner");
                        t.start();
                        getWrapper().getStaticExtImpl().routeSocketDataToOutput(pipes[0], this.mOutput, this.mPkg.packageName);
                        if (!this.backupManagerService.waitUntilOperationComplete(this.mOpToken)) {
                            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Full backup failed on package " + this.mPkg.packageName);
                        } else {
                            result = 0;
                        }
                        this.mBackupManagerMonitorEventSender.monitorAgentLoggingResults(this.mPkg, this.mAgent);
                        this.mOutput.flush();
                        if (pipes != null) {
                            if (pipes[0] != null) {
                                pipes[0].close();
                            }
                            if (pipes[1] != null) {
                                pipes[1].close();
                            }
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Error backing up " + this.mPkg.packageName + ": " + e.getMessage());
                        result = -1003;
                        this.mOutput.flush();
                        if (pipes != null) {
                            if (pipes[0] != null) {
                                pipes[0].close();
                            }
                            if (pipes[1] != null) {
                                pipes[1].close();
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    try {
                        this.mOutput.flush();
                        if (pipes != null) {
                            if (pipes[0] != null) {
                                pipes[0].close();
                            }
                            if (pipes[1] != null) {
                                pipes[1].close();
                            }
                        }
                    } catch (java.io.IOException e2) {
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Error bringing down backup stack");
                    }
                    throw th;
                }
            } catch (java.io.IOException e3) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Error bringing down backup stack");
                result = -1000;
            }
        } else {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unable to bind to full agent for " + this.mPkg.packageName);
        }
        if (this.backupManagerService.getWrapper().getExtImpl().needTearDown(this.mBackupEligibilityRules.getBackupDestination(), this.mPkg.applicationInfo) && !getWrapper().getExtImpl().hasSameAgentTask(this.backupManagerService, this.mPkg.applicationInfo)) {
            tearDown();
        }
        return result;
    }

    public void sendQuotaExceeded(final long backupDataBytes, final long quotaBytes) {
        if (initializeAgent()) {
            try {
                com.android.server.backup.remote.RemoteCall.execute(new com.android.server.backup.remote.RemoteCallable() { // from class: com.android.server.backup.fullbackup.FullBackupEngine$$ExternalSyntheticLambda0
                    @Override // com.android.server.backup.remote.RemoteCallable
                    public final void call(java.lang.Object obj) throws android.os.RemoteException {
                        this.f$0.lambda$sendQuotaExceeded$0(backupDataBytes, quotaBytes, (android.app.backup.IBackupCallback) obj);
                    }
                }, this.mAgentTimeoutParameters.getQuotaExceededTimeoutMillis());
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Remote exception while telling agent about quota exceeded");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendQuotaExceeded$0(long backupDataBytes, long quotaBytes, android.app.backup.IBackupCallback callback) throws android.os.RemoteException {
        this.mAgent.doQuotaExceeded(backupDataBytes, quotaBytes, callback);
    }

    private boolean initializeAgent() {
        if (this.mAgent == null) {
            this.mAgent = this.backupManagerService.bindToAgentSynchronous(this.mPkg.applicationInfo, 1, this.mBackupEligibilityRules.getBackupDestination());
        }
        return this.mAgent != null;
    }

    private void tearDown() {
        if (this.mPkg != null) {
            this.backupManagerService.tearDownAgentAndKill(this.mPkg.applicationInfo);
        }
    }

    public com.android.server.backup.fullbackup.IFullBackupEngineWrapper getWrapper() {
        return this.mFullBackupEngineWrapper;
    }

    private class FullBackupEngineWrapper implements com.android.server.backup.fullbackup.IFullBackupEngineWrapper {
        private FullBackupEngineWrapper() {
        }

        @Override // com.android.server.backup.fullbackup.IFullBackupEngineWrapper
        public com.android.server.backup.fullbackup.IFullBackupEngineExt getExtImpl() {
            return com.android.server.backup.fullbackup.FullBackupEngine.this.mFullBackupEngineExt;
        }

        @Override // com.android.server.backup.fullbackup.IFullBackupEngineWrapper
        public com.android.server.backup.UserBackupManagerService getUserBackupManagerService() {
            return com.android.server.backup.fullbackup.FullBackupEngine.this.backupManagerService;
        }

        @Override // com.android.server.backup.fullbackup.IFullBackupEngineWrapper
        public com.android.server.backup.fullbackup.IFullBackupEngineExt.IStaticExt getStaticExtImpl() {
            return com.android.server.backup.fullbackup.FullBackupEngine.sStaticFullBackupEngineExt;
        }
    }
}
