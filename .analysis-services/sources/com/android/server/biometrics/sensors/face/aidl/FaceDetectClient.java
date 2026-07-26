package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceDetectClient extends com.android.server.biometrics.sensors.AcquisitionClient<com.android.server.biometrics.sensors.face.aidl.AidlSession> implements com.android.server.biometrics.sensors.DetectionConsumer {
    private static final java.lang.String TAG = "FaceDetectClient";
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private android.hardware.biometrics.common.ICancellationSignal mCancellationSignal;
    private final boolean mIsStrongBiometric;
    private final android.hardware.face.FaceAuthenticateOptions mOptions;
    private android.hardware.SensorPrivacyManager mSensorPrivacyManager;

    FaceDetectClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, android.hardware.face.FaceAuthenticateOptions options, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, boolean isStrongBiometric) {
        this(context, lazyDaemon, token, requestId, listener, options, logger, biometricContext, authenticationStateListeners, isStrongBiometric, (android.hardware.SensorPrivacyManager) context.getSystemService(android.hardware.SensorPrivacyManager.class));
    }

    FaceDetectClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, android.hardware.face.FaceAuthenticateOptions options, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, boolean isStrongBiometric, android.hardware.SensorPrivacyManager sensorPrivacyManager) {
        super(context, lazyDaemon, token, listener, options.getUserId(), options.getOpPackageName(), 0, options.getSensorId(), false, logger, biometricContext);
        setRequestId(requestId);
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mIsStrongBiometric = isStrongBiometric;
        this.mSensorPrivacyManager = sensorPrivacyManager;
        this.mOptions = options;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, 4).build());
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
        this.mAuthenticationStateListeners.onAuthenticationStarted(new android.hardware.biometrics.events.AuthenticationStartedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, 4).build());
        if ((this.mSensorPrivacyManager != null && this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 2)) || com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().isSatelliteMode()) {
            onError(1, 0);
            this.mCallback.onClientFinished(this, false);
            return;
        }
        try {
            doDetectInteraction();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Remote exception when requesting face detect", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int error, int vendorCode) {
        this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, 4, android.hardware.face.FaceManager.getErrorString(getContext(), error, vendorCode), error).build());
        super.onError(error, vendorCode);
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, 4).build());
    }

    private void doDetectInteraction() throws android.os.RemoteException {
        final com.android.server.biometrics.sensors.face.aidl.AidlSession session = getFreshDaemon();
        if (session.hasContextMethods()) {
            com.android.server.biometrics.log.OperationContextExt opContext = getOperationContext();
            getBiometricContext().subscribe(opContext, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceDetectClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$doDetectInteraction$0(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceDetectClient$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.biometrics.sensors.face.aidl.FaceDetectClient.lambda$doDetectInteraction$1(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, this.mOptions);
        } else {
            this.mCancellationSignal = session.getSession().detectInteraction();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doDetectInteraction$0(com.android.server.biometrics.sensors.face.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            this.mCancellationSignal = session.getSession().detectInteractionWithContext(ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when requesting face detect", e);
            this.mCallback.onClientFinished(this, false);
        }
    }

    static /* synthetic */ void lambda$doDetectInteraction$1(com.android.server.biometrics.sensors.face.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
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
            getListener().onDetected(getSensorId(), getTargetUserId(), this.mIsStrongBiometric);
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
