package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AppForegroundHelper {
    protected static final int FOREGROUND_IMPORTANCE_CUTOFF = 125;
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.AppForegroundHelper.AppForegroundListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface AppForegroundListener {
        void onAppForegroundChanged(int i, boolean z);
    }

    public abstract boolean isAppForeground(int i);

    protected static boolean isForeground(int importance) {
        return importance <= 125;
    }

    public final void addListener(com.android.server.location.injector.AppForegroundHelper.AppForegroundListener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(com.android.server.location.injector.AppForegroundHelper.AppForegroundListener listener) {
        this.mListeners.remove(listener);
    }

    protected final void notifyAppForeground(int uid, boolean foreground) {
        for (com.android.server.location.injector.AppForegroundHelper.AppForegroundListener listener : this.mListeners) {
            listener.onAppForegroundChanged(uid, foreground);
        }
    }
}
