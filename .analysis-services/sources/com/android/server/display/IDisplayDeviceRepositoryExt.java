package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayDeviceRepositoryExt {
    default boolean interceptDisplayDeviceAdded(java.util.List<com.android.server.display.DisplayDevice> mDisplayDevices, com.android.server.display.DisplayDeviceInfo info) {
        return false;
    }

    default void handleDisplayDeviceAdded(java.lang.String packageName, int uid) {
    }

    default void handleDisplayDeviceRemoved(java.lang.String packageName, int uid) {
    }

    default void onDisplayRemoved(com.android.server.display.DisplayDevice device) {
    }

    default void onDisplayDeviceEvent(com.android.server.display.DisplayDevice device, int event, long now, long timestamp) {
    }

    default void handleDisplayDeviceAdded(com.android.server.display.DisplayDevice device) {
    }

    default void handleDisplayDeviceChanged(com.android.server.display.DisplayDevice device, int diff, com.android.server.display.DisplayDeviceInfo lastInfo) {
    }

    default void handleDisplayDeviceRemoved(com.android.server.display.DisplayDevice device) {
    }
}
