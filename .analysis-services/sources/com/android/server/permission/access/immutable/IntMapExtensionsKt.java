package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntMapExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a9\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\tH\u0086\b\u001a9\u0010\n\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\tH\u0086\b\u001aF\u0010\u000b\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\f*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\r\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\f0\tH\u0086\b¢\u0006\u0002\u0010\u000e\u001a9\u0010\u000f\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\u0011\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00100\tH\u0086\b\u001a9\u0010\u0012\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\u0011\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00100\tH\u0086\b\u001a4\u0010\u0013\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0017H\u0086\b¢\u0006\u0002\u0010\u0018\u001a-\u0010\u0019\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u00032\u0006\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u0002H\u0002¢\u0006\u0002\u0010\u001a\u001a!\u0010\u001b\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0001H\u0086\u0002\u001a9\u0010\u001c\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\tH\u0086\b\u001a3\u0010\u001d\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u0002H\u00022\u0006\u0010\u0016\u001a\u0002H\u0002¢\u0006\u0002\u0010\u001f\u001a.\u0010 \u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010!\"\"\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\""}, d2 = {"lastIndex", "", "T", "Lcom/android/server/permission/access/immutable/IntMap;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IntMap;)I", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "firstNotNullOfOrNullIndexed", "R", "transform", "(Lcom/android/server/permission/access/immutable/IntMap;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "forEachIndexed", "", "action", "forEachReversedIndexed", "getOrPut", "Lcom/android/server/permission/access/immutable/MutableIntMap;", "key", "defaultValue", "Lkotlin/Function0;", "(Lcom/android/server/permission/access/immutable/MutableIntMap;ILkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getWithDefault", "(Lcom/android/server/permission/access/immutable/IntMap;ILjava/lang/Object;)Ljava/lang/Object;", "minusAssign", "noneIndexed", "putWithDefault", "value", "(Lcom/android/server/permission/access/immutable/MutableIntMap;ILjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "set", "(Lcom/android/server/permission/access/immutable/MutableIntMap;ILjava/lang/Object;)V", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IntMapExtensionsKt {
    public static final <T> boolean allIndexed(com.android.server.permission.access.immutable.IntMap<T> intMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        int size = intMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intMap.keyAt(index$iv);
            java.lang.Object value = intMap.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean anyIndexed(com.android.server.permission.access.immutable.IntMap<T> intMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        int size = intMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intMap.keyAt(index$iv);
            java.lang.Object value = intMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <T, R> R firstNotNullOfOrNullIndexed(com.android.server.permission.access.immutable.IntMap<T> intMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, ? extends R> function3) {
        int size = intMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intMap.keyAt(index$iv);
            java.lang.Object value = intMap.valueAt(index$iv);
            int index = index$iv;
            R rInvoke = function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value);
            if (rInvoke != null) {
                return rInvoke;
            }
        }
        return null;
    }

    public static final <T> void forEachIndexed(com.android.server.permission.access.immutable.IntMap<T> intMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = intMap.getSize();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(intMap.keyAt(index)), intMap.valueAt(index));
        }
    }

    public static final <T> void forEachReversedIndexed(com.android.server.permission.access.immutable.IntMap<T> intMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = intMap.getSize() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(intMap.keyAt(index)), intMap.valueAt(index));
        }
    }

    public static final <T> T getWithDefault(com.android.server.permission.access.immutable.IntMap<T> intMap, int key, T t) {
        int index;
        if (intMap != null && (index = intMap.indexOfKey(key)) >= 0) {
            return intMap.valueAt(index);
        }
        return t;
    }

    public static final <T> int getLastIndex(com.android.server.permission.access.immutable.IntMap<T> intMap) {
        return intMap.getSize() - 1;
    }

    public static final <T> boolean noneIndexed(com.android.server.permission.access.immutable.IntMap<T> intMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        int size = intMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intMap.keyAt(index$iv);
            java.lang.Object value = intMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> T getOrPut(com.android.server.permission.access.immutable.MutableIntMap<T> mutableIntMap, int key, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        T t = mutableIntMap.get(key);
        if (t != null) {
            return t;
        }
        T tInvoke = function0.invoke();
        mutableIntMap.put(key, tInvoke);
        return tInvoke;
    }

    public static final <T> void minusAssign(com.android.server.permission.access.immutable.MutableIntMap<T> mutableIntMap, int key) {
        mutableIntMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(key);
        com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        com.android.server.permission.access.immutable.IntMapKt.gc(mutableIntMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
    }

    public static final <T> T putWithDefault(com.android.server.permission.access.immutable.MutableIntMap<T> mutableIntMap, int key, T t, T t2) {
        int index = mutableIntMap.indexOfKey(key);
        if (index >= 0) {
            T tValueAt = mutableIntMap.valueAt(index);
            if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(t, tValueAt)) {
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(t, t2)) {
                    mutableIntMap.removeAt(index);
                } else {
                    mutableIntMap.putAt(index, t);
                }
            }
            return tValueAt;
        }
        if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(t, t2)) {
            mutableIntMap.put(key, t);
        }
        return t2;
    }

    public static final <T> void set(com.android.server.permission.access.immutable.MutableIntMap<T> mutableIntMap, int key, T t) {
        mutableIntMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().put(key, t);
    }
}
