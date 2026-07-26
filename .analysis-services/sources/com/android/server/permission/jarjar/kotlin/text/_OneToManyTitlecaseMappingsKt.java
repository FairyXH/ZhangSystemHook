package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: _OneToManyTitlecaseMappings.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\f\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000¨\u0006\u0003"}, d2 = {"titlecaseImpl", "", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class _OneToManyTitlecaseMappingsKt {
    public static final java.lang.String titlecaseImpl(char $this$titlecaseImpl) {
        java.lang.String strValueOf = java.lang.String.valueOf($this$titlecaseImpl);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        java.lang.String uppercase = strValueOf.toUpperCase(java.util.Locale.ROOT);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(uppercase, "toUpperCase(...)");
        if (uppercase.length() > 1) {
            if ($this$titlecaseImpl == 329) {
                return uppercase;
            }
            char cCharAt = uppercase.charAt(0);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(uppercase, "null cannot be cast to non-null type java.lang.String");
            java.lang.String strSubstring = uppercase.substring(1);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
            java.lang.String lowerCase = strSubstring.toLowerCase(java.util.Locale.ROOT);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            return cCharAt + lowerCase;
        }
        return java.lang.String.valueOf(java.lang.Character.toTitleCase($this$titlecaseImpl));
    }
}
