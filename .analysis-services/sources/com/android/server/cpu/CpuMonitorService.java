package com.android.server.cpu;

/* JADX INFO: loaded from: classes.dex */
public final class CpuMonitorService extends com.android.server.SystemService {
    private static final long CACHE_DURATION_MILLISECONDS;
    static final long DEFAULT_MONITORING_INTERVAL_MILLISECONDS = -1;
    private static final long LATEST_AVAILABILITY_DURATION_MILLISECONDS;
    private final android.util.SparseArrayMap<com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback, com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo> mAvailabilityCallbackInfosByCallbacksByCpuset;
    private final android.content.Context mContext;
    private final com.android.server.cpu.CpuInfoReader mCpuInfoReader;
    private final android.util.SparseArray<com.android.server.cpu.CpuMonitorService.CpusetInfo> mCpusetInfosByCpuset;
    private long mCurrentMonitoringIntervalMillis;
    private final long mDebugMonitoringIntervalMillis;
    private android.os.Handler mHandler;
    private final android.os.HandlerThread mHandlerThread;
    private final long mLatestAvailabilityDurationMillis;
    private final com.android.server.cpu.CpuMonitorInternal mLocalService;
    private final java.lang.Object mLock;
    private final java.lang.Runnable mMonitorCpuStats;
    private final long mNormalMonitoringIntervalMillis;
    private final boolean mShouldDebugMonitor;
    static final java.lang.String TAG = com.android.server.cpu.CpuMonitorService.class.getSimpleName();
    static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final long NORMAL_MONITORING_INTERVAL_MILLISECONDS = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    private static final long DEBUG_MONITORING_INTERVAL_MILLISECONDS = java.util.concurrent.TimeUnit.MINUTES.toMillis(1);

    static {
        CACHE_DURATION_MILLISECONDS = (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) ? java.util.concurrent.TimeUnit.MINUTES.toMillis(30L) : java.util.concurrent.TimeUnit.MINUTES.toMillis(10L);
        LATEST_AVAILABILITY_DURATION_MILLISECONDS = java.util.concurrent.TimeUnit.SECONDS.toMillis(30L);
    }

    public CpuMonitorService(android.content.Context context) {
        com.android.server.cpu.CpuInfoReader cpuInfoReader = new com.android.server.cpu.CpuInfoReader();
        boolean z = true;
        com.android.server.ServiceThread serviceThread = new com.android.server.ServiceThread(TAG, 10, true);
        if (!android.os.Build.IS_USERDEBUG && !android.os.Build.IS_ENG) {
            z = false;
        }
        this(context, cpuInfoReader, serviceThread, z, NORMAL_MONITORING_INTERVAL_MILLISECONDS, DEBUG_MONITORING_INTERVAL_MILLISECONDS, LATEST_AVAILABILITY_DURATION_MILLISECONDS);
    }

    CpuMonitorService(android.content.Context context, com.android.server.cpu.CpuInfoReader cpuInfoReader, android.os.HandlerThread handlerThread, boolean shouldDebugMonitor, long normalMonitoringIntervalMillis, long debugMonitoringIntervalMillis, long latestAvailabilityDurationMillis) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mMonitorCpuStats = new java.lang.Runnable() { // from class: com.android.server.cpu.CpuMonitorService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.monitorCpuStats();
            }
        };
        this.mCurrentMonitoringIntervalMillis = -1L;
        this.mLocalService = new com.android.server.cpu.CpuMonitorInternal() { // from class: com.android.server.cpu.CpuMonitorService.1
            @Override // com.android.server.cpu.CpuMonitorInternal
            public void addCpuAvailabilityCallback(java.util.concurrent.Executor executor, com.android.server.cpu.CpuAvailabilityMonitoringConfig config, com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback callback) {
                com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo;
                java.util.Objects.requireNonNull(callback, "Callback must be non-null");
                java.util.Objects.requireNonNull(config, "Config must be non-null");
                synchronized (com.android.server.cpu.CpuMonitorService.this.mLock) {
                    for (int i = 0; i < com.android.server.cpu.CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
                        int cpuset = com.android.server.cpu.CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.keyAt(i);
                        com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo2 = (com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo) com.android.server.cpu.CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.delete(cpuset, callback);
                        if (callbackInfo2 != null) {
                            com.android.server.utils.Slogf.i(com.android.server.cpu.CpuMonitorService.TAG, "Overwriting the existing %s", callbackInfo2);
                        }
                    }
                    callbackInfo = com.android.server.cpu.CpuMonitorService.this.newCallbackInfoLocked(config, callback, executor);
                }
                com.android.server.cpu.CpuMonitorService.this.asyncNotifyMonitoringIntervalChangeToClient(callbackInfo);
                if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Successfully added %s", callbackInfo);
                }
            }

            @Override // com.android.server.cpu.CpuMonitorInternal
            public void removeCpuAvailabilityCallback(com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback callback) {
                synchronized (com.android.server.cpu.CpuMonitorService.this.mLock) {
                    for (int i = 0; i < com.android.server.cpu.CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
                        int cpuset = com.android.server.cpu.CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.keyAt(i);
                        com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo = (com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo) com.android.server.cpu.CpuMonitorService.this.mAvailabilityCallbackInfosByCallbacksByCpuset.delete(cpuset, callback);
                        if (callbackInfo != null) {
                            if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                                com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Successfully removed %s", callbackInfo);
                            }
                            com.android.server.cpu.CpuMonitorService.this.checkAndStopMonitoringLocked();
                            return;
                        }
                    }
                    com.android.server.utils.Slogf.w(com.android.server.cpu.CpuMonitorService.TAG, "CpuAvailabilityCallback was not previously added. Ignoring the remove request");
                }
            }
        };
        this.mContext = context;
        this.mHandlerThread = handlerThread;
        this.mShouldDebugMonitor = shouldDebugMonitor;
        this.mNormalMonitoringIntervalMillis = normalMonitoringIntervalMillis;
        this.mDebugMonitoringIntervalMillis = debugMonitoringIntervalMillis;
        this.mLatestAvailabilityDurationMillis = latestAvailabilityDurationMillis;
        this.mCpuInfoReader = cpuInfoReader;
        this.mCpusetInfosByCpuset = new android.util.SparseArray<>(2);
        this.mCpusetInfosByCpuset.append(1, new com.android.server.cpu.CpuMonitorService.CpusetInfo(1));
        this.mCpusetInfosByCpuset.append(2, new com.android.server.cpu.CpuMonitorService.CpusetInfo(2));
        this.mAvailabilityCallbackInfosByCallbacksByCpuset = new android.util.SparseArrayMap<>();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        if (!this.mCpuInfoReader.init() || this.mCpuInfoReader.readCpuInfos() == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Failed to initialize CPU info reader. This happens when the CPU frequency stats are not available or the sysfs interface has changed in the Kernel. Cannot monitor CPU without these stats. Terminating CPU monitor service");
            return;
        }
        this.mHandlerThread.start();
        this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper());
        publishLocalService(com.android.server.cpu.CpuMonitorInternal.class, this.mLocalService);
        publishBinderService("cpu_monitor", new com.android.server.cpu.CpuMonitorService.CpuMonitorBinder(), false, 1);
        com.android.server.Watchdog.getInstance().addThread(this.mHandler);
        synchronized (this.mLock) {
            if (this.mShouldDebugMonitor && !this.mHandler.hasCallbacks(this.mMonitorCpuStats)) {
                this.mCurrentMonitoringIntervalMillis = this.mDebugMonitoringIntervalMillis;
                com.android.server.utils.Slogf.i(TAG, "Starting debug monitoring");
                this.mHandler.post(this.mMonitorCpuStats);
            }
        }
    }

    long getCurrentMonitoringIntervalMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mCurrentMonitoringIntervalMillis;
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDump(final android.util.IndentingPrintWriter writer) {
        writer.printf("*%s*\n", new java.lang.Object[]{getClass().getSimpleName()});
        writer.increaseIndent();
        this.mCpuInfoReader.dump(writer);
        writer.printf("mShouldDebugMonitor = %s\n", new java.lang.Object[]{this.mShouldDebugMonitor ? "Yes" : "No"});
        writer.printf("mNormalMonitoringIntervalMillis = %d\n", new java.lang.Object[]{java.lang.Long.valueOf(this.mNormalMonitoringIntervalMillis)});
        writer.printf("mDebugMonitoringIntervalMillis = %d\n", new java.lang.Object[]{java.lang.Long.valueOf(this.mDebugMonitoringIntervalMillis)});
        writer.printf("mLatestAvailabilityDurationMillis = %d\n", new java.lang.Object[]{java.lang.Long.valueOf(this.mLatestAvailabilityDurationMillis)});
        synchronized (this.mLock) {
            writer.printf("mCurrentMonitoringIntervalMillis = %d\n", new java.lang.Object[]{java.lang.Long.valueOf(this.mCurrentMonitoringIntervalMillis)});
            if (hasClientCallbacksLocked()) {
                writer.println("CPU availability change callbacks:");
                writer.increaseIndent();
                this.mAvailabilityCallbackInfosByCallbacksByCpuset.forEach(new java.util.function.Consumer() { // from class: com.android.server.cpu.CpuMonitorService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        writer.printf("%s\n", new java.lang.Object[]{(com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo) obj});
                    }
                });
                writer.decreaseIndent();
            }
            if (this.mCpusetInfosByCpuset.size() > 0) {
                writer.println("Cpuset infos:");
                writer.increaseIndent();
                for (int i = 0; i < this.mCpusetInfosByCpuset.size(); i++) {
                    writer.printf("%s\n", new java.lang.Object[]{this.mCpusetInfosByCpuset.valueAt(i)});
                }
                writer.decreaseIndent();
            }
        }
        writer.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void monitorCpuStats() {
        long uptimeMillis = android.os.SystemClock.uptimeMillis();
        this.mHandler.removeCallbacks(this.mMonitorCpuStats);
        android.util.SparseArray<com.android.server.cpu.CpuInfoReader.CpuInfo> cpuInfosByCoreId = this.mCpuInfoReader.readCpuInfos();
        if (cpuInfosByCoreId == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Failed to read CPU info from device");
            synchronized (this.mLock) {
                stopMonitoringCpuStatsLocked();
            }
            return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < cpuInfosByCoreId.size(); i++) {
                com.android.server.cpu.CpuInfoReader.CpuInfo cpuInfo = cpuInfosByCoreId.valueAt(i);
                for (int j = 0; j < this.mCpusetInfosByCpuset.size(); j++) {
                    this.mCpusetInfosByCpuset.valueAt(j).appendCpuInfo(uptimeMillis, cpuInfo);
                }
            }
            for (int i2 = 0; i2 < this.mCpusetInfosByCpuset.size(); i2++) {
                com.android.server.cpu.CpuMonitorService.CpusetInfo cpusetInfo = this.mCpusetInfosByCpuset.valueAt(i2);
                cpusetInfo.populateLatestCpuAvailabilityInfo(uptimeMillis, this.mLatestAvailabilityDurationMillis);
                checkClientThresholdsAndNotifyLocked(cpusetInfo);
            }
            if (this.mCurrentMonitoringIntervalMillis > 0 && (hasClientCallbacksLocked() || this.mShouldDebugMonitor)) {
                this.mHandler.postAtTime(this.mMonitorCpuStats, this.mCurrentMonitoringIntervalMillis + uptimeMillis);
            } else {
                stopMonitoringCpuStatsLocked();
            }
        }
    }

    private void checkClientThresholdsAndNotifyLocked(com.android.server.cpu.CpuMonitorService.CpusetInfo cpusetInfo) {
        int prevAvailabilityPercent = cpusetInfo.getPrevCpuAvailabilityPercent();
        com.android.server.cpu.CpuAvailabilityInfo latestAvailabilityInfo = cpusetInfo.getLatestCpuAvailabilityInfo();
        if (latestAvailabilityInfo == null || prevAvailabilityPercent < 0 || this.mAvailabilityCallbackInfosByCallbacksByCpuset.numElementsForKey(cpusetInfo.cpuset) == 0) {
            return;
        }
        for (int i = 0; i < this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
            for (int j = 0; j < this.mAvailabilityCallbackInfosByCallbacksByCpuset.numElementsForKeyAt(i); j++) {
                com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo = (com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo) this.mAvailabilityCallbackInfosByCallbacksByCpuset.valueAt(i, j);
                if (callbackInfo.config.cpuset == cpusetInfo.cpuset && didCrossAnyThreshold(prevAvailabilityPercent, latestAvailabilityInfo.latestAvgAvailabilityPercent, callbackInfo.config.getThresholds())) {
                    asyncNotifyCpuAvailabilityToClient(latestAvailabilityInfo, callbackInfo);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void asyncNotifyMonitoringIntervalChangeToClient(com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo) {
        if (callbackInfo.executor == null) {
            this.mHandler.post(callbackInfo.notifyMonitoringIntervalChangeRunnable);
        } else {
            callbackInfo.executor.execute(callbackInfo.notifyMonitoringIntervalChangeRunnable);
        }
    }

    private void asyncNotifyCpuAvailabilityToClient(com.android.server.cpu.CpuAvailabilityInfo availabilityInfo, com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo) {
        callbackInfo.notifyCpuAvailabilityChangeRunnable.prepare(availabilityInfo);
        if (callbackInfo.executor == null) {
            this.mHandler.post(callbackInfo.notifyCpuAvailabilityChangeRunnable);
        } else {
            callbackInfo.executor.execute(callbackInfo.notifyCpuAvailabilityChangeRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo newCallbackInfoLocked(com.android.server.cpu.CpuAvailabilityMonitoringConfig config, com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback callback, java.util.concurrent.Executor executor) {
        com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo callbackInfo = new com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo(this, config, callback, executor);
        java.lang.String cpusetStr = com.android.server.cpu.CpuAvailabilityMonitoringConfig.toCpusetString(callbackInfo.config.cpuset);
        com.android.server.cpu.CpuMonitorService.CpusetInfo cpusetInfo = this.mCpusetInfosByCpuset.get(callbackInfo.config.cpuset);
        com.android.internal.util.Preconditions.checkState(cpusetInfo != null, "Missing cpuset info for cpuset %s", new java.lang.Object[]{cpusetStr});
        boolean hasExistingClientCallbacks = hasClientCallbacksLocked();
        this.mAvailabilityCallbackInfosByCallbacksByCpuset.add(callbackInfo.config.cpuset, callbackInfo.callback, callbackInfo);
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Added a CPU availability callback: %s", callbackInfo);
        }
        com.android.server.cpu.CpuAvailabilityInfo latestInfo = cpusetInfo.getLatestCpuAvailabilityInfo();
        if (latestInfo != null) {
            asyncNotifyCpuAvailabilityToClient(latestInfo, callbackInfo);
        }
        if (hasExistingClientCallbacks && this.mHandler.hasCallbacks(this.mMonitorCpuStats)) {
            return callbackInfo;
        }
        this.mHandler.removeCallbacks(this.mMonitorCpuStats);
        this.mCurrentMonitoringIntervalMillis = this.mNormalMonitoringIntervalMillis;
        this.mHandler.post(this.mMonitorCpuStats);
        return callbackInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndStopMonitoringLocked() {
        if (hasClientCallbacksLocked()) {
            return;
        }
        if (this.mShouldDebugMonitor) {
            if (DEBUG) {
                com.android.server.utils.Slogf.e(TAG, "Switching to debug monitoring");
            }
            this.mCurrentMonitoringIntervalMillis = this.mDebugMonitoringIntervalMillis;
            return;
        }
        stopMonitoringCpuStatsLocked();
    }

    private boolean hasClientCallbacksLocked() {
        for (int i = 0; i < this.mAvailabilityCallbackInfosByCallbacksByCpuset.numMaps(); i++) {
            if (this.mAvailabilityCallbackInfosByCallbacksByCpuset.numElementsForKeyAt(i) > 0) {
                return true;
            }
        }
        return false;
    }

    private void stopMonitoringCpuStatsLocked() {
        this.mHandler.removeCallbacks(this.mMonitorCpuStats);
        this.mCurrentMonitoringIntervalMillis = -1L;
        for (int i = 0; i < this.mCpusetInfosByCpuset.size(); i++) {
            this.mCpusetInfosByCpuset.valueAt(i).clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean containsCpuset(int cpusetCategories, int expectedCpuset) {
        switch (expectedCpuset) {
            case 1:
                return (cpusetCategories & 1) != 0;
            case 2:
                return (cpusetCategories & 2) != 0;
            default:
                com.android.server.utils.Slogf.wtf(TAG, "Provided invalid expectedCpuset %d", java.lang.Integer.valueOf(expectedCpuset));
                return false;
        }
    }

    private static boolean didCrossAnyThreshold(int prevAvailabilityPercent, int curAvailabilityPercent, android.util.IntArray thresholds) {
        if (prevAvailabilityPercent == curAvailabilityPercent) {
            return false;
        }
        for (int i = 0; i < thresholds.size(); i++) {
            int threshold = thresholds.get(i);
            if (prevAvailabilityPercent < threshold && curAvailabilityPercent >= threshold) {
                return true;
            }
            if (prevAvailabilityPercent >= threshold && curAvailabilityPercent < threshold) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class CpuAvailabilityCallbackInfo {
        public final com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback callback;
        public final com.android.server.cpu.CpuAvailabilityMonitoringConfig config;
        public final java.util.concurrent.Executor executor;
        public final com.android.server.cpu.CpuMonitorService service;
        public final java.lang.Runnable notifyMonitoringIntervalChangeRunnable = new java.lang.Runnable() { // from class: com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.1
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.this.callback.onMonitoringIntervalChanged(com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.this.service.getCurrentMonitoringIntervalMillis());
            }
        };
        public final com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.NotifyCpuAvailabilityChangeRunnable notifyCpuAvailabilityChangeRunnable = new com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.NotifyCpuAvailabilityChangeRunnable();

        CpuAvailabilityCallbackInfo(com.android.server.cpu.CpuMonitorService service, com.android.server.cpu.CpuAvailabilityMonitoringConfig config, com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback callback, java.util.concurrent.Executor executor) {
            this.service = service;
            this.config = config;
            this.callback = callback;
            this.executor = executor;
        }

        public java.lang.String toString() {
            return "CpuAvailabilityCallbackInfo{config = " + this.config + ", callback = " + this.callback + ", mExecutor = " + this.executor + '}';
        }

        private final class NotifyCpuAvailabilityChangeRunnable implements java.lang.Runnable {
            private com.android.server.cpu.CpuAvailabilityInfo mCpuAvailabilityInfo;
            private final java.lang.Object mLock;

            private NotifyCpuAvailabilityChangeRunnable() {
                this.mLock = new java.lang.Object();
            }

            public void prepare(com.android.server.cpu.CpuAvailabilityInfo cpuAvailabilityInfo) {
                synchronized (this.mLock) {
                    this.mCpuAvailabilityInfo = cpuAvailabilityInfo;
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (this.mLock) {
                    com.android.server.cpu.CpuMonitorService.CpuAvailabilityCallbackInfo.this.callback.onAvailabilityChanged(this.mCpuAvailabilityInfo);
                }
            }
        }
    }

    private final class CpuMonitorBinder extends android.os.Binder {
        private final com.android.server.utils.PriorityDump.PriorityDumper mPriorityDumper;

        private CpuMonitorBinder() {
            this.mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.cpu.CpuMonitorService.CpuMonitorBinder.1
                @Override // com.android.server.utils.PriorityDump.PriorityDumper
                public void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
                    if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.cpu.CpuMonitorService.this.mContext, com.android.server.cpu.CpuMonitorService.TAG, pw) || asProto) {
                        return;
                    }
                    android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
                    try {
                        com.android.server.cpu.CpuMonitorService.this.doDump(ipw);
                        ipw.close();
                    } catch (java.lang.Throwable th) {
                        try {
                            ipw.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            };
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            com.android.server.utils.PriorityDump.dump(this.mPriorityDumper, fd, pw, args);
        }
    }

    private static final class CpusetInfo {
        public final int cpuset;
        private com.android.server.cpu.CpuAvailabilityInfo mLatestCpuAvailabilityInfo;
        private final android.util.LongSparseArray<com.android.server.cpu.CpuMonitorService.CpusetInfo.Snapshot> mSnapshotsByUptime = new android.util.LongSparseArray<>();

        CpusetInfo(int cpuset) {
            this.cpuset = cpuset;
        }

        public void appendCpuInfo(long uptimeMillis, com.android.server.cpu.CpuInfoReader.CpuInfo cpuInfo) {
            if (!com.android.server.cpu.CpuMonitorService.containsCpuset(cpuInfo.cpusetCategories, this.cpuset)) {
                return;
            }
            com.android.server.cpu.CpuMonitorService.CpusetInfo.Snapshot currentSnapshot = this.mSnapshotsByUptime.get(uptimeMillis);
            if (currentSnapshot == null) {
                currentSnapshot = new com.android.server.cpu.CpuMonitorService.CpusetInfo.Snapshot(uptimeMillis);
                this.mSnapshotsByUptime.append(uptimeMillis, currentSnapshot);
                if (this.mSnapshotsByUptime.size() > 0 && uptimeMillis - this.mSnapshotsByUptime.valueAt(0).uptimeMillis > com.android.server.cpu.CpuMonitorService.CACHE_DURATION_MILLISECONDS) {
                    this.mSnapshotsByUptime.removeAt(0);
                }
            }
            currentSnapshot.appendCpuInfo(cpuInfo);
        }

        public com.android.server.cpu.CpuAvailabilityInfo getLatestCpuAvailabilityInfo() {
            return this.mLatestCpuAvailabilityInfo;
        }

        public void populateLatestCpuAvailabilityInfo(long currentUptimeMillis, long latestAvailabilityDurationMillis) {
            int numSnapshots = this.mSnapshotsByUptime.size();
            if (numSnapshots == 0) {
                this.mLatestCpuAvailabilityInfo = null;
                return;
            }
            com.android.server.cpu.CpuMonitorService.CpusetInfo.Snapshot latestSnapshot = this.mSnapshotsByUptime.valueAt(numSnapshots - 1);
            if (latestSnapshot.uptimeMillis != currentUptimeMillis) {
                if (com.android.server.cpu.CpuMonitorService.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.cpu.CpuMonitorService.TAG, "Skipping stale CPU availability information for cpuset %s", com.android.server.cpu.CpuAvailabilityMonitoringConfig.toCpusetString(this.cpuset));
                }
                this.mLatestCpuAvailabilityInfo = null;
            } else {
                if (this.mLatestCpuAvailabilityInfo != null && this.mLatestCpuAvailabilityInfo.dataTimestampUptimeMillis == latestSnapshot.uptimeMillis) {
                    return;
                }
                long earliestUptimeMillis = currentUptimeMillis - latestAvailabilityDurationMillis;
                this.mLatestCpuAvailabilityInfo = new com.android.server.cpu.CpuAvailabilityInfo(this.cpuset, latestSnapshot.uptimeMillis, latestSnapshot.getAverageAvailableCpuFreqPercent(), getCumulativeAvgAvailabilityPercent(earliestUptimeMillis), latestAvailabilityDurationMillis);
            }
        }

        public int getPrevCpuAvailabilityPercent() {
            int numSnapshots = this.mSnapshotsByUptime.size();
            if (numSnapshots < 2) {
                return -1;
            }
            return this.mSnapshotsByUptime.valueAt(numSnapshots - 2).getAverageAvailableCpuFreqPercent();
        }

        private int getCumulativeAvgAvailabilityPercent(long earliestUptimeMillis) {
            long totalAvailableCpuFreqKHz = 0;
            long totalOnlineMaxCpuFreqKHz = 0;
            int totalAccountedSnapshots = 0;
            long earliestSeenUptimeMillis = Long.MAX_VALUE;
            for (int i = this.mSnapshotsByUptime.size() - 1; i >= 0; i--) {
                com.android.server.cpu.CpuMonitorService.CpusetInfo.Snapshot snapshot = this.mSnapshotsByUptime.valueAt(i);
                earliestSeenUptimeMillis = snapshot.uptimeMillis;
                if (snapshot.uptimeMillis <= earliestUptimeMillis) {
                    break;
                }
                totalAccountedSnapshots++;
                totalAvailableCpuFreqKHz += snapshot.totalNormalizedAvailableCpuFreqKHz;
                totalOnlineMaxCpuFreqKHz += snapshot.totalOnlineMaxCpuFreqKHz;
            }
            if (earliestSeenUptimeMillis > earliestUptimeMillis || totalAccountedSnapshots < 2) {
                return -1;
            }
            return (int) ((totalAvailableCpuFreqKHz * 100.0d) / totalOnlineMaxCpuFreqKHz);
        }

        public void clear() {
            this.mLatestCpuAvailabilityInfo = null;
            this.mSnapshotsByUptime.clear();
        }

        public java.lang.String toString() {
            return "CpusetInfo{cpuset = " + com.android.server.cpu.CpuAvailabilityMonitoringConfig.toCpusetString(this.cpuset) + ", mSnapshotsByUptime = " + this.mSnapshotsByUptime + ", mLatestCpuAvailabilityInfo = " + this.mLatestCpuAvailabilityInfo + '}';
        }

        private static final class Snapshot {
            public long totalNormalizedAvailableCpuFreqKHz;
            public int totalOfflineCpus;
            public long totalOfflineMaxCpuFreqKHz;
            public int totalOnlineCpus;
            public long totalOnlineMaxCpuFreqKHz;
            public final long uptimeMillis;

            Snapshot(long uptimeMillis) {
                this.uptimeMillis = uptimeMillis;
            }

            public void appendCpuInfo(com.android.server.cpu.CpuInfoReader.CpuInfo cpuInfo) {
                if (!cpuInfo.isOnline) {
                    this.totalOfflineCpus++;
                    this.totalOfflineMaxCpuFreqKHz += cpuInfo.maxCpuFreqKHz;
                } else {
                    this.totalOnlineCpus++;
                    this.totalNormalizedAvailableCpuFreqKHz += cpuInfo.getNormalizedAvailableCpuFreqKHz();
                    this.totalOnlineMaxCpuFreqKHz += cpuInfo.maxCpuFreqKHz;
                }
            }

            public int getAverageAvailableCpuFreqPercent() {
                int percent = (int) ((this.totalNormalizedAvailableCpuFreqKHz * 100.0d) / this.totalOnlineMaxCpuFreqKHz);
                if (percent < 0) {
                    com.android.server.utils.Slogf.wtf(com.android.server.cpu.CpuMonitorService.TAG, "Computed negative CPU availability percent(%d) for %s ", java.lang.Integer.valueOf(percent), toString());
                    return 0;
                }
                return percent;
            }

            public java.lang.String toString() {
                return "Snapshot{uptimeMillis = " + this.uptimeMillis + ", totalOnlineCpus = " + this.totalOnlineCpus + ", totalOfflineCpus = " + this.totalOfflineCpus + ", totalNormalizedAvailableCpuFreqKHz = " + this.totalNormalizedAvailableCpuFreqKHz + ", totalOnlineMaxCpuFreqKHz = " + this.totalOnlineMaxCpuFreqKHz + ", totalOfflineMaxCpuFreqKHz = " + this.totalOfflineMaxCpuFreqKHz + '}';
            }
        }
    }
}
