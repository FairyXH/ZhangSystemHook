package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintGetAuthenticatorIdClient extends com.android.server.biometrics.sensors.HalClientMonitor<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
    private static final java.lang.String TAG = "FingerprintGetAuthenticatorIdClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;

    FingerprintGetAuthenticatorIdClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, null, null, userId, owner, 0, sensorId, biometricLogger, biometricContext);
        this.mAuthenticatorIds = authenticatorIds;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            if (getFreshDaemon().getSession() != null) {
                getFreshDaemon().getSession().getAuthenticatorId();
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAuthenticatorIdRetrieved(long authenticatorId) {
        this.mAuthenticatorIds.put(java.lang.Integer.valueOf(getTargetUserId() == 999 ? 0 : getTargetUserId()), java.lang.Long.valueOf(authenticatorId));
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 5;
    }
}
