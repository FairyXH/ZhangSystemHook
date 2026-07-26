package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class JobServiceContext implements android.content.ServiceConnection {
    private static final long ANR_PRE_UDC_APIS_ON_SLOW_RESPONSES = 258236856;
    private static final long EXECUTION_DURATION_STAMP_PERIOD_MILLIS = 300000;
    private static final int MSG_TIMEOUT = 0;
    public static final int NO_PREFERRED_UID = -1;
    private static final java.lang.String TAG = "JobServiceContext";
    static final int VERB_BINDING = 0;
    static final int VERB_EXECUTING = 2;
    static final int VERB_FINISHED = 4;
    static final int VERB_STARTING = 1;
    static final int VERB_STOPPING = 3;
    private boolean mAwaitingNotification;
    private final com.android.internal.app.IBatteryStats mBatteryStats;
    private final android.os.Handler mCallbackHandler;
    private boolean mCancelled;
    private final com.android.server.job.JobCompletedListener mCompletedListener;
    private final android.content.Context mContext;
    private java.lang.String mDeathMarkDebugReason;
    private int mDeathMarkInternalStopReason;
    private long mEstimatedDownloadBytes;
    private long mEstimatedUploadBytes;
    private long mExecutionStartTimeElapsed;
    private long mInitialDownloadedBytesFromCalling;
    private long mInitialDownloadedBytesFromSource;
    private long mInitialUploadedBytesFromCalling;
    private long mInitialUploadedBytesFromSource;
    private final com.android.server.job.JobConcurrencyManager mJobConcurrencyManager;
    private final com.android.server.job.JobPackageTracker mJobPackageTracker;
    private long mLastExecutionDurationStampTimeElapsed;
    private long mLastUnsuccessfulFinishElapsed;
    private final java.lang.Object mLock;
    private long mMaxExecutionTimeMillis;
    private long mMinExecutionGuaranteeMillis;
    private final com.android.server.job.JobNotificationCoordinator mNotificationCoordinator;
    private android.app.job.JobParameters mParams;
    private java.lang.String mPendingDebugStopReason;
    private int mPendingInternalStopReason;
    private android.net.Network mPendingNetworkChange;
    private final android.os.PowerManager mPowerManager;
    private boolean mPreviousJobHadSuccessfulFinish;
    private com.android.server.job.JobServiceContext.JobCallback mRunningCallback;
    private com.android.server.job.controllers.JobStatus mRunningJob;
    private int mRunningJobWorkType;
    private final com.android.server.job.JobSchedulerService mService;
    public java.lang.String mStoppedReason;
    public long mStoppedTime;
    private long mTimeoutElapsed;
    private long mTransferredDownloadBytes;
    private long mTransferredUploadBytes;
    private android.os.PowerManager.WakeLock mWakeLock;
    android.app.job.IJobService service;
    private static final boolean DEBUG = com.android.server.job.JobSchedulerService.DEBUG;
    private static final boolean DEBUG_STANDBY = com.android.server.job.JobSchedulerService.DEBUG_STANDBY;
    private static final long OP_BIND_TIMEOUT_MILLIS = android.os.Build.HW_TIMEOUT_MULTIPLIER * 18000;
    private static final long OP_TIMEOUT_MILLIS = android.os.Build.HW_TIMEOUT_MULTIPLIER * com.android.server.EventLogTags.JOB_DEFERRED_EXECUTION;
    private static final long NOTIFICATION_TIMEOUT_MILLIS = ((long) android.os.Build.HW_TIMEOUT_MULTIPLIER) * 10000;
    private static final com.android.modules.expresslog.Histogram sEnqueuedJwiAtJobStart = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_w_uid_enqueued_work_items_at_job_start", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(20, 1, 3.0f, 1.4f));
    private static final com.android.modules.expresslog.Histogram sTransferredNetworkDownloadKBHighWaterMarkLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_transferred_network_download_kilobytes_high_water_mark", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
    private static final com.android.modules.expresslog.Histogram sTransferredNetworkUploadKBHighWaterMarkLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_transferred_network_upload_kilobytes_high_water_mark", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
    private static final com.android.modules.expresslog.Histogram sUpdatedEstimatedNetworkDownloadKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_updated_estimated_network_download_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
    private static final com.android.modules.expresslog.Histogram sUpdatedEstimatedNetworkUploadKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_updated_estimated_network_upload_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
    private static final java.lang.String[] VERB_STRINGS = {"VERB_BINDING", "VERB_STARTING", "VERB_EXECUTING", "VERB_STOPPING", "VERB_FINISHED"};
    private int mPendingStopReason = 0;
    private int mDeathMarkStopReason = 0;
    private com.android.server.job.JobServiceContext.JobServiceContextWrapper mJobServiceContextWrapper = new com.android.server.job.JobServiceContext.JobServiceContextWrapper();
    private com.android.server.job.IJobServiceContextExt mJobServiceContextExt = (com.android.server.job.IJobServiceContextExt) system.ext.loader.core.ExtLoader.type(com.android.server.job.IJobServiceContextExt.class).base(this).create();
    private final android.app.ActivityManagerInternal mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
    private final android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
    private boolean mAvailable = true;
    int mVerb = 4;
    private int mPreferredUid = -1;

    final class JobCallback extends android.app.job.IJobCallback.Stub {
        public java.lang.String mStoppedReason;
        public long mStoppedTime;

        JobCallback() {
        }

        public void acknowledgeGetTransferredDownloadBytesMessage(int jobId, int workId, long transferredBytes) {
            com.android.server.job.JobServiceContext.this.doAcknowledgeGetTransferredDownloadBytesMessage(this, jobId, workId, transferredBytes);
        }

        public void acknowledgeGetTransferredUploadBytesMessage(int jobId, int workId, long transferredBytes) {
            com.android.server.job.JobServiceContext.this.doAcknowledgeGetTransferredUploadBytesMessage(this, jobId, workId, transferredBytes);
        }

        public void acknowledgeStartMessage(int jobId, boolean ongoing) {
            com.android.server.job.JobServiceContext.this.doAcknowledgeStartMessage(this, jobId, ongoing);
        }

        public void acknowledgeStopMessage(int jobId, boolean reschedule) {
            com.android.server.job.JobServiceContext.this.doAcknowledgeStopMessage(this, jobId, reschedule);
        }

        public android.app.job.JobWorkItem dequeueWork(int jobId) {
            return com.android.server.job.JobServiceContext.this.doDequeueWork(this, jobId);
        }

        public boolean completeWork(int jobId, int workId) {
            return com.android.server.job.JobServiceContext.this.doCompleteWork(this, jobId, workId);
        }

        public void jobFinished(int jobId, boolean reschedule) {
            com.android.server.job.JobServiceContext.this.doJobFinished(this, jobId, reschedule);
        }

        public void updateEstimatedNetworkBytes(int jobId, android.app.job.JobWorkItem item, long downloadBytes, long uploadBytes) {
            com.android.server.job.JobServiceContext.this.doUpdateEstimatedNetworkBytes(this, jobId, item, downloadBytes, uploadBytes);
        }

        public void updateTransferredNetworkBytes(int jobId, android.app.job.JobWorkItem item, long downloadBytes, long uploadBytes) {
            com.android.server.job.JobServiceContext.this.doUpdateTransferredNetworkBytes(this, jobId, item, downloadBytes, uploadBytes);
        }

        public void setNotification(int jobId, int notificationId, android.app.Notification notification, int jobEndNotificationPolicy) {
            com.android.server.job.JobServiceContext.this.doSetNotification(this, jobId, notificationId, notification, jobEndNotificationPolicy);
        }
    }

    JobServiceContext(com.android.server.job.JobSchedulerService service, com.android.server.job.JobConcurrencyManager concurrencyManager, com.android.server.job.JobNotificationCoordinator notificationCoordinator, com.android.internal.app.IBatteryStats batteryStats, com.android.server.job.JobPackageTracker tracker, android.os.Looper looper) {
        this.mContext = service.getContext();
        this.mLock = service.getLock();
        this.mService = service;
        this.mBatteryStats = batteryStats;
        this.mJobPackageTracker = tracker;
        this.mCallbackHandler = new com.android.server.job.JobServiceContext.JobServiceHandler(looper);
        this.mJobConcurrencyManager = concurrencyManager;
        this.mNotificationCoordinator = notificationCoordinator;
        this.mCompletedListener = service;
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
    }

    boolean executeRunnableJob(com.android.server.job.controllers.JobStatus job, int workType) {
        android.net.Uri[] triggeredUris;
        java.lang.String[] triggeredAuthorities;
        boolean binding;
        java.lang.String[] strArr;
        long bindFlags;
        synchronized (this.mLock) {
            if (this.mAvailable) {
                this.mPreferredUid = -1;
                this.mRunningJob = job;
                this.mRunningJobWorkType = workType;
                this.mRunningCallback = new com.android.server.job.JobServiceContext.JobCallback();
                this.mPendingNetworkChange = null;
                boolean isDeadlineExpired = job.hasDeadlineConstraint() && job.getLatestRunTimeElapsed() < com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                if (job.changedUris == null) {
                    triggeredUris = null;
                } else {
                    android.net.Uri[] triggeredUris2 = new android.net.Uri[job.changedUris.size()];
                    job.changedUris.toArray(triggeredUris2);
                    triggeredUris = triggeredUris2;
                }
                if (job.changedAuthorities == null) {
                    triggeredAuthorities = null;
                } else {
                    java.lang.String[] triggeredAuthorities2 = new java.lang.String[job.changedAuthorities.size()];
                    job.changedAuthorities.toArray(triggeredAuthorities2);
                    triggeredAuthorities = triggeredAuthorities2;
                }
                android.app.job.JobInfo ji = job.getJob();
                android.net.Network passedNetwork = canGetNetworkInformation(job) ? job.network : null;
                this.mParams = new android.app.job.JobParameters(this.mRunningCallback, job.getNamespace(), job.getJobId(), ji.getExtras(), ji.getTransientExtras(), ji.getClipData(), ji.getClipGrantFlags(), isDeadlineExpired, job.shouldTreatAsExpeditedJob(), job.shouldTreatAsUserInitiatedJob(), triggeredUris, triggeredAuthorities, passedNetwork);
                this.mExecutionStartTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                this.mLastExecutionDurationStampTimeElapsed = this.mExecutionStartTimeElapsed;
                this.mMinExecutionGuaranteeMillis = this.mService.getMinJobExecutionGuaranteeMs(job);
                this.mMaxExecutionTimeMillis = java.lang.Math.max(this.mService.getMaxJobExecutionTimeMs(job), this.mMinExecutionGuaranteeMillis);
                this.mEstimatedDownloadBytes = job.getEstimatedNetworkDownloadBytes();
                this.mEstimatedUploadBytes = job.getEstimatedNetworkUploadBytes();
                this.mTransferredUploadBytes = 0L;
                this.mTransferredDownloadBytes = 0L;
                this.mAwaitingNotification = job.isUserVisibleJob();
                if (job.getWrapper().getExtImpl().getOplusExtraStr(job) != null) {
                    this.mParams.mJobParametersExt.setStringValue("setOplusExtraStr", job.getWrapper().getExtImpl().getOplusExtraStr(job));
                }
                long whenDeferred = job.getWhenStandbyDeferred();
                if (whenDeferred > 0) {
                    long deferral = this.mExecutionStartTimeElapsed - whenDeferred;
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.JOB_DEFERRED_EXECUTION, deferral);
                    if (DEBUG_STANDBY) {
                        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
                        sb.append("Starting job deferred for standby by ");
                        android.util.TimeUtils.formatDuration(deferral, sb);
                        sb.append(" ms : ");
                        sb.append(job.toShortString());
                        android.util.Slog.v(TAG, sb.toString());
                    }
                }
                job.clearPersistedUtcTimes();
                this.mWakeLock = this.mPowerManager.newWakeLock(1, job.getWakelockTag());
                this.mWakeLock.setWorkSource(this.mService.deriveWorkSource(job.getSourceUid(), job.getSourcePackageName()));
                this.mWakeLock.setReferenceCounted(false);
                this.mWakeLock.acquire();
                this.mVerb = 0;
                scheduleOpTimeOutLocked();
                android.content.Intent intent = new android.content.Intent().setComponent(job.getServiceComponent()).setFlags(4);
                intent.putExtra("BINDSERVICE_FROM_JOB", true);
                boolean startedWithForegroundFlag = false;
                try {
                    if (job.shouldTreatAsUserInitiatedJob() && !job.isUserBgRestricted()) {
                        bindFlags = 32769 | 65536;
                        if (job.hasConnectivityConstraint()) {
                            bindFlags |= 4295098368L;
                        }
                        startedWithForegroundFlag = true;
                    } else if (job.shouldTreatAsExpeditedJob() || job.shouldTreatAsUserInitiatedJob()) {
                        bindFlags = 32769 | 65540;
                        if (job.hasConnectivityConstraint()) {
                            bindFlags |= 131072;
                        }
                    } else {
                        bindFlags = 32769 | 260;
                    }
                    boolean binding2 = this.mContext.bindServiceAsUser(intent, this, android.content.Context.BindServiceFlags.of(bindFlags), android.os.UserHandle.of(job.getUserId()));
                    binding = binding2;
                } catch (java.lang.SecurityException e) {
                    android.util.Slog.w(TAG, "Job service " + job.getServiceComponent().getShortClassName() + " cannot be executed: " + e.getMessage());
                    binding = false;
                }
                if (!binding) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, job.getServiceComponent().getShortClassName() + " unavailable.");
                    }
                    this.mContext.unbindService(this);
                    this.mRunningJob = null;
                    this.mRunningJobWorkType = 0;
                    this.mRunningCallback = null;
                    this.mParams = null;
                    this.mExecutionStartTimeElapsed = 0L;
                    this.mWakeLock.release();
                    this.mVerb = 4;
                    removeOpTimeOutLocked();
                    return false;
                }
                this.mJobPackageTracker.noteActive(job);
                int sourceUid = job.getSourceUid();
                this.mInitialDownloadedBytesFromSource = android.net.TrafficStats.getUidRxBytes(sourceUid);
                this.mInitialUploadedBytesFromSource = android.net.TrafficStats.getUidTxBytes(sourceUid);
                this.mInitialDownloadedBytesFromCalling = android.net.TrafficStats.getUidRxBytes(job.getUid());
                this.mInitialUploadedBytesFromCalling = android.net.TrafficStats.getUidTxBytes(job.getUid());
                int[] iArr = job.isProxyJob() ? new int[]{sourceUid, job.getUid()} : new int[]{sourceUid};
                if (job.isProxyJob()) {
                    strArr = new java.lang.String[]{null, job.getSourceTag()};
                } else {
                    strArr = new java.lang.String[]{job.getSourceTag()};
                }
                com.android.internal.util.FrameworkStatsLog.write(8, iArr, strArr, job.getBatteryName(), 1, -1, job.getStandbyBucket(), job.getLoggingJobId(), job.hasChargingConstraint(), job.hasBatteryNotLowConstraint(), job.hasStorageNotLowConstraint(), job.hasTimingDelayConstraint(), job.hasDeadlineConstraint(), job.hasIdleConstraint(), job.hasConnectivityConstraint(), job.hasContentTriggerConstraint(), job.isRequestedExpeditedJob(), job.shouldTreatAsExpeditedJob(), 0, job.getJob().isPrefetch(), job.getJob().getPriority(), job.getEffectivePriority(), job.getNumPreviousAttempts(), job.getJob().getMaxExecutionDelayMillis(), isDeadlineExpired, job.isConstraintSatisfied(1), job.isConstraintSatisfied(2), job.isConstraintSatisfied(8), job.isConstraintSatisfied(Integer.MIN_VALUE), job.isConstraintSatisfied(4), job.isConstraintSatisfied(268435456), job.isConstraintSatisfied(67108864), this.mExecutionStartTimeElapsed - job.enqueueTime, job.getJob().isUserInitiated(), job.shouldTreatAsUserInitiatedJob(), job.getJob().isPeriodic(), job.getJob().getMinLatencyMillis(), job.getEstimatedNetworkDownloadBytes(), job.getEstimatedNetworkUploadBytes(), job.getWorkCount(), android.app.ActivityManager.processStateAmToProto(this.mService.getUidProcState(job.getUid())), job.getNamespaceHash(), 0L, 0L, 0L, 0L, job.getJob().getIntervalMillis(), job.getJob().getFlexMillis(), job.hasFlexibilityConstraint(), job.isConstraintSatisfied(2097152), job.canApplyTransportAffinities(), job.getNumAppliedFlexibleConstraints(), job.getNumDroppedFlexibleConstraints(), job.getFilteredTraceTag(), job.getFilteredDebugTags());
                sEnqueuedJwiAtJobStart.logSampleWithUid(job.getUid(), job.getWorkCount());
                java.lang.String sourcePackage = job.getSourcePackageName();
                if (android.os.Trace.isTagEnabled(524288L)) {
                    android.os.Trace.asyncTraceForTrackBegin(524288L, "JobScheduler", job.computeSystemTraceTag(), getId());
                }
                if (job.getAppTraceTag() != null) {
                    android.os.Trace.asyncTraceForTrackBegin(4096L, "JobScheduler", job.getAppTraceTag(), job.getJobId());
                }
                try {
                    this.mBatteryStats.noteJobStart(job.getBatteryName(), job.getSourceUid());
                } catch (android.os.RemoteException e2) {
                }
                int jobUserId = job.getSourceUserId();
                this.mUsageStatsManagerInternal.setLastJobRunTime(sourcePackage, jobUserId, this.mExecutionStartTimeElapsed);
                this.mAvailable = false;
                this.mStoppedReason = null;
                this.mStoppedTime = 0L;
                job.startedAsExpeditedJob = job.shouldTreatAsExpeditedJob();
                job.startedAsUserInitiatedJob = job.shouldTreatAsUserInitiatedJob();
                job.startedWithForegroundFlag = startedWithForegroundFlag;
                return true;
            }
            android.util.Slog.e(TAG, "Starting new runnable but context is unavailable > Error.");
            return false;
        }
    }

    private boolean canGetNetworkInformation(com.android.server.job.controllers.JobStatus job) {
        if (job.getJob().getRequiredNetwork() == null) {
            return false;
        }
        int uid = job.getUid();
        if (android.app.compat.CompatChanges.isChangeEnabled(271850009L, uid)) {
            java.lang.String pkgName = job.getServiceComponent().getPackageName();
            return hasPermissionForDelivery(uid, pkgName, "android.permission.ACCESS_NETWORK_STATE");
        }
        return true;
    }

    private boolean hasPermissionForDelivery(int uid, java.lang.String pkgName, java.lang.String permission) {
        int result = android.content.PermissionChecker.checkPermissionForDataDelivery(this.mContext, permission, -1, uid, pkgName, (java.lang.String) null, "network info via JS");
        return result == 0;
    }

    com.android.server.job.controllers.JobStatus getRunningJobLocked() {
        return this.mRunningJob;
    }

    int getRunningJobWorkType() {
        return this.mRunningJobWorkType;
    }

    private java.lang.String getRunningJobNameLocked() {
        return this.mRunningJob != null ? this.mRunningJob.toShortString() : "<null>";
    }

    void cancelExecutingJobLocked(int reason, int internalStopReason, java.lang.String debugReason) {
        doCancelLocked(reason, internalStopReason, debugReason);
    }

    void markForProcessDeathLocked(int reason, int internalStopReason, java.lang.String debugReason) {
        if (this.mVerb == 4) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Too late to mark for death (verb=" + this.mVerb + "), ignoring.");
                return;
            }
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Marking " + this.mRunningJob.toShortString() + " for death because " + reason + ":" + debugReason);
        }
        this.mDeathMarkStopReason = reason;
        this.mDeathMarkInternalStopReason = internalStopReason;
        this.mDeathMarkDebugReason = debugReason;
        if (this.mParams.getStopReason() == 0) {
            this.mParams.setStopReason(reason, internalStopReason, debugReason);
        }
    }

    int getPreferredUid() {
        return this.mPreferredUid;
    }

    void clearPreferredUid() {
        this.mPreferredUid = -1;
    }

    int getId() {
        return hashCode();
    }

    long getExecutionStartTimeElapsed() {
        return this.mExecutionStartTimeElapsed;
    }

    long getTimeoutElapsed() {
        return this.mTimeoutElapsed;
    }

    long getRemainingGuaranteedTimeMs(long nowElapsed) {
        return java.lang.Math.max(0L, (this.mExecutionStartTimeElapsed + this.mMinExecutionGuaranteeMillis) - nowElapsed);
    }

    void informOfNetworkChangeLocked(android.net.Network newNetwork) {
        if (newNetwork != null && this.mRunningJob != null && !canGetNetworkInformation(this.mRunningJob)) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Skipping network change call because of missing permissions");
            }
        } else {
            if (this.mVerb != 2) {
                android.util.Slog.w(TAG, "Sending onNetworkChanged for a job that isn't started. " + this.mRunningJob);
                if (this.mVerb == 0 || this.mVerb == 1) {
                    this.mPendingNetworkChange = newNetwork;
                    return;
                }
                return;
            }
            try {
                this.mParams.setNetwork(newNetwork);
                this.mPendingNetworkChange = null;
                this.service.onNetworkChanged(this.mParams);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error sending onNetworkChanged to client.", e);
                closeAndCleanupJobLocked(true, "host crashed when trying to inform of network change");
            }
        }
    }

    boolean isWithinExecutionGuaranteeTime() {
        return com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() < this.mExecutionStartTimeElapsed + this.mMinExecutionGuaranteeMillis;
    }

    boolean stopIfExecutingLocked(java.lang.String pkgName, int userId, java.lang.String namespace, boolean matchJobId, int jobId, int stopReason, int internalStopReason) {
        com.android.server.job.controllers.JobStatus executing = getRunningJobLocked();
        if (executing == null) {
            return false;
        }
        if (userId == -1 || userId == executing.getUserId()) {
            if ((pkgName == null || pkgName.equals(executing.getSourcePackageName())) && java.util.Objects.equals(namespace, executing.getNamespace())) {
                if ((!matchJobId || jobId == executing.getJobId()) && this.mVerb == 2) {
                    this.mParams.setStopReason(stopReason, internalStopReason, "stop from shell");
                    sendStopMessageLocked("stop from shell");
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    android.util.Pair<java.lang.Long, java.lang.Long> getEstimatedNetworkBytes() {
        return android.util.Pair.create(java.lang.Long.valueOf(this.mEstimatedDownloadBytes), java.lang.Long.valueOf(this.mEstimatedUploadBytes));
    }

    android.util.Pair<java.lang.Long, java.lang.Long> getTransferredNetworkBytes() {
        return android.util.Pair.create(java.lang.Long.valueOf(this.mTransferredDownloadBytes), java.lang.Long.valueOf(this.mTransferredUploadBytes));
    }

    void doJobFinished(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, boolean reschedule) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!verifyCallerLocked(cb)) {
                    return;
                }
                this.mParams.setStopReason(0, 10, "app called jobFinished");
                doCallbackLocked(reschedule, "app called jobFinished");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAcknowledgeGetTransferredDownloadBytesMessage(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, int workId, long transferredBytes) {
        synchronized (this.mLock) {
            if (verifyCallerLocked(cb)) {
                this.mTransferredDownloadBytes = transferredBytes;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAcknowledgeGetTransferredUploadBytesMessage(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, int workId, long transferredBytes) {
        synchronized (this.mLock) {
            if (verifyCallerLocked(cb)) {
                this.mTransferredUploadBytes = transferredBytes;
            }
        }
    }

    void doAcknowledgeStopMessage(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, boolean reschedule) {
        doCallback(cb, reschedule, null);
    }

    void doAcknowledgeStartMessage(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, boolean ongoing) {
        doCallback(cb, ongoing, "finished start");
    }

    android.app.job.JobWorkItem doDequeueWork(com.android.server.job.JobServiceContext.JobCallback cb, int jobId) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!assertCallerLocked(cb)) {
                    return null;
                }
                if (this.mVerb != 3 && this.mVerb != 4) {
                    android.app.job.JobWorkItem work = this.mRunningJob.dequeueWorkLocked();
                    if (work == null && !this.mRunningJob.hasExecutingWorkLocked()) {
                        this.mParams.setStopReason(0, 10, "last work dequeued");
                        doCallbackLocked(false, "last work dequeued");
                    } else if (work != null) {
                        this.mService.mJobs.touchJob(this.mRunningJob);
                    }
                    return work;
                }
                return null;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    boolean doCompleteWork(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, int workId) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!assertCallerLocked(cb)) {
                    return true;
                }
                if (this.mRunningJob.completeWorkLocked(workId)) {
                    this.mService.mJobs.touchJob(this.mRunningJob);
                    return true;
                }
                android.os.Binder.restoreCallingIdentity(ident);
                return false;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doUpdateEstimatedNetworkBytes(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, android.app.job.JobWorkItem item, long downloadBytes, long uploadBytes) {
        synchronized (this.mLock) {
            if (verifyCallerLocked(cb)) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_estimated_network_bytes_updated", this.mRunningJob.getUid());
                sUpdatedEstimatedNetworkDownloadKBLogger.logSample(com.android.server.job.JobSchedulerService.safelyScaleBytesToKBForHistogram(downloadBytes));
                sUpdatedEstimatedNetworkUploadKBLogger.logSample(com.android.server.job.JobSchedulerService.safelyScaleBytesToKBForHistogram(uploadBytes));
                if (this.mEstimatedDownloadBytes != -1 && downloadBytes != -1) {
                    if (this.mEstimatedDownloadBytes < downloadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_estimated_network_download_bytes_increased", this.mRunningJob.getUid());
                    } else if (this.mEstimatedDownloadBytes > downloadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_estimated_network_download_bytes_decreased", this.mRunningJob.getUid());
                    }
                }
                if (this.mEstimatedUploadBytes != -1 && uploadBytes != -1) {
                    if (this.mEstimatedUploadBytes < uploadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_estimated_network_upload_bytes_increased", this.mRunningJob.getUid());
                    } else if (this.mEstimatedUploadBytes > uploadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_estimated_network_upload_bytes_decreased", this.mRunningJob.getUid());
                    }
                }
                this.mEstimatedDownloadBytes = downloadBytes;
                this.mEstimatedUploadBytes = uploadBytes;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doUpdateTransferredNetworkBytes(com.android.server.job.JobServiceContext.JobCallback cb, int jobId, android.app.job.JobWorkItem item, long downloadBytes, long uploadBytes) {
        synchronized (this.mLock) {
            if (verifyCallerLocked(cb)) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_transferred_network_bytes_updated", this.mRunningJob.getUid());
                sTransferredNetworkDownloadKBHighWaterMarkLogger.logSample(com.android.server.job.JobSchedulerService.safelyScaleBytesToKBForHistogram(downloadBytes));
                sTransferredNetworkUploadKBHighWaterMarkLogger.logSample(com.android.server.job.JobSchedulerService.safelyScaleBytesToKBForHistogram(uploadBytes));
                if (this.mTransferredDownloadBytes != -1 && downloadBytes != -1) {
                    if (this.mTransferredDownloadBytes < downloadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_transferred_network_download_bytes_increased", this.mRunningJob.getUid());
                    } else if (this.mTransferredDownloadBytes > downloadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_transferred_network_download_bytes_decreased", this.mRunningJob.getUid());
                    }
                }
                if (this.mTransferredUploadBytes != -1 && uploadBytes != -1) {
                    if (this.mTransferredUploadBytes < uploadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_transferred_network_upload_bytes_increased", this.mRunningJob.getUid());
                    } else if (this.mTransferredUploadBytes > uploadBytes) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_transferred_network_upload_bytes_decreased", this.mRunningJob.getUid());
                    }
                }
                this.mTransferredDownloadBytes = downloadBytes;
                this.mTransferredUploadBytes = uploadBytes;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSetNotification(com.android.server.job.JobServiceContext.JobCallback cb, int jodId, int notificationId, android.app.Notification notification, int jobEndNotificationPolicy) {
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!verifyCallerLocked(cb)) {
                    return;
                }
                if (callingUid != this.mRunningJob.getUid()) {
                    android.util.Slog.wtfStack(TAG, "Calling UID isn't the same as running job's UID...");
                    throw new java.lang.SecurityException("Can't post notification on behalf of another app");
                }
                java.lang.String callingPkgName = this.mRunningJob.getServiceComponent().getPackageName();
                this.mNotificationCoordinator.enqueueNotification(this, callingPkgName, callingPid, callingUid, notificationId, notification, jobEndNotificationPolicy);
                if (this.mAwaitingNotification) {
                    this.mAwaitingNotification = false;
                    if (this.mVerb == 2) {
                        scheduleOpTimeOutLocked();
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        synchronized (this.mLock) {
            com.android.server.job.controllers.JobStatus runningJob = this.mRunningJob;
            if (runningJob != null && name.equals(runningJob.getServiceComponent())) {
                this.service = android.app.job.IJobService.Stub.asInterface(service);
                doServiceBoundLocked();
                return;
            }
            closeAndCleanupJobLocked(true, "connected for different component");
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName name) {
        synchronized (this.mLock) {
            if (this.mDeathMarkStopReason != 0) {
                this.mParams.setStopReason(this.mDeathMarkStopReason, this.mDeathMarkInternalStopReason, this.mDeathMarkDebugReason);
            } else if (this.mRunningJob != null) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_unexpected_service_disconnects", this.mRunningJob.getUid());
            }
            closeAndCleanupJobLocked(true, "unexpectedly disconnected");
        }
    }

    @Override // android.content.ServiceConnection
    public void onBindingDied(android.content.ComponentName name) {
        synchronized (this.mLock) {
            if (this.mRunningJob == null) {
                android.util.Slog.e(TAG, "Binding died for " + name.getPackageName() + " but no running job on this context");
            } else if (this.mRunningJob.getServiceComponent().equals(name)) {
                android.util.Slog.e(TAG, "Binding died for " + this.mRunningJob.getSourceUserId() + ":" + name.getPackageName());
            } else {
                android.util.Slog.e(TAG, "Binding died for " + name.getPackageName() + " but context is running a different job");
            }
            closeAndCleanupJobLocked(true, "binding died");
        }
    }

    @Override // android.content.ServiceConnection
    public void onNullBinding(android.content.ComponentName name) {
        synchronized (this.mLock) {
            if (this.mRunningJob == null) {
                android.util.Slog.wtf(TAG, "Got null binding for " + name.getPackageName() + " but no running job on this context");
            } else if (this.mRunningJob.getServiceComponent().equals(name)) {
                android.util.Slog.wtf(TAG, "Got null binding for " + this.mRunningJob.getSourceUserId() + ":" + name.getPackageName());
            } else {
                android.util.Slog.wtf(TAG, "Got null binding for " + name.getPackageName() + " but context is running a different job");
            }
            closeAndCleanupJobLocked(false, "null binding");
        }
    }

    private boolean verifyCallerLocked(com.android.server.job.JobServiceContext.JobCallback cb) {
        if (this.mRunningCallback != cb) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Stale callback received, ignoring.");
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean assertCallerLocked(com.android.server.job.JobServiceContext.JobCallback cb) {
        if (!verifyCallerLocked(cb)) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (!this.mPreviousJobHadSuccessfulFinish && nowElapsed - this.mLastUnsuccessfulFinishElapsed < 15000) {
                return false;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("Caller no longer running");
            if (cb.mStoppedReason != null) {
                sb.append(", last stopped ");
                android.util.TimeUtils.formatDuration(nowElapsed - cb.mStoppedTime, sb);
                sb.append(" because: ");
                sb.append(cb.mStoppedReason);
            }
            throw new java.lang.SecurityException(sb.toString());
        }
        return true;
    }

    private class JobServiceHandler extends android.os.Handler {
        JobServiceHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 0:
                    synchronized (com.android.server.job.JobServiceContext.this.mLock) {
                        if (message.obj == com.android.server.job.JobServiceContext.this.mRunningCallback) {
                            com.android.server.job.JobServiceContext.this.handleOpTimeoutLocked();
                        } else {
                            com.android.server.job.JobServiceContext.JobCallback jc = (com.android.server.job.JobServiceContext.JobCallback) message.obj;
                            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
                            sb.append("Ignoring timeout of no longer active job");
                            if (jc.mStoppedReason != null) {
                                sb.append(", stopped ");
                                android.util.TimeUtils.formatDuration(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() - jc.mStoppedTime, sb);
                                sb.append(" because: ");
                                sb.append(jc.mStoppedReason);
                            }
                            android.util.Slog.w(com.android.server.job.JobServiceContext.TAG, sb.toString());
                        }
                        break;
                    }
                    return;
                default:
                    android.util.Slog.e(com.android.server.job.JobServiceContext.TAG, "Unrecognised message: " + message);
                    return;
            }
        }
    }

    void doServiceBoundLocked() {
        removeOpTimeOutLocked();
        handleServiceBoundLocked();
    }

    void doCallback(com.android.server.job.JobServiceContext.JobCallback cb, boolean reschedule, java.lang.String reason) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (!verifyCallerLocked(cb)) {
                    return;
                }
                doCallbackLocked(reschedule, reason);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void doCallbackLocked(boolean reschedule, java.lang.String reason) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "doCallback of : " + this.mRunningJob + " v:" + VERB_STRINGS[this.mVerb]);
        }
        removeOpTimeOutLocked();
        if (this.mVerb == 1) {
            handleStartedLocked(reschedule);
            return;
        }
        if (this.mVerb == 2 || this.mVerb == 3) {
            handleFinishedLocked(reschedule, reason);
        } else if (DEBUG) {
            android.util.Slog.d(TAG, "Unrecognised callback: " + this.mRunningJob);
        }
    }

    private void doCancelLocked(int stopReasonCode, int internalStopReasonCode, java.lang.String debugReason) {
        if (this.mVerb == 4 || this.mVerb == 3) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Too late to process cancel for context (verb=" + this.mVerb + "), ignoring.");
                return;
            }
            return;
        }
        if (this.mRunningJob.startedAsExpeditedJob && stopReasonCode == 10) {
            long earliestStopTimeElapsed = this.mExecutionStartTimeElapsed + this.mMinExecutionGuaranteeMillis;
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (nowElapsed < earliestStopTimeElapsed) {
                this.mPendingStopReason = stopReasonCode;
                this.mPendingInternalStopReason = internalStopReasonCode;
                this.mPendingDebugStopReason = debugReason;
                return;
            }
        }
        this.mParams.setStopReason(stopReasonCode, internalStopReasonCode, debugReason);
        if (stopReasonCode == 2) {
            this.mPreferredUid = this.mRunningJob != null ? this.mRunningJob.getUid() : -1;
        }
        handleCancelLocked(debugReason);
    }

    private void handleServiceBoundLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleServiceBound for " + getRunningJobNameLocked());
        }
        if (this.mVerb != 0) {
            android.util.Slog.e(TAG, "Sending onStartJob for a job that isn't pending. " + VERB_STRINGS[this.mVerb]);
            closeAndCleanupJobLocked(false, "started job not pending");
            return;
        }
        if (this.mCancelled) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Job cancelled while waiting for bind to complete. " + this.mRunningJob);
            }
            closeAndCleanupJobLocked(true, "cancelled while waiting for bind");
        } else {
            try {
                this.mVerb = 1;
                scheduleOpTimeOutLocked();
                this.service.startJob(this.mParams);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Error sending onStart message to '" + this.mRunningJob.getServiceComponent().getShortClassName() + "' ", e);
            }
        }
    }

    private void handleStartedLocked(boolean workOngoing) {
        switch (this.mVerb) {
            case 1:
                this.mVerb = 2;
                if (!workOngoing) {
                    handleFinishedLocked(false, "onStartJob returned false");
                } else if (this.mCancelled) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Job cancelled while waiting for onStartJob to complete.");
                    }
                    handleCancelLocked(null);
                } else {
                    scheduleOpTimeOutLocked();
                    if (this.mPendingNetworkChange != null && !java.util.Objects.equals(this.mParams.getNetwork(), this.mPendingNetworkChange)) {
                        informOfNetworkChangeLocked(this.mPendingNetworkChange);
                    }
                    if (this.mRunningJob.isUserVisibleJob()) {
                        this.mService.informObserversOfUserVisibleJobChange(this, this.mRunningJob, true);
                    }
                }
                break;
            default:
                android.util.Slog.e(TAG, "Handling started job but job wasn't starting! Was " + VERB_STRINGS[this.mVerb] + ".");
                break;
        }
    }

    private void handleFinishedLocked(boolean reschedule, java.lang.String reason) {
        switch (this.mVerb) {
            case 2:
            case 3:
                closeAndCleanupJobLocked(reschedule, reason);
                break;
            default:
                android.util.Slog.e(TAG, "Got an execution complete message for a job that wasn't beingexecuted. Was " + VERB_STRINGS[this.mVerb] + ".");
                break;
        }
    }

    private void handleCancelLocked(java.lang.String reason) {
        if (com.android.server.job.JobSchedulerService.DEBUG) {
            android.util.Slog.d(TAG, "Handling cancel for: " + this.mRunningJob.getJobId() + " " + VERB_STRINGS[this.mVerb]);
        }
        switch (this.mVerb) {
            case 0:
            case 1:
                this.mCancelled = true;
                applyStoppedReasonLocked(reason);
                break;
            case 2:
                sendStopMessageLocked(reason);
                break;
            case 3:
                break;
            default:
                android.util.Slog.e(TAG, "Cancelling a job without a valid verb: " + this.mVerb);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOpTimeoutLocked() {
        switch (this.mVerb) {
            case 0:
                onSlowAppResponseLocked(true, true, "job_scheduler.value_cntr_w_uid_slow_app_response_binding", "timed out while binding", "Timed out while trying to bind", false);
                break;
            case 1:
                onSlowAppResponseLocked(false, true, "job_scheduler.value_cntr_w_uid_slow_app_response_on_start_job", "timed out while starting", "No response to onStartJob", android.app.compat.CompatChanges.isChangeEnabled(ANR_PRE_UDC_APIS_ON_SLOW_RESPONSES, this.mRunningJob.getUid()));
                break;
            case 2:
                if (this.mPendingStopReason != 0) {
                    if (this.mService.isReadyToBeExecutedLocked(this.mRunningJob, false)) {
                        this.mPendingStopReason = 0;
                        this.mPendingInternalStopReason = 0;
                        this.mPendingDebugStopReason = null;
                    } else {
                        android.util.Slog.i(TAG, "JS was waiting to stop this job. Sending onStop: " + getRunningJobNameLocked());
                        this.mParams.setStopReason(this.mPendingStopReason, this.mPendingInternalStopReason, this.mPendingDebugStopReason);
                        sendStopMessageLocked(this.mPendingDebugStopReason);
                    }
                }
                long latestStopTimeElapsed = this.mExecutionStartTimeElapsed + this.mMaxExecutionTimeMillis;
                long earliestStopTimeElapsed = this.mExecutionStartTimeElapsed + this.mMinExecutionGuaranteeMillis;
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                if (nowElapsed >= latestStopTimeElapsed) {
                    android.util.Slog.i(TAG, "Client timed out while executing (no jobFinished received). Sending onStop: " + getRunningJobNameLocked());
                    this.mParams.setStopReason(3, 3, "client timed out");
                    sendStopMessageLocked("timeout while executing");
                } else if (nowElapsed >= earliestStopTimeElapsed) {
                    java.lang.String reason = this.mJobConcurrencyManager.shouldStopRunningJobLocked(this);
                    if (reason != null) {
                        android.util.Slog.i(TAG, "Stopping client after min execution time: " + getRunningJobNameLocked() + " because " + reason);
                        this.mParams.setStopReason(4, 3, reason);
                        sendStopMessageLocked(reason);
                    } else {
                        android.util.Slog.i(TAG, "Letting " + getRunningJobNameLocked() + " continue to run past min execution time");
                        scheduleOpTimeOutLocked();
                    }
                } else if (this.mAwaitingNotification) {
                    onSlowAppResponseLocked(true, true, "job_scheduler.value_cntr_w_uid_slow_app_response_set_notification", "timed out while stopping", "required notification not provided", true);
                } else {
                    long timeSinceDurationStampTimeMs = nowElapsed - this.mLastExecutionDurationStampTimeElapsed;
                    if (timeSinceDurationStampTimeMs < 300000) {
                        android.util.Slog.e(TAG, "Unexpected op timeout while EXECUTING");
                    }
                    this.mRunningJob.incrementCumulativeExecutionTime(timeSinceDurationStampTimeMs);
                    this.mService.mJobs.touchJob(this.mRunningJob);
                    this.mLastExecutionDurationStampTimeElapsed = nowElapsed;
                    scheduleOpTimeOutLocked();
                }
                break;
            case 3:
                onSlowAppResponseLocked(true, false, "job_scheduler.value_cntr_w_uid_slow_app_response_on_stop_job", "timed out while stopping", "No response to onStopJob", android.app.compat.CompatChanges.isChangeEnabled(ANR_PRE_UDC_APIS_ON_SLOW_RESPONSES, this.mRunningJob.getUid()));
                break;
            default:
                android.util.Slog.e(TAG, "Handling timeout for an invalid job state: " + getRunningJobNameLocked() + ", dropping.");
                closeAndCleanupJobLocked(false, "invalid timeout");
                break;
        }
    }

    private void sendStopMessageLocked(java.lang.String reason) {
        removeOpTimeOutLocked();
        if (this.mVerb != 2) {
            android.util.Slog.e(TAG, "Sending onStopJob for a job that isn't started. " + this.mRunningJob);
            closeAndCleanupJobLocked(false, reason);
            return;
        }
        try {
            applyStoppedReasonLocked(reason);
            this.mVerb = 3;
            scheduleOpTimeOutLocked();
            this.service.stopJob(this.mParams);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error sending onStopJob to client.", e);
            closeAndCleanupJobLocked(true, "host crashed when trying to stop");
        }
    }

    private void onSlowAppResponseLocked(boolean reschedule, boolean updateStopReasons, java.lang.String texCounterMetricId, java.lang.String debugReason, java.lang.String anrMessage, boolean triggerAnr) {
        android.util.Slog.w(TAG, anrMessage + " for " + getRunningJobNameLocked());
        com.android.modules.expresslog.Counter.logIncrementWithUid(texCounterMetricId, this.mRunningJob.getUid());
        if (updateStopReasons) {
            this.mParams.setStopReason(0, 12, debugReason);
        }
        if (triggerAnr && this.mRunningJob.serviceProcessName != null) {
            this.mActivityManagerInternal.appNotResponding(this.mRunningJob.serviceProcessName, this.mRunningJob.getUid(), com.android.internal.os.TimeoutRecord.forJobService(anrMessage));
        }
        closeAndCleanupJobLocked(reschedule, debugReason);
    }

    private void closeAndCleanupJobLocked(boolean reschedule, java.lang.String loggingDebugReason) {
        int reschedulingStopReason;
        int reschedulingInternalStopReason;
        java.lang.String[] strArr;
        if (this.mVerb == 4) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Cleaning up " + this.mRunningJob.toShortString() + " reschedule=" + reschedule + " reason=" + loggingDebugReason);
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        applyStoppedReasonLocked(loggingDebugReason);
        com.android.server.job.controllers.JobStatus completedJob = this.mRunningJob;
        completedJob.incrementCumulativeExecutionTime(nowElapsed - this.mLastExecutionDurationStampTimeElapsed);
        int loggingStopReason = this.mParams.getStopReason();
        int loggingInternalStopReason = this.mParams.getInternalStopReasonCode();
        if (this.mDeathMarkStopReason != 0) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Job marked for death because of " + android.app.job.JobParameters.getInternalReasonCodeDescription(this.mDeathMarkInternalStopReason) + ": " + this.mDeathMarkDebugReason);
            }
            int reschedulingStopReason2 = this.mDeathMarkStopReason;
            reschedulingStopReason = reschedulingStopReason2;
            reschedulingInternalStopReason = this.mDeathMarkInternalStopReason;
        } else {
            reschedulingStopReason = loggingStopReason;
            reschedulingInternalStopReason = loggingInternalStopReason;
        }
        this.mPreviousJobHadSuccessfulFinish = loggingInternalStopReason == 10;
        if (!this.mPreviousJobHadSuccessfulFinish) {
            this.mLastUnsuccessfulFinishElapsed = nowElapsed;
        }
        this.mJobPackageTracker.noteInactive(completedJob, loggingInternalStopReason, loggingDebugReason);
        int sourceUid = completedJob.getSourceUid();
        int[] iArr = completedJob.isProxyJob() ? new int[]{sourceUid, completedJob.getUid()} : new int[]{sourceUid};
        if (completedJob.isProxyJob()) {
            strArr = new java.lang.String[]{null, completedJob.getSourceTag()};
        } else {
            strArr = new java.lang.String[]{completedJob.getSourceTag()};
        }
        int reschedulingInternalStopReason2 = reschedulingInternalStopReason;
        int reschedulingStopReason3 = reschedulingStopReason;
        com.android.internal.util.FrameworkStatsLog.write(8, iArr, strArr, completedJob.getBatteryName(), 0, loggingInternalStopReason, completedJob.getStandbyBucket(), completedJob.getLoggingJobId(), completedJob.hasChargingConstraint(), completedJob.hasBatteryNotLowConstraint(), completedJob.hasStorageNotLowConstraint(), completedJob.hasTimingDelayConstraint(), completedJob.hasDeadlineConstraint(), completedJob.hasIdleConstraint(), completedJob.hasConnectivityConstraint(), completedJob.hasContentTriggerConstraint(), completedJob.isRequestedExpeditedJob(), completedJob.startedAsExpeditedJob, loggingStopReason, completedJob.getJob().isPrefetch(), completedJob.getJob().getPriority(), completedJob.getEffectivePriority(), completedJob.getNumPreviousAttempts(), completedJob.getJob().getMaxExecutionDelayMillis(), this.mParams.isOverrideDeadlineExpired(), completedJob.isConstraintSatisfied(1), completedJob.isConstraintSatisfied(2), completedJob.isConstraintSatisfied(8), completedJob.isConstraintSatisfied(Integer.MIN_VALUE), completedJob.isConstraintSatisfied(4), completedJob.isConstraintSatisfied(268435456), completedJob.isConstraintSatisfied(67108864), this.mExecutionStartTimeElapsed - completedJob.enqueueTime, completedJob.getJob().isUserInitiated(), completedJob.startedAsUserInitiatedJob, completedJob.getJob().isPeriodic(), completedJob.getJob().getMinLatencyMillis(), completedJob.getEstimatedNetworkDownloadBytes(), completedJob.getEstimatedNetworkUploadBytes(), completedJob.getWorkCount(), android.app.ActivityManager.processStateAmToProto(this.mService.getUidProcState(completedJob.getUid())), completedJob.getNamespaceHash(), android.net.TrafficStats.getUidRxBytes(completedJob.getSourceUid()) - this.mInitialDownloadedBytesFromSource, android.net.TrafficStats.getUidTxBytes(completedJob.getSourceUid()) - this.mInitialUploadedBytesFromSource, android.net.TrafficStats.getUidRxBytes(completedJob.getUid()) - this.mInitialDownloadedBytesFromCalling, android.net.TrafficStats.getUidTxBytes(completedJob.getUid()) - this.mInitialUploadedBytesFromCalling, completedJob.getJob().getIntervalMillis(), completedJob.getJob().getFlexMillis(), completedJob.hasFlexibilityConstraint(), completedJob.isConstraintSatisfied(2097152), completedJob.canApplyTransportAffinities(), completedJob.getNumAppliedFlexibleConstraints(), completedJob.getNumDroppedFlexibleConstraints(), completedJob.getFilteredTraceTag(), completedJob.getFilteredDebugTags());
        if (android.os.Trace.isTagEnabled(524288L)) {
            android.os.Trace.asyncTraceForTrackEnd(524288L, "JobScheduler", getId());
        }
        if (completedJob.getAppTraceTag() != null) {
            android.os.Trace.asyncTraceForTrackEnd(4096L, "JobScheduler", completedJob.getJobId());
        }
        try {
            try {
                this.mBatteryStats.noteJobFinish(this.mRunningJob.getBatteryName(), this.mRunningJob.getSourceUid(), loggingInternalStopReason);
            } catch (android.os.RemoteException e) {
            }
        } catch (android.os.RemoteException e2) {
        }
        this.mNotificationCoordinator.removeNotificationAssociation(this, reschedulingStopReason3, completedJob);
        if (this.mWakeLock != null) {
            this.mWakeLock.release();
        }
        int workType = this.mRunningJobWorkType;
        try {
            this.mContext.unbindService(this);
        } catch (java.lang.Exception e3) {
            android.util.Slog.e(TAG, "unbind service got trouble and we will ignore it, wtf!!!");
        }
        this.mWakeLock = null;
        this.mRunningJob = null;
        this.mRunningJobWorkType = 0;
        this.mRunningCallback = null;
        this.mParams = null;
        this.mVerb = 4;
        this.mCancelled = false;
        this.service = null;
        this.mAvailable = true;
        this.mDeathMarkStopReason = 0;
        this.mDeathMarkInternalStopReason = 0;
        this.mDeathMarkDebugReason = null;
        this.mLastExecutionDurationStampTimeElapsed = 0L;
        this.mPendingStopReason = 0;
        this.mPendingInternalStopReason = 0;
        this.mPendingDebugStopReason = null;
        this.mPendingNetworkChange = null;
        removeOpTimeOutLocked();
        if (completedJob.isUserVisibleJob()) {
            this.mService.informObserversOfUserVisibleJobChange(this, completedJob, false);
        }
        this.mCompletedListener.onJobCompletedLocked(completedJob, reschedulingStopReason3, reschedulingInternalStopReason2, reschedule);
        this.mJobConcurrencyManager.onJobCompletedLocked(this, completedJob, workType);
    }

    private void applyStoppedReasonLocked(java.lang.String reason) {
        if (reason != null && this.mStoppedReason == null) {
            this.mStoppedReason = reason;
            this.mStoppedTime = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (this.mRunningCallback != null) {
                this.mRunningCallback.mStoppedReason = this.mStoppedReason;
                this.mRunningCallback.mStoppedTime = this.mStoppedTime;
            }
        }
    }

    private void scheduleOpTimeOutLocked() {
        long timeoutMillis;
        long minTimeout;
        removeOpTimeOutLocked();
        switch (this.mVerb) {
            case 0:
                timeoutMillis = OP_BIND_TIMEOUT_MILLIS;
                break;
            case 1:
            default:
                timeoutMillis = OP_TIMEOUT_MILLIS;
                break;
            case 2:
                long earliestStopTimeElapsed = this.mExecutionStartTimeElapsed + this.mMinExecutionGuaranteeMillis;
                long latestStopTimeElapsed = this.mExecutionStartTimeElapsed + this.mMaxExecutionTimeMillis;
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                if (nowElapsed < earliestStopTimeElapsed) {
                    minTimeout = earliestStopTimeElapsed - nowElapsed;
                } else {
                    minTimeout = latestStopTimeElapsed - nowElapsed;
                }
                if (this.mAwaitingNotification) {
                    minTimeout = java.lang.Math.min(minTimeout, NOTIFICATION_TIMEOUT_MILLIS);
                }
                timeoutMillis = java.lang.Math.min(minTimeout, 300000L);
                break;
        }
        long timeoutMillis2 = this.mJobServiceContextExt.translateJobTimeout(this.mRunningJob, this.mVerb, timeoutMillis);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Scheduling time out for '" + this.mRunningJob.getServiceComponent().getShortClassName() + "' jId: " + this.mParams.getJobId() + ", in " + (timeoutMillis2 / 1000) + " s");
        }
        android.os.Message m = this.mCallbackHandler.obtainMessage(0, this.mRunningCallback);
        this.mCallbackHandler.sendMessageDelayed(m, timeoutMillis2);
        this.mTimeoutElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() + timeoutMillis2;
    }

    private void removeOpTimeOutLocked() {
        this.mCallbackHandler.removeMessages(0);
    }

    void dumpLocked(android.util.IndentingPrintWriter pw, long nowElapsed) {
        if (this.mRunningJob == null) {
            if (this.mStoppedReason != null) {
                pw.print("inactive since ");
                android.util.TimeUtils.formatDuration(this.mStoppedTime, nowElapsed, pw);
                pw.print(", stopped because: ");
                pw.println(this.mStoppedReason);
                return;
            }
            pw.println("inactive");
            return;
        }
        pw.println(this.mRunningJob.toShortString());
        pw.increaseIndent();
        pw.print("Running for: ");
        android.util.TimeUtils.formatDuration(nowElapsed - this.mExecutionStartTimeElapsed, pw);
        pw.print(", timeout at: ");
        android.util.TimeUtils.formatDuration(this.mTimeoutElapsed - nowElapsed, pw);
        pw.println();
        pw.print("Remaining execution limits: [");
        android.util.TimeUtils.formatDuration((this.mExecutionStartTimeElapsed + this.mMinExecutionGuaranteeMillis) - nowElapsed, pw);
        pw.print(", ");
        android.util.TimeUtils.formatDuration((this.mExecutionStartTimeElapsed + this.mMaxExecutionTimeMillis) - nowElapsed, pw);
        pw.print("]");
        if (this.mPendingStopReason != 0) {
            pw.print(" Pending stop because ");
            pw.print(this.mPendingStopReason);
            pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            pw.print(this.mPendingInternalStopReason);
            pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            pw.print(this.mPendingDebugStopReason);
        }
        pw.println();
        pw.decreaseIndent();
    }

    public com.android.server.job.IJobServiceContextWrapper getWrapper() {
        return this.mJobServiceContextWrapper;
    }

    private class JobServiceContextWrapper implements com.android.server.job.IJobServiceContextWrapper {
        private JobServiceContextWrapper() {
        }

        @Override // com.android.server.job.IJobServiceContextWrapper
        public com.android.server.job.IJobServiceContextExt getExtImpl() {
            return com.android.server.job.JobServiceContext.this.mJobServiceContextExt;
        }

        @Override // com.android.server.job.IJobServiceContextWrapper
        public android.app.job.JobParameters getParams() {
            return com.android.server.job.JobServiceContext.this.mParams;
        }

        @Override // com.android.server.job.IJobServiceContextWrapper
        public com.android.server.job.controllers.JobStatus getRunningJob() {
            return com.android.server.job.JobServiceContext.this.mRunningJob;
        }

        @Override // com.android.server.job.IJobServiceContextWrapper
        public java.lang.Object getLock() {
            return com.android.server.job.JobServiceContext.this.mLock;
        }
    }
}
