package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface IClientMonitorCallbackConverterExt {
    default void notifyFaceAuthenticationResult(boolean state) {
    }

    default void notifyFingerprintAuthenticationResult(boolean state) {
    }
}
