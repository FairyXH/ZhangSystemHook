package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public interface IWallpaperManagerServiceExt extends com.android.server.wallpaper.IWallpaperManagerServiceSeparateExt {
    default void recycleBitmapDecoderAndDeleteCrop(com.android.server.wallpaper.WallpaperData wallpaper, android.graphics.BitmapRegionDecoder decoder) {
    }

    default void initCustomizeWallpaper(android.content.Context context) {
    }

    default boolean isSetWallpaperAllowed(java.lang.String callingPackage, android.content.Context context) {
        return false;
    }

    default void removeDisplayData(int displayId, com.android.server.wallpaper.WallpaperManagerService wms) {
    }

    default boolean shouldCommetOriginCode() {
        return false;
    }

    default android.os.Handler getEventHandler(android.content.Context mContext) {
        return mContext.getMainThreadHandler();
    }

    default void onLoadSettingsEnd(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }

    default void onLockWallpaperChanged(android.content.Context context, int userId) {
    }

    default boolean needDefaultImageWallpaper(android.content.Context context, int userId) {
        return true;
    }

    default android.content.ComponentName getDefaultWallpaperComponent(android.content.Context context, int userId) {
        return null;
    }

    default void initWallpaperBitmap() {
    }

    default void updateWallpaperBitmap() {
    }

    default int getServiceUserId(int userId, android.content.ComponentName imageWallpaper, android.content.ComponentName componentName) {
        return userId;
    }

    default boolean ignoreFileEventForCopyLocked(com.android.server.wallpaper.WallpaperData wpData, int event) {
        return false;
    }

    default void checkSysChangedWhenSysAndLockIsLive(com.android.server.wallpaper.WallpaperData sysWP, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> lockWallpaperMap) {
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerServiceSeparateExt
    default void updateWallpaperBeforeTryingToRebind(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerServiceSeparateExt
    default boolean shouldNotifyCallbacks(com.android.server.wallpaper.WallpaperData wallpaper) {
        return true;
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerServiceSeparateExt
    default void initExt() {
    }

    default void saveMaximumSizeDimension(int displayId, int sizeDimension) {
    }

    default void clearFlipClockTextColorIfNeed(android.content.Context context, int which, int userId) {
    }

    default void clearFlipClockTextStyleIfNeed(android.content.Context context, int which, int userId) {
    }

    default boolean onServiceAttachedLocked(boolean doRevert, int userId, android.content.ComponentName attachWallpaperComponent) {
        return false;
    }

    default void removeEngineIfDisconnected(com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        if (connector != null) {
            connector.mEngine = null;
        }
    }

    default boolean needScaleUp(int sourceHeight, int displayHeight) {
        return false;
    }

    default void registerLogSwitchObserver(android.content.Context context) {
    }

    default boolean isWallpaperSupportBackup(int userId, int which) {
        return false;
    }

    default void updateSystemWallpaperParameter(com.android.server.wallpaper.WallpaperData system2, int newWhich) {
    }

    default void notifyLockWallpaperChanged(android.content.Context context, int userId, android.content.ComponentName name, boolean isLiveWallpaper) {
    }

    default void restoreDefaultThemeIfNeeded(android.content.Context context, int wallpaperType) {
    }

    default void setWallpapersCallingPackage(java.lang.String callingPackage) {
    }

    default java.lang.String getWallpapersCallingPackage() {
        return null;
    }

    default void onWallpaperClearEvent(android.content.ComponentName component, int which, int userId, java.lang.String reason) {
    }

    default void onUserSwitchComplete(int newUserId) {
    }

    default boolean checkServiceAvailable(android.content.Context context, android.content.pm.IPackageManager pm, android.content.ComponentName wpService, int userId) {
        return false;
    }

    default boolean setWallpaperComponent2(android.content.ComponentName name, int userId) throws android.os.RemoteException {
        return false;
    }

    default android.os.ParcelFileDescriptor getWallpaperScreenShot(int userid) {
        return null;
    }

    default void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default void onWallpaperChange() {
    }

    default boolean rebindWallpaperIfReset(com.android.server.wallpaper.WallpaperData wallpaper, boolean needTryToRebind) {
        return false;
    }
}
