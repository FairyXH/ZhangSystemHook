package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public final class FaceAuthenticator extends android.hardware.biometrics.IBiometricAuthenticator.Stub {
    private final android.hardware.face.IFaceService mFaceService;
    private final int mSensorId;

    public FaceAuthenticator(android.hardware.face.IFaceService faceService, int sensorId) {
        this.mFaceService = faceService;
        this.mSensorId = sensorId;
    }

    public android.hardware.biometrics.ITestSession createTestSession(android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFaceService.createTestSession(this.mSensorId, callback, opPackageName);
    }

    public android.hardware.biometrics.SensorPropertiesInternal getSensorProperties(java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFaceService.getSensorProperties(this.mSensorId, opPackageName);
    }

    public byte[] dumpSensorServiceStateProto(boolean clearSchedulerBuffer) throws android.os.RemoteException {
        return this.mFaceService.dumpSensorServiceStateProto(this.mSensorId, clearSchedulerBuffer);
    }

    public void prepareForAuthentication(boolean requireConfirmation, android.os.IBinder token, long operationId, int userId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, java.lang.String opPackageName, long requestId, int cookie, boolean allowBackgroundAuthentication, boolean isForLegacyFingerprintManager) throws android.os.RemoteException {
        this.mFaceService.prepareForAuthentication(requireConfirmation, token, operationId, sensorReceiver, new android.hardware.face.FaceAuthenticateOptions.Builder().setUserId(userId).setSensorId(this.mSensorId).setOpPackageName(opPackageName).build(), requestId, cookie, allowBackgroundAuthentication);
    }

    public void startPreparedClient(int cookie) throws android.os.RemoteException {
        this.mFaceService.startPreparedClient(this.mSensorId, cookie);
    }

    public void cancelAuthenticationFromService(android.os.IBinder token, java.lang.String opPackageName, long requestId) throws android.os.RemoteException {
        this.mFaceService.cancelAuthenticationFromService(this.mSensorId, token, opPackageName, requestId);
    }

    public boolean isHardwareDetected(java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFaceService.isHardwareDetected(this.mSensorId, opPackageName);
    }

    public boolean hasEnrolledTemplates(int userId, java.lang.String opPackageName) throws android.os.RemoteException {
        return this.mFaceService.hasEnrolledFaces(this.mSensorId, userId, opPackageName);
    }

    public void invalidateAuthenticatorId(int userId, android.hardware.biometrics.IInvalidationCallback callback) throws android.os.RemoteException {
        this.mFaceService.invalidateAuthenticatorId(this.mSensorId, userId, callback);
    }

    public int getLockoutModeForUser(int userId) throws android.os.RemoteException {
        return this.mFaceService.getLockoutModeForUser(this.mSensorId, userId);
    }

    public long getAuthenticatorId(int callingUserId) throws android.os.RemoteException {
        return this.mFaceService.getAuthenticatorId(this.mSensorId, callingUserId);
    }

    public void resetLockout(android.os.IBinder token, java.lang.String opPackageName, int userId, byte[] hardwareAuthToken) throws android.os.RemoteException {
        this.mFaceService.resetLockout(token, this.mSensorId, userId, hardwareAuthToken, opPackageName);
    }
}
