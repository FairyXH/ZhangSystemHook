package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public class HidlToAidlCallbackConverter extends android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.Stub {
    final com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler mAidlResponseHandler;

    public HidlToAidlCallbackConverter(com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mAidlResponseHandler = aidlResponseHandler;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
    public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) {
        this.mAidlResponseHandler.onEnrollmentProgress(fingerId, remaining);
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
    public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) {
        onAcquired_2_2(deviceId, acquiredInfo, vendorCode);
    }

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback
    public void onAcquired_2_2(long deviceId, int acquiredInfo, int vendorCode) {
        this.mAidlResponseHandler.onAcquired(acquiredInfo, vendorCode);
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
    public void onAuthenticated(long deviceId, int fingerId, int groupId, java.util.ArrayList<java.lang.Byte> token) {
        if (fingerId != 0) {
            byte[] hardwareAuthToken = new byte[token.size()];
            for (int i = 0; i < token.size(); i++) {
                hardwareAuthToken[i] = token.get(i).byteValue();
            }
            this.mAidlResponseHandler.onAuthenticationSucceeded(fingerId, com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(hardwareAuthToken));
            return;
        }
        this.mAidlResponseHandler.onAuthenticationFailed();
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
    public void onError(long deviceId, int error, int vendorCode) {
        this.mAidlResponseHandler.onError(error, vendorCode);
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
    public void onRemoved(long deviceId, int fingerId, int groupId, int remaining) {
        this.mAidlResponseHandler.onEnrollmentRemoved(fingerId, remaining);
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
    public void onEnumerate(long deviceId, int fingerId, int groupId, int remaining) {
        this.mAidlResponseHandler.onEnrollmentEnumerated(fingerId, remaining);
    }

    void onChallengeGenerated(long challenge) {
        this.mAidlResponseHandler.onChallengeGenerated(challenge);
    }

    void onChallengeRevoked(long challenge) {
        this.mAidlResponseHandler.onChallengeRevoked(challenge);
    }

    void onResetLockout() {
        this.mAidlResponseHandler.onLockoutCleared();
    }

    <T extends com.android.server.biometrics.sensors.BaseClientMonitor> void unsupportedClientScheduled(java.lang.Class<T> className) {
        this.mAidlResponseHandler.onUnsupportedClientScheduled(className);
    }
}
