package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceResetLockoutClient extends com.android.server.biometrics.sensors.HalClientMonitor<com.android.server.biometrics.sensors.face.aidl.AidlSession> implements com.android.server.biometrics.sensors.ErrorConsumer {
    private static final java.lang.String TAG = "FaceResetLockoutClient";
    private final int mBiometricStrength;
    com.android.server.biometrics.sensors.face.IFaceResetLockoutClientExt mFaceResetLockoutClientExt;
    private final android.hardware.keymaster.HardwareAuthToken mHardwareAuthToken;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private final com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker;

    public FaceResetLockoutClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, byte[] hardwareAuthToken, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, int biometricStrength) {
        super(context, lazyDaemon, null, null, userId, owner, 0, sensorId, logger, biometricContext);
        this.mFaceResetLockoutClientExt = (com.android.server.biometrics.sensors.face.IFaceResetLockoutClientExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.IFaceResetLockoutClientExt.class).base(this).create();
        this.mHardwareAuthToken = com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(hardwareAuthToken);
        this.mLockoutTracker = lockoutTracker;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mBiometricStrength = biometricStrength;
        this.mFaceResetLockoutClientExt.init(context, lazyDaemon, this.mHardwareAuthToken);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        try {
            if (this.mFaceResetLockoutClientExt != null) {
                this.mFaceResetLockoutClientExt.startHalOperation();
            } else {
                android.hardware.biometrics.face.ISession session = getFreshDaemon().getSession();
                session.resetLockout(this.mHardwareAuthToken);
                if (session instanceof com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter) {
                    this.mCallback.onClientFinished(this, true);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Unable to reset lockout", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    void onLockoutCleared() {
        resetLocalLockoutStateToNone(getSensorId(), getTargetUserId(), this.mLockoutTracker, this.mLockoutResetDispatcher, getBiometricContext().getAuthSessionCoordinator(), this.mBiometricStrength, getRequestId());
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    static void resetLocalLockoutStateToNone(int sensorId, int userId, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator, int biometricStrength, long requestId) {
        authSessionCoordinator.resetLockoutFor(userId, biometricStrength, requestId);
        lockoutTracker.setLockoutModeForUser(userId, 0);
        lockoutResetDispatcher.notifyLockoutResetCallbacks(sensorId);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 12;
    }

    @Override // com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        android.util.Slog.e(TAG, "Error during resetLockout: " + errorCode);
        this.mCallback.onClientFinished(this, false);
    }
}
