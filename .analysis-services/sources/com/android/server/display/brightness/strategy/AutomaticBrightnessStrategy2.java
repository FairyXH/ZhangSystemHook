package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
@java.lang.Deprecated
public class AutomaticBrightnessStrategy2 {
    private boolean mAppliedTemporaryAutoBrightnessAdjustment;
    private boolean mAutoBrightnessAdjustmentChanged;
    private boolean mAutoBrightnessDisabledDueToDisplayOff;
    private com.android.server.display.AutomaticBrightnessController mAutomaticBrightnessController;
    private android.hardware.display.BrightnessConfiguration mBrightnessConfiguration;
    private final android.content.Context mContext;
    private final int mDisplayId;
    private int mAutoBrightnessAdjustmentReasonsFlags = 0;
    private boolean mShouldResetShortTermModel = false;
    private boolean mAppliedAutoBrightness = false;
    private boolean mUseAutoBrightness = false;
    private boolean mIsAutoBrightnessEnabled = false;
    private boolean mIsShortTermModelActive = false;
    private float mAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
    private float mPendingAutoBrightnessAdjustment = Float.NaN;
    private float mTemporaryAutoBrightnessAdjustment = Float.NaN;

    public AutomaticBrightnessStrategy2(android.content.Context context, int displayId) {
        this.mContext = context;
        this.mDisplayId = displayId;
    }

    public void setAutoBrightnessState(int targetDisplayState, boolean allowAutoBrightnessWhileDozingConfig, int brightnessReason, int policy, float lastUserSetScreenBrightness, boolean userSetBrightnessChanged) {
        int autoBrightnessState;
        boolean z = false;
        boolean autoBrightnessEnabledInDoze = allowAutoBrightnessWhileDozingConfig && android.view.Display.isDozeState(targetDisplayState);
        this.mIsAutoBrightnessEnabled = shouldUseAutoBrightness() && !((targetDisplayState != 2 && !autoBrightnessEnabledInDoze) || brightnessReason == 6 || this.mAutomaticBrightnessController == null);
        if (shouldUseAutoBrightness() && targetDisplayState != 2 && !autoBrightnessEnabledInDoze) {
            z = true;
        }
        this.mAutoBrightnessDisabledDueToDisplayOff = z;
        if (this.mIsAutoBrightnessEnabled && brightnessReason != 10) {
            autoBrightnessState = 1;
        } else if (this.mAutoBrightnessDisabledDueToDisplayOff) {
            autoBrightnessState = 3;
        } else {
            autoBrightnessState = 2;
        }
        accommodateUserBrightnessChanges(userSetBrightnessChanged, lastUserSetScreenBrightness, policy, targetDisplayState, this.mBrightnessConfiguration, autoBrightnessState);
    }

    public boolean isAutoBrightnessEnabled() {
        return this.mIsAutoBrightnessEnabled;
    }

    public boolean isAutoBrightnessDisabledDueToDisplayOff() {
        return this.mAutoBrightnessDisabledDueToDisplayOff;
    }

    public void setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration brightnessConfiguration, boolean shouldResetShortTermModel) {
        this.mBrightnessConfiguration = brightnessConfiguration;
        setShouldResetShortTermModel(shouldResetShortTermModel);
    }

    public boolean processPendingAutoBrightnessAdjustments() {
        this.mAutoBrightnessAdjustmentChanged = false;
        if (java.lang.Float.isNaN(this.mPendingAutoBrightnessAdjustment)) {
            return false;
        }
        if (this.mAutoBrightnessAdjustment == this.mPendingAutoBrightnessAdjustment) {
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            return false;
        }
        this.mAutoBrightnessAdjustment = this.mPendingAutoBrightnessAdjustment;
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        this.mAutoBrightnessAdjustmentChanged = true;
        return true;
    }

    public void setAutomaticBrightnessController(com.android.server.display.AutomaticBrightnessController automaticBrightnessController) {
        if (automaticBrightnessController == this.mAutomaticBrightnessController) {
            return;
        }
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.stop();
        }
        this.mAutomaticBrightnessController = automaticBrightnessController;
    }

    public boolean shouldUseAutoBrightness() {
        return this.mUseAutoBrightness;
    }

    public void setUseAutoBrightness(boolean useAutoBrightness) {
        this.mUseAutoBrightness = useAutoBrightness;
    }

    public boolean isShortTermModelActive() {
        return this.mIsShortTermModelActive;
    }

    public void updatePendingAutoBrightnessAdjustments() {
        float adj = android.provider.Settings.System.getFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", 0.0f, -2);
        this.mPendingAutoBrightnessAdjustment = java.lang.Float.isNaN(adj) ? Float.NaN : com.android.server.display.brightness.BrightnessUtils.clampBrightnessAdjustment(adj);
    }

    public void setTemporaryAutoBrightnessAdjustment(float temporaryAutoBrightnessAdjustment) {
        this.mTemporaryAutoBrightnessAdjustment = temporaryAutoBrightnessAdjustment;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println("AutomaticBrightnessStrategy:");
        writer.println("  mDisplayId=" + this.mDisplayId);
        writer.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
        writer.println("  mPendingAutoBrightnessAdjustment=" + this.mPendingAutoBrightnessAdjustment);
        writer.println("  mTemporaryAutoBrightnessAdjustment=" + this.mTemporaryAutoBrightnessAdjustment);
        writer.println("  mShouldResetShortTermModel=" + this.mShouldResetShortTermModel);
        writer.println("  mAppliedAutoBrightness=" + this.mAppliedAutoBrightness);
        writer.println("  mAutoBrightnessAdjustmentChanged=" + this.mAutoBrightnessAdjustmentChanged);
        writer.println("  mAppliedTemporaryAutoBrightnessAdjustment=" + this.mAppliedTemporaryAutoBrightnessAdjustment);
        writer.println("  mUseAutoBrightness=" + this.mUseAutoBrightness);
        writer.println("  mWasShortTermModelActive=" + this.mIsShortTermModelActive);
        writer.println("  mAutoBrightnessAdjustmentReasonsFlags=" + this.mAutoBrightnessAdjustmentReasonsFlags);
    }

    public boolean getAutoBrightnessAdjustmentChanged() {
        return this.mAutoBrightnessAdjustmentChanged;
    }

    public boolean isTemporaryAutoBrightnessAdjustmentApplied() {
        return this.mAppliedTemporaryAutoBrightnessAdjustment;
    }

    public float getAutomaticScreenBrightness(com.android.server.display.brightness.BrightnessEvent brightnessEvent) {
        float brightness;
        if (this.mAutomaticBrightnessController != null) {
            brightness = this.mAutomaticBrightnessController.getAutomaticScreenBrightness(brightnessEvent);
        } else {
            brightness = Float.NaN;
        }
        adjustAutomaticBrightnessStateIfValid(brightness);
        return brightness;
    }

    public int getAutoBrightnessAdjustmentReasonsFlags() {
        return this.mAutoBrightnessAdjustmentReasonsFlags;
    }

    public boolean hasAppliedAutoBrightness() {
        return this.mAppliedAutoBrightness;
    }

    void adjustAutomaticBrightnessStateIfValid(float brightnessState) {
        int i;
        float newAutoBrightnessAdjustment;
        if (isTemporaryAutoBrightnessAdjustmentApplied()) {
            i = 1;
        } else {
            i = 2;
        }
        this.mAutoBrightnessAdjustmentReasonsFlags = i;
        if (this.mAutomaticBrightnessController != null) {
            newAutoBrightnessAdjustment = this.mAutomaticBrightnessController.getAutomaticScreenBrightnessAdjustment();
        } else {
            newAutoBrightnessAdjustment = 0.0f;
        }
        if (!java.lang.Float.isNaN(newAutoBrightnessAdjustment) && this.mAutoBrightnessAdjustment != newAutoBrightnessAdjustment) {
            putAutoBrightnessAdjustmentSetting(newAutoBrightnessAdjustment);
        } else {
            this.mAutoBrightnessAdjustmentReasonsFlags = 0;
        }
    }

    void setShouldResetShortTermModel(boolean shouldResetShortTermModel) {
        this.mShouldResetShortTermModel = shouldResetShortTermModel;
    }

    boolean shouldResetShortTermModel() {
        return this.mShouldResetShortTermModel;
    }

    float getAutoBrightnessAdjustment() {
        return this.mAutoBrightnessAdjustment;
    }

    float getPendingAutoBrightnessAdjustment() {
        return this.mPendingAutoBrightnessAdjustment;
    }

    float getTemporaryAutoBrightnessAdjustment() {
        return this.mTemporaryAutoBrightnessAdjustment;
    }

    void putAutoBrightnessAdjustmentSetting(float adjustment) {
        if (this.mDisplayId == 0) {
            this.mAutoBrightnessAdjustment = adjustment;
            android.provider.Settings.System.putFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", adjustment, -2);
        }
    }

    public void setAutoBrightnessApplied(boolean autoBrightnessApplied) {
        this.mAppliedAutoBrightness = autoBrightnessApplied;
    }

    void accommodateUserBrightnessChanges(boolean userSetBrightnessChanged, float lastUserSetScreenBrightness, int policy, int displayState, android.hardware.display.BrightnessConfiguration brightnessConfiguration, int autoBrightnessState) {
        processPendingAutoBrightnessAdjustments();
        float autoBrightnessAdjustment = updateTemporaryAutoBrightnessAdjustments();
        this.mIsShortTermModelActive = false;
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.configure(autoBrightnessState, brightnessConfiguration, lastUserSetScreenBrightness, userSetBrightnessChanged, autoBrightnessAdjustment, this.mAutoBrightnessAdjustmentChanged, policy, displayState, this.mShouldResetShortTermModel);
            this.mShouldResetShortTermModel = false;
            this.mIsShortTermModelActive = this.mAutomaticBrightnessController.hasUserDataPoints();
        }
    }

    private float updateTemporaryAutoBrightnessAdjustments() {
        this.mAppliedTemporaryAutoBrightnessAdjustment = !java.lang.Float.isNaN(this.mTemporaryAutoBrightnessAdjustment);
        return this.mAppliedTemporaryAutoBrightnessAdjustment ? this.mTemporaryAutoBrightnessAdjustment : this.mAutoBrightnessAdjustment;
    }

    private float getAutoBrightnessAdjustmentSetting() {
        float adj = android.provider.Settings.System.getFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", 0.0f, -2);
        if (java.lang.Float.isNaN(adj)) {
            return 0.0f;
        }
        return com.android.server.display.brightness.BrightnessUtils.clampBrightnessAdjustment(adj);
    }
}
