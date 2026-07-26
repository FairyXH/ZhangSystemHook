package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class UnmodifiableSparseIntArray {
    private static final java.lang.String TAG = "ImmutableSparseIntArray";
    private final android.util.SparseIntArray mArray;

    public UnmodifiableSparseIntArray(android.util.SparseIntArray array) {
        this.mArray = array;
    }

    public int size() {
        return this.mArray.size();
    }

    public int get(int key) {
        return this.mArray.get(key);
    }

    public int get(int key, int valueIfKeyNotFound) {
        return this.mArray.get(key, valueIfKeyNotFound);
    }

    public int keyAt(int index) {
        return this.mArray.keyAt(index);
    }

    public int valueAt(int index) {
        return this.mArray.valueAt(index);
    }

    public int indexOfValue(int value) {
        return this.mArray.indexOfValue(value);
    }

    public java.lang.String toString() {
        return this.mArray.toString();
    }
}
