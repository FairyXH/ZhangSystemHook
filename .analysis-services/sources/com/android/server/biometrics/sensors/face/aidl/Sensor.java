package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class Sensor {
    private static final java.lang.String TAG = "Sensor";
    public static com.android.server.biometrics.sensors.face.aidl.ISensorExt mOplusSensor;
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final android.content.Context mContext;
    com.android.server.biometrics.sensors.face.aidl.AidlSession mCurrentSession;
    private final android.os.Handler mHandler;
    private java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> mLazySession;
    private com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker;
    private final com.android.server.biometrics.sensors.face.aidl.FaceProvider mProvider;
    private com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> mScheduler;
    private final android.hardware.face.FaceSensorPropertiesInternal mSensorProperties;
    private boolean mTestHalEnabled;
    private final android.os.IBinder mToken;

    Sensor(com.android.server.biometrics.sensors.face.aidl.FaceProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.face.FaceSensorPropertiesInternal sensorProperties, com.android.server.biometrics.log.BiometricContext biometricContext) {
        this.mProvider = provider;
        this.mContext = context;
        this.mToken = new android.os.Binder();
        this.mHandler = handler;
        this.mSensorProperties = sensorProperties;
        this.mBiometricContext = biometricContext;
        this.mAuthenticatorIds = new java.util.HashMap();
    }

    public Sensor(com.android.server.biometrics.sensors.face.aidl.FaceProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.biometrics.face.SensorProps prop, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresChallenge) {
        this(provider, context, handler, getFaceSensorPropertiesInternal(prop, resetLockoutRequiresChallenge), biometricContext);
    }

    public void init(com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.face.aidl.FaceProvider provider) {
        setScheduler(getBiometricSchedulerForInit(lockoutResetDispatcher, provider));
        this.mLazySession = new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$init$0();
            }
        };
        mOplusSensor = (com.android.server.biometrics.sensors.face.aidl.ISensorExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.aidl.ISensorExt.class).base(this).create();
        this.mLockoutTracker = new com.android.server.biometrics.sensors.LockoutCache();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.biometrics.sensors.face.aidl.AidlSession lambda$init$0() {
        if (this.mCurrentSession != null) {
            return this.mCurrentSession;
        }
        return null;
    }

    private com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> getBiometricSchedulerForInit(com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.face.aidl.FaceProvider provider) {
        return new com.android.server.biometrics.sensors.BiometricScheduler<>(this.mHandler, 1, (com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher) null, (java.util.function.Supplier<java.lang.Integer>) new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getBiometricSchedulerForInit$1();
            }
        }, new com.android.server.biometrics.sensors.face.aidl.Sensor.AnonymousClass1(lockoutResetDispatcher, provider));
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.face.aidl.Sensor$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.biometrics.sensors.UserSwitchProvider<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> {
        final /* synthetic */ com.android.server.biometrics.sensors.LockoutResetDispatcher val$lockoutResetDispatcher;
        final /* synthetic */ com.android.server.biometrics.sensors.face.aidl.FaceProvider val$provider;

        AnonymousClass1(com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.face.aidl.FaceProvider faceProvider) {
            this.val$lockoutResetDispatcher = lockoutResetDispatcher;
            this.val$provider = faceProvider;
        }

        @Override // com.android.server.biometrics.sensors.UserSwitchProvider
        public com.android.server.biometrics.sensors.StopUserClient<android.hardware.biometrics.face.ISession> getStopUserClient(int userId) {
            return new com.android.server.biometrics.sensors.face.aidl.FaceStopUserClient(com.android.server.biometrics.sensors.face.aidl.Sensor.this.mContext, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getStopUserClient$0();
                }
            }, com.android.server.biometrics.sensors.face.aidl.Sensor.this.mToken, userId, com.android.server.biometrics.sensors.face.aidl.Sensor.this.mSensorProperties.sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(com.android.server.biometrics.sensors.face.aidl.Sensor.this.mContext), com.android.server.biometrics.sensors.face.aidl.Sensor.this.mBiometricContext, new com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$1$$ExternalSyntheticLambda1
                @Override // com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback
                public final void onUserStopped() {
                    this.f$0.lambda$getStopUserClient$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.hardware.biometrics.face.ISession lambda$getStopUserClient$0() {
            return ((com.android.server.biometrics.sensors.face.aidl.AidlSession) com.android.server.biometrics.sensors.face.aidl.Sensor.this.mLazySession.get()).getSession();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStopUserClient$1() {
            com.android.server.biometrics.sensors.face.aidl.Sensor.this.mCurrentSession = null;
        }

        @Override // com.android.server.biometrics.sensors.UserSwitchProvider
        public com.android.server.biometrics.sensors.StartUserClient<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> getStartUserClient(final int newUserId) {
            final int sensorId = com.android.server.biometrics.sensors.face.aidl.Sensor.this.mSensorProperties.sensorId;
            com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler resultController = new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler(com.android.server.biometrics.sensors.face.aidl.Sensor.this.mContext, com.android.server.biometrics.sensors.face.aidl.Sensor.this.mScheduler, sensorId, newUserId, com.android.server.biometrics.sensors.face.aidl.Sensor.this.mLockoutTracker, this.val$lockoutResetDispatcher, com.android.server.biometrics.sensors.face.aidl.Sensor.this.mBiometricContext.getAuthSessionCoordinator(), new com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor.1.1
                @Override // com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onEnrollSuccess() {
                    com.android.server.biometrics.sensors.face.aidl.Sensor.this.mProvider.scheduleLoadAuthenticatorIdsForUser(sensorId, newUserId);
                    com.android.server.biometrics.sensors.face.aidl.Sensor.this.mProvider.scheduleInvalidationRequest(sensorId, newUserId);
                }

                @Override // com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onHardwareUnavailable() {
                    android.util.Slog.e(com.android.server.biometrics.sensors.face.aidl.Sensor.TAG, "Face sensor hardware unavailable.");
                    com.android.server.biometrics.sensors.face.aidl.Sensor.this.mCurrentSession = null;
                }
            });
            com.android.server.biometrics.sensors.face.aidl.Sensor.mOplusSensor.setProvider(resultController, this.val$provider);
            return com.android.server.biometrics.sensors.face.aidl.Sensor.this.getStartUserClient(resultController, sensorId, newUserId, this.val$provider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getBiometricSchedulerForInit$1() {
        return java.lang.Integer.valueOf(this.mCurrentSession != null ? this.mCurrentSession.getUserId() : -10000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.biometrics.sensors.face.aidl.FaceStartUserClient getStartUserClient(final com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler resultController, final int sensorId, int newUserId, final com.android.server.biometrics.sensors.face.aidl.FaceProvider provider) {
        com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<android.hardware.biometrics.face.ISession> userStartedCallback = new com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$$ExternalSyntheticLambda2
            @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
            public final void onUserStarted(int i, java.lang.Object obj, int i2) {
                this.f$0.lambda$getStartUserClient$2(resultController, sensorId, provider, i, (android.hardware.biometrics.face.ISession) obj, i2);
            }
        };
        android.content.Context context = this.mContext;
        java.util.Objects.requireNonNull(provider);
        return new com.android.server.biometrics.sensors.face.aidl.FaceStartUserClient(context, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.face.aidl.Sensor$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return provider.getHalInstance();
            }
        }, this.mToken, newUserId, this.mSensorProperties.sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, resultController, userStartedCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStartUserClient$2(com.android.server.biometrics.sensors.face.aidl.AidlResponseHandler resultController, int sensorId, com.android.server.biometrics.sensors.face.aidl.FaceProvider provider, int userIdStarted, android.hardware.biometrics.face.ISession newSession, int halInterfaceVersion) {
        android.util.Slog.d(TAG, "New face session created for user: " + userIdStarted + " with hal version: " + halInterfaceVersion);
        this.mCurrentSession = new com.android.server.biometrics.sensors.face.aidl.AidlSession(halInterfaceVersion, newSession, userIdStarted, resultController);
        if (com.android.server.biometrics.sensors.face.FaceUtils.getLegacyInstance(sensorId).isInvalidationInProgress(this.mContext, userIdStarted)) {
            android.util.Slog.w(TAG, "Scheduling unfinished invalidation request for face sensor: " + sensorId + ", user: " + userIdStarted);
            provider.scheduleInvalidationRequest(sensorId, userIdStarted);
        }
    }

    private static android.hardware.face.FaceSensorPropertiesInternal getFaceSensorPropertiesInternal(android.hardware.biometrics.face.SensorProps prop, boolean resetLockoutRequiresChallenge) {
        java.util.List<android.hardware.biometrics.ComponentInfoInternal> componentInfo = new java.util.ArrayList<>();
        if (prop.commonProps.componentInfo != null) {
            for (android.hardware.biometrics.common.ComponentInfo info : prop.commonProps.componentInfo) {
                componentInfo.add(new android.hardware.biometrics.ComponentInfoInternal(info.componentId, info.hardwareVersion, info.firmwareVersion, info.serialNumber, info.softwareVersion));
            }
        }
        return new android.hardware.face.FaceSensorPropertiesInternal(prop.commonProps.sensorId, prop.commonProps.sensorStrength, prop.commonProps.maxEnrollmentsPerUser, componentInfo, prop.sensorType, prop.supportsDetectInteraction, prop.halControlsPreview, resetLockoutRequiresChallenge);
    }

    public java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> getLazySession() {
        return this.mLazySession;
    }

    protected android.hardware.face.FaceSensorPropertiesInternal getSensorProperties() {
        return this.mSensorProperties;
    }

    protected com.android.server.biometrics.sensors.face.aidl.AidlSession getSessionForUser(int userId) {
        android.util.Slog.d(TAG, "getSessionForUser: mCurrentSession: " + this.mCurrentSession);
        if (this.mCurrentSession != null && this.mCurrentSession.getUserId() == userId) {
            return this.mCurrentSession;
        }
        return null;
    }

    android.hardware.biometrics.ITestSession createTestSession(android.hardware.biometrics.ITestSessionCallback callback) {
        return new com.android.server.biometrics.sensors.face.aidl.BiometricTestSessionImpl(this.mContext, this.mSensorProperties.sensorId, callback, this.mProvider, this);
    }

    public com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> getScheduler() {
        return this.mScheduler;
    }

    protected com.android.server.biometrics.sensors.LockoutTracker getLockoutTracker(boolean forAuth) {
        if (forAuth) {
            return null;
        }
        return this.mLockoutTracker;
    }

    protected java.util.Map<java.lang.Integer, java.lang.Long> getAuthenticatorIds() {
        return this.mAuthenticatorIds;
    }

    void setTestHalEnabled(boolean enabled) {
        android.util.Slog.w(TAG, "Face setTestHalEnabled: " + enabled);
        if (enabled != this.mTestHalEnabled) {
            try {
                if (this.mCurrentSession != null) {
                    android.util.Slog.d(TAG, "Closing old face session");
                    this.mCurrentSession.getSession().close();
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException", e);
            }
            this.mCurrentSession = null;
        }
        this.mTestHalEnabled = enabled;
    }

    void dumpProtoState(int sensorId, android.util.proto.ProtoOutputStream proto, boolean clearSchedulerBuffer) {
        long sensorToken = proto.start(2246267895809L);
        proto.write(1120986464257L, this.mSensorProperties.sensorId);
        proto.write(1159641169922L, 2);
        proto.write(1120986464259L, com.android.server.biometrics.Utils.getCurrentStrength(this.mSensorProperties.sensorId));
        proto.write(1146756268036L, this.mScheduler.dumpProtoState(clearSchedulerBuffer));
        for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getUsers()) {
            int userId = user.getUserHandle().getIdentifier();
            long userToken = proto.start(2246267895813L);
            proto.write(1120986464257L, userId);
            proto.write(1120986464258L, com.android.server.biometrics.sensors.face.FaceUtils.getInstance(this.mSensorProperties.sensorId).getBiometricsForUser(this.mContext, userId).size());
            proto.end(userToken);
        }
        proto.write(1133871366150L, this.mSensorProperties.resetLockoutRequiresHardwareAuthToken);
        proto.write(1133871366151L, this.mSensorProperties.resetLockoutRequiresChallenge);
        proto.end(sensorToken);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onBinderDied() {
        com.android.server.biometrics.sensors.BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
        if (currentClient != 0 && currentClient.isInterruptable()) {
            android.util.Slog.e(TAG, "Sending face hardware unavailable error for client: " + currentClient);
            com.android.server.biometrics.sensors.ErrorConsumer errorConsumer = (com.android.server.biometrics.sensors.ErrorConsumer) currentClient;
            errorConsumer.onError(1, 0);
            com.android.internal.util.FrameworkStatsLog.write(148, 4, 1, -1);
        } else if (currentClient != 0) {
            currentClient.cancel();
        }
        this.mScheduler.recordCrashState();
        this.mScheduler.reset();
        this.mCurrentSession = null;
    }

    protected com.android.server.biometrics.log.BiometricContext getBiometricContext() {
        return this.mBiometricContext;
    }

    protected android.os.Handler getHandler() {
        return this.mHandler;
    }

    protected android.content.Context getContext() {
        return this.mContext;
    }

    public void scheduleFaceUpdateActiveUserClient(int userId) {
    }

    public boolean isHardwareDetected(java.lang.String halInstanceName) {
        return this.mTestHalEnabled || android.os.ServiceManager.checkService(new java.lang.StringBuilder().append(android.hardware.biometrics.face.IFace.DESCRIPTOR).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(halInstanceName).toString()) != null;
    }

    public int getLockoutModeForUser(int userId) {
        return this.mBiometricContext.getAuthSessionCoordinator().getLockoutStateFor(userId, com.android.server.biometrics.Utils.getCurrentStrength(this.mSensorProperties.sensorId));
    }

    public void setScheduler(com.android.server.biometrics.sensors.BiometricScheduler scheduler) {
        this.mScheduler = scheduler;
    }

    public void setLazySession(java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazySession) {
        this.mLazySession = lazySession;
    }
}
