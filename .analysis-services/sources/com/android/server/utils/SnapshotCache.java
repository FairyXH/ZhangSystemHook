package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public abstract class SnapshotCache<T> extends com.android.server.utils.Watcher {
    private static final boolean ENABLED = true;
    private static final java.util.WeakHashMap<com.android.server.utils.SnapshotCache, java.lang.Void> sCaches = new java.util.WeakHashMap<>();
    private volatile boolean mSealed;
    private volatile T mSnapshot;
    protected final T mSource;
    private final com.android.server.utils.SnapshotCache.Statistics mStatistics;

    public abstract T createSnapshot();

    private static class Statistics {
        final java.lang.String mName;
        private final java.util.concurrent.atomic.AtomicInteger mReused = new java.util.concurrent.atomic.AtomicInteger(0);
        private final java.util.concurrent.atomic.AtomicInteger mRebuilt = new java.util.concurrent.atomic.AtomicInteger(0);

        Statistics(java.lang.String n) {
            this.mName = n;
        }
    }

    public SnapshotCache(T source, com.android.server.utils.Watchable watchable, java.lang.String name) {
        this.mSnapshot = null;
        this.mSealed = false;
        this.mSource = source;
        watchable.registerObserver(this);
        if (name != null) {
            this.mStatistics = new com.android.server.utils.SnapshotCache.Statistics(name);
            sCaches.put(this, null);
        } else {
            this.mStatistics = null;
        }
    }

    public SnapshotCache(T source, com.android.server.utils.Watchable watchable) {
        this(source, watchable, null);
    }

    public SnapshotCache() {
        this.mSnapshot = null;
        this.mSealed = false;
        this.mSource = null;
        this.mSealed = true;
        this.mStatistics = null;
    }

    @Override // com.android.server.utils.Watcher
    public final void onChange(com.android.server.utils.Watchable what) {
        if (this.mSealed) {
            throw new java.lang.IllegalStateException("attempt to change a sealed object");
        }
        this.mSnapshot = null;
    }

    public final void seal() {
        this.mSealed = true;
    }

    public final T snapshot() {
        T s = this.mSnapshot;
        if (s == null) {
            s = createSnapshot();
            this.mSnapshot = s;
            if (this.mStatistics != null) {
                this.mStatistics.mRebuilt.incrementAndGet();
            }
        } else if (this.mStatistics != null) {
            this.mStatistics.mReused.incrementAndGet();
        }
        return s;
    }

    public static class Sealed<T> extends com.android.server.utils.SnapshotCache<T> {
        @Override // com.android.server.utils.SnapshotCache
        public T createSnapshot() {
            throw new java.lang.UnsupportedOperationException("cannot snapshot a sealed snaphot");
        }
    }

    public static class Auto<T extends com.android.server.utils.Snappable<T>> extends com.android.server.utils.SnapshotCache<T> {
        public Auto(T source, com.android.server.utils.Watchable watchable, java.lang.String name) {
            super(source, watchable, name);
        }

        public Auto(T source, com.android.server.utils.Watchable watchable) {
            this(source, watchable, null);
        }

        @Override // com.android.server.utils.SnapshotCache
        public T createSnapshot() {
            return (T) this.mSource.snapshot();
        }
    }
}
