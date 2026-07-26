package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceSetFeatureClient extends com.android.server.biometrics.sensors.HalClientMonitor<com.android.server.biometrics.sensors.face.aidl.AidlSession> implements com.android.server.biometrics.sensors.ErrorConsumer {
    private static final java.lang.String TAG = "FaceSetFeatureClient";
    private final boolean mEnabled;
    private final int mFeature;
    private final android.hardware.keymaster.HardwareAuthToken mHardwareAuthToken;

    public FaceSetFeatureClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, int feature, boolean enabled, byte[] hardwareAuthToken) {
        super(context, lazyDaemon, token, listener, userId, owner, 0, sensorId, logger, biometricContext);
        this.mFeature = feature;
        this.mEnabled = enabled;
        this.mHardwareAuthToken = com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(hardwareAuthToken);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
        try {
            getListener().onFeatureSet(false, this.mFeature);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send error", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            getFreshDaemon().getSession().setFeature(this.mHardwareAuthToken, com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.convertFrameworkToAidlFeature(this.mFeature), this.mEnabled);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Unable to set feature: " + this.mFeature + " to enabled: " + this.mEnabled, e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 8;
    }

    public void onFeatureSet(boolean success) {
        try {
            getListener().onFeatureSet(success, this.mFeature);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        try {
            getListener().onFeatureSet(false, this.mFeature);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        this.mCallback.onClientFinished(this, false);
    }
}
