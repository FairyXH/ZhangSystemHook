package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: MutableCollectionsJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0005\u001a\u0019\u0010\u0006\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0087\b\u001a \u0010\t\u001a\u00020\u0001\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a6\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\r0\fH\u0087\bø\u0001\u0000\u001a5\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000fj\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0010H\u0087\b\u001a2\u0010\u0011\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000fj\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0010\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0012"}, d2 = {"fill", "", "T", "", "value", "(Ljava/util/List;Ljava/lang/Object;)V", "shuffle", "random", "Ljava/util/Random;", "sort", "", "comparison", "Lkotlin/Function2;", "", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "sortWith", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/CollectionsKt")
public class CollectionsKt__MutableCollectionsJVMKt extends com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__IteratorsKt {
    @com.android.server.permission.jarjar.kotlin.Deprecated(level = com.android.server.permission.jarjar.kotlin.DeprecationLevel.ERROR, message = "Use sortWith(comparator) instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.sortWith(comparator)", imports = {}))
    private static final <T> void sort(java.util.List<T> list, java.util.Comparator<? super T> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        throw new com.android.server.permission.jarjar.kotlin.NotImplementedError(null, 1, null);
    }

    @com.android.server.permission.jarjar.kotlin.Deprecated(level = com.android.server.permission.jarjar.kotlin.DeprecationLevel.ERROR, message = "Use sortWith(Comparator(comparison)) instead.", replaceWith = @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "this.sortWith(Comparator(comparison))", imports = {}))
    private static final <T> void sort(java.util.List<T> list, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super T, ? super T, java.lang.Integer> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "comparison");
        throw new com.android.server.permission.jarjar.kotlin.NotImplementedError(null, 1, null);
    }

    public static final <T extends java.lang.Comparable<? super T>> void sort(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        if (list.size() > 1) {
            java.util.Collections.sort(list);
        }
    }

    public static final <T> void sortWith(java.util.List<T> list, java.util.Comparator<? super T> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (list.size() > 1) {
            java.util.Collections.sort(list, comparator);
        }
    }

    private static final <T> void fill(java.util.List<T> list, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        java.util.Collections.fill(list, t);
    }

    private static final <T> void shuffle(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        java.util.Collections.shuffle(list);
    }

    private static final <T> void shuffle(java.util.List<T> list, java.util.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        java.util.Collections.shuffle(list, random);
    }
}
