package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ScreenInteractiveHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface ScreenInteractiveChangedListener {
        void onScreenInteractiveChanged(boolean z);
    }

    public abstract boolean isInteractive();

    public final void addListener(com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener listener) {
        this.mListeners.remove(listener);
    }

    protected final void notifyScreenInteractiveChanged(boolean interactive) {
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "screen interactive is now " + interactive);
        }
        for (com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener listener : this.mListeners) {
            listener.onScreenInteractiveChanged(interactive);
        }
    }
}
