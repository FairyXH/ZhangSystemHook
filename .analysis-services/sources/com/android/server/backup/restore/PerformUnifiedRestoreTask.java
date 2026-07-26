package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class PerformUnifiedRestoreTask implements com.android.server.backup.BackupRestoreTask {
    private com.android.server.backup.UserBackupManagerService backupManagerService;
    private java.util.List<android.content.pm.PackageInfo> mAcceptSet;
    private android.app.IBackupAgent mAgent;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private java.lang.Boolean mAreVToUListsSet;
    private android.os.ParcelFileDescriptor mBackupData;
    private java.io.File mBackupDataName;
    private final com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    private com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender;
    private android.content.pm.PackageInfo mCurrentPackage;
    private boolean mDidLaunch;
    private final int mEphemeralOpToken;
    private boolean mFinished;
    private boolean mIsSystemRestore;
    private final com.android.server.backup.internal.OnTaskFinishedListener mListener;
    private android.os.ParcelFileDescriptor mNewState;
    private java.io.File mNewStateName;
    private android.app.backup.IRestoreObserver mObserver;
    private final com.android.server.backup.OperationStorage mOperationStorage;
    private com.android.server.backup.PackageManagerBackupAgent mPmAgent;
    private int mPmToken;
    private int mRestoreAttemptedAppsCount;
    private android.app.backup.RestoreDescription mRestoreDescription;
    private java.io.File mStageName;
    private long mStartRealtime;
    private com.android.server.backup.restore.UnifiedRestoreState mState;
    private java.io.File mStateDir;
    private int mStatus;
    private android.content.pm.PackageInfo mTargetPackage;
    private long mToken;
    private final com.android.server.backup.transport.TransportConnection mTransportConnection;
    private final com.android.server.backup.TransportManager mTransportManager;
    private final int mUserId;
    private java.util.List<java.lang.String> mVToUAllowlist;
    private java.util.List<java.lang.String> mVToUDenylist;
    private byte[] mWidgetData;

    PerformUnifiedRestoreTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String vToUAllowlist, java.lang.String vToUDenyList) {
        this.mAreVToUListsSet = false;
        this.mListener = null;
        this.mAgentTimeoutParameters = null;
        this.mOperationStorage = null;
        this.mTransportConnection = transportConnection;
        this.mTransportManager = null;
        this.mEphemeralOpToken = 0;
        this.mUserId = 0;
        this.mBackupEligibilityRules = null;
        this.backupManagerService = backupManagerService;
        this.mBackupManagerMonitorEventSender = new com.android.server.backup.utils.BackupManagerMonitorEventSender(null);
        this.mVToUAllowlist = createVToUList(vToUAllowlist);
        this.mVToUDenylist = createVToUList(vToUDenyList);
    }

    public PerformUnifiedRestoreTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long restoreSetToken, android.content.pm.PackageInfo targetPackage, int pmToken, boolean isFullSystemRestore, java.lang.String[] filterSet, com.android.server.backup.internal.OnTaskFinishedListener listener, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        java.lang.String[] filterSet2;
        this.mAreVToUListsSet = false;
        this.backupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mUserId = backupManagerService.getUserId();
        this.mTransportManager = backupManagerService.getTransportManager();
        this.mEphemeralOpToken = backupManagerService.generateRandomIntegerToken();
        this.mState = com.android.server.backup.restore.UnifiedRestoreState.INITIAL;
        this.mStartRealtime = android.os.SystemClock.elapsedRealtime();
        this.mTransportConnection = transportConnection;
        this.mObserver = observer;
        this.mBackupManagerMonitorEventSender = new com.android.server.backup.utils.BackupManagerMonitorEventSender(monitor);
        this.mToken = restoreSetToken;
        this.mPmToken = pmToken;
        this.mTargetPackage = targetPackage;
        this.mIsSystemRestore = isFullSystemRestore;
        this.mFinished = false;
        this.mDidLaunch = false;
        this.mListener = listener;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
        this.mBackupEligibilityRules = backupEligibilityRules;
        if (targetPackage != null) {
            this.mAcceptSet = new java.util.ArrayList();
            this.mAcceptSet.add(targetPackage);
        } else {
            if (filterSet != null) {
                filterSet2 = filterSet;
            } else {
                java.util.List<android.content.pm.PackageInfo> apps = com.android.server.backup.PackageManagerBackupAgent.getStorableApplications(backupManagerService.getPackageManager(), this.mUserId, backupEligibilityRules);
                filterSet2 = packagesToNames(apps);
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Full restore; asking about " + filterSet2.length + " apps");
            }
            this.mAcceptSet = new java.util.ArrayList(filterSet2.length);
            boolean hasSettings = false;
            boolean hasSystem = false;
            for (java.lang.String str : filterSet2) {
                try {
                    android.content.pm.PackageManager pm = backupManagerService.getPackageManager();
                    android.content.pm.PackageInfo info = pm.getPackageInfoAsUser(str, 0, this.mUserId);
                    if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(info.packageName)) {
                        hasSystem = true;
                    } else if (com.android.server.backup.UserBackupManagerService.SETTINGS_PACKAGE.equals(info.packageName)) {
                        hasSettings = true;
                    } else {
                        android.content.pm.ApplicationInfo applicationInfo = info.applicationInfo;
                        if (backupEligibilityRules.appIsEligibleForBackup(applicationInfo) && (!com.android.server.backup.Flags.enableSkippingRestoreLaunchedApps() || backupEligibilityRules.isAppEligibleForRestore(applicationInfo))) {
                            this.mAcceptSet.add(info);
                        }
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
            }
            if (hasSystem) {
                try {
                    this.mAcceptSet.add(0, backupManagerService.getPackageManager().getPackageInfoAsUser(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0, this.mUserId));
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                }
            }
            if (hasSettings) {
                try {
                    this.mAcceptSet.add(backupManagerService.getPackageManager().getPackageInfoAsUser(com.android.server.backup.UserBackupManagerService.SETTINGS_PACKAGE, 0, this.mUserId));
                } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                }
            }
        }
        this.mAcceptSet = backupManagerService.filterUserFacingPackages(this.mAcceptSet);
    }

    private java.lang.String[] packagesToNames(java.util.List<android.content.pm.PackageInfo> apps) {
        int N = apps.size();
        java.lang.String[] names = new java.lang.String[N];
        for (int i = 0; i < N; i++) {
            names[i] = apps.get(i).packageName;
        }
        return names;
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
        switch (this.mState) {
            case INITIAL:
                startRestore();
                break;
            case RUNNING_QUEUE:
                dispatchNextRestore();
                break;
            case RESTORE_KEYVALUE:
                restoreKeyValue();
                break;
            case RESTORE_FULL:
                restoreFull();
                break;
            case RESTORE_FINISHED:
                restoreFinished();
                break;
            case FINAL:
                if (!this.mFinished) {
                    finalizeRestore();
                } else {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Duplicate finish");
                }
                this.mFinished = true;
                break;
        }
    }

    private void startRestore() {
        sendStartRestore(this.mAcceptSet.size());
        try {
            java.lang.String transportDirName = this.mTransportManager.getTransportDirName(this.mTransportConnection.getTransportComponent());
            this.mStateDir = new java.io.File(this.backupManagerService.getBaseStateDir(), transportDirName);
            android.content.pm.PackageInfo pmPackage = new android.content.pm.PackageInfo();
            pmPackage.packageName = com.android.server.backup.UserBackupManagerService.PACKAGE_MANAGER_SENTINEL;
            this.mAcceptSet.add(0, pmPackage);
            android.content.pm.PackageInfo[] packages = (android.content.pm.PackageInfo[]) this.mAcceptSet.toArray(new android.content.pm.PackageInfo[0]);
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.startRestore()");
            if (this.mBackupManagerMonitorEventSender.getMonitor() == null) {
                this.mBackupManagerMonitorEventSender.setMonitor(transport.getBackupManagerMonitor());
            }
            if (com.android.server.backup.Flags.enableIncreasedBmmLoggingForRestoreAtInstall()) {
                for (android.content.pm.PackageInfo info : this.mAcceptSet) {
                    this.mBackupManagerMonitorEventSender.monitorEvent(75, info, 3, addRestoreOperationTypeToEvent(null));
                }
            }
            if (this.mIsSystemRestore) {
                com.android.server.AppWidgetBackupBridge.systemRestoreStarting(this.mUserId);
                android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(53, null, 3, monitoringExtras);
            } else {
                android.os.Bundle monitoringExtras2 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(54, null, 3, monitoringExtras2);
            }
            this.mStatus = transport.startRestore(this.mToken, packages);
            if (this.mStatus != 0) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Transport error " + this.mStatus + "; no restore possible");
                android.os.Bundle monitoringExtras3 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(55, this.mCurrentPackage, 1, monitoringExtras3);
                this.mStatus = -1000;
                executeNextState(com.android.server.backup.restore.UnifiedRestoreState.FINAL);
                return;
            }
            android.app.backup.RestoreDescription desc = transport.nextRestorePackage();
            if (desc == null) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "No restore metadata available; halting");
                android.os.Bundle monitoringExtras4 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(22, this.mCurrentPackage, 3, monitoringExtras4);
                this.mStatus = -1000;
                executeNextState(com.android.server.backup.restore.UnifiedRestoreState.FINAL);
                return;
            }
            if (com.android.server.backup.UserBackupManagerService.PACKAGE_MANAGER_SENTINEL.equals(desc.getPackageName())) {
                this.mCurrentPackage = new android.content.pm.PackageInfo();
                this.mCurrentPackage.packageName = com.android.server.backup.UserBackupManagerService.PACKAGE_MANAGER_SENTINEL;
                this.mCurrentPackage.applicationInfo = new android.content.pm.ApplicationInfo();
                this.mCurrentPackage.applicationInfo.uid = 1000;
                this.mPmAgent = this.backupManagerService.makeMetadataAgent(null);
                this.mAgent = android.app.IBackupAgent.Stub.asInterface(this.mPmAgent.onBind());
                initiateOneRestore(this.mCurrentPackage, 0L);
                this.backupManagerService.getBackupHandler().removeMessages(18);
                if (!this.mPmAgent.hasMetadata()) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "PM agent has no metadata, so not restoring");
                    android.os.Bundle monitoringExtras5 = addRestoreOperationTypeToEvent(null);
                    this.mBackupManagerMonitorEventSender.monitorEvent(24, this.mCurrentPackage, 3, monitoringExtras5);
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, com.android.server.backup.UserBackupManagerService.PACKAGE_MANAGER_SENTINEL, "Package manager restore metadata missing");
                    this.mStatus = -1000;
                    this.backupManagerService.getBackupHandler().removeMessages(20, this);
                    executeNextState(com.android.server.backup.restore.UnifiedRestoreState.FINAL);
                    return;
                }
                return;
            }
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Required package metadata but got " + desc.getPackageName());
            android.os.Bundle monitoringExtras6 = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(23, this.mCurrentPackage, 3, monitoringExtras6);
            this.mStatus = -1000;
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.FINAL);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to contact transport for restore: " + e.getMessage());
            android.os.Bundle monitoringExtras7 = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(25, null, 1, monitoringExtras7);
            this.mStatus = -1000;
            this.backupManagerService.getBackupHandler().removeMessages(20, this);
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.FINAL);
        }
    }

    private void dispatchNextRestore() {
        com.android.server.backup.restore.UnifiedRestoreState nextState;
        java.lang.String pkgName;
        com.android.server.backup.restore.UnifiedRestoreState nextState2 = com.android.server.backup.restore.UnifiedRestoreState.FINAL;
        try {
            try {
                com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.dispatchNextRestore()");
                this.mRestoreDescription = transport.nextRestorePackage();
                pkgName = this.mRestoreDescription != null ? this.mRestoreDescription.getPackageName() : null;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Can't get next restore target from transport; halting: " + e.getMessage());
                android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(60, null, 3, monitoringExtras);
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_TRANSPORT_FAILURE, new java.lang.Object[0]);
                this.mStatus = -1000;
                nextState = com.android.server.backup.restore.UnifiedRestoreState.FINAL;
            }
            if (pkgName == null) {
                android.os.Bundle monitoringExtras2 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(56, null, 1, monitoringExtras2);
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Failure getting next package name");
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_TRANSPORT_FAILURE, new java.lang.Object[0]);
                nextState2 = com.android.server.backup.restore.UnifiedRestoreState.FINAL;
                return;
            }
            if (this.mRestoreDescription == android.app.backup.RestoreDescription.NO_MORE_PACKAGES) {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, "No more packages; finishing restore");
                int millis = (int) (android.os.SystemClock.elapsedRealtime() - this.mStartRealtime);
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_SUCCESS, java.lang.Integer.valueOf(this.mRestoreAttemptedAppsCount), java.lang.Integer.valueOf(millis));
                nextState2 = com.android.server.backup.restore.UnifiedRestoreState.FINAL;
                return;
            }
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Next restore package: " + this.mRestoreDescription);
            this.mRestoreAttemptedAppsCount++;
            sendOnRestorePackage(this.mRestoreAttemptedAppsCount, pkgName);
            com.android.server.backup.PackageManagerBackupAgent.Metadata metaInfo = this.mPmAgent.getRestoredMetadata(pkgName);
            if (metaInfo == null) {
                android.content.pm.PackageInfo pkgInfo = new android.content.pm.PackageInfo();
                pkgInfo.packageName = pkgName;
                android.os.Bundle monitoringExtras3 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(24, pkgInfo, 3, monitoringExtras3);
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "No metadata for " + pkgName);
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, pkgName, "Package metadata missing");
                nextState2 = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                return;
            }
            try {
                this.mCurrentPackage = this.backupManagerService.getPackageManager().getPackageInfoAsUser(pkgName, 134217728, this.mUserId);
                android.os.Bundle monitoringExtras4 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(67, this.mCurrentPackage, 3, monitoringExtras4);
                if (metaInfo.versionCode > this.mCurrentPackage.getLongVersionCode()) {
                    if (this.mIsSystemRestore && isVToUDowngrade(this.mPmAgent.getSourceSdk(), android.os.Build.VERSION.SDK_INT)) {
                        if (!this.mAreVToUListsSet.booleanValue()) {
                            this.mVToUAllowlist = createVToUList(android.provider.Settings.Secure.getStringForUser(this.backupManagerService.getContext().getContentResolver(), "v_to_u_restore_allowlist", this.mUserId));
                            this.mVToUDenylist = createVToUList(android.provider.Settings.Secure.getStringForUser(this.backupManagerService.getContext().getContentResolver(), "v_to_u_restore_denylist", this.mUserId));
                            logVToUListsToBMM();
                            this.mAreVToUListsSet = true;
                        }
                        if (!isPackageEligibleForVToURestore(this.mCurrentPackage)) {
                            this.mBackupManagerMonitorEventSender.monitorEvent(71, this.mCurrentPackage, 3, addRestoreOperationTypeToEvent(null));
                            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, pkgName + " : Package not eligible for V to U downgrade scenario");
                            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, pkgName, "Package not eligible for V to U downgrade scenario");
                            nextState2 = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                            return;
                        }
                        this.mBackupManagerMonitorEventSender.monitorEvent(70, this.mCurrentPackage, 3, addRestoreOperationTypeToEvent(null));
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Package " + pkgName + " is eligible for V to U downgrade scenario");
                    } else {
                        if ((this.mCurrentPackage.applicationInfo.flags & 131072) == 0) {
                            logDowngradeScenario(false, metaInfo);
                            nextState2 = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                            return;
                        }
                        logDowngradeScenario(true, metaInfo);
                    }
                }
                this.mWidgetData = null;
                int type = this.mRestoreDescription.getDataType();
                if (type == 1) {
                    nextState = com.android.server.backup.restore.UnifiedRestoreState.RESTORE_KEYVALUE;
                } else if (type == 2) {
                    nextState = com.android.server.backup.restore.UnifiedRestoreState.RESTORE_FULL;
                } else {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unrecognized restore type " + type);
                    android.os.Bundle monitoringExtras5 = addRestoreOperationTypeToEvent(null);
                    this.mBackupManagerMonitorEventSender.monitorEvent(57, this.mCurrentPackage, 3, monitoringExtras5);
                    nextState = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Package not present: " + pkgName);
                android.os.Bundle monitoringExtras6 = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(26, createPackageInfoForBMMLogging(pkgName), 3, monitoringExtras6);
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, pkgName, "Package missing on device");
                nextState2 = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
            }
        } finally {
            executeNextState(nextState2);
        }
    }

    private void restoreKeyValue() {
        java.lang.String packageName = this.mCurrentPackage.packageName;
        this.mBackupManagerMonitorEventSender.monitorEvent(58, this.mCurrentPackage, 3, addRestoreOperationTypeToEvent(null));
        if (this.mCurrentPackage.applicationInfo.backupAgentName == null || "".equals(this.mCurrentPackage.applicationInfo.backupAgentName)) {
            android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(28, this.mCurrentPackage, 2, monitoringExtras);
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, packageName, "Package has no agent");
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
            return;
        }
        com.android.server.backup.PackageManagerBackupAgent.Metadata metaInfo = this.mPmAgent.getRestoredMetadata(packageName);
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (!com.android.server.backup.BackupUtils.signaturesMatch(metaInfo.sigHashes, this.mCurrentPackage, pmi)) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Signature mismatch restoring " + packageName);
            android.os.Bundle monitoringExtras2 = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(29, this.mCurrentPackage, 3, monitoringExtras2);
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, packageName, "Signature mismatch");
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
            return;
        }
        this.mAgent = this.backupManagerService.bindToAgentSynchronous(this.mCurrentPackage.applicationInfo, 2, this.mBackupEligibilityRules.getBackupDestination());
        if (this.mAgent == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Can't find backup agent for " + packageName);
            android.os.Bundle monitoringExtras3 = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(30, this.mCurrentPackage, 3, monitoringExtras3);
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, packageName, "Restore agent missing");
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
            return;
        }
        this.mDidLaunch = true;
        try {
            initiateOneRestore(this.mCurrentPackage, metaInfo.versionCode);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Error when attempting restore: " + e.toString());
            android.os.Bundle monitoringExtras4 = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(61, this.mCurrentPackage, 2, monitoringExtras4);
            keyValueAgentErrorCleanup(false);
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    private void initiateOneRestore(android.content.pm.PackageInfo app, long appVersionCode) {
        android.os.ParcelFileDescriptor stage;
        com.android.server.backup.restore.UnifiedRestoreState nextState;
        java.lang.String packageName = app.packageName;
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "initiateOneRestore packageName=" + packageName);
        this.mBackupDataName = new java.io.File(this.backupManagerService.getDataDir(), packageName + ".restore");
        this.mStageName = new java.io.File(this.backupManagerService.getDataDir(), packageName + ".stage");
        this.mNewStateName = new java.io.File(this.mStateDir, packageName + com.android.server.backup.keyvalue.KeyValueBackupTask.NEW_STATE_FILE_SUFFIX);
        boolean staging = shouldStageBackupData(packageName);
        java.io.File downloadFile = staging ? this.mStageName : this.mBackupDataName;
        try {
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.initiateOneRestore()");
            android.os.ParcelFileDescriptor stage2 = android.os.ParcelFileDescriptor.open(downloadFile, 1006632960);
            if (transport.getRestoreData(stage2) != 0) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Error getting restore data for " + packageName);
                android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(63, this.mCurrentPackage, 1, monitoringExtras);
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_TRANSPORT_FAILURE, new java.lang.Object[0]);
                stage2.close();
                downloadFile.delete();
                if (com.android.server.backup.BackupAndRestoreFeatureFlags.getUnifiedRestoreContinueAfterTransportFailureInKvRestore()) {
                    nextState = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                } else {
                    nextState = com.android.server.backup.restore.UnifiedRestoreState.FINAL;
                }
                executeNextState(nextState);
                return;
            }
            if (!staging) {
                stage = stage2;
            } else {
                stage2.close();
                android.os.ParcelFileDescriptor stage3 = android.os.ParcelFileDescriptor.open(downloadFile, 268435456);
                this.mBackupData = android.os.ParcelFileDescriptor.open(this.mBackupDataName, 1006632960);
                android.app.backup.BackupDataInput in = new android.app.backup.BackupDataInput(stage3.getFileDescriptor());
                android.app.backup.BackupDataOutput out = new android.app.backup.BackupDataOutput(this.mBackupData.getFileDescriptor());
                filterExcludedKeys(packageName, in, out);
                this.mBackupData.close();
                stage = stage3;
            }
            stage.close();
            this.mBackupData = android.os.ParcelFileDescriptor.open(this.mBackupDataName, 268435456);
            this.mNewState = android.os.ParcelFileDescriptor.open(this.mNewStateName, 1006632960);
            long restoreAgentTimeoutMillis = this.mAgentTimeoutParameters.getRestoreAgentTimeoutMillis(app.applicationInfo.uid);
            this.backupManagerService.prepareOperationTimeout(this.mEphemeralOpToken, restoreAgentTimeoutMillis, this, 1);
            this.mAgent.doRestoreWithExcludedKeys(this.mBackupData, appVersionCode, this.mNewState, this.mEphemeralOpToken, this.backupManagerService.getBackupManagerBinder(), new java.util.ArrayList(getExcludedKeysForPackage(packageName)));
        } catch (java.lang.Exception e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to call app for restore: " + packageName, e);
            android.os.Bundle monitoringExtras2 = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(61, this.mCurrentPackage, 2, monitoringExtras2);
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, packageName, e.toString());
            keyValueAgentErrorCleanup(false);
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    boolean shouldStageBackupData(java.lang.String packageName) {
        return (packageName.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) && getExcludedKeysForPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).isEmpty()) ? false : true;
    }

    java.util.Set<java.lang.String> getExcludedKeysForPackage(java.lang.String packageName) {
        return this.backupManagerService.getExcludedRestoreKeys(packageName);
    }

    void filterExcludedKeys(java.lang.String packageName, android.app.backup.BackupDataInput in, android.app.backup.BackupDataOutput out) throws java.lang.Exception {
        java.util.Set<java.lang.String> excludedKeysForPackage = getExcludedKeysForPackage(packageName);
        int bufferSize = 8192;
        if (com.android.server.backup.Flags.enableMaxSizeWritesToPipes()) {
            bufferSize = 65536;
        }
        byte[] buffer = new byte[bufferSize];
        while (in.readNextHeader()) {
            java.lang.String key = in.getKey();
            int size = in.getDataSize();
            if (excludedKeysForPackage != null && excludedKeysForPackage.contains(key)) {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Skipping blocked key " + key);
                in.skipEntityData();
            } else if (key.equals(com.android.server.backup.UserBackupManagerService.KEY_WIDGET_STATE)) {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Restoring widget state for " + packageName);
                this.mWidgetData = new byte[size];
                in.readEntityData(this.mWidgetData, 0, size);
            } else {
                if (size > buffer.length) {
                    buffer = new byte[size];
                }
                in.readEntityData(buffer, 0, size);
                out.writeEntityHeader(key, size);
                out.writeEntityData(buffer, size);
            }
        }
    }

    private void restoreFull() {
        this.mBackupManagerMonitorEventSender.monitorEvent(59, this.mCurrentPackage, 3, addRestoreOperationTypeToEvent(null));
        try {
            com.android.server.backup.restore.PerformUnifiedRestoreTask.StreamFeederThread feeder = new com.android.server.backup.restore.PerformUnifiedRestoreTask.StreamFeederThread();
            new java.lang.Thread(feeder, "unified-stream-feeder").start();
        } catch (java.io.IOException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to construct pipes for stream restore!");
            android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(64, this.mCurrentPackage, 3, monitoringExtras);
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    private void restoreFinished() {
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "restoreFinished packageName=" + this.mCurrentPackage.packageName);
        try {
            long restoreAgentFinishedTimeoutMillis = this.mAgentTimeoutParameters.getRestoreAgentFinishedTimeoutMillis();
            this.backupManagerService.prepareOperationTimeout(this.mEphemeralOpToken, restoreAgentFinishedTimeoutMillis, this, 1);
            this.mAgent.doRestoreFinished(this.mEphemeralOpToken, this.backupManagerService.getBackupManagerBinder());
        } catch (java.lang.Exception e) {
            java.lang.String packageName = this.mCurrentPackage.packageName;
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to finalize restore of " + packageName);
            android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
            this.mBackupManagerMonitorEventSender.monitorEvent(69, this.mCurrentPackage, 2, monitoringExtras);
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, packageName, e.toString());
            keyValueAgentErrorCleanup(true);
            executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
        }
    }

    class StreamFeederThread extends com.android.server.backup.restore.RestoreEngine implements java.lang.Runnable, com.android.server.backup.BackupRestoreTask {
        com.android.server.backup.restore.FullRestoreEngine mEngine;
        com.android.server.backup.restore.FullRestoreEngineThread mEngineThread;
        private final int mEphemeralOpToken;
        final java.lang.String TAG = "StreamFeederThread";
        android.os.ParcelFileDescriptor[] mTransportPipes = android.os.ParcelFileDescriptor.createPipe();
        android.os.ParcelFileDescriptor[] mEnginePipes = android.os.ParcelFileDescriptor.createPipe();

        public StreamFeederThread() throws java.io.IOException {
            this.mEphemeralOpToken = com.android.server.backup.restore.PerformUnifiedRestoreTask.this.backupManagerService.generateRandomIntegerToken();
            setRunning(true);
        }

        /* JADX WARN: Removed duplicated region for block: B:115:0x0368 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:120:0x0296 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0272  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x0274  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x027a  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x02d5 A[PHI: r1
  0x02d5: PHI (r1v22 'status' int) = (r1v21 'status' int), (r1v32 'status' int) binds: [B:73:0x02d3, B:91:0x03a5] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:75:0x02db A[PHI: r1
  0x02db: PHI (r1v23 'status' int) = (r1v21 'status' int), (r1v32 'status' int) binds: [B:73:0x02d3, B:91:0x03a5] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x0346  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x0348  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x034e  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 1100
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.restore.PerformUnifiedRestoreTask.StreamFeederThread.run():void");
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void execute() {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void operationComplete(long result) {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void handleCancel(boolean cancelAll) {
            com.android.server.backup.restore.PerformUnifiedRestoreTask.this.mOperationStorage.removeOperation(this.mEphemeralOpToken);
            android.util.Slog.w("StreamFeederThread", "Full-data restore target timed out; shutting down");
            android.os.Bundle monitoringExtras = com.android.server.backup.restore.PerformUnifiedRestoreTask.this.addRestoreOperationTypeToEvent(null);
            com.android.server.backup.restore.PerformUnifiedRestoreTask.this.mBackupManagerMonitorEventSender.monitorEvent(45, com.android.server.backup.restore.PerformUnifiedRestoreTask.this.mCurrentPackage, 2, monitoringExtras);
            this.mEngineThread.handleTimeout();
            libcore.io.IoUtils.closeQuietly(this.mEnginePipes[1]);
            this.mEnginePipes[1] = null;
            libcore.io.IoUtils.closeQuietly(this.mEnginePipes[0]);
            this.mEnginePipes[0] = null;
        }
    }

    private void finalizeRestore() {
        try {
            com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("PerformUnifiedRestoreTask.finalizeRestore()");
            transport.finishRestore();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Error finishing restore", e);
        }
        sendEndRestore();
        this.backupManagerService.getBackupHandler().removeMessages(8);
        if (this.mPmToken > 0) {
            try {
                this.backupManagerService.getPackageManagerBinder().finishPackageInstall(this.mPmToken, this.mDidLaunch);
            } catch (android.os.RemoteException e2) {
            }
        } else {
            long restoreAgentTimeoutMillis = this.mAgentTimeoutParameters.getRestoreSessionTimeoutMillis();
            this.backupManagerService.getBackupHandler().sendEmptyMessageDelayed(8, restoreAgentTimeoutMillis);
        }
        if (this.mIsSystemRestore) {
            com.android.server.AppWidgetBackupBridge.systemRestoreFinished(this.mUserId);
        }
        if (this.mIsSystemRestore && this.mPmAgent != null) {
            this.backupManagerService.setAncestralPackages(this.mPmAgent.getRestoredPackages());
            this.backupManagerService.setAncestralToken(this.mToken);
            this.backupManagerService.setAncestralBackupDestination(this.mBackupEligibilityRules.getBackupDestination());
            this.backupManagerService.writeRestoreTokens();
        }
        synchronized (this.backupManagerService.getPendingRestores()) {
            if (this.backupManagerService.getPendingRestores().size() > 0) {
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Starting next pending restore.");
                com.android.server.backup.restore.PerformUnifiedRestoreTask task = this.backupManagerService.getPendingRestores().remove();
                this.backupManagerService.getBackupHandler().sendMessage(this.backupManagerService.getBackupHandler().obtainMessage(20, task));
            } else {
                this.backupManagerService.setRestoreInProgress(false);
            }
        }
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Restore complete.");
        android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
        this.mBackupManagerMonitorEventSender.monitorEvent(68, null, 3, monitoringExtras);
        this.mListener.onFinished("PerformUnifiedRestoreTask.finalizeRestore()");
    }

    void keyValueAgentErrorCleanup(boolean clearAppData) {
        if (clearAppData) {
            this.backupManagerService.clearApplicationDataAfterRestoreFailure(this.mCurrentPackage.packageName);
        }
        keyValueAgentCleanup();
    }

    void keyValueAgentCleanup() {
        this.mBackupDataName.delete();
        this.mStageName.delete();
        try {
            if (this.mBackupData != null) {
                this.mBackupData.close();
            }
        } catch (java.io.IOException e) {
        }
        try {
            if (this.mNewState != null) {
                this.mNewState.close();
            }
        } catch (java.io.IOException e2) {
        }
        this.mNewState = null;
        this.mBackupData = null;
        this.mNewStateName.delete();
        if (this.mCurrentPackage.applicationInfo != null) {
            try {
                this.backupManagerService.getActivityManager().unbindBackupAgent(this.mCurrentPackage.applicationInfo);
                int appFlags = this.mCurrentPackage.applicationInfo.flags;
                boolean killAfterRestore = !android.os.UserHandle.isCore(this.mCurrentPackage.applicationInfo.uid) && (this.mRestoreDescription.getDataType() == 2 || (65536 & appFlags) != 0);
                if (this.mTargetPackage == null && killAfterRestore) {
                    android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Restore complete, killing host process of " + this.mCurrentPackage.applicationInfo.processName);
                    this.backupManagerService.getActivityManager().killApplicationProcess(this.mCurrentPackage.applicationInfo.processName, this.mCurrentPackage.applicationInfo.uid);
                }
            } catch (android.os.RemoteException e3) {
            }
        }
        this.backupManagerService.getBackupHandler().removeMessages(18, this);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long unusedResult) {
        com.android.server.backup.restore.UnifiedRestoreState nextState;
        this.mOperationStorage.removeOperation(this.mEphemeralOpToken);
        switch (this.mState) {
            case INITIAL:
                nextState = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                break;
            case RUNNING_QUEUE:
            default:
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unexpected restore callback into state " + this.mState);
                keyValueAgentErrorCleanup(true);
                nextState = com.android.server.backup.restore.UnifiedRestoreState.FINAL;
                break;
            case RESTORE_KEYVALUE:
            case RESTORE_FULL:
                nextState = com.android.server.backup.restore.UnifiedRestoreState.RESTORE_FINISHED;
                break;
            case RESTORE_FINISHED:
                int size = (int) this.mBackupDataName.length();
                android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
                this.mBackupManagerMonitorEventSender.monitorEvent(62, this.mCurrentPackage, 3, monitoringExtras);
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_PACKAGE, this.mCurrentPackage.packageName, java.lang.Integer.valueOf(size));
                this.mBackupManagerMonitorEventSender.monitorAgentLoggingResults(this.mCurrentPackage, this.mAgent);
                keyValueAgentCleanup();
                if (this.mWidgetData != null) {
                    this.backupManagerService.restoreWidgetData(this.mCurrentPackage.packageName, this.mWidgetData);
                }
                nextState = com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE;
                break;
        }
        executeNextState(nextState);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean cancelAll) {
        this.mOperationStorage.removeOperation(this.mEphemeralOpToken);
        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Timeout restoring application " + this.mCurrentPackage.packageName);
        android.os.Bundle monitoringExtras = addRestoreOperationTypeToEvent(null);
        this.mBackupManagerMonitorEventSender.monitorEvent(31, this.mCurrentPackage, 2, monitoringExtras);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, this.mCurrentPackage.packageName, "restore timeout");
        keyValueAgentErrorCleanup(true);
        executeNextState(com.android.server.backup.restore.UnifiedRestoreState.RUNNING_QUEUE);
    }

    void executeNextState(com.android.server.backup.restore.UnifiedRestoreState nextState) {
        this.mState = nextState;
        android.os.Message msg = this.backupManagerService.getBackupHandler().obtainMessage(20, this);
        this.backupManagerService.getBackupHandler().sendMessage(msg);
    }

    com.android.server.backup.restore.UnifiedRestoreState getCurrentUnifiedRestoreStateForTesting() {
        return this.mState;
    }

    void setCurrentUnifiedRestoreStateForTesting(com.android.server.backup.restore.UnifiedRestoreState state) {
        this.mState = state;
    }

    void setStateDirForTesting(java.io.File stateDir) {
        this.mStateDir = stateDir;
    }

    void initiateOneRestoreForTesting(android.content.pm.PackageInfo app, long appVersionCode) {
        initiateOneRestore(app, appVersionCode);
    }

    void sendStartRestore(int numPackages) {
        if (this.mObserver != null) {
            try {
                this.mObserver.restoreStarting(numPackages);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Restore observer went away: startRestore");
                this.mObserver = null;
            }
        }
    }

    void sendOnRestorePackage(int index, java.lang.String name) {
        if (this.mObserver != null) {
            try {
                this.mObserver.onUpdate(index, name);
            } catch (android.os.RemoteException e) {
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Restore observer died in onUpdate");
                this.mObserver = null;
            }
        }
    }

    void sendEndRestore() {
        if (this.mObserver != null) {
            try {
                this.mObserver.restoreFinished(this.mStatus);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Restore observer went away: endRestore");
                this.mObserver = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.Bundle addRestoreOperationTypeToEvent(android.os.Bundle extras) {
        return com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(extras, "android.app.backup.extra.OPERATION_TYPE", 1);
    }

    protected boolean isVToUDowngrade(int sourceSdk, int targetSdk) {
        return com.android.server.backup.Flags.enableVToURestoreForSystemComponentsInAllowlist() && sourceSdk > 34 && targetSdk == 34;
    }

    protected java.util.List<java.lang.String> createVToUList(java.lang.String listString) {
        java.util.List<java.lang.String> list = new java.util.ArrayList<>();
        if (listString != null) {
            java.util.List<java.lang.String> list2 = java.util.Arrays.asList(listString.split(","));
            return list2;
        }
        return list;
    }

    protected boolean isPackageEligibleForVToURestore(android.content.pm.PackageInfo mCurrentPackage) {
        if (this.mVToUDenylist.contains(mCurrentPackage.packageName)) {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, mCurrentPackage.packageName + " : Package is in V to U denylist");
            return false;
        }
        if ((mCurrentPackage.applicationInfo.flags & 131072) == 0) {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, mCurrentPackage.packageName + " : Package has restoreAnyVersion=false and is in V to U allowlist");
            return this.mVToUAllowlist.contains(mCurrentPackage.packageName);
        }
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, mCurrentPackage.packageName + " : Package has restoreAnyVersion=true and is not in V to U denylist");
        return true;
    }

    private void logDowngradeScenario(boolean isRestoreAnyVersion, com.android.server.backup.PackageManagerBackupAgent.Metadata metaInfo) {
        android.os.Bundle monitoringExtras;
        java.lang.String message;
        android.os.Bundle monitoringExtras2 = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_RESTORE_VERSION", metaInfo.versionCode);
        if (isRestoreAnyVersion) {
            monitoringExtras = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(monitoringExtras2, "android.app.backup.extra.LOG_RESTORE_ANYWAY", true);
            message = "Source version " + metaInfo.versionCode + " > installed version " + this.mCurrentPackage.getLongVersionCode() + " but restoreAnyVersion";
        } else {
            monitoringExtras = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(monitoringExtras2, "android.app.backup.extra.LOG_RESTORE_ANYWAY", false);
            message = "Source version " + metaInfo.versionCode + " > installed version " + this.mCurrentPackage.getLongVersionCode();
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_AGENT_FAILURE, this.mCurrentPackage.packageName, message);
        }
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Package " + this.mCurrentPackage.packageName + ": " + message);
        this.mBackupManagerMonitorEventSender.monitorEvent(27, this.mCurrentPackage, 3, addRestoreOperationTypeToEvent(monitoringExtras));
    }

    private void logVToUListsToBMM() {
        android.os.Bundle monitoringExtrasAllowlist = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.V_TO_U_ALLOWLIST", this.mVToUAllowlist.toString());
        this.mBackupManagerMonitorEventSender.monitorEvent(72, null, 3, addRestoreOperationTypeToEvent(monitoringExtrasAllowlist));
        android.os.Bundle monitoringExtrasDenylist = com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.V_TO_U_DENYLIST", this.mVToUDenylist.toString());
        this.mBackupManagerMonitorEventSender.monitorEvent(72, null, 3, addRestoreOperationTypeToEvent(monitoringExtrasDenylist));
    }

    private android.content.pm.PackageInfo createPackageInfoForBMMLogging(java.lang.String packageName) {
        android.content.pm.PackageInfo packageInfo = new android.content.pm.PackageInfo();
        packageInfo.packageName = packageName;
        return packageInfo;
    }
}
