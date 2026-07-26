package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: StringBuilderJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlin/text/SystemProperties;", "", "()V", "LINE_SEPARATOR", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SystemProperties {
    public static final com.android.server.permission.jarjar.kotlin.text.SystemProperties INSTANCE = new com.android.server.permission.jarjar.kotlin.text.SystemProperties();
    public static final java.lang.String LINE_SEPARATOR;

    private SystemProperties() {
    }

    static {
        java.lang.String property = java.lang.System.getProperty("line.separator");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(property);
        LINE_SEPARATOR = property;
    }
}
