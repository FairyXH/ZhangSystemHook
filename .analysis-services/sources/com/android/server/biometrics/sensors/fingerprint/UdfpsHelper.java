package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class UdfpsHelper {
    private static final java.lang.String TAG = "UdfpsHelper";

    public static void onFingerDown(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint daemon, int x, int y, float minor, float major) {
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint extension = android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.castFrom((android.os.IHwInterface) daemon);
        if (extension == null) {
            android.util.Slog.v(TAG, "onFingerDown | failed to cast the HIDL to V2_3");
            return;
        }
        try {
            extension.onFingerDown(x, y, minor, major);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "onFingerDown | RemoteException: ", e);
        }
    }

    public static void onFingerUp(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint daemon) {
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint extension = android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.castFrom((android.os.IHwInterface) daemon);
        if (extension == null) {
            android.util.Slog.v(TAG, "onFingerUp | failed to cast the HIDL to V2_3");
            return;
        }
        try {
            extension.onFingerUp();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "onFingerUp | RemoteException: ", e);
        }
    }

    public static boolean isValidAcquisitionMessage(android.content.Context context, int acquireInfo, int vendorCode) {
        return android.hardware.fingerprint.FingerprintManager.getAcquiredString(context, acquireInfo, vendorCode) != null;
    }
}
