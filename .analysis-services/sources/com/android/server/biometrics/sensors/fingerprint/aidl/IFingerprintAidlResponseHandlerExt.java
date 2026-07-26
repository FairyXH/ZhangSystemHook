package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintAidlResponseHandlerExt {
    default void setProvider(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider) {
    }

    default com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider getProvider() {
        return null;
    }

    default boolean onAuthenticated(int mSensorId, int enrollmentId, int mgroupId, java.util.ArrayList<java.lang.Byte> tokenByte) {
        return false;
    }

    default boolean onError(int error, int vendorCode) {
        return false;
    }

    default boolean onAcquired(int acquireInfo, int vendorCode) {
        return false;
    }

    default void handleOnEnrollment(android.hardware.fingerprint.Fingerprint fingerprint, int remaining) {
    }

    default void onLockoutTimed() {
    }

    default void onLockoutPermanent() {
    }

    default boolean onTransactFromHal(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
        return false;
    }

    default void handleOnFingerprintCmd(int cmdId, byte[] result, int resultLen) {
    }

    default void handleOnEngineeringInfoUpdated(int length, java.util.ArrayList<java.lang.Integer> keys, java.util.ArrayList<java.lang.String> values) {
    }

    default void onInteractionDetected() {
    }
}
