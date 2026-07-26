package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: MapsJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000d\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\u001a4\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007H\u0001\u001aQ\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052\u0006\u0010\t\u001a\u00020\u00012#\u0010\n\u001a\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007\u0012\u0004\u0012\u00020\f0\u000b¢\u0006\u0002\b\rH\u0081\bø\u0001\u0000\u001aI\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052#\u0010\n\u001a\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007\u0012\u0004\u0012\u00020\f0\u000b¢\u0006\u0002\b\rH\u0081\bø\u0001\u0000\u001a \u0010\u000e\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005H\u0001\u001a(\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a\u0010\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0001H\u0001\u001a2\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0013\u001aa\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0015\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052\u000e\u0010\u0016\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00040\u00172*\u0010\u0018\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00130\u0019\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0013H\u0007¢\u0006\u0002\u0010\u001a\u001aY\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0015\"\u000e\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u001b\"\u0004\b\u0001\u0010\u00052*\u0010\u0018\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00130\u0019\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0013¢\u0006\u0002\u0010\u001c\u001aC\u0010\u001d\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u001e2\u0006\u0010\u001f\u001a\u0002H\u00042\f\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00050!H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\"\u001a\u0019\u0010#\u001a\u00020$*\u000e\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020%0\u0003H\u0087\b\u001a2\u0010&\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0000\u001a1\u0010'\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0081\b\u001a:\u0010(\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0015\"\u000e\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u001b\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\u001a@\u0010(\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0015\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u000e\u0010\u0016\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00040\u0017\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006)"}, d2 = {"INT_MAX_POWER_OF_TWO", "", "build", "", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "builder", "", "buildMapInternal", "capacity", "builderAction", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "createMapBuilder", "mapCapacity", "expectedSize", "mapOf", "pair", "Lkotlin/Pair;", "sortedMapOf", "Ljava/util/SortedMap;", "comparator", "Ljava/util/Comparator;", "pairs", "", "(Ljava/util/Comparator;[Lkotlin/Pair;)Ljava/util/SortedMap;", "", "([Lkotlin/Pair;)Ljava/util/SortedMap;", "getOrPut", "Ljava/util/concurrent/ConcurrentMap;", "key", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/concurrent/ConcurrentMap;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "toProperties", "Ljava/util/Properties;", "", "toSingletonMap", "toSingletonMapOrSelf", "toSortedMap", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/MapsKt")
public class MapsKt__MapsJVMKt extends com.android.server.permission.jarjar.kotlin.collections.MapsKt__MapWithDefaultKt {
    private static final int INT_MAX_POWER_OF_TWO = 1073741824;

    public static final <K, V> java.util.Map<K, V> mapOf(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V> pair) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pair, "pair");
        java.util.Map<K, V> mapSingletonMap = java.util.Collections.singletonMap(pair.getFirst(), pair.getSecond());
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(mapSingletonMap, "singletonMap(...)");
        return mapSingletonMap;
    }

    private static final <K, V> java.util.Map<K, V> buildMapInternal(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map<K, V>, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "builderAction");
        java.util.Map mapCreateMapBuilder = com.android.server.permission.jarjar.kotlin.collections.MapsKt.createMapBuilder();
        function1.invoke(mapCreateMapBuilder);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.build(mapCreateMapBuilder);
    }

    private static final <K, V> java.util.Map<K, V> buildMapInternal(int capacity, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map<K, V>, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "builderAction");
        java.util.Map mapCreateMapBuilder = com.android.server.permission.jarjar.kotlin.collections.MapsKt.createMapBuilder(capacity);
        function1.invoke(mapCreateMapBuilder);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.build(mapCreateMapBuilder);
    }

    public static final <K, V> java.util.Map<K, V> createMapBuilder() {
        return new com.android.server.permission.jarjar.kotlin.collections.builders.MapBuilder();
    }

    public static final <K, V> java.util.Map<K, V> createMapBuilder(int capacity) {
        return new com.android.server.permission.jarjar.kotlin.collections.builders.MapBuilder(capacity);
    }

    public static final <K, V> java.util.Map<K, V> build(java.util.Map<K, V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "builder");
        return ((com.android.server.permission.jarjar.kotlin.collections.builders.MapBuilder) map).build();
    }

    public static final <K, V> V getOrPut(java.util.concurrent.ConcurrentMap<K, V> concurrentMap, K k, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends V> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(concurrentMap, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "defaultValue");
        V v = concurrentMap.get(k);
        if (v != null) {
            return v;
        }
        V vInvoke = function0.invoke();
        V vPutIfAbsent = concurrentMap.putIfAbsent(k, vInvoke);
        return vPutIfAbsent == null ? vInvoke : vPutIfAbsent;
    }

    public static final <K extends java.lang.Comparable<? super K>, V> java.util.SortedMap<K, V> toSortedMap(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return new java.util.TreeMap(map);
    }

    public static final <K, V> java.util.SortedMap<K, V> toSortedMap(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super K> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        java.util.TreeMap $this$toSortedMap_u24lambda_u241 = new java.util.TreeMap(comparator);
        $this$toSortedMap_u24lambda_u241.putAll(map);
        return $this$toSortedMap_u24lambda_u241;
    }

    public static final <K extends java.lang.Comparable<? super K>, V> java.util.SortedMap<K, V> sortedMapOf(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>... pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        java.util.TreeMap $this$sortedMapOf_u24lambda_u242 = new java.util.TreeMap();
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll($this$sortedMapOf_u24lambda_u242, pairArr);
        return $this$sortedMapOf_u24lambda_u242;
    }

    public static final <K, V> java.util.SortedMap<K, V> sortedMapOf(java.util.Comparator<? super K> comparator, com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>... pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        java.util.TreeMap $this$sortedMapOf_u24lambda_u243 = new java.util.TreeMap(comparator);
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll($this$sortedMapOf_u24lambda_u243, pairArr);
        return $this$sortedMapOf_u24lambda_u243;
    }

    private static final java.util.Properties toProperties(java.util.Map<java.lang.String, java.lang.String> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        java.util.Properties $this$toProperties_u24lambda_u244 = new java.util.Properties();
        $this$toProperties_u24lambda_u244.putAll(map);
        return $this$toProperties_u24lambda_u244;
    }

    private static final <K, V> java.util.Map<K, V> toSingletonMapOrSelf(java.util.Map<K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toSingletonMap(map);
    }

    public static final <K, V> java.util.Map<K, V> toSingletonMap(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        java.util.Map.Entry<? extends K, ? extends V> next = map.entrySet().iterator().next();
        java.util.Map<K, V> mapSingletonMap = java.util.Collections.singletonMap(next.getKey(), next.getValue());
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(mapSingletonMap, "with(...)");
        return mapSingletonMap;
    }

    public static final int mapCapacity(int expectedSize) {
        if (expectedSize < 0) {
            return expectedSize;
        }
        if (expectedSize < 3) {
            return expectedSize + 1;
        }
        if (expectedSize < 1073741824) {
            return (int) ((expectedSize / 0.75f) + 1.0f);
        }
        return Integer.MAX_VALUE;
    }
}
