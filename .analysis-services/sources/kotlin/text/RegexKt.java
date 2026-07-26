package kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002¨\u0006\u0014"}, d2 = {"fromInt", "", "T", "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class RegexKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final int toInt(java.lang.Iterable<? extends kotlin.text.FlagEnum> iterable) {
        int accumulator$iv = 0;
        for (java.lang.Object element$iv : iterable) {
            kotlin.text.FlagEnum option = (kotlin.text.FlagEnum) element$iv;
            int value = accumulator$iv;
            accumulator$iv = value | option.getValue();
        }
        return accumulator$iv;
    }

    private static final /* synthetic */ <T extends java.lang.Enum<T> & kotlin.text.FlagEnum> java.util.Set<T> fromInt(final int value) {
        kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(4, "T");
        java.util.EnumSet enumSetAllOf = java.util.EnumSet.allOf(java.lang.Enum.class);
        java.util.EnumSet $this$fromInt_u24lambda_u241 = enumSetAllOf;
        kotlin.jvm.internal.Intrinsics.checkNotNull($this$fromInt_u24lambda_u241);
        kotlin.jvm.internal.Intrinsics.needClassReification();
        kotlin.collections.CollectionsKt.retainAll($this$fromInt_u24lambda_u241, new kotlin.jvm.functions.Function1<T, java.lang.Boolean>() { // from class: kotlin.text.RegexKt$fromInt$1$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            /* JADX WARN: Incorrect types in method signature: (TT;)Ljava/lang/Boolean; */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Boolean invoke(java.lang.Enum r3) {
                return java.lang.Boolean.valueOf((value & ((kotlin.text.FlagEnum) r3).getMask()) == ((kotlin.text.FlagEnum) r3).getValue());
            }
        });
        java.util.Set<T> setUnmodifiableSet = java.util.Collections.unmodifiableSet(enumSetAllOf);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(setUnmodifiableSet, "unmodifiableSet(...)");
        return setUnmodifiableSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlin.text.MatchResult findNext(java.util.regex.Matcher $this$findNext, int from, java.lang.CharSequence input) {
        if ($this$findNext.find(from)) {
            return new kotlin.text.MatcherMatchResult($this$findNext, input);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlin.text.MatchResult matchEntire(java.util.regex.Matcher $this$matchEntire, java.lang.CharSequence input) {
        if ($this$matchEntire.matches()) {
            return new kotlin.text.MatcherMatchResult($this$matchEntire, input);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlin.ranges.IntRange range(java.util.regex.MatchResult $this$range) {
        return kotlin.ranges.RangesKt.until($this$range.start(), $this$range.end());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlin.ranges.IntRange range(java.util.regex.MatchResult $this$range, int groupIndex) {
        return kotlin.ranges.RangesKt.until($this$range.start(groupIndex), $this$range.end(groupIndex));
    }
}
