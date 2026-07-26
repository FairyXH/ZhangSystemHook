package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class EnrollClient<T> extends com.android.server.biometrics.sensors.AcquisitionClient<T> implements com.android.server.biometrics.sensors.EnrollmentModifier {
    private static final java.lang.String TAG = "Biometrics/EnrollClient";
    private static com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt mOplusFingerUtilsExt = (com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt.class).create();
    protected final com.android.server.biometrics.sensors.BiometricUtils mBiometricUtils;
    private final int mEnrollReason;
    private long mEnrollmentStartTimeMs;
    protected final byte[] mHardwareAuthToken;
    private final boolean mHasEnrollmentsBeforeStarting;
    private android.hardware.IRedLoggerExt mRedLoggerExt;
    protected final int mTimeoutSec;

    protected abstract boolean hasReachedEnrollmentLimit();

    public EnrollClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, byte[] hardwareAuthToken, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils utils, int timeoutSec, int sensorId, boolean shouldVibrate, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, int enrollReason) {
        super(context, lazyDaemon, token, listener, userId, owner, 0, sensorId, shouldVibrate, logger, biometricContext);
        this.mRedLoggerExt = (android.hardware.IRedLoggerExt) system.ext.loader.core.ExtLoader.type(android.hardware.IRedLoggerExt.class).create();
        this.mBiometricUtils = utils;
        this.mHardwareAuthToken = java.util.Arrays.copyOf(hardwareAuthToken, hardwareAuthToken.length);
        this.mTimeoutSec = timeoutSec;
        this.mHasEnrollmentsBeforeStarting = hasEnrollments();
        this.mEnrollReason = enrollReason;
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollmentStateChanged() {
        boolean hasEnrollmentsNow = hasEnrollments();
        return hasEnrollmentsNow != this.mHasEnrollmentsBeforeStarting;
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollments() {
        return !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
    }

    public void onEnrollResult(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) {
        if (this.mShouldVibrate) {
            vibrateSuccess();
        }
        com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener = getListener();
        try {
            listener.onEnrollResult(identifier, remaining);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        if (remaining == 0) {
            this.mBiometricUtils.addBiometricForUser(getContext(), getTargetUserId(), identifier);
            getLogger().logOnEnrolled(getTargetUserId(), java.lang.System.currentTimeMillis() - this.mEnrollmentStartTimeMs, true, this.mEnrollReason);
            this.mCallback.onClientFinished(this, true);
            if (this.mRedLoggerExt == null) {
                android.util.Slog.e(TAG, "mRedLoggerExt is null!");
            } else if (this instanceof com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.EnrollClient$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEnrollResult$0();
                    }
                });
            } else if (this instanceof com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.EnrollClient$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEnrollResult$1();
                    }
                });
            }
        }
        notifyUserActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollResult$0() {
        this.mRedLoggerExt.saveREDLog("FINGERPRINT", getTargetUserId(), "enroll", 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollResult$1() {
        this.mRedLoggerExt.saveREDLog("FACE", getTargetUserId(), "enroll", 1);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        if (hasReachedEnrollmentLimit()) {
            android.util.Slog.e(TAG, "Reached enrollment limit");
            callback.onClientFinished(this, false);
        } else {
            this.mEnrollmentStartTimeMs = java.lang.System.currentTimeMillis();
            startHalOperation();
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int error, int vendorCode) {
        getLogger().logOnEnrolled(getTargetUserId(), java.lang.System.currentTimeMillis() - this.mEnrollmentStartTimeMs, false, this.mEnrollReason);
        super.onError(error, vendorCode);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 2;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    protected int getRequestReasonFromFingerprintEnrollReason(int reason) {
        int reason2 = mOplusFingerUtilsExt.getReasonForCloneSystem(reason);
        android.util.Slog.d(TAG, "Fingerprint enroll tempReason:" + reason + ", reason:" + reason2);
        switch (reason2) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    protected int getRequestReasonFromFaceEnrollReason(int reason) {
        switch (reason) {
            case 1:
            case 2:
            case 3:
                return 2;
            default:
                return 0;
        }
    }
}
