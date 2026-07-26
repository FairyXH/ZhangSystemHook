package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceProvider implements android.os.IBinder.DeathRecipient, com.android.server.biometrics.sensors.face.ServiceProvider {
    private static final int ENROLL_TIMEOUT_SEC = 75;
    private static final java.lang.String TAG = "FaceProvider";
    private static com.android.server.biometrics.sensors.face.aidl.IFaceProviderExt sOplusServiceProvider = (com.android.server.biometrics.sensors.face.aidl.IFaceProviderExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.face.aidl.IFaceProviderExt.class).base((java.lang.Object) null).create();
    private final android.app.ActivityTaskManager mActivityTaskManager;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private com.android.server.biometrics.AuthenticationStatsCollector mAuthenticationStatsCollector;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final com.android.server.biometrics.BiometricHandlerProvider mBiometricHandlerProvider;
    private final com.android.server.biometrics.sensors.BiometricStateCallback mBiometricStateCallback;
    private final android.content.Context mContext;
    private android.hardware.biometrics.face.IFace mDaemon;
    final com.android.server.biometrics.sensors.SensorList<com.android.server.biometrics.sensors.face.aidl.Sensor> mFaceSensors;
    private final java.lang.String mHalInstanceName;
    private final android.os.Handler mHandler;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private final java.util.concurrent.atomic.AtomicLong mRequestCounter;
    private com.android.server.biometrics.sensors.face.IServiceProviderWrapper mServiceProviderWrapper;
    private final com.android.server.biometrics.sensors.face.aidl.FaceProvider.BiometricTaskStackListener mTaskStackListener;
    private boolean mTestHalEnabled;
    private final com.android.server.biometrics.sensors.face.UsageStats mUsageStats;

    /* JADX INFO: Access modifiers changed from: private */
    final class BiometricTaskStackListener extends android.app.TaskStackListener {
        private BiometricTaskStackListener() {
        }

        public void onTaskStackChanged() {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$BiometricTaskStackListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTaskStackChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskStackChanged$0() {
            for (int i = 0; i < com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mFaceSensors.size(); i++) {
                com.android.server.biometrics.sensors.BaseClientMonitor client = com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mFaceSensors.valueAt(i).getScheduler().getCurrentClient();
                if (!(client instanceof com.android.server.biometrics.sensors.AuthenticationClient)) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.getTag(), "Task stack changed for client: " + client);
                } else if (!com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mContext, client.getOwnerString()) && !com.android.server.biometrics.Utils.isSystem(com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mContext, client.getOwnerString()) && com.android.server.biometrics.Utils.isBackground(client.getOwnerString()) && !client.isAlreadyDone()) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.getTag(), "Stopping background authentication, currentClient: " + client);
                    com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mFaceSensors.valueAt(i).getScheduler().cancelAuthenticationOrDetection(client.getToken(), client.getRequestId());
                }
            }
        }
    }

    public FaceProvider(android.content.Context context, com.android.server.biometrics.sensors.BiometricStateCallback biometricStateCallback, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, android.hardware.biometrics.face.SensorProps[] props, java.lang.String halInstanceName, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresChallenge) {
        this(context, biometricStateCallback, authenticationStateListeners, props, halInstanceName, lockoutResetDispatcher, biometricContext, null, com.android.server.biometrics.BiometricHandlerProvider.getInstance(), resetLockoutRequiresChallenge, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    FaceProvider(android.content.Context context, com.android.server.biometrics.sensors.BiometricStateCallback biometricStateCallback, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, android.hardware.biometrics.face.SensorProps[] sensorPropsArr, java.lang.String str, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, android.hardware.biometrics.face.IFace iFace, com.android.server.biometrics.BiometricHandlerProvider biometricHandlerProvider, boolean z, boolean z2) {
        this.mRequestCounter = new java.util.concurrent.atomic.AtomicLong(0L);
        this.mServiceProviderWrapper = new com.android.server.biometrics.sensors.face.aidl.FaceProvider.OplusFaceProviderWrapper();
        this.mContext = context;
        this.mBiometricStateCallback = biometricStateCallback;
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mHalInstanceName = str;
        this.mFaceSensors = new com.android.server.biometrics.sensors.SensorList<>(android.app.ActivityManager.getService());
        this.mHandler = biometricHandlerProvider.getFaceHandler();
        this.mUsageStats = new com.android.server.biometrics.sensors.face.UsageStats(context);
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mActivityTaskManager = android.app.ActivityTaskManager.getInstance();
        this.mTaskStackListener = new com.android.server.biometrics.sensors.face.aidl.FaceProvider.BiometricTaskStackListener();
        this.mBiometricContext = biometricContext;
        this.mAuthSessionCoordinator = this.mBiometricContext.getAuthSessionCoordinator();
        this.mDaemon = iFace;
        this.mTestHalEnabled = z2;
        this.mBiometricHandlerProvider = biometricHandlerProvider;
        initAuthenticationBroadcastReceiver();
        initFaceDanglingBroadcastReceiver();
        initSensors(z, sensorPropsArr);
    }

    private void initAuthenticationBroadcastReceiver() {
        new com.android.server.biometrics.AuthenticationStatsBroadcastReceiver(this.mContext, 4, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$initAuthenticationBroadcastReceiver$0((com.android.server.biometrics.AuthenticationStatsCollector) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initAuthenticationBroadcastReceiver$0(com.android.server.biometrics.AuthenticationStatsCollector collector) {
        android.util.Slog.d(getTag(), "Initializing AuthenticationStatsCollector");
        this.mAuthenticationStatsCollector = collector;
    }

    private void initFaceDanglingBroadcastReceiver() {
        new com.android.server.biometrics.BiometricDanglingReceiver(this.mContext, 4);
    }

    private void initSensors(boolean resetLockoutRequiresChallenge, android.hardware.biometrics.face.SensorProps[] props) {
        int i = 0;
        if (resetLockoutRequiresChallenge) {
            android.util.Slog.d(getTag(), "Adding HIDL configs");
            int length = props.length;
            while (i < length) {
                android.hardware.biometrics.face.SensorProps prop = props[i];
                addHidlSensors(prop, resetLockoutRequiresChallenge);
                i++;
            }
        } else {
            android.util.Slog.d(getTag(), "Adding AIDL configs");
            int length2 = props.length;
            while (i < length2) {
                android.hardware.biometrics.face.SensorProps prop2 = props[i];
                addAidlSensors(prop2, resetLockoutRequiresChallenge);
                i++;
            }
        }
        getExtImpl().init(this, this.mContext, props, this.mHalInstanceName, this.mHandler);
    }

    private void addHidlSensors(android.hardware.biometrics.face.SensorProps prop, boolean resetLockoutRequiresChallenge) {
        final int sensorId = prop.commonProps.sensorId;
        com.android.server.biometrics.sensors.face.aidl.Sensor sensor = new com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter(this, this.mContext, this.mHandler, prop, this.mLockoutResetDispatcher, this.mBiometricContext, resetLockoutRequiresChallenge, new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$addHidlSensors$1(sensorId);
            }
        });
        sensor.init(this.mLockoutResetDispatcher, this);
        int userId = sensor.getLazySession().get() == null ? -10000 : sensor.getLazySession().get().getUserId();
        this.mFaceSensors.addSensor(sensorId, sensor, userId, new android.app.SynchronousUserSwitchObserver() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.1
            public void onUserSwitching(int newUserId) {
                com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.scheduleInternalCleanup(sensorId, newUserId, null);
                com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.scheduleGetFeature(sensorId, new android.os.Binder(), newUserId, 1, null, com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mContext.getOpPackageName());
            }
        });
        android.util.Slog.d(getTag(), "Added: " + this.mFaceSensors.get(sensorId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addHidlSensors$1(int sensorId) {
        scheduleInternalCleanup(sensorId, android.app.ActivityManager.getCurrentUser(), null);
        scheduleGetFeature(sensorId, new android.os.Binder(), android.app.ActivityManager.getCurrentUser(), 1, null, this.mContext.getOpPackageName());
    }

    private void addAidlSensors(android.hardware.biometrics.face.SensorProps prop, boolean resetLockoutRequiresChallenge) {
        final int sensorId = prop.commonProps.sensorId;
        com.android.server.biometrics.sensors.face.aidl.Sensor sensor = new com.android.server.biometrics.sensors.face.aidl.Sensor(this, this.mContext, this.mHandler, prop, this.mBiometricContext, resetLockoutRequiresChallenge);
        sensor.init(this.mLockoutResetDispatcher, this);
        int userId = sensor.getLazySession().get() == null ? -10000 : sensor.getLazySession().get().getUserId();
        this.mFaceSensors.addSensor(sensorId, sensor, userId, new android.app.SynchronousUserSwitchObserver() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.2
            public void onUserSwitching(int newUserId) {
                com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.scheduleInternalCleanup(sensorId, newUserId, null);
            }
        });
        android.util.Slog.d(getTag(), "Added: " + this.mFaceSensors.get(sensorId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getTag() {
        return "FaceProvider/" + this.mHalInstanceName;
    }

    boolean hasHalInstance() {
        return this.mTestHalEnabled || android.os.ServiceManager.checkService(new java.lang.StringBuilder().append(android.hardware.biometrics.face.IFace.DESCRIPTOR).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(this.mHalInstanceName).toString()) != null;
    }

    synchronized android.hardware.biometrics.face.IFace getHalInstance() {
        if (this.mTestHalEnabled) {
            return new com.android.server.biometrics.sensors.face.aidl.TestHal();
        }
        if (this.mDaemon != null) {
            return this.mDaemon;
        }
        android.util.Slog.d(getTag(), "Daemon was null, reconnecting");
        this.mDaemon = android.hardware.biometrics.face.IFace.Stub.asInterface(android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService(android.hardware.biometrics.face.IFace.DESCRIPTOR + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mHalInstanceName)));
        if (this.mDaemon == null) {
            android.util.Slog.e(getTag(), "Unable to get daemon");
            return null;
        }
        try {
            this.mDaemon.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(getTag(), "Unable to linkToDeath", e);
        }
        for (int i = 0; i < this.mFaceSensors.size(); i++) {
            int sensorId = this.mFaceSensors.keyAt(i);
            scheduleLoadAuthenticatorIds(sensorId);
            scheduleInternalCleanup(sensorId, android.app.ActivityManager.getCurrentUser(), null);
        }
        if (android.os.Build.isDebuggable()) {
            com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> utils = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(this.mFaceSensors.keyAt(0));
            for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getAliveUsers()) {
                java.util.List<android.hardware.face.Face> enrollments = utils.getBiometricsForUser(this.mContext, user.id);
                android.util.Slog.d(getTag(), "Expecting enrollments for user " + user.id + ": " + enrollments.stream().map(new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda17
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return java.lang.Integer.valueOf(((android.hardware.face.Face) obj).getBiometricId());
                    }
                }).toList());
            }
        }
        return this.mDaemon;
    }

    private void scheduleForSensor(int sensorId, com.android.server.biometrics.sensors.BaseClientMonitor client) {
        if (!this.mFaceSensors.contains(sensorId)) {
            throw new java.lang.IllegalStateException("Unable to schedule client: " + client + " for sensor: " + sensorId);
        }
        this.mFaceSensors.get(sensorId).getScheduler().scheduleClientMonitor(client);
    }

    private void scheduleForSensor(int sensorId, com.android.server.biometrics.sensors.BaseClientMonitor client, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        if (!this.mFaceSensors.contains(sensorId)) {
            throw new java.lang.IllegalStateException("Unable to schedule client: " + client + " for sensor: " + sensorId);
        }
        this.mFaceSensors.get(sensorId).getScheduler().scheduleClientMonitor(client, callback);
    }

    private void scheduleLoadAuthenticatorIds(int sensorId) {
        for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getAliveUsers()) {
            scheduleLoadAuthenticatorIdsForUser(sensorId, user.id);
        }
    }

    protected void scheduleLoadAuthenticatorIdsForUser(final int sensorId, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleLoadAuthenticatorIdsForUser$2(sensorId, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLoadAuthenticatorIdsForUser$2(int sensorId, int userId) {
        com.android.server.biometrics.sensors.face.aidl.FaceGetAuthenticatorIdClient client = new com.android.server.biometrics.sensors.face.aidl.FaceGetAuthenticatorIdClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), userId, this.mContext.getOpPackageName(), sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFaceSensors.get(sensorId).getAuthenticatorIds());
        scheduleForSensor(sensorId, client);
    }

    void scheduleInvalidationRequest(final int sensorId, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInvalidationRequest$3(userId, sensorId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidationRequest$3(int userId, int sensorId) {
        com.android.server.biometrics.sensors.InvalidationRequesterClient<android.hardware.face.Face> client = new com.android.server.biometrics.sensors.InvalidationRequesterClient<>(this.mContext, userId, sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId));
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean containsSensor(int sensorId) {
        return this.mFaceSensors.contains(sensorId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public java.util.List<android.hardware.face.FaceSensorPropertiesInternal> getSensorProperties() {
        java.util.List<android.hardware.face.FaceSensorPropertiesInternal> props = new java.util.ArrayList<>();
        for (int i = 0; i < this.mFaceSensors.size(); i++) {
            props.add(this.mFaceSensors.valueAt(i).getSensorProperties());
        }
        return props;
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public android.hardware.face.FaceSensorPropertiesInternal getSensorProperties(int sensorId) {
        return this.mFaceSensors.get(sensorId).getSensorProperties();
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public java.util.List<android.hardware.face.Face> getEnrolledFaces(int sensorId, int userId) {
        return com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean hasEnrollments(int sensorId, int userId) {
        return !getEnrolledFaces(sensorId, userId).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleInvalidateAuthenticatorId(final int sensorId, final int userId, final android.hardware.biometrics.IInvalidationCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInvalidateAuthenticatorId$4(sensorId, userId, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidateAuthenticatorId$4(int sensorId, int userId, android.hardware.biometrics.IInvalidationCallback callback) {
        com.android.server.biometrics.sensors.face.aidl.FaceInvalidationClient client = new com.android.server.biometrics.sensors.face.aidl.FaceInvalidationClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), userId, sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFaceSensors.get(sensorId).getAuthenticatorIds(), callback);
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public int getLockoutModeForUser(int sensorId, int userId) {
        return this.mFaceSensors.get(sensorId).getLockoutModeForUser(userId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public long getAuthenticatorId(int sensorId, int userId) {
        return this.mFaceSensors.get(sensorId).getAuthenticatorIds().getOrDefault(java.lang.Integer.valueOf(userId), 0L).longValue();
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean isHardwareDetected(int sensorId) {
        return this.mFaceSensors.get(sensorId).isHardwareDetected(this.mHalInstanceName);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleGenerateChallenge(final int sensorId, final int userId, final android.os.IBinder token, final android.hardware.face.IFaceServiceReceiver receiver, final java.lang.String opPackageName) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleGenerateChallenge$5(sensorId, userId, token, receiver, opPackageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGenerateChallenge$5(int sensorId, int userId, android.os.IBinder token, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        com.android.server.biometrics.sensors.face.aidl.FaceGenerateChallengeClient client = new com.android.server.biometrics.sensors.face.aidl.FaceGenerateChallengeClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), userId, opPackageName, sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext);
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleRevokeChallenge(final int sensorId, final int userId, final android.os.IBinder token, final java.lang.String opPackageName, final long challenge) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRevokeChallenge$6(sensorId, token, userId, opPackageName, challenge);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRevokeChallenge$6(int sensorId, android.os.IBinder token, int userId, java.lang.String opPackageName, long challenge) {
        com.android.server.biometrics.sensors.face.aidl.FaceRevokeChallengeClient client = new com.android.server.biometrics.sensors.face.aidl.FaceRevokeChallengeClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, userId, opPackageName, sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, challenge);
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public long scheduleEnroll(final int sensorId, final android.os.IBinder token, final byte[] hardwareAuthToken, final int userId, final android.hardware.face.IFaceServiceReceiver receiver, final java.lang.String opPackageName, final int[] disabledFeatures, final android.view.Surface previewSurface, final boolean debugConsent, final android.hardware.face.FaceEnrollOptions options) {
        final long id = this.mRequestCounter.incrementAndGet();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleEnroll$7(sensorId, userId, token, receiver, hardwareAuthToken, opPackageName, id, disabledFeatures, previewSurface, debugConsent, options);
            }
        });
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleEnroll$7(int sensorId, int userId, android.os.IBinder token, android.hardware.face.IFaceServiceReceiver receiver, byte[] hardwareAuthToken, java.lang.String opPackageName, long id, int[] disabledFeatures, android.view.Surface previewSurface, boolean debugConsent, android.hardware.face.FaceEnrollOptions options) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        int maxTemplatesPerUser = this.mFaceSensors.get(sensorId).getSensorProperties().maxEnrollmentsPerUser;
        com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient client = new com.android.server.biometrics.sensors.face.aidl.FaceEnrollClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), userId, hardwareAuthToken, opPackageName, id, com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId), disabledFeatures, 75, previewSurface, sensorId, createLogger(1, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, maxTemplatesPerUser, debugConsent, options, this.mAuthenticationStateListeners);
        scheduleForSensor(sensorId, client, this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void cancelEnrollment(final int sensorId, final android.os.IBinder token, final long requestId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelEnrollment$8(sensorId, token, requestId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelEnrollment$8(int sensorId, android.os.IBinder token, long requestId) {
        this.mFaceSensors.get(sensorId).getScheduler().cancelEnrollment(token, requestId);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public long scheduleFaceDetect(final android.os.IBinder token, final com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, final android.hardware.face.FaceAuthenticateOptions options, final int statsClient) {
        final long id = this.mRequestCounter.incrementAndGet();
        final int sensorId = options.getSensorId();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleFaceDetect$9(sensorId, token, id, callback, options, statsClient);
            }
        });
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleFaceDetect$9(int sensorId, android.os.IBinder token, long id, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, android.hardware.face.FaceAuthenticateOptions options, int statsClient) {
        boolean isStrongBiometric = com.android.server.biometrics.Utils.isStrongBiometric(sensorId);
        com.android.server.biometrics.sensors.face.aidl.FaceDetectClient client = new com.android.server.biometrics.sensors.face.aidl.FaceDetectClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, id, callback, options, createLogger(2, statsClient, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mAuthenticationStateListeners, isStrongBiometric);
        scheduleForSensor(sensorId, client, this.mBiometricStateCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelFaceDetect$10(int sensorId, android.os.IBinder token, long requestId) {
        this.mFaceSensors.get(sensorId).getScheduler().cancelAuthenticationOrDetection(token, requestId);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void cancelFaceDetect(final int sensorId, final android.os.IBinder token, final long requestId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelFaceDetect$10(sensorId, token, requestId);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleAuthenticate(final android.os.IBinder token, final long operationId, final int cookie, final com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, final android.hardware.face.FaceAuthenticateOptions options, final long requestId, final boolean restricted, final int statsClient, final boolean allowBackgroundAuthentication) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleAuthenticate$11(options, allowBackgroundAuthentication, token, requestId, callback, operationId, restricted, cookie, statsClient);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAuthenticate$11(android.hardware.face.FaceAuthenticateOptions options, boolean allowBackgroundAuthentication, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, long operationId, boolean restricted, int cookie, int statsClient) {
        boolean mAllowBackgroundAuthentication;
        int userId = options.getUserId();
        int sensorId = options.getSensorId();
        boolean isStrongBiometric = com.android.server.biometrics.Utils.isStrongBiometric(sensorId);
        if (allowBackgroundAuthentication) {
            mAllowBackgroundAuthentication = allowBackgroundAuthentication;
        } else {
            boolean mAllowBackgroundAuthentication2 = getExtImpl().isBackgroundAuthAllow(options.getOpPackageName());
            mAllowBackgroundAuthentication = mAllowBackgroundAuthentication2;
        }
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        this.mFaceSensors.get(sensorId).getLockoutTracker(true);
        com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient client = new com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, requestId, callback, operationId, restricted, options, cookie, false, createLogger(2, statsClient, this.mAuthenticationStatsCollector), this.mBiometricContext, isStrongBiometric, this.mUsageStats, null, mAllowBackgroundAuthentication, com.android.server.biometrics.Utils.getCurrentStrength(sensorId), this.mAuthenticationStateListeners);
        scheduleForSensor(sensorId, client, new com.android.server.biometrics.sensors.face.aidl.FaceProvider.AnonymousClass3(userId, sensorId, requestId, client));
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.face.aidl.FaceProvider$3, reason: invalid class name */
    class AnonymousClass3 implements com.android.server.biometrics.sensors.ClientMonitorCallback {
        final /* synthetic */ com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient val$client;
        final /* synthetic */ long val$requestId;
        final /* synthetic */ int val$sensorId;
        final /* synthetic */ int val$userId;

        AnonymousClass3(int i, int i2, long j, com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient faceAuthenticationClient) {
            this.val$userId = i;
            this.val$sensorId = i2;
            this.val$requestId = j;
            this.val$client = faceAuthenticationClient;
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
            android.os.Handler biometricCallbackHandler = com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mBiometricHandlerProvider.getBiometricCallbackHandler();
            final int i = this.val$userId;
            final int i2 = this.val$sensorId;
            final long j = this.val$requestId;
            biometricCallbackHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$3$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientStarted$0(i, i2, j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClientStarted$0(int userId, int sensorId, long requestId) {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mAuthSessionCoordinator.authStartedFor(userId, sensorId, requestId);
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
            android.os.Handler biometricCallbackHandler = com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mBiometricHandlerProvider.getBiometricCallbackHandler();
            final int i = this.val$userId;
            final int i2 = this.val$sensorId;
            final long j = this.val$requestId;
            final com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient faceAuthenticationClient = this.val$client;
            biometricCallbackHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientFinished$1(i, i2, j, faceAuthenticationClient);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClientFinished$1(int userId, int sensorId, long requestId, com.android.server.biometrics.sensors.face.aidl.FaceAuthenticationClient client) {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.this.mAuthSessionCoordinator.authEndedFor(userId, com.android.server.biometrics.Utils.getCurrentStrength(sensorId), sensorId, requestId, client.wasAuthSuccessful());
        }
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public long scheduleAuthenticate(android.os.IBinder token, long operationId, int cookie, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, android.hardware.face.FaceAuthenticateOptions options, boolean restricted, int statsClient, boolean allowBackgroundAuthentication) {
        long id = this.mRequestCounter.incrementAndGet();
        scheduleAuthenticate(token, operationId, cookie, callback, options, id, restricted, statsClient, allowBackgroundAuthentication);
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAuthentication$12(int sensorId, android.os.IBinder token, long requestId) {
        this.mFaceSensors.get(sensorId).getScheduler().cancelAuthenticationOrDetection(token, requestId);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void cancelAuthentication(final int sensorId, final android.os.IBinder token, final long requestId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelAuthentication$12(sensorId, token, requestId);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleRemove(int sensorId, android.os.IBinder token, int faceId, int userId, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
        scheduleRemoveSpecifiedIds(sensorId, token, new int[]{faceId}, userId, receiver, opPackageName);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleRemoveAll(int sensorId, android.os.IBinder token, int userId, android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
        java.util.List<android.hardware.face.Face> faces = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId);
        int[] faceIds = new int[faces.size()];
        for (int i = 0; i < faces.size(); i++) {
            faceIds[i] = faces.get(i).getBiometricId();
        }
        scheduleRemoveSpecifiedIds(sensorId, token, faceIds, userId, receiver, opPackageName);
    }

    private void scheduleRemoveSpecifiedIds(final int sensorId, final android.os.IBinder token, final int[] faceIds, final int userId, final android.hardware.face.IFaceServiceReceiver receiver, final java.lang.String opPackageName) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRemoveSpecifiedIds$13(sensorId, userId, token, receiver, faceIds, opPackageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRemoveSpecifiedIds$13(int sensorId, int userId, android.os.IBinder token, android.hardware.face.IFaceServiceReceiver receiver, int[] faceIds, java.lang.String opPackageName) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        com.android.server.biometrics.sensors.face.aidl.FaceRemovalClient client = new com.android.server.biometrics.sensors.face.aidl.FaceRemovalClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), faceIds, userId, opPackageName, com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId), sensorId, createLogger(4, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFaceSensors.get(sensorId).getAuthenticatorIds());
        scheduleForSensor(sensorId, client, this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleResetLockout(final int sensorId, final int userId, final byte[] hardwareAuthToken) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleResetLockout$14(sensorId, userId, hardwareAuthToken);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleResetLockout$14(int sensorId, int userId, byte[] hardwareAuthToken) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        com.android.server.biometrics.sensors.face.aidl.FaceResetLockoutClient client = new com.android.server.biometrics.sensors.face.aidl.FaceResetLockoutClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), userId, this.mContext.getOpPackageName(), sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, hardwareAuthToken, this.mFaceSensors.get(sensorId).getLockoutTracker(false), this.mLockoutResetDispatcher, com.android.server.biometrics.Utils.getCurrentStrength(sensorId));
        scheduleForSensor(sensorId, client);
        getExtImpl().resetFaceLockout(hardwareAuthToken);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleSetFeature(final int sensorId, final android.os.IBinder token, final int userId, final int feature, final boolean enabled, final byte[] hardwareAuthToken, final android.hardware.face.IFaceServiceReceiver receiver, java.lang.String opPackageName) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleSetFeature$15(sensorId, userId, token, receiver, feature, enabled, hardwareAuthToken);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleSetFeature$15(int sensorId, int userId, android.os.IBinder token, android.hardware.face.IFaceServiceReceiver receiver, int feature, boolean enabled, byte[] hardwareAuthToken) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        java.util.List<android.hardware.face.Face> faces = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId);
        if (faces.isEmpty()) {
            android.util.Slog.w(getTag(), "Ignoring setFeature, no templates enrolled for user: " + userId);
        } else {
            com.android.server.biometrics.sensors.face.aidl.FaceSetFeatureClient client = new com.android.server.biometrics.sensors.face.aidl.FaceSetFeatureClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), userId, this.mContext.getOpPackageName(), sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, feature, enabled, hardwareAuthToken);
            scheduleForSensor(sensorId, client);
        }
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleGetFeature(final int sensorId, final android.os.IBinder token, final int userId, final int feature, final com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, java.lang.String opPackageName) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleGetFeature$16(sensorId, userId, token, callback, feature);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGetFeature$16(int sensorId, int userId, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, int feature) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        java.util.List<android.hardware.face.Face> faces = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId);
        if (faces.isEmpty()) {
            android.util.Slog.w(getTag(), "Ignoring getFeature, no templates enrolled for user: " + userId);
        } else {
            com.android.server.biometrics.sensors.face.aidl.FaceGetFeatureClient client = new com.android.server.biometrics.sensors.face.aidl.FaceGetFeatureClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), token, callback, userId, this.mContext.getOpPackageName(), sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, feature);
            scheduleForSensor(sensorId, client);
        }
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void startPreparedClient(final int sensorId, final int cookie) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startPreparedClient$17(sensorId, cookie);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPreparedClient$17(int sensorId, int cookie) {
        this.mFaceSensors.get(sensorId).getScheduler().startPreparedClient(cookie);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleInternalCleanup(int sensorId, int userId, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        scheduleInternalCleanup(sensorId, userId, callback, false);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleInternalCleanup(final int sensorId, final int userId, final com.android.server.biometrics.sensors.ClientMonitorCallback callback, final boolean favorHalEnrollments) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInternalCleanup$18(sensorId, userId, favorHalEnrollments, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInternalCleanup$18(int sensorId, int userId, boolean favorHalEnrollments, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        this.mFaceSensors.get(sensorId).scheduleFaceUpdateActiveUserClient(userId);
        com.android.server.biometrics.sensors.face.aidl.FaceInternalCleanupClient client = new com.android.server.biometrics.sensors.face.aidl.FaceInternalCleanupClient(this.mContext, this.mFaceSensors.get(sensorId).getLazySession(), userId, this.mContext.getOpPackageName(), sensorId, createLogger(3, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId), this.mFaceSensors.get(sensorId).getAuthenticatorIds());
        if (favorHalEnrollments) {
            client.setFavorHalEnrollments();
        }
        scheduleForSensor(sensorId, client, new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(callback, this.mBiometricStateCallback));
    }

    private com.android.server.biometrics.log.BiometricLogger createLogger(int statsAction, int statsClient, com.android.server.biometrics.AuthenticationStatsCollector authenticationStatsCollector) {
        return new com.android.server.biometrics.log.BiometricLogger(this.mContext, 4, statsAction, statsClient, authenticationStatsCollector);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoState(int sensorId, android.util.proto.ProtoOutputStream proto, boolean clearSchedulerBuffer) {
        if (this.mFaceSensors.contains(sensorId)) {
            this.mFaceSensors.get(sensorId).dumpProtoState(sensorId, proto, clearSchedulerBuffer);
        }
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoMetrics(int sensorId, java.io.FileDescriptor fd) {
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpInternal(int sensorId, java.io.PrintWriter printWriter) {
        com.android.server.biometrics.sensors.PerformanceTracker performanceTracker = com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(sensorId);
        org.json.JSONObject jSONObject = new org.json.JSONObject();
        try {
            jSONObject.put(com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, getTag());
            org.json.JSONArray sets = new org.json.JSONArray();
            for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getUsers()) {
                int userId = user.getUserHandle().getIdentifier();
                int c = com.android.server.biometrics.sensors.face.FaceUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId).size();
                org.json.JSONObject set = new org.json.JSONObject();
                set.put("id", userId);
                set.put(com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, c);
                set.put("accept", performanceTracker.getAcceptForUser(userId));
                set.put("reject", performanceTracker.getRejectForUser(userId));
                set.put("acquire", performanceTracker.getAcquireForUser(userId));
                set.put("lockout", performanceTracker.getTimedLockoutForUser(userId));
                set.put("permanentLockout", performanceTracker.getPermanentLockoutForUser(userId));
                set.put("acceptCrypto", performanceTracker.getAcceptCryptoForUser(userId));
                set.put("rejectCrypto", performanceTracker.getRejectCryptoForUser(userId));
                set.put("acquireCrypto", performanceTracker.getAcquireCryptoForUser(userId));
                sets.put(set);
            }
            jSONObject.put("prints", sets);
        } catch (org.json.JSONException e) {
            android.util.Slog.e(getTag(), "dump formatting failure", e);
        }
        printWriter.println(jSONObject);
        printWriter.println("HAL deaths since last reboot: " + performanceTracker.getHALDeathCount());
        printWriter.println("---AuthSessionCoordinator logs begin---");
        printWriter.println(this.mBiometricContext.getAuthSessionCoordinator());
        printWriter.println("---AuthSessionCoordinator logs end  ---");
        this.mFaceSensors.get(sensorId).getScheduler().dump(printWriter);
        this.mUsageStats.print(printWriter);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public android.hardware.biometrics.ITestSession createTestSession(int sensorId, android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) {
        return this.mFaceSensors.get(sensorId).createTestSession(callback);
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void dumpHal(int sensorId, java.io.FileDescriptor fd, java.lang.String[] args) {
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.e(getTag(), "HAL died");
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$binderDied$19();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$binderDied$19() {
        this.mDaemon = null;
        for (int i = 0; i < this.mFaceSensors.size(); i++) {
            com.android.server.biometrics.sensors.face.aidl.Sensor sensor = this.mFaceSensors.valueAt(i);
            int sensorId = this.mFaceSensors.keyAt(i);
            com.android.server.biometrics.sensors.PerformanceTracker performanceTracker = com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(sensorId);
            if (performanceTracker != null) {
                performanceTracker.incrementHALDeathCount();
            } else {
                android.util.Slog.w(getTag(), "Performance tracker is null. Not counting HAL death.");
            }
            sensor.onBinderDied();
        }
    }

    void setTestHalEnabled(boolean enabled) {
        this.mTestHalEnabled = enabled;
    }

    public boolean getTestHalEnabled() {
        return this.mTestHalEnabled;
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public void scheduleWatchdog(int sensorId) {
        android.util.Slog.d(getTag(), "Starting watchdog for face");
        com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.face.IFace, android.hardware.biometrics.face.ISession> scheduler = this.mFaceSensors.get(sensorId).getScheduler();
        if (scheduler == null) {
            return;
        }
        scheduler.startWatchdog();
    }

    public void handleOnFaceCmd(int cmdId, java.util.ArrayList<java.lang.Byte> result, int resultLen) {
        getExtImpl().handleOnFaceCmd(cmdId, result, resultLen);
    }

    public static com.android.server.biometrics.sensors.face.aidl.IFaceProviderExt getExtImpl() {
        if (sOplusServiceProvider == null) {
            return new com.android.server.biometrics.sensors.face.aidl.IFaceProviderExt() { // from class: com.android.server.biometrics.sensors.face.aidl.FaceProvider.4
            };
        }
        return sOplusServiceProvider;
    }

    @Override // com.android.server.biometrics.sensors.face.ServiceProvider
    public com.android.server.biometrics.sensors.face.IServiceProviderWrapper getServiceProviderWrapper() {
        return this.mServiceProviderWrapper;
    }

    private class OplusFaceProviderWrapper implements com.android.server.biometrics.sensors.face.IServiceProviderWrapper {
        private OplusFaceProviderWrapper() {
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public long getLockoutAttemptDeadline(int userId) {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().getLockoutAttemptDeadline(userId);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int getFailedAttempts() {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().getFailedAttempts();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void resetFaceDaemon() {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().resetFaceDaemon();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int getFaceProcessMemory() {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().getFaceProcessMemory();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int scheduleSendFaceCmd(int sensorId, int cmdId, byte[] inbuf) {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().scheduleSendFaceCmd(sensorId, cmdId, inbuf);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void authPreOperation(android.os.IBinder token, java.lang.String opPackageName) {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().authPreOperation(token, opPackageName);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onSystemReady() {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().onSystemReady();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void dumpInternal(java.io.PrintWriter pw, java.lang.String[] args) {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().dumpInternal(pw, args);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onAuthenticated(boolean authenticated) {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().onAuthenticated(authenticated);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onError(int error, int vendorCode) {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().onError(error, vendorCode);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public boolean onAcquired(int acquireInfo, int vendorCode) {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().onAcquired(acquireInfo, vendorCode);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onLockoutTimed() {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().onLockoutTimed();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public void onLockoutPermanent() {
            com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().onLockoutPermanent();
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int regsiterFaceCmdCallback(android.hardware.face.IFaceCommandCallback callback) {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().regsiterFaceCmdCallback(callback);
        }

        @Override // com.android.server.biometrics.sensors.face.IServiceProviderWrapper
        public int unregsiterFaceCmdCallback(android.hardware.face.IFaceCommandCallback callback) {
            return com.android.server.biometrics.sensors.face.aidl.FaceProvider.getExtImpl().unregsiterFaceCmdCallback(callback);
        }
    }

    public void sendFaceReEnrollNotification() {
        this.mAuthenticationStatsCollector.sendFaceReEnrollNotification();
    }
}
