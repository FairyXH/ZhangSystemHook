package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: Exceptions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\u0002¨\u0006\u0006"}, d2 = {"constructMessage", "", "file", "Ljava/io/File;", "other", com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ExceptionsKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.String constructMessage(java.io.File file, java.io.File other, java.lang.String reason) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(file.toString());
        if (other != null) {
            sb.append(" -> " + other);
        }
        if (reason != null) {
            sb.append(": " + reason);
        }
        java.lang.String string = sb.toString();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }
}
