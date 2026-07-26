package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public class HidlToAidlSensorAdapter extends com.android.server.biometrics.sensors.fingerprint.aidl.Sensor implements android.os.IHwBinder.DeathRecipient {
    private static final java.lang.String TAG = "HidlToAidlSensorAdapter";
    private final com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback mAidlResponseHandlerCallback;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private int mCurrentUserId;
    private android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint mDaemon;
    private final java.lang.Runnable mInternalCleanupRunnable;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl mLockoutTracker;
    private com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession mSession;
    private final com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> mUserStartedCallback;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int newUserId, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession newUser, int halInterfaceVersion) {
        if (this.mCurrentUserId != newUserId) {
            handleUserChanged(newUserId);
        }
    }

    public HidlToAidlSensorAdapter(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.biometrics.fingerprint.SensorProps prop, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresHardwareAuthToken, java.lang.Runnable internalCleanupRunnable) {
        this(provider, context, handler, prop, lockoutResetDispatcher, biometricContext, resetLockoutRequiresHardwareAuthToken, internalCleanupRunnable, new com.android.server.biometrics.sensors.AuthSessionCoordinator(), null, null);
    }

    HidlToAidlSensorAdapter(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.biometrics.fingerprint.SensorProps prop, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresHardwareAuthToken, java.lang.Runnable internalCleanupRunnable, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint daemon, com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback aidlResponseHandlerCallback) {
        com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback aidlResponseHandlerCallback2;
        super(provider, context, handler, getFingerprintSensorPropertiesInternal(prop, new java.util.ArrayList(), resetLockoutRequiresHardwareAuthToken), biometricContext, null);
        this.mCurrentUserId = -10000;
        this.mUserStartedCallback = new com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
            public final void onUserStarted(int i, java.lang.Object obj, int i2) {
                this.f$0.lambda$new$0(i, (com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession) obj, i2);
            }
        };
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mInternalCleanupRunnable = internalCleanupRunnable;
        this.mAuthSessionCoordinator = authSessionCoordinator;
        this.mDaemon = daemon;
        if (aidlResponseHandlerCallback == null) {
            aidlResponseHandlerCallback2 = new com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.1
                @Override // com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onEnrollSuccess() {
                    com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getScheduler().scheduleClientMonitor(com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getFingerprintUpdateActiveUserClient(com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.mCurrentUserId, true));
                }

                @Override // com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onHardwareUnavailable() {
                    com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.mDaemon = null;
                    com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.mSession = null;
                    com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.mCurrentUserId = -10000;
                }
            };
        } else {
            aidlResponseHandlerCallback2 = aidlResponseHandlerCallback;
        }
        this.mAidlResponseHandlerCallback = aidlResponseHandlerCallback2;
    }

    public void serviceDied(long cookie) {
        android.util.Slog.d(TAG, "Fingerprint HAL died.");
        this.mSession = null;
        this.mDaemon = null;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.aidl.Sensor
    public int getLockoutModeForUser(int userId) {
        return this.mLockoutTracker.getLockoutModeForUser(userId);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.aidl.Sensor
    public void init(com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher) {
        setLazySession(new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.getSession();
            }
        });
        setScheduler(new com.android.server.biometrics.sensors.BiometricScheduler(getHandler(), com.android.server.biometrics.sensors.BiometricScheduler.sensorTypeFromFingerprintProperties(getSensorProperties()), gestureAvailabilityDispatcher, (java.util.function.Supplier<java.lang.Integer>) new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$init$1();
            }
        }, getUserSwitchProvider()));
        this.mLockoutTracker = new com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl(getContext(), new com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutResetCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda7
            @Override // com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutResetCallback
            public final void onLockoutReset(int i) {
                this.f$0.lambda$init$2(i);
            }
        }, getHandler());
        mFingerprintSensorExt.setProvider(getAidlResponseHandler(), getProvider());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$init$1() {
        return java.lang.Integer.valueOf(this.mCurrentUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2(int userId) {
        this.mLockoutResetDispatcher.notifyLockoutResetCallbacks(getSensorProperties().sensorId);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.aidl.Sensor
    protected com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession getSessionForUser(int userId) {
        if (this.mSession != null && this.mSession.getUserId() == userId) {
            return this.mSession;
        }
        return null;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.aidl.Sensor
    protected boolean isHardwareDetected(java.lang.String halInstance) {
        return getIBiometricsFingerprint() != null;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.aidl.Sensor
    protected com.android.server.biometrics.sensors.LockoutTracker getLockoutTracker(boolean forAuth) {
        return this.mLockoutTracker;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession getSession() {
        if (this.mSession != null && this.mDaemon != null) {
            return this.mSession;
        }
        com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession aidlSession = new com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession(new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.getIBiometricsFingerprint();
            }
        }, this.mCurrentUserId, getAidlResponseHandler());
        this.mSession = aidlSession;
        return aidlSession;
    }

    private com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler getAidlResponseHandler() {
        return new com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler(getContext(), getScheduler(), getSensorProperties().sensorId, this.mCurrentUserId, this.mLockoutTracker, this.mLockoutResetDispatcher, this.mAuthSessionCoordinator, this.mAidlResponseHandlerCallback);
    }

    android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint getIBiometricsFingerprint() {
        if (getProvider().getTestHalEnabled()) {
            com.android.server.biometrics.sensors.fingerprint.hidl.TestHal testHal = new com.android.server.biometrics.sensors.fingerprint.hidl.TestHal(getContext(), getSensorProperties().sensorId);
            testHal.setNotify(new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlCallbackConverter(getAidlResponseHandler()));
            return testHal;
        }
        if (this.mDaemon != null) {
            return this.mDaemon;
        }
        try {
            this.mDaemon = android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.getService();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get fingerprint HAL", e);
        } catch (java.util.NoSuchElementException e2) {
            android.util.Slog.w(TAG, "NoSuchElementException", e2);
        }
        if (this.mDaemon == null) {
            android.util.Slog.w(TAG, "Fingerprint HAL not available");
            return null;
        }
        this.mDaemon.asBinder().linkToDeath(this, 0L);
        scheduleLoadAuthenticatorIds();
        this.mInternalCleanupRunnable.run();
        return this.mDaemon;
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$2, reason: invalid class name */
    class AnonymousClass2 implements com.android.server.biometrics.sensors.UserSwitchProvider<android.hardware.biometrics.fingerprint.ISession, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
        AnonymousClass2() {
        }

        @Override // com.android.server.biometrics.sensors.UserSwitchProvider
        public com.android.server.biometrics.sensors.StopUserClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> getStopUserClient(int userId) {
            android.content.Context context = com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getContext();
            final com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter hidlToAidlSensorAdapter = com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this;
            return new com.android.server.biometrics.sensors.StopUserClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession>(context, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$2$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return hidlToAidlSensorAdapter.getSession();
                }
            }, null, userId, com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getSensorProperties().sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getContext()), com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getBiometricContext(), new com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$2$$ExternalSyntheticLambda1
                @Override // com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback
                public final void onUserStopped() {
                    this.f$0.lambda$getStopUserClient$0();
                }
            }) { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.2.1
                @Override // com.android.server.biometrics.sensors.BaseClientMonitor
                public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
                    super.start(callback);
                    startHalOperation();
                }

                @Override // com.android.server.biometrics.sensors.HalClientMonitor
                protected void startHalOperation() {
                    onUserStopped();
                }

                @Override // com.android.server.biometrics.sensors.HalClientMonitor
                public void unableToStart() {
                    getCallback().onClientFinished(this, false);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStopUserClient$0() {
            com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.mCurrentUserId = -10000;
            com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.mSession = null;
        }

        @Override // com.android.server.biometrics.sensors.UserSwitchProvider
        public com.android.server.biometrics.sensors.StartUserClient<android.hardware.biometrics.fingerprint.ISession, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> getStartUserClient(int newUserId) {
            return com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.this.getFingerprintUpdateActiveUserClient(newUserId, false);
        }
    }

    private com.android.server.biometrics.sensors.UserSwitchProvider<android.hardware.biometrics.fingerprint.ISession, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> getUserSwitchProvider() {
        return new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter.AnonymousClass2();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.biometrics.sensors.fingerprint.hidl.FingerprintUpdateActiveUserClient getFingerprintUpdateActiveUserClient(int newUserId, boolean forceUpdateAuthenticatorIds) {
        return new com.android.server.biometrics.sensors.fingerprint.hidl.FingerprintUpdateActiveUserClient(getContext(), new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getFingerprintUpdateActiveUserClient$3();
            }
        }, newUserId, TAG, getSensorProperties().sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(getContext()), getBiometricContext(), new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getFingerprintUpdateActiveUserClient$4();
            }
        }, !com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(getSensorProperties().sensorId).getBiometricsForUser(getContext(), newUserId).isEmpty(), getAuthenticatorIds(), forceUpdateAuthenticatorIds, this.mUserStartedCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.hardware.biometrics.fingerprint.ISession lambda$getFingerprintUpdateActiveUserClient$3() {
        return getSession().getSession();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getFingerprintUpdateActiveUserClient$4() {
        return java.lang.Integer.valueOf(this.mCurrentUserId);
    }

    private void scheduleLoadAuthenticatorIds() {
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleLoadAuthenticatorIds$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLoadAuthenticatorIds$5() {
        for (android.content.pm.UserInfo user : android.os.UserManager.get(getContext()).getAliveUsers()) {
            int targetUserId = user.id;
            if (!getAuthenticatorIds().containsKey(java.lang.Integer.valueOf(targetUserId))) {
                getScheduler().scheduleClientMonitor(getFingerprintUpdateActiveUserClient(targetUserId, true));
            }
        }
    }

    void handleUserChanged(int newUserId) {
        android.util.Slog.d(TAG, "User changed. Current user for fingerprint sensor is " + newUserId);
        this.mSession = null;
        this.mCurrentUserId = newUserId;
    }
}
