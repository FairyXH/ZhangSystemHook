package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWallpaperControllerExt {
    default float checkWallpaperOffsetX(android.content.Context context, com.android.server.wm.WindowState wallpaperWin, boolean sync, float offset) {
        return offset;
    }

    default void handleWallpaperCreated(com.android.server.wm.DisplayContent displayContent) {
    }

    default com.oplus.wallpaper.IWallpaperCallbackExt getCallback() {
        return null;
    }

    default void dispatchWallpaperWindowsTarget(com.android.server.wm.WindowState wallpaperTarget, com.android.server.wm.DisplayContent mDisplayContent, boolean visible) {
    }

    default void removeWallpaperWindows() {
    }

    default void dispatchWallpaperWindowsForRegister() {
    }

    default boolean sendWindowWallpaperCommand(com.android.server.wm.WindowState window, java.lang.String action, android.os.Bundle extras, boolean sync) {
        return false;
    }

    default boolean skipHideSecondaryWallpaper(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default void computeLastWallpaperScaleValue() {
    }

    default float getLastWallpaperScaleValue() {
        return 1.0f;
    }

    default void updateWallpaperParameters(com.android.server.wm.WindowState wallpaper, android.os.Bundle extras) {
    }

    default boolean skipHideWallpaper(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean isRecentsTargetShellEnable(com.android.server.wm.WindowState w) {
        return false;
    }

    default void forceRestoreWallpaperScale(com.android.server.wm.WindowState window) {
    }

    default void screenshotWallpaper() {
    }

    default void setLockWallpaperZoomOut(com.android.server.wm.WindowState w, float zoom) {
    }

    default void forceReLayoutWhenVisible() {
    }
}
