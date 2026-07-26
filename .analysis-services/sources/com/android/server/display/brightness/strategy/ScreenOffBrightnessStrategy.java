package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class ScreenOffBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        return com.android.server.display.brightness.BrightnessUtils.constructDisplayBrightnessState(5, -1.0f, -1.0f, getName());
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "ScreenOffBrightnessStrategy";
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 5;
    }
}
