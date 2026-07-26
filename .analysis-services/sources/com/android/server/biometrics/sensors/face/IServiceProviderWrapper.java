package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public interface IServiceProviderWrapper {
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

    default int scheduleSendFaceCmd(int sensorId, int cmdId, byte[] inbuf) {
        return -1;
    }

    default void authPreOperation(android.os.IBinder token, java.lang.String opPackageName) {
    }

    default void onSystemReady() {
    }

    default void onAuthenticated(boolean authenticated) {
    }

    default void onError(int error, int vendorCode) {
    }

    default boolean onAcquired(int acquireInfo, int vendorCode) {
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
}
