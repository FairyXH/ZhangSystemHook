package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Indent.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "line", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class StringsKt__IndentKt$getIndentFunction$2 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.String, java.lang.String> {
    final /* synthetic */ java.lang.String $indent;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    StringsKt__IndentKt$getIndentFunction$2(java.lang.String str) {
        super(1);
        this.$indent = str;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.lang.String invoke(java.lang.String line) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(line, "line");
        return this.$indent + line;
    }
}
