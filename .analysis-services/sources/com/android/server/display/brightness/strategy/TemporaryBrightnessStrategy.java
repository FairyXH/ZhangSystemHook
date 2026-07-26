package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class TemporaryBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    private float mTemporaryScreenBrightness = Float.NaN;

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        com.android.server.display.DisplayBrightnessState displayBrightnessState = com.android.server.display.brightness.BrightnessUtils.constructDisplayBrightnessState(7, this.mTemporaryScreenBrightness, this.mTemporaryScreenBrightness, getName());
        return displayBrightnessState;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "TemporaryBrightnessStrategy";
    }

    public float getTemporaryScreenBrightness() {
        return this.mTemporaryScreenBrightness;
    }

    public void setTemporaryScreenBrightness(float temporaryScreenBrightness) {
        this.mTemporaryScreenBrightness = temporaryScreenBrightness;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
        writer.println("TemporaryBrightnessStrategy:");
        writer.println("  mTemporaryScreenBrightness:" + this.mTemporaryScreenBrightness);
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 7;
    }
}
