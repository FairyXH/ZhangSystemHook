package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: loaded from: classes.dex */
public class HidlToAidlSensorAdapter extends com.android.server.biometrics.sensors.face.aidl.Sensor implements android.os.IHwBinder.DeathRecipient {
    private static final java.lang.String TAG = "HidlToAidlSensorAdapter";
    private final com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback mAidlResponseHandlerCallback;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private int mCurrentUserId;
    private android.hardware.biometrics.face.V1_0.IBiometricsFace mDaemon;
    private final com.android.server.biometrics.sensors.face.aidl.FaceProvider mFaceProvider;
    private final java.lang.Runnable mInternalCleanupAndGetFeatureRunnable;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private com.android.server.biometrics.sensors.face.LockoutHalImpl mLockoutTracker;
    private com.android.server.biometrics.sensors.face.aidl.AidlSession mSession;
    private final com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<com.android.server.biometrics.sensors.face.aidl.AidlSession> mUserStartedCallback;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int newUserId, com.android.server.biometrics.sensors.face.aidl.AidlSession newUser, int halInterfaceVersion) {
        if (newUserId != this.mCurrentUserId) {
            handleUserChanged(newUserId);
        }
    }

    public HidlToAidlSensorAdapter(com.android.server.biometrics.sensors.face.aidl.FaceProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.biometrics.face.SensorProps prop, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresChallenge, java.lang.Runnable internalCleanupAndGetFeatureRunnable) {
        this(provider, context, handler, prop, lockoutResetDispatcher, biometricContext, resetLockoutRequiresChallenge, internalCleanupAndGetFeatureRunnable, new com.android.server.biometrics.sensors.AuthSessionCoordinator(), null, null);
    }

    HidlToAidlSensorAdapter(com.android.server.biometrics.sensors.face.aidl.FaceProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.biometrics.face.SensorProps prop, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresChallenge, java.lang.Runnable internalCleanupAndGetFeatureRunnable, com.android.server.biometrics.sensors.AuthSessionCoordinator authSessionCoordinator, android.hardware.biometrics.face.V1_0.IBiometricsFace daemon, com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback aidlResponseHandlerCallback) {
        com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback aidlResponseHandlerCallback2;
        super(provider, context, handler, prop, biometricContext, resetLockoutRequiresChallenge);
        this.mCurrentUserId = -10000;
        this.mUserStartedCallback = new com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda4
            @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
            public final void onUserStarted(int i, java.lang.Object obj, int i2) {
                this.f$0.lambda$new$0(i, (com.android.server.biometrics.sensors.face.aidl.AidlSession) obj, i2);
            }
        };
        this.mInternalCleanupAndGetFeatureRunnable = internalCleanupAndGetFeatureRunnable;
        this.mFaceProvider = provider;
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mAuthSessionCoordinator = authSessionCoordinator;
        this.mDaemon = daemon;
        if (aidlResponseHandlerCallback == null) {
            aidlResponseHandlerCallback2 = new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback() { // from class: com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter.1
                @Override // com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onEnrollSuccess() {
                    com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter.this.scheduleFaceUpdateActiveUserClient(com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter.this.mCurrentUserId);
                }

                @Override // com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onHardwareUnavailable() {
                    com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter.this.mDaemon = null;
                    com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter.this.mCurrentUserId = -10000;
                }
            };
        } else {
            aidlResponseHandlerCallback2 = aidlResponseHandlerCallback;
        }
        this.mAidlResponseHandlerCallback = aidlResponseHandlerCallback2;
    }

    @Override // com.android.server.biometrics.sensors.face.aidl.Sensor
    public void scheduleFaceUpdateActiveUserClient(int userId) {
        getScheduler().scheduleClientMonitor(getFaceUpdateActiveUserClient(userId));
    }

    public void serviceDied(long cookie) {
        android.util.Slog.d(TAG, "Face HAL died.");
        this.mDaemon = null;
    }

    @Override // com.android.server.biometrics.sensors.face.aidl.Sensor
    public boolean isHardwareDetected(java.lang.String halInstanceName) {
        return getIBiometricsFace() != null;
    }

    @Override // com.android.server.biometrics.sensors.face.aidl.Sensor
    public int getLockoutModeForUser(int userId) {
        return this.mLockoutTracker.getLockoutModeForUser(userId);
    }

    @Override // com.android.server.biometrics.sensors.face.aidl.Sensor
    public void init(com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.face.aidl.FaceProvider provider) {
        setScheduler(new com.android.server.biometrics.sensors.BiometricScheduler(getHandler(), 1, (com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher) null, (java.util.function.Supplier<java.lang.Integer>) new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$init$1();
            }
        }, (com.android.server.biometrics.sensors.UserSwitchProvider) null));
        setLazySession(new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.getSession();
            }
        });
        this.mLockoutTracker = new com.android.server.biometrics.sensors.face.LockoutHalImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$init$1() {
        return java.lang.Integer.valueOf(this.mCurrentUserId);
    }

    @Override // com.android.server.biometrics.sensors.face.aidl.Sensor
    protected com.android.server.biometrics.sensors.face.aidl.AidlSession getSessionForUser(int userId) {
        if (this.mSession != null && this.mSession.getUserId() == userId) {
            return this.mSession;
        }
        return null;
    }

    @Override // com.android.server.biometrics.sensors.face.aidl.Sensor
    protected com.android.server.biometrics.sensors.LockoutTracker getLockoutTracker(boolean forAuth) {
        return this.mLockoutTracker;
    }

    com.android.server.biometrics.sensors.face.aidl.AidlSession getSession() {
        if (this.mDaemon != null && this.mSession != null) {
            return this.mSession;
        }
        com.android.server.biometrics.sensors.face.aidl.AidlSession aidlSession = new com.android.server.biometrics.sensors.face.aidl.AidlSession(getContext(), new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda3(this), this.mCurrentUserId, getAidlResponseHandler());
        this.mSession = aidlSession;
        return aidlSession;
    }

    private com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler getAidlResponseHandler() {
        return new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler(getContext(), getScheduler(), getSensorProperties().sensorId, this.mCurrentUserId, this.mLockoutTracker, this.mLockoutResetDispatcher, this.mAuthSessionCoordinator, this.mAidlResponseHandlerCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.biometrics.face.V1_0.IBiometricsFace getIBiometricsFace() {
        if (this.mFaceProvider.getTestHalEnabled()) {
            com.android.server.biometrics.sensors.face.hidl.TestHal testHal = new com.android.server.biometrics.sensors.face.hidl.TestHal(getContext(), getSensorProperties().sensorId);
            testHal.setCallback(new com.android.server.biometrics.sensors.face.hidl.HidlToAidlCallbackConverter(getAidlResponseHandler()));
            return testHal;
        }
        if (this.mDaemon != null) {
            return this.mDaemon;
        }
        android.util.Slog.d(TAG, "Face daemon was null, reconnecting, current operation: " + getScheduler().getCurrentClient());
        try {
            this.mDaemon = android.hardware.biometrics.face.V1_0.IBiometricsFace.getService();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get face HAL", e);
        } catch (java.util.NoSuchElementException e2) {
            android.util.Slog.w(TAG, "NoSuchElementException", e2);
        }
        if (this.mDaemon == null) {
            android.util.Slog.w(TAG, "Face HAL not available");
            return null;
        }
        this.mDaemon.asBinder().linkToDeath(this, 0L);
        scheduleLoadAuthenticatorIds();
        this.mInternalCleanupAndGetFeatureRunnable.run();
        return this.mDaemon;
    }

    void handleUserChanged(int newUserId) {
        android.util.Slog.d(TAG, "User changed. Current user for face sensor is " + newUserId);
        this.mSession = null;
        this.mCurrentUserId = newUserId;
    }

    private void scheduleLoadAuthenticatorIds() {
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleLoadAuthenticatorIds$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLoadAuthenticatorIds$2() {
        for (android.content.pm.UserInfo user : android.os.UserManager.get(getContext()).getAliveUsers()) {
            int targetUserId = user.id;
            if (!getAuthenticatorIds().containsKey(java.lang.Integer.valueOf(targetUserId))) {
                scheduleFaceUpdateActiveUserClient(targetUserId);
            }
        }
    }

    private com.android.server.biometrics.sensors.face.hidl.FaceUpdateActiveUserClient getFaceUpdateActiveUserClient(int userId) {
        return new com.android.server.biometrics.sensors.face.hidl.FaceUpdateActiveUserClient(getContext(), new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter$$ExternalSyntheticLambda3(this), this.mUserStartedCallback, userId, TAG, getSensorProperties().sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(getContext()), getBiometricContext(), !com.android.server.biometrics.sensors.face.FaceUtils.getInstance(getSensorProperties().sensorId).getBiometricsForUser(getContext(), userId).isEmpty(), getAuthenticatorIds());
    }
}
