package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppBatteryTracker extends com.android.server.am.BaseAppStateTracker<com.android.server.am.AppBatteryTracker.AppBatteryPolicy> implements com.android.server.am.AppRestrictionController.UidBatteryUsageProvider {
    static final com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage BATTERY_USAGE_NONE = new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage();
    static final long BATTERY_USAGE_STATS_POLLING_INTERVAL_MS_DEBUG = 2000;
    static final long BATTERY_USAGE_STATS_POLLING_INTERVAL_MS_LONG = 1800000;
    static final long BATTERY_USAGE_STATS_POLLING_MIN_INTERVAL_MS_DEBUG = 2000;
    static final long BATTERY_USAGE_STATS_POLLING_MIN_INTERVAL_MS_LONG = 300000;
    static final boolean DEBUG_BACKGROUND_BATTERY_TRACKER = false;
    static final boolean DEBUG_BACKGROUND_BATTERY_TRACKER_VERBOSE = false;
    static final java.lang.String TAG = "ActivityManager";
    private final android.util.SparseBooleanArray mActiveUserIdStates;
    private final long mBatteryUsageStatsPollingIntervalMs;
    private final long mBatteryUsageStatsPollingMinIntervalMs;
    private boolean mBatteryUsageStatsUpdatePending;
    private final java.lang.Runnable mBgBatteryUsageStatsCheck;
    private final java.lang.Runnable mBgBatteryUsageStatsPolling;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> mDebugUidPercentages;
    private long mLastBatteryUsageSamplingTs;
    private long mLastReportTime;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> mLastUidBatteryUsage;
    private long mLastUidBatteryUsageStartTs;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.BatteryUsage> mTmpUidBatteryUsage;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> mTmpUidBatteryUsage2;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> mTmpUidBatteryUsageInWindow;
    private final android.util.ArraySet<android.os.UserHandle> mTmpUserIds;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.BatteryUsage> mUidBatteryUsage;
    private final android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> mUidBatteryUsageInWindow;

    AppBatteryTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller) {
        this(context, controller, null, null);
    }

    AppBatteryTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<com.android.server.am.AppBatteryTracker.AppBatteryPolicy>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mBgBatteryUsageStatsPolling = new java.lang.Runnable() { // from class: com.android.server.am.AppBatteryTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.updateBatteryUsageStatsAndCheck();
            }
        };
        this.mBgBatteryUsageStatsCheck = new java.lang.Runnable() { // from class: com.android.server.am.AppBatteryTracker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.checkBatteryUsageStats();
            }
        };
        this.mActiveUserIdStates = new android.util.SparseBooleanArray();
        this.mUidBatteryUsage = new android.util.SparseArray<>();
        this.mUidBatteryUsageInWindow = new android.util.SparseArray<>();
        this.mLastUidBatteryUsage = new android.util.SparseArray<>();
        this.mTmpUidBatteryUsage = new android.util.SparseArray<>();
        this.mTmpUidBatteryUsage2 = new android.util.SparseArray<>();
        this.mTmpUidBatteryUsageInWindow = new android.util.SparseArray<>();
        this.mTmpUserIds = new android.util.ArraySet<>();
        this.mLastReportTime = 0L;
        this.mDebugUidPercentages = new android.util.SparseArray<>();
        if (injector == null) {
            this.mBatteryUsageStatsPollingIntervalMs = 1800000L;
            this.mBatteryUsageStatsPollingMinIntervalMs = 300000L;
        } else {
            this.mBatteryUsageStatsPollingIntervalMs = 2000L;
            this.mBatteryUsageStatsPollingMinIntervalMs = 2000L;
        }
        this.mInjector.setPolicy(new com.android.server.am.AppBatteryTracker.AppBatteryPolicy(this.mInjector, this));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    int getType() {
        return 1;
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() throws java.lang.Throwable {
        super.onSystemReady();
        com.android.server.pm.UserManagerInternal um = this.mInjector.getUserManagerInternal();
        int[] userIds = um.getUserIds();
        for (int userId : userIds) {
            if (um.isUserRunning(userId)) {
                synchronized (this.mLock) {
                    this.mActiveUserIdStates.put(userId, true);
                }
            }
        }
        scheduleBatteryUsageStatsUpdateIfNecessary(this.mBatteryUsageStatsPollingIntervalMs);
    }

    private void scheduleBatteryUsageStatsUpdateIfNecessary(long delay) throws java.lang.Throwable {
        if (((com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy()).isEnabled()) {
            synchronized (this.mLock) {
                if (!this.mBgHandler.hasCallbacks(this.mBgBatteryUsageStatsPolling)) {
                    this.mBgHandler.postDelayed(this.mBgBatteryUsageStatsPolling, delay);
                }
            }
            logAppBatteryTrackerIfNeeded();
        }
    }

    private void logAppBatteryTrackerIfNeeded() throws java.lang.Throwable {
        long now = android.os.SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            com.android.server.am.AppBatteryTracker.AppBatteryPolicy bgPolicy = (com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy();
            if (now - this.mLastReportTime < bgPolicy.mBgCurrentDrainWindowMs) {
                return;
            }
            this.mLastReportTime = now;
            updateBatteryUsageStatsIfNecessary(this.mInjector.currentTimeMillis(), true);
            synchronized (this.mLock) {
                int size = this.mUidBatteryUsageInWindow.size();
                for (int i = 0; i < size; i++) {
                    int uid = this.mUidBatteryUsageInWindow.keyAt(i);
                    if ((android.os.UserHandle.isCore(uid) || android.os.UserHandle.isApp(uid)) && !BATTERY_USAGE_NONE.equals(this.mUidBatteryUsageInWindow.valueAt(i))) {
                        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO, uid, 0, 0, 0, (byte[]) null, getTrackerInfoForStatsd(uid), (byte[]) null, (byte[]) null, 0, 0, 0, android.app.ActivityManager.isLowRamDeviceStatic(), 0);
                    }
                }
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    byte[] getTrackerInfoForStatsd(int uid) {
        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage temp;
        synchronized (this.mLock) {
            temp = this.mUidBatteryUsageInWindow.get(uid);
        }
        if (temp == null) {
            return null;
        }
        com.android.server.am.AppBatteryTracker.BatteryUsage bgUsage = temp.calcPercentage(uid, (com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy());
        double allUsage = bgUsage.mPercentage[0] + bgUsage.mPercentage[1] + bgUsage.mPercentage[2] + bgUsage.mPercentage[3] + bgUsage.mPercentage[4];
        double usageBackground = bgUsage.mPercentage[2];
        double usageFgs = bgUsage.mPercentage[3];
        double usageForeground = bgUsage.mPercentage[1];
        double usageCached = bgUsage.mPercentage[4];
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        proto.write(1120986464257L, allUsage * 10000.0d);
        proto.write(1120986464258L, usageBackground * 10000.0d);
        proto.write(1120986464259L, usageFgs * 10000.0d);
        proto.write(1120986464260L, usageForeground * 10000.0d);
        proto.write(1120986464261L, usageCached * 10000.0d);
        proto.flush();
        return proto.getBytes();
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUserStarted(int userId) {
        synchronized (this.mLock) {
            this.mActiveUserIdStates.put(userId, true);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUserStopped(int userId) {
        synchronized (this.mLock) {
            this.mActiveUserIdStates.put(userId, false);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mActiveUserIdStates.delete(userId);
            for (int i = this.mUidBatteryUsage.size() - 1; i >= 0; i--) {
                if (android.os.UserHandle.getUserId(this.mUidBatteryUsage.keyAt(i)) == userId) {
                    this.mUidBatteryUsage.removeAt(i);
                }
            }
            for (int i2 = this.mUidBatteryUsageInWindow.size() - 1; i2 >= 0; i2--) {
                if (android.os.UserHandle.getUserId(this.mUidBatteryUsageInWindow.keyAt(i2)) == userId) {
                    this.mUidBatteryUsageInWindow.removeAt(i2);
                }
            }
            ((com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy()).onUserRemovedLocked(userId);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUidRemoved(int uid) {
        synchronized (this.mLock) {
            this.mUidBatteryUsage.delete(uid);
            this.mUidBatteryUsageInWindow.delete(uid);
            ((com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy()).onUidRemovedLocked(uid);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUserInteractionStarted(java.lang.String packageName, int uid) {
        ((com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy()).onUserInteractionStarted(packageName, uid);
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onBackgroundRestrictionChanged(int uid, java.lang.String pkgName, boolean restricted) {
        ((com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy()).onBackgroundRestrictionChanged(uid, pkgName, restricted);
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0024 A[Catch: all -> 0x002e, TryCatch #0 {, blocks: (B:5:0x0010, B:6:0x001a, B:8:0x0024, B:10:0x002c, B:9:0x002a), top: B:15:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002a A[Catch: all -> 0x002e, TryCatch #0 {, blocks: (B:5:0x0010, B:6:0x001a, B:8:0x0024, B:10:0x002c, B:9:0x002a), top: B:15:0x0010 }] */
    @Override // com.android.server.am.AppRestrictionController.UidBatteryUsageProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getUidBatteryUsage(int r7) throws java.lang.Throwable {
        /*
            r6 = this;
            com.android.server.am.BaseAppStateTracker$Injector<T extends com.android.server.am.BaseAppStatePolicy> r0 = r6.mInjector
            long r0 = r0.currentTimeMillis()
            r2 = 0
            boolean r2 = r6.updateBatteryUsageStatsIfNecessary(r0, r2)
            java.lang.Object r3 = r6.mLock
            monitor-enter(r3)
            if (r2 == 0) goto L1a
            android.os.Handler r4 = r6.mBgHandler     // Catch: java.lang.Throwable -> L2e
            java.lang.Runnable r5 = r6.mBgBatteryUsageStatsPolling     // Catch: java.lang.Throwable -> L2e
            r4.removeCallbacks(r5)     // Catch: java.lang.Throwable -> L2e
            r6.scheduleBgBatteryUsageStatsCheck()     // Catch: java.lang.Throwable -> L2e
        L1a:
            android.util.SparseArray<com.android.server.am.AppBatteryTracker$BatteryUsage> r4 = r6.mUidBatteryUsage     // Catch: java.lang.Throwable -> L2e
            java.lang.Object r4 = r4.get(r7)     // Catch: java.lang.Throwable -> L2e
            com.android.server.am.AppBatteryTracker$BatteryUsage r4 = (com.android.server.am.AppBatteryTracker.BatteryUsage) r4     // Catch: java.lang.Throwable -> L2e
            if (r4 == 0) goto L2a
            com.android.server.am.AppBatteryTracker$ImmutableBatteryUsage r5 = new com.android.server.am.AppBatteryTracker$ImmutableBatteryUsage     // Catch: java.lang.Throwable -> L2e
            r5.<init>(r4)     // Catch: java.lang.Throwable -> L2e
            goto L2c
        L2a:
            com.android.server.am.AppBatteryTracker$ImmutableBatteryUsage r5 = com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE     // Catch: java.lang.Throwable -> L2e
        L2c:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L2e
            return r5
        L2e:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L2e
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppBatteryTracker.getUidBatteryUsage(int):com.android.server.am.AppBatteryTracker$ImmutableBatteryUsage");
    }

    private void scheduleBgBatteryUsageStatsCheck() {
        if (!this.mBgHandler.hasCallbacks(this.mBgBatteryUsageStatsCheck)) {
            this.mBgHandler.post(this.mBgBatteryUsageStatsCheck);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatteryUsageStatsAndCheck() throws java.lang.Throwable {
        long now = this.mInjector.currentTimeMillis();
        if (updateBatteryUsageStatsIfNecessary(now, false)) {
            checkBatteryUsageStats();
            return;
        }
        synchronized (this.mLock) {
            scheduleBatteryUsageStatsUpdateIfNecessary((this.mLastBatteryUsageSamplingTs + this.mBatteryUsageStatsPollingMinIntervalMs) - now);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkBatteryUsageStats() throws java.lang.Throwable {
        long now = android.os.SystemClock.elapsedRealtime();
        com.android.server.am.AppBatteryTracker.AppBatteryPolicy bgPolicy = (com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy();
        try {
            android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> uidConsumers = this.mTmpUidBatteryUsageInWindow;
            synchronized (this.mLock) {
                copyUidBatteryUsage(this.mUidBatteryUsageInWindow, uidConsumers);
            }
            long since = java.lang.Math.max(0L, now - bgPolicy.mBgCurrentDrainWindowMs);
            int size = uidConsumers.size();
            for (int i = 0; i < size; i++) {
                int uid = uidConsumers.keyAt(i);
                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage actualUsage = uidConsumers.valueAt(i);
                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage exemptedUsage = this.mAppRestrictionController.getUidBatteryExemptedUsageSince(uid, since, now, bgPolicy.mBgCurrentDrainExemptedTypes);
                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage bgUsage = actualUsage.mutate().subtract(exemptedUsage).calcPercentage(uid, bgPolicy).unmutate();
                bgPolicy.handleUidBatteryUsage(uid, bgUsage);
            }
            int size2 = this.mDebugUidPercentages.size();
            for (int i2 = 0; i2 < size2; i2++) {
                bgPolicy.handleUidBatteryUsage(this.mDebugUidPercentages.keyAt(i2), this.mDebugUidPercentages.valueAt(i2));
            }
        } finally {
            scheduleBatteryUsageStatsUpdateIfNecessary(this.mBatteryUsageStatsPollingIntervalMs);
        }
    }

    private boolean updateBatteryUsageStatsIfNecessary(long now, boolean forceUpdate) throws java.lang.Throwable {
        boolean needUpdate = false;
        synchronized (this.mLock) {
            if (this.mLastBatteryUsageSamplingTs + this.mBatteryUsageStatsPollingMinIntervalMs >= now && !forceUpdate) {
                return false;
            }
            if (this.mBatteryUsageStatsUpdatePending) {
                try {
                    this.mLock.wait();
                } catch (java.lang.InterruptedException e) {
                }
            } else {
                this.mBatteryUsageStatsUpdatePending = true;
                needUpdate = true;
            }
            if (needUpdate) {
                updateBatteryUsageStatsOnce(now);
                synchronized (this.mLock) {
                    this.mLastBatteryUsageSamplingTs = now;
                    this.mBatteryUsageStatsUpdatePending = false;
                    this.mLock.notifyAll();
                }
            }
            return true;
        }
    }

    private void updateBatteryUsageStatsOnce(long now) throws java.lang.Throwable {
        android.util.ArraySet<android.os.UserHandle> userIds;
        long curDuration;
        boolean needUpdateUidBatteryUsageInWindow;
        long lastUidBatteryUsageStartTs;
        android.os.BatteryUsageStatsQuery.Builder builder;
        long curDuration2;
        android.os.BatteryStatsInternal batteryStatsInternal;
        long needUpdateUidBatteryUsageInWindow2;
        long lastUidBatteryUsageStartTs2;
        boolean needUpdateUidBatteryUsageInWindow3;
        android.os.BatteryUsageStatsQuery.Builder builder2;
        com.android.server.am.AppBatteryTracker.AppBatteryPolicy bgPolicy = (com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy();
        android.util.ArraySet<android.os.UserHandle> userIds2 = this.mTmpUserIds;
        android.util.SparseArray<com.android.server.am.AppBatteryTracker.BatteryUsage> buf = this.mTmpUidBatteryUsage;
        android.os.BatteryStatsInternal batteryStatsInternal2 = this.mInjector.getBatteryStatsInternal();
        long windowSize = bgPolicy.mBgCurrentDrainWindowMs;
        buf.clear();
        userIds2.clear();
        synchronized (this.mLock) {
            try {
                for (int i = this.mActiveUserIdStates.size() - 1; i >= 0; i--) {
                    try {
                        userIds2.add(android.os.UserHandle.of(this.mActiveUserIdStates.keyAt(i)));
                        if (!this.mActiveUserIdStates.valueAt(i)) {
                            this.mActiveUserIdStates.removeAt(i);
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        while (true) {
                            try {
                                throw th;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                }
                android.os.BatteryUsageStatsQuery.Builder builder3 = new android.os.BatteryUsageStatsQuery.Builder().includeProcessStateData().setMaxStatsAgeMs(0L);
                android.os.BatteryUsageStats stats = updateBatteryUsageStatsOnceInternal(0L, buf, builder3, userIds2, batteryStatsInternal2);
                long builder4 = stats != null ? stats.getStatsStartTimestamp() : 0L;
                long curEnd = stats != null ? stats.getStatsEndTimestamp() : now;
                long curDuration3 = curEnd - builder4;
                if (curDuration3 < windowSize) {
                    userIds = userIds2;
                    curDuration = windowSize;
                    needUpdateUidBatteryUsageInWindow = true;
                } else {
                    synchronized (this.mLock) {
                        try {
                            try {
                                userIds = userIds2;
                                curDuration = windowSize;
                                copyUidBatteryUsage(buf, this.mUidBatteryUsageInWindow, (windowSize * 1.0d) / curDuration3);
                                needUpdateUidBatteryUsageInWindow = false;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                            throw th;
                        }
                    }
                }
                this.mTmpUidBatteryUsage2.clear();
                copyUidBatteryUsage(buf, this.mTmpUidBatteryUsage2);
                synchronized (this.mLock) {
                    try {
                        lastUidBatteryUsageStartTs = this.mLastUidBatteryUsageStartTs;
                        this.mLastUidBatteryUsageStartTs = builder4;
                    } finally {
                        th = th;
                        long j = builder4;
                        boolean z = needUpdateUidBatteryUsageInWindow;
                        long j2 = curDuration;
                        while (true) {
                            try {
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                            }
                        }
                    }
                }
                if (builder4 > lastUidBatteryUsageStartTs && lastUidBatteryUsageStartTs > 0) {
                    android.os.BatteryUsageStatsQuery.Builder builder5 = new android.os.BatteryUsageStatsQuery.Builder().includeProcessStateData().aggregateSnapshots(lastUidBatteryUsageStartTs, builder4);
                    long curStart = builder4;
                    android.os.BatteryUsageStats statsCommit = updateBatteryUsageStatsOnceInternal(0L, buf, builder5, userIds, batteryStatsInternal2);
                    long curDuration4 = curDuration3 + (curStart - lastUidBatteryUsageStartTs);
                    try {
                        if (statsCommit != null) {
                            statsCommit.close();
                        } else {
                            android.util.Slog.w("ActivityManager", "Stat was null");
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.w("ActivityManager", "Failed to close a stat");
                    }
                    curDuration2 = curDuration4;
                    builder = builder5;
                } else {
                    builder = builder3;
                    curDuration2 = curDuration3;
                }
                if (needUpdateUidBatteryUsageInWindow && curDuration >= curDuration) {
                    synchronized (this.mLock) {
                        try {
                            try {
                                needUpdateUidBatteryUsageInWindow2 = curDuration;
                                batteryStatsInternal = batteryStatsInternal2;
                                lastUidBatteryUsageStartTs2 = lastUidBatteryUsageStartTs;
                                copyUidBatteryUsage(buf, this.mUidBatteryUsageInWindow, (needUpdateUidBatteryUsageInWindow2 * 1.0d) / curDuration);
                                needUpdateUidBatteryUsageInWindow3 = false;
                            } catch (java.lang.Throwable th6) {
                                th = th6;
                                throw th;
                            }
                        } catch (java.lang.Throwable th7) {
                            th = th7;
                            throw th;
                        }
                    }
                } else {
                    batteryStatsInternal = batteryStatsInternal2;
                    needUpdateUidBatteryUsageInWindow2 = curDuration;
                    lastUidBatteryUsageStartTs2 = lastUidBatteryUsageStartTs;
                    needUpdateUidBatteryUsageInWindow3 = needUpdateUidBatteryUsageInWindow;
                }
                synchronized (this.mLock) {
                    int i2 = 0;
                    try {
                        int size = buf.size();
                        while (i2 < size) {
                            try {
                                int uid = buf.keyAt(i2);
                                int index = this.mUidBatteryUsage.indexOfKey(uid);
                                com.android.server.am.AppBatteryTracker.BatteryUsage lastUsage = this.mLastUidBatteryUsage.get(uid, BATTERY_USAGE_NONE);
                                com.android.server.am.AppBatteryTracker.BatteryUsage curUsage = buf.valueAt(i2);
                                if (index >= 0) {
                                    com.android.server.am.AppBatteryTracker.BatteryUsage totalUsage = this.mUidBatteryUsage.valueAt(index);
                                    com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage immutableBatteryUsage = BATTERY_USAGE_NONE;
                                    builder2 = builder;
                                    try {
                                        totalUsage.subtract(lastUsage).add(curUsage);
                                    } catch (java.lang.Throwable th8) {
                                        th = th8;
                                        while (true) {
                                            try {
                                                throw th;
                                            } catch (java.lang.Throwable th9) {
                                                th = th9;
                                            }
                                        }
                                    }
                                } else {
                                    builder2 = builder;
                                    com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage immutableBatteryUsage2 = BATTERY_USAGE_NONE;
                                    this.mUidBatteryUsage.put(uid, curUsage);
                                }
                                i2++;
                                builder = builder2;
                            } catch (java.lang.Throwable th10) {
                                th = th10;
                            }
                        }
                        try {
                            copyUidBatteryUsage(this.mTmpUidBatteryUsage2, this.mLastUidBatteryUsage);
                            this.mTmpUidBatteryUsage2.clear();
                            if (needUpdateUidBatteryUsageInWindow3) {
                                long start = now - needUpdateUidBatteryUsageInWindow;
                                long end = lastUidBatteryUsageStartTs2 - 1;
                                updateBatteryUsageStatsOnceInternal(end - start, buf, new android.os.BatteryUsageStatsQuery.Builder().includeProcessStateData().aggregateSnapshots(start, end), userIds, batteryStatsInternal);
                                synchronized (this.mLock) {
                                    copyUidBatteryUsage(buf, this.mUidBatteryUsageInWindow);
                                }
                            }
                            try {
                                if (stats != null) {
                                    stats.close();
                                } else {
                                    android.util.Slog.w("ActivityManager", "Stat was null");
                                }
                            } catch (java.io.IOException e2) {
                                android.util.Slog.w("ActivityManager", "Failed to close a stat");
                            }
                        } catch (java.lang.Throwable th11) {
                            th = th11;
                            while (true) {
                                throw th;
                            }
                        }
                    } catch (java.lang.Throwable th12) {
                        th = th12;
                    }
                }
            } catch (java.lang.Throwable th13) {
                th = th13;
            }
        }
    }

    private android.os.BatteryUsageStats updateBatteryUsageStatsOnceInternal(long expectedDuration, android.util.SparseArray<com.android.server.am.AppBatteryTracker.BatteryUsage> buf, android.os.BatteryUsageStatsQuery.Builder builder, android.util.ArraySet<android.os.UserHandle> userIds, android.os.BatteryStatsInternal batteryStatsInternal) {
        int size = userIds.size();
        for (int i = 0; i < size; i++) {
            builder.addUser(userIds.valueAt(i));
        }
        java.util.List<android.os.BatteryUsageStats> statsList = batteryStatsInternal.getBatteryUsageStats(java.util.Arrays.asList(builder.build()));
        if (com.android.internal.util.ArrayUtils.isEmpty(statsList)) {
            return null;
        }
        android.os.BatteryUsageStats stats = statsList.get(0);
        for (int i2 = 1; i2 < statsList.size(); i2++) {
            try {
                if (statsList.get(i2) != null) {
                    statsList.get(i2).close();
                } else {
                    android.util.Slog.w("ActivityManager", "Stat was null");
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w("ActivityManager", "Failed to close a stat in BatteryUsageStats List");
            }
        }
        java.util.List<android.os.UidBatteryConsumer> uidConsumers = stats.getUidBatteryConsumers();
        if (uidConsumers != null) {
            long start = stats.getStatsStartTimestamp();
            long end = stats.getStatsEndTimestamp();
            double scale = expectedDuration > 0 ? java.lang.Math.min((expectedDuration * 1.0d) / (end - start), 1.0d) : 1.0d;
            com.android.server.am.AppBatteryTracker.AppBatteryPolicy bgPolicy = (com.android.server.am.AppBatteryTracker.AppBatteryPolicy) this.mInjector.getPolicy();
            for (android.os.UidBatteryConsumer uidConsumer : uidConsumers) {
                java.util.List<android.os.UidBatteryConsumer> uidConsumers2 = uidConsumers;
                int rawUid = uidConsumer.getUid();
                if (android.os.UserHandle.isIsolated(rawUid)) {
                    uidConsumers = uidConsumers2;
                } else if (rawUid < 0) {
                    android.util.Slog.e("ActivityManager", "updateBatteryUsageStatsOnceInternal uid=" + rawUid + " from UidBatteryConsumer is negative.");
                    uidConsumers = uidConsumers2;
                } else {
                    int uid = rawUid;
                    int sharedAppId = android.os.UserHandle.getAppIdFromSharedAppGid(uid);
                    if (sharedAppId > 0) {
                        uid = android.os.UserHandle.getUid(0, sharedAppId);
                    }
                    com.android.server.am.AppBatteryTracker.BatteryUsage bgUsage = new com.android.server.am.AppBatteryTracker.BatteryUsage(uidConsumer, bgPolicy).scale(scale);
                    int index = buf.indexOfKey(uid);
                    if (index < 0) {
                        buf.put(uid, bgUsage);
                    } else {
                        com.android.server.am.AppBatteryTracker.BatteryUsage before = buf.valueAt(index);
                        before.add(bgUsage);
                    }
                    uidConsumers = uidConsumers2;
                }
            }
        }
        return stats;
    }

    private static void copyUidBatteryUsage(android.util.SparseArray<? extends com.android.server.am.AppBatteryTracker.BatteryUsage> source, android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> dest) {
        dest.clear();
        for (int i = source.size() - 1; i >= 0; i--) {
            dest.put(source.keyAt(i), new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage(source.valueAt(i)));
        }
    }

    private static void copyUidBatteryUsage(android.util.SparseArray<? extends com.android.server.am.AppBatteryTracker.BatteryUsage> source, android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> dest, double scale) {
        dest.clear();
        for (int i = source.size() - 1; i >= 0; i--) {
            dest.put(source.keyAt(i), new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage(source.valueAt(i), scale));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCurrentDrainMonitorEnabled(boolean enabled) {
        if (enabled) {
            if (!this.mBgHandler.hasCallbacks(this.mBgBatteryUsageStatsPolling)) {
                this.mBgHandler.postDelayed(this.mBgBatteryUsageStatsPolling, this.mBatteryUsageStatsPollingIntervalMs);
                return;
            }
            return;
        }
        this.mBgHandler.removeCallbacks(this.mBgBatteryUsageStatsPolling);
        synchronized (this.mLock) {
            if (this.mBatteryUsageStatsUpdatePending) {
                try {
                    this.mLock.wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
            this.mUidBatteryUsage.clear();
            this.mUidBatteryUsageInWindow.clear();
            this.mLastUidBatteryUsage.clear();
            this.mLastBatteryUsageSamplingTs = 0L;
            this.mLastUidBatteryUsageStartTs = 0L;
        }
    }

    void setDebugUidPercentage(int[] uids, double[][] percentages) {
        this.mDebugUidPercentages.clear();
        for (int i = 0; i < uids.length; i++) {
            this.mDebugUidPercentages.put(uids[i], new com.android.server.am.AppBatteryTracker.BatteryUsage().setPercentage(percentages[i]).unmutate());
        }
        scheduleBgBatteryUsageStatsCheck();
    }

    void clearDebugUidPercentage() {
        this.mDebugUidPercentages.clear();
        scheduleBgBatteryUsageStatsCheck();
    }

    void reset() throws java.lang.Throwable {
        synchronized (this.mLock) {
            this.mUidBatteryUsage.clear();
            this.mUidBatteryUsageInWindow.clear();
            this.mLastUidBatteryUsage.clear();
            this.mLastBatteryUsageSamplingTs = 0L;
            this.mLastUidBatteryUsageStartTs = 0L;
        }
        this.mBgHandler.removeCallbacks(this.mBgBatteryUsageStatsPolling);
        updateBatteryUsageStatsAndCheck();
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) throws java.lang.Throwable {
        com.android.server.am.AppBatteryTracker appBatteryTracker = this;
        pw.print(prefix);
        pw.println("APP BATTERY STATE TRACKER:");
        appBatteryTracker.updateBatteryUsageStatsIfNecessary(appBatteryTracker.mInjector.currentTimeMillis(), true);
        scheduleBgBatteryUsageStatsCheck();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        appBatteryTracker.mBgHandler.getLooper().getQueue().addIdleHandler(new android.os.MessageQueue.IdleHandler() { // from class: com.android.server.am.AppBatteryTracker$$ExternalSyntheticLambda2
            @Override // android.os.MessageQueue.IdleHandler
            public final boolean queueIdle() {
                return com.android.server.am.AppBatteryTracker.lambda$dump$0(latch);
            }
        });
        try {
            latch.await();
        } catch (java.lang.InterruptedException e) {
        }
        synchronized (appBatteryTracker.mLock) {
            android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> uidConsumers = appBatteryTracker.mUidBatteryUsageInWindow;
            pw.print("  " + prefix);
            pw.print("  Last battery usage start=");
            android.util.TimeUtils.dumpTime(pw, appBatteryTracker.mLastUidBatteryUsageStartTs);
            pw.println();
            pw.print("  " + prefix);
            pw.print("Battery usage over last ");
            java.lang.String newPrefix = "    " + prefix;
            com.android.server.am.AppBatteryTracker.AppBatteryPolicy bgPolicy = (com.android.server.am.AppBatteryTracker.AppBatteryPolicy) appBatteryTracker.mInjector.getPolicy();
            long now = android.os.SystemClock.elapsedRealtime();
            long since = java.lang.Math.max(0L, now - bgPolicy.mBgCurrentDrainWindowMs);
            pw.println(android.util.TimeUtils.formatDuration(now - since));
            if (uidConsumers.size() == 0) {
                pw.print(newPrefix);
                pw.println("(none)");
            } else {
                int size = uidConsumers.size();
                int i = 0;
                while (i < size) {
                    int uid = uidConsumers.keyAt(i);
                    com.android.server.am.AppBatteryTracker.BatteryUsage bgUsage = uidConsumers.valueAt(i).calcPercentage(uid, bgPolicy);
                    com.android.server.am.AppBatteryTracker.BatteryUsage exemptedUsage = appBatteryTracker.mAppRestrictionController.getUidBatteryExemptedUsageSince(uid, since, now, bgPolicy.mBgCurrentDrainExemptedTypes).calcPercentage(uid, bgPolicy);
                    com.android.server.am.AppBatteryTracker.BatteryUsage reportedUsage = new com.android.server.am.AppBatteryTracker.BatteryUsage(bgUsage).subtract(exemptedUsage).calcPercentage(uid, bgPolicy);
                    pw.format("%s%s: [%s] %s (%s) | %s (%s) | %s (%s) | %s\n", newPrefix, android.os.UserHandle.formatUid(uid), android.os.PowerExemptionManager.reasonCodeToString(bgPolicy.shouldExemptUid(uid)), bgUsage.toString(), bgUsage.percentageToString(), exemptedUsage.toString(), exemptedUsage.percentageToString(), reportedUsage.toString(), reportedUsage.percentageToString(), appBatteryTracker.mUidBatteryUsage.get(uid, BATTERY_USAGE_NONE).toString());
                    i++;
                    appBatteryTracker = this;
                    size = size;
                    bgPolicy = bgPolicy;
                    uidConsumers = uidConsumers;
                }
            }
        }
        super.dump(pw, prefix);
    }

    static /* synthetic */ boolean lambda$dump$0(java.util.concurrent.CountDownLatch latch) {
        latch.countDown();
        return false;
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void dumpAsProto(android.util.proto.ProtoOutputStream proto, int uid) throws java.lang.Throwable {
        updateBatteryUsageStatsIfNecessary(this.mInjector.currentTimeMillis(), true);
        synchronized (this.mLock) {
            android.util.SparseArray<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> uidConsumers = this.mUidBatteryUsageInWindow;
            if (uid != -1) {
                com.android.server.am.AppBatteryTracker.BatteryUsage usage = uidConsumers.get(uid);
                if (usage != null) {
                    dumpUidStats(proto, uid, usage);
                }
            } else {
                int size = uidConsumers.size();
                for (int i = 0; i < size; i++) {
                    int aUid = uidConsumers.keyAt(i);
                    dumpUidStats(proto, aUid, uidConsumers.valueAt(i));
                }
            }
        }
    }

    private void dumpUidStats(android.util.proto.ProtoOutputStream proto, int uid, com.android.server.am.AppBatteryTracker.BatteryUsage usage) {
        if (usage.mUsage == null) {
            return;
        }
        double foregroundUsage = usage.getUsagePowerMah(1);
        double backgroundUsage = usage.getUsagePowerMah(2);
        double fgsUsage = usage.getUsagePowerMah(3);
        double cachedUsage = usage.getUsagePowerMah(4);
        if (foregroundUsage == 0.0d && backgroundUsage == 0.0d && fgsUsage == 0.0d) {
            return;
        }
        long token = proto.start(2246267895809L);
        proto.write(1120986464257L, uid);
        dumpProcessStateStats(proto, 1, foregroundUsage);
        dumpProcessStateStats(proto, 2, backgroundUsage);
        dumpProcessStateStats(proto, 3, fgsUsage);
        dumpProcessStateStats(proto, 4, cachedUsage);
        proto.end(token);
    }

    private void dumpProcessStateStats(android.util.proto.ProtoOutputStream proto, int processState, double powerMah) {
        if (powerMah == 0.0d) {
            return;
        }
        long token = proto.start(2246267895810L);
        proto.write(1159641169921L, processState);
        proto.write(1103806595075L, powerMah);
        proto.end(token);
    }

    static class BatteryUsage {
        static final int BATTERY_USAGE_COUNT = 5;
        static final int BATTERY_USAGE_INDEX_BACKGROUND = 2;
        static final int BATTERY_USAGE_INDEX_CACHED = 4;
        static final int BATTERY_USAGE_INDEX_FOREGROUND = 1;
        static final int BATTERY_USAGE_INDEX_FOREGROUND_SERVICE = 3;
        static final int BATTERY_USAGE_INDEX_UNSPECIFIED = 0;
        static final android.os.BatteryConsumer.Dimensions[] BATT_DIMENS = {new android.os.BatteryConsumer.Dimensions(-1, 0), new android.os.BatteryConsumer.Dimensions(-1, 1), new android.os.BatteryConsumer.Dimensions(-1, 2), new android.os.BatteryConsumer.Dimensions(-1, 3), new android.os.BatteryConsumer.Dimensions(-1, 4)};
        double[] mPercentage;
        double[] mUsage;

        BatteryUsage() {
            this(0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
        }

        BatteryUsage(double unspecifiedUsage, double fgUsage, double bgUsage, double fgsUsage, double cachedUsage) {
            this.mUsage = new double[]{unspecifiedUsage, fgUsage, bgUsage, fgsUsage, cachedUsage};
        }

        BatteryUsage(double[] usage) {
            this.mUsage = usage;
        }

        BatteryUsage(com.android.server.am.AppBatteryTracker.BatteryUsage other, double scale) {
            this(other);
            scaleInternal(scale);
        }

        BatteryUsage(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            this.mUsage = new double[other.mUsage.length];
            setToInternal(other);
        }

        BatteryUsage(android.os.UidBatteryConsumer consumer, com.android.server.am.AppBatteryTracker.AppBatteryPolicy policy) {
            android.os.BatteryConsumer.Dimensions[] dims = policy.mBatteryDimensions;
            this.mUsage = new double[]{getConsumedPowerNoThrow(consumer, dims[0]), getConsumedPowerNoThrow(consumer, dims[1]), getConsumedPowerNoThrow(consumer, dims[2]), getConsumedPowerNoThrow(consumer, dims[3]), getConsumedPowerNoThrow(consumer, dims[4])};
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage setTo(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            return setToInternal(other);
        }

        private com.android.server.am.AppBatteryTracker.BatteryUsage setToInternal(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            java.lang.System.arraycopy(other.mUsage, 0, this.mUsage, 0, other.mUsage.length);
            if (other.mPercentage != null) {
                this.mPercentage = new double[other.mPercentage.length];
                java.lang.System.arraycopy(other.mPercentage, 0, this.mPercentage, 0, other.mPercentage.length);
            } else {
                this.mPercentage = null;
            }
            return this;
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage add(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            for (int i = 0; i < other.mUsage.length; i++) {
                double[] dArr = this.mUsage;
                dArr[i] = dArr[i] + other.mUsage[i];
            }
            return this;
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage subtract(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            for (int i = 0; i < other.mUsage.length; i++) {
                this.mUsage[i] = java.lang.Math.max(0.0d, this.mUsage[i] - other.mUsage[i]);
            }
            return this;
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage scale(double scale) {
            return scaleInternal(scale);
        }

        private com.android.server.am.AppBatteryTracker.BatteryUsage scaleInternal(double scale) {
            for (int i = 0; i < this.mUsage.length; i++) {
                double[] dArr = this.mUsage;
                dArr[i] = dArr[i] * scale;
            }
            return this;
        }

        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage unmutate() {
            return new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage(this);
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage calcPercentage(int uid, com.android.server.am.AppBatteryTracker.AppBatteryPolicy policy) {
            if (this.mPercentage == null || this.mPercentage.length != this.mUsage.length) {
                this.mPercentage = new double[this.mUsage.length];
            }
            policy.calcPercentage(uid, this.mUsage, this.mPercentage);
            return this;
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage setPercentage(double[] percentage) {
            this.mPercentage = percentage;
            return this;
        }

        double[] getPercentage() {
            return this.mPercentage;
        }

        java.lang.String percentageToString() {
            return formatBatteryUsagePercentage(this.mPercentage);
        }

        public java.lang.String toString() {
            return formatBatteryUsage(this.mUsage);
        }

        double getUsagePowerMah(int processState) {
            switch (processState) {
                case 1:
                    return this.mUsage[1];
                case 2:
                    return this.mUsage[2];
                case 3:
                    return this.mUsage[3];
                case 4:
                    return this.mUsage[4];
                default:
                    return 0.0d;
            }
        }

        boolean isValid() {
            for (int i = 0; i < this.mUsage.length; i++) {
                if (this.mUsage[i] < 0.0d) {
                    return false;
                }
            }
            return true;
        }

        boolean isEmpty() {
            for (int i = 0; i < this.mUsage.length; i++) {
                if (this.mUsage[i] > 0.0d) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(java.lang.Object other) {
            if (other == null) {
                return false;
            }
            com.android.server.am.AppBatteryTracker.BatteryUsage otherUsage = (com.android.server.am.AppBatteryTracker.BatteryUsage) other;
            for (int i = 0; i < this.mUsage.length; i++) {
                if (java.lang.Double.compare(this.mUsage[i], otherUsage.mUsage[i]) != 0) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int hashCode = 0;
            for (int i = 0; i < this.mUsage.length; i++) {
                hashCode = java.lang.Double.hashCode(this.mUsage[i]) + (hashCode * 31);
            }
            return hashCode;
        }

        private static java.lang.String formatBatteryUsage(double[] usage) {
            return java.lang.String.format("%.3f %.3f %.3f %.3f %.3f mAh", java.lang.Double.valueOf(usage[0]), java.lang.Double.valueOf(usage[1]), java.lang.Double.valueOf(usage[2]), java.lang.Double.valueOf(usage[3]), java.lang.Double.valueOf(usage[4]));
        }

        static java.lang.String formatBatteryUsagePercentage(double[] percentage) {
            return java.lang.String.format("%4.2f%% %4.2f%% %4.2f%% %4.2f%% %4.2f%%", java.lang.Double.valueOf(percentage[0]), java.lang.Double.valueOf(percentage[1]), java.lang.Double.valueOf(percentage[2]), java.lang.Double.valueOf(percentage[3]), java.lang.Double.valueOf(percentage[4]));
        }

        private static double getConsumedPowerNoThrow(android.os.UidBatteryConsumer uidConsumer, android.os.BatteryConsumer.Dimensions dimens) {
            try {
                return uidConsumer.getConsumedPower(dimens);
            } catch (java.lang.IllegalArgumentException e) {
                return 0.0d;
            }
        }
    }

    static final class ImmutableBatteryUsage extends com.android.server.am.AppBatteryTracker.BatteryUsage {
        ImmutableBatteryUsage() {
        }

        ImmutableBatteryUsage(double unspecifiedUsage, double fgUsage, double bgUsage, double fgsUsage, double cachedUsage) {
            super(unspecifiedUsage, fgUsage, bgUsage, fgsUsage, cachedUsage);
        }

        ImmutableBatteryUsage(double[] usage) {
            super(usage);
        }

        ImmutableBatteryUsage(com.android.server.am.AppBatteryTracker.BatteryUsage other, double scale) {
            super(other, scale);
        }

        ImmutableBatteryUsage(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            super(other);
        }

        ImmutableBatteryUsage(android.os.UidBatteryConsumer consumer, com.android.server.am.AppBatteryTracker.AppBatteryPolicy policy) {
            super(consumer, policy);
        }

        @Override // com.android.server.am.AppBatteryTracker.BatteryUsage
        com.android.server.am.AppBatteryTracker.BatteryUsage setTo(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            throw new java.lang.RuntimeException("Readonly");
        }

        @Override // com.android.server.am.AppBatteryTracker.BatteryUsage
        com.android.server.am.AppBatteryTracker.BatteryUsage add(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            throw new java.lang.RuntimeException("Readonly");
        }

        @Override // com.android.server.am.AppBatteryTracker.BatteryUsage
        com.android.server.am.AppBatteryTracker.BatteryUsage subtract(com.android.server.am.AppBatteryTracker.BatteryUsage other) {
            throw new java.lang.RuntimeException("Readonly");
        }

        @Override // com.android.server.am.AppBatteryTracker.BatteryUsage
        com.android.server.am.AppBatteryTracker.BatteryUsage scale(double scale) {
            throw new java.lang.RuntimeException("Readonly");
        }

        @Override // com.android.server.am.AppBatteryTracker.BatteryUsage
        com.android.server.am.AppBatteryTracker.BatteryUsage setPercentage(double[] percentage) {
            throw new java.lang.RuntimeException("Readonly");
        }

        com.android.server.am.AppBatteryTracker.BatteryUsage mutate() {
            return new com.android.server.am.AppBatteryTracker.BatteryUsage(this);
        }
    }

    static final class AppBatteryPolicy extends com.android.server.am.BaseAppStatePolicy<com.android.server.am.AppBatteryTracker> {
        static final int BATTERY_USAGE_TYPE_BACKGROUND = 4;
        static final int BATTERY_USAGE_TYPE_CACHED = 16;
        static final int BATTERY_USAGE_TYPE_FOREGROUND = 2;
        static final int BATTERY_USAGE_TYPE_FOREGROUND_SERVICE = 8;
        static final int BATTERY_USAGE_TYPE_UNSPECIFIED = 1;
        static final boolean DEFAULT_BG_CURRENT_DRAIN_DECOUPLE_THRESHOLD = true;
        static final int DEFAULT_BG_CURRENT_DRAIN_POWER_COMPONENTS = -1;
        static final int INDEX_HIGH_CURRENT_DRAIN_THRESHOLD = 1;
        static final int INDEX_REGULAR_CURRENT_DRAIN_THRESHOLD = 0;
        static final java.lang.String KEY_BG_CURRENT_DRAIN_AUTO_RESTRICT_ABUSIVE_APPS_ENABLED = "bg_current_drain_auto_restrict_abusive_apps_enabled";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_DECOUPLE_THRESHOLDS = "bg_current_drain_decouple_thresholds";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_EVENT_DURATION_BASED_THRESHOLD_ENABLED = "bg_current_drain_event_duration_based_threshold_enabled";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_EXEMPTED_TYPES = "bg_current_drain_exempted_types";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_BY_BG_LOCATION = "bg_current_drain_high_threshold_by_bg_location";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_TO_BG_RESTRICTED = "bg_current_drain_high_threshold_to_bg_restricted";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_TO_RESTRICTED_BUCKET = "bg_current_drain_high_threshold_to_restricted_bucket";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_INTERACTION_GRACE_PERIOD = "bg_current_drain_interaction_grace_period";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_LOCATION_MIN_DURATION = "bg_current_drain_location_min_duration";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_MEDIA_PLAYBACK_MIN_DURATION = "bg_current_drain_media_playback_min_duration";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_MONITOR_ENABLED = "bg_current_drain_monitor_enabled";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_POWER_COMPONENTS = "bg_current_drain_power_components";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_THRESHOLD_TO_BG_RESTRICTED = "bg_current_drain_threshold_to_bg_restricted";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_THRESHOLD_TO_RESTRICTED_BUCKET = "bg_current_drain_threshold_to_restricted_bucket";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_TYPES_TO_BG_RESTRICTED = "bg_current_drain_types_to_bg_restricted";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_TYPES_TO_RESTRICTED_BUCKET = "bg_current_drain_types_to_restricted_bucket";
        static final java.lang.String KEY_BG_CURRENT_DRAIN_WINDOW = "bg_current_drain_window";
        private static final int TIME_STAMP_INDEX_BG_RESTRICTED = 1;
        private static final int TIME_STAMP_INDEX_LAST = 2;
        private static final int TIME_STAMP_INDEX_RESTRICTED_BUCKET = 0;
        volatile android.os.BatteryConsumer.Dimensions[] mBatteryDimensions;
        private int mBatteryFullChargeMah;
        volatile boolean mBgCurrentDrainAutoRestrictAbusiveAppsEnabled;
        volatile float[] mBgCurrentDrainBgRestrictedThreshold;
        volatile int mBgCurrentDrainBgRestrictedTypes;
        volatile boolean mBgCurrentDrainDecoupleThresholds;
        volatile boolean mBgCurrentDrainEventDurationBasedThresholdEnabled;
        volatile int mBgCurrentDrainExemptedTypes;
        volatile boolean mBgCurrentDrainHighThresholdByBgLocation;
        volatile long mBgCurrentDrainInteractionGracePeriodMs;
        volatile long mBgCurrentDrainLocationMinDuration;
        volatile long mBgCurrentDrainMediaPlaybackMinDuration;
        volatile int mBgCurrentDrainPowerComponents;
        volatile float[] mBgCurrentDrainRestrictedBucketThreshold;
        volatile int mBgCurrentDrainRestrictedBucketTypes;
        volatile long mBgCurrentDrainWindowMs;
        final boolean mDefaultBgCurrentDrainAutoRestrictAbusiveAppsEnabled;
        final float mDefaultBgCurrentDrainBgRestrictedHighThreshold;
        final float mDefaultBgCurrentDrainBgRestrictedThreshold;
        final boolean mDefaultBgCurrentDrainEventDurationBasedThresholdEnabled;
        final int mDefaultBgCurrentDrainExemptedTypes;
        final boolean mDefaultBgCurrentDrainHighThresholdByBgLocation;
        final long mDefaultBgCurrentDrainInteractionGracePeriodMs;
        final long mDefaultBgCurrentDrainLocationMinDuration;
        final long mDefaultBgCurrentDrainMediaPlaybackMinDuration;
        final int mDefaultBgCurrentDrainPowerComponent;
        final float mDefaultBgCurrentDrainRestrictedBucket;
        final float mDefaultBgCurrentDrainRestrictedBucketHighThreshold;
        final int mDefaultBgCurrentDrainTypesToBgRestricted;
        final long mDefaultBgCurrentDrainWindowMs;
        final int mDefaultCurrentDrainTypesToRestrictedBucket;
        private final android.util.SparseArray<android.util.Pair<long[], com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]>> mHighBgBatteryPackages;
        private final android.util.SparseLongArray mLastInteractionTime;
        private final java.lang.Object mLock;

        AppBatteryPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, com.android.server.am.AppBatteryTracker tracker) {
            super(injector, tracker, KEY_BG_CURRENT_DRAIN_MONITOR_ENABLED, tracker.mContext.getResources().getBoolean(android.R.bool.config_bg_current_drain_auto_restrict_abusive_apps));
            this.mBgCurrentDrainRestrictedBucketThreshold = new float[2];
            this.mBgCurrentDrainBgRestrictedThreshold = new float[2];
            this.mHighBgBatteryPackages = new android.util.SparseArray<>();
            this.mLastInteractionTime = new android.util.SparseLongArray();
            this.mLock = tracker.mLock;
            android.content.res.Resources resources = tracker.mContext.getResources();
            float[] val = getFloatArray(resources.obtainTypedArray(android.R.array.config_bg_current_drain_high_threshold_to_bg_restricted));
            this.mDefaultBgCurrentDrainRestrictedBucket = android.app.ActivityManager.isLowRamDeviceStatic() ? val[1] : val[0];
            float[] val2 = getFloatArray(resources.obtainTypedArray(android.R.array.config_batteryPackageTypeSystem));
            this.mDefaultBgCurrentDrainBgRestrictedThreshold = android.app.ActivityManager.isLowRamDeviceStatic() ? val2[1] : val2[0];
            this.mDefaultBgCurrentDrainWindowMs = resources.getInteger(android.R.integer.config_batterySaver_full_soundTriggerMode) * 1000;
            this.mDefaultBgCurrentDrainInteractionGracePeriodMs = this.mDefaultBgCurrentDrainWindowMs;
            float[] val3 = getFloatArray(resources.obtainTypedArray(android.R.array.config_batteryPackageTypeService));
            this.mDefaultBgCurrentDrainRestrictedBucketHighThreshold = android.app.ActivityManager.isLowRamDeviceStatic() ? val3[1] : val3[0];
            float[] val4 = getFloatArray(resources.obtainTypedArray(android.R.array.config_backupHealthConnectDataAndSettingsKnownSigners));
            this.mDefaultBgCurrentDrainBgRestrictedHighThreshold = android.app.ActivityManager.isLowRamDeviceStatic() ? val4[1] : val4[0];
            this.mDefaultBgCurrentDrainMediaPlaybackMinDuration = resources.getInteger(android.R.integer.config_autoPowerModeThresholdAngle) * 1000;
            this.mDefaultBgCurrentDrainLocationMinDuration = resources.getInteger(android.R.integer.config_autoPowerModeAnyMotionSensor) * 1000;
            this.mDefaultBgCurrentDrainEventDurationBasedThresholdEnabled = resources.getBoolean(android.R.bool.config_battery_percentage_setting_available);
            this.mDefaultBgCurrentDrainAutoRestrictAbusiveAppsEnabled = resources.getBoolean(android.R.bool.config_batteryStatsResetOnUnplugHighBatteryLevel);
            this.mDefaultCurrentDrainTypesToRestrictedBucket = resources.getInteger(android.R.integer.config_batterySaver_full_locationMode);
            this.mDefaultBgCurrentDrainTypesToBgRestricted = resources.getInteger(android.R.integer.config_batteryHistoryStorageSize);
            this.mDefaultBgCurrentDrainPowerComponent = resources.getInteger(android.R.integer.config_backgroundUserScheduledStopTimeSecs);
            this.mDefaultBgCurrentDrainExemptedTypes = resources.getInteger(android.R.integer.config_autoGroupAtCount);
            this.mDefaultBgCurrentDrainHighThresholdByBgLocation = resources.getBoolean(android.R.bool.config_batterymeterDualTone);
            this.mBgCurrentDrainRestrictedBucketThreshold[0] = this.mDefaultBgCurrentDrainRestrictedBucket;
            this.mBgCurrentDrainRestrictedBucketThreshold[1] = this.mDefaultBgCurrentDrainRestrictedBucketHighThreshold;
            this.mBgCurrentDrainBgRestrictedThreshold[0] = this.mDefaultBgCurrentDrainBgRestrictedThreshold;
            this.mBgCurrentDrainBgRestrictedThreshold[1] = this.mDefaultBgCurrentDrainBgRestrictedHighThreshold;
            this.mBgCurrentDrainWindowMs = this.mDefaultBgCurrentDrainWindowMs;
            this.mBgCurrentDrainInteractionGracePeriodMs = this.mDefaultBgCurrentDrainInteractionGracePeriodMs;
            this.mBgCurrentDrainMediaPlaybackMinDuration = this.mDefaultBgCurrentDrainMediaPlaybackMinDuration;
            this.mBgCurrentDrainLocationMinDuration = this.mDefaultBgCurrentDrainLocationMinDuration;
        }

        static float[] getFloatArray(android.content.res.TypedArray array) {
            int length = array.length();
            float[] floatArray = new float[length];
            for (int i = 0; i < length; i++) {
                floatArray[i] = array.getFloat(i, Float.NaN);
            }
            array.recycle();
            return floatArray;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00b7  */
        @Override // com.android.server.am.BaseAppStatePolicy
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(java.lang.String r2) {
            /*
                Method dump skipped, instruction units count: 330
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppBatteryTracker.AppBatteryPolicy.onPropertiesChanged(java.lang.String):void");
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        void updateTrackerEnabled() {
            if (this.mBatteryFullChargeMah > 0) {
                super.updateTrackerEnabled();
            } else {
                this.mTrackerEnabled = false;
                onTrackerEnabled(false);
            }
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean enabled) {
            ((com.android.server.am.AppBatteryTracker) this.mTracker).onCurrentDrainMonitorEnabled(enabled);
        }

        private void updateCurrentDrainThreshold() {
            this.mBgCurrentDrainRestrictedBucketThreshold[0] = android.provider.DeviceConfig.getFloat("activity_manager", KEY_BG_CURRENT_DRAIN_THRESHOLD_TO_RESTRICTED_BUCKET, this.mDefaultBgCurrentDrainRestrictedBucket);
            this.mBgCurrentDrainRestrictedBucketThreshold[1] = android.provider.DeviceConfig.getFloat("activity_manager", KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_TO_RESTRICTED_BUCKET, this.mDefaultBgCurrentDrainRestrictedBucketHighThreshold);
            this.mBgCurrentDrainBgRestrictedThreshold[0] = android.provider.DeviceConfig.getFloat("activity_manager", KEY_BG_CURRENT_DRAIN_THRESHOLD_TO_BG_RESTRICTED, this.mDefaultBgCurrentDrainBgRestrictedThreshold);
            this.mBgCurrentDrainBgRestrictedThreshold[1] = android.provider.DeviceConfig.getFloat("activity_manager", KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_TO_BG_RESTRICTED, this.mDefaultBgCurrentDrainBgRestrictedHighThreshold);
            this.mBgCurrentDrainRestrictedBucketTypes = android.provider.DeviceConfig.getInt("activity_manager", KEY_BG_CURRENT_DRAIN_TYPES_TO_RESTRICTED_BUCKET, this.mDefaultCurrentDrainTypesToRestrictedBucket);
            this.mBgCurrentDrainBgRestrictedTypes = android.provider.DeviceConfig.getInt("activity_manager", KEY_BG_CURRENT_DRAIN_TYPES_TO_BG_RESTRICTED, this.mDefaultBgCurrentDrainTypesToBgRestricted);
            this.mBgCurrentDrainPowerComponents = android.provider.DeviceConfig.getInt("activity_manager", KEY_BG_CURRENT_DRAIN_POWER_COMPONENTS, this.mDefaultBgCurrentDrainPowerComponent);
            if (this.mBgCurrentDrainPowerComponents == -1) {
                this.mBatteryDimensions = com.android.server.am.AppBatteryTracker.BatteryUsage.BATT_DIMENS;
            } else {
                this.mBatteryDimensions = new android.os.BatteryConsumer.Dimensions[5];
                for (int i = 0; i < 5; i++) {
                    this.mBatteryDimensions[i] = new android.os.BatteryConsumer.Dimensions(this.mBgCurrentDrainPowerComponents, i);
                }
            }
            this.mBgCurrentDrainHighThresholdByBgLocation = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_BY_BG_LOCATION, this.mDefaultBgCurrentDrainHighThresholdByBgLocation);
        }

        private void updateCurrentDrainWindow() {
            this.mBgCurrentDrainWindowMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_CURRENT_DRAIN_WINDOW, this.mDefaultBgCurrentDrainWindowMs);
        }

        private void updateCurrentDrainInteractionGracePeriod() {
            this.mBgCurrentDrainInteractionGracePeriodMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_CURRENT_DRAIN_INTERACTION_GRACE_PERIOD, this.mDefaultBgCurrentDrainInteractionGracePeriodMs);
        }

        private void updateCurrentDrainMediaPlaybackMinDuration() {
            this.mBgCurrentDrainMediaPlaybackMinDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_CURRENT_DRAIN_MEDIA_PLAYBACK_MIN_DURATION, this.mDefaultBgCurrentDrainMediaPlaybackMinDuration);
        }

        private void updateCurrentDrainLocationMinDuration() {
            this.mBgCurrentDrainLocationMinDuration = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_CURRENT_DRAIN_LOCATION_MIN_DURATION, this.mDefaultBgCurrentDrainLocationMinDuration);
        }

        private void updateCurrentDrainEventDurationBasedThresholdEnabled() {
            this.mBgCurrentDrainEventDurationBasedThresholdEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_CURRENT_DRAIN_EVENT_DURATION_BASED_THRESHOLD_ENABLED, this.mDefaultBgCurrentDrainEventDurationBasedThresholdEnabled);
        }

        private void updateCurrentDrainExemptedTypes() {
            this.mBgCurrentDrainExemptedTypes = android.provider.DeviceConfig.getInt("activity_manager", KEY_BG_CURRENT_DRAIN_EXEMPTED_TYPES, this.mDefaultBgCurrentDrainExemptedTypes);
        }

        private void updateCurrentDrainDecoupleThresholds() {
            this.mBgCurrentDrainDecoupleThresholds = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_CURRENT_DRAIN_DECOUPLE_THRESHOLDS, true);
        }

        private void updateBgCurrentDrainAutoRestrictAbusiveAppsEnabled() {
            this.mBgCurrentDrainAutoRestrictAbusiveAppsEnabled = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_CURRENT_DRAIN_AUTO_RESTRICT_ABUSIVE_APPS_ENABLED, this.mDefaultBgCurrentDrainAutoRestrictAbusiveAppsEnabled);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            this.mBatteryFullChargeMah = this.mInjector.getBatteryManagerInternal().getBatteryFullCharge() / 1000;
            super.onSystemReady();
            updateCurrentDrainThreshold();
            updateCurrentDrainWindow();
            updateCurrentDrainInteractionGracePeriod();
            updateCurrentDrainMediaPlaybackMinDuration();
            updateCurrentDrainLocationMinDuration();
            updateCurrentDrainEventDurationBasedThresholdEnabled();
            updateCurrentDrainExemptedTypes();
            updateCurrentDrainDecoupleThresholds();
            updateBgCurrentDrainAutoRestrictAbusiveAppsEnabled();
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public int getProposedRestrictionLevel(java.lang.String packageName, int uid, int maxLevel) {
            int restrictedLevel;
            boolean canRestrict = false;
            if (maxLevel <= 30) {
                return 0;
            }
            synchronized (this.mLock) {
                android.util.Pair<long[], com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]> pair = this.mHighBgBatteryPackages.get(uid);
                if (pair != null) {
                    long lastInteractionTime = this.mLastInteractionTime.get(uid, 0L);
                    long[] ts = (long[]) pair.first;
                    boolean noInteractionRecently = ts[0] > this.mBgCurrentDrainInteractionGracePeriodMs + lastInteractionTime;
                    if (((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController.isAutoRestrictAbusiveAppEnabled() && this.mBgCurrentDrainAutoRestrictAbusiveAppsEnabled) {
                        canRestrict = true;
                    }
                    if (noInteractionRecently && canRestrict) {
                        restrictedLevel = 40;
                    } else {
                        restrictedLevel = 30;
                    }
                    int i = 50;
                    if (maxLevel > 50) {
                        if (ts[1] <= 0) {
                            i = restrictedLevel;
                        }
                        return i;
                    }
                    if (maxLevel == 50) {
                        return restrictedLevel;
                    }
                }
                return 30;
            }
        }

        double[] calcPercentage(int uid, double[] usage, double[] percentage) {
            com.android.server.am.AppBatteryTracker.BatteryUsage debugUsage = uid > 0 ? (com.android.server.am.AppBatteryTracker.BatteryUsage) ((com.android.server.am.AppBatteryTracker) this.mTracker).mDebugUidPercentages.get(uid) : null;
            double[] forced = debugUsage != null ? debugUsage.getPercentage() : null;
            for (int i = 0; i < usage.length; i++) {
                percentage[i] = forced != null ? forced[i] : (usage[i] / ((double) this.mBatteryFullChargeMah)) * 100.0d;
            }
            return percentage;
        }

        private double sumPercentageOfTypes(double[] percentage, int types) {
            double result = 0.0d;
            int type = java.lang.Integer.highestOneBit(types);
            while (type != 0) {
                int index = java.lang.Integer.numberOfTrailingZeros(type);
                result += percentage[index];
                types &= ~type;
                type = java.lang.Integer.highestOneBit(types);
            }
            return result;
        }

        private static java.lang.String batteryUsageTypesToString(int types) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("[");
            boolean needDelimiter = false;
            int type = java.lang.Integer.highestOneBit(types);
            while (type != 0) {
                if (needDelimiter) {
                    sb.append('|');
                }
                needDelimiter = true;
                switch (type) {
                    case 1:
                        sb.append("UNSPECIFIED");
                        break;
                    case 2:
                        sb.append("FOREGROUND");
                        break;
                    case 4:
                        sb.append("BACKGROUND");
                        break;
                    case 8:
                        sb.append("FOREGROUND_SERVICE");
                        break;
                    case 16:
                        sb.append("CACHED");
                        break;
                    default:
                        return "[UNKNOWN(" + java.lang.Integer.toHexString(types) + ")]";
                }
                types &= ~type;
                type = java.lang.Integer.highestOneBit(types);
            }
            sb.append("]");
            return sb.toString();
        }

        /* JADX WARN: Not initialized variable reg: 28, insn: 0x0173: MOVE (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r28 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('notifyController' boolean)]), block:B:87:0x0173 */
        void handleUidBatteryUsage(int uid, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage usage) throws java.lang.Throwable {
            java.lang.Object obj;
            boolean notifyController;
            boolean decoupleThresholds;
            boolean notifyController2;
            int reason = shouldExemptUid(uid);
            if (reason != -1) {
                return;
            }
            boolean notifyController3 = false;
            boolean excessive = false;
            double rbPercentage = sumPercentageOfTypes(usage.getPercentage(), this.mBgCurrentDrainRestrictedBucketTypes);
            double brPercentage = sumPercentageOfTypes(usage.getPercentage(), this.mBgCurrentDrainBgRestrictedTypes);
            java.lang.Object obj2 = this.mLock;
            synchronized (obj2) {
                try {
                    try {
                        int curLevel = ((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController.getRestrictionLevel(uid);
                        if (curLevel >= 50) {
                            try {
                            } catch (java.lang.Throwable th) {
                                th = th;
                                obj = obj2;
                            }
                        } else {
                            long lastInteractionTime = this.mLastInteractionTime.get(uid, 0L);
                            long now = android.os.SystemClock.elapsedRealtime();
                            obj = obj2;
                            try {
                                int thresholdIndex = getCurrentDrainThresholdIndex(uid, now, this.mBgCurrentDrainWindowMs);
                                int index = this.mHighBgBatteryPackages.indexOfKey(uid);
                                boolean decoupleThresholds2 = this.mBgCurrentDrainDecoupleThresholds;
                                double rbThreshold = this.mBgCurrentDrainRestrictedBucketThreshold[thresholdIndex];
                                double brThreshold = this.mBgCurrentDrainBgRestrictedThreshold[thresholdIndex];
                                boolean z = false;
                                try {
                                    if (index >= 0) {
                                        boolean notifyController4 = false;
                                        android.util.Pair<long[], com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]> pair = this.mHighBgBatteryPackages.valueAt(index);
                                        long[] ts = (long[]) pair.first;
                                        long lastRestrictBucketTs = ts[0];
                                        if (rbPercentage >= rbThreshold) {
                                            if (now > lastInteractionTime + this.mBgCurrentDrainInteractionGracePeriodMs) {
                                                if (lastRestrictBucketTs == 0) {
                                                    ts[0] = now;
                                                    ((com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]) pair.second)[0] = usage;
                                                }
                                                notifyController4 = true;
                                            }
                                            excessive = true;
                                        }
                                        if (brPercentage >= brThreshold) {
                                            if (decoupleThresholds2 || (curLevel == 40 && now > this.mBgCurrentDrainWindowMs + lastRestrictBucketTs)) {
                                                z = true;
                                            }
                                            boolean notifyController5 = z;
                                            if (notifyController5) {
                                                try {
                                                    ts[1] = now;
                                                    ((com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]) pair.second)[1] = usage;
                                                } catch (java.lang.Throwable th2) {
                                                    th = th2;
                                                }
                                            }
                                            notifyController = notifyController5;
                                            excessive = true;
                                        } else {
                                            ts[1] = 0;
                                            ((com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]) pair.second)[1] = null;
                                            notifyController = notifyController4;
                                        }
                                        if (excessive) {
                                            return;
                                        } else {
                                            return;
                                        }
                                    }
                                    long[] ts2 = null;
                                    com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[] usages = null;
                                    if (rbPercentage >= rbThreshold) {
                                        decoupleThresholds = decoupleThresholds2;
                                        if (now > lastInteractionTime + this.mBgCurrentDrainInteractionGracePeriodMs) {
                                            long[] ts3 = {now, 0};
                                            try {
                                                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[] usages2 = new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[2];
                                                usages2[0] = usage;
                                                this.mHighBgBatteryPackages.put(uid, android.util.Pair.create(ts3, usages2));
                                                usages = usages2;
                                                ts2 = ts3;
                                                notifyController3 = true;
                                            } catch (java.lang.Throwable th3) {
                                                th = th3;
                                            }
                                        }
                                        excessive = true;
                                    } else {
                                        decoupleThresholds = decoupleThresholds2;
                                    }
                                    if (!decoupleThresholds || brPercentage < brThreshold) {
                                        notifyController = notifyController3;
                                    } else {
                                        if (ts2 == null) {
                                            try {
                                                long[] ts4 = new long[2];
                                                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[] usages3 = new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[2];
                                                notifyController2 = notifyController3;
                                                try {
                                                    this.mHighBgBatteryPackages.put(uid, android.util.Pair.create(ts4, usages3));
                                                    usages = usages3;
                                                    ts2 = ts4;
                                                } catch (java.lang.Throwable th4) {
                                                    th = th4;
                                                }
                                            } catch (java.lang.Throwable th5) {
                                                th = th5;
                                            }
                                        } else {
                                            notifyController2 = notifyController3;
                                        }
                                        ts2[1] = now;
                                        usages[1] = usage;
                                        excessive = true;
                                        notifyController = true;
                                    }
                                    if (excessive || !notifyController) {
                                        return;
                                    }
                                    ((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController.refreshAppRestrictionLevelForUid(uid, 1536, 2, true);
                                    return;
                                } catch (java.lang.Throwable th6) {
                                    th = th6;
                                }
                            } catch (java.lang.Throwable th7) {
                                th = th7;
                            }
                        }
                    } catch (java.lang.Throwable th8) {
                        while (true) {
                            th = th8;
                        }
                    }
                } catch (java.lang.Throwable th9) {
                    th = th9;
                    obj = obj2;
                }
                throw th;
            }
        }

        private int getCurrentDrainThresholdIndex(int uid, long now, long window) {
            if (hasMediaPlayback(uid, now, window) || hasLocation(uid, now, window)) {
                return 1;
            }
            return 0;
        }

        private boolean hasMediaPlayback(int uid, long now, long window) {
            return this.mBgCurrentDrainEventDurationBasedThresholdEnabled && ((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController.getCompositeMediaPlaybackDurations(uid, now, window) >= this.mBgCurrentDrainMediaPlaybackMinDuration;
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        private boolean hasLocation(int uid, long now, long window) {
            if (!this.mBgCurrentDrainHighThresholdByBgLocation) {
                return false;
            }
            if (((com.android.server.am.AppBatteryTracker) this.mTracker).mInjector.checkPermission("android.permission.ACCESS_BACKGROUND_LOCATION", -1, uid) == 0) {
                return true;
            }
            if (!this.mBgCurrentDrainEventDurationBasedThresholdEnabled) {
                return false;
            }
            long since = java.lang.Math.max(0L, now - window);
            com.android.server.am.AppRestrictionController controller = ((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController;
            long locationDuration = controller.getForegroundServiceTotalDurationsSince(uid, since, now, 8);
            return locationDuration >= this.mBgCurrentDrainLocationMinDuration;
        }

        void onUserInteractionStarted(java.lang.String packageName, int uid) {
            int index;
            boolean changed = false;
            synchronized (this.mLock) {
                this.mLastInteractionTime.put(uid, android.os.SystemClock.elapsedRealtime());
                int curLevel = ((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController.getRestrictionLevel(uid, packageName);
                if (curLevel != 50 && (index = this.mHighBgBatteryPackages.indexOfKey(uid)) >= 0) {
                    this.mHighBgBatteryPackages.removeAt(index);
                    changed = true;
                }
            }
            if (changed) {
                ((com.android.server.am.AppBatteryTracker) this.mTracker).mAppRestrictionController.refreshAppRestrictionLevelForUid(uid, 768, 3, true);
            }
        }

        void onBackgroundRestrictionChanged(int uid, java.lang.String pkgName, boolean restricted) {
            if (restricted) {
                return;
            }
            synchronized (this.mLock) {
                android.util.Pair<long[], com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]> pair = this.mHighBgBatteryPackages.get(uid);
                if (pair != null) {
                    ((long[]) pair.first)[1] = 0;
                    ((com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]) pair.second)[1] = null;
                }
            }
        }

        void reset() throws java.lang.Throwable {
            this.mHighBgBatteryPackages.clear();
            this.mLastInteractionTime.clear();
            ((com.android.server.am.AppBatteryTracker) this.mTracker).reset();
        }

        void onUserRemovedLocked(int userId) {
            for (int i = this.mHighBgBatteryPackages.size() - 1; i >= 0; i--) {
                if (android.os.UserHandle.getUserId(this.mHighBgBatteryPackages.keyAt(i)) == userId) {
                    this.mHighBgBatteryPackages.removeAt(i);
                }
            }
            for (int i2 = this.mLastInteractionTime.size() - 1; i2 >= 0; i2--) {
                if (android.os.UserHandle.getUserId(this.mLastInteractionTime.keyAt(i2)) == userId) {
                    this.mLastInteractionTime.removeAt(i2);
                }
            }
        }

        void onUidRemovedLocked(int uid) {
            this.mHighBgBatteryPackages.remove(uid);
            this.mLastInteractionTime.delete(uid);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) throws java.lang.Throwable {
            java.lang.Object obj;
            java.lang.Object obj2;
            pw.print(prefix);
            pw.println("APP BATTERY TRACKER POLICY SETTINGS:");
            java.lang.String prefix2 = "  " + prefix;
            super.dump(pw, prefix2);
            if (isEnabled()) {
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_THRESHOLD_TO_RESTRICTED_BUCKET);
                pw.print('=');
                char c = 0;
                pw.println(this.mBgCurrentDrainRestrictedBucketThreshold[0]);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_TO_RESTRICTED_BUCKET);
                pw.print('=');
                pw.println(this.mBgCurrentDrainRestrictedBucketThreshold[1]);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_THRESHOLD_TO_BG_RESTRICTED);
                pw.print('=');
                pw.println(this.mBgCurrentDrainBgRestrictedThreshold[0]);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_TO_BG_RESTRICTED);
                pw.print('=');
                pw.println(this.mBgCurrentDrainBgRestrictedThreshold[1]);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_WINDOW);
                pw.print('=');
                pw.println(this.mBgCurrentDrainWindowMs);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_INTERACTION_GRACE_PERIOD);
                pw.print('=');
                pw.println(this.mBgCurrentDrainInteractionGracePeriodMs);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_MEDIA_PLAYBACK_MIN_DURATION);
                pw.print('=');
                pw.println(this.mBgCurrentDrainMediaPlaybackMinDuration);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_LOCATION_MIN_DURATION);
                pw.print('=');
                pw.println(this.mBgCurrentDrainLocationMinDuration);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_EVENT_DURATION_BASED_THRESHOLD_ENABLED);
                pw.print('=');
                pw.println(this.mBgCurrentDrainEventDurationBasedThresholdEnabled);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_AUTO_RESTRICT_ABUSIVE_APPS_ENABLED);
                pw.print('=');
                pw.println(this.mBgCurrentDrainAutoRestrictAbusiveAppsEnabled);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_TYPES_TO_RESTRICTED_BUCKET);
                pw.print('=');
                pw.println(batteryUsageTypesToString(this.mBgCurrentDrainRestrictedBucketTypes));
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_TYPES_TO_BG_RESTRICTED);
                pw.print('=');
                pw.println(batteryUsageTypesToString(this.mBgCurrentDrainBgRestrictedTypes));
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_POWER_COMPONENTS);
                pw.print('=');
                pw.println(this.mBgCurrentDrainPowerComponents);
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_EXEMPTED_TYPES);
                pw.print('=');
                pw.println(com.android.server.am.BaseAppStateTracker.stateTypesToString(this.mBgCurrentDrainExemptedTypes));
                pw.print(prefix2);
                pw.print(KEY_BG_CURRENT_DRAIN_HIGH_THRESHOLD_BY_BG_LOCATION);
                pw.print('=');
                pw.println(this.mBgCurrentDrainHighThresholdByBgLocation);
                pw.print(prefix2);
                pw.print("Full charge capacity=");
                pw.print(this.mBatteryFullChargeMah);
                pw.println(" mAh");
                pw.print(prefix2);
                pw.println("Excessive current drain detected:");
                java.lang.Object obj3 = this.mLock;
                synchronized (obj3) {
                    try {
                        try {
                            int size = this.mHighBgBatteryPackages.size();
                            java.lang.String prefix3 = "  " + prefix2;
                            if (size > 0) {
                                try {
                                    long now = android.os.SystemClock.elapsedRealtime();
                                    int i = 0;
                                    while (i < size) {
                                        int uid = this.mHighBgBatteryPackages.keyAt(i);
                                        android.util.Pair<long[], com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]> pair = this.mHighBgBatteryPackages.valueAt(i);
                                        long[] ts = (long[]) pair.first;
                                        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[] usages = (com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage[]) pair.second;
                                        int thresholdIndex = getCurrentDrainThresholdIndex(uid, now, this.mBgCurrentDrainWindowMs);
                                        java.lang.String prefix4 = prefix3;
                                        int i2 = i;
                                        obj = obj3;
                                        try {
                                            pw.format("%s%s: (threshold=%4.2f%%/%4.2f%%) %s / %s\n", prefix4, android.os.UserHandle.formatUid(uid), java.lang.Float.valueOf(this.mBgCurrentDrainRestrictedBucketThreshold[thresholdIndex]), java.lang.Float.valueOf(this.mBgCurrentDrainBgRestrictedThreshold[thresholdIndex]), formatHighBgBatteryRecord(ts[c], now, usages[c]), formatHighBgBatteryRecord(ts[1], now, usages[1]));
                                            i = i2 + 1;
                                            prefix3 = prefix4;
                                            obj3 = obj;
                                            c = 0;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            throw th;
                                        }
                                    }
                                    obj2 = obj3;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    obj = obj3;
                                }
                            } else {
                                obj2 = obj3;
                                pw.print(prefix3);
                                pw.println("(none)");
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            obj = obj3;
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                }
            }
        }

        private java.lang.String formatHighBgBatteryRecord(long ts, long now, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage usage) {
            if (ts > 0 && usage != null) {
                return java.lang.String.format("%s %s (%s)", android.util.TimeUtils.formatTime(ts, now), usage.toString(), usage.percentageToString());
            }
            return "0";
        }
    }
}
