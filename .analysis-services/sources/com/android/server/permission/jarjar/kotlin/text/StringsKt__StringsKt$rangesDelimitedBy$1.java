package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Strings.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\r\n\u0002\b\u0002\u0010\u0000\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u0001*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0002H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "Lkotlin/Pair;", "", "", "currentIndex", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class StringsKt__StringsKt$rangesDelimitedBy$1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.lang.CharSequence, java.lang.Integer, com.android.server.permission.jarjar.kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer>> {
    final /* synthetic */ char[] $delimiters;
    final /* synthetic */ boolean $ignoreCase;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    StringsKt__StringsKt$rangesDelimitedBy$1(char[] cArr, boolean z) {
        super(2);
        this.$delimiters = cArr;
        this.$ignoreCase = z;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer> invoke(java.lang.CharSequence charSequence, java.lang.Integer num) {
        return invoke(charSequence, num.intValue());
    }

    public final com.android.server.permission.jarjar.kotlin.Pair<java.lang.Integer, java.lang.Integer> invoke(java.lang.CharSequence $this$$receiver, int currentIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$$receiver, "$this$$receiver");
        int it = com.android.server.permission.jarjar.kotlin.text.StringsKt.indexOfAny($this$$receiver, this.$delimiters, currentIndex, this.$ignoreCase);
        if (it < 0) {
            return null;
        }
        return com.android.server.permission.jarjar.kotlin.TuplesKt.to(java.lang.Integer.valueOf(it), 1);
    }
}
