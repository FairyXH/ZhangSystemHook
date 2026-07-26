package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class AcquisitionClient<T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> implements com.android.server.biometrics.sensors.ErrorConsumer {
    private static final java.lang.String TAG = "Biometrics/AcquisitionClient";
    private boolean mAlreadyCancelled;
    private final android.os.PowerManager mPowerManager;
    private boolean mShouldSendErrorToClient;
    protected final boolean mShouldVibrate;
    private static final android.os.VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(50);
    private static final android.os.VibrationEffect SUCCESS_VIBRATION_EFFECT = android.os.VibrationEffect.get(0);
    private static final android.os.VibrationEffect ERROR_VIBRATION_EFFECT = android.os.VibrationEffect.get(1);
    public static com.android.server.biometrics.sensors.tool.IBiometricsVibratorUtilsExt.IStaticExt sStaticExt = (com.android.server.biometrics.sensors.tool.IBiometricsVibratorUtilsExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.tool.IBiometricsVibratorUtilsExt.IStaticExt.class).create();

    protected abstract void stopHalOperation();

    public AcquisitionClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int cookie, int sensorId, boolean shouldVibrate, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, lazyDaemon, token, listener, userId, owner, cookie, sensorId, logger, biometricContext);
        this.mShouldSendErrorToClient = true;
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mShouldVibrate = shouldVibrate;
        sStaticExt.init(context);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
        try {
            getListener().onError(getSensorId(), getCookie(), 1, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send error", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        onErrorInternal(errorCode, vendorCode, true);
    }

    public void onUserCanceled() {
        android.util.Slog.d(TAG, "onUserCanceled");
        onErrorInternal(10, 0, false);
        stopHalOperation();
    }

    protected void onErrorInternal(int errorCode, int vendorCode, boolean finish) {
        android.util.Slog.d(TAG, "onErrorInternal code: " + errorCode + ", finish: " + finish);
        if (this.mShouldSendErrorToClient) {
            getLogger().logOnError(getContext(), getOperationContext(), errorCode, vendorCode, getTargetUserId());
            try {
                this.mShouldSendErrorToClient = false;
                getListener().onError(getSensorId(), getCookie(), errorCode, vendorCode);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to invoke sendError", e);
            }
        }
        if (finish) {
            if (this.mCallback == null) {
                android.util.Slog.e(TAG, "Callback is null, perhaps the client hasn't been started yet?");
            } else {
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void cancel() {
        if (this.mAlreadyCancelled) {
            android.util.Slog.w(TAG, "Cancel was already requested");
        } else {
            stopHalOperation();
            this.mAlreadyCancelled = true;
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void cancelWithoutStarting(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        android.util.Slog.d(TAG, "cancelWithoutStarting: " + this);
        try {
            getListener().onError(getSensorId(), getCookie(), 5, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to invoke sendError", e);
        }
        callback.onClientFinished(this, true);
    }

    public void onAcquired(int acquiredInfo, int vendorCode) {
        onAcquiredInternal(acquiredInfo, vendorCode, true);
    }

    protected final void onAcquiredInternal(int acquiredInfo, int vendorCode, boolean shouldSend) {
        getLogger().logOnAcquired(getContext(), getOperationContext(), acquiredInfo, vendorCode, getTargetUserId());
        android.util.Slog.v(TAG, "Acquired: " + acquiredInfo + " " + vendorCode + ", shouldSend: " + shouldSend);
        if (acquiredInfo == 0) {
            notifyUserActivity();
        }
        if (shouldSend) {
            try {
                getListener().onAcquired(getSensorId(), acquiredInfo, vendorCode);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to invoke sendAcquired", e);
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    final void notifyUserActivity() {
        long now = android.os.SystemClock.uptimeMillis();
        this.mPowerManager.userActivity(now, 2, 0);
    }

    protected final void vibrateSuccess() {
        android.os.Vibrator vibrator;
        if (!sStaticExt.vibrateFingerprintSuccess(getContext(), this) && (vibrator = (android.os.Vibrator) getContext().getSystemService(android.os.Vibrator.class)) != null && this.mShouldVibrate) {
            vibrator.vibrate(android.os.Process.myUid(), getContext().getOpPackageName(), SUCCESS_VIBRATION_EFFECT, getClass().getSimpleName() + "::success", HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
        }
    }

    protected final void vibrateError() {
        android.os.Vibrator vibrator;
        if (!sStaticExt.vibrateFingerprintError(getContext(), this) && (vibrator = (android.os.Vibrator) getContext().getSystemService(android.os.Vibrator.class)) != null && this.mShouldVibrate) {
            vibrator.vibrate(android.os.Process.myUid(), getContext().getOpPackageName(), ERROR_VIBRATION_EFFECT, getClass().getSimpleName() + "::error", HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean isInterruptable() {
        return true;
    }

    public boolean isAlreadyCancelled() {
        return this.mAlreadyCancelled;
    }
}
