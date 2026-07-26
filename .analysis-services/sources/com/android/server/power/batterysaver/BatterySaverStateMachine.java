package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public class BatterySaverStateMachine {
    private static final int ADAPTIVE_AUTO_DISABLE_BATTERY_LEVEL = 80;
    private static final long ADAPTIVE_CHANGE_TIMEOUT_MS = 86400000;
    private static final java.lang.String BATTERY_SAVER_NOTIF_CHANNEL_ID = "battery_saver_channel";
    private static final boolean DEBUG = false;
    private static final int DYNAMIC_MODE_NOTIFICATION_ID = 1992;
    private static final java.lang.String DYNAMIC_MODE_NOTIF_CHANNEL_ID = "dynamic_mode_notification";
    private static final java.lang.String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
    private static final java.lang.String EXTRA_SHOW_FRAGMENT_TITLE = ":settings:show_fragment_args";
    private static final java.lang.String PREFERENCE_KEY_BATTERY_SAVER_SCHEDULER = "battery_saver_schedule";
    private static final int STATE_AUTOMATIC_ON = 3;
    private static final int STATE_MANUAL_ON = 2;
    private static final int STATE_OFF = 1;
    private static final int STATE_OFF_AUTOMATIC_SNOOZED = 4;
    private static final int STATE_PENDING_STICKY_ON = 5;
    private static final int STICKY_AUTO_DISABLED_NOTIFICATION_ID = 1993;
    private static final long STICKY_DISABLED_NOTIFY_TIMEOUT_MS = java.time.Duration.ofHours(12).toMillis();
    private static final java.lang.String TAG = "BatterySaverStateMachine";
    private int mBatteryLevel;
    private final com.android.server.power.batterysaver.BatterySaverController mBatterySaverController;
    private final boolean mBatterySaverStickyBehaviourDisabled;
    private final boolean mBatterySaverTurnedOffNotificationEnabled;
    private boolean mBatteryStatusSet;
    private boolean mBootCompleted;
    private final android.content.Context mContext;
    private final int mDynamicPowerSavingsDefaultDisableThreshold;
    private int mDynamicPowerSavingsDisableThreshold;
    private boolean mDynamicPowerSavingsEnableBatterySaver;
    private boolean mIsBatteryLevelLow;
    private boolean mIsPowered;
    private long mLastAdaptiveBatterySaverChangedExternallyElapsed;
    private int mLastChangedIntReason;
    private java.lang.String mLastChangedStrReason;
    private final java.lang.Object mLock;
    private int mSettingAutomaticBatterySaver;
    private boolean mSettingBatterySaverEnabled;
    private boolean mSettingBatterySaverEnabledSticky;
    private boolean mSettingBatterySaverStickyAutoDisableEnabled;
    private int mSettingBatterySaverStickyAutoDisableThreshold;
    private int mSettingBatterySaverTriggerThreshold;
    private boolean mSettingsLoaded;
    private final android.database.ContentObserver mSettingsObserver = new android.database.ContentObserver(null) { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            synchronized (com.android.server.power.batterysaver.BatterySaverStateMachine.this.mLock) {
                com.android.server.power.batterysaver.BatterySaverStateMachine.this.refreshSettingsLocked();
            }
        }
    };
    private final java.lang.Runnable mThresholdChangeLogger = new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$1();
        }
    };
    private com.android.server.power.batterysaver.BatterySaverStateMachine.BatterySaverStateMachineWrapper mBSSMWrapper = new com.android.server.power.batterysaver.BatterySaverStateMachine.BatterySaverStateMachineWrapper();
    private com.android.server.power.batterysaver.IBatterySaverStateMachineExt mBSSMExt = (com.android.server.power.batterysaver.IBatterySaverStateMachineExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.batterysaver.IBatterySaverStateMachineExt.class).base(this).create();
    private int mState = 1;

    /* JADX WARN: Multi-variable type inference failed */
    public BatterySaverStateMachine(java.lang.Object obj, android.content.Context context, com.android.server.power.batterysaver.BatterySaverController batterySaverController) {
        this.mLock = obj;
        this.mContext = context;
        this.mBatterySaverController = batterySaverController;
        this.mBatterySaverStickyBehaviourDisabled = this.mContext.getResources().getBoolean(android.R.bool.config_automatic_brightness_available);
        this.mBatterySaverTurnedOffNotificationEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_awareSettingAvailable);
        this.mDynamicPowerSavingsDefaultDisableThreshold = this.mContext.getResources().getInteger(android.R.integer.config_doublelineClockDefault);
        this.mBSSMExt.init(context);
    }

    public void systemReady() {
        this.mBatterySaverController.systemReady();
        getBatterySaverPolicy().systemReady();
    }

    public com.android.server.power.batterysaver.BatterySaverController getBatterySaverController() {
        return this.mBatterySaverController;
    }

    public com.android.server.power.batterysaver.BatterySaverPolicy getBatterySaverPolicy() {
        return this.mBatterySaverController.getBatterySaverPolicy();
    }

    private boolean isAutomaticModeActiveLocked() {
        return this.mSettingAutomaticBatterySaver == 0 && this.mSettingBatterySaverTriggerThreshold > 0;
    }

    private boolean isInAutomaticLowZoneLocked() {
        return this.mIsBatteryLevelLow;
    }

    private boolean isDynamicModeActiveLocked() {
        return this.mSettingAutomaticBatterySaver == 1 && this.mDynamicPowerSavingsEnableBatterySaver;
    }

    private boolean isInDynamicLowZoneLocked() {
        return this.mBatteryLevel <= this.mDynamicPowerSavingsDisableThreshold;
    }

    public void onBootCompleted() {
        putGlobalSetting("low_power", 0);
        runOnBgThread(new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBootCompleted$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootCompleted$0() {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("low_power"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_sticky"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_trigger_level"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("automatic_power_save_mode"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("dynamic_power_savings_enabled"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("dynamic_power_savings_disable_threshold"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_sticky_auto_disable_enabled"), false, this.mSettingsObserver, 0);
        cr.registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_sticky_auto_disable_level"), false, this.mSettingsObserver, 0);
        synchronized (this.mLock) {
            boolean lowPowerModeEnabledSticky = getGlobalSetting("low_power_sticky", 0) != 0;
            if (lowPowerModeEnabledSticky) {
                this.mState = 5;
            }
            this.mBootCompleted = true;
            refreshSettingsLocked();
            doAutoBatterySaverLocked();
            this.mBSSMExt.onBootCompleted(this.mSettingBatterySaverEnabledSticky);
        }
    }

    void runOnBgThread(java.lang.Runnable r) {
        com.android.internal.os.BackgroundThread.getHandler().post(r);
    }

    void runOnBgThreadLazy(java.lang.Runnable r, int delayMillis) {
        android.os.Handler h = com.android.internal.os.BackgroundThread.getHandler();
        h.removeCallbacks(r);
        h.postDelayed(r, delayMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSettingsLocked() {
        boolean lowPowerModeEnabled = getGlobalSetting("low_power", 0) != 0;
        boolean lowPowerModeEnabledSticky = getGlobalSetting("low_power_sticky", 0) != 0;
        boolean dynamicPowerSavingsBatterySaver = getGlobalSetting("dynamic_power_savings_enabled", 0) != 0;
        int lowPowerModeTriggerLevel = getGlobalSetting("low_power_trigger_level", 0);
        int automaticBatterySaverMode = getGlobalSetting("automatic_power_save_mode", 0);
        int dynamicPowerSavingsDisableThreshold = getGlobalSetting("dynamic_power_savings_disable_threshold", this.mDynamicPowerSavingsDefaultDisableThreshold);
        boolean isStickyAutoDisableEnabled = getGlobalSetting("low_power_sticky_auto_disable_enabled", 1) != 0;
        int stickyAutoDisableThreshold = getGlobalSetting("low_power_sticky_auto_disable_level", 90);
        setSettingsLocked(lowPowerModeEnabled, lowPowerModeEnabledSticky, lowPowerModeTriggerLevel, isStickyAutoDisableEnabled, stickyAutoDisableThreshold, automaticBatterySaverMode, dynamicPowerSavingsBatterySaver, dynamicPowerSavingsDisableThreshold);
    }

    void setSettingsLocked(boolean batterySaverEnabled, boolean batterySaverEnabledSticky, int batterySaverTriggerThreshold, boolean isStickyAutoDisableEnabled, int stickyAutoDisableThreshold, int automaticBatterySaver, boolean dynamicPowerSavingsBatterySaver, int dynamicPowerSavingsDisableThreshold) {
        this.mSettingsLoaded = true;
        int stickyAutoDisableThreshold2 = java.lang.Math.max(stickyAutoDisableThreshold, batterySaverTriggerThreshold);
        boolean enabledChanged = this.mSettingBatterySaverEnabled != batterySaverEnabled;
        boolean stickyChanged = this.mSettingBatterySaverEnabledSticky != batterySaverEnabledSticky;
        boolean thresholdChanged = this.mSettingBatterySaverTriggerThreshold != batterySaverTriggerThreshold;
        boolean stickyAutoDisableEnabledChanged = this.mSettingBatterySaverStickyAutoDisableEnabled != isStickyAutoDisableEnabled;
        boolean stickyAutoDisableThresholdChanged = this.mSettingBatterySaverStickyAutoDisableThreshold != stickyAutoDisableThreshold2;
        boolean automaticModeChanged = this.mSettingAutomaticBatterySaver != automaticBatterySaver;
        boolean dynamicPowerSavingsThresholdChanged = this.mDynamicPowerSavingsDisableThreshold != dynamicPowerSavingsDisableThreshold;
        boolean dynamicPowerSavingsBatterySaverChanged = this.mDynamicPowerSavingsEnableBatterySaver != dynamicPowerSavingsBatterySaver;
        if (!enabledChanged && !stickyChanged && !thresholdChanged && !automaticModeChanged && !stickyAutoDisableEnabledChanged && !stickyAutoDisableThresholdChanged && !dynamicPowerSavingsThresholdChanged && !dynamicPowerSavingsBatterySaverChanged) {
            return;
        }
        this.mSettingBatterySaverEnabled = batterySaverEnabled;
        this.mSettingBatterySaverEnabledSticky = batterySaverEnabledSticky;
        this.mSettingBatterySaverTriggerThreshold = batterySaverTriggerThreshold;
        this.mSettingBatterySaverStickyAutoDisableEnabled = isStickyAutoDisableEnabled;
        this.mSettingBatterySaverStickyAutoDisableThreshold = stickyAutoDisableThreshold2;
        this.mSettingAutomaticBatterySaver = automaticBatterySaver;
        this.mDynamicPowerSavingsDisableThreshold = dynamicPowerSavingsDisableThreshold;
        this.mDynamicPowerSavingsEnableBatterySaver = dynamicPowerSavingsBatterySaver;
        if (thresholdChanged) {
            runOnBgThreadLazy(this.mThresholdChangeLogger, 2000);
        }
        if (!this.mSettingBatterySaverStickyAutoDisableEnabled) {
            hideStickyDisabledNotification();
        }
        if (enabledChanged) {
            java.lang.String reason = batterySaverEnabled ? "Global.low_power changed to 1" : "Global.low_power changed to 0";
            enableBatterySaverLocked(batterySaverEnabled, true, 8, reason);
        } else {
            doAutoBatterySaverLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        com.android.server.EventLogTags.writeBatterySaverSetting(this.mSettingBatterySaverTriggerThreshold);
    }

    public void setBatteryStatus(boolean newPowered, int newLevel, boolean newBatteryLevelLow) {
        synchronized (this.mLock) {
            boolean lowChanged = true;
            this.mBatteryStatusSet = true;
            boolean poweredChanged = this.mIsPowered != newPowered;
            boolean levelChanged = this.mBatteryLevel != newLevel;
            if (this.mIsBatteryLevelLow == newBatteryLevelLow) {
                lowChanged = false;
            }
            if (poweredChanged || levelChanged || lowChanged) {
                this.mIsPowered = newPowered;
                this.mBatteryLevel = newLevel;
                this.mIsBatteryLevelLow = newBatteryLevelLow;
                doAutoBatterySaverLocked();
            }
        }
    }

    public android.os.BatterySaverPolicyConfig getFullBatterySaverPolicy() {
        android.os.BatterySaverPolicyConfig policyLocked;
        synchronized (this.mLock) {
            policyLocked = this.mBatterySaverController.getPolicyLocked(2);
        }
        return policyLocked;
    }

    public boolean setFullBatterySaverPolicy(android.os.BatterySaverPolicyConfig config) {
        boolean fullPolicyLocked;
        synchronized (this.mLock) {
            fullPolicyLocked = this.mBatterySaverController.setFullPolicyLocked(config, 13);
        }
        return fullPolicyLocked;
    }

    public boolean setAdaptiveBatterySaverEnabled(boolean enabled) {
        boolean adaptivePolicyEnabledLocked;
        synchronized (this.mLock) {
            this.mLastAdaptiveBatterySaverChangedExternallyElapsed = android.os.SystemClock.elapsedRealtime();
            adaptivePolicyEnabledLocked = this.mBatterySaverController.setAdaptivePolicyEnabledLocked(enabled, 11);
        }
        return adaptivePolicyEnabledLocked;
    }

    public boolean setAdaptiveBatterySaverPolicy(android.os.BatterySaverPolicyConfig config) {
        boolean adaptivePolicyLocked;
        synchronized (this.mLock) {
            this.mLastAdaptiveBatterySaverChangedExternallyElapsed = android.os.SystemClock.elapsedRealtime();
            adaptivePolicyLocked = this.mBatterySaverController.setAdaptivePolicyLocked(config, 11);
        }
        return adaptivePolicyLocked;
    }

    private void doAutoBatterySaverLocked() {
        if (!this.mBSSMExt.isOplusFeatureDisalbed() || !this.mBootCompleted || !this.mSettingsLoaded || !this.mBatteryStatusSet) {
            return;
        }
        updateStateLocked(false, false);
        if (android.os.SystemClock.elapsedRealtime() - this.mLastAdaptiveBatterySaverChangedExternallyElapsed > 86400000) {
            this.mBatterySaverController.setAdaptivePolicyEnabledLocked(false, 12);
            this.mBatterySaverController.resetAdaptivePolicyLocked(12);
        } else if (this.mIsPowered && this.mBatteryLevel >= 80) {
            this.mBatterySaverController.setAdaptivePolicyEnabledLocked(false, 7);
        }
    }

    private void updateStateLocked(boolean manual, boolean enable) {
        if (!manual && (!this.mBootCompleted || !this.mSettingsLoaded || !this.mBatteryStatusSet)) {
            return;
        }
        switch (this.mState) {
            case 1:
                if (!this.mIsPowered) {
                    if (manual) {
                        if (!enable) {
                            android.util.Slog.e(TAG, "Tried to disable BS when it's already OFF");
                        } else {
                            enableBatterySaverLocked(true, true, 2);
                            hideStickyDisabledNotification();
                            this.mState = 2;
                        }
                        break;
                    } else if (isAutomaticModeActiveLocked() && isInAutomaticLowZoneLocked()) {
                        enableBatterySaverLocked(true, false, 0);
                        hideStickyDisabledNotification();
                        this.mState = 3;
                        break;
                    } else if (isDynamicModeActiveLocked() && isInDynamicLowZoneLocked()) {
                        enableBatterySaverLocked(true, false, 9);
                        hideStickyDisabledNotification();
                        this.mState = 3;
                        break;
                    }
                }
                break;
            case 2:
                if (manual) {
                    if (enable) {
                        android.util.Slog.e(TAG, "Tried to enable BS when it's already MANUAL_ON");
                    } else {
                        enableBatterySaverLocked(false, true, 3);
                        this.mState = 1;
                    }
                } else if (this.mIsPowered) {
                    enableBatterySaverLocked(false, false, 7);
                    if (this.mSettingBatterySaverEnabledSticky && !this.mBatterySaverStickyBehaviourDisabled) {
                        this.mState = 5;
                    } else {
                        this.mState = 1;
                    }
                }
                break;
            case 3:
                if (this.mIsPowered) {
                    enableBatterySaverLocked(false, false, 7);
                    this.mState = 1;
                    break;
                } else if (manual) {
                    if (enable) {
                        android.util.Slog.e(TAG, "Tried to enable BS when it's already AUTO_ON");
                    } else {
                        enableBatterySaverLocked(false, true, 3);
                        this.mState = 4;
                    }
                    break;
                } else if (isAutomaticModeActiveLocked() && !isInAutomaticLowZoneLocked()) {
                    enableBatterySaverLocked(false, false, 1);
                    this.mState = 1;
                    break;
                } else if (isDynamicModeActiveLocked() && !isInDynamicLowZoneLocked()) {
                    enableBatterySaverLocked(false, false, 10);
                    this.mState = 1;
                    break;
                } else if (!isAutomaticModeActiveLocked() && !isDynamicModeActiveLocked()) {
                    enableBatterySaverLocked(false, false, 8);
                    this.mState = 1;
                    break;
                }
                break;
            case 4:
                if (manual) {
                    if (!enable) {
                        android.util.Slog.e(TAG, "Tried to disable BS when it's already AUTO_SNOOZED");
                    } else {
                        enableBatterySaverLocked(true, true, 2);
                        this.mState = 2;
                    }
                } else if (this.mIsPowered || ((isAutomaticModeActiveLocked() && !isInAutomaticLowZoneLocked()) || ((isDynamicModeActiveLocked() && !isInDynamicLowZoneLocked()) || (!isAutomaticModeActiveLocked() && !isDynamicModeActiveLocked())))) {
                    this.mState = 1;
                }
                break;
            case 5:
                if (manual) {
                    android.util.Slog.e(TAG, "Tried to manually change BS state from PENDING_STICKY_ON");
                } else {
                    boolean shouldTurnOffSticky = this.mSettingBatterySaverStickyAutoDisableEnabled && this.mBatteryLevel >= this.mSettingBatterySaverStickyAutoDisableThreshold;
                    boolean isStickyDisabled = this.mBatterySaverStickyBehaviourDisabled || !this.mSettingBatterySaverEnabledSticky;
                    if (isStickyDisabled || shouldTurnOffSticky) {
                        this.mState = 1;
                        setStickyActive(false);
                        triggerStickyDisabledNotification();
                    } else if (!this.mIsPowered) {
                        enableBatterySaverLocked(true, true, 4);
                        this.mState = 2;
                    }
                }
                break;
            default:
                android.util.Slog.wtf(TAG, "Unknown state: " + this.mState);
                break;
        }
    }

    int getState() {
        int i;
        synchronized (this.mLock) {
            i = this.mState;
        }
        return i;
    }

    public void setBatterySaverEnabledManually(boolean enabled) {
        synchronized (this.mLock) {
            if (!this.mBSSMExt.isOplusFeatureDisalbed()) {
                android.util.Slog.d(TAG, "setBatterySaverEnabledManually: onSetBatterySaverEnabledManually. enabled=" + enabled);
                this.mBSSMExt.onSetBatterySaverEnabledManually(enabled);
            } else {
                android.util.Slog.d(TAG, "setBatterySaverEnabledManually: updateStateLocked. enabled=" + enabled);
                updateStateLocked(true, enabled);
            }
        }
    }

    private void enableBatterySaverLocked(boolean enable, boolean manual, int intReason) {
        enableBatterySaverLocked(enable, manual, intReason, com.android.server.power.batterysaver.BatterySaverController.reasonToString(intReason));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableBatterySaverLocked(boolean z, boolean z2, int i, java.lang.String str) {
        if (this.mBatterySaverController.isFullEnabled() == z) {
            android.util.Slog.d(TAG, "Already " + (z ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
            return;
        }
        if (z && this.mIsPowered && this.mBSSMExt.isOplusFeatureDisalbed()) {
            return;
        }
        this.mLastChangedIntReason = i;
        this.mLastChangedStrReason = str;
        this.mSettingBatterySaverEnabled = z;
        putGlobalSetting("low_power", z ? 1 : 0);
        if (z2) {
            setStickyActive(!this.mBatterySaverStickyBehaviourDisabled && z);
        }
        this.mBatterySaverController.enableBatterySaver(z, i);
        android.util.Slog.d(TAG, "enableBatterySaver: Enabled=" + z + " manual=" + z2 + " reason=" + str + "(" + i + ")");
        if (i == 9 || i == 0) {
            if (com.android.server.power.batterysaver.Flags.updateAutoTurnOnNotificationStringAndAction()) {
                triggerDynamicModeNotificationV2();
                return;
            } else {
                triggerDynamicModeNotification();
                return;
            }
        }
        if (!z) {
            hideDynamicModeNotification();
        }
    }

    void triggerDynamicModeNotification() {
        runOnBgThread(new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$triggerDynamicModeNotification$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerDynamicModeNotification$2() {
        android.app.NotificationManager manager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        ensureNotificationChannelExists(manager, DYNAMIC_MODE_NOTIF_CHANNEL_ID, android.R.string.dynamic_mode_notification_channel_name);
        manager.notifyAsUser(TAG, DYNAMIC_MODE_NOTIFICATION_ID, buildNotification(DYNAMIC_MODE_NOTIF_CHANNEL_ID, android.R.string.dynamic_mode_notification_title, android.R.string.dynamic_mode_notification_summary, "android.settings.BATTERY_SAVER_SETTINGS", 0L), android.os.UserHandle.ALL);
    }

    void triggerDynamicModeNotificationV2() {
        runOnBgThread(new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$triggerDynamicModeNotificationV2$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerDynamicModeNotificationV2$3() {
        android.app.NotificationManager manager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        ensureNotificationChannelExists(manager, DYNAMIC_MODE_NOTIF_CHANNEL_ID, android.R.string.dynamic_mode_notification_channel_name);
        android.os.Bundle highlightBundle = new android.os.Bundle(1);
        highlightBundle.putString(EXTRA_FRAGMENT_ARG_KEY, PREFERENCE_KEY_BATTERY_SAVER_SCHEDULER);
        manager.notifyAsUser(TAG, DYNAMIC_MODE_NOTIFICATION_ID, buildNotificationV2(DYNAMIC_MODE_NOTIF_CHANNEL_ID, android.R.string.dynamic_mode_notification_title_v2, android.R.string.dynamic_mode_notification_summary_v2, "android.settings.BATTERY_SAVER_SETTINGS", 0L, highlightBundle), android.os.UserHandle.ALL);
    }

    void triggerStickyDisabledNotification() {
        if (!this.mBatterySaverTurnedOffNotificationEnabled) {
            return;
        }
        runOnBgThread(new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$triggerStickyDisabledNotification$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerStickyDisabledNotification$4() {
        android.app.NotificationManager manager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        ensureNotificationChannelExists(manager, BATTERY_SAVER_NOTIF_CHANNEL_ID, android.R.string.bg_user_sound_notification_message);
        manager.notifyAsUser(TAG, STICKY_AUTO_DISABLED_NOTIFICATION_ID, buildNotification(BATTERY_SAVER_NOTIF_CHANNEL_ID, android.R.string.biometric_dangling_notification_action_not_now, android.R.string.battery_saver_off_notification_title, "android.settings.BATTERY_SAVER_SETTINGS", STICKY_DISABLED_NOTIFY_TIMEOUT_MS), android.os.UserHandle.ALL);
    }

    private void ensureNotificationChannelExists(android.app.NotificationManager manager, java.lang.String channelId, int nameId) {
        android.app.NotificationChannel channel = new android.app.NotificationChannel(channelId, this.mContext.getText(nameId), 3);
        channel.setSound(null, null);
        channel.setBlockable(true);
        manager.createNotificationChannel(channel);
    }

    private android.app.Notification buildNotification(java.lang.String channelId, int titleId, int summaryId, java.lang.String intentAction, long timeoutMs) {
        android.content.res.Resources res = this.mContext.getResources();
        android.content.Intent intent = new android.content.Intent(intentAction);
        intent.setFlags(268468224);
        android.app.PendingIntent batterySaverIntent = android.app.PendingIntent.getActivity(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
        java.lang.String title = res.getString(titleId);
        java.lang.String summary = res.getString(summaryId);
        return new android.app.Notification.Builder(this.mContext, channelId).setSmallIcon(android.R.drawable.global_actions_item_red_background).setContentTitle(title).setContentText(summary).setContentIntent(batterySaverIntent).setStyle(new android.app.Notification.BigTextStyle().bigText(summary)).setOnlyAlertOnce(true).setAutoCancel(true).setTimeoutAfter(timeoutMs).build();
    }

    private android.app.Notification buildNotificationV2(java.lang.String channelId, int titleId, int summaryId, java.lang.String intentAction, long timeoutMs, android.os.Bundle highlightBundle) {
        android.content.res.Resources res = this.mContext.getResources();
        android.content.Intent intent = new android.content.Intent(intentAction).setFlags(268468224).putExtra(EXTRA_SHOW_FRAGMENT_TITLE, highlightBundle);
        android.app.PendingIntent batterySaverIntent = android.app.PendingIntent.getActivity(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
        java.lang.String title = res.getString(titleId);
        java.lang.String summary = res.getString(summaryId);
        return new android.app.Notification.Builder(this.mContext, channelId).setSmallIcon(android.R.drawable.global_actions_item_red_background).setContentTitle(title).setContentText(summary).setContentIntent(batterySaverIntent).setStyle(new android.app.Notification.BigTextStyle().bigText(summary)).setOnlyAlertOnce(true).setAutoCancel(true).setTimeoutAfter(timeoutMs).build();
    }

    private void hideDynamicModeNotification() {
        hideNotification(DYNAMIC_MODE_NOTIFICATION_ID);
    }

    private void hideStickyDisabledNotification() {
        hideNotification(STICKY_AUTO_DISABLED_NOTIFICATION_ID);
    }

    private void hideNotification(final int notificationId) {
        runOnBgThread(new java.lang.Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$hideNotification$5(notificationId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideNotification$5(int notificationId) {
        android.app.NotificationManager manager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        manager.cancelAsUser(TAG, notificationId, android.os.UserHandle.ALL);
    }

    private void setStickyActive(boolean z) {
        this.mSettingBatterySaverEnabledSticky = z;
        putGlobalSetting("low_power_sticky", this.mSettingBatterySaverEnabledSticky ? 1 : 0);
    }

    protected void putGlobalSetting(java.lang.String key, int value) {
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), key, value);
    }

    protected int getGlobalSetting(java.lang.String key, int defValue) {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), key, defValue);
    }

    public void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        ipw.println();
        ipw.println("Battery saver state machine:");
        ipw.increaseIndent();
        synchronized (this.mLock) {
            ipw.print("Enabled=");
            ipw.println(this.mBatterySaverController.isEnabled());
            ipw.increaseIndent();
            ipw.print("full=");
            ipw.println(this.mBatterySaverController.isFullEnabled());
            ipw.print("adaptive=");
            ipw.print(this.mBatterySaverController.isAdaptiveEnabled());
            if (this.mBatterySaverController.isAdaptiveEnabled()) {
                ipw.print(" (advertise=");
                ipw.print(getBatterySaverPolicy().shouldAdvertiseIsEnabled());
                ipw.print(")");
            }
            ipw.decreaseIndent();
            ipw.println();
            ipw.print("mState=");
            ipw.println(this.mState);
            ipw.print("mLastChangedIntReason=");
            ipw.println(this.mLastChangedIntReason);
            ipw.print("mLastChangedStrReason=");
            ipw.println(this.mLastChangedStrReason);
            ipw.print("mBootCompleted=");
            ipw.println(this.mBootCompleted);
            ipw.print("mSettingsLoaded=");
            ipw.println(this.mSettingsLoaded);
            ipw.print("mBatteryStatusSet=");
            ipw.println(this.mBatteryStatusSet);
            ipw.print("mIsPowered=");
            ipw.println(this.mIsPowered);
            ipw.print("mBatteryLevel=");
            ipw.println(this.mBatteryLevel);
            ipw.print("mIsBatteryLevelLow=");
            ipw.println(this.mIsBatteryLevelLow);
            ipw.print("mSettingAutomaticBatterySaver=");
            ipw.println(this.mSettingAutomaticBatterySaver);
            ipw.print("mSettingBatterySaverEnabled=");
            ipw.println(this.mSettingBatterySaverEnabled);
            ipw.print("mSettingBatterySaverEnabledSticky=");
            ipw.println(this.mSettingBatterySaverEnabledSticky);
            ipw.print("mSettingBatterySaverStickyAutoDisableEnabled=");
            ipw.println(this.mSettingBatterySaverStickyAutoDisableEnabled);
            ipw.print("mSettingBatterySaverStickyAutoDisableThreshold=");
            ipw.println(this.mSettingBatterySaverStickyAutoDisableThreshold);
            ipw.print("mSettingBatterySaverTriggerThreshold=");
            ipw.println(this.mSettingBatterySaverTriggerThreshold);
            ipw.print("mBatterySaverStickyBehaviourDisabled=");
            ipw.println(this.mBatterySaverStickyBehaviourDisabled);
            ipw.print("mBatterySaverTurnedOffNotificationEnabled=");
            ipw.println(this.mBatterySaverTurnedOffNotificationEnabled);
            ipw.print("mDynamicPowerSavingsDefaultDisableThreshold=");
            ipw.println(this.mDynamicPowerSavingsDefaultDisableThreshold);
            ipw.print("mDynamicPowerSavingsDisableThreshold=");
            ipw.println(this.mDynamicPowerSavingsDisableThreshold);
            ipw.print("mDynamicPowerSavingsEnableBatterySaver=");
            ipw.println(this.mDynamicPowerSavingsEnableBatterySaver);
            ipw.print("mLastAdaptiveBatterySaverChangedExternallyElapsed=");
            ipw.println(this.mLastAdaptiveBatterySaverChangedExternallyElapsed);
        }
        ipw.decreaseIndent();
    }

    public void dumpProto(android.util.proto.ProtoOutputStream proto, long tag) {
        synchronized (this.mLock) {
            long token = proto.start(tag);
            proto.write(1133871366145L, this.mBatterySaverController.isEnabled());
            proto.write(1159641169938L, this.mState);
            proto.write(1133871366158L, this.mBatterySaverController.isFullEnabled());
            proto.write(1133871366159L, this.mBatterySaverController.isAdaptiveEnabled());
            proto.write(1133871366160L, getBatterySaverPolicy().shouldAdvertiseIsEnabled());
            proto.write(1133871366146L, this.mBootCompleted);
            proto.write(1133871366147L, this.mSettingsLoaded);
            proto.write(1133871366148L, this.mBatteryStatusSet);
            proto.write(1133871366150L, this.mIsPowered);
            proto.write(1120986464263L, this.mBatteryLevel);
            proto.write(1133871366152L, this.mIsBatteryLevelLow);
            proto.write(1159641169939L, this.mSettingAutomaticBatterySaver);
            proto.write(1133871366153L, this.mSettingBatterySaverEnabled);
            proto.write(1133871366154L, this.mSettingBatterySaverEnabledSticky);
            proto.write(1120986464267L, this.mSettingBatterySaverTriggerThreshold);
            proto.write(1133871366156L, this.mSettingBatterySaverStickyAutoDisableEnabled);
            proto.write(1120986464269L, this.mSettingBatterySaverStickyAutoDisableThreshold);
            proto.write(1120986464276L, this.mDynamicPowerSavingsDefaultDisableThreshold);
            proto.write(1120986464277L, this.mDynamicPowerSavingsDisableThreshold);
            proto.write(1133871366166L, this.mDynamicPowerSavingsEnableBatterySaver);
            proto.write(1112396529681L, this.mLastAdaptiveBatterySaverChangedExternallyElapsed);
            proto.end(token);
        }
    }

    public com.android.server.power.batterysaver.IBatterySaverStateMachineWrapper getWrapper() {
        return this.mBSSMWrapper;
    }

    private class BatterySaverStateMachineWrapper implements com.android.server.power.batterysaver.IBatterySaverStateMachineWrapper {
        private BatterySaverStateMachineWrapper() {
        }

        @Override // com.android.server.power.batterysaver.IBatterySaverStateMachineWrapper
        public void enableBatterySaverLocked(boolean enable, boolean manual, int intReason, java.lang.String strReason) {
            com.android.server.power.batterysaver.BatterySaverStateMachine.this.enableBatterySaverLocked(enable, manual, intReason, strReason);
        }
    }
}
