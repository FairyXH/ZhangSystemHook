package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IUdfpsHelperExt {
    default void preShowUdfpsOverlay(int sensor, int reason) {
    }

    default void preHideUdfpsOverlay(int sensor) {
    }

    default void init(android.content.Context context) {
    }
}
