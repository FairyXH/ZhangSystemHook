package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedSparseArray<E> extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final com.android.server.utils.Watcher mObserver;
    private final android.util.SparseArray<E> mStorage;
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

    public WatchedSparseArray() {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = new android.util.SparseArray<>();
    }

    public WatchedSparseArray(int initialCapacity) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = new android.util.SparseArray<>(initialCapacity);
    }

    public WatchedSparseArray(android.util.SparseArray<E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = c.clone();
    }

    public WatchedSparseArray(com.android.server.utils.WatchedSparseArray<E> r) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedSparseArray.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable o) {
                com.android.server.utils.WatchedSparseArray.this.dispatchChange(o);
            }
        };
        this.mStorage = r.mStorage.clone();
    }

    public void copyFrom(android.util.SparseArray<E> src) {
        clear();
        int end = src.size();
        for (int i = 0; i < end; i++) {
            put(src.keyAt(i), src.valueAt(i));
        }
    }

    public void copyTo(android.util.SparseArray<E> dst) {
        dst.clear();
        int end = size();
        for (int i = 0; i < end; i++) {
            dst.put(keyAt(i), valueAt(i));
        }
    }

    public android.util.SparseArray<E> untrackedStorage() {
        return this.mStorage;
    }

    public boolean contains(int key) {
        return this.mStorage.contains(key);
    }

    public E get(int key) {
        return this.mStorage.get(key);
    }

    public E get(int key, E valueIfKeyNotFound) {
        return this.mStorage.get(key, valueIfKeyNotFound);
    }

    public void delete(int key) {
        E child = this.mStorage.get(key);
        this.mStorage.delete(key);
        unregisterChildIf(child);
        onChanged();
    }

    public E removeReturnOld(int i) {
        E e = (E) this.mStorage.removeReturnOld(i);
        unregisterChildIf(e);
        return e;
    }

    public void remove(int key) {
        delete(key);
    }

    public void removeAt(int index) {
        E child = this.mStorage.valueAt(index);
        this.mStorage.removeAt(index);
        unregisterChildIf(child);
        onChanged();
    }

    public void removeAtRange(int index, int size) {
        java.util.ArrayList<E> children = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                children.add(this.mStorage.valueAt(i + index));
            } catch (java.lang.Exception e) {
            }
        }
        try {
            this.mStorage.removeAtRange(index, size);
            onChanged();
        } finally {
            for (int i2 = 0; i2 < size; i2++) {
                unregisterChildIf(children.get(i2));
            }
        }
    }

    public void put(int key, E value) {
        E old = this.mStorage.get(key);
        this.mStorage.put(key, value);
        unregisterChildIf(old);
        registerChild(value);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int index) {
        return this.mStorage.keyAt(index);
    }

    public E valueAt(int index) {
        return this.mStorage.valueAt(index);
    }

    public void setValueAt(int index, E value) {
        E old = this.mStorage.valueAt(index);
        this.mStorage.setValueAt(index, value);
        if (value != old) {
            unregisterChildIf(old);
            registerChild(value);
            onChanged();
        }
    }

    public int indexOfKey(int key) {
        return this.mStorage.indexOfKey(key);
    }

    public int indexOfValue(E value) {
        return this.mStorage.indexOfValue(value);
    }

    public int indexOfValueByValue(E value) {
        return this.mStorage.indexOfValueByValue(value);
    }

    public void clear() {
        if (this.mWatching) {
            int end = this.mStorage.size();
            for (int i = 0; i < end; i++) {
                unregisterChild(this.mStorage.valueAt(i));
            }
        }
        this.mStorage.clear();
        onChanged();
    }

    public void append(int key, E value) {
        this.mStorage.append(key, value);
        registerChild(value);
        onChanged();
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public boolean equals(java.lang.Object o) {
        if (o instanceof com.android.server.utils.WatchedSparseArray) {
            com.android.server.utils.WatchedSparseArray w = (com.android.server.utils.WatchedSparseArray) o;
            return this.mStorage.equals(w.mStorage);
        }
        return false;
    }

    public java.lang.String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedSparseArray<E> snapshot() {
        com.android.server.utils.WatchedSparseArray<E> l = new com.android.server.utils.WatchedSparseArray<>(size());
        snapshot(l, this);
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedSparseArray<E> r) {
        snapshot(this, r);
    }

    public static <E> void snapshot(com.android.server.utils.WatchedSparseArray<E> watchedSparseArray, com.android.server.utils.WatchedSparseArray<E> watchedSparseArray2) {
        if (watchedSparseArray.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedSparseArray2.size();
        for (int i = 0; i < size; i++) {
            java.lang.Object objMaybeSnapshot = com.android.server.utils.Snapshots.maybeSnapshot(watchedSparseArray2.valueAt(i));
            ((com.android.server.utils.WatchedSparseArray) watchedSparseArray).mStorage.put(watchedSparseArray2.keyAt(i), (E) objMaybeSnapshot);
        }
        watchedSparseArray.seal();
    }
}
