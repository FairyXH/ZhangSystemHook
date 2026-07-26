package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public interface IWallpaperManagerServiceWrapper {
    default void removeDisplayData(int displayId) {
    }

    default android.content.Context getContext() {
        return null;
    }

    default java.lang.Object getLock() {
        return new java.lang.Object();
    }

    default android.content.ComponentName getImageWallpaper() {
        return new android.content.ComponentName("", "");
    }

    default void notifyCallbacksLocked(com.android.server.wallpaper.WallpaperData wpData) {
    }

    default int getCurrentUserId() {
        return -10000;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getWallpaperMap() {
        return new android.util.SparseArray<>();
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getLockWallpaperMap() {
        return new android.util.SparseArray<>();
    }

    default void loadSettingsLocked(int userId, boolean keepDimensionHints, int which) {
    }

    default com.android.server.wallpaper.WallpaperData getFallbackWallpaper() {
        return null;
    }

    default void notifyWallpaperColorsChangedOnDisplay(com.android.server.wallpaper.WallpaperData wallpaper, int which, int displayId) {
    }

    default java.lang.String[] getPerUserFiles() {
        return new java.lang.String[0];
    }

    default void clearWallpaperComponentLocked(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }

    default void detachWallpaperLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    default void updateFallbackConnection() {
    }

    default com.android.server.wallpaper.IWallpaperManagerServiceExt getExtImpl() {
        return null;
    }

    default android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> getWallpaperCallbacks(com.android.server.wallpaper.WallpaperData wallpaper) {
        return new android.os.RemoteCallbackList<>();
    }

    default void setWallpaperComponent(android.content.ComponentName name, int userId, int which) {
    }

    default android.content.ComponentName getDefaultWallpaperComponent() {
        return null;
    }

    default boolean hasPermission(java.lang.String permission) {
        return false;
    }

    default com.android.server.wallpaper.WallpaperData findWallpaperAtDisplay(int userId, int displayId) {
        return null;
    }

    default boolean changingToSame(android.content.ComponentName componentName, com.android.server.wallpaper.WallpaperData wallpaper) {
        return false;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperManagerService.DisplayConnector> getDisplayConnectors(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
        return new android.util.SparseArray<>();
    }

    default void initDisplayState(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
    }

    default void removeCallback(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }

    default android.app.ActivityManager getActivityManager() {
        return null;
    }

    default android.hardware.display.DisplayManager getDisplayManager() {
        return null;
    }

    default void updateLogState(boolean on) {
    }

    default void notifyLockWallpaperChanged() {
    }

    default void clearWallpaperLocked(int which, int userId, boolean fromForeground, android.os.IRemoteCallback reply) {
    }

    default void scheduleTimeoutLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    default void clearWallpaperBitmaps(int userID, int wallpaperType) {
    }

    default void migrateStaticSystemToLockWallpaperLocked(int userId, int phyDisplayId) {
    }

    default void updateEngineFlags(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }
}
