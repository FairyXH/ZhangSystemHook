package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BatteryExternalStatsWorker implements com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync {
    private static final boolean DEBUG = false;
    private static final long EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS = 2000;
    private static final long MAX_WIFI_STATS_SAMPLE_ERROR_MILLIS = 750;
    private static final java.lang.String TAG = "BatteryExternalStatsWorker";
    public static final int UID_FINAL_REMOVAL_AFTER_USER_REMOVAL_DELAY_MILLIS = 10000;
    public static final int UID_QUICK_REMOVAL_AFTER_USER_REMOVAL_DELAY_MILLIS = 2000;
    private java.util.concurrent.Future<?> mBatteryLevelSync;
    private java.util.concurrent.Future<?> mCurrentFuture;
    private java.lang.String mCurrentReason;
    private com.android.server.power.stats.EnergyConsumerSnapshot mEnergyConsumerSnapshot;
    private android.util.SparseArray<int[]> mEnergyConsumerTypeToIdMap;
    private final java.util.concurrent.ScheduledExecutorService mExecutorService;
    final com.android.server.power.stats.BatteryExternalStatsWorker.Injector mInjector;
    private long mLastCollectionTimeStamp;
    private android.os.connectivity.WifiActivityEnergyInfo mLastWifiInfo;
    private boolean mOnBattery;
    private boolean mOnBatteryScreenOff;
    private int[] mPerDisplayScreenStates;
    private android.power.PowerStatsInternal mPowerStatsInternal;
    private java.util.concurrent.Future<?> mProcessStateSync;
    private int mScreenState;
    private final com.android.server.power.stats.BatteryStatsImpl mStats;
    private final java.lang.Runnable mSyncTask;
    private android.telephony.TelephonyManager mTelephony;
    private int mUpdateFlags;
    private boolean mUseLatestStates;
    private java.util.concurrent.Future<?> mWakelockChangesUpdate;
    private android.net.wifi.WifiManager mWifiManager;
    private final java.lang.Object mWorkerLock;
    private final java.lang.Runnable mWriteTask;

    static /* synthetic */ java.lang.Thread lambda$new$1(final java.lang.Runnable r) {
        java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.power.stats.BatteryExternalStatsWorker.lambda$new$0(r);
            }
        }, "batterystats-worker");
        t.setPriority(5);
        return t;
    }

    static /* synthetic */ void lambda$new$0(java.lang.Runnable r) {
        android.os.ThreadLocalWorkSource.setUid(android.os.Process.myUid());
        r.run();
    }

    public static class Injector {
        private final android.content.Context mContext;

        Injector(android.content.Context context) {
            this.mContext = context;
        }

        public <T> T getSystemService(java.lang.Class<T> cls) {
            return (T) this.mContext.getSystemService(cls);
        }

        public <T> T getLocalService(java.lang.Class<T> cls) {
            return (T) com.android.server.LocalServices.getService(cls);
        }
    }

    public BatteryExternalStatsWorker(android.content.Context context, com.android.server.power.stats.BatteryStatsImpl stats) {
        this(new com.android.server.power.stats.BatteryExternalStatsWorker.Injector(context), stats);
    }

    BatteryExternalStatsWorker(com.android.server.power.stats.BatteryExternalStatsWorker.Injector injector, com.android.server.power.stats.BatteryStatsImpl stats) {
        this.mExecutorService = java.util.concurrent.Executors.newSingleThreadScheduledExecutor(new java.util.concurrent.ThreadFactory() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.ThreadFactory
            public final java.lang.Thread newThread(java.lang.Runnable runnable) {
                return com.android.server.power.stats.BatteryExternalStatsWorker.lambda$new$1(runnable);
            }
        });
        this.mUpdateFlags = 0;
        this.mCurrentFuture = null;
        this.mCurrentReason = null;
        this.mPerDisplayScreenStates = null;
        this.mUseLatestStates = true;
        this.mWorkerLock = new java.lang.Object();
        this.mWifiManager = null;
        this.mTelephony = null;
        this.mPowerStatsInternal = null;
        this.mLastWifiInfo = new android.os.connectivity.WifiActivityEnergyInfo(0L, 0, 0L, 0L, 0L, 0L);
        this.mEnergyConsumerTypeToIdMap = null;
        this.mEnergyConsumerSnapshot = null;
        this.mSyncTask = new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker.1
            @Override // java.lang.Runnable
            public void run() {
                int updateFlags;
                java.lang.String reason;
                boolean onBattery;
                boolean onBatteryScreenOff;
                int screenState;
                int[] displayScreenStates;
                boolean useLatestStates;
                synchronized (com.android.server.power.stats.BatteryExternalStatsWorker.this) {
                    updateFlags = com.android.server.power.stats.BatteryExternalStatsWorker.this.mUpdateFlags;
                    reason = com.android.server.power.stats.BatteryExternalStatsWorker.this.mCurrentReason;
                    onBattery = com.android.server.power.stats.BatteryExternalStatsWorker.this.mOnBattery;
                    onBatteryScreenOff = com.android.server.power.stats.BatteryExternalStatsWorker.this.mOnBatteryScreenOff;
                    screenState = com.android.server.power.stats.BatteryExternalStatsWorker.this.mScreenState;
                    displayScreenStates = com.android.server.power.stats.BatteryExternalStatsWorker.this.mPerDisplayScreenStates;
                    useLatestStates = com.android.server.power.stats.BatteryExternalStatsWorker.this.mUseLatestStates;
                    com.android.server.power.stats.BatteryExternalStatsWorker.this.mUpdateFlags = 0;
                    com.android.server.power.stats.BatteryExternalStatsWorker.this.mCurrentReason = null;
                    com.android.server.power.stats.BatteryExternalStatsWorker.this.mCurrentFuture = null;
                    com.android.server.power.stats.BatteryExternalStatsWorker.this.mUseLatestStates = true;
                    if ((updateFlags & 127) == 127) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.cancelSyncDueToBatteryLevelChangeLocked();
                    }
                    if ((updateFlags & 1) != 0) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.cancelCpuSyncDueToWakelockChange();
                    }
                    if ((updateFlags & 14) == 14) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.cancelSyncDueToProcessStateChange();
                    }
                }
                try {
                    synchronized (com.android.server.power.stats.BatteryExternalStatsWorker.this.mWorkerLock) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.updateExternalStatsLocked(reason, updateFlags, onBattery, onBatteryScreenOff, screenState, displayScreenStates, useLatestStates);
                    }
                    if ((updateFlags & 1) != 0) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.mStats.updateCpuTimesForAllUids();
                    }
                    synchronized (com.android.server.power.stats.BatteryExternalStatsWorker.this.mStats) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.mStats.clearPendingRemovedUidsLocked();
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.wtf(com.android.server.power.stats.BatteryExternalStatsWorker.TAG, "Error updating external stats: ", e);
                }
                if ((updateFlags & 128) != 0) {
                    synchronized (com.android.server.power.stats.BatteryExternalStatsWorker.this) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.mLastCollectionTimeStamp = 0L;
                    }
                } else if ((updateFlags & 127) == 127) {
                    synchronized (com.android.server.power.stats.BatteryExternalStatsWorker.this) {
                        com.android.server.power.stats.BatteryExternalStatsWorker.this.mLastCollectionTimeStamp = android.os.SystemClock.elapsedRealtime();
                    }
                }
            }
        };
        this.mWriteTask = new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.power.stats.BatteryExternalStatsWorker.this.mStats) {
                    com.android.server.power.stats.BatteryExternalStatsWorker.this.mStats.writeAsyncLocked();
                }
            }
        };
        this.mInjector = injector;
        this.mStats = stats;
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00a1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void systemServicesReady() {
        /*
            r12 = this;
            com.android.server.power.stats.BatteryExternalStatsWorker$Injector r0 = r12.mInjector
            java.lang.Class<android.net.wifi.WifiManager> r1 = android.net.wifi.WifiManager.class
            java.lang.Object r0 = r0.getSystemService(r1)
            android.net.wifi.WifiManager r0 = (android.net.wifi.WifiManager) r0
            com.android.server.power.stats.BatteryExternalStatsWorker$Injector r1 = r12.mInjector
            java.lang.Class<android.telephony.TelephonyManager> r2 = android.telephony.TelephonyManager.class
            java.lang.Object r1 = r1.getSystemService(r2)
            android.telephony.TelephonyManager r1 = (android.telephony.TelephonyManager) r1
            com.android.server.power.stats.BatteryExternalStatsWorker$Injector r2 = r12.mInjector
            java.lang.Class<android.power.PowerStatsInternal> r3 = android.power.PowerStatsInternal.class
            java.lang.Object r2 = r2.getLocalService(r3)
            android.power.PowerStatsInternal r2 = (android.power.PowerStatsInternal) r2
            com.android.server.power.stats.BatteryStatsImpl r3 = r12.mStats
            monitor-enter(r3)
            com.android.server.power.stats.BatteryStatsImpl r4 = r12.mStats     // Catch: java.lang.Throwable -> Laf
            int r4 = r4.getBatteryVoltageMvLocked()     // Catch: java.lang.Throwable -> Laf
            monitor-exit(r3)     // Catch: java.lang.Throwable -> Laf
            java.lang.Object r5 = r12.mWorkerLock
            monitor-enter(r5)
            r12.mWifiManager = r0     // Catch: java.lang.Throwable -> Lac
            r12.mTelephony = r1     // Catch: java.lang.Throwable -> Lac
            r12.mPowerStatsInternal = r2     // Catch: java.lang.Throwable -> Lac
            r3 = 0
            r6 = 0
            android.power.PowerStatsInternal r7 = r12.mPowerStatsInternal     // Catch: java.lang.Throwable -> Lac
            if (r7 == 0) goto L9e
        L38:
            android.util.SparseArray r7 = r12.populateEnergyConsumerSubsystemMapsLocked()     // Catch: java.lang.Throwable -> Lac
            if (r7 == 0) goto L9e
            com.android.server.power.stats.EnergyConsumerSnapshot r8 = new com.android.server.power.stats.EnergyConsumerSnapshot     // Catch: java.lang.Throwable -> Lac
            r8.<init>(r7)     // Catch: java.lang.Throwable -> Lac
            r12.mEnergyConsumerSnapshot = r8     // Catch: java.lang.Throwable -> Lac
            java.util.concurrent.CompletableFuture r8 = r12.getEnergyConsumptionData()     // Catch: java.util.concurrent.ExecutionException -> L59 java.lang.Throwable -> L77 java.lang.Throwable -> Lac
            java.util.concurrent.TimeUnit r9 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch: java.util.concurrent.ExecutionException -> L59 java.lang.Throwable -> L77 java.lang.Throwable -> Lac
            r10 = 2000(0x7d0, double:9.88E-321)
            java.lang.Object r8 = r8.get(r10, r9)     // Catch: java.util.concurrent.ExecutionException -> L59 java.lang.Throwable -> L77 java.lang.Throwable -> Lac
            android.hardware.power.stats.EnergyConsumerResult[] r8 = (android.hardware.power.stats.EnergyConsumerResult[]) r8     // Catch: java.util.concurrent.ExecutionException -> L59 java.lang.Throwable -> L77 java.lang.Throwable -> Lac
            com.android.server.power.stats.EnergyConsumerSnapshot r9 = r12.mEnergyConsumerSnapshot     // Catch: java.util.concurrent.ExecutionException -> L59 java.lang.Throwable -> L77 java.lang.Throwable -> Lac
            r9.updateAndGetDelta(r8, r4)     // Catch: java.util.concurrent.ExecutionException -> L59 java.lang.Throwable -> L77 java.lang.Throwable -> Lac
            goto L92
        L59:
            r8 = move-exception
            java.lang.String r9 = "BatteryExternalStatsWorker"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lac
            r10.<init>()     // Catch: java.lang.Throwable -> Lac
            java.lang.String r11 = "exception reading initial getEnergyConsumedAsync: "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch: java.lang.Throwable -> Lac
            java.lang.Throwable r11 = r8.getCause()     // Catch: java.lang.Throwable -> Lac
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch: java.lang.Throwable -> Lac
            java.lang.String r10 = r10.toString()     // Catch: java.lang.Throwable -> Lac
            android.util.Slog.wtf(r9, r10)     // Catch: java.lang.Throwable -> Lac
            goto L92
        L77:
            r8 = move-exception
            java.lang.String r9 = "BatteryExternalStatsWorker"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lac
            r10.<init>()     // Catch: java.lang.Throwable -> Lac
            java.lang.String r11 = "timeout or interrupt reading initial getEnergyConsumedAsync: "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch: java.lang.Throwable -> Lac
            java.lang.StringBuilder r10 = r10.append(r8)     // Catch: java.lang.Throwable -> Lac
            java.lang.String r10 = r10.toString()     // Catch: java.lang.Throwable -> Lac
            android.util.Slog.w(r9, r10)     // Catch: java.lang.Throwable -> Lac
        L92:
            com.android.server.power.stats.EnergyConsumerSnapshot r8 = r12.mEnergyConsumerSnapshot     // Catch: java.lang.Throwable -> Lac
            java.lang.String[] r8 = r8.getOtherOrdinalNames()     // Catch: java.lang.Throwable -> Lac
            r6 = r8
            boolean[] r8 = getSupportedEnergyBuckets(r7)     // Catch: java.lang.Throwable -> Lac
            r3 = r8
        L9e:
            com.android.server.power.stats.BatteryStatsImpl r7 = r12.mStats     // Catch: java.lang.Throwable -> Lac
            monitor-enter(r7)     // Catch: java.lang.Throwable -> Lac
            com.android.server.power.stats.BatteryStatsImpl r8 = r12.mStats     // Catch: java.lang.Throwable -> La9
            r8.initEnergyConsumerStatsLocked(r3, r6)     // Catch: java.lang.Throwable -> La9
            monitor-exit(r7)     // Catch: java.lang.Throwable -> La9
            monitor-exit(r5)     // Catch: java.lang.Throwable -> Lac
            return
        La9:
            r8 = move-exception
            monitor-exit(r7)     // Catch: java.lang.Throwable -> La9
            throw r8     // Catch: java.lang.Throwable -> Lac
        Lac:
            r3 = move-exception
            monitor-exit(r5)     // Catch: java.lang.Throwable -> Lac
            throw r3
        Laf:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> Laf
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryExternalStatsWorker.systemServicesReady():void");
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public synchronized java.util.concurrent.Future<?> scheduleSync(java.lang.String reason, int flags) {
        return scheduleSyncLocked(reason, flags);
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public synchronized java.util.concurrent.Future<?> scheduleCpuSyncDueToRemovedUid(int uid) {
        return scheduleSyncLocked("remove-uid", 1);
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public java.util.concurrent.Future<?> scheduleSyncDueToScreenStateChange(int flags, boolean onBattery, boolean onBatteryScreenOff, int screenState, int[] perDisplayScreenStates) {
        java.util.concurrent.Future<?> futureScheduleSyncLocked;
        synchronized (this) {
            if (this.mCurrentFuture == null || (this.mUpdateFlags & 1) == 0) {
                this.mOnBattery = onBattery;
                this.mOnBatteryScreenOff = onBatteryScreenOff;
                this.mUseLatestStates = false;
            }
            this.mScreenState = screenState;
            this.mPerDisplayScreenStates = perDisplayScreenStates;
            futureScheduleSyncLocked = scheduleSyncLocked("screen-state", flags);
        }
        return futureScheduleSyncLocked;
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public java.util.concurrent.Future<?> scheduleCpuSyncDueToWakelockChange(long delayMillis) {
        java.util.concurrent.Future<?> future;
        synchronized (this) {
            this.mWakelockChangesUpdate = scheduleDelayedSyncLocked(this.mWakelockChangesUpdate, new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleCpuSyncDueToWakelockChange$3();
                }
            }, delayMillis);
            future = this.mWakelockChangesUpdate;
        }
        return future;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleCpuSyncDueToWakelockChange$3() {
        scheduleSync("wakelock-change", 1);
        scheduleRunnable(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleCpuSyncDueToWakelockChange$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleCpuSyncDueToWakelockChange$2() {
        this.mStats.postBatteryNeedsCpuUpdateMsg();
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public void cancelCpuSyncDueToWakelockChange() {
        synchronized (this) {
            if (this.mWakelockChangesUpdate != null) {
                this.mWakelockChangesUpdate.cancel(false);
                this.mWakelockChangesUpdate = null;
            }
        }
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public java.util.concurrent.Future<?> scheduleSyncDueToBatteryLevelChange(long delayMillis) {
        java.util.concurrent.Future<?> future;
        synchronized (this) {
            this.mBatteryLevelSync = scheduleDelayedSyncLocked(this.mBatteryLevelSync, new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleSyncDueToBatteryLevelChange$4();
                }
            }, delayMillis);
            future = this.mBatteryLevelSync;
        }
        return future;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleSyncDueToBatteryLevelChange$4() {
        scheduleSync("battery-level", 127);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelSyncDueToBatteryLevelChangeLocked() {
        if (this.mBatteryLevelSync != null) {
            this.mBatteryLevelSync.cancel(false);
            this.mBatteryLevelSync = null;
        }
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public void scheduleSyncDueToProcessStateChange(final int flags, long delayMillis) {
        synchronized (this) {
            this.mProcessStateSync = scheduleDelayedSyncLocked(this.mProcessStateSync, new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleSyncDueToProcessStateChange$5(flags);
                }
            }, delayMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleSyncDueToProcessStateChange$5(int flags) {
        scheduleSync("procstate-change", flags);
    }

    public void cancelSyncDueToProcessStateChange() {
        synchronized (this) {
            if (this.mProcessStateSync != null) {
                this.mProcessStateSync.cancel(false);
                this.mProcessStateSync = null;
            }
        }
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync
    public java.util.concurrent.Future<?> scheduleCleanupDueToRemovedUser(final int userId) {
        java.util.concurrent.ScheduledFuture<?> scheduledFutureSchedule;
        synchronized (this) {
            try {
                try {
                    this.mExecutorService.schedule(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$scheduleCleanupDueToRemovedUser$6(userId);
                        }
                    }, EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
                    scheduledFutureSchedule = this.mExecutorService.schedule(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryExternalStatsWorker$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$scheduleCleanupDueToRemovedUser$7(userId);
                        }
                    }, 10000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (java.util.concurrent.RejectedExecutionException e) {
                    return java.util.concurrent.CompletableFuture.failedFuture(e);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return scheduledFutureSchedule;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleCleanupDueToRemovedUser$6(int userId) {
        synchronized (this.mStats) {
            this.mStats.clearRemovedUserUidsLocked(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleCleanupDueToRemovedUser$7(int userId) {
        synchronized (this.mStats) {
            this.mStats.clearRemovedUserUidsLocked(userId);
        }
    }

    private java.util.concurrent.Future<?> scheduleDelayedSyncLocked(java.util.concurrent.Future<?> lastScheduledSync, java.lang.Runnable syncRunnable, long delayMillis) {
        if (this.mExecutorService.isShutdown()) {
            return java.util.concurrent.CompletableFuture.failedFuture(new java.lang.IllegalStateException("worker shutdown"));
        }
        if (lastScheduledSync != null) {
            if (delayMillis == 0) {
                lastScheduledSync.cancel(false);
            } else {
                return lastScheduledSync;
            }
        }
        try {
            return this.mExecutorService.schedule(syncRunnable, delayMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            return java.util.concurrent.CompletableFuture.failedFuture(e);
        }
    }

    public synchronized java.util.concurrent.Future<?> scheduleWrite() {
        if (this.mExecutorService.isShutdown()) {
            return java.util.concurrent.CompletableFuture.failedFuture(new java.lang.IllegalStateException("worker shutdown"));
        }
        scheduleSyncLocked("write", 127);
        try {
            return this.mExecutorService.submit(this.mWriteTask);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            return java.util.concurrent.CompletableFuture.failedFuture(e);
        }
    }

    public synchronized void scheduleRunnable(java.lang.Runnable runnable) {
        try {
            this.mExecutorService.submit(runnable);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            android.util.Slog.e(TAG, "Couldn't schedule " + runnable, e);
        }
    }

    public void shutdown() {
        this.mExecutorService.shutdownNow();
    }

    private java.util.concurrent.Future<?> scheduleSyncLocked(java.lang.String reason, int flags) {
        if (this.mExecutorService.isShutdown()) {
            return java.util.concurrent.CompletableFuture.failedFuture(new java.lang.IllegalStateException("worker shutdown"));
        }
        if (this.mCurrentFuture == null) {
            this.mUpdateFlags = flags;
            this.mCurrentReason = reason;
            try {
                this.mCurrentFuture = this.mExecutorService.submit(this.mSyncTask);
            } catch (java.util.concurrent.RejectedExecutionException e) {
                return java.util.concurrent.CompletableFuture.failedFuture(e);
            }
        }
        this.mUpdateFlags |= flags;
        return this.mCurrentFuture;
    }

    public long getLastCollectionTimeStamp() {
        long j;
        synchronized (this) {
            j = this.mLastCollectionTimeStamp;
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:120:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x02f0  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x029f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:219:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v30 */
    /* JADX WARN: Type inference failed for: r3v32, types: [int] */
    /* JADX WARN: Type inference failed for: r3v35 */
    /* JADX WARN: Type inference failed for: r3v36 */
    /* JADX WARN: Type inference failed for: r3v37 */
    /* JADX WARN: Type inference failed for: r3v38 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v40 */
    /* JADX WARN: Type inference failed for: r3v41 */
    /* JADX WARN: Type inference failed for: r3v42 */
    /* JADX WARN: Type inference failed for: r3v43 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateExternalStatsLocked(java.lang.String r41, int r42, boolean r43, boolean r44, int r45, int[] r46, boolean r47) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 901
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryExternalStatsWorker.updateExternalStatsLocked(java.lang.String, int, boolean, boolean, int, int[], boolean):void");
    }

    static /* synthetic */ void lambda$updateExternalStatsLocked$8(android.os.SynchronousResultReceiver tempWifiReceiver, android.os.connectivity.WifiActivityEnergyInfo info) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable("controller_activity", info);
        tempWifiReceiver.send(0, bundle);
    }

    private static <T extends android.os.Parcelable> T awaitControllerInfo(android.os.SynchronousResultReceiver synchronousResultReceiver) {
        if (synchronousResultReceiver == null) {
            return null;
        }
        try {
            android.os.SynchronousResultReceiver.Result resultAwaitResult = synchronousResultReceiver.awaitResult(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS);
            if (resultAwaitResult.bundle != null) {
                resultAwaitResult.bundle.setDefusable(true);
                T t = (T) resultAwaitResult.bundle.getParcelable("controller_activity");
                if (t != null) {
                    return t;
                }
            }
        } catch (java.util.concurrent.TimeoutException e) {
            android.util.Slog.w(TAG, "timeout reading " + synchronousResultReceiver.getName() + " stats");
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00f6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.os.connectivity.WifiActivityEnergyInfo extractDeltaLocked(android.os.connectivity.WifiActivityEnergyInfo r55) {
        /*
            Method dump skipped, instruction units count: 271
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryExternalStatsWorker.extractDeltaLocked(android.os.connectivity.WifiActivityEnergyInfo):android.os.connectivity.WifiActivityEnergyInfo");
    }

    private static boolean[] getSupportedEnergyBuckets(android.util.SparseArray<android.hardware.power.stats.EnergyConsumer> idToConsumer) {
        if (idToConsumer == null) {
            return null;
        }
        boolean[] buckets = new boolean[10];
        int size = idToConsumer.size();
        for (int idx = 0; idx < size; idx++) {
            android.hardware.power.stats.EnergyConsumer consumer = idToConsumer.valueAt(idx);
            switch (consumer.type) {
                case 1:
                    buckets[5] = true;
                    break;
                case 2:
                    buckets[3] = true;
                    break;
                case 3:
                    buckets[0] = true;
                    buckets[1] = true;
                    buckets[2] = true;
                    break;
                case 4:
                    buckets[6] = true;
                    break;
                case 5:
                    buckets[7] = true;
                    buckets[9] = true;
                    break;
                case 6:
                    buckets[4] = true;
                    break;
                case 7:
                    buckets[8] = true;
                    break;
            }
        }
        return buckets;
    }

    private java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> getEnergyConsumptionData() {
        return getEnergyConsumptionData(new int[0]);
    }

    private java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> getEnergyConsumptionData(int[] consumerIds) {
        return this.mPowerStatsInternal.getEnergyConsumedAsync(consumerIds);
    }

    public java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> getEnergyConsumersLocked(int flags) {
        if (this.mEnergyConsumerSnapshot == null || this.mPowerStatsInternal == null) {
            return null;
        }
        if (flags == 127) {
            return getEnergyConsumptionData();
        }
        android.util.IntArray energyConsumerIds = new android.util.IntArray();
        if ((flags & 8) != 0) {
            addEnergyConsumerIdLocked(energyConsumerIds, 1);
        }
        if ((flags & 1) != 0) {
            addEnergyConsumerIdLocked(energyConsumerIds, 2);
        }
        if ((flags & 32) != 0) {
            addEnergyConsumerIdLocked(energyConsumerIds, 3);
        }
        if ((flags & 4) != 0) {
            addEnergyConsumerIdLocked(energyConsumerIds, 5);
        }
        if ((flags & 2) != 0) {
            addEnergyConsumerIdLocked(energyConsumerIds, 6);
        }
        if ((flags & 64) != 0) {
            addEnergyConsumerIdLocked(energyConsumerIds, 7);
        }
        if (energyConsumerIds.size() == 0) {
            return null;
        }
        return getEnergyConsumptionData(energyConsumerIds.toArray());
    }

    private void addEnergyConsumerIdLocked(android.util.IntArray energyConsumerIds, int type) {
        int[] consumerIds = this.mEnergyConsumerTypeToIdMap.get(type);
        if (consumerIds == null) {
            return;
        }
        energyConsumerIds.addAll(consumerIds);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.util.SparseArray<android.hardware.power.stats.EnergyConsumer> populateEnergyConsumerSubsystemMapsLocked() {
        /*
            r8 = this;
            android.power.PowerStatsInternal r0 = r8.mPowerStatsInternal
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            android.power.PowerStatsInternal r0 = r8.mPowerStatsInternal
            android.hardware.power.stats.EnergyConsumer[] r0 = r0.getEnergyConsumerInfo()
            if (r0 == 0) goto Lac
            int r2 = r0.length
            if (r2 != 0) goto L13
            goto Lac
        L13:
            android.util.SparseArray r1 = new android.util.SparseArray
            int r2 = r0.length
            r1.<init>(r2)
            android.util.SparseArray r2 = new android.util.SparseArray
            r2.<init>()
            int r3 = r0.length
            r4 = 0
        L20:
            if (r4 >= r3) goto L83
            r5 = r0[r4]
            int r6 = r5.ordinal
            if (r6 == 0) goto L61
            byte r6 = r5.type
            switch(r6) {
                case 0: goto L60;
                case 1: goto L2d;
                case 2: goto L60;
                case 3: goto L60;
                default: goto L2d;
            }
        L2d:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "EnergyConsumer '"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = r5.name
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = "' has unexpected ordinal "
            java.lang.StringBuilder r6 = r6.append(r7)
            int r7 = r5.ordinal
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = " for type "
            java.lang.StringBuilder r6 = r6.append(r7)
            byte r7 = r5.type
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            java.lang.String r7 = "BatteryExternalStatsWorker"
            android.util.Slog.w(r7, r6)
            goto L80
        L60:
        L61:
            int r6 = r5.id
            r1.put(r6, r5)
            byte r6 = r5.type
            java.lang.Object r6 = r2.get(r6)
            android.util.IntArray r6 = (android.util.IntArray) r6
            if (r6 != 0) goto L7b
            android.util.IntArray r7 = new android.util.IntArray
            r7.<init>()
            r6 = r7
            byte r7 = r5.type
            r2.put(r7, r6)
        L7b:
            int r7 = r5.id
            r6.add(r7)
        L80:
            int r4 = r4 + 1
            goto L20
        L83:
            android.util.SparseArray r3 = new android.util.SparseArray
            int r4 = r2.size()
            r3.<init>(r4)
            r8.mEnergyConsumerTypeToIdMap = r3
            int r3 = r2.size()
            r4 = 0
        L93:
            if (r4 >= r3) goto Lab
            int r5 = r2.keyAt(r4)
            java.lang.Object r6 = r2.valueAt(r4)
            android.util.IntArray r6 = (android.util.IntArray) r6
            int[] r6 = r6.toArray()
            android.util.SparseArray<int[]> r7 = r8.mEnergyConsumerTypeToIdMap
            r7.put(r5, r6)
            int r4 = r4 + 1
            goto L93
        Lab:
            return r1
        Lac:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryExternalStatsWorker.populateEnergyConsumerSubsystemMapsLocked():android.util.SparseArray");
    }
}
