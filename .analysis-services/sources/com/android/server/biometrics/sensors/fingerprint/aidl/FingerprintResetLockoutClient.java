package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintResetLockoutClient extends com.android.server.biometrics.sensors.HalClientMonitor<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> implements com.android.server.biometrics.sensors.ErrorConsumer {
    private static final java.lang.String TAG = "FingerprintResetLockoutClient";
    private final int mBiometricStrength;
    private final android.hardware.keymaster.HardwareAuthToken mHardwareAuthToken;
    private final com.android.server.biometrics.sensors.LockoutTracker mLockoutCache;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;

    public FingerprintResetLockoutClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext, byte[] hardwareAuthToken, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, int biometricStrength) {
        super(context, lazyDaemon, null, null, userId, owner, 0, sensorId, biometricLogger, biometricContext);
        this.mHardwareAuthToken = hardwareAuthToken == null ? null : com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(hardwareAuthToken);
        this.mLockoutCache = lockoutTracker;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mBiometricStrength = biometricStrength;
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
            getFreshDaemon().getSession().resetLockout(this.mHardwareAuthToken);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to reset lockout", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    void onLockoutCleared() {
        resetLocalLockoutStateToNone(getSensorId(), getTargetUserId(), this.mLockoutCache, this.mLockoutResetDispatcher, getBiometricContext().getAuthSessionCoordinator(), this.mBiometricStrength, getRequestId());
        this.mCallback.onClientFinished(this, true);
    }

    static void resetLocalLockoutStateToNone(int sensorId, int userId, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator, int biometricStrength, long requestId) {
        lockoutTracker.resetFailedAttemptsForUser(true, userId);
        lockoutTracker.setLockoutModeForUser(userId, 0);
        lockoutResetDispatcher.notifyLockoutResetCallbacks(sensorId);
        authSessionCoordinator.resetLockoutFor(userId, biometricStrength, requestId);
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
