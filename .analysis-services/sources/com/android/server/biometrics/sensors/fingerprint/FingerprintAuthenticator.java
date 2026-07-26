package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public final class FingerprintAuthenticator extends android.hardware.biometrics.IBiometricAuthenticator.Stub {
    private final android.hardware.fingerprint.IFingerprintService mFingerprintService;
    private final int mSensorId;

    public FingerprintAuthenticator(android.hardware.fingerprint.IFingerprintService fingerprintService, int sensorId) {
        this.mFingerprintService = fingerprintService;
        this.mSensorId = sensorId;
    }

    public android.hardware.biometrics.ITestSession createTestSession(android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFingerprintService.createTestSession(this.mSensorId, callback, opPackageName);
    }

    public android.hardware.biometrics.SensorPropertiesInternal getSensorProperties(java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFingerprintService.getSensorProperties(this.mSensorId, opPackageName);
    }

    public byte[] dumpSensorServiceStateProto(boolean clearSchedulerBuffer) throws android.os.RemoteException {
        return this.mFingerprintService.dumpSensorServiceStateProto(this.mSensorId, clearSchedulerBuffer);
    }

    public void prepareForAuthentication(boolean requireConfirmation, android.os.IBinder token, long operationId, int userId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, java.lang.String opPackageName, long requestId, int cookie, boolean allowBackgroundAuthentication, boolean isForLegacyFingerprintManager) throws android.os.RemoteException {
        this.mFingerprintService.prepareForAuthentication(token, operationId, sensorReceiver, new android.hardware.fingerprint.FingerprintAuthenticateOptions.Builder().setSensorId(this.mSensorId).setUserId(userId).setOpPackageName(opPackageName).build(), requestId, cookie, allowBackgroundAuthentication, isForLegacyFingerprintManager);
    }

    public void startPreparedClient(int cookie) throws android.os.RemoteException {
        this.mFingerprintService.startPreparedClient(this.mSensorId, cookie);
    }

    public void cancelAuthenticationFromService(android.os.IBinder token, java.lang.String opPackageName, long requestId) throws android.os.RemoteException {
        this.mFingerprintService.cancelAuthenticationFromService(this.mSensorId, token, opPackageName, requestId);
    }

    public boolean isHardwareDetected(java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFingerprintService.isHardwareDetected(this.mSensorId, opPackageName);
    }

    public boolean hasEnrolledTemplates(int userId, java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFingerprintService.hasEnrolledFingerprints(this.mSensorId, userId, opPackageName);
    }

    public int getLockoutModeForUser(int userId) throws android.os.RemoteException {
        return this.mFingerprintService.getLockoutModeForUser(this.mSensorId, userId);
    }

    public void invalidateAuthenticatorId(int userId, android.hardware.biometrics.IInvalidationCallback callback) throws android.os.RemoteException {
        this.mFingerprintService.invalidateAuthenticatorId(this.mSensorId, userId, callback);
    }

    public long getAuthenticatorId(int callingUserId) throws android.os.RemoteException {
        return this.mFingerprintService.getAuthenticatorId(this.mSensorId, callingUserId);
    }

    public void resetLockout(android.os.IBinder token, java.lang.String opPackageName, int userId, byte[] hardwareAuthToken) throws android.os.RemoteException {
        this.mFingerprintService.resetLockout(token, this.mSensorId, userId, hardwareAuthToken, opPackageName);
    }
}
