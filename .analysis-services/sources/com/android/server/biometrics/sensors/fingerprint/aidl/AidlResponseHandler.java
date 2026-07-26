package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class AidlResponseHandler extends android.hardware.biometrics.fingerprint.ISessionCallback.Stub {
    private static final java.lang.String TAG = "AidlResponseHandler";
    public static com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerExt mFingerprintAidlResponseHandlerExt = (com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerExt.class).create();
    private final com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback mAidlResponseHandlerCallback;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final android.content.Context mContext;
    private com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerWrapper mFingerprintAidlResponseHandlerWrapper = new com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.OplusFingerprintAidlResponseHandlerWrapper();
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private final com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker;
    private final com.android.server.biometrics.sensors.BiometricScheduler mScheduler;
    private final int mSensorId;
    private final int mUserId;

    public interface AidlResponseHandlerCallback {
        void onEnrollSuccess();

        void onHardwareUnavailable();
    }

    public AidlResponseHandler(android.content.Context context, com.android.server.biometrics.sensors.BiometricScheduler scheduler, int sensorId, int userId, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator, com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback aidlResponseHandlerCallback) {
        this.mContext = context;
        this.mScheduler = scheduler;
        this.mSensorId = sensorId;
        this.mUserId = userId;
        this.mLockoutTracker = lockoutTracker;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mAuthSessionCoordinator = authSessionCoordinator;
        this.mAidlResponseHandlerCallback = aidlResponseHandlerCallback;
    }

    public int getInterfaceVersion() {
        return 4;
    }

    public java.lang.String getInterfaceHash() {
        return "41a730a7a6b5aa9cebebce70ee5b5e509b0af6fb";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onChallengeGenerated$0(long challenge, com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGenerateChallengeClient c) {
        c.onChallengeGenerated(this.mSensorId, this.mUserId, challenge);
    }

    public void onChallengeGenerated(final long challenge) {
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGenerateChallengeClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onChallengeGenerated$0(challenge, (com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGenerateChallengeClient) obj);
            }
        });
    }

    public void onChallengeRevoked(final long challenge) {
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRevokeChallengeClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRevokeChallengeClient) obj).onChallengeRevoked(challenge);
            }
        });
    }

    public void onAcquired(final int acquiredInfo, final int vendorCode) {
        handleResponse(com.android.server.biometrics.sensors.AcquisitionClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.AcquisitionClient) obj).onAcquired(acquiredInfo, vendorCode);
            }
        });
    }

    public void onAcquired(final byte info, final int vendorCode) {
        handleResponse(com.android.server.biometrics.sensors.AcquisitionClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.lambda$onAcquired$3(info, vendorCode, (com.android.server.biometrics.sensors.AcquisitionClient) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onAcquired$3(byte info, int vendorCode, com.android.server.biometrics.sensors.AcquisitionClient c) {
        byte aidlAcquiredInfo = info;
        if ("android.server.biometrics.fingerprint".equals(c.getOwnerString()) && aidlAcquiredInfo == 1 && (c instanceof com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient)) {
            aidlAcquiredInfo = 2;
            android.util.Slog.d(TAG, "aidlAcquiredInfo: 2 ,info:" + ((int) info));
        }
        if (mFingerprintAidlResponseHandlerExt != null && mFingerprintAidlResponseHandlerExt.onAcquired(com.android.server.biometrics.sensors.fingerprint.aidl.AidlConversionUtils.toFrameworkAcquiredInfo(aidlAcquiredInfo), vendorCode)) {
            return;
        }
        c.onAcquired(com.android.server.biometrics.sensors.fingerprint.aidl.AidlConversionUtils.toFrameworkAcquiredInfo(aidlAcquiredInfo), vendorCode);
    }

    public void onError(final int error, final int vendorCode) {
        handleResponse(com.android.server.biometrics.sensors.ErrorConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onError$4(error, vendorCode, (com.android.server.biometrics.sensors.ErrorConsumer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onError$4(int error, int vendorCode, com.android.server.biometrics.sensors.ErrorConsumer c) {
        if (mFingerprintAidlResponseHandlerExt != null && mFingerprintAidlResponseHandlerExt.onError(error, vendorCode)) {
            return;
        }
        c.onError(error, vendorCode);
        if (error == 1) {
            this.mAidlResponseHandlerCallback.onHardwareUnavailable();
        }
    }

    public void onError(byte error, int vendorCode) {
        onError(com.android.server.biometrics.sensors.fingerprint.aidl.AidlConversionUtils.toFrameworkError(error), vendorCode);
    }

    public void onEnrollmentProgress(int enrollmentId, final int remaining) {
        com.android.server.biometrics.sensors.BaseClientMonitor client = this.mScheduler.getCurrentClient();
        if (client == null) {
            return;
        }
        int currentUserId = client.getTargetUserId();
        java.lang.CharSequence name = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(this.mSensorId).getUniqueName(this.mContext, currentUserId);
        final android.hardware.fingerprint.Fingerprint fingerprint = new android.hardware.fingerprint.Fingerprint(name, currentUserId, enrollmentId, this.mSensorId);
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onEnrollmentProgress$5(fingerprint, remaining, (com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$5(android.hardware.fingerprint.Fingerprint fingerprint, int remaining, com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient c) {
        if (mFingerprintAidlResponseHandlerExt != null) {
            mFingerprintAidlResponseHandlerExt.handleOnEnrollment(fingerprint, remaining);
        }
        c.onEnrollResult(fingerprint, remaining);
        if (remaining == 0) {
            this.mAidlResponseHandlerCallback.onEnrollSuccess();
        }
    }

    public void onAuthenticationSucceeded(final int enrollmentId, android.hardware.keymaster.HardwareAuthToken hat) {
        final android.hardware.fingerprint.Fingerprint fp = new android.hardware.fingerprint.Fingerprint("", enrollmentId, this.mSensorId);
        byte[] byteArray = com.android.server.biometrics.HardwareAuthTokenUtils.toByteArray(hat);
        final java.util.ArrayList<java.lang.Byte> byteList = new java.util.ArrayList<>();
        for (byte b : byteArray) {
            byteList.add(java.lang.Byte.valueOf(b));
        }
        handleResponse(com.android.server.biometrics.sensors.AuthenticationConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onAuthenticationSucceeded$6(enrollmentId, fp, byteList, (com.android.server.biometrics.sensors.AuthenticationConsumer) obj);
            }
        }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onAuthenticationSucceeded$7((com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAuthenticationSucceeded$6(int enrollmentId, android.hardware.fingerprint.Fingerprint fp, java.util.ArrayList byteList, com.android.server.biometrics.sensors.AuthenticationConsumer c) {
        if (mFingerprintAidlResponseHandlerExt != null && mFingerprintAidlResponseHandlerExt.onAuthenticated(this.mSensorId, enrollmentId, fp.getGroupId(), byteList)) {
            return;
        }
        c.onAuthenticated(fp, true, byteList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAuthenticationSucceeded$7(com.android.server.biometrics.sensors.BaseClientMonitor c) {
        onInteractionDetected();
    }

    public void onAuthenticationFailed() {
        final android.hardware.fingerprint.Fingerprint fp = new android.hardware.fingerprint.Fingerprint("", 0, this.mSensorId);
        handleResponse(com.android.server.biometrics.sensors.AuthenticationConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onAuthenticationFailed$8(fp, (com.android.server.biometrics.sensors.AuthenticationConsumer) obj);
            }
        }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onAuthenticationFailed$9((com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAuthenticationFailed$8(android.hardware.fingerprint.Fingerprint fp, com.android.server.biometrics.sensors.AuthenticationConsumer c) {
        if (mFingerprintAidlResponseHandlerExt != null && mFingerprintAidlResponseHandlerExt.onAuthenticated(this.mSensorId, 0, fp.getGroupId(), null)) {
            return;
        }
        c.onAuthenticated(fp, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAuthenticationFailed$9(com.android.server.biometrics.sensors.BaseClientMonitor c) {
        onInteractionDetected();
    }

    public void onLockoutTimed(final long durationMillis) {
        handleResponse(com.android.server.biometrics.sensors.LockoutConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.lambda$onLockoutTimed$10(durationMillis, (com.android.server.biometrics.sensors.LockoutConsumer) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onLockoutTimed$10(long durationMillis, com.android.server.biometrics.sensors.LockoutConsumer c) {
        if (mFingerprintAidlResponseHandlerExt.getProvider().getServiceProviderAidlEx() != null && (mFingerprintAidlResponseHandlerExt.getProvider().getServiceProviderAidlEx().getFailedAttempts() + 1) % 5 != 0) {
            android.util.Slog.e(TAG, "onLockoutTimed maybe just in touch mode, user not press side key");
            return;
        }
        if (mFingerprintAidlResponseHandlerExt != null) {
            mFingerprintAidlResponseHandlerExt.onLockoutTimed();
        }
        c.onLockoutTimed(durationMillis);
    }

    public void onLockoutPermanent() {
        handleResponse(com.android.server.biometrics.sensors.LockoutConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.lambda$onLockoutPermanent$11((com.android.server.biometrics.sensors.LockoutConsumer) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onLockoutPermanent$11(com.android.server.biometrics.sensors.LockoutConsumer c) {
        if (mFingerprintAidlResponseHandlerExt.getProvider().getServiceProviderAidlEx() != null && (mFingerprintAidlResponseHandlerExt.getProvider().getServiceProviderAidlEx().getFailedAttempts() + 1) % 5 != 0) {
            android.util.Slog.e(TAG, "onLockoutPermanent maybe just in touch mode, user not press side key");
            return;
        }
        if (mFingerprintAidlResponseHandlerExt != null) {
            mFingerprintAidlResponseHandlerExt.onLockoutPermanent();
        }
        c.onLockoutPermanent();
    }

    public void onLockoutCleared() {
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintResetLockoutClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintResetLockoutClient) obj).onLockoutCleared();
            }
        }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onLockoutCleared$12((com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLockoutCleared$12(com.android.server.biometrics.sensors.BaseClientMonitor c) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintResetLockoutClient.resetLocalLockoutStateToNone(this.mSensorId, this.mUserId, this.mLockoutTracker, this.mLockoutResetDispatcher, this.mAuthSessionCoordinator, com.android.server.biometrics.Utils.getCurrentStrength(this.mSensorId), -1L);
    }

    public void onInteractionDetected() {
        android.util.Slog.d(TAG, "[onInteractionDetected]: mFingerprintAidlResponseHandlerExt=" + mFingerprintAidlResponseHandlerExt);
        if (mFingerprintAidlResponseHandlerExt != null) {
            mFingerprintAidlResponseHandlerExt.onInteractionDetected();
        }
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient) obj).onInteractionDetected();
            }
        });
    }

    public void onEnrollmentsEnumerated(int[] enrollmentIds) {
        if (enrollmentIds.length > 0) {
            for (int i = 0; i < enrollmentIds.length; i++) {
                onEnrollmentEnumerated(enrollmentIds[i], (enrollmentIds.length - i) - 1);
            }
            return;
        }
        handleResponse(com.android.server.biometrics.sensors.EnumerateConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.EnumerateConsumer) obj).onEnumerationResult(null, 0);
            }
        });
    }

    public void onEnrollmentEnumerated(int enrollmentId, final int remaining) {
        final android.hardware.fingerprint.Fingerprint fp = new android.hardware.fingerprint.Fingerprint("", enrollmentId, this.mSensorId);
        handleResponse(com.android.server.biometrics.sensors.EnumerateConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.EnumerateConsumer) obj).onEnumerationResult(fp, remaining);
            }
        });
    }

    public void onEnrollmentRemoved(int enrollmentId, final int remaining) {
        final android.hardware.fingerprint.Fingerprint fp = new android.hardware.fingerprint.Fingerprint("", enrollmentId, this.mSensorId);
        handleResponse(com.android.server.biometrics.sensors.RemovalConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.RemovalConsumer) obj).onRemoved(fp, remaining);
            }
        });
    }

    public void onEnrollmentsRemoved(int[] enrollmentIds) {
        if (enrollmentIds.length > 0) {
            for (int i = 0; i < enrollmentIds.length; i++) {
                onEnrollmentRemoved(enrollmentIds[i], (enrollmentIds.length - i) - 1);
            }
            return;
        }
        handleResponse(com.android.server.biometrics.sensors.RemovalConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.RemovalConsumer) obj).onRemoved(null, 0);
            }
        });
    }

    public void onAuthenticatorIdRetrieved(final long authenticatorId) {
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGetAuthenticatorIdClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGetAuthenticatorIdClient) obj).onAuthenticatorIdRetrieved(authenticatorId);
            }
        });
    }

    public void onAuthenticatorIdInvalidated(final long newAuthenticatorId) {
        handleResponse(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInvalidationClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInvalidationClient) obj).onAuthenticatorIdInvalidated(newAuthenticatorId);
            }
        });
    }

    public <T extends com.android.server.biometrics.sensors.BaseClientMonitor> void onUnsupportedClientScheduled(java.lang.Class<T> className) {
        android.util.Slog.e(TAG, className + " is not supported in the HAL.");
        handleResponse(className, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.BaseClientMonitor) obj).cancel();
            }
        });
    }

    private <T> void handleResponse(java.lang.Class<T> className, java.util.function.Consumer<T> action) {
        handleResponse(className, action, null);
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        android.util.Slog.d(TAG, "[onTransact]code " + code + " flags: " + flags);
        if (super.onTransact(code, data, reply, flags)) {
            return true;
        }
        if (mFingerprintAidlResponseHandlerExt != null && mFingerprintAidlResponseHandlerExt.onTransactFromHal(code, data, reply, flags)) {
            return true;
        }
        android.util.Slog.d(TAG, "[onTransact]code " + code + " flags: " + flags);
        return false;
    }

    private <T> void handleResponse(final java.lang.Class<T> className, final java.util.function.Consumer<T> action, final java.util.function.Consumer<com.android.server.biometrics.sensors.BaseClientMonitor> alternateAction) {
        this.mScheduler.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleResponse$20(className, action, alternateAction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleResponse$20(java.lang.Class className, java.util.function.Consumer action, java.util.function.Consumer alternateAction) {
        com.android.server.biometrics.sensors.BaseClientMonitor client = this.mScheduler.getCurrentClient();
        if (className.isInstance(client)) {
            action.accept(client);
            return;
        }
        android.util.Slog.e(TAG, "Client monitor is not an instance of " + className.getName());
        if (alternateAction != null) {
            alternateAction.accept(client);
        }
    }

    public void handleOnFingerprintCmd(int cmdId, byte[] result, int resultLen) {
        if (mFingerprintAidlResponseHandlerExt != null) {
            mFingerprintAidlResponseHandlerExt.handleOnFingerprintCmd(cmdId, result, resultLen);
        }
    }

    public void handleOnEngineeringInfoUpdated(int length, java.util.ArrayList<java.lang.Integer> keys, java.util.ArrayList<java.lang.String> values) {
        if (mFingerprintAidlResponseHandlerExt != null) {
            mFingerprintAidlResponseHandlerExt.handleOnEngineeringInfoUpdated(length, keys, values);
        }
    }

    public void onSessionClosed() {
        android.os.Handler handler = this.mScheduler.getHandler();
        com.android.server.biometrics.sensors.BiometricScheduler biometricScheduler = this.mScheduler;
        java.util.Objects.requireNonNull(biometricScheduler);
        handler.post(new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda24(biometricScheduler));
    }

    public com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerWrapper getWrapper() {
        return this.mFingerprintAidlResponseHandlerWrapper;
    }

    private class OplusFingerprintAidlResponseHandlerWrapper implements com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerWrapper {
        private OplusFingerprintAidlResponseHandlerWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerWrapper
        public com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAidlResponseHandlerExt getExtImpl() {
            return com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.mFingerprintAidlResponseHandlerExt;
        }
    }
}
