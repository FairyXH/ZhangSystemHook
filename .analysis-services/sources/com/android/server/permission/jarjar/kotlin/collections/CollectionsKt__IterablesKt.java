package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Iterables.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a.\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\bø\u0001\u0000\u001a \u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\b\u001a\u00020\u0007H\u0001\u001a\u001f\u0010\t\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0001¢\u0006\u0002\u0010\n\u001a\"\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\f\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a@\u0010\r\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000f0\f0\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u000f*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u000f0\u000e0\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0010"}, d2 = {"Iterable", "", "T", "iterator", "Lkotlin/Function0;", "", "collectionSizeOrDefault", "", "default", "collectionSizeOrNull", "(Ljava/lang/Iterable;)Ljava/lang/Integer;", "flatten", "", "unzip", "Lkotlin/Pair;", "R", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/CollectionsKt")
public class CollectionsKt__IterablesKt extends com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__CollectionsKt {

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__IterablesKt$Iterable$1, reason: invalid class name */
    /* JADX INFO: compiled from: Iterables.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004"}, d2 = {"com/android/server/permission/jarjar/kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass1<T> implements java.lang.Iterable<T>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<java.util.Iterator<T>> $iterator;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends java.util.Iterator<? extends T>> function0) {
            this.$iterator = function0;
        }

        @Override // java.lang.Iterable
        public java.util.Iterator<T> iterator() {
            return this.$iterator.invoke();
        }
    }

    private static final <T> java.lang.Iterable<T> Iterable(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends java.util.Iterator<? extends T>> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "iterator");
        return new com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__IterablesKt.AnonymousClass1(function0);
    }

    public static final <T> java.lang.Integer collectionSizeOrNull(java.lang.Iterable<? extends T> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        if (iterable instanceof java.util.Collection) {
            return java.lang.Integer.valueOf(((java.util.Collection) iterable).size());
        }
        return null;
    }

    public static final <T> int collectionSizeOrDefault(java.lang.Iterable<? extends T> iterable, int i) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        return iterable instanceof java.util.Collection ? ((java.util.Collection) iterable).size() : i;
    }

    public static final <T> java.util.List<T> flatten(java.lang.Iterable<? extends java.lang.Iterable<? extends T>> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        java.util.ArrayList result = new java.util.ArrayList();
        java.util.Iterator<? extends java.lang.Iterable<? extends T>> it = iterable.iterator();
        while (it.hasNext()) {
            com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(result, it.next());
        }
        return result;
    }

    public static final <T, R> com.android.server.permission.jarjar.kotlin.Pair<java.util.List<T>, java.util.List<R>> unzip(java.lang.Iterable<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends T, ? extends R>> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        int expectedSize = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.collectionSizeOrDefault(iterable, 10);
        java.util.ArrayList listT = new java.util.ArrayList(expectedSize);
        java.util.ArrayList listR = new java.util.ArrayList(expectedSize);
        for (com.android.server.permission.jarjar.kotlin.Pair<? extends T, ? extends R> pair : iterable) {
            listT.add(pair.getFirst());
            listR.add(pair.getSecond());
        }
        return com.android.server.permission.jarjar.kotlin.TuplesKt.to(listT, listR);
    }
}
