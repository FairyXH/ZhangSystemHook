package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
interface DisplayUpdater {
    void updateDisplayInfo(java.lang.Runnable runnable);

    default void onDisplayContentDisplayPropertiesPreChanged(int displayId, int initialDisplayWidth, int initialDisplayHeight, int newWidth, int newHeight) {
    }

    default void onDisplayContentDisplayPropertiesPostChanged(int previousRotation, int newRotation, android.window.DisplayAreaInfo newDisplayAreaInfo) {
    }

    default void onDisplaySwitching(boolean switching) {
    }

    default boolean waitForTransition(android.os.Message screenUnBlocker) {
        return false;
    }
}
