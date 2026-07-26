package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
interface IWallpaperManagerService extends android.app.IWallpaperManager, android.os.IBinder {
    void onBootPhase(int i);

    void onUnlockUser(int i);
}
