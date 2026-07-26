package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedArraySet<E> extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final com.android.server.utils.Watcher mObserver;
    private final android.util.ArraySet<E> mStorage;
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
        if (this.mWatching && (o instanceof com.android.server.utils.Watchable) && !this.mStorage.contains(o)) {
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

    public WatchedArraySet() {
        this(0, false);
    }

    public WatchedArraySet(int capacity) {
        this(capacity, false);
    }

    public WatchedArraySet(int capacity, boolean identityHashCode) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArraySet.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArraySet.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArraySet<>(capacity, identityHashCode);
    }

    public WatchedArraySet(E[] array) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArraySet.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArraySet.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArraySet<>(array);
    }

    public WatchedArraySet(android.util.ArraySet<E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArraySet.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArraySet.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArraySet<>((android.util.ArraySet) c);
    }

    public WatchedArraySet(com.android.server.utils.WatchedArraySet<E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArraySet.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArraySet.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArraySet<>((android.util.ArraySet) c.mStorage);
    }

    public void copyFrom(android.util.ArraySet<E> src) {
        clear();
        int end = src.size();
        this.mStorage.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            add(src.valueAt(i));
        }
    }

    public void copyTo(android.util.ArraySet<E> dst) {
        dst.clear();
        int end = size();
        dst.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            dst.add(valueAt(i));
        }
    }

    public android.util.ArraySet<E> untrackedStorage() {
        return this.mStorage;
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

    public boolean contains(java.lang.Object key) {
        return this.mStorage.contains(key);
    }

    public int indexOf(java.lang.Object key) {
        return this.mStorage.indexOf(key);
    }

    public E valueAt(int index) {
        return this.mStorage.valueAt(index);
    }

    public boolean isEmpty() {
        return this.mStorage.isEmpty();
    }

    public boolean add(E value) {
        boolean result = this.mStorage.add(value);
        registerChild(value);
        onChanged();
        return result;
    }

    public void append(E value) {
        this.mStorage.append(value);
        registerChild(value);
        onChanged();
    }

    public void addAll(java.util.Collection<? extends E> collection) {
        this.mStorage.addAll(collection);
        onChanged();
    }

    public void addAll(com.android.server.utils.WatchedArraySet<? extends E> array) {
        int end = array.size();
        for (int i = 0; i < end; i++) {
            add(array.valueAt(i));
        }
    }

    public boolean remove(java.lang.Object o) {
        if (this.mStorage.remove(o)) {
            unregisterChildIf(o);
            onChanged();
            return true;
        }
        return false;
    }

    public E removeAt(int index) {
        E result = this.mStorage.removeAt(index);
        unregisterChildIf(result);
        onChanged();
        return result;
    }

    public boolean removeAll(android.util.ArraySet<? extends E> array) {
        int end = array.size();
        boolean any = false;
        for (int i = 0; i < end; i++) {
            any = remove(array.valueAt(i)) || any;
        }
        return any;
    }

    public int size() {
        return this.mStorage.size();
    }

    public boolean equals(java.lang.Object object) {
        if (object instanceof com.android.server.utils.WatchedArraySet) {
            return this.mStorage.equals(((com.android.server.utils.WatchedArraySet) object).mStorage);
        }
        return this.mStorage.equals(object);
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public java.lang.String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedArraySet<E> snapshot() {
        com.android.server.utils.WatchedArraySet<E> l = new com.android.server.utils.WatchedArraySet<>();
        snapshot(l, this);
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedArraySet<E> r) {
        snapshot(this, r);
    }

    public static <E> void snapshot(com.android.server.utils.WatchedArraySet<E> dst, com.android.server.utils.WatchedArraySet<E> src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int end = src.size();
        ((com.android.server.utils.WatchedArraySet) dst).mStorage.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            ((com.android.server.utils.WatchedArraySet) dst).mStorage.append(com.android.server.utils.Snapshots.maybeSnapshot(src.valueAt(i)));
        }
        dst.seal();
    }
}
