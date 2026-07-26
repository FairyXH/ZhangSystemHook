package com.android.server.permission.access.collection;

/* JADX INFO: compiled from: SparseBooleanArrayExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u001a-\u0010\u0007\u001a\u00020\b*\u00020\u00022\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a-\u0010\u000b\u001a\u00020\b*\u00020\u00022\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a-\u0010\f\u001a\u00020\r*\u00020\u00022\u001e\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\nH\u0086\b\u001a-\u0010\u000f\u001a\u00020\r*\u00020\u00022\u001e\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r0\nH\u0086\b\u001a#\u0010\u0010\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u0013H\u0086\b\u001a\u0015\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0001H\u0086\n\u001a-\u0010\u0015\u001a\u00020\b*\u00020\u00022\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a\u0012\u0010\u0016\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0001\u001a\u001a\u0010\u0016\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\b\u001a-\u0010\u0017\u001a\u00020\b*\u00020\u00022\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a-\u0010\u0018\u001a\u00020\b*\u00020\u00022\u001e\u0010\t\u001a\u001a\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\nH\u0086\b\u001a\u001d\u0010\u0019\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\bH\u0086\n\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0016\u0010\u0005\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004¨\u0006\u001b"}, d2 = {"lastIndex", "", "Landroid/util/SparseBooleanArray;", "getLastIndex", "(Landroid/util/SparseBooleanArray;)I", "size", "getSize", "allIndexed", "", "predicate", "Lkotlin/Function3;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "getOrPut", "key", "defaultValue", "Lkotlin/Function0;", "minusAssign", "noneIndexed", "remove", "removeAllIndexed", "retainAllIndexed", "set", "value", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SparseBooleanArrayExtensionsKt {
    public static final boolean allIndexed(android.util.SparseBooleanArray $this$allIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, java.lang.Boolean> function3) {
        int size = $this$allIndexed.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = $this$allIndexed.keyAt(index$iv);
            boolean value = $this$allIndexed.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), java.lang.Boolean.valueOf(value)).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final boolean anyIndexed(android.util.SparseBooleanArray $this$anyIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, java.lang.Boolean> function3) {
        int size = $this$anyIndexed.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = $this$anyIndexed.keyAt(index$iv);
            boolean value = $this$anyIndexed.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), java.lang.Boolean.valueOf(value)).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final void forEachIndexed(android.util.SparseBooleanArray $this$forEachIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        int size = $this$forEachIndexed.size();
        for (int index = 0; index < size; index++) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf($this$forEachIndexed.keyAt(index)), java.lang.Boolean.valueOf($this$forEachIndexed.valueAt(index)));
        }
    }

    public static final void forEachReversedIndexed(android.util.SparseBooleanArray $this$forEachReversedIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        for (int index = $this$forEachReversedIndexed.size() - 1; -1 < index; index--) {
            function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf($this$forEachReversedIndexed.keyAt(index)), java.lang.Boolean.valueOf($this$forEachReversedIndexed.valueAt(index)));
        }
    }

    public static final boolean getOrPut(android.util.SparseBooleanArray $this$getOrPut, int key, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<java.lang.Boolean> function0) {
        int index = $this$getOrPut.indexOfKey(key);
        if (index >= 0) {
            return $this$getOrPut.valueAt(index);
        }
        java.lang.Boolean boolInvoke = function0.invoke();
        boolean it = boolInvoke.booleanValue();
        $this$getOrPut.put(key, it);
        return boolInvoke.booleanValue();
    }

    public static final int getLastIndex(android.util.SparseBooleanArray $this$lastIndex) {
        return $this$lastIndex.size() - 1;
    }

    public static final void minusAssign(android.util.SparseBooleanArray $this$minusAssign, int key) {
        $this$minusAssign.delete(key);
    }

    public static final boolean noneIndexed(android.util.SparseBooleanArray $this$noneIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, java.lang.Boolean> function3) {
        int size = $this$noneIndexed.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int key = $this$noneIndexed.keyAt(index$iv);
            boolean value = $this$noneIndexed.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), java.lang.Boolean.valueOf(value)).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final void remove(android.util.SparseBooleanArray $this$remove, int key) {
        $this$remove.delete(key);
    }

    public static final boolean remove(android.util.SparseBooleanArray $this$remove, int key, boolean defaultValue) {
        int index = $this$remove.indexOfKey(key);
        if (index >= 0) {
            boolean zValueAt = $this$remove.valueAt(index);
            $this$remove.removeAt(index);
            return zValueAt;
        }
        return defaultValue;
    }

    public static final boolean removeAllIndexed(android.util.SparseBooleanArray $this$removeAllIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, java.lang.Boolean> function3) {
        boolean isChanged = false;
        for (int index$iv = $this$removeAllIndexed.size() - 1; -1 < index$iv; index$iv--) {
            int key = $this$removeAllIndexed.keyAt(index$iv);
            boolean value = $this$removeAllIndexed.valueAt(index$iv);
            int index = index$iv;
            if (function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), java.lang.Boolean.valueOf(value)).booleanValue()) {
                $this$removeAllIndexed.removeAt(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final boolean retainAllIndexed(android.util.SparseBooleanArray $this$retainAllIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Boolean, java.lang.Boolean> function3) {
        boolean isChanged = false;
        for (int index$iv = $this$retainAllIndexed.size() - 1; -1 < index$iv; index$iv--) {
            int key = $this$retainAllIndexed.keyAt(index$iv);
            boolean value = $this$retainAllIndexed.valueAt(index$iv);
            int index = index$iv;
            if (!function3.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(key), java.lang.Boolean.valueOf(value)).booleanValue()) {
                $this$retainAllIndexed.removeAt(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final void set(android.util.SparseBooleanArray $this$set, int key, boolean value) {
        $this$set.put(key, value);
    }

    public static final int getSize(android.util.SparseBooleanArray $this$size) {
        return $this$size.size();
    }
}
