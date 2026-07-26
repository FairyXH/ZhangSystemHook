package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class GenerateChallengeClient<T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> {
    private static final java.lang.String TAG = "GenerateChallengeClient";

    public GenerateChallengeClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, lazyDaemon, token, listener, userId, owner, 0, sensorId, biometricLogger, biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
        try {
            getListener().onChallengeGenerated(getSensorId(), getTargetUserId(), 0L);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send error", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 10;
    }
}
