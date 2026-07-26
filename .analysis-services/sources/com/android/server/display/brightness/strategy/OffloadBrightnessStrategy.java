package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class OffloadBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    private final com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private float mOffloadScreenBrightness = Float.NaN;

    public OffloadBrightnessStrategy(com.android.server.display.feature.DisplayManagerFlags displayManagerFlags) {
        this.mDisplayManagerFlags = displayManagerFlags;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        float offloadBrightness = this.mOffloadScreenBrightness;
        if (this.mDisplayManagerFlags.isRefactorDisplayPowerControllerEnabled()) {
            this.mOffloadScreenBrightness = Float.NaN;
        }
        com.android.server.display.brightness.BrightnessReason brightnessReason = new com.android.server.display.brightness.BrightnessReason();
        brightnessReason.setReason(11);
        return new com.android.server.display.DisplayBrightnessState.Builder().setBrightness(offloadBrightness).setSdrBrightness(offloadBrightness).setBrightnessReason(brightnessReason).setDisplayBrightnessStrategyName(getName()).setIsSlowChange(false).setShouldUpdateScreenBrightnessSetting(true).build();
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "OffloadBrightnessStrategy";
    }

    public float getOffloadScreenBrightness() {
        return this.mOffloadScreenBrightness;
    }

    public void setOffloadScreenBrightness(float offloadScreenBrightness) {
        this.mOffloadScreenBrightness = offloadScreenBrightness;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
        writer.println("OffloadBrightnessStrategy:");
        writer.println("  mOffloadScreenBrightness:" + this.mOffloadScreenBrightness);
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
        if (!strategySelectionNotifyRequest.getSelectedDisplayBrightnessStrategy().getName().equals(getName()) && !strategySelectionNotifyRequest.getSelectedDisplayBrightnessStrategy().getName().equals("InvalidBrightnessStrategy")) {
            this.mOffloadScreenBrightness = Float.NaN;
        }
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 11;
    }
}
