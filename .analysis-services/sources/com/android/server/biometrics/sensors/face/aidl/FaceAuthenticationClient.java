package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceAuthenticationClient extends com.android.server.biometrics.sensors.AuthenticationClient<com.android.server.biometrics.sensors.face.aidl.AidlSession, android.hardware.face.FaceAuthenticateOptions> implements com.android.server.biometrics.sensors.LockoutConsumer {
    private static final java.lang.String TAG = "FaceAuthenticationClient";
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private final int[] mBiometricPromptIgnoreList;
    private final int[] mBiometricPromptIgnoreListVendor;
    private android.hardware.biometrics.common.ICancellationSignal mCancellationSignal;
    private com.android.server.biometrics.sensors.face.aidl.IFaceAuthenticationClientExt mFaceAuthenticationClientExt;
    private final boolean mIsStrongBiometric;
    private final int[] mKeyguardIgnoreList;
    private final int[] mKeyguardIgnoreListVendor;
    private int mLastAcquire;
    private final android.hardware.SensorPrivacyManager mSensorPrivacyManager;
    private final com.android.server.biometrics.sensors.face.UsageStats mUsageStats;

    public FaceAuthenticationClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, long operationId, boolean restricted, android.hardware.face.FaceAuthenticateOptions options, int cookie, boolean requireConfirmation, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, boolean isStrongBiometric, com.android.server.biometrics.sensors.face.UsageStats usageStats, com.android.server.biometrics.sensors.LockoutTracker lockoutCache, boolean allowBackgroundAuthentication, int sensorStrength, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners) {
        this(context, lazyDaemon, token, requestId, listener, operationId, restricted, options, cookie, requireConfirmation, logger, biometricContext, isStrongBiometric, usageStats, lockoutCache, allowBackgroundAuthentication, (android.hardware.SensorPrivacyManager) context.getSystemService(android.hardware.SensorPrivacyManager.class), sensorStrength, authenticationStateListeners);
    }

    FaceAuthenticationClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, long operationId, boolean restricted, android.hardware.face.FaceAuthenticateOptions options, int cookie, boolean requireConfirmation, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, boolean isStrongBiometric, com.android.server.biometrics.sensors.face.UsageStats usageStats, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, boolean allowBackgroundAuthentication, android.hardware.SensorPrivacyManager sensorPrivacyManager, int biometricStrength, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners) {
        super(context, lazyDaemon, token, listener, operationId, restricted, options, cookie, requireConfirmation, logger, biometricContext, isStrongBiometric, null, lockoutTracker, allowBackgroundAuthentication, true, biometricStrength);
        this.mLastAcquire = 23;
        setRequestId(requestId);
        this.mIsStrongBiometric = isStrongBiometric;
        this.mUsageStats = usageStats;
        this.mSensorPrivacyManager = sensorPrivacyManager;
        this.mAuthSessionCoordinator = biometricContext.getAuthSessionCoordinator();
        this.mAuthenticationStateListeners = authenticationStateListeners;
        android.content.res.Resources resources = getContext().getResources();
        this.mBiometricPromptIgnoreList = resources.getIntArray(android.R.array.config_ephemeralResolverPackage);
        this.mBiometricPromptIgnoreListVendor = resources.getIntArray(android.R.array.config_face_acquire_enroll_ignorelist);
        this.mKeyguardIgnoreList = resources.getIntArray(android.R.array.config_face_acquire_biometricprompt_ignorelist);
        this.mKeyguardIgnoreListVendor = resources.getIntArray(android.R.array.config_face_acquire_vendor_biometricprompt_ignorelist);
        this.mFaceAuthenticationClientExt = (com.android.server.biometrics.sensors.face.aidl.IFaceAuthenticationClientExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.aidl.IFaceAuthenticationClientExt.class).base(this).create();
        this.mFaceAuthenticationClientExt.init(context, lazyDaemon, options.getOpPackageName(), options.getUserId(), operationId);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        this.mState = 1;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected com.android.server.biometrics.sensors.ClientMonitorCallback wrapCallbackForStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        return new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(getLogger().getAmbientLightProbe(true), callback);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        this.mAuthenticationStateListeners.onAuthenticationStarted(new android.hardware.biometrics.events.AuthenticationStartedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason()).build());
        try {
            if ((this.mSensorPrivacyManager != null && this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 2)) || com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().isSatelliteMode()) {
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
                return;
            }
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().scheduleAuthenticate();
            this.mCancellationSignal = this.mFaceAuthenticationClientExt.startHalOperation();
            if (this.mCancellationSignal != null) {
                return;
            }
            doAuthenticate();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception when requesting auth", e);
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    private void doAuthenticate() throws android.os.RemoteException {
        final com.android.server.biometrics.sensors.face.aidl.AidlSession session = getFreshDaemon();
        if (session.hasContextMethods()) {
            com.android.server.biometrics.log.OperationContextExt opContext = getOperationContext();
            getBiometricContext().subscribe(opContext, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$doAuthenticate$0(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient.lambda$doAuthenticate$1(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, getOptions());
        } else {
            this.mCancellationSignal = session.getSession().authenticate(this.mOperationId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doAuthenticate$0(com.android.server.biometrics.sensors.face.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            this.mCancellationSignal = session.getSession().authenticateWithContext(this.mOperationId, ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting auth", e);
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    static /* synthetic */ void lambda$doAuthenticate$1(com.android.server.biometrics.sensors.face.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            session.getSession().onContextChanged(ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to notify context changed", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason()).build());
        unsubscribeBiometricContext();
        if (this.mCancellationSignal != null) {
            try {
                if (this.mFaceAuthenticationClientExt.stopHalOperation()) {
                    return;
                }
                this.mCancellationSignal.cancel();
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception when requesting cancel", e);
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
                return;
            }
        }
        android.util.Slog.e(TAG, "Cancellation signal is null");
        this.mCallback.onClientFinished(this, false);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public boolean wasUserDetected() {
        return (this.mLastAcquire == 11 || this.mLastAcquire == 21 || this.mLastAcquire == 23) ? false : true;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    protected void handleLifecycleAfterAuth(boolean authenticated) {
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason()).build());
        this.mCallback.onClientFinished(this, true);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, boolean authenticated, java.util.ArrayList<java.lang.Byte> token) {
        super.onAuthenticated(identifier, authenticated, token);
        this.mState = 4;
        this.mUsageStats.addEvent(new com.android.server.biometrics.sensors.face.UsageStats.AuthenticationEvent(getStartTimeMs(), java.lang.System.currentTimeMillis() - getStartTimeMs(), authenticated, 0, 0, getTargetUserId()));
        if (android.adaptiveauth.Flags.reportBiometricAuthAttempts()) {
            if (authenticated) {
                this.mAuthenticationStateListeners.onAuthenticationSucceeded(new android.hardware.biometrics.events.AuthenticationSucceededInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), this.mIsStrongBiometric, getTargetUserId()).build());
            } else {
                this.mAuthenticationStateListeners.onAuthenticationFailed(new android.hardware.biometrics.events.AuthenticationFailedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), getTargetUserId()).build());
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int error, int vendorCode) {
        this.mUsageStats.addEvent(new com.android.server.biometrics.sensors.face.UsageStats.AuthenticationEvent(getStartTimeMs(), java.lang.System.currentTimeMillis() - getStartTimeMs(), false, error, vendorCode, getTargetUserId()));
        this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), android.hardware.face.FaceManager.getErrorString(getContext(), error, vendorCode), error).build());
        super.onError(error, vendorCode);
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason()).build());
    }

    private int[] getAcquireIgnorelist() {
        return isBiometricPrompt() ? this.mBiometricPromptIgnoreList : this.mKeyguardIgnoreList;
    }

    private int[] getAcquireVendorIgnorelist() {
        return isBiometricPrompt() ? this.mBiometricPromptIgnoreListVendor : this.mKeyguardIgnoreListVendor;
    }

    private boolean shouldSendAcquiredMessage(int acquireInfo, int vendorCode) {
        return acquireInfo == 22 ? !com.android.server.biometrics.Utils.listContains(getAcquireVendorIgnorelist(), vendorCode) : !com.android.server.biometrics.Utils.listContains(getAcquireIgnorelist(), acquireInfo);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int acquireInfo, int vendorCode) {
        this.mLastAcquire = acquireInfo;
        boolean shouldSend = shouldSendAcquiredMessage(acquireInfo, vendorCode);
        if (shouldSend) {
            this.mAuthenticationStateListeners.onAuthenticationAcquired(new android.hardware.biometrics.events.AuthenticationAcquiredInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), acquireInfo).build());
            java.lang.String helpMessage = android.hardware.face.FaceManager.getAuthHelpMessage(getContext(), acquireInfo, vendorCode);
            if (helpMessage != null) {
                int helpCode = getHelpCode(acquireInfo, vendorCode);
                this.mAuthenticationStateListeners.onAuthenticationHelp(new android.hardware.biometrics.events.AuthenticationHelpInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), helpMessage, helpCode).build());
            }
        }
        onAcquiredInternal(acquireInfo, vendorCode, shouldSend);
        if (getLockoutTracker() == null || getLockoutTracker().getLockoutModeForUser(getTargetUserId()) == 0) {
            com.android.server.biometrics.sensors.PerformanceTracker pt = com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId());
            pt.incrementAcquireForUser(getTargetUserId(), isCryptoOperation());
        }
    }

    public void onAuthenticationFrame(android.hardware.face.FaceAuthenticationFrame frame) {
        int acquireInfo = frame.getData().getAcquiredInfo();
        int vendorCode = frame.getData().getVendorCode();
        this.mLastAcquire = acquireInfo;
        onAcquiredInternal(acquireInfo, vendorCode, false);
        boolean shouldSend = shouldSendAcquiredMessage(acquireInfo, vendorCode);
        if (shouldSend) {
            if (shouldSend) {
                try {
                    this.mAuthenticationStateListeners.onAuthenticationAcquired(new android.hardware.biometrics.events.AuthenticationAcquiredInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), acquireInfo).build());
                    java.lang.String helpMessage = android.hardware.face.FaceManager.getAuthHelpMessage(getContext(), acquireInfo, vendorCode);
                    if (helpMessage != null) {
                        int helpCode = getHelpCode(acquireInfo, vendorCode);
                        this.mAuthenticationStateListeners.onAuthenticationHelp(new android.hardware.biometrics.events.AuthenticationHelpInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason(), helpMessage, helpCode).build());
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to send authentication frame", e);
                    this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReason()).build());
                    this.mCallback.onClientFinished(this, false);
                    return;
                }
            }
            getListener().onAuthenticationFrame(frame);
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutTimed(long durationMillis) {
        this.mAuthSessionCoordinator.lockOutTimed(getTargetUserId(), getSensorStrength(), getSensorId(), durationMillis, getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 7, 0, getTargetUserId());
        com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementTimedLockoutForUser(getTargetUserId());
        onError(7, 0);
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutPermanent() {
        this.mAuthSessionCoordinator.lockedOutFor(getTargetUserId(), getSensorStrength(), getSensorId(), getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 9, 0, getTargetUserId());
        com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementPermanentLockoutForUser(getTargetUserId());
        onError(9, 0);
    }

    private static int getHelpCode(int acquireInfo, int vendorCode) {
        if (acquireInfo == 22) {
            return vendorCode + 1000;
        }
        return acquireInfo;
    }
}
