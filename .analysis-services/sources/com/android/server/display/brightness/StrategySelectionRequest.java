package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class StrategySelectionRequest {
    private android.hardware.display.DisplayManagerInternal.DisplayOffloadSession mDisplayOffloadSession;
    private android.hardware.display.DisplayManagerInternal.DisplayPowerRequest mDisplayPowerRequest;
    private float mLastUserSetScreenBrightness;
    private int mTargetDisplayState;
    private boolean mUserSetBrightnessChanged;

    public StrategySelectionRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int targetDisplayState, float lastUserSetScreenBrightness, boolean userSetBrightnessChanged, android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession) {
        this.mDisplayPowerRequest = displayPowerRequest;
        this.mTargetDisplayState = targetDisplayState;
        this.mLastUserSetScreenBrightness = lastUserSetScreenBrightness;
        this.mUserSetBrightnessChanged = userSetBrightnessChanged;
        this.mDisplayOffloadSession = displayOffloadSession;
    }

    public android.hardware.display.DisplayManagerInternal.DisplayPowerRequest getDisplayPowerRequest() {
        return this.mDisplayPowerRequest;
    }

    public int getTargetDisplayState() {
        return this.mTargetDisplayState;
    }

    public float getLastUserSetScreenBrightness() {
        return this.mLastUserSetScreenBrightness;
    }

    public boolean isUserSetBrightnessChanged() {
        return this.mUserSetBrightnessChanged;
    }

    public android.hardware.display.DisplayManagerInternal.DisplayOffloadSession getDisplayOffloadSession() {
        return this.mDisplayOffloadSession;
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof com.android.server.display.brightness.StrategySelectionRequest)) {
            return false;
        }
        com.android.server.display.brightness.StrategySelectionRequest other = (com.android.server.display.brightness.StrategySelectionRequest) obj;
        return java.util.Objects.equals(this.mDisplayPowerRequest, other.getDisplayPowerRequest()) && this.mTargetDisplayState == other.getTargetDisplayState() && this.mLastUserSetScreenBrightness == other.getLastUserSetScreenBrightness() && this.mUserSetBrightnessChanged == other.isUserSetBrightnessChanged() && this.mDisplayOffloadSession.equals(other.getDisplayOffloadSession());
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mDisplayPowerRequest, java.lang.Integer.valueOf(this.mTargetDisplayState), java.lang.Float.valueOf(this.mLastUserSetScreenBrightness), java.lang.Boolean.valueOf(this.mUserSetBrightnessChanged), this.mDisplayOffloadSession);
    }
}
