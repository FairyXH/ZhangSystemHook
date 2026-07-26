package com.android.server;

/* JADX INFO: loaded from: classes.dex */
final class UiModeManagerService extends com.android.server.SystemService {
    private static final boolean ENABLE_LAUNCH_DESK_DOCK_APP = true;
    private static final boolean LOG;
    public static final java.util.Set<java.lang.Integer> SUPPORTED_NIGHT_MODE_CUSTOM_TYPES;
    private static final java.lang.String SYSTEM_PROPERTY_DEVICE_THEME = "persist.sys.theme";
    private static final java.lang.String TAG = android.app.UiModeManager.class.getSimpleName();
    private final java.time.LocalTime DEFAULT_CUSTOM_NIGHT_END_TIME;
    private final java.time.LocalTime DEFAULT_CUSTOM_NIGHT_START_TIME;
    private com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManager;
    private android.app.AlarmManager mAlarmManager;
    private int mAttentionModeThemeOverlay;
    private final android.content.BroadcastReceiver mBatteryReceiver;
    private boolean mCar;
    private int mCarModeEnableFlags;
    private boolean mCarModeEnabled;
    private boolean mCarModeKeepsScreenOn;
    private java.util.Map<java.lang.Integer, java.lang.String> mCarModePackagePriority;
    private boolean mCharging;
    private boolean mComputedNightMode;
    private android.content.res.Configuration mConfiguration;
    private final android.database.ContentObserver mContrastObserver;
    private final android.util.SparseArray<java.lang.Float> mContrasts;
    int mCurUiMode;
    private int mCurrentUser;
    private java.time.LocalTime mCustomAutoNightModeEndMilliseconds;
    private java.time.LocalTime mCustomAutoNightModeStartMilliseconds;
    private final android.app.AlarmManager.OnAlarmListener mCustomTimeListener;
    private final android.database.ContentObserver mDarkThemeObserver;
    private int mDefaultUiModeType;
    private boolean mDeskModeKeepsScreenOn;
    private final android.content.BroadcastReceiver mDeviceInactiveListener;
    private final android.content.BroadcastReceiver mDockModeReceiver;
    private int mDockState;
    private android.service.dreams.DreamManagerInternal mDreamManagerInternal;
    private boolean mDreamsDisabledByAmbientModeSuppression;
    private boolean mEnableCarDockLaunch;
    private final android.os.Handler mHandler;
    private boolean mHoldingConfiguration;
    private final com.android.server.UiModeManagerService.Injector mInjector;
    private android.app.KeyguardManager mKeyguardManager;
    private boolean mLastBedtimeRequestedNightMode;
    private int mLastBroadcastState;
    private android.os.PowerManagerInternal mLocalPowerManager;
    private final com.android.server.UiModeManagerService.LocalService mLocalService;
    public final java.lang.Object mLock;
    private final com.android.server.UiModeManagerService.NightMode mNightMode;
    private int mNightModeCustomType;
    private boolean mNightModeLocked;
    private android.app.NotificationManager mNotificationManager;
    private final android.content.BroadcastReceiver mOnShutdown;
    private final android.content.BroadcastReceiver mOnTimeChangedHandler;
    private boolean mOverrideNightModeOff;
    private boolean mOverrideNightModeOn;
    private int mOverrideNightModeUser;
    private android.os.PowerManager mPowerManager;
    private boolean mPowerSave;
    private android.util.SparseArray<java.util.List<com.android.server.UiModeManagerService.ProjectionHolder>> mProjectionHolders;
    private android.util.SparseArray<android.os.RemoteCallbackList<android.app.IOnProjectionStateChangedListener>> mProjectionListeners;
    private final android.content.BroadcastReceiver mResultReceiver;
    private final android.app.IUiModeManager.Stub mService;
    private int mSetUiMode;
    private final android.content.BroadcastReceiver mSettingsRestored;
    private boolean mSetupWizardComplete;
    private final android.database.ContentObserver mSetupWizardObserver;
    private boolean mStartDreamImmediatelyOnDock;
    private android.app.StatusBarManager mStatusBarManager;
    boolean mSystemReady;
    private boolean mTelevision;
    private final com.android.server.twilight.TwilightListener mTwilightListener;
    private com.android.server.twilight.TwilightManager mTwilightManager;
    private boolean mUiModeLocked;
    private final android.util.SparseArray<android.os.RemoteCallbackList<android.app.IUiModeManagerCallback>> mUiModeManagerCallbacks;
    private com.android.server.IUiModeManagerServiceWrapper mUiModemsWrapper;
    private com.android.server.IUiModeManagerServiceExt mUmssExt;
    private boolean mVrHeadset;
    private final android.service.vr.IVrStateCallbacks mVrStateCallbacks;
    private boolean mWaitForDeviceInactive;
    private android.os.PowerManager.WakeLock mWakeLock;
    private boolean mWatch;
    private com.android.server.wm.WindowManagerInternal mWindowManager;

    private interface NightMode {
        int get();

        void set(int i);
    }

    static {
        LOG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) && "0".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
        SUPPORTED_NIGHT_MODE_CUSTOM_TYPES = new android.util.ArraySet(new java.lang.Integer[]{0, 1});
    }

    public UiModeManagerService(android.content.Context context) {
        this(context, false, null, new com.android.server.UiModeManagerService.Injector());
    }

    protected UiModeManagerService(android.content.Context context, boolean setupWizardComplete, com.android.server.twilight.TwilightManager tm, com.android.server.UiModeManagerService.Injector injector) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mDockState = 0;
        this.mLastBroadcastState = 0;
        this.mNightMode = new com.android.server.UiModeManagerService.NightMode() { // from class: com.android.server.UiModeManagerService.1
            private int mNightModeValue = 1;

            @Override // com.android.server.UiModeManagerService.NightMode
            public int get() {
                return this.mNightModeValue;
            }

            @Override // com.android.server.UiModeManagerService.NightMode
            public void set(int mode) {
                this.mNightModeValue = mode;
                if (android.app.Flags.enableNightModeBinderCache()) {
                    android.app.UiModeManager.invalidateNightModeCache();
                }
            }
        };
        this.mNightModeCustomType = -1;
        this.mAttentionModeThemeOverlay = 1000;
        this.DEFAULT_CUSTOM_NIGHT_START_TIME = java.time.LocalTime.of(22, 0);
        this.DEFAULT_CUSTOM_NIGHT_END_TIME = java.time.LocalTime.of(6, 0);
        this.mCustomAutoNightModeStartMilliseconds = this.DEFAULT_CUSTOM_NIGHT_START_TIME;
        this.mCustomAutoNightModeEndMilliseconds = this.DEFAULT_CUSTOM_NIGHT_END_TIME;
        this.mCarModePackagePriority = new java.util.HashMap();
        this.mCarModeEnabled = false;
        this.mCharging = false;
        this.mPowerSave = false;
        this.mWaitForDeviceInactive = false;
        this.mLastBedtimeRequestedNightMode = false;
        this.mStartDreamImmediatelyOnDock = true;
        this.mDreamsDisabledByAmbientModeSuppression = false;
        this.mEnableCarDockLaunch = true;
        this.mUiModeLocked = false;
        this.mNightModeLocked = false;
        this.mCurUiMode = 0;
        this.mSetUiMode = 0;
        this.mHoldingConfiguration = false;
        this.mConfiguration = new android.content.res.Configuration();
        this.mHandler = new android.os.Handler();
        this.mOverrideNightModeUser = 0;
        this.mLocalService = new com.android.server.UiModeManagerService.LocalService();
        this.mUiModeManagerCallbacks = new android.util.SparseArray<>();
        this.mUmssExt = (com.android.server.IUiModeManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.IUiModeManagerServiceExt.class).create();
        this.mContrasts = new android.util.SparseArray<>();
        this.mResultReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (getResultCode() != -1) {
                    if (com.android.server.UiModeManagerService.LOG) {
                        android.util.Slog.v(com.android.server.UiModeManagerService.TAG, "Handling broadcast result for action " + intent.getAction() + ": canceled: " + getResultCode());
                    }
                } else {
                    int enableFlags = intent.getIntExtra("enableFlags", 0);
                    int disableFlags = intent.getIntExtra("disableFlags", 0);
                    synchronized (com.android.server.UiModeManagerService.this.mLock) {
                        com.android.server.UiModeManagerService.this.updateAfterBroadcastLocked(intent.getAction(), enableFlags, disableFlags);
                    }
                }
            }
        };
        this.mDockModeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                int state = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
                com.android.server.UiModeManagerService.this.updateDockState(state);
            }
        };
        this.mBatteryReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                byte b;
                java.lang.String action = intent.getAction();
                switch (action.hashCode()) {
                    case -1538406691:
                        if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        com.android.server.UiModeManagerService.this.mCharging = intent.getIntExtra("plugged", 0) != 0;
                        break;
                }
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    if (com.android.server.UiModeManagerService.this.mSystemReady) {
                        com.android.server.UiModeManagerService.this.updateLocked(0, 0);
                    }
                }
            }
        };
        this.mTwilightListener = new com.android.server.twilight.TwilightListener() { // from class: com.android.server.UiModeManagerService.5
            @Override // com.android.server.twilight.TwilightListener
            public void onTwilightStateChanged(com.android.server.twilight.TwilightState state) {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    if (com.android.server.UiModeManagerService.this.mNightMode.get() == 0 && com.android.server.UiModeManagerService.this.mSystemReady) {
                        if (com.android.server.UiModeManagerService.this.shouldApplyAutomaticChangesImmediately()) {
                            if (com.android.server.UiModeManagerService.LOG) {
                                android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "onTwilightStateChanged updateLocked now-->" + java.time.LocalTime.now().toString());
                            }
                            com.android.server.UiModeManagerService.this.updateLocked(0, 0);
                        } else {
                            if (com.android.server.UiModeManagerService.LOG) {
                                android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "onTwilightStateChanged wait screen off-->" + java.time.LocalTime.now().toString());
                            }
                            com.android.server.UiModeManagerService.this.registerDeviceInactiveListenerLocked();
                        }
                    }
                }
            }
        };
        this.mDeviceInactiveListener = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    if (com.android.server.UiModeManagerService.LOG) {
                        android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "screenOff receiver-->" + java.time.LocalTime.now().toString());
                    }
                    com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
                    com.android.server.UiModeManagerService.this.updateLocked(0, 0);
                }
            }
        };
        this.mOnTimeChangedHandler = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.7
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    if (com.android.server.UiModeManagerService.LOG) {
                        android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "TimeChanged-->" + java.time.LocalTime.now().toString());
                    }
                    com.android.server.UiModeManagerService.this.updateCustomTimeLocked();
                }
            }
        };
        this.mCustomTimeListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.UiModeManagerService$$ExternalSyntheticLambda2
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$0();
            }
        };
        this.mVrStateCallbacks = new android.service.vr.IVrStateCallbacks.Stub() { // from class: com.android.server.UiModeManagerService.8
            public void onVrStateChanged(boolean enabled) {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    com.android.server.UiModeManagerService.this.mVrHeadset = enabled;
                    if (com.android.server.UiModeManagerService.this.mSystemReady) {
                        com.android.server.UiModeManagerService.this.updateLocked(0, 0);
                    }
                }
            }
        };
        this.mSetupWizardObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.UiModeManagerService.9
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri) {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    if (com.android.server.UiModeManagerService.this.setupWizardCompleteForCurrentUser() && !selfChange) {
                        com.android.server.UiModeManagerService.this.mSetupWizardComplete = true;
                        com.android.server.UiModeManagerService.this.getContext().getContentResolver().unregisterContentObserver(com.android.server.UiModeManagerService.this.mSetupWizardObserver);
                        android.content.Context context2 = com.android.server.UiModeManagerService.this.getContext();
                        com.android.server.UiModeManagerService.this.updateNightModeFromSettingsLocked(context2, context2.getResources(), android.os.UserHandle.getCallingUserId());
                        com.android.server.UiModeManagerService.this.updateLocked(0, 0);
                    }
                }
            }
        };
        this.mDarkThemeObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.UiModeManagerService.10
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri) {
                com.android.server.UiModeManagerService.this.updateSystemProperties();
            }
        };
        this.mContrastObserver = new com.android.server.UiModeManagerService.AnonymousClass11(this.mHandler);
        this.mOnShutdown = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.12
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (com.android.server.UiModeManagerService.this.mNightMode.get() == 0) {
                    com.android.server.UiModeManagerService.this.persistComputedNightMode(com.android.server.UiModeManagerService.this.mCurrentUser);
                }
            }
        };
        this.mSettingsRestored = new android.content.BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.13
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.util.List<java.lang.String> settings = java.util.Arrays.asList("ui_night_mode", "dark_theme_custom_start_time", "dark_theme_custom_end_time");
                if (settings.contains(intent.getExtras().getCharSequence("setting_name"))) {
                    synchronized (com.android.server.UiModeManagerService.this.mLock) {
                        com.android.server.UiModeManagerService.this.updateNightModeFromSettingsLocked(context2, context2.getResources(), android.os.UserHandle.getCallingUserId());
                        com.android.server.UiModeManagerService.this.updateConfigurationLocked();
                    }
                }
            }
        };
        this.mUiModemsWrapper = new com.android.server.UiModeManagerService.UiModeManagerServiceWrapper();
        this.mService = new com.android.server.UiModeManagerService.Stub(context);
        this.mConfiguration.setToDefaults();
        this.mSetupWizardComplete = setupWizardComplete;
        this.mTwilightManager = tm;
        this.mInjector = injector;
        this.mUmssExt.init(context, this, this.mService);
    }

    private static android.content.Intent buildHomeIntent(java.lang.String category) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN");
        intent.addCategory(category);
        intent.setFlags(270532608);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mLock) {
            if (LOG) {
                android.util.Slog.d(TAG, "customTime alarm-->" + java.time.LocalTime.now().toString());
            }
            updateCustomTimeLocked();
        }
    }

    /* JADX INFO: renamed from: com.android.server.UiModeManagerService$11, reason: invalid class name */
    class AnonymousClass11 extends android.database.ContentObserver {
        AnonymousClass11(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                if (com.android.server.UiModeManagerService.this.updateContrastLocked()) {
                    final float contrast = com.android.server.UiModeManagerService.this.getContrastLocked();
                    ((android.os.RemoteCallbackList) com.android.server.UiModeManagerService.this.mUiModeManagerCallbacks.get(com.android.server.UiModeManagerService.this.mCurrentUser, new android.os.RemoteCallbackList())).broadcast(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.UiModeManagerService$11$$ExternalSyntheticLambda0
                        public final void acceptOrThrow(java.lang.Object obj) {
                            ((android.app.IUiModeManagerCallback) obj).notifyContrastChanged(contrast);
                        }
                    }));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemProperties() {
        int mode = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "ui_night_mode", this.mNightMode.get(), 0);
        if (mode == 0 || mode == 3) {
            mode = 2;
        }
        android.os.SystemProperties.set(SYSTEM_PROPERTY_DEVICE_THEME, java.lang.Integer.toString(mode));
    }

    void setStartDreamImmediatelyOnDock(boolean startDreamImmediatelyOnDock) {
        this.mStartDreamImmediatelyOnDock = startDreamImmediatelyOnDock;
    }

    void setDreamsDisabledByAmbientModeSuppression(boolean disabledByAmbientModeSuppression) {
        this.mDreamsDisabledByAmbientModeSuppression = disabledByAmbientModeSuppression;
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mCurrentUser = to.getUserIdentifier();
        if (this.mNightMode.get() == 0) {
            persistComputedNightMode(from.getUserIdentifier());
        }
        getContext().getContentResolver().unregisterContentObserver(this.mSetupWizardObserver);
        verifySetupWizardCompleted();
        unregisterDeviceInactiveListenerLocked();
        synchronized (this.mLock) {
            updateNightModeFromSettingsLocked(getContext(), getContext().getResources(), to.getUserIdentifier());
            updateLocked(0, 0);
            this.mUmssExt.persistNightModeStatistics(getContext(), this.mCurrentUser);
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            synchronized (this.mLock) {
                android.content.Context context = getContext();
                boolean z = true;
                this.mSystemReady = true;
                this.mKeyguardManager = (android.app.KeyguardManager) context.getSystemService(android.app.KeyguardManager.class);
                this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
                this.mWakeLock = this.mPowerManager.newWakeLock(26, TAG);
                this.mWindowManager = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
                this.mActivityTaskManager = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
                this.mAlarmManager = (android.app.AlarmManager) getContext().getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
                com.android.server.twilight.TwilightManager twilightManager = (com.android.server.twilight.TwilightManager) getLocalService(com.android.server.twilight.TwilightManager.class);
                if (twilightManager != null) {
                    this.mTwilightManager = twilightManager;
                }
                this.mLocalPowerManager = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
                this.mDreamManagerInternal = (android.service.dreams.DreamManagerInternal) com.android.server.LocalServices.getService(android.service.dreams.DreamManagerInternal.class);
                initPowerSave();
                if (this.mDockState != 2) {
                    z = false;
                }
                this.mCarModeEnabled = z;
                registerVrStateListener();
                context.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("ui_night_mode"), false, this.mDarkThemeObserver, 0);
                context.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("contrast_level"), false, this.mContrastObserver, -1);
                context.registerReceiver(this.mDockModeReceiver, new android.content.IntentFilter("android.intent.action.DOCK_EVENT"));
                android.content.IntentFilter batteryFilter = new android.content.IntentFilter("android.intent.action.BATTERY_CHANGED");
                batteryFilter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
                context.registerReceiver(this.mBatteryReceiver, batteryFilter);
                context.registerReceiver(this.mSettingsRestored, new android.content.IntentFilter("android.os.action.SETTING_RESTORED"));
                context.registerReceiver(this.mOnShutdown, new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN"));
                this.mUmssExt.darkModeRegisterThermalProtect(this);
                this.mUmssExt.darkModeRegisterShutdownReceiver(this, this.mHandler);
                updateConfigurationLocked();
                applyConfigurationExternallyLocked();
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        final android.content.Context context = getContext();
        verifySetupWizardCompleted();
        final android.content.res.Resources res = context.getResources();
        this.mNightMode.set(res.getInteger(android.R.integer.config_defaultKeyboardVibrationIntensity));
        this.mStartDreamImmediatelyOnDock = res.getBoolean(android.R.bool.config_shortPressEarlyOnPower);
        this.mDreamsDisabledByAmbientModeSuppression = res.getBoolean(android.R.bool.config_dragToMaximizeInDesktopMode);
        this.mDefaultUiModeType = res.getInteger(android.R.integer.config_defaultPeakRefreshRate);
        this.mCarModeKeepsScreenOn = res.getInteger(android.R.integer.config_burnInProtectionMinVerticalOffset) == 1;
        this.mDeskModeKeepsScreenOn = res.getInteger(android.R.integer.config_defaultRefreshRateInZone) == 1;
        this.mEnableCarDockLaunch = res.getBoolean(android.R.bool.config_enableAutoPowerModes);
        this.mUiModeLocked = res.getBoolean(android.R.bool.config_keepDreamingWhenUnplugging);
        this.mNightModeLocked = res.getBoolean(android.R.bool.config_jobSchedulerRestrictBackgroundUser);
        android.content.pm.PackageManager pm = context.getPackageManager();
        this.mTelevision = pm.hasSystemFeature("android.hardware.type.television") || pm.hasSystemFeature("android.software.leanback");
        this.mCar = pm.hasSystemFeature("android.hardware.type.automotive");
        this.mWatch = pm.hasSystemFeature("android.hardware.type.watch");
        com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.UiModeManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStart$1(context, res);
            }
        }, TAG + ".onStart");
        publishBinderService("uimode", this.mService);
        publishLocalService(com.android.server.UiModeManagerInternal.class, this.mLocalService);
        this.mUmssExt.darkModeOnStartInit(context, this);
        this.mUmssExt.persistNightModeStatistics(getContext(), android.os.UserHandle.getCallingUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$1(android.content.Context context, android.content.res.Resources res) {
        synchronized (this.mLock) {
            com.android.server.twilight.TwilightManager twilightManager = (com.android.server.twilight.TwilightManager) getLocalService(com.android.server.twilight.TwilightManager.class);
            if (twilightManager != null) {
                this.mTwilightManager = twilightManager;
            }
            updateNightModeFromSettingsLocked(context, res, android.os.UserHandle.getCallingUserId());
            updateSystemProperties();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistComputedNightMode(int i) {
        android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode_last_computed", this.mComputedNightMode ? 1 : 0, i);
    }

    private void initPowerSave() {
        if (this.mUmssExt.darkModeShouldHideSaveMode()) {
            return;
        }
        this.mPowerSave = this.mLocalPowerManager.getLowPowerState(16).batterySaverEnabled;
        this.mLocalPowerManager.registerLowPowerModeObserver(16, new java.util.function.Consumer() { // from class: com.android.server.UiModeManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$initPowerSave$2((android.os.PowerSaveState) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initPowerSave$2(android.os.PowerSaveState state) {
        synchronized (this.mLock) {
            if (this.mPowerSave == state.batterySaverEnabled) {
                return;
            }
            this.mPowerSave = state.batterySaverEnabled;
            if (this.mSystemReady) {
                updateLocked(0, 0);
            }
        }
    }

    protected android.app.IUiModeManager getService() {
        return this.mService;
    }

    protected android.content.res.Configuration getConfiguration() {
        return this.mConfiguration;
    }

    private void verifySetupWizardCompleted() {
        android.content.Context context = getContext();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!setupWizardCompleteForCurrentUser()) {
            this.mSetupWizardComplete = false;
            context.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("user_setup_complete"), false, this.mSetupWizardObserver, userId);
        } else {
            this.mSetupWizardComplete = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setupWizardCompleteForCurrentUser() {
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "user_setup_complete", 0, android.os.UserHandle.getCallingUserId()) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCustomTimeLocked() {
        if (this.mNightMode.get() != 3) {
            return;
        }
        if (shouldApplyAutomaticChangesImmediately()) {
            if (LOG) {
                android.util.Slog.d(TAG, "updateCustomTimeLocked updateLocked now-->" + java.time.LocalTime.now().toString());
            }
            updateLocked(0, 0);
        } else {
            if (LOG) {
                android.util.Slog.d(TAG, "updateCustomTimeLocked wait screen off-->" + java.time.LocalTime.now().toString());
            }
            registerDeviceInactiveListenerLocked();
        }
        scheduleNextCustomTimeListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightModeFromSettingsLocked(android.content.Context context, android.content.res.Resources res, int userId) {
        if (!this.mCarModeEnabled && !this.mCar && this.mSetupWizardComplete) {
            this.mNightMode.set(android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode", res.getInteger(android.R.integer.config_defaultKeyboardVibrationIntensity), userId));
            this.mNightModeCustomType = android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_custom_type", -1, userId);
            this.mOverrideNightModeOn = android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_override_on", 0, userId) != 0;
            this.mOverrideNightModeOff = android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_override_off", 0, userId) != 0;
            this.mCustomAutoNightModeStartMilliseconds = java.time.LocalTime.ofNanoOfDay(android.provider.Settings.Secure.getLongForUser(context.getContentResolver(), "dark_theme_custom_start_time", this.DEFAULT_CUSTOM_NIGHT_START_TIME.toNanoOfDay() / 1000, userId) * 1000);
            this.mCustomAutoNightModeEndMilliseconds = java.time.LocalTime.ofNanoOfDay(android.provider.Settings.Secure.getLongForUser(context.getContentResolver(), "dark_theme_custom_end_time", this.DEFAULT_CUSTOM_NIGHT_END_TIME.toNanoOfDay() / 1000, userId) * 1000);
            if (this.mNightMode.get() == 0) {
                this.mComputedNightMode = android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_last_computed", 0, userId) != 0;
            }
            this.mUmssExt.darkModeInitSettings(getContext());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long toMilliSeconds(java.time.LocalTime t) {
        return t.toNanoOfDay() / 1000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.time.LocalTime fromMilliseconds(long t) {
        return java.time.LocalTime.ofNanoOfDay(1000 * t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDeviceInactiveListenerLocked() {
        if (this.mPowerSave) {
            return;
        }
        this.mWaitForDeviceInactive = true;
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.DREAMING_STARTED");
        getContext().registerReceiver(this.mDeviceInactiveListener, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelCustomAlarm() {
        this.mAlarmManager.cancel(this.mCustomTimeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDeviceInactiveListenerLocked() {
        this.mWaitForDeviceInactive = false;
        try {
            getContext().unregisterReceiver(this.mDeviceInactiveListener);
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    private void registerTimeChangeEvent() {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        getContext().registerReceiver(this.mOnTimeChangedHandler, intentFilter);
    }

    private void unregisterTimeChangeEvent() {
        try {
            getContext().unregisterReceiver(this.mOnTimeChangedHandler);
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class Stub extends android.app.IUiModeManager.Stub {
        Stub(android.content.Context context) {
            super(android.os.PermissionEnforcer.fromContext(context));
        }

        public void addCallback(android.app.IUiModeManagerCallback callback) {
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                if (!com.android.server.UiModeManagerService.this.mUiModeManagerCallbacks.contains(userId)) {
                    com.android.server.UiModeManagerService.this.mUiModeManagerCallbacks.put(userId, new android.os.RemoteCallbackList());
                }
                ((android.os.RemoteCallbackList) com.android.server.UiModeManagerService.this.mUiModeManagerCallbacks.get(userId)).register(callback);
            }
        }

        public void enableCarMode(int flags, int priority, java.lang.String callingPackage) {
            if (isUiModeLocked()) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "enableCarMode while UI mode is locked");
                return;
            }
            if (priority != 0 && com.android.server.UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.ENTER_CAR_MODE_PRIORITIZED") != 0) {
                throw new java.lang.SecurityException("Enabling car mode with a priority requires permission ENTER_CAR_MODE_PRIORITIZED");
            }
            boolean isShellCaller = com.android.server.UiModeManagerService.this.mInjector.getCallingUid() == 2000;
            if (!isShellCaller) {
                com.android.server.UiModeManagerService.this.assertLegit(callingPackage);
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    com.android.server.UiModeManagerService.this.setCarModeLocked(true, flags, priority, callingPackage);
                    if (com.android.server.UiModeManagerService.this.mSystemReady) {
                        com.android.server.UiModeManagerService.this.updateLocked(flags, 0);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void disableCarMode(int flags) {
            disableCarModeByCallingPackage(flags, null);
        }

        public void disableCarModeByCallingPackage(int flags, final java.lang.String callingPackage) {
            if (isUiModeLocked()) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "disableCarMode while UI mode is locked");
                return;
            }
            int callingUid = com.android.server.UiModeManagerService.this.mInjector.getCallingUid();
            boolean isSystemCaller = callingUid == 1000;
            boolean isShellCaller = callingUid == 2000;
            if (!isSystemCaller && !isShellCaller) {
                com.android.server.UiModeManagerService.this.assertLegit(callingPackage);
            }
            int carModeFlags = isSystemCaller ? flags : flags & (-3);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    int priority = ((java.lang.Integer) com.android.server.UiModeManagerService.this.mCarModePackagePriority.entrySet().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.UiModeManagerService$Stub$$ExternalSyntheticLambda2
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return ((java.lang.String) ((java.util.Map.Entry) obj).getValue()).equals(callingPackage);
                        }
                    }).findFirst().map(new com.android.server.UiModeManagerService$Stub$$ExternalSyntheticLambda3()).orElse(0)).intValue();
                    com.android.server.UiModeManagerService.this.setCarModeLocked(false, carModeFlags, priority, callingPackage);
                    if (com.android.server.UiModeManagerService.this.mSystemReady) {
                        com.android.server.UiModeManagerService.this.updateLocked(0, flags);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int getCurrentModeType() {
            int i;
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    i = com.android.server.UiModeManagerService.this.mCurUiMode & 15;
                }
                return i;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setNightMode(int mode) {
            int customModeType;
            if (mode == 3) {
                customModeType = 0;
            } else {
                customModeType = -1;
            }
            setNightModeInternal(mode, customModeType);
        }

        private void setNightModeInternal(int mode, int customModeType) {
            int i;
            if (isNightModeLocked() && com.android.server.UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "Night mode locked, requires MODIFY_DAY_NIGHT_MODE permission");
                return;
            }
            switch (mode) {
                case 0:
                case 1:
                case 2:
                    break;
                case 3:
                    if (!com.android.server.UiModeManagerService.SUPPORTED_NIGHT_MODE_CUSTOM_TYPES.contains(java.lang.Integer.valueOf(customModeType))) {
                        throw new java.lang.IllegalArgumentException("Can't set the custom type to " + customModeType);
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException("Unknown mode: " + mode);
            }
            int user = android.os.UserHandle.getCallingUserId();
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(user);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.UiModeManagerService.this.mLock) {
                    if (com.android.server.UiModeManagerService.LOG) {
                        android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "userId:" + user + "-->pid:" + android.os.Binder.getCallingPid() + "-->setNightMode:" + mode + "-->oldMode:" + com.android.server.UiModeManagerService.this.mNightMode);
                    }
                    com.android.server.UiModeManagerService.this.mUmssExt.upCommonStatistics(com.android.server.UiModeManagerService.this.getContext(), user, com.android.server.UiModeManagerService.this.mNightMode.get(), mode);
                    if (com.android.server.UiModeManagerService.this.mNightMode.get() != mode || com.android.server.UiModeManagerService.this.mNightModeCustomType != customModeType) {
                        if (com.android.server.UiModeManagerService.this.mNightMode.get() == 0 || com.android.server.UiModeManagerService.this.mNightMode.get() == 3) {
                            com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
                            com.android.server.UiModeManagerService.this.cancelCustomAlarm();
                        }
                        com.android.server.UiModeManagerService uiModeManagerService = com.android.server.UiModeManagerService.this;
                        if (mode == 3) {
                            i = customModeType;
                        } else {
                            i = -1;
                        }
                        uiModeManagerService.mNightModeCustomType = i;
                        com.android.server.UiModeManagerService.this.mNightMode.set(mode);
                        com.android.server.UiModeManagerService.this.mAttentionModeThemeOverlay = 1000;
                        com.android.server.UiModeManagerService.this.resetNightModeOverrideLocked();
                        com.android.server.UiModeManagerService.this.persistNightMode(user);
                        if ((com.android.server.UiModeManagerService.this.mNightMode.get() != 0 && com.android.server.UiModeManagerService.this.mNightMode.get() != 3) || com.android.server.UiModeManagerService.this.shouldApplyAutomaticChangesImmediately()) {
                            com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
                            com.android.server.UiModeManagerService.this.updateLocked(0, 0);
                        } else {
                            com.android.server.UiModeManagerService.this.registerDeviceInactiveListenerLocked();
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int getNightMode() {
            int i;
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                i = com.android.server.UiModeManagerService.this.mNightMode.get();
            }
            return i;
        }

        public void setNightModeCustomType(int nightModeCustomType) {
            setNightModeCustomType_enforcePermission();
            setNightModeInternal(3, nightModeCustomType);
        }

        public int getNightModeCustomType() {
            int i;
            getNightModeCustomType_enforcePermission();
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                i = com.android.server.UiModeManagerService.this.mNightModeCustomType;
            }
            return i;
        }

        public void setAttentionModeThemeOverlay(int attentionModeThemeOverlayType) {
            setAttentionModeThemeOverlay_enforcePermission();
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(android.os.UserHandle.getCallingUserId());
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                if (com.android.server.UiModeManagerService.this.mAttentionModeThemeOverlay != attentionModeThemeOverlayType) {
                    com.android.server.UiModeManagerService.this.mAttentionModeThemeOverlay = attentionModeThemeOverlayType;
                    android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.UiModeManagerService$Stub$$ExternalSyntheticLambda0
                        public final void runOrThrow() throws java.lang.Exception {
                            this.f$0.lambda$setAttentionModeThemeOverlay$1();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setAttentionModeThemeOverlay$1() throws java.lang.Exception {
            com.android.server.UiModeManagerService.this.updateLocked(0, 0);
        }

        public int getAttentionModeThemeOverlay() {
            int i;
            getAttentionModeThemeOverlay_enforcePermission();
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                i = com.android.server.UiModeManagerService.this.mAttentionModeThemeOverlay;
            }
            return i;
        }

        public void setApplicationNightMode(int mode) {
            int configNightMode;
            switch (mode) {
                case 0:
                case 1:
                case 2:
                case 3:
                    switch (mode) {
                        case 1:
                            configNightMode = 16;
                            break;
                        case 2:
                            configNightMode = 32;
                            break;
                        default:
                            configNightMode = 0;
                            break;
                    }
                    com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater updater = com.android.server.UiModeManagerService.this.mActivityTaskManager.createPackageConfigurationUpdater();
                    updater.setNightMode(configNightMode);
                    updater.commit();
                    return;
                default:
                    throw new java.lang.IllegalArgumentException("Unknown mode: " + mode);
            }
        }

        public boolean isUiModeLocked() {
            boolean z;
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                z = com.android.server.UiModeManagerService.this.mUiModeLocked;
            }
            return z;
        }

        public boolean isNightModeLocked() {
            boolean z;
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                z = com.android.server.UiModeManagerService.this.mNightModeLocked;
            }
            return z;
        }

        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.UiModeManagerService.Shell(com.android.server.UiModeManagerService.this.mService).exec(com.android.server.UiModeManagerService.this.mService, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.UiModeManagerService.this.getContext(), com.android.server.UiModeManagerService.TAG, pw)) {
                com.android.server.UiModeManagerService.this.dumpImpl(pw);
            }
        }

        public boolean setNightModeActivatedForCustomMode(int modeNightCustomType, boolean active) {
            return setNightModeActivatedForModeInternal(modeNightCustomType, active);
        }

        public boolean setNightModeActivated(boolean active) {
            return setNightModeActivatedForModeInternal(com.android.server.UiModeManagerService.this.mNightModeCustomType, active);
        }

        private boolean setNightModeActivatedForModeInternal(int modeCustomType, boolean active) {
            if (com.android.server.UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "Night mode locked, requires MODIFY_DAY_NIGHT_MODE permission");
                return false;
            }
            int user = android.os.Binder.getCallingUserHandle().getIdentifier();
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(user);
            if (user != com.android.server.UiModeManagerService.this.mCurrentUser && com.android.server.UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") != 0) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "Target user is not current user, INTERACT_ACROSS_USERS permission is required");
                return false;
            }
            if (modeCustomType == 1) {
                com.android.server.UiModeManagerService.this.mLastBedtimeRequestedNightMode = active;
            }
            if (modeCustomType != com.android.server.UiModeManagerService.this.mNightModeCustomType) {
                return false;
            }
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (com.android.server.UiModeManagerService.LOG) {
                        android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "pid:" + android.os.Binder.getCallingPid() + "-->setNightModeActivated-->" + active);
                    }
                    if (com.android.server.UiModeManagerService.this.mNightMode.get() != 0 && com.android.server.UiModeManagerService.this.mNightMode.get() != 3) {
                        if (com.android.server.UiModeManagerService.this.mNightMode.get() == 1 && active) {
                            com.android.server.UiModeManagerService.this.mUmssExt.upCommonStatistics(com.android.server.UiModeManagerService.this.getContext(), com.android.server.UiModeManagerService.this.mCurrentUser, com.android.server.UiModeManagerService.this.mNightMode.get(), 2);
                            com.android.server.UiModeManagerService.this.mNightMode.set(2);
                        } else if (com.android.server.UiModeManagerService.this.mNightMode.get() == 2 && !active) {
                            com.android.server.UiModeManagerService.this.mUmssExt.upCommonStatistics(com.android.server.UiModeManagerService.this.getContext(), com.android.server.UiModeManagerService.this.mCurrentUser, com.android.server.UiModeManagerService.this.mNightMode.get(), 1);
                            com.android.server.UiModeManagerService.this.mNightMode.set(1);
                        }
                    } else {
                        com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
                        com.android.server.UiModeManagerService.this.mOverrideNightModeOff = active ? false : true;
                        com.android.server.UiModeManagerService.this.mOverrideNightModeOn = active;
                        com.android.server.UiModeManagerService.this.mOverrideNightModeUser = com.android.server.UiModeManagerService.this.mCurrentUser;
                        com.android.server.UiModeManagerService.this.persistNightModeOverrides(com.android.server.UiModeManagerService.this.mCurrentUser);
                    }
                    com.android.server.UiModeManagerService.this.updateConfigurationLocked();
                    com.android.server.UiModeManagerService.this.applyConfigurationExternallyLocked();
                    com.android.server.UiModeManagerService.this.persistNightMode(com.android.server.UiModeManagerService.this.mCurrentUser);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
            return true;
        }

        public long getCustomNightModeStart() {
            return com.android.server.UiModeManagerService.this.mCustomAutoNightModeStartMilliseconds.toNanoOfDay() / 1000;
        }

        public void setCustomNightModeStart(long time) {
            if (isNightModeLocked() && com.android.server.UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "Set custom time start, requires MODIFY_DAY_NIGHT_MODE permission");
                return;
            }
            int user = android.os.UserHandle.getCallingUserId();
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(user);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    java.time.LocalTime newTime = java.time.LocalTime.ofNanoOfDay(1000 * time);
                    if (newTime == null) {
                        return;
                    }
                    com.android.server.UiModeManagerService.this.mCustomAutoNightModeStartMilliseconds = newTime;
                    if (com.android.server.UiModeManagerService.LOG) {
                        android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "pid:" + android.os.Binder.getCallingPid() + "-->setCustomNightModeStart-->" + com.android.server.UiModeManagerService.this.mCustomAutoNightModeStartMilliseconds);
                    }
                    com.android.server.UiModeManagerService.this.persistNightMode(user);
                    com.android.server.UiModeManagerService.this.onCustomTimeUpdated(user);
                } catch (java.time.DateTimeException e) {
                    com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public long getCustomNightModeEnd() {
            return com.android.server.UiModeManagerService.this.mCustomAutoNightModeEndMilliseconds.toNanoOfDay() / 1000;
        }

        public void setCustomNightModeEnd(long time) {
            java.time.LocalTime newTime;
            if (isNightModeLocked() && com.android.server.UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "Set custom time end, requires MODIFY_DAY_NIGHT_MODE permission");
                return;
            }
            int user = android.os.UserHandle.getCallingUserId();
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(user);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    newTime = java.time.LocalTime.ofNanoOfDay(1000 * time);
                } catch (java.time.DateTimeException e) {
                    com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
                }
                if (newTime == null) {
                    return;
                }
                com.android.server.UiModeManagerService.this.mCustomAutoNightModeEndMilliseconds = newTime;
                if (com.android.server.UiModeManagerService.LOG) {
                    android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "pid:" + android.os.Binder.getCallingPid() + "-->setCustomNightModeEnd-->" + com.android.server.UiModeManagerService.this.mCustomAutoNightModeEndMilliseconds);
                }
                com.android.server.UiModeManagerService.this.onCustomTimeUpdated(user);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean requestProjection(android.os.IBinder binder, int projectionType, java.lang.String callingPackage) {
            com.android.server.UiModeManagerService.this.assertLegit(callingPackage);
            com.android.server.UiModeManagerService.assertSingleProjectionType(projectionType);
            com.android.server.UiModeManagerService.this.enforceProjectionTypePermissions(projectionType);
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(android.os.UserHandle.getCallingUserId());
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                if (com.android.server.UiModeManagerService.this.mProjectionHolders == null) {
                    com.android.server.UiModeManagerService.this.mProjectionHolders = new android.util.SparseArray(1);
                }
                if (!com.android.server.UiModeManagerService.this.mProjectionHolders.contains(projectionType)) {
                    com.android.server.UiModeManagerService.this.mProjectionHolders.put(projectionType, new java.util.ArrayList(1));
                }
                java.util.List<com.android.server.UiModeManagerService.ProjectionHolder> currentHolders = (java.util.List) com.android.server.UiModeManagerService.this.mProjectionHolders.get(projectionType);
                for (int i = 0; i < currentHolders.size(); i++) {
                    if (callingPackage.equals(currentHolders.get(i).mPackageName)) {
                        return true;
                    }
                }
                if (projectionType == 1 && !currentHolders.isEmpty()) {
                    return false;
                }
                final com.android.server.UiModeManagerService uiModeManagerService = com.android.server.UiModeManagerService.this;
                com.android.server.UiModeManagerService.ProjectionHolder projectionHolder = new com.android.server.UiModeManagerService.ProjectionHolder(callingPackage, projectionType, binder, new com.android.server.UiModeManagerService.ProjectionHolder.ProjectionReleaser() { // from class: com.android.server.UiModeManagerService$Stub$$ExternalSyntheticLambda1
                    @Override // com.android.server.UiModeManagerService.ProjectionHolder.ProjectionReleaser
                    public final boolean release(int i2, java.lang.String str) {
                        return uiModeManagerService.releaseProjectionUnchecked(i2, str);
                    }
                });
                if (!projectionHolder.linkToDeath()) {
                    return false;
                }
                currentHolders.add(projectionHolder);
                android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "Package " + callingPackage + " set projection type " + projectionType + ".");
                com.android.server.UiModeManagerService.this.onProjectionStateChangedLocked(projectionType);
                return true;
            }
        }

        public boolean releaseProjection(int projectionType, java.lang.String callingPackage) {
            com.android.server.UiModeManagerService.this.assertLegit(callingPackage);
            com.android.server.UiModeManagerService.assertSingleProjectionType(projectionType);
            com.android.server.UiModeManagerService.this.enforceProjectionTypePermissions(projectionType);
            com.android.server.UiModeManagerService.this.enforceValidCallingUser(android.os.UserHandle.getCallingUserId());
            return com.android.server.UiModeManagerService.this.releaseProjectionUnchecked(projectionType, callingPackage);
        }

        public int getActiveProjectionTypes() {
            getActiveProjectionTypes_enforcePermission();
            int projectionTypeFlag = 0;
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                if (com.android.server.UiModeManagerService.this.mProjectionHolders != null) {
                    for (int i = 0; i < com.android.server.UiModeManagerService.this.mProjectionHolders.size(); i++) {
                        if (!((java.util.List) com.android.server.UiModeManagerService.this.mProjectionHolders.valueAt(i)).isEmpty()) {
                            projectionTypeFlag |= com.android.server.UiModeManagerService.this.mProjectionHolders.keyAt(i);
                        }
                    }
                }
            }
            return projectionTypeFlag;
        }

        public java.util.List<java.lang.String> getProjectingPackages(int projectionType) {
            java.util.List<java.lang.String> packageNames;
            getProjectingPackages_enforcePermission();
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                packageNames = new java.util.ArrayList<>();
                com.android.server.UiModeManagerService.this.populateWithRelevantActivePackageNames(projectionType, packageNames);
            }
            return packageNames;
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x0072 A[Catch: all -> 0x0074, DONT_GENERATE, TryCatch #0 {, blocks: (B:7:0x0014, B:9:0x001c, B:10:0x0027, B:12:0x0033, B:13:0x0041, B:15:0x0053, B:17:0x0064, B:20:0x0069, B:21:0x0072), top: B:26:0x0014, inners: #1 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void addOnProjectionStateChangedListener(android.app.IOnProjectionStateChangedListener r7, int r8) {
            /*
                r6 = this;
                r6.addOnProjectionStateChangedListener_enforcePermission()
                if (r8 != 0) goto L6
                return
            L6:
                com.android.server.UiModeManagerService r0 = com.android.server.UiModeManagerService.this
                int r1 = android.os.UserHandle.getCallingUserId()
                com.android.server.UiModeManagerService.m491$$Nest$menforceValidCallingUser(r0, r1)
                com.android.server.UiModeManagerService r0 = com.android.server.UiModeManagerService.this
                java.lang.Object r0 = r0.mLock
                monitor-enter(r0)
                com.android.server.UiModeManagerService r1 = com.android.server.UiModeManagerService.this     // Catch: java.lang.Throwable -> L74
                android.util.SparseArray r1 = com.android.server.UiModeManagerService.m468$$Nest$fgetmProjectionListeners(r1)     // Catch: java.lang.Throwable -> L74
                if (r1 != 0) goto L27
                com.android.server.UiModeManagerService r1 = com.android.server.UiModeManagerService.this     // Catch: java.lang.Throwable -> L74
                android.util.SparseArray r2 = new android.util.SparseArray     // Catch: java.lang.Throwable -> L74
                r3 = 1
                r2.<init>(r3)     // Catch: java.lang.Throwable -> L74
                com.android.server.UiModeManagerService.m484$$Nest$fputmProjectionListeners(r1, r2)     // Catch: java.lang.Throwable -> L74
            L27:
                com.android.server.UiModeManagerService r1 = com.android.server.UiModeManagerService.this     // Catch: java.lang.Throwable -> L74
                android.util.SparseArray r1 = com.android.server.UiModeManagerService.m468$$Nest$fgetmProjectionListeners(r1)     // Catch: java.lang.Throwable -> L74
                boolean r1 = r1.contains(r8)     // Catch: java.lang.Throwable -> L74
                if (r1 != 0) goto L41
                com.android.server.UiModeManagerService r1 = com.android.server.UiModeManagerService.this     // Catch: java.lang.Throwable -> L74
                android.util.SparseArray r1 = com.android.server.UiModeManagerService.m468$$Nest$fgetmProjectionListeners(r1)     // Catch: java.lang.Throwable -> L74
                android.os.RemoteCallbackList r2 = new android.os.RemoteCallbackList     // Catch: java.lang.Throwable -> L74
                r2.<init>()     // Catch: java.lang.Throwable -> L74
                r1.put(r8, r2)     // Catch: java.lang.Throwable -> L74
            L41:
                com.android.server.UiModeManagerService r1 = com.android.server.UiModeManagerService.this     // Catch: java.lang.Throwable -> L74
                android.util.SparseArray r1 = com.android.server.UiModeManagerService.m468$$Nest$fgetmProjectionListeners(r1)     // Catch: java.lang.Throwable -> L74
                java.lang.Object r1 = r1.get(r8)     // Catch: java.lang.Throwable -> L74
                android.os.RemoteCallbackList r1 = (android.os.RemoteCallbackList) r1     // Catch: java.lang.Throwable -> L74
                boolean r1 = r1.register(r7)     // Catch: java.lang.Throwable -> L74
                if (r1 == 0) goto L72
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L74
                r1.<init>()     // Catch: java.lang.Throwable -> L74
                com.android.server.UiModeManagerService r2 = com.android.server.UiModeManagerService.this     // Catch: java.lang.Throwable -> L74
                int r2 = com.android.server.UiModeManagerService.m498$$Nest$mpopulateWithRelevantActivePackageNames(r2, r8, r1)     // Catch: java.lang.Throwable -> L74
                boolean r3 = r1.isEmpty()     // Catch: java.lang.Throwable -> L74
                if (r3 != 0) goto L72
                r7.onProjectionStateChanged(r2, r1)     // Catch: android.os.RemoteException -> L68 java.lang.Throwable -> L74
                goto L72
            L68:
                r3 = move-exception
                java.lang.String r4 = com.android.server.UiModeManagerService.m513$$Nest$sfgetTAG()     // Catch: java.lang.Throwable -> L74
                java.lang.String r5 = "Failed a call to onProjectionStateChanged() during listener registration."
                android.util.Slog.w(r4, r5)     // Catch: java.lang.Throwable -> L74
            L72:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L74
                return
            L74:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L74
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.UiModeManagerService.Stub.addOnProjectionStateChangedListener(android.app.IOnProjectionStateChangedListener, int):void");
        }

        public void removeOnProjectionStateChangedListener(android.app.IOnProjectionStateChangedListener listener) {
            removeOnProjectionStateChangedListener_enforcePermission();
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                if (com.android.server.UiModeManagerService.this.mProjectionListeners != null) {
                    for (int i = 0; i < com.android.server.UiModeManagerService.this.mProjectionListeners.size(); i++) {
                        ((android.os.RemoteCallbackList) com.android.server.UiModeManagerService.this.mProjectionListeners.valueAt(i)).unregister(listener);
                    }
                }
            }
        }

        public float getContrast() {
            float contrastLocked;
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                contrastLocked = com.android.server.UiModeManagerService.this.getContrastLocked();
            }
            return contrastLocked;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceValidCallingUser(int userId) {
        if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            return;
        }
        if (LOG) {
            android.util.Slog.d(TAG, "enforceValidCallingUser: userId=" + userId + " isSystemUser=" + (userId == 0) + " current user=" + this.mCurrentUser + " callingPid=" + android.os.Binder.getCallingPid() + " callingUid=" + this.mInjector.getCallingUid());
        }
        long ident = android.os.Binder.clearCallingIdentity();
        if (userId != 0) {
            try {
                if (userId != this.mCurrentUser && !com.android.server.pm.UserManagerService.getInstance().isSameProfileGroup(userId, this.mCurrentUser)) {
                    throw new java.lang.SecurityException("Calling user is not valid for level-1 compatibility in MUMD. callingUserId=" + userId + " currentUserId=" + this.mCurrentUser);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceProjectionTypePermissions(int p) {
        if ((p & 1) != 0) {
            getContext().enforceCallingPermission("android.permission.TOGGLE_AUTOMOTIVE_PROJECTION", "toggleProjection");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertSingleProjectionType(int p) {
        boolean projectionTypeIsPowerOfTwoOrZero = ((p + (-1)) & p) == 0;
        if (p == 0 || !projectionTypeIsPowerOfTwoOrZero) {
            throw new java.lang.IllegalArgumentException("Must specify exactly one projection type.");
        }
    }

    private static java.util.List<java.lang.String> toPackageNameList(java.util.Collection<com.android.server.UiModeManagerService.ProjectionHolder> c) {
        java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>();
        for (com.android.server.UiModeManagerService.ProjectionHolder p : c) {
            packageNames.add(p.mPackageName);
        }
        return packageNames;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int populateWithRelevantActivePackageNames(int projectionType, java.util.List<java.lang.String> packageNames) {
        packageNames.clear();
        int projectionTypeFlag = 0;
        if (this.mProjectionHolders != null) {
            for (int i = 0; i < this.mProjectionHolders.size(); i++) {
                int key = this.mProjectionHolders.keyAt(i);
                java.util.List<com.android.server.UiModeManagerService.ProjectionHolder> holders = this.mProjectionHolders.valueAt(i);
                if ((projectionType & key) != 0 && packageNames.addAll(toPackageNameList(holders))) {
                    projectionTypeFlag |= key;
                }
            }
        }
        return projectionTypeFlag;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean releaseProjectionUnchecked(int projectionType, java.lang.String pkg) {
        boolean removed;
        java.util.List<com.android.server.UiModeManagerService.ProjectionHolder> holders;
        synchronized (this.mLock) {
            removed = false;
            if (this.mProjectionHolders != null && (holders = this.mProjectionHolders.get(projectionType)) != null) {
                for (int i = holders.size() - 1; i >= 0; i--) {
                    com.android.server.UiModeManagerService.ProjectionHolder holder = holders.get(i);
                    if (pkg.equals(holder.mPackageName)) {
                        holder.unlinkToDeath();
                        android.util.Slog.d(TAG, "Projection type " + projectionType + " released by " + pkg + ".");
                        holders.remove(i);
                        removed = true;
                    }
                }
            }
            if (removed) {
                onProjectionStateChangedLocked(projectionType);
            } else {
                android.util.Slog.w(TAG, pkg + " tried to release projection type " + projectionType + " but was not set by that package.");
            }
        }
        return removed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getContrastLocked() {
        if (!this.mContrasts.contains(this.mCurrentUser)) {
            updateContrastLocked();
        }
        return this.mContrasts.get(this.mCurrentUser).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateContrastLocked() {
        float contrast = android.provider.Settings.Secure.getFloatForUser(getContext().getContentResolver(), "contrast_level", 0.0f, this.mCurrentUser);
        if (java.lang.Math.abs(this.mContrasts.get(this.mCurrentUser, java.lang.Float.valueOf(Float.MAX_VALUE)).floatValue() - contrast) >= 1.0E-10d) {
            this.mContrasts.put(this.mCurrentUser, java.lang.Float.valueOf(contrast));
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ProjectionHolder implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mBinder;
        private final java.lang.String mPackageName;
        private final com.android.server.UiModeManagerService.ProjectionHolder.ProjectionReleaser mProjectionReleaser;
        private final int mProjectionType;

        /* JADX INFO: Access modifiers changed from: private */
        interface ProjectionReleaser {
            boolean release(int i, java.lang.String str);
        }

        private ProjectionHolder(java.lang.String packageName, int projectionType, android.os.IBinder binder, com.android.server.UiModeManagerService.ProjectionHolder.ProjectionReleaser projectionReleaser) {
            this.mPackageName = packageName;
            this.mProjectionType = projectionType;
            this.mBinder = binder;
            this.mProjectionReleaser = projectionReleaser;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean linkToDeath() {
            try {
                this.mBinder.linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.UiModeManagerService.TAG, "linkToDeath failed for projection requester: " + this.mPackageName + ".", e);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unlinkToDeath() {
            this.mBinder.unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.w(com.android.server.UiModeManagerService.TAG, "Projection holder " + this.mPackageName + " died. Releasing projection type " + this.mProjectionType + ".");
            this.mProjectionReleaser.release(this.mProjectionType, this.mPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertLegit(java.lang.String packageName) {
        if (!doesPackageHaveCallingUid(packageName)) {
            throw new java.lang.SecurityException("Caller claimed bogus packageName: " + packageName + ".");
        }
    }

    private boolean doesPackageHaveCallingUid(java.lang.String packageName) {
        int callingUid = this.mInjector.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return getContext().getPackageManager().getPackageUidAsUser(packageName, callingUserId) == callingUid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProjectionStateChangedLocked(int changedProjectionType) {
        if (this.mProjectionListeners == null) {
            return;
        }
        for (int i = 0; i < this.mProjectionListeners.size(); i++) {
            int listenerProjectionType = this.mProjectionListeners.keyAt(i);
            if ((changedProjectionType & listenerProjectionType) != 0) {
                android.os.RemoteCallbackList<android.app.IOnProjectionStateChangedListener> listeners = this.mProjectionListeners.valueAt(i);
                java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>();
                int activeProjectionTypes = populateWithRelevantActivePackageNames(listenerProjectionType, packageNames);
                int listenerCount = listeners.beginBroadcast();
                for (int j = 0; j < listenerCount; j++) {
                    try {
                        listeners.getBroadcastItem(j).onProjectionStateChanged(activeProjectionTypes, packageNames);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Failed a call to onProjectionStateChanged().");
                    }
                }
                listeners.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCustomTimeUpdated(int user) {
        persistNightMode(user);
        if (this.mNightMode.get() != 3) {
            return;
        }
        if (shouldApplyAutomaticChangesImmediately()) {
            unregisterDeviceInactiveListenerLocked();
            updateLocked(0, 0);
            if (LOG) {
                android.util.Slog.d(TAG, "onCustomTimeUpdated updateLocked now");
                return;
            }
            return;
        }
        registerDeviceInactiveListenerLocked();
        if (LOG) {
            android.util.Slog.d(TAG, "onCustomTimeUpdated wait screen off");
        }
    }

    void dumpImpl(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("Current UI Mode Service state:");
            pw.print("  mDockState=");
            pw.print(this.mDockState);
            pw.print(" mLastBroadcastState=");
            pw.println(this.mLastBroadcastState);
            pw.print(" mStartDreamImmediatelyOnDock=");
            pw.print(this.mStartDreamImmediatelyOnDock);
            pw.print("  mNightMode=");
            pw.print(this.mNightMode.get());
            pw.print(" (");
            pw.print(com.android.server.UiModeManagerService.Shell.nightModeToStr(this.mNightMode.get(), this.mNightModeCustomType));
            pw.print(") ");
            pw.print(" mOverrideOn/Off=");
            pw.print(this.mOverrideNightModeOn);
            pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            pw.print(this.mOverrideNightModeOff);
            pw.print("  mAttentionModeThemeOverlay=");
            pw.print(this.mAttentionModeThemeOverlay);
            pw.print(" mNightModeLocked=");
            pw.println(this.mNightModeLocked);
            pw.print("  mCarModeEnabled=");
            pw.print(this.mCarModeEnabled);
            pw.print(" (carModeApps=");
            for (java.util.Map.Entry<java.lang.Integer, java.lang.String> entry : this.mCarModePackagePriority.entrySet()) {
                pw.print(entry.getKey());
                pw.print(":");
                pw.print(entry.getValue());
                pw.print(" ");
            }
            pw.println("");
            pw.print(" mWaitForDeviceInactive=");
            pw.print(this.mWaitForDeviceInactive);
            pw.print(" mComputedNightMode=");
            pw.print(this.mComputedNightMode);
            pw.print(" customStart=");
            pw.print(this.mCustomAutoNightModeStartMilliseconds);
            pw.print(" customEnd");
            pw.print(this.mCustomAutoNightModeEndMilliseconds);
            pw.print(" mCarModeEnableFlags=");
            pw.print(this.mCarModeEnableFlags);
            pw.print(" mEnableCarDockLaunch=");
            pw.println(this.mEnableCarDockLaunch);
            pw.print("  mCurUiMode=0x");
            pw.print(java.lang.Integer.toHexString(this.mCurUiMode));
            pw.print(" mUiModeLocked=");
            pw.print(this.mUiModeLocked);
            pw.print(" mSetUiMode=0x");
            pw.println(java.lang.Integer.toHexString(this.mSetUiMode));
            pw.print("  mHoldingConfiguration=");
            pw.print(this.mHoldingConfiguration);
            pw.print(" mSystemReady=");
            pw.println(this.mSystemReady);
            if (this.mTwilightManager != null) {
                pw.print("  mTwilightService.getLastTwilightState()=");
                pw.println(this.mTwilightManager.getLastTwilightState());
                this.mUmssExt.darkModeDumpUiModeManagerServiceMessage(pw, this.mTwilightManager);
            }
        }
    }

    void setCarModeLocked(boolean enabled, int flags, int priority, java.lang.String packageName) {
        if (enabled) {
            enableCarMode(priority, packageName);
        } else {
            disableCarMode(flags, priority, packageName);
        }
        boolean isCarModeNowEnabled = isCarModeEnabled();
        if (this.mCarModeEnabled != isCarModeNowEnabled) {
            this.mCarModeEnabled = isCarModeNowEnabled;
            if (!isCarModeNowEnabled) {
                android.content.Context context = getContext();
                updateNightModeFromSettingsLocked(context, context.getResources(), android.os.UserHandle.getCallingUserId());
            }
        }
        this.mCarModeEnableFlags = flags;
    }

    private void disableCarMode(int flags, int priority, java.lang.String packageName) {
        boolean isChangeAllowed = true;
        boolean isDisableAll = (flags & 2) != 0;
        boolean isPriorityTracked = this.mCarModePackagePriority.keySet().contains(java.lang.Integer.valueOf(priority));
        boolean isDefaultPriority = priority == 0;
        if (!isDefaultPriority && ((!isPriorityTracked || !this.mCarModePackagePriority.get(java.lang.Integer.valueOf(priority)).equals(packageName)) && !isDisableAll)) {
            isChangeAllowed = false;
        }
        if (isChangeAllowed) {
            android.util.Slog.d(TAG, "disableCarMode: disabling, priority=" + priority + ", packageName=" + packageName);
            if (isDisableAll) {
                java.util.Set<java.util.Map.Entry<java.lang.Integer, java.lang.String>> entries = new android.util.ArraySet<>(this.mCarModePackagePriority.entrySet());
                this.mCarModePackagePriority.clear();
                for (java.util.Map.Entry<java.lang.Integer, java.lang.String> entry : entries) {
                    notifyCarModeDisabled(entry.getKey().intValue(), entry.getValue());
                }
                return;
            }
            this.mCarModePackagePriority.remove(java.lang.Integer.valueOf(priority));
            notifyCarModeDisabled(priority, packageName);
        }
    }

    private void enableCarMode(int priority, java.lang.String packageName) {
        boolean isPriorityTracked = this.mCarModePackagePriority.containsKey(java.lang.Integer.valueOf(priority));
        boolean isPackagePresent = this.mCarModePackagePriority.containsValue(packageName);
        if (!isPriorityTracked && !isPackagePresent) {
            android.util.Slog.d(TAG, "enableCarMode: enabled at priority=" + priority + ", packageName=" + packageName);
            this.mCarModePackagePriority.put(java.lang.Integer.valueOf(priority), packageName);
            notifyCarModeEnabled(priority, packageName);
            return;
        }
        android.util.Slog.d(TAG, "enableCarMode: car mode at priority " + priority + " already enabled.");
    }

    private void notifyCarModeEnabled(int priority, java.lang.String packageName) {
        android.content.Intent intent = new android.content.Intent("android.app.action.ENTER_CAR_MODE_PRIORITIZED");
        intent.putExtra("android.app.extra.CALLING_PACKAGE", packageName);
        intent.putExtra("android.app.extra.PRIORITY", priority);
        getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.HANDLE_CAR_MODE_CHANGES");
    }

    private void notifyCarModeDisabled(int priority, java.lang.String packageName) {
        android.content.Intent intent = new android.content.Intent("android.app.action.EXIT_CAR_MODE_PRIORITIZED");
        intent.putExtra("android.app.extra.CALLING_PACKAGE", packageName);
        intent.putExtra("android.app.extra.PRIORITY", priority);
        getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.HANDLE_CAR_MODE_CHANGES");
    }

    private boolean isCarModeEnabled() {
        return this.mCarModePackagePriority.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDockState(int newState) {
        synchronized (this.mLock) {
            if (newState != this.mDockState) {
                this.mDockState = newState;
                setCarModeLocked(this.mDockState == 2, 0, 0, "");
                if (this.mSystemReady) {
                    updateLocked(1, 0);
                }
            }
        }
    }

    private static boolean isDeskDockState(int state) {
        switch (state) {
            case 1:
            case 3:
            case 4:
                return true;
            case 2:
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistNightMode(int user) {
        if (this.mCarModeEnabled || this.mCar) {
            return;
        }
        android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode", this.mNightMode.get(), user);
        android.provider.Settings.Secure.putLongForUser(getContext().getContentResolver(), "ui_night_mode_custom_type", this.mNightModeCustomType, user);
        android.provider.Settings.Secure.putLongForUser(getContext().getContentResolver(), "dark_theme_custom_start_time", this.mCustomAutoNightModeStartMilliseconds.toNanoOfDay() / 1000, user);
        android.provider.Settings.Secure.putLongForUser(getContext().getContentResolver(), "dark_theme_custom_end_time", this.mCustomAutoNightModeEndMilliseconds.toNanoOfDay() / 1000, user);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistNightModeOverrides(int i) {
        if (this.mCarModeEnabled || this.mCar) {
            return;
        }
        android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode_override_on", this.mOverrideNightModeOn ? 1 : 0, i);
        android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode_override_off", this.mOverrideNightModeOff ? 1 : 0, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfigurationLocked() {
        int uiMode;
        int uiMode2 = this.mDefaultUiModeType;
        if (!this.mUiModeLocked) {
            if (this.mTelevision) {
                uiMode2 = 4;
            } else if (this.mWatch) {
                uiMode2 = 6;
            } else if (this.mCarModeEnabled) {
                uiMode2 = 3;
            } else if (isDeskDockState(this.mDockState)) {
                uiMode2 = 2;
            } else if (this.mVrHeadset) {
                uiMode2 = 7;
            }
        }
        if (this.mNightMode.get() == 2 || this.mNightMode.get() == 1) {
            updateComputedNightModeLocked(this.mNightMode.get() == 2);
        }
        if (this.mNightMode.get() == 0) {
            boolean activateNightMode = this.mComputedNightMode;
            if (this.mTwilightManager != null) {
                this.mTwilightManager.registerListener(this.mTwilightListener, this.mHandler);
                com.android.server.twilight.TwilightState lastState = this.mTwilightManager.getLastTwilightState();
                activateNightMode = lastState == null ? this.mComputedNightMode : lastState.isNight();
                if (LOG) {
                    android.util.Slog.d(TAG, "updateConfigurationLocked-->lastState: " + lastState + " mComputedNightMode: " + this.mComputedNightMode + " activateNightMode: " + activateNightMode + " time: " + java.time.LocalTime.now().toString());
                }
            }
            updateComputedNightModeLocked(activateNightMode);
        } else if (this.mTwilightManager != null) {
            this.mTwilightManager.unregisterListener(this.mTwilightListener);
        }
        if (this.mNightMode.get() == 3) {
            if (this.mNightModeCustomType == 1) {
                updateComputedNightModeLocked(this.mLastBedtimeRequestedNightMode);
            } else {
                registerTimeChangeEvent();
                boolean activate = computeCustomNightMode();
                updateComputedNightModeLocked(activate);
                if (LOG) {
                    android.util.Slog.d(TAG, "updateConfigurationLocked-->activate: " + activate + " time: " + java.time.LocalTime.now().toString());
                }
                scheduleNextCustomTimeListener();
            }
        } else {
            unregisterTimeChangeEvent();
        }
        if (this.mPowerSave && !this.mCarModeEnabled && !this.mCar) {
            uiMode = (uiMode2 & (-17)) | 32;
        } else {
            uiMode = getComputedUiModeConfiguration(uiMode2);
        }
        if (LOG) {
            android.util.Slog.d(TAG, "updateConfigurationLocked: mDockState=" + this.mDockState + "; mCarMode=" + this.mCarModeEnabled + "; mNightMode=" + this.mNightMode + "; mNightModeCustomType=" + this.mNightModeCustomType + "; uiMode=" + uiMode);
        }
        this.mCurUiMode = uiMode;
        if (this.mHoldingConfiguration) {
            return;
        }
        if (!this.mWaitForDeviceInactive || this.mPowerSave) {
            this.mConfiguration.uiMode = uiMode;
        }
    }

    private int getComputedUiModeConfiguration(int uiMode) {
        if (this.mUmssExt.darkModeIsSuperSaveMode()) {
            return this.mUmssExt.darkModeGetSuperSaveUiMode(uiMode);
        }
        return (uiMode | (this.mComputedNightMode ? 32 : 16)) & (this.mComputedNightMode ? -17 : -33);
    }

    private boolean computeCustomNightMode() {
        return android.util.TimeUtils.isTimeBetween(java.time.LocalTime.now(), this.mCustomAutoNightModeStartMilliseconds, this.mCustomAutoNightModeEndMilliseconds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyConfigurationExternallyLocked() {
        if (this.mSetUiMode != this.mConfiguration.uiMode) {
            if (LOG) {
                android.util.Slog.d(TAG, "change uiMode to-->" + this.mConfiguration.uiMode);
            }
            this.mUmssExt.darkModeNightModeChange(this, this.mConfiguration.uiMode, this.mSetUiMode);
            this.mUmssExt.fontUpdateConfigurationInUIMode(getContext(), this.mConfiguration, -2);
            this.mSetUiMode = this.mConfiguration.uiMode;
            this.mUmssExt.notifyFlingerUiMode(this.mSetUiMode);
            this.mWindowManager.clearSnapshotCache();
            this.mUmssExt.darkModeSetValueForState(getContext(), -2, this.mSetUiMode);
            try {
                android.app.ActivityTaskManager.getService().updateConfiguration(this.mConfiguration);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failure communicating with activity manager", e);
            } catch (java.lang.SecurityException e2) {
                android.util.Slog.e(TAG, "Activity does not have the ", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldApplyAutomaticChangesImmediately() {
        return this.mCar || !this.mPowerManager.isInteractive() || this.mNightModeCustomType == 1 || this.mDreamManagerInternal.isDreaming();
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [java.time.ZonedDateTime] */
    private void scheduleNextCustomTimeListener() {
        java.time.LocalDateTime next;
        cancelCustomAlarm();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        boolean active = computeCustomNightMode();
        if (active) {
            next = getDateTimeAfter(this.mCustomAutoNightModeEndMilliseconds, now);
        } else {
            next = getDateTimeAfter(this.mCustomAutoNightModeStartMilliseconds, now);
        }
        long millis = next.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.mAlarmManager.setExact(1, millis, TAG, this.mCustomTimeListener, null);
        if (LOG) {
            android.util.Slog.d(TAG, "next customTime alarm-->" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.lang.Long.valueOf(millis)));
        }
    }

    private java.time.LocalDateTime getDateTimeAfter(java.time.LocalTime localTime, java.time.LocalDateTime compareTime) {
        java.time.LocalDateTime ldt = java.time.LocalDateTime.of(compareTime.toLocalDate(), localTime);
        return ldt.isBefore(compareTime) ? ldt.plusDays(1L) : ldt;
    }

    void updateLocked(int enableFlags, int disableFlags) {
        java.lang.String action = null;
        java.lang.String oldAction = null;
        if (this.mLastBroadcastState == 2) {
            adjustStatusBarCarModeLocked();
            oldAction = android.app.UiModeManager.ACTION_EXIT_CAR_MODE;
        } else if (isDeskDockState(this.mLastBroadcastState)) {
            oldAction = android.app.UiModeManager.ACTION_EXIT_DESK_MODE;
        }
        boolean z = false;
        if (this.mCarModeEnabled) {
            if (this.mLastBroadcastState != 2) {
                adjustStatusBarCarModeLocked();
                if (oldAction != null) {
                    sendForegroundBroadcastToAllUsers(oldAction);
                }
                this.mLastBroadcastState = 2;
                action = android.app.UiModeManager.ACTION_ENTER_CAR_MODE;
            }
        } else if (isDeskDockState(this.mDockState)) {
            if (!isDeskDockState(this.mLastBroadcastState)) {
                if (oldAction != null) {
                    sendForegroundBroadcastToAllUsers(oldAction);
                }
                this.mLastBroadcastState = this.mDockState;
                action = android.app.UiModeManager.ACTION_ENTER_DESK_MODE;
            }
        } else {
            this.mLastBroadcastState = 0;
            action = oldAction;
        }
        if (action != null) {
            if (LOG) {
                android.util.Slog.v(TAG, java.lang.String.format("updateLocked: preparing broadcast: action=%s enable=0x%08x disable=0x%08x", action, java.lang.Integer.valueOf(enableFlags), java.lang.Integer.valueOf(disableFlags)));
            }
            android.content.Intent intent = new android.content.Intent(action);
            intent.putExtra("enableFlags", enableFlags);
            intent.putExtra("disableFlags", disableFlags);
            intent.addFlags(268435456);
            getContext().sendOrderedBroadcastAsUser(intent, android.os.UserHandle.CURRENT, null, this.mResultReceiver, null, -1, null, null);
            this.mHoldingConfiguration = true;
            updateConfigurationLocked();
        } else {
            java.lang.String category = null;
            if (this.mCarModeEnabled) {
                if (this.mEnableCarDockLaunch && (enableFlags & 1) != 0) {
                    category = "android.intent.category.CAR_DOCK";
                }
            } else if (isDeskDockState(this.mDockState)) {
                if ((enableFlags & 1) != 0) {
                    category = "android.intent.category.DESK_DOCK";
                }
            } else if ((disableFlags & 1) != 0) {
                category = "android.intent.category.HOME";
            }
            if (LOG) {
                android.util.Slog.v(TAG, "updateLocked: null action, mDockState=" + this.mDockState + ", category=" + category);
            }
            sendConfigurationAndStartDreamOrDockAppLocked(category);
        }
        if (this.mCharging && ((this.mCarModeEnabled && this.mCarModeKeepsScreenOn && (this.mCarModeEnableFlags & 2) == 0) || (this.mCurUiMode == 2 && this.mDeskModeKeepsScreenOn))) {
            z = true;
        }
        boolean keepScreenOn = z;
        if (keepScreenOn != this.mWakeLock.isHeld()) {
            if (keepScreenOn) {
                this.mWakeLock.acquire();
            } else {
                this.mWakeLock.release();
            }
        }
    }

    private void sendForegroundBroadcastToAllUsers(java.lang.String action) {
        getContext().sendBroadcastAsUser(new android.content.Intent(action).addFlags(268435456), android.os.UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAfterBroadcastLocked(java.lang.String action, int enableFlags, int disableFlags) {
        java.lang.String category = null;
        if (android.app.UiModeManager.ACTION_ENTER_CAR_MODE.equals(action)) {
            if (this.mEnableCarDockLaunch && (enableFlags & 1) != 0) {
                category = "android.intent.category.CAR_DOCK";
            }
        } else if (android.app.UiModeManager.ACTION_ENTER_DESK_MODE.equals(action)) {
            if ((enableFlags & 1) != 0) {
                category = "android.intent.category.DESK_DOCK";
            }
        } else if ((disableFlags & 1) != 0) {
            category = "android.intent.category.HOME";
        }
        if (LOG) {
            android.util.Slog.v(TAG, java.lang.String.format("Handling broadcast result for action %s: enable=0x%08x, disable=0x%08x, category=%s", action, java.lang.Integer.valueOf(enableFlags), java.lang.Integer.valueOf(disableFlags), category));
        }
        sendConfigurationAndStartDreamOrDockAppLocked(category);
    }

    private void sendConfigurationAndStartDreamOrDockAppLocked(java.lang.String category) {
        android.content.Intent homeIntent;
        this.mHoldingConfiguration = false;
        updateConfigurationLocked();
        boolean dockAppStarted = false;
        if (category != null) {
            android.content.Intent homeIntent2 = buildHomeIntent(category);
            if (android.service.dreams.Sandman.shouldStartDockApp(getContext(), homeIntent2)) {
                try {
                    homeIntent = homeIntent2;
                    try {
                        int result = android.app.ActivityTaskManager.getService().startActivityWithConfig((android.app.IApplicationThread) null, getContext().getBasePackageName(), getContext().getAttributionTag(), homeIntent2, (java.lang.String) null, (android.os.IBinder) null, (java.lang.String) null, 0, 0, this.mConfiguration, (android.os.Bundle) null, -2);
                        if (android.app.ActivityManager.isStartResultSuccessful(result)) {
                            dockAppStarted = true;
                        } else if (result != -91) {
                            android.util.Slog.e(TAG, "Could not start dock app: " + homeIntent + ", startActivityWithConfig result " + result);
                        }
                    } catch (android.os.RemoteException e) {
                        ex = e;
                        android.util.Slog.e(TAG, "Could not start dock app: " + homeIntent, ex);
                    }
                } catch (android.os.RemoteException e2) {
                    ex = e2;
                    homeIntent = homeIntent2;
                }
            }
        }
        applyConfigurationExternallyLocked();
        boolean dreamsSuppressed = this.mDreamsDisabledByAmbientModeSuppression && this.mLocalPowerManager.isAmbientDisplaySuppressed();
        if (category != null && !dockAppStarted && !dreamsSuppressed) {
            if (this.mStartDreamImmediatelyOnDock || this.mWindowManager.isKeyguardShowingAndNotOccluded() || !this.mPowerManager.isInteractive()) {
                this.mInjector.startDreamWhenDockedIfAppropriate(getContext());
            }
        }
    }

    private void adjustStatusBarCarModeLocked() {
        int i;
        android.content.Context context = getContext();
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (android.app.StatusBarManager) context.getSystemService("statusbar");
        }
        if (this.mStatusBarManager != null) {
            android.app.StatusBarManager statusBarManager = this.mStatusBarManager;
            if (this.mCarModeEnabled) {
                i = 524288;
            } else {
                i = 0;
            }
            statusBarManager.disable(i);
        }
        if (this.mNotificationManager == null) {
            this.mNotificationManager = (android.app.NotificationManager) context.getSystemService("notification");
        }
        if (this.mNotificationManager != null) {
            if (this.mCarModeEnabled) {
                android.content.Intent carModeOffIntent = new android.content.Intent(context, (java.lang.Class<?>) com.android.internal.app.DisableCarModeActivity.class);
                android.app.Notification.Builder n = new android.app.Notification.Builder(context, com.android.internal.notification.SystemNotificationChannels.CAR_MODE).setSmallIcon(android.R.drawable.search_dropdown_dark).setDefaults(4).setOngoing(true).setWhen(0L).setColor(context.getColor(android.R.color.system_notification_accent_color)).setContentTitle(context.getString(android.R.string.checked)).setContentText(context.getString(android.R.string.cfTemplateRegisteredTime)).setContentIntent(android.app.PendingIntent.getActivityAsUser(context, 0, carModeOffIntent, 33554432, null, android.os.UserHandle.CURRENT));
                this.mNotificationManager.notifyAsUser(null, 10, n.build(), android.os.UserHandle.ALL);
                return;
            }
            this.mNotificationManager.cancelAsUser(null, 10, android.os.UserHandle.ALL);
        }
    }

    private void updateComputedNightModeLocked(boolean activate) {
        boolean autoFirst = this.mUmssExt.darkModeGetAutoFirst();
        boolean z = false;
        if (this.mNightMode.get() != 2 && this.mNightMode.get() != 1) {
            if (this.mOverrideNightModeOn && !activate) {
                this.mComputedNightMode = true;
                return;
            } else if (this.mOverrideNightModeOff && activate) {
                this.mComputedNightMode = false;
                return;
            }
        }
        if (android.app.Flags.modesApi()) {
            switch (this.mAttentionModeThemeOverlay) {
                case 1001:
                    z = true;
                    break;
                case 1002:
                    break;
                default:
                    z = activate;
                    break;
            }
            this.mComputedNightMode = z;
        } else {
            this.mComputedNightMode = activate;
        }
        this.mComputedNightMode = this.mUmssExt.darkModeOverrideComputedNightMode(this.mNightMode.get(), autoFirst, this.mComputedNightMode);
        if (this.mNightMode.get() != 0 || (this.mTwilightManager != null && this.mTwilightManager.getLastTwilightState() != null)) {
            resetNightModeOverrideLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean resetNightModeOverrideLocked() {
        if (!this.mOverrideNightModeOff && !this.mOverrideNightModeOn) {
            return false;
        }
        this.mOverrideNightModeOff = false;
        this.mOverrideNightModeOn = false;
        persistNightModeOverrides(this.mOverrideNightModeUser);
        this.mOverrideNightModeUser = 0;
        return true;
    }

    private void registerVrStateListener() {
        android.service.vr.IVrManager vrManager = android.service.vr.IVrManager.Stub.asInterface(android.os.ServiceManager.getService("vrmanager"));
        if (vrManager != null) {
            try {
                vrManager.registerListener(this.mVrStateCallbacks);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to register VR mode state listener: " + e);
            }
        }
    }

    private static class Shell extends android.os.ShellCommand {
        public static final java.lang.String NIGHT_MODE_STR_AUTO = "auto";
        public static final java.lang.String NIGHT_MODE_STR_CUSTOM_BEDTIME = "custom_bedtime";
        public static final java.lang.String NIGHT_MODE_STR_CUSTOM_SCHEDULE = "custom_schedule";
        public static final java.lang.String NIGHT_MODE_STR_NO = "no";
        public static final java.lang.String NIGHT_MODE_STR_UNKNOWN = "unknown";
        public static final java.lang.String NIGHT_MODE_STR_YES = "yes";
        private final android.app.IUiModeManager mInterface;

        Shell(android.app.IUiModeManager iface) {
            this.mInterface = iface;
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("UiModeManager service (uimode) commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println("  night [yes|no|auto|custom_schedule|custom_bedtime]");
            pw.println("    Set or read night mode.");
            pw.println("  car [yes|no]");
            pw.println("    Set or read car mode.");
            pw.println("  time [start|end] <ISO time>");
            pw.println("    Set custom start/end schedule time (night mode must be set to custom to apply).");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:8:0x000f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onCommand(java.lang.String r6) {
            /*
                r5 = this;
                if (r6 != 0) goto L7
                int r0 = r5.handleDefaultCommands(r6)
                return r0
            L7:
                r0 = -1
                int r1 = r6.hashCode()     // Catch: android.os.RemoteException -> L49
                switch(r1) {
                    case 98260: goto L26;
                    case 3560141: goto L1b;
                    case 104817688: goto L10;
                    default: goto Lf;
                }     // Catch: android.os.RemoteException -> L49
            Lf:
                goto L30
            L10:
                java.lang.String r1 = "night"
                boolean r1 = r6.equals(r1)     // Catch: android.os.RemoteException -> L49
                if (r1 == 0) goto Lf
                r1 = 0
                goto L31
            L1b:
                java.lang.String r1 = "time"
                boolean r1 = r6.equals(r1)     // Catch: android.os.RemoteException -> L49
                if (r1 == 0) goto Lf
                r1 = 2
                goto L31
            L26:
                java.lang.String r1 = "car"
                boolean r1 = r6.equals(r1)     // Catch: android.os.RemoteException -> L49
                if (r1 == 0) goto Lf
                r1 = 1
                goto L31
            L30:
                r1 = r0
            L31:
                switch(r1) {
                    case 0: goto L43;
                    case 1: goto L3e;
                    case 2: goto L39;
                    default: goto L34;
                }     // Catch: android.os.RemoteException -> L49
            L34:
                int r0 = r5.handleDefaultCommands(r6)     // Catch: android.os.RemoteException -> L49
                goto L48
            L39:
                int r0 = r5.handleCustomTime()     // Catch: android.os.RemoteException -> L49
                return r0
            L3e:
                int r0 = r5.handleCarMode()     // Catch: android.os.RemoteException -> L49
                return r0
            L43:
                int r0 = r5.handleNightMode()     // Catch: android.os.RemoteException -> L49
                return r0
            L48:
                return r0
            L49:
                r1 = move-exception
                java.io.PrintWriter r2 = r5.getErrPrintWriter()
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "Remote exception: "
                java.lang.StringBuilder r3 = r3.append(r4)
                java.lang.StringBuilder r3 = r3.append(r1)
                java.lang.String r3 = r3.toString()
                r2.println(r3)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.UiModeManagerService.Shell.onCommand(java.lang.String):int");
        }

        private int handleCustomTime() throws android.os.RemoteException {
            byte b;
            java.lang.String modeStr = getNextArg();
            if (modeStr == null) {
                printCustomTime();
                return 0;
            }
            switch (modeStr.hashCode()) {
                case 100571:
                    b = !modeStr.equals("end") ? (byte) -1 : (byte) 1;
                    break;
                case 109757538:
                    b = !modeStr.equals("start") ? (byte) -1 : (byte) 0;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    java.lang.String start = getNextArg();
                    this.mInterface.setCustomNightModeStart(com.android.server.UiModeManagerService.toMilliSeconds(java.time.LocalTime.parse(start)));
                    return 0;
                case 1:
                    java.lang.String end = getNextArg();
                    this.mInterface.setCustomNightModeEnd(com.android.server.UiModeManagerService.toMilliSeconds(java.time.LocalTime.parse(end)));
                    return 0;
                default:
                    getErrPrintWriter().println("command must be in [start|end]");
                    return -1;
            }
        }

        private void printCustomTime() throws android.os.RemoteException {
            getOutPrintWriter().println("start " + com.android.server.UiModeManagerService.fromMilliseconds(this.mInterface.getCustomNightModeStart()).toString());
            getOutPrintWriter().println("end " + com.android.server.UiModeManagerService.fromMilliseconds(this.mInterface.getCustomNightModeEnd()).toString());
        }

        private int handleNightMode() throws android.os.RemoteException {
            java.io.PrintWriter err = getErrPrintWriter();
            java.lang.String modeStr = getNextArg();
            if (modeStr == null) {
                printCurrentNightMode();
                return 0;
            }
            int mode = strToNightMode(modeStr);
            int customType = strToNightModeCustomType(modeStr);
            if (mode >= 0) {
                this.mInterface.setNightMode(mode);
                if (mode == 3) {
                    this.mInterface.setNightModeCustomType(customType);
                }
                printCurrentNightMode();
                return 0;
            }
            err.println("Error: mode must be 'yes', 'no', or 'auto', or 'custom_schedule', or 'custom_bedtime'");
            return -1;
        }

        private void printCurrentNightMode() throws android.os.RemoteException {
            java.io.PrintWriter pw = getOutPrintWriter();
            int currMode = this.mInterface.getNightMode();
            int customType = this.mInterface.getNightModeCustomType();
            java.lang.String currModeStr = nightModeToStr(currMode, customType);
            pw.println("Night mode: " + currModeStr);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static java.lang.String nightModeToStr(int mode, int customType) {
            switch (mode) {
                case 0:
                    return "auto";
                case 1:
                    return NIGHT_MODE_STR_NO;
                case 2:
                    return NIGHT_MODE_STR_YES;
                case 3:
                    if (customType == 0) {
                        return NIGHT_MODE_STR_CUSTOM_SCHEDULE;
                    }
                    if (customType == 1) {
                        return NIGHT_MODE_STR_CUSTOM_BEDTIME;
                    }
                    return "unknown";
                default:
                    return "unknown";
            }
        }

        private static int strToNightMode(java.lang.String modeStr) {
            byte b;
            switch (modeStr.hashCode()) {
                case -757868544:
                    b = !modeStr.equals(NIGHT_MODE_STR_CUSTOM_BEDTIME) ? (byte) -1 : (byte) 4;
                    break;
                case 3521:
                    b = !modeStr.equals(NIGHT_MODE_STR_NO) ? (byte) -1 : (byte) 1;
                    break;
                case 119527:
                    b = !modeStr.equals(NIGHT_MODE_STR_YES) ? (byte) -1 : (byte) 0;
                    break;
                case 3005871:
                    b = !modeStr.equals("auto") ? (byte) -1 : (byte) 2;
                    break;
                case 164399013:
                    b = !modeStr.equals(NIGHT_MODE_STR_CUSTOM_SCHEDULE) ? (byte) -1 : (byte) 3;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return 2;
                case 1:
                    return 1;
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 3;
                default:
                    return -1;
            }
        }

        private static int strToNightModeCustomType(java.lang.String customTypeStr) {
            byte b;
            switch (customTypeStr.hashCode()) {
                case -757868544:
                    b = !customTypeStr.equals(NIGHT_MODE_STR_CUSTOM_BEDTIME) ? (byte) -1 : (byte) 0;
                    break;
                case 164399013:
                    b = !customTypeStr.equals(NIGHT_MODE_STR_CUSTOM_SCHEDULE) ? (byte) -1 : (byte) 1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                default:
                    return -1;
            }
        }

        private int handleCarMode() throws android.os.RemoteException {
            java.io.PrintWriter err = getErrPrintWriter();
            java.lang.String modeStr = getNextArg();
            if (modeStr == null) {
                printCurrentCarMode();
                return 0;
            }
            if (modeStr.equals(NIGHT_MODE_STR_YES)) {
                this.mInterface.enableCarMode(0, 0, "");
                printCurrentCarMode();
                return 0;
            }
            if (modeStr.equals(NIGHT_MODE_STR_NO)) {
                this.mInterface.disableCarMode(0);
                printCurrentCarMode();
                return 0;
            }
            err.println("Error: mode must be 'yes', or 'no'");
            return -1;
        }

        private void printCurrentCarMode() throws android.os.RemoteException {
            java.io.PrintWriter pw = getOutPrintWriter();
            int currMode = this.mInterface.getCurrentModeType();
            pw.println("Car mode: " + (currMode == 3 ? NIGHT_MODE_STR_YES : NIGHT_MODE_STR_NO));
        }
    }

    public final class LocalService extends com.android.server.UiModeManagerInternal {
        public LocalService() {
        }

        @Override // com.android.server.UiModeManagerInternal
        public boolean isNightMode() {
            boolean isIt;
            synchronized (com.android.server.UiModeManagerService.this.mLock) {
                isIt = (com.android.server.UiModeManagerService.this.mConfiguration.uiMode & 32) != 0;
                if (com.android.server.UiModeManagerService.LOG) {
                    android.util.Slog.d(com.android.server.UiModeManagerService.TAG, "LocalService.isNightMode(): mNightMode=" + com.android.server.UiModeManagerService.this.mNightMode + "; mComputedNightMode=" + com.android.server.UiModeManagerService.this.mComputedNightMode + "; uiMode=" + com.android.server.UiModeManagerService.this.mConfiguration.uiMode + "; isIt=" + isIt);
                }
            }
            return isIt;
        }
    }

    public static class Injector {
        public int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        public void startDreamWhenDockedIfAppropriate(android.content.Context context) {
            android.service.dreams.Sandman.startDreamWhenDockedIfAppropriate(context);
        }
    }

    public com.android.server.IUiModeManagerServiceWrapper getWrapper() {
        return this.mUiModemsWrapper;
    }

    private class UiModeManagerServiceWrapper implements com.android.server.IUiModeManagerServiceWrapper {
        private UiModeManagerServiceWrapper() {
        }

        @Override // com.android.server.IUiModeManagerServiceWrapper
        public void unregisterScreenOffEvent() {
            com.android.server.UiModeManagerService.this.unregisterDeviceInactiveListenerLocked();
        }
    }
}
