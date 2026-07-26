package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintAuthenticationClientAidlExt {
    default void init(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, int targetUserId, long operationId, java.lang.String owner) {
    }

    default boolean isMistakeTouchMode() {
        return false;
    }

    default boolean onHandleFailedAttempt(com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker, int userId) {
        return false;
    }

    default android.hardware.biometrics.common.ICancellationSignal startHalOperation() throws android.os.RemoteException {
        return null;
    }

    default void setIsNearState(boolean isNearState) {
    }

    default void onAuthenticated(boolean authenticated) {
    }
}
