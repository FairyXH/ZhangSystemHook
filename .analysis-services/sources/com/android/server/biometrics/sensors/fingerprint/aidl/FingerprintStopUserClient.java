package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintStopUserClient extends com.android.server.biometrics.sensors.StopUserClient<android.hardware.biometrics.fingerprint.ISession> {
    private static final java.lang.String TAG = "FingerprintStopUserClient";

    public FingerprintStopUserClient(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.fingerprint.ISession> lazyDaemon, android.os.IBinder token, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback callback) {
        super(context, lazyDaemon, token, userId, sensorId, logger, biometricContext, callback);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().close();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception", e);
            getCallback().onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }
}
