package com.android.server.location;

/* JADX INFO: loaded from: classes2.dex */
public class LocationManagerService extends android.location.ILocationManager.Stub implements com.android.server.location.provider.LocationProviderManager.StateChangedListener {
    private static final java.lang.String ATTRIBUTION_TAG = "LocationService";
    private final android.content.Context mContext;
    private android.location.ILocationListener mDeprecatedGnssBatchingListener;
    private java.lang.String mExtraLocationControllerPackage;
    private boolean mExtraLocationControllerPackageEnabled;
    private com.android.server.location.provider.proxy.ProxyGeocodeProvider mGeocodeProvider;
    private final com.android.server.location.geofence.GeofenceManager mGeofenceManager;
    private final com.android.server.location.injector.Injector mInjector;
    android.location.LocationManagerInternal.LocationPackageTagsListener mLocationTagsChangedListener;
    private final com.android.server.location.provider.PassiveLocationProviderManager mPassiveManager;
    public static final java.lang.String TAG = "LocationManagerService";
    public static boolean D = android.util.Log.isLoggable(TAG, 3);
    private static com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass = null;
    final java.lang.Object mLock = new java.lang.Object();
    private volatile com.android.server.location.gnss.GnssManagerService mGnssManagerService = null;
    private final java.lang.Object mDeprecatedGnssBatchingLock = new java.lang.Object();
    final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.provider.LocationProviderManager> mProviderManagers = new java.util.concurrent.CopyOnWriteArrayList<>();
    private com.android.server.location.LocationManagerService.LocationManagerServiceWrapper mLmsWrapper = new com.android.server.location.LocationManagerService.LocationManagerServiceWrapper();
    private com.android.server.location.interfaces.ILocationFreezeProc mLocationFreeze = null;
    private com.android.server.location.interfaces.IVirtualGnssLocationProvider mVirtualProvider = null;
    private com.android.server.location.interfaces.IVirtualGnssHal mVirtualGnssHal = null;
    private final com.android.server.location.LocationManagerService.LocalService mLocalService = new com.android.server.location.LocationManagerService.LocalService();

    public static class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.location.LocationManagerService mService;
        private final com.android.server.location.LocationManagerService.SystemInjector mSystemInjector;
        private final com.android.server.location.LocationManagerService.Lifecycle.LifecycleUserInfoHelper mUserInfoHelper;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mUserInfoHelper = new com.android.server.location.LocationManagerService.Lifecycle.LifecycleUserInfoHelper(context);
            this.mSystemInjector = new com.android.server.location.LocationManagerService.SystemInjector(context, this.mUserInfoHelper);
            this.mService = new com.android.server.location.LocationManagerService(context, this.mSystemInjector);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService("location", this.mService);
            android.location.LocationManager.invalidateLocalLocationEnabledCaches();
            android.location.LocationManager.disableLocalLocationEnabledCaches();
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 500) {
                this.mSystemInjector.onSystemReady();
                this.mService.onSystemReady();
                this.mService.oplusSystemReady(this.mService);
            } else if (phase == 600) {
                this.mService.onSystemThirdPartyAppsCanStart();
                this.mService.oplusSystemThirdPartyAppsCanStart();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            this.mUserInfoHelper.onUserStarted(user.getUserIdentifier());
            this.mService.logLocationEnabledState();
            this.mService.logEmergencyState();
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            this.mUserInfoHelper.onCurrentUserChanged(from.getUserIdentifier(), to.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            this.mUserInfoHelper.onUserStopped(user.getUserIdentifier());
        }

        /* JADX INFO: Access modifiers changed from: private */
        static class LifecycleUserInfoHelper extends com.android.server.location.injector.SystemUserInfoHelper {
            LifecycleUserInfoHelper(android.content.Context context) {
                super(context);
            }

            void onUserStarted(int userId) {
                dispatchOnUserStarted(userId);
            }

            void onUserStopped(int userId) {
                dispatchOnUserStopped(userId);
            }

            void onCurrentUserChanged(final int fromUserId, final int toUserId) {
                if (com.android.server.location.LocationManagerService.mOplusLbsClass == null) {
                    dispatchOnCurrentUserChanged(fromUserId, toUserId);
                } else {
                    com.android.server.location.LocationManagerService.mOplusLbsClass.getHandler(0).post(new java.lang.Runnable() { // from class: com.android.server.location.LocationManagerService$Lifecycle$LifecycleUserInfoHelper$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onCurrentUserChanged$0(fromUserId, toUserId);
                        }
                    });
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onCurrentUserChanged$0(int fromUserId, int toUserId) {
                dispatchOnCurrentUserChanged(fromUserId, toUserId);
            }
        }
    }

    LocationManagerService(android.content.Context context, com.android.server.location.injector.Injector injector) {
        this.mContext = context.createAttributionContext(ATTRIBUTION_TAG);
        this.mInjector = injector;
        com.android.server.LocalServices.addService(android.location.LocationManagerInternal.class, this.mLocalService);
        this.mGeofenceManager = new com.android.server.location.geofence.GeofenceManager(this.mContext, injector);
        this.mInjector.getLocationSettings().registerLocationUserSettingsListener(new com.android.server.location.settings.LocationSettings.LocationUserSettingsListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda5
            @Override // com.android.server.location.settings.LocationSettings.LocationUserSettingsListener
            public final void onLocationUserSettingsChanged(int i, com.android.server.location.settings.LocationUserSettings locationUserSettings, com.android.server.location.settings.LocationUserSettings locationUserSettings2) {
                this.f$0.onLocationUserSettingsChanged(i, locationUserSettings, locationUserSettings2);
            }
        });
        this.mInjector.getSettingsHelper().addOnLocationEnabledChangedListener(new com.android.server.location.injector.SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda6
            @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
            public final void onSettingChanged(int i) {
                this.f$0.onLocationModeChanged(i);
            }
        });
        this.mInjector.getSettingsHelper().addAdasAllowlistChangedListener(new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda7
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                this.f$0.lambda$new$0();
            }
        });
        this.mInjector.getSettingsHelper().addIgnoreSettingsAllowlistChangedListener(new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda8
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                this.f$0.lambda$new$1();
            }
        });
        this.mInjector.getUserInfoHelper().addListener(new com.android.server.location.injector.UserInfoHelper.UserListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda9
            @Override // com.android.server.location.injector.UserInfoHelper.UserListener
            public final void onUserChanged(int i, int i2) {
                this.f$0.lambda$new$2(i, i2);
            }
        });
        this.mInjector.getEmergencyHelper().addOnEmergencyStateChangedListener(new com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda10
            @Override // com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener
            public final void onStateChanged() {
                this.f$0.onEmergencyStateChanged();
            }
        });
        this.mPassiveManager = new com.android.server.location.provider.PassiveLocationProviderManager(this.mContext, injector);
        addLocationProviderManager(this.mPassiveManager, new com.android.server.location.provider.PassiveLocationProvider(this.mContext));
        com.android.server.pm.permission.LegacyPermissionManagerInternal permissionManagerInternal = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        permissionManagerInternal.setLocationPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda11
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public final java.lang.String[] getPackages(int i) {
                return this.f$0.lambda$new$3(i);
            }
        });
        permissionManagerInternal.setLocationExtraPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda12
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public final java.lang.String[] getPackages(int i) {
                return this.f$0.lambda$new$4(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        refreshAppOpsRestrictions(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        refreshAppOpsRestrictions(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(int userId, int change) {
        if (change == 2) {
            refreshAppOpsRestrictions(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$new$3(int userId) {
        return this.mContext.getResources().getStringArray(android.R.array.config_localPrivateDisplayPorts);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$new$4(int userId) {
        return this.mContext.getResources().getStringArray(android.R.array.config_localNotStealTopFocusDisplayPorts);
    }

    com.android.server.location.provider.LocationProviderManager getLocationProviderManager(java.lang.String providerName) {
        if (providerName == null) {
            return null;
        }
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            if (providerName.equals(manager.getName())) {
                if (!manager.isVisibleToCaller()) {
                    return null;
                }
                return manager;
            }
        }
        return null;
    }

    private com.android.server.location.provider.LocationProviderManager getOrAddLocationProviderManager(java.lang.String providerName) {
        synchronized (this.mProviderManagers) {
            for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
                if (providerName.equals(manager.getName())) {
                    return manager;
                }
            }
            com.android.server.location.provider.LocationProviderManager manager2 = new com.android.server.location.provider.LocationProviderManager(this.mContext, this.mInjector, providerName, this.mPassiveManager);
            addLocationProviderManager(manager2, null);
            return manager2;
        }
    }

    void addLocationProviderManager(com.android.server.location.provider.LocationProviderManager manager, com.android.server.location.provider.AbstractLocationProvider realProvider) {
        synchronized (this.mProviderManagers) {
            boolean z = true;
            com.android.internal.util.Preconditions.checkState(getLocationProviderManager(manager.getName()) == null);
            manager.startManager(this);
            if (realProvider != null) {
                if (manager != this.mPassiveManager) {
                    int defaultStationaryThrottlingSetting = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch") ? 0 : 1;
                    boolean enableStationaryThrottling = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "location_enable_stationary_throttle", defaultStationaryThrottlingSetting) != 0;
                    if (mOplusLbsClass != null) {
                        if (!enableStationaryThrottling || !mOplusLbsClass.isStationaryThrottlingEnable()) {
                            z = false;
                        }
                        enableStationaryThrottling = z;
                    }
                    if (enableStationaryThrottling) {
                        realProvider = new com.android.server.location.provider.StationaryThrottlingLocationProvider(manager.getName(), this.mInjector, realProvider);
                    }
                }
                manager.setRealProvider(realProvider);
            }
            this.mProviderManagers.add(manager);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLocationProviderManager(com.android.server.location.provider.LocationProviderManager manager) {
        synchronized (this.mProviderManagers) {
            boolean removed = this.mProviderManagers.remove(manager);
            com.android.internal.util.Preconditions.checkArgument(removed);
            manager.setMockProvider(null);
            manager.setRealProvider(null);
            manager.stopManager();
        }
    }

    void onSystemReady() {
        if (android.os.Build.IS_DEBUGGABLE) {
            android.app.AppOpsManager appOps = (android.app.AppOpsManager) java.util.Objects.requireNonNull((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class));
            appOps.startWatchingNoted(new int[]{1, 0}, new android.app.AppOpsManager.OnOpNotedListener() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda2
                public final void onOpNoted(java.lang.String str, int i, java.lang.String str2, java.lang.String str3, int i2, int i3) {
                    this.f$0.lambda$onSystemReady$5(str, i, str2, str3, i2, i3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$5(java.lang.String code, int uid, java.lang.String packageName, java.lang.String attributionTag, int flags, int result) {
        if (!isLocationEnabledForUser(android.os.UserHandle.getUserId(uid))) {
            android.util.Log.w(TAG, "location noteOp with location off - " + android.location.util.identity.CallerIdentity.forTest(uid, 0, packageName, attributionTag));
        }
    }

    void onSystemThirdPartyAppsCanStart() {
        char c = 0;
        try {
            com.android.internal.util.Preconditions.checkState(!this.mContext.getPackageManager().queryIntentServicesAsUser(new android.content.Intent("com.android.location.service.FusedLocationProvider"), 1572864, 0).isEmpty(), "Unable to find a direct boot aware fused location provider");
        } catch (java.lang.IllegalStateException expected) {
            android.util.Log.e(TAG, expected.getMessage());
        }
        com.android.server.location.provider.proxy.ProxyLocationProvider fusedProvider = com.android.server.location.provider.proxy.ProxyLocationProvider.create(this.mContext, "fused", "com.android.location.service.FusedLocationProvider", mOplusLbsClass.getFlpResId(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE), mOplusLbsClass.getFlpResId(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME));
        if (fusedProvider != null) {
            com.android.server.location.provider.LocationProviderManager fusedManager = new com.android.server.location.provider.LocationProviderManager(this.mContext, this.mInjector, "fused", this.mPassiveManager);
            addLocationProviderManager(fusedManager, fusedProvider);
        } else {
            android.util.Log.wtf(TAG, "no fused location provider found");
        }
        boolean hasLocationFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.location");
        boolean hasGpsFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.location.gps");
        com.android.server.location.provider.AbstractLocationProvider virtualProvider = this.mVirtualProvider.getVirtualProvider(this.mContext);
        if (hasLocationFeature && (com.android.server.location.gnss.hal.GnssNative.isSupported() || virtualProvider != null)) {
            if (virtualProvider != null) {
                android.util.Log.d(TAG, "using virtual gnssProvider");
            }
            com.android.server.location.gnss.GnssConfiguration gnssConfiguration = new com.android.server.location.gnss.GnssConfiguration(this.mContext);
            com.android.server.location.gnss.hal.GnssNative gnssNative = com.android.server.location.gnss.hal.GnssNative.create(this.mInjector, gnssConfiguration, hasGpsFeature || virtualProvider == null, this.mVirtualGnssHal);
            this.mGnssManagerService = new com.android.server.location.gnss.GnssManagerService(this.mContext, this.mInjector, gnssNative);
            this.mGnssManagerService.onSystemReady();
            boolean useGnssHardwareProvider = this.mContext.getResources().getBoolean(android.R.bool.config_supportsMultiWindow);
            com.android.server.location.provider.AbstractLocationProvider gnssProvider = null;
            if (!useGnssHardwareProvider) {
                gnssProvider = com.android.server.location.provider.proxy.ProxyLocationProvider.create(this.mContext, com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, "android.location.provider.action.GNSS_PROVIDER", android.R.bool.config_enableContextSyncInCall, android.R.string.config_icon_mask);
            }
            if (gnssProvider == null) {
                if (hasGpsFeature) {
                    gnssProvider = this.mGnssManagerService.getGnssLocationProvider();
                } else {
                    gnssProvider = virtualProvider;
                }
            } else {
                com.android.server.location.provider.LocationProviderManager gnssHardwareManager = new com.android.server.location.provider.LocationProviderManager(this.mContext, this.mInjector, "gps_hardware", null, java.util.Collections.singletonList("android.permission.LOCATION_HARDWARE"));
                addLocationProviderManager(gnssHardwareManager, this.mGnssManagerService.getGnssLocationProvider());
            }
            com.android.server.location.provider.LocationProviderManager gnssManager = new com.android.server.location.provider.LocationProviderManager(this.mContext, this.mInjector, com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, this.mPassiveManager);
            addLocationProviderManager(gnssManager, gnssProvider);
        }
        com.android.server.location.HardwareActivityRecognitionProxy hardwareActivityRecognitionProxy = com.android.server.location.HardwareActivityRecognitionProxy.createAndRegister(this.mContext);
        if (hardwareActivityRecognitionProxy == null) {
            android.util.Log.e(TAG, "unable to bind ActivityRecognitionProxy");
        }
        if (this.mGnssManagerService != null) {
            com.android.server.location.geofence.GeofenceProxy provider = com.android.server.location.geofence.GeofenceProxy.createAndBind(this.mContext, this.mGnssManagerService.getGnssGeofenceProxy());
            if (provider == null) {
                android.util.Log.e(TAG, "unable to bind to GeofenceProxy");
            }
        }
        java.lang.String[] testProviderStrings = this.mContext.getResources().getStringArray(android.R.array.config_sms_enabled_locking_shift_tables);
        int length = testProviderStrings.length;
        int i = 0;
        while (i < length) {
            java.lang.String testProviderString = testProviderStrings[i];
            java.lang.String[] fragments = testProviderString.split(",");
            java.lang.String name = fragments[c].trim();
            android.location.provider.ProviderProperties properties = new android.location.provider.ProviderProperties.Builder().setHasNetworkRequirement(java.lang.Boolean.parseBoolean(fragments[1])).setHasSatelliteRequirement(java.lang.Boolean.parseBoolean(fragments[2])).setHasCellRequirement(java.lang.Boolean.parseBoolean(fragments[3])).setHasMonetaryCost(java.lang.Boolean.parseBoolean(fragments[4])).setHasAltitudeSupport(java.lang.Boolean.parseBoolean(fragments[5])).setHasSpeedSupport(java.lang.Boolean.parseBoolean(fragments[6])).setHasBearingSupport(java.lang.Boolean.parseBoolean(fragments[7])).setPowerUsage(java.lang.Integer.parseInt(fragments[8])).setAccuracy(java.lang.Integer.parseInt(fragments[9])).build();
            com.android.server.location.provider.LocationProviderManager manager = getOrAddLocationProviderManager(name);
            manager.setMockProvider(new com.android.server.location.provider.MockLocationProvider(properties, android.location.util.identity.CallerIdentity.fromContext(this.mContext), java.util.Collections.emptySet()));
            i++;
            fusedProvider = fusedProvider;
            c = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationUserSettingsChanged(int userId, com.android.server.location.settings.LocationUserSettings oldSettings, com.android.server.location.settings.LocationUserSettings newSettings) {
        if (oldSettings.isAdasGnssLocationEnabled() != newSettings.isAdasGnssLocationEnabled()) {
            boolean enabled = newSettings.isAdasGnssLocationEnabled();
            if (D) {
                android.util.Log.d(TAG, "[u" + userId + "] adas gnss location enabled = " + enabled);
            }
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logAdasLocationEnabled(userId, enabled);
            android.content.Intent intent = new android.content.Intent("android.location.action.ADAS_GNSS_ENABLED_CHANGED").putExtra("android.location.extra.ADAS_GNSS_ENABLED", enabled).addFlags(1073741824).addFlags(268435456);
            this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationModeChanged(int userId) {
        boolean enabled = this.mInjector.getSettingsHelper().isLocationEnabled(userId);
        android.location.LocationManager.invalidateLocalLocationEnabledCaches();
        android.util.Log.d(TAG, "[u" + userId + "] location enabled = " + enabled);
        com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logLocationEnabled(userId, enabled);
        logLocationEnabledState();
        android.content.Intent intent = new android.content.Intent("android.location.MODE_CHANGED").putExtra("android.location.extra.LOCATION_ENABLED", enabled).addFlags(1073741824).addFlags(268435456);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
        refreshAppOpsRestrictions(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEmergencyStateChanged() {
        logEmergencyState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logEmergencyState() {
        boolean isInEmergency = this.mInjector.getEmergencyHelper().isInEmergency(Long.MIN_VALUE);
        this.mInjector.getLocationUsageLogger().logEmergencyStateChanged(isInEmergency);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logLocationEnabledState() {
        boolean locationEnabled = false;
        int[] runningUserIds = this.mInjector.getUserInfoHelper().getRunningUserIds();
        for (int userId : runningUserIds) {
            locationEnabled = this.mInjector.getSettingsHelper().isLocationEnabled(userId);
            if (locationEnabled) {
                break;
            }
        }
        this.mInjector.getLocationUsageLogger().logLocationEnabledStateChanged(locationEnabled);
    }

    public int getGnssYearOfHardware() {
        if (this.mGnssManagerService == null) {
            return 0;
        }
        return this.mGnssManagerService.getGnssYearOfHardware();
    }

    public java.lang.String getGnssHardwareModelName() {
        return this.mGnssManagerService == null ? "" : this.mGnssManagerService.getGnssHardwareModelName();
    }

    public int getGnssBatchSize() {
        if (this.mGnssManagerService == null) {
            return 0;
        }
        return this.mGnssManagerService.getGnssBatchSize();
    }

    public void startGnssBatch(long periodNanos, android.location.ILocationListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) throws java.lang.Throwable {
        startGnssBatch_enforcePermission();
        if (this.mGnssManagerService == null) {
            return;
        }
        long intervalMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(periodNanos);
        synchronized (this.mDeprecatedGnssBatchingLock) {
            try {
                try {
                    stopGnssBatch();
                    registerLocationListener(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, new android.location.LocationRequest.Builder(intervalMs).setMaxUpdateDelayMillis(((long) this.mGnssManagerService.getGnssBatchSize()) * intervalMs).setHiddenFromAppOps(true).build(), listener, packageName, attributionTag, listenerId);
                    this.mDeprecatedGnssBatchingListener = listener;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public void flushGnssBatch() {
        flushGnssBatch_enforcePermission();
        if (this.mGnssManagerService == null) {
            return;
        }
        synchronized (this.mDeprecatedGnssBatchingLock) {
            if (this.mDeprecatedGnssBatchingListener != null) {
                requestListenerFlush(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, this.mDeprecatedGnssBatchingListener, 0);
            }
        }
    }

    public void stopGnssBatch() {
        stopGnssBatch_enforcePermission();
        if (this.mGnssManagerService == null) {
            return;
        }
        synchronized (this.mDeprecatedGnssBatchingLock) {
            if (this.mDeprecatedGnssBatchingListener != null) {
                android.location.ILocationListener listener = this.mDeprecatedGnssBatchingListener;
                this.mDeprecatedGnssBatchingListener = null;
                unregisterLocationListener(listener);
            }
        }
    }

    public boolean hasProvider(java.lang.String provider) {
        return getLocationProviderManager(provider) != null;
    }

    public java.util.List<java.lang.String> getAllProviders() {
        java.util.ArrayList<java.lang.String> providers = new java.util.ArrayList<>(this.mProviderManagers.size());
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            if (manager.isVisibleToCaller()) {
                providers.add(manager.getName());
            }
        }
        return providers;
    }

    public java.util.List<java.lang.String> getProviders(android.location.Criteria criteria, boolean enabledOnly) {
        java.util.ArrayList<java.lang.String> providers;
        if (!com.android.server.location.LocationPermissions.checkCallingOrSelfLocationPermission(this.mContext, 1)) {
            return java.util.Collections.emptyList();
        }
        synchronized (this.mLock) {
            providers = new java.util.ArrayList<>(this.mProviderManagers.size());
            for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
                if (manager.isVisibleToCaller()) {
                    java.lang.String name = manager.getName();
                    if (!name.equals(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS) || mOplusLbsClass.isVirtualGpsVisibleOnlyForPad(android.os.Binder.getCallingUid())) {
                        if (!enabledOnly || manager.isEnabled(android.os.UserHandle.getCallingUserId())) {
                            if (criteria == null || android.location.LocationProvider.propertiesMeetCriteria(name, manager.getProperties(), criteria)) {
                                providers.add(name);
                            }
                        }
                    }
                }
            }
        }
        return providers;
    }

    public java.lang.String getBestProvider(android.location.Criteria criteria, boolean enabledOnly) {
        java.util.List<java.lang.String> providers;
        synchronized (this.mLock) {
            providers = getProviders(criteria, enabledOnly);
            if (providers.isEmpty()) {
                providers = getProviders(null, enabledOnly);
            }
        }
        if (providers.isEmpty()) {
            return null;
        }
        if (providers.contains("fused")) {
            return "fused";
        }
        if (providers.contains(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS)) {
            return com.android.server.am.IOplusSceneManager.APP_SCENE_GPS;
        }
        if (providers.contains("network")) {
            return "network";
        }
        return providers.get(0);
    }

    public java.lang.String[] getBackgroundThrottlingWhitelist() {
        return (java.lang.String[]) this.mInjector.getSettingsHelper().getBackgroundThrottlePackageWhitelist().toArray(new java.lang.String[0]);
    }

    public android.os.PackageTagsList getIgnoreSettingsAllowlist() {
        return this.mInjector.getSettingsHelper().getIgnoreSettingsAllowlist();
    }

    public android.os.PackageTagsList getAdasAllowlist() {
        return this.mInjector.getSettingsHelper().getAdasAllowlist();
    }

    public android.os.ICancellationSignal getCurrentLocation(java.lang.String provider, android.location.LocationRequest request, android.location.ILocationCallback consumer, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, listenerId);
        int permissionLevel = com.android.server.location.LocationPermissions.getPermissionLevel(this.mContext, identity.getUid(), identity.getPid());
        if (android.location.flags.Flags.enableLocationBypass()) {
            if (permissionLevel == 0) {
                if (this.mContext.checkCallingPermission("android.permission.LOCATION_BYPASS") != 0) {
                    com.android.server.location.LocationPermissions.enforceLocationPermission(identity.getUid(), permissionLevel, 1);
                } else {
                    permissionLevel = 2;
                }
            }
        } else {
            com.android.server.location.LocationPermissions.enforceLocationPermission(identity.getUid(), permissionLevel, 1);
        }
        com.android.internal.util.Preconditions.checkState((identity.getPid() == android.os.Process.myPid() && attributionTag == null) ? false : true);
        android.location.LocationRequest request2 = validateLocationRequest(provider, request, identity);
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        com.android.internal.util.Preconditions.checkArgument(manager != null, "provider \"" + provider + "\" does not exist");
        return manager.getCurrentLocation(request2, identity, permissionLevel, consumer);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void registerLocationListener(java.lang.String r17, android.location.LocationRequest r18, android.location.ILocationListener r19, java.lang.String r20, java.lang.String r21, java.lang.String r22) {
        /*
            Method dump skipped, instruction units count: 256
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.LocationManagerService.registerLocationListener(java.lang.String, android.location.LocationRequest, android.location.ILocationListener, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public void registerLocationPendingIntent(java.lang.String provider, android.location.LocationRequest request, android.app.PendingIntent pendingIntent, java.lang.String packageName, java.lang.String attributionTag) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, android.app.AppOpsManager.toReceiverId(pendingIntent));
        int permissionLevel = com.android.server.location.LocationPermissions.getPermissionLevel(this.mContext, identity.getUid(), identity.getPid());
        if (android.location.flags.Flags.enableLocationBypass()) {
            if (permissionLevel == 0) {
                if (this.mContext.checkCallingPermission("android.permission.LOCATION_BYPASS") != 0) {
                    com.android.server.location.LocationPermissions.enforceLocationPermission(identity.getUid(), permissionLevel, 1);
                } else {
                    permissionLevel = 2;
                }
            }
        } else {
            com.android.server.location.LocationPermissions.enforceLocationPermission(identity.getUid(), permissionLevel, 1);
        }
        com.android.internal.util.Preconditions.checkArgument((identity.getPid() == android.os.Process.myPid() && attributionTag == null) ? false : true);
        if (android.app.compat.CompatChanges.isChangeEnabled(169887240L, identity.getUid())) {
            boolean usesSystemApi = request.isLowPower() || request.isHiddenFromAppOps() || request.isLocationSettingsIgnored() || !request.getWorkSource().isEmpty();
            if (usesSystemApi) {
                throw new java.lang.SecurityException("PendingIntent location requests may not use system APIs: " + request);
            }
        }
        android.location.LocationRequest request2 = validateLocationRequest(provider, request, identity);
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        com.android.internal.util.Preconditions.checkArgument(manager != null, "provider \"" + provider + "\" does not exist");
        if (this.mLocationFreeze == null) {
            android.util.Log.i(TAG, "new LocationFreeze before storeLocationRequest.");
            this.mLocationFreeze = (com.android.server.location.interfaces.ILocationFreezeProc) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ILocationFreezeProc.DEFAULT, this.mContext);
        }
        if (this.mLocationFreeze != null && !this.mLocationFreeze.storeLocationRequest(manager, request2, identity, permissionLevel, pendingIntent)) {
            android.util.Log.i(TAG, "the app is freeze, return.");
        } else {
            manager.registerLocationRequest(request2, identity, permissionLevel, pendingIntent);
        }
    }

    private android.location.LocationRequest validateLocationRequest(java.lang.String provider, android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity) {
        if (!request.getWorkSource().isEmpty()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS", "setting a work source requires android.permission.UPDATE_DEVICE_STATS");
        }
        android.location.LocationRequest.Builder sanitized = new android.location.LocationRequest.Builder(request);
        if (!android.app.compat.CompatChanges.isChangeEnabled(168936375L, android.os.Binder.getCallingUid()) && this.mContext.checkCallingPermission("android.permission.LOCATION_HARDWARE") != 0) {
            sanitized.setLowPower(false);
        }
        android.os.WorkSource workSource = new android.os.WorkSource(request.getWorkSource());
        if (workSource.size() > 0 && workSource.getPackageName(0) == null) {
            android.util.Log.w(TAG, "received (and ignoring) illegal worksource with no package name");
            workSource.clear();
        } else {
            java.util.List<android.os.WorkSource.WorkChain> workChains = workSource.getWorkChains();
            if (workChains != null && !workChains.isEmpty() && workChains.get(0).getAttributionTag() == null) {
                android.util.Log.w(TAG, "received (and ignoring) illegal worksource with no attribution tag");
                workSource.clear();
            }
        }
        if (workSource.isEmpty()) {
            identity.addToWorkSource(workSource);
        }
        sanitized.setWorkSource(workSource);
        android.location.LocationRequest request2 = sanitized.build();
        boolean isLocationProvider = this.mLocalService.isProvider(null, identity);
        if (request2.isLowPower() && android.app.compat.CompatChanges.isChangeEnabled(168936375L, identity.getUid())) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.LOCATION_HARDWARE", "low power request requires android.permission.LOCATION_HARDWARE");
        }
        if (request2.isHiddenFromAppOps()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_APP_OPS_STATS", "hiding from app ops requires android.permission.UPDATE_APP_OPS_STATS");
        }
        if (request2.isAdasGnssBypass()) {
            if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                throw new java.lang.IllegalArgumentException("adas gnss bypass requests are only allowed on automotive devices");
            }
            if (!com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(provider)) {
                throw new java.lang.IllegalArgumentException("adas gnss bypass requests are only allowed on the \"gps\" provider");
            }
            if (!isLocationProvider) {
                com.android.server.location.LocationPermissions.enforceCallingOrSelfBypassPermission(this.mContext);
            }
        }
        if (request2.isLocationSettingsIgnored() && !isLocationProvider) {
            com.android.server.location.LocationPermissions.enforceCallingOrSelfBypassPermission(this.mContext);
        }
        return request2;
    }

    public void requestListenerFlush(java.lang.String provider, android.location.ILocationListener listener, int requestCode) {
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        com.android.internal.util.Preconditions.checkArgument(manager != null, "provider \"" + provider + "\" does not exist");
        manager.flush((android.location.ILocationListener) java.util.Objects.requireNonNull(listener), requestCode);
    }

    public void requestPendingIntentFlush(java.lang.String provider, android.app.PendingIntent pendingIntent, int requestCode) {
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        com.android.internal.util.Preconditions.checkArgument(manager != null, "provider \"" + provider + "\" does not exist");
        manager.flush((android.app.PendingIntent) java.util.Objects.requireNonNull(pendingIntent), requestCode);
    }

    public void unregisterLocationListener(android.location.ILocationListener listener) {
        android.app.ActivityManagerInternal managerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (managerInternal != null) {
            managerInternal.logFgsApiEnd(3, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
        }
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            manager.unregisterLocationRequest(listener);
        }
        if (this.mLocationFreeze != null) {
            this.mLocationFreeze.removeLocationRequest(listener.asBinder());
        }
    }

    public void unregisterLocationPendingIntent(android.app.PendingIntent pendingIntent) {
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            manager.unregisterLocationRequest(pendingIntent);
        }
        if (this.mLocationFreeze != null) {
            this.mLocationFreeze.removeLocationRequest(pendingIntent);
        }
    }

    public android.location.Location getLastLocation(java.lang.String provider, android.location.LastLocationRequest request, java.lang.String packageName, java.lang.String attributionTag) {
        try {
            android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag);
            int permissionLevel = com.android.server.location.LocationPermissions.getPermissionLevel(this.mContext, identity.getUid(), identity.getPid());
            boolean z = true;
            if (android.location.flags.Flags.enableLocationBypass()) {
                if (permissionLevel == 0) {
                    if (this.mContext.checkCallingPermission("android.permission.LOCATION_BYPASS") != 0) {
                        com.android.server.location.LocationPermissions.enforceLocationPermission(identity.getUid(), permissionLevel, 1);
                    } else {
                        permissionLevel = 2;
                    }
                }
            } else {
                com.android.server.location.LocationPermissions.enforceLocationPermission(identity.getUid(), permissionLevel, 1);
            }
            if (identity.getPid() == android.os.Process.myPid() && attributionTag == null) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkArgument(z);
            request = validateLastLocationRequest(provider, request, identity);
            com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
            if (manager == null) {
                return null;
            }
            android.location.Location originLocation = manager.getLastLocation(request, identity, permissionLevel);
            if (mOplusLbsClass != null && "network".equals(provider)) {
                return mOplusLbsClass.getLastLocation(originLocation, request, permissionLevel);
            }
            return originLocation;
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(TAG, "getLastLocation catch IllegalArgumentException, provider = " + provider + "| lastLocationRequest = " + request + "| packageName = " + packageName + "| attributionTag = " + attributionTag);
            e.printStackTrace();
            throw e;
        }
    }

    private android.location.LastLocationRequest validateLastLocationRequest(java.lang.String provider, android.location.LastLocationRequest request, android.location.util.identity.CallerIdentity identity) {
        android.location.LastLocationRequest.Builder sanitized = new android.location.LastLocationRequest.Builder(request);
        android.location.LastLocationRequest request2 = sanitized.build();
        boolean isLocationProvider = this.mLocalService.isProvider(null, identity);
        if (request2.isHiddenFromAppOps()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_APP_OPS_STATS", "hiding from app ops requires android.permission.UPDATE_APP_OPS_STATS");
        }
        if (request2.isAdasGnssBypass()) {
            if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                throw new java.lang.IllegalArgumentException("adas gnss bypass requests are only allowed on automotive devices");
            }
            if (!com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(provider)) {
                throw new java.lang.IllegalArgumentException("adas gnss bypass requests are only allowed on the \"gps\" provider");
            }
            if (!isLocationProvider) {
                com.android.server.location.LocationPermissions.enforceCallingOrSelfBypassPermission(this.mContext);
            }
        }
        if (request2.isLocationSettingsIgnored() && !isLocationProvider) {
            com.android.server.location.LocationPermissions.enforceCallingOrSelfBypassPermission(this.mContext);
        }
        return request2;
    }

    public android.location.LocationTime getGnssTimeMillis() {
        return this.mLocalService.getGnssTimeMillis();
    }

    public void injectLocation(android.location.Location location) {
        super.injectLocation_enforcePermission();
        com.android.internal.util.Preconditions.checkArgument(location.isComplete());
        int userId = android.os.UserHandle.getCallingUserId();
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(location.getProvider());
        if (manager != null && manager.isEnabled(userId)) {
            manager.injectLastLocation((android.location.Location) java.util.Objects.requireNonNull(location), userId);
        }
    }

    public void requestGeofence(android.location.Geofence geofence, android.app.PendingIntent intent, java.lang.String packageName, java.lang.String attributionTag) {
        this.mGeofenceManager.addGeofence(geofence, intent, packageName, attributionTag);
    }

    public void removeGeofence(android.app.PendingIntent pendingIntent) {
        this.mGeofenceManager.removeGeofence(pendingIntent);
    }

    public void registerGnssStatusCallback(android.location.IGnssStatusListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.registerGnssStatusCallback(listener, packageName, attributionTag, listenerId);
        }
    }

    public void unregisterGnssStatusCallback(android.location.IGnssStatusListener listener) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.unregisterGnssStatusCallback(listener);
        }
    }

    public void registerGnssNmeaCallback(android.location.IGnssNmeaListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.registerGnssNmeaCallback(listener, packageName, attributionTag, listenerId);
        }
    }

    public void unregisterGnssNmeaCallback(android.location.IGnssNmeaListener listener) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.unregisterGnssNmeaCallback(listener);
        }
    }

    public void addGnssMeasurementsListener(android.location.GnssMeasurementRequest request, android.location.IGnssMeasurementsListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.addGnssMeasurementsListener(request, listener, packageName, attributionTag, listenerId);
        }
    }

    public void removeGnssMeasurementsListener(android.location.IGnssMeasurementsListener listener) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.removeGnssMeasurementsListener(listener);
        }
    }

    public void addGnssAntennaInfoListener(android.location.IGnssAntennaInfoListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.addGnssAntennaInfoListener(listener, packageName, attributionTag, listenerId);
        }
    }

    public void removeGnssAntennaInfoListener(android.location.IGnssAntennaInfoListener listener) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.removeGnssAntennaInfoListener(listener);
        }
    }

    public void addProviderRequestListener(android.location.provider.IProviderRequestListener listener) {
        addProviderRequestListener_enforcePermission();
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            if (manager.isVisibleToCaller()) {
                manager.addProviderRequestListener(listener);
            }
        }
    }

    public void removeProviderRequestListener(android.location.provider.IProviderRequestListener listener) {
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            manager.removeProviderRequestListener(listener);
        }
    }

    public void injectGnssMeasurementCorrections(android.location.GnssMeasurementCorrections corrections) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.injectGnssMeasurementCorrections(corrections);
        }
    }

    public android.location.GnssCapabilities getGnssCapabilities() {
        return this.mGnssManagerService == null ? new android.location.GnssCapabilities.Builder().build() : this.mGnssManagerService.getGnssCapabilities();
    }

    public java.util.List<android.location.GnssAntennaInfo> getGnssAntennaInfos() {
        if (this.mGnssManagerService == null) {
            return null;
        }
        return this.mGnssManagerService.getGnssAntennaInfos();
    }

    public void addGnssNavigationMessageListener(android.location.IGnssNavigationMessageListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.addGnssNavigationMessageListener(listener, packageName, attributionTag, listenerId);
        }
    }

    public void removeGnssNavigationMessageListener(android.location.IGnssNavigationMessageListener listener) {
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.removeGnssNavigationMessageListener(listener);
        }
    }

    public void sendExtraCommand(java.lang.String provider, java.lang.String command, android.os.Bundle extras) {
        if (mOplusLbsClass != null && !mOplusLbsClass.sendExtraCommand(provider, command, extras)) {
            return;
        }
        com.android.server.location.LocationPermissions.enforceCallingOrSelfLocationPermission(this.mContext, 1);
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_LOCATION_EXTRA_COMMANDS", null);
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager((java.lang.String) java.util.Objects.requireNonNull(provider));
        if (manager != null) {
            manager.sendExtraCommand(android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), (java.lang.String) java.util.Objects.requireNonNull(command), extras);
        }
        this.mInjector.getLocationUsageLogger().logLocationApiUsage(0, 5, provider);
        this.mInjector.getLocationUsageLogger().logLocationApiUsage(1, 5, provider);
    }

    public android.location.provider.ProviderProperties getProviderProperties(java.lang.String provider) {
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        if (provider.equals(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS) && !mOplusLbsClass.isVirtualGpsVisibleOnlyForPad(android.os.Binder.getCallingUid())) {
            throw new java.lang.IllegalArgumentException();
        }
        com.android.internal.util.Preconditions.checkArgument(manager != null, "provider \"" + provider + "\" does not exist");
        return manager.getProperties();
    }

    public boolean isProviderPackage(java.lang.String provider, java.lang.String packageName, java.lang.String attributionTag) {
        isProviderPackage_enforcePermission();
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            if (provider == null || provider.equals(manager.getName())) {
                android.location.util.identity.CallerIdentity identity = manager.getProviderIdentity();
                if (identity == null) {
                    continue;
                } else {
                    if (identity.getPackageName().equals(packageName) && (attributionTag == null || java.util.Objects.equals(identity.getAttributionTag(), attributionTag))) {
                        return true;
                    }
                    if ("network".equals(provider) && packageName != null && packageName.equals("com.google.android.gms")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public java.util.List<java.lang.String> getProviderPackages(java.lang.String provider) {
        getProviderPackages_enforcePermission();
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        if (manager == null) {
            return java.util.Collections.emptyList();
        }
        android.location.util.identity.CallerIdentity identity = manager.getProviderIdentity();
        if (identity == null) {
            return java.util.Collections.emptyList();
        }
        return java.util.Collections.singletonList(identity.getPackageName());
    }

    public void setExtraLocationControllerPackage(java.lang.String packageName) {
        super.setExtraLocationControllerPackage_enforcePermission();
        synchronized (this.mLock) {
            this.mExtraLocationControllerPackage = packageName;
        }
    }

    public java.lang.String getExtraLocationControllerPackage() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mExtraLocationControllerPackage;
        }
        return str;
    }

    public void setExtraLocationControllerPackageEnabled(boolean enabled) {
        super.setExtraLocationControllerPackageEnabled_enforcePermission();
        synchronized (this.mLock) {
            this.mExtraLocationControllerPackageEnabled = enabled;
        }
    }

    public boolean isExtraLocationControllerPackageEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mExtraLocationControllerPackageEnabled && this.mExtraLocationControllerPackage != null;
        }
        return z;
    }

    public void setLocationEnabledForUser(boolean enabled, int userId) {
        android.util.Log.i(TAG, "setLocationEnabledForUser enabled = " + enabled);
        if (mOplusLbsClass != null && enabled && (mOplusLbsClass.isStealthSecurity() || mOplusLbsClass.isSatelliteCommunicationEnable())) {
            return;
        }
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "setLocationEnabledForUser", null);
        this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS", null);
        android.location.LocationManager.invalidateLocalLocationEnabledCaches();
        this.mInjector.getSettingsHelper().setLocationEnabled(enabled, userId2);
    }

    public boolean isLocationEnabledForUser(int userId) {
        if (mOplusLbsClass != null) {
            return mOplusLbsClass.getOplusLocationMode(userId);
        }
        return this.mInjector.getSettingsHelper().isLocationEnabled(android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "isLocationEnabledForUser", null));
    }

    public void setAdasGnssLocationEnabledForUser(final boolean enabled, int userId) {
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "setAdasGnssLocationEnabledForUser", null);
        com.android.server.location.LocationPermissions.enforceCallingOrSelfBypassPermission(this.mContext);
        this.mInjector.getLocationSettings().updateUserSettings(userId2, new java.util.function.Function() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.settings.LocationUserSettings) obj).withAdasGnssLocationEnabled(enabled);
            }
        });
    }

    public boolean isAdasGnssLocationEnabledForUser(int userId) {
        return this.mInjector.getLocationSettings().getUserSettings(android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "isAdasGnssLocationEnabledForUser", null)).isAdasGnssLocationEnabled();
    }

    public boolean isProviderEnabledForUser(java.lang.String provider, int userId) {
        if (mOplusLbsClass != null && mOplusLbsClass.isGpsEnableForSpecialApp(provider, userId, this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid()))) {
            provider = "network";
        }
        return this.mLocalService.isProviderEnabledForUser(provider, userId);
    }

    public void setAutomotiveGnssSuspended(boolean suspended) {
        super.setAutomotiveGnssSuspended_enforcePermission();
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalStateException("setAutomotiveGnssSuspended only allowed on automotive devices");
        }
        if (this.mGnssManagerService != null) {
            this.mGnssManagerService.setAutomotiveGnssSuspended(suspended);
        }
    }

    public boolean isAutomotiveGnssSuspended() {
        super.isAutomotiveGnssSuspended_enforcePermission();
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalStateException("isAutomotiveGnssSuspended only allowed on automotive devices");
        }
        if (this.mGnssManagerService != null) {
            return this.mGnssManagerService.isAutomotiveGnssSuspended();
        }
        return false;
    }

    public boolean isGeocodeAvailable() {
        return this.mGeocodeProvider != null || (mOplusLbsClass != null && mOplusLbsClass.isGeocodeAvailable());
    }

    public void reverseGeocode(android.location.provider.ReverseGeocodeRequest request, android.location.provider.IGeocodeCallback callback) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, request.getCallingPackage(), request.getCallingAttributionTag());
        com.android.internal.util.Preconditions.checkArgument(identity.getUid() == request.getCallingUid());
        if (mOplusLbsClass != null) {
            mOplusLbsClass.reverseGeocode(request, callback);
        } else if (this.mGeocodeProvider != null) {
            this.mGeocodeProvider.reverseGeocode(request, callback);
        } else {
            try {
                callback.onError((java.lang.String) null);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void forwardGeocode(android.location.provider.ForwardGeocodeRequest request, android.location.provider.IGeocodeCallback callback) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, request.getCallingPackage(), request.getCallingAttributionTag());
        com.android.internal.util.Preconditions.checkArgument(identity.getUid() == request.getCallingUid());
        if (mOplusLbsClass != null) {
            mOplusLbsClass.forwardGeocode(request, callback);
        } else if (this.mGeocodeProvider != null) {
            this.mGeocodeProvider.forwardGeocode(request, callback);
        } else {
            try {
                callback.onError((java.lang.String) null);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void addTestProvider(java.lang.String provider, android.location.provider.ProviderProperties properties, java.util.List<java.lang.String> extraAttributionTags, java.lang.String packageName, java.lang.String attributionTag) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinderUnsafe(packageName, attributionTag);
        if (!this.mInjector.getAppOpsHelper().noteOp(58, identity)) {
            return;
        }
        if (mOplusLbsClass != null) {
            mOplusLbsClass.onAddMockProvider(packageName, provider);
        }
        com.android.server.location.provider.LocationProviderManager manager = getOrAddLocationProviderManager(provider);
        manager.setMockProvider(new com.android.server.location.provider.MockLocationProvider(properties, identity, new android.util.ArraySet(extraAttributionTags)));
    }

    public void removeTestProvider(java.lang.String provider, java.lang.String packageName, java.lang.String attributionTag) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinderUnsafe(packageName, attributionTag);
        if (!packageName.equalsIgnoreCase(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) && !this.mInjector.getAppOpsHelper().noteOp(58, identity)) {
            return;
        }
        if (mOplusLbsClass != null) {
            mOplusLbsClass.onRemoveMockProvider(packageName, provider);
        }
        synchronized (this.mLock) {
            com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
            if (manager == null) {
                return;
            }
            manager.setMockProvider(null);
            if (!manager.hasProvider()) {
                removeLocationProviderManager(manager);
            }
        }
    }

    public void setTestProviderLocation(java.lang.String provider, android.location.Location location, java.lang.String packageName, java.lang.String attributionTag) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinderUnsafe(packageName, attributionTag);
        if (!this.mInjector.getAppOpsHelper().noteOp(58, identity)) {
            return;
        }
        com.android.internal.util.Preconditions.checkArgument(location.isComplete(), "incomplete location object, missing timestamp or accuracy?");
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        if (manager == null) {
            throw new java.lang.IllegalArgumentException("provider doesn't exist: " + provider);
        }
        manager.setMockProviderLocation(location);
    }

    public void setTestProviderEnabled(java.lang.String provider, boolean enabled, java.lang.String packageName, java.lang.String attributionTag) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinderUnsafe(packageName, attributionTag);
        if (!packageName.equalsIgnoreCase(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) && !this.mInjector.getAppOpsHelper().noteOp(58, identity)) {
            return;
        }
        com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(provider);
        if (manager == null) {
            throw new java.lang.IllegalArgumentException("provider doesn't exist: " + provider);
        }
        manager.setMockProviderAllowed(enabled);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
        return new com.android.server.location.LocationShellCommand(this.mContext, this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            return;
        }
        if (mOplusLbsClass != null && mOplusLbsClass.dealDumpCommand(pw, args)) {
            return;
        }
        final android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        if (args.length > 0) {
            com.android.server.location.provider.LocationProviderManager manager = getLocationProviderManager(args[0]);
            if (manager != null) {
                ipw.println("Provider:");
                ipw.increaseIndent();
                manager.dump(fd, ipw, args);
                ipw.decreaseIndent();
                ipw.println("Event Log:");
                ipw.increaseIndent();
                com.android.server.location.eventlog.LocationEventLog locationEventLog = com.android.server.location.eventlog.LocationEventLog.EVENT_LOG;
                java.util.Objects.requireNonNull(ipw);
                locationEventLog.iterate(new java.util.function.Consumer() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ipw.println((java.lang.String) obj);
                    }
                }, manager.getName());
                ipw.decreaseIndent();
                return;
            }
            if ("--gnssmetrics".equals(args[0])) {
                if (this.mGnssManagerService != null) {
                    this.mGnssManagerService.dump(fd, ipw, args);
                    return;
                }
                return;
            }
        }
        ipw.println("Location Manager State:");
        ipw.increaseIndent();
        ipw.println("User Info:");
        ipw.increaseIndent();
        this.mInjector.getUserInfoHelper().dump(fd, ipw, args);
        ipw.decreaseIndent();
        ipw.println("Location Settings:");
        ipw.increaseIndent();
        this.mInjector.getSettingsHelper().dump(fd, ipw, args);
        this.mInjector.getLocationSettings().dump(fd, ipw, args);
        ipw.decreaseIndent();
        synchronized (this.mLock) {
            if (this.mExtraLocationControllerPackage != null) {
                ipw.println("Location Controller Extra Package: " + this.mExtraLocationControllerPackage + (this.mExtraLocationControllerPackageEnabled ? " [enabled]" : " [disabled]"));
            }
        }
        ipw.println("Location Providers:");
        ipw.increaseIndent();
        java.util.Iterator<com.android.server.location.provider.LocationProviderManager> it = this.mProviderManagers.iterator();
        while (it.hasNext()) {
            it.next().dump(fd, ipw, args);
        }
        ipw.decreaseIndent();
        ipw.println("Historical Aggregate Location Provider Data:");
        ipw.increaseIndent();
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.AggregateStats>> aggregateStats = com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.copyAggregateStats();
        for (int i = 0; i < aggregateStats.size(); i++) {
            ipw.print(aggregateStats.keyAt(i));
            ipw.println(":");
            ipw.increaseIndent();
            android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.AggregateStats> providerStats = aggregateStats.valueAt(i);
            for (int j = 0; j < providerStats.size(); j++) {
                ipw.print(providerStats.keyAt(j));
                ipw.print(": ");
                providerStats.valueAt(j).updateTotals();
                ipw.println(providerStats.valueAt(j));
            }
            ipw.decreaseIndent();
        }
        ipw.decreaseIndent();
        ipw.println("Historical Aggregate Gnss Measurement Provider Data:");
        ipw.increaseIndent();
        android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats> gnssAggregateStats = com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.copyGnssMeasurementAggregateStats();
        for (int i2 = 0; i2 < gnssAggregateStats.size(); i2++) {
            ipw.print(gnssAggregateStats.keyAt(i2));
            ipw.print(": ");
            gnssAggregateStats.valueAt(i2).updateTotals();
            ipw.println(gnssAggregateStats.valueAt(i2));
        }
        ipw.decreaseIndent();
        if (this.mGnssManagerService != null) {
            ipw.println("GNSS Manager:");
            ipw.increaseIndent();
            this.mGnssManagerService.dump(fd, ipw, args);
            ipw.decreaseIndent();
        }
        ipw.println("Geofence Manager:");
        ipw.increaseIndent();
        this.mGeofenceManager.dump(fd, ipw, args);
        ipw.decreaseIndent();
        ipw.println("Event Log:");
        ipw.increaseIndent();
        com.android.server.location.eventlog.LocationEventLog locationEventLog2 = com.android.server.location.eventlog.LocationEventLog.EVENT_LOG;
        java.util.Objects.requireNonNull(ipw);
        locationEventLog2.iterate(new java.util.function.Consumer() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ipw.println((java.lang.String) obj);
            }
        });
        ipw.decreaseIndent();
        if (mOplusLbsClass != null) {
            mOplusLbsClass.dumpOplusContent(pw);
        }
    }

    @Override // com.android.server.location.provider.LocationProviderManager.StateChangedListener
    public void onStateChanged(java.lang.String provider, com.android.server.location.provider.AbstractLocationProvider.State oldState, com.android.server.location.provider.AbstractLocationProvider.State newState) {
        if (!java.util.Objects.equals(oldState.identity, newState.identity)) {
            refreshAppOpsRestrictions(-1);
        }
        if (!oldState.extraAttributionTags.equals(newState.extraAttributionTags) || !java.util.Objects.equals(oldState.identity, newState.identity)) {
            synchronized (this.mLock) {
                final android.location.LocationManagerInternal.LocationPackageTagsListener listener = this.mLocationTagsChangedListener;
                if (listener != null) {
                    final int oldUid = oldState.identity != null ? oldState.identity.getUid() : -1;
                    final int newUid = newState.identity != null ? newState.identity.getUid() : -1;
                    if (oldUid != -1) {
                        final android.os.PackageTagsList tags = calculateAppOpsLocationSourceTags(oldUid);
                        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                listener.onLocationPackageTagsChanged(oldUid, tags);
                            }
                        });
                    }
                    if (newUid != -1 && newUid != oldUid) {
                        final android.os.PackageTagsList tags2 = calculateAppOpsLocationSourceTags(newUid);
                        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.LocationManagerService$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                listener.onLocationPackageTagsChanged(newUid, tags2);
                            }
                        });
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void refreshAppOpsRestrictions(int userId) {
        if (userId == -1) {
            int[] runningUserIds = this.mInjector.getUserInfoHelper().getRunningUserIds();
            for (int i : runningUserIds) {
                refreshAppOpsRestrictions(i);
            }
            return;
        }
        com.android.internal.util.Preconditions.checkArgument(userId >= 0);
        boolean enabled = this.mInjector.getSettingsHelper().isLocationEnabled(userId);
        android.os.PackageTagsList allowedPackages = null;
        if (!enabled) {
            android.os.PackageTagsList.Builder builder = new android.os.PackageTagsList.Builder();
            for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
                android.location.util.identity.CallerIdentity identity = manager.getProviderIdentity();
                if (identity != null) {
                    builder.add(identity.getPackageName(), identity.getAttributionTag());
                }
            }
            builder.add(this.mInjector.getSettingsHelper().getIgnoreSettingsAllowlist());
            builder.add(this.mInjector.getSettingsHelper().getAdasAllowlist());
            allowedPackages = builder.build();
        }
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) java.util.Objects.requireNonNull((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class));
        appOpsManager.setUserRestrictionForUser(0, !enabled, this, allowedPackages, userId);
        appOpsManager.setUserRestrictionForUser(1, !enabled, this, allowedPackages, userId);
    }

    android.os.PackageTagsList calculateAppOpsLocationSourceTags(int uid) {
        android.os.PackageTagsList.Builder builder = new android.os.PackageTagsList.Builder();
        for (com.android.server.location.provider.LocationProviderManager manager : this.mProviderManagers) {
            com.android.server.location.provider.AbstractLocationProvider.State managerState = manager.getState();
            if (managerState.identity != null && managerState.identity.getUid() == uid) {
                builder.add(managerState.identity.getPackageName(), managerState.extraAttributionTags);
                if (managerState.extraAttributionTags.isEmpty() || managerState.identity.getAttributionTag() != null) {
                    builder.add(managerState.identity.getPackageName(), managerState.identity.getAttributionTag());
                } else {
                    android.util.Log.e(TAG, manager.getName() + " provider has specified a null attribution tag and a non-empty set of extra attribution tags - dropping the null attribution tag");
                }
            }
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class LocalService extends android.location.LocationManagerInternal {
        LocalService() {
        }

        public boolean isProviderEnabledForUser(java.lang.String provider, int userId) {
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "isProviderEnabledForUser", null);
            com.android.server.location.provider.LocationProviderManager manager = com.android.server.location.LocationManagerService.this.getLocationProviderManager(provider);
            if (manager == null) {
                return false;
            }
            return manager.isEnabled(userId2);
        }

        public void addProviderEnabledListener(java.lang.String provider, android.location.LocationManagerInternal.ProviderEnabledListener listener) {
            com.android.server.location.provider.LocationProviderManager manager = (com.android.server.location.provider.LocationProviderManager) java.util.Objects.requireNonNull(com.android.server.location.LocationManagerService.this.getLocationProviderManager(provider));
            manager.addEnabledListener(listener);
        }

        public void removeProviderEnabledListener(java.lang.String provider, android.location.LocationManagerInternal.ProviderEnabledListener listener) {
            com.android.server.location.provider.LocationProviderManager manager = (com.android.server.location.provider.LocationProviderManager) java.util.Objects.requireNonNull(com.android.server.location.LocationManagerService.this.getLocationProviderManager(provider));
            manager.removeEnabledListener(listener);
        }

        public boolean isProvider(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
            for (com.android.server.location.provider.LocationProviderManager manager : com.android.server.location.LocationManagerService.this.mProviderManagers) {
                if (provider == null || provider.equals(manager.getName())) {
                    if (identity.equals(manager.getProviderIdentity()) && manager.isVisibleToCaller()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public android.location.LocationTime getGnssTimeMillis() {
            android.location.Location location;
            com.android.server.location.provider.LocationProviderManager gpsManager = com.android.server.location.LocationManagerService.this.getLocationProviderManager(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS);
            if (gpsManager == null || (location = gpsManager.getLastLocationUnsafe(-1, 2, false, Long.MAX_VALUE)) == null) {
                return null;
            }
            return new android.location.LocationTime(location.getTime(), location.getElapsedRealtimeNanos());
        }

        public void setLocationPackageTagsListener(final android.location.LocationManagerInternal.LocationPackageTagsListener listener) {
            synchronized (com.android.server.location.LocationManagerService.this.mLock) {
                com.android.server.location.LocationManagerService.this.mLocationTagsChangedListener = listener;
                if (listener != null) {
                    android.util.ArraySet<java.lang.Integer> uids = new android.util.ArraySet<>(com.android.server.location.LocationManagerService.this.mProviderManagers.size());
                    for (com.android.server.location.provider.LocationProviderManager manager : com.android.server.location.LocationManagerService.this.mProviderManagers) {
                        android.location.util.identity.CallerIdentity identity = manager.getProviderIdentity();
                        if (identity != null) {
                            uids.add(java.lang.Integer.valueOf(identity.getUid()));
                        }
                    }
                    java.util.Iterator<java.lang.Integer> it = uids.iterator();
                    while (it.hasNext()) {
                        final int uid = it.next().intValue();
                        final android.os.PackageTagsList tags = com.android.server.location.LocationManagerService.this.calculateAppOpsLocationSourceTags(uid);
                        if (!tags.isEmpty()) {
                            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.LocationManagerService$LocalService$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    listener.onLocationPackageTagsChanged(uid, tags);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private static final class SystemInjector implements com.android.server.location.injector.Injector {
        private final com.android.server.location.injector.AlarmHelper mAlarmHelper;
        private final com.android.server.location.injector.SystemAppForegroundHelper mAppForegroundHelper;
        private final com.android.server.location.injector.SystemAppOpsHelper mAppOpsHelper;
        private final android.content.Context mContext;
        private final com.android.server.location.injector.SystemDeviceIdleHelper mDeviceIdleHelper;
        private com.android.server.location.injector.SystemEmergencyHelper mEmergencyCallHelper;
        private final com.android.server.location.injector.SystemLocationPermissionsHelper mLocationPermissionsHelper;
        private final com.android.server.location.injector.SystemLocationPowerSaveModeHelper mLocationPowerSaveModeHelper;
        private final com.android.server.location.settings.LocationSettings mLocationSettings;
        private final com.android.server.location.injector.PackageResetHelper mPackageResetHelper;
        private final com.android.server.location.injector.SystemScreenInteractiveHelper mScreenInteractiveHelper;
        private final com.android.server.location.injector.SystemSettingsHelper mSettingsHelper;
        private boolean mSystemReady;
        private final com.android.server.location.injector.SystemUserInfoHelper mUserInfoHelper;
        private final com.android.server.location.injector.SystemDeviceStationaryHelper mDeviceStationaryHelper = new com.android.server.location.injector.SystemDeviceStationaryHelper();
        private final com.android.server.location.injector.LocationUsageLogger mLocationUsageLogger = new com.android.server.location.injector.LocationUsageLogger();

        SystemInjector(android.content.Context context, com.android.server.location.injector.SystemUserInfoHelper userInfoHelper) {
            this.mContext = context;
            this.mUserInfoHelper = userInfoHelper;
            this.mLocationSettings = new com.android.server.location.settings.LocationSettings(context);
            this.mAlarmHelper = new com.android.server.location.injector.SystemAlarmHelper(context);
            this.mAppOpsHelper = new com.android.server.location.injector.SystemAppOpsHelper(context);
            this.mLocationPermissionsHelper = new com.android.server.location.injector.SystemLocationPermissionsHelper(context, this.mAppOpsHelper);
            this.mSettingsHelper = new com.android.server.location.injector.SystemSettingsHelper(context);
            this.mAppForegroundHelper = new com.android.server.location.injector.SystemAppForegroundHelper(context);
            this.mLocationPowerSaveModeHelper = new com.android.server.location.injector.SystemLocationPowerSaveModeHelper(context);
            this.mScreenInteractiveHelper = new com.android.server.location.injector.SystemScreenInteractiveHelper(context);
            this.mDeviceIdleHelper = new com.android.server.location.injector.SystemDeviceIdleHelper(context);
            this.mPackageResetHelper = new com.android.server.location.injector.SystemPackageResetHelper(context);
        }

        synchronized void onSystemReady() {
            this.mUserInfoHelper.onSystemReady();
            this.mAppOpsHelper.onSystemReady();
            this.mLocationPermissionsHelper.onSystemReady();
            this.mSettingsHelper.onSystemReady();
            this.mAppForegroundHelper.onSystemReady();
            this.mLocationPowerSaveModeHelper.onSystemReady();
            this.mScreenInteractiveHelper.onSystemReady();
            this.mDeviceStationaryHelper.onSystemReady();
            this.mDeviceIdleHelper.onSystemReady();
            if (this.mEmergencyCallHelper != null) {
                this.mEmergencyCallHelper.onSystemReady();
            }
            this.mSystemReady = true;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.UserInfoHelper getUserInfoHelper() {
            return this.mUserInfoHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.settings.LocationSettings getLocationSettings() {
            return this.mLocationSettings;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.AlarmHelper getAlarmHelper() {
            return this.mAlarmHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.AppOpsHelper getAppOpsHelper() {
            return this.mAppOpsHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.LocationPermissionsHelper getLocationPermissionsHelper() {
            return this.mLocationPermissionsHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.SettingsHelper getSettingsHelper() {
            return this.mSettingsHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.AppForegroundHelper getAppForegroundHelper() {
            return this.mAppForegroundHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.LocationPowerSaveModeHelper getLocationPowerSaveModeHelper() {
            return this.mLocationPowerSaveModeHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.ScreenInteractiveHelper getScreenInteractiveHelper() {
            return this.mScreenInteractiveHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.DeviceStationaryHelper getDeviceStationaryHelper() {
            return this.mDeviceStationaryHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.DeviceIdleHelper getDeviceIdleHelper() {
            return this.mDeviceIdleHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public synchronized com.android.server.location.injector.EmergencyHelper getEmergencyHelper() {
            if (this.mEmergencyCallHelper == null) {
                this.mEmergencyCallHelper = new com.android.server.location.injector.SystemEmergencyHelper(this.mContext);
                if (this.mSystemReady) {
                    this.mEmergencyCallHelper.onSystemReady();
                }
            }
            return this.mEmergencyCallHelper;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.LocationUsageLogger getLocationUsageLogger() {
            return this.mLocationUsageLogger;
        }

        @Override // com.android.server.location.injector.Injector
        public com.android.server.location.injector.PackageResetHelper getPackageResetHelper() {
            return this.mPackageResetHelper;
        }
    }

    public com.android.server.location.ILocationManagerServiceWrapper getWrapper() {
        return this.mLmsWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void oplusSystemReady(com.android.server.location.LocationManagerService service) {
        this.mLocationFreeze = (com.android.server.location.interfaces.ILocationFreezeProc) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ILocationFreezeProc.DEFAULT, this.mContext);
        this.mVirtualProvider = (com.android.server.location.interfaces.IVirtualGnssLocationProvider) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IVirtualGnssLocationProvider.DEFAULT, this.mContext);
        this.mVirtualGnssHal = (com.android.server.location.interfaces.IVirtualGnssHal) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IVirtualGnssHal.DEFAULT, this.mContext);
        mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext);
        if (mOplusLbsClass != null) {
            mOplusLbsClass.oplusSystemReady(service);
            mOplusLbsClass.initFlpCoordinator(this.mContext);
            com.android.server.location.provider.LocationProviderManager.oplusSystemReady(this.mContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void oplusSystemThirdPartyAppsCanStart() {
        if (mOplusLbsClass != null) {
            mOplusLbsClass.oplusSystemThirdPartyAppsCanStart();
        }
    }

    private class LocationManagerServiceWrapper implements com.android.server.location.ILocationManagerServiceWrapper {
        private LocationManagerServiceWrapper() {
        }

        @Override // com.android.server.location.ILocationManagerServiceWrapper
        public com.android.server.location.provider.LocationProviderManager getLocationProviderManager(java.lang.String providerName) {
            return com.android.server.location.LocationManagerService.this.getLocationProviderManager(providerName);
        }

        @Override // com.android.server.location.ILocationManagerServiceWrapper
        public void addLocationProviderManager(com.android.server.location.provider.LocationProviderManager locationProviderManager, com.android.server.location.provider.AbstractLocationProvider abstractLocationProvider) {
            com.android.server.location.LocationManagerService.this.addLocationProviderManager(locationProviderManager, abstractLocationProvider);
        }

        @Override // com.android.server.location.ILocationManagerServiceWrapper
        public void removeLocationProviderManager(com.android.server.location.provider.LocationProviderManager locationProviderManager) {
            com.android.server.location.LocationManagerService.this.removeLocationProviderManager(locationProviderManager);
        }

        @Override // com.android.server.location.ILocationManagerServiceWrapper
        public com.android.server.location.provider.LocationProviderManager creatLocationProviderManager(java.lang.String providerName) {
            return new com.android.server.location.provider.LocationProviderManager(com.android.server.location.LocationManagerService.this.mContext, com.android.server.location.LocationManagerService.this.mInjector, providerName, com.android.server.location.LocationManagerService.this.mPassiveManager);
        }

        @Override // com.android.server.location.ILocationManagerServiceWrapper
        public android.location.IGpsGeofenceHardware getGpsGeofenceHardware() {
            if (com.android.server.location.LocationManagerService.this.mGnssManagerService != null) {
                return com.android.server.location.LocationManagerService.this.mGnssManagerService.getGnssGeofenceProxy();
            }
            return null;
        }
    }
}
