package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u000e\u0010\u0005\u001a\n \u0006*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"<anonymous>", "", "T", "Lkotlin/text/FlagEnum;", "", "it", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "invoke", "(Ljava/lang/Enum;)Ljava/lang/Boolean;", "com/android/server/permission/jarjar/kotlin/text/RegexKt$fromInt$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class Regex$special$$inlined$fromInt$1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<com.android.server.permission.jarjar.kotlin.text.RegexOption, java.lang.Boolean> {
    final /* synthetic */ int $value;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Regex$special$$inlined$fromInt$1(int i) {
        super(1);
        this.$value = i;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.lang.Boolean invoke(com.android.server.permission.jarjar.kotlin.text.RegexOption regexOption) {
        return java.lang.Boolean.valueOf((this.$value & regexOption.getMask()) == regexOption.getValue());
    }
}
