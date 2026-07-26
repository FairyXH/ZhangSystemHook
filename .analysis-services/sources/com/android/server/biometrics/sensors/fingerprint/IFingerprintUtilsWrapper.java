package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintUtilsWrapper {
    default com.android.server.biometrics.sensors.fingerprint.FingerprintUserState getStateForUser(android.content.Context ctx, int userId) {
        return null;
    }
}
