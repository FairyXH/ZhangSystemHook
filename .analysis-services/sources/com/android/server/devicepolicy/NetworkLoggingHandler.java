package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class NetworkLoggingHandler extends android.os.Handler {
    private static final long BATCH_FINALIZATION_TIMEOUT_ALARM_INTERVAL_MS = 1800000;
    private static final long BATCH_FINALIZATION_TIMEOUT_MS = 5400000;
    static final int LOG_NETWORK_EVENT_MSG = 1;
    private static final int MAX_BATCHES = 5;
    private static final int MAX_EVENTS_PER_BATCH = 1200;
    static final java.lang.String NETWORK_EVENT_KEY = "network_event";
    private static final java.lang.String NETWORK_LOGGING_TIMEOUT_ALARM_TAG = "NetworkLogging.batchTimeout";
    private static final long RETRIEVED_BATCH_DISCARD_DELAY_MS = 300000;
    private final android.app.AlarmManager mAlarmManager;
    private final android.app.AlarmManager.OnAlarmListener mBatchTimeoutAlarmListener;
    private final android.util.LongSparseArray<java.util.ArrayList<android.app.admin.NetworkEvent>> mBatches;
    private long mCurrentBatchToken;
    private final com.android.server.devicepolicy.DevicePolicyManagerService mDpm;
    private long mId;
    private long mLastFinalizationNanos;
    private long mLastRetrievedBatchToken;
    private java.util.ArrayList<android.app.admin.NetworkEvent> mNetworkEvents;
    private boolean mPaused;
    private int mTargetUserId;
    private static final java.lang.String TAG = com.android.server.devicepolicy.NetworkLoggingHandler.class.getSimpleName();
    private static final long FORCE_FETCH_THROTTLE_NS = java.util.concurrent.TimeUnit.SECONDS.toNanos(10);

    NetworkLoggingHandler(android.os.Looper looper, com.android.server.devicepolicy.DevicePolicyManagerService dpm, int targetUserId) {
        this(looper, dpm, 0L, targetUserId);
    }

    NetworkLoggingHandler(android.os.Looper looper, com.android.server.devicepolicy.DevicePolicyManagerService dpm, long id, int targetUserId) {
        super(looper);
        this.mLastFinalizationNanos = -1L;
        this.mBatchTimeoutAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.devicepolicy.NetworkLoggingHandler.1
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                android.os.Bundle notificationExtras;
                android.util.Slog.d(com.android.server.devicepolicy.NetworkLoggingHandler.TAG, "Received a batch finalization timeout alarm, finalizing " + com.android.server.devicepolicy.NetworkLoggingHandler.this.mNetworkEvents.size() + " pending events.");
                synchronized (com.android.server.devicepolicy.NetworkLoggingHandler.this) {
                    notificationExtras = com.android.server.devicepolicy.NetworkLoggingHandler.this.finalizeBatchAndBuildAdminMessageLocked();
                }
                if (notificationExtras != null) {
                    com.android.server.devicepolicy.NetworkLoggingHandler.this.notifyDeviceOwnerOrProfileOwner(notificationExtras);
                }
            }
        };
        this.mNetworkEvents = new java.util.ArrayList<>();
        this.mBatches = new android.util.LongSparseArray<>(5);
        this.mPaused = false;
        this.mDpm = dpm;
        this.mAlarmManager = this.mDpm.mInjector.getAlarmManager();
        this.mId = id;
        this.mTargetUserId = targetUserId;
    }

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                android.app.admin.NetworkEvent networkEvent = (android.app.admin.NetworkEvent) msg.getData().getParcelable(NETWORK_EVENT_KEY, android.app.admin.NetworkEvent.class);
                if (networkEvent != null) {
                    android.os.Bundle notificationExtras = null;
                    synchronized (this) {
                        this.mNetworkEvents.add(networkEvent);
                        if (this.mNetworkEvents.size() >= MAX_EVENTS_PER_BATCH) {
                            notificationExtras = finalizeBatchAndBuildAdminMessageLocked();
                        }
                        break;
                    }
                    if (notificationExtras != null) {
                        notifyDeviceOwnerOrProfileOwner(notificationExtras);
                        return;
                    }
                    return;
                }
                return;
            default:
                android.util.Slog.d(TAG, "NetworkLoggingHandler received an unknown of message.");
                return;
        }
    }

    void scheduleBatchFinalization() {
        long when = android.os.SystemClock.elapsedRealtime() + BATCH_FINALIZATION_TIMEOUT_MS;
        this.mAlarmManager.setWindow(2, when, 1800000L, NETWORK_LOGGING_TIMEOUT_ALARM_TAG, this.mBatchTimeoutAlarmListener, this);
        android.util.Slog.d(TAG, "Scheduled a new batch finalization alarm 5400000ms from now.");
    }

    long forceBatchFinalization() {
        synchronized (this) {
            long toWaitNanos = (this.mLastFinalizationNanos + FORCE_FETCH_THROTTLE_NS) - java.lang.System.nanoTime();
            if (toWaitNanos > 0) {
                return java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(toWaitNanos) + 1;
            }
            android.os.Bundle notificationExtras = finalizeBatchAndBuildAdminMessageLocked();
            if (notificationExtras != null) {
                notifyDeviceOwnerOrProfileOwner(notificationExtras);
            }
            return 0L;
        }
    }

    synchronized void pause() {
        android.util.Slog.d(TAG, "Paused network logging");
        this.mPaused = true;
    }

    void resume() {
        android.os.Bundle notificationExtras = null;
        synchronized (this) {
            if (!this.mPaused) {
                android.util.Slog.d(TAG, "Attempted to resume network logging, but logging is not paused.");
                return;
            }
            android.util.Slog.d(TAG, "Resumed network logging. Current batch=" + this.mCurrentBatchToken + ", LastRetrievedBatch=" + this.mLastRetrievedBatchToken);
            this.mPaused = false;
            if (this.mBatches.size() > 0 && this.mLastRetrievedBatchToken != this.mCurrentBatchToken) {
                scheduleBatchFinalization();
                notificationExtras = buildAdminMessageLocked();
            }
            if (notificationExtras != null) {
                notifyDeviceOwnerOrProfileOwner(notificationExtras);
            }
        }
    }

    synchronized void discardLogs() {
        this.mBatches.clear();
        this.mNetworkEvents = new java.util.ArrayList<>();
        android.util.Slog.d(TAG, "Discarded all network logs");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.Bundle finalizeBatchAndBuildAdminMessageLocked() {
        this.mLastFinalizationNanos = java.lang.System.nanoTime();
        android.os.Bundle notificationExtras = null;
        if (this.mNetworkEvents.size() > 0) {
            for (android.app.admin.NetworkEvent event : this.mNetworkEvents) {
                event.setId(this.mId);
                if (this.mId == Long.MAX_VALUE) {
                    android.util.Slog.i(TAG, "Reached maximum id value; wrapping around ." + this.mCurrentBatchToken);
                    this.mId = 0L;
                } else {
                    this.mId++;
                }
            }
            if (this.mBatches.size() >= 5) {
                this.mBatches.removeAt(0);
            }
            this.mCurrentBatchToken++;
            this.mBatches.append(this.mCurrentBatchToken, this.mNetworkEvents);
            this.mNetworkEvents = new java.util.ArrayList<>();
            if (!this.mPaused) {
                notificationExtras = buildAdminMessageLocked();
            }
        } else {
            android.util.Slog.d(TAG, "Was about to finalize the batch, but there were no events to send to the DPC, the batchToken of last available batch: " + this.mCurrentBatchToken);
        }
        scheduleBatchFinalization();
        return notificationExtras;
    }

    private android.os.Bundle buildAdminMessageLocked() {
        android.os.Bundle extras = new android.os.Bundle();
        int lastBatchSize = this.mBatches.valueAt(this.mBatches.size() - 1).size();
        extras.putLong("android.app.extra.EXTRA_NETWORK_LOGS_TOKEN", this.mCurrentBatchToken);
        extras.putInt("android.app.extra.EXTRA_NETWORK_LOGS_COUNT", lastBatchSize);
        return extras;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceOwnerOrProfileOwner(android.os.Bundle extras) {
        if (java.lang.Thread.holdsLock(this)) {
            android.util.Slog.wtfStack(TAG, "Shouldn't be called with NetworkLoggingHandler lock held");
        } else {
            android.util.Slog.d(TAG, "Sending network logging batch broadcast to device owner or profile owner, batchToken: " + extras.getLong("android.app.extra.EXTRA_NETWORK_LOGS_TOKEN", -1L));
            this.mDpm.sendDeviceOwnerOrProfileOwnerCommand("android.app.action.NETWORK_LOGS_AVAILABLE", extras, this.mTargetUserId);
        }
    }

    synchronized java.util.List<android.app.admin.NetworkEvent> retrieveFullLogBatch(final long batchToken) {
        int index = this.mBatches.indexOfKey(batchToken);
        if (index < 0) {
            return null;
        }
        postDelayed(new java.lang.Runnable() { // from class: com.android.server.devicepolicy.NetworkLoggingHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$retrieveFullLogBatch$0(batchToken);
            }
        }, 300000L);
        this.mLastRetrievedBatchToken = batchToken;
        return this.mBatches.valueAt(index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$retrieveFullLogBatch$0(long batchToken) {
        synchronized (this) {
            while (this.mBatches.size() > 0 && this.mBatches.keyAt(0) <= batchToken) {
                this.mBatches.removeAt(0);
            }
        }
    }
}
