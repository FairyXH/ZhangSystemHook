package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
class BiometricTestSessionImpl extends android.hardware.biometrics.ITestSession.Stub {
    private static final java.lang.String TAG = "fp/aidl/BiometricTestSessionImpl";
    private static final int VHAL_ENROLLMENT_ID = 9999;
    private final com.android.server.biometrics.sensors.BiometricStateCallback mBiometricStateCallback;
    private final android.hardware.biometrics.ITestSessionCallback mCallback;
    private final android.content.Context mContext;
    private final com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider mProvider;
    private final com.android.server.biometrics.sensors.fingerprint.aidl.Sensor mSensor;
    private final int mSensorId;
    private final android.hardware.fingerprint.IFingerprintServiceReceiver mReceiver = new android.hardware.fingerprint.IFingerprintServiceReceiver.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.1
        public void onEnrollResult(android.hardware.fingerprint.Fingerprint fp, int remaining) {
        }

        public void onAcquired(int acquiredInfo, int vendorCode) {
        }

        public void onAuthenticationSucceeded(android.hardware.fingerprint.Fingerprint fp, int userId, boolean isStrongBiometric) {
        }

        public void onFingerprintDetected(int sensorId, int userId, boolean isStrongBiometric) {
        }

        public void onAuthenticationFailed() {
        }

        public void onError(int error, int vendorCode) {
        }

        public void onRemoved(android.hardware.fingerprint.Fingerprint fp, int remaining) {
        }

        public void onChallengeGenerated(int sensorId, int userId, long challenge) {
        }

        public void onUdfpsPointerDown(int sensorId) {
        }

        public void onUdfpsPointerUp(int sensorId) {
        }

        public void onUdfpsOverlayShown() {
        }
    };
    private final java.util.Set<java.lang.Integer> mEnrollmentIds = new java.util.HashSet();
    private final java.util.Random mRandom = new java.util.Random();

    BiometricTestSessionImpl(android.content.Context context, int sensorId, android.hardware.biometrics.ITestSessionCallback callback, com.android.server.biometrics.sensors.BiometricStateCallback biometricStateCallback, com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor sensor) {
        this.mContext = context;
        this.mSensorId = sensorId;
        this.mCallback = callback;
        this.mBiometricStateCallback = biometricStateCallback;
        this.mProvider = provider;
        this.mSensor = sensor;
    }

    public void setTestHalEnabled(boolean enabled) {
        super.setTestHalEnabled_enforcePermission();
        this.mSensor.setTestHalEnabled(enabled);
        this.mProvider.setTestHalEnabled(enabled);
    }

    public void startEnroll(int userId) {
        super.startEnroll_enforcePermission();
        this.mProvider.scheduleEnroll(this.mSensorId, new android.os.Binder(), new byte[69], userId, this.mReceiver, this.mContext.getOpPackageName(), 2, new android.hardware.fingerprint.FingerprintEnrollOptions.Builder().build());
    }

    public void finishEnroll(int userId) throws android.os.RemoteException {
        super.finishEnroll_enforcePermission();
        android.util.Slog.i(TAG, "finishEnroll(): useVhalForTesting=" + this.mProvider.useVhalForTesting());
        if (this.mProvider.useVhalForTesting()) {
            android.hardware.biometrics.fingerprint.AcquiredInfoAndVendorCode[] acquiredInfoAndVendorCodes = {new android.hardware.biometrics.fingerprint.AcquiredInfoAndVendorCode()};
            android.hardware.biometrics.fingerprint.EnrollmentProgressStep[] enrollmentProgressSteps = {new android.hardware.biometrics.fingerprint.EnrollmentProgressStep(), new android.hardware.biometrics.fingerprint.EnrollmentProgressStep()};
            enrollmentProgressSteps[0].durationMs = 100;
            enrollmentProgressSteps[0].acquiredInfoAndVendorCodes = acquiredInfoAndVendorCodes;
            enrollmentProgressSteps[1].durationMs = 200;
            enrollmentProgressSteps[1].acquiredInfoAndVendorCodes = acquiredInfoAndVendorCodes;
            android.hardware.biometrics.fingerprint.NextEnrollment nextEnrollment = new android.hardware.biometrics.fingerprint.NextEnrollment();
            nextEnrollment.id = VHAL_ENROLLMENT_ID;
            nextEnrollment.progressSteps = enrollmentProgressSteps;
            nextEnrollment.result = true;
            this.mProvider.getVhal().setNextEnrollment(nextEnrollment);
            this.mProvider.simulateVhalFingerDown(userId, this.mSensorId);
            return;
        }
        int nextRandomId = this.mRandom.nextInt();
        while (this.mEnrollmentIds.contains(java.lang.Integer.valueOf(nextRandomId))) {
            nextRandomId = this.mRandom.nextInt();
        }
        this.mEnrollmentIds.add(java.lang.Integer.valueOf(nextRandomId));
        this.mSensor.getSessionForUser(userId).getHalSessionCallback().onEnrollmentProgress(nextRandomId, 0);
    }

    public void acceptAuthentication(int userId) throws android.os.RemoteException {
        super.acceptAuthentication_enforcePermission();
        if (this.mProvider.useVhalForTesting()) {
            this.mProvider.getVhal().setEnrollmentHit(VHAL_ENROLLMENT_ID);
            this.mProvider.simulateVhalFingerDown(userId, this.mSensorId);
            return;
        }
        java.util.List<android.hardware.fingerprint.Fingerprint> fingerprints = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(this.mSensorId).getBiometricsForUser(this.mContext, userId);
        if (fingerprints.isEmpty()) {
            android.util.Slog.w(TAG, "No fingerprints, returning");
        } else {
            int fid = fingerprints.get(0).getBiometricId();
            this.mSensor.getSessionForUser(userId).getHalSessionCallback().onAuthenticationSucceeded(fid, com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(new byte[69]));
        }
    }

    public void rejectAuthentication(int userId) throws android.os.RemoteException {
        super.rejectAuthentication_enforcePermission();
        if (this.mProvider.useVhalForTesting()) {
            this.mProvider.getVhal().setEnrollmentHit(10000);
            this.mProvider.simulateVhalFingerDown(userId, this.mSensorId);
        } else {
            this.mSensor.getSessionForUser(userId).getHalSessionCallback().onAuthenticationFailed();
        }
    }

    public void notifyAcquired(int userId, int acquireInfo) {
        super.notifyAcquired_enforcePermission();
        this.mSensor.getSessionForUser(userId).getHalSessionCallback().onAcquired((byte) acquireInfo, 0);
    }

    public void notifyError(int userId, int errorCode) {
        super.notifyError_enforcePermission();
        this.mSensor.getSessionForUser(userId).getHalSessionCallback().onError((byte) errorCode, 0);
    }

    public void cleanupInternalState(int userId) throws android.os.RemoteException {
        super.cleanupInternalState_enforcePermission();
        android.util.Slog.d(TAG, "cleanupInternalState: " + userId);
        if (this.mProvider.useVhalForTesting()) {
            android.util.Slog.i(TAG, "cleanup virtualhal configurations");
            this.mProvider.getVhal().resetConfigurations();
        }
        this.mProvider.scheduleInternalCleanup(this.mSensorId, userId, new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
                try {
                    android.util.Slog.d(com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.TAG, "onClientStarted: " + clientMonitor);
                    com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.this.mCallback.onCleanupStarted(clientMonitor.getTargetUserId());
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.TAG, "Remote exception", e);
                }
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                try {
                    android.util.Slog.d(com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.TAG, "onClientFinished: " + clientMonitor);
                    com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.this.mCallback.onCleanupFinished(clientMonitor.getTargetUserId());
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl.TAG, "Remote exception", e);
                }
            }
        });
    }
}
