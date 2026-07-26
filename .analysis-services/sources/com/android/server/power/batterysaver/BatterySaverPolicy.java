package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public class BatterySaverPolicy extends android.database.ContentObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
    static final boolean DEBUG = false;
    static final java.lang.String KEY_ADJUST_BRIGHTNESS_FACTOR = "adjust_brightness_factor";
    static final java.lang.String KEY_ADVERTISE_IS_ENABLED = "advertise_is_enabled";

    @java.lang.Deprecated
    private static final java.lang.String KEY_CPU_FREQ_INTERACTIVE = "cpufreq-i";

    @java.lang.Deprecated
    private static final java.lang.String KEY_CPU_FREQ_NONINTERACTIVE = "cpufreq-n";
    static final java.lang.String KEY_DEFER_FULL_BACKUP = "defer_full_backup";
    static final java.lang.String KEY_DEFER_KEYVALUE_BACKUP = "defer_keyvalue_backup";
    static final java.lang.String KEY_DISABLE_ANIMATION = "disable_animation";
    static final java.lang.String KEY_DISABLE_AOD = "disable_aod";
    static final java.lang.String KEY_DISABLE_LAUNCH_BOOST = "disable_launch_boost";
    static final java.lang.String KEY_DISABLE_OPTIONAL_SENSORS = "disable_optional_sensors";
    static final java.lang.String KEY_DISABLE_VIBRATION = "disable_vibration";
    static final java.lang.String KEY_ENABLE_BRIGHTNESS_ADJUSTMENT = "enable_brightness_adjustment";
    static final java.lang.String KEY_ENABLE_DATASAVER = "enable_datasaver";
    static final java.lang.String KEY_ENABLE_FIREWALL = "enable_firewall";
    static final java.lang.String KEY_ENABLE_NIGHT_MODE = "enable_night_mode";
    static final java.lang.String KEY_ENABLE_QUICK_DOZE = "enable_quick_doze";
    static final java.lang.String KEY_FORCE_ALL_APPS_STANDBY = "force_all_apps_standby";
    static final java.lang.String KEY_FORCE_BACKGROUND_CHECK = "force_background_check";
    static final java.lang.String KEY_LOCATION_MODE = "location_mode";
    static final java.lang.String KEY_SOUNDTRIGGER_MODE = "soundtrigger_mode";
    private static final java.lang.String KEY_SUFFIX_ADAPTIVE = "_adaptive";
    static final int POLICY_LEVEL_ADAPTIVE = 1;
    static final int POLICY_LEVEL_FULL = 2;
    static final int POLICY_LEVEL_OFF = 0;
    private static final java.lang.String TAG = "BatterySaverPolicy";
    private final com.android.server.power.batterysaver.BatterySaverPolicy.Policy DEFAULT_FULL_POLICY;
    final com.android.server.power.batterysaver.BatterySaverPolicy.PolicyBoolean mAccessibilityEnabled;
    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy mAdaptivePolicy;
    final com.android.server.power.batterysaver.BatterySaverPolicy.PolicyBoolean mAutomotiveProjectionActive;
    private com.android.server.power.batterysaver.IBatterySaverPolicyExt mBSPolicyExt;
    private final com.android.server.power.batterysaver.BatterySavingStats mBatterySavingStats;
    private final android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy mDefaultAdaptivePolicy;
    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy mDefaultFullPolicy;
    private java.lang.String mDeviceSpecificSettings;
    private java.lang.String mDeviceSpecificSettingsSource;
    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy mEffectivePolicyRaw;
    private java.lang.String mEventLogKeys;
    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy mFullPolicy;
    private final android.os.Handler mHandler;
    private android.provider.DeviceConfig.Properties mLastDeviceConfigProperties;
    private final java.util.List<com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener> mListeners;
    private final java.lang.Object mLock;
    private final android.app.UiModeManager.OnProjectionStateChangedListener mOnProjectionStateChangedListener;
    private int mPolicyLevel;
    private java.lang.String mSettings;
    static final com.android.server.power.batterysaver.BatterySaverPolicy.Policy OFF_POLICY = new com.android.server.power.batterysaver.BatterySaverPolicy.Policy(1.0f, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, 0, 0);
    private static final com.android.server.power.batterysaver.BatterySaverPolicy.Policy DEFAULT_ADAPTIVE_POLICY = OFF_POLICY;

    public interface BatterySaverPolicyListener {
        void onBatterySaverPolicyChanged(com.android.server.power.batterysaver.BatterySaverPolicy batterySaverPolicy);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface PolicyLevel {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int t, java.util.Set pkgs) {
        this.mAutomotiveProjectionActive.update(!pkgs.isEmpty());
    }

    public BatterySaverPolicy(java.lang.Object lock, android.content.Context context, com.android.server.power.batterysaver.BatterySavingStats batterySavingStats) {
        super(com.android.internal.os.BackgroundThread.getHandler());
        this.mAccessibilityEnabled = new com.android.server.power.batterysaver.BatterySaverPolicy.PolicyBoolean("accessibility");
        this.mAutomotiveProjectionActive = new com.android.server.power.batterysaver.BatterySaverPolicy.PolicyBoolean("automotiveProjection");
        this.mDefaultAdaptivePolicy = DEFAULT_ADAPTIVE_POLICY;
        this.mAdaptivePolicy = DEFAULT_ADAPTIVE_POLICY;
        this.mEffectivePolicyRaw = OFF_POLICY;
        this.mPolicyLevel = 0;
        this.mOnProjectionStateChangedListener = new android.app.UiModeManager.OnProjectionStateChangedListener() { // from class: com.android.server.power.batterysaver.BatterySaverPolicy$$ExternalSyntheticLambda1
            public final void onProjectionStateChanged(int i, java.util.Set set) {
                this.f$0.lambda$new$0(i, set);
            }
        };
        this.mListeners = new java.util.ArrayList();
        this.mLock = lock;
        this.mHandler = com.android.internal.os.BackgroundThread.getHandler();
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mBatterySavingStats = batterySavingStats;
        android.content.res.Resources res = context.getResources();
        this.DEFAULT_FULL_POLICY = new com.android.server.power.batterysaver.BatterySaverPolicy.Policy(res.getFloat(android.R.dimen.chooser_direct_share_label_placeholder_max_width), true, res.getBoolean(android.R.bool.config_batterySaverStickyBehaviourDisabled), res.getBoolean(android.R.bool.config_batterySaverSupported), res.getBoolean(android.R.bool.config_batterySaverTurnedOffNotificationEnabled), res.getBoolean(android.R.bool.config_batterySaver_full_deferFullBackup), res.getBoolean(android.R.bool.config_batterySaver_full_deferKeyValueBackup), res.getBoolean(android.R.bool.config_batterySaver_full_disableAnimation), res.getBoolean(android.R.bool.config_batterySaver_full_disableAod), res.getBoolean(android.R.bool.config_batterySaver_full_disableLaunchBoost), res.getBoolean(android.R.bool.config_batterySaver_full_disableOptionalSensors), res.getBoolean(android.R.bool.config_batterySaver_full_disableVibration), res.getBoolean(android.R.bool.config_batterySaver_full_enableAdjustBrightness), res.getBoolean(android.R.bool.config_batterySaver_full_enableDataSaver), res.getBoolean(android.R.bool.config_batterySaver_full_enableFirewall), res.getBoolean(android.R.bool.config_batterySaver_full_enableNightMode), res.getInteger(android.R.integer.config_autoBrightnessLightSensorRate), res.getInteger(android.R.integer.config_autoBrightnessShortTermModelTimeout));
        this.mDefaultFullPolicy = this.DEFAULT_FULL_POLICY;
        this.mFullPolicy = this.DEFAULT_FULL_POLICY;
        this.mBSPolicyExt = (com.android.server.power.batterysaver.IBatterySaverPolicyExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.batterysaver.IBatterySaverPolicyExt.class).base(this).create();
        this.mBSPolicyExt.init(context);
    }

    public void systemReady() {
        com.android.internal.util.ConcurrentUtils.wtfIfLockHeld(TAG, this.mLock);
        this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("battery_saver_constants"), false, this);
        this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("battery_saver_device_specific_constants"), false, this);
        android.view.accessibility.AccessibilityManager acm = (android.view.accessibility.AccessibilityManager) this.mContext.getSystemService(android.view.accessibility.AccessibilityManager.class);
        acm.addAccessibilityStateChangeListener(new android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener() { // from class: com.android.server.power.batterysaver.BatterySaverPolicy$$ExternalSyntheticLambda2
            @Override // android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener
            public final void onAccessibilityStateChanged(boolean z) {
                this.f$0.lambda$systemReady$1(z);
            }
        });
        this.mAccessibilityEnabled.initialize(acm.isEnabled());
        android.app.UiModeManager uiModeManager = (android.app.UiModeManager) this.mContext.getSystemService(android.app.UiModeManager.class);
        uiModeManager.addOnProjectionStateChangedListener(1, this.mContext.getMainExecutor(), this.mOnProjectionStateChangedListener);
        this.mAutomotiveProjectionActive.initialize(uiModeManager.getActiveProjectionTypes() != 0);
        android.provider.DeviceConfig.addOnPropertiesChangedListener("battery_saver", this.mContext.getMainExecutor(), this);
        this.mLastDeviceConfigProperties = android.provider.DeviceConfig.getProperties("battery_saver", new java.lang.String[0]);
        onChange(true, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$1(boolean enabled) {
        this.mAccessibilityEnabled.update(enabled);
    }

    public void addListener(com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener listener) {
        synchronized (this.mLock) {
            this.mListeners.add(listener);
        }
    }

    java.lang.String getGlobalSetting(java.lang.String key) {
        return android.provider.Settings.Global.getString(this.mContentResolver, key);
    }

    int getDeviceSpecificConfigResId() {
        return android.R.string.config_bodyFontFamilyMedium;
    }

    void invalidatePowerSaveModeCaches() {
        android.os.PowerManager.invalidatePowerSaveModeCaches();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeNotifyListenersOfPolicyChange() {
        synchronized (this.mLock) {
            if (this.mPolicyLevel == 0) {
                return;
            }
            final com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener[] listeners = (com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener[]) this.mListeners.toArray(new com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener[this.mListeners.size()]);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverPolicy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$maybeNotifyListenersOfPolicyChange$2(listeners);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeNotifyListenersOfPolicyChange$2(com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener[] listeners) {
        for (com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener listener : listeners) {
            listener.onBatterySaverPolicyChanged(this);
        }
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange, android.net.Uri uri) {
        refreshSettings();
    }

    public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
        boolean changed;
        this.mLastDeviceConfigProperties = android.provider.DeviceConfig.getProperties("battery_saver", new java.lang.String[0]);
        com.android.server.power.batterysaver.BatterySaverPolicy.Policy newAdaptivePolicy = null;
        com.android.server.power.batterysaver.BatterySaverPolicy.Policy newFullPolicy = null;
        synchronized (this.mLock) {
            for (java.lang.String name : properties.getKeyset()) {
                if (name != null) {
                    if (name.endsWith(KEY_SUFFIX_ADAPTIVE)) {
                        if (newAdaptivePolicy == null) {
                            newAdaptivePolicy = com.android.server.power.batterysaver.BatterySaverPolicy.Policy.fromSettings("", "", this.mLastDeviceConfigProperties, KEY_SUFFIX_ADAPTIVE, DEFAULT_ADAPTIVE_POLICY);
                        }
                    } else if (newFullPolicy == null) {
                        newFullPolicy = com.android.server.power.batterysaver.BatterySaverPolicy.Policy.fromSettings(this.mSettings, this.mDeviceSpecificSettings, this.mLastDeviceConfigProperties, null, this.DEFAULT_FULL_POLICY);
                    }
                }
            }
            changed = newFullPolicy != null ? false | maybeUpdateDefaultFullPolicy(newFullPolicy) : false;
            if (newAdaptivePolicy != null && !this.mAdaptivePolicy.equals(newAdaptivePolicy)) {
                this.mDefaultAdaptivePolicy = newAdaptivePolicy;
                this.mAdaptivePolicy = this.mDefaultAdaptivePolicy;
                changed = (this.mPolicyLevel == 1) | changed;
            }
            updatePolicyDependenciesLocked();
        }
        if (changed) {
            maybeNotifyListenersOfPolicyChange();
        }
    }

    private void refreshSettings() {
        synchronized (this.mLock) {
            java.lang.String setting = getGlobalSetting("battery_saver_constants");
            java.lang.String deviceSpecificSetting = getGlobalSetting("battery_saver_device_specific_constants");
            this.mDeviceSpecificSettingsSource = "battery_saver_device_specific_constants";
            if (android.text.TextUtils.isEmpty(deviceSpecificSetting) || "null".equals(deviceSpecificSetting)) {
                deviceSpecificSetting = this.mContext.getString(getDeviceSpecificConfigResId());
                this.mDeviceSpecificSettingsSource = "(overlay)";
            }
            if (updateConstantsLocked(setting, deviceSpecificSetting)) {
                maybeNotifyListenersOfPolicyChange();
            }
        }
    }

    boolean updateConstantsLocked(java.lang.String setting, java.lang.String deviceSpecificSetting) {
        java.lang.String setting2 = android.text.TextUtils.emptyIfNull(setting);
        java.lang.String deviceSpecificSetting2 = android.text.TextUtils.emptyIfNull(deviceSpecificSetting);
        if (setting2.equals(this.mSettings) && deviceSpecificSetting2.equals(this.mDeviceSpecificSettings)) {
            return false;
        }
        this.mSettings = setting2;
        this.mDeviceSpecificSettings = deviceSpecificSetting2;
        boolean changed = maybeUpdateDefaultFullPolicy(com.android.server.power.batterysaver.BatterySaverPolicy.Policy.fromSettings(setting2, deviceSpecificSetting2, this.mLastDeviceConfigProperties, null, this.DEFAULT_FULL_POLICY));
        this.mDefaultAdaptivePolicy = com.android.server.power.batterysaver.BatterySaverPolicy.Policy.fromSettings("", "", this.mLastDeviceConfigProperties, KEY_SUFFIX_ADAPTIVE, DEFAULT_ADAPTIVE_POLICY);
        if (this.mPolicyLevel == 1 && !this.mAdaptivePolicy.equals(this.mDefaultAdaptivePolicy)) {
            changed = true;
        }
        this.mAdaptivePolicy = this.mDefaultAdaptivePolicy;
        updatePolicyDependenciesLocked();
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePolicyDependenciesLocked() {
        int locationMode;
        com.android.server.power.batterysaver.BatterySaverPolicy.Policy rawPolicy = getCurrentRawPolicyLocked();
        invalidatePowerSaveModeCaches();
        if (this.mAutomotiveProjectionActive.get() && rawPolicy.locationMode != 0 && rawPolicy.locationMode != 3) {
            locationMode = 3;
        } else {
            locationMode = rawPolicy.locationMode;
        }
        int locationMode2 = locationMode;
        this.mEffectivePolicyRaw = new com.android.server.power.batterysaver.BatterySaverPolicy.Policy(rawPolicy.adjustBrightnessFactor, rawPolicy.advertiseIsEnabled, rawPolicy.deferFullBackup, rawPolicy.deferKeyValueBackup, rawPolicy.disableAnimation, rawPolicy.disableAod, rawPolicy.disableLaunchBoost, rawPolicy.disableOptionalSensors, rawPolicy.disableVibration && !this.mAccessibilityEnabled.get(), rawPolicy.enableAdjustBrightness, rawPolicy.enableDataSaver, rawPolicy.enableFirewall, rawPolicy.enableNightMode && !this.mAutomotiveProjectionActive.get(), rawPolicy.enableQuickDoze, rawPolicy.forceAllAppsStandby, rawPolicy.forceBackgroundCheck, locationMode2, rawPolicy.soundTriggerMode);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (this.mEffectivePolicyRaw.forceAllAppsStandby) {
            sb.append("A");
        }
        if (this.mEffectivePolicyRaw.forceBackgroundCheck) {
            sb.append("B");
        }
        if (this.mEffectivePolicyRaw.disableVibration) {
            sb.append("v");
        }
        if (this.mEffectivePolicyRaw.disableAnimation) {
            sb.append(com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        }
        sb.append(this.mEffectivePolicyRaw.soundTriggerMode);
        if (this.mEffectivePolicyRaw.deferFullBackup) {
            sb.append("F");
        }
        if (this.mEffectivePolicyRaw.deferKeyValueBackup) {
            sb.append("K");
        }
        if (this.mEffectivePolicyRaw.enableFirewall) {
            sb.append("f");
        }
        if (this.mEffectivePolicyRaw.enableDataSaver) {
            sb.append("d");
        }
        if (this.mEffectivePolicyRaw.enableAdjustBrightness) {
            sb.append("b");
        }
        if (this.mEffectivePolicyRaw.disableLaunchBoost) {
            sb.append("l");
        }
        if (this.mEffectivePolicyRaw.disableOptionalSensors) {
            sb.append("S");
        }
        if (this.mEffectivePolicyRaw.disableAod) {
            sb.append("o");
        }
        if (this.mEffectivePolicyRaw.enableQuickDoze) {
            sb.append("q");
        }
        sb.append(this.mEffectivePolicyRaw.locationMode);
        this.mEventLogKeys = sb.toString();
    }

    static class Policy {
        public final float adjustBrightnessFactor;
        public boolean advertiseIsEnabled;
        public final boolean deferFullBackup;
        public final boolean deferKeyValueBackup;
        public final boolean disableAnimation;
        public boolean disableAod;
        public boolean disableLaunchBoost;
        public boolean disableOptionalSensors;
        public boolean disableVibration;
        public boolean enableAdjustBrightness;
        public final boolean enableDataSaver;
        public boolean enableFirewall;
        public boolean enableNightMode;
        public final boolean enableQuickDoze;
        public final boolean forceAllAppsStandby;
        public final boolean forceBackgroundCheck;
        public int locationMode;
        private final int mHashCode;
        public int soundTriggerMode;

        Policy(float adjustBrightnessFactor, boolean advertiseIsEnabled, boolean deferFullBackup, boolean deferKeyValueBackup, boolean disableAnimation, boolean disableAod, boolean disableLaunchBoost, boolean disableOptionalSensors, boolean disableVibration, boolean enableAdjustBrightness, boolean enableDataSaver, boolean enableFirewall, boolean enableNightMode, boolean enableQuickDoze, boolean forceAllAppsStandby, boolean forceBackgroundCheck, int locationMode, int soundTriggerMode) {
            this.adjustBrightnessFactor = java.lang.Math.min(1.0f, java.lang.Math.max(0.0f, adjustBrightnessFactor));
            this.advertiseIsEnabled = advertiseIsEnabled;
            this.deferFullBackup = deferFullBackup;
            this.deferKeyValueBackup = deferKeyValueBackup;
            this.disableAnimation = disableAnimation;
            this.disableAod = disableAod;
            this.disableLaunchBoost = disableLaunchBoost;
            this.disableOptionalSensors = disableOptionalSensors;
            this.disableVibration = disableVibration;
            this.enableAdjustBrightness = enableAdjustBrightness;
            this.enableDataSaver = enableDataSaver;
            this.enableFirewall = enableFirewall;
            this.enableNightMode = enableNightMode;
            this.enableQuickDoze = enableQuickDoze;
            this.forceAllAppsStandby = forceAllAppsStandby;
            this.forceBackgroundCheck = forceBackgroundCheck;
            if (locationMode < 0 || 4 < locationMode) {
                android.util.Slog.e(com.android.server.power.batterysaver.BatterySaverPolicy.TAG, "Invalid location mode: " + locationMode);
                this.locationMode = 0;
            } else {
                this.locationMode = locationMode;
            }
            if (soundTriggerMode < 0 || soundTriggerMode > 2) {
                android.util.Slog.e(com.android.server.power.batterysaver.BatterySaverPolicy.TAG, "Invalid SoundTrigger mode: " + soundTriggerMode);
                this.soundTriggerMode = 0;
            } else {
                this.soundTriggerMode = soundTriggerMode;
            }
            this.mHashCode = java.util.Objects.hash(java.lang.Float.valueOf(adjustBrightnessFactor), java.lang.Boolean.valueOf(advertiseIsEnabled), java.lang.Boolean.valueOf(deferFullBackup), java.lang.Boolean.valueOf(deferKeyValueBackup), java.lang.Boolean.valueOf(disableAnimation), java.lang.Boolean.valueOf(disableAod), java.lang.Boolean.valueOf(disableLaunchBoost), java.lang.Boolean.valueOf(disableOptionalSensors), java.lang.Boolean.valueOf(disableVibration), java.lang.Boolean.valueOf(enableAdjustBrightness), java.lang.Boolean.valueOf(enableDataSaver), java.lang.Boolean.valueOf(enableFirewall), java.lang.Boolean.valueOf(enableNightMode), java.lang.Boolean.valueOf(enableQuickDoze), java.lang.Boolean.valueOf(forceAllAppsStandby), java.lang.Boolean.valueOf(forceBackgroundCheck), java.lang.Integer.valueOf(locationMode), java.lang.Integer.valueOf(soundTriggerMode));
        }

        static com.android.server.power.batterysaver.BatterySaverPolicy.Policy fromConfig(android.os.BatterySaverPolicyConfig config) {
            if (config == null) {
                android.util.Slog.e(com.android.server.power.batterysaver.BatterySaverPolicy.TAG, "Null config passed down to BatterySaverPolicy");
                return com.android.server.power.batterysaver.BatterySaverPolicy.OFF_POLICY;
            }
            config.getDeviceSpecificSettings();
            return new com.android.server.power.batterysaver.BatterySaverPolicy.Policy(config.getAdjustBrightnessFactor(), config.getAdvertiseIsEnabled(), config.getDeferFullBackup(), config.getDeferKeyValueBackup(), config.getDisableAnimation(), config.getDisableAod(), config.getDisableLaunchBoost(), config.getDisableOptionalSensors(), config.getDisableVibration(), config.getEnableAdjustBrightness(), config.getEnableDataSaver(), config.getEnableFirewall(), config.getEnableNightMode(), config.getEnableQuickDoze(), config.getForceAllAppsStandby(), config.getForceBackgroundCheck(), config.getLocationMode(), config.getSoundTriggerMode());
        }

        android.os.BatterySaverPolicyConfig toConfig() {
            return new android.os.BatterySaverPolicyConfig.Builder().setAdjustBrightnessFactor(this.adjustBrightnessFactor).setAdvertiseIsEnabled(this.advertiseIsEnabled).setDeferFullBackup(this.deferFullBackup).setDeferKeyValueBackup(this.deferKeyValueBackup).setDisableAnimation(this.disableAnimation).setDisableAod(this.disableAod).setDisableLaunchBoost(this.disableLaunchBoost).setDisableOptionalSensors(this.disableOptionalSensors).setDisableVibration(this.disableVibration).setEnableAdjustBrightness(this.enableAdjustBrightness).setEnableDataSaver(this.enableDataSaver).setEnableFirewall(this.enableFirewall).setEnableNightMode(this.enableNightMode).setEnableQuickDoze(this.enableQuickDoze).setForceAllAppsStandby(this.forceAllAppsStandby).setForceBackgroundCheck(this.forceBackgroundCheck).setLocationMode(this.locationMode).setSoundTriggerMode(this.soundTriggerMode).build();
        }

        static com.android.server.power.batterysaver.BatterySaverPolicy.Policy fromSettings(java.lang.String settings, java.lang.String deviceSpecificSettings, android.provider.DeviceConfig.Properties properties, java.lang.String configSuffix) {
            return fromSettings(settings, deviceSpecificSettings, properties, configSuffix, com.android.server.power.batterysaver.BatterySaverPolicy.OFF_POLICY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.power.batterysaver.BatterySaverPolicy.Policy fromSettings(java.lang.String settings, java.lang.String deviceSpecificSettings, android.provider.DeviceConfig.Properties properties, java.lang.String configSuffix, com.android.server.power.batterysaver.BatterySaverPolicy.Policy defaultPolicy) {
            com.android.server.utils.UserSettingDeviceConfigMediator userSettingDeviceConfigMediator = new com.android.server.utils.UserSettingDeviceConfigMediator.SettingsOverridesIndividualMediator(',');
            java.lang.String configSuffix2 = android.text.TextUtils.emptyIfNull(configSuffix);
            try {
                userSettingDeviceConfigMediator.setSettingsString(deviceSpecificSettings);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.wtf(com.android.server.power.batterysaver.BatterySaverPolicy.TAG, "Bad device specific battery saver constants: " + deviceSpecificSettings);
            }
            try {
                userSettingDeviceConfigMediator.setSettingsString(settings);
                try {
                    userSettingDeviceConfigMediator.setDeviceConfigProperties(properties);
                } catch (java.lang.IllegalArgumentException e2) {
                    android.util.Slog.wtf(com.android.server.power.batterysaver.BatterySaverPolicy.TAG, "Bad battery saver constants: " + settings);
                }
            } catch (java.lang.IllegalArgumentException e3) {
            }
            float adjustBrightnessFactor = userSettingDeviceConfigMediator.getFloat(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ADJUST_BRIGHTNESS_FACTOR + configSuffix2, defaultPolicy.adjustBrightnessFactor);
            boolean advertiseIsEnabled = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ADVERTISE_IS_ENABLED + configSuffix2, defaultPolicy.advertiseIsEnabled);
            boolean deferFullBackup = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DEFER_FULL_BACKUP + configSuffix2, defaultPolicy.deferFullBackup);
            boolean deferKeyValueBackup = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DEFER_KEYVALUE_BACKUP + configSuffix2, defaultPolicy.deferKeyValueBackup);
            boolean disableAnimation = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DISABLE_ANIMATION + configSuffix2, defaultPolicy.disableAnimation);
            boolean disableAod = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DISABLE_AOD + configSuffix2, defaultPolicy.disableAod);
            boolean disableLaunchBoost = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DISABLE_LAUNCH_BOOST + configSuffix2, defaultPolicy.disableLaunchBoost);
            boolean disableOptionalSensors = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DISABLE_OPTIONAL_SENSORS + configSuffix2, defaultPolicy.disableOptionalSensors);
            boolean disableVibrationConfig = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_DISABLE_VIBRATION + configSuffix2, defaultPolicy.disableVibration);
            boolean enableBrightnessAdjustment = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ENABLE_BRIGHTNESS_ADJUSTMENT + configSuffix2, defaultPolicy.enableAdjustBrightness);
            boolean enableDataSaver = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ENABLE_DATASAVER + configSuffix2, defaultPolicy.enableDataSaver);
            boolean enableFirewall = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ENABLE_FIREWALL + configSuffix2, defaultPolicy.enableFirewall);
            boolean enableNightMode = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ENABLE_NIGHT_MODE + configSuffix2, defaultPolicy.enableNightMode);
            boolean enableQuickDoze = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_ENABLE_QUICK_DOZE + configSuffix2, defaultPolicy.enableQuickDoze);
            boolean forceAllAppsStandby = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_FORCE_ALL_APPS_STANDBY + configSuffix2, defaultPolicy.forceAllAppsStandby);
            boolean forceBackgroundCheck = userSettingDeviceConfigMediator.getBoolean(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_FORCE_BACKGROUND_CHECK + configSuffix2, defaultPolicy.forceBackgroundCheck);
            int locationMode = userSettingDeviceConfigMediator.getInt(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_LOCATION_MODE + configSuffix2, defaultPolicy.locationMode);
            int soundTriggerMode = userSettingDeviceConfigMediator.getInt(com.android.server.power.batterysaver.BatterySaverPolicy.KEY_SOUNDTRIGGER_MODE + configSuffix2, defaultPolicy.soundTriggerMode);
            return new com.android.server.power.batterysaver.BatterySaverPolicy.Policy(adjustBrightnessFactor, advertiseIsEnabled, deferFullBackup, deferKeyValueBackup, disableAnimation, disableAod, disableLaunchBoost, disableOptionalSensors, disableVibrationConfig, enableBrightnessAdjustment, enableDataSaver, enableFirewall, enableNightMode, enableQuickDoze, forceAllAppsStandby, forceBackgroundCheck, locationMode, soundTriggerMode);
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof com.android.server.power.batterysaver.BatterySaverPolicy.Policy)) {
                return false;
            }
            com.android.server.power.batterysaver.BatterySaverPolicy.Policy other = (com.android.server.power.batterysaver.BatterySaverPolicy.Policy) obj;
            return java.lang.Float.compare(other.adjustBrightnessFactor, this.adjustBrightnessFactor) == 0 && this.advertiseIsEnabled == other.advertiseIsEnabled && this.deferFullBackup == other.deferFullBackup && this.deferKeyValueBackup == other.deferKeyValueBackup && this.disableAnimation == other.disableAnimation && this.disableAod == other.disableAod && this.disableLaunchBoost == other.disableLaunchBoost && this.disableOptionalSensors == other.disableOptionalSensors && this.disableVibration == other.disableVibration && this.enableAdjustBrightness == other.enableAdjustBrightness && this.enableDataSaver == other.enableDataSaver && this.enableFirewall == other.enableFirewall && this.enableNightMode == other.enableNightMode && this.enableQuickDoze == other.enableQuickDoze && this.forceAllAppsStandby == other.forceAllAppsStandby && this.forceBackgroundCheck == other.forceBackgroundCheck && this.locationMode == other.locationMode && this.soundTriggerMode == other.soundTriggerMode;
        }

        public int hashCode() {
            return this.mHashCode;
        }
    }

    public android.os.PowerSaveState getBatterySaverPolicy(int type) {
        boolean soundTriggerBatterySaverEnabled;
        synchronized (this.mLock) {
            com.android.server.power.batterysaver.BatterySaverPolicy.Policy currPolicy = getCurrentPolicyLocked();
            android.os.PowerSaveState.Builder builder = new android.os.PowerSaveState.Builder().setGlobalBatterySaverEnabled(currPolicy.advertiseIsEnabled);
            this.mBSPolicyExt.onGetBatterySaverPolicy(type, this.mPolicyLevel, currPolicy, this.mAutomotiveProjectionActive.get());
            switch (type) {
                case 1:
                    soundTriggerBatterySaverEnabled = currPolicy.advertiseIsEnabled || currPolicy.locationMode != 0;
                    return builder.setBatterySaverEnabled(soundTriggerBatterySaverEnabled).setLocationMode(currPolicy.locationMode).build();
                case 2:
                    return builder.setBatterySaverEnabled(currPolicy.disableVibration).build();
                case 3:
                    return builder.setBatterySaverEnabled(currPolicy.disableAnimation).build();
                case 4:
                    return builder.setBatterySaverEnabled(currPolicy.deferFullBackup).build();
                case 5:
                    return builder.setBatterySaverEnabled(currPolicy.deferKeyValueBackup).build();
                case 6:
                    return builder.setBatterySaverEnabled(currPolicy.enableFirewall).build();
                case 7:
                    return builder.setBatterySaverEnabled(currPolicy.enableAdjustBrightness).setBrightnessFactor(currPolicy.adjustBrightnessFactor).build();
                case 8:
                    soundTriggerBatterySaverEnabled = currPolicy.advertiseIsEnabled || currPolicy.soundTriggerMode != 0;
                    return builder.setBatterySaverEnabled(soundTriggerBatterySaverEnabled).setSoundTriggerMode(currPolicy.soundTriggerMode).build();
                case 9:
                default:
                    boolean isEnabled = currPolicy.advertiseIsEnabled;
                    return builder.setBatterySaverEnabled(isEnabled).build();
                case 10:
                    return builder.setBatterySaverEnabled(currPolicy.enableDataSaver).build();
                case 11:
                    return builder.setBatterySaverEnabled(currPolicy.forceAllAppsStandby).build();
                case 12:
                    return builder.setBatterySaverEnabled(currPolicy.forceBackgroundCheck).build();
                case 13:
                    return builder.setBatterySaverEnabled(currPolicy.disableOptionalSensors).build();
                case 14:
                    return builder.setBatterySaverEnabled(currPolicy.disableAod).build();
                case 15:
                    return builder.setBatterySaverEnabled(currPolicy.enableQuickDoze).build();
                case 16:
                    return builder.setBatterySaverEnabled(currPolicy.enableNightMode).build();
            }
        }
    }

    boolean setPolicyLevel(int level) {
        synchronized (this.mLock) {
            if (this.mPolicyLevel == level) {
                return false;
            }
            if (this.mPolicyLevel == 2) {
                this.mFullPolicy = this.mDefaultFullPolicy;
            }
            switch (level) {
                case 0:
                case 1:
                case 2:
                    this.mPolicyLevel = level;
                    updatePolicyDependenciesLocked();
                    return true;
                default:
                    android.util.Slog.wtf(TAG, "setPolicyLevel invalid level given: " + level);
                    return false;
            }
        }
    }

    com.android.server.power.batterysaver.BatterySaverPolicy.Policy getPolicyLocked(int policyLevel) {
        switch (policyLevel) {
            case 0:
                return OFF_POLICY;
            case 1:
                return this.mAdaptivePolicy;
            case 2:
                return this.mFullPolicy;
            default:
                throw new java.lang.IllegalArgumentException("getPolicyLocked: incorrect policy level provided - " + policyLevel);
        }
    }

    private boolean maybeUpdateDefaultFullPolicy(com.android.server.power.batterysaver.BatterySaverPolicy.Policy p) {
        boolean fullPolicyChanged = false;
        if (!this.mDefaultFullPolicy.equals(p)) {
            boolean isDefaultFullPolicyOverridden = !this.mDefaultFullPolicy.equals(this.mFullPolicy);
            if (!isDefaultFullPolicyOverridden) {
                this.mFullPolicy = p;
                fullPolicyChanged = this.mPolicyLevel == 2;
            }
            this.mDefaultFullPolicy = p;
        }
        return fullPolicyChanged;
    }

    boolean setFullPolicyLocked(com.android.server.power.batterysaver.BatterySaverPolicy.Policy p) {
        if (p == null) {
            android.util.Slog.wtf(TAG, "setFullPolicy given null policy");
            return false;
        }
        if (this.mFullPolicy.equals(p)) {
            return false;
        }
        this.mFullPolicy = p;
        if (this.mPolicyLevel != 2) {
            return false;
        }
        updatePolicyDependenciesLocked();
        return true;
    }

    boolean setAdaptivePolicyLocked(com.android.server.power.batterysaver.BatterySaverPolicy.Policy p) {
        if (p == null) {
            android.util.Slog.wtf(TAG, "setAdaptivePolicy given null policy");
            return false;
        }
        if (this.mAdaptivePolicy.equals(p)) {
            return false;
        }
        this.mAdaptivePolicy = p;
        if (this.mPolicyLevel != 1) {
            return false;
        }
        updatePolicyDependenciesLocked();
        return true;
    }

    boolean resetAdaptivePolicyLocked() {
        return setAdaptivePolicyLocked(this.mDefaultAdaptivePolicy);
    }

    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy getCurrentPolicyLocked() {
        return this.mEffectivePolicyRaw;
    }

    private com.android.server.power.batterysaver.BatterySaverPolicy.Policy getCurrentRawPolicyLocked() {
        switch (this.mPolicyLevel) {
            case 1:
                return this.mAdaptivePolicy;
            case 2:
                return this.mFullPolicy;
            default:
                return OFF_POLICY;
        }
    }

    public int getGpsMode() {
        int i;
        synchronized (this.mLock) {
            this.mBSPolicyExt.onGetGpsMode(this.mPolicyLevel, getCurrentPolicyLocked(), this.mAutomotiveProjectionActive.get());
            i = getCurrentPolicyLocked().locationMode;
        }
        return i;
    }

    public boolean isLaunchBoostDisabled() {
        boolean z;
        synchronized (this.mLock) {
            this.mBSPolicyExt.onIsLaunchBoostDisabled(this.mPolicyLevel, getCurrentPolicyLocked());
            z = getCurrentPolicyLocked().disableLaunchBoost;
        }
        return z;
    }

    boolean shouldAdvertiseIsEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = getCurrentPolicyLocked().advertiseIsEnabled;
        }
        return z;
    }

    public java.lang.String toEventLogString() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mEventLogKeys;
        }
        return str;
    }

    public void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        synchronized (this.mLock) {
            ipw.println();
            this.mBatterySavingStats.dump(ipw);
            ipw.println();
            ipw.println("Battery saver policy (*NOTE* they only apply when battery saver is ON):");
            ipw.increaseIndent();
            ipw.println("Settings: battery_saver_constants");
            ipw.increaseIndent();
            ipw.println("value: " + this.mSettings);
            ipw.decreaseIndent();
            ipw.println("Settings: " + this.mDeviceSpecificSettingsSource);
            ipw.increaseIndent();
            ipw.println("value: " + this.mDeviceSpecificSettings);
            ipw.decreaseIndent();
            ipw.println("DeviceConfig: battery_saver");
            ipw.increaseIndent();
            java.util.Set<java.lang.String> keys = this.mLastDeviceConfigProperties.getKeyset();
            if (keys.size() == 0) {
                ipw.println("N/A");
            } else {
                for (java.lang.String key : keys) {
                    ipw.print(key);
                    ipw.print(": ");
                    ipw.println(this.mLastDeviceConfigProperties.getString(key, (java.lang.String) null));
                }
            }
            ipw.decreaseIndent();
            ipw.println("mAccessibilityEnabled=" + this.mAccessibilityEnabled.get());
            ipw.println("mAutomotiveProjectionActive=" + this.mAutomotiveProjectionActive.get());
            ipw.println("mPolicyLevel=" + this.mPolicyLevel);
            dumpPolicyLocked(ipw, "default full", this.mDefaultFullPolicy);
            dumpPolicyLocked(ipw, "current full", this.mFullPolicy);
            dumpPolicyLocked(ipw, "default adaptive", this.mDefaultAdaptivePolicy);
            dumpPolicyLocked(ipw, "current adaptive", this.mAdaptivePolicy);
            dumpPolicyLocked(ipw, "effective", this.mEffectivePolicyRaw);
            ipw.decreaseIndent();
        }
    }

    private void dumpPolicyLocked(android.util.IndentingPrintWriter pw, java.lang.String label, com.android.server.power.batterysaver.BatterySaverPolicy.Policy p) {
        pw.println();
        pw.println("Policy '" + label + "'");
        pw.increaseIndent();
        pw.println("advertise_is_enabled=" + p.advertiseIsEnabled);
        pw.println("disable_vibration=" + p.disableVibration);
        pw.println("disable_animation=" + p.disableAnimation);
        pw.println("defer_full_backup=" + p.deferFullBackup);
        pw.println("defer_keyvalue_backup=" + p.deferKeyValueBackup);
        pw.println("enable_firewall=" + p.enableFirewall);
        pw.println("enable_datasaver=" + p.enableDataSaver);
        pw.println("disable_launch_boost=" + p.disableLaunchBoost);
        pw.println("enable_brightness_adjustment=" + p.enableAdjustBrightness);
        pw.println("adjust_brightness_factor=" + p.adjustBrightnessFactor);
        pw.println("location_mode=" + p.locationMode);
        pw.println("force_all_apps_standby=" + p.forceAllAppsStandby);
        pw.println("force_background_check=" + p.forceBackgroundCheck);
        pw.println("disable_optional_sensors=" + p.disableOptionalSensors);
        pw.println("disable_aod=" + p.disableAod);
        pw.println("soundtrigger_mode=" + p.soundTriggerMode);
        pw.println("enable_quick_doze=" + p.enableQuickDoze);
        pw.println("enable_night_mode=" + p.enableNightMode);
        pw.decreaseIndent();
    }

    private void dumpMap(java.io.PrintWriter pw, android.util.ArrayMap<java.lang.String, java.lang.String> map) {
        if (map == null || map.size() == 0) {
            pw.println("N/A");
            return;
        }
        int size = map.size();
        for (int i = 0; i < size; i++) {
            pw.print(map.keyAt(i));
            pw.print(": '");
            pw.print(map.valueAt(i));
            pw.println("'");
        }
    }

    class PolicyBoolean {
        private final java.lang.String mDebugName;
        private boolean mValue;

        private PolicyBoolean(java.lang.String debugName) {
            this.mDebugName = debugName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initialize(boolean initialValue) {
            synchronized (com.android.server.power.batterysaver.BatterySaverPolicy.this.mLock) {
                this.mValue = initialValue;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean get() {
            boolean z;
            synchronized (com.android.server.power.batterysaver.BatterySaverPolicy.this.mLock) {
                z = this.mValue;
            }
            return z;
        }

        void update(boolean newValue) {
            synchronized (com.android.server.power.batterysaver.BatterySaverPolicy.this.mLock) {
                if (this.mValue != newValue) {
                    android.util.Slog.d(com.android.server.power.batterysaver.BatterySaverPolicy.TAG, this.mDebugName + " changed to " + newValue + ", updating policy.");
                    this.mValue = newValue;
                    com.android.server.power.batterysaver.BatterySaverPolicy.this.updatePolicyDependenciesLocked();
                    com.android.server.power.batterysaver.BatterySaverPolicy.this.maybeNotifyListenersOfPolicyChange();
                }
            }
        }
    }
}
