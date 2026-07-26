package com.android.server.biometrics.sensors.iris;

/* JADX INFO: loaded from: classes.dex */
public final class IrisAuthenticator extends android.hardware.biometrics.IBiometricAuthenticator.Stub {
    private final android.hardware.iris.IIrisService mIrisService;

    public IrisAuthenticator(android.hardware.iris.IIrisService irisService, int sensorId) {
        this.mIrisService = irisService;
    }

    public android.hardware.biometrics.ITestSession createTestSession(android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) throws android.os.RemoteException {
        return null;
    }

    public android.hardware.biometrics.SensorPropertiesInternal getSensorProperties(java.lang.String opPackageName) throws android.os.RemoteException {
        return null;
    }

    public byte[] dumpSensorServiceStateProto(boolean clearSchedulerBuffer) throws android.os.RemoteException {
        return null;
    }

    public void prepareForAuthentication(boolean requireConfirmation, android.os.IBinder token, long sessionId, int userId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, java.lang.String opPackageName, long requestId, int cookie, boolean allowBackgroundAuthentication, boolean isForLegacyFingerprintManager) throws android.os.RemoteException {
    }

    public void startPreparedClient(int cookie) throws android.os.RemoteException {
    }

    public void cancelAuthenticationFromService(android.os.IBinder token, java.lang.String opPackageName, long requestId) throws android.os.RemoteException {
    }

    public boolean isHardwareDetected(java.lang.String opPackageName) throws android.os.RemoteException {
        return false;
    }

    public boolean hasEnrolledTemplates(int userId, java.lang.String opPackageName) throws android.os.RemoteException {
        return false;
    }

    public int getLockoutModeForUser(int userId) throws android.os.RemoteException {
        return 0;
    }

    public void invalidateAuthenticatorId(int userId, android.hardware.biometrics.IInvalidationCallback callback) {
    }

    public long getAuthenticatorId(int callingUserId) throws android.os.RemoteException {
        return 0L;
    }

    public void resetLockout(android.os.IBinder token, java.lang.String opPackageName, int userId, byte[] hardwareAuthToken) throws android.os.RemoteException {
    }
}
