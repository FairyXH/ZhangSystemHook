package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintRemovalClient extends com.android.server.biometrics.sensors.RemovalClient<android.hardware.fingerprint.Fingerprint, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
    private static final java.lang.String TAG = "FingerprintRemovalClient";
    private final int[] mBiometricIds;

    public FingerprintRemovalClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int[] biometricIds, int userId, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, token, listener, userId, owner, utils, sensorId, logger, biometricContext, authenticatorIds);
        this.mBiometricIds = biometricIds;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().removeEnrollments(this.mBiometricIds);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting remove", e);
            this.mCallback.onClientFinished(this, false);
        }
    }
}
