package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public final class SparseArrayUtils {
    private SparseArrayUtils() {
    }

    public static <V> java.util.HashSet<V> union(android.util.SparseArray<java.util.HashSet<V>> sets) {
        java.util.HashSet<V> unionSet = new java.util.HashSet<>();
        int n = sets.size();
        for (int i = 0; i < n; i++) {
            java.util.HashSet<V> ithSet = sets.valueAt(i);
            if (ithSet != null) {
                unionSet.addAll(ithSet);
            }
        }
        return unionSet;
    }
}
