package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAbsAppSnapshotControllerExt {
    default int drawAppThemeSnapshot(int color, com.android.server.wm.Task task) {
        return color;
    }

    default void snapshotTask(com.android.server.wm.Task task, android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer) {
    }

    default android.window.ScreenCapture.ScreenshotHardwareBuffer createTaskSnapshot(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity, android.graphics.Rect rect, float scaleFraction, int pixelFormat, android.view.SurfaceControl[] excludeLayers) {
        return null;
    }

    default boolean isActivityTypeMultiSearch(int activityType) {
        return false;
    }

    default void prepareTaskSnapshot(android.graphics.Rect contentInsets, com.android.server.wm.ActivityRecord activity, com.android.server.wm.WindowState mainWindow) {
    }

    default boolean isSecondScreenOn(com.android.server.policy.WindowManagerPolicy policy) {
        return false;
    }

    default boolean snapshotForScreenOffActPreload(com.android.server.wm.Task task) {
        return false;
    }

    default boolean skipDrawAppThemeSnapshot(com.android.server.wm.ActivityRecord topActivity) {
        return false;
    }
}
