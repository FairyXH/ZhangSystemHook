package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface ILedExt {
    default void systemReady() {
    }

    default void handleScreenState(boolean screenon) {
    }

    default void setDebugSwitch(boolean on) {
    }

    default boolean isIgnoreUpdateLights(android.hardware.health.HealthInfo healthInfo) {
        return false;
    }

    default void turnOffBatteryLights() {
    }

    default void initLedExtImpl(android.content.Context context, com.android.server.lights.LightsManager lightsManager, java.lang.Object batteryLed) {
    }
}
