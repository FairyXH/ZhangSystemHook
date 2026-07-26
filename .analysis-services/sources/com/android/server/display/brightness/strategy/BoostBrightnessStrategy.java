package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class BoostBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;

    public BoostBrightnessStrategy(com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this.mDpcExt = dpcExt;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        com.android.server.display.DisplayBrightnessState displayBrightnessState = com.android.server.display.brightness.BrightnessUtils.constructDisplayBrightnessState(8, this.mDpcExt.getMaxDisplayBrightness(), this.mDpcExt.getMaxDisplayBrightness(), getName());
        return displayBrightnessState;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "BoostBrightnessStrategy";
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 8;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
    }
}
