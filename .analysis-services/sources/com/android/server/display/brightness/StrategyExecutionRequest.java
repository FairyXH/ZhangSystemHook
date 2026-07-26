package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class StrategyExecutionRequest {
    private final float mCurrentScreenBrightness;
    private final android.hardware.display.DisplayManagerInternal.DisplayPowerRequest mDisplayPowerRequest;
    private boolean mUserSetBrightnessChanged;

    public StrategyExecutionRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, float currentScreenBrightness, boolean userSetBrightnessChanged) {
        this.mDisplayPowerRequest = displayPowerRequest;
        this.mCurrentScreenBrightness = currentScreenBrightness;
        this.mUserSetBrightnessChanged = userSetBrightnessChanged;
    }

    public android.hardware.display.DisplayManagerInternal.DisplayPowerRequest getDisplayPowerRequest() {
        return this.mDisplayPowerRequest;
    }

    public float getCurrentScreenBrightness() {
        return this.mCurrentScreenBrightness;
    }

    public boolean isUserSetBrightnessChanged() {
        return this.mUserSetBrightnessChanged;
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof com.android.server.display.brightness.StrategyExecutionRequest)) {
            return false;
        }
        com.android.server.display.brightness.StrategyExecutionRequest other = (com.android.server.display.brightness.StrategyExecutionRequest) obj;
        return java.util.Objects.equals(this.mDisplayPowerRequest, other.getDisplayPowerRequest()) && this.mCurrentScreenBrightness == other.getCurrentScreenBrightness() && this.mUserSetBrightnessChanged == other.isUserSetBrightnessChanged();
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mDisplayPowerRequest, java.lang.Float.valueOf(this.mCurrentScreenBrightness), java.lang.Boolean.valueOf(this.mUserSetBrightnessChanged));
    }
}
