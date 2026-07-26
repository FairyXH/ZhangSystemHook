package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusFeatureBrightnessBarController extends android.common.IOplusCommonFeature {
    public static final float BAD_VALUE = -1.0f;
    public static final com.android.server.display.IOplusFeatureBrightnessBarController DEFAULT = new com.android.server.display.IOplusFeatureBrightnessBarController() { // from class: com.android.server.display.IOplusFeatureBrightnessBarController.1
    };
    public static final java.lang.String NAME = "IOplusFeatureBrightnessBarController";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusFeatureBrightnessBarController;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default float getBrightnessBarScale() {
        return 1.0f;
    }

    default float getUpdateBrightnessState(float brightness) {
        return brightness;
    }

    default int getOriginalBrightnessState(int brightness, boolean isDataBase) {
        return brightness;
    }

    default void setLux(float lux) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default void setReason(int reason) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default void onScreenEvent(boolean isOn) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default void brightnessModeOnChange(int Mode) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default void setAnimating(boolean isAnimating, boolean isPrimaryAnimator) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default java.lang.String getFeatureBrightnessBarControllerState(float brightness) {
        return "";
    }

    default void setCameraMode(boolean isCameraMode) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default void setFeatureOn(boolean isFeatureOn) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default boolean getFeatureSwitch() {
        android.util.Slog.w(NAME, "subclass not yet constructed");
        return false;
    }

    default void setSmartWorkHandler(android.os.Handler handler) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default boolean getJustNowToSwitchState() {
        android.util.Slog.w(NAME, "subclass not yet constructed");
        return false;
    }

    default void setJustNowToSwitchState(boolean state) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
    }

    default int getRateByDuration(float targetBrightness, float currntBrightness, int during, int defaultRate) {
        android.util.Slog.w(NAME, "subclass not yet constructed");
        return defaultRate;
    }
}
