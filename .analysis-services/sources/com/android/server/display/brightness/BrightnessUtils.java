package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class BrightnessUtils {
    public static boolean isValidBrightnessValue(float brightness) {
        return !java.lang.Float.isNaN(brightness) && brightness >= 0.0f && brightness <= 1.0f;
    }

    public static boolean isValidBrightnessValue(float brightness, float screenBrightnessRangeMinimum, float screenBrightnessRangeMaximum) {
        return !java.lang.Float.isNaN(brightness) && com.android.internal.display.BrightnessSynchronizer.floatCompare(brightness, screenBrightnessRangeMinimum, false) && com.android.internal.display.BrightnessSynchronizer.floatCompare(brightness, screenBrightnessRangeMaximum, true);
    }

    public static float clampAbsoluteBrightness(float value) {
        return value;
    }

    public static float clampBrightnessAdjustment(float value) {
        return value;
    }

    public static com.android.server.display.DisplayBrightnessState constructDisplayBrightnessState(int brightnessChangeReason, float brightness, float sdrBrightness, java.lang.String displayBrightnessStrategyName) {
        return constructDisplayBrightnessState(brightnessChangeReason, brightness, sdrBrightness, displayBrightnessStrategyName, false);
    }

    public static com.android.server.display.DisplayBrightnessState constructDisplayBrightnessState(int brightnessChangeReason, float brightness, float sdrBrightness, java.lang.String displayBrightnessStrategyName, boolean slowChange) {
        com.android.server.display.brightness.BrightnessReason brightnessReason = new com.android.server.display.brightness.BrightnessReason();
        brightnessReason.setReason(brightnessChangeReason);
        return new com.android.server.display.DisplayBrightnessState.Builder().setBrightness(brightness).setSdrBrightness(sdrBrightness).setBrightnessReason(brightnessReason).setDisplayBrightnessStrategyName(displayBrightnessStrategyName).setIsSlowChange(slowChange).build();
    }
}
