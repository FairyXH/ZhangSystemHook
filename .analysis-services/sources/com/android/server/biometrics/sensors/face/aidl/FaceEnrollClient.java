package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceEnrollClient extends com.android.server.biometrics.sensors.EnrollClient<com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    private static final java.lang.String TAG = "FaceEnrollClient";
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private android.hardware.biometrics.common.ICancellationSignal mCancellationSignal;
    private final boolean mDebugConsent;
    private final int[] mDisabledFeatures;
    private final int[] mEnrollIgnoreList;
    private final int[] mEnrollIgnoreListVendor;
    private final int mEnrollReason;
    private android.hardware.common.NativeHandle mHwPreviewHandle;
    private final int mMaxTemplatesPerUser;
    private android.os.NativeHandle mOsPreviewHandle;
    private final com.android.server.biometrics.sensors.ClientMonitorCallback mPreviewHandleDeleterCallback;
    private final android.view.Surface mPreviewSurface;

    public FaceEnrollClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, byte[] hardwareAuthToken, java.lang.String opPackageName, long requestId, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> utils, int[] disabledFeatures, int timeoutSec, android.view.Surface previewSurface, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, int maxTemplatesPerUser, boolean debugConsent, android.hardware.face.FaceEnrollOptions options, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners) {
        super(context, lazyDaemon, token, listener, userId, hardwareAuthToken, opPackageName, utils, timeoutSec, sensorId, false, logger, biometricContext, android.hardware.biometrics.BiometricFaceConstants.reasonToMetric(options.getEnrollReason()));
        this.mPreviewHandleDeleterCallback = new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient.1
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
            }

            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient.this.releaseSurfaceHandlesIfNeeded();
            }
        };
        setRequestId(requestId);
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mEnrollReason = options.getEnrollReason();
        this.mEnrollIgnoreList = getContext().getResources().getIntArray(android.R.array.config_ethernet_interfaces);
        this.mEnrollIgnoreListVendor = getContext().getResources().getIntArray(android.R.array.config_face_acquire_keyguard_ignorelist);
        this.mMaxTemplatesPerUser = maxTemplatesPerUser;
        this.mDebugConsent = debugConsent;
        this.mDisabledFeatures = disabledFeatures;
        this.mPreviewSurface = previewSurface;
        android.util.Slog.w(TAG, "EnrollOptions " + android.hardware.face.FaceEnrollOptions.enrollReasonToString(options.getEnrollReason()));
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFaceEnrollNotification(getContext());
        com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFaceReEnrollNotification(getContext());
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected com.android.server.biometrics.sensors.ClientMonitorCallback wrapCallbackForStart(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        return new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(this.mPreviewHandleDeleterCallback, getLogger().getAmbientLightProbe(true), callback);
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient
    protected boolean hasReachedEnrollmentLimit() {
        return com.android.server.biometrics.sensors.face.FaceUtils.getInstance(getSensorId()).getBiometricsForUser(getContext(), getTargetUserId()).size() >= this.mMaxTemplatesPerUser;
    }

    private boolean shouldSendAcquiredMessage(int acquireInfo, int vendorCode) {
        return acquireInfo == 22 ? !com.android.server.biometrics.Utils.listContains(this.mEnrollIgnoreListVendor, vendorCode) : !com.android.server.biometrics.Utils.listContains(this.mEnrollIgnoreList, acquireInfo);
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int acquireInfo, int vendorCode) {
        boolean shouldSend = shouldSendAcquiredMessage(acquireInfo, vendorCode);
        if (shouldSend) {
            int helpCode = getHelpCode(acquireInfo, vendorCode);
            java.lang.String helpMessage = android.hardware.face.FaceManager.getEnrollHelpMessage(getContext(), acquireInfo, vendorCode);
            this.mAuthenticationStateListeners.onAuthenticationHelp(new android.hardware.biometrics.events.AuthenticationHelpInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReasonFromFaceEnrollReason(this.mEnrollReason), helpMessage, helpCode).build());
        }
        onAcquiredInternal(acquireInfo, vendorCode, shouldSend);
    }

    public void onEnrollmentFrame(android.hardware.face.FaceEnrollFrame frame) {
        int acquireInfo = frame.getData().getAcquiredInfo();
        int vendorCode = frame.getData().getVendorCode();
        onAcquiredInternal(acquireInfo, vendorCode, false);
        boolean shouldSend = shouldSendAcquiredMessage(acquireInfo, vendorCode);
        if (shouldSend) {
            try {
                int helpCode = getHelpCode(acquireInfo, vendorCode);
                java.lang.String helpMessage = android.hardware.face.FaceManager.getEnrollHelpMessage(getContext(), acquireInfo, vendorCode);
                this.mAuthenticationStateListeners.onAuthenticationHelp(new android.hardware.biometrics.events.AuthenticationHelpInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReasonFromFaceEnrollReason(this.mEnrollReason), helpMessage, helpCode).build());
                getListener().onEnrollmentFrame(frame);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to send enrollment frame", e);
                this.mCallback.onClientFinished(this, false);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
        this.mAuthenticationStateListeners.onAuthenticationStarted(new android.hardware.biometrics.events.AuthenticationStartedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReasonFromFaceEnrollReason(this.mEnrollReason)).build());
        obtainSurfaceHandlesIfNeeded();
        try {
            java.util.List<java.lang.Byte> featureList = new java.util.ArrayList<>();
            if (this.mDebugConsent) {
                featureList.add((byte) 2);
            }
            boolean shouldAddDiversePoses = true;
            for (int disabledFeature : this.mDisabledFeatures) {
                if (com.android.server.biometrics.sensors.face.aidl.AidlConversionUtils.convertFrameworkToAidlFeature(disabledFeature) == 1) {
                    shouldAddDiversePoses = false;
                }
            }
            if (shouldAddDiversePoses) {
                featureList.add((byte) 1);
            }
            byte[] features = new byte[featureList.size()];
            for (int i = 0; i < featureList.size(); i++) {
                features[i] = featureList.get(i).byteValue();
            }
            doEnroll(features);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Exception when requesting enroll", e);
            onError(2, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    @Override // com.android.server.biometrics.sensors.EnrollClient, com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int error, int vendorCode) {
        this.mAuthenticationStateListeners.onAuthenticationError(new android.hardware.biometrics.events.AuthenticationErrorInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReasonFromFaceEnrollReason(this.mEnrollReason), android.hardware.face.FaceManager.getErrorString(getContext(), error, vendorCode), error).build());
        super.onError(error, vendorCode);
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReasonFromFaceEnrollReason(this.mEnrollReason)).build());
    }

    private void doEnroll(final byte[] features) throws android.os.RemoteException {
        final com.android.server.biometrics.sensors.face.aidl.AidlSession session = getFreshDaemon();
        final android.hardware.keymaster.HardwareAuthToken hat = com.android.server.biometrics.HardwareAuthTokenUtils.toHardwareAuthToken(this.mHardwareAuthToken);
        if (session.hasContextMethods()) {
            com.android.server.biometrics.log.OperationContextExt opContext = getOperationContext();
            getBiometricContext().subscribe(opContext, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$doEnroll$0(session, hat, features, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient.lambda$doEnroll$1(session, (android.hardware.biometrics.common.OperationContext) obj);
                }
            }, null);
        } else {
            this.mCancellationSignal = session.getSession().enroll(hat, (byte) 0, features, this.mHwPreviewHandle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doEnroll$0(com.android.server.biometrics.sensors.face.aidl.AidlSession session, android.hardware.keymaster.HardwareAuthToken hat, byte[] features, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            if (session.supportsFaceEnrollOptions()) {
                android.hardware.biometrics.face.FaceEnrollOptions options = new android.hardware.biometrics.face.FaceEnrollOptions();
                options.hardwareAuthToken = hat;
                options.enrollmentType = (byte) 0;
                options.features = features;
                options.nativeHandlePreview = null;
                options.context = ctx;
                options.surfacePreview = this.mPreviewSurface;
                this.mCancellationSignal = session.getSession().enrollWithOptions(options);
            } else {
                this.mCancellationSignal = session.getSession().enrollWithContext(hat, (byte) 0, features, this.mHwPreviewHandle, ctx);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception when requesting enroll", e);
            onError(2, 0);
            this.mCallback.onClientFinished(this, false);
        }
    }

    static /* synthetic */ void lambda$doEnroll$1(com.android.server.biometrics.sensors.face.aidl.AidlSession session, android.hardware.biometrics.common.OperationContext ctx) {
        try {
            session.getSession().onContextChanged(ctx);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to notify context changed", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    protected void stopHalOperation() {
        this.mAuthenticationStateListeners.onAuthenticationStopped(new android.hardware.biometrics.events.AuthenticationStoppedInfo.Builder(android.hardware.biometrics.BiometricSourceType.FACE, getRequestReasonFromFaceEnrollReason(this.mEnrollReason)).build());
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

    private void obtainSurfaceHandlesIfNeeded() {
        if (this.mPreviewSurface != null) {
            this.mOsPreviewHandle = com.android.server.biometrics.sensors.face.FaceService.acquireSurfaceHandle(this.mPreviewSurface);
            try {
                this.mHwPreviewHandle = com.android.server.biometrics.sensors.face.aidl.AidlNativeHandleUtils.dup(this.mOsPreviewHandle);
                android.util.Slog.v(TAG, "Obtained handles for the preview surface.");
            } catch (java.io.IOException e) {
                this.mHwPreviewHandle = null;
                android.util.Slog.e(TAG, "Failed to dup mOsPreviewHandle", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseSurfaceHandlesIfNeeded() {
        if (this.mPreviewSurface != null && this.mHwPreviewHandle == null) {
            android.util.Slog.w(TAG, "mHwPreviewHandle is null even though mPreviewSurface is not null.");
        }
        if (this.mHwPreviewHandle != null) {
            try {
                android.util.Slog.v(TAG, "Closing mHwPreviewHandle");
                com.android.server.biometrics.sensors.face.aidl.AidlNativeHandleUtils.close(this.mHwPreviewHandle);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to close mPreviewSurface", e);
            }
            this.mHwPreviewHandle = null;
        }
        if (this.mOsPreviewHandle != null) {
            android.util.Slog.v(TAG, "Releasing mOsPreviewHandle");
            com.android.server.biometrics.sensors.face.FaceService.releaseSurfaceHandle(this.mOsPreviewHandle);
            this.mOsPreviewHandle = null;
        }
        if (this.mPreviewSurface != null) {
            android.util.Slog.v(TAG, "Releasing mPreviewSurface");
            this.mPreviewSurface.release();
        }
    }

    private static int getHelpCode(int acquireInfo, int vendorCode) {
        if (acquireInfo == 22) {
            return vendorCode + 1000;
        }
        return acquireInfo;
    }
}
