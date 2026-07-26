package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class DisplayPowerController implements com.android.server.display.AutomaticBrightnessController.Callbacks, com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks, com.android.server.display.DisplayPowerControllerInterface {
    private static final int BRIGHTNESS_CHANGE_STATSD_REPORT_INTERVAL_MS = 500;
    private static final int COLOR_FADE_OFF_ANIMATION_DURATION_MILLIS = 100;
    private static final int COLOR_FADE_ON_ANIMATION_DURATION_MILLIS = 250;
    private static final java.lang.String GLOBAL_HBM_SELL_MODE = "global_hbm_sell_mode";
    private static final int MSG_BOOT_COMPLETED = 13;
    private static final int MSG_BRIGHTNESS_RAMP_DONE = 10;
    private static final int MSG_CONFIGURE_BRIGHTNESS = 4;
    private static final int MSG_OFFLOADING_SCREEN_ON_UNBLOCKED = 18;
    private static final int MSG_RESET_FPS_AFTER_FINISH_DC_BRIGHTNESS = 21;
    private static final int MSG_RESET_SCREEN_ON_CABC = 22;
    private static final int MSG_SCREEN_OFF_UNBLOCKED = 3;
    private static final int MSG_SCREEN_ON_UNBLOCKED = 2;
    private static final int MSG_SET_BRIGHTNESS_FROM_OFFLOAD = 17;
    private static final int MSG_SET_DWBC_COLOR_OVERRIDE = 15;
    private static final int MSG_SET_DWBC_LOGGING_ENABLED = 16;
    private static final int MSG_SET_TEMPORARY_AUTO_BRIGHTNESS_ADJUSTMENT = 6;
    private static final int MSG_SET_TEMPORARY_BRIGHTNESS = 5;
    private static final int MSG_STATSD_HBM_BRIGHTNESS = 11;
    private static final int MSG_STOP = 7;
    private static final int MSG_SWITCH_AUTOBRIGHTNESS_MODE = 14;
    private static final int MSG_SWITCH_USER = 12;
    private static final int MSG_UPDATE_BRIGHTNESS = 8;
    private static final int MSG_UPDATE_POWER_STATE = 1;
    private static final int MSG_UPDATE_RBC = 9;
    private static final int RAMP_STATE_SKIP_AUTOBRIGHT = 2;
    private static final int RAMP_STATE_SKIP_INITIAL = 1;
    private static final int RAMP_STATE_SKIP_NONE = 0;
    private static final int REPORTED_TO_POLICY_SCREEN_OFF = 0;
    private static final int REPORTED_TO_POLICY_SCREEN_ON = 2;
    private static final int REPORTED_TO_POLICY_SCREEN_TURNING_OFF = 3;
    private static final int REPORTED_TO_POLICY_SCREEN_TURNING_ON = 1;
    private static final int REPORTED_TO_POLICY_UNREPORTED = -1;
    private static final int RINGBUFFER_MAX = 100;
    private static final int RINGBUFFER_RBC_MAX = 20;
    private static final java.lang.String SCREEN_OFF_BLOCKED_TRACE_NAME = "Screen off blocked";
    private static final java.lang.String SCREEN_ON_BLOCKED_BY_DISPLAYOFFLOAD_TRACE_NAME = "Screen on blocked by displayoffload";
    private static final java.lang.String SCREEN_ON_BLOCKED_TRACE_NAME = "Screen on blocked";
    private static final java.lang.String SECOND_SCREEN_AUTO_BRIGHTNESS_ADJ = "second_screen_auto_brightness_adj";
    private static final java.lang.String UNBLOCK_REASON_GO_TO_SLEEP = "UNBLOCK_REASON_GO_TO_SLEEP";
    private static final boolean USE_COLOR_FADE_ON_ANIMATION = false;
    private boolean isRM;
    private boolean mAppliedDimming;
    private boolean mAppliedThrottling;
    private com.android.server.display.IColorAutomaticBrightnessController mAutomaticBrightnessController;
    private final com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy mAutomaticBrightnessStrategy;
    private final com.android.internal.app.IBatteryStats mBatteryStats;
    private final com.android.server.display.DisplayBlanker mBlanker;
    private boolean mBootCompleted;
    private final boolean mBrightnessBucketsInDozeConfig;
    private final com.android.server.display.brightness.clamper.BrightnessClamperController mBrightnessClamperController;
    private com.android.internal.util.RingBuffer<com.android.server.display.brightness.BrightnessEvent> mBrightnessEventRingBuffer;
    private long mBrightnessRampDecreaseMaxTimeIdleMillis;
    private long mBrightnessRampDecreaseMaxTimeMillis;
    private long mBrightnessRampIncreaseMaxTimeIdleMillis;
    private long mBrightnessRampIncreaseMaxTimeMillis;
    private float mBrightnessRampRateFastDecrease;
    private float mBrightnessRampRateFastIncrease;
    private float mBrightnessRampRateSlowDecrease;
    private float mBrightnessRampRateSlowDecreaseIdle;
    private float mBrightnessRampRateSlowIncrease;
    private float mBrightnessRampRateSlowIncreaseIdle;
    private final com.android.server.display.BrightnessRangeController mBrightnessRangeController;
    private final com.android.server.display.BrightnessThrottler mBrightnessThrottler;
    private final com.android.server.display.BrightnessTracker mBrightnessTracker;
    private final com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal mCdsi;
    private final com.android.server.display.DisplayPowerController.Clock mClock;
    private final boolean mColorFadeEnabled;
    private final boolean mColorFadeFadesConfig;
    private android.animation.ObjectAnimator mColorFadeOffAnimator;
    private android.animation.ObjectAnimator mColorFadeOnAnimator;
    private final android.content.Context mContext;
    private final boolean mDisplayBlanksAfterDozeConfig;
    private final com.android.server.display.brightness.DisplayBrightnessController mDisplayBrightnessController;
    private com.android.server.display.DisplayDevice mDisplayDevice;
    private com.android.server.display.DisplayDeviceConfig mDisplayDeviceConfig;
    private final int mDisplayId;
    private android.hardware.display.DisplayManagerInternal.DisplayOffloadSession mDisplayOffloadSession;
    private final com.android.server.display.DisplayPowerProximityStateController mDisplayPowerProximityStateController;
    private boolean mDisplayReadyLocked;
    private final com.android.server.display.state.DisplayStateController mDisplayStateController;
    private int mDisplayStatsId;
    private final com.android.server.display.whitebalance.DisplayWhiteBalanceController mDisplayWhiteBalanceController;
    private final com.android.server.display.whitebalance.DisplayWhiteBalanceSettings mDisplayWhiteBalanceSettings;
    private float mDozeScaleFactor;
    private boolean mDozing;
    public com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private final com.android.server.display.feature.DisplayManagerFlags mFlags;
    private final com.android.server.display.DisplayPowerController.DisplayControllerHandler mHandler;
    private float mInitialAutoBrightness;
    private final com.android.server.display.DisplayPowerController.Injector mInjector;
    private boolean mIsDisplayInternal;
    private boolean mIsEnabled;
    private boolean mIsInTransition;
    private boolean mIsPrimaryDisplay;
    private boolean mIsRbcActive;
    private final com.android.server.display.brightness.BrightnessEvent mLastBrightnessEvent;
    private int mLastState;
    private android.hardware.Sensor mLightSensor;
    private final com.android.server.display.LogicalDisplay mLogicalDisplay;
    private float[] mNitsRange;
    private final java.lang.Runnable mOnBrightnessChangeRunnable;
    private boolean mPendingRequestChangedLocked;
    private android.hardware.display.DisplayManagerInternal.DisplayPowerRequest mPendingRequestLocked;
    private boolean mPendingScreenOff;
    private com.android.server.display.DisplayPowerController.ScreenOffUnblocker mPendingScreenOffUnblocker;
    private com.android.server.display.DisplayPowerController.ScreenOnUnblocker mPendingScreenOnUnblocker;
    private java.lang.Runnable mPendingScreenOnUnblockerByDisplayOffload;
    private boolean mPendingUpdatePowerStateLocked;
    private android.hardware.display.DisplayManagerInternal.DisplayPowerRequest mPowerRequest;
    private com.android.server.display.DisplayPowerState mPowerState;
    private float mScreenBrightnessDefault;
    private final float mScreenBrightnessDozeConfig;
    private float mScreenBrightnessNormalMaximum;
    private com.android.server.display.RampAnimator.DualRampAnimator<com.android.server.display.DisplayPowerState> mScreenBrightnessRampAnimator;
    private float mScreenBrightnessRangeMaximum;
    private float mScreenBrightnessRangeMinimum;
    private long mScreenOffBlockStartRealTime;
    private android.hardware.Sensor mScreenOffBrightnessSensor;
    private com.android.server.display.ScreenOffBrightnessSensorController mScreenOffBrightnessSensorController;
    private long mScreenOnBlockByDisplayOffloadStartRealTime;
    private long mScreenOnBlockStartRealTime;
    private boolean mScreenTurningOnWasBlockedByDisplayOffload;
    private final android.hardware.SensorManager mSensorManager;
    private final com.android.server.display.DisplayPowerController.SettingsObserver mSettingsObserver;
    private final boolean mSkipScreenOnBrightnessRamp;
    private boolean mStopped;
    private final java.lang.String mTag;
    private final com.android.server.display.brightness.BrightnessEvent mTempBrightnessEvent;
    private java.lang.String mThermalBrightnessThrottlingDataId;
    private java.lang.String mUniqueDisplayId;
    private boolean mUseSoftwareAutoBrightnessConfig;
    private final com.android.server.display.WakelockController mWakelockController;
    private final com.android.server.policy.WindowManagerPolicy mWindowManagerPolicy;
    private static final java.lang.String TAG = "DisplayPowerController";
    private static boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    private static final boolean MTK_DEBUG = "eng".equals(android.os.Build.TYPE);
    private static final int DC_MODE_BRIGHT_EDGE = android.os.SystemProperties.getInt("ro.vendor.display.dc.brightness.threshold", 260);
    private static final java.lang.String DC_MODE_CUSTOMIZATION_KEY = "ro.vendor.display.dc.brightness.customization";
    private static final boolean DC_MODE_BRIGHT_CUSTOMIZATION = android.os.SystemProperties.getBoolean(DC_MODE_CUSTOMIZATION_KEY, false);
    private static final boolean IS_LIGHT_OS_BY_AMS = android.os.SystemProperties.getBoolean("ro.oplus.lightos.ams", false);
    private static boolean DEBUG_PANIC = false;
    private static final float SCREEN_ANIMATION_RATE_MINIMUM = 0.0f;
    private static final float[] BRIGHTNESS_RANGE_BOUNDARIES = {SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f, 100.0f, 200.0f, 300.0f, 400.0f, 500.0f, 600.0f, 700.0f, 800.0f, 900.0f, 1000.0f, 1200.0f, 1400.0f, 1600.0f, 1800.0f, 2000.0f, 2250.0f, 2500.0f, 2750.0f, 3000.0f};
    private static final int[] BRIGHTNESS_RANGE_INDEX = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37};
    private static boolean DisplayDisable = android.os.SystemProperties.getBoolean("ro.oplus.display.fingerprint_disable", false);
    private static final java.lang.String FP_SENSOR_TYPE = android.os.SystemProperties.get("persist.vendor.fingerprint.sensor_type", "unknow");
    private final java.lang.Object mLock = new java.lang.Object();
    private int mLeadDisplayId = -1;
    private final com.android.server.display.DisplayPowerController.CachedBrightnessInfo mCachedBrightnessInfo = new com.android.server.display.DisplayPowerController.CachedBrightnessInfo();
    private int mReportedScreenStateToPolicy = -1;
    final com.android.server.display.brightness.BrightnessReason mBrightnessReason = new com.android.server.display.brightness.BrightnessReason();
    private final com.android.server.display.brightness.BrightnessReason mBrightnessReasonTemp = new com.android.server.display.brightness.BrightnessReason();
    private float mLastStatsBrightness = SCREEN_ANIMATION_RATE_MINIMUM;
    private final com.android.internal.util.RingBuffer<com.android.server.display.brightness.BrightnessEvent> mRbcEventRingBuffer = new com.android.internal.util.RingBuffer<>(com.android.server.display.brightness.BrightnessEvent.class, 20);
    private int mSkipRampState = 0;
    private boolean mIsUserSwitching = false;
    private boolean mDCBrightnessChange = false;
    private boolean mUpdateFpsForDc = false;
    private boolean mResetFpsStatePending = false;
    private android.util.SparseArray<com.android.server.display.DisplayPowerControllerInterface> mDisplayBrightnessFollowers = new android.util.SparseArray<>();
    private final android.animation.Animator.AnimatorListener mAnimatorListener = new android.animation.Animator.AnimatorListener() { // from class: com.android.server.display.DisplayPowerController.2
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(android.animation.Animator animation) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(android.animation.Animator animation) {
            com.android.server.display.DisplayPowerController.this.sendUpdatePowerState();
            com.android.server.display.DisplayPowerController.this.mDpcExt.onAnimationChanged(animation, 2);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(android.animation.Animator animation) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(android.animation.Animator animation) {
        }
    };
    private final com.android.server.display.RampAnimator.Listener mRampAnimatorListener = new com.android.server.display.RampAnimator.Listener() { // from class: com.android.server.display.DisplayPowerController.3
        @Override // com.android.server.display.RampAnimator.Listener
        public void onAnimationStart(boolean isPrimaryAnimator) {
            com.android.server.display.DisplayPowerController.this.mDpcExt.setAnimating(true, isPrimaryAnimator);
        }

        @Override // com.android.server.display.RampAnimator.Listener
        public void onAnimationEnd(boolean isPrimaryAnimator) {
            com.android.server.display.DisplayPowerController.this.sendUpdatePowerState();
            if (isPrimaryAnimator) {
                com.android.server.display.DisplayPowerController.this.updateFpsIfNeeded(com.android.server.display.DisplayPowerController.this.mDpcExt.getMaximumScreenBrightnessSetting());
                com.android.server.display.DisplayPowerController.this.mDpcExt.setLowPowerAnimatingState(false);
                com.android.server.display.DisplayPowerController.this.mDpcExt.setHDRAnimatingState(false);
                android.os.Message msg = com.android.server.display.DisplayPowerController.this.mHandler.obtainMessage(10);
                com.android.server.display.DisplayPowerController.this.mHandler.sendMessageAtTime(msg, com.android.server.display.DisplayPowerController.this.mClock.uptimeMillis());
            }
            com.android.server.display.DisplayPowerController.this.mDpcExt.setAnimating(false, isPrimaryAnimator);
        }
    };
    private final java.lang.Runnable mCleanListener = new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda7
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.sendUpdatePowerState();
        }
    };
    private final com.android.server.display.IOplusDisplayPowerControllerWrapper mWrapper = new com.android.server.display.DisplayPowerController.OplusDisplayPowerControllerWrapper();

    interface Clock {
        long uptimeMillis();
    }

    DisplayPowerController(android.content.Context context, com.android.server.display.DisplayPowerController.Injector injector, android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks callbacks, android.os.Handler handler, android.hardware.SensorManager sensorManager, com.android.server.display.DisplayBlanker blanker, com.android.server.display.LogicalDisplay logicalDisplay, com.android.server.display.BrightnessTracker brightnessTracker, com.android.server.display.BrightnessSetting brightnessSetting, java.lang.Runnable onBrightnessChangeRunnable, com.android.server.display.HighBrightnessModeMetadata hbmMetadata, boolean bootCompleted, com.android.server.display.feature.DisplayManagerFlags flags) {
        this.mIsPrimaryDisplay = false;
        this.mFlags = flags;
        this.mInjector = injector != null ? injector : new com.android.server.display.DisplayPowerController.Injector();
        this.mClock = this.mInjector.getClock();
        this.mLogicalDisplay = logicalDisplay;
        this.mDisplayId = this.mLogicalDisplay.getDisplayIdLocked();
        this.mSensorManager = sensorManager;
        this.mHandler = new com.android.server.display.DisplayPowerController.DisplayControllerHandler(handler.getLooper());
        this.mDisplayDeviceConfig = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig();
        this.mIsEnabled = logicalDisplay.isEnabledLocked();
        this.mIsInTransition = logicalDisplay.isInTransitionLocked();
        this.mIsDisplayInternal = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked().type == 1;
        this.mDpcExt = (com.android.server.display.IOplusDisplayPowerControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IOplusDisplayPowerControllerExt.class).base(this).create();
        this.mWakelockController = this.mInjector.getWakelockController(this.mDisplayId, callbacks);
        this.mDisplayPowerProximityStateController = this.mInjector.getDisplayPowerProximityStateController(this.mWakelockController, this.mDisplayDeviceConfig, this.mHandler.getLooper(), new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$new$0();
            }
        }, this.mDisplayId, this.mSensorManager, this.mDpcExt);
        this.mDisplayStateController = new com.android.server.display.state.DisplayStateController(this.mDisplayPowerProximityStateController, this.mDpcExt);
        this.mTag = "DisplayPowerController[" + this.mDisplayId + "]";
        this.mThermalBrightnessThrottlingDataId = logicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId;
        this.mDisplayDevice = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        this.mUniqueDisplayId = logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        this.mDisplayStatsId = this.mUniqueDisplayId.hashCode();
        this.mLastBrightnessEvent = new com.android.server.display.brightness.BrightnessEvent(this.mDisplayId);
        this.mTempBrightnessEvent = new com.android.server.display.brightness.BrightnessEvent(this.mDisplayId);
        if (this.mDisplayId == 0) {
            this.mBatteryStats = com.android.server.am.BatteryStatsService.getService();
        } else {
            this.mBatteryStats = null;
        }
        this.mSettingsObserver = new com.android.server.display.DisplayPowerController.SettingsObserver(this.mHandler);
        this.mWindowManagerPolicy = (com.android.server.policy.WindowManagerPolicy) com.android.server.LocalServices.getService(com.android.server.policy.WindowManagerPolicy.class);
        this.mBlanker = blanker;
        this.mContext = context;
        this.mBrightnessTracker = brightnessTracker;
        this.mLightSensor = this.mSensorManager.getDefaultSensor(5);
        this.mDpcExt.init(this.mContext, this.mDisplayId);
        if (this.mDisplayId == 0) {
            this.mDpcExt.setDisplayPowerController(this);
            this.mDpcExt.setOplusDisplayPowerControllerCallback(callbacks);
            this.mDpcExt.setDisplayPowerControlHandler(handler);
        }
        this.mOnBrightnessChangeRunnable = onBrightnessChangeRunnable;
        android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        android.content.res.Resources resources = context.getResources();
        this.mScreenBrightnessDozeConfig = com.android.server.display.brightness.BrightnessUtils.clampAbsoluteBrightness(pm.getBrightnessConstraint(4));
        loadBrightnessRampRates();
        this.mSkipScreenOnBrightnessRamp = resources.getBoolean(android.R.bool.config_satellite_should_notify_availability);
        this.mDozeScaleFactor = context.getResources().getFraction(android.R.fraction.config_screenAutoBrightnessDozeScaleFactor, 1, 1);
        java.lang.Runnable modeChangeCallback = new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        };
        com.android.server.display.HighBrightnessModeController hbmController = createHbmControllerLocked(hbmMetadata, modeChangeCallback);
        this.mBrightnessThrottler = createBrightnessThrottlerLocked();
        this.mBrightnessRangeController = this.mInjector.getBrightnessRangeController(hbmController, modeChangeCallback, this.mDisplayDeviceConfig, this.mHandler, flags, this.mDisplayDevice.getDisplayTokenLocked(), this.mDisplayDevice.getDisplayDeviceInfoLocked());
        this.mDisplayBrightnessController = new com.android.server.display.brightness.DisplayBrightnessController(context, null, this.mDisplayId, this.mLogicalDisplay.getDisplayInfoLocked().brightnessDefault, brightnessSetting, new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$2();
            }
        }, new android.os.HandlerExecutor(this.mHandler), flags, this.mDpcExt);
        com.android.server.display.DisplayPowerController.Injector injector2 = this.mInjector;
        com.android.server.display.DisplayPowerController.DisplayControllerHandler displayControllerHandler = this.mHandler;
        java.util.Objects.requireNonNull(modeChangeCallback);
        this.mBrightnessClamperController = injector2.getBrightnessClamperController(displayControllerHandler, new com.android.server.display.BrightnessRangeController$$ExternalSyntheticLambda5(modeChangeCallback), new com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData(this.mUniqueDisplayId, this.mThermalBrightnessThrottlingDataId, logicalDisplay.getPowerThrottlingDataIdLocked(), this.mDisplayDeviceConfig), this.mContext, flags, this.mSensorManager, this.mDpcExt);
        saveBrightnessInfo(getScreenBrightnessSetting());
        this.mAutomaticBrightnessStrategy = this.mDisplayBrightnessController.getAutomaticBrightnessStrategy();
        com.android.server.display.whitebalance.DisplayWhiteBalanceSettings displayWhiteBalanceSettings = null;
        com.android.server.display.whitebalance.DisplayWhiteBalanceController displayWhiteBalanceController = null;
        if (this.mDisplayId == 0) {
            try {
                displayWhiteBalanceController = this.mInjector.getDisplayWhiteBalanceController(this.mHandler, this.mSensorManager, resources);
                displayWhiteBalanceSettings = new com.android.server.display.whitebalance.DisplayWhiteBalanceSettings(this.mContext, this.mHandler);
                displayWhiteBalanceSettings.setCallbacks(this);
                displayWhiteBalanceController.setCallbacks(this);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(this.mTag, "failed to set up display white-balance: " + e);
            }
        }
        this.mDisplayWhiteBalanceSettings = displayWhiteBalanceSettings;
        this.mDisplayWhiteBalanceController = displayWhiteBalanceController;
        loadNitsRange(resources);
        if (this.mDisplayId == 0) {
            this.mCdsi = (com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal) com.android.server.LocalServices.getService(com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal.class);
            if (this.mCdsi != null) {
                boolean active = this.mCdsi.setReduceBrightColorsListener(new com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener() { // from class: com.android.server.display.DisplayPowerController.1
                    @Override // com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener
                    public void onReduceBrightColorsActivationChanged(boolean activated, boolean userInitiated) {
                        com.android.server.display.DisplayPowerController.this.applyReduceBrightColorsSplineAdjustment();
                    }

                    @Override // com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener
                    public void onReduceBrightColorsStrengthChanged(int strength) {
                        com.android.server.display.DisplayPowerController.this.applyReduceBrightColorsSplineAdjustment();
                    }
                });
                if (active) {
                    applyReduceBrightColorsSplineAdjustment();
                }
            }
        } else {
            this.mCdsi = null;
        }
        setUpAutoBrightness(context, handler);
        this.mColorFadeEnabled = this.mInjector.isColorFadeEnabled() && !resources.getBoolean(android.R.bool.config_displayBlanksAfterDoze);
        this.mColorFadeFadesConfig = resources.getBoolean(android.R.bool.config_alwaysScaleWallpaper);
        this.mDisplayBlanksAfterDozeConfig = resources.getBoolean(android.R.bool.config_disable_all_cb_messages);
        this.mBrightnessBucketsInDozeConfig = resources.getBoolean(android.R.bool.config_dismissDreamOnActivityStart);
        this.mDpcExt.initParameters(this.mHandler);
        this.mIsPrimaryDisplay = this.mDpcExt.isPrimaryDisplay(this.mUniqueDisplayId);
        this.mDpcExt.setUniqueDisplayId(this.mIsPrimaryDisplay, this.mUniqueDisplayId);
        this.mDpcExt.setDCMode();
        android.util.Slog.d(this.mTag, "DPC construct " + this.mUniqueDisplayId + " mIsPrimaryDisplay:" + this.mIsPrimaryDisplay);
        java.lang.String isReset = android.os.SystemProperties.get("debug.display.cabc.reset", "0");
        this.isRM = "1".equals(isReset);
        this.mBootCompleted = bootCompleted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        sendUpdatePowerState();
        lambda$new$2();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyReduceBrightColorsSplineAdjustment() {
        this.mHandler.obtainMessage(9).sendToTarget();
        sendUpdatePowerState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRbcChanged() {
        if (this.mAutomaticBrightnessController == null) {
            return;
        }
        float[] adjustedNits = new float[this.mNitsRange.length];
        for (int i = 0; i < this.mNitsRange.length; i++) {
            adjustedNits[i] = this.mCdsi.getReduceBrightColorsAdjustedBrightnessNits(this.mNitsRange[i]);
        }
        this.mIsRbcActive = this.mCdsi.isReduceBrightColorsActivated();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public boolean isProximitySensorAvailable() {
        return this.mDisplayPowerProximityStateController.isProximitySensorAvailable();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public android.content.pm.ParceledListSlice<android.hardware.display.BrightnessChangeEvent> getBrightnessEvents(int userId, boolean includePackage) {
        if (this.mBrightnessTracker == null) {
            return null;
        }
        return this.mBrightnessTracker.getEvents(userId, includePackage);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onSwitchUser(int newUserId, int userSerial, float newBrightness) {
        float currentBrightness = this.mDisplayBrightnessController.getCurrentBrightness();
        android.os.Message msg = this.mHandler.obtainMessage(12, newUserId, userSerial, java.lang.Float.valueOf(currentBrightness));
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnSwitchUser(int newUserId, int userSerial, float newBrightness) {
        android.util.Slog.i(this.mTag, "Switching user newUserId=" + newUserId + " userSerial=" + userSerial + " newBrightness=" + newBrightness);
        this.mIsUserSwitching = true;
        if (this.mDpcExt.onSwitchUser(newUserId, this.mDisplayBrightnessController.getCurrentBrightness(), (this.mBrightnessReason.getModifier() & 1) == 1)) {
            this.mIsUserSwitching = false;
            return;
        }
        this.mIsUserSwitching = false;
        handleBrightnessModeChange();
        if (this.mBrightnessTracker != null) {
            this.mBrightnessTracker.onSwitchUser(newUserId);
        }
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.resetShortTermModel();
        }
        sendUpdatePowerState();
        this.mDpcExt.setDCMode();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public android.content.pm.ParceledListSlice<android.hardware.display.AmbientBrightnessDayStats> getAmbientBrightnessStats(int userId) {
        if (this.mBrightnessTracker == null) {
            return null;
        }
        return this.mBrightnessTracker.getAmbientBrightnessStats(userId);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void persistBrightnessTrackerState() {
        if (this.mBrightnessTracker != null) {
            this.mBrightnessTracker.persistBrightnessTrackerState();
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public boolean requestPowerState(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, boolean waitForNegativeProximity) {
        synchronized (this.mLock) {
            if (this.mStopped) {
                return true;
            }
            boolean changed = this.mDisplayPowerProximityStateController.setPendingWaitForNegativeProximityLocked(waitForNegativeProximity);
            if (this.mPendingRequestLocked == null) {
                this.mPendingRequestLocked = new android.hardware.display.DisplayManagerInternal.DisplayPowerRequest(request);
                changed = true;
            } else if (!this.mPendingRequestLocked.equals(request)) {
                this.mPendingRequestLocked.copyFrom(request);
                changed = true;
            }
            if (this.mDpcExt.isUseProximityForceSuspendStateChanged(this.mDisplayId)) {
                this.mPendingRequestLocked.copyFrom(request);
                changed = true;
            }
            if (changed) {
                this.mDisplayReadyLocked = false;
                if (!this.mPendingRequestChangedLocked) {
                    this.mPendingRequestChangedLocked = true;
                    this.mDpcExt.updateBrightnessAnimationStatus(this.mPowerState, this.mPendingRequestLocked.policy, this.mLogicalDisplay, this.mDisplayId);
                    sendUpdatePowerStateLocked();
                }
            }
            this.mDpcExt.setPowerRequestPolicy(request.policy);
            if (changed) {
                android.util.Slog.d(this.mTag, "requestPowerState: " + request + ", waitForNegativeProximity=" + waitForNegativeProximity + " displayReady=" + this.mDisplayReadyLocked + " pendingRequest=" + this.mPendingRequestChangedLocked);
            }
            return this.mDisplayReadyLocked;
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void overrideDozeScreenState(final int displayState, final int reason) {
        android.util.Slog.i(TAG, "New offload doze override: " + android.view.Display.stateToString(displayState));
        this.mHandler.postAtTime(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$overrideDozeScreenState$3(displayState, reason);
            }
        }, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$overrideDozeScreenState$3(int displayState, int reason) throws java.lang.Throwable {
        if (this.mDisplayOffloadSession != null) {
            if (!android.hardware.display.DisplayManagerInternal.DisplayOffloadSession.isSupportedOffloadState(displayState) && displayState != 0) {
                return;
            }
            this.mDisplayStateController.overrideDozeScreenState(displayState, reason);
            lambda$new$0();
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setDisplayOffloadSession(android.hardware.display.DisplayManagerInternal.DisplayOffloadSession session) {
        if (session == this.mDisplayOffloadSession) {
            return;
        }
        unblockScreenOnByDisplayOffload();
        this.mDisplayOffloadSession = session;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public android.hardware.display.BrightnessConfiguration getDefaultBrightnessConfiguration() {
        if (this.mAutomaticBrightnessController == null) {
            return null;
        }
        return this.mAutomaticBrightnessController.getDefaultConfig();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onDisplayChanged(final com.android.server.display.HighBrightnessModeMetadata hbmMetadata, int leadDisplayId) {
        this.mLeadDisplayId = leadDisplayId;
        final com.android.server.display.DisplayDevice device = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        if (device == null) {
            android.util.Slog.wtf(this.mTag, "Display Device is null in DisplayPowerController2 for display: " + this.mLogicalDisplay.getDisplayIdLocked());
            return;
        }
        final java.lang.String uniqueId = device.getUniqueId();
        final com.android.server.display.DisplayDeviceConfig config = device.getDisplayDeviceConfig();
        final android.os.IBinder token = device.getDisplayTokenLocked();
        final com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        final boolean isEnabled = this.mLogicalDisplay.isEnabledLocked();
        final boolean isInTransition = this.mLogicalDisplay.isInTransitionLocked();
        final boolean isDisplayInternal = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked() != null && this.mLogicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked().type == 1;
        final java.lang.String thermalBrightnessThrottlingDataId = this.mLogicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId;
        final int displayId = this.mLogicalDisplay.getDisplayIdLocked();
        final java.lang.String powerThrottlingDataId = this.mLogicalDisplay.getPowerThrottlingDataIdLocked();
        this.mHandler.postAtTime(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$onDisplayChanged$4(isEnabled, isInTransition, device, uniqueId, config, thermalBrightnessThrottlingDataId, token, info, hbmMetadata, isDisplayInternal, powerThrottlingDataId, displayId);
            }
        }, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayChanged$4(boolean isEnabled, boolean isInTransition, com.android.server.display.DisplayDevice device, java.lang.String uniqueId, com.android.server.display.DisplayDeviceConfig config, java.lang.String thermalBrightnessThrottlingDataId, android.os.IBinder token, com.android.server.display.DisplayDeviceInfo info, com.android.server.display.HighBrightnessModeMetadata hbmMetadata, boolean isDisplayInternal, java.lang.String powerThrottlingDataId, int displayId) throws java.lang.Throwable {
        boolean changed = false;
        if (this.mIsEnabled != isEnabled || this.mIsInTransition != isInTransition) {
            changed = true;
            this.mIsEnabled = isEnabled;
            this.mIsInTransition = isInTransition;
        }
        if (this.mDisplayDevice == device) {
            if (!java.util.Objects.equals(this.mThermalBrightnessThrottlingDataId, thermalBrightnessThrottlingDataId)) {
                changed = true;
                this.mThermalBrightnessThrottlingDataId = thermalBrightnessThrottlingDataId;
                this.mBrightnessThrottler.loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(config.getThermalBrightnessThrottlingDataMapByThrottlingId(), config.getTempSensor(), this.mThermalBrightnessThrottlingDataId, this.mUniqueDisplayId);
            }
        } else {
            changed = true;
            this.mDisplayDevice = device;
            this.mUniqueDisplayId = uniqueId;
            this.mDisplayStatsId = this.mUniqueDisplayId.hashCode();
            this.mDisplayDeviceConfig = config;
            this.mThermalBrightnessThrottlingDataId = thermalBrightnessThrottlingDataId;
            loadFromDisplayDeviceConfig(token, info, hbmMetadata);
            this.mDisplayPowerProximityStateController.notifyDisplayDeviceChanged(config);
            this.mPowerState.resetScreenState();
        }
        this.mIsDisplayInternal = isDisplayInternal;
        this.mBrightnessClamperController.onDisplayChanged(new com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData(uniqueId, thermalBrightnessThrottlingDataId, powerThrottlingDataId, config));
        if (changed || this.mWrapper.getLogicalDisplayMapper().isRemapDisabledSecondaryDisplayId(displayId)) {
            if (this.mDpcExt != null) {
                this.mIsPrimaryDisplay = this.mDpcExt.isPrimaryDisplay(this.mUniqueDisplayId);
                this.mDpcExt.setUniqueDisplayId(this.mIsPrimaryDisplay, this.mUniqueDisplayId);
            }
            if (this.mScreenBrightnessRampAnimator != null) {
                this.mScreenBrightnessRampAnimator.setDisplayId(displayId, this.mIsPrimaryDisplay);
            } else {
                android.util.Slog.e(this.mTag, "mScreenBrightnessRampAnimator is null, current dpc is " + this);
            }
            android.util.Slog.d(this.mTag, "onDisplayChanged id=" + displayId + " uniqueDisplayId=" + uniqueId + " enable=" + this.mLogicalDisplay.isEnabledLocked());
            lambda$new$0();
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void stop() {
        synchronized (this.mLock) {
            clearDisplayBrightnessFollowersLocked();
            this.mStopped = true;
            android.os.Message msg = this.mHandler.obtainMessage(7);
            this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
            if (this.mAutomaticBrightnessController != null) {
                this.mAutomaticBrightnessController.stop();
            }
            this.mDisplayBrightnessController.stop();
            this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        }
    }

    private void loadFromDisplayDeviceConfig(android.os.IBinder token, com.android.server.display.DisplayDeviceInfo info, com.android.server.display.HighBrightnessModeMetadata hbmMetadata) {
        loadBrightnessRampRates();
        loadNitsRange(this.mContext.getResources());
        setUpAutoBrightness(this.mContext, this.mHandler);
        reloadReduceBrightColours();
        setAnimatorRampSpeeds(false);
        this.mBrightnessRangeController.loadFromConfig(hbmMetadata, token, info, this.mDisplayDeviceConfig);
        this.mBrightnessThrottler.loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(this.mDisplayDeviceConfig.getThermalBrightnessThrottlingDataMapByThrottlingId(), this.mDisplayDeviceConfig.getTempSensor(), this.mThermalBrightnessThrottlingDataId, this.mUniqueDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUpdatePowerState() {
        synchronized (this.mLock) {
            sendUpdatePowerStateLocked();
        }
    }

    private void sendUpdatePowerStateLocked() {
        if (!this.mStopped && !this.mPendingUpdatePowerStateLocked) {
            this.mPendingUpdatePowerStateLocked = true;
            android.os.Message msg = this.mHandler.obtainMessage(1);
            this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
        }
    }

    private void initialize(int displayState) {
        this.mPowerState = this.mInjector.getDisplayPowerState(this.mBlanker, this.mColorFadeEnabled ? new com.android.server.display.ColorFade(this.mDisplayId) : null, this.mDisplayId, displayState, this.mDpcExt);
        if (this.mColorFadeEnabled) {
            this.mColorFadeOnAnimator = android.animation.ObjectAnimator.ofFloat(this.mPowerState, com.android.server.display.DisplayPowerState.COLOR_FADE_LEVEL, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f);
            this.mColorFadeOnAnimator.setDuration(250L);
            this.mColorFadeOnAnimator.addListener(this.mAnimatorListener);
            this.mColorFadeOffAnimator = android.animation.ObjectAnimator.ofFloat(this.mPowerState, com.android.server.display.DisplayPowerState.COLOR_FADE_LEVEL, 1.0f, SCREEN_ANIMATION_RATE_MINIMUM);
            this.mColorFadeOffAnimator.setDuration(100L);
            this.mColorFadeOffAnimator.addListener(this.mAnimatorListener);
        }
        this.mScreenBrightnessRampAnimator = this.mInjector.getDualRampAnimator(this.mPowerState, com.android.server.display.DisplayPowerState.SCREEN_BRIGHTNESS_FLOAT, com.android.server.display.DisplayPowerState.SCREEN_SDR_BRIGHTNESS_FLOAT);
        this.mScreenBrightnessRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeMillis, this.mBrightnessRampDecreaseMaxTimeMillis);
        this.mDpcExt.setPowerState(this.mPowerState);
        this.mScreenBrightnessRampAnimator.setDisplayId(this.mDisplayId, this.mIsPrimaryDisplay);
        android.util.Slog.d(this.mTag, "in initialize current dpc is " + this);
        this.mScreenBrightnessRampAnimator.setListener(this.mRampAnimatorListener);
        noteScreenState(this.mPowerState.getScreenState(), 1);
        noteScreenBrightness(this.mPowerState.getScreenBrightness());
        float brightness = this.mDisplayBrightnessController.convertToAdjustedNits(this.mPowerState.getScreenBrightness());
        if (this.mBrightnessTracker != null && brightness >= SCREEN_ANIMATION_RATE_MINIMUM) {
            this.mBrightnessTracker.start(brightness);
        }
        com.android.server.display.BrightnessSetting.BrightnessSettingListener brightnessSettingListener = new com.android.server.display.BrightnessSetting.BrightnessSettingListener() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda1
            @Override // com.android.server.display.BrightnessSetting.BrightnessSettingListener
            public final void onBrightnessChanged(float f) {
                this.f$0.lambda$initialize$5(f);
            }
        };
        this.mDisplayBrightnessController.registerBrightnessSettingChangeListener(brightnessSettingListener);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("screen_auto_brightness_adj"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor(GLOBAL_HBM_SELL_MODE), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("screen_auto_brightness_adj_talkback"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness"), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor(SECOND_SCREEN_AUTO_BRIGHTNESS_ADJ), false, this.mSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness_mode"), false, this.mSettingsObserver, -1);
        if (this.mFlags.areAutoBrightnessModesEnabled()) {
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness_for_als"), false, this.mSettingsObserver, -2);
        }
        handleBrightnessModeChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$5(float brightnessValue) {
        android.os.Message msg = this.mHandler.obtainMessage(8, java.lang.Float.valueOf(brightnessValue));
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUpAutoBrightness(android.content.Context context, android.os.Handler handler) {
        com.android.server.display.BrightnessMappingStrategy idleModeBrightnessMapper;
        this.mUseSoftwareAutoBrightnessConfig = this.mDisplayDeviceConfig.isAutoBrightnessAvailable() && (this.mDisplayId == 0 || this.mDpcExt.useSoftwareAutoBrightnessConfigInOtherDisplay(this.mDisplayId));
        if (!this.mUseSoftwareAutoBrightnessConfig && !this.mDpcExt.hasRemapDisable()) {
            return;
        }
        android.util.SparseArray<com.android.server.display.BrightnessMappingStrategy> brightnessMappers = new android.util.SparseArray<>();
        com.android.server.display.BrightnessMappingStrategy defaultModeBrightnessMapper = this.mInjector.getDefaultModeBrightnessMapper(context, this.mDisplayDeviceConfig, this.mDisplayWhiteBalanceController);
        brightnessMappers.append(0, defaultModeBrightnessMapper);
        boolean isIdleScreenBrightnessEnabled = context.getResources().getBoolean(android.R.bool.config_enableGeofenceOverlay);
        if (isIdleScreenBrightnessEnabled && (idleModeBrightnessMapper = com.android.server.display.BrightnessMappingStrategy.create(context, this.mDisplayDeviceConfig, 1, this.mDisplayWhiteBalanceController)) != null) {
            brightnessMappers.append(1, idleModeBrightnessMapper);
        }
        com.android.server.display.BrightnessMappingStrategy dozeModeBrightnessMapper = com.android.server.display.BrightnessMappingStrategy.create(context, this.mDisplayDeviceConfig, 2, this.mDisplayWhiteBalanceController);
        if (this.mFlags.areAutoBrightnessModesEnabled() && dozeModeBrightnessMapper != null) {
            brightnessMappers.put(2, dozeModeBrightnessMapper);
        }
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.getUserLux();
            this.mAutomaticBrightnessController.getUserNits();
        }
        if (defaultModeBrightnessMapper == null) {
            this.mUseSoftwareAutoBrightnessConfig = false;
            return;
        }
        this.mDisplayDeviceConfig.getAmbientBrightnessHysteresis();
        this.mDisplayDeviceConfig.getScreenBrightnessHysteresis();
        this.mDisplayDeviceConfig.getAmbientBrightnessIdleHysteresis();
        this.mDisplayDeviceConfig.getScreenBrightnessIdleHysteresis();
        this.mDisplayDeviceConfig.getAutoBrightnessBrighteningLightDebounce();
        long darkeningLightDebounce = this.mDisplayDeviceConfig.getAutoBrightnessDarkeningLightDebounce();
        this.mDisplayDeviceConfig.getAutoBrightnessBrighteningLightDebounceIdle();
        this.mDisplayDeviceConfig.getAutoBrightnessDarkeningLightDebounceIdle();
        context.getResources().getBoolean(android.R.bool.config_assistTouchGestureEnabledDefault);
        context.getResources().getInteger(android.R.integer.config_jobSchedulerInactivityIdleThresholdOnStablePower);
        int lightSensorRate = context.getResources().getInteger(android.R.integer.config_audio_notif_vol_steps);
        int initialLightSensorRate = context.getResources().getInteger(android.R.integer.config_audio_notif_vol_default);
        if (initialLightSensorRate != -1 && initialLightSensorRate > lightSensorRate) {
            android.util.Slog.w(this.mTag, "Expected config_autoBrightnessInitialLightSensorRate (" + initialLightSensorRate + ") to be less than or equal to config_autoBrightnessLightSensorRate (" + lightSensorRate + ").");
        }
        loadAmbientLightSensor();
        if (this.mBrightnessTracker != null && this.mDisplayId == 0) {
            this.mBrightnessTracker.setLightSensor(this.mLightSensor);
        }
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.stop();
        }
        this.mDpcExt.stop(this.mIsPrimaryDisplay);
        this.mAutomaticBrightnessController = this.mDpcExt.initAutomaticBrightnessController(this, handler.getLooper(), this.mSensorManager, this.mLightSensor, defaultModeBrightnessMapper, this.mDozeScaleFactor, lightSensorRate, darkeningLightDebounce);
        this.mDisplayBrightnessController.setUpAutoBrightness(this.mAutomaticBrightnessController, this.mSensorManager, this.mDisplayDeviceConfig, this.mHandler, defaultModeBrightnessMapper, this.mIsEnabled, this.mLeadDisplayId);
        this.mBrightnessEventRingBuffer = new com.android.internal.util.RingBuffer<>(com.android.server.display.brightness.BrightnessEvent.class, 100);
        if (!this.mFlags.isRefactorDisplayPowerControllerEnabled()) {
            if (this.mScreenOffBrightnessSensorController != null) {
                this.mScreenOffBrightnessSensorController.stop();
                this.mScreenOffBrightnessSensorController = null;
            }
            loadScreenOffBrightnessSensor();
            int[] sensorValueToLux = this.mDisplayDeviceConfig.getScreenOffBrightnessSensorValueToLux();
            if (this.mScreenOffBrightnessSensor != null && sensorValueToLux != null) {
                this.mScreenOffBrightnessSensorController = this.mInjector.getScreenOffBrightnessSensorController(this.mSensorManager, this.mScreenOffBrightnessSensor, this.mHandler, new com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda2(), sensorValueToLux, defaultModeBrightnessMapper);
            }
        }
    }

    private void loadBrightnessRampRates() {
        this.mBrightnessRampRateFastDecrease = this.mDisplayDeviceConfig.getBrightnessRampFastDecrease();
        this.mBrightnessRampRateFastIncrease = this.mDisplayDeviceConfig.getBrightnessRampFastIncrease();
        this.mBrightnessRampRateSlowDecrease = this.mDisplayDeviceConfig.getBrightnessRampSlowDecrease();
        this.mBrightnessRampRateSlowIncrease = this.mDisplayDeviceConfig.getBrightnessRampSlowIncrease();
        this.mBrightnessRampRateSlowDecreaseIdle = this.mDisplayDeviceConfig.getBrightnessRampSlowDecreaseIdle();
        this.mBrightnessRampRateSlowIncreaseIdle = this.mDisplayDeviceConfig.getBrightnessRampSlowIncreaseIdle();
        this.mBrightnessRampDecreaseMaxTimeMillis = this.mDisplayDeviceConfig.getBrightnessRampDecreaseMaxMillis();
        this.mBrightnessRampIncreaseMaxTimeMillis = this.mDisplayDeviceConfig.getBrightnessRampIncreaseMaxMillis();
        this.mBrightnessRampDecreaseMaxTimeIdleMillis = this.mDisplayDeviceConfig.getBrightnessRampDecreaseMaxIdleMillis();
        this.mBrightnessRampIncreaseMaxTimeIdleMillis = this.mDisplayDeviceConfig.getBrightnessRampIncreaseMaxIdleMillis();
    }

    private void loadNitsRange(android.content.res.Resources resources) {
        if (this.mDisplayDeviceConfig != null && this.mDisplayDeviceConfig.getNits() != null) {
            this.mNitsRange = this.mDisplayDeviceConfig.getNits();
        } else {
            android.util.Slog.w(this.mTag, "Screen brightness nits configuration is unavailable; falling back");
            this.mNitsRange = com.android.server.display.BrightnessMappingStrategy.getFloatArray(resources.obtainTypedArray(android.R.array.config_roundedCornerRadiusArray));
        }
    }

    private void reloadReduceBrightColours() {
        if (this.mCdsi != null && this.mCdsi.isReduceBrightColorsActivated()) {
            applyReduceBrightColorsSplineAdjustment();
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAutomaticScreenBrightnessMode(int mode) {
        android.os.Message msg = this.mHandler.obtainMessage();
        msg.what = 14;
        msg.arg1 = mode;
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    private void setAnimatorRampSpeeds(boolean isIdle) {
        if (this.mScreenBrightnessRampAnimator == null) {
            return;
        }
        if (this.mFlags.isAdaptiveTone1Enabled() && isIdle) {
            this.mScreenBrightnessRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeIdleMillis, this.mBrightnessRampDecreaseMaxTimeIdleMillis);
        } else {
            this.mScreenBrightnessRampAnimator.setAnimationTimeLimits(this.mBrightnessRampIncreaseMaxTimeMillis, this.mBrightnessRampDecreaseMaxTimeMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupHandlerThreadAfterStop() {
        float brightness;
        this.mDisplayPowerProximityStateController.cleanup();
        this.mBrightnessRangeController.stop();
        this.mBrightnessThrottler.stop();
        this.mBrightnessClamperController.stop();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mWakelockController.releaseAll();
        if (this.mPowerState != null) {
            brightness = this.mPowerState.getScreenBrightness();
        } else {
            brightness = SCREEN_ANIMATION_RATE_MINIMUM;
        }
        reportStats(brightness);
        if (this.mPowerState != null) {
            this.mPowerState.stop();
            this.mPowerState = null;
        }
        if (!this.mFlags.isRefactorDisplayPowerControllerEnabled() && this.mScreenOffBrightnessSensorController != null) {
            this.mScreenOffBrightnessSensorController.stop();
        }
        if (this.mDisplayWhiteBalanceController != null) {
            this.mDisplayWhiteBalanceController.setEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updatePowerState, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() throws java.lang.Throwable {
        android.os.Trace.traceBegin(131072L, "DisplayPowerController#updatePowerState");
        updatePowerStateInternal();
        android.os.Trace.traceEnd(131072L);
    }

    /* JADX WARN: Removed duplicated region for block: B:191:0x03bf  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x03ff  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x043d  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x044d  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x04e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x04f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:270:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x05d2  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x05dd  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x05e4  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0615  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0623  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0635  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0643 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:299:0x0645 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x06a3  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x06c2  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x06c7  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x06f3  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x06f7  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x0701  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x0705  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x074d  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x07ab  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x07b7  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x07c1  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x07c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:381:0x07f8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:395:0x0823  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x082c  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x082e  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0837  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0887  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x088f  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0807 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updatePowerStateInternal() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2208
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.DisplayPowerController.updatePowerStateInternal():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDwbcOverride(float cct) throws java.lang.Throwable {
        if (this.mDisplayWhiteBalanceController != null) {
            this.mDisplayWhiteBalanceController.setAmbientColorTemperatureOverride(cct);
            lambda$new$0();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDwbcStrongMode(int arg) {
        if (this.mDisplayWhiteBalanceController != null) {
            boolean isIdle = arg == 1;
            this.mDisplayWhiteBalanceController.setStrongModeEnabled(isIdle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDwbcLoggingEnabled(int arg) {
        if (this.mDisplayWhiteBalanceController != null) {
            boolean enabled = arg == 1;
            this.mDisplayWhiteBalanceController.setLoggingEnabled(enabled);
            this.mDisplayWhiteBalanceSettings.setLoggingEnabled(enabled);
        }
    }

    public void updateFpsWhenDcChange(boolean enter) {
        if (this.mDCBrightnessChange == enter) {
            return;
        }
        android.util.Slog.d(this.mTag, "debug enter: " + enter);
        this.mDpcExt.updateFpsWhenDcChange(enter);
        this.mDCBrightnessChange = enter;
    }

    public void updateFpsIfNeeded(float brightness) {
        boolean tmpMode = this.mDpcExt.isDCMode() && brightness < ((float) DC_MODE_BRIGHT_EDGE) && this.mScreenBrightnessRampAnimator.isAnimating();
        if (DC_MODE_BRIGHT_CUSTOMIZATION) {
            if (this.mUpdateFpsForDc != tmpMode) {
                if (tmpMode) {
                    this.mResetFpsStatePending = false;
                    updateFpsWhenDcChange(true);
                } else {
                    this.mResetFpsStatePending = true;
                    this.mHandler.removeMessages(21);
                    android.os.Message msg = this.mHandler.obtainMessage(21);
                    this.mHandler.sendMessageDelayed(msg, 1000L);
                }
            }
            this.mUpdateFpsForDc = tmpMode;
        }
    }

    @Override // com.android.server.display.AutomaticBrightnessController.Callbacks
    public void updateBrightness() {
        sendUpdatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void ignoreProximitySensorUntilChanged() {
        this.mDisplayPowerProximityStateController.ignoreProximitySensorUntilChanged();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration brightnessConfiguration, boolean z) {
        this.mHandler.obtainMessage(4, z ? 1 : 0, 0, brightnessConfiguration).sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setTemporaryBrightness(float brightness) {
        android.os.Message msg = this.mHandler.obtainMessage(5, java.lang.Float.floatToIntBits(brightness), 0);
        msg.sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setTemporaryAutoBrightnessAdjustment(float adjustment) {
        this.mDpcExt.setTemporaryAutoBrightnessAdjustment(adjustment);
        if (adjustment == this.mDpcExt.getAdjustmentGalleryIn() || adjustment == this.mDpcExt.getAdjustmentGalleryOut()) {
            if (!this.mDpcExt.isGalleryBrightnessEnhanceSupport()) {
                return;
            } else {
                android.util.Slog.d(this.mTag, "setTemporaryAutoBrightnessAdjustment=" + adjustment);
            }
        }
        android.os.Message msg = this.mHandler.obtainMessage(6, java.lang.Float.floatToIntBits(adjustment), 0);
        msg.sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightnessFromOffload(float brightness) {
        android.os.Message msg = this.mHandler.obtainMessage(17, java.lang.Float.floatToIntBits(brightness), 0);
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public float[] getAutoBrightnessLevels(int mode) {
        int preset = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_for_als", 2, -2);
        return this.mDisplayDeviceConfig.getAutoBrightnessBrighteningLevels(mode, preset);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public float[] getAutoBrightnessLuxLevels(int mode) {
        int preset = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_for_als", 2, -2);
        return this.mDisplayDeviceConfig.getAutoBrightnessBrighteningLevelsLux(mode, preset);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public android.hardware.display.BrightnessInfo getBrightnessInfo() {
        android.hardware.display.BrightnessInfo brightnessInfo;
        synchronized (this.mCachedBrightnessInfo) {
            brightnessInfo = new android.hardware.display.BrightnessInfo(this.mCachedBrightnessInfo.brightness.value, this.mCachedBrightnessInfo.adjustedBrightness.value, this.mCachedBrightnessInfo.brightnessMin.value, this.mCachedBrightnessInfo.brightnessMax.value, this.mCachedBrightnessInfo.hbmMode.value, this.mCachedBrightnessInfo.hbmTransitionPoint.value, this.mCachedBrightnessInfo.brightnessMaxReason.value);
        }
        return brightnessInfo;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void onBootCompleted() {
        android.os.Message msg = this.mHandler.obtainMessage(13);
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    private boolean saveBrightnessInfo(float brightness) {
        return saveBrightnessInfo(brightness, null);
    }

    private boolean saveBrightnessInfo(float brightness, com.android.server.display.DisplayBrightnessState state) {
        return saveBrightnessInfo(brightness, brightness, state);
    }

    private boolean saveBrightnessInfo(float brightness, float adjustedBrightness, com.android.server.display.DisplayBrightnessState state) {
        boolean changed;
        synchronized (this.mCachedBrightnessInfo) {
            float stateMax = state != null ? state.getMaxBrightness() : this.mScreenBrightnessNormalMaximum;
            float stateMin = state != null ? state.getMinBrightness() : this.mScreenBrightnessRangeMinimum;
            float minBrightness = java.lang.Math.max(stateMin, java.lang.Math.min(this.mScreenBrightnessRangeMinimum, stateMax));
            float maxBrightness = java.lang.Math.min(this.mScreenBrightnessNormalMaximum, stateMax);
            boolean changed2 = false | this.mCachedBrightnessInfo.checkAndSetFloat(this.mCachedBrightnessInfo.brightness, brightness);
            changed = changed2 | this.mCachedBrightnessInfo.checkAndSetFloat(this.mCachedBrightnessInfo.adjustedBrightness, adjustedBrightness) | this.mCachedBrightnessInfo.checkAndSetFloat(this.mCachedBrightnessInfo.brightnessMin, minBrightness) | this.mCachedBrightnessInfo.checkAndSetFloat(this.mCachedBrightnessInfo.brightnessMax, maxBrightness) | this.mCachedBrightnessInfo.checkAndSetInt(this.mCachedBrightnessInfo.hbmMode, this.mBrightnessRangeController.getHighBrightnessMode()) | this.mCachedBrightnessInfo.checkAndSetFloat(this.mCachedBrightnessInfo.hbmTransitionPoint, this.mBrightnessRangeController.getTransitionPoint()) | this.mCachedBrightnessInfo.checkAndSetInt(this.mCachedBrightnessInfo.brightnessMaxReason, this.mBrightnessClamperController.getBrightnessMaxReason());
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: postBrightnessChangeRunnable, reason: merged with bridge method [inline-methods] */
    public void lambda$new$2() {
        if (!this.mHandler.hasCallbacks(this.mOnBrightnessChangeRunnable)) {
            this.mHandler.post(this.mOnBrightnessChangeRunnable);
        }
    }

    private com.android.server.display.HighBrightnessModeController createHbmControllerLocked(com.android.server.display.HighBrightnessModeMetadata hbmMetadata, java.lang.Runnable modeChangeCallback) {
        com.android.server.display.DisplayDeviceConfig ddConfig = this.mDisplayDevice.getDisplayDeviceConfig();
        android.os.IBinder displayToken = this.mDisplayDevice.getDisplayTokenLocked();
        java.lang.String displayUniqueId = this.mDisplayDevice.getUniqueId();
        com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData = ddConfig != null ? ddConfig.getHighBrightnessModeData() : null;
        com.android.server.display.DisplayDeviceInfo info = this.mDisplayDevice.getDisplayDeviceInfoLocked();
        return this.mInjector.getHighBrightnessModeController(this.mHandler, info.width, info.height, displayToken, displayUniqueId, SCREEN_ANIMATION_RATE_MINIMUM, 1.0f, hbmData, new com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda4
            @Override // com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig
            public final float getHdrBrightnessFromSdr(float f, float f2) {
                return this.f$0.lambda$createHbmControllerLocked$6(f, f2);
            }
        }, modeChangeCallback, hbmMetadata, this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ float lambda$createHbmControllerLocked$6(float sdrBrightness, float maxDesiredHdrSdrRatio) {
        return this.mDisplayDeviceConfig.getHdrBrightnessFromSdr(sdrBrightness, maxDesiredHdrSdrRatio);
    }

    private com.android.server.display.BrightnessThrottler createBrightnessThrottlerLocked() {
        com.android.server.display.DisplayDevice device = this.mLogicalDisplay.getPrimaryDisplayDeviceLocked();
        com.android.server.display.DisplayDeviceConfig ddConfig = device.getDisplayDeviceConfig();
        return new com.android.server.display.BrightnessThrottler(this.mHandler, new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createBrightnessThrottlerLocked$7();
            }
        }, this.mUniqueDisplayId, this.mLogicalDisplay.getDisplayInfoLocked().thermalBrightnessThrottlingDataId, ddConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createBrightnessThrottlerLocked$7() {
        sendUpdatePowerState();
        lambda$new$2();
    }

    private void blockScreenOn() {
        this.mDpcExt.removeMessageWhenScreenOn(this.mHandler, 2);
        if (this.mPendingScreenOnUnblocker == null) {
            android.os.Trace.asyncTraceBegin(131072L, SCREEN_ON_BLOCKED_TRACE_NAME, 0);
            this.mPendingScreenOnUnblocker = new com.android.server.display.DisplayPowerController.ScreenOnUnblocker();
            this.mScreenOnBlockStartRealTime = android.os.SystemClock.elapsedRealtime();
            android.util.Slog.i(this.mTag, "Blocking screen on until initial contents have been drawn.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unblockScreenOn() {
        this.mDpcExt.removeMessageWhenScreenOn(this.mHandler, 2);
        if (this.mPendingScreenOnUnblocker != null) {
            this.mPendingScreenOnUnblocker = null;
            long delay = android.os.SystemClock.elapsedRealtime() - this.mScreenOnBlockStartRealTime;
            android.util.Slog.i(this.mTag, "Unblocked screen on after " + delay + " ms");
            android.os.Trace.asyncTraceEnd(131072L, SCREEN_ON_BLOCKED_TRACE_NAME, 0);
        }
    }

    private void blockScreenOff() {
        if (this.mPendingScreenOffUnblocker == null) {
            android.os.Trace.asyncTraceBegin(131072L, SCREEN_OFF_BLOCKED_TRACE_NAME, 0);
            this.mPendingScreenOffUnblocker = new com.android.server.display.DisplayPowerController.ScreenOffUnblocker();
            this.mScreenOffBlockStartRealTime = android.os.SystemClock.elapsedRealtime();
            android.util.Slog.i(this.mTag, "Blocking screen off");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unblockScreenOff() {
        if (this.mPendingScreenOffUnblocker != null) {
            this.mPendingScreenOffUnblocker = null;
            long delay = android.os.SystemClock.elapsedRealtime() - this.mScreenOffBlockStartRealTime;
            android.util.Slog.i(this.mTag, "Unblocked screen off after " + delay + " ms");
            this.mBrightnessTracker.screenOffAction();
            android.os.Trace.asyncTraceEnd(131072L, SCREEN_OFF_BLOCKED_TRACE_NAME, 0);
        }
    }

    private void blockScreenOnByDisplayOffload(final android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession) {
        if (this.mPendingScreenOnUnblockerByDisplayOffload != null || displayOffloadSession == null) {
            return;
        }
        this.mScreenTurningOnWasBlockedByDisplayOffload = true;
        android.os.Trace.asyncTraceBegin(131072L, SCREEN_ON_BLOCKED_BY_DISPLAYOFFLOAD_TRACE_NAME, 0);
        this.mScreenOnBlockByDisplayOffloadStartRealTime = android.os.SystemClock.elapsedRealtime();
        this.mPendingScreenOnUnblockerByDisplayOffload = new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$blockScreenOnByDisplayOffload$8(displayOffloadSession);
            }
        };
        if (!displayOffloadSession.blockScreenOn(this.mPendingScreenOnUnblockerByDisplayOffload)) {
            this.mPendingScreenOnUnblockerByDisplayOffload = null;
            long delay = android.os.SystemClock.elapsedRealtime() - this.mScreenOnBlockByDisplayOffloadStartRealTime;
            android.util.Slog.w(this.mTag, "Tried blocking screen on for offloading but failed. So, end trace after " + delay + " ms.");
            android.os.Trace.asyncTraceEnd(131072L, SCREEN_ON_BLOCKED_BY_DISPLAYOFFLOAD_TRACE_NAME, 0);
            return;
        }
        android.util.Slog.i(this.mTag, "Blocking screen on for offloading.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onDisplayOffloadUnblockScreenOn, reason: merged with bridge method [inline-methods] */
    public void lambda$blockScreenOnByDisplayOffload$8(android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession) {
        android.os.Message msg = this.mHandler.obtainMessage(18, displayOffloadSession);
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unblockScreenOnByDisplayOffload() {
        if (this.mPendingScreenOnUnblockerByDisplayOffload == null) {
            return;
        }
        this.mPendingScreenOnUnblockerByDisplayOffload = null;
        long delay = android.os.SystemClock.elapsedRealtime() - this.mScreenOnBlockByDisplayOffloadStartRealTime;
        android.util.Slog.i(this.mTag, "Unblocked screen on for offloading after " + delay + " ms");
        android.os.Trace.asyncTraceEnd(131072L, SCREEN_ON_BLOCKED_BY_DISPLAYOFFLOAD_TRACE_NAME, 0);
    }

    private boolean setScreenState(int state, int reason) {
        return setScreenState(state, reason, false);
    }

    private boolean setScreenState(int state, int reason, boolean reportOnly) {
        boolean isOff = state == 1 || state == 3 || state == 4;
        boolean isOn = state == 2;
        boolean changed = this.mPowerState.getScreenState() != state;
        if (isOn && changed && !this.mScreenTurningOnWasBlockedByDisplayOffload) {
            blockScreenOnByDisplayOffload(this.mDisplayOffloadSession);
        } else if (!isOn && this.mScreenTurningOnWasBlockedByDisplayOffload) {
            unblockScreenOnByDisplayOffload();
            this.mScreenTurningOnWasBlockedByDisplayOffload = false;
        }
        if (changed || this.mReportedScreenStateToPolicy == -1) {
            if (isOff && !this.mDisplayPowerProximityStateController.isScreenOffBecauseOfProximity()) {
                if (this.mReportedScreenStateToPolicy == 2 || this.mReportedScreenStateToPolicy == -1) {
                    setReportedScreenState(3);
                    blockScreenOff();
                    this.mWindowManagerPolicy.screenTurningOff(this.mDisplayId, this.mPendingScreenOffUnblocker);
                    unblockScreenOff();
                } else if (this.mPendingScreenOffUnblocker != null) {
                    return false;
                }
            }
            this.mDpcExt.setScreenStateExt(this.mIsPrimaryDisplay, state, this.mPowerState, this.mPowerRequest);
            if (!reportOnly && changed && readyToUpdateDisplayState() && this.mPendingScreenOffUnblocker == null && this.mPendingScreenOnUnblockerByDisplayOffload == null) {
                android.os.Trace.traceCounter(131072L, "ScreenState", state);
                java.lang.String propertyValue = java.lang.String.valueOf(state);
                try {
                    android.os.SystemProperties.set("debug.tracing.screen_state", propertyValue);
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.e(this.mTag, "Failed to set a system property: key=debug.tracing.screen_state value=" + propertyValue + " " + e.getMessage());
                }
                this.mDpcExt.handlePwkMonitorForTheia(state, isOff);
                this.mPowerState.setScreenState(state, reason);
                noteScreenState(state, reason);
            }
        }
        if (DEBUG_PANIC) {
            android.util.Slog.d(this.mTag, "setScreenState: isOff=" + isOff + ", mReportedScreenStateToPolicy=" + this.mReportedScreenStateToPolicy);
        }
        if (isOff && this.mReportedScreenStateToPolicy != 0 && !this.mDisplayPowerProximityStateController.isScreenOffBecauseOfProximity()) {
            setReportedScreenState(0);
            unblockScreenOn();
            this.mDpcExt.unblockDisplayReady();
            this.mWindowManagerPolicy.screenTurnedOff(this.mDisplayId, this.mIsInTransition);
        } else if (!isOff && this.mReportedScreenStateToPolicy == 3) {
            unblockScreenOff();
            this.mWindowManagerPolicy.screenTurnedOff(this.mDisplayId, this.mIsInTransition);
            setReportedScreenState(0);
        }
        if (!isOff && (this.mReportedScreenStateToPolicy == 0 || this.mReportedScreenStateToPolicy == -1)) {
            setReportedScreenState(1);
            if (DEBUG) {
                android.util.Slog.d(this.mTag, "setScreenState: ColorFadeLevel=" + this.mPowerState.getColorFadeLevel());
            }
            if (this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
                blockScreenOn();
            } else {
                unblockScreenOn();
            }
            this.mWindowManagerPolicy.screenTurningOn(this.mDisplayId, this.mPendingScreenOnUnblocker);
        }
        if (this.isRM) {
            resetCabc(state);
        }
        if (this.mPendingScreenOnUnblocker == null && this.mPendingScreenOnUnblockerByDisplayOffload == null) {
            return !this.mDpcExt.isBlockScreenOnByBiometrics() || "optical".equals(FP_SENSOR_TYPE) || "ultrasonic".equals(FP_SENSOR_TYPE);
        }
        return false;
    }

    private void resetCabc(int state) {
        if (this.mLastState != state && state == 2 && this.mReportedScreenStateToPolicy == 2 && getScreenBrightnessSetting() > SCREEN_ANIMATION_RATE_MINIMUM) {
            this.mLastState = state;
            android.os.Message msg = this.mHandler.obtainMessage(22);
            this.mHandler.sendMessageDelayed(msg, 100L);
        }
        if (state != 2) {
            this.mLastState = state;
        }
    }

    private void setReportedScreenState(int state) {
        android.os.Trace.traceCounter(131072L, "ReportedScreenStateToPolicy", state);
        this.mReportedScreenStateToPolicy = state;
        if (state == 2) {
            this.mScreenTurningOnWasBlockedByDisplayOffload = false;
        }
    }

    private void loadAmbientLightSensor() {
        int fallbackType = this.mDisplayId == 0 ? 5 : 0;
        this.mLightSensor = com.android.server.display.utils.SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getAmbientLightSensor(), fallbackType);
    }

    private void loadScreenOffBrightnessSensor() {
        this.mScreenOffBrightnessSensor = com.android.server.display.utils.SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getScreenOffBrightnessSensor(), 0);
    }

    private float clampScreenBrightness(float value) {
        if (this.mDpcExt.hasRemapDisable()) {
            return android.util.MathUtils.constrain(value, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum);
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateScreenBrightness(float target, float sdrTarget, float rate) {
        animateScreenBrightness(target, sdrTarget, rate, false);
    }

    private void animateScreenBrightness(float target, float sdrTarget, float rate, boolean ignoreAnimationLimits) {
        this.mDpcExt.animateScreenBrightness(this.mScreenBrightnessRampAnimator, target, sdrTarget, rate, this.mPowerRequest, this.mPowerState);
        if (this.mScreenBrightnessRampAnimator.animateTo(target, sdrTarget, rate, ignoreAnimationLimits)) {
            android.os.Trace.traceCounter(131072L, "TargetScreenBrightness", (int) target);
            java.lang.String propertyValue = java.lang.String.valueOf(target);
            try {
                android.os.SystemProperties.set("debug.tracing.screen_brightness", propertyValue);
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.e(this.mTag, "Failed to set a system property: key=debug.tracing.screen_brightness value=" + propertyValue + " " + e.getMessage());
            }
            noteScreenBrightness(target);
        }
    }

    private void animateScreenStateChange(int target, int reason, boolean performScreenOffTransition) {
        if (DisplayDisable && target == 2 && this.mDpcExt.isBlockedBySideFingerprint()) {
            android.util.Slog.d(this.mTag, "animateScreenStateChange state:" + android.view.Display.stateToString(target));
            return;
        }
        if (this.mColorFadeEnabled && (this.mColorFadeOnAnimator.isStarted() || this.mColorFadeOffAnimator.isStarted())) {
            if (target != 2) {
                android.util.Slog.d(this.mTag, "animateScreenStateChange animation in progress state:" + android.view.Display.stateToString(target));
                return;
            } else {
                this.mPendingScreenOff = false;
                if (this.mColorFadeOffAnimator.isStarted()) {
                    this.mColorFadeOffAnimator.cancel();
                }
            }
        }
        if (this.mDisplayBlanksAfterDozeConfig && android.view.Display.isDozeState(this.mPowerState.getScreenState()) && !android.view.Display.isDozeState(target) && target != 2) {
            this.mPowerState.prepareColorFade(this.mContext, this.mColorFadeFadesConfig ? 2 : 0);
            if (this.mColorFadeOffAnimator != null) {
                this.mColorFadeOffAnimator.end();
            }
            setScreenState(1, reason, target != 1);
        }
        if (this.mPendingScreenOff && target != 1) {
            setScreenState(1, reason);
            this.mPendingScreenOff = false;
            this.mPowerState.dismissColorFadeResources();
        }
        if (target == 2) {
            if (android.view.Display.isDozeState(this.mPowerState.getScreenState()) && this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                android.util.Slog.d(this.mTag, "animateScreenStateChange target == Display.STATE_ON, current is doze");
            }
            if (!setScreenState(2, reason)) {
                android.util.Slog.d(this.mTag, "animateScreenStateChange screen on blocked blocker=" + this.mPendingScreenOnUnblocker);
                return;
            } else {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
        }
        if (target == 3) {
            if (this.mScreenBrightnessRampAnimator.isAnimating() && this.mPowerState.getScreenState() == 2) {
                android.util.Slog.d(this.mTag, "animateScreenStateChange DOZE isAnimating");
                return;
            } else if (!setScreenState(3, reason)) {
                android.util.Slog.d(this.mTag, "animateScreenStateChange DOZE setScreenState");
                return;
            } else {
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
        }
        if (target == 4) {
            if (!this.mScreenBrightnessRampAnimator.isAnimating() || this.mPowerState.getScreenState() == 4) {
                if (this.mPowerState.getScreenState() != 4) {
                    setScreenState(4, reason);
                }
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
            android.util.Slog.d(this.mTag, "animateScreenStateChange DOZE_SUSPEND isAnimating");
            return;
        }
        if (target == 6) {
            if (!this.mScreenBrightnessRampAnimator.isAnimating() || this.mPowerState.getScreenState() == 6) {
                if (this.mPowerState.getScreenState() != 6) {
                    if (!setScreenState(2, reason)) {
                        return;
                    } else {
                        setScreenState(6, reason);
                    }
                }
                this.mPowerState.setColorFadeLevel(1.0f);
                this.mPowerState.dismissColorFade();
                return;
            }
            return;
        }
        this.mPendingScreenOff = true;
        boolean isFolding = this.mDpcExt.isFolding();
        if (DEBUG) {
            android.util.Slog.d(this.mTag, "isFolding = " + isFolding);
        }
        if (!this.mColorFadeEnabled || isFolding) {
            this.mPowerState.setColorFadeLevel(SCREEN_ANIMATION_RATE_MINIMUM);
        }
        if (this.mDpcExt.isSilentRebootFirstGoToSleep(this.mDisplayId)) {
            this.mPowerState.setColorFadeLevel(SCREEN_ANIMATION_RATE_MINIMUM);
        }
        if (this.mPowerState.getColorFadeLevel() == SCREEN_ANIMATION_RATE_MINIMUM) {
            setScreenState(1, reason);
            this.mPendingScreenOff = false;
            this.mPowerState.dismissColorFadeResources();
        } else {
            if (performScreenOffTransition) {
                if (this.mPowerState.prepareColorFade(this.mContext, this.mColorFadeFadesConfig ? 2 : 1) && this.mPowerState.getScreenState() != 1) {
                    this.mColorFadeOffAnimator.start();
                    return;
                }
            }
            this.mColorFadeOffAnimator.end();
        }
    }

    private void sendOnStateChangedWithWakelock() {
        boolean wakeLockAcquired = this.mWakelockController.acquireWakelock(4);
        if (wakeLockAcquired) {
            this.mHandler.post(this.mWakelockController.getOnStateChangedRunnable());
        }
    }

    private void logDisplayPolicyChanged(int newPolicy) {
        android.metrics.LogMaker log = new android.metrics.LogMaker(1696);
        log.setType(6);
        log.setSubtype(newPolicy);
        com.android.internal.logging.MetricsLogger.action(log);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChange() {
        this.mDisplayBrightnessController.setPendingScreenBrightness(getScreenBrightnessSetting());
        this.mDpcExt.setGlobalHbmSellMode();
        this.mAutomaticBrightnessStrategy.updatePendingAutoBrightnessAdjustments();
        sendUpdatePowerState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBrightnessModeChange() {
        int screenBrightnessModeSetting = android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", 0, -2);
        this.mAutomaticBrightnessStrategy.setUseAutoBrightness(screenBrightnessModeSetting == 1);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public float getScreenBrightnessSetting() {
        float brightness;
        this.mDisplayBrightnessController.getScreenBrightnessSetting();
        java.lang.String v = android.provider.Settings.System.getStringForUser(this.mContext.getContentResolver(), "screen_brightness", -2);
        try {
            brightness = v != null ? java.lang.Integer.parseInt(v) : this.mScreenBrightnessDefault;
        } catch (java.lang.NumberFormatException e) {
            brightness = this.mScreenBrightnessDefault;
        }
        if (!this.mIsUserSwitching) {
            return this.mDpcExt.handleScreenBrightnessSettingChange(brightness);
        }
        return brightness;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public float getDozeBrightnessForOffload() {
        return this.mDisplayBrightnessController.getCurrentBrightness() * this.mDozeScaleFactor;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightness(float brightness) {
        this.mDisplayBrightnessController.setBrightness(clampScreenBrightness(brightness), this.mBrightnessRangeController.getCurrentBrightnessMax());
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightness(float brightness, int userSerial) {
        this.mDisplayBrightnessController.setBrightness(clampScreenBrightness(brightness), userSerial, this.mBrightnessRangeController.getCurrentBrightnessMax());
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public int getDisplayId() {
        return this.mDisplayId;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public int getLeadDisplayId() {
        return this.mLeadDisplayId;
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setBrightnessToFollow(float leadDisplayBrightness, float nits, float ambientLux, boolean slowChange) {
        this.mBrightnessRangeController.onAmbientLuxChange(ambientLux);
        if (nits == -1.0f) {
            this.mDisplayBrightnessController.setBrightnessToFollow(leadDisplayBrightness, slowChange);
        } else {
            float brightness = this.mDisplayBrightnessController.getBrightnessFromNits(nits);
            if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightness, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
                this.mDisplayBrightnessController.setBrightnessToFollow(brightness, slowChange);
            } else {
                this.mDisplayBrightnessController.setBrightnessToFollow(leadDisplayBrightness, slowChange);
            }
        }
        sendUpdatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void addDisplayBrightnessFollower(com.android.server.display.DisplayPowerControllerInterface follower) {
        synchronized (this.mLock) {
            this.mDisplayBrightnessFollowers.append(follower.getDisplayId(), follower);
            sendUpdatePowerStateLocked();
        }
    }

    private float getBrightnessByNit(float nit) {
        return this.mDpcExt.getBrightnessByNit(nit);
    }

    private float getNitByBrightness(float brightness) {
        return this.mDpcExt.getNitByBrightness(brightness);
    }

    private float convertToAdjustedNits(float brightness) {
        return this.mDpcExt.getNitByBrightness(brightness);
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void removeDisplayBrightnessFollower(final com.android.server.display.DisplayPowerControllerInterface follower) {
        synchronized (this.mLock) {
            this.mDisplayBrightnessFollowers.remove(follower.getDisplayId());
            this.mHandler.postAtTime(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    follower.setBrightnessToFollow(Float.NaN, -1.0f, com.android.server.display.DisplayPowerController.SCREEN_ANIMATION_RATE_MINIMUM, false);
                }
            }, this.mClock.uptimeMillis());
        }
    }

    private void clearDisplayBrightnessFollowersLocked() {
        for (int i = 0; i < this.mDisplayBrightnessFollowers.size(); i++) {
            final com.android.server.display.DisplayPowerControllerInterface follower = this.mDisplayBrightnessFollowers.valueAt(i);
            this.mHandler.postAtTime(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    follower.setBrightnessToFollow(Float.NaN, -1.0f, com.android.server.display.DisplayPowerController.SCREEN_ANIMATION_RATE_MINIMUM, false);
                }
            }, this.mClock.uptimeMillis());
        }
        this.mDisplayBrightnessFollowers.clear();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void dump(final java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println();
            pw.println("Display Power Controller:");
            pw.println("  mDisplayId=" + this.mDisplayId);
            pw.println("  mLeadDisplayId=" + this.mLeadDisplayId);
            pw.println("  mLightSensor=" + this.mLightSensor);
            pw.println("  mDisplayBrightnessFollowers=" + this.mDisplayBrightnessFollowers);
            pw.println();
            pw.println("Display Power Controller Locked State:");
            pw.println("  mDisplayReadyLocked=" + this.mDisplayReadyLocked);
            pw.println("  mPendingRequestLocked=" + this.mPendingRequestLocked);
            pw.println("  mPendingRequestChangedLocked=" + this.mPendingRequestChangedLocked);
            pw.println("  mPendingUpdatePowerStateLocked=" + this.mPendingUpdatePowerStateLocked);
        }
        pw.println();
        pw.println("Display Power Controller Configuration:");
        pw.println("  mScreenBrightnessDozeConfig=" + this.mScreenBrightnessDozeConfig);
        pw.println("  mScreenBrightnessRangeMinimum=" + this.mScreenBrightnessRangeMinimum);
        pw.println("  mScreenBrightnessRangeMaximum=" + this.mScreenBrightnessRangeMaximum);
        pw.println("  mScreenBrightnessNormalMaximum=" + this.mScreenBrightnessNormalMaximum);
        pw.println("  mUseSoftwareAutoBrightnessConfig=" + this.mUseSoftwareAutoBrightnessConfig);
        pw.println("  mSkipScreenOnBrightnessRamp=" + this.mSkipScreenOnBrightnessRamp);
        pw.println("  mColorFadeFadesConfig=" + this.mColorFadeFadesConfig);
        pw.println("  mColorFadeEnabled=" + this.mColorFadeEnabled);
        pw.println("  mIsDisplayInternal=" + this.mIsDisplayInternal);
        synchronized (this.mCachedBrightnessInfo) {
            pw.println("  mCachedBrightnessInfo.brightness=" + this.mCachedBrightnessInfo.brightness.value);
            pw.println("  mCachedBrightnessInfo.adjustedBrightness=" + this.mCachedBrightnessInfo.adjustedBrightness.value);
            pw.println("  mCachedBrightnessInfo.brightnessMin=" + this.mCachedBrightnessInfo.brightnessMin.value);
            pw.println("  mCachedBrightnessInfo.brightnessMax=" + this.mCachedBrightnessInfo.brightnessMax.value);
            pw.println("  mCachedBrightnessInfo.hbmMode=" + this.mCachedBrightnessInfo.hbmMode.value);
            pw.println("  mCachedBrightnessInfo.hbmTransitionPoint=" + this.mCachedBrightnessInfo.hbmTransitionPoint.value);
            pw.println("  mCachedBrightnessInfo.brightnessMaxReason =" + this.mCachedBrightnessInfo.brightnessMaxReason.value);
        }
        pw.println("  mDisplayBlanksAfterDozeConfig=" + this.mDisplayBlanksAfterDozeConfig);
        pw.println("  mBrightnessBucketsInDozeConfig=" + this.mBrightnessBucketsInDozeConfig);
        pw.println("  mDozeScaleFactor=" + this.mDozeScaleFactor);
        this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$11(pw);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$11(java.io.PrintWriter pw) {
        pw.println();
        pw.println("Display Power Controller Thread State:");
        pw.println("  mPowerRequest=" + this.mPowerRequest);
        pw.println("  mBrightnessReason=" + this.mBrightnessReason);
        pw.println("  mAppliedDimming=" + this.mAppliedDimming);
        pw.println("  mAppliedThrottling=" + this.mAppliedThrottling);
        pw.println("  mDozing=" + this.mDozing);
        pw.println("  mSkipRampState=" + skipRampStateToString(this.mSkipRampState));
        pw.println("  mScreenOnBlockStartRealTime=" + this.mScreenOnBlockStartRealTime);
        pw.println("  mScreenOffBlockStartRealTime=" + this.mScreenOffBlockStartRealTime);
        pw.println("  mPendingScreenOnUnblocker=" + this.mPendingScreenOnUnblocker);
        pw.println("  mPendingScreenOffUnblocker=" + this.mPendingScreenOffUnblocker);
        pw.println("  mPendingScreenOff=" + this.mPendingScreenOff);
        pw.println("  mReportedToPolicy=" + reportedToPolicyToString(this.mReportedScreenStateToPolicy));
        pw.println("  mIsRbcActive=" + this.mIsRbcActive);
        java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(pw, "    ");
        this.mAutomaticBrightnessStrategy.dump(indentingPrintWriter);
        if (this.mScreenBrightnessRampAnimator != null) {
            pw.println("  mScreenBrightnessRampAnimator.isAnimating()=" + this.mScreenBrightnessRampAnimator.isAnimating());
        }
        if (this.mColorFadeOnAnimator != null) {
            pw.println("  mColorFadeOnAnimator.isStarted()=" + this.mColorFadeOnAnimator.isStarted());
        }
        if (this.mColorFadeOffAnimator != null) {
            pw.println("  mColorFadeOffAnimator.isStarted()=" + this.mColorFadeOffAnimator.isStarted());
        }
        if (this.mPowerState != null) {
            this.mPowerState.dump(pw);
        }
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.dump(pw);
            dumpBrightnessEvents(pw);
        }
        dumpRbcEvents(pw);
        if (this.mScreenOffBrightnessSensorController != null) {
            this.mScreenOffBrightnessSensorController.dump(pw);
        }
        if (this.mBrightnessRangeController != null) {
            this.mBrightnessRangeController.dump(pw);
        }
        if (this.mBrightnessThrottler != null) {
            this.mBrightnessThrottler.dump(pw);
        }
        pw.println();
        if (this.mDisplayWhiteBalanceController != null) {
            this.mDisplayWhiteBalanceController.dump(pw);
            this.mDisplayWhiteBalanceSettings.dump(pw);
        }
        this.mDpcExt.dump(pw);
        pw.println();
        if (this.mWakelockController != null) {
            this.mWakelockController.dumpLocal(pw);
        }
        pw.println();
        if (this.mDisplayBrightnessController != null) {
            this.mDisplayBrightnessController.dump(pw);
        }
        pw.println();
        if (this.mDisplayStateController != null) {
            this.mDisplayStateController.dumpsys(pw);
        }
        pw.println();
        if (this.mBrightnessClamperController != null) {
            this.mBrightnessClamperController.dump(indentingPrintWriter);
        }
    }

    private static java.lang.String reportedToPolicyToString(int state) {
        switch (state) {
            case 0:
                return "REPORTED_TO_POLICY_SCREEN_OFF";
            case 1:
                return "REPORTED_TO_POLICY_SCREEN_TURNING_ON";
            case 2:
                return "REPORTED_TO_POLICY_SCREEN_ON";
            default:
                return java.lang.Integer.toString(state);
        }
    }

    private static java.lang.String skipRampStateToString(int state) {
        switch (state) {
            case 0:
                return "RAMP_STATE_SKIP_NONE";
            case 1:
                return "RAMP_STATE_SKIP_INITIAL";
            case 2:
                return "RAMP_STATE_SKIP_AUTOBRIGHT";
            default:
                return java.lang.Integer.toString(state);
        }
    }

    private void dumpBrightnessEvents(java.io.PrintWriter pw) {
        if (this.mBrightnessEventRingBuffer == null) {
            return;
        }
        int size = this.mBrightnessEventRingBuffer.size();
        if (size < 1) {
            pw.println("No Automatic Brightness Adjustments");
            return;
        }
        pw.println("Automatic Brightness Adjustments Last " + size + " Events: ");
        com.android.server.display.brightness.BrightnessEvent[] eventArray = (com.android.server.display.brightness.BrightnessEvent[]) this.mBrightnessEventRingBuffer.toArray();
        for (int i = 0; i < this.mBrightnessEventRingBuffer.size(); i++) {
            pw.println("  " + eventArray[i].toString());
        }
    }

    private void dumpRbcEvents(java.io.PrintWriter pw) {
        int size = this.mRbcEventRingBuffer.size();
        if (size < 1) {
            pw.println("No Reduce Bright Colors Adjustments");
            return;
        }
        pw.println("Reduce Bright Colors Adjustments Last " + size + " Events: ");
        com.android.server.display.brightness.BrightnessEvent[] eventArray = (com.android.server.display.brightness.BrightnessEvent[]) this.mRbcEventRingBuffer.toArray();
        for (int i = 0; i < this.mRbcEventRingBuffer.size(); i++) {
            pw.println("  " + eventArray[i]);
        }
    }

    private void noteScreenState(int screenState, int reason) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SCREEN_STATE_CHANGED_V2, screenState, this.mDisplayStatsId, reason);
        if (this.mBatteryStats != null) {
            try {
                this.mBatteryStats.noteScreenState(screenState);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void noteScreenBrightness(float brightness) {
        if (this.mBatteryStats != null) {
            try {
                this.mBatteryStats.noteScreenBrightness((int) brightness);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportStats(float brightness) {
        if (this.mLastStatsBrightness == brightness) {
            return;
        }
        synchronized (this.mCachedBrightnessInfo) {
            if (this.mCachedBrightnessInfo.hbmTransitionPoint == null) {
                return;
            }
            float hbmTransitionPoint = this.mCachedBrightnessInfo.hbmTransitionPoint.value;
            boolean aboveTransition = brightness > hbmTransitionPoint;
            boolean oldAboveTransition = this.mLastStatsBrightness > hbmTransitionPoint;
            if (aboveTransition || oldAboveTransition) {
                this.mLastStatsBrightness = brightness;
                this.mHandler.removeMessages(11);
                if (aboveTransition != oldAboveTransition) {
                    logHbmBrightnessStats(brightness, this.mDisplayStatsId);
                    return;
                }
                android.os.Message msg = this.mHandler.obtainMessage();
                msg.what = 11;
                msg.arg1 = java.lang.Float.floatToIntBits(brightness);
                msg.arg2 = this.mDisplayStatsId;
                this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis() + 500);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logHbmBrightnessStats(float brightness, int displayStatsId) {
        synchronized (this.mHandler) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_HBM_BRIGHTNESS_CHANGED, displayStatsId, brightness);
        }
    }

    private int nitsToRangeIndex(float nits) {
        for (int i = 0; i < BRIGHTNESS_RANGE_BOUNDARIES.length; i++) {
            if (nits < BRIGHTNESS_RANGE_BOUNDARIES[i]) {
                return BRIGHTNESS_RANGE_INDEX[i];
            }
        }
        return 38;
    }

    private int convertBrightnessReasonToStatsEnum(int brightnessReason) {
        switch (brightnessReason) {
        }
        return 0;
    }

    private void logBrightnessEvent(com.android.server.display.brightness.BrightnessEvent event, float unmodifiedBrightness) {
        float appliedHbmMaxNits;
        float appliedThermalCapNits;
        int modifier = event.getReason().getModifier();
        int flags = event.getFlags();
        boolean brightnessIsMax = unmodifiedBrightness == event.getHbmMax();
        float brightnessInNits = this.mDisplayBrightnessController.convertToAdjustedNits(event.getBrightness());
        float appliedLowPowerMode = event.isLowPowerModeSet() ? event.getPowerFactor() : -1.0f;
        int appliedRbcStrength = event.isRbcEnabled() ? event.getRbcStrength() : -1;
        if (event.getHbmMode() != 0) {
            appliedHbmMaxNits = this.mDisplayBrightnessController.convertToAdjustedNits(event.getHbmMax());
        } else {
            appliedHbmMaxNits = -1.0f;
        }
        if (event.getThermalMax() != 1.0f) {
            appliedThermalCapNits = this.mDisplayBrightnessController.convertToAdjustedNits(event.getThermalMax());
        } else {
            appliedThermalCapNits = -1.0f;
        }
        if (this.mIsDisplayInternal) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_BRIGHTNESS_CHANGED, this.mDisplayBrightnessController.convertToAdjustedNits(event.getInitialBrightness()), brightnessInNits, event.getLux(), event.getPhysicalDisplayId(), event.wasShortTermModelActive(), appliedLowPowerMode, appliedRbcStrength, appliedHbmMaxNits, appliedThermalCapNits, event.isAutomaticBrightnessEnabled(), 1, convertBrightnessReasonToStatsEnum(event.getReason().getReason()), nitsToRangeIndex(brightnessInNits), brightnessIsMax, event.getHbmMode() == 1, event.getHbmMode() == 2, (modifier & 2) > 0, this.mBrightnessClamperController.getBrightnessMaxReason(), (modifier & 1) > 0, event.isRbcEnabled(), (flags & 2) > 0, (flags & 4) > 0, (flags & 8) > 0, event.getAutoBrightnessMode() == 1, (flags & 32) > 0);
        }
    }

    private boolean readyToUpdateDisplayState() {
        return this.mDisplayId == 0 || this.mBootCompleted;
    }

    private final class DisplayControllerHandler extends android.os.Handler {
        DisplayControllerHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            com.android.server.display.DisplayPowerController.this.mDpcExt.onDisplayControllerHandler(msg, com.android.server.display.DisplayPowerController.this.mHandler);
            switch (msg.what) {
                case 1:
                    com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    break;
                case 2:
                    if (com.android.server.display.DisplayPowerController.this.mPendingScreenOnUnblocker == msg.obj) {
                        if ((com.oplus.util.OplusPlatformLevelUtils.IS_LIGHT_OS || com.android.server.display.DisplayPowerController.IS_LIGHT_OS_BY_AMS) && com.android.server.display.DisplayPowerController.this.mPendingScreenOnUnblocker != null) {
                            com.android.server.display.DisplayPowerController.this.mDpcExt.setUxWhenWindowUnblock(true);
                        }
                        com.android.server.display.DisplayPowerController.this.unblockScreenOn();
                        com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    }
                    break;
                case 3:
                    if (com.android.server.display.DisplayPowerController.this.mPendingScreenOffUnblocker == msg.obj) {
                        com.android.server.display.DisplayPowerController.this.unblockScreenOff();
                        com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    }
                    break;
                case 4:
                    android.hardware.display.BrightnessConfiguration brightnessConfiguration = (android.hardware.display.BrightnessConfiguration) msg.obj;
                    com.android.server.display.DisplayPowerController.this.mAutomaticBrightnessStrategy.setBrightnessConfiguration(brightnessConfiguration, msg.arg1 == 1);
                    if (com.android.server.display.DisplayPowerController.this.mBrightnessTracker != null) {
                        com.android.server.display.BrightnessTracker brightnessTracker = com.android.server.display.DisplayPowerController.this.mBrightnessTracker;
                        if (brightnessConfiguration != null && brightnessConfiguration.shouldCollectColorSamples()) {
                            z = true;
                        }
                        brightnessTracker.setShouldCollectColorSample(z);
                    }
                    com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    break;
                case 5:
                    float temporaryBrightness = com.android.server.display.DisplayPowerController.this.mDpcExt.handleSetTemporaryBrightnessMessage(java.lang.Float.intBitsToFloat(msg.arg1), "MSG_SET_TEMPORARY_BRIGHTNESS", com.android.server.display.DisplayPowerController.this.mDisplayId);
                    com.android.server.display.DisplayPowerController.this.mDisplayBrightnessController.setTemporaryBrightness(java.lang.Float.valueOf(temporaryBrightness));
                    com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    break;
                case 6:
                    com.android.server.display.DisplayPowerController.this.mAutomaticBrightnessStrategy.setTemporaryAutoBrightnessAdjustment(java.lang.Float.intBitsToFloat(msg.arg1));
                    com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    break;
                case 7:
                    com.android.server.display.DisplayPowerController.this.cleanupHandlerThreadAfterStop();
                    break;
                case 8:
                    if (!com.android.server.display.DisplayPowerController.this.mStopped) {
                        com.android.server.display.DisplayPowerController.this.handleSettingsChange();
                        break;
                    }
                    break;
                case 9:
                    com.android.server.display.DisplayPowerController.this.handleRbcChanged();
                    break;
                case 10:
                    if (com.android.server.display.DisplayPowerController.this.mPowerState != null) {
                        float brightness = com.android.server.display.DisplayPowerController.this.mPowerState.getScreenBrightness();
                        com.android.server.display.DisplayPowerController.this.reportStats(brightness);
                    }
                    break;
                case 11:
                    com.android.server.display.DisplayPowerController.this.logHbmBrightnessStats(java.lang.Float.intBitsToFloat(msg.arg1), msg.arg2);
                    break;
                case 12:
                    float newBrightness = msg.obj instanceof java.lang.Float ? ((java.lang.Float) msg.obj).floatValue() : Float.NaN;
                    com.android.server.display.DisplayPowerController.this.handleOnSwitchUser(msg.arg1, msg.arg2, newBrightness);
                    break;
                case 13:
                    com.android.server.display.DisplayPowerController.this.mBootCompleted = true;
                    com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    break;
                case 14:
                    z = msg.arg1 == 1;
                    com.android.server.display.DisplayPowerController.this.setDwbcStrongMode(msg.arg1);
                    break;
                case 15:
                    float cct = java.lang.Float.intBitsToFloat(msg.arg1);
                    com.android.server.display.DisplayPowerController.this.setDwbcOverride(cct);
                    break;
                case 16:
                    com.android.server.display.DisplayPowerController.this.setDwbcLoggingEnabled(msg.arg1);
                    break;
                case 17:
                    if (com.android.server.display.DisplayPowerController.this.mDisplayBrightnessController.setBrightnessFromOffload(java.lang.Float.intBitsToFloat(msg.arg1))) {
                        com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    }
                    break;
                case 18:
                    if (com.android.server.display.DisplayPowerController.this.mDisplayOffloadSession == msg.obj) {
                        com.android.server.display.DisplayPowerController.this.unblockScreenOnByDisplayOffload();
                        com.android.server.display.DisplayPowerController.this.lambda$new$0();
                    }
                    break;
                case 21:
                    if (com.android.server.display.DisplayPowerController.this.mResetFpsStatePending) {
                        com.android.server.display.DisplayPowerController.this.updateFpsWhenDcChange(false);
                    }
                    break;
                case 22:
                    com.android.server.display.DisplayPowerController.this.mDpcExt.setRmMode();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            int state = com.android.server.display.DisplayPowerController.this.mPowerState != null ? com.android.server.display.DisplayPowerController.this.mPowerState.getScreenState() : 2;
            com.android.server.display.DisplayPowerController.this.mDpcExt.onChange(com.android.server.display.DisplayPowerController.this.mContext, com.android.server.display.DisplayPowerController.this.mDisplayId, selfChange, uri, state);
            if (uri.equals(android.provider.Settings.System.getUriFor("screen_brightness_mode"))) {
                com.android.server.display.DisplayPowerController.this.mHandler.postAtTime(new java.lang.Runnable() { // from class: com.android.server.display.DisplayPowerController$SettingsObserver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() throws java.lang.Throwable {
                        this.f$0.lambda$onChange$0();
                    }
                }, com.android.server.display.DisplayPowerController.this.mClock.uptimeMillis());
                return;
            }
            if (uri.equals(android.provider.Settings.System.getUriFor("screen_brightness_for_als"))) {
                int preset = android.provider.Settings.System.getIntForUser(com.android.server.display.DisplayPowerController.this.mContext.getContentResolver(), "screen_brightness_for_als", 2, -2);
                android.util.Slog.i(com.android.server.display.DisplayPowerController.this.mTag, "Setting up auto-brightness for preset " + com.android.server.display.config.DisplayBrightnessMappingConfig.autoBrightnessPresetToString(preset));
                com.android.server.display.DisplayPowerController.this.setUpAutoBrightness(com.android.server.display.DisplayPowerController.this.mContext, com.android.server.display.DisplayPowerController.this.mHandler);
                com.android.server.display.DisplayPowerController.this.sendUpdatePowerState();
                return;
            }
            com.android.server.display.DisplayPowerController.this.handleSettingsChange();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$0() throws java.lang.Throwable {
            com.android.server.display.DisplayPowerController.this.handleBrightnessModeChange();
            com.android.server.display.DisplayPowerController.this.lambda$new$0();
        }
    }

    private final class ScreenOnUnblocker implements com.android.server.policy.WindowManagerPolicy.ScreenOnListener {
        private ScreenOnUnblocker() {
        }

        @Override // com.android.server.policy.WindowManagerPolicy.ScreenOnListener
        public void onScreenOn() {
            android.os.Message msg = com.android.server.display.DisplayPowerController.this.mHandler.obtainMessage(2, this);
            if (com.android.server.display.DisplayPowerController.this.mDpcExt.sendMessageWhenScreenOnUnblocker(com.android.server.display.DisplayPowerController.this.mHandler, msg)) {
                return;
            }
            com.android.server.display.DisplayPowerController.this.mHandler.sendMessageAtTime(msg, com.android.server.display.DisplayPowerController.this.mClock.uptimeMillis());
        }
    }

    private final class ScreenOffUnblocker implements com.android.server.policy.WindowManagerPolicy.ScreenOffListener {
        private ScreenOffUnblocker() {
        }

        @Override // com.android.server.policy.WindowManagerPolicy.ScreenOffListener
        public void onScreenOff() {
            android.os.Message msg = com.android.server.display.DisplayPowerController.this.mHandler.obtainMessage(3, this);
            com.android.server.display.DisplayPowerController.this.mHandler.sendMessageAtTime(msg, com.android.server.display.DisplayPowerController.this.mClock.uptimeMillis());
        }
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAutoBrightnessLoggingEnabled(boolean enabled) {
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.setLoggingEnabled(enabled);
        }
        if (this.mDpcExt != null) {
            this.mDpcExt.setLoggingEnabled(enabled);
        }
        if (this.mScreenBrightnessRampAnimator != null) {
            this.mScreenBrightnessRampAnimator.setLoggingEnabled(enabled);
        }
        DEBUG_PANIC = enabled;
        android.util.Slog.d(this.mTag, "setLoggingEnabled loggingEnabled=" + enabled);
    }

    @Override // com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks
    public void updateWhiteBalance() {
        sendUpdatePowerState();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setDisplayWhiteBalanceLoggingEnabled(boolean z) {
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage();
        messageObtainMessage.what = 16;
        messageObtainMessage.arg1 = z ? 1 : 0;
        messageObtainMessage.sendToTarget();
    }

    @Override // com.android.server.display.DisplayPowerControllerInterface
    public void setAmbientColorTemperatureOverride(float cct) {
        android.os.Message msg = this.mHandler.obtainMessage();
        msg.what = 15;
        msg.arg1 = java.lang.Float.floatToIntBits(cct);
        msg.sendToTarget();
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.DisplayPowerController.Clock getClock() {
            return new com.android.server.display.DisplayPowerController.Clock() { // from class: com.android.server.display.DisplayPowerController$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayPowerController.Clock
                public final long uptimeMillis() {
                    return android.os.SystemClock.uptimeMillis();
                }
            };
        }

        com.android.server.display.DisplayPowerState getDisplayPowerState(com.android.server.display.DisplayBlanker blanker, com.android.server.display.ColorFade colorFade, int displayId, int displayState, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.DisplayPowerState(blanker, colorFade, displayId, displayState, dpcExt);
        }

        com.android.server.display.RampAnimator.DualRampAnimator<com.android.server.display.DisplayPowerState> getDualRampAnimator(com.android.server.display.DisplayPowerState dps, android.util.FloatProperty<com.android.server.display.DisplayPowerState> firstProperty, android.util.FloatProperty<com.android.server.display.DisplayPowerState> secondProperty) {
            return new com.android.server.display.RampAnimator.DualRampAnimator<>(dps, firstProperty, secondProperty);
        }

        com.android.server.display.WakelockController getWakelockController(int displayId, android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks) {
            return new com.android.server.display.WakelockController(displayId, displayPowerCallbacks);
        }

        com.android.server.display.DisplayPowerProximityStateController getDisplayPowerProximityStateController(com.android.server.display.WakelockController wakelockController, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Looper looper, java.lang.Runnable nudgeUpdatePowerState, int displayId, android.hardware.SensorManager sensorManager, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.DisplayPowerProximityStateController(wakelockController, displayDeviceConfig, looper, nudgeUpdatePowerState, displayId, sensorManager, null, dpcExt);
        }

        com.android.server.display.AutomaticBrightnessController getAutomaticBrightnessController(com.android.server.display.AutomaticBrightnessController.Callbacks callbacks, android.os.Looper looper, android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, android.util.SparseArray<com.android.server.display.BrightnessMappingStrategy> brightnessMappingStrategyMap, int lightSensorWarmUpTime, float brightnessMin, float brightnessMax, float dozeScaleFactor, int lightSensorRate, int initialLightSensorRate, long brighteningLightDebounceConfig, long darkeningLightDebounceConfig, long brighteningLightDebounceConfigIdle, long darkeningLightDebounceConfigIdle, boolean resetAmbientLuxAfterWarmUpConfig, com.android.server.display.config.HysteresisLevels ambientBrightnessThresholds, com.android.server.display.config.HysteresisLevels screenBrightnessThresholds, com.android.server.display.config.HysteresisLevels ambientBrightnessThresholdsIdle, com.android.server.display.config.HysteresisLevels screenBrightnessThresholdsIdle, android.content.Context context, com.android.server.display.BrightnessRangeController brightnessModeController, com.android.server.display.BrightnessThrottler brightnessThrottler, int ambientLightHorizonShort, int ambientLightHorizonLong, float userLux, float userNits, com.android.server.display.brightness.clamper.BrightnessClamperController brightnessClamperController, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags) {
            return new com.android.server.display.AutomaticBrightnessController(callbacks, looper, sensorManager, lightSensor, brightnessMappingStrategyMap, lightSensorWarmUpTime, brightnessMin, brightnessMax, dozeScaleFactor, lightSensorRate, initialLightSensorRate, brighteningLightDebounceConfig, darkeningLightDebounceConfig, brighteningLightDebounceConfigIdle, darkeningLightDebounceConfigIdle, resetAmbientLuxAfterWarmUpConfig, ambientBrightnessThresholds, screenBrightnessThresholds, ambientBrightnessThresholdsIdle, screenBrightnessThresholdsIdle, context, brightnessModeController, brightnessThrottler, ambientLightHorizonShort, ambientLightHorizonLong, userLux, userNits, displayManagerFlags);
        }

        com.android.server.display.BrightnessMappingStrategy getDefaultModeBrightnessMapper(android.content.Context context, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, com.android.server.display.whitebalance.DisplayWhiteBalanceController displayWhiteBalanceController) {
            return com.android.server.display.BrightnessMappingStrategy.create(context, displayDeviceConfig, 0, displayWhiteBalanceController);
        }

        com.android.server.display.ScreenOffBrightnessSensorController getScreenOffBrightnessSensorController(android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, android.os.Handler handler, com.android.server.display.ScreenOffBrightnessSensorController.Clock clock, int[] sensorValueToLux, com.android.server.display.BrightnessMappingStrategy brightnessMapper) {
            return new com.android.server.display.ScreenOffBrightnessSensorController(sensorManager, lightSensor, handler, clock, sensorValueToLux, brightnessMapper);
        }

        com.android.server.display.HighBrightnessModeController getHighBrightnessModeController(android.os.Handler handler, int width, int height, android.os.IBinder displayToken, java.lang.String displayUniqueId, float brightnessMin, float brightnessMax, com.android.server.display.DisplayDeviceConfig.HighBrightnessModeData hbmData, com.android.server.display.HighBrightnessModeController.HdrBrightnessDeviceConfig hdrBrightnessCfg, java.lang.Runnable hbmChangeCallback, com.android.server.display.HighBrightnessModeMetadata hbmMetadata, android.content.Context context) {
            return new com.android.server.display.HighBrightnessModeController(handler, width, height, displayToken, displayUniqueId, brightnessMin, brightnessMax, hbmData, hdrBrightnessCfg, hbmChangeCallback, hbmMetadata, context);
        }

        com.android.server.display.BrightnessRangeController getBrightnessRangeController(com.android.server.display.HighBrightnessModeController hbmController, java.lang.Runnable modeChangeCallback, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Handler handler, com.android.server.display.feature.DisplayManagerFlags flags, android.os.IBinder displayToken, com.android.server.display.DisplayDeviceInfo info) {
            return new com.android.server.display.BrightnessRangeController(hbmController, modeChangeCallback, displayDeviceConfig, handler, flags, displayToken, info);
        }

        com.android.server.display.brightness.clamper.BrightnessClamperController getBrightnessClamperController(android.os.Handler handler, com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListener, com.android.server.display.brightness.clamper.BrightnessClamperController.DisplayDeviceData data, android.content.Context context, com.android.server.display.feature.DisplayManagerFlags flags, android.hardware.SensorManager sensorManager, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.brightness.clamper.BrightnessClamperController(handler, clamperChangeListener, data, context, flags, sensorManager, dpcExt);
        }

        com.android.server.display.whitebalance.DisplayWhiteBalanceController getDisplayWhiteBalanceController(android.os.Handler handler, android.hardware.SensorManager sensorManager, android.content.res.Resources resources) {
            return com.android.server.display.whitebalance.DisplayWhiteBalanceFactory.create(handler, sensorManager, resources);
        }

        boolean isColorFadeEnabled() {
            return !android.app.ActivityManager.isLowRamDeviceStatic();
        }
    }

    static class CachedBrightnessInfo {
        public android.util.MutableFloat brightness = new android.util.MutableFloat(Float.NaN);
        public android.util.MutableFloat adjustedBrightness = new android.util.MutableFloat(Float.NaN);
        public android.util.MutableFloat brightnessMin = new android.util.MutableFloat(Float.NaN);
        public android.util.MutableFloat brightnessMax = new android.util.MutableFloat(Float.NaN);
        public android.util.MutableInt hbmMode = new android.util.MutableInt(0);
        public android.util.MutableFloat hbmTransitionPoint = new android.util.MutableFloat(Float.POSITIVE_INFINITY);
        public android.util.MutableInt brightnessMaxReason = new android.util.MutableInt(0);

        CachedBrightnessInfo() {
        }

        public boolean checkAndSetFloat(android.util.MutableFloat mf, float f) {
            if (mf.value != f) {
                mf.value = f;
                return true;
            }
            return false;
        }

        public boolean checkAndSetInt(android.util.MutableInt mi, int i) {
            if (mi.value != i) {
                mi.value = i;
                return true;
            }
            return false;
        }
    }

    public com.android.server.display.IOplusDisplayPowerControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class OplusDisplayPowerControllerWrapper implements com.android.server.display.IOplusDisplayPowerControllerWrapper {
        private com.android.server.display.LogicalDisplayMapper mLogicalDisplayMapper;

        private OplusDisplayPowerControllerWrapper() {
            this.mLogicalDisplayMapper = null;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setDebug(boolean val) {
            com.android.server.display.DisplayPowerController.DEBUG = val;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void updatePowerState() throws java.lang.Throwable {
            com.android.server.display.DisplayPowerController.this.lambda$new$0();
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void sendUpdatePowerState() {
            com.android.server.display.DisplayPowerController.this.sendUpdatePowerState();
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void animateScreenBrightness(float target, float sdrTarget, float rate) {
            com.android.server.display.DisplayPowerController.this.animateScreenBrightness(target, sdrTarget, rate);
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessRangeMinimum(float val) {
            com.android.server.display.DisplayPowerController.this.mScreenBrightnessRangeMinimum = val;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessRangeMaximum(float val) {
            com.android.server.display.DisplayPowerController.this.mScreenBrightnessRangeMaximum = val;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessNormalMaximum(float val) {
            com.android.server.display.DisplayPowerController.this.mScreenBrightnessNormalMaximum = val;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setScreenBrightnessDefault(float val) {
            com.android.server.display.DisplayPowerController.this.mScreenBrightnessDefault = val;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setAutoBrightnessAdjustment(float val) {
            if (com.android.server.display.DisplayPowerController.this.mAutomaticBrightnessStrategy != null) {
                com.android.server.display.DisplayPowerController.this.mAutomaticBrightnessStrategy.setAutoBrightnessAdjustment(val);
            }
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void handleSettingsChange() {
            com.android.server.display.DisplayPowerController.this.handleSettingsChange();
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public com.android.server.display.DisplayPowerProximityStateController getDisplayPowerProximityStateController() {
            return com.android.server.display.DisplayPowerController.this.mDisplayPowerProximityStateController;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void setLogicalDisplayMapper(com.android.server.display.LogicalDisplayMapper mapper) {
            this.mLogicalDisplayMapper = mapper;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public com.android.server.display.LogicalDisplayMapper getLogicalDisplayMapper() {
            return this.mLogicalDisplayMapper;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public boolean isScreenOnUnblockerExist() {
            return com.android.server.display.DisplayPowerController.this.mPendingScreenOnUnblocker != null;
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public void sendMsgUnblockScreenOn(boolean needBlockedScreenOn) {
            if (!needBlockedScreenOn && com.android.server.display.DisplayPowerController.this.mHandler.hasMessages(2)) {
                com.android.server.display.DisplayPowerController.this.mHandler.removeMessages(2);
                android.os.Message msg = com.android.server.display.DisplayPowerController.this.mHandler.obtainMessage(2, com.android.server.display.DisplayPowerController.this.mPendingScreenOnUnblocker);
                msg.setAsynchronous(true);
                com.android.server.display.DisplayPowerController.this.mHandler.sendMessage(msg);
                android.util.Slog.d(com.android.server.display.DisplayPowerController.this.mTag, "MSG_SCREEN_ON_UNBLOCKED sended");
            }
        }

        @Override // com.android.server.display.IOplusDisplayPowerControllerWrapper
        public int getDisplayId() {
            return com.android.server.display.DisplayPowerController.this.mDisplayId;
        }
    }
}
