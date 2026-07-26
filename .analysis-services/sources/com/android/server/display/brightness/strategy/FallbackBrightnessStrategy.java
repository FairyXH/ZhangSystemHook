package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class FallbackBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        com.android.server.display.brightness.BrightnessReason brightnessReason = new com.android.server.display.brightness.BrightnessReason();
        brightnessReason.setReason(1);
        return new com.android.server.display.DisplayBrightnessState.Builder().setBrightness(strategyExecutionRequest.getCurrentScreenBrightness()).setSdrBrightness(strategyExecutionRequest.getCurrentScreenBrightness()).setBrightnessReason(brightnessReason).setDisplayBrightnessStrategyName(getName()).setShouldUpdateScreenBrightnessSetting(true).setIsUserInitiatedChange(strategyExecutionRequest.isUserSetBrightnessChanged()).build();
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return com.android.server.display.brightness.strategy.DisplayBrightnessStrategyConstants.FALLBACK_BRIGHTNESS_STRATEGY_NAME;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 1;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
    }
}
