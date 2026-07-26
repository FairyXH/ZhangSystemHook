package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class BrightnessEvent {
    public static final int FLAG_DOZE_SCALE = 4;
    public static final int FLAG_INVALID_LUX = 2;
    public static final int FLAG_LOW_POWER_MODE = 32;
    public static final int FLAG_RBC = 1;
    public static final int FLAG_USER_SET = 8;
    private static final java.text.SimpleDateFormat FORMAT = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private int mAdjustmentFlags;
    private int mAutoBrightnessMode;
    private boolean mAutomaticBrightnessEnabled;
    private float mBrightness;
    private java.lang.String mDisplayBrightnessStrategyName;
    private int mDisplayId;
    private int mDisplayPolicy;
    private int mDisplayState;
    private int mFlags;
    private float mHbmMax;
    private int mHbmMode;
    private float mInitialBrightness;
    private float mLux;
    private java.lang.String mPhysicalDisplayId;
    private float mPowerFactor;
    private float mPreThresholdBrightness;
    private float mPreThresholdLux;
    private int mRbcStrength;
    private com.android.server.display.brightness.BrightnessReason mReason = new com.android.server.display.brightness.BrightnessReason();
    private float mRecommendedBrightness;
    private float mThermalMax;
    private long mTime;
    private boolean mWasShortTermModelActive;

    public BrightnessEvent(com.android.server.display.brightness.BrightnessEvent that) {
        copyFrom(that);
    }

    public BrightnessEvent(int displayId) {
        this.mDisplayId = displayId;
        reset();
    }

    public void copyFrom(com.android.server.display.brightness.BrightnessEvent that) {
        this.mReason.set(that.getReason());
        this.mDisplayId = that.getDisplayId();
        this.mPhysicalDisplayId = that.getPhysicalDisplayId();
        this.mDisplayState = that.mDisplayState;
        this.mDisplayPolicy = that.mDisplayPolicy;
        this.mTime = that.getTime();
        this.mLux = that.getLux();
        this.mPreThresholdLux = that.getPreThresholdLux();
        this.mInitialBrightness = that.getInitialBrightness();
        this.mBrightness = that.getBrightness();
        this.mRecommendedBrightness = that.getRecommendedBrightness();
        this.mPreThresholdBrightness = that.getPreThresholdBrightness();
        this.mHbmMode = that.getHbmMode();
        this.mHbmMax = that.getHbmMax();
        this.mRbcStrength = that.getRbcStrength();
        this.mThermalMax = that.getThermalMax();
        this.mPowerFactor = that.getPowerFactor();
        this.mWasShortTermModelActive = that.wasShortTermModelActive();
        this.mFlags = that.getFlags();
        this.mAdjustmentFlags = that.getAdjustmentFlags();
        this.mAutomaticBrightnessEnabled = that.isAutomaticBrightnessEnabled();
        this.mDisplayBrightnessStrategyName = that.getDisplayBrightnessStrategyName();
        this.mAutoBrightnessMode = that.mAutoBrightnessMode;
    }

    public void reset() {
        this.mReason = new com.android.server.display.brightness.BrightnessReason();
        this.mTime = android.os.SystemClock.uptimeMillis();
        this.mPhysicalDisplayId = "";
        this.mDisplayState = 0;
        this.mDisplayPolicy = 0;
        this.mLux = 0.0f;
        this.mPreThresholdLux = 0.0f;
        this.mInitialBrightness = Float.NaN;
        this.mBrightness = Float.NaN;
        this.mRecommendedBrightness = Float.NaN;
        this.mPreThresholdBrightness = Float.NaN;
        this.mHbmMode = 0;
        this.mHbmMax = 1.0f;
        this.mRbcStrength = 0;
        this.mThermalMax = 1.0f;
        this.mPowerFactor = 1.0f;
        this.mWasShortTermModelActive = false;
        this.mFlags = 0;
        this.mAdjustmentFlags = 0;
        this.mAutomaticBrightnessEnabled = true;
        this.mDisplayBrightnessStrategyName = "";
        this.mAutoBrightnessMode = 0;
    }

    public boolean equalsMainData(com.android.server.display.brightness.BrightnessEvent that) {
        return this.mReason.equals(that.mReason) && this.mDisplayId == that.mDisplayId && this.mPhysicalDisplayId.equals(that.mPhysicalDisplayId) && this.mDisplayState == that.mDisplayState && this.mDisplayPolicy == that.mDisplayPolicy && java.lang.Float.floatToRawIntBits(this.mLux) == java.lang.Float.floatToRawIntBits(that.mLux) && java.lang.Float.floatToRawIntBits(this.mPreThresholdLux) == java.lang.Float.floatToRawIntBits(that.mPreThresholdLux) && java.lang.Float.floatToRawIntBits(this.mBrightness) == java.lang.Float.floatToRawIntBits(that.mBrightness) && java.lang.Float.floatToRawIntBits(this.mRecommendedBrightness) == java.lang.Float.floatToRawIntBits(that.mRecommendedBrightness) && java.lang.Float.floatToRawIntBits(this.mPreThresholdBrightness) == java.lang.Float.floatToRawIntBits(that.mPreThresholdBrightness) && this.mHbmMode == that.mHbmMode && java.lang.Float.floatToRawIntBits(this.mHbmMax) == java.lang.Float.floatToRawIntBits(that.mHbmMax) && this.mRbcStrength == that.mRbcStrength && java.lang.Float.floatToRawIntBits(this.mThermalMax) == java.lang.Float.floatToRawIntBits(that.mThermalMax) && java.lang.Float.floatToRawIntBits(this.mPowerFactor) == java.lang.Float.floatToRawIntBits(that.mPowerFactor) && this.mWasShortTermModelActive == that.mWasShortTermModelActive && this.mFlags == that.mFlags && this.mAdjustmentFlags == that.mAdjustmentFlags && this.mAutomaticBrightnessEnabled == that.mAutomaticBrightnessEnabled && this.mDisplayBrightnessStrategyName.equals(that.mDisplayBrightnessStrategyName) && this.mAutoBrightnessMode == that.mAutoBrightnessMode;
    }

    public java.lang.String toString(boolean includeTime) {
        return (includeTime ? FORMAT.format(new java.util.Date(this.mTime)) + " - " : "") + "BrightnessEvent: disp=" + this.mDisplayId + ", physDisp=" + this.mPhysicalDisplayId + ", displayState=" + android.view.Display.stateToString(this.mDisplayState) + ", displayPolicy=" + android.hardware.display.DisplayManagerInternal.DisplayPowerRequest.policyToString(this.mDisplayPolicy) + ", brt=" + this.mBrightness + ((this.mFlags & 8) != 0 ? "(user_set)" : "") + ", initBrt=" + this.mInitialBrightness + ", rcmdBrt=" + this.mRecommendedBrightness + ", preBrt=" + this.mPreThresholdBrightness + ", lux=" + this.mLux + ", preLux=" + this.mPreThresholdLux + ", hbmMax=" + this.mHbmMax + ", hbmMode=" + android.hardware.display.BrightnessInfo.hbmToString(this.mHbmMode) + ", rbcStrength=" + this.mRbcStrength + ", thrmMax=" + this.mThermalMax + ", powerFactor=" + this.mPowerFactor + ", wasShortTermModelActive=" + this.mWasShortTermModelActive + ", flags=" + flagsToString() + ", reason=" + this.mReason.toString(this.mAdjustmentFlags) + ", autoBrightness=" + this.mAutomaticBrightnessEnabled + ", strategy=" + this.mDisplayBrightnessStrategyName + ", autoBrightnessMode=" + com.android.server.display.config.DisplayBrightnessMappingConfig.autoBrightnessModeToString(this.mAutoBrightnessMode);
    }

    public java.lang.String toString() {
        return toString(true);
    }

    public com.android.server.display.brightness.BrightnessReason getReason() {
        return this.mReason;
    }

    public void setReason(com.android.server.display.brightness.BrightnessReason reason) {
        this.mReason = reason;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public void setDisplayId(int displayId) {
        this.mDisplayId = displayId;
    }

    public java.lang.String getPhysicalDisplayId() {
        return this.mPhysicalDisplayId;
    }

    public void setPhysicalDisplayId(java.lang.String mPhysicalDisplayId) {
        this.mPhysicalDisplayId = mPhysicalDisplayId;
    }

    public void setDisplayState(int state) {
        this.mDisplayState = state;
    }

    public void setDisplayPolicy(int policy) {
        this.mDisplayPolicy = policy;
    }

    public float getLux() {
        return this.mLux;
    }

    public void setLux(float lux) {
        this.mLux = lux;
    }

    public float getPreThresholdLux() {
        return this.mPreThresholdLux;
    }

    public void setPreThresholdLux(float preThresholdLux) {
        this.mPreThresholdLux = preThresholdLux;
    }

    public float getInitialBrightness() {
        return this.mInitialBrightness;
    }

    public void setInitialBrightness(float mInitialBrightness) {
        this.mInitialBrightness = mInitialBrightness;
    }

    public float getBrightness() {
        return this.mBrightness;
    }

    public void setBrightness(float brightness) {
        this.mBrightness = brightness;
    }

    public float getRecommendedBrightness() {
        return this.mRecommendedBrightness;
    }

    public void setRecommendedBrightness(float recommendedBrightness) {
        this.mRecommendedBrightness = recommendedBrightness;
    }

    public float getPreThresholdBrightness() {
        return this.mPreThresholdBrightness;
    }

    public void setPreThresholdBrightness(float preThresholdBrightness) {
        this.mPreThresholdBrightness = preThresholdBrightness;
    }

    public int getHbmMode() {
        return this.mHbmMode;
    }

    public void setHbmMode(int hbmMode) {
        this.mHbmMode = hbmMode;
    }

    public float getHbmMax() {
        return this.mHbmMax;
    }

    public void setHbmMax(float hbmMax) {
        this.mHbmMax = hbmMax;
    }

    public int getRbcStrength() {
        return this.mRbcStrength;
    }

    public void setRbcStrength(int mRbcStrength) {
        this.mRbcStrength = mRbcStrength;
    }

    public boolean isRbcEnabled() {
        return (this.mFlags & 1) != 0;
    }

    public float getThermalMax() {
        return this.mThermalMax;
    }

    public void setThermalMax(float thermalMax) {
        this.mThermalMax = thermalMax;
    }

    public float getPowerFactor() {
        return this.mPowerFactor;
    }

    public void setPowerFactor(float mPowerFactor) {
        this.mPowerFactor = mPowerFactor;
    }

    public boolean isLowPowerModeSet() {
        return (this.mFlags & 32) != 0;
    }

    public boolean setWasShortTermModelActive(boolean wasShortTermModelActive) {
        this.mWasShortTermModelActive = wasShortTermModelActive;
        return wasShortTermModelActive;
    }

    public boolean wasShortTermModelActive() {
        return this.mWasShortTermModelActive;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public void setFlags(int flags) {
        this.mFlags = flags;
    }

    public int getAdjustmentFlags() {
        return this.mAdjustmentFlags;
    }

    public void setAdjustmentFlags(int adjustmentFlags) {
        this.mAdjustmentFlags = adjustmentFlags;
    }

    public boolean isAutomaticBrightnessEnabled() {
        return this.mAutomaticBrightnessEnabled;
    }

    public void setDisplayBrightnessStrategyName(java.lang.String displayBrightnessStrategyName) {
        this.mDisplayBrightnessStrategyName = displayBrightnessStrategyName;
    }

    public java.lang.String getDisplayBrightnessStrategyName() {
        return this.mDisplayBrightnessStrategyName;
    }

    public void setAutomaticBrightnessEnabled(boolean mAutomaticBrightnessEnabled) {
        this.mAutomaticBrightnessEnabled = mAutomaticBrightnessEnabled;
    }

    public int getAutoBrightnessMode() {
        return this.mAutoBrightnessMode;
    }

    public void setAutoBrightnessMode(int mode) {
        this.mAutoBrightnessMode = mode;
    }

    public java.lang.String flagsToString() {
        return ((this.mFlags & 8) != 0 ? "user_set " : "") + ((this.mFlags & 1) != 0 ? "rbc " : "") + ((this.mFlags & 2) != 0 ? "invalid_lux " : "") + ((this.mFlags & 4) != 0 ? "doze_scale " : "") + ((this.mFlags & 32) != 0 ? "low_power_mode " : "");
    }
}
