package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IZoomWindowManagerExt {
    public static final int TRANSIT_FLOAT_OPEN_TO_ZOOM = 102;
    public static final int TRANSIT_ZOOM_CLOSE = 1105;
    public static final int TRANSIT_ZOOM_CLOSE_TO_FLOAT = 103;
    public static final int TRANSIT_ZOOM_OPEN = 100;
    public static final int TYPE_FULL_ANIMATION = 105;
    public static final int TYPE_SCALE_ANIMATION = 104;
    public static final int UPDATE_MODE_APP_ORIENTATION_CHANGE = 1;

    default boolean checkInSideGestureHotZone(float x, float y) {
        return false;
    }

    default void gestureSwipeFromBottom() {
    }

    default boolean recentAnimationFinished(int taskId, int type, android.graphics.Rect rect, int orientation, android.os.Bundle bOptions, android.view.IRecentsAnimationController controller, boolean moveHomeToTop, boolean sendUserLeaveHint) {
        return false;
    }

    default void adjustInputWindowHandle(com.android.server.wm.InputMonitor monitor, com.android.server.wm.WindowState win, com.android.server.wm.InputWindowHandleWrapper handle) {
    }
}
