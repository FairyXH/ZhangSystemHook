package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Lkotlin/text/MatchGroup;", "it", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class MatcherMatchResult$groups$1$iterator$1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.Integer, com.android.server.permission.jarjar.kotlin.text.MatchGroup> {
    final /* synthetic */ com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult$groups$1 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    MatcherMatchResult$groups$1$iterator$1(com.android.server.permission.jarjar.kotlin.text.MatcherMatchResult$groups$1 matcherMatchResult$groups$1) {
        super(1);
        this.this$0 = matcherMatchResult$groups$1;
    }

    public final com.android.server.permission.jarjar.kotlin.text.MatchGroup invoke(int it) {
        return this.this$0.get(it);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.text.MatchGroup invoke(java.lang.Integer num) {
        return invoke(num.intValue());
    }
}
