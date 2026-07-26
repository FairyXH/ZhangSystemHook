package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ProcessProfileRecord {
    final com.android.server.am.ProcessRecord mApp;
    private com.android.internal.app.procstats.ProcessState mBaseProcessTracker;
    private com.android.server.power.stats.BatteryStatsImpl.Uid.Proc mCurProcBatteryStats;
    private int mCurRawAdj;
    private long mInitialIdlePssOrRss;
    private long mLastCachedPss;
    private long mLastCachedRss;
    private long mLastCachedSwapPss;
    private long mLastLowMemory;
    private android.os.Debug.MemoryInfo mLastMemInfo;
    private long mLastMemInfoTime;
    private long mLastPss;
    private long mLastPssTime;
    private long mLastRequestedGc;
    private long mLastRss;
    private long mLastStateTime;
    private long mLastSwapPss;
    private long mNextPssTime;
    private boolean mPendingUiClean;
    private int mPid;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    final java.lang.Object mProfilerLock;
    private int mPssStatType;
    private boolean mReportLowMemory;
    private final com.android.server.am.ActivityManagerService mService;
    private int mSetAdj;
    private int mSetProcState;
    private android.app.IApplicationThread mThread;
    private int mTrimMemoryLevel;
    private final com.android.server.am.ProcessList.ProcStateMemTracker mProcStateMemTracker = new com.android.server.am.ProcessList.ProcStateMemTracker();
    private int mPssProcState = 20;
    final java.util.concurrent.atomic.AtomicLong mLastCpuTime = new java.util.concurrent.atomic.AtomicLong(0);
    final java.util.concurrent.atomic.AtomicLong mCurCpuTime = new java.util.concurrent.atomic.AtomicLong(0);
    final java.util.concurrent.atomic.AtomicLong mLastCpuDelayTime = new java.util.concurrent.atomic.AtomicLong(0);
    private java.util.concurrent.atomic.AtomicInteger mCurrentHostingComponentTypes = new java.util.concurrent.atomic.AtomicInteger(0);
    private java.util.concurrent.atomic.AtomicInteger mHistoricalHostingComponentTypes = new java.util.concurrent.atomic.AtomicInteger(0);

    ProcessProfileRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
        this.mProcLock = this.mService.mProcLock;
        this.mProfilerLock = this.mService.mAppProfiler.mProfilerLock;
    }

    void init(long now) {
        this.mNextPssTime = now;
        this.mLastPssTime = now;
    }

    com.android.internal.app.procstats.ProcessState getBaseProcessTracker() {
        return this.mBaseProcessTracker;
    }

    void setBaseProcessTracker(com.android.internal.app.procstats.ProcessState baseProcessTracker) {
        this.mBaseProcessTracker = baseProcessTracker;
    }

    void onProcessFrozen() {
        synchronized (this.mService.mProcessStats.mLock) {
            com.android.internal.app.procstats.ProcessState tracker = this.mBaseProcessTracker;
            if (tracker != null) {
                com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                long now = android.os.SystemClock.uptimeMillis();
                synchronized (pkgList) {
                    tracker.onProcessFrozen(now, pkgList.getPackageListLocked());
                }
            }
        }
    }

    void onProcessUnfrozen() {
        synchronized (this.mService.mProcessStats.mLock) {
            com.android.internal.app.procstats.ProcessState tracker = this.mBaseProcessTracker;
            if (tracker != null) {
                com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                long now = android.os.SystemClock.uptimeMillis();
                synchronized (pkgList) {
                    tracker.onProcessUnfrozen(now, pkgList.getPackageListLocked());
                }
            }
        }
    }

    void onProcessActive(android.app.IApplicationThread thread, final com.android.server.am.ProcessStatsService tracker) {
        if (this.mThread == null) {
            synchronized (this.mProfilerLock) {
                try {
                    synchronized (tracker.mLock) {
                        try {
                            final com.android.internal.app.procstats.ProcessState origBase = getBaseProcessTracker();
                            com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                            if (origBase != null) {
                                synchronized (pkgList) {
                                    origBase.setState(-1, tracker.getMemFactorLocked(), android.os.SystemClock.uptimeMillis(), pkgList.getPackageListLocked());
                                }
                                origBase.makeInactive();
                            }
                            android.content.pm.ApplicationInfo info = this.mApp.info;
                            final int attributionUid = getUidForAttribution(this.mApp);
                            final com.android.internal.app.procstats.ProcessState baseProcessTracker = tracker.getProcessStateLocked(info.packageName, attributionUid, info.longVersionCode, this.mApp.processName);
                            setBaseProcessTracker(baseProcessTracker);
                            baseProcessTracker.makeActive();
                            pkgList.forEachPackage(new java.util.function.BiConsumer() { // from class: com.android.server.am.ProcessProfileRecord$$ExternalSyntheticLambda1
                                @Override // java.util.function.BiConsumer
                                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                                    this.f$0.lambda$onProcessActive$0(origBase, tracker, attributionUid, baseProcessTracker, (java.lang.String) obj, (com.android.internal.app.procstats.ProcessStats.ProcessStateHolder) obj2);
                                }
                            });
                            this.mThread = thread;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            return;
        }
        synchronized (this.mProfilerLock) {
            this.mThread = thread;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onProcessActive$0(com.android.internal.app.procstats.ProcessState origBase, com.android.server.am.ProcessStatsService tracker, int attributionUid, com.android.internal.app.procstats.ProcessState baseProcessTracker, java.lang.String pkgName, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder) {
        if (holder.state != null && holder.state != origBase) {
            holder.state.makeInactive();
        }
        tracker.updateProcessStateHolderLocked(holder, pkgName, attributionUid, this.mApp.info.longVersionCode, this.mApp.processName);
        if (holder.state != baseProcessTracker) {
            holder.state.makeActive();
        }
    }

    void onProcessInactive(com.android.server.am.ProcessStatsService tracker) {
        synchronized (this.mProfilerLock) {
            synchronized (tracker.mLock) {
                final com.android.internal.app.procstats.ProcessState origBase = getBaseProcessTracker();
                if (origBase != null) {
                    com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                    synchronized (pkgList) {
                        origBase.setState(-1, tracker.getMemFactorLocked(), android.os.SystemClock.uptimeMillis(), pkgList.getPackageListLocked());
                    }
                    origBase.makeInactive();
                    setBaseProcessTracker(null);
                    pkgList.forEachPackageProcessStats(new java.util.function.Consumer() { // from class: com.android.server.am.ProcessProfileRecord$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.am.ProcessProfileRecord.lambda$onProcessInactive$1(origBase, (com.android.internal.app.procstats.ProcessStats.ProcessStateHolder) obj);
                        }
                    });
                }
                this.mThread = null;
            }
        }
        this.mCurrentHostingComponentTypes.set(0);
        this.mHistoricalHostingComponentTypes.set(0);
    }

    static /* synthetic */ void lambda$onProcessInactive$1(com.android.internal.app.procstats.ProcessState origBase, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder) {
        if (holder.state != null && holder.state != origBase) {
            holder.state.makeInactive();
        }
        holder.pkg = null;
        holder.state = null;
    }

    long getLastPssTime() {
        return this.mLastPssTime;
    }

    void setLastPssTime(long lastPssTime) {
        this.mLastPssTime = lastPssTime;
    }

    long getNextPssTime() {
        return this.mNextPssTime;
    }

    void setNextPssTime(long nextPssTime) {
        this.mNextPssTime = nextPssTime;
    }

    long getInitialIdlePssOrRss() {
        return this.mInitialIdlePssOrRss;
    }

    void setInitialIdlePssOrRss(long initialIdlePssOrRss) {
        this.mInitialIdlePssOrRss = initialIdlePssOrRss;
    }

    long getLastPss() {
        return this.mLastPss;
    }

    void setLastPss(long lastPss) {
        this.mLastPss = lastPss;
    }

    long getLastCachedPss() {
        return this.mLastCachedPss;
    }

    void setLastCachedPss(long lastCachedPss) {
        this.mLastCachedPss = lastCachedPss;
    }

    long getLastCachedRss() {
        return this.mLastCachedRss;
    }

    void setLastCachedRss(long lastCachedRss) {
        this.mLastCachedRss = lastCachedRss;
    }

    long getLastSwapPss() {
        return this.mLastSwapPss;
    }

    void setLastSwapPss(long lastSwapPss) {
        this.mLastSwapPss = lastSwapPss;
    }

    long getLastCachedSwapPss() {
        return this.mLastCachedSwapPss;
    }

    void setLastCachedSwapPss(long lastCachedSwapPss) {
        this.mLastCachedSwapPss = lastCachedSwapPss;
    }

    long getLastRss() {
        return this.mLastRss;
    }

    void setLastRss(long lastRss) {
        this.mLastRss = lastRss;
    }

    android.os.Debug.MemoryInfo getLastMemInfo() {
        return this.mLastMemInfo;
    }

    void setLastMemInfo(android.os.Debug.MemoryInfo lastMemInfo) {
        this.mLastMemInfo = lastMemInfo;
    }

    long getLastMemInfoTime() {
        return this.mLastMemInfoTime;
    }

    void setLastMemInfoTime(long lastMemInfoTime) {
        this.mLastMemInfoTime = lastMemInfoTime;
    }

    int getPssProcState() {
        return this.mPssProcState;
    }

    void setPssProcState(int pssProcState) {
        this.mPssProcState = pssProcState;
    }

    int getPssStatType() {
        return this.mPssStatType;
    }

    void setPssStatType(int pssStatType) {
        this.mPssStatType = pssStatType;
    }

    int getTrimMemoryLevel() {
        return this.mTrimMemoryLevel;
    }

    void setTrimMemoryLevel(int trimMemoryLevel) {
        this.mTrimMemoryLevel = trimMemoryLevel;
    }

    boolean hasPendingUiClean() {
        return this.mPendingUiClean;
    }

    void setPendingUiClean(boolean pendingUiClean) {
        this.mPendingUiClean = pendingUiClean;
        this.mApp.getWindowProcessController().setPendingUiClean(pendingUiClean);
    }

    com.android.server.power.stats.BatteryStatsImpl.Uid.Proc getCurProcBatteryStats() {
        return this.mCurProcBatteryStats;
    }

    void setCurProcBatteryStats(com.android.server.power.stats.BatteryStatsImpl.Uid.Proc curProcBatteryStats) {
        this.mCurProcBatteryStats = curProcBatteryStats;
    }

    long getLastRequestedGc() {
        return this.mLastRequestedGc;
    }

    void setLastRequestedGc(long lastRequestedGc) {
        this.mLastRequestedGc = lastRequestedGc;
    }

    long getLastLowMemory() {
        return this.mLastLowMemory;
    }

    void setLastLowMemory(long lastLowMemory) {
        this.mLastLowMemory = lastLowMemory;
    }

    boolean getReportLowMemory() {
        return this.mReportLowMemory;
    }

    void setReportLowMemory(boolean reportLowMemory) {
        this.mReportLowMemory = reportLowMemory;
    }

    void addPss(long pss, long uss, long rss, boolean always, int type, long duration) {
        synchronized (this.mService.mProcessStats.mLock) {
            com.android.internal.app.procstats.ProcessState tracker = this.mBaseProcessTracker;
            if (tracker != null) {
                com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                synchronized (pkgList) {
                    tracker.addPss(pss, uss, rss, always, type, duration, pkgList.getPackageListLocked());
                }
            }
        }
    }

    void reportExcessiveCpu() {
        synchronized (this.mService.mProcessStats.mLock) {
            com.android.internal.app.procstats.ProcessState tracker = this.mBaseProcessTracker;
            if (tracker != null) {
                com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                synchronized (pkgList) {
                    tracker.reportExcessiveCpu(pkgList.getPackageListLocked());
                }
            }
        }
    }

    void setProcessTrackerState(int procState, int memFactor) {
        synchronized (this.mService.mProcessStats.mLock) {
            com.android.internal.app.procstats.ProcessState tracker = this.mBaseProcessTracker;
            if (tracker != null && procState != 20) {
                com.android.server.am.PackageList pkgList = this.mApp.getPkgList();
                long now = android.os.SystemClock.uptimeMillis();
                synchronized (pkgList) {
                    tracker.setState(procState, memFactor, now, pkgList.getPackageListLocked());
                }
            }
        }
    }

    void commitNextPssTime() {
        commitNextPssTime(this.mProcStateMemTracker);
    }

    void abortNextPssTime() {
        abortNextPssTime(this.mProcStateMemTracker);
    }

    long computeNextPssTime(int procState, boolean test, boolean sleeping, long now) {
        return com.android.server.am.ProcessList.computeNextPssTime(procState, this.mProcStateMemTracker, test, sleeping, now, java.lang.Math.max(this.mService.mBootCompletedTimestamp, this.mService.mLastIdleTime) + this.mService.mConstants.FULL_PSS_MIN_INTERVAL);
    }

    private static void commitNextPssTime(com.android.server.am.ProcessList.ProcStateMemTracker tracker) {
        if (tracker.mPendingMemState >= 0) {
            tracker.mHighestMem[tracker.mPendingMemState] = tracker.mPendingHighestMemState;
            tracker.mScalingFactor[tracker.mPendingMemState] = tracker.mPendingScalingFactor;
            tracker.mTotalHighestMem = tracker.mPendingHighestMemState;
            tracker.mPendingMemState = -1;
        }
    }

    private static void abortNextPssTime(com.android.server.am.ProcessList.ProcStateMemTracker tracker) {
        tracker.mPendingMemState = -1;
    }

    private static int getUidForAttribution(com.android.server.am.ProcessRecord processRecord) {
        if (android.os.Process.isIsolatedUid(processRecord.uid)) {
            return processRecord.info.uid;
        }
        return processRecord.uid;
    }

    int getPid() {
        return this.mPid;
    }

    void setPid(int pid) {
        this.mPid = pid;
    }

    android.app.IApplicationThread getThread() {
        return this.mThread;
    }

    int getSetProcState() {
        return this.mSetProcState;
    }

    int getSetAdj() {
        return this.mSetAdj;
    }

    int getCurRawAdj() {
        return this.mCurRawAdj;
    }

    long getLastStateTime() {
        return this.mLastStateTime;
    }

    void updateProcState(com.android.server.am.ProcessStateRecord state) {
        this.mSetProcState = state.getCurProcState();
        this.mSetAdj = state.getCurAdj();
        this.mCurRawAdj = state.getCurRawAdj();
        this.mLastStateTime = state.getLastStateTime();
    }

    void addHostingComponentType(int type) {
        this.mCurrentHostingComponentTypes.set(this.mCurrentHostingComponentTypes.get() | type);
        this.mHistoricalHostingComponentTypes.set(this.mHistoricalHostingComponentTypes.get() | type);
    }

    void clearHostingComponentType(int type) {
        this.mCurrentHostingComponentTypes.set(this.mCurrentHostingComponentTypes.get() & (~type));
    }

    int getCurrentHostingComponentTypes() {
        return this.mCurrentHostingComponentTypes.get();
    }

    int getHistoricalHostingComponentTypes() {
        return this.mHistoricalHostingComponentTypes.get();
    }

    void dumpPss(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        synchronized (this.mProfilerLock) {
            if (this.mService.mAppProfiler.isProfilingPss()) {
                pw.print(prefix);
                pw.print("lastPssTime=");
                android.util.TimeUtils.formatDuration(this.mLastPssTime, nowUptime, pw);
                pw.print(" pssProcState=");
                pw.print(this.mPssProcState);
                pw.print(" pssStatType=");
                pw.print(this.mPssStatType);
                pw.print(" nextPssTime=");
                android.util.TimeUtils.formatDuration(this.mNextPssTime, nowUptime, pw);
                pw.println();
                pw.print(prefix);
                pw.print("lastPss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastPss * 1024);
                pw.print(" lastSwapPss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastSwapPss * 1024);
                pw.print(" lastCachedPss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastCachedPss * 1024);
                pw.print(" lastCachedSwapPss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastCachedSwapPss * 1024);
                pw.print(" lastRss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastRss * 1024);
            } else {
                pw.print(prefix);
                pw.print("lastRssTime=");
                android.util.TimeUtils.formatDuration(this.mLastPssTime, nowUptime, pw);
                pw.print(" rssProcState=");
                pw.print(this.mPssProcState);
                pw.print(" rssStatType=");
                pw.print(this.mPssStatType);
                pw.print(" nextRssTime=");
                android.util.TimeUtils.formatDuration(this.mNextPssTime, nowUptime, pw);
                pw.println();
                pw.print(prefix);
                pw.print("lastRss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastRss * 1024);
                pw.print(" lastCachedRss=");
                android.util.DebugUtils.printSizeValue(pw, this.mLastCachedRss * 1024);
            }
            pw.println();
            pw.print(prefix);
            pw.print("trimMemoryLevel=");
            pw.println(this.mTrimMemoryLevel);
            pw.print(prefix);
            pw.print("procStateMemTracker: ");
            this.mProcStateMemTracker.dumpLine(pw);
            pw.print(prefix);
            pw.print("lastRequestedGc=");
            android.util.TimeUtils.formatDuration(this.mLastRequestedGc, nowUptime, pw);
            pw.print(" lastLowMemory=");
            android.util.TimeUtils.formatDuration(this.mLastLowMemory, nowUptime, pw);
            pw.print(" reportLowMemory=");
            pw.println(this.mReportLowMemory);
        }
        pw.print(prefix);
        pw.print("currentHostingComponentTypes=0x");
        pw.print(java.lang.Integer.toHexString(getCurrentHostingComponentTypes()));
        pw.print(" historicalHostingComponentTypes=0x");
        pw.println(java.lang.Integer.toHexString(getHistoricalHostingComponentTypes()));
    }

    void dumpCputime(java.io.PrintWriter pw, java.lang.String prefix) {
        long lastCpuTime = this.mLastCpuTime.get();
        pw.print(prefix);
        pw.print("lastCpuTime=");
        pw.print(lastCpuTime);
        if (lastCpuTime > 0) {
            pw.print(" timeUsed=");
            android.util.TimeUtils.formatDuration(this.mCurCpuTime.get() - lastCpuTime, pw);
        }
        pw.println();
    }
}
