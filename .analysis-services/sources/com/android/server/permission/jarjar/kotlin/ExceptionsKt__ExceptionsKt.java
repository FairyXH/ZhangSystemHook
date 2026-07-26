package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Exceptions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0014\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003H\u0007\u001a\r\u0010\u0010\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0010\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0012H\u0087\b\u001a\u0015\u0010\u0010\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\f\u0010\u0015\u001a\u00020\u0016*\u00020\u0003H\u0007\"!\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\f\u0012\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007\"$\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t*\u00020\u00038FX\u0087\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u0005\u001a\u0004\b\u000b\u0010\f¨\u0006\u0017"}, d2 = {"stackTrace", "", "Ljava/lang/StackTraceElement;", "", "getStackTrace$annotations", "(Ljava/lang/Throwable;)V", "getStackTrace", "(Ljava/lang/Throwable;)[Ljava/lang/StackTraceElement;", "suppressedExceptions", "", "getSuppressedExceptions$annotations", "getSuppressedExceptions", "(Ljava/lang/Throwable;)Ljava/util/List;", "addSuppressed", "", "exception", "printStackTrace", "stream", "Ljava/io/PrintStream;", "writer", "Ljava/io/PrintWriter;", "stackTraceToString", "", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/ExceptionsKt")
public class ExceptionsKt__ExceptionsKt {
    public static /* synthetic */ void getStackTrace$annotations(java.lang.Throwable th) {
    }

    public static /* synthetic */ void getSuppressedExceptions$annotations(java.lang.Throwable th) {
    }

    private static final void printStackTrace(java.lang.Throwable $this$printStackTrace) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$printStackTrace, "<this>");
        $this$printStackTrace.printStackTrace();
    }

    private static final void printStackTrace(java.lang.Throwable $this$printStackTrace, java.io.PrintWriter writer) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$printStackTrace, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(writer, "writer");
        $this$printStackTrace.printStackTrace(writer);
    }

    private static final void printStackTrace(java.lang.Throwable $this$printStackTrace, java.io.PrintStream stream) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$printStackTrace, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stream, "stream");
        $this$printStackTrace.printStackTrace(stream);
    }

    public static final java.lang.StackTraceElement[] getStackTrace(java.lang.Throwable $this$stackTrace) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$stackTrace, "<this>");
        java.lang.StackTraceElement[] stackTrace = $this$stackTrace.getStackTrace();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(stackTrace);
        return stackTrace;
    }

    public static final java.lang.String stackTraceToString(java.lang.Throwable $this$stackTraceToString) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$stackTraceToString, "<this>");
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        $this$stackTraceToString.printStackTrace(pw);
        pw.flush();
        java.lang.String string = sw.toString();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static final void addSuppressed(java.lang.Throwable $this$addSuppressed, java.lang.Throwable exception) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$addSuppressed, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        if ($this$addSuppressed != exception) {
            com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed($this$addSuppressed, exception);
        }
    }

    public static final java.util.List<java.lang.Throwable> getSuppressedExceptions(java.lang.Throwable $this$suppressedExceptions) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$suppressedExceptions, "<this>");
        return com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.IMPLEMENTATIONS.getSuppressed($this$suppressedExceptions);
    }
}
