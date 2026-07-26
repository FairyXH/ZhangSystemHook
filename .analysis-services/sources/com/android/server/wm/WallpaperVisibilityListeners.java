package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperVisibilityListeners {
    private final android.util.SparseArray<android.os.RemoteCallbackList<android.view.IWallpaperVisibilityListener>> mDisplayListeners = new android.util.SparseArray<>();

    WallpaperVisibilityListeners() {
    }

    void registerWallpaperVisibilityListener(android.view.IWallpaperVisibilityListener listener, int displayId) {
        android.os.RemoteCallbackList<android.view.IWallpaperVisibilityListener> listeners = this.mDisplayListeners.get(displayId);
        if (listeners == null) {
            listeners = new android.os.RemoteCallbackList<>();
            this.mDisplayListeners.append(displayId, listeners);
        }
        listeners.register(listener);
    }

    void unregisterWallpaperVisibilityListener(android.view.IWallpaperVisibilityListener listener, int displayId) {
        android.os.RemoteCallbackList<android.view.IWallpaperVisibilityListener> listeners = this.mDisplayListeners.get(displayId);
        if (listeners == null) {
            return;
        }
        listeners.unregister(listener);
    }

    void notifyWallpaperVisibilityChanged(com.android.server.wm.DisplayContent displayContent) {
        int displayId = displayContent.getDisplayId();
        boolean visible = displayContent.mWallpaperController.isWallpaperVisible();
        android.os.RemoteCallbackList<android.view.IWallpaperVisibilityListener> displayListeners = this.mDisplayListeners.get(displayId);
        if (displayListeners == null) {
            return;
        }
        int i = displayListeners.beginBroadcast();
        while (i > 0) {
            i--;
            android.view.IWallpaperVisibilityListener listener = displayListeners.getBroadcastItem(i);
            try {
                listener.onWallpaperVisibilityChanged(visible, displayId);
            } catch (android.os.RemoteException e) {
            }
        }
        displayListeners.finishBroadcast();
    }
}
