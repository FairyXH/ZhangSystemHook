package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class LogUtil {
    public static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public static void d(java.lang.String tag, java.lang.String info) {
        android.util.Log.d(tag, info);
    }

    public static void i(java.lang.String tag, java.lang.String info) {
        android.util.Log.i(tag, info);
    }

    public static void debugD(java.lang.String tag, java.lang.String info) {
        if (DEBUG) {
            android.util.Log.d(tag, info);
        }
    }

    public static void debugI(java.lang.String tag, java.lang.String info) {
        if (DEBUG) {
            android.util.Log.i(tag, info);
        }
    }

    public static void debuglogD(java.lang.String tag, java.lang.String info) {
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Log.d(tag, info);
        }
    }

    public static void debuglogI(java.lang.String tag, java.lang.String info) {
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Log.i(tag, info);
        }
    }

    public static void debuglogE(java.lang.String tag, java.lang.String info) {
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Log.e(tag, info);
        }
    }

    public static void sDebugD(java.lang.String tag, java.lang.String info) {
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Slog.d(tag, info);
        }
    }

    public static void sDebugI(java.lang.String tag, java.lang.String info) {
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Slog.i(tag, info);
        }
    }

    public static void sDebugE(java.lang.String tag, java.lang.String info) {
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Slog.e(tag, info);
        }
    }
}
