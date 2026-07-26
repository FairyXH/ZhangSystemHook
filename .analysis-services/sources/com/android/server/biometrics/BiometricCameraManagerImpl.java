package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class BiometricCameraManagerImpl implements com.android.server.biometrics.BiometricCameraManager {
    private final android.hardware.camera2.CameraManager mCameraManager;
    private final android.hardware.SensorPrivacyManager mSensorPrivacyManager;
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Boolean> mIsCameraAvailable = new java.util.concurrent.ConcurrentHashMap<>();
    private final android.hardware.camera2.CameraManager.AvailabilityCallback mCameraAvailabilityCallback = new android.hardware.camera2.CameraManager.AvailabilityCallback() { // from class: com.android.server.biometrics.BiometricCameraManagerImpl.1
        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraAvailable(java.lang.String cameraId) {
            com.android.server.biometrics.BiometricCameraManagerImpl.this.mIsCameraAvailable.put(cameraId, true);
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraUnavailable(java.lang.String cameraId) {
            com.android.server.biometrics.BiometricCameraManagerImpl.this.mIsCameraAvailable.put(cameraId, false);
        }
    };

    public BiometricCameraManagerImpl(android.hardware.camera2.CameraManager cameraManager, android.hardware.SensorPrivacyManager sensorPrivacyManager) {
        this.mCameraManager = cameraManager;
        this.mSensorPrivacyManager = sensorPrivacyManager;
        this.mCameraManager.registerAvailabilityCallback(this.mCameraAvailabilityCallback, (android.os.Handler) null);
    }

    @Override // com.android.server.biometrics.BiometricCameraManager
    public boolean isAnyCameraUnavailable() {
        for (java.lang.String cameraId : this.mIsCameraAvailable.keySet()) {
            if (!this.mIsCameraAvailable.get(cameraId).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.biometrics.BiometricCameraManager
    public boolean isCameraPrivacyEnabled() {
        return this.mSensorPrivacyManager != null && this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 2);
    }
}
