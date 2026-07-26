package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceRemovalClient extends com.android.server.biometrics.sensors.RemovalClient<android.hardware.face.Face, com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    private static final java.lang.String TAG = "FaceRemovalClient";
    final int[] mBiometricIds;

    public FaceRemovalClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int[] biometricIds, int userId, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, token, listener, userId, owner, utils, sensorId, logger, biometricContext, authenticatorIds);
        this.mBiometricIds = biometricIds;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().removeEnrollments(this.mBiometricIds);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception when requesting remove", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
