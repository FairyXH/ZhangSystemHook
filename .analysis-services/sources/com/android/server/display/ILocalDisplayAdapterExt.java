package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface ILocalDisplayAdapterExt {
    public static final float DEFAULT_SCALE = 1.0f;

    default void init(android.content.Context context) {
    }

    default void updateDisplayModes(boolean isDefaultDisplay, long physicalDisplayId) {
    }

    default int findDisplayModeIdByPolicy(boolean isDefaultDisplay, int policy, int displayId, int baseModeId) {
        return baseModeId;
    }

    default float getPowerOnRealTimeBrightness(boolean isFirstDisplay, float def) {
        return def;
    }

    default boolean hasRemapDisable() {
        return false;
    }

    default void setPrimaryPhysicalDisplayId(long physicalDisplayId) {
    }

    default int getSecondaryLcdDensity() {
        return 0;
    }

    default boolean isLongTakeAodToOn(int currentState, int state, long physicalDisplayId) {
        return false;
    }

    default boolean getDebugBrightness() {
        return false;
    }

    default float brightnessToBacklight(long id, float brightness) {
        return 0.0f;
    }

    default float brightnessToNits(float brightness) {
        return 0.0f;
    }

    default float brightnessToNits(long id, float brightness) {
        return 0.0f;
    }

    default float brightnessToColor(long id, float brightness) {
        return 0.0f;
    }

    default float getBrightnessFromNit(long id, float nit) {
        return 0.0f;
    }

    default float getMaxBrightness(long id) {
        return 0.0f;
    }

    default float getTotalDisplayBrightness(long id) {
        return 0.0f;
    }

    default float getAodBrightness() {
        return 0.0f;
    }

    default void updateScreenBrightnessProvider(float newBrightness, int brightness, float nit, long physicalDisplayId, boolean isFirstDisplay, int sdrBrightness, int oldState, int state) {
    }

    default void updateDCLayerState(int value) {
    }

    default void requestDisplayState(boolean isFirstDisplay, int state) {
    }

    default void setDisplayPowerModeFinished(boolean isFirstDisplay, int state) {
    }

    default float getDefaultDisplayBrightness(long physicalDisplayId) {
        return 0.0f;
    }

    default void setStaticDisplayDensity(android.view.SurfaceControl.StaticDisplayInfo staticDisplayInfo, long physicalDisplayId) {
    }

    default void setDisplayInfoDpi(com.android.server.display.DisplayDeviceInfo info, long physicalDisplayId) {
    }

    default float getEnhanceDolbyOriginNit(long id, float hdrBrightness, float sdrBrightness) {
        return 0.0f;
    }

    default void setCurrentEdrEnhanceScale(float scale) {
    }

    default float getEnhanceDolbyScale(float enhanceNits, float originNits, long physicalDisplayId) {
        return 1.0f;
    }

    default boolean needCaculateScale(long physicalDisplayId) {
        return false;
    }

    default int getEdrType() {
        return 0;
    }

    default android.os.Handler getOPlusRefreshRateHandler(android.os.Handler defHandler) {
        return defHandler;
    }

    default void setSwitchingTrackerPowerEventLog(int state, boolean start) {
    }

    default boolean isAnimating(long id) {
        return false;
    }

    default void notifyBacklightAnimFinished(float scale) {
    }
}
