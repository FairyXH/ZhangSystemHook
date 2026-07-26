package com.android.server.permission.jarjar.kotlin.system;

/* JADX INFO: compiled from: Process.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b¨\u0006\u0004"}, d2 = {"exitProcess", "", "status", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ProcessKt {
    private static final java.lang.Void exitProcess(int status) {
        java.lang.System.exit(status);
        throw new java.lang.RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
    }
}
