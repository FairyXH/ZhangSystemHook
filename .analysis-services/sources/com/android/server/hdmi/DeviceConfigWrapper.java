package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceConfigWrapper {
    private static final java.lang.String TAG = "DeviceConfigWrapper";

    boolean getBoolean(java.lang.String name, boolean defaultValue) {
        return android.provider.DeviceConfig.getBoolean("hdmi_control", name, defaultValue);
    }

    void addOnPropertiesChangedListener(java.util.concurrent.Executor mainExecutor, android.provider.DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("hdmi_control", mainExecutor, onPropertiesChangedListener);
    }
}
