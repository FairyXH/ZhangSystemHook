package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class ManagedApplicationService {
    private static final int MAX_RETRY_COUNT = 4;
    private static final long MAX_RETRY_DURATION_MS = 16000;
    private static final long MIN_RETRY_DURATION_MS = 2000;
    public static final int RETRY_BEST_EFFORT = 3;
    public static final int RETRY_FOREVER = 1;
    public static final int RETRY_NEVER = 2;
    private static final long RETRY_RESET_TIME_MS = 64000;
    private android.os.IInterface mBoundInterface;
    private final com.android.server.utils.ManagedApplicationService.BinderChecker mChecker;
    private final int mClientLabel;
    private final android.content.ComponentName mComponent;
    private android.content.ServiceConnection mConnection;
    private final android.content.Context mContext;
    private final com.android.server.utils.ManagedApplicationService.EventCallback mEventCb;
    private final android.os.Handler mHandler;
    private final boolean mIsImportant;
    private long mLastRetryTimeMs;
    private com.android.server.utils.ManagedApplicationService.PendingEvent mPendingEvent;
    private int mRetryCount;
    private final int mRetryType;
    private boolean mRetrying;
    private final java.lang.String mSettingsAction;
    private final int mUserId;
    private final java.lang.String TAG = getClass().getSimpleName();
    private final java.lang.Runnable mRetryRunnable = new java.lang.Runnable() { // from class: com.android.server.utils.ManagedApplicationService$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.doRetry();
        }
    };
    private final java.lang.Object mLock = new java.lang.Object();
    private long mNextRetryDurationMs = MIN_RETRY_DURATION_MS;

    public interface BinderChecker {
        android.os.IInterface asInterface(android.os.IBinder iBinder);

        boolean checkType(android.os.IInterface iInterface);
    }

    public interface EventCallback {
        void onServiceEvent(com.android.server.utils.ManagedApplicationService.LogEvent logEvent);
    }

    public interface LogFormattable {
        java.lang.String toLogString(java.text.SimpleDateFormat simpleDateFormat);
    }

    public interface PendingEvent {
        void runEvent(android.os.IInterface iInterface) throws android.os.RemoteException;
    }

    public static class LogEvent implements com.android.server.utils.ManagedApplicationService.LogFormattable {
        public static final int EVENT_BINDING_DIED = 3;
        public static final int EVENT_CONNECTED = 1;
        public static final int EVENT_DISCONNECTED = 2;
        public static final int EVENT_STOPPED_PERMANENTLY = 4;
        public final android.content.ComponentName component;
        public final int event;
        public final long timestamp;

        public LogEvent(long timestamp, android.content.ComponentName component, int event) {
            this.timestamp = timestamp;
            this.component = component;
            this.event = event;
        }

        @Override // com.android.server.utils.ManagedApplicationService.LogFormattable
        public java.lang.String toLogString(java.text.SimpleDateFormat dateFormat) {
            return dateFormat.format(new java.util.Date(this.timestamp)) + "   " + eventToString(this.event) + " Managed Service: " + (this.component == null ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : this.component.flattenToString());
        }

        public static java.lang.String eventToString(int event) {
            switch (event) {
                case 1:
                    return "Connected";
                case 2:
                    return "Disconnected";
                case 3:
                    return "Binding Died For";
                case 4:
                    return "Permanently Stopped";
                default:
                    return "Unknown Event Occurred";
            }
        }
    }

    private ManagedApplicationService(android.content.Context context, android.content.ComponentName component, int userId, int clientLabel, java.lang.String settingsAction, com.android.server.utils.ManagedApplicationService.BinderChecker binderChecker, boolean isImportant, int retryType, android.os.Handler handler, com.android.server.utils.ManagedApplicationService.EventCallback eventCallback) {
        this.mContext = context;
        this.mComponent = component;
        this.mUserId = userId;
        this.mClientLabel = clientLabel;
        this.mSettingsAction = settingsAction;
        this.mChecker = binderChecker;
        this.mIsImportant = isImportant;
        this.mRetryType = retryType;
        this.mHandler = handler;
        this.mEventCb = eventCallback;
    }

    public static com.android.server.utils.ManagedApplicationService build(android.content.Context context, android.content.ComponentName component, int userId, int clientLabel, java.lang.String settingsAction, com.android.server.utils.ManagedApplicationService.BinderChecker binderChecker, boolean isImportant, int retryType, android.os.Handler handler, com.android.server.utils.ManagedApplicationService.EventCallback eventCallback) {
        return new com.android.server.utils.ManagedApplicationService(context, component, userId, clientLabel, settingsAction, binderChecker, isImportant, retryType, handler, eventCallback);
    }

    public int getUserId() {
        return this.mUserId;
    }

    public android.content.ComponentName getComponent() {
        return this.mComponent;
    }

    public boolean disconnectIfNotMatching(android.content.ComponentName componentName, int userId) {
        if (matches(componentName, userId)) {
            return false;
        }
        disconnect();
        return true;
    }

    public void sendEvent(com.android.server.utils.ManagedApplicationService.PendingEvent event) {
        android.os.IInterface iface;
        synchronized (this.mLock) {
            iface = this.mBoundInterface;
            if (iface == null) {
                this.mPendingEvent = event;
            }
        }
        if (iface != null) {
            try {
                event.runEvent(iface);
            } catch (android.os.RemoteException | java.lang.RuntimeException ex) {
                android.util.Slog.e(this.TAG, "Received exception from user service: ", ex);
            }
        }
    }

    public void disconnect() {
        synchronized (this.mLock) {
            if (this.mConnection == null) {
                return;
            }
            this.mContext.unbindService(this.mConnection);
            this.mConnection = null;
            this.mBoundInterface = null;
        }
    }

    public void connect() {
        synchronized (this.mLock) {
            if (this.mConnection != null) {
                return;
            }
            android.content.Intent intent = new android.content.Intent().setComponent(this.mComponent);
            if (this.mClientLabel != 0) {
                intent.putExtra("android.intent.extra.client_label", this.mClientLabel);
            }
            if (this.mSettingsAction != null) {
                intent.putExtra("android.intent.extra.client_intent", android.app.PendingIntent.getActivity(this.mContext, 0, new android.content.Intent(this.mSettingsAction), 67108864));
            }
            this.mConnection = new com.android.server.utils.ManagedApplicationService.AnonymousClass1();
            int flags = android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN;
            if (this.mIsImportant) {
                flags = 67108865 | 64;
            }
            try {
                if (!this.mContext.bindServiceAsUser(intent, this.mConnection, flags, new android.os.UserHandle(this.mUserId))) {
                    android.util.Slog.w(this.TAG, "Unable to bind service: " + intent);
                    startRetriesLocked();
                }
            } catch (java.lang.SecurityException e) {
                android.util.Slog.w(this.TAG, "Unable to bind service: " + intent, e);
                startRetriesLocked();
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.utils.ManagedApplicationService$1, reason: invalid class name */
    class AnonymousClass1 implements android.content.ServiceConnection {
        AnonymousClass1() {
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName componentName) {
            final long timestamp = java.lang.System.currentTimeMillis();
            android.util.Slog.w(com.android.server.utils.ManagedApplicationService.this.TAG, "Service binding died: " + componentName);
            synchronized (com.android.server.utils.ManagedApplicationService.this.mLock) {
                if (com.android.server.utils.ManagedApplicationService.this.mConnection != this) {
                    return;
                }
                com.android.server.utils.ManagedApplicationService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.utils.ManagedApplicationService$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onBindingDied$0(timestamp);
                    }
                });
                com.android.server.utils.ManagedApplicationService.this.mBoundInterface = null;
                com.android.server.utils.ManagedApplicationService.this.startRetriesLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindingDied$0(long timestamp) {
            com.android.server.utils.ManagedApplicationService.this.mEventCb.onServiceEvent(new com.android.server.utils.ManagedApplicationService.LogEvent(timestamp, com.android.server.utils.ManagedApplicationService.this.mComponent, 3));
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder iBinder) {
            final long timestamp = java.lang.System.currentTimeMillis();
            android.util.Slog.i(com.android.server.utils.ManagedApplicationService.this.TAG, "Service connected: " + componentName);
            android.os.IInterface iface = null;
            com.android.server.utils.ManagedApplicationService.PendingEvent pendingEvent = null;
            synchronized (com.android.server.utils.ManagedApplicationService.this.mLock) {
                if (com.android.server.utils.ManagedApplicationService.this.mConnection != this) {
                    return;
                }
                com.android.server.utils.ManagedApplicationService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.utils.ManagedApplicationService$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceConnected$1(timestamp);
                    }
                });
                com.android.server.utils.ManagedApplicationService.this.stopRetriesLocked();
                com.android.server.utils.ManagedApplicationService.this.mBoundInterface = null;
                if (com.android.server.utils.ManagedApplicationService.this.mChecker != null) {
                    com.android.server.utils.ManagedApplicationService.this.mBoundInterface = com.android.server.utils.ManagedApplicationService.this.mChecker.asInterface(iBinder);
                    if (!com.android.server.utils.ManagedApplicationService.this.mChecker.checkType(com.android.server.utils.ManagedApplicationService.this.mBoundInterface)) {
                        com.android.server.utils.ManagedApplicationService.this.mBoundInterface = null;
                        android.util.Slog.w(com.android.server.utils.ManagedApplicationService.this.TAG, "Invalid binder from " + componentName);
                        com.android.server.utils.ManagedApplicationService.this.startRetriesLocked();
                        return;
                    } else {
                        iface = com.android.server.utils.ManagedApplicationService.this.mBoundInterface;
                        pendingEvent = com.android.server.utils.ManagedApplicationService.this.mPendingEvent;
                        com.android.server.utils.ManagedApplicationService.this.mPendingEvent = null;
                    }
                }
                if (iface != null && pendingEvent != null) {
                    try {
                        pendingEvent.runEvent(iface);
                    } catch (android.os.RemoteException | java.lang.RuntimeException ex) {
                        android.util.Slog.e(com.android.server.utils.ManagedApplicationService.this.TAG, "Received exception from user service: ", ex);
                        com.android.server.utils.ManagedApplicationService.this.startRetriesLocked();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$1(long timestamp) {
            com.android.server.utils.ManagedApplicationService.this.mEventCb.onServiceEvent(new com.android.server.utils.ManagedApplicationService.LogEvent(timestamp, com.android.server.utils.ManagedApplicationService.this.mComponent, 1));
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName componentName) {
            final long timestamp = java.lang.System.currentTimeMillis();
            android.util.Slog.w(com.android.server.utils.ManagedApplicationService.this.TAG, "Service disconnected: " + componentName);
            synchronized (com.android.server.utils.ManagedApplicationService.this.mLock) {
                if (com.android.server.utils.ManagedApplicationService.this.mConnection != this) {
                    return;
                }
                com.android.server.utils.ManagedApplicationService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.utils.ManagedApplicationService$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceDisconnected$2(timestamp);
                    }
                });
                com.android.server.utils.ManagedApplicationService.this.mBoundInterface = null;
                com.android.server.utils.ManagedApplicationService.this.startRetriesLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$2(long timestamp) {
            com.android.server.utils.ManagedApplicationService.this.mEventCb.onServiceEvent(new com.android.server.utils.ManagedApplicationService.LogEvent(timestamp, com.android.server.utils.ManagedApplicationService.this.mComponent, 2));
        }
    }

    private boolean matches(android.content.ComponentName component, int userId) {
        return java.util.Objects.equals(this.mComponent, component) && this.mUserId == userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRetriesLocked() {
        if (checkAndDeliverServiceDiedCbLocked()) {
            disconnect();
        } else {
            if (this.mRetrying) {
                return;
            }
            this.mRetrying = true;
            queueRetryLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRetriesLocked() {
        this.mRetrying = false;
        this.mHandler.removeCallbacks(this.mRetryRunnable);
    }

    private void queueRetryLocked() {
        long now = android.os.SystemClock.uptimeMillis();
        if (now - this.mLastRetryTimeMs > RETRY_RESET_TIME_MS) {
            this.mNextRetryDurationMs = MIN_RETRY_DURATION_MS;
            this.mRetryCount = 0;
        }
        this.mLastRetryTimeMs = now;
        this.mHandler.postDelayed(this.mRetryRunnable, this.mNextRetryDurationMs);
        this.mNextRetryDurationMs = java.lang.Math.min(this.mNextRetryDurationMs * 2, MAX_RETRY_DURATION_MS);
        this.mRetryCount++;
    }

    private boolean checkAndDeliverServiceDiedCbLocked() {
        if (this.mRetryType == 2 || (this.mRetryType == 3 && this.mRetryCount >= 4)) {
            android.util.Slog.e(this.TAG, "Service " + this.mComponent + " has died too much, not retrying.");
            if (this.mEventCb != null) {
                final long timestamp = java.lang.System.currentTimeMillis();
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.utils.ManagedApplicationService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$checkAndDeliverServiceDiedCbLocked$0(timestamp);
                    }
                });
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAndDeliverServiceDiedCbLocked$0(long timestamp) {
        this.mEventCb.onServiceEvent(new com.android.server.utils.ManagedApplicationService.LogEvent(timestamp, this.mComponent, 4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRetry() {
        synchronized (this.mLock) {
            if (this.mConnection == null) {
                return;
            }
            if (this.mRetrying) {
                android.util.Slog.i(this.TAG, "Attempting to reconnect " + this.mComponent + "...");
                disconnect();
                if (checkAndDeliverServiceDiedCbLocked()) {
                    return;
                }
                queueRetryLocked();
                connect();
            }
        }
    }
}
