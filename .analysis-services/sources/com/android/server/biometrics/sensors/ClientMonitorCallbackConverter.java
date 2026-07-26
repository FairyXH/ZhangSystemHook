package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class ClientMonitorCallbackConverter {
    private com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt mExt;
    private final android.hardware.face.IFaceServiceReceiver mFaceServiceReceiver;
    private final android.hardware.fingerprint.IFingerprintServiceReceiver mFingerprintServiceReceiver;
    private final android.hardware.biometrics.IBiometricSensorReceiver mSensorReceiver;

    public ClientMonitorCallbackConverter(android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver) {
        this.mExt = (com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt.class).create();
        this.mSensorReceiver = sensorReceiver;
        this.mFaceServiceReceiver = null;
        this.mFingerprintServiceReceiver = null;
    }

    public ClientMonitorCallbackConverter(android.hardware.face.IFaceServiceReceiver faceServiceReceiver) {
        this.mExt = (com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt.class).create();
        this.mSensorReceiver = null;
        this.mFaceServiceReceiver = faceServiceReceiver;
        this.mFingerprintServiceReceiver = null;
    }

    public ClientMonitorCallbackConverter(android.hardware.fingerprint.IFingerprintServiceReceiver fingerprintServiceReceiver) {
        this.mExt = (com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IClientMonitorCallbackConverterExt.class).create();
        this.mSensorReceiver = null;
        this.mFaceServiceReceiver = null;
        this.mFingerprintServiceReceiver = fingerprintServiceReceiver;
    }

    public int getModality() {
        if (this.mFaceServiceReceiver != null) {
            return 8;
        }
        if (this.mFingerprintServiceReceiver != null) {
            return 2;
        }
        return 0;
    }

    public void onAcquired(int sensorId, int acquiredInfo, int vendorCode) throws android.os.RemoteException {
        if (this.mSensorReceiver != null) {
            this.mSensorReceiver.onAcquired(sensorId, acquiredInfo, vendorCode);
        } else if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onAcquired(acquiredInfo, vendorCode);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onAcquired(acquiredInfo, vendorCode);
        }
    }

    void onAuthenticationSucceeded(int sensorId, android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, byte[] token, int userId, boolean isStrongBiometric) throws android.os.RemoteException {
        if (this.mSensorReceiver != null) {
            this.mSensorReceiver.onAuthenticationSucceeded(sensorId, token);
            return;
        }
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onAuthenticationSucceeded((android.hardware.face.Face) identifier, userId, isStrongBiometric);
            this.mExt.notifyFaceAuthenticationResult(true);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onAuthenticationSucceeded((android.hardware.fingerprint.Fingerprint) identifier, userId, isStrongBiometric);
            this.mExt.notifyFingerprintAuthenticationResult(true);
        }
    }

    void onAuthenticationFailed(int sensorId) throws android.os.RemoteException {
        if (this.mSensorReceiver != null) {
            this.mSensorReceiver.onAuthenticationFailed(sensorId);
        } else if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onAuthenticationFailed();
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onAuthenticationFailed();
        }
    }

    public void onError(int sensorId, int cookie, int error, int vendorCode) throws android.os.RemoteException {
        if (this.mSensorReceiver != null) {
            this.mSensorReceiver.onError(sensorId, cookie, error, vendorCode);
        } else if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onError(error, vendorCode);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onError(error, vendorCode);
        }
    }

    public void onDetected(int sensorId, int userId, boolean isStrongBiometric) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onFaceDetected(sensorId, userId, isStrongBiometric);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onFingerprintDetected(sensorId, userId, isStrongBiometric);
        }
    }

    void onEnrollResult(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onEnrollResult((android.hardware.face.Face) identifier, remaining);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onEnrollResult((android.hardware.fingerprint.Fingerprint) identifier, remaining);
        }
    }

    public void onRemoved(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onRemoved((android.hardware.face.Face) identifier, remaining);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onRemoved((android.hardware.fingerprint.Fingerprint) identifier, remaining);
        }
    }

    public void onChallengeGenerated(int sensorId, int userId, long challenge) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onChallengeGenerated(sensorId, userId, challenge);
        } else if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onChallengeGenerated(sensorId, userId, challenge);
        }
    }

    public void onFeatureSet(boolean success, int feature) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onFeatureSet(success, feature);
        }
    }

    public void onFeatureGet(boolean success, int[] features, boolean[] featureState) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onFeatureGet(success, features, featureState);
        }
    }

    public void onUdfpsPointerDown(int sensorId) throws android.os.RemoteException {
        if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onUdfpsPointerDown(sensorId);
        }
    }

    public void onUdfpsPointerUp(int sensorId) throws android.os.RemoteException {
        if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onUdfpsPointerUp(sensorId);
        }
    }

    public void onUdfpsOverlayShown() throws android.os.RemoteException {
        if (this.mFingerprintServiceReceiver != null) {
            this.mFingerprintServiceReceiver.onUdfpsOverlayShown();
        }
    }

    public void onAuthenticationFrame(android.hardware.face.FaceAuthenticationFrame frame) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onAuthenticationFrame(frame);
        }
    }

    public void onEnrollmentFrame(android.hardware.face.FaceEnrollFrame frame) throws android.os.RemoteException {
        if (this.mFaceServiceReceiver != null) {
            this.mFaceServiceReceiver.onEnrollmentFrame(frame);
        }
    }
}
