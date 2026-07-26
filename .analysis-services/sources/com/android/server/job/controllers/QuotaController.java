package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class QuotaController extends com.android.server.job.controllers.StateController {
    private static final java.lang.String ALARM_TAG_CLEANUP = "*job.cleanup*";
    private static final java.lang.String ALARM_TAG_QUOTA_CHECK = "*job.quota_check*";
    private static final boolean DEBUG;
    private static final long MAX_PERIOD_MS = 86400000;
    private static final int MSG_CHECK_PACKAGE = 2;
    private static final int MSG_CLEAN_UP_SESSIONS = 1;
    static final int MSG_END_GRACE_PERIOD = 6;
    private static final int MSG_PROCESS_USAGE_EVENT = 5;
    static final int MSG_REACHED_COUNT_QUOTA = 7;
    static final int MSG_REACHED_EJ_TIME_QUOTA = 4;
    static final int MSG_REACHED_TIME_QUOTA = 0;
    private static final int MSG_UID_PROCESS_STATE_CHANGED = 3;
    private static final int SYSTEM_APP_CHECK_FLAGS = 4993024;
    private static final java.lang.String TAG = "JobScheduler.Quota";
    private final android.app.AlarmManager mAlarmManager;
    private final long[] mAllowedTimePerPeriodMs;
    private final com.android.server.job.controllers.BackgroundJobsController mBackgroundJobsController;
    private final long[] mBucketPeriodsMs;
    private final com.android.server.job.controllers.ConnectivityController mConnectivityController;
    private final java.util.function.Consumer<java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent>> mDeleteOldEventsFunctor;
    private long mEJGracePeriodTempAllowlistMs;
    private long mEJGracePeriodTopAppMs;
    private long mEJLimitWindowSizeMs;
    private final long[] mEJLimitsMs;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.QuotaController.Timer> mEJPkgTimers;
    private long mEJRewardInteractionMs;
    private long mEJRewardNotificationSeenMs;
    private long mEJRewardTopAppMs;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.QuotaController.ShrinkableDebits> mEJStats;
    private final android.util.SparseArrayMap<java.lang.String, java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent>> mEJTimingSessions;
    private long mEJTopAppTimeChunkSizeMs;
    private final com.android.server.job.controllers.QuotaController.EarliestEndTimeFunctor mEarliestEndTimeFunctor;
    private long mEjLimitAdditionInstallerMs;
    private long mEjLimitAdditionSpecialMs;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.QuotaController.ExecutionStats[]> mExecutionStatsCache;
    private final android.util.SparseBooleanArray mForegroundUids;
    private final com.android.server.job.controllers.QuotaController.QcHandler mHandler;
    private final com.android.server.job.controllers.QuotaController.InQuotaAlarmQueue mInQuotaAlarmQueue;
    private final int[] mMaxBucketJobCounts;
    private final int[] mMaxBucketSessionCounts;
    private long mMaxExecutionTimeIntoQuotaMs;
    private long mMaxExecutionTimeMs;
    private int mMaxJobCountPerRateLimitingWindow;
    private int mMaxSessionCountPerRateLimitingWindow;
    private long mNextCleanupTimeElapsed;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.QuotaController.Timer> mPkgTimers;
    private final com.android.server.job.controllers.QuotaController.QcConstants mQcConstants;
    private long mQuotaBufferMs;
    private long mQuotaBumpAdditionalDurationMs;
    private int mQuotaBumpAdditionalJobCount;
    private int mQuotaBumpAdditionalSessionCount;
    private int mQuotaBumpLimit;
    private long mQuotaBumpWindowSizeMs;
    private long mRateLimitingWindowMs;
    private final android.app.AlarmManager.OnAlarmListener mSessionCleanupAlarmListener;
    private final android.util.SparseSetArray<java.lang.String> mSystemInstallers;
    private final android.util.SparseBooleanArray mTempAllowlistCache;
    private final android.util.SparseLongArray mTempAllowlistGraceCache;
    private final com.android.server.job.controllers.QuotaController.TimedEventTooOldPredicate mTimedEventTooOld;
    private final com.android.server.job.controllers.QuotaController.TimerChargingUpdateFunctor mTimerChargingUpdateFunctor;
    private final android.util.SparseArrayMap<java.lang.String, java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent>> mTimingEvents;
    private long mTimingSessionCoalescingDurationMs;
    private final android.util.SparseBooleanArray mTopAppCache;
    private final android.util.SparseLongArray mTopAppGraceCache;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.QuotaController.TopAppTimer> mTopAppTrackers;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mTopStartedJobs;
    private final android.util.SparseArrayMap<java.lang.String, android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mTrackedJobs;
    private final com.android.server.job.controllers.QuotaController.UidConstraintUpdater mUpdateUidConstraints;

    interface TimedEvent {
        void dump(android.util.IndentingPrintWriter indentingPrintWriter);

        long getEndTimeElapsed();
    }

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashLong(long val) {
        return (int) ((val >>> 32) ^ val);
    }

    static class ExecutionStats {
        public long allowedTimePerPeriodMs;
        public int bgJobCountInMaxPeriod;
        public int bgJobCountInWindow;
        public long executionTimeInMaxPeriodMs;
        public long executionTimeInWindowMs;
        public long expirationTimeElapsed;
        public long inQuotaTimeElapsed;
        public int jobCountInRateLimitingWindow;
        public int jobCountLimit;
        public long jobRateLimitExpirationTimeElapsed;
        public int sessionCountInRateLimitingWindow;
        public int sessionCountInWindow;
        public int sessionCountLimit;
        public long sessionRateLimitExpirationTimeElapsed;
        public long windowSizeMs;

        ExecutionStats() {
        }

        public java.lang.String toString() {
            return "expirationTime=" + this.expirationTimeElapsed + ", allowedTimePerPeriodMs=" + this.allowedTimePerPeriodMs + ", windowSizeMs=" + this.windowSizeMs + ", jobCountLimit=" + this.jobCountLimit + ", sessionCountLimit=" + this.sessionCountLimit + ", executionTimeInWindow=" + this.executionTimeInWindowMs + ", bgJobCountInWindow=" + this.bgJobCountInWindow + ", executionTimeInMaxPeriod=" + this.executionTimeInMaxPeriodMs + ", bgJobCountInMaxPeriod=" + this.bgJobCountInMaxPeriod + ", sessionCountInWindow=" + this.sessionCountInWindow + ", inQuotaTime=" + this.inQuotaTimeElapsed + ", rateLimitJobCountExpirationTime=" + this.jobRateLimitExpirationTimeElapsed + ", rateLimitJobCountWindow=" + this.jobCountInRateLimitingWindow + ", rateLimitSessionCountExpirationTime=" + this.sessionRateLimitExpirationTimeElapsed + ", rateLimitSessionCountWindow=" + this.sessionCountInRateLimitingWindow;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.job.controllers.QuotaController.ExecutionStats)) {
                return false;
            }
            com.android.server.job.controllers.QuotaController.ExecutionStats other = (com.android.server.job.controllers.QuotaController.ExecutionStats) obj;
            return this.expirationTimeElapsed == other.expirationTimeElapsed && this.allowedTimePerPeriodMs == other.allowedTimePerPeriodMs && this.windowSizeMs == other.windowSizeMs && this.jobCountLimit == other.jobCountLimit && this.sessionCountLimit == other.sessionCountLimit && this.executionTimeInWindowMs == other.executionTimeInWindowMs && this.bgJobCountInWindow == other.bgJobCountInWindow && this.executionTimeInMaxPeriodMs == other.executionTimeInMaxPeriodMs && this.sessionCountInWindow == other.sessionCountInWindow && this.bgJobCountInMaxPeriod == other.bgJobCountInMaxPeriod && this.inQuotaTimeElapsed == other.inQuotaTimeElapsed && this.jobRateLimitExpirationTimeElapsed == other.jobRateLimitExpirationTimeElapsed && this.jobCountInRateLimitingWindow == other.jobCountInRateLimitingWindow && this.sessionRateLimitExpirationTimeElapsed == other.sessionRateLimitExpirationTimeElapsed && this.sessionCountInRateLimitingWindow == other.sessionCountInRateLimitingWindow;
        }

        public int hashCode() {
            int result = (0 * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.expirationTimeElapsed);
            return (((((((((((((((((((((((((((result * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.allowedTimePerPeriodMs)) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.windowSizeMs)) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.jobCountLimit)) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.sessionCountLimit)) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.executionTimeInWindowMs)) * 31) + this.bgJobCountInWindow) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.executionTimeInMaxPeriodMs)) * 31) + this.bgJobCountInMaxPeriod) * 31) + this.sessionCountInWindow) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.inQuotaTimeElapsed)) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.jobRateLimitExpirationTimeElapsed)) * 31) + this.jobCountInRateLimitingWindow) * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.sessionRateLimitExpirationTimeElapsed)) * 31) + this.sessionCountInRateLimitingWindow;
        }
    }

    private class QcUidObserver extends android.app.UidObserver {
        private QcUidObserver() {
        }

        public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
            com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(3, uid, procState).sendToTarget();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public QuotaController(com.android.server.job.JobSchedulerService jobSchedulerService, com.android.server.job.controllers.BackgroundJobsController backgroundJobsController, com.android.server.job.controllers.ConnectivityController connectivityController) {
        super(jobSchedulerService);
        this.mTrackedJobs = new android.util.SparseArrayMap<>();
        this.mPkgTimers = new android.util.SparseArrayMap<>();
        this.mEJPkgTimers = new android.util.SparseArrayMap<>();
        this.mTimingEvents = new android.util.SparseArrayMap<>();
        this.mEJTimingSessions = new android.util.SparseArrayMap<>();
        this.mExecutionStatsCache = new android.util.SparseArrayMap<>();
        this.mEJStats = new android.util.SparseArrayMap<>();
        this.mTopAppTrackers = new android.util.SparseArrayMap<>();
        this.mForegroundUids = new android.util.SparseBooleanArray();
        this.mTopStartedJobs = new android.util.ArraySet<>();
        this.mTempAllowlistCache = new android.util.SparseBooleanArray();
        this.mTempAllowlistGraceCache = new android.util.SparseLongArray();
        this.mTopAppCache = new android.util.SparseBooleanArray();
        this.mTopAppGraceCache = new android.util.SparseLongArray();
        this.mAllowedTimePerPeriodMs = new long[]{600000, 600000, 600000, 600000, 0, 600000, 600000};
        this.mMaxExecutionTimeMs = 14400000L;
        this.mQuotaBufferMs = 30000L;
        this.mMaxExecutionTimeIntoQuotaMs = this.mMaxExecutionTimeMs - this.mQuotaBufferMs;
        this.mRateLimitingWindowMs = 60000L;
        this.mMaxJobCountPerRateLimitingWindow = 20;
        this.mMaxSessionCountPerRateLimitingWindow = 20;
        this.mNextCleanupTimeElapsed = 0L;
        this.mSessionCleanupAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.job.controllers.QuotaController.1
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(1).sendToTarget();
            }
        };
        this.mBucketPeriodsMs = new long[]{600000, com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT, 28800000, 86400000, 0, 86400000, 600000};
        this.mMaxBucketJobCounts = new int[]{75, 120, 200, 48, 0, 10, 75};
        this.mMaxBucketSessionCounts = new int[]{75, 10, 8, 3, 0, 1, 75};
        this.mTimingSessionCoalescingDurationMs = 5000L;
        this.mEJLimitsMs = new long[]{1800000, 1800000, 600000, 600000, 0, 300000, 3600000};
        this.mEjLimitAdditionInstallerMs = 1800000L;
        this.mEjLimitAdditionSpecialMs = 900000L;
        this.mEJLimitWindowSizeMs = 86400000L;
        this.mEJTopAppTimeChunkSizeMs = 30000L;
        this.mEJRewardTopAppMs = 10000L;
        this.mEJRewardInteractionMs = 15000L;
        this.mEJRewardNotificationSeenMs = 0L;
        this.mEJGracePeriodTempAllowlistMs = 180000L;
        this.mEJGracePeriodTopAppMs = 60000L;
        this.mQuotaBumpAdditionalDurationMs = 60000L;
        this.mQuotaBumpAdditionalJobCount = 2;
        this.mQuotaBumpAdditionalSessionCount = 1;
        this.mQuotaBumpWindowSizeMs = 28800000L;
        this.mQuotaBumpLimit = 8;
        this.mSystemInstallers = new android.util.SparseSetArray<>();
        java.lang.Object[] objArr = 0;
        java.lang.Object[] objArr2 = 0;
        this.mEarliestEndTimeFunctor = new com.android.server.job.controllers.QuotaController.EarliestEndTimeFunctor();
        this.mTimerChargingUpdateFunctor = new com.android.server.job.controllers.QuotaController.TimerChargingUpdateFunctor();
        this.mUpdateUidConstraints = new com.android.server.job.controllers.QuotaController.UidConstraintUpdater();
        this.mTimedEventTooOld = new com.android.server.job.controllers.QuotaController.TimedEventTooOldPredicate();
        this.mDeleteOldEventsFunctor = new java.util.function.Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$2((java.util.List) obj);
            }
        };
        this.mHandler = new com.android.server.job.controllers.QuotaController.QcHandler(com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        this.mQcConstants = new com.android.server.job.controllers.QuotaController.QcConstants();
        this.mBackgroundJobsController = backgroundJobsController;
        this.mConnectivityController = connectivityController;
        this.mInQuotaAlarmQueue = new com.android.server.job.controllers.QuotaController.InQuotaAlarmQueue(this.mContext, com.android.server.AppSchedulingModuleThread.get().getLooper());
        ((com.android.server.usage.AppStandbyInternal) com.android.server.LocalServices.getService(com.android.server.usage.AppStandbyInternal.class)).addListener(new com.android.server.job.controllers.QuotaController.StandbyTracker());
        ((android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class)).registerListener(new com.android.server.job.controllers.QuotaController.UsageEventTracker());
        ((com.android.server.PowerAllowlistInternal) com.android.server.LocalServices.getService(com.android.server.PowerAllowlistInternal.class)).registerTempAllowlistChangeListener(new com.android.server.job.controllers.QuotaController.TempAllowlistTracker());
        try {
            android.app.ActivityManager.getService().registerUidObserver(new com.android.server.job.controllers.QuotaController.QcUidObserver(), 1, 4, (java.lang.String) null);
            android.app.ActivityManager.getService().registerUidObserver(new com.android.server.job.controllers.QuotaController.QcUidObserver(), 1, 2, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onSystemServicesReady() {
        synchronized (this.mLock) {
            cacheInstallerPackagesLocked(0);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs;
        boolean outOfEJQuota;
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        int userId = jobStatus.getSourceUserId();
        java.lang.String pkgName = jobStatus.getSourcePackageName();
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs2 = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
        if (jobs2 != null) {
            jobs = jobs2;
        } else {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs3 = new android.util.ArraySet<>();
            this.mTrackedJobs.add(userId, pkgName, jobs3);
            jobs = jobs3;
        }
        jobs.add(jobStatus);
        jobStatus.setTrackingController(64);
        boolean isWithinQuota = isWithinQuotaLocked(jobStatus);
        boolean isWithinEJQuota = jobStatus.isRequestedExpeditedJob() && isWithinEJQuotaLocked(jobStatus);
        setConstraintSatisfied(jobStatus, nowElapsed, isWithinQuota, isWithinEJQuota);
        if (jobStatus.isRequestedExpeditedJob()) {
            setExpeditedQuotaApproved(jobStatus, nowElapsed, isWithinEJQuota);
            outOfEJQuota = isWithinEJQuota ? false : true;
        } else {
            outOfEJQuota = false;
        }
        if (!isWithinQuota || outOfEJQuota) {
            maybeScheduleStartAlarmLocked(userId, pkgName, jobStatus.getEffectiveStandbyBucket());
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Prepping for " + jobStatus.toShortString());
        }
        int uid = jobStatus.getSourceUid();
        if (this.mTopAppCache.get(uid)) {
            if (DEBUG) {
                android.util.Slog.d(TAG, jobStatus.toShortString() + " is top started job");
            }
            this.mTopStartedJobs.add(jobStatus);
        } else {
            if (jobStatus.shouldTreatAsUserInitiatedJob()) {
                return;
            }
            int userId = jobStatus.getSourceUserId();
            java.lang.String packageName = jobStatus.getSourcePackageName();
            android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.QuotaController.Timer> timerMap = jobStatus.shouldTreatAsExpeditedJob() ? this.mEJPkgTimers : this.mPkgTimers;
            com.android.server.job.controllers.QuotaController.Timer timer = (com.android.server.job.controllers.QuotaController.Timer) timerMap.get(userId, packageName);
            if (timer == null) {
                timer = new com.android.server.job.controllers.QuotaController.Timer(uid, userId, packageName, !jobStatus.shouldTreatAsExpeditedJob());
                timerMap.add(userId, packageName, timer);
            }
            timer.startTrackingJobLocked(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void unprepareFromExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        com.android.server.job.controllers.QuotaController.Timer timer;
        com.android.server.job.controllers.QuotaController.Timer timer2 = (com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        if (timer2 != null) {
            timer2.stopTrackingJob(jobStatus);
        }
        if (jobStatus.isRequestedExpeditedJob() && (timer = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName())) != null) {
            timer.stopTrackingJob(jobStatus);
        }
        this.mTopStartedJobs.remove(jobStatus);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if (jobStatus.clearTrackingController(64)) {
            unprepareFromExecutionLocked(jobStatus);
            int userId = jobStatus.getSourceUserId();
            java.lang.String pkgName = jobStatus.getSourcePackageName();
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
            if (jobs != null && jobs.remove(jobStatus) && jobs.size() == 0) {
                this.mInQuotaAlarmQueue.removeAlarmForKey(android.content.pm.UserPackage.of(userId, pkgName));
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(java.lang.String packageName, int uid) {
        if (packageName == null) {
            android.util.Slog.wtf(TAG, "Told app removed but given null package name.");
            return;
        }
        clearAppStatsLocked(android.os.UserHandle.getUserId(uid), packageName);
        if (this.mService.getPackagesForUidLocked(uid) == null) {
            this.mForegroundUids.delete(uid);
            this.mTempAllowlistCache.delete(uid);
            this.mTempAllowlistGraceCache.delete(uid);
            this.mTopAppCache.delete(uid);
            this.mTopAppGraceCache.delete(uid);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserAddedLocked(int userId) {
        cacheInstallerPackagesLocked(userId);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int userId) {
        this.mTrackedJobs.delete(userId);
        this.mPkgTimers.delete(userId);
        this.mEJPkgTimers.delete(userId);
        this.mTimingEvents.delete(userId);
        this.mEJTimingSessions.delete(userId);
        this.mInQuotaAlarmQueue.removeAlarmsForUserId(userId);
        this.mExecutionStatsCache.delete(userId);
        this.mEJStats.delete(userId);
        this.mSystemInstallers.remove(userId);
        this.mTopAppTrackers.delete(userId);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onBatteryStateChangedLocked() {
        handleNewChargingStateLocked();
    }

    public void clearAppStatsLocked(int userId, java.lang.String packageName) {
        this.mTrackedJobs.delete(userId, packageName);
        com.android.server.job.controllers.QuotaController.Timer timer = (com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.delete(userId, packageName);
        if (timer != null && timer.isActive()) {
            android.util.Slog.e(TAG, "clearAppStats called before Timer turned off.");
            timer.dropEverythingLocked();
        }
        com.android.server.job.controllers.QuotaController.Timer timer2 = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.delete(userId, packageName);
        if (timer2 != null && timer2.isActive()) {
            android.util.Slog.e(TAG, "clearAppStats called before EJ Timer turned off.");
            timer2.dropEverythingLocked();
        }
        this.mTimingEvents.delete(userId, packageName);
        this.mEJTimingSessions.delete(userId, packageName);
        this.mInQuotaAlarmQueue.removeAlarmForKey(android.content.pm.UserPackage.of(userId, packageName));
        this.mExecutionStatsCache.delete(userId, packageName);
        this.mEJStats.delete(userId, packageName);
        this.mTopAppTrackers.delete(userId, packageName);
    }

    private void cacheInstallerPackagesLocked(int userId) {
        java.util.List<android.content.pm.PackageInfo> packages = this.mContext.getPackageManager().getInstalledPackagesAsUser(SYSTEM_APP_CHECK_FLAGS, userId);
        for (int i = packages.size() - 1; i >= 0; i--) {
            android.content.pm.PackageInfo pi = packages.get(i);
            android.content.pm.ApplicationInfo ai = pi.applicationInfo;
            int idx = com.android.internal.util.ArrayUtils.indexOf(pi.requestedPermissions, "android.permission.INSTALL_PACKAGES");
            if (idx >= 0 && ai != null && this.mContext.checkPermission("android.permission.INSTALL_PACKAGES", -1, ai.uid) == 0) {
                this.mSystemInstallers.add(android.os.UserHandle.getUserId(ai.uid), pi.packageName);
            }
        }
    }

    private boolean isUidInForeground(int uid) {
        boolean z;
        if (android.os.UserHandle.isCore(uid)) {
            return true;
        }
        synchronized (this.mLock) {
            z = this.mForegroundUids.get(uid);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTopStartedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        return this.mTopStartedJobs.contains(jobStatus);
    }

    public long getMaxJobExecutionTimeMsLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (!jobStatus.shouldTreatAsExpeditedJob()) {
            if (this.mService.isBatteryCharging()) {
                return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            }
            boolean isInPrivilegedState = this.mTopAppCache.get(jobStatus.getSourceUid()) || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid());
            boolean isJobImportant = jobStatus.getEffectivePriority() >= 400 || (jobStatus.getFlags() & 2) != 0;
            if (isInPrivilegedState && isJobImportant) {
                return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            }
            return getTimeUntilQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        }
        if (this.mService.isBatteryCharging()) {
            return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
        }
        if (jobStatus.getEffectiveStandbyBucket() == 6) {
            return java.lang.Math.max(this.mEJLimitsMs[6] / 2, getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
        }
        if (this.mTopAppCache.get(jobStatus.getSourceUid()) || isTopStartedJobLocked(jobStatus)) {
            return java.lang.Math.max(this.mEJLimitsMs[0] / 2, getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
        }
        if (isUidInForeground(jobStatus.getSourceUid())) {
            return java.lang.Math.max(this.mEJLimitsMs[1] / 2, getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName()));
        }
        return getTimeUntilEJQuotaConsumedLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasTempAllowlistExemptionLocked(int sourceUid, int standbyBucket, long nowElapsed) {
        if (standbyBucket == 5 || standbyBucket == 4) {
            return false;
        }
        long tempAllowlistGracePeriodEndElapsed = this.mTempAllowlistGraceCache.get(sourceUid);
        return this.mTempAllowlistCache.get(sourceUid) || nowElapsed < tempAllowlistGracePeriodEndElapsed;
    }

    public boolean isWithinEJQuotaLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (isQuotaFreeLocked(jobStatus.getEffectiveStandbyBucket()) || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid())) {
            return true;
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (hasTempAllowlistExemptionLocked(jobStatus.getSourceUid(), jobStatus.getEffectiveStandbyBucket(), nowElapsed)) {
            return true;
        }
        long topAppGracePeriodEndElapsed = this.mTopAppGraceCache.get(jobStatus.getSourceUid());
        boolean hasTopAppExemption = this.mTopAppCache.get(jobStatus.getSourceUid()) || nowElapsed < topAppGracePeriodEndElapsed;
        return hasTopAppExemption || 0 < getRemainingEJExecutionTimeLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
    }

    com.android.server.job.controllers.QuotaController.ShrinkableDebits getEJDebitsLocked(int userId, java.lang.String packageName) {
        com.android.server.job.controllers.QuotaController.ShrinkableDebits debits = (com.android.server.job.controllers.QuotaController.ShrinkableDebits) this.mEJStats.get(userId, packageName);
        if (debits == null) {
            com.android.server.job.controllers.QuotaController.ShrinkableDebits debits2 = new com.android.server.job.controllers.QuotaController.ShrinkableDebits(com.android.server.job.JobSchedulerService.standbyBucketForPackage(packageName, userId, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis()));
            this.mEJStats.add(userId, packageName, debits2);
            return debits2;
        }
        return debits;
    }

    boolean isWithinQuotaLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        int standbyBucket = jobStatus.getEffectiveStandbyBucket();
        if (!com.android.server.job.Flags.countQuotaFix()) {
            return jobStatus.shouldTreatAsUserInitiatedJob() || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid()) || isWithinQuotaLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), standbyBucket);
        }
        if (jobStatus.shouldTreatAsUserInitiatedJob() || isTopStartedJobLocked(jobStatus) || isUidInForeground(jobStatus.getSourceUid())) {
            return true;
        }
        if (standbyBucket == 4) {
            return false;
        }
        if (isQuotaFreeLocked(standbyBucket)) {
            return true;
        }
        com.android.server.job.controllers.QuotaController.ExecutionStats stats = getExecutionStatsLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), standbyBucket);
        if (getRemainingExecutionTimeLocked(stats) <= 0) {
            return false;
        }
        if (standbyBucket == 5 || !this.mService.isCurrentlyRunningLocked(jobStatus)) {
            return isUnderJobCountQuotaLocked(stats) && isUnderSessionCountQuotaLocked(stats);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isQuotaFreeLocked(int standbyBucket) {
        return this.mService.isBatteryCharging() && standbyBucket != 5;
    }

    boolean isWithinQuotaLocked(int userId, java.lang.String packageName, int standbyBucket) {
        if (standbyBucket == 4) {
            return false;
        }
        if (isQuotaFreeLocked(standbyBucket)) {
            return true;
        }
        com.android.server.job.controllers.QuotaController.ExecutionStats stats = getExecutionStatsLocked(userId, packageName, standbyBucket);
        return getRemainingExecutionTimeLocked(stats) > 0 && isUnderJobCountQuotaLocked(stats) && isUnderSessionCountQuotaLocked(stats);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUnderJobCountQuotaLocked(com.android.server.job.controllers.QuotaController.ExecutionStats stats) {
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        boolean isUnderAllowedTimeQuota = stats.jobRateLimitExpirationTimeElapsed <= now || stats.jobCountInRateLimitingWindow < this.mMaxJobCountPerRateLimitingWindow;
        return isUnderAllowedTimeQuota && stats.bgJobCountInWindow < stats.jobCountLimit;
    }

    private boolean isUnderSessionCountQuotaLocked(com.android.server.job.controllers.QuotaController.ExecutionStats stats) {
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        boolean isUnderAllowedTimeQuota = stats.sessionRateLimitExpirationTimeElapsed <= now || stats.sessionCountInRateLimitingWindow < this.mMaxSessionCountPerRateLimitingWindow;
        return isUnderAllowedTimeQuota && stats.sessionCountInWindow < stats.sessionCountLimit;
    }

    long getRemainingExecutionTimeLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        return getRemainingExecutionTimeLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), jobStatus.getEffectiveStandbyBucket());
    }

    long getRemainingExecutionTimeLocked(int userId, java.lang.String packageName) {
        int standbyBucket = com.android.server.job.JobSchedulerService.standbyBucketForPackage(packageName, userId, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
        return getRemainingExecutionTimeLocked(userId, packageName, standbyBucket);
    }

    private long getRemainingExecutionTimeLocked(int userId, java.lang.String packageName, int standbyBucket) {
        if (standbyBucket == 4) {
            return 0L;
        }
        return getRemainingExecutionTimeLocked(getExecutionStatsLocked(userId, packageName, standbyBucket));
    }

    private long getRemainingExecutionTimeLocked(com.android.server.job.controllers.QuotaController.ExecutionStats stats) {
        return java.lang.Math.min(stats.allowedTimePerPeriodMs - stats.executionTimeInWindowMs, this.mMaxExecutionTimeMs - stats.executionTimeInMaxPeriodMs);
    }

    long getRemainingEJExecutionTimeLocked(int userId, java.lang.String packageName) {
        long nowElapsed;
        com.android.server.job.controllers.QuotaController.ShrinkableDebits quota = getEJDebitsLocked(userId, packageName);
        if (quota.getStandbyBucketLocked() != 4) {
            long limitMs = getEJLimitMsLocked(userId, packageName, quota.getStandbyBucketLocked());
            long remainingMs = limitMs - quota.getTallyLocked();
            java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> timingSessions = (java.util.List) this.mEJTimingSessions.get(userId, packageName);
            long nowElapsed2 = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            long windowStartTimeElapsed = nowElapsed2 - this.mEJLimitWindowSizeMs;
            if (timingSessions != null) {
                while (true) {
                    if (timingSessions.size() <= 0) {
                        nowElapsed = nowElapsed2;
                        break;
                    }
                    com.android.server.job.controllers.QuotaController.TimingSession ts = (com.android.server.job.controllers.QuotaController.TimingSession) timingSessions.get(0);
                    long limitMs2 = limitMs;
                    if (ts.endTimeElapsed < windowStartTimeElapsed) {
                        long duration = ts.endTimeElapsed - ts.startTimeElapsed;
                        remainingMs += duration;
                        quota.transactLocked(-duration);
                        timingSessions.remove(0);
                        limitMs = limitMs2;
                        nowElapsed2 = nowElapsed2;
                    } else {
                        nowElapsed = nowElapsed2;
                        if (ts.startTimeElapsed < windowStartTimeElapsed) {
                            remainingMs += windowStartTimeElapsed - ts.startTimeElapsed;
                        }
                    }
                }
            } else {
                nowElapsed = nowElapsed2;
            }
            com.android.server.job.controllers.QuotaController.TopAppTimer topAppTimer = (com.android.server.job.controllers.QuotaController.TopAppTimer) this.mTopAppTrackers.get(userId, packageName);
            if (topAppTimer != null && topAppTimer.isActive()) {
                remainingMs += topAppTimer.getPendingReward(nowElapsed);
            }
            com.android.server.job.controllers.QuotaController.Timer timer = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.get(userId, packageName);
            if (timer == null) {
                return remainingMs;
            }
            return remainingMs - timer.getCurrentDuration(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
        }
        return 0L;
    }

    private long getEJLimitMsLocked(int userId, java.lang.String packageName, int standbyBucket) {
        long baseLimitMs = this.mEJLimitsMs[standbyBucket];
        if (this.mSystemInstallers.contains(userId, packageName)) {
            return this.mEjLimitAdditionInstallerMs + baseLimitMs;
        }
        return baseLimitMs;
    }

    long getTimeUntilQuotaConsumedLocked(int userId, java.lang.String packageName) {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        int standbyBucket = com.android.server.job.JobSchedulerService.standbyBucketForPackage(packageName, userId, nowElapsed);
        if (standbyBucket == 4) {
            return 0L;
        }
        java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events = (java.util.List) this.mTimingEvents.get(userId, packageName);
        com.android.server.job.controllers.QuotaController.ExecutionStats stats = getExecutionStatsLocked(userId, packageName, standbyBucket);
        if (events == null || events.size() == 0) {
            if (stats.windowSizeMs == this.mAllowedTimePerPeriodMs[standbyBucket]) {
                return this.mMaxExecutionTimeMs;
            }
            return this.mAllowedTimePerPeriodMs[standbyBucket];
        }
        long startWindowElapsed = nowElapsed - stats.windowSizeMs;
        long startMaxElapsed = nowElapsed - 86400000;
        long allowedTimePerPeriodMs = this.mAllowedTimePerPeriodMs[standbyBucket];
        long allowedTimeRemainingMs = allowedTimePerPeriodMs - stats.executionTimeInWindowMs;
        long maxExecutionTimeRemainingMs = this.mMaxExecutionTimeMs - stats.executionTimeInMaxPeriodMs;
        if (stats.windowSizeMs == this.mAllowedTimePerPeriodMs[standbyBucket]) {
            return calculateTimeUntilQuotaConsumedLocked(events, startMaxElapsed, maxExecutionTimeRemainingMs, false);
        }
        return java.lang.Math.min(calculateTimeUntilQuotaConsumedLocked(events, startMaxElapsed, maxExecutionTimeRemainingMs, false), calculateTimeUntilQuotaConsumedLocked(events, startWindowElapsed, allowedTimeRemainingMs, true));
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private long calculateTimeUntilQuotaConsumedLocked(java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> r20, long r21, long r23, boolean r25) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            r2 = 0
            r4 = r21
            r6 = 0
            java.time.Clock r7 = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock
            long r7 = r7.millis()
            long r9 = r0.mQuotaBumpWindowSizeMs
            long r7 = r7 - r9
            int r9 = r20.size()
            if (r25 == 0) goto L49
            int r10 = r9 + (-1)
            r12 = r10
            r10 = r23
        L1d:
            if (r12 < 0) goto L47
            java.lang.Object r13 = r1.get(r12)
            com.android.server.job.controllers.QuotaController$TimedEvent r13 = (com.android.server.job.controllers.QuotaController.TimedEvent) r13
            boolean r14 = r13 instanceof com.android.server.job.controllers.QuotaController.QuotaBump
            if (r14 == 0) goto L42
            long r14 = r13.getEndTimeElapsed()
            int r14 = (r14 > r7 ? 1 : (r14 == r7 ? 0 : -1))
            if (r14 < 0) goto L40
            int r14 = r6 + 1
            int r15 = r0.mQuotaBumpLimit
            if (r6 >= r15) goto L3d
            r15 = r2
            long r2 = r0.mQuotaBumpAdditionalDurationMs
            long r10 = r10 + r2
            r6 = r14
            goto L43
        L3d:
            r15 = r2
            r6 = r14
            goto L4c
        L40:
            r15 = r2
            goto L4c
        L42:
            r15 = r2
        L43:
            int r12 = r12 + (-1)
            r2 = r15
            goto L1d
        L47:
            r15 = r2
            goto L4c
        L49:
            r15 = r2
            r10 = r23
        L4c:
            r2 = 0
        L4d:
            if (r2 >= r9) goto L99
            java.lang.Object r3 = r1.get(r2)
            com.android.server.job.controllers.QuotaController$TimedEvent r3 = (com.android.server.job.controllers.QuotaController.TimedEvent) r3
            boolean r12 = r3 instanceof com.android.server.job.controllers.QuotaController.QuotaBump
            if (r12 == 0) goto L5a
            goto L64
        L5a:
            r12 = r3
            com.android.server.job.controllers.QuotaController$TimingSession r12 = (com.android.server.job.controllers.QuotaController.TimingSession) r12
            long r13 = r12.endTimeElapsed
            int r13 = (r13 > r21 ? 1 : (r13 == r21 ? 0 : -1))
            if (r13 >= 0) goto L67
        L64:
            r24 = r6
            goto L94
        L67:
            long r13 = r12.startTimeElapsed
            int r13 = (r13 > r21 ? 1 : (r13 == r21 ? 0 : -1))
            if (r13 > 0) goto L77
            long r13 = r12.endTimeElapsed
            long r13 = r13 - r21
            long r15 = r15 + r13
            long r4 = r12.endTimeElapsed
            r24 = r6
            goto L94
        L77:
            long r13 = r12.startTimeElapsed
            long r13 = r13 - r4
            int r17 = (r13 > r10 ? 1 : (r13 == r10 ? 0 : -1))
            if (r17 <= 0) goto L83
            r17 = r4
            r24 = r6
            goto L9d
        L83:
            r23 = r3
            r17 = r4
            long r3 = r12.endTimeElapsed
            r24 = r6
            long r5 = r12.startTimeElapsed
            long r3 = r3 - r5
            long r3 = r3 + r13
            long r15 = r15 + r3
            long r10 = r10 - r13
            long r3 = r12.endTimeElapsed
            r4 = r3
        L94:
            int r2 = r2 + 1
            r6 = r24
            goto L4d
        L99:
            r17 = r4
            r24 = r6
        L9d:
            long r2 = r15 + r10
            long r4 = r0.mMaxExecutionTimeMs
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 <= 0) goto Lbd
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Calculated quota consumed time too high: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = "JobScheduler.Quota"
            android.util.Slog.wtf(r5, r4)
        Lbd:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.QuotaController.calculateTimeUntilQuotaConsumedLocked(java.util.List, long, long, boolean):long");
    }

    long getTimeUntilEJQuotaConsumedLocked(int userId, java.lang.String packageName) {
        long nowElapsed;
        long startWindowElapsed;
        long remainingExecutionTimeMs = getRemainingEJExecutionTimeLocked(userId, packageName);
        java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> sessions = (java.util.List) this.mEJTimingSessions.get(userId, packageName);
        if (sessions == null || sessions.size() == 0) {
            return remainingExecutionTimeMs;
        }
        long nowElapsed2 = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        com.android.server.job.controllers.QuotaController.ShrinkableDebits quota = getEJDebitsLocked(userId, packageName);
        long limitMs = getEJLimitMsLocked(userId, packageName, quota.getStandbyBucketLocked());
        long startWindowElapsed2 = java.lang.Math.max(0L, nowElapsed2 - this.mEJLimitWindowSizeMs);
        long deadSpaceMs = 0;
        long phasedOutSessionTimeMs = 0;
        long remainingDeadSpaceMs = remainingExecutionTimeMs;
        int i = 0;
        while (i < sessions.size()) {
            com.android.server.job.controllers.QuotaController.TimingSession session = (com.android.server.job.controllers.QuotaController.TimingSession) sessions.get(i);
            if (session.endTimeElapsed < startWindowElapsed2) {
                nowElapsed = nowElapsed2;
                remainingDeadSpaceMs += session.endTimeElapsed - session.startTimeElapsed;
                sessions.remove(i);
                i--;
                startWindowElapsed = startWindowElapsed2;
            } else {
                nowElapsed = nowElapsed2;
                if (session.startTimeElapsed < startWindowElapsed2) {
                    phasedOutSessionTimeMs = session.endTimeElapsed - startWindowElapsed2;
                    startWindowElapsed = startWindowElapsed2;
                } else {
                    long phasedOutSessionTimeMs2 = session.startTimeElapsed;
                    long timeBetweenSessions = phasedOutSessionTimeMs2 - (i == 0 ? startWindowElapsed2 : sessions.get(i - 1).getEndTimeElapsed());
                    long usedDeadSpaceMs = java.lang.Math.min(remainingDeadSpaceMs, timeBetweenSessions);
                    deadSpaceMs += usedDeadSpaceMs;
                    if (usedDeadSpaceMs == timeBetweenSessions) {
                        long timeBetweenSessions2 = session.endTimeElapsed;
                        startWindowElapsed = startWindowElapsed2;
                        long startWindowElapsed3 = session.startTimeElapsed;
                        phasedOutSessionTimeMs += timeBetweenSessions2 - startWindowElapsed3;
                    } else {
                        startWindowElapsed = startWindowElapsed2;
                    }
                    remainingDeadSpaceMs -= usedDeadSpaceMs;
                    if (remainingDeadSpaceMs <= 0) {
                        break;
                    }
                }
            }
            i++;
            nowElapsed2 = nowElapsed;
            startWindowElapsed2 = startWindowElapsed;
        }
        return java.lang.Math.min(limitMs, deadSpaceMs + phasedOutSessionTimeMs + remainingDeadSpaceMs);
    }

    com.android.server.job.controllers.QuotaController.ExecutionStats getExecutionStatsLocked(int userId, java.lang.String packageName, int standbyBucket) {
        return getExecutionStatsLocked(userId, packageName, standbyBucket, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.job.controllers.QuotaController.ExecutionStats getExecutionStatsLocked(int userId, java.lang.String packageName, int standbyBucket, boolean refreshStatsIfOld) {
        if (standbyBucket == 4) {
            android.util.Slog.wtf(TAG, "getExecutionStatsLocked called for a NEVER app.");
            return new com.android.server.job.controllers.QuotaController.ExecutionStats();
        }
        com.android.server.job.controllers.QuotaController.ExecutionStats[] appStats = (com.android.server.job.controllers.QuotaController.ExecutionStats[]) this.mExecutionStatsCache.get(userId, packageName);
        if (appStats == null) {
            appStats = new com.android.server.job.controllers.QuotaController.ExecutionStats[this.mBucketPeriodsMs.length];
            this.mExecutionStatsCache.add(userId, packageName, appStats);
        }
        com.android.server.job.controllers.QuotaController.ExecutionStats stats = appStats[standbyBucket];
        if (stats == null) {
            stats = new com.android.server.job.controllers.QuotaController.ExecutionStats();
            appStats[standbyBucket] = stats;
        }
        if (refreshStatsIfOld) {
            long bucketAllowedTimeMs = this.mAllowedTimePerPeriodMs[standbyBucket];
            long bucketWindowSizeMs = this.mBucketPeriodsMs[standbyBucket];
            int jobCountLimit = this.mMaxBucketJobCounts[standbyBucket];
            int sessionCountLimit = this.mMaxBucketSessionCounts[standbyBucket];
            com.android.server.job.controllers.QuotaController.Timer timer = (com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.get(userId, packageName);
            if ((timer != null && timer.isActive()) || stats.expirationTimeElapsed <= com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() || stats.allowedTimePerPeriodMs != bucketAllowedTimeMs || stats.windowSizeMs != bucketWindowSizeMs || stats.jobCountLimit != jobCountLimit || stats.sessionCountLimit != sessionCountLimit) {
                stats.allowedTimePerPeriodMs = bucketAllowedTimeMs;
                stats.windowSizeMs = bucketWindowSizeMs;
                stats.jobCountLimit = jobCountLimit;
                stats.sessionCountLimit = sessionCountLimit;
                updateExecutionStatsLocked(userId, packageName, stats);
            }
        }
        return stats;
    }

    void updateExecutionStatsLocked(int userId, java.lang.String packageName, com.android.server.job.controllers.QuotaController.ExecutionStats stats) {
        long allowedTimeIntoQuotaMs;
        int sessionCountInWindow;
        long startMaxElapsed;
        int i;
        java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events;
        long quotaBumpWindowStartElapsed;
        long emptyTimeMs;
        long start;
        long emptyTimeMs2;
        long allowedTimeIntoQuotaMs2;
        stats.executionTimeInWindowMs = 0L;
        stats.bgJobCountInWindow = 0;
        stats.executionTimeInMaxPeriodMs = 0L;
        stats.bgJobCountInMaxPeriod = 0;
        stats.sessionCountInWindow = 0;
        if (stats.jobCountLimit == 0 || stats.sessionCountLimit == 0) {
            stats.inQuotaTimeElapsed = Long.MAX_VALUE;
        } else {
            stats.inQuotaTimeElapsed = 0L;
        }
        long allowedTimeIntoQuotaMs3 = stats.allowedTimePerPeriodMs - this.mQuotaBufferMs;
        com.android.server.job.controllers.QuotaController.Timer timer = (com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.get(userId, packageName);
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        stats.expirationTimeElapsed = nowElapsed + 86400000;
        if (timer != null && timer.isActive()) {
            long currentDuration = timer.getCurrentDuration(nowElapsed);
            stats.executionTimeInMaxPeriodMs = currentDuration;
            stats.executionTimeInWindowMs = currentDuration;
            int bgJobCount = timer.getBgJobCount();
            stats.bgJobCountInMaxPeriod = bgJobCount;
            stats.bgJobCountInWindow = bgJobCount;
            stats.expirationTimeElapsed = nowElapsed;
            if (stats.executionTimeInWindowMs >= allowedTimeIntoQuotaMs3) {
                stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, (nowElapsed - allowedTimeIntoQuotaMs3) + stats.windowSizeMs);
            }
            if (stats.executionTimeInMaxPeriodMs >= this.mMaxExecutionTimeIntoQuotaMs) {
                long inQuotaTime = (nowElapsed - this.mMaxExecutionTimeIntoQuotaMs) + 86400000;
                stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, inQuotaTime);
            }
            if (stats.bgJobCountInWindow >= stats.jobCountLimit) {
                long inQuotaTime2 = stats.windowSizeMs + nowElapsed;
                stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, inQuotaTime2);
            }
        }
        java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events2 = (java.util.List) this.mTimingEvents.get(userId, packageName);
        if (events2 != null && events2.size() != 0) {
            long startWindowElapsed = nowElapsed - stats.windowSizeMs;
            long startMaxElapsed2 = nowElapsed - 86400000;
            long quotaBumpWindowStartElapsed2 = nowElapsed - this.mQuotaBumpWindowSizeMs;
            int loopStart = events2.size() - 1;
            int numQuotaBumps = 0;
            int i2 = loopStart;
            int sessionCountInWindow2 = 0;
            long emptyTimeMs3 = Long.MAX_VALUE;
            while (true) {
                if (i2 < 0) {
                    allowedTimeIntoQuotaMs = allowedTimeIntoQuotaMs3;
                    sessionCountInWindow = sessionCountInWindow2;
                    startMaxElapsed = startMaxElapsed2;
                    break;
                }
                sessionCountInWindow = sessionCountInWindow2;
                com.android.server.job.controllers.QuotaController.TimedEvent event = events2.get(i2);
                if (event.getEndTimeElapsed() >= quotaBumpWindowStartElapsed2) {
                    startMaxElapsed = startMaxElapsed2;
                    if (numQuotaBumps >= this.mQuotaBumpLimit) {
                        allowedTimeIntoQuotaMs = allowedTimeIntoQuotaMs3;
                        break;
                    }
                    if (event instanceof com.android.server.job.controllers.QuotaController.QuotaBump) {
                        allowedTimeIntoQuotaMs2 = allowedTimeIntoQuotaMs3;
                        stats.allowedTimePerPeriodMs += this.mQuotaBumpAdditionalDurationMs;
                        stats.jobCountLimit += this.mQuotaBumpAdditionalJobCount;
                        stats.sessionCountLimit += this.mQuotaBumpAdditionalSessionCount;
                        numQuotaBumps++;
                        emptyTimeMs3 = java.lang.Math.min(emptyTimeMs3, event.getEndTimeElapsed() - quotaBumpWindowStartElapsed2);
                    } else {
                        allowedTimeIntoQuotaMs2 = allowedTimeIntoQuotaMs3;
                    }
                    i2--;
                    sessionCountInWindow2 = sessionCountInWindow;
                    startMaxElapsed2 = startMaxElapsed;
                    allowedTimeIntoQuotaMs3 = allowedTimeIntoQuotaMs2;
                } else {
                    allowedTimeIntoQuotaMs = allowedTimeIntoQuotaMs3;
                    startMaxElapsed = startMaxElapsed2;
                    break;
                }
            }
            com.android.server.job.controllers.QuotaController.TimingSession lastSeenTimingSession = null;
            int i3 = loopStart;
            while (true) {
                if (i3 < 0) {
                    i = sessionCountInWindow;
                    break;
                }
                com.android.server.job.controllers.QuotaController.TimedEvent event2 = events2.get(i3);
                if (event2 instanceof com.android.server.job.controllers.QuotaController.QuotaBump) {
                    events = events2;
                    quotaBumpWindowStartElapsed = quotaBumpWindowStartElapsed2;
                } else {
                    com.android.server.job.controllers.QuotaController.TimingSession session = (com.android.server.job.controllers.QuotaController.TimingSession) event2;
                    if (startWindowElapsed < session.endTimeElapsed) {
                        if (startWindowElapsed < session.startTimeElapsed) {
                            start = session.startTimeElapsed;
                            events = events2;
                            emptyTimeMs2 = java.lang.Math.min(emptyTimeMs3, session.startTimeElapsed - startWindowElapsed);
                        } else {
                            events = events2;
                            start = startWindowElapsed;
                            emptyTimeMs2 = 0;
                        }
                        long emptyTimeMs4 = stats.executionTimeInWindowMs;
                        long emptyTimeMs5 = emptyTimeMs2;
                        long emptyTimeMs6 = session.endTimeElapsed;
                        stats.executionTimeInWindowMs = emptyTimeMs4 + (emptyTimeMs6 - start);
                        stats.bgJobCountInWindow += session.bgJobCount;
                        if (stats.executionTimeInWindowMs < allowedTimeIntoQuotaMs) {
                            quotaBumpWindowStartElapsed = quotaBumpWindowStartElapsed2;
                        } else {
                            quotaBumpWindowStartElapsed = quotaBumpWindowStartElapsed2;
                            stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, ((stats.executionTimeInWindowMs + start) - allowedTimeIntoQuotaMs) + stats.windowSizeMs);
                        }
                        if (stats.bgJobCountInWindow >= stats.jobCountLimit) {
                            long inQuotaTime3 = session.endTimeElapsed + stats.windowSizeMs;
                            stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, inQuotaTime3);
                        }
                        boolean shouldCoalesce = lastSeenTimingSession != null && lastSeenTimingSession.startTimeElapsed - session.endTimeElapsed <= this.mTimingSessionCoalescingDurationMs;
                        if (shouldCoalesce) {
                            emptyTimeMs3 = emptyTimeMs5;
                        } else {
                            int sessionCountInWindow3 = sessionCountInWindow + 1;
                            if (sessionCountInWindow3 >= stats.sessionCountLimit) {
                                long inQuotaTime4 = session.endTimeElapsed + stats.windowSizeMs;
                                stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, inQuotaTime4);
                            }
                            sessionCountInWindow = sessionCountInWindow3;
                            emptyTimeMs3 = emptyTimeMs5;
                        }
                    } else {
                        events = events2;
                        quotaBumpWindowStartElapsed = quotaBumpWindowStartElapsed2;
                    }
                    if (startMaxElapsed < session.startTimeElapsed) {
                        stats.executionTimeInMaxPeriodMs += session.endTimeElapsed - session.startTimeElapsed;
                        stats.bgJobCountInMaxPeriod += session.bgJobCount;
                        emptyTimeMs = java.lang.Math.min(emptyTimeMs3, session.startTimeElapsed - startMaxElapsed);
                        long emptyTimeMs7 = stats.executionTimeInMaxPeriodMs;
                        if (emptyTimeMs7 >= this.mMaxExecutionTimeIntoQuotaMs) {
                            stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, ((session.startTimeElapsed + stats.executionTimeInMaxPeriodMs) - this.mMaxExecutionTimeIntoQuotaMs) + 86400000);
                        }
                    } else {
                        long emptyTimeMs8 = session.endTimeElapsed;
                        if (startMaxElapsed >= emptyTimeMs8) {
                            i = sessionCountInWindow;
                            break;
                        }
                        stats.executionTimeInMaxPeriodMs += session.endTimeElapsed - startMaxElapsed;
                        stats.bgJobCountInMaxPeriod += session.bgJobCount;
                        emptyTimeMs = 0;
                        long emptyTimeMs9 = stats.executionTimeInMaxPeriodMs;
                        if (emptyTimeMs9 >= this.mMaxExecutionTimeIntoQuotaMs) {
                            stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, ((startMaxElapsed + stats.executionTimeInMaxPeriodMs) - this.mMaxExecutionTimeIntoQuotaMs) + 86400000);
                        }
                    }
                    lastSeenTimingSession = session;
                    emptyTimeMs3 = emptyTimeMs;
                }
                i3--;
                events2 = events;
                quotaBumpWindowStartElapsed2 = quotaBumpWindowStartElapsed;
            }
            stats.expirationTimeElapsed = nowElapsed + emptyTimeMs3;
            stats.sessionCountInWindow = i;
        }
    }

    void invalidateAllExecutionStatsLocked() {
        final long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        this.mExecutionStatsCache.forEach(new java.util.function.Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.job.controllers.QuotaController.lambda$invalidateAllExecutionStatsLocked$0(nowElapsed, (com.android.server.job.controllers.QuotaController.ExecutionStats[]) obj);
            }
        });
    }

    static /* synthetic */ void lambda$invalidateAllExecutionStatsLocked$0(long nowElapsed, com.android.server.job.controllers.QuotaController.ExecutionStats[] appStats) {
        if (appStats != null) {
            for (com.android.server.job.controllers.QuotaController.ExecutionStats stats : appStats) {
                if (stats != null) {
                    stats.expirationTimeElapsed = nowElapsed;
                }
            }
        }
    }

    void invalidateAllExecutionStatsLocked(int userId, java.lang.String packageName) {
        com.android.server.job.controllers.QuotaController.ExecutionStats[] appStats = (com.android.server.job.controllers.QuotaController.ExecutionStats[]) this.mExecutionStatsCache.get(userId, packageName);
        if (appStats != null) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            for (com.android.server.job.controllers.QuotaController.ExecutionStats stats : appStats) {
                if (stats != null) {
                    stats.expirationTimeElapsed = nowElapsed;
                }
            }
        }
    }

    void incrementJobCountLocked(int userId, java.lang.String packageName, int count) {
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        com.android.server.job.controllers.QuotaController.ExecutionStats[] appStats = (com.android.server.job.controllers.QuotaController.ExecutionStats[]) this.mExecutionStatsCache.get(userId, packageName);
        if (appStats == null) {
            appStats = new com.android.server.job.controllers.QuotaController.ExecutionStats[this.mBucketPeriodsMs.length];
            this.mExecutionStatsCache.add(userId, packageName, appStats);
        }
        for (int i = 0; i < appStats.length; i++) {
            com.android.server.job.controllers.QuotaController.ExecutionStats stats = appStats[i];
            if (stats == null) {
                stats = new com.android.server.job.controllers.QuotaController.ExecutionStats();
                appStats[i] = stats;
            }
            if (stats.jobRateLimitExpirationTimeElapsed <= now) {
                stats.jobRateLimitExpirationTimeElapsed = this.mRateLimitingWindowMs + now;
                stats.jobCountInRateLimitingWindow = 0;
            }
            stats.jobCountInRateLimitingWindow += count;
            if (com.android.server.job.Flags.countQuotaFix()) {
                stats.bgJobCountInWindow += count;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void incrementTimingSessionCountLocked(int userId, java.lang.String packageName) {
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        com.android.server.job.controllers.QuotaController.ExecutionStats[] appStats = (com.android.server.job.controllers.QuotaController.ExecutionStats[]) this.mExecutionStatsCache.get(userId, packageName);
        if (appStats == null) {
            appStats = new com.android.server.job.controllers.QuotaController.ExecutionStats[this.mBucketPeriodsMs.length];
            this.mExecutionStatsCache.add(userId, packageName, appStats);
        }
        for (int i = 0; i < appStats.length; i++) {
            com.android.server.job.controllers.QuotaController.ExecutionStats stats = appStats[i];
            if (stats == null) {
                stats = new com.android.server.job.controllers.QuotaController.ExecutionStats();
                appStats[i] = stats;
            }
            if (stats.sessionRateLimitExpirationTimeElapsed <= now) {
                stats.sessionRateLimitExpirationTimeElapsed = this.mRateLimitingWindowMs + now;
                stats.sessionCountInRateLimitingWindow = 0;
            }
            stats.sessionCountInRateLimitingWindow++;
        }
    }

    void saveTimingSession(int userId, java.lang.String packageName, com.android.server.job.controllers.QuotaController.TimingSession session, boolean isExpedited) {
        saveTimingSession(userId, packageName, session, isExpedited, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveTimingSession(int userId, java.lang.String packageName, com.android.server.job.controllers.QuotaController.TimingSession session, boolean isExpedited, long debitAdjustment) {
        synchronized (this.mLock) {
            android.util.SparseArrayMap<java.lang.String, java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent>> sessionMap = isExpedited ? this.mEJTimingSessions : this.mTimingEvents;
            java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> sessions = (java.util.List) sessionMap.get(userId, packageName);
            if (sessions == null) {
                sessions = new java.util.ArrayList<>();
                sessionMap.add(userId, packageName, sessions);
            }
            sessions.add(session);
            if (isExpedited) {
                com.android.server.job.controllers.QuotaController.ShrinkableDebits quota = getEJDebitsLocked(userId, packageName);
                quota.transactLocked((session.endTimeElapsed - session.startTimeElapsed) + debitAdjustment);
            } else {
                invalidateAllExecutionStatsLocked(userId, packageName);
                maybeScheduleCleanupAlarmLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantRewardForInstantEvent(int userId, java.lang.String packageName, long credit) {
        if (credit == 0) {
            return;
        }
        synchronized (this.mLock) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            com.android.server.job.controllers.QuotaController.ShrinkableDebits quota = getEJDebitsLocked(userId, packageName);
            if (transactQuotaLocked(userId, packageName, nowElapsed, quota, credit)) {
                this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForPkgLocked(nowElapsed, userId, packageName));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean transactQuotaLocked(int userId, java.lang.String packageName, long nowElapsed, com.android.server.job.controllers.QuotaController.ShrinkableDebits debits, long credit) {
        com.android.server.job.controllers.QuotaController.Timer ejTimer;
        long oldTally = debits.getTallyLocked();
        long leftover = debits.transactLocked(-credit);
        if (DEBUG) {
            android.util.Slog.d(TAG, "debits overflowed by " + leftover);
        }
        boolean changed = oldTally != debits.getTallyLocked();
        if (leftover != 0 && (ejTimer = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.get(userId, packageName)) != null && ejTimer.isActive()) {
            ejTimer.updateDebitAdjustment(nowElapsed, leftover);
            return true;
        }
        return changed;
    }

    private final class EarliestEndTimeFunctor implements java.util.function.Consumer<java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent>> {
        public long earliestEndElapsed;

        private EarliestEndTimeFunctor() {
            this.earliestEndElapsed = Long.MAX_VALUE;
        }

        @Override // java.util.function.Consumer
        public void accept(java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events) {
            if (events != null && events.size() > 0) {
                this.earliestEndElapsed = java.lang.Math.min(this.earliestEndElapsed, events.get(0).getEndTimeElapsed());
            }
        }

        void reset() {
            this.earliestEndElapsed = Long.MAX_VALUE;
        }
    }

    void maybeScheduleCleanupAlarmLocked() {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (this.mNextCleanupTimeElapsed > nowElapsed) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Not scheduling cleanup since there's already one at " + this.mNextCleanupTimeElapsed + " (in " + (this.mNextCleanupTimeElapsed - nowElapsed) + "ms)");
                return;
            }
            return;
        }
        this.mEarliestEndTimeFunctor.reset();
        this.mTimingEvents.forEach(this.mEarliestEndTimeFunctor);
        this.mEJTimingSessions.forEach(this.mEarliestEndTimeFunctor);
        long earliestEndElapsed = this.mEarliestEndTimeFunctor.earliestEndElapsed;
        if (earliestEndElapsed == Long.MAX_VALUE) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Didn't find a time to schedule cleanup");
                return;
            }
            return;
        }
        long nextCleanupElapsed = 86400000 + earliestEndElapsed;
        if (nextCleanupElapsed - this.mNextCleanupTimeElapsed <= 600000) {
            nextCleanupElapsed = this.mNextCleanupTimeElapsed + 600000;
        }
        this.mNextCleanupTimeElapsed = nextCleanupElapsed;
        this.mAlarmManager.set(3, nextCleanupElapsed, ALARM_TAG_CLEANUP, this.mSessionCleanupAlarmListener, this.mHandler);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Scheduled next cleanup for " + this.mNextCleanupTimeElapsed);
        }
    }

    private class TimerChargingUpdateFunctor implements java.util.function.Consumer<com.android.server.job.controllers.QuotaController.Timer> {
        private boolean mIsCharging;
        private long mNowElapsed;

        private TimerChargingUpdateFunctor() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setStatus(long nowElapsed, boolean isCharging) {
            this.mNowElapsed = nowElapsed;
            this.mIsCharging = isCharging;
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.QuotaController.Timer timer) {
            if (com.android.server.job.JobSchedulerService.standbyBucketForPackage(timer.mPkg.packageName, timer.mPkg.userId, this.mNowElapsed) != 5) {
                timer.onStateChangedLocked(this.mNowElapsed, this.mIsCharging);
            }
        }
    }

    private void handleNewChargingStateLocked() {
        this.mTimerChargingUpdateFunctor.setStatus(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis(), this.mService.isBatteryCharging());
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleNewChargingStateLocked: " + this.mService.isBatteryCharging());
        }
        this.mEJPkgTimers.forEach(this.mTimerChargingUpdateFunctor);
        this.mPkgTimers.forEach(this.mTimerChargingUpdateFunctor);
        com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleNewChargingStateLocked$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleNewChargingStateLocked$1() {
        synchronized (this.mLock) {
            maybeUpdateAllConstraintsLocked();
        }
    }

    private void maybeUpdateAllConstraintsLocked() {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        for (int u = 0; u < this.mTrackedJobs.numMaps(); u++) {
            int userId = this.mTrackedJobs.keyAt(u);
            for (int p = 0; p < this.mTrackedJobs.numElementsForKey(userId); p++) {
                java.lang.String packageName = (java.lang.String) this.mTrackedJobs.keyAt(u, p);
                changedJobs.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) maybeUpdateConstraintForPkgLocked(nowElapsed, userId, packageName));
            }
        }
        int u2 = changedJobs.size();
        if (u2 > 0) {
            this.mStateChangedListener.onControllerStateChanged(changedJobs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.ArraySet<com.android.server.job.controllers.JobStatus> maybeUpdateConstraintForPkgLocked(long nowElapsed, int userId, java.lang.String packageName) {
        com.android.server.job.controllers.JobStatus js;
        boolean isWithinEJQuota;
        int i;
        boolean z;
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, packageName);
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
        if (jobs == null || jobs.size() == 0) {
            return changedJobs;
        }
        boolean z2 = false;
        int realStandbyBucket = jobs.valueAt(0).getStandbyBucket();
        boolean realInQuota = isWithinQuotaLocked(userId, packageName, realStandbyBucket);
        boolean z3 = true;
        boolean outOfEJQuota = false;
        int i2 = jobs.size() - 1;
        while (i2 >= 0) {
            com.android.server.job.controllers.JobStatus js2 = jobs.valueAt(i2);
            boolean isWithinEJQuota2 = (js2.isRequestedExpeditedJob() && isWithinEJQuotaLocked(js2)) ? z3 : z2;
            if (isTopStartedJobLocked(js2)) {
                if (!js2.setQuotaConstraintSatisfied(nowElapsed, z3)) {
                    js = js2;
                    isWithinEJQuota = isWithinEJQuota2;
                    i = i2;
                    z = z3;
                } else {
                    changedJobs.add(js2);
                    js = js2;
                    isWithinEJQuota = isWithinEJQuota2;
                    i = i2;
                    z = z3;
                }
            } else {
                if (realStandbyBucket == 6 || realStandbyBucket == 0 || realStandbyBucket != js2.getEffectiveStandbyBucket()) {
                    js = js2;
                    isWithinEJQuota = isWithinEJQuota2;
                    i = i2;
                    z = z3;
                } else if (com.android.server.job.Flags.countQuotaFix() && this.mService.isCurrentlyRunningLocked(js2)) {
                    js = js2;
                    isWithinEJQuota = isWithinEJQuota2;
                    i = i2;
                    z = z3;
                } else {
                    js = js2;
                    isWithinEJQuota = isWithinEJQuota2;
                    i = i2;
                    z = z3;
                    if (setConstraintSatisfied(js2, nowElapsed, realInQuota, isWithinEJQuota)) {
                        changedJobs.add(js);
                    }
                }
                if (setConstraintSatisfied(js, nowElapsed, isWithinQuotaLocked(js), isWithinEJQuota)) {
                    changedJobs.add(js);
                }
            }
            if (js.isRequestedExpeditedJob()) {
                boolean isWithinEJQuota3 = isWithinEJQuota;
                if (setExpeditedQuotaApproved(js, nowElapsed, isWithinEJQuota3)) {
                    changedJobs.add(js);
                }
                outOfEJQuota |= !isWithinEJQuota3 ? z : false;
            }
            i2 = i - 1;
            z3 = z;
            z2 = false;
        }
        if (!realInQuota || outOfEJQuota) {
            maybeScheduleStartAlarmLocked(userId, packageName, realStandbyBucket);
        } else {
            this.mInQuotaAlarmQueue.removeAlarmForKey(android.content.pm.UserPackage.of(userId, packageName));
        }
        return changedJobs;
    }

    private class UidConstraintUpdater implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        public final android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs;
        private final android.util.SparseArrayMap<java.lang.String, java.lang.Integer> mToScheduleStartAlarms;
        long mUpdateTimeElapsed;

        private UidConstraintUpdater() {
            this.mToScheduleStartAlarms = new android.util.SparseArrayMap<>();
            this.changedJobs = new android.util.ArraySet<>();
            this.mUpdateTimeElapsed = 0L;
        }

        void prepare() {
            this.mUpdateTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.changedJobs.clear();
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus jobStatus) {
            boolean isWithinEJQuota;
            if (jobStatus.isRequestedExpeditedJob()) {
                isWithinEJQuota = com.android.server.job.controllers.QuotaController.this.isWithinEJQuotaLocked(jobStatus);
            } else {
                isWithinEJQuota = false;
            }
            if (com.android.server.job.controllers.QuotaController.this.setConstraintSatisfied(jobStatus, this.mUpdateTimeElapsed, com.android.server.job.controllers.QuotaController.this.isWithinQuotaLocked(jobStatus), isWithinEJQuota)) {
                this.changedJobs.add(jobStatus);
            }
            if (com.android.server.job.controllers.QuotaController.this.setExpeditedQuotaApproved(jobStatus, this.mUpdateTimeElapsed, isWithinEJQuota)) {
                this.changedJobs.add(jobStatus);
            }
            int userId = jobStatus.getSourceUserId();
            java.lang.String packageName = jobStatus.getSourcePackageName();
            int realStandbyBucket = jobStatus.getStandbyBucket();
            if (isWithinEJQuota && com.android.server.job.controllers.QuotaController.this.isWithinQuotaLocked(userId, packageName, realStandbyBucket)) {
                com.android.server.job.controllers.QuotaController.this.mInQuotaAlarmQueue.removeAlarmForKey(android.content.pm.UserPackage.of(userId, packageName));
            } else {
                this.mToScheduleStartAlarms.add(userId, packageName, java.lang.Integer.valueOf(realStandbyBucket));
            }
        }

        void postProcess() {
            for (int u = 0; u < this.mToScheduleStartAlarms.numMaps(); u++) {
                int userId = this.mToScheduleStartAlarms.keyAt(u);
                for (int p = 0; p < this.mToScheduleStartAlarms.numElementsForKey(userId); p++) {
                    java.lang.String packageName = (java.lang.String) this.mToScheduleStartAlarms.keyAt(u, p);
                    int standbyBucket = ((java.lang.Integer) this.mToScheduleStartAlarms.get(userId, packageName)).intValue();
                    com.android.server.job.controllers.QuotaController.this.maybeScheduleStartAlarmLocked(userId, packageName, standbyBucket);
                }
            }
        }

        void reset() {
            this.mToScheduleStartAlarms.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.ArraySet<com.android.server.job.controllers.JobStatus> maybeUpdateConstraintForUidLocked(int uid) {
        this.mUpdateUidConstraints.prepare();
        this.mService.getJobStore().forEachJobForSourceUid(uid, this.mUpdateUidConstraints);
        this.mUpdateUidConstraints.postProcess();
        this.mUpdateUidConstraints.reset();
        return this.mUpdateUidConstraints.changedJobs;
    }

    void maybeScheduleStartAlarmLocked(int userId, java.lang.String packageName, int standbyBucket) {
        long inRegularQuotaTimeElapsed;
        long inEJQuotaTimeElapsed;
        if (standbyBucket == 4) {
            return;
        }
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, packageName);
        if (jobs == null || jobs.size() == 0) {
            android.util.Slog.e(TAG, "maybeScheduleStartAlarmLocked called for " + packageToString(userId, packageName) + " that has no jobs");
            this.mInQuotaAlarmQueue.removeAlarmForKey(android.content.pm.UserPackage.of(userId, packageName));
            return;
        }
        com.android.server.job.controllers.QuotaController.ExecutionStats stats = getExecutionStatsLocked(userId, packageName, standbyBucket);
        boolean isUnderJobCountQuota = isUnderJobCountQuotaLocked(stats);
        boolean isUnderTimingSessionCountQuota = isUnderSessionCountQuotaLocked(stats);
        long remainingEJQuota = getRemainingEJExecutionTimeLocked(userId, packageName);
        boolean inRegularQuota = stats.executionTimeInWindowMs < this.mAllowedTimePerPeriodMs[standbyBucket] && stats.executionTimeInMaxPeriodMs < this.mMaxExecutionTimeMs && isUnderJobCountQuota && isUnderTimingSessionCountQuota;
        if (inRegularQuota && remainingEJQuota > 0) {
            if (DEBUG) {
                android.util.Slog.e(TAG, "maybeScheduleStartAlarmLocked called for " + packageToString(userId, packageName) + " even though it already has " + getRemainingExecutionTimeLocked(userId, packageName, standbyBucket) + "ms in its quota.");
            }
            this.mInQuotaAlarmQueue.removeAlarmForKey(android.content.pm.UserPackage.of(userId, packageName));
            this.mHandler.obtainMessage(2, userId, 0, packageName).sendToTarget();
            return;
        }
        long inRegularQuotaTimeElapsed2 = Long.MAX_VALUE;
        long inEJQuotaTimeElapsed2 = Long.MAX_VALUE;
        if (!inRegularQuota) {
            long inQuotaTimeElapsed = stats.inQuotaTimeElapsed;
            if (!isUnderJobCountQuota && stats.bgJobCountInWindow < stats.jobCountLimit) {
                inQuotaTimeElapsed = java.lang.Math.max(inQuotaTimeElapsed, stats.jobRateLimitExpirationTimeElapsed);
            }
            if (!isUnderTimingSessionCountQuota && stats.sessionCountInWindow < stats.sessionCountLimit) {
                inQuotaTimeElapsed = java.lang.Math.max(inQuotaTimeElapsed, stats.sessionRateLimitExpirationTimeElapsed);
            }
            inRegularQuotaTimeElapsed2 = inQuotaTimeElapsed;
        }
        if (remainingEJQuota <= 0) {
            long limitMs = getEJLimitMsLocked(userId, packageName, standbyBucket) - this.mQuotaBufferMs;
            long sumMs = 0;
            com.android.server.job.controllers.QuotaController.Timer ejTimer = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.get(userId, packageName);
            if (ejTimer != null && ejTimer.isActive()) {
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                sumMs = 0 + ejTimer.getCurrentDuration(nowElapsed);
                if (sumMs >= limitMs) {
                    inEJQuotaTimeElapsed2 = (nowElapsed - limitMs) + this.mEJLimitWindowSizeMs;
                }
            }
            java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> timingSessions = (java.util.List) this.mEJTimingSessions.get(userId, packageName);
            if (timingSessions != null) {
                int i = timingSessions.size() - 1;
                while (true) {
                    if (i < 0) {
                        inRegularQuotaTimeElapsed = inRegularQuotaTimeElapsed2;
                        break;
                    }
                    com.android.server.job.controllers.QuotaController.TimingSession ts = (com.android.server.job.controllers.QuotaController.TimingSession) timingSessions.get(i);
                    long j = ts.endTimeElapsed;
                    inRegularQuotaTimeElapsed = inRegularQuotaTimeElapsed2;
                    long inRegularQuotaTimeElapsed3 = ts.startTimeElapsed;
                    long durationMs = j - inRegularQuotaTimeElapsed3;
                    sumMs += durationMs;
                    if (sumMs < limitMs) {
                        i--;
                        inRegularQuotaTimeElapsed2 = inRegularQuotaTimeElapsed;
                    } else {
                        long j2 = ts.startTimeElapsed + (sumMs - limitMs);
                        long durationMs2 = this.mEJLimitWindowSizeMs;
                        inEJQuotaTimeElapsed2 = j2 + durationMs2;
                        break;
                    }
                }
                inEJQuotaTimeElapsed = inEJQuotaTimeElapsed2;
            } else {
                inRegularQuotaTimeElapsed = inRegularQuotaTimeElapsed2;
                if ((ejTimer == null || !ejTimer.isActive()) && inRegularQuota) {
                    android.util.Slog.wtf(TAG, packageToString(userId, packageName) + " has 0 EJ quota without running anything");
                    return;
                }
                inEJQuotaTimeElapsed = inEJQuotaTimeElapsed2;
            }
        } else {
            inRegularQuotaTimeElapsed = inRegularQuotaTimeElapsed2;
            inEJQuotaTimeElapsed = Long.MAX_VALUE;
        }
        long inQuotaTimeElapsed2 = java.lang.Math.min(inRegularQuotaTimeElapsed, inEJQuotaTimeElapsed);
        if (inQuotaTimeElapsed2 <= com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis()) {
            long nowElapsed2 = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            android.util.Slog.wtf(TAG, "In quota time is " + (nowElapsed2 - inQuotaTimeElapsed2) + "ms old. Now=" + nowElapsed2 + ", inQuotaTime=" + inQuotaTimeElapsed2 + ": " + stats);
            inQuotaTimeElapsed2 = nowElapsed2 + 300000;
        }
        this.mInQuotaAlarmQueue.addAlarm(android.content.pm.UserPackage.of(userId, packageName), inQuotaTimeElapsed2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setConstraintSatisfied(com.android.server.job.controllers.JobStatus jobStatus, long nowElapsed, boolean isWithinQuota, boolean isWithinEjQuota) {
        boolean isSatisfied;
        if (jobStatus.startedAsExpeditedJob) {
            isSatisfied = isWithinEjQuota;
        } else if (this.mService.isCurrentlyRunningLocked(jobStatus)) {
            isSatisfied = isWithinQuota;
        } else {
            isSatisfied = isWithinEjQuota || isWithinQuota;
        }
        if (!isSatisfied && jobStatus.getWhenStandbyDeferred() == 0) {
            jobStatus.setWhenStandbyDeferred(nowElapsed);
        }
        return jobStatus.setQuotaConstraintSatisfied(nowElapsed, isSatisfied);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setExpeditedQuotaApproved(com.android.server.job.controllers.JobStatus jobStatus, long nowElapsed, boolean isWithinQuota) {
        if (jobStatus.setExpeditedJobQuotaApproved(nowElapsed, isWithinQuota)) {
            this.mBackgroundJobsController.evaluateStateLocked(jobStatus);
            this.mConnectivityController.evaluateStateLocked(jobStatus);
            if (isWithinQuota && jobStatus.isReady()) {
                this.mStateChangedListener.onRunJobNow(jobStatus);
                return true;
            }
            return true;
        }
        return false;
    }

    static final class TimingSession implements com.android.server.job.controllers.QuotaController.TimedEvent {
        public final int bgJobCount;
        public final long endTimeElapsed;
        private final int mHashCode;
        public final long startTimeElapsed;

        TimingSession(long startElapsed, long endElapsed, int bgJobCount) {
            this.startTimeElapsed = startElapsed;
            this.endTimeElapsed = endElapsed;
            this.bgJobCount = bgJobCount;
            int hashCode = (0 * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.startTimeElapsed);
            this.mHashCode = (((hashCode * 31) + com.android.server.job.controllers.QuotaController.hashLong(this.endTimeElapsed)) * 31) + bgJobCount;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public long getEndTimeElapsed() {
            return this.endTimeElapsed;
        }

        public java.lang.String toString() {
            return "TimingSession{" + this.startTimeElapsed + "->" + this.endTimeElapsed + ", " + this.bgJobCount + "}";
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.job.controllers.QuotaController.TimingSession)) {
                return false;
            }
            com.android.server.job.controllers.QuotaController.TimingSession other = (com.android.server.job.controllers.QuotaController.TimingSession) obj;
            return this.startTimeElapsed == other.startTimeElapsed && this.endTimeElapsed == other.endTimeElapsed && this.bgJobCount == other.bgJobCount;
        }

        public int hashCode() {
            return this.mHashCode;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public void dump(android.util.IndentingPrintWriter pw) {
            pw.print(this.startTimeElapsed);
            pw.print(" -> ");
            pw.print(this.endTimeElapsed);
            pw.print(" (");
            pw.print(this.endTimeElapsed - this.startTimeElapsed);
            pw.print("), ");
            pw.print(this.bgJobCount);
            pw.print(" bg jobs.");
            pw.println();
        }

        public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, this.startTimeElapsed);
            proto.write(1112396529666L, this.endTimeElapsed);
            proto.write(1120986464259L, this.bgJobCount);
            proto.end(token);
        }
    }

    static final class QuotaBump implements com.android.server.job.controllers.QuotaController.TimedEvent {
        public final long eventTimeElapsed;

        QuotaBump(long eventElapsed) {
            this.eventTimeElapsed = eventElapsed;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public long getEndTimeElapsed() {
            return this.eventTimeElapsed;
        }

        @Override // com.android.server.job.controllers.QuotaController.TimedEvent
        public void dump(android.util.IndentingPrintWriter pw) {
            pw.print("Quota bump @ ");
            pw.print(this.eventTimeElapsed);
            pw.println();
        }
    }

    static final class ShrinkableDebits {
        private long mDebitTally = 0;
        private int mStandbyBucket;

        ShrinkableDebits(int standbyBucket) {
            this.mStandbyBucket = standbyBucket;
        }

        long getTallyLocked() {
            return this.mDebitTally;
        }

        long transactLocked(long amount) {
            long leftover;
            if (amount >= 0 || java.lang.Math.abs(amount) <= this.mDebitTally) {
                leftover = 0;
            } else {
                leftover = this.mDebitTally + amount;
            }
            this.mDebitTally = java.lang.Math.max(0L, this.mDebitTally + amount);
            return leftover;
        }

        void setStandbyBucketLocked(int standbyBucket) {
            this.mStandbyBucket = standbyBucket;
        }

        int getStandbyBucketLocked() {
            return this.mStandbyBucket;
        }

        public java.lang.String toString() {
            return "ShrinkableDebits { debit tally: " + this.mDebitTally + ", bucket: " + this.mStandbyBucket + " }";
        }

        void dumpLocked(android.util.IndentingPrintWriter pw) {
            pw.println(toString());
        }
    }

    private final class Timer {
        private int mBgJobCount;
        private long mDebitAdjustment;
        private final android.content.pm.UserPackage mPkg;
        private final boolean mRegularJobTimer;
        private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mRunningBgJobs = new android.util.ArraySet<>();
        private long mStartTimeElapsed;
        private final int mUid;

        Timer(int uid, int userId, java.lang.String packageName, boolean regularJobTimer) {
            this.mPkg = android.content.pm.UserPackage.of(userId, packageName);
            this.mUid = uid;
            this.mRegularJobTimer = regularJobTimer;
        }

        void startTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
            if (jobStatus.shouldTreatAsUserInitiatedJob()) {
                if (com.android.server.job.controllers.QuotaController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.QuotaController.TAG, "Timer ignoring " + jobStatus.toShortString() + " because it's user-initiated");
                    return;
                }
                return;
            }
            if (com.android.server.job.controllers.QuotaController.this.isTopStartedJobLocked(jobStatus)) {
                if (com.android.server.job.controllers.QuotaController.DEBUG) {
                    android.util.Slog.v(com.android.server.job.controllers.QuotaController.TAG, "Timer ignoring " + jobStatus.toShortString() + " because isTop");
                    return;
                }
                return;
            }
            if (com.android.server.job.controllers.QuotaController.DEBUG) {
                android.util.Slog.v(com.android.server.job.controllers.QuotaController.TAG, "Starting to track " + jobStatus.toShortString());
            }
            if (this.mRunningBgJobs.add(jobStatus) && shouldTrackLocked()) {
                this.mBgJobCount++;
                if (this.mRegularJobTimer) {
                    com.android.server.job.controllers.QuotaController.this.incrementJobCountLocked(this.mPkg.userId, this.mPkg.packageName, 1);
                    if (com.android.server.job.Flags.countQuotaFix()) {
                        com.android.server.job.controllers.QuotaController.ExecutionStats stats = com.android.server.job.controllers.QuotaController.this.getExecutionStatsLocked(this.mPkg.userId, this.mPkg.packageName, jobStatus.getEffectiveStandbyBucket(), false);
                        if (!com.android.server.job.controllers.QuotaController.this.isUnderJobCountQuotaLocked(stats)) {
                            com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(7, this.mPkg).sendToTarget();
                        }
                    }
                }
                if (this.mRunningBgJobs.size() == 1) {
                    this.mStartTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                    this.mDebitAdjustment = 0L;
                    if (this.mRegularJobTimer) {
                        com.android.server.job.controllers.QuotaController.this.invalidateAllExecutionStatsLocked(this.mPkg.userId, this.mPkg.packageName);
                    }
                    scheduleCutoff();
                    return;
                }
                return;
            }
            if (android.os.Trace.isTagEnabled(524288L)) {
                android.os.Trace.instantForTrack(524288L, "JobScheduler", "QC/- " + this.mPkg);
            }
        }

        void stopTrackingJob(com.android.server.job.controllers.JobStatus jobStatus) {
            if (com.android.server.job.controllers.QuotaController.DEBUG) {
                android.util.Slog.v(com.android.server.job.controllers.QuotaController.TAG, "Stopping tracking of " + jobStatus.toShortString());
            }
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                if (this.mRunningBgJobs.size() == 0) {
                    if (com.android.server.job.controllers.QuotaController.DEBUG) {
                        android.util.Slog.d(com.android.server.job.controllers.QuotaController.TAG, "Timer isn't tracking any jobs but still told to stop");
                    }
                    return;
                }
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                int standbyBucket = com.android.server.job.JobSchedulerService.standbyBucketForPackage(this.mPkg.packageName, this.mPkg.userId, nowElapsed);
                if (this.mRunningBgJobs.remove(jobStatus) && this.mRunningBgJobs.size() == 0 && !com.android.server.job.controllers.QuotaController.this.isQuotaFreeLocked(standbyBucket)) {
                    emitSessionLocked(nowElapsed);
                    cancelCutoff();
                }
            }
        }

        void updateDebitAdjustment(long nowElapsed, long debit) {
            this.mDebitAdjustment = java.lang.Math.max(this.mDebitAdjustment + debit, this.mStartTimeElapsed - nowElapsed);
        }

        void dropEverythingLocked() {
            this.mRunningBgJobs.clear();
            cancelCutoff();
        }

        private void emitSessionLocked(long nowElapsed) {
            if (this.mBgJobCount <= 0) {
                return;
            }
            com.android.server.job.controllers.QuotaController.TimingSession ts = new com.android.server.job.controllers.QuotaController.TimingSession(this.mStartTimeElapsed, nowElapsed, this.mBgJobCount);
            com.android.server.job.controllers.QuotaController.this.saveTimingSession(this.mPkg.userId, this.mPkg.packageName, ts, !this.mRegularJobTimer, this.mDebitAdjustment);
            this.mBgJobCount = 0;
            cancelCutoff();
            if (this.mRegularJobTimer) {
                com.android.server.job.controllers.QuotaController.this.incrementTimingSessionCountLocked(this.mPkg.userId, this.mPkg.packageName);
            }
        }

        public boolean isActive() {
            boolean z;
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                z = this.mBgJobCount > 0;
            }
            return z;
        }

        boolean isRunning(com.android.server.job.controllers.JobStatus jobStatus) {
            return this.mRunningBgJobs.contains(jobStatus);
        }

        long getCurrentDuration(long nowElapsed) {
            long j;
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                j = !isActive() ? 0L : (nowElapsed - this.mStartTimeElapsed) + this.mDebitAdjustment;
            }
            return j;
        }

        int getBgJobCount() {
            int i;
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                i = this.mBgJobCount;
            }
            return i;
        }

        private boolean shouldTrackLocked() {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            int standbyBucket = com.android.server.job.JobSchedulerService.standbyBucketForPackage(this.mPkg.packageName, this.mPkg.userId, nowElapsed);
            boolean hasTempAllowlistExemption = !this.mRegularJobTimer && com.android.server.job.controllers.QuotaController.this.hasTempAllowlistExemptionLocked(this.mUid, standbyBucket, nowElapsed);
            long topAppGracePeriodEndElapsed = com.android.server.job.controllers.QuotaController.this.mTopAppGraceCache.get(this.mUid);
            boolean hasTopAppExemption = !this.mRegularJobTimer && (com.android.server.job.controllers.QuotaController.this.mTopAppCache.get(this.mUid) || nowElapsed < topAppGracePeriodEndElapsed);
            if (com.android.server.job.controllers.QuotaController.DEBUG) {
                android.util.Slog.d(com.android.server.job.controllers.QuotaController.TAG, "quotaFree=" + com.android.server.job.controllers.QuotaController.this.isQuotaFreeLocked(standbyBucket) + " isFG=" + com.android.server.job.controllers.QuotaController.this.mForegroundUids.get(this.mUid) + " tempEx=" + hasTempAllowlistExemption + " topEx=" + hasTopAppExemption);
            }
            return (com.android.server.job.controllers.QuotaController.this.isQuotaFreeLocked(standbyBucket) || com.android.server.job.controllers.QuotaController.this.mForegroundUids.get(this.mUid) || hasTempAllowlistExemption || hasTopAppExemption) ? false : true;
        }

        void onStateChangedLocked(long nowElapsed, boolean isQuotaFree) {
            if (isQuotaFree) {
                emitSessionLocked(nowElapsed);
                return;
            }
            if (!isActive() && shouldTrackLocked() && this.mRunningBgJobs.size() > 0) {
                this.mStartTimeElapsed = nowElapsed;
                this.mDebitAdjustment = 0L;
                this.mBgJobCount = this.mRunningBgJobs.size();
                if (this.mRegularJobTimer) {
                    com.android.server.job.controllers.QuotaController.this.incrementJobCountLocked(this.mPkg.userId, this.mPkg.packageName, this.mBgJobCount);
                    com.android.server.job.controllers.QuotaController.this.invalidateAllExecutionStatsLocked(this.mPkg.userId, this.mPkg.packageName);
                }
                scheduleCutoff();
            }
        }

        void rescheduleCutoff() {
            cancelCutoff();
            scheduleCutoff();
        }

        private void scheduleCutoff() {
            long timeRemainingMs;
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                if (isActive()) {
                    android.os.Message msg = com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(this.mRegularJobTimer ? 0 : 4, this.mPkg);
                    if (this.mRegularJobTimer) {
                        timeRemainingMs = com.android.server.job.controllers.QuotaController.this.getTimeUntilQuotaConsumedLocked(this.mPkg.userId, this.mPkg.packageName);
                    } else {
                        timeRemainingMs = com.android.server.job.controllers.QuotaController.this.getTimeUntilEJQuotaConsumedLocked(this.mPkg.userId, this.mPkg.packageName);
                    }
                    if (com.android.server.job.controllers.QuotaController.DEBUG) {
                        android.util.Slog.i(com.android.server.job.controllers.QuotaController.TAG, (this.mRegularJobTimer ? "Regular job" : "EJ") + " for " + this.mPkg + " has " + timeRemainingMs + "ms left.");
                    }
                    com.android.server.job.controllers.QuotaController.this.mHandler.sendMessageDelayed(msg, timeRemainingMs);
                }
            }
        }

        private void cancelCutoff() {
            com.android.server.job.controllers.QuotaController.this.mHandler.removeMessages(this.mRegularJobTimer ? 0 : 4, this.mPkg);
        }

        public void dump(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
            pw.print("Timer<");
            pw.print(this.mRegularJobTimer ? "REG" : "EJ");
            pw.print(">{");
            pw.print(this.mPkg);
            pw.print("} ");
            if (isActive()) {
                pw.print("started at ");
                pw.print(this.mStartTimeElapsed);
                pw.print(" (");
                pw.print(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() - this.mStartTimeElapsed);
                pw.print("ms ago)");
            } else {
                pw.print("NOT active");
            }
            pw.print(", ");
            pw.print(this.mBgJobCount);
            pw.print(" running bg jobs");
            if (!this.mRegularJobTimer) {
                pw.print(" (debit adj=");
                pw.print(this.mDebitAdjustment);
                pw.print(")");
            }
            pw.println();
            pw.increaseIndent();
            for (int i = 0; i < this.mRunningBgJobs.size(); i++) {
                com.android.server.job.controllers.JobStatus js = this.mRunningBgJobs.valueAt(i);
                if (predicate.test(js)) {
                    pw.println(js.toShortString());
                }
            }
            pw.decreaseIndent();
        }

        public void dump(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
            long token = proto.start(fieldId);
            proto.write(1133871366146L, isActive());
            proto.write(1112396529667L, this.mStartTimeElapsed);
            proto.write(1120986464260L, this.mBgJobCount);
            for (int i = 0; i < this.mRunningBgJobs.size(); i++) {
                com.android.server.job.controllers.JobStatus js = this.mRunningBgJobs.valueAt(i);
                if (predicate.test(js)) {
                    js.writeToShortProto(proto, 2246267895813L);
                }
            }
            proto.end(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class TopAppTimer {
        private final android.util.SparseArray<android.app.usage.UsageEvents.Event> mActivities = new android.util.SparseArray<>();
        private final android.content.pm.UserPackage mPkg;
        private long mStartTimeElapsed;

        TopAppTimer(int userId, java.lang.String packageName) {
            this.mPkg = android.content.pm.UserPackage.of(userId, packageName);
        }

        private int calculateTimeChunks(long nowElapsed) {
            long totalTopTimeMs = nowElapsed - this.mStartTimeElapsed;
            int numTimeChunks = (int) (totalTopTimeMs / com.android.server.job.controllers.QuotaController.this.mEJTopAppTimeChunkSizeMs);
            long remainderMs = totalTopTimeMs % com.android.server.job.controllers.QuotaController.this.mEJTopAppTimeChunkSizeMs;
            if (remainderMs >= 1000) {
                return numTimeChunks + 1;
            }
            return numTimeChunks;
        }

        long getPendingReward(long nowElapsed) {
            return com.android.server.job.controllers.QuotaController.this.mEJRewardTopAppMs * ((long) calculateTimeChunks(nowElapsed));
        }

        void processEventLocked(android.app.usage.UsageEvents.Event event) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            switch (event.getEventType()) {
                case 1:
                    if (this.mActivities.size() == 0) {
                        this.mStartTimeElapsed = nowElapsed;
                    }
                    this.mActivities.put(event.mInstanceId, event);
                    break;
                case 2:
                case 23:
                case 24:
                    android.app.usage.UsageEvents.Event existingEvent = (android.app.usage.UsageEvents.Event) this.mActivities.removeReturnOld(event.mInstanceId);
                    if (existingEvent != null && this.mActivities.size() == 0) {
                        long pendingReward = getPendingReward(nowElapsed);
                        if (com.android.server.job.controllers.QuotaController.DEBUG) {
                            android.util.Slog.d(com.android.server.job.controllers.QuotaController.TAG, "Crediting " + this.mPkg + " " + pendingReward + "ms for " + calculateTimeChunks(nowElapsed) + " time chunks");
                        }
                        com.android.server.job.controllers.QuotaController.ShrinkableDebits debits = com.android.server.job.controllers.QuotaController.this.getEJDebitsLocked(this.mPkg.userId, this.mPkg.packageName);
                        if (com.android.server.job.controllers.QuotaController.this.transactQuotaLocked(this.mPkg.userId, this.mPkg.packageName, nowElapsed, debits, pendingReward)) {
                            com.android.server.job.controllers.QuotaController.this.mStateChangedListener.onControllerStateChanged(com.android.server.job.controllers.QuotaController.this.maybeUpdateConstraintForPkgLocked(nowElapsed, this.mPkg.userId, this.mPkg.packageName));
                        }
                        break;
                    }
                    break;
            }
        }

        boolean isActive() {
            boolean z;
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                z = this.mActivities.size() > 0;
            }
            return z;
        }

        public void dump(android.util.IndentingPrintWriter pw) {
            pw.print("TopAppTimer{");
            pw.print(this.mPkg);
            pw.print("} ");
            if (isActive()) {
                pw.print("started at ");
                pw.print(this.mStartTimeElapsed);
                pw.print(" (");
                pw.print(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() - this.mStartTimeElapsed);
                pw.print("ms ago)");
            } else {
                pw.print("NOT active");
            }
            pw.println();
            pw.increaseIndent();
            for (int i = 0; i < this.mActivities.size(); i++) {
                android.app.usage.UsageEvents.Event event = this.mActivities.valueAt(i);
                pw.println(event.getClassName());
            }
            pw.decreaseIndent();
        }

        public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1133871366146L, isActive());
            proto.write(1112396529667L, this.mStartTimeElapsed);
            proto.write(1120986464260L, this.mActivities.size());
            proto.end(token);
        }
    }

    final class StandbyTracker extends com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener {
        StandbyTracker() {
        }

        public void onAppIdleStateChanged(final java.lang.String packageName, final int userId, boolean idle, final int bucket, int reason) {
            com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.QuotaController$StandbyTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAppIdleStateChanged$0(bucket, userId, packageName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAppIdleStateChanged$0(int bucket, int userId, java.lang.String packageName) {
            int bucketIndex = com.android.server.job.JobSchedulerService.standbyBucketToBucketIndex(bucket);
            com.android.server.job.controllers.QuotaController.this.updateStandbyBucket(userId, packageName, bucketIndex);
        }

        public void triggerTemporaryQuotaBump(java.lang.String packageName, int userId) {
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events = (java.util.List) com.android.server.job.controllers.QuotaController.this.mTimingEvents.get(userId, packageName);
                if (events != null && events.size() != 0) {
                    events.add(new com.android.server.job.controllers.QuotaController.QuotaBump(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis()));
                    com.android.server.job.controllers.QuotaController.this.invalidateAllExecutionStatsLocked(userId, packageName);
                    com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(2, userId, 0, packageName).sendToTarget();
                }
            }
        }
    }

    void updateStandbyBucket(int userId, java.lang.String packageName, int bucketIndex) {
        if (DEBUG) {
            android.util.Slog.i(TAG, "Moving pkg " + packageToString(userId, packageName) + " to bucketIndex " + bucketIndex);
        }
        java.util.List<com.android.server.job.controllers.JobStatus> restrictedChanges = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.job.controllers.QuotaController.ShrinkableDebits debits = (com.android.server.job.controllers.QuotaController.ShrinkableDebits) this.mEJStats.get(userId, packageName);
            if (debits != null) {
                debits.setStandbyBucketLocked(bucketIndex);
            }
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, packageName);
            if (jobs != null && jobs.size() != 0) {
                for (int i = jobs.size() - 1; i >= 0; i--) {
                    com.android.server.job.controllers.JobStatus js = jobs.valueAt(i);
                    if ((bucketIndex == 5 || js.getStandbyBucket() == 5) && bucketIndex != js.getStandbyBucket()) {
                        restrictedChanges.add(js);
                    }
                    js.setStandbyBucket(bucketIndex);
                }
                com.android.server.job.controllers.QuotaController.Timer timer = (com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.get(userId, packageName);
                if (timer != null && timer.isActive()) {
                    timer.rescheduleCutoff();
                }
                com.android.server.job.controllers.QuotaController.Timer timer2 = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.get(userId, packageName);
                if (timer2 != null && timer2.isActive()) {
                    timer2.rescheduleCutoff();
                }
                this.mStateChangedListener.onControllerStateChanged(maybeUpdateConstraintForPkgLocked(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis(), userId, packageName));
                if (restrictedChanges.size() > 0) {
                    this.mStateChangedListener.onRestrictedBucketChanged(restrictedChanges);
                }
            }
        }
    }

    final class UsageEventTracker implements android.app.usage.UsageStatsManagerInternal.UsageEventListener {
        UsageEventTracker() {
        }

        @Override // android.app.usage.UsageStatsManagerInternal.UsageEventListener
        public void onUsageEvent(int userId, android.app.usage.UsageEvents.Event event) {
            switch (event.getEventType()) {
                case 1:
                case 2:
                case 7:
                case 9:
                case 10:
                case 12:
                case 23:
                case 24:
                    com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(5, userId, 0, event).sendToTarget();
                    break;
                default:
                    if (com.android.server.job.controllers.QuotaController.DEBUG) {
                        android.util.Slog.d(com.android.server.job.controllers.QuotaController.TAG, "Dropping usage event " + event.getEventType());
                    }
                    break;
            }
        }
    }

    final class TempAllowlistTracker implements com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener {
        TempAllowlistTracker() {
        }

        public void onAppAdded(int uid) {
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                com.android.server.job.controllers.QuotaController.this.mTempAllowlistCache.put(uid, true);
                android.util.ArraySet<java.lang.String> packages = com.android.server.job.controllers.QuotaController.this.mService.getPackagesForUidLocked(uid);
                if (packages != null) {
                    int userId = android.os.UserHandle.getUserId(uid);
                    for (int i = packages.size() - 1; i >= 0; i--) {
                        com.android.server.job.controllers.QuotaController.Timer t = (com.android.server.job.controllers.QuotaController.Timer) com.android.server.job.controllers.QuotaController.this.mEJPkgTimers.get(userId, packages.valueAt(i));
                        if (t != null) {
                            t.onStateChangedLocked(nowElapsed, true);
                        }
                    }
                    android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = com.android.server.job.controllers.QuotaController.this.maybeUpdateConstraintForUidLocked(uid);
                    if (changedJobs.size() > 0) {
                        com.android.server.job.controllers.QuotaController.this.mStateChangedListener.onControllerStateChanged(changedJobs);
                    }
                }
            }
        }

        public void onAppRemoved(int uid) {
            synchronized (com.android.server.job.controllers.QuotaController.this.mLock) {
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                long endElapsed = com.android.server.job.controllers.QuotaController.this.mEJGracePeriodTempAllowlistMs + nowElapsed;
                com.android.server.job.controllers.QuotaController.this.mTempAllowlistCache.delete(uid);
                com.android.server.job.controllers.QuotaController.this.mTempAllowlistGraceCache.put(uid, endElapsed);
                android.os.Message msg = com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(6, uid, 0);
                com.android.server.job.controllers.QuotaController.this.mHandler.sendMessageDelayed(msg, com.android.server.job.controllers.QuotaController.this.mEJGracePeriodTempAllowlistMs);
            }
        }
    }

    private static final class TimedEventTooOldPredicate implements java.util.function.Predicate<com.android.server.job.controllers.QuotaController.TimedEvent> {
        private long mNowElapsed;

        private TimedEventTooOldPredicate() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateNow() {
            this.mNowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.job.controllers.QuotaController.TimedEvent ts) {
            return ts.getEndTimeElapsed() <= this.mNowElapsed - 86400000;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(java.util.List events) {
        if (events != null) {
            events.removeIf(this.mTimedEventTooOld);
        }
    }

    void deleteObsoleteSessionsLocked() {
        this.mTimedEventTooOld.updateNow();
        this.mTimingEvents.forEach(this.mDeleteOldEventsFunctor);
        for (int uIdx = 0; uIdx < this.mEJTimingSessions.numMaps(); uIdx++) {
            int userId = this.mEJTimingSessions.keyAt(uIdx);
            for (int pIdx = 0; pIdx < this.mEJTimingSessions.numElementsForKey(userId); pIdx++) {
                java.lang.String packageName = (java.lang.String) this.mEJTimingSessions.keyAt(uIdx, pIdx);
                com.android.server.job.controllers.QuotaController.ShrinkableDebits debits = getEJDebitsLocked(userId, packageName);
                java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> sessions = (java.util.List) this.mEJTimingSessions.get(userId, packageName);
                if (sessions != null) {
                    while (sessions.size() > 0) {
                        com.android.server.job.controllers.QuotaController.TimingSession ts = (com.android.server.job.controllers.QuotaController.TimingSession) sessions.get(0);
                        if (this.mTimedEventTooOld.test((com.android.server.job.controllers.QuotaController.TimedEvent) ts)) {
                            long duration = ts.endTimeElapsed - ts.startTimeElapsed;
                            debits.transactLocked(-duration);
                            sessions.remove(0);
                        }
                    }
                }
            }
        }
    }

    private class QcHandler extends android.os.Handler {
        QcHandler(android.os.Looper looper) {
            super(looper);
        }

        /* JADX WARN: Removed duplicated region for block: B:126:0x03d2 A[Catch: all -> 0x033d, TryCatch #3 {all -> 0x033d, blocks: (B:98:0x0310, B:100:0x032e, B:102:0x0331, B:118:0x03a8, B:120:0x03b4, B:133:0x0404, B:135:0x0410, B:136:0x0417, B:122:0x03c0, B:124:0x03ca, B:126:0x03d2, B:128:0x03e6, B:129:0x03e9, B:131:0x03fe, B:132:0x0401, B:106:0x0342, B:110:0x0351, B:112:0x0371, B:114:0x037d, B:116:0x03a5, B:111:0x0361), top: B:173:0x030e, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:135:0x0410 A[Catch: all -> 0x033d, TryCatch #3 {all -> 0x033d, blocks: (B:98:0x0310, B:100:0x032e, B:102:0x0331, B:118:0x03a8, B:120:0x03b4, B:133:0x0404, B:135:0x0410, B:136:0x0417, B:122:0x03c0, B:124:0x03ca, B:126:0x03d2, B:128:0x03e6, B:129:0x03e9, B:131:0x03fe, B:132:0x0401, B:106:0x0342, B:110:0x0351, B:112:0x0371, B:114:0x037d, B:116:0x03a5, B:111:0x0361), top: B:173:0x030e, outer: #0 }] */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void handleMessage(android.os.Message r18) {
            /*
                Method dump skipped, instruction units count: 1402
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.QuotaController.QcHandler.handleMessage(android.os.Message):void");
        }
    }

    private class InQuotaAlarmQueue extends com.android.server.utils.AlarmQueue<android.content.pm.UserPackage> {
        private InQuotaAlarmQueue(android.content.Context context, android.os.Looper looper) {
            super(context, looper, com.android.server.job.controllers.QuotaController.ALARM_TAG_QUOTA_CHECK, "In quota", false, 60000L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(android.content.pm.UserPackage key, int userId) {
            return key.userId == userId;
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(android.util.ArraySet<android.content.pm.UserPackage> expired) {
            for (int i = 0; i < expired.size(); i++) {
                android.content.pm.UserPackage p = expired.valueAt(i);
                com.android.server.job.controllers.QuotaController.this.mHandler.obtainMessage(2, p.userId, 0, p.packageName).sendToTarget();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForUpdatedConstantsLocked() {
        this.mQcConstants.mShouldReevaluateConstraints = false;
        this.mQcConstants.mRateLimitingConstantsUpdated = false;
        this.mQcConstants.mExecutionPeriodConstantsUpdated = false;
        this.mQcConstants.mEJLimitConstantsUpdated = false;
        this.mQcConstants.mQuotaBumpConstantsUpdated = false;
    }

    @Override // com.android.server.job.controllers.StateController
    public void processConstantLocked(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
        this.mQcConstants.processConstantLocked(properties, key);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onConstantsUpdatedLocked() {
        if (this.mQcConstants.mShouldReevaluateConstraints) {
            com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onConstantsUpdatedLocked$3();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$3() {
        synchronized (this.mLock) {
            invalidateAllExecutionStatsLocked();
            maybeUpdateAllConstraintsLocked();
        }
    }

    class QcConstants {
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_RARE_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = 600000;
        private static final long DEFAULT_ALLOWED_TIME_PER_PERIOD_WORKING_MS = 600000;
        private static final long DEFAULT_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = 180000;
        private static final long DEFAULT_EJ_GRACE_PERIOD_TOP_APP_MS = 60000;
        private static final long DEFAULT_EJ_LIMIT_ACTIVE_MS = 1800000;
        private static final long DEFAULT_EJ_LIMIT_ADDITION_INSTALLER_MS = 1800000;
        private static final long DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS = 900000;
        private static final long DEFAULT_EJ_LIMIT_EXEMPTED_MS = 3600000;
        private static final long DEFAULT_EJ_LIMIT_FREQUENT_MS = 600000;
        private static final long DEFAULT_EJ_LIMIT_RARE_MS = 600000;
        private static final long DEFAULT_EJ_LIMIT_RESTRICTED_MS = 300000;
        private static final long DEFAULT_EJ_LIMIT_WORKING_MS = 1800000;
        private static final long DEFAULT_EJ_REWARD_INTERACTION_MS = 15000;
        private static final long DEFAULT_EJ_REWARD_NOTIFICATION_SEEN_MS = 0;
        private static final long DEFAULT_EJ_REWARD_TOP_APP_MS = 10000;
        private static final long DEFAULT_EJ_TOP_APP_TIME_CHUNK_SIZE_MS = 30000;
        private static final long DEFAULT_EJ_WINDOW_SIZE_MS = 86400000;
        private static final long DEFAULT_IN_QUOTA_BUFFER_MS = 30000;
        private static final long DEFAULT_MAX_EXECUTION_TIME_MS = 14400000;
        private static final int DEFAULT_MAX_JOB_COUNT_ACTIVE = 75;
        private static final int DEFAULT_MAX_JOB_COUNT_EXEMPTED = 75;
        private static final int DEFAULT_MAX_JOB_COUNT_FREQUENT = 200;
        private static final int DEFAULT_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        private static final int DEFAULT_MAX_JOB_COUNT_RARE = 48;
        private static final int DEFAULT_MAX_JOB_COUNT_RESTRICTED = 10;
        private static final int DEFAULT_MAX_JOB_COUNT_WORKING = 120;
        private static final int DEFAULT_MAX_SESSION_COUNT_ACTIVE = 75;
        private static final int DEFAULT_MAX_SESSION_COUNT_EXEMPTED = 75;
        private static final int DEFAULT_MAX_SESSION_COUNT_FREQUENT = 8;
        private static final int DEFAULT_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        private static final int DEFAULT_MAX_SESSION_COUNT_RARE = 3;
        private static final int DEFAULT_MAX_SESSION_COUNT_RESTRICTED = 1;
        private static final int DEFAULT_MAX_SESSION_COUNT_WORKING = 10;
        private static final long DEFAULT_MIN_QUOTA_CHECK_DELAY_MS = 60000;
        private static final long DEFAULT_QUOTA_BUMP_ADDITIONAL_DURATION_MS = 60000;
        private static final int DEFAULT_QUOTA_BUMP_ADDITIONAL_JOB_COUNT = 2;
        private static final int DEFAULT_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = 1;
        private static final int DEFAULT_QUOTA_BUMP_LIMIT = 8;
        private static final long DEFAULT_QUOTA_BUMP_WINDOW_SIZE_MS = 28800000;
        private static final long DEFAULT_RATE_LIMITING_WINDOW_MS = 60000;
        private static final long DEFAULT_TIMING_SESSION_COALESCING_DURATION_MS = 5000;
        private static final long DEFAULT_WINDOW_SIZE_ACTIVE_MS = 600000;
        private static final long DEFAULT_WINDOW_SIZE_EXEMPTED_MS = 600000;
        private static final long DEFAULT_WINDOW_SIZE_FREQUENT_MS = 28800000;
        private static final long DEFAULT_WINDOW_SIZE_RARE_MS = 86400000;
        private static final long DEFAULT_WINDOW_SIZE_RESTRICTED_MS = 86400000;
        private static final long DEFAULT_WINDOW_SIZE_WORKING_MS = 7200000;
        static final java.lang.String KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = "qc_allowed_time_per_period_active_ms";
        static final java.lang.String KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = "qc_allowed_time_per_period_exempted_ms";
        static final java.lang.String KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = "qc_allowed_time_per_period_frequent_ms";
        static final java.lang.String KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS = "qc_allowed_time_per_period_rare_ms";
        static final java.lang.String KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = "qc_allowed_time_per_period_restricted_ms";
        static final java.lang.String KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS = "qc_allowed_time_per_period_working_ms";
        static final java.lang.String KEY_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = "qc_ej_grace_period_temp_allowlist_ms";
        static final java.lang.String KEY_EJ_GRACE_PERIOD_TOP_APP_MS = "qc_ej_grace_period_top_app_ms";
        static final java.lang.String KEY_EJ_LIMIT_ACTIVE_MS = "qc_ej_limit_active_ms";
        static final java.lang.String KEY_EJ_LIMIT_ADDITION_INSTALLER_MS = "qc_ej_limit_addition_installer_ms";
        static final java.lang.String KEY_EJ_LIMIT_ADDITION_SPECIAL_MS = "qc_ej_limit_addition_special_ms";
        static final java.lang.String KEY_EJ_LIMIT_EXEMPTED_MS = "qc_ej_limit_exempted_ms";
        static final java.lang.String KEY_EJ_LIMIT_FREQUENT_MS = "qc_ej_limit_frequent_ms";
        static final java.lang.String KEY_EJ_LIMIT_RARE_MS = "qc_ej_limit_rare_ms";
        static final java.lang.String KEY_EJ_LIMIT_RESTRICTED_MS = "qc_ej_limit_restricted_ms";
        static final java.lang.String KEY_EJ_LIMIT_WORKING_MS = "qc_ej_limit_working_ms";
        static final java.lang.String KEY_EJ_REWARD_INTERACTION_MS = "qc_ej_reward_interaction_ms";
        static final java.lang.String KEY_EJ_REWARD_NOTIFICATION_SEEN_MS = "qc_ej_reward_notification_seen_ms";
        static final java.lang.String KEY_EJ_REWARD_TOP_APP_MS = "qc_ej_reward_top_app_ms";
        static final java.lang.String KEY_EJ_TOP_APP_TIME_CHUNK_SIZE_MS = "qc_ej_top_app_time_chunk_size_ms";
        static final java.lang.String KEY_EJ_WINDOW_SIZE_MS = "qc_ej_window_size_ms";
        static final java.lang.String KEY_IN_QUOTA_BUFFER_MS = "qc_in_quota_buffer_ms";
        static final java.lang.String KEY_MAX_EXECUTION_TIME_MS = "qc_max_execution_time_ms";
        static final java.lang.String KEY_MAX_JOB_COUNT_ACTIVE = "qc_max_job_count_active";
        static final java.lang.String KEY_MAX_JOB_COUNT_EXEMPTED = "qc_max_job_count_exempted";
        static final java.lang.String KEY_MAX_JOB_COUNT_FREQUENT = "qc_max_job_count_frequent";
        static final java.lang.String KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = "qc_max_job_count_per_rate_limiting_window";
        static final java.lang.String KEY_MAX_JOB_COUNT_RARE = "qc_max_job_count_rare";
        static final java.lang.String KEY_MAX_JOB_COUNT_RESTRICTED = "qc_max_job_count_restricted";
        static final java.lang.String KEY_MAX_JOB_COUNT_WORKING = "qc_max_job_count_working";
        static final java.lang.String KEY_MAX_SESSION_COUNT_ACTIVE = "qc_max_session_count_active";
        static final java.lang.String KEY_MAX_SESSION_COUNT_EXEMPTED = "qc_max_session_count_exempted";
        static final java.lang.String KEY_MAX_SESSION_COUNT_FREQUENT = "qc_max_session_count_frequent";
        static final java.lang.String KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = "qc_max_session_count_per_rate_limiting_window";
        static final java.lang.String KEY_MAX_SESSION_COUNT_RARE = "qc_max_session_count_rare";
        static final java.lang.String KEY_MAX_SESSION_COUNT_RESTRICTED = "qc_max_session_count_restricted";
        static final java.lang.String KEY_MAX_SESSION_COUNT_WORKING = "qc_max_session_count_working";
        static final java.lang.String KEY_MIN_QUOTA_CHECK_DELAY_MS = "qc_min_quota_check_delay_ms";
        static final java.lang.String KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS = "qc_quota_bump_additional_duration_ms";
        static final java.lang.String KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT = "qc_quota_bump_additional_job_count";
        static final java.lang.String KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = "qc_quota_bump_additional_session_count";
        static final java.lang.String KEY_QUOTA_BUMP_LIMIT = "qc_quota_bump_limit";
        static final java.lang.String KEY_QUOTA_BUMP_WINDOW_SIZE_MS = "qc_quota_bump_window_size_ms";
        static final java.lang.String KEY_RATE_LIMITING_WINDOW_MS = "qc_rate_limiting_window_ms";
        static final java.lang.String KEY_TIMING_SESSION_COALESCING_DURATION_MS = "qc_timing_session_coalescing_duration_ms";
        static final java.lang.String KEY_WINDOW_SIZE_ACTIVE_MS = "qc_window_size_active_ms";
        static final java.lang.String KEY_WINDOW_SIZE_EXEMPTED_MS = "qc_window_size_exempted_ms";
        static final java.lang.String KEY_WINDOW_SIZE_FREQUENT_MS = "qc_window_size_frequent_ms";
        static final java.lang.String KEY_WINDOW_SIZE_RARE_MS = "qc_window_size_rare_ms";
        static final java.lang.String KEY_WINDOW_SIZE_RESTRICTED_MS = "qc_window_size_restricted_ms";
        static final java.lang.String KEY_WINDOW_SIZE_WORKING_MS = "qc_window_size_working_ms";
        private static final int MIN_BUCKET_JOB_COUNT = 10;
        private static final int MIN_BUCKET_SESSION_COUNT = 1;
        private static final long MIN_MAX_EXECUTION_TIME_MS = 3600000;
        private static final int MIN_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = 10;
        private static final int MIN_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = 10;
        private static final long MIN_RATE_LIMITING_WINDOW_MS = 30000;
        private static final java.lang.String QC_CONSTANT_PREFIX = "qc_";
        private boolean mShouldReevaluateConstraints = false;
        private boolean mRateLimitingConstantsUpdated = false;
        private boolean mExecutionPeriodConstantsUpdated = false;
        private boolean mEJLimitConstantsUpdated = false;
        private boolean mQuotaBumpConstantsUpdated = false;
        public long ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_WORKING_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_RARE_MS = 600000;
        public long ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = 600000;
        public long IN_QUOTA_BUFFER_MS = 30000;
        public long WINDOW_SIZE_EXEMPTED_MS = 600000;
        public long WINDOW_SIZE_ACTIVE_MS = 600000;
        public long WINDOW_SIZE_WORKING_MS = 7200000;
        public long WINDOW_SIZE_FREQUENT_MS = 28800000;
        public long WINDOW_SIZE_RARE_MS = 86400000;
        public long WINDOW_SIZE_RESTRICTED_MS = 86400000;
        public long MAX_EXECUTION_TIME_MS = 14400000;
        public int MAX_JOB_COUNT_EXEMPTED = 75;
        public int MAX_JOB_COUNT_ACTIVE = 75;
        public int MAX_JOB_COUNT_WORKING = 120;
        public int MAX_JOB_COUNT_FREQUENT = 200;
        public int MAX_JOB_COUNT_RARE = 48;
        public int MAX_JOB_COUNT_RESTRICTED = 10;
        public long RATE_LIMITING_WINDOW_MS = 60000;
        public int MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        public int MAX_SESSION_COUNT_EXEMPTED = 75;
        public int MAX_SESSION_COUNT_ACTIVE = 75;
        public int MAX_SESSION_COUNT_WORKING = 10;
        public int MAX_SESSION_COUNT_FREQUENT = 8;
        public int MAX_SESSION_COUNT_RARE = 3;
        public int MAX_SESSION_COUNT_RESTRICTED = 1;
        public int MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = 20;
        public long TIMING_SESSION_COALESCING_DURATION_MS = DEFAULT_TIMING_SESSION_COALESCING_DURATION_MS;
        public long MIN_QUOTA_CHECK_DELAY_MS = 60000;
        public long EJ_LIMIT_EXEMPTED_MS = 3600000;
        public long EJ_LIMIT_ACTIVE_MS = 1800000;
        public long EJ_LIMIT_WORKING_MS = 1800000;
        public long EJ_LIMIT_FREQUENT_MS = 600000;
        public long EJ_LIMIT_RARE_MS = 600000;
        public long EJ_LIMIT_RESTRICTED_MS = 300000;
        public long EJ_LIMIT_ADDITION_SPECIAL_MS = DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS;
        public long EJ_LIMIT_ADDITION_INSTALLER_MS = 1800000;
        public long EJ_WINDOW_SIZE_MS = 86400000;
        public long EJ_TOP_APP_TIME_CHUNK_SIZE_MS = 30000;
        public long EJ_REWARD_TOP_APP_MS = 10000;
        public long EJ_REWARD_INTERACTION_MS = DEFAULT_EJ_REWARD_INTERACTION_MS;
        public long EJ_REWARD_NOTIFICATION_SEEN_MS = 0;
        public long EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS = 180000;
        public long EJ_GRACE_PERIOD_TOP_APP_MS = 60000;
        public long QUOTA_BUMP_ADDITIONAL_DURATION_MS = 60000;
        public int QUOTA_BUMP_ADDITIONAL_JOB_COUNT = 2;
        public int QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = 1;
        public long QUOTA_BUMP_WINDOW_SIZE_MS = 28800000;
        public int QUOTA_BUMP_LIMIT = 8;

        QcConstants() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:152:0x027c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void processConstantLocked(android.provider.DeviceConfig.Properties r17, java.lang.String r18) {
            /*
                Method dump skipped, instruction units count: 1804
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.QuotaController.QcConstants.processConstantLocked(android.provider.DeviceConfig$Properties, java.lang.String):void");
        }

        private void updateExecutionPeriodConstantsLocked() {
            char c;
            if (!this.mExecutionPeriodConstantsUpdated) {
                this.mExecutionPeriodConstantsUpdated = true;
                android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[]{KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS, KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS, KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS, KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS, KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS, KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS, KEY_IN_QUOTA_BUFFER_MS, KEY_MAX_EXECUTION_TIME_MS, KEY_WINDOW_SIZE_EXEMPTED_MS, KEY_WINDOW_SIZE_ACTIVE_MS, KEY_WINDOW_SIZE_WORKING_MS, KEY_WINDOW_SIZE_FREQUENT_MS, KEY_WINDOW_SIZE_RARE_MS, KEY_WINDOW_SIZE_RESTRICTED_MS});
                this.ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS, 600000L);
                this.ALLOWED_TIME_PER_PERIOD_ACTIVE_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS, 600000L);
                this.ALLOWED_TIME_PER_PERIOD_WORKING_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS, 600000L);
                this.ALLOWED_TIME_PER_PERIOD_FREQUENT_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS, 600000L);
                this.ALLOWED_TIME_PER_PERIOD_RARE_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS, 600000L);
                this.ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS = properties.getLong(KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS, 600000L);
                this.IN_QUOTA_BUFFER_MS = properties.getLong(KEY_IN_QUOTA_BUFFER_MS, 30000L);
                this.MAX_EXECUTION_TIME_MS = properties.getLong(KEY_MAX_EXECUTION_TIME_MS, 14400000L);
                this.WINDOW_SIZE_EXEMPTED_MS = properties.getLong(KEY_WINDOW_SIZE_EXEMPTED_MS, 600000L);
                this.WINDOW_SIZE_ACTIVE_MS = properties.getLong(KEY_WINDOW_SIZE_ACTIVE_MS, 600000L);
                this.WINDOW_SIZE_WORKING_MS = properties.getLong(KEY_WINDOW_SIZE_WORKING_MS, 7200000L);
                this.WINDOW_SIZE_FREQUENT_MS = properties.getLong(KEY_WINDOW_SIZE_FREQUENT_MS, 28800000L);
                this.WINDOW_SIZE_RARE_MS = properties.getLong(KEY_WINDOW_SIZE_RARE_MS, 86400000L);
                this.WINDOW_SIZE_RESTRICTED_MS = properties.getLong(KEY_WINDOW_SIZE_RESTRICTED_MS, 86400000L);
                long newMaxExecutionTimeMs = java.lang.Math.max(3600000L, java.lang.Math.min(86400000L, this.MAX_EXECUTION_TIME_MS));
                if (com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs != newMaxExecutionTimeMs) {
                    com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs = newMaxExecutionTimeMs;
                    com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeIntoQuotaMs = com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs - com.android.server.job.controllers.QuotaController.this.mQuotaBufferMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAllowedTimeExemptedMs = java.lang.Math.min(com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs, java.lang.Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS));
                long minAllowedTimeMs = java.lang.Math.min(Long.MAX_VALUE, newAllowedTimeExemptedMs);
                if (com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[6] != newAllowedTimeExemptedMs) {
                    com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[6] = newAllowedTimeExemptedMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAllowedTimeActiveMs = java.lang.Math.min(com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs, java.lang.Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_ACTIVE_MS));
                long minAllowedTimeMs2 = java.lang.Math.min(minAllowedTimeMs, newAllowedTimeActiveMs);
                if (com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[0] != newAllowedTimeActiveMs) {
                    com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[0] = newAllowedTimeActiveMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAllowedTimeWorkingMs = java.lang.Math.min(com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs, java.lang.Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_WORKING_MS));
                long minAllowedTimeMs3 = java.lang.Math.min(minAllowedTimeMs2, newAllowedTimeWorkingMs);
                if (com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[1] != newAllowedTimeWorkingMs) {
                    com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[1] = newAllowedTimeWorkingMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAllowedTimeFrequentMs = java.lang.Math.min(com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs, java.lang.Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_FREQUENT_MS));
                long minAllowedTimeMs4 = java.lang.Math.min(minAllowedTimeMs3, newAllowedTimeFrequentMs);
                if (com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[2] != newAllowedTimeFrequentMs) {
                    com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[2] = newAllowedTimeFrequentMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAllowedTimeRareMs = java.lang.Math.min(com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs, java.lang.Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_RARE_MS));
                long minAllowedTimeMs5 = java.lang.Math.min(minAllowedTimeMs4, newAllowedTimeRareMs);
                if (com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[3] != newAllowedTimeRareMs) {
                    com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[3] = newAllowedTimeRareMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAllowedTimeRestrictedMs = java.lang.Math.min(com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs, java.lang.Math.max(60000L, this.ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS));
                long minAllowedTimeMs6 = java.lang.Math.min(minAllowedTimeMs5, newAllowedTimeRestrictedMs);
                if (com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[5] != newAllowedTimeRestrictedMs) {
                    com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[5] = newAllowedTimeRestrictedMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newQuotaBufferMs = java.lang.Math.max(0L, java.lang.Math.min(minAllowedTimeMs6, java.lang.Math.min(300000L, this.IN_QUOTA_BUFFER_MS)));
                if (com.android.server.job.controllers.QuotaController.this.mQuotaBufferMs != newQuotaBufferMs) {
                    com.android.server.job.controllers.QuotaController.this.mQuotaBufferMs = newQuotaBufferMs;
                    com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeIntoQuotaMs = com.android.server.job.controllers.QuotaController.this.mMaxExecutionTimeMs - com.android.server.job.controllers.QuotaController.this.mQuotaBufferMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newExemptedPeriodMs = java.lang.Math.max(com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[6], java.lang.Math.min(86400000L, this.WINDOW_SIZE_EXEMPTED_MS));
                if (com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[6] != newExemptedPeriodMs) {
                    com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[6] = newExemptedPeriodMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newActivePeriodMs = java.lang.Math.max(com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[0], java.lang.Math.min(86400000L, this.WINDOW_SIZE_ACTIVE_MS));
                if (com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[0] == newActivePeriodMs) {
                    c = 1;
                } else {
                    com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[0] = newActivePeriodMs;
                    c = 1;
                    this.mShouldReevaluateConstraints = true;
                }
                long newWorkingPeriodMs = java.lang.Math.max(com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[c], java.lang.Math.min(86400000L, this.WINDOW_SIZE_WORKING_MS));
                if (com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[1] != newWorkingPeriodMs) {
                    com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[1] = newWorkingPeriodMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newFrequentPeriodMs = java.lang.Math.max(com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[2], java.lang.Math.min(86400000L, this.WINDOW_SIZE_FREQUENT_MS));
                if (com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[2] != newFrequentPeriodMs) {
                    com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[2] = newFrequentPeriodMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newRarePeriodMs = java.lang.Math.max(com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[3], java.lang.Math.min(86400000L, this.WINDOW_SIZE_RARE_MS));
                if (com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[3] != newRarePeriodMs) {
                    com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[3] = newRarePeriodMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newRestrictedPeriodMs = java.lang.Math.max(com.android.server.job.controllers.QuotaController.this.mAllowedTimePerPeriodMs[5], java.lang.Math.min(com.android.server.usage.UnixCalendar.WEEK_IN_MILLIS, this.WINDOW_SIZE_RESTRICTED_MS));
                if (com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[5] != newRestrictedPeriodMs) {
                    com.android.server.job.controllers.QuotaController.this.mBucketPeriodsMs[5] = newRestrictedPeriodMs;
                    this.mShouldReevaluateConstraints = true;
                }
            }
        }

        private void updateRateLimitingConstantsLocked() {
            if (this.mRateLimitingConstantsUpdated) {
                return;
            }
            this.mRateLimitingConstantsUpdated = true;
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[]{KEY_RATE_LIMITING_WINDOW_MS, KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW, KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW});
            this.RATE_LIMITING_WINDOW_MS = properties.getLong(KEY_RATE_LIMITING_WINDOW_MS, 60000L);
            this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW = properties.getInt(KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW, 20);
            this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW = properties.getInt(KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW, 20);
            long newRateLimitingWindowMs = java.lang.Math.min(86400000L, java.lang.Math.max(30000L, this.RATE_LIMITING_WINDOW_MS));
            if (com.android.server.job.controllers.QuotaController.this.mRateLimitingWindowMs != newRateLimitingWindowMs) {
                com.android.server.job.controllers.QuotaController.this.mRateLimitingWindowMs = newRateLimitingWindowMs;
                this.mShouldReevaluateConstraints = true;
            }
            int newMaxJobCountPerRateLimitingWindow = java.lang.Math.max(10, this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW);
            if (com.android.server.job.controllers.QuotaController.this.mMaxJobCountPerRateLimitingWindow != newMaxJobCountPerRateLimitingWindow) {
                com.android.server.job.controllers.QuotaController.this.mMaxJobCountPerRateLimitingWindow = newMaxJobCountPerRateLimitingWindow;
                this.mShouldReevaluateConstraints = true;
            }
            int newMaxSessionCountPerRateLimitPeriod = java.lang.Math.max(10, this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW);
            if (com.android.server.job.controllers.QuotaController.this.mMaxSessionCountPerRateLimitingWindow != newMaxSessionCountPerRateLimitPeriod) {
                com.android.server.job.controllers.QuotaController.this.mMaxSessionCountPerRateLimitingWindow = newMaxSessionCountPerRateLimitPeriod;
                this.mShouldReevaluateConstraints = true;
            }
        }

        private void updateEJLimitConstantsLocked() {
            if (!this.mEJLimitConstantsUpdated) {
                this.mEJLimitConstantsUpdated = true;
                android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[]{KEY_EJ_LIMIT_EXEMPTED_MS, KEY_EJ_LIMIT_ACTIVE_MS, KEY_EJ_LIMIT_WORKING_MS, KEY_EJ_LIMIT_FREQUENT_MS, KEY_EJ_LIMIT_RARE_MS, KEY_EJ_LIMIT_RESTRICTED_MS, KEY_EJ_LIMIT_ADDITION_SPECIAL_MS, KEY_EJ_LIMIT_ADDITION_INSTALLER_MS, KEY_EJ_WINDOW_SIZE_MS});
                this.EJ_LIMIT_EXEMPTED_MS = properties.getLong(KEY_EJ_LIMIT_EXEMPTED_MS, 3600000L);
                this.EJ_LIMIT_ACTIVE_MS = properties.getLong(KEY_EJ_LIMIT_ACTIVE_MS, 1800000L);
                this.EJ_LIMIT_WORKING_MS = properties.getLong(KEY_EJ_LIMIT_WORKING_MS, 1800000L);
                this.EJ_LIMIT_FREQUENT_MS = properties.getLong(KEY_EJ_LIMIT_FREQUENT_MS, 600000L);
                this.EJ_LIMIT_RARE_MS = properties.getLong(KEY_EJ_LIMIT_RARE_MS, 600000L);
                this.EJ_LIMIT_RESTRICTED_MS = properties.getLong(KEY_EJ_LIMIT_RESTRICTED_MS, 300000L);
                this.EJ_LIMIT_ADDITION_INSTALLER_MS = properties.getLong(KEY_EJ_LIMIT_ADDITION_INSTALLER_MS, 1800000L);
                this.EJ_LIMIT_ADDITION_SPECIAL_MS = properties.getLong(KEY_EJ_LIMIT_ADDITION_SPECIAL_MS, DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS);
                this.EJ_WINDOW_SIZE_MS = properties.getLong(KEY_EJ_WINDOW_SIZE_MS, 86400000L);
                long newWindowSizeMs = java.lang.Math.max(3600000L, java.lang.Math.min(86400000L, this.EJ_WINDOW_SIZE_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitWindowSizeMs != newWindowSizeMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitWindowSizeMs = newWindowSizeMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newExemptLimitMs = java.lang.Math.max(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, java.lang.Math.min(newWindowSizeMs, this.EJ_LIMIT_EXEMPTED_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[6] != newExemptLimitMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[6] = newExemptLimitMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newActiveLimitMs = java.lang.Math.max(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, java.lang.Math.min(newExemptLimitMs, this.EJ_LIMIT_ACTIVE_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[0] != newActiveLimitMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[0] = newActiveLimitMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newWorkingLimitMs = java.lang.Math.max(DEFAULT_EJ_LIMIT_ADDITION_SPECIAL_MS, java.lang.Math.min(newActiveLimitMs, this.EJ_LIMIT_WORKING_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[1] != newWorkingLimitMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[1] = newWorkingLimitMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newFrequentLimitMs = java.lang.Math.max(600000L, java.lang.Math.min(newWorkingLimitMs, this.EJ_LIMIT_FREQUENT_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[2] != newFrequentLimitMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[2] = newFrequentLimitMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newRareLimitMs = java.lang.Math.max(600000L, java.lang.Math.min(newFrequentLimitMs, this.EJ_LIMIT_RARE_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[3] != newRareLimitMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[3] = newRareLimitMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newRestrictedLimitMs = java.lang.Math.max(300000L, java.lang.Math.min(newRareLimitMs, this.EJ_LIMIT_RESTRICTED_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[5] != newRestrictedLimitMs) {
                    com.android.server.job.controllers.QuotaController.this.mEJLimitsMs[5] = newRestrictedLimitMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAdditionInstallerMs = java.lang.Math.max(0L, java.lang.Math.min(newWindowSizeMs - newActiveLimitMs, this.EJ_LIMIT_ADDITION_INSTALLER_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEjLimitAdditionInstallerMs != newAdditionInstallerMs) {
                    com.android.server.job.controllers.QuotaController.this.mEjLimitAdditionInstallerMs = newAdditionInstallerMs;
                    this.mShouldReevaluateConstraints = true;
                }
                long newAdditionSpecialMs = java.lang.Math.max(0L, java.lang.Math.min(newWindowSizeMs - newActiveLimitMs, this.EJ_LIMIT_ADDITION_SPECIAL_MS));
                if (com.android.server.job.controllers.QuotaController.this.mEjLimitAdditionSpecialMs != newAdditionSpecialMs) {
                    com.android.server.job.controllers.QuotaController.this.mEjLimitAdditionSpecialMs = newAdditionSpecialMs;
                    this.mShouldReevaluateConstraints = true;
                }
            }
        }

        private void updateQuotaBumpConstantsLocked() {
            if (this.mQuotaBumpConstantsUpdated) {
                return;
            }
            this.mQuotaBumpConstantsUpdated = true;
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[]{KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS, KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT, KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT, KEY_QUOTA_BUMP_WINDOW_SIZE_MS, KEY_QUOTA_BUMP_LIMIT});
            this.QUOTA_BUMP_ADDITIONAL_DURATION_MS = properties.getLong(KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS, 60000L);
            this.QUOTA_BUMP_ADDITIONAL_JOB_COUNT = properties.getInt(KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT, 2);
            this.QUOTA_BUMP_ADDITIONAL_SESSION_COUNT = properties.getInt(KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT, 1);
            this.QUOTA_BUMP_WINDOW_SIZE_MS = properties.getLong(KEY_QUOTA_BUMP_WINDOW_SIZE_MS, 28800000L);
            this.QUOTA_BUMP_LIMIT = properties.getInt(KEY_QUOTA_BUMP_LIMIT, 8);
            long newWindowSizeMs = java.lang.Math.max(3600000L, java.lang.Math.min(86400000L, this.QUOTA_BUMP_WINDOW_SIZE_MS));
            if (com.android.server.job.controllers.QuotaController.this.mQuotaBumpWindowSizeMs != newWindowSizeMs) {
                com.android.server.job.controllers.QuotaController.this.mQuotaBumpWindowSizeMs = newWindowSizeMs;
                this.mShouldReevaluateConstraints = true;
            }
            int newLimit = java.lang.Math.max(0, this.QUOTA_BUMP_LIMIT);
            if (com.android.server.job.controllers.QuotaController.this.mQuotaBumpLimit != newLimit) {
                com.android.server.job.controllers.QuotaController.this.mQuotaBumpLimit = newLimit;
                this.mShouldReevaluateConstraints = true;
            }
            int newJobAddition = java.lang.Math.max(0, this.QUOTA_BUMP_ADDITIONAL_JOB_COUNT);
            if (com.android.server.job.controllers.QuotaController.this.mQuotaBumpAdditionalJobCount != newJobAddition) {
                com.android.server.job.controllers.QuotaController.this.mQuotaBumpAdditionalJobCount = newJobAddition;
                this.mShouldReevaluateConstraints = true;
            }
            int newSessionAddition = java.lang.Math.max(0, this.QUOTA_BUMP_ADDITIONAL_SESSION_COUNT);
            if (com.android.server.job.controllers.QuotaController.this.mQuotaBumpAdditionalSessionCount != newSessionAddition) {
                com.android.server.job.controllers.QuotaController.this.mQuotaBumpAdditionalSessionCount = newSessionAddition;
                this.mShouldReevaluateConstraints = true;
            }
            long newAdditionalDuration = java.lang.Math.max(0L, java.lang.Math.min(600000L, this.QUOTA_BUMP_ADDITIONAL_DURATION_MS));
            if (com.android.server.job.controllers.QuotaController.this.mQuotaBumpAdditionalDurationMs != newAdditionalDuration) {
                com.android.server.job.controllers.QuotaController.this.mQuotaBumpAdditionalDurationMs = newAdditionalDuration;
                this.mShouldReevaluateConstraints = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(android.util.IndentingPrintWriter pw) {
            pw.println();
            pw.println("QuotaController:");
            pw.increaseIndent();
            pw.print(KEY_ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS, java.lang.Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_EXEMPTED_MS)).println();
            pw.print(KEY_ALLOWED_TIME_PER_PERIOD_ACTIVE_MS, java.lang.Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_ACTIVE_MS)).println();
            pw.print(KEY_ALLOWED_TIME_PER_PERIOD_WORKING_MS, java.lang.Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_WORKING_MS)).println();
            pw.print(KEY_ALLOWED_TIME_PER_PERIOD_FREQUENT_MS, java.lang.Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_FREQUENT_MS)).println();
            pw.print(KEY_ALLOWED_TIME_PER_PERIOD_RARE_MS, java.lang.Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_RARE_MS)).println();
            pw.print(KEY_ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS, java.lang.Long.valueOf(this.ALLOWED_TIME_PER_PERIOD_RESTRICTED_MS)).println();
            pw.print(KEY_IN_QUOTA_BUFFER_MS, java.lang.Long.valueOf(this.IN_QUOTA_BUFFER_MS)).println();
            pw.print(KEY_WINDOW_SIZE_EXEMPTED_MS, java.lang.Long.valueOf(this.WINDOW_SIZE_EXEMPTED_MS)).println();
            pw.print(KEY_WINDOW_SIZE_ACTIVE_MS, java.lang.Long.valueOf(this.WINDOW_SIZE_ACTIVE_MS)).println();
            pw.print(KEY_WINDOW_SIZE_WORKING_MS, java.lang.Long.valueOf(this.WINDOW_SIZE_WORKING_MS)).println();
            pw.print(KEY_WINDOW_SIZE_FREQUENT_MS, java.lang.Long.valueOf(this.WINDOW_SIZE_FREQUENT_MS)).println();
            pw.print(KEY_WINDOW_SIZE_RARE_MS, java.lang.Long.valueOf(this.WINDOW_SIZE_RARE_MS)).println();
            pw.print(KEY_WINDOW_SIZE_RESTRICTED_MS, java.lang.Long.valueOf(this.WINDOW_SIZE_RESTRICTED_MS)).println();
            pw.print(KEY_MAX_EXECUTION_TIME_MS, java.lang.Long.valueOf(this.MAX_EXECUTION_TIME_MS)).println();
            pw.print(KEY_MAX_JOB_COUNT_EXEMPTED, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_EXEMPTED)).println();
            pw.print(KEY_MAX_JOB_COUNT_ACTIVE, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_ACTIVE)).println();
            pw.print(KEY_MAX_JOB_COUNT_WORKING, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_WORKING)).println();
            pw.print(KEY_MAX_JOB_COUNT_FREQUENT, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_FREQUENT)).println();
            pw.print(KEY_MAX_JOB_COUNT_RARE, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_RARE)).println();
            pw.print(KEY_MAX_JOB_COUNT_RESTRICTED, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_RESTRICTED)).println();
            pw.print(KEY_RATE_LIMITING_WINDOW_MS, java.lang.Long.valueOf(this.RATE_LIMITING_WINDOW_MS)).println();
            pw.print(KEY_MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW, java.lang.Integer.valueOf(this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW)).println();
            pw.print(KEY_MAX_SESSION_COUNT_EXEMPTED, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_EXEMPTED)).println();
            pw.print(KEY_MAX_SESSION_COUNT_ACTIVE, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_ACTIVE)).println();
            pw.print(KEY_MAX_SESSION_COUNT_WORKING, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_WORKING)).println();
            pw.print(KEY_MAX_SESSION_COUNT_FREQUENT, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_FREQUENT)).println();
            pw.print(KEY_MAX_SESSION_COUNT_RARE, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_RARE)).println();
            pw.print(KEY_MAX_SESSION_COUNT_RESTRICTED, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_RESTRICTED)).println();
            pw.print(KEY_MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW, java.lang.Integer.valueOf(this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW)).println();
            pw.print(KEY_TIMING_SESSION_COALESCING_DURATION_MS, java.lang.Long.valueOf(this.TIMING_SESSION_COALESCING_DURATION_MS)).println();
            pw.print(KEY_MIN_QUOTA_CHECK_DELAY_MS, java.lang.Long.valueOf(this.MIN_QUOTA_CHECK_DELAY_MS)).println();
            pw.print(KEY_EJ_LIMIT_EXEMPTED_MS, java.lang.Long.valueOf(this.EJ_LIMIT_EXEMPTED_MS)).println();
            pw.print(KEY_EJ_LIMIT_ACTIVE_MS, java.lang.Long.valueOf(this.EJ_LIMIT_ACTIVE_MS)).println();
            pw.print(KEY_EJ_LIMIT_WORKING_MS, java.lang.Long.valueOf(this.EJ_LIMIT_WORKING_MS)).println();
            pw.print(KEY_EJ_LIMIT_FREQUENT_MS, java.lang.Long.valueOf(this.EJ_LIMIT_FREQUENT_MS)).println();
            pw.print(KEY_EJ_LIMIT_RARE_MS, java.lang.Long.valueOf(this.EJ_LIMIT_RARE_MS)).println();
            pw.print(KEY_EJ_LIMIT_RESTRICTED_MS, java.lang.Long.valueOf(this.EJ_LIMIT_RESTRICTED_MS)).println();
            pw.print(KEY_EJ_LIMIT_ADDITION_INSTALLER_MS, java.lang.Long.valueOf(this.EJ_LIMIT_ADDITION_INSTALLER_MS)).println();
            pw.print(KEY_EJ_LIMIT_ADDITION_SPECIAL_MS, java.lang.Long.valueOf(this.EJ_LIMIT_ADDITION_SPECIAL_MS)).println();
            pw.print(KEY_EJ_WINDOW_SIZE_MS, java.lang.Long.valueOf(this.EJ_WINDOW_SIZE_MS)).println();
            pw.print(KEY_EJ_TOP_APP_TIME_CHUNK_SIZE_MS, java.lang.Long.valueOf(this.EJ_TOP_APP_TIME_CHUNK_SIZE_MS)).println();
            pw.print(KEY_EJ_REWARD_TOP_APP_MS, java.lang.Long.valueOf(this.EJ_REWARD_TOP_APP_MS)).println();
            pw.print(KEY_EJ_REWARD_INTERACTION_MS, java.lang.Long.valueOf(this.EJ_REWARD_INTERACTION_MS)).println();
            pw.print(KEY_EJ_REWARD_NOTIFICATION_SEEN_MS, java.lang.Long.valueOf(this.EJ_REWARD_NOTIFICATION_SEEN_MS)).println();
            pw.print(KEY_EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS, java.lang.Long.valueOf(this.EJ_GRACE_PERIOD_TEMP_ALLOWLIST_MS)).println();
            pw.print(KEY_EJ_GRACE_PERIOD_TOP_APP_MS, java.lang.Long.valueOf(this.EJ_GRACE_PERIOD_TOP_APP_MS)).println();
            pw.print(KEY_QUOTA_BUMP_ADDITIONAL_DURATION_MS, java.lang.Long.valueOf(this.QUOTA_BUMP_ADDITIONAL_DURATION_MS)).println();
            pw.print(KEY_QUOTA_BUMP_ADDITIONAL_JOB_COUNT, java.lang.Integer.valueOf(this.QUOTA_BUMP_ADDITIONAL_JOB_COUNT)).println();
            pw.print(KEY_QUOTA_BUMP_ADDITIONAL_SESSION_COUNT, java.lang.Integer.valueOf(this.QUOTA_BUMP_ADDITIONAL_SESSION_COUNT)).println();
            pw.print(KEY_QUOTA_BUMP_WINDOW_SIZE_MS, java.lang.Long.valueOf(this.QUOTA_BUMP_WINDOW_SIZE_MS)).println();
            pw.print(KEY_QUOTA_BUMP_LIMIT, java.lang.Integer.valueOf(this.QUOTA_BUMP_LIMIT)).println();
            pw.decreaseIndent();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(android.util.proto.ProtoOutputStream proto) {
            long qcToken = proto.start(1146756268056L);
            proto.write(1112396529666L, this.IN_QUOTA_BUFFER_MS);
            proto.write(1112396529667L, this.WINDOW_SIZE_ACTIVE_MS);
            proto.write(1112396529668L, this.WINDOW_SIZE_WORKING_MS);
            proto.write(1112396529669L, this.WINDOW_SIZE_FREQUENT_MS);
            proto.write(1112396529670L, this.WINDOW_SIZE_RARE_MS);
            proto.write(1112396529684L, this.WINDOW_SIZE_RESTRICTED_MS);
            proto.write(1112396529671L, this.MAX_EXECUTION_TIME_MS);
            proto.write(1120986464264L, this.MAX_JOB_COUNT_ACTIVE);
            proto.write(1120986464265L, this.MAX_JOB_COUNT_WORKING);
            proto.write(1120986464266L, this.MAX_JOB_COUNT_FREQUENT);
            proto.write(1120986464267L, this.MAX_JOB_COUNT_RARE);
            proto.write(1120986464277L, this.MAX_JOB_COUNT_RESTRICTED);
            proto.write(1120986464275L, this.RATE_LIMITING_WINDOW_MS);
            proto.write(1120986464268L, this.MAX_JOB_COUNT_PER_RATE_LIMITING_WINDOW);
            proto.write(1120986464269L, this.MAX_SESSION_COUNT_ACTIVE);
            proto.write(1120986464270L, this.MAX_SESSION_COUNT_WORKING);
            proto.write(1120986464271L, this.MAX_SESSION_COUNT_FREQUENT);
            proto.write(1120986464272L, this.MAX_SESSION_COUNT_RARE);
            proto.write(1120986464278L, this.MAX_SESSION_COUNT_RESTRICTED);
            proto.write(1120986464273L, this.MAX_SESSION_COUNT_PER_RATE_LIMITING_WINDOW);
            proto.write(1112396529682L, this.TIMING_SESSION_COALESCING_DURATION_MS);
            proto.write(1112396529687L, this.MIN_QUOTA_CHECK_DELAY_MS);
            proto.write(1112396529688L, this.EJ_LIMIT_ACTIVE_MS);
            proto.write(1112396529689L, this.EJ_LIMIT_WORKING_MS);
            proto.write(1112396529690L, this.EJ_LIMIT_FREQUENT_MS);
            proto.write(1112396529691L, this.EJ_LIMIT_RARE_MS);
            proto.write(1112396529692L, this.EJ_LIMIT_RESTRICTED_MS);
            proto.write(1112396529693L, this.EJ_WINDOW_SIZE_MS);
            proto.write(1112396529694L, this.EJ_TOP_APP_TIME_CHUNK_SIZE_MS);
            proto.write(1112396529695L, this.EJ_REWARD_TOP_APP_MS);
            proto.write(1112396529696L, this.EJ_REWARD_INTERACTION_MS);
            proto.write(1112396529697L, this.EJ_REWARD_NOTIFICATION_SEEN_MS);
            proto.end(qcToken);
        }
    }

    long[] getAllowedTimePerPeriodMs() {
        return this.mAllowedTimePerPeriodMs;
    }

    int[] getBucketMaxJobCounts() {
        return this.mMaxBucketJobCounts;
    }

    int[] getBucketMaxSessionCounts() {
        return this.mMaxBucketSessionCounts;
    }

    long[] getBucketWindowSizes() {
        return this.mBucketPeriodsMs;
    }

    android.util.SparseBooleanArray getForegroundUids() {
        return this.mForegroundUids;
    }

    android.os.Handler getHandler() {
        return this.mHandler;
    }

    long getEJGracePeriodTempAllowlistMs() {
        return this.mEJGracePeriodTempAllowlistMs;
    }

    long getEJGracePeriodTopAppMs() {
        return this.mEJGracePeriodTopAppMs;
    }

    long[] getEJLimitsMs() {
        return this.mEJLimitsMs;
    }

    long getEjLimitAdditionInstallerMs() {
        return this.mEjLimitAdditionInstallerMs;
    }

    long getEjLimitAdditionSpecialMs() {
        return this.mEjLimitAdditionSpecialMs;
    }

    long getEJLimitWindowSizeMs() {
        return this.mEJLimitWindowSizeMs;
    }

    long getEJRewardInteractionMs() {
        return this.mEJRewardInteractionMs;
    }

    long getEJRewardNotificationSeenMs() {
        return this.mEJRewardNotificationSeenMs;
    }

    long getEJRewardTopAppMs() {
        return this.mEJRewardTopAppMs;
    }

    java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> getEJTimingSessions(int userId, java.lang.String packageName) {
        return (java.util.List) this.mEJTimingSessions.get(userId, packageName);
    }

    long getEJTopAppTimeChunkSizeMs() {
        return this.mEJTopAppTimeChunkSizeMs;
    }

    long getInQuotaBufferMs() {
        return this.mQuotaBufferMs;
    }

    long getMaxExecutionTimeMs() {
        return this.mMaxExecutionTimeMs;
    }

    int getMaxJobCountPerRateLimitingWindow() {
        return this.mMaxJobCountPerRateLimitingWindow;
    }

    int getMaxSessionCountPerRateLimitingWindow() {
        return this.mMaxSessionCountPerRateLimitingWindow;
    }

    long getMinQuotaCheckDelayMs() {
        return this.mInQuotaAlarmQueue.getMinTimeBetweenAlarmsMs();
    }

    long getRateLimitingWindowMs() {
        return this.mRateLimitingWindowMs;
    }

    long getTimingSessionCoalescingDurationMs() {
        return this.mTimingSessionCoalescingDurationMs;
    }

    java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> getTimingSessions(int userId, java.lang.String packageName) {
        return (java.util.List) this.mTimingEvents.get(userId, packageName);
    }

    com.android.server.job.controllers.QuotaController.QcConstants getQcConstants() {
        return this.mQcConstants;
    }

    long getQuotaBumpAdditionDurationMs() {
        return this.mQuotaBumpAdditionalDurationMs;
    }

    int getQuotaBumpAdditionJobCount() {
        return this.mQuotaBumpAdditionalJobCount;
    }

    int getQuotaBumpAdditionSessionCount() {
        return this.mQuotaBumpAdditionalSessionCount;
    }

    int getQuotaBumpLimit() {
        return this.mQuotaBumpLimit;
    }

    long getQuotaBumpWindowSizeMs() {
        return this.mQuotaBumpWindowSizeMs;
    }

    @Override // com.android.server.job.controllers.StateController
    @dalvik.annotation.optimization.NeverCompile
    public void dumpControllerStateLocked(final android.util.IndentingPrintWriter pw, final java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        pw.println("Current elapsed time: " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
        pw.println();
        pw.print("Foreground UIDs: ");
        pw.println(this.mForegroundUids.toString());
        pw.println();
        pw.print("Cached top apps: ");
        pw.println(this.mTopAppCache.toString());
        pw.print("Cached top app grace period: ");
        pw.println(this.mTopAppGraceCache.toString());
        pw.print("Cached temp allowlist: ");
        pw.println(this.mTempAllowlistCache.toString());
        pw.print("Cached temp allowlist grace period: ");
        pw.println(this.mTempAllowlistGraceCache.toString());
        pw.println();
        pw.println("Special apps:");
        pw.increaseIndent();
        pw.print("System installers={");
        for (int si = 0; si < this.mSystemInstallers.size(); si++) {
            if (si > 0) {
                pw.print(", ");
            }
            pw.print(this.mSystemInstallers.keyAt(si));
            pw.print("->");
            pw.print(this.mSystemInstallers.get(si));
        }
        pw.println("}");
        pw.decreaseIndent();
        pw.println();
        this.mTrackedJobs.forEach(new java.util.function.Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$dumpControllerStateLocked$4(predicate, pw, (android.util.ArraySet) obj);
            }
        });
        pw.println();
        for (int u = 0; u < this.mPkgTimers.numMaps(); u++) {
            int userId = this.mPkgTimers.keyAt(u);
            for (int p = 0; p < this.mPkgTimers.numElementsForKey(userId); p++) {
                java.lang.String pkgName = (java.lang.String) this.mPkgTimers.keyAt(u, p);
                ((com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.valueAt(u, p)).dump(pw, predicate);
                pw.println();
                java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events = (java.util.List) this.mTimingEvents.get(userId, pkgName);
                if (events != null) {
                    pw.increaseIndent();
                    pw.println("Saved events:");
                    pw.increaseIndent();
                    for (int j = events.size() - 1; j >= 0; j--) {
                        com.android.server.job.controllers.QuotaController.TimedEvent event = events.get(j);
                        event.dump(pw);
                    }
                    pw.decreaseIndent();
                    pw.decreaseIndent();
                    pw.println();
                }
            }
        }
        pw.println();
        for (int u2 = 0; u2 < this.mEJPkgTimers.numMaps(); u2++) {
            int userId2 = this.mEJPkgTimers.keyAt(u2);
            for (int p2 = 0; p2 < this.mEJPkgTimers.numElementsForKey(userId2); p2++) {
                java.lang.String pkgName2 = (java.lang.String) this.mEJPkgTimers.keyAt(u2, p2);
                ((com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.valueAt(u2, p2)).dump(pw, predicate);
                pw.println();
                java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> sessions = (java.util.List) this.mEJTimingSessions.get(userId2, pkgName2);
                if (sessions != null) {
                    pw.increaseIndent();
                    pw.println("Saved sessions:");
                    pw.increaseIndent();
                    for (int j2 = sessions.size() - 1; j2 >= 0; j2--) {
                        com.android.server.job.controllers.QuotaController.TimedEvent session = sessions.get(j2);
                        session.dump(pw);
                    }
                    pw.decreaseIndent();
                    pw.decreaseIndent();
                    pw.println();
                }
            }
        }
        pw.println();
        this.mTopAppTrackers.forEach(new java.util.function.Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.job.controllers.QuotaController.TopAppTimer) obj).dump(pw);
            }
        });
        pw.println();
        pw.println("Cached execution stats:");
        pw.increaseIndent();
        for (int u3 = 0; u3 < this.mExecutionStatsCache.numMaps(); u3++) {
            int userId3 = this.mExecutionStatsCache.keyAt(u3);
            for (int p3 = 0; p3 < this.mExecutionStatsCache.numElementsForKey(userId3); p3++) {
                java.lang.String pkgName3 = (java.lang.String) this.mExecutionStatsCache.keyAt(u3, p3);
                com.android.server.job.controllers.QuotaController.ExecutionStats[] stats = (com.android.server.job.controllers.QuotaController.ExecutionStats[]) this.mExecutionStatsCache.valueAt(u3, p3);
                pw.println(packageToString(userId3, pkgName3));
                pw.increaseIndent();
                for (int i = 0; i < stats.length; i++) {
                    com.android.server.job.controllers.QuotaController.ExecutionStats executionStats = stats[i];
                    if (executionStats != null) {
                        pw.print(com.android.server.job.controllers.JobStatus.bucketName(i));
                        pw.print(": ");
                        pw.println(executionStats);
                    }
                }
                pw.decreaseIndent();
            }
        }
        pw.decreaseIndent();
        pw.println();
        pw.println("EJ debits:");
        pw.increaseIndent();
        for (int u4 = 0; u4 < this.mEJStats.numMaps(); u4++) {
            int userId4 = this.mEJStats.keyAt(u4);
            for (int p4 = 0; p4 < this.mEJStats.numElementsForKey(userId4); p4++) {
                java.lang.String pkgName4 = (java.lang.String) this.mEJStats.keyAt(u4, p4);
                com.android.server.job.controllers.QuotaController.ShrinkableDebits debits = (com.android.server.job.controllers.QuotaController.ShrinkableDebits) this.mEJStats.valueAt(u4, p4);
                pw.print(packageToString(userId4, pkgName4));
                pw.print(": ");
                debits.dumpLocked(pw);
            }
        }
        pw.decreaseIndent();
        pw.println();
        this.mInQuotaAlarmQueue.dump(pw);
        pw.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$4(java.util.function.Predicate predicate, android.util.IndentingPrintWriter pw, android.util.ArraySet jobs) {
        for (int j = 0; j < jobs.size(); j++) {
            com.android.server.job.controllers.JobStatus js = (com.android.server.job.controllers.JobStatus) jobs.valueAt(j);
            if (predicate.test(js)) {
                pw.print("#");
                js.printUniqueId(pw);
                pw.print(" from ");
                android.os.UserHandle.formatUid(pw, js.getSourceUid());
                if (this.mTopStartedJobs.contains(js)) {
                    pw.print(" (TOP)");
                }
                pw.println();
                pw.increaseIndent();
                pw.print(com.android.server.job.controllers.JobStatus.bucketName(js.getEffectiveStandbyBucket()));
                pw.print(", ");
                if (js.shouldTreatAsExpeditedJob()) {
                    pw.print("within EJ quota");
                } else if (js.startedAsExpeditedJob) {
                    pw.print("out of EJ quota");
                } else if (js.isConstraintSatisfied(16777216)) {
                    pw.print("within regular quota");
                } else {
                    pw.print("not within quota");
                }
                pw.print(", ");
                if (js.shouldTreatAsExpeditedJob()) {
                    pw.print(getRemainingEJExecutionTimeLocked(js.getSourceUserId(), js.getSourcePackageName()));
                    pw.print("ms remaining in EJ quota");
                } else if (js.startedAsExpeditedJob) {
                    pw.print("should be stopped after min execution time");
                } else {
                    pw.print(getRemainingExecutionTimeLocked(js));
                    pw.print("ms remaining in quota");
                }
                pw.println();
                pw.decreaseIndent();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token;
        com.android.server.job.controllers.QuotaController.Timer ejTimer;
        long mToken;
        int userId;
        int p;
        java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events;
        final java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate2 = predicate;
        long token2 = proto.start(fieldId);
        long mToken2 = proto.start(1146756268041L);
        proto.write(1133871366145L, this.mService.isBatteryCharging());
        proto.write(1112396529670L, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
        for (int i = 0; i < this.mForegroundUids.size(); i++) {
            proto.write(2220498092035L, this.mForegroundUids.keyAt(i));
        }
        this.mTrackedJobs.forEach(new java.util.function.Consumer() { // from class: com.android.server.job.controllers.QuotaController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$dumpControllerStateLocked$6(predicate2, proto, (android.util.ArraySet) obj);
            }
        });
        int u = 0;
        while (u < this.mPkgTimers.numMaps()) {
            int userId2 = this.mPkgTimers.keyAt(u);
            int p2 = 0;
            while (p2 < this.mPkgTimers.numElementsForKey(userId2)) {
                java.lang.String pkgName = (java.lang.String) this.mPkgTimers.keyAt(u, p2);
                long psToken = proto.start(2246267895813L);
                ((com.android.server.job.controllers.QuotaController.Timer) this.mPkgTimers.valueAt(u, p2)).dump(proto, 1146756268034L, predicate2);
                com.android.server.job.controllers.QuotaController.Timer ejTimer2 = (com.android.server.job.controllers.QuotaController.Timer) this.mEJPkgTimers.get(userId2, pkgName);
                if (ejTimer2 == null) {
                    token = token2;
                } else {
                    token = token2;
                    ejTimer2.dump(proto, 1146756268038L, predicate2);
                }
                java.util.List<com.android.server.job.controllers.QuotaController.TimedEvent> events2 = (java.util.List) this.mTimingEvents.get(userId2, pkgName);
                if (events2 != null) {
                    int j = events2.size() - 1;
                    while (j >= 0) {
                        com.android.server.job.controllers.QuotaController.TimedEvent event = events2.get(j);
                        if (!(event instanceof com.android.server.job.controllers.QuotaController.TimingSession)) {
                            events = events2;
                        } else {
                            com.android.server.job.controllers.QuotaController.TimingSession session = (com.android.server.job.controllers.QuotaController.TimingSession) event;
                            events = events2;
                            session.dump(proto, 2246267895811L);
                        }
                        j--;
                        events2 = events;
                    }
                }
                com.android.server.job.controllers.QuotaController.ExecutionStats[] stats = (com.android.server.job.controllers.QuotaController.ExecutionStats[]) this.mExecutionStatsCache.get(userId2, pkgName);
                if (stats != null) {
                    int bucketIndex = 0;
                    while (bucketIndex < stats.length) {
                        com.android.server.job.controllers.QuotaController.ExecutionStats es = stats[bucketIndex];
                        if (es == null) {
                            mToken = mToken2;
                            userId = userId2;
                            ejTimer = ejTimer2;
                            p = p2;
                        } else {
                            int userId3 = userId2;
                            ejTimer = ejTimer2;
                            long esToken = proto.start(2246267895812L);
                            mToken = mToken2;
                            proto.write(1159641169921L, bucketIndex);
                            userId = userId3;
                            p = p2;
                            proto.write(1112396529666L, es.expirationTimeElapsed);
                            proto.write(1112396529667L, es.windowSizeMs);
                            proto.write(1120986464270L, es.jobCountLimit);
                            proto.write(1120986464271L, es.sessionCountLimit);
                            proto.write(1112396529668L, es.executionTimeInWindowMs);
                            proto.write(1120986464261L, es.bgJobCountInWindow);
                            proto.write(1112396529670L, es.executionTimeInMaxPeriodMs);
                            proto.write(1120986464263L, es.bgJobCountInMaxPeriod);
                            proto.write(1120986464267L, es.sessionCountInWindow);
                            proto.write(1112396529672L, es.inQuotaTimeElapsed);
                            proto.write(1112396529673L, es.jobRateLimitExpirationTimeElapsed);
                            proto.write(1120986464266L, es.jobCountInRateLimitingWindow);
                            proto.write(1112396529676L, es.sessionRateLimitExpirationTimeElapsed);
                            proto.write(1120986464269L, es.sessionCountInRateLimitingWindow);
                            proto.end(esToken);
                        }
                        bucketIndex++;
                        ejTimer2 = ejTimer;
                        mToken2 = mToken;
                        p2 = p;
                        userId2 = userId;
                    }
                }
                proto.end(psToken);
                p2++;
                predicate2 = predicate;
                token2 = token;
                mToken2 = mToken2;
                userId2 = userId2;
            }
            u++;
            predicate2 = predicate;
        }
        proto.end(mToken2);
        proto.end(token2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$6(java.util.function.Predicate predicate, android.util.proto.ProtoOutputStream proto, android.util.ArraySet jobs) {
        for (int j = 0; j < jobs.size(); j++) {
            com.android.server.job.controllers.JobStatus js = (com.android.server.job.controllers.JobStatus) jobs.valueAt(j);
            if (predicate.test(js)) {
                long jsToken = proto.start(2246267895812L);
                js.writeToShortProto(proto, 1146756268033L);
                proto.write(1120986464258L, js.getSourceUid());
                proto.write(1159641169923L, js.getEffectiveStandbyBucket());
                proto.write(1133871366148L, this.mTopStartedJobs.contains(js));
                proto.write(1133871366149L, js.isConstraintSatisfied(16777216));
                proto.write(1112396529670L, getRemainingExecutionTimeLocked(js));
                proto.write(1133871366151L, js.isRequestedExpeditedJob());
                proto.write(1133871366152L, js.isExpeditedQuotaApproved());
                proto.end(jsToken);
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        this.mQcConstants.dump(pw);
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(android.util.proto.ProtoOutputStream proto) {
        this.mQcConstants.dump(proto);
    }
}
