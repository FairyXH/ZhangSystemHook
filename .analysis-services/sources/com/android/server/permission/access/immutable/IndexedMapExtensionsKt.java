package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedMapExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u001f\n\u0002\b\f\u001aE\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001aE\u0010\u000b\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001aR\u0010\f\u001a\u0004\u0018\u0001H\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\r*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\r0\nH\u0086\b¢\u0006\u0002\u0010\u000f\u001aE\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00110\nH\u0086\b\u001aE\u0010\u0013\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00110\nH\u0086\b\u001a@\u0010\u0014\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00152\u0006\u0010\u0016\u001a\u0002H\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0018H\u0086\b¢\u0006\u0002\u0010\u0019\u001a9\u0010\u001a\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u00042\u0006\u0010\u0016\u001a\u0002H\u00022\u0006\u0010\u0017\u001a\u0002H\u0003¢\u0006\u0002\u0010\u001b\u001ah\u0010\u001c\u001a\u0002H\u001d\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\r\"\u000e\b\u0003\u0010\u001d*\b\u0012\u0004\u0012\u0002H\r0\u001e*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001f\u001a\u0002H\u001d2\u001e\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\r0\nH\u0086\b¢\u0006\u0002\u0010 \u001aj\u0010!\u001a\u0002H\u001d\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\r\"\u000e\b\u0003\u0010\u001d*\b\u0012\u0004\u0012\u0002H\r0\u001e*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001f\u001a\u0002H\u001d2 \u0010\u000e\u001a\u001c\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0006\u0012\u0004\u0018\u0001H\r0\nH\u0086\b¢\u0006\u0002\u0010 \u001a2\u0010\"\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00152\u0006\u0010\u0016\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010#\u001aE\u0010$\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a?\u0010%\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00152\u0006\u0010\u0016\u001a\u0002H\u00022\u0006\u0010&\u001a\u0002H\u00032\u0006\u0010\u0017\u001a\u0002H\u0003¢\u0006\u0002\u0010'\u001a:\u0010(\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00152\u0006\u0010\u0016\u001a\u0002H\u00022\u0006\u0010&\u001a\u0002H\u0003H\u0086\n¢\u0006\u0002\u0010)\".\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00048Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006*"}, d2 = {"lastIndex", "", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lcom/android/server/permission/access/immutable/IndexedMap;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IndexedMap;)I", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "firstNotNullOfOrNullIndexed", "R", "transform", "(Lcom/android/server/permission/access/immutable/IndexedMap;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "forEachIndexed", "", "action", "forEachReversedIndexed", "getOrPut", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "key", "defaultValue", "Lkotlin/Function0;", "(Lcom/android/server/permission/access/immutable/MutableIndexedMap;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getWithDefault", "(Lcom/android/server/permission/access/immutable/IndexedMap;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "mapIndexedTo", "C", "", "destination", "(Lcom/android/server/permission/access/immutable/IndexedMap;Ljava/util/Collection;Lkotlin/jvm/functions/Function3;)Ljava/util/Collection;", "mapNotNullIndexedTo", "minusAssign", "(Lcom/android/server/permission/access/immutable/MutableIndexedMap;Ljava/lang/Object;)V", "noneIndexed", "putWithDefault", "value", "(Lcom/android/server/permission/access/immutable/MutableIndexedMap;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "set", "(Lcom/android/server/permission/access/immutable/MutableIndexedMap;Ljava/lang/Object;Ljava/lang/Object;)V", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IndexedMapExtensionsKt {
    public static final <K, V> boolean allIndexed(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedMap.keyAt(index$iv);
            java.lang.Object value = indexedMap.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, V> boolean anyIndexed(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedMap.keyAt(index$iv);
            java.lang.Object value = indexedMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <K, V, R> R firstNotNullOfOrNullIndexed(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, ? extends R> function3) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedMap.keyAt(index$iv);
            java.lang.Object value = indexedMap.valueAt(index$iv);
            int index = index$iv;
            R rInvoke = function3.invoke(java.lang.Integer.valueOf(index), key, value);
            if (rInvoke != null) {
                return rInvoke;
            }
        }
        return null;
    }

    public static final <K, V> void forEachIndexed(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = indexedMap.getSize();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), indexedMap.keyAt(index), indexedMap.valueAt(index));
        }
    }

    public static final <K, V> void forEachReversedIndexed(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = indexedMap.getSize() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), indexedMap.keyAt(index), indexedMap.valueAt(index));
        }
    }

    public static final <K, V> V getWithDefault(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, K k, V v) {
        int index;
        if (indexedMap != null && (index = indexedMap.indexOfKey(k)) >= 0) {
            return indexedMap.valueAt(index);
        }
        return v;
    }

    public static final <K, V> int getLastIndex(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap) {
        return indexedMap.getSize() - 1;
    }

    public static final <K, V> boolean noneIndexed(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedMap.keyAt(index$iv);
            java.lang.Object value = indexedMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, V, R, C extends java.util.Collection<R>> C mapIndexedTo(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, C c, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, ? extends R> function3) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedMap.keyAt(index$iv);
            java.lang.Object value = indexedMap.valueAt(index$iv);
            int index = index$iv;
            c.add(function3.invoke(java.lang.Integer.valueOf(index), key, value));
        }
        return c;
    }

    public static final <K, V, R, C extends java.util.Collection<R>> C mapNotNullIndexedTo(com.android.server.permission.access.immutable.IndexedMap<K, V> indexedMap, C c, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, ? extends R> function3) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedMap.keyAt(index$iv);
            java.lang.Object value = indexedMap.valueAt(index$iv);
            int index = index$iv;
            R rInvoke = function3.invoke(java.lang.Integer.valueOf(index), key, value);
            if (rInvoke != null) {
                c.add(rInvoke);
            }
        }
        return c;
    }

    public static final <K, V> V getOrPut(com.android.server.permission.access.immutable.MutableIndexedMap<K, V> mutableIndexedMap, K k, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends V> function0) {
        V v = mutableIndexedMap.get(k);
        if (v != null) {
            return v;
        }
        V vInvoke = function0.invoke();
        mutableIndexedMap.put(k, vInvoke);
        return vInvoke;
    }

    public static final <K, V> void minusAssign(com.android.server.permission.access.immutable.MutableIndexedMap<K, V> mutableIndexedMap, K k) {
        mutableIndexedMap.remove(k);
    }

    public static final <K, V> V putWithDefault(com.android.server.permission.access.immutable.MutableIndexedMap<K, V> mutableIndexedMap, K k, V v, V v2) {
        int index = mutableIndexedMap.indexOfKey(k);
        if (index >= 0) {
            V vValueAt = mutableIndexedMap.valueAt(index);
            if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(v, vValueAt)) {
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(v, v2)) {
                    mutableIndexedMap.removeAt(index);
                } else {
                    mutableIndexedMap.putAt(index, v);
                }
            }
            return vValueAt;
        }
        if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(v, v2)) {
            mutableIndexedMap.put(k, v);
        }
        return v2;
    }

    public static final <K, V> void set(com.android.server.permission.access.immutable.MutableIndexedMap<K, V> mutableIndexedMap, K k, V v) {
        mutableIndexedMap.put(k, v);
    }
}
