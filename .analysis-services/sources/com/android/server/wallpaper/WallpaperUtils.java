package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperUtils {
    static final java.lang.String RECORD_FILE = "decode_record";
    static final java.lang.String RECORD_LOCK_FILE = "decode_lock_record";
    static final java.lang.String WALLPAPER_CROP = "wallpaper";
    private static int sWallpaperId;
    static final int DEBUG_DEPTH = android.os.SystemProperties.getInt("debug.wallpaper.depth", 10);
    static boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final java.lang.String WALLPAPER = "wallpaper_orig";
    static final java.lang.String WALLPAPER_LOCK_ORIG = "wallpaper_lock_orig";
    static final java.lang.String WALLPAPER_LOCK_CROP = "wallpaper_lock";
    static final java.lang.String WALLPAPER_INFO = "wallpaper_info.xml";
    private static final java.lang.String[] sPerUserFiles = {WALLPAPER, "wallpaper", WALLPAPER_LOCK_ORIG, WALLPAPER_LOCK_CROP, WALLPAPER_INFO};

    WallpaperUtils() {
    }

    static java.io.File getWallpaperDir(int userId) {
        return android.os.Environment.getUserSystemDirectory(userId);
    }

    static int makeWallpaperIdLocked() {
        do {
            sWallpaperId++;
        } while (sWallpaperId == 0);
        return sWallpaperId;
    }

    static int getCurrentWallpaperId() {
        return sWallpaperId;
    }

    static void setCurrentWallpaperId(int id) {
        sWallpaperId = id;
    }

    static java.util.List<java.io.File> getWallpaperFiles(int userId) {
        java.io.File wallpaperDir = getWallpaperDir(userId);
        java.util.List<java.io.File> result = new java.util.ArrayList<>();
        for (int i = 0; i < sPerUserFiles.length; i++) {
            result.add(new java.io.File(wallpaperDir, sPerUserFiles[i]));
        }
        return result;
    }
}
