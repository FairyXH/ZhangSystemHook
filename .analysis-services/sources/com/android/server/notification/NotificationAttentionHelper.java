package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class NotificationAttentionHelper {
    private static final java.lang.String BREATHE_LIGHT_DEFAULT_ALLOW_PERIOD = "07002330";
    private static final int DEFAULT_NOTIFICATION_COOLDOWN_ALL = 1;
    private static final int DEFAULT_NOTIFICATION_COOLDOWN_ENABLED = 1;
    private static final int DEFAULT_NOTIFICATION_COOLDOWN_ENABLED_FOR_WORK = 1;
    private static final int DEFAULT_NOTIFICATION_COOLDOWN_VIBRATE_UNLOCKED = 0;
    private static final float DEFAULT_VOLUME = 1.0f;
    private static final long LIGHTS_OFF_DELAY = 7200000;
    private static final int REQUEST_CODE_LIGHTS_PERIOD_TIMEOUT = 11;
    private static final int REQUEST_CODE_LIGHTS_TIMEOUT = 10;
    private android.view.accessibility.AccessibilityManager mAccessibilityManager;
    private com.android.server.lights.LogicalLight mAttentionLight;
    private android.media.AudioManager mAudioManager;
    private int mCallState;
    private final android.content.Context mContext;
    private boolean mDisableNotificationEffects;
    private final com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.FlagResolver mFlagResolver;
    boolean mHasLight;
    private final android.media.AudioAttributes mInCallNotificationAudioAttributes;
    private final android.net.Uri mInCallNotificationUri;
    private final float mInCallNotificationVolume;
    private boolean mIsAutomotive;
    private android.app.KeyguardManager mKeyguardManager;
    private final com.android.server.notification.NotificationManagerPrivate mNMP;
    private final com.android.server.notification.INotificationManagerServiceWrapper mNMSWrapper;
    private boolean mNotificationCooldownApplyToAll;
    private boolean mNotificationCooldownEnabled;
    private boolean mNotificationCooldownForWorkEnabled;
    private boolean mNotificationCooldownVibrateUnlocked;
    private boolean mNotificationEffectsEnabledForAutomotive;
    private com.android.server.lights.LogicalLight mNotificationLight;
    private boolean mNotificationPulseEnabled;
    private final android.content.pm.PackageManager mPackageManager;
    private final com.android.server.notification.PreferencesHelper mPreferencesHelper;
    private final com.android.server.notification.NotificationAttentionHelper.SettingsObserver mSettingsObserver;
    private java.lang.String mSettingsSpecialLights;
    private java.lang.String mSoundNotificationKey;
    private final com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy mStrategy;
    private boolean mSystemReady;
    private final android.telephony.TelephonyManager mTelephonyManager;
    private final android.os.UserManager mUm;
    private final com.android.server.notification.NotificationUsageStats mUsageStats;
    private final boolean mUseAttentionLight;
    private java.lang.String mVibrateNotificationKey;
    private com.android.server.notification.VibratorHelper mVibratorHelper;
    private final com.android.server.notification.ZenModeHelper mZenModeHelper;
    static final java.lang.String TAG = "NotifAttentionHelper";
    static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    static final boolean DEBUG_INTERRUPTIVENESS = android.os.SystemProperties.getBoolean("debug.notification.interruptiveness", false);
    static final java.util.Set<java.lang.String> NOTIFICATION_AVALANCHE_TRIGGER_INTENTS = java.util.Set.of("android.intent.action.AIRPLANE_MODE", "android.intent.action.BOOT_COMPLETED", "android.intent.action.USER_SWITCHED", "android.intent.action.MANAGED_PROFILE_AVAILABLE");
    static final java.util.Map<java.lang.String, android.util.Pair<java.lang.String, java.lang.Boolean>> NOTIFICATION_AVALANCHE_TRIGGER_EXTRAS = java.util.Map.of("android.intent.action.AIRPLANE_MODE", new android.util.Pair("state", false), "android.intent.action.MANAGED_PROFILE_AVAILABLE", new android.util.Pair("android.intent.extra.QUIET_MODE", false));
    private static final java.lang.String ACTION_NOTIFICATION_LIGHTS_TIMEOUT = com.android.server.notification.NotificationManagerService.class.getSimpleName() + ".LIGHTS_TIMEOUT";
    private static final java.lang.String ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT = com.android.server.notification.NotificationManagerService.class.getSimpleName() + ".LIGHTS_PERIOD_TIMEOUT";
    public static boolean mCustomizeBreathLight = false;
    java.util.ArrayList<java.lang.String> mLights = new java.util.ArrayList<>();
    private boolean mInCallStateOffHook = false;
    private boolean mScreenOn = true;
    private boolean mUserPresent = false;
    private android.os.Binder mCallNotificationToken = null;
    private int mCurrentWorkProfileId = -10000;
    private final android.content.BroadcastReceiver mIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationAttentionHelper.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (action.equals("android.intent.action.SCREEN_ON")) {
                com.android.server.notification.NotificationAttentionHelper.this.mScreenOn = true;
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                com.android.server.notification.NotificationAttentionHelper.this.mScreenOn = false;
                com.android.server.notification.NotificationAttentionHelper.this.mUserPresent = false;
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            } else if (action.equals("android.intent.action.PHONE_STATE")) {
                com.android.server.notification.NotificationAttentionHelper.this.mInCallStateOffHook = android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK.equals(intent.getStringExtra("state"));
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            } else if (action.equals("android.intent.action.USER_PRESENT")) {
                com.android.server.notification.NotificationAttentionHelper.this.mUserPresent = true;
                if (com.android.server.notification.NotificationAttentionHelper.this.mNotificationLight != null) {
                    com.android.server.notification.NotificationAttentionHelper.this.mNotificationLight.turnOff();
                }
            } else if (action.equals("android.intent.action.USER_ADDED") || action.equals("android.intent.action.USER_REMOVED") || action.equals("android.intent.action.USER_SWITCHED") || action.equals("android.intent.action.USER_UNLOCKED")) {
                com.android.server.notification.NotificationAttentionHelper.this.loadUserSettings();
            } else if (action.equals("android.intent.action.TIME_SET")) {
                android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "Lights time changed");
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
                com.android.server.notification.NotificationAttentionHelper.this.scheduleLightsPeriod();
            }
            if (com.android.server.notification.Flags.crossAppPoliteNotifications() && com.android.server.notification.NotificationAttentionHelper.NOTIFICATION_AVALANCHE_TRIGGER_INTENTS.contains(action)) {
                boolean enableAvalancheStrategy = true;
                android.util.Pair<java.lang.String, java.lang.Boolean> expectedExtras = com.android.server.notification.NotificationAttentionHelper.NOTIFICATION_AVALANCHE_TRIGGER_EXTRAS.get(action);
                if (expectedExtras != null) {
                    enableAvalancheStrategy = intent.getBooleanExtra((java.lang.String) expectedExtras.first, false) == ((java.lang.Boolean) expectedExtras.second).booleanValue();
                }
                if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                    android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "Avalanche trigger intent received: " + action + ". Enabling avalanche strategy: " + enableAvalancheStrategy);
                }
                if (enableAvalancheStrategy && (com.android.server.notification.NotificationAttentionHelper.this.mStrategy instanceof com.android.server.notification.NotificationAttentionHelper.StrategyAvalanche)) {
                    ((com.android.server.notification.NotificationAttentionHelper.StrategyAvalanche) com.android.server.notification.NotificationAttentionHelper.this.mStrategy).setTriggerTimeMs(java.lang.System.currentTimeMillis());
                }
            }
        }
    };
    private boolean mScreenLocked = true;
    private java.lang.String mBreathAllowPeriod = BREATHE_LIGHT_DEFAULT_ALLOW_PERIOD;
    private boolean mIsFlashing = false;
    private int mBeginHour = 7;
    private int mBeginMinute = 0;
    private int mEndHour = 23;
    private int mEndMinute = 30;
    private final android.content.BroadcastReceiver mNotificationLightsOffReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationAttentionHelper.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (action == null) {
                return;
            }
            if (com.android.server.notification.NotificationAttentionHelper.ACTION_NOTIFICATION_LIGHTS_TIMEOUT.equals(action)) {
                if (com.android.server.notification.NotificationAttentionHelper.this.mIsFlashing) {
                    android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "Lights timeout, turn it off");
                }
                com.android.server.notification.NotificationAttentionHelper.this.clearLightsLocked();
            } else if (com.android.server.notification.NotificationAttentionHelper.ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT.equals(action)) {
                android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "Lights period timeout");
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
                com.android.server.notification.NotificationAttentionHelper.this.scheduleLightsPeriod();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    interface ExemptionProvider {
        boolean isExempted(com.android.server.notification.NotificationRecord notificationRecord);
    }

    public NotificationAttentionHelper(android.content.Context context, com.android.server.lights.LightsManager lightsManager, android.view.accessibility.AccessibilityManager accessibilityManager, android.content.pm.PackageManager packageManager, android.os.UserManager userManager, com.android.server.notification.NotificationUsageStats usageStats, com.android.server.notification.NotificationManagerPrivate notificationManagerPrivate, com.android.server.notification.ZenModeHelper zenModeHelper, com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.FlagResolver flagResolver, com.android.server.notification.INotificationManagerServiceWrapper nMSWrapper, com.android.server.notification.PreferencesHelper preferencesHelper) {
        this.mNMSWrapper = nMSWrapper;
        this.mPreferencesHelper = preferencesHelper;
        this.mContext = context;
        this.mPackageManager = packageManager;
        this.mTelephonyManager = (android.telephony.TelephonyManager) context.getSystemService(android.telephony.TelephonyManager.class);
        this.mAccessibilityManager = accessibilityManager;
        this.mUm = userManager;
        this.mNMP = notificationManagerPrivate;
        this.mUsageStats = usageStats;
        this.mZenModeHelper = zenModeHelper;
        this.mFlagResolver = flagResolver;
        this.mVibratorHelper = new com.android.server.notification.VibratorHelper(context);
        this.mNotificationLight = lightsManager.getLight(4);
        this.mAttentionLight = lightsManager.getLight(5);
        android.content.res.Resources resources = context.getResources();
        this.mUseAttentionLight = resources.getBoolean(android.R.bool.config_supportsConcurrentInternalDisplays);
        this.mHasLight = resources.getBoolean(android.R.bool.config_glanceableHubEnabled);
        if (android.provider.Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 0) {
            this.mDisableNotificationEffects = true;
        }
        this.mInCallNotificationUri = android.net.Uri.parse("file://" + resources.getString(android.R.string.config_managed_provisioning_package));
        this.mInCallNotificationAudioAttributes = new android.media.AudioAttributes.Builder().setContentType(4).setUsage(2).build();
        this.mInCallNotificationVolume = resources.getFloat(android.R.dimen.config_ambiguousGestureMultiplier);
        if (com.android.server.notification.Flags.politeNotifications()) {
            this.mStrategy = createPolitenessStrategy();
        } else {
            this.mStrategy = null;
        }
        this.mSettingsObserver = new com.android.server.notification.NotificationAttentionHelper.SettingsObserver();
        loadUserSettings();
    }

    private com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy createPolitenessStrategy() {
        if (com.android.server.notification.Flags.crossAppPoliteNotifications()) {
            com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy appStrategy = new com.android.server.notification.NotificationAttentionHelper.StrategyPerApp(this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_T1), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_T2), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_VOLUME1), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_VOLUME2), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_COUNTER_RESET), new com.android.server.notification.NotificationAttentionHelper.ExemptionProvider() { // from class: com.android.server.notification.NotificationAttentionHelper$$ExternalSyntheticLambda0
                @Override // com.android.server.notification.NotificationAttentionHelper.ExemptionProvider
                public final boolean isExempted(com.android.server.notification.NotificationRecord notificationRecord) {
                    return this.f$0.lambda$createPolitenessStrategy$0(notificationRecord);
                }
            });
            return new com.android.server.notification.NotificationAttentionHelper.StrategyAvalanche(this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_T1), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_T2), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_VOLUME1), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_VOLUME2), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_AVALANCHE_TIMEOUT), appStrategy, appStrategy.mExemptionProvider);
        }
        return new com.android.server.notification.NotificationAttentionHelper.StrategyPerApp(this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_T1), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_T2), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_VOLUME1), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_VOLUME2), this.mFlagResolver.getIntValue(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.NOTIF_COOLDOWN_COUNTER_RESET), new com.android.server.notification.NotificationAttentionHelper.ExemptionProvider() { // from class: com.android.server.notification.NotificationAttentionHelper$$ExternalSyntheticLambda1
            @Override // com.android.server.notification.NotificationAttentionHelper.ExemptionProvider
            public final boolean isExempted(com.android.server.notification.NotificationRecord notificationRecord) {
                return this.f$0.lambda$createPolitenessStrategy$1(notificationRecord);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPolitenessStrategy$0(com.android.server.notification.NotificationRecord record) {
        return this.mPackageManager.checkPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", record.getSbn().getPackageName()) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPolitenessStrategy$1(com.android.server.notification.NotificationRecord record) {
        return this.mPackageManager.checkPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", record.getSbn().getPackageName()) == 0;
    }

    com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy getPolitenessStrategy() {
        return this.mStrategy;
    }

    public void onSystemReady() {
        this.mSystemReady = true;
        this.mIsAutomotive = this.mPackageManager.hasSystemFeature("android.hardware.type.automotive", 0);
        this.mNotificationEffectsEnabledForAutomotive = this.mContext.getResources().getBoolean(android.R.bool.config_enableNightMode);
        this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
        this.mKeyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
        registerBroadcastListeners();
    }

    private void registerBroadcastListeners() {
        if (this.mPackageManager.hasSystemFeature("android.hardware.telephony")) {
            this.mTelephonyManager.listen(new android.telephony.PhoneStateListener() { // from class: com.android.server.notification.NotificationAttentionHelper.1
                @Override // android.telephony.PhoneStateListener
                public void onCallStateChanged(int state, java.lang.String incomingNumber) {
                    if (com.android.server.notification.NotificationAttentionHelper.this.mCallState == state) {
                        return;
                    }
                    if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                        android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "Call state changed: " + com.android.server.notification.NotificationAttentionHelper.callStateToString(state));
                    }
                    com.android.server.notification.NotificationAttentionHelper.this.mCallState = state;
                }
            }, 32);
        }
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.intent.action.USER_ADDED");
        filter.addAction("android.intent.action.USER_REMOVED");
        filter.addAction("android.intent.action.USER_SWITCHED");
        filter.addAction("android.intent.action.USER_UNLOCKED");
        filter.addAction("android.intent.action.TIME_SET");
        if (com.android.server.notification.Flags.crossAppPoliteNotifications()) {
            for (java.lang.String avalancheIntent : NOTIFICATION_AVALANCHE_TRIGGER_INTENTS) {
                filter.addAction(avalancheIntent);
            }
        }
        this.mContext.registerReceiverAsUser(this.mIntentReceiver, android.os.UserHandle.ALL, filter, null, null);
        android.content.IntentFilter lightsTimeoutFilter = new android.content.IntentFilter(ACTION_NOTIFICATION_LIGHTS_TIMEOUT);
        lightsTimeoutFilter.addAction(ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT);
        this.mContext.registerReceiver(this.mNotificationLightsOffReceiver, lightsTimeoutFilter);
        this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_LIGHT_PULSE_URI, false, this.mSettingsObserver, -1);
        if (com.android.server.notification.Flags.politeNotifications()) {
            this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_COOLDOWN_ENABLED_URI, false, this.mSettingsObserver, -1);
            this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_COOLDOWN_ALL_URI, false, this.mSettingsObserver, -1);
            this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_COOLDOWN_VIBRATE_UNLOCKED_URI, false, this.mSettingsObserver, -1);
        }
        this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT, false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_SETTINGS_SPECIAL_LIGHTS, false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME, false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE, false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(com.android.server.notification.NotificationAttentionHelper.SettingsObserver.NOTIFICATION_VIBRATION_INTENSITY, false, this.mSettingsObserver, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadUserSettings() {
        boolean pulseEnabled = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "notification_light_pulse", 0, -2) != 0;
        if (this.mNotificationPulseEnabled != pulseEnabled) {
            this.mNotificationPulseEnabled = pulseEnabled;
            updateLightsLocked();
        }
        if (com.android.server.notification.Flags.politeNotifications()) {
            try {
                this.mCurrentWorkProfileId = getManagedProfileId(android.app.ActivityManager.getCurrentUser());
                this.mNotificationCooldownEnabled = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "notification_cooldown_enabled", 1, -2) != 0;
                if (this.mCurrentWorkProfileId != -10000) {
                    this.mNotificationCooldownForWorkEnabled = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "notification_cooldown_enabled", 1, this.mCurrentWorkProfileId) != 0;
                } else {
                    this.mNotificationCooldownForWorkEnabled = false;
                }
                this.mNotificationCooldownApplyToAll = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "notification_cooldown_all", 1, -2) != 0;
                this.mStrategy.setApplyCooldownPerPackage(this.mNotificationCooldownApplyToAll);
                if (com.android.server.notification.Flags.vibrateWhileUnlocked()) {
                    this.mNotificationCooldownVibrateUnlocked = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "notification_cooldown_vibrate_unlocked", 0, -2) != 0;
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Failed to read Settings: " + e);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x03f8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x046d  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x04a3  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x04ec A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:241:0x04f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0500 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0523  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0529  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0530 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0534  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x0571  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0577  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x059a  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x05d1  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x05d4  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x05d6  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x05db  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x05e6  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0610  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x062f  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x018f  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0191  */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v11 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v2, types: [int] */
    /* JADX WARN: Type inference failed for: r5v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int buzzBeepBlinkLocked(com.android.server.notification.NotificationRecord r35, com.android.server.notification.NotificationAttentionHelper.Signals r36) {
        /*
            Method dump skipped, instruction units count: 1595
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationAttentionHelper.buzzBeepBlinkLocked(com.android.server.notification.NotificationRecord, com.android.server.notification.NotificationAttentionHelper$Signals):int");
    }

    private int getPoliteBit(com.android.server.notification.NotificationRecord record) {
        switch (getPolitenessState(record)) {
            case 1:
                return 8;
            case 2:
                return 16;
            default:
                return 0;
        }
    }

    private int getPolitenessState(com.android.server.notification.NotificationRecord record) {
        if (!isPoliteNotificationFeatureEnabled(record)) {
            return 0;
        }
        return this.mStrategy.getPolitenessState(record);
    }

    boolean isInsistentUpdate(com.android.server.notification.NotificationRecord record) {
        return (java.util.Objects.equals(record.getKey(), this.mSoundNotificationKey) || java.util.Objects.equals(record.getKey(), this.mVibrateNotificationKey)) && isCurrentlyInsistent();
    }

    boolean isCurrentlyInsistent() {
        return isLoopingRingtoneNotification(this.mNMP.getNotificationByKey(this.mSoundNotificationKey)) || isLoopingRingtoneNotification(this.mNMP.getNotificationByKey(this.mVibrateNotificationKey));
    }

    boolean shouldMuteNotificationLocked(com.android.server.notification.NotificationRecord record, com.android.server.notification.NotificationAttentionHelper.Signals signals) {
        android.app.Notification notification = record.getNotification();
        if ((record.isUpdate && (notification.flags & 8) != 0) || record.shouldPostSilently()) {
            return true;
        }
        java.lang.String disableEffects = disableNotificationEffects(record, signals.listenerHints);
        if (disableEffects != null) {
            if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                android.util.Slog.v(TAG, "buzzBeepBlinkLocked--shouldMuteNotificationLocked disableEffects:" + disableEffects);
            }
            com.android.server.notification.ZenLog.traceDisableEffects(record, disableEffects);
            return true;
        }
        if (record.isIntercepted()) {
            if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                android.util.Slog.v(TAG, "buzzBeepBlinkLocked--shouldMuteNotificationLocked suppressed due to DND");
            }
            return true;
        }
        if (record.getSbn().isGroup() && notification.suppressAlertingDueToGrouping()) {
            return true;
        }
        java.lang.String pkg = record.getSbn().getPackageName();
        if (this.mUsageStats.isAlertRateLimited(pkg)) {
            android.util.Slog.e(TAG, "Muting recently noisy " + record.getKey());
            return true;
        }
        if (isCurrentlyInsistent() && !isInsistentUpdate(record)) {
            return true;
        }
        boolean isBubbleOrOverflowed = record.canBubble() && (record.isFlagBubbleRemoved() || record.getNotification().isBubbleNotification());
        return record.isUpdate && !record.isInterruptive() && isBubbleOrOverflowed && record.getNotification().getBubbleMetadata() != null && record.getNotification().getBubbleMetadata().isNotificationSuppressed();
    }

    private boolean isLoopingRingtoneNotification(com.android.server.notification.NotificationRecord playingRecord) {
        if (playingRecord != null && playingRecord.getAudioAttributes().getUsage() == 6 && (playingRecord.getNotification().flags & 4) != 0) {
            return true;
        }
        return false;
    }

    boolean playSound(com.android.server.notification.NotificationRecord record, android.net.Uri soundUri) {
        boolean shouldPlay;
        if (android.media.audio.Flags.focusExclusiveWithRecording()) {
            shouldPlay = this.mAudioManager.shouldNotificationSoundPlay(record.getAudioAttributes()) || (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().shouldPlayForVibrationRing(this.mAudioManager));
        } else {
            shouldPlay = !this.mAudioManager.isAudioFocusExclusive() && (this.mAudioManager.getStreamVolume(android.media.AudioAttributes.toLegacyStreamType(record.getAudioAttributes())) != 0 || (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().shouldPlayForVibrationRing(this.mAudioManager)));
        }
        if (!shouldPlay) {
            android.util.Slog.d(TAG, "Not playing sound " + soundUri + " due to focus/volume");
            return false;
        }
        boolean looping = (record.getNotification().flags & 4) != 0;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.media.IRingtonePlayer player = this.mAudioManager.getRingtonePlayer();
                if (player != null) {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Playing sound " + soundUri + " with attributes " + record.getAudioAttributes());
                    }
                    if (this.mNMSWrapper.getNMSExt() != null && !this.mNMSWrapper.getNMSExt().playAsync(player, record, soundUri, looping)) {
                        player.playAsync(soundUri, record.getSbn().getUser(), looping, record.getAudioAttributes(), getSoundVolume(record));
                    }
                    android.util.Slog.v(TAG, "Ringtone player is null.");
                    return true;
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Failed playSound: " + e);
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private boolean isPoliteNotificationFeatureEnabled(com.android.server.notification.NotificationRecord record) {
        if (!com.android.server.notification.Flags.politeNotifications() || !this.mNotificationCooldownEnabled) {
            return false;
        }
        if (!isNotificationForWorkProfile(record) || this.mNotificationCooldownForWorkEnabled) {
            return this.mNotificationCooldownApplyToAll || record.isConversation();
        }
        return false;
    }

    private float getSoundVolume(com.android.server.notification.NotificationRecord record) {
        if (!isPoliteNotificationFeatureEnabled(record)) {
            return 1.0f;
        }
        return this.mStrategy.getSoundVolume(record);
    }

    private float getVibrationIntensity(com.android.server.notification.NotificationRecord record) {
        if (!isPoliteNotificationFeatureEnabled(record)) {
            return 1.0f;
        }
        return this.mStrategy.getVibrationIntensity(record);
    }

    boolean playVibration(final com.android.server.notification.NotificationRecord record, android.os.VibrationEffect effect, boolean delayVibForSound) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            float scale = getVibrationIntensity(record);
            final android.os.VibrationEffect scaledEffect = java.lang.Float.compare(scale, 1.0f) != 0 ? this.mVibratorHelper.scale(effect, scale) : effect;
            if (delayVibForSound) {
                new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationAttentionHelper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$playVibration$2(record, scaledEffect);
                    }
                }).start();
            } else {
                vibrate(record, scaledEffect, false);
            }
            android.os.Binder.restoreCallingIdentity(identity);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playVibration$2(com.android.server.notification.NotificationRecord record, android.os.VibrationEffect scaledEffect) {
        int waitMs = this.mAudioManager.getFocusRampTimeMs(3, record.getAudioAttributes());
        if (DEBUG) {
            android.util.Slog.v(TAG, "Delaying vibration for notification " + record.getKey() + " by " + waitMs + "ms");
        }
        try {
            java.lang.Thread.sleep(waitMs);
        } catch (java.lang.InterruptedException e) {
        }
        if (this.mNMP.getNotificationByKey(record.getKey()) != null) {
            if (record.getKey().equals(this.mVibrateNotificationKey)) {
                vibrate(record, scaledEffect, true);
                return;
            } else {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "No vibration for notification " + record.getKey() + ": a new notification is vibrating, or effects were cleared while waiting");
                    return;
                }
                return;
            }
        }
        android.util.Slog.w(TAG, "No vibration for canceled notification " + record.getKey());
    }

    private void vibrate(com.android.server.notification.NotificationRecord record, android.os.VibrationEffect effect, boolean delayed) {
        java.lang.String reason = "Notification (" + record.getSbn().getOpPkg() + " " + record.getSbn().getUid() + ") " + (delayed ? "(Delayed)" : "");
        this.mVibratorHelper.vibrate(effect, record.getAudioAttributes(), reason);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.server.notification.NotificationAttentionHelper$2] */
    void playInCallNotification() {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        if (this.mAudioManager.getRingerModeInternal() == 2 && android.provider.Settings.Secure.getIntForUser(cr, "in_call_notification_enabled", 1, cr.getUserId()) != 0) {
            new java.lang.Thread() { // from class: com.android.server.notification.NotificationAttentionHelper.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    long identity = android.os.Binder.clearCallingIdentity();
                    try {
                        try {
                            android.media.IRingtonePlayer player = com.android.server.notification.NotificationAttentionHelper.this.mAudioManager.getRingtonePlayer();
                            if (player != null) {
                                if (com.android.server.notification.NotificationAttentionHelper.this.mCallNotificationToken != null) {
                                    if (com.android.server.notification.NotificationAttentionHelper.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationAttentionHelper.this.mNMSWrapper.getNMSExt().isLoggable()) {
                                        android.util.Slog.v(com.android.server.notification.NotificationAttentionHelper.TAG, "stop player to play in call notification sound.");
                                    }
                                    player.stop(com.android.server.notification.NotificationAttentionHelper.this.mCallNotificationToken);
                                }
                                com.android.server.notification.NotificationAttentionHelper.this.mCallNotificationToken = new android.os.Binder();
                                player.play(com.android.server.notification.NotificationAttentionHelper.this.mCallNotificationToken, com.android.server.notification.NotificationAttentionHelper.this.mInCallNotificationUri, com.android.server.notification.NotificationAttentionHelper.this.mInCallNotificationAudioAttributes, com.android.server.notification.NotificationAttentionHelper.this.mInCallNotificationVolume, false);
                            }
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(com.android.server.notification.NotificationAttentionHelper.TAG, "Failed playInCallNotification: " + e);
                        }
                    } finally {
                        android.os.Binder.restoreCallingIdentity(identity);
                    }
                }
            }.start();
        }
    }

    void clearSoundLocked() {
        this.mSoundNotificationKey = null;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.media.IRingtonePlayer player = this.mAudioManager.getRingtonePlayer();
                if (player != null) {
                    player.stopAsync();
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Failed clearSoundLocked: " + e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    void clearVibrateLocked() {
        this.mVibrateNotificationKey = null;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mVibratorHelper.cancelVibration();
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    void clearLightsLocked() {
        this.mLights.clear();
        updateLightsLocked();
    }

    public void clearEffectsLocked(java.lang.String key, boolean shouldForcePlayRington) {
        if (key.equals(this.mSoundNotificationKey)) {
            if (shouldForcePlayRington) {
                android.util.Slog.w(TAG, "We should not stop red envelope ring here");
            } else {
                clearSoundLocked();
            }
        }
        if (key.equals(this.mVibrateNotificationKey)) {
            clearVibrateLocked();
        }
        boolean removed = this.mLights.remove(key);
        if (removed) {
            updateLightsLocked();
        }
    }

    public void clearAttentionEffects() {
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
            android.util.Slog.v(TAG, "clearEffects by notification delegate.");
        }
        clearSoundLocked();
        clearVibrateLocked();
    }

    void updateLightsLocked() {
        if (this.mNotificationLight == null) {
            return;
        }
        com.android.server.notification.NotificationRecord ledNotification = null;
        while (ledNotification == null && !this.mLights.isEmpty()) {
            java.lang.String owner = this.mLights.get(this.mLights.size() - 1);
            ledNotification = this.mNMP.getNotificationByKey(owner);
            if (ledNotification == null) {
                android.util.Slog.wtfStack(TAG, "LED Notification does not exist: " + owner);
                this.mLights.remove(owner);
            }
        }
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().updateLightsStateLocked(ledNotification)) {
            return;
        }
        if (ledNotification == null || isInCall() || this.mScreenOn) {
            this.mNotificationLight.turnOff();
            return;
        }
        com.android.server.notification.NotificationRecord.Light light = ledNotification.getLight();
        if (light != null && this.mNotificationPulseEnabled) {
            this.mNotificationLight.setFlashing(light.color, 1, light.onMs, light.offMs);
        }
    }

    boolean canShowLightsLocked(com.android.server.notification.NotificationRecord record, com.android.server.notification.NotificationAttentionHelper.Signals signals, boolean aboveThreshold) {
        if (!this.mSystemReady) {
            return false;
        }
        if (this.mNMSWrapper.getNMSExt() != null && !this.mNMSWrapper.getNMSExt().hasCustomizeBreathLight()) {
            return false;
        }
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().canShowLightsLocked(record)) {
            if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                android.util.Slog.d(TAG, "mNMSExt#canShowLightsLocked returns true.");
            }
            return true;
        }
        if (!this.mHasLight || !this.mNotificationPulseEnabled || record.getLight() == null || !aboveThreshold || (record.getSuppressedVisualEffects() & 8) != 0) {
            return false;
        }
        android.app.Notification notification = record.getNotification();
        if (!record.isUpdate || (notification.flags & 8) == 0) {
            return ((record.getSbn().isGroup() && record.getNotification().suppressAlertingDueToGrouping()) || isInCall() || !isNotificationForCurrentUser(record, signals)) ? false : true;
        }
        return false;
    }

    private java.lang.String disableNotificationEffects(com.android.server.notification.NotificationRecord record, int listenerHints) {
        if (this.mDisableNotificationEffects) {
            return "booleanState";
        }
        if ((listenerHints & 1) != 0) {
            return "listenerHints";
        }
        if (record != null && record.getAudioAttributes() != null) {
            if ((listenerHints & 2) != 0 && record.getAudioAttributes().getUsage() != 6) {
                return "listenerNoti";
            }
            if ((listenerHints & 4) != 0 && record.getAudioAttributes().getUsage() == 6) {
                return "listenerCall";
            }
        }
        if (this.mCallState != 0 && !this.mZenModeHelper.isCall(record)) {
            return "callState";
        }
        return null;
    }

    public void updateDisableNotificationEffectsLocked(int status) {
        this.mDisableNotificationEffects = (262144 & status) != 0;
        if (this.mDisableNotificationEffects) {
            if (this.mNMSWrapper != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                android.util.Slog.v(TAG, "clearsound due to set disable by notification delegate.");
            }
            clearAttentionEffects();
        }
    }

    boolean isInCall() {
        int audioMode;
        return this.mInCallStateOffHook || (audioMode = this.mAudioManager.getMode()) == 2 || audioMode == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String callStateToString(int state) {
        switch (state) {
            case 0:
                return "CALL_STATE_IDLE";
            case 1:
                return "CALL_STATE_RINGING";
            case 2:
                return "CALL_STATE_OFFHOOK";
            default:
                return "CALL_STATE_UNKNOWN_" + state;
        }
    }

    boolean isNotificationForCurrentUser(com.android.server.notification.NotificationRecord record, com.android.server.notification.NotificationAttentionHelper.Signals signals) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int currentUser = android.app.ActivityManager.getCurrentUser();
            android.os.Binder.restoreCallingIdentity(token);
            return (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isNotificationForCurrentUser(record, record.getUserId())) || record.getUserId() == -1 || record.getUserId() == currentUser || signals.isCurrentProfile;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private boolean isNotificationForWorkProfile(com.android.server.notification.NotificationRecord record) {
        return record.getUser().getIdentifier() == this.mCurrentWorkProfileId && this.mCurrentWorkProfileId != -10000;
    }

    private int getManagedProfileId(int parentUserId) {
        java.util.List<android.content.pm.UserInfo> profiles = this.mUm.getProfiles(parentUserId);
        for (android.content.pm.UserInfo profile : profiles) {
            if (profile.isManagedProfile() && profile.getUserHandle().getIdentifier() != parentUserId) {
                return profile.getUserHandle().getIdentifier();
            }
        }
        return -10000;
    }

    void sendAccessibilityEvent(com.android.server.notification.NotificationRecord record) {
        if (!this.mAccessibilityManager.isEnabled()) {
            return;
        }
        android.app.Notification notification = record.getNotification();
        java.lang.CharSequence packageName = record.getSbn().getPackageName();
        android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(64);
        event.setPackageName(packageName);
        event.setClassName(android.app.Notification.class.getName());
        int visibilityOverride = record.getPackageVisibilityOverride();
        int notifVisibility = visibilityOverride == -1000 ? notification.visibility : visibilityOverride;
        int userId = record.getUser().getIdentifier();
        boolean needPublic = userId >= 0 && this.mKeyguardManager.isDeviceLocked(userId);
        if (needPublic && notifVisibility != 1) {
            event.setParcelableData(notification.publicVersion);
        } else {
            event.setParcelableData(notification);
        }
        java.lang.CharSequence tickerText = notification.tickerText;
        if (!android.text.TextUtils.isEmpty(tickerText)) {
            event.getText().add(tickerText);
        }
        this.mAccessibilityManager.sendAccessibilityEvent(event);
    }

    public void onUserInteraction(com.android.server.notification.NotificationRecord record) {
        if (isPoliteNotificationFeatureEnabled(record)) {
            this.mStrategy.onUserInteraction(record);
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        pw.println("\n  Notification attention state:");
        pw.print(prefix);
        pw.println("  mSoundNotificationKey=" + this.mSoundNotificationKey);
        pw.print(prefix);
        pw.println("  mVibrateNotificationKey=" + this.mVibrateNotificationKey);
        pw.print(prefix);
        pw.println("  mDisableNotificationEffects=" + this.mDisableNotificationEffects);
        pw.print(prefix);
        pw.println("  mCallState=" + callStateToString(this.mCallState));
        pw.print(prefix);
        pw.println("  mSystemReady=" + this.mSystemReady);
        pw.print(prefix);
        pw.println("  mNotificationPulseEnabled=" + this.mNotificationPulseEnabled);
        if (DEBUG) {
            pw.println("\n  mCustomizeBreathLight: " + mCustomizeBreathLight);
            pw.println("\n  mSettingsSpecialLights: " + this.mSettingsSpecialLights);
            pw.println("\n  mBreathAllowPeriod: " + this.mBreathAllowPeriod);
            pw.println("\n  mScreenLocked: " + this.mScreenLocked);
            pw.println("\n  mIsFlashing: " + this.mIsFlashing);
            pw.println();
        }
        int N = this.mLights.size();
        if (N > 0) {
            pw.print(prefix);
            pw.println("  Lights List:");
            for (int i = 0; i < N; i++) {
                if (i == N - 1) {
                    pw.print("  > ");
                } else {
                    pw.print("    ");
                }
                pw.println(this.mLights.get(i));
            }
            pw.println("  ");
        }
    }

    public static class Signals {
        private final boolean isCurrentProfile;
        private final int listenerHints;

        public Signals(boolean isCurrentProfile, int listenerHints) {
            this.isCurrentProfile = isCurrentProfile;
            this.listenerHints = listenerHints;
        }
    }

    static abstract class PolitenessStrategy {
        static final int POLITE_STATE_DEFAULT = 0;
        static final int POLITE_STATE_MUTED = 2;
        static final int POLITE_STATE_POLITE = 1;
        protected boolean mApplyPerPackage;
        protected final com.android.server.notification.NotificationAttentionHelper.ExemptionProvider mExemptionProvider;
        protected final int mTimeoutMuted;
        protected final int mTimeoutPolite;
        protected final float mVolumeMuted;
        protected final float mVolumePolite;
        protected boolean mIsActive = true;
        protected final java.util.Map<java.lang.String, java.lang.Integer> mVolumeStates = new java.util.HashMap();
        protected final java.util.Map<java.lang.String, java.lang.Long> mLastUpdatedTimestampByPackage = new java.util.HashMap();

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface PolitenessState {
        }

        abstract void onNotificationPosted(com.android.server.notification.NotificationRecord notificationRecord);

        public PolitenessStrategy(int timeoutPolite, int timeoutMuted, int volumePolite, int volumeMuted, com.android.server.notification.NotificationAttentionHelper.ExemptionProvider exemptionProvider) {
            this.mTimeoutPolite = timeoutPolite;
            this.mTimeoutMuted = timeoutMuted;
            this.mVolumePolite = volumePolite / 100.0f;
            this.mVolumeMuted = volumeMuted / 100.0f;
            this.mExemptionProvider = exemptionProvider;
        }

        void setApplyCooldownPerPackage(boolean applyPerPackage) {
            this.mApplyPerPackage = applyPerPackage;
        }

        boolean shouldIgnoreNotification(com.android.server.notification.NotificationRecord record) {
            return record.getSbn().isGroup() && record.getSbn().getNotification().isGroupSummary();
        }

        java.lang.String getChannelKey(com.android.server.notification.NotificationRecord record) {
            java.lang.String channelId = record.getChannel().getConversationId() != null ? record.getChannel().getConversationId() : record.getChannel().getId();
            if (this.mApplyPerPackage && !record.getChannel().hasUserSetSound()) {
                channelId = "";
            }
            return record.getSbn().getNormalizedUserId() + ":" + record.getSbn().getPackageName() + ":" + channelId;
        }

        public float getSoundVolume(com.android.server.notification.NotificationRecord record) {
            float volume = 1.0f;
            java.lang.String key = getChannelKey(record);
            int volState = getPolitenessState(record);
            switch (volState) {
                case 0:
                    volume = 1.0f;
                    break;
                case 1:
                    volume = this.mVolumePolite;
                    break;
                case 2:
                    volume = this.mVolumeMuted;
                    break;
                default:
                    android.util.Log.w(com.android.server.notification.NotificationAttentionHelper.TAG, "getSoundVolume unexpected volume state: " + volState);
                    break;
            }
            if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "getSoundVolume state: " + volState + " vol: " + volume + " key: " + key);
            }
            return volume;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float getVibrationIntensity(com.android.server.notification.NotificationRecord record) {
            return getSoundVolume(record);
        }

        public void onUserInteraction(com.android.server.notification.NotificationRecord record) {
            java.lang.String key = getChannelKey(record);
            this.mVolumeStates.put(key, 0);
            setLastNotificationUpdateTimeMs(record, 0L);
        }

        public int getPolitenessState(com.android.server.notification.NotificationRecord record) {
            return this.mVolumeStates.getOrDefault(getChannelKey(record), 0).intValue();
        }

        void setLastNotificationUpdateTimeMs(com.android.server.notification.NotificationRecord record, long timestampMillis) {
            record.getChannel().setLastNotificationUpdateTimeMs(timestampMillis);
            this.mLastUpdatedTimestampByPackage.put(record.getSbn().getPackageName(), java.lang.Long.valueOf(timestampMillis));
        }

        long getLastNotificationUpdateTimeMs(com.android.server.notification.NotificationRecord record) {
            if (record.getChannel().hasUserSetSound() || !this.mApplyPerPackage) {
                return record.getChannel().getLastNotificationUpdateTimeMs();
            }
            return this.mLastUpdatedTimestampByPackage.getOrDefault(record.getSbn().getPackageName(), 0L).longValue();
        }

        int getNextState(int currState, long timeSinceLastNotif) {
            switch (currState) {
                case 0:
                    if (timeSinceLastNotif < this.mTimeoutPolite) {
                    }
                    break;
                case 1:
                    if (timeSinceLastNotif >= this.mTimeoutMuted) {
                        if (timeSinceLastNotif > this.mTimeoutPolite) {
                        }
                    }
                    break;
                case 2:
                    if (timeSinceLastNotif > this.mTimeoutMuted) {
                    }
                    break;
                default:
                    android.util.Log.w(com.android.server.notification.NotificationAttentionHelper.TAG, "getNextState unexpected volume state: " + currState);
                    break;
            }
            return currState;
        }

        boolean isActive() {
            return this.mIsActive;
        }
    }

    private static class StrategyPerApp extends com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy {
        private final int mMaxPostedForReset;
        private final java.util.Map<java.lang.String, java.lang.Integer> mNumPosted;

        public StrategyPerApp(int timeoutPolite, int timeoutMuted, int volumePolite, int volumeMuted, int maxPosted, com.android.server.notification.NotificationAttentionHelper.ExemptionProvider exemptionProvider) {
            super(timeoutPolite, timeoutMuted, volumePolite, volumeMuted, exemptionProvider);
            this.mNumPosted = new java.util.HashMap();
            this.mMaxPostedForReset = maxPosted;
            if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "StrategyPerApp: " + timeoutPolite + " " + timeoutMuted);
            }
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        public void onNotificationPosted(com.android.server.notification.NotificationRecord record) {
            int nextState;
            if (shouldIgnoreNotification(record)) {
                return;
            }
            long timeSinceLastNotif = java.lang.System.currentTimeMillis() - getLastNotificationUpdateTimeMs(record);
            java.lang.String key = getChannelKey(record);
            int currState = getPolitenessState(record);
            if (com.android.server.notification.Flags.politeNotificationsAttnUpdate()) {
                nextState = getNextState(currState, timeSinceLastNotif, record);
            } else {
                nextState = getNextState(currState, timeSinceLastNotif);
            }
            int numPosted = this.mNumPosted.getOrDefault(key, 0).intValue() + 1;
            this.mNumPosted.put(key, java.lang.Integer.valueOf(numPosted));
            if (currState == 2 && numPosted >= this.mMaxPostedForReset) {
                nextState = 0;
                this.mNumPosted.put(key, 0);
            }
            if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "onNotificationPosted time delta: " + timeSinceLastNotif + " vol state: " + nextState + " key: " + key + " numposted " + numPosted);
            }
            this.mVolumeStates.put(key, java.lang.Integer.valueOf(nextState));
        }

        int getNextState(int currState, long timeSinceLastNotif, com.android.server.notification.NotificationRecord record) {
            if (this.mExemptionProvider.isExempted(record)) {
                return 0;
            }
            return getNextState(currState, timeSinceLastNotif);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        public void onUserInteraction(com.android.server.notification.NotificationRecord record) {
            super.onUserInteraction(record);
            this.mNumPosted.put(getChannelKey(record), 0);
        }
    }

    private static class StrategyAvalanche extends com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy {
        private static final java.lang.String COMMON_KEY = "cross_app_common_key";
        private final com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy mAppStrategy;
        private long mLastAvalancheTriggerTimestamp;
        private long mLastNotificationTimestamp;
        private final int mTimeoutAvalanche;

        StrategyAvalanche(int timeoutPolite, int timeoutMuted, int volumePolite, int volumeMuted, int timeoutAvalanche, com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy appStrategy, com.android.server.notification.NotificationAttentionHelper.ExemptionProvider exemptionProvider) {
            super(timeoutPolite, timeoutMuted, volumePolite, volumeMuted, exemptionProvider);
            this.mLastNotificationTimestamp = 0L;
            this.mLastAvalancheTriggerTimestamp = 0L;
            this.mTimeoutAvalanche = timeoutAvalanche;
            this.mAppStrategy = appStrategy;
            if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "StrategyAvalanche: " + timeoutPolite + " " + timeoutMuted + " " + timeoutAvalanche);
            }
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        void onNotificationPosted(com.android.server.notification.NotificationRecord record) {
            int nextState;
            if (isAvalancheActive()) {
                if (shouldIgnoreNotification(record)) {
                    return;
                }
                long timeSinceLastNotif = java.lang.System.currentTimeMillis() - getLastNotificationUpdateTimeMs(record);
                java.lang.String key = getChannelKey(record);
                int currState = getPolitenessState(record);
                if (com.android.server.notification.Flags.politeNotificationsAttnUpdate()) {
                    nextState = getNextState(currState, timeSinceLastNotif, record);
                } else {
                    nextState = getNextState(currState, timeSinceLastNotif);
                }
                if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                    android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "StrategyAvalanche onNotificationPosted time delta: " + timeSinceLastNotif + " vol state: " + nextState + " key: " + key);
                }
                this.mVolumeStates.put(key, java.lang.Integer.valueOf(nextState));
            }
            this.mAppStrategy.onNotificationPosted(record);
        }

        int getNextState(int currState, long timeSinceLastNotif, com.android.server.notification.NotificationRecord record) {
            if (!isAvalancheExempted(record)) {
                return 2;
            }
            if (isAvalancheExemptedFullVolume(record)) {
                return 0;
            }
            return getNextState(currState, timeSinceLastNotif);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        public int getPolitenessState(com.android.server.notification.NotificationRecord record) {
            if (isAvalancheActive()) {
                return super.getPolitenessState(record);
            }
            return this.mAppStrategy.getPolitenessState(record);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        public float getSoundVolume(com.android.server.notification.NotificationRecord record) {
            if (isAvalancheActive()) {
                return super.getSoundVolume(record);
            }
            return this.mAppStrategy.getSoundVolume(record);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        public void onUserInteraction(com.android.server.notification.NotificationRecord record) {
            super.onUserInteraction(record);
            this.mAppStrategy.onUserInteraction(record);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        java.lang.String getChannelKey(com.android.server.notification.NotificationRecord record) {
            if (isAvalancheActive()) {
                if (com.android.server.notification.Flags.politeNotificationsAttnUpdate()) {
                    if (isAvalancheExempted(record)) {
                        return super.getChannelKey(record);
                    }
                    return record.getSbn().getNormalizedUserId() + ":" + COMMON_KEY;
                }
                if (record.getChannel().hasUserSetSound()) {
                    return super.getChannelKey(record);
                }
                return record.getSbn().getNormalizedUserId() + ":" + COMMON_KEY;
            }
            return this.mAppStrategy.getChannelKey(record);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        public void setLastNotificationUpdateTimeMs(com.android.server.notification.NotificationRecord record, long timestampMillis) {
            super.setLastNotificationUpdateTimeMs(record, timestampMillis);
            this.mLastNotificationTimestamp = timestampMillis;
            this.mAppStrategy.setLastNotificationUpdateTimeMs(record, timestampMillis);
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        long getLastNotificationUpdateTimeMs(com.android.server.notification.NotificationRecord record) {
            if (com.android.server.notification.Flags.politeNotificationsAttnUpdate()) {
                if (isAvalancheExempted(record)) {
                    return super.getLastNotificationUpdateTimeMs(record);
                }
                return this.mLastNotificationTimestamp;
            }
            if (record.getChannel().hasUserSetSound()) {
                return super.getLastNotificationUpdateTimeMs(record);
            }
            return this.mLastNotificationTimestamp;
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        void setApplyCooldownPerPackage(boolean applyPerPackage) {
            super.setApplyCooldownPerPackage(applyPerPackage);
            this.mAppStrategy.setApplyCooldownPerPackage(applyPerPackage);
        }

        boolean isAvalancheActive() {
            this.mIsActive = java.lang.System.currentTimeMillis() - this.mLastAvalancheTriggerTimestamp < ((long) this.mTimeoutAvalanche);
            if (com.android.server.notification.NotificationAttentionHelper.DEBUG) {
                android.util.Log.i(com.android.server.notification.NotificationAttentionHelper.TAG, "StrategyAvalanche: active " + this.mIsActive);
            }
            return this.mIsActive;
        }

        @Override // com.android.server.notification.NotificationAttentionHelper.PolitenessStrategy
        boolean isActive() {
            return isAvalancheActive();
        }

        void setTriggerTimeMs(long timestamp) {
            this.mLastAvalancheTriggerTimestamp = timestamp;
        }

        private boolean isAvalancheExemptedFullVolume(com.android.server.notification.NotificationRecord record) {
            if ((record.isConversation() && record.getChannel().isImportantConversation()) || record.getNotification().isStyle(android.app.Notification.CallStyle.class)) {
                return true;
            }
            java.lang.String category = record.getNotification().category;
            if ("reminder".equals(category) || "event".equals(category)) {
                return true;
            }
            return this.mExemptionProvider.isExempted(record);
        }

        private boolean isAvalancheExempted(com.android.server.notification.NotificationRecord record) {
            if (isAvalancheExemptedFullVolume(record)) {
                return true;
            }
            return (record.isConversation() && record.getNotification().getWhen() > this.mLastAvalancheTriggerTimestamp) || record.getNotification().fullScreenIntent != null || record.getNotification().isColorized();
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        private static final android.net.Uri NOTIFICATION_LIGHT_PULSE_URI = android.provider.Settings.System.getUriFor("notification_light_pulse");
        private static final android.net.Uri NOTIFICATION_COOLDOWN_ENABLED_URI = android.provider.Settings.System.getUriFor("notification_cooldown_enabled");
        private static final android.net.Uri NOTIFICATION_COOLDOWN_ALL_URI = android.provider.Settings.System.getUriFor("notification_cooldown_all");
        private static final android.net.Uri NOTIFICATION_COOLDOWN_VIBRATE_UNLOCKED_URI = android.provider.Settings.System.getUriFor("notification_cooldown_vibrate_unlocked");
        private static final android.net.Uri NOTIFICATION_CUSTOMIZE_BREATH_LIGHT = android.provider.Settings.Global.getUriFor("customize_breath_light_mms");
        private static final android.net.Uri NOTIFICATION_SETTINGS_SPECIAL_LIGHTS = android.provider.Settings.Secure.getUriFor("settings_special_lights");
        private static final android.net.Uri NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME = android.provider.Settings.Global.getUriFor("customize_breath_light_time");
        private static final android.net.Uri NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE = android.provider.Settings.Global.getUriFor("zen_mode");
        private static final java.lang.String SYSTEM_NOTIFICATION_VIBRATION_INTENSITY = "notification_stepless_vibration_intensity";
        private static final android.net.Uri NOTIFICATION_VIBRATION_INTENSITY = android.provider.Settings.System.getUriFor(SYSTEM_NOTIFICATION_VIBRATION_INTENSITY);

        public SettingsObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (NOTIFICATION_LIGHT_PULSE_URI.equals(uri)) {
                boolean pulseEnabled = android.provider.Settings.System.getIntForUser(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "notification_light_pulse", 0, -2) != 0;
                if (com.android.server.notification.NotificationAttentionHelper.this.mNotificationPulseEnabled != pulseEnabled) {
                    com.android.server.notification.NotificationAttentionHelper.this.mNotificationPulseEnabled = pulseEnabled;
                    com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
                }
            }
            if (NOTIFICATION_CUSTOMIZE_BREATH_LIGHT.equals(uri)) {
                com.android.server.notification.NotificationAttentionHelper.mCustomizeBreathLight = android.provider.Settings.Global.getInt(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "customize_breath_light_mms", 0) == 1;
                android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "BreathLight: " + com.android.server.notification.NotificationAttentionHelper.mCustomizeBreathLight);
                com.android.server.notification.NotificationAttentionHelper.this.scheduleLightsPeriod();
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            }
            if (NOTIFICATION_SETTINGS_SPECIAL_LIGHTS.equals(uri)) {
                com.android.server.notification.NotificationAttentionHelper.this.mSettingsSpecialLights = android.provider.Settings.Secure.getStringForUser(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "settings_special_lights", -2);
                android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "light list: " + com.android.server.notification.NotificationAttentionHelper.this.mSettingsSpecialLights);
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            }
            if (NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME.equals(uri)) {
                com.android.server.notification.NotificationAttentionHelper.this.mBreathAllowPeriod = android.provider.Settings.Global.getString(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "customize_breath_light_time");
                android.util.Slog.d(com.android.server.notification.NotificationAttentionHelper.TAG, "breath time: " + com.android.server.notification.NotificationAttentionHelper.this.mBreathAllowPeriod);
                com.android.server.notification.NotificationAttentionHelper.this.scheduleLightsPeriod();
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            }
            if (NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE.equals(uri)) {
                com.android.server.notification.NotificationAttentionHelper.this.updateLightsLocked();
            }
            if (com.android.server.notification.Flags.politeNotifications()) {
                if (NOTIFICATION_COOLDOWN_ENABLED_URI.equals(uri)) {
                    com.android.server.notification.NotificationAttentionHelper.this.mNotificationCooldownEnabled = android.provider.Settings.System.getIntForUser(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "notification_cooldown_enabled", 1, -2) != 0;
                    if (com.android.server.notification.NotificationAttentionHelper.this.mCurrentWorkProfileId != -10000) {
                        com.android.server.notification.NotificationAttentionHelper.this.mNotificationCooldownForWorkEnabled = android.provider.Settings.System.getIntForUser(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "notification_cooldown_enabled", 1, com.android.server.notification.NotificationAttentionHelper.this.mCurrentWorkProfileId) != 0;
                    } else {
                        com.android.server.notification.NotificationAttentionHelper.this.mNotificationCooldownForWorkEnabled = false;
                    }
                }
                if (NOTIFICATION_COOLDOWN_ALL_URI.equals(uri)) {
                    com.android.server.notification.NotificationAttentionHelper.this.mNotificationCooldownApplyToAll = android.provider.Settings.System.getIntForUser(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "notification_cooldown_all", 1, -2) != 0;
                    com.android.server.notification.NotificationAttentionHelper.this.mStrategy.setApplyCooldownPerPackage(com.android.server.notification.NotificationAttentionHelper.this.mNotificationCooldownApplyToAll);
                }
                if (com.android.server.notification.Flags.vibrateWhileUnlocked() && NOTIFICATION_COOLDOWN_VIBRATE_UNLOCKED_URI.equals(uri)) {
                    com.android.server.notification.NotificationAttentionHelper.this.mNotificationCooldownVibrateUnlocked = android.provider.Settings.System.getIntForUser(com.android.server.notification.NotificationAttentionHelper.this.mContext.getContentResolver(), "notification_cooldown_vibrate_unlocked", 0, -2) != 0;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleLightsPeriod() {
        long timeoutPeriod;
        long timeoutPeriod2 = java.lang.System.currentTimeMillis();
        long oneDayMin = java.util.concurrent.TimeUnit.DAYS.toMinutes(1L);
        int hour = 0;
        int min = 0;
        int sec = 0;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        if (calendar != null) {
            hour = calendar.get(11);
            min = calendar.get(12);
            sec = calendar.get(13);
        }
        long endSec = (((long) this.mEndHour) * 60 * 60) + (((long) this.mEndMinute) * 60);
        long nowSec = (((long) hour) * 60 * 60) + (((long) min) * 60) + ((long) sec);
        long diff = endSec - nowSec;
        if (diff > 0) {
            timeoutPeriod = timeoutPeriod2 + (1000 * diff);
        } else {
            timeoutPeriod = timeoutPeriod2 + (((60 * oneDayMin) + diff) * 1000);
        }
        android.util.Slog.d(TAG, "scheduleLightsPeriod " + mCustomizeBreathLight + ", timeoutPeriod: " + timeoutPeriod + ", now: " + java.lang.System.currentTimeMillis() + ", endSec: " + endSec + ", nowSec: " + nowSec + ", diff: " + diff);
        android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(this.mContext, 11, new android.content.Intent(ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).addFlags(268435456), 67108864);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        if (mCustomizeBreathLight) {
            alarmManager.setExactAndAllowWhileIdle(0, timeoutPeriod, pi);
        } else {
            alarmManager.cancel(pi);
        }
    }

    void setIsAutomotive(boolean isAutomotive) {
        this.mIsAutomotive = isAutomotive;
    }

    boolean getIsAutomotive() {
        return this.mIsAutomotive;
    }

    void setNotificationEffectsEnabledForAutomotive(boolean isEnabled) {
        this.mNotificationEffectsEnabledForAutomotive = isEnabled;
    }

    void setSystemReady(boolean systemReady) {
        this.mSystemReady = systemReady;
    }

    void setKeyguardManager(android.app.KeyguardManager keyguardManager) {
        this.mKeyguardManager = keyguardManager;
    }

    void setAccessibilityManager(android.view.accessibility.AccessibilityManager am) {
        this.mAccessibilityManager = am;
    }

    com.android.server.notification.VibratorHelper getVibratorHelper() {
        return this.mVibratorHelper;
    }

    void setVibratorHelper(com.android.server.notification.VibratorHelper helper) {
        this.mVibratorHelper = helper;
    }

    void setScreenOn(boolean on) {
        this.mScreenOn = on;
    }

    void setUserPresent(boolean userPresent) {
        this.mUserPresent = userPresent;
    }

    void setLights(com.android.server.lights.LogicalLight light) {
        this.mNotificationLight = light;
        this.mAttentionLight = light;
    }

    com.android.server.lights.LogicalLight getNotificationLight() {
        return this.mNotificationLight;
    }

    void setAudioManager(android.media.AudioManager audioManager) {
        this.mAudioManager = audioManager;
    }

    void setInCallStateOffHook(boolean inCallStateOffHook) {
        this.mInCallStateOffHook = inCallStateOffHook;
    }
}
