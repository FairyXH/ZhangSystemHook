package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
class BrightnessLowPowerModeModifier extends com.android.server.display.brightness.clamper.BrightnessModifier {
    private static final int LOW_POWER_OFF = 0;
    private static final int LOW_POWER_ON = 1;
    private com.android.server.display.IOplusDisplayPowerControllerExt mDpcExt;
    private boolean mIsPrimaryDisplay;

    public BrightnessLowPowerModeModifier(com.android.server.display.IOplusDisplayPowerControllerExt dpcExt, boolean isPrimaryDisplay) {
        this.mIsPrimaryDisplay = false;
        this.mDpcExt = dpcExt;
        this.mIsPrimaryDisplay = isPrimaryDisplay;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    boolean shouldApply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest) {
        this.mDpcExt.setSavePowerMode(displayPowerRequest.lowPowerMode ? 1 : 0);
        return displayPowerRequest.lowPowerMode;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    float getBrightnessAdjusted(float currentBrightness, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request) {
        float brightnessFactor = java.lang.Math.min(request.screenLowPowerBrightnessFactor, 1.0f);
        int batteryLevel = request.batteryLevel;
        float brightnessState = this.mDpcExt.getLowPowerModeBtnExp(currentBrightness, brightnessFactor, this.mIsPrimaryDisplay, batteryLevel);
        return java.lang.Math.max(brightnessState, this.mDpcExt.getMinDisplayBrightness());
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier
    int getModifier() {
        return 2;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void dump(java.io.PrintWriter pw) {
        pw.println("BrightnessLowPowerModeModifier:");
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "    ");
        super.dump(ipw);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessModifier, com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setAnimatingState(boolean state) {
        this.mDpcExt.setLowPowerAnimatingState(true);
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public boolean shouldListenToLightSensor() {
        return false;
    }

    @Override // com.android.server.display.brightness.clamper.BrightnessStateModifier
    public void setAmbientLux(float lux) {
    }
}
