package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class BiometricSchedulerOperation {
    private static final int CANCEL_WATCHDOG_DELAY_MS = 3000;
    protected static final int STATE_FINISHED = 5;
    protected static final int STATE_STARTED = 2;
    protected static final int STATE_STARTED_CANCELING = 3;
    protected static final int STATE_WAITING_FOR_COOKIE = 4;
    protected static final int STATE_WAITING_IN_QUEUE = 0;
    protected static final int STATE_WAITING_IN_QUEUE_CANCELING = 1;
    protected static final java.lang.String TAG = "BiometricSchedulerOperation";
    final java.lang.Runnable mCancelWatchdog;
    private final com.android.server.biometrics.sensors.ClientMonitorCallback mClientCallback;
    private final com.android.server.biometrics.sensors.BaseClientMonitor mClientMonitor;
    private final java.util.function.BooleanSupplier mIsDebuggable;
    private com.android.server.biometrics.sensors.ClientMonitorCallback mOnStartCallback;
    private int mState;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    protected @interface OperationState {
    }

    BiometricSchedulerOperation(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        this(clientMonitor, callback, 0);
    }

    BiometricSchedulerOperation(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, com.android.server.biometrics.sensors.ClientMonitorCallback callback, java.util.function.BooleanSupplier isDebuggable) {
        this(clientMonitor, callback, 0, isDebuggable);
    }

    protected BiometricSchedulerOperation(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, com.android.server.biometrics.sensors.ClientMonitorCallback callback, int state) {
        this(clientMonitor, callback, state, new java.util.function.BooleanSupplier() { // from class: com.android.server.biometrics.sensors.BiometricSchedulerOperation$$ExternalSyntheticLambda1
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return android.os.Build.isDebuggable();
            }
        });
    }

    private BiometricSchedulerOperation(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, com.android.server.biometrics.sensors.ClientMonitorCallback callback, int state, java.util.function.BooleanSupplier isDebuggable) {
        this.mClientMonitor = clientMonitor;
        this.mClientCallback = callback;
        this.mState = state;
        this.mIsDebuggable = isDebuggable;
        this.mCancelWatchdog = new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricSchedulerOperation$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (!isFinished()) {
            android.util.Slog.e(TAG, "[Watchdog Triggered]: " + this);
            try {
                this.mClientMonitor.getListener().onError(this.mClientMonitor.getSensorId(), this.mClientMonitor.getCookie(), 5, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception when trying to send error in cancel watchdog.");
            }
            getWrappedCallback(this.mOnStartCallback).onClientFinished(this.mClientMonitor, false);
        }
    }

    public int isReadyToStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        if (this.mState == 4 || this.mState == 0) {
            int cookie = this.mClientMonitor.getCookie();
            if (cookie != 0) {
                this.mState = 4;
                this.mClientMonitor.waitForCookie(getWrappedCallback(callback));
            }
            return cookie;
        }
        return 0;
    }

    public boolean start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        if (errorWhenNoneOf("start", 0, 4, 1)) {
            return false;
        }
        if (this.mClientMonitor.getCookie() != 0) {
            if (!this.mIsDebuggable.getAsBoolean()) {
                android.util.Slog.e(TAG, "operation requires cookie");
            } else {
                throw new java.lang.IllegalStateException("operation requires cookie");
            }
        }
        return doStart(callback);
    }

    public boolean startWithCookie(com.android.server.biometrics.sensors.ClientMonitorCallback callback, int cookie) {
        if (this.mClientMonitor.getCookie() != cookie) {
            android.util.Slog.e(TAG, "Mismatched cookie for operation: " + this + ", received: " + cookie);
            return false;
        }
        if (errorWhenNoneOf("start", 0, 4, 1)) {
            return false;
        }
        return doStart(callback);
    }

    private boolean doStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        this.mOnStartCallback = callback;
        com.android.server.biometrics.sensors.ClientMonitorCallback cb = getWrappedCallback(callback);
        if (this.mState == 1) {
            android.util.Slog.d(TAG, "Operation marked for cancellation, cancelling now: " + this);
            cb.onClientFinished(this.mClientMonitor, true);
            if (this.mClientMonitor instanceof com.android.server.biometrics.sensors.ErrorConsumer) {
                com.android.server.biometrics.sensors.ErrorConsumer errorConsumer = (com.android.server.biometrics.sensors.ErrorConsumer) this.mClientMonitor;
                errorConsumer.onError(5, 0);
            } else {
                android.util.Slog.w(TAG, "monitor cancelled but does not implement ErrorConsumer");
            }
            return false;
        }
        if (isUnstartableHalOperation()) {
            android.util.Slog.v(TAG, "unable to start: " + this);
            ((com.android.server.biometrics.sensors.HalClientMonitor) this.mClientMonitor).unableToStart();
            cb.onClientFinished(this.mClientMonitor, false);
            return false;
        }
        if (this.mState == 5) {
            android.util.Slog.w(TAG, "Operation is in the wrong state: " + this.mState + ", expected STATE_WAITING_FOR_COOKIE");
            return false;
        }
        this.mState = 2;
        this.mClientMonitor.start(cb);
        android.util.Slog.v(TAG, "started: " + this);
        return true;
    }

    public void abort() {
        if (errorWhenNoneOf("abort", 0, 4, 1)) {
            return;
        }
        if (isHalOperation()) {
            ((com.android.server.biometrics.sensors.HalClientMonitor) this.mClientMonitor).unableToStart();
        }
        getWrappedCallback().onClientFinished(this.mClientMonitor, false);
        android.util.Slog.v(TAG, "Aborted: " + this);
    }

    public boolean markCanceling() {
        if (this.mState == 0 && isInterruptable()) {
            this.mState = 1;
            return true;
        }
        return false;
    }

    void markCancelingForWatchdog() {
        this.mState = 1;
    }

    public void cancel(android.os.Handler handler, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        if (errorWhenOneOf("cancel", 5)) {
            return;
        }
        int currentState = this.mState;
        if (currentState == 3) {
            android.util.Slog.w(TAG, "Cannot cancel - already invoked for operation: " + this);
            return;
        }
        this.mState = 3;
        if (currentState == 0 || currentState == 1 || currentState == 4) {
            android.util.Slog.d(TAG, "[Cancelling] Current client (without start): " + this.mClientMonitor);
            this.mClientMonitor.cancelWithoutStarting(getWrappedCallback(callback));
        } else {
            android.util.Slog.d(TAG, "[Cancelling] Current client: " + this.mClientMonitor);
            this.mClientMonitor.cancel();
        }
        handler.postDelayed(this.mCancelWatchdog, 3000L);
    }

    private com.android.server.biometrics.sensors.ClientMonitorCallback getWrappedCallback() {
        return getWrappedCallback(null);
    }

    private com.android.server.biometrics.sensors.ClientMonitorCallback getWrappedCallback(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        com.android.server.biometrics.sensors.ClientMonitorCallback destroyCallback = new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.BiometricSchedulerOperation.1
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                android.util.Slog.d(com.android.server.biometrics.sensors.BiometricSchedulerOperation.TAG, "[Finished / destroy]: " + clientMonitor);
                com.android.server.biometrics.sensors.BiometricSchedulerOperation.this.mClientMonitor.destroy();
                com.android.server.biometrics.sensors.BiometricSchedulerOperation.this.mState = 5;
            }
        };
        return new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(destroyCallback, callback, this.mClientCallback);
    }

    public int getSensorId() {
        return this.mClientMonitor.getSensorId();
    }

    public int getProtoEnum() {
        return this.mClientMonitor.getProtoEnum();
    }

    public int getTargetUserId() {
        return this.mClientMonitor.getTargetUserId();
    }

    public boolean isFor(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
        return this.mClientMonitor == clientMonitor;
    }

    public boolean isInterruptable() {
        return this.mClientMonitor.isInterruptable();
    }

    private boolean isHalOperation() {
        return this.mClientMonitor instanceof com.android.server.biometrics.sensors.HalClientMonitor;
    }

    private boolean isUnstartableHalOperation() {
        if (isHalOperation()) {
            com.android.server.biometrics.sensors.HalClientMonitor<?> client = (com.android.server.biometrics.sensors.HalClientMonitor) this.mClientMonitor;
            if (client.getFreshDaemon() == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isEnrollOperation() {
        return this.mClientMonitor instanceof com.android.server.biometrics.sensors.EnrollClient;
    }

    public boolean isAuthenticateOperation() {
        return this.mClientMonitor instanceof com.android.server.biometrics.sensors.AuthenticationClient;
    }

    public boolean isAuthenticationOrDetectionOperation() {
        boolean isAuthentication = this.mClientMonitor instanceof com.android.server.biometrics.sensors.AuthenticationConsumer;
        boolean isDetection = this.mClientMonitor instanceof com.android.server.biometrics.sensors.DetectionConsumer;
        return isAuthentication || isDetection;
    }

    public boolean isStartUserOperation() {
        return this.mClientMonitor instanceof com.android.server.biometrics.sensors.StartUserClient;
    }

    public boolean isAcquisitionOperation() {
        return this.mClientMonitor instanceof com.android.server.biometrics.sensors.AcquisitionClient;
    }

    public boolean isMatchingRequestId(long requestId) {
        return !this.mClientMonitor.hasRequestId() || this.mClientMonitor.getRequestId() == requestId;
    }

    public boolean isMatchingToken(android.os.IBinder token) {
        return this.mClientMonitor.getToken() == token;
    }

    public boolean isStarted() {
        return this.mState == 2;
    }

    public boolean isCanceling() {
        return this.mState == 3;
    }

    public boolean isFinished() {
        return this.mState == 5;
    }

    public boolean isMarkedCanceling() {
        return this.mState == 1;
    }

    public boolean isWaitingForCookie() {
        return this.mState == 4;
    }

    @java.lang.Deprecated
    public com.android.server.biometrics.sensors.BaseClientMonitor getClientMonitor() {
        return this.mClientMonitor;
    }

    private boolean errorWhenOneOf(java.lang.String op, int... states) {
        boolean isError = com.android.internal.util.ArrayUtils.contains(states, this.mState);
        if (isError) {
            java.lang.String err = op + ": mState must not be " + this.mState;
            if (this.mIsDebuggable.getAsBoolean()) {
                throw new java.lang.IllegalStateException(err);
            }
            android.util.Slog.e(TAG, err);
        }
        return isError;
    }

    private boolean errorWhenNoneOf(java.lang.String op, int... states) {
        boolean isError = !com.android.internal.util.ArrayUtils.contains(states, this.mState);
        if (isError) {
            java.lang.String err = op + ": mState=" + this.mState + " must be one of " + java.util.Arrays.toString(states);
            if (this.mIsDebuggable.getAsBoolean() && this.mState != 5) {
                throw new java.lang.IllegalStateException(err);
            }
            android.util.Slog.e(TAG, err);
        }
        return isError;
    }

    public java.lang.String toString() {
        return this.mClientMonitor + ", State: " + this.mState;
    }
}
