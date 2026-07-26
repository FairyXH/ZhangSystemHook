package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public interface IFaceServiceExt {
    default void init() {
    }

    default void authPreOperation(android.os.IBinder token, java.lang.String opPackageName) {
    }

    default void scheduleEnroll() {
    }

    default void onSystemReady() {
    }

    default boolean isBiometricDisabled() {
        return false;
    }

    default void dumpInternal(com.android.server.biometrics.sensors.face.ServiceProvider provider, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default java.util.List<android.hardware.face.Face> getEnrolledFacesExcludePalms(java.util.List<android.hardware.face.Face> faces) {
        return faces;
    }
}
