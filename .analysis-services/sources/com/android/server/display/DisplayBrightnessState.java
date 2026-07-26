package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class DisplayBrightnessState {
    public static final float CUSTOM_ANIMATION_RATE_NOT_SET = -1.0f;
    private final float mBrightness;
    private final int mBrightnessAdjustmentFlag;
    private final com.android.server.display.brightness.BrightnessEvent mBrightnessEvent;
    private final com.android.server.display.brightness.BrightnessReason mBrightnessReason;
    private final float mCustomAnimationRate;
    private final java.lang.String mDisplayBrightnessStrategyName;
    private final boolean mIsSlowChange;
    private final boolean mIsUserInitiatedChange;
    private final float mMaxBrightness;
    private final float mMinBrightness;
    private final float mSdrBrightness;
    private final boolean mShouldUpdateScreenBrightnessSetting;
    private final boolean mShouldUseAutoBrightness;

    private DisplayBrightnessState(com.android.server.display.DisplayBrightnessState.Builder builder) {
        this.mBrightness = builder.getBrightness();
        this.mSdrBrightness = builder.getSdrBrightness();
        this.mBrightnessReason = builder.getBrightnessReason();
        this.mDisplayBrightnessStrategyName = builder.getDisplayBrightnessStrategyName();
        this.mShouldUseAutoBrightness = builder.getShouldUseAutoBrightness();
        this.mIsSlowChange = builder.isSlowChange();
        this.mMaxBrightness = builder.getMaxBrightness();
        this.mMinBrightness = builder.getMinBrightness();
        this.mCustomAnimationRate = builder.getCustomAnimationRate();
        this.mShouldUpdateScreenBrightnessSetting = builder.shouldUpdateScreenBrightnessSetting();
        this.mBrightnessEvent = builder.getBrightnessEvent();
        this.mBrightnessAdjustmentFlag = builder.getBrightnessAdjustmentFlag();
        this.mIsUserInitiatedChange = builder.isUserInitiatedChange();
    }

    public float getBrightness() {
        return this.mBrightness;
    }

    public float getSdrBrightness() {
        return this.mSdrBrightness;
    }

    public com.android.server.display.brightness.BrightnessReason getBrightnessReason() {
        return this.mBrightnessReason;
    }

    public java.lang.String getDisplayBrightnessStrategyName() {
        return this.mDisplayBrightnessStrategyName;
    }

    public boolean getShouldUseAutoBrightness() {
        return this.mShouldUseAutoBrightness;
    }

    public boolean isSlowChange() {
        return this.mIsSlowChange;
    }

    public float getMaxBrightness() {
        return this.mMaxBrightness;
    }

    public float getMinBrightness() {
        return this.mMinBrightness;
    }

    public float getCustomAnimationRate() {
        return this.mCustomAnimationRate;
    }

    public boolean shouldUpdateScreenBrightnessSetting() {
        return this.mShouldUpdateScreenBrightnessSetting;
    }

    public com.android.server.display.brightness.BrightnessEvent getBrightnessEvent() {
        return this.mBrightnessEvent;
    }

    public int getBrightnessAdjustmentFlag() {
        return this.mBrightnessAdjustmentFlag;
    }

    public boolean isUserInitiatedChange() {
        return this.mIsUserInitiatedChange;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder("DisplayBrightnessState:");
        stringBuilder.append("\n    brightness:");
        stringBuilder.append(getBrightness());
        stringBuilder.append("\n    sdrBrightness:");
        stringBuilder.append(getSdrBrightness());
        stringBuilder.append("\n    brightnessReason:");
        stringBuilder.append(getBrightnessReason());
        stringBuilder.append("\n    shouldUseAutoBrightness:");
        stringBuilder.append(getShouldUseAutoBrightness());
        stringBuilder.append("\n    isSlowChange:").append(this.mIsSlowChange);
        stringBuilder.append("\n    maxBrightness:").append(this.mMaxBrightness);
        stringBuilder.append("\n    minBrightness:").append(this.mMinBrightness);
        stringBuilder.append("\n    customAnimationRate:").append(this.mCustomAnimationRate);
        stringBuilder.append("\n    shouldUpdateScreenBrightnessSetting:").append(this.mShouldUpdateScreenBrightnessSetting);
        stringBuilder.append("\n    mBrightnessEvent:").append(java.util.Objects.toString(this.mBrightnessEvent, "null"));
        stringBuilder.append("\n    mBrightnessAdjustmentFlag:").append(this.mBrightnessAdjustmentFlag);
        stringBuilder.append("\n    mIsUserInitiatedChange:").append(this.mIsUserInitiatedChange);
        return stringBuilder.toString();
    }

    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof com.android.server.display.DisplayBrightnessState)) {
            return false;
        }
        com.android.server.display.DisplayBrightnessState otherState = (com.android.server.display.DisplayBrightnessState) other;
        return this.mBrightness == otherState.getBrightness() && this.mSdrBrightness == otherState.getSdrBrightness() && this.mBrightnessReason.equals(otherState.getBrightnessReason()) && android.text.TextUtils.equals(this.mDisplayBrightnessStrategyName, otherState.getDisplayBrightnessStrategyName()) && this.mShouldUseAutoBrightness == otherState.getShouldUseAutoBrightness() && this.mIsSlowChange == otherState.isSlowChange() && this.mMaxBrightness == otherState.getMaxBrightness() && this.mMinBrightness == otherState.getMinBrightness() && this.mCustomAnimationRate == otherState.getCustomAnimationRate() && this.mShouldUpdateScreenBrightnessSetting == otherState.shouldUpdateScreenBrightnessSetting() && java.util.Objects.equals(this.mBrightnessEvent, otherState.getBrightnessEvent()) && this.mBrightnessAdjustmentFlag == otherState.getBrightnessAdjustmentFlag() && this.mIsUserInitiatedChange == otherState.isUserInitiatedChange();
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Float.valueOf(this.mBrightness), java.lang.Float.valueOf(this.mSdrBrightness), this.mBrightnessReason, java.lang.Boolean.valueOf(this.mShouldUseAutoBrightness), java.lang.Boolean.valueOf(this.mIsSlowChange), java.lang.Float.valueOf(this.mMaxBrightness), java.lang.Float.valueOf(this.mMinBrightness), java.lang.Float.valueOf(this.mCustomAnimationRate), java.lang.Boolean.valueOf(this.mShouldUpdateScreenBrightnessSetting), this.mBrightnessEvent, java.lang.Integer.valueOf(this.mBrightnessAdjustmentFlag), java.lang.Boolean.valueOf(this.mIsUserInitiatedChange));
    }

    public static com.android.server.display.DisplayBrightnessState.Builder builder() {
        return new com.android.server.display.DisplayBrightnessState.Builder();
    }

    public static class Builder {
        private float mBrightness;
        private com.android.server.display.brightness.BrightnessEvent mBrightnessEvent;
        private java.lang.String mDisplayBrightnessStrategyName;
        private boolean mIsSlowChange;
        private boolean mIsUserInitiatedChange;
        private float mMaxBrightness;
        private float mMinBrightness;
        private float mSdrBrightness;
        private boolean mShouldUpdateScreenBrightnessSetting;
        private boolean mShouldUseAutoBrightness;
        private com.android.server.display.brightness.BrightnessReason mBrightnessReason = new com.android.server.display.brightness.BrightnessReason();
        private float mCustomAnimationRate = -1.0f;
        public int mBrightnessAdjustmentFlag = 0;

        public static com.android.server.display.DisplayBrightnessState.Builder from(com.android.server.display.DisplayBrightnessState state) {
            com.android.server.display.DisplayBrightnessState.Builder builder = new com.android.server.display.DisplayBrightnessState.Builder();
            builder.setBrightness(state.getBrightness());
            builder.setSdrBrightness(state.getSdrBrightness());
            builder.setBrightnessReason(state.getBrightnessReason());
            builder.setDisplayBrightnessStrategyName(state.getDisplayBrightnessStrategyName());
            builder.setShouldUseAutoBrightness(state.getShouldUseAutoBrightness());
            builder.setIsSlowChange(state.isSlowChange());
            builder.setMaxBrightness(state.getMaxBrightness());
            builder.setMinBrightness(state.getMinBrightness());
            builder.setCustomAnimationRate(state.getCustomAnimationRate());
            builder.setShouldUpdateScreenBrightnessSetting(state.shouldUpdateScreenBrightnessSetting());
            builder.setBrightnessEvent(state.getBrightnessEvent());
            builder.setBrightnessAdjustmentFlag(state.getBrightnessAdjustmentFlag());
            builder.setIsUserInitiatedChange(state.isUserInitiatedChange());
            return builder;
        }

        public float getBrightness() {
            return this.mBrightness;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setBrightness(float brightness) {
            this.mBrightness = brightness;
            return this;
        }

        public float getSdrBrightness() {
            return this.mSdrBrightness;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setSdrBrightness(float sdrBrightness) {
            this.mSdrBrightness = sdrBrightness;
            return this;
        }

        public com.android.server.display.brightness.BrightnessReason getBrightnessReason() {
            return this.mBrightnessReason;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setBrightnessReason(com.android.server.display.brightness.BrightnessReason brightnessReason) {
            this.mBrightnessReason = brightnessReason;
            return this;
        }

        public java.lang.String getDisplayBrightnessStrategyName() {
            return this.mDisplayBrightnessStrategyName;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setDisplayBrightnessStrategyName(java.lang.String displayBrightnessStrategyName) {
            this.mDisplayBrightnessStrategyName = displayBrightnessStrategyName;
            return this;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setShouldUseAutoBrightness(boolean shouldUseAutoBrightness) {
            this.mShouldUseAutoBrightness = shouldUseAutoBrightness;
            return this;
        }

        public boolean getShouldUseAutoBrightness() {
            return this.mShouldUseAutoBrightness;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setIsSlowChange(boolean isSlowChange) {
            this.mIsSlowChange = isSlowChange;
            return this;
        }

        public boolean isSlowChange() {
            return this.mIsSlowChange;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setMaxBrightness(float maxBrightness) {
            this.mMaxBrightness = maxBrightness;
            return this;
        }

        public float getMaxBrightness() {
            return this.mMaxBrightness;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setMinBrightness(float minBrightness) {
            this.mMinBrightness = minBrightness;
            return this;
        }

        public float getMinBrightness() {
            return this.mMinBrightness;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setCustomAnimationRate(float animationRate) {
            this.mCustomAnimationRate = animationRate;
            return this;
        }

        public float getCustomAnimationRate() {
            return this.mCustomAnimationRate;
        }

        public boolean shouldUpdateScreenBrightnessSetting() {
            return this.mShouldUpdateScreenBrightnessSetting;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setShouldUpdateScreenBrightnessSetting(boolean shouldUpdateScreenBrightnessSetting) {
            this.mShouldUpdateScreenBrightnessSetting = shouldUpdateScreenBrightnessSetting;
            return this;
        }

        public com.android.server.display.DisplayBrightnessState build() {
            return new com.android.server.display.DisplayBrightnessState(this);
        }

        public com.android.server.display.brightness.BrightnessEvent getBrightnessEvent() {
            return this.mBrightnessEvent;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setBrightnessEvent(com.android.server.display.brightness.BrightnessEvent brightnessEvent) {
            this.mBrightnessEvent = brightnessEvent;
            return this;
        }

        public int getBrightnessAdjustmentFlag() {
            return this.mBrightnessAdjustmentFlag;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setBrightnessAdjustmentFlag(int brightnessAdjustmentFlag) {
            this.mBrightnessAdjustmentFlag = brightnessAdjustmentFlag;
            return this;
        }

        public boolean isUserInitiatedChange() {
            return this.mIsUserInitiatedChange;
        }

        public com.android.server.display.DisplayBrightnessState.Builder setIsUserInitiatedChange(boolean isUserInitiatedChange) {
            this.mIsUserInitiatedChange = isUserInitiatedChange;
            return this;
        }
    }
}
