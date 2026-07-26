package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class FullRestoreEngine extends com.android.server.backup.restore.RestoreEngine {
    private static final int FAIL_DEFAULT = -100;
    private static final int RESTORE_FAIL_AGENT_ERROR = -152;
    private static final int RESTORE_FAIL_PACKAGE_NOT_INSTALL = -153;
    private static final int RESTORE_FAIL_PACKAGE_NOT_MATCH = -151;
    private static final int RESTORE_OPTION_CLEAN_AND_CLOSE_AGENT = 1;
    private android.app.IBackupAgent mAgent;
    private java.lang.String mAgentPackage;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    final boolean mAllowApks;
    private long mAppVersion;
    private final com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    private com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    final byte[] mBuffer;
    private final java.util.HashSet<java.lang.String> mClearedPackages;
    private java.lang.String mCurrentRestorePkgName;
    private final com.android.server.backup.restore.RestoreDeleteObserver mDeleteObserver;
    final int mEphemeralOpToken;
    public com.android.server.backup.restore.IFullRestoreEngineExt mFullRestoreEngineExt;
    private com.android.server.backup.restore.IFullRestoreEngineWrapper mFullRestoreEngineWrapper;
    private final boolean mIsAdbRestore;
    private boolean mIsOplusRestore;
    private final java.util.HashMap<java.lang.String, android.content.pm.Signature[]> mManifestSignatures;
    final android.app.backup.IBackupManagerMonitor mMonitor;
    private final com.android.server.backup.BackupRestoreTask mMonitorTask;
    private com.android.server.backup.fullbackup.FullBackupObbConnection mObbConnection;
    private android.app.backup.IFullBackupRestoreObserver mObserver;
    final android.content.pm.PackageInfo mOnlyPackage;
    private final com.android.server.backup.OperationStorage mOperationStorage;
    private final java.util.HashMap<java.lang.String, java.lang.String> mPackageInstallers;
    private final java.util.HashMap<java.lang.String, com.android.server.backup.restore.RestorePolicy> mPackagePolicies;
    private android.os.ParcelFileDescriptor[] mPipes;
    private boolean mPipesClosed;
    private final java.lang.Object mPipesLock;
    private com.android.server.backup.FileMetadata mReadOnlyParent;
    private int mRestoreType;
    private android.content.pm.ApplicationInfo mTargetApp;
    private final int mUserId;
    private byte[] mWidgetData;

    public FullRestoreEngine(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, com.android.server.backup.BackupRestoreTask monitorTask, android.app.backup.IFullBackupRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, android.content.pm.PackageInfo onlyPackage, boolean allowApks, int ephemeralOpToken, boolean isAdbRestore, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        this.mDeleteObserver = new com.android.server.backup.restore.RestoreDeleteObserver();
        this.mObbConnection = null;
        this.mPackagePolicies = new java.util.HashMap<>();
        this.mPackageInstallers = new java.util.HashMap<>();
        this.mManifestSignatures = new java.util.HashMap<>();
        this.mClearedPackages = new java.util.HashSet<>();
        this.mPipes = null;
        this.mPipesLock = new java.lang.Object();
        this.mWidgetData = null;
        this.mReadOnlyParent = null;
        this.mIsOplusRestore = false;
        this.mCurrentRestorePkgName = null;
        this.mRestoreType = 1;
        this.mFullRestoreEngineExt = (com.android.server.backup.restore.IFullRestoreEngineExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.restore.IFullRestoreEngineExt.class).base(this).create();
        this.mFullRestoreEngineWrapper = new com.android.server.backup.restore.FullRestoreEngine.FullRestoreEngineWrapper();
        this.mBackupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mEphemeralOpToken = ephemeralOpToken;
        this.mMonitorTask = monitorTask;
        this.mObserver = observer;
        this.mMonitor = monitor;
        this.mBackupManagerMonitorEventSender = new com.android.server.backup.utils.BackupManagerMonitorEventSender(monitor);
        this.mOnlyPackage = onlyPackage;
        this.mAllowApks = allowApks;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
        this.mIsAdbRestore = isAdbRestore;
        this.mUserId = backupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
        if (com.android.server.backup.Flags.enableMaxSizeWritesToPipes()) {
            this.mBuffer = new byte[65536];
        } else {
            this.mBuffer = new byte[32768];
        }
        this.mIsOplusRestore = this.mBackupEligibilityRules.getBackupDestination() == 3;
    }

    FullRestoreEngine() {
        this.mDeleteObserver = new com.android.server.backup.restore.RestoreDeleteObserver();
        this.mObbConnection = null;
        this.mPackagePolicies = new java.util.HashMap<>();
        this.mPackageInstallers = new java.util.HashMap<>();
        this.mManifestSignatures = new java.util.HashMap<>();
        this.mClearedPackages = new java.util.HashSet<>();
        this.mPipes = null;
        this.mPipesLock = new java.lang.Object();
        this.mWidgetData = null;
        this.mReadOnlyParent = null;
        this.mIsOplusRestore = false;
        this.mCurrentRestorePkgName = null;
        this.mRestoreType = 1;
        this.mFullRestoreEngineExt = (com.android.server.backup.restore.IFullRestoreEngineExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.restore.IFullRestoreEngineExt.class).base(this).create();
        this.mFullRestoreEngineWrapper = new com.android.server.backup.restore.FullRestoreEngine.FullRestoreEngineWrapper();
        this.mIsAdbRestore = false;
        this.mAllowApks = false;
        this.mEphemeralOpToken = 0;
        this.mUserId = 0;
        this.mBackupEligibilityRules = null;
        this.mAgentTimeoutParameters = null;
        this.mBuffer = null;
        this.mBackupManagerService = null;
        this.mOperationStorage = null;
        this.mMonitor = null;
        this.mMonitorTask = null;
        this.mOnlyPackage = null;
    }

    public android.app.IBackupAgent getAgent() {
        return this.mAgent;
    }

    public byte[] getWidgetData() {
        return this.mWidgetData;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 24, insn: 0x02e0: MOVE (r4 I:??[OBJECT, ARRAY]) = (r24 I:??[OBJECT, ARRAY]), block:B:87:0x02d4 */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0334  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x041e  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x07b4 A[Catch: IOException -> 0x08bc, TRY_LEAVE, TryCatch #41 {IOException -> 0x08bc, blocks: (B:270:0x07b4, B:268:0x07aa), top: B:403:0x07aa }] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0851  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x085f A[Catch: IOException -> 0x090a, TryCatch #4 {IOException -> 0x090a, blocks: (B:306:0x0841, B:313:0x085f, B:315:0x08a0, B:323:0x08d6, B:326:0x08e4, B:328:0x08ea, B:330:0x08ed, B:333:0x08fa, B:329:0x08ec, B:334:0x08fc), top: B:371:0x0841 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00dc A[Catch: IOException -> 0x01df, TRY_ENTER, TryCatch #51 {IOException -> 0x01df, blocks: (B:31:0x00dc, B:34:0x00e6, B:53:0x0178, B:59:0x01f7, B:69:0x0250, B:71:0x0258), top: B:415:0x00da }] */
    /* JADX WARN: Removed duplicated region for block: B:323:0x08d6 A[Catch: IOException -> 0x090a, TryCatch #4 {IOException -> 0x090a, blocks: (B:306:0x0841, B:313:0x085f, B:315:0x08a0, B:323:0x08d6, B:326:0x08e4, B:328:0x08ea, B:330:0x08ed, B:333:0x08fa, B:329:0x08ec, B:334:0x08fc), top: B:371:0x0841 }] */
    /* JADX WARN: Removed duplicated region for block: B:352:0x0979  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x09c0  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x09c4  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x031c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:451:? A[ADDED_TO_REGION, REMOVE, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01ed A[Catch: IOException -> 0x0944, TRY_ENTER, TRY_LEAVE, TryCatch #37 {IOException -> 0x0944, blocks: (B:7:0x0028, B:9:0x002f, B:15:0x004a, B:29:0x00d2, B:57:0x01ed, B:65:0x0230), top: B:400:0x0028 }] */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v22 */
    /* JADX WARN: Type inference failed for: r12v23 */
    /* JADX WARN: Type inference failed for: r12v24 */
    /* JADX WARN: Type inference failed for: r12v25 */
    /* JADX WARN: Type inference failed for: r12v26 */
    /* JADX WARN: Type inference failed for: r12v27 */
    /* JADX WARN: Type inference failed for: r12v28 */
    /* JADX WARN: Type inference failed for: r12v29 */
    /* JADX WARN: Type inference failed for: r12v30 */
    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v12 */
    /* JADX WARN: Type inference failed for: r14v22 */
    /* JADX WARN: Type inference failed for: r14v23 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r14v9 */
    /* JADX WARN: Type inference failed for: r2v38 */
    /* JADX WARN: Type inference failed for: r2v40 */
    /* JADX WARN: Type inference failed for: r2v44 */
    /* JADX WARN: Type inference failed for: r2v45 */
    /* JADX WARN: Type inference failed for: r2v47 */
    /* JADX WARN: Type inference failed for: r2v48 */
    /* JADX WARN: Type inference failed for: r2v53 */
    /* JADX WARN: Type inference failed for: r2v54 */
    /* JADX WARN: Type inference failed for: r2v55 */
    /* JADX WARN: Type inference failed for: r2v58, types: [com.android.server.backup.utils.TarBackupReader] */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r46v0 */
    /* JADX WARN: Type inference failed for: r46v1 */
    /* JADX WARN: Type inference failed for: r46v10 */
    /* JADX WARN: Type inference failed for: r46v11 */
    /* JADX WARN: Type inference failed for: r46v12 */
    /* JADX WARN: Type inference failed for: r46v13 */
    /* JADX WARN: Type inference failed for: r46v14 */
    /* JADX WARN: Type inference failed for: r46v15 */
    /* JADX WARN: Type inference failed for: r46v16 */
    /* JADX WARN: Type inference failed for: r46v17 */
    /* JADX WARN: Type inference failed for: r46v18 */
    /* JADX WARN: Type inference failed for: r46v19 */
    /* JADX WARN: Type inference failed for: r46v2 */
    /* JADX WARN: Type inference failed for: r46v20 */
    /* JADX WARN: Type inference failed for: r46v21 */
    /* JADX WARN: Type inference failed for: r46v22 */
    /* JADX WARN: Type inference failed for: r46v23 */
    /* JADX WARN: Type inference failed for: r46v26 */
    /* JADX WARN: Type inference failed for: r46v27 */
    /* JADX WARN: Type inference failed for: r46v28 */
    /* JADX WARN: Type inference failed for: r46v29 */
    /* JADX WARN: Type inference failed for: r46v3 */
    /* JADX WARN: Type inference failed for: r46v30 */
    /* JADX WARN: Type inference failed for: r46v31 */
    /* JADX WARN: Type inference failed for: r46v32 */
    /* JADX WARN: Type inference failed for: r46v33 */
    /* JADX WARN: Type inference failed for: r46v34 */
    /* JADX WARN: Type inference failed for: r46v35 */
    /* JADX WARN: Type inference failed for: r46v36 */
    /* JADX WARN: Type inference failed for: r46v37 */
    /* JADX WARN: Type inference failed for: r46v38 */
    /* JADX WARN: Type inference failed for: r46v39 */
    /* JADX WARN: Type inference failed for: r46v4 */
    /* JADX WARN: Type inference failed for: r46v40 */
    /* JADX WARN: Type inference failed for: r46v41 */
    /* JADX WARN: Type inference failed for: r46v42 */
    /* JADX WARN: Type inference failed for: r46v43 */
    /* JADX WARN: Type inference failed for: r46v44 */
    /* JADX WARN: Type inference failed for: r46v45 */
    /* JADX WARN: Type inference failed for: r46v46 */
    /* JADX WARN: Type inference failed for: r46v47 */
    /* JADX WARN: Type inference failed for: r46v5 */
    /* JADX WARN: Type inference failed for: r46v6 */
    /* JADX WARN: Type inference failed for: r46v7 */
    /* JADX WARN: Type inference failed for: r46v8 */
    /* JADX WARN: Type inference failed for: r46v9 */
    /* JADX WARN: Type inference failed for: r47v0, types: [com.android.server.backup.restore.FullRestoreEngine] */
    /* JADX WARN: Type inference failed for: r7v0, types: [com.android.server.backup.utils.TarBackupReader] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v15 */
    /* JADX WARN: Type inference failed for: r7v16, types: [android.content.pm.PackageInfo] */
    /* JADX WARN: Type inference failed for: r7v2, types: [android.content.pm.PackageInfo] */
    /* JADX WARN: Type inference failed for: r7v27, types: [android.content.pm.PackageInfo] */
    /* JADX WARN: Type inference failed for: r7v28 */
    /* JADX WARN: Type inference failed for: r7v29 */
    /* JADX WARN: Type inference failed for: r7v32 */
    /* JADX WARN: Type inference failed for: r7v33 */
    /* JADX WARN: Type inference failed for: r7v34 */
    /* JADX WARN: Type inference failed for: r7v35 */
    /* JADX WARN: Type inference failed for: r7v36 */
    /* JADX WARN: Type inference failed for: r7v37 */
    /* JADX WARN: Type inference failed for: r7v38 */
    /* JADX WARN: Type inference failed for: r7v39 */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v40 */
    /* JADX WARN: Type inference failed for: r7v41 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v9 */
    /* JADX WARN: Type inference failed for: r9v0, types: [android.app.backup.IBackupManagerMonitor] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v11 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v18 */
    /* JADX WARN: Type inference failed for: r9v19 */
    /* JADX WARN: Type inference failed for: r9v20 */
    /* JADX WARN: Type inference failed for: r9v21 */
    /* JADX WARN: Type inference failed for: r9v22 */
    /* JADX WARN: Type inference failed for: r9v23 */
    /* JADX WARN: Type inference failed for: r9v24 */
    /* JADX WARN: Type inference failed for: r9v25 */
    /* JADX WARN: Type inference failed for: r9v26 */
    /* JADX WARN: Type inference failed for: r9v27 */
    /* JADX WARN: Type inference failed for: r9v34 */
    /* JADX WARN: Type inference failed for: r9v35 */
    /* JADX WARN: Type inference failed for: r9v38 */
    /* JADX WARN: Type inference failed for: r9v39 */
    /* JADX WARN: Type inference failed for: r9v44 */
    /* JADX WARN: Type inference failed for: r9v48 */
    /* JADX WARN: Type inference failed for: r9v49 */
    /* JADX WARN: Type inference failed for: r9v50 */
    /* JADX WARN: Type inference failed for: r9v52 */
    /* JADX WARN: Type inference failed for: r9v54 */
    /* JADX WARN: Type inference failed for: r9v55 */
    /* JADX WARN: Type inference failed for: r9v56 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean restoreOneFile(java.io.InputStream r48, boolean r49, byte[] r50, android.content.pm.PackageInfo r51, boolean r52, int r53, android.app.backup.IBackupManagerMonitor r54) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 2514
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.restore.FullRestoreEngine.restoreOneFile(java.io.InputStream, boolean, byte[], android.content.pm.PackageInfo, boolean, int, android.app.backup.IBackupManagerMonitor):boolean");
    }

    static /* synthetic */ void lambda$restoreOneFile$0(long bytesRead) {
    }

    boolean shouldSkipReadOnlyDir(com.android.server.backup.FileMetadata info) {
        if (isValidParent(this.mReadOnlyParent, info)) {
            return true;
        }
        if (isReadOnlyDir(info)) {
            this.mReadOnlyParent = info;
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Skipping restore of " + info.path + " and its contents as read-only dirs are currently not supported.");
            return true;
        }
        this.mReadOnlyParent = null;
        return false;
    }

    private void logBMMEvent(int eventId, android.content.pm.PackageInfo pkgInfo) {
        if (com.android.server.backup.Flags.enableIncreasedBmmLoggingForRestoreAtInstall()) {
            this.mBackupManagerMonitorEventSender.monitorEvent(eventId, pkgInfo, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.OPERATION_TYPE", 1));
        }
    }

    private static boolean isValidParent(com.android.server.backup.FileMetadata parentDir, com.android.server.backup.FileMetadata childDir) {
        return parentDir != null && childDir.packageName.equals(parentDir.packageName) && childDir.domain.equals(parentDir.domain) && childDir.path.startsWith(getPathWithTrailingSeparator(parentDir.path));
    }

    private static java.lang.String getPathWithTrailingSeparator(java.lang.String path) {
        return path.endsWith(java.io.File.separator) ? path : path + java.io.File.separator;
    }

    private static boolean isReadOnlyDir(com.android.server.backup.FileMetadata file) {
        return file.type == 2 && (file.mode & ((long) android.system.OsConstants.S_IWUSR)) == 0;
    }

    private void setUpPipes() throws java.io.IOException {
        synchronized (this.mPipesLock) {
            this.mPipes = android.os.ParcelFileDescriptor.createPipe();
            this.mPipesClosed = false;
        }
    }

    private void tearDownPipes() {
        synchronized (this.mPipesLock) {
            if (!this.mPipesClosed && this.mPipes != null) {
                try {
                    this.mPipes[0].close();
                    this.mPipes[1].close();
                    this.mPipesClosed = true;
                } catch (java.io.IOException e) {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Couldn't close agent pipes", e);
                }
            }
        }
    }

    private void tearDownAgent(android.content.pm.ApplicationInfo app, boolean doRestoreFinished) {
        if (this.mAgent != null) {
            if (doRestoreFinished) {
                try {
                    int token = this.mBackupManagerService.generateRandomIntegerToken();
                    long fullBackupAgentTimeoutMillis = this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
                    com.android.server.backup.restore.AdbRestoreFinishedLatch latch = new com.android.server.backup.restore.AdbRestoreFinishedLatch(this.mBackupManagerService, this.mOperationStorage, token);
                    this.mBackupManagerService.prepareOperationTimeout(token, fullBackupAgentTimeoutMillis, latch, 1);
                    if (this.mTargetApp.processName.equals("system")) {
                        java.lang.Runnable runner = new com.android.server.backup.restore.AdbRestoreFinishedRunnable(this.mAgent, token, this.mBackupManagerService);
                        new java.lang.Thread(runner, "restore-sys-finished-runner").start();
                    } else {
                        this.mAgent.doRestoreFinished(token, this.mBackupManagerService.getBackupManagerBinder());
                    }
                    latch.await();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Lost app trying to shut down");
                }
            }
            this.mBackupManagerService.tearDownAgentAndKill(app);
            this.mAgent = null;
        }
    }

    void handleTimeout() {
        if (this.mIsOplusRestore) {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "handle restore timeout");
            tearDownAgent(this.mTargetApp, false);
        }
        tearDownPipes();
        setResult(-2);
        setRunning(false);
    }

    private boolean isRestorableFile(com.android.server.backup.FileMetadata info) {
        if (this.mBackupEligibilityRules.getBackupDestination() == 1) {
            return true;
        }
        if ("c".equals(info.domain)) {
            return false;
        }
        return (com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD.equals(info.domain) && info.path.startsWith("no_backup/")) ? false : true;
    }

    private static boolean isCanonicalFilePath(java.lang.String path) {
        if (path.contains("..") || path.contains("//")) {
            return false;
        }
        return true;
    }

    private boolean shouldForceClearAppDataOnFullRestore(java.lang.String packageName) {
        java.lang.String packageListString = android.provider.Settings.Secure.getStringForUser(this.mBackupManagerService.getContext().getContentResolver(), "packages_to_clear_data_before_full_restore", this.mUserId);
        if (android.text.TextUtils.isEmpty(packageListString)) {
            return false;
        }
        java.util.List<java.lang.String> packages = java.util.Arrays.asList(packageListString.split(";"));
        return packages.contains(packageName);
    }

    void sendOnRestorePackage(java.lang.String name) {
        if (this.mObserver != null) {
            try {
                this.mObserver.onRestorePackage(name);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "full restore observer went away: restorePackage");
                this.mObserver = null;
            }
        }
    }

    public void setRestoreType(int restoreType) {
        this.mRestoreType = restoreType;
    }

    public com.android.server.backup.restore.IFullRestoreEngineWrapper getWrapper() {
        return this.mFullRestoreEngineWrapper;
    }

    private class FullRestoreEngineWrapper implements com.android.server.backup.restore.IFullRestoreEngineWrapper {
        private FullRestoreEngineWrapper() {
        }

        @Override // com.android.server.backup.restore.IFullRestoreEngineWrapper
        public com.android.server.backup.restore.IFullRestoreEngineExt getExtImpl() {
            return com.android.server.backup.restore.FullRestoreEngine.this.mFullRestoreEngineExt;
        }

        @Override // com.android.server.backup.restore.IFullRestoreEngineWrapper
        public com.android.server.backup.UserBackupManagerService getUserBackupManagerService() {
            return com.android.server.backup.restore.FullRestoreEngine.this.mBackupManagerService;
        }
    }
}
