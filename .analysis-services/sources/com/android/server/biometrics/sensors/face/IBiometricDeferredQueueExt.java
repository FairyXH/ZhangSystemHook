package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricDeferredQueueExt {
    default boolean hasEnrolledPalms(int userId) {
        return false;
    }
}
