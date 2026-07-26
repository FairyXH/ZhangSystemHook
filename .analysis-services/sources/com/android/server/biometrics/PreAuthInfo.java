package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
class PreAuthInfo {
    static final int AUTHENTICATOR_OK = 1;
    static final int BIOMETRIC_DISABLED_BY_DEVICE_POLICY = 3;
    static final int BIOMETRIC_HARDWARE_NOT_DETECTED = 6;
    static final int BIOMETRIC_INSUFFICIENT_STRENGTH = 4;
    static final int BIOMETRIC_INSUFFICIENT_STRENGTH_AFTER_DOWNGRADE = 5;
    static final int BIOMETRIC_LOCKOUT_PERMANENT = 11;
    static final int BIOMETRIC_LOCKOUT_TIMED = 10;
    static final int BIOMETRIC_NOT_ENABLED_FOR_APPS = 8;
    static final int BIOMETRIC_NOT_ENROLLED = 7;
    static final int BIOMETRIC_NO_HARDWARE = 2;
    static final int BIOMETRIC_SENSOR_PRIVACY_ENABLED = 12;
    static final int CREDENTIAL_NOT_ENROLLED = 9;
    private static final java.lang.String TAG = "BiometricService/PreAuthInfo";
    private static com.android.server.biometrics.IPreAuthInfoExt mPreAuthInfoExt = (com.android.server.biometrics.IPreAuthInfoExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.IPreAuthInfoExt.class).create();
    final boolean confirmationRequested;
    final android.content.Context context;
    final boolean credentialAvailable;
    final boolean credentialRequested;
    final java.util.List<com.android.server.biometrics.BiometricSensor> eligibleSensors;
    final boolean ignoreEnrollmentState;
    final java.util.List<android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer>> ineligibleSensors;
    private final com.android.server.biometrics.BiometricCameraManager mBiometricCameraManager;
    private final boolean mBiometricRequested;
    private final int mBiometricStrengthRequested;
    final int userId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface AuthenticatorStatus {
    }

    private PreAuthInfo(boolean biometricRequested, int biometricStrengthRequested, boolean credentialRequested, java.util.List<com.android.server.biometrics.BiometricSensor> eligibleSensors, java.util.List<android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer>> ineligibleSensors, boolean credentialAvailable, boolean confirmationRequested, boolean ignoreEnrollmentState, int userId, android.content.Context context, com.android.server.biometrics.BiometricCameraManager biometricCameraManager) {
        this.mBiometricRequested = biometricRequested;
        this.mBiometricStrengthRequested = biometricStrengthRequested;
        this.mBiometricCameraManager = biometricCameraManager;
        this.credentialRequested = credentialRequested;
        this.eligibleSensors = eligibleSensors;
        this.ineligibleSensors = ineligibleSensors;
        this.credentialAvailable = credentialAvailable;
        this.confirmationRequested = confirmationRequested;
        this.ignoreEnrollmentState = ignoreEnrollmentState;
        this.userId = userId;
        this.context = context;
    }

    static com.android.server.biometrics.PreAuthInfo create(android.app.trust.ITrustManager trustManager, android.app.admin.DevicePolicyManager devicePolicyManager, com.android.server.biometrics.BiometricService.SettingObserver settingObserver, java.util.List<com.android.server.biometrics.BiometricSensor> sensors, int userId, android.hardware.biometrics.PromptInfo promptInfo, java.lang.String opPackageName, boolean checkDevicePolicyManager, android.content.Context context, com.android.server.biometrics.BiometricCameraManager biometricCameraManager) throws android.os.RemoteException {
        java.util.Iterator<com.android.server.biometrics.BiometricSensor> it;
        com.android.server.biometrics.BiometricSensor sensor;
        java.lang.String str;
        boolean confirmationRequested = promptInfo.isConfirmationRequested();
        boolean biometricRequested = com.android.server.biometrics.Utils.isBiometricRequested(promptInfo);
        int requestedStrength = com.android.server.biometrics.Utils.getPublicBiometricStrength(promptInfo);
        boolean credentialRequested = com.android.server.biometrics.Utils.isCredentialRequested(promptInfo);
        boolean credentialAvailable = trustManager.isDeviceSecure(userId, context.getDeviceId());
        java.util.List<com.android.server.biometrics.BiometricSensor> eligibleSensors = new java.util.ArrayList<>();
        java.util.List<android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer>> ineligibleSensors = new java.util.ArrayList<>();
        if (biometricRequested) {
            java.util.Iterator<com.android.server.biometrics.BiometricSensor> it2 = sensors.iterator();
            while (it2.hasNext()) {
                com.android.server.biometrics.BiometricSensor sensor2 = it2.next();
                int status = getStatusForBiometricAuthenticator(devicePolicyManager, settingObserver, sensor2, userId, opPackageName, checkDevicePolicyManager, requestedStrength, promptInfo.getAllowedSensorIds(), promptInfo.isIgnoreEnrollmentState(), biometricCameraManager);
                if (mPreAuthInfoExt != null) {
                    com.android.server.biometrics.IPreAuthInfoExt iPreAuthInfoExt = mPreAuthInfoExt;
                    it = it2;
                    str = TAG;
                    if (!iPreAuthInfoExt.needSkipEligibleSensorAdd(sensor2, userId, opPackageName, context, promptInfo)) {
                        sensor = sensor2;
                    } else {
                        ineligibleSensors.add(new android.util.Pair<>(sensor2, 8));
                        android.util.Slog.d(str, "add ineligibleSensors, Package: " + opPackageName + " Sensor ID: " + sensor2.id + " Modality: " + sensor2.modality + " Status: " + status);
                        it2 = it;
                    }
                } else {
                    it = it2;
                    sensor = sensor2;
                    str = TAG;
                }
                android.util.Slog.d(str, "Package: " + opPackageName + " Sensor ID: " + sensor.id + " Modality: " + sensor.modality + " Status: " + status);
                if (status == 1) {
                    eligibleSensors.add(sensor);
                } else {
                    ineligibleSensors.add(new android.util.Pair<>(sensor, java.lang.Integer.valueOf(status)));
                }
                it2 = it;
            }
        }
        return new com.android.server.biometrics.PreAuthInfo(biometricRequested, requestedStrength, credentialRequested, eligibleSensors, ineligibleSensors, credentialAvailable, confirmationRequested, promptInfo.isIgnoreEnrollmentState(), userId, context, biometricCameraManager);
    }

    private static int getStatusForBiometricAuthenticator(android.app.admin.DevicePolicyManager devicePolicyManager, com.android.server.biometrics.BiometricService.SettingObserver settingObserver, com.android.server.biometrics.BiometricSensor sensor, int userId, java.lang.String opPackageName, boolean checkDevicePolicyManager, int requestedStrength, java.util.List<java.lang.Integer> requestedSensorIds, boolean ignoreEnrollmentState, com.android.server.biometrics.BiometricCameraManager biometricCameraManager) {
        if (!requestedSensorIds.isEmpty() && !requestedSensorIds.contains(java.lang.Integer.valueOf(sensor.id))) {
            return 2;
        }
        if (sensor.modality == 8 && biometricCameraManager.isAnyCameraUnavailable()) {
            return 6;
        }
        boolean wasStrongEnough = com.android.server.biometrics.Utils.isAtLeastStrength(sensor.oemStrength, requestedStrength);
        boolean isStrongEnough = com.android.server.biometrics.Utils.isAtLeastStrength(sensor.getCurrentStrength(), requestedStrength);
        if (wasStrongEnough && !isStrongEnough) {
            return 5;
        }
        if (!wasStrongEnough) {
            return 4;
        }
        if ("com.coloros.codebook".equals(opPackageName) && sensor.modality == 8) {
            android.util.Slog.d(TAG, "Package: " + opPackageName + " Sensor ID: " + sensor.id + " Modality: " + sensor.modality + " BIOMETRIC_INSUFFICIENT_STRENGTH");
            return 4;
        }
        try {
            if (!sensor.impl.isHardwareDetected(opPackageName)) {
                return 6;
            }
            if (!sensor.impl.hasEnrolledTemplates(userId, opPackageName) && !ignoreEnrollmentState) {
                return 7;
            }
            if (biometricCameraManager != null && sensor.modality == 8 && biometricCameraManager.isCameraPrivacyEnabled()) {
                return 12;
            }
            int lockoutMode = sensor.impl.getLockoutModeForUser(userId);
            if (lockoutMode == 1) {
                return 10;
            }
            if (lockoutMode == 2) {
                return 11;
            }
            if (!isEnabledForApp(settingObserver, sensor.modality, userId)) {
                return 8;
            }
            if (!checkDevicePolicyManager || !isBiometricDisabledByDevicePolicy(devicePolicyManager, sensor.modality, userId)) {
                return 1;
            }
            return 3;
        } catch (android.os.RemoteException e) {
            return 6;
        }
    }

    private static boolean isEnabledForApp(com.android.server.biometrics.BiometricService.SettingObserver settingObserver, int modality, int userId) {
        return settingObserver.getEnabledForApps(userId);
    }

    private static boolean isBiometricDisabledByDevicePolicy(android.app.admin.DevicePolicyManager devicePolicyManager, int modality, int effectiveUserId) {
        int biometricToCheck = mapModalityToDevicePolicyType(modality);
        if (biometricToCheck == 0) {
            throw new java.lang.IllegalStateException("Modality unknown to devicePolicyManager: " + modality);
        }
        int devicePolicyDisabledFeatures = devicePolicyManager.getKeyguardDisabledFeatures(null, effectiveUserId);
        boolean isBiometricDisabled = (biometricToCheck & devicePolicyDisabledFeatures) != 0;
        android.util.Slog.w(TAG, "isBiometricDisabledByDevicePolicy(" + modality + "," + effectiveUserId + ")=" + isBiometricDisabled);
        return isBiometricDisabled;
    }

    private static int mapModalityToDevicePolicyType(int modality) {
        switch (modality) {
            case 2:
                return 32;
            case 4:
                return 256;
            case 8:
                return 128;
            default:
                android.util.Slog.e(TAG, "Error modality=" + modality);
                return 0;
        }
    }

    private android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> calculateErrorByPriority() {
        android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> sensorNotEnrolled = null;
        android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> sensorLockout = null;
        android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> hardwareNotDetected = null;
        for (android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> pair : this.ineligibleSensors) {
            int status = ((java.lang.Integer) pair.second).intValue();
            if (status == 10 || status == 11) {
                sensorLockout = pair;
            }
            if (status == 7) {
                sensorNotEnrolled = pair;
            }
            if (status == 6) {
                hardwareNotDetected = pair;
            }
        }
        if (sensorLockout != null) {
            return sensorLockout;
        }
        if (hardwareNotDetected != null) {
            return hardwareNotDetected;
        }
        if (sensorNotEnrolled != null) {
            return sensorNotEnrolled;
        }
        return this.ineligibleSensors.get(0);
    }

    private android.util.Pair<java.lang.Integer, java.lang.Integer> getInternalStatus() {
        int status;
        int modality = 0;
        boolean cameraPrivacyEnabled = false;
        if (this.mBiometricCameraManager != null) {
            cameraPrivacyEnabled = this.mBiometricCameraManager.isCameraPrivacyEnabled();
        }
        if (this.mBiometricRequested && this.credentialRequested) {
            if (this.credentialAvailable || !this.eligibleSensors.isEmpty()) {
                for (com.android.server.biometrics.BiometricSensor sensor : this.eligibleSensors) {
                    modality |= sensor.modality;
                }
                if (this.credentialAvailable) {
                    modality |= 1;
                    status = 1;
                } else if (modality == 8 && cameraPrivacyEnabled) {
                    status = 12;
                } else {
                    status = 1;
                }
            } else if (!this.ineligibleSensors.isEmpty()) {
                android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> pair = calculateErrorByPriority();
                modality = 0 | ((com.android.server.biometrics.BiometricSensor) pair.first).modality;
                status = ((java.lang.Integer) pair.second).intValue();
            } else {
                modality = 0 | 1;
                status = 9;
            }
        } else if (this.mBiometricRequested) {
            if (!this.eligibleSensors.isEmpty()) {
                for (com.android.server.biometrics.BiometricSensor sensor2 : this.eligibleSensors) {
                    modality |= sensor2.modality;
                }
                if (modality == 8 && cameraPrivacyEnabled) {
                    status = 12;
                } else {
                    status = 1;
                }
            } else if (!this.ineligibleSensors.isEmpty()) {
                android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> pair2 = calculateErrorByPriority();
                modality = 0 | ((com.android.server.biometrics.BiometricSensor) pair2.first).modality;
                status = ((java.lang.Integer) pair2.second).intValue();
            } else {
                modality = 0 | 0;
                status = 2;
            }
        } else if (this.credentialRequested) {
            modality = 0 | 1;
            status = this.credentialAvailable ? 1 : 9;
        } else {
            android.util.Slog.e(TAG, "No authenticators requested");
            status = 2;
        }
        android.util.Slog.d(TAG, "getCanAuthenticateInternal Modality: " + modality + " AuthenticatorStatus: " + status);
        return new android.util.Pair<>(java.lang.Integer.valueOf(modality), java.lang.Integer.valueOf(status));
    }

    int getCanAuthenticateResult() {
        return com.android.server.biometrics.Utils.biometricConstantsToBiometricManager(com.android.server.biometrics.Utils.authenticatorStatusToBiometricConstant(((java.lang.Integer) getInternalStatus().second).intValue()));
    }

    android.util.Pair<java.lang.Integer, java.lang.Integer> getPreAuthenticateStatus() {
        android.util.Pair<java.lang.Integer, java.lang.Integer> internalStatus = getInternalStatus();
        int publicError = com.android.server.biometrics.Utils.authenticatorStatusToBiometricConstant(((java.lang.Integer) internalStatus.second).intValue());
        int modality = ((java.lang.Integer) internalStatus.first).intValue();
        switch (((java.lang.Integer) internalStatus.second).intValue()) {
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
                break;
            case 3:
            case 4:
            case 8:
            default:
                modality = 0;
                break;
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(modality), java.lang.Integer.valueOf(publicError));
    }

    boolean shouldShowCredential() {
        return this.credentialRequested && this.credentialAvailable;
    }

    int getEligibleModalities() {
        int modalities = 0;
        for (com.android.server.biometrics.BiometricSensor sensor : this.eligibleSensors) {
            modalities |= sensor.modality;
        }
        if (this.credentialRequested && this.credentialAvailable) {
            return modalities | 1;
        }
        return modalities;
    }

    int numSensorsWaitingForCookie() {
        int numWaiting = 0;
        for (com.android.server.biometrics.BiometricSensor sensor : this.eligibleSensors) {
            if (sensor.getSensorState() == 1) {
                android.util.Slog.d(TAG, "Sensor ID: " + sensor.id + " Waiting for cookie: " + sensor.getCookie());
                numWaiting++;
            }
        }
        return numWaiting;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder string = new java.lang.StringBuilder("BiometricRequested: " + this.mBiometricRequested + ", StrengthRequested: " + this.mBiometricStrengthRequested + ", CredentialRequested: " + this.credentialRequested);
        string.append(", Eligible:{");
        for (com.android.server.biometrics.BiometricSensor sensor : this.eligibleSensors) {
            string.append(sensor.id).append(" ");
        }
        string.append("}");
        string.append(", Ineligible:{");
        for (android.util.Pair<com.android.server.biometrics.BiometricSensor, java.lang.Integer> ineligible : this.ineligibleSensors) {
            string.append(ineligible.first).append(":").append(ineligible.second).append(" ");
        }
        string.append("}");
        string.append(", CredentialAvailable: ").append(this.credentialAvailable);
        string.append(", ");
        return string.toString();
    }
}
