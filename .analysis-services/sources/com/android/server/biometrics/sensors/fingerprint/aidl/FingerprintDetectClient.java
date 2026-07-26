package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintDetectClient extends com.android.server.biometrics.sensors.AcquisitionClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> implements com.android.server.biometrics.sensors.DetectionConsumer {
    private static final java.lang.String TAG = "FingerprintDetectClient";
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private android.hardware.biometrics.common.ICancellationSignal mCancellationSignal;
    private final boolean mIsStrongBiometric;
    private final android.hardware.fingerprint.FingerprintAuthenticateOptions mOptions;
    private final com.android.server.biometrics.sensors.SensorOverlays mSensorOverlays;

    public FingerprintDetectClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, android.hardware.fingerprint.FingerprintAuthenticateOptions options, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, android.hardware.fingerprint.IUdfpsOverlayController udfpsOverlayController, boolean isStrongBiometric) {
        super(context, lazyDaemon, token, listener, options.getUserId(), options.getOpPackageName(), 0, options.getSensorId(), true, biometricLogger, biometricContext);
        setRequestId(requestId);
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mIsStrongBiometric = isStrongBiometric;
        this.mSensorOverlays = new com.android.server.biometrics.sensors.SensorOverlays(udfpsOverlayController);
        this.mSensorOverlays.setContext(context);
        this.mOptions = options;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mSensorOverlays.hide(getSensorId());
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, 4).build());
        unsubscribeBiometricContext();
        if (this.mCancellationSignal != null) {
            try {
                this.mCancellationSignal.cancel();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception", e);
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        this.mSensorOverlays.show(getSensorId(), 4, this);
        this.mAuthenticationStateListeners.onAuthenticationStarted(new android.hardware.biometrics.events.AuthenticationStartedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, 4).build());
        try {
            doDetectInteraction();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting finger detect", e);
            this.mSensorOverlays.hide(getSensorId());
            this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, 4).build());
            this.mCallback.onClientFinished(this, false);
        }
    }

    private void doDetectInteraction() throws android.os.RemoteException {
        final com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = getFreshDaemon();
        if (session.hasContextMethods()) {
            com.android.server.biometrics.log.OperationContextExt opContext = getOperationContext();
            getBiometricContext().subscribe(opContext, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$doDetectInteraction$0(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient.lambda$doDetectInteraction$1(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, this.mOptions);
        } else {
            this.mCancellationSignal = session.getSession().detectInteraction();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doDetectInteraction$0(com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            this.mCancellationSignal = session.getSession().detectInteractionWithContext(ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to start detect interaction", e);
            this.mSensorOverlays.hide(getSensorId());
            this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FINGERPRINT, 4).build());
            this.mCallback.onClientFinished(this, false);
        }
    }

    static /* synthetic */ void lambda$doDetectInteraction$1(com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            session.getSession().onContextChanged(ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to notify context changed", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.DetectionConsumer
    public void onInteractionDetected() {
        vibrateSuccess();
        try {
            if (getListener() == null) {
                android.util.Slog.e(TAG, "Listener is null!");
            } else {
                getListener().onDetected(getSensorId(), getTargetUserId(), this.mIsStrongBiometric);
                this.mSensorOverlays.hide(getSensorId());
            }
            this.mCallback.onClientFinished(this, true);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when sending onDetected", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 13;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }
}
