package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntReferenceMapExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u001aS\u0010\b\u001a\u00020\t\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00052\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001aS\u0010\f\u001a\u00020\t\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00052\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001aS\u0010\r\u001a\u00020\u000e\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00052\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000e0\u000bH\u0086\b\u001aS\u0010\u0010\u001a\u00020\u000e\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00052\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000e0\u000bH\u0086\b\u001a;\u0010\u0011\u001a\u00020\u000e\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00122\u0006\u0010\u0013\u001a\u00020\u0001H\u0086\u0002\u001aN\u0010\u0014\u001a\u0002H\u0004\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00122\u0006\u0010\u0013\u001a\u00020\u00012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0016H\u0086\b¢\u0006\u0002\u0010\u0017\u001aS\u0010\u0018\u001a\u00020\t\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00052\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001aH\u0010\u0019\u001a\u00020\u000e\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00122\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u0002H\u0004H\u0086\u0002¢\u0006\u0002\u0010\u001b\"<\u0010\u0000\u001a\u00020\u0001\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\b\b\u0001\u0010\u0004*\u0002H\u0002*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u001c"}, d2 = {"lastIndex", "", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IntReferenceMap;)I", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "minusAssign", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "key", "mutateOrPut", "defaultValue", "Lkotlin/Function0;", "(Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;ILkotlin/jvm/functions/Function0;)Lcom/android/server/permission/access/immutable/Immutable;", "noneIndexed", "set", "value", "(Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;ILcom/android/server/permission/access/immutable/Immutable;)V", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IntReferenceMapExtensionsKt {
    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> boolean allIndexed(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super I, java.lang.Boolean> function3) {
        int size = intReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intReferenceMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.Immutable value = intReferenceMap.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> boolean anyIndexed(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super I, java.lang.Boolean> function3) {
        int size = intReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intReferenceMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.Immutable value = intReferenceMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> void forEachIndexed(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super I, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = intReferenceMap.getSize();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(intReferenceMap.keyAt(index)), intReferenceMap.valueAt(index));
        }
    }

    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> void forEachReversedIndexed(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super I, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = intReferenceMap.getSize() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(intReferenceMap.keyAt(index)), intReferenceMap.valueAt(index));
        }
    }

    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> int getLastIndex(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap) {
        return intReferenceMap.getSize() - 1;
    }

    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> boolean noneIndexed(com.android.server.permission.access.immutable.IntReferenceMap<I, M> intReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super I, java.lang.Boolean> function3) {
        int size = intReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = intReferenceMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.Immutable value = intReferenceMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Incorrect return type in method signature: <I::Lcom/android/server/permission/access/immutable/Immutable<TM;>;M::TI;>(Lcom/android/server/permission/access/immutable/MutableIntReferenceMap<TI;TM;>;ILcom/android/server/permission/jarjar/kotlin/jvm/functions/Function0<+TM;>;)TM; */
    public static final com.android.server.permission.access.immutable.Immutable mutateOrPut(com.android.server.permission.access.immutable.MutableIntReferenceMap $this$mutateOrPut, int key, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0 defaultValue) {
        com.android.server.permission.access.immutable.Immutable it = $this$mutateOrPut.mutate(key);
        if (it != null) {
            return it;
        }
        java.lang.Object objInvoke = defaultValue.invoke();
        $this$mutateOrPut.put(key, (com.android.server.permission.access.immutable.Immutable) objInvoke);
        return (com.android.server.permission.access.immutable.Immutable) objInvoke;
    }

    public static final <I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> void minusAssign(com.android.server.permission.access.immutable.MutableIntReferenceMap<I, M> mutableIntReferenceMap, int key) {
        mutableIntReferenceMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().remove(key);
        com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        com.android.server.permission.access.immutable.IntMapKt.gc(mutableIntReferenceMap.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar());
    }

    /* JADX WARN: Incorrect types in method signature: <I::Lcom/android/server/permission/access/immutable/Immutable<TM;>;M::TI;>(Lcom/android/server/permission/access/immutable/MutableIntReferenceMap<TI;TM;>;ITM;)V */
    public static final void set(com.android.server.permission.access.immutable.MutableIntReferenceMap $this$set, int key, com.android.server.permission.access.immutable.Immutable value) {
        $this$set.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().put(key, new com.android.server.permission.access.immutable.MutableReference(value));
    }
}
