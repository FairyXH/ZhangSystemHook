package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public class TestHal extends android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.Stub {
    private static final java.lang.String TAG = "fingerprint.hidl.TestHal";
    private android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback mCallback;
    private final android.content.Context mContext;
    private final int mSensorId;

    TestHal(android.content.Context context, int sensorId) {
        this.mContext = context;
        this.mSensorId = sensorId;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint
    public boolean isUdfps(int sensorId) {
        return false;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint
    public void onFingerDown(int x, int y, float minor, float major) {
    }

    @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint
    public void onFingerUp() {
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public long setNotify(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback clientCallback) {
        this.mCallback = clientCallback;
        return 0L;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public long preEnroll() {
        return 0L;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int enroll(byte[] hat, int gid, int timeoutSec) {
        android.util.Slog.w(TAG, "enroll");
        return 0;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int postEnroll() {
        return 0;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public long getAuthenticatorId() {
        return 0L;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int cancel() throws android.os.RemoteException {
        if (this.mCallback != null) {
            this.mCallback.onError(0L, 5, 0);
        }
        return 0;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int enumerate() throws android.os.RemoteException {
        android.util.Slog.w(TAG, "Enumerate");
        if (this.mCallback != null) {
            this.mCallback.onEnumerate(0L, 0, 0, 0);
            return 0;
        }
        return 0;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int remove(int gid, int fid) throws android.os.RemoteException {
        android.util.Slog.w(TAG, "Remove");
        if (this.mCallback != null) {
            if (fid == 0) {
                java.util.List<android.hardware.fingerprint.Fingerprint> fingerprints = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(this.mSensorId).getBiometricsForUser(this.mContext, gid);
                for (int i = 0; i < fingerprints.size(); i++) {
                    android.hardware.fingerprint.Fingerprint fp = fingerprints.get(i);
                    this.mCallback.onRemoved(0L, fp.getBiometricId(), gid, (fingerprints.size() - i) - 1);
                }
                return 0;
            }
            this.mCallback.onRemoved(0L, fid, gid, 0);
            return 0;
        }
        return 0;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int setActiveGroup(int gid, java.lang.String storePath) {
        return 0;
    }

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
    public int authenticate(long operationId, int gid) {
        android.util.Slog.w(TAG, "Authenticate");
        return 0;
    }
}
