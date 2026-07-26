package com.android.server.permission.access.collection;

/* JADX INFO: compiled from: ListExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0002\u001a3\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0086\b\u001a3\u0010\u0007\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0086\b\u001a3\u0010\b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u0005H\u0086\b\u001a3\u0010\u000b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\t0\u0005H\u0086\b\u001a3\u0010\f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0086\b\u001a3\u0010\r\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000e2\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0086\b\u001a3\u0010\u000f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000e2\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0086\b¨\u0006\u0010"}, d2 = {"allIndexed", "", "T", "", "predicate", "Lkotlin/Function2;", "", "anyIndexed", "forEachIndexed", "", "action", "forEachReversedIndexed", "noneIndexed", "removeAllIndexed", "", "retainAllIndexed", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ListExtensionsKt {
    public static final <T> boolean allIndexed(java.util.List<? extends T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = list.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = list.get(index$iv);
            int index = index$iv;
            if (!function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean anyIndexed(java.util.List<? extends T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = list.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = list.get(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public static final <T> void forEachIndexed(java.util.List<? extends T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            function2.invoke(java.lang.Integer.valueOf(i), list.get(i));
        }
    }

    public static final <T> void forEachReversedIndexed(java.util.List<? extends T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        for (int lastIndex = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list); -1 < lastIndex; lastIndex--) {
            function2.invoke(java.lang.Integer.valueOf(lastIndex), list.get(lastIndex));
        }
    }

    public static final <T> boolean noneIndexed(java.util.List<? extends T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        int size = list.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.Object element = list.get(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <T> boolean removeAllIndexed(java.util.List<T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        boolean isChanged = false;
        for (int index$iv = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list); -1 < index$iv; index$iv--) {
            java.lang.Object element = list.get(index$iv);
            int index = index$iv;
            if (function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                list.remove(index);
                isChanged = true;
            }
        }
        return isChanged;
    }

    public static final <T> boolean retainAllIndexed(java.util.List<T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, java.lang.Boolean> function2) {
        boolean isChanged = false;
        for (int index$iv = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list); -1 < index$iv; index$iv--) {
            java.lang.Object element = list.get(index$iv);
            int index = index$iv;
            if (!function2.invoke(java.lang.Integer.valueOf(index), element).booleanValue()) {
                list.remove(index);
                isChanged = true;
            }
        }
        return isChanged;
    }
}
