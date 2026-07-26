package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceInvalidationClient extends com.android.server.biometrics.sensors.InvalidationClient<android.hardware.face.Face, com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    private static final java.lang.String TAG = "FaceInvalidationClient";

    public FaceInvalidationClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds, android.hardware.biometrics.IInvalidationCallback callback) {
        super(context, lazyDaemon, userId, sensorId, logger, biometricContext, authenticatorIds, callback);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().invalidateAuthenticatorId();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
