package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedLongSparseArray<E> extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final com.android.server.utils.Watcher mObserver;
    private final android.util.LongSparseArray<E> mStorage;
    private volatile boolean mWatching;

    private void onChanged() {
        dispatchChange(this);
    }

    private void registerChild(java.lang.Object o) {
        if (this.mWatching && (o instanceof com.android.server.utils.Watchable)) {
            ((com.android.server.utils.Watchable) o).registerObserver(this.mObserver);
        }
    }

    private void unregisterChild(java.lang.Object o) {
        if (this.mWatching && (o instanceof com.android.server.utils.Watchable)) {
            ((com.android.server.utils.Watchable) o).unregisterObserver(this.mObserver);
        }
    }

    private void unregisterChildIf(java.lang.Object o) {
        if (this.mWatching && (o instanceof com.android.server.utils.Watchable) && this.mStorage.indexOfValue(o) == -1) {
            ((com.android.server.utils.Watchable) o).unregisterObserver(this.mObserver);
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        super.registerObserver(observer);
        if (registeredObserverCount() == 1) {
            this.mWatching = true;
            int end = this.mStorage.size();
            for (int i = 0; i < end; i++) {
                registerChild(this.mStorage.valueAt(i));
            }
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        super.unregisterObserver(observer);
        if (registeredObserverCount() == 0) {
            int end = this.mStorage.size();
            for (int i = 0; i < end; i++) {
                unregisterChild(this.mStorage.valueAt(i));
            }
            this.mWatching = false;
        }
    }

    public WatchedLongSparseArray() {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedLongSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedLongSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = new android.util.LongSparseArray<>();
    }

    public WatchedLongSparseArray(int initialCapacity) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedLongSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedLongSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = new android.util.LongSparseArray<>(initialCapacity);
    }

    public WatchedLongSparseArray(android.util.LongSparseArray<E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedLongSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedLongSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = c.clone();
    }

    public WatchedLongSparseArray(com.android.server.utils.WatchedLongSparseArray<E> r) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedLongSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedLongSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = r.mStorage.clone();
    }

    public void copyFrom(android.util.LongSparseArray<E> src) {
        clear();
        int end = src.size();
        for (int i = 0; i < end; i++) {
            put(src.keyAt(i), src.valueAt(i));
        }
    }

    public void copyTo(android.util.LongSparseArray<E> dst) {
        dst.clear();
        int end = size();
        for (int i = 0; i < end; i++) {
            dst.put(keyAt(i), valueAt(i));
        }
    }

    public android.util.LongSparseArray<E> untrackedStorage() {
        return this.mStorage;
    }

    public E get(long key) {
        return this.mStorage.get(key);
    }

    public E get(long key, E valueIfKeyNotFound) {
        return this.mStorage.get(key, valueIfKeyNotFound);
    }

    public void delete(long key) {
        E old = this.mStorage.get(key, null);
        this.mStorage.delete(key);
        unregisterChildIf(old);
        onChanged();
    }

    public void remove(long key) {
        delete(key);
    }

    public void removeAt(int index) {
        E old = this.mStorage.valueAt(index);
        this.mStorage.removeAt(index);
        unregisterChildIf(old);
        onChanged();
    }

    public void put(long key, E value) {
        E old = this.mStorage.get(key);
        this.mStorage.put(key, value);
        unregisterChildIf(old);
        registerChild(value);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public long keyAt(int index) {
        return this.mStorage.keyAt(index);
    }

    public E valueAt(int index) {
        return this.mStorage.valueAt(index);
    }

    public void setValueAt(int index, E value) {
        E old = this.mStorage.valueAt(index);
        this.mStorage.setValueAt(index, value);
        unregisterChildIf(old);
        registerChild(value);
        onChanged();
    }

    public int indexOfKey(long key) {
        return this.mStorage.indexOfKey(key);
    }

    public int indexOfValue(E value) {
        return this.mStorage.indexOfValue(value);
    }

    public int indexOfValueByValue(E value) {
        return this.mStorage.indexOfValueByValue(value);
    }

    public void clear() {
        int end = this.mStorage.size();
        for (int i = 0; i < end; i++) {
            unregisterChild(this.mStorage.valueAt(i));
        }
        this.mStorage.clear();
        onChanged();
    }

    public void append(long key, E value) {
        this.mStorage.append(key, value);
        registerChild(value);
        onChanged();
    }

    public java.lang.String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedLongSparseArray<E> snapshot() {
        com.android.server.utils.WatchedLongSparseArray<E> l = new com.android.server.utils.WatchedLongSparseArray<>(size());
        snapshot(l, this);
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedLongSparseArray<E> r) {
        snapshot(this, r);
    }

    public static <E> void snapshot(com.android.server.utils.WatchedLongSparseArray<E> watchedLongSparseArray, com.android.server.utils.WatchedLongSparseArray<E> watchedLongSparseArray2) {
        if (watchedLongSparseArray.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedLongSparseArray2.size();
        for (int i = 0; i < size; i++) {
            java.lang.Object objMaybeSnapshot = com.android.server.utils.Snapshots.maybeSnapshot(watchedLongSparseArray2.valueAt(i));
            ((com.android.server.utils.WatchedLongSparseArray) watchedLongSparseArray).mStorage.put(watchedLongSparseArray2.keyAt(i), (E) objMaybeSnapshot);
        }
        watchedLongSparseArray.seal();
    }
}
