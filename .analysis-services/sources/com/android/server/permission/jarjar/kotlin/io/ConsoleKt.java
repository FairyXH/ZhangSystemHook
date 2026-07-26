package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: Console.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u0013\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0007H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\fH\u0087\b\u001a\t\u0010\r\u001a\u00020\u0001H\u0087\b\u001a\u0013\u0010\r\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0007H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\fH\u0087\b\u001a\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u001a\b\u0010\u0010\u001a\u00020\u000fH\u0007\u001a\n\u0010\u0011\u001a\u0004\u0018\u00010\u000fH\u0007¨\u0006\u0012"}, d2 = {"print", "", "message", "", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "readln", "readlnOrNull", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ConsoleKt {
    private static final void print(java.lang.Object message) {
        java.lang.System.out.print(message);
    }

    private static final void print(int message) {
        java.lang.System.out.print(message);
    }

    private static final void print(long message) {
        java.lang.System.out.print(message);
    }

    private static final void print(byte message) {
        java.lang.System.out.print(java.lang.Byte.valueOf(message));
    }

    private static final void print(short message) {
        java.lang.System.out.print(java.lang.Short.valueOf(message));
    }

    private static final void print(char message) {
        java.lang.System.out.print(message);
    }

    private static final void print(boolean message) {
        java.lang.System.out.print(message);
    }

    private static final void print(float message) {
        java.lang.System.out.print(message);
    }

    private static final void print(double message) {
        java.lang.System.out.print(message);
    }

    private static final void print(char[] message) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        java.lang.System.out.print(message);
    }

    private static final void println(java.lang.Object message) {
        java.lang.System.out.println(message);
    }

    private static final void println(int message) {
        java.lang.System.out.println(message);
    }

    private static final void println(long message) {
        java.lang.System.out.println(message);
    }

    private static final void println(byte message) {
        java.lang.System.out.println(java.lang.Byte.valueOf(message));
    }

    private static final void println(short message) {
        java.lang.System.out.println(java.lang.Short.valueOf(message));
    }

    private static final void println(char message) {
        java.lang.System.out.println(message);
    }

    private static final void println(boolean message) {
        java.lang.System.out.println(message);
    }

    private static final void println(float message) {
        java.lang.System.out.println(message);
    }

    private static final void println(double message) {
        java.lang.System.out.println(message);
    }

    private static final void println(char[] message) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        java.lang.System.out.println(message);
    }

    private static final void println() {
        java.lang.System.out.println();
    }

    public static final java.lang.String readln() {
        java.lang.String str = readlnOrNull();
        if (str != null) {
            return str;
        }
        throw new com.android.server.permission.jarjar.kotlin.io.ReadAfterEOFException("EOF has already been reached");
    }

    public static final java.lang.String readlnOrNull() {
        return readLine();
    }

    public static final java.lang.String readLine() {
        com.android.server.permission.jarjar.kotlin.io.LineReader lineReader = com.android.server.permission.jarjar.kotlin.io.LineReader.INSTANCE;
        java.io.InputStream inputStream = java.lang.System.in;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(inputStream, "in");
        java.nio.charset.Charset charsetDefaultCharset = java.nio.charset.Charset.defaultCharset();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(charsetDefaultCharset, "defaultCharset(...)");
        return lineReader.readLine(inputStream, charsetDefaultCharset);
    }
}
