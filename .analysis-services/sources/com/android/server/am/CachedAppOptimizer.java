package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class CachedAppOptimizer {
    static final int ASYNC_RECEIVED_WHILE_FROZEN = 2;
    private static final java.lang.String ATRACE_COMPACTION_TRACK = "Compaction";
    private static final java.lang.String ATRACE_FREEZER_TRACK = "Freezer";
    static final int BINDER_ERROR_MSG = 8;
    private static final int COMPACT_ACTION_ANON_FLAG = 2;
    private static final int COMPACT_ACTION_FILE_FLAG = 1;
    static final double COMPACT_DOWNGRADE_FREE_SWAP_THRESHOLD = 0.2d;
    static final int COMPACT_NATIVE_MSG = 5;
    static final int COMPACT_PROCESS_MSG = 1;
    static final int COMPACT_SYSTEM_MSG = 2;
    static final int DEADLOCK_WATCHDOG_MSG = 7;
    static final long DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB = 8000;
    static final long DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB = 12000;
    static final long DEFAULT_COMPACT_THROTTLE_1 = 5000;
    static final long DEFAULT_COMPACT_THROTTLE_2 = 10000;
    static final long DEFAULT_COMPACT_THROTTLE_3 = 500;
    static final long DEFAULT_COMPACT_THROTTLE_4 = 10000;
    static final long DEFAULT_COMPACT_THROTTLE_5 = 600000;
    static final long DEFAULT_COMPACT_THROTTLE_6 = 600000;
    static final long DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ = 999;
    static final long DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ = 900;
    static final int DEFAULT_FREEZER_BINDER_ASYNC_THRESHOLD = 1024;
    static final boolean DEFAULT_FREEZER_BINDER_CALLBACK_ENABLED = true;
    static final long DEFAULT_FREEZER_BINDER_CALLBACK_THROTTLE = 10000;
    static final long DEFAULT_FREEZER_BINDER_DIVISOR = 4;
    static final boolean DEFAULT_FREEZER_BINDER_ENABLED = true;
    static final int DEFAULT_FREEZER_BINDER_OFFSET = 500;
    static final long DEFAULT_FREEZER_BINDER_THRESHOLD = 1000;
    static final long DEFAULT_FREEZER_DEBOUNCE_TIMEOUT = 10000;
    static final boolean DEFAULT_FREEZER_EXEMPT_INST_PKG = false;
    static final float DEFAULT_STATSD_SAMPLE_RATE = 0.1f;
    static final boolean DEFAULT_USE_COMPACTION = false;
    static final boolean DEFAULT_USE_FREEZER = true;
    static final int DO_FREEZE = 1;
    static final boolean ENABLE_SHARED_AND_CODE_COMPACT = false;
    private static final int FREEZE_BINDER_TIMEOUT_MS = 0;
    private static final int FREEZE_DEADLOCK_TIMEOUT_MS = 1000;
    static final java.lang.String KEY_COMPACT_FULL_DELTA_RSS_THROTTLE_KB = "compact_full_delta_rss_throttle_kb";
    static final java.lang.String KEY_COMPACT_FULL_RSS_THROTTLE_KB = "compact_full_rss_throttle_kb";
    static final java.lang.String KEY_COMPACT_PROC_STATE_THROTTLE = "compact_proc_state_throttle";
    static final java.lang.String KEY_COMPACT_STATSD_SAMPLE_RATE = "compact_statsd_sample_rate";
    static final java.lang.String KEY_COMPACT_THROTTLE_1 = "compact_throttle_1";
    static final java.lang.String KEY_COMPACT_THROTTLE_2 = "compact_throttle_2";
    static final java.lang.String KEY_COMPACT_THROTTLE_3 = "compact_throttle_3";
    static final java.lang.String KEY_COMPACT_THROTTLE_4 = "compact_throttle_4";
    static final java.lang.String KEY_COMPACT_THROTTLE_5 = "compact_throttle_5";
    static final java.lang.String KEY_COMPACT_THROTTLE_6 = "compact_throttle_6";
    static final java.lang.String KEY_COMPACT_THROTTLE_MAX_OOM_ADJ = "compact_throttle_max_oom_adj";
    static final java.lang.String KEY_COMPACT_THROTTLE_MIN_OOM_ADJ = "compact_throttle_min_oom_adj";
    static final java.lang.String KEY_FREEZER_BINDER_ASYNC_THRESHOLD = "freeze_binder_async_threshold";
    static final java.lang.String KEY_FREEZER_BINDER_CALLBACK_ENABLED = "freeze_binder_callback_enabled";
    static final java.lang.String KEY_FREEZER_BINDER_CALLBACK_THROTTLE = "freeze_binder_callback_throttle";
    static final java.lang.String KEY_FREEZER_BINDER_DIVISOR = "freeze_binder_divisor";
    static final java.lang.String KEY_FREEZER_BINDER_ENABLED = "freeze_binder_enabled";
    static final java.lang.String KEY_FREEZER_BINDER_OFFSET = "freeze_binder_offset";
    static final java.lang.String KEY_FREEZER_BINDER_THRESHOLD = "freeze_binder_threshold";
    static final java.lang.String KEY_FREEZER_DEBOUNCE_TIMEOUT = "freeze_debounce_timeout";
    static final java.lang.String KEY_FREEZER_EXEMPT_INST_PKG = "freeze_exempt_inst_pkg";
    static final java.lang.String KEY_FREEZER_STATSD_SAMPLE_RATE = "freeze_statsd_sample_rate";
    static final java.lang.String KEY_USE_COMPACTION = "use_compaction";
    static final java.lang.String KEY_USE_FREEZER = "use_freezer";
    static final int LAST_COMPACTED_ANY_PROCESS_STATS_HISTORY_SIZE = 20;
    static final int LAST_COMPACTION_FOR_PROCESS_STATS_SIZE = 256;
    static final int REPORT_UNFREEZE = 2;
    static final int REPORT_UNFREEZE_MSG = 4;
    private static final int RSS_ANON_INDEX = 2;
    private static final int RSS_FILE_INDEX = 1;
    private static final int RSS_SWAP_INDEX = 3;
    private static final int RSS_TOTAL_INDEX = 0;
    static final int SET_FROZEN_PROCESS_MSG = 3;
    static final int SYNC_RECEIVED_WHILE_FROZEN = 1;
    static final int TXNS_PENDING_WHILE_FROZEN = 4;
    static final int UID_FROZEN_STATE_CHANGED_MSG = 6;
    static final int UNFREEZE_REASON_ACTIVITY = 1;
    static final int UNFREEZE_REASON_ALLOWLIST = 10;
    static final int UNFREEZE_REASON_BACKUP = 22;
    static final int UNFREEZE_REASON_BINDER_TXNS = 18;
    static final int UNFREEZE_REASON_BIND_SERVICE = 4;
    static final int UNFREEZE_REASON_COMPONENT_DISABLED = 29;
    static final int UNFREEZE_REASON_EXECUTING_SERVICE = 27;
    static final int UNFREEZE_REASON_FEATURE_FLAGS = 19;
    static final int UNFREEZE_REASON_FILE_LOCKS = 16;
    static final int UNFREEZE_REASON_FILE_LOCK_CHECK_FAILURE = 17;
    static final int UNFREEZE_REASON_FINISH_RECEIVER = 2;
    static final int UNFREEZE_REASON_GET_PROVIDER = 7;
    static final int UNFREEZE_REASON_NONE = 0;
    static final int UNFREEZE_REASON_PING = 15;
    static final int UNFREEZE_REASON_PROCESS_BEGIN = 11;
    static final int UNFREEZE_REASON_PROCESS_END = 12;
    static final int UNFREEZE_REASON_REMOVE_PROVIDER = 8;
    static final int UNFREEZE_REASON_REMOVE_TASK = 24;
    static final int UNFREEZE_REASON_RESTRICTION_CHANGE = 28;
    static final int UNFREEZE_REASON_SHELL = 23;
    static final int UNFREEZE_REASON_SHORT_FGS_TIMEOUT = 20;
    static final int UNFREEZE_REASON_START_RECEIVER = 3;
    static final int UNFREEZE_REASON_START_SERVICE = 6;
    static final int UNFREEZE_REASON_STOP_SERVICE = 26;
    static final int UNFREEZE_REASON_SYSTEM_INIT = 21;
    static final int UNFREEZE_REASON_TRIM_MEMORY = 13;
    static final int UNFREEZE_REASON_UID_IDLE = 25;
    static final int UNFREEZE_REASON_UI_VISIBILITY = 9;
    static final int UNFREEZE_REASON_UNBIND_SERVICE = 5;
    private final com.android.server.am.ActivityManagerService mAm;
    public com.android.server.am.ICachedAppOptimizerExt mCachedAppOptimizerExt;
    final com.android.server.ServiceThread mCachedAppOptimizerThread;
    volatile float mCompactStatsdSampleRate;
    volatile long mCompactThrottleFullFull;
    volatile long mCompactThrottleFullSome;
    volatile long mCompactThrottleMaxOomAdj;
    volatile long mCompactThrottleMinOomAdj;
    volatile long mCompactThrottleSomeFull;
    volatile long mCompactThrottleSomeSome;
    android.os.Handler mCompactionHandler;
    java.util.LinkedList<com.android.server.am.CachedAppOptimizer.SingleCompactionStats> mCompactionStatsHistory;
    private android.os.Handler mFreezeHandler;
    volatile int mFreezerBinderAsyncThreshold;
    volatile boolean mFreezerBinderCallbackEnabled;
    private long mFreezerBinderCallbackLast;
    volatile long mFreezerBinderCallbackThrottle;
    volatile long mFreezerBinderDivisor;
    volatile boolean mFreezerBinderEnabled;
    volatile int mFreezerBinderOffset;
    volatile long mFreezerBinderThreshold;
    volatile long mFreezerDebounceTimeout;
    private int mFreezerDisableCount;
    volatile boolean mFreezerExemptInstPkg;
    public final java.lang.Object mFreezerLock;
    private boolean mFreezerOverride;
    volatile float mFreezerStatsdSampleRate;
    private final android.util.SparseArray<com.android.server.am.ProcessRecord> mFrozenProcesses;
    volatile long mFullAnonRssThrottleKb;
    volatile long mFullDeltaRssThrottleKb;
    java.util.LinkedHashMap<java.lang.Integer, com.android.server.am.CachedAppOptimizer.SingleCompactionStats> mLastCompactionStats;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnFlagsChangedListener;
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mOnNativeBootFlagsChangedListener;
    private final java.util.ArrayList<com.android.server.am.ProcessRecord> mPendingCompactionProcesses;
    private final java.util.LinkedHashMap<java.lang.String, com.android.server.am.CachedAppOptimizer.AggregatedProcessCompactionStats> mPerProcessCompactStats;
    private final java.util.EnumMap<com.android.server.am.CachedAppOptimizer.CompactSource, com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats> mPerSourceCompactStats;
    final java.lang.Object mPhenotypeFlagLock;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.internal.os.ProcLocksReader mProcLocksReader;
    final java.util.Set<java.lang.Integer> mProcStateThrottle;
    private final com.android.server.am.CachedAppOptimizer.ProcessDependencies mProcessDependencies;
    private final java.util.Random mRandom;
    private final com.android.server.am.CachedAppOptimizer.SettingsContentObserver mSettingsObserver;
    private long mSystemCompactionsPerformed;
    private long mSystemTotalMemFreed;
    private com.android.server.am.CachedAppOptimizer.PropertyChangedCallbackForTest mTestCallback;
    private long mTotalCompactionDowngrades;
    private java.util.EnumMap<com.android.server.am.CachedAppOptimizer.CancelCompactReason, java.lang.Integer> mTotalCompactionsCancelled;
    private volatile boolean mUseCompaction;
    private volatile boolean mUseFreezer;
    private com.android.server.am.CachedAppOptimizer.CachedAppOptimizerWrapper mWrapper;
    static final java.lang.String DEFAULT_COMPACT_PROC_STATE_THROTTLE = java.lang.String.valueOf(11);
    static final android.net.Uri CACHED_APP_FREEZER_ENABLED_URI = android.provider.Settings.Global.getUriFor("cached_apps_freezer");

    public enum CancelCompactReason {
        SCREEN_ON,
        OOM_IMPROVEMENT
    }

    public enum CompactProfile {
        NONE,
        SOME,
        ANON,
        FULL
    }

    public enum CompactSource {
        APP,
        SHELL
    }

    interface ProcessDependencies {
        long[] getRss(int i);

        void performCompaction(com.android.server.am.CachedAppOptimizer.CompactProfile compactProfile, int i) throws java.io.IOException;
    }

    interface PropertyChangedCallbackForTest {
        void onPropertyChanged();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface UnfreezeReason {
    }

    private static native void cancelCompaction();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void compactProcess(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void compactSystem();

    public static native int freezeBinder(int i, boolean z, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getBinderFreezeInfo(int i);

    static native double getFreeSwapPercent();

    private static native java.lang.String getFreezerCheckPath();

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getMemoryFreedCompaction();

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getUsedZramMemory();

    private static native boolean isFreezerProfileValid();

    /* JADX INFO: Access modifiers changed from: private */
    public static native long threadCpuTimeNs();

    private final class SettingsContentObserver extends android.database.ContentObserver {
        SettingsContentObserver() {
            super(com.android.server.am.CachedAppOptimizer.this.mAm.mHandler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (com.android.server.am.CachedAppOptimizer.CACHED_APP_FREEZER_ENABLED_URI.equals(uri)) {
                synchronized (com.android.server.am.CachedAppOptimizer.this.mPhenotypeFlagLock) {
                    com.android.server.am.CachedAppOptimizer.this.updateUseFreezer();
                }
            }
        }
    }

    class AggregatedCompactionStats {
        public long mFullCompactPerformed;
        public long mFullCompactRequested;
        public double mMaxCompactEfficiency;
        public long mProcCompactionsMiscThrottled;
        public long mProcCompactionsNoPidThrottled;
        public long mProcCompactionsOomAdjThrottled;
        public long mProcCompactionsRSSThrottled;
        public long mProcCompactionsTimeThrottled;
        public long mSomeCompactPerformed;
        public long mSomeCompactRequested;
        public long mSumOrigAnonRss;
        public long mTotalAnonMemFreedKBs;
        public long mTotalCpuTimeMillis;
        public long mTotalDeltaAnonRssKBs;
        public long mTotalZramConsumedKBs;

        AggregatedCompactionStats() {
        }

        public long getThrottledSome() {
            return this.mSomeCompactRequested - this.mSomeCompactPerformed;
        }

        public long getThrottledFull() {
            return this.mFullCompactRequested - this.mFullCompactPerformed;
        }

        public void addMemStats(long anonRssSaved, long zramConsumed, long memFreed, long origAnonRss, long totalCpuTimeMillis) {
            double compactEfficiency = memFreed / origAnonRss;
            if (compactEfficiency > this.mMaxCompactEfficiency) {
                this.mMaxCompactEfficiency = compactEfficiency;
            }
            this.mTotalDeltaAnonRssKBs += anonRssSaved;
            this.mTotalZramConsumedKBs += zramConsumed;
            this.mTotalAnonMemFreedKBs += memFreed;
            this.mSumOrigAnonRss += origAnonRss;
            this.mTotalCpuTimeMillis += totalCpuTimeMillis;
        }

        @dalvik.annotation.optimization.NeverCompile
        public void dump(java.io.PrintWriter pw) {
            long totalCompactRequested = this.mSomeCompactRequested + this.mFullCompactRequested;
            long totalCompactPerformed = this.mSomeCompactPerformed + this.mFullCompactPerformed;
            pw.println("    Performed / Requested:");
            pw.println("      Some: (" + this.mSomeCompactPerformed + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mSomeCompactRequested + ")");
            pw.println("      Full: (" + this.mFullCompactPerformed + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mFullCompactRequested + ")");
            long throttledSome = getThrottledSome();
            long throttledFull = getThrottledFull();
            if (throttledSome > 0 || throttledFull > 0) {
                pw.println("    Throttled:");
                pw.println("       Some: " + throttledSome + " Full: " + throttledFull);
                pw.println("    Throttled by Type:");
                long compactionsThrottled = totalCompactRequested - totalCompactPerformed;
                long unaccountedThrottled = ((((compactionsThrottled - this.mProcCompactionsNoPidThrottled) - this.mProcCompactionsOomAdjThrottled) - this.mProcCompactionsTimeThrottled) - this.mProcCompactionsRSSThrottled) - this.mProcCompactionsMiscThrottled;
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("       NoPid: ");
                long totalCompactPerformed2 = this.mProcCompactionsNoPidThrottled;
                pw.println(sbAppend.append(totalCompactPerformed2).append(" OomAdj: ").append(this.mProcCompactionsOomAdjThrottled).append(" Time: ").append(this.mProcCompactionsTimeThrottled).append(" RSS: ").append(this.mProcCompactionsRSSThrottled).append(" Misc: ").append(this.mProcCompactionsMiscThrottled).append(" Unaccounted: ").append(unaccountedThrottled).toString());
                double compactThrottlePercentage = (compactionsThrottled / totalCompactRequested) * 100.0d;
                pw.println("    Throttle Percentage: " + compactThrottlePercentage);
            }
            if (this.mFullCompactPerformed > 0) {
                pw.println("    -----Memory Stats----");
                pw.println("    Total Delta Anon RSS (KB) : " + this.mTotalDeltaAnonRssKBs);
                pw.println("    Total Physical ZRAM Consumed (KB): " + this.mTotalZramConsumedKBs);
                pw.println("    Total Anon Memory Freed (KB): " + this.mTotalAnonMemFreedKBs);
                pw.println("    Avg Compaction Efficiency (Anon Freed/Anon RSS): " + (this.mTotalAnonMemFreedKBs / this.mSumOrigAnonRss));
                pw.println("    Max Compaction Efficiency: " + this.mMaxCompactEfficiency);
                pw.println("    Avg Compression Ratio (1 - ZRAM Consumed/DeltaAnonRSS): " + (1.0d - (this.mTotalZramConsumedKBs / this.mTotalDeltaAnonRssKBs)));
                long avgKBsPerProcCompact = this.mFullCompactPerformed > 0 ? this.mTotalAnonMemFreedKBs / this.mFullCompactPerformed : 0L;
                pw.println("    Avg Anon Mem Freed/Compaction (KB) : " + avgKBsPerProcCompact);
                double compactionCost = this.mTotalCpuTimeMillis / (this.mTotalAnonMemFreedKBs / 1024.0d);
                pw.println("    Compaction Cost (ms/MB): " + compactionCost);
            }
        }
    }

    class AggregatedProcessCompactionStats extends com.android.server.am.CachedAppOptimizer.AggregatedCompactionStats {
        public final java.lang.String processName;

        AggregatedProcessCompactionStats(java.lang.String processName) {
            super();
            this.processName = processName;
        }
    }

    class AggregatedSourceCompactionStats extends com.android.server.am.CachedAppOptimizer.AggregatedCompactionStats {
        public final com.android.server.am.CachedAppOptimizer.CompactSource sourceType;

        AggregatedSourceCompactionStats(com.android.server.am.CachedAppOptimizer.CompactSource sourceType) {
            super();
            this.sourceType = sourceType;
        }
    }

    public CachedAppOptimizer(com.android.server.am.ActivityManagerService am) {
        this(am, null, new com.android.server.am.CachedAppOptimizer.DefaultProcessDependencies());
    }

    CachedAppOptimizer(com.android.server.am.ActivityManagerService am, com.android.server.am.CachedAppOptimizer.PropertyChangedCallbackForTest callback, com.android.server.am.CachedAppOptimizer.ProcessDependencies processDependencies) {
        this.mPendingCompactionProcesses = new java.util.ArrayList<>();
        this.mFrozenProcesses = new android.util.SparseArray<>();
        this.mFreezerLock = new java.lang.Object();
        this.mCachedAppOptimizerExt = (com.android.server.am.ICachedAppOptimizerExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.ICachedAppOptimizerExt.class).create();
        this.mOnFlagsChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CachedAppOptimizer.1
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                synchronized (com.android.server.am.CachedAppOptimizer.this.mPhenotypeFlagLock) {
                    for (java.lang.String name : properties.getKeyset()) {
                        if (com.android.server.am.CachedAppOptimizer.KEY_USE_COMPACTION.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateUseCompaction();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_1.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_2.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_3.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_4.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_5.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_6.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateCompactionThrottles();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_STATSD_SAMPLE_RATE.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateCompactStatsdSampleRate();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_FREEZER_STATSD_SAMPLE_RATE.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateFreezerStatsdSampleRate();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_FULL_RSS_THROTTLE_KB.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateFullRssThrottle();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_FULL_DELTA_RSS_THROTTLE_KB.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateFullDeltaRssThrottle();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_PROC_STATE_THROTTLE.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateProcStateThrottle();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_MIN_OOM_ADJ.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateMinOomAdjThrottle();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_COMPACT_THROTTLE_MAX_OOM_ADJ.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateMaxOomAdjThrottle();
                        }
                    }
                }
                if (com.android.server.am.CachedAppOptimizer.this.mTestCallback != null) {
                    com.android.server.am.CachedAppOptimizer.this.mTestCallback.onPropertyChanged();
                }
            }
        };
        this.mOnNativeBootFlagsChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CachedAppOptimizer.2
            public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                synchronized (com.android.server.am.CachedAppOptimizer.this.mPhenotypeFlagLock) {
                    for (java.lang.String name : properties.getKeyset()) {
                        if (com.android.server.am.CachedAppOptimizer.KEY_FREEZER_DEBOUNCE_TIMEOUT.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateFreezerDebounceTimeout();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_FREEZER_EXEMPT_INST_PKG.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateFreezerExemptInstPkg();
                        } else if (com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_ENABLED.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_DIVISOR.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_THRESHOLD.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_OFFSET.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_CALLBACK_ENABLED.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_CALLBACK_THROTTLE.equals(name) || com.android.server.am.CachedAppOptimizer.KEY_FREEZER_BINDER_ASYNC_THRESHOLD.equals(name)) {
                            com.android.server.am.CachedAppOptimizer.this.updateFreezerBinderState();
                        }
                    }
                }
                if (com.android.server.am.CachedAppOptimizer.this.mTestCallback != null) {
                    com.android.server.am.CachedAppOptimizer.this.mTestCallback.onPropertyChanged();
                }
            }
        };
        this.mPhenotypeFlagLock = new java.lang.Object();
        this.mCompactThrottleSomeSome = DEFAULT_COMPACT_THROTTLE_1;
        this.mCompactThrottleSomeFull = 10000L;
        this.mCompactThrottleFullSome = 500L;
        this.mCompactThrottleFullFull = 10000L;
        this.mCompactThrottleMinOomAdj = DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ;
        this.mCompactThrottleMaxOomAdj = DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ;
        this.mUseCompaction = false;
        this.mUseFreezer = false;
        this.mFreezerDisableCount = 1;
        this.mRandom = new java.util.Random();
        this.mCompactStatsdSampleRate = DEFAULT_STATSD_SAMPLE_RATE;
        this.mFreezerStatsdSampleRate = DEFAULT_STATSD_SAMPLE_RATE;
        this.mFullAnonRssThrottleKb = DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB;
        this.mFullDeltaRssThrottleKb = DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB;
        this.mFreezerBinderEnabled = true;
        this.mFreezerBinderDivisor = 4L;
        this.mFreezerBinderOffset = 500;
        this.mFreezerBinderThreshold = 1000L;
        this.mFreezerBinderCallbackEnabled = true;
        this.mFreezerBinderCallbackThrottle = 10000L;
        this.mFreezerBinderAsyncThreshold = 1024;
        this.mFreezerOverride = false;
        this.mFreezerBinderCallbackLast = -1L;
        this.mFreezerDebounceTimeout = 10000L;
        this.mFreezerExemptInstPkg = false;
        this.mLastCompactionStats = new java.util.LinkedHashMap<java.lang.Integer, com.android.server.am.CachedAppOptimizer.SingleCompactionStats>() { // from class: com.android.server.am.CachedAppOptimizer.3
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(java.util.Map.Entry<java.lang.Integer, com.android.server.am.CachedAppOptimizer.SingleCompactionStats> entry) {
                return size() > 256;
            }
        };
        this.mCompactionStatsHistory = new java.util.LinkedList<com.android.server.am.CachedAppOptimizer.SingleCompactionStats>() { // from class: com.android.server.am.CachedAppOptimizer.4
            @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
            public boolean add(com.android.server.am.CachedAppOptimizer.SingleCompactionStats e) {
                if (size() >= 20) {
                    remove();
                }
                return super.add(e);
            }
        };
        this.mPerProcessCompactStats = new java.util.LinkedHashMap<>(256);
        this.mPerSourceCompactStats = new java.util.EnumMap<>(com.android.server.am.CachedAppOptimizer.CompactSource.class);
        this.mTotalCompactionsCancelled = new java.util.EnumMap<>(com.android.server.am.CachedAppOptimizer.CancelCompactReason.class);
        this.mWrapper = new com.android.server.am.CachedAppOptimizer.CachedAppOptimizerWrapper();
        this.mAm = am;
        this.mProcLock = am.mProcLock;
        this.mCachedAppOptimizerThread = new com.android.server.ServiceThread("CachedAppOptimizerThread", 2, true);
        this.mProcStateThrottle = new java.util.HashSet();
        this.mProcessDependencies = processDependencies;
        this.mTestCallback = callback;
        this.mSettingsObserver = new com.android.server.am.CachedAppOptimizer.SettingsContentObserver();
        this.mProcLocksReader = new com.android.internal.os.ProcLocksReader();
    }

    public void init() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager", android.app.ActivityThread.currentApplication().getMainExecutor(), this.mOnFlagsChangedListener);
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager_native_boot", android.app.ActivityThread.currentApplication().getMainExecutor(), this.mOnNativeBootFlagsChangedListener);
        this.mAm.mContext.getContentResolver().registerContentObserver(CACHED_APP_FREEZER_ENABLED_URI, false, this.mSettingsObserver);
        synchronized (this.mPhenotypeFlagLock) {
            updateUseCompaction();
            updateCompactionThrottles();
            updateCompactStatsdSampleRate();
            updateFreezerStatsdSampleRate();
            updateFullRssThrottle();
            updateFullDeltaRssThrottle();
            updateProcStateThrottle();
            updateUseFreezer();
            updateMinOomAdjThrottle();
            updateMaxOomAdjThrottle();
        }
    }

    public boolean useCompaction() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseCompaction;
        }
        return z;
    }

    public boolean useFreezer() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseFreezer;
        }
        return z;
    }

    public boolean freezerExemptInstPkg() {
        boolean z;
        synchronized (this.mPhenotypeFlagLock) {
            z = this.mUseFreezer && this.mFreezerExemptInstPkg;
        }
        return z;
    }

    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw) {
        double avgKBsPerSystemCompact;
        pw.println("CachedAppOptimizer settings");
        synchronized (this.mPhenotypeFlagLock) {
            pw.println("  use_compaction=" + this.mUseCompaction);
            pw.println("  compact_throttle_1=" + this.mCompactThrottleSomeSome);
            pw.println("  compact_throttle_2=" + this.mCompactThrottleSomeFull);
            pw.println("  compact_throttle_3=" + this.mCompactThrottleFullSome);
            pw.println("  compact_throttle_4=" + this.mCompactThrottleFullFull);
            pw.println("  compact_throttle_min_oom_adj=" + this.mCompactThrottleMinOomAdj);
            pw.println("  compact_throttle_max_oom_adj=" + this.mCompactThrottleMaxOomAdj);
            pw.println("  compact_statsd_sample_rate=" + this.mCompactStatsdSampleRate);
            pw.println("  compact_full_rss_throttle_kb=" + this.mFullAnonRssThrottleKb);
            pw.println("  compact_full_delta_rss_throttle_kb=" + this.mFullDeltaRssThrottleKb);
            pw.println("  compact_proc_state_throttle=" + java.util.Arrays.toString(this.mProcStateThrottle.toArray(new java.lang.Integer[0])));
            pw.println(" Per-Process Compaction Stats");
            long totalCompactPerformedSome = 0;
            long totalCompactPerformedFull = 0;
            for (com.android.server.am.CachedAppOptimizer.AggregatedProcessCompactionStats stats : this.mPerProcessCompactStats.values()) {
                pw.println("-----" + stats.processName + "-----");
                totalCompactPerformedSome += stats.mSomeCompactPerformed;
                totalCompactPerformedFull += stats.mFullCompactPerformed;
                stats.dump(pw);
                pw.println();
            }
            pw.println();
            pw.println(" Per-Source Compaction Stats");
            for (com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats stats2 : this.mPerSourceCompactStats.values()) {
                pw.println("-----" + stats2.sourceType + "-----");
                stats2.dump(pw);
                pw.println();
            }
            pw.println();
            pw.println("Total Compactions Performed by profile: " + totalCompactPerformedSome + " some, " + totalCompactPerformedFull + " full");
            pw.println("Total compactions downgraded: " + this.mTotalCompactionDowngrades);
            pw.println("Total compactions cancelled by reason: ");
            for (K reason : this.mTotalCompactionsCancelled.keySet()) {
                pw.println("    " + reason + ": " + this.mTotalCompactionsCancelled.get(reason));
            }
            pw.println();
            pw.println(" System Compaction Memory Stats");
            pw.println("    Compactions Performed: " + this.mSystemCompactionsPerformed);
            pw.println("    Total Memory Freed (KB): " + this.mSystemTotalMemFreed);
            if (this.mSystemCompactionsPerformed > 0) {
                avgKBsPerSystemCompact = this.mSystemTotalMemFreed / this.mSystemCompactionsPerformed;
            } else {
                avgKBsPerSystemCompact = 0.0d;
            }
            pw.println("    Avg Mem Freed per Compact (KB): " + avgKBsPerSystemCompact);
            pw.println();
            pw.println("  Tracking last compaction stats for " + this.mLastCompactionStats.size() + " processes.");
            pw.println("Last Compaction per process stats:");
            pw.println("    (ProcessName,Source,DeltaAnonRssKBs,ZramConsumedKBs,AnonMemFreedKBs,CompactEfficiency,CompactCost(ms/MB),procState,oomAdj,oomAdjReason)");
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.am.CachedAppOptimizer.SingleCompactionStats> entry : this.mLastCompactionStats.entrySet()) {
                com.android.server.am.CachedAppOptimizer.SingleCompactionStats stats3 = entry.getValue();
                stats3.dump(pw);
            }
            pw.println();
            pw.println("Last 20 Compactions Stats:");
            pw.println("    (ProcessName,Source,DeltaAnonRssKBs,ZramConsumedKBs,AnonMemFreedKBs,CompactEfficiency,CompactCost(ms/MB),procState,oomAdj,oomAdjReason)");
            for (com.android.server.am.CachedAppOptimizer.SingleCompactionStats stats4 : this.mCompactionStatsHistory) {
                stats4.dump(pw);
            }
            pw.println();
            pw.println("  use_freezer=" + this.mUseFreezer);
            pw.println("  freeze_statsd_sample_rate=" + this.mFreezerStatsdSampleRate);
            pw.println("  freeze_debounce_timeout=" + this.mFreezerDebounceTimeout);
            pw.println("  freeze_exempt_inst_pkg=" + this.mFreezerExemptInstPkg);
            pw.println("  freeze_binder_enabled=" + this.mFreezerBinderEnabled);
            pw.println("  freeze_binder_threshold=" + this.mFreezerBinderThreshold);
            pw.println("  freeze_binder_divisor=" + this.mFreezerBinderDivisor);
            pw.println("  freeze_binder_offset=" + this.mFreezerBinderOffset);
            pw.println("  freeze_binder_callback_enabled=" + this.mFreezerBinderCallbackEnabled);
            pw.println("  freeze_binder_callback_throttle=" + this.mFreezerBinderCallbackThrottle);
            pw.println("  freeze_binder_async_threshold=" + this.mFreezerBinderAsyncThreshold);
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    int size = this.mFrozenProcesses.size();
                    pw.println("  Apps frozen: " + size);
                    for (int i = 0; i < size; i++) {
                        com.android.server.am.ProcessRecord app = this.mFrozenProcesses.valueAt(i);
                        pw.println("    " + app.mOptRecord.getFreezeUnfreezeTime() + ": " + app.getPid() + " " + app.processName + (app.mOptRecord.isFreezeSticky() ? " (sticky)" : ""));
                    }
                    if (!this.mPendingCompactionProcesses.isEmpty()) {
                        pw.println("  Pending compactions:");
                        int size2 = this.mPendingCompactionProcesses.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            com.android.server.am.ProcessRecord app2 = this.mPendingCompactionProcesses.get(i2);
                            pw.println("    pid: " + app2.getPid() + ". name: " + app2.processName + ". hasPendingCompact: " + app2.mOptRecord.hasPendingCompact());
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    boolean compactApp(com.android.server.am.ProcessRecord app, com.android.server.am.CachedAppOptimizer.CompactProfile compactProfile, com.android.server.am.CachedAppOptimizer.CompactSource source, boolean force) {
        app.mOptRecord.setReqCompactSource(source);
        app.mOptRecord.setReqCompactProfile(compactProfile);
        com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats perSourceStats = getPerSourceAggregatedCompactStat(source);
        com.android.server.am.CachedAppOptimizer.AggregatedCompactionStats perProcStats = getPerProcessAggregatedCompactStat(app.processName);
        switch (compactProfile.ordinal()) {
            case 1:
                perProcStats.mSomeCompactRequested++;
                perSourceStats.mSomeCompactRequested++;
                break;
            case 2:
            default:
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unimplemented compaction type, consider adding it.");
                return false;
            case 3:
                perProcStats.mFullCompactRequested++;
                perSourceStats.mFullCompactRequested++;
                break;
        }
        if (!app.mOptRecord.hasPendingCompact()) {
            java.lang.String processName = app.processName != null ? app.processName : "";
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "compactApp " + app.mOptRecord.getReqCompactSource().name() + " " + app.mOptRecord.getReqCompactProfile().name() + " " + processName);
            }
            app.mOptRecord.setHasPendingCompact(true);
            app.mOptRecord.setForceCompact(force);
            this.mPendingCompactionProcesses.add(app);
            this.mCompactionHandler.sendMessage(this.mCompactionHandler.obtainMessage(1, app.mState.getCurAdj(), app.mState.getSetProcState()));
            return true;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, " compactApp Skipped for " + app.processName + " pendingCompact= " + app.mOptRecord.hasPendingCompact() + ". Requested compact profile: " + app.mOptRecord.getReqCompactProfile().name() + ". Compact source " + app.mOptRecord.getReqCompactSource().name());
        }
        return false;
    }

    void compactNative(com.android.server.am.CachedAppOptimizer.CompactProfile compactProfile, int pid) {
        this.mCompactionHandler.sendMessage(this.mCompactionHandler.obtainMessage(5, pid, compactProfile.ordinal()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.am.CachedAppOptimizer.AggregatedProcessCompactionStats getPerProcessAggregatedCompactStat(java.lang.String processName) {
        if (processName == null) {
            processName = "";
        }
        com.android.server.am.CachedAppOptimizer.AggregatedProcessCompactionStats stats = this.mPerProcessCompactStats.get(processName);
        if (stats == null) {
            com.android.server.am.CachedAppOptimizer.AggregatedProcessCompactionStats stats2 = new com.android.server.am.CachedAppOptimizer.AggregatedProcessCompactionStats(processName);
            this.mPerProcessCompactStats.put(processName, stats2);
            return stats2;
        }
        return stats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats getPerSourceAggregatedCompactStat(com.android.server.am.CachedAppOptimizer.CompactSource source) {
        com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats stats = this.mPerSourceCompactStats.get(source);
        if (stats == null) {
            com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats stats2 = new com.android.server.am.CachedAppOptimizer.AggregatedSourceCompactionStats(source);
            this.mPerSourceCompactStats.put(source, stats2);
            return stats2;
        }
        return stats;
    }

    void compactAllSystem() {
        if (useCompaction()) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "compactAllSystem");
            }
            android.os.Trace.instantForTrack(64L, ATRACE_COMPACTION_TRACK, "compactAllSystem");
            this.mCompactionHandler.sendMessage(this.mCompactionHandler.obtainMessage(2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseCompaction() {
        this.mUseCompaction = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_USE_COMPACTION, false);
        if (this.mUseCompaction && this.mCompactionHandler == null) {
            if (!this.mCachedAppOptimizerThread.isAlive()) {
                this.mCachedAppOptimizerThread.start();
            }
            this.mCompactionHandler = new com.android.server.am.CachedAppOptimizer.MemCompactionHandler();
        }
        android.os.Process.setThreadGroupAndCpuset(this.mCachedAppOptimizerThread.getThreadId(), 2);
    }

    public synchronized boolean enableFreezer(final boolean enable) {
        if (!this.mUseFreezer) {
            return false;
        }
        if (enable) {
            this.mFreezerDisableCount--;
            if (this.mFreezerDisableCount > 0) {
                return true;
            }
            if (this.mFreezerDisableCount < 0) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "unbalanced call to enableFreezer, ignoring");
                this.mFreezerDisableCount = 0;
                return false;
            }
        } else {
            this.mFreezerDisableCount++;
            if (this.mFreezerDisableCount > 1) {
                return true;
            }
        }
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        try {
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            this.mFreezerOverride = enable ? false : true;
                            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "freezer override set to " + this.mFreezerOverride);
                            this.mAm.mProcessList.forEachLruProcessesLOSP(true, new java.util.function.Consumer() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda1
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    this.f$0.lambda$enableFreezer$0(enable, (com.android.server.am.ProcessRecord) obj);
                                }
                            });
                        } finally {
                            th = th;
                            while (true) {
                                try {
                                } catch (java.lang.Throwable th) {
                                }
                            }
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableFreezer$0(boolean enable, com.android.server.am.ProcessRecord process) {
        if (process == null) {
            return;
        }
        com.android.server.am.ProcessCachedOptimizerRecord opt = process.mOptRecord;
        if (enable && opt.hasFreezerOverride()) {
            freezeAppAsyncLSP(process);
            opt.setFreezerOverride(false);
        }
        if (!enable && opt.isFrozen()) {
            unfreezeAppLSP(process, 19);
            opt.setFreezerOverride(true);
        }
    }

    public static boolean isFreezerSupported() {
        boolean supported = false;
        java.io.FileReader fr = null;
        try {
            java.lang.String path = getFreezerCheckPath();
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Checking cgroup freezer: " + path);
            fr = new java.io.FileReader(path);
            char state = (char) fr.read();
            if (state == '1' || state == '0') {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Checking binder freezer ioctl");
                getBinderFreezeInfo(android.os.Process.myPid());
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Checking freezer profiles");
                supported = isFreezerProfileValid();
            } else {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unexpected value in cgroup.freeze");
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "File cgroup.freeze not present");
        } catch (java.lang.RuntimeException e2) {
            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to read freezer info");
        } catch (java.lang.Exception e3) {
            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to read cgroup.freeze: " + e3.toString());
        }
        if (fr != null) {
            try {
                fr.close();
            } catch (java.io.IOException e4) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Exception closing cgroup.freeze: " + e4.toString());
            }
        }
        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer supported: " + supported);
        return supported;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseFreezer() {
        java.lang.String configOverride = android.provider.Settings.Global.getString(this.mAm.mContext.getContentResolver(), "cached_apps_freezer");
        if (com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED.equals(configOverride)) {
            this.mUseFreezer = false;
        } else if ((com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED.equals(configOverride) || android.provider.DeviceConfig.getBoolean("activity_manager_native_boot", KEY_USE_FREEZER, true)) && this.mWrapper.isExtUseFreezeEnable()) {
            this.mUseFreezer = isFreezerSupported();
            updateFreezerDebounceTimeout();
            updateFreezerExemptInstPkg();
        } else {
            this.mUseFreezer = false;
        }
        final boolean useFreezer = this.mUseFreezer;
        this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateUseFreezer$1(useFreezer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUseFreezer$1(boolean useFreezer) {
        if (useFreezer) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer enabled");
            enableFreezer(true);
            if (!this.mCachedAppOptimizerThread.isAlive()) {
                this.mCachedAppOptimizerThread.start();
            }
            if (this.mFreezeHandler == null) {
                this.mFreezeHandler = new com.android.server.am.CachedAppOptimizer.FreezeHandler();
            }
            android.os.Process.setThreadGroupAndCpuset(this.mCachedAppOptimizerThread.getThreadId(), 2);
            return;
        }
        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer disabled");
        enableFreezer(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCompactionThrottles() {
        boolean useThrottleDefaults = false;
        java.lang.String throttleSomeSomeFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_1);
        java.lang.String throttleSomeFullFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_2);
        java.lang.String throttleFullSomeFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_3);
        java.lang.String throttleFullFullFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_4);
        java.lang.String throttleBFGSFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_5);
        java.lang.String throttlePersistentFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_6);
        java.lang.String throttleMinOomAdjFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_MIN_OOM_ADJ);
        java.lang.String throttleMaxOomAdjFlag = android.provider.DeviceConfig.getProperty("activity_manager", KEY_COMPACT_THROTTLE_MAX_OOM_ADJ);
        if (android.text.TextUtils.isEmpty(throttleSomeSomeFlag) || android.text.TextUtils.isEmpty(throttleSomeFullFlag) || android.text.TextUtils.isEmpty(throttleFullSomeFlag) || android.text.TextUtils.isEmpty(throttleFullFullFlag) || android.text.TextUtils.isEmpty(throttleBFGSFlag) || android.text.TextUtils.isEmpty(throttlePersistentFlag) || android.text.TextUtils.isEmpty(throttleMinOomAdjFlag) || android.text.TextUtils.isEmpty(throttleMaxOomAdjFlag)) {
            useThrottleDefaults = true;
        } else {
            try {
                this.mCompactThrottleSomeSome = java.lang.Integer.parseInt(throttleSomeSomeFlag);
                this.mCompactThrottleSomeFull = java.lang.Integer.parseInt(throttleSomeFullFlag);
                this.mCompactThrottleFullSome = java.lang.Integer.parseInt(throttleFullSomeFlag);
                this.mCompactThrottleFullFull = java.lang.Integer.parseInt(throttleFullFullFlag);
                this.mCompactThrottleMinOomAdj = java.lang.Long.parseLong(throttleMinOomAdjFlag);
                this.mCompactThrottleMaxOomAdj = java.lang.Long.parseLong(throttleMaxOomAdjFlag);
            } catch (java.lang.NumberFormatException e) {
                useThrottleDefaults = true;
            }
        }
        if (useThrottleDefaults) {
            this.mCompactThrottleSomeSome = DEFAULT_COMPACT_THROTTLE_1;
            this.mCompactThrottleSomeFull = 10000L;
            this.mCompactThrottleFullSome = 500L;
            this.mCompactThrottleFullFull = 10000L;
            this.mCompactThrottleMinOomAdj = DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ;
            this.mCompactThrottleMaxOomAdj = DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCompactStatsdSampleRate() {
        this.mCompactStatsdSampleRate = android.provider.DeviceConfig.getFloat("activity_manager", KEY_COMPACT_STATSD_SAMPLE_RATE, DEFAULT_STATSD_SAMPLE_RATE);
        this.mCompactStatsdSampleRate = java.lang.Math.min(1.0f, java.lang.Math.max(0.0f, this.mCompactStatsdSampleRate));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFreezerStatsdSampleRate() {
        this.mFreezerStatsdSampleRate = android.provider.DeviceConfig.getFloat("activity_manager", KEY_FREEZER_STATSD_SAMPLE_RATE, DEFAULT_STATSD_SAMPLE_RATE);
        this.mFreezerStatsdSampleRate = java.lang.Math.min(1.0f, java.lang.Math.max(0.0f, this.mFreezerStatsdSampleRate));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFullRssThrottle() {
        this.mFullAnonRssThrottleKb = android.provider.DeviceConfig.getLong("activity_manager", KEY_COMPACT_FULL_RSS_THROTTLE_KB, DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB);
        if (this.mFullAnonRssThrottleKb < 0) {
            this.mFullAnonRssThrottleKb = DEFAULT_COMPACT_FULL_RSS_THROTTLE_KB;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFullDeltaRssThrottle() {
        this.mFullDeltaRssThrottleKb = android.provider.DeviceConfig.getLong("activity_manager", KEY_COMPACT_FULL_DELTA_RSS_THROTTLE_KB, DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB);
        if (this.mFullDeltaRssThrottleKb < 0) {
            this.mFullDeltaRssThrottleKb = DEFAULT_COMPACT_FULL_DELTA_RSS_THROTTLE_KB;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProcStateThrottle() {
        java.lang.String procStateThrottleString = android.provider.DeviceConfig.getString("activity_manager", KEY_COMPACT_PROC_STATE_THROTTLE, DEFAULT_COMPACT_PROC_STATE_THROTTLE);
        if (!parseProcStateThrottle(procStateThrottleString)) {
            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to parse app compact proc state throttle \"" + procStateThrottleString + "\" falling back to default.");
            if (!parseProcStateThrottle(DEFAULT_COMPACT_PROC_STATE_THROTTLE)) {
                android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to parse default app compact proc state throttle " + DEFAULT_COMPACT_PROC_STATE_THROTTLE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMinOomAdjThrottle() {
        this.mCompactThrottleMinOomAdj = android.provider.DeviceConfig.getLong("activity_manager", KEY_COMPACT_THROTTLE_MIN_OOM_ADJ, DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ);
        if (this.mCompactThrottleMinOomAdj < DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ) {
            this.mCompactThrottleMinOomAdj = DEFAULT_COMPACT_THROTTLE_MIN_OOM_ADJ;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaxOomAdjThrottle() {
        this.mCompactThrottleMaxOomAdj = android.provider.DeviceConfig.getLong("activity_manager", KEY_COMPACT_THROTTLE_MAX_OOM_ADJ, DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ);
        if (this.mCompactThrottleMaxOomAdj > DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ) {
            this.mCompactThrottleMaxOomAdj = DEFAULT_COMPACT_THROTTLE_MAX_OOM_ADJ;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFreezerDebounceTimeout() {
        this.mFreezerDebounceTimeout = android.provider.DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_DEBOUNCE_TIMEOUT, 10000L);
        if (this.mFreezerDebounceTimeout < 0) {
            this.mFreezerDebounceTimeout = 10000L;
        }
        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer timeout set to " + this.mFreezerDebounceTimeout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFreezerExemptInstPkg() {
        this.mFreezerExemptInstPkg = android.provider.DeviceConfig.getBoolean("activity_manager_native_boot", KEY_FREEZER_EXEMPT_INST_PKG, false);
        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer exemption set to " + this.mFreezerExemptInstPkg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFreezerBinderState() {
        this.mFreezerBinderEnabled = android.provider.DeviceConfig.getBoolean("activity_manager_native_boot", KEY_FREEZER_BINDER_ENABLED, true);
        this.mFreezerBinderDivisor = android.provider.DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_BINDER_DIVISOR, 4L);
        this.mFreezerBinderOffset = android.provider.DeviceConfig.getInt("activity_manager_native_boot", KEY_FREEZER_BINDER_OFFSET, 500);
        this.mFreezerBinderThreshold = android.provider.DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_BINDER_THRESHOLD, 1000L);
        this.mFreezerBinderCallbackEnabled = android.provider.DeviceConfig.getBoolean("activity_manager_native_boot", KEY_FREEZER_BINDER_CALLBACK_ENABLED, true);
        this.mFreezerBinderCallbackThrottle = android.provider.DeviceConfig.getLong("activity_manager_native_boot", KEY_FREEZER_BINDER_CALLBACK_THROTTLE, 10000L);
        this.mFreezerBinderAsyncThreshold = android.provider.DeviceConfig.getInt("activity_manager_native_boot", KEY_FREEZER_BINDER_ASYNC_THRESHOLD, 1024);
        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer binder state set to enabled=" + this.mFreezerBinderEnabled + ", divisor=" + this.mFreezerBinderDivisor + ", offset=" + this.mFreezerBinderOffset + ", threshold=" + this.mFreezerBinderThreshold + ", callback enabled=" + this.mFreezerBinderCallbackEnabled + ", callback throttle=" + this.mFreezerBinderCallbackThrottle + ", async threshold=" + this.mFreezerBinderAsyncThreshold);
    }

    private boolean parseProcStateThrottle(java.lang.String procStateThrottleString) {
        java.lang.String[] procStates = android.text.TextUtils.split(procStateThrottleString, ",");
        this.mProcStateThrottle.clear();
        for (java.lang.String procState : procStates) {
            try {
                this.mProcStateThrottle.add(java.lang.Integer.valueOf(java.lang.Integer.parseInt(procState)));
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Failed to parse default app compaction proc state: " + procState);
                return false;
            }
        }
        return true;
    }

    private long updateEarliestFreezableTime(com.android.server.am.ProcessRecord app, long delayMillis) {
        long now = android.os.SystemClock.uptimeMillis();
        app.mOptRecord.setEarliestFreezableTime(java.lang.Math.max(app.mOptRecord.getEarliestFreezableTime(), now + delayMillis));
        return app.mOptRecord.getEarliestFreezableTime() - now;
    }

    void unfreezeTemporarily(com.android.server.am.ProcessRecord app, int reason) {
        unfreezeTemporarily(app, reason, this.mFreezerDebounceTimeout);
    }

    void unfreezeTemporarily(com.android.server.am.ProcessRecord app, int reason, long delayMillis) {
        if (this.mUseFreezer) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    long delay = updateEarliestFreezableTime(app, delayMillis);
                    if (app.mOptRecord.isFrozen() || app.mOptRecord.isPendingFreeze()) {
                        unfreezeAppLSP(app, reason);
                        freezeAppAsyncLSP(app, delay);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    void freezeAppAsyncLSP(com.android.server.am.ProcessRecord app) {
        freezeAppAsyncLSP(app, updateEarliestFreezableTime(app, this.mFreezerDebounceTimeout));
    }

    void forceFreezeAppAsyncLSP(com.android.server.am.ProcessRecord app) {
        freezeAppAsyncInternalLSP(app, 0L, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void freezeAppAsyncLSP(com.android.server.am.ProcessRecord app, long delayMillis) {
        freezeAppAsyncInternalLSP(app, delayMillis, false);
    }

    void freezeAppAsyncAtEarliestLSP(com.android.server.am.ProcessRecord app) {
        freezeAppAsyncLSP(app, updateEarliestFreezableTime(app, 0L));
    }

    void freezeAppAsyncImmediateLSP(com.android.server.am.ProcessRecord app) {
        freezeAppAsyncInternalLSP(app, 0L, false);
    }

    private void freezeAppAsyncInternalLSP(com.android.server.am.ProcessRecord app, long delayMillis, boolean force) {
        android.app.IApplicationThread thread;
        com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
        if (opt.isPendingFreeze()) {
            if (delayMillis == 0) {
                if (this.mCachedAppOptimizerExt != null && !this.mCachedAppOptimizerExt.checkFreezeProc(app)) {
                    return;
                }
                this.mFreezeHandler.removeMessages(3, app);
                this.mFreezeHandler.sendMessage(this.mFreezeHandler.obtainMessage(3, 1, 0, app));
                return;
            }
            return;
        }
        if (opt.isFreezeSticky() && !force) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skip freezing because unfrozen state is sticky pid=" + app.getPid() + " " + app.processName);
                return;
            }
            return;
        }
        if (app.mState.getSetAdj() >= 900 && (thread = app.getThread()) != null) {
            this.mCachedAppOptimizerExt.notifyTrimMemory(app);
            try {
                thread.scheduleTrimMemory(40);
            } catch (android.os.RemoteException e) {
            }
        }
        reportProcessFreezableChangedLocked(app);
        if (this.mCachedAppOptimizerExt != null && !this.mCachedAppOptimizerExt.checkFreezeProc(app)) {
            return;
        }
        app.mOptRecord.setLastUsedTimeout(delayMillis);
        this.mFreezeHandler.sendMessageDelayed(this.mFreezeHandler.obtainMessage(3, 1, 0, app), delayMillis);
        opt.setPendingFreeze(true);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Async freezing " + app.getPid() + " " + app.processName);
        }
    }

    void unfreezeAppInternalLSP(com.android.server.am.ProcessRecord app, int reason, boolean force) {
        boolean processFreezableChangeReported;
        java.lang.String str;
        int pid = app.getPid();
        com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
        boolean sticky = opt.isFreezeSticky();
        if (sticky && !force) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skip unfreezing because frozen state is sticky pid=" + pid + " " + app.processName);
                return;
            }
            return;
        }
        if (this.mCachedAppOptimizerExt != null && !this.mCachedAppOptimizerExt.checkUnfreezeProc(app)) {
            reportProcessFreezableChangedLocked(app);
            return;
        }
        if (!opt.isPendingFreeze()) {
            processFreezableChangeReported = false;
        } else {
            this.mFreezeHandler.removeMessages(3, app);
            opt.setPendingFreeze(false);
            reportProcessFreezableChangedLocked(app);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Cancel freezing " + pid + " " + app.processName);
            }
            processFreezableChangeReported = true;
        }
        com.android.server.am.UidRecord uidRec = app.getUidRecord();
        if (uidRec != null && uidRec.isFrozen()) {
            uidRec.setFrozen(false);
            postUidFrozenMessage(uidRec.getUid(), false);
        }
        opt.setFreezerOverride(false);
        if (pid != 0 && opt.isFrozen()) {
            boolean processKilled = false;
            try {
                int freezeInfo = getBinderFreezeInfo(pid);
                if ((freezeInfo & 1) != 0) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "pid " + pid + " " + app.processName + " received sync transactions while frozen, killing");
                    try {
                        app.killLocked("Sync transaction while in frozen state", 14, 20, true);
                        processKilled = true;
                    } catch (java.lang.Exception e) {
                        e = e;
                        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to query binder frozen info for pid " + pid + " " + app.processName + ". Killing it. Exception: " + e);
                        app.killLocked("Unable to query binder frozen stats", 14, 19, true);
                        processKilled = true;
                    }
                }
                if ((freezeInfo & 2) != 0 && com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "pid " + pid + " " + app.processName + " received async transactions while frozen");
                }
            } catch (java.lang.Exception e2) {
                e = e2;
            }
            if (processKilled) {
                return;
            }
            if (!processFreezableChangeReported) {
                reportProcessFreezableChangedLocked(app);
            }
            long freezeTime = opt.getFreezeUnfreezeTime();
            try {
                freezeBinder(pid, false, 0);
                try {
                    traceAppFreeze(app.processName, pid, reason);
                    android.os.Process.setProcessFrozen(pid, app.uid, false);
                    str = " ";
                } catch (java.lang.Exception e3) {
                    str = " ";
                }
                try {
                    opt.setFreezeUnfreezeTime(android.os.SystemClock.uptimeMillis());
                    opt.setFrozen(false);
                    this.mFrozenProcesses.delete(pid);
                } catch (java.lang.Exception e4) {
                    android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to unfreeze " + pid + str + app.processName + ". This might cause inconsistency or UI hangs.");
                }
                if (!opt.isFrozen()) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "sync unfroze " + pid + str + app.processName + " for " + reason);
                    this.mFreezeHandler.sendMessage(this.mFreezeHandler.obtainMessage(4, pid, (int) java.lang.Math.min(opt.getFreezeUnfreezeTime() - freezeTime, 2147483647L), new android.util.Pair(app, java.lang.Integer.valueOf(reason))));
                }
            } catch (java.lang.RuntimeException e5) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to unfreeze binder for " + pid + " " + app.processName + ". Killing it");
                app.killLocked("Unable to unfreeze", 14, 19, true);
            }
        }
    }

    void unfreezeAppLSP(com.android.server.am.ProcessRecord app, int reason) {
        synchronized (this.mFreezerLock) {
            unfreezeAppInternalLSP(app, reason, false);
        }
    }

    void unfreezeProcess(int pid, int reason) {
        synchronized (this.mFreezerLock) {
            com.android.server.am.ProcessRecord app = this.mFrozenProcesses.get(pid);
            if (app == null) {
                return;
            }
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "quick sync unfreeze " + pid + " for " + reason);
            try {
                freezeBinder(pid, false, 0);
                try {
                    traceAppFreeze(app.processName, pid, reason);
                    android.os.Process.setProcessFrozen(pid, app.uid, false);
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to quick unfreeze " + pid);
                }
            } catch (java.lang.RuntimeException e2) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to quick unfreeze binder for " + pid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void traceAppFreeze(java.lang.String processName, int pid, int reason) {
        android.os.Trace.instantForTrack(64L, ATRACE_FREEZER_TRACK, (reason < 0 ? "Freeze " : "Unfreeze ") + processName + ":" + pid + " " + reason);
    }

    void onCleanupApplicationRecordLocked(com.android.server.am.ProcessRecord app) {
        if (this.mUseFreezer) {
            com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
            boolean isFrozen = false;
            if (opt.isPendingFreeze()) {
                this.mFreezeHandler.removeMessages(3, app);
                opt.setPendingFreeze(false);
            }
            com.android.server.am.UidRecord uidRec = app.getUidRecord();
            if (uidRec != null) {
                if (uidRec.getNumOfProcs() > 1 && uidRec.areAllProcessesFrozen(app)) {
                    isFrozen = true;
                }
                if (isFrozen != uidRec.isFrozen()) {
                    uidRec.setFrozen(isFrozen);
                    postUidFrozenMessage(uidRec.getUid(), isFrozen);
                }
            }
            this.mFrozenProcesses.delete(app.getPid());
        }
    }

    void onWakefulnessChanged(int wakefulness) {
        if (wakefulness == 1 && useCompaction()) {
            cancelAllCompactions(com.android.server.am.CachedAppOptimizer.CancelCompactReason.SCREEN_ON);
        }
    }

    void cancelAllCompactions(com.android.server.am.CachedAppOptimizer.CancelCompactReason reason) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            while (!this.mPendingCompactionProcesses.isEmpty()) {
                try {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                        android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Cancel pending compaction as system is awake for process=" + this.mPendingCompactionProcesses.get(0).processName);
                    }
                    cancelCompactionForProcess(this.mPendingCompactionProcesses.get(0), reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            this.mPendingCompactionProcesses.clear();
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    void cancelCompactionForProcess(com.android.server.am.ProcessRecord app, com.android.server.am.CachedAppOptimizer.CancelCompactReason cancelReason) {
        boolean cancelled = false;
        if (this.mPendingCompactionProcesses.contains(app)) {
            app.mOptRecord.setHasPendingCompact(false);
            this.mPendingCompactionProcesses.remove(app);
            cancelled = true;
        }
        if (com.android.server.am.CachedAppOptimizer.DefaultProcessDependencies.mPidCompacting == app.mPid) {
            cancelCompaction();
            cancelled = true;
        }
        if (cancelled) {
            if (this.mTotalCompactionsCancelled.containsKey(cancelReason)) {
                int count = this.mTotalCompactionsCancelled.get(cancelReason).intValue();
                this.mTotalCompactionsCancelled.put(cancelReason, java.lang.Integer.valueOf(count + 1));
            } else {
                this.mTotalCompactionsCancelled.put(cancelReason, 1);
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, new java.lang.StringBuilder().append("Cancelled pending or running compactions for process: ").append(app.processName).toString() != null ? app.processName : " reason: " + cancelReason.name());
            }
        }
    }

    void onOomAdjustChanged(int oldAdj, int newAdj, com.android.server.am.ProcessRecord app) {
        if (useCompaction() && newAdj < oldAdj && newAdj < 900) {
            cancelCompactionForProcess(app, com.android.server.am.CachedAppOptimizer.CancelCompactReason.OOM_IMPROVEMENT);
        }
    }

    void onProcessFrozen(com.android.server.am.ProcessRecord frozenProc) {
        if (useCompaction()) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    compactApp(frozenProc, com.android.server.am.CachedAppOptimizer.CompactProfile.FULL, com.android.server.am.CachedAppOptimizer.CompactSource.APP, false);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
        frozenProc.onProcessFrozen();
    }

    void onProcessFrozenCancelled(com.android.server.am.ProcessRecord app) {
        app.onProcessFrozenCancelled();
    }

    com.android.server.am.CachedAppOptimizer.CompactProfile resolveCompactionProfile(com.android.server.am.CachedAppOptimizer.CompactProfile profile) {
        if (profile == com.android.server.am.CachedAppOptimizer.CompactProfile.FULL) {
            double swapFreePercent = getFreeSwapPercent();
            if (swapFreePercent < COMPACT_DOWNGRADE_FREE_SWAP_THRESHOLD) {
                profile = com.android.server.am.CachedAppOptimizer.CompactProfile.SOME;
                this.mTotalCompactionDowngrades++;
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Downgraded compaction to " + profile + " due to low swap. Swap Free% " + swapFreePercent);
                }
            }
        }
        if (profile == com.android.server.am.CachedAppOptimizer.CompactProfile.SOME) {
            profile = com.android.server.am.CachedAppOptimizer.CompactProfile.NONE;
        } else if (profile == com.android.server.am.CachedAppOptimizer.CompactProfile.FULL) {
            profile = com.android.server.am.CachedAppOptimizer.CompactProfile.ANON;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Final compaction profile " + profile + " due to file compact disabled");
        }
        return profile;
    }

    boolean isProcessFrozen(int pid) {
        boolean zContains;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                zContains = this.mFrozenProcesses.contains(pid);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        return zContains;
    }

    static final class SingleCompactionStats {
        private static final float STATSD_SAMPLE_RATE = 0.1f;
        private static final java.util.Random mRandom = new java.util.Random();
        public long mAnonMemFreedKBs;
        public float mCpuTimeMillis;
        public long mDeltaAnonRssKBs;
        public int mOomAdj;
        public int mOomAdjReason;
        public long mOrigAnonRss;
        public int mProcState;
        public java.lang.String mProcessName;
        private final long[] mRssAfterCompaction;
        public com.android.server.am.CachedAppOptimizer.CompactSource mSourceType;
        public final int mUid;
        public long mZramConsumedKBs;

        SingleCompactionStats(long[] rss, com.android.server.am.CachedAppOptimizer.CompactSource source, java.lang.String processName, long deltaAnonRss, long zramConsumed, long anonMemFreed, long origAnonRss, long cpuTimeMillis, int procState, int oomAdj, int oomAdjReason, int uid) {
            this.mRssAfterCompaction = rss;
            this.mSourceType = source;
            this.mProcessName = processName;
            this.mUid = uid;
            this.mDeltaAnonRssKBs = deltaAnonRss;
            this.mZramConsumedKBs = zramConsumed;
            this.mAnonMemFreedKBs = anonMemFreed;
            this.mCpuTimeMillis = cpuTimeMillis;
            this.mOrigAnonRss = origAnonRss;
            this.mProcState = procState;
            this.mOomAdj = oomAdj;
            this.mOomAdjReason = oomAdjReason;
        }

        double getCompactEfficiency() {
            return this.mAnonMemFreedKBs / this.mOrigAnonRss;
        }

        double getCompactCost() {
            return (((double) this.mCpuTimeMillis) / this.mAnonMemFreedKBs) * 1024.0d;
        }

        long[] getRssAfterCompaction() {
            return this.mRssAfterCompaction;
        }

        @dalvik.annotation.optimization.NeverCompile
        void dump(java.io.PrintWriter pw) {
            pw.println("    (" + this.mProcessName + "," + this.mSourceType.name() + "," + this.mDeltaAnonRssKBs + "," + this.mZramConsumedKBs + "," + this.mAnonMemFreedKBs + "," + getCompactEfficiency() + "," + getCompactCost() + "," + this.mProcState + "," + this.mOomAdj + "," + com.android.server.am.OomAdjuster.oomAdjReasonToString(this.mOomAdjReason) + ")");
        }

        void sendStat() {
            if (mRandom.nextFloat() < STATSD_SAMPLE_RATE) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_COMPACTED_V2, this.mUid, this.mProcState, this.mOomAdj, this.mDeltaAnonRssKBs, this.mZramConsumedKBs, this.mCpuTimeMillis, this.mOrigAnonRss, this.mOomAdjReason);
            }
        }
    }

    private final class MemCompactionHandler extends android.os.Handler {
        private MemCompactionHandler() {
            super(com.android.server.am.CachedAppOptimizer.this.mCachedAppOptimizerThread.getLooper());
        }

        private boolean shouldOomAdjThrottleCompaction(com.android.server.am.ProcessRecord proc) {
            java.lang.String name = proc.processName;
            if (proc.mState.getSetAdj() <= 200) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping compaction as process " + name + " is now perceptible.");
                    return true;
                }
                return true;
            }
            return false;
        }

        private boolean shouldTimeThrottleCompaction(com.android.server.am.ProcessRecord proc, long start, com.android.server.am.CachedAppOptimizer.CompactProfile pendingProfile, com.android.server.am.CachedAppOptimizer.CompactSource source) {
            com.android.server.am.ProcessCachedOptimizerRecord opt = proc.mOptRecord;
            java.lang.String name = proc.processName;
            com.android.server.am.CachedAppOptimizer.CompactProfile lastCompactProfile = opt.getLastCompactProfile();
            long lastCompactTime = opt.getLastCompactTime();
            if (lastCompactTime != 0 && source == com.android.server.am.CachedAppOptimizer.CompactSource.APP) {
                if (pendingProfile == com.android.server.am.CachedAppOptimizer.CompactProfile.SOME) {
                    if ((lastCompactProfile == com.android.server.am.CachedAppOptimizer.CompactProfile.SOME && start - lastCompactTime < com.android.server.am.CachedAppOptimizer.this.mCompactThrottleSomeSome) || (lastCompactProfile == com.android.server.am.CachedAppOptimizer.CompactProfile.FULL && start - lastCompactTime < com.android.server.am.CachedAppOptimizer.this.mCompactThrottleSomeFull)) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping some compaction for " + name + ": too soon. throttle=" + com.android.server.am.CachedAppOptimizer.this.mCompactThrottleSomeSome + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + com.android.server.am.CachedAppOptimizer.this.mCompactThrottleSomeFull + " last=" + (start - lastCompactTime) + "ms ago");
                            return true;
                        }
                        return true;
                    }
                    return false;
                }
                if (pendingProfile == com.android.server.am.CachedAppOptimizer.CompactProfile.FULL) {
                    if ((lastCompactProfile == com.android.server.am.CachedAppOptimizer.CompactProfile.SOME && start - lastCompactTime < com.android.server.am.CachedAppOptimizer.this.mCompactThrottleFullSome) || (lastCompactProfile == com.android.server.am.CachedAppOptimizer.CompactProfile.FULL && start - lastCompactTime < com.android.server.am.CachedAppOptimizer.this.mCompactThrottleFullFull)) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping full compaction for " + name + ": too soon. throttle=" + com.android.server.am.CachedAppOptimizer.this.mCompactThrottleFullSome + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + com.android.server.am.CachedAppOptimizer.this.mCompactThrottleFullFull + " last=" + (start - lastCompactTime) + "ms ago");
                            return true;
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        private boolean shouldThrottleMiscCompaction(com.android.server.am.ProcessRecord proc, int procState) {
            if (com.android.server.am.CachedAppOptimizer.this.mProcStateThrottle.contains(java.lang.Integer.valueOf(procState))) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    java.lang.String name = proc.processName;
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping full compaction for process " + name + "; proc state is " + procState);
                    return true;
                }
                return true;
            }
            return false;
        }

        private boolean shouldRssThrottleCompaction(com.android.server.am.CachedAppOptimizer.CompactProfile profile, int pid, java.lang.String name, long[] rssBefore) {
            long anonRssBefore = rssBefore[2];
            com.android.server.am.CachedAppOptimizer.SingleCompactionStats lastCompactionStats = com.android.server.am.CachedAppOptimizer.this.mLastCompactionStats.get(java.lang.Integer.valueOf(pid));
            if (rssBefore[0] == 0 && rssBefore[1] == 0 && rssBefore[2] == 0 && rssBefore[3] == 0) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping compaction forprocess " + pid + " with no memory usage. Dead?");
                }
                return true;
            }
            if (profile != com.android.server.am.CachedAppOptimizer.CompactProfile.FULL) {
                return false;
            }
            if (com.android.server.am.CachedAppOptimizer.this.mFullAnonRssThrottleKb > 0 && anonRssBefore < com.android.server.am.CachedAppOptimizer.this.mFullAnonRssThrottleKb) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                    android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping full compaction for process " + name + "; anon RSS is too small: " + anonRssBefore + "KB.");
                }
                return true;
            }
            if (lastCompactionStats != null && com.android.server.am.CachedAppOptimizer.this.mFullDeltaRssThrottleKb > 0) {
                long[] lastRss = lastCompactionStats.getRssAfterCompaction();
                long absDelta = java.lang.Math.abs(rssBefore[1] - lastRss[1]) + java.lang.Math.abs(rssBefore[2] - lastRss[2]) + java.lang.Math.abs(rssBefore[3] - lastRss[3]);
                if (absDelta <= com.android.server.am.CachedAppOptimizer.this.mFullDeltaRssThrottleKb) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_COMPACTION) {
                        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping full compaction for process " + name + "; abs delta is too small: " + absDelta + "KB.");
                        return true;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:102:0x0403  */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void handleMessage(android.os.Message r82) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 1310
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.CachedAppOptimizer.MemCompactionHandler.handleMessage(android.os.Message):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportOneUidFrozenStateChanged(int uid, boolean frozen) {
        int[] frozenStates = new int[1];
        int[] uids = {uid};
        frozenStates[0] = frozen ? 1 : 2;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "reportOneUidFrozenStateChanged uid " + uid + " frozen = " + frozen);
        }
        this.mAm.reportUidFrozenStateChanged(uids, frozenStates);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postUidFrozenMessage(int i, boolean z) {
        java.lang.Integer numValueOf = java.lang.Integer.valueOf(i);
        this.mFreezeHandler.removeEqualMessages(6, numValueOf);
        this.mFreezeHandler.sendMessage(this.mFreezeHandler.obtainMessage(6, z ? 1 : 0, 0, numValueOf));
    }

    private void reportProcessFreezableChangedLocked(com.android.server.am.ProcessRecord app) {
        this.mAm.onProcessFreezableChangedLocked(app);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class FreezeHandler extends android.os.Handler implements com.android.internal.os.ProcLocksReader.ProcLocksReaderCallback {
        private FreezeHandler() {
            super(com.android.server.am.CachedAppOptimizer.this.mCachedAppOptimizerThread.getLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 3:
                    com.android.server.am.ProcessRecord proc = (com.android.server.am.ProcessRecord) msg.obj;
                    com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.CachedAppOptimizer.this.mAm;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            if (!proc.mOptRecord.isPendingFreeze()) {
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            freezeProcess(proc);
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            if (proc.mOptRecord.isFrozen()) {
                                com.android.server.am.CachedAppOptimizer.this.onProcessFrozen(proc);
                                removeMessages(7);
                                sendEmptyMessageDelayed(7, 1000L);
                                return;
                            }
                            com.android.server.am.CachedAppOptimizer.this.onProcessFrozenCancelled(proc);
                            return;
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                case 4:
                    int pid = msg.arg1;
                    int frozenDuration = msg.arg2;
                    android.util.Pair<com.android.server.am.ProcessRecord, java.lang.Integer> obj = (android.util.Pair) msg.obj;
                    com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) obj.first;
                    java.lang.String processName = app.processName;
                    int reason = ((java.lang.Integer) obj.second).intValue();
                    reportUnfreeze(app, pid, frozenDuration, processName, reason);
                    return;
                case 5:
                default:
                    return;
                case 6:
                    boolean frozen = msg.arg1 == 1;
                    int uid = ((java.lang.Integer) msg.obj).intValue();
                    com.android.server.am.CachedAppOptimizer.this.reportOneUidFrozenStateChanged(uid, frozen);
                    return;
                case 7:
                    try {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Freezer deadlock watchdog");
                        }
                        com.android.server.am.CachedAppOptimizer.this.mProcLocksReader.handleBlockingFileLocks(this);
                        return;
                    } catch (java.io.IOException e) {
                        android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to check file locks");
                        return;
                    }
                case 8:
                    android.util.IntArray pids = new android.util.IntArray();
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.CachedAppOptimizer.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            int size = com.android.server.am.CachedAppOptimizer.this.mFrozenProcesses.size();
                            for (int i = 0; i < size; i++) {
                                pids.add(com.android.server.am.CachedAppOptimizer.this.mFrozenProcesses.keyAt(i));
                            }
                        } catch (java.lang.Throwable th2) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th2;
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    com.android.server.am.CachedAppOptimizer.this.binderErrorInternal(pids);
                    return;
            }
        }

        private void handleBinderFreezerFailure(final com.android.server.am.ProcessRecord proc, java.lang.String reason) {
            if (!com.android.server.am.CachedAppOptimizer.this.mFreezerBinderEnabled) {
                com.android.server.am.CachedAppOptimizer.this.unfreezeAppLSP(proc, 18);
                com.android.server.am.CachedAppOptimizer.this.freezeAppAsyncLSP(proc);
                return;
            }
            if (proc.mOptRecord.getLastUsedTimeout() <= com.android.server.am.CachedAppOptimizer.this.mFreezerBinderThreshold) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Kill app due to repeated failure to freeze binder: " + proc.getPid() + " " + proc.processName);
                com.android.server.am.CachedAppOptimizer.this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.CachedAppOptimizer$FreezeHandler$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleBinderFreezerFailure$0(proc);
                    }
                });
                return;
            }
            long timeout = proc.mOptRecord.getLastUsedTimeout() / com.android.server.am.CachedAppOptimizer.this.mFreezerBinderDivisor;
            int offset = com.android.server.am.CachedAppOptimizer.this.mRandom.nextInt(com.android.server.am.CachedAppOptimizer.this.mFreezerBinderOffset * 2) - com.android.server.am.CachedAppOptimizer.this.mFreezerBinderOffset;
            long timeout2 = java.lang.Math.max(((long) offset) + timeout, com.android.server.am.CachedAppOptimizer.this.mFreezerBinderThreshold);
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Reschedule freeze for process " + proc.getPid() + " " + proc.processName + " (" + reason + "), timeout=" + timeout2);
            android.os.Trace.instantForTrack(64L, com.android.server.am.CachedAppOptimizer.ATRACE_FREEZER_TRACK, "Reschedule freeze " + proc.processName + ":" + proc.getPid() + " timeout=" + timeout2 + ", reason=" + reason);
            com.android.server.am.CachedAppOptimizer.this.unfreezeAppLSP(proc, 18);
            com.android.server.am.CachedAppOptimizer.this.freezeAppAsyncLSP(proc, timeout2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleBinderFreezerFailure$0(com.android.server.am.ProcessRecord proc) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.CachedAppOptimizer.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (proc.getThread() == null) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    } else {
                        proc.killLocked("excessive binder traffic during cached", 9, 7, true);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        private void freezeProcess(final com.android.server.am.ProcessRecord proc) {
            proc.getPid();
            java.lang.String name = proc.processName;
            com.android.server.am.ProcessCachedOptimizerRecord opt = proc.mOptRecord;
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.CachedAppOptimizer.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                if (opt.isPendingFreeze()) {
                    opt.setPendingFreeze(false);
                    int pid = proc.getPid();
                    if (com.android.server.am.CachedAppOptimizer.this.mFreezerOverride) {
                        opt.setFreezerOverride(true);
                        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping freeze for process " + pid + " " + name + " curAdj = " + proc.mState.getCurAdj() + "(override)");
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        return;
                    }
                    if (opt.shouldNotFreeze()) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping freeze because process is marked should not be frozen");
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        return;
                    }
                    if (pid != 0 && !opt.isFrozen()) {
                        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "freezing " + pid + " " + name);
                        try {
                        } catch (java.lang.RuntimeException e) {
                            android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to freeze binder for " + pid + " " + name);
                            com.android.server.am.CachedAppOptimizer.this.mFreezeHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.CachedAppOptimizer$FreezeHandler$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$freezeProcess$1(proc);
                                }
                            });
                        }
                        if (com.android.server.am.CachedAppOptimizer.freezeBinder(pid, true, 0) != 0) {
                            handleBinderFreezerFailure(proc, "outstanding txns");
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            return;
                        }
                        long unfreezeTime = opt.getFreezeUnfreezeTime();
                        try {
                            com.android.server.am.CachedAppOptimizer.traceAppFreeze(proc.processName, pid, -1);
                            android.os.Process.setProcessFrozen(pid, proc.uid, true);
                            opt.setFreezeUnfreezeTime(android.os.SystemClock.uptimeMillis());
                            opt.setFrozen(true);
                            opt.setHasCollectedFrozenPSS(false);
                            com.android.server.am.CachedAppOptimizer.this.mFrozenProcesses.put(pid, proc);
                        } catch (java.lang.Exception e2) {
                            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to freeze " + pid + " " + name);
                        }
                        long unfrozenDuration = opt.getFreezeUnfreezeTime() - unfreezeTime;
                        boolean frozen = opt.isFrozen();
                        com.android.server.am.UidRecord uidRec = proc.getUidRecord();
                        if (frozen && uidRec != null && uidRec.areAllProcessesFrozen()) {
                            uidRec.setFrozen(true);
                            com.android.server.am.CachedAppOptimizer.this.postUidFrozenMessage(uidRec.getUid(), true);
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        if (!frozen) {
                            return;
                        }
                        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_FREEZE, java.lang.Integer.valueOf(pid), name);
                        if (com.android.server.am.CachedAppOptimizer.this.mRandom.nextFloat() < com.android.server.am.CachedAppOptimizer.this.mFreezerStatsdSampleRate) {
                            com.android.internal.util.FrameworkStatsLog.write(254, 1, pid, name, unfrozenDuration, 0, 0);
                        }
                        try {
                            int freezeInfo = com.android.server.am.CachedAppOptimizer.getBinderFreezeInfo(pid);
                            if ((freezeInfo & 4) != 0) {
                                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = com.android.server.am.CachedAppOptimizer.this.mProcLock;
                                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                                synchronized (activityManagerGlobalLock2) {
                                    try {
                                        handleBinderFreezerFailure(proc, "new pending txns");
                                    } finally {
                                    }
                                }
                                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                return;
                            }
                            return;
                        } catch (java.lang.RuntimeException e3) {
                            android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to freeze binder for " + pid + " " + name);
                            com.android.server.am.CachedAppOptimizer.this.mFreezeHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.CachedAppOptimizer$FreezeHandler$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$freezeProcess$2(proc);
                                }
                            });
                            return;
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    }
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping freeze for process " + pid + " " + name + ". Already frozen or not a real process");
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$freezeProcess$1(com.android.server.am.ProcessRecord proc) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.CachedAppOptimizer.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    proc.killLocked("Unable to freeze binder interface", 14, 19, true);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$freezeProcess$2(com.android.server.am.ProcessRecord proc) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.CachedAppOptimizer.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    proc.killLocked("Unable to freeze binder interface", 14, 19, true);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        private void reportUnfreeze(com.android.server.am.ProcessRecord app, int pid, int frozenDuration, java.lang.String processName, int reason) {
            android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_UNFREEZE, java.lang.Integer.valueOf(pid), processName, java.lang.Integer.valueOf(reason));
            app.onProcessUnfrozen();
            if (com.android.server.am.CachedAppOptimizer.this.mRandom.nextFloat() < com.android.server.am.CachedAppOptimizer.this.mFreezerStatsdSampleRate) {
                com.android.internal.util.FrameworkStatsLog.write(254, 2, pid, processName, frozenDuration, 0, reason);
            }
        }

        public void onBlockingFileLock(android.util.IntArray pids) {
            com.android.server.am.ProcessRecord pr;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Blocking file lock found: " + pids);
            }
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.CachedAppOptimizer.this.mAm;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.CachedAppOptimizer.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            int pid = pids.get(0);
                            com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) com.android.server.am.CachedAppOptimizer.this.mFrozenProcesses.get(pid);
                            if (app != null) {
                                int i = 1;
                                while (true) {
                                    if (i >= pids.size()) {
                                        break;
                                    }
                                    int blocked = pids.get(i);
                                    synchronized (com.android.server.am.CachedAppOptimizer.this.mAm.mPidsSelfLocked) {
                                        pr = com.android.server.am.CachedAppOptimizer.this.mAm.mPidsSelfLocked.get(blocked);
                                    }
                                    if (pr == null || pr.mState.getCurAdj() >= 900) {
                                        i++;
                                    } else {
                                        android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, app.processName + " (" + pid + ") blocks " + pr.processName + " (" + blocked + ")");
                                        com.android.server.am.CachedAppOptimizer.this.unfreezeAppLSP(app, 16);
                                        break;
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                } catch (java.lang.Throwable th2) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    private static final class DefaultProcessDependencies implements com.android.server.am.CachedAppOptimizer.ProcessDependencies {
        public static volatile int mPidCompacting = -1;

        private DefaultProcessDependencies() {
        }

        @Override // com.android.server.am.CachedAppOptimizer.ProcessDependencies
        public long[] getRss(int pid) {
            return android.os.Process.getRss(pid);
        }

        @Override // com.android.server.am.CachedAppOptimizer.ProcessDependencies
        public void performCompaction(com.android.server.am.CachedAppOptimizer.CompactProfile profile, int pid) throws java.io.IOException {
            mPidCompacting = pid;
            if (profile == com.android.server.am.CachedAppOptimizer.CompactProfile.FULL) {
                com.android.server.am.CachedAppOptimizer.compactProcess(pid, 3);
            } else if (profile == com.android.server.am.CachedAppOptimizer.CompactProfile.SOME) {
                com.android.server.am.CachedAppOptimizer.compactProcess(pid, 1);
            } else if (profile == com.android.server.am.CachedAppOptimizer.CompactProfile.ANON) {
                com.android.server.am.CachedAppOptimizer.compactProcess(pid, 2);
            }
            mPidCompacting = -1;
        }
    }

    static int getUnfreezeReasonCodeFromOomAdjReason(int oomAdjReason) {
        switch (oomAdjReason) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 20;
            case 14:
                return 21;
            case 15:
                return 22;
            case 16:
                return 23;
            case 17:
                return 24;
            case 18:
                return 25;
            case 19:
                return 26;
            case 20:
                return 27;
            case 21:
                return 28;
            case 22:
                return 29;
            default:
                return 0;
        }
    }

    public void killProcess(final int pid, final java.lang.String reason, final int reasonCode, final int subReason) {
        this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$killProcess$2(pid, reason, reasonCode, subReason);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$killProcess$2(int pid, java.lang.String reason, int reasonCode, int subReason) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        com.android.server.am.ProcessRecord proc = this.mFrozenProcesses.get(pid);
                        if (proc != null && proc.getThread() != null && !proc.isKilledByAm()) {
                            proc.killLocked(reason, reasonCode, subReason, true);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    public void binderError(int debugPid, com.android.server.am.ProcessRecord app, int code, int flags, int err) {
        android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "pid " + debugPid + " " + (app == null ? "null" : app.processName) + " sent binder code " + code + " with flags " + flags + " to frozen apps and got error " + err);
        if (!this.mUseFreezer || !this.mFreezerBinderCallbackEnabled) {
            return;
        }
        long now = android.os.SystemClock.uptimeMillis();
        if (now < this.mFreezerBinderCallbackLast + this.mFreezerBinderCallbackThrottle) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Too many transaction errors, throttling freezer binder callback.");
        } else {
            this.mFreezerBinderCallbackLast = now;
            this.mFreezeHandler.sendEmptyMessage(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void binderErrorInternal(android.util.IntArray pids) {
        final android.util.ArraySet<java.lang.Integer> pidsAsync = this.mFreezerBinderAsyncThreshold < 0 ? null : new android.util.ArraySet<>();
        for (int i = 0; i < pids.size(); i++) {
            int current = pids.get(i);
            try {
                int freezeInfo = getBinderFreezeInfo(current);
                if ((freezeInfo & 1) != 0) {
                    killProcess(current, "Sync transaction while frozen", 14, 20);
                } else if ((freezeInfo & 2) != 0) {
                    if (pidsAsync != null) {
                        pidsAsync.add(java.lang.Integer.valueOf(current));
                    }
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_FREEZER) {
                        android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "pid " + current + " received async transactions while frozen");
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to query binder frozen stats for pid " + current);
            }
        }
        if (pidsAsync == null || pidsAsync.size() == 0) {
            return;
        }
        com.android.internal.os.BinderfsStatsReader binderfsStatsReader = new com.android.internal.os.BinderfsStatsReader();
        java.util.Objects.requireNonNull(pidsAsync);
        binderfsStatsReader.handleFreeAsyncSpace(new java.util.function.Predicate() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return pidsAsync.contains((java.lang.Integer) obj);
            }
        }, new java.util.function.BiConsumer() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$binderErrorInternal$3((java.lang.Integer) obj, (java.lang.Integer) obj2);
            }
        }, new java.util.function.Consumer() { // from class: com.android.server.am.CachedAppOptimizer$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to parse binderfs stats");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$binderErrorInternal$3(java.lang.Integer current, java.lang.Integer free) {
        if (free.intValue() < this.mFreezerBinderAsyncThreshold) {
            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "pid " + current + " has " + free + " free async space, killing");
            killProcess(current.intValue(), "Async binder space running out while frozen", 14, 31);
        }
    }

    public com.android.server.am.ICachedAppOptimizerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class CachedAppOptimizerWrapper implements com.android.server.am.ICachedAppOptimizerWrapper {
        private boolean mExtUseFreezerEnable;

        private CachedAppOptimizerWrapper() {
            this.mExtUseFreezerEnable = true;
        }

        @Override // com.android.server.am.ICachedAppOptimizerWrapper
        public void updateExtUseFreezerEnable(boolean enable) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "updateExtUseFreezerEnable: " + enable);
            synchronized (com.android.server.am.CachedAppOptimizer.this.mPhenotypeFlagLock) {
                if (this.mExtUseFreezerEnable != enable) {
                    this.mExtUseFreezerEnable = enable;
                    com.android.server.am.CachedAppOptimizer.this.updateUseFreezer();
                }
            }
        }

        public boolean isExtUseFreezeEnable() {
            return this.mExtUseFreezerEnable;
        }
    }
}
