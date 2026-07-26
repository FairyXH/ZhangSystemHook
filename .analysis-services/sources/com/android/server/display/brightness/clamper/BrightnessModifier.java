package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
abstract class BrightnessModifier implements com.android.server.display.brightness.clamper.BrightnessStateModifier {
    private boolean mApplied = false;

    abstract float getBrightnessAdjusted(float f, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest);

    abstract int getModifier();

    abstract boolean shouldApply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest);

    BrightnessModifier() {
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void apply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, com.android.server.display.DisplayBrightnessState.Builder stateBuilder) {
        if (shouldApply(request)) {
            float value = stateBuilder.getBrightness();
            if (value > 0.0f) {
                stateBuilder.setBrightness(getBrightnessAdjusted(value, request));
                stateBuilder.getBrightnessReason().addModifier(getModifier());
            }
            if (!this.mApplied) {
                stateBuilder.setIsSlowChange(false);
                setAnimatingState(true);
                setRateType();
            }
            this.mApplied = true;
            return;
        }
        if (this.mApplied) {
            stateBuilder.setIsSlowChange(false);
            this.mApplied = false;
            setAnimatingState(true);
            recoverRateType();
        }
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void dump(java.io.PrintWriter pw) {
        pw.println("BrightnessModifier:");
        pw.println("  mApplied=" + this.mApplied);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void stop() {
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setAnimatingState(boolean state) {
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setRateType() {
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void recoverRateType() {
    }
}
