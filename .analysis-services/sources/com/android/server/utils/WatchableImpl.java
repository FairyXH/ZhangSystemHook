package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchableImpl implements com.android.server.utils.Watchable {
    protected final java.util.ArrayList<com.android.server.utils.Watcher> mObservers = new java.util.ArrayList<>();
    private boolean mSealed = false;

    @Override // com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        java.util.Objects.requireNonNull(observer, "observer may not be null");
        synchronized (this.mObservers) {
            if (!this.mObservers.contains(observer)) {
                this.mObservers.add(observer);
            }
        }
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        java.util.Objects.requireNonNull(observer, "observer may not be null");
        synchronized (this.mObservers) {
            this.mObservers.remove(observer);
        }
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(com.android.server.utils.Watcher observer) {
        boolean zContains;
        synchronized (this.mObservers) {
            zContains = this.mObservers.contains(observer);
        }
        return zContains;
    }

    public int registeredObserverCount() {
        return this.mObservers.size();
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(com.android.server.utils.Watchable what) {
        synchronized (this.mObservers) {
            if (this.mSealed) {
                throw new java.lang.IllegalStateException("attempt to change a sealed object");
            }
            int end = this.mObservers.size();
            for (int i = 0; i < end; i++) {
                this.mObservers.get(i).onChange(what);
            }
        }
    }

    public void seal() {
        synchronized (this.mObservers) {
            this.mSealed = true;
        }
    }

    public boolean isSealed() {
        boolean z;
        synchronized (this.mObservers) {
            z = this.mSealed;
        }
        return z;
    }
}
