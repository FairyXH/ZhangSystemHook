package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class DisplayBrightnessController {
    com.android.server.display.IColorAutomaticBrightnessController mAutomaticBrightnessController;
    private final android.os.HandlerExecutor mBrightnessChangeExecutor;
    private final com.android.server.display.BrightnessSetting mBrightnessSetting;
    private com.android.server.display.BrightnessSetting.BrightnessSettingListener mBrightnessSettingListener;
    private android.content.Context mContext;
    private float mCurrentScreenBrightness;
    private com.android.server.display.brightness.strategy.DisplayBrightnessStrategy mDisplayBrightnessStrategy;
    private com.android.server.display.brightness.DisplayBrightnessStrategySelector mDisplayBrightnessStrategySelector;
    private final int mDisplayId;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private java.lang.Runnable mOnBrightnessChangeRunnable;
    private float mPendingScreenBrightness;
    private final boolean mPersistBrightnessNitsForDefaultDisplay;
    private final float mScreenBrightnessDefault;
    private float mScreenBrightnessNormalMaximum;
    private float mScreenBrightnessRangeMaximum;
    private float mScreenBrightnessRangeMinimum;
    private boolean mUserSetScreenBrightnessUpdated;
    private final java.lang.Object mLock = new java.lang.Object();
    private float mLastUserSetScreenBrightness = Float.NaN;

    public DisplayBrightnessController(android.content.Context context, com.android.server.display.brightness.DisplayBrightnessController.Injector injector, int displayId, float defaultScreenBrightness, com.android.server.display.BrightnessSetting brightnessSetting, java.lang.Runnable onBrightnessChangeRunnable, android.os.HandlerExecutor brightnessChangeExecutor, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        injector = injector == null ? new com.android.server.display.brightness.DisplayBrightnessController.Injector() : injector;
        this.mDisplayId = displayId;
        this.mDpcExt = dpcExt;
        this.mContext = context;
        this.mBrightnessSetting = brightnessSetting;
        this.mPendingScreenBrightness = Float.NaN;
        this.mScreenBrightnessDefault = com.android.server.display.brightness.BrightnessUtils.clampAbsoluteBrightness(defaultScreenBrightness);
        this.mCurrentScreenBrightness = getScreenBrightnessSetting();
        this.mOnBrightnessChangeRunnable = onBrightnessChangeRunnable;
        this.mDisplayBrightnessStrategySelector = injector.getDisplayBrightnessStrategySelector(context, displayId, flags, dpcExt);
        this.mBrightnessChangeExecutor = brightnessChangeExecutor;
        this.mPersistBrightnessNitsForDefaultDisplay = context.getResources().getBoolean(android.R.bool.config_multiuserVisibleBackgroundUsersOnDefaultDisplay);
        this.mScreenBrightnessRangeMinimum = dpcExt.getMinDisplayBrightness();
        this.mScreenBrightnessRangeMaximum = dpcExt.getTotalDisplayBrightness();
        this.mScreenBrightnessNormalMaximum = dpcExt.getMaxDisplayBrightness();
    }

    public com.android.server.display.DisplayBrightnessState updateBrightness(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int targetDisplayState, android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession) {
        com.android.server.display.DisplayBrightnessState state;
        if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(displayPowerRequest.dozeScreenBrightness)) {
            displayPowerRequest.dozeScreenBrightness *= this.mScreenBrightnessNormalMaximum;
            android.util.Slog.d("DisplayBrightnessController", "updateBrightness dozeScreenBrightness " + displayPowerRequest.dozeScreenBrightness);
        }
        synchronized (this.mLock) {
            this.mDisplayBrightnessStrategy = this.mDisplayBrightnessStrategySelector.selectStrategy(constructStrategySelectionRequest(displayPowerRequest, targetDisplayState, displayOffloadSession));
            state = this.mDisplayBrightnessStrategy.updateBrightness(constructStrategyExecutionRequest(displayPowerRequest));
        }
        if (state != null) {
            return addAutomaticBrightnessState(state);
        }
        return state;
    }

    public void setTemporaryBrightness(java.lang.Float temporaryBrightness) {
        synchronized (this.mLock) {
            setTemporaryBrightnessLocked(temporaryBrightness.floatValue());
        }
    }

    public void setBrightnessToFollow(float brightnessToFollow, boolean slowChange) {
        synchronized (this.mLock) {
            this.mDisplayBrightnessStrategySelector.getFollowerDisplayBrightnessStrategy().setBrightnessToFollow(brightnessToFollow, slowChange);
        }
    }

    public boolean setBrightnessFromOffload(float brightness) {
        synchronized (this.mLock) {
            if (this.mDisplayBrightnessStrategySelector.getOffloadBrightnessStrategy() != null && !com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mDisplayBrightnessStrategySelector.getOffloadBrightnessStrategy().getOffloadScreenBrightness(), brightness)) {
                this.mDisplayBrightnessStrategySelector.getOffloadBrightnessStrategy().setOffloadScreenBrightness(brightness);
                return true;
            }
            return false;
        }
    }

    public boolean isAllowAutoBrightnessWhileDozing() {
        boolean zIsAllowAutoBrightnessWhileDozing;
        synchronized (this.mLock) {
            zIsAllowAutoBrightnessWhileDozing = this.mDisplayBrightnessStrategySelector.isAllowAutoBrightnessWhileDozing();
        }
        return zIsAllowAutoBrightnessWhileDozing;
    }

    public boolean isAllowAutoBrightnessWhileDozingConfig() {
        boolean zIsAllowAutoBrightnessWhileDozingConfig;
        synchronized (this.mLock) {
            zIsAllowAutoBrightnessWhileDozingConfig = this.mDisplayBrightnessStrategySelector.isAllowAutoBrightnessWhileDozingConfig();
        }
        return zIsAllowAutoBrightnessWhileDozingConfig;
    }

    public void setAndNotifyCurrentScreenBrightness(float brightnessValue) {
        boolean hasBrightnessChanged;
        synchronized (this.mLock) {
            hasBrightnessChanged = brightnessValue != this.mCurrentScreenBrightness;
            setCurrentScreenBrightnessLocked(brightnessValue);
        }
        if (hasBrightnessChanged) {
            notifyCurrentScreenBrightness();
        }
    }

    public float getCurrentBrightness() {
        float f;
        synchronized (this.mLock) {
            f = this.mCurrentScreenBrightness;
        }
        return f;
    }

    public float getPendingScreenBrightness() {
        float f;
        synchronized (this.mLock) {
            f = this.mPendingScreenBrightness;
        }
        return f;
    }

    public void setPendingScreenBrightness(float brightnessValue) {
        float brightnessByDisplay = brightnessValue;
        if (this.mDisplayId > 0) {
            brightnessByDisplay = this.mDpcExt.getBrightnessByNit(this.mDpcExt.getNitByBrightness(brightnessValue));
        }
        synchronized (this.mLock) {
            this.mPendingScreenBrightness = brightnessByDisplay;
        }
    }

    public boolean getIsUserSetScreenBrightnessUpdated() {
        return this.mUserSetScreenBrightnessUpdated;
    }

    public void registerBrightnessSettingChangeListener(com.android.server.display.BrightnessSetting.BrightnessSettingListener brightnessSettingListener) {
        this.mBrightnessSettingListener = brightnessSettingListener;
        this.mBrightnessSetting.registerListener(this.mBrightnessSettingListener);
    }

    public float getLastUserSetScreenBrightness() {
        float f;
        synchronized (this.mLock) {
            f = this.mLastUserSetScreenBrightness;
        }
        return f;
    }

    public float getTemporaryBrightnessLocked() {
        return this.mDisplayBrightnessStrategySelector.getTemporaryDisplayBrightnessStrategy().getTemporaryScreenBrightness();
    }

    public float getScreenBrightnessSetting() {
        float brightness;
        float fClampAbsoluteBrightness;
        this.mBrightnessSetting.getBrightness();
        java.lang.String val = android.provider.Settings.System.getStringForUser(this.mContext.getContentResolver(), "screen_brightness", -2);
        try {
            brightness = val != null ? java.lang.Integer.parseInt(val) : this.mScreenBrightnessDefault;
        } catch (java.lang.NumberFormatException e) {
            brightness = this.mScreenBrightnessDefault;
        }
        synchronized (this.mLock) {
            if (java.lang.Float.isNaN(brightness)) {
                brightness = this.mScreenBrightnessDefault;
            }
            fClampAbsoluteBrightness = com.android.server.display.brightness.BrightnessUtils.clampAbsoluteBrightness(brightness);
        }
        return fClampAbsoluteBrightness;
    }

    public void setBrightness(float brightnessValue, float maxBrightness) {
        this.mBrightnessSetting.setBrightness(brightnessValue);
        if (this.mDisplayId == 0 && this.mPersistBrightnessNitsForDefaultDisplay) {
            float nits = convertToNits(brightnessValue);
            float currentlyStoredNits = this.mBrightnessSetting.getBrightnessNitsForDefaultDisplay();
            if (nits >= 0.0f && (brightnessValue != maxBrightness || currentlyStoredNits <= nits)) {
                this.mBrightnessSetting.setBrightnessNitsForDefaultDisplay(nits);
            }
        }
        this.mDpcExt.setBrightnessExt(brightnessValue);
    }

    public void setBrightness(float brightnessValue, int userSerial, float maxBrightness) {
        this.mBrightnessSetting.setUserSerial(userSerial);
        setBrightness(brightnessValue, maxBrightness);
    }

    public void updateScreenBrightnessSetting(float brightnessValue, float maxBrightness) {
        synchronized (this.mLock) {
            if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightnessValue, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum) && brightnessValue != this.mCurrentScreenBrightness) {
                setCurrentScreenBrightnessLocked(brightnessValue);
                notifyCurrentScreenBrightness();
                setBrightness(brightnessValue, maxBrightness);
            }
        }
    }

    public void setUpAutoBrightness(com.android.server.display.IColorAutomaticBrightnessController automaticBrightnessController, android.hardware.SensorManager sensorManager, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Handler handler, com.android.server.display.BrightnessMappingStrategy brightnessMappingStrategy, boolean isEnabled, int leadDisplayId) {
        setAutomaticBrightnessController(automaticBrightnessController);
        setUpAutoBrightnessFallbackStrategy(sensorManager, displayDeviceConfig, handler, brightnessMappingStrategy, isEnabled, leadDisplayId);
    }

    public com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy getAutomaticBrightnessStrategy() {
        return this.mDisplayBrightnessStrategySelector.getAutomaticBrightnessStrategy();
    }

    public float convertToNits(float brightness) {
        if (this.mAutomaticBrightnessController == null) {
            return -1.0f;
        }
        return this.mAutomaticBrightnessController.convertToNits(brightness);
    }

    public float convertToAdjustedNits(float brightness) {
        if (this.mAutomaticBrightnessController == null) {
            return -1.0f;
        }
        return this.mAutomaticBrightnessController.convertToAdjustedNits(brightness);
    }

    public float getBrightnessFromNits(float nits) {
        if (this.mAutomaticBrightnessController == null) {
            return Float.NaN;
        }
        return this.mAutomaticBrightnessController.getBrightnessFromNits(nits);
    }

    public void stop() {
        if (this.mBrightnessSetting != null) {
            this.mBrightnessSetting.unregisterListener(this.mBrightnessSettingListener);
        }
        com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy autoBrightnessFallbackStrategy = getAutoBrightnessFallbackStrategy();
        if (autoBrightnessFallbackStrategy != null) {
            autoBrightnessFallbackStrategy.stop();
        }
    }

    private com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy getAutoBrightnessFallbackStrategy() {
        com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy autoBrightnessFallbackStrategy;
        synchronized (this.mLock) {
            autoBrightnessFallbackStrategy = this.mDisplayBrightnessStrategySelector.getAutoBrightnessFallbackStrategy();
        }
        return autoBrightnessFallbackStrategy;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println();
        writer.println("DisplayBrightnessController:");
        writer.println("  mDisplayId=: " + this.mDisplayId);
        writer.println("  mScreenBrightnessDefault=" + this.mScreenBrightnessDefault);
        writer.println("  mPersistBrightnessNitsForDefaultDisplay=" + this.mPersistBrightnessNitsForDefaultDisplay);
        synchronized (this.mLock) {
            writer.println("  mPendingScreenBrightness=" + this.mPendingScreenBrightness);
            writer.println("  mCurrentScreenBrightness=" + this.mCurrentScreenBrightness);
            writer.println("  mLastUserSetScreenBrightness=" + this.mLastUserSetScreenBrightness);
            if (this.mDisplayBrightnessStrategy != null) {
                writer.println("  Last selected DisplayBrightnessStrategy= " + this.mDisplayBrightnessStrategy.getName());
            }
            this.mDisplayBrightnessStrategySelector.dump(new android.util.IndentingPrintWriter(writer, " "));
        }
    }

    boolean updateUserSetScreenBrightness() {
        this.mUserSetScreenBrightnessUpdated = false;
        synchronized (this.mLock) {
            if (!com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(this.mPendingScreenBrightness, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
                if (com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mCurrentScreenBrightness, getTemporaryBrightnessLocked())) {
                    setTemporaryBrightnessLocked(Float.NaN);
                }
                return false;
            }
            if (com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mCurrentScreenBrightness, this.mPendingScreenBrightness)) {
                this.mPendingScreenBrightness = Float.NaN;
                setTemporaryBrightnessLocked(Float.NaN);
                return false;
            }
            setCurrentScreenBrightnessLocked(this.mPendingScreenBrightness);
            this.mLastUserSetScreenBrightness = this.mPendingScreenBrightness;
            this.mPendingScreenBrightness = Float.NaN;
            setTemporaryBrightnessLocked(Float.NaN);
            notifyCurrentScreenBrightness();
            this.mUserSetScreenBrightnessUpdated = true;
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static class Injector {
        Injector() {
        }

        com.android.server.display.brightness.DisplayBrightnessStrategySelector getDisplayBrightnessStrategySelector(android.content.Context context, int displayId, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.brightness.DisplayBrightnessStrategySelector(context, null, displayId, flags, dpcExt);
        }
    }

    com.android.server.display.BrightnessSetting.BrightnessSettingListener getBrightnessSettingListener() {
        return this.mBrightnessSettingListener;
    }

    com.android.server.display.brightness.strategy.DisplayBrightnessStrategy getCurrentDisplayBrightnessStrategy() {
        com.android.server.display.brightness.strategy.DisplayBrightnessStrategy displayBrightnessStrategy;
        synchronized (this.mLock) {
            displayBrightnessStrategy = this.mDisplayBrightnessStrategy;
        }
        return displayBrightnessStrategy;
    }

    void setAutomaticBrightnessController(com.android.server.display.IColorAutomaticBrightnessController automaticBrightnessController) {
        this.mAutomaticBrightnessController = automaticBrightnessController;
        getAutomaticBrightnessStrategy().setAutomaticBrightnessController(automaticBrightnessController);
        loadNitBasedBrightnessSetting();
    }

    private void setUpAutoBrightnessFallbackStrategy(android.hardware.SensorManager sensorManager, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, android.os.Handler handler, com.android.server.display.BrightnessMappingStrategy brightnessMappingStrategy, boolean isEnabled, int leadDisplayId) {
        com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy autoBrightnessFallbackStrategy = getAutoBrightnessFallbackStrategy();
        if (autoBrightnessFallbackStrategy != null) {
            autoBrightnessFallbackStrategy.setupAutoBrightnessFallbackSensor(sensorManager, displayDeviceConfig, handler, brightnessMappingStrategy, isEnabled, leadDisplayId);
        }
    }

    private com.android.server.display.DisplayBrightnessState addAutomaticBrightnessState(com.android.server.display.DisplayBrightnessState state) {
        com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy autoStrat = getAutomaticBrightnessStrategy();
        com.android.server.display.DisplayBrightnessState.Builder builder = com.android.server.display.DisplayBrightnessState.Builder.from(state);
        builder.setShouldUseAutoBrightness(autoStrat != null && autoStrat.shouldUseAutoBrightness());
        return builder.build();
    }

    private void setTemporaryBrightnessLocked(float temporaryBrightness) {
        this.mDisplayBrightnessStrategySelector.getTemporaryDisplayBrightnessStrategy().setTemporaryScreenBrightness(temporaryBrightness);
    }

    private void setCurrentScreenBrightnessLocked(float brightnessValue) {
        if (brightnessValue != this.mCurrentScreenBrightness) {
            this.mCurrentScreenBrightness = brightnessValue;
        }
    }

    private void notifyCurrentScreenBrightness() {
        this.mBrightnessChangeExecutor.execute(this.mOnBrightnessChangeRunnable);
    }

    private void loadNitBasedBrightnessSetting() {
        float currentBrightnessSetting = Float.NaN;
        if (this.mDisplayId == 0 && this.mPersistBrightnessNitsForDefaultDisplay) {
            float brightnessNitsForDefaultDisplay = this.mBrightnessSetting.getBrightnessNitsForDefaultDisplay();
            if (brightnessNitsForDefaultDisplay >= 0.0f) {
                float brightnessForDefaultDisplay = getBrightnessFromNits(brightnessNitsForDefaultDisplay);
                if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightnessForDefaultDisplay)) {
                    this.mBrightnessSetting.setBrightnessNoNotify(brightnessForDefaultDisplay);
                    currentBrightnessSetting = brightnessForDefaultDisplay;
                }
            }
        }
        if (java.lang.Float.isNaN(currentBrightnessSetting)) {
            currentBrightnessSetting = getScreenBrightnessSetting();
        }
        synchronized (this.mLock) {
            this.mCurrentScreenBrightness = currentBrightnessSetting;
        }
    }

    private com.android.server.display.brightness.StrategySelectionRequest constructStrategySelectionRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int targetDisplayState, android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession) {
        float lastUserSetScreenBrightness;
        boolean userSetBrightnessChanged = updateUserSetScreenBrightness();
        synchronized (this.mLock) {
            lastUserSetScreenBrightness = this.mLastUserSetScreenBrightness;
        }
        return new com.android.server.display.brightness.StrategySelectionRequest(displayPowerRequest, targetDisplayState, lastUserSetScreenBrightness, userSetBrightnessChanged, displayOffloadSession);
    }

    private com.android.server.display.brightness.StrategyExecutionRequest constructStrategyExecutionRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
        float currentScreenBrightness = getCurrentBrightness();
        return new com.android.server.display.brightness.StrategyExecutionRequest(displayPowerRequest, currentScreenBrightness, this.mUserSetScreenBrightnessUpdated);
    }
}
