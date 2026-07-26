package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public interface ServiceProvider extends com.android.server.biometrics.sensors.BiometricServiceProvider<android.hardware.face.FaceSensorPropertiesInternal> {
    void cancelAuthentication(int i, android.os.IBinder iBinder, long j);

    void cancelEnrollment(int i, android.os.IBinder iBinder, long j);

    void cancelFaceDetect(int i, android.os.IBinder iBinder, long j);

    android.hardware.biometrics.ITestSession createTestSession(int i, android.hardware.biometrics.ITestSessionCallback iTestSessionCallback, java.lang.String str);

    void dumpHal(int i, java.io.FileDescriptor fileDescriptor, java.lang.String[] strArr);

    java.util.List<android.hardware.face.Face> getEnrolledFaces(int i, int i2);

    com.android.server.biometrics.sensors.face.IServiceProviderWrapper getServiceProviderWrapper();

    long scheduleAuthenticate(android.os.IBinder iBinder, long j, int i, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, android.hardware.face.FaceAuthenticateOptions faceAuthenticateOptions, boolean z, int i2, boolean z2);

    void scheduleAuthenticate(android.os.IBinder iBinder, long j, int i, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, android.hardware.face.FaceAuthenticateOptions faceAuthenticateOptions, long j2, boolean z, int i2, boolean z2);

    long scheduleEnroll(int i, android.os.IBinder iBinder, byte[] bArr, int i2, android.hardware.face.IFaceServiceReceiver iFaceServiceReceiver, java.lang.String str, int[] iArr, android.view.Surface surface, boolean z, android.hardware.face.FaceEnrollOptions faceEnrollOptions);

    long scheduleFaceDetect(android.os.IBinder iBinder, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, android.hardware.face.FaceAuthenticateOptions faceAuthenticateOptions, int i);

    void scheduleGenerateChallenge(int i, int i2, android.os.IBinder iBinder, android.hardware.face.IFaceServiceReceiver iFaceServiceReceiver, java.lang.String str);

    void scheduleGetFeature(int i, android.os.IBinder iBinder, int i2, int i3, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, java.lang.String str);

    void scheduleInternalCleanup(int i, int i2, com.android.server.biometrics.sensors.ClientMonitorCallback clientMonitorCallback);

    void scheduleInternalCleanup(int i, int i2, com.android.server.biometrics.sensors.ClientMonitorCallback clientMonitorCallback, boolean z);

    void scheduleRemove(int i, android.os.IBinder iBinder, int i2, int i3, android.hardware.face.IFaceServiceReceiver iFaceServiceReceiver, java.lang.String str);

    void scheduleRemoveAll(int i, android.os.IBinder iBinder, int i2, android.hardware.face.IFaceServiceReceiver iFaceServiceReceiver, java.lang.String str);

    void scheduleResetLockout(int i, int i2, byte[] bArr);

    void scheduleRevokeChallenge(int i, int i2, android.os.IBinder iBinder, java.lang.String str, long j);

    void scheduleSetFeature(int i, android.os.IBinder iBinder, int i2, int i3, boolean z, byte[] bArr, android.hardware.face.IFaceServiceReceiver iFaceServiceReceiver, java.lang.String str);

    void startPreparedClient(int i, int i2);

    default void scheduleInvalidateAuthenticatorId(int sensorId, int userId, android.hardware.biometrics.IInvalidationCallback callback) {
        throw new java.lang.IllegalStateException("Providers that support invalidation must override this method");
    }

    default void scheduleWatchdog(int sensorId) {
    }
}
