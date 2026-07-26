package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintAuthenticationClientExt {
    default void init(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint> lazyDaemon, int targetUserId, long operationId, java.lang.String owner) {
    }

    default boolean isMistakeTouchMode() {
        return false;
    }

    default boolean onHandleFailedAttempt(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl mLockoutFrameworkImpl, int userId) {
        return false;
    }

    default boolean startHalOperation() throws android.os.RemoteException {
        return false;
    }

    default void setIsNearState(boolean isNearState) {
    }

    default void onAuthenticated(boolean authenticated) {
    }
}
