package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IMirageWindowManagerExt {
    public static final int DISPLAY_ID = 2020;

    default void applySurfacePrivacyProtectionPolicy(boolean surfaceShownChanged, boolean surfaceShown, com.android.server.wm.WindowState window) {
    }

    default void onTaskAdded(int displayId, com.android.server.wm.Task task) {
    }

    default void onTaskRemoved(int displayId, com.android.server.wm.Task task) {
    }

    default boolean shouldReparentToNull(int taskId) {
        return false;
    }

    default void onDisplayAdded(int displayId) {
    }

    default void onDisplayRemoved(int displayId) {
    }

    default boolean onGoingToSleep(int displayId) {
        return true;
    }

    default boolean shouldHideTaskInRecents(com.android.server.wm.Task task) {
        return false;
    }

    default boolean shouldForceLauncherVisible() {
        return false;
    }
}
