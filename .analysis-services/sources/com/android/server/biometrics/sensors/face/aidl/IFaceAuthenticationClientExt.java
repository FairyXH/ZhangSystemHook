package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFaceAuthenticationClientExt {
    default void init(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, java.lang.String owner, int targetUserId, long operationId) {
    }

    default android.hardware.biometrics.common.ICancellationSignal startHalOperation() throws android.os.RemoteException {
        return null;
    }

    default boolean stopHalOperation() throws android.os.RemoteException {
        return false;
    }
}
