package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IDeviceStateProviderImplExt {
    default void init(com.android.server.policy.DeviceStateProviderImpl base, android.content.Context context) {
    }

    default void onBootPhase(int phase) {
    }

    default void setNeededSensors(android.util.ArraySet<android.hardware.Sensor> sensors) {
    }

    default void registerSensor(android.hardware.SensorEventListener listener) {
    }

    default int getSensorDelay(int delay, java.lang.String type) {
        return 0;
    }

    default void notifyCreateSleepToken(java.lang.String tag) {
    }

    default void notifyRemoveSleepToken(java.lang.String tag) {
    }

    default boolean isNeedBreakSetDeviceState(int state) {
        return false;
    }

    default void notifyCreateSleepToken(java.lang.String tag, int displayId, android.view.Display display) {
    }

    default void notifyRemoveSleepToken(java.lang.String tag, int displayId, android.view.Display display) {
    }

    default int adjustDeviceState(int lastState, int newState, java.util.Map<android.hardware.Sensor, android.hardware.SensorEvent> latestSensorEvent, android.hardware.Sensor hingeSensor) {
        return newState;
    }

    default boolean getDisplayOn(int displayId) {
        return false;
    }

    default boolean isNeedInterceptDeviceState(int lastState, int newState) {
        return false;
    }

    default boolean unregisterSensorsIfLockStateChanged(int lastState, boolean keyguardShown) {
        return true;
    }

    default boolean isNeedAddSubGravitySensor(java.lang.String expectedSensorType) {
        return false;
    }

    default boolean isRemapDisabledDisplay() {
        return false;
    }
}
