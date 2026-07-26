package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public interface IWallpaperManagerServiceSeparateExt {
    public static final int[] ALL_PHYSICAL_DISPLAY_IDS = {1, 2};
    public static final int PHYSICAL_DISPLAY_MAIN = 1;
    public static final int PHYSICAL_DISPLAY_SUB = 2;

    default void initSeparateWallpaperForMultiDisplay(android.content.Context context) {
    }

    default java.io.File getWallpaperDir(int userId, int physicalDisplayId, java.io.File defWallpaperDir) {
        return defWallpaperDir;
    }

    default int getWhichValue(int wallpaperType, int physicalDisplayId) {
        return wallpaperType;
    }

    default int getWhichValue(int wallpaperType, int[] physicalDisplayIds) {
        return wallpaperType;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getWallpaperMapByWhich(int which, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> defaultMap) {
        return defaultMap;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getWallpaperMap(int wallpaperType, int physicalDisplayId, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> defaultMap) {
        return defaultMap;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getWallpaperMapLock(com.android.server.wallpaper.WallpaperData wallpaperData, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> defaultMap) {
        return defaultMap;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getWallpaperMapSys(com.android.server.wallpaper.WallpaperData wallpaperData, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> defaultMap) {
        return defaultMap;
    }

    default int getCurrentPhysicalDisplayIdLocked() {
        return 1;
    }

    default android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getCurrentWallpaperMap(int wallpaperType, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> defaultMap) {
        return defaultMap;
    }

    default void setIsMainDisplayWallpaperChangeLocked(com.android.server.wallpaper.WallpaperData wpData, int which) {
    }

    default boolean loadSettingsLocked(int userId, boolean keepDimensionHints, int physicalDisplayId, int which) {
        return false;
    }

    default int getCachePhysicalDisplayId() {
        return 1;
    }

    default void onWrittenEventStart(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }

    default void onWrittenEventEnd(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }

    default boolean saveSettingsLocked(int userId, int physicalDisplayId) {
        return false;
    }

    default boolean saveSettingsLockedForAffectedPhysicalDisplays(int userId, int which) {
        return false;
    }

    default boolean saveSettingsLockedOnServiceConnected(int userId, int physicalDisplayId) {
        return false;
    }

    default void copyWallpaperColorsToOtherPhysicalDisplaysLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    default void finalizeSubDisplay() {
    }

    default java.util.List<com.android.server.wallpaper.WallpaperData> getWallpaperForAllPhysicalDisplay(int userId, int wallpaperType, com.android.server.wallpaper.WallpaperData[] wallpaperDatas) {
        return java.util.Arrays.asList(wallpaperDatas);
    }

    default void stopSubDisplayObserversLocked(int userId) {
    }

    default void restoreconSubDisplayFiles(int userId) {
    }

    default void deleteSubDisplayFiles(int userId) {
    }

    default void initOnUserSwitch(int userId) {
    }

    default boolean hasNamedSubWallpaperForUser(int userId, java.lang.String name) {
        return false;
    }

    default boolean isNotAvailableWhichWithSinglePhysicalDisplayFlag(int which) {
        return (which == 1 || which == 2) ? false : true;
    }

    default int getPhysicalDisplayIdLocked(int which) {
        return 1;
    }

    default int getWallpaperType(int which) {
        return which;
    }

    default void forEachPhysicalDisplayWallpaperLocked(int userId, int wallpaperType, java.util.function.Consumer<com.android.server.wallpaper.WallpaperData> action) {
    }

    default com.android.server.wallpaper.WallpaperData getWallpaperSafeLocked(int userId, int wallpaperType, int physicalDisplayId) {
        return null;
    }

    default com.android.server.wallpaper.WallpaperData getWallpaperSafeLocked(int userId, int which) {
        return null;
    }

    default int formatWhichPending(int which) {
        return which;
    }

    default int formatWhichClear(int which) {
        return which;
    }

    default int getDisplayIdFromPhysicalDisplayId(int physicalDisplayId, android.hardware.display.DisplayManager displayManager) {
        return 0;
    }

    default boolean detachSharedWallpaperLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
        return false;
    }

    default void removeLastWallpaperLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    default boolean bindSharedWallpaperComponentLocked(android.content.ComponentName componentName, boolean force, boolean fromUser, com.android.server.wallpaper.WallpaperData curWallpaper, com.android.server.wallpaper.WallpaperData wallpaper, android.os.IRemoteCallback reply) {
        return false;
    }

    default boolean maybeDetachLastWallpapers(int currentUserId, com.android.server.wallpaper.WallpaperData curWallpaper, com.android.server.wallpaper.WallpaperData fallbackWallpaper) {
        return false;
    }

    default boolean setLastWallpaper(int currentUserId, com.android.server.wallpaper.WallpaperData curWallpaper, com.android.server.wallpaper.WallpaperData fallbackWallpaper) {
        return false;
    }

    default boolean needAttachService(com.android.server.wallpaper.WallpaperData wallpaperData) {
        return true;
    }

    default boolean bindWallpaperComponentOnImageChangedLocked(com.android.server.wallpaper.WallpaperData wallpaper, android.os.IRemoteCallback reply) {
        return false;
    }

    default void switchWallpaperForOtherPhysicalDisplay(int userId, boolean isForceSwitch) {
    }

    default boolean bindWallpaperComponentCheck(android.content.ComponentName componentName, boolean force, boolean fromUser, com.android.server.wallpaper.WallpaperData wallpaper, android.os.IRemoteCallback reply, int cacheDisplayId) {
        return false;
    }

    default boolean bindWallpaperComponentLocked(android.content.ComponentName componentName, boolean force, boolean fromUser, com.android.server.wallpaper.WallpaperData wallpaper, android.os.IRemoteCallback reply, int displayFlag) {
        return false;
    }

    default void initWallpaperOnBindWallpaperComponentLocked(com.android.server.wallpaper.WallpaperData wallpaperData) {
    }

    default boolean onServiceDisconnected(com.android.server.wallpaper.WallpaperData wallpaper) {
        return false;
    }

    default boolean isCurrentPhysicalDisplayWallpaper(com.android.server.wallpaper.WallpaperData wallpaper) {
        return true;
    }

    default com.android.server.wallpaper.WallpaperData newDirectBootAwareFallbackWallpaper(com.android.server.wallpaper.WallpaperData baseWallpaper, java.util.function.Supplier<com.android.server.wallpaper.WallpaperData> defaultFallback) {
        return defaultFallback.get();
    }

    default boolean clearWallpaperLocked(int wallpaperType, int userId, boolean fromForeground, android.os.IRemoteCallback reply, int physicalDisplayId) {
        return false;
    }

    default boolean clearWallpaperLockedForComponent(int wallpaperType, int userId, boolean fromForeground, android.os.IRemoteCallback reply, android.content.ComponentName componentName) {
        return false;
    }

    default boolean clearWallpaperLockedForMultiPhysicalDisplays(int which, int userId, boolean fromForeground, android.os.IRemoteCallback reply) {
        return false;
    }

    default boolean isCurrentPhysicalDisplayWallpaperChangedLocked(int which) {
        return true;
    }

    default int getBindWallpaperServiceFlag(int baseFlags, com.android.server.wallpaper.WallpaperData wallpaper) {
        return baseFlags;
    }

    default void updateWallpaperBeforeTryingToRebind(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    default boolean shouldNotifyCallbacks(com.android.server.wallpaper.WallpaperData wallpaper) {
        return false;
    }

    default void setFoldWallpaperComponentChecked(android.content.ComponentName name, java.lang.String callingPackage, int userId, int which) {
    }

    default void initExt() {
    }

    default void registerWallpaperCallbacksToOtherPhysicalDisplays(int which, android.app.IWallpaperManagerCallback cb, com.android.server.wallpaper.WallpaperData defaultWallpaper) {
    }

    default android.view.Display[] getDisplays(android.hardware.display.DisplayManager displayManager, com.android.server.wallpaper.WallpaperData wallpaper) {
        if (displayManager == null) {
            return new android.view.Display[0];
        }
        return displayManager.getDisplays();
    }

    default boolean isUsableDisplay(android.view.Display display) {
        return false;
    }

    default void detachOtherPhysicalDisplaysWallpaper(int userId, com.android.server.wallpaper.WallpaperData curSysWallpaper, int lastUserId) {
    }

    default android.app.WallpaperInfo getFoldWallpaperInfo(int userId, int which) {
        return null;
    }

    default com.android.server.wallpaper.WallpaperData findWallpaperAtDisplay(int userId, int displayId, int which, com.android.server.wallpaper.WallpaperData defaultWallpaper) {
        return defaultWallpaper;
    }

    default int getPhysicalDisplayIdFromDisplayIdLocked(int displayId) {
        return 1;
    }

    default boolean isAvailableFallbackDisplay(android.view.Display display) {
        return true;
    }

    default boolean notifyWallpaperColorsChanged(com.android.server.wallpaper.WallpaperData wallpaper, int which) {
        return false;
    }

    default java.io.File getRecordFile(com.android.server.wallpaper.WallpaperData cropWallpaper, java.lang.String recordName, java.io.File defaultFile) {
        return defaultFile;
    }

    default void subDisplayErrorCheck(int userId, int type, java.lang.String fileName) {
    }

    default com.android.server.wallpaper.WallpaperData getWallpaperForWallpaperColors(int which, int userId, int displayId) {
        return null;
    }

    default boolean clearWallpaperComponentLockedOnPackageUpdated(com.android.server.wallpaper.WallpaperData wallpaper) {
        return false;
    }

    default void resetOnPackageUpdatedLocked() {
    }

    default java.util.List<com.android.server.wallpaper.WallpaperData> getWallpapersLocked(int wallpaperType, android.util.SparseArray<com.android.server.wallpaper.WallpaperData> defaultWallpaperMap) {
        if (defaultWallpaperMap == null) {
            return java.util.Collections.emptyList();
        }
        java.util.List<com.android.server.wallpaper.WallpaperData> wallpapers = new java.util.ArrayList<>(defaultWallpaperMap.size());
        for (int i = 0; i < defaultWallpaperMap.size(); i++) {
            wallpapers.add(defaultWallpaperMap.valueAt(i));
        }
        return wallpapers;
    }

    default boolean delayRebindOnCrashTimeout(com.android.server.wallpaper.WallpaperData wallpaper, com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
        return false;
    }

    default boolean setDecoderSampleSize(android.graphics.ImageDecoder decoder, int sampleSize, android.graphics.BitmapFactory.Options options) {
        return false;
    }

    default boolean setWallpaperComponent(android.content.ComponentName name, java.lang.String callingPackage, int which, int userId) {
        return false;
    }

    default void setFromForegroundAppLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
    }

    default boolean forEachAvailableDisplayConnector(android.util.SparseArray<com.android.server.wallpaper.WallpaperManagerService.DisplayConnector> connectors, com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection, java.util.function.Consumer<com.android.server.wallpaper.WallpaperManagerService.DisplayConnector> action) {
        return false;
    }

    default boolean isAvailableSetWallpaperFlagOnBind(com.android.server.wallpaper.WallpaperData wallpaper, android.app.WallpaperInfo wallpaperInfo) {
        return true;
    }

    default boolean rebindWallpaperComponent(android.content.ComponentName componentName, int physicalId) {
        return false;
    }

    default boolean unBindServiceForSeparateWallpaper(android.content.Context context, com.android.server.wallpaper.WallpaperData wallpaper) {
        return false;
    }

    default int doRectifyWhich(int which) {
        return which;
    }

    default void copySysWallpaperToOtherPhysicalDisplaysLocked(com.android.server.wallpaper.WallpaperData changedWallpaper, int flag) {
    }

    default void deleteStaticLockWallpaper(com.android.server.wallpaper.WallpaperData changedWallpaper, int flag) {
    }

    default boolean migrateStaticSystemToLockWallpaperLocked(int userId, int which) {
        return false;
    }

    default boolean onSystemAndLockToLock(com.android.server.wallpaper.WallpaperData wallpaperData) {
        return false;
    }

    default boolean onStaticSystemAndLockToSys(com.android.server.wallpaper.WallpaperData wallpaperData) {
        return false;
    }

    default boolean onLiveSystemAndLockToSys(com.android.server.wallpaper.WallpaperData wallpaperData) {
        return false;
    }

    default boolean isSharedConnectionWallpaperWithSameFlag(com.android.server.wallpaper.WallpaperData wallpaperData) {
        return false;
    }

    default com.android.server.wallpaper.WallpaperManagerService.WallpaperDestinationChangeHandler getPendingMigrationViaStatic(com.android.server.wallpaper.WallpaperManagerService.WallpaperDestinationChangeHandler orgin, int phyID) {
        return orgin;
    }

    default void putPendingMigrationViaStatic(com.android.server.wallpaper.WallpaperManagerService.WallpaperDestinationChangeHandler handler, int phyID) {
    }

    default void removePendingMigrationViaStatic(int phyID) {
    }
}
