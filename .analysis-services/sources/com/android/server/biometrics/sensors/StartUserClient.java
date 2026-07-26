package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class StartUserClient<T, U> extends com.android.server.biometrics.sensors.HalClientMonitor<T> {
    protected final com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<U> mUserStartedCallback;

    public interface UserStartedCallback<U> {
        void onUserStarted(int i, U u, int i2);
    }

    public StartUserClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<U> callback) {
        super(context, lazyDaemon, token, null, userId, context.getOpPackageName(), 0, sensorId, logger, biometricContext);
        this.mUserStartedCallback = callback;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 17;
    }
}
