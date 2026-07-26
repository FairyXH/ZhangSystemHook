package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
class AuthResult {
    static final int AUTHENTICATED = 2;
    static final int FAILED = 0;
    static final int LOCKED_OUT = 1;
    private final int mBiometricStrength;
    private final int mStatus;

    AuthResult(int status, int strength) {
        this.mStatus = status;
        this.mBiometricStrength = strength;
    }

    int getStatus() {
        return this.mStatus;
    }

    int getBiometricStrength() {
        return this.mBiometricStrength;
    }
}
