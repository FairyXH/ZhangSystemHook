package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class SecurityLogMonitor implements java.lang.Runnable {
    private static final int BUFFER_ENTRIES_CRITICAL_LEVEL = 9216;
    private static final int BUFFER_ENTRIES_MAXIMUM_LEVEL = 10240;
    static final int BUFFER_ENTRIES_NOTIFICATION_LEVEL = 1024;
    private static final boolean DEBUG = false;
    private static final int MAX_AUDIT_LOG_EVENTS = 10000;
    private static final java.lang.String TAG = "SecurityLogMonitor";
    private boolean mAuditLogEnabled;
    private int mEnabledUser;
    private final android.os.Handler mHandler;
    private long mLastForceNanos;
    private boolean mLegacyLogEnabled;
    private final com.android.server.devicepolicy.DevicePolicyManagerService mService;
    private static final long RATE_LIMIT_INTERVAL_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(2);
    private static final long BROADCAST_RETRY_INTERVAL_MS = java.util.concurrent.TimeUnit.MINUTES.toMillis(30);
    private static final long POLLING_INTERVAL_MS = java.util.concurrent.TimeUnit.MINUTES.toMillis(1);
    private static final long OVERLAP_NS = java.util.concurrent.TimeUnit.SECONDS.toNanos(3);
    private static final long FORCE_FETCH_THROTTLE_NS = java.util.concurrent.TimeUnit.SECONDS.toNanos(10);
    private static final long MAX_AUDIT_LOG_EVENT_AGE_NS = java.util.concurrent.TimeUnit.HOURS.toNanos(8);
    private final java.util.concurrent.locks.Lock mLock = new java.util.concurrent.locks.ReentrantLock();
    private java.lang.Thread mMonitorThread = null;
    private java.util.ArrayList<android.app.admin.SecurityLog.SecurityEvent> mPendingLogs = new java.util.ArrayList<>();
    private boolean mAllowedToRetrieve = false;
    private boolean mCriticalLevelLogged = false;
    private final java.util.ArrayList<android.app.admin.SecurityLog.SecurityEvent> mLastEvents = new java.util.ArrayList<>();
    private long mLastEventNanos = -1;
    private long mNextAllowedRetrievalTimeMillis = -1;
    private boolean mPaused = false;
    private final java.util.concurrent.Semaphore mForceSemaphore = new java.util.concurrent.Semaphore(0);
    private final android.util.SparseArray<android.app.admin.IAuditLogEventsCallback> mAuditLogCallbacks = new android.util.SparseArray<>();
    private final java.util.ArrayDeque<android.app.admin.SecurityLog.SecurityEvent> mAuditLogEventBuffer = new java.util.ArrayDeque<>();
    private long mId = 0;

    SecurityLogMonitor(com.android.server.devicepolicy.DevicePolicyManagerService service, android.os.Handler handler) {
        this.mLastForceNanos = 0L;
        this.mService = service;
        this.mLastForceNanos = java.lang.System.nanoTime();
        this.mHandler = handler;
    }

    void start(int enabledUser) {
        android.util.Slog.i(TAG, "Starting security logging for user " + enabledUser);
        this.mEnabledUser = enabledUser;
        this.mLock.lock();
        try {
            if (this.mMonitorThread == null) {
                resetLegacyBufferLocked();
                startMonitorThreadLocked();
            } else {
                android.util.Slog.i(TAG, "Security log monitor thread is already running");
            }
        } finally {
            this.mLock.unlock();
        }
    }

    void stop() {
        android.util.Slog.i(TAG, "Stopping security logging.");
        this.mLock.lock();
        try {
            if (this.mMonitorThread != null) {
                stopMonitorThreadLocked();
                resetLegacyBufferLocked();
            }
        } finally {
            this.mLock.unlock();
        }
    }

    void setLoggingParams(int enabledUser, boolean legacyLogEnabled, boolean auditLogEnabled) {
        com.android.server.utils.Slogf.i(TAG, "Setting logging params, user = %d -> %d, legacy: %b -> %b, audit %b -> %b", java.lang.Integer.valueOf(this.mEnabledUser), java.lang.Integer.valueOf(enabledUser), java.lang.Boolean.valueOf(this.mLegacyLogEnabled), java.lang.Boolean.valueOf(legacyLogEnabled), java.lang.Boolean.valueOf(this.mAuditLogEnabled), java.lang.Boolean.valueOf(auditLogEnabled));
        this.mLock.lock();
        try {
            this.mEnabledUser = enabledUser;
            if (this.mMonitorThread == null && (legacyLogEnabled || auditLogEnabled)) {
                startMonitorThreadLocked();
            } else if (this.mMonitorThread != null && !legacyLogEnabled && !auditLogEnabled) {
                stopMonitorThreadLocked();
            }
            if (this.mLegacyLogEnabled != legacyLogEnabled) {
                resetLegacyBufferLocked();
                this.mLegacyLogEnabled = legacyLogEnabled;
            }
            if (this.mAuditLogEnabled != auditLogEnabled) {
                resetAuditBufferLocked();
                this.mAuditLogEnabled = auditLogEnabled;
            }
        } finally {
            this.mLock.unlock();
        }
    }

    private void startMonitorThreadLocked() {
        this.mId = 0L;
        this.mPaused = false;
        this.mMonitorThread = new java.lang.Thread(this);
        this.mMonitorThread.start();
        android.app.admin.SecurityLog.writeEvent(210011, new java.lang.Object[0]);
        android.util.Slog.i(TAG, "Security log monitor thread started");
    }

    private void stopMonitorThreadLocked() {
        this.mMonitorThread.interrupt();
        try {
            this.mMonitorThread.join(java.util.concurrent.TimeUnit.SECONDS.toMillis(5L));
        } catch (java.lang.InterruptedException e) {
            android.util.Log.e(TAG, "Interrupted while waiting for thread to stop", e);
        }
        this.mMonitorThread = null;
        android.app.admin.SecurityLog.writeEvent(210012, new java.lang.Object[0]);
    }

    private void resetLegacyBufferLocked() {
        this.mPendingLogs = new java.util.ArrayList<>();
        this.mCriticalLevelLogged = false;
        this.mAllowedToRetrieve = false;
        this.mNextAllowedRetrievalTimeMillis = -1L;
        android.util.Slog.i(TAG, "Legacy buffer reset.");
    }

    private void resetAuditBufferLocked() {
        this.mAuditLogEventBuffer.clear();
        this.mAuditLogCallbacks.clear();
    }

    void pause() {
        android.util.Slog.i(TAG, "Paused.");
        this.mLock.lock();
        this.mPaused = true;
        this.mAllowedToRetrieve = false;
        this.mLock.unlock();
    }

    void resume() {
        this.mLock.lock();
        try {
            if (!this.mPaused) {
                android.util.Log.d(TAG, "Attempted to resume, but logging is not paused.");
                return;
            }
            this.mPaused = false;
            this.mAllowedToRetrieve = false;
            this.mLock.unlock();
            android.util.Slog.i(TAG, "Resumed.");
            try {
                notifyDeviceOwnerOrProfileOwnerIfNeeded(false);
            } catch (java.lang.InterruptedException e) {
                android.util.Log.w(TAG, "Thread interrupted.", e);
            }
        } finally {
            this.mLock.unlock();
        }
    }

    void discardLogs() {
        this.mLock.lock();
        this.mAllowedToRetrieve = false;
        this.mPendingLogs = new java.util.ArrayList<>();
        this.mCriticalLevelLogged = false;
        this.mLock.unlock();
        android.util.Slog.i(TAG, "Discarded all logs.");
    }

    java.util.List<android.app.admin.SecurityLog.SecurityEvent> retrieveLogs() {
        this.mLock.lock();
        try {
            if (this.mAllowedToRetrieve) {
                this.mAllowedToRetrieve = false;
                this.mNextAllowedRetrievalTimeMillis = android.os.SystemClock.elapsedRealtime() + RATE_LIMIT_INTERVAL_MS;
                java.util.List<android.app.admin.SecurityLog.SecurityEvent> result = this.mPendingLogs;
                this.mPendingLogs = new java.util.ArrayList<>();
                this.mCriticalLevelLogged = false;
                return result;
            }
            this.mLock.unlock();
            return null;
        } finally {
            this.mLock.unlock();
        }
    }

    private void getNextBatch(java.util.ArrayList<android.app.admin.SecurityLog.SecurityEvent> newLogs) throws java.io.IOException {
        if (this.mLastEventNanos < 0) {
            android.app.admin.SecurityLog.readEvents(newLogs);
        } else {
            long startNanos = this.mLastEvents.isEmpty() ? this.mLastEventNanos : java.lang.Math.max(0L, this.mLastEventNanos - OVERLAP_NS);
            android.app.admin.SecurityLog.readEventsSince(startNanos, newLogs);
        }
        int i = 0;
        while (true) {
            if (i >= newLogs.size() - 1) {
                break;
            }
            if (newLogs.get(i).getTimeNanos() <= newLogs.get(i + 1).getTimeNanos()) {
                i++;
            } else {
                newLogs.sort(new java.util.Comparator() { // from class: com.android.server.devicepolicy.SecurityLogMonitor$$ExternalSyntheticLambda1
                    @Override // java.util.Comparator
                    public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                        return java.lang.Long.signum(((android.app.admin.SecurityLog.SecurityEvent) obj).getTimeNanos() - ((android.app.admin.SecurityLog.SecurityEvent) obj2).getTimeNanos());
                    }
                });
                break;
            }
        }
        int i2 = this.mEnabledUser;
        android.app.admin.SecurityLog.redactEvents(newLogs, i2);
    }

    private void saveLastEvents(java.util.ArrayList<android.app.admin.SecurityLog.SecurityEvent> newLogs) {
        this.mLastEvents.clear();
        if (newLogs.isEmpty()) {
            return;
        }
        this.mLastEventNanos = newLogs.get(newLogs.size() - 1).getTimeNanos();
        int pos = newLogs.size() - 2;
        while (pos >= 0 && this.mLastEventNanos - newLogs.get(pos).getTimeNanos() < OVERLAP_NS) {
            pos--;
        }
        this.mLastEvents.addAll(newLogs.subList(pos + 1, newLogs.size()));
    }

    private void mergeBatchLocked(java.util.ArrayList<android.app.admin.SecurityLog.SecurityEvent> newLogs) {
        java.util.List<android.app.admin.SecurityLog.SecurityEvent> dedupedLogs = new java.util.ArrayList<>();
        int curPos = 0;
        int lastPos = 0;
        while (lastPos < this.mLastEvents.size() && curPos < newLogs.size()) {
            android.app.admin.SecurityLog.SecurityEvent curEvent = newLogs.get(curPos);
            long currentNanos = curEvent.getTimeNanos();
            if (currentNanos > this.mLastEventNanos) {
                break;
            }
            android.app.admin.SecurityLog.SecurityEvent lastEvent = this.mLastEvents.get(lastPos);
            long lastNanos = lastEvent.getTimeNanos();
            if (lastNanos > currentNanos) {
                dedupedLogs.add(curEvent);
                curPos++;
            } else if (lastNanos < currentNanos) {
                lastPos++;
            } else {
                if (!lastEvent.eventEquals(curEvent)) {
                    dedupedLogs.add(curEvent);
                }
                lastPos++;
                curPos++;
            }
        }
        dedupedLogs.addAll(newLogs.subList(curPos, newLogs.size()));
        for (android.app.admin.SecurityLog.SecurityEvent event : dedupedLogs) {
            assignLogId(event);
        }
        if (!android.app.admin.flags.Flags.securityLogV2Enabled() || this.mLegacyLogEnabled) {
            addToLegacyBufferLocked(dedupedLogs);
        }
        if (android.app.admin.flags.Flags.securityLogV2Enabled() && this.mAuditLogEnabled) {
            addAuditLogEventsLocked(dedupedLogs);
        }
    }

    private void addToLegacyBufferLocked(java.util.List<android.app.admin.SecurityLog.SecurityEvent> dedupedLogs) {
        this.mPendingLogs.addAll(dedupedLogs);
        checkCriticalLevel();
        if (this.mPendingLogs.size() > BUFFER_ENTRIES_MAXIMUM_LEVEL) {
            this.mPendingLogs = new java.util.ArrayList<>(this.mPendingLogs.subList(this.mPendingLogs.size() - 5120, this.mPendingLogs.size()));
            this.mCriticalLevelLogged = false;
            android.util.Slog.i(TAG, "Pending logs buffer full. Discarding old logs.");
        }
    }

    private void checkCriticalLevel() {
        if (android.app.admin.SecurityLog.isLoggingEnabled() && this.mPendingLogs.size() >= BUFFER_ENTRIES_CRITICAL_LEVEL && !this.mCriticalLevelLogged) {
            this.mCriticalLevelLogged = true;
            android.app.admin.SecurityLog.writeEvent(210015, new java.lang.Object[0]);
        }
    }

    private void assignLogId(android.app.admin.SecurityLog.SecurityEvent event) {
        event.setId(this.mId);
        if (this.mId == Long.MAX_VALUE) {
            android.util.Slog.i(TAG, "Reached maximum id value; wrapping around.");
            this.mId = 0L;
        } else {
            this.mId++;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean force;
        android.os.Process.setThreadPriority(10);
        java.util.ArrayList<android.app.admin.SecurityLog.SecurityEvent> newLogs = new java.util.ArrayList<>();
        while (!java.lang.Thread.currentThread().isInterrupted()) {
            try {
                force = this.mForceSemaphore.tryAcquire(POLLING_INTERVAL_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
                getNextBatch(newLogs);
                this.mLock.lockInterruptibly();
                try {
                    mergeBatchLocked(newLogs);
                    this.mLock.unlock();
                    saveLastEvents(newLogs);
                    newLogs.clear();
                } catch (java.lang.Throwable th) {
                    this.mLock.unlock();
                    throw th;
                }
            } catch (java.io.IOException e) {
                android.util.Log.e(TAG, "Failed to read security log", e);
            } catch (java.lang.InterruptedException e2) {
                android.util.Log.i(TAG, "Thread interrupted, exiting.", e2);
            }
            if (!android.app.admin.flags.Flags.securityLogV2Enabled() || this.mLegacyLogEnabled) {
                notifyDeviceOwnerOrProfileOwnerIfNeeded(force);
            }
        }
        this.mLastEvents.clear();
        if (this.mLastEventNanos != -1) {
            this.mLastEventNanos++;
        }
        android.util.Slog.i(TAG, "MonitorThread exit.");
    }

    private void notifyDeviceOwnerOrProfileOwnerIfNeeded(boolean force) throws java.lang.InterruptedException {
        boolean allowRetrievalAndNotifyDOOrPO = false;
        this.mLock.lockInterruptibly();
        try {
            if (this.mPaused) {
                return;
            }
            int logSize = this.mPendingLogs.size();
            if ((logSize >= 1024 || (force && logSize > 0)) && !this.mAllowedToRetrieve) {
                allowRetrievalAndNotifyDOOrPO = true;
            }
            if (logSize > 0 && android.os.SystemClock.elapsedRealtime() >= this.mNextAllowedRetrievalTimeMillis) {
                allowRetrievalAndNotifyDOOrPO = true;
            }
            if (allowRetrievalAndNotifyDOOrPO) {
                this.mAllowedToRetrieve = true;
                this.mNextAllowedRetrievalTimeMillis = android.os.SystemClock.elapsedRealtime() + BROADCAST_RETRY_INTERVAL_MS;
            }
            if (allowRetrievalAndNotifyDOOrPO) {
                android.util.Slog.i(TAG, "notify DO or PO");
                this.mService.sendDeviceOwnerOrProfileOwnerCommand("android.app.action.SECURITY_LOGS_AVAILABLE", null, this.mEnabledUser);
            }
        } finally {
            this.mLock.unlock();
        }
    }

    public long forceLogs() {
        long nowNanos = java.lang.System.nanoTime();
        synchronized (this.mForceSemaphore) {
            long toWaitNanos = (this.mLastForceNanos + FORCE_FETCH_THROTTLE_NS) - nowNanos;
            if (toWaitNanos > 0) {
                return java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(toWaitNanos) + 1;
            }
            this.mLastForceNanos = nowNanos;
            if (this.mForceSemaphore.availablePermits() == 0) {
                this.mForceSemaphore.release();
            }
            return 0L;
        }
    }

    public void setAuditLogEventsCallback(int uid, android.app.admin.IAuditLogEventsCallback callback) {
        this.mLock.lock();
        try {
            if (callback == null) {
                this.mAuditLogCallbacks.remove(uid);
                com.android.server.utils.Slogf.i(TAG, "Cleared audit log callback for UID %d", java.lang.Integer.valueOf(uid));
                return;
            }
            java.util.List<android.app.admin.SecurityLog.SecurityEvent> events = new java.util.ArrayList<>(this.mAuditLogEventBuffer);
            scheduleSendAuditLogs(uid, callback, events);
            this.mAuditLogCallbacks.append(uid, callback);
            this.mLock.unlock();
            com.android.server.utils.Slogf.i(TAG, "Set audit log callback for UID %d", java.lang.Integer.valueOf(uid));
        } finally {
            this.mLock.unlock();
        }
    }

    private void addAuditLogEventsLocked(java.util.List<android.app.admin.SecurityLog.SecurityEvent> events) {
        if (this.mPaused) {
            return;
        }
        if (!events.isEmpty()) {
            for (int i = 0; i < this.mAuditLogCallbacks.size(); i++) {
                int uid = this.mAuditLogCallbacks.keyAt(i);
                scheduleSendAuditLogs(uid, this.mAuditLogCallbacks.valueAt(i), events);
            }
        }
        this.mAuditLogEventBuffer.addAll(events);
        trimAuditLogBufferLocked();
    }

    private void trimAuditLogBufferLocked() {
        long nowNanos = java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(java.lang.System.currentTimeMillis());
        java.util.Iterator<android.app.admin.SecurityLog.SecurityEvent> iterator = this.mAuditLogEventBuffer.iterator();
        while (iterator.hasNext()) {
            android.app.admin.SecurityLog.SecurityEvent event = iterator.next();
            if (this.mAuditLogEventBuffer.size() > 10000 || nowNanos - event.getTimeNanos() > MAX_AUDIT_LOG_EVENT_AGE_NS) {
                iterator.remove();
            } else {
                return;
            }
        }
    }

    private void scheduleSendAuditLogs(final int uid, final android.app.admin.IAuditLogEventsCallback callback, final java.util.List<android.app.admin.SecurityLog.SecurityEvent> events) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicepolicy.SecurityLogMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleSendAuditLogs$1(uid, callback, events);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendAuditLogs, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleSendAuditLogs$1(int uid, android.app.admin.IAuditLogEventsCallback callback, java.util.List<android.app.admin.SecurityLog.SecurityEvent> events) {
        try {
            events.size();
            callback.onNewAuditLogEvents(events);
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.e(TAG, e, "Failed to invoke audit log callback for UID %d", java.lang.Integer.valueOf(uid));
            removeAuditLogEventsCallbackIfDead(uid, callback);
        }
    }

    private void removeAuditLogEventsCallbackIfDead(int uid, android.app.admin.IAuditLogEventsCallback callback) {
        android.os.IBinder binder = callback.asBinder();
        if (binder.isBinderAlive()) {
            android.util.Slog.i(TAG, "Callback binder is still alive, not removing.");
            return;
        }
        this.mLock.lock();
        try {
            int index = this.mAuditLogCallbacks.indexOfKey(uid);
            if (index < 0) {
                com.android.server.utils.Slogf.i(TAG, "Callback not registered for UID %d, nothing to remove", java.lang.Integer.valueOf(uid));
                return;
            }
            android.os.IBinder storedBinder = this.mAuditLogCallbacks.valueAt(index).asBinder();
            if (!storedBinder.equals(binder)) {
                com.android.server.utils.Slogf.i(TAG, "Callback is already replaced for UID %d, not removing", java.lang.Integer.valueOf(uid));
            } else {
                com.android.server.utils.Slogf.i(TAG, "Removing callback for UID %d", java.lang.Integer.valueOf(uid));
                this.mAuditLogCallbacks.removeAt(index);
            }
        } finally {
            this.mLock.unlock();
        }
    }
}
