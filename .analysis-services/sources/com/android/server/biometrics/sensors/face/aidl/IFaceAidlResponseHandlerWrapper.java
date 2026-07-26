package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IFaceAidlResponseHandlerWrapper {
    default com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerExt getExtImpl() {
        return new com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerExt() { // from class: com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerWrapper.1
        };
    }
}
