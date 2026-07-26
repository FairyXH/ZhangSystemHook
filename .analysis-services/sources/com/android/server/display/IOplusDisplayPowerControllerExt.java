package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusDisplayPowerControllerExt {
    public static final int ADJUSTMENT_GALLERY_IN = 16385;
    public static final int ADJUSTMENT_GALLERY_OUT = 32769;
    public static final int MAX_BRIGHTNESS = 8191;

    default void dismissEglContext(com.android.server.display.ColorFade colorFade, int displayid) {
    }

    default void init(android.content.Context context, int displayId) {
    }

    default void stop(boolean isPrimaryDisplay) {
    }

    default boolean applyOplusProximitySensorLocked(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest powerRequest, int proximity, boolean proximitySensorEnabled, boolean waitingForNegativeProximity, boolean screenOffBecauseOfProximity, int state, boolean hasProximitySensor, int displayId) {
        return false;
    }

    default void onProximityDebounceTimeArrived(int displayId, int proximity) {
    }

    default boolean getUseProximityForceSuspendState(int displayId) {
        return false;
    }

    default boolean isUseProximityForceSuspendStateChanged(int displayId) {
        return false;
    }

    default boolean interceptProximityEvent() {
        return false;
    }

    default boolean registerPSensor(android.hardware.SensorManager sensorManager, android.hardware.SensorEventListener listener, int samplingPeriodUs, android.os.Handler handler, android.hardware.Sensor sensor) {
        return false;
    }

    default boolean isSilentRebootFirstGoToSleep(int displayId) {
        return false;
    }

    default void setPowerState(com.android.server.display.DisplayPowerState powerState) {
    }

    default void onAnimationChanged(android.animation.Animator animation, int num) {
    }

    default boolean isBlockDisplayByBiometrics() {
        return false;
    }

    default boolean isBlockScreenOnByBiometrics() {
        return false;
    }

    default void unblockDisplayReady() {
    }

    default boolean sendMessageWhenScreenOnUnblocker(android.os.Handler mHandler, android.os.Message msg) {
        return false;
    }

    default void removeMessageWhenScreenOn(android.os.Handler mHandler, int msg) {
    }

    default void onUpdatePowerState(int state, int policy, int brightness) {
    }

    default void notifyBrightnessChange(float brightnessState) {
    }

    default void blockScreenOnByBiometrics(java.lang.String reason) {
    }

    default void unblockScreenOnByBiometrics(java.lang.String reason) {
    }

    default boolean hasBiometricsBlockedReason(java.lang.String reason) {
        return false;
    }

    default void removeFaceBlockReasonFromBlockReasonList() {
    }

    default void setUseProximityForceSuspend(boolean enable, int displayId) {
    }

    default int getScreenState() {
        return -1;
    }

    default void initParameters(android.os.Handler handler) {
    }

    default void setDCMode() {
    }

    default void setRmMode() {
    }

    default boolean isDCMode() {
        return false;
    }

    default void setGlobalHbmSellMode() {
    }

    default int getGlobalHbmSellMode() {
        return -1;
    }

    default void animateLongTakeStateChange(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, int oldState, int state, int displayId) {
    }

    default void handleDisplayChanged(boolean slowChange, boolean autoBrightnessEnabled, float brightnessFloat, int state, int displayId, int reason) {
    }

    default void onDisplayControllerHandler(android.os.Message msg, android.os.Handler handler) {
    }

    default float getMinimumScreenBrightnessSetting(float oriMinScreenBrightness) {
        return oriMinScreenBrightness;
    }

    default int getMaximumScreenBrightnessSetting() {
        return 0;
    }

    default float getOplusWindowMaxBrightness(float brightness) {
        return brightness;
    }

    default boolean isSpecialAdj(float value) {
        return false;
    }

    default void setScreenStateExt(boolean isPrimary, int state, com.android.server.display.DisplayPowerState powerState, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest powerrequest) {
    }

    default void setQuickDarkToBrightStatus(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest powerrequest) {
    }

    default float caculateautoBrightnessAdjustment(float autoBrightnessAdjustment) {
        return autoBrightnessAdjustment;
    }

    default boolean notifyBrightnessSetting(int brightness, boolean appliedTemporaryBrightness, boolean appliedTemporaryAutoBrightnessAdjustment, int currentScreenBrightnessSetting, boolean slowChange) {
        return slowChange;
    }

    default int applydimmingbrightness(int brightness) {
        return brightness;
    }

    default int putBrightnessTodatabase(int target) {
        return target;
    }

    default void setDuration(int duration) {
    }

    default void setAnimating(boolean animating, boolean isPrimaryAnimator) {
    }

    default float handleScreenBrightnessSettingChange(float brightness) {
        return brightness;
    }

    default float handleSetTemporaryBrightnessMessage(float brightness, java.lang.String msg, int displayId) {
        return brightness;
    }

    default void setLoggingEnabled(boolean debug) {
    }

    default void setReason(int reason) {
    }

    default void handlePwkMonitorForTheia(int state, boolean isOff) {
    }

    default void cancelPwkBecauseProximity() {
    }

    default void registerEdrListener(android.os.IBinder displayToken) {
    }

    default float getBrightnessByNit(float nit) {
        return -1.0f;
    }

    default float getNitByBrightness(float brightness) {
        return -1.0f;
    }

    default void updateBrightnessAnimationStatus(com.android.server.display.DisplayPowerState powerState, int powerRqstPolicy, com.android.server.display.LogicalDisplay display, int displayId) {
    }

    default float getLowPowerModeBtnExp(float brightnessState, float factor, boolean isPriamryDisplay, int currentBatteryLevel) {
        return brightnessState * factor;
    }

    default void pokeDynamicVsyncAnimation(int durationInMs, java.lang.String detail) {
    }

    default boolean isBlockedBySideFingerprint() {
        return false;
    }

    default void setSpecBrightness(int gear, java.lang.String reason, int rate, int index) {
    }

    default android.os.IBinder getPhysicalDisplayToken(long physicalDisplayId) {
        return null;
    }

    default void setTemporaryAutoBrightnessAdjustment(float adjustment) {
    }

    default com.android.server.display.IColorAutomaticBrightnessController initAutomaticBrightnessController(com.android.server.display.AutomaticBrightnessController.Callbacks callbacks, android.os.Looper looper, android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, com.android.server.display.BrightnessMappingStrategy mapper, float dozeScaleFactor, int lightSensorRate, long darkeningLightDebounce) {
        return null;
    }

    default void setOplusDisplayPowerControllerCallback(android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks callback2) {
    }

    default void setDisplayPowerControlHandler(android.os.Handler handler) {
    }

    default void setDisplayPowerController(com.android.server.display.DisplayPowerController controller) {
    }

    default void dump(java.io.PrintWriter pw) {
    }

    default void updateScreenBrightnessOverride(boolean appliedScreenBrightnessOverride) {
    }

    default void handleBrightnessTotalRateType(int type) {
    }

    default void updateBrightnessTotalRateType(int type) {
    }

    default void setDimRateType() {
    }

    default void recoverOriginRateType() {
    }

    default boolean updateAutoBrightnessEnabled(com.android.server.display.LogicalDisplayMapper mapper, boolean autoBrightnessEnabled, int state, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest powerRequest) {
        return autoBrightnessEnabled;
    }

    default boolean hasRemapDisable() {
        return false;
    }

    default float getCurrentScreenBrightnessSetting(int displayId, float currScreenBrightnessSetting) {
        return currScreenBrightnessSetting;
    }

    default float getDynamicMaxBrightness(int displayId, float maxBrightness) {
        return maxBrightness;
    }

    default float getMaxDisplayBrightness() {
        return 0.0f;
    }

    default float getMinDisplayBrightness() {
        return 0.0f;
    }

    default float getTotalDisplayBrightness() {
        return 0.0f;
    }

    default float getScreenNormalMaxBrightness() {
        return 0.0f;
    }

    default boolean isSupportManualHBM() {
        return false;
    }

    default boolean isResetManualHBM() {
        return false;
    }

    default void setSunlightExitMode(boolean sunLightExitMode) {
    }

    default boolean onSwitchUser(int newUserId, float currentBrightnessSetting, boolean isDimming) {
        return false;
    }

    default void onChange(android.content.Context context, int displayId, boolean selfChange, android.net.Uri uri, int state) {
    }

    default float getAdjustmentSetting(android.content.Context context, float pendingAdj) {
        return 0.0f;
    }

    default void animateScreenBrightness(com.android.server.display.RampAnimator.DualRampAnimator<com.android.server.display.DisplayPowerState> rampAnimator, float target, float sdrTarget, float rate, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, com.android.server.display.DisplayPowerState powerState) {
    }

    default boolean isPrimaryDisplay(java.lang.String uniqueId) {
        return false;
    }

    default void setUniqueDisplayId(boolean isPrimary, java.lang.String uniqueId) {
    }

    default void configure(boolean enable, float adjustment, boolean dozing, boolean userInitiatedChange, int displayId, int state) {
    }

    default boolean isKeepRegistSensor(int displayId) {
        return false;
    }

    default int getAutomaticScreenBrightness() {
        return 0;
    }

    default int getAIBrightness() {
        return 0;
    }

    default void setByUser(boolean byUser) {
    }

    default void setWinOverride(boolean winOverride) {
    }

    default void setSavePowerMode(int savePowerMode) {
    }

    default void setLowPowerAnimatingState(boolean state) {
    }

    default void setHDRAnimatingState(boolean state) {
    }

    default int getLastBrightnessMode() {
        return 0;
    }

    default void updateFpsWhenDcChange(boolean enter) {
    }

    default void setPowerRequestPolicy(int policy) {
    }

    default android.hardware.display.BrightnessInfo getAccessibilityBrightnessInfo(float currentBrightness) {
        return new android.hardware.display.BrightnessInfo(currentBrightness, currentBrightness, 0.0f, 8191.0f, 0, 0.0f, 0);
    }

    default boolean getBrightnessInfoByAccessibility() {
        return false;
    }

    default boolean setBrightnessByAccessibility() {
        return false;
    }

    default void setBrightnessExt(float brightnessValue) {
    }

    default boolean useSoftwareAutoBrightnessConfigInOtherDisplay(int displayId) {
        return false;
    }

    default boolean postBrightnessChanged(float oldBrightnessValue, float newBrightnessValue) {
        return true;
    }

    default int getAdjustmentGalleryIn() {
        return ADJUSTMENT_GALLERY_IN;
    }

    default int getAdjustmentGalleryOut() {
        return ADJUSTMENT_GALLERY_OUT;
    }

    default boolean shouldIgnoreDoze(int state) {
        return false;
    }

    default boolean isFolding() {
        return false;
    }

    default boolean getResetTemporaryStrategyStatus() {
        return false;
    }

    default void setResetTemporaryStrategyStatus(boolean enable) {
    }

    default boolean isPowerSaveBrightnessReduceSupport() {
        return false;
    }

    default boolean isGalleryBrightnessEnhanceSupport() {
        return false;
    }

    default boolean isIgnoreProximity() {
        return false;
    }

    default boolean needScreenOffWhenDeviceStateClose() {
        return false;
    }

    default void resetNeedScreenOffWhenDeviceStateClose() {
    }

    default boolean isFactoryVersion() {
        return false;
    }

    default boolean onWakeUp() {
        return false;
    }

    default void setPowerOnUX(int oldState, int curState) {
    }

    default void clearPowerOnUX() {
    }

    default void setUxWhenWindowUnblock(boolean flag) {
    }

    default boolean getIgnoreReadyState() {
        return false;
    }

    default void setIgnoreReadyState(boolean ignore) {
    }

    default boolean isRemapDisplayDevice() {
        return false;
    }
}
