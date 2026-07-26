package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class StopUserClient<T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> {
    private final com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback mUserStoppedCallback;

    public interface UserStoppedCallback {
        void onUserStopped();
    }

    public void onUserStopped() {
        this.mUserStoppedCallback.onUserStopped();
        getCallback().onClientFinished(this, true);
    }

    public StopUserClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback callback) {
        super(context, lazyDaemon, token, null, userId, context.getOpPackageName(), 0, sensorId, logger, biometricContext);
        this.mUserStoppedCallback = callback;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 16;
    }
}
