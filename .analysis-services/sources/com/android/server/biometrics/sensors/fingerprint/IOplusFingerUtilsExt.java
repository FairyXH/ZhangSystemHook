package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusFingerUtilsExt {
    default void notifyResetLockoutAttemptDeadline(long deadline, int userId) {
    }

    default int getReasonForCloneSystem(int reason) {
        return reason;
    }
}
