package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
class FaceGetAuthenticatorIdClient extends com.android.server.biometrics.sensors.HalClientMonitor<com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    private static final java.lang.String TAG = "FaceGetAuthenticatorIdClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;

    FaceGetAuthenticatorIdClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, int userId, java.lang.String opPackageName, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, null, null, userId, opPackageName, 0, sensorId, logger, biometricContext);
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
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAuthenticatorIdRetrieved(long authenticatorId) {
        this.mAuthenticatorIds.put(java.lang.Integer.valueOf(getTargetUserId()), java.lang.Long.valueOf(authenticatorId));
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 5;
    }
}
