package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface ILogicalDisplayExt {
    default void setDisplayInfoFlags(com.android.server.display.DisplayDeviceInfo deviceInfo, android.view.DisplayInfo displayInfo, int displayId) {
    }

    default boolean isSecondaryDisplayEnabled() {
        return false;
    }

    default void setSecondaryDisplayEnabled(boolean secondaryDisplayEnabled) {
    }

    default boolean isMirageMirrorMode(int displayId) {
        return false;
    }

    default boolean adjustRotatedForMirage(boolean rotated, int displayId, android.view.DisplayInfo displayInfo) {
        return rotated;
    }

    default boolean isAlwaysRotateDisplayDeviceEnabled(boolean alwaysRotateDisplayDeviceEnabled, com.android.server.display.DisplayDevice device, android.view.DisplayInfo displayInfo) {
        return alwaysRotateDisplayDeviceEnabled;
    }
}
