package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintUpdateActiveUserClient extends com.android.server.biometrics.sensors.StartUserClient<android.hardware.biometrics.fingerprint.ISession, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
    private static final java.lang.String FP_DATA_DIR = "fpdata";
    private static final java.lang.String TAG = "FingerprintUpdateActiveUserClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    private final java.util.function.Supplier<java.lang.Integer> mCurrentUserId;
    private java.io.File mDirectory;
    private com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprintUpdateActiveUserClientExt mFingerprintUpdateActiveUserClientExt;
    private final boolean mForceUpdateAuthenticatorId;
    private final boolean mHasEnrolledBiometrics;

    FingerprintUpdateActiveUserClient(android.content.Context context, java.util.function.Supplier<android.hardware.biometrics.fingerprint.ISession> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.function.Supplier<java.lang.Integer> currentUserId, boolean hasEnrolledBiometrics, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds, boolean forceUpdateAuthenticatorId, com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> userStartedCallback) {
        super(context, lazyDaemon, null, userId, sensorId, logger, biometricContext, userStartedCallback);
        this.mFingerprintUpdateActiveUserClientExt = (com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprintUpdateActiveUserClientExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprintUpdateActiveUserClientExt.class).base(this).create();
        this.mCurrentUserId = currentUserId;
        this.mForceUpdateAuthenticatorId = forceUpdateAuthenticatorId;
        this.mHasEnrolledBiometrics = hasEnrolledBiometrics;
        this.mAuthenticatorIds = authenticatorIds;
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
    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        java.io.File baseDir;
        super.start(callback);
        if (this.mCurrentUserId.get().intValue() == getTargetUserId() && !this.mForceUpdateAuthenticatorId) {
            android.util.Slog.d(TAG, "Already user: " + this.mCurrentUserId + ", returning");
            this.mUserStartedCallback.onUserStarted(getTargetUserId(), null, 0);
            callback.onClientFinished(this, true);
            return;
        }
        int firstSdkInt = android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT;
        if (firstSdkInt < 1) {
            android.util.Slog.e(TAG, "First SDK version " + firstSdkInt + " is invalid; must be at least VERSION_CODES.BASE");
        }
        if (firstSdkInt <= 27) {
            baseDir = android.os.Environment.getUserSystemDirectory(this.mBaseClientMonitorExt.hookTargetUserId(getTargetUserId()));
        } else {
            baseDir = android.os.Environment.getDataVendorDeDirectory(this.mBaseClientMonitorExt.hookTargetUserId(getTargetUserId()));
        }
        this.mDirectory = new java.io.File(baseDir, FP_DATA_DIR);
        if (!this.mDirectory.exists()) {
            if (!this.mDirectory.mkdir()) {
                android.util.Slog.e(TAG, "Cannot make directory: " + this.mDirectory.getAbsolutePath());
                callback.onClientFinished(this, false);
                return;
            } else if (!android.os.SELinux.restorecon(this.mDirectory)) {
                android.util.Slog.e(TAG, "Restorecons failed. Directory will have wrong label.");
                callback.onClientFinished(this, false);
                return;
            }
        }
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
        try {
            int targetId = getTargetUserId();
            android.util.Slog.d(TAG, "Setting active user: " + targetId);
            com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter sessionAdapter = (com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSessionAdapter) getFreshDaemon();
            sessionAdapter.setActiveGroup(this.mBaseClientMonitorExt.hookTargetUserId(targetId), this.mDirectory.getAbsolutePath());
            this.mAuthenticatorIds.put(java.lang.Integer.valueOf(targetId), java.lang.Long.valueOf(this.mHasEnrolledBiometrics ? sessionAdapter.getAuthenticatorIdForUpdateClient() : 0L));
            this.mUserStartedCallback.onUserStarted(targetId, null, 0);
            this.mCallback.onClientFinished(this, true);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to setActiveGroup: " + e);
            this.mCallback.onClientFinished(this, false);
        } catch (java.lang.NullPointerException e2) {
            android.util.Slog.e(TAG, "Failed to setActiveGroup: " + e2);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.StartUserClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 1;
    }
}
