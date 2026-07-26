package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class InvalidationClient<S extends android.hardware.biometrics.BiometricAuthenticator.Identifier, T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> {
    private static final java.lang.String TAG = "InvalidationClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    private final android.hardware.biometrics.IInvalidationCallback mInvalidationCallback;

    public InvalidationClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds, android.hardware.biometrics.IInvalidationCallback callback) {
        super(context, lazyDaemon, null, null, userId, context.getOpPackageName(), 0, sensorId, logger, biometricContext);
        this.mAuthenticatorIds = authenticatorIds;
        this.mInvalidationCallback = callback;
    }

    public void onAuthenticatorIdInvalidated(long newAuthenticatorId) {
        this.mAuthenticatorIds.put(java.lang.Integer.valueOf(getTargetUserId()), java.lang.Long.valueOf(newAuthenticatorId));
        try {
            this.mInvalidationCallback.onCompleted();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 15;
    }
}
