package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public interface IFaceResetLockoutClientExt {
    default void init(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.hardware.keymaster.HardwareAuthToken hardwareAuthToken) {
    }

    default void startHalOperation() throws android.os.RemoteException {
    }
}
