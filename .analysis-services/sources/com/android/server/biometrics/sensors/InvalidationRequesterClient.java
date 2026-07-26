package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class InvalidationRequesterClient<S extends android.hardware.biometrics.BiometricAuthenticator.Identifier> extends com.android.server.biometrics.sensors.BaseClientMonitor {
    private final android.hardware.biometrics.BiometricManager mBiometricManager;
    private final android.hardware.biometrics.IInvalidationCallback mInvalidationCallback;
    private final com.android.server.biometrics.sensors.BiometricUtils<S> mUtils;

    public InvalidationRequesterClient(android.content.Context context, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.BiometricUtils<S> utils) {
        super(context, null, null, userId, context.getOpPackageName(), 0, sensorId, logger, biometricContext);
        this.mInvalidationCallback = new android.hardware.biometrics.IInvalidationCallback.Stub() { // from class: com.android.server.biometrics.sensors.InvalidationRequesterClient.1
            public void onCompleted() {
                com.android.server.biometrics.sensors.InvalidationRequesterClient.this.mUtils.setInvalidationInProgress(com.android.server.biometrics.sensors.InvalidationRequesterClient.this.getContext(), com.android.server.biometrics.sensors.InvalidationRequesterClient.this.getTargetUserId(), false);
                com.android.server.biometrics.sensors.InvalidationRequesterClient.this.mCallback.onClientFinished(com.android.server.biometrics.sensors.InvalidationRequesterClient.this, true);
            }
        };
        this.mBiometricManager = (android.hardware.biometrics.BiometricManager) context.getSystemService(android.hardware.biometrics.BiometricManager.class);
        this.mUtils = utils;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        this.mUtils.setInvalidationInProgress(getContext(), getTargetUserId(), true);
        this.mBiometricManager.invalidateAuthenticatorIds(getTargetUserId(), getSensorId(), this.mInvalidationCallback);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 14;
    }
}
