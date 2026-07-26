package com.android.server.backup.keyvalue;

/* JADX INFO: loaded from: classes.dex */
public class KeyValueBackupTask implements com.android.server.backup.BackupRestoreTask, java.lang.Runnable {
    private static final java.lang.String BLANK_STATE_FILE_NAME = "blank_state";
    public static final java.lang.String NEW_STATE_FILE_SUFFIX = ".new";
    static final java.lang.String NO_DATA_END_SENTINEL = "@end@";
    private static final java.lang.String PM_PACKAGE = "@pm@";
    public static final java.lang.String STAGING_FILE_SUFFIX = ".data";
    private static final java.lang.String SUCCESS_STATE_SUBDIR = "backing-up";
    private static final java.lang.String TAG = "KVBT";
    private static final java.util.concurrent.atomic.AtomicInteger THREAD_COUNT = new java.util.concurrent.atomic.AtomicInteger();
    private static final int THREAD_PRIORITY = 10;
    private android.app.IBackupAgent mAgent;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private android.os.ParcelFileDescriptor mBackupData;
    private java.io.File mBackupDataFile;
    private final com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final java.io.File mBlankStateFile;
    private final android.os.ConditionVariable mCancelAcknowledged = new android.os.ConditionVariable(false);
    private volatile boolean mCancelled = false;
    private final int mCurrentOpToken;
    private android.content.pm.PackageInfo mCurrentPackage;
    private final java.io.File mDataDirectory;
    private com.android.server.backup.fullbackup.PerformFullTransportBackupTask mFullBackupTask;
    private boolean mHasDataToBackup;
    private final com.android.server.backup.DataChangedJournal mJournal;
    private android.os.ParcelFileDescriptor mNewState;
    private java.io.File mNewStateFile;
    private boolean mNonIncremental;
    private final com.android.server.backup.OperationStorage mOperationStorage;
    private final java.util.List<java.lang.String> mOriginalQueue;
    private final android.content.pm.PackageManager mPackageManager;
    private volatile com.android.server.backup.remote.RemoteCall mPendingCall;
    private final java.util.List<java.lang.String> mPendingFullBackups;
    private final java.util.List<java.lang.String> mQueue;
    private final java.lang.Object mQueueLock;
    private final com.android.server.backup.keyvalue.KeyValueBackupReporter mReporter;
    private android.os.ParcelFileDescriptor mSavedState;
    private java.io.File mSavedStateFile;
    private final java.io.File mStateDirectory;
    private final com.android.server.backup.internal.OnTaskFinishedListener mTaskFinishedListener;
    private final com.android.server.backup.transport.TransportConnection mTransportConnection;
    private final int mUserId;
    private final boolean mUserInitiated;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface StateTransaction {
        public static final int COMMIT_NEW = 0;
        public static final int DISCARD_ALL = 2;
        public static final int DISCARD_NEW = 1;
    }

    public static com.android.server.backup.keyvalue.KeyValueBackupTask start(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String transportDirName, java.util.List<java.lang.String> queue, com.android.server.backup.DataChangedJournal dataChangedJournal, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, com.android.server.backup.internal.OnTaskFinishedListener listener, java.util.List<java.lang.String> pendingFullBackups, boolean userInitiated, boolean nonIncremental, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        com.android.server.backup.keyvalue.KeyValueBackupReporter reporter = new com.android.server.backup.keyvalue.KeyValueBackupReporter(backupManagerService, observer, new com.android.server.backup.utils.BackupManagerMonitorEventSender(monitor));
        com.android.server.backup.keyvalue.KeyValueBackupTask task = new com.android.server.backup.keyvalue.KeyValueBackupTask(backupManagerService, operationStorage, transportConnection, transportDirName, queue, dataChangedJournal, reporter, listener, pendingFullBackups, userInitiated, nonIncremental, backupEligibilityRules);
        java.lang.Thread thread = new java.lang.Thread(task, "key-value-backup-" + THREAD_COUNT.incrementAndGet());
        thread.start();
        com.android.server.backup.keyvalue.KeyValueBackupReporter.onNewThread(thread.getName());
        return task;
    }

    public KeyValueBackupTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String transportDirName, java.util.List<java.lang.String> queue, com.android.server.backup.DataChangedJournal journal, com.android.server.backup.keyvalue.KeyValueBackupReporter reporter, com.android.server.backup.internal.OnTaskFinishedListener taskFinishedListener, java.util.List<java.lang.String> pendingFullBackups, boolean userInitiated, boolean nonIncremental, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        this.mBackupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mPackageManager = backupManagerService.getPackageManager();
        this.mTransportConnection = transportConnection;
        this.mOriginalQueue = queue;
        this.mQueue = new java.util.ArrayList(queue);
        this.mJournal = journal;
        this.mReporter = reporter;
        this.mTaskFinishedListener = taskFinishedListener;
        this.mPendingFullBackups = pendingFullBackups;
        this.mUserInitiated = userInitiated;
        this.mNonIncremental = nonIncremental;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
        this.mStateDirectory = new java.io.File(backupManagerService.getBaseStateDir(), transportDirName);
        this.mDataDirectory = this.mBackupManagerService.getDataDir();
        this.mCurrentOpToken = backupManagerService.generateRandomIntegerToken();
        this.mQueueLock = this.mBackupManagerService.getQueueLock();
        this.mBlankStateFile = new java.io.File(this.mStateDirectory, BLANK_STATE_FILE_NAME);
        this.mUserId = backupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
    }

    private void registerTask() {
        this.mOperationStorage.registerOperation(this.mCurrentOpToken, 0, this, 2);
    }

    private void unregisterTask() {
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }

    @Override // java.lang.Runnable
    public void run() throws java.lang.Throwable {
        android.os.Process.setThreadPriority(10);
        this.mHasDataToBackup = false;
        java.util.Set<java.lang.String> backedUpApps = new java.util.HashSet<>();
        int status = 0;
        try {
            startTask();
            while (!this.mQueue.isEmpty() && !this.mCancelled) {
                java.lang.String packageName = this.mQueue.remove(0);
                try {
                    if ("@pm@".equals(packageName)) {
                        backupPm();
                    } else {
                        backupPackage(packageName);
                    }
                    setSuccessState(packageName, true);
                    backedUpApps.add(packageName);
                } catch (com.android.server.backup.keyvalue.AgentException e) {
                    setSuccessState(packageName, false);
                    if (e.isTransitory()) {
                        this.mBackupManagerService.dataChangedImpl(packageName);
                    }
                }
            }
            informTransportOfUnchangedApps(backedUpApps);
        } catch (com.android.server.backup.keyvalue.TaskException e2) {
            if (e2.isStateCompromised()) {
                this.mBackupManagerService.resetBackupState(this.mStateDirectory);
            }
            revertTask();
            status = e2.getStatus();
        }
        finishTask(status);
    }

    private void informTransportOfUnchangedApps(java.util.Set<java.lang.String> appsBackedUp) {
        java.lang.String[] succeedingPackages = getSucceedingPackages();
        if (succeedingPackages == null) {
            return;
        }
        int flags = 8;
        if (this.mUserInitiated) {
            flags = 8 | 1;
        }
        boolean noDataPackageEncountered = false;
        try {
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.informTransportOfEmptyBackups()");
            for (java.lang.String packageName : succeedingPackages) {
                if (appsBackedUp.contains(packageName)) {
                    android.util.Log.v(TAG, "Skipping package which was backed up this time: " + packageName);
                } else {
                    try {
                        android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfo(packageName, 0);
                        if (!isEligibleForNoDataCall(packageInfo)) {
                            clearStatus(packageName);
                        } else {
                            sendNoDataChangedTo(transport, packageInfo, flags);
                            noDataPackageEncountered = true;
                        }
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        clearStatus(packageName);
                    }
                }
            }
            if (noDataPackageEncountered) {
                android.content.pm.PackageInfo endSentinal = new android.content.pm.PackageInfo();
                endSentinal.packageName = NO_DATA_END_SENTINEL;
                sendNoDataChangedTo(transport, endSentinal, flags);
            }
        } catch (android.os.RemoteException | com.android.server.backup.transport.TransportNotAvailableException e2) {
            android.util.Log.e(TAG, "Could not inform transport of all unchanged apps", e2);
        }
    }

    private boolean isEligibleForNoDataCall(android.content.pm.PackageInfo packageInfo) {
        return this.mBackupEligibilityRules.appIsKeyValueOnly(packageInfo) && this.mBackupEligibilityRules.appIsRunningAndEligibleForBackupWithTransport(this.mTransportConnection, packageInfo.packageName);
    }

    private void sendNoDataChangedTo(com.android.server.backup.transport.BackupTransportClient transport, android.content.pm.PackageInfo packageInfo, int flags) throws android.os.RemoteException {
        try {
            android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(this.mBlankStateFile, android.hardware.audio.common.V2_0.AudioFormat.MP2);
            try {
                int result = transport.performBackup(packageInfo, pfd, flags);
                if (result != -1000 && result != -1001) {
                    transport.finishBackup();
                    return;
                }
                android.util.Log.w(TAG, "Aborting informing transport of unchanged apps, transport errored");
            } finally {
                libcore.io.IoUtils.closeQuietly(pfd);
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Log.e(TAG, "Unable to find blank state file, aborting unchanged apps signal.");
        }
    }

    private java.lang.String[] getSucceedingPackages() {
        java.io.File stateDirectory = getTopLevelSuccessStateDirectory(false);
        if (stateDirectory == null) {
            return null;
        }
        return stateDirectory.list();
    }

    private void setSuccessState(java.lang.String packageName, boolean success) {
        java.io.File successStateFile = getSuccessStateFileFor(packageName);
        if (successStateFile != null && successStateFile.exists() != success) {
            if (!success) {
                clearStatus(packageName, successStateFile);
                return;
            }
            try {
                if (!successStateFile.createNewFile()) {
                    android.util.Log.w(TAG, "Unable to permanently record success for " + packageName);
                }
            } catch (java.io.IOException e) {
                android.util.Log.w(TAG, "Unable to permanently record success for " + packageName, e);
            }
        }
    }

    private void clearStatus(java.lang.String packageName) {
        java.io.File successStateFile = getSuccessStateFileFor(packageName);
        if (successStateFile == null) {
            return;
        }
        clearStatus(packageName, successStateFile);
    }

    private void clearStatus(java.lang.String packageName, java.io.File successStateFile) {
        if (successStateFile.exists() && !successStateFile.delete()) {
            android.util.Log.w(TAG, "Unable to remove status file for " + packageName);
        }
    }

    private java.io.File getSuccessStateFileFor(java.lang.String packageName) {
        java.io.File stateDirectory = getTopLevelSuccessStateDirectory(true);
        if (stateDirectory == null) {
            return null;
        }
        return new java.io.File(stateDirectory, packageName);
    }

    private java.io.File getTopLevelSuccessStateDirectory(boolean createIfMissing) {
        java.io.File directory = new java.io.File(this.mStateDirectory, SUCCESS_STATE_SUBDIR);
        if (!directory.exists() && createIfMissing && !directory.mkdirs()) {
            android.util.Log.e(TAG, "Unable to create backing-up state directory");
            return null;
        }
        return directory;
    }

    private int sendDataToTransport(android.content.pm.PackageInfo packageInfo) throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException {
        try {
            return sendDataToTransport();
        } catch (java.io.IOException e) {
            this.mReporter.onAgentDataError(packageInfo.packageName, e);
            throw com.android.server.backup.keyvalue.TaskException.causedBy(e);
        }
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long unusedResult) {
    }

    private void startTask() throws com.android.server.backup.keyvalue.TaskException {
        if (this.mBackupManagerService.isBackupOperationInProgress()) {
            this.mReporter.onSkipBackup();
            throw com.android.server.backup.keyvalue.TaskException.create();
        }
        this.mFullBackupTask = createFullBackupTask(this.mPendingFullBackups);
        registerTask();
        if (this.mQueue.isEmpty() && this.mPendingFullBackups.isEmpty()) {
            this.mReporter.onEmptyQueueAtStart();
            return;
        }
        boolean backupPm = this.mQueue.remove("@pm@") || !this.mNonIncremental;
        if (backupPm) {
            this.mQueue.add(0, "@pm@");
        } else {
            this.mReporter.onSkipPm();
        }
        this.mReporter.onQueueReady(this.mQueue);
        java.io.File pmState = new java.io.File(this.mStateDirectory, "@pm@");
        try {
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.startTask()");
            java.lang.String transportName = transport.name();
            if (transportName.contains("EncryptedLocalTransport")) {
                this.mNonIncremental = true;
            }
            this.mReporter.onTransportReady(transportName);
            if (pmState.length() <= 0) {
                this.mReporter.onInitializeTransport(transportName);
                this.mBackupManagerService.resetBackupState(this.mStateDirectory);
                int status = transport.initializeDevice();
                this.mReporter.onTransportInitialized(status);
                if (status != 0) {
                    throw com.android.server.backup.keyvalue.TaskException.stateCompromised();
                }
            }
        } catch (com.android.server.backup.keyvalue.TaskException e) {
            throw e;
        } catch (java.lang.Exception e2) {
            this.mReporter.onInitializeTransportError(e2);
            throw com.android.server.backup.keyvalue.TaskException.stateCompromised();
        }
    }

    private com.android.server.backup.fullbackup.PerformFullTransportBackupTask createFullBackupTask(java.util.List<java.lang.String> packages) {
        return new com.android.server.backup.fullbackup.PerformFullTransportBackupTask(this.mBackupManagerService, this.mOperationStorage, this.mTransportConnection, null, (java.lang.String[]) packages.toArray(new java.lang.String[packages.size()]), false, null, new java.util.concurrent.CountDownLatch(1), this.mReporter.getObserver(), this.mReporter.getMonitor(), this.mTaskFinishedListener, this.mUserInitiated, this.mBackupEligibilityRules);
    }

    private void backupPm() throws com.android.server.backup.keyvalue.TaskException {
        this.mReporter.onStartPackageBackup("@pm@");
        this.mCurrentPackage = new android.content.pm.PackageInfo();
        this.mCurrentPackage.packageName = "@pm@";
        try {
            try {
                extractPmAgentData(this.mCurrentPackage);
                int status = sendDataToTransport(this.mCurrentPackage);
                cleanUpAgentForTransportStatus(status);
            } catch (com.android.server.backup.keyvalue.TaskException e) {
                throw com.android.server.backup.keyvalue.TaskException.stateCompromised(e);
            }
        } catch (com.android.server.backup.keyvalue.AgentException | com.android.server.backup.keyvalue.TaskException e2) {
            this.mReporter.onExtractPmAgentDataError(e2);
            cleanUpAgentForError(e2);
            if (e2 instanceof com.android.server.backup.keyvalue.TaskException) {
                throw ((com.android.server.backup.keyvalue.TaskException) e2);
            }
            throw com.android.server.backup.keyvalue.TaskException.stateCompromised(e2);
        }
    }

    private void backupPackage(java.lang.String packageName) throws com.android.server.backup.keyvalue.BackupException {
        this.mReporter.onStartPackageBackup(packageName);
        this.mCurrentPackage = getPackageForBackup(packageName);
        try {
            extractAgentData(this.mCurrentPackage);
            com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender = new com.android.server.backup.utils.BackupManagerMonitorEventSender(this.mReporter.getMonitor());
            mBackupManagerMonitorEventSender.monitorAgentLoggingResults(this.mCurrentPackage, this.mAgent);
            int status = sendDataToTransport(this.mCurrentPackage);
            cleanUpAgentForTransportStatus(status);
        } catch (com.android.server.backup.keyvalue.AgentException | com.android.server.backup.keyvalue.TaskException e) {
            cleanUpAgentForError(e);
            throw e;
        }
    }

    private android.content.pm.PackageInfo getPackageForBackup(java.lang.String packageName) throws com.android.server.backup.keyvalue.AgentException {
        try {
            android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfoAsUser(packageName, 134217728, this.mUserId);
            android.content.pm.ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (!this.mBackupEligibilityRules.appIsEligibleForBackup(applicationInfo)) {
                this.mReporter.onPackageNotEligibleForBackup(packageName);
                throw com.android.server.backup.keyvalue.AgentException.permanent();
            }
            if (this.mBackupEligibilityRules.appGetsFullBackup(packageInfo)) {
                this.mReporter.onPackageEligibleForFullBackup(packageName);
                throw com.android.server.backup.keyvalue.AgentException.permanent();
            }
            if (this.mBackupEligibilityRules.appIsStopped(applicationInfo)) {
                this.mReporter.onPackageStopped(packageName);
                throw com.android.server.backup.keyvalue.AgentException.permanent();
            }
            return packageInfo;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            this.mReporter.onAgentUnknown(packageName);
            throw com.android.server.backup.keyvalue.AgentException.permanent(e);
        }
    }

    private android.app.IBackupAgent bindAgent(android.content.pm.PackageInfo packageInfo) throws com.android.server.backup.keyvalue.AgentException {
        java.lang.String packageName = packageInfo.packageName;
        try {
            android.app.IBackupAgent agent = this.mBackupManagerService.bindToAgentSynchronous(packageInfo.applicationInfo, 0, this.mBackupEligibilityRules.getBackupDestination());
            if (agent == null) {
                this.mReporter.onAgentError(packageName);
                throw com.android.server.backup.keyvalue.AgentException.transitory();
            }
            return agent;
        } catch (java.lang.SecurityException e) {
            this.mReporter.onBindAgentError(packageName, e);
            throw com.android.server.backup.keyvalue.AgentException.transitory(e);
        }
    }

    private void finishTask(int status) {
        for (java.lang.String packageName : this.mQueue) {
            this.mBackupManagerService.dataChangedImpl(packageName);
        }
        if (this.mJournal != null && !this.mJournal.delete()) {
            this.mReporter.onJournalDeleteFailed(this.mJournal);
        }
        java.lang.String transportName = null;
        long currentToken = this.mBackupManagerService.getCurrentToken();
        if (this.mHasDataToBackup && status == 0 && currentToken == 0) {
            try {
                com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.finishTask()");
                transportName = transport.name();
                this.mBackupManagerService.setCurrentToken(transport.getCurrentRestoreSet());
                this.mBackupManagerService.writeRestoreTokens();
            } catch (java.lang.Exception e) {
                this.mReporter.onSetCurrentTokenError(e);
            }
        }
        synchronized (this.mQueueLock) {
            this.mBackupManagerService.setBackupRunning(false);
            if (status == -1001) {
                this.mReporter.onTransportNotInitialized(transportName);
                try {
                    triggerTransportInitializationLocked();
                } catch (java.lang.Exception e2) {
                    this.mReporter.onPendingInitializeTransportError(e2);
                    status = -1000;
                }
            }
        }
        unregisterTask();
        this.mReporter.onTaskFinished();
        if (this.mCancelled) {
            this.mCancelAcknowledged.open();
        }
        if (!this.mCancelled && status == 0 && this.mFullBackupTask != null && !this.mPendingFullBackups.isEmpty()) {
            this.mReporter.onStartFullBackup(this.mPendingFullBackups);
            new java.lang.Thread(this.mFullBackupTask, "full-transport-requested").start();
            return;
        }
        if (this.mFullBackupTask != null) {
            this.mFullBackupTask.unregisterTask();
        }
        this.mTaskFinishedListener.onFinished("KVBT.finishTask()");
        this.mReporter.onBackupFinished(getBackupFinishedStatus(this.mCancelled, status));
        this.mBackupManagerService.getWakelock().release();
    }

    private int getBackupFinishedStatus(boolean cancelled, int transportStatus) {
        if (cancelled) {
            return -2003;
        }
        switch (transportStatus) {
            case -1005:
            case com.android.server.job.JobSchedulerShellCommand.CMD_ERR_CONSTRAINTS /* -1002 */:
            case 0:
                return 0;
            default:
                return -1000;
        }
    }

    private void triggerTransportInitializationLocked() throws java.lang.Exception {
        com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.triggerTransportInitializationLocked");
        this.mBackupManagerService.getPendingInits().add(transport.name());
        deletePmStateFile();
        this.mBackupManagerService.backupNow();
    }

    private void deletePmStateFile() {
        new java.io.File(this.mStateDirectory, "@pm@").delete();
    }

    private void extractPmAgentData(android.content.pm.PackageInfo packageInfo) throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException {
        com.android.internal.util.Preconditions.checkArgument(packageInfo.packageName.equals("@pm@"));
        android.app.backup.BackupAgent pmAgent = this.mBackupManagerService.makeMetadataAgentWithEligibilityRules(this.mBackupEligibilityRules);
        this.mAgent = android.app.IBackupAgent.Stub.asInterface(pmAgent.onBind());
        extractAgentData(packageInfo, this.mAgent);
    }

    private void extractAgentData(android.content.pm.PackageInfo packageInfo) throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException {
        this.mBackupManagerService.setWorkSource(new android.os.WorkSource(packageInfo.applicationInfo.uid));
        try {
            this.mAgent = bindAgent(packageInfo);
            extractAgentData(packageInfo, this.mAgent);
        } finally {
            this.mBackupManagerService.setWorkSource(null);
        }
    }

    private void extractAgentData(android.content.pm.PackageInfo packageInfo, final android.app.IBackupAgent agent) throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException {
        java.lang.String packageName = packageInfo.packageName;
        this.mReporter.onExtractAgentData(packageName);
        this.mSavedStateFile = new java.io.File(this.mStateDirectory, packageName);
        this.mBackupDataFile = new java.io.File(this.mDataDirectory, packageName + STAGING_FILE_SUFFIX);
        this.mNewStateFile = new java.io.File(this.mStateDirectory, packageName + NEW_STATE_FILE_SUFFIX);
        this.mReporter.onAgentFilesReady(this.mBackupDataFile);
        boolean callingAgent = false;
        try {
            java.io.File savedStateFileForAgent = this.mNonIncremental ? this.mBlankStateFile : this.mSavedStateFile;
            this.mSavedState = android.os.ParcelFileDescriptor.open(savedStateFileForAgent, android.hardware.audio.common.V2_0.AudioFormat.MP2);
            this.mBackupData = android.os.ParcelFileDescriptor.open(this.mBackupDataFile, 1006632960);
            this.mNewState = android.os.ParcelFileDescriptor.open(this.mNewStateFile, 1006632960);
            if (this.mUserId == 0 && !android.os.SELinux.restorecon(this.mBackupDataFile)) {
                this.mReporter.onRestoreconFailed(this.mBackupDataFile);
            }
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.extractAgentData()");
            final long quota = transport.getBackupQuota(packageName, false);
            final int transportFlags = transport.getTransportFlags();
            callingAgent = true;
            com.android.server.backup.remote.RemoteResult agentResult = remoteCall(new com.android.server.backup.remote.RemoteCallable() { // from class: com.android.server.backup.keyvalue.KeyValueBackupTask$$ExternalSyntheticLambda1
                @Override // com.android.server.backup.remote.RemoteCallable
                public final void call(java.lang.Object obj) throws android.os.RemoteException {
                    this.f$0.lambda$extractAgentData$0(agent, quota, transportFlags, (android.app.backup.IBackupCallback) obj);
                }
            }, this.mAgentTimeoutParameters.getKvBackupAgentTimeoutMillis(), "doBackup()");
            checkAgentResult(packageInfo, agentResult);
        } catch (java.lang.Exception e) {
            this.mReporter.onCallAgentDoBackupError(packageName, callingAgent, e);
            if (callingAgent) {
                throw com.android.server.backup.keyvalue.AgentException.transitory(e);
            }
            throw com.android.server.backup.keyvalue.TaskException.create();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$extractAgentData$0(android.app.IBackupAgent agent, long quota, int transportFlags, android.app.backup.IBackupCallback callback) throws android.os.RemoteException {
        agent.doBackup(this.mSavedState, this.mBackupData, this.mNewState, quota, callback, transportFlags);
    }

    private void checkAgentResult(android.content.pm.PackageInfo packageInfo, com.android.server.backup.remote.RemoteResult result) throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException {
        if (result == com.android.server.backup.remote.RemoteResult.FAILED_THREAD_INTERRUPTED) {
            this.mCancelled = true;
            this.mReporter.onAgentCancelled(packageInfo);
            throw com.android.server.backup.keyvalue.TaskException.create();
        }
        if (result == com.android.server.backup.remote.RemoteResult.FAILED_CANCELLED) {
            this.mReporter.onAgentCancelled(packageInfo);
            throw com.android.server.backup.keyvalue.TaskException.create();
        }
        if (result == com.android.server.backup.remote.RemoteResult.FAILED_TIMED_OUT) {
            this.mReporter.onAgentTimedOut(packageInfo);
            throw com.android.server.backup.keyvalue.AgentException.transitory();
        }
        com.android.internal.util.Preconditions.checkState(result.isPresent());
        long resultCode = result.get();
        if (resultCode == -1) {
            this.mReporter.onAgentResultError(packageInfo);
            throw com.android.server.backup.keyvalue.AgentException.transitory();
        }
        com.android.internal.util.Preconditions.checkState(resultCode == 0);
    }

    private void agentFail(android.app.IBackupAgent agent, java.lang.String message) {
        try {
            agent.fail(message);
        } catch (java.lang.Exception e) {
            this.mReporter.onFailAgentError(this.mCurrentPackage.packageName);
        }
    }

    private java.lang.String SHA1Checksum(byte[] input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
            byte[] checksum = md.digest(input);
            java.lang.StringBuilder string = new java.lang.StringBuilder(checksum.length * 2);
            for (byte item : checksum) {
                string.append(java.lang.Integer.toHexString(item));
            }
            return string.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            this.mReporter.onDigestError(e);
            return "00";
        }
    }

    private void writeWidgetPayloadIfAppropriate(java.io.FileDescriptor fd, java.lang.String pkgName) throws java.io.IOException {
        byte[] widgetState = com.android.server.AppWidgetBackupBridge.getWidgetState(pkgName, this.mUserId);
        java.io.File widgetFile = new java.io.File(this.mStateDirectory, pkgName + "_widget");
        boolean priorStateExists = widgetFile.exists();
        if (!priorStateExists && widgetState == null) {
            return;
        }
        this.mReporter.onWriteWidgetData(priorStateExists, widgetState);
        java.lang.String newChecksum = null;
        if (widgetState != null) {
            newChecksum = SHA1Checksum(widgetState);
            if (priorStateExists) {
                java.io.FileInputStream fin = new java.io.FileInputStream(widgetFile);
                try {
                    java.io.DataInputStream in = new java.io.DataInputStream(fin);
                    try {
                        java.lang.String priorChecksum = in.readUTF();
                        in.close();
                        fin.close();
                        if (java.util.Objects.equals(newChecksum, priorChecksum)) {
                            return;
                        }
                    } finally {
                    }
                } catch (java.lang.Throwable th) {
                    try {
                        fin.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
        android.app.backup.BackupDataOutput out = new android.app.backup.BackupDataOutput(fd);
        if (widgetState == null) {
            out.writeEntityHeader(com.android.server.backup.UserBackupManagerService.KEY_WIDGET_STATE, -1);
            widgetFile.delete();
            return;
        }
        java.io.FileOutputStream fout = new java.io.FileOutputStream(widgetFile);
        try {
            java.io.DataOutputStream stateOut = new java.io.DataOutputStream(fout);
            try {
                stateOut.writeUTF(newChecksum);
                stateOut.close();
                fout.close();
                out.writeEntityHeader(com.android.server.backup.UserBackupManagerService.KEY_WIDGET_STATE, widgetState.length);
                out.writeEntityData(widgetState, widgetState.length);
            } finally {
            }
        } catch (java.lang.Throwable th3) {
            try {
                fout.close();
            } catch (java.lang.Throwable th4) {
                th3.addSuppressed(th4);
            }
            throw th3;
        }
    }

    private int sendDataToTransport() throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException, java.io.IOException {
        com.android.internal.util.Preconditions.checkState(this.mBackupData != null);
        checkBackupData(this.mCurrentPackage.applicationInfo, this.mBackupDataFile);
        java.lang.String packageName = this.mCurrentPackage.packageName;
        writeWidgetPayloadIfAppropriate(this.mBackupData.getFileDescriptor(), packageName);
        boolean nonIncremental = this.mSavedStateFile.length() == 0;
        int status = transportPerformBackup(this.mCurrentPackage, this.mBackupDataFile, nonIncremental);
        handleTransportStatus(status, packageName, this.mBackupDataFile.length());
        return status;
    }

    private int transportPerformBackup(android.content.pm.PackageInfo packageInfo, java.io.File backupDataFile, boolean nonIncremental) throws com.android.server.backup.keyvalue.TaskException {
        java.lang.String packageName = packageInfo.packageName;
        long size = backupDataFile.length();
        if (size <= 0) {
            this.mReporter.onEmptyData(packageInfo);
            return 0;
        }
        this.mHasDataToBackup = true;
        try {
            android.os.ParcelFileDescriptor backupData = android.os.ParcelFileDescriptor.open(backupDataFile, 268435456);
            try {
                com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.transportPerformBackup()");
                this.mReporter.onTransportPerformBackup(packageName);
                int flags = getPerformBackupFlags(this.mUserInitiated, nonIncremental);
                int status = transport.performBackup(packageInfo, backupData, flags);
                if (status == 0) {
                    status = transport.finishBackup();
                } else if (status == -1001) {
                    this.mReporter.onTransportNotInitialized(transport.name());
                }
                if (backupData != null) {
                    backupData.close();
                }
                if (nonIncremental && status == -1006) {
                    this.mReporter.onPackageBackupNonIncrementalAndNonIncrementalRequired(packageName);
                    throw com.android.server.backup.keyvalue.TaskException.create();
                }
                return status;
            } finally {
            }
        } catch (java.lang.Exception e) {
            this.mReporter.onPackageBackupTransportError(packageName, e);
            throw com.android.server.backup.keyvalue.TaskException.causedBy(e);
        }
    }

    private void handleTransportStatus(int status, java.lang.String packageName, long size) throws com.android.server.backup.keyvalue.AgentException, com.android.server.backup.keyvalue.TaskException {
        if (status == 0) {
            this.mReporter.onPackageBackupComplete(packageName, size);
            return;
        }
        if (status == -1006) {
            this.mReporter.onPackageBackupNonIncrementalRequired(this.mCurrentPackage);
            this.mQueue.add(0, packageName);
        } else {
            if (status == -1002) {
                this.mReporter.onPackageBackupRejected(packageName);
                throw com.android.server.backup.keyvalue.AgentException.permanent();
            }
            if (status == -1005) {
                this.mReporter.onPackageBackupQuotaExceeded(packageName);
                agentDoQuotaExceeded(this.mAgent, packageName, size);
                throw com.android.server.backup.keyvalue.AgentException.permanent();
            }
            this.mReporter.onPackageBackupTransportFailure(packageName);
            throw com.android.server.backup.keyvalue.TaskException.forStatus(status);
        }
    }

    private void agentDoQuotaExceeded(final android.app.IBackupAgent agent, java.lang.String packageName, final long size) {
        if (agent != null) {
            try {
                com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.agentDoQuotaExceeded()");
                final long quota = transport.getBackupQuota(packageName, false);
                remoteCall(new com.android.server.backup.remote.RemoteCallable() { // from class: com.android.server.backup.keyvalue.KeyValueBackupTask$$ExternalSyntheticLambda0
                    @Override // com.android.server.backup.remote.RemoteCallable
                    public final void call(java.lang.Object obj) {
                        agent.doQuotaExceeded(size, quota, (android.app.backup.IBackupCallback) obj);
                    }
                }, this.mAgentTimeoutParameters.getQuotaExceededTimeoutMillis(), "doQuotaExceeded()");
            } catch (java.lang.Exception e) {
                this.mReporter.onAgentDoQuotaExceededError(e);
            }
        }
    }

    private void checkBackupData(android.content.pm.ApplicationInfo applicationInfo, java.io.File backupDataFile) throws com.android.server.backup.keyvalue.AgentException, java.io.IOException {
        if (applicationInfo == null || (applicationInfo.flags & 1) != 0) {
            return;
        }
        android.os.ParcelFileDescriptor backupData = android.os.ParcelFileDescriptor.open(backupDataFile, 268435456);
        try {
            android.app.backup.BackupDataInput backupDataInput = new android.app.backup.BackupDataInput(backupData.getFileDescriptor());
            while (backupDataInput.readNextHeader()) {
                java.lang.String key = backupDataInput.getKey();
                if (key != null && key.charAt(0) >= 65280) {
                    this.mReporter.onAgentIllegalKey(this.mCurrentPackage, key);
                    agentFail(this.mAgent, "Illegal backup key: " + key);
                    throw com.android.server.backup.keyvalue.AgentException.permanent();
                }
                backupDataInput.skipEntityData();
            }
            if (backupData != null) {
                backupData.close();
            }
        } catch (java.lang.Throwable th) {
            if (backupData != null) {
                try {
                    backupData.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private int getPerformBackupFlags(boolean z, boolean z2) {
        int i;
        if (z2) {
            i = 4;
        } else {
            i = 2;
        }
        return (z ? 1 : 0) | i;
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean cancelAll) {
        com.android.internal.util.Preconditions.checkArgument(cancelAll, "Can't partially cancel a key-value backup task");
        markCancel();
        waitCancel();
    }

    public void markCancel() {
        this.mReporter.onCancel();
        this.mCancelled = true;
        com.android.server.backup.remote.RemoteCall pendingCall = this.mPendingCall;
        if (pendingCall != null) {
            pendingCall.cancel();
        }
    }

    public void waitCancel() {
        this.mCancelAcknowledged.block();
    }

    private void revertTask() throws java.lang.Throwable {
        long delay;
        this.mReporter.onRevertTask();
        try {
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("KVBT.revertTask()");
            delay = transport.requestBackupTime();
        } catch (java.lang.Exception e) {
            this.mReporter.onTransportRequestBackupTimeError(e);
            delay = 0;
        }
        com.android.server.backup.KeyValueBackupJob.schedule(this.mBackupManagerService.getUserId(), this.mBackupManagerService.getContext(), delay, this.mBackupManagerService);
        for (java.lang.String packageName : this.mOriginalQueue) {
            this.mBackupManagerService.dataChangedImpl(packageName);
        }
    }

    private void cleanUpAgentForError(com.android.server.backup.keyvalue.BackupException exception) {
        cleanUpAgent(1);
    }

    private void cleanUpAgentForTransportStatus(int status) {
        switch (status) {
            case -1006:
                cleanUpAgent(2);
                return;
            case 0:
                cleanUpAgent(0);
                return;
            default:
                throw new java.lang.AssertionError();
        }
    }

    private void cleanUpAgent(int stateTransaction) {
        applyStateTransaction(stateTransaction);
        if (this.mBackupDataFile != null) {
            this.mBackupDataFile.delete();
        }
        this.mBlankStateFile.delete();
        this.mSavedStateFile = null;
        this.mBackupDataFile = null;
        this.mNewStateFile = null;
        tryCloseFileDescriptor(this.mSavedState, "old state");
        tryCloseFileDescriptor(this.mBackupData, "backup data");
        tryCloseFileDescriptor(this.mNewState, "new state");
        this.mSavedState = null;
        this.mBackupData = null;
        this.mNewState = null;
        if (this.mCurrentPackage.applicationInfo != null) {
            this.mBackupManagerService.unbindAgent(this.mCurrentPackage.applicationInfo);
        }
        this.mAgent = null;
    }

    private void applyStateTransaction(int stateTransaction) {
        switch (stateTransaction) {
            case 0:
                this.mNewStateFile.renameTo(this.mSavedStateFile);
                return;
            case 1:
                if (this.mNewStateFile != null) {
                    this.mNewStateFile.delete();
                    return;
                }
                return;
            case 2:
                this.mSavedStateFile.delete();
                this.mNewStateFile.delete();
                return;
            default:
                throw new java.lang.IllegalArgumentException("Unknown state transaction " + stateTransaction);
        }
    }

    private void tryCloseFileDescriptor(java.io.Closeable closeable, java.lang.String logName) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (java.io.IOException e) {
                this.mReporter.onCloseFileDescriptorError(logName);
            }
        }
    }

    private com.android.server.backup.remote.RemoteResult remoteCall(com.android.server.backup.remote.RemoteCallable<android.app.backup.IBackupCallback> remoteCallable, long timeoutMs, java.lang.String logIdentifier) throws android.os.RemoteException {
        this.mPendingCall = new com.android.server.backup.remote.RemoteCall(this.mCancelled, remoteCallable, timeoutMs);
        com.android.server.backup.remote.RemoteResult result = this.mPendingCall.call();
        this.mReporter.onRemoteCallReturned(result, logIdentifier);
        this.mPendingCall = null;
        return result;
    }
}
