package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public abstract class BiometricSensor {
    static final int STATE_AUTHENTICATING = 3;
    static final int STATE_CANCELING = 4;
    static final int STATE_COOKIE_RETURNED = 2;
    static final int STATE_STOPPED = 5;
    static final int STATE_UNKNOWN = 0;
    static final int STATE_WAITING_FOR_COOKIE = 1;
    private static final java.lang.String TAG = "BiometricService/Sensor";
    public final int id;
    public final android.hardware.biometrics.IBiometricAuthenticator impl;
    private final android.content.Context mContext;
    private int mCookie;
    private int mError;
    private int mSensorState;
    private int mUpdatedStrength;
    public final int modality;
    public final int oemStrength;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface SensorState {
    }

    abstract boolean confirmationAlwaysRequired(int i);

    abstract boolean confirmationSupported();

    BiometricSensor(android.content.Context context, int id, int modality, int strength, android.hardware.biometrics.IBiometricAuthenticator impl) {
        this.mContext = context;
        this.id = id;
        this.modality = modality;
        this.oemStrength = strength;
        this.impl = impl;
        this.mUpdatedStrength = strength;
        goToStateUnknown();
    }

    void goToStateUnknown() {
        this.mSensorState = 0;
        this.mCookie = 0;
        this.mError = 0;
    }

    void goToStateWaitingForCookie(boolean requireConfirmation, android.os.IBinder token, long sessionId, int userId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, java.lang.String opPackageName, long requestId, int cookie, boolean allowBackgroundAuthentication, boolean isForLegacyFingerprintManager) throws android.os.RemoteException {
        this.mCookie = cookie;
        this.impl.prepareForAuthentication(requireConfirmation, token, sessionId, userId, sensorReceiver, opPackageName, requestId, this.mCookie, allowBackgroundAuthentication, isForLegacyFingerprintManager);
        this.mSensorState = 1;
    }

    void goToStateCookieReturnedIfCookieMatches(int cookie) {
        if (cookie == this.mCookie) {
            android.util.Slog.d(TAG, "Sensor(" + this.id + ") matched cookie: " + cookie);
            this.mSensorState = 2;
        }
    }

    void startSensor() throws android.os.RemoteException {
        this.impl.startPreparedClient(this.mCookie);
        this.mSensorState = 3;
    }

    void goToStateCancelling(android.os.IBinder token, java.lang.String opPackageName, long requestId) throws android.os.RemoteException {
        if (this.mSensorState != 4) {
            this.impl.cancelAuthenticationFromService(token, opPackageName, requestId);
            this.mSensorState = 4;
        }
    }

    void goToStoppedStateIfCookieMatches(int cookie, int error) {
        if (cookie == this.mCookie) {
            android.util.Slog.d(TAG, "Sensor(" + this.id + ") now in STATE_STOPPED");
            this.mError = error;
            this.mSensorState = 5;
        }
    }

    int getCurrentStrength() {
        return this.oemStrength | this.mUpdatedStrength;
    }

    int getSensorState() {
        return this.mSensorState;
    }

    int getCookie() {
        return this.mCookie;
    }

    void updateStrength(int newStrength) {
        java.lang.String log = "updateStrength: Before(" + this + ")";
        this.mUpdatedStrength = newStrength;
        android.util.Slog.d(TAG, log + " After(" + this + ")");
    }

    public java.lang.String toString() {
        return "ID(" + this.id + "), oemStrength: " + this.oemStrength + ", updatedStrength: " + this.mUpdatedStrength + ", modality " + this.modality + ", state: " + this.mSensorState + ", cookie: " + this.mCookie;
    }
}
