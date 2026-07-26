package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public interface IFingerprintUserStateExt {
    default void attributeFingerprint(com.android.modules.utils.TypedXmlSerializer serializer, android.hardware.fingerprint.Fingerprint fp) {
    }

    default android.hardware.fingerprint.Fingerprint parseBiometricsLocked(com.android.modules.utils.TypedXmlPullParser parser, android.hardware.fingerprint.Fingerprint fingerprint) {
        return fingerprint;
    }

    default android.hardware.fingerprint.Fingerprint getCopyFingerprint(android.hardware.fingerprint.Fingerprint fp) {
        return fp;
    }

    default int setFingerprintFlags(int fingerprintId, int flags) {
        return 0;
    }
}
