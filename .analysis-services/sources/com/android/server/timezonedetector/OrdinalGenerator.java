package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
class OrdinalGenerator<T> {
    private final java.util.function.Function<T, T> mCanonicalizationFunction;
    private final android.util.ArraySet<T> mKnownIds = new android.util.ArraySet<>();

    OrdinalGenerator(java.util.function.Function<T, T> canonicalizationFunction) {
        this.mCanonicalizationFunction = (java.util.function.Function) java.util.Objects.requireNonNull(canonicalizationFunction);
    }

    int ordinal(T object) {
        T canonical = this.mCanonicalizationFunction.apply(object);
        int ordinal = this.mKnownIds.indexOf(canonical);
        if (ordinal < 0) {
            int ordinal2 = this.mKnownIds.size();
            this.mKnownIds.add(canonical);
            return ordinal2;
        }
        return ordinal;
    }

    int[] ordinals(java.util.List<T> objects) {
        int[] ordinals = new int[objects.size()];
        for (int i = 0; i < ordinals.length; i++) {
            ordinals[i] = ordinal(objects.get(i));
        }
        return ordinals;
    }
}
