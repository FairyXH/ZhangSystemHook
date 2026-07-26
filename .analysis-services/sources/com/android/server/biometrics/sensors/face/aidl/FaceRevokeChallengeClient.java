package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceRevokeChallengeClient extends com.android.server.biometrics.sensors.RevokeChallengeClient<com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    private static final java.lang.String TAG = "FaceRevokeChallengeClient";
    private final long mChallenge;

    public FaceRevokeChallengeClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, long challenge) {
        super(context, lazyDaemon, token, userId, owner, sensorId, logger, biometricContext);
        this.mChallenge = challenge;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().revokeChallenge(this.mChallenge);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Unable to revokeChallenge", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    void onChallengeRevoked(int sensorId, int userId, long challenge) {
        boolean success = challenge == this.mChallenge;
        this.mCallback.onClientFinished(this, success);
    }
}
