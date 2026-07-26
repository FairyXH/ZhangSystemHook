package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class Snapshots {
    public static <T> T maybeSnapshot(T t) {
        if (t instanceof com.android.server.utils.Snappable) {
            return (T) ((com.android.server.utils.Snappable) t).snapshot();
        }
        return t;
    }

    public static <E> void copy(android.util.SparseArray<E> dst, android.util.SparseArray<E> src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("copy destination is not empty");
        }
        int end = src.size();
        for (int i = 0; i < end; i++) {
            dst.put(src.keyAt(i), src.valueAt(i));
        }
    }

    public static <E> void copy(android.util.SparseSetArray<E> dst, android.util.SparseSetArray<E> src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("copy destination is not empty");
        }
        int end = src.size();
        for (int i = 0; i < end; i++) {
            int size = src.sizeAt(i);
            for (int j = 0; j < size; j++) {
                dst.add(src.keyAt(i), src.valueAt(i, j));
            }
        }
    }

    public static void snapshot(android.util.SparseIntArray dst, android.util.SparseIntArray src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int end = src.size();
        for (int i = 0; i < end; i++) {
            dst.put(src.keyAt(i), src.valueAt(i));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E extends com.android.server.utils.Snappable<E>> void snapshot(android.util.SparseArray<E> sparseArray, android.util.SparseArray<E> src) {
        if (sparseArray.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int end = src.size();
        for (int i = 0; i < end; i++) {
            sparseArray.put(src.keyAt(i), (com.android.server.utils.Snappable) src.valueAt(i).snapshot());
        }
    }

    public static <E extends com.android.server.utils.Snappable<E>> void snapshot(android.util.SparseSetArray<E> dst, android.util.SparseSetArray<E> src) {
        if (dst.size() != 0) {
            throw new java.lang.IllegalArgumentException("snapshot destination is not empty");
        }
        int end = src.size();
        for (int i = 0; i < end; i++) {
            int size = src.sizeAt(i);
            for (int j = 0; j < size; j++) {
                dst.add(src.keyAt(i), (com.android.server.utils.Snappable) ((com.android.server.utils.Snappable) src.valueAt(i, j)).snapshot());
            }
        }
    }
}
