package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class RemovalClient<S extends android.hardware.biometrics.BiometricAuthenticator.Identifier, T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> implements com.android.server.biometrics.sensors.RemovalConsumer, com.android.server.biometrics.sensors.EnrollmentModifier {
    private static final java.lang.String TAG = "Biometrics/RemovalClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    private final com.android.server.biometrics.sensors.BiometricUtils<S> mBiometricUtils;
    private final boolean mHasEnrollmentsBeforeStarting;
    private android.hardware.IRedLoggerExt mRedLoggerExt;

    public RemovalClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils<S> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, token, listener, userId, owner, 0, sensorId, logger, biometricContext);
        this.mRedLoggerExt = (android.hardware.IRedLoggerExt) system.ext.loader.core.ExtLoader.type(android.hardware.IRedLoggerExt.class).create();
        this.mBiometricUtils = utils;
        this.mAuthenticatorIds = authenticatorIds;
        this.mHasEnrollmentsBeforeStarting = !utils.getBiometricsForUser(context, userId).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.RemovalConsumer
    public void onRemoved(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) {
        if (identifier == null) {
            android.util.Slog.e(TAG, "identifier was null, skipping onRemove()");
            try {
                getListener().onError(getSensorId(), getCookie(), 6, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to send error to client for onRemoved", e);
            }
            this.mCallback.onClientFinished(this, false);
            return;
        }
        android.util.Slog.d(TAG, "onRemoved: " + identifier.getBiometricId() + " remaining: " + remaining);
        this.mBiometricUtils.removeBiometricForUser(getContext(), getTargetUserId(), identifier.getBiometricId());
        try {
            getListener().onRemoved(identifier, remaining);
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to notify Removed:", e2);
        }
        if (remaining == 0) {
            if (this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty()) {
                android.util.Slog.d(TAG, "Last biometric removed for user: " + getTargetUserId());
                this.mAuthenticatorIds.put(java.lang.Integer.valueOf(getTargetUserId()), 0L);
            }
            this.mCallback.onClientFinished(this, true);
            if (this.mRedLoggerExt == null) {
                android.util.Slog.e(TAG, "mRedLoggerExt is null!");
            } else if (this instanceof com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRemovalClient) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.RemovalClient$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onRemoved$0();
                    }
                });
            } else if (this instanceof com.android.server.biometrics.sensors.face.aidl.FaceRemovalClient) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.RemovalClient$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onRemoved$1();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRemoved$0() {
        this.mRedLoggerExt.saveREDLog("FINGERPRINT", getTargetUserId(), "remove", 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRemoved$1() {
        this.mRedLoggerExt.saveREDLog("FACE", getTargetUserId(), "remove", 1);
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollmentStateChanged() {
        boolean hasEnrollmentsNow = !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
        return hasEnrollmentsNow != this.mHasEnrollmentsBeforeStarting;
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollments() {
        return !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 4;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }
}
