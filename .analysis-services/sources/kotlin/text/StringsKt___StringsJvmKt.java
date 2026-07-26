package kotlin.text;

/* JADX INFO: compiled from: _StringsJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0006\u001a;\u0010\u0007\u001a\u0004\u0018\u00010\u0001\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\b0\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\f\u001a/\u0010\r\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00010\u000fj\n\u0012\u0006\b\u0000\u0012\u00020\u0001`\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0006\u001a;\u0010\u0013\u001a\u0004\u0018\u00010\u0001\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\b0\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\f\u001a/\u0010\u0014\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00010\u000fj\n\u0012\u0006\b\u0000\u0012\u00020\u0001`\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a)\u0010\u0015\u001a\u00020\u0016*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u0017\u001a)\u0010\u0015\u001a\u00020\u0018*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00180\u000bH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u0019\u001a\u0010\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u001b*\u00020\u0002\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001c"}, d2 = {"elementAt", "", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "max", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
class StringsKt___StringsJvmKt extends kotlin.text.StringsKt__StringsKt {
    private static final char elementAt(java.lang.CharSequence $this$elementAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$elementAt, "<this>");
        return $this$elementAt.charAt(index);
    }

    public static final java.util.SortedSet<java.lang.Character> toSortedSet(java.lang.CharSequence $this$toSortedSet) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        return (java.util.SortedSet) kotlin.text.StringsKt.toCollection($this$toSortedSet, new java.util.TreeSet());
    }

    @kotlin.Deprecated(message = "Use maxOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Character max(java.lang.CharSequence $this$max) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$max, "<this>");
        return kotlin.text.StringsKt.maxOrNull($this$max);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use maxByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <R extends java.lang.Comparable<? super R>> java.lang.Character maxBy(java.lang.CharSequence $this$maxBy, kotlin.jvm.functions.Function1<? super java.lang.Character, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxBy, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if ($this$maxBy.length() == 0) {
            return null;
        }
        char maxElem$iv = $this$maxBy.charAt(0);
        int lastIndex$iv = kotlin.text.StringsKt.getLastIndex($this$maxBy);
        if (lastIndex$iv == 0) {
            return java.lang.Character.valueOf(maxElem$iv);
        }
        java.lang.Comparable maxValue$iv = selector.invoke(java.lang.Character.valueOf(maxElem$iv));
        ?? it = new kotlin.ranges.IntRange(1, lastIndex$iv).iterator();
        while (it.hasNext()) {
            int i$iv = it.nextInt();
            char e$iv = $this$maxBy.charAt(i$iv);
            R rInvoke = selector.invoke(java.lang.Character.valueOf(e$iv));
            if (maxValue$iv.compareTo(rInvoke) < 0) {
                maxElem$iv = e$iv;
                maxValue$iv = rInvoke;
            }
        }
        return java.lang.Character.valueOf(maxElem$iv);
    }

    @kotlin.Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Character maxWith(java.lang.CharSequence $this$maxWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$maxWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.text.StringsKt.maxWithOrNull($this$maxWith, comparator);
    }

    @kotlin.Deprecated(message = "Use minOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minOrNull()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Character min(java.lang.CharSequence $this$min) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$min, "<this>");
        return kotlin.text.StringsKt.minOrNull($this$min);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [kotlin.collections.IntIterator] */
    @kotlin.Deprecated(message = "Use minByOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <R extends java.lang.Comparable<? super R>> java.lang.Character minBy(java.lang.CharSequence $this$minBy, kotlin.jvm.functions.Function1<? super java.lang.Character, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minBy, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        if ($this$minBy.length() == 0) {
            return null;
        }
        char minElem$iv = $this$minBy.charAt(0);
        int lastIndex$iv = kotlin.text.StringsKt.getLastIndex($this$minBy);
        if (lastIndex$iv == 0) {
            return java.lang.Character.valueOf(minElem$iv);
        }
        java.lang.Comparable minValue$iv = selector.invoke(java.lang.Character.valueOf(minElem$iv));
        ?? it = new kotlin.ranges.IntRange(1, lastIndex$iv).iterator();
        while (it.hasNext()) {
            int i$iv = it.nextInt();
            char e$iv = $this$minBy.charAt(i$iv);
            R rInvoke = selector.invoke(java.lang.Character.valueOf(e$iv));
            if (minValue$iv.compareTo(rInvoke) > 0) {
                minElem$iv = e$iv;
                minValue$iv = rInvoke;
            }
        }
        return java.lang.Character.valueOf(minElem$iv);
    }

    @kotlin.Deprecated(message = "Use minWithOrNull instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ java.lang.Character minWith(java.lang.CharSequence $this$minWith, java.util.Comparator comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return kotlin.text.StringsKt.minWithOrNull($this$minWith, comparator);
    }

    private static final java.math.BigDecimal sumOfBigDecimal(java.lang.CharSequence $this$sumOf, kotlin.jvm.functions.Function1<? super java.lang.Character, ? extends java.math.BigDecimal> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigDecimal sum = java.math.BigDecimal.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        for (int i = 0; i < $this$sumOf.length(); i++) {
            char element = $this$sumOf.charAt(i);
            java.math.BigDecimal bigDecimalAdd = sum.add(selector.invoke(java.lang.Character.valueOf(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigDecimalAdd, "add(...)");
            sum = bigDecimalAdd;
        }
        return sum;
    }

    private static final java.math.BigInteger sumOfBigInteger(java.lang.CharSequence $this$sumOf, kotlin.jvm.functions.Function1<? super java.lang.Character, ? extends java.math.BigInteger> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.math.BigInteger sum = java.math.BigInteger.valueOf(0L);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sum, "valueOf(...)");
        for (int i = 0; i < $this$sumOf.length(); i++) {
            char element = $this$sumOf.charAt(i);
            java.math.BigInteger bigIntegerAdd = sum.add(selector.invoke(java.lang.Character.valueOf(element)));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
            sum = bigIntegerAdd;
        }
        return sum;
    }
}
