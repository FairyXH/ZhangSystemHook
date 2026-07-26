package com.android.server.permission.jarjar.kotlin.comparisons;

/* JADX INFO: compiled from: Comparisons.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u0012\u0012\u0004\u0012\u0002H\u00010\u0002j\b\u0012\u0004\u0012\u0002H\u0001`\u0003B\u001d\u0012\u0016\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0002j\b\u0012\u0004\u0012\u00028\u0000`\u0003¢\u0006\u0002\u0010\u0005J\u001d\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00028\u00002\u0006\u0010\u000b\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0002j\b\u0012\u0004\u0012\u00028\u0000`\u0003R!\u0010\u0004\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0002j\b\u0012\u0004\u0012\u00028\u0000`\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000e"}, d2 = {"Lkotlin/comparisons/ReversedComparator;", "T", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "comparator", "(Ljava/util/Comparator;)V", "getComparator", "()Ljava/util/Comparator;", "compare", "", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "(Ljava/lang/Object;Ljava/lang/Object;)I", "reversed", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ReversedComparator<T> implements java.util.Comparator<T> {
    private final java.util.Comparator<T> comparator;

    public ReversedComparator(java.util.Comparator<T> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        this.comparator = comparator;
    }

    public final java.util.Comparator<T> getComparator() {
        return this.comparator;
    }

    @Override // java.util.Comparator
    public int compare(T t, T t2) {
        return this.comparator.compare(t2, t);
    }

    @Override // java.util.Comparator
    public final java.util.Comparator<T> reversed() {
        return this.comparator;
    }
}
