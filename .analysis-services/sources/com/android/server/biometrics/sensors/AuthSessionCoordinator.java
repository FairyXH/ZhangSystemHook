package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class AuthSessionCoordinator implements com.android.server.biometrics.sensors.AuthSessionListener {
    private static final java.lang.String TAG = "AuthSessionCoordinator";
    private final java.util.Set<java.lang.Integer> mAuthOperations;
    private com.android.server.biometrics.sensors.AuthResultCoordinator mAuthResultCoordinator;
    private boolean mIsAuthenticating;
    private final com.android.server.biometrics.sensors.MultiBiometricLockoutState mMultiBiometricLockoutState;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator.RingBuffer mRingBuffer;
    private int mUserId;

    public AuthSessionCoordinator() {
        this(android.os.SystemClock.elapsedRealtimeClock());
    }

    AuthSessionCoordinator(java.time.Clock clock) {
        this.mAuthOperations = new java.util.HashSet();
        this.mAuthResultCoordinator = new com.android.server.biometrics.sensors.AuthResultCoordinator();
        this.mMultiBiometricLockoutState = new com.android.server.biometrics.sensors.MultiBiometricLockoutState(clock);
        this.mRingBuffer = new com.android.server.biometrics.sensors.AuthSessionCoordinator.RingBuffer(100);
    }

    void onAuthSessionStarted(int userId) {
        this.mAuthOperations.clear();
        this.mUserId = userId;
        this.mIsAuthenticating = true;
        this.mAuthResultCoordinator = new com.android.server.biometrics.sensors.AuthResultCoordinator();
        this.mRingBuffer.addApiCall("internal : onAuthSessionStarted(" + userId + ")");
    }

    void endAuthSession() {
        java.util.Map<java.lang.Integer, java.lang.Integer> result = this.mAuthResultCoordinator.getResult();
        java.util.Iterator it = java.util.Arrays.asList(4095, 255, 15).iterator();
        while (it.hasNext()) {
            int authenticator = ((java.lang.Integer) it.next()).intValue();
            java.lang.Integer value = result.get(java.lang.Integer.valueOf(authenticator));
            if ((value.intValue() & 4) == 4) {
                this.mMultiBiometricLockoutState.clearPermanentLockOut(this.mUserId, authenticator);
                this.mMultiBiometricLockoutState.clearTimedLockout(this.mUserId, authenticator);
            } else if ((value.intValue() & 1) == 1) {
                this.mMultiBiometricLockoutState.setPermanentLockOut(this.mUserId, authenticator);
            } else if ((value.intValue() & 2) == 2) {
                this.mMultiBiometricLockoutState.setTimedLockout(this.mUserId, authenticator);
            }
        }
        if (this.mAuthOperations.isEmpty()) {
            this.mRingBuffer.addApiCall("internal : onAuthSessionEnded(" + this.mUserId + ")");
            clearSession();
        }
    }

    private void clearSession() {
        this.mIsAuthenticating = false;
        this.mAuthOperations.clear();
    }

    public int getLockoutStateFor(int userId, int strength) {
        return this.mMultiBiometricLockoutState.getLockoutState(userId, strength);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void authStartedFor(int userId, int sensorId, long requestId) {
        this.mRingBuffer.addApiCall("authStartedFor(userId=" + userId + ", sensorId=" + sensorId + ", requestId=" + requestId + ")");
        if (!this.mIsAuthenticating) {
            onAuthSessionStarted(userId);
        }
        if (this.mAuthOperations.contains(java.lang.Integer.valueOf(sensorId))) {
            android.util.Slog.e(TAG, "Error, authStartedFor(" + sensorId + ") without being finished");
        } else if (this.mUserId != userId) {
            android.util.Slog.e(TAG, "Error authStartedFor(" + userId + ") Incorrect userId, expected" + this.mUserId + ", ignoring...");
        } else {
            this.mAuthOperations.add(java.lang.Integer.valueOf(sensorId));
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void lockedOutFor(int userId, int biometricStrength, int sensorId, long requestId) {
        java.lang.String lockedOutStr = "lockOutFor(userId=" + userId + ", biometricStrength=" + biometricStrength + ", sensorId=" + sensorId + ", requestId=" + requestId + ")";
        this.mRingBuffer.addApiCall(lockedOutStr);
        this.mAuthResultCoordinator.lockedOutFor(biometricStrength);
        attemptToFinish(userId, sensorId, lockedOutStr);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void lockOutTimed(int userId, int biometricStrength, int sensorId, long time, long requestId) {
        java.lang.String lockedOutStr = "lockOutTimedFor(userId=" + userId + ", biometricStrength=" + biometricStrength + ", sensorId=" + sensorId + "time=" + time + ", requestId=" + requestId + ")";
        this.mRingBuffer.addApiCall(lockedOutStr);
        this.mAuthResultCoordinator.lockOutTimed(biometricStrength);
        attemptToFinish(userId, sensorId, lockedOutStr);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void authEndedFor(int userId, int biometricStrength, int sensorId, long requestId, boolean wasSuccessful) {
        java.lang.String authEndedStr = "authEndedFor(userId=" + userId + " ,biometricStrength=" + biometricStrength + ", sensorId=" + sensorId + ", requestId=" + requestId + ", wasSuccessful=" + wasSuccessful + ")";
        this.mRingBuffer.addApiCall(authEndedStr);
        if (wasSuccessful) {
            this.mAuthResultCoordinator.authenticatedFor(biometricStrength);
        }
        attemptToFinish(userId, sensorId, authEndedStr);
    }

    @Override // com.android.server.biometrics.sensors.AuthSessionListener
    public void resetLockoutFor(int userId, int biometricStrength, long requestId) {
        java.lang.String resetLockStr = "resetLockoutFor(userId=" + userId + " ,biometricStrength=" + biometricStrength + ", requestId=" + requestId + ")";
        this.mRingBuffer.addApiCall(resetLockStr);
        if (biometricStrength == 15) {
            clearSession();
            this.mMultiBiometricLockoutState.clearPermanentLockOut(userId, biometricStrength);
            this.mMultiBiometricLockoutState.clearTimedLockout(userId, biometricStrength);
        }
    }

    private void attemptToFinish(int userId, int sensorId, java.lang.String description) {
        boolean didFail = false;
        if (!this.mAuthOperations.contains(java.lang.Integer.valueOf(sensorId))) {
            android.util.Slog.e(TAG, "Error unable to find auth operation : " + description);
            didFail = true;
        }
        if (userId != this.mUserId) {
            android.util.Slog.e(TAG, "Error mismatched userId, expected=" + this.mUserId + " for " + description);
            didFail = true;
        }
        if (didFail) {
            return;
        }
        this.mAuthOperations.remove(java.lang.Integer.valueOf(sensorId));
        if (this.mIsAuthenticating) {
            endAuthSession();
        }
    }

    public java.lang.String toString() {
        return this.mRingBuffer + "\n" + this.mMultiBiometricLockoutState;
    }

    private static class RingBuffer {
        private int mApiCallNumber;
        private final java.lang.String[] mApiCalls;
        private int mCurr;
        private final int mSize;

        RingBuffer(int size) {
            if (size <= 0) {
                android.util.Slog.wtf(com.android.server.biometrics.sensors.AuthSessionCoordinator.TAG, "Cannot initialize ring buffer of size: " + size);
            }
            this.mApiCalls = new java.lang.String[size];
            this.mCurr = 0;
            this.mSize = size;
            this.mApiCallNumber = 0;
        }

        synchronized void addApiCall(java.lang.String str) {
            this.mApiCalls[this.mCurr] = str;
            this.mCurr++;
            this.mCurr %= this.mSize;
            this.mApiCallNumber++;
        }

        public synchronized java.lang.String toString() {
            java.lang.String buffer;
            buffer = "";
            int apiCall = this.mApiCallNumber > this.mSize ? this.mApiCallNumber - this.mSize : 0;
            for (int i = 0; i < this.mSize; i++) {
                int location = (this.mCurr + i) % this.mSize;
                if (this.mApiCalls[location] != null) {
                    int apiCall2 = apiCall + 1;
                    buffer = buffer + java.lang.String.format("#%-5d %s\n", java.lang.Integer.valueOf(apiCall), this.mApiCalls[location]);
                    apiCall = apiCall2;
                }
            }
            return buffer;
        }
    }
}
