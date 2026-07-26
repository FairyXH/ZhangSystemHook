package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: RegexExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0005H\u0087\b¨\u0006\u0007"}, d2 = {"toRegex", "Lkotlin/text/Regex;", "", "options", "", "Lkotlin/text/RegexOption;", "option", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/text/StringsKt")
class StringsKt__RegexExtensionsKt extends com.android.server.permission.jarjar.kotlin.text.StringsKt__RegexExtensionsJVMKt {
    private static final com.android.server.permission.jarjar.kotlin.text.Regex toRegex(java.lang.String $this$toRegex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toRegex, "<this>");
        return new com.android.server.permission.jarjar.kotlin.text.Regex($this$toRegex);
    }

    private static final com.android.server.permission.jarjar.kotlin.text.Regex toRegex(java.lang.String $this$toRegex, com.android.server.permission.jarjar.kotlin.text.RegexOption option) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toRegex, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(option, "option");
        return new com.android.server.permission.jarjar.kotlin.text.Regex($this$toRegex, option);
    }

    private static final com.android.server.permission.jarjar.kotlin.text.Regex toRegex(java.lang.String $this$toRegex, java.util.Set<? extends com.android.server.permission.jarjar.kotlin.text.RegexOption> set) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toRegex, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(set, "options");
        return new com.android.server.permission.jarjar.kotlin.text.Regex($this$toRegex, set);
    }
}
