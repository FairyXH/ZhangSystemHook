package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintUtilsExt {
    default int hookTargetUserId(int defaultUserId) {
        return defaultUserId;
    }

    default int setFingerprintFlags(android.content.Context ctx, int fingerId, int flags, int userId) {
        return -1;
    }
}
