package kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _CollectionsJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000d\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001aA\u0010\u0006\u001a\u0002H\u0007\"\u0010\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\t\u001a\u0002H\u00072\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\n\u001a)\u0010\u000b\u001a\u0004\u0018\u0001H\f\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0003H\u0007¢\u0006\u0002\u0010\u000e\u001a\u0019\u0010\u000b\u001a\u0004\u0018\u00010\u000f*\b\u0012\u0004\u0012\u00020\u000f0\u0003H\u0007¢\u0006\u0002\u0010\u0010\u001a\u0019\u0010\u000b\u001a\u0004\u0018\u00010\u0011*\b\u0012\u0004\u0012\u00020\u00110\u0003H\u0007¢\u0006\u0002\u0010\u0012\u001aG\u0010\u0013\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u0002H\u00020\u0015H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u001a\u0010\u0018\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0019j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u001aH\u0007¢\u0006\u0002\u0010\u001b\u001a)\u0010\u001c\u001a\u0004\u0018\u0001H\f\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0003H\u0007¢\u0006\u0002\u0010\u000e\u001a\u0019\u0010\u001c\u001a\u0004\u0018\u00010\u000f*\b\u0012\u0004\u0012\u00020\u000f0\u0003H\u0007¢\u0006\u0002\u0010\u0010\u001a\u0019\u0010\u001c\u001a\u0004\u0018\u00010\u0011*\b\u0012\u0004\u0012\u00020\u00110\u0003H\u0007¢\u0006\u0002\u0010\u0012\u001aG\u0010\u001d\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u0002H\u00020\u0015H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a;\u0010\u001e\u001a\u0004\u0018\u0001H\f\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u001a\u0010\u0018\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0019j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u001aH\u0007¢\u0006\u0002\u0010\u001b\u001a\u0016\u0010\u001f\u001a\u00020 \"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0!\u001a5\u0010\"\u001a\u00020#\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020#0\u0015H\u0087\bø\u0001\u0000¢\u0006\u0002\b$\u001a5\u0010\"\u001a\u00020%\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020%0\u0015H\u0087\bø\u0001\u0000¢\u0006\u0002\b&\u001a&\u0010'\u001a\b\u0012\u0004\u0012\u0002H\f0(\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\r*\b\u0012\u0004\u0012\u0002H\f0\u0003\u001a8\u0010'\u001a\b\u0012\u0004\u0012\u0002H\f0(\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00032\u001a\u0010\u0018\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0019j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u001a\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006)"}, d2 = {"filterIsInstance", "", "R", "", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Ljava/lang/Iterable;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "max", "T", "", "(Ljava/lang/Iterable;)Ljava/lang/Comparable;", "", "(Ljava/lang/Iterable;)Ljava/lang/Double;", "", "(Ljava/lang/Iterable;)Ljava/lang/Float;", "maxBy", "selector", "Lkotlin/Function1;", "(Ljava/lang/Iterable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/Iterable;Ljava/util/Comparator;)Ljava/lang/Object;", "min", "minBy", "minWith", "reverse", "", "", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/CollectionsKt")
public class CollectionsKt___CollectionsJvmKt extends kotlin.collections.CollectionsKt__ReversedViewsKt {
    public static final <R> java.util.List<R> filterIsInstance(java.lang.Iterable<?> iterable, java.lang.Class<R> klass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(klass, "klass");
        return (java.util.List) kotlin.collections.CollectionsKt.filterIsInstanceTo(iterable, new java.util.ArrayList(), klass);
    }

    public static final <C extends java.util.Collection<? super R>, R> C filterIsInstanceTo(java.lang.Iterable<?> iterable, C destination, java.lang.Class<R> klass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(klass, "klass");
        for (java.lang.Object element : iterable) {
            if (klass.isInstance(element)) {
                destination.add(element);
            }
        }
        return destination;
    }

    public static final <T> void reverse(java.util.List<T> list) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "<this>");
        java.util.Collections.reverse(list);
    }

    public static final <T extends java.lang.Comparable<? super T>> java.util.SortedSet<T> toSortedSet(java.lang.Iterable<? extends T> iterable) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        return (java.util.SortedSet) kotlin.collections.CollectionsKt.toCollection(iterable, new java.util.TreeSet());
    }

    public static final <T> java.util.SortedSet<T> toSortedSet(java.lang.Iterable<? extends T> iterable, java.util.Comparator<? super T> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.SortedSet) kotlin.collections.CollectionsKt.toCollection(iterable, new java.util.TreeSet(comparator));
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Double m11734max(java.lang.Iterable $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.collections.CollectionsKt.maxOrNull($this$max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Float m11735max(java.lang.Iterable $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.collections.CollectionsKt.maxOrNull($this$max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Comparable max(java.lang.Iterable $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.collections.CollectionsKt.maxOrNull($this$max);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [T] */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <T, R extends java.lang.Comparable<? super R>> T maxBy(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        if (!it.hasNext()) {
            return next;
        }
        R rInvoke = selector.invoke(next);
        do {
            java.lang.Object next2 = it.next();
            R rInvoke2 = selector.invoke(next2);
            next = next;
            if (rInvoke.compareTo(rInvoke2) < 0) {
                rInvoke = rInvoke2;
                next = (T) next2;
            }
        } while (it.hasNext());
        return (T) next;
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Object maxWith(java.lang.Iterable $this$maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.CollectionsKt.maxWithOrNull($this$maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Double m11736min(java.lang.Iterable $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.collections.CollectionsKt.minOrNull($this$min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Float m11737min(java.lang.Iterable $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.collections.CollectionsKt.minOrNull($this$min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Comparable min(java.lang.Iterable $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.collections.CollectionsKt.minOrNull($this$min);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [T] */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <T, R extends java.lang.Comparable<? super R>> T minBy(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        if (!it.hasNext()) {
            return next;
        }
        R rInvoke = selector.invoke(next);
        do {
            java.lang.Object next2 = it.next();
            R rInvoke2 = selector.invoke(next2);
            next = next;
            if (rInvoke.compareTo(rInvoke2) > 0) {
                rInvoke = rInvoke2;
                next = (T) next2;
            }
        } while (it.hasNext());
        return (T) next;
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Object minWith(java.lang.Iterable $this$minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.collections.CollectionsKt.minWithOrNull($this$minWith, comparator);
    }

    private static final <T> java.math.BigDecimal sumOfBigDecimal(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        for (java.lang.Object element : iterable) {
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(element));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final <T> java.math.BigInteger sumOfBigInteger(java.lang.Iterable<? extends T> iterable, kotlin.jvm.functions.Function1<? super T, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        for (java.lang.Object element : iterable) {
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(element));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }
}
