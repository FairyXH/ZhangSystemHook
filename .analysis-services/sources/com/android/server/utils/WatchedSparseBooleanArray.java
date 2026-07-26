package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedSparseBooleanArray extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final android.util.SparseBooleanArray mStorage;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseBooleanArray() {
        this.mStorage = new android.util.SparseBooleanArray();
    }

    public WatchedSparseBooleanArray(int initialCapacity) {
        this.mStorage = new android.util.SparseBooleanArray(initialCapacity);
    }

    public WatchedSparseBooleanArray(android.util.SparseBooleanArray c) {
        this.mStorage = c.clone();
    }

    public WatchedSparseBooleanArray(com.android.server.utils.WatchedSparseBooleanArray r) {
        this.mStorage = r.mStorage.clone();
    }

    public void copyFrom(android.util.SparseBooleanArray src) {
        clear();
        int end = src.size();
        for (int i = 0; i < end; i++) {
            put(src.keyAt(i), src.valueAt(i));
        }
    }

    public void copyTo(android.util.SparseBooleanArray dst) {
        dst.clear();
        int end = size();
        for (int i = 0; i < end; i++) {
            dst.put(keyAt(i), valueAt(i));
        }
    }

    public android.util.SparseBooleanArray untrackedStorage() {
        return this.mStorage;
    }

    public boolean get(int key) {
        return this.mStorage.get(key);
    }

    public boolean get(int key, boolean valueIfKeyNotFound) {
        return this.mStorage.get(key, valueIfKeyNotFound);
    }

    public void delete(int key) {
        this.mStorage.delete(key);
        onChanged();
    }

    public void removeAt(int index) {
        this.mStorage.removeAt(index);
        onChanged();
    }

    public void put(int key, boolean value) {
        this.mStorage.put(key, value);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int index) {
        return this.mStorage.keyAt(index);
    }

    public boolean valueAt(int index) {
        return this.mStorage.valueAt(index);
    }

    public void setValueAt(int index, boolean value) {
        if (this.mStorage.valueAt(index) != value) {
            this.mStorage.setValueAt(index, value);
            onChanged();
        }
    }

    public void setKeyAt(int index, int key) {
        if (this.mStorage.keyAt(index) != key) {
            this.mStorage.setKeyAt(index, key);
            onChanged();
        }
    }

    public int indexOfKey(int key) {
        return this.mStorage.indexOfKey(key);
    }

    public int indexOfValue(boolean value) {
        return this.mStorage.indexOfValue(value);
    }

    public void clear() {
        this.mStorage.clear();
        onChanged();
    }

    public void append(int key, boolean value) {
        this.mStorage.append(key, value);
        onChanged();
    }

    public int hashCode() {
        return this.mStorage.hashCode();
    }

    public boolean equals(java.lang.Object o) {
        if (o instanceof com.android.server.utils.WatchedSparseBooleanArray) {
            com.android.server.utils.WatchedSparseBooleanArray w = (com.android.server.utils.WatchedSparseBooleanArray) o;
            return this.mStorage.equals(w.mStorage);
        }
        return false;
    }

    public java.lang.String toString() {
        return this.mStorage.toString();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.utils.WatchedSparseBooleanArray snapshot() {
        com.android.server.utils.WatchedSparseBooleanArray l = new com.android.server.utils.WatchedSparseBooleanArray(this);
        l.seal();
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedSparseBooleanArray r) {
        snapshot(this, r);
    }

    public static void snapshot(com.android.server.utils.WatchedSparseBooleanArray dst, com.android.server.utils.WatchedSparseBooleanArray src) {
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
