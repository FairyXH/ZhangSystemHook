package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public interface IFaceServiceWrapper {
    default com.android.server.biometrics.sensors.face.IFaceServiceExt getExtImpl() {
        return new com.android.server.biometrics.sensors.face.IFaceServiceExt() { // from class: com.android.server.biometrics.sensors.face.IFaceServiceWrapper.1
        };
    }

    default void setExtensionWrapper(android.os.IBinder extension) {
    }

    default com.android.server.biometrics.sensors.face.ServiceProvider getProviderForSensorWrapper(int sensorId) {
        return null;
    }

    default android.util.Pair<java.lang.Integer, com.android.server.biometrics.sensors.face.ServiceProvider> getSingleProviderWrapper() {
        return null;
    }
}
