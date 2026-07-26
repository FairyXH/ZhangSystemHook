package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class AutomaticBrightnessStrategy extends com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2 implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    private boolean mAppliedAutoBrightness;
    private boolean mAppliedTemporaryAutoBrightnessAdjustment;
    private float mAutoBrightnessAdjustment;
    private boolean mAutoBrightnessAdjustmentChanged;
    private int mAutoBrightnessAdjustmentReasonsFlags;
    private boolean mAutoBrightnessDisabledDueToDisplayOff;
    private com.android.server.display.IColorAutomaticBrightnessController mAutomaticBrightnessController;
    private android.hardware.display.BrightnessConfiguration mBrightnessConfiguration;
    private final android.content.Context mContext;
    private final int mDisplayId;
    private com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy.Injector mInjector;
    private boolean mIsAutoBrightnessEnabled;
    private boolean mIsConfigured;
    private boolean mIsShortTermModelActive;
    private boolean mIsSlowChange;
    private float mPendingAutoBrightnessAdjustment;
    private boolean mShouldResetShortTermModel;
    private float mTemporaryAutoBrightnessAdjustment;
    private boolean mUseAutoBrightness;

    interface Injector {
        com.android.server.display.brightness.BrightnessEvent getBrightnessEvent(int i);
    }

    AutomaticBrightnessStrategy(android.content.Context context, int displayId, com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy.Injector injector, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        super(context, displayId);
        this.mAutoBrightnessAdjustmentReasonsFlags = 0;
        this.mShouldResetShortTermModel = false;
        this.mAppliedAutoBrightness = false;
        this.mUseAutoBrightness = false;
        this.mIsAutoBrightnessEnabled = false;
        this.mIsShortTermModelActive = false;
        this.mContext = context;
        this.mDisplayId = displayId;
        this.mAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        this.mDpcExt = dpcExt;
        this.mDisplayManagerFlags = displayManagerFlags;
        this.mInjector = injector == null ? new com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy.RealInjector() : injector;
    }

    public AutomaticBrightnessStrategy(android.content.Context context, int displayId, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this(context, displayId, null, displayManagerFlags, dpcExt);
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public void setAutoBrightnessState(int targetDisplayState, boolean allowAutoBrightnessWhileDozingConfig, int brightnessReason, int policy, float lastUserSetScreenBrightness, boolean userSetBrightnessChanged) {
        int autoBrightnessState;
        boolean z = false;
        switchMode(targetDisplayState, false);
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
        this.mIsConfigured = true;
    }

    public void setIsConfigured(boolean configure) {
        this.mIsConfigured = configure;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean isAutoBrightnessEnabled() {
        return this.mIsAutoBrightnessEnabled;
    }

    public boolean isAutoBrightnessValid() {
        float brightness;
        boolean isValid = false;
        if (isAutoBrightnessEnabled()) {
            if (this.mAutomaticBrightnessController != null) {
                brightness = this.mAutomaticBrightnessController.getAutomaticScreenBrightness();
            } else {
                brightness = Float.NaN;
            }
            if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(brightness) || brightness == -1.0f) {
                isValid = true;
            }
        }
        this.mIsSlowChange = hasAppliedAutoBrightness() && !getAutoBrightnessAdjustmentChanged();
        setAutoBrightnessApplied(isValid);
        return isValid;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean isAutoBrightnessDisabledDueToDisplayOff() {
        return this.mAutoBrightnessDisabledDueToDisplayOff;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public void setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration brightnessConfiguration, boolean shouldResetShortTermModel) {
        this.mBrightnessConfiguration = brightnessConfiguration;
        setShouldResetShortTermModel(shouldResetShortTermModel);
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean processPendingAutoBrightnessAdjustments() {
        this.mAutoBrightnessAdjustmentChanged = false;
        if (this.mDpcExt.isSpecialAdj(this.mTemporaryAutoBrightnessAdjustment)) {
            this.mAutoBrightnessAdjustment = this.mTemporaryAutoBrightnessAdjustment;
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
            this.mAutoBrightnessAdjustmentChanged = true;
            return true;
        }
        if (java.lang.Float.isNaN(this.mPendingAutoBrightnessAdjustment)) {
            return false;
        }
        if (this.mAutoBrightnessAdjustment == this.mPendingAutoBrightnessAdjustment || this.mDpcExt.isSpecialAdj(this.mAutoBrightnessAdjustment)) {
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
            return false;
        }
        if (this.mPendingAutoBrightnessAdjustment == 0.0f) {
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            return false;
        }
        this.mAutoBrightnessAdjustment = this.mPendingAutoBrightnessAdjustment;
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        this.mAutoBrightnessAdjustmentChanged = true;
        return true;
    }

    public void setAutomaticBrightnessController(com.android.server.display.IColorAutomaticBrightnessController automaticBrightnessController) {
        if (automaticBrightnessController == this.mAutomaticBrightnessController) {
            return;
        }
        if (this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.stop();
        }
        this.mAutomaticBrightnessController = automaticBrightnessController;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean shouldUseAutoBrightness() {
        return this.mUseAutoBrightness;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public void setUseAutoBrightness(boolean useAutoBrightness) {
        this.mUseAutoBrightness = useAutoBrightness;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean isShortTermModelActive() {
        return this.mIsShortTermModelActive;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public void updatePendingAutoBrightnessAdjustments() {
        float adj = android.provider.Settings.System.getFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", 0.0f, -2);
        this.mPendingAutoBrightnessAdjustment = java.lang.Float.isNaN(adj) ? Float.NaN : com.android.server.display.brightness.BrightnessUtils.clampBrightnessAdjustment(adj);
        if (this.mDpcExt.hasRemapDisable()) {
            this.mDpcExt.getAdjustmentSetting(this.mContext, this.mPendingAutoBrightnessAdjustment);
        }
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public void setTemporaryAutoBrightnessAdjustment(float temporaryAutoBrightnessAdjustment) {
        this.mTemporaryAutoBrightnessAdjustment = temporaryAutoBrightnessAdjustment;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        com.android.server.display.brightness.BrightnessReason brightnessReason = new com.android.server.display.brightness.BrightnessReason();
        brightnessReason.setReason(4);
        com.android.server.display.brightness.BrightnessEvent brightnessEvent = this.mInjector.getBrightnessEvent(this.mDisplayId);
        boolean z = true;
        float brightness = getAutomaticScreenBrightness(brightnessEvent, true);
        com.android.server.display.DisplayBrightnessState.Builder shouldUpdateScreenBrightnessSetting = new com.android.server.display.DisplayBrightnessState.Builder().setBrightness(brightness).setSdrBrightness(brightness).setBrightnessReason(brightnessReason).setDisplayBrightnessStrategyName(getName()).setIsSlowChange(this.mIsSlowChange).setBrightnessEvent(brightnessEvent).setBrightnessAdjustmentFlag(this.mAutoBrightnessAdjustmentReasonsFlags).setShouldUpdateScreenBrightnessSetting(brightness != strategyExecutionRequest.getCurrentScreenBrightness());
        if (!getAutoBrightnessAdjustmentChanged() && !strategyExecutionRequest.isUserSetBrightnessChanged()) {
            z = false;
        }
        return shouldUpdateScreenBrightnessSetting.setIsUserInitiatedChange(z).build();
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "AutomaticBrightnessStrategy";
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2, com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
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

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
        if (!this.mIsConfigured) {
            setAutoBrightnessState(strategySelectionNotifyRequest.getTargetDisplayState(), strategySelectionNotifyRequest.isAllowAutoBrightnessWhileDozingConfig(), strategySelectionNotifyRequest.getSelectedDisplayBrightnessStrategy().getReason(), strategySelectionNotifyRequest.getDisplayPowerRequest().policy, strategySelectionNotifyRequest.getLastUserSetScreenBrightness(), strategySelectionNotifyRequest.isUserSetBrightnessChanged());
        }
        this.mIsConfigured = false;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 4;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean getAutoBrightnessAdjustmentChanged() {
        return this.mAutoBrightnessAdjustmentChanged;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean isTemporaryAutoBrightnessAdjustmentApplied() {
        return this.mAppliedTemporaryAutoBrightnessAdjustment;
    }

    public float getAutomaticScreenBrightness(com.android.server.display.brightness.BrightnessEvent brightnessEvent, boolean isAutomaticBrightnessAdjusted) {
        float brightness;
        if (this.mAutomaticBrightnessController != null) {
            brightness = this.mAutomaticBrightnessController.getAutomaticScreenBrightness();
        } else {
            brightness = Float.NaN;
        }
        if (!isAutomaticBrightnessAdjusted) {
            adjustAutomaticBrightnessStateIfValid(brightness);
        }
        return brightness;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public boolean hasAppliedAutoBrightness() {
        return this.mAppliedAutoBrightness;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
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
        if (java.lang.Float.isNaN(newAutoBrightnessAdjustment) || this.mAutoBrightnessAdjustment == newAutoBrightnessAdjustment) {
            this.mAutoBrightnessAdjustmentReasonsFlags = 0;
        }
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    void setShouldResetShortTermModel(boolean shouldResetShortTermModel) {
        this.mShouldResetShortTermModel = shouldResetShortTermModel;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    boolean shouldResetShortTermModel() {
        return this.mShouldResetShortTermModel;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    float getAutoBrightnessAdjustment() {
        return this.mAutoBrightnessAdjustment;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    float getPendingAutoBrightnessAdjustment() {
        return this.mPendingAutoBrightnessAdjustment;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    float getTemporaryAutoBrightnessAdjustment() {
        return this.mTemporaryAutoBrightnessAdjustment;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    void putAutoBrightnessAdjustmentSetting(float adjustment) {
        if (this.mDisplayId == 0) {
            this.mAutoBrightnessAdjustment = adjustment;
            android.provider.Settings.System.putFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", adjustment, -2);
        }
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    public void setAutoBrightnessApplied(boolean autoBrightnessApplied) {
        this.mAppliedAutoBrightness = autoBrightnessApplied;
    }

    @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2
    void accommodateUserBrightnessChanges(boolean userSetBrightnessChanged, float lastUserSetScreenBrightness, int policy, int displayState, android.hardware.display.BrightnessConfiguration brightnessConfiguration, int autoBrightnessState) {
        processPendingAutoBrightnessAdjustments();
        float autoBrightnessAdjustment = updateTemporaryAutoBrightnessAdjustments();
        this.mIsShortTermModelActive = false;
        if (this.mAutomaticBrightnessController != null) {
            boolean dozing = displayState != 2;
            this.mDpcExt.configure(this.mIsAutoBrightnessEnabled, autoBrightnessAdjustment, dozing, false, this.mDisplayId, displayState);
        }
    }

    private void switchMode(int state, boolean sendUpdate) {
        if (this.mDisplayManagerFlags.areAutoBrightnessModesEnabled() && this.mAutomaticBrightnessController != null) {
            this.mAutomaticBrightnessController.isInIdleMode();
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

    public float getAutomaticScreenBrightness() {
        float brightness;
        if (this.mAutomaticBrightnessController != null) {
            brightness = this.mAutomaticBrightnessController.getAutomaticScreenBrightness();
        } else {
            brightness = Float.NaN;
        }
        adjustAutomaticBrightnessStateIfValid(brightness);
        return brightness;
    }

    public void setAutoBrightnessAdjustment(float val) {
        this.mAutoBrightnessAdjustment = val;
    }

    static class RealInjector implements com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy.Injector {
        RealInjector() {
        }

        @Override // com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy.Injector
        public com.android.server.display.brightness.BrightnessEvent getBrightnessEvent(int displayId) {
            return new com.android.server.display.brightness.BrightnessEvent(displayId);
        }
    }
}
