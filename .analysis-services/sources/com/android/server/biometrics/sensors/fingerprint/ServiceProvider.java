package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface ServiceProvider extends com.android.server.biometrics.sensors.BiometricServiceProvider<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> {
    void cancelAuthentication(int i, android.os.IBinder iBinder, long j);

    void cancelEnrollment(int i, android.os.IBinder iBinder, long j);

    android.hardware.biometrics.ITestSession createTestSession(int i, android.hardware.biometrics.ITestSessionCallback iTestSessionCallback, java.lang.String str);

    java.util.List<android.hardware.fingerprint.Fingerprint> getEnrolledFingerprints(int i, int i2);

    com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintServiceProviderExt getServiceProviderAidlEx();

    com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprint21ServiceProviderExt getServiceProviderEx();

    void onPointerDown(long j, int i, android.hardware.biometrics.fingerprint.PointerContext pointerContext);

    void onPointerUp(long j, int i, android.hardware.biometrics.fingerprint.PointerContext pointerContext);

    void onPowerPressed();

    void onUdfpsUiEvent(int i, long j, int i2);

    void rename(int i, int i2, int i3, java.lang.String str);

    long scheduleAuthenticate(android.os.IBinder iBinder, long j, int i, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, android.hardware.fingerprint.FingerprintAuthenticateOptions fingerprintAuthenticateOptions, boolean z, int i2, boolean z2);

    void scheduleAuthenticate(android.os.IBinder iBinder, long j, int i, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, android.hardware.fingerprint.FingerprintAuthenticateOptions fingerprintAuthenticateOptions, long j2, boolean z, int i2, boolean z2);

    long scheduleEnroll(int i, android.os.IBinder iBinder, byte[] bArr, int i2, android.hardware.fingerprint.IFingerprintServiceReceiver iFingerprintServiceReceiver, java.lang.String str, int i3, android.hardware.fingerprint.FingerprintEnrollOptions fingerprintEnrollOptions);

    long scheduleFingerDetect(android.os.IBinder iBinder, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter clientMonitorCallbackConverter, android.hardware.fingerprint.FingerprintAuthenticateOptions fingerprintAuthenticateOptions, int i);

    void scheduleGenerateChallenge(int i, int i2, android.os.IBinder iBinder, android.hardware.fingerprint.IFingerprintServiceReceiver iFingerprintServiceReceiver, java.lang.String str);

    void scheduleInternalCleanup(int i, int i2, com.android.server.biometrics.sensors.ClientMonitorCallback clientMonitorCallback);

    void scheduleInternalCleanup(int i, int i2, com.android.server.biometrics.sensors.ClientMonitorCallback clientMonitorCallback, boolean z);

    void scheduleInvalidateAuthenticatorId(int i, int i2, android.hardware.biometrics.IInvalidationCallback iInvalidationCallback);

    void scheduleRemove(int i, android.os.IBinder iBinder, android.hardware.fingerprint.IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, int i3, java.lang.String str);

    void scheduleRemoveAll(int i, android.os.IBinder iBinder, android.hardware.fingerprint.IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, java.lang.String str);

    void scheduleResetLockout(int i, int i2, byte[] bArr);

    void scheduleRevokeChallenge(int i, int i2, android.os.IBinder iBinder, java.lang.String str, long j);

    void setIgnoreDisplayTouches(long j, int i, boolean z);

    void setUdfpsOverlayController(android.hardware.fingerprint.IUdfpsOverlayController iUdfpsOverlayController);

    void startPreparedClient(int i, int i2);

    default void scheduleWatchdog(int sensorId) {
    }

    default void simulateVhalFingerDown(int userId, int sensorId) {
    }
}
