package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class AidlResponseHandler extends android.hardware.biometrics.face.ISessionCallback.Stub {
    private static final int FACE_ACQUIRED_VENDOR_IMAGA_BUFFER_SEQ = 1003;
    private static final int REMAINING_FOR_SIMILAR_FACE = 9999;
    private static final java.lang.String TAG = "AidlResponseHandler";
    private final com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback mAidlResponseHandlerCallback;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final android.content.Context mContext;
    private com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerWrapper mFaceAidlResponseHandlerWrapper = new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.OplusFaceAidlResponseHandlerWrapper();
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private final com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker;
    private final com.android.server.biometrics.sensors.BiometricScheduler mScheduler;
    private final int mSensorId;
    private final int mUserId;
    private static int mImageBufferSeq = 0;
    private static com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerExt mFaceAidlResponseHandlerExt = (com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerExt.class).create();

    public interface AidlResponseHandlerCallback {
        void onEnrollSuccess();

        void onHardwareUnavailable();
    }

    public AidlResponseHandler(android.content.Context context, com.android.server.biometrics.sensors.BiometricScheduler scheduler, int sensorId, int userId, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator, com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback aidlResponseHandlerCallback) {
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
        return "c43fbb9be4a662cc9ace640dba21cccdb84c6c21";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onChallengeGenerated$0(long challenge, com.android.server.biometrics.sensors.face.aidl.FaceGenerateChallengeClient c) {
        c.onChallengeGenerated(this.mSensorId, this.mUserId, challenge);
    }

    public void onChallengeGenerated(final long challenge) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceGenerateChallengeClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onChallengeGenerated$0(challenge, (com.android.server.biometrics.sensors.face.aidl.FaceGenerateChallengeClient) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onChallengeRevoked$1(long challenge, com.android.server.biometrics.sensors.face.aidl.FaceRevokeChallengeClient c) {
        c.onChallengeRevoked(this.mSensorId, this.mUserId, challenge);
    }

    public void onChallengeRevoked(final long challenge) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceRevokeChallengeClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onChallengeRevoked$1(challenge, (com.android.server.biometrics.sensors.face.aidl.FaceRevokeChallengeClient) obj);
            }
        });
    }

    public void onAuthenticationFrame(final android.hardware.biometrics.face.AuthenticationFrame frame) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.lambda$onAuthenticationFrame$2(frame, (com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onAuthenticationFrame$2(android.hardware.biometrics.face.AuthenticationFrame frame, com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient c) {
        if (frame == null) {
            android.util.Slog.e(TAG, "Received null enrollment frame for face authentication client.");
            return;
        }
        mImageBufferSeq++;
        mFaceAidlResponseHandlerExt.onAcquired(mImageBufferSeq, 1003);
        if (mFaceAidlResponseHandlerExt.onAcquired(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkAuthenticationFrame(frame).getData().getAcquiredInfo(), com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkAuthenticationFrame(frame).getData().getVendorCode())) {
            android.util.Slog.d(TAG, "skip authentication frame return");
        } else {
            c.onAuthenticationFrame(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkAuthenticationFrame(frame));
        }
    }

    public void onEnrollmentFrame(final android.hardware.biometrics.face.EnrollmentFrame frame) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.lambda$onEnrollmentFrame$3(frame, (com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onEnrollmentFrame$3(android.hardware.biometrics.face.EnrollmentFrame frame, com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient c) {
        if (frame == null) {
            android.util.Slog.e(TAG, "Received null enrollment frame for face enroll client.");
        } else if (mFaceAidlResponseHandlerExt.onAcquired(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkEnrollmentFrame(frame).getData().getAcquiredInfo(), com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkEnrollmentFrame(frame).getData().getVendorCode())) {
            android.util.Slog.d(TAG, "skip enrollment frame return");
        } else {
            c.onEnrollmentFrame(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkEnrollmentFrame(frame));
        }
    }

    public void onError(byte error, int vendorCode) {
        onError(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.toFrameworkError(error), vendorCode);
    }

    public void onError(final int error, final int vendorCode) {
        handleResponse(com.android.server.biometrics.sensors.ErrorConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onError$4(error, vendorCode, (com.android.server.biometrics.sensors.ErrorConsumer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onError$4(int error, int vendorCode, com.android.server.biometrics.sensors.ErrorConsumer c) {
        c.onError(error, vendorCode);
        if (error == 1) {
            this.mAidlResponseHandlerCallback.onHardwareUnavailable();
        }
        mImageBufferSeq = 0;
        mFaceAidlResponseHandlerExt.onError(error, vendorCode);
    }

    public void onEnrollmentProgress(int enrollmentId, final int remaining) {
        com.android.server.biometrics.sensors.BaseClientMonitor client = this.mScheduler.getCurrentClient();
        if (client == null) {
            return;
        }
        int currentUserId = client.getTargetUserId();
        if (remaining == REMAINING_FOR_SIMILAR_FACE) {
            return;
        }
        java.lang.CharSequence name = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(this.mSensorId).getUniqueName(this.mContext, currentUserId);
        final android.hardware.face.Face face = new android.hardware.face.Face(name, enrollmentId, this.mSensorId);
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onEnrollmentProgress$5(face, remaining, (com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$5(android.hardware.face.Face face, int remaining, com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient c) {
        c.onEnrollResult(face, remaining);
        if (remaining == 0) {
            this.mAidlResponseHandlerCallback.onEnrollSuccess();
        }
    }

    public void onAuthenticationSucceeded(int enrollmentId, android.hardware.keymaster.HardwareAuthToken hat) {
        final android.hardware.face.Face face = new android.hardware.face.Face("", enrollmentId, this.mSensorId);
        byte[] byteArray = com.android.server.biometrics.HardwareAuthTokenUtils.toByteArray(hat);
        final java.util.ArrayList<java.lang.Byte> byteList = new java.util.ArrayList<>();
        for (byte b : byteArray) {
            byteList.add(java.lang.Byte.valueOf(b));
        }
        handleResponse(com.android.server.biometrics.sensors.AuthenticationConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.lambda$onAuthenticationSucceeded$6(face, byteList, (com.android.server.biometrics.sensors.AuthenticationConsumer) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onAuthenticationSucceeded$6(android.hardware.face.Face face, java.util.ArrayList byteList, com.android.server.biometrics.sensors.AuthenticationConsumer c) {
        c.onAuthenticated(face, true, byteList);
        mImageBufferSeq = 0;
        mFaceAidlResponseHandlerExt.onAuthenticated(true);
    }

    public void onAuthenticationFailed() {
        final android.hardware.face.Face face = new android.hardware.face.Face("", 0, this.mSensorId);
        handleResponse(com.android.server.biometrics.sensors.AuthenticationConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.lambda$onAuthenticationFailed$7(face, (com.android.server.biometrics.sensors.AuthenticationConsumer) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onAuthenticationFailed$7(android.hardware.face.Face face, com.android.server.biometrics.sensors.AuthenticationConsumer c) {
        c.onAuthenticated(face, false, null);
        mImageBufferSeq = 0;
        mFaceAidlResponseHandlerExt.onAuthenticated(false);
    }

    public void onLockoutTimed(final long durationMillis) {
        handleResponse(com.android.server.biometrics.sensors.LockoutConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.lambda$onLockoutTimed$8(durationMillis, (com.android.server.biometrics.sensors.LockoutConsumer) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onLockoutTimed$8(long durationMillis, com.android.server.biometrics.sensors.LockoutConsumer c) {
        mFaceAidlResponseHandlerExt.onLockoutTimed();
        c.onLockoutTimed(durationMillis);
    }

    public void onLockoutPermanent() {
        handleResponse(com.android.server.biometrics.sensors.LockoutConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.lambda$onLockoutPermanent$9((com.android.server.biometrics.sensors.LockoutConsumer) obj);
            }
        });
    }

    static /* synthetic */ void lambda$onLockoutPermanent$9(com.android.server.biometrics.sensors.LockoutConsumer c) {
        mFaceAidlResponseHandlerExt.onLockoutPermanent();
        c.onLockoutPermanent();
    }

    public void onLockoutCleared() {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceResetLockoutClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceResetLockoutClient) obj).onLockoutCleared();
            }
        }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onLockoutCleared$10((com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLockoutCleared$10(com.android.server.biometrics.sensors.BaseClientMonitor c) {
        com.android.server.biometrics.sensors.face.aidl.FaceResetLockoutClient.resetLocalLockoutStateToNone(this.mSensorId, this.mUserId, this.mLockoutTracker, this.mLockoutResetDispatcher, this.mAuthSessionCoordinator, com.android.server.biometrics.Utils.getCurrentStrength(this.mSensorId), -1L);
    }

    public void onInteractionDetected() {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceDetectClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceDetectClient) obj).onInteractionDetected();
            }
        });
    }

    public void onEnrollmentsEnumerated(final int[] enrollmentIds) {
        if (enrollmentIds.length > 0) {
            for (int i = 0; i < enrollmentIds.length; i++) {
                final android.hardware.face.Face face = new android.hardware.face.Face("", enrollmentIds[i], this.mSensorId);
                final int finalI = i;
                handleResponse(com.android.server.biometrics.sensors.EnumerateConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda17
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        android.hardware.face.Face face2 = face;
                        int[] iArr = enrollmentIds;
                        int i2 = finalI;
                        ((com.android.server.biometrics.sensors.EnumerateConsumer) obj).onEnumerationResult(face2, (iArr.length - i2) - 1);
                    }
                });
            }
            return;
        }
        handleResponse(com.android.server.biometrics.sensors.EnumerateConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.EnumerateConsumer) obj).onEnumerationResult(null, 0);
            }
        });
    }

    public void onFeaturesRetrieved(final byte[] features) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceGetFeatureClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceGetFeatureClient) obj).onFeatureGet(true, features);
            }
        });
    }

    public void onFeatureSet(byte feature) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceSetFeatureClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceSetFeatureClient) obj).onFeatureSet(true);
            }
        });
    }

    public void onEnrollmentsRemoved(final int[] enrollmentIds) {
        if (enrollmentIds.length > 0) {
            for (int i = 0; i < enrollmentIds.length; i++) {
                final android.hardware.face.Face face = new android.hardware.face.Face("", enrollmentIds[i], this.mSensorId);
                final int finalI = i;
                handleResponse(com.android.server.biometrics.sensors.RemovalConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        android.hardware.face.Face face2 = face;
                        int[] iArr = enrollmentIds;
                        int i2 = finalI;
                        ((com.android.server.biometrics.sensors.RemovalConsumer) obj).onRemoved(face2, (iArr.length - i2) - 1);
                    }
                });
            }
            return;
        }
        handleResponse(com.android.server.biometrics.sensors.RemovalConsumer.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.RemovalConsumer) obj).onRemoved(null, 0);
            }
        });
    }

    public void onAuthenticatorIdRetrieved(final long authenticatorId) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceGetAuthenticatorIdClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceGetAuthenticatorIdClient) obj).onAuthenticatorIdRetrieved(authenticatorId);
            }
        });
    }

    public void onAuthenticatorIdInvalidated(final long newAuthenticatorId) {
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceInvalidationClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceInvalidationClient) obj).onAuthenticatorIdInvalidated(newAuthenticatorId);
            }
        });
    }

    public void onAcquired(final int acquiredInfo, final int vendorCode) {
        handleResponse(com.android.server.biometrics.sensors.AcquisitionClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.AcquisitionClient) obj).onAcquired(acquiredInfo, vendorCode);
            }
        });
    }

    public void onLockoutChanged(final long duration) {
        this.mScheduler.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onLockoutChanged$20(duration);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLockoutChanged$20(long duration) {
        int lockoutMode;
        if (duration == 0) {
            lockoutMode = 0;
        } else if (duration == -1 || duration == Long.MAX_VALUE) {
            lockoutMode = 2;
        } else {
            lockoutMode = 1;
        }
        this.mLockoutTracker.setLockoutModeForUser(this.mUserId, lockoutMode);
        if (duration == 0) {
            this.mLockoutResetDispatcher.notifyLockoutResetCallbacks(this.mSensorId);
        }
    }

    public void onUnsupportedClientScheduled() {
        android.util.Slog.e(TAG, "FaceInvalidationClient is not supported in the HAL.");
        handleResponse(com.android.server.biometrics.sensors.face.aidl.FaceInvalidationClient.class, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.biometrics.sensors.face.aidl.FaceInvalidationClient) obj).cancel();
            }
        });
    }

    private <T> void handleResponse(java.lang.Class<T> className, java.util.function.Consumer<T> action) {
        handleResponse(className, action, null);
    }

    private <T> void handleResponse(final java.lang.Class<T> className, final java.util.function.Consumer<T> actionIfClassMatchesClient, final java.util.function.Consumer<com.android.server.biometrics.sensors.BaseClientMonitor> alternateAction) {
        this.mScheduler.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleResponse$21(className, actionIfClassMatchesClient, alternateAction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleResponse$21(java.lang.Class className, java.util.function.Consumer actionIfClassMatchesClient, java.util.function.Consumer alternateAction) {
        com.android.server.biometrics.sensors.BaseClientMonitor client = this.mScheduler.getCurrentClient();
        if (className.isInstance(client)) {
            actionIfClassMatchesClient.accept(client);
            return;
        }
        android.util.Slog.d(TAG, "Current client is not an instance of " + className.getName());
        if (alternateAction != null) {
            alternateAction.accept(client);
        }
    }

    public void onSessionClosed() {
        android.os.Handler handler = this.mScheduler.getHandler();
        com.android.server.biometrics.sensors.BiometricScheduler biometricScheduler = this.mScheduler;
        java.util.Objects.requireNonNull(biometricScheduler);
        handler.post(new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler$$ExternalSyntheticLambda24(biometricScheduler));
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (super.onTransact(code, data, reply, flags)) {
            return true;
        }
        if (mFaceAidlResponseHandlerExt != null && mFaceAidlResponseHandlerExt.onTransactFromHal(code, data, reply, flags)) {
            return true;
        }
        android.util.Slog.d(TAG, "[onTransact]code " + code + " flags: " + flags);
        return false;
    }

    public com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerWrapper getWrapper() {
        return this.mFaceAidlResponseHandlerWrapper;
    }

    private class OplusFaceAidlResponseHandlerWrapper implements com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerWrapper {
        private OplusFaceAidlResponseHandlerWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerWrapper
        public com.android.server.biometrics.sensors.face.aidl.IFaceAidlResponseHandlerExt getExtImpl() {
            return com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.mFaceAidlResponseHandlerExt;
        }
    }
}
