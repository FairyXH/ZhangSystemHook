package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class AnrHelper {
    static final int APP_NOT_RESPONDING_DEFER_MSG = 4;
    static final int APP_NOT_RESPONDING_DEFER_TIMEOUT_MILLIS = 10000;
    private static final int DEFAULT_THREAD_KEEP_ALIVE_SECOND = 10;
    private static final java.lang.String TAG = "ActivityManager";
    private final java.util.ArrayList<com.android.server.am.AnrHelper.AnrRecord> mAnrRecords;
    private final java.util.concurrent.ExecutorService mAuxiliaryTaskExecutor;
    private final java.util.concurrent.ExecutorService mEarlyDumpExecutor;
    private android.os.Handler mFgHandler;
    private long mLastAnrTimeMs;
    private int mProcessingPid;
    private final java.util.concurrent.atomic.AtomicBoolean mRunning;
    private final com.android.server.am.ActivityManagerService mService;
    private final java.util.Set<java.lang.Integer> mTempDumpedPids;
    private static final long EXPIRED_REPORT_TIME_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(10);
    private static final long CONSECUTIVE_ANR_TIME_MS = java.util.concurrent.TimeUnit.MINUTES.toMillis(2);
    private static final long SELF_ONLY_AFTER_BOOT_MS = java.util.concurrent.TimeUnit.MINUTES.toMillis(10);
    private static final java.util.concurrent.ThreadFactory sDefaultThreadFactory = new java.util.concurrent.ThreadFactory() { // from class: com.android.server.am.AnrHelper$$ExternalSyntheticLambda0
        @Override // java.util.concurrent.ThreadFactory
        public final java.lang.Thread newThread(java.lang.Runnable runnable) {
            return com.android.server.am.AnrHelper.lambda$static$0(runnable);
        }
    };
    private static final java.util.concurrent.ThreadFactory sMainProcessDumpThreadFactory = new java.util.concurrent.ThreadFactory() { // from class: com.android.server.am.AnrHelper$$ExternalSyntheticLambda1
        @Override // java.util.concurrent.ThreadFactory
        public final java.lang.Thread newThread(java.lang.Runnable runnable) {
            return com.android.server.am.AnrHelper.lambda$static$1(runnable);
        }
    };

    static /* synthetic */ java.lang.Thread lambda$static$0(java.lang.Runnable r) {
        return new java.lang.Thread(r, "AnrAuxiliaryTaskExecutor");
    }

    static /* synthetic */ java.lang.Thread lambda$static$1(java.lang.Runnable r) {
        return new java.lang.Thread(r, "AnrMainProcessDumpThread");
    }

    AnrHelper(com.android.server.am.ActivityManagerService service) {
        this(service, makeExpiringThreadPoolWithSize(1, sDefaultThreadFactory), makeExpiringThreadPoolWithSize(2, sMainProcessDumpThreadFactory));
    }

    AnrHelper(com.android.server.am.ActivityManagerService service, java.util.concurrent.ExecutorService auxExecutor, java.util.concurrent.ExecutorService earlyDumpExecutor) {
        this.mAnrRecords = new java.util.ArrayList<>();
        this.mTempDumpedPids = java.util.Collections.synchronizedSet(new android.util.ArraySet());
        this.mRunning = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mLastAnrTimeMs = 0L;
        this.mProcessingPid = -1;
        this.mFgHandler = new android.os.Handler(com.android.server.FgThread.getHandler().getLooper()) { // from class: com.android.server.am.AnrHelper.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 4:
                        com.android.server.am.AnrHelper.this.appNotResponding((com.android.server.am.AnrHelper.AnrRecord) msg.obj);
                        break;
                }
            }
        };
        this.mService = service;
        this.mAuxiliaryTaskExecutor = auxExecutor;
        this.mEarlyDumpExecutor = earlyDumpExecutor;
    }

    void appNotResponding(com.android.server.am.ProcessRecord anrProcess, com.android.internal.os.TimeoutRecord timeoutRecord) {
        appNotResponding(anrProcess, null, null, null, null, false, timeoutRecord, false);
    }

    void appNotResponding(com.android.server.am.ProcessRecord anrProcess, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, com.android.server.wm.WindowProcessController parentProcess, boolean aboveSystem, final com.android.internal.os.TimeoutRecord timeoutRecord, boolean isContinuousAnr) {
        try {
            timeoutRecord.mLatencyTracker.appNotRespondingStarted();
            final int incomingPid = anrProcess.mPid;
            timeoutRecord.mLatencyTracker.waitingOnAnrRecordLockStarted();
            synchronized (this.mAnrRecords) {
                timeoutRecord.mLatencyTracker.waitingOnAnrRecordLockEnded();
                if (incomingPid == 0) {
                    android.util.Slog.i("ActivityManager", "Skip zero pid ANR, process=" + anrProcess.processName);
                    return;
                }
                if (this.mProcessingPid == incomingPid) {
                    android.util.Slog.i("ActivityManager", "Skip duplicated ANR, pid=" + incomingPid + " " + timeoutRecord.mReason);
                    return;
                }
                if (!this.mTempDumpedPids.add(java.lang.Integer.valueOf(incomingPid))) {
                    android.util.Slog.i("ActivityManager", "Skip ANR being predumped, pid=" + incomingPid + " " + timeoutRecord.mReason);
                    return;
                }
                for (int i = this.mAnrRecords.size() - 1; i >= 0; i--) {
                    if (this.mAnrRecords.get(i).mPid == incomingPid) {
                        android.util.Slog.i("ActivityManager", "Skip queued ANR, pid=" + incomingPid + " " + timeoutRecord.mReason);
                        return;
                    }
                }
                timeoutRecord.mLatencyTracker.earlyDumpRequestSubmittedWithSize(this.mTempDumpedPids.size());
                java.util.concurrent.Future<java.io.File> firstPidDumpPromise = this.mEarlyDumpExecutor.submit(new java.util.concurrent.Callable() { // from class: com.android.server.am.AnrHelper$$ExternalSyntheticLambda2
                    @Override // java.util.concurrent.Callable
                    public final java.lang.Object call() {
                        return this.f$0.lambda$appNotResponding$2(incomingPid, timeoutRecord);
                    }
                });
                timeoutRecord.mLatencyTracker.anrRecordPlacingOnQueueWithSize(this.mAnrRecords.size());
                appNotResponding(new com.android.server.am.AnrHelper.AnrRecord(anrProcess, activityShortComponentName, aInfo, parentShortComponentName, parentProcess, aboveSystem, timeoutRecord, isContinuousAnr, firstPidDumpPromise));
            }
        } finally {
            timeoutRecord.mLatencyTracker.appNotRespondingEnded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.io.File lambda$appNotResponding$2(int incomingPid, com.android.internal.os.TimeoutRecord timeoutRecord) throws java.lang.Exception {
        java.io.File tracesFile = com.android.server.am.StackTracesDumpHelper.dumpStackTracesTempFile(incomingPid, timeoutRecord.mLatencyTracker);
        this.mTempDumpedPids.remove(java.lang.Integer.valueOf(incomingPid));
        return tracesFile;
    }

    void deferAppNotResponding(com.android.server.am.ProcessRecord anrProcess, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, com.android.server.wm.WindowProcessController parentProcess, boolean aboveSystem, java.util.concurrent.ExecutorService auxiliaryTaskExecutor, com.android.internal.os.TimeoutRecord timeoutRecord, long delayInMillis, boolean isContinuousAnr, java.util.concurrent.Future<java.io.File> firstPidFilePromise) {
        com.android.server.am.AnrHelper.AnrRecord anrRecord = new com.android.server.am.AnrHelper.AnrRecord(anrProcess, activityShortComponentName, aInfo, parentShortComponentName, parentProcess, aboveSystem, timeoutRecord, isContinuousAnr, firstPidFilePromise);
        android.os.Message msg = android.os.Message.obtain();
        msg.what = 4;
        msg.obj = anrRecord;
        this.mFgHandler.sendMessageDelayed(msg, delayInMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appNotResponding(com.android.server.am.AnrHelper.AnrRecord anrRecord) {
        if (anrRecord != null) {
            this.mService.handleAppNotResponding(anrRecord.mApp, anrRecord.mActivityShortComponentName, anrRecord.mAppInfo, anrRecord.mActivityShortComponentName, anrRecord.mParentProcess, anrRecord.mAboveSystem, anrRecord.mTimeoutRecord.mReason, anrRecord.mEventId);
        }
        synchronized (this.mAnrRecords) {
            this.mAnrRecords.add(anrRecord);
        }
        startAnrConsumerIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnrConsumerIfNeeded() {
        if (this.mRunning.compareAndSet(false, true)) {
            new com.android.server.am.AnrHelper.AnrConsumerThread().start();
        }
    }

    private static java.util.concurrent.ThreadPoolExecutor makeExpiringThreadPoolWithSize(int size, java.util.concurrent.ThreadFactory factory) {
        java.util.concurrent.ThreadPoolExecutor pool = new java.util.concurrent.ThreadPoolExecutor(size, size, 10L, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.LinkedBlockingQueue(), factory);
        pool.allowCoreThreadTimeOut(true);
        return pool;
    }

    private class AnrConsumerThread extends java.lang.Thread {
        AnrConsumerThread() {
            super("AnrConsumer");
        }

        private com.android.server.am.AnrHelper.AnrRecord next() {
            synchronized (com.android.server.am.AnrHelper.this.mAnrRecords) {
                if (com.android.server.am.AnrHelper.this.mAnrRecords.isEmpty()) {
                    return null;
                }
                com.android.server.am.AnrHelper.AnrRecord record = (com.android.server.am.AnrHelper.AnrRecord) com.android.server.am.AnrHelper.this.mAnrRecords.remove(0);
                com.android.server.am.AnrHelper.this.mProcessingPid = record.mPid;
                record.mTimeoutRecord.mLatencyTracker.anrRecordsQueueSizeWhenPopped(com.android.server.am.AnrHelper.this.mAnrRecords.size());
                return record;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                com.android.server.am.AnrHelper.AnrRecord r = next();
                if (r == null) {
                    break;
                }
                com.android.server.am.AnrHelper.this.scheduleBinderHeavyHitterAutoSamplerIfNecessary();
                int currentPid = r.mApp.mPid;
                if (currentPid != r.mPid) {
                    android.util.Slog.i("ActivityManager", "Skip ANR with mismatched pid=" + r.mPid + ", current pid=" + currentPid);
                } else {
                    long startTime = android.os.SystemClock.uptimeMillis();
                    long reportLatency = startTime - r.mTimestamp;
                    boolean onlyDumpSelf = reportLatency > com.android.server.am.AnrHelper.EXPIRED_REPORT_TIME_MS || startTime < com.android.server.am.AnrHelper.SELF_ONLY_AFTER_BOOT_MS;
                    r.appNotResponding(onlyDumpSelf);
                    long endTime = android.os.SystemClock.uptimeMillis();
                    android.util.Slog.d("ActivityManager", "Completed ANR of " + r.mApp.processName + " in " + (endTime - startTime) + "ms, latency " + reportLatency + (onlyDumpSelf ? "ms (expired, only dump ANR app)" : "ms"));
                }
            }
            com.android.server.am.AnrHelper.this.mRunning.set(false);
            synchronized (com.android.server.am.AnrHelper.this.mAnrRecords) {
                com.android.server.am.AnrHelper.this.mProcessingPid = -1;
                if (!com.android.server.am.AnrHelper.this.mAnrRecords.isEmpty()) {
                    com.android.server.am.AnrHelper.this.startAnrConsumerIfNeeded();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleBinderHeavyHitterAutoSamplerIfNecessary() {
        try {
            android.os.Trace.traceBegin(64L, "scheduleBinderHeavyHitterAutoSamplerIfNecessary()");
            long now = android.os.SystemClock.uptimeMillis();
            if (this.mLastAnrTimeMs + CONSECUTIVE_ANR_TIME_MS > now) {
                this.mService.scheduleBinderHeavyHitterAutoSampler();
            }
            this.mLastAnrTimeMs = now;
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    private class AnrRecord {
        final boolean mAboveSystem;
        final java.lang.String mActivityShortComponentName;
        final com.android.server.am.ProcessRecord mApp;
        final android.content.pm.ApplicationInfo mAppInfo;
        final java.util.concurrent.Future<java.io.File> mFirstPidFilePromise;
        final boolean mIsContinuousAnr;
        final com.android.server.wm.WindowProcessController mParentProcess;
        final java.lang.String mParentShortComponentName;
        final int mPid;
        final com.android.internal.os.TimeoutRecord mTimeoutRecord;
        final int mUid;
        final long mTimestamp = android.os.SystemClock.uptimeMillis();
        final java.lang.String mEventId = java.util.UUID.randomUUID().toString();

        AnrRecord(com.android.server.am.ProcessRecord anrProcess, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, com.android.server.wm.WindowProcessController parentProcess, boolean aboveSystem, com.android.internal.os.TimeoutRecord timeoutRecord, boolean isContinuousAnr, java.util.concurrent.Future<java.io.File> firstPidFilePromise) {
            this.mApp = anrProcess;
            this.mPid = anrProcess.mPid;
            this.mUid = anrProcess.uid;
            this.mActivityShortComponentName = activityShortComponentName;
            this.mParentShortComponentName = parentShortComponentName;
            this.mTimeoutRecord = timeoutRecord;
            this.mAppInfo = aInfo;
            this.mParentProcess = parentProcess;
            this.mAboveSystem = aboveSystem;
            this.mIsContinuousAnr = isContinuousAnr;
            this.mFirstPidFilePromise = firstPidFilePromise;
        }

        void appNotResponding(boolean onlyDumpSelf) {
            try {
                this.mTimeoutRecord.mLatencyTracker.anrProcessingStarted();
                this.mApp.mErrorState.appNotResponding(this.mActivityShortComponentName, this.mAppInfo, this.mParentShortComponentName, this.mParentProcess, this.mAboveSystem, this.mTimeoutRecord, com.android.server.am.AnrHelper.this.mAuxiliaryTaskExecutor, onlyDumpSelf, this.mIsContinuousAnr, this.mFirstPidFilePromise, this.mEventId);
                android.app.usage.UsageStatsManagerInternal usmi = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
                if (usmi != null && this.mApp.info != null) {
                    usmi.reportEvent(this.mApp.info.packageName, this.mApp.userId, 32);
                }
            } finally {
                this.mTimeoutRecord.mLatencyTracker.anrProcessingEnded();
            }
        }
    }
}
