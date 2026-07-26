package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntSetExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\u001a\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u001a'\u0010\t\u001a\u00020\n*\u00020\u00022\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\n0\fH\u0086\b\u001a'\u0010\r\u001a\u00020\n*\u00020\u00022\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\n0\fH\u0086\b\u001a'\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0018\u0010\u0010\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000f0\fH\u0086\b\u001a'\u0010\u0011\u001a\u00020\u000f*\u00020\u00022\u0018\u0010\u0010\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000f0\fH\u0086\b\u001a\u0015\u0010\u0012\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u0014\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0001H\u0086\u0002\u001a'\u0010\u0015\u001a\u00020\n*\u00020\u00022\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\n0\fH\u0086\b\u001a\u0015\u0010\u0016\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\u000f*\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0002H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\u000f*\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0001H\u0086\u0002\u001a\u0015\u0010\u0017\u001a\u00020\u000f*\u00020\u00062\u0006\u0010\u0019\u001a\u00020\bH\u0086\u0002\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006\u001a"}, d2 = {"lastIndex", "", "Lcom/android/server/permission/access/immutable/IntSet;", "getLastIndex", "(Lcom/android/server/permission/access/immutable/IntSet;)I", "MutableIntSet", "Lcom/android/server/permission/access/immutable/MutableIntSet;", "values", "", "allIndexed", "", "predicate", "Lkotlin/Function2;", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "minus", "element", "minusAssign", "noneIndexed", "plus", "plusAssign", "set", "array", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IntSetExtensionsKt {
    public static final boolean allIndexed(com.android.server.permission.access.immutable.IntSet $this$allIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, java.lang.Boolean> function2) {
        int size = $this$allIndexed.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int element = $this$allIndexed.elementAt(index$iv);
            int index = index$iv;
            if (!function2.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(element)).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final boolean anyIndexed(com.android.server.permission.access.immutable.IntSet $this$anyIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, java.lang.Boolean> function2) {
        int size = $this$anyIndexed.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int element = $this$anyIndexed.elementAt(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(element)).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final void forEachIndexed(com.android.server.permission.access.immutable.IntSet $this$forEachIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        int size = $this$forEachIndexed.getSize();
        for (int index = 0; index < size; index++) {
            function2.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf($this$forEachIndexed.elementAt(index)));
        }
    }

    public static final void forEachReversedIndexed(com.android.server.permission.access.immutable.IntSet $this$forEachReversedIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        for (int index = $this$forEachReversedIndexed.getSize() - 1; -1 < index; index--) {
            function2.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf($this$forEachReversedIndexed.elementAt(index)));
        }
    }

    public static final int getLastIndex(com.android.server.permission.access.immutable.IntSet $this$lastIndex) {
        return $this$lastIndex.getSize() - 1;
    }

    public static final com.android.server.permission.access.immutable.MutableIntSet minus(com.android.server.permission.access.immutable.IntSet $this$minus, int element) {
        com.android.server.permission.access.immutable.MutableIntSet $this$minus_u24lambda_u242 = $this$minus.toMutable();
        minusAssign($this$minus_u24lambda_u242, element);
        return $this$minus_u24lambda_u242;
    }

    public static final void minusAssign(com.android.server.permission.access.immutable.IntSet $this$minusAssign, int element) {
        $this$minusAssign.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().delete(element);
    }

    public static final boolean noneIndexed(com.android.server.permission.access.immutable.IntSet $this$noneIndexed, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, java.lang.Boolean> function2) {
        int size = $this$noneIndexed.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int element = $this$noneIndexed.elementAt(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(element)).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final com.android.server.permission.access.immutable.MutableIntSet plus(com.android.server.permission.access.immutable.IntSet $this$plus, int element) {
        com.android.server.permission.access.immutable.MutableIntSet $this$plus_u24lambda_u244 = $this$plus.toMutable();
        plusAssign($this$plus_u24lambda_u244, element);
        return $this$plus_u24lambda_u244;
    }

    public static final com.android.server.permission.access.immutable.MutableIntSet MutableIntSet(int[] values) {
        com.android.server.permission.access.immutable.MutableIntSet $this$MutableIntSet_u24lambda_u245 = new com.android.server.permission.access.immutable.MutableIntSet(null, 1, null);
        plusAssign($this$MutableIntSet_u24lambda_u245, values);
        return $this$MutableIntSet_u24lambda_u245;
    }

    public static final void plusAssign(com.android.server.permission.access.immutable.MutableIntSet $this$plusAssign, int element) {
        $this$plusAssign.getArray$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().put(element, true);
    }

    public static final void plusAssign(com.android.server.permission.access.immutable.MutableIntSet $this$plusAssign, com.android.server.permission.access.immutable.IntSet set) {
        int size = set.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int it = set.elementAt(index$iv);
            plusAssign($this$plusAssign, it);
        }
    }

    public static final void plusAssign(com.android.server.permission.access.immutable.MutableIntSet $this$plusAssign, int[] array) {
        for (int element$iv : array) {
            plusAssign($this$plusAssign, element$iv);
        }
    }
}
