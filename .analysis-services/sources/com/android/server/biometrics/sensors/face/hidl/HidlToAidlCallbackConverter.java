package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: loaded from: classes.dex */
public class HidlToAidlCallbackConverter extends android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback.Stub {
    private final com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler mAidlResponseHandler;

    public HidlToAidlCallbackConverter(com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mAidlResponseHandler = aidlResponseHandler;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onEnrollResult(long deviceId, int faceId, int userId, int remaining) {
        this.mAidlResponseHandler.onEnrollmentProgress(faceId, remaining);
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onAuthenticated(long deviceId, int faceId, int userId, java.util.ArrayList<java.lang.Byte> token) {
        boolean authenticated = faceId != 0;
        byte[] hardwareAuthToken = new byte[token.size()];
        for (int i = 0; i < token.size(); i++) {
            hardwareAuthToken[i] = token.get(i).byteValue();
        }
        if (authenticated) {
            this.mAidlResponseHandler.onAuthenticationSucceeded(faceId, com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(hardwareAuthToken));
        } else {
            this.mAidlResponseHandler.onAuthenticationFailed();
        }
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onAcquired(long deviceId, int userId, int acquiredInfo, int vendorCode) {
        this.mAidlResponseHandler.onAcquired(acquiredInfo, vendorCode);
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onError(long deviceId, int userId, int error, int vendorCode) {
        this.mAidlResponseHandler.onError(error, vendorCode);
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onRemoved(long deviceId, java.util.ArrayList<java.lang.Integer> removed, int userId) {
        int[] enrollmentIds = new int[removed.size()];
        for (int i = 0; i < removed.size(); i++) {
            enrollmentIds[i] = removed.get(i).intValue();
        }
        this.mAidlResponseHandler.onEnrollmentsRemoved(enrollmentIds);
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onEnumerate(long deviceId, java.util.ArrayList<java.lang.Integer> faceIds, int userId) {
        int[] enrollmentIds = new int[faceIds.size()];
        for (int i = 0; i < faceIds.size(); i++) {
            enrollmentIds[i] = faceIds.get(i).intValue();
        }
        this.mAidlResponseHandler.onEnrollmentsEnumerated(enrollmentIds);
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback
    public void onLockoutChanged(long duration) {
        this.mAidlResponseHandler.onLockoutChanged(duration);
    }

    void onChallengeGenerated(long challenge) {
        this.mAidlResponseHandler.onChallengeGenerated(challenge);
    }

    void onChallengeRevoked(long challenge) {
        this.mAidlResponseHandler.onChallengeRevoked(challenge);
    }

    void onFeatureGet(byte[] features) {
        this.mAidlResponseHandler.onFeaturesRetrieved(features);
    }

    void onFeatureSet(byte feature) {
        this.mAidlResponseHandler.onFeatureSet(feature);
    }

    void onAuthenticatorIdRetrieved(long authenticatorId) {
        this.mAidlResponseHandler.onAuthenticatorIdRetrieved(authenticatorId);
    }

    void onUnsupportedClientScheduled() {
        this.mAidlResponseHandler.onUnsupportedClientScheduled();
    }
}
