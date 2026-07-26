package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowStateAnimatorExt {
    default boolean prepareSurfaceLocked(com.android.server.wm.WindowState w) {
        return false;
    }

    default boolean setStartingWindowExitAnimation(int transit, com.android.server.wm.WindowState win) {
        return false;
    }

    default void skipWindowAnimation(boolean isEntrance, com.android.server.wm.WindowState win, android.view.animation.Animation a) {
    }

    default boolean waitDrawingCompleted(com.android.server.wm.WindowState windowState, android.content.Context context) {
        return false;
    }

    default void notifyWindowSurfaceShown(com.android.server.wm.WindowState win) {
    }

    default void destoryCompactDimmer(com.android.server.wm.WindowState mWin) {
    }

    default boolean shouldSkipOrientation(boolean isWallpaper, com.android.server.wm.WindowState win) {
        return true;
    }

    default void adjustMultiSearchAnimation(boolean isEntrance, com.android.server.wm.WindowState win, android.view.animation.Animation a) {
    }

    default boolean hideForUnFolded(com.android.server.wm.WindowState w) {
        return false;
    }

    default boolean skipWindowAnimationIfNeed(int transit, boolean isEntrance, com.android.server.wm.WindowState win) {
        return false;
    }

    default void addStartingBackColorLayerIfNeed(com.android.server.wm.WindowState win) {
    }

    default void notifyWinSurfaceShow(com.android.server.wm.WindowState win, boolean lastHidden) {
    }
}
