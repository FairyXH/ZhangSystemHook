package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
class DisplayDimModifier extends com.android.server.display.brightness.clamper.BrightnessModifier {
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private final float mScreenBrightnessDimConfig;
    private final float mScreenBrightnessMinimumDimAmount;

    DisplayDimModifier(android.content.Context context, com.android.server.display.IOplusDisplayPowerControllerExt dpcExt) {
        android.os.PowerManager pm = (android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) context.getSystemService(android.os.PowerManager.class));
        android.content.res.Resources resources = context.getResources();
        this.mScreenBrightnessDimConfig = com.android.server.display.brightness.BrightnessUtils.clampAbsoluteBrightness(pm.getBrightnessConstraint(3));
        this.mScreenBrightnessMinimumDimAmount = resources.getFloat(android.R.dimen.config_letterboxThinLetterboxWidthDp);
        this.mDpcExt = dpcExt;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    boolean shouldApply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request) {
        return request.policy == 2;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    float getBrightnessAdjusted(float currentBrightness, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request) {
        float brightnessState = this.mDpcExt.applydimmingbrightness((int) currentBrightness);
        return brightnessState;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    int getModifier() {
        return 1;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setRateType() {
        this.mDpcExt.setDimRateType();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void recoverRateType() {
        this.mDpcExt.recoverOriginRateType();
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void dump(java.io.PrintWriter pw) {
        pw.println("DisplayDimModifier:");
        pw.println("  mScreenBrightnessDimConfig=" + this.mScreenBrightnessDimConfig);
        pw.println("  mScreenBrightnessMinimumDimAmount=" + this.mScreenBrightnessMinimumDimAmount);
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "    ");
        super.dump(ipw);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public boolean shouldListenToLightSensor() {
        return false;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setAmbientLux(float lux) {
    }
}
