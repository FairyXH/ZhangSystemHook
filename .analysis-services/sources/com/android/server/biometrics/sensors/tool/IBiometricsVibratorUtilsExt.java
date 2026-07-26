package com.android.server.biometrics.sensors.tool;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricsVibratorUtilsExt {

    public interface IStaticExt {
        default boolean vibrateFingerprintSuccess(android.content.Context context, com.android.server.biometrics.sensors.AcquisitionClient client) {
            return false;
        }

        default boolean vibrateFingerprintError(android.content.Context context, com.android.server.biometrics.sensors.AcquisitionClient client) {
            return false;
        }

        default void init(android.content.Context context) {
        }
    }
}
