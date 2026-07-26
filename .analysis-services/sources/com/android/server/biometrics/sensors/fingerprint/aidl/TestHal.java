package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class TestHal extends android.hardware.biometrics.fingerprint.IFingerprint.Stub {
    private static final java.lang.String TAG = "fingerprint.aidl.TestHal";

    public int getInterfaceVersion() {
        return 4;
    }

    public java.lang.String getInterfaceHash() {
        return "41a730a7a6b5aa9cebebce70ee5b5e509b0af6fb";
    }

    public android.hardware.biometrics.fingerprint.SensorProps[] getSensorProps() {
        android.util.Slog.w(TAG, "getSensorProps");
        return new android.hardware.biometrics.fingerprint.SensorProps[0];
    }

    public android.hardware.biometrics.fingerprint.ISession createSession(int sensorId, int userId, final android.hardware.biometrics.fingerprint.ISessionCallback cb) {
        android.util.Slog.w(TAG, "createSession, sensorId: " + sensorId + " userId: " + userId);
        return new android.hardware.biometrics.fingerprint.ISession.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1
            public int getInterfaceVersion() {
                return 4;
            }

            public java.lang.String getInterfaceHash() {
                return "41a730a7a6b5aa9cebebce70ee5b5e509b0af6fb";
            }

            public void generateChallenge() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "generateChallenge");
                cb.onChallengeGenerated(0L);
            }

            public void revokeChallenge(long challenge) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "revokeChallenge: " + challenge);
                cb.onChallengeRevoked(challenge);
            }

            public android.hardware.biometrics.common.ICancellationSignal enroll(android.hardware.keymaster.HardwareAuthToken hat) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "enroll");
                return new android.hardware.biometrics.common.ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1.1
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
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "authenticate");
                return new android.hardware.biometrics.common.ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1.2
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
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "detectInteraction");
                return new android.hardware.biometrics.common.ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1.3
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
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "enumerateEnrollments");
                cb.onEnrollmentsEnumerated(new int[0]);
            }

            public void removeEnrollments(int[] enrollmentIds) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "removeEnrollments");
                cb.onEnrollmentsRemoved(enrollmentIds);
            }

            public void getAuthenticatorId() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "getAuthenticatorId");
                cb.onAuthenticatorIdRetrieved(0L);
            }

            public void invalidateAuthenticatorId() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "invalidateAuthenticatorId");
                cb.onAuthenticatorIdInvalidated(0L);
            }

            public void resetLockout(android.hardware.keymaster.HardwareAuthToken hat) throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "resetLockout");
                cb.onLockoutCleared();
            }

            public void close() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "close");
                cb.onSessionClosed();
            }

            public void onPointerDown(int pointerId, int x, int y, float minor, float major) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "onPointerDown");
            }

            public void onPointerUp(int pointerId) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "onPointerUp");
            }

            public void onUiReady() {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "onUiReady");
            }

            public android.hardware.biometrics.common.ICancellationSignal authenticateWithContext(long operationId, android.hardware.biometrics.common.OperationContext context) {
                return authenticate(operationId);
            }

            public android.hardware.biometrics.common.ICancellationSignal enrollWithContext(android.hardware.keymaster.HardwareAuthToken hat, android.hardware.biometrics.common.OperationContext context) {
                return enroll(hat);
            }

            public android.hardware.biometrics.common.ICancellationSignal detectInteractionWithContext(android.hardware.biometrics.common.OperationContext context) {
                return detectInteraction();
            }

            public void onPointerDownWithContext(android.hardware.biometrics.fingerprint.PointerContext context) {
                onPointerDown(context.pointerId, (int) context.x, (int) context.y, context.minor, context.major);
            }

            public void onPointerUpWithContext(android.hardware.biometrics.fingerprint.PointerContext context) {
                onPointerUp(context.pointerId);
            }

            public void onContextChanged(android.hardware.biometrics.common.OperationContext context) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "onContextChanged");
            }

            public void onPointerCancelWithContext(android.hardware.biometrics.fingerprint.PointerContext context) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "onPointerCancelWithContext");
            }

            public void setIgnoreDisplayTouches(boolean shouldIgnore) {
                android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.TAG, "setIgnoreDisplayTouches");
            }
        };
    }
}
