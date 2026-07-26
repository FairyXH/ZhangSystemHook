package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IMirageDisplayManagerExt {
    default int getLastAssignedDisplayId(com.android.server.display.DisplayDevice device) {
        return -1;
    }

    default void recordDisplayIdForDisplay(com.android.server.display.DisplayDevice device, int displayId) {
    }

    default int getBondDisplayIdLocked(int displayId, com.android.server.display.DisplayDeviceInfo info, boolean hasContent) {
        return -1;
    }

    default void init(com.android.server.display.OverlayDisplayAdapter displayAdapter, com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.os.Handler displayHandler, android.content.Context context, android.os.Handler uiHandler) {
    }

    default boolean isMirageCarMode(int displayId) {
        return false;
    }

    default boolean isMirageFixedOrientation(int displayId) {
        return false;
    }

    default int getMirageDisplayCastMode(int displayId) {
        return -1;
    }

    default boolean isMirageDisplayEnabled() {
        return false;
    }

    default boolean isMirageDisplay(int displayId) {
        return false;
    }

    default boolean isMirageBackgroundStreamMode(int displayId) {
        return false;
    }

    default boolean isMiragePcMode(int displayId) {
        return false;
    }

    default boolean isMirageTvMode(int displayId) {
        return false;
    }

    default void onMirrorOutputSurfaceOrientationChanged(int displayId) {
    }
}
