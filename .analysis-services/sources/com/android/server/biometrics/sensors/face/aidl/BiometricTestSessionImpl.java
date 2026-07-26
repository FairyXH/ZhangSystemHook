package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class BiometricTestSessionImpl extends android.hardware.biometrics.ITestSession.Stub {
    private static final java.lang.String TAG = "face/aidl/BiometricTestSessionImpl";
    private final android.hardware.biometrics.ITestSessionCallback mCallback;
    private final android.content.Context mContext;
    private final com.android.server.biometrics.sensors.face.aidl.FaceProvider mProvider;
    private final com.android.server.biometrics.sensors.face.aidl.Sensor mSensor;
    private final int mSensorId;
    private final android.hardware.face.IFaceServiceReceiver mReceiver = new android.hardware.face.IFaceServiceReceiver.Stub() { // from class: com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.1
        public void onEnrollResult(android.hardware.face.Face face, int remaining) {
        }

        public void onAcquired(int acquireInfo, int vendorCode) {
        }

        public void onAuthenticationSucceeded(android.hardware.face.Face face, int userId, boolean isStrongBiometric) {
        }

        public void onFaceDetected(int sensorId, int userId, boolean isStrongBiometric) {
        }

        public void onAuthenticationFailed() {
        }

        public void onError(int error, int vendorCode) {
        }

        public void onRemoved(android.hardware.face.Face face, int remaining) {
        }

        public void onFeatureSet(boolean success, int feature) {
        }

        public void onFeatureGet(boolean success, int[] features, boolean[] featureState) {
        }

        public void onChallengeGenerated(int sensorId, int userId, long challenge) {
        }

        public void onAuthenticationFrame(android.hardware.face.FaceAuthenticationFrame frame) {
        }

        public void onEnrollmentFrame(android.hardware.face.FaceEnrollFrame frame) {
        }
    };
    private final java.util.Set<java.lang.Integer> mEnrollmentIds = new java.util.HashSet();
    private final java.util.Random mRandom = new java.util.Random();

    BiometricTestSessionImpl(android.content.Context context, int sensorId, android.hardware.biometrics.ITestSessionCallback callback, com.android.server.biometrics.sensors.face.aidl.FaceProvider provider, com.android.server.biometrics.sensors.face.aidl.Sensor sensor) {
        this.mContext = context;
        this.mSensorId = sensorId;
        this.mCallback = callback;
        this.mProvider = provider;
        this.mSensor = sensor;
    }

    public void setTestHalEnabled(boolean enabled) {
        super.setTestHalEnabled_enforcePermission();
        this.mProvider.setTestHalEnabled(enabled);
        this.mSensor.setTestHalEnabled(enabled);
    }

    public void startEnroll(int userId) {
        super.startEnroll_enforcePermission();
        this.mProvider.scheduleEnroll(this.mSensorId, new android.os.Binder(), new byte[69], userId, this.mReceiver, this.mContext.getOpPackageName(), new int[0], null, false, new android.hardware.face.FaceEnrollOptions.Builder().build());
    }

    public void finishEnroll(int userId) {
        super.finishEnroll_enforcePermission();
        int nextRandomId = this.mRandom.nextInt();
        while (this.mEnrollmentIds.contains(java.lang.Integer.valueOf(nextRandomId))) {
            nextRandomId = this.mRandom.nextInt();
        }
        this.mEnrollmentIds.add(java.lang.Integer.valueOf(nextRandomId));
        this.mSensor.getSessionForUser(userId).getHalSessionCallback().onEnrollmentProgress(nextRandomId, 0);
    }

    public void acceptAuthentication(int userId) {
        super.acceptAuthentication_enforcePermission();
        java.util.List<android.hardware.face.Face> faces = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(this.mSensorId).getBiometricsForUser(this.mContext, userId);
        if (faces.isEmpty()) {
            android.util.Slog.w(TAG, "No faces, returning");
        } else {
            int fid = faces.get(0).getBiometricId();
            this.mSensor.getSessionForUser(userId).getHalSessionCallback().onAuthenticationSucceeded(fid, com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(new byte[69]));
        }
    }

    public void rejectAuthentication(int userId) {
        super.rejectAuthentication_enforcePermission();
        this.mSensor.getSessionForUser(userId).getHalSessionCallback().onAuthenticationFailed();
    }

    public void notifyAcquired(int userId, int acquireInfo) {
        super.notifyAcquired_enforcePermission();
        android.hardware.biometrics.face.BaseFrame data = new android.hardware.biometrics.face.BaseFrame();
        data.acquiredInfo = (byte) acquireInfo;
        if (this.mSensor.getScheduler().getCurrentClient() instanceof com.android.server.biometrics.sensors.EnrollClient) {
            android.hardware.biometrics.face.EnrollmentFrame frame = new android.hardware.biometrics.face.EnrollmentFrame();
            frame.data = data;
            this.mSensor.getSessionForUser(userId).getHalSessionCallback().onEnrollmentFrame(frame);
        } else {
            android.hardware.biometrics.face.AuthenticationFrame frame2 = new android.hardware.biometrics.face.AuthenticationFrame();
            frame2.data = data;
            this.mSensor.getSessionForUser(userId).getHalSessionCallback().onAuthenticationFrame(frame2);
        }
    }

    public void notifyError(int userId, int errorCode) {
        super.notifyError_enforcePermission();
        this.mSensor.getSessionForUser(userId).getHalSessionCallback().onError((byte) errorCode, 0);
    }

    public void cleanupInternalState(int userId) {
        super.cleanupInternalState_enforcePermission();
        android.util.Slog.d(TAG, "cleanupInternalState: " + userId);
        this.mProvider.scheduleInternalCleanup(this.mSensorId, userId, new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
                try {
                    android.util.Slog.d(com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.TAG, "onClientStarted: " + clientMonitor);
                    com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.this.mCallback.onCleanupStarted(clientMonitor.getTargetUserId());
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.TAG, "Remote exception", e);
                }
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                try {
                    android.util.Slog.d(com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.TAG, "onClientFinished: " + clientMonitor);
                    com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.this.mCallback.onCleanupFinished(clientMonitor.getTargetUserId());
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl.TAG, "Remote exception", e);
                }
            }
        });
    }
}
