package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFaceAidlResponseHandlerExt {
    default void setProvider(com.android.server.biometrics.sensors.face.aidl.FaceProvider provider) {
    }

    default com.android.server.biometrics.sensors.face.aidl.FaceProvider getProvider() {
        return null;
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

    default boolean onTransactFromHal(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
        return false;
    }
}
