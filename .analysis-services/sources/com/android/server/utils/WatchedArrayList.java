package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedArrayList<E> extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final com.android.server.utils.Watcher mObserver;
    private final java.util.ArrayList<E> mStorage;
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
                registerChild(this.mStorage.get(i));
            }
        }
    }

    @Override // com.android.server.utils.WatchableImpl, com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        super.unregisterObserver(observer);
        if (registeredObserverCount() == 0) {
            int end = this.mStorage.size();
            for (int i = 0; i < end; i++) {
                unregisterChild(this.mStorage.get(i));
            }
            this.mWatching = false;
        }
    }

    public WatchedArrayList() {
        this(0);
    }

    public WatchedArrayList(int capacity) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayList.this.dispatchChange(what);
            }
        };
        this.mStorage = new java.util.ArrayList<>(capacity);
    }

    public WatchedArrayList(java.util.Collection<? extends E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayList.this.dispatchChange(what);
            }
        };
        this.mStorage = new java.util.ArrayList<>();
        if (c != null) {
            this.mStorage.addAll(c);
        }
    }

    public WatchedArrayList(java.util.ArrayList<E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayList.this.dispatchChange(what);
            }
        };
        this.mStorage = new java.util.ArrayList<>(c);
    }

    public WatchedArrayList(com.android.server.utils.WatchedArrayList<E> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayList.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayList.this.dispatchChange(what);
            }
        };
        this.mStorage = new java.util.ArrayList<>(c.mStorage);
    }

    public void copyFrom(java.util.ArrayList<E> src) {
        clear();
        int end = src.size();
        this.mStorage.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            add(src.get(i));
        }
    }

    public void copyTo(java.util.ArrayList<E> dst) {
        dst.clear();
        int end = size();
        dst.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            dst.add(get(i));
        }
    }

    public java.util.ArrayList<E> untrackedStorage() {
        return this.mStorage;
    }

    public boolean add(E value) {
        boolean result = this.mStorage.add(value);
        registerChild(value);
        onChanged();
        return result;
    }

    public void add(int index, E value) {
        this.mStorage.add(index, value);
        registerChild(value);
        onChanged();
    }

    public boolean addAll(java.util.Collection<? extends E> c) {
        if (c.size() > 0) {
            for (E e : c) {
                this.mStorage.add(e);
            }
            onChanged();
            return true;
        }
        return false;
    }

    public boolean addAll(int index, java.util.Collection<? extends E> c) {
        if (c.size() > 0) {
            for (E e : c) {
                this.mStorage.add(index, e);
                index++;
            }
            onChanged();
            return true;
        }
        return false;
    }

    public void clear() {
        if (this.mWatching) {
            int end = this.mStorage.size();
            for (int i = 0; i < end; i++) {
                unregisterChild(this.mStorage.get(i));
            }
        }
        this.mStorage.clear();
        onChanged();
    }

    public boolean contains(java.lang.Object o) {
        return this.mStorage.contains(o);
    }

    public boolean containsAll(java.util.Collection<?> c) {
        return this.mStorage.containsAll(c);
    }

    public void ensureCapacity(int min) {
        this.mStorage.ensureCapacity(min);
    }

    public E get(int index) {
        return this.mStorage.get(index);
    }

    public int indexOf(java.lang.Object o) {
        return this.mStorage.indexOf(o);
    }

    public boolean isEmpty() {
        return this.mStorage.isEmpty();
    }

    public int lastIndexOf(java.lang.Object o) {
        return this.mStorage.lastIndexOf(o);
    }

    public E remove(int index) {
        E result = this.mStorage.remove(index);
        unregisterChildIf(result);
        onChanged();
        return result;
    }

    public boolean remove(java.lang.Object o) {
        if (this.mStorage.remove(o)) {
            unregisterChildIf(o);
            onChanged();
            return true;
        }
        return false;
    }

    public E set(int index, E value) {
        E result = this.mStorage.set(index, value);
        if (value != result) {
            unregisterChildIf(result);
            registerChild(value);
            onChanged();
        }
        return result;
    }

    public int size() {
        return this.mStorage.size();
    }

    public boolean equals(java.lang.Object o) {
        if (o instanceof com.android.server.utils.WatchedArrayList) {
            com.android.server.utils.WatchedArrayList w = (com.android.server.utils.WatchedArrayList) o;
            return this.mStorage.equals(w.mStorage);
        }
        return false;
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedArrayList<E> snapshot() {
        com.android.server.utils.WatchedArrayList<E> l = new com.android.server.utils.WatchedArrayList<>(size());
        snapshot(l, this);
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedArrayList<E> r) {
        snapshot(this, r);
    }

    public static <E> void snapshot(com.android.server.utils.WatchedArrayList<E> watchedArrayList, com.android.server.utils.WatchedArrayList<E> watchedArrayList2) {
        if (watchedArrayList.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedArrayList2.size();
        ((com.android.server.utils.WatchedArrayList) watchedArrayList).mStorage.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            ((com.android.server.utils.WatchedArrayList) watchedArrayList).mStorage.add((E) com.android.server.utils.Snapshots.maybeSnapshot(watchedArrayList2.get(i)));
        }
        watchedArrayList.seal();
    }
}
