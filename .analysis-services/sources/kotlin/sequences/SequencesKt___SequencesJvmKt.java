package kotlin.sequences;

/* JADX INFO: compiled from: _SequencesJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000V\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001aA\u0010\u0005\u001a\u0002H\u0006\"\u0010\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0007\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\b\u001a\u0002H\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¢\u0006\u0002\u0010\t\u001a)\u0010\n\u001a\u0004\u0018\u0001H\u000b\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0007¢\u0006\u0002\u0010\r\u001a\u0019\u0010\n\u001a\u0004\u0018\u00010\u000e*\b\u0012\u0004\u0012\u00020\u000e0\u0001H\u0007¢\u0006\u0002\u0010\u000f\u001a\u0019\u0010\n\u001a\u0004\u0018\u00010\u0010*\b\u0012\u0004\u0012\u00020\u00100\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001aG\u0010\u0012\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a;\u0010\u0016\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019H\u0007¢\u0006\u0002\u0010\u001a\u001a)\u0010\u001b\u001a\u0004\u0018\u0001H\u000b\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0007¢\u0006\u0002\u0010\r\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u000e*\b\u0012\u0004\u0012\u00020\u000e0\u0001H\u0007¢\u0006\u0002\u0010\u000f\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u0010*\b\u0012\u0004\u0012\u00020\u00100\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001aG\u0010\u001c\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a;\u0010\u001d\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019H\u0007¢\u0006\u0002\u0010\u001a\u001a5\u0010\u001e\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020\u001f0\u0014H\u0087\bø\u0001\u0000¢\u0006\u0002\b \u001a5\u0010\u001e\u001a\u00020!\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020!0\u0014H\u0087\bø\u0001\u0000¢\u0006\u0002\b\"\u001a&\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u000b0$\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001\u001a8\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u000b0$\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006%"}, d2 = {"filterIsInstance", "Lkotlin/sequences/Sequence;", "R", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Lkotlin/sequences/Sequence;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "max", "T", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Comparable;", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Double;", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Float;", "maxBy", "selector", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Lkotlin/sequences/Sequence;Ljava/util/Comparator;)Ljava/lang/Object;", "min", "minBy", "minWith", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/sequences/SequencesKt")
class SequencesKt___SequencesJvmKt extends kotlin.sequences.SequencesKt__SequencesKt {
    public static final <R> kotlin.sequences.Sequence<R> filterIsInstance(kotlin.sequences.Sequence<?> sequence, final java.lang.Class<R> klass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(klass, "klass");
        kotlin.sequences.Sequence<R> sequenceFilter = kotlin.sequences.SequencesKt.filter(sequence, new kotlin.jvm.functions.Function1<java.lang.Object, java.lang.Boolean>() { // from class: kotlin.sequences.SequencesKt___SequencesJvmKt.filterIsInstance.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Boolean invoke(java.lang.Object it) {
                return java.lang.Boolean.valueOf(klass.isInstance(it));
            }
        });
        kotlin.jvm.internal.Intrinsics.checkNotNull(sequenceFilter, "null cannot be cast to non-null type kotlin.sequences.Sequence<R of kotlin.sequences.SequencesKt___SequencesJvmKt.filterIsInstance>");
        return sequenceFilter;
    }

    public static final <C extends java.util.Collection<? super R>, R> C filterIsInstanceTo(kotlin.sequences.Sequence<?> sequence, C destination, java.lang.Class<R> klass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(klass, "klass");
        for (java.lang.Object element : sequence) {
            if (klass.isInstance(element)) {
                destination.add(element);
            }
        }
        return destination;
    }

    public static final <T extends java.lang.Comparable<? super T>> java.util.SortedSet<T> toSortedSet(kotlin.sequences.Sequence<? extends T> sequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        return (java.util.SortedSet) kotlin.sequences.SequencesKt.toCollection(sequence, new java.util.TreeSet());
    }

    public static final <T> java.util.SortedSet<T> toSortedSet(kotlin.sequences.Sequence<? extends T> sequence, java.util.Comparator<? super T> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.SortedSet) kotlin.sequences.SequencesKt.toCollection(sequence, new java.util.TreeSet(comparator));
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Double m12578max(kotlin.sequences.Sequence $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.sequences.SequencesKt.maxOrNull($this$max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: max, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Float m12579max(kotlin.sequences.Sequence $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.sequences.SequencesKt.maxOrNull($this$max);
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Comparable max(kotlin.sequences.Sequence $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.sequences.SequencesKt.maxOrNull($this$max);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [T] */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <T, R extends java.lang.Comparable<? super R>> T maxBy(kotlin.sequences.Sequence<? extends T> sequence, kotlin.jvm.functions.Function1<? super T, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<? extends T> it = sequence.iterator();
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
    public static final /* synthetic */ java.lang.Object maxWith(kotlin.sequences.Sequence $this$maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.sequences.SequencesKt.maxWithOrNull($this$maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Double m12580min(kotlin.sequences.Sequence $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.sequences.SequencesKt.minOrNull($this$min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* JADX INFO: renamed from: min, reason: collision with other method in class */
    public static final /* synthetic */ java.lang.Float m12581min(kotlin.sequences.Sequence $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.sequences.SequencesKt.minOrNull($this$min);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Comparable min(kotlin.sequences.Sequence $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.sequences.SequencesKt.minOrNull($this$min);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [T, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [T] */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <T, R extends java.lang.Comparable<? super R>> T minBy(kotlin.sequences.Sequence<? extends T> sequence, kotlin.jvm.functions.Function1<? super T, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<? extends T> it = sequence.iterator();
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
    public static final /* synthetic */ java.lang.Object minWith(kotlin.sequences.Sequence $this$minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.sequences.SequencesKt.minWithOrNull($this$minWith, comparator);
    }

    private static final <T> java.math.BigDecimal sumOfBigDecimal(kotlin.sequences.Sequence<? extends T> sequence, kotlin.jvm.functions.Function1<? super T, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        for (java.lang.Object element : sequence) {
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(element));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final <T> java.math.BigInteger sumOfBigInteger(kotlin.sequences.Sequence<? extends T> sequence, kotlin.jvm.functions.Function1<? super T, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        for (java.lang.Object element : sequence) {
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(element));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }
}
