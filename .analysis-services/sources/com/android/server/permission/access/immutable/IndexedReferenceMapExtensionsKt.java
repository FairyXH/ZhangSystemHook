package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedReferenceMapExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a_\u0010\t\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00062\u001e\u0010\u000b\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\n0\fH\u0086\b\u001a_\u0010\r\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00062\u001e\u0010\u000b\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\n0\fH\u0086\b\u001a_\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00062\u001e\u0010\u0010\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u000f0\fH\u0086\b\u001a_\u0010\u0011\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00062\u001e\u0010\u0010\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u000f0\fH\u0086\b\u001aL\u0010\u0012\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00132\u0006\u0010\u0014\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010\u0015\u001aZ\u0010\u0016\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00132\u0006\u0010\u0014\u001a\u0002H\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0018H\u0086\b¢\u0006\u0002\u0010\u0019\u001a_\u0010\u001a\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00062\u001e\u0010\u000b\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\n0\fH\u0086\b\u001aT\u0010\u001b\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00132\u0006\u0010\u0014\u001a\u0002H\u00022\u0006\u0010\u001c\u001a\u0002H\u0005H\u0086\n¢\u0006\u0002\u0010\u001d\"H\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u000e\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0002\u0010\u0005*\u0002H\u0003*\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00050\u00068Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u001e"}, d2 = {"lastIndex", "", "K", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "Lcom/android/server/permission/access/immutable/IndexedReferenceMap;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IndexedReferenceMap;)I", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "minusAssign", "Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;", "key", "(Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;Ljava/lang/Object;)V", "mutateOrPut", "defaultValue", "Lkotlin/Function0;", "(Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Lcom/android/server/permission/access/immutable/Immutable;", "noneIndexed", "set", "value", "(Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;Ljava/lang/Object;Lcom/android/server/permission/access/immutable/Immutable;)V", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IndexedReferenceMapExtensionsKt {
    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> boolean allIndexed(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super I, java.lang.Boolean> function3) {
        int size = indexedReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedReferenceMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.Immutable value = indexedReferenceMap.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> boolean anyIndexed(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super I, java.lang.Boolean> function3) {
        int size = indexedReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedReferenceMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.Immutable value = indexedReferenceMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> void forEachIndexed(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super I, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = indexedReferenceMap.getSize();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), indexedReferenceMap.keyAt(index), indexedReferenceMap.valueAt(index));
        }
    }

    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> void forEachReversedIndexed(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super I, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = indexedReferenceMap.getSize() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), indexedReferenceMap.keyAt(index), indexedReferenceMap.valueAt(index));
        }
    }

    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> int getLastIndex(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap) {
        return indexedReferenceMap.getSize() - 1;
    }

    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> boolean noneIndexed(com.android.server.permission.access.immutable.IndexedReferenceMap<K, I, M> indexedReferenceMap, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super K, ? super I, java.lang.Boolean> function3) {
        int size = indexedReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object key = indexedReferenceMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.Immutable value = indexedReferenceMap.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), key, value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Incorrect return type in method signature: <K:Ljava/lang/Object;I::Lcom/android/server/permission/access/immutable/Immutable<TM;>;M::TI;>(Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap<TK;TI;TM;>;TK;Lcom/android/server/permission/jarjar/kotlin/jvm/functions/Function0<+TM;>;)TM; */
    public static final com.android.server.permission.access.immutable.Immutable mutateOrPut(com.android.server.permission.access.immutable.MutableIndexedReferenceMap $this$mutateOrPut, java.lang.Object key, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0 defaultValue) {
        com.android.server.permission.access.immutable.Immutable it = $this$mutateOrPut.mutate(key);
        if (it != null) {
            return it;
        }
        java.lang.Object objInvoke = defaultValue.invoke();
        $this$mutateOrPut.put(key, (com.android.server.permission.access.immutable.Immutable) objInvoke);
        return (com.android.server.permission.access.immutable.Immutable) objInvoke;
    }

    public static final <K, I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> void minusAssign(com.android.server.permission.access.immutable.MutableIndexedReferenceMap<K, I, M> mutableIndexedReferenceMap, K k) {
        mutableIndexedReferenceMap.remove(k);
    }

    /* JADX WARN: Incorrect types in method signature: <K:Ljava/lang/Object;I::Lcom/android/server/permission/access/immutable/Immutable<TM;>;M::TI;>(Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap<TK;TI;TM;>;TK;TM;)V */
    public static final void set(com.android.server.permission.access.immutable.MutableIndexedReferenceMap $this$set, java.lang.Object key, com.android.server.permission.access.immutable.Immutable value) {
        $this$set.put(key, value);
    }
}
