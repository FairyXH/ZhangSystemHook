package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprint21ServiceProviderExt {
    default void init(android.content.Context context, android.hardware.fingerprint.FingerprintSensorPropertiesInternal sensorProperties, com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl mLockoutTracker, com.android.server.biometrics.sensors.BiometricScheduler scheduler, android.os.Handler handler, int userId, java.util.function.Supplier<android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint> lazyDaemon, com.android.server.biometrics.log.BiometricContext biometricContext) {
    }

    default void initHidlToAidl(com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler aidlResponseHandler) {
    }

    default void setFingerKeymode(int enable, int sensorId) {
    }

    default void closeFingerKeymodeIfOpen() {
    }

    default int pauseEnroll(int sensorId) {
        return -1;
    }

    default int continueEnroll(int sensorId) {
        return -1;
    }

    default int getEnrollmentTotalTimes(int sensorId) {
        return -1;
    }

    default long getLockoutAttemptDeadline(int userId) {
        return -1L;
    }

    default int getFailedAttempts() {
        return -1;
    }

    default boolean dispatchOnAcquired(long deviceId, int acquiredInfo, int vendorCode) {
        return false;
    }

    default boolean dispatchOnAuthenticated(long deviceId, int fingerId, int groupId, java.util.ArrayList<java.lang.Byte> tokenByte) {
        return false;
    }

    default boolean dispatchOnError(long deviceId, int error, int vendorCode) {
        return false;
    }

    default void handleOnFingerprintCmd(int cmdId, java.util.ArrayList<java.lang.Byte> result, int resultLen) {
    }

    default int sendFingerprintCmd(int cmdId, byte[] inbuf) {
        return -1;
    }

    default boolean isClientCanAuth(android.os.IBinder token, java.lang.String opPackageName) {
        return false;
    }

    default boolean handleOnPointerDown() {
        return false;
    }

    default boolean handleOnPointerUp() {
        return false;
    }

    default boolean handleServiceDied() {
        return false;
    }

    default void onSystemReady() {
    }

    default void cancelFingerprintExtraInfo(android.os.IBinder token, java.lang.String opPkgName, long requstId) {
    }

    default void handleCancelAuthentication(int sensorId, android.os.IBinder token) {
    }

    default void handleCancelEnrollment(int sensorId, android.os.IBinder token) {
    }

    default void handleRemove(int sensorId, int fingerId, java.lang.String opPackageName, int userId) {
    }

    default void setUdfpsOverlayController(android.hardware.fingerprint.IUdfpsOverlayController controller) {
    }

    default void authPreOperation(android.os.IBinder token, java.lang.String opPackageName) {
    }

    default void enrollPreOperation(android.os.IBinder token, java.lang.String opPackageName, int userId) {
    }

    default void handleOnEnrollment(android.hardware.fingerprint.Fingerprint fingerprint, int remaining) {
    }

    default void dumpInternal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default void cancelTouchEventListener(android.os.IBinder token, java.lang.String opPkgName, long requstId) {
    }

    default void setOplusCallback(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint deamon) {
    }

    default void showFingerprintIcon(android.os.IBinder mToken, java.lang.String opPkgName) {
    }

    default void hideFingerprintIcon(android.os.IBinder mToken, java.lang.String opPkgName) {
    }

    default boolean isIconShow() {
        return false;
    }

    default void setScreenOffStateEarlyForkeyguardAuth() {
    }

    default void userSwitchNotice(android.content.Context context) {
    }
}
