package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface DisplayPowerControllerInterface {
    void addDisplayBrightnessFollower(com.android.server.display.DisplayPowerControllerInterface displayPowerControllerInterface);

    void dump(java.io.PrintWriter printWriter);

    android.content.pm.ParceledListSlice<android.hardware.display.AmbientBrightnessDayStats> getAmbientBrightnessStats(int i);

    float[] getAutoBrightnessLevels(int i);

    float[] getAutoBrightnessLuxLevels(int i);

    android.content.pm.ParceledListSlice<android.hardware.display.BrightnessChangeEvent> getBrightnessEvents(int i, boolean z);

    android.hardware.display.BrightnessInfo getBrightnessInfo();

    android.hardware.display.BrightnessConfiguration getDefaultBrightnessConfiguration();

    int getDisplayId();

    float getDozeBrightnessForOffload();

    int getLeadDisplayId();

    float getScreenBrightnessSetting();

    void ignoreProximitySensorUntilChanged();

    boolean isProximitySensorAvailable();

    void onBootCompleted();

    void onDisplayChanged(com.android.server.display.HighBrightnessModeMetadata highBrightnessModeMetadata, int i);

    void onSwitchUser(int i, int i2, float f);

    void overrideDozeScreenState(int i, int i2);

    void persistBrightnessTrackerState();

    void removeDisplayBrightnessFollower(com.android.server.display.DisplayPowerControllerInterface displayPowerControllerInterface);

    boolean requestPowerState(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, boolean z);

    void setAmbientColorTemperatureOverride(float f);

    void setAutoBrightnessLoggingEnabled(boolean z);

    void setAutomaticScreenBrightnessMode(int i);

    void setBrightness(float f);

    void setBrightness(float f, int i);

    void setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration brightnessConfiguration, boolean z);

    void setBrightnessFromOffload(float f);

    void setBrightnessToFollow(float f, float f2, float f3, boolean z);

    void setDisplayOffloadSession(android.hardware.display.DisplayManagerInternal.DisplayOffloadSession displayOffloadSession);

    void setDisplayWhiteBalanceLoggingEnabled(boolean z);

    void setTemporaryAutoBrightnessAdjustment(float f);

    void setTemporaryBrightness(float f);

    void stop();
}
