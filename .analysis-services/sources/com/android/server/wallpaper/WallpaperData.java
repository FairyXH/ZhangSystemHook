package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperData {
    boolean allowBackup;
    android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> callbacks;
    com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection;
    final android.graphics.Rect cropHint;
    public boolean fromForegroundApp;
    boolean imageWallpaperPending;
    long lastDiedTime;
    com.android.server.wallpaper.WallpaperData.BindSource mBindSource;
    private final android.util.SparseArray<java.io.File> mCropFiles;
    android.util.SparseArray<android.graphics.Rect> mCropHints;
    boolean mIsColorExtractedFromDim;
    int mOrientationWhenSet;
    float mSampleSize;
    boolean mSystemWasBoth;
    android.util.SparseArray<java.lang.Float> mUidToDimAmount;
    public com.android.server.wallpaper.IWallpaperDataExt mWallpaperDataExt;
    float mWallpaperDimAmount;
    private final android.util.SparseArray<java.io.File> mWallpaperFiles;
    int mWhich;
    java.lang.String name;
    android.content.ComponentName nextWallpaperComponent;
    android.app.WallpaperColors primaryColors;
    android.app.IWallpaperManagerCallback setComplete;
    final int userId;
    android.content.ComponentName wallpaperComponent;
    int wallpaperId;
    com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver wallpaperObserver;
    boolean wallpaperUpdating;

    enum BindSource {
        UNKNOWN,
        CONNECT_LOCKED,
        CONNECTION_TRY_TO_REBIND,
        INITIALIZE_FALLBACK,
        PACKAGE_UPDATE_FINISHED,
        RESTORE_SETTINGS_LIVE_FAILURE,
        RESTORE_SETTINGS_LIVE_SUCCESS,
        RESTORE_SETTINGS_STATIC,
        SET_LIVE,
        SET_LIVE_TO_CLEAR,
        SET_STATIC,
        SWITCH_WALLPAPER_FAILURE,
        SWITCH_WALLPAPER_SWITCH_USER,
        SWITCH_WALLPAPER_UNLOCK_USER
    }

    WallpaperData(int userId, int wallpaperType) {
        this.name = "";
        this.mWallpaperDimAmount = 0.0f;
        this.mUidToDimAmount = new android.util.SparseArray<>();
        this.callbacks = new android.os.RemoteCallbackList<>();
        this.cropHint = new android.graphics.Rect(0, 0, 0, 0);
        this.mSampleSize = 1.0f;
        this.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.UNKNOWN;
        this.mWallpaperFiles = new android.util.SparseArray<>();
        this.mCropFiles = new android.util.SparseArray<>();
        this.mWallpaperDataExt = (com.android.server.wallpaper.IWallpaperDataExt) system.ext.loader.core.ExtLoader.type(com.android.server.wallpaper.IWallpaperDataExt.class).base(this).create();
        this.mCropHints = new android.util.SparseArray<>();
        this.mOrientationWhenSet = -1;
        this.userId = userId;
        this.mWhich = wallpaperType;
    }

    WallpaperData(com.android.server.wallpaper.WallpaperData source) {
        this.name = "";
        this.mWallpaperDimAmount = 0.0f;
        this.mUidToDimAmount = new android.util.SparseArray<>();
        this.callbacks = new android.os.RemoteCallbackList<>();
        this.cropHint = new android.graphics.Rect(0, 0, 0, 0);
        this.mSampleSize = 1.0f;
        this.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.UNKNOWN;
        this.mWallpaperFiles = new android.util.SparseArray<>();
        this.mCropFiles = new android.util.SparseArray<>();
        this.mWallpaperDataExt = (com.android.server.wallpaper.IWallpaperDataExt) system.ext.loader.core.ExtLoader.type(com.android.server.wallpaper.IWallpaperDataExt.class).base(this).create();
        this.mCropHints = new android.util.SparseArray<>();
        this.mOrientationWhenSet = -1;
        this.userId = source.userId;
        this.wallpaperComponent = source.wallpaperComponent;
        this.mWhich = source.mWhich;
        this.wallpaperId = source.wallpaperId;
        this.cropHint.set(source.cropHint);
        if (source.mCropHints != null) {
            this.mCropHints = source.mCropHints.clone();
        }
        this.allowBackup = source.allowBackup;
        this.primaryColors = source.primaryColors;
        this.mWallpaperDimAmount = source.mWallpaperDimAmount;
        this.connection = source.connection;
        if (this.connection != null) {
            this.connection.mWallpaper = this;
        }
        this.mWallpaperDataExt = source.mWallpaperDataExt;
    }

    java.io.File getWallpaperFile() {
        java.lang.String fileName = this.mWallpaperDataExt.getWallpaperType(this.mWhich) == 2 ? "wallpaper_lock_orig" : "wallpaper_orig";
        return getFile(this.mWallpaperFiles, fileName);
    }

    java.io.File getCropFile() {
        java.lang.String fileName = this.mWallpaperDataExt.getWallpaperType(this.mWhich) == 2 ? "wallpaper_lock" : com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER;
        return getFile(this.mCropFiles, fileName);
    }

    private java.io.File getFile(android.util.SparseArray<java.io.File> map, java.lang.String fileName) {
        java.io.File result = map.get(this.mWallpaperDataExt.getWallpaperType(this.mWhich));
        if (result == null) {
            java.io.File wallpaperDir = this.mWallpaperDataExt.getWallpaperDirWithPhysicalDisplayId(this.userId, this.mWallpaperDataExt.getPhysicalDisplayId(), com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(this.userId));
            java.io.File result2 = new java.io.File(wallpaperDir, fileName);
            map.put(this.userId, result2);
            return result2;
        }
        return result;
    }

    public java.lang.String toString() {
        final java.lang.StringBuilder out = new java.lang.StringBuilder(defaultString(this));
        out.append(", id: ");
        out.append(this.wallpaperId);
        out.append(", which: ");
        out.append(this.mWhich);
        out.append(", userId: ");
        out.append(this.userId);
        out.append(", wallpaperComponent: ");
        out.append(this.wallpaperComponent != null ? this.wallpaperComponent.flattenToShortString() : "null");
        out.append(", nextWallpaperComponent: ");
        out.append(this.nextWallpaperComponent != null ? this.nextWallpaperComponent.flattenToShortString() : "null");
        out.append(", file mod: ");
        out.append(getWallpaperFile() != null ? java.lang.Long.valueOf(getWallpaperFile().lastModified()) : "null");
        if (this.connection == null) {
            out.append(", no connection");
        } else {
            out.append(", info: ");
            out.append(this.connection.mInfo);
            out.append(", engine(s):");
            this.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperData$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wallpaper.WallpaperData.lambda$toString$0(out, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                }
            });
        }
        out.append(this.mWallpaperDataExt.toString());
        return out.toString();
    }

    static /* synthetic */ void lambda$toString$0(java.lang.StringBuilder out, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        if (connector.mEngine != null) {
            out.append(" ");
            out.append(defaultString(connector.mEngine));
        } else {
            out.append(" null");
        }
        out.append(", connector.mToken: ");
        out.append(connector.mToken);
    }

    private static java.lang.String defaultString(java.lang.Object o) {
        return o.getClass().getSimpleName() + "@" + java.lang.Integer.toHexString(o.hashCode());
    }

    boolean cropExists() {
        return getCropFile().exists();
    }

    boolean sourceExists() {
        return getWallpaperFile().exists();
    }
}
