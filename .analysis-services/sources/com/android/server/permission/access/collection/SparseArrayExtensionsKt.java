package com.android.server.permission.access.collection;

/* JADX INFO: compiled from: SparseArrayExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a9\u0010\b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001a9\u0010\f\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001a9\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000e0\u000bH\u0086\b\u001a9\u0010\u0010\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u000e0\u000bH\u0086\b\u001a4\u0010\u0011\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0014H\u0086\b¢\u0006\u0002\u0010\u0015\u001a!\u0010\u0016\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0001H\u0086\n\u001a9\u0010\u0017\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001a9\u0010\u0018\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\u001a9\u0010\u0019\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u000bH\u0086\b\"\"\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\"\u0010\u0006\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005¨\u0006\u001a"}, d2 = {"lastIndex", "", "T", "Landroid/util/SparseArray;", "getLastIndex", "(Landroid/util/SparseArray;)I", "size", "getSize", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "getOrPut", "key", "defaultValue", "Lkotlin/Function0;", "(Landroid/util/SparseArray;ILkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "minusAssign", "noneIndexed", "removeAllIndexed", "retainAllIndexed", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SparseArrayExtensionsKt {
    public static final <T> boolean allIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        int size = sparseArray.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = sparseArray.keyAt(index$iv);
            java.lang.Object value = sparseArray.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean anyIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        int size = sparseArray.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = sparseArray.keyAt(index$iv);
            java.lang.Object value = sparseArray.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <T> void forEachIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = sparseArray.size();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(sparseArray.keyAt(index)), sparseArray.valueAt(index));
        }
    }

    public static final <T> void forEachReversedIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = sparseArray.size() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(sparseArray.keyAt(index)), sparseArray.valueAt(index));
        }
    }

    public static final <T> T getOrPut(android.util.SparseArray<T> sparseArray, int key, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        int index = sparseArray.indexOfKey(key);
        if (index >= 0) {
            return sparseArray.valueAt(index);
        }
        T tInvoke = function0.invoke();
        sparseArray.put(key, tInvoke);
        return tInvoke;
    }

    public static final <T> int getLastIndex(android.util.SparseArray<T> sparseArray) {
        return sparseArray.size() - 1;
    }

    public static final <T> void minusAssign(android.util.SparseArray<T> sparseArray, int key) {
        sparseArray.delete(key);
    }

    public static final <T> boolean noneIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        int size = sparseArray.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = sparseArray.keyAt(index$iv);
            java.lang.Object value = sparseArray.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean removeAllIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        boolean isChanged = false;
        for (int index$iv = sparseArray.size() - 1; -1 < index$iv; index$iv--) {
            int key = sparseArray.keyAt(index$iv);
            java.lang.Object value = sparseArray.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                sparseArray.removeAt(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final <T> boolean retainAllIndexed(android.util.SparseArray<T> sparseArray, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super T, java.lang.Boolean> function3) {
        boolean isChanged = false;
        for (int index$iv = sparseArray.size() - 1; -1 < index$iv; index$iv--) {
            int key = sparseArray.keyAt(index$iv);
            java.lang.Object value = sparseArray.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), value).booleanValue()) {
                sparseArray.removeAt(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final <T> int getSize(android.util.SparseArray<T> sparseArray) {
        return sparseArray.size();
    }
}
