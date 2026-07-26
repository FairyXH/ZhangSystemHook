package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintGenerateChallengeClient extends com.android.server.biometrics.sensors.GenerateChallengeClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
    private static final java.lang.String TAG = "FingerprintGenerateChallengeClient";

    public FingerprintGenerateChallengeClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, lazyDaemon, token, listener, userId, owner, sensorId, biometricLogger, biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().generateChallenge();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to generateChallenge", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    void onChallengeGenerated(int sensorId, int userId, long challenge) {
        try {
            com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener = getListener();
            listener.onChallengeGenerated(sensorId, userId, challenge);
            this.mCallback.onClientFinished(this, true);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send challenge", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
