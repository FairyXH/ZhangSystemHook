package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceGetFeatureClient extends com.android.server.biometrics.sensors.HalClientMonitor<com.android.server.biometrics.sensors.face.aidl.AidlSession> implements com.android.server.biometrics.sensors.ErrorConsumer {
    private static final java.lang.String TAG = "FaceGetFeatureClient";
    private final int mFeature;
    private final int mUserId;

    public FaceGetFeatureClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, int feature) {
        super(context, lazyDaemon, token, listener, userId, owner, 0, sensorId, logger, biometricContext);
        this.mUserId = userId;
        this.mFeature = feature;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
        this.mCallback.onClientFinished(this, false);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            android.hardware.biometrics.face.ISession session = getFreshDaemon().getSession();
            if (session instanceof com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter) {
                ((com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter) session).setFeature(this.mFeature);
            }
            session.getFeatures();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Unable to getFeature", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 9;
    }

    public void onFeatureGet(boolean success, byte[] features) {
        try {
            java.util.HashMap<java.lang.Integer, java.lang.Boolean> featureMap = getFeatureMap();
            int[] featuresToSend = new int[featureMap.size()];
            boolean[] featureState = new boolean[featureMap.size()];
            for (byte b : features) {
                featureMap.put(java.lang.Integer.valueOf(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.convertAidlToFrameworkFeature(b)), true);
            }
            int i = 0;
            for (java.util.Map.Entry<java.lang.Integer, java.lang.Boolean> entry : featureMap.entrySet()) {
                featuresToSend[i] = entry.getKey().intValue();
                featureState[i] = entry.getValue().booleanValue();
                i++;
            }
            boolean attentionEnabled = featureMap.get(1).booleanValue();
            android.util.Slog.d(TAG, "Updating attention value for user: " + this.mUserId + " to value: " + attentionEnabled);
            android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "face_unlock_attention_required", attentionEnabled ? 1 : 0, this.mUserId);
            getListener().onFeatureGet(success, featuresToSend, featureState);
            this.mCallback.onClientFinished(this, true);
        } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "exception", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    private java.util.HashMap<java.lang.Integer, java.lang.Boolean> getFeatureMap() {
        java.util.HashMap<java.lang.Integer, java.lang.Boolean> featureMap = new java.util.HashMap<>();
        featureMap.put(1, false);
        return featureMap;
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        try {
            getListener().onFeatureGet(false, new int[0], new boolean[0]);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        this.mCallback.onClientFinished(this, false);
    }
}
