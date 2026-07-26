package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public interface IWallpaperDataExt {
    public static final int SET_WALLPAPER_FLAG_COPY = 1;

    default void setPhysicalDisplayId(int physicalDisplayId) {
    }

    default int getPhysicalDisplayId() {
        return 1;
    }

    default void addSetWallpaperFlagLocked(int flag) {
    }

    default boolean hasSetWallpaperFlagLocked(int flag) {
        return false;
    }

    default int getSetWallpaperFlagLocked() {
        return 0;
    }

    default boolean resetSetWallpaperFlagLocked(int flag) {
        return false;
    }

    default int getWallpaperType(int which) {
        return which;
    }

    default int getPhysicalDisplayIdByWhich(int which) {
        return 1;
    }

    default java.io.File getWallpaperDir(int userId, int which, java.io.File defWallpaperDir) {
        return defWallpaperDir;
    }

    default java.io.File getWallpaperDirWithPhysicalDisplayId(int userId, int physicalDisplayId, java.io.File defWallpaperDir) {
        return defWallpaperDir;
    }
}
