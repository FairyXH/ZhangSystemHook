package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintServiceWrapper {
    default com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceExt getExtImpl() {
        return new com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceExt() { // from class: com.android.server.biometrics.sensors.fingerprint.IFingerprintServiceWrapper.1
        };
    }

    default com.android.server.biometrics.sensors.fingerprint.ServiceProvider getProviderForSensorWrapper(int sensorId) {
        return null;
    }

    default android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.fingerprint.ServiceProvider> getSingleProviderWrapper() {
        return null;
    }

    default boolean canUseFingerprintWrapper(java.lang.String opPackageName, java.lang.String attributionTag, boolean requireForeground, int uid, int pid, int userId) {
        return true;
    }
}
