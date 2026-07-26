package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
class JobConcurrencyManager {
    private static final int ALL_WORK_TYPES = 127;
    static final java.lang.String CONFIG_KEY_PREFIX_CONCURRENCY = "concurrency_";
    private static final com.android.server.job.JobConcurrencyManager.WorkConfigLimitsPerMemoryTrimLevel CONFIG_LIMITS_SCREEN_OFF;
    private static final com.android.server.job.JobConcurrencyManager.WorkConfigLimitsPerMemoryTrimLevel CONFIG_LIMITS_SCREEN_ON;
    private static final boolean DEBUG = com.android.server.job.JobSchedulerService.DEBUG;
    static final int DEFAULT_CONCURRENCY_LIMIT;
    private static final boolean DEFAULT_ENABLE_MAX_WAIT_TIME_BYPASS = true;
    static final long DEFAULT_MAX_WAIT_EJ_MS = 300000;
    static final long DEFAULT_MAX_WAIT_REGULAR_MS = 1800000;
    static final long DEFAULT_MAX_WAIT_UI_MS = 300000;
    private static final int DEFAULT_PKG_CONCURRENCY_LIMIT_EJ = 3;
    private static final int DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR;
    private static final long DEFAULT_SCREEN_OFF_ADJUSTMENT_DELAY_MS = 30000;
    private static final java.lang.String KEY_CONCURRENCY_LIMIT = "concurrency_limit";
    static final java.lang.String KEY_ENABLE_MAX_WAIT_TIME_BYPASS = "concurrency_enable_max_wait_time_bypass";
    private static final java.lang.String KEY_MAX_WAIT_EJ_MS = "concurrency_max_wait_ej_ms";
    private static final java.lang.String KEY_MAX_WAIT_REGULAR_MS = "concurrency_max_wait_regular_ms";
    static final java.lang.String KEY_MAX_WAIT_UI_MS = "concurrency_max_wait_ui_ms";
    static final java.lang.String KEY_PKG_CONCURRENCY_LIMIT_EJ = "concurrency_pkg_concurrency_limit_ej";
    static final java.lang.String KEY_PKG_CONCURRENCY_LIMIT_REGULAR = "concurrency_pkg_concurrency_limit_regular";
    private static final java.lang.String KEY_SCREEN_OFF_ADJUSTMENT_DELAY_MS = "concurrency_screen_off_adjustment_delay_ms";
    static final int MAX_CONCURRENCY_LIMIT = 64;
    private static final int MAX_RETAINED_OBJECTS = 96;
    static final int NUM_WORK_TYPES = 7;
    private static final int PRIVILEGED_STATE_BAL = 2;
    private static final int PRIVILEGED_STATE_NONE = 1;
    private static final int PRIVILEGED_STATE_TOP = 3;
    private static final int PRIVILEGED_STATE_UNDEFINED = 0;
    private static final int SYSTEM_STATE_REFRESH_MIN_INTERVAL = 1000;
    private static final java.lang.String TAG = "JobScheduler.Concurrency";
    static final int WORK_TYPE_BG = 16;
    static final int WORK_TYPE_BGUSER = 64;
    static final int WORK_TYPE_BGUSER_IMPORTANT = 32;
    static final int WORK_TYPE_EJ = 8;
    static final int WORK_TYPE_FGS = 2;
    static final int WORK_TYPE_NONE = 0;
    static final int WORK_TYPE_TOP = 1;
    static final int WORK_TYPE_UI = 4;
    private static final com.android.modules.expresslog.Histogram sConcurrencyHistogramLogger;
    private static final java.util.Comparator<com.android.server.job.JobConcurrencyManager.ContextAssignment> sDeterminationComparator;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.JobConcurrencyManager.PackageStats> mActivePkgStats;
    final java.util.List<com.android.server.job.JobServiceContext> mActiveServices;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.content.Context mContext;
    private final android.util.Pools.Pool<com.android.server.job.JobConcurrencyManager.ContextAssignment> mContextAssignmentPool;
    private boolean mCurrentInteractiveState;
    private boolean mEffectiveInteractiveState;
    com.android.server.job.JobConcurrencyManager.GracePeriodObserver mGracePeriodObserver;
    private final android.os.Handler mHandler;
    private final android.util.ArraySet<com.android.server.job.JobServiceContext> mIdleContexts;
    private final com.android.server.job.JobConcurrencyManager.Injector mInjector;
    public com.android.server.job.IJobConcurrencyManagerExt mJobConcurrencyManagerExt;
    private int mLastMemoryTrimLevel;
    private long mLastScreenOffRealtime;
    private long mLastScreenOnRealtime;
    private final java.lang.Object mLock;
    private long mMaxWaitEjMs;
    private long mMaxWaitRegularMs;
    private boolean mMaxWaitTimeBypassEnabled;
    private long mMaxWaitUIMs;
    private long mNextSystemStateRefreshTime;
    private final com.android.server.job.JobNotificationCoordinator mNotificationCoordinator;
    private int mNumDroppedContexts;
    private final java.util.function.Consumer<com.android.server.job.JobConcurrencyManager.PackageStats> mPackageStatsStagingCountClearer;
    private int mPkgConcurrencyLimitEj;
    private int mPkgConcurrencyLimitRegular;
    private final android.util.Pools.Pool<com.android.server.job.JobConcurrencyManager.PackageStats> mPkgStatsPool;
    private android.os.PowerManager mPowerManager;
    private final java.lang.Runnable mRampUpForScreenOff;
    private final android.content.BroadcastReceiver mReceiver;
    private final com.android.server.job.JobConcurrencyManager.AssignmentInfo mRecycledAssignmentInfo;
    private final android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> mRecycledChanged;
    private final android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> mRecycledIdle;
    private final java.util.ArrayList<com.android.server.job.JobConcurrencyManager.ContextAssignment> mRecycledPreferredUidOnly;
    private final android.util.SparseIntArray mRecycledPrivilegedState;
    private final java.util.ArrayList<com.android.server.job.JobConcurrencyManager.ContextAssignment> mRecycledStoppable;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mRunningJobs;
    private long mScreenOffAdjustmentDelayMs;
    private final com.android.server.job.JobSchedulerService mService;
    boolean mShouldRestrictBgUser;
    private final com.android.internal.util.StatLogger mStatLogger;
    private int mSteadyStateConcurrencyLimit;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private final com.android.server.job.JobConcurrencyManager.WorkCountTracker mWorkCountTracker;
    private com.android.server.job.JobConcurrencyManager.WorkTypeConfig mWorkTypeConfig;

    interface Stats {
        public static final int ASSIGN_JOBS_TO_CONTEXTS = 0;
        public static final int COUNT = 2;
        public static final int REFRESH_SYSTEM_STATE = 1;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface WorkType {
    }

    static {
        if (android.app.ActivityManager.isLowRamDeviceStatic()) {
            DEFAULT_CONCURRENCY_LIMIT = 8;
        } else {
            long ramBytes = new com.android.internal.util.MemInfoReader().getTotalSize();
            if (ramBytes <= android.util.DataUnit.GIGABYTES.toBytes(6L)) {
                DEFAULT_CONCURRENCY_LIMIT = 16;
            } else if (ramBytes <= android.util.DataUnit.GIGABYTES.toBytes(8L)) {
                DEFAULT_CONCURRENCY_LIMIT = 20;
            } else if (ramBytes <= android.util.DataUnit.GIGABYTES.toBytes(12L)) {
                DEFAULT_CONCURRENCY_LIMIT = 32;
            } else {
                DEFAULT_CONCURRENCY_LIMIT = 40;
            }
        }
        DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR = DEFAULT_CONCURRENCY_LIMIT / 2;
        CONFIG_LIMITS_SCREEN_ON = new com.android.server.job.JobConcurrencyManager.WorkConfigLimitsPerMemoryTrimLevel(new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_on_normal", DEFAULT_CONCURRENCY_LIMIT, (DEFAULT_CONCURRENCY_LIMIT * 3) / 4, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.4f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.2f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(16, java.lang.Float.valueOf(0.05f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.05f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.5f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.25f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.2f)))), new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_on_moderate", DEFAULT_CONCURRENCY_LIMIT, DEFAULT_CONCURRENCY_LIMIT / 2, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.4f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(16, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.1f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.4f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.1f)))), new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_on_low", DEFAULT_CONCURRENCY_LIMIT, (DEFAULT_CONCURRENCY_LIMIT * 4) / 10, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.6f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.1f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.33333334f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.16666667f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.16666667f)))), new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_on_critical", DEFAULT_CONCURRENCY_LIMIT, (DEFAULT_CONCURRENCY_LIMIT * 4) / 10, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.7f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.05f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.16666667f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.16666667f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.16666667f)))));
        CONFIG_LIMITS_SCREEN_OFF = new com.android.server.job.JobConcurrencyManager.WorkConfigLimitsPerMemoryTrimLevel(new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_off_normal", DEFAULT_CONCURRENCY_LIMIT, DEFAULT_CONCURRENCY_LIMIT, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.3f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.2f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.2f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.15f)), android.util.Pair.create(16, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.05f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.6f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.2f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.2f)))), new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_off_moderate", DEFAULT_CONCURRENCY_LIMIT, (DEFAULT_CONCURRENCY_LIMIT * 9) / 10, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.3f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.2f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.2f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.15f)), android.util.Pair.create(16, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.05f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.5f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.1f)))), new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_off_low", DEFAULT_CONCURRENCY_LIMIT, (DEFAULT_CONCURRENCY_LIMIT * 6) / 10, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.3f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.15f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.15f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(16, java.lang.Float.valueOf(0.05f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.05f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.25f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.1f)))), new com.android.server.job.JobConcurrencyManager.WorkTypeConfig("screen_off_critical", DEFAULT_CONCURRENCY_LIMIT, (DEFAULT_CONCURRENCY_LIMIT * 4) / 10, java.util.List.of(android.util.Pair.create(1, java.lang.Float.valueOf(0.3f)), android.util.Pair.create(2, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(4, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(8, java.lang.Float.valueOf(0.05f))), java.util.List.of(android.util.Pair.create(16, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(32, java.lang.Float.valueOf(0.1f)), android.util.Pair.create(64, java.lang.Float.valueOf(0.1f)))));
        sDeterminationComparator = new java.util.Comparator() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.job.JobConcurrencyManager.lambda$static$0((com.android.server.job.JobConcurrencyManager.ContextAssignment) obj, (com.android.server.job.JobConcurrencyManager.ContextAssignment) obj2);
            }
        };
        sConcurrencyHistogramLogger = new com.android.modules.expresslog.Histogram("job_scheduler.value_hist_job_concurrency", new com.android.modules.expresslog.Histogram.UniformOptions(100, 0.0f, 99.0f));
    }

    static java.lang.String workTypeToString(int workType) {
        switch (workType) {
            case 0:
                return "NONE";
            case 1:
                return "TOP";
            case 2:
                return "FGS";
            case 4:
                return "UI";
            case 8:
                return "EJ";
            case 16:
                return "BG";
            case 32:
                return "BGUSER_IMPORTANT";
            case 64:
                return "BGUSER";
            default:
                return "WORK(" + workType + ")";
        }
    }

    static /* synthetic */ int lambda$static$0(com.android.server.job.JobConcurrencyManager.ContextAssignment ca1, com.android.server.job.JobConcurrencyManager.ContextAssignment ca2) {
        if (ca1 == ca2) {
            return 0;
        }
        com.android.server.job.controllers.JobStatus js1 = ca1.context.getRunningJobLocked();
        com.android.server.job.controllers.JobStatus js2 = ca2.context.getRunningJobLocked();
        if (js1 == null) {
            if (js2 == null) {
                return 0;
            }
            return 1;
        }
        if (js2 == null) {
            return -1;
        }
        if (js1.lastEvaluatedBias == 40) {
            if (js2.lastEvaluatedBias != 40) {
                return -1;
            }
        } else if (js2.lastEvaluatedBias == 40) {
            return 1;
        }
        return java.lang.Long.compare(ca2.context.getExecutionStartTimeElapsed(), ca1.context.getExecutionStartTimeElapsed());
    }

    JobConcurrencyManager(com.android.server.job.JobSchedulerService service) {
        this(service, new com.android.server.job.JobConcurrencyManager.Injector());
    }

    JobConcurrencyManager(com.android.server.job.JobSchedulerService service, com.android.server.job.JobConcurrencyManager.Injector injector) {
        this.mRecycledChanged = new android.util.ArraySet<>();
        this.mRecycledIdle = new android.util.ArraySet<>();
        this.mRecycledPreferredUidOnly = new java.util.ArrayList<>();
        this.mRecycledStoppable = new java.util.ArrayList<>();
        this.mRecycledAssignmentInfo = new com.android.server.job.JobConcurrencyManager.AssignmentInfo();
        this.mRecycledPrivilegedState = new android.util.SparseIntArray();
        this.mContextAssignmentPool = new android.util.Pools.SimplePool(96);
        this.mActiveServices = new java.util.ArrayList();
        this.mIdleContexts = new android.util.ArraySet<>();
        this.mNumDroppedContexts = 0;
        this.mRunningJobs = new android.util.ArraySet<>();
        this.mWorkCountTracker = new com.android.server.job.JobConcurrencyManager.WorkCountTracker();
        this.mPkgStatsPool = new android.util.Pools.SimplePool(96);
        this.mActivePkgStats = new android.util.SparseArrayMap<>();
        this.mWorkTypeConfig = CONFIG_LIMITS_SCREEN_OFF.normal;
        this.mScreenOffAdjustmentDelayMs = 30000L;
        this.mSteadyStateConcurrencyLimit = DEFAULT_CONCURRENCY_LIMIT;
        this.mPkgConcurrencyLimitEj = 3;
        this.mPkgConcurrencyLimitRegular = DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR;
        this.mMaxWaitTimeBypassEnabled = true;
        this.mMaxWaitUIMs = 300000L;
        this.mMaxWaitEjMs = 300000L;
        this.mMaxWaitRegularMs = 1800000L;
        this.mPackageStatsStagingCountClearer = new java.util.function.Consumer() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.job.JobConcurrencyManager.PackageStats) obj).resetStagedCount();
            }
        };
        this.mStatLogger = new com.android.internal.util.StatLogger(new java.lang.String[]{"assignJobsToContexts", "refreshSystemState"});
        this.mJobConcurrencyManagerExt = (com.android.server.job.IJobConcurrencyManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.job.IJobConcurrencyManagerExt.class).base(this).create();
        this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.job.JobConcurrencyManager.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:17:0x0036  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r5, android.content.Intent r6) {
                /*
                    r4 = this;
                    java.lang.String r0 = r6.getAction()
                    int r1 = r0.hashCode()
                    r2 = 0
                    r3 = 1
                    switch(r1) {
                        case -2128145023: goto L2c;
                        case -1454123155: goto L22;
                        case 870701415: goto L18;
                        case 1779291251: goto Le;
                        default: goto Ld;
                    }
                Ld:
                    goto L36
                Le:
                    java.lang.String r1 = "android.os.action.POWER_SAVE_MODE_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = 3
                    goto L37
                L18:
                    java.lang.String r1 = "android.os.action.DEVICE_IDLE_MODE_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = 2
                    goto L37
                L22:
                    java.lang.String r1 = "android.intent.action.SCREEN_ON"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = r2
                    goto L37
                L2c:
                    java.lang.String r1 = "android.intent.action.SCREEN_OFF"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = r3
                    goto L37
                L36:
                    r0 = -1
                L37:
                    switch(r0) {
                        case 0: goto L94;
                        case 1: goto L8e;
                        case 2: goto L62;
                        case 3: goto L3b;
                        default: goto L3a;
                    }
                L3a:
                    goto L9a
                L3b:
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    android.os.PowerManager r0 = com.android.server.job.JobConcurrencyManager.m4488$$Nest$fgetmPowerManager(r0)
                    if (r0 == 0) goto L9a
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    android.os.PowerManager r0 = com.android.server.job.JobConcurrencyManager.m4488$$Nest$fgetmPowerManager(r0)
                    boolean r0 = r0.isPowerSaveMode()
                    if (r0 == 0) goto L9a
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    java.lang.Object r0 = com.android.server.job.JobConcurrencyManager.m4487$$Nest$fgetmLock(r0)
                    monitor-enter(r0)
                    com.android.server.job.JobConcurrencyManager r1 = com.android.server.job.JobConcurrencyManager.this     // Catch: java.lang.Throwable -> L5f
                    java.lang.String r2 = "battery saver"
                    com.android.server.job.JobConcurrencyManager.m4490$$Nest$mstopOvertimeJobsLocked(r1, r2)     // Catch: java.lang.Throwable -> L5f
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L5f
                    goto L9a
                L5f:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L5f
                    throw r1
                L62:
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    android.os.PowerManager r0 = com.android.server.job.JobConcurrencyManager.m4488$$Nest$fgetmPowerManager(r0)
                    if (r0 == 0) goto L9a
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    android.os.PowerManager r0 = com.android.server.job.JobConcurrencyManager.m4488$$Nest$fgetmPowerManager(r0)
                    boolean r0 = r0.isDeviceIdleMode()
                    if (r0 == 0) goto L9a
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    java.lang.Object r0 = com.android.server.job.JobConcurrencyManager.m4487$$Nest$fgetmLock(r0)
                    monitor-enter(r0)
                    com.android.server.job.JobConcurrencyManager r1 = com.android.server.job.JobConcurrencyManager.this     // Catch: java.lang.Throwable -> L8b
                    com.android.server.job.JobConcurrencyManager.m4491$$Nest$mstopUnexemptedJobsForDoze(r1)     // Catch: java.lang.Throwable -> L8b
                    com.android.server.job.JobConcurrencyManager r1 = com.android.server.job.JobConcurrencyManager.this     // Catch: java.lang.Throwable -> L8b
                    java.lang.String r2 = "deep doze"
                    com.android.server.job.JobConcurrencyManager.m4490$$Nest$mstopOvertimeJobsLocked(r1, r2)     // Catch: java.lang.Throwable -> L8b
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L8b
                    goto L9a
                L8b:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L8b
                    throw r1
                L8e:
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    com.android.server.job.JobConcurrencyManager.m4489$$Nest$monInteractiveStateChanged(r0, r2)
                    goto L9a
                L94:
                    com.android.server.job.JobConcurrencyManager r0 = com.android.server.job.JobConcurrencyManager.this
                    com.android.server.job.JobConcurrencyManager.m4489$$Nest$monInteractiveStateChanged(r0, r3)
                L9a:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobConcurrencyManager.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mRampUpForScreenOff = new java.lang.Runnable() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.rampUpForScreenOff();
            }
        };
        this.mService = service;
        this.mLock = this.mService.getLock();
        this.mContext = service.getTestableContext();
        this.mInjector = injector;
        this.mNotificationCoordinator = new com.android.server.job.JobNotificationCoordinator();
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mHandler = com.android.server.AppSchedulingModuleThread.getHandler();
        this.mGracePeriodObserver = new com.android.server.job.JobConcurrencyManager.GracePeriodObserver(this.mContext);
        this.mShouldRestrictBgUser = this.mContext.getResources().getBoolean(android.R.bool.config_hibernationDeletesOatArtifactsEnabled);
    }

    public void onSystemReady() {
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
        android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        filter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter);
        try {
            android.app.ActivityManager.getService().registerUserSwitchObserver(this.mGracePeriodObserver, TAG);
        } catch (android.os.RemoteException e) {
        }
        onInteractiveStateChanged(this.mPowerManager.isInteractive());
    }

    void onThirdPartyAppsCanStart() {
        com.android.internal.app.IBatteryStats batteryStats = com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats"));
        for (int i = 0; i < this.mSteadyStateConcurrencyLimit; i++) {
            this.mIdleContexts.add(this.mInjector.createJobServiceContext(this.mService, this, this.mNotificationCoordinator, batteryStats, this.mService.mJobPackageTracker, com.android.server.AppSchedulingModuleThread.get().getLooper()));
        }
    }

    void onAppRemovedLocked(java.lang.String pkgName, int uid) {
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats = (com.android.server.job.JobConcurrencyManager.PackageStats) this.mActivePkgStats.get(android.os.UserHandle.getUserId(uid), pkgName);
        if (packageStats != null) {
            if (packageStats.numRunningEj > 0 || packageStats.numRunningRegular > 0) {
                android.util.Slog.w(TAG, pkgName + "(" + uid + ") marked as removed before jobs stopped running");
            } else {
                this.mActivePkgStats.delete(android.os.UserHandle.getUserId(uid), pkgName);
            }
        }
    }

    void onUserRemoved(int userId) {
        this.mGracePeriodObserver.onUserRemoved(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInteractiveStateChanged(boolean interactive) {
        synchronized (this.mLock) {
            if (this.mCurrentInteractiveState == interactive) {
                return;
            }
            this.mCurrentInteractiveState = interactive;
            if (DEBUG) {
                android.util.Slog.d(TAG, "Interactive: " + interactive);
            }
            long nowRealtime = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (interactive) {
                this.mLastScreenOnRealtime = nowRealtime;
                this.mEffectiveInteractiveState = true;
                this.mHandler.removeCallbacks(this.mRampUpForScreenOff);
            } else {
                this.mLastScreenOffRealtime = nowRealtime;
                this.mHandler.postDelayed(this.mRampUpForScreenOff, this.mScreenOffAdjustmentDelayMs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rampUpForScreenOff() {
        synchronized (this.mLock) {
            if (this.mEffectiveInteractiveState) {
                if (this.mLastScreenOnRealtime > this.mLastScreenOffRealtime) {
                    return;
                }
                long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                if (this.mLastScreenOffRealtime + this.mScreenOffAdjustmentDelayMs > now) {
                    return;
                }
                this.mEffectiveInteractiveState = false;
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ramping up concurrency");
                }
                this.mService.maybeRunPendingJobsLocked();
            }
        }
    }

    android.util.ArraySet<com.android.server.job.controllers.JobStatus> getRunningJobsLocked() {
        return this.mRunningJobs;
    }

    boolean isJobRunningLocked(com.android.server.job.controllers.JobStatus job) {
        return this.mRunningJobs.contains(job);
    }

    boolean isJobInOvertimeLocked(com.android.server.job.controllers.JobStatus job) {
        if (!this.mRunningJobs.contains(job)) {
            return false;
        }
        for (int i = this.mActiveServices.size() - 1; i >= 0; i--) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus jobStatus = jsc.getRunningJobLocked();
            if (jobStatus == job) {
                return !jsc.isWithinExecutionGuaranteeTime();
            }
        }
        android.util.Slog.wtf(TAG, "Couldn't find long running job on a context");
        this.mRunningJobs.remove(job);
        return false;
    }

    private boolean isSimilarJobRunningLocked(com.android.server.job.controllers.JobStatus job) {
        for (int i = this.mRunningJobs.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus js = this.mRunningJobs.valueAt(i);
            if (job.matches(js.getUid(), js.getNamespace(), js.getJobId())) {
                return true;
            }
        }
        return false;
    }

    private boolean refreshSystemStateLocked() {
        long nowUptime = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        if (nowUptime < this.mNextSystemStateRefreshTime) {
            return false;
        }
        long start = this.mStatLogger.getTime();
        this.mNextSystemStateRefreshTime = 1000 + nowUptime;
        this.mLastMemoryTrimLevel = 0;
        try {
            this.mLastMemoryTrimLevel = android.app.ActivityManager.getService().getMemoryTrimLevel();
        } catch (android.os.RemoteException e) {
        }
        this.mStatLogger.logDurationStat(1, start);
        return true;
    }

    private void updateCounterConfigLocked() {
        if (!refreshSystemStateLocked()) {
            return;
        }
        com.android.server.job.JobConcurrencyManager.WorkConfigLimitsPerMemoryTrimLevel workConfigs = this.mEffectiveInteractiveState ? CONFIG_LIMITS_SCREEN_ON : CONFIG_LIMITS_SCREEN_OFF;
        switch (this.mLastMemoryTrimLevel) {
            case 1:
                this.mWorkTypeConfig = workConfigs.moderate;
                break;
            case 2:
                this.mWorkTypeConfig = workConfigs.low;
                break;
            case 3:
                this.mWorkTypeConfig = workConfigs.critical;
                break;
            default:
                this.mWorkTypeConfig = workConfigs.normal;
                break;
        }
        this.mWorkCountTracker.setConfig(this.mWorkTypeConfig);
    }

    void assignJobsToContextsLocked() {
        long start = this.mStatLogger.getTime();
        assignJobsToContextsInternalLocked();
        this.mStatLogger.logDurationStat(0, start);
    }

    private void assignJobsToContextsInternalLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, printPendingQueueLocked());
        }
        if (this.mService.getPendingJobQueue().size() == 0) {
            return;
        }
        prepareForAssignmentDeterminationLocked(this.mRecycledIdle, this.mRecycledPreferredUidOnly, this.mRecycledStoppable, this.mRecycledAssignmentInfo);
        if (DEBUG) {
            android.util.Slog.d(TAG, printAssignments("running jobs initial", this.mRecycledStoppable, this.mRecycledPreferredUidOnly));
        }
        determineAssignmentsLocked(this.mRecycledChanged, this.mRecycledIdle, this.mRecycledPreferredUidOnly, this.mRecycledStoppable, this.mRecycledAssignmentInfo);
        if (DEBUG) {
            android.util.Slog.d(TAG, printAssignments("running jobs final", this.mRecycledStoppable, this.mRecycledPreferredUidOnly, this.mRecycledChanged));
            android.util.Slog.d(TAG, "work count results: " + this.mWorkCountTracker);
        }
        carryOutAssignmentChangesLocked(this.mRecycledChanged);
        cleanUpAfterAssignmentChangesLocked(this.mRecycledChanged, this.mRecycledIdle, this.mRecycledPreferredUidOnly, this.mRecycledStoppable, this.mRecycledAssignmentInfo, this.mRecycledPrivilegedState);
        noteConcurrency(true);
    }

    void prepareForAssignmentDeterminationLocked(android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> idle, java.util.List<com.android.server.job.JobConcurrencyManager.ContextAssignment> preferredUidOnly, java.util.List<com.android.server.job.JobConcurrencyManager.ContextAssignment> stoppable, com.android.server.job.JobConcurrencyManager.AssignmentInfo info) {
        com.android.server.job.JobServiceContext jsc;
        com.android.server.job.PendingJobQueue pendingJobQueue;
        int numRunningJobs;
        int i;
        com.android.server.job.PendingJobQueue pendingJobQueue2 = this.mService.getPendingJobQueue();
        java.util.List<com.android.server.job.JobServiceContext> activeServices = this.mActiveServices;
        updateCounterConfigLocked();
        this.mWorkCountTracker.resetCounts();
        updateNonRunningPrioritiesLocked(pendingJobQueue2, true);
        int numRunningJobs2 = activeServices.size();
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        long minPreferredUidOnlyWaitingTimeMs = Long.MAX_VALUE;
        int i2 = 0;
        while (i2 < numRunningJobs2) {
            com.android.server.job.JobServiceContext jsc2 = activeServices.get(i2);
            com.android.server.job.controllers.JobStatus js = jsc2.getRunningJobLocked();
            com.android.server.job.JobConcurrencyManager.ContextAssignment assignment = (com.android.server.job.JobConcurrencyManager.ContextAssignment) this.mContextAssignmentPool.acquire();
            if (assignment == null) {
                assignment = new com.android.server.job.JobConcurrencyManager.ContextAssignment();
            }
            assignment.context = jsc2;
            if (js == null) {
                pendingJobQueue = pendingJobQueue2;
            } else {
                pendingJobQueue = pendingJobQueue2;
                this.mWorkCountTracker.incrementRunningJobCount(jsc2.getRunningJobWorkType());
                assignment.workType = jsc2.getRunningJobWorkType();
                if (js.startedWithImmediacyPrivilege) {
                    i = 1;
                    info.numRunningImmediacyPrivileged++;
                } else {
                    i = 1;
                }
                if (js.shouldTreatAsUserInitiatedJob()) {
                    info.numRunningUi += i;
                } else if (js.startedAsExpeditedJob) {
                    info.numRunningEj += i;
                } else {
                    info.numRunningReg += i;
                }
            }
            assignment.preferredUid = jsc2.getPreferredUid();
            java.lang.String strShouldStopRunningJobLocked = shouldStopRunningJobLocked(jsc2);
            assignment.shouldStopJobReason = strShouldStopRunningJobLocked;
            if (strShouldStopRunningJobLocked != null) {
                stoppable.add(assignment);
                numRunningJobs = numRunningJobs2;
            } else {
                numRunningJobs = numRunningJobs2;
                assignment.timeUntilStoppableMs = jsc2.getRemainingGuaranteedTimeMs(nowElapsed);
                long minPreferredUidOnlyWaitingTimeMs2 = java.lang.Math.min(minPreferredUidOnlyWaitingTimeMs, assignment.timeUntilStoppableMs);
                preferredUidOnly.add(assignment);
                minPreferredUidOnlyWaitingTimeMs = minPreferredUidOnlyWaitingTimeMs2;
            }
            i2++;
            numRunningJobs2 = numRunningJobs;
            pendingJobQueue2 = pendingJobQueue;
        }
        preferredUidOnly.sort(sDeterminationComparator);
        stoppable.sort(sDeterminationComparator);
        for (int i3 = numRunningJobs2; i3 < this.mSteadyStateConcurrencyLimit; i3++) {
            int numIdleContexts = this.mIdleContexts.size();
            if (numIdleContexts <= 0) {
                android.util.Slog.w(TAG, "Had fewer than " + this.mSteadyStateConcurrencyLimit + " in existence");
                jsc = createNewJobServiceContext();
            } else {
                jsc = this.mIdleContexts.removeAt(numIdleContexts - 1);
            }
            com.android.server.job.JobConcurrencyManager.ContextAssignment assignment2 = (com.android.server.job.JobConcurrencyManager.ContextAssignment) this.mContextAssignmentPool.acquire();
            if (assignment2 == null) {
                assignment2 = new com.android.server.job.JobConcurrencyManager.ContextAssignment();
            }
            assignment2.context = jsc;
            idle.add(assignment2);
        }
        this.mWorkCountTracker.onCountDone();
        info.minPreferredUidOnlyWaitingTimeMs = minPreferredUidOnlyWaitingTimeMs == Long.MAX_VALUE ? 0L : minPreferredUidOnlyWaitingTimeMs;
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x03cf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void determineAssignmentsLocked(android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> r35, android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> r36, java.util.List<com.android.server.job.JobConcurrencyManager.ContextAssignment> r37, java.util.List<com.android.server.job.JobConcurrencyManager.ContextAssignment> r38, com.android.server.job.JobConcurrencyManager.AssignmentInfo r39) {
        /*
            Method dump skipped, instruction units count: 1011
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobConcurrencyManager.determineAssignmentsLocked(android.util.ArraySet, android.util.ArraySet, java.util.List, java.util.List, com.android.server.job.JobConcurrencyManager$AssignmentInfo):void");
    }

    private void carryOutAssignmentChangesLocked(android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> changed) {
        for (int c = changed.size() - 1; c >= 0; c--) {
            com.android.server.job.JobConcurrencyManager.ContextAssignment assignment = changed.valueAt(c);
            com.android.server.job.controllers.JobStatus js = assignment.context.getRunningJobLocked();
            if (js != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "preempting job: " + js);
                }
                assignment.context.cancelExecutingJobLocked(assignment.preemptReasonCode, 2, assignment.preemptReason);
            } else {
                com.android.server.job.controllers.JobStatus pendingJob = assignment.newJob;
                if (DEBUG) {
                    android.util.Slog.d(TAG, "About to run job on context " + assignment.context.getId() + ", job: " + pendingJob);
                }
                startJobLocked(assignment.context, pendingJob, assignment.newWorkType);
            }
            assignment.clear();
            this.mContextAssignmentPool.release(assignment);
        }
    }

    private void cleanUpAfterAssignmentChangesLocked(android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> changed, android.util.ArraySet<com.android.server.job.JobConcurrencyManager.ContextAssignment> idle, java.util.List<com.android.server.job.JobConcurrencyManager.ContextAssignment> preferredUidOnly, java.util.List<com.android.server.job.JobConcurrencyManager.ContextAssignment> stoppable, com.android.server.job.JobConcurrencyManager.AssignmentInfo assignmentInfo, android.util.SparseIntArray privilegedState) {
        for (int s = stoppable.size() - 1; s >= 0; s--) {
            com.android.server.job.JobConcurrencyManager.ContextAssignment assignment = stoppable.get(s);
            assignment.clear();
            this.mContextAssignmentPool.release(assignment);
        }
        int s2 = preferredUidOnly.size();
        for (int p = s2 - 1; p >= 0; p--) {
            com.android.server.job.JobConcurrencyManager.ContextAssignment assignment2 = preferredUidOnly.get(p);
            assignment2.clear();
            this.mContextAssignmentPool.release(assignment2);
        }
        int p2 = idle.size();
        for (int i = p2 - 1; i >= 0; i--) {
            com.android.server.job.JobConcurrencyManager.ContextAssignment assignment3 = idle.valueAt(i);
            this.mIdleContexts.add(assignment3.context);
            assignment3.clear();
            this.mContextAssignmentPool.release(assignment3);
        }
        changed.clear();
        idle.clear();
        stoppable.clear();
        preferredUidOnly.clear();
        assignmentInfo.clear();
        privilegedState.clear();
        this.mWorkCountTracker.resetStagingCount();
        this.mActivePkgStats.forEach(this.mPackageStatsStagingCountClearer);
    }

    boolean hasImmediacyPrivilegeLocked(com.android.server.job.controllers.JobStatus job, android.util.SparseIntArray cachedPrivilegedState) {
        if (!job.shouldTreatAsExpeditedJob() && !job.shouldTreatAsUserInitiatedJob()) {
            return false;
        }
        if (job.lastEvaluatedBias == 40) {
            return true;
        }
        int uid = job.getSourceUid();
        int privilegedState = cachedPrivilegedState.get(uid, 0);
        switch (privilegedState) {
            case 1:
                return false;
            case 2:
                return job.shouldTreatAsUserInitiatedJob();
            case 3:
                return true;
            default:
                int procState = this.mActivityManagerInternal.getUidProcessState(uid);
                if (procState == 2) {
                    cachedPrivilegedState.put(uid, 3);
                    return true;
                }
                if (job.shouldTreatAsExpeditedJob()) {
                    return false;
                }
                android.app.BackgroundStartPrivileges bsp = this.mActivityManagerInternal.getBackgroundStartPrivileges(uid);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Job " + job.toShortString() + " bsp state: " + bsp);
                }
                boolean balAllowed = bsp.allowsBackgroundActivityStarts();
                cachedPrivilegedState.put(uid, balAllowed ? 2 : 1);
                return balAllowed;
        }
    }

    void onUidBiasChangedLocked(int prevBias, int newBias) {
        if ((prevBias != 40 && newBias != 40) || this.mService.getPendingJobQueue().size() == 0) {
            return;
        }
        assignJobsToContextsLocked();
    }

    com.android.server.job.JobServiceContext getRunningJobServiceContextLocked(com.android.server.job.controllers.JobStatus job) {
        if (!this.mRunningJobs.contains(job)) {
            return null;
        }
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus executing = jsc.getRunningJobLocked();
            if (executing == job) {
                return jsc;
            }
        }
        android.util.Slog.wtf(TAG, "Couldn't find running job on a context");
        this.mRunningJobs.remove(job);
        return null;
    }

    boolean stopJobOnServiceContextLocked(com.android.server.job.controllers.JobStatus job, int reason, int internalReasonCode, java.lang.String debugReason) {
        if (!this.mRunningJobs.contains(job)) {
            return false;
        }
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus executing = jsc.getRunningJobLocked();
            if (executing == job) {
                jsc.cancelExecutingJobLocked(reason, internalReasonCode, debugReason);
                return true;
            }
        }
        android.util.Slog.wtf(TAG, "Couldn't find running job on a context");
        this.mRunningJobs.remove(job);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopUnexemptedJobsForDoze() {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus executing = jsc.getRunningJobLocked();
            if (executing != null && !executing.canRunInDoze()) {
                jsc.cancelExecutingJobLocked(4, 4, "cancelled due to doze");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopOvertimeJobsLocked(java.lang.String debugReason) {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus jobStatus = jsc.getRunningJobLocked();
            if (jobStatus != null && !jsc.isWithinExecutionGuaranteeTime()) {
                jsc.cancelExecutingJobLocked(4, 3, debugReason);
            }
        }
    }

    void maybeStopOvertimeJobsLocked(com.android.server.job.restrictions.JobRestriction restriction) {
        for (int i = this.mActiveServices.size() - 1; i >= 0; i--) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus jobStatus = jsc.getRunningJobLocked();
            if (jobStatus != null && !jsc.isWithinExecutionGuaranteeTime() && restriction.isJobRestricted(jobStatus, this.mService.evaluateJobBiasLocked(jobStatus))) {
                jsc.cancelExecutingJobLocked(restriction.getStopReason(), restriction.getInternalReason(), android.app.job.JobParameters.getInternalReasonCodeDescription(restriction.getInternalReason()));
            }
        }
    }

    void markJobsForUserStopLocked(int userId, java.lang.String packageName, java.lang.String debugReason) {
        for (int i = this.mActiveServices.size() - 1; i >= 0; i--) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus jobStatus = jsc.getRunningJobLocked();
            if (jobStatus != null && userId == jobStatus.getUserId() && jobStatus.getServiceComponent().getPackageName().equals(packageName)) {
                jsc.markForProcessDeathLocked(13, 11, debugReason);
            }
        }
    }

    void stopNonReadyActiveJobsLocked() {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext serviceContext = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus running = serviceContext.getRunningJobLocked();
            if (running != null) {
                if (!running.isReady()) {
                    if (running.getEffectiveStandbyBucket() == 5 && running.getStopReason() == 12) {
                        serviceContext.cancelExecutingJobLocked(running.getStopReason(), 6, "cancelled due to restricted bucket");
                    } else {
                        serviceContext.cancelExecutingJobLocked(running.getStopReason(), 1, "cancelled due to unsatisfied constraints");
                    }
                } else {
                    com.android.server.job.restrictions.JobRestriction restriction = this.mService.checkIfRestricted(running);
                    if (restriction != null) {
                        int internalReasonCode = restriction.getInternalReason();
                        serviceContext.cancelExecutingJobLocked(restriction.getStopReason(), internalReasonCode, "restricted due to " + android.app.job.JobParameters.getInternalReasonCodeDescription(internalReasonCode));
                    }
                }
            }
        }
    }

    private void noteConcurrency(boolean logForHistogram) {
        this.mService.mJobPackageTracker.noteConcurrency(this.mRunningJobs.size(), this.mWorkCountTracker.getRunningJobCount(1));
        if (logForHistogram) {
            sConcurrencyHistogramLogger.logSample(this.mActiveServices.size());
        }
    }

    private void updateNonRunningPrioritiesLocked(com.android.server.job.PendingJobQueue jobQueue, boolean updateCounter) {
        jobQueue.resetIterator();
        while (true) {
            com.android.server.job.controllers.JobStatus pending = jobQueue.next();
            if (pending != null) {
                if (!this.mRunningJobs.contains(pending)) {
                    pending.lastEvaluatedBias = this.mService.evaluateJobBiasLocked(pending);
                    if (updateCounter) {
                        this.mWorkCountTracker.incrementPendingJobCount(getJobWorkTypes(pending));
                    }
                }
            } else {
                return;
            }
        }
    }

    private com.android.server.job.JobConcurrencyManager.PackageStats getPkgStatsLocked(int userId, java.lang.String packageName) {
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats = (com.android.server.job.JobConcurrencyManager.PackageStats) this.mActivePkgStats.get(userId, packageName);
        if (packageStats == null) {
            packageStats = (com.android.server.job.JobConcurrencyManager.PackageStats) this.mPkgStatsPool.acquire();
            if (packageStats == null) {
                packageStats = new com.android.server.job.JobConcurrencyManager.PackageStats();
            }
            packageStats.setPackage(userId, packageName);
        }
        return packageStats;
    }

    boolean isPkgConcurrencyLimitedLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats;
        if (jobStatus.lastEvaluatedBias < 40 && this.mService.getPendingJobQueue().size() + this.mRunningJobs.size() >= this.mWorkTypeConfig.getMaxTotal() && (packageStats = (com.android.server.job.JobConcurrencyManager.PackageStats) this.mActivePkgStats.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName())) != null) {
            return jobStatus.shouldTreatAsExpeditedJob() ? packageStats.numRunningEj + packageStats.numStagedEj >= this.mPkgConcurrencyLimitEj : packageStats.numRunningRegular + packageStats.numStagedRegular >= this.mPkgConcurrencyLimitRegular;
        }
        return false;
    }

    private void startJobLocked(com.android.server.job.JobServiceContext worker, com.android.server.job.controllers.JobStatus jobStatus, int workType) {
        java.util.List<com.android.server.job.controllers.StateController> controllers = this.mService.mControllers;
        int numControllers = controllers.size();
        android.os.PowerManager.WakeLock wl = this.mPowerManager.newWakeLock(1, jobStatus.getWakelockTag());
        wl.setWorkSource(this.mService.deriveWorkSource(jobStatus.getSourceUid(), jobStatus.getSourcePackageName()));
        wl.setReferenceCounted(false);
        wl.acquire();
        for (int ic = 0; ic < numControllers; ic++) {
            try {
                controllers.get(ic).prepareForExecutionLocked(jobStatus);
            } finally {
                wl.release();
            }
        }
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats = getPkgStatsLocked(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        packageStats.adjustStagedCount(false, jobStatus.shouldTreatAsExpeditedJob());
        if (!worker.executeRunnableJob(jobStatus, workType)) {
            android.util.Slog.e(TAG, "Error executing " + jobStatus);
            this.mJobConcurrencyManagerExt.hookStartJobErrorExecute(jobStatus, workType);
            this.mWorkCountTracker.onStagedJobFailed(workType);
            for (int ic2 = 0; ic2 < numControllers; ic2++) {
                controllers.get(ic2).unprepareFromExecutionLocked(jobStatus);
            }
        } else {
            this.mRunningJobs.add(jobStatus);
            this.mActiveServices.add(worker);
            this.mIdleContexts.remove(worker);
            this.mWorkCountTracker.onJobStarted(workType);
            packageStats.adjustRunningCount(true, jobStatus.shouldTreatAsExpeditedJob());
            this.mActivePkgStats.add(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName(), packageStats);
            this.mService.resetPendingJobReasonCache(jobStatus);
        }
        if (this.mService.getPendingJobQueue().remove(jobStatus)) {
            this.mService.mJobPackageTracker.noteNonpending(jobStatus);
        }
    }

    void onJobCompletedLocked(com.android.server.job.JobServiceContext worker, com.android.server.job.controllers.JobStatus jobStatus, int workType) {
        java.lang.String str;
        int allWorkTypes;
        int workAsType;
        java.lang.String str2;
        int highBiasWorkType;
        boolean wouldBeWaitingTooLong;
        this.mWorkCountTracker.onJobFinished(workType);
        this.mRunningJobs.remove(jobStatus);
        this.mActiveServices.remove(worker);
        boolean respectConcurrencyLimit = true;
        if (this.mIdleContexts.size() >= 96) {
            this.mNumDroppedContexts++;
        } else {
            this.mIdleContexts.add(worker);
        }
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats = (com.android.server.job.JobConcurrencyManager.PackageStats) this.mActivePkgStats.get(jobStatus.getSourceUserId(), jobStatus.getSourcePackageName());
        if (packageStats != null) {
            packageStats.adjustRunningCount(false, jobStatus.startedAsExpeditedJob);
            if (packageStats.numRunningEj <= 0 && packageStats.numRunningRegular <= 0) {
                this.mActivePkgStats.delete(packageStats.userId, packageStats.packageName);
                this.mPkgStatsPool.release(packageStats);
            }
        } else {
            android.util.Slog.wtf(TAG, "Running job didn't have an active PackageStats object");
        }
        com.android.server.job.PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        if (pendingJobQueue.size() != 0) {
            if (this.mActiveServices.size() >= this.mSteadyStateConcurrencyLimit) {
                if (!this.mMaxWaitTimeBypassEnabled) {
                    respectConcurrencyLimit = true;
                } else {
                    long minWaitingTimeMs = Long.MAX_VALUE;
                    long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                    for (int i = this.mActiveServices.size() - 1; i >= 0; i--) {
                        minWaitingTimeMs = java.lang.Math.min(minWaitingTimeMs, this.mActiveServices.get(i).getRemainingGuaranteedTimeMs(nowElapsed));
                    }
                    if (this.mWorkCountTracker.getPendingJobCount(4) > 0) {
                        wouldBeWaitingTooLong = minWaitingTimeMs >= this.mMaxWaitUIMs;
                    } else {
                        wouldBeWaitingTooLong = this.mWorkCountTracker.getPendingJobCount(8) > 0 ? minWaitingTimeMs >= this.mMaxWaitEjMs : minWaitingTimeMs >= this.mMaxWaitRegularMs;
                    }
                    if (wouldBeWaitingTooLong) {
                        respectConcurrencyLimit = false;
                    }
                }
                if (respectConcurrencyLimit) {
                    worker.clearPreferredUid();
                    noteConcurrency(false);
                    return;
                }
            }
            java.lang.String str3 = "Already running similar job to: ";
            if (worker.getPreferredUid() != -1) {
                updateCounterConfigLocked();
                updateNonRunningPrioritiesLocked(pendingJobQueue, false);
                com.android.server.job.controllers.JobStatus highestBiasJob = null;
                int highBiasWorkType2 = workType;
                int highBiasAllWorkTypes = workType;
                com.android.server.job.controllers.JobStatus backupJob = null;
                pendingJobQueue.resetIterator();
                int backupWorkType = 0;
                int backupAllWorkTypes = 0;
                while (true) {
                    com.android.server.job.controllers.JobStatus nextPending = pendingJobQueue.next();
                    if (nextPending == null) {
                        break;
                    }
                    int backupAllWorkTypes2 = backupAllWorkTypes;
                    if (this.mRunningJobs.contains(nextPending)) {
                        android.util.Slog.wtf(TAG, "Pending queue contained a running job");
                        if (DEBUG) {
                            android.util.Slog.e(TAG, "Pending+running job: " + nextPending);
                        }
                        pendingJobQueue.remove(nextPending);
                        str2 = str3;
                    } else if (com.android.server.job.Flags.countQuotaFix() && !nextPending.isReady()) {
                        if (DEBUG) {
                            android.util.Slog.w(TAG, "Pending+not ready job: " + nextPending);
                        }
                        pendingJobQueue.remove(nextPending);
                        str2 = str3;
                    } else {
                        if (DEBUG && isSimilarJobRunningLocked(nextPending)) {
                            android.util.Slog.w(TAG, str3 + nextPending);
                        }
                        str2 = str3;
                        if (worker.getPreferredUid() != nextPending.getUid()) {
                            if (backupJob == null && !isPkgConcurrencyLimitedLocked(nextPending)) {
                                int allWorkTypes2 = getJobWorkTypes(nextPending);
                                int workAsType2 = this.mWorkCountTracker.canJobStart(allWorkTypes2);
                                if (workAsType2 != 0) {
                                    backupJob = nextPending;
                                    backupWorkType = workAsType2;
                                    backupAllWorkTypes2 = allWorkTypes2;
                                }
                                backupAllWorkTypes = backupAllWorkTypes2;
                                str3 = str2;
                            }
                        } else if ((nextPending.lastEvaluatedBias > jobStatus.lastEvaluatedBias || !isPkgConcurrencyLimitedLocked(nextPending)) && (highestBiasJob == null || highestBiasJob.lastEvaluatedBias < nextPending.lastEvaluatedBias)) {
                            highestBiasJob = nextPending;
                            highBiasAllWorkTypes = getJobWorkTypes(nextPending);
                            int workAsType3 = this.mWorkCountTracker.canJobStart(highBiasAllWorkTypes);
                            if (workAsType3 == 0) {
                                highBiasWorkType = workType;
                            } else {
                                highBiasWorkType = workAsType3;
                            }
                            highBiasWorkType2 = highBiasWorkType;
                            backupAllWorkTypes = backupAllWorkTypes2;
                            str3 = str2;
                        }
                    }
                    backupAllWorkTypes = backupAllWorkTypes2;
                    str3 = str2;
                }
                int backupAllWorkTypes3 = backupAllWorkTypes;
                if (highestBiasJob != null) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Running job " + highestBiasJob + " as preemption");
                    }
                    this.mWorkCountTracker.stageJob(highBiasWorkType2, highBiasAllWorkTypes);
                    startJobLocked(worker, highestBiasJob, highBiasWorkType2);
                } else {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Couldn't find preemption job for uid " + worker.getPreferredUid());
                    }
                    worker.clearPreferredUid();
                    if (backupJob != null) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Running job " + backupJob + " instead");
                        }
                        this.mWorkCountTracker.stageJob(backupWorkType, backupAllWorkTypes3);
                        startJobLocked(worker, backupJob, backupWorkType);
                    }
                }
            } else {
                java.lang.String str4 = "Already running similar job to: ";
                if (pendingJobQueue.size() > 0) {
                    updateCounterConfigLocked();
                    updateNonRunningPrioritiesLocked(pendingJobQueue, false);
                    com.android.server.job.controllers.JobStatus highestBiasJob2 = null;
                    int highBiasWorkType3 = workType;
                    int highBiasAllWorkTypes2 = workType;
                    pendingJobQueue.resetIterator();
                    while (true) {
                        com.android.server.job.controllers.JobStatus nextPending2 = pendingJobQueue.next();
                        if (nextPending2 == null) {
                            break;
                        }
                        if (this.mRunningJobs.contains(nextPending2)) {
                            android.util.Slog.wtf(TAG, "Pending queue contained a running job");
                            if (DEBUG) {
                                android.util.Slog.e(TAG, "Pending+running job: " + nextPending2);
                            }
                            pendingJobQueue.remove(nextPending2);
                            str = str4;
                        } else if (com.android.server.job.Flags.countQuotaFix() && !nextPending2.isReady()) {
                            if (DEBUG) {
                                android.util.Slog.w(TAG, "Pending+not ready job: " + nextPending2);
                            }
                            pendingJobQueue.remove(nextPending2);
                            str = str4;
                        } else {
                            if (!DEBUG || !isSimilarJobRunningLocked(nextPending2)) {
                                str = str4;
                            } else {
                                str = str4;
                                android.util.Slog.w(TAG, str + nextPending2);
                            }
                            if (!isPkgConcurrencyLimitedLocked(nextPending2) && (workAsType = this.mWorkCountTracker.canJobStart((allWorkTypes = getJobWorkTypes(nextPending2)))) != 0) {
                                if (highestBiasJob2 == null || highestBiasJob2.lastEvaluatedBias < nextPending2.lastEvaluatedBias) {
                                    highBiasAllWorkTypes2 = allWorkTypes;
                                    highBiasWorkType3 = workAsType;
                                    highestBiasJob2 = nextPending2;
                                }
                                str4 = str;
                            }
                        }
                        str4 = str;
                    }
                    if (highestBiasJob2 != null) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "About to run job: " + highestBiasJob2);
                        }
                        this.mWorkCountTracker.stageJob(highBiasWorkType3, highBiasAllWorkTypes2);
                        startJobLocked(worker, highestBiasJob2, highBiasWorkType3);
                    }
                }
            }
            noteConcurrency(false);
            return;
        }
        worker.clearPreferredUid();
        noteConcurrency(false);
    }

    java.lang.String shouldStopRunningJobLocked(com.android.server.job.JobServiceContext context) {
        com.android.server.job.controllers.JobStatus js = context.getRunningJobLocked();
        if (js == null || context.isWithinExecutionGuaranteeTime()) {
            return null;
        }
        if (this.mPowerManager.isPowerSaveMode()) {
            return "battery saver";
        }
        if (this.mPowerManager.isDeviceIdleMode()) {
            return "deep doze";
        }
        com.android.server.job.restrictions.JobRestriction jobRestriction = this.mService.checkIfRestricted(js);
        if (jobRestriction != null) {
            return "restriction:" + android.app.job.JobParameters.getInternalReasonCodeDescription(jobRestriction.getInternalReason());
        }
        updateCounterConfigLocked();
        int workType = context.getRunningJobWorkType();
        if (this.mRunningJobs.size() > this.mWorkTypeConfig.getMaxTotal() || this.mWorkCountTracker.isOverTypeLimit(workType)) {
            return "too many jobs running";
        }
        com.android.server.job.PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        int numPending = pendingJobQueue.size();
        if (numPending == 0) {
            return null;
        }
        if (js.shouldTreatAsExpeditedJob() || js.startedAsExpeditedJob) {
            if (workType == 32 || workType == 64) {
                if (this.mWorkCountTracker.getPendingJobCount(32) > 0) {
                    return "blocking " + workTypeToString(32) + " queue";
                }
                if (this.mWorkCountTracker.getPendingJobCount(8) > 0 && this.mWorkCountTracker.canJobStart(8, workType) != 0) {
                    return "blocking " + workTypeToString(8) + " queue";
                }
            } else {
                if (this.mWorkCountTracker.getPendingJobCount(8) > 0) {
                    return "blocking " + workTypeToString(8) + " queue";
                }
                if (js.startedWithImmediacyPrivilege) {
                    int immediacyPrivilegeCount = 0;
                    for (int r = this.mRunningJobs.size() - 1; r >= 0; r--) {
                        com.android.server.job.controllers.JobStatus j = this.mRunningJobs.valueAt(r);
                        if (j.startedWithImmediacyPrivilege) {
                            immediacyPrivilegeCount++;
                        }
                    }
                    if (immediacyPrivilegeCount > this.mWorkTypeConfig.getMaxTotal() / 2) {
                        return "prevent immediacy privilege dominance";
                    }
                }
            }
            return null;
        }
        if (this.mWorkCountTracker.getPendingJobCount(workType) > 0) {
            return "blocking " + workTypeToString(workType) + " queue";
        }
        int remainingWorkTypes = 127;
        pendingJobQueue.resetIterator();
        do {
            com.android.server.job.controllers.JobStatus pending = pendingJobQueue.next();
            if (pending == null) {
                break;
            }
            int workTypes = getJobWorkTypes(pending);
            if ((workTypes & remainingWorkTypes) > 0 && this.mWorkCountTracker.canJobStart(workTypes, workType) != 0) {
                return "blocking other pending jobs";
            }
            remainingWorkTypes &= ~workTypes;
        } while (remainingWorkTypes != 0);
        return null;
    }

    boolean executeStopCommandLocked(java.io.PrintWriter pw, java.lang.String pkgName, int userId, java.lang.String namespace, boolean matchJobId, int jobId, int stopReason, int internalStopReason) {
        boolean foundSome = false;
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus js = jc.getRunningJobLocked();
            if (jc.stopIfExecutingLocked(pkgName, userId, namespace, matchJobId, jobId, stopReason, internalStopReason)) {
                foundSome = true;
                pw.print("Stopping job: ");
                js.printUniqueId(pw);
                pw.print(" ");
                pw.println(js.getServiceComponent().flattenToShortString());
            }
        }
        return foundSome;
    }

    android.util.Pair<java.lang.Long, java.lang.Long> getEstimatedNetworkBytesLocked(java.lang.String pkgName, int uid, java.lang.String namespace, int jobId) {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus js = jc.getRunningJobLocked();
            if (js != null && js.matches(uid, namespace, jobId) && js.getSourcePackageName().equals(pkgName)) {
                return jc.getEstimatedNetworkBytes();
            }
        }
        return null;
    }

    android.util.Pair<java.lang.Long, java.lang.Long> getTransferredNetworkBytesLocked(java.lang.String pkgName, int uid, java.lang.String namespace, int jobId) {
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus js = jc.getRunningJobLocked();
            if (js != null && js.matches(uid, namespace, jobId) && js.getSourcePackageName().equals(pkgName)) {
                return jc.getTransferredNetworkBytes();
            }
        }
        return null;
    }

    boolean isNotificationAssociatedWithAnyUserInitiatedJobs(int notificationId, int userId, java.lang.String packageName) {
        return this.mNotificationCoordinator.isNotificationAssociatedWithAnyUserInitiatedJobs(notificationId, userId, packageName);
    }

    boolean isNotificationChannelAssociatedWithAnyUserInitiatedJobs(java.lang.String notificationChannel, int userId, java.lang.String packageName) {
        return this.mNotificationCoordinator.isNotificationChannelAssociatedWithAnyUserInitiatedJobs(notificationChannel, userId, packageName);
    }

    private com.android.server.job.JobServiceContext createNewJobServiceContext() {
        return this.mInjector.createJobServiceContext(this.mService, this, this.mNotificationCoordinator, com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats")), this.mService.mJobPackageTracker, com.android.server.AppSchedulingModuleThread.get().getLooper());
    }

    private java.lang.String printPendingQueueLocked() {
        java.lang.StringBuilder s = new java.lang.StringBuilder("Pending queue: ");
        com.android.server.job.PendingJobQueue pendingJobQueue = this.mService.getPendingJobQueue();
        pendingJobQueue.resetIterator();
        while (true) {
            com.android.server.job.controllers.JobStatus js = pendingJobQueue.next();
            if (js != null) {
                s.append("(").append("{").append(js.getNamespace()).append("} ").append(js.getJob().getId()).append(", ").append(js.getUid()).append(") ");
            } else {
                return s.toString();
            }
        }
    }

    private static java.lang.String printAssignments(java.lang.String header, java.util.Collection<com.android.server.job.JobConcurrencyManager.ContextAssignment>... list) {
        java.lang.StringBuilder s = new java.lang.StringBuilder(header + ": ");
        for (int l = 0; l < list.length; l++) {
            java.util.Collection<com.android.server.job.JobConcurrencyManager.ContextAssignment> assignments = list[l];
            int c = 0;
            for (com.android.server.job.JobConcurrencyManager.ContextAssignment assignment : assignments) {
                com.android.server.job.controllers.JobStatus job = assignment.newJob == null ? assignment.context.getRunningJobLocked() : assignment.newJob;
                if (l > 0 || c > 0) {
                    s.append(" ");
                }
                s.append("(").append(assignment.context.getId()).append("=");
                if (job == null) {
                    s.append("nothing");
                } else {
                    if (job.getNamespace() != null) {
                        s.append(job.getNamespace()).append(":");
                    }
                    s.append(job.getJobId()).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(job.getUid());
                }
                s.append(")");
                c++;
            }
        }
        return s.toString();
    }

    void updateConfigLocked() {
        android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("jobscheduler", new java.lang.String[0]);
        this.mSteadyStateConcurrencyLimit = java.lang.Math.max(8, java.lang.Math.min(64, properties.getInt(KEY_CONCURRENCY_LIMIT, DEFAULT_CONCURRENCY_LIMIT)));
        this.mScreenOffAdjustmentDelayMs = properties.getLong(KEY_SCREEN_OFF_ADJUSTMENT_DELAY_MS, 30000L);
        CONFIG_LIMITS_SCREEN_ON.normal.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_ON.moderate.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_ON.low.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_ON.critical.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_OFF.normal.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_OFF.moderate.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_OFF.low.update(properties, this.mSteadyStateConcurrencyLimit);
        CONFIG_LIMITS_SCREEN_OFF.critical.update(properties, this.mSteadyStateConcurrencyLimit);
        this.mPkgConcurrencyLimitEj = java.lang.Math.max(1, java.lang.Math.min(this.mSteadyStateConcurrencyLimit, properties.getInt(KEY_PKG_CONCURRENCY_LIMIT_EJ, 3)));
        this.mPkgConcurrencyLimitRegular = java.lang.Math.max(1, java.lang.Math.min(this.mSteadyStateConcurrencyLimit, properties.getInt(KEY_PKG_CONCURRENCY_LIMIT_REGULAR, DEFAULT_PKG_CONCURRENCY_LIMIT_REGULAR)));
        this.mMaxWaitTimeBypassEnabled = properties.getBoolean(KEY_ENABLE_MAX_WAIT_TIME_BYPASS, true);
        this.mMaxWaitUIMs = java.lang.Math.max(0L, properties.getLong(KEY_MAX_WAIT_UI_MS, 300000L));
        this.mMaxWaitEjMs = java.lang.Math.max(this.mMaxWaitUIMs, properties.getLong(KEY_MAX_WAIT_EJ_MS, 300000L));
        this.mMaxWaitRegularMs = java.lang.Math.max(this.mMaxWaitEjMs, properties.getLong(KEY_MAX_WAIT_REGULAR_MS, 1800000L));
    }

    public void dumpLocked(final android.util.IndentingPrintWriter pw, long now, long nowRealtime) {
        pw.println("Concurrency:");
        pw.increaseIndent();
        try {
            pw.println("Configuration:");
            pw.increaseIndent();
            pw.print(KEY_CONCURRENCY_LIMIT, java.lang.Integer.valueOf(this.mSteadyStateConcurrencyLimit)).println();
            pw.print(KEY_SCREEN_OFF_ADJUSTMENT_DELAY_MS, java.lang.Long.valueOf(this.mScreenOffAdjustmentDelayMs)).println();
            pw.print(KEY_PKG_CONCURRENCY_LIMIT_EJ, java.lang.Integer.valueOf(this.mPkgConcurrencyLimitEj)).println();
            pw.print(KEY_PKG_CONCURRENCY_LIMIT_REGULAR, java.lang.Integer.valueOf(this.mPkgConcurrencyLimitRegular)).println();
            pw.print(KEY_ENABLE_MAX_WAIT_TIME_BYPASS, java.lang.Boolean.valueOf(this.mMaxWaitTimeBypassEnabled)).println();
            pw.print(KEY_MAX_WAIT_UI_MS, java.lang.Long.valueOf(this.mMaxWaitUIMs)).println();
            pw.print(KEY_MAX_WAIT_EJ_MS, java.lang.Long.valueOf(this.mMaxWaitEjMs)).println();
            pw.print(KEY_MAX_WAIT_REGULAR_MS, java.lang.Long.valueOf(this.mMaxWaitRegularMs)).println();
            pw.println();
            CONFIG_LIMITS_SCREEN_ON.normal.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_ON.moderate.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_ON.low.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_ON.critical.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_OFF.normal.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_OFF.moderate.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_OFF.low.dump(pw);
            pw.println();
            CONFIG_LIMITS_SCREEN_OFF.critical.dump(pw);
            pw.println();
            pw.decreaseIndent();
            pw.print("Screen state: current ");
            java.lang.String str = "ON";
            pw.print(this.mCurrentInteractiveState ? "ON" : "OFF");
            pw.print("  effective ");
            if (!this.mEffectiveInteractiveState) {
                str = "OFF";
            }
            pw.print(str);
            pw.println();
            pw.print("Last screen ON: ");
            android.util.TimeUtils.dumpTimeWithDelta(pw, (now - nowRealtime) + this.mLastScreenOnRealtime, now);
            pw.println();
            pw.print("Last screen OFF: ");
            android.util.TimeUtils.dumpTimeWithDelta(pw, (now - nowRealtime) + this.mLastScreenOffRealtime, now);
            pw.println();
            pw.println();
            pw.print("Current work counts: ");
            pw.println(this.mWorkCountTracker);
            pw.println();
            pw.print("mLastMemoryTrimLevel: ");
            pw.println(this.mLastMemoryTrimLevel);
            pw.println();
            pw.println("Active Package stats:");
            pw.increaseIndent();
            this.mActivePkgStats.forEach(new java.util.function.Consumer() { // from class: com.android.server.job.JobConcurrencyManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.job.JobConcurrencyManager.PackageStats) obj).dumpLocked(pw);
                }
            });
            pw.decreaseIndent();
            pw.println();
            pw.print("User Grace Period: ");
            pw.println(this.mGracePeriodObserver.mGracePeriodExpiration);
            pw.println();
            this.mStatLogger.dump(pw);
        } finally {
            pw.decreaseIndent();
        }
    }

    void dumpContextInfoLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate, long nowElapsed, long nowUptime) {
        pw.println("Active jobs:");
        pw.increaseIndent();
        if (this.mActiveServices.size() == 0) {
            pw.println("N/A");
        }
        for (int i = 0; i < this.mActiveServices.size(); i++) {
            com.android.server.job.JobServiceContext jsc = this.mActiveServices.get(i);
            com.android.server.job.controllers.JobStatus job = jsc.getRunningJobLocked();
            if (job == null || predicate.test(job)) {
                pw.print("Slot #");
                pw.print(i);
                pw.print("(ID=");
                pw.print(jsc.getId());
                pw.print("): ");
                jsc.dumpLocked(pw, nowElapsed);
                if (job != null) {
                    pw.increaseIndent();
                    pw.increaseIndent();
                    job.dump(pw, false, nowElapsed);
                    pw.decreaseIndent();
                    pw.print("Evaluated bias: ");
                    pw.println(android.app.job.JobInfo.getBiasString(job.lastEvaluatedBias));
                    pw.print("Active at ");
                    android.util.TimeUtils.formatDuration(job.madeActive - nowUptime, pw);
                    pw.print(", pending for ");
                    android.util.TimeUtils.formatDuration(job.madeActive - job.madePending, pw);
                    pw.decreaseIndent();
                    pw.println();
                }
            }
        }
        pw.decreaseIndent();
        pw.println();
        pw.print("Idle contexts (");
        pw.print(this.mIdleContexts.size());
        pw.println("):");
        pw.increaseIndent();
        for (int i2 = 0; i2 < this.mIdleContexts.size(); i2++) {
            com.android.server.job.JobServiceContext jsc2 = this.mIdleContexts.valueAt(i2);
            pw.print("ID=");
            pw.print(jsc2.getId());
            pw.print(": ");
            jsc2.dumpLocked(pw, nowElapsed);
        }
        pw.decreaseIndent();
        if (this.mNumDroppedContexts > 0) {
            pw.println();
            pw.print("Dropped ");
            pw.print(this.mNumDroppedContexts);
            pw.println(" contexts");
        }
    }

    public void dumpProtoLocked(android.util.proto.ProtoOutputStream proto, long tag, long now, long nowRealtime) {
        long token = proto.start(tag);
        proto.write(1133871366145L, this.mCurrentInteractiveState);
        proto.write(1133871366146L, this.mEffectiveInteractiveState);
        proto.write(1112396529667L, nowRealtime - this.mLastScreenOnRealtime);
        proto.write(1112396529668L, nowRealtime - this.mLastScreenOffRealtime);
        proto.write(1120986464262L, this.mLastMemoryTrimLevel);
        this.mStatLogger.dumpProto(proto, 1146756268039L);
        proto.end(token);
    }

    boolean shouldRunAsFgUserJob(com.android.server.job.controllers.JobStatus job) {
        if (!this.mShouldRestrictBgUser) {
            return true;
        }
        int userId = job.getSourceUserId();
        android.content.pm.UserInfo userInfo = this.mUserManagerInternal.getUserInfo(userId);
        if (userInfo.profileGroupId != -10000 && userInfo.profileGroupId != userId) {
            userId = userInfo.profileGroupId;
            userInfo = this.mUserManagerInternal.getUserInfo(userId);
        }
        int currentUser = this.mActivityManagerInternal.getCurrentUserId();
        return currentUser == userId || userInfo.isPrimary() || this.mGracePeriodObserver.isWithinGracePeriodForUser(userId);
    }

    int getJobWorkTypes(com.android.server.job.controllers.JobStatus js) {
        int classification;
        int classification2 = 0;
        if (shouldRunAsFgUserJob(js)) {
            if (js.lastEvaluatedBias >= 40) {
                classification = 0 | 1;
            } else if (js.lastEvaluatedBias >= 35) {
                classification = 0 | 2;
            } else {
                classification = 0 | 16;
            }
            if (js.shouldTreatAsExpeditedJob()) {
                return classification | 8;
            }
            if (js.shouldTreatAsUserInitiatedJob()) {
                return classification | 4;
            }
            return classification;
        }
        if (js.lastEvaluatedBias >= 35 || js.shouldTreatAsExpeditedJob() || js.shouldTreatAsUserInitiatedJob()) {
            classification2 = 0 | 32;
        }
        return classification2 | 64;
    }

    static class WorkTypeConfig {
        private static final java.lang.String KEY_PREFIX_MAX = "concurrency_max_";
        static final java.lang.String KEY_PREFIX_MAX_RATIO = "concurrency_max_ratio_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_BG = "concurrency_max_ratio_bg_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_BGUSER = "concurrency_max_ratio_bguser_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_BGUSER_IMPORTANT = "concurrency_max_ratio_bguser_important_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_EJ = "concurrency_max_ratio_ej_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_FGS = "concurrency_max_ratio_fgs_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_TOP = "concurrency_max_ratio_top_";
        private static final java.lang.String KEY_PREFIX_MAX_RATIO_UI = "concurrency_max_ratio_ui_";
        static final java.lang.String KEY_PREFIX_MAX_TOTAL = "concurrency_max_total_";
        private static final java.lang.String KEY_PREFIX_MIN = "concurrency_min_";
        static final java.lang.String KEY_PREFIX_MIN_RATIO = "concurrency_min_ratio_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_BG = "concurrency_min_ratio_bg_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_BGUSER = "concurrency_min_ratio_bguser_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_BGUSER_IMPORTANT = "concurrency_min_ratio_bguser_important_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_EJ = "concurrency_min_ratio_ej_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_FGS = "concurrency_min_ratio_fgs_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_TOP = "concurrency_min_ratio_top_";
        private static final java.lang.String KEY_PREFIX_MIN_RATIO_UI = "concurrency_min_ratio_ui_";
        private final java.lang.String mConfigIdentifier;
        private final int mDefaultMaxTotal;
        private int mMaxTotal;
        private final android.util.SparseIntArray mMinReservedSlots = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mMaxAllowedSlots = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mDefaultMinReservedSlotsRatio = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mDefaultMaxAllowedSlotsRatio = new android.util.SparseIntArray(7);

        WorkTypeConfig(java.lang.String configIdentifier, int steadyStateConcurrencyLimit, int defaultMaxTotal, java.util.List<android.util.Pair<java.lang.Integer, java.lang.Float>> defaultMinRatio, java.util.List<android.util.Pair<java.lang.Integer, java.lang.Float>> defaultMaxRatio) {
            float ratio;
            int wt;
            this.mConfigIdentifier = configIdentifier;
            int iMin = java.lang.Math.min(defaultMaxTotal, steadyStateConcurrencyLimit);
            this.mMaxTotal = iMin;
            this.mDefaultMaxTotal = iMin;
            int numReserved = 0;
            int i = defaultMinRatio.size();
            while (true) {
                i--;
                float f = 0.0f;
                if (i >= 0) {
                    ratio = ((java.lang.Float) defaultMinRatio.get(i).second).floatValue();
                    wt = ((java.lang.Integer) defaultMinRatio.get(i).first).intValue();
                    if (ratio < 0.0f || 1.0f <= ratio) {
                        break;
                    }
                    this.mDefaultMinReservedSlotsRatio.put(wt, java.lang.Float.floatToRawIntBits(ratio));
                    numReserved = (int) (numReserved + (this.mMaxTotal * ratio));
                } else {
                    int i2 = this.mDefaultMaxTotal;
                    if (i2 < 0 || numReserved > this.mDefaultMaxTotal) {
                        throw new java.lang.IllegalArgumentException("Invalid default config: t=" + defaultMaxTotal + " min=" + defaultMinRatio + " max=" + defaultMaxRatio);
                    }
                    int i3 = defaultMaxRatio.size() - 1;
                    while (i3 >= 0) {
                        float ratio2 = ((java.lang.Float) defaultMaxRatio.get(i3).second).floatValue();
                        int wt2 = ((java.lang.Integer) defaultMaxRatio.get(i3).first).intValue();
                        float minRatio = java.lang.Float.intBitsToFloat(this.mDefaultMinReservedSlotsRatio.get(wt2, 0));
                        if (ratio2 < minRatio || ratio2 <= f) {
                            throw new java.lang.IllegalArgumentException("Invalid default config: t=" + defaultMaxTotal + " min=" + defaultMinRatio + " max=" + defaultMaxRatio);
                        }
                        this.mDefaultMaxAllowedSlotsRatio.put(wt2, java.lang.Float.floatToRawIntBits(ratio2));
                        i3--;
                        f = 0.0f;
                    }
                    update(new android.provider.DeviceConfig.Properties.Builder("jobscheduler").build(), steadyStateConcurrencyLimit);
                    return;
                }
            }
            throw new java.lang.IllegalArgumentException("Invalid default min ratio: wt=" + wt + " minRatio=" + ratio);
        }

        void update(android.provider.DeviceConfig.Properties properties, int steadyStateConcurrencyLimit) {
            this.mMaxTotal = java.lang.Math.max(1, java.lang.Math.min(steadyStateConcurrencyLimit, properties.getInt(KEY_PREFIX_MAX_TOTAL + this.mConfigIdentifier, this.mDefaultMaxTotal)));
            int oneIntBits = java.lang.Float.floatToIntBits(1.0f);
            this.mMaxAllowedSlots.clear();
            int maxTop = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_TOP + this.mConfigIdentifier, 1, oneIntBits);
            this.mMaxAllowedSlots.put(1, maxTop);
            int maxFgs = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_FGS + this.mConfigIdentifier, 2, oneIntBits);
            this.mMaxAllowedSlots.put(2, maxFgs);
            int maxUi = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_UI + this.mConfigIdentifier, 4, oneIntBits);
            this.mMaxAllowedSlots.put(4, maxUi);
            int maxEj = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_EJ + this.mConfigIdentifier, 8, oneIntBits);
            this.mMaxAllowedSlots.put(8, maxEj);
            int maxBg = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_BG + this.mConfigIdentifier, 16, oneIntBits);
            this.mMaxAllowedSlots.put(16, maxBg);
            int maxBgUserImp = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_BGUSER_IMPORTANT + this.mConfigIdentifier, 32, oneIntBits);
            this.mMaxAllowedSlots.put(32, maxBgUserImp);
            int maxBgUser = getMaxValue(properties, KEY_PREFIX_MAX_RATIO_BGUSER + this.mConfigIdentifier, 64, oneIntBits);
            this.mMaxAllowedSlots.put(64, maxBgUser);
            int remaining = this.mMaxTotal;
            this.mMinReservedSlots.clear();
            int minTop = getMinValue(properties, KEY_PREFIX_MIN_RATIO_TOP + this.mConfigIdentifier, 1, 1, java.lang.Math.min(maxTop, this.mMaxTotal));
            this.mMinReservedSlots.put(1, minTop);
            int remaining2 = remaining - minTop;
            int minFgs = getMinValue(properties, KEY_PREFIX_MIN_RATIO_FGS + this.mConfigIdentifier, 2, 0, java.lang.Math.min(maxFgs, remaining2));
            this.mMinReservedSlots.put(2, minFgs);
            int remaining3 = remaining2 - minFgs;
            int minUi = getMinValue(properties, KEY_PREFIX_MIN_RATIO_UI + this.mConfigIdentifier, 4, 0, java.lang.Math.min(maxUi, remaining3));
            this.mMinReservedSlots.put(4, minUi);
            int remaining4 = remaining3 - minUi;
            int minEj = getMinValue(properties, KEY_PREFIX_MIN_RATIO_EJ + this.mConfigIdentifier, 8, 0, java.lang.Math.min(maxEj, remaining4));
            this.mMinReservedSlots.put(8, minEj);
            int remaining5 = remaining4 - minEj;
            int minBg = getMinValue(properties, KEY_PREFIX_MIN_RATIO_BG + this.mConfigIdentifier, 16, 0, java.lang.Math.min(maxBg, remaining5));
            this.mMinReservedSlots.put(16, minBg);
            int remaining6 = remaining5 - minBg;
            int minBgUserImp = getMinValue(properties, KEY_PREFIX_MIN_RATIO_BGUSER_IMPORTANT + this.mConfigIdentifier, 32, 0, java.lang.Math.min(maxBgUserImp, remaining6));
            this.mMinReservedSlots.put(32, minBgUserImp);
            int minBgUser = getMinValue(properties, KEY_PREFIX_MIN_RATIO_BGUSER + this.mConfigIdentifier, 64, 0, java.lang.Math.min(maxBgUser, remaining6 - minBgUserImp));
            this.mMinReservedSlots.put(64, minBgUser);
        }

        private int getMaxValue(android.provider.DeviceConfig.Properties properties, java.lang.String key, int workType, int defaultFloatInIntBits) {
            float maxRatio = java.lang.Math.min(1.0f, properties.getFloat(key, java.lang.Float.intBitsToFloat(this.mDefaultMaxAllowedSlotsRatio.get(workType, defaultFloatInIntBits))));
            return java.lang.Math.max(1, (int) (this.mMaxTotal * maxRatio));
        }

        private int getMinValue(android.provider.DeviceConfig.Properties properties, java.lang.String key, int workType, int lowerLimit, int upperLimit) {
            float minRatio = java.lang.Math.min(1.0f, properties.getFloat(key, java.lang.Float.intBitsToFloat(this.mDefaultMinReservedSlotsRatio.get(workType))));
            return java.lang.Math.max(lowerLimit, java.lang.Math.min(upperLimit, (int) (this.mMaxTotal * minRatio)));
        }

        int getMaxTotal() {
            return this.mMaxTotal;
        }

        int getMax(int workType) {
            return this.mMaxAllowedSlots.get(workType, this.mMaxTotal);
        }

        int getMinReserved(int workType) {
            return this.mMinReservedSlots.get(workType);
        }

        void dump(android.util.IndentingPrintWriter pw) {
            pw.print(KEY_PREFIX_MAX_TOTAL + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxTotal)).println();
            pw.print(KEY_PREFIX_MIN_RATIO_TOP + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(1))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_TOP + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(1))).println();
            pw.print(KEY_PREFIX_MIN_RATIO_FGS + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(2))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_FGS + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(2))).println();
            pw.print(KEY_PREFIX_MIN_RATIO_UI + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(4))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_UI + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(4))).println();
            pw.print(KEY_PREFIX_MIN_RATIO_EJ + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(8))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_EJ + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(8))).println();
            pw.print(KEY_PREFIX_MIN_RATIO_BG + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(16))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_BG + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(16))).println();
            pw.print(KEY_PREFIX_MIN_RATIO_BGUSER + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(32))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_BGUSER + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(32))).println();
            pw.print(KEY_PREFIX_MIN_RATIO_BGUSER + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMinReservedSlots.get(64))).println();
            pw.print(KEY_PREFIX_MAX_RATIO_BGUSER + this.mConfigIdentifier, java.lang.Integer.valueOf(this.mMaxAllowedSlots.get(64))).println();
        }
    }

    static class WorkConfigLimitsPerMemoryTrimLevel {
        public final com.android.server.job.JobConcurrencyManager.WorkTypeConfig critical;
        public final com.android.server.job.JobConcurrencyManager.WorkTypeConfig low;
        public final com.android.server.job.JobConcurrencyManager.WorkTypeConfig moderate;
        public final com.android.server.job.JobConcurrencyManager.WorkTypeConfig normal;

        WorkConfigLimitsPerMemoryTrimLevel(com.android.server.job.JobConcurrencyManager.WorkTypeConfig normal, com.android.server.job.JobConcurrencyManager.WorkTypeConfig moderate, com.android.server.job.JobConcurrencyManager.WorkTypeConfig low, com.android.server.job.JobConcurrencyManager.WorkTypeConfig critical) {
            this.normal = normal;
            this.moderate = moderate;
            this.low = low;
            this.critical = critical;
        }
    }

    static class GracePeriodObserver extends android.app.UserSwitchObserver {
        int mGracePeriod;
        final android.util.SparseLongArray mGracePeriodExpiration = new android.util.SparseLongArray();
        final java.lang.Object mLock = new java.lang.Object();
        private int mCurrentUserId = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentUserId();
        private final com.android.server.pm.UserManagerInternal mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);

        GracePeriodObserver(android.content.Context context) {
            this.mGracePeriod = java.lang.Math.max(0, context.getResources().getInteger(android.R.integer.config_extraFreeKbytesAdjust));
        }

        public void onUserSwitchComplete(int newUserId) {
            long expiration = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() + ((long) this.mGracePeriod);
            synchronized (this.mLock) {
                if (this.mCurrentUserId != -10000 && this.mUserManagerInternal.exists(this.mCurrentUserId)) {
                    this.mGracePeriodExpiration.append(this.mCurrentUserId, expiration);
                }
                this.mGracePeriodExpiration.delete(newUserId);
                this.mCurrentUserId = newUserId;
            }
        }

        void onUserRemoved(int userId) {
            synchronized (this.mLock) {
                this.mGracePeriodExpiration.delete(userId);
            }
        }

        public boolean isWithinGracePeriodForUser(int userId) {
            boolean z;
            synchronized (this.mLock) {
                z = userId == this.mCurrentUserId || com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis() < this.mGracePeriodExpiration.get(userId, Long.MAX_VALUE);
            }
            return z;
        }
    }

    static class WorkCountTracker {
        private int mConfigMaxTotal;
        private final android.util.SparseIntArray mConfigNumReservedSlots = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mConfigAbsoluteMaxSlots = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mRecycledReserved = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mNumActuallyReservedSlots = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mNumPendingJobs = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mNumRunningJobs = new android.util.SparseIntArray(7);
        private final android.util.SparseIntArray mNumStartingJobs = new android.util.SparseIntArray(7);
        private int mNumUnspecializedRemaining = 0;

        WorkCountTracker() {
        }

        void setConfig(com.android.server.job.JobConcurrencyManager.WorkTypeConfig workTypeConfig) {
            this.mConfigMaxTotal = workTypeConfig.getMaxTotal();
            for (int workType = 1; workType < 127; workType <<= 1) {
                this.mConfigNumReservedSlots.put(workType, workTypeConfig.getMinReserved(workType));
                this.mConfigAbsoluteMaxSlots.put(workType, workTypeConfig.getMax(workType));
            }
            int workType2 = this.mConfigMaxTotal;
            this.mNumUnspecializedRemaining = workType2;
            for (int i = this.mNumRunningJobs.size() - 1; i >= 0; i--) {
                this.mNumUnspecializedRemaining -= java.lang.Math.max(this.mNumRunningJobs.valueAt(i), this.mConfigNumReservedSlots.get(this.mNumRunningJobs.keyAt(i)));
            }
        }

        void resetCounts() {
            this.mNumActuallyReservedSlots.clear();
            this.mNumPendingJobs.clear();
            this.mNumRunningJobs.clear();
            resetStagingCount();
        }

        void resetStagingCount() {
            this.mNumStartingJobs.clear();
        }

        void incrementRunningJobCount(int workType) {
            this.mNumRunningJobs.put(workType, this.mNumRunningJobs.get(workType) + 1);
        }

        void incrementPendingJobCount(int workTypes) {
            adjustPendingJobCount(workTypes, true);
        }

        void decrementPendingJobCount(int workTypes) {
            if (adjustPendingJobCount(workTypes, false) > 1) {
                for (int workType = 1; workType <= workTypes; workType <<= 1) {
                    if ((workType & workTypes) == workType) {
                        maybeAdjustReservations(workType);
                    }
                }
            }
        }

        private int adjustPendingJobCount(int workTypes, boolean add) {
            int adj = add ? 1 : -1;
            int numAdj = 0;
            for (int workType = 1; workType <= workTypes; workType <<= 1) {
                if ((workTypes & workType) == workType) {
                    this.mNumPendingJobs.put(workType, this.mNumPendingJobs.get(workType) + adj);
                    numAdj++;
                }
            }
            return numAdj;
        }

        void stageJob(int workType, int allWorkTypes) {
            int newNumStartingJobs = this.mNumStartingJobs.get(workType) + 1;
            this.mNumStartingJobs.put(workType, newNumStartingJobs);
            decrementPendingJobCount(allWorkTypes);
            if (this.mNumRunningJobs.get(workType) + newNumStartingJobs > this.mNumActuallyReservedSlots.get(workType)) {
                this.mNumUnspecializedRemaining--;
            }
        }

        void onStagedJobFailed(int workType) {
            int oldNumStartingJobs = this.mNumStartingJobs.get(workType);
            if (oldNumStartingJobs == 0) {
                android.util.Slog.e(com.android.server.job.JobConcurrencyManager.TAG, "# staged jobs for " + workType + " went negative.");
            } else {
                this.mNumStartingJobs.put(workType, oldNumStartingJobs - 1);
                maybeAdjustReservations(workType);
            }
        }

        private void maybeAdjustReservations(int workType) {
            int numRemainingForType = java.lang.Math.max(this.mConfigNumReservedSlots.get(workType), this.mNumRunningJobs.get(workType) + this.mNumStartingJobs.get(workType) + this.mNumPendingJobs.get(workType));
            if (numRemainingForType < this.mNumActuallyReservedSlots.get(workType)) {
                this.mNumActuallyReservedSlots.put(workType, numRemainingForType);
                int assignWorkType = 0;
                for (int i = 0; i < this.mNumActuallyReservedSlots.size(); i++) {
                    int wt = this.mNumActuallyReservedSlots.keyAt(i);
                    if (assignWorkType == 0 || wt < assignWorkType) {
                        int total = this.mNumRunningJobs.get(wt) + this.mNumStartingJobs.get(wt) + this.mNumPendingJobs.get(wt);
                        if (this.mNumActuallyReservedSlots.valueAt(i) < this.mConfigAbsoluteMaxSlots.get(wt) && total > this.mNumActuallyReservedSlots.valueAt(i)) {
                            assignWorkType = wt;
                        }
                    }
                }
                if (assignWorkType != 0) {
                    this.mNumActuallyReservedSlots.put(assignWorkType, this.mNumActuallyReservedSlots.get(assignWorkType) + 1);
                } else {
                    this.mNumUnspecializedRemaining++;
                }
            }
        }

        void onJobStarted(int workType) {
            this.mNumRunningJobs.put(workType, this.mNumRunningJobs.get(workType) + 1);
            int oldNumStartingJobs = this.mNumStartingJobs.get(workType);
            if (oldNumStartingJobs == 0) {
                android.util.Slog.e(com.android.server.job.JobConcurrencyManager.TAG, "# stated jobs for " + workType + " went negative.");
            } else {
                this.mNumStartingJobs.put(workType, oldNumStartingJobs - 1);
            }
        }

        void onJobFinished(int workType) {
            int newNumRunningJobs = this.mNumRunningJobs.get(workType) - 1;
            if (newNumRunningJobs < 0) {
                android.util.Slog.e(com.android.server.job.JobConcurrencyManager.TAG, "# running jobs for " + workType + " went negative.");
            } else {
                this.mNumRunningJobs.put(workType, newNumRunningJobs);
                maybeAdjustReservations(workType);
            }
        }

        void onCountDone() {
            this.mNumUnspecializedRemaining = this.mConfigMaxTotal;
            for (int workType = 1; workType < 127; workType <<= 1) {
                int run = this.mNumRunningJobs.get(workType);
                this.mRecycledReserved.put(workType, run);
                this.mNumUnspecializedRemaining -= run;
            }
            for (int workType2 = 1; workType2 < 127; workType2 <<= 1) {
                int num = this.mNumRunningJobs.get(workType2) + this.mNumPendingJobs.get(workType2);
                int res = this.mRecycledReserved.get(workType2);
                int fillUp = java.lang.Math.max(0, java.lang.Math.min(this.mNumUnspecializedRemaining, java.lang.Math.min(num, this.mConfigNumReservedSlots.get(workType2) - res)));
                this.mRecycledReserved.put(workType2, res + fillUp);
                this.mNumUnspecializedRemaining -= fillUp;
            }
            for (int workType3 = 1; workType3 < 127; workType3 <<= 1) {
                int num2 = this.mNumRunningJobs.get(workType3) + this.mNumPendingJobs.get(workType3);
                int res2 = this.mRecycledReserved.get(workType3);
                int unspecializedAssigned = java.lang.Math.max(0, java.lang.Math.min(this.mNumUnspecializedRemaining, java.lang.Math.min(this.mConfigAbsoluteMaxSlots.get(workType3), num2) - res2));
                this.mNumActuallyReservedSlots.put(workType3, res2 + unspecializedAssigned);
                this.mNumUnspecializedRemaining -= unspecializedAssigned;
            }
        }

        int canJobStart(int workTypes) {
            for (int workType = 1; workType <= workTypes; workType <<= 1) {
                if ((workTypes & workType) == workType) {
                    int maxAllowed = java.lang.Math.min(this.mConfigAbsoluteMaxSlots.get(workType), this.mNumActuallyReservedSlots.get(workType) + this.mNumUnspecializedRemaining);
                    if (this.mNumRunningJobs.get(workType) + this.mNumStartingJobs.get(workType) < maxAllowed) {
                        return workType;
                    }
                }
            }
            return 0;
        }

        int canJobStart(int workTypes, int replacingWorkType) {
            boolean changedNums;
            int oldNumRunning = this.mNumRunningJobs.get(replacingWorkType);
            if (replacingWorkType != 0 && oldNumRunning > 0) {
                this.mNumRunningJobs.put(replacingWorkType, oldNumRunning - 1);
                this.mNumUnspecializedRemaining++;
                changedNums = true;
            } else {
                changedNums = false;
            }
            int ret = canJobStart(workTypes);
            if (changedNums) {
                this.mNumRunningJobs.put(replacingWorkType, oldNumRunning);
                this.mNumUnspecializedRemaining--;
            }
            return ret;
        }

        int getPendingJobCount(int workType) {
            return this.mNumPendingJobs.get(workType, 0);
        }

        int getRunningJobCount(int workType) {
            return this.mNumRunningJobs.get(workType, 0);
        }

        boolean isOverTypeLimit(int workType) {
            return getRunningJobCount(workType) > this.mConfigAbsoluteMaxSlots.get(workType);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("Config={");
            sb.append("tot=").append(this.mConfigMaxTotal);
            sb.append(" mins=");
            sb.append(this.mConfigNumReservedSlots);
            sb.append(" maxs=");
            sb.append(this.mConfigAbsoluteMaxSlots);
            sb.append("}");
            sb.append(", act res=").append(this.mNumActuallyReservedSlots);
            sb.append(", Pending=").append(this.mNumPendingJobs);
            sb.append(", Running=").append(this.mNumRunningJobs);
            sb.append(", Staged=").append(this.mNumStartingJobs);
            sb.append(", # unspecialized remaining=").append(this.mNumUnspecializedRemaining);
            return sb.toString();
        }
    }

    static class PackageStats {
        public int numRunningEj;
        public int numRunningRegular;
        public int numStagedEj;
        public int numStagedRegular;
        public java.lang.String packageName;
        public int userId;

        PackageStats() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPackage(int userId, java.lang.String packageName) {
            this.userId = userId;
            this.packageName = packageName;
            this.numRunningRegular = 0;
            this.numRunningEj = 0;
            resetStagedCount();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetStagedCount() {
            this.numStagedRegular = 0;
            this.numStagedEj = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void adjustRunningCount(boolean add, boolean forEj) {
            if (forEj) {
                this.numRunningEj = java.lang.Math.max(0, this.numRunningEj + (add ? 1 : -1));
            } else {
                this.numRunningRegular = java.lang.Math.max(0, this.numRunningRegular + (add ? 1 : -1));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void adjustStagedCount(boolean add, boolean forEj) {
            if (forEj) {
                this.numStagedEj = java.lang.Math.max(0, this.numStagedEj + (add ? 1 : -1));
            } else {
                this.numStagedRegular = java.lang.Math.max(0, this.numStagedRegular + (add ? 1 : -1));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpLocked(android.util.IndentingPrintWriter pw) {
            pw.print("PackageStats{");
            pw.print(this.userId);
            pw.print("-");
            pw.print(this.packageName);
            pw.print("#runEJ", java.lang.Integer.valueOf(this.numRunningEj));
            pw.print("#runReg", java.lang.Integer.valueOf(this.numRunningRegular));
            pw.print("#stagedEJ", java.lang.Integer.valueOf(this.numStagedEj));
            pw.print("#stagedReg", java.lang.Integer.valueOf(this.numStagedRegular));
            pw.println("}");
        }
    }

    static final class ContextAssignment {
        public com.android.server.job.JobServiceContext context;
        public com.android.server.job.controllers.JobStatus newJob;
        public java.lang.String preemptReason;
        public java.lang.String shouldStopJobReason;
        public long timeUntilStoppableMs;
        public int preferredUid = -1;
        public int workType = 0;
        public int preemptReasonCode = 0;
        public int newWorkType = 0;

        ContextAssignment() {
        }

        void clear() {
            this.context = null;
            this.preferredUid = -1;
            this.workType = 0;
            this.preemptReason = null;
            this.preemptReasonCode = 0;
            this.timeUntilStoppableMs = 0L;
            this.shouldStopJobReason = null;
            this.newJob = null;
            this.newWorkType = 0;
        }
    }

    static final class AssignmentInfo {
        public long minPreferredUidOnlyWaitingTimeMs;
        public int numRunningEj;
        public int numRunningImmediacyPrivileged;
        public int numRunningReg;
        public int numRunningUi;

        AssignmentInfo() {
        }

        void clear() {
            this.minPreferredUidOnlyWaitingTimeMs = 0L;
            this.numRunningImmediacyPrivileged = 0;
            this.numRunningUi = 0;
            this.numRunningEj = 0;
            this.numRunningReg = 0;
        }
    }

    void addRunningJobForTesting(com.android.server.job.controllers.JobStatus job) {
        com.android.server.job.JobServiceContext context;
        this.mRunningJobs.add(job);
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats = getPackageStatsForTesting(job.getSourceUserId(), job.getSourcePackageName());
        packageStats.adjustRunningCount(true, job.shouldTreatAsExpeditedJob());
        if (this.mIdleContexts.size() > 0) {
            context = this.mIdleContexts.removeAt(this.mIdleContexts.size() - 1);
        } else {
            context = createNewJobServiceContext();
        }
        context.executeRunnableJob(job, this.mWorkCountTracker.canJobStart(getJobWorkTypes(job)));
        this.mActiveServices.add(context);
    }

    int getPackageConcurrencyLimitEj() {
        return this.mPkgConcurrencyLimitEj;
    }

    int getPackageConcurrencyLimitRegular() {
        return this.mPkgConcurrencyLimitRegular;
    }

    com.android.server.job.JobConcurrencyManager.PackageStats getPackageStatsForTesting(int userId, java.lang.String packageName) {
        com.android.server.job.JobConcurrencyManager.PackageStats packageStats = getPkgStatsLocked(userId, packageName);
        this.mActivePkgStats.add(userId, packageName, packageStats);
        return packageStats;
    }

    static class Injector {
        Injector() {
        }

        com.android.server.job.JobServiceContext createJobServiceContext(com.android.server.job.JobSchedulerService service, com.android.server.job.JobConcurrencyManager concurrencyManager, com.android.server.job.JobNotificationCoordinator notificationCoordinator, com.android.internal.app.IBatteryStats batteryStats, com.android.server.job.JobPackageTracker tracker, android.os.Looper looper) {
            return new com.android.server.job.JobServiceContext(service, concurrencyManager, notificationCoordinator, batteryStats, tracker, looper);
        }
    }
}
