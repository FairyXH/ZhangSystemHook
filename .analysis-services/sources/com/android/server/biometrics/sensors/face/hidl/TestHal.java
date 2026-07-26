package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: loaded from: classes.dex */
public class TestHal extends android.hardware.biometrics.face.V1_0.IBiometricsFace.Stub {
    private static final java.lang.String TAG = "face.hidl.TestHal";
    private android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback mCallback;
    private final android.content.Context mContext;
    private final int mSensorId;
    private int mUserId;

    TestHal(android.content.Context context, int sensorId) {
        this.mContext = context;
        this.mSensorId = sensorId;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public android.hardware.biometrics.face.V1_0.OptionalUint64 setCallback(android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback clientCallback) {
        this.mCallback = clientCallback;
        android.hardware.biometrics.face.V1_0.OptionalUint64 result = new android.hardware.biometrics.face.V1_0.OptionalUint64();
        result.status = 0;
        return new android.hardware.biometrics.face.V1_0.OptionalUint64();
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int setActiveUser(int userId, java.lang.String storePath) {
        this.mUserId = userId;
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public android.hardware.biometrics.face.V1_0.OptionalUint64 generateChallenge(int challengeTimeoutSec) {
        android.util.Slog.w(TAG, "generateChallenge");
        android.hardware.biometrics.face.V1_0.OptionalUint64 result = new android.hardware.biometrics.face.V1_0.OptionalUint64();
        result.status = 0;
        result.value = 0L;
        return result;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int enroll(java.util.ArrayList<java.lang.Byte> hat, int timeoutSec, java.util.ArrayList<java.lang.Integer> disabledFeatures) {
        android.util.Slog.w(TAG, "enroll");
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int revokeChallenge() {
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int setFeature(int feature, boolean enabled, java.util.ArrayList<java.lang.Byte> hat, int faceId) {
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public android.hardware.biometrics.face.V1_0.OptionalBool getFeature(int feature, int faceId) {
        android.hardware.biometrics.face.V1_0.OptionalBool result = new android.hardware.biometrics.face.V1_0.OptionalBool();
        result.status = 0;
        result.value = true;
        return result;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public android.hardware.biometrics.face.V1_0.OptionalUint64 getAuthenticatorId() {
        android.hardware.biometrics.face.V1_0.OptionalUint64 result = new android.hardware.biometrics.face.V1_0.OptionalUint64();
        result.status = 0;
        result.value = 0L;
        return result;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int cancel() throws android.os.RemoteException {
        if (this.mCallback != null) {
            this.mCallback.onError(0L, 0, 5, 0);
            return 0;
        }
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int enumerate() throws android.os.RemoteException {
        android.util.Slog.w(TAG, "enumerate");
        if (this.mCallback != null) {
            this.mCallback.onEnumerate(0L, new java.util.ArrayList<>(), 0);
        }
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int remove(int faceId) throws android.os.RemoteException {
        android.util.Slog.w(TAG, "remove");
        if (this.mCallback == null) {
            return 0;
        }
        if (faceId == 0) {
            java.util.List<android.hardware.face.Face> faces = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(this.mSensorId).getBiometricsForUser(this.mContext, this.mUserId);
            java.util.ArrayList<java.lang.Integer> faceIds = new java.util.ArrayList<>();
            for (android.hardware.face.Face face : faces) {
                faceIds.add(java.lang.Integer.valueOf(face.getBiometricId()));
            }
            this.mCallback.onRemoved(0L, faceIds, this.mUserId);
            return 0;
        }
        this.mCallback.onRemoved(0L, new java.util.ArrayList<>(java.util.Collections.singletonList(java.lang.Integer.valueOf(faceId))), this.mUserId);
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int authenticate(long operationId) {
        android.util.Slog.w(TAG, "authenticate");
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int userActivity() {
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int resetLockout(java.util.ArrayList<java.lang.Byte> hat) {
        android.util.Slog.w(TAG, "resetLockout");
        return 0;
    }
}
