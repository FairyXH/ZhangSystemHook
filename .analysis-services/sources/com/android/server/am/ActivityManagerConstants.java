package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ActivityManagerConstants extends android.database.ContentObserver {
    private static final android.net.Uri ACTIVITY_MANAGER_CONSTANTS_URI;
    private static final android.net.Uri ACTIVITY_STARTS_LOGGING_ENABLED_URI;
    public static int BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = 0;
    public static boolean BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = false;
    public static float BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD = 0.0f;
    public static int BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = 0;
    public static boolean BINDER_HEAVY_HITTER_WATCHER_ENABLED = false;
    public static float BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = 0.0f;
    static final long DEFAULT_BACKGROUND_SETTLE_TIME = 60000;
    private static final long DEFAULT_BG_START_TIMEOUT = 15000;
    private static final int DEFAULT_BOOT_TIME_TEMP_ALLOWLIST_DURATION = 20000;
    private static final int DEFAULT_BOUND_SERVICE_CRASH_MAX_RETRY = 16;
    private static final long DEFAULT_BOUND_SERVICE_CRASH_RESTART_DURATION = 1800000;
    private static final java.lang.String DEFAULT_COMPONENT_ALIAS_OVERRIDES = "";
    private static final long DEFAULT_CONTENT_PROVIDER_RETAIN_TIME = 20000;
    static final long DEFAULT_DATA_SYNC_FGS_TIMEOUT_DURATION = 21600000;
    private static final int DEFAULT_DEFER_BOOT_COMPLETED_BROADCAST = 6;
    private static final boolean DEFAULT_ENABLE_BATCHING_OOM_ADJ;
    private static final boolean DEFAULT_ENABLE_COMPONENT_ALIAS = false;
    private static final boolean DEFAULT_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = true;
    private static final boolean DEFAULT_ENABLE_NEW_OOM_ADJ;
    private static final boolean DEFAULT_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION = false;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_CRITICAL_MEM = 30000;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_LOW_MEM = 20000;
    private static final long[] DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MODERATE_MEM = 10000;
    private static final long DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_NORMAL_MEM = 0;
    private static final long DEFAULT_FGSERVICE_MIN_REPORT_TIME = 3000;
    private static final long DEFAULT_FGSERVICE_MIN_SHOWN_TIME = 2000;
    private static final long DEFAULT_FGSERVICE_SCREEN_ON_AFTER_TIME = 5000;
    private static final long DEFAULT_FGSERVICE_SCREEN_ON_BEFORE_TIME = 1000;
    private static final boolean DEFAULT_FGS_ALLOW_OPT_OUT = false;
    private static final float DEFAULT_FGS_ATOM_SAMPLE_RATE = 1.0f;
    private static final int DEFAULT_FGS_BOOT_COMPLETED_ALLOWLIST = 1073743640;
    static final long DEFAULT_FGS_CRASH_EXTRA_WAIT_DURATION = 10000;
    private static final float DEFAULT_FGS_START_ALLOWED_LOG_SAMPLE_RATE = 0.25f;
    private static final float DEFAULT_FGS_START_DENIED_LOG_SAMPLE_RATE = 1.0f;
    private static final int DEFAULT_FGS_START_FOREGROUND_TIMEOUT_MS = 10000;
    private static final long DEFAULT_FG_TO_BG_FGS_GRACE_DURATION = 5000;
    private static final long DEFAULT_FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION = 1000;
    private static final boolean DEFAULT_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = true;
    private static final long DEFAULT_FULL_PSS_LOWERED_INTERVAL = 300000;
    private static final long DEFAULT_FULL_PSS_MIN_INTERVAL = 1200000;
    private static final long DEFAULT_GC_MIN_INTERVAL = 60000;
    private static final long DEFAULT_GC_TIMEOUT = 5000;
    static final boolean DEFAULT_KILL_BG_RESTRICTED_CACHED_IDLE = false;
    static final long DEFAULT_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME_MS = 60000;
    private static final float DEFAULT_LOW_SWAP_THRESHOLD_PERCENT = 0.1f;
    private static final int DEFAULT_MAX_CACHED_PROCESSES = 1024;
    private static final long DEFAULT_MAX_EMPTY_TIME_MILLIS = 3600000000L;
    private static final int DEFAULT_MAX_PHANTOM_PROCESSES = 32;
    private static final long DEFAULT_MAX_PREVIOUS_TIME = 60000;
    static final int DEFAULT_MAX_SERVICE_CONNECTIONS_PER_PROCESS = 3000;
    private static final long DEFAULT_MAX_SERVICE_INACTIVITY = 1800000;
    static final long DEFAULT_MEDIA_PROCESSING_FGS_TIMEOUT_DURATION = 21600000;
    private static final long DEFAULT_MEMORY_INFO_THROTTLE_TIME = 300000;
    private static final long DEFAULT_MIN_ASSOC_LOG_DURATION = 300000;
    private static final int DEFAULT_MIN_CRASH_INTERVAL = 120000;
    private static final long DEFAULT_NETWORK_ACCESS_TIMEOUT_MS = 200;
    private static final long DEFAULT_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS = 600000;
    private static final boolean DEFAULT_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED = true;
    private static final int DEFAULT_OOMADJ_UPDATE_POLICY = 1;
    private static final int DEFAULT_PENDINGINTENT_WARNING_THRESHOLD = 2000;
    private static final long DEFAULT_POWER_CHECK_INTERVAL;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_1 = 25;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_2 = 25;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_3 = 10;
    private static final int DEFAULT_POWER_CHECK_MAX_CPU_4 = 2;
    private static final boolean DEFAULT_PRIORITIZE_ALARM_BROADCASTS = true;
    private static final boolean DEFAULT_PROACTIVE_KILLS_ENABLED = false;
    private static final int DEFAULT_PROCESS_CRASH_COUNT_LIMIT = 12;
    private static final int DEFAULT_PROCESS_CRASH_COUNT_RESET_INTERVAL = 43200000;
    private static final long DEFAULT_PROCESS_KILL_TIMEOUT_MS = 10000;
    private static final boolean DEFAULT_PROCESS_START_ASYNC = true;
    private static final int DEFAULT_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR = 1;
    private static final long DEFAULT_SERVICE_BACKGROUND_TIMEOUT;
    private static final long DEFAULT_SERVICE_BG_ACTIVITY_START_TIMEOUT = 10000;
    private static final long DEFAULT_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS = 15000;
    private static final long DEFAULT_SERVICE_MIN_RESTART_TIME_BETWEEN = 10000;
    private static final long DEFAULT_SERVICE_RESET_RUN_DURATION = 60000;
    private static final long DEFAULT_SERVICE_RESTART_DURATION = 1000;
    private static final int DEFAULT_SERVICE_RESTART_DURATION_FACTOR = 4;
    private static final long DEFAULT_SERVICE_RESTART_DURATION_LOW = 30000;
    private static final int DEFAULT_SERVICE_START_FOREGROUND_ANR_DELAY_MS = 10000;
    private static final int DEFAULT_SERVICE_START_FOREGROUND_TIMEOUT_MS = 30000;
    private static final long DEFAULT_SERVICE_TIMEOUT;
    private static final long DEFAULT_SERVICE_USAGE_INTERACTION_TIME_POST_S = 60000;
    private static final long DEFAULT_SERVICE_USAGE_INTERACTION_TIME_PRE_S = 1800000;
    static final long DEFAULT_SHORT_FGS_ANR_EXTRA_WAIT_DURATION = 10000;
    static final long DEFAULT_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION = 5000;
    static final long DEFAULT_SHORT_FGS_TIMEOUT_DURATION = 180000;
    private static final boolean DEFAULT_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED = true;
    private static final long DEFAULT_TIERED_CACHED_ADJ_DECAY_TIME = 60000;
    private static final long DEFAULT_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = 15000;
    private static final long DEFAULT_TOP_TO_FGS_GRACE_DURATION = 15000;
    private static final long DEFAULT_USAGE_STATS_INTERACTION_INTERVAL_POST_S = 600000;
    private static final long DEFAULT_USAGE_STATS_INTERACTION_INTERVAL_PRE_S = 7200000;
    private static final boolean DEFAULT_USE_TIERED_CACHED_ADJ = false;
    private static final long DEFAULT_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION = 5000;
    private static final android.net.Uri ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI;
    private static final android.net.Uri FORCE_ENABLE_PSS_PROFILING_URI;
    private static final android.net.Uri FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI;
    static final java.lang.String KEY_BACKGROUND_SETTLE_TIME = "background_settle_time";
    static final java.lang.String KEY_BG_START_TIMEOUT = "service_bg_start_timeout";
    private static final java.lang.String KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = "binder_heavy_hitter_auto_sampler_batchsize";
    private static final java.lang.String KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = "binder_heavy_hitter_auto_sampler_enabled";
    private static final java.lang.String KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD = "binder_heavy_hitter_auto_sampler_threshold";
    private static final java.lang.String KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = "binder_heavy_hitter_watcher_batchsize";
    private static final java.lang.String KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED = "binder_heavy_hitter_watcher_enabled";
    private static final java.lang.String KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = "binder_heavy_hitter_watcher_threshold";
    static final java.lang.String KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION = "boot_time_temp_allowlist_duration";
    static final java.lang.String KEY_BOUND_SERVICE_CRASH_MAX_RETRY = "service_crash_max_retry";
    static final java.lang.String KEY_BOUND_SERVICE_CRASH_RESTART_DURATION = "service_crash_restart_duration";
    static final java.lang.String KEY_COMPONENT_ALIAS_OVERRIDES = "component_alias_overrides";
    private static final java.lang.String KEY_CONTENT_PROVIDER_RETAIN_TIME = "content_provider_retain_time";
    private static final java.lang.String KEY_DATA_SYNC_FGS_TIMEOUT_DURATION = "data_sync_fgs_timeout_duration";
    private static final java.lang.String KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED = "default_background_activity_starts_enabled";
    private static final java.lang.String KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED = "default_background_fgs_starts_restriction_enabled";
    private static final java.lang.String KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK = "default_fgs_starts_restriction_check_caller_target_sdk";
    private static final java.lang.String KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED = "default_fgs_starts_restriction_enabled";
    private static final java.lang.String KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED = "default_fgs_starts_restriction_notification_enabled";
    private static final java.lang.String KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED = "deferred_fgs_notifications_api_gated";
    private static final java.lang.String KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED = "deferred_fgs_notifications_enabled";
    private static final java.lang.String KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME = "deferred_fgs_notification_exclusion_time";
    private static final java.lang.String KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT = "deferred_fgs_notification_exclusion_time_for_short";
    private static final java.lang.String KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL = "deferred_fgs_notification_interval";
    private static final java.lang.String KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT = "deferred_fgs_notification_interval_for_short";
    private static final java.lang.String KEY_DEFER_BOOT_COMPLETED_BROADCAST = "defer_boot_completed_broadcast";
    static final java.lang.String KEY_DISABLE_APP_PROFILER_PSS_PROFILING = "disable_app_profiler_pss_profiling";
    static final java.lang.String KEY_ENABLE_BATCHING_OOM_ADJ = "enable_batching_oom_adj";
    static final java.lang.String KEY_ENABLE_COMPONENT_ALIAS = "enable_experimental_component_alias";
    static final java.lang.String KEY_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = "enable_extra_delay_svc_restart_mem_pressure";
    static final java.lang.String KEY_ENABLE_NEW_OOMADJ = "enable_new_oom_adj";
    private static final java.lang.String KEY_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION = "enable_wait_for_finish_attach_application";
    static final java.lang.String KEY_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = "extra_delay_svc_restart_mem_pressure";
    private static final java.lang.String KEY_FGSERVICE_MIN_REPORT_TIME = "fgservice_min_report_time";
    private static final java.lang.String KEY_FGSERVICE_MIN_SHOWN_TIME = "fgservice_min_shown_time";
    private static final java.lang.String KEY_FGSERVICE_SCREEN_ON_AFTER_TIME = "fgservice_screen_on_after_time";
    private static final java.lang.String KEY_FGSERVICE_SCREEN_ON_BEFORE_TIME = "fgservice_screen_on_before_time";
    static final java.lang.String KEY_FGS_ALLOW_OPT_OUT = "fgs_allow_opt_out";
    static final java.lang.String KEY_FGS_ATOM_SAMPLE_RATE = "fgs_atom_sample_rate";
    private static final java.lang.String KEY_FGS_BOOT_COMPLETED_ALLOWLIST = "fgs_boot_completed_allowlist";
    private static final java.lang.String KEY_FGS_CRASH_EXTRA_WAIT_DURATION = "fgs_crash_extra_wait_duration";
    static final java.lang.String KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE = "fgs_start_allowed_log_sample_rate";
    static final java.lang.String KEY_FGS_START_DENIED_LOG_SAMPLE_RATE = "fgs_start_denied_log_sample_rate";
    static final java.lang.String KEY_FGS_START_FOREGROUND_TIMEOUT = "fgs_start_foreground_timeout";
    static final java.lang.String KEY_FG_TO_BG_FGS_GRACE_DURATION = "fg_to_bg_fgs_grace_duration";
    static final java.lang.String KEY_FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION = "follow_up_oomadj_update_wait_duration";
    private static final java.lang.String KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = "force_bg_check_on_restricted";
    private static final java.lang.String KEY_FULL_PSS_LOWERED_INTERVAL = "full_pss_lowered_interval";
    private static final java.lang.String KEY_FULL_PSS_MIN_INTERVAL = "full_pss_min_interval";
    private static final java.lang.String KEY_GC_MIN_INTERVAL = "gc_min_interval";
    private static final java.lang.String KEY_GC_TIMEOUT = "gc_timeout";
    private static final java.lang.String KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES = "imperceptible_kill_exempt_packages";
    private static final java.lang.String KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES = "imperceptible_kill_exempt_proc_states";
    static final java.lang.String KEY_KILL_BG_RESTRICTED_CACHED_IDLE = "kill_bg_restricted_cached_idle";
    static final java.lang.String KEY_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME = "kill_bg_restricted_cached_idle_settle_time";
    private static final java.lang.String KEY_LOW_SWAP_THRESHOLD_PERCENT = "low_swap_threshold_percent";
    private static final java.lang.String KEY_MAX_CACHED_PROCESSES = "max_cached_processes";
    private static final java.lang.String KEY_MAX_EMPTY_TIME_MILLIS = "max_empty_time_millis";
    private static final java.lang.String KEY_MAX_PHANTOM_PROCESSES = "max_phantom_processes";
    static final java.lang.String KEY_MAX_PREVIOUS_TIME = "max_previous_time";
    private static final java.lang.String KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS = "max_service_connections_per_process";
    static final java.lang.String KEY_MAX_SERVICE_INACTIVITY = "service_max_inactivity";
    private static final java.lang.String KEY_MEDIA_PROCESSING_FGS_TIMEOUT_DURATION = "media_processing_fgs_timeout_duration";
    static final java.lang.String KEY_MEMORY_INFO_THROTTLE_TIME = "memory_info_throttle_time";
    private static final java.lang.String KEY_MIN_ASSOC_LOG_DURATION = "min_assoc_log_duration";
    static final java.lang.String KEY_MIN_CRASH_INTERVAL = "min_crash_interval";
    static final java.lang.String KEY_NETWORK_ACCESS_TIMEOUT_MS = "network_access_timeout_ms";
    private static final java.lang.String KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS = "no_kill_cached_processes_post_boot_completed_duration_millis";
    private static final java.lang.String KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED = "no_kill_cached_processes_until_boot_completed";
    private static final java.lang.String KEY_OOMADJ_UPDATE_POLICY = "oomadj_update_policy";
    static final java.lang.String KEY_PENDINGINTENT_WARNING_THRESHOLD = "pendingintent_warning_threshold";
    private static final java.lang.String KEY_POWER_CHECK_INTERVAL = "power_check_interval";
    private static final java.lang.String KEY_POWER_CHECK_MAX_CPU_1 = "power_check_max_cpu_1";
    private static final java.lang.String KEY_POWER_CHECK_MAX_CPU_2 = "power_check_max_cpu_2";
    private static final java.lang.String KEY_POWER_CHECK_MAX_CPU_3 = "power_check_max_cpu_3";
    private static final java.lang.String KEY_POWER_CHECK_MAX_CPU_4 = "power_check_max_cpu_4";
    private static final java.lang.String KEY_PRIORITIZE_ALARM_BROADCASTS = "prioritize_alarm_broadcasts";
    private static final java.lang.String KEY_PROACTIVE_KILLS_ENABLED = "proactive_kills_enabled";
    static final java.lang.String KEY_PROCESS_CRASH_COUNT_LIMIT = "process_crash_count_limit";
    static final java.lang.String KEY_PROCESS_CRASH_COUNT_RESET_INTERVAL = "process_crash_count_reset_interval";
    private static final java.lang.String KEY_PROCESS_KILL_TIMEOUT = "process_kill_timeout";
    static final java.lang.String KEY_PROCESS_START_ASYNC = "process_start_async";
    private static final java.lang.String KEY_PROC_STATE_DEBUG_UIDS = "proc_state_debug_uids";
    static final java.lang.String KEY_PSS_TO_RSS_THRESHOLD_MODIFIER = "pss_to_rss_threshold_modifier";
    private static final java.lang.String KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR = "push_messaging_over_quota_behavior";
    static final java.lang.String KEY_SERVICE_BG_ACTIVITY_START_TIMEOUT = "service_bg_activity_start_timeout";
    private static final java.lang.String KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS = "service_bind_almost_perceptible_timeout_ms";
    static final java.lang.String KEY_SERVICE_MIN_RESTART_TIME_BETWEEN = "service_min_restart_time_between";
    static final java.lang.String KEY_SERVICE_RESET_RUN_DURATION = "service_reset_run_duration";
    static final java.lang.String KEY_SERVICE_RESTART_DURATION = "service_restart_duration";
    static final java.lang.String KEY_SERVICE_RESTART_DURATION_FACTOR = "service_restart_duration_factor";
    private static final java.lang.String KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS = "service_start_foreground_anr_delay_ms";
    private static final java.lang.String KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS = "service_start_foreground_timeout_ms";
    private static final java.lang.String KEY_SERVICE_USAGE_INTERACTION_TIME_POST_S = "service_usage_interaction_time_post_s";
    private static final java.lang.String KEY_SERVICE_USAGE_INTERACTION_TIME_PRE_S = "service_usage_interaction_time";
    private static final java.lang.String KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION = "short_fgs_anr_extra_wait_duration";
    private static final java.lang.String KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION = "short_fgs_proc_state_extra_wait_duration";
    private static final java.lang.String KEY_SHORT_FGS_TIMEOUT_DURATION = "short_fgs_timeout_duration";
    private static final java.lang.String KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED = "system_exempt_power_restrictions_enabled";
    static final java.lang.String KEY_TIERED_CACHED_ADJ_DECAY_TIME = "tiered_cached_adj_decay_time";
    static final java.lang.String KEY_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = "top_to_almost_perceptible_grace_duration";
    static final java.lang.String KEY_TOP_TO_FGS_GRACE_DURATION = "top_to_fgs_grace_duration";
    private static final java.lang.String KEY_USAGE_STATS_INTERACTION_INTERVAL_POST_S = "usage_stats_interaction_interval_post_s";
    private static final java.lang.String KEY_USAGE_STATS_INTERACTION_INTERVAL_PRE_S = "usage_stats_interaction_interval";
    static final java.lang.String KEY_USE_TIERED_CACHED_ADJ = "use_tiered_cached_adj";
    static final java.lang.String KEY_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION = "vis_to_invis_uij_schedule_grace_duration";
    public static float LOW_SWAP_THRESHOLD_PERCENT = 0.0f;
    public static long MAX_PREVIOUS_TIME = 0;
    public static long MIN_ASSOC_LOG_DURATION = 0;
    private static final long MIN_AUTOMATIC_HEAP_DUMP_PSS_THRESHOLD_BYTES = 102400;
    public static int MIN_CRASH_INTERVAL = 0;
    private static final int OOMADJ_UPDATE_POLICY_QUICK = 1;
    private static final int OOMADJ_UPDATE_POLICY_SLOW = 0;
    public static boolean PROACTIVE_KILLS_ENABLED = false;
    static int PROCESS_CRASH_COUNT_LIMIT = 0;
    static long PROCESS_CRASH_COUNT_RESET_INTERVAL = 0;
    private static final java.lang.String TAG = "ActivityManagerConstants";
    public boolean APP_PROFILER_PSS_PROFILING_DISABLED;
    public long BACKGROUND_SETTLE_TIME;
    public long BG_START_TIMEOUT;
    public long BOUND_SERVICE_CRASH_RESTART_DURATION;
    public long BOUND_SERVICE_MAX_CRASH_RETRY;
    long CONTENT_PROVIDER_RETAIN_TIME;
    public int CUR_MAX_CACHED_PROCESSES;
    public int CUR_MAX_EMPTY_PROCESSES;
    public int CUR_TRIM_CACHED_PROCESSES;
    public int CUR_TRIM_EMPTY_PROCESSES;
    public boolean ENABLE_BATCHING_OOM_ADJ;
    public boolean ENABLE_NEW_OOMADJ;
    public long FGSERVICE_MIN_REPORT_TIME;
    public long FGSERVICE_MIN_SHOWN_TIME;
    public long FGSERVICE_SCREEN_ON_AFTER_TIME;
    public long FGSERVICE_SCREEN_ON_BEFORE_TIME;
    public int FGS_BOOT_COMPLETED_ALLOWLIST;
    public boolean FLAG_PROCESS_START_ASYNC;
    public long FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION;
    boolean FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS;
    long FULL_PSS_LOWERED_INTERVAL;
    long FULL_PSS_MIN_INTERVAL;
    long GC_MIN_INTERVAL;
    long GC_TIMEOUT;
    public android.util.ArraySet<java.lang.String> IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES;
    public android.util.ArraySet<java.lang.Integer> IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES;
    public final android.util.ArraySet<android.content.ComponentName> KEEP_WARMING_SERVICES;
    public int MAX_CACHED_PROCESSES;
    public int MAX_PHANTOM_PROCESSES;
    public long MAX_SERVICE_INACTIVITY;
    public long MEMORY_INFO_THROTTLE_TIME;
    public boolean OOMADJ_UPDATE_QUICK;
    public int PENDINGINTENT_WARNING_THRESHOLD;
    long POWER_CHECK_INTERVAL;
    int POWER_CHECK_MAX_CPU_1;
    int POWER_CHECK_MAX_CPU_2;
    int POWER_CHECK_MAX_CPU_3;
    int POWER_CHECK_MAX_CPU_4;
    public float PSS_TO_RSS_THRESHOLD_MODIFIER;
    long SERVICE_BACKGROUND_TIMEOUT;
    public long SERVICE_BG_ACTIVITY_START_TIMEOUT;
    public long SERVICE_MIN_RESTART_TIME_BETWEEN;
    public long SERVICE_RESET_RUN_DURATION;
    public long SERVICE_RESTART_DURATION;
    public int SERVICE_RESTART_DURATION_FACTOR;
    public long SERVICE_RESTART_DURATION_LOW;
    long SERVICE_TIMEOUT;
    long SERVICE_USAGE_INTERACTION_TIME_POST_S;
    long SERVICE_USAGE_INTERACTION_TIME_PRE_S;
    public long TIERED_CACHED_ADJ_DECAY_TIME;
    public long TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION;
    public volatile long TOP_TO_FGS_GRACE_DURATION;
    long USAGE_STATS_INTERACTION_INTERVAL_POST_S;
    long USAGE_STATS_INTERACTION_INTERVAL_PRE_S;
    public boolean USE_TIERED_CACHED_ADJ;
    volatile long mBootTimeTempAllowlistDuration;
    volatile java.lang.String mComponentAliasOverrides;
    private final int mCustomizedMaxCachedProcesses;
    public volatile long mDataSyncFgsTimeoutDuration;
    private final int mDefaultBinderHeavyHitterAutoSamplerBatchSize;
    private final boolean mDefaultBinderHeavyHitterAutoSamplerEnabled;
    private final float mDefaultBinderHeavyHitterAutoSamplerThreshold;
    private final int mDefaultBinderHeavyHitterWatcherBatchSize;
    private final boolean mDefaultBinderHeavyHitterWatcherEnabled;
    private final float mDefaultBinderHeavyHitterWatcherThreshold;
    private final boolean mDefaultDisableAppProfilerPssProfiling;
    private java.util.List<java.lang.String> mDefaultImperceptibleKillExemptPackages;
    private java.util.List<java.lang.Integer> mDefaultImperceptibleKillExemptProcStates;
    private final float mDefaultPssToRssThresholdModifier;
    volatile int mDeferBootCompletedBroadcast;
    volatile boolean mEnableComponentAlias;
    boolean mEnableExtraServiceRestartDelayOnMemPressure;
    volatile boolean mEnableProcStateStacktrace;
    public volatile boolean mEnableWaitForFinishAttachApplication;
    long[] mExtraServiceRestartDelayOnMemPressure;
    volatile long mFgToBgFgsGraceDuration;
    volatile boolean mFgsAllowOptOut;
    volatile float mFgsAtomSampleRate;
    public volatile long mFgsCrashExtraWaitDuration;
    volatile long mFgsNotificationDeferralExclusionTime;
    volatile long mFgsNotificationDeferralExclusionTimeForShort;
    volatile long mFgsNotificationDeferralInterval;
    volatile long mFgsNotificationDeferralIntervalForShort;
    volatile float mFgsStartAllowedLogSampleRate;
    volatile float mFgsStartDeniedLogSampleRate;
    volatile long mFgsStartForegroundTimeoutMs;
    volatile boolean mFgsStartRestrictionCheckCallerTargetSdk;
    volatile boolean mFgsStartRestrictionNotificationEnabled;
    volatile boolean mFlagActivityStartsLoggingEnabled;
    volatile boolean mFlagBackgroundActivityStartsEnabled;
    volatile boolean mFlagBackgroundFgsStartRestrictionEnabled;
    volatile boolean mFlagFgsNotificationDeferralApiGated;
    volatile boolean mFlagFgsNotificationDeferralEnabled;
    volatile boolean mFlagFgsStartRestrictionEnabled;
    volatile boolean mFlagForegroundServiceStartsLoggingEnabled;
    volatile boolean mFlagSystemExemptPowerRestrictionsEnabled;
    volatile boolean mForceEnablePssProfiling;
    volatile boolean mKillBgRestrictedAndCachedIdle;
    volatile long mKillBgRestrictedAndCachedIdleSettleTimeMs;
    volatile long mMaxEmptyTimeMillis;
    volatile int mMaxServiceConnectionsPerProcess;
    public volatile long mMediaProcessingFgsTimeoutDuration;
    volatile long mNetworkAccessTimeoutMs;
    volatile long mNoKillCachedProcessesPostBootCompletedDurationMillis;
    volatile boolean mNoKillCachedProcessesUntilBootCompleted;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnDeviceConfigChangedForComponentAliasListener;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnDeviceConfigChangedListener;
    private int mOverrideMaxCachedProcesses;
    private final android.util.KeyValueListParser mParser;
    volatile boolean mPrioritizeAlarmBroadcasts;
    volatile int mProcStateDebugSetProcStateDelay;
    volatile int mProcStateDebugSetUidStateDelay;
    volatile android.util.SparseBooleanArray mProcStateDebugUids;
    volatile long mProcessKillTimeoutMs;
    volatile int mPushMessagingOverQuotaBehavior;
    private android.content.ContentResolver mResolver;
    private final com.android.server.am.ActivityManagerService mService;
    volatile long mServiceBindAlmostPerceptibleTimeoutMs;
    volatile int mServiceStartForegroundAnrDelayMs;
    volatile int mServiceStartForegroundTimeoutMs;
    public volatile long mShortFgsAnrExtraWaitDuration;
    public volatile long mShortFgsProcStateExtraWaitDuration;
    public volatile long mShortFgsTimeoutDuration;
    private final boolean mSystemServerAutomaticHeapDumpEnabled;
    private final java.lang.String mSystemServerAutomaticHeapDumpPackageName;
    private long mSystemServerAutomaticHeapDumpPssThresholdBytes;
    volatile long mVisibleToInvisibleUijScheduleGraceDurationMs;

    static {
        DEFAULT_POWER_CHECK_INTERVAL = (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER_QUICK ? 1 : 5) * 60 * 1000;
        DEFAULT_ENABLE_NEW_OOM_ADJ = com.android.server.am.Flags.oomadjusterCorrectnessRewrite();
        DEFAULT_ENABLE_BATCHING_OOM_ADJ = com.android.server.am.Flags.batchingOomAdj();
        DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE = new long[]{0, 10000, 20000, 30000};
        DEFAULT_SERVICE_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * DEFAULT_BOOT_TIME_TEMP_ALLOWLIST_DURATION;
        DEFAULT_SERVICE_BACKGROUND_TIMEOUT = DEFAULT_SERVICE_TIMEOUT * 10;
        MAX_PREVIOUS_TIME = 60000L;
        MIN_CRASH_INTERVAL = 120000;
        PROCESS_CRASH_COUNT_RESET_INTERVAL = 43200000L;
        PROCESS_CRASH_COUNT_LIMIT = 12;
        ACTIVITY_MANAGER_CONSTANTS_URI = android.provider.Settings.Global.getUriFor("activity_manager_constants");
        ACTIVITY_STARTS_LOGGING_ENABLED_URI = android.provider.Settings.Global.getUriFor("activity_starts_logging_enabled");
        FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI = android.provider.Settings.Global.getUriFor("foreground_service_starts_logging_enabled");
        ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI = android.provider.Settings.Global.getUriFor("enable_automatic_system_server_heap_dumps");
        FORCE_ENABLE_PSS_PROFILING_URI = android.provider.Settings.Global.getUriFor("force_enable_pss_profiling");
        MIN_ASSOC_LOG_DURATION = 300000L;
        PROACTIVE_KILLS_ENABLED = false;
        LOW_SWAP_THRESHOLD_PERCENT = DEFAULT_LOW_SWAP_THRESHOLD_PERCENT;
    }

    ActivityManagerConstants(android.content.Context context, com.android.server.am.ActivityManagerService service, android.os.Handler handler) {
        super(handler);
        boolean z = false;
        this.mProcStateDebugUids = new android.util.SparseBooleanArray(0);
        this.mEnableProcStateStacktrace = false;
        this.mProcStateDebugSetProcStateDelay = 0;
        this.mProcStateDebugSetUidStateDelay = 0;
        this.MAX_CACHED_PROCESSES = 1024;
        this.BACKGROUND_SETTLE_TIME = 60000L;
        this.FGSERVICE_MIN_SHOWN_TIME = DEFAULT_FGSERVICE_MIN_SHOWN_TIME;
        this.FGSERVICE_MIN_REPORT_TIME = 3000L;
        this.FGSERVICE_SCREEN_ON_BEFORE_TIME = 1000L;
        this.FGSERVICE_SCREEN_ON_AFTER_TIME = 5000L;
        this.FGS_BOOT_COMPLETED_ALLOWLIST = DEFAULT_FGS_BOOT_COMPLETED_ALLOWLIST;
        this.CONTENT_PROVIDER_RETAIN_TIME = 20000L;
        this.GC_TIMEOUT = 5000L;
        this.GC_MIN_INTERVAL = 60000L;
        this.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = true;
        this.FULL_PSS_MIN_INTERVAL = DEFAULT_FULL_PSS_MIN_INTERVAL;
        this.FULL_PSS_LOWERED_INTERVAL = 300000L;
        this.POWER_CHECK_INTERVAL = DEFAULT_POWER_CHECK_INTERVAL;
        this.POWER_CHECK_MAX_CPU_1 = 25;
        this.POWER_CHECK_MAX_CPU_2 = 25;
        this.POWER_CHECK_MAX_CPU_3 = 10;
        this.POWER_CHECK_MAX_CPU_4 = 2;
        this.SERVICE_USAGE_INTERACTION_TIME_PRE_S = 1800000L;
        this.SERVICE_USAGE_INTERACTION_TIME_POST_S = 60000L;
        this.USAGE_STATS_INTERACTION_INTERVAL_PRE_S = 7200000L;
        this.USAGE_STATS_INTERACTION_INTERVAL_POST_S = 600000L;
        this.SERVICE_RESTART_DURATION = 1000L;
        this.SERVICE_RESTART_DURATION_LOW = 30000L;
        this.SERVICE_RESET_RUN_DURATION = 60000L;
        this.SERVICE_RESTART_DURATION_FACTOR = 4;
        this.SERVICE_MIN_RESTART_TIME_BETWEEN = 10000L;
        this.SERVICE_TIMEOUT = DEFAULT_SERVICE_TIMEOUT;
        this.SERVICE_BACKGROUND_TIMEOUT = DEFAULT_SERVICE_BACKGROUND_TIMEOUT;
        this.MAX_SERVICE_INACTIVITY = 1800000L;
        this.BG_START_TIMEOUT = 15000L;
        this.SERVICE_BG_ACTIVITY_START_TIMEOUT = 10000L;
        this.BOUND_SERVICE_CRASH_RESTART_DURATION = 1800000L;
        this.BOUND_SERVICE_MAX_CRASH_RETRY = 16L;
        this.FLAG_PROCESS_START_ASYNC = true;
        this.MEMORY_INFO_THROTTLE_TIME = 300000L;
        this.TOP_TO_FGS_GRACE_DURATION = 15000L;
        this.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = 15000L;
        this.mFlagBackgroundFgsStartRestrictionEnabled = true;
        this.mFlagFgsStartRestrictionEnabled = true;
        this.mFgsStartRestrictionNotificationEnabled = false;
        this.mForceEnablePssProfiling = false;
        this.mFgsStartRestrictionCheckCallerTargetSdk = true;
        this.mFlagFgsNotificationDeferralEnabled = true;
        this.mFlagFgsNotificationDeferralApiGated = false;
        this.mFgsNotificationDeferralInterval = 10000L;
        this.mFgsNotificationDeferralIntervalForShort = this.mFgsNotificationDeferralInterval;
        this.mFgsNotificationDeferralExclusionTime = 120000L;
        this.mFgsNotificationDeferralExclusionTimeForShort = this.mFgsNotificationDeferralExclusionTime;
        this.mFlagSystemExemptPowerRestrictionsEnabled = true;
        this.mPushMessagingOverQuotaBehavior = 1;
        this.mBootTimeTempAllowlistDuration = 20000L;
        this.mFgToBgFgsGraceDuration = 5000L;
        this.mVisibleToInvisibleUijScheduleGraceDurationMs = 5000L;
        this.mFgsStartForegroundTimeoutMs = 10000L;
        this.mFgsAtomSampleRate = 1.0f;
        this.mFgsStartAllowedLogSampleRate = DEFAULT_FGS_START_ALLOWED_LOG_SAMPLE_RATE;
        this.mFgsStartDeniedLogSampleRate = 1.0f;
        this.mKillBgRestrictedAndCachedIdle = false;
        this.mKillBgRestrictedAndCachedIdleSettleTimeMs = 60000L;
        this.mProcessKillTimeoutMs = 10000L;
        this.mFgsAllowOptOut = false;
        this.mExtraServiceRestartDelayOnMemPressure = DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE;
        this.mEnableExtraServiceRestartDelayOnMemPressure = true;
        this.mEnableComponentAlias = false;
        this.mDeferBootCompletedBroadcast = 6;
        this.mPrioritizeAlarmBroadcasts = true;
        this.mServiceStartForegroundTimeoutMs = 30000;
        this.mServiceStartForegroundAnrDelayMs = 10000;
        this.mServiceBindAlmostPerceptibleTimeoutMs = 15000L;
        this.mComponentAliasOverrides = "";
        this.mMaxServiceConnectionsPerProcess = 3000;
        this.mParser = new android.util.KeyValueListParser(',');
        this.mOverrideMaxCachedProcesses = -1;
        this.mNoKillCachedProcessesUntilBootCompleted = true;
        this.mNoKillCachedProcessesPostBootCompletedDurationMillis = 600000L;
        this.CUR_TRIM_EMPTY_PROCESSES = computeEmptyProcessLimit(this.MAX_CACHED_PROCESSES) / 2;
        this.CUR_TRIM_CACHED_PROCESSES = (this.MAX_CACHED_PROCESSES - computeEmptyProcessLimit(this.MAX_CACHED_PROCESSES)) / 3;
        this.mMaxEmptyTimeMillis = DEFAULT_MAX_EMPTY_TIME_MILLIS;
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES = new android.util.ArraySet<>();
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES = new android.util.ArraySet<>();
        this.PENDINGINTENT_WARNING_THRESHOLD = 2000;
        this.KEEP_WARMING_SERVICES = new android.util.ArraySet<>();
        this.MAX_PHANTOM_PROCESSES = 32;
        this.mNetworkAccessTimeoutMs = DEFAULT_NETWORK_ACCESS_TIMEOUT_MS;
        this.OOMADJ_UPDATE_QUICK = true;
        this.mShortFgsTimeoutDuration = 180000L;
        this.mShortFgsProcStateExtraWaitDuration = 5000L;
        this.mMediaProcessingFgsTimeoutDuration = 21600000L;
        this.mDataSyncFgsTimeoutDuration = 21600000L;
        this.mEnableWaitForFinishAttachApplication = false;
        this.mShortFgsAnrExtraWaitDuration = 10000L;
        this.mFgsCrashExtraWaitDuration = 10000L;
        this.USE_TIERED_CACHED_ADJ = false;
        this.TIERED_CACHED_ADJ_DECAY_TIME = 60000L;
        this.ENABLE_NEW_OOMADJ = DEFAULT_ENABLE_NEW_OOM_ADJ;
        this.ENABLE_BATCHING_OOM_ADJ = DEFAULT_ENABLE_BATCHING_OOM_ADJ;
        this.FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION = 1000L;
        this.mOnDeviceConfigChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.ActivityManagerConstants.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:209:0x034a  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties r4) {
                /*
                    Method dump skipped, instruction units count: 1656
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerConstants.AnonymousClass1.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
            }
        };
        this.mOnDeviceConfigChangedForComponentAliasListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.ActivityManagerConstants.2
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:17:0x0033  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties r4) {
                /*
                    r3 = this;
                    java.util.Set r0 = r4.getKeyset()
                    java.util.Iterator r0 = r0.iterator()
                L8:
                    boolean r1 = r0.hasNext()
                    if (r1 == 0) goto L3f
                    java.lang.Object r1 = r0.next()
                    java.lang.String r1 = (java.lang.String) r1
                    if (r1 != 0) goto L17
                    return
                L17:
                    int r2 = r1.hashCode()
                    switch(r2) {
                        case -1542414221: goto L29;
                        case 551822262: goto L1f;
                        default: goto L1e;
                    }
                L1e:
                    goto L33
                L1f:
                    java.lang.String r2 = "component_alias_overrides"
                    boolean r2 = r1.equals(r2)
                    if (r2 == 0) goto L1e
                    r2 = 1
                    goto L34
                L29:
                    java.lang.String r2 = "enable_experimental_component_alias"
                    boolean r2 = r1.equals(r2)
                    if (r2 == 0) goto L1e
                    r2 = 0
                    goto L34
                L33:
                    r2 = -1
                L34:
                    switch(r2) {
                        case 0: goto L38;
                        case 1: goto L38;
                        default: goto L37;
                    }
                L37:
                    goto L3e
                L38:
                    com.android.server.am.ActivityManagerConstants r2 = com.android.server.am.ActivityManagerConstants.this
                    com.android.server.am.ActivityManagerConstants.m1071$$Nest$mupdateComponentAliases(r2)
                L3e:
                    goto L8
                L3f:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerConstants.AnonymousClass2.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
            }
        };
        this.mService = service;
        if (android.os.Build.IS_DEBUGGABLE && context.getResources().getBoolean(android.R.bool.config_decoupleStatusBarAndDisplayCutoutFromScreenSize)) {
            z = true;
        }
        this.mSystemServerAutomaticHeapDumpEnabled = z;
        this.mSystemServerAutomaticHeapDumpPackageName = context.getPackageName();
        this.mSystemServerAutomaticHeapDumpPssThresholdBytes = java.lang.Math.max(MIN_AUTOMATIC_HEAP_DUMP_PSS_THRESHOLD_BYTES, context.getResources().getInteger(android.R.integer.config_datause_notification_type));
        this.mDefaultImperceptibleKillExemptPackages = java.util.Arrays.asList(context.getResources().getStringArray(android.R.array.config_defaultAllowlistLaunchOnPrivateDisplayPackages));
        this.mDefaultImperceptibleKillExemptProcStates = (java.util.List) java.util.Arrays.stream(context.getResources().getIntArray(android.R.array.config_defaultAmbientContextServices)).boxed().collect(java.util.stream.Collectors.toList());
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.addAll(this.mDefaultImperceptibleKillExemptPackages);
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.addAll(this.mDefaultImperceptibleKillExemptProcStates);
        this.mDefaultBinderHeavyHitterWatcherEnabled = context.getResources().getBoolean(android.R.bool.config_defaultEmergencyGestureEnabled);
        this.mDefaultBinderHeavyHitterWatcherBatchSize = context.getResources().getInteger(android.R.integer.config_debugSystemServerPssThresholdBytes);
        this.mDefaultBinderHeavyHitterWatcherThreshold = context.getResources().getFloat(android.R.dimen.chooser_icon_size);
        this.mDefaultBinderHeavyHitterAutoSamplerEnabled = context.getResources().getBoolean(android.R.bool.config_defaultBinderHeavyHitterWatcherEnabled);
        this.mDefaultBinderHeavyHitterAutoSamplerBatchSize = context.getResources().getInteger(android.R.integer.config_datause_throttle_kbitsps);
        this.mDefaultBinderHeavyHitterAutoSamplerThreshold = context.getResources().getFloat(android.R.dimen.chooser_header_scroll_elevation);
        BINDER_HEAVY_HITTER_WATCHER_ENABLED = this.mDefaultBinderHeavyHitterWatcherEnabled;
        BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = this.mDefaultBinderHeavyHitterWatcherBatchSize;
        BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = this.mDefaultBinderHeavyHitterWatcherThreshold;
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = this.mDefaultBinderHeavyHitterAutoSamplerEnabled;
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = this.mDefaultBinderHeavyHitterAutoSamplerBatchSize;
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD = this.mDefaultBinderHeavyHitterAutoSamplerThreshold;
        service.scheduleUpdateBinderHeavyHitterWatcherConfig();
        this.KEEP_WARMING_SERVICES.addAll((java.util.Collection<? extends android.content.ComponentName>) java.util.Arrays.stream(context.getResources().getStringArray(android.R.array.config_highRefreshRateBlacklist)).map(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda71()).collect(java.util.stream.Collectors.toSet()));
        this.mCustomizedMaxCachedProcesses = context.getResources().getInteger(android.R.integer.config_cdma_3waycall_flash_delay);
        this.CUR_MAX_CACHED_PROCESSES = this.mCustomizedMaxCachedProcesses;
        this.CUR_MAX_EMPTY_PROCESSES = computeEmptyProcessLimit(this.CUR_MAX_CACHED_PROCESSES);
        int rawMaxEmptyProcesses = computeEmptyProcessLimit(java.lang.Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES));
        this.CUR_TRIM_EMPTY_PROCESSES = rawMaxEmptyProcesses / 2;
        this.CUR_TRIM_CACHED_PROCESSES = (java.lang.Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES) - rawMaxEmptyProcesses) / 3;
        loadNativeBootDeviceConfigConstants();
        this.mDefaultDisableAppProfilerPssProfiling = context.getResources().getBoolean(android.R.bool.config_allow_ussd_over_ims);
        this.APP_PROFILER_PSS_PROFILING_DISABLED = this.mDefaultDisableAppProfilerPssProfiling;
        this.mDefaultPssToRssThresholdModifier = context.getResources().getFloat(android.R.dimen.car_touch_target_size);
        this.PSS_TO_RSS_THRESHOLD_MODIFIER = this.mDefaultPssToRssThresholdModifier;
    }

    public void start(android.content.ContentResolver resolver) {
        this.mResolver = resolver;
        this.mResolver.registerContentObserver(ACTIVITY_MANAGER_CONSTANTS_URI, false, this);
        this.mResolver.registerContentObserver(ACTIVITY_STARTS_LOGGING_ENABLED_URI, false, this);
        this.mResolver.registerContentObserver(FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI, false, this);
        if (this.mSystemServerAutomaticHeapDumpEnabled) {
            this.mResolver.registerContentObserver(ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI, false, this);
        }
        this.mResolver.registerContentObserver(FORCE_ENABLE_PSS_PROFILING_URI, false, this);
        updateConstants();
        if (this.mSystemServerAutomaticHeapDumpEnabled) {
            updateEnableAutomaticSystemServerHeapDumps();
        }
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager", android.app.ActivityThread.currentApplication().getMainExecutor(), this.mOnDeviceConfigChangedListener);
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager_ca", android.app.ActivityThread.currentApplication().getMainExecutor(), this.mOnDeviceConfigChangedForComponentAliasListener);
        loadDeviceConfigConstants();
        updateActivityStartsLoggingEnabled();
        updateForegroundServiceStartsLoggingEnabled();
        updateForceEnablePssProfiling();
        this.mService.initDropboxRateLimiter();
    }

    void loadDeviceConfigConstants() {
        this.mOnDeviceConfigChangedListener.onPropertiesChanged(android.provider.DeviceConfig.getProperties("activity_manager", new java.lang.String[0]));
        this.mOnDeviceConfigChangedForComponentAliasListener.onPropertiesChanged(android.provider.DeviceConfig.getProperties("activity_manager_ca", new java.lang.String[0]));
    }

    private void loadNativeBootDeviceConfigConstants() {
        this.ENABLE_NEW_OOMADJ = com.android.server.am.BroadcastConstants.getDeviceConfigBoolean(KEY_ENABLE_NEW_OOMADJ, DEFAULT_ENABLE_NEW_OOM_ADJ);
        this.ENABLE_BATCHING_OOM_ADJ = com.android.server.am.BroadcastConstants.getDeviceConfigBoolean(KEY_ENABLE_BATCHING_OOM_ADJ, DEFAULT_ENABLE_BATCHING_OOM_ADJ);
    }

    public void setOverrideMaxCachedProcesses(int value) {
        this.mOverrideMaxCachedProcesses = value;
        updateMaxCachedProcesses();
    }

    public int getOverrideMaxCachedProcesses() {
        return this.mOverrideMaxCachedProcesses;
    }

    public static int computeEmptyProcessLimit(int totalProcessLimit) {
        return totalProcessLimit / 2;
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange, android.net.Uri uri) {
        if (uri == null) {
            return;
        }
        if (ACTIVITY_MANAGER_CONSTANTS_URI.equals(uri)) {
            updateConstants();
            return;
        }
        if (ACTIVITY_STARTS_LOGGING_ENABLED_URI.equals(uri)) {
            updateActivityStartsLoggingEnabled();
            return;
        }
        if (FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED_URI.equals(uri)) {
            updateForegroundServiceStartsLoggingEnabled();
        } else if (ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS_URI.equals(uri)) {
            updateEnableAutomaticSystemServerHeapDumps();
        } else if (FORCE_ENABLE_PSS_PROFILING_URI.equals(uri)) {
            updateForceEnablePssProfiling();
        }
    }

    private void updateConstants() {
        java.lang.String setting = android.provider.Settings.Global.getString(this.mResolver, "activity_manager_constants");
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                try {
                    this.mParser.setString(setting);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(TAG, "Bad activity manager config settings", e);
                }
                long currentPowerCheckInterval = this.POWER_CHECK_INTERVAL;
                this.BACKGROUND_SETTLE_TIME = this.mParser.getLong(KEY_BACKGROUND_SETTLE_TIME, 60000L);
                this.FGSERVICE_MIN_SHOWN_TIME = this.mParser.getLong(KEY_FGSERVICE_MIN_SHOWN_TIME, DEFAULT_FGSERVICE_MIN_SHOWN_TIME);
                this.FGSERVICE_MIN_REPORT_TIME = this.mParser.getLong(KEY_FGSERVICE_MIN_REPORT_TIME, 3000L);
                this.FGSERVICE_SCREEN_ON_BEFORE_TIME = this.mParser.getLong(KEY_FGSERVICE_SCREEN_ON_BEFORE_TIME, 1000L);
                this.FGSERVICE_SCREEN_ON_AFTER_TIME = this.mParser.getLong(KEY_FGSERVICE_SCREEN_ON_AFTER_TIME, 5000L);
                this.FGS_BOOT_COMPLETED_ALLOWLIST = this.mParser.getInt(KEY_FGS_BOOT_COMPLETED_ALLOWLIST, DEFAULT_FGS_BOOT_COMPLETED_ALLOWLIST);
                this.CONTENT_PROVIDER_RETAIN_TIME = this.mParser.getLong(KEY_CONTENT_PROVIDER_RETAIN_TIME, 20000L);
                this.GC_TIMEOUT = this.mParser.getLong(KEY_GC_TIMEOUT, 5000L);
                this.GC_MIN_INTERVAL = this.mParser.getLong(KEY_GC_MIN_INTERVAL, 60000L);
                this.FULL_PSS_MIN_INTERVAL = this.mParser.getLong(KEY_FULL_PSS_MIN_INTERVAL, DEFAULT_FULL_PSS_MIN_INTERVAL);
                this.FULL_PSS_LOWERED_INTERVAL = this.mParser.getLong(KEY_FULL_PSS_LOWERED_INTERVAL, 300000L);
                this.POWER_CHECK_INTERVAL = this.mParser.getLong(KEY_POWER_CHECK_INTERVAL, DEFAULT_POWER_CHECK_INTERVAL);
                this.POWER_CHECK_MAX_CPU_1 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_1, 25);
                this.POWER_CHECK_MAX_CPU_2 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_2, 25);
                this.POWER_CHECK_MAX_CPU_3 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_3, 10);
                this.POWER_CHECK_MAX_CPU_4 = this.mParser.getInt(KEY_POWER_CHECK_MAX_CPU_4, 2);
                this.SERVICE_USAGE_INTERACTION_TIME_PRE_S = this.mParser.getLong(KEY_SERVICE_USAGE_INTERACTION_TIME_PRE_S, 1800000L);
                this.SERVICE_USAGE_INTERACTION_TIME_POST_S = this.mParser.getLong(KEY_SERVICE_USAGE_INTERACTION_TIME_POST_S, 60000L);
                this.USAGE_STATS_INTERACTION_INTERVAL_PRE_S = this.mParser.getLong(KEY_USAGE_STATS_INTERACTION_INTERVAL_PRE_S, 7200000L);
                this.USAGE_STATS_INTERACTION_INTERVAL_POST_S = this.mParser.getLong(KEY_USAGE_STATS_INTERACTION_INTERVAL_POST_S, 600000L);
                this.SERVICE_RESTART_DURATION = this.mParser.getLong(KEY_SERVICE_RESTART_DURATION, 1000L);
                this.SERVICE_RESET_RUN_DURATION = this.mParser.getLong(KEY_SERVICE_RESET_RUN_DURATION, 60000L);
                this.SERVICE_RESTART_DURATION_FACTOR = this.mParser.getInt(KEY_SERVICE_RESTART_DURATION_FACTOR, 4);
                this.SERVICE_MIN_RESTART_TIME_BETWEEN = this.mParser.getLong(KEY_SERVICE_MIN_RESTART_TIME_BETWEEN, 10000L);
                this.MAX_SERVICE_INACTIVITY = this.mParser.getLong(KEY_MAX_SERVICE_INACTIVITY, 1800000L);
                this.BG_START_TIMEOUT = this.mParser.getLong(KEY_BG_START_TIMEOUT, 15000L);
                this.SERVICE_BG_ACTIVITY_START_TIMEOUT = this.mParser.getLong(KEY_SERVICE_BG_ACTIVITY_START_TIMEOUT, 10000L);
                this.BOUND_SERVICE_CRASH_RESTART_DURATION = this.mParser.getLong(KEY_BOUND_SERVICE_CRASH_RESTART_DURATION, 1800000L);
                this.BOUND_SERVICE_MAX_CRASH_RETRY = this.mParser.getInt(KEY_BOUND_SERVICE_CRASH_MAX_RETRY, 16);
                this.FLAG_PROCESS_START_ASYNC = this.mParser.getBoolean(KEY_PROCESS_START_ASYNC, true);
                this.MEMORY_INFO_THROTTLE_TIME = this.mParser.getLong(KEY_MEMORY_INFO_THROTTLE_TIME, 300000L);
                this.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION = this.mParser.getDurationMillis(KEY_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION, 15000L);
                MIN_CRASH_INTERVAL = this.mParser.getInt(KEY_MIN_CRASH_INTERVAL, 120000);
                this.PENDINGINTENT_WARNING_THRESHOLD = this.mParser.getInt(KEY_PENDINGINTENT_WARNING_THRESHOLD, 2000);
                PROCESS_CRASH_COUNT_RESET_INTERVAL = this.mParser.getInt(KEY_PROCESS_CRASH_COUNT_RESET_INTERVAL, DEFAULT_PROCESS_CRASH_COUNT_RESET_INTERVAL);
                PROCESS_CRASH_COUNT_LIMIT = this.mParser.getInt(KEY_PROCESS_CRASH_COUNT_LIMIT, 12);
                if (this.POWER_CHECK_INTERVAL != currentPowerCheckInterval) {
                    this.mService.mHandler.removeMessages(27);
                    android.os.Message msg = this.mService.mHandler.obtainMessage(27);
                    this.mService.mHandler.sendMessageDelayed(msg, this.POWER_CHECK_INTERVAL);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void updateActivityStartsLoggingEnabled() {
        this.mFlagActivityStartsLoggingEnabled = android.provider.Settings.Global.getInt(this.mResolver, "activity_starts_logging_enabled", 1) == 1;
    }

    private void updateForceEnablePssProfiling() {
        this.mForceEnablePssProfiling = android.provider.Settings.Global.getInt(this.mResolver, "force_enable_pss_profiling", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBackgroundActivityStarts() {
        this.mFlagBackgroundActivityStartsEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED, false);
    }

    private void updateForegroundServiceStartsLoggingEnabled() {
        this.mFlagForegroundServiceStartsLoggingEnabled = android.provider.Settings.Global.getInt(this.mResolver, "foreground_service_starts_logging_enabled", 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBackgroundFgsStartsRestriction() {
        this.mFlagBackgroundFgsStartRestrictionEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartsRestriction() {
        this.mFlagFgsStartRestrictionEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartsRestrictionNotification() {
        this.mFgsStartRestrictionNotificationEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartsRestrictionCheckCallerTargetSdk() {
        this.mFgsStartRestrictionCheckCallerTargetSdk = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralEnable() {
        this.mFlagFgsNotificationDeferralEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralApiGated() {
        this.mFlagFgsNotificationDeferralApiGated = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralInterval() {
        this.mFgsNotificationDeferralInterval = android.provider.DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralIntervalForShort() {
        this.mFgsNotificationDeferralIntervalForShort = android.provider.DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralExclusionTime() {
        this.mFgsNotificationDeferralExclusionTime = android.provider.DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME, 120000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsNotificationDeferralExclusionTimeForShort() {
        this.mFgsNotificationDeferralExclusionTimeForShort = android.provider.DeviceConfig.getLong("activity_manager", KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT, 120000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemExemptPowerRestrictionsEnabled() {
        this.mFlagSystemExemptPowerRestrictionsEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePushMessagingOverQuotaBehavior() {
        this.mPushMessagingOverQuotaBehavior = android.provider.DeviceConfig.getInt("activity_manager", KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR, 1);
        if (this.mPushMessagingOverQuotaBehavior < -1 || this.mPushMessagingOverQuotaBehavior > 1) {
            this.mPushMessagingOverQuotaBehavior = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOomAdjUpdatePolicy() {
        this.OOMADJ_UPDATE_QUICK = android.provider.DeviceConfig.getInt("activity_manager", KEY_OOMADJ_UPDATE_POLICY, 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForceRestrictedBackgroundCheck() {
        this.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBootTimeTempAllowListDuration() {
        this.mBootTimeTempAllowlistDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION, 20000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgToBgFgsGraceDuration() {
        this.mFgToBgFgsGraceDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_FG_TO_BG_FGS_GRACE_DURATION, 5000L);
    }

    private void updateVisibleToInvisibleUijScheduleGraceDuration() {
        this.mVisibleToInvisibleUijScheduleGraceDurationMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_VISIBLE_TO_INVISIBLE_UIJ_SCHEDULE_GRACE_DURATION, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartForegroundTimeout() {
        this.mFgsStartForegroundTimeoutMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_FGS_START_FOREGROUND_TIMEOUT, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsAtomSamplePercent() {
        this.mFgsAtomSampleRate = android.provider.DeviceConfig.getFloat("activity_manager", KEY_FGS_ATOM_SAMPLE_RATE, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartAllowedLogSamplePercent() {
        this.mFgsStartAllowedLogSampleRate = android.provider.DeviceConfig.getFloat("activity_manager", KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE, DEFAULT_FGS_START_ALLOWED_LOG_SAMPLE_RATE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsStartDeniedLogSamplePercent() {
        this.mFgsStartDeniedLogSampleRate = android.provider.DeviceConfig.getFloat("activity_manager", KEY_FGS_START_DENIED_LOG_SAMPLE_RATE, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKillBgRestrictedCachedIdle() {
        this.mKillBgRestrictedAndCachedIdle = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_KILL_BG_RESTRICTED_CACHED_IDLE, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKillBgRestrictedCachedIdleSettleTime() {
        long currentSettleTime = this.mKillBgRestrictedAndCachedIdleSettleTimeMs;
        this.mKillBgRestrictedAndCachedIdleSettleTimeMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_KILL_BG_RESTRICTED_CACHED_IDLE_SETTLE_TIME, 60000L);
        if (this.mKillBgRestrictedAndCachedIdleSettleTimeMs < currentSettleTime) {
            this.mService.mHandler.sendEmptyMessageDelayed(58, this.mKillBgRestrictedAndCachedIdleSettleTimeMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsAllowOptOut() {
        this.mFgsAllowOptOut = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_FGS_ALLOW_OPT_OUT, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExtraServiceRestartDelayOnMemPressure() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                int memFactor = this.mService.mAppProfiler.getLastMemoryLevelLocked();
                long[] prevDelays = this.mExtraServiceRestartDelayOnMemPressure;
                this.mExtraServiceRestartDelayOnMemPressure = parseLongArray(KEY_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE, DEFAULT_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE);
                this.mService.mServices.performRescheduleServiceRestartOnMemoryPressureLocked(this.mExtraServiceRestartDelayOnMemPressure[memFactor], prevDelays[memFactor], "config", android.os.SystemClock.uptimeMillis());
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnableExtraServiceRestartDelayOnMemPressure() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                boolean prevEnabled = this.mEnableExtraServiceRestartDelayOnMemPressure;
                this.mEnableExtraServiceRestartDelayOnMemPressure = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_ENABLE_EXTRA_SERVICE_RESTART_DELAY_ON_MEM_PRESSURE, true);
                this.mService.mServices.rescheduleServiceRestartOnMemoryPressureIfNeededLocked(prevEnabled, this.mEnableExtraServiceRestartDelayOnMemPressure, android.os.SystemClock.uptimeMillis());
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePrioritizeAlarmBroadcasts() {
        boolean z;
        java.lang.String flag = android.provider.DeviceConfig.getString("activity_manager", KEY_PRIORITIZE_ALARM_BROADCASTS, "");
        if (android.text.TextUtils.isEmpty(flag)) {
            z = true;
        } else {
            z = java.lang.Boolean.parseBoolean(flag);
        }
        this.mPrioritizeAlarmBroadcasts = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeferBootCompletedBroadcast() {
        this.mDeferBootCompletedBroadcast = android.provider.DeviceConfig.getInt("activity_manager", KEY_DEFER_BOOT_COMPLETED_BROADCAST, 6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNoKillCachedProcessesUntilBootCompleted() {
        this.mNoKillCachedProcessesUntilBootCompleted = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNoKillCachedProcessesPostBootCompletedDurationMillis() {
        this.mNoKillCachedProcessesPostBootCompletedDurationMillis = android.provider.DeviceConfig.getLong("activity_manager", KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS, 600000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxEmptyTimeMillis() {
        this.mMaxEmptyTimeMillis = android.provider.DeviceConfig.getLong("activity_manager", KEY_MAX_EMPTY_TIME_MILLIS, DEFAULT_MAX_EMPTY_TIME_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkAccessTimeoutMs() {
        this.mNetworkAccessTimeoutMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_NETWORK_ACCESS_TIMEOUT_MS, DEFAULT_NETWORK_ACCESS_TIMEOUT_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceStartForegroundTimeoutMs() {
        this.mServiceStartForegroundTimeoutMs = android.provider.DeviceConfig.getInt("activity_manager", KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS, 30000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceStartForegroundAnrDealyMs() {
        this.mServiceStartForegroundAnrDelayMs = android.provider.DeviceConfig.getInt("activity_manager", KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS, 10000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceBindAlmostPerceptibleTimeoutMs() {
        this.mServiceBindAlmostPerceptibleTimeoutMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS, 15000L);
    }

    private long[] parseLongArray(java.lang.String key, long[] def) {
        java.lang.String val = android.provider.DeviceConfig.getString("activity_manager", key, (java.lang.String) null);
        if (!android.text.TextUtils.isEmpty(val)) {
            java.lang.String[] ss = val.split(",");
            if (ss.length == def.length) {
                long[] tmp = new long[ss.length];
                for (int i = 0; i < ss.length; i++) {
                    try {
                        tmp[i] = java.lang.Long.parseLong(ss[i]);
                    } catch (java.lang.NumberFormatException e) {
                    }
                }
                return tmp;
            }
        }
        return def;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateComponentAliases() {
        this.mEnableComponentAlias = android.provider.DeviceConfig.getBoolean("activity_manager_ca", KEY_ENABLE_COMPONENT_ALIAS, false);
        this.mComponentAliasOverrides = android.provider.DeviceConfig.getString("activity_manager_ca", KEY_COMPONENT_ALIAS_OVERRIDES, "");
        this.mService.mComponentAliasResolver.update(this.mEnableComponentAlias, this.mComponentAliasOverrides);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProcessKillTimeout() {
        this.mProcessKillTimeoutMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_PROCESS_KILL_TIMEOUT, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImperceptibleKillExemptions() {
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.clear();
        this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.addAll(this.mDefaultImperceptibleKillExemptPackages);
        java.lang.String val = android.provider.DeviceConfig.getString("activity_manager", KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES, (java.lang.String) null);
        if (!android.text.TextUtils.isEmpty(val)) {
            this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.addAll(java.util.Arrays.asList(val.split(",")));
        }
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.clear();
        this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.addAll(this.mDefaultImperceptibleKillExemptProcStates);
        java.lang.String val2 = android.provider.DeviceConfig.getString("activity_manager", KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES, (java.lang.String) null);
        if (!android.text.TextUtils.isEmpty(val2)) {
            java.util.Arrays.asList(val2.split(",")).stream().forEach(new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerConstants$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$updateImperceptibleKillExemptions$0((java.lang.String) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateImperceptibleKillExemptions$0(java.lang.String v) {
        try {
            this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.add(java.lang.Integer.valueOf(java.lang.Integer.parseInt(v)));
        } catch (java.lang.NumberFormatException e) {
        }
    }

    private void updateEnableAutomaticSystemServerHeapDumps() {
        if (!this.mSystemServerAutomaticHeapDumpEnabled) {
            android.util.Slog.wtf(TAG, "updateEnableAutomaticSystemServerHeapDumps called when leak detection disabled");
            return;
        }
        boolean enabled = android.provider.Settings.Global.getInt(this.mResolver, "enable_automatic_system_server_heap_dumps", 1) == 1;
        long threshold = enabled ? this.mSystemServerAutomaticHeapDumpPssThresholdBytes : 0L;
        this.mService.setDumpHeapDebugLimit(null, 0, threshold, this.mSystemServerAutomaticHeapDumpPackageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxCachedProcesses() {
        int i;
        java.lang.String maxCachedProcessesFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_MAX_CACHED_PROCESSES);
        try {
            if (this.mOverrideMaxCachedProcesses < 0) {
                i = android.text.TextUtils.isEmpty(maxCachedProcessesFlag) ? this.mCustomizedMaxCachedProcesses : java.lang.Integer.parseInt(maxCachedProcessesFlag);
            } else {
                i = this.mOverrideMaxCachedProcesses;
            }
            this.CUR_MAX_CACHED_PROCESSES = i;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Unable to parse flag for max_cached_processes: " + maxCachedProcessesFlag, e);
            this.CUR_MAX_CACHED_PROCESSES = this.mCustomizedMaxCachedProcesses;
        }
        this.CUR_MAX_EMPTY_PROCESSES = computeEmptyProcessLimit(this.CUR_MAX_CACHED_PROCESSES);
        int rawMaxEmptyProcesses = computeEmptyProcessLimit(java.lang.Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES));
        this.CUR_TRIM_EMPTY_PROCESSES = rawMaxEmptyProcesses / 2;
        this.CUR_TRIM_CACHED_PROCESSES = (java.lang.Integer.min(this.CUR_MAX_CACHED_PROCESSES, this.MAX_CACHED_PROCESSES) - rawMaxEmptyProcesses) / 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProactiveKillsEnabled() {
        PROACTIVE_KILLS_ENABLED = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_PROACTIVE_KILLS_ENABLED, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLowSwapThresholdPercent() {
        LOW_SWAP_THRESHOLD_PERCENT = android.provider.DeviceConfig.getFloat("activity_manager", KEY_LOW_SWAP_THRESHOLD_PERCENT, DEFAULT_LOW_SWAP_THRESHOLD_PERCENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTopToFgsGraceDuration() {
        this.TOP_TO_FGS_GRACE_DURATION = android.provider.DeviceConfig.getLong("activity_manager", KEY_TOP_TO_FGS_GRACE_DURATION, 15000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxPreviousTime() {
        MAX_PREVIOUS_TIME = android.provider.DeviceConfig.getLong("activity_manager", KEY_MAX_PREVIOUS_TIME, 60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProcStateDebugUids() {
        java.lang.String val = android.provider.DeviceConfig.getString("activity_manager", KEY_PROC_STATE_DEBUG_UIDS, "").trim();
        this.mEnableProcStateStacktrace = false;
        this.mProcStateDebugSetProcStateDelay = 0;
        this.mProcStateDebugSetUidStateDelay = 0;
        if (val.length() == 0) {
            this.mProcStateDebugUids = new android.util.SparseBooleanArray(0);
            return;
        }
        java.lang.String[] uids = val.split(",");
        android.util.SparseBooleanArray newArray = new android.util.SparseBooleanArray(0);
        int length = uids.length;
        for (int i = 0; i < length; i++) {
            java.lang.String token = uids[i];
            if (token.length() != 0) {
                if ("stack".equals(token)) {
                    this.mEnableProcStateStacktrace = true;
                } else {
                    boolean isUid = true;
                    char prefix = token.charAt(0);
                    if ('a' <= prefix && prefix <= 'z') {
                        isUid = false;
                        token = token.substring(1);
                    }
                    try {
                        int value = java.lang.Integer.parseInt(token.trim());
                        if (isUid) {
                            newArray.put(value, true);
                        } else if (prefix == 'p') {
                            this.mProcStateDebugSetProcStateDelay = value;
                        } else if (prefix != 'u') {
                            android.util.Slog.w(TAG, "Invalid prefix " + prefix + " in " + val);
                        } else {
                            this.mProcStateDebugSetUidStateDelay = value;
                        }
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Slog.w(TAG, "Invalid number " + token + " in " + val);
                    }
                }
            }
        }
        this.mProcStateDebugUids = newArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMinAssocLogDuration() {
        MIN_ASSOC_LOG_DURATION = android.provider.DeviceConfig.getLong("activity_manager", KEY_MIN_ASSOC_LOG_DURATION, 300000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBinderHeavyHitterWatcher() {
        BINDER_HEAVY_HITTER_WATCHER_ENABLED = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED, this.mDefaultBinderHeavyHitterWatcherEnabled);
        BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE = android.provider.DeviceConfig.getInt("activity_manager", KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE, this.mDefaultBinderHeavyHitterWatcherBatchSize);
        BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = android.provider.DeviceConfig.getFloat("activity_manager", KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD, this.mDefaultBinderHeavyHitterWatcherThreshold);
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED, this.mDefaultBinderHeavyHitterAutoSamplerEnabled);
        BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE = android.provider.DeviceConfig.getInt("activity_manager", KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE, this.mDefaultBinderHeavyHitterAutoSamplerBatchSize);
        BINDER_HEAVY_HITTER_WATCHER_THRESHOLD = android.provider.DeviceConfig.getFloat("activity_manager", KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD, this.mDefaultBinderHeavyHitterAutoSamplerThreshold);
        this.mService.scheduleUpdateBinderHeavyHitterWatcherConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxPhantomProcesses() {
        int oldVal = this.MAX_PHANTOM_PROCESSES;
        this.MAX_PHANTOM_PROCESSES = android.provider.DeviceConfig.getInt("activity_manager", KEY_MAX_PHANTOM_PROCESSES, 32);
        if (oldVal > this.MAX_PHANTOM_PROCESSES) {
            com.android.server.am.ActivityManagerService.MainHandler mainHandler = this.mService.mHandler;
            com.android.server.am.PhantomProcessList phantomProcessList = this.mService.mPhantomProcessList;
            java.util.Objects.requireNonNull(phantomProcessList);
            mainHandler.post(new com.android.server.am.ActivityManagerConstants$$ExternalSyntheticLambda0(phantomProcessList));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxServiceConnectionsPerProcess() {
        this.mMaxServiceConnectionsPerProcess = android.provider.DeviceConfig.getInt("activity_manager", KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS, 3000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShortFgsTimeoutDuration() {
        this.mShortFgsTimeoutDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_SHORT_FGS_TIMEOUT_DURATION, 180000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShortFgsProcStateExtraWaitDuration() {
        this.mShortFgsProcStateExtraWaitDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShortFgsAnrExtraWaitDuration() {
        this.mShortFgsAnrExtraWaitDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMediaProcessingFgsTimeoutDuration() {
        this.mMediaProcessingFgsTimeoutDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_MEDIA_PROCESSING_FGS_TIMEOUT_DURATION, 21600000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataSyncFgsTimeoutDuration() {
        this.mDataSyncFgsTimeoutDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_DATA_SYNC_FGS_TIMEOUT_DURATION, 21600000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFgsCrashExtraWaitDuration() {
        this.mFgsCrashExtraWaitDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_FGS_CRASH_EXTRA_WAIT_DURATION, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnableWaitForFinishAttachApplication() {
        this.mEnableWaitForFinishAttachApplication = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseTieredCachedAdj() {
        this.USE_TIERED_CACHED_ADJ = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_USE_TIERED_CACHED_ADJ, false);
        this.TIERED_CACHED_ADJ_DECAY_TIME = android.provider.DeviceConfig.getLong("activity_manager", KEY_TIERED_CACHED_ADJ_DECAY_TIME, 60000L);
    }

    private void updateEnableNewOomAdj() {
        this.ENABLE_NEW_OOMADJ = android.provider.DeviceConfig.getBoolean("activity_manager_native_boot", KEY_ENABLE_NEW_OOMADJ, DEFAULT_ENABLE_NEW_OOM_ADJ);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFollowUpOomAdjUpdateWaitDuration() {
        this.FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION = android.provider.DeviceConfig.getLong("activity_manager", KEY_FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFGSPermissionEnforcementFlagsIfNecessary(java.lang.String name) {
        android.app.ForegroundServiceTypePolicy.getDefaultPolicy().updatePermissionEnforcementFlagIfNecessary(name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisableAppProfilerPssProfiling() {
        this.APP_PROFILER_PSS_PROFILING_DISABLED = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_DISABLE_APP_PROFILER_PSS_PROFILING, this.mDefaultDisableAppProfilerPssProfiling);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePssToRssThresholdModifier() {
        this.PSS_TO_RSS_THRESHOLD_MODIFIER = android.provider.DeviceConfig.getFloat("activity_manager", KEY_PSS_TO_RSS_THRESHOLD_MODIFIER, this.mDefaultPssToRssThresholdModifier);
    }

    private void updateEnableBatchingOomAdj() {
        this.ENABLE_BATCHING_OOM_ADJ = android.provider.DeviceConfig.getBoolean("activity_manager_native_boot", KEY_ENABLE_BATCHING_OOM_ADJ, DEFAULT_ENABLE_BATCHING_OOM_ADJ);
    }

    boolean shouldDebugUidForProcState(int uid) {
        android.util.SparseBooleanArray ar = this.mProcStateDebugUids;
        int size = ar.size();
        if (size == 0) {
            return false;
        }
        if (size <= 8) {
            for (int i = 0; i < size; i++) {
                if (ar.keyAt(i) == uid) {
                    return ar.valueAt(i);
                }
            }
            return false;
        }
        return ar.get(uid, false);
    }

    boolean shouldEnableProcStateDebug() {
        return this.mProcStateDebugUids.size() > 0;
    }

    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER SETTINGS (dumpsys activity settings) activity_manager_constants:");
        pw.print("  ");
        pw.print(KEY_MAX_CACHED_PROCESSES);
        pw.print("=");
        pw.println(this.MAX_CACHED_PROCESSES);
        pw.print("  ");
        pw.print(KEY_BACKGROUND_SETTLE_TIME);
        pw.print("=");
        pw.println(this.BACKGROUND_SETTLE_TIME);
        pw.print("  ");
        pw.print(KEY_FGSERVICE_MIN_SHOWN_TIME);
        pw.print("=");
        pw.println(this.FGSERVICE_MIN_SHOWN_TIME);
        pw.print("  ");
        pw.print(KEY_FGSERVICE_MIN_REPORT_TIME);
        pw.print("=");
        pw.println(this.FGSERVICE_MIN_REPORT_TIME);
        pw.print("  ");
        pw.print(KEY_FGSERVICE_SCREEN_ON_BEFORE_TIME);
        pw.print("=");
        pw.println(this.FGSERVICE_SCREEN_ON_BEFORE_TIME);
        pw.print("  ");
        pw.print(KEY_FGSERVICE_SCREEN_ON_AFTER_TIME);
        pw.print("=");
        pw.println(this.FGSERVICE_SCREEN_ON_AFTER_TIME);
        pw.print("  ");
        pw.print(KEY_FGS_BOOT_COMPLETED_ALLOWLIST);
        pw.print("=");
        pw.println(this.FGS_BOOT_COMPLETED_ALLOWLIST);
        pw.print("  ");
        pw.print(KEY_CONTENT_PROVIDER_RETAIN_TIME);
        pw.print("=");
        pw.println(this.CONTENT_PROVIDER_RETAIN_TIME);
        pw.print("  ");
        pw.print(KEY_GC_TIMEOUT);
        pw.print("=");
        pw.println(this.GC_TIMEOUT);
        pw.print("  ");
        pw.print(KEY_GC_MIN_INTERVAL);
        pw.print("=");
        pw.println(this.GC_MIN_INTERVAL);
        pw.print("  ");
        pw.print(KEY_FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS);
        pw.print("=");
        pw.println(this.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS);
        pw.print("  ");
        pw.print(KEY_FULL_PSS_MIN_INTERVAL);
        pw.print("=");
        pw.println(this.FULL_PSS_MIN_INTERVAL);
        pw.print("  ");
        pw.print(KEY_FULL_PSS_LOWERED_INTERVAL);
        pw.print("=");
        pw.println(this.FULL_PSS_LOWERED_INTERVAL);
        pw.print("  ");
        pw.print(KEY_POWER_CHECK_INTERVAL);
        pw.print("=");
        pw.println(this.POWER_CHECK_INTERVAL);
        pw.print("  ");
        pw.print(KEY_POWER_CHECK_MAX_CPU_1);
        pw.print("=");
        pw.println(this.POWER_CHECK_MAX_CPU_1);
        pw.print("  ");
        pw.print(KEY_POWER_CHECK_MAX_CPU_2);
        pw.print("=");
        pw.println(this.POWER_CHECK_MAX_CPU_2);
        pw.print("  ");
        pw.print(KEY_POWER_CHECK_MAX_CPU_3);
        pw.print("=");
        pw.println(this.POWER_CHECK_MAX_CPU_3);
        pw.print("  ");
        pw.print(KEY_POWER_CHECK_MAX_CPU_4);
        pw.print("=");
        pw.println(this.POWER_CHECK_MAX_CPU_4);
        pw.print("  ");
        pw.print(KEY_SERVICE_USAGE_INTERACTION_TIME_PRE_S);
        pw.print("=");
        pw.println(this.SERVICE_USAGE_INTERACTION_TIME_PRE_S);
        pw.print("  ");
        pw.print(KEY_SERVICE_USAGE_INTERACTION_TIME_POST_S);
        pw.print("=");
        pw.println(this.SERVICE_USAGE_INTERACTION_TIME_POST_S);
        pw.print("  ");
        pw.print(KEY_USAGE_STATS_INTERACTION_INTERVAL_PRE_S);
        pw.print("=");
        pw.println(this.USAGE_STATS_INTERACTION_INTERVAL_PRE_S);
        pw.print("  ");
        pw.print(KEY_USAGE_STATS_INTERACTION_INTERVAL_POST_S);
        pw.print("=");
        pw.println(this.USAGE_STATS_INTERACTION_INTERVAL_POST_S);
        pw.print("  ");
        pw.print(KEY_SERVICE_RESTART_DURATION);
        pw.print("=");
        pw.println(this.SERVICE_RESTART_DURATION);
        pw.print("  ");
        pw.print(KEY_SERVICE_RESET_RUN_DURATION);
        pw.print("=");
        pw.println(this.SERVICE_RESET_RUN_DURATION);
        pw.print("  ");
        pw.print(KEY_SERVICE_RESTART_DURATION_FACTOR);
        pw.print("=");
        pw.println(this.SERVICE_RESTART_DURATION_FACTOR);
        pw.print("  ");
        pw.print(KEY_SERVICE_MIN_RESTART_TIME_BETWEEN);
        pw.print("=");
        pw.println(this.SERVICE_MIN_RESTART_TIME_BETWEEN);
        pw.print("  ");
        pw.print(KEY_MAX_SERVICE_INACTIVITY);
        pw.print("=");
        pw.println(this.MAX_SERVICE_INACTIVITY);
        pw.print("  ");
        pw.print(KEY_BG_START_TIMEOUT);
        pw.print("=");
        pw.println(this.BG_START_TIMEOUT);
        pw.print("  ");
        pw.print(KEY_SERVICE_BG_ACTIVITY_START_TIMEOUT);
        pw.print("=");
        pw.println(this.SERVICE_BG_ACTIVITY_START_TIMEOUT);
        pw.print("  ");
        pw.print(KEY_BOUND_SERVICE_CRASH_RESTART_DURATION);
        pw.print("=");
        pw.println(this.BOUND_SERVICE_CRASH_RESTART_DURATION);
        pw.print("  ");
        pw.print(KEY_BOUND_SERVICE_CRASH_MAX_RETRY);
        pw.print("=");
        pw.println(this.BOUND_SERVICE_MAX_CRASH_RETRY);
        pw.print("  ");
        pw.print(KEY_PROCESS_START_ASYNC);
        pw.print("=");
        pw.println(this.FLAG_PROCESS_START_ASYNC);
        pw.print("  ");
        pw.print(KEY_MEMORY_INFO_THROTTLE_TIME);
        pw.print("=");
        pw.println(this.MEMORY_INFO_THROTTLE_TIME);
        pw.print("  ");
        pw.print(KEY_TOP_TO_FGS_GRACE_DURATION);
        pw.print("=");
        pw.println(this.TOP_TO_FGS_GRACE_DURATION);
        pw.print("  ");
        pw.print(KEY_TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION);
        pw.print("=");
        pw.println(this.TOP_TO_ALMOST_PERCEPTIBLE_GRACE_DURATION);
        pw.print("  ");
        pw.print(KEY_MIN_CRASH_INTERVAL);
        pw.print("=");
        pw.println(MIN_CRASH_INTERVAL);
        pw.print("  ");
        pw.print(KEY_PROCESS_CRASH_COUNT_RESET_INTERVAL);
        pw.print("=");
        pw.println(PROCESS_CRASH_COUNT_RESET_INTERVAL);
        pw.print("  ");
        pw.print(KEY_PROCESS_CRASH_COUNT_LIMIT);
        pw.print("=");
        pw.println(PROCESS_CRASH_COUNT_LIMIT);
        pw.print("  ");
        pw.print(KEY_IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES);
        pw.print("=");
        pw.println(java.util.Arrays.toString(this.IMPERCEPTIBLE_KILL_EXEMPT_PROC_STATES.toArray()));
        pw.print("  ");
        pw.print(KEY_IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES);
        pw.print("=");
        pw.println(java.util.Arrays.toString(this.IMPERCEPTIBLE_KILL_EXEMPT_PACKAGES.toArray()));
        pw.print("  ");
        pw.print(KEY_MIN_ASSOC_LOG_DURATION);
        pw.print("=");
        pw.println(MIN_ASSOC_LOG_DURATION);
        pw.print("  ");
        pw.print(KEY_BINDER_HEAVY_HITTER_WATCHER_ENABLED);
        pw.print("=");
        pw.println(BINDER_HEAVY_HITTER_WATCHER_ENABLED);
        pw.print("  ");
        pw.print(KEY_BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE);
        pw.print("=");
        pw.println(BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE);
        pw.print("  ");
        pw.print(KEY_BINDER_HEAVY_HITTER_WATCHER_THRESHOLD);
        pw.print("=");
        pw.println(BINDER_HEAVY_HITTER_WATCHER_THRESHOLD);
        pw.print("  ");
        pw.print(KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED);
        pw.print("=");
        pw.println(BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED);
        pw.print("  ");
        pw.print(KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE);
        pw.print("=");
        pw.println(BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE);
        pw.print("  ");
        pw.print(KEY_BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD);
        pw.print("=");
        pw.println(BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD);
        pw.print("  ");
        pw.print(KEY_MAX_PHANTOM_PROCESSES);
        pw.print("=");
        pw.println(this.MAX_PHANTOM_PROCESSES);
        pw.print("  ");
        pw.print(KEY_BOOT_TIME_TEMP_ALLOWLIST_DURATION);
        pw.print("=");
        pw.println(this.mBootTimeTempAllowlistDuration);
        pw.print("  ");
        pw.print(KEY_FG_TO_BG_FGS_GRACE_DURATION);
        pw.print("=");
        pw.println(this.mFgToBgFgsGraceDuration);
        pw.print("  ");
        pw.print(KEY_FGS_START_FOREGROUND_TIMEOUT);
        pw.print("=");
        pw.println(this.mFgsStartForegroundTimeoutMs);
        pw.print("  ");
        pw.print(KEY_DEFAULT_BACKGROUND_ACTIVITY_STARTS_ENABLED);
        pw.print("=");
        pw.println(this.mFlagBackgroundActivityStartsEnabled);
        pw.print("  ");
        pw.print(KEY_DEFAULT_BACKGROUND_FGS_STARTS_RESTRICTION_ENABLED);
        pw.print("=");
        pw.println(this.mFlagBackgroundFgsStartRestrictionEnabled);
        pw.print("  ");
        pw.print(KEY_DEFAULT_FGS_STARTS_RESTRICTION_ENABLED);
        pw.print("=");
        pw.println(this.mFlagFgsStartRestrictionEnabled);
        pw.print("  ");
        pw.print(KEY_DEFAULT_FGS_STARTS_RESTRICTION_NOTIFICATION_ENABLED);
        pw.print("=");
        pw.println(this.mFgsStartRestrictionNotificationEnabled);
        pw.print("  ");
        pw.print(KEY_DEFAULT_FGS_STARTS_RESTRICTION_CHECK_CALLER_TARGET_SDK);
        pw.print("=");
        pw.println(this.mFgsStartRestrictionCheckCallerTargetSdk);
        pw.print("  ");
        pw.print(KEY_FGS_ATOM_SAMPLE_RATE);
        pw.print("=");
        pw.println(this.mFgsAtomSampleRate);
        pw.print("  ");
        pw.print(KEY_FGS_START_ALLOWED_LOG_SAMPLE_RATE);
        pw.print("=");
        pw.println(this.mFgsStartAllowedLogSampleRate);
        pw.print("  ");
        pw.print(KEY_FGS_START_DENIED_LOG_SAMPLE_RATE);
        pw.print("=");
        pw.println(this.mFgsStartDeniedLogSampleRate);
        pw.print("  ");
        pw.print(KEY_PUSH_MESSAGING_OVER_QUOTA_BEHAVIOR);
        pw.print("=");
        pw.println(this.mPushMessagingOverQuotaBehavior);
        pw.print("  ");
        pw.print(KEY_FGS_ALLOW_OPT_OUT);
        pw.print("=");
        pw.println(this.mFgsAllowOptOut);
        pw.print("  ");
        pw.print(KEY_ENABLE_COMPONENT_ALIAS);
        pw.print("=");
        pw.println(this.mEnableComponentAlias);
        pw.print("  ");
        pw.print(KEY_COMPONENT_ALIAS_OVERRIDES);
        pw.print("=");
        pw.println(this.mComponentAliasOverrides);
        pw.print("  ");
        pw.print(KEY_DEFER_BOOT_COMPLETED_BROADCAST);
        pw.print("=");
        pw.println(this.mDeferBootCompletedBroadcast);
        pw.print("  ");
        pw.print(KEY_PRIORITIZE_ALARM_BROADCASTS);
        pw.print("=");
        pw.println(this.mPrioritizeAlarmBroadcasts);
        pw.print("  ");
        pw.print(KEY_NO_KILL_CACHED_PROCESSES_UNTIL_BOOT_COMPLETED);
        pw.print("=");
        pw.println(this.mNoKillCachedProcessesUntilBootCompleted);
        pw.print("  ");
        pw.print(KEY_NO_KILL_CACHED_PROCESSES_POST_BOOT_COMPLETED_DURATION_MILLIS);
        pw.print("=");
        pw.println(this.mNoKillCachedProcessesPostBootCompletedDurationMillis);
        pw.print("  ");
        pw.print(KEY_MAX_EMPTY_TIME_MILLIS);
        pw.print("=");
        pw.println(this.mMaxEmptyTimeMillis);
        pw.print("  ");
        pw.print(KEY_SERVICE_START_FOREGROUND_TIMEOUT_MS);
        pw.print("=");
        pw.println(this.mServiceStartForegroundTimeoutMs);
        pw.print("  ");
        pw.print(KEY_SERVICE_START_FOREGROUND_ANR_DELAY_MS);
        pw.print("=");
        pw.println(this.mServiceStartForegroundAnrDelayMs);
        pw.print("  ");
        pw.print(KEY_SERVICE_BIND_ALMOST_PERCEPTIBLE_TIMEOUT_MS);
        pw.print("=");
        pw.println(this.mServiceBindAlmostPerceptibleTimeoutMs);
        pw.print("  ");
        pw.print(KEY_NETWORK_ACCESS_TIMEOUT_MS);
        pw.print("=");
        pw.println(this.mNetworkAccessTimeoutMs);
        pw.print("  ");
        pw.print(KEY_MAX_SERVICE_CONNECTIONS_PER_PROCESS);
        pw.print("=");
        pw.println(this.mMaxServiceConnectionsPerProcess);
        pw.print("  ");
        pw.print(KEY_PROACTIVE_KILLS_ENABLED);
        pw.print("=");
        pw.println(PROACTIVE_KILLS_ENABLED);
        pw.print("  ");
        pw.print(KEY_LOW_SWAP_THRESHOLD_PERCENT);
        pw.print("=");
        pw.println(LOW_SWAP_THRESHOLD_PERCENT);
        pw.print("  ");
        pw.print(KEY_DEFERRED_FGS_NOTIFICATIONS_ENABLED);
        pw.print("=");
        pw.println(this.mFlagFgsNotificationDeferralEnabled);
        pw.print("  ");
        pw.print(KEY_DEFERRED_FGS_NOTIFICATIONS_API_GATED);
        pw.print("=");
        pw.println(this.mFlagFgsNotificationDeferralApiGated);
        pw.print("  ");
        pw.print(KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL);
        pw.print("=");
        pw.println(this.mFgsNotificationDeferralInterval);
        pw.print("  ");
        pw.print(KEY_DEFERRED_FGS_NOTIFICATION_INTERVAL_FOR_SHORT);
        pw.print("=");
        pw.println(this.mFgsNotificationDeferralIntervalForShort);
        pw.print("  ");
        pw.print(KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME);
        pw.print("=");
        pw.println(this.mFgsNotificationDeferralExclusionTime);
        pw.print("  ");
        pw.print(KEY_DEFERRED_FGS_NOTIFICATION_EXCLUSION_TIME_FOR_SHORT);
        pw.print("=");
        pw.println(this.mFgsNotificationDeferralExclusionTimeForShort);
        pw.print("  ");
        pw.print(KEY_SYSTEM_EXEMPT_POWER_RESTRICTIONS_ENABLED);
        pw.print("=");
        pw.println(this.mFlagSystemExemptPowerRestrictionsEnabled);
        pw.print("  ");
        pw.print(KEY_SHORT_FGS_TIMEOUT_DURATION);
        pw.print("=");
        pw.println(this.mShortFgsTimeoutDuration);
        pw.print("  ");
        pw.print(KEY_SHORT_FGS_PROC_STATE_EXTRA_WAIT_DURATION);
        pw.print("=");
        pw.println(this.mShortFgsProcStateExtraWaitDuration);
        pw.print("  ");
        pw.print(KEY_SHORT_FGS_ANR_EXTRA_WAIT_DURATION);
        pw.print("=");
        pw.println(this.mShortFgsAnrExtraWaitDuration);
        pw.print("  ");
        pw.print(KEY_MEDIA_PROCESSING_FGS_TIMEOUT_DURATION);
        pw.print("=");
        pw.println(this.mMediaProcessingFgsTimeoutDuration);
        pw.print("  ");
        pw.print(KEY_DATA_SYNC_FGS_TIMEOUT_DURATION);
        pw.print("=");
        pw.println(this.mDataSyncFgsTimeoutDuration);
        pw.print("  ");
        pw.print(KEY_FGS_CRASH_EXTRA_WAIT_DURATION);
        pw.print("=");
        pw.println(this.mFgsCrashExtraWaitDuration);
        pw.print("  ");
        pw.print(KEY_USE_TIERED_CACHED_ADJ);
        pw.print("=");
        pw.println(this.USE_TIERED_CACHED_ADJ);
        pw.print("  ");
        pw.print(KEY_TIERED_CACHED_ADJ_DECAY_TIME);
        pw.print("=");
        pw.println(this.TIERED_CACHED_ADJ_DECAY_TIME);
        pw.print("  ");
        pw.print(KEY_ENABLE_NEW_OOMADJ);
        pw.print("=");
        pw.println(this.ENABLE_NEW_OOMADJ);
        pw.print("  ");
        pw.print(KEY_DISABLE_APP_PROFILER_PSS_PROFILING);
        pw.print("=");
        pw.println(this.APP_PROFILER_PSS_PROFILING_DISABLED);
        pw.print("  ");
        pw.print(KEY_PSS_TO_RSS_THRESHOLD_MODIFIER);
        pw.print("=");
        pw.println(this.PSS_TO_RSS_THRESHOLD_MODIFIER);
        pw.print("  ");
        pw.print(KEY_MAX_PREVIOUS_TIME);
        pw.print("=");
        pw.println(MAX_PREVIOUS_TIME);
        pw.print("  ");
        pw.print(KEY_ENABLE_BATCHING_OOM_ADJ);
        pw.print("=");
        pw.println(this.ENABLE_BATCHING_OOM_ADJ);
        pw.println();
        if (this.mOverrideMaxCachedProcesses >= 0) {
            pw.print("  mOverrideMaxCachedProcesses=");
            pw.println(this.mOverrideMaxCachedProcesses);
        }
        pw.print("  mCustomizedMaxCachedProcesses=");
        pw.println(this.mCustomizedMaxCachedProcesses);
        pw.print("  CUR_MAX_CACHED_PROCESSES=");
        pw.println(this.CUR_MAX_CACHED_PROCESSES);
        pw.print("  CUR_MAX_EMPTY_PROCESSES=");
        pw.println(this.CUR_MAX_EMPTY_PROCESSES);
        pw.print("  CUR_TRIM_EMPTY_PROCESSES=");
        pw.println(this.CUR_TRIM_EMPTY_PROCESSES);
        pw.print("  CUR_TRIM_CACHED_PROCESSES=");
        pw.println(this.CUR_TRIM_CACHED_PROCESSES);
        pw.print("  OOMADJ_UPDATE_QUICK=");
        pw.println(this.OOMADJ_UPDATE_QUICK);
        pw.print("  ENABLE_WAIT_FOR_FINISH_ATTACH_APPLICATION=");
        pw.println(this.mEnableWaitForFinishAttachApplication);
        pw.print("  ");
        pw.print(KEY_FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION);
        pw.print("=");
        pw.println(this.FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION);
        synchronized (this.mProcStateDebugUids) {
            pw.print("  ");
            pw.print(KEY_PROC_STATE_DEBUG_UIDS);
            pw.print("=");
            pw.println(this.mProcStateDebugUids);
            pw.print("    uid-state-delay=");
            pw.println(this.mProcStateDebugSetUidStateDelay);
            pw.print("    proc-state-delay=");
            pw.println(this.mProcStateDebugSetProcStateDelay);
        }
    }
}
