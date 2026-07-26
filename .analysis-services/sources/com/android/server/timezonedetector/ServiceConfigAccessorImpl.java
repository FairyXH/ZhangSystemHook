package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class ServiceConfigAccessorImpl implements com.android.server.timezonedetector.ServiceConfigAccessor {
    private static com.android.server.timezonedetector.ServiceConfigAccessor sInstance;
    private final java.util.List<com.android.server.timezonedetector.StateChangeListener> mConfigurationInternalListeners = new java.util.ArrayList();
    private final android.content.Context mContext;
    private final android.content.ContentResolver mCr;
    private final android.location.LocationManager mLocationManager;
    private boolean mRecordStateChangesForTests;
    private final com.android.server.timedetector.ServerFlags mServerFlags;
    private java.lang.String mTestPrimaryLocationTimeZoneProviderMode;
    private java.lang.String mTestPrimaryLocationTimeZoneProviderPackageName;
    private java.lang.String mTestSecondaryLocationTimeZoneProviderMode;
    private java.lang.String mTestSecondaryLocationTimeZoneProviderPackageName;
    private final android.os.UserManager mUserManager;
    private static final java.util.Set<java.lang.String> CONFIGURATION_INTERNAL_SERVER_FLAGS_KEYS_TO_WATCH = java.util.Set.of(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED, com.android.server.timedetector.ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED, com.android.server.timedetector.ServerFlags.KEY_ENHANCED_METRICS_COLLECTION_ENABLED, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT, com.android.server.timedetector.ServerFlags.KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED);
    private static final java.util.Set<java.lang.String> LOCATION_TIME_ZONE_MANAGER_SERVER_FLAGS_KEYS_TO_WATCH = java.util.Set.of(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS, com.android.server.timedetector.ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS, com.android.server.timedetector.ServerFlags.KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS, com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS);
    private static final java.time.Duration DEFAULT_LTZP_INITIALIZATION_TIMEOUT = java.time.Duration.ofMinutes(5);
    private static final java.time.Duration DEFAULT_LTZP_INITIALIZATION_TIMEOUT_FUZZ = java.time.Duration.ofMinutes(1);
    private static final java.time.Duration DEFAULT_LTZP_UNCERTAINTY_DELAY = java.time.Duration.ofMinutes(5);
    private static final java.time.Duration DEFAULT_LTZP_EVENT_FILTER_AGE_THRESHOLD = java.time.Duration.ofMinutes(1);
    private static final java.lang.Object SLOCK = new java.lang.Object();

    private ServiceConfigAccessorImpl(android.content.Context context) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mCr = context.getContentResolver();
        this.mUserManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        this.mLocationManager = (android.location.LocationManager) context.getSystemService(android.location.LocationManager.class);
        this.mServerFlags = com.android.server.timedetector.ServerFlags.getInstance(this.mContext);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_SWITCHED");
        filter.addAction("android.location.MODE_CHANGED");
        this.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.timezonedetector.ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        }, filter, null, null);
        android.os.Handler mainThreadHandler = this.mContext.getMainThreadHandler();
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        android.database.ContentObserver contentObserver = new android.database.ContentObserver(mainThreadHandler) { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.timezonedetector.ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        };
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("auto_time_zone"), true, contentObserver);
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("auto_time_zone_explicit"), true, contentObserver);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("location_time_zone_detection_enabled"), true, contentObserver, -1);
        this.mServerFlags.addListener(new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.handleConfigurationInternalChangeOnMainThread();
            }
        }, CONFIGURATION_INTERNAL_SERVER_FLAGS_KEYS_TO_WATCH);
        this.mUserManager.addUserRestrictionsListener(new com.android.server.timezonedetector.ServiceConfigAccessorImpl.AnonymousClass3(mainThreadHandler));
    }

    /* JADX INFO: renamed from: com.android.server.timezonedetector.ServiceConfigAccessorImpl$3, reason: invalid class name */
    class AnonymousClass3 extends android.os.IUserRestrictionsListener.Stub {
        final /* synthetic */ android.os.Handler val$mainThreadHandler;

        AnonymousClass3(android.os.Handler handler) {
            this.val$mainThreadHandler = handler;
        }

        public void onUserRestrictionsChanged(final int userId, final android.os.Bundle newRestrictions, final android.os.Bundle prevRestrictions) {
            this.val$mainThreadHandler.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.ServiceConfigAccessorImpl$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserRestrictionsChanged$0(userId, newRestrictions, prevRestrictions);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserRestrictionsChanged$0(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
            com.android.server.timezonedetector.ServiceConfigAccessorImpl.this.handleUserRestrictionsChangeOnMainThread(userId, newRestrictions, prevRestrictions);
        }
    }

    public static com.android.server.timezonedetector.ServiceConfigAccessor getInstance(android.content.Context context) {
        com.android.server.timezonedetector.ServiceConfigAccessor serviceConfigAccessor;
        synchronized (SLOCK) {
            if (sInstance == null) {
                sInstance = new com.android.server.timezonedetector.ServiceConfigAccessorImpl(context);
            }
            serviceConfigAccessor = sInstance;
        }
        return serviceConfigAccessor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConfigurationInternalChangeOnMainThread() {
        java.util.List<com.android.server.timezonedetector.StateChangeListener> configurationInternalListeners;
        synchronized (this) {
            configurationInternalListeners = new java.util.ArrayList<>(this.mConfigurationInternalListeners);
        }
        for (com.android.server.timezonedetector.StateChangeListener changeListener : configurationInternalListeners) {
            changeListener.onChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserRestrictionsChangeOnMainThread(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
        handleConfigurationInternalChangeOnMainThread();
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void addConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mConfigurationInternalListeners.add((com.android.server.timezonedetector.StateChangeListener) java.util.Objects.requireNonNull(listener));
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void removeConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mConfigurationInternalListeners.remove(java.util.Objects.requireNonNull(listener));
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized com.android.server.timezonedetector.ConfigurationInternal getCurrentUserConfigurationInternal() {
        int currentUserId;
        currentUserId = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentUserId();
        return getConfigurationInternal(currentUserId);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean updateConfiguration(int userId, android.app.time.TimeZoneConfiguration requestedConfigurationUpdates, boolean bypassUserPolicyChecks) {
        java.util.Objects.requireNonNull(requestedConfigurationUpdates);
        com.android.server.timezonedetector.ConfigurationInternal configurationInternal = getConfigurationInternal(userId);
        android.app.time.TimeZoneCapabilities capabilities = configurationInternal.asCapabilities(bypassUserPolicyChecks);
        android.app.time.TimeZoneConfiguration oldConfiguration = configurationInternal.asConfiguration();
        android.app.time.TimeZoneConfiguration newConfiguration = capabilities.tryApplyConfigChanges(oldConfiguration, requestedConfigurationUpdates);
        if (newConfiguration == null) {
            return false;
        }
        storeConfiguration(userId, requestedConfigurationUpdates, newConfiguration);
        return true;
    }

    private void storeConfiguration(int userId, android.app.time.TimeZoneConfiguration requestedConfigurationUpdates, android.app.time.TimeZoneConfiguration newConfiguration) {
        java.util.Objects.requireNonNull(newConfiguration);
        if (isAutoDetectionFeatureSupported()) {
            if (requestedConfigurationUpdates.hasIsAutoDetectionEnabled()) {
                android.provider.Settings.Global.putInt(this.mCr, "auto_time_zone_explicit", 1);
            }
            boolean autoDetectionEnabled = newConfiguration.isAutoDetectionEnabled();
            setAutoDetectionEnabledIfRequired(autoDetectionEnabled);
            if (getGeoDetectionSettingEnabledOverride().isEmpty() && isGeoTimeZoneDetectionFeatureSupported() && isTelephonyTimeZoneDetectionFeatureSupported()) {
                boolean geoDetectionEnabledSetting = newConfiguration.isGeoDetectionEnabled();
                setGeoDetectionEnabledSettingIfRequired(userId, geoDetectionEnabledSetting);
            }
        }
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized com.android.server.timezonedetector.ConfigurationInternal getConfigurationInternal(int userId) {
        return new com.android.server.timezonedetector.ConfigurationInternal.Builder().setUserId(userId).setTelephonyDetectionFeatureSupported(isTelephonyTimeZoneDetectionFeatureSupported()).setGeoDetectionFeatureSupported(isGeoTimeZoneDetectionFeatureSupported()).setTelephonyFallbackSupported(isTelephonyFallbackSupported()).setGeoDetectionRunInBackgroundEnabled(getGeoDetectionRunInBackgroundEnabled()).setEnhancedMetricsCollectionEnabled(isEnhancedMetricsCollectionEnabled()).setAutoDetectionEnabledSetting(getAutoDetectionEnabledSetting()).setUserConfigAllowed(isUserConfigAllowed(userId)).setLocationEnabledSetting(getLocationEnabledSetting(userId)).setGeoDetectionEnabledSetting(getGeoDetectionEnabledSetting(userId)).build();
    }

    private void setAutoDetectionEnabledIfRequired(boolean z) {
        if (getAutoDetectionEnabledSetting() != z) {
            android.provider.Settings.Global.putInt(this.mCr, "auto_time_zone", z ? 1 : 0);
        }
    }

    private boolean getLocationEnabledSetting(int userId) {
        return this.mLocationManager.isLocationEnabledForUser(android.os.UserHandle.of(userId));
    }

    private boolean isUserConfigAllowed(int userId) {
        android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
        return !this.mUserManager.hasUserRestriction("no_config_date_time", userHandle);
    }

    private boolean getAutoDetectionEnabledSetting() {
        boolean z = android.provider.Settings.Global.getInt(this.mCr, "auto_time_zone", 1) > 0;
        java.util.Optional<java.lang.Boolean> optionalBoolean = this.mServerFlags.getOptionalBoolean(com.android.server.timedetector.ServerFlags.KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT);
        if (optionalBoolean.isPresent() && android.provider.Settings.Global.getInt(this.mCr, "auto_time_zone_explicit", 0) == 0) {
            boolean zBooleanValue = optionalBoolean.get().booleanValue();
            if (zBooleanValue != z) {
                android.provider.Settings.Global.putInt(this.mCr, "auto_time_zone", zBooleanValue ? 1 : 0);
            }
            return zBooleanValue;
        }
        return z;
    }

    private boolean getGeoDetectionEnabledSetting(int i) {
        java.util.Optional<java.lang.Boolean> geoDetectionSettingEnabledOverride = getGeoDetectionSettingEnabledOverride();
        if (geoDetectionSettingEnabledOverride.isPresent()) {
            return geoDetectionSettingEnabledOverride.get().booleanValue();
        }
        return android.provider.Settings.Secure.getIntForUser(this.mCr, "location_time_zone_detection_enabled", isGeoDetectionEnabledForUsersByDefault() ? 1 : 0, i) != 0;
    }

    private void setGeoDetectionEnabledSettingIfRequired(int i, boolean z) {
        if (getGeoDetectionEnabledSetting(i) != z) {
            android.provider.Settings.Secure.putIntForUser(this.mCr, "location_time_zone_detection_enabled", z ? 1 : 0, i);
        }
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public void addLocationTimeZoneManagerConfigListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mServerFlags.addListener(listener, LOCATION_TIME_ZONE_MANAGER_SERVER_FLAGS_KEYS_TO_WATCH);
    }

    private boolean isAutoDetectionFeatureSupported() {
        return isTelephonyTimeZoneDetectionFeatureSupported() || isGeoTimeZoneDetectionFeatureSupported();
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isTelephonyTimeZoneDetectionFeatureSupported() {
        return getConfigBoolean(android.R.bool.config_enablePopulationDensityProviderOverlay) && this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isGeoTimeZoneDetectionFeatureSupportedInConfig() {
        return this.mContext.getResources().getBoolean(android.R.bool.config_enableFusedLocationOverlay);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isGeoTimeZoneDetectionFeatureSupported() {
        return isGeoTimeZoneDetectionFeatureSupportedInConfig() && isGeoTimeZoneDetectionFeatureSupportedInternal() && atLeastOneProviderIsEnabled();
    }

    private boolean atLeastOneProviderIsEnabled() {
        return (java.util.Objects.equals(getPrimaryLocationTimeZoneProviderMode(), com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED) && java.util.Objects.equals(getSecondaryLocationTimeZoneProviderMode(), com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED)) ? false : true;
    }

    private boolean isGeoTimeZoneDetectionFeatureSupportedInternal() {
        return this.mServerFlags.getBoolean(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED, true);
    }

    private boolean getGeoDetectionRunInBackgroundEnabled() {
        return this.mServerFlags.getBoolean(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED, false);
    }

    private boolean isEnhancedMetricsCollectionEnabled() {
        return this.mServerFlags.getBoolean(com.android.server.timedetector.ServerFlags.KEY_ENHANCED_METRICS_COLLECTION_ENABLED, false);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized java.lang.String getPrimaryLocationTimeZoneProviderPackageName() {
        if (this.mTestPrimaryLocationTimeZoneProviderMode != null) {
            return this.mTestPrimaryLocationTimeZoneProviderPackageName;
        }
        return this.mContext.getResources().getString(android.R.string.config_rearDisplayPhysicalAddress);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void setTestPrimaryLocationTimeZoneProviderPackageName(java.lang.String testPrimaryLocationTimeZoneProviderPackageName) {
        this.mTestPrimaryLocationTimeZoneProviderPackageName = testPrimaryLocationTimeZoneProviderPackageName;
        this.mTestPrimaryLocationTimeZoneProviderMode = this.mTestPrimaryLocationTimeZoneProviderPackageName == null ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED;
        this.mContext.getMainThreadHandler().post(new com.android.server.timezonedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(this));
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean isTestPrimaryLocationTimeZoneProvider() {
        return this.mTestPrimaryLocationTimeZoneProviderMode != null;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized java.lang.String getSecondaryLocationTimeZoneProviderPackageName() {
        if (this.mTestSecondaryLocationTimeZoneProviderMode != null) {
            return this.mTestSecondaryLocationTimeZoneProviderPackageName;
        }
        return this.mContext.getResources().getString(android.R.string.config_servicesExtensionPackage);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void setTestSecondaryLocationTimeZoneProviderPackageName(java.lang.String testSecondaryLocationTimeZoneProviderPackageName) {
        this.mTestSecondaryLocationTimeZoneProviderPackageName = testSecondaryLocationTimeZoneProviderPackageName;
        this.mTestSecondaryLocationTimeZoneProviderMode = this.mTestSecondaryLocationTimeZoneProviderPackageName == null ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED;
        this.mContext.getMainThreadHandler().post(new com.android.server.timezonedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(this));
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean isTestSecondaryLocationTimeZoneProvider() {
        return this.mTestSecondaryLocationTimeZoneProviderMode != null;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void setRecordStateChangesForTests(boolean enabled) {
        this.mRecordStateChangesForTests = enabled;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized boolean getRecordStateChangesForTests() {
        return this.mRecordStateChangesForTests;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized java.lang.String getPrimaryLocationTimeZoneProviderMode() {
        if (this.mTestPrimaryLocationTimeZoneProviderMode != null) {
            return this.mTestPrimaryLocationTimeZoneProviderMode;
        }
        return this.mServerFlags.getOptionalString(com.android.server.timedetector.ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE).orElse(getPrimaryLocationTimeZoneProviderModeFromConfig());
    }

    private synchronized java.lang.String getPrimaryLocationTimeZoneProviderModeFromConfig() {
        return getConfigBoolean(android.R.bool.config_enableMotionPrediction) ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized java.lang.String getSecondaryLocationTimeZoneProviderMode() {
        if (this.mTestSecondaryLocationTimeZoneProviderMode != null) {
            return this.mTestSecondaryLocationTimeZoneProviderMode;
        }
        return this.mServerFlags.getOptionalString(com.android.server.timedetector.ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE).orElse(getSecondaryLocationTimeZoneProviderModeFromConfig());
    }

    private synchronized java.lang.String getSecondaryLocationTimeZoneProviderModeFromConfig() {
        return getConfigBoolean(android.R.bool.config_enableNewAutoSelectNetworkUI) ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public boolean isGeoDetectionEnabledForUsersByDefault() {
        return this.mServerFlags.getBoolean(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT, false);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public java.util.Optional<java.lang.Boolean> getGeoDetectionSettingEnabledOverride() {
        return this.mServerFlags.getOptionalBoolean(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public java.time.Duration getLocationTimeZoneProviderInitializationTimeout() {
        return this.mServerFlags.getDurationFromMillis(com.android.server.timedetector.ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS, DEFAULT_LTZP_INITIALIZATION_TIMEOUT);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public java.time.Duration getLocationTimeZoneProviderInitializationTimeoutFuzz() {
        return this.mServerFlags.getDurationFromMillis(com.android.server.timedetector.ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS, DEFAULT_LTZP_INITIALIZATION_TIMEOUT_FUZZ);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public java.time.Duration getLocationTimeZoneUncertaintyDelay() {
        return this.mServerFlags.getDurationFromMillis(com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS, DEFAULT_LTZP_UNCERTAINTY_DELAY);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public java.time.Duration getLocationTimeZoneProviderEventFilteringAgeThreshold() {
        return this.mServerFlags.getDurationFromMillis(com.android.server.timedetector.ServerFlags.KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS, DEFAULT_LTZP_EVENT_FILTER_AGE_THRESHOLD);
    }

    @Override // com.android.server.timezonedetector.ServiceConfigAccessor
    public synchronized void resetVolatileTestConfig() {
        this.mTestPrimaryLocationTimeZoneProviderPackageName = null;
        this.mTestPrimaryLocationTimeZoneProviderMode = null;
        this.mTestSecondaryLocationTimeZoneProviderPackageName = null;
        this.mTestSecondaryLocationTimeZoneProviderMode = null;
        this.mRecordStateChangesForTests = false;
        this.mContext.getMainThreadHandler().post(new com.android.server.timezonedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(this));
    }

    private boolean isTelephonyFallbackSupported() {
        return this.mServerFlags.getBoolean(com.android.server.timedetector.ServerFlags.KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED, getConfigBoolean(android.R.bool.config_skipActivityRelaunchWhenDocking));
    }

    private boolean getConfigBoolean(int providerEnabledConfigId) {
        android.content.res.Resources resources = this.mContext.getResources();
        return resources.getBoolean(providerEnabledConfigId);
    }
}
