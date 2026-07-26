package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRecentsAnimationControllerExt {
    default void markTaskNoAnimation(int taskId) {
    }

    default void hooksetPosition(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, com.android.server.wm.SurfaceAnimator mSurfaceAnimator, float x, float y) {
    }

    default void hooksetWindowCrop(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, com.android.server.wm.SurfaceAnimator mSurfaceAnimator, android.graphics.Rect mTmpRect) {
    }

    default boolean isZoomWindowMode(int windowMode) {
        return false;
    }

    default void resetZoomAnimationFinished(boolean moveHomeToTop) {
    }

    default android.view.RemoteAnimationTarget obtainLaunchViewInfoForRecents(com.android.server.wm.Task task, android.view.RemoteAnimationTarget originTarget) {
        return originTarget;
    }

    default void hideDisplaySwitchNotification(com.android.server.wm.Task task, boolean forceClearNotification) {
    }

    default void adjustTouchableRegion(com.android.server.wm.WindowState targetAppMainWindow, android.graphics.Rect rect) {
    }

    default void finishPutt(int type, int taskId, android.graphics.Rect rect, int orientation, android.os.Bundle bOptions) {
    }

    default void adjustAnimationBounds(com.android.server.wm.Task task, android.graphics.Rect bounds) {
    }

    default boolean isInSplitRootTask(com.android.server.wm.Task task) {
        return false;
    }

    default com.android.server.wm.WallpaperController adjustWallpaperController(com.android.server.wm.WallpaperController originController, com.android.server.wm.DisplayContent dc) {
        return originController;
    }

    default void sendTasksAppeared(android.view.RemoteAnimationTarget[] targets) {
    }
}
