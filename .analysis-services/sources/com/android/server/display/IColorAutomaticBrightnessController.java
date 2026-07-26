package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IColorAutomaticBrightnessController {
    void addCallbacks(com.android.server.display.AutomaticBrightnessController.Callbacks callbacks);

    void configure(boolean z, float f, boolean z2, boolean z3, int i, int i2);

    float convertToAdjustedNits(float f);

    float convertToFloatScale(float f);

    float convertToNits(float f);

    void dump(java.io.PrintWriter printWriter);

    int getAIBrightness();

    int getAIBrightness(int i);

    float getAmbientLux();

    int getAutoRate(int i);

    int getAutomaticScreenBrightness();

    int getAutomaticScreenBrightness(int i);

    float getAutomaticScreenBrightnessAdjustment();

    float getBrightnessFromNits(float f);

    com.android.server.display.AutomaticBrightnessController.Callbacks getCallbacks();

    int getCameraMode();

    android.hardware.display.BrightnessConfiguration getDefaultConfig();

    float getRawAutomaticScreenBrightness();

    float getUserLux();

    float getUserNits();

    boolean getmLightSensorEnabled();

    boolean getmProximityNear();

    void init(com.android.server.display.AutomaticBrightnessController.Callbacks callbacks, android.os.Looper looper, android.hardware.SensorManager sensorManager, android.hardware.Sensor sensor, com.android.server.display.BrightnessMappingStrategy brightnessMappingStrategy, float f, int i, long j);

    boolean isAlreadyInit();

    boolean isCtsTest();

    boolean isDefaultConfig();

    boolean isInIdleMode();

    boolean isSensorChanged();

    void resetShortTermModel();

    void setAnimating(boolean z, int i, boolean z2);

    void setAutomaticScreenBrightness(int i, int i2);

    void setCameraBacklight(boolean z);

    void setCameraMode(int i);

    void setCameraUseAdjustmentSetting(boolean z);

    void setDualLightSensorConfig(boolean z, java.lang.String str, java.lang.String str2, float f);

    void setGalleryBacklight(boolean z);

    void setGalleryMode(int i);

    boolean setLoggingEnabled(boolean z);

    void setLux(float f);

    void setPowerRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest);

    void setScreenOn(int i, boolean z, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest);

    void setStateChanged(int i, android.os.Bundle bundle);

    void setTalkBack(boolean z, int i);

    void stop();

    void updateBrightnessTotalRateType(int i, int i2);
}
