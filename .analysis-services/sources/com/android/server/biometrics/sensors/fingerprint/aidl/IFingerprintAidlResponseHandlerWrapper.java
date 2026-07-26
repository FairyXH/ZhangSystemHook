package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintAidlResponseHandlerWrapper {
    default com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerExt getExtImpl() {
        return new com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerExt() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerWrapper.1
        };
    }
}
