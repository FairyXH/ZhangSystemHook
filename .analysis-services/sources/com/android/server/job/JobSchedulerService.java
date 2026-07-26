package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public class JobSchedulerService extends com.android.server.SystemService implements com.android.server.job.StateChangedListener, com.android.server.job.JobCompletedListener {
    public static final int ACTIVE_INDEX = 0;
    public static boolean DEBUG = android.util.Log.isLoggable("JobScheduler", 3);
    public static final boolean DEBUG_STANDBY;
    private static boolean DEBUG_USAGE = false;
    public static final int EXEMPTED_INDEX = 6;
    public static final int FREQUENT_INDEX = 2;
    public static final long MAX_ALLOWED_PERIOD_MS = 31536000000L;
    private static final int MAX_JOBS_PER_APP = 150;
    static final int MSG_CHECK_CHANGED_JOB_LIST = 8;
    static final int MSG_CHECK_INDIVIDUAL_JOB = 0;
    static final int MSG_CHECK_JOB = 1;
    static final int MSG_CHECK_JOB_GREEDY = 3;
    static final int MSG_CHECK_MEDIA_EXEMPTION = 9;
    static final int MSG_INFORM_OBSERVERS_OF_USER_VISIBLE_JOB_CHANGE = 11;
    static final int MSG_INFORM_OBSERVER_OF_ALL_USER_VISIBLE_JOBS = 10;
    static final int MSG_STOP_JOB = 2;
    static final int MSG_UID_ACTIVE = 6;
    static final int MSG_UID_GONE = 5;
    static final int MSG_UID_IDLE = 7;
    static final int MSG_UID_STATE_CHANGED = 4;
    public static final int NEVER_INDEX = 4;
    private static final int NUM_COMPLETED_JOB_HISTORY = 20;
    private static final long PERIODIC_JOB_WINDOW_BUFFER = 1800000;
    private static final java.lang.String QUOTA_TRACKER_ANR_TAG = "anr";
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_ANR;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_DISABLED;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_TIMEOUT_REG;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL;
    private static final com.android.server.utils.quota.Category QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ;
    private static final java.lang.String QUOTA_TRACKER_SCHEDULE_LOGGED = ".schedulePersisted out-of-quota logged";
    private static final java.lang.String QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG = ".schedulePersisted()";
    private static final java.lang.String QUOTA_TRACKER_TIMEOUT_EJ_TAG = "timeout-ej";
    private static final java.lang.String QUOTA_TRACKER_TIMEOUT_REG_TAG = "timeout-reg";
    private static final java.lang.String QUOTA_TRACKER_TIMEOUT_TOTAL_TAG = "timeout-total";
    private static final java.lang.String QUOTA_TRACKER_TIMEOUT_UIJ_TAG = "timeout-uij";
    public static final int RARE_INDEX = 3;
    private static final long REQUIRE_NETWORK_CONSTRAINT_FOR_NETWORK_JOB_WORK_ITEMS = 241104082;
    static final long REQUIRE_NETWORK_PERMISSIONS_FOR_CONNECTIVITY_JOBS = 271850009;
    public static final int RESTRICTED_INDEX = 5;
    public static final java.lang.String TAG = "JobScheduler";
    public static final long THROW_ON_UNSUPPORTED_BIAS_USAGE = 300477393;
    public static final java.lang.String TRACE_TRACK_NAME = "JobScheduler";
    public static final int WORKING_INDEX = 1;
    public static java.time.Clock sElapsedRealtimeClock;
    private static final com.android.modules.expresslog.Histogram sEnqueuedJwiHighWaterMarkLogger;
    private static final com.android.modules.expresslog.Histogram sInitialJobEstimatedNetworkDownloadKBLogger;
    private static final com.android.modules.expresslog.Histogram sInitialJobEstimatedNetworkUploadKBLogger;
    private static final com.android.modules.expresslog.Histogram sInitialJwiEstimatedNetworkDownloadKBLogger;
    private static final com.android.modules.expresslog.Histogram sInitialJwiEstimatedNetworkUploadKBLogger;
    private static final com.android.modules.expresslog.Histogram sJobMinimumChunkKBLogger;
    private static final com.android.modules.expresslog.Histogram sJwiMinimumChunkKBLogger;
    public static java.time.Clock sSystemClock;
    public static java.time.Clock sUptimeMillisClock;
    public static android.app.usage.UsageStatsManagerInternal sUsageStatsManagerInternal;
    android.app.ActivityManagerInternal mActivityManagerInternal;
    private final com.android.server.usage.AppStandbyInternal mAppStandbyInternal;
    com.android.server.AppStateTrackerImpl mAppStateTracker;
    private final android.util.SparseBooleanArray mBackingUpUids;
    final com.android.server.job.JobSchedulerService.BatteryStateTracker mBatteryStateTracker;
    private final android.os.BatteryStatsInternal mBatteryStatsInternal;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final java.util.function.Consumer<com.android.server.job.controllers.JobStatus> mCancelJobDueToUserRemovalConsumer;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mChangedJobList;
    private final android.util.SparseArray<java.lang.String> mCloudMediaProviderPackages;
    final com.android.server.job.JobConcurrencyManager mConcurrencyManager;
    private final com.android.server.job.controllers.ConnectivityController mConnectivityController;
    final com.android.server.job.JobSchedulerService.Constants mConstants;
    final com.android.server.job.JobSchedulerService.ConstantsObserver mConstantsObserver;
    final java.util.List<com.android.server.job.controllers.StateController> mControllers;
    final android.util.ArrayMap<java.lang.String, java.lang.Boolean> mDebuggableApps;
    private final com.android.server.job.controllers.DeviceIdleJobsController mDeviceIdleJobsController;
    private final com.android.server.job.controllers.FlexibilityController mFlexibilityController;
    final com.android.server.job.JobSchedulerService.JobHandler mHandler;
    private final java.util.function.Predicate<java.lang.Integer> mIsUidActivePredicate;
    final com.android.server.job.JobPackageTracker mJobPackageTracker;
    final java.util.List<com.android.server.job.restrictions.JobRestriction> mJobRestrictions;
    private com.android.server.job.IJobSchedulerServiceExt mJobSchedulerServiceExt;
    private com.android.server.job.JobSchedulerService.JobSchedulerServiceWrapper mJobSchedulerServiceWrapper;
    final com.android.server.job.JobSchedulerService.JobSchedulerStub mJobSchedulerStub;
    private final java.util.concurrent.CountDownLatch mJobStoreLoadedLatch;
    private final java.lang.Runnable mJobTimeUpdater;
    final com.android.server.job.JobStore mJobs;
    private int mLastCancelledJobIndex;
    private final long[] mLastCancelledJobTimeElapsed;
    private final com.android.server.job.controllers.JobStatus[] mLastCancelledJobs;
    private int mLastCompletedJobIndex;
    private final long[] mLastCompletedJobTimeElapsed;
    private final com.android.server.job.controllers.JobStatus[] mLastCompletedJobs;
    com.android.server.DeviceIdleInternal mLocalDeviceIdleController;
    android.content.pm.PackageManagerInternal mLocalPM;
    final java.lang.Object mLock;
    private final com.android.server.job.JobSchedulerService.MaybeReadyJobQueueFunctor mMaybeQueueFunctor;
    private final com.android.server.job.PendingJobQueue mPendingJobQueue;
    private final android.util.SparseArrayMap<java.lang.String, android.util.SparseIntArray> mPendingJobReasonCache;
    private final android.util.SparseArray<android.util.SparseArrayMap<java.lang.String, java.lang.Boolean>> mPermissionCache;
    private final com.android.server.job.controllers.PrefetchController mPrefetchController;
    private final com.android.server.job.controllers.QuotaController mQuotaController;
    private final com.android.server.utils.quota.CountQuotaTracker mQuotaTracker;
    private final com.android.server.job.JobSchedulerService.ReadyJobQueueFunctor mReadyQueueFunctor;
    boolean mReadyToRock;
    boolean mReportedActive;
    private final java.util.List<com.android.server.job.controllers.RestrictingController> mRestrictiveControllers;
    final com.android.server.job.JobSchedulerService.StandbyTracker mStandbyTracker;
    private final java.util.concurrent.CountDownLatch mStartControllerTrackingLatch;
    int[] mStartedUsers;
    private final com.android.server.job.controllers.StorageController mStorageController;
    private final android.content.BroadcastReceiver mTimeSetReceiver;
    final android.util.SparseIntArray mUidBiasOverride;
    private final android.util.SparseIntArray mUidCapabilities;
    private final android.app.IUidObserver mUidObserver;
    private final android.util.SparseIntArray mUidProcStates;
    private final android.util.SparseSetArray<java.lang.String> mUidToPackageCache;
    private final android.os.RemoteCallbackList<android.app.job.IUserVisibleJobObserver> mUserVisibleJobObservers;

    static {
        DEBUG_STANDBY = DEBUG;
        DEBUG_USAGE = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
        sSystemClock = java.time.Clock.systemUTC();
        sUptimeMillisClock = new com.android.server.job.JobSchedulerService.MySimpleClock(java.time.ZoneOffset.UTC) { // from class: com.android.server.job.JobSchedulerService.1
            @Override // com.android.server.job.JobSchedulerService.MySimpleClock, java.time.Clock, java.time.InstantSource
            public long millis() {
                return android.os.SystemClock.uptimeMillis();
            }
        };
        sElapsedRealtimeClock = new com.android.server.job.JobSchedulerService.MySimpleClock(java.time.ZoneOffset.UTC) { // from class: com.android.server.job.JobSchedulerService.2
            @Override // com.android.server.job.JobSchedulerService.MySimpleClock, java.time.Clock, java.time.InstantSource
            public long millis() {
                return android.os.SystemClock.elapsedRealtime();
            }
        };
        QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED = new com.android.server.utils.quota.Category(QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG);
        QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED = new com.android.server.utils.quota.Category(QUOTA_TRACKER_SCHEDULE_LOGGED);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ = new com.android.server.utils.quota.Category(QUOTA_TRACKER_TIMEOUT_UIJ_TAG);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ = new com.android.server.utils.quota.Category(QUOTA_TRACKER_TIMEOUT_EJ_TAG);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_REG = new com.android.server.utils.quota.Category(QUOTA_TRACKER_TIMEOUT_REG_TAG);
        QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL = new com.android.server.utils.quota.Category(QUOTA_TRACKER_TIMEOUT_TOTAL_TAG);
        QUOTA_TRACKER_CATEGORY_ANR = new com.android.server.utils.quota.Category(QUOTA_TRACKER_ANR_TAG);
        QUOTA_TRACKER_CATEGORY_DISABLED = new com.android.server.utils.quota.Category(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        sEnqueuedJwiHighWaterMarkLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_w_uid_enqueued_work_items_high_water_mark", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(25, 0, 5.0f, 1.4f));
        sInitialJobEstimatedNetworkDownloadKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_initial_job_estimated_network_download_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sInitialJwiEstimatedNetworkDownloadKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_initial_jwi_estimated_network_download_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sInitialJobEstimatedNetworkUploadKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_initial_job_estimated_network_upload_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sInitialJwiEstimatedNetworkUploadKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_initial_jwi_estimated_network_upload_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(50, 0, 32.0f, 1.31f));
        sJobMinimumChunkKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_w_uid_job_minimum_chunk_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(25, 0, 5.0f, 1.76f));
        sJwiMinimumChunkKBLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_w_uid_jwi_minimum_chunk_kilobytes", new com.android.modules.expresslog.Histogram.ScaledRangeOptions(25, 0, 5.0f, 1.76f));
    }

    private static abstract class MySimpleClock extends java.time.Clock {
        private final java.time.ZoneId mZoneId;

        @Override // java.time.Clock, java.time.InstantSource
        public abstract long millis();

        MySimpleClock(java.time.ZoneId zoneId) {
            this.mZoneId = zoneId;
        }

        @Override // java.time.Clock
        public java.time.ZoneId getZone() {
            return this.mZoneId;
        }

        @Override // java.time.Clock, java.time.InstantSource
        public java.time.Clock withZone(java.time.ZoneId zone) {
            return new com.android.server.job.JobSchedulerService.MySimpleClock(zone) { // from class: com.android.server.job.JobSchedulerService.MySimpleClock.1
                @Override // com.android.server.job.JobSchedulerService.MySimpleClock, java.time.Clock, java.time.InstantSource
                public long millis() {
                    return com.android.server.job.JobSchedulerService.MySimpleClock.this.millis();
                }
            };
        }

        @Override // java.time.Clock, java.time.InstantSource
        public java.time.Instant instant() {
            return java.time.Instant.ofEpochMilli(millis());
        }
    }

    private class ConstantsObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private boolean mCacheConfigChanges;
        private android.provider.DeviceConfig.Properties mLastPropertiesPulled;

        private ConstantsObserver() {
            this.mCacheConfigChanges = false;
        }

        public java.lang.String getValueLocked(java.lang.String key) {
            if (this.mLastPropertiesPulled == null) {
                return null;
            }
            return this.mLastPropertiesPulled.getString(key, (java.lang.String) null);
        }

        public void setCacheConfigChangesLocked(boolean enabled) {
            if (enabled && !this.mCacheConfigChanges) {
                this.mLastPropertiesPulled = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[0]);
            } else {
                this.mLastPropertiesPulled = null;
            }
            this.mCacheConfigChanges = enabled;
        }

        public void start() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("jobscheduler", com.android.server.AppSchedulingModuleThread.getExecutor(), this);
            onPropertiesChanged(android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[0]));
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0082  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties r13) {
            /*
                Method dump skipped, instruction units count: 1062
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerService.ConstantsObserver.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
        }
    }

    void updateQuotaTracker() {
        this.mQuotaTracker.setEnabled(this.mConstants.ENABLE_API_QUOTAS || this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED, this.mConstants.API_QUOTA_SCHEDULE_COUNT, this.mConstants.API_QUOTA_SCHEDULE_WINDOW_MS);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_TIMEOUT_REG, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT, this.mConstants.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_ANR, this.mConstants.EXECUTION_SAFEGUARDS_UDC_ANR_COUNT, this.mConstants.EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS);
    }

    public static class Constants {
        private static final int DEFAULT_API_QUOTA_SCHEDULE_COUNT = 250;
        private static final boolean DEFAULT_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = false;
        private static final boolean DEFAULT_API_QUOTA_SCHEDULE_THROW_EXCEPTION = true;
        private static final long DEFAULT_API_QUOTA_SCHEDULE_WINDOW_MS = 60000;
        private static final float DEFAULT_CONN_CONGESTION_DELAY_FRAC = 0.5f;
        private static final float DEFAULT_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = 0.5f;
        private static final long DEFAULT_CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS = 1860000;
        private static final float DEFAULT_CONN_PREFETCH_RELAX_FRAC = 0.5f;
        private static final long DEFAULT_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = 60000;
        private static final boolean DEFAULT_CONN_USE_CELL_SIGNAL_STRENGTH = true;
        private static final boolean DEFAULT_ENABLE_API_QUOTAS = true;
        private static final boolean DEFAULT_ENABLE_EXECUTION_SAFEGUARDS_UDC = true;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = 3;
        private static final long DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = 21600000;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = 5;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = 3;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = 10;
        private static final int DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = 2;
        private static final long DEFAULT_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = 86400000;
        private static final float DEFAULT_HEAVY_USE_FACTOR = 0.9f;
        private static final long DEFAULT_MAX_CPU_ONLY_JOB_BATCH_DELAY_MS = 1860000;
        private static final long DEFAULT_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = 1860000;
        static final int DEFAULT_MAX_NUM_PERSISTED_JOB_WORK_ITEMS = 100000;
        private static final long DEFAULT_MIN_EXP_BACKOFF_TIME_MS = 10000;
        private static final long DEFAULT_MIN_LINEAR_BACKOFF_TIME_MS = 10000;
        private static final float DEFAULT_MODERATE_USE_FACTOR = 0.5f;
        static final boolean DEFAULT_PERSIST_IN_SPLIT_FILES = true;
        private static final long DEFAULT_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = 3600000;
        public static final long DEFAULT_RUNTIME_CUMULATIVE_UI_LIMIT_MS = 86400000;
        public static final long DEFAULT_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = 1800000;
        public static final long DEFAULT_RUNTIME_MIN_EJ_GUARANTEE_MS = 180000;
        public static final long DEFAULT_RUNTIME_MIN_GUARANTEE_MS = 600000;
        public static final float DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = 1.35f;
        public static final long DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS;
        public static final long DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS;
        public static final long DEFAULT_RUNTIME_UI_LIMIT_MS;
        public static final boolean DEFAULT_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = false;
        private static final int DEFAULT_SYSTEM_STOP_TO_FAILURE_RATIO = 3;
        private static final java.lang.String KEY_API_QUOTA_SCHEDULE_COUNT = "aq_schedule_count";
        private static final java.lang.String KEY_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = "aq_schedule_return_failure";
        private static final java.lang.String KEY_API_QUOTA_SCHEDULE_THROW_EXCEPTION = "aq_schedule_throw_exception";
        private static final java.lang.String KEY_API_QUOTA_SCHEDULE_WINDOW_MS = "aq_schedule_window_ms";
        private static final java.lang.String KEY_CONN_CONGESTION_DELAY_FRAC = "conn_congestion_delay_frac";
        private static final java.lang.String KEY_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = "conn_low_signal_strength_relax_frac";
        private static final java.lang.String KEY_CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS = "conn_max_connectivity_job_batch_delay_ms";
        private static final java.lang.String KEY_CONN_PREFETCH_RELAX_FRAC = "conn_prefetch_relax_frac";
        private static final java.lang.String KEY_CONN_TRANSPORT_BATCH_THRESHOLD = "conn_transport_batch_threshold";
        private static final java.lang.String KEY_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = "conn_update_all_jobs_min_interval_ms";
        private static final java.lang.String KEY_CONN_USE_CELL_SIGNAL_STRENGTH = "conn_use_cell_signal_strength";
        private static final java.lang.String KEY_ENABLE_API_QUOTAS = "enable_api_quotas";
        private static final java.lang.String KEY_ENABLE_EXECUTION_SAFEGUARDS_UDC = "enable_execution_safeguards_udc";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = "es_u_anr_count";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = "es_u_anr_window_ms";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = "es_u_timeout_ej_count";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = "es_u_timeout_reg_count";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = "es_u_timeout_total_count";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = "es_u_timeout_uij_count";
        private static final java.lang.String KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = "es_u_timeout_window_ms";
        private static final java.lang.String KEY_HEAVY_USE_FACTOR = "heavy_use_factor";
        private static final java.lang.String KEY_MAX_CPU_ONLY_JOB_BATCH_DELAY_MS = "max_cpu_only_job_batch_delay_ms";
        private static final java.lang.String KEY_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = "max_non_active_job_batch_delay_ms";
        private static final java.lang.String KEY_MAX_NUM_PERSISTED_JOB_WORK_ITEMS = "max_num_persisted_job_work_items";
        private static final java.lang.String KEY_MIN_EXP_BACKOFF_TIME_MS = "min_exp_backoff_time_ms";
        private static final java.lang.String KEY_MIN_LINEAR_BACKOFF_TIME_MS = "min_linear_backoff_time_ms";
        private static final java.lang.String KEY_MIN_READY_CPU_ONLY_JOBS_COUNT = "min_ready_cpu_only_jobs_count";
        private static final java.lang.String KEY_MIN_READY_NON_ACTIVE_JOBS_COUNT = "min_ready_non_active_jobs_count";
        private static final java.lang.String KEY_MODERATE_USE_FACTOR = "moderate_use_factor";
        private static final java.lang.String KEY_PERSIST_IN_SPLIT_FILES = "persist_in_split_files";
        private static final java.lang.String KEY_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = "prefetch_force_batch_relax_threshold_ms";
        private static final java.lang.String KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS = "runtime_cumulative_ui_limit_ms";
        private static final java.lang.String KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = "runtime_free_quota_max_limit_ms";
        private static final java.lang.String KEY_RUNTIME_MIN_EJ_GUARANTEE_MS = "runtime_min_ej_guarantee_ms";
        private static final java.lang.String KEY_RUNTIME_MIN_GUARANTEE_MS = "runtime_min_guarantee_ms";
        private static final java.lang.String KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = "runtime_min_ui_data_transfer_guarantee_buffer_factor";
        private static final java.lang.String KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = "runtime_min_ui_data_transfer_guarantee_ms";
        private static final java.lang.String KEY_RUNTIME_MIN_UI_GUARANTEE_MS = "runtime_min_ui_guarantee_ms";
        private static final java.lang.String KEY_RUNTIME_UI_LIMIT_MS = "runtime_ui_limit_ms";
        private static final java.lang.String KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = "runtime_use_data_estimates_for_limits";
        private static final java.lang.String KEY_SYSTEM_STOP_TO_FAILURE_RATIO = "system_stop_to_failure_ratio";
        private static final int DEFAULT_MIN_READY_CPU_ONLY_JOBS_COUNT = java.lang.Math.min(3, com.android.server.job.JobConcurrencyManager.DEFAULT_CONCURRENCY_LIMIT / 3);
        private static final int DEFAULT_MIN_READY_NON_ACTIVE_JOBS_COUNT = java.lang.Math.min(5, com.android.server.job.JobConcurrencyManager.DEFAULT_CONCURRENCY_LIMIT / 3);
        private static final android.util.SparseIntArray DEFAULT_CONN_TRANSPORT_BATCH_THRESHOLD = new android.util.SparseIntArray();
        int MIN_READY_CPU_ONLY_JOBS_COUNT = DEFAULT_MIN_READY_CPU_ONLY_JOBS_COUNT;
        int MIN_READY_NON_ACTIVE_JOBS_COUNT = DEFAULT_MIN_READY_NON_ACTIVE_JOBS_COUNT;
        long MAX_CPU_ONLY_JOB_BATCH_DELAY_MS = 1860000;
        long MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = 1860000;
        float HEAVY_USE_FACTOR = DEFAULT_HEAVY_USE_FACTOR;
        float MODERATE_USE_FACTOR = 0.5f;
        long MIN_LINEAR_BACKOFF_TIME_MS = 10000;
        long MIN_EXP_BACKOFF_TIME_MS = 10000;
        int SYSTEM_STOP_TO_FAILURE_RATIO = 3;
        public float CONN_CONGESTION_DELAY_FRAC = 0.5f;
        public float CONN_PREFETCH_RELAX_FRAC = 0.5f;
        public boolean CONN_USE_CELL_SIGNAL_STRENGTH = true;
        public long CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = 60000;
        public float CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = 0.5f;
        public android.util.SparseIntArray CONN_TRANSPORT_BATCH_THRESHOLD = new android.util.SparseIntArray();
        public long CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS = 1860000;
        public long PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = 3600000;
        public boolean ENABLE_API_QUOTAS = true;
        public int API_QUOTA_SCHEDULE_COUNT = 250;
        public long API_QUOTA_SCHEDULE_WINDOW_MS = 60000;
        public boolean API_QUOTA_SCHEDULE_THROW_EXCEPTION = true;
        public boolean API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = false;
        public boolean ENABLE_EXECUTION_SAFEGUARDS_UDC = true;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = 2;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = 5;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = 3;
        public int EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = 10;
        public long EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = 86400000;
        public int EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = 3;
        public long EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS;
        public long RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = 1800000;
        public long RUNTIME_MIN_GUARANTEE_MS = 600000;
        public long RUNTIME_MIN_EJ_GUARANTEE_MS = 180000;
        public long RUNTIME_MIN_UI_GUARANTEE_MS = DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS;
        public long RUNTIME_UI_LIMIT_MS = DEFAULT_RUNTIME_UI_LIMIT_MS;
        public float RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = 1.35f;
        public long RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS;
        public long RUNTIME_CUMULATIVE_UI_LIMIT_MS = 86400000;
        public boolean RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = false;
        public boolean PERSIST_IN_SPLIT_FILES = true;
        public int MAX_NUM_PERSISTED_JOB_WORK_ITEMS = 100000;

        static {
            DEFAULT_CONN_TRANSPORT_BATCH_THRESHOLD.put(0, java.lang.Math.min(3, com.android.server.job.JobConcurrencyManager.DEFAULT_CONCURRENCY_LIMIT / 3));
            DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS = java.lang.Math.max(DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS, 600000L);
            DEFAULT_RUNTIME_UI_LIMIT_MS = java.lang.Math.max(43200000L, 1800000L);
            DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = java.lang.Math.max(600000L, DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS);
        }

        public Constants() {
            copyTransportBatchThresholdDefaults();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateBatchingConstantsLocked() {
            this.MIN_READY_CPU_ONLY_JOBS_COUNT = java.lang.Math.max(0, java.lang.Math.min(com.android.server.job.JobConcurrencyManager.DEFAULT_CONCURRENCY_LIMIT / 3, android.provider.DeviceConfig.getInt("jobscheduler", KEY_MIN_READY_CPU_ONLY_JOBS_COUNT, DEFAULT_MIN_READY_CPU_ONLY_JOBS_COUNT)));
            this.MIN_READY_NON_ACTIVE_JOBS_COUNT = java.lang.Math.max(0, java.lang.Math.min(com.android.server.job.JobConcurrencyManager.DEFAULT_CONCURRENCY_LIMIT / 3, android.provider.DeviceConfig.getInt("jobscheduler", KEY_MIN_READY_NON_ACTIVE_JOBS_COUNT, DEFAULT_MIN_READY_NON_ACTIVE_JOBS_COUNT)));
            this.MAX_CPU_ONLY_JOB_BATCH_DELAY_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_MAX_CPU_ONLY_JOB_BATCH_DELAY_MS, 1860000L);
            this.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS, 1860000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateUseFactorConstantsLocked() {
            this.HEAVY_USE_FACTOR = android.provider.DeviceConfig.getFloat("jobscheduler", KEY_HEAVY_USE_FACTOR, DEFAULT_HEAVY_USE_FACTOR);
            this.MODERATE_USE_FACTOR = android.provider.DeviceConfig.getFloat("jobscheduler", KEY_MODERATE_USE_FACTOR, 0.5f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateBackoffConstantsLocked() {
            this.MIN_LINEAR_BACKOFF_TIME_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_MIN_LINEAR_BACKOFF_TIME_MS, 10000L);
            this.MIN_EXP_BACKOFF_TIME_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_MIN_EXP_BACKOFF_TIME_MS, 10000L);
            this.SYSTEM_STOP_TO_FAILURE_RATIO = android.provider.DeviceConfig.getInt("jobscheduler", KEY_SYSTEM_STOP_TO_FAILURE_RATIO, 3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateConnectivityConstantsLocked() {
            this.CONN_CONGESTION_DELAY_FRAC = android.provider.DeviceConfig.getFloat("jobscheduler", KEY_CONN_CONGESTION_DELAY_FRAC, 0.5f);
            this.CONN_PREFETCH_RELAX_FRAC = android.provider.DeviceConfig.getFloat("jobscheduler", KEY_CONN_PREFETCH_RELAX_FRAC, 0.5f);
            this.CONN_USE_CELL_SIGNAL_STRENGTH = android.provider.DeviceConfig.getBoolean("jobscheduler", KEY_CONN_USE_CELL_SIGNAL_STRENGTH, true);
            this.CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS, 60000L);
            this.CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC = android.provider.DeviceConfig.getFloat("jobscheduler", KEY_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC, 0.5f);
            java.lang.String batchThresholdConfigString = android.provider.DeviceConfig.getString("jobscheduler", KEY_CONN_TRANSPORT_BATCH_THRESHOLD, (java.lang.String) null);
            android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
            this.CONN_TRANSPORT_BATCH_THRESHOLD.clear();
            try {
                parser.setString(batchThresholdConfigString);
                for (int t = parser.size() - 1; t >= 0; t--) {
                    java.lang.String transportString = parser.keyAt(t);
                    try {
                        int transport = java.lang.Integer.parseInt(transportString);
                        this.CONN_TRANSPORT_BATCH_THRESHOLD.put(transport, java.lang.Math.max(0, java.lang.Math.min(com.android.server.job.JobConcurrencyManager.DEFAULT_CONCURRENCY_LIMIT / 3, parser.getInt(transportString, 1))));
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Slog.e("JobScheduler", "Bad transport string", e);
                    }
                }
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.wtf("JobScheduler", "Bad string for conn_transport_batch_threshold", e2);
                copyTransportBatchThresholdDefaults();
            }
            this.CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS = java.lang.Math.max(0L, java.lang.Math.min(86400000L, android.provider.DeviceConfig.getLong("jobscheduler", KEY_CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS, 1860000L)));
        }

        private void copyTransportBatchThresholdDefaults() {
            for (int i = DEFAULT_CONN_TRANSPORT_BATCH_THRESHOLD.size() - 1; i >= 0; i--) {
                this.CONN_TRANSPORT_BATCH_THRESHOLD.put(DEFAULT_CONN_TRANSPORT_BATCH_THRESHOLD.keyAt(i), DEFAULT_CONN_TRANSPORT_BATCH_THRESHOLD.valueAt(i));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePersistingConstantsLocked() {
            this.PERSIST_IN_SPLIT_FILES = android.provider.DeviceConfig.getBoolean("jobscheduler", KEY_PERSIST_IN_SPLIT_FILES, true);
            this.MAX_NUM_PERSISTED_JOB_WORK_ITEMS = android.provider.DeviceConfig.getInt("jobscheduler", KEY_MAX_NUM_PERSISTED_JOB_WORK_ITEMS, 100000);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePrefetchConstantsLocked() {
            this.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS, 3600000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateApiQuotaConstantsLocked() {
            this.ENABLE_API_QUOTAS = android.provider.DeviceConfig.getBoolean("jobscheduler", KEY_ENABLE_API_QUOTAS, true);
            this.ENABLE_EXECUTION_SAFEGUARDS_UDC = android.provider.DeviceConfig.getBoolean("jobscheduler", KEY_ENABLE_EXECUTION_SAFEGUARDS_UDC, true);
            this.API_QUOTA_SCHEDULE_COUNT = java.lang.Math.max(250, android.provider.DeviceConfig.getInt("jobscheduler", KEY_API_QUOTA_SCHEDULE_COUNT, 250));
            this.API_QUOTA_SCHEDULE_WINDOW_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_API_QUOTA_SCHEDULE_WINDOW_MS, 60000L);
            this.API_QUOTA_SCHEDULE_THROW_EXCEPTION = android.provider.DeviceConfig.getBoolean("jobscheduler", KEY_API_QUOTA_SCHEDULE_THROW_EXCEPTION, true);
            this.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT = android.provider.DeviceConfig.getBoolean("jobscheduler", KEY_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT, false);
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT = java.lang.Math.max(2, android.provider.DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, 2));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT = java.lang.Math.max(2, android.provider.DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, 5));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT = java.lang.Math.max(2, android.provider.DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT, 3));
            int highestTimeoutCount = java.lang.Math.max(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, java.lang.Math.max(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT = java.lang.Math.max(highestTimeoutCount, android.provider.DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT, 10));
            this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS, 86400000L);
            this.EXECUTION_SAFEGUARDS_UDC_ANR_COUNT = java.lang.Math.max(1, android.provider.DeviceConfig.getInt("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT, 3));
            this.EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS = android.provider.DeviceConfig.getLong("jobscheduler", KEY_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS, DEFAULT_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRuntimeConstantsLocked() {
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[]{KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, KEY_RUNTIME_MIN_GUARANTEE_MS, KEY_RUNTIME_MIN_EJ_GUARANTEE_MS, KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, KEY_RUNTIME_MIN_UI_GUARANTEE_MS, KEY_RUNTIME_UI_LIMIT_MS, KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS, KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS, KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS});
            this.RUNTIME_MIN_GUARANTEE_MS = java.lang.Math.max(600000L, properties.getLong(KEY_RUNTIME_MIN_GUARANTEE_MS, 600000L));
            this.RUNTIME_MIN_EJ_GUARANTEE_MS = java.lang.Math.max(60000L, properties.getLong(KEY_RUNTIME_MIN_EJ_GUARANTEE_MS, 180000L));
            this.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS = java.lang.Math.max(this.RUNTIME_MIN_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, 1800000L));
            this.RUNTIME_MIN_UI_GUARANTEE_MS = java.lang.Math.max(this.RUNTIME_MIN_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_MIN_UI_GUARANTEE_MS, DEFAULT_RUNTIME_MIN_UI_GUARANTEE_MS));
            this.RUNTIME_UI_LIMIT_MS = java.lang.Math.max(this.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, java.lang.Math.max(this.RUNTIME_MIN_UI_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_UI_LIMIT_MS, DEFAULT_RUNTIME_UI_LIMIT_MS)));
            this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR = java.lang.Math.max(1.0f, properties.getFloat(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, 1.35f));
            this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS = java.lang.Math.max(this.RUNTIME_MIN_UI_GUARANTEE_MS, properties.getLong(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS, DEFAULT_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS));
            this.RUNTIME_CUMULATIVE_UI_LIMIT_MS = java.lang.Math.max(this.RUNTIME_UI_LIMIT_MS, properties.getLong(KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS, 86400000L));
            this.RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS = properties.getBoolean(KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS, false);
        }

        void dump(android.util.IndentingPrintWriter pw) {
            pw.println("Settings:");
            pw.increaseIndent();
            pw.print(KEY_MIN_READY_CPU_ONLY_JOBS_COUNT, java.lang.Integer.valueOf(this.MIN_READY_CPU_ONLY_JOBS_COUNT)).println();
            pw.print(KEY_MIN_READY_NON_ACTIVE_JOBS_COUNT, java.lang.Integer.valueOf(this.MIN_READY_NON_ACTIVE_JOBS_COUNT)).println();
            pw.print(KEY_MAX_CPU_ONLY_JOB_BATCH_DELAY_MS, java.lang.Long.valueOf(this.MAX_CPU_ONLY_JOB_BATCH_DELAY_MS)).println();
            pw.print(KEY_MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS, java.lang.Long.valueOf(this.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS)).println();
            pw.print(KEY_HEAVY_USE_FACTOR, java.lang.Float.valueOf(this.HEAVY_USE_FACTOR)).println();
            pw.print(KEY_MODERATE_USE_FACTOR, java.lang.Float.valueOf(this.MODERATE_USE_FACTOR)).println();
            pw.print(KEY_MIN_LINEAR_BACKOFF_TIME_MS, java.lang.Long.valueOf(this.MIN_LINEAR_BACKOFF_TIME_MS)).println();
            pw.print(KEY_MIN_EXP_BACKOFF_TIME_MS, java.lang.Long.valueOf(this.MIN_EXP_BACKOFF_TIME_MS)).println();
            pw.print(KEY_SYSTEM_STOP_TO_FAILURE_RATIO, java.lang.Integer.valueOf(this.SYSTEM_STOP_TO_FAILURE_RATIO)).println();
            pw.print(KEY_CONN_CONGESTION_DELAY_FRAC, java.lang.Float.valueOf(this.CONN_CONGESTION_DELAY_FRAC)).println();
            pw.print(KEY_CONN_PREFETCH_RELAX_FRAC, java.lang.Float.valueOf(this.CONN_PREFETCH_RELAX_FRAC)).println();
            pw.print(KEY_CONN_USE_CELL_SIGNAL_STRENGTH, java.lang.Boolean.valueOf(this.CONN_USE_CELL_SIGNAL_STRENGTH)).println();
            pw.print(KEY_CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS, java.lang.Long.valueOf(this.CONN_UPDATE_ALL_JOBS_MIN_INTERVAL_MS)).println();
            pw.print(KEY_CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC, java.lang.Float.valueOf(this.CONN_LOW_SIGNAL_STRENGTH_RELAX_FRAC)).println();
            pw.print(KEY_CONN_TRANSPORT_BATCH_THRESHOLD, this.CONN_TRANSPORT_BATCH_THRESHOLD.toString()).println();
            pw.print(KEY_CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS, java.lang.Long.valueOf(this.CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS)).println();
            pw.print(KEY_PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS, java.lang.Long.valueOf(this.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS)).println();
            pw.print(KEY_ENABLE_API_QUOTAS, java.lang.Boolean.valueOf(this.ENABLE_API_QUOTAS)).println();
            pw.print(KEY_API_QUOTA_SCHEDULE_COUNT, java.lang.Integer.valueOf(this.API_QUOTA_SCHEDULE_COUNT)).println();
            pw.print(KEY_API_QUOTA_SCHEDULE_WINDOW_MS, java.lang.Long.valueOf(this.API_QUOTA_SCHEDULE_WINDOW_MS)).println();
            pw.print(KEY_API_QUOTA_SCHEDULE_THROW_EXCEPTION, java.lang.Boolean.valueOf(this.API_QUOTA_SCHEDULE_THROW_EXCEPTION)).println();
            pw.print(KEY_API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT, java.lang.Boolean.valueOf(this.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT)).println();
            pw.print(KEY_ENABLE_EXECUTION_SAFEGUARDS_UDC, java.lang.Boolean.valueOf(this.ENABLE_EXECUTION_SAFEGUARDS_UDC)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT, java.lang.Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_UIJ_COUNT)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT, java.lang.Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_EJ_COUNT)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT, java.lang.Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_REG_COUNT)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT, java.lang.Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_TOTAL_COUNT)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS, java.lang.Long.valueOf(this.EXECUTION_SAFEGUARDS_UDC_TIMEOUT_WINDOW_MS)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_ANR_COUNT, java.lang.Integer.valueOf(this.EXECUTION_SAFEGUARDS_UDC_ANR_COUNT)).println();
            pw.print(KEY_EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS, java.lang.Long.valueOf(this.EXECUTION_SAFEGUARDS_UDC_ANR_WINDOW_MS)).println();
            pw.print(KEY_RUNTIME_MIN_GUARANTEE_MS, java.lang.Long.valueOf(this.RUNTIME_MIN_GUARANTEE_MS)).println();
            pw.print(KEY_RUNTIME_MIN_EJ_GUARANTEE_MS, java.lang.Long.valueOf(this.RUNTIME_MIN_EJ_GUARANTEE_MS)).println();
            pw.print(KEY_RUNTIME_FREE_QUOTA_MAX_LIMIT_MS, java.lang.Long.valueOf(this.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS)).println();
            pw.print(KEY_RUNTIME_MIN_UI_GUARANTEE_MS, java.lang.Long.valueOf(this.RUNTIME_MIN_UI_GUARANTEE_MS)).println();
            pw.print(KEY_RUNTIME_UI_LIMIT_MS, java.lang.Long.valueOf(this.RUNTIME_UI_LIMIT_MS)).println();
            pw.print(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR, java.lang.Float.valueOf(this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR)).println();
            pw.print(KEY_RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS, java.lang.Long.valueOf(this.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS)).println();
            pw.print(KEY_RUNTIME_CUMULATIVE_UI_LIMIT_MS, java.lang.Long.valueOf(this.RUNTIME_CUMULATIVE_UI_LIMIT_MS)).println();
            pw.print(KEY_RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS, java.lang.Boolean.valueOf(this.RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS)).println();
            pw.print(KEY_PERSIST_IN_SPLIT_FILES, java.lang.Boolean.valueOf(this.PERSIST_IN_SPLIT_FILES)).println();
            pw.print(KEY_MAX_NUM_PERSISTED_JOB_WORK_ITEMS, java.lang.Integer.valueOf(this.MAX_NUM_PERSISTED_JOB_WORK_ITEMS)).println();
            pw.decreaseIndent();
        }

        void dump(android.util.proto.ProtoOutputStream proto) {
            proto.write(1120986464285L, this.MIN_READY_NON_ACTIVE_JOBS_COUNT);
            proto.write(1112396529694L, this.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS);
            proto.write(1103806595080L, this.HEAVY_USE_FACTOR);
            proto.write(1103806595081L, this.MODERATE_USE_FACTOR);
            proto.write(1112396529681L, this.MIN_LINEAR_BACKOFF_TIME_MS);
            proto.write(1112396529682L, this.MIN_EXP_BACKOFF_TIME_MS);
            proto.write(1103806595093L, this.CONN_CONGESTION_DELAY_FRAC);
            proto.write(1103806595094L, this.CONN_PREFETCH_RELAX_FRAC);
            proto.write(1133871366175L, this.ENABLE_API_QUOTAS);
            proto.write(1120986464288L, this.API_QUOTA_SCHEDULE_COUNT);
            proto.write(1112396529697L, this.API_QUOTA_SCHEDULE_WINDOW_MS);
            proto.write(1133871366178L, this.API_QUOTA_SCHEDULE_THROW_EXCEPTION);
            proto.write(1133871366179L, this.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT);
        }
    }

    public static java.lang.String getPackageName(android.content.Intent intent) {
        android.net.Uri uri = intent.getData();
        if (uri == null) {
            return null;
        }
        java.lang.String pkg = uri.getSchemeSpecificPart();
        return pkg;
    }

    public android.content.Context getTestableContext() {
        return getContext();
    }

    public java.lang.Object getLock() {
        return this.mLock;
    }

    public com.android.server.job.JobStore getJobStore() {
        return this.mJobs;
    }

    public com.android.server.job.JobSchedulerService.Constants getConstants() {
        return this.mConstants;
    }

    com.android.server.job.PendingJobQueue getPendingJobQueue() {
        return this.mPendingJobQueue;
    }

    public android.os.WorkSource deriveWorkSource(int sourceUid, java.lang.String sourcePackageName) {
        if (!android.os.WorkSource.isChainedBatteryAttributionEnabled(getContext())) {
            return sourcePackageName == null ? new android.os.WorkSource(sourceUid) : new android.os.WorkSource(sourceUid, sourcePackageName);
        }
        android.os.WorkSource ws = new android.os.WorkSource();
        ws.createWorkChain().addNode(sourceUid, sourcePackageName).addNode(1000, "JobScheduler");
        return ws;
    }

    public android.util.ArraySet<java.lang.String> getPackagesForUidLocked(int uid) {
        android.util.ArraySet<java.lang.String> packages = this.mUidToPackageCache.get(uid);
        if (packages == null) {
            try {
                java.lang.String[] pkgs = android.app.AppGlobals.getPackageManager().getPackagesForUid(uid);
                if (pkgs != null) {
                    for (java.lang.String pkg : pkgs) {
                        this.mUidToPackageCache.add(uid, pkg);
                    }
                    return this.mUidToPackageCache.get(uid);
                }
                return packages;
            } catch (android.os.RemoteException e) {
                return packages;
            }
        }
        return packages;
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mStartedUsers = com.android.internal.util.ArrayUtils.appendInt(this.mStartedUsers, user.getUserIdentifier());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserCompletedEvent(com.android.server.SystemService.TargetUser user, com.android.server.SystemService.UserCompletedEventType eventType) {
        if (eventType.includesOnUserStarting() || eventType.includesOnUserUnlocked()) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mStartedUsers = com.android.internal.util.ArrayUtils.removeInt(this.mStartedUsers, user.getUserIdentifier());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUidActive(int uid) {
        return this.mAppStateTracker.isUidActiveSynced(uid);
    }

    public int scheduleAsPackage(android.app.job.JobInfo job, android.app.job.JobWorkItem work, int callingUid, java.lang.String packageName, int userId, java.lang.String namespace, java.lang.String tag) throws java.lang.Throwable {
        java.lang.String str;
        com.android.server.job.controllers.JobStatus jobStatus;
        java.lang.String[] strArr;
        java.lang.String servicePkg = job.getService().getPackageName();
        if (job.isPersisted() && (packageName == null || packageName.equals(servicePkg))) {
            java.lang.String pkg = packageName == null ? servicePkg : packageName;
            if (!this.mQuotaTracker.isWithinQuota(userId, pkg, QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG)) {
                if (this.mQuotaTracker.isWithinQuota(userId, pkg, QUOTA_TRACKER_SCHEDULE_LOGGED)) {
                    android.util.Slog.wtf("JobScheduler", userId + "-" + pkg + " has called schedule() too many times");
                    this.mQuotaTracker.noteEvent(userId, pkg, QUOTA_TRACKER_SCHEDULE_LOGGED);
                }
                this.mAppStandbyInternal.restrictApp(pkg, userId, 4);
                if (this.mConstants.API_QUOTA_SCHEDULE_THROW_EXCEPTION) {
                    synchronized (this.mLock) {
                        if (!this.mDebuggableApps.containsKey(packageName)) {
                            try {
                                android.content.pm.ApplicationInfo appInfo = android.app.AppGlobals.getPackageManager().getApplicationInfo(pkg, 0L, userId);
                                if (appInfo == null) {
                                    return 0;
                                }
                                this.mDebuggableApps.put(packageName, java.lang.Boolean.valueOf((appInfo.flags & 2) != 0));
                            } catch (android.os.RemoteException e) {
                                throw new java.lang.RuntimeException(e);
                            }
                        }
                        boolean isDebuggable = this.mDebuggableApps.get(packageName).booleanValue();
                        if (isDebuggable) {
                            throw new android.os.LimitExceededException("schedule()/enqueue() called more than " + this.mQuotaTracker.getLimit(QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED) + " times in the past " + this.mQuotaTracker.getWindowSizeMs(QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED) + "ms. See the documentation for more information.");
                        }
                    }
                }
                if (this.mConstants.API_QUOTA_SCHEDULE_RETURN_FAILURE_RESULT) {
                    return 0;
                }
            }
            this.mQuotaTracker.noteEvent(userId, pkg, QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG);
        }
        if (this.mActivityManagerInternal.isAppStartModeDisabled(callingUid, servicePkg)) {
            android.util.Slog.w("JobScheduler", "Not scheduling job for " + callingUid + ":" + job.toString() + " -- package not allowed to start");
            com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_app_start_mode_disabled", callingUid);
            return 0;
        }
        this.mJobSchedulerServiceExt.scheduleAsPackage(getContext(), job, callingUid);
        if (job.getRequiredNetwork() != null) {
            sInitialJobEstimatedNetworkDownloadKBLogger.logSample(safelyScaleBytesToKBForHistogram(job.getEstimatedNetworkDownloadBytes()));
            sInitialJobEstimatedNetworkUploadKBLogger.logSample(safelyScaleBytesToKBForHistogram(job.getEstimatedNetworkUploadBytes()));
            sJobMinimumChunkKBLogger.logSampleWithUid(callingUid, safelyScaleBytesToKBForHistogram(job.getMinimumNetworkChunkBytes()));
            if (work != null) {
                sInitialJwiEstimatedNetworkDownloadKBLogger.logSample(safelyScaleBytesToKBForHistogram(work.getEstimatedNetworkDownloadBytes()));
                sInitialJwiEstimatedNetworkUploadKBLogger.logSample(safelyScaleBytesToKBForHistogram(work.getEstimatedNetworkUploadBytes()));
                sJwiMinimumChunkKBLogger.logSampleWithUid(callingUid, safelyScaleBytesToKBForHistogram(work.getMinimumNetworkChunkBytes()));
            }
        }
        if (work != null) {
            com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_job_work_items_enqueued", callingUid);
        }
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.job.controllers.JobStatus toCancel = this.mJobs.getJobByUidAndJobId(callingUid, namespace, job.getId());
                    if (work != null && toCancel != null && toCancel.getJob().equals(job)) {
                        if (toCancel.getWorkCount() >= this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS && toCancel.isPersisted()) {
                            android.util.Slog.w("JobScheduler", "Too many JWIs for uid " + callingUid);
                            throw new java.lang.IllegalStateException("Apps may not persist more than " + this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS + " JobWorkItems per job");
                        }
                        toCancel.enqueueWorkLocked(work);
                        if (toCancel.getJob().isUserInitiated()) {
                            toCancel.removeInternalFlags(6);
                        }
                        this.mJobs.touchJob(toCancel);
                        sEnqueuedJwiHighWaterMarkLogger.logSampleWithUid(callingUid, toCancel.getWorkCount());
                        toCancel.maybeAddForegroundExemption(this.mIsUidActivePredicate);
                        return 1;
                    }
                    com.android.server.job.controllers.JobStatus jobStatus2 = com.android.server.job.controllers.JobStatus.createFromJobInfo(job, callingUid, packageName, userId, namespace, tag);
                    if (jobStatus2.isRequestedExpeditedJob() && !this.mQuotaController.isWithinEJQuotaLocked(jobStatus2)) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_ej_out_of_quota", callingUid);
                        return 0;
                    }
                    jobStatus2.maybeAddForegroundExemption(this.mIsUidActivePredicate);
                    if (DEBUG) {
                        android.util.Slog.d("JobScheduler", "SCHEDULE: " + jobStatus2.toShortString());
                    }
                    if (packageName == null && this.mJobs.countJobsForUid(callingUid) > 150) {
                        this.mJobSchedulerServiceExt.onHookRedundantJob(getContext(), this.mJobs, callingUid, 150);
                    }
                    jobStatus2.prepareLocked();
                    if (toCancel != null) {
                        if (work != null && toCancel.isPersisted() && toCancel.getWorkCount() >= this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS) {
                            android.util.Slog.w("JobScheduler", "Too many JWIs for uid " + callingUid);
                            throw new java.lang.IllegalStateException("Apps may not persist more than " + this.mConstants.MAX_NUM_PERSISTED_JOB_WORK_ITEMS + " JobWorkItems per job");
                        }
                        str = null;
                        jobStatus = jobStatus2;
                        cancelJobImplLocked(toCancel, jobStatus2, 1, 0, "job rescheduled by app");
                    } else {
                        str = null;
                        jobStatus = jobStatus2;
                        startTrackingJobLocked(jobStatus, null);
                    }
                    if (work != null) {
                        jobStatus.enqueueWorkLocked(work);
                        sEnqueuedJwiHighWaterMarkLogger.logSampleWithUid(callingUid, jobStatus.getWorkCount());
                    }
                    int sourceUid = jobStatus.getSourceUid();
                    int[] iArr = jobStatus.isProxyJob() ? new int[]{sourceUid, callingUid} : new int[]{sourceUid};
                    if (jobStatus.isProxyJob()) {
                        strArr = new java.lang.String[]{str, jobStatus.getSourceTag()};
                    } else {
                        strArr = new java.lang.String[]{jobStatus.getSourceTag()};
                    }
                    com.android.internal.util.FrameworkStatsLog.write(8, iArr, strArr, jobStatus.getBatteryName(), 2, -1, jobStatus.getStandbyBucket(), jobStatus.getLoggingJobId(), jobStatus.hasChargingConstraint(), jobStatus.hasBatteryNotLowConstraint(), jobStatus.hasStorageNotLowConstraint(), jobStatus.hasTimingDelayConstraint(), jobStatus.hasDeadlineConstraint(), jobStatus.hasIdleConstraint(), jobStatus.hasConnectivityConstraint(), jobStatus.hasContentTriggerConstraint(), jobStatus.isRequestedExpeditedJob(), false, 0, jobStatus.getJob().isPrefetch(), jobStatus.getJob().getPriority(), jobStatus.getEffectivePriority(), jobStatus.getNumPreviousAttempts(), jobStatus.getJob().getMaxExecutionDelayMillis(), false, false, false, false, false, false, false, false, 0L, jobStatus.getJob().isUserInitiated(), false, jobStatus.getJob().isPeriodic(), jobStatus.getJob().getMinLatencyMillis(), jobStatus.getEstimatedNetworkDownloadBytes(), jobStatus.getEstimatedNetworkUploadBytes(), jobStatus.getWorkCount(), android.app.ActivityManager.processStateAmToProto(this.mUidProcStates.get(jobStatus.getUid())), jobStatus.getNamespaceHash(), 0L, 0L, 0L, 0L, jobStatus.getJob().getIntervalMillis(), jobStatus.getJob().getFlexMillis(), jobStatus.hasFlexibilityConstraint(), false, jobStatus.canApplyTransportAffinities(), jobStatus.getNumAppliedFlexibleConstraints(), jobStatus.getNumDroppedFlexibleConstraints(), jobStatus.getFilteredTraceTag(), jobStatus.getFilteredDebugTags());
                    this.mJobSchedulerServiceExt.updateJobCheckTime(0, false);
                    if (isReadyToBeExecutedLocked(jobStatus)) {
                        this.mJobPackageTracker.notePending(jobStatus);
                        this.mPendingJobQueue.add(jobStatus);
                        maybeRunPendingJobsLocked();
                    }
                    this.mJobSchedulerServiceExt.updateJobCheckTime(0, true);
                    return 1;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.ArrayMap<java.lang.String, java.util.List<android.app.job.JobInfo>> getPendingJobs(int uid) {
        android.util.ArrayMap<java.lang.String, java.util.List<android.app.job.JobInfo>> outMap = new android.util.ArrayMap<>();
        synchronized (this.mLock) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.getJobsByUid(uid);
            for (int i = jobs.size() - 1; i >= 0; i--) {
                com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
                java.util.List<android.app.job.JobInfo> outList = outMap.get(job.getNamespace());
                if (outList == null) {
                    outList = new java.util.ArrayList();
                    outMap.put(job.getNamespace(), outList);
                }
                outList.add(job.getJob());
            }
        }
        return outMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.app.job.JobInfo> getPendingJobsInNamespace(int uid, java.lang.String namespace) {
        java.util.ArrayList<android.app.job.JobInfo> outList;
        synchronized (this.mLock) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.getJobsByUid(uid);
            outList = new java.util.ArrayList<>();
            for (int i = jobs.size() - 1; i >= 0; i--) {
                com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
                if (java.util.Objects.equals(namespace, job.getNamespace())) {
                    outList.add(job.getJob());
                }
            }
        }
        return outList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPendingJobReason(int uid, java.lang.String namespace, int jobId) {
        int reason;
        int reason2;
        synchronized (this.mPendingJobReasonCache) {
            android.util.SparseIntArray jobIdToReason = (android.util.SparseIntArray) this.mPendingJobReasonCache.get(uid, namespace);
            if (jobIdToReason != null && (reason2 = jobIdToReason.get(jobId, 0)) != 0) {
                return reason2;
            }
            synchronized (this.mLock) {
                reason = getPendingJobReasonLocked(uid, namespace, jobId);
                if (DEBUG) {
                    android.util.Slog.v("JobScheduler", "getPendingJobReason(" + uid + "," + namespace + "," + jobId + ")=" + reason);
                }
            }
            synchronized (this.mPendingJobReasonCache) {
                android.util.SparseIntArray jobIdToReason2 = (android.util.SparseIntArray) this.mPendingJobReasonCache.get(uid, namespace);
                if (jobIdToReason2 == null) {
                    jobIdToReason2 = new android.util.SparseIntArray();
                    this.mPendingJobReasonCache.add(uid, namespace, jobIdToReason2);
                }
                jobIdToReason2.put(jobId, reason);
            }
            return reason;
        }
    }

    int getPendingJobReason(com.android.server.job.controllers.JobStatus job) {
        return getPendingJobReason(job.getUid(), job.getNamespace(), job.getJobId());
    }

    private int getPendingJobReasonLocked(int uid, java.lang.String namespace, int jobId) {
        com.android.server.job.controllers.JobStatus job = this.mJobs.getJobByUidAndJobId(uid, namespace, jobId);
        if (job == null) {
            return -2;
        }
        if (isCurrentlyRunningLocked(job)) {
            return -1;
        }
        boolean jobReady = job.isReady();
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " ready=" + jobReady);
        }
        if (!jobReady) {
            return job.getPendingJobReason();
        }
        boolean userStarted = areUsersStartedLocked(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " userStarted=" + userStarted);
        }
        if (!userStarted) {
            return 15;
        }
        boolean backingUp = this.mBackingUpUids.get(job.getSourceUid());
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " backingUp=" + backingUp);
        }
        if (backingUp) {
            return 1;
        }
        com.android.server.job.restrictions.JobRestriction restriction = checkIfRestricted(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " restriction=" + restriction);
        }
        if (restriction != null) {
            return restriction.getPendingReason();
        }
        boolean jobPending = this.mPendingJobQueue.contains(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " pending=" + jobPending);
        }
        if (jobPending) {
            return 12;
        }
        boolean jobActive = this.mConcurrencyManager.isJobRunningLocked(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " active=" + jobActive);
        }
        if (jobActive) {
            return 0;
        }
        boolean componentUsable = isComponentUsable(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "getPendingJobReasonLocked: " + job.toShortString() + " componentUsable=" + componentUsable);
        }
        if (!componentUsable) {
            return 1;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.job.JobInfo getPendingJob(int uid, java.lang.String namespace, int jobId) {
        synchronized (this.mLock) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.getJobsByUid(uid);
            for (int i = jobs.size() - 1; i >= 0; i--) {
                com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
                if (job.getJobId() == jobId && java.util.Objects.equals(namespace, job.getNamespace())) {
                    return job.getJob();
                }
            }
            return null;
        }
    }

    void notePendingUserRequestedAppStopInternal(java.lang.String packageName, int userId, java.lang.String debugReason) {
        int packageUid = this.mLocalPM.getPackageUid(packageName, 0L, userId);
        if (packageUid < 0) {
            android.util.Slog.wtf("JobScheduler", "Asked to stop jobs of an unknown package");
            return;
        }
        synchronized (this.mLock) {
            this.mConcurrencyManager.markJobsForUserStopLocked(userId, packageName, debugReason);
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mJobs.getJobsByUid(packageUid);
            for (int i = jobs.size() - 1; i >= 0; i--) {
                com.android.server.job.controllers.JobStatus job = jobs.valueAt(i);
                job.addInternalFlags(2);
                if (this.mPendingJobQueue.remove(job)) {
                    synchronized (this.mPendingJobReasonCache) {
                        android.util.SparseIntArray jobIdToReason = (android.util.SparseIntArray) this.mPendingJobReasonCache.get(job.getUid(), job.getNamespace());
                        if (jobIdToReason == null) {
                            jobIdToReason = new android.util.SparseIntArray();
                            this.mPendingJobReasonCache.add(job.getUid(), job.getNamespace(), jobIdToReason);
                        }
                        jobIdToReason.put(job.getJobId(), 15);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(com.android.server.job.controllers.JobStatus toRemove) {
        cancelJobImplLocked(toRemove, null, 13, 7, "user removed");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelJobsForUserLocked(final int userHandle) {
        this.mJobs.forEachJob(new java.util.function.Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.job.JobSchedulerService.lambda$cancelJobsForUserLocked$1(userHandle, (com.android.server.job.controllers.JobStatus) obj);
            }
        }, this.mCancelJobDueToUserRemovalConsumer);
    }

    static /* synthetic */ boolean lambda$cancelJobsForUserLocked$1(int userHandle, com.android.server.job.controllers.JobStatus job) {
        return job.getUserId() == userHandle || job.getSourceUserId() == userHandle;
    }

    private void cancelJobsForNonExistentUsers() {
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        synchronized (this.mLock) {
            this.mJobs.removeJobsOfUnlistedUsers(umi.getUserIds());
        }
        synchronized (this.mPendingJobReasonCache) {
            this.mPendingJobReasonCache.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelJobsForPackageAndUidLocked(java.lang.String pkgName, int uid, boolean includeSchedulingApp, boolean includeSourceApp, int reason, int internalReasonCode, java.lang.String debugReason) {
        boolean includeSourceApp2;
        if (!includeSchedulingApp && !includeSourceApp) {
            android.util.Slog.wtfStack("JobScheduler", "Didn't indicate whether to cancel jobs for scheduling and/or source app");
            includeSourceApp2 = true;
        } else {
            includeSourceApp2 = includeSourceApp;
        }
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(pkgName)) {
            android.util.Slog.wtfStack("JobScheduler", "Can't cancel all jobs for system package");
            return;
        }
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsForUid = new android.util.ArraySet<>();
        if (includeSchedulingApp) {
            this.mJobs.getJobsByUid(uid, jobsForUid);
        }
        if (includeSourceApp2) {
            this.mJobs.getJobsBySourceUid(uid, jobsForUid);
        }
        for (int i = jobsForUid.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus job = jobsForUid.valueAt(i);
            boolean shouldCancel = (includeSchedulingApp && job.getServiceComponent().getPackageName().equals(pkgName)) || (includeSourceApp2 && job.getSourcePackageName().equals(pkgName));
            if (shouldCancel) {
                cancelJobImplLocked(job, null, reason, internalReasonCode, debugReason);
            }
        }
    }

    public boolean cancelJobsForUid(int uid, boolean includeSourceApp, int reason, int internalReasonCode, java.lang.String debugReason) {
        return cancelJobsForUid(uid, includeSourceApp, false, null, reason, internalReasonCode, debugReason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:26:0x005b
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public boolean cancelJobsForUid(int r14, boolean r15, boolean r16, java.lang.String r17, int r18, int r19, java.lang.String r20) {
        /*
            r13 = this;
            r7 = r13
            r8 = r14
            r9 = r17
            r0 = 1000(0x3e8, float:1.401E-42)
            if (r8 != r0) goto L15
            if (r16 == 0) goto Lc
            if (r9 != 0) goto L15
        Lc:
            java.lang.String r0 = "JobScheduler"
            java.lang.String r1 = "Can't cancel all jobs for system uid"
            android.util.Slog.wtfStack(r0, r1)
            r0 = 0
            return r0
        L15:
            r1 = 0
            java.lang.Object r10 = r7.mLock
            monitor-enter(r10)
            android.util.ArraySet r0 = new android.util.ArraySet     // Catch: java.lang.Throwable -> L5b
            r0.<init>()     // Catch: java.lang.Throwable -> L5b
            com.android.server.job.JobStore r2 = r7.mJobs     // Catch: java.lang.Throwable -> L5b
            r2.getJobsByUid(r14, r0)     // Catch: java.lang.Throwable -> L5b
            if (r15 == 0) goto L2a
            com.android.server.job.JobStore r2 = r7.mJobs     // Catch: java.lang.Throwable -> L5b
            r2.getJobsBySourceUid(r14, r0)     // Catch: java.lang.Throwable -> L5b
        L2a:
            r2 = 0
            r11 = r1
            r12 = r2
        L2d:
            int r1 = r0.size()     // Catch: java.lang.Throwable -> L58
            if (r12 >= r1) goto L56
            java.lang.Object r1 = r0.valueAt(r12)     // Catch: java.lang.Throwable -> L58
            r2 = r1
            com.android.server.job.controllers.JobStatus r2 = (com.android.server.job.controllers.JobStatus) r2     // Catch: java.lang.Throwable -> L58
            if (r16 == 0) goto L46
            java.lang.String r1 = r2.getNamespace()     // Catch: java.lang.Throwable -> L58
            boolean r1 = java.util.Objects.equals(r9, r1)     // Catch: java.lang.Throwable -> L58
            if (r1 == 0) goto L53
        L46:
            r3 = 0
            r1 = r13
            r4 = r18
            r5 = r19
            r6 = r20
            r1.cancelJobImplLocked(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L58
            r1 = 1
            r11 = r1
        L53:
            int r12 = r12 + 1
            goto L2d
        L56:
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L58
            return r11
        L58:
            r0 = move-exception
            r1 = r11
            goto L5c
        L5b:
            r0 = move-exception
        L5c:
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L5b
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerService.cancelJobsForUid(int, boolean, boolean, java.lang.String, int, int, java.lang.String):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean cancelJob(int uid, java.lang.String namespace, int jobId, int callingUid, int reason) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.job.controllers.JobStatus toCancel = this.mJobs.getJobByUidAndJobId(uid, namespace, jobId);
            if (toCancel != null) {
                cancelJobImplLocked(toCancel, null, reason, 0, "cancel() called by app, callingUid=" + callingUid + " uid=" + uid + " jobId=" + jobId);
            }
            z = toCancel != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelJobImplLocked(com.android.server.job.controllers.JobStatus cancelled, com.android.server.job.controllers.JobStatus incomingJob, int reason, int internalReasonCode, java.lang.String debugReason) {
        java.lang.String str;
        int[] iArr;
        java.lang.String[] strArr;
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "CANCEL: " + cancelled.toShortString());
        }
        if (DEBUG_USAGE) {
            android.util.Slog.d("JobScheduler", "CANCEL: " + cancelled.getJobId() + " " + cancelled.getSourcePackageName());
        }
        cancelled.unprepareLocked();
        stopTrackingJobLocked(cancelled, incomingJob, true);
        if (this.mPendingJobQueue.remove(cancelled)) {
            this.mJobPackageTracker.noteNonpending(cancelled);
        }
        this.mChangedJobList.remove(cancelled);
        boolean wasRunning = this.mConcurrencyManager.stopJobOnServiceContextLocked(cancelled, reason, internalReasonCode, debugReason);
        if (wasRunning) {
            str = "JobScheduler";
        } else {
            int sourceUid = cancelled.getSourceUid();
            if (!cancelled.isProxyJob()) {
                iArr = new int[]{sourceUid};
            } else {
                iArr = new int[]{sourceUid, cancelled.getUid()};
            }
            int[] iArr2 = iArr;
            if (cancelled.isProxyJob()) {
                strArr = new java.lang.String[]{null, cancelled.getSourceTag()};
            } else {
                strArr = new java.lang.String[]{cancelled.getSourceTag()};
            }
            str = "JobScheduler";
            com.android.internal.util.FrameworkStatsLog.write(8, iArr2, strArr, cancelled.getBatteryName(), 3, internalReasonCode, cancelled.getStandbyBucket(), cancelled.getLoggingJobId(), cancelled.hasChargingConstraint(), cancelled.hasBatteryNotLowConstraint(), cancelled.hasStorageNotLowConstraint(), cancelled.hasTimingDelayConstraint(), cancelled.hasDeadlineConstraint(), cancelled.hasIdleConstraint(), cancelled.hasConnectivityConstraint(), cancelled.hasContentTriggerConstraint(), cancelled.isRequestedExpeditedJob(), false, reason, cancelled.getJob().isPrefetch(), cancelled.getJob().getPriority(), cancelled.getEffectivePriority(), cancelled.getNumPreviousAttempts(), cancelled.getJob().getMaxExecutionDelayMillis(), cancelled.isConstraintSatisfied(1073741824), cancelled.isConstraintSatisfied(1), cancelled.isConstraintSatisfied(2), cancelled.isConstraintSatisfied(8), cancelled.isConstraintSatisfied(Integer.MIN_VALUE), cancelled.isConstraintSatisfied(4), cancelled.isConstraintSatisfied(268435456), cancelled.isConstraintSatisfied(67108864), 0L, cancelled.getJob().isUserInitiated(), false, cancelled.getJob().isPeriodic(), cancelled.getJob().getMinLatencyMillis(), cancelled.getEstimatedNetworkDownloadBytes(), cancelled.getEstimatedNetworkUploadBytes(), cancelled.getWorkCount(), android.app.ActivityManager.processStateAmToProto(this.mUidProcStates.get(cancelled.getUid())), cancelled.getNamespaceHash(), 0L, 0L, 0L, 0L, cancelled.getJob().getIntervalMillis(), cancelled.getJob().getFlexMillis(), cancelled.hasFlexibilityConstraint(), cancelled.isConstraintSatisfied(2097152), cancelled.canApplyTransportAffinities(), cancelled.getNumAppliedFlexibleConstraints(), cancelled.getNumDroppedFlexibleConstraints(), cancelled.getFilteredTraceTag(), cancelled.getFilteredDebugTags());
        }
        if (incomingJob != null) {
            if (DEBUG) {
                android.util.Slog.i(str, "Tracking replacement job " + incomingJob.toShortString());
            }
            startTrackingJobLocked(incomingJob, cancelled);
        }
        reportActiveLocked();
        if (this.mLastCancelledJobs.length > 0 && internalReasonCode == 0) {
            this.mLastCancelledJobs[this.mLastCancelledJobIndex] = cancelled;
            this.mLastCancelledJobTimeElapsed[this.mLastCancelledJobIndex] = sElapsedRealtimeClock.millis();
            this.mLastCancelledJobIndex = (this.mLastCancelledJobIndex + 1) % this.mLastCancelledJobs.length;
        }
    }

    void updateUidState(int uid, int procState, int capabilities) {
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "UID " + uid + " proc state changed to " + android.app.ActivityManager.procStateToString(procState) + " with capabilities=" + android.app.ActivityManager.getCapabilitiesSummary(capabilities));
        }
        synchronized (this.mLock) {
            this.mUidProcStates.put(uid, procState);
            int prevBias = this.mUidBiasOverride.get(uid, 0);
            if (procState == 2) {
                this.mUidBiasOverride.put(uid, 40);
            } else if (procState <= 4) {
                this.mUidBiasOverride.put(uid, 35);
            } else if (procState <= 5) {
                this.mUidBiasOverride.put(uid, 30);
            } else {
                this.mUidBiasOverride.delete(uid);
            }
            if (capabilities == 0 || procState == 20) {
                this.mUidCapabilities.delete(uid);
            } else {
                this.mUidCapabilities.put(uid, capabilities);
            }
            int newBias = this.mUidBiasOverride.get(uid, 0);
            if (prevBias != newBias) {
                if (DEBUG) {
                    android.util.Slog.d("JobScheduler", "UID " + uid + " bias changed from " + prevBias + " to " + newBias);
                }
                for (int c = 0; c < this.mControllers.size(); c++) {
                    this.mControllers.get(c).onUidBiasChangedLocked(uid, prevBias, newBias);
                }
                this.mConcurrencyManager.onUidBiasChangedLocked(prevBias, newBias);
            }
        }
    }

    public int getUidBias(int uid) {
        int i;
        synchronized (this.mLock) {
            i = this.mUidBiasOverride.get(uid, 0);
        }
        return i;
    }

    public int getUidCapabilities(int uid) {
        int i;
        synchronized (this.mLock) {
            i = this.mUidCapabilities.get(uid, 0);
        }
        return i;
    }

    public int getUidProcState(int uid) {
        int i;
        synchronized (this.mLock) {
            i = this.mUidProcStates.get(uid, -1);
        }
        return i;
    }

    @Override // com.android.server.job.StateChangedListener
    public void onDeviceIdleStateChanged(boolean deviceIdle) {
        synchronized (this.mLock) {
            if (DEBUG) {
                android.util.Slog.d("JobScheduler", "Doze state changed: " + deviceIdle);
            }
            if (!deviceIdle && this.mReadyToRock) {
                if (this.mLocalDeviceIdleController != null && !this.mReportedActive) {
                    this.mReportedActive = true;
                    this.mLocalDeviceIdleController.setJobsActive(true);
                }
                this.mHandler.obtainMessage(1).sendToTarget();
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onNetworkChanged(com.android.server.job.controllers.JobStatus jobStatus, android.net.Network newNetwork) {
        synchronized (this.mLock) {
            com.android.server.job.JobServiceContext jsc = this.mConcurrencyManager.getRunningJobServiceContextLocked(jobStatus);
            if (jsc != null) {
                jsc.informOfNetworkChangeLocked(newNetwork);
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onRestrictedBucketChanged(java.util.List<com.android.server.job.controllers.JobStatus> jobs) {
        int len = jobs.size();
        if (len == 0) {
            android.util.Slog.wtf("JobScheduler", "onRestrictedBucketChanged called with no jobs");
            return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < len; i++) {
                com.android.server.job.controllers.JobStatus js = jobs.get(i);
                for (int j = this.mRestrictiveControllers.size() - 1; j >= 0; j--) {
                    if (js.getStandbyBucket() == 5) {
                        this.mRestrictiveControllers.get(j).startTrackingRestrictedJobLocked(js);
                    } else {
                        this.mRestrictiveControllers.get(j).stopTrackingRestrictedJobLocked(js);
                    }
                }
            }
        }
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    void reportActiveLocked() {
        boolean active = this.mPendingJobQueue.size() > 0;
        if (!active) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> runningJobs = this.mConcurrencyManager.getRunningJobsLocked();
            int i = runningJobs.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                com.android.server.job.controllers.JobStatus job = runningJobs.valueAt(i);
                if (job.canRunInDoze()) {
                    i--;
                } else {
                    active = true;
                    break;
                }
            }
        }
        if (this.mReportedActive != active) {
            this.mReportedActive = active;
            if (this.mLocalDeviceIdleController != null) {
                this.mLocalDeviceIdleController.setJobsActive(active);
            }
        }
    }

    void reportAppUsage(java.lang.String packageName, int userId) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JobSchedulerService(android.content.Context context) {
        int i;
        super(context);
        java.lang.Object[] objArr = 0;
        this.mJobSchedulerServiceWrapper = new com.android.server.job.JobSchedulerService.JobSchedulerServiceWrapper();
        this.mJobSchedulerServiceExt = (com.android.server.job.IJobSchedulerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.job.IJobSchedulerServiceExt.class).create();
        this.mLock = new java.lang.Object();
        this.mJobPackageTracker = new com.android.server.job.JobPackageTracker();
        this.mCloudMediaProviderPackages = new android.util.SparseArray<>();
        this.mUserVisibleJobObservers = new android.os.RemoteCallbackList<>();
        this.mPermissionCache = new android.util.SparseArray<>();
        this.mPendingJobQueue = new com.android.server.job.PendingJobQueue();
        this.mStartedUsers = libcore.util.EmptyArray.INT;
        this.mLastCompletedJobIndex = 0;
        this.mLastCompletedJobs = new com.android.server.job.controllers.JobStatus[20];
        this.mLastCompletedJobTimeElapsed = new long[20];
        this.mLastCancelledJobIndex = 0;
        if (!DEBUG) {
            i = 0;
        } else {
            i = 20;
        }
        this.mLastCancelledJobs = new com.android.server.job.controllers.JobStatus[i];
        this.mLastCancelledJobTimeElapsed = new long[DEBUG ? 20 : 0];
        this.mUidBiasOverride = new android.util.SparseIntArray();
        this.mUidCapabilities = new android.util.SparseIntArray();
        this.mUidProcStates = new android.util.SparseIntArray();
        this.mBackingUpUids = new android.util.SparseBooleanArray();
        this.mDebuggableApps = new android.util.ArrayMap<>();
        this.mUidToPackageCache = new android.util.SparseSetArray<>();
        this.mChangedJobList = new android.util.ArraySet<>();
        this.mPendingJobReasonCache = new android.util.SparseArrayMap<>();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.job.JobSchedulerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsForUid;
                java.lang.String action = intent.getAction();
                if (com.android.server.job.JobSchedulerService.DEBUG) {
                    android.util.Slog.d("JobScheduler", "Receieved: " + action);
                }
                java.lang.String pkgName = com.android.server.job.JobSchedulerService.getPackageName(intent);
                int pkgUid = intent.getIntExtra("android.intent.extra.UID", -1);
                int i2 = 0;
                if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
                    synchronized (com.android.server.job.JobSchedulerService.this.mPermissionCache) {
                        com.android.server.job.JobSchedulerService.this.mPermissionCache.remove(pkgUid);
                    }
                    if (pkgName != null && pkgUid != -1) {
                        java.lang.String[] changedComponents = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
                        if (changedComponents != null) {
                            int length = changedComponents.length;
                            while (true) {
                                if (i2 >= length) {
                                    break;
                                }
                                java.lang.String component = changedComponents[i2];
                                if (!component.equals(pkgName)) {
                                    i2++;
                                } else {
                                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                                        android.util.Slog.d("JobScheduler", "Package state change: " + pkgName);
                                    }
                                    try {
                                        int userId = android.os.UserHandle.getUserId(pkgUid);
                                        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
                                        int state = pm.getApplicationEnabledSetting(pkgName, userId);
                                        if (state == 2 || state == 3) {
                                            if (com.android.server.job.JobSchedulerService.DEBUG) {
                                                android.util.Slog.d("JobScheduler", "Removing jobs for package " + pkgName + " in user " + userId);
                                            }
                                            try {
                                                synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                                    try {
                                                        com.android.server.job.JobSchedulerService.this.cancelJobsForPackageAndUidLocked(pkgName, pkgUid, true, true, 13, 7, "app disabled");
                                                    } catch (java.lang.Throwable th) {
                                                        th = th;
                                                        throw th;
                                                    }
                                                }
                                            } catch (java.lang.Throwable th2) {
                                                th = th2;
                                            }
                                            throw th;
                                        }
                                    } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
                                    }
                                }
                            }
                            if (com.android.server.job.JobSchedulerService.DEBUG) {
                                android.util.Slog.d("JobScheduler", "Something in " + pkgName + " changed. Reevaluating controller states.");
                            }
                            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                for (int c = com.android.server.job.JobSchedulerService.this.mControllers.size() - 1; c >= 0; c--) {
                                    com.android.server.job.JobSchedulerService.this.mControllers.get(c).reevaluateStateLocked(pkgUid);
                                }
                            }
                            return;
                        }
                        return;
                    }
                    android.util.Slog.w("JobScheduler", "PACKAGE_CHANGED for " + pkgName + " / uid " + pkgUid);
                    return;
                }
                if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                    synchronized (com.android.server.job.JobSchedulerService.this.mPermissionCache) {
                        com.android.server.job.JobSchedulerService.this.mPermissionCache.remove(pkgUid);
                    }
                    if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                            com.android.server.job.JobSchedulerService.this.mUidToPackageCache.remove(pkgUid);
                        }
                        return;
                    }
                    return;
                }
                if ("android.intent.action.PACKAGE_FULLY_REMOVED".equals(action)) {
                    synchronized (com.android.server.job.JobSchedulerService.this.mPermissionCache) {
                        com.android.server.job.JobSchedulerService.this.mPermissionCache.remove(pkgUid);
                    }
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Removing jobs for " + pkgName + " (uid=" + pkgUid + ")");
                    }
                    synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                        com.android.server.job.JobSchedulerService.this.mUidToPackageCache.remove(pkgUid);
                        com.android.server.job.JobSchedulerService.this.cancelJobsForPackageAndUidLocked(pkgName, pkgUid, true, true, 13, 7, "app uninstalled");
                        for (int c2 = 0; c2 < com.android.server.job.JobSchedulerService.this.mControllers.size(); c2++) {
                            com.android.server.job.JobSchedulerService.this.mControllers.get(c2).onAppRemovedLocked(pkgName, pkgUid);
                        }
                        com.android.server.job.JobSchedulerService.this.mDebuggableApps.remove(pkgName);
                        com.android.server.job.JobSchedulerService.this.mConcurrencyManager.onAppRemovedLocked(pkgName, pkgUid);
                        com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.hookReceivePackageRemove(com.android.server.job.JobSchedulerService.this, pkgUid, pkgName);
                    }
                    return;
                }
                if ("android.intent.action.UID_REMOVED".equals(action)) {
                    if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                            com.android.server.job.JobSchedulerService.this.mUidBiasOverride.delete(pkgUid);
                            com.android.server.job.JobSchedulerService.this.mUidCapabilities.delete(pkgUid);
                            com.android.server.job.JobSchedulerService.this.mUidProcStates.delete(pkgUid);
                        }
                        return;
                    }
                    return;
                }
                if ("android.intent.action.USER_ADDED".equals(action)) {
                    int userId2 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                        for (int c3 = 0; c3 < com.android.server.job.JobSchedulerService.this.mControllers.size(); c3++) {
                            com.android.server.job.JobSchedulerService.this.mControllers.get(c3).onUserAddedLocked(userId2);
                        }
                    }
                    return;
                }
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    int userId3 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Removing jobs for user: " + userId3);
                    }
                    synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                        com.android.server.job.JobSchedulerService.this.mUidToPackageCache.clear();
                        com.android.server.job.JobSchedulerService.this.cancelJobsForUserLocked(userId3);
                        for (int c4 = 0; c4 < com.android.server.job.JobSchedulerService.this.mControllers.size(); c4++) {
                            com.android.server.job.JobSchedulerService.this.mControllers.get(c4).onUserRemovedLocked(userId3);
                        }
                    }
                    com.android.server.job.JobSchedulerService.this.mConcurrencyManager.onUserRemoved(userId3);
                    synchronized (com.android.server.job.JobSchedulerService.this.mPermissionCache) {
                        for (int u = com.android.server.job.JobSchedulerService.this.mPermissionCache.size() - 1; u >= 0; u--) {
                            int uid = com.android.server.job.JobSchedulerService.this.mPermissionCache.keyAt(u);
                            if (userId3 == android.os.UserHandle.getUserId(uid)) {
                                com.android.server.job.JobSchedulerService.this.mPermissionCache.removeAt(u);
                            }
                        }
                    }
                    return;
                }
                if ("android.intent.action.QUERY_PACKAGE_RESTART".equals(action)) {
                    if (pkgUid != -1) {
                        synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                            jobsForUid = com.android.server.job.JobSchedulerService.this.mJobs.getJobsByUid(pkgUid);
                        }
                        for (int i3 = jobsForUid.size() - 1; i3 >= 0; i3--) {
                            if (jobsForUid.valueAt(i3).getSourcePackageName().equals(pkgName)) {
                                if (com.android.server.job.JobSchedulerService.DEBUG) {
                                    android.util.Slog.d("JobScheduler", "Restart query: package " + pkgName + " at uid " + pkgUid + " has jobs");
                                }
                                setResultCode(-1);
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                if ("android.intent.action.PACKAGE_RESTARTED".equals(action) && pkgUid != -1) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Removing jobs for pkg " + pkgName + " at uid " + pkgUid);
                    }
                    if (!com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.ignoreJobRemoved(com.android.server.job.JobSchedulerService.this, pkgName, pkgUid)) {
                        return;
                    }
                    synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                        com.android.server.job.JobSchedulerService.this.cancelJobsForPackageAndUidLocked(pkgName, pkgUid, true, false, 13, 0, "app force stopped");
                    }
                    com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.hookReceivePackageRestarted(com.android.server.job.JobSchedulerService.this, pkgUid, pkgName);
                }
            }
        };
        this.mUidObserver = new android.app.UidObserver() { // from class: com.android.server.job.JobSchedulerService.4
            public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.argi1 = uid;
                args.argi2 = procState;
                args.argi3 = capability;
                com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(4, args).sendToTarget();
            }

            public void onUidGone(int i2, boolean z) {
                com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(5, i2, z ? 1 : 0).sendToTarget();
            }

            public void onUidActive(int uid) {
                com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(6, uid, 0).sendToTarget();
            }

            public void onUidIdle(int i2, boolean z) {
                com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(7, i2, z ? 1 : 0).sendToTarget();
            }
        };
        this.mIsUidActivePredicate = new java.util.function.Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.isUidActive(((java.lang.Integer) obj).intValue());
            }
        };
        this.mCancelJobDueToUserRemovalConsumer = new java.util.function.Consumer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((com.android.server.job.controllers.JobStatus) obj);
            }
        };
        this.mTimeSetReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.job.JobSchedulerService.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.intent.action.TIME_SET".equals(intent.getAction()) && com.android.server.job.JobSchedulerService.this.mJobs.clockNowValidToInflate(com.android.server.job.JobSchedulerService.sSystemClock.millis())) {
                    android.util.Slog.i("JobScheduler", "RTC now valid; recalculating persisted job windows");
                    context2.unregisterReceiver(this);
                    com.android.server.job.JobSchedulerService.this.mJobs.runWorkAsync(com.android.server.job.JobSchedulerService.this.mJobTimeUpdater);
                }
            }
        };
        this.mJobTimeUpdater = new java.lang.Runnable() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$3();
            }
        };
        this.mReadyQueueFunctor = new com.android.server.job.JobSchedulerService.ReadyJobQueueFunctor();
        this.mMaybeQueueFunctor = new com.android.server.job.JobSchedulerService.MaybeReadyJobQueueFunctor();
        this.mLocalPM = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
        this.mHandler = new com.android.server.job.JobSchedulerService.JobHandler(com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mConstants = new com.android.server.job.JobSchedulerService.Constants();
        this.mConstantsObserver = new com.android.server.job.JobSchedulerService.ConstantsObserver();
        this.mJobSchedulerStub = new com.android.server.job.JobSchedulerService.JobSchedulerStub();
        this.mConcurrencyManager = new com.android.server.job.JobConcurrencyManager(this);
        this.mStandbyTracker = new com.android.server.job.JobSchedulerService.StandbyTracker();
        sUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        this.mQuotaTracker = new com.android.server.utils.quota.CountQuotaTracker(context, new com.android.server.utils.quota.Categorizer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda7
            @Override // com.android.server.utils.quota.Categorizer
            public final com.android.server.utils.quota.Category getCategory(int i2, java.lang.String str, java.lang.String str2) {
                return this.f$0.lambda$new$2(i2, str, str2);
            }
        });
        updateQuotaTracker();
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED, 1, 60000L);
        this.mQuotaTracker.setCountLimit(QUOTA_TRACKER_CATEGORY_DISABLED, Integer.MAX_VALUE, 60000L);
        this.mAppStandbyInternal = (com.android.server.usage.AppStandbyInternal) com.android.server.LocalServices.getService(com.android.server.usage.AppStandbyInternal.class);
        this.mAppStandbyInternal.addListener(this.mStandbyTracker);
        this.mBatteryStatsInternal = (android.os.BatteryStatsInternal) com.android.server.LocalServices.getService(android.os.BatteryStatsInternal.class);
        publishLocalService(com.android.server.job.JobSchedulerInternal.class, new com.android.server.job.JobSchedulerService.LocalService());
        this.mJobStoreLoadedLatch = new java.util.concurrent.CountDownLatch(1);
        this.mJobs = com.android.server.job.JobStore.get(this);
        this.mJobs.initAsync(this.mJobStoreLoadedLatch);
        this.mJobSchedulerServiceExt.onHookPreInit(this, this.mHandler, context);
        this.mBatteryStateTracker = new com.android.server.job.JobSchedulerService.BatteryStateTracker();
        this.mBatteryStateTracker.startTracking();
        this.mStartControllerTrackingLatch = new java.util.concurrent.CountDownLatch(1);
        this.mControllers = new java.util.ArrayList();
        this.mPrefetchController = new com.android.server.job.controllers.PrefetchController(this);
        this.mControllers.add(this.mPrefetchController);
        this.mFlexibilityController = new com.android.server.job.controllers.FlexibilityController(this, this.mPrefetchController);
        this.mControllers.add(this.mFlexibilityController);
        this.mConnectivityController = new com.android.server.job.controllers.ConnectivityController(this, this.mFlexibilityController);
        this.mControllers.add(this.mConnectivityController);
        this.mControllers.add(new com.android.server.job.controllers.TimeController(this));
        com.android.server.job.controllers.IdleController idleController = new com.android.server.job.controllers.IdleController(this, this.mFlexibilityController);
        this.mControllers.add(idleController);
        com.android.server.job.controllers.BatteryController batteryController = new com.android.server.job.controllers.BatteryController(this, this.mFlexibilityController);
        this.mControllers.add(batteryController);
        this.mStorageController = new com.android.server.job.controllers.StorageController(this);
        this.mControllers.add(this.mStorageController);
        com.android.server.job.controllers.BackgroundJobsController backgroundJobsController = new com.android.server.job.controllers.BackgroundJobsController(this);
        this.mControllers.add(backgroundJobsController);
        this.mControllers.add(new com.android.server.job.controllers.ContentObserverController(this));
        this.mDeviceIdleJobsController = new com.android.server.job.controllers.DeviceIdleJobsController(this);
        this.mControllers.add(this.mDeviceIdleJobsController);
        this.mQuotaController = new com.android.server.job.controllers.QuotaController(this, backgroundJobsController, this.mConnectivityController);
        this.mControllers.add(this.mQuotaController);
        this.mControllers.add(new com.android.server.job.controllers.ComponentController(this));
        startControllerTrackingAsync();
        this.mRestrictiveControllers = new java.util.ArrayList();
        this.mRestrictiveControllers.add(batteryController);
        this.mRestrictiveControllers.add(this.mConnectivityController);
        this.mRestrictiveControllers.add(idleController);
        this.mJobRestrictions = new java.util.ArrayList();
        this.mJobRestrictions.add(new com.android.server.job.restrictions.ThermalStatusRestriction(this));
        this.mJobSchedulerServiceExt.onHookEndInit(this, this.mControllers, this.mJobRestrictions);
        if (!this.mJobs.jobTimesInflatedValid()) {
            android.util.Slog.w("JobScheduler", "!!! RTC not yet good; tracking time updates for job scheduling");
            context.registerReceiver(this.mTimeSetReceiver, new android.content.IntentFilter("android.intent.action.TIME_SET"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.utils.quota.Category lambda$new$2(int userId, java.lang.String packageName, java.lang.String tag) {
        if (QUOTA_TRACKER_TIMEOUT_UIJ_TAG.equals(tag)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_UIJ;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_TIMEOUT_EJ_TAG.equals(tag)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_EJ;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_TIMEOUT_REG_TAG.equals(tag)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_REG;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_TIMEOUT_TOTAL_TAG.equals(tag)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_TIMEOUT_TOTAL;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_ANR_TAG.equals(tag)) {
            if (this.mConstants.ENABLE_EXECUTION_SAFEGUARDS_UDC) {
                return QUOTA_TRACKER_CATEGORY_ANR;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG.equals(tag)) {
            if (this.mConstants.ENABLE_API_QUOTAS) {
                return QUOTA_TRACKER_CATEGORY_SCHEDULE_PERSISTED;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        if (QUOTA_TRACKER_SCHEDULE_LOGGED.equals(tag)) {
            if (this.mConstants.ENABLE_API_QUOTAS) {
                return QUOTA_TRACKER_CATEGORY_SCHEDULE_LOGGED;
            }
            return QUOTA_TRACKER_CATEGORY_DISABLED;
        }
        android.util.Slog.wtf("JobScheduler", "Unexpected category tag: " + tag);
        return QUOTA_TRACKER_CATEGORY_DISABLED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        android.os.Process.setThreadPriority(-2);
        java.util.ArrayList<com.android.server.job.controllers.JobStatus> toRemove = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.job.controllers.JobStatus> toAdd = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            getJobStore().getRtcCorrectedJobsLocked(toAdd, toRemove);
            int N = toAdd.size();
            for (int i = 0; i < N; i++) {
                com.android.server.job.controllers.JobStatus oldJob = toRemove.get(i);
                com.android.server.job.controllers.JobStatus newJob = toAdd.get(i);
                if (DEBUG) {
                    android.util.Slog.v("JobScheduler", "  replacing " + oldJob + " with " + newJob);
                }
                cancelJobImplLocked(oldJob, newJob, 14, 9, "deferred rtc calculation");
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("jobscheduler", this.mJobSchedulerStub);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (480 == phase) {
            try {
                this.mStartControllerTrackingLatch.await();
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.e("JobScheduler", "Couldn't wait on controller tracking start latch");
            }
            try {
                this.mJobStoreLoadedLatch.await();
                return;
            } catch (java.lang.InterruptedException e2) {
                android.util.Slog.e("JobScheduler", "Couldn't wait on job store loading latch");
                return;
            }
        }
        if (500 == phase) {
            this.mJobSchedulerServiceExt.onHookSystemReady();
            this.mConstantsObserver.start();
            for (int i = this.mControllers.size() - 1; i >= 0; i--) {
                this.mControllers.get(i).onSystemServicesReady();
            }
            this.mAppStateTracker = (com.android.server.AppStateTrackerImpl) java.util.Objects.requireNonNull((com.android.server.AppStateTracker) com.android.server.LocalServices.getService(com.android.server.AppStateTracker.class));
            ((android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class)).registerCloudProviderChangeListener(new com.android.server.job.JobSchedulerService.CloudProviderChangeListener());
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
            filter.addAction("android.intent.action.PACKAGE_ADDED");
            filter.addAction("android.intent.action.PACKAGE_CHANGED");
            filter.addAction("android.intent.action.PACKAGE_RESTARTED");
            filter.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
            filter.addDataScheme("package");
            getContext().registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, filter, null, null);
            android.content.IntentFilter uidFilter = new android.content.IntentFilter("android.intent.action.UID_REMOVED");
            getContext().registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, uidFilter, null, null);
            android.content.IntentFilter userFilter = new android.content.IntentFilter("android.intent.action.USER_REMOVED");
            userFilter.addAction("android.intent.action.USER_ADDED");
            getContext().registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, userFilter, null, null);
            try {
                android.app.ActivityManager.getService().registerUidObserver(this.mUidObserver, 15, -1, (java.lang.String) null);
            } catch (android.os.RemoteException e3) {
            }
            this.mConcurrencyManager.onSystemReady();
            cancelJobsForNonExistentUsers();
            for (int i2 = this.mJobRestrictions.size() - 1; i2 >= 0; i2--) {
                this.mJobRestrictions.get(i2).onSystemServicesReady();
            }
            return;
        }
        if (phase == 600) {
            synchronized (this.mLock) {
                this.mReadyToRock = true;
                this.mLocalDeviceIdleController = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
                this.mConcurrencyManager.onThirdPartyAppsCanStart();
                this.mJobs.forEachJob(new java.util.function.Consumer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$onBootPhase$4((com.android.server.job.controllers.JobStatus) obj);
                    }
                });
                if (!com.android.server.job.Flags.doNotForceRushExecutionAtBoot()) {
                    this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$4(com.android.server.job.controllers.JobStatus job) {
        for (int controller = 0; controller < this.mControllers.size(); controller++) {
            com.android.server.job.controllers.StateController sc = this.mControllers.get(controller);
            sc.maybeStartTrackingJobLocked(job, null);
        }
    }

    private void startControllerTrackingAsync() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startControllerTrackingAsync$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startControllerTrackingAsync$5() {
        synchronized (this.mLock) {
            for (int i = this.mControllers.size() - 1; i >= 0; i--) {
                this.mControllers.get(i).startTrackingLocked();
            }
        }
        this.mStartControllerTrackingLatch.countDown();
    }

    private void startTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if (!jobStatus.isPreparedLocked()) {
            android.util.Slog.wtf("JobScheduler", "Not yet prepared when started tracking: " + jobStatus);
        }
        jobStatus.enqueueTime = sElapsedRealtimeClock.millis();
        boolean update = lastJob != null;
        this.mJobs.add(jobStatus);
        resetPendingJobReasonCache(jobStatus);
        if (this.mReadyToRock) {
            for (int i = 0; i < this.mControllers.size(); i++) {
                com.android.server.job.controllers.StateController controller = this.mControllers.get(i);
                if (update) {
                    controller.maybeStopTrackingJobLocked(jobStatus, null);
                }
                controller.maybeStartTrackingJobLocked(jobStatus, lastJob);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean stopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob, boolean removeFromPersisted) {
        jobStatus.stopTrackingJobLocked(incomingJob);
        synchronized (this.mPendingJobReasonCache) {
            android.util.SparseIntArray reasonCache = (android.util.SparseIntArray) this.mPendingJobReasonCache.get(jobStatus.getUid(), jobStatus.getNamespace());
            if (reasonCache != null) {
                reasonCache.delete(jobStatus.getJobId());
            }
        }
        boolean removed = this.mJobs.remove(jobStatus, removeFromPersisted);
        if (!removed) {
            android.util.Slog.w("JobScheduler", "Job didn't exist in JobStore: #" + jobStatus.getSourcePackageName() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + jobStatus.getJobId());
        }
        if (this.mReadyToRock) {
            for (int i = 0; i < this.mControllers.size(); i++) {
                com.android.server.job.controllers.StateController controller = this.mControllers.get(i);
                controller.maybeStopTrackingJobLocked(jobStatus, incomingJob);
            }
        }
        return removed;
    }

    void resetPendingJobReasonCache(com.android.server.job.controllers.JobStatus jobStatus) {
        synchronized (this.mPendingJobReasonCache) {
            android.util.SparseIntArray reasons = (android.util.SparseIntArray) this.mPendingJobReasonCache.get(jobStatus.getUid(), jobStatus.getNamespace());
            if (reasons != null) {
                reasons.delete(jobStatus.getJobId());
            }
        }
    }

    public boolean isCurrentlyRunningLocked(com.android.server.job.controllers.JobStatus job) {
        return this.mConcurrencyManager.isJobRunningLocked(job);
    }

    public boolean isJobInOvertimeLocked(com.android.server.job.controllers.JobStatus job) {
        return this.mConcurrencyManager.isJobInOvertimeLocked(job);
    }

    private void noteJobPending(com.android.server.job.controllers.JobStatus job) {
        this.mJobPackageTracker.notePending(job);
    }

    void noteJobsPending(android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs) {
        for (int i = jobs.size() - 1; i >= 0; i--) {
            noteJobPending(jobs.valueAt(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void noteJobNonPending(com.android.server.job.controllers.JobStatus job) {
        this.mJobPackageTracker.noteNonpending(job);
    }

    private void clearPendingJobQueue() {
        this.mPendingJobQueue.resetIterator();
        while (true) {
            com.android.server.job.controllers.JobStatus job = this.mPendingJobQueue.next();
            if (job != null) {
                noteJobNonPending(job);
            } else {
                this.mPendingJobQueue.clear();
                return;
            }
        }
    }

    com.android.server.job.controllers.JobStatus getRescheduleJobForFailureLocked(com.android.server.job.controllers.JobStatus failureToReschedule, int stopReason, int internalStopReason) {
        long backoff;
        long delayMillis;
        if (internalStopReason == 11 && failureToReschedule.isUserVisibleJob()) {
            android.util.Slog.i("JobScheduler", "Dropping " + failureToReschedule.toShortString() + " because of user stop");
            return null;
        }
        long elapsedNowMillis = sElapsedRealtimeClock.millis();
        android.app.job.JobInfo job = failureToReschedule.getJob();
        long initialBackoffMillis = job.getInitialBackoffMillis();
        int numFailures = failureToReschedule.getNumFailures();
        int numSystemStops = failureToReschedule.getNumSystemStops();
        if (internalStopReason == 10 || internalStopReason == 3 || internalStopReason == 12 || stopReason == 13) {
            numFailures++;
        } else {
            numSystemStops++;
        }
        int backoffAttempts = (numSystemStops / this.mConstants.SYSTEM_STOP_TO_FAILURE_RATIO) + numFailures;
        if (backoffAttempts == 0) {
            delayMillis = 0;
        } else {
            switch (job.getBackoffPolicy()) {
                case 0:
                    long backoff2 = initialBackoffMillis;
                    if (backoff2 < this.mConstants.MIN_LINEAR_BACKOFF_TIME_MS) {
                        backoff2 = this.mConstants.MIN_LINEAR_BACKOFF_TIME_MS;
                    }
                    backoff = backoff2 * ((long) backoffAttempts);
                    break;
                default:
                    if (DEBUG) {
                        android.util.Slog.v("JobScheduler", "Unrecognised back-off policy, defaulting to exponential.");
                        break;
                    }
                case 1:
                    long backoff3 = initialBackoffMillis;
                    if (backoff3 < this.mConstants.MIN_EXP_BACKOFF_TIME_MS) {
                        backoff3 = this.mConstants.MIN_EXP_BACKOFF_TIME_MS;
                    }
                    backoff = (long) java.lang.Math.scalb(backoff3, backoffAttempts - 1);
                    break;
            }
            long delayMillis2 = java.lang.Math.min(backoff, 18000000L);
            delayMillis = elapsedNowMillis + this.mJobSchedulerServiceExt.translateDelayTime(job, delayMillis2);
        }
        com.android.server.job.controllers.JobStatus newJob = new com.android.server.job.controllers.JobStatus(failureToReschedule, delayMillis, Long.MAX_VALUE, numFailures, numSystemStops, failureToReschedule.getLastSuccessfulRunTime(), sSystemClock.millis(), failureToReschedule.getCumulativeExecutionTimeMs());
        if (stopReason == 13) {
            newJob.addInternalFlags(2);
        }
        long cumulativeExecutionTimeMs = newJob.getCumulativeExecutionTimeMs();
        long earliestRuntimeMs = this.mConstants.RUNTIME_CUMULATIVE_UI_LIMIT_MS;
        if (cumulativeExecutionTimeMs >= earliestRuntimeMs && newJob.shouldTreatAsUserInitiatedJob()) {
            newJob.addInternalFlags(4);
        }
        if (job.isPeriodic()) {
            newJob.setOriginalLatestRunTimeElapsed(failureToReschedule.getOriginalLatestRunTimeElapsed());
        }
        for (int ic = 0; ic < this.mControllers.size(); ic++) {
            com.android.server.job.controllers.StateController controller = this.mControllers.get(ic);
            controller.rescheduleForFailureLocked(newJob, failureToReschedule);
        }
        return newJob;
    }

    com.android.server.job.controllers.JobStatus getRescheduleJobForPeriodic(com.android.server.job.controllers.JobStatus periodicToReschedule) {
        long rescheduleBuffer;
        long olrte;
        long rescheduleBuffer2;
        long elapsedNow = sElapsedRealtimeClock.millis();
        long period = java.lang.Math.max(android.app.job.JobInfo.getMinPeriodMillis(), java.lang.Math.min(31536000000L, periodicToReschedule.getJob().getIntervalMillis()));
        long flex = java.lang.Math.max(android.app.job.JobInfo.getMinFlexMillis(), java.lang.Math.min(period, periodicToReschedule.getJob().getFlexMillis()));
        long olrte2 = periodicToReschedule.getOriginalLatestRunTimeElapsed();
        if (olrte2 < 0 || olrte2 == Long.MAX_VALUE) {
            android.util.Slog.wtf("JobScheduler", "Invalid periodic job original latest run time: " + olrte2);
            olrte2 = elapsedNow;
        }
        long latestRunTimeElapsed = olrte2;
        long diffMs = java.lang.Math.abs(elapsedNow - latestRunTimeElapsed);
        if (elapsedNow > latestRunTimeElapsed) {
            if (DEBUG) {
                rescheduleBuffer2 = 0;
                android.util.Slog.i("JobScheduler", "Periodic job ran after its intended window by " + diffMs + " ms");
            } else {
                rescheduleBuffer2 = 0;
            }
            long numSkippedWindows = (diffMs / period) + 1;
            if (period != flex && (period - flex) - (diffMs % period) <= flex / 6) {
                if (DEBUG) {
                    android.util.Slog.d("JobScheduler", "Custom flex job ran too close to next window.");
                }
                numSkippedWindows++;
            }
            long newLatestRuntimeElapsed = latestRunTimeElapsed + (period * numSkippedWindows);
            olrte = newLatestRuntimeElapsed;
            rescheduleBuffer = rescheduleBuffer2;
        } else {
            long rescheduleBuffer3 = latestRunTimeElapsed + period;
            if (diffMs < 1800000 && diffMs < period / 6) {
                rescheduleBuffer = java.lang.Math.min(1800000L, (period / 6) - diffMs);
                olrte = rescheduleBuffer3;
            } else {
                rescheduleBuffer = 0;
                olrte = rescheduleBuffer3;
            }
        }
        if (olrte < elapsedNow) {
            android.util.Slog.wtf("JobScheduler", "Rescheduling calculated latest runtime in the past: " + olrte);
            return new com.android.server.job.controllers.JobStatus(periodicToReschedule, (elapsedNow + period) - flex, elapsedNow + period, 0, 0, sSystemClock.millis(), periodicToReschedule.getLastFailedRunTime(), 0L);
        }
        long newEarliestRunTimeElapsed = olrte - java.lang.Math.min(flex, period - rescheduleBuffer);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "Rescheduling executed periodic. New execution window [" + (newEarliestRunTimeElapsed / 1000) + ", " + (olrte / 1000) + "]s");
        }
        return new com.android.server.job.controllers.JobStatus(periodicToReschedule, newEarliestRunTimeElapsed, olrte, 0, 0, sSystemClock.millis(), periodicToReschedule.getLastFailedRunTime(), 0L);
    }

    void maybeProcessBuggyJob(com.android.server.job.controllers.JobStatus jobStatus, int debugStopReason) {
        java.lang.String str;
        boolean jobTimedOut = debugStopReason == 3;
        if (!jobTimedOut && jobStatus.madeActive > 0) {
            long executionDurationMs = sUptimeMillisClock.millis() - jobStatus.madeActive;
            if (jobStatus.startedAsUserInitiatedJob) {
                jobTimedOut = executionDurationMs >= this.mConstants.RUNTIME_MIN_UI_GUARANTEE_MS;
            } else if (jobStatus.startedAsExpeditedJob) {
                jobTimedOut = executionDurationMs >= this.mConstants.RUNTIME_MIN_EJ_GUARANTEE_MS;
            } else {
                jobTimedOut = executionDurationMs >= this.mConstants.RUNTIME_MIN_GUARANTEE_MS;
            }
        }
        if (jobTimedOut) {
            int userId = jobStatus.getTimeoutBlameUserId();
            java.lang.String pkg = jobStatus.getTimeoutBlamePackageName();
            com.android.server.utils.quota.CountQuotaTracker countQuotaTracker = this.mQuotaTracker;
            if (jobStatus.startedAsUserInitiatedJob) {
                str = QUOTA_TRACKER_TIMEOUT_UIJ_TAG;
            } else if (jobStatus.startedAsExpeditedJob) {
                str = QUOTA_TRACKER_TIMEOUT_EJ_TAG;
            } else {
                str = QUOTA_TRACKER_TIMEOUT_REG_TAG;
            }
            countQuotaTracker.noteEvent(userId, pkg, str);
            if (!this.mQuotaTracker.noteEvent(userId, pkg, QUOTA_TRACKER_TIMEOUT_TOTAL_TAG)) {
                this.mAppStandbyInternal.restrictApp(pkg, userId, 4);
            }
        }
        if (debugStopReason == 12) {
            int callingUserId = jobStatus.getUserId();
            java.lang.String callingPkg = jobStatus.getServiceComponent().getPackageName();
            if (!this.mQuotaTracker.noteEvent(callingUserId, callingPkg, QUOTA_TRACKER_ANR_TAG)) {
                this.mAppStandbyInternal.restrictApp(callingPkg, callingUserId, 4);
            }
        }
    }

    @Override // com.android.server.job.JobCompletedListener
    public void onJobCompletedLocked(com.android.server.job.controllers.JobStatus jobStatus, int stopReason, int debugStopReason, boolean needsReschedule) {
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "Completed " + jobStatus + ", reason=" + debugStopReason + ", reschedule=" + needsReschedule);
        }
        this.mLastCompletedJobs[this.mLastCompletedJobIndex] = jobStatus;
        this.mLastCompletedJobTimeElapsed[this.mLastCompletedJobIndex] = sElapsedRealtimeClock.millis();
        this.mLastCompletedJobIndex = (this.mLastCompletedJobIndex + 1) % 20;
        maybeProcessBuggyJob(jobStatus, debugStopReason);
        if (debugStopReason == 7 || debugStopReason == 8) {
            jobStatus.unprepareLocked();
            reportActiveLocked();
            return;
        }
        com.android.server.job.controllers.JobStatus rescheduledJob = needsReschedule ? getRescheduleJobForFailureLocked(jobStatus, stopReason, debugStopReason) : null;
        if (rescheduledJob != null && !rescheduledJob.shouldTreatAsUserInitiatedJob() && (debugStopReason == 3 || debugStopReason == 2)) {
            rescheduledJob.disallowRunInBatterySaverAndDoze();
        }
        if (!stopTrackingJobLocked(jobStatus, rescheduledJob, !jobStatus.getJob().isPeriodic())) {
            if (DEBUG) {
                android.util.Slog.d("JobScheduler", "Could not find job to remove. Was job removed while executing?");
            }
            com.android.server.job.controllers.JobStatus newJs = this.mJobs.getJobByUidAndJobId(jobStatus.getUid(), jobStatus.getNamespace(), jobStatus.getJobId());
            if (newJs != null) {
                this.mHandler.obtainMessage(0, newJs).sendToTarget();
                return;
            }
            return;
        }
        if (rescheduledJob != null) {
            try {
                rescheduledJob.prepareLocked();
            } catch (java.lang.SecurityException e) {
                android.util.Slog.w("JobScheduler", "Unable to regrant job permissions for " + rescheduledJob);
            }
            startTrackingJobLocked(rescheduledJob, jobStatus);
        } else if (jobStatus.getJob().isPeriodic()) {
            com.android.server.job.controllers.JobStatus rescheduledPeriodic = getRescheduleJobForPeriodic(jobStatus);
            try {
                rescheduledPeriodic.prepareLocked();
            } catch (java.lang.SecurityException e2) {
                android.util.Slog.w("JobScheduler", "Unable to regrant job permissions for " + rescheduledPeriodic);
            }
            startTrackingJobLocked(rescheduledPeriodic, jobStatus);
        }
        jobStatus.unprepareLocked();
        reportActiveLocked();
    }

    @Override // com.android.server.job.StateChangedListener
    public void onControllerStateChanged(android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs) {
        if (changedJobs == null) {
            this.mJobSchedulerServiceExt.onControllerStateChangedWithEmpty();
            this.mHandler.obtainMessage(1).sendToTarget();
            synchronized (this.mPendingJobReasonCache) {
                this.mPendingJobReasonCache.clear();
            }
            return;
        }
        if (changedJobs.size() > 0) {
            synchronized (this.mLock) {
                this.mChangedJobList.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) changedJobs);
            }
            this.mHandler.obtainMessage(8).sendToTarget();
            synchronized (this.mPendingJobReasonCache) {
                for (int i = changedJobs.size() - 1; i >= 0; i--) {
                    com.android.server.job.controllers.JobStatus job = changedJobs.valueAt(i);
                    resetPendingJobReasonCache(job);
                }
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onRestrictionStateChanged(com.android.server.job.restrictions.JobRestriction restriction, boolean stopOvertimeJobs) {
        this.mJobSchedulerServiceExt.onRestrictionStateChanged(restriction);
        this.mHandler.obtainMessage(1).sendToTarget();
        if (stopOvertimeJobs) {
            synchronized (this.mLock) {
                this.mConcurrencyManager.maybeStopOvertimeJobsLocked(restriction);
            }
        }
    }

    @Override // com.android.server.job.StateChangedListener
    public void onRunJobNow(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus == null) {
            this.mHandler.obtainMessage(3).sendToTarget();
        } else {
            this.mHandler.obtainMessage(0, jobStatus).sendToTarget();
        }
    }

    private final class JobHandler extends android.os.Handler {
        public JobHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                if (com.android.server.job.JobSchedulerService.this.mReadyToRock) {
                    com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, false);
                    switch (message.what) {
                        case 0:
                            com.android.server.job.controllers.JobStatus js = (com.android.server.job.controllers.JobStatus) message.obj;
                            if (js != null) {
                                if (com.android.server.job.JobSchedulerService.this.isReadyToBeExecutedLocked(js)) {
                                    com.android.server.job.JobSchedulerService.this.mJobPackageTracker.notePending(js);
                                    com.android.server.job.JobSchedulerService.this.mPendingJobQueue.add(js);
                                }
                                com.android.server.job.JobSchedulerService.this.mChangedJobList.remove(js);
                            } else {
                                android.util.Slog.e("JobScheduler", "Given null job to check individually");
                            }
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 1:
                            if (com.android.server.job.JobSchedulerService.DEBUG) {
                                android.util.Slog.d("JobScheduler", "MSG_CHECK_JOB");
                            }
                            if (com.android.server.job.JobSchedulerService.this.mReportedActive) {
                                com.android.server.job.JobSchedulerService.this.queueReadyJobsForExecutionLocked();
                            } else {
                                com.android.server.job.JobSchedulerService.this.maybeQueueReadyJobsForExecutionLocked();
                            }
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 2:
                            com.android.server.job.JobSchedulerService.this.cancelJobImplLocked((com.android.server.job.controllers.JobStatus) message.obj, null, message.arg1, 1, "app no longer allowed to run");
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 3:
                            if (com.android.server.job.JobSchedulerService.DEBUG) {
                                android.util.Slog.d("JobScheduler", "MSG_CHECK_JOB_GREEDY");
                            }
                            com.android.server.job.JobSchedulerService.this.queueReadyJobsForExecutionLocked();
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 4:
                            com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) message.obj;
                            int uid = args.argi1;
                            int procState = args.argi2;
                            int capabilities = args.argi3;
                            com.android.server.job.JobSchedulerService.this.updateUidState(uid, procState, capabilities);
                            args.recycle();
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 5:
                            int uid2 = message.arg1;
                            boolean disabled = message.arg2 != 0;
                            com.android.server.job.JobSchedulerService.this.updateUidState(uid2, 20, 0);
                            if (disabled) {
                                com.android.server.job.JobSchedulerService.this.cancelJobsForUid(uid2, true, 11, 1, "uid gone");
                            }
                            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                com.android.server.job.JobSchedulerService.this.mDeviceIdleJobsController.setUidActiveLocked(uid2, false);
                                break;
                            }
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 6:
                            int uid3 = message.arg1;
                            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                com.android.server.job.JobSchedulerService.this.mDeviceIdleJobsController.setUidActiveLocked(uid3, true);
                                break;
                            }
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 7:
                            int uid4 = message.arg1;
                            boolean disabled2 = message.arg2 != 0;
                            if (disabled2) {
                                com.android.server.job.JobSchedulerService.this.cancelJobsForUid(uid4, true, 11, 1, "app uid idle");
                            }
                            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                com.android.server.job.JobSchedulerService.this.mDeviceIdleJobsController.setUidActiveLocked(uid4, false);
                                break;
                            }
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 8:
                            if (com.android.server.job.JobSchedulerService.DEBUG) {
                                android.util.Slog.d("JobScheduler", "MSG_CHECK_CHANGED_JOB_LIST");
                            }
                            com.android.server.job.JobSchedulerService.this.checkChangedJobListLocked();
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 9:
                            com.android.internal.os.SomeArgs args2 = (com.android.internal.os.SomeArgs) message.obj;
                            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                com.android.server.job.JobSchedulerService.this.updateMediaBackupExemptionLocked(args2.argi1, (java.lang.String) args2.arg1, (java.lang.String) args2.arg2);
                                break;
                            }
                            args2.recycle();
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 10:
                            android.app.job.IUserVisibleJobObserver observer = (android.app.job.IUserVisibleJobObserver) message.obj;
                            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                                for (int i = com.android.server.job.JobSchedulerService.this.mConcurrencyManager.mActiveServices.size() - 1; i >= 0; i--) {
                                    com.android.server.job.JobServiceContext context = com.android.server.job.JobSchedulerService.this.mConcurrencyManager.mActiveServices.get(i);
                                    com.android.server.job.controllers.JobStatus jobStatus = context.getRunningJobLocked();
                                    if (jobStatus != null && jobStatus.isUserVisibleJob()) {
                                        try {
                                            observer.onUserVisibleJobStateChanged(jobStatus.getUserVisibleJobSummary(), true);
                                        } catch (android.os.RemoteException e) {
                                        }
                                    }
                                }
                                break;
                            }
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        case 11:
                            com.android.internal.os.SomeArgs args3 = (com.android.internal.os.SomeArgs) message.obj;
                            android.app.job.UserVisibleJobSummary summary = ((com.android.server.job.controllers.JobStatus) args3.arg2).getUserVisibleJobSummary();
                            boolean isRunning = args3.argi1 == 1;
                            for (int i2 = com.android.server.job.JobSchedulerService.this.mUserVisibleJobObservers.beginBroadcast() - 1; i2 >= 0; i2--) {
                                try {
                                    com.android.server.job.JobSchedulerService.this.mUserVisibleJobObservers.getBroadcastItem(i2).onUserVisibleJobStateChanged(summary, isRunning);
                                } catch (android.os.RemoteException e2) {
                                }
                            }
                            com.android.server.job.JobSchedulerService.this.mUserVisibleJobObservers.finishBroadcast();
                            args3.recycle();
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                        default:
                            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.updateJobCheckTime(message.what, true);
                            com.android.server.job.JobSchedulerService.this.maybeRunPendingJobsLocked();
                            return;
                    }
                }
            }
        }
    }

    com.android.server.job.restrictions.JobRestriction checkIfRestricted(com.android.server.job.controllers.JobStatus job) {
        for (int i = this.mJobRestrictions.size() - 1; i >= 0; i--) {
            com.android.server.job.restrictions.JobRestriction restriction = this.mJobRestrictions.get(i);
            if (restriction.isJobRestricted(job, evaluateJobBiasLocked(job))) {
                return restriction;
            }
        }
        return null;
    }

    private void stopNonReadyActiveJobsLocked() {
        this.mConcurrencyManager.stopNonReadyActiveJobsLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queueReadyJobsForExecutionLocked() {
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(8);
        this.mChangedJobList.clear();
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "queuing all ready jobs for execution:");
        }
        clearPendingJobQueue();
        stopNonReadyActiveJobsLocked();
        this.mJobs.forEachJob(this.mReadyQueueFunctor);
        this.mReadyQueueFunctor.postProcessLocked();
        if (DEBUG) {
            int queuedJobs = this.mPendingJobQueue.size();
            if (queuedJobs == 0) {
                android.util.Slog.d("JobScheduler", "No jobs pending.");
            } else {
                android.util.Slog.d("JobScheduler", queuedJobs + " jobs queued.");
            }
        }
    }

    final class ReadyJobQueueFunctor implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        final android.util.ArraySet<com.android.server.job.controllers.JobStatus> newReadyJobs = new android.util.ArraySet<>();

        ReadyJobQueueFunctor() {
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus job) {
            if (com.android.server.job.JobSchedulerService.this.isReadyToBeExecutedLocked(job)) {
                if (com.android.server.job.JobSchedulerService.DEBUG) {
                    android.util.Slog.d("JobScheduler", "    queued " + job.toShortString());
                }
                this.newReadyJobs.add(job);
                return;
            }
            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.jobQueueFunctorNotAccept(job);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postProcessLocked() {
            com.android.server.job.JobSchedulerService.this.noteJobsPending(this.newReadyJobs);
            com.android.server.job.JobSchedulerService.this.mPendingJobQueue.addAll(this.newReadyJobs);
            this.newReadyJobs.clear();
        }
    }

    final class MaybeReadyJobQueueFunctor implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        final android.util.ArrayMap<android.net.Network, android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mBatches = new android.util.ArrayMap<>();
        final java.util.List<com.android.server.job.controllers.JobStatus> runnableJobs = new java.util.ArrayList();
        final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mUnbatchedJobs = new android.util.ArraySet<>();
        final android.util.ArrayMap<android.net.Network, java.lang.Integer> mUnbatchedJobCount = new android.util.ArrayMap<>();

        public MaybeReadyJobQueueFunctor() {
            reset();
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus job) {
            int internalStopReason;
            java.lang.String debugReason;
            boolean shouldForceBatchJob;
            long timeUntilDeadlineMs;
            boolean batchingEnabled;
            boolean batchDelayExpired;
            boolean isRunning = com.android.server.job.JobSchedulerService.this.isCurrentlyRunningLocked(job);
            if (com.android.server.job.JobSchedulerService.this.isReadyToBeExecutedLocked(job, false)) {
                if (com.android.server.job.JobSchedulerService.this.mActivityManagerInternal.isAppStartModeDisabled(job.getUid(), job.getJob().getService().getPackageName())) {
                    android.util.Slog.w("JobScheduler", "Aborting job " + job.getUid() + ":" + job.getJob().toString() + " -- package not allowed to start");
                    if (isRunning) {
                        com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(2, 11, 0, job).sendToTarget();
                        return;
                    } else {
                        if (com.android.server.job.JobSchedulerService.this.mPendingJobQueue.remove(job)) {
                            com.android.server.job.JobSchedulerService.this.noteJobNonPending(job);
                            return;
                        }
                        return;
                    }
                }
                if (job.overrideState > 0) {
                    shouldForceBatchJob = false;
                } else {
                    boolean shouldForceBatchJob2 = job.shouldTreatAsExpeditedJob();
                    if (shouldForceBatchJob2 || job.shouldTreatAsUserInitiatedJob()) {
                        shouldForceBatchJob = false;
                    } else if (job.getEffectiveStandbyBucket() == 5) {
                        shouldForceBatchJob = true;
                    } else if (job.getJob().isPrefetch()) {
                        long relativelySoonCutoffTime = com.android.server.job.JobSchedulerService.sSystemClock.millis() + com.android.server.job.JobSchedulerService.this.mConstants.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS;
                        shouldForceBatchJob = com.android.server.job.JobSchedulerService.this.mPrefetchController.getNextEstimatedLaunchTimeLocked(job) > relativelySoonCutoffTime;
                    } else if (job.getNumPreviousAttempts() > 0) {
                        shouldForceBatchJob = false;
                    } else {
                        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                        if (job.hasDeadlineConstraint()) {
                            timeUntilDeadlineMs = job.getLatestRunTimeElapsed() - nowElapsed;
                        } else {
                            timeUntilDeadlineMs = Long.MAX_VALUE;
                        }
                        if (com.android.server.job.Flags.batchConnectivityJobsPerNetwork() && job.hasConnectivityConstraint()) {
                            boolean batchDelayExpired2 = job.getFirstForceBatchedTimeElapsed() > 0 && nowElapsed - job.getFirstForceBatchedTimeElapsed() >= com.android.server.job.JobSchedulerService.this.mConstants.CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS;
                            shouldForceBatchJob = (batchDelayExpired2 || job.getEffectiveStandbyBucket() == 6 || timeUntilDeadlineMs <= com.android.server.job.JobSchedulerService.this.mConstants.CONN_MAX_CONNECTIVITY_JOB_BATCH_DELAY_MS / 2 || com.android.server.job.JobSchedulerService.this.mConnectivityController.isNetworkInStateForJobRunLocked(job)) ? false : true;
                        } else {
                            boolean shouldForceBatchJob3 = com.android.server.job.Flags.batchActiveBucketJobs();
                            if (shouldForceBatchJob3) {
                                batchingEnabled = com.android.server.job.JobSchedulerService.this.mConstants.MIN_READY_CPU_ONLY_JOBS_COUNT > 1 && timeUntilDeadlineMs > com.android.server.job.JobSchedulerService.this.mConstants.MAX_CPU_ONLY_JOB_BATCH_DELAY_MS / 2 && !job.uidActive && !job.getJob().isExemptedFromAppStandby();
                                batchDelayExpired = job.getFirstForceBatchedTimeElapsed() > 0 && nowElapsed - job.getFirstForceBatchedTimeElapsed() >= com.android.server.job.JobSchedulerService.this.mConstants.MAX_CPU_ONLY_JOB_BATCH_DELAY_MS;
                            } else {
                                batchingEnabled = com.android.server.job.JobSchedulerService.this.mConstants.MIN_READY_NON_ACTIVE_JOBS_COUNT > 1 && job.getEffectiveStandbyBucket() != 0;
                                batchDelayExpired = job.getFirstForceBatchedTimeElapsed() > 0 && nowElapsed - job.getFirstForceBatchedTimeElapsed() >= com.android.server.job.JobSchedulerService.this.mConstants.MAX_NON_ACTIVE_JOB_BATCH_DELAY_MS;
                            }
                            shouldForceBatchJob = (!batchingEnabled || job.getEffectiveStandbyBucket() == 6 || batchDelayExpired) ? false : true;
                        }
                    }
                }
                android.net.Network network = com.android.server.job.Flags.batchConnectivityJobsPerNetwork() ? job.network : null;
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> batch = this.mBatches.get(network);
                if (batch == null) {
                    batch = new android.util.ArraySet<>();
                    this.mBatches.put(network, batch);
                }
                batch.add(job);
                if (!shouldForceBatchJob) {
                    com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.acceptForMaybeReadyJobQueueFunctor(job);
                    this.mUnbatchedJobCount.put(network, java.lang.Integer.valueOf(this.mUnbatchedJobCount.getOrDefault(job.network, 0).intValue() + 1));
                } else if (job.getFirstForceBatchedTimeElapsed() == 0) {
                    job.setFirstForceBatchedTimeElapsed(com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                }
                if (!isRunning) {
                    this.runnableJobs.add(job);
                    if (!shouldForceBatchJob) {
                        this.mUnbatchedJobs.add(job);
                        return;
                    }
                    return;
                }
                return;
            }
            if (isRunning) {
                if (!job.isReady()) {
                    if (job.getEffectiveStandbyBucket() == 5 && job.getStopReason() == 12) {
                        internalStopReason = 6;
                        debugReason = "cancelled due to restricted bucket";
                    } else {
                        internalStopReason = 1;
                        debugReason = "cancelled due to unsatisfied constraints";
                    }
                } else {
                    com.android.server.job.restrictions.JobRestriction restriction = com.android.server.job.JobSchedulerService.this.checkIfRestricted(job);
                    if (restriction != null) {
                        int internalStopReason2 = restriction.getInternalReason();
                        internalStopReason = internalStopReason2;
                        debugReason = "restricted due to " + android.app.job.JobParameters.getInternalReasonCodeDescription(internalStopReason2);
                    } else {
                        internalStopReason = -1;
                        debugReason = "couldn't figure out why the job should stop running";
                    }
                }
                com.android.server.job.JobSchedulerService.this.mConcurrencyManager.stopJobOnServiceContextLocked(job, job.getStopReason(), internalStopReason, debugReason);
            } else if (com.android.server.job.JobSchedulerService.this.mPendingJobQueue.remove(job)) {
                com.android.server.job.JobSchedulerService.this.noteJobNonPending(job);
            }
            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.jobQueueFunctorNotAccept(job);
        }

        void postProcessLocked() {
            int unbatchedJobCount;
            int minReadyCount;
            int unbatchedJobCount2;
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsToRun = this.mUnbatchedJobs;
            if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: " + this.mUnbatchedJobs.size() + " unbatched jobs.");
            }
            int unbatchedCount = 0;
            int n = this.mBatches.size();
            while (true) {
                n--;
                if (n < 0) {
                    break;
                }
                android.net.Network network = this.mBatches.keyAt(n);
                java.lang.Integer unbatchedJobCountObj = this.mUnbatchedJobCount.get(network);
                if (unbatchedJobCountObj != null) {
                    unbatchedJobCount2 = unbatchedJobCountObj.intValue();
                    unbatchedCount += unbatchedJobCount2;
                } else {
                    unbatchedJobCount2 = 0;
                }
                if (network != null) {
                    android.util.ArraySet<com.android.server.job.controllers.JobStatus> batchedJobs = this.mBatches.valueAt(n);
                    if (unbatchedJobCount2 > 0) {
                        if (com.android.server.job.JobSchedulerService.DEBUG) {
                            android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: piggybacking " + (batchedJobs.size() - unbatchedJobCount2) + " jobs on " + network + " because of unbatched job");
                        }
                        jobsToRun.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) batchedJobs);
                    } else {
                        android.net.NetworkCapabilities networkCapabilities = com.android.server.job.JobSchedulerService.this.mConnectivityController.getNetworkCapabilities(network);
                        if (networkCapabilities == null) {
                            android.util.Slog.e("JobScheduler", "Couldn't get NetworkCapabilities for network " + network);
                        } else {
                            int[] transports = networkCapabilities.getTransportTypes();
                            int maxNetworkBatchReq = 1;
                            int length = transports.length;
                            while (unbatchedJobCount < length) {
                                int transport = transports[unbatchedJobCount];
                                maxNetworkBatchReq = java.lang.Math.max(maxNetworkBatchReq, com.android.server.job.JobSchedulerService.this.mConstants.CONN_TRANSPORT_BATCH_THRESHOLD.get(transport));
                                unbatchedJobCount++;
                            }
                            if (batchedJobs.size() >= maxNetworkBatchReq) {
                                if (com.android.server.job.JobSchedulerService.DEBUG) {
                                    android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: " + batchedJobs.size() + " batched network jobs meet requirement for " + network);
                                }
                                jobsToRun.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) batchedJobs);
                            }
                        }
                    }
                }
            }
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> batchedNonNetworkedJobs = this.mBatches.get(null);
            if (batchedNonNetworkedJobs != null) {
                if (com.android.server.job.Flags.batchActiveBucketJobs()) {
                    minReadyCount = com.android.server.job.JobSchedulerService.this.mConstants.MIN_READY_CPU_ONLY_JOBS_COUNT;
                } else {
                    minReadyCount = com.android.server.job.JobSchedulerService.this.mConstants.MIN_READY_NON_ACTIVE_JOBS_COUNT;
                }
                if (jobsToRun.size() > 0) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        java.lang.Integer unbatchedJobCountObj2 = this.mUnbatchedJobCount.get(null);
                        unbatchedJobCount = unbatchedJobCountObj2 != null ? unbatchedJobCountObj2.intValue() : 0;
                        android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: piggybacking " + (batchedNonNetworkedJobs.size() - unbatchedJobCount) + " non-network jobs");
                    }
                    jobsToRun.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) batchedNonNetworkedJobs);
                } else if (batchedNonNetworkedJobs.size() >= minReadyCount) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: adding " + batchedNonNetworkedJobs.size() + " batched non-network jobs.");
                    }
                    jobsToRun.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) batchedNonNetworkedJobs);
                }
            }
            final com.android.server.job.JobSchedulerService jobSchedulerService = com.android.server.job.JobSchedulerService.this;
            jobsToRun.removeIf(new java.util.function.Predicate() { // from class: com.android.server.job.JobSchedulerService$MaybeReadyJobQueueFunctor$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return jobSchedulerService.isCurrentlyRunningLocked((com.android.server.job.controllers.JobStatus) obj);
                }
            });
            if (unbatchedCount > 0 || jobsToRun.size() > 0 || com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.readyForPostProcess()) {
                if (com.android.server.job.JobSchedulerService.DEBUG) {
                    android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: Running " + jobsToRun + " jobs.");
                }
                com.android.server.job.JobSchedulerService.this.noteJobsPending(jobsToRun);
                com.android.server.job.JobSchedulerService.this.mPendingJobQueue.addAll(jobsToRun);
            } else if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "maybeQueueReadyJobsForExecutionLocked: Not running anything.");
            }
            int numRunnableJobs = this.runnableJobs.size();
            if (numRunnableJobs > 0 && numRunnableJobs != jobsToRun.size()) {
                synchronized (com.android.server.job.JobSchedulerService.this.mPendingJobReasonCache) {
                    for (int i = 0; i < numRunnableJobs; i++) {
                        com.android.server.job.controllers.JobStatus job = this.runnableJobs.get(i);
                        if (!jobsToRun.contains(job)) {
                            android.util.SparseIntArray reasons = (android.util.SparseIntArray) com.android.server.job.JobSchedulerService.this.mPendingJobReasonCache.get(job.getUid(), job.getNamespace());
                            if (reasons == null) {
                                reasons = new android.util.SparseIntArray();
                                com.android.server.job.JobSchedulerService.this.mPendingJobReasonCache.add(job.getUid(), job.getNamespace(), reasons);
                            }
                            reasons.put(job.getJobId(), 13);
                        }
                    }
                }
            }
            reset();
        }

        void reset() {
            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.resetForMaybeReadyJobQueueFunctor();
            this.runnableJobs.clear();
            this.mBatches.clear();
            this.mUnbatchedJobs.clear();
            this.mUnbatchedJobCount.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeQueueReadyJobsForExecutionLocked() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(8);
        this.mChangedJobList.clear();
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "Maybe queuing ready jobs...");
        }
        clearPendingJobQueue();
        stopNonReadyActiveJobsLocked();
        this.mJobs.forEachJob(this.mMaybeQueueFunctor);
        this.mMaybeQueueFunctor.postProcessLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkChangedJobListLocked() {
        this.mHandler.removeMessages(8);
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "Check changed jobs...");
        }
        if (this.mChangedJobList.size() == 0) {
            return;
        }
        this.mChangedJobList.forEach(this.mMaybeQueueFunctor);
        this.mMaybeQueueFunctor.postProcessLocked();
        this.mChangedJobList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMediaBackupExemptionLocked(final int userId, final java.lang.String oldPkg, final java.lang.String newPkg) {
        java.util.function.Predicate<com.android.server.job.controllers.JobStatus> shouldProcessJob = new java.util.function.Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.job.JobSchedulerService.lambda$updateMediaBackupExemptionLocked$6(userId, oldPkg, newPkg, (com.android.server.job.controllers.JobStatus) obj);
            }
        };
        this.mJobs.forEachJob(shouldProcessJob, new java.util.function.Consumer() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateMediaBackupExemptionLocked$7((com.android.server.job.controllers.JobStatus) obj);
            }
        });
        this.mHandler.sendEmptyMessage(8);
    }

    static /* synthetic */ boolean lambda$updateMediaBackupExemptionLocked$6(int userId, java.lang.String oldPkg, java.lang.String newPkg, com.android.server.job.controllers.JobStatus job) {
        return job.getSourceUserId() == userId && (job.getSourcePackageName().equals(oldPkg) || job.getSourcePackageName().equals(newPkg));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMediaBackupExemptionLocked$7(com.android.server.job.controllers.JobStatus job) {
        if (job.updateMediaBackupExemptionStatus()) {
            this.mChangedJobList.add(job);
        }
    }

    public boolean areUsersStartedLocked(com.android.server.job.controllers.JobStatus job) {
        boolean sourceStarted = com.android.internal.util.ArrayUtils.contains(this.mStartedUsers, job.getSourceUserId());
        if (this.mJobSchedulerServiceExt.checkIdleJobNotUserStatus(job)) {
            return true;
        }
        if (job.getUserId() == job.getSourceUserId()) {
            return sourceStarted;
        }
        return sourceStarted && com.android.internal.util.ArrayUtils.contains(this.mStartedUsers, job.getUserId());
    }

    boolean isReadyToBeExecutedLocked(com.android.server.job.controllers.JobStatus job) {
        return isReadyToBeExecutedLocked(job, true);
    }

    boolean isReadyToBeExecutedLocked(com.android.server.job.controllers.JobStatus job, boolean rejectActive) {
        boolean jobReady = job.isReady() || evaluateControllerStatesLocked(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "isReadyToBeExecutedLocked: " + job.toShortString() + " ready=" + jobReady);
        }
        if (!jobReady) {
            if (job.getSourcePackageName().equals("android.jobscheduler.cts.jobtestapp")) {
                android.util.Slog.v("JobScheduler", "    NOT READY: " + job);
            }
            return false;
        }
        boolean jobExists = this.mJobs.containsJob(job);
        boolean userStarted = areUsersStartedLocked(job);
        boolean backingUp = this.mBackingUpUids.get(job.getSourceUid());
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "isReadyToBeExecutedLocked: " + job.toShortString() + " exists=" + jobExists + " userStarted=" + userStarted + " backingUp=" + backingUp);
        }
        if (!jobExists || !userStarted || backingUp || checkIfRestricted(job) != null) {
            return false;
        }
        boolean jobPending = this.mPendingJobQueue.contains(job);
        boolean jobActive = rejectActive && this.mConcurrencyManager.isJobRunningLocked(job);
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "isReadyToBeExecutedLocked: " + job.toShortString() + " pending=" + jobPending + " active=" + jobActive);
        }
        if (jobPending || jobActive || !this.mJobSchedulerServiceExt.isReadyToBeExecuted(job)) {
            return false;
        }
        return this.mJobSchedulerServiceExt.isComponentUsable(job, isComponentUsable(job));
    }

    private boolean isComponentUsable(com.android.server.job.controllers.JobStatus job) {
        java.lang.String processName = job.serviceProcessName;
        if (processName == null) {
            if (DEBUG) {
                android.util.Slog.v("JobScheduler", "isComponentUsable: " + job.toShortString() + " component not present");
                return false;
            }
            return false;
        }
        boolean appIsBad = this.mActivityManagerInternal.isAppBad(processName, job.getUid());
        if (DEBUG && appIsBad) {
            android.util.Slog.i("JobScheduler", "App is bad for " + job.toShortString() + " so not runnable");
        }
        return !appIsBad;
    }

    boolean evaluateControllerStatesLocked(com.android.server.job.controllers.JobStatus job) {
        for (int c = this.mControllers.size() - 1; c >= 0; c--) {
            com.android.server.job.controllers.StateController sc = this.mControllers.get(c);
            sc.evaluateStateLocked(job);
        }
        return job.isReady();
    }

    public boolean areComponentsInPlaceLocked(com.android.server.job.controllers.JobStatus job) {
        boolean jobExists = this.mJobs.containsJob(job);
        boolean userStarted = areUsersStartedLocked(job);
        boolean backingUp = this.mBackingUpUids.get(job.getSourceUid());
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "areComponentsInPlaceLocked: " + job.toShortString() + " exists=" + jobExists + " userStarted=" + userStarted + " backingUp=" + backingUp);
        }
        if (!jobExists || !userStarted || backingUp) {
            return false;
        }
        com.android.server.job.restrictions.JobRestriction restriction = checkIfRestricted(job);
        if (restriction != null) {
            if (DEBUG) {
                android.util.Slog.v("JobScheduler", "areComponentsInPlaceLocked: " + job.toShortString() + " restricted due to " + restriction.getInternalReason());
            }
            return false;
        }
        return isComponentUsable(job);
    }

    public long getMinJobExecutionGuaranteeMs(com.android.server.job.controllers.JobStatus job) {
        long jMin;
        long upperLimitMs;
        synchronized (this.mLock) {
            if (job.shouldTreatAsUserInitiatedJob() && checkRunUserInitiatedJobsPermission(job.getSourceUid(), job.getSourcePackageName())) {
                boolean isWithinTimeoutQuota = this.mQuotaTracker.isWithinQuota(job.getTimeoutBlameUserId(), job.getTimeoutBlamePackageName(), QUOTA_TRACKER_TIMEOUT_UIJ_TAG);
                if (isWithinTimeoutQuota) {
                    upperLimitMs = this.mConstants.RUNTIME_UI_LIMIT_MS;
                } else {
                    upperLimitMs = this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
                }
                if (job.getJob().getRequiredNetwork() != null) {
                    if (this.mConstants.RUNTIME_USE_DATA_ESTIMATES_FOR_LIMITS) {
                        long estimatedTransferTimeMs = this.mConnectivityController.getEstimatedTransferTimeMs(job);
                        if (estimatedTransferTimeMs == -1) {
                            return java.lang.Math.min(upperLimitMs, this.mConstants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS);
                        }
                        long factoredTransferTimeMs = (long) (estimatedTransferTimeMs * this.mConstants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_BUFFER_FACTOR);
                        return java.lang.Math.min(upperLimitMs, java.lang.Math.max(factoredTransferTimeMs, this.mConstants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS));
                    }
                    return java.lang.Math.min(upperLimitMs, java.lang.Math.max(this.mConstants.RUNTIME_MIN_UI_GUARANTEE_MS, this.mConstants.RUNTIME_MIN_UI_DATA_TRANSFER_GUARANTEE_MS));
                }
                return java.lang.Math.min(upperLimitMs, this.mConstants.RUNTIME_MIN_UI_GUARANTEE_MS);
            }
            boolean isWithinTimeoutQuota2 = job.shouldTreatAsExpeditedJob();
            if (isWithinTimeoutQuota2) {
                if (job.getEffectiveStandbyBucket() != 5) {
                    jMin = this.mConstants.RUNTIME_MIN_EJ_GUARANTEE_MS;
                } else {
                    jMin = java.lang.Math.min(this.mConstants.RUNTIME_MIN_EJ_GUARANTEE_MS, 300000L);
                }
                return jMin;
            }
            return this.mConstants.RUNTIME_MIN_GUARANTEE_MS;
        }
    }

    public long getMaxJobExecutionTimeMs(com.android.server.job.controllers.JobStatus job) {
        long normalUpperLimitMs;
        long upperLimitMs;
        synchronized (this.mLock) {
            if (job.shouldTreatAsUserInitiatedJob() && checkRunUserInitiatedJobsPermission(job.getSourceUid(), job.getSourcePackageName()) && this.mQuotaTracker.isWithinQuota(job.getTimeoutBlameUserId(), job.getTimeoutBlamePackageName(), QUOTA_TRACKER_TIMEOUT_UIJ_TAG)) {
                return this.mConstants.RUNTIME_UI_LIMIT_MS;
            }
            if (job.shouldTreatAsUserInitiatedJob()) {
                return this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            }
            java.lang.String timeoutTag = job.shouldTreatAsExpeditedJob() ? QUOTA_TRACKER_TIMEOUT_EJ_TAG : QUOTA_TRACKER_TIMEOUT_REG_TAG;
            if (job.shouldTreatAsExpeditedJob()) {
                normalUpperLimitMs = this.mConstants.RUNTIME_MIN_GUARANTEE_MS;
            } else {
                normalUpperLimitMs = this.mConstants.RUNTIME_FREE_QUOTA_MAX_LIMIT_MS;
            }
            if (this.mQuotaTracker.isWithinQuota(job.getTimeoutBlameUserId(), job.getTimeoutBlamePackageName(), timeoutTag)) {
                upperLimitMs = normalUpperLimitMs;
            } else {
                upperLimitMs = this.mConstants.RUNTIME_MIN_GUARANTEE_MS;
            }
            return java.lang.Math.min(upperLimitMs, this.mQuotaController.getMaxJobExecutionTimeMsLocked(job));
        }
    }

    void maybeRunPendingJobsLocked() {
        if (DEBUG) {
            android.util.Slog.d("JobScheduler", "pending queue: " + this.mPendingJobQueue.size() + " jobs.");
        }
        this.mConcurrencyManager.assignJobsToContextsLocked();
        reportActiveLocked();
    }

    private int adjustJobBias(int curBias, com.android.server.job.controllers.JobStatus job) {
        if (curBias < 40) {
            float factor = this.mJobPackageTracker.getLoadFactor(job);
            if (factor >= this.mConstants.HEAVY_USE_FACTOR) {
                return curBias - 80;
            }
            if (factor >= this.mConstants.MODERATE_USE_FACTOR) {
                return curBias - 40;
            }
            return curBias;
        }
        return curBias;
    }

    int evaluateJobBiasLocked(com.android.server.job.controllers.JobStatus job) {
        int bias = job.getBias();
        if (bias >= 30) {
            return adjustJobBias(bias, job);
        }
        int override = this.mUidBiasOverride.get(job.getSourceUid(), 0);
        if (override != 0) {
            return adjustJobBias(override, job);
        }
        return adjustJobBias(bias, job);
    }

    void informObserversOfUserVisibleJobChange(com.android.server.job.JobServiceContext jobServiceContext, com.android.server.job.controllers.JobStatus jobStatus, boolean z) {
        com.android.internal.os.SomeArgs someArgsObtain = com.android.internal.os.SomeArgs.obtain();
        someArgsObtain.arg1 = jobServiceContext;
        someArgsObtain.arg2 = jobStatus;
        someArgsObtain.argi1 = z ? 1 : 0;
        this.mHandler.obtainMessage(11, someArgsObtain).sendToTarget();
    }

    final class BatteryStateTracker extends android.content.BroadcastReceiver implements android.os.BatteryManagerInternal.ChargingPolicyChangeListener {
        private int mBatteryLevel;
        private boolean mBatteryNotLow;
        private boolean mCharging;
        private int mChargingPolicy;
        private android.content.BroadcastReceiver mMonitor;
        private boolean mPowerConnected;
        private int mLastBatterySeq = -1;
        private final android.os.BatteryManagerInternal mBatteryManagerInternal = (android.os.BatteryManagerInternal) com.android.server.LocalServices.getService(android.os.BatteryManagerInternal.class);

        BatteryStateTracker() {
        }

        public void startTracking() {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.BATTERY_LOW");
            filter.addAction("android.intent.action.BATTERY_OKAY");
            filter.addAction("android.os.action.CHARGING");
            filter.addAction("android.os.action.DISCHARGING");
            filter.addAction("android.intent.action.BATTERY_LEVEL_CHANGED");
            filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
            com.android.server.job.JobSchedulerService.this.getTestableContext().registerReceiver(this, filter);
            this.mBatteryManagerInternal.registerChargingPolicyChangeListener(this);
            this.mBatteryLevel = this.mBatteryManagerInternal.getBatteryLevel();
            this.mBatteryNotLow = !this.mBatteryManagerInternal.getBatteryLevelLow();
            com.android.internal.app.IBatteryStats mBatteryStats = com.android.server.am.BatteryStatsService.getService();
            try {
                this.mCharging = mBatteryStats.isCharging();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e("JobScheduler", "mBatteryStats.isCharging() error occurred");
                this.mCharging = this.mBatteryManagerInternal.isPowered(15);
            }
            this.mChargingPolicy = this.mBatteryManagerInternal.getChargingPolicy();
        }

        public void setMonitorBatteryLocked(boolean enabled) {
            if (enabled) {
                if (this.mMonitor == null) {
                    this.mMonitor = new android.content.BroadcastReceiver() { // from class: com.android.server.job.JobSchedulerService.BatteryStateTracker.1
                        @Override // android.content.BroadcastReceiver
                        public void onReceive(android.content.Context context, android.content.Intent intent) {
                            com.android.server.job.JobSchedulerService.BatteryStateTracker.this.onReceiveInternal(intent);
                        }
                    };
                    android.content.IntentFilter filter = new android.content.IntentFilter();
                    filter.addAction("android.intent.action.BATTERY_CHANGED");
                    com.android.server.job.JobSchedulerService.this.getTestableContext().registerReceiver(this.mMonitor, filter);
                    return;
                }
                return;
            }
            if (this.mMonitor != null) {
                com.android.server.job.JobSchedulerService.this.getTestableContext().unregisterReceiver(this.mMonitor);
                this.mMonitor = null;
            }
        }

        public boolean isCharging() {
            return isConsideredCharging();
        }

        public boolean isBatteryNotLow() {
            return this.mBatteryNotLow;
        }

        public boolean isMonitoring() {
            return this.mMonitor != null;
        }

        public boolean isPowerConnected() {
            return this.mPowerConnected;
        }

        public int getSeq() {
            return this.mLastBatterySeq;
        }

        public void onChargingPolicyChanged(int newPolicy) {
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                if (this.mChargingPolicy == newPolicy) {
                    return;
                }
                if (com.android.server.job.JobSchedulerService.DEBUG) {
                    android.util.Slog.i("JobScheduler", "Charging policy changed from " + this.mChargingPolicy + " to " + newPolicy);
                }
                boolean wasConsideredCharging = isConsideredCharging();
                this.mChargingPolicy = newPolicy;
                if (android.os.Trace.isTagEnabled(524288L)) {
                    android.os.Trace.instantForTrack(524288L, "JobScheduler", "CHARGING POLICY CHANGED#" + this.mChargingPolicy);
                }
                if (isConsideredCharging() != wasConsideredCharging) {
                    for (int c = com.android.server.job.JobSchedulerService.this.mControllers.size() - 1; c >= 0; c--) {
                        com.android.server.job.JobSchedulerService.this.mControllers.get(c).onBatteryStateChangedLocked();
                    }
                }
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            onReceiveInternal(intent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onReceiveInternal(android.content.Intent intent) {
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                java.lang.String action = intent.getAction();
                boolean changed = false;
                if ("android.intent.action.BATTERY_LOW".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Battery life too low @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (this.mBatteryNotLow) {
                        this.mBatteryNotLow = false;
                        changed = true;
                    }
                } else if ("android.intent.action.BATTERY_OKAY".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Battery high enough @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (!this.mBatteryNotLow) {
                        this.mBatteryNotLow = true;
                        changed = true;
                    }
                } else if ("android.intent.action.BATTERY_LEVEL_CHANGED".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Battery level changed @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    boolean wasConsideredCharging = isConsideredCharging();
                    this.mBatteryLevel = this.mBatteryManagerInternal.getBatteryLevel();
                    changed = isConsideredCharging() != wasConsideredCharging;
                } else if ("android.intent.action.ACTION_POWER_CONNECTED".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Power connected @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (this.mPowerConnected) {
                        return;
                    }
                    this.mPowerConnected = true;
                    changed = true;
                } else if ("android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Power disconnected @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (!this.mPowerConnected) {
                        return;
                    }
                    this.mPowerConnected = false;
                    changed = true;
                } else if ("android.os.action.CHARGING".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Battery charging @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (!this.mCharging) {
                        boolean wasConsideredCharging2 = isConsideredCharging();
                        this.mCharging = true;
                        changed = isConsideredCharging() != wasConsideredCharging2;
                    }
                } else if ("android.os.action.DISCHARGING".equals(action)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Battery discharging @ " + com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
                    }
                    if (this.mCharging) {
                        boolean wasConsideredCharging3 = isConsideredCharging();
                        this.mCharging = false;
                        changed = isConsideredCharging() != wasConsideredCharging3;
                    }
                }
                this.mLastBatterySeq = intent.getIntExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mLastBatterySeq);
                if (changed) {
                    for (int c = com.android.server.job.JobSchedulerService.this.mControllers.size() - 1; c >= 0; c--) {
                        com.android.server.job.JobSchedulerService.this.mControllers.get(c).onBatteryStateChangedLocked();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isConsideredCharging() {
            if (this.mCharging) {
                return true;
            }
            if (this.mPowerConnected && this.mChargingPolicy != Integer.MIN_VALUE) {
                return this.mBatteryLevel >= 70 && android.os.BatteryManager.isAdaptiveChargingPolicy(this.mChargingPolicy);
            }
            return false;
        }
    }

    final class LocalService implements com.android.server.job.JobSchedulerInternal {
        LocalService() {
        }

        public java.util.List<android.app.job.JobInfo> getSystemScheduledOwnJobs(final java.lang.String namespace) {
            final java.util.List<android.app.job.JobInfo> ownJobs;
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                ownJobs = new java.util.ArrayList<>();
                com.android.server.job.JobSchedulerService.this.mJobs.forEachJob(1000, new java.util.function.Consumer() { // from class: com.android.server.job.JobSchedulerService$LocalService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.job.JobSchedulerService.LocalService.lambda$getSystemScheduledOwnJobs$0(namespace, ownJobs, (com.android.server.job.controllers.JobStatus) obj);
                    }
                });
            }
            return ownJobs;
        }

        static /* synthetic */ void lambda$getSystemScheduledOwnJobs$0(java.lang.String namespace, java.util.List ownJobs, com.android.server.job.controllers.JobStatus job) {
            if (job.getSourceUid() == 1000 && java.util.Objects.equals(job.getNamespace(), namespace) && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(job.getSourcePackageName())) {
                ownJobs.add(job.getJob());
            }
        }

        public void cancelJobsForUid(int uid, boolean includeProxiedJobs, int reason, int internalReasonCode, java.lang.String debugReason) {
            com.android.server.job.JobSchedulerService.this.cancelJobsForUid(uid, includeProxiedJobs, reason, internalReasonCode, debugReason);
        }

        public void addBackingUpUid(int uid) {
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                com.android.server.job.JobSchedulerService.this.mBackingUpUids.put(uid, true);
            }
        }

        public void removeBackingUpUid(int uid) {
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                com.android.server.job.JobSchedulerService.this.mBackingUpUids.delete(uid);
                if (com.android.server.job.JobSchedulerService.this.mJobs.countJobsForUid(uid) > 0) {
                    com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }

        public void clearAllBackingUpUids() {
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                if (com.android.server.job.JobSchedulerService.this.mBackingUpUids.size() > 0) {
                    com.android.server.job.JobSchedulerService.this.mBackingUpUids.clear();
                    com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }

        public java.lang.String getCloudMediaProviderPackage(int userId) {
            return (java.lang.String) com.android.server.job.JobSchedulerService.this.mCloudMediaProviderPackages.get(userId);
        }

        public void reportAppUsage(java.lang.String packageName, int userId) {
            com.android.server.job.JobSchedulerService.this.reportAppUsage(packageName, userId);
        }

        public boolean isAppConsideredBuggy(int callingUserId, java.lang.String callingPackageName, int timeoutBlameUserId, java.lang.String timeoutBlamePackageName) {
            return (com.android.server.job.JobSchedulerService.this.mQuotaTracker.isWithinQuota(callingUserId, callingPackageName, com.android.server.job.JobSchedulerService.QUOTA_TRACKER_ANR_TAG) && com.android.server.job.JobSchedulerService.this.mQuotaTracker.isWithinQuota(callingUserId, callingPackageName, com.android.server.job.JobSchedulerService.QUOTA_TRACKER_SCHEDULE_PERSISTED_TAG) && com.android.server.job.JobSchedulerService.this.mQuotaTracker.isWithinQuota(timeoutBlameUserId, timeoutBlamePackageName, com.android.server.job.JobSchedulerService.QUOTA_TRACKER_TIMEOUT_TOTAL_TAG)) ? false : true;
        }

        public boolean isNotificationAssociatedWithAnyUserInitiatedJobs(int notificationId, int userId, java.lang.String packageName) {
            if (packageName == null) {
                return false;
            }
            return com.android.server.job.JobSchedulerService.this.mConcurrencyManager.isNotificationAssociatedWithAnyUserInitiatedJobs(notificationId, userId, packageName);
        }

        public boolean isNotificationChannelAssociatedWithAnyUserInitiatedJobs(java.lang.String notificationChannel, int userId, java.lang.String packageName) {
            if (packageName == null || notificationChannel == null) {
                return false;
            }
            return com.android.server.job.JobSchedulerService.this.mConcurrencyManager.isNotificationChannelAssociatedWithAnyUserInitiatedJobs(notificationChannel, userId, packageName);
        }

        public com.android.server.job.JobSchedulerInternal.JobStorePersistStats getPersistStats() {
            com.android.server.job.JobSchedulerInternal.JobStorePersistStats jobStorePersistStats;
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                jobStorePersistStats = new com.android.server.job.JobSchedulerInternal.JobStorePersistStats(com.android.server.job.JobSchedulerService.this.mJobs.getPersistStats());
            }
            return jobStorePersistStats;
        }
    }

    final class StandbyTracker extends com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener {
        StandbyTracker() {
        }

        public void onAppIdleStateChanged(java.lang.String packageName, int userId, boolean idle, int bucket, int reason) {
        }

        public void onUserInteractionStarted(java.lang.String packageName, int userId) {
            int uid = com.android.server.job.JobSchedulerService.this.mLocalPM.getPackageUid(packageName, 8192L, userId);
            if (uid < 0) {
                return;
            }
            long sinceLast = com.android.server.job.JobSchedulerService.sUsageStatsManagerInternal.getTimeSinceLastJobRun(packageName, userId);
            if (sinceLast > 172800000) {
                sinceLast = 0;
            }
            com.android.server.job.JobSchedulerService.DeferredJobCounter counter = new com.android.server.job.JobSchedulerService.DeferredJobCounter();
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                com.android.server.job.JobSchedulerService.this.mJobs.forEachJobForSourceUid(uid, counter);
            }
            if (counter.numDeferred() > 0 || sinceLast > 0) {
                com.android.server.job.JobSchedulerService.this.mBatteryStatsInternal.noteJobsDeferred(uid, counter.numDeferred(), sinceLast);
                com.android.internal.util.FrameworkStatsLog.write_non_chained(85, uid, (java.lang.String) null, counter.numDeferred(), sinceLast);
            }
        }
    }

    static class DeferredJobCounter implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        private int mDeferred = 0;

        DeferredJobCounter() {
        }

        public int numDeferred() {
            return this.mDeferred;
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus job) {
            if (job.getWhenStandbyDeferred() > 0) {
                this.mDeferred++;
            }
        }
    }

    public static int standbyBucketToBucketIndex(int bucket) {
        if (bucket == 50) {
            return 4;
        }
        if (bucket > 40) {
            return 5;
        }
        if (bucket > 30) {
            return 3;
        }
        if (bucket > 20) {
            return 2;
        }
        if (bucket > 10) {
            return 1;
        }
        if (bucket > 5) {
            return 0;
        }
        return 6;
    }

    public static int standbyBucketForPackage(java.lang.String packageName, int userId, long elapsedNow) {
        int bucket;
        if (sUsageStatsManagerInternal != null) {
            bucket = sUsageStatsManagerInternal.getAppStandbyBucket(packageName, userId, elapsedNow);
        } else {
            bucket = 0;
        }
        int bucket2 = standbyBucketToBucketIndex(bucket);
        if (DEBUG_STANDBY) {
            android.util.Slog.v("JobScheduler", packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + userId + " standby bucket index: " + bucket2);
        }
        return bucket2;
    }

    static int safelyScaleBytesToKBForHistogram(long bytes) {
        long kilobytes = bytes / 1000;
        if (kilobytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (kilobytes < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) kilobytes;
    }

    private class CloudProviderChangeListener implements android.os.storage.StorageManagerInternal.CloudProviderChangeListener {
        private CloudProviderChangeListener() {
        }

        public void onCloudProviderChanged(int userId, java.lang.String authority) {
            android.content.pm.PackageManager pm = com.android.server.job.JobSchedulerService.this.getContext().createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager();
            android.content.pm.ProviderInfo pi = pm.resolveContentProvider(authority, android.content.pm.PackageManager.ComponentInfoFlags.of(0L));
            java.lang.String newPkg = pi == null ? null : pi.packageName;
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                java.lang.String oldPkg = (java.lang.String) com.android.server.job.JobSchedulerService.this.mCloudMediaProviderPackages.get(userId);
                if (!java.util.Objects.equals(oldPkg, newPkg)) {
                    if (com.android.server.job.JobSchedulerService.DEBUG) {
                        android.util.Slog.d("JobScheduler", "Cloud provider of user " + userId + " changed from " + oldPkg + " to " + newPkg);
                    }
                    com.android.server.job.JobSchedulerService.this.mCloudMediaProviderPackages.put(userId, newPkg);
                    com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                    args.argi1 = userId;
                    args.arg1 = oldPkg;
                    args.arg2 = newPkg;
                    com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(9, args).sendToTarget();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasPermission(int uid, int pid, java.lang.String permission) {
        synchronized (this.mPermissionCache) {
            android.util.SparseArrayMap<java.lang.String, java.lang.Boolean> pidPermissions = this.mPermissionCache.get(uid);
            if (pidPermissions == null) {
                pidPermissions = new android.util.SparseArrayMap<>();
                this.mPermissionCache.put(uid, pidPermissions);
            }
            java.lang.Boolean cached = (java.lang.Boolean) pidPermissions.get(pid, permission);
            if (cached != null) {
                return cached.booleanValue();
            }
            int result = getContext().checkPermission(permission, pid, uid);
            boolean permissionGranted = result == 0;
            pidPermissions.add(pid, permission, java.lang.Boolean.valueOf(permissionGranted));
            return permissionGranted;
        }
    }

    final class JobSchedulerStub extends android.app.job.IJobScheduler.Stub {
        JobSchedulerStub() {
        }

        private void enforceValidJobRequest(int uid, int pid, android.app.job.JobInfo job) {
            android.content.pm.PackageManager pm = com.android.server.job.JobSchedulerService.this.getContext().createContextAsUser(android.os.UserHandle.getUserHandleForUid(uid), 0).getPackageManager();
            android.content.ComponentName service = job.getService();
            try {
                android.content.pm.ServiceInfo si = pm.getServiceInfo(service, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED);
                if (si == null) {
                    throw new java.lang.IllegalArgumentException("No such service " + service);
                }
                if (si.applicationInfo.uid != uid) {
                    throw new java.lang.IllegalArgumentException("uid " + uid + " cannot schedule job in " + service.getPackageName());
                }
                if (!"android.permission.BIND_JOB_SERVICE".equals(si.permission)) {
                    throw new java.lang.IllegalArgumentException("Scheduled service " + service + " does not require android.permission.BIND_JOB_SERVICE permission");
                }
                if (job.isPersisted() && !canPersistJobs(pid, uid)) {
                    throw new java.lang.IllegalArgumentException("Requested job cannot be persisted without holding android.permission.RECEIVE_BOOT_COMPLETED permission");
                }
                if (job.getRequiredNetwork() != null && android.app.compat.CompatChanges.isChangeEnabled(com.android.server.job.JobSchedulerService.REQUIRE_NETWORK_PERMISSIONS_FOR_CONNECTIVITY_JOBS, uid) && !com.android.server.job.JobSchedulerService.this.hasPermission(uid, pid, "android.permission.ACCESS_NETWORK_STATE")) {
                    throw new java.lang.SecurityException("android.permission.ACCESS_NETWORK_STATE required for jobs with a connectivity constraint");
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.IllegalArgumentException("Tried to schedule job for non-existent component: " + service);
            }
        }

        private android.app.job.JobInfo enforceBuilderApiPermissions(int uid, int pid, android.app.job.JobInfo job) {
            if (job.getBias() != 0 && !com.android.server.job.JobSchedulerService.this.hasPermission(uid, pid, "android.permission.UPDATE_DEVICE_STATS")) {
                if (android.app.compat.CompatChanges.isChangeEnabled(com.android.server.job.JobSchedulerService.THROW_ON_UNSUPPORTED_BIAS_USAGE, uid)) {
                    throw new java.lang.SecurityException("Apps may not call setBias()");
                }
                android.util.Slog.w("JobScheduler", "Uid " + uid + " set bias on its job");
                return new android.app.job.JobInfo.Builder(job).setBias(0).build(false, false, false, false);
            }
            return job;
        }

        private boolean canPersistJobs(int pid, int uid) {
            return com.android.server.job.JobSchedulerService.this.hasPermission(uid, pid, "android.permission.RECEIVE_BOOT_COMPLETED");
        }

        private int validateJob(android.app.job.JobInfo job, int callingUid, int callingPid, int sourceUserId, java.lang.String sourcePkgName, android.app.job.JobWorkItem jobWorkItem) {
            boolean rejectNegativeNetworkEstimates = android.app.compat.CompatChanges.isChangeEnabled(253665015L, callingUid);
            job.enforceValidity(android.app.compat.CompatChanges.isChangeEnabled(194532703L, callingUid), rejectNegativeNetworkEstimates, android.app.compat.CompatChanges.isChangeEnabled(311402873L, callingUid), android.app.compat.CompatChanges.isChangeEnabled(323349338L, callingUid));
            if ((job.getFlags() & 1) != 0) {
                com.android.server.job.JobSchedulerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONNECTIVITY_INTERNAL", "JobScheduler");
            }
            if ((job.getFlags() & 8) != 0) {
                if (callingUid != 1000) {
                    throw new java.lang.SecurityException("Job has invalid flags");
                }
                if (job.isPeriodic()) {
                    android.util.Slog.wtf("JobScheduler", "Periodic jobs mustn't have FLAG_EXEMPT_FROM_APP_STANDBY. Job=" + job);
                }
            }
            if (job.isUserInitiated()) {
                int sourceUid = -1;
                int sourcePid = -1;
                if (sourceUserId != -1 && sourcePkgName != null) {
                    try {
                        sourceUid = android.app.AppGlobals.getPackageManager().getPackageUid(sourcePkgName, 0L, sourceUserId);
                    } catch (android.os.RemoteException e) {
                    }
                }
                boolean isInStateToScheduleUiJobSource = false;
                java.lang.String callingPkgName = job.getService().getPackageName();
                if (sourceUid != -1) {
                    int sourceResult = validateRunUserInitiatedJobsPermission(sourceUid, sourcePkgName);
                    if (sourceResult != 1) {
                        return sourceResult;
                    }
                    if (callingUid == sourceUid && callingPkgName.equals(sourcePkgName)) {
                        sourcePid = callingPid;
                    }
                    isInStateToScheduleUiJobSource = isInStateToScheduleUserInitiatedJobs(sourceUid, sourcePid, sourcePkgName);
                }
                boolean isInStateToScheduleUiJobCalling = false;
                if (callingUid != sourceUid || !callingPkgName.equals(sourcePkgName)) {
                    int callingResult = validateRunUserInitiatedJobsPermission(callingUid, callingPkgName);
                    if (callingResult != 1) {
                        return callingResult;
                    }
                    if (!isInStateToScheduleUiJobSource) {
                        isInStateToScheduleUiJobCalling = isInStateToScheduleUserInitiatedJobs(callingUid, callingPid, callingPkgName);
                    }
                }
                if (!isInStateToScheduleUiJobSource && !isInStateToScheduleUiJobCalling) {
                    android.util.Slog.e("JobScheduler", "Uid(s) " + sourceUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + callingUid + " not in a state to schedule user-initiated jobs");
                    com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_invalid_state", callingUid);
                    return 0;
                }
            }
            if (jobWorkItem != null) {
                jobWorkItem.enforceValidity(rejectNegativeNetworkEstimates);
                if ((jobWorkItem.getEstimatedNetworkDownloadBytes() != -1 || jobWorkItem.getEstimatedNetworkUploadBytes() != -1 || jobWorkItem.getMinimumNetworkChunkBytes() != -1) && job.getRequiredNetwork() == null) {
                    if (android.app.compat.CompatChanges.isChangeEnabled(com.android.server.job.JobSchedulerService.REQUIRE_NETWORK_CONSTRAINT_FOR_NETWORK_JOB_WORK_ITEMS, callingUid)) {
                        throw new java.lang.IllegalArgumentException("JobWorkItem implies network usage but job doesn't specify a network constraint");
                    }
                    android.util.Slog.e("JobScheduler", "JobWorkItem implies network usage but job doesn't specify a network constraint");
                }
                if (job.isPersisted() && jobWorkItem.getIntent() != null) {
                    throw new java.lang.IllegalArgumentException("Cannot persist JobWorkItems with Intents");
                }
            }
            com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt.checkOplusPermission(job, android.os.Binder.getCallingPid(), callingUid);
            return 1;
        }

        private java.lang.String validateNamespace(java.lang.String namespace) {
            java.lang.String namespace2 = android.app.job.JobScheduler.sanitizeNamespace(namespace);
            if (namespace2 != null) {
                if (namespace2.isEmpty()) {
                    throw new java.lang.IllegalArgumentException("namespace cannot be empty");
                }
                if (namespace2.length() > 1000) {
                    throw new java.lang.IllegalArgumentException("namespace cannot be more than 1000 characters");
                }
                return namespace2.intern();
            }
            return namespace2;
        }

        private int validateRunUserInitiatedJobsPermission(int uid, java.lang.String packageName) {
            int state = com.android.server.job.JobSchedulerService.this.getRunUserInitiatedJobsPermissionState(uid, packageName);
            if (state == 2) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_no_permission", uid);
                throw new java.lang.SecurityException("android.permission.RUN_USER_INITIATED_JOBS required to schedule user-initiated jobs.");
            }
            if (state != 1) {
                return 1;
            }
            com.android.modules.expresslog.Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_schedule_failure_uij_no_permission", uid);
            return 0;
        }

        private boolean isInStateToScheduleUserInitiatedJobs(int uid, int pid, java.lang.String pkgName) {
            int procState = com.android.server.job.JobSchedulerService.this.mActivityManagerInternal.getUidProcessState(uid);
            if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "Uid " + uid + " proc state=" + android.app.ActivityManager.procStateToString(procState));
            }
            if (procState == 2) {
                return true;
            }
            boolean canScheduleUiJobsInBg = com.android.server.job.JobSchedulerService.this.mActivityManagerInternal.canScheduleUserInitiatedJobs(uid, pid, pkgName);
            if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "Uid " + uid + " AM.canScheduleUserInitiatedJobs= " + canScheduleUiJobsInBg);
            }
            return canScheduleUiJobsInBg;
        }

        public int schedule(java.lang.String namespace, android.app.job.JobInfo job) throws android.os.RemoteException {
            if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "Scheduling job: " + job.toString());
            }
            if (com.android.server.job.JobSchedulerService.DEBUG_USAGE) {
                android.util.Slog.d("JobScheduler", "Scheduling job: " + job.toString2());
            }
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(uid);
            enforceValidJobRequest(uid, pid, job);
            int result = validateJob(job, uid, pid, -1, null, null);
            if (result != 1) {
                return result;
            }
            java.lang.String namespace2 = validateNamespace(namespace);
            android.app.job.JobInfo job2 = enforceBuilderApiPermissions(uid, pid, job);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.job.JobSchedulerService.this.scheduleAsPackage(job2, null, uid, null, userId, namespace2, null);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int enqueue(java.lang.String namespace, android.app.job.JobInfo job, android.app.job.JobWorkItem work) throws android.os.RemoteException {
            if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "Enqueueing job: " + job.toString() + " work: " + work);
            }
            if (com.android.server.job.JobSchedulerService.DEBUG_USAGE) {
                android.util.Slog.d("JobScheduler", "Enqueueing job: " + job.toString2() + " work: " + work);
            }
            int uid = android.os.Binder.getCallingUid();
            int pid = android.os.Binder.getCallingPid();
            int userId = android.os.UserHandle.getUserId(uid);
            enforceValidJobRequest(uid, pid, job);
            if (work == null) {
                throw new java.lang.NullPointerException("work is null");
            }
            int result = validateJob(job, uid, pid, -1, null, work);
            if (result != 1) {
                return result;
            }
            java.lang.String namespace2 = validateNamespace(namespace);
            android.app.job.JobInfo job2 = enforceBuilderApiPermissions(uid, pid, job);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.job.JobSchedulerService.this.scheduleAsPackage(job2, work, uid, null, userId, namespace2, null);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int scheduleAsPackage(java.lang.String namespace, android.app.job.JobInfo job, java.lang.String packageName, int userId, java.lang.String tag) throws android.os.RemoteException {
            int callerUid = android.os.Binder.getCallingUid();
            int callerPid = android.os.Binder.getCallingPid();
            if (com.android.server.job.JobSchedulerService.DEBUG) {
                android.util.Slog.d("JobScheduler", "Caller uid " + callerUid + " scheduling job: " + job.toString() + " on behalf of " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            }
            if (packageName == null) {
                throw new java.lang.NullPointerException("Must specify a package for scheduleAsPackage()");
            }
            int mayScheduleForOthers = com.android.server.job.JobSchedulerService.this.getContext().checkCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS");
            if (mayScheduleForOthers != 0) {
                throw new java.lang.SecurityException("Caller uid " + callerUid + " not permitted to schedule jobs for other apps");
            }
            enforceValidJobRequest(callerUid, callerPid, job);
            int result = validateJob(job, callerUid, callerPid, userId, packageName, null);
            if (result != 1) {
                return result;
            }
            java.lang.String namespace2 = validateNamespace(namespace);
            android.app.job.JobInfo job2 = enforceBuilderApiPermissions(callerUid, callerPid, job);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.job.JobSchedulerService.this.scheduleAsPackage(job2, null, callerUid, packageName, userId, namespace2, tag);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public java.util.Map<java.lang.String, android.content.pm.ParceledListSlice<android.app.job.JobInfo>> getAllPendingJobs() throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.util.ArrayMap<java.lang.String, java.util.List<android.app.job.JobInfo>> jobs = com.android.server.job.JobSchedulerService.this.getPendingJobs(uid);
                android.util.ArrayMap<java.lang.String, android.content.pm.ParceledListSlice<android.app.job.JobInfo>> outMap = new android.util.ArrayMap<>();
                for (int i = 0; i < jobs.size(); i++) {
                    outMap.put(jobs.keyAt(i), new android.content.pm.ParceledListSlice<>(jobs.valueAt(i)));
                }
                return outMap;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.content.pm.ParceledListSlice<android.app.job.JobInfo> getAllPendingJobsInNamespace(java.lang.String namespace) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return new android.content.pm.ParceledListSlice<>(com.android.server.job.JobSchedulerService.this.getPendingJobsInNamespace(uid, validateNamespace(namespace)));
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.app.job.JobInfo getPendingJob(java.lang.String namespace, int jobId) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.job.JobSchedulerService.this.getPendingJob(uid, validateNamespace(namespace), jobId);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int getPendingJobReason(java.lang.String namespace, int jobId) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.job.JobSchedulerService.this.getPendingJobReason(uid, validateNamespace(namespace), jobId);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void cancelAll() throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.job.JobSchedulerService.this.cancelJobsForUid(uid, false, 1, 0, "cancelAll() called by app, callingUid=" + uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void cancelAllInNamespace(java.lang.String namespace) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.job.JobSchedulerService.this.cancelJobsForUid(uid, false, true, validateNamespace(namespace), 1, 0, "cancelAllInNamespace() called by app, callingUid=" + uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void cancel(java.lang.String namespace, int jobId) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.job.JobSchedulerService.this.cancelJob(uid, validateNamespace(namespace), jobId, uid, 1);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean canRunUserInitiatedJobs(java.lang.String packageName) {
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            int packageUid = com.android.server.job.JobSchedulerService.this.mLocalPM.getPackageUid(packageName, 0L, userId);
            if (callingUid != packageUid) {
                throw new java.lang.SecurityException("Uid " + callingUid + " cannot query canRunUserInitiatedJobs for package " + packageName);
            }
            return com.android.server.job.JobSchedulerService.this.checkRunUserInitiatedJobsPermission(packageUid, packageName);
        }

        public boolean hasRunUserInitiatedJobsPermission(java.lang.String packageName, int userId) {
            int uid = com.android.server.job.JobSchedulerService.this.mLocalPM.getPackageUid(packageName, 0L, userId);
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid != uid && !android.os.UserHandle.isCore(callingUid)) {
                throw new java.lang.SecurityException("Uid " + callingUid + " cannot query hasRunUserInitiatedJobsPermission for package " + packageName);
            }
            return com.android.server.job.JobSchedulerService.this.checkRunUserInitiatedJobsPermission(uid, packageName);
        }

        /* JADX WARN: Code restructure failed: missing block: B:28:0x0066, code lost:
        
            if (r2 >= r10.length) goto L35;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x0068, code lost:
        
            r3 = r10[r2];
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x007a, code lost:
        
            r0 = r7.this$0.getContext().getPackageManager().getPackageUid(r3, 4194304);
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x007d, code lost:
        
            r9.println("Invalid package: " + r3);
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x0093, code lost:
        
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void dump(java.io.FileDescriptor r8, java.io.PrintWriter r9, java.lang.String[] r10) throws android.content.pm.PackageManager.NameNotFoundException {
            /*
                r7 = this;
                com.android.server.job.JobSchedulerService r0 = com.android.server.job.JobSchedulerService.this
                android.content.Context r0 = r0.getContext()
                java.lang.String r1 = "JobScheduler"
                boolean r0 = com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(r0, r1, r9)
                if (r0 != 0) goto Lf
                return
            Lf:
                r0 = -1
                r1 = 0
                boolean r2 = com.android.internal.util.ArrayUtils.isEmpty(r10)
                if (r2 != 0) goto L94
                r2 = 0
            L18:
                int r3 = r10.length
                if (r2 >= r3) goto L65
                r3 = r10[r2]
                java.lang.String r4 = "-h"
                boolean r4 = r4.equals(r3)
                if (r4 == 0) goto L29
                com.android.server.job.JobSchedulerService.dumpHelp(r9)
                return
            L29:
                java.lang.String r4 = "-a"
                boolean r4 = r4.equals(r3)
                if (r4 == 0) goto L32
                goto L3b
            L32:
                java.lang.String r4 = "--proto"
                boolean r4 = r4.equals(r3)
                if (r4 == 0) goto L3f
                r1 = 1
            L3b:
                int r2 = r2 + 1
                goto L18
            L3f:
                int r4 = r3.length()
                if (r4 <= 0) goto L65
                r4 = 0
                char r4 = r3.charAt(r4)
                r5 = 45
                if (r4 != r5) goto L65
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Unknown option: "
                java.lang.StringBuilder r4 = r4.append(r5)
                java.lang.StringBuilder r4 = r4.append(r3)
                java.lang.String r4 = r4.toString()
                r9.println(r4)
                return
            L65:
                int r3 = r10.length
                if (r2 >= r3) goto L94
                r3 = r10[r2]
                com.android.server.job.JobSchedulerService r4 = com.android.server.job.JobSchedulerService.this     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L7c
                android.content.Context r4 = r4.getContext()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L7c
                android.content.pm.PackageManager r4 = r4.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L7c
                r5 = 4194304(0x400000, float:5.877472E-39)
                int r4 = r4.getPackageUid(r3, r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L7c
                r0 = r4
                goto L94
            L7c:
                r4 = move-exception
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r6 = "Invalid package: "
                java.lang.StringBuilder r5 = r5.append(r6)
                java.lang.StringBuilder r5 = r5.append(r3)
                java.lang.String r5 = r5.toString()
                r9.println(r5)
                return
            L94:
                long r2 = android.os.Binder.clearCallingIdentity()
                if (r1 == 0) goto La0
                com.android.server.job.JobSchedulerService r4 = com.android.server.job.JobSchedulerService.this     // Catch: java.lang.Throwable -> Lb1
                r4.dumpInternalProto(r8, r0)     // Catch: java.lang.Throwable -> Lb1
                goto Lac
            La0:
                com.android.server.job.JobSchedulerService r4 = com.android.server.job.JobSchedulerService.this     // Catch: java.lang.Throwable -> Lb1
                android.util.IndentingPrintWriter r5 = new android.util.IndentingPrintWriter     // Catch: java.lang.Throwable -> Lb1
                java.lang.String r6 = "  "
                r5.<init>(r9, r6)     // Catch: java.lang.Throwable -> Lb1
                r4.dumpInternal(r5, r0)     // Catch: java.lang.Throwable -> Lb1
            Lac:
                android.os.Binder.restoreCallingIdentity(r2)
                return
            Lb1:
                r4 = move-exception
                android.os.Binder.restoreCallingIdentity(r2)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerService.JobSchedulerStub.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
            return new com.android.server.job.JobSchedulerShellCommand(com.android.server.job.JobSchedulerService.this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
        }

        public java.util.List<android.app.job.JobInfo> getStartedJobs() {
            java.util.ArrayList<android.app.job.JobInfo> runningJobs;
            int uid = android.os.Binder.getCallingUid();
            if (uid != 1000) {
                throw new java.lang.SecurityException("getStartedJobs() is system internal use only.");
            }
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> runningJobStatuses = com.android.server.job.JobSchedulerService.this.mConcurrencyManager.getRunningJobsLocked();
                runningJobs = new java.util.ArrayList<>(runningJobStatuses.size());
                for (int i = runningJobStatuses.size() - 1; i >= 0; i--) {
                    com.android.server.job.controllers.JobStatus job = runningJobStatuses.valueAt(i);
                    if (job != null) {
                        runningJobs.add(job.getJob());
                    }
                }
            }
            return runningJobs;
        }

        public android.content.pm.ParceledListSlice<android.app.job.JobSnapshot> getAllJobSnapshots() {
            android.content.pm.ParceledListSlice<android.app.job.JobSnapshot> parceledListSlice;
            int uid = android.os.Binder.getCallingUid();
            if (uid != 1000) {
                throw new java.lang.SecurityException("getAllJobSnapshots() is system internal use only.");
            }
            synchronized (com.android.server.job.JobSchedulerService.this.mLock) {
                final java.util.ArrayList<android.app.job.JobSnapshot> snapshots = new java.util.ArrayList<>(com.android.server.job.JobSchedulerService.this.mJobs.size());
                com.android.server.job.JobSchedulerService.this.mJobs.forEachJob(new java.util.function.Consumer() { // from class: com.android.server.job.JobSchedulerService$JobSchedulerStub$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$getAllJobSnapshots$0(snapshots, (com.android.server.job.controllers.JobStatus) obj);
                    }
                });
                parceledListSlice = new android.content.pm.ParceledListSlice<>(snapshots);
            }
            return parceledListSlice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getAllJobSnapshots$0(java.util.ArrayList snapshots, com.android.server.job.controllers.JobStatus job) {
            snapshots.add(new android.app.job.JobSnapshot(job.getJob(), job.getSatisfiedConstraintFlags(), com.android.server.job.JobSchedulerService.this.isReadyToBeExecutedLocked(job)));
        }

        public void registerUserVisibleJobObserver(android.app.job.IUserVisibleJobObserver observer) {
            super.registerUserVisibleJobObserver_enforcePermission();
            if (observer == null) {
                throw new java.lang.NullPointerException("observer");
            }
            com.android.server.job.JobSchedulerService.this.mUserVisibleJobObservers.register(observer);
            com.android.server.job.JobSchedulerService.this.mHandler.obtainMessage(10, observer).sendToTarget();
        }

        public void unregisterUserVisibleJobObserver(android.app.job.IUserVisibleJobObserver observer) {
            super.unregisterUserVisibleJobObserver_enforcePermission();
            if (observer == null) {
                throw new java.lang.NullPointerException("observer");
            }
            com.android.server.job.JobSchedulerService.this.mUserVisibleJobObservers.unregister(observer);
        }

        public void notePendingUserRequestedAppStop(java.lang.String packageName, int userId, java.lang.String debugReason) {
            super.notePendingUserRequestedAppStop_enforcePermission();
            if (packageName == null) {
                throw new java.lang.NullPointerException(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            }
            com.android.server.job.JobSchedulerService.this.notePendingUserRequestedAppStopInternal(packageName, userId, debugReason);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4 */
    int executeRunCommand(java.lang.String pkgName, int userId, java.lang.String namespace, int jobId, boolean satisfied, boolean force) throws java.lang.Throwable {
        java.lang.Object obj;
        com.android.server.job.controllers.JobStatus js;
        java.lang.Object obj2;
        int i;
        int i2 = pkgName;
        android.util.Slog.d("JobScheduler", "executeRunCommand(): " + ((java.lang.String) i2) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + namespace + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + userId + " " + jobId + " s=" + satisfied + " f=" + force);
        java.util.concurrent.CountDownLatch delayLatch = new java.util.concurrent.CountDownLatch(1);
        try {
            int uid = android.app.AppGlobals.getPackageManager().getPackageUid((java.lang.String) i2, 0L, userId != -1 ? userId : 0);
            if (uid < 0) {
                return -1000;
            }
            java.lang.Object obj3 = this.mLock;
            try {
            } catch (java.lang.Throwable th) {
                th = th;
            }
            synchronized (obj3) {
                try {
                    com.android.server.job.controllers.JobStatus js2 = this.mJobs.getJobByUidAndJobId(uid, namespace, jobId);
                    try {
                        if (js2 != null) {
                            js2.overrideState = force ? 3 : satisfied ? 1 : 2;
                            for (int c = this.mControllers.size() - 1; c >= 0; c--) {
                                this.mControllers.get(c).evaluateStateLocked(js2);
                            }
                            if (js2.isConstraintsSatisfied()) {
                                js = js2;
                                obj2 = obj3;
                                i = 0;
                                delayLatch.countDown();
                            } else {
                                if (!js2.hasConnectivityConstraint() || js2.isConstraintSatisfied(268435456) || !js2.wouldBeReadyWithConstraint(268435456)) {
                                    com.android.server.job.controllers.JobStatus js3 = js2;
                                    java.lang.Object obj4 = obj3;
                                    int i3 = 0;
                                    js3.overrideState = i3;
                                    return com.android.server.job.JobSchedulerShellCommand.CMD_ERR_CONSTRAINTS;
                                }
                                js = js2;
                                obj2 = obj3;
                                i = 0;
                                this.mHandler.postDelayed(checkConstraintRunnableForTesting(this.mHandler, js2, delayLatch, 5, 1000L), 1000L);
                            }
                            try {
                                delayLatch.await(7L, java.util.concurrent.TimeUnit.SECONDS);
                            } catch (java.lang.InterruptedException e) {
                                android.util.Slog.e("JobScheduler", "Couldn't wait for asynchronous constraint change", e);
                            }
                            synchronized (this.mLock) {
                                if (!js.isConstraintsSatisfied()) {
                                    js.overrideState = i;
                                    return com.android.server.job.JobSchedulerShellCommand.CMD_ERR_CONSTRAINTS;
                                }
                                queueReadyJobsForExecutionLocked();
                                maybeRunPendingJobsLocked();
                                return i;
                            }
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        obj = obj3;
                        i2 = 0;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    obj = obj3;
                    i2 = 0;
                }
                try {
                    throw th;
                } catch (android.os.RemoteException e2) {
                    return i2;
                }
            }
            return com.android.server.job.JobSchedulerShellCommand.CMD_ERR_NO_JOB;
        } catch (android.os.RemoteException e3) {
            return 0;
        }
    }

    private static java.lang.Runnable checkConstraintRunnableForTesting(final android.os.Handler handler, final com.android.server.job.controllers.JobStatus js, final java.util.concurrent.CountDownLatch latch, final int remainingAttempts, final long delayMs) {
        return new java.lang.Runnable() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.job.JobSchedulerService.lambda$checkConstraintRunnableForTesting$8(remainingAttempts, js, latch, handler, delayMs);
            }
        };
    }

    static /* synthetic */ void lambda$checkConstraintRunnableForTesting$8(int remainingAttempts, com.android.server.job.controllers.JobStatus js, java.util.concurrent.CountDownLatch latch, android.os.Handler handler, long delayMs) {
        if (remainingAttempts <= 0 || js.isConstraintsSatisfied()) {
            latch.countDown();
        } else {
            handler.postDelayed(checkConstraintRunnableForTesting(handler, js, latch, remainingAttempts - 1, delayMs), delayMs);
        }
    }

    int executeStopCommand(java.io.PrintWriter pw, java.lang.String pkgName, int userId, java.lang.String namespace, boolean hasJobId, int jobId, int stopReason, int internalStopReason) throws java.lang.Throwable {
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "executeStopJobCommand(): " + pkgName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + userId + " " + jobId + ": " + stopReason + "(" + android.app.job.JobParameters.getInternalReasonCodeDescription(internalStopReason) + ")");
        }
        synchronized (this.mLock) {
            try {
                try {
                    boolean foundSome = this.mConcurrencyManager.executeStopCommandLocked(pw, pkgName, userId, namespace, hasJobId, jobId, stopReason, internalStopReason);
                    if (!foundSome) {
                        pw.println("No matching executing jobs found.");
                    }
                    return 0;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    int executeCancelCommand(java.io.PrintWriter pw, java.lang.String pkgName, int userId, java.lang.String namespace, boolean hasJobId, int jobId) {
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "executeCancelCommand(): " + pkgName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + userId + " " + jobId);
        }
        int pkgUid = -1;
        try {
            android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
            pkgUid = pm.getPackageUid(pkgName, 0L, userId);
        } catch (android.os.RemoteException e) {
        }
        if (pkgUid < 0) {
            pw.println("Package " + pkgName + " not found.");
            return -1000;
        }
        if (!hasJobId) {
            pw.println("Canceling all jobs for " + pkgName + " in user " + userId);
            if (!cancelJobsForUid(pkgUid, false, 13, 0, "cancel shell command for package")) {
                pw.println("No matching jobs found.");
                return 0;
            }
            return 0;
        }
        pw.println("Canceling job " + pkgName + "/#" + jobId + " in user " + userId);
        if (!cancelJob(pkgUid, namespace, jobId, 2000, 13)) {
            pw.println("No matching job found.");
            return 0;
        }
        return 0;
    }

    void setFlexPolicy(boolean override, int appliedConstraints) {
        if (DEBUG) {
            android.util.Slog.v("JobScheduler", "setFlexPolicy(): " + override + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + appliedConstraints);
        }
        this.mFlexibilityController.setLocalPolicyForTesting(override, appliedConstraints);
    }

    void setMonitorBattery(boolean enabled) {
        synchronized (this.mLock) {
            this.mBatteryStateTracker.setMonitorBatteryLocked(enabled);
        }
    }

    int getBatterySeq() {
        int seq;
        synchronized (this.mLock) {
            seq = this.mBatteryStateTracker.getSeq();
        }
        return seq;
    }

    public boolean isBatteryCharging() {
        boolean zIsCharging;
        synchronized (this.mLock) {
            zIsCharging = this.mBatteryStateTracker.isCharging();
        }
        return zIsCharging;
    }

    public boolean isBatteryNotLow() {
        boolean zIsBatteryNotLow;
        synchronized (this.mLock) {
            zIsBatteryNotLow = this.mBatteryStateTracker.isBatteryNotLow();
        }
        return zIsBatteryNotLow;
    }

    public boolean isPowerConnected() {
        boolean zIsPowerConnected;
        synchronized (this.mLock) {
            zIsPowerConnected = this.mBatteryStateTracker.isPowerConnected();
        }
        return zIsPowerConnected;
    }

    void setCacheConfigChanges(boolean enabled) {
        synchronized (this.mLock) {
            this.mConstantsObserver.setCacheConfigChangesLocked(enabled);
        }
    }

    java.lang.String getConfigValue(java.lang.String key) {
        java.lang.String valueLocked;
        synchronized (this.mLock) {
            valueLocked = this.mConstantsObserver.getValueLocked(key);
        }
        return valueLocked;
    }

    int getStorageSeq() {
        int seq;
        synchronized (this.mLock) {
            seq = this.mStorageController.getTracker().getSeq();
        }
        return seq;
    }

    boolean getStorageNotLow() {
        boolean zIsStorageNotLow;
        synchronized (this.mLock) {
            zIsStorageNotLow = this.mStorageController.getTracker().isStorageNotLow();
        }
        return zIsStorageNotLow;
    }

    int getEstimatedNetworkBytes(java.io.PrintWriter pw, java.lang.String pkgName, int userId, java.lang.String namespace, int jobId, int byteOption) {
        long downloadBytes;
        long uploadBytes;
        try {
            int uid = android.app.AppGlobals.getPackageManager().getPackageUid(pkgName, 0L, userId != -1 ? userId : 0);
            if (uid < 0) {
                pw.print("unknown(");
                pw.print(pkgName);
                pw.println(")");
                return -1000;
            }
            synchronized (this.mLock) {
                com.android.server.job.controllers.JobStatus js = this.mJobs.getJobByUidAndJobId(uid, namespace, jobId);
                if (DEBUG) {
                    android.util.Slog.d("JobScheduler", "get-estimated-network-bytes " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + namespace + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + jobId + ": " + js);
                }
                if (js == null) {
                    pw.print("unknown(");
                    android.os.UserHandle.formatUid(pw, uid);
                    pw.print("/jid");
                    pw.print(jobId);
                    pw.println(")");
                    return com.android.server.job.JobSchedulerShellCommand.CMD_ERR_NO_JOB;
                }
                android.util.Pair<java.lang.Long, java.lang.Long> bytes = this.mConcurrencyManager.getEstimatedNetworkBytesLocked(pkgName, uid, namespace, jobId);
                if (bytes == null) {
                    downloadBytes = js.getEstimatedNetworkDownloadBytes();
                    uploadBytes = js.getEstimatedNetworkUploadBytes();
                } else {
                    downloadBytes = ((java.lang.Long) bytes.first).longValue();
                    uploadBytes = ((java.lang.Long) bytes.second).longValue();
                }
                if (byteOption == 0) {
                    pw.println(downloadBytes);
                } else {
                    pw.println(uploadBytes);
                }
                pw.println();
            }
        } catch (android.os.RemoteException e) {
        }
        return 0;
    }

    int getTransferredNetworkBytes(java.io.PrintWriter pw, java.lang.String pkgName, int userId, java.lang.String namespace, int jobId, int byteOption) {
        long downloadBytes;
        long uploadBytes;
        try {
            int uid = android.app.AppGlobals.getPackageManager().getPackageUid(pkgName, 0L, userId != -1 ? userId : 0);
            if (uid < 0) {
                pw.print("unknown(");
                pw.print(pkgName);
                pw.println(")");
                return -1000;
            }
            synchronized (this.mLock) {
                com.android.server.job.controllers.JobStatus js = this.mJobs.getJobByUidAndJobId(uid, namespace, jobId);
                if (DEBUG) {
                    android.util.Slog.d("JobScheduler", "get-transferred-network-bytes " + uid + namespace + "//" + jobId + ": " + js);
                }
                if (js == null) {
                    pw.print("unknown(");
                    android.os.UserHandle.formatUid(pw, uid);
                    pw.print("/jid");
                    pw.print(jobId);
                    pw.println(")");
                    return com.android.server.job.JobSchedulerShellCommand.CMD_ERR_NO_JOB;
                }
                android.util.Pair<java.lang.Long, java.lang.Long> bytes = this.mConcurrencyManager.getTransferredNetworkBytesLocked(pkgName, uid, namespace, jobId);
                if (bytes == null) {
                    downloadBytes = 0;
                    uploadBytes = 0;
                } else {
                    downloadBytes = ((java.lang.Long) bytes.first).longValue();
                    uploadBytes = ((java.lang.Long) bytes.second).longValue();
                }
                if (byteOption == 0) {
                    pw.println(downloadBytes);
                } else {
                    pw.println(uploadBytes);
                }
                pw.println();
            }
        } catch (android.os.RemoteException e) {
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkRunUserInitiatedJobsPermission(int packageUid, java.lang.String packageName) {
        return getRunUserInitiatedJobsPermissionState(packageUid, packageName) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRunUserInitiatedJobsPermissionState(int packageUid, java.lang.String packageName) {
        return android.content.PermissionChecker.checkPermissionForPreflight(getTestableContext(), "android.permission.RUN_USER_INITIATED_JOBS", -1, packageUid, packageName);
    }

    protected com.android.server.job.controllers.ConnectivityController getConnectivityController() {
        return this.mConnectivityController;
    }

    protected com.android.server.job.controllers.QuotaController getQuotaController() {
        return this.mQuotaController;
    }

    protected void waitOnAsyncLoadingForTesting() throws java.lang.Exception {
        this.mStartControllerTrackingLatch.await();
    }

    int getJobState(java.io.PrintWriter pw, java.lang.String pkgName, int userId, java.lang.String namespace, int jobId) throws java.lang.Throwable {
        boolean printed;
        try {
            try {
                int uid = android.app.AppGlobals.getPackageManager().getPackageUid(pkgName, 0L, userId != -1 ? userId : 0);
                if (uid < 0) {
                    pw.print("unknown(");
                    pw.print(pkgName);
                    pw.println(")");
                    return -1000;
                }
                try {
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                synchronized (this.mLock) {
                    try {
                        com.android.server.job.controllers.JobStatus js = this.mJobs.getJobByUidAndJobId(uid, namespace, jobId);
                        if (DEBUG) {
                            try {
                                android.util.Slog.d("JobScheduler", "get-job-state " + namespace + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + jobId + ": " + js);
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                        if (js == null) {
                            pw.print("unknown(");
                            android.os.UserHandle.formatUid(pw, uid);
                            pw.print("/jid");
                            pw.print(jobId);
                            pw.println(")");
                            return com.android.server.job.JobSchedulerShellCommand.CMD_ERR_NO_JOB;
                        }
                        boolean printed2 = false;
                        if (this.mPendingJobQueue.contains(js)) {
                            pw.print("pending");
                            printed2 = true;
                        }
                        if (this.mConcurrencyManager.isJobRunningLocked(js)) {
                            if (printed2) {
                                pw.print(" ");
                            }
                            printed2 = true;
                            pw.println(com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE);
                        }
                        if (!com.android.internal.util.ArrayUtils.contains(this.mStartedUsers, js.getUserId())) {
                            if (printed2) {
                                pw.print(" ");
                            }
                            printed2 = true;
                            pw.println("user-stopped");
                        }
                        if (!com.android.internal.util.ArrayUtils.contains(this.mStartedUsers, js.getSourceUserId())) {
                            if (printed2) {
                                pw.print(" ");
                            }
                            printed2 = true;
                            pw.println("source-user-stopped");
                        }
                        if (!this.mBackingUpUids.get(js.getSourceUid())) {
                            printed = printed2;
                        } else {
                            if (printed2) {
                                pw.print(" ");
                            }
                            pw.println("backing-up");
                            printed = true;
                        }
                        boolean componentPresent = false;
                        try {
                            try {
                                componentPresent = android.app.AppGlobals.getPackageManager().getServiceInfo(js.getServiceComponent(), 268435456L, js.getUserId()) != null;
                            } catch (android.os.RemoteException e) {
                            }
                        } catch (android.os.RemoteException e2) {
                        }
                        if (!componentPresent) {
                            if (printed) {
                                pw.print(" ");
                            }
                            printed = true;
                            pw.println("no-component");
                        }
                        if (js.isReady()) {
                            if (printed) {
                                pw.print(" ");
                            }
                            printed = true;
                            pw.println("ready");
                        }
                        if (!printed) {
                            pw.print("waiting");
                        }
                        pw.println();
                        return 0;
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                    throw th;
                }
            } catch (android.os.RemoteException e3) {
                return 0;
            }
        } catch (android.os.RemoteException e4) {
            return 0;
        }
    }

    void resetExecutionQuota(java.lang.String pkgName, int userId) {
        synchronized (this.mLock) {
            this.mQuotaController.clearAppStatsLocked(userId, pkgName);
        }
    }

    void resetScheduleQuota() {
        this.mQuotaTracker.clear();
    }

    void triggerDockState(boolean idleState) {
        android.content.Intent dockIntent;
        if (idleState) {
            dockIntent = new android.content.Intent("android.intent.action.DOCK_IDLE");
        } else {
            dockIntent = new android.content.Intent("android.intent.action.DOCK_ACTIVE");
        }
        dockIntent.setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        dockIntent.addFlags(1342177280);
        getContext().sendBroadcastAsUser(dockIntent, android.os.UserHandle.ALL);
    }

    static void dumpHelp(java.io.PrintWriter pw) {
        pw.println("Job Scheduler (jobscheduler) dump options:");
        pw.println("  [-h] [package] ...");
        pw.println("    -h: print this help");
        pw.println("  [package] is an optional package name to limit the output to.");
    }

    private static void sortJobs(java.util.List<com.android.server.job.controllers.JobStatus> jobs) {
        java.util.Collections.sort(jobs, new java.util.Comparator<com.android.server.job.controllers.JobStatus>() { // from class: com.android.server.job.JobSchedulerService.6
            @Override // java.util.Comparator
            public int compare(com.android.server.job.controllers.JobStatus o1, com.android.server.job.controllers.JobStatus o2) {
                int uid1 = o1.getUid();
                int uid2 = o2.getUid();
                int id1 = o1.getJobId();
                int id2 = o2.getJobId();
                if (uid1 != uid2) {
                    return uid1 < uid2 ? -1 : 1;
                }
                if (id1 < id2) {
                    return -1;
                }
                return id1 > id2 ? 1 : 0;
            }
        });
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpInternal(android.util.IndentingPrintWriter pw, int filterUid) throws java.lang.Throwable {
        java.lang.Object obj;
        boolean z;
        boolean pendingPrinted;
        int pendingIdx;
        com.android.server.job.controllers.JobStatus job;
        boolean jobPrinted;
        java.util.List<com.android.server.job.controllers.JobStatus> jobs;
        boolean z2;
        final int filterAppId = android.os.UserHandle.getAppId(filterUid);
        long now = sSystemClock.millis();
        long nowElapsed = sElapsedRealtimeClock.millis();
        long nowUptime = sUptimeMillisClock.millis();
        java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate = new java.util.function.Predicate() { // from class: com.android.server.job.JobSchedulerService$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj2) {
                return com.android.server.job.JobSchedulerService.lambda$dumpInternal$9(filterAppId, (com.android.server.job.controllers.JobStatus) obj2);
            }
        };
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                try {
                    this.mConstants.dump(pw);
                    for (com.android.server.job.controllers.StateController controller : this.mControllers) {
                        try {
                            pw.increaseIndent();
                            controller.dumpConstants(pw);
                            pw.decreaseIndent();
                        } catch (java.lang.Throwable th) {
                            th = th;
                            obj = obj2;
                        }
                    }
                    pw.println();
                    pw.println("Aconfig flags:");
                    pw.increaseIndent();
                    pw.print(com.android.server.job.Flags.FLAG_BATCH_ACTIVE_BUCKET_JOBS, java.lang.Boolean.valueOf(com.android.server.job.Flags.batchActiveBucketJobs()));
                    pw.println();
                    pw.print(com.android.server.job.Flags.FLAG_BATCH_CONNECTIVITY_JOBS_PER_NETWORK, java.lang.Boolean.valueOf(com.android.server.job.Flags.batchConnectivityJobsPerNetwork()));
                    pw.println();
                    pw.print(com.android.server.job.Flags.FLAG_DO_NOT_FORCE_RUSH_EXECUTION_AT_BOOT, java.lang.Boolean.valueOf(com.android.server.job.Flags.doNotForceRushExecutionAtBoot()));
                    pw.println();
                    pw.print("android.app.job.backup_jobs_exemption", java.lang.Boolean.valueOf(com.android.internal.hidden_from_bootclasspath.android.app.job.Flags.backupJobsExemption()));
                    pw.println();
                    pw.decreaseIndent();
                    pw.println();
                    boolean z3 = true;
                    for (int i = this.mJobRestrictions.size() - 1; i >= 0; i--) {
                        this.mJobRestrictions.get(i).dumpConstants(pw);
                    }
                    pw.println();
                    this.mQuotaTracker.dump(pw);
                    pw.println();
                    pw.print("Power connected: ");
                    pw.println(this.mBatteryStateTracker.isPowerConnected());
                    pw.print("Battery charging: ");
                    pw.println(this.mBatteryStateTracker.mCharging);
                    pw.print("Considered charging: ");
                    pw.println(this.mBatteryStateTracker.isConsideredCharging());
                    pw.print("Battery level: ");
                    pw.println(this.mBatteryStateTracker.mBatteryLevel);
                    pw.print("Battery not low: ");
                    pw.println(this.mBatteryStateTracker.isBatteryNotLow());
                    if (this.mBatteryStateTracker.isMonitoring()) {
                        pw.print("MONITORING: seq=");
                        pw.println(this.mBatteryStateTracker.getSeq());
                    }
                    pw.println();
                    pw.println("Started users: " + java.util.Arrays.toString(this.mStartedUsers));
                    pw.println();
                    pw.print("Media Cloud Providers: ");
                    pw.println(this.mCloudMediaProviderPackages);
                    pw.println();
                    pw.print("Registered ");
                    pw.print(this.mJobs.size());
                    pw.println(" jobs:");
                    pw.increaseIndent();
                    boolean jobPrinted2 = false;
                    if (this.mJobs.size() <= 0) {
                        z = true;
                    } else {
                        java.util.List<com.android.server.job.controllers.JobStatus> jobs2 = this.mJobs.mJobSet.getAllJobs();
                        sortJobs(jobs2);
                        for (com.android.server.job.controllers.JobStatus job2 : jobs2) {
                            if (predicate.test(job2)) {
                                boolean jobPrinted3 = true;
                                pw.print("JOB ");
                                job2.printUniqueId(pw);
                                pw.print(": ");
                                pw.println(job2.toShortStringExceptUniqueId());
                                pw.increaseIndent();
                                job2.dump(pw, z3, nowElapsed);
                                pw.print("Restricted due to:");
                                boolean isRestricted = checkIfRestricted(job2) != null ? z3 : false;
                                if (isRestricted) {
                                    z2 = true;
                                    int i2 = this.mJobRestrictions.size() - 1;
                                    while (i2 >= 0) {
                                        boolean jobPrinted4 = jobPrinted3;
                                        com.android.server.job.restrictions.JobRestriction restriction = this.mJobRestrictions.get(i2);
                                        java.util.List<com.android.server.job.controllers.JobStatus> jobs3 = jobs2;
                                        if (restriction.isJobRestricted(job2, evaluateJobBiasLocked(job2))) {
                                            int reason = restriction.getInternalReason();
                                            pw.print(" ");
                                            pw.print(android.app.job.JobParameters.getInternalReasonCodeDescription(reason));
                                        }
                                        i2--;
                                        jobPrinted3 = jobPrinted4;
                                        jobs2 = jobs3;
                                    }
                                    jobPrinted = jobPrinted3;
                                    jobs = jobs2;
                                } else {
                                    jobPrinted = true;
                                    jobs = jobs2;
                                    z2 = z3;
                                    pw.print(" none");
                                }
                                pw.println(".");
                                pw.print("Ready: ");
                                pw.print(isReadyToBeExecutedLocked(job2));
                                pw.print(" (job=");
                                pw.print(job2.isReady());
                                pw.print(" user=");
                                pw.print(areUsersStartedLocked(job2));
                                pw.print(" !restricted=");
                                pw.print(!isRestricted ? z2 : false);
                                pw.print(" !pending=");
                                pw.print(!this.mPendingJobQueue.contains(job2) ? z2 : false);
                                pw.print(" !active=");
                                pw.print(!this.mConcurrencyManager.isJobRunningLocked(job2) ? z2 : false);
                                pw.print(" !backingup=");
                                pw.print(!this.mBackingUpUids.get(job2.getSourceUid()) ? z2 : false);
                                pw.print(" comp=");
                                pw.print(isComponentUsable(job2));
                                pw.println(")");
                                pw.decreaseIndent();
                                z3 = z2;
                                jobPrinted2 = jobPrinted;
                                jobs2 = jobs;
                            }
                        }
                        z = z3;
                    }
                    if (!jobPrinted2) {
                        pw.println("None.");
                    }
                    pw.decreaseIndent();
                    for (int i3 = 0; i3 < this.mControllers.size(); i3++) {
                        pw.println();
                        pw.println(this.mControllers.get(i3).getClass().getSimpleName() + ":");
                        pw.increaseIndent();
                        this.mControllers.get(i3).dumpControllerStateLocked(pw, predicate);
                        pw.decreaseIndent();
                    }
                    boolean procStatePrinted = false;
                    for (int i4 = 0; i4 < this.mUidProcStates.size(); i4++) {
                        int uid = this.mUidProcStates.keyAt(i4);
                        if (filterAppId == -1 || filterAppId == android.os.UserHandle.getAppId(uid)) {
                            if (!procStatePrinted) {
                                procStatePrinted = true;
                                pw.println();
                                pw.println("Uid proc states:");
                                pw.increaseIndent();
                            }
                            pw.print(android.os.UserHandle.formatUid(uid));
                            pw.print(": ");
                            pw.println(android.app.ActivityManager.procStateToString(this.mUidProcStates.valueAt(i4)));
                        }
                    }
                    if (procStatePrinted) {
                        pw.decreaseIndent();
                    }
                    boolean overridePrinted = false;
                    for (int i5 = 0; i5 < this.mUidBiasOverride.size(); i5++) {
                        int uid2 = this.mUidBiasOverride.keyAt(i5);
                        if (filterAppId == -1 || filterAppId == android.os.UserHandle.getAppId(uid2)) {
                            if (!overridePrinted) {
                                overridePrinted = true;
                                pw.println();
                                pw.println("Uid bias overrides:");
                                pw.increaseIndent();
                            }
                            pw.print(android.os.UserHandle.formatUid(uid2));
                            pw.print(": ");
                            pw.println(this.mUidBiasOverride.valueAt(i5));
                        }
                    }
                    if (overridePrinted) {
                        pw.decreaseIndent();
                    }
                    boolean capabilitiesPrinted = false;
                    for (int i6 = 0; i6 < this.mUidCapabilities.size(); i6++) {
                        int uid3 = this.mUidCapabilities.keyAt(i6);
                        if (filterAppId == -1 || filterAppId == android.os.UserHandle.getAppId(uid3)) {
                            if (!capabilitiesPrinted) {
                                capabilitiesPrinted = true;
                                pw.println();
                                pw.println("Uid capabilities:");
                                pw.increaseIndent();
                            }
                            pw.print(android.os.UserHandle.formatUid(uid3));
                            pw.print(": ");
                            pw.println(android.app.ActivityManager.getCapabilitiesSummary(this.mUidCapabilities.valueAt(i6)));
                        }
                    }
                    if (capabilitiesPrinted) {
                        pw.decreaseIndent();
                    }
                    boolean uidMapPrinted = false;
                    for (int i7 = 0; i7 < this.mUidToPackageCache.size(); i7++) {
                        int uid4 = this.mUidToPackageCache.keyAt(i7);
                        if (filterUid == -1 || filterUid == uid4) {
                            if (!uidMapPrinted) {
                                uidMapPrinted = true;
                                pw.println();
                                pw.println("Cached UID->package map:");
                                pw.increaseIndent();
                            }
                            pw.print(uid4);
                            pw.print(": ");
                            pw.println(this.mUidToPackageCache.get(uid4));
                        }
                    }
                    if (uidMapPrinted) {
                        pw.decreaseIndent();
                    }
                    boolean backingPrinted = false;
                    for (int i8 = 0; i8 < this.mBackingUpUids.size(); i8++) {
                        int uid5 = this.mBackingUpUids.keyAt(i8);
                        if (filterAppId == -1 || filterAppId == android.os.UserHandle.getAppId(uid5)) {
                            if (!backingPrinted) {
                                pw.println();
                                pw.println("Backing up uids:");
                                pw.increaseIndent();
                                backingPrinted = true;
                            } else {
                                pw.print(", ");
                            }
                            pw.print(android.os.UserHandle.formatUid(uid5));
                        }
                    }
                    if (backingPrinted) {
                        pw.decreaseIndent();
                        pw.println();
                    }
                    pw.println();
                    this.mJobPackageTracker.dump(pw, filterAppId);
                    pw.println();
                    if (this.mJobPackageTracker.dumpHistory(pw, filterAppId)) {
                        pw.println();
                    }
                    pw.println("Pending queue:");
                    pw.increaseIndent();
                    this.mPendingJobQueue.resetIterator();
                    pendingPrinted = false;
                    pendingIdx = 0;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    obj = obj2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
            while (true) {
                job = this.mPendingJobQueue.next();
                if (job == null) {
                    break;
                }
                int pendingIdx2 = pendingIdx + 1;
                if (!predicate.test(job)) {
                    pendingIdx = pendingIdx2;
                } else {
                    if (!pendingPrinted) {
                        pendingPrinted = true;
                    }
                    pw.print("Pending #");
                    pw.print(pendingIdx2);
                    pw.print(": ");
                    pw.println(job.toShortString());
                    pw.increaseIndent();
                    job.dump(pw, false, nowElapsed);
                    int bias = evaluateJobBiasLocked(job);
                    pw.print("Evaluated bias: ");
                    pw.println(android.app.job.JobInfo.getBiasString(bias));
                    pw.print("Enq: ");
                    pendingIdx = pendingIdx2;
                    android.util.TimeUtils.formatDuration(job.madePending - nowUptime, pw);
                    pw.decreaseIndent();
                    pw.println();
                }
                throw th;
            }
            if (!pendingPrinted) {
                pw.println(com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
            }
            pw.decreaseIndent();
            pw.println();
            boolean z4 = z;
            this.mConcurrencyManager.dumpContextInfoLocked(pw, predicate, nowElapsed, nowUptime);
            pw.println();
            pw.println("Recently completed jobs:");
            pw.increaseIndent();
            boolean recentPrinted = false;
            com.android.server.job.controllers.JobStatus job3 = job;
            for (int r = 1; r <= 20; r++) {
                int idx = ((this.mLastCompletedJobIndex + 20) - r) % 20;
                job3 = this.mLastCompletedJobs[idx];
                if (job3 != null && predicate.test(job3)) {
                    android.util.TimeUtils.formatDuration(this.mLastCompletedJobTimeElapsed[idx], nowElapsed, pw);
                    pw.println();
                    pw.increaseIndent();
                    pw.increaseIndent();
                    pw.println(job3.toShortString());
                    job3.dump(pw, z4, nowElapsed);
                    pw.decreaseIndent();
                    pw.decreaseIndent();
                    recentPrinted = true;
                }
            }
            if (!recentPrinted) {
                pw.println(com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
            }
            pw.decreaseIndent();
            pw.println();
            boolean recentCancellationsPrinted = false;
            for (int r2 = 1; r2 <= this.mLastCancelledJobs.length; r2++) {
                int idx2 = ((this.mLastCancelledJobIndex + this.mLastCancelledJobs.length) - r2) % this.mLastCancelledJobs.length;
                com.android.server.job.controllers.JobStatus job4 = this.mLastCancelledJobs[idx2];
                if (job4 != null && predicate.test(job4)) {
                    if (!recentCancellationsPrinted) {
                        pw.println();
                        pw.println("Recently cancelled jobs:");
                        pw.increaseIndent();
                        recentCancellationsPrinted = true;
                    }
                    android.util.TimeUtils.formatDuration(this.mLastCancelledJobTimeElapsed[idx2], nowElapsed, pw);
                    pw.println();
                    pw.increaseIndent();
                    pw.increaseIndent();
                    pw.println(job4.toShortString());
                    job4.dump(pw, z4, nowElapsed);
                    pw.decreaseIndent();
                    pw.decreaseIndent();
                }
            }
            if (!recentCancellationsPrinted) {
                pw.decreaseIndent();
                pw.println();
            }
            if (filterUid == -1) {
                pw.println();
                pw.print("mReadyToRock=");
                pw.println(this.mReadyToRock);
                pw.print("mReportedActive=");
                pw.println(this.mReportedActive);
            }
            pw.println();
            this.mConcurrencyManager.dumpLocked(pw, now, nowElapsed);
            pw.println();
            pw.print("PersistStats: ");
            pw.println(this.mJobs.getPersistStats());
        }
        this.mJobSchedulerServiceExt.dumpProxyJob(pw);
        this.mJobSchedulerServiceExt.dumpStateChanged(pw);
        pw.println();
    }

    static /* synthetic */ boolean lambda$dumpInternal$9(int filterAppId, com.android.server.job.controllers.JobStatus js) {
        return filterAppId == -1 || android.os.UserHandle.getAppId(js.getUid()) == filterAppId || android.os.UserHandle.getAppId(js.getSourceUid()) == filterAppId;
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x0211 A[Catch: all -> 0x0238, TRY_LEAVE, TryCatch #7 {all -> 0x0238, blocks: (B:59:0x020b, B:61:0x0211, B:70:0x0253, B:72:0x025b, B:74:0x0261), top: B:125:0x020b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void dumpInternalProto(java.io.FileDescriptor r32, int r33) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 824
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerService.dumpInternalProto(java.io.FileDescriptor, int):void");
    }

    static /* synthetic */ boolean lambda$dumpInternalProto$10(int filterAppId, com.android.server.job.controllers.JobStatus js) {
        return filterAppId == -1 || android.os.UserHandle.getAppId(js.getUid()) == filterAppId || android.os.UserHandle.getAppId(js.getSourceUid()) == filterAppId;
    }

    public com.android.server.job.IJobSchedulerServiceWrapper getWrapper() {
        return this.mJobSchedulerServiceWrapper;
    }

    private class JobSchedulerServiceWrapper implements com.android.server.job.IJobSchedulerServiceWrapper {
        private JobSchedulerServiceWrapper() {
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public com.android.server.job.IJobSchedulerServiceExt getExtImpl() {
            return com.android.server.job.JobSchedulerService.this.mJobSchedulerServiceExt;
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public int getMAX_JOBS_PER_APP() {
            return 150;
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public void cancelJobImplLocked(com.android.server.job.controllers.JobStatus cancelled, com.android.server.job.controllers.JobStatus incomingJob, int reason, int internalReasonCode, java.lang.String debugReason) {
            com.android.server.job.JobSchedulerService.this.cancelJobImplLocked(cancelled, incomingJob, reason, internalReasonCode, debugReason);
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public boolean stopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob, boolean removeFromPersisted) {
            return com.android.server.job.JobSchedulerService.this.stopTrackingJobLocked(jobStatus, incomingJob, removeFromPersisted);
        }

        @Override // com.android.server.job.IJobSchedulerServiceWrapper
        public void cancelJobsForPackageAndUidLocked(java.lang.String pkgName, int uid, int reason, int internalReasonCode, java.lang.String debugReason) {
            com.android.server.job.JobSchedulerService.this.cancelJobsForPackageAndUidLocked(pkgName, uid, true, true, reason, internalReasonCode, debugReason);
        }
    }
}
