package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class RevokeChallengeClient<T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> {
    public RevokeChallengeClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, lazyDaemon, token, null, userId, owner, 0, sensorId, biometricLogger, biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 11;
    }
}
