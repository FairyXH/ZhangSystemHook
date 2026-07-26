package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public class HidlToAidlSessionAdapter implements android.hardware.biometrics.fingerprint.ISession {
    static final int ENROLL_TIMEOUT_SEC = 60;
    public static com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprint21ServiceProviderExt mFingerprint21ServiceProviderExt = (com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprint21ServiceProviderExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprint21ServiceProviderExt.class).create();
    private final java.lang.String TAG = "HidlToAidlSessionAdapter";
    private com.android.server.biometrics.sensors.IBaseClientMonitorExt mBaseClientMonitorExt = (com.android.server.biometrics.sensors.IBaseClientMonitorExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.IBaseClientMonitorExt.class).create();
    private com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlCallbackConverter mHidlToAidlCallbackConverter;
    private final java.util.function.Supplier<android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint> mSession;
    private final int mUserId;

    public HidlToAidlSessionAdapter(java.util.function.Supplier<android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint> session, int userId, com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mSession = session;
        this.mUserId = this.mBaseClientMonitorExt.hookTargetUserId(userId);
        setCallback(aidlResponseHandler);
        mFingerprint21ServiceProviderExt.initHidlToAidl(aidlResponseHandler);
    }

    public android.os.IBinder asBinder() {
        return null;
    }

    public void generateChallenge() throws android.os.RemoteException {
        if (this.mSession.get() != null) {
            long challenge = this.mSession.get().preEnroll();
            this.mHidlToAidlCallbackConverter.onChallengeGenerated(challenge);
        } else {
            android.util.Slog.e("HidlToAidlSessionAdapter", "Unable to preEnroll HIDL. HIDL daemon is null.");
        }
    }

    public void revokeChallenge(long challenge) throws android.os.RemoteException {
        this.mSession.get().postEnroll();
        this.mHidlToAidlCallbackConverter.onChallengeRevoked(0L);
    }

    public android.hardware.biometrics.common.ICancellationSignal enroll(android.hardware.keymaster.HardwareAuthToken hat) throws android.os.RemoteException {
        this.mSession.get().enroll(com.android.server.biometrics.HardwareAuthTokenUtils.toByteArray(hat), this.mUserId, 60);
        return new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter.Cancellation();
    }

    public android.hardware.biometrics.common.ICancellationSignal authenticate(long operationId) throws android.os.RemoteException {
        this.mSession.get().authenticate(operationId, this.mUserId);
        return new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter.Cancellation();
    }

    public android.hardware.biometrics.common.ICancellationSignal detectInteraction() throws android.os.RemoteException {
        this.mSession.get().authenticate(0L, this.mUserId);
        return new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter.Cancellation();
    }

    public void enumerateEnrollments() throws android.os.RemoteException {
        if (this.mSession.get() != null) {
            this.mSession.get().enumerate();
        } else {
            android.util.Slog.e("HidlToAidlSessionAdapter", "Unable to HIDL  enumerate. HIDL daemon is null.");
        }
    }

    public void removeEnrollments(int[] enrollmentIds) throws android.os.RemoteException {
        if (enrollmentIds.length > 1) {
            this.mSession.get().remove(this.mUserId, 0);
        } else {
            this.mSession.get().remove(this.mUserId, enrollmentIds[0]);
        }
    }

    public void onPointerDown(int pointerId, int x, int y, float minor, float major) throws android.os.RemoteException {
        com.android.server.biometrics.sensors.fingerprint.UdfpsHelper.onFingerDown(this.mSession.get(), x, y, minor, major);
    }

    public void onPointerUp(int pointerId) throws android.os.RemoteException {
        com.android.server.biometrics.sensors.fingerprint.UdfpsHelper.onFingerUp(this.mSession.get());
    }

    public void getAuthenticatorId() throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "getAuthenticatorId unsupported in HIDL");
        this.mHidlToAidlCallbackConverter.unsupportedClientScheduled(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGetAuthenticatorIdClient.class);
    }

    public void invalidateAuthenticatorId() throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "invalidateAuthenticatorId unsupported in HIDL");
        this.mHidlToAidlCallbackConverter.unsupportedClientScheduled(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInvalidationClient.class);
    }

    public void resetLockout(android.hardware.keymaster.HardwareAuthToken hat) throws android.os.RemoteException {
        this.mHidlToAidlCallbackConverter.onResetLockout();
    }

    public void close() throws android.os.RemoteException {
        android.util.Log.d("HidlToAidlSessionAdapter", "cancel() was called for hidl fingerprint");
        this.mSession.get().cancel();
    }

    public void onUiReady() throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "onUiReady unsupported in HIDL");
    }

    public android.hardware.biometrics.common.ICancellationSignal authenticateWithContext(long operationId, android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "authenticateWithContext unsupported in HIDL");
        return authenticate(operationId);
    }

    public android.hardware.biometrics.common.ICancellationSignal enrollWithContext(android.hardware.keymaster.HardwareAuthToken hat, android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "enrollWithContext unsupported in HIDL");
        return enroll(hat);
    }

    public android.hardware.biometrics.common.ICancellationSignal detectInteractionWithContext(android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "enrollWithContext unsupported in HIDL");
        return detectInteraction();
    }

    public void onPointerDownWithContext(android.hardware.biometrics.fingerprint.PointerContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "onPointerDownWithContext unsupported in HIDL");
        onPointerDown(context.pointerId, (int) context.x, (int) context.y, context.minor, context.major);
    }

    public void onPointerUpWithContext(android.hardware.biometrics.fingerprint.PointerContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "onPointerUpWithContext unsupported in HIDL");
        onPointerUp(context.pointerId);
    }

    public void onContextChanged(android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "onContextChanged unsupported in HIDL");
    }

    public void onPointerCancelWithContext(android.hardware.biometrics.fingerprint.PointerContext context) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "onPointerCancelWithContext unsupported in HIDL");
    }

    public void setIgnoreDisplayTouches(boolean shouldIgnore) throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "setIgnoreDisplayTouches unsupported in HIDL");
    }

    public int getInterfaceVersion() throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "getInterfaceVersion unsupported in HIDL");
        return 0;
    }

    public java.lang.String getInterfaceHash() throws android.os.RemoteException {
        android.util.Log.e("HidlToAidlSessionAdapter", "getInterfaceHash unsupported in HIDL");
        return null;
    }

    public long getAuthenticatorIdForUpdateClient() throws android.os.RemoteException {
        return this.mSession.get().getAuthenticatorId();
    }

    public void setActiveGroup(int userId, java.lang.String absolutePath) throws android.os.RemoteException {
        if (this.mSession.get() != null) {
            this.mSession.get().setActiveGroup(userId, absolutePath);
        } else {
            android.util.Slog.e("HidlToAidlSessionAdapter", "Unable to set HIDL setActiveGroup. HIDL daemon is null.");
        }
    }

    public int sendFingerprintCmd(int cmdId, java.util.ArrayList<java.lang.Byte> byteArrayList) {
        if (this.mSession == null || this.mSession.get() == null || !this.mSession.get().toString().contains("vendor.oplus.hardware.biometrics.fingerprint@2.1")) {
            return -1;
        }
        vendor.oplus.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint extension = vendor.oplus.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.castFrom((android.os.IHwInterface) this.mSession.get());
        if (extension == null) {
            android.util.Slog.e("HidlToAidlSessionAdapter", "[sendFingerprintCmd]: cmdId:" + cmdId + " can not send to finger hal");
            return -1;
        }
        try {
            int mRes = extension.sendFingerprintCmd(cmdId, byteArrayList);
            return mRes;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e("HidlToAidlSessionAdapter", "[sendFingerprintCmd]: cmdId:" + cmdId + "failed", e);
            return -1;
        }
    }

    public java.util.function.Supplier<android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint> getSession() {
        return this.mSession;
    }

    private void setCallback(com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mHidlToAidlCallbackConverter = new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlCallbackConverter(aidlResponseHandler);
        try {
            if (this.mSession.get() == null) {
                android.util.Slog.e("HidlToAidlSessionAdapter", "Unable to set HIDL callback. HIDL daemon is null.");
                return;
            }
            long halId = this.mSession.get().setNotify(this.mHidlToAidlCallbackConverter);
            android.util.Slog.d("HidlToAidlSessionAdapter", "Fingerprint HAL ready, HAL ID: " + halId);
            if (halId == 0) {
                android.util.Slog.d("HidlToAidlSessionAdapter", "Unable to set HIDL callback.");
            }
            mFingerprint21ServiceProviderExt.setOplusCallback(this.mSession.get());
        } catch (android.os.RemoteException e) {
            android.util.Slog.d("HidlToAidlSessionAdapter", "Failed to set callback");
        }
    }

    private class Cancellation extends android.hardware.biometrics.common.ICancellationSignal.Stub {
        Cancellation() {
        }

        public void cancel() throws android.os.RemoteException {
            try {
                ((android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint) com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter.this.mSession.get()).cancel();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e("HidlToAidlSessionAdapter", "Remote exception when requesting cancel", e);
            }
        }

        public int getInterfaceVersion() throws android.os.RemoteException {
            return 0;
        }

        public java.lang.String getInterfaceHash() throws android.os.RemoteException {
            return null;
        }
    }
}
