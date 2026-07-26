package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: loaded from: classes.dex */
public class HidlToAidlSessionAdapter implements android.hardware.biometrics.face.ISession {
    static final int CHALLENGE_TIMEOUT_SEC = 600;
    static final int ENROLL_TIMEOUT_SEC = 75;
    private static final int GENERATE_CHALLENGE_COUNTER_TTL_MILLIS = 600000;
    private static final int GENERATE_CHALLENGE_REUSE_INTERVAL_MILLIS = 60000;
    private static final int INVALID_VALUE = -1;
    private static final java.lang.String TAG = "HidlToAidlSessionAdapter";
    private final java.time.Clock mClock;
    private final android.content.Context mContext;
    private int mFeature;
    private long mGenerateChallengeCreatedAt;
    private long mGenerateChallengeResult;
    private final java.util.List<java.lang.Long> mGeneratedChallengeCount;
    private com.android.server.biometrics.sensors.face.hidl.HidlToAidlCallbackConverter mHidlToAidlCallbackConverter;
    private java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> mSession;
    private final int mUserId;

    public HidlToAidlSessionAdapter(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> session, int userId, com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler aidlResponseHandler) {
        this(context, session, userId, aidlResponseHandler, java.time.Clock.systemUTC());
    }

    HidlToAidlSessionAdapter(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> session, int userId, com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler aidlResponseHandler, java.time.Clock clock) {
        this.mGeneratedChallengeCount = new java.util.ArrayList();
        this.mGenerateChallengeCreatedAt = -1L;
        this.mGenerateChallengeResult = -1L;
        this.mFeature = -1;
        this.mSession = session;
        this.mUserId = userId;
        this.mContext = context;
        this.mClock = clock;
        setCallback(aidlResponseHandler);
    }

    public android.os.IBinder asBinder() {
        return null;
    }

    public void generateChallenge() throws android.os.RemoteException {
        incrementChallengeCount();
        if (isGeneratedChallengeCacheValid()) {
            android.util.Slog.d(TAG, "Current challenge is cached and will be reused");
            this.mHidlToAidlCallbackConverter.onChallengeGenerated(this.mGenerateChallengeResult);
        } else {
            this.mGenerateChallengeCreatedAt = this.mClock.millis();
            this.mGenerateChallengeResult = this.mSession.get().generateChallenge(600).value;
            this.mHidlToAidlCallbackConverter.onChallengeGenerated(this.mGenerateChallengeResult);
        }
    }

    public void revokeChallenge(long challenge) throws android.os.RemoteException {
        boolean shouldRevoke = decrementChallengeCount() == 0;
        if (!shouldRevoke) {
            android.util.Slog.w(TAG, "scheduleRevokeChallenge skipped - challenge still in use: " + this.mGeneratedChallengeCount);
            this.mHidlToAidlCallbackConverter.onError(0L, this.mUserId, 2, 0);
        } else {
            this.mGenerateChallengeCreatedAt = -1L;
            this.mGenerateChallengeResult = -1L;
            this.mSession.get().revokeChallenge();
            this.mHidlToAidlCallbackConverter.onChallengeRevoked(0L);
        }
    }

    public android.hardware.biometrics.face.EnrollmentStageConfig[] getEnrollmentConfig(byte enrollmentType) throws android.os.RemoteException {
        android.util.Slog.e(TAG, "getEnrollmentConfig unsupported in HIDL");
        return null;
    }

    public android.hardware.biometrics.common.ICancellationSignal enroll(android.hardware.keymaster.HardwareAuthToken hat, byte type, byte[] features, android.hardware.common.NativeHandle previewSurface) throws android.os.RemoteException {
        java.util.ArrayList<java.lang.Byte> token = new java.util.ArrayList<>();
        byte[] hardwareAuthTokenArray = com.android.server.biometrics.HardwareAuthTokenUtils.toByteArray(hat);
        for (byte b : hardwareAuthTokenArray) {
            token.add(java.lang.Byte.valueOf(b));
        }
        java.util.ArrayList<java.lang.Integer> disabledFeatures = new java.util.ArrayList<>();
        for (byte b2 : features) {
            disabledFeatures.add(java.lang.Integer.valueOf(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.convertAidlToFrameworkFeature(b2)));
        }
        this.mSession.get().enroll(token, 75, disabledFeatures);
        return new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter.Cancellation();
    }

    public android.hardware.biometrics.common.ICancellationSignal authenticate(long operationId) throws android.os.RemoteException {
        this.mSession.get().authenticate(operationId);
        return new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter.Cancellation();
    }

    public android.hardware.biometrics.common.ICancellationSignal detectInteraction() throws android.os.RemoteException {
        this.mSession.get().authenticate(0L);
        return new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter.Cancellation();
    }

    public void enumerateEnrollments() throws android.os.RemoteException {
        this.mSession.get().enumerate();
    }

    public void removeEnrollments(int[] enrollmentIds) throws android.os.RemoteException {
        this.mSession.get().remove(enrollmentIds[0]);
    }

    public void setFeature(int feature) {
        this.mFeature = feature;
    }

    public void getFeatures() throws android.os.RemoteException {
        int faceId = getFaceId();
        if (faceId == -1 || this.mFeature == -1) {
            return;
        }
        android.hardware.biometrics.face.V1_0.OptionalBool result = this.mSession.get().getFeature(this.mFeature, faceId);
        if (result.status == 0 && result.value) {
            this.mHidlToAidlCallbackConverter.onFeatureGet(new byte[]{com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.convertFrameworkToAidlFeature(this.mFeature)});
        } else if (result.status == 0) {
            this.mHidlToAidlCallbackConverter.onFeatureGet(new byte[0]);
        } else {
            this.mHidlToAidlCallbackConverter.onError(0L, this.mUserId, 17, 0);
        }
        this.mFeature = -1;
    }

    public void setFeature(android.hardware.keymaster.HardwareAuthToken hat, byte feature, boolean enabled) throws android.os.RemoteException {
        int faceId = getFaceId();
        if (faceId == -1) {
            return;
        }
        java.util.ArrayList<java.lang.Byte> hardwareAuthTokenList = new java.util.ArrayList<>();
        for (byte b : com.android.server.biometrics.HardwareAuthTokenUtils.toByteArray(hat)) {
            hardwareAuthTokenList.add(java.lang.Byte.valueOf(b));
        }
        int result = this.mSession.get().setFeature(com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.convertAidlToFrameworkFeature(feature), enabled, hardwareAuthTokenList, faceId);
        if (result == 0) {
            this.mHidlToAidlCallbackConverter.onFeatureSet(feature);
        } else {
            this.mHidlToAidlCallbackConverter.onError(0L, this.mUserId, 17, 0);
        }
    }

    public void getAuthenticatorId() throws android.os.RemoteException {
        long authenticatorId = this.mSession.get().getAuthenticatorId().value;
        this.mHidlToAidlCallbackConverter.onAuthenticatorIdRetrieved(authenticatorId);
    }

    public void invalidateAuthenticatorId() throws android.os.RemoteException {
        android.util.Slog.e(TAG, "invalidateAuthenticatorId unsupported in HIDL");
        this.mHidlToAidlCallbackConverter.onUnsupportedClientScheduled();
    }

    public void resetLockout(android.hardware.keymaster.HardwareAuthToken hat) throws android.os.RemoteException {
        java.util.ArrayList<java.lang.Byte> hardwareAuthToken = new java.util.ArrayList<>();
        for (byte b : com.android.server.biometrics.HardwareAuthTokenUtils.toByteArray(hat)) {
            hardwareAuthToken.add(java.lang.Byte.valueOf(b));
        }
        this.mSession.get().resetLockout(hardwareAuthToken);
    }

    public void close() throws android.os.RemoteException {
        android.util.Slog.e(TAG, "close unsupported in HIDL");
    }

    public android.hardware.biometrics.common.ICancellationSignal authenticateWithContext(long operationId, android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Slog.e(TAG, "authenticateWithContext unsupported in HIDL");
        return authenticate(operationId);
    }

    public android.hardware.biometrics.common.ICancellationSignal enrollWithContext(android.hardware.keymaster.HardwareAuthToken hat, byte type, byte[] features, android.hardware.common.NativeHandle previewSurface, android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Slog.e(TAG, "enrollWithContext unsupported in HIDL");
        return enroll(hat, type, features, previewSurface);
    }

    public android.hardware.biometrics.common.ICancellationSignal detectInteractionWithContext(android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Slog.e(TAG, "detectInteractionWithContext unsupported in HIDL");
        return detectInteraction();
    }

    public void onContextChanged(android.hardware.biometrics.common.OperationContext context) throws android.os.RemoteException {
        android.util.Slog.e(TAG, "onContextChanged unsupported in HIDL");
    }

    public int getInterfaceVersion() throws android.os.RemoteException {
        android.util.Slog.e(TAG, "getInterfaceVersion unsupported in HIDL");
        return 0;
    }

    public java.lang.String getInterfaceHash() throws android.os.RemoteException {
        android.util.Slog.e(TAG, "getInterfaceHash unsupported in HIDL");
        return null;
    }

    public android.hardware.biometrics.common.ICancellationSignal enrollWithOptions(android.hardware.biometrics.face.FaceEnrollOptions options) {
        android.util.Slog.e(TAG, "enrollWithOptions unsupported in HIDL");
        return null;
    }

    private boolean isGeneratedChallengeCacheValid() {
        return (this.mGenerateChallengeCreatedAt == -1 || this.mGenerateChallengeResult == -1 || this.mClock.millis() - this.mGenerateChallengeCreatedAt >= 60000) ? false : true;
    }

    private void incrementChallengeCount() {
        this.mGeneratedChallengeCount.add(0, java.lang.Long.valueOf(this.mClock.millis()));
    }

    private int decrementChallengeCount() {
        final long now = this.mClock.millis();
        this.mGeneratedChallengeCount.removeIf(new java.util.function.Predicate() { // from class: com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter.lambda$decrementChallengeCount$0(now, (java.lang.Long) obj);
            }
        });
        if (!this.mGeneratedChallengeCount.isEmpty()) {
            this.mGeneratedChallengeCount.remove(0);
        }
        return this.mGeneratedChallengeCount.size();
    }

    static /* synthetic */ boolean lambda$decrementChallengeCount$0(long now, java.lang.Long x) {
        return now - x.longValue() > 600000;
    }

    private void setCallback(com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler aidlResponseHandler) {
        this.mHidlToAidlCallbackConverter = new com.android.server.biometrics.sensors.face.hidl.HidlToAidlCallbackConverter(aidlResponseHandler);
        try {
            if (this.mSession.get() == null) {
                android.util.Slog.e(TAG, "Unable to set HIDL callback. HIDL daemon is null.");
                return;
            }
            long halId = this.mSession.get().setCallback(this.mHidlToAidlCallbackConverter).value;
            android.util.Slog.d(TAG, "Face HAL ready, HAL ID: " + halId);
            if (halId == 0) {
                android.util.Slog.d(TAG, "Unable to set HIDL callback.");
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.d(TAG, "Failed to set callback");
        }
    }

    private int getFaceId() {
        android.hardware.face.FaceManager faceManager = (android.hardware.face.FaceManager) this.mContext.getSystemService(android.hardware.face.FaceManager.class);
        java.util.List<android.hardware.face.Face> faces = faceManager.getEnrolledFaces(this.mUserId);
        if (faces.isEmpty()) {
            android.util.Slog.d(TAG, "No faces to get feature from.");
            this.mHidlToAidlCallbackConverter.onError(0L, this.mUserId, 11, 0);
            return -1;
        }
        return faces.get(0).getBiometricId();
    }

    private class Cancellation extends android.hardware.biometrics.common.ICancellationSignal.Stub {
        Cancellation() {
        }

        public void cancel() throws android.os.RemoteException {
            try {
                ((android.hardware.biometrics.face.V1_0.IBiometricsFace) com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter.this.mSession.get()).cancel();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.biometrics.sensors.face.hidl.HidlToAidlSessionAdapter.TAG, "Remote exception when requesting cancel", e);
            }
        }

        public int getInterfaceVersion() throws android.os.RemoteException {
            return 0;
        }

        public java.lang.String getInterfaceHash() throws android.os.RemoteException {
            return null;
        }
    }
}
