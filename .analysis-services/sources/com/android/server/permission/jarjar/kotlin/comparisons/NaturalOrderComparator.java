package com.android.server.permission.jarjar.kotlin.comparisons;

/* JADX INFO: compiled from: Comparisons.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\bÂ\u0002\u0018\u00002\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u0002`\u0004B\u0007\b\u0002¢\u0006\u0002\u0010\u0005J$\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u00022\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0016J\"\u0010\n\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u0002`\u0004¨\u0006\u000b"}, d2 = {"Lkotlin/comparisons/NaturalOrderComparator;", "Ljava/util/Comparator;", "", "", "Lkotlin/Comparator;", "()V", "compare", "", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "reversed", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class NaturalOrderComparator implements java.util.Comparator<java.lang.Comparable<? super java.lang.Object>> {
    public static final com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator INSTANCE = new com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator();

    private NaturalOrderComparator() {
    }

    @Override // java.util.Comparator
    public /* bridge */ /* synthetic */ int compare(java.lang.Comparable<? super java.lang.Object> comparable, java.lang.Comparable<? super java.lang.Object> comparable2) {
        return compare2((java.lang.Comparable<java.lang.Object>) comparable, (java.lang.Comparable<java.lang.Object>) comparable2);
    }

    /* JADX INFO: renamed from: compare, reason: avoid collision after fix types in other method */
    public int compare2(java.lang.Comparable<java.lang.Object> comparable, java.lang.Comparable<java.lang.Object> comparable2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparable, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparable2, "b");
        return comparable.compareTo(comparable2);
    }

    @Override // java.util.Comparator
    public final java.util.Comparator<java.lang.Comparable<? super java.lang.Object>> reversed() {
        return com.android.server.permission.jarjar.kotlin.comparisons.ReverseOrderComparator.INSTANCE;
    }
}
