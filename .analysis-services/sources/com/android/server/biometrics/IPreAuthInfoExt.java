package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public interface IPreAuthInfoExt {
    default boolean needSkipEligibleSensorAdd(com.android.server.biometrics.BiometricSensor mSensor, int userId, java.lang.String pkg, android.content.Context context, android.hardware.biometrics.PromptInfo promptInfo) {
        return false;
    }
}
