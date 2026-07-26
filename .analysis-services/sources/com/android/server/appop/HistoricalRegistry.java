package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
final class HistoricalRegistry {
    private static final boolean DEBUG = false;
    private static final long DEFAULT_COMPRESSION_STEP = 10;
    private static final int DEFAULT_MODE = 1;
    private static final java.lang.String HISTORY_FILE_SUFFIX = ".xml";
    private static final int MSG_WRITE_PENDING_HISTORY = 1;
    private static final java.lang.String PARAMETER_ASSIGNMENT = "=";
    private static final java.lang.String PARAMETER_DELIMITER = ",";
    private long mBaseSnapshotInterval;
    private android.app.AppOpsManager.HistoricalOps mCurrentHistoricalOps;
    private volatile com.android.server.appop.DiscreteRegistry mDiscreteRegistry;
    private final java.lang.Object mInMemoryLock;
    private long mIntervalCompressionMultiplier;
    private int mMode;
    private long mNextPersistDueTimeMillis;
    private final java.lang.Object mOnDiskLock;
    private long mPendingHistoryOffsetMillis;
    private java.util.LinkedList<android.app.AppOpsManager.HistoricalOps> mPendingWrites;
    private com.android.server.appop.HistoricalRegistry.Persistence mPersistence;
    private static final boolean KEEP_WTF_LOG = android.os.Build.IS_DEBUGGABLE;
    private static final java.lang.String LOG_TAG = com.android.server.appop.HistoricalRegistry.class.getSimpleName();
    private static final long DEFAULT_SNAPSHOT_INTERVAL_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(15);

    HistoricalRegistry(java.lang.Object lock) {
        this.mPendingWrites = new java.util.LinkedList<>();
        this.mOnDiskLock = new java.lang.Object();
        this.mMode = 1;
        this.mBaseSnapshotInterval = DEFAULT_SNAPSHOT_INTERVAL_MILLIS;
        this.mIntervalCompressionMultiplier = DEFAULT_COMPRESSION_STEP;
        this.mInMemoryLock = lock;
        this.mDiscreteRegistry = new com.android.server.appop.DiscreteRegistry(lock);
    }

    HistoricalRegistry(com.android.server.appop.HistoricalRegistry other) {
        this(other.mInMemoryLock);
        this.mMode = other.mMode;
        this.mBaseSnapshotInterval = other.mBaseSnapshotInterval;
        this.mIntervalCompressionMultiplier = other.mIntervalCompressionMultiplier;
        this.mDiscreteRegistry = other.mDiscreteRegistry;
    }

    void systemReady(final android.content.ContentResolver resolver) {
        this.mDiscreteRegistry.systemReady();
        android.net.Uri uri = android.provider.Settings.Global.getUriFor("appop_history_parameters");
        resolver.registerContentObserver(uri, false, new android.database.ContentObserver(com.android.server.FgThread.getHandler()) { // from class: com.android.server.appop.HistoricalRegistry.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.appop.HistoricalRegistry.this.updateParametersFromSetting(resolver);
            }
        });
        updateParametersFromSetting(resolver);
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (this.mMode != 0) {
                    if (!isPersistenceInitializedMLocked()) {
                        this.mPersistence = new com.android.server.appop.HistoricalRegistry.Persistence(this.mBaseSnapshotInterval, this.mIntervalCompressionMultiplier);
                    }
                    long lastPersistTimeMills = this.mPersistence.getLastPersistTimeMillisDLocked();
                    if (lastPersistTimeMills > 0) {
                        this.mPendingHistoryOffsetMillis = java.lang.System.currentTimeMillis() - lastPersistTimeMills;
                    }
                }
            }
        }
    }

    private boolean isPersistenceInitializedMLocked() {
        return this.mPersistence != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0051  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateParametersFromSetting(android.content.ContentResolver r15) {
        /*
            Method dump skipped, instruction units count: 214
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.appop.HistoricalRegistry.updateParametersFromSetting(android.content.ContentResolver):void");
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw, int filterUid, java.lang.String filterPackage, java.lang.String filterAttributionTag, int filterOp, int filter) {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                pw.println();
                pw.print(prefix);
                pw.print("History:");
                pw.print("  mode=");
                pw.println(android.app.AppOpsManager.historicalModeToString(this.mMode));
                com.android.server.appop.HistoricalRegistry.StringDumpVisitor visitor = new com.android.server.appop.HistoricalRegistry.StringDumpVisitor(prefix + "  ", pw, filterUid, filterPackage, filterAttributionTag, filterOp, filter);
                long nowMillis = java.lang.System.currentTimeMillis();
                android.app.AppOpsManager.HistoricalOps currentOps = getUpdatedPendingHistoricalOpsMLocked(nowMillis);
                makeRelativeToEpochStart(currentOps, nowMillis);
                currentOps.accept(visitor);
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.e(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                java.util.List<android.app.AppOpsManager.HistoricalOps> ops = this.mPersistence.readHistoryDLocked();
                if (ops != null) {
                    long remainingToFillBatchMillis = (this.mNextPersistDueTimeMillis - nowMillis) - this.mBaseSnapshotInterval;
                    int opCount = ops.size();
                    for (int i = 0; i < opCount; i++) {
                        android.app.AppOpsManager.HistoricalOps op = ops.get(i);
                        op.offsetBeginAndEndTime(remainingToFillBatchMillis);
                        makeRelativeToEpochStart(op, nowMillis);
                        op.accept(visitor);
                    }
                } else {
                    pw.println("  Empty");
                }
            }
        }
    }

    void dumpDiscreteData(java.io.PrintWriter pw, int uidFilter, java.lang.String packageNameFilter, java.lang.String attributionTagFilter, int filter, int dumpOp, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix, int nDiscreteOps) {
        this.mDiscreteRegistry.dump(pw, uidFilter, packageNameFilter, attributionTagFilter, filter, dumpOp, sdf, date, prefix, nDiscreteOps);
    }

    int getMode() {
        int i;
        synchronized (this.mInMemoryLock) {
            i = this.mMode;
        }
        return i;
    }

    void getHistoricalOpsFromDiskRaw(int uid, java.lang.String packageName, java.lang.String attributionTag, java.lang.String[] opNames, int historyFlags, int filter, long beginTimeMillis, long endTimeMillis, int flags, java.lang.String[] attributionExemptedPackages, android.os.RemoteCallback callback) throws java.lang.Throwable {
        android.app.AppOpsManager.HistoricalOps result;
        java.lang.Object obj;
        android.app.AppOpsManager.HistoricalOps result2 = new android.app.AppOpsManager.HistoricalOps(beginTimeMillis, endTimeMillis);
        if ((historyFlags & 1) == 0) {
            result = result2;
        } else {
            java.lang.Object obj2 = this.mOnDiskLock;
            synchronized (obj2) {
                try {
                    try {
                        synchronized (this.mInMemoryLock) {
                            try {
                                if (!isPersistenceInitializedMLocked()) {
                                    try {
                                        android.util.Slog.e(LOG_TAG, "Interaction before persistence initialized");
                                        callback.sendResult(new android.os.Bundle());
                                        try {
                                            return;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            obj = obj2;
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                } else {
                                    result = result2;
                                    this.mPersistence.collectHistoricalOpsDLocked(result2, uid, packageName, attributionTag, opNames, filter, beginTimeMillis, endTimeMillis, flags);
                                }
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                            }
                            while (true) {
                                try {
                                    throw th;
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                }
                            }
                        }
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                        obj = obj2;
                    }
                } catch (java.lang.Throwable th6) {
                    th = th6;
                }
            }
        }
        if ((historyFlags & 2) != 0) {
            this.mDiscreteRegistry.addFilteredDiscreteOpsToHistoricalOps(result, beginTimeMillis, endTimeMillis, filter, uid, packageName, opNames, attributionTag, flags, new android.util.ArraySet(attributionExemptedPackages));
        }
        android.os.Bundle payload = new android.os.Bundle();
        payload.putParcelable("historical_ops", result);
        callback.sendResult(payload);
    }

    void getHistoricalOps(int uid, java.lang.String packageName, java.lang.String attributionTag, java.lang.String[] opNames, int historyFlags, int filter, long beginTimeMillis, long endTimeMillis, int flags, java.lang.String[] attributionExemptPkgs, android.os.RemoteCallback callback) throws java.lang.Throwable {
        long inMemoryAdjEndTimeMillis;
        long inMemoryAdjBeginTimeMillis;
        android.os.Bundle payload;
        long endTimeMillis2;
        long currentTimeMillis;
        android.os.RemoteCallback remoteCallback;
        android.app.AppOpsManager.HistoricalOps result;
        android.os.RemoteCallback remoteCallback2;
        java.lang.Object obj;
        long currentTimeMillis2;
        java.lang.Object obj2;
        long currentTimeMillis3 = java.lang.System.currentTimeMillis();
        long endTimeMillis3 = endTimeMillis == Long.MAX_VALUE ? currentTimeMillis3 : endTimeMillis;
        android.os.Bundle payload2 = new android.os.Bundle();
        long inMemoryAdjBeginTimeMillis2 = java.lang.Math.max(currentTimeMillis3 - endTimeMillis3, 0L);
        long inMemoryAdjEndTimeMillis2 = java.lang.Math.max(currentTimeMillis3 - beginTimeMillis, 0L);
        android.app.AppOpsManager.HistoricalOps result2 = new android.app.AppOpsManager.HistoricalOps(inMemoryAdjBeginTimeMillis2, inMemoryAdjEndTimeMillis2);
        if ((historyFlags & 2) != 0) {
            inMemoryAdjEndTimeMillis = inMemoryAdjEndTimeMillis2;
            inMemoryAdjBeginTimeMillis = inMemoryAdjBeginTimeMillis2;
            payload = payload2;
            endTimeMillis2 = endTimeMillis3;
            currentTimeMillis = currentTimeMillis3;
            remoteCallback = callback;
            this.mDiscreteRegistry.addFilteredDiscreteOpsToHistoricalOps(result2, beginTimeMillis, endTimeMillis3, filter, uid, packageName, opNames, attributionTag, flags, new android.util.ArraySet(attributionExemptPkgs));
        } else {
            inMemoryAdjEndTimeMillis = inMemoryAdjEndTimeMillis2;
            inMemoryAdjBeginTimeMillis = inMemoryAdjBeginTimeMillis2;
            payload = payload2;
            endTimeMillis2 = endTimeMillis3;
            currentTimeMillis = currentTimeMillis3;
            remoteCallback = callback;
        }
        if ((historyFlags & 1) != 0) {
            java.lang.Object obj3 = this.mOnDiskLock;
            synchronized (obj3) {
                try {
                    try {
                        synchronized (this.mInMemoryLock) {
                            try {
                                if (isPersistenceInitializedMLocked()) {
                                    long currentTimeMillis4 = currentTimeMillis;
                                    try {
                                        android.app.AppOpsManager.HistoricalOps currentOps = getUpdatedPendingHistoricalOpsMLocked(currentTimeMillis4);
                                        if (inMemoryAdjBeginTimeMillis < currentOps.getEndTimeMillis()) {
                                            try {
                                                if (inMemoryAdjEndTimeMillis > currentOps.getBeginTimeMillis()) {
                                                    android.app.AppOpsManager.HistoricalOps currentOpsCopy = new android.app.AppOpsManager.HistoricalOps(currentOps);
                                                    currentTimeMillis2 = currentTimeMillis4;
                                                    try {
                                                        currentOpsCopy.filter(uid, packageName, attributionTag, opNames, historyFlags, filter, inMemoryAdjBeginTimeMillis, inMemoryAdjEndTimeMillis);
                                                        result = result2;
                                                        try {
                                                            result.merge(currentOpsCopy);
                                                        } catch (java.lang.Throwable th) {
                                                            th = th;
                                                            while (true) {
                                                                throw th;
                                                            }
                                                        }
                                                    } catch (java.lang.Throwable th2) {
                                                        th = th2;
                                                        while (true) {
                                                            throw th;
                                                        }
                                                    }
                                                } else {
                                                    result = result2;
                                                    currentTimeMillis2 = currentTimeMillis4;
                                                }
                                            } catch (java.lang.Throwable th3) {
                                                th = th3;
                                            }
                                        } else {
                                            result = result2;
                                            currentTimeMillis2 = currentTimeMillis4;
                                        }
                                        java.util.List<android.app.AppOpsManager.HistoricalOps> pendingWrites = new java.util.ArrayList<>(this.mPendingWrites);
                                        this.mPendingWrites.clear();
                                        boolean collectOpsFromDisk = inMemoryAdjEndTimeMillis > currentOps.getEndTimeMillis();
                                        if (collectOpsFromDisk) {
                                            try {
                                                persistPendingHistory(pendingWrites);
                                                long onDiskAndInMemoryOffsetMillis = (currentTimeMillis2 - this.mNextPersistDueTimeMillis) + this.mBaseSnapshotInterval;
                                                long onDiskAdjBeginTimeMillis = java.lang.Math.max(inMemoryAdjBeginTimeMillis - onDiskAndInMemoryOffsetMillis, 0L);
                                                long onDiskAdjEndTimeMillis = java.lang.Math.max(inMemoryAdjEndTimeMillis - onDiskAndInMemoryOffsetMillis, 0L);
                                                obj2 = obj3;
                                                remoteCallback2 = callback;
                                                this.mPersistence.collectHistoricalOpsDLocked(result, uid, packageName, attributionTag, opNames, filter, onDiskAdjBeginTimeMillis, onDiskAdjEndTimeMillis, flags);
                                            } catch (java.lang.Throwable th4) {
                                                th = th4;
                                                obj = obj3;
                                                throw th;
                                            }
                                        } else {
                                            remoteCallback2 = callback;
                                            obj2 = obj3;
                                        }
                                    } catch (java.lang.Throwable th5) {
                                        th = th5;
                                    }
                                } else {
                                    try {
                                        android.util.Slog.e(LOG_TAG, "Interaction before persistence initialized");
                                        remoteCallback.sendResult(new android.os.Bundle());
                                        try {
                                        } catch (java.lang.Throwable th6) {
                                            th = th6;
                                            obj = obj3;
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th7) {
                                        th = th7;
                                        while (true) {
                                            try {
                                                throw th;
                                            } catch (java.lang.Throwable th8) {
                                                th = th8;
                                            }
                                        }
                                    }
                                }
                            } catch (java.lang.Throwable th9) {
                                th = th9;
                            }
                        }
                    } catch (java.lang.Throwable th10) {
                        th = th10;
                        obj = obj3;
                    }
                } catch (java.lang.Throwable th11) {
                    th = th11;
                }
            }
            return;
        }
        result = result2;
        remoteCallback2 = remoteCallback;
        result.setBeginAndEndTime(beginTimeMillis, endTimeMillis2);
        android.os.Bundle payload3 = payload;
        payload3.putParcelable("historical_ops", result);
        remoteCallback2.sendResult(payload3);
    }

    void incrementOpAccessedCount(int op, int uid, java.lang.String packageName, java.lang.String attributionTag, int uidState, int flags, long accessTime, int attributionFlags, int attributionChainId) {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 1) {
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.v(LOG_TAG, "Interaction before persistence initialized");
                } else {
                    getUpdatedPendingHistoricalOpsMLocked(java.lang.System.currentTimeMillis()).increaseAccessCount(op, uid, packageName, attributionTag, uidState, flags, 1L);
                    this.mDiscreteRegistry.recordDiscreteAccess(uid, packageName, op, attributionTag, flags, uidState, accessTime, -1L, attributionFlags, attributionChainId);
                }
            }
        }
    }

    void incrementOpRejected(int op, int uid, java.lang.String packageName, java.lang.String attributionTag, int uidState, int flags) {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 1) {
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.v(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                getUpdatedPendingHistoricalOpsMLocked(java.lang.System.currentTimeMillis()).increaseRejectCount(op, uid, packageName, attributionTag, uidState, flags, 1L);
            }
        }
    }

    void increaseOpAccessDuration(int op, int uid, java.lang.String packageName, java.lang.String attributionTag, int uidState, int flags, long eventStartTime, long increment, int attributionFlags, int attributionChainId) {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 1) {
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.v(LOG_TAG, "Interaction before persistence initialized");
                } else {
                    getUpdatedPendingHistoricalOpsMLocked(java.lang.System.currentTimeMillis()).increaseAccessDuration(op, uid, packageName, attributionTag, uidState, flags, increment);
                    this.mDiscreteRegistry.recordDiscreteAccess(uid, packageName, op, attributionTag, flags, uidState, eventStartTime, increment, attributionFlags, attributionChainId);
                }
            }
        }
    }

    void setHistoryParameters(int mode, long baseSnapshotInterval, long intervalCompressionMultiplier) {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                boolean resampleHistory = false;
                android.util.Slog.i(LOG_TAG, "New history parameters: mode:" + android.app.AppOpsManager.historicalModeToString(mode) + " baseSnapshotInterval:" + baseSnapshotInterval + " intervalCompressionMultiplier:" + intervalCompressionMultiplier);
                if (this.mMode != mode) {
                    this.mMode = mode;
                    if (this.mMode == 0) {
                        clearHistoryOnDiskDLocked();
                    }
                    if (this.mMode == 2) {
                        this.mDiscreteRegistry.setDebugMode(true);
                    }
                }
                if (this.mBaseSnapshotInterval != baseSnapshotInterval) {
                    this.mBaseSnapshotInterval = baseSnapshotInterval;
                    resampleHistory = true;
                }
                if (this.mIntervalCompressionMultiplier != intervalCompressionMultiplier) {
                    this.mIntervalCompressionMultiplier = intervalCompressionMultiplier;
                    resampleHistory = true;
                }
                if (resampleHistory) {
                    resampleHistoryOnDiskInMemoryDMLocked(0L);
                }
            }
        }
    }

    void offsetHistory(long offsetMillis) {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.e(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                java.util.List<android.app.AppOpsManager.HistoricalOps> history = this.mPersistence.readHistoryDLocked();
                clearHistoricalRegistry();
                if (history != null) {
                    int historySize = history.size();
                    for (int i = 0; i < historySize; i++) {
                        android.app.AppOpsManager.HistoricalOps ops = history.get(i);
                        ops.offsetBeginAndEndTime(offsetMillis);
                    }
                    if (offsetMillis < 0) {
                        pruneFutureOps(history);
                    }
                    this.mPersistence.persistHistoricalOpsDLocked(history);
                }
            }
        }
    }

    void offsetDiscreteHistory(long offsetMillis) {
        this.mDiscreteRegistry.offsetHistory(offsetMillis);
    }

    void addHistoricalOps(android.app.AppOpsManager.HistoricalOps ops) {
        synchronized (this.mInMemoryLock) {
            if (!isPersistenceInitializedMLocked()) {
                android.util.Slog.d(LOG_TAG, "Interaction before persistence initialized");
                return;
            }
            ops.offsetBeginAndEndTime(this.mBaseSnapshotInterval);
            this.mPendingWrites.offerFirst(ops);
            java.util.List<android.app.AppOpsManager.HistoricalOps> pendingWrites = new java.util.ArrayList<>(this.mPendingWrites);
            this.mPendingWrites.clear();
            persistPendingHistory(pendingWrites);
        }
    }

    private void resampleHistoryOnDiskInMemoryDMLocked(long offsetMillis) {
        this.mPersistence = new com.android.server.appop.HistoricalRegistry.Persistence(this.mBaseSnapshotInterval, this.mIntervalCompressionMultiplier);
        offsetHistory(offsetMillis);
    }

    void resetHistoryParameters() {
        if (!isPersistenceInitializedMLocked()) {
            android.util.Slog.d(LOG_TAG, "Interaction before persistence initialized");
        } else {
            setHistoryParameters(1, DEFAULT_SNAPSHOT_INTERVAL_MILLIS, DEFAULT_COMPRESSION_STEP);
            this.mDiscreteRegistry.setDebugMode(false);
        }
    }

    void clearHistory(int uid, java.lang.String packageName) {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.d(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                if (this.mMode != 1) {
                    return;
                }
                for (int index = 0; index < this.mPendingWrites.size(); index++) {
                    this.mPendingWrites.get(index).clearHistory(uid, packageName);
                }
                getUpdatedPendingHistoricalOpsMLocked(java.lang.System.currentTimeMillis()).clearHistory(uid, packageName);
                this.mPersistence.clearHistoryDLocked(uid, packageName);
                this.mDiscreteRegistry.clearHistory(uid, packageName);
            }
        }
    }

    void writeAndClearDiscreteHistory() {
        this.mDiscreteRegistry.writeAndClearAccessHistory();
    }

    void clearAllHistory() {
        clearHistoricalRegistry();
        this.mDiscreteRegistry.clearHistory();
    }

    void clearHistoricalRegistry() {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (!isPersistenceInitializedMLocked()) {
                    android.util.Slog.d(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                clearHistoryOnDiskDLocked();
                this.mNextPersistDueTimeMillis = 0L;
                this.mPendingHistoryOffsetMillis = 0L;
                this.mCurrentHistoricalOps = null;
            }
        }
    }

    private void clearHistoryOnDiskDLocked() {
        com.android.server.IoThread.getHandler().removeMessages(1);
        synchronized (this.mInMemoryLock) {
            this.mCurrentHistoricalOps = null;
            this.mNextPersistDueTimeMillis = java.lang.System.currentTimeMillis();
            this.mPendingWrites.clear();
        }
        com.android.server.appop.HistoricalRegistry.Persistence.clearHistoryDLocked();
    }

    private android.app.AppOpsManager.HistoricalOps getUpdatedPendingHistoricalOpsMLocked(long now) {
        if (this.mCurrentHistoricalOps != null) {
            long remainingTimeMillis = this.mNextPersistDueTimeMillis - now;
            if (remainingTimeMillis > this.mBaseSnapshotInterval) {
                this.mPendingHistoryOffsetMillis = remainingTimeMillis - this.mBaseSnapshotInterval;
            }
            long elapsedTimeMillis = this.mBaseSnapshotInterval - remainingTimeMillis;
            this.mCurrentHistoricalOps.setEndTime(elapsedTimeMillis);
            if (remainingTimeMillis > 0) {
                return this.mCurrentHistoricalOps;
            }
            if (this.mCurrentHistoricalOps.isEmpty()) {
                this.mCurrentHistoricalOps.setBeginAndEndTime(0L, 0L);
                this.mNextPersistDueTimeMillis = this.mBaseSnapshotInterval + now;
                return this.mCurrentHistoricalOps;
            }
            this.mCurrentHistoricalOps.offsetBeginAndEndTime(this.mBaseSnapshotInterval);
            this.mCurrentHistoricalOps.setBeginTime(this.mCurrentHistoricalOps.getEndTimeMillis() - this.mBaseSnapshotInterval);
            long overdueTimeMillis = java.lang.Math.abs(remainingTimeMillis);
            this.mCurrentHistoricalOps.offsetBeginAndEndTime(overdueTimeMillis);
            schedulePersistHistoricalOpsMLocked(this.mCurrentHistoricalOps);
        }
        this.mCurrentHistoricalOps = new android.app.AppOpsManager.HistoricalOps(0L, 0L);
        this.mNextPersistDueTimeMillis = this.mBaseSnapshotInterval + now;
        return this.mCurrentHistoricalOps;
    }

    void shutdown() {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 0) {
                return;
            }
            persistPendingHistory();
        }
    }

    void persistPendingHistory() {
        java.util.List<android.app.AppOpsManager.HistoricalOps> pendingWrites;
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                pendingWrites = new java.util.ArrayList<>(this.mPendingWrites);
                this.mPendingWrites.clear();
                if (this.mPendingHistoryOffsetMillis != 0) {
                    resampleHistoryOnDiskInMemoryDMLocked(this.mPendingHistoryOffsetMillis);
                    this.mPendingHistoryOffsetMillis = 0L;
                }
            }
            persistPendingHistory(pendingWrites);
        }
        this.mDiscreteRegistry.writeAndClearAccessHistory();
    }

    private void persistPendingHistory(java.util.List<android.app.AppOpsManager.HistoricalOps> pendingWrites) {
        synchronized (this.mOnDiskLock) {
            com.android.server.IoThread.getHandler().removeMessages(1);
            if (pendingWrites.isEmpty()) {
                return;
            }
            int opCount = pendingWrites.size();
            for (int i = 0; i < opCount; i++) {
                android.app.AppOpsManager.HistoricalOps current = pendingWrites.get(i);
                if (i > 0) {
                    android.app.AppOpsManager.HistoricalOps previous = pendingWrites.get(i - 1);
                    current.offsetBeginAndEndTime(previous.getBeginTimeMillis());
                }
            }
            this.mPersistence.persistHistoricalOpsDLocked(pendingWrites);
        }
    }

    private void schedulePersistHistoricalOpsMLocked(android.app.AppOpsManager.HistoricalOps ops) {
        android.os.Message message = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.appop.HistoricalRegistry$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.appop.HistoricalRegistry) obj).persistPendingHistory();
            }
        }, this);
        message.what = 1;
        com.android.server.IoThread.getHandler().sendMessage(message);
        this.mPendingWrites.offerFirst(ops);
    }

    private static void makeRelativeToEpochStart(android.app.AppOpsManager.HistoricalOps ops, long nowMillis) {
        ops.setBeginAndEndTime(nowMillis - ops.getEndTimeMillis(), nowMillis - ops.getBeginTimeMillis());
    }

    private void pruneFutureOps(java.util.List<android.app.AppOpsManager.HistoricalOps> ops) {
        int opCount = ops.size();
        for (int i = opCount - 1; i >= 0; i--) {
            android.app.AppOpsManager.HistoricalOps op = ops.get(i);
            if (op.getEndTimeMillis() <= this.mBaseSnapshotInterval) {
                ops.remove(i);
            } else if (op.getBeginTimeMillis() < this.mBaseSnapshotInterval) {
                double filterScale = (op.getEndTimeMillis() - this.mBaseSnapshotInterval) / op.getDurationMillis();
                com.android.server.appop.HistoricalRegistry.Persistence.spliceFromBeginning(op, filterScale);
            }
        }
    }

    private static final class Persistence {
        private static final java.lang.String ATTR_ACCESS_COUNT = "ac";
        private static final java.lang.String ATTR_ACCESS_DURATION = "du";
        private static final java.lang.String ATTR_BEGIN_TIME = "beg";
        private static final java.lang.String ATTR_END_TIME = "end";
        private static final java.lang.String ATTR_NAME = "na";
        private static final java.lang.String ATTR_OVERFLOW = "ov";
        private static final java.lang.String ATTR_REJECT_COUNT = "rc";
        private static final java.lang.String ATTR_VERSION = "ver";
        private static final int CURRENT_VERSION = 2;
        private static final boolean DEBUG = false;
        private static final java.lang.String TAG_ATTRIBUTION = "ftr";
        private static final java.lang.String TAG_OP = "op";
        private static final java.lang.String TAG_OPS = "ops";
        private static final java.lang.String TAG_PACKAGE = "pkg";
        private static final java.lang.String TAG_STATE = "st";
        private static final java.lang.String TAG_UID = "uid";
        private final long mBaseSnapshotInterval;
        private final long mIntervalCompressionMultiplier;
        private static final java.lang.String LOG_TAG = com.android.server.appop.HistoricalRegistry.Persistence.class.getSimpleName();
        private static final java.lang.String TAG_HISTORY = "history";
        private static final com.android.internal.os.AtomicDirectory sHistoricalAppOpsDir = new com.android.internal.os.AtomicDirectory(new java.io.File(new java.io.File(android.os.Environment.getDataSystemDirectory(), "appops"), TAG_HISTORY));

        Persistence(long baseSnapshotInterval, long intervalCompressionMultiplier) {
            this.mBaseSnapshotInterval = baseSnapshotInterval;
            this.mIntervalCompressionMultiplier = intervalCompressionMultiplier;
        }

        private java.io.File generateFile(java.io.File baseDir, int depth) {
            long globalBeginMillis = computeGlobalIntervalBeginMillis(depth);
            return new java.io.File(baseDir, java.lang.Long.toString(globalBeginMillis) + com.android.server.appop.HistoricalRegistry.HISTORY_FILE_SUFFIX);
        }

        void clearHistoryDLocked(int uid, java.lang.String packageName) {
            java.util.List<android.app.AppOpsManager.HistoricalOps> historicalOps = readHistoryDLocked();
            if (historicalOps == null) {
                return;
            }
            for (int index = 0; index < historicalOps.size(); index++) {
                historicalOps.get(index).clearHistory(uid, packageName);
            }
            clearHistoryDLocked();
            persistHistoricalOpsDLocked(historicalOps);
        }

        static void clearHistoryDLocked() {
            sHistoricalAppOpsDir.delete();
        }

        void persistHistoricalOpsDLocked(java.util.List<android.app.AppOpsManager.HistoricalOps> ops) {
            try {
                java.io.File newBaseDir = sHistoricalAppOpsDir.startWrite();
                java.io.File oldBaseDir = sHistoricalAppOpsDir.getBackupDirectory();
                java.util.Set<java.lang.String> oldFileNames = getHistoricalFileNames(oldBaseDir);
                handlePersistHistoricalOpsRecursiveDLocked(newBaseDir, oldBaseDir, ops, oldFileNames, 0);
                sHistoricalAppOpsDir.finishWrite();
            } catch (java.lang.Throwable t) {
                com.android.server.appop.HistoricalRegistry.wtf("Failed to write historical app ops, restoring backup", t, null);
                sHistoricalAppOpsDir.failWrite();
            }
        }

        java.util.List<android.app.AppOpsManager.HistoricalOps> readHistoryRawDLocked() {
            return collectHistoricalOpsBaseDLocked(-1, null, null, null, 0, 0L, Long.MAX_VALUE, 31);
        }

        java.util.List<android.app.AppOpsManager.HistoricalOps> readHistoryDLocked() {
            java.util.List<android.app.AppOpsManager.HistoricalOps> result = readHistoryRawDLocked();
            if (result != null) {
                int opCount = result.size();
                for (int i = 0; i < opCount; i++) {
                    result.get(i).offsetBeginAndEndTime(this.mBaseSnapshotInterval);
                }
            }
            return result;
        }

        long getLastPersistTimeMillisDLocked() {
            java.io.File[] files;
            try {
                java.io.File baseDir = sHistoricalAppOpsDir.startRead();
                files = baseDir.listFiles();
            } catch (java.lang.Throwable e) {
                com.android.server.appop.HistoricalRegistry.wtf("Error reading historical app ops. Deleting history.", e, null);
                sHistoricalAppOpsDir.delete();
            }
            if (files != null && files.length > 0) {
                java.io.File shortestFile = null;
                for (java.io.File candidate : files) {
                    java.lang.String candidateName = candidate.getName();
                    if (candidateName.endsWith(com.android.server.appop.HistoricalRegistry.HISTORY_FILE_SUFFIX)) {
                        if (shortestFile == null) {
                            shortestFile = candidate;
                        } else if (candidateName.length() < shortestFile.getName().length()) {
                            shortestFile = candidate;
                        }
                    }
                }
                if (shortestFile == null) {
                    return 0L;
                }
                return shortestFile.lastModified();
            }
            sHistoricalAppOpsDir.finishRead();
            return 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void collectHistoricalOpsDLocked(android.app.AppOpsManager.HistoricalOps currentOps, int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, long filterBeingMillis, long filterEndMillis, int filterFlags) {
            java.util.List<android.app.AppOpsManager.HistoricalOps> readOps = collectHistoricalOpsBaseDLocked(filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterBeingMillis, filterEndMillis, filterFlags);
            if (readOps != null) {
                int readCount = readOps.size();
                for (int i = 0; i < readCount; i++) {
                    android.app.AppOpsManager.HistoricalOps readOp = readOps.get(i);
                    currentOps.merge(readOp);
                }
            }
        }

        private java.util.LinkedList<android.app.AppOpsManager.HistoricalOps> collectHistoricalOpsBaseDLocked(int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, long filterBeginTimeMillis, long filterEndTimeMillis, int filterFlags) {
            java.io.File baseDir = null;
            try {
                java.io.File baseDir2 = sHistoricalAppOpsDir.startRead();
                try {
                    java.util.Set<java.lang.String> historyFiles = getHistoricalFileNames(baseDir2);
                    long[] globalContentOffsetMillis = {0};
                    java.util.LinkedList<android.app.AppOpsManager.HistoricalOps> ops = collectHistoricalOpsRecursiveDLocked(baseDir2, filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterBeginTimeMillis, filterEndTimeMillis, filterFlags, globalContentOffsetMillis, null, 0, historyFiles);
                    sHistoricalAppOpsDir.finishRead();
                    return ops;
                } catch (java.lang.Throwable th) {
                    t = th;
                    baseDir = baseDir2;
                    com.android.server.appop.HistoricalRegistry.wtf("Error reading historical app ops. Deleting history.", t, baseDir);
                    sHistoricalAppOpsDir.delete();
                    return null;
                }
            } catch (java.lang.Throwable th2) {
                t = th2;
            }
        }

        private java.util.LinkedList<android.app.AppOpsManager.HistoricalOps> collectHistoricalOpsRecursiveDLocked(java.io.File baseDir, int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, long filterBeginTimeMillis, long filterEndTimeMillis, int filterFlags, long[] globalContentOffsetMillis, java.util.LinkedList<android.app.AppOpsManager.HistoricalOps> outOps, int depth, java.util.Set<java.lang.String> historyFiles) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            long previousIntervalEndMillis = ((long) java.lang.Math.pow(this.mIntervalCompressionMultiplier, depth)) * this.mBaseSnapshotInterval;
            long currentIntervalEndMillis = this.mBaseSnapshotInterval * ((long) java.lang.Math.pow(this.mIntervalCompressionMultiplier, depth + 1));
            long filterBeginTimeMillis2 = java.lang.Math.max(filterBeginTimeMillis - previousIntervalEndMillis, 0L);
            long filterEndTimeMillis2 = filterEndTimeMillis - previousIntervalEndMillis;
            java.util.List<android.app.AppOpsManager.HistoricalOps> readOps = readHistoricalOpsLocked(baseDir, previousIntervalEndMillis, currentIntervalEndMillis, filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterBeginTimeMillis2, filterEndTimeMillis2, filterFlags, globalContentOffsetMillis, depth, historyFiles);
            if (readOps != null && readOps.isEmpty()) {
                return outOps;
            }
            java.util.LinkedList<android.app.AppOpsManager.HistoricalOps> outOps2 = collectHistoricalOpsRecursiveDLocked(baseDir, filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterBeginTimeMillis2, filterEndTimeMillis2, filterFlags, globalContentOffsetMillis, outOps, depth + 1, historyFiles);
            if (outOps2 != null) {
                int opCount = outOps2.size();
                for (int i = 0; i < opCount; i++) {
                    android.app.AppOpsManager.HistoricalOps collectedOp = outOps2.get(i);
                    collectedOp.offsetBeginAndEndTime(currentIntervalEndMillis);
                }
            }
            if (readOps != null) {
                if (outOps2 == null) {
                    outOps2 = new java.util.LinkedList<>();
                }
                int opCount2 = readOps.size();
                for (int i2 = opCount2 - 1; i2 >= 0; i2--) {
                    outOps2.offerFirst(readOps.get(i2));
                }
            }
            return outOps2;
        }

        private void handlePersistHistoricalOpsRecursiveDLocked(java.io.File newBaseDir, java.io.File oldBaseDir, java.util.List<android.app.AppOpsManager.HistoricalOps> passedOps, java.util.Set<java.lang.String> oldFileNames, int depth) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.util.List<android.app.AppOpsManager.HistoricalOps> list;
            android.app.AppOpsManager.HistoricalOps persistedOp;
            android.app.AppOpsManager.HistoricalOps overflowedOp;
            int existingOpCount;
            long previousIntervalEndMillis = ((long) java.lang.Math.pow(this.mIntervalCompressionMultiplier, depth)) * this.mBaseSnapshotInterval;
            long currentIntervalEndMillis = ((long) java.lang.Math.pow(this.mIntervalCompressionMultiplier, depth + 1)) * this.mBaseSnapshotInterval;
            if (passedOps == null || passedOps.isEmpty()) {
                int i = depth;
                java.util.Set<java.lang.String> set = oldFileNames;
                com.android.server.appop.HistoricalRegistry.Persistence persistence = this;
                java.io.File file = newBaseDir;
                if (!oldFileNames.isEmpty()) {
                    java.io.File oldFile = persistence.generateFile(oldBaseDir, i);
                    if (set.remove(oldFile.getName())) {
                        java.nio.file.Files.createLink(persistence.generateFile(file, i).toPath(), oldFile.toPath());
                    }
                    handlePersistHistoricalOpsRecursiveDLocked(newBaseDir, oldBaseDir, passedOps, oldFileNames, i + 1);
                    return;
                }
                return;
            }
            int passedOpCount = passedOps.size();
            for (int i2 = 0; i2 < passedOpCount; i2++) {
                android.app.AppOpsManager.HistoricalOps passedOp = passedOps.get(i2);
                passedOp.offsetBeginAndEndTime(-previousIntervalEndMillis);
            }
            java.util.List<android.app.AppOpsManager.HistoricalOps> existingOps = readHistoricalOpsLocked(oldBaseDir, previousIntervalEndMillis, currentIntervalEndMillis, -1, null, null, null, 0, Long.MIN_VALUE, Long.MAX_VALUE, 31, null, depth, null);
            if (existingOps == null || (existingOpCount = existingOps.size()) <= 0) {
                list = passedOps;
            } else {
                list = passedOps;
                long elapsedTimeMillis = list.get(passedOps.size() - 1).getEndTimeMillis();
                for (int i3 = 0; i3 < existingOpCount; i3++) {
                    android.app.AppOpsManager.HistoricalOps existingOp = existingOps.get(i3);
                    existingOp.offsetBeginAndEndTime(elapsedTimeMillis);
                }
            }
            java.util.List<android.app.AppOpsManager.HistoricalOps> allOps = new java.util.LinkedList<>(list);
            if (existingOps != null) {
                allOps.addAll(existingOps);
            }
            int opCount = allOps.size();
            java.util.List<android.app.AppOpsManager.HistoricalOps> persistedOps = null;
            java.util.List<android.app.AppOpsManager.HistoricalOps> overflowedOps = null;
            long intervalOverflowMillis = 0;
            for (int i4 = 0; i4 < opCount; i4++) {
                android.app.AppOpsManager.HistoricalOps op = allOps.get(i4);
                if (op.getEndTimeMillis() <= currentIntervalEndMillis) {
                    persistedOp = op;
                    overflowedOp = null;
                } else if (op.getBeginTimeMillis() < currentIntervalEndMillis) {
                    persistedOp = op;
                    long intervalOverflowMillis2 = op.getEndTimeMillis() - currentIntervalEndMillis;
                    if (intervalOverflowMillis2 > previousIntervalEndMillis) {
                        double splitScale = intervalOverflowMillis2 / op.getDurationMillis();
                        overflowedOp = spliceFromEnd(op, splitScale);
                        long intervalOverflowMillis3 = op.getEndTimeMillis() - currentIntervalEndMillis;
                        persistedOp = persistedOp;
                        intervalOverflowMillis = intervalOverflowMillis3;
                    } else {
                        overflowedOp = null;
                        intervalOverflowMillis = intervalOverflowMillis2;
                    }
                } else {
                    persistedOp = null;
                    overflowedOp = op;
                }
                if (persistedOp != null) {
                    if (persistedOps == null) {
                        persistedOps = new java.util.ArrayList<>();
                    }
                    persistedOps.add(persistedOp);
                }
                if (overflowedOp != null) {
                    if (overflowedOps == null) {
                        overflowedOps = new java.util.ArrayList<>();
                    }
                    overflowedOps.add(overflowedOp);
                }
            }
            java.io.File newFile = generateFile(newBaseDir, depth);
            oldFileNames.remove(newFile.getName());
            if (persistedOps != null) {
                normalizeSnapshotForSlotDuration(persistedOps, previousIntervalEndMillis);
                writeHistoricalOpsDLocked(persistedOps, intervalOverflowMillis, newFile);
            }
            handlePersistHistoricalOpsRecursiveDLocked(newBaseDir, oldBaseDir, overflowedOps, oldFileNames, depth + 1);
        }

        private java.util.List<android.app.AppOpsManager.HistoricalOps> readHistoricalOpsLocked(java.io.File baseDir, long intervalBeginMillis, long intervalEndMillis, int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, long filterBeginTimeMillis, long filterEndTimeMillis, int filterFlags, long[] cumulativeOverflowMillis, int depth, java.util.Set<java.lang.String> historyFiles) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.io.File file = generateFile(baseDir, depth);
            if (historyFiles != null) {
                historyFiles.remove(file.getName());
            }
            if (filterBeginTimeMillis >= filterEndTimeMillis || filterEndTimeMillis < intervalBeginMillis) {
                return java.util.Collections.emptyList();
            }
            if (filterBeginTimeMillis >= intervalEndMillis + ((intervalEndMillis - intervalBeginMillis) / this.mIntervalCompressionMultiplier) + (cumulativeOverflowMillis != null ? cumulativeOverflowMillis[0] : 0L) || !file.exists()) {
                if (historyFiles == null || historyFiles.isEmpty()) {
                    return java.util.Collections.emptyList();
                }
                return null;
            }
            return readHistoricalOpsLocked(file, filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterBeginTimeMillis, filterEndTimeMillis, filterFlags, cumulativeOverflowMillis);
        }

        private java.util.List<android.app.AppOpsManager.HistoricalOps> readHistoricalOpsLocked(java.io.File file, int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, long filterBeginTimeMillis, long filterEndTimeMillis, int filterFlags, long[] cumulativeOverflowMillis) throws java.lang.Throwable {
            java.lang.Throwable th;
            int depth;
            int version;
            try {
                java.io.FileInputStream stream = new java.io.FileInputStream(file);
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                    com.android.internal.util.XmlUtils.beginDocument(parser, TAG_HISTORY);
                    int version2 = parser.getAttributeInt((java.lang.String) null, ATTR_VERSION);
                    if (version2 < 2) {
                        throw new java.lang.IllegalStateException("Dropping unsupported history version 1 for file:" + file);
                    }
                    long overflowMillis = parser.getAttributeLong((java.lang.String) null, ATTR_OVERFLOW, 0L);
                    int depth2 = parser.getDepth();
                    java.util.List<android.app.AppOpsManager.HistoricalOps> allOps = null;
                    while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth2)) {
                        try {
                            if (TAG_OPS.equals(parser.getName())) {
                                depth = depth2;
                                version = version2;
                                android.app.AppOpsManager.HistoricalOps ops = readeHistoricalOpsDLocked(parser, filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterBeginTimeMillis, filterEndTimeMillis, filterFlags, cumulativeOverflowMillis);
                                if (ops != null) {
                                    if (ops.isEmpty()) {
                                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                    } else {
                                        java.util.List<android.app.AppOpsManager.HistoricalOps> allOps2 = allOps == null ? new java.util.ArrayList<>() : allOps;
                                        try {
                                            allOps2.add(ops);
                                            allOps = allOps2;
                                            depth2 = depth;
                                            version2 = version;
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            try {
                                                stream.close();
                                                throw th;
                                            } catch (java.lang.Throwable th3) {
                                                th.addSuppressed(th3);
                                                throw th;
                                            }
                                        }
                                    }
                                }
                            } else {
                                depth = depth2;
                                version = version2;
                            }
                            depth2 = depth;
                            version2 = version;
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    }
                    if (cumulativeOverflowMillis != null) {
                        cumulativeOverflowMillis[0] = cumulativeOverflowMillis[0] + overflowMillis;
                    }
                    try {
                        stream.close();
                        return allOps;
                    } catch (java.io.FileNotFoundException e) {
                        android.util.Slog.i(LOG_TAG, "No history file: " + file.getName());
                        return java.util.Collections.emptyList();
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            } catch (java.io.FileNotFoundException e2) {
            }
        }

        private android.app.AppOpsManager.HistoricalOps readeHistoricalOpsDLocked(com.android.modules.utils.TypedXmlPullParser parser, int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, long filterBeginTimeMillis, long filterEndTimeMillis, int filterFlags, long[] cumulativeOverflowMillis) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            com.android.modules.utils.TypedXmlPullParser typedXmlPullParser = parser;
            long beginTimeMillis = typedXmlPullParser.getAttributeLong((java.lang.String) null, ATTR_BEGIN_TIME, 0L) + (cumulativeOverflowMillis != null ? cumulativeOverflowMillis[0] : 0L);
            long endTimeMillis = typedXmlPullParser.getAttributeLong((java.lang.String) null, ATTR_END_TIME, 0L) + (cumulativeOverflowMillis != null ? cumulativeOverflowMillis[0] : 0L);
            if (filterEndTimeMillis < beginTimeMillis) {
                return null;
            }
            if (filterBeginTimeMillis > endTimeMillis) {
                return new android.app.AppOpsManager.HistoricalOps(0L, 0L);
            }
            long filteredBeginTimeMillis = java.lang.Math.max(beginTimeMillis, filterBeginTimeMillis);
            long filteredEndTimeMillis = java.lang.Math.min(endTimeMillis, filterEndTimeMillis);
            long filteredEndTimeMillis2 = filteredEndTimeMillis;
            double filterScale = (filteredEndTimeMillis - filteredBeginTimeMillis) / (endTimeMillis - beginTimeMillis);
            int depth = parser.getDepth();
            android.app.AppOpsManager.HistoricalOps ops = null;
            while (com.android.internal.util.XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if ("uid".equals(parser.getName())) {
                    android.app.AppOpsManager.HistoricalOps ops2 = ops;
                    long filteredEndTimeMillis3 = filteredEndTimeMillis2;
                    int depth2 = depth;
                    long filteredBeginTimeMillis2 = filteredBeginTimeMillis;
                    long endTimeMillis2 = endTimeMillis;
                    long beginTimeMillis2 = beginTimeMillis;
                    android.app.AppOpsManager.HistoricalOps returnedOps = readHistoricalUidOpsDLocked(ops, parser, filterUid, filterPackageName, filterAttributionTag, filterOpNames, filter, filterFlags, filterScale);
                    if (ops2 != null) {
                        ops = ops2;
                    } else {
                        ops = returnedOps;
                    }
                    typedXmlPullParser = parser;
                    filteredBeginTimeMillis = filteredBeginTimeMillis2;
                    depth = depth2;
                    endTimeMillis = endTimeMillis2;
                    beginTimeMillis = beginTimeMillis2;
                    filteredEndTimeMillis2 = filteredEndTimeMillis3;
                } else {
                    typedXmlPullParser = parser;
                    filteredEndTimeMillis2 = filteredEndTimeMillis2;
                }
            }
            android.app.AppOpsManager.HistoricalOps ops3 = ops;
            long filteredBeginTimeMillis3 = filteredBeginTimeMillis;
            long filteredEndTimeMillis4 = filteredEndTimeMillis2;
            if (ops3 != null) {
                ops3.setBeginAndEndTime(filteredBeginTimeMillis3, filteredEndTimeMillis4);
            }
            return ops3;
        }

        private android.app.AppOpsManager.HistoricalOps readHistoricalUidOpsDLocked(android.app.AppOpsManager.HistoricalOps ops, com.android.modules.utils.TypedXmlPullParser parser, int filterUid, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, int filterFlags, double filterScale) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int uid = parser.getAttributeInt((java.lang.String) null, ATTR_NAME);
            if ((filter & 1) != 0 && filterUid != uid) {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                return null;
            }
            int depth = parser.getDepth();
            android.app.AppOpsManager.HistoricalOps ops2 = ops;
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if (TAG_PACKAGE.equals(parser.getName())) {
                    android.app.AppOpsManager.HistoricalOps returnedOps = readHistoricalPackageOpsDLocked(ops2, uid, parser, filterPackageName, filterAttributionTag, filterOpNames, filter, filterFlags, filterScale);
                    if (ops2 == null) {
                        ops2 = returnedOps;
                    }
                }
            }
            return ops2;
        }

        private android.app.AppOpsManager.HistoricalOps readHistoricalPackageOpsDLocked(android.app.AppOpsManager.HistoricalOps ops, int uid, com.android.modules.utils.TypedXmlPullParser parser, java.lang.String filterPackageName, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, int filterFlags, double filterScale) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_NAME);
            if ((filter & 2) != 0 && !filterPackageName.equals(packageName)) {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                return null;
            }
            int depth = parser.getDepth();
            android.app.AppOpsManager.HistoricalOps ops2 = ops;
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if (TAG_ATTRIBUTION.equals(parser.getName())) {
                    android.app.AppOpsManager.HistoricalOps returnedOps = readHistoricalAttributionOpsDLocked(ops2, uid, packageName, parser, filterAttributionTag, filterOpNames, filter, filterFlags, filterScale);
                    if (ops2 == null) {
                        ops2 = returnedOps;
                    }
                }
            }
            return ops2;
        }

        private android.app.AppOpsManager.HistoricalOps readHistoricalAttributionOpsDLocked(android.app.AppOpsManager.HistoricalOps ops, int uid, java.lang.String packageName, com.android.modules.utils.TypedXmlPullParser parser, java.lang.String filterAttributionTag, java.lang.String[] filterOpNames, int filter, int filterFlags, double filterScale) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String attributionTag = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_NAME);
            if ((filter & 4) != 0 && !java.util.Objects.equals(filterAttributionTag, attributionTag)) {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                return null;
            }
            int depth = parser.getDepth();
            android.app.AppOpsManager.HistoricalOps ops2 = ops;
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if (TAG_OP.equals(parser.getName())) {
                    android.app.AppOpsManager.HistoricalOps returnedOps = readHistoricalOpDLocked(ops2, uid, packageName, attributionTag, parser, filterOpNames, filter, filterFlags, filterScale);
                    if (ops2 == null) {
                        ops2 = returnedOps;
                    }
                }
            }
            return ops2;
        }

        private android.app.AppOpsManager.HistoricalOps readHistoricalOpDLocked(android.app.AppOpsManager.HistoricalOps ops, int uid, java.lang.String packageName, java.lang.String attributionTag, com.android.modules.utils.TypedXmlPullParser parser, java.lang.String[] filterOpNames, int filter, int filterFlags, double filterScale) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int op = parser.getAttributeInt((java.lang.String) null, ATTR_NAME);
            if ((filter & 8) != 0 && !com.android.internal.util.ArrayUtils.contains(filterOpNames, android.app.AppOpsManager.opToPublicName(op))) {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                return null;
            }
            int depth = parser.getDepth();
            android.app.AppOpsManager.HistoricalOps ops2 = ops;
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if (TAG_STATE.equals(parser.getName())) {
                    android.app.AppOpsManager.HistoricalOps returnedOps = readStateDLocked(ops2, uid, packageName, attributionTag, op, parser, filterFlags, filterScale);
                    if (ops2 == null) {
                        ops2 = returnedOps;
                    }
                }
            }
            return ops2;
        }

        private android.app.AppOpsManager.HistoricalOps readStateDLocked(android.app.AppOpsManager.HistoricalOps ops, int uid, java.lang.String packageName, java.lang.String attributionTag, int op, com.android.modules.utils.TypedXmlPullParser parser, int filterFlags, double filterScale) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            long key;
            android.app.AppOpsManager.HistoricalOps ops2;
            long accessDuration;
            long rejectCount;
            long accessCount;
            long key2 = parser.getAttributeLong((java.lang.String) null, ATTR_NAME);
            int flags = android.app.AppOpsManager.extractFlagsFromKey(key2) & filterFlags;
            if (flags == 0) {
                return null;
            }
            int uidState = android.app.AppOpsManager.extractUidStateFromKey(key2);
            long accessCount2 = parser.getAttributeLong((java.lang.String) null, ATTR_ACCESS_COUNT, 0L);
            if (accessCount2 > 0) {
                if (java.lang.Double.isNaN(filterScale)) {
                    accessCount = accessCount2;
                } else {
                    accessCount = (long) android.app.AppOpsManager.HistoricalOps.round(accessCount2 * filterScale);
                }
                if (ops != null) {
                    ops2 = ops;
                } else {
                    ops2 = new android.app.AppOpsManager.HistoricalOps(0L, 0L);
                }
                key = 0;
                ops2.increaseAccessCount(op, uid, packageName, attributionTag, uidState, flags, accessCount);
            } else {
                key = 0;
                ops2 = ops;
            }
            long rejectCount2 = parser.getAttributeLong((java.lang.String) null, ATTR_REJECT_COUNT, key);
            if (rejectCount2 > key) {
                if (java.lang.Double.isNaN(filterScale)) {
                    rejectCount = rejectCount2;
                } else {
                    rejectCount = (long) android.app.AppOpsManager.HistoricalOps.round(rejectCount2 * filterScale);
                }
                if (ops2 == null) {
                    ops2 = new android.app.AppOpsManager.HistoricalOps(key, key);
                }
                ops2.increaseRejectCount(op, uid, packageName, attributionTag, uidState, flags, rejectCount);
            }
            long accessDuration2 = parser.getAttributeLong((java.lang.String) null, ATTR_ACCESS_DURATION, key);
            if (accessDuration2 > key) {
                if (java.lang.Double.isNaN(filterScale)) {
                    accessDuration = accessDuration2;
                } else {
                    accessDuration = (long) android.app.AppOpsManager.HistoricalOps.round(accessDuration2 * filterScale);
                }
                if (ops2 == null) {
                    ops2 = new android.app.AppOpsManager.HistoricalOps(key, key);
                }
                ops2.increaseAccessDuration(op, uid, packageName, attributionTag, uidState, flags, accessDuration);
            }
            return ops2;
        }

        private void writeHistoricalOpsDLocked(java.util.List<android.app.AppOpsManager.HistoricalOps> allOps, long intervalOverflowMillis, java.io.File file) throws java.io.IOException {
            java.io.FileOutputStream output = sHistoricalAppOpsDir.openWrite(file);
            try {
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(output);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startDocument((java.lang.String) null, true);
                serializer.startTag((java.lang.String) null, TAG_HISTORY);
                serializer.attributeInt((java.lang.String) null, ATTR_VERSION, 2);
                if (intervalOverflowMillis != 0) {
                    serializer.attributeLong((java.lang.String) null, ATTR_OVERFLOW, intervalOverflowMillis);
                }
                if (allOps != null) {
                    int opsCount = allOps.size();
                    for (int i = 0; i < opsCount; i++) {
                        android.app.AppOpsManager.HistoricalOps ops = allOps.get(i);
                        writeHistoricalOpDLocked(ops, serializer);
                    }
                }
                serializer.endTag((java.lang.String) null, TAG_HISTORY);
                serializer.endDocument();
                sHistoricalAppOpsDir.closeWrite(output);
            } catch (java.io.IOException e) {
                sHistoricalAppOpsDir.failWrite(output);
                throw e;
            }
        }

        private void writeHistoricalOpDLocked(android.app.AppOpsManager.HistoricalOps ops, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, TAG_OPS);
            serializer.attributeLong((java.lang.String) null, ATTR_BEGIN_TIME, ops.getBeginTimeMillis());
            serializer.attributeLong((java.lang.String) null, ATTR_END_TIME, ops.getEndTimeMillis());
            int uidCount = ops.getUidCount();
            for (int i = 0; i < uidCount; i++) {
                android.app.AppOpsManager.HistoricalUidOps uidOp = ops.getUidOpsAt(i);
                writeHistoricalUidOpsDLocked(uidOp, serializer);
            }
            serializer.endTag((java.lang.String) null, TAG_OPS);
        }

        private void writeHistoricalUidOpsDLocked(android.app.AppOpsManager.HistoricalUidOps uidOps, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, "uid");
            serializer.attributeInt((java.lang.String) null, ATTR_NAME, uidOps.getUid());
            int packageCount = uidOps.getPackageCount();
            for (int i = 0; i < packageCount; i++) {
                android.app.AppOpsManager.HistoricalPackageOps packageOps = uidOps.getPackageOpsAt(i);
                writeHistoricalPackageOpsDLocked(packageOps, serializer);
            }
            serializer.endTag((java.lang.String) null, "uid");
        }

        private void writeHistoricalPackageOpsDLocked(android.app.AppOpsManager.HistoricalPackageOps packageOps, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, TAG_PACKAGE);
            serializer.attributeInterned((java.lang.String) null, ATTR_NAME, packageOps.getPackageName());
            int numAttributions = packageOps.getAttributedOpsCount();
            for (int i = 0; i < numAttributions; i++) {
                android.app.AppOpsManager.AttributedHistoricalOps op = packageOps.getAttributedOpsAt(i);
                writeHistoricalAttributionOpsDLocked(op, serializer);
            }
            serializer.endTag((java.lang.String) null, TAG_PACKAGE);
        }

        private void writeHistoricalAttributionOpsDLocked(android.app.AppOpsManager.AttributedHistoricalOps attributionOps, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, TAG_ATTRIBUTION);
            com.android.internal.util.XmlUtils.writeStringAttribute(serializer, ATTR_NAME, attributionOps.getTag());
            int opCount = attributionOps.getOpCount();
            for (int i = 0; i < opCount; i++) {
                android.app.AppOpsManager.HistoricalOp op = attributionOps.getOpAt(i);
                writeHistoricalOpDLocked(op, serializer);
            }
            serializer.endTag((java.lang.String) null, TAG_ATTRIBUTION);
        }

        private void writeHistoricalOpDLocked(android.app.AppOpsManager.HistoricalOp op, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            android.util.LongSparseArray keys = op.collectKeys();
            if (keys == null || keys.size() <= 0) {
                return;
            }
            serializer.startTag((java.lang.String) null, TAG_OP);
            serializer.attributeInt((java.lang.String) null, ATTR_NAME, op.getOpCode());
            int keyCount = keys.size();
            for (int i = 0; i < keyCount; i++) {
                writeStateOnLocked(op, keys.keyAt(i), serializer);
            }
            serializer.endTag((java.lang.String) null, TAG_OP);
        }

        private void writeStateOnLocked(android.app.AppOpsManager.HistoricalOp op, long key, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            int uidState = android.app.AppOpsManager.extractUidStateFromKey(key);
            int flags = android.app.AppOpsManager.extractFlagsFromKey(key);
            long accessCount = op.getAccessCount(uidState, uidState, flags);
            long rejectCount = op.getRejectCount(uidState, uidState, flags);
            long accessDuration = op.getAccessDuration(uidState, uidState, flags);
            if (accessCount <= 0 && rejectCount <= 0 && accessDuration <= 0) {
                return;
            }
            serializer.startTag((java.lang.String) null, TAG_STATE);
            serializer.attributeLong((java.lang.String) null, ATTR_NAME, key);
            if (accessCount > 0) {
                serializer.attributeLong((java.lang.String) null, ATTR_ACCESS_COUNT, accessCount);
            }
            if (rejectCount > 0) {
                serializer.attributeLong((java.lang.String) null, ATTR_REJECT_COUNT, rejectCount);
            }
            if (accessDuration > 0) {
                serializer.attributeLong((java.lang.String) null, ATTR_ACCESS_DURATION, accessDuration);
            }
            serializer.endTag((java.lang.String) null, TAG_STATE);
        }

        private static void enforceOpsWellFormed(java.util.List<android.app.AppOpsManager.HistoricalOps> ops) {
            if (ops == null) {
                return;
            }
            android.app.AppOpsManager.HistoricalOps current = null;
            int opsCount = ops.size();
            for (int i = 0; i < opsCount; i++) {
                android.app.AppOpsManager.HistoricalOps previous = current;
                android.app.AppOpsManager.HistoricalOps current2 = ops.get(i);
                current = current2;
                if (current.isEmpty()) {
                    throw new java.lang.IllegalStateException("Empty ops:\n" + opsToDebugString(ops));
                }
                if (current.getEndTimeMillis() < current.getBeginTimeMillis()) {
                    throw new java.lang.IllegalStateException("Begin after end:\n" + opsToDebugString(ops));
                }
                if (previous != null) {
                    if (previous.getEndTimeMillis() > current.getBeginTimeMillis()) {
                        throw new java.lang.IllegalStateException("Intersecting ops:\n" + opsToDebugString(ops));
                    }
                    if (previous.getBeginTimeMillis() > current.getBeginTimeMillis()) {
                        throw new java.lang.IllegalStateException("Non increasing ops:\n" + opsToDebugString(ops));
                    }
                }
            }
        }

        private long computeGlobalIntervalBeginMillis(int depth) {
            long beginTimeMillis = 0;
            for (int i = 0; i < depth + 1; i++) {
                beginTimeMillis = (long) (beginTimeMillis + java.lang.Math.pow(this.mIntervalCompressionMultiplier, i));
            }
            return this.mBaseSnapshotInterval * beginTimeMillis;
        }

        private static android.app.AppOpsManager.HistoricalOps spliceFromEnd(android.app.AppOpsManager.HistoricalOps ops, double spliceRatio) {
            android.app.AppOpsManager.HistoricalOps splice = ops.spliceFromEnd(spliceRatio);
            return splice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static android.app.AppOpsManager.HistoricalOps spliceFromBeginning(android.app.AppOpsManager.HistoricalOps ops, double spliceRatio) {
            android.app.AppOpsManager.HistoricalOps splice = ops.spliceFromBeginning(spliceRatio);
            return splice;
        }

        private static void normalizeSnapshotForSlotDuration(java.util.List<android.app.AppOpsManager.HistoricalOps> ops, long slotDurationMillis) {
            int opCount = ops.size();
            int processedIdx = opCount - 1;
            while (processedIdx >= 0) {
                android.app.AppOpsManager.HistoricalOps processedOp = ops.get(processedIdx);
                long slotBeginTimeMillis = java.lang.Math.max(processedOp.getEndTimeMillis() - slotDurationMillis, 0L);
                for (int candidateIdx = processedIdx - 1; candidateIdx >= 0; candidateIdx--) {
                    android.app.AppOpsManager.HistoricalOps candidateOp = ops.get(candidateIdx);
                    long candidateSlotIntersectionMillis = candidateOp.getEndTimeMillis() - java.lang.Math.min(slotBeginTimeMillis, processedOp.getBeginTimeMillis());
                    if (candidateSlotIntersectionMillis <= 0) {
                        break;
                    }
                    float candidateSplitRatio = candidateSlotIntersectionMillis / candidateOp.getDurationMillis();
                    if (java.lang.Float.compare(candidateSplitRatio, 1.0f) >= 0) {
                        ops.remove(candidateIdx);
                        processedIdx--;
                        processedOp.merge(candidateOp);
                    } else {
                        android.app.AppOpsManager.HistoricalOps endSplice = spliceFromEnd(candidateOp, candidateSplitRatio);
                        if (endSplice != null) {
                            processedOp.merge(endSplice);
                        }
                        if (candidateOp.isEmpty()) {
                            ops.remove(candidateIdx);
                            processedIdx--;
                        }
                    }
                }
                processedIdx--;
            }
        }

        private static java.lang.String opsToDebugString(java.util.List<android.app.AppOpsManager.HistoricalOps> ops) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            int opCount = ops.size();
            for (int i = 0; i < opCount; i++) {
                builder.append("  ");
                builder.append(ops.get(i));
                if (i < opCount - 1) {
                    builder.append('\n');
                }
            }
            return builder.toString();
        }

        private static java.util.Set<java.lang.String> getHistoricalFileNames(java.io.File historyDir) {
            java.io.File[] files = historyDir.listFiles();
            if (files == null) {
                return java.util.Collections.emptySet();
            }
            android.util.ArraySet<java.lang.String> fileNames = new android.util.ArraySet<>(files.length);
            for (java.io.File file : files) {
                fileNames.add(file.getName());
            }
            return fileNames;
        }
    }

    private static class HistoricalFilesInvariant {
        private final java.util.List<java.io.File> mBeginFiles = new java.util.ArrayList();

        private HistoricalFilesInvariant() {
        }

        public void startTracking(java.io.File folder) {
            java.io.File[] files = folder.listFiles();
            if (files != null) {
                java.util.Collections.addAll(this.mBeginFiles, files);
            }
        }

        public void stopTracking(java.io.File folder) {
            java.util.List<java.io.File> endFiles = new java.util.ArrayList<>();
            java.io.File[] files = folder.listFiles();
            if (files != null) {
                java.util.Collections.addAll(endFiles, files);
            }
            long beginOldestFileOffsetMillis = getOldestFileOffsetMillis(this.mBeginFiles);
            long endOldestFileOffsetMillis = getOldestFileOffsetMillis(endFiles);
            if (endOldestFileOffsetMillis < beginOldestFileOffsetMillis) {
                java.lang.String message = "History loss detected!\nold files: " + this.mBeginFiles;
                com.android.server.appop.HistoricalRegistry.wtf(message, null, folder);
                throw new java.lang.IllegalStateException(message);
            }
        }

        private static long getOldestFileOffsetMillis(java.util.List<java.io.File> files) {
            if (files.isEmpty()) {
                return 0L;
            }
            java.lang.String longestName = files.get(0).getName();
            int fileCount = files.size();
            for (int i = 1; i < fileCount; i++) {
                java.io.File file = files.get(i);
                if (file.getName().length() > longestName.length()) {
                    longestName = file.getName();
                }
            }
            return java.lang.Long.parseLong(longestName.replace(com.android.server.appop.HistoricalRegistry.HISTORY_FILE_SUFFIX, ""));
        }
    }

    private final class StringDumpVisitor implements android.app.AppOpsManager.HistoricalOpsVisitor {
        private final java.lang.String mAttributionPrefix;
        private final java.lang.String mEntryPrefix;
        private final int mFilter;
        private final java.lang.String mFilterAttributionTag;
        private final int mFilterOp;
        private final java.lang.String mFilterPackage;
        private final int mFilterUid;
        private final java.lang.String mOpsPrefix;
        private final java.lang.String mPackagePrefix;
        private final java.lang.String mUidPrefix;
        private final java.lang.String mUidStatePrefix;
        private final java.io.PrintWriter mWriter;
        private final long mNow = java.lang.System.currentTimeMillis();
        private final java.text.SimpleDateFormat mDateFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private final java.util.Date mDate = new java.util.Date();

        StringDumpVisitor(java.lang.String prefix, java.io.PrintWriter writer, int filterUid, java.lang.String filterPackage, java.lang.String filterAttributionTag, int filterOp, int filter) {
            this.mOpsPrefix = prefix + "  ";
            this.mUidPrefix = this.mOpsPrefix + "  ";
            this.mPackagePrefix = this.mUidPrefix + "  ";
            this.mAttributionPrefix = this.mPackagePrefix + "  ";
            this.mEntryPrefix = this.mAttributionPrefix + "  ";
            this.mUidStatePrefix = this.mEntryPrefix + "  ";
            this.mWriter = writer;
            this.mFilterUid = filterUid;
            this.mFilterPackage = filterPackage;
            this.mFilterAttributionTag = filterAttributionTag;
            this.mFilterOp = filterOp;
            this.mFilter = filter;
        }

        public void visitHistoricalOps(android.app.AppOpsManager.HistoricalOps ops) {
            this.mWriter.println();
            this.mWriter.print(this.mOpsPrefix);
            this.mWriter.println("snapshot:");
            this.mWriter.print(this.mUidPrefix);
            this.mWriter.print("begin = ");
            this.mDate.setTime(ops.getBeginTimeMillis());
            this.mWriter.print(this.mDateFormatter.format(this.mDate));
            this.mWriter.print("  (");
            android.util.TimeUtils.formatDuration(ops.getBeginTimeMillis() - this.mNow, this.mWriter);
            this.mWriter.println(")");
            this.mWriter.print(this.mUidPrefix);
            this.mWriter.print("end = ");
            this.mDate.setTime(ops.getEndTimeMillis());
            this.mWriter.print(this.mDateFormatter.format(this.mDate));
            this.mWriter.print("  (");
            android.util.TimeUtils.formatDuration(ops.getEndTimeMillis() - this.mNow, this.mWriter);
            this.mWriter.println(")");
        }

        public void visitHistoricalUidOps(android.app.AppOpsManager.HistoricalUidOps ops) {
            if ((this.mFilter & 1) != 0 && this.mFilterUid != ops.getUid()) {
                return;
            }
            this.mWriter.println();
            this.mWriter.print(this.mUidPrefix);
            this.mWriter.print("Uid ");
            android.os.UserHandle.formatUid(this.mWriter, ops.getUid());
            this.mWriter.println(":");
        }

        public void visitHistoricalPackageOps(android.app.AppOpsManager.HistoricalPackageOps ops) {
            if ((this.mFilter & 2) != 0 && !this.mFilterPackage.equals(ops.getPackageName())) {
                return;
            }
            this.mWriter.print(this.mPackagePrefix);
            this.mWriter.print("Package ");
            this.mWriter.print(ops.getPackageName());
            this.mWriter.println(":");
        }

        public void visitHistoricalAttributionOps(android.app.AppOpsManager.AttributedHistoricalOps ops) {
            if ((this.mFilter & 4) != 0 && !java.util.Objects.equals(this.mFilterPackage, ops.getTag())) {
                return;
            }
            this.mWriter.print(this.mAttributionPrefix);
            this.mWriter.print("Attribution ");
            this.mWriter.print(ops.getTag());
            this.mWriter.println(":");
        }

        public void visitHistoricalOp(android.app.AppOpsManager.HistoricalOp ops) {
            int keyCount;
            if ((this.mFilter & 8) == 0 || this.mFilterOp == ops.getOpCode()) {
                this.mWriter.print(this.mEntryPrefix);
                this.mWriter.print(android.app.AppOpsManager.opToName(ops.getOpCode()));
                this.mWriter.println(":");
                android.util.LongSparseArray keys = ops.collectKeys();
                int keyCount2 = keys.size();
                int i = 0;
                while (i < keyCount2) {
                    long key = keys.keyAt(i);
                    int uidState = android.app.AppOpsManager.extractUidStateFromKey(key);
                    int flags = android.app.AppOpsManager.extractFlagsFromKey(key);
                    boolean printedUidState = false;
                    long accessCount = ops.getAccessCount(uidState, uidState, flags);
                    if (accessCount > 0) {
                        if (0 == 0) {
                            this.mWriter.print(this.mUidStatePrefix);
                            this.mWriter.print(android.app.AppOpsManager.keyToString(key));
                            this.mWriter.print(" = ");
                            printedUidState = true;
                        }
                        this.mWriter.print("access=");
                        this.mWriter.print(accessCount);
                    }
                    long rejectCount = ops.getRejectCount(uidState, uidState, flags);
                    android.util.LongSparseArray keys2 = keys;
                    if (rejectCount <= 0) {
                        keyCount = keyCount2;
                    } else {
                        if (!printedUidState) {
                            keyCount = keyCount2;
                            this.mWriter.print(this.mUidStatePrefix);
                            this.mWriter.print(android.app.AppOpsManager.keyToString(key));
                            this.mWriter.print(" = ");
                            printedUidState = true;
                        } else {
                            keyCount = keyCount2;
                            this.mWriter.print(", ");
                        }
                        this.mWriter.print("reject=");
                        this.mWriter.print(rejectCount);
                    }
                    long accessDuration = ops.getAccessDuration(uidState, uidState, flags);
                    if (accessDuration > 0) {
                        if (!printedUidState) {
                            this.mWriter.print(this.mUidStatePrefix);
                            this.mWriter.print(android.app.AppOpsManager.keyToString(key));
                            this.mWriter.print(" = ");
                            printedUidState = true;
                        } else {
                            this.mWriter.print(", ");
                        }
                        this.mWriter.print("duration=");
                        android.util.TimeUtils.formatDuration(accessDuration, this.mWriter);
                    }
                    if (printedUidState) {
                        this.mWriter.println("");
                    }
                    i++;
                    keys = keys2;
                    keyCount2 = keyCount;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void wtf(java.lang.String message, java.lang.Throwable t, java.io.File storage) {
        android.util.Slog.wtf(LOG_TAG, message, t);
        if (KEEP_WTF_LOG) {
            try {
                java.io.File file = new java.io.File(new java.io.File(android.os.Environment.getDataSystemDirectory(), "appops"), "wtf" + android.util.TimeUtils.formatForLogging(java.lang.System.currentTimeMillis()));
                if (file.createNewFile()) {
                    java.io.PrintWriter writer = new java.io.PrintWriter(file);
                    if (t != null) {
                        try {
                            writer.append('\n').append((java.lang.CharSequence) t.toString());
                        } finally {
                        }
                    }
                    writer.append('\n').append((java.lang.CharSequence) android.os.Debug.getCallers(10));
                    if (storage != null) {
                        writer.append((java.lang.CharSequence) ("\nfiles: " + java.util.Arrays.toString(storage.listFiles())));
                    } else {
                        writer.append((java.lang.CharSequence) "\nfiles: none");
                    }
                    writer.close();
                }
            } catch (java.io.IOException e) {
            }
        }
    }
}
