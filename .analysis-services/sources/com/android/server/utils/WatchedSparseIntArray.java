package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedSparseIntArray extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final android.util.SparseIntArray mStorage;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseIntArray() {
        this.mStorage = new android.util.SparseIntArray();
    }

    public WatchedSparseIntArray(int initialCapacity) {
        this.mStorage = new android.util.SparseIntArray(initialCapacity);
    }

    public WatchedSparseIntArray(android.util.SparseIntArray c) {
        this.mStorage = c.clone();
    }

    public WatchedSparseIntArray(com.android.server.utils.WatchedSparseIntArray r) {
        this.mStorage = r.mStorage.clone();
    }

    public void copyFrom(android.util.SparseIntArray src) {
        clear();
        int end = src.size();
        for (int i = 0; i < end; i++) {
            put(src.keyAt(i), src.valueAt(i));
        }
    }

    public void copyTo(android.util.SparseIntArray dst) {
        dst.clear();
        int end = size();
        for (int i = 0; i < end; i++) {
            dst.put(keyAt(i), valueAt(i));
        }
    }

    public android.util.SparseIntArray untrackedStorage() {
        return this.mStorage;
    }

    public int get(int key) {
        return this.mStorage.get(key);
    }

    public int get(int key, int valueIfKeyNotFound) {
        return this.mStorage.get(key, valueIfKeyNotFound);
    }

    public void delete(int key) {
        int index = this.mStorage.indexOfKey(key);
        if (index >= 0) {
            this.mStorage.removeAt(index);
            onChanged();
        }
    }

    public void removeAt(int index) {
        this.mStorage.removeAt(index);
        onChanged();
    }

    public void put(int key, int value) {
        this.mStorage.put(key, value);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int index) {
        return this.mStorage.keyAt(index);
    }

    public int valueAt(int index) {
        return this.mStorage.valueAt(index);
    }

    public void setValueAt(int index, int value) {
        if (this.mStorage.valueAt(index) != value) {
            this.mStorage.setValueAt(index, value);
            onChanged();
        }
    }

    public int indexOfKey(int key) {
        return this.mStorage.indexOfKey(key);
    }

    public int indexOfValue(int value) {
        return this.mStorage.indexOfValue(value);
    }

    public void clear() {
        int count = size();
        this.mStorage.clear();
        if (count > 0) {
            onChanged();
        }
    }

    public void append(int key, int value) {
        this.mStorage.append(key, value);
        onChanged();
    }

    public int[] copyKeys() {
        return this.mStorage.copyKeys();
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public boolean equals(java.lang.Object o) {
        if (o instanceof com.android.server.utils.WatchedSparseIntArray) {
            com.android.server.utils.WatchedSparseIntArray w = (com.android.server.utils.WatchedSparseIntArray) o;
            return this.mStorage.equals(w.mStorage);
        }
        return false;
    }

    public java.lang.String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedSparseIntArray snapshot() {
        com.android.server.utils.WatchedSparseIntArray l = new com.android.server.utils.WatchedSparseIntArray(this);
        l.seal();
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedSparseIntArray r) {
        snapshot(this, r);
    }

    public static void snapshot(com.android.server.utils.WatchedSparseIntArray dst, com.android.server.utils.WatchedSparseIntArray src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int end = src.size();
        for (int i = 0; i < end; i++) {
            dst.mStorage.put(src.keyAt(i), src.valueAt(i));
        }
        dst.seal();
    }
}
