package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class StrategySelectionNotifyRequest {
    private final boolean mAllowAutoBrightnessWhileDozingConfig;
    private android.hardware.display.DisplayManagerInternal.DisplayPowerRequest mDisplayPowerRequest;
    private final boolean mIsAutoBrightnessEnabled;
    private float mLastUserSetScreenBrightness;
    private final com.android.server.display.brightness.strategy.DisplayBrightnessStrategy mSelectedDisplayBrightnessStrategy;
    private int mTargetDisplayState;
    private boolean mUserSetBrightnessChanged;

    public StrategySelectionNotifyRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int targetDisplayState, com.android.server.display.brightness.strategy.DisplayBrightnessStrategy displayBrightnessStrategy, float lastUserSetScreenBrightness, boolean userSetBrightnessChanged, boolean allowAutoBrightnessWhileDozingConfig, boolean isAutoBrightnessEnabled) {
        this.mDisplayPowerRequest = displayPowerRequest;
        this.mTargetDisplayState = targetDisplayState;
        this.mSelectedDisplayBrightnessStrategy = displayBrightnessStrategy;
        this.mLastUserSetScreenBrightness = lastUserSetScreenBrightness;
        this.mUserSetBrightnessChanged = userSetBrightnessChanged;
        this.mAllowAutoBrightnessWhileDozingConfig = allowAutoBrightnessWhileDozingConfig;
        this.mIsAutoBrightnessEnabled = isAutoBrightnessEnabled;
    }

    public com.android.server.display.brightness.strategy.DisplayBrightnessStrategy getSelectedDisplayBrightnessStrategy() {
        return this.mSelectedDisplayBrightnessStrategy;
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof com.android.server.display.brightness.StrategySelectionNotifyRequest)) {
            return false;
        }
        com.android.server.display.brightness.StrategySelectionNotifyRequest other = (com.android.server.display.brightness.StrategySelectionNotifyRequest) obj;
        return other.getSelectedDisplayBrightnessStrategy() == getSelectedDisplayBrightnessStrategy() && java.util.Objects.equals(this.mDisplayPowerRequest, other.getDisplayPowerRequest()) && this.mTargetDisplayState == other.getTargetDisplayState() && this.mUserSetBrightnessChanged == other.isUserSetBrightnessChanged() && this.mLastUserSetScreenBrightness == other.getLastUserSetScreenBrightness() && this.mAllowAutoBrightnessWhileDozingConfig == other.isAllowAutoBrightnessWhileDozingConfig() && this.mIsAutoBrightnessEnabled == other.isAutoBrightnessEnabled();
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mSelectedDisplayBrightnessStrategy, this.mDisplayPowerRequest, java.lang.Integer.valueOf(this.mTargetDisplayState), java.lang.Boolean.valueOf(this.mUserSetBrightnessChanged), java.lang.Float.valueOf(this.mLastUserSetScreenBrightness), java.lang.Boolean.valueOf(this.mAllowAutoBrightnessWhileDozingConfig), java.lang.Boolean.valueOf(this.mIsAutoBrightnessEnabled));
    }

    public float getLastUserSetScreenBrightness() {
        return this.mLastUserSetScreenBrightness;
    }

    public boolean isUserSetBrightnessChanged() {
        return this.mUserSetBrightnessChanged;
    }

    public android.hardware.display.DisplayManagerInternal.DisplayPowerRequest getDisplayPowerRequest() {
        return this.mDisplayPowerRequest;
    }

    public int getTargetDisplayState() {
        return this.mTargetDisplayState;
    }

    public boolean isAllowAutoBrightnessWhileDozingConfig() {
        return this.mAllowAutoBrightnessWhileDozingConfig;
    }

    public boolean isAutoBrightnessEnabled() {
        return this.mIsAutoBrightnessEnabled;
    }

    public java.lang.String toString() {
        return "StrategySelectionNotifyRequest: mDisplayPowerRequest=" + this.mDisplayPowerRequest + " mTargetDisplayState=" + this.mTargetDisplayState + " mSelectedDisplayBrightnessStrategy=" + this.mSelectedDisplayBrightnessStrategy + " mLastUserSetScreenBrightness=" + this.mLastUserSetScreenBrightness + " mUserSetBrightnessChanged=" + this.mUserSetBrightnessChanged + " mAllowAutoBrightnessWhileDozingConfig=" + this.mAllowAutoBrightnessWhileDozingConfig + " mIsAutoBrightnessEnabled=" + this.mIsAutoBrightnessEnabled;
    }
}
