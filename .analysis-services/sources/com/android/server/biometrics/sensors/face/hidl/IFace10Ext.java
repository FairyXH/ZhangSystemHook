package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFace10Ext {
    default void init(android.content.Context context, android.hardware.face.FaceSensorPropertiesInternal sensorProperties, com.android.server.biometrics.sensors.BiometricScheduler scheduler, android.os.Handler handler, int userId, java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> LazyDaemon) {
    }

    default long getLockoutAttemptDeadline(int userId) {
        return -1L;
    }

    default int getFailedAttempts() {
        return -1;
    }

    default void resetFaceDaemon() {
    }

    default int getFaceProcessMemory() {
        return -1;
    }

    default void handleOnFaceCmd(int cmdId, java.util.ArrayList<java.lang.Byte> result, int resultLen) {
    }

    default int scheduleSendFaceCmd(int sensorId, int cmdId, byte[] inbuf) {
        return -1;
    }

    default void authPreOperation(android.os.IBinder token, java.lang.String opPackageName) {
    }

    default void scheduleAuthenticate() {
    }

    default void onSystemReady() {
    }

    default void resetFaceLockout(byte[] token) {
    }

    default void setPreviewSurface(android.os.NativeHandle surfaceHandle) {
    }

    default void onAuthenticated(boolean authenticated) {
    }

    default void onError(int error, int vendorCode) {
    }

    default boolean onAcquired(int acquireInfo, int vendorCode) {
        return false;
    }

    default void setOplusCallback(android.hardware.biometrics.face.V1_0.IBiometricsFace deamon) {
    }

    default boolean isBackgroundAuthAllow(java.lang.String opPackageName) {
        return false;
    }

    default void onLockoutTimed() {
    }

    default void onLockoutPermanent() {
    }

    default void dumpInternal(java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default int regsiterFaceCmdCallback(android.hardware.face.IFaceCommandCallback callback) {
        return -1;
    }

    default int unregsiterFaceCmdCallback(android.hardware.face.IFaceCommandCallback callback) {
        return -1;
    }

    default boolean isSatelliteMode() {
        return false;
    }
}
