package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WatchedSparseSetArray<T> extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable {
    private final android.util.SparseSetArray mStorage;

    private void onChanged() {
        dispatchChange(this);
    }

    public WatchedSparseSetArray() {
        this.mStorage = new android.util.SparseSetArray();
    }

    public WatchedSparseSetArray(com.android.server.utils.WatchedSparseSetArray<T> watchedSparseSetArray) {
        this.mStorage = new android.util.SparseSetArray(watchedSparseSetArray.untrackedStorage());
    }

    public WatchedSparseSetArray(android.util.SparseSetArray<T> sparseSetArray) {
        this.mStorage = sparseSetArray;
    }

    public android.util.SparseSetArray<T> untrackedStorage() {
        return this.mStorage;
    }

    public boolean add(int n, T value) {
        boolean res = this.mStorage.add(n, value);
        onChanged();
        return res;
    }

    public void addAll(int n, android.util.ArraySet<T> values) {
        this.mStorage.addAll(n, values);
        onChanged();
    }

    public void clear() {
        this.mStorage.clear();
        onChanged();
    }

    public boolean contains(int n, T value) {
        return this.mStorage.contains(n, value);
    }

    public android.util.ArraySet<T> get(int n) {
        return this.mStorage.get(n);
    }

    public boolean remove(int n, T value) {
        if (this.mStorage.remove(n, value)) {
            onChanged();
            return true;
        }
        return false;
    }

    public void remove(int n) {
        this.mStorage.remove(n);
        onChanged();
    }

    public int size() {
        return this.mStorage.size();
    }

    public int keyAt(int index) {
        return this.mStorage.keyAt(index);
    }

    public int sizeAt(int index) {
        return this.mStorage.sizeAt(index);
    }

    public T valueAt(int i, int i2) {
        return (T) this.mStorage.valueAt(i, i2);
    }

    public void copyFrom(android.util.SparseSetArray<T> c) {
        clear();
        int end = c.size();
        for (int i = 0; i < end; i++) {
            int key = c.keyAt(i);
            android.util.ArraySet<T> set = c.get(key);
            this.mStorage.addAll(key, set);
        }
        onChanged();
    }

    @Override // com.android.server.utils.Snappable
    public java.lang.Object snapshot() {
        com.android.server.utils.WatchedSparseSetArray l = new com.android.server.utils.WatchedSparseSetArray(this);
        l.seal();
        return l;
    }

    public void snapshot(com.android.server.utils.WatchedSparseSetArray<T> r) {
        snapshot(this, r);
    }

    public static void snapshot(com.android.server.utils.WatchedSparseSetArray dst, com.android.server.utils.WatchedSparseSetArray src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int arraySize = src.size();
        for (int i = 0; i < arraySize; i++) {
            android.util.ArraySet<T> arraySet = src.get(i);
            int setSize = arraySet.size();
            for (int j = 0; j < setSize; j++) {
                dst.mStorage.add(src.keyAt(i), arraySet.valueAt(j));
            }
        }
        dst.seal();
    }
}
