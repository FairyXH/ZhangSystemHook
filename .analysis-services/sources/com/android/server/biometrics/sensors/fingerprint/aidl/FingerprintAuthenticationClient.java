package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintAuthenticationClient extends com.android.server.biometrics.sensors.AuthenticationClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession, android.hardware.fingerprint.FingerprintAuthenticateOptions> implements com.android.server.biometrics.sensors.fingerprint.Udfps, com.android.server.biometrics.sensors.LockoutConsumer, com.android.server.biometrics.sensors.fingerprint.PowerPressHandler {
    private static final java.lang.String TAG = "FingerprintAuthenticationClient";
    private final com.android.server.biometrics.log.CallbackWithProbe<com.android.server.biometrics.log.Probe> mALSProbeCallback;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private android.hardware.biometrics.common.ICancellationSignal mCancellationSignal;
    public com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAuthenticationClientAidlExt mFingerprintAuthenticationClientAidlExt;
    private boolean mIsPointerDown;
    private final boolean mIsStrongBiometric;
    private final com.android.server.biometrics.sensors.SensorOverlays mSensorOverlays;
    private final android.hardware.fingerprint.FingerprintSensorPropertiesInternal mSensorProps;

    public FingerprintAuthenticationClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, long operationId, boolean restricted, android.hardware.fingerprint.FingerprintAuthenticateOptions options, int cookie, boolean requireConfirmation, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext, boolean isStrongBiometric, android.app.TaskStackListener taskStackListener, android.hardware.fingerprint.IUdfpsOverlayController udfpsOverlayController, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, boolean allowBackgroundAuthentication, android.hardware.fingerprint.FingerprintSensorPropertiesInternal sensorProps, int biometricStrength, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker) {
        super(context, lazyDaemon, token, listener, operationId, restricted, options, cookie, requireConfirmation, biometricLogger, biometricContext, isStrongBiometric, taskStackListener, lockoutTracker, allowBackgroundAuthentication, true, biometricStrength);
        this.mFingerprintAuthenticationClientAidlExt = (com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAuthenticationClientAidlExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintAuthenticationClientAidlExt.class).base(this).create();
        setRequestId(requestId);
        this.mSensorOverlays = new com.android.server.biometrics.sensors.SensorOverlays(udfpsOverlayController);
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mIsStrongBiometric = isStrongBiometric;
        this.mSensorOverlays.setContext(context);
        this.mSensorProps = sensorProps;
        this.mALSProbeCallback = getLogger().getAmbientLightProbe(false);
        this.mAuthSessionCoordinator = biometricContext.getAuthSessionCoordinator();
        this.mFingerprintAuthenticationClientAidlExt.init(context, lazyDaemon, options.getUserId(), operationId, options.getOpPackageName());
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        if (this.mSensorProps.isAnyUdfpsType()) {
            this.mState = 2;
        } else {
            this.mState = 1;
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected com.android.server.biometrics.sensors.ClientMonitorCallback wrapCallbackForStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        return new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(this.mALSProbeCallback, getBiometricContextUnsubscriber(), callback);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    protected void handleLifecycleAfterAuth(boolean authenticated) {
        if (authenticated) {
            this.mCallback.onClientFinished(this, true);
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public boolean wasUserDetected() {
        return false;
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, boolean authenticated, java.util.ArrayList<java.lang.Byte> token) {
        super.onAuthenticated(identifier, authenticated, token);
        handleLockout(authenticated);
        this.mFingerprintAuthenticationClientAidlExt.onAuthenticated(authenticated);
        if (authenticated) {
            this.mState = 4;
            this.mSensorOverlays.hide(getSensorId());
            if (android.adaptiveauth.Flags.reportBiometricAuthAttempts()) {
                this.mAuthenticationStateListeners.onAuthenticationSucceeded(new android.hardware.biometrics.events.AuthenticationSucceededInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), this.mIsStrongBiometric, getTargetUserId()).build());
            }
            this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason()).build());
            return;
        }
        this.mState = 3;
        if (android.adaptiveauth.Flags.reportBiometricAuthAttempts()) {
            this.mAuthenticationStateListeners.onAuthenticationFailed(new android.hardware.biometrics.events.AuthenticationFailedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), getTargetUserId()).build());
        }
    }

    private void handleLockout(boolean authenticated) {
        int errorCode;
        if (getLockoutTracker() == null) {
            android.util.Slog.d(TAG, "Lockout is implemented by the HAL");
            return;
        }
        if (authenticated && !isBiometricPrompt()) {
            getLockoutTracker().resetFailedAttemptsForUser(true, getTargetUserId());
            return;
        }
        int lockoutMode = getLockoutTracker().getLockoutModeForUser(getTargetUserId());
        if (lockoutMode != 0) {
            android.util.Slog.w(TAG, "Fingerprint locked out, lockoutMode(" + lockoutMode + ")");
            if (lockoutMode == 1) {
                errorCode = 7;
            } else {
                errorCode = 9;
            }
            this.mSensorOverlays.hide(getSensorId());
            this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), android.hardware.fingerprint.FingerprintManager.getErrorString(getContext(), errorCode, 0), errorCode).build());
            onErrorInternal(errorCode, 0, false);
            cancel();
        }
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(final int acquiredInfo, int vendorCode) {
        java.lang.String helpMsg;
        this.mAuthenticationStateListeners.onAuthenticationAcquired(new android.hardware.biometrics.events.AuthenticationAcquiredInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), acquiredInfo).build());
        if (acquiredInfo != 7 && (helpMsg = android.hardware.fingerprint.FingerprintManager.getAcquiredString(getContext(), acquiredInfo, vendorCode)) != null) {
            int helpCode = acquiredInfo == 6 ? vendorCode + 1000 : acquiredInfo;
            this.mAuthenticationStateListeners.onAuthenticationHelp(new android.hardware.biometrics.events.AuthenticationHelpInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), helpMsg, helpCode).build());
        }
        this.mSensorOverlays.ifUdfps(new com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer
            public final void accept(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$onAcquired$0(acquiredInfo, (android.hardware.fingerprint.IUdfpsOverlayController) obj);
            }
        });
        super.onAcquired(acquiredInfo, vendorCode);
        com.android.server.biometrics.sensors.PerformanceTracker pt = com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId());
        if (pt != null) {
            pt.incrementAcquireForUser(getTargetUserId(), isCryptoOperation());
        } else {
            android.util.Slog.e(TAG, "Unable to get performance tracker for sensor id " + getSensorId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAcquired$0(int acquiredInfo, android.hardware.fingerprint.IUdfpsOverlayController controller) throws android.os.RemoteException {
        controller.onAcquired(getSensorId(), acquiredInfo);
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), android.hardware.fingerprint.FingerprintManager.getErrorString(getContext(), errorCode, vendorCode), errorCode).build());
        super.onError(errorCode, vendorCode);
        if (errorCode == 18) {
            com.android.server.biometrics.sensors.BiometricNotificationUtils.showBadCalibrationNotification(getContext());
        }
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason()).build());
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        this.mSensorOverlays.show(getSensorId(), getRequestReason(), this);
        this.mAuthenticationStateListeners.onAuthenticationStarted(new android.hardware.biometrics.events.AuthenticationStartedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason()).build());
        try {
            if (this.mFingerprintAuthenticationClientAidlExt != null) {
                this.mCancellationSignal = this.mFingerprintAuthenticationClientAidlExt.startHalOperation();
                if (this.mCancellationSignal != null) {
                    return;
                }
            }
            doAuthenticate();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
            onError(1, 0);
            this.mSensorOverlays.hide(getSensorId());
            this.mCallback.onClientFinished(this, false);
        }
    }

    private void doAuthenticate() throws android.os.RemoteException {
        final com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
        com.android.server.biometrics.log.OperationContextExt opContext = getOperationContext();
        getBiometricContext().subscribe(opContext, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$doAuthenticate$1(session, (android.hardware.biometrics.common.OperationContext) obj);
            }
        }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$doAuthenticate$2(session, (android.hardware.biometrics.common.OperationContext) obj);
            }
        }, getOptions());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doAuthenticate$1(com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            if (session.hasContextMethods()) {
                this.mCancellationSignal = session.getSession().authenticateWithContext(this.mOperationId, ctx);
            } else {
                this.mCancellationSignal = session.getSession().authenticate(this.mOperationId);
            }
            if (getBiometricContext().isAwake()) {
                this.mALSProbeCallback.getProbe().enable();
            } else {
                this.mALSProbeCallback.getProbe().disable();
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
            onError(1, 0);
            this.mSensorOverlays.hide(getSensorId());
            this.mCallback.onClientFinished(this, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doAuthenticate$2(com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        if (session.hasContextMethods()) {
            try {
                session.getSession().onContextChanged(ctx);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to notify context changed", e);
            }
        }
        boolean isAwake = getBiometricContext().isAwake();
        if (isAwake) {
            this.mALSProbeCallback.getProbe().enable();
        } else {
            this.mALSProbeCallback.getProbe().disable();
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason()).build());
        unsubscribeBiometricContext();
        if (this.mCancellationSignal != null) {
            try {
                this.mCancellationSignal.cancel();
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception", e);
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
                return;
            }
        }
        android.util.Slog.e(TAG, "Cancellation signal was null");
        this.mCallback.onClientFinished(this, false);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerDown(android.hardware.biometrics.fingerprint.PointerContext pc) {
        try {
            this.mIsPointerDown = true;
            this.mState = 1;
            com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
            if (session.hasContextMethods()) {
                session.getSession().onPointerDownWithContext(pc);
            } else {
                session.getSession().onPointerDown(pc.pointerId, (int) pc.x, (int) pc.y, pc.minor, pc.major);
            }
            getListener().onUdfpsPointerDown(getSensorId());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerUp(android.hardware.biometrics.fingerprint.PointerContext pc) {
        try {
            this.mIsPointerDown = false;
            this.mState = 3;
            com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
            if (session.hasContextMethods()) {
                session.getSession().onPointerUpWithContext(pc);
            } else {
                session.getSession().onPointerUp(pc.pointerId);
            }
            getListener().onUdfpsPointerUp(getSensorId());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void setIgnoreDisplayTouches(boolean ignoreTouches) {
        try {
            getFreshDaemon().getSession().setIgnoreDisplayTouches(ignoreTouches);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public boolean isPointerDown() {
        return this.mIsPointerDown;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onUdfpsUiEvent(int event) {
        if (event == 2) {
            try {
                getFreshDaemon().getSession().onUiReady();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception", e);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutTimed(long durationMillis) {
        this.mAuthSessionCoordinator.lockOutTimed(getTargetUserId(), getSensorStrength(), getSensorId(), durationMillis, getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 7, 0, getTargetUserId());
        com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementTimedLockoutForUser(getTargetUserId());
        try {
            this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), android.hardware.fingerprint.FingerprintManager.getErrorString(getContext(), 7, 0), 7).build());
            if (getListener() != null) {
                getListener().onError(getSensorId(), getCookie(), 7, 0);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason()).build());
        this.mCallback.onClientFinished(this, false);
    }

    @Override // com.android.server.biometrics.sensors.LockoutConsumer
    public void onLockoutPermanent() {
        this.mAuthSessionCoordinator.lockedOutFor(getTargetUserId(), getSensorStrength(), getSensorId(), getRequestId());
        getLogger().logOnError(getContext(), getOperationContext(), 9, 0, getTargetUserId());
        com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId()).incrementPermanentLockoutForUser(getTargetUserId());
        try {
            this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason(), android.hardware.fingerprint.FingerprintManager.getErrorString(getContext(), 9, 0), 9).build());
            if (getListener() != null) {
                getListener().onError(getSensorId(), getCookie(), 9, 0);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReason()).build());
        this.mCallback.onClientFinished(this, false);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.PowerPressHandler
    public void onPowerPressed() {
    }

    @Override // com.android.server.biometrics.sensors.AuthenticationClient
    public int handleFailedAttempt(int userId) {
        if (this.mFingerprintAuthenticationClientAidlExt.isMistakeTouchMode()) {
            return 0;
        }
        getLockoutTracker().getLockoutModeForUser(userId);
        int lockoutMode = super.handleFailedAttempt(userId);
        this.mFingerprintAuthenticationClientAidlExt.onHandleFailedAttempt(getLockoutTracker(), userId);
        return lockoutMode;
    }
}
