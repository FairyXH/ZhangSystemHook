package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Grouping.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u001a\u009e\u0001\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\bø\u0001\u0000\u001a·\u0001\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aI\u0010\u0014\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0016\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00150\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u0010H\u0007¢\u0006\u0002\u0010\u0016\u001a¿\u0001\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u000526\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\bø\u0001\u0000\u001a\u007f\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001aØ\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u001026\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a\u0093\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u001f\u001a\u008b\u0001\u0010 \u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0001\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\bø\u0001\u0000\u001a¤\u0001\u0010\"\u001a\u0002H\u0010\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010#\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006$"}, d2 = {"aggregate", "", "K", "R", "T", "Lkotlin/collections/Grouping;", "operation", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "key", "accumulator", "element", "", "first", "aggregateTo", "M", "", "destination", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function4;)Ljava/util/Map;", "eachCountTo", "", "(Lkotlin/collections/Grouping;Ljava/util/Map;)Ljava/util/Map;", "fold", "initialValueSelector", "Lkotlin/Function2;", "Lkotlin/Function3;", "initialValue", "(Lkotlin/collections/Grouping;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "foldTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "(Lkotlin/collections/Grouping;Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "reduce", "S", "reduceTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/GroupingKt")
class GroupingKt__GroupingKt extends com.android.server.permission.jarjar.kotlin.collections.GroupingKt__GroupingJVMKt {
    public static final <T, K, R> java.util.Map<K, R> aggregate(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, com.android.server.permission.jarjar.kotlin.jvm.functions.Function4<? super K, ? super R, ? super T, ? super java.lang.Boolean, ? extends R> function4) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function4, "operation");
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            java.lang.Object key$iv = grouping.keyOf(next);
            java.lang.Object accumulator$iv = linkedHashMap.get(key$iv);
            linkedHashMap.put(key$iv, function4.invoke(key$iv, accumulator$iv, next, java.lang.Boolean.valueOf(accumulator$iv == null && !linkedHashMap.containsKey(key$iv))));
        }
        return linkedHashMap;
    }

    public static final <T, K, R, M extends java.util.Map<? super K, R>> M aggregateTo(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function4<? super K, ? super R, ? super T, ? super java.lang.Boolean, ? extends R> function4) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function4, "operation");
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            java.lang.Object key = grouping.keyOf(next);
            java.lang.Object accumulator = m.get(key);
            m.put(key, function4.invoke(key, accumulator, next, java.lang.Boolean.valueOf(accumulator == null && !m.containsKey(key))));
        }
        return m;
    }

    public static final <T, K, R> java.util.Map<K, R> fold(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super K, ? super T, ? extends R> function2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super K, ? super R, ? super T, ? extends R> function3) {
        int $i$f$fold;
        java.lang.Object key;
        java.lang.Object objInvoke;
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super K, ? super T, ? extends R> function22 = function2;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function22, "initialValueSelector");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "operation");
        int $i$f$fold2 = 0;
        java.util.Map destination$iv$iv = new java.util.LinkedHashMap();
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            K kKeyOf = grouping.keyOf(next);
            java.lang.Object accumulator$iv$iv = destination$iv$iv.get(kKeyOf);
            boolean first = accumulator$iv$iv == null && !destination$iv$iv.containsKey(kKeyOf);
            if (first) {
                $i$f$fold = $i$f$fold2;
                key = kKeyOf;
                objInvoke = function22.invoke(key, next);
            } else {
                $i$f$fold = $i$f$fold2;
                key = kKeyOf;
                objInvoke = accumulator$iv$iv;
            }
            destination$iv$iv.put(kKeyOf, function3.invoke(key, objInvoke, next));
            function22 = function2;
            $i$f$fold2 = $i$f$fold;
        }
        return destination$iv$iv;
    }

    public static final <T, K, R, M extends java.util.Map<? super K, R>> M foldTo(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super K, ? super T, ? extends R> function2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super K, ? super R, ? super T, ? extends R> function3) {
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super K, ? super T, ? extends R> function22 = function2;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function22, "initialValueSelector");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "operation");
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            java.lang.Object key$iv = grouping.keyOf(next);
            java.lang.Object accumulator$iv = m.get(key$iv);
            boolean first = accumulator$iv == null && !m.containsKey(key$iv);
            m.put(key$iv, function3.invoke(key$iv, first ? function22.invoke(key$iv, next) : accumulator$iv, next));
            function22 = function2;
        }
        return m;
    }

    public static final <T, K, R> java.util.Map<K, R> fold(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, R r, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super T, ? extends R> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "operation");
        int $i$f$fold = 0;
        java.util.Map destination$iv$iv = new java.util.LinkedHashMap();
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            K kKeyOf = grouping.keyOf(next);
            java.lang.Object accumulator$iv$iv = destination$iv$iv.get(kKeyOf);
            boolean first = accumulator$iv$iv == null && !destination$iv$iv.containsKey(kKeyOf);
            int $i$f$fold2 = $i$f$fold;
            destination$iv$iv.put(kKeyOf, function2.invoke(first ? r : accumulator$iv$iv, next));
            $i$f$fold = $i$f$fold2;
        }
        return destination$iv$iv;
    }

    public static final <T, K, R, M extends java.util.Map<? super K, R>> M foldTo(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, M m, R r, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super T, ? extends R> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "operation");
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            K kKeyOf = grouping.keyOf(next);
            java.lang.Object accumulator$iv = m.get(kKeyOf);
            boolean first = accumulator$iv == null && !m.containsKey(kKeyOf);
            m.put(kKeyOf, function2.invoke(first ? r : accumulator$iv, next));
        }
        return m;
    }

    public static final <S, T extends S, K> java.util.Map<K, S> reduce(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super K, ? super S, ? super T, ? extends S> function3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "operation");
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            java.lang.Object key$iv$iv = grouping.keyOf(next);
            java.lang.Object accumulator$iv$iv = linkedHashMap.get(key$iv$iv);
            boolean first = accumulator$iv$iv == null && !linkedHashMap.containsKey(key$iv$iv);
            java.lang.Object e = next;
            if (!first) {
                e = function3.invoke(key$iv$iv, accumulator$iv$iv, e);
            }
            linkedHashMap.put(key$iv$iv, e);
        }
        return linkedHashMap;
    }

    public static final <S, T extends S, K, M extends java.util.Map<? super K, S>> M reduceTo(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super K, ? super S, ? super T, ? extends S> function3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "operation");
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            T next = itSourceIterator.next();
            java.lang.Object key$iv = grouping.keyOf(next);
            java.lang.Object accumulator$iv = m.get(key$iv);
            boolean first = accumulator$iv == null && !m.containsKey(key$iv);
            java.lang.Object e = next;
            if (!first) {
                e = function3.invoke(key$iv, accumulator$iv, e);
            }
            m.put(key$iv, e);
        }
        return m;
    }

    public static final <T, K, M extends java.util.Map<? super K, java.lang.Integer>> M eachCountTo(com.android.server.permission.jarjar.kotlin.collections.Grouping<T, ? extends K> grouping, M m) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(grouping, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        java.util.Iterator<T> itSourceIterator = grouping.sourceIterator();
        while (itSourceIterator.hasNext()) {
            K kKeyOf = grouping.keyOf(itSourceIterator.next());
            java.lang.Object accumulator$iv$iv = m.get(kKeyOf);
            boolean first$iv = accumulator$iv$iv == null && !m.containsKey(kKeyOf);
            int acc = ((java.lang.Number) (first$iv ? 0 : accumulator$iv$iv)).intValue();
            m.put(kKeyOf, java.lang.Integer.valueOf(acc + 1));
        }
        return m;
    }
}
