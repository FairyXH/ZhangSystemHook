package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
public interface BrightnessStateModifier {
    void apply(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, com.android.server.display.DisplayBrightnessState.Builder builder);

    void dump(java.io.PrintWriter printWriter);

    void recoverRateType();

    void setAmbientLux(float f);

    void setAnimatingState(boolean z);

    void setRateType();

    boolean shouldListenToLightSensor();

    void stop();
}
