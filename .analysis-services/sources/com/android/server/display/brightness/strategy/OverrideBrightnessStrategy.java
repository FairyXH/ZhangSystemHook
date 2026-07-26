package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class OverrideBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private float mOplusOverriedBrightness = Float.NaN;
    private float mOplusLastOverriedBrightness = Float.NaN;

    public OverrideBrightnessStrategy(com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        this.mDpcExt = dpcExt;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        float brightness = strategyExecutionRequest.getDisplayPowerRequest().screenBrightnessOverride;
        float windowMaxBrightness = this.mDpcExt.getOplusWindowMaxBrightness(brightness);
        if (!java.lang.Float.isNaN(this.mOplusOverriedBrightness)) {
            if (brightness == this.mOplusLastOverriedBrightness) {
                brightness = this.mOplusOverriedBrightness;
            } else {
                this.mOplusOverriedBrightness = Float.NaN;
                this.mOplusLastOverriedBrightness = Float.NaN;
            }
        }
        float brightness2 = java.lang.Math.min(brightness, windowMaxBrightness);
        return com.android.server.display.brightness.BrightnessUtils.constructDisplayBrightnessState(6, brightness2, brightness2, getName());
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "OverrideBrightnessStrategy";
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 6;
    }

    public void setOplusOverriedBrightness(float oplusOverriedBrightness) {
        this.mOplusOverriedBrightness = oplusOverriedBrightness;
    }

    public void setOplusLastOverriedBrightness(float oplusLastOverriedBrightness) {
        this.mOplusLastOverriedBrightness = oplusLastOverriedBrightness;
    }
}
