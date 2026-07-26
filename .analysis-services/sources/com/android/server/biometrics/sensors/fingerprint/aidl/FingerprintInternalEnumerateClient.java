package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintInternalEnumerateClient extends com.android.server.biometrics.sensors.InternalEnumerateClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
    private static final java.lang.String TAG = "FingerprintInternalEnumerateClient";

    protected FingerprintInternalEnumerateClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, int userId, java.lang.String owner, java.util.List<android.hardware.fingerprint.Fingerprint> enrolledList, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, lazyDaemon, token, userId, owner, enrolledList, utils, sensorId, logger, biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().enumerateEnrollments();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting enumerate", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.InternalEnumerateClient
    protected int getModality() {
        return 1;
    }
}
