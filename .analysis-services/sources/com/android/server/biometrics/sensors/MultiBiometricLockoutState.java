package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
class MultiBiometricLockoutState {
    private static final java.lang.String TAG = "MultiBiometricLockoutState";
    private final java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState>> mCanUserAuthenticate = new java.util.HashMap();
    private final java.time.Clock mClock;

    MultiBiometricLockoutState(java.time.Clock clock) {
        this.mClock = clock;
    }

    private java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> createUnlockedMap() {
        java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> lockOutMap = new java.util.HashMap<>();
        lockOutMap.put(15, new com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState(15, false, false));
        lockOutMap.put(255, new com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState(255, false, false));
        lockOutMap.put(4095, new com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState(4095, false, false));
        return lockOutMap;
    }

    private java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> getAuthMapForUser(int userId) {
        if (!this.mCanUserAuthenticate.containsKey(java.lang.Integer.valueOf(userId))) {
            this.mCanUserAuthenticate.put(java.lang.Integer.valueOf(userId), createUnlockedMap());
        }
        return this.mCanUserAuthenticate.get(java.lang.Integer.valueOf(userId));
    }

    void setPermanentLockOut(int userId, int strength) {
        java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> authMap = getAuthMapForUser(userId);
        switch (strength) {
            case 15:
                authMap.get(15).mPermanentlyLockedOut = true;
            case 255:
                authMap.get(255).mPermanentlyLockedOut = true;
            case 4095:
                authMap.get(4095).mPermanentlyLockedOut = true;
                break;
            default:
                android.util.Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + strength);
                break;
        }
    }

    void clearPermanentLockOut(int userId, int strength) {
        java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> authMap = getAuthMapForUser(userId);
        switch (strength) {
            case 15:
                authMap.get(15).mPermanentlyLockedOut = false;
            case 255:
                authMap.get(255).mPermanentlyLockedOut = false;
            case 4095:
                authMap.get(4095).mPermanentlyLockedOut = false;
                break;
            default:
                android.util.Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + strength);
                break;
        }
    }

    void setTimedLockout(int userId, int strength) {
        java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> authMap = getAuthMapForUser(userId);
        switch (strength) {
            case 15:
                authMap.get(15).mTimedLockout = true;
            case 255:
                authMap.get(255).mTimedLockout = true;
            case 4095:
                authMap.get(4095).mTimedLockout = true;
                break;
            default:
                android.util.Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + strength);
                break;
        }
    }

    void clearTimedLockout(int userId, int strength) {
        java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> authMap = getAuthMapForUser(userId);
        switch (strength) {
            case 15:
                authMap.get(15).mTimedLockout = false;
            case 255:
                authMap.get(255).mTimedLockout = false;
            case 4095:
                authMap.get(4095).mTimedLockout = false;
                break;
            default:
                android.util.Slog.e(TAG, "increaseLockoutTime called for invalid strength : " + strength);
                break;
        }
    }

    int getLockoutState(int userId, int strength) {
        java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> authMap = getAuthMapForUser(userId);
        if (!authMap.containsKey(java.lang.Integer.valueOf(strength))) {
            android.util.Slog.e(TAG, "Error, getLockoutState for unknown strength: " + strength + " returning LOCKOUT_NONE");
            return 0;
        }
        com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState state = authMap.get(java.lang.Integer.valueOf(strength));
        if (state.mPermanentlyLockedOut) {
            return 2;
        }
        return state.mTimedLockout ? 1 : 0;
    }

    public java.lang.String toString() {
        java.lang.String dumpState = "Permanent Lockouts\n";
        final long time = this.mClock.millis();
        for (java.util.Map.Entry<java.lang.Integer, java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState>> userState : this.mCanUserAuthenticate.entrySet()) {
            int userId = userState.getKey().intValue();
            java.util.Map<java.lang.Integer, com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState> map = userState.getValue();
            java.lang.String prettyStr = (java.lang.String) map.entrySet().stream().map(new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.MultiBiometricLockoutState$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.server.biometrics.sensors.MultiBiometricLockoutState.AuthenticatorState) ((java.util.Map.Entry) obj).getValue()).toString(time);
                }
            }).collect(java.util.stream.Collectors.joining(", "));
            dumpState = dumpState + "UserId=" + userId + ", {" + prettyStr + "}\n";
        }
        return dumpState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class AuthenticatorState {
        private java.lang.Integer mAuthenticatorType;
        private boolean mPermanentlyLockedOut;
        private boolean mTimedLockout;

        AuthenticatorState(java.lang.Integer authenticatorId, boolean permanentlyLockedOut, boolean timedLockout) {
            this.mAuthenticatorType = authenticatorId;
            this.mPermanentlyLockedOut = permanentlyLockedOut;
            this.mTimedLockout = timedLockout;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public java.lang.String toString(long currentTime) {
            java.lang.String timedLockout = this.mTimedLockout ? "true" : "false";
            java.lang.String permanentLockout = this.mPermanentlyLockedOut ? "true" : "false";
            return java.lang.String.format("(%s, permanentLockout=%s, timedLockout=%s)", android.hardware.biometrics.BiometricManager.authenticatorToStr(this.mAuthenticatorType.intValue()), permanentLockout, timedLockout);
        }
    }
}
