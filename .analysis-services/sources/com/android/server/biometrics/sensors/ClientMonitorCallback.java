package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface ClientMonitorCallback {
    default void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
    }

    default void onBiometricAction(int action) {
    }

    default void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
    }
}
