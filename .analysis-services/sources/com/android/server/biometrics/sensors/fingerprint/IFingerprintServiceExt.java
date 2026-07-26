package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintServiceExt {
    default void setBinderExtension(android.os.Binder extensionService) {
    }

    default boolean enrollPreOperation(android.os.IBinder token, java.lang.String opPackageName, int userId) {
        return false;
    }

    default boolean authPreOperation(android.os.IBinder token, java.lang.String opPackageName, int sensorId) {
        return false;
    }

    default void notifyOperationCanceled(android.hardware.fingerprint.IFingerprintServiceReceiver receiver) {
    }

    default boolean prepareForAuthPreOperation(android.os.IBinder token, java.lang.String opPackageName) {
        return false;
    }

    default void dumpInternal(com.android.server.biometrics.sensors.fingerprint.ServiceProvider provider, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default int changeUserIdIfNeeded(int userId) {
        return userId;
    }

    default void onSystemReady() {
    }

    default boolean skipAuthWithPrompt(java.lang.String opPackageName) {
        return false;
    }

    default boolean isBiometricDisabled() {
        return false;
    }

    default android.os.Handler createHandlerWithNewLooper() {
        return null;
    }

    default void setSkipAuthPrompt(boolean skip, java.lang.String opPackageName) {
    }
}
