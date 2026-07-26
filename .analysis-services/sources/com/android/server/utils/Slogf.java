package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public final class Slogf {
    private static final java.util.Formatter sFormatter;
    private static final java.lang.StringBuilder sMessageBuilder;

    static {
        android.util.TimingsTraceLog t = new android.util.TimingsTraceLog("SLog", 524288L);
        t.traceBegin("static_init");
        sMessageBuilder = new java.lang.StringBuilder();
        sFormatter = new java.util.Formatter(sMessageBuilder, java.util.Locale.ENGLISH);
        t.traceEnd();
    }

    private Slogf() {
        throw new java.lang.UnsupportedOperationException("provides only static methods");
    }

    public static int v(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.v(tag, msg);
    }

    public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Slog.v(tag, msg, tr);
    }

    public static int d(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.d(tag, msg);
    }

    public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Slog.d(tag, msg, tr);
    }

    public static int i(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.i(tag, msg);
    }

    public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Slog.i(tag, msg, tr);
    }

    public static int w(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.w(tag, msg);
    }

    public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Slog.w(tag, msg, tr);
    }

    public static int w(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Slog.w(tag, tr);
    }

    public static int e(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.e(tag, msg);
    }

    public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Slog.e(tag, msg, tr);
    }

    public static int wtf(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.wtf(tag, msg);
    }

    public static void wtfQuiet(java.lang.String tag, java.lang.String msg) {
        android.util.Slog.wtfQuiet(tag, msg);
    }

    public static int wtfStack(java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.wtfStack(tag, msg);
    }

    public static int wtf(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Slog.wtf(tag, tr);
    }

    public static int wtf(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Slog.wtf(tag, msg, tr);
    }

    public static int println(int priority, java.lang.String tag, java.lang.String msg) {
        return android.util.Slog.println(priority, tag, msg);
    }

    public static void v(java.lang.String tag, java.lang.String format, java.lang.Object... args) {
        v(tag, getMessage(format, args));
    }

    public static void v(java.lang.String tag, java.lang.Throwable throwable, java.lang.String format, java.lang.Object... args) {
        v(tag, getMessage(format, args), throwable);
    }

    public static void d(java.lang.String tag, java.lang.String format, java.lang.Object... args) {
        d(tag, getMessage(format, args));
    }

    public static void d(java.lang.String tag, java.lang.Throwable throwable, java.lang.String format, java.lang.Object... args) {
        d(tag, getMessage(format, args), throwable);
    }

    public static void i(java.lang.String tag, java.lang.String format, java.lang.Object... args) {
        i(tag, getMessage(format, args));
    }

    public static void i(java.lang.String tag, java.lang.Throwable throwable, java.lang.String format, java.lang.Object... args) {
        i(tag, getMessage(format, args), throwable);
    }

    public static void w(java.lang.String tag, java.lang.String format, java.lang.Object... args) {
        w(tag, getMessage(format, args));
    }

    public static void w(java.lang.String tag, java.lang.Throwable throwable, java.lang.String format, java.lang.Object... args) {
        w(tag, getMessage(format, args), throwable);
    }

    public static void e(java.lang.String tag, java.lang.String format, java.lang.Object... args) {
        e(tag, getMessage(format, args));
    }

    public static void e(java.lang.String tag, java.lang.Throwable throwable, java.lang.String format, java.lang.Object... args) {
        e(tag, getMessage(format, args), throwable);
    }

    public static void wtf(java.lang.String tag, java.lang.String format, java.lang.Object... args) {
        wtf(tag, getMessage(format, args));
    }

    public static void wtf(java.lang.String tag, java.lang.Throwable throwable, java.lang.String format, java.lang.Object... args) {
        wtf(tag, getMessage(format, args), throwable);
    }

    private static java.lang.String getMessage(java.lang.String format, java.lang.Object... args) {
        java.lang.String message;
        synchronized (sMessageBuilder) {
            sFormatter.format(format, args);
            message = sMessageBuilder.toString();
            sMessageBuilder.setLength(0);
        }
        return message;
    }
}
