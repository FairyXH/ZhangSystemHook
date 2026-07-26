package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class RotationCache<T, R> {
    private final android.util.SparseArray<R> mCache = new android.util.SparseArray<>(4);
    private T mCachedFor;
    private final com.android.server.wm.utils.RotationCache.RotationDependentComputation<T, R> mComputation;

    @java.lang.FunctionalInterface
    public interface RotationDependentComputation<T, R> {
        R compute(T t, int i);
    }

    public RotationCache(com.android.server.wm.utils.RotationCache.RotationDependentComputation<T, R> computation) {
        this.mComputation = computation;
    }

    public R getOrCompute(T t, int rotation) {
        if (t != this.mCachedFor) {
            this.mCache.clear();
            this.mCachedFor = t;
        }
        int idx = this.mCache.indexOfKey(rotation);
        if (idx >= 0) {
            return this.mCache.valueAt(idx);
        }
        R result = this.mComputation.compute(t, rotation);
        this.mCache.put(rotation, result);
        return result;
    }
}
