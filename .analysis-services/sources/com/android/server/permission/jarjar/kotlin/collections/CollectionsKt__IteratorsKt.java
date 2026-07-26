package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Iterators.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a0\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0086\bø\u0001\u0000\u001a\u001f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0087\n\u001a\"\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b0\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\t"}, d2 = {"forEach", "", "T", "", "operation", "Lkotlin/Function1;", "iterator", "withIndex", "Lkotlin/collections/IndexedValue;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/CollectionsKt")
class CollectionsKt__IteratorsKt extends com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__IteratorsJVMKt {
    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> java.util.Iterator<T> iterator(java.util.Iterator<? extends T> it) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "<this>");
        return it;
    }

    public static final <T> java.util.Iterator<com.android.server.permission.jarjar.kotlin.collections.IndexedValue<T>> withIndex(java.util.Iterator<? extends T> it) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "<this>");
        return new com.android.server.permission.jarjar.kotlin.collections.IndexingIterator(it);
    }

    public static final <T> void forEach(java.util.Iterator<? extends T> it, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "operation");
        while (it.hasNext()) {
            java.lang.Object element = it.next();
            function1.invoke(element);
        }
    }
}
