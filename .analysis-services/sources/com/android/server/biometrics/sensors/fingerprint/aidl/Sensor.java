package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class Sensor {
    private static final java.lang.String TAG = "Sensor";
    public static com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintSensorExt mFingerprintSensorExt = (com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintSensorExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintSensorExt.class).create();
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final android.content.Context mContext;
    com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession mCurrentSession;
    private final android.os.Handler mHandler;
    private java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> mLazySession;
    private com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker;
    private final com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider mProvider;
    private com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.fingerprint.IFingerprint, android.hardware.biometrics.fingerprint.ISession> mScheduler;
    private final android.hardware.fingerprint.FingerprintSensorPropertiesInternal mSensorProperties;
    private boolean mTestHalEnabled;
    private final android.os.IBinder mToken;

    public Sensor(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.fingerprint.FingerprintSensorPropertiesInternal sensorProperties, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session) {
        this.mProvider = provider;
        this.mContext = context;
        this.mToken = new android.os.Binder();
        this.mHandler = handler;
        this.mSensorProperties = sensorProperties;
        this.mBiometricContext = biometricContext;
        this.mAuthenticatorIds = new java.util.HashMap();
        this.mCurrentSession = session;
    }

    Sensor(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider provider, android.content.Context context, android.os.Handler handler, android.hardware.biometrics.fingerprint.SensorProps sensorProp, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.List<android.hardware.biometrics.SensorLocationInternal> workaroundLocation, boolean resetLockoutRequiresHardwareAuthToken) {
        this(provider, context, handler, getFingerprintSensorPropertiesInternal(sensorProp, workaroundLocation, resetLockoutRequiresHardwareAuthToken), biometricContext, null);
    }

    public void init(com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher) {
        setScheduler(getBiometricSchedulerForInit(gestureAvailabilityDispatcher, lockoutResetDispatcher));
        this.mLockoutTracker = new com.android.server.biometrics.sensors.LockoutCache();
        this.mLazySession = new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$init$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession lambda$init$0() {
        if (this.mCurrentSession != null) {
            return this.mCurrentSession;
        }
        return null;
    }

    private com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.fingerprint.IFingerprint, android.hardware.biometrics.fingerprint.ISession> getBiometricSchedulerForInit(com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher) {
        return new com.android.server.biometrics.sensors.BiometricScheduler<>(this.mHandler, com.android.server.biometrics.sensors.BiometricScheduler.sensorTypeFromFingerprintProperties(this.mSensorProperties), gestureAvailabilityDispatcher, (java.util.function.Supplier<java.lang.Integer>) new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getBiometricSchedulerForInit$1();
            }
        }, new com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.AnonymousClass1(lockoutResetDispatcher));
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.biometrics.sensors.UserSwitchProvider<android.hardware.biometrics.fingerprint.IFingerprint, android.hardware.biometrics.fingerprint.ISession> {
        final /* synthetic */ com.android.server.biometrics.sensors.LockoutResetDispatcher val$lockoutResetDispatcher;

        AnonymousClass1(com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher) {
            this.val$lockoutResetDispatcher = lockoutResetDispatcher;
        }

        @Override // com.android.server.biometrics.sensors.UserSwitchProvider
        public com.android.server.biometrics.sensors.StopUserClient<android.hardware.biometrics.fingerprint.ISession> getStopUserClient(int userId) {
            return new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintStopUserClient(com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mContext, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getStopUserClient$0();
                }
            }, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mToken, userId, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mSensorProperties.sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mContext), com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mBiometricContext, new com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$1$$ExternalSyntheticLambda1
                @Override // com.android.server.biometrics.sensors.StopUserClient.UserStoppedCallback
                public final void onUserStopped() {
                    this.f$0.lambda$getStopUserClient$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.hardware.biometrics.fingerprint.ISession lambda$getStopUserClient$0() {
            return ((com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession) com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mLazySession.get()).getSession();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStopUserClient$1() {
            com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mCurrentSession = null;
        }

        @Override // com.android.server.biometrics.sensors.UserSwitchProvider
        public com.android.server.biometrics.sensors.StartUserClient<android.hardware.biometrics.fingerprint.IFingerprint, android.hardware.biometrics.fingerprint.ISession> getStartUserClient(final int newUserId) {
            final int sensorId = com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mSensorProperties.sensorId;
            com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler resultController = new com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler(com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mContext, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mScheduler, sensorId, newUserId, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mLockoutTracker, this.val$lockoutResetDispatcher, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mBiometricContext.getAuthSessionCoordinator(), new com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.1.1
                @Override // com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onEnrollSuccess() {
                    int multiSystemUserId = com.oplus.multiuser.OplusMultiUserManager.getInstance().getMultiSystemUserId();
                    if (multiSystemUserId != -10000 && (newUserId == multiSystemUserId || newUserId == 0)) {
                        com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mProvider.scheduleLoadAuthenticatorIdsForUser(sensorId, multiSystemUserId);
                        com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mProvider.scheduleLoadAuthenticatorIdsForUser(sensorId, 0);
                        android.util.Slog.w(com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.TAG, "ScheduleLoadAuthenticatorIdsForUser for USER_SYSTEM and multiSystemUserId: " + multiSystemUserId);
                    } else {
                        com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mProvider.scheduleLoadAuthenticatorIdsForUser(sensorId, newUserId);
                    }
                    com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mProvider.scheduleInvalidationRequest(sensorId, newUserId);
                }

                @Override // com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler.AidlResponseHandlerCallback
                public void onHardwareUnavailable() {
                    android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.TAG, "Fingerprint sensor hardware unavailable.");
                    com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mCurrentSession = null;
                }
            });
            com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.mFingerprintSensorExt.setProvider(resultController, com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.mProvider);
            return com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.this.getStartUserClient(resultController, sensorId, newUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getBiometricSchedulerForInit$1() {
        return java.lang.Integer.valueOf(this.mCurrentSession != null ? this.mCurrentSession.getUserId() : -10000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintStartUserClient getStartUserClient(final com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler resultController, final int sensorId, int newUserId) {
        com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback<android.hardware.biometrics.fingerprint.ISession> userStartedCallback = new com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda0
            @Override // com.android.server.biometrics.sensors.StartUserClient.UserStartedCallback
            public final void onUserStarted(int i, java.lang.Object obj, int i2) {
                this.f$0.lambda$getStartUserClient$2(resultController, sensorId, i, (android.hardware.biometrics.fingerprint.ISession) obj, i2);
            }
        };
        android.content.Context context = this.mContext;
        final com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider fingerprintProvider = this.mProvider;
        java.util.Objects.requireNonNull(fingerprintProvider);
        return new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintStartUserClient(context, new java.util.function.Supplier() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return fingerprintProvider.getHalInstance();
            }
        }, this.mToken, newUserId, this.mSensorProperties.sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, resultController, userStartedCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStartUserClient$2(com.android.server.biometrics.sensors.fingerprint.aidl.AidlResponseHandler resultController, int sensorId, int userIdStarted, android.hardware.biometrics.fingerprint.ISession newSession, int halInterfaceVersion) {
        android.util.Slog.d(TAG, "New fingerprint session created for user: " + userIdStarted + " with hal version: " + halInterfaceVersion);
        this.mCurrentSession = new com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession(halInterfaceVersion, newSession, userIdStarted, resultController);
        if (com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId).isInvalidationInProgress(this.mContext, userIdStarted)) {
            android.util.Slog.w(TAG, "Scheduling unfinished invalidation request for fingerprint sensor: " + sensorId + ", user: " + userIdStarted);
            this.mProvider.scheduleInvalidationRequest(sensorId, userIdStarted);
        }
    }

    protected static android.hardware.fingerprint.FingerprintSensorPropertiesInternal getFingerprintSensorPropertiesInternal(android.hardware.biometrics.fingerprint.SensorProps prop, java.util.List<android.hardware.biometrics.SensorLocationInternal> workaroundLocations, boolean resetLockoutRequiresHardwareAuthToken) {
        java.util.List<android.hardware.biometrics.ComponentInfoInternal> componentInfo = new java.util.ArrayList<>();
        if (prop.commonProps.componentInfo != null) {
            for (android.hardware.biometrics.common.ComponentInfo info : prop.commonProps.componentInfo) {
                componentInfo.add(new android.hardware.biometrics.ComponentInfoInternal(info.componentId, info.hardwareVersion, info.firmwareVersion, info.serialNumber, info.softwareVersion));
            }
        }
        return new android.hardware.fingerprint.FingerprintSensorPropertiesInternal(prop.commonProps.sensorId, prop.commonProps.sensorStrength, prop.commonProps.maxEnrollmentsPerUser, componentInfo, prop.sensorType, prop.halControlsIllumination, resetLockoutRequiresHardwareAuthToken, !workaroundLocations.isEmpty() ? workaroundLocations : (java.util.List) java.util.Arrays.stream(prop.sensorLocations).map(new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.Sensor$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.biometrics.sensors.fingerprint.aidl.Sensor.lambda$getFingerprintSensorPropertiesInternal$3((android.hardware.biometrics.fingerprint.SensorLocation) obj);
            }
        }).collect(java.util.stream.Collectors.toList()));
    }

    static /* synthetic */ android.hardware.biometrics.SensorLocationInternal lambda$getFingerprintSensorPropertiesInternal$3(android.hardware.biometrics.fingerprint.SensorLocation location) {
        return new android.hardware.biometrics.SensorLocationInternal(location.display, location.sensorLocationX, location.sensorLocationY, location.sensorRadius);
    }

    public java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> getLazySession() {
        return this.mLazySession;
    }

    public android.hardware.fingerprint.FingerprintSensorPropertiesInternal getSensorProperties() {
        return this.mSensorProperties;
    }

    protected com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession getSessionForUser(int userId) {
        android.util.Slog.d(TAG, "getSessionForUser: mCurrentSession: " + this.mCurrentSession);
        if (this.mCurrentSession != null && this.mCurrentSession.getUserId() == userId) {
            return this.mCurrentSession;
        }
        return null;
    }

    android.hardware.biometrics.ITestSession createTestSession(android.hardware.biometrics.ITestSessionCallback callback, com.android.server.biometrics.sensors.BiometricStateCallback biometricStateCallback) {
        return new com.android.server.biometrics.sensors.fingerprint.aidl.BiometricTestSessionImpl(this.mContext, this.mSensorProperties.sensorId, callback, biometricStateCallback, this.mProvider, this);
    }

    public com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.fingerprint.IFingerprint, android.hardware.biometrics.fingerprint.ISession> getScheduler() {
        return this.mScheduler;
    }

    protected com.android.server.biometrics.sensors.LockoutTracker getLockoutTracker(boolean forAuth) {
        if (forAuth) {
            return null;
        }
        return this.mLockoutTracker;
    }

    public java.util.Map<java.lang.Integer, java.lang.Long> getAuthenticatorIds() {
        return this.mAuthenticatorIds;
    }

    void setTestHalEnabled(boolean enabled) {
        android.util.Slog.w(TAG, "Fingerprint setTestHalEnabled: " + enabled);
        if (enabled != this.mTestHalEnabled) {
            try {
                if (this.mCurrentSession != null) {
                    android.util.Slog.d(TAG, "Closing old fingerprint session");
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
        proto.write(1159641169922L, 1);
        if (this.mSensorProperties.isAnyUdfpsType()) {
            proto.write(2259152797704L, 0);
        }
        proto.write(1120986464259L, com.android.server.biometrics.Utils.getCurrentStrength(this.mSensorProperties.sensorId));
        proto.write(1146756268036L, this.mScheduler.dumpProtoState(clearSchedulerBuffer));
        for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getUsers()) {
            int userId = user.getUserHandle().getIdentifier();
            long userToken = proto.start(2246267895813L);
            proto.write(1120986464257L, userId);
            proto.write(1120986464258L, com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(this.mSensorProperties.sensorId).getBiometricsForUser(this.mContext, userId).size());
            proto.end(userToken);
        }
        proto.write(1133871366150L, this.mSensorProperties.resetLockoutRequiresHardwareAuthToken);
        proto.write(1133871366151L, this.mSensorProperties.resetLockoutRequiresChallenge);
        proto.end(sensorToken);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onBinderDied() {
        com.android.server.biometrics.sensors.BaseClientMonitor currentClient = this.mScheduler.getCurrentClient();
        if (currentClient instanceof com.android.server.biometrics.sensors.ErrorConsumer) {
            android.util.Slog.e(TAG, "Sending fingerprint hardware unavailable error for client: " + currentClient);
            com.android.server.biometrics.sensors.ErrorConsumer errorConsumer = (com.android.server.biometrics.sensors.ErrorConsumer) currentClient;
            errorConsumer.onError(1, 0);
            com.android.internal.util.FrameworkStatsLog.write(148, 1, 1, -1);
        } else if (currentClient != 0) {
            currentClient.cancel();
        }
        this.mScheduler.recordCrashState();
        this.mScheduler.reset();
        this.mCurrentSession = null;
    }

    protected android.os.Handler getHandler() {
        return this.mHandler;
    }

    protected android.content.Context getContext() {
        return this.mContext;
    }

    protected boolean isHardwareDetected(java.lang.String halInstance) {
        return this.mTestHalEnabled || android.os.ServiceManager.checkService(new java.lang.StringBuilder().append(android.hardware.biometrics.fingerprint.IFingerprint.DESCRIPTOR).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(halInstance).toString()) != null;
    }

    protected com.android.server.biometrics.log.BiometricContext getBiometricContext() {
        return this.mBiometricContext;
    }

    public int getLockoutModeForUser(int userId) {
        return this.mBiometricContext.getAuthSessionCoordinator().getLockoutStateFor(userId, com.android.server.biometrics.Utils.getCurrentStrength(this.mSensorProperties.sensorId));
    }

    public void setScheduler(com.android.server.biometrics.sensors.BiometricScheduler scheduler) {
        this.mScheduler = scheduler;
    }

    public void setLazySession(java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazySession) {
        this.mLazySession = lazySession;
    }

    public com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider getProvider() {
        return this.mProvider;
    }
}
