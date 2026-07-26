package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: _Sets.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004¨\u0006\r"}, d2 = {"minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/SetsKt")
class SetsKt___SetsKt extends com.android.server.permission.jarjar.kotlin.collections.SetsKt__SetsKt {
    public static final <T> java.util.Set<T> minus(java.util.Set<? extends T> set, T t) {
        boolean z;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        java.lang.Iterable result = new java.util.LinkedHashSet(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(set.size()));
        boolean removed = false;
        java.util.Set<? extends T> $this$filterTo$iv = set;
        for (T t2 : $this$filterTo$iv) {
            if (removed || !com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(t2, t)) {
                z = true;
            } else {
                removed = true;
                z = false;
            }
            if (z) {
                ((java.util.Collection) result).add(t2);
            }
        }
        java.lang.Iterable $this$filterTo$iv2 = (java.util.Collection) result;
        return (java.util.Set) $this$filterTo$iv2;
    }

    public static final <T> java.util.Set<T> minus(java.util.Set<? extends T> set, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(set);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(result, tArr);
        return result;
    }

    public static final <T> java.util.Set<T> minus(java.util.Set<? extends T> set, java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        java.util.Collection<?> collectionConvertToListIfNotCollection = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.convertToListIfNotCollection(iterable);
        if (collectionConvertToListIfNotCollection.isEmpty()) {
            return com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.toSet(set);
        }
        if (collectionConvertToListIfNotCollection instanceof java.util.Set) {
            java.util.Set<? extends T> $this$filterNotTo$iv = set;
            java.util.Collection destination$iv = new java.util.LinkedHashSet();
            for (T t : $this$filterNotTo$iv) {
                if (!collectionConvertToListIfNotCollection.contains(t)) {
                    destination$iv.add(t);
                }
            }
            return (java.util.Set) destination$iv;
        }
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(set);
        result.removeAll(collectionConvertToListIfNotCollection);
        return result;
    }

    public static final <T> java.util.Set<T> minus(java.util.Set<? extends T> set, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(set);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(result, sequence);
        return result;
    }

    private static final <T> java.util.Set<T> minusElement(java.util.Set<? extends T> set, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        return com.android.server.permission.jarjar.kotlin.collections.SetsKt.minus(set, t);
    }

    public static final <T> java.util.Set<T> plus(java.util.Set<? extends T> set, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(set.size() + 1));
        result.addAll(set);
        result.add(t);
        return result;
    }

    public static final <T> java.util.Set<T> plus(java.util.Set<? extends T> set, T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "elements");
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(set.size() + tArr.length));
        result.addAll(set);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(result, tArr);
        return result;
    }

    public static final <T> java.util.Set<T> plus(java.util.Set<? extends T> set, java.lang.Iterable<? extends T> iterable) {
        int size;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "elements");
        java.lang.Integer numCollectionSizeOrNull = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.collectionSizeOrNull(iterable);
        if (numCollectionSizeOrNull != null) {
            int it = numCollectionSizeOrNull.intValue();
            size = set.size() + it;
        } else {
            size = set.size() * 2;
        }
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(size));
        result.addAll(set);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(result, iterable);
        return result;
    }

    public static final <T> java.util.Set<T> plus(java.util.Set<? extends T> set, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "elements");
        java.util.LinkedHashSet result = new java.util.LinkedHashSet(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(set.size() * 2));
        result.addAll(set);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(result, sequence);
        return result;
    }

    private static final <T> java.util.Set<T> plusElement(java.util.Set<? extends T> set, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "<this>");
        return com.android.server.permission.jarjar.kotlin.collections.SetsKt.plus(set, t);
    }
}
