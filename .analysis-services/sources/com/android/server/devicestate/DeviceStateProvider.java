package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
public interface DeviceStateProvider extends android.util.Dumpable {
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_DEFAULT = 0;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_EXTERNAL_DISPLAY_ADDED = 6;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_EXTERNAL_DISPLAY_REMOVED = 7;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_INITIALIZED = 1;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_POWER_SAVE_DISABLED = 5;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_POWER_SAVE_ENABLED = 4;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_THERMAL_CRITICAL = 3;
    public static final int SUPPORTED_DEVICE_STATES_CHANGED_THERMAL_NORMAL = 2;

    public interface Listener {
        void onStateChanged(int i);

        void onSupportedDeviceStatesChanged(android.hardware.devicestate.DeviceState[] deviceStateArr, int i);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SupportedStatesUpdatedReason {
    }

    void notifyKeyguardShowOrSleep(boolean z);

    void registerSensor();

    void setListener(com.android.server.devicestate.DeviceStateProvider.Listener listener);
}
