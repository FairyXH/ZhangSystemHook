package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IndexedListSetExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\u001a3\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\tH\u0086\b\u001a3\u0010\n\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\tH\u0086\b\u001a3\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\r\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\f0\tH\u0086\b\u001a3\u0010\u000e\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\r\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\f0\tH\u0086\b\u001a,\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0011\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0012\u001a&\u0010\u0013\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00102\u0006\u0010\u0011\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010\u0014\u001a3\u0010\u0015\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00070\tH\u0086\b\u001a,\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0011\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0012\u001a&\u0010\u0017\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00102\u0006\u0010\u0011\u001a\u0002H\u0002H\u0086\n¢\u0006\u0002\u0010\u0014\u001aA\u0010\u0018\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u00012\u001e\u0010\u001a\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u001bH\u0086\b\"\"\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u001c"}, d2 = {"lastIndex", "", "T", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IndexedListSet;)I", "allIndexed", "", "predicate", "Lkotlin/Function2;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "minus", "Lcom/android/server/permission/access/immutable/MutableIndexedListSet;", "element", "(Lcom/android/server/permission/access/immutable/IndexedListSet;Ljava/lang/Object;)Lcom/android/server/permission/access/immutable/MutableIndexedListSet;", "minusAssign", "(Lcom/android/server/permission/access/immutable/MutableIndexedListSet;Ljava/lang/Object;)V", "noneIndexed", "plus", "plusAssign", "reduceIndexed", "initialValue", "accumulator", "Lkotlin/Function3;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IndexedListSetExtensionsKt {
    public static final <T> boolean allIndexed(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedListSet.elementAt(index$iv);
            int index = index$iv;
            if (!function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean anyIndexed(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedListSet.elementAt(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <T> void forEachIndexed(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        int size = indexedListSet.getSize();
        for (int index = 0; index < size; index++) {
            function2.invoke(java.lang.Integer.valueOf(index), indexedListSet.elementAt(index));
        }
    }

    public static final <T> void forEachReversedIndexed(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        for (int index = indexedListSet.getSize() - 1; -1 < index; index--) {
            function2.invoke(java.lang.Integer.valueOf(index), indexedListSet.elementAt(index));
        }
    }

    public static final <T> int getLastIndex(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet) {
        return indexedListSet.getSize() - 1;
    }

    public static final <T> com.android.server.permission.access.immutable.MutableIndexedListSet<T> minus(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, T t) {
        com.android.server.permission.access.immutable.MutableIndexedListSet<T> mutable = indexedListSet.toMutable();
        mutable.remove(t);
        return mutable;
    }

    public static final <T> boolean noneIndexed(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedListSet.elementAt(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> com.android.server.permission.access.immutable.MutableIndexedListSet<T> plus(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, T t) {
        com.android.server.permission.access.immutable.MutableIndexedListSet<T> mutable = indexedListSet.toMutable();
        mutable.add(t);
        return mutable;
    }

    public static final <T> int reduceIndexed(com.android.server.permission.access.immutable.IndexedListSet<T> indexedListSet, int initialValue, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Integer> function3) {
        int value = initialValue;
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = indexedListSet.elementAt(index$iv);
            int index = index$iv;
            value = function3.invoke(java.lang.Integer.valueOf(value), java.lang.Integer.valueOf(index), element).intValue();
        }
        return value;
    }

    public static final <T> void minusAssign(com.android.server.permission.access.immutable.MutableIndexedListSet<T> mutableIndexedListSet, T t) {
        mutableIndexedListSet.remove(t);
    }

    public static final <T> void plusAssign(com.android.server.permission.access.immutable.MutableIndexedListSet<T> mutableIndexedListSet, T t) {
        mutableIndexedListSet.add(t);
    }
}
