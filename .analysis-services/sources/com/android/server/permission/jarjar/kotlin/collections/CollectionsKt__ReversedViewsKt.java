package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: ReversedViews.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007¢\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\n\u001a\u001d\u0010\u000b\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\f¨\u0006\r"}, d2 = {"asReversed", "", "T", "", "asReversedMutable", "reverseElementIndex", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reverseIteratorIndex", "reverseIteratorIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt extends com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__MutableCollectionsKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(java.util.List<?> list, int index) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list)).contains(index)) {
            return com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list) - index;
        }
        throw new java.lang.IndexOutOfBoundsException("Element index " + index + " must be in range [" + new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list)) + "].");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(java.util.List<?> list, int index) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, list.size()).contains(index)) {
            return list.size() - index;
        }
        throw new java.lang.IndexOutOfBoundsException("Position index " + index + " must be in range [" + new com.android.server.permission.jarjar.kotlin.ranges.IntRange(0, list.size()) + "].");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int reverseIteratorIndex$CollectionsKt__ReversedViewsKt(java.util.List<?> list, int index) {
        return com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(list) - index;
    }

    public static final <T> java.util.List<T> asReversed(java.util.List<? extends T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        return new com.android.server.permission.jarjar.kotlin.collections.ReversedListReadOnly(list);
    }

    public static final <T> java.util.List<T> asReversedMutable(java.util.List<T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        return new com.android.server.permission.jarjar.kotlin.collections.ReversedList(list);
    }
}
