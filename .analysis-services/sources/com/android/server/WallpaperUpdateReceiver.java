package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class WallpaperUpdateReceiver extends android.content.BroadcastReceiver {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "WallpaperUpdateReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        if (intent != null && "android.intent.action.DEVICE_CUSTOMIZATION_READY".equals(intent.getAction())) {
            android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.WallpaperUpdateReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.updateWallpaper();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWallpaper() {
        try {
            android.app.ActivityThread currentActivityThread = android.app.ActivityThread.currentActivityThread();
            android.app.ContextImpl systemUiContext = currentActivityThread.getSystemUiContext();
            android.app.WallpaperManager wallpaperManager = android.app.WallpaperManager.getInstance(systemUiContext);
            if (isUserSetWallpaper(wallpaperManager, systemUiContext)) {
                android.util.Slog.i(TAG, "User has set wallpaper, skip to resetting");
            } else if (wallpaperManager.getWallpaperInfo() == null) {
                wallpaperManager.clearWallpaper();
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Failed to customize system wallpaper." + e);
        }
    }

    private boolean isUserSetWallpaper(android.app.WallpaperManager wm, android.content.Context context) {
        android.app.WallpaperInfo info = wm.getWallpaperInfo();
        if (info == null) {
            android.os.ParcelFileDescriptor sysWallpaper = wm.getWallpaperFile(1);
            android.os.ParcelFileDescriptor lockWallpaper = wm.getWallpaperFile(2);
            if (sysWallpaper != null || lockWallpaper != null) {
                return true;
            }
            return false;
        }
        android.content.ComponentName currCN = info.getComponent();
        android.content.ComponentName defaultCN = android.app.WallpaperManager.getCmfDefaultWallpaperComponent(context);
        if (!currCN.equals(defaultCN)) {
            return true;
        }
        return false;
    }
}
