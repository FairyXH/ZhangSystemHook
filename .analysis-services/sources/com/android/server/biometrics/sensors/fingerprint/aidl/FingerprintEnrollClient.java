package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintEnrollClient extends com.android.server.biometrics.sensors.EnrollClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> implements com.android.server.biometrics.sensors.fingerprint.Udfps, com.android.server.biometrics.sensors.fingerprint.PowerPressHandler {
    private static final java.lang.String TAG = "FingerprintEnrollClient";
    private final com.android.server.biometrics.log.CallbackWithProbe<com.android.server.biometrics.log.Probe> mALSProbeCallback;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private android.hardware.biometrics.common.ICancellationSignal mCancellationSignal;
    private final int mEnrollReason;
    private boolean mIsPointerDown;
    private final int mMaxTemplatesPerUser;
    private final com.android.server.biometrics.sensors.SensorOverlays mSensorOverlays;
    private final android.hardware.fingerprint.FingerprintSensorPropertiesInternal mSensorProps;

    private static boolean shouldVibrateFor(android.content.Context context, android.hardware.fingerprint.FingerprintSensorPropertiesInternal sensorProps) {
        if (sensorProps == null) {
            return true;
        }
        android.view.accessibility.AccessibilityManager am = (android.view.accessibility.AccessibilityManager) context.getSystemService(android.view.accessibility.AccessibilityManager.class);
        boolean isAccessbilityEnabled = am.isTouchExplorationEnabled();
        return !sensorProps.isAnyUdfpsType() || isAccessbilityEnabled;
    }

    public FingerprintEnrollClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, byte[] hardwareAuthToken, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, android.hardware.fingerprint.FingerprintSensorPropertiesInternal sensorProps, android.hardware.fingerprint.IUdfpsOverlayController udfpsOverlayController, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, int maxTemplatesPerUser, int enrollReason, android.hardware.fingerprint.FingerprintEnrollOptions options) {
        super(context, lazyDaemon, token, listener, userId, hardwareAuthToken, owner, utils, 0, sensorId, shouldVibrateFor(context, sensorProps), logger, biometricContext, android.hardware.biometrics.BiometricFingerprintConstants.reasonToMetric(options.getEnrollReason()));
        setRequestId(requestId);
        this.mSensorProps = sensorProps;
        this.mSensorOverlays = new com.android.server.biometrics.sensors.SensorOverlays(udfpsOverlayController);
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mSensorOverlays.setContext(context);
        this.mMaxTemplatesPerUser = maxTemplatesPerUser;
        this.mALSProbeCallback = getLogger().getAmbientLightProbe(true);
        this.mEnrollReason = enrollReason;
        if (enrollReason == 1) {
            getLogger().disableMetrics();
        }
        android.util.Slog.w(TAG, "EnrollOptions " + android.hardware.fingerprint.FingerprintEnrollOptions.enrollReasonToString(options.getEnrollReason()));
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFingerprintEnrollNotification(getContext());
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected com.android.server.biometrics.sensors.ClientMonitorCallback wrapCallbackForStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        return new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(this.mALSProbeCallback, getBiometricContextUnsubscriber(), callback);
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient
    public void onEnrollResult(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, final int remaining) {
        super.onEnrollResult(identifier, remaining);
        this.mSensorOverlays.ifUdfps(new com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient$$ExternalSyntheticLambda2
            @Override // com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer
            public final void accept(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$onEnrollResult$0(remaining, (android.hardware.fingerprint.IUdfpsOverlayController) obj);
            }
        });
        if (remaining == 0) {
            this.mSensorOverlays.hide(getSensorId());
            this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason)).build());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollResult$0(int remaining, android.hardware.fingerprint.IUdfpsOverlayController controller) throws android.os.RemoteException {
        controller.onEnrollmentProgress(getSensorId(), remaining);
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(final int acquiredInfo, final int vendorCode) {
        if (acquiredInfo != 7) {
            this.mAuthenticationStateListeners.onAuthenticationAcquired(new android.hardware.biometrics.events.AuthenticationAcquiredInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason), acquiredInfo).build());
        }
        java.lang.String helpMsg = android.hardware.fingerprint.FingerprintManager.getAcquiredString(getContext(), acquiredInfo, vendorCode);
        if (helpMsg != null) {
            int helpCode = acquiredInfo == 6 ? vendorCode + 1000 : acquiredInfo;
            this.mAuthenticationStateListeners.onAuthenticationHelp(new android.hardware.biometrics.events.AuthenticationHelpInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason), helpMsg, helpCode).build());
        }
        boolean acquiredGood = acquiredInfo == 0;
        if (this.mSensorProps != null && this.mSensorProps.isAnyUdfpsType()) {
            if (acquiredGood && this.mShouldVibrate) {
                vibrateSuccess();
            }
            this.mSensorOverlays.ifUdfps(new com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient$$ExternalSyntheticLambda3
                @Override // com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer
                public final void accept(java.lang.Object obj) throws android.os.RemoteException {
                    this.f$0.lambda$onAcquired$1(acquiredInfo, (android.hardware.fingerprint.IUdfpsOverlayController) obj);
                }
            });
        }
        this.mSensorOverlays.ifUdfps(new com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient$$ExternalSyntheticLambda4
            @Override // com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer
            public final void accept(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$onAcquired$2(acquiredInfo, vendorCode, (android.hardware.fingerprint.IUdfpsOverlayController) obj);
            }
        });
        this.mCallback.onBiometricAction(0);
        super.onAcquired(acquiredInfo, vendorCode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAcquired$1(int acquiredInfo, android.hardware.fingerprint.IUdfpsOverlayController controller) throws android.os.RemoteException {
        controller.onAcquired(getSensorId(), acquiredInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAcquired$2(int acquiredInfo, int vendorCode, android.hardware.fingerprint.IUdfpsOverlayController controller) throws android.os.RemoteException {
        if (com.android.server.biometrics.sensors.fingerprint.UdfpsHelper.isValidAcquisitionMessage(getContext(), acquiredInfo, vendorCode)) {
            controller.onEnrollmentHelp(getSensorId());
        }
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason), android.hardware.fingerprint.FingerprintManager.getErrorString(getContext(), errorCode, vendorCode), errorCode).build());
        super.onError(errorCode, vendorCode);
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason)).build());
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient
    protected boolean hasReachedEnrollmentLimit() {
        return this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).size() >= this.mMaxTemplatesPerUser;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        this.mSensorOverlays.show(getSensorId(), getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason), this);
        this.mAuthenticationStateListeners.onAuthenticationStarted(new android.hardware.biometrics.events.AuthenticationStartedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason)).build());
        com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelBadCalibrationNotification(getContext());
        try {
            doEnroll();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting enroll", e);
            onError(2, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    private void doEnroll() throws android.os.RemoteException {
        final com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
        final android.hardware.keymaster.HardwareAuthToken hat = com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(this.mHardwareAuthToken);
        if (session.hasContextMethods()) {
            com.android.server.biometrics.log.OperationContextExt opContext = getOperationContext();
            getBiometricContext().subscribe(opContext, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$doEnroll$3(session, hat, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient.lambda$doEnroll$4(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, null);
        } else {
            this.mCancellationSignal = session.getSession().enroll(hat);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doEnroll$3(com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session, android.hardware.keymaster.HardwareAuthToken hat, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            this.mCancellationSignal = session.getSession().enrollWithContext(hat, ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting enroll", e);
            onError(2, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    static /* synthetic */ void lambda$doEnroll$4(com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            session.getSession().onContextChanged(ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to notify context changed", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, getRequestReasonFromFingerprintEnrollReason(this.mEnrollReason)).build());
        unsubscribeBiometricContext();
        if (this.mCancellationSignal != null) {
            try {
                this.mCancellationSignal.cancel();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception when requesting cancel", e);
                onError(1, 0);
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerDown(android.hardware.biometrics.fingerprint.PointerContext pc) {
        try {
            this.mIsPointerDown = true;
            com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
            if (session.hasContextMethods()) {
                session.getSession().onPointerDownWithContext(pc);
            } else {
                session.getSession().onPointerDown(pc.pointerId, (int) pc.x, (int) pc.y, pc.minor, pc.major);
            }
            getListener().onUdfpsPointerDown(getSensorId());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send pointer down", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onPointerUp(android.hardware.biometrics.fingerprint.PointerContext pc) {
        try {
            this.mIsPointerDown = false;
            com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
            if (session.hasContextMethods()) {
                session.getSession().onPointerUpWithContext(pc);
            } else {
                session.getSession().onPointerUp(pc.pointerId);
            }
            getListener().onUdfpsPointerUp(getSensorId());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send pointer up", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public boolean isPointerDown() {
        return this.mIsPointerDown;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void onUdfpsUiEvent(int event) {
        try {
            switch (event) {
                case 1:
                    getListener().onUdfpsOverlayShown();
                    break;
                case 2:
                    getFreshDaemon().getSession().onUiReady();
                    break;
                default:
                    android.util.Slog.w(TAG, "No matching event for onUdfpsUiEvent");
                    break;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send onUdfpsUiEvent", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.Udfps
    public void setIgnoreDisplayTouches(boolean ignoreTouches) {
        try {
            getFreshDaemon().getSession().setIgnoreDisplayTouches(ignoreTouches);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to send setIgnoreDisplayTouches", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.PowerPressHandler
    public void onPowerPressed() {
    }
}
