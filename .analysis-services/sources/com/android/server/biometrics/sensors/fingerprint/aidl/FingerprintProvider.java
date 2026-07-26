package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintProvider implements android.os.IBinder.DeathRecipient, com.android.server.biometrics.sensors.fingerprint.ServiceProvider {
    private static final boolean AIDL_SUPPORT;
    private static final java.lang.String TAG = "FingerprintProvider";
    public static com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintServiceProviderExt mFingerprintProviderExt = (com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintServiceProviderExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintServiceProviderExt.class).create();
    private final android.app.ActivityTaskManager mActivityTaskManager;
    private final com.android.server.biometrics.sensors.AuthSessionCoordinator mAuthSessionCoordinator;
    private final com.android.server.biometrics.sensors.AuthenticationStateListeners mAuthenticationStateListeners;
    private com.android.server.biometrics.AuthenticationStatsCollector mAuthenticationStatsCollector;
    private final com.android.server.biometrics.log.BiometricContext mBiometricContext;
    private final com.android.server.biometrics.BiometricHandlerProvider mBiometricHandlerProvider;
    private final com.android.server.biometrics.sensors.BiometricStateCallback mBiometricStateCallback;
    private final android.content.Context mContext;
    private android.hardware.biometrics.fingerprint.IFingerprint mDaemon;
    final com.android.server.biometrics.sensors.SensorList<com.android.server.biometrics.sensors.fingerprint.aidl.Sensor> mFingerprintSensors;
    private final java.lang.String mHalInstanceName;
    private java.lang.String mHalInstanceNameCurrent;
    private final android.os.Handler mHandler;
    private final com.android.server.biometrics.sensors.LockoutResetDispatcher mLockoutResetDispatcher;
    private final java.util.concurrent.atomic.AtomicLong mRequestCounter;
    private final com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.BiometricTaskStackListener mTaskStackListener;
    private boolean mTestHalEnabled;
    private android.hardware.fingerprint.IUdfpsOverlayController mUdfpsOverlayController;
    private android.hardware.biometrics.fingerprint.IVirtualHal mVhal;

    static {
        AIDL_SUPPORT = "1".equals(android.os.SystemProperties.get("vendor.fingerprint.aidl.support", "0")) || "vsoc_arm64".equals(android.os.SystemProperties.get("ro.soc.model"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class BiometricTaskStackListener extends android.app.TaskStackListener {
        private BiometricTaskStackListener() {
        }

        public void onTaskStackChanged() {
            com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$BiometricTaskStackListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTaskStackChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskStackChanged$0() {
            for (int i = 0; i < com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mFingerprintSensors.size(); i++) {
                com.android.server.biometrics.sensors.BaseClientMonitor client = com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mFingerprintSensors.valueAt(i).getScheduler().getCurrentClient();
                if (!(client instanceof com.android.server.biometrics.sensors.AuthenticationClient)) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.getTag(), "Task stack changed for client: " + client);
                } else if (!com.android.server.biometrics.Utils.isKeyguard(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mContext, client.getOwnerString()) && !com.android.server.biometrics.Utils.isSystem(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mContext, client.getOwnerString()) && com.android.server.biometrics.Utils.isBackground(client.getOwnerString()) && !client.isAlreadyDone()) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.getTag(), "Stopping background authentication, currentClient: " + client);
                    com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mFingerprintSensors.valueAt(i).getScheduler().cancelAuthenticationOrDetection(client.getToken(), client.getRequestId());
                }
            }
        }
    }

    public FingerprintProvider(android.content.Context context, com.android.server.biometrics.sensors.BiometricStateCallback biometricStateCallback, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, android.hardware.biometrics.fingerprint.SensorProps[] props, java.lang.String halInstanceName, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, boolean resetLockoutRequiresHardwareAuthToken) {
        this(context, biometricStateCallback, authenticationStateListeners, props, halInstanceName, lockoutResetDispatcher, gestureAvailabilityDispatcher, biometricContext, null, com.android.server.biometrics.BiometricHandlerProvider.getInstance(), resetLockoutRequiresHardwareAuthToken, false);
    }

    FingerprintProvider(android.content.Context context, com.android.server.biometrics.sensors.BiometricStateCallback biometricStateCallback, com.android.server.biometrics.sensors.AuthenticationStateListeners authenticationStateListeners, android.hardware.biometrics.fingerprint.SensorProps[] props, java.lang.String halInstanceName, com.android.server.biometrics.sensors.LockoutResetDispatcher lockoutResetDispatcher, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, com.android.server.biometrics.log.BiometricContext biometricContext, android.hardware.biometrics.fingerprint.IFingerprint daemon, com.android.server.biometrics.BiometricHandlerProvider biometricHandlerProvider, boolean resetLockoutRequiresHardwareAuthToken, boolean testHalEnabled) {
        this.mRequestCounter = new java.util.concurrent.atomic.AtomicLong(0L);
        this.mContext = context;
        this.mBiometricStateCallback = biometricStateCallback;
        this.mAuthenticationStateListeners = authenticationStateListeners;
        this.mHalInstanceName = halInstanceName;
        this.mFingerprintSensors = new com.android.server.biometrics.sensors.SensorList<>(android.app.ActivityManager.getService());
        this.mHandler = biometricHandlerProvider.getFingerprintHandler();
        this.mLockoutResetDispatcher = lockoutResetDispatcher;
        this.mActivityTaskManager = android.app.ActivityTaskManager.getInstance();
        this.mTaskStackListener = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.BiometricTaskStackListener();
        this.mBiometricContext = biometricContext;
        this.mAuthSessionCoordinator = this.mBiometricContext.getAuthSessionCoordinator();
        this.mDaemon = daemon;
        this.mTestHalEnabled = testHalEnabled;
        this.mBiometricHandlerProvider = biometricHandlerProvider;
        initAuthenticationBroadcastReceiver();
        initFingerprintDanglingBroadcastReceiver();
        initSensors(resetLockoutRequiresHardwareAuthToken, props, gestureAvailabilityDispatcher);
    }

    private void initAuthenticationBroadcastReceiver() {
        new com.android.server.biometrics.AuthenticationStatsBroadcastReceiver(this.mContext, 1, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda3
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

    private void initFingerprintDanglingBroadcastReceiver() {
        new com.android.server.biometrics.BiometricDanglingReceiver(this.mContext, 1);
    }

    private void initSensors(boolean resetLockoutRequiresHardwareAuthToken, android.hardware.biometrics.fingerprint.SensorProps[] props, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher) {
        int i = 0;
        if (!resetLockoutRequiresHardwareAuthToken) {
            android.util.Slog.d(getTag(), "Adding HIDL configs");
            int length = props.length;
            while (i < length) {
                android.hardware.biometrics.fingerprint.SensorProps sensorConfig = props[i];
                addHidlSensors(sensorConfig, gestureAvailabilityDispatcher, resetLockoutRequiresHardwareAuthToken);
                i++;
            }
        } else {
            android.util.Slog.d(getTag(), "Adding AIDL configs");
            java.util.List<android.hardware.biometrics.SensorLocationInternal> workaroundLocations = getWorkaroundSensorProps(this.mContext);
            int length2 = props.length;
            while (i < length2) {
                android.hardware.biometrics.fingerprint.SensorProps prop = props[i];
                addAidlSensors(prop, gestureAvailabilityDispatcher, workaroundLocations, resetLockoutRequiresHardwareAuthToken);
                i++;
            }
        }
        mFingerprintProviderExt.init(this.mContext, this.mHalInstanceName, this.mHandler, this.mFingerprintSensors, this.mBiometricContext, this);
    }

    private void addHidlSensors(android.hardware.biometrics.fingerprint.SensorProps prop, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, boolean resetLockoutRequiresHardwareAuthToken) {
        final int sensorId = prop.commonProps.sensorId;
        com.android.server.biometrics.sensors.fingerprint.aidl.Sensor sensor = new com.android.server.biometrics.sensors.fingerprint.hidl.HidlToAidlSensorAdapter(this, this.mContext, this.mHandler, prop, this.mLockoutResetDispatcher, this.mBiometricContext, resetLockoutRequiresHardwareAuthToken, new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$addHidlSensors$1(sensorId);
            }
        });
        sensor.init(gestureAvailabilityDispatcher, this.mLockoutResetDispatcher);
        int sessionUserId = sensor.getLazySession().get() == null ? -10000 : sensor.getLazySession().get().getUserId();
        this.mFingerprintSensors.addSensor(sensorId, sensor, sessionUserId, new android.app.SynchronousUserSwitchObserver() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.1
            public void onUserSwitching(int newUserId) {
                com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.scheduleInternalCleanup(sensorId, newUserId, null);
            }
        });
        android.util.Slog.d(getTag(), "Added: " + this.mFingerprintSensors.get(sensorId).toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addHidlSensors$1(int sensorId) {
        scheduleInternalCleanup(sensorId, android.app.ActivityManager.getCurrentUser(), null);
    }

    private void addAidlSensors(android.hardware.biometrics.fingerprint.SensorProps prop, com.android.server.biometrics.sensors.fingerprint.GestureAvailabilityDispatcher gestureAvailabilityDispatcher, java.util.List<android.hardware.biometrics.SensorLocationInternal> workaroundLocations, boolean resetLockoutRequiresHardwareAuthToken) {
        final int sensorId = prop.commonProps.sensorId;
        com.android.server.biometrics.sensors.fingerprint.aidl.Sensor sensor = new com.android.server.biometrics.sensors.fingerprint.aidl.Sensor(this, this.mContext, this.mHandler, prop, this.mBiometricContext, workaroundLocations, resetLockoutRequiresHardwareAuthToken);
        sensor.init(gestureAvailabilityDispatcher, this.mLockoutResetDispatcher);
        int sessionUserId = sensor.getLazySession().get() == null ? -10000 : sensor.getLazySession().get().getUserId();
        this.mFingerprintSensors.addSensor(sensorId, sensor, sessionUserId, new android.app.SynchronousUserSwitchObserver() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.2
            public void onUserSwitching(int newUserId) {
                com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.scheduleInternalCleanup(sensorId, newUserId, null);
            }
        });
        android.util.Slog.d(getTag(), "Added: " + this.mFingerprintSensors.get(sensorId).toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getTag() {
        return "FingerprintProvider/" + this.mHalInstanceName;
    }

    boolean hasHalInstance() {
        return this.mTestHalEnabled || android.os.ServiceManager.checkService(new java.lang.StringBuilder().append(android.hardware.biometrics.fingerprint.IFingerprint.DESCRIPTOR).append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER).append(this.mHalInstanceName).toString()) != null;
    }

    synchronized android.hardware.biometrics.fingerprint.IFingerprint getHalInstance() {
        if (this.mTestHalEnabled) {
            if (com.android.server.biometrics.Flags.useVhalForTesting()) {
                if (!this.mHalInstanceNameCurrent.contains("virtual")) {
                    android.util.Slog.i(getTag(), "Switching fingerprint hal from " + this.mHalInstanceName + " to virtual hal");
                    this.mHalInstanceNameCurrent = "virtual";
                    this.mDaemon = null;
                }
            } else {
                return new com.android.server.biometrics.sensors.fingerprint.aidl.TestHal();
            }
        } else if (this.mHalInstanceNameCurrent == null) {
            this.mHalInstanceNameCurrent = this.mHalInstanceName;
        } else if (this.mHalInstanceNameCurrent.contains("virtual") && this.mHalInstanceNameCurrent != this.mHalInstanceName) {
            android.util.Slog.i(getTag(), "Switching fingerprint from virtual hal to " + this.mHalInstanceName);
            this.mHalInstanceNameCurrent = this.mHalInstanceName;
            this.mDaemon = null;
        }
        if (this.mDaemon != null) {
            if (!mFingerprintProviderExt.isSideFingerprintInitialized()) {
                mFingerprintProviderExt.notifyHalReady();
            }
            return this.mDaemon;
        }
        android.util.Slog.d(getTag(), "Daemon was null, reconnecting");
        this.mDaemon = android.hardware.biometrics.fingerprint.IFingerprint.Stub.asInterface(android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService(android.hardware.biometrics.fingerprint.IFingerprint.DESCRIPTOR + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mHalInstanceNameCurrent)));
        if (this.mDaemon == null) {
            android.util.Slog.e(getTag(), "Unable to get daemon");
            return null;
        }
        try {
            this.mDaemon.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(getTag(), "Unable to linkToDeath", e);
        }
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            int sensorId = this.mFingerprintSensors.keyAt(i);
            scheduleLoadAuthenticatorIds(sensorId);
            scheduleInternalCleanup(sensorId, android.app.ActivityManager.getCurrentUser(), null);
        }
        if (mFingerprintProviderExt != null) {
            mFingerprintProviderExt.notifyHalReady();
        }
        if (android.os.Build.isDebuggable()) {
            com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> utils = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(this.mFingerprintSensors.keyAt(0));
            for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getAliveUsers()) {
                java.util.List<android.hardware.fingerprint.Fingerprint> enrollments = utils.getBiometricsForUser(this.mContext, user.id);
                android.util.Slog.d(getTag(), "Expecting enrollments for user " + user.id + ": " + enrollments.stream().map(new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda14
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return java.lang.Integer.valueOf(((android.hardware.fingerprint.Fingerprint) obj).getBiometricId());
                    }
                }).toList());
            }
        }
        return this.mDaemon;
    }

    private void scheduleForSensor(int sensorId, com.android.server.biometrics.sensors.BaseClientMonitor client) {
        if (!this.mFingerprintSensors.contains(sensorId)) {
            throw new java.lang.IllegalStateException("Unable to schedule client: " + client + " for sensor: " + sensorId);
        }
        this.mFingerprintSensors.get(sensorId).getScheduler().scheduleClientMonitor(client);
    }

    private void scheduleForSensor(int sensorId, com.android.server.biometrics.sensors.BaseClientMonitor client, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        if (!this.mFingerprintSensors.contains(sensorId)) {
            throw new java.lang.IllegalStateException("Unable to schedule client: " + client + " for sensor: " + sensorId);
        }
        this.mFingerprintSensors.get(sensorId).getScheduler().scheduleClientMonitor(client, callback);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean containsSensor(int sensorId) {
        return this.mFingerprintSensors.contains(sensorId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> getSensorProperties() {
        java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> props = new java.util.ArrayList<>();
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            props.add(this.mFingerprintSensors.valueAt(i).getSensorProperties());
        }
        return props;
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public android.hardware.fingerprint.FingerprintSensorPropertiesInternal getSensorProperties(int sensorId) {
        if (this.mFingerprintSensors.size() == 0) {
            return null;
        }
        if (sensorId == -1) {
            return this.mFingerprintSensors.valueAt(0).getSensorProperties();
        }
        com.android.server.biometrics.sensors.fingerprint.aidl.Sensor sensor = this.mFingerprintSensors.get(sensorId);
        if (sensor != null) {
            return sensor.getSensorProperties();
        }
        return null;
    }

    private void scheduleLoadAuthenticatorIds(int sensorId) {
        for (android.content.pm.UserInfo user : android.os.UserManager.get(this.mContext).getAliveUsers()) {
            scheduleLoadAuthenticatorIdsForUser(sensorId, user.id);
        }
    }

    protected void scheduleLoadAuthenticatorIdsForUser(final int sensorId, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleLoadAuthenticatorIdsForUser$2(sensorId, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLoadAuthenticatorIdsForUser$2(int sensorId, int userId) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGetAuthenticatorIdClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGetAuthenticatorIdClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), userId, this.mContext.getOpPackageName(), sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFingerprintSensors.get(sensorId).getAuthenticatorIds());
        scheduleForSensor(sensorId, client);
    }

    void scheduleInvalidationRequest(final int sensorId, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInvalidationRequest$3(userId, sensorId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidationRequest$3(int userId, int sensorId) {
        com.android.server.biometrics.sensors.InvalidationRequesterClient<android.hardware.fingerprint.Fingerprint> client = new com.android.server.biometrics.sensors.InvalidationRequesterClient<>(this.mContext, userId, sensorId, com.android.server.biometrics.log.BiometricLogger.ofUnknown(this.mContext), this.mBiometricContext, com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId));
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleResetLockout(final int sensorId, final int userId, final byte[] hardwareAuthToken) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleResetLockout$4(sensorId, userId, hardwareAuthToken);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleResetLockout$4(int sensorId, int userId, byte[] hardwareAuthToken) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintResetLockoutClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintResetLockoutClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), userId, this.mContext.getOpPackageName(), sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, hardwareAuthToken, this.mFingerprintSensors.get(sensorId).getLockoutTracker(false), this.mLockoutResetDispatcher, com.android.server.biometrics.Utils.getCurrentStrength(sensorId));
        scheduleForSensor(sensorId, client);
        mFingerprintProviderExt.resetFingerprintLockout(hardwareAuthToken, userId);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleGenerateChallenge(final int sensorId, final int userId, final android.os.IBinder token, final android.hardware.fingerprint.IFingerprintServiceReceiver receiver, final java.lang.String opPackageName) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleGenerateChallenge$5(sensorId, token, receiver, userId, opPackageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGenerateChallenge$5(int sensorId, android.os.IBinder token, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, int userId, java.lang.String opPackageName) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGenerateChallengeClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintGenerateChallengeClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), userId, opPackageName, sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext);
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleRevokeChallenge(final int sensorId, final int userId, final android.os.IBinder token, final java.lang.String opPackageName, final long challenge) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRevokeChallenge$6(sensorId, token, userId, opPackageName, challenge);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRevokeChallenge$6(int sensorId, android.os.IBinder token, int userId, java.lang.String opPackageName, long challenge) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRevokeChallengeClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRevokeChallengeClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), token, userId, opPackageName, sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, challenge);
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public long scheduleEnroll(final int sensorId, final android.os.IBinder token, final byte[] hardwareAuthToken, final int userId, final android.hardware.fingerprint.IFingerprintServiceReceiver receiver, final java.lang.String opPackageName, final int enrollReason, final android.hardware.fingerprint.FingerprintEnrollOptions options) {
        final long id = this.mRequestCounter.incrementAndGet();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleEnroll$7(sensorId, token, id, receiver, userId, hardwareAuthToken, opPackageName, enrollReason, options);
            }
        });
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleEnroll$7(int sensorId, android.os.IBinder token, long id, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, int userId, byte[] hardwareAuthToken, java.lang.String opPackageName, int enrollReason, android.hardware.fingerprint.FingerprintEnrollOptions options) {
        int maxTemplatesPerUser = this.mFingerprintSensors.get(sensorId).getSensorProperties().maxEnrollmentsPerUser;
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintEnrollClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), token, id, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), userId, hardwareAuthToken, opPackageName, com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId), sensorId, createLogger(1, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFingerprintSensors.get(sensorId).getSensorProperties(), this.mUdfpsOverlayController, this.mAuthenticationStateListeners, maxTemplatesPerUser, enrollReason, options);
        scheduleForSensor(sensorId, client, this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void cancelEnrollment(final int sensorId, final android.os.IBinder token, final long requestId) {
        mFingerprintProviderExt.handleCancelEnrollment(sensorId, token);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelEnrollment$8(sensorId, token, requestId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelEnrollment$8(int sensorId, android.os.IBinder token, long requestId) {
        this.mFingerprintSensors.get(sensorId).getScheduler().cancelEnrollment(token, requestId);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public long scheduleFingerDetect(final android.os.IBinder token, final com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, final android.hardware.fingerprint.FingerprintAuthenticateOptions options, final int statsClient) {
        final long id = this.mRequestCounter.incrementAndGet();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleFingerDetect$9(options, token, id, callback, statsClient);
            }
        });
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleFingerDetect$9(android.hardware.fingerprint.FingerprintAuthenticateOptions options, android.os.IBinder token, long id, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, int statsClient) {
        int sensorId = options.getSensorId();
        boolean isStrongBiometric = com.android.server.biometrics.Utils.isStrongBiometric(sensorId);
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintDetectClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), token, id, callback, options, createLogger(2, statsClient, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mAuthenticationStateListeners, this.mUdfpsOverlayController, isStrongBiometric);
        scheduleForSensor(sensorId, client, this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleAuthenticate(final android.os.IBinder token, final long operationId, final int cookie, final com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, final android.hardware.fingerprint.FingerprintAuthenticateOptions options, final long requestId, final boolean restricted, final int statsClient, final boolean allowBackgroundAuthentication) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleAuthenticate$10(options, token, requestId, callback, operationId, restricted, cookie, statsClient, allowBackgroundAuthentication);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAuthenticate$10(android.hardware.fingerprint.FingerprintAuthenticateOptions options, android.os.IBinder token, long requestId, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, long operationId, boolean restricted, int cookie, int statsClient, boolean allowBackgroundAuthentication) {
        int userId = options.getUserId();
        int sensorId = options.getSensorId();
        boolean isStrongBiometric = com.android.server.biometrics.Utils.isStrongBiometric(sensorId);
        com.android.server.biometrics.sensors.LockoutTracker lockoutTracker = this.mFingerprintSensors.get(sensorId).getLockoutTracker(true);
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintAuthenticationClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), token, requestId, callback, operationId, restricted, options, cookie, false, createLogger(2, statsClient, this.mAuthenticationStatsCollector), this.mBiometricContext, isStrongBiometric, this.mTaskStackListener, this.mUdfpsOverlayController, this.mAuthenticationStateListeners, allowBackgroundAuthentication, this.mFingerprintSensors.get(sensorId).getSensorProperties(), com.android.server.biometrics.Utils.getCurrentStrength(sensorId), lockoutTracker);
        scheduleForSensor(sensorId, client, new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.AnonymousClass3(userId, sensorId, requestId));
    }

    /* JADX INFO: renamed from: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$3, reason: invalid class name */
    class AnonymousClass3 implements com.android.server.biometrics.sensors.ClientMonitorCallback {
        final /* synthetic */ long val$requestId;
        final /* synthetic */ int val$sensorId;
        final /* synthetic */ int val$userId;

        AnonymousClass3(int i, int i2, long j) {
            this.val$userId = i;
            this.val$sensorId = i2;
            this.val$requestId = j;
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
            com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mBiometricStateCallback.onClientStarted(clientMonitor);
            android.os.Handler biometricCallbackHandler = com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mBiometricHandlerProvider.getBiometricCallbackHandler();
            final int i = this.val$userId;
            final int i2 = this.val$sensorId;
            final long j = this.val$requestId;
            biometricCallbackHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientStarted$0(i, i2, j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClientStarted$0(int userId, int sensorId, long requestId) {
            com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mAuthSessionCoordinator.authStartedFor(userId, sensorId, requestId);
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onBiometricAction(int action) {
            com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mBiometricStateCallback.onBiometricAction(action);
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, final boolean success) {
            com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mBiometricStateCallback.onClientFinished(clientMonitor, success);
            android.os.Handler biometricCallbackHandler = com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mBiometricHandlerProvider.getBiometricCallbackHandler();
            final int i = this.val$userId;
            final int i2 = this.val$sensorId;
            final long j = this.val$requestId;
            biometricCallbackHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$3$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientFinished$1(i, i2, j, success);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClientFinished$1(int userId, int sensorId, long requestId, boolean success) {
            com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider.this.mAuthSessionCoordinator.authEndedFor(userId, com.android.server.biometrics.Utils.getCurrentStrength(sensorId), sensorId, requestId, success);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public long scheduleAuthenticate(android.os.IBinder token, long operationId, int cookie, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter callback, android.hardware.fingerprint.FingerprintAuthenticateOptions options, boolean restricted, int statsClient, boolean allowBackgroundAuthentication) {
        long id = this.mRequestCounter.incrementAndGet();
        scheduleAuthenticate(token, operationId, cookie, callback, options, id, restricted, statsClient, allowBackgroundAuthentication);
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPreparedClient$11(int sensorId, int cookie) {
        this.mFingerprintSensors.get(sensorId).getScheduler().startPreparedClient(cookie);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void startPreparedClient(final int sensorId, final int cookie) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startPreparedClient$11(sensorId, cookie);
            }
        });
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void cancelAuthentication(final int sensorId, final android.os.IBinder token, final long requestId) {
        android.util.Slog.d(getTag(), "[cancelAuthentication] sensorId: " + sensorId + " ,token: " + token + " ,requestId: " + requestId);
        mFingerprintProviderExt.handleCancelAuthentication(sensorId, token);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelAuthentication$12(sensorId, token, requestId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAuthentication$12(int sensorId, android.os.IBinder token, long requestId) {
        this.mFingerprintSensors.get(sensorId).getScheduler().cancelAuthenticationOrDetection(token, requestId);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleRemove(int sensorId, android.os.IBinder token, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, int fingerId, int userId, java.lang.String opPackageName) {
        scheduleRemoveSpecifiedIds(sensorId, token, new int[]{fingerId}, userId, receiver, opPackageName);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleRemoveAll(int sensorId, android.os.IBinder token, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, int userId, java.lang.String opPackageName) {
        java.util.List<android.hardware.fingerprint.Fingerprint> fingers = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId);
        int[] fingerIds = new int[fingers.size()];
        for (int i = 0; i < fingers.size(); i++) {
            fingerIds[i] = fingers.get(i).getBiometricId();
        }
        scheduleRemoveSpecifiedIds(sensorId, token, fingerIds, userId, receiver, opPackageName);
    }

    private void scheduleRemoveSpecifiedIds(final int sensorId, final android.os.IBinder token, final int[] fingerprintIds, final int userId, final android.hardware.fingerprint.IFingerprintServiceReceiver receiver, final java.lang.String opPackageName) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRemoveSpecifiedIds$13(opPackageName, userId, sensorId, token, receiver, fingerprintIds);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRemoveSpecifiedIds$13(java.lang.String opPackageName, int userId, int sensorId, android.os.IBinder token, android.hardware.fingerprint.IFingerprintServiceReceiver receiver, int[] fingerprintIds) {
        android.util.Slog.d(getTag(), "[scheduleRemoveSpecifiedIds] opPackageName:" + opPackageName + " userId:" + userId);
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRemovalClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRemovalClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), token, new com.android.server.biometrics.sensors.ClientMonitorCallbackConverter(receiver), fingerprintIds, userId, opPackageName, com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId), sensorId, createLogger(4, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFingerprintSensors.get(sensorId).getAuthenticatorIds());
        scheduleForSensor(sensorId, client, this.mBiometricStateCallback);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleInternalCleanup(int sensorId, int userId, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        scheduleInternalCleanup(sensorId, userId, callback, false);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleInternalCleanup(final int sensorId, final int userId, final com.android.server.biometrics.sensors.ClientMonitorCallback callback, final boolean favorHalEnrollments) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInternalCleanup$14(sensorId, userId, favorHalEnrollments, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInternalCleanup$14(int sensorId, int userId, boolean favorHalEnrollments, com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInternalCleanupClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInternalCleanupClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), userId, this.mContext.getOpPackageName(), sensorId, createLogger(3, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId), this.mFingerprintSensors.get(sensorId).getAuthenticatorIds());
        if (favorHalEnrollments) {
            client.setFavorHalEnrollments();
        }
        scheduleForSensor(sensorId, client, new com.android.server.biometrics.sensors.ClientMonitorCompositeCallback(callback, this.mBiometricStateCallback));
    }

    private com.android.server.biometrics.log.BiometricLogger createLogger(int statsAction, int statsClient, com.android.server.biometrics.AuthenticationStatsCollector authenticationStatsCollector) {
        return new com.android.server.biometrics.log.BiometricLogger(this.mContext, 1, statsAction, statsClient, authenticationStatsCollector);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean isHardwareDetected(int sensorId) {
        return this.mFingerprintSensors.get(sensorId).isHardwareDetected(this.mHalInstanceName);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void rename(int sensorId, int fingerId, int userId, java.lang.String name) {
        com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId).renameBiometricForUser(this.mContext, userId, fingerId, name);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public java.util.List<android.hardware.fingerprint.Fingerprint> getEnrolledFingerprints(int sensorId, int userId) {
        return com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public boolean hasEnrollments(int sensorId, int userId) {
        return !getEnrolledFingerprints(sensorId, userId).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleInvalidateAuthenticatorId(final int sensorId, final int userId, final android.hardware.biometrics.IInvalidationCallback callback) {
        if (!AIDL_SUPPORT) {
            try {
                callback.onCompleted();
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to complete InvalidateAuthenticatorId: " + e.getMessage());
                return;
            }
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInvalidateAuthenticatorId$15(sensorId, userId, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInvalidateAuthenticatorId$15(int sensorId, int userId, android.hardware.biometrics.IInvalidationCallback callback) {
        com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInvalidationClient client = new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInvalidationClient(this.mContext, this.mFingerprintSensors.get(sensorId).getLazySession(), userId, sensorId, createLogger(0, 0, this.mAuthenticationStatsCollector), this.mBiometricContext, this.mFingerprintSensors.get(sensorId).getAuthenticatorIds(), callback);
        scheduleForSensor(sensorId, client);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public int getLockoutModeForUser(int sensorId, int userId) {
        return this.mFingerprintSensors.get(sensorId).getLockoutModeForUser(userId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public long getAuthenticatorId(int sensorId, int userId) {
        if (userId == 999) {
            userId = 0;
        }
        return this.mFingerprintSensors.get(sensorId).getAuthenticatorIds().getOrDefault(java.lang.Integer.valueOf(userId), 0L).longValue();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPointerDown(long requestId, int sensorId, final android.hardware.biometrics.fingerprint.PointerContext pc) {
        this.mFingerprintSensors.get(sensorId).getScheduler().getCurrentClientIfMatches(requestId, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onPointerDown$16(pc, (com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$onPointerDown$16(android.hardware.biometrics.fingerprint.PointerContext pc, com.android.server.biometrics.sensors.BaseClientMonitor baseClientMonitor) {
        if (mFingerprintProviderExt.handleOnPointerDown()) {
            return;
        }
        if (!(baseClientMonitor instanceof com.android.server.biometrics.sensors.fingerprint.Udfps)) {
            android.util.Slog.e(getTag(), "onPointerDown received during client: " + baseClientMonitor);
        } else {
            ((com.android.server.biometrics.sensors.fingerprint.Udfps) baseClientMonitor).onPointerDown(pc);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPointerUp(long requestId, int sensorId, final android.hardware.biometrics.fingerprint.PointerContext pc) {
        this.mFingerprintSensors.get(sensorId).getScheduler().getCurrentClientIfMatches(requestId, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onPointerUp$17(pc, (com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$onPointerUp$17(android.hardware.biometrics.fingerprint.PointerContext pc, com.android.server.biometrics.sensors.BaseClientMonitor baseClientMonitor) {
        if (mFingerprintProviderExt.handleOnPointerUp()) {
            return;
        }
        if (!(baseClientMonitor instanceof com.android.server.biometrics.sensors.fingerprint.Udfps)) {
            android.util.Slog.e(getTag(), "onPointerUp received during client: " + baseClientMonitor);
        } else {
            ((com.android.server.biometrics.sensors.fingerprint.Udfps) baseClientMonitor).onPointerUp(pc);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onUdfpsUiEvent(final int event, long requestId, int sensorId) {
        this.mFingerprintSensors.get(sensorId).getScheduler().getCurrentClientIfMatches(requestId, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onUdfpsUiEvent$18(event, (com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$onUdfpsUiEvent$18(int event, com.android.server.biometrics.sensors.BaseClientMonitor baseClientMonitor) {
        if (!(baseClientMonitor instanceof com.android.server.biometrics.sensors.fingerprint.Udfps)) {
            android.util.Slog.e(getTag(), "onUdfpsUiEvent received during client: " + baseClientMonitor);
        } else {
            ((com.android.server.biometrics.sensors.fingerprint.Udfps) baseClientMonitor).onUdfpsUiEvent(event);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void setUdfpsOverlayController(android.hardware.fingerprint.IUdfpsOverlayController controller) {
        this.mUdfpsOverlayController = controller;
        mFingerprintProviderExt.setUdfpsOverlayController(controller);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void setIgnoreDisplayTouches(long requestId, int sensorId, final boolean ignoreTouches) {
        this.mFingerprintSensors.get(sensorId).getScheduler().getCurrentClientIfMatches(requestId, new java.util.function.Consumer() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$setIgnoreDisplayTouches$19(ignoreTouches, (com.android.server.biometrics.sensors.BaseClientMonitor) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$setIgnoreDisplayTouches$19(boolean ignoreTouches, com.android.server.biometrics.sensors.BaseClientMonitor baseClientMonitor) {
        if (!(baseClientMonitor instanceof com.android.server.biometrics.sensors.fingerprint.Udfps)) {
            android.util.Slog.e(getTag(), "setIgnoreDisplayTouches received during client: " + baseClientMonitor);
        } else {
            ((com.android.server.biometrics.sensors.fingerprint.Udfps) baseClientMonitor).setIgnoreDisplayTouches(ignoreTouches);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void onPowerPressed() {
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            com.android.server.biometrics.sensors.fingerprint.aidl.Sensor sensor = this.mFingerprintSensors.valueAt(i);
            android.os.IBinder.DeathRecipient currentClient = sensor.getScheduler().getCurrentClient();
            if (currentClient == null) {
                return;
            }
            if (currentClient instanceof com.android.server.biometrics.sensors.fingerprint.PowerPressHandler) {
                ((com.android.server.biometrics.sensors.fingerprint.PowerPressHandler) currentClient).onPowerPressed();
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.BiometricServiceProvider
    public void dumpProtoState(int sensorId, android.util.proto.ProtoOutputStream proto, boolean clearSchedulerBuffer) {
        if (this.mFingerprintSensors.contains(sensorId)) {
            this.mFingerprintSensors.get(sensorId).dumpProtoState(sensorId, proto, clearSchedulerBuffer);
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
                int c = com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(sensorId).getBiometricsForUser(this.mContext, userId).size();
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
        this.mFingerprintSensors.get(sensorId).getScheduler().dump(printWriter);
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public android.hardware.biometrics.ITestSession createTestSession(int sensorId, android.hardware.biometrics.ITestSessionCallback callback, java.lang.String opPackageName) {
        return this.mFingerprintSensors.get(sensorId).createTestSession(callback, this.mBiometricStateCallback);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.e(getTag(), "HAL died");
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintProvider$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$binderDied$20();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$binderDied$20() {
        this.mDaemon = null;
        for (int i = 0; i < this.mFingerprintSensors.size(); i++) {
            com.android.server.biometrics.sensors.fingerprint.aidl.Sensor sensor = this.mFingerprintSensors.valueAt(i);
            int sensorId = this.mFingerprintSensors.keyAt(i);
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
        boolean changed = enabled != this.mTestHalEnabled;
        this.mTestHalEnabled = enabled;
        android.util.Slog.i(getTag(), "setTestHalEnabled(): useVhalForTesting=" + com.android.server.biometrics.Flags.useVhalForTesting() + "mTestHalEnabled=" + this.mTestHalEnabled + " changed=" + changed);
        if (changed && useVhalForTesting()) {
            getHalInstance();
        }
    }

    public boolean getTestHalEnabled() {
        return this.mTestHalEnabled;
    }

    private java.util.List<android.hardware.biometrics.SensorLocationInternal> getWorkaroundSensorProps(android.content.Context context) {
        android.hardware.biometrics.SensorLocationInternal location;
        java.util.List<android.hardware.biometrics.SensorLocationInternal> sensorLocations = new java.util.ArrayList<>();
        android.content.res.TypedArray sfpsProps = context.getResources().obtainTypedArray(android.R.array.config_screenThresholdLevels);
        for (int i = 0; i < sfpsProps.length(); i++) {
            int id = sfpsProps.getResourceId(i, -1);
            if (id > 0 && (location = parseSensorLocation(context.getResources().obtainTypedArray(id))) != null) {
                sensorLocations.add(location);
            }
        }
        sfpsProps.recycle();
        return sensorLocations;
    }

    private android.hardware.biometrics.SensorLocationInternal parseSensorLocation(android.content.res.TypedArray array) {
        if (array == null) {
            return null;
        }
        try {
            return new android.hardware.biometrics.SensorLocationInternal(array.getString(0), array.getInt(1, 0), array.getInt(2, 0), array.getInt(3, 0));
        } catch (java.lang.Exception e) {
            android.util.Slog.w(getTag(), "malformed sensor location", e);
            return null;
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void scheduleWatchdog(int sensorId) {
        android.util.Slog.d(getTag(), "Starting watchdog for fingerprint");
        com.android.server.biometrics.sensors.BiometricScheduler<android.hardware.biometrics.fingerprint.IFingerprint, android.hardware.biometrics.fingerprint.ISession> scheduler = this.mFingerprintSensors.get(sensorId).getScheduler();
        if (scheduler == null) {
            return;
        }
        scheduler.startWatchdog();
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public void simulateVhalFingerDown(int userId, int sensorId) {
        android.util.Slog.d(getTag(), "Simulate virtual HAL finger down event");
        com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession session = this.mFingerprintSensors.get(sensorId).getSessionForUser(userId);
        if (session == null) {
            android.util.Slog.e(getTag(), "no existing hal session found - aborting");
            return;
        }
        android.hardware.biometrics.fingerprint.PointerContext pc = new android.hardware.biometrics.fingerprint.PointerContext();
        try {
            session.getSession().onPointerDownWithContext(pc);
            session.getSession().onUiReady();
            session.getSession().onPointerUpWithContext(pc);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(getTag(), "failed hal operation ", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public com.android.server.biometrics.sensors.fingerprint.hidl.IFingerprint21ServiceProviderExt getServiceProviderEx() {
        return null;
    }

    @Override // com.android.server.biometrics.sensors.fingerprint.ServiceProvider
    public com.android.server.biometrics.sensors.fingerprint.aidl.IFingerprintServiceProviderExt getServiceProviderAidlEx() {
        return mFingerprintProviderExt;
    }

    public void handleOnEnrollment(android.hardware.fingerprint.Fingerprint fingerprint, int remaining) {
        mFingerprintProviderExt.handleOnEnrollment(fingerprint, remaining);
    }

    public boolean onAcquired(int acquiredInfo, int vendorCode) {
        return mFingerprintProviderExt.onAcquired(acquiredInfo, vendorCode);
    }

    public boolean onAuthenticated(int mSensorId, int enrollmentId, int mgroupId, java.util.ArrayList<java.lang.Byte> tokenByte) {
        return mFingerprintProviderExt.onAuthenticated(mSensorId, enrollmentId, mgroupId, tokenByte);
    }

    public void onInteractionDetected() {
        android.util.Slog.d(getTag(), "[onInteractionDetected]: mFingerprintProviderExt=" + mFingerprintProviderExt);
        if (mFingerprintProviderExt != null) {
            mFingerprintProviderExt.onInteractionDetected();
        }
    }

    public boolean onError(int error, int vendorCode) {
        return mFingerprintProviderExt.onError(error, vendorCode);
    }

    public void onLockoutTimed() {
        mFingerprintProviderExt.onLockoutTimed();
    }

    public void onLockoutPermanent() {
        mFingerprintProviderExt.onLockoutPermanent();
    }

    public boolean onTransactFromHal(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
        return mFingerprintProviderExt.onTransactFromHal(code, data, reply, flags);
    }

    public void handleOnFingerprintCmd(int cmdId, byte[] result, int resultLen) {
        getServiceProviderAidlEx().handleOnFingerprintCmd(cmdId, result, resultLen);
    }

    public void handleOnEngineeringInfoUpdated(int length, java.util.ArrayList<java.lang.Integer> keys, java.util.ArrayList<java.lang.String> values) {
        getServiceProviderAidlEx().onEngineeringInfoUpdated(length, keys, values);
    }

    public void sendFingerprintReEnrollNotification() {
        this.mAuthenticationStatsCollector.sendFingerprintReEnrollNotification();
    }

    public android.hardware.biometrics.fingerprint.IVirtualHal getVhal() throws android.os.RemoteException {
        if (this.mVhal == null && useVhalForTesting()) {
            this.mVhal = android.hardware.biometrics.fingerprint.IVirtualHal.Stub.asInterface(this.mDaemon.asBinder().getExtension());
            if (this.mVhal == null) {
                android.util.Slog.e(getTag(), "Unable to get fingerprint virtualhal interface");
            }
        }
        return this.mVhal;
    }

    public boolean useVhalForTesting() {
        return com.android.server.biometrics.Flags.useVhalForTesting() && this.mTestHalEnabled;
    }
}
