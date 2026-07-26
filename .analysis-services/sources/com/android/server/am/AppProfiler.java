package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class AppProfiler {
    private static final java.lang.String ACTION_HEAP_DUMP_FINISHED = "com.android.internal.intent.action.HEAP_DUMP_FINISHED";
    static final java.lang.String ACTIVITY_START_PSS_DEFER_CONFIG = "activity_start_pss_defer";
    static final long BATTERY_STATS_TIME = 1800000;
    private static final java.lang.String EXTRA_HEAP_DUMP_IS_USER_INITIATED = "com.android.internal.extra.heap_dump.IS_USER_INITIATED";
    private static final java.lang.String EXTRA_HEAP_DUMP_PROCESS_NAME = "com.android.internal.extra.heap_dump.PROCESS_NAME";
    private static final java.lang.String EXTRA_HEAP_DUMP_REPORT_PACKAGE = "com.android.internal.extra.heap_dump.REPORT_PACKAGE";
    private static final java.lang.String EXTRA_HEAP_DUMP_SIZE_BYTES = "com.android.internal.extra.heap_dump.SIZE_BYTES";
    static final long MONITOR_CPU_MAX_TIME = 268435455;
    static final long MONITOR_CPU_MIN_TIME = 5000;
    static final boolean MONITOR_CPU_USAGE = true;
    static final boolean MONITOR_THREAD_CPU_USAGE = false;
    private static final java.lang.String TAG = "ActivityManager";
    public com.android.server.power.stats.IBatteryStatsImplExt mBatteryStatsImplExt;
    private final android.os.Handler mBgHandler;
    boolean mHasHomeProcess;
    boolean mHasPreviousProcess;
    private int mLastNumProcesses;
    private final com.android.server.am.LowMemDetector mLowMemDetector;
    private int mMemWatchDumpPid;
    private java.lang.String mMemWatchDumpProcName;
    private int mMemWatchDumpUid;
    private android.net.Uri mMemWatchDumpUri;
    private boolean mMemWatchIsUserInitiated;
    final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    public com.android.internal.os.IProcessCpuTrackerExt mProcessCpuTrackerExt;
    private final com.android.server.am.ActivityManagerService mService;
    static final java.lang.String TAG_PSS = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_PSS;
    static final java.lang.String TAG_RSS = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_RSS;
    static final java.lang.String TAG_OOM_ADJ = com.android.server.am.ActivityManagerService.TAG_OOM_ADJ;
    private static final boolean IS_AGING_VERSION = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", ""));
    private static final boolean IS_HIGHTEMP_VERSION = "hightempaging".equals(android.os.SystemProperties.get("ro.oplus.image.my_engineering.type", ""));
    private volatile long mPssDeferralTime = 0;
    private final java.util.ArrayList<com.android.server.am.ProcessProfileRecord> mPendingPssOrRssProfiles = new java.util.ArrayList<>();
    private final java.util.concurrent.atomic.AtomicInteger mActivityStartingNesting = new java.util.concurrent.atomic.AtomicInteger(0);
    private long mLastFullPssTime = android.os.SystemClock.uptimeMillis();
    private boolean mFullPssOrRssPending = false;
    private volatile boolean mTestPssOrRssMode = false;
    private boolean mAllowLowerMemLevel = false;
    private int mLastMemoryLevel = 0;
    private int mMemFactorOverride = -1;
    private long mLowRamTimeSinceLastIdle = 0;
    private long mLowRamStartTime = 0;
    private long mLastMemUsageReportTime = 0;
    private final java.util.ArrayList<com.android.server.am.ProcessRecord> mProcessesToGc = new java.util.ArrayList<>();
    private java.util.Map<java.lang.String, java.lang.String> mAppAgentMap = null;
    private int mProfileType = 0;
    private final com.android.server.am.AppProfiler.ProfileData mProfileData = new com.android.server.am.AppProfiler.ProfileData();
    private final com.android.internal.app.ProcessMap<android.util.Pair<java.lang.Long, java.lang.String>> mMemWatchProcesses = new com.android.internal.app.ProcessMap<>();
    private final com.android.internal.os.ProcessCpuTracker mProcessCpuTracker = new com.android.internal.os.ProcessCpuTracker(false);
    private final java.util.concurrent.atomic.AtomicLong mLastCpuTime = new java.util.concurrent.atomic.AtomicLong(0);
    private final java.util.concurrent.atomic.AtomicBoolean mProcessCpuMutexFree = new java.util.concurrent.atomic.AtomicBoolean(true);
    private final java.util.concurrent.CountDownLatch mProcessCpuInitLatch = new java.util.concurrent.CountDownLatch(1);
    private volatile long mLastWriteTime = 0;
    final com.android.server.am.AppProfiler.CachedAppsWatermarkData mCachedAppsWatermarkData = new com.android.server.am.AppProfiler.CachedAppsWatermarkData();
    final java.lang.Object mProfilerLock = new java.lang.Object();
    public com.android.server.am.IAppProfilerExt mAppProfilerExt = (com.android.server.am.IAppProfilerExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IAppProfilerExt.class).create();
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mPssDelayConfigListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.AppProfiler.1
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains(com.android.server.am.AppProfiler.ACTIVITY_START_PSS_DEFER_CONFIG)) {
                com.android.server.am.AppProfiler.this.mPssDeferralTime = properties.getLong(com.android.server.am.AppProfiler.ACTIVITY_START_PSS_DEFER_CONFIG, 0L);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
                    android.util.Slog.d(com.android.server.am.AppProfiler.TAG_PSS, "Activity-start PSS delay now " + com.android.server.am.AppProfiler.this.mPssDeferralTime + " ms");
                }
            }
        }
    };
    private final java.lang.Thread mProcessCpuThread = new com.android.server.am.AppProfiler.ProcessCpuThread("CpuTracker");

    private class ProfileData {
        private java.lang.String mProfileApp;
        private com.android.server.am.ProcessRecord mProfileProc;
        private android.app.ProfilerInfo mProfilerInfo;

        private ProfileData() {
            this.mProfileApp = null;
            this.mProfileProc = null;
            this.mProfilerInfo = null;
        }

        void setProfileApp(java.lang.String profileApp) {
            this.mProfileApp = profileApp;
            if (com.android.server.am.AppProfiler.this.mService.mAtmInternal != null) {
                com.android.server.am.AppProfiler.this.mService.mAtmInternal.setProfileApp(profileApp);
            }
        }

        java.lang.String getProfileApp() {
            return this.mProfileApp;
        }

        void setProfileProc(com.android.server.am.ProcessRecord profileProc) {
            this.mProfileProc = profileProc;
            if (com.android.server.am.AppProfiler.this.mService.mAtmInternal != null) {
                com.android.server.am.AppProfiler.this.mService.mAtmInternal.setProfileProc(profileProc == null ? null : profileProc.getWindowProcessController());
            }
        }

        com.android.server.am.ProcessRecord getProfileProc() {
            return this.mProfileProc;
        }

        void setProfilerInfo(android.app.ProfilerInfo profilerInfo) {
            this.mProfilerInfo = profilerInfo;
            if (com.android.server.am.AppProfiler.this.mService.mAtmInternal != null) {
                com.android.server.am.AppProfiler.this.mService.mAtmInternal.setProfilerInfo(profilerInfo);
            }
        }

        android.app.ProfilerInfo getProfilerInfo() {
            return this.mProfilerInfo;
        }
    }

    class CachedAppsWatermarkData {
        int mAverageFrozenTimeInSeconds;
        int mBinderProxySnapshot;
        private long[] mCachedAppFrozenDurations;
        int mCachedAppHighWatermark;
        int mCachedInKb;
        private long mEarliestFrozenTimestamp;
        int mFreeInKb;
        int mKernelInKb;
        private long mLatestFrozenTimestamp;
        int mLongestFrozenTimeInSeconds;
        int mMeanFrozenTimeInSeconds;
        int mNumOfFrozenApps;
        int mShortestFrozenTimeInSeconds;
        private long mTotalFrozenDurations;
        int mUptimeInSeconds;
        int mZramInKb;

        CachedAppsWatermarkData() {
        }

        void updateCachedAppsHighWatermarkIfNecessaryLocked(int numOfCachedApps, long now) {
            if (numOfCachedApps > this.mCachedAppHighWatermark) {
                this.mCachedAppHighWatermark = numOfCachedApps;
                this.mUptimeInSeconds = (int) (now / 1000);
                com.android.server.am.AppProfiler.this.mService.mHandler.removeMessages(79);
                com.android.server.am.AppProfiler.this.mService.mHandler.obtainMessage(79, java.lang.Long.valueOf(now)).sendToTarget();
            }
        }

        void updateCachedAppsSnapshot(final long now) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.AppProfiler.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    this.mEarliestFrozenTimestamp = now;
                    this.mLatestFrozenTimestamp = 0L;
                    this.mTotalFrozenDurations = 0L;
                    this.mNumOfFrozenApps = 0;
                    int lruSize = com.android.server.am.AppProfiler.this.mService.mProcessList.getLruSizeLOSP();
                    if (this.mCachedAppFrozenDurations == null || this.mCachedAppFrozenDurations.length < lruSize) {
                        this.mCachedAppFrozenDurations = new long[java.lang.Math.max(lruSize, com.android.server.am.AppProfiler.this.mService.mConstants.CUR_MAX_CACHED_PROCESSES)];
                    }
                    com.android.server.am.AppProfiler.this.mService.mProcessList.forEachLruProcessesLOSP(true, new java.util.function.Consumer() { // from class: com.android.server.am.AppProfiler$CachedAppsWatermarkData$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$updateCachedAppsSnapshot$0(now, (com.android.server.am.ProcessRecord) obj);
                        }
                    });
                    if (this.mNumOfFrozenApps > 0) {
                        this.mLongestFrozenTimeInSeconds = (int) ((now - this.mEarliestFrozenTimestamp) / 1000);
                        this.mShortestFrozenTimeInSeconds = (int) ((now - this.mLatestFrozenTimestamp) / 1000);
                        this.mAverageFrozenTimeInSeconds = (int) ((this.mTotalFrozenDurations / ((long) this.mNumOfFrozenApps)) / 1000);
                        this.mMeanFrozenTimeInSeconds = (int) (com.android.internal.util.QuickSelect.select(this.mCachedAppFrozenDurations, 0, this.mNumOfFrozenApps, this.mNumOfFrozenApps / 2) / 1000);
                    }
                    this.mBinderProxySnapshot = 0;
                    android.util.SparseIntArray counts = com.android.internal.os.BinderInternal.nGetBinderProxyPerUidCounts();
                    if (counts != null) {
                        int size = counts.size();
                        for (int i = 0; i < size; i++) {
                            int uid = counts.keyAt(i);
                            com.android.server.am.UidRecord uidRec = com.android.server.am.AppProfiler.this.mService.mProcessList.getUidRecordLOSP(uid);
                            if (uidRec != null) {
                                this.mBinderProxySnapshot += counts.valueAt(i);
                            }
                        }
                    }
                    com.android.internal.util.MemInfoReader memInfo = new com.android.internal.util.MemInfoReader();
                    memInfo.readMemInfo();
                    this.mFreeInKb = (int) memInfo.getFreeSizeKb();
                    this.mCachedInKb = (int) memInfo.getCachedSizeKb();
                    this.mZramInKb = (int) memInfo.getZramTotalSizeKb();
                    this.mKernelInKb = (int) memInfo.getKernelUsedSizeKb();
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateCachedAppsSnapshot$0(long now, com.android.server.am.ProcessRecord app) {
            if (app.mOptRecord.isFrozen()) {
                long freezeTime = app.mOptRecord.getFreezeUnfreezeTime();
                if (freezeTime < this.mEarliestFrozenTimestamp) {
                    this.mEarliestFrozenTimestamp = freezeTime;
                }
                if (freezeTime > this.mLatestFrozenTimestamp) {
                    this.mLatestFrozenTimestamp = freezeTime;
                }
                long duration = now - freezeTime;
                this.mTotalFrozenDurations += duration;
                long[] jArr = this.mCachedAppFrozenDurations;
                int i = this.mNumOfFrozenApps;
                this.mNumOfFrozenApps = i + 1;
                jArr[i] = duration;
            }
        }

        android.util.StatsEvent getCachedAppsHighWatermarkStats(int atomTag, boolean resetAfterPull) {
            android.util.StatsEvent event;
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.AppProfiler.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    event = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mCachedAppHighWatermark, this.mUptimeInSeconds, this.mBinderProxySnapshot, this.mFreeInKb, this.mCachedInKb, this.mZramInKb, this.mKernelInKb, this.mNumOfFrozenApps, this.mLongestFrozenTimeInSeconds, this.mShortestFrozenTimeInSeconds, this.mMeanFrozenTimeInSeconds, this.mAverageFrozenTimeInSeconds);
                    if (resetAfterPull) {
                        this.mCachedAppHighWatermark = 0;
                        this.mUptimeInSeconds = 0;
                        this.mBinderProxySnapshot = 0;
                        this.mFreeInKb = 0;
                        this.mCachedInKb = 0;
                        this.mZramInKb = 0;
                        this.mKernelInKb = 0;
                        this.mNumOfFrozenApps = 0;
                        this.mLongestFrozenTimeInSeconds = 0;
                        this.mShortestFrozenTimeInSeconds = 0;
                        this.mMeanFrozenTimeInSeconds = 0;
                        this.mAverageFrozenTimeInSeconds = 0;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            return event;
        }
    }

    private class BgHandler extends android.os.Handler {
        static final int COLLECT_PSS_BG_MSG = 1;
        static final int DEFER_PSS_MSG = 2;
        static final int MEMORY_PRESSURE_CHANGED = 4;
        static final int STOP_DEFERRING_PSS_MSG = 3;

        BgHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 1:
                    if (!com.android.server.am.AppProfiler.this.mAppProfilerExt.isNeedSkipDumpPss(com.android.server.am.AppProfiler.this.mBgHandler)) {
                        if (com.android.server.am.AppProfiler.this.isProfilingPss()) {
                            com.android.server.am.AppProfiler.this.collectPssInBackground();
                            return;
                        } else {
                            com.android.server.am.AppProfiler.this.collectRssInBackground();
                            return;
                        }
                    }
                    return;
                case 2:
                    com.android.server.am.AppProfiler.this.deferPssForActivityStart();
                    return;
                case 3:
                    com.android.server.am.AppProfiler.this.stopDeferPss();
                    return;
                case 4:
                    com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.AppProfiler.this.mService;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            com.android.server.am.AppProfiler.this.handleMemoryPressureChangedLocked(msg.arg1, msg.arg2);
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                default:
                    com.android.server.am.AppProfiler.this.mProcessCpuTracker.mProcessCpuTrackerExt.handleMessage(msg);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0312 A[Catch: all -> 0x03a7, TRY_LEAVE, TryCatch #1 {all -> 0x03a7, blocks: (B:133:0x030b, B:135:0x0312), top: B:194:0x030b }] */
    /* JADX WARN: Removed duplicated region for block: B:168:0x03a1  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01bc A[Catch: all -> 0x03d0, TRY_ENTER, TRY_LEAVE, TryCatch #7 {all -> 0x03d0, blocks: (B:60:0x0138, B:72:0x0185, B:81:0x01bc), top: B:206:0x0138 }] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:205:? -> B:175:0x03ae). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:189:0x03dd
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void collectPssInBackground() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 992
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppProfiler.collectPssInBackground():void");
    }

    static /* synthetic */ boolean lambda$collectPssInBackground$0(com.android.internal.os.ProcessCpuTracker.Stats st) {
        return st.vsize > 0 && st.uid < 10000;
    }

    boolean isProfilingPss() {
        return !com.android.internal.hidden_from_bootclasspath.android.os.Flags.removeAppProfilerPssCollection() || this.mService.mConstants.mForceEnablePssProfiling;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0270 A[Catch: all -> 0x02e9, TryCatch #3 {all -> 0x02e9, blocks: (B:118:0x023a, B:124:0x0269, B:126:0x0270, B:130:0x0292, B:134:0x029d, B:138:0x02ac, B:142:0x02db), top: B:176:0x023a }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0172 A[Catch: all -> 0x0304, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0304, blocks: (B:54:0x00ea, B:66:0x013a, B:75:0x0172), top: B:172:0x00ea }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:185:? -> B:150:0x02ed). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void collectRssInBackground() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 792
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppProfiler.collectRssInBackground():void");
    }

    static /* synthetic */ boolean lambda$collectRssInBackground$1(com.android.internal.os.ProcessCpuTracker.Stats st) {
        return st.vsize > 0 && st.uid < 10000;
    }

    void updateNextPssTimeLPf(int procState, com.android.server.am.ProcessProfileRecord profile, long now, boolean forceUpdate) {
        if (!forceUpdate && ((now <= profile.getNextPssTime() && now <= java.lang.Math.max(profile.getLastPssTime() + 3600000, profile.getLastStateTime() + com.android.server.am.ProcessList.minTimeFromStateChange(this.mTestPssOrRssMode))) || !requestPssLPf(profile, procState))) {
            return;
        }
        profile.setNextPssTime(profile.computeNextPssTime(procState, this.mTestPssOrRssMode, this.mService.mAtmInternal.isSleeping(), now));
    }

    private void recordPssSampleLPf(com.android.server.am.ProcessProfileRecord profile, int procState, long pss, long uss, long swapPss, long rss, int statType, long pssDuration, long now) {
        com.android.server.am.ProcessRecord proc = profile.mApp;
        this.mAppProfilerExt.recordPssStats(proc.info.packageName, proc.processName, proc.info.uid, proc.getPid(), procState, pss, uss, swapPss, rss);
        com.android.server.am.EventLogTags.writeAmPss(profile.getPid(), proc.uid, proc.processName, pss * 1024, uss * 1024, swapPss * 1024, rss * 1024, statType, procState, pssDuration);
        profile.setLastPssTime(now);
        profile.addPss(pss, uss, rss, true, statType, pssDuration);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
            android.util.Slog.d(TAG_PSS, "pss of " + proc.toShortString() + ": " + pss + " lastPss=" + profile.getLastPss() + " state=" + com.android.server.am.ProcessList.makeProcStateString(procState));
        }
        if (profile.getInitialIdlePssOrRss() == 0) {
            profile.setInitialIdlePssOrRss(pss);
        }
        profile.setLastPss(pss);
        profile.setLastSwapPss(swapPss);
        if (procState >= 14) {
            profile.setLastCachedPss(pss);
            profile.setLastCachedSwapPss(swapPss);
        }
        profile.setLastRss(rss);
        android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.String>> watchUids = (android.util.SparseArray) this.mMemWatchProcesses.getMap().get(proc.processName);
        java.lang.Long check = null;
        if (watchUids != null) {
            android.util.Pair<java.lang.Long, java.lang.String> val = watchUids.get(proc.uid);
            if (val == null) {
                val = watchUids.get(0);
            }
            if (val != null) {
                check = (java.lang.Long) val.first;
            }
        }
        if (check != null && 1024 * pss >= check.longValue() && profile.getThread() != null && this.mMemWatchDumpProcName == null) {
            if (android.os.Build.IS_DEBUGGABLE || proc.isDebuggable()) {
                android.util.Slog.w("ActivityManager", "Process " + proc + " exceeded pss limit " + check + "; reporting");
                startHeapDumpLPf(profile, false);
            } else {
                android.util.Slog.w("ActivityManager", "Process " + proc + " exceeded pss limit " + check + ", but debugging not enabled");
            }
        }
    }

    private void recordRssSampleLPf(com.android.server.am.ProcessProfileRecord profile, int procState, long rss, int statType, long rssDuration, long now) {
        long j;
        com.android.server.am.ProcessRecord proc = profile.mApp;
        this.mAppProfilerExt.recordRssStats(proc, procState, rss);
        com.android.server.am.EventLogTags.writeAmPss(profile.getPid(), proc.uid, proc.processName, 0L, 0L, 0L, rss * 1024, statType, procState, rssDuration);
        profile.setLastPssTime(now);
        profile.addPss(0L, 0L, rss, true, statType, rssDuration);
        if (!com.android.server.am.ActivityManagerDebugConfig.DEBUG_RSS) {
            j = rss;
        } else {
            j = rss;
            android.util.Slog.d(TAG_RSS, "rss of " + proc.toShortString() + ": " + j + " lastRss=" + profile.getLastRss() + " state=" + com.android.server.am.ProcessList.makeProcStateString(procState));
        }
        if (profile.getInitialIdlePssOrRss() == 0) {
            profile.setInitialIdlePssOrRss(j);
        }
        profile.setLastRss(j);
        if (procState >= 14) {
            profile.setLastCachedRss(j);
        }
        android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.String>> watchUids = (android.util.SparseArray) this.mMemWatchProcesses.getMap().get(proc.processName);
        java.lang.Long check = null;
        if (watchUids != null) {
            android.util.Pair<java.lang.Long, java.lang.String> val = watchUids.get(proc.uid);
            if (val == null) {
                val = watchUids.get(0);
            }
            if (val != null) {
                check = (java.lang.Long) val.first;
            }
        }
        if (check != null) {
            long pss = android.os.Debug.getPss(profile.getPid(), null, null);
            if (1024 * pss >= check.longValue() && profile.getThread() != null && this.mMemWatchDumpProcName == null) {
                if (android.os.Build.IS_DEBUGGABLE || proc.isDebuggable()) {
                    android.util.Slog.w("ActivityManager", "Process " + proc + " exceeded pss limit " + check + "; reporting");
                    startHeapDumpLPf(profile, false);
                } else {
                    android.util.Slog.w("ActivityManager", "Process " + proc + " exceeded pss limit " + check + ", but debugging not enabled");
                }
            }
        }
    }

    private final class RecordPssRunnable implements java.lang.Runnable {
        private final android.content.ContentResolver mContentResolver;
        private final android.net.Uri mDumpUri;
        private final com.android.server.am.ProcessProfileRecord mProfile;

        RecordPssRunnable(com.android.server.am.ProcessProfileRecord profile, android.net.Uri dumpUri, android.content.ContentResolver contentResolver) {
            this.mProfile = profile;
            this.mDumpUri = dumpUri;
            this.mContentResolver = contentResolver;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                android.os.ParcelFileDescriptor fd = this.mContentResolver.openFileDescriptor(this.mDumpUri, "rw");
                try {
                    android.app.IApplicationThread thread = this.mProfile.getThread();
                    if (thread != null) {
                        try {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
                                android.util.Slog.d(com.android.server.am.AppProfiler.TAG_PSS, "Requesting dump heap from " + this.mProfile.mApp + " to " + this.mDumpUri.getPath());
                            }
                            thread.dumpHeap(true, false, false, (java.lang.String) null, this.mDumpUri.getPath(), fd, (android.os.RemoteCallback) null);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    if (fd != null) {
                        fd.close();
                    }
                } finally {
                }
            } catch (java.io.IOException e2) {
                android.util.Slog.e("ActivityManager", "Failed to dump heap", e2);
                com.android.server.am.AppProfiler.this.abortHeapDump(this.mProfile.mApp.processName);
            }
        }
    }

    void startHeapDumpLPf(com.android.server.am.ProcessProfileRecord profile, boolean isUserInitiated) {
        com.android.server.am.ProcessRecord proc = profile.mApp;
        this.mMemWatchDumpProcName = proc.processName;
        this.mMemWatchDumpUri = makeHeapDumpUri(proc.processName);
        this.mMemWatchDumpPid = profile.getPid();
        this.mMemWatchDumpUid = proc.uid;
        this.mMemWatchIsUserInitiated = isUserInitiated;
        try {
            android.content.Context ctx = this.mService.mContext.createPackageContextAsUser(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0, android.os.UserHandle.getUserHandleForUid(this.mMemWatchDumpUid));
            com.android.internal.os.BackgroundThread.getHandler().post(new com.android.server.am.AppProfiler.RecordPssRunnable(profile, this.mMemWatchDumpUri, ctx.getContentResolver()));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("android package not found.");
        }
    }

    void dumpHeapFinished(java.lang.String path, int callerPid) {
        synchronized (this.mProfilerLock) {
            if (callerPid != this.mMemWatchDumpPid) {
                android.util.Slog.w("ActivityManager", "dumpHeapFinished: Calling pid " + android.os.Binder.getCallingPid() + " does not match last pid " + this.mMemWatchDumpPid);
                return;
            }
            if (this.mMemWatchDumpUri != null && this.mMemWatchDumpUri.getPath().equals(path)) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
                    android.util.Slog.d(TAG_PSS, "Dump heap finished for " + path);
                }
                this.mService.mHandler.sendEmptyMessage(50);
                java.lang.Runtime.getRuntime().gc();
                return;
            }
            android.util.Slog.w("ActivityManager", "dumpHeapFinished: Calling path " + path + " does not match last path " + this.mMemWatchDumpUri);
        }
    }

    void handlePostDumpHeapNotification() {
        int uid;
        java.lang.String procName;
        long memLimit;
        java.lang.String reportPackage;
        boolean isUserInitiated;
        synchronized (this.mProfilerLock) {
            uid = this.mMemWatchDumpUid;
            procName = this.mMemWatchDumpProcName;
            android.util.Pair<java.lang.Long, java.lang.String> val = (android.util.Pair) this.mMemWatchProcesses.get(procName, uid);
            if (val == null) {
                val = (android.util.Pair) this.mMemWatchProcesses.get(procName, 0);
            }
            if (val != null) {
                memLimit = ((java.lang.Long) val.first).longValue();
                reportPackage = (java.lang.String) val.second;
            } else {
                memLimit = 0;
                reportPackage = null;
            }
            isUserInitiated = this.mMemWatchIsUserInitiated;
            this.mMemWatchDumpUri = null;
            this.mMemWatchDumpProcName = null;
            this.mMemWatchDumpPid = -1;
            this.mMemWatchDumpUid = -1;
        }
        if (procName == null) {
            return;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
            android.util.Slog.d(TAG_PSS, "Showing dump heap notification from " + procName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid);
        }
        android.content.Intent dumpFinishedIntent = new android.content.Intent(ACTION_HEAP_DUMP_FINISHED);
        dumpFinishedIntent.setPackage("com.android.shell");
        dumpFinishedIntent.putExtra("android.intent.extra.UID", uid);
        dumpFinishedIntent.putExtra(EXTRA_HEAP_DUMP_IS_USER_INITIATED, isUserInitiated);
        dumpFinishedIntent.putExtra(EXTRA_HEAP_DUMP_SIZE_BYTES, memLimit);
        dumpFinishedIntent.putExtra(EXTRA_HEAP_DUMP_REPORT_PACKAGE, reportPackage);
        dumpFinishedIntent.putExtra(EXTRA_HEAP_DUMP_PROCESS_NAME, procName);
        this.mService.mContext.sendBroadcastAsUser(dumpFinishedIntent, android.os.UserHandle.getUserHandleForUid(uid));
    }

    void setDumpHeapDebugLimit(java.lang.String processName, int uid, long maxMemSize, java.lang.String reportPackage) {
        synchronized (this.mProfilerLock) {
            try {
                if (maxMemSize > 0) {
                    this.mMemWatchProcesses.put(processName, uid, new android.util.Pair(java.lang.Long.valueOf(maxMemSize), reportPackage));
                } else if (uid != 0) {
                    this.mMemWatchProcesses.remove(processName, uid);
                } else {
                    this.mMemWatchProcesses.getMap().remove(processName);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abortHeapDump(java.lang.String procName) {
        android.os.Message msg = this.mService.mHandler.obtainMessage(51);
        msg.obj = procName;
        this.mService.mHandler.sendMessage(msg);
    }

    void handleAbortDumpHeap(java.lang.String procName) {
        if (procName != null) {
            synchronized (this.mProfilerLock) {
                if (procName.equals(this.mMemWatchDumpProcName)) {
                    this.mMemWatchDumpProcName = null;
                    this.mMemWatchDumpUri = null;
                    this.mMemWatchDumpPid = -1;
                    this.mMemWatchDumpUid = -1;
                }
            }
        }
    }

    private static android.net.Uri makeHeapDumpUri(java.lang.String procName) {
        return android.net.Uri.parse("content://com.android.shell.heapdump/" + procName + "_javaheap.bin");
    }

    private boolean requestPssLPf(com.android.server.am.ProcessProfileRecord profile, int procState) {
        if (this.mPendingPssOrRssProfiles.contains(profile)) {
            return false;
        }
        if (this.mPendingPssOrRssProfiles.size() == 0) {
            long deferral = (this.mPssDeferralTime <= 0 || this.mActivityStartingNesting.get() <= 0) ? 0L : this.mPssDeferralTime;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS && deferral > 0) {
                android.util.Slog.d(TAG_PSS, "requestPssLPf() deferring PSS request by " + deferral + " ms");
            }
            this.mBgHandler.sendEmptyMessageDelayed(1, deferral);
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
            android.util.Slog.d(TAG_PSS, "Requesting pss of: " + profile.mApp);
        }
        profile.setPssProcState(procState);
        profile.setPssStatType(0);
        this.mPendingPssOrRssProfiles.add(profile);
        return true;
    }

    private void deferPssIfNeededLPf() {
        if (this.mPendingPssOrRssProfiles.size() > 0) {
            this.mBgHandler.removeMessages(1);
            this.mBgHandler.sendEmptyMessageDelayed(1, this.mPssDeferralTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deferPssForActivityStart() {
        if (this.mPssDeferralTime > 0) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
                android.util.Slog.d(TAG_PSS, "Deferring PSS collection for activity start");
            }
            synchronized (this.mProfilerLock) {
                deferPssIfNeededLPf();
            }
            this.mActivityStartingNesting.getAndIncrement();
            this.mBgHandler.sendEmptyMessageDelayed(3, this.mPssDeferralTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDeferPss() {
        int nesting = this.mActivityStartingNesting.decrementAndGet();
        if (nesting <= 0) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
                android.util.Slog.d(TAG_PSS, "PSS activity start deferral interval ended; now " + nesting);
            }
            if (nesting < 0) {
                android.util.Slog.wtf("ActivityManager", "Activity start nesting undercount!");
                this.mActivityStartingNesting.incrementAndGet();
                return;
            }
            return;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
            android.util.Slog.d(TAG_PSS, "Still deferring PSS, nesting=" + nesting);
        }
    }

    void requestPssAllProcsLPr(final long now, final boolean always, final boolean memLowered) {
        synchronized (this.mProfilerLock) {
            if (!always) {
                if (now < this.mLastFullPssTime + (memLowered ? this.mService.mConstants.FULL_PSS_LOWERED_INTERVAL : this.mService.mConstants.FULL_PSS_MIN_INTERVAL)) {
                    return;
                }
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PSS) {
                android.util.Slog.d(TAG_PSS, "Requesting pss of all procs!  memLowered=" + memLowered);
            }
            this.mLastFullPssTime = now;
            this.mFullPssOrRssPending = true;
            for (int i = this.mPendingPssOrRssProfiles.size() - 1; i >= 0; i--) {
                this.mPendingPssOrRssProfiles.get(i).abortNextPssTime();
            }
            this.mPendingPssOrRssProfiles.ensureCapacity(this.mService.mProcessList.getLruSizeLOSP());
            this.mPendingPssOrRssProfiles.clear();
            this.mService.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$requestPssAllProcsLPr$2(memLowered, always, now, (com.android.server.am.ProcessRecord) obj);
                }
            });
            if (!this.mBgHandler.hasMessages(1)) {
                this.mBgHandler.sendEmptyMessage(1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestPssAllProcsLPr$2(boolean memLowered, boolean always, long now, com.android.server.am.ProcessRecord app) {
        com.android.server.am.ProcessProfileRecord profile = app.mProfile;
        if (profile.getThread() == null || profile.getSetProcState() == 20) {
            return;
        }
        long lastStateTime = profile.getLastStateTime();
        if (memLowered || ((always && now > 1000 + lastStateTime) || now > 1200000 + lastStateTime)) {
            profile.setPssProcState(profile.getSetProcState());
            profile.setPssStatType(always ? 2 : 1);
            updateNextPssTimeLPf(profile.getSetProcState(), profile, now, true);
            this.mPendingPssOrRssProfiles.add(profile);
        }
    }

    void setTestPssMode(boolean enabled) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mTestPssOrRssMode = enabled;
                if (enabled) {
                    requestPssAllProcsLPr(android.os.SystemClock.uptimeMillis(), true, true);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    boolean getTestPssMode() {
        return this.mTestPssOrRssMode;
    }

    int getLastMemoryLevelLocked() {
        if (this.mMemFactorOverride != -1) {
            return this.mMemFactorOverride;
        }
        return this.mLastMemoryLevel;
    }

    boolean isLastMemoryLevelNormal() {
        return this.mMemFactorOverride != -1 ? this.mMemFactorOverride <= 0 : this.mLastMemoryLevel <= 0;
    }

    void updateLowRamTimestampLPr(long now) {
        this.mLowRamTimeSinceLastIdle = 0L;
        if (this.mLowRamStartTime != 0) {
            this.mLowRamStartTime = now;
        }
    }

    void setAllowLowerMemLevelLocked(boolean allowLowerMemLevel) {
        this.mAllowLowerMemLevel = allowLowerMemLevel;
    }

    boolean allowLowerMemLevelLocked() {
        return this.mAllowLowerMemLevel;
    }

    void setMemFactorOverrideLocked(int factor) {
        this.mMemFactorOverride = factor;
    }

    void updateLowMemStateLSP(int numCached, int numEmpty, int numTrimming, long now) {
        int memFactor;
        if (this.mLowMemDetector != null && this.mLowMemDetector.isAvailable()) {
            memFactor = this.mLowMemDetector.getMemFactor();
        } else if (numCached <= this.mService.mConstants.CUR_TRIM_CACHED_PROCESSES && numEmpty <= this.mService.mConstants.CUR_TRIM_EMPTY_PROCESSES) {
            int numCachedAndEmpty = numCached + numEmpty;
            if (numCachedAndEmpty <= 3) {
                memFactor = 3;
            } else if (numCachedAndEmpty <= 5) {
                memFactor = 2;
            } else {
                memFactor = 1;
            }
        } else {
            memFactor = 0;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            android.util.Slog.d(TAG_OOM_ADJ, "oom: memFactor=" + memFactor + " override=" + this.mMemFactorOverride + " last=" + this.mLastMemoryLevel + " allowLow=" + this.mAllowLowerMemLevel + " numProcs=" + this.mService.mProcessList.getLruSizeLOSP() + " last=" + this.mLastNumProcesses);
        }
        boolean z = this.mMemFactorOverride != -1;
        boolean override = z;
        if (z) {
            memFactor = this.mMemFactorOverride;
        }
        if (memFactor > this.mLastMemoryLevel && !override && (!this.mAllowLowerMemLevel || this.mService.mProcessList.getLruSizeLOSP() >= this.mLastNumProcesses)) {
            memFactor = this.mLastMemoryLevel;
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                android.util.Slog.d(TAG_OOM_ADJ, "Keeping last mem factor!");
            }
        }
        if (memFactor != this.mLastMemoryLevel) {
            com.android.server.am.EventLogTags.writeAmMemFactor(memFactor, this.mLastMemoryLevel);
            com.android.internal.util.FrameworkStatsLog.write(15, memFactor);
            if (!this.mAppProfilerExt.handleMemLevelChanged(this.mLastMemoryLevel, memFactor, this.mBgHandler, 4)) {
                this.mBgHandler.obtainMessage(4, this.mLastMemoryLevel, memFactor).sendToTarget();
            }
        }
        this.mCachedAppsWatermarkData.updateCachedAppsHighWatermarkIfNecessaryLocked(numCached + numEmpty, now);
        synchronized (this.mService.mProcessStats.mLock) {
            this.mService.mProcessStats.setMemFactorLocked(memFactor, this.mService.mAtmInternal == null || !this.mService.mAtmInternal.isSleeping(), android.os.SystemClock.uptimeMillis());
            this.mService.mProcessStats.getMemFactorLocked();
        }
        this.mLastMemoryLevel = memFactor;
        this.mLastNumProcesses = this.mService.mProcessList.getLruSizeLOSP();
        this.mService.mProcessList.forEachLruProcessesLOSP(true, new java.util.function.Consumer() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.am.AppProfiler.lambda$updateLowMemStateLSP$3((com.android.server.am.ProcessRecord) obj);
            }
        });
    }

    static /* synthetic */ void lambda$updateLowMemStateLSP$3(com.android.server.am.ProcessRecord app) {
        android.app.IApplicationThread thread;
        com.android.server.am.ProcessProfileRecord processProfileRecord = app.mProfile;
        com.android.server.am.ProcessStateRecord state = app.mState;
        if (state.hasProcStateChanged()) {
            state.setProcStateChanged(false);
        }
        int procState = app.mState.getCurProcState();
        if (((procState >= 7 && procState < 16) || app.mState.isSystemNoUi()) && app.mProfile.hasPendingUiClean() && (thread = app.getThread()) != null) {
            try {
                thread.scheduleTrimMemory(20);
                app.mProfile.setPendingUiClean(false);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void trimMemoryUiHiddenIfNecessaryLSP(com.android.server.am.ProcessRecord app) {
        if ((app.mState.getCurProcState() >= 7 || app.mState.isSystemNoUi()) && app.mProfile.hasPendingUiClean()) {
            scheduleTrimMemoryLSP(app, 20, "Trimming memory of bg-ui ");
            app.mProfile.setPendingUiClean(false);
        }
    }

    private void scheduleTrimMemoryLSP(com.android.server.am.ProcessRecord app, int level, java.lang.String msg) {
        android.app.IApplicationThread thread;
        if (app.mProfile.getTrimMemoryLevel() < level && (thread = app.getThread()) != null) {
            try {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH || com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                    android.util.Slog.v(TAG_OOM_ADJ, msg + app.processName + " to " + level);
                }
                this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeTemporarily(app, 13);
                thread.scheduleTrimMemory(level);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    long getLowRamTimeSinceIdleLPr(long now) {
        return this.mLowRamTimeSinceLastIdle + (this.mLowRamStartTime > 0 ? now - this.mLowRamStartTime : 0L);
    }

    private void performAppGcLPf(com.android.server.am.ProcessRecord app) {
        try {
            com.android.server.am.ProcessProfileRecord profile = app.mProfile;
            profile.setLastRequestedGc(android.os.SystemClock.uptimeMillis());
            android.app.IApplicationThread thread = profile.getThread();
            if (thread != null) {
                if (profile.getReportLowMemory()) {
                    profile.setReportLowMemory(false);
                    thread.scheduleLowMemory();
                } else {
                    thread.processInBackground();
                }
            }
        } catch (java.lang.Exception e) {
        }
    }

    private void performAppGcsLPf() {
        if (this.mProcessesToGc.size() <= 0) {
            return;
        }
        while (this.mProcessesToGc.size() > 0) {
            com.android.server.am.ProcessRecord proc = this.mProcessesToGc.remove(0);
            com.android.server.am.ProcessProfileRecord profile = proc.mProfile;
            if (profile.getCurRawAdj() > 200 || profile.getReportLowMemory()) {
                if (profile.getLastRequestedGc() + this.mService.mConstants.GC_MIN_INTERVAL <= android.os.SystemClock.uptimeMillis()) {
                    performAppGcLPf(proc);
                    scheduleAppGcsLPf();
                    return;
                } else {
                    addProcessToGcListLPf(proc);
                    scheduleAppGcsLPf();
                }
            }
        }
        scheduleAppGcsLPf();
    }

    final void performAppGcsIfAppropriateLocked() {
        synchronized (this.mProfilerLock) {
            if (this.mService.canGcNowLocked()) {
                performAppGcsLPf();
            } else {
                scheduleAppGcsLPf();
            }
        }
    }

    final void scheduleAppGcsLPf() {
        this.mService.mHandler.removeMessages(5);
        if (this.mProcessesToGc.size() > 0) {
            com.android.server.am.ProcessRecord proc = this.mProcessesToGc.get(0);
            android.os.Message msg = this.mService.mHandler.obtainMessage(5);
            long when = proc.mProfile.getLastRequestedGc() + this.mService.mConstants.GC_MIN_INTERVAL;
            long now = android.os.SystemClock.uptimeMillis();
            if (when < this.mService.mConstants.GC_TIMEOUT + now) {
                when = now + this.mService.mConstants.GC_TIMEOUT;
            }
            this.mService.mHandler.sendMessageAtTime(msg, when);
        }
    }

    private void addProcessToGcListLPf(com.android.server.am.ProcessRecord proc) {
        boolean added = false;
        int i = this.mProcessesToGc.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            if (this.mProcessesToGc.get(i).mProfile.getLastRequestedGc() >= proc.mProfile.getLastRequestedGc()) {
                i--;
            } else {
                added = true;
                this.mProcessesToGc.add(i + 1, proc);
                break;
            }
        }
        if (!added) {
            this.mProcessesToGc.add(0, proc);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x001a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final void doLowMemReportIfNeededLocked(final com.android.server.am.ProcessRecord r15) {
        /*
            r14 = this;
            com.android.server.am.ActivityManagerService r0 = r14.mService
            com.android.server.am.ProcessList r0 = r0.mProcessList
            boolean r0 = r0.haveBackgroundProcessLOSP()
            if (r0 != 0) goto L69
            boolean r0 = android.os.Build.IS_DEBUGGABLE
            r1 = 0
            if (r0 == 0) goto L1a
            java.lang.String r0 = "persist.low_mem.report"
            r2 = 1
            boolean r0 = android.os.SystemProperties.getBoolean(r0, r2)
            if (r0 == 0) goto L1a
            goto L1b
        L1a:
            r2 = r1
        L1b:
            r0 = r2
            long r8 = android.os.SystemClock.uptimeMillis()
            if (r0 == 0) goto L30
            long r2 = r14.mLastMemUsageReportTime
            r4 = 300000(0x493e0, double:1.482197E-318)
            long r2 = r2 + r4
            int r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r2 >= 0) goto L2e
            r0 = 0
            goto L30
        L2e:
            r14.mLastMemUsageReportTime = r8
        L30:
            com.android.server.am.ActivityManagerService r2 = r14.mService
            com.android.server.am.ProcessList r2 = r2.mProcessList
            int r10 = r2.getLruSizeLOSP()
            if (r0 == 0) goto L40
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>(r10)
            goto L41
        L40:
            r2 = 0
        L41:
            r11 = r2
            com.android.server.am.EventLogTags.writeAmLowMemory(r10)
            com.android.server.am.ActivityManagerService r2 = r14.mService
            com.android.server.am.ProcessList r12 = r2.mProcessList
            com.android.server.am.AppProfiler$$ExternalSyntheticLambda4 r13 = new com.android.server.am.AppProfiler$$ExternalSyntheticLambda4
            r2 = r13
            r3 = r14
            r4 = r15
            r5 = r11
            r6 = r8
            r2.<init>()
            r12.forEachLruProcessesLOSP(r1, r13)
            if (r0 == 0) goto L69
            com.android.server.am.ActivityManagerService r1 = r14.mService
            com.android.server.am.ActivityManagerService$MainHandler r1 = r1.mHandler
            r2 = 33
            android.os.Message r1 = r1.obtainMessage(r2, r11)
            com.android.server.am.ActivityManagerService r2 = r14.mService
            com.android.server.am.ActivityManagerService$MainHandler r2 = r2.mHandler
            r2.sendMessage(r1)
        L69:
            java.lang.Object r0 = r14.mProfilerLock
            monitor-enter(r0)
            r14.scheduleAppGcsLPf()     // Catch: java.lang.Throwable -> L71
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L71
            return
        L71:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L71
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppProfiler.doLowMemReportIfNeededLocked(com.android.server.am.ProcessRecord):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doLowMemReportIfNeededLocked$4(com.android.server.am.ProcessRecord dyingProc, java.util.ArrayList memInfos, long now, com.android.server.am.ProcessRecord rec) {
        if (rec == dyingProc || rec.getThread() == null) {
            return;
        }
        com.android.server.am.ProcessStateRecord state = rec.mState;
        if (memInfos != null) {
            memInfos.add(new com.android.server.am.ProcessMemInfo(rec.processName, rec.getPid(), state.getSetAdj(), state.getSetProcState(), state.getAdjType(), state.makeAdjReason()));
        }
        com.android.server.am.ProcessProfileRecord profile = rec.mProfile;
        if (profile.getLastLowMemory() + this.mService.mConstants.GC_MIN_INTERVAL <= now) {
            synchronized (this.mProfilerLock) {
                if (state.getSetAdj() <= 400) {
                    profile.setLastRequestedGc(0L);
                } else {
                    profile.setLastRequestedGc(profile.getLastLowMemory());
                }
                profile.setReportLowMemory(true);
                profile.setLastLowMemory(now);
                this.mProcessesToGc.remove(rec);
                addProcessToGcListLPf(rec);
            }
        }
    }

    void reportMemUsage(java.util.ArrayList<com.android.server.am.ProcessMemInfo> memInfos) {
        long j;
        com.android.server.am.AppProfiler appProfiler;
        long totalMemtrackGl;
        long cachedPss;
        long cachedPss2;
        int i;
        java.lang.StringBuilder shortNativeBuilder;
        java.lang.StringBuilder fullJavaBuilder;
        android.util.SparseArray<com.android.server.am.ProcessMemInfo> infoMap;
        java.util.ArrayList<com.android.server.am.ProcessMemInfo> arrayList = memInfos;
        android.util.SparseArray<com.android.server.am.ProcessMemInfo> infoMap2 = new android.util.SparseArray<>(memInfos.size());
        int size = memInfos.size();
        for (int i2 = 0; i2 < size; i2++) {
            com.android.server.am.ProcessMemInfo mi = arrayList.get(i2);
            infoMap2.put(mi.pid, mi);
        }
        updateCpuStatsNow();
        long[] memtrackTmp = new long[4];
        long[] swaptrackTmp = new long[2];
        java.util.List<com.android.internal.os.ProcessCpuTracker.Stats> stats = getCpuStats(new java.util.function.Predicate() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.am.AppProfiler.lambda$reportMemUsage$5((com.android.internal.os.ProcessCpuTracker.Stats) obj);
            }
        });
        int statsCount = stats.size();
        long totalMemtrackGraphics = 0;
        long totalMemtrackGl2 = 0;
        int i3 = 0;
        while (true) {
            j = 0;
            if (i3 >= statsCount) {
                break;
            }
            com.android.internal.os.ProcessCpuTracker.Stats st = stats.get(i3);
            java.util.List<com.android.internal.os.ProcessCpuTracker.Stats> stats2 = stats;
            int statsCount2 = statsCount;
            long pss = android.os.Debug.getPss(st.pid, swaptrackTmp, memtrackTmp);
            if (pss > 0 && infoMap2.indexOfKey(st.pid) < 0) {
                com.android.server.am.ProcessMemInfo mi2 = new com.android.server.am.ProcessMemInfo(st.name, st.pid, -1000, -1, "native", null);
                mi2.pss = pss;
                mi2.swapPss = swaptrackTmp[1];
                mi2.memtrack = memtrackTmp[0];
                totalMemtrackGraphics += memtrackTmp[1];
                totalMemtrackGl2 += memtrackTmp[2];
                arrayList.add(mi2);
            }
            i3++;
            stats = stats2;
            statsCount = statsCount2;
        }
        long totalPss = 0;
        long totalSwapPss = 0;
        long totalMemtrack = 0;
        int i4 = 0;
        int size2 = memInfos.size();
        while (i4 < size2) {
            com.android.server.am.ProcessMemInfo mi3 = arrayList.get(i4);
            android.util.SparseArray<com.android.server.am.ProcessMemInfo> infoMap3 = infoMap2;
            int size3 = size2;
            if (mi3.pss != j) {
                infoMap = infoMap3;
            } else {
                mi3.pss = android.os.Debug.getPss(mi3.pid, swaptrackTmp, memtrackTmp);
                mi3.swapPss = swaptrackTmp[1];
                infoMap = infoMap3;
                mi3.memtrack = memtrackTmp[0];
                totalMemtrackGraphics += memtrackTmp[1];
                totalMemtrackGl2 += memtrackTmp[2];
            }
            totalPss += mi3.pss;
            totalSwapPss += mi3.swapPss;
            totalMemtrack += mi3.memtrack;
            i4++;
            infoMap2 = infoMap;
            size2 = size3;
            j = 0;
        }
        android.util.SparseArray<com.android.server.am.ProcessMemInfo> infoMap4 = infoMap2;
        java.util.Collections.sort(arrayList, new java.util.Comparator<com.android.server.am.ProcessMemInfo>() { // from class: com.android.server.am.AppProfiler.2
            @Override // java.util.Comparator
            public int compare(com.android.server.am.ProcessMemInfo lhs, com.android.server.am.ProcessMemInfo rhs) {
                if (lhs.oomAdj != rhs.oomAdj) {
                    return lhs.oomAdj < rhs.oomAdj ? -1 : 1;
                }
                if (lhs.pss != rhs.pss) {
                    return lhs.pss < rhs.pss ? 1 : -1;
                }
                return 0;
            }
        });
        java.lang.StringBuilder tag = new java.lang.StringBuilder(128);
        java.lang.StringBuilder stack = new java.lang.StringBuilder(128);
        tag.append("Low on memory -- ");
        com.android.server.am.ActivityManagerService.appendMemBucket(tag, totalPss, "total", false);
        com.android.server.am.ActivityManagerService.appendMemBucket(stack, totalPss, "total", true);
        java.lang.StringBuilder fullNativeBuilder = new java.lang.StringBuilder(1024);
        java.lang.StringBuilder shortNativeBuilder2 = new java.lang.StringBuilder(1024);
        java.lang.StringBuilder fullJavaBuilder2 = new java.lang.StringBuilder(1024);
        long cachedPss3 = 0;
        int size4 = memInfos.size();
        int lastOomAdj = Integer.MIN_VALUE;
        long extraNativeRam = 0;
        long extraNativeMemtrack = 0;
        int lastOomAdj2 = 1;
        int i5 = 0;
        while (true) {
            long[] swaptrackTmp2 = swaptrackTmp;
            if (i5 >= size4) {
                break;
            }
            com.android.server.am.ProcessMemInfo mi4 = arrayList.get(i5);
            android.util.SparseArray<com.android.server.am.ProcessMemInfo> infoMap5 = infoMap4;
            if (mi4.oomAdj < 900) {
                totalMemtrackGl = totalMemtrackGl2;
                cachedPss = cachedPss3;
            } else {
                totalMemtrackGl = totalMemtrackGl2;
                long totalMemtrackGl3 = mi4.pss;
                cachedPss = cachedPss3 + totalMemtrackGl3;
            }
            if (mi4.oomAdj == -1000) {
                cachedPss2 = cachedPss;
            } else if (mi4.oomAdj < 500 || mi4.oomAdj == 600 || mi4.oomAdj == 700) {
                if (lastOomAdj != mi4.oomAdj) {
                    lastOomAdj = mi4.oomAdj;
                    if (mi4.oomAdj <= 0) {
                        tag.append(" / ");
                    }
                    if (mi4.oomAdj >= 0) {
                        if (lastOomAdj2 != 0) {
                            stack.append(":");
                            lastOomAdj2 = 0;
                        }
                        stack.append("\n\t at ");
                    } else {
                        stack.append("$");
                    }
                } else {
                    tag.append(" ");
                    stack.append("$");
                }
                if (mi4.oomAdj > 0) {
                    cachedPss2 = cachedPss;
                } else {
                    cachedPss2 = cachedPss;
                    long cachedPss4 = mi4.pss;
                    com.android.server.am.ActivityManagerService.appendMemBucket(tag, cachedPss4, mi4.name, false);
                }
                long cachedPss5 = mi4.pss;
                com.android.server.am.ActivityManagerService.appendMemBucket(stack, cachedPss5, mi4.name, true);
                if (mi4.oomAdj >= 0 && (i5 + 1 >= size4 || arrayList.get(i5 + 1).oomAdj != lastOomAdj)) {
                    stack.append("(");
                    for (int k = 0; k < com.android.server.am.ActivityManagerService.DUMP_MEM_OOM_ADJ.length; k++) {
                        if (com.android.server.am.ActivityManagerService.DUMP_MEM_OOM_ADJ[k] == mi4.oomAdj) {
                            stack.append(com.android.server.am.ActivityManagerService.DUMP_MEM_OOM_LABEL[k]);
                            stack.append(":");
                            stack.append(com.android.server.am.ActivityManagerService.DUMP_MEM_OOM_ADJ[k]);
                        }
                    }
                    stack.append(")");
                }
            } else {
                cachedPss2 = cachedPss;
            }
            int i6 = lastOomAdj2;
            com.android.server.am.ActivityManagerService.appendMemInfo(fullNativeBuilder, mi4);
            if (mi4.oomAdj != -1000) {
                i = i6;
                shortNativeBuilder = shortNativeBuilder2;
                if (extraNativeRam > 0) {
                    com.android.server.am.ActivityManagerService.appendBasicMemEntry(shortNativeBuilder, -1000, -1, extraNativeRam, extraNativeMemtrack, "(Other native)");
                    shortNativeBuilder.append('\n');
                    extraNativeRam = 0;
                }
                fullJavaBuilder = fullJavaBuilder2;
                com.android.server.am.ActivityManagerService.appendMemInfo(fullJavaBuilder, mi4);
            } else if (mi4.pss >= 512) {
                shortNativeBuilder = shortNativeBuilder2;
                com.android.server.am.ActivityManagerService.appendMemInfo(shortNativeBuilder, mi4);
                i = i6;
                fullJavaBuilder = fullJavaBuilder2;
            } else {
                shortNativeBuilder = shortNativeBuilder2;
                i = i6;
                extraNativeRam += mi4.pss;
                extraNativeMemtrack += mi4.memtrack;
                fullJavaBuilder = fullJavaBuilder2;
            }
            i5++;
            arrayList = memInfos;
            fullJavaBuilder2 = fullJavaBuilder;
            shortNativeBuilder2 = shortNativeBuilder;
            lastOomAdj2 = i;
            swaptrackTmp = swaptrackTmp2;
            infoMap4 = infoMap5;
            totalMemtrackGl2 = totalMemtrackGl;
            cachedPss3 = cachedPss2;
        }
        long totalMemtrackGl4 = totalMemtrackGl2;
        java.lang.StringBuilder shortNativeBuilder3 = shortNativeBuilder2;
        java.lang.StringBuilder fullJavaBuilder3 = fullJavaBuilder2;
        fullJavaBuilder3.append("           ");
        com.android.server.am.ProcessList.appendRamKb(fullJavaBuilder3, totalPss);
        fullJavaBuilder3.append(": TOTAL");
        if (totalMemtrack > 0) {
            fullJavaBuilder3.append(" (");
            fullJavaBuilder3.append(com.android.server.am.ActivityManagerService.stringifyKBSize(totalMemtrack));
            fullJavaBuilder3.append(" memtrack)");
        }
        fullJavaBuilder3.append("\n");
        com.android.internal.util.MemInfoReader memInfo = new com.android.internal.util.MemInfoReader();
        try {
            memInfo.readMemInfo();
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.e("ActivityManager", "readMemInfo :" + e);
        }
        long[] infos = memInfo.getRawInfo();
        java.lang.StringBuilder memInfoBuilder = new java.lang.StringBuilder(1024);
        android.os.Debug.getMemInfo(infos);
        memInfoBuilder.append("  MemInfo: ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[5])).append(" slab, ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[4])).append(" shmem, ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[12])).append(" vm alloc, ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[13])).append(" page tables ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[14])).append(" kernel stack\n");
        memInfoBuilder.append("           ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[2])).append(" buffers, ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[3])).append(" cached, ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[11])).append(" mapped, ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[1])).append(" free\n");
        if (infos[10] != 0) {
            memInfoBuilder.append("  ZRAM: ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[10]));
            memInfoBuilder.append(" RAM, ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[8]));
            memInfoBuilder.append(" swap total, ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(infos[9]));
            memInfoBuilder.append(" swap free\n");
        }
        long[] ksm = com.android.server.am.ActivityManagerService.getKsmInfo();
        if (ksm[1] != 0 || ksm[0] != 0 || ksm[2] != 0 || ksm[3] != 0) {
            memInfoBuilder.append("  KSM: ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(ksm[1]));
            memInfoBuilder.append(" saved from shared ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(ksm[0]));
            memInfoBuilder.append("\n       ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(ksm[2]));
            memInfoBuilder.append(" unshared; ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(ksm[3]));
            memInfoBuilder.append(" volatile\n");
        }
        memInfoBuilder.append("  Free RAM: ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(cachedPss3 + memInfo.getCachedSizeKb() + memInfo.getFreeSizeKb()));
        memInfoBuilder.append("\n");
        long kernelUsed = memInfo.getKernelUsedSizeKb();
        long ionHeap = android.os.Debug.getIonHeapsSizeKb();
        long ionPool = android.os.Debug.getIonPoolsSizeKb();
        long dmabufMapped = android.os.Debug.getDmabufMappedSizeKb();
        if (ionHeap >= 0 && ionPool >= 0) {
            long ionUnmapped = ionHeap - dmabufMapped;
            memInfoBuilder.append("       ION: ");
            memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(ionHeap + ionPool));
            memInfoBuilder.append("\n");
            kernelUsed += ionUnmapped;
            totalPss = (totalPss - totalMemtrackGraphics) + dmabufMapped;
        } else {
            long totalExportedDmabuf = android.os.Debug.getDmabufTotalExportedKb();
            if (totalExportedDmabuf >= 0) {
                long dmabufUnmapped = totalExportedDmabuf - dmabufMapped;
                memInfoBuilder.append("DMA-BUF: ");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(totalExportedDmabuf));
                memInfoBuilder.append("\n");
                kernelUsed += dmabufUnmapped;
                totalPss = (totalPss - totalMemtrackGraphics) + dmabufMapped;
            }
            long totalExportedDmabufHeap = android.os.Debug.getDmabufHeapTotalExportedKb();
            if (totalExportedDmabufHeap >= 0) {
                memInfoBuilder.append("DMA-BUF Heap: ");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(totalExportedDmabufHeap));
                memInfoBuilder.append("\n");
            }
            long totalDmabufHeapPool = android.os.Debug.getDmabufHeapPoolsSizeKb();
            if (totalDmabufHeapPool >= 0) {
                memInfoBuilder.append("DMA-BUF Heaps pool: ");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(totalDmabufHeapPool));
                memInfoBuilder.append("\n");
            }
        }
        long gpuUsage = android.os.Debug.getGpuTotalUsageKb();
        if (gpuUsage >= 0) {
            long gpuPrivateUsage = android.os.Debug.getGpuPrivateMemoryKb();
            if (gpuPrivateUsage >= 0) {
                long gpuDmaBufUsage = gpuUsage - gpuPrivateUsage;
                memInfoBuilder.append("      GPU: ");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(gpuUsage));
                memInfoBuilder.append(" (");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(gpuDmaBufUsage));
                memInfoBuilder.append(" dmabuf + ");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(gpuPrivateUsage));
                memInfoBuilder.append(" private)\n");
                totalPss -= totalMemtrackGl4;
                kernelUsed += gpuPrivateUsage;
            } else {
                memInfoBuilder.append("       GPU: ");
                memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(gpuUsage));
                memInfoBuilder.append("\n");
            }
        }
        memInfoBuilder.append("  Used RAM: ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize((totalPss - cachedPss3) + kernelUsed));
        memInfoBuilder.append("\n");
        memInfoBuilder.append("  Lost RAM: ");
        memInfoBuilder.append(com.android.server.am.ActivityManagerService.stringifyKBSize(((((memInfo.getTotalSizeKb() - (totalPss - totalSwapPss)) - memInfo.getFreeSizeKb()) - memInfo.getCachedSizeKb()) - kernelUsed) - memInfo.getZramTotalSizeKb()));
        memInfoBuilder.append("\n");
        android.util.Slog.i("ActivityManager", "Low on memory:");
        android.util.Slog.i("ActivityManager", shortNativeBuilder3.toString());
        android.util.Slog.i("ActivityManager", fullJavaBuilder3.toString());
        android.util.Slog.i("ActivityManager", memInfoBuilder.toString());
        java.lang.StringBuilder dropBuilder = new java.lang.StringBuilder(1024);
        dropBuilder.append("Low on memory:");
        dropBuilder.append((java.lang.CharSequence) stack);
        dropBuilder.append('\n');
        dropBuilder.append((java.lang.CharSequence) fullNativeBuilder);
        dropBuilder.append((java.lang.CharSequence) fullJavaBuilder3);
        dropBuilder.append('\n');
        dropBuilder.append((java.lang.CharSequence) memInfoBuilder);
        dropBuilder.append('\n');
        if (IS_HIGHTEMP_VERSION || IS_AGING_VERSION) {
            appProfiler = this;
        } else {
            java.io.StringWriter catSw = new java.io.StringWriter();
            appProfiler = this;
            com.android.server.am.ActivityManagerService activityManagerService = appProfiler.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        java.io.PrintWriter catPw = new com.android.internal.util.FastPrintWriter(catSw, false, 256);
                        java.lang.String[] emptyArgs = new java.lang.String[0];
                        catPw.println();
                        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = appProfiler.mProcLock;
                        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                        synchronized (activityManagerGlobalLock) {
                            try {
                                appProfiler.mService.mProcessList.dumpProcessesLSP(null, catPw, emptyArgs, 0, false, null, -1);
                            } catch (java.lang.Throwable th) {
                                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                throw th;
                            }
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        catPw.println();
                        appProfiler.mService.mServices.newServiceDumperLocked(null, catPw, emptyArgs, 0, false, null).dumpLocked();
                        catPw.println();
                        appProfiler.mService.mAtmInternal.dump(com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_CMD, null, catPw, emptyArgs, 0, false, false, null, -1);
                        catPw.flush();
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        dropBuilder.append(catSw.toString());
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        }
        com.android.internal.util.FrameworkStatsLog.write(81);
        appProfiler.mService.addErrorToDropBox("lowmem", null, "system_server", null, null, null, tag.toString(), dropBuilder.toString(), null, null, null, null, null, null);
        com.android.server.am.ActivityManagerService activityManagerService2 = appProfiler.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService2) {
            try {
                try {
                    long now = android.os.SystemClock.uptimeMillis();
                    if (appProfiler.mLastMemUsageReportTime < now) {
                        appProfiler.mLastMemUsageReportTime = now;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    static /* synthetic */ boolean lambda$reportMemUsage$5(com.android.internal.os.ProcessCpuTracker.Stats st) {
        return st.vsize > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMemoryPressureChangedLocked(int oldMemFactor, int newMemFactor) {
        this.mService.mServices.rescheduleServiceRestartOnMemoryPressureIfNeededLocked(oldMemFactor, newMemFactor, "mem-pressure-event", android.os.SystemClock.uptimeMillis());
    }

    private void stopProfilerLPf(com.android.server.am.ProcessRecord proc, int profileType) {
        android.app.IApplicationThread thread;
        if (proc == null || proc == this.mProfileData.getProfileProc()) {
            proc = this.mProfileData.getProfileProc();
            profileType = this.mProfileType;
            clearProfilerLPf();
        }
        if (proc == null || (thread = proc.mProfile.getThread()) == null) {
            return;
        }
        try {
            thread.profilerControl(false, (android.app.ProfilerInfo) null, profileType);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Process disappeared");
        }
    }

    void clearProfilerLPf() {
        if (this.mProfileData.getProfilerInfo() != null && this.mProfileData.getProfilerInfo().profileFd != null) {
            try {
                this.mProfileData.getProfilerInfo().profileFd.close();
            } catch (java.io.IOException e) {
            }
        }
        this.mProfileData.setProfileApp(null);
        this.mProfileData.setProfileProc(null);
        this.mProfileData.setProfilerInfo(null);
    }

    void clearProfilerLPf(com.android.server.am.ProcessRecord app) {
        if (this.mProfileData.getProfileProc() == null || this.mProfileData.getProfilerInfo() == null || this.mProfileData.getProfileProc() != app) {
            return;
        }
        clearProfilerLPf();
    }

    boolean profileControlLPf(com.android.server.am.ProcessRecord proc, boolean start, android.app.ProfilerInfo profilerInfo, int profileType) throws java.io.IOException {
        android.os.ParcelFileDescriptor fd;
        try {
            try {
                if (start) {
                    stopProfilerLPf(null, 0);
                    this.mService.setProfileApp(proc.info, proc.processName, profilerInfo, proc.isSdkSandbox ? proc.getClientInfoForSdkSandbox() : null);
                    this.mProfileData.setProfileProc(proc);
                    this.mProfileType = profileType;
                    android.os.ParcelFileDescriptor fd2 = profilerInfo.profileFd;
                    try {
                        fd = fd2.dup();
                    } catch (java.io.IOException e) {
                        fd = null;
                    }
                    profilerInfo.profileFd = fd;
                    proc.mProfile.getThread().profilerControl(start, profilerInfo, profileType);
                    try {
                        this.mProfileData.getProfilerInfo().profileFd.close();
                    } catch (java.io.IOException e2) {
                    }
                    this.mProfileData.getProfilerInfo().profileFd = null;
                    if (proc.getPid() == com.android.server.am.ActivityManagerService.MY_PID) {
                        profilerInfo = null;
                    }
                } else {
                    stopProfilerLPf(proc, profileType);
                }
                if (profilerInfo == null || profilerInfo.profileFd == null) {
                    return true;
                }
                try {
                    profilerInfo.profileFd.close();
                    return true;
                } catch (java.io.IOException e3) {
                    return true;
                }
            } catch (android.os.RemoteException e4) {
                throw new java.lang.IllegalStateException("Process disappeared");
            }
        } finally {
            if (profilerInfo != null && profilerInfo.profileFd != null) {
                try {
                    profilerInfo.profileFd.close();
                } catch (java.io.IOException e5) {
                }
            }
        }
    }

    void setProfileAppLPf(java.lang.String processName, android.app.ProfilerInfo profilerInfo) {
        this.mProfileData.setProfileApp(processName);
        if (this.mProfileData.getProfilerInfo() != null && this.mProfileData.getProfilerInfo().profileFd != null) {
            try {
                this.mProfileData.getProfilerInfo().profileFd.close();
            } catch (java.io.IOException e) {
            }
        }
        this.mProfileData.setProfilerInfo(new android.app.ProfilerInfo(profilerInfo));
        this.mProfileType = 0;
    }

    void setProfileProcLPf(com.android.server.am.ProcessRecord proc) {
        this.mProfileData.setProfileProc(proc);
    }

    void setAgentAppLPf(java.lang.String packageName, java.lang.String agent) {
        if (agent == null) {
            if (this.mAppAgentMap != null) {
                this.mAppAgentMap.remove(packageName);
                if (this.mAppAgentMap.isEmpty()) {
                    this.mAppAgentMap = null;
                    return;
                }
                return;
            }
            return;
        }
        if (this.mAppAgentMap == null) {
            this.mAppAgentMap = new java.util.HashMap();
        }
        if (this.mAppAgentMap.size() >= 100) {
            android.util.Slog.e("ActivityManager", "App agent map has too many entries, cannot add " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + agent);
        } else {
            this.mAppAgentMap.put(packageName, agent);
        }
    }

    void updateCpuStats() {
        long now = android.os.SystemClock.uptimeMillis();
        if (this.mLastCpuTime.get() < now - MONITOR_CPU_MIN_TIME && this.mProcessCpuMutexFree.compareAndSet(true, false)) {
            synchronized (this.mProcessCpuThread) {
                this.mProcessCpuThread.notify();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 28, insn: 0x0307: MOVE (r2 I:??[long, double]) = (r28 I:??[long, double] A[D('now' long)]), block:B:126:0x0307 */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0165 A[Catch: all -> 0x01aa, TRY_ENTER, TRY_LEAVE, TryCatch #11 {all -> 0x01aa, blocks: (B:49:0x013a, B:58:0x0165), top: B:174:0x013a }] */
    /* JADX WARN: Type inference failed for: r9v39 */
    /* JADX WARN: Type inference failed for: r9v40 */
    /* JADX WARN: Type inference failed for: r9v41 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void updateCpuStatsNow() {
        /*
            Method dump skipped, instruction units count: 907
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppProfiler.updateCpuStatsNow():void");
    }

    long getCpuTimeForPid(int pid) {
        return this.mProcessCpuTracker.getCpuTimeForPid(pid);
    }

    long getCpuDelayTimeForPid(int pid) {
        return this.mProcessCpuTracker.getCpuDelayTimeForPid(pid);
    }

    java.util.List<com.android.internal.os.ProcessCpuTracker.Stats> getCpuStats(final java.util.function.Predicate<com.android.internal.os.ProcessCpuTracker.Stats> predicate) {
        java.util.List<com.android.internal.os.ProcessCpuTracker.Stats> stats;
        synchronized (this.mProcessCpuTracker) {
            stats = this.mProcessCpuTracker.getStats(new com.android.internal.os.ProcessCpuTracker.FilterStats() { // from class: com.android.server.am.AppProfiler$$ExternalSyntheticLambda6
                public final boolean needed(com.android.internal.os.ProcessCpuTracker.Stats stats2) {
                    return predicate.test(stats2);
                }
            });
        }
        return stats;
    }

    void forAllCpuStats(java.util.function.Consumer<com.android.internal.os.ProcessCpuTracker.Stats> consumer) {
        synchronized (this.mProcessCpuTracker) {
            int numOfStats = this.mProcessCpuTracker.countStats();
            for (int i = 0; i < numOfStats; i++) {
                consumer.accept(this.mProcessCpuTracker.getStats(i));
            }
        }
    }

    private class ProcessCpuThread extends java.lang.Thread {
        ProcessCpuThread(java.lang.String name) {
            super(name);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (com.android.server.am.AppProfiler.this.mProcessCpuTracker) {
                try {
                    com.android.server.am.AppProfiler.this.mProcessCpuInitLatch.countDown();
                    com.android.server.am.AppProfiler.this.mProcessCpuTracker.init();
                } finally {
                    th = th;
                    while (true) {
                        try {
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    }
                }
            }
            while (true) {
                try {
                    try {
                        synchronized (this) {
                            long now = android.os.SystemClock.uptimeMillis();
                            long nextCpuDelay = (com.android.server.am.AppProfiler.this.mLastCpuTime.get() + com.android.server.am.AppProfiler.MONITOR_CPU_MAX_TIME) - now;
                            long nextWriteDelay = (com.android.server.am.AppProfiler.this.mLastWriteTime + 1800000) - now;
                            if (nextWriteDelay < nextCpuDelay) {
                                nextCpuDelay = nextWriteDelay;
                            }
                            if (nextCpuDelay > 0) {
                                com.android.server.am.AppProfiler.this.mProcessCpuMutexFree.set(true);
                                wait(nextCpuDelay);
                            }
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e("ActivityManager", "Unexpected exception collecting process stats", e);
                    }
                } catch (java.lang.InterruptedException e2) {
                }
                com.android.server.am.AppProfiler.this.updateCpuStatsNow();
            }
        }
    }

    class CpuBinder extends android.os.Binder {
        private final com.android.server.utils.PriorityDump.PriorityDumper mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.am.AppProfiler.CpuBinder.1
            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
                if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.am.AppProfiler.this.mService.mContext, "cpuinfo", pw)) {
                    return;
                }
                synchronized (com.android.server.am.AppProfiler.this.mProcessCpuTracker) {
                    if (asProto) {
                        com.android.server.am.AppProfiler.this.mProcessCpuTracker.dumpProto(fd);
                        return;
                    }
                    pw.print(com.android.server.am.AppProfiler.this.mProcessCpuTracker.printCurrentLoad());
                    pw.print(com.android.server.am.AppProfiler.this.mProcessCpuTracker.printCurrentState(android.os.SystemClock.uptimeMillis()));
                    com.android.server.am.AppProfiler.this.mProcessCpuTracker.mProcessCpuTrackerExt.printCpuTrack(pw);
                }
            }
        };

        CpuBinder() {
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            com.android.server.utils.PriorityDump.dump(this.mPriorityDumper, fd, pw, args);
        }
    }

    void setCpuInfoService() {
        android.os.ServiceManager.addService("cpuinfo", new com.android.server.am.AppProfiler.CpuBinder(), false, 1);
    }

    AppProfiler(com.android.server.am.ActivityManagerService service, android.os.Looper bgLooper, com.android.server.am.LowMemDetector detector) {
        this.mService = service;
        this.mProcLock = service.mProcLock;
        this.mBgHandler = new com.android.server.am.AppProfiler.BgHandler(bgLooper);
        this.mLowMemDetector = detector;
        this.mProcessCpuTracker.mProcessCpuTrackerExt.initHandler(this.mBgHandler);
    }

    void retrieveSettings() {
        long pssDeferralMs = android.provider.DeviceConfig.getLong("activity_manager", ACTIVITY_START_PSS_DEFER_CONFIG, 0L);
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager", android.app.ActivityThread.currentApplication().getMainExecutor(), this.mPssDelayConfigListener);
        this.mPssDeferralTime = pssDeferralMs;
    }

    void onActivityManagerInternalAdded() {
        this.mProcessCpuThread.start();
        try {
            this.mProcessCpuInitLatch.await();
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.wtf("ActivityManager", "Interrupted wait during start", e);
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException("Interrupted wait during start");
        }
    }

    void onActivityLaunched() {
        if (this.mPssDeferralTime > 0) {
            android.os.Message msg = this.mBgHandler.obtainMessage(2);
            this.mBgHandler.sendMessageAtFrontOfQueue(msg);
        }
    }

    android.app.ProfilerInfo setupProfilerInfoLocked(android.app.IApplicationThread thread, com.android.server.am.ProcessRecord app, com.android.server.am.ActiveInstrumentation instr) throws java.io.IOException, android.os.RemoteException {
        android.app.ProfilerInfo profilerInfo = null;
        java.lang.String preBindAgent = null;
        java.lang.String processName = app.processName;
        synchronized (this.mProfilerLock) {
            if (this.mProfileData.getProfileApp() != null && this.mProfileData.getProfileApp().equals(processName)) {
                this.mProfileData.setProfileProc(app);
                if (this.mProfileData.getProfilerInfo() != null) {
                    boolean needsInfo = this.mProfileData.getProfilerInfo().profileFile != null || this.mProfileData.getProfilerInfo().attachAgentDuringBind;
                    profilerInfo = needsInfo ? new android.app.ProfilerInfo(this.mProfileData.getProfilerInfo()) : null;
                    if (this.mProfileData.getProfilerInfo().agent != null) {
                        preBindAgent = this.mProfileData.getProfilerInfo().agent;
                    }
                }
            } else if (instr != null && instr.mProfileFile != null) {
                profilerInfo = new android.app.ProfilerInfo(instr.mProfileFile, (android.os.ParcelFileDescriptor) null, 0, false, false, (java.lang.String) null, false, 0, 1);
            }
            if (this.mAppAgentMap != null && this.mAppAgentMap.containsKey(processName) && app.isDebuggable()) {
                this.mAppAgentMap.get(processName);
                if (profilerInfo == null) {
                    profilerInfo = new android.app.ProfilerInfo((java.lang.String) null, (android.os.ParcelFileDescriptor) null, 0, false, false, this.mAppAgentMap.get(processName), true, 0, 1);
                } else if (profilerInfo.agent == null) {
                    profilerInfo = profilerInfo.setAgent(this.mAppAgentMap.get(processName), true);
                }
            }
            if (profilerInfo != null && profilerInfo.profileFd != null) {
                profilerInfo.profileFd = profilerInfo.profileFd.dup();
                if (android.text.TextUtils.equals(this.mProfileData.getProfileApp(), processName) && this.mProfileData.getProfilerInfo() != null) {
                    clearProfilerLPf();
                }
            }
        }
        if (this.mService.mActiveInstrumentation.size() > 0 && instr == null) {
            for (int i = this.mService.mActiveInstrumentation.size() - 1; i >= 0 && app.getActiveInstrumentation() == null; i--) {
                com.android.server.am.ActiveInstrumentation aInstr = this.mService.mActiveInstrumentation.get(i);
                if (!aInstr.mFinished && aInstr.mTargetInfo.uid == app.uid) {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            if (aInstr.mTargetProcesses.length == 0) {
                                if (aInstr.mTargetInfo.packageName.equals(app.info.packageName)) {
                                    app.setActiveInstrumentation(aInstr);
                                    aInstr.mRunningProcesses.add(app);
                                }
                            } else {
                                java.lang.String[] strArr = aInstr.mTargetProcesses;
                                int length = strArr.length;
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= length) {
                                        break;
                                    }
                                    java.lang.String proc = strArr[i2];
                                    if (!proc.equals(app.processName)) {
                                        i2++;
                                    } else {
                                        app.setActiveInstrumentation(aInstr);
                                        aInstr.mRunningProcesses.add(app);
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
                }
            }
        }
        if (preBindAgent != null) {
            thread.attachAgent(preBindAgent);
        }
        if (app.isDebuggable()) {
            thread.attachStartupAgents(app.info.dataDir);
        }
        return profilerInfo;
    }

    void onCleanupApplicationRecordLocked(com.android.server.am.ProcessRecord app) {
        synchronized (this.mProfilerLock) {
            com.android.server.am.ProcessProfileRecord profile = app.mProfile;
            this.mProcessesToGc.remove(app);
            this.mPendingPssOrRssProfiles.remove(profile);
            profile.abortNextPssTime();
        }
    }

    void onAppDiedLocked(com.android.server.am.ProcessRecord app) {
        synchronized (this.mProfilerLock) {
            if (this.mProfileData.getProfileProc() == app) {
                clearProfilerLPf();
            }
        }
    }

    boolean dumpMemWatchProcessesLPf(java.io.PrintWriter pw, boolean needSep) {
        if (this.mMemWatchProcesses.getMap().size() > 0) {
            pw.println("  Mem watch processes:");
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.String>>> procs = this.mMemWatchProcesses.getMap();
            for (int i = procs.size() - 1; i >= 0; i--) {
                java.lang.String proc = procs.keyAt(i);
                android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.String>> uids = procs.valueAt(i);
                for (int j = uids.size() - 1; j >= 0; j--) {
                    if (needSep) {
                        pw.println();
                        needSep = false;
                    }
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append("    ").append(proc).append('/');
                    android.os.UserHandle.formatUid(sb, uids.keyAt(j));
                    android.util.Pair<java.lang.Long, java.lang.String> val = uids.valueAt(j);
                    sb.append(": ");
                    android.util.DebugUtils.sizeValueToString(((java.lang.Long) val.first).longValue(), sb);
                    if (val.second != null) {
                        sb.append(", report to ").append((java.lang.String) val.second);
                    }
                    pw.println(sb.toString());
                }
            }
            pw.print("  mMemWatchDumpProcName=");
            pw.println(this.mMemWatchDumpProcName);
            pw.print("  mMemWatchDumpUri=");
            pw.println(this.mMemWatchDumpUri);
            pw.print("  mMemWatchDumpPid=");
            pw.println(this.mMemWatchDumpPid);
            pw.print("  mMemWatchDumpUid=");
            pw.println(this.mMemWatchDumpUid);
            pw.print("  mMemWatchIsUserInitiated=");
            pw.println(this.mMemWatchIsUserInitiated);
        }
        return needSep;
    }

    boolean dumpProfileDataLocked(java.io.PrintWriter pw, java.lang.String dumpPackage, boolean needSep) {
        if ((this.mProfileData.getProfileApp() != null || this.mProfileData.getProfileProc() != null || (this.mProfileData.getProfilerInfo() != null && (this.mProfileData.getProfilerInfo().profileFile != null || this.mProfileData.getProfilerInfo().profileFd != null))) && (dumpPackage == null || dumpPackage.equals(this.mProfileData.getProfileApp()))) {
            if (needSep) {
                pw.println();
                needSep = false;
            }
            pw.println("  mProfileApp=" + this.mProfileData.getProfileApp() + " mProfileProc=" + this.mProfileData.getProfileProc());
            if (this.mProfileData.getProfilerInfo() != null) {
                pw.println("  mProfileFile=" + this.mProfileData.getProfilerInfo().profileFile + " mProfileFd=" + this.mProfileData.getProfilerInfo().profileFd);
                pw.println("  mSamplingInterval=" + this.mProfileData.getProfilerInfo().samplingInterval + " mAutoStopProfiler=" + this.mProfileData.getProfilerInfo().autoStopProfiler + " mStreamingOutput=" + this.mProfileData.getProfilerInfo().streamingOutput + " mClockType=" + this.mProfileData.getProfilerInfo().clockType + " mProfilerOutputVersion=" + this.mProfileData.getProfilerInfo().profilerOutputVersion);
                pw.println("  mProfileType=" + this.mProfileType);
            }
        }
        return needSep;
    }

    void dumpLastMemoryLevelLocked(java.io.PrintWriter pw) {
        switch (this.mLastMemoryLevel) {
            case 0:
                pw.println("normal)");
                break;
            case 1:
                pw.println("moderate)");
                break;
            case 2:
                pw.println("low)");
                break;
            case 3:
                pw.println("critical)");
                break;
            default:
                pw.print(this.mLastMemoryLevel);
                pw.println(")");
                break;
        }
    }

    void dumpMemoryLevelsLocked(java.io.PrintWriter pw) {
        pw.println("  mAllowLowerMemLevel=" + this.mAllowLowerMemLevel + " mLastMemoryLevel=" + this.mLastMemoryLevel + " mLastNumProcesses=" + this.mLastNumProcesses);
    }

    void writeMemWatchProcessToProtoLPf(android.util.proto.ProtoOutputStream proto) {
        if (this.mMemWatchProcesses.getMap().size() > 0) {
            long token = proto.start(1146756268064L);
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.String>>> procs = this.mMemWatchProcesses.getMap();
            for (int i = 0; i < procs.size(); i++) {
                java.lang.String proc = procs.keyAt(i);
                android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.String>> uids = procs.valueAt(i);
                long ptoken = proto.start(2246267895809L);
                proto.write(1138166333441L, proc);
                int j = uids.size() - 1;
                while (j >= 0) {
                    long utoken = proto.start(2246267895810L);
                    android.util.Pair<java.lang.Long, java.lang.String> val = uids.valueAt(j);
                    proto.write(1120986464257L, uids.keyAt(j));
                    proto.write(1138166333442L, android.util.DebugUtils.sizeValueToString(((java.lang.Long) val.first).longValue(), new java.lang.StringBuilder()));
                    proto.write(1138166333443L, (java.lang.String) val.second);
                    proto.end(utoken);
                    j--;
                    procs = procs;
                    token = token;
                }
                proto.end(ptoken);
            }
            long token2 = token;
            long dtoken = proto.start(1146756268034L);
            proto.write(1138166333441L, this.mMemWatchDumpProcName);
            proto.write(1138166333446L, this.mMemWatchDumpUri.toString());
            proto.write(1120986464259L, this.mMemWatchDumpPid);
            proto.write(1120986464260L, this.mMemWatchDumpUid);
            proto.write(1133871366149L, this.mMemWatchIsUserInitiated);
            proto.end(dtoken);
            proto.end(token2);
        }
    }

    void writeProfileDataToProtoLocked(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage) {
        if (this.mProfileData.getProfileApp() == null && this.mProfileData.getProfileProc() == null) {
            if (this.mProfileData.getProfilerInfo() != null) {
                if (this.mProfileData.getProfilerInfo().profileFile == null && this.mProfileData.getProfilerInfo().profileFd == null) {
                    return;
                }
            } else {
                return;
            }
        }
        if (dumpPackage == null || dumpPackage.equals(this.mProfileData.getProfileApp())) {
            long token = proto.start(1146756268066L);
            proto.write(1138166333441L, this.mProfileData.getProfileApp());
            this.mProfileData.getProfileProc().dumpDebug(proto, 1146756268034L);
            if (this.mProfileData.getProfilerInfo() != null) {
                this.mProfileData.getProfilerInfo().dumpDebug(proto, 1146756268035L);
                proto.write(1120986464260L, this.mProfileType);
            }
            proto.end(token);
        }
    }

    void writeMemoryLevelsToProtoLocked(android.util.proto.ProtoOutputStream proto) {
        proto.write(1133871366199L, this.mAllowLowerMemLevel);
        proto.write(1120986464312L, this.mLastMemoryLevel);
        proto.write(1120986464313L, this.mLastNumProcesses);
    }

    void printCurrentCpuState(java.lang.StringBuilder report, long time) {
        synchronized (this.mProcessCpuTracker) {
            report.append(this.mProcessCpuTracker.printCurrentState(time, 10));
        }
    }

    android.util.Pair<java.lang.String, java.lang.String> getAppProfileStatsForDebugging(long time, int linesOfStats) {
        java.lang.String cpuLoad;
        java.lang.String stats;
        synchronized (this.mProcessCpuTracker) {
            updateCpuStatsNow();
            cpuLoad = this.mProcessCpuTracker.printCurrentLoad();
            stats = this.mProcessCpuTracker.printCurrentState(time);
        }
        int toIndex = 0;
        int i = 0;
        while (true) {
            if (i > linesOfStats) {
                break;
            }
            int nextIndex = stats.indexOf(10, toIndex);
            if (nextIndex == -1) {
                toIndex = stats.length();
                break;
            }
            toIndex = nextIndex + 1;
            i++;
        }
        return new android.util.Pair<>(cpuLoad, stats.substring(0, toIndex));
    }

    void writeProcessesToGcToProto(android.util.proto.ProtoOutputStream proto, long fieldId, java.lang.String dumpPackage) {
        if (this.mProcessesToGc.size() > 0) {
            long now = android.os.SystemClock.uptimeMillis();
            int size = this.mProcessesToGc.size();
            for (int i = 0; i < size; i++) {
                com.android.server.am.ProcessRecord r = this.mProcessesToGc.get(i);
                if (dumpPackage == null || dumpPackage.equals(r.info.packageName)) {
                    long token = proto.start(fieldId);
                    com.android.server.am.ProcessProfileRecord profile = r.mProfile;
                    r.dumpDebug(proto, 1146756268033L);
                    proto.write(1133871366146L, profile.getReportLowMemory());
                    proto.write(1112396529667L, now);
                    proto.write(1112396529668L, profile.getLastRequestedGc());
                    proto.write(1112396529669L, profile.getLastLowMemory());
                    proto.end(token);
                }
            }
        }
    }

    boolean dumpProcessesToGc(java.io.PrintWriter pw, boolean needSep, java.lang.String dumpPackage) {
        if (this.mProcessesToGc.size() > 0) {
            boolean printed = false;
            long now = android.os.SystemClock.uptimeMillis();
            int size = this.mProcessesToGc.size();
            for (int i = 0; i < size; i++) {
                com.android.server.am.ProcessRecord proc = this.mProcessesToGc.get(i);
                if (dumpPackage == null || dumpPackage.equals(proc.info.packageName)) {
                    if (!printed) {
                        if (needSep) {
                            pw.println();
                        }
                        needSep = true;
                        pw.println("  Processes that are waiting to GC:");
                        printed = true;
                    }
                    pw.print("    Process ");
                    pw.println(proc);
                    com.android.server.am.ProcessProfileRecord profile = proc.mProfile;
                    pw.print("      lowMem=");
                    pw.print(profile.getReportLowMemory());
                    pw.print(", last gced=");
                    pw.print(now - profile.getLastRequestedGc());
                    pw.print(" ms ago, last lowMem=");
                    pw.print(now - profile.getLastLowMemory());
                    pw.println(" ms ago");
                }
            }
        }
        return needSep;
    }
}
