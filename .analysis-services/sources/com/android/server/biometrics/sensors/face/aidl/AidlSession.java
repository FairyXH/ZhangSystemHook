package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class AidlSession {
    private final com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler mAidlResponseHandler;
    private final int mHalInterfaceVersion;
    private final android.hardware.biometrics.face.ISession mSession;
    private final int mUserId;

    public AidlSession(int halInterfaceVersion, android.hardware.biometrics.face.ISession session, int userId, com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mHalInterfaceVersion = halInterfaceVersion;
        this.mSession = session;
        this.mUserId = userId;
        this.mAidlResponseHandler = aidlResponseHandler;
    }

    public AidlSession(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> session, int userId, com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mSession = new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter(context, session, userId, aidlResponseHandler);
        this.mHalInterfaceVersion = 0;
        this.mUserId = userId;
        this.mAidlResponseHandler = aidlResponseHandler;
    }

    public android.hardware.biometrics.face.ISession getSession() {
        return this.mSession;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler getHalSessionCallback() {
        return this.mAidlResponseHandler;
    }

    public boolean hasContextMethods() {
        return this.mHalInterfaceVersion >= 2;
    }

    public boolean supportsFaceEnrollOptions() {
        return this.mHalInterfaceVersion >= 4;
    }
}
