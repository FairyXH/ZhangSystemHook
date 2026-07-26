package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: GroupingJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aZ\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0081\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, d2 = {"eachCount", "", "K", "", "T", "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "f", "Lkotlin/Function1;", "", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/GroupingKt")
class GroupingKt__GroupingJVMKt {
    public static final <T, K> java.util.Map<K, java.lang.Integer> eachCount(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        java.util.Map destination$iv = new java.util.LinkedHashMap();
        com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping2 = grouping;
        java.util.Iterator<T> itSourceIterator = grouping2.sourceIterator();
        while (itSourceIterator.hasNext()) {
            K kKeyOf = grouping2.keyOf(itSourceIterator.next());
            java.lang.Object accumulator$iv$iv = destination$iv.get(kKeyOf);
            boolean first$iv = accumulator$iv$iv == null && !destination$iv.containsKey(kKeyOf);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef acc = (com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef) (first$iv ? new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef() : accumulator$iv$iv);
            acc.element++;
            destination$iv.put(kKeyOf, acc);
            grouping2 = grouping2;
        }
        for (java.util.Map.Entry it : destination$iv.entrySet()) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(it, "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4, R of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4>");
            com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableMapEntry(it).setValue(java.lang.Integer.valueOf(((com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.IntRef) it.getValue()).element));
        }
        return com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableMap(destination$iv);
    }

    private static final <K, V, R> java.util.Map<K, R> mapValuesInPlace(java.util.Map<K, V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "f");
        java.lang.Iterable $this$forEach$iv = map.entrySet();
        for (java.lang.Object element$iv : $this$forEach$iv) {
            java.util.Map.Entry it = (java.util.Map.Entry) element$iv;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(it, "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4, R of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda$4>");
            com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableMapEntry(it).setValue(function1.invoke(it));
        }
        return com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableMap(map);
    }
}
