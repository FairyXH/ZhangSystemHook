package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
public final class DreamManagerService extends com.android.server.SystemService {
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String DOZE_WAKE_LOCK_TAG = "dream:doze";
    private static final int DREAM_DISABLED = 0;
    private static final int DREAM_ON_CHARGE = 2;
    private static final int DREAM_ON_DOCK = 1;
    private static final int DREAM_ON_DOCK_OR_CHARGE = 3;
    private static final java.lang.String DREAM_WAKE_LOCK_TAG = "dream:dream";
    private static final java.lang.String TAG = "DreamManagerService";
    private final com.android.server.wm.ActivityInterceptorCallback mActivityInterceptorCallback;
    private final android.content.ComponentName mAmbientDisplayComponent;
    private final com.android.server.wm.ActivityTaskManagerInternal mAtmInternal;
    private final android.os.BatteryManagerInternal mBatteryManagerInternal;
    private final android.content.BroadcastReceiver mChargingReceiver;
    private final android.content.Context mContext;
    private final com.android.server.dreams.DreamController mController;
    private final com.android.server.dreams.DreamController.Listener mControllerListener;
    private com.android.server.dreams.DreamManagerService.DreamRecord mCurrentDream;
    private final boolean mDismissDreamOnActivityStart;
    private final android.content.BroadcastReceiver mDockStateReceiver;
    private final android.hardware.display.AmbientDisplayConfiguration mDozeConfig;
    private final android.database.ContentObserver mDozeEnabledObserver;
    private final android.os.PowerManager.WakeLock mDozeWakeLock;
    private final java.util.concurrent.CopyOnWriteArrayList<android.service.dreams.DreamManagerInternal.DreamManagerStateListener> mDreamManagerStateListeners;
    private android.content.ComponentName mDreamOverlayServiceName;
    private final com.android.server.dreams.DreamUiEventLogger mDreamUiEventLogger;
    private final boolean mDreamsActivatedOnChargeByDefault;
    private final boolean mDreamsActivatedOnDockByDefault;
    private final boolean mDreamsDisabledByAmbientModeSuppressionConfig;
    private final boolean mDreamsEnabledByDefaultConfig;
    private boolean mDreamsEnabledSetting;
    private final boolean mDreamsOnlyEnabledForDockUser;
    private boolean mForceAmbientDisplayEnabled;
    private final android.os.Handler mHandler;
    private boolean mIsCharging;
    private boolean mIsDocked;
    private final boolean mKeepDreamingWhenUnpluggingDefault;
    private final java.lang.Object mLock;
    private final android.content.pm.PackageManagerInternal mPmInternal;
    private final android.os.PowerManager mPowerManager;
    private final android.os.PowerManagerInternal mPowerManagerInternal;
    private com.android.server.dreams.DreamManagerService.SettingsObserver mSettingsObserver;
    private android.content.ComponentName mSystemDreamComponent;
    private final java.lang.Runnable mSystemPropertiesChanged;
    private final com.android.internal.logging.UiEventLogger mUiEventLogger;
    private final android.os.UserManager mUserManager;
    private int mWhenToDream;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface WhenToDream {
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            com.android.server.dreams.DreamManagerService.this.updateWhenToDreamSettings();
        }
    }

    public DreamManagerService(android.content.Context context) {
        this(context, new com.android.server.dreams.DreamManagerService.DreamHandler(com.android.server.FgThread.get().getLooper()));
    }

    DreamManagerService(android.content.Context context, android.os.Handler handler) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mDreamManagerStateListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mActivityInterceptorCallback = new com.android.server.wm.ActivityInterceptorCallback() { // from class: com.android.server.dreams.DreamManagerService.1
            @Override // com.android.server.wm.ActivityInterceptorCallback
            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptResult onInterceptActivityLaunch(com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
                return null;
            }

            @Override // com.android.server.wm.ActivityInterceptorCallback
            public void onActivityLaunched(android.app.TaskInfo taskInfo, android.content.pm.ActivityInfo activityInfo, com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
                int activityType = taskInfo.getActivityType();
                boolean shouldRequestAwaken = false;
                boolean activityAllowed = activityType == 2 || activityType == 5 || activityType == 4;
                synchronized (com.android.server.dreams.DreamManagerService.this.mLock) {
                    if (com.android.server.dreams.DreamManagerService.this.mCurrentDream != null && !com.android.server.dreams.DreamManagerService.this.mCurrentDream.isWaking && !com.android.server.dreams.DreamManagerService.this.mCurrentDream.isDozing && !activityAllowed) {
                        shouldRequestAwaken = true;
                    }
                }
                if (shouldRequestAwaken) {
                    com.android.server.dreams.DreamManagerService.this.requestAwakenInternal("stopping dream due to activity start: " + activityInfo.name);
                }
            }
        };
        this.mChargingReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.dreams.DreamManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (com.android.server.dreams.Flags.useBatteryChangedBroadcast()) {
                    com.android.server.dreams.DreamManagerService.this.mIsCharging = com.android.server.dreams.DreamManagerService.this.mBatteryManagerInternal.isPowered(15);
                } else {
                    com.android.server.dreams.DreamManagerService.this.mIsCharging = "android.os.action.CHARGING".equals(intent.getAction());
                }
            }
        };
        this.mDockStateReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.dreams.DreamManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.intent.action.DOCK_EVENT".equals(intent.getAction())) {
                    int dockState = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
                    com.android.server.dreams.DreamManagerService.this.mIsDocked = dockState != 0;
                }
            }
        };
        this.mControllerListener = new com.android.server.dreams.DreamController.Listener() { // from class: com.android.server.dreams.DreamManagerService.4
            @Override // com.android.server.dreams.DreamController.Listener
            public void onDreamStarted(android.os.Binder token) {
                com.android.server.dreams.DreamManagerService.this.reportDreamingStarted();
            }

            @Override // com.android.server.dreams.DreamController.Listener
            public void onDreamStopped(android.os.Binder token) {
                synchronized (com.android.server.dreams.DreamManagerService.this.mLock) {
                    if (com.android.server.dreams.DreamManagerService.this.mCurrentDream != null && com.android.server.dreams.DreamManagerService.this.mCurrentDream.token == token) {
                        com.android.server.dreams.DreamManagerService.this.cleanupDreamLocked();
                    }
                }
                com.android.server.dreams.DreamManagerService.this.reportDreamingStopped();
            }
        };
        this.mDozeEnabledObserver = new android.database.ContentObserver(null) { // from class: com.android.server.dreams.DreamManagerService.5
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.dreams.DreamManagerService.this.writePulseGestureEnabled();
            }
        };
        this.mSystemPropertiesChanged = new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService.6
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.dreams.DreamManagerService.DEBUG) {
                    android.util.Slog.d(com.android.server.dreams.DreamManagerService.TAG, "System properties changed");
                }
                synchronized (com.android.server.dreams.DreamManagerService.this.mLock) {
                    if (com.android.server.dreams.DreamManagerService.this.mCurrentDream != null && com.android.server.dreams.DreamManagerService.this.mCurrentDream.name != null && com.android.server.dreams.DreamManagerService.this.mCurrentDream.canDoze && !com.android.server.dreams.DreamManagerService.this.mCurrentDream.name.equals(com.android.server.dreams.DreamManagerService.this.getDozeComponent())) {
                        com.android.server.dreams.DreamManagerService.this.mPowerManager.wakeUp(android.os.SystemClock.uptimeMillis(), "android.server.dreams:SYSPROP");
                    }
                }
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mController = new com.android.server.dreams.DreamController(context, this.mHandler, this.mControllerListener);
        this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mPowerManagerInternal = (android.os.PowerManagerInternal) getLocalService(android.os.PowerManagerInternal.class);
        this.mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) getLocalService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mPmInternal = (android.content.pm.PackageManagerInternal) getLocalService(android.content.pm.PackageManagerInternal.class);
        this.mUserManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        this.mDozeWakeLock = this.mPowerManager.newWakeLock(64, DOZE_WAKE_LOCK_TAG);
        this.mDozeConfig = new android.hardware.display.AmbientDisplayConfiguration(this.mContext);
        this.mUiEventLogger = new com.android.internal.logging.UiEventLoggerImpl();
        this.mDreamUiEventLogger = new com.android.server.dreams.DreamUiEventLoggerImpl(this.mContext.getResources().getStringArray(android.R.array.config_locationDriverAssistancePackageNames));
        android.hardware.display.AmbientDisplayConfiguration adc = new android.hardware.display.AmbientDisplayConfiguration(this.mContext);
        this.mAmbientDisplayComponent = android.content.ComponentName.unflattenFromString(adc.ambientDisplayComponent());
        this.mDreamsOnlyEnabledForDockUser = this.mContext.getResources().getBoolean(android.R.bool.config_dreamsActivatedOnSleepByDefault);
        this.mDismissDreamOnActivityStart = this.mContext.getResources().getBoolean(android.R.bool.config_disableWeaverOnUnsecuredUsers);
        this.mDreamsEnabledByDefaultConfig = this.mContext.getResources().getBoolean(android.R.bool.config_dreamsActivatedOnDockByDefault);
        this.mDreamsActivatedOnChargeByDefault = this.mContext.getResources().getBoolean(android.R.bool.config_dozeWakeLockScreenSensorAvailable);
        this.mDreamsActivatedOnDockByDefault = this.mContext.getResources().getBoolean(android.R.bool.config_dozeSupportsAodWallpaper);
        this.mSettingsObserver = new com.android.server.dreams.DreamManagerService.SettingsObserver(this.mHandler);
        this.mKeepDreamingWhenUnpluggingDefault = this.mContext.getResources().getBoolean(android.R.bool.config_hideDisplayCutoutWithDisplayArea);
        this.mDreamsDisabledByAmbientModeSuppressionConfig = this.mContext.getResources().getBoolean(android.R.bool.config_dragToMaximizeInDesktopMode);
        if (com.android.server.dreams.Flags.useBatteryChangedBroadcast()) {
            this.mBatteryManagerInternal = (android.os.BatteryManagerInternal) getLocalService(android.os.BatteryManagerInternal.class);
        } else {
            this.mBatteryManagerInternal = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("dreams", new com.android.server.dreams.DreamManagerService.BinderService());
        publishLocalService(android.service.dreams.DreamManagerInternal.class, new com.android.server.dreams.DreamManagerService.LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 600) {
            if (android.os.Build.IS_DEBUGGABLE) {
                android.os.SystemProperties.addChangeCallback(this.mSystemPropertiesChanged);
            }
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("doze_pulse_on_double_tap"), false, this.mDozeEnabledObserver, -1);
            writePulseGestureEnabled();
            if (this.mDismissDreamOnActivityStart) {
                this.mAtmInternal.registerActivityStartInterceptor(4, this.mActivityInterceptorCallback);
            }
            this.mContext.registerReceiver(this.mDockStateReceiver, new android.content.IntentFilter("android.intent.action.DOCK_EVENT"));
            android.content.IntentFilter chargingIntentFilter = new android.content.IntentFilter();
            if (com.android.server.dreams.Flags.useBatteryChangedBroadcast()) {
                chargingIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
                chargingIntentFilter.setPriority(1000);
            } else {
                chargingIntentFilter.addAction("android.os.action.CHARGING");
                chargingIntentFilter.addAction("android.os.action.DISCHARGING");
            }
            this.mContext.registerReceiver(this.mChargingReceiver, chargingIntentFilter);
            this.mSettingsObserver = new com.android.server.dreams.DreamManagerService.SettingsObserver(this.mHandler);
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("screensaver_activate_on_sleep"), false, this.mSettingsObserver, -1);
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("screensaver_activate_on_dock"), false, this.mSettingsObserver, -1);
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("screensaver_enabled"), false, this.mSettingsObserver, -1);
            this.mIsCharging = ((android.os.BatteryManager) this.mContext.getSystemService(android.os.BatteryManager.class)).isCharging();
            updateWhenToDreamSettings();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        updateWhenToDreamSettings();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserSwitching$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserSwitching$0() {
        writePulseGestureEnabled();
        synchronized (this.mLock) {
            stopDreamLocked(false, "user switched");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("DREAM MANAGER (dumpsys dreams)");
            pw.println();
            pw.println("mCurrentDream=" + this.mCurrentDream);
            pw.println("mForceAmbientDisplayEnabled=" + this.mForceAmbientDisplayEnabled);
            pw.println("mDreamsOnlyEnabledForDockUser=" + this.mDreamsOnlyEnabledForDockUser);
            pw.println("mDreamsEnabledSetting=" + this.mDreamsEnabledSetting);
            pw.println("mDreamsActivatedOnDockByDefault=" + this.mDreamsActivatedOnDockByDefault);
            pw.println("mDreamsActivatedOnChargeByDefault=" + this.mDreamsActivatedOnChargeByDefault);
            pw.println("mIsDocked=" + this.mIsDocked);
            pw.println("mIsCharging=" + this.mIsCharging);
            pw.println("mWhenToDream=" + this.mWhenToDream);
            pw.println("mKeepDreamingWhenUnpluggingDefault=" + this.mKeepDreamingWhenUnpluggingDefault);
            pw.println("getDozeComponent()=" + getDozeComponent());
            pw.println("mDreamOverlayServiceName=" + android.content.ComponentName.flattenToShortString(this.mDreamOverlayServiceName));
            pw.println();
            com.android.internal.util.DumpUtils.dumpAsync(this.mHandler, new com.android.internal.util.DumpUtils.Dump() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda6
                public final void dump(java.io.PrintWriter printWriter, java.lang.String str) {
                    this.f$0.lambda$dumpInternal$1(printWriter, str);
                }
            }, pw, "", 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpInternal$1(java.io.PrintWriter pw1, java.lang.String prefix) {
        this.mController.dump(pw1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWhenToDreamSettings() {
        synchronized (this.mLock) {
            android.content.ContentResolver resolver = this.mContext.getContentResolver();
            boolean z = true;
            int activateWhenCharging = android.provider.Settings.Secure.getIntForUser(resolver, "screensaver_activate_on_sleep", this.mDreamsActivatedOnChargeByDefault ? 1 : 0, -2) != 0 ? 2 : 0;
            int activateWhenDocked = android.provider.Settings.Secure.getIntForUser(resolver, "screensaver_activate_on_dock", this.mDreamsActivatedOnDockByDefault ? 1 : 0, -2) != 0 ? 1 : 0;
            this.mWhenToDream = activateWhenCharging + activateWhenDocked;
            if (android.provider.Settings.Secure.getIntForUser(resolver, "screensaver_enabled", this.mDreamsEnabledByDefaultConfig ? 1 : 0, -2) == 0) {
                z = false;
            }
            this.mDreamsEnabledSetting = z;
        }
    }

    private void reportKeepDreamingWhenUnpluggingChanged(final boolean keepDreaming) {
        notifyDreamStateListeners(new java.util.function.Consumer() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.service.dreams.DreamManagerInternal.DreamManagerStateListener) obj).onKeepDreamingWhenUnpluggingChanged(keepDreaming);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportDreamingStarted() {
        notifyDreamStateListeners(new java.util.function.Consumer() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.service.dreams.DreamManagerInternal.DreamManagerStateListener) obj).onDreamingStarted();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportDreamingStopped() {
        notifyDreamStateListeners(new java.util.function.Consumer() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.service.dreams.DreamManagerInternal.DreamManagerStateListener) obj).onDreamingStopped();
            }
        });
    }

    private void notifyDreamStateListeners(final java.util.function.Consumer<android.service.dreams.DreamManagerInternal.DreamManagerStateListener> notifier) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyDreamStateListeners$5(notifier);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDreamStateListeners$5(java.util.function.Consumer notifier) {
        for (android.service.dreams.DreamManagerInternal.DreamManagerStateListener listener : this.mDreamManagerStateListeners) {
            notifier.accept(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDreamingInternal() {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mCurrentDream == null || this.mCurrentDream.isPreview || this.mCurrentDream.isWaking) ? false : true;
        }
        return z;
    }

    private boolean isDozingInternal() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mCurrentDream != null && this.mCurrentDream.isDozing;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDreamingOrInPreviewInternal() {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mCurrentDream == null || this.mCurrentDream.isWaking) ? false : true;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canStartDreamingInternal(boolean isScreenOn) {
        synchronized (this.mLock) {
            if (isScreenOn) {
                if (isDreamingInternal() && dreamIsFrontmost()) {
                    return false;
                }
            }
            if (!this.mDreamsEnabledSetting) {
                return false;
            }
            if (!dreamsEnabledForUser(android.app.ActivityManager.getCurrentUser())) {
                return false;
            }
            if (!this.mUserManager.isUserUnlocked()) {
                return false;
            }
            if (this.mDreamsDisabledByAmbientModeSuppressionConfig && this.mPowerManagerInternal.isAmbientDisplaySuppressed()) {
                android.util.Slog.i(TAG, "Can't start dreaming because ambient is suppressed.");
                return false;
            }
            if ((this.mWhenToDream & 2) == 2) {
                return this.mIsCharging;
            }
            if ((this.mWhenToDream & 1) != 1) {
                return false;
            }
            return this.mIsDocked;
        }
    }

    private boolean dreamIsFrontmost() {
        return !android.service.dreams.Flags.dreamHandlesBeingObscured() || this.mController.dreamIsFrontmost();
    }

    protected void requestStartDreamFromShell() {
        requestDreamInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestDreamInternal() {
        if (isDreamingInternal() && !dreamIsFrontmost() && this.mController.bringDreamToFront()) {
            return;
        }
        long time = android.os.SystemClock.uptimeMillis();
        this.mPowerManager.userActivity(time, true);
        this.mPowerManagerInternal.nap(time, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestAwakenInternal(java.lang.String reason) {
        long time = android.os.SystemClock.uptimeMillis();
        this.mPowerManager.userActivity(time, false);
        stopDreamInternal(false, reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishSelfInternal(android.os.IBinder token, boolean immediate) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Dream finished: " + token + ", immediate=" + immediate);
        }
        synchronized (this.mLock) {
            if (this.mCurrentDream != null && this.mCurrentDream.token == token) {
                stopDreamLocked(immediate, "finished self");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void testDreamInternal(android.content.ComponentName dream, int userId) {
        synchronized (this.mLock) {
            startDreamLocked(dream, true, false, userId, "test dream");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDreamInternal(boolean doze, java.lang.String reason) {
        int userId = android.app.ActivityManager.getCurrentUser();
        android.content.ComponentName dream = chooseDreamForUser(doze, userId);
        if (dream != null) {
            synchronized (this.mLock) {
                startDreamLocked(dream, false, doze, userId, reason);
            }
        }
    }

    protected void requestStopDreamFromShell() {
        stopDreamInternal(false, "stopping dream from shell");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDreamInternal(boolean immediate, java.lang.String reason) {
        synchronized (this.mLock) {
            stopDreamLocked(immediate, reason);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDozingInternal(android.os.IBinder token, int screenState, int reason, int screenBrightness) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Dream requested to start dozing: " + token + ", screenState=" + screenState + ", screenBrightness=" + screenBrightness);
        }
        synchronized (this.mLock) {
            if (this.mCurrentDream != null && this.mCurrentDream.token == token && this.mCurrentDream.canDoze) {
                this.mCurrentDream.dozeScreenState = screenState;
                this.mCurrentDream.dozeScreenBrightness = screenBrightness;
                this.mPowerManagerInternal.setDozeOverrideFromDreamManager(screenState, reason, screenBrightness);
                if (!this.mCurrentDream.isDozing) {
                    this.mCurrentDream.isDozing = true;
                    this.mDozeWakeLock.acquire();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDozingInternal(android.os.IBinder token) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Dream requested to stop dozing: " + token);
        }
        synchronized (this.mLock) {
            if (this.mCurrentDream != null && this.mCurrentDream.token == token && this.mCurrentDream.isDozing) {
                this.mCurrentDream.isDozing = false;
                this.mDozeWakeLock.release();
                this.mPowerManagerInternal.setDozeOverrideFromDreamManager(0, 5, -1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceAmbientDisplayEnabledInternal(boolean enabled) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Force ambient display enabled: " + enabled);
        }
        synchronized (this.mLock) {
            this.mForceAmbientDisplayEnabled = enabled;
        }
    }

    private android.content.ComponentName chooseDreamForUser(boolean doze, int userId) {
        if (doze) {
            android.content.ComponentName dozeComponent = getDozeComponent(userId);
            if (validateDream(dozeComponent)) {
                return dozeComponent;
            }
            return null;
        }
        if (this.mSystemDreamComponent != null) {
            return this.mSystemDreamComponent;
        }
        android.content.ComponentName[] dreams = getDreamComponentsForUser(userId);
        if (dreams == null || dreams.length == 0) {
            return null;
        }
        return dreams[0];
    }

    private boolean validateDream(android.content.ComponentName component) {
        if (component == null) {
            return false;
        }
        android.content.pm.ServiceInfo serviceInfo = getServiceInfo(component);
        if (serviceInfo == null) {
            android.util.Slog.w(TAG, "Dream " + component + " does not exist");
            return false;
        }
        if (serviceInfo.applicationInfo.targetSdkVersion >= 21 && !"android.permission.BIND_DREAM_SERVICE".equals(serviceInfo.permission)) {
            android.util.Slog.w(TAG, "Dream " + component + " is not available because its manifest is missing the android.permission.BIND_DREAM_SERVICE permission on the dream service declaration.");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.ComponentName[] getDreamComponentsForUser(int userId) {
        android.content.ComponentName defaultDream;
        if (!dreamsEnabledForUser(userId)) {
            return null;
        }
        java.lang.String names = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "screensaver_components", userId);
        android.content.ComponentName[] components = componentsFromString(names);
        java.util.List<android.content.ComponentName> validComponents = new java.util.ArrayList<>();
        if (components != null) {
            for (android.content.ComponentName component : components) {
                if (validateDream(component)) {
                    validComponents.add(component);
                }
            }
        }
        if (validComponents.isEmpty() && (defaultDream = getDefaultDreamComponentForUser(userId)) != null) {
            android.util.Slog.w(TAG, "Falling back to default dream " + defaultDream);
            validComponents.add(defaultDream);
        }
        return (android.content.ComponentName[]) validComponents.toArray(new android.content.ComponentName[validComponents.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDreamComponentsForUser(int userId, android.content.ComponentName[] componentNames) {
        android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "screensaver_components", componentsToString(componentNames), userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSystemDreamComponentInternal(android.content.ComponentName componentName) {
        synchronized (this.mLock) {
            if (java.util.Objects.equals(this.mSystemDreamComponent, componentName)) {
                return;
            }
            this.mSystemDreamComponent = componentName;
            reportKeepDreamingWhenUnpluggingChanged(shouldKeepDreamingWhenUnplugging());
            if (isDreamingInternal() && !isDozingInternal()) {
                startDreamInternal(false, (this.mSystemDreamComponent == null ? "clear" : "set") + " system dream component");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldKeepDreamingWhenUnplugging() {
        return this.mKeepDreamingWhenUnpluggingDefault && this.mSystemDreamComponent == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.ComponentName getDefaultDreamComponentForUser(int userId) {
        java.lang.String name = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "screensaver_default_component", userId);
        if (name == null) {
            return null;
        }
        return android.content.ComponentName.unflattenFromString(name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.ComponentName getDozeComponent() {
        return getDozeComponent(android.app.ActivityManager.getCurrentUser());
    }

    private android.content.ComponentName getDozeComponent(int userId) {
        if (this.mForceAmbientDisplayEnabled || this.mDozeConfig.enabled(userId)) {
            return android.content.ComponentName.unflattenFromString(this.mDozeConfig.ambientDisplayComponent());
        }
        return null;
    }

    private boolean dreamsEnabledForUser(int userId) {
        if (!this.mDreamsOnlyEnabledForDockUser) {
            return true;
        }
        if (userId < 0) {
            return false;
        }
        int mainUserId = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getMainUserId();
        return userId == mainUserId;
    }

    private android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName name) {
        if (name != null) {
            try {
                return this.mContext.getPackageManager().getServiceInfo(name, 268435456);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    private void startDreamLocked(final android.content.ComponentName name, final boolean isPreviewMode, final boolean canDoze, final int userId, final java.lang.String reason) {
        if (this.mCurrentDream != null && !this.mCurrentDream.isWaking && java.util.Objects.equals(this.mCurrentDream.name, name) && this.mCurrentDream.isPreview == isPreviewMode && this.mCurrentDream.canDoze == canDoze && this.mCurrentDream.userId == userId) {
            android.util.Slog.i(TAG, "Already in target dream.");
            return;
        }
        android.util.Slog.i(TAG, "Entering dreamland.");
        if (this.mCurrentDream != null && this.mCurrentDream.isDozing) {
            stopDozingInternal(this.mCurrentDream.token);
        }
        this.mCurrentDream = new com.android.server.dreams.DreamManagerService.DreamRecord(name, userId, isPreviewMode, canDoze);
        if (!this.mCurrentDream.name.equals(this.mAmbientDisplayComponent)) {
            this.mUiEventLogger.log(com.android.server.dreams.DreamUiEventLogger.DreamUiEventEnum.DREAM_START);
            this.mDreamUiEventLogger.log(com.android.server.dreams.DreamUiEventLogger.DreamUiEventEnum.DREAM_START, this.mCurrentDream.name.flattenToString());
        }
        final android.os.PowerManager.WakeLock wakeLock = this.mPowerManager.newWakeLock(1, DREAM_WAKE_LOCK_TAG);
        final android.os.Binder dreamToken = this.mCurrentDream.token;
        this.mHandler.post(wakeLock.wrap(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startDreamLocked$6(name, dreamToken, isPreviewMode, canDoze, userId, wakeLock, reason);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDreamLocked$6(android.content.ComponentName name, android.os.Binder dreamToken, boolean isPreviewMode, boolean canDoze, int userId, android.os.PowerManager.WakeLock wakeLock, java.lang.String reason) {
        this.mAtmInternal.notifyActiveDreamChanged(name);
        this.mController.startDream(dreamToken, name, isPreviewMode, canDoze, userId, wakeLock, this.mDreamOverlayServiceName, reason);
    }

    private void stopDreamLocked(final boolean immediate, final java.lang.String reason) {
        if (this.mCurrentDream != null) {
            if (immediate) {
                android.util.Slog.i(TAG, "Leaving dreamland.");
                cleanupDreamLocked();
            } else if (!this.mCurrentDream.isWaking) {
                android.util.Slog.i(TAG, "Gently waking up from dream.");
                this.mCurrentDream.isWaking = true;
            } else {
                return;
            }
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$stopDreamLocked$7(immediate, reason);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopDreamLocked$7(boolean immediate, java.lang.String reason) {
        this.mController.stopDream(immediate, reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupDreamLocked() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cleanupDreamLocked$8();
            }
        });
        if (this.mCurrentDream == null) {
            return;
        }
        if (!this.mCurrentDream.name.equals(this.mAmbientDisplayComponent)) {
            this.mUiEventLogger.log(com.android.server.dreams.DreamUiEventLogger.DreamUiEventEnum.DREAM_STOP);
            this.mDreamUiEventLogger.log(com.android.server.dreams.DreamUiEventLogger.DreamUiEventEnum.DREAM_STOP, this.mCurrentDream.name.flattenToString());
        }
        if (this.mCurrentDream.isDozing) {
            this.mDozeWakeLock.release();
        }
        this.mCurrentDream = null;
        this.mPowerManagerInternal.setDozeOverrideFromDreamManager(0, 5, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupDreamLocked$8() {
        this.mAtmInternal.notifyActiveDreamChanged(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermission(java.lang.String permission) {
        if (this.mContext.checkCallingOrSelfPermission(permission) != 0) {
            throw new java.lang.SecurityException("Access denied to process: " + android.os.Binder.getCallingPid() + ", must have permission " + permission);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writePulseGestureEnabled() {
        android.content.ComponentName name = getDozeComponent();
        boolean dozeEnabled = validateDream(name);
        ((com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class)).setPulseGestureEnabled(dozeEnabled);
    }

    private static java.lang.String componentsToString(android.content.ComponentName[] componentNames) {
        if (componentNames == null) {
            return null;
        }
        java.lang.StringBuilder names = new java.lang.StringBuilder();
        for (android.content.ComponentName componentName : componentNames) {
            if (names.length() > 0) {
                names.append(',');
            }
            names.append(componentName.flattenToString());
        }
        return names.toString();
    }

    private static android.content.ComponentName[] componentsFromString(java.lang.String names) {
        if (names == null) {
            return null;
        }
        java.lang.String[] namesArray = names.split(",");
        android.content.ComponentName[] componentNames = new android.content.ComponentName[namesArray.length];
        for (int i = 0; i < namesArray.length; i++) {
            componentNames[i] = android.content.ComponentName.unflattenFromString(namesArray[i]);
        }
        return componentNames;
    }

    private static final class DreamHandler extends android.os.Handler {
        public DreamHandler(android.os.Looper looper) {
            super(looper, null, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class BinderService extends android.service.dreams.IDreamManager.Stub {
        private BinderService() {
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.dreams.DreamManagerService.this.mContext, com.android.server.dreams.DreamManagerService.TAG, pw)) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.dreams.DreamManagerService.this.dumpInternal(pw);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.dreams.DreamShellCommand(com.android.server.dreams.DreamManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public android.content.ComponentName[] getDreamComponents() {
            return getDreamComponentsForUser(android.os.UserHandle.getCallingUserId());
        }

        public android.content.ComponentName[] getDreamComponentsForUser(int userId) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.READ_DREAM_STATE");
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getDreamComponents", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.dreams.DreamManagerService.this.getDreamComponentsForUser(userId2);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setDreamComponents(android.content.ComponentName[] componentNames) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            int userId = android.os.UserHandle.getCallingUserId();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                setDreamComponentsForUser(userId, componentNames);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setDreamComponentsForUser(int userId, android.content.ComponentName[] componentNames) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "setDreamComponents", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.setDreamComponentsForUser(userId2, componentNames);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setSystemDreamComponent(android.content.ComponentName componentName) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.setSystemDreamComponentInternal(componentName);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void registerDreamOverlayService(android.content.ComponentName overlayComponent) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            com.android.server.dreams.DreamManagerService.this.mDreamOverlayServiceName = overlayComponent;
        }

        public android.content.ComponentName getDefaultDreamComponentForUser(int userId) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.READ_DREAM_STATE");
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getDefaultDreamComponent", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.dreams.DreamManagerService.this.getDefaultDreamComponentForUser(userId2);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isDreaming() {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.READ_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.dreams.DreamManagerService.this.isDreamingInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isDreamingOrInPreview() {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.READ_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.dreams.DreamManagerService.this.isDreamingOrInPreviewInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void dream() {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.requestDreamInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean canStartDreaming(boolean isScreenOn) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.READ_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.dreams.DreamManagerService.this.canStartDreamingInternal(isScreenOn);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void testDream(int userId, android.content.ComponentName dream) {
            if (dream == null) {
                throw new java.lang.IllegalArgumentException("dream must not be null");
            }
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "testDream", null);
            int currentUserId = android.app.ActivityManager.getCurrentUser();
            if (userId2 != currentUserId) {
                android.util.Slog.w(com.android.server.dreams.DreamManagerService.TAG, "Aborted attempt to start a test dream while a different  user is active: userId=" + userId2 + ", currentUserId=" + currentUserId);
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.testDreamInternal(dream, userId2);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void awaken() {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.requestAwakenInternal("request awaken");
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void finishSelf(android.os.IBinder token, boolean immediate) {
            if (token == null) {
                throw new java.lang.IllegalArgumentException("token must not be null");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.finishSelfInternal(token, immediate);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void startDozing(android.os.IBinder token, int screenState, int reason, int screenBrightness) {
            if (token == null) {
                throw new java.lang.IllegalArgumentException("token must not be null");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.startDozingInternal(token, screenState, reason, screenBrightness);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void stopDozing(android.os.IBinder token) {
            if (token == null) {
                throw new java.lang.IllegalArgumentException("token must not be null");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.stopDozingInternal(token);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void forceAmbientDisplayEnabled(boolean enabled) {
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.DEVICE_POWER");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.forceAmbientDisplayEnabledInternal(enabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void startDreamActivity(final android.content.Intent intent) {
            final int callingUid = android.os.Binder.getCallingUid();
            final int callingPid = android.os.Binder.getCallingPid();
            com.android.server.dreams.DreamManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$BinderService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startDreamActivity$0(intent, callingUid, callingPid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$startDreamActivity$0(android.content.Intent intent, int callingUid, int callingPid) {
            synchronized (com.android.server.dreams.DreamManagerService.this.mLock) {
                if (com.android.server.dreams.DreamManagerService.this.mCurrentDream == null) {
                    android.util.Slog.e(com.android.server.dreams.DreamManagerService.TAG, "Attempt to start DreamActivity, but the device is not dreaming. Aborting without starting the DreamActivity.");
                    return;
                }
                android.os.Binder dreamToken = com.android.server.dreams.DreamManagerService.this.mCurrentDream.token;
                java.lang.String dreamPackageName = com.android.server.dreams.DreamManagerService.this.mCurrentDream.name.getPackageName();
                if (!canLaunchDreamActivity(dreamPackageName, intent.getPackage(), callingUid)) {
                    android.util.Slog.e(com.android.server.dreams.DreamManagerService.TAG, "The dream activity can be started only when the device is dreaming and only by the active dream package.");
                    return;
                }
                android.app.IAppTask appTask = com.android.server.dreams.DreamManagerService.this.mAtmInternal.startDreamActivity(intent, callingUid, callingPid);
                if (appTask == null) {
                    android.util.Slog.e(com.android.server.dreams.DreamManagerService.TAG, "Could not start dream activity.");
                    com.android.server.dreams.DreamManagerService.this.stopDreamInternal(true, "DreamActivity not started");
                } else {
                    com.android.server.dreams.DreamManagerService.this.mController.setDreamAppTask(dreamToken, appTask);
                }
            }
        }

        public void setDreamIsObscured(final boolean isObscured) {
            if (!android.service.dreams.Flags.dreamHandlesBeingObscured()) {
                return;
            }
            com.android.server.dreams.DreamManagerService.this.checkPermission("android.permission.WRITE_DREAM_STATE");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.dreams.DreamManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamManagerService$BinderService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setDreamIsObscured$1(isObscured);
                    }
                });
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setDreamIsObscured$1(boolean isObscured) {
            com.android.server.dreams.DreamManagerService.this.mController.setDreamIsObscured(isObscured);
        }

        boolean canLaunchDreamActivity(java.lang.String dreamPackageName, java.lang.String packageName, int callingUid) {
            if (dreamPackageName == null || packageName == null) {
                android.util.Slog.e(com.android.server.dreams.DreamManagerService.TAG, "Cannot launch dream activity due to invalid state. dream component= " + dreamPackageName + ", packageName=" + packageName);
                return false;
            }
            if (!com.android.server.dreams.DreamManagerService.this.mPmInternal.isSameApp(packageName, callingUid, android.os.UserHandle.getUserId(callingUid))) {
                android.util.Slog.e(com.android.server.dreams.DreamManagerService.TAG, "Cannot launch dream activity because package=" + packageName + " does not match callingUid=" + callingUid);
                return false;
            }
            if (packageName.equals(dreamPackageName)) {
                return true;
            }
            android.util.Slog.e(com.android.server.dreams.DreamManagerService.TAG, "Dream packageName does not match active dream. Package " + packageName + " does not match " + dreamPackageName);
            return false;
        }
    }

    private final class LocalService extends android.service.dreams.DreamManagerInternal {
        private LocalService() {
        }

        public void startDream(boolean doze, java.lang.String reason) {
            com.android.server.dreams.DreamManagerService.this.startDreamInternal(doze, reason);
        }

        public void stopDream(boolean immediate, java.lang.String reason) {
            com.android.server.dreams.DreamManagerService.this.stopDreamInternal(immediate, reason);
        }

        public boolean isDreaming() {
            return com.android.server.dreams.DreamManagerService.this.isDreamingInternal();
        }

        public boolean canStartDreaming(boolean isScreenOn) {
            return com.android.server.dreams.DreamManagerService.this.canStartDreamingInternal(isScreenOn);
        }

        public void requestDream() {
            com.android.server.dreams.DreamManagerService.this.requestDreamInternal();
        }

        public void registerDreamManagerStateListener(android.service.dreams.DreamManagerInternal.DreamManagerStateListener listener) {
            com.android.server.dreams.DreamManagerService.this.mDreamManagerStateListeners.add(listener);
            listener.onKeepDreamingWhenUnpluggingChanged(com.android.server.dreams.DreamManagerService.this.shouldKeepDreamingWhenUnplugging());
        }

        public void unregisterDreamManagerStateListener(android.service.dreams.DreamManagerInternal.DreamManagerStateListener listener) {
            com.android.server.dreams.DreamManagerService.this.mDreamManagerStateListeners.remove(listener);
        }
    }

    private static final class DreamRecord {
        public final boolean canDoze;
        public final boolean isPreview;
        public final android.content.ComponentName name;
        public final int userId;
        public final android.os.Binder token = new android.os.Binder();
        public boolean isDozing = false;
        public boolean isWaking = false;
        public int dozeScreenState = 0;
        public int dozeScreenBrightness = -1;

        DreamRecord(android.content.ComponentName name, int userId, boolean isPreview, boolean canDoze) {
            this.name = name;
            this.userId = userId;
            this.isPreview = isPreview;
            this.canDoze = canDoze;
        }

        public java.lang.String toString() {
            return "DreamRecord{token=" + this.token + ", name=" + this.name + ", userId=" + this.userId + ", isPreview=" + this.isPreview + ", canDoze=" + this.canDoze + ", isDozing=" + this.isDozing + ", isWaking=" + this.isWaking + ", dozeScreenState=" + this.dozeScreenState + ", dozeScreenBrightness=" + this.dozeScreenBrightness + '}';
        }
    }
}
