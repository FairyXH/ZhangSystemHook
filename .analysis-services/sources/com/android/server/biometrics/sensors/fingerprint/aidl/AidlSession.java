package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class AidlSession {
    private final com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler mAidlResponseHandler;
    private final int mHalInterfaceVersion;
    private final android.hardware.biometrics.fingerprint.ISession mSession;
    private final int mUserId;

    public AidlSession(int halInterfaceVersion, android.hardware.biometrics.fingerprint.ISession session, int userId, com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mHalInterfaceVersion = halInterfaceVersion;
        this.mSession = session;
        this.mUserId = userId;
        this.mAidlResponseHandler = aidlResponseHandler;
    }

    public AidlSession(java.util.function.Supplier<android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint> session, int userId, com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mSession = new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter(session, userId, aidlResponseHandler);
        this.mHalInterfaceVersion = 0;
        this.mUserId = userId;
        this.mAidlResponseHandler = aidlResponseHandler;
    }

    public android.hardware.biometrics.fingerprint.ISession getSession() {
        return this.mSession;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler getHalSessionCallback() {
        return this.mAidlResponseHandler;
    }

    public boolean hasContextMethods() {
        return this.mHalInterfaceVersion >= 2;
    }
}
