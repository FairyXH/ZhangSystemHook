package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintServiceProviderExt {
    default void init(android.content.Context context, java.lang.String halInstanceName, android.os.Handler handler, com.android.server.biometrics.sensors.SensorList<com.android.server.biometrics.sensors.fingerprint.aidl.Sensor> sensors, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider) {
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

    default void handleOnFingerprintCmd(int cmdId, byte[] result, int resultLen) {
    }

    default void onEngineeringInfoUpdated(int length, java.util.ArrayList<java.lang.Integer> keysArray, java.util.ArrayList<java.lang.String> valuesArray) {
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

    default int getAuthRegisteredResult(java.lang.String opPkgName) {
        return -1;
    }

    default boolean isSideFingerprintInitialized() {
        return true;
    }

    default boolean onAcquired(int acquiredInfo, int vendorCode) {
        return false;
    }

    default boolean onAuthenticated(int sensorId, int fingerId, int groupId, java.util.ArrayList<java.lang.Byte> tokenByte) {
        return false;
    }

    default boolean onError(int error, int vendorCode) {
        return false;
    }

    default void onLockoutPermanent() {
    }

    default void onLockoutTimed() {
    }

    default void resetFingerprintLockout(byte[] token, int userId) {
    }

    default boolean onTransactFromHal(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
        return false;
    }

    default void notifyHalReady() {
    }

    default android.os.Handler createHandlerWithNewLooper() {
        return null;
    }

    default void setScreenOffStateEarlyForkeyguardAuth() {
    }

    default void userSwitchNotice(android.content.Context context) {
    }

    default void onInteractionDetected() {
    }
}
