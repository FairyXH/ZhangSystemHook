package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedSetExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u001e\n\u0000\u001a+\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\b\"\u0002H\u0002¢\u0006\u0002\u0010\t\u001a3\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000b0\rH\u0086\b\u001a3\u0010\u000e\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000b0\rH\u0086\b\u001a3\u0010\u000f\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u0011\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00100\rH\u0086\b\u001a3\u0010\u0012\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u0011\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00100\rH\u0086\b\u001a,\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0014\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0015\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0016\u001a&\u0010\u0017\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\u0006\u0010\u0015\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010\u0018\u001a3\u0010\u0019\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000b0\rH\u0086\b\u001a,\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0014\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0015\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0016\u001a&\u0010\u001b\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\u0006\u0010\u0015\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010\u0018\u001a'\u0010\u001b\u001a\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00142\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0086\u0002\"\"\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u001e"}, d2 = {"lastIndex", "", "T", "Lcom/android/server/permission/access/immutable/IndexedSet;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IndexedSet;)I", "indexedSetOf", "elements", "", "([Ljava/lang/Object;)Lcom/android/server/permission/access/immutable/IndexedSet;", "allIndexed", "", "predicate", "Lkotlin/Function2;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "minus", "Lcom/android/server/permission/access/immutable/MutableIndexedSet;", "element", "(Lcom/android/server/permission/access/immutable/IndexedSet;Ljava/lang/Object;)Lcom/android/server/permission/access/immutable/MutableIndexedSet;", "minusAssign", "(Lcom/android/server/permission/access/immutable/MutableIndexedSet;Ljava/lang/Object;)V", "noneIndexed", "plus", "plusAssign", "collection", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IndexedSetExtensionsKt {
    public static final <T> com.android.server.permission.access.immutable.IndexedSet<T> indexedSetOf(T... tArr) {
        return new com.android.server.permission.access.immutable.MutableIndexedSet(new android.util.ArraySet(com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(tArr)));
    }

    public static final <T> boolean allIndexed(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = indexedSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedSet.elementAt(index$iv);
            int index = index$iv;
            if (!function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean anyIndexed(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = indexedSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedSet.elementAt(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <T> void forEachIndexed(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        int size = indexedSet.getSize();
        for (int index = 0; index < size; index++) {
            function2.invoke(java.lang.Integer.valueOf(index), indexedSet.elementAt(index));
        }
    }

    public static final <T> void forEachReversedIndexed(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        for (int index = indexedSet.getSize() - 1; -1 < index; index--) {
            function2.invoke(java.lang.Integer.valueOf(index), indexedSet.elementAt(index));
        }
    }

    public static final <T> int getLastIndex(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet) {
        return indexedSet.getSize() - 1;
    }

    public static final <T> com.android.server.permission.access.immutable.MutableIndexedSet<T> minus(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, T t) {
        com.android.server.permission.access.immutable.MutableIndexedSet<T> mutable = indexedSet.toMutable();
        mutable.remove(t);
        return mutable;
    }

    public static final <T> boolean noneIndexed(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = indexedSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedSet.elementAt(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> com.android.server.permission.access.immutable.MutableIndexedSet<T> plus(com.android.server.permission.access.immutable.IndexedSet<T> indexedSet, T t) {
        com.android.server.permission.access.immutable.MutableIndexedSet<T> mutable = indexedSet.toMutable();
        mutable.add(t);
        return mutable;
    }

    public static final <T> void minusAssign(com.android.server.permission.access.immutable.MutableIndexedSet<T> mutableIndexedSet, T t) {
        mutableIndexedSet.remove(t);
    }

    public static final <T> void plusAssign(com.android.server.permission.access.immutable.MutableIndexedSet<T> mutableIndexedSet, T t) {
        mutableIndexedSet.add(t);
    }

    public static final <T> void plusAssign(com.android.server.permission.access.immutable.MutableIndexedSet<T> mutableIndexedSet, java.util.Collection<? extends T> collection) {
        java.util.Collection<? extends T> $this$forEach$iv = collection;
        java.util.Iterator<T> it = $this$forEach$iv.iterator();
        while (it.hasNext()) {
            mutableIndexedSet.add(it.next());
        }
    }
}
