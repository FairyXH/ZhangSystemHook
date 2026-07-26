package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibrationSettings {
    private static final java.lang.String TAG = "VibrationSettings";
    private static final int VIBRATE_ON_DISABLED_USAGE_ALLOWED = 66;
    private android.media.AudioManager mAudioManager;
    private boolean mBatterySaverMode;
    private final android.content.Context mContext;
    private android.util.SparseIntArray mCurrentVibrationIntensities;
    private final android.util.SparseArray<android.os.VibrationEffect> mFallbackEffects;
    private boolean mKeyboardVibrationOn;
    private final java.util.List<com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged> mListeners;
    private final java.lang.Object mLock;
    private boolean mOnWirelessCharger;
    private android.os.PowerManagerInternal mPowerManagerInternal;
    private int mRingerMode;
    final com.android.server.vibrator.VibrationSettings.SettingsBroadcastReceiver mSettingChangeReceiver;
    final com.android.server.vibrator.VibrationSettings.SettingsContentObserver mSettingObserver;
    private final java.lang.String mSystemUiPackage;
    final com.android.server.vibrator.VibrationSettings.VibrationUidObserver mUidObserver;
    final com.android.server.vibrator.VibrationSettings.VibrationUserSwitchObserver mUserSwitchObserver;
    private boolean mVibrateInputDevices;
    private boolean mVibrateOn;
    private final android.os.vibrator.VibrationConfig mVibrationConfig;
    private final com.android.server.vibrator.IVibrationSettingsWrapper mVibrationSettingsWrapper;
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal mVirtualDeviceManagerInternal;
    private static final java.util.Set<java.lang.Integer> BACKGROUND_PROCESS_USAGE_ALLOWLIST = new java.util.HashSet(java.util.Arrays.asList(33, 17, 49, 65, 50, 34));
    private static final java.util.Set<java.lang.Integer> BATTERY_SAVER_USAGE_ALLOWLIST = new java.util.HashSet(java.util.Arrays.asList(33, 17, 65, 34, 50));
    private static final java.util.Set<java.lang.Integer> SYSTEM_VIBRATION_SCREEN_OFF_USAGE_ALLOWLIST = new java.util.HashSet(java.util.Arrays.asList(18, 66, 34, 50));
    private static final java.util.Set<java.lang.Integer> POWER_MANAGER_SLEEP_REASON_ALLOWLIST = new java.util.HashSet(java.util.Arrays.asList(9, 2));
    private static final android.content.IntentFilter INTERNAL_RINGER_MODE_CHANGED_INTENT_FILTER = new android.content.IntentFilter("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION");

    interface OnVibratorSettingsChanged {
        void onChange();
    }

    VibrationSettings(android.content.Context context, android.os.Handler handler) {
        this(context, handler, new android.os.vibrator.VibrationConfig(context.getResources()));
    }

    VibrationSettings(android.content.Context context, android.os.Handler handler, android.os.vibrator.VibrationConfig config) {
        this.mLock = new java.lang.Object();
        this.mListeners = new java.util.ArrayList();
        this.mCurrentVibrationIntensities = new android.util.SparseIntArray();
        this.mVibrationSettingsWrapper = new com.android.server.vibrator.VibrationSettings.VibrationSettingsWrapper();
        this.mContext = context;
        this.mVibrationConfig = config;
        this.mSettingObserver = new com.android.server.vibrator.VibrationSettings.SettingsContentObserver(handler);
        this.mSettingChangeReceiver = new com.android.server.vibrator.VibrationSettings.SettingsBroadcastReceiver();
        this.mUidObserver = new com.android.server.vibrator.VibrationSettings.VibrationUidObserver();
        this.mUserSwitchObserver = new com.android.server.vibrator.VibrationSettings.VibrationUserSwitchObserver();
        this.mSystemUiPackage = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getSystemUiServiceComponent().getPackageName();
        android.os.VibrationEffect clickEffect = createEffectFromResource(android.R.array.config_trustedAccessibilityServices);
        android.os.VibrationEffect doubleClickEffect = createEffectFromResource(android.R.array.config_display_no_service_when_sim_unready);
        android.os.VibrationEffect heavyClickEffect = createEffectFromResource(android.R.array.config_locationProviderPackageNames);
        android.os.VibrationEffect tickEffect = createEffectFromResource(android.R.array.config_cdma_dun_supported_types);
        this.mFallbackEffects = new android.util.SparseArray<>();
        this.mFallbackEffects.put(0, clickEffect);
        this.mFallbackEffects.put(1, doubleClickEffect);
        this.mFallbackEffects.put(2, tickEffect);
        this.mFallbackEffects.put(5, heavyClickEffect);
        this.mFallbackEffects.put(21, android.os.VibrationEffect.get(2, false));
        this.mVibrationSettingsWrapper.getExtImpl().init(this.mContext);
        update();
    }

    public void onSystemReady() {
        android.content.Intent batteryStatus;
        android.os.PowerManagerInternal pm = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        android.media.AudioManager am = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
        int ringerMode = am.getRingerModeInternal();
        synchronized (this.mLock) {
            this.mPowerManagerInternal = pm;
            this.mAudioManager = am;
            this.mRingerMode = ringerMode;
        }
        try {
            android.app.ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
        }
        try {
            android.app.ActivityManager.getService().registerUserSwitchObserver(this.mUserSwitchObserver, TAG);
        } catch (android.os.RemoteException e2) {
        }
        pm.registerLowPowerModeObserver(new android.os.PowerManagerInternal.LowPowerModeListener() { // from class: com.android.server.vibrator.VibrationSettings.1
            public int getServiceType() {
                return 2;
            }

            public void onLowPowerModeChanged(android.os.PowerSaveState result) {
                boolean shouldNotifyListeners;
                synchronized (com.android.server.vibrator.VibrationSettings.this.mLock) {
                    shouldNotifyListeners = result.batterySaverEnabled != com.android.server.vibrator.VibrationSettings.this.mBatterySaverMode;
                    com.android.server.vibrator.VibrationSettings.this.mBatterySaverMode = result.batterySaverEnabled;
                }
                if (shouldNotifyListeners) {
                    com.android.server.vibrator.VibrationSettings.this.notifyListeners();
                }
            }
        });
        registerSettingsChangeReceiver(INTERNAL_RINGER_MODE_CHANGED_INTENT_FILTER);
        registerSettingsObserver(android.provider.Settings.System.getUriFor("vibrate_input_devices"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("vibrate_on"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("haptic_feedback_enabled"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("alarm_vibration_intensity"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("haptic_feedback_intensity"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("hardware_haptic_feedback_intensity"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("media_vibration_intensity"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("notification_vibration_intensity"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("ring_vibration_intensity"));
        registerSettingsObserver(android.provider.Settings.System.getUriFor("keyboard_vibration_enabled"));
        if (this.mVibrationConfig.ignoreVibrationsOnWirelessCharger() && (batteryStatus = this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.vibrator.VibrationSettings.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                com.android.server.vibrator.VibrationSettings.this.updateBatteryInfo(intent);
            }
        }, new android.content.IntentFilter("android.intent.action.BATTERY_CHANGED"), 4)) != null) {
            updateBatteryInfo(batteryStatus);
        }
        this.mVibrationSettingsWrapper.getExtImpl().onSystemReady();
        update();
    }

    public void addListener(com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged listener) {
        synchronized (this.mLock) {
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
            }
        }
    }

    public void removeListener(com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged listener) {
        synchronized (this.mLock) {
            this.mListeners.remove(listener);
        }
    }

    public int getRampStepDuration() {
        return this.mVibrationConfig.getRampStepDurationMs();
    }

    public int getRampDownDuration() {
        return this.mVibrationConfig.getRampDownDurationMs();
    }

    public int getDefaultIntensity(int usageHint) {
        return this.mVibrationConfig.getDefaultVibrationIntensity(usageHint);
    }

    public int getCurrentIntensity(int usageHint) {
        int i;
        int defaultIntensity = getDefaultIntensity(usageHint);
        synchronized (this.mLock) {
            i = this.mCurrentVibrationIntensities.get(usageHint, defaultIntensity);
        }
        return i;
    }

    public int getRequestVibrationParamsTimeoutMs() {
        return this.mVibrationConfig.getRequestVibrationParamsTimeoutMs();
    }

    public int[] getRequestVibrationParamsForUsages() {
        return this.mVibrationConfig.getRequestVibrationParamsForUsages();
    }

    public android.os.VibrationEffect getFallbackEffect(int effectId) {
        return this.mFallbackEffects.get(effectId);
    }

    public boolean shouldVibrateInputDevices() {
        return this.mVibrateInputDevices;
    }

    public com.android.server.vibrator.Vibration.Status shouldIgnoreVibration(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        int usage = callerInfo.attrs.getUsage();
        synchronized (this.mLock) {
            if (this.mVibrationSettingsWrapper.getExtImpl().shouldIgnoreVibrationForPowerSaveMode(callerInfo)) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_FOR_POWER;
            }
            if (!this.mUidObserver.isUidForeground(callerInfo.uid) && !BACKGROUND_PROCESS_USAGE_ALLOWLIST.contains(java.lang.Integer.valueOf(usage))) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_BACKGROUND;
            }
            if (callerInfo.deviceId != 0 && callerInfo.deviceId != -1) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_FROM_VIRTUAL_DEVICE;
            }
            if (callerInfo.deviceId == -1 && isAppRunningOnAnyVirtualDevice(callerInfo.uid)) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_FROM_VIRTUAL_DEVICE;
            }
            if (this.mBatterySaverMode && !BATTERY_SAVER_USAGE_ALLOWLIST.contains(java.lang.Integer.valueOf(usage))) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_FOR_POWER;
            }
            if (!callerInfo.attrs.isFlagSet(2) && !shouldVibrateForUserSetting(callerInfo)) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_FOR_SETTINGS;
            }
            if (!callerInfo.attrs.isFlagSet(1) && !shouldVibrateForRingerModeLocked(usage)) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_FOR_RINGER_MODE;
            }
            if (this.mVibrationConfig.ignoreVibrationsOnWirelessCharger() && this.mOnWirelessCharger) {
                return com.android.server.vibrator.Vibration.Status.IGNORED_ON_WIRELESS_CHARGER;
            }
            return null;
        }
    }

    public boolean shouldCancelVibrationOnScreenOff(com.android.server.vibrator.Vibration.CallerInfo callerInfo, long vibrationStartUptimeMillis) {
        android.os.PowerManagerInternal pm;
        synchronized (this.mLock) {
            pm = this.mPowerManagerInternal;
        }
        if (pm != null) {
            android.os.PowerManager.SleepData sleepData = pm.getLastGoToSleep();
            if (sleepData.goToSleepUptimeMillis < vibrationStartUptimeMillis || POWER_MANAGER_SLEEP_REASON_ALLOWLIST.contains(java.lang.Integer.valueOf(sleepData.goToSleepReason))) {
                android.util.Slog.d(TAG, "Ignoring screen off event triggered at uptime " + sleepData.goToSleepUptimeMillis + " for reason " + android.os.PowerManager.sleepReasonToString(sleepData.goToSleepReason));
                return false;
            }
        }
        if (SYSTEM_VIBRATION_SCREEN_OFF_USAGE_ALLOWLIST.contains(java.lang.Integer.valueOf(callerInfo.attrs.getUsage()))) {
            return (callerInfo.uid == 1000 || callerInfo.uid == 0 || this.mSystemUiPackage.equals(callerInfo.opPkg)) ? false : true;
        }
        return true;
    }

    private boolean shouldVibrateForRingerModeLocked(int usageHint) {
        if (usageHint != 33 && usageHint != 49) {
            return true;
        }
        return this.mVibrationSettingsWrapper.getExtImpl().shouldVibrateForRingerModeLocked(getCurrentIntensity(usageHint), this.mRingerMode);
    }

    private boolean shouldVibrateForUserSetting(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        int usage = callerInfo.attrs.getUsage();
        if (!this.mVibrateOn && 66 != usage) {
            return false;
        }
        if (android.os.vibrator.Flags.keyboardCategoryEnabled() && this.mVibrationConfig.hasFixedKeyboardAmplitude()) {
            int category = callerInfo.attrs.getCategory();
            if (usage == 18 && category == 1) {
                return this.mKeyboardVibrationOn;
            }
        }
        return usage == 33 ? getCurrentIntensity(usage) != 0 || this.mVibrationSettingsWrapper.getExtImpl().shouldIgnoreVibration(usage, this.mRingerMode) : getCurrentIntensity(usage) != 0;
    }

    void update() {
        updateSettings(-2);
        updateRingerMode();
        notifyListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettings(int userHandle) {
        synchronized (this.mLock) {
            boolean z = true;
            this.mVibrateInputDevices = loadSystemSetting("vibrate_input_devices", 0, userHandle) > 0;
            this.mVibrateOn = loadSystemSetting("vibrate_on", 1, userHandle) > 0;
            if (loadSystemSetting("keyboard_vibration_enabled", this.mVibrationConfig.isDefaultKeyboardVibrationEnabled() ? 1 : 0, userHandle) <= 0) {
                z = false;
            }
            this.mKeyboardVibrationOn = z;
            int alarmIntensity = toIntensity(loadSystemSetting("alarm_vibration_intensity", -1, userHandle), getDefaultIntensity(17));
            int defaultHapticFeedbackIntensity = getDefaultIntensity(18);
            int hapticFeedbackIntensity = toIntensity(loadSystemSetting("haptic_feedback_intensity", -1, userHandle), defaultHapticFeedbackIntensity);
            int positiveHapticFeedbackIntensity = toPositiveIntensity(hapticFeedbackIntensity, defaultHapticFeedbackIntensity);
            int hardwareFeedbackIntensity = toIntensity(loadSystemSetting("hardware_haptic_feedback_intensity", -1, userHandle), positiveHapticFeedbackIntensity);
            int mediaIntensity = toIntensity(loadSystemSetting("media_vibration_intensity", -1, userHandle), getDefaultIntensity(19));
            int defaultNotificationIntensity = getDefaultIntensity(49);
            int notificationIntensity = toIntensity(loadSystemSetting("notification_vibration_intensity", -1, userHandle), defaultNotificationIntensity);
            int positiveNotificationIntensity = toPositiveIntensity(notificationIntensity, defaultNotificationIntensity);
            int ringIntensity = toIntensity(loadSystemSetting("ring_vibration_intensity", -1, userHandle), getDefaultIntensity(33));
            this.mCurrentVibrationIntensities.clear();
            this.mCurrentVibrationIntensities.put(17, alarmIntensity);
            this.mCurrentVibrationIntensities.put(49, notificationIntensity);
            this.mCurrentVibrationIntensities.put(19, mediaIntensity);
            this.mCurrentVibrationIntensities.put(0, mediaIntensity);
            this.mCurrentVibrationIntensities.put(33, ringIntensity);
            this.mCurrentVibrationIntensities.put(65, positiveNotificationIntensity);
            this.mCurrentVibrationIntensities.put(50, hardwareFeedbackIntensity);
            this.mCurrentVibrationIntensities.put(34, hardwareFeedbackIntensity);
            this.mCurrentVibrationIntensities.put(18, hapticFeedbackIntensity);
            this.mCurrentVibrationIntensities.put(66, positiveHapticFeedbackIntensity);
            this.mVibrationSettingsWrapper.getExtImpl().updateSettings();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRingerMode() {
        int ringerModeInternal;
        synchronized (this.mLock) {
            if (this.mAudioManager == null) {
                ringerModeInternal = 0;
            } else {
                ringerModeInternal = this.mAudioManager.getRingerModeInternal();
            }
            this.mRingerMode = ringerModeInternal;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatteryInfo(android.content.Intent intent) {
        int pluggedInfo = intent.getIntExtra("plugged", 0);
        synchronized (this.mLock) {
            this.mOnWirelessCharger = pluggedInfo == 4;
        }
    }

    public java.lang.String toString() {
        java.lang.String str;
        synchronized (this.mLock) {
            java.lang.StringBuilder vibrationIntensitiesString = new java.lang.StringBuilder("{");
            for (int i = 0; i < this.mCurrentVibrationIntensities.size(); i++) {
                int usage = this.mCurrentVibrationIntensities.keyAt(i);
                int intensity = this.mCurrentVibrationIntensities.valueAt(i);
                vibrationIntensitiesString.append(android.os.VibrationAttributes.usageToString(usage)).append("=(").append(intensityToString(intensity)).append(",default:").append(intensityToString(getDefaultIntensity(usage))).append("), ");
            }
            vibrationIntensitiesString.append('}');
            java.lang.String keyboardVibrationOnString = this.mKeyboardVibrationOn + " (default: " + this.mVibrationConfig.isDefaultKeyboardVibrationEnabled() + ")";
            str = "VibrationSettings{mVibratorConfig=" + this.mVibrationConfig + ", mVibrateOn=" + this.mVibrateOn + ", mKeyboardVibrationOn=" + keyboardVibrationOnString + ", mVibrateInputDevices=" + this.mVibrateInputDevices + ", mBatterySaverMode=" + this.mBatterySaverMode + ", mRingerMode=" + ringerModeToString(this.mRingerMode) + ", mOnWirelessCharger=" + this.mOnWirelessCharger + ", mVibrationIntensities=" + ((java.lang.Object) vibrationIntensitiesString) + ", mProcStatesCache=" + this.mUidObserver.mProcStatesCache + '}';
        }
        return str;
    }

    void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("VibrationSettings:");
            pw.increaseIndent();
            pw.println("vibrateOn = " + this.mVibrateOn);
            pw.println("keyboardVibrationOn = " + this.mKeyboardVibrationOn + ", default: " + this.mVibrationConfig.isDefaultKeyboardVibrationEnabled());
            pw.println("vibrateInputDevices = " + this.mVibrateInputDevices);
            pw.println("batterySaverMode = " + this.mBatterySaverMode);
            pw.println("ringerMode = " + ringerModeToString(this.mRingerMode));
            pw.println("onWirelessCharger = " + this.mOnWirelessCharger);
            pw.println("processStateCache size = " + this.mUidObserver.mProcStatesCache.size());
            pw.println("VibrationIntensities:");
            pw.increaseIndent();
            for (int i = 0; i < this.mCurrentVibrationIntensities.size(); i++) {
                int usage = this.mCurrentVibrationIntensities.keyAt(i);
                int intensity = this.mCurrentVibrationIntensities.valueAt(i);
                pw.println(android.os.VibrationAttributes.usageToString(usage) + " = " + intensityToString(intensity) + ", default: " + intensityToString(getDefaultIntensity(usage)));
            }
            pw.decreaseIndent();
            this.mVibrationConfig.dumpWithoutDefaultSettings(pw);
            pw.decreaseIndent();
        }
    }

    void dump(android.util.proto.ProtoOutputStream proto) {
        synchronized (this.mLock) {
            proto.write(1133871366168L, this.mVibrateOn);
            proto.write(1133871366169L, this.mKeyboardVibrationOn);
            proto.write(1133871366150L, this.mBatterySaverMode);
            proto.write(1120986464274L, getCurrentIntensity(17));
            proto.write(1120986464275L, getDefaultIntensity(17));
            proto.write(1120986464278L, getCurrentIntensity(50));
            proto.write(1120986464279L, getDefaultIntensity(50));
            proto.write(1120986464263L, getCurrentIntensity(18));
            proto.write(1120986464264L, getDefaultIntensity(18));
            proto.write(1120986464276L, getCurrentIntensity(19));
            proto.write(1120986464277L, getDefaultIntensity(19));
            proto.write(1120986464265L, getCurrentIntensity(49));
            proto.write(1120986464266L, getDefaultIntensity(49));
            proto.write(1120986464267L, getCurrentIntensity(33));
            proto.write(1120986464268L, getDefaultIntensity(33));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListeners() {
        java.util.List<com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged> currentListeners;
        synchronized (this.mLock) {
            currentListeners = new java.util.ArrayList<>(this.mListeners);
        }
        for (com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged listener : currentListeners) {
            listener.onChange();
        }
    }

    private static java.lang.String intensityToString(int intensity) {
        switch (intensity) {
            case 0:
                return "OFF";
            case 1:
                return "LOW";
            case 2:
                return "MEDIUM";
            case 3:
                return com.android.server.utils.PriorityDump.PRIORITY_ARG_HIGH;
            default:
                return "UNKNOWN INTENSITY " + intensity;
        }
    }

    private static java.lang.String ringerModeToString(int ringerMode) {
        switch (ringerMode) {
            case 0:
                return "silent";
            case 1:
                return "vibrate";
            case 2:
                return "normal";
            default:
                return java.lang.String.valueOf(ringerMode);
        }
    }

    private int toPositiveIntensity(int value, int defaultValue) {
        if (value == 0) {
            return defaultValue;
        }
        return toIntensity(value, defaultValue);
    }

    private int toIntensity(int value, int defaultValue) {
        if (value < 0 || value > 3) {
            return defaultValue;
        }
        return value;
    }

    private boolean loadBooleanSetting(java.lang.String settingKey, int userHandle) {
        return loadSystemSetting(settingKey, 0, userHandle) != 0;
    }

    private int loadSystemSetting(java.lang.String settingName, int defaultValue, int userHandle) {
        return android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), settingName, defaultValue, userHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSettingsObserver(android.net.Uri settingUri) {
        this.mContext.getContentResolver().registerContentObserver(settingUri, true, this.mSettingObserver, -1);
    }

    private void registerSettingsChangeReceiver(android.content.IntentFilter intentFilter) {
        this.mContext.registerReceiver(this.mSettingChangeReceiver, intentFilter, 2);
    }

    private android.os.VibrationEffect createEffectFromResource(int resId) {
        return createEffectFromResource(this.mContext.getResources(), resId);
    }

    static android.os.VibrationEffect createEffectFromResource(android.content.res.Resources res, int resId) {
        long[] timings = getLongIntArray(res, resId);
        return createEffectFromTimings(timings);
    }

    private static android.os.VibrationEffect createEffectFromTimings(long[] timings) {
        if (timings == null || timings.length == 0) {
            return null;
        }
        if (timings.length == 1) {
            return android.os.VibrationEffect.createOneShot(timings[0], -1);
        }
        return android.os.VibrationEffect.createWaveform(timings, -1);
    }

    private static long[] getLongIntArray(android.content.res.Resources r, int resid) {
        int[] ar = r.getIntArray(resid);
        if (ar == null) {
            return null;
        }
        long[] out = new long[ar.length];
        for (int i = 0; i < ar.length; i++) {
            out[i] = ar[i];
        }
        return out;
    }

    private boolean isAppRunningOnAnyVirtualDevice(int uid) {
        if (this.mVirtualDeviceManagerInternal == null) {
            this.mVirtualDeviceManagerInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        }
        return this.mVirtualDeviceManagerInternal != null && this.mVirtualDeviceManagerInternal.isAppRunningOnAnyVirtualDevice(uid);
    }

    final class SettingsContentObserver extends android.database.ContentObserver {
        SettingsContentObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            com.android.server.vibrator.VibrationSettings.this.updateSettings(-2);
            com.android.server.vibrator.VibrationSettings.this.notifyListeners();
        }
    }

    final class SettingsBroadcastReceiver extends android.content.BroadcastReceiver {
        SettingsBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if ("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION".equals(action)) {
                com.android.server.vibrator.VibrationSettings.this.updateRingerMode();
                com.android.server.vibrator.VibrationSettings.this.notifyListeners();
            }
        }
    }

    final class VibrationUidObserver extends android.app.UidObserver {
        private final android.util.SparseArray<java.lang.Integer> mProcStatesCache = new android.util.SparseArray<>();

        VibrationUidObserver() {
        }

        public boolean isUidForeground(int uid) {
            boolean z;
            synchronized (this) {
                z = this.mProcStatesCache.get(uid, 6).intValue() <= 6;
            }
            return z;
        }

        public void onUidGone(int uid, boolean disabled) {
            synchronized (this) {
                this.mProcStatesCache.delete(uid);
            }
        }

        public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
            synchronized (this) {
                this.mProcStatesCache.put(uid, java.lang.Integer.valueOf(procState));
            }
        }
    }

    final class VibrationUserSwitchObserver extends android.app.SynchronousUserSwitchObserver {
        VibrationUserSwitchObserver() {
        }

        public void onUserSwitching(int newUserId) {
            com.android.server.vibrator.VibrationSettings.this.updateSettings(newUserId);
            com.android.server.vibrator.VibrationSettings.this.notifyListeners();
        }

        public void onUserSwitchComplete(int newUserId) {
            com.android.server.vibrator.VibrationSettings.this.update();
        }
    }

    public com.android.server.vibrator.IVibrationSettingsWrapper getWrapper() {
        return this.mVibrationSettingsWrapper;
    }

    private class VibrationSettingsWrapper implements com.android.server.vibrator.IVibrationSettingsWrapper {
        private final com.android.server.vibrator.IVibrationSettingsExt mVibrationSettingsExt;

        private VibrationSettingsWrapper() {
            this.mVibrationSettingsExt = (com.android.server.vibrator.IVibrationSettingsExt) system.ext.loader.core.ExtLoader.type(com.android.server.vibrator.IVibrationSettingsExt.class).base(com.android.server.vibrator.VibrationSettings.this).create();
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public com.android.server.vibrator.IVibrationSettingsExt getExtImpl() {
            return this.mVibrationSettingsExt;
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void registerSettingsObserverExt(android.net.Uri settingUri) {
            com.android.server.vibrator.VibrationSettings.this.registerSettingsObserver(settingUri);
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void updateTouchUsageVibrationIntensity(int intensity) {
            com.android.server.vibrator.VibrationSettings.this.mCurrentVibrationIntensities.put(18, intensity);
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void updateNotificationUsageVibrationIntensity(int intensity) {
            com.android.server.vibrator.VibrationSettings.this.mCurrentVibrationIntensities.put(49, intensity);
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void updateRingtoneUsageVibrationIntensity(int intensity) {
            com.android.server.vibrator.VibrationSettings.this.mCurrentVibrationIntensities.put(33, intensity);
            com.android.server.vibrator.VibrationSettings.this.mCurrentVibrationIntensities.put(17, intensity);
        }
    }
}
