package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricServiceExt {
    default android.os.Handler createHandlerWithNewLooper() {
        return null;
    }

    default void resetLockoutTimeBound(com.android.server.biometrics.log.BiometricContext biometricContext, int modality, int userId) {
    }
}
