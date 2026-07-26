package com.android.server.permission.access.collection;

/* JADX INFO: compiled from: ArrayMapExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\u001aE\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001aE\u0010\u000b\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001aE\u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\r0\nH\u0086\b\u001aE\u0010\u000f\u001a\u00020\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\r0\nH\u0086\b\u001a@\u0010\u0010\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0011\u001a\u0002H\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0013H\u0086\b¢\u0006\u0002\u0010\u0014\u001a2\u0010\u0015\u001a\u00020\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0011\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010\u0016\u001aE\u0010\u0017\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001aE\u0010\u0018\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001aE\u0010\u0019\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a:\u0010\u001a\u001a\u00020\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0011\u001a\u0002H\u00022\u0006\u0010\u001b\u001a\u0002H\u0003H\u0086\n¢\u0006\u0002\u0010\u001c\".\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00048Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u001d"}, d2 = {"lastIndex", "", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Landroid/util/ArrayMap;", "getLastIndex", "(Landroid/util/ArrayMap;)I", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "getOrPut", "key", "defaultValue", "Lkotlin/Function0;", "(Landroid/util/ArrayMap;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "minusAssign", "(Landroid/util/ArrayMap;Ljava/lang/Object;)V", "noneIndexed", "removeAllIndexed", "retainAllIndexed", "set", "value", "(Landroid/util/ArrayMap;Ljava/lang/Object;Ljava/lang/Object;)V", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ArrayMapExtensionsKt {
    public static final <K, V> boolean allIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        int size = arrayMap.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = arrayMap.keyAt(index$iv);
            java.lang.Object value = arrayMap.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, V> boolean anyIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        int size = arrayMap.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = arrayMap.keyAt(index$iv);
            java.lang.Object value = arrayMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <K, V> void forEachIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = arrayMap.size();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), arrayMap.keyAt(index), arrayMap.valueAt(index));
        }
    }

    public static final <K, V> void forEachReversedIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = arrayMap.size() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), arrayMap.keyAt(index), arrayMap.valueAt(index));
        }
    }

    public static final <K, V> V getOrPut(android.util.ArrayMap<K, V> arrayMap, K k, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends V> function0) {
        V v = arrayMap.get(k);
        if (v != null) {
            return v;
        }
        V vInvoke = function0.invoke();
        arrayMap.put(k, vInvoke);
        return vInvoke;
    }

    public static final <K, V> int getLastIndex(android.util.ArrayMap<K, V> arrayMap) {
        return arrayMap.size() - 1;
    }

    public static final <K, V> void minusAssign(android.util.ArrayMap<K, V> arrayMap, K k) {
        arrayMap.remove(k);
    }

    public static final <K, V> boolean noneIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        int size = arrayMap.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = arrayMap.keyAt(index$iv);
            java.lang.Object value = arrayMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, V> boolean removeAllIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        boolean isChanged = false;
        for (int index$iv = arrayMap.size() - 1; -1 < index$iv; index$iv--) {
            java.lang.Object key = arrayMap.keyAt(index$iv);
            java.lang.Object value = arrayMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                arrayMap.removeAt(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final <K, V> boolean retainAllIndexed(android.util.ArrayMap<K, V> arrayMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super V, java.lang.Boolean> function3) {
        boolean isChanged = false;
        for (int index$iv = arrayMap.size() - 1; -1 < index$iv; index$iv--) {
            java.lang.Object key = arrayMap.keyAt(index$iv);
            java.lang.Object value = arrayMap.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                arrayMap.removeAt(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final <K, V> void set(android.util.ArrayMap<K, V> arrayMap, K k, V v) {
        arrayMap.put(k, v);
    }
}
