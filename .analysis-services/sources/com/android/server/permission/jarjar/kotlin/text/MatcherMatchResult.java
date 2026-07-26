package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\n\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00168VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001b¨\u0006\u001d"}, d2 = {"Lkotlin/text/MatcherMatchResult;", "Lkotlin/text/MatchResult;", "matcher", "Ljava/util/regex/Matcher;", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, "", "(Ljava/util/regex/Matcher;Ljava/lang/CharSequence;)V", "groupValues", "", "", "getGroupValues", "()Ljava/util/List;", "groupValues_", "groups", "Lkotlin/text/MatchGroupCollection;", "getGroups", "()Lkotlin/text/MatchGroupCollection;", "matchResult", "Ljava/util/regex/MatchResult;", "getMatchResult", "()Ljava/util/regex/MatchResult;", "range", "Lkotlin/ranges/IntRange;", "getRange", "()Lkotlin/ranges/IntRange;", "value", "getValue", "()Ljava/lang/String;", "next", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class MatcherMatchResult implements com.android.server.permission.jarjar.kotlin.text.MatchResult {
    private java.util.List<java.lang.String> groupValues_;
    private final com.android.server.permission.jarjar.kotlin.text.MatchGroupCollection groups;
    private final java.lang.CharSequence input;
    private final java.util.regex.Matcher matcher;

    public MatcherMatchResult(java.util.regex.Matcher matcher, java.lang.CharSequence input) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(matcher, "matcher");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(input, com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT);
        this.matcher = matcher;
        this.input = input;
        this.groups = new com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult$groups$1(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchResult
    public com.android.server.permission.jarjar.kotlin.text.MatchResult.Destructured getDestructured() {
        return com.android.server.permission.jarjar.kotlin.text.MatchResult.DefaultImpls.getDestructured(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.util.regex.MatchResult getMatchResult() {
        return this.matcher;
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchResult
    public com.android.server.permission.jarjar.kotlin.ranges.IntRange getRange() {
        return com.android.server.permission.jarjar.kotlin.text.RegexKt.range(getMatchResult());
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchResult
    public java.lang.String getValue() {
        java.lang.String strGroup = getMatchResult().group();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strGroup, "group(...)");
        return strGroup;
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchResult
    public com.android.server.permission.jarjar.kotlin.text.MatchGroupCollection getGroups() {
        return this.groups;
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchResult
    public java.util.List<java.lang.String> getGroupValues() {
        if (this.groupValues_ == null) {
            this.groupValues_ = new com.android.server.permission.jarjar.kotlin.collections.AbstractList<java.lang.String>() { // from class: com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult$groupValues$1
                @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
                public final /* bridge */ boolean contains(java.lang.Object element) {
                    if (element instanceof java.lang.String) {
                        return contains((java.lang.String) element);
                    }
                    return false;
                }

                public /* bridge */ boolean contains(java.lang.String element) {
                    return super.contains(element);
                }

                @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
                public final /* bridge */ int indexOf(java.lang.Object element) {
                    if (element instanceof java.lang.String) {
                        return indexOf((java.lang.String) element);
                    }
                    return -1;
                }

                public /* bridge */ int indexOf(java.lang.String element) {
                    return super.indexOf(element);
                }

                @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
                public final /* bridge */ int lastIndexOf(java.lang.Object element) {
                    if (element instanceof java.lang.String) {
                        return lastIndexOf((java.lang.String) element);
                    }
                    return -1;
                }

                public /* bridge */ int lastIndexOf(java.lang.String element) {
                    return super.lastIndexOf(element);
                }

                @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection
                public int getSize() {
                    return this.this$0.getMatchResult().groupCount() + 1;
                }

                @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
                public java.lang.String get(int index) {
                    java.lang.String strGroup = this.this$0.getMatchResult().group(index);
                    return strGroup == null ? "" : strGroup;
                }
            };
        }
        java.util.List<java.lang.String> list = this.groupValues_;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(list);
        return list;
    }

    @Override // com.android.server.permission.jarjar.kotlin.text.MatchResult
    public com.android.server.permission.jarjar.kotlin.text.MatchResult next() {
        int nextIndex = getMatchResult().end() + (getMatchResult().end() == getMatchResult().start() ? 1 : 0);
        if (nextIndex > this.input.length()) {
            return null;
        }
        java.util.regex.Matcher matcher = this.matcher.pattern().matcher(this.input);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(matcher, "matcher(...)");
        return com.android.server.permission.jarjar.kotlin.text.RegexKt.findNext(matcher, nextIndex, this.input);
    }
}
