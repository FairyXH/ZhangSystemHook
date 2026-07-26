package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
class AuthResultCoordinator {
    static final int AUTHENTICATOR_DEFAULT = 0;
    static final int AUTHENTICATOR_PERMANENT_LOCKED = 1;
    static final int AUTHENTICATOR_TIMED_LOCKED = 2;
    static final int AUTHENTICATOR_UNLOCKED = 4;
    private static final java.lang.String TAG = "AuthResultCoordinator";
    private final java.util.Map<java.lang.Integer, java.lang.Integer> mAuthenticatorState = new android.util.ArrayMap();

    AuthResultCoordinator() {
        this.mAuthenticatorState.put(15, 0);
        this.mAuthenticatorState.put(255, 0);
        this.mAuthenticatorState.put(4095, 0);
    }

    private void updateState(int strength, java.util.function.IntFunction<java.lang.Integer> mapper) {
        switch (strength) {
            case 15:
                this.mAuthenticatorState.put(15, mapper.apply(this.mAuthenticatorState.get(15).intValue()));
            case 255:
                this.mAuthenticatorState.put(255, mapper.apply(this.mAuthenticatorState.get(255).intValue()));
            case 4095:
                this.mAuthenticatorState.put(4095, mapper.apply(this.mAuthenticatorState.get(4095).intValue()));
                break;
        }
    }

    void authenticatedFor(int strength) {
        if (strength == 15) {
            updateState(strength, new java.util.function.IntFunction() { // from class: com.android.server.biometrics.sensors.AuthResultCoordinator$$ExternalSyntheticLambda2
                @Override // java.util.function.IntFunction
                public final java.lang.Object apply(int i) {
                    return java.lang.Integer.valueOf(i | 4);
                }
            });
        }
    }

    void lockedOutFor(int strength) {
        updateState(strength, new java.util.function.IntFunction() { // from class: com.android.server.biometrics.sensors.AuthResultCoordinator$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return java.lang.Integer.valueOf(i | 1);
            }
        });
    }

    void lockOutTimed(int strength) {
        updateState(strength, new java.util.function.IntFunction() { // from class: com.android.server.biometrics.sensors.AuthResultCoordinator$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return java.lang.Integer.valueOf(i | 2);
            }
        });
    }

    final java.util.Map<java.lang.Integer, java.lang.Integer> getResult() {
        return java.util.Collections.unmodifiableMap(this.mAuthenticatorState);
    }
}
