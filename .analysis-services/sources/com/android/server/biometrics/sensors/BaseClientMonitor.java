package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseClientMonitor implements android.os.IBinder.DeathRecipient {
    protected static final boolean DEBUG = true;
    private static final java.lang.String TAG = "BaseClientMonitor";
    private static int sCount = 0;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final android.content.Context mContext;
    private final int mCookie;
    private com.android.server.biometrics.sensors.ClientMonitorCallbackConverter mListener;
    private final com.android.server.biometrics.log.BiometricLogger mLogger;
    private final java.lang.String mOwner;
    private long mRequestId;
    private final int mSensorId;
    private final int mSequentialId;
    private final int mTargetUserId;
    private android.os.IBinder mToken;
    public com.android.server.biometrics.sensors.IBaseClientMonitorExt mBaseClientMonitorExt = (com.android.server.biometrics.sensors.IBaseClientMonitorExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IBaseClientMonitorExt.class).base(this).create();
    private boolean mAlreadyDone = false;
    protected com.android.server.biometrics.sensors.ClientMonitorCallback mCallback = new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.BaseClientMonitor.1
        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
            android.util.Slog.e(com.android.server.biometrics.sensors.BaseClientMonitor.TAG, "mCallback onClientStarted: called before set (should not happen)");
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
            android.util.Slog.e(com.android.server.biometrics.sensors.BaseClientMonitor.TAG, "mCallback onClientFinished: called before set (should not happen)");
        }
    };

    public abstract int getProtoEnum();

    public BaseClientMonitor(android.content.Context context, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int cookie, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        int i = sCount;
        sCount = i + 1;
        this.mSequentialId = i;
        this.mContext = context;
        this.mToken = token;
        this.mRequestId = -1L;
        this.mListener = listener == null ? new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter((android.hardware.biometrics.IBiometricSensorReceiver) new android.hardware.biometrics.IBiometricSensorReceiver.Default()) : listener;
        this.mTargetUserId = userId;
        this.mOwner = owner;
        this.mCookie = cookie;
        this.mSensorId = sensorId;
        this.mLogger = logger;
        this.mBiometricContext = biometricContext;
        if (token != null) {
            try {
                token.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "caught remote exception in linkToDeath: ", e);
            }
        }
    }

    public boolean interruptsPrecedingClients() {
        return false;
    }

    public void waitForCookie(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        this.mCallback = callback;
    }

    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        this.mCallback = wrapCallbackForStart(callback);
        this.mCallback.onClientStarted(this);
    }

    protected com.android.server.biometrics.sensors.ClientMonitorCallback wrapCallbackForStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        return callback;
    }

    public void setIsAlreadyDone(boolean IsAlreadyDone) {
        this.mAlreadyDone = IsAlreadyDone;
    }

    public void destroy() {
        this.mAlreadyDone = true;
        if (this.mToken != null) {
            try {
                this.mToken.unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Slog.e(TAG, "destroy(): " + this + ":", new java.lang.Exception("here"));
            }
            this.mToken = null;
        }
    }

    void markAlreadyDone() {
        android.util.Slog.d(TAG, "marking operation as done: " + this);
        this.mAlreadyDone = true;
    }

    public boolean isAlreadyDone() {
        return this.mAlreadyDone;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        binderDiedInternal(true);
    }

    void binderDiedInternal(boolean clearListener) {
        android.util.Slog.e(TAG, "Binder died, operation: " + this);
        if (this.mAlreadyDone) {
            android.util.Slog.w(TAG, "Binder died but client is finished, ignoring");
            return;
        }
        if (isInterruptable()) {
            android.util.Slog.e(TAG, "Binder died, cancelling client");
            cancel();
        }
        this.mToken = null;
        if (clearListener) {
            this.mListener = new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter((android.hardware.biometrics.IBiometricSensorReceiver) new android.hardware.biometrics.IBiometricSensorReceiver.Default());
        }
    }

    protected boolean isCryptoOperation() {
        return false;
    }

    protected com.android.server.biometrics.log.BiometricContext getBiometricContext() {
        return this.mBiometricContext;
    }

    public com.android.server.biometrics.log.BiometricLogger getLogger() {
        return this.mLogger;
    }

    public final android.content.Context getContext() {
        return this.mContext;
    }

    public final java.lang.String getOwnerString() {
        return this.mOwner;
    }

    public com.android.server.biometrics.sensors.ClientMonitorCallbackConverter getListener() {
        return this.mListener;
    }

    public int getTargetUserId() {
        return this.mTargetUserId;
    }

    public final android.os.IBinder getToken() {
        return this.mToken;
    }

    public int getSensorId() {
        return this.mSensorId;
    }

    public int getCookie() {
        return this.mCookie;
    }

    public long getRequestId() {
        return this.mRequestId;
    }

    public boolean hasRequestId() {
        return this.mRequestId > 0;
    }

    protected final void setRequestId(long id) {
        if (id <= 0) {
            throw new java.lang.IllegalArgumentException("request id must be positive");
        }
        this.mRequestId = id;
    }

    public com.android.server.biometrics.sensors.ClientMonitorCallback getCallback() {
        return this.mCallback;
    }

    public java.lang.String toString() {
        return "{[" + this.mSequentialId + "] " + getClass().getName() + ", proto=" + getProtoEnum() + ", owner=" + getOwnerString() + ", cookie=" + getCookie() + ", requestId=" + getRequestId() + ", userId=" + getTargetUserId() + "}";
    }

    public void cancel() {
        cancelWithoutStarting(this.mCallback);
    }

    public void cancelWithoutStarting(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        android.util.Slog.d(TAG, "cancelWithoutStarting: " + this);
        try {
            com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener = getListener();
            listener.onError(getSensorId(), getCookie(), 5, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to invoke sendError", e);
        }
        callback.onClientFinished(this, true);
    }

    public boolean isInterruptable() {
        return false;
    }
}
