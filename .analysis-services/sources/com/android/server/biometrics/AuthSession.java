package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public final class AuthSession implements android.os.IBinder.DeathRecipient {
    private static final boolean DEBUG = true;
    private static final java.lang.String TAG = "BiometricService/AuthSession";
    private int mAuthenticatedSensorId;
    private long mAuthenticatedTimeMs;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    final com.android.server.biometrics.log.BiometricFrameworkStatsLogger mBiometricFrameworkStatsLogger;
    private final android.hardware.biometrics.BiometricManager mBiometricManager;
    private boolean mCancelled;
    private final com.android.server.biometrics.AuthSession.ClientDeathReceiver mClientDeathReceiver;
    private final android.hardware.biometrics.IBiometricServiceReceiver mClientReceiver;
    private final android.content.Context mContext;
    private final boolean mDebugEnabled;
    private int mErrorEscrow;
    private final java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> mFingerprintSensorProperties;
    private final android.security.KeyStoreAuthorization mKeyStoreAuthorization;
    private final java.lang.String mOpPackageName;
    private final com.android.server.biometrics.log.OperationContextExt mOperationContext;
    private final long mOperationId;
    final com.android.server.biometrics.PreAuthInfo mPreAuthInfo;
    final android.hardware.biometrics.PromptInfo mPromptInfo;
    private final java.util.Random mRandom;
    private final long mRequestId;
    final android.hardware.biometrics.IBiometricSensorReceiver mSensorReceiver;
    private int[] mSensors;
    private final java.util.List<java.lang.Integer> mSfpsSensorIds;
    private long mStartTimeMs;
    private int mState;
    private final com.android.internal.statusbar.IStatusBarService mStatusBarService;
    final android.hardware.biometrics.IBiometricSysuiReceiver mSysuiReceiver;
    final android.os.IBinder mToken;
    private byte[] mTokenEscrow;
    private final int mUserId;
    private int mVendorCodeEscrow;

    interface ClientDeathReceiver {
        void onClientDied();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface SessionState {
    }

    AuthSession(android.content.Context context, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.internal.statusbar.IStatusBarService statusBarService, android.hardware.biometrics.IBiometricSysuiReceiver sysuiReceiver, android.security.KeyStoreAuthorization keyStoreAuthorization, java.util.Random random, com.android.server.biometrics.AuthSession.ClientDeathReceiver clientDeathReceiver, com.android.server.biometrics.PreAuthInfo preAuthInfo, android.os.IBinder token, long requestId, long operationId, int userId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, android.hardware.biometrics.IBiometricServiceReceiver clientReceiver, java.lang.String opPackageName, android.hardware.biometrics.PromptInfo promptInfo, boolean debugEnabled, java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> fingerprintSensorProperties) {
        this(context, biometricContext, statusBarService, sysuiReceiver, keyStoreAuthorization, random, clientDeathReceiver, preAuthInfo, token, requestId, operationId, userId, sensorReceiver, clientReceiver, opPackageName, promptInfo, debugEnabled, fingerprintSensorProperties, com.android.server.biometrics.log.BiometricFrameworkStatsLogger.getInstance());
    }

    AuthSession(android.content.Context context, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.internal.statusbar.IStatusBarService statusBarService, android.hardware.biometrics.IBiometricSysuiReceiver sysuiReceiver, android.security.KeyStoreAuthorization keyStoreAuthorization, java.util.Random random, com.android.server.biometrics.AuthSession.ClientDeathReceiver clientDeathReceiver, com.android.server.biometrics.PreAuthInfo preAuthInfo, android.os.IBinder token, long requestId, long operationId, int userId, android.hardware.biometrics.IBiometricSensorReceiver sensorReceiver, android.hardware.biometrics.IBiometricServiceReceiver clientReceiver, java.lang.String opPackageName, android.hardware.biometrics.PromptInfo promptInfo, boolean debugEnabled, java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> fingerprintSensorProperties, com.android.server.biometrics.log.BiometricFrameworkStatsLogger logger) {
        this.mState = 0;
        this.mAuthenticatedSensorId = -1;
        android.util.Slog.d(TAG, "Creating AuthSession with: " + preAuthInfo);
        this.mContext = context;
        this.mBiometricContext = biometricContext;
        this.mStatusBarService = statusBarService;
        this.mSysuiReceiver = sysuiReceiver;
        this.mKeyStoreAuthorization = keyStoreAuthorization;
        this.mRandom = random;
        this.mClientDeathReceiver = clientDeathReceiver;
        this.mPreAuthInfo = preAuthInfo;
        this.mToken = token;
        this.mRequestId = requestId;
        this.mOperationId = operationId;
        this.mUserId = userId;
        this.mSensorReceiver = sensorReceiver;
        this.mClientReceiver = clientReceiver;
        this.mOpPackageName = opPackageName;
        this.mPromptInfo = promptInfo;
        this.mDebugEnabled = debugEnabled;
        this.mFingerprintSensorProperties = fingerprintSensorProperties;
        this.mCancelled = false;
        this.mBiometricFrameworkStatsLogger = logger;
        this.mOperationContext = new com.android.server.biometrics.log.OperationContextExt(true);
        this.mBiometricManager = (android.hardware.biometrics.BiometricManager) this.mContext.getSystemService(android.hardware.biometrics.BiometricManager.class);
        this.mSfpsSensorIds = this.mFingerprintSensorProperties.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.hardware.fingerprint.FingerprintSensorPropertiesInternal) obj).isAnySidefpsType();
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((android.hardware.fingerprint.FingerprintSensorPropertiesInternal) obj).sensorId);
            }
        }).toList();
        try {
            this.mClientReceiver.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Unable to link to death");
        }
        setSensorsToStateUnknown();
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.e(TAG, "Binder died, session: " + this);
        this.mClientDeathReceiver.onClientDied();
    }

    private int getEligibleModalities() {
        return this.mPreAuthInfo.getEligibleModalities();
    }

    private void setSensorsToStateUnknown() {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            android.util.Slog.v(TAG, "set to unknown state sensor: " + sensor.id);
            sensor.goToStateUnknown();
        }
    }

    private void setSensorsToStateWaitingForCookie(boolean isTryAgain) throws android.os.RemoteException {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            int state = sensor.getSensorState();
            if (isTryAgain && state != 5 && state != 4) {
                android.util.Slog.d(TAG, "Skip retry because sensor: " + sensor.id + " is: " + state);
            } else {
                if (isTryAgain) {
                    this.mState = 5;
                }
                int cookie = this.mRandom.nextInt(2147483646) + 1;
                boolean requireConfirmation = isConfirmationRequired(sensor);
                android.util.Slog.v(TAG, "waiting for cooking for sensor: " + sensor.id);
                sensor.goToStateWaitingForCookie(requireConfirmation, this.mToken, this.mOperationId, this.mUserId, this.mSensorReceiver, this.mOpPackageName, this.mRequestId, cookie, this.mPromptInfo.isAllowBackgroundAuthentication(), this.mPromptInfo.isForLegacyFingerprintManager());
            }
        }
    }

    void goToInitialState() throws android.os.RemoteException {
        if (this.mPreAuthInfo.credentialAvailable && this.mPreAuthInfo.eligibleSensors.isEmpty()) {
            this.mState = 9;
            this.mSensors = new int[0];
            this.mStatusBarService.showAuthenticationDialog(this.mPromptInfo, this.mSysuiReceiver, this.mSensors, true, false, this.mUserId, this.mOperationId, this.mOpPackageName, this.mRequestId);
        } else {
            if (!this.mPreAuthInfo.eligibleSensors.isEmpty()) {
                setSensorsToStateWaitingForCookie(false);
                this.mState = 1;
                return;
            }
            throw new java.lang.IllegalStateException("No authenticators requested");
        }
    }

    void onCookieReceived(int cookie) {
        java.lang.String str;
        boolean requireConfirmation;
        com.android.internal.statusbar.IStatusBarService iStatusBarService;
        android.hardware.biometrics.PromptInfo promptInfo;
        android.hardware.biometrics.IBiometricSysuiReceiver iBiometricSysuiReceiver;
        int[] iArr;
        boolean zShouldShowCredential;
        int i;
        long j;
        java.lang.String str2;
        if (this.mCancelled) {
            android.util.Slog.w(TAG, "Received cookie but already cancelled (ignoring): " + cookie);
            return;
        }
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onCookieReceived after successful auth");
            return;
        }
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            sensor.goToStateCookieReturnedIfCookieMatches(cookie);
        }
        if (allCookiesReceived()) {
            this.mStartTimeMs = java.lang.System.currentTimeMillis();
            startAllPreparedSensorsExceptFingerprint();
            if (this.mState == 5) {
                this.mState = 3;
                return;
            }
            try {
                requireConfirmation = isConfirmationRequiredByAnyEligibleSensor();
                this.mSensors = new int[this.mPreAuthInfo.eligibleSensors.size()];
                for (int i2 = 0; i2 < this.mPreAuthInfo.eligibleSensors.size(); i2++) {
                    this.mSensors[i2] = this.mPreAuthInfo.eligibleSensors.get(i2).id;
                }
                iStatusBarService = this.mStatusBarService;
                promptInfo = this.mPromptInfo;
                iBiometricSysuiReceiver = this.mSysuiReceiver;
                iArr = this.mSensors;
                zShouldShowCredential = this.mPreAuthInfo.shouldShowCredential();
                i = this.mUserId;
                j = this.mOperationId;
                str2 = this.mOpPackageName;
                str = TAG;
            } catch (android.os.RemoteException e) {
                e = e;
                str = TAG;
            }
            try {
                iStatusBarService.showAuthenticationDialog(promptInfo, iBiometricSysuiReceiver, iArr, zShouldShowCredential, requireConfirmation, i, j, str2, this.mRequestId);
                this.mState = 2;
                return;
            } catch (android.os.RemoteException e2) {
                e = e2;
                android.util.Slog.e(str, "Remote exception", e);
                return;
            }
        }
        android.util.Slog.v(TAG, "onCookieReceived: still waiting");
    }

    private boolean isConfirmationRequired(com.android.server.biometrics.BiometricSensor sensor) {
        return sensor.confirmationSupported() && (sensor.confirmationAlwaysRequired(this.mUserId) || this.mPreAuthInfo.confirmationRequested);
    }

    private boolean isConfirmationRequiredByAnyEligibleSensor() {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (isConfirmationRequired(sensor)) {
                return true;
            }
        }
        return false;
    }

    private void startAllPreparedSensorsExceptFingerprint() {
        startAllPreparedSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Boolean.valueOf(((com.android.server.biometrics.BiometricSensor) obj).modality != 2);
            }
        });
    }

    private void startAllPreparedFingerprintSensors() {
        startAllPreparedSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Boolean.valueOf(((com.android.server.biometrics.BiometricSensor) obj).modality == 2);
            }
        });
    }

    private void startAllPreparedSensors(java.util.function.Function<com.android.server.biometrics.BiometricSensor, java.lang.Boolean> filter) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (filter.apply(sensor).booleanValue()) {
                try {
                    android.util.Slog.v(TAG, "Starting sensor: " + sensor.id);
                    sensor.startSensor();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Unable to start prepared client, sensor: " + sensor, e);
                }
            }
        }
    }

    private void cancelAllSensors() {
        cancelAllSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.biometrics.AuthSession.lambda$cancelAllSensors$3((com.android.server.biometrics.BiometricSensor) obj);
            }
        });
    }

    static /* synthetic */ java.lang.Boolean lambda$cancelAllSensors$3(com.android.server.biometrics.BiometricSensor sensor) {
        return true;
    }

    private void cancelAllSensors(java.util.function.Function<com.android.server.biometrics.BiometricSensor, java.lang.Boolean> filter) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            try {
                if (filter.apply(sensor).booleanValue()) {
                    android.util.Slog.d(TAG, "Cancelling sensorId: " + sensor.id);
                    sensor.goToStateCancelling(this.mToken, this.mOpPackageName, this.mRequestId);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to cancel authentication");
            }
        }
    }

    boolean onErrorReceived(final int sensorId, int cookie, int error, int vendorCode) throws android.os.RemoteException {
        android.util.Slog.d(TAG, "onErrorReceived sensor: " + sensorId + " error: " + error);
        if (!containsCookie(cookie)) {
            android.util.Slog.e(TAG, "Unknown/expired cookie: " + cookie);
            return false;
        }
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (sensor.getSensorState() == 3) {
                sensor.goToStoppedStateIfCookieMatches(cookie, error);
            }
        }
        if (hasAuthenticated()) {
            android.util.Slog.d(TAG, "onErrorReceived after successful auth (ignoring)");
            return false;
        }
        boolean errorLockout = error == 7 || error == 9;
        if (errorLockout) {
            cancelAllSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$onErrorReceived$4(sensorId, (com.android.server.biometrics.BiometricSensor) obj);
                }
            });
        }
        this.mErrorEscrow = error;
        this.mVendorCodeEscrow = vendorCode;
        int modality = sensorIdToModality(sensorId);
        switch (this.mState) {
            case 1:
                if (isAllowDeviceCredential()) {
                    int authenticators = this.mPromptInfo.getAuthenticators();
                    this.mPromptInfo.setAuthenticators(com.android.server.biometrics.Utils.removeBiometricBits(authenticators));
                    this.mState = 9;
                    this.mSensors = new int[0];
                    this.mStatusBarService.showAuthenticationDialog(this.mPromptInfo, this.mSysuiReceiver, this.mSensors, true, false, this.mUserId, this.mOperationId, this.mOpPackageName, this.mRequestId);
                } else {
                    this.mClientReceiver.onError(modality, error, vendorCode);
                }
                break;
            case 2:
            case 3:
            case 6:
                if (isAllowDeviceCredential() && errorLockout) {
                    this.mState = 9;
                    this.mStatusBarService.onBiometricError(modality, error, vendorCode);
                } else if (error == 5) {
                    this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                    this.mClientReceiver.onError(modality, error, vendorCode);
                } else {
                    this.mState = 8;
                    this.mStatusBarService.onBiometricError(modality, error, vendorCode);
                }
                break;
            case 4:
                this.mClientReceiver.onError(modality, error, vendorCode);
                this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                break;
            case 5:
            case 7:
            case 8:
            default:
                android.util.Slog.e(TAG, "Unhandled error state, mState: " + this.mState);
                break;
            case 9:
                android.util.Slog.d(TAG, "Biometric canceled, ignoring from state: " + this.mState);
                break;
            case 10:
                this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                break;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$onErrorReceived$4(int sensorId, com.android.server.biometrics.BiometricSensor sensor) {
        return java.lang.Boolean.valueOf(com.android.server.biometrics.Utils.isAtLeastStrength(sensorIdToStrength(sensorId), sensor.getCurrentStrength()));
    }

    void onAcquired(int sensorId, int acquiredInfo, int vendorCode) {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onAcquired after successful auth");
            return;
        }
        java.lang.String message = getAcquiredMessageForSensor(sensorId, acquiredInfo, vendorCode);
        android.util.Slog.d(TAG, "sensorId: " + sensorId + " acquiredInfo: " + acquiredInfo + " message: " + message);
        if (message == null) {
            return;
        }
        try {
            this.mStatusBarService.onBiometricHelp(sensorIdToModality(sensorId), message);
            int aAcquiredInfo = acquiredInfo == 6 ? vendorCode + 1000 : acquiredInfo;
            this.mClientReceiver.onAcquired(aAcquiredInfo, message);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    void onSystemEvent(int event) {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onSystemEvent after successful auth");
        } else {
            if (!this.mPromptInfo.isReceiveSystemEvents()) {
                return;
            }
            try {
                this.mClientReceiver.onSystemEvent(event);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException", e);
            }
        }
    }

    void onDialogAnimatedIn(boolean startFingerprintNow) {
        if (this.mState != 2 && this.mState != 8 && this.mState != 4 && this.mState != 6) {
            android.util.Slog.e(TAG, "onDialogAnimatedIn, unexpected state: " + this.mState);
            return;
        }
        if (this.mState != 6) {
            this.mState = 3;
        }
        if (startFingerprintNow) {
            startAllPreparedFingerprintSensors();
        } else {
            android.util.Slog.d(TAG, "delaying fingerprint sensor start");
        }
        this.mBiometricContext.updateContext(this.mOperationContext, isCrypto());
    }

    void onStartFingerprint() {
        if (this.mState != 2 && this.mState != 3 && this.mState != 4 && this.mState != 6 && this.mState != 8) {
            android.util.Slog.w(TAG, "onStartFingerprint, started from unexpected state: " + this.mState);
        }
        startAllPreparedFingerprintSensors();
    }

    void onTryAgainPressed() {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onTryAgainPressed after successful auth");
            return;
        }
        if (this.mState != 4) {
            android.util.Slog.w(TAG, "onTryAgainPressed, state: " + this.mState);
        }
        try {
            setSensorsToStateWaitingForCookie(true);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException: " + e);
        }
    }

    void onAuthenticationSucceeded(final int sensorId, boolean strong, byte[] token) {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onAuthenticationSucceeded after successful auth");
            return;
        }
        this.mAuthenticatedSensorId = sensorId;
        if (strong) {
            this.mTokenEscrow = token;
        } else if (token != null) {
            android.util.Slog.w(TAG, "Dropping authToken for non-strong biometric, id: " + sensorId);
        }
        try {
            this.mStatusBarService.onBiometricAuthenticated(sensorIdToModality(sensorId));
            boolean requireConfirmation = isConfirmationRequiredByAnyEligibleSensor();
            if (!requireConfirmation) {
                this.mState = 7;
            } else {
                this.mAuthenticatedTimeMs = java.lang.System.currentTimeMillis();
                this.mState = 6;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
        }
        if (this.mState == 6) {
            cancelAllSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda6
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$onAuthenticationSucceeded$5(sensorId, (com.android.server.biometrics.BiometricSensor) obj);
                }
            });
        } else {
            cancelAllSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda7
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Boolean.valueOf(((com.android.server.biometrics.BiometricSensor) obj).id != sensorId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$onAuthenticationSucceeded$5(int sensorId, com.android.server.biometrics.BiometricSensor sensor) {
        return java.lang.Boolean.valueOf((sensor.id == sensorId || this.mSfpsSensorIds.contains(java.lang.Integer.valueOf(sensor.id))) ? false : true);
    }

    void onAuthenticationRejected(int sensorId) {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onAuthenticationRejected after successful auth");
            return;
        }
        try {
            this.mStatusBarService.onBiometricError(sensorIdToModality(sensorId), 100, 0);
            if (pauseSensorIfSupported(sensorId)) {
                this.mState = 4;
            }
            this.mClientReceiver.onAuthenticationFailed();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
        }
    }

    void onAuthenticationTimedOut(int sensorId, int cookie, int error, int vendorCode) {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onAuthenticationTimedOut after successful auth");
            return;
        }
        try {
            this.mStatusBarService.onBiometricError(sensorIdToModality(sensorId), error, vendorCode);
            pauseSensorIfSupported(sensorId);
            this.mState = 4;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
        }
    }

    private boolean pauseSensorIfSupported(final int sensorId) {
        boolean isSensorCancelling = sensorIdToState(sensorId) == 4;
        if (sensorIdToModality(sensorId) != 8 || isSensorCancelling) {
            return false;
        }
        cancelAllSensors(new java.util.function.Function() { // from class: com.android.server.biometrics.AuthSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Boolean.valueOf(((com.android.server.biometrics.BiometricSensor) obj).id == sensorId);
            }
        });
        return true;
    }

    void onDeviceCredentialPressed() {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onDeviceCredentialPressed after successful auth");
        } else {
            cancelAllSensors();
            this.mState = 9;
        }
    }

    boolean onClientDied() {
        try {
            switch (this.mState) {
                case 2:
                case 3:
                    this.mState = 10;
                    cancelAllSensors();
                    return false;
                default:
                    this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
                    return true;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote Exception: " + e);
            return true;
        }
    }

    private boolean hasAuthenticated() {
        return this.mAuthenticatedSensorId != -1;
    }

    private boolean hasAuthenticatedAndConfirmed() {
        return this.mAuthenticatedSensorId != -1 && this.mState == 7;
    }

    private void logOnDialogDismissed(int reason) {
        if (reason == 1) {
            long latency = java.lang.System.currentTimeMillis() - this.mAuthenticatedTimeMs;
            android.util.Slog.v(TAG, "Confirmed! Modality: " + statsModality() + ", User: " + this.mUserId + ", IsCrypto: " + isCrypto() + ", Client: " + getStatsClient() + ", RequireConfirmation: " + this.mPreAuthInfo.confirmationRequested + ", State: 3, Latency: " + latency + ", SessionId: " + this.mOperationContext.getId());
            this.mBiometricFrameworkStatsLogger.authenticate(this.mOperationContext, statsModality(), 0, getStatsClient(), this.mDebugEnabled, latency, 3, this.mPreAuthInfo.confirmationRequested, this.mUserId, -1.0f);
            return;
        }
        long latency2 = java.lang.System.currentTimeMillis() - this.mStartTimeMs;
        int error = 0;
        switch (reason) {
            case 2:
                error = 13;
                break;
            case 3:
                error = 10;
                break;
        }
        android.util.Slog.v(TAG, "Dismissed! Modality: " + statsModality() + ", User: " + this.mUserId + ", IsCrypto: " + isCrypto() + ", Action: 2, Client: " + getStatsClient() + ", Reason: " + reason + ", Error: " + error + ", Latency: " + latency2 + ", SessionId: " + this.mOperationContext.getId());
        if (error != 0) {
            this.mBiometricFrameworkStatsLogger.error(this.mOperationContext, statsModality(), 2, getStatsClient(), this.mDebugEnabled, latency2, error, 0, this.mUserId);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x003c A[Catch: all -> 0x006c, RemoteException -> 0x006f, TryCatch #0 {RemoteException -> 0x006f, blocks: (B:6:0x000b, B:7:0x0011, B:8:0x0017, B:9:0x0025, B:10:0x0032, B:11:0x0038, B:13:0x003c, B:15:0x0062, B:14:0x005c, B:20:0x0071), top: B:53:0x0005, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x005c A[Catch: all -> 0x006c, RemoteException -> 0x006f, TryCatch #0 {RemoteException -> 0x006f, blocks: (B:6:0x000b, B:7:0x0011, B:8:0x0017, B:9:0x0025, B:10:0x0032, B:11:0x0038, B:13:0x003c, B:15:0x0062, B:14:0x005c, B:20:0x0071), top: B:53:0x0005, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void onDialogDismissed(int r9, byte[] r10) {
        /*
            Method dump skipped, instruction units count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.biometrics.AuthSession.onDialogDismissed(int, byte[]):void");
    }

    boolean onCancelAuthSession(boolean force) {
        if (hasAuthenticatedAndConfirmed()) {
            android.util.Slog.d(TAG, "onCancelAuthSession after successful auth");
            return true;
        }
        this.mCancelled = true;
        boolean authStarted = this.mState == 1 || this.mState == 2 || this.mState == 3;
        cancelAllSensors();
        if (authStarted && !force) {
            return false;
        }
        try {
            this.mClientReceiver.onError(getEligibleModalities(), 5, 0);
            this.mStatusBarService.hideAuthenticationDialog(this.mRequestId);
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
            return false;
        }
    }

    boolean isCrypto() {
        return this.mOperationId != 0;
    }

    private boolean containsCookie(int cookie) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (sensor.getCookie() == cookie) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowDeviceCredential() {
        return com.android.server.biometrics.Utils.isCredentialRequested(this.mPromptInfo);
    }

    boolean allCookiesReceived() {
        int remainingCookies = this.mPreAuthInfo.numSensorsWaitingForCookie();
        android.util.Slog.d(TAG, "Remaining cookies: " + remainingCookies);
        return remainingCookies == 0;
    }

    int getState() {
        return this.mState;
    }

    long getRequestId() {
        return this.mRequestId;
    }

    private int statsModality() {
        int modality = 0;
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if ((sensor.modality & 2) != 0) {
                modality |= 1;
            }
            if ((sensor.modality & 4) != 0) {
                modality |= 2;
            }
            if ((sensor.modality & 8) != 0) {
                modality |= 4;
            }
        }
        return modality;
    }

    private int sensorIdToModality(int sensorId) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (sensorId == sensor.id) {
                return sensor.modality;
            }
        }
        android.util.Slog.e(TAG, "Unknown sensor: " + sensorId);
        return 0;
    }

    private int sensorIdToState(int sensorId) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (sensorId == sensor.id) {
                return sensor.getSensorState();
            }
        }
        android.util.Slog.e(TAG, "Unknown sensor: " + sensorId);
        return 0;
    }

    private int sensorIdToStrength(int sensorId) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mPreAuthInfo.eligibleSensors) {
            if (sensorId == sensor.id) {
                return sensor.getCurrentStrength();
            }
        }
        android.util.Slog.e(TAG, "Unknown sensor: " + sensorId);
        return 4095;
    }

    private java.lang.String getAcquiredMessageForSensor(int sensorId, int acquiredInfo, int vendorCode) {
        int modality = sensorIdToModality(sensorId);
        switch (modality) {
            case 2:
                return android.hardware.fingerprint.FingerprintManager.getAcquiredString(this.mContext, acquiredInfo, vendorCode);
            case 8:
                return android.hardware.face.FaceManager.getAuthHelpMessage(this.mContext, acquiredInfo, vendorCode);
            default:
                return null;
        }
    }

    private int getStatsClient() {
        if (this.mPromptInfo.isForLegacyFingerprintManager()) {
            return 3;
        }
        return 2;
    }

    public java.lang.String toString() {
        return "State: " + this.mState + ", cancelled: " + this.mCancelled + ", isCrypto: " + isCrypto() + ", PreAuthInfo: " + this.mPreAuthInfo + ", requestId: " + this.mRequestId;
    }
}
