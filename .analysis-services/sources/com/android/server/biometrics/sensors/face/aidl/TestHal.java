package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class TestHal extends android.hardware.biometrics.face.IFace.Stub {
    private static final java.lang.String TAG = "face.aidl.TestHal";

    public int getInterfaceVersion() {
        return 4;
    }

    public java.lang.String getInterfaceHash() {
        return "c43fbb9be4a662cc9ace640dba21cccdb84c6c21";
    }

    public android.hardware.biometrics.face.SensorProps[] getSensorProps() {
        android.util.Slog.w(TAG, "getSensorProps");
        return new android.hardware.biometrics.face.SensorProps[0];
    }

    public android.hardware.biometrics.face.ISession createSession(int sensorId, int userId, final android.hardware.biometrics.face.ISessionCallback cb) {
        android.util.Slog.w(TAG, "createSession, sensorId: " + sensorId + " userId: " + userId);
        return new android.hardware.biometrics.face.ISession.Stub() { // from class: com.android.server.biometrics.sensors.face.aidl.TestHal.1
            public int getInterfaceVersion() {
                return 4;
            }

            public java.lang.String getInterfaceHash() {
                return "c43fbb9be4a662cc9ace640dba21cccdb84c6c21";
            }

            public void generateChallenge() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "generateChallenge");
                cb.onChallengeGenerated(0L);
            }

            public void revokeChallenge(long challenge) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "revokeChallenge: " + challenge);
                cb.onChallengeRevoked(challenge);
            }

            public android.hardware.biometrics.face.EnrollmentStageConfig[] getEnrollmentConfig(byte enrollmentType) {
                return new android.hardware.biometrics.face.EnrollmentStageConfig[0];
            }

            public android.hardware.biometrics.common.ICancellationSignal enroll(android.hardware.keymaster.HardwareAuthToken hat, byte enrollmentType, byte[] features, android.hardware.common.NativeHandle previewSurface) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "enroll");
                return new android.hardware.biometrics.common.ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.face.aidl.TestHal.1.1
                    public void cancel() throws android.os.RemoteException {
                        cb.onError((byte) 5, 0);
                    }

                    public int getInterfaceVersion() {
                        return 4;
                    }

                    public java.lang.String getInterfaceHash() {
                        return "8a6cd86630181a4df6f20056259ec200ffe39209";
                    }
                };
            }

            public android.hardware.biometrics.common.ICancellationSignal authenticate(long operationId) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "authenticate");
                return new android.hardware.biometrics.common.ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.face.aidl.TestHal.1.2
                    public void cancel() throws android.os.RemoteException {
                        cb.onError((byte) 5, 0);
                    }

                    public int getInterfaceVersion() {
                        return 4;
                    }

                    public java.lang.String getInterfaceHash() {
                        return "8a6cd86630181a4df6f20056259ec200ffe39209";
                    }
                };
            }

            public android.hardware.biometrics.common.ICancellationSignal detectInteraction() {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "detectInteraction");
                return new android.hardware.biometrics.common.ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.face.aidl.TestHal.1.3
                    public void cancel() throws android.os.RemoteException {
                        cb.onError((byte) 5, 0);
                    }

                    public int getInterfaceVersion() {
                        return 4;
                    }

                    public java.lang.String getInterfaceHash() {
                        return "8a6cd86630181a4df6f20056259ec200ffe39209";
                    }
                };
            }

            public void enumerateEnrollments() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "enumerateEnrollments");
                cb.onEnrollmentsEnumerated(new int[0]);
            }

            public void removeEnrollments(int[] enrollmentIds) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "removeEnrollments");
                cb.onEnrollmentsRemoved(enrollmentIds);
            }

            public void getFeatures() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "getFeatures");
                cb.onFeaturesRetrieved(new byte[0]);
            }

            public void setFeature(android.hardware.keymaster.HardwareAuthToken hat, byte feature, boolean enabled) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "setFeature");
                cb.onFeatureSet(feature);
            }

            public void getAuthenticatorId() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "getAuthenticatorId");
                cb.onAuthenticatorIdRetrieved(0L);
            }

            public void invalidateAuthenticatorId() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "invalidateAuthenticatorId");
                cb.onAuthenticatorIdInvalidated(0L);
            }

            public void resetLockout(android.hardware.keymaster.HardwareAuthToken hat) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "resetLockout");
                cb.onLockoutCleared();
            }

            public void close() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "close");
                cb.onSessionClosed();
            }

            public android.hardware.biometrics.common.ICancellationSignal authenticateWithContext(long operationId, android.hardware.biometrics.common.OperationContext context) {
                return authenticate(operationId);
            }

            public android.hardware.biometrics.common.ICancellationSignal enrollWithContext(android.hardware.keymaster.HardwareAuthToken hat, byte enrollmentType, byte[] features, android.hardware.common.NativeHandle previewSurface, android.hardware.biometrics.common.OperationContext context) {
                return enroll(hat, enrollmentType, features, previewSurface);
            }

            public android.hardware.biometrics.common.ICancellationSignal detectInteractionWithContext(android.hardware.biometrics.common.OperationContext context) {
                return detectInteraction();
            }

            public void onContextChanged(android.hardware.biometrics.common.OperationContext context) {
                android.util.Slog.w(com.android.server.biometrics.sensors.face.aidl.TestHal.TAG, "onContextChanged");
            }

            public android.hardware.biometrics.common.ICancellationSignal enrollWithOptions(android.hardware.biometrics.face.FaceEnrollOptions options) {
                return enroll(options.hardwareAuthToken, options.enrollmentType, options.features, options.nativeHandlePreview);
            }
        };
    }
}
