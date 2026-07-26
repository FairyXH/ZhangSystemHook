package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedArrayMap<K, V> extends com.android.server.utils.WatchableImpl implements java.util.Map<K, V>, com.android.server.utils.Snappable {
    private final com.android.server.utils.Watcher mObserver;
    private final android.util.ArrayMap<K, V> mStorage;
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
        if (this.mWatching && (o instanceof com.android.server.utils.Watchable) && !this.mStorage.containsValue(o)) {
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

    public WatchedArrayMap() {
        this(0, false);
    }

    public WatchedArrayMap(int capacity) {
        this(capacity, false);
    }

    public WatchedArrayMap(int capacity, boolean identityHashCode) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayMap.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArrayMap<>(capacity, identityHashCode);
    }

    public WatchedArrayMap(java.util.Map<? extends K, ? extends V> map) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayMap.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArrayMap<>();
        if (map != null) {
            putAll(map);
        }
    }

    public WatchedArrayMap(android.util.ArrayMap<K, V> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayMap.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArrayMap<>(c);
    }

    public WatchedArrayMap(com.android.server.utils.WatchedArrayMap<K, V> c) {
        this.mWatching = false;
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.utils.WatchedArrayMap.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.utils.WatchedArrayMap.this.dispatchChange(what);
            }
        };
        this.mStorage = new android.util.ArrayMap<>(c.mStorage);
    }

    public void copyFrom(android.util.ArrayMap<K, V> src) {
        clear();
        int end = src.size();
        this.mStorage.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            put(src.keyAt(i), src.valueAt(i));
        }
    }

    public void copyTo(android.util.ArrayMap<K, V> dst) {
        dst.clear();
        int end = size();
        dst.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            dst.put(keyAt(i), valueAt(i));
        }
    }

    public android.util.ArrayMap<K, V> untrackedStorage() {
        return this.mStorage;
    }

    @Override // java.util.Map
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

    @Override // java.util.Map
    public boolean containsKey(java.lang.Object key) {
        return this.mStorage.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(java.lang.Object value) {
        return this.mStorage.containsValue(value);
    }

    @Override // java.util.Map
    public java.util.Set<java.util.Map.Entry<K, V>> entrySet() {
        return java.util.Collections.unmodifiableSet(this.mStorage.entrySet());
    }

    @Override // java.util.Map
    public boolean equals(java.lang.Object o) {
        if (o instanceof com.android.server.utils.WatchedArrayMap) {
            com.android.server.utils.WatchedArrayMap w = (com.android.server.utils.WatchedArrayMap) o;
            return this.mStorage.equals(w.mStorage);
        }
        return false;
    }

    @Override // java.util.Map
    public V get(java.lang.Object key) {
        return this.mStorage.get(key);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.mStorage.hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.mStorage.isEmpty();
    }

    @Override // java.util.Map
    public java.util.Set<K> keySet() {
        return java.util.Collections.unmodifiableSet(this.mStorage.keySet());
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        V result = this.mStorage.put(key, value);
        registerChild(value);
        onChanged();
        return result;
    }

    @Override // java.util.Map
    public void putAll(java.util.Map<? extends K, ? extends V> map) {
        for (java.util.Map.Entry<? extends K, ? extends V> element : map.entrySet()) {
            put(element.getKey(), element.getValue());
        }
    }

    @Override // java.util.Map
    public V remove(java.lang.Object key) {
        V result = this.mStorage.remove(key);
        unregisterChildIf(result);
        onChanged();
        return result;
    }

    @Override // java.util.Map
    public int size() {
        return this.mStorage.size();
    }

    @Override // java.util.Map
    public java.util.Collection<V> values() {
        return java.util.Collections.unmodifiableCollection(this.mStorage.values());
    }

    public K keyAt(int index) {
        return this.mStorage.keyAt(index);
    }

    public V valueAt(int index) {
        return this.mStorage.valueAt(index);
    }

    public int indexOfKey(K key) {
        return this.mStorage.indexOfKey(key);
    }

    public int indexOfValue(V value) {
        return this.mStorage.indexOfValue(value);
    }

    public V setValueAt(int index, V value) {
        V result = this.mStorage.setValueAt(index, value);
        if (value != result) {
            unregisterChildIf(result);
            registerChild(value);
            onChanged();
        }
        return result;
    }

    public V removeAt(int index) {
        V result = this.mStorage.removeAt(index);
        unregisterChildIf(result);
        onChanged();
        return result;
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedArrayMap<K, V> snapshot() {
        com.android.server.utils.WatchedArrayMap<K, V> l = new com.android.server.utils.WatchedArrayMap<>();
        snapshot(l, this);
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedArrayMap<K, V> r) {
        snapshot(this, r);
    }

    public static <K, V> void snapshot(com.android.server.utils.WatchedArrayMap<K, V> watchedArrayMap, com.android.server.utils.WatchedArrayMap<K, V> watchedArrayMap2) {
        if (watchedArrayMap.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int size = watchedArrayMap2.size();
        ((com.android.server.utils.WatchedArrayMap) watchedArrayMap).mStorage.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            java.lang.Object objMaybeSnapshot = com.android.server.utils.Snapshots.maybeSnapshot(watchedArrayMap2.valueAt(i));
            ((com.android.server.utils.WatchedArrayMap) watchedArrayMap).mStorage.put(watchedArrayMap2.keyAt(i), (V) objMaybeSnapshot);
        }
        watchedArrayMap.seal();
    }
}
