package com.android.server.permission.jarjar.kotlin.comparisons;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Comparisons.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a>\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001aY\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\t\u001aZ\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001a>\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001aZ\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001a-\u0010\r\u001a\u00020\u000e\"\f\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00062\b\u0010\u000f\u001a\u0004\u0018\u0001H\u00022\b\u0010\u0010\u001a\u0004\u0018\u0001H\u0002¢\u0006\u0002\u0010\u0011\u001aA\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aY\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\u0014\u001a]\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001aG\u0010\u0016\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022 \u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\bH\u0002¢\u0006\u0004\b\u0017\u0010\u0014\u001a&\u0010\u0018\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a-\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a-\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a&\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a0\u0010\u001d\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\u001aO\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u001aR\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001an\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001aR\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001an\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001ap\u0010!\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u000328\b\u0004\u0010\"\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u000e0#H\u0087\bø\u0001\u0000\u001aO\u0010&\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006'"}, d2 = {"compareBy", "Ljava/util/Comparator;", "T", "Lkotlin/Comparator;", "selector", "Lkotlin/Function1;", "", "selectors", "", "([Lkotlin/jvm/functions/Function1;)Ljava/util/Comparator;", "K", "comparator", "compareByDescending", "compareValues", "", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)I", "compareValuesBy", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;[Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)I", "compareValuesByImpl", "compareValuesByImpl$ComparisonsKt__ComparisonsKt", "naturalOrder", "nullsFirst", "", "nullsLast", "reverseOrder", "reversed", "then", "thenBy", "thenByDescending", "thenComparator", "comparison", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "thenDescending", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/comparisons/ComparisonsKt")
public class ComparisonsKt__ComparisonsKt {
    public static final <T> int compareValuesBy(T t, T t2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>>... function1Arr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1Arr, "selectors");
        if (!(function1Arr.length > 0)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        return compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, t2, function1Arr);
    }

    private static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T t, T t2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>>[] function1Arr) {
        for (com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1 : function1Arr) {
            int diff = com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.compareValues(function1.invoke(t), function1.invoke(t2));
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    private static final <T> int compareValuesBy(T t, T t2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.compareValues(function1.invoke(t), function1.invoke(t2));
    }

    private static final <T, K> int compareValuesBy(T t, T t2, java.util.Comparator<? super K> comparator, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends K> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return comparator.compare(function1.invoke(t), function1.invoke(t2));
    }

    public static final <T extends java.lang.Comparable<?>> int compareValues(T t, T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t.compareTo(t2);
    }

    public static final <T> java.util.Comparator<T> compareBy(final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>>... function1Arr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1Arr, "selectors");
        if (!(function1Arr.length > 0)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.compareBy$lambda$0$ComparisonsKt__ComparisonsKt(function1Arr, obj, obj2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int compareBy$lambda$0$ComparisonsKt__ComparisonsKt(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1[] $selectors, java.lang.Object a, java.lang.Object b) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($selectors, "$selectors");
        return compareValuesByImpl$ComparisonsKt__ComparisonsKt(a, b, $selectors);
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$2, reason: invalid class name */
    /* JADX INFO: compiled from: Comparisons.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", "T", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, k = 3, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass2<T> implements java.util.Comparator {
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Comparable<?>> $selector;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass2(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
            this.$selector = function1;
        }

        @Override // java.util.Comparator
        public final int compare(T t, T t2) {
            com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Comparable<?>> function1 = this.$selector;
            return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.compareValues(function1.invoke(t), function1.invoke(t2));
        }
    }

    private static final <T> java.util.Comparator<T> compareBy(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.AnonymousClass2(function1);
    }

    private static final <T, K> java.util.Comparator<T> compareBy(final java.util.Comparator<? super K> comparator, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends K> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.compareBy.3
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                java.util.Comparator<? super K> comparator2 = comparator;
                com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, K> function12 = function1;
                return comparator2.compare((java.lang.Object) function12.invoke(t), (java.lang.Object) function12.invoke(t2));
            }
        };
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareByDescending$1, reason: invalid class name */
    /* JADX INFO: compiled from: Comparisons.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", "T", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, k = 3, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass1<T> implements java.util.Comparator {
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Comparable<?>> $selector;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
            this.$selector = function1;
        }

        @Override // java.util.Comparator
        public final int compare(T t, T t2) {
            com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Comparable<?>> function1 = this.$selector;
            return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.compareValues(function1.invoke(t2), function1.invoke(t));
        }
    }

    private static final <T> java.util.Comparator<T> compareByDescending(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.AnonymousClass1(function1);
    }

    private static final <T, K> java.util.Comparator<T> compareByDescending(final java.util.Comparator<? super K> comparator, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends K> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.compareByDescending.2
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                java.util.Comparator<? super K> comparator2 = comparator;
                com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, K> function12 = function1;
                return comparator2.compare((java.lang.Object) function12.invoke(t2), (java.lang.Object) function12.invoke(t));
            }
        };
    }

    private static final <T> java.util.Comparator<T> thenBy(final java.util.Comparator<T> comparator, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.thenBy.1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = comparator.compare(t, t2);
                if (previousCompare != 0) {
                    return previousCompare;
                }
                com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Comparable<?>> function12 = function1;
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.compareValues(function12.invoke(t), function12.invoke(t2));
            }
        };
    }

    private static final <T, K> java.util.Comparator<T> thenBy(final java.util.Comparator<T> comparator, final java.util.Comparator<? super K> comparator2, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends K> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator2, "comparator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.thenBy.2
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int iCompare = comparator.compare(t, t2);
                if (iCompare != 0) {
                    return iCompare;
                }
                java.util.Comparator<? super K> comparator3 = comparator2;
                com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, K> function12 = function1;
                return comparator3.compare((java.lang.Object) function12.invoke(t), (java.lang.Object) function12.invoke(t2));
            }
        };
    }

    private static final <T> java.util.Comparator<T> thenByDescending(final java.util.Comparator<T> comparator, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Comparable<?>> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.thenByDescending.1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = comparator.compare(t, t2);
                if (previousCompare != 0) {
                    return previousCompare;
                }
                com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, java.lang.Comparable<?>> function12 = function1;
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.compareValues(function12.invoke(t2), function12.invoke(t));
            }
        };
    }

    private static final <T, K> java.util.Comparator<T> thenByDescending(final java.util.Comparator<T> comparator, final java.util.Comparator<? super K> comparator2, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends K> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator2, "comparator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "selector");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.thenByDescending.2
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int iCompare = comparator.compare(t, t2);
                if (iCompare != 0) {
                    return iCompare;
                }
                java.util.Comparator<? super K> comparator3 = comparator2;
                com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<T, K> function12 = function1;
                return comparator3.compare((java.lang.Object) function12.invoke(t2), (java.lang.Object) function12.invoke(t));
            }
        };
    }

    private static final <T> java.util.Comparator<T> thenComparator(final java.util.Comparator<T> comparator, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super T, ? super T, java.lang.Integer> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "comparison");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.thenComparator.1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = comparator.compare(t, t2);
                return previousCompare != 0 ? previousCompare : function2.invoke(t, t2).intValue();
            }
        };
    }

    public static final <T> java.util.Comparator<T> then(final java.util.Comparator<T> comparator, final java.util.Comparator<? super T> comparator2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator2, "comparator");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.then$lambda$1$ComparisonsKt__ComparisonsKt(comparator, comparator2, obj, obj2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int then$lambda$1$ComparisonsKt__ComparisonsKt(java.util.Comparator $this_then, java.util.Comparator $comparator, java.lang.Object a, java.lang.Object b) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this_then, "$this_then");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($comparator, "$comparator");
        int previousCompare = $this_then.compare(a, b);
        return previousCompare != 0 ? previousCompare : $comparator.compare(a, b);
    }

    public static final <T> java.util.Comparator<T> thenDescending(final java.util.Comparator<T> comparator, final java.util.Comparator<? super T> comparator2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator2, "comparator");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda4
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.thenDescending$lambda$2$ComparisonsKt__ComparisonsKt(comparator, comparator2, obj, obj2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int thenDescending$lambda$2$ComparisonsKt__ComparisonsKt(java.util.Comparator $this_thenDescending, java.util.Comparator $comparator, java.lang.Object a, java.lang.Object b) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this_thenDescending, "$this_thenDescending");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($comparator, "$comparator");
        int previousCompare = $this_thenDescending.compare(a, b);
        return previousCompare != 0 ? previousCompare : $comparator.compare(b, a);
    }

    public static final <T> java.util.Comparator<T> nullsFirst(final java.util.Comparator<? super T> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda3
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.nullsFirst$lambda$3$ComparisonsKt__ComparisonsKt(comparator, obj, obj2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int nullsFirst$lambda$3$ComparisonsKt__ComparisonsKt(java.util.Comparator $comparator, java.lang.Object a, java.lang.Object b) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($comparator, "$comparator");
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return $comparator.compare(a, b);
    }

    private static final <T extends java.lang.Comparable<? super T>> java.util.Comparator<T> nullsFirst() {
        return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.nullsFirst(com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.naturalOrder());
    }

    public static final <T> java.util.Comparator<T> nullsLast(final java.util.Comparator<? super T> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return new java.util.Comparator() { // from class: com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt.nullsLast$lambda$4$ComparisonsKt__ComparisonsKt(comparator, obj, obj2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int nullsLast$lambda$4$ComparisonsKt__ComparisonsKt(java.util.Comparator $comparator, java.lang.Object a, java.lang.Object b) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($comparator, "$comparator");
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return 1;
        }
        if (b == null) {
            return -1;
        }
        return $comparator.compare(a, b);
    }

    private static final <T extends java.lang.Comparable<? super T>> java.util.Comparator<T> nullsLast() {
        return com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.nullsLast(com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.naturalOrder());
    }

    public static final <T extends java.lang.Comparable<? super T>> java.util.Comparator<T> naturalOrder() {
        com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator naturalOrderComparator = com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator.INSTANCE;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(naturalOrderComparator, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder> }");
        return naturalOrderComparator;
    }

    public static final <T extends java.lang.Comparable<? super T>> java.util.Comparator<T> reverseOrder() {
        com.android.server.permission.jarjar.kotlin.comparisons.ReverseOrderComparator reverseOrderComparator = com.android.server.permission.jarjar.kotlin.comparisons.ReverseOrderComparator.INSTANCE;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(reverseOrderComparator, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reverseOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reverseOrder> }");
        return reverseOrderComparator;
    }

    public static final <T> java.util.Comparator<T> reversed(java.util.Comparator<T> comparator) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "<this>");
        if (comparator instanceof com.android.server.permission.jarjar.kotlin.comparisons.ReversedComparator) {
            return ((com.android.server.permission.jarjar.kotlin.comparisons.ReversedComparator) comparator).getComparator();
        }
        if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(comparator, com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator.INSTANCE)) {
            com.android.server.permission.jarjar.kotlin.comparisons.ReverseOrderComparator reverseOrderComparator = com.android.server.permission.jarjar.kotlin.comparisons.ReverseOrderComparator.INSTANCE;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(reverseOrderComparator, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed> }");
            return reverseOrderComparator;
        }
        if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(comparator, com.android.server.permission.jarjar.kotlin.comparisons.ReverseOrderComparator.INSTANCE)) {
            return new com.android.server.permission.jarjar.kotlin.comparisons.ReversedComparator(comparator);
        }
        com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator naturalOrderComparator = com.android.server.permission.jarjar.kotlin.comparisons.NaturalOrderComparator.INSTANCE;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(naturalOrderComparator, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed> }");
        return naturalOrderComparator;
    }
}
