package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class BiometricService extends com.android.server.SystemService {
    static final java.lang.String TAG = "BiometricService";
    com.android.server.biometrics.AuthSession mAuthSession;
    private final com.android.server.biometrics.BiometricCameraManager mBiometricCameraManager;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final com.android.server.biometrics.BiometricNotificationLogger mBiometricNotificationLogger;
    private com.android.server.biometrics.IBiometricServiceExt mBiometricServiceExt;
    com.android.server.biometrics.BiometricStrengthController mBiometricStrengthController;
    private final android.app.admin.DevicePolicyManager mDevicePolicyManager;
    private final java.util.List<com.android.server.biometrics.BiometricService.EnabledOnKeyguardCallback> mEnabledOnKeyguardCallbacks;
    android.service.gatekeeper.IGateKeeperService mGateKeeper;
    private final android.os.Handler mHandler;
    final android.hardware.biometrics.IBiometricService.Stub mImpl;
    private final com.android.server.biometrics.BiometricService.Injector mInjector;
    android.security.KeyStoreAuthorization mKeyStoreAuthorization;
    private final java.util.Random mRandom;
    private final java.util.function.Supplier<java.lang.Long> mRequestCounter;
    final java.util.ArrayList<com.android.server.biometrics.BiometricSensor> mSensors;
    final com.android.server.biometrics.BiometricService.SettingObserver mSettingObserver;
    com.android.internal.statusbar.IStatusBarService mStatusBarService;
    android.app.trust.ITrustManager mTrustManager;
    private final android.os.UserManager mUserManager;

    static class InvalidationTracker {
        private final android.hardware.biometrics.IInvalidationCallback mClientCallback;
        private final java.util.Set<java.lang.Integer> mSensorsPendingInvalidation = new android.util.ArraySet();

        public static com.android.server.biometrics.BiometricService.InvalidationTracker start(android.content.Context context, java.util.ArrayList<com.android.server.biometrics.BiometricSensor> sensors, int userId, int fromSensorId, android.hardware.biometrics.IInvalidationCallback clientCallback) {
            return new com.android.server.biometrics.BiometricService.InvalidationTracker(context, sensors, userId, fromSensorId, clientCallback);
        }

        private InvalidationTracker(android.content.Context context, java.util.ArrayList<com.android.server.biometrics.BiometricSensor> sensors, int userId, int fromSensorId, android.hardware.biometrics.IInvalidationCallback clientCallback) {
            this.mClientCallback = clientCallback;
            for (final com.android.server.biometrics.BiometricSensor sensor : sensors) {
                if (sensor.id != fromSensorId && com.android.server.biometrics.Utils.isAtLeastStrength(sensor.oemStrength, 15)) {
                    try {
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote Exception", e);
                    }
                    if (sensor.impl.hasEnrolledTemplates(userId, context.getOpPackageName())) {
                        android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "Requesting authenticatorId invalidation for sensor: " + sensor.id);
                        synchronized (this) {
                            this.mSensorsPendingInvalidation.add(java.lang.Integer.valueOf(sensor.id));
                        }
                        try {
                            sensor.impl.invalidateAuthenticatorId(userId, new android.hardware.biometrics.IInvalidationCallback.Stub() { // from class: com.android.server.biometrics.BiometricService.InvalidationTracker.1
                                public void onCompleted() {
                                    com.android.server.biometrics.BiometricService.InvalidationTracker.this.onInvalidated(sensor.id);
                                }
                            });
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "RemoteException", e2);
                        }
                    } else {
                        continue;
                    }
                }
            }
            synchronized (this) {
                if (this.mSensorsPendingInvalidation.isEmpty()) {
                    try {
                        android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "No sensors require invalidation");
                        this.mClientCallback.onCompleted();
                    } catch (android.os.RemoteException e3) {
                        android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote Exception", e3);
                    }
                }
            }
        }

        void onInvalidated(int sensorId) {
            synchronized (this) {
                this.mSensorsPendingInvalidation.remove(java.lang.Integer.valueOf(sensorId));
                android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "Sensor " + sensorId + " invalidated, remaining size: " + this.mSensorsPendingInvalidation.size());
                if (this.mSensorsPendingInvalidation.isEmpty()) {
                    try {
                        this.mClientCallback.onCompleted();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote Exception", e);
                    }
                }
            }
        }
    }

    public static class SettingObserver extends android.database.ContentObserver {
        private static final boolean DEFAULT_ALWAYS_REQUIRE_CONFIRMATION = false;
        private static final boolean DEFAULT_APP_ENABLED = true;
        private static final boolean DEFAULT_KEYGUARD_ENABLED = true;
        private final android.net.Uri BIOMETRIC_APP_ENABLED;
        private final android.net.Uri BIOMETRIC_KEYGUARD_ENABLED;
        private final android.net.Uri FACE_UNLOCK_ALWAYS_REQUIRE_CONFIRMATION;
        private final android.net.Uri FACE_UNLOCK_APP_ENABLED;
        private final android.net.Uri FACE_UNLOCK_KEYGUARD_ENABLED;
        private final java.util.Map<java.lang.Integer, java.lang.Boolean> mBiometricEnabledForApps;
        private final java.util.Map<java.lang.Integer, java.lang.Boolean> mBiometricEnabledOnKeyguard;
        private final java.util.List<com.android.server.biometrics.BiometricService.EnabledOnKeyguardCallback> mCallbacks;
        private final android.content.ContentResolver mContentResolver;
        private final java.util.Map<java.lang.Integer, java.lang.Boolean> mFaceAlwaysRequireConfirmation;
        private final boolean mUseLegacyFaceOnlySettings;

        public SettingObserver(android.content.Context context, android.os.Handler handler, java.util.List<com.android.server.biometrics.BiometricService.EnabledOnKeyguardCallback> callbacks) {
            super(handler);
            this.FACE_UNLOCK_KEYGUARD_ENABLED = android.provider.Settings.Secure.getUriFor("face_unlock_keyguard_enabled");
            this.FACE_UNLOCK_APP_ENABLED = android.provider.Settings.Secure.getUriFor("face_unlock_app_enabled");
            this.FACE_UNLOCK_ALWAYS_REQUIRE_CONFIRMATION = android.provider.Settings.Secure.getUriFor("face_unlock_always_require_confirmation");
            this.BIOMETRIC_KEYGUARD_ENABLED = android.provider.Settings.Secure.getUriFor("biometric_keyguard_enabled");
            this.BIOMETRIC_APP_ENABLED = android.provider.Settings.Secure.getUriFor("biometric_app_enabled");
            this.mBiometricEnabledOnKeyguard = new java.util.HashMap();
            this.mBiometricEnabledForApps = new java.util.HashMap();
            this.mFaceAlwaysRequireConfirmation = new java.util.HashMap();
            this.mContentResolver = context.getContentResolver();
            this.mCallbacks = callbacks;
            boolean hasFingerprint = context.getPackageManager().hasSystemFeature("android.hardware.fingerprint");
            boolean hasFace = context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face");
            this.mUseLegacyFaceOnlySettings = android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT <= 29 && hasFace && !hasFingerprint;
            updateContentObserver();
        }

        public void updateContentObserver() {
            this.mContentResolver.unregisterContentObserver(this);
            if (this.mUseLegacyFaceOnlySettings) {
                this.mContentResolver.registerContentObserver(this.FACE_UNLOCK_KEYGUARD_ENABLED, false, this, -1);
                this.mContentResolver.registerContentObserver(this.FACE_UNLOCK_APP_ENABLED, false, this, -1);
            } else {
                this.mContentResolver.registerContentObserver(this.BIOMETRIC_KEYGUARD_ENABLED, false, this, -1);
                this.mContentResolver.registerContentObserver(this.BIOMETRIC_APP_ENABLED, false, this, -1);
            }
            this.mContentResolver.registerContentObserver(this.FACE_UNLOCK_ALWAYS_REQUIRE_CONFIRMATION, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (this.FACE_UNLOCK_KEYGUARD_ENABLED.equals(uri)) {
                this.mBiometricEnabledOnKeyguard.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "face_unlock_keyguard_enabled", 1, userId) != 0));
                if (userId == android.app.ActivityManager.getCurrentUser() && !selfChange) {
                    notifyEnabledOnKeyguardCallbacks(userId);
                    return;
                }
                return;
            }
            if (this.FACE_UNLOCK_APP_ENABLED.equals(uri)) {
                this.mBiometricEnabledForApps.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "face_unlock_app_enabled", 1, userId) != 0));
                return;
            }
            if (this.FACE_UNLOCK_ALWAYS_REQUIRE_CONFIRMATION.equals(uri)) {
                this.mFaceAlwaysRequireConfirmation.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "face_unlock_always_require_confirmation", 0, userId) != 0));
                return;
            }
            if (this.BIOMETRIC_KEYGUARD_ENABLED.equals(uri)) {
                this.mBiometricEnabledOnKeyguard.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "biometric_keyguard_enabled", 1, userId) != 0));
                if (userId == android.app.ActivityManager.getCurrentUser() && !selfChange) {
                    notifyEnabledOnKeyguardCallbacks(userId);
                    return;
                }
                return;
            }
            if (this.BIOMETRIC_APP_ENABLED.equals(uri)) {
                this.mBiometricEnabledForApps.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(android.provider.Settings.Secure.getIntForUser(this.mContentResolver, "biometric_app_enabled", 1, userId) != 0));
            }
        }

        public boolean getEnabledOnKeyguard(int userId) {
            if (!this.mBiometricEnabledOnKeyguard.containsKey(java.lang.Integer.valueOf(userId))) {
                if (this.mUseLegacyFaceOnlySettings) {
                    onChange(true, this.FACE_UNLOCK_KEYGUARD_ENABLED, userId);
                } else {
                    onChange(true, this.BIOMETRIC_KEYGUARD_ENABLED, userId);
                }
            }
            return this.mBiometricEnabledOnKeyguard.get(java.lang.Integer.valueOf(userId)).booleanValue();
        }

        public boolean getEnabledForApps(int userId) {
            if (!this.mBiometricEnabledForApps.containsKey(java.lang.Integer.valueOf(userId))) {
                if (this.mUseLegacyFaceOnlySettings) {
                    onChange(true, this.FACE_UNLOCK_APP_ENABLED, userId);
                } else {
                    onChange(true, this.BIOMETRIC_APP_ENABLED, userId);
                }
            }
            return this.mBiometricEnabledForApps.getOrDefault(java.lang.Integer.valueOf(userId), true).booleanValue();
        }

        public boolean getConfirmationAlwaysRequired(int modality, int userId) {
            switch (modality) {
                case 8:
                    if (!this.mFaceAlwaysRequireConfirmation.containsKey(java.lang.Integer.valueOf(userId))) {
                        onChange(true, this.FACE_UNLOCK_ALWAYS_REQUIRE_CONFIRMATION, userId);
                    }
                    return this.mFaceAlwaysRequireConfirmation.get(java.lang.Integer.valueOf(userId)).booleanValue();
                default:
                    return false;
            }
        }

        void notifyEnabledOnKeyguardCallbacks(int userId) {
            this.mCallbacks.removeIf(new java.util.function.Predicate() { // from class: com.android.server.biometrics.BiometricService$SettingObserver$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return java.util.Objects.isNull((com.android.server.biometrics.BiometricService.EnabledOnKeyguardCallback) obj);
                }
            });
            java.util.List<com.android.server.biometrics.BiometricService.EnabledOnKeyguardCallback> callbacks = this.mCallbacks;
            for (int i = 0; i < callbacks.size(); i++) {
                callbacks.get(i).notify(this.mBiometricEnabledOnKeyguard.getOrDefault(java.lang.Integer.valueOf(userId), true).booleanValue(), userId);
            }
        }
    }

    final class EnabledOnKeyguardCallback implements android.os.IBinder.DeathRecipient {
        private final android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback mCallback;

        EnabledOnKeyguardCallback(android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback callback) {
            this.mCallback = callback;
            try {
                this.mCallback.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.biometrics.BiometricService.TAG, "Unable to linkToDeath", e);
            }
        }

        void notify(boolean enabled, int userId) {
            try {
                this.mCallback.onChanged(enabled, userId);
            } catch (android.os.DeadObjectException e) {
                android.util.Slog.w(com.android.server.biometrics.BiometricService.TAG, "Death while invoking notify", e);
                com.android.server.biometrics.BiometricService.this.mEnabledOnKeyguardCallbacks.remove(this);
            } catch (android.os.RemoteException e2) {
                android.util.Slog.w(com.android.server.biometrics.BiometricService.TAG, "Failed to invoke onChanged", e2);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Enabled callback binder died");
            com.android.server.biometrics.BiometricService.this.mEnabledOnKeyguardCallbacks.remove(this);
        }
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.BiometricService$1, reason: invalid class name */
    class AnonymousClass1 extends android.hardware.biometrics.IBiometricSensorReceiver.Stub {
        final /* synthetic */ long val$requestId;

        AnonymousClass1(long j) {
            this.val$requestId = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationSucceeded$0(long requestId, int sensorId, byte[] token) {
            com.android.server.biometrics.BiometricService.this.handleAuthenticationSucceeded(requestId, sensorId, token);
        }

        public void onAuthenticationSucceeded(final int sensorId, final byte[] token) {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAuthenticationSucceeded$0(j, sensorId, token);
                }
            });
        }

        public void onAuthenticationFailed(final int sensorId) {
            android.util.Slog.v(com.android.server.biometrics.BiometricService.TAG, "onAuthenticationFailed");
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAuthenticationFailed$1(j, sensorId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationFailed$1(long requestId, int sensorId) {
            com.android.server.biometrics.BiometricService.this.handleAuthenticationRejected(requestId, sensorId);
        }

        public void onError(final int sensorId, final int cookie, final int error, final int vendorCode) {
            if (error == 3) {
                android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
                final long j = this.val$requestId;
                handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onError$2(j, sensorId, cookie, error, vendorCode);
                    }
                });
            } else {
                android.os.Handler handler2 = com.android.server.biometrics.BiometricService.this.mHandler;
                final long j2 = this.val$requestId;
                handler2.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$1$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onError$3(j2, sensorId, cookie, error, vendorCode);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$2(long requestId, int sensorId, int cookie, int error, int vendorCode) {
            com.android.server.biometrics.BiometricService.this.handleAuthenticationTimedOut(requestId, sensorId, cookie, error, vendorCode);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$3(long requestId, int sensorId, int cookie, int error, int vendorCode) {
            com.android.server.biometrics.BiometricService.this.handleOnError(requestId, sensorId, cookie, error, vendorCode);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAcquired$4(long requestId, int sensorId, int acquiredInfo, int vendorCode) {
            com.android.server.biometrics.BiometricService.this.handleOnAcquired(requestId, sensorId, acquiredInfo, vendorCode);
        }

        public void onAcquired(final int sensorId, final int acquiredInfo, final int vendorCode) {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$1$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAcquired$4(j, sensorId, acquiredInfo, vendorCode);
                }
            });
        }
    }

    private android.hardware.biometrics.IBiometricSensorReceiver createBiometricSensorReceiver(long requestId) {
        return new com.android.server.biometrics.BiometricService.AnonymousClass1(requestId);
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.BiometricService$2, reason: invalid class name */
    class AnonymousClass2 extends android.hardware.biometrics.IBiometricSysuiReceiver.Stub {
        final /* synthetic */ long val$requestId;

        AnonymousClass2(long j) {
            this.val$requestId = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDialogDismissed$0(long requestId, int reason, byte[] credentialAttestation) {
            com.android.server.biometrics.BiometricService.this.handleOnDismissed(requestId, reason, credentialAttestation);
        }

        public void onDialogDismissed(final int reason, final byte[] credentialAttestation) {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$2$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDialogDismissed$0(j, reason, credentialAttestation);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTryAgainPressed$1(long requestId) {
            com.android.server.biometrics.BiometricService.this.handleOnTryAgainPressed(requestId);
        }

        public void onTryAgainPressed() {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$2$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTryAgainPressed$1(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeviceCredentialPressed$2(long requestId) {
            com.android.server.biometrics.BiometricService.this.handleOnDeviceCredentialPressed(requestId);
        }

        public void onDeviceCredentialPressed() {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDeviceCredentialPressed$2(j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemEvent$3(long requestId, int event) {
            com.android.server.biometrics.BiometricService.this.handleOnSystemEvent(requestId, event);
        }

        public void onSystemEvent(final int event) {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSystemEvent$3(j, event);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDialogAnimatedIn$4(long requestId, boolean startFingerprintNow) {
            com.android.server.biometrics.BiometricService.this.handleOnDialogAnimatedIn(requestId, startFingerprintNow);
        }

        public void onDialogAnimatedIn(final boolean startFingerprintNow) {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDialogAnimatedIn$4(j, startFingerprintNow);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartFingerprintNow$5(long requestId) {
            com.android.server.biometrics.BiometricService.this.handleOnStartFingerprintNow(requestId);
        }

        public void onStartFingerprintNow() {
            android.os.Handler handler = com.android.server.biometrics.BiometricService.this.mHandler;
            final long j = this.val$requestId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStartFingerprintNow$5(j);
                }
            });
        }
    }

    private android.hardware.biometrics.IBiometricSysuiReceiver createSysuiReceiver(long requestId) {
        return new com.android.server.biometrics.BiometricService.AnonymousClass2(requestId);
    }

    private com.android.server.biometrics.AuthSession.ClientDeathReceiver createClientDeathReceiver(final long requestId) {
        return new com.android.server.biometrics.AuthSession.ClientDeathReceiver() { // from class: com.android.server.biometrics.BiometricService$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.AuthSession.ClientDeathReceiver
            public final void onClientDied() {
                this.f$0.lambda$createClientDeathReceiver$1(requestId);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createClientDeathReceiver$1(final long requestId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createClientDeathReceiver$0(requestId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class BiometricServiceWrapper extends android.hardware.biometrics.IBiometricService.Stub {
        private BiometricServiceWrapper() {
        }

        public android.hardware.biometrics.ITestSession createTestSession(int sensorId, android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) throws android.os.RemoteException {
            super.createTestSession_enforcePermission();
            for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                if (sensor.id == sensorId) {
                    return sensor.impl.createTestSession(callback, opPackageName);
                }
            }
            android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Unknown sensor for createTestSession: " + sensorId);
            return null;
        }

        public java.util.List<android.hardware.biometrics.SensorPropertiesInternal> getSensorProperties(java.lang.String opPackageName) throws android.os.RemoteException {
            super.getSensorProperties_enforcePermission();
            java.util.List<android.hardware.biometrics.SensorPropertiesInternal> sensors = new java.util.ArrayList<>();
            for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                android.hardware.biometrics.SensorPropertiesInternal prop = android.hardware.biometrics.SensorPropertiesInternal.from(sensor.impl.getSensorProperties(opPackageName));
                sensors.add(prop);
            }
            return sensors;
        }

        public void onReadyForAuthentication(final long requestId, final int cookie) {
            super.onReadyForAuthentication_enforcePermission();
            com.android.server.biometrics.BiometricService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$BiometricServiceWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReadyForAuthentication$0(requestId, cookie);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReadyForAuthentication$0(long requestId, int cookie) {
            com.android.server.biometrics.BiometricService.this.handleOnReadyForAuthentication(requestId, cookie);
        }

        public long authenticate(final android.os.IBinder token, final long operationId, final int userId, final android.hardware.biometrics.IBiometricServiceReceiver receiver, final java.lang.String opPackageName, final android.hardware.biometrics.PromptInfo promptInfo) {
            super.authenticate_enforcePermission();
            if (token == null || receiver == null || opPackageName == null || promptInfo == null) {
                android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Unable to authenticate, one or more null arguments");
                return -1L;
            }
            if (!com.android.server.biometrics.Utils.isValidAuthenticatorConfig(promptInfo)) {
                throw new java.lang.SecurityException("Invalid authenticator configuration");
            }
            com.android.server.biometrics.Utils.combineAuthenticatorBundles(promptInfo);
            final long requestId = ((java.lang.Long) com.android.server.biometrics.BiometricService.this.mRequestCounter.get()).longValue();
            com.android.server.biometrics.BiometricService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$BiometricServiceWrapper$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$authenticate$1(token, requestId, operationId, userId, receiver, opPackageName, promptInfo);
                }
            });
            return requestId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$authenticate$1(android.os.IBinder token, long requestId, long operationId, int userId, android.hardware.biometrics.IBiometricServiceReceiver receiver, java.lang.String opPackageName, android.hardware.biometrics.PromptInfo promptInfo) {
            com.android.server.biometrics.BiometricService.this.handleAuthenticate(token, requestId, operationId, userId, receiver, opPackageName, promptInfo);
        }

        public void cancelAuthentication(android.os.IBinder token, java.lang.String opPackageName, final long requestId) {
            super.cancelAuthentication_enforcePermission();
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = token;
            args.arg2 = opPackageName;
            args.arg3 = java.lang.Long.valueOf(requestId);
            android.util.Slog.i(com.android.server.biometrics.BiometricService.TAG, "cancelAuthentication the caller is " + opPackageName);
            com.android.server.biometrics.BiometricService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$BiometricServiceWrapper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$cancelAuthentication$2(requestId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cancelAuthentication$2(long requestId) {
            com.android.server.biometrics.BiometricService.this.handleCancelAuthentication(requestId);
        }

        public int canAuthenticate(java.lang.String opPackageName, int userId, int callingUserId, int authenticators) {
            super.canAuthenticate_enforcePermission();
            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "canAuthenticate: User=" + userId + ", Caller=" + callingUserId + ", Authenticators=" + authenticators + ", opPackageName=" + opPackageName);
            if (!com.android.server.biometrics.Utils.isValidAuthenticatorConfig(authenticators)) {
                throw new java.lang.SecurityException("Invalid authenticator configuration");
            }
            try {
                com.android.server.biometrics.PreAuthInfo preAuthInfo = com.android.server.biometrics.BiometricService.this.createPreAuthInfo(opPackageName, userId, authenticators);
                return preAuthInfo.getCanAuthenticateResult();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote exception", e);
                return 1;
            }
        }

        public long getLastAuthenticationTime(int userId, int authenticators) {
            super.getLastAuthenticationTime_enforcePermission();
            if (!com.android.internal.hidden_from_bootclasspath.android.hardware.biometrics.Flags.lastAuthenticationTime()) {
                throw new java.lang.UnsupportedOperationException();
            }
            com.android.server.utils.Slogf.d(com.android.server.biometrics.BiometricService.TAG, "getLastAuthenticationTime(userId=%d, authenticators=0x%x)", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(authenticators));
            try {
                long secureUserId = com.android.server.biometrics.BiometricService.this.mGateKeeper.getSecureUserId(userId);
                if (secureUserId == 0) {
                    com.android.server.utils.Slogf.w(com.android.server.biometrics.BiometricService.TAG, "No secure user id for " + userId);
                    return -1L;
                }
                java.util.ArrayList<java.lang.Integer> hardwareAuthenticators = new java.util.ArrayList<>(2);
                if ((32768 & authenticators) != 0) {
                    hardwareAuthenticators.add(1);
                }
                if ((authenticators & 15) != 0) {
                    hardwareAuthenticators.add(2);
                }
                if (hardwareAuthenticators.isEmpty()) {
                    throw new java.lang.IllegalArgumentException("authenticators must not be empty");
                }
                int[] authTypesArray = hardwareAuthenticators.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
                return com.android.server.biometrics.BiometricService.this.mKeyStoreAuthorization.getLastAuthTime(secureUserId, authTypesArray);
            } catch (android.os.RemoteException e) {
                com.android.server.utils.Slogf.w(com.android.server.biometrics.BiometricService.TAG, "Failed to get secure user id for " + userId, e);
                return -1L;
            }
        }

        public boolean hasEnrolledBiometrics(int userId, java.lang.String opPackageName) {
            super.hasEnrolledBiometrics_enforcePermission();
            try {
                for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                    if (sensor.impl.hasEnrolledTemplates(userId, opPackageName)) {
                        return true;
                    }
                }
                return false;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote exception", e);
                return false;
            }
        }

        public synchronized void registerAuthenticator(int id, int modality, int strength, android.hardware.biometrics.IBiometricAuthenticator authenticator) {
            super.registerAuthenticator_enforcePermission();
            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "Registering ID: " + id + " Modality: " + modality + " Strength: " + strength);
            if (authenticator == null) {
                throw new java.lang.IllegalArgumentException("Authenticator must not be null. Did you forget to modify the core/res/res/values/xml overlay for config_biometric_sensors?");
            }
            if (strength != 15 && strength != 255 && strength != 4095) {
                throw new java.lang.IllegalStateException("Unsupported strength");
            }
            for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                if (sensor.id == id) {
                    throw new java.lang.IllegalStateException("Cannot register duplicate authenticator");
                }
            }
            com.android.server.biometrics.BiometricService.this.mSensors.add(new com.android.server.biometrics.BiometricSensor(com.android.server.biometrics.BiometricService.this.getContext(), id, modality, strength, authenticator) { // from class: com.android.server.biometrics.BiometricService.BiometricServiceWrapper.1
                @Override // com.android.server.biometrics.BiometricSensor
                boolean confirmationAlwaysRequired(int userId) {
                    return com.android.server.biometrics.BiometricService.this.mSettingObserver.getConfirmationAlwaysRequired(this.modality, userId);
                }

                @Override // com.android.server.biometrics.BiometricSensor
                boolean confirmationSupported() {
                    return com.android.server.biometrics.Utils.isConfirmationSupported(this.modality);
                }
            });
            com.android.server.biometrics.BiometricService.this.mBiometricStrengthController.updateStrengths();
        }

        public void registerEnabledOnKeyguardCallback(android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback callback) {
            super.registerEnabledOnKeyguardCallback_enforcePermission();
            com.android.server.biometrics.BiometricService.this.mEnabledOnKeyguardCallbacks.add(com.android.server.biometrics.BiometricService.this.new EnabledOnKeyguardCallback(callback));
            java.util.List<android.content.pm.UserInfo> aliveUsers = com.android.server.biometrics.BiometricService.this.mUserManager.getAliveUsers();
            try {
                for (android.content.pm.UserInfo userInfo : aliveUsers) {
                    int userId = userInfo.id;
                    callback.onChanged(com.android.server.biometrics.BiometricService.this.mSettingObserver.getEnabledOnKeyguard(userId), userId);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.biometrics.BiometricService.TAG, "Remote exception", e);
            }
        }

        public void invalidateAuthenticatorIds(int userId, int fromSensorId, android.hardware.biometrics.IInvalidationCallback callback) {
            super.invalidateAuthenticatorIds_enforcePermission();
            com.android.server.biometrics.BiometricService.InvalidationTracker.start(com.android.server.biometrics.BiometricService.this.getContext(), com.android.server.biometrics.BiometricService.this.mSensors, userId, fromSensorId, callback);
        }

        public long[] getAuthenticatorIds(int callingUserId) {
            boolean hasEnrollments;
            long authenticatorId;
            super.getAuthenticatorIds_enforcePermission();
            java.util.List<java.lang.Long> authenticatorIds = new java.util.ArrayList<>();
            for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                try {
                    hasEnrollments = sensor.impl.hasEnrolledTemplates(callingUserId, com.android.server.biometrics.BiometricService.this.getContext().getOpPackageName());
                    authenticatorId = sensor.impl.getAuthenticatorId(callingUserId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "RemoteException", e);
                }
                if (!hasEnrollments || !com.android.server.biometrics.Utils.isAtLeastStrength(sensor.getCurrentStrength(), 15)) {
                    android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "Sensor " + sensor + ", sensorId " + sensor.id + ", hasEnrollments: " + hasEnrollments + " cannot participate in Keystore operations");
                } else {
                    authenticatorIds.add(java.lang.Long.valueOf(authenticatorId));
                }
            }
            long[] result = new long[authenticatorIds.size()];
            for (int i = 0; i < authenticatorIds.size(); i++) {
                result[i] = authenticatorIds.get(i).longValue();
            }
            return result;
        }

        public void resetLockoutTimeBound(android.os.IBinder token, java.lang.String opPackageName, int fromSensorId, int userId, byte[] hardwareAuthToken) {
            super.resetLockoutTimeBound_enforcePermission();
            if (!com.android.server.biometrics.Utils.isAtLeastStrength(com.android.server.biometrics.BiometricService.this.getSensorForId(fromSensorId).getCurrentStrength(), 15)) {
                android.util.Slog.w(com.android.server.biometrics.BiometricService.TAG, "Sensor: " + fromSensorId + " is does not meet the required strength to request resetLockout");
                return;
            }
            for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                if (sensor.id == fromSensorId) {
                    com.android.server.biometrics.BiometricService.this.mBiometricServiceExt.resetLockoutTimeBound(com.android.server.biometrics.BiometricService.this.mBiometricContext, sensor.modality, userId);
                } else {
                    try {
                        android.hardware.biometrics.SensorPropertiesInternal props = sensor.impl.getSensorProperties(com.android.server.biometrics.BiometricService.this.getContext().getOpPackageName());
                        boolean supportsChallengelessHat = props.resetLockoutRequiresHardwareAuthToken && !props.resetLockoutRequiresChallenge;
                        boolean doesNotRequireHat = true ^ props.resetLockoutRequiresHardwareAuthToken;
                        if (supportsChallengelessHat || doesNotRequireHat) {
                            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "resetLockout from: " + fromSensorId + ", for: " + sensor.id + ", userId: " + userId);
                            sensor.impl.resetLockout(token, opPackageName, userId, hardwareAuthToken);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote exception", e);
                    }
                }
            }
        }

        public void resetLockout(final int userId, byte[] hardwareAuthToken) {
            super.resetLockout_enforcePermission();
            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "resetLockout(userId=" + userId + ", hat=" + (hardwareAuthToken == null ? "null " : "present") + ")");
            com.android.server.biometrics.BiometricService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$BiometricServiceWrapper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$resetLockout$3(userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resetLockout$3(int userId) {
            com.android.server.biometrics.BiometricService.this.mBiometricContext.getAuthSessionCoordinator().resetLockoutFor(userId, 15, -1L);
        }

        public int getCurrentStrength(int sensorId) {
            super.getCurrentStrength_enforcePermission();
            for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                if (sensor.id == sensorId) {
                    return sensor.getCurrentStrength();
                }
            }
            android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Unknown sensorId: " + sensorId);
            return 0;
        }

        public int getCurrentModality(java.lang.String opPackageName, int userId, int callingUserId, int authenticators) {
            super.getCurrentModality_enforcePermission();
            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "getCurrentModality: User=" + userId + ", Caller=" + callingUserId + ", Authenticators=" + authenticators);
            if (!com.android.server.biometrics.Utils.isValidAuthenticatorConfig(authenticators)) {
                throw new java.lang.SecurityException("Invalid authenticator configuration");
            }
            try {
                com.android.server.biometrics.PreAuthInfo preAuthInfo = com.android.server.biometrics.BiometricService.this.createPreAuthInfo(opPackageName, userId, authenticators);
                return ((java.lang.Integer) preAuthInfo.getPreAuthenticateStatus().first).intValue();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.biometrics.BiometricService.TAG, "Remote exception", e);
                return 0;
            }
        }

        public int getSupportedModalities(int authenticators) {
            int modality;
            super.getSupportedModalities_enforcePermission();
            android.util.Slog.d(com.android.server.biometrics.BiometricService.TAG, "getSupportedModalities: Authenticators=" + authenticators);
            if (!com.android.server.biometrics.Utils.isValidAuthenticatorConfig(authenticators)) {
                throw new java.lang.SecurityException("Invalid authenticator configuration");
            }
            if (com.android.server.biometrics.Utils.isCredentialRequested(authenticators)) {
                modality = 1;
            } else {
                modality = 0;
            }
            if (com.android.server.biometrics.Utils.isBiometricRequested(authenticators)) {
                int requestedStrength = com.android.server.biometrics.Utils.getPublicBiometricStrength(authenticators);
                for (com.android.server.biometrics.BiometricSensor sensor : com.android.server.biometrics.BiometricService.this.mSensors) {
                    int sensorStrength = sensor.getCurrentStrength();
                    if (com.android.server.biometrics.Utils.isAtLeastStrength(sensorStrength, requestedStrength)) {
                        modality |= sensor.modality;
                    }
                }
            }
            return modality;
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x008b A[Catch: all -> 0x0095, RemoteException -> 0x0097, TRY_LEAVE, TryCatch #1 {RemoteException -> 0x0097, blocks: (B:6:0x0013, B:8:0x0016, B:10:0x0021, B:12:0x0025, B:16:0x0031, B:18:0x0054, B:19:0x005c, B:20:0x006c, B:22:0x0072, B:23:0x0087, B:24:0x008b), top: B:35:0x0013, outer: #0 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        protected void dump(java.io.FileDescriptor r11, java.io.PrintWriter r12, java.lang.String[] r13) {
            /*
                r10 = this;
                com.android.server.biometrics.BiometricService r0 = com.android.server.biometrics.BiometricService.this
                android.content.Context r0 = r0.getContext()
                java.lang.String r1 = "BiometricService"
                boolean r0 = com.android.internal.util.DumpUtils.checkDumpPermission(r0, r1, r12)
                if (r0 != 0) goto Lf
                return
            Lf:
                long r2 = android.os.Binder.clearCallingIdentity()
                int r0 = r13.length     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                if (r0 <= 0) goto L8b
                java.lang.String r0 = "--proto"
                r4 = 0
                r5 = r13[r4]     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                boolean r0 = r0.equals(r5)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                if (r0 == 0) goto L8b
                int r0 = r13.length     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                r5 = 1
                if (r0 <= r5) goto L30
                java.lang.String r0 = "--clear-scheduler-buffer"
                r6 = r13[r5]     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                boolean r0 = r0.equals(r6)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                if (r0 == 0) goto L30
                goto L31
            L30:
                r5 = r4
            L31:
                r0 = r5
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                r5.<init>()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                java.lang.String r6 = "ClearSchedulerBuffer: "
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                java.lang.StringBuilder r5 = r5.append(r0)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                android.util.Slog.d(r1, r5)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                android.util.proto.ProtoOutputStream r5 = new android.util.proto.ProtoOutputStream     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                r5.<init>(r11)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                com.android.server.biometrics.BiometricService r6 = com.android.server.biometrics.BiometricService.this     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                com.android.server.biometrics.AuthSession r6 = r6.mAuthSession     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                if (r6 == 0) goto L5c
                com.android.server.biometrics.BiometricService r4 = com.android.server.biometrics.BiometricService.this     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                com.android.server.biometrics.AuthSession r4 = r4.mAuthSession     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                int r4 = r4.getState()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
            L5c:
                r6 = 1159641169922(0x10e00000002, double:5.729388635616E-312)
                r5.write(r6, r4)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                com.android.server.biometrics.BiometricService r4 = com.android.server.biometrics.BiometricService.this     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                java.util.ArrayList<com.android.server.biometrics.BiometricSensor> r4 = r4.mSensors     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                java.util.Iterator r4 = r4.iterator()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
            L6c:
                boolean r6 = r4.hasNext()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                if (r6 == 0) goto L87
                java.lang.Object r6 = r4.next()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                com.android.server.biometrics.BiometricSensor r6 = (com.android.server.biometrics.BiometricSensor) r6     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                android.hardware.biometrics.IBiometricAuthenticator r7 = r6.impl     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                byte[] r7 = r7.dumpSensorServiceStateProto(r0)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                r8 = 2246267895809(0x20b00000001, double:1.1098037986753E-311)
                r5.write(r8, r7)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                goto L6c
            L87:
                r5.flush()     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                goto L90
            L8b:
                com.android.server.biometrics.BiometricService r0 = com.android.server.biometrics.BiometricService.this     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
                com.android.server.biometrics.BiometricService.m2388$$Nest$mdumpInternal(r0, r12)     // Catch: java.lang.Throwable -> L95 android.os.RemoteException -> L97
            L90:
            L91:
                android.os.Binder.restoreCallingIdentity(r2)
                goto L9f
            L95:
                r0 = move-exception
                goto La0
            L97:
                r0 = move-exception
                java.lang.String r4 = "Remote exception"
                android.util.Slog.e(r1, r4, r0)     // Catch: java.lang.Throwable -> L95
                goto L91
            L9f:
                return
            La0:
                android.os.Binder.restoreCallingIdentity(r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.biometrics.BiometricService.BiometricServiceWrapper.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
        }
    }

    private void checkInternalPermission() {
        getContext().enforceCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL", "Must have USE_BIOMETRIC_INTERNAL permission");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.biometrics.PreAuthInfo createPreAuthInfo(java.lang.String opPackageName, int userId, int authenticators) throws android.os.RemoteException {
        android.hardware.biometrics.PromptInfo promptInfo = new android.hardware.biometrics.PromptInfo();
        promptInfo.setAuthenticators(authenticators);
        return com.android.server.biometrics.PreAuthInfo.create(this.mTrustManager, this.mDevicePolicyManager, this.mSettingObserver, this.mSensors, userId, promptInfo, opPackageName, false, getContext(), this.mBiometricCameraManager);
    }

    public static class Injector {
        public android.app.IActivityManager getActivityManagerService() {
            return android.app.ActivityManager.getService();
        }

        public android.security.KeyStoreAuthorization getKeyStoreAuthorization() {
            return android.security.KeyStoreAuthorization.getInstance();
        }

        public android.service.gatekeeper.IGateKeeperService getGateKeeperService() {
            return android.security.GateKeeper.getService();
        }

        public android.app.trust.ITrustManager getTrustManager() {
            return android.app.trust.ITrustManager.Stub.asInterface(android.os.ServiceManager.getService("trust"));
        }

        public com.android.internal.statusbar.IStatusBarService getStatusBarService() {
            return com.android.internal.statusbar.IStatusBarService.Stub.asInterface(android.os.ServiceManager.getService("statusbar"));
        }

        public com.android.server.biometrics.BiometricService.SettingObserver getSettingObserver(android.content.Context context, android.os.Handler handler, java.util.List<com.android.server.biometrics.BiometricService.EnabledOnKeyguardCallback> callbacks) {
            return new com.android.server.biometrics.BiometricService.SettingObserver(context, handler, callbacks);
        }

        public boolean isDebugEnabled(android.content.Context context, int userId) {
            return com.android.server.biometrics.Utils.isDebugEnabled(context, userId);
        }

        public void publishBinderService(com.android.server.biometrics.BiometricService service, android.hardware.biometrics.IBiometricService.Stub impl) {
            service.publishBinderService("biometric", impl);
        }

        public com.android.server.biometrics.BiometricStrengthController getBiometricStrengthController(com.android.server.biometrics.BiometricService service) {
            return new com.android.server.biometrics.BiometricStrengthController(service);
        }

        public java.lang.String[] getConfiguration(android.content.Context context) {
            return context.getResources().getStringArray(android.R.array.config_bg_current_drain_high_threshold_to_restricted_bucket);
        }

        public android.app.admin.DevicePolicyManager getDevicePolicyManager(android.content.Context context) {
            return (android.app.admin.DevicePolicyManager) context.getSystemService(android.app.admin.DevicePolicyManager.class);
        }

        public java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> getFingerprintSensorProperties(android.content.Context context) {
            android.hardware.fingerprint.FingerprintManager fpm;
            if (context.getPackageManager().hasSystemFeature("android.hardware.fingerprint") && (fpm = (android.hardware.fingerprint.FingerprintManager) context.getSystemService(android.hardware.fingerprint.FingerprintManager.class)) != null) {
                return fpm.getSensorPropertiesInternal();
            }
            return new java.util.ArrayList();
        }

        public java.util.function.Supplier<java.lang.Long> getRequestGenerator() {
            final java.util.concurrent.atomic.AtomicLong generator = new java.util.concurrent.atomic.AtomicLong(0L);
            return new java.util.function.Supplier() { // from class: com.android.server.biometrics.BiometricService$Injector$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return java.lang.Long.valueOf(generator.incrementAndGet());
                }
            };
        }

        public com.android.server.biometrics.log.BiometricContext getBiometricContext(android.content.Context context) {
            return com.android.server.biometrics.log.BiometricContext.getInstance(context);
        }

        public android.os.UserManager getUserManager(android.content.Context context) {
            return (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        }

        public com.android.server.biometrics.BiometricCameraManager getBiometricCameraManager(android.content.Context context) {
            return new com.android.server.biometrics.BiometricCameraManagerImpl((android.hardware.camera2.CameraManager) context.getSystemService(android.hardware.camera2.CameraManager.class), (android.hardware.SensorPrivacyManager) context.getSystemService(android.hardware.SensorPrivacyManager.class));
        }

        public com.android.server.biometrics.BiometricNotificationLogger getNotificationLogger() {
            return new com.android.server.biometrics.BiometricNotificationLogger();
        }
    }

    public BiometricService(android.content.Context context) {
        this(context, new com.android.server.biometrics.BiometricService.Injector(), com.android.server.biometrics.BiometricHandlerProvider.getInstance());
    }

    BiometricService(android.content.Context context, com.android.server.biometrics.BiometricService.Injector injector, com.android.server.biometrics.BiometricHandlerProvider biometricHandlerProvider) {
        super(context);
        this.mRandom = new java.util.Random();
        this.mSensors = new java.util.ArrayList<>();
        this.mBiometricServiceExt = (com.android.server.biometrics.IBiometricServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.IBiometricServiceExt.class).create();
        this.mInjector = injector;
        this.mHandler = biometricHandlerProvider.getBiometricCallbackHandler();
        this.mDevicePolicyManager = this.mInjector.getDevicePolicyManager(context);
        this.mImpl = new com.android.server.biometrics.BiometricService.BiometricServiceWrapper();
        this.mEnabledOnKeyguardCallbacks = new java.util.ArrayList();
        this.mSettingObserver = this.mInjector.getSettingObserver(context, this.mHandler, this.mEnabledOnKeyguardCallbacks);
        this.mRequestCounter = this.mInjector.getRequestGenerator();
        this.mBiometricContext = injector.getBiometricContext(context);
        this.mUserManager = injector.getUserManager(context);
        this.mBiometricCameraManager = injector.getBiometricCameraManager(context);
        this.mKeyStoreAuthorization = injector.getKeyStoreAuthorization();
        this.mGateKeeper = injector.getGateKeeperService();
        this.mBiometricNotificationLogger = injector.getNotificationLogger();
        try {
            injector.getActivityManagerService().registerUserSwitchObserver(new android.app.UserSwitchObserver() { // from class: com.android.server.biometrics.BiometricService.3
                public void onUserSwitchComplete(int newUserId) {
                    com.android.server.biometrics.BiometricService.this.mSettingObserver.updateContentObserver();
                    com.android.server.biometrics.BiometricService.this.mSettingObserver.notifyEnabledOnKeyguardCallbacks(newUserId);
                }
            }, com.android.server.biometrics.BiometricService.class.getName());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to register user switch observer", e);
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mStatusBarService = this.mInjector.getStatusBarService();
        this.mTrustManager = this.mInjector.getTrustManager();
        this.mInjector.publishBinderService(this, this.mImpl);
        this.mBiometricStrengthController = this.mInjector.getBiometricStrengthController(this);
        this.mBiometricStrengthController.startListening();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    com.android.server.biometrics.BiometricService.this.mBiometricNotificationLogger.registerAsSystemService(com.android.server.biometrics.BiometricService.this.getContext(), new android.content.ComponentName(com.android.server.biometrics.BiometricService.this.getContext(), (java.lang.Class<?>) com.android.server.biometrics.BiometricNotificationLogger.class), -1);
                } catch (android.os.RemoteException e) {
                }
            }
        });
    }

    private boolean isStrongBiometric(int id) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mSensors) {
            if (sensor.id == id) {
                return com.android.server.biometrics.Utils.isAtLeastStrength(sensor.getCurrentStrength(), 15);
            }
        }
        android.util.Slog.e(TAG, "Unknown sensorId: " + id);
        return false;
    }

    private com.android.server.biometrics.AuthSession getAuthSessionIfCurrent(long requestId) {
        com.android.server.biometrics.AuthSession session = this.mAuthSession;
        if (session != null && session.getRequestId() == requestId) {
            return session;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAuthenticationSucceeded(long requestId, int sensorId, byte[] token) {
        android.util.Slog.v(TAG, "handleAuthenticationSucceeded(), sensorId: " + sensorId);
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.e(TAG, "handleAuthenticationSucceeded: AuthSession is null");
        } else {
            session.onAuthenticationSucceeded(sensorId, isStrongBiometric(sensorId), token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAuthenticationRejected(long requestId, int sensorId) {
        android.util.Slog.v(TAG, "handleAuthenticationRejected()");
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleAuthenticationRejected: AuthSession is not current");
        } else {
            session.onAuthenticationRejected(sensorId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAuthenticationTimedOut(long requestId, int sensorId, int cookie, int error, int vendorCode) {
        android.util.Slog.v(TAG, "handleAuthenticationTimedOut(), sensorId: " + sensorId + ", cookie: " + cookie + ", error: " + error + ", vendorCode: " + vendorCode);
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleAuthenticationTimedOut: AuthSession is not current");
        } else {
            session.onAuthenticationTimedOut(sensorId, cookie, error, vendorCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnError(long requestId, int sensorId, int cookie, int error, int vendorCode) {
        android.util.Slog.d(TAG, "handleOnError() sensorId: " + sensorId + ", cookie: " + cookie + ", error: " + error + ", vendorCode: " + vendorCode);
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnError: AuthSession is not current");
            return;
        }
        try {
            boolean finished = session.onErrorReceived(sensorId, cookie, error, vendorCode);
            if (finished) {
                android.util.Slog.d(TAG, "handleOnError: AuthSession finished");
                this.mAuthSession = null;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnAcquired(long requestId, int sensorId, int acquiredInfo, int vendorCode) {
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "onAcquired: AuthSession is not current");
        } else {
            session.onAcquired(sensorId, acquiredInfo, vendorCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDismissed(long requestId, int reason, byte[] credentialAttestation) {
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.e(TAG, "onDismissed: " + reason + ", AuthSession is not current");
        } else {
            session.onDialogDismissed(reason, credentialAttestation);
            this.mAuthSession = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnTryAgainPressed(long requestId) {
        android.util.Slog.d(TAG, "onTryAgainPressed");
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnTryAgainPressed: AuthSession is not current");
        } else {
            session.onTryAgainPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDeviceCredentialPressed(long requestId) {
        android.util.Slog.d(TAG, "onDeviceCredentialPressed");
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnDeviceCredentialPressed: AuthSession is not current");
        } else {
            session.onDeviceCredentialPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnSystemEvent(long requestId, int event) {
        android.util.Slog.d(TAG, "onSystemEvent: " + event);
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnSystemEvent: AuthSession is not current");
        } else {
            session.onSystemEvent(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleClientDied, reason: merged with bridge method [inline-methods] */
    public void lambda$createClientDeathReceiver$0(long requestId) {
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleClientDied: AuthSession is not current");
            return;
        }
        android.util.Slog.e(TAG, "Session: " + session);
        boolean finished = session.onClientDied();
        if (finished) {
            this.mAuthSession = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDialogAnimatedIn(long requestId, boolean startFingerprintNow) {
        android.util.Slog.d(TAG, "handleOnDialogAnimatedIn");
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnDialogAnimatedIn: AuthSession is not current");
        } else {
            session.onDialogAnimatedIn(startFingerprintNow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnStartFingerprintNow(long requestId) {
        android.util.Slog.d(TAG, "handleOnStartFingerprintNow");
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnStartFingerprintNow: AuthSession is not current");
        } else {
            session.onStartFingerprint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReadyForAuthentication(long requestId, int cookie) {
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleOnReadyForAuthentication: AuthSession is not current");
        } else {
            session.onCookieReceived(cookie);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAuthenticate(final android.os.IBinder token, final long requestId, final long operationId, final int userId, final android.hardware.biometrics.IBiometricServiceReceiver receiver, final java.lang.String opPackageName, final android.hardware.biometrics.PromptInfo promptInfo) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.BiometricService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleAuthenticate$2(userId, promptInfo, opPackageName, requestId, token, operationId, receiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleAuthenticate$2(int userId, android.hardware.biometrics.PromptInfo promptInfo, java.lang.String opPackageName, long requestId, android.os.IBinder token, long operationId, android.hardware.biometrics.IBiometricServiceReceiver receiver) {
        try {
            com.android.server.biometrics.PreAuthInfo preAuthInfo = com.android.server.biometrics.PreAuthInfo.create(this.mTrustManager, this.mDevicePolicyManager, this.mSettingObserver, this.mSensors, userId, promptInfo, opPackageName, promptInfo.isDisallowBiometricsIfPolicyExists(), getContext(), this.mBiometricCameraManager);
            if (promptInfo.isUseDefaultTitle() && android.text.TextUtils.isEmpty(promptInfo.getTitle())) {
                promptInfo.setTitle(getContext().getString(android.R.string.biometric_error_user_canceled));
            }
            int eligible = preAuthInfo.getEligibleModalities();
            boolean z = true;
            boolean hasEligibleFingerprintSensor = (eligible & 2) == 2;
            if ((eligible & 8) != 8) {
                z = false;
            }
            boolean hasEligibleFaceSensor = z;
            if (promptInfo.isUseDefaultSubtitle()) {
                if (hasEligibleFingerprintSensor && hasEligibleFaceSensor) {
                    promptInfo.setSubtitle(getContext().getString(android.R.string.biometric_error_hw_unavailable));
                } else if (hasEligibleFingerprintSensor) {
                    promptInfo.setSubtitle(getContext().getString(android.R.string.fingerprint_dialog_use_fingerprint_instead));
                } else if (hasEligibleFaceSensor) {
                    promptInfo.setSubtitle(getContext().getString(android.R.string.face_error_hw_not_available));
                } else {
                    promptInfo.setSubtitle(getContext().getString(android.R.string.roamingText6));
                }
            }
            android.util.Pair<java.lang.Integer, java.lang.Integer> preAuthStatus = preAuthInfo.getPreAuthenticateStatus();
            android.util.Slog.d(TAG, "handleAuthenticate: modality(" + preAuthStatus.first + "), status(" + preAuthStatus.second + "), preAuthInfo: " + preAuthInfo + " requestId: " + requestId + " promptInfo.isIgnoreEnrollmentState: " + promptInfo.isIgnoreEnrollmentState());
            if (((java.lang.Integer) preAuthStatus.second).intValue() == 0) {
                if (preAuthInfo.credentialRequested && preAuthInfo.credentialAvailable && preAuthInfo.eligibleSensors.isEmpty()) {
                    promptInfo.setAuthenticators(32768);
                }
                authenticateInternal(token, requestId, operationId, userId, receiver, opPackageName, promptInfo, preAuthInfo);
                return;
            }
            try {
                receiver.onError(((java.lang.Integer) preAuthStatus.first).intValue(), ((java.lang.Integer) preAuthStatus.second).intValue(), 0);
            } catch (android.os.RemoteException e) {
                e = e;
                android.util.Slog.e(TAG, "Remote exception", e);
            }
        } catch (android.os.RemoteException e2) {
            e = e2;
        }
    }

    private void authenticateInternal(android.os.IBinder token, long requestId, long operationId, int userId, android.hardware.biometrics.IBiometricServiceReceiver receiver, java.lang.String opPackageName, android.hardware.biometrics.PromptInfo promptInfo, com.android.server.biometrics.PreAuthInfo preAuthInfo) {
        android.util.Slog.d(TAG, "Creating authSession with authRequest: " + preAuthInfo);
        if (this.mAuthSession != null) {
            android.util.Slog.w(TAG, "Existing AuthSession: " + this.mAuthSession);
            this.mAuthSession.onCancelAuthSession(true);
            this.mAuthSession = null;
        }
        boolean debugEnabled = this.mInjector.isDebugEnabled(getContext(), userId);
        this.mAuthSession = new com.android.server.biometrics.AuthSession(getContext(), this.mBiometricContext, this.mStatusBarService, createSysuiReceiver(requestId), this.mKeyStoreAuthorization, this.mRandom, createClientDeathReceiver(requestId), preAuthInfo, token, requestId, operationId, userId, createBiometricSensorReceiver(requestId), receiver, opPackageName, promptInfo, debugEnabled, this.mInjector.getFingerprintSensorProperties(getContext()));
        try {
            this.mAuthSession.goToInitialState();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCancelAuthentication(long requestId) {
        com.android.server.biometrics.AuthSession session = getAuthSessionIfCurrent(requestId);
        if (session == null) {
            android.util.Slog.w(TAG, "handleCancelAuthentication: AuthSession is not current");
            return;
        }
        boolean finished = session.onCancelAuthSession(false);
        if (finished) {
            android.util.Slog.d(TAG, "handleCancelAuthentication: AuthSession finished");
            this.mAuthSession = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.biometrics.BiometricSensor getSensorForId(int sensorId) {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mSensors) {
            if (sensor.id == sensorId) {
                return sensor;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(java.io.PrintWriter pw) {
        pw.println("Legacy Settings: " + this.mSettingObserver.mUseLegacyFaceOnlySettings);
        pw.println();
        pw.println("Sensors:");
        for (com.android.server.biometrics.BiometricSensor sensor : this.mSensors) {
            pw.println(" " + sensor);
        }
        pw.println();
        pw.println("CurrentSession: " + this.mAuthSession);
        pw.println();
    }
}
