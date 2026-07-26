package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class BiometricDeferredQueue {
    private static final java.lang.String TAG = "BiometricDeferredQueue";
    private android.hardware.biometrics.BiometricManager mBiometricManager;
    private android.hardware.face.FaceManager mFaceManager;
    private com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask mFaceResetLockoutTask;
    private android.hardware.fingerprint.FingerprintManager mFingerprintManager;
    private final com.android.server.locksettings.SyntheticPasswordManager mSpManager;
    private com.android.server.biometrics.sensors.face.IBiometricDeferredQueueExt mIBiometricDeferredQueueExt = (com.android.server.biometrics.sensors.face.IBiometricDeferredQueueExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.IBiometricDeferredQueueExt.class).base(this).create();
    private final com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask.FinishCallback mFaceFinishCallback = new com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask.FinishCallback() { // from class: com.android.server.locksettings.BiometricDeferredQueue$$ExternalSyntheticLambda2
        @Override // com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask.FinishCallback
        public final void onFinished() {
            this.f$0.lambda$new$0();
        }
    };
    private final android.os.Handler mHandler = com.android.server.biometrics.BiometricHandlerProvider.getInstance().getBiometricCallbackHandler();
    private final java.util.ArrayList<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> mPendingResetLockoutsForFingerprint = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> mPendingResetLockoutsForFace = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> mPendingResetLockouts = new java.util.ArrayList<>();

    private static class UserAuthInfo {
        final byte[] gatekeeperPassword;
        final int userId;

        UserAuthInfo(int userId, byte[] gatekeeperPassword) {
            this.userId = userId;
            this.gatekeeperPassword = gatekeeperPassword;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class FaceResetLockoutTask implements android.hardware.face.FaceManager.GenerateChallengeCallback {
        android.hardware.face.FaceManager faceManager;
        com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask.FinishCallback finishCallback;
        java.util.List<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> pendingResetLockuts;
        java.util.Set<java.lang.Integer> sensorIds;
        com.android.server.locksettings.SyntheticPasswordManager spManager;

        interface FinishCallback {
            void onFinished();
        }

        FaceResetLockoutTask(com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask.FinishCallback finishCallback, android.hardware.face.FaceManager faceManager, com.android.server.locksettings.SyntheticPasswordManager spManager, java.util.Set<java.lang.Integer> sensorIds, java.util.List<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> pendingResetLockouts) {
            this.finishCallback = finishCallback;
            this.faceManager = faceManager;
            this.spManager = spManager;
            this.sensorIds = sensorIds;
            this.pendingResetLockuts = pendingResetLockouts;
        }

        public void onGenerateChallengeResult(int sensorId, int userId, long challenge) {
            if (!this.sensorIds.contains(java.lang.Integer.valueOf(sensorId))) {
                android.util.Slog.e(com.android.server.locksettings.BiometricDeferredQueue.TAG, "Unknown sensorId received: " + sensorId);
                return;
            }
            for (com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo userAuthInfo : this.pendingResetLockuts) {
                android.util.Slog.d(com.android.server.locksettings.BiometricDeferredQueue.TAG, "Resetting face lockout for sensor: " + sensorId + ", user: " + userAuthInfo.userId);
                byte[] hat = com.android.server.locksettings.BiometricDeferredQueue.requestHatFromGatekeeperPassword(this.spManager, userAuthInfo, challenge);
                if (hat != null) {
                    this.faceManager.resetLockout(sensorId, userAuthInfo.userId, hat);
                }
            }
            this.sensorIds.remove(java.lang.Integer.valueOf(sensorId));
            this.faceManager.revokeChallenge(sensorId, userId, challenge);
            if (this.sensorIds.isEmpty()) {
                android.util.Slog.d(com.android.server.locksettings.BiometricDeferredQueue.TAG, "Done requesting resetLockout for all face sensors");
                this.finishCallback.onFinished();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mFaceResetLockoutTask = null;
    }

    BiometricDeferredQueue(com.android.server.locksettings.SyntheticPasswordManager spManager) {
        this.mSpManager = spManager;
    }

    public void systemReady(android.hardware.fingerprint.FingerprintManager fingerprintManager, android.hardware.face.FaceManager faceManager, android.hardware.biometrics.BiometricManager biometricManager) {
        this.mFingerprintManager = fingerprintManager;
        this.mFaceManager = faceManager;
        this.mBiometricManager = biometricManager;
    }

    void addPendingLockoutResetForUser(final int userId, final byte[] gatekeeperPassword) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.BiometricDeferredQueue$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$addPendingLockoutResetForUser$1(userId, gatekeeperPassword);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addPendingLockoutResetForUser$1(int userId, byte[] gatekeeperPassword) {
        if (this.mFingerprintManager != null && this.mFingerprintManager.hasEnrolledFingerprints(userId)) {
            android.util.Slog.d(TAG, "Fingerprint addPendingLockoutResetForUser: " + userId);
            this.mPendingResetLockoutsForFingerprint.add(new com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo(userId, gatekeeperPassword));
        }
        if (this.mBiometricManager != null) {
            android.util.Slog.d(TAG, "Fingerprint addPendingLockoutResetForUser: " + userId);
            this.mPendingResetLockouts.add(new com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo(userId, gatekeeperPassword));
        }
        if (this.mFaceManager != null && this.mFaceManager.hasEnrolledTemplates(userId)) {
            android.util.Slog.d(TAG, "Face addPendingLockoutResetForUser: " + userId);
            this.mPendingResetLockoutsForFace.add(new com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo(userId, gatekeeperPassword));
        }
        if (this.mIBiometricDeferredQueueExt != null && this.mIBiometricDeferredQueueExt.hasEnrolledPalms(userId)) {
            android.util.Slog.d(TAG, "Palm addPendingLockoutResetForUser: " + userId);
            this.mPendingResetLockoutsForFace.add(new com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo(userId, gatekeeperPassword));
        }
    }

    void processPendingLockoutResets() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.BiometricDeferredQueue$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processPendingLockoutResets$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processPendingLockoutResets$2() {
        if (!this.mPendingResetLockoutsForFingerprint.isEmpty()) {
            android.util.Slog.d(TAG, "Processing pending resetLockout for fingerprint");
            processPendingLockoutsForFingerprint(new java.util.ArrayList(this.mPendingResetLockoutsForFingerprint));
            this.mPendingResetLockoutsForFingerprint.clear();
        }
        if (!this.mPendingResetLockouts.isEmpty()) {
            android.util.Slog.d(TAG, "Processing pending resetLockouts(Generic)");
            processPendingLockoutsGeneric(new java.util.ArrayList(this.mPendingResetLockouts));
            this.mPendingResetLockouts.clear();
        }
        if (!this.mPendingResetLockoutsForFace.isEmpty()) {
            android.util.Slog.d(TAG, "Processing pending resetLockout for face");
            processPendingLockoutsForFace(new java.util.ArrayList(this.mPendingResetLockoutsForFace));
            this.mPendingResetLockoutsForFace.clear();
        }
    }

    private void processPendingLockoutsForFingerprint(java.util.List<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> pendingResetLockouts) {
        if (this.mFingerprintManager != null) {
            java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> fingerprintSensorProperties = this.mFingerprintManager.getSensorPropertiesInternal();
            for (android.hardware.fingerprint.FingerprintSensorPropertiesInternal prop : fingerprintSensorProperties) {
                if (!prop.resetLockoutRequiresHardwareAuthToken) {
                    java.util.Iterator<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> it = pendingResetLockouts.iterator();
                    while (it.hasNext()) {
                        this.mFingerprintManager.resetLockout(prop.sensorId, it.next().userId, null);
                    }
                } else if (!prop.resetLockoutRequiresChallenge) {
                    for (com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo user : pendingResetLockouts) {
                        android.util.Slog.d(TAG, "Resetting fingerprint lockout for sensor: " + prop.sensorId + ", user: " + user.userId);
                        byte[] hat = requestHatFromGatekeeperPassword(this.mSpManager, user, 0L);
                        if (hat != null) {
                            this.mFingerprintManager.resetLockout(prop.sensorId, user.userId, hat);
                        }
                    }
                } else {
                    android.util.Slog.w(TAG, "No fingerprint HAL interface requires HAT with challenge, sensorId: " + prop.sensorId);
                }
            }
        }
    }

    private void processPendingLockoutsForFace(java.util.List<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> pendingResetLockouts) {
        if (this.mFaceManager != null) {
            if (this.mFaceResetLockoutTask != null) {
                android.util.Slog.w(TAG, "mFaceGenerateChallengeCallback not null, previous operation may be stuck");
            }
            java.util.List<android.hardware.face.FaceSensorPropertiesInternal> faceSensorProperties = this.mFaceManager.getSensorPropertiesInternal();
            java.util.Set<java.lang.Integer> sensorIds = new android.util.ArraySet<>();
            java.util.Iterator<android.hardware.face.FaceSensorPropertiesInternal> it = faceSensorProperties.iterator();
            while (it.hasNext()) {
                sensorIds.add(java.lang.Integer.valueOf(it.next().sensorId));
            }
            this.mFaceResetLockoutTask = new com.android.server.locksettings.BiometricDeferredQueue.FaceResetLockoutTask(this.mFaceFinishCallback, this.mFaceManager, this.mSpManager, sensorIds, pendingResetLockouts);
            for (android.hardware.face.FaceSensorPropertiesInternal prop : faceSensorProperties) {
                if (prop.resetLockoutRequiresHardwareAuthToken) {
                    for (com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo user : pendingResetLockouts) {
                        if (prop.resetLockoutRequiresChallenge) {
                            android.util.Slog.d(TAG, "Generating challenge for sensor: " + prop.sensorId + ", user: " + user.userId);
                            this.mFaceManager.generateChallenge(prop.sensorId, user.userId, this.mFaceResetLockoutTask);
                        } else {
                            android.util.Slog.d(TAG, "Resetting face lockout for sensor: " + prop.sensorId + ", user: " + user.userId);
                            byte[] hat = requestHatFromGatekeeperPassword(this.mSpManager, user, 0L);
                            if (hat != null) {
                                this.mFaceManager.resetLockout(prop.sensorId, user.userId, hat);
                            }
                        }
                    }
                } else {
                    android.util.Slog.w(TAG, "Lockout is below the HAL for all face authentication interfaces, sensorId: " + prop.sensorId);
                }
            }
        }
    }

    private void processPendingLockoutsGeneric(java.util.List<com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo> pendingResetLockouts) {
        for (com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo user : pendingResetLockouts) {
            android.util.Slog.d(TAG, "Resetting biometric lockout for user: " + user.userId);
            byte[] hat = requestHatFromGatekeeperPassword(this.mSpManager, user, 0L);
            if (hat != null) {
                this.mBiometricManager.resetLockout(user.userId, hat);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] requestHatFromGatekeeperPassword(com.android.server.locksettings.SyntheticPasswordManager spManager, com.android.server.locksettings.BiometricDeferredQueue.UserAuthInfo userAuthInfo, long challenge) {
        com.android.internal.widget.VerifyCredentialResponse response = spManager.verifyChallengeInternal(getGatekeeperService(), userAuthInfo.gatekeeperPassword, challenge, userAuthInfo.userId);
        if (response == null) {
            android.util.Slog.wtf(TAG, "VerifyChallenge failed, null response");
            return null;
        }
        if (response.getResponseCode() != 0) {
            android.util.Slog.wtf(TAG, "VerifyChallenge failed, response: " + response.getResponseCode());
            return null;
        }
        if (response.getGatekeeperHAT() == null) {
            android.util.Slog.e(TAG, "Null HAT received from spManager");
        }
        return response.getGatekeeperHAT();
    }

    private static synchronized android.service.gatekeeper.IGateKeeperService getGatekeeperService() {
        android.os.IBinder service = android.os.ServiceManager.waitForService("android.service.gatekeeper.IGateKeeperService");
        if (service == null) {
            android.util.Slog.e(TAG, "Unable to acquire GateKeeperService");
            return null;
        }
        return android.service.gatekeeper.IGateKeeperService.Stub.asInterface(service);
    }
}
