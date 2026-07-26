package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AppOpsHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.AppOpsHelper.LocationAppOpListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface LocationAppOpListener {
        void onAppOpsChanged(java.lang.String str);
    }

    public abstract boolean checkOpNoThrow(int i, android.location.util.identity.CallerIdentity callerIdentity);

    public abstract void finishOp(int i, android.location.util.identity.CallerIdentity callerIdentity);

    public abstract boolean noteOp(int i, android.location.util.identity.CallerIdentity callerIdentity);

    public abstract boolean noteOpNoThrow(int i, android.location.util.identity.CallerIdentity callerIdentity);

    public abstract boolean startOpNoThrow(int i, android.location.util.identity.CallerIdentity callerIdentity);

    protected final void notifyAppOpChanged(java.lang.String packageName) {
        for (com.android.server.location.injector.AppOpsHelper.LocationAppOpListener listener : this.mListeners) {
            listener.onAppOpsChanged(packageName);
        }
    }

    public final void addListener(com.android.server.location.injector.AppOpsHelper.LocationAppOpListener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(com.android.server.location.injector.AppOpsHelper.LocationAppOpListener listener) {
        this.mListeners.remove(listener);
    }
}
