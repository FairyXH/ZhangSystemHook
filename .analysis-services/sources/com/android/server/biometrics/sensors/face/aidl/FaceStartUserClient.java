package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceStartUserClient extends com.android.server.biometrics.sensors.StartUserClient<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> {
    private static final java.lang.String TAG = "FaceStartUserClient";
    private final android.hardware.biometrics.face.ISessionCallback mSessionCallback;

    public FaceStartUserClient(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.face.IFace> lazyDaemon, android.os.IBinder token, int userId, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, android.hardware.biometrics.face.ISessionCallback sessionCallback, com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<android.hardware.biometrics.face.ISession> callback) {
        super(context, lazyDaemon, token, userId, sensorId, logger, biometricContext, callback);
        this.mSessionCallback = sessionCallback;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
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
        try {
            android.hardware.biometrics.face.IFace hal = getFreshDaemon();
            int version = hal.getInterfaceVersion();
            android.hardware.biometrics.face.ISession newSession = hal.createSession(getSensorId(), getTargetUserId(), this.mSessionCallback);
            if (newSession == null) {
                android.util.Slog.e(TAG, "newSession is null");
            } else {
                android.os.Binder.allowBlocking(newSession.asBinder());
            }
            this.mUserStartedCallback.onUserStarted(getTargetUserId(), newSession, version);
            getCallback().onClientFinished(this, true);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception", e);
            getCallback().onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }
}
