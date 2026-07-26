package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class UnmodifiableSparseArray<E> {
    private static final java.lang.String TAG = "ImmutableSparseArray";
    private final android.util.SparseArray<E> mArray;

    public UnmodifiableSparseArray(android.util.SparseArray<E> array) {
        this.mArray = array;
    }

    public int size() {
        return this.mArray.size();
    }

    public E get(int key) {
        return this.mArray.get(key);
    }

    public E get(int key, E valueIfKeyNotFound) {
        return this.mArray.get(key, valueIfKeyNotFound);
    }

    public int keyAt(int index) {
        return this.mArray.keyAt(index);
    }

    public E valueAt(int index) {
        return this.mArray.valueAt(index);
    }

    public int indexOfValue(E value) {
        return this.mArray.indexOfValue(value);
    }

    public java.lang.String toString() {
        return this.mArray.toString();
    }
}
