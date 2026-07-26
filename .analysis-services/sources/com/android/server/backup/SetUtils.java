package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public final class SetUtils {
    private SetUtils() {
    }

    public static <T> java.util.Set<T> union(java.util.Set<T> set1, java.util.Set<T> set2) {
        java.util.Set<T> unionSet = new java.util.HashSet<>(set1);
        unionSet.addAll(set2);
        return unionSet;
    }
}
