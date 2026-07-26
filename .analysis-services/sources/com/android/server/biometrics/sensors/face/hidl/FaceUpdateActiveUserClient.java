package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceUpdateActiveUserClient extends com.android.server.biometrics.sensors.StartUserClient<android.hardware.biometrics.face.V1_0.IBiometricsFace, com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    private static final java.lang.String FACE_DATA_DIR = "facedata";
    private static final java.lang.String TAG = "FaceUpdateActiveUserClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    private final boolean mHasEnrolledBiometrics;

    FaceUpdateActiveUserClient(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, boolean hasEnrolledBiometrics, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        this(context, lazyDaemon, new com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.face.hidl.FaceUpdateActiveUserClient$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
            public final void onUserStarted(int i, java.lang.Object obj, int i2) {
                com.android.server.biometrics.sensors.face.hidl.FaceUpdateActiveUserClient.lambda$new$0(i, obj, i2);
            }
        }, userId, owner, sensorId, logger, biometricContext, hasEnrolledBiometrics, authenticatorIds);
    }

    static /* synthetic */ void lambda$new$0(int newUserId, java.lang.Object newUser, int halInterfaceVersion) {
    }

    FaceUpdateActiveUserClient(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.face.V1_0.IBiometricsFace> lazyDaemon, com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback userStartedCallback, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, boolean hasEnrolledBiometrics, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, null, userId, sensorId, logger, biometricContext, userStartedCallback);
        this.mHasEnrolledBiometrics = hasEnrolledBiometrics;
        this.mAuthenticatorIds = authenticatorIds;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        java.io.File storePath = new java.io.File(android.os.Environment.getDataVendorDeDirectory(getTargetUserId()), FACE_DATA_DIR);
        if (!storePath.exists()) {
            android.util.Slog.e(TAG, "vold has not created the directory?");
            this.mCallback.onClientFinished(this, false);
            return;
        }
        try {
            android.hardware.biometrics.face.V1_0.IBiometricsFace daemon = getFreshDaemon();
            daemon.setActiveUser(getTargetUserId(), storePath.getAbsolutePath());
            this.mAuthenticatorIds.put(java.lang.Integer.valueOf(getTargetUserId()), java.lang.Long.valueOf(this.mHasEnrolledBiometrics ? daemon.getAuthenticatorId().value : 0L));
            this.mUserStartedCallback.onUserStarted(getTargetUserId(), null, 0);
            this.mCallback.onClientFinished(this, true);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to setActiveUser: " + e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.StartUserClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 1;
    }
}
