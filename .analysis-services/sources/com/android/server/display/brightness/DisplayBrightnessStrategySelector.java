package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayBrightnessStrategySelector {
    private static final java.lang.String TAG = "DisplayBrightnessStrategySelector";
    private boolean mAllowAutoBrightnessWhileDozing;
    private final boolean mAllowAutoBrightnessWhileDozingConfig;
    private final com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy mAutoBrightnessFallbackStrategy;
    private final com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy mAutomaticBrightnessStrategy;
    private final com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy mAutomaticBrightnessStrategy1;
    private final com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2 mAutomaticBrightnessStrategy2;
    private final com.android.server.display.brightness.strategy.BoostBrightnessStrategy mBoostBrightnessStrategy;
    final com.android.server.display.brightness.strategy.DisplayBrightnessStrategy[] mDisplayBrightnessStrategies;
    private final int mDisplayId;
    private final com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private final com.android.server.display.brightness.strategy.DozeBrightnessStrategy mDozeBrightnessStrategy;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private final com.android.server.display.brightness.strategy.FallbackBrightnessStrategy mFallbackBrightnessStrategy;
    private final com.android.server.display.brightness.strategy.FollowerBrightnessStrategy mFollowerBrightnessStrategy;
    private final com.android.server.display.brightness.strategy.InvalidBrightnessStrategy mInvalidBrightnessStrategy;
    private final com.android.server.display.brightness.strategy.OffloadBrightnessStrategy mOffloadBrightnessStrategy;
    private java.lang.String mOldBrightnessStrategyName;
    private final com.android.server.display.brightness.strategy.OverrideBrightnessStrategy mOverrideBrightnessStrategy;
    private float mScreenBrightnessRangeMaximum;
    private float mScreenBrightnessRangeMinimum;
    private final com.android.server.display.brightness.strategy.ScreenOffBrightnessStrategy mScreenOffBrightnessStrategy;
    private final com.android.server.display.brightness.strategy.TemporaryBrightnessStrategy mTemporaryBrightnessStrategy;

    public DisplayBrightnessStrategySelector(android.content.Context context, com.android.server.display.brightness.DisplayBrightnessStrategySelector.Injector injector, int displayId, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        injector = injector == null ? new com.android.server.display.brightness.DisplayBrightnessStrategySelector.Injector() : injector;
        this.mDisplayManagerFlags = flags;
        this.mDisplayId = displayId;
        this.mDozeBrightnessStrategy = injector.getDozeBrightnessStrategy();
        this.mScreenOffBrightnessStrategy = injector.getScreenOffBrightnessStrategy();
        this.mOverrideBrightnessStrategy = injector.getOverrideBrightnessStrategy(dpcExt);
        this.mTemporaryBrightnessStrategy = injector.getTemporaryBrightnessStrategy();
        this.mBoostBrightnessStrategy = injector.getBoostBrightnessStrategy(dpcExt);
        this.mFollowerBrightnessStrategy = injector.getFollowerBrightnessStrategy(displayId);
        this.mInvalidBrightnessStrategy = injector.getInvalidBrightnessStrategy();
        this.mAutomaticBrightnessStrategy1 = !this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled() ? null : injector.getAutomaticBrightnessStrategy1(context, displayId, this.mDisplayManagerFlags, dpcExt);
        this.mAutomaticBrightnessStrategy2 = this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled() ? null : injector.getAutomaticBrightnessStrategy2(context, displayId);
        this.mAutomaticBrightnessStrategy = injector.getAutomaticBrightnessStrategy1(context, displayId, this.mDisplayManagerFlags, dpcExt);
        this.mAutoBrightnessFallbackStrategy = this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled() ? injector.getAutoBrightnessFallbackStrategy() : null;
        if (flags.isDisplayOffloadEnabled()) {
            this.mOffloadBrightnessStrategy = injector.getOffloadBrightnessStrategy(this.mDisplayManagerFlags);
        } else {
            this.mOffloadBrightnessStrategy = null;
        }
        this.mFallbackBrightnessStrategy = this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled() ? injector.getFallbackBrightnessStrategy() : null;
        this.mDisplayBrightnessStrategies = new com.android.server.display.brightness.strategy.DisplayBrightnessStrategy[]{this.mInvalidBrightnessStrategy, this.mScreenOffBrightnessStrategy, this.mDozeBrightnessStrategy, this.mFollowerBrightnessStrategy, this.mBoostBrightnessStrategy, this.mOverrideBrightnessStrategy, this.mTemporaryBrightnessStrategy, this.mAutomaticBrightnessStrategy1, this.mOffloadBrightnessStrategy, this.mAutoBrightnessFallbackStrategy, this.mFallbackBrightnessStrategy};
        this.mAllowAutoBrightnessWhileDozingConfig = context.getResources().getBoolean(android.R.bool.config_allowAutoBrightnessWhileDozing);
        this.mAllowAutoBrightnessWhileDozing = this.mAllowAutoBrightnessWhileDozingConfig;
        this.mOldBrightnessStrategyName = this.mInvalidBrightnessStrategy.getName();
        this.mScreenBrightnessRangeMinimum = dpcExt.getMinDisplayBrightness();
        this.mScreenBrightnessRangeMaximum = dpcExt.getTotalDisplayBrightness();
        this.mDpcExt = dpcExt;
    }

    public com.android.server.display.brightness.strategy.DisplayBrightnessStrategy selectStrategy(com.android.server.display.brightness.StrategySelectionRequest strategySelectionRequest) {
        com.android.server.display.brightness.strategy.DisplayBrightnessStrategy displayBrightnessStrategy = this.mInvalidBrightnessStrategy;
        int targetDisplayState = strategySelectionRequest.getTargetDisplayState();
        android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest = strategySelectionRequest.getDisplayPowerRequest();
        setAllowAutoBrightnessWhileDozing(strategySelectionRequest.getDisplayOffloadSession());
        if (this.mDpcExt.getResetTemporaryStrategyStatus() && !java.lang.Float.isNaN(this.mTemporaryBrightnessStrategy.getTemporaryScreenBrightness())) {
            android.util.Slog.i(TAG, "reset temporaryScreenBrightness to invalid.");
            this.mTemporaryBrightnessStrategy.setTemporaryScreenBrightness(Float.NaN);
            this.mDpcExt.setResetTemporaryStrategyStatus(false);
        }
        if (targetDisplayState == 1) {
            displayBrightnessStrategy = this.mScreenOffBrightnessStrategy;
        } else if (shouldUseDozeBrightnessStrategy(displayPowerRequest)) {
            displayBrightnessStrategy = this.mDozeBrightnessStrategy;
        } else if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(this.mFollowerBrightnessStrategy.getBrightnessToFollow(), this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
            displayBrightnessStrategy = this.mFollowerBrightnessStrategy;
        } else if (displayPowerRequest.boostScreenBrightness) {
            displayBrightnessStrategy = this.mBoostBrightnessStrategy;
        } else if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(displayPowerRequest.screenBrightnessOverride, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
            displayBrightnessStrategy = this.mOverrideBrightnessStrategy;
        } else if (com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(this.mTemporaryBrightnessStrategy.getTemporaryScreenBrightness(), this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
            displayBrightnessStrategy = this.mTemporaryBrightnessStrategy;
        } else if (this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled() && isAutomaticBrightnessStrategyValid(strategySelectionRequest)) {
            displayBrightnessStrategy = this.mAutomaticBrightnessStrategy1;
        } else if (this.mAutomaticBrightnessStrategy.shouldUseAutoBrightness() && this.mOffloadBrightnessStrategy != null && com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(this.mOffloadBrightnessStrategy.getOffloadScreenBrightness(), this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
            displayBrightnessStrategy = this.mOffloadBrightnessStrategy;
        } else if (isAutoBrightnessFallbackStrategyValid()) {
            displayBrightnessStrategy = this.mAutoBrightnessFallbackStrategy;
        } else if (this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled()) {
            displayBrightnessStrategy = this.mFallbackBrightnessStrategy;
        }
        if (this.mTemporaryBrightnessStrategy.getName().equals(displayBrightnessStrategy.getName())) {
            this.mDpcExt.setByUser(true);
        } else {
            this.mDpcExt.setByUser(false);
        }
        if (this.mOverrideBrightnessStrategy.getName().equals(displayBrightnessStrategy.getName())) {
            if (!com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(this.mTemporaryBrightnessStrategy.getTemporaryScreenBrightness(), this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum)) {
                this.mDpcExt.updateScreenBrightnessOverride(true);
            } else {
                this.mOverrideBrightnessStrategy.setOplusOverriedBrightness(this.mTemporaryBrightnessStrategy.getTemporaryScreenBrightness());
                this.mOverrideBrightnessStrategy.setOplusLastOverriedBrightness(displayPowerRequest.screenBrightnessOverride);
            }
            this.mDpcExt.setWinOverride(true);
        } else {
            this.mOverrideBrightnessStrategy.setOplusOverriedBrightness(Float.NaN);
            this.mOverrideBrightnessStrategy.setOplusLastOverriedBrightness(Float.NaN);
            this.mDpcExt.setWinOverride(false);
        }
        if (this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled()) {
            postProcess(constructStrategySelectionNotifyRequest(displayBrightnessStrategy, strategySelectionRequest));
        }
        if (!this.mOldBrightnessStrategyName.equals(displayBrightnessStrategy.getName())) {
            android.util.Slog.i(TAG, "Changing the DisplayBrightnessStrategy from " + this.mOldBrightnessStrategyName + " to " + displayBrightnessStrategy.getName() + " for display " + this.mDisplayId);
            if (this.mOverrideBrightnessStrategy.getName().equals(this.mOldBrightnessStrategyName) && this.mTemporaryBrightnessStrategy.getName().equals(displayBrightnessStrategy.getName())) {
                this.mTemporaryBrightnessStrategy.setTemporaryScreenBrightness(Float.NaN);
                displayBrightnessStrategy = this.mInvalidBrightnessStrategy;
                android.util.Slog.d(TAG, "Changing the DisplayBrightnessStrategy to InvalidBrightnessStrategy");
            }
            this.mOldBrightnessStrategyName = displayBrightnessStrategy.getName();
        }
        return displayBrightnessStrategy;
    }

    public com.android.server.display.brightness.strategy.TemporaryBrightnessStrategy getTemporaryDisplayBrightnessStrategy() {
        return this.mTemporaryBrightnessStrategy;
    }

    public com.android.server.display.brightness.strategy.FollowerBrightnessStrategy getFollowerDisplayBrightnessStrategy() {
        return this.mFollowerBrightnessStrategy;
    }

    public com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy getAutomaticBrightnessStrategy() {
        return this.mAutomaticBrightnessStrategy;
    }

    public com.android.server.display.brightness.strategy.OffloadBrightnessStrategy getOffloadBrightnessStrategy() {
        return this.mOffloadBrightnessStrategy;
    }

    public boolean isAllowAutoBrightnessWhileDozing() {
        return this.mAllowAutoBrightnessWhileDozing;
    }

    public boolean isAllowAutoBrightnessWhileDozingConfig() {
        return this.mAllowAutoBrightnessWhileDozingConfig;
    }

    public com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy getAutoBrightnessFallbackStrategy() {
        return this.mAutoBrightnessFallbackStrategy;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println();
        writer.println("DisplayBrightnessStrategySelector:");
        writer.println("  mDisplayId= " + this.mDisplayId);
        writer.println("  mOldBrightnessStrategyName= " + this.mOldBrightnessStrategyName);
        writer.println("  mAllowAutoBrightnessWhileDozingConfig= " + this.mAllowAutoBrightnessWhileDozingConfig);
        writer.println("  mAllowAutoBrightnessWhileDozing= " + this.mAllowAutoBrightnessWhileDozing);
        java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(writer, " ");
        for (com.android.server.display.brightness.strategy.DisplayBrightnessStrategy displayBrightnessStrategy : this.mDisplayBrightnessStrategies) {
            if (displayBrightnessStrategy != null) {
                displayBrightnessStrategy.dump(indentingPrintWriter);
            }
        }
    }

    void setAllowAutoBrightnessWhileDozing(android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession) {
        this.mAllowAutoBrightnessWhileDozing = this.mAllowAutoBrightnessWhileDozingConfig;
        if (this.mDisplayManagerFlags.offloadControlsDozeAutoBrightness() && this.mDisplayManagerFlags.isDisplayOffloadEnabled() && displayOffloadSession != null) {
            this.mAllowAutoBrightnessWhileDozing &= displayOffloadSession.allowAutoBrightnessInDoze();
        }
    }

    private boolean isAutoBrightnessFallbackStrategyValid() {
        return this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled() && this.mAutoBrightnessFallbackStrategy != null && getAutomaticBrightnessStrategy().shouldUseAutoBrightness() && this.mAutoBrightnessFallbackStrategy.isValid();
    }

    private boolean isAutomaticBrightnessStrategyValid(com.android.server.display.brightness.StrategySelectionRequest strategySelectionRequest) {
        this.mAutomaticBrightnessStrategy1.setAutoBrightnessState(strategySelectionRequest.getTargetDisplayState(), this.mAllowAutoBrightnessWhileDozing, 0, strategySelectionRequest.getDisplayPowerRequest().policy, strategySelectionRequest.getLastUserSetScreenBrightness(), strategySelectionRequest.isUserSetBrightnessChanged());
        return this.mAutomaticBrightnessStrategy1.isAutoBrightnessValid();
    }

    private com.android.server.display.brightness.StrategySelectionNotifyRequest constructStrategySelectionNotifyRequest(com.android.server.display.brightness.strategy.DisplayBrightnessStrategy selectedDisplayBrightnessStrategy, com.android.server.display.brightness.StrategySelectionRequest strategySelectionRequest) {
        return new com.android.server.display.brightness.StrategySelectionNotifyRequest(strategySelectionRequest.getDisplayPowerRequest(), strategySelectionRequest.getTargetDisplayState(), selectedDisplayBrightnessStrategy, strategySelectionRequest.getLastUserSetScreenBrightness(), strategySelectionRequest.isUserSetBrightnessChanged(), this.mAllowAutoBrightnessWhileDozing, getAutomaticBrightnessStrategy().shouldUseAutoBrightness());
    }

    private void postProcess(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
        for (com.android.server.display.brightness.strategy.DisplayBrightnessStrategy displayBrightnessStrategy : this.mDisplayBrightnessStrategies) {
            if (displayBrightnessStrategy != null) {
                displayBrightnessStrategy.strategySelectionPostProcessor(strategySelectionNotifyRequest);
            }
        }
    }

    private boolean shouldUseDozeBrightnessStrategy(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
        return displayPowerRequest.policy == 1 && !this.mAllowAutoBrightnessWhileDozing && com.android.server.display.brightness.BrightnessUtils.isValidBrightnessValue(displayPowerRequest.dozeScreenBrightness, this.mScreenBrightnessRangeMinimum, this.mScreenBrightnessRangeMaximum);
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.brightness.strategy.ScreenOffBrightnessStrategy getScreenOffBrightnessStrategy() {
            return new com.android.server.display.brightness.strategy.ScreenOffBrightnessStrategy();
        }

        com.android.server.display.brightness.strategy.DozeBrightnessStrategy getDozeBrightnessStrategy() {
            return new com.android.server.display.brightness.strategy.DozeBrightnessStrategy();
        }

        com.android.server.display.brightness.strategy.OverrideBrightnessStrategy getOverrideBrightnessStrategy(com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.brightness.strategy.OverrideBrightnessStrategy(dpcExt);
        }

        com.android.server.display.brightness.strategy.TemporaryBrightnessStrategy getTemporaryBrightnessStrategy() {
            return new com.android.server.display.brightness.strategy.TemporaryBrightnessStrategy();
        }

        com.android.server.display.brightness.strategy.BoostBrightnessStrategy getBoostBrightnessStrategy(com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.brightness.strategy.BoostBrightnessStrategy(dpcExt);
        }

        com.android.server.display.brightness.strategy.FollowerBrightnessStrategy getFollowerBrightnessStrategy(int displayId) {
            return new com.android.server.display.brightness.strategy.FollowerBrightnessStrategy(displayId);
        }

        com.android.server.display.brightness.strategy.InvalidBrightnessStrategy getInvalidBrightnessStrategy() {
            return new com.android.server.display.brightness.strategy.InvalidBrightnessStrategy();
        }

        com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy getAutomaticBrightnessStrategy1(android.content.Context context, int displayId, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
            return new com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy(context, displayId, displayManagerFlags, dpcExt);
        }

        com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2 getAutomaticBrightnessStrategy2(android.content.Context context, int displayId) {
            return new com.android.server.display.brightness.strategy.AutomaticBrightnessStrategy2(context, displayId);
        }

        com.android.server.display.brightness.strategy.OffloadBrightnessStrategy getOffloadBrightnessStrategy(com.android.server.display.feature.DisplayManagerFlags displayManagerFlags) {
            return new com.android.server.display.brightness.strategy.OffloadBrightnessStrategy(displayManagerFlags);
        }

        com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy getAutoBrightnessFallbackStrategy() {
            return new com.android.server.display.brightness.strategy.AutoBrightnessFallbackStrategy(null);
        }

        com.android.server.display.brightness.strategy.FallbackBrightnessStrategy getFallbackBrightnessStrategy() {
            return new com.android.server.display.brightness.strategy.FallbackBrightnessStrategy();
        }
    }
}
