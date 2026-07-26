package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class AutomaticBrightnessController {
    private static final long AMBIENT_LIGHT_PREDICTION_TIME_MILLIS = 100;
    public static final int AUTO_BRIGHTNESS_DISABLED = 2;
    public static final int AUTO_BRIGHTNESS_ENABLED = 1;
    public static final int AUTO_BRIGHTNESS_MODE_DEFAULT = 0;
    public static final int AUTO_BRIGHTNESS_MODE_DOZE = 2;
    public static final int AUTO_BRIGHTNESS_MODE_IDLE = 1;
    public static final int AUTO_BRIGHTNESS_MODE_MAX = 2;
    public static final int AUTO_BRIGHTNESS_OFF_DUE_TO_DISPLAY_STATE = 3;
    private static final int BRIGHTNESS_ADJUSTMENT_SAMPLE_DEBOUNCE_MILLIS = 10000;
    private static final boolean DEBUG_PRETEND_LIGHT_SENSOR_ABSENT = false;
    private static final int MSG_BRIGHTNESS_ADJUSTMENT_SAMPLE = 2;
    private static final int MSG_INVALIDATE_CURRENT_SHORT_TERM_MODEL = 3;
    private static final int MSG_INVALIDATE_PAUSED_SHORT_TERM_MODEL = 7;
    private static final int MSG_RUN_UPDATE = 6;
    private static final int MSG_UPDATE_AMBIENT_LUX = 1;
    private static final int MSG_UPDATE_FOREGROUND_APP = 4;
    private static final int MSG_UPDATE_FOREGROUND_APP_SYNC = 5;
    private static final java.lang.String TAG = "AutomaticBrightnessController";
    private android.app.IActivityTaskManager mActivityTaskManager;
    private float mAmbientBrighteningThreshold;
    private final com.android.server.display.config.HysteresisLevels mAmbientBrightnessThresholds;
    private final com.android.server.display.config.HysteresisLevels mAmbientBrightnessThresholdsIdle;
    private float mAmbientDarkeningThreshold;
    private final int mAmbientLightHorizonLong;
    private final int mAmbientLightHorizonShort;
    private com.android.server.display.AutomaticBrightnessController.AmbientLightRingBuffer mAmbientLightRingBuffer;
    private float mAmbientLux;
    private boolean mAmbientLuxValid;
    private final long mBrighteningLightDebounceConfig;
    private final long mBrighteningLightDebounceConfigIdle;
    private float mBrightnessAdjustmentSampleOldBrightness;
    private float mBrightnessAdjustmentSampleOldLux;
    private boolean mBrightnessAdjustmentSamplePending;
    private final android.util.SparseArray<com.android.server.display.BrightnessMappingStrategy> mBrightnessMappingStrategyMap;
    private final com.android.server.display.BrightnessRangeController mBrightnessRangeController;
    private final com.android.server.display.BrightnessThrottler mBrightnessThrottler;
    private final com.android.server.display.AutomaticBrightnessController.Callbacks mCallbacks;
    private com.android.server.display.AutomaticBrightnessController.Clock mClock;
    private android.content.Context mContext;
    private com.android.server.display.BrightnessMappingStrategy mCurrentBrightnessMapper;
    private int mCurrentLightSensorRate;
    private final long mDarkeningLightDebounceConfig;
    private final long mDarkeningLightDebounceConfigIdle;
    private final com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private int mDisplayPolicy;
    private int mDisplayState;
    private final float mDozeScaleFactor;
    private float mFastAmbientLux;
    private int mForegroundAppCategory;
    private java.lang.String mForegroundAppPackageName;
    private com.android.server.display.AutomaticBrightnessController.AutomaticBrightnessHandler mHandler;
    private final int mInitialLightSensorRate;
    private final com.android.server.display.AutomaticBrightnessController.Injector mInjector;
    private boolean mIsBrightnessThrottled;
    private float mLastObservedLux;
    private long mLastObservedLuxTime;
    private final android.hardware.Sensor mLightSensor;
    private long mLightSensorEnableTime;
    private boolean mLightSensorEnabled;
    private final android.hardware.SensorEventListener mLightSensorListener;
    private int mLightSensorWarmUpTimeConfig;
    private boolean mLoggingEnabled;
    private final int mNormalLightSensorRate;
    private android.content.pm.PackageManager mPackageManager;
    private final com.android.server.display.AutomaticBrightnessController.ShortTermModel mPausedShortTermModel;
    private int mPendingForegroundAppCategory;
    private java.lang.String mPendingForegroundAppPackageName;
    private float mPreThresholdBrightness;
    private float mPreThresholdLux;
    private float mRawScreenAutoBrightness;
    private int mRecentLightSamples;
    private final boolean mResetAmbientLuxAfterWarmUpConfig;
    private float mScreenAutoBrightness;
    private float mScreenBrighteningThreshold;
    private final float mScreenBrightnessRangeMaximum;
    private final float mScreenBrightnessRangeMinimum;
    private final com.android.server.display.config.HysteresisLevels mScreenBrightnessThresholds;
    private final com.android.server.display.config.HysteresisLevels mScreenBrightnessThresholdsIdle;
    private float mScreenDarkeningThreshold;
    private final android.hardware.SensorManager mSensorManager;
    private final com.android.server.display.AutomaticBrightnessController.ShortTermModel mShortTermModel;
    private float mSlowAmbientLux;
    private int mState;
    private com.android.server.display.AutomaticBrightnessController.TaskStackListenerImpl mTaskStackListener;
    private final int mWeightingIntercept;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AutomaticBrightnessMode {
    }

    interface Callbacks {
        void updateBrightness();
    }

    interface Clock {
        long getSensorEventScaleTime();

        long uptimeMillis();
    }

    AutomaticBrightnessController(com.android.server.display.AutomaticBrightnessController.Callbacks callbacks, android.os.Looper looper, android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, android.util.SparseArray<com.android.server.display.BrightnessMappingStrategy> brightnessMappingStrategyMap, int lightSensorWarmUpTime, float brightnessMin, float brightnessMax, float dozeScaleFactor, int lightSensorRate, int initialLightSensorRate, long brighteningLightDebounceConfig, long darkeningLightDebounceConfig, long brighteningLightDebounceConfigIdle, long darkeningLightDebounceConfigIdle, boolean resetAmbientLuxAfterWarmUpConfig, com.android.server.display.config.HysteresisLevels ambientBrightnessThresholds, com.android.server.display.config.HysteresisLevels screenBrightnessThresholds, com.android.server.display.config.HysteresisLevels ambientBrightnessThresholdsIdle, com.android.server.display.config.HysteresisLevels screenBrightnessThresholdsIdle, android.content.Context context, com.android.server.display.BrightnessRangeController brightnessModeController, com.android.server.display.BrightnessThrottler brightnessThrottler, int ambientLightHorizonShort, int ambientLightHorizonLong, float userLux, float userNits, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags) {
        this(new com.android.server.display.AutomaticBrightnessController.Injector(), callbacks, looper, sensorManager, lightSensor, brightnessMappingStrategyMap, lightSensorWarmUpTime, brightnessMin, brightnessMax, dozeScaleFactor, lightSensorRate, initialLightSensorRate, brighteningLightDebounceConfig, darkeningLightDebounceConfig, brighteningLightDebounceConfigIdle, darkeningLightDebounceConfigIdle, resetAmbientLuxAfterWarmUpConfig, ambientBrightnessThresholds, screenBrightnessThresholds, ambientBrightnessThresholdsIdle, screenBrightnessThresholdsIdle, context, brightnessModeController, brightnessThrottler, ambientLightHorizonShort, ambientLightHorizonLong, userLux, userNits, displayManagerFlags);
    }

    AutomaticBrightnessController(com.android.server.display.AutomaticBrightnessController.Injector injector, com.android.server.display.AutomaticBrightnessController.Callbacks callbacks, android.os.Looper looper, android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, android.util.SparseArray<com.android.server.display.BrightnessMappingStrategy> brightnessMappingStrategyMap, int lightSensorWarmUpTime, float brightnessMin, float brightnessMax, float dozeScaleFactor, int lightSensorRate, int initialLightSensorRate, long brighteningLightDebounceConfig, long darkeningLightDebounceConfig, long brighteningLightDebounceConfigIdle, long darkeningLightDebounceConfigIdle, boolean resetAmbientLuxAfterWarmUpConfig, com.android.server.display.config.HysteresisLevels ambientBrightnessThresholds, com.android.server.display.config.HysteresisLevels screenBrightnessThresholds, com.android.server.display.config.HysteresisLevels ambientBrightnessThresholdsIdle, com.android.server.display.config.HysteresisLevels screenBrightnessThresholdsIdle, android.content.Context context, com.android.server.display.BrightnessRangeController brightnessRangeController, com.android.server.display.BrightnessThrottler brightnessThrottler, int ambientLightHorizonShort, int ambientLightHorizonLong, float userLux, float userNits, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags) {
        this.mAmbientLux = -1.0f;
        this.mScreenAutoBrightness = Float.NaN;
        this.mRawScreenAutoBrightness = Float.NaN;
        this.mDisplayPolicy = 0;
        this.mDisplayState = 0;
        this.mState = 2;
        this.mLightSensorListener = new android.hardware.SensorEventListener() { // from class: com.android.server.display.AutomaticBrightnessController.2
            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(android.hardware.SensorEvent event) {
                if (com.android.server.display.AutomaticBrightnessController.this.mLightSensorEnabled) {
                    long time = com.android.server.display.AutomaticBrightnessController.this.mDisplayManagerFlags.offloadControlsDozeAutoBrightness() ? java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(event.timestamp) : com.android.server.display.AutomaticBrightnessController.this.mClock.uptimeMillis();
                    float lux = event.values[0];
                    com.android.server.display.AutomaticBrightnessController.this.handleLightSensorEvent(time, lux);
                }
            }

            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
            }
        };
        this.mInjector = injector;
        this.mClock = injector.createClock(displayManagerFlags.offloadControlsDozeAutoBrightness());
        this.mContext = context;
        this.mCallbacks = callbacks;
        this.mSensorManager = sensorManager;
        this.mCurrentBrightnessMapper = brightnessMappingStrategyMap.get(0);
        this.mScreenBrightnessRangeMinimum = brightnessMin;
        this.mScreenBrightnessRangeMaximum = brightnessMax;
        this.mLightSensorWarmUpTimeConfig = lightSensorWarmUpTime;
        this.mDozeScaleFactor = dozeScaleFactor;
        this.mNormalLightSensorRate = lightSensorRate;
        this.mInitialLightSensorRate = initialLightSensorRate;
        this.mCurrentLightSensorRate = -1;
        this.mBrighteningLightDebounceConfig = brighteningLightDebounceConfig;
        this.mDarkeningLightDebounceConfig = darkeningLightDebounceConfig;
        this.mBrighteningLightDebounceConfigIdle = brighteningLightDebounceConfigIdle;
        this.mDarkeningLightDebounceConfigIdle = darkeningLightDebounceConfigIdle;
        this.mResetAmbientLuxAfterWarmUpConfig = resetAmbientLuxAfterWarmUpConfig;
        this.mAmbientLightHorizonLong = ambientLightHorizonLong;
        this.mAmbientLightHorizonShort = ambientLightHorizonShort;
        this.mWeightingIntercept = ambientLightHorizonLong;
        this.mAmbientBrightnessThresholds = ambientBrightnessThresholds;
        this.mAmbientBrightnessThresholdsIdle = ambientBrightnessThresholdsIdle;
        this.mScreenBrightnessThresholds = screenBrightnessThresholds;
        this.mScreenBrightnessThresholdsIdle = screenBrightnessThresholdsIdle;
        this.mShortTermModel = new com.android.server.display.AutomaticBrightnessController.ShortTermModel();
        this.mPausedShortTermModel = new com.android.server.display.AutomaticBrightnessController.ShortTermModel();
        this.mHandler = new com.android.server.display.AutomaticBrightnessController.AutomaticBrightnessHandler(looper);
        this.mAmbientLightRingBuffer = new com.android.server.display.AutomaticBrightnessController.AmbientLightRingBuffer(this.mNormalLightSensorRate, this.mAmbientLightHorizonLong, this.mClock);
        this.mLightSensor = lightSensor;
        this.mActivityTaskManager = android.app.ActivityTaskManager.getService();
        this.mPackageManager = this.mContext.getPackageManager();
        this.mTaskStackListener = new com.android.server.display.AutomaticBrightnessController.TaskStackListenerImpl();
        this.mForegroundAppPackageName = null;
        this.mPendingForegroundAppPackageName = null;
        this.mForegroundAppCategory = -1;
        this.mPendingForegroundAppCategory = -1;
        this.mBrightnessRangeController = brightnessRangeController;
        this.mBrightnessThrottler = brightnessThrottler;
        this.mBrightnessMappingStrategyMap = brightnessMappingStrategyMap;
        this.mDisplayManagerFlags = displayManagerFlags;
        if (userNits != -1.0f) {
            setScreenBrightnessByUser(userLux, getBrightnessFromNits(userNits));
        }
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        for (int i = 0; i < this.mBrightnessMappingStrategyMap.size(); i++) {
            this.mBrightnessMappingStrategyMap.valueAt(i).setLoggingEnabled(loggingEnabled);
        }
        this.mLoggingEnabled = loggingEnabled;
        return true;
    }

    public float getAutomaticScreenBrightness() {
        return getAutomaticScreenBrightness(null);
    }

    public float getAutomaticScreenBrightness(com.android.server.display.brightness.BrightnessEvent brightnessEvent) {
        if (brightnessEvent != null) {
            brightnessEvent.setLux(this.mAmbientLuxValid ? this.mAmbientLux : Float.NaN);
            brightnessEvent.setPreThresholdLux(this.mPreThresholdLux);
            brightnessEvent.setPreThresholdBrightness(this.mPreThresholdBrightness);
            brightnessEvent.setRecommendedBrightness(this.mScreenAutoBrightness);
            brightnessEvent.setFlags(brightnessEvent.getFlags() | (!this.mAmbientLuxValid ? 2 : 0) | (shouldApplyDozeScaleFactor() ? 4 : 0));
            brightnessEvent.setAutoBrightnessMode(getMode());
        }
        if (!this.mAmbientLuxValid) {
            return Float.NaN;
        }
        if (shouldApplyDozeScaleFactor()) {
            return this.mScreenAutoBrightness * this.mDozeScaleFactor;
        }
        return this.mScreenAutoBrightness;
    }

    public float getRawAutomaticScreenBrightness() {
        return this.mRawScreenAutoBrightness;
    }

    public boolean hasValidAmbientLux() {
        return this.mAmbientLuxValid;
    }

    public float getAutomaticScreenBrightnessAdjustment() {
        return this.mCurrentBrightnessMapper.getAutoBrightnessAdjustment();
    }

    public void configure(int state, android.hardware.display.BrightnessConfiguration configuration, float brightness, boolean userChangedBrightness, float adjustment, boolean userChangedAutoBrightnessAdjustment, int displayPolicy, int displayState, boolean shouldResetShortTermModel) {
        this.mState = state;
        boolean changed = setBrightnessConfiguration(configuration, shouldResetShortTermModel) | setDisplayPolicy(displayPolicy);
        this.mDisplayState = displayState;
        if (userChangedAutoBrightnessAdjustment) {
            changed |= setAutoBrightnessAdjustment(adjustment);
        }
        boolean userInitiatedChange = true;
        boolean enable = this.mState == 1;
        if (userChangedBrightness && enable) {
            changed |= setScreenBrightnessByUser(brightness);
        }
        if (!userChangedBrightness && !userChangedAutoBrightnessAdjustment) {
            userInitiatedChange = false;
        }
        if (userInitiatedChange && enable) {
            prepareBrightnessAdjustmentSample();
        }
        boolean changed2 = changed | setLightSensorEnabled(enable);
        if (this.mIsBrightnessThrottled != this.mBrightnessThrottler.isThrottled()) {
            this.mIsBrightnessThrottled = this.mBrightnessThrottler.isThrottled();
            changed2 = true;
        }
        if (changed2) {
            updateAutoBrightness(false, userInitiatedChange);
        }
    }

    public void stop() {
        setLightSensorEnabled(false);
    }

    public boolean hasUserDataPoints() {
        return this.mCurrentBrightnessMapper.hasUserDataPoints();
    }

    public boolean isDefaultConfig() {
        return this.mCurrentBrightnessMapper.getMode() == 0 && this.mCurrentBrightnessMapper.isDefaultConfig();
    }

    public android.hardware.display.BrightnessConfiguration getDefaultConfig() {
        return this.mBrightnessMappingStrategyMap.get(0).getDefaultConfig();
    }

    public void update() {
        this.mHandler.sendEmptyMessage(6);
    }

    float getAmbientLux() {
        return this.mAmbientLux;
    }

    float getSlowAmbientLux() {
        return this.mSlowAmbientLux;
    }

    float getFastAmbientLux() {
        return this.mFastAmbientLux;
    }

    private boolean setDisplayPolicy(int policy) {
        if (this.mDisplayPolicy == policy) {
            return false;
        }
        int oldPolicy = this.mDisplayPolicy;
        this.mDisplayPolicy = policy;
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Display policy transitioning from " + oldPolicy + " to " + policy);
        }
        if (!isInteractivePolicy(policy) && isInteractivePolicy(oldPolicy) && !isInIdleMode()) {
            this.mHandler.sendEmptyMessageDelayed(3, this.mCurrentBrightnessMapper.getShortTermModelTimeout());
            return true;
        }
        if (isInteractivePolicy(policy) && !isInteractivePolicy(oldPolicy)) {
            this.mHandler.removeMessages(3);
            return true;
        }
        return true;
    }

    private static boolean isInteractivePolicy(int policy) {
        return policy == 3 || policy == 2;
    }

    private boolean setScreenBrightnessByUser(float brightness) {
        if (!this.mAmbientLuxValid) {
            return false;
        }
        return setScreenBrightnessByUser(this.mAmbientLux, brightness);
    }

    private boolean setScreenBrightnessByUser(float lux, float brightness) {
        if (lux == -1.0f || java.lang.Float.isNaN(brightness)) {
            return false;
        }
        this.mCurrentBrightnessMapper.addUserDataPoint(lux, brightness);
        this.mShortTermModel.setUserBrightness(lux, brightness);
        return true;
    }

    public void resetShortTermModel() {
        this.mCurrentBrightnessMapper.clearUserDataPoints();
        this.mShortTermModel.reset();
    }

    public boolean setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration configuration, boolean shouldResetShortTermModel) {
        if (!this.mBrightnessMappingStrategyMap.get(0).setBrightnessConfiguration(configuration)) {
            return false;
        }
        if (!isInIdleMode() && shouldResetShortTermModel) {
            resetShortTermModel();
            return true;
        }
        return true;
    }

    public int getMode() {
        return this.mCurrentBrightnessMapper.getMode();
    }

    public boolean isInIdleMode() {
        return this.mCurrentBrightnessMapper.getMode() == 1;
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println();
        pw.println("Automatic Brightness Controller Configuration:");
        pw.println("  mState=" + configStateToString(this.mState));
        pw.println("  mScreenBrightnessRangeMinimum=" + this.mScreenBrightnessRangeMinimum);
        pw.println("  mScreenBrightnessRangeMaximum=" + this.mScreenBrightnessRangeMaximum);
        pw.println("  mDozeScaleFactor=" + this.mDozeScaleFactor);
        pw.println("  mInitialLightSensorRate=" + this.mInitialLightSensorRate);
        pw.println("  mNormalLightSensorRate=" + this.mNormalLightSensorRate);
        pw.println("  mLightSensorWarmUpTimeConfig=" + this.mLightSensorWarmUpTimeConfig);
        pw.println("  mBrighteningLightDebounceConfig=" + this.mBrighteningLightDebounceConfig);
        pw.println("  mDarkeningLightDebounceConfig=" + this.mDarkeningLightDebounceConfig);
        pw.println("  mBrighteningLightDebounceConfigIdle=" + this.mBrighteningLightDebounceConfigIdle);
        pw.println("  mDarkeningLightDebounceConfigIdle=" + this.mDarkeningLightDebounceConfigIdle);
        pw.println("  mResetAmbientLuxAfterWarmUpConfig=" + this.mResetAmbientLuxAfterWarmUpConfig);
        pw.println("  mAmbientLightHorizonLong=" + this.mAmbientLightHorizonLong);
        pw.println("  mAmbientLightHorizonShort=" + this.mAmbientLightHorizonShort);
        pw.println("  mWeightingIntercept=" + this.mWeightingIntercept);
        pw.println();
        pw.println("Automatic Brightness Controller State:");
        pw.println("  mLightSensor=" + this.mLightSensor);
        pw.println("  mLightSensorEnabled=" + this.mLightSensorEnabled);
        pw.println("  mLightSensorEnableTime=" + android.util.TimeUtils.formatUptime(this.mLightSensorEnableTime));
        pw.println("  mCurrentLightSensorRate=" + this.mCurrentLightSensorRate);
        pw.println("  mAmbientLux=" + this.mAmbientLux);
        pw.println("  mAmbientLuxValid=" + this.mAmbientLuxValid);
        pw.println("  mPreThresholdLux=" + this.mPreThresholdLux);
        pw.println("  mPreThresholdBrightness=" + this.mPreThresholdBrightness);
        pw.println("  mAmbientBrighteningThreshold=" + this.mAmbientBrighteningThreshold);
        pw.println("  mAmbientDarkeningThreshold=" + this.mAmbientDarkeningThreshold);
        pw.println("  mScreenBrighteningThreshold=" + this.mScreenBrighteningThreshold);
        pw.println("  mScreenDarkeningThreshold=" + this.mScreenDarkeningThreshold);
        pw.println("  mLastObservedLux=" + this.mLastObservedLux);
        pw.println("  mLastObservedLuxTime=" + android.util.TimeUtils.formatUptime(this.mLastObservedLuxTime));
        pw.println("  mRecentLightSamples=" + this.mRecentLightSamples);
        pw.println("  mAmbientLightRingBuffer=" + this.mAmbientLightRingBuffer);
        pw.println("  mScreenAutoBrightness=" + this.mScreenAutoBrightness);
        pw.println("  mDisplayPolicy=" + android.hardware.display.DisplayManagerInternal.DisplayPowerRequest.policyToString(this.mDisplayPolicy));
        pw.println("  mShortTermModel=");
        this.mShortTermModel.dump(pw);
        pw.println("  mPausedShortTermModel=");
        this.mPausedShortTermModel.dump(pw);
        pw.println();
        pw.println("  mBrightnessAdjustmentSamplePending=" + this.mBrightnessAdjustmentSamplePending);
        pw.println("  mBrightnessAdjustmentSampleOldLux=" + this.mBrightnessAdjustmentSampleOldLux);
        pw.println("  mBrightnessAdjustmentSampleOldBrightness=" + this.mBrightnessAdjustmentSampleOldBrightness);
        pw.println("  mForegroundAppPackageName=" + this.mForegroundAppPackageName);
        pw.println("  mPendingForegroundAppPackageName=" + this.mPendingForegroundAppPackageName);
        pw.println("  mForegroundAppCategory=" + this.mForegroundAppCategory);
        pw.println("  mPendingForegroundAppCategory=" + this.mPendingForegroundAppCategory);
        pw.println("  Current mode=" + com.android.server.display.config.DisplayBrightnessMappingConfig.autoBrightnessModeToString(this.mCurrentBrightnessMapper.getMode()));
        for (int i = 0; i < this.mBrightnessMappingStrategyMap.size(); i++) {
            pw.println();
            pw.println("  Mapper for mode " + com.android.server.display.config.DisplayBrightnessMappingConfig.autoBrightnessModeToString(this.mBrightnessMappingStrategyMap.keyAt(i)) + ":");
            this.mBrightnessMappingStrategyMap.valueAt(i).dump(pw, this.mBrightnessRangeController.getNormalBrightnessMax());
        }
        pw.println();
        pw.println("  mAmbientBrightnessThresholds=" + this.mAmbientBrightnessThresholds);
        pw.println("  mAmbientBrightnessThresholdsIdle=" + this.mAmbientBrightnessThresholdsIdle);
        pw.println("  mScreenBrightnessThresholds=" + this.mScreenBrightnessThresholds);
        pw.println("  mScreenBrightnessThresholdsIdle=" + this.mScreenBrightnessThresholdsIdle);
    }

    public float[] getLastSensorValues() {
        return this.mAmbientLightRingBuffer.getAllLuxValues();
    }

    public long[] getLastSensorTimestamps() {
        return this.mAmbientLightRingBuffer.getAllTimestamps();
    }

    private java.lang.String configStateToString(int state) {
        switch (state) {
            case 1:
                return "AUTO_BRIGHTNESS_ENABLED";
            case 2:
                return "AUTO_BRIGHTNESS_DISABLED";
            case 3:
                return "AUTO_BRIGHTNESS_OFF_DUE_TO_DISPLAY_STATE";
            default:
                return java.lang.String.valueOf(state);
        }
    }

    private boolean setLightSensorEnabled(boolean enable) {
        if (enable) {
            if (!this.mLightSensorEnabled) {
                this.mLightSensorEnabled = true;
                this.mLightSensorEnableTime = this.mClock.uptimeMillis();
                this.mCurrentLightSensorRate = this.mInitialLightSensorRate;
                registerForegroundAppUpdater();
                this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, this.mCurrentLightSensorRate * 1000, this.mHandler);
                return true;
            }
        } else if (this.mLightSensorEnabled) {
            this.mLightSensorEnabled = false;
            this.mAmbientLuxValid = !this.mResetAmbientLuxAfterWarmUpConfig;
            if (!this.mAmbientLuxValid) {
                this.mPreThresholdLux = Float.NaN;
            }
            this.mScreenAutoBrightness = Float.NaN;
            this.mRawScreenAutoBrightness = Float.NaN;
            this.mPreThresholdBrightness = Float.NaN;
            this.mRecentLightSamples = 0;
            this.mAmbientLightRingBuffer.clear();
            this.mCurrentLightSensorRate = -1;
            this.mHandler.removeMessages(1);
            unregisterForegroundAppUpdater();
            this.mSensorManager.unregisterListener(this.mLightSensorListener);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLightSensorEvent(long time, float lux) {
        android.os.Trace.traceCounter(131072L, "ALS", (int) lux);
        this.mHandler.removeMessages(1);
        if (this.mAmbientLightRingBuffer.size() == 0) {
            adjustLightSensorRate(this.mNormalLightSensorRate);
        }
        applyLightSensorMeasurement(time, lux);
        updateAmbientLux(time);
    }

    private void applyLightSensorMeasurement(long time, float lux) {
        this.mRecentLightSamples++;
        this.mAmbientLightRingBuffer.prune(time - ((long) this.mAmbientLightHorizonLong));
        this.mAmbientLightRingBuffer.push(time, lux);
        this.mLastObservedLux = lux;
        this.mLastObservedLuxTime = time;
    }

    private void adjustLightSensorRate(int lightSensorRate) {
        if (lightSensorRate != this.mCurrentLightSensorRate) {
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "adjustLightSensorRate: previousRate=" + this.mCurrentLightSensorRate + ", currentRate=" + lightSensorRate);
            }
            this.mCurrentLightSensorRate = lightSensorRate;
            this.mSensorManager.unregisterListener(this.mLightSensorListener);
            this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, lightSensorRate * 1000, this.mHandler);
        }
    }

    private boolean setAutoBrightnessAdjustment(float adjustment) {
        return this.mCurrentBrightnessMapper.setAutoBrightnessAdjustment(adjustment);
    }

    private void setAmbientLux(float lux) {
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "setAmbientLux(" + lux + ")");
        }
        if (lux < 0.0f) {
            android.util.Slog.w(TAG, "Ambient lux was negative, ignoring and setting to 0");
            lux = 0.0f;
        }
        this.mAmbientLux = lux;
        if (isInIdleMode()) {
            this.mAmbientBrighteningThreshold = this.mAmbientBrightnessThresholdsIdle.getBrighteningThreshold(lux);
            this.mAmbientDarkeningThreshold = this.mAmbientBrightnessThresholdsIdle.getDarkeningThreshold(lux);
        } else {
            this.mAmbientBrighteningThreshold = this.mAmbientBrightnessThresholds.getBrighteningThreshold(lux);
            this.mAmbientDarkeningThreshold = this.mAmbientBrightnessThresholds.getDarkeningThreshold(lux);
        }
        this.mBrightnessRangeController.onAmbientLuxChange(this.mAmbientLux);
        this.mShortTermModel.maybeReset(this.mAmbientLux);
    }

    private float calculateAmbientLux(long now, long horizon) {
        long j = now;
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "calculateAmbientLux(" + j + ", " + horizon + ")");
        }
        int N = this.mAmbientLightRingBuffer.size();
        if (N == 0) {
            android.util.Slog.e(TAG, "calculateAmbientLux: No ambient light readings available");
            return -1.0f;
        }
        int endIndex = 0;
        long horizonStartTime = j - horizon;
        for (int i = 0; i < N - 1 && this.mAmbientLightRingBuffer.getTime(i + 1) <= horizonStartTime; i++) {
            endIndex++;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "calculateAmbientLux: selected endIndex=" + endIndex + ", point=(" + this.mAmbientLightRingBuffer.getTime(endIndex) + ", " + this.mAmbientLightRingBuffer.getLux(endIndex) + ")");
        }
        float sum = 0.0f;
        float totalWeight = 0.0f;
        long endTime = AMBIENT_LIGHT_PREDICTION_TIME_MILLIS;
        int i2 = N - 1;
        while (i2 >= endIndex) {
            long eventTime = this.mAmbientLightRingBuffer.getTime(i2);
            if (i2 == endIndex && eventTime < horizonStartTime) {
                eventTime = horizonStartTime;
            }
            long horizonStartTime2 = horizonStartTime;
            int endIndex2 = endIndex;
            long startTime = eventTime - j;
            float weight = calculateWeight(startTime, endTime);
            float lux = this.mAmbientLightRingBuffer.getLux(i2);
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "calculateAmbientLux: [" + startTime + ", " + endTime + "]: lux=" + lux + ", weight=" + weight);
            }
            totalWeight += weight;
            sum += lux * weight;
            endTime = startTime;
            i2--;
            j = now;
            endIndex = endIndex2;
            horizonStartTime = horizonStartTime2;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "calculateAmbientLux: totalWeight=" + totalWeight + ", newAmbientLux=" + (sum / totalWeight));
        }
        return sum / totalWeight;
    }

    private float calculateWeight(long startDelta, long endDelta) {
        return weightIntegral(endDelta) - weightIntegral(startDelta);
    }

    private float weightIntegral(long x) {
        return x * ((x * 0.5f) + this.mWeightingIntercept);
    }

    private long nextAmbientLightBrighteningTransition(long time) {
        int N = this.mAmbientLightRingBuffer.size();
        long earliestValidTime = time;
        for (int i = N - 1; i >= 0 && this.mAmbientLightRingBuffer.getLux(i) > this.mAmbientBrighteningThreshold; i--) {
            earliestValidTime = this.mAmbientLightRingBuffer.getTime(i);
        }
        return (isInIdleMode() ? this.mBrighteningLightDebounceConfigIdle : this.mBrighteningLightDebounceConfig) + earliestValidTime;
    }

    private long nextAmbientLightDarkeningTransition(long time) {
        int N = this.mAmbientLightRingBuffer.size();
        long earliestValidTime = time;
        for (int i = N - 1; i >= 0 && this.mAmbientLightRingBuffer.getLux(i) < this.mAmbientDarkeningThreshold; i--) {
            earliestValidTime = this.mAmbientLightRingBuffer.getTime(i);
        }
        return (isInIdleMode() ? this.mDarkeningLightDebounceConfigIdle : this.mDarkeningLightDebounceConfig) + earliestValidTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAmbientLux() {
        long time = this.mClock.getSensorEventScaleTime();
        this.mAmbientLightRingBuffer.prune(time - ((long) this.mAmbientLightHorizonLong));
        updateAmbientLux(time);
    }

    private void updateAmbientLux(long time) {
        if (!this.mAmbientLuxValid) {
            long timeWhenSensorWarmedUp = ((long) this.mLightSensorWarmUpTimeConfig) + this.mLightSensorEnableTime;
            if (time < timeWhenSensorWarmedUp) {
                if (this.mLoggingEnabled) {
                    android.util.Slog.d(TAG, "updateAmbientLux: Sensor not ready yet: time=" + time + ", timeWhenSensorWarmedUp=" + timeWhenSensorWarmedUp);
                }
                this.mHandler.sendEmptyMessageAtTime(1, timeWhenSensorWarmedUp);
                return;
            } else {
                setAmbientLux(calculateAmbientLux(time, this.mAmbientLightHorizonShort));
                this.mAmbientLuxValid = true;
                if (this.mLoggingEnabled) {
                    android.util.Slog.d(TAG, "updateAmbientLux: Initializing: mAmbientLightRingBuffer=" + this.mAmbientLightRingBuffer + ", mAmbientLux=" + this.mAmbientLux);
                }
                updateAutoBrightness(true, false);
            }
        }
        long nextBrightenTransition = nextAmbientLightBrighteningTransition(time);
        long nextDarkenTransition = nextAmbientLightDarkeningTransition(time);
        this.mSlowAmbientLux = calculateAmbientLux(time, this.mAmbientLightHorizonLong);
        this.mFastAmbientLux = calculateAmbientLux(time, this.mAmbientLightHorizonShort);
        if ((this.mSlowAmbientLux >= this.mAmbientBrighteningThreshold && this.mFastAmbientLux >= this.mAmbientBrighteningThreshold && nextBrightenTransition <= time) || (this.mSlowAmbientLux <= this.mAmbientDarkeningThreshold && this.mFastAmbientLux <= this.mAmbientDarkeningThreshold && nextDarkenTransition <= time)) {
            this.mPreThresholdLux = this.mAmbientLux;
            setAmbientLux(this.mFastAmbientLux);
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "updateAmbientLux: " + (this.mFastAmbientLux > this.mAmbientLux ? "Brightened" : "Darkened") + ": mAmbientBrighteningThreshold=" + this.mAmbientBrighteningThreshold + ", mAmbientDarkeningThreshold=" + this.mAmbientDarkeningThreshold + ", mAmbientLightRingBuffer=" + this.mAmbientLightRingBuffer + ", mAmbientLux=" + this.mAmbientLux);
            }
            updateAutoBrightness(true, false);
            nextBrightenTransition = nextAmbientLightBrighteningTransition(time);
            nextDarkenTransition = nextAmbientLightDarkeningTransition(time);
        }
        long nextTransitionTime = java.lang.Math.min(nextDarkenTransition, nextBrightenTransition);
        long nextTransitionTime2 = nextTransitionTime > time ? nextTransitionTime : ((long) this.mNormalLightSensorRate) + time;
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "updateAmbientLux: Scheduling ambient lux update for " + nextTransitionTime2 + android.util.TimeUtils.formatUptime(nextTransitionTime2));
        }
        this.mHandler.sendEmptyMessageAtTime(1, convertToUptime(nextTransitionTime2));
    }

    private long convertToUptime(long time) {
        return (time - this.mClock.getSensorEventScaleTime()) + this.mClock.uptimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAutoBrightness(boolean sendUpdate, boolean isManuallySet) {
        if (!this.mAmbientLuxValid) {
            return;
        }
        float value = this.mCurrentBrightnessMapper.getBrightness(this.mAmbientLux, this.mForegroundAppPackageName, this.mForegroundAppCategory);
        this.mRawScreenAutoBrightness = value;
        float newScreenAutoBrightness = clampScreenBrightness(value);
        boolean currentBrightnessWithinAllowedRange = com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mScreenAutoBrightness, clampScreenBrightness(this.mScreenAutoBrightness));
        boolean withinThreshold = !java.lang.Float.isNaN(this.mScreenAutoBrightness) && newScreenAutoBrightness > this.mScreenDarkeningThreshold && newScreenAutoBrightness < this.mScreenBrighteningThreshold;
        if (withinThreshold && !isManuallySet && currentBrightnessWithinAllowedRange) {
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "ignoring newScreenAutoBrightness: " + this.mScreenDarkeningThreshold + " < " + newScreenAutoBrightness + " < " + this.mScreenBrighteningThreshold);
                return;
            }
            return;
        }
        if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mScreenAutoBrightness, newScreenAutoBrightness)) {
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "updateAutoBrightness: mScreenAutoBrightness=" + this.mScreenAutoBrightness + ", newScreenAutoBrightness=" + newScreenAutoBrightness);
            }
            if (!withinThreshold) {
                this.mPreThresholdBrightness = this.mScreenAutoBrightness;
            }
            this.mScreenAutoBrightness = newScreenAutoBrightness;
            if (isInIdleMode()) {
                this.mScreenBrighteningThreshold = clampScreenBrightness(this.mScreenBrightnessThresholdsIdle.getBrighteningThreshold(newScreenAutoBrightness));
                this.mScreenDarkeningThreshold = clampScreenBrightness(this.mScreenBrightnessThresholdsIdle.getDarkeningThreshold(newScreenAutoBrightness));
            } else {
                this.mScreenBrighteningThreshold = clampScreenBrightness(this.mScreenBrightnessThresholds.getBrighteningThreshold(newScreenAutoBrightness));
                this.mScreenDarkeningThreshold = clampScreenBrightness(this.mScreenBrightnessThresholds.getDarkeningThreshold(newScreenAutoBrightness));
            }
            if (sendUpdate) {
                this.mCallbacks.updateBrightness();
            }
        }
    }

    private float clampScreenBrightness(float value) {
        float minBrightness = java.lang.Math.min(this.mBrightnessRangeController.getCurrentBrightnessMin(), this.mBrightnessThrottler.getBrightnessCap());
        float maxBrightness = java.lang.Math.min(this.mBrightnessRangeController.getCurrentBrightnessMax(), this.mBrightnessThrottler.getBrightnessCap());
        return android.util.MathUtils.constrain(value, minBrightness, maxBrightness);
    }

    private void prepareBrightnessAdjustmentSample() {
        if (!this.mBrightnessAdjustmentSamplePending) {
            this.mBrightnessAdjustmentSamplePending = true;
            this.mBrightnessAdjustmentSampleOldLux = this.mAmbientLuxValid ? this.mAmbientLux : -1.0f;
            this.mBrightnessAdjustmentSampleOldBrightness = this.mScreenAutoBrightness;
        } else {
            this.mHandler.removeMessages(2);
        }
        this.mHandler.sendEmptyMessageDelayed(2, 10000L);
    }

    private void cancelBrightnessAdjustmentSample() {
        if (this.mBrightnessAdjustmentSamplePending) {
            this.mBrightnessAdjustmentSamplePending = false;
            this.mHandler.removeMessages(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void collectBrightnessAdjustmentSample() {
        if (this.mBrightnessAdjustmentSamplePending) {
            this.mBrightnessAdjustmentSamplePending = false;
            if (this.mAmbientLuxValid) {
                if (this.mScreenAutoBrightness >= 0.0f || this.mScreenAutoBrightness == -1.0f) {
                    if (this.mLoggingEnabled) {
                        android.util.Slog.d(TAG, "Auto-brightness adjustment changed by user: lux=" + this.mAmbientLux + ", brightness=" + this.mScreenAutoBrightness + ", ring=" + this.mAmbientLightRingBuffer);
                    }
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.AUTO_BRIGHTNESS_ADJ, java.lang.Float.valueOf(this.mBrightnessAdjustmentSampleOldLux), java.lang.Float.valueOf(this.mBrightnessAdjustmentSampleOldBrightness), java.lang.Float.valueOf(this.mAmbientLux), java.lang.Float.valueOf(this.mScreenAutoBrightness));
                }
            }
        }
    }

    private void registerForegroundAppUpdater() {
        try {
            this.mActivityTaskManager.registerTaskStackListener(this.mTaskStackListener);
            updateForegroundApp();
        } catch (android.os.RemoteException e) {
            if (this.mLoggingEnabled) {
                android.util.Slog.e(TAG, "Failed to register foreground app updater: " + e);
            }
        }
    }

    private void unregisterForegroundAppUpdater() {
        try {
            this.mActivityTaskManager.unregisterTaskStackListener(this.mTaskStackListener);
        } catch (android.os.RemoteException e) {
        }
        this.mForegroundAppPackageName = null;
        this.mForegroundAppCategory = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForegroundApp() {
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Attempting to update foreground app");
        }
        this.mInjector.getBackgroundThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.AutomaticBrightnessController.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    android.app.ActivityTaskManager.RootTaskInfo info = com.android.server.display.AutomaticBrightnessController.this.mActivityTaskManager.getFocusedRootTaskInfo();
                    if (info != null && info.topActivity != null) {
                        java.lang.String packageName = info.topActivity.getPackageName();
                        java.lang.String currentForegroundAppPackageName = com.android.server.display.AutomaticBrightnessController.this.mForegroundAppPackageName;
                        if (currentForegroundAppPackageName != null && currentForegroundAppPackageName.equals(packageName)) {
                            return;
                        }
                        com.android.server.display.AutomaticBrightnessController.this.mPendingForegroundAppPackageName = packageName;
                        com.android.server.display.AutomaticBrightnessController.this.mPendingForegroundAppCategory = -1;
                        try {
                            android.content.pm.ApplicationInfo app = com.android.server.display.AutomaticBrightnessController.this.mPackageManager.getApplicationInfo(packageName, 4194304);
                            com.android.server.display.AutomaticBrightnessController.this.mPendingForegroundAppCategory = app.category;
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        }
                        com.android.server.display.AutomaticBrightnessController.this.mHandler.sendEmptyMessage(5);
                    }
                } catch (android.os.RemoteException e2) {
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForegroundAppSync() {
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Updating foreground app: packageName=" + this.mPendingForegroundAppPackageName + ", category=" + this.mPendingForegroundAppCategory);
        }
        this.mForegroundAppPackageName = this.mPendingForegroundAppPackageName;
        this.mPendingForegroundAppPackageName = null;
        this.mForegroundAppCategory = this.mPendingForegroundAppCategory;
        this.mPendingForegroundAppCategory = -1;
        updateAutoBrightness(true, false);
    }

    private void switchModeAndShortTermModels(int mode) {
        com.android.server.display.AutomaticBrightnessController.ShortTermModel tempShortTermModel = new com.android.server.display.AutomaticBrightnessController.ShortTermModel();
        tempShortTermModel.set(this.mCurrentBrightnessMapper.getUserLux(), this.mCurrentBrightnessMapper.getUserBrightness(), true);
        this.mHandler.removeMessages(7);
        this.mHandler.sendEmptyMessageAtTime(7, this.mClock.uptimeMillis() + this.mCurrentBrightnessMapper.getShortTermModelTimeout());
        android.util.Slog.i(TAG, "mPreviousShortTermModel: " + this.mPausedShortTermModel);
        this.mCurrentBrightnessMapper = this.mBrightnessMappingStrategyMap.get(mode);
        if (this.mPausedShortTermModel != null) {
            if (!this.mPausedShortTermModel.maybeReset(this.mAmbientLux)) {
                setScreenBrightnessByUser(this.mPausedShortTermModel.mAnchor, this.mPausedShortTermModel.mBrightness);
            }
            this.mPausedShortTermModel.copyFrom(tempShortTermModel);
        }
    }

    public void switchMode(int mode, boolean sendUpdate) {
        if (!this.mBrightnessMappingStrategyMap.contains(mode) || this.mCurrentBrightnessMapper.getMode() == mode) {
            return;
        }
        android.util.Slog.i(TAG, "Switching to mode " + com.android.server.display.config.DisplayBrightnessMappingConfig.autoBrightnessModeToString(mode));
        if (mode == 1 || this.mCurrentBrightnessMapper.getMode() == 1) {
            switchModeAndShortTermModels(mode);
        } else {
            resetShortTermModel();
            this.mCurrentBrightnessMapper = this.mBrightnessMappingStrategyMap.get(mode);
        }
        if (sendUpdate) {
            update();
        } else {
            updateAutoBrightness(false, false);
        }
    }

    float getUserLux() {
        return this.mCurrentBrightnessMapper.getUserLux();
    }

    float getUserNits() {
        return convertToNits(this.mCurrentBrightnessMapper.getUserBrightness());
    }

    public float convertToNits(float brightness) {
        return this.mCurrentBrightnessMapper.convertToNits(brightness);
    }

    public float convertToAdjustedNits(float brightness) {
        return this.mCurrentBrightnessMapper.convertToAdjustedNits(brightness);
    }

    public float getBrightnessFromNits(float nits) {
        return this.mCurrentBrightnessMapper.getBrightnessFromNits(nits);
    }

    public void recalculateSplines(boolean applyAdjustment, float[] adjustment) {
        this.mCurrentBrightnessMapper.recalculateSplines(applyAdjustment, adjustment);
        resetShortTermModel();
        if (applyAdjustment) {
            setScreenBrightnessByUser(getAutomaticScreenBrightness());
        }
    }

    private boolean shouldApplyDozeScaleFactor() {
        return android.view.Display.isDozeState(this.mDisplayState) && getMode() != 2;
    }

    private class ShortTermModel {
        private float mAnchor;
        private float mBrightness;
        private boolean mIsValid;

        private ShortTermModel() {
            this.mAnchor = -1.0f;
            this.mBrightness = Float.NaN;
            this.mIsValid = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.mAnchor = -1.0f;
            this.mBrightness = Float.NaN;
            this.mIsValid = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalidate() {
            this.mIsValid = false;
            if (com.android.server.display.AutomaticBrightnessController.this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.AutomaticBrightnessController.TAG, "ShortTermModel: invalidate user data");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setUserBrightness(float lux, float brightness) {
            this.mAnchor = lux;
            this.mBrightness = brightness;
            this.mIsValid = true;
            if (com.android.server.display.AutomaticBrightnessController.this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.AutomaticBrightnessController.TAG, "ShortTermModel: anchor=" + this.mAnchor);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean maybeReset(float currentLux) {
            if (!this.mIsValid && this.mAnchor != -1.0f) {
                if (com.android.server.display.AutomaticBrightnessController.this.mCurrentBrightnessMapper.shouldResetShortTermModel(currentLux, this.mAnchor)) {
                    com.android.server.display.AutomaticBrightnessController.this.resetShortTermModel();
                } else {
                    this.mIsValid = true;
                }
                return this.mIsValid;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(float anchor, float brightness, boolean valid) {
            this.mAnchor = anchor;
            this.mBrightness = brightness;
            this.mIsValid = valid;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void copyFrom(com.android.server.display.AutomaticBrightnessController.ShortTermModel from) {
            this.mAnchor = from.mAnchor;
            this.mBrightness = from.mBrightness;
            this.mIsValid = from.mIsValid;
        }

        public java.lang.String toString() {
            return "mAnchor: " + this.mAnchor + "\n mBrightness: " + this.mBrightness + "\n mIsValid: " + this.mIsValid;
        }

        void dump(java.io.PrintWriter pw) {
            pw.println(this);
        }
    }

    private final class AutomaticBrightnessHandler extends android.os.Handler {
        public AutomaticBrightnessHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.display.AutomaticBrightnessController.this.updateAmbientLux();
                    break;
                case 2:
                    com.android.server.display.AutomaticBrightnessController.this.collectBrightnessAdjustmentSample();
                    break;
                case 3:
                    com.android.server.display.AutomaticBrightnessController.this.mShortTermModel.invalidate();
                    break;
                case 4:
                    com.android.server.display.AutomaticBrightnessController.this.updateForegroundApp();
                    break;
                case 5:
                    com.android.server.display.AutomaticBrightnessController.this.updateForegroundAppSync();
                    break;
                case 6:
                    com.android.server.display.AutomaticBrightnessController.this.updateAutoBrightness(true, false);
                    break;
                case 7:
                    com.android.server.display.AutomaticBrightnessController.this.mPausedShortTermModel.invalidate();
                    break;
            }
        }
    }

    class TaskStackListenerImpl extends android.app.TaskStackListener {
        TaskStackListenerImpl() {
        }

        public void onTaskStackChanged() {
            com.android.server.display.AutomaticBrightnessController.this.mHandler.sendEmptyMessage(4);
        }
    }

    private static final class AmbientLightRingBuffer {
        private static final float BUFFER_SLACK = 1.5f;
        private int mCapacity;
        com.android.server.display.AutomaticBrightnessController.Clock mClock;
        private int mCount;
        private int mEnd;
        private float[] mRingLux;
        private long[] mRingTime;
        private int mStart;

        public AmbientLightRingBuffer(long lightSensorRate, int ambientLightHorizon, com.android.server.display.AutomaticBrightnessController.Clock clock) {
            if (lightSensorRate <= 0) {
                throw new java.lang.IllegalArgumentException("lightSensorRate must be above 0");
            }
            this.mCapacity = (int) java.lang.Math.ceil((ambientLightHorizon * BUFFER_SLACK) / lightSensorRate);
            this.mRingLux = new float[this.mCapacity];
            this.mRingTime = new long[this.mCapacity];
            this.mClock = clock;
        }

        public float getLux(int index) {
            return this.mRingLux[offsetOf(index)];
        }

        public float[] getAllLuxValues() {
            float[] values = new float[this.mCount];
            if (this.mCount == 0) {
                return values;
            }
            if (this.mStart < this.mEnd) {
                java.lang.System.arraycopy(this.mRingLux, this.mStart, values, 0, this.mCount);
            } else {
                java.lang.System.arraycopy(this.mRingLux, this.mStart, values, 0, this.mCapacity - this.mStart);
                java.lang.System.arraycopy(this.mRingLux, 0, values, this.mCapacity - this.mStart, this.mEnd);
            }
            return values;
        }

        public long getTime(int index) {
            return this.mRingTime[offsetOf(index)];
        }

        public long[] getAllTimestamps() {
            long[] values = new long[this.mCount];
            if (this.mCount == 0) {
                return values;
            }
            if (this.mStart < this.mEnd) {
                java.lang.System.arraycopy(this.mRingTime, this.mStart, values, 0, this.mCount);
            } else {
                java.lang.System.arraycopy(this.mRingTime, this.mStart, values, 0, this.mCapacity - this.mStart);
                java.lang.System.arraycopy(this.mRingTime, 0, values, this.mCapacity - this.mStart, this.mEnd);
            }
            return values;
        }

        public void push(long time, float lux) {
            int next = this.mEnd;
            if (this.mCount == this.mCapacity) {
                int newSize = this.mCapacity * 2;
                float[] newRingLux = new float[newSize];
                long[] newRingTime = new long[newSize];
                int length = this.mCapacity - this.mStart;
                java.lang.System.arraycopy(this.mRingLux, this.mStart, newRingLux, 0, length);
                java.lang.System.arraycopy(this.mRingTime, this.mStart, newRingTime, 0, length);
                if (this.mStart != 0) {
                    java.lang.System.arraycopy(this.mRingLux, 0, newRingLux, length, this.mStart);
                    java.lang.System.arraycopy(this.mRingTime, 0, newRingTime, length, this.mStart);
                }
                this.mRingLux = newRingLux;
                this.mRingTime = newRingTime;
                next = this.mCapacity;
                this.mCapacity = newSize;
                this.mStart = 0;
            }
            this.mRingTime[next] = time;
            this.mRingLux[next] = lux;
            this.mEnd = next + 1;
            if (this.mEnd == this.mCapacity) {
                this.mEnd = 0;
            }
            this.mCount++;
        }

        public void prune(long horizon) {
            if (this.mCount == 0) {
                return;
            }
            while (this.mCount > 1) {
                int next = this.mStart + 1;
                if (next >= this.mCapacity) {
                    next -= this.mCapacity;
                }
                if (this.mRingTime[next] > horizon) {
                    break;
                }
                this.mStart = next;
                this.mCount--;
            }
            if (this.mRingTime[this.mStart] < horizon) {
                this.mRingTime[this.mStart] = horizon;
            }
        }

        public int size() {
            return this.mCount;
        }

        public void clear() {
            this.mStart = 0;
            this.mEnd = 0;
            this.mCount = 0;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder buf = new java.lang.StringBuilder();
            buf.append('[');
            for (int i = 0; i < this.mCount; i++) {
                long next = i + 1 < this.mCount ? getTime(i + 1) : this.mClock.getSensorEventScaleTime();
                if (i != 0) {
                    buf.append(", ");
                }
                buf.append(getLux(i));
                buf.append(" / ");
                buf.append(next - getTime(i));
                buf.append("ms");
            }
            buf.append(']');
            return buf.toString();
        }

        private int offsetOf(int index) {
            if (index >= this.mCount || index < 0) {
                throw new java.lang.ArrayIndexOutOfBoundsException(index);
            }
            int index2 = index + this.mStart;
            if (index2 >= this.mCapacity) {
                return index2 - this.mCapacity;
            }
            return index2;
        }
    }

    private static class RealClock implements com.android.server.display.AutomaticBrightnessController.Clock {
        private final boolean mOffloadControlsDozeBrightness;

        RealClock(boolean offloadControlsDozeBrightness) {
            this.mOffloadControlsDozeBrightness = offloadControlsDozeBrightness;
        }

        @Override // com.android.server.display.AutomaticBrightnessController.Clock
        public long uptimeMillis() {
            return android.os.SystemClock.uptimeMillis();
        }

        @Override // com.android.server.display.AutomaticBrightnessController.Clock
        public long getSensorEventScaleTime() {
            return this.mOffloadControlsDozeBrightness ? android.os.SystemClock.elapsedRealtime() : uptimeMillis();
        }
    }

    public static class Injector {
        public android.os.Handler getBackgroundThreadHandler() {
            return com.android.internal.os.BackgroundThread.getHandler();
        }

        com.android.server.display.AutomaticBrightnessController.Clock createClock(boolean offloadControlsDozeBrightness) {
            return new com.android.server.display.AutomaticBrightnessController.RealClock(offloadControlsDozeBrightness);
        }
    }
}
