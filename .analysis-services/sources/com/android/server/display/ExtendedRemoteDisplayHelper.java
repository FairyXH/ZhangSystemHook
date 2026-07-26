package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class ExtendedRemoteDisplayHelper {
    private static final java.lang.String TAG = "ExtendedRemoteDisplayHelper";
    private static java.lang.Class sExtRemoteDisplayClass;
    private static java.lang.reflect.Method sExtRemoteDisplayDispose;
    private static java.lang.reflect.Method sExtRemoteDisplayListen;

    ExtendedRemoteDisplayHelper() {
    }

    static {
        try {
            sExtRemoteDisplayClass = java.lang.Class.forName("com.qualcomm.wfd.ExtendedRemoteDisplay");
        } catch (java.lang.Throwable th) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay Not available.");
        }
        if (sExtRemoteDisplayClass != null) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay Is available. Find Methods");
            try {
                sExtRemoteDisplayListen = sExtRemoteDisplayClass.getDeclaredMethod("listen", java.lang.String.class, android.media.RemoteDisplay.Listener.class, android.os.Handler.class, android.content.Context.class);
            } catch (java.lang.Throwable th2) {
                android.util.Slog.i(TAG, "ExtendedRemoteDisplay.listen Not available.");
            }
            try {
                sExtRemoteDisplayDispose = sExtRemoteDisplayClass.getDeclaredMethod("dispose", new java.lang.Class[0]);
            } catch (java.lang.Throwable th3) {
                android.util.Slog.i(TAG, "ExtendedRemoteDisplay.dispose Not available.");
            }
        }
    }

    public static java.lang.Object listen(java.lang.String iface, android.media.RemoteDisplay.Listener listener, android.os.Handler handler, android.content.Context context) {
        android.util.Slog.i(TAG, "ExtendedRemoteDisplay.listen");
        if (sExtRemoteDisplayListen == null || sExtRemoteDisplayDispose == null) {
            return null;
        }
        try {
            java.lang.Object extRemoteDisplay = sExtRemoteDisplayListen.invoke(null, iface, listener, handler, context);
            return extRemoteDisplay;
        } catch (java.lang.IllegalAccessException e) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay.listen -IllegalAccessException");
            e.printStackTrace();
            return null;
        } catch (java.lang.reflect.InvocationTargetException e2) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay.listen - InvocationTargetException");
            java.lang.Throwable cause = e2.getCause();
            if (cause instanceof java.lang.RuntimeException) {
                throw ((java.lang.RuntimeException) cause);
            }
            if (cause instanceof java.lang.Error) {
                throw ((java.lang.Error) cause);
            }
            throw new java.lang.RuntimeException(e2);
        }
    }

    public static void dispose(java.lang.Object extRemoteDisplay) {
        android.util.Slog.i(TAG, "ExtendedRemoteDisplay.dispose");
        try {
            sExtRemoteDisplayDispose.invoke(extRemoteDisplay, new java.lang.Object[0]);
        } catch (java.lang.IllegalAccessException e) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay.dispose-IllegalAccessException");
            e.printStackTrace();
        } catch (java.lang.reflect.InvocationTargetException e2) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay.dispose - InvocationTargetException");
            java.lang.Throwable cause = e2.getCause();
            if (cause instanceof java.lang.RuntimeException) {
                throw ((java.lang.RuntimeException) cause);
            }
            if (cause instanceof java.lang.Error) {
                throw ((java.lang.Error) cause);
            }
            throw new java.lang.RuntimeException(e2);
        }
    }

    public static boolean isAvailable() {
        if (sExtRemoteDisplayClass != null && sExtRemoteDisplayDispose != null && sExtRemoteDisplayListen != null) {
            android.util.Slog.i(TAG, "ExtendedRemoteDisplay isAvailable() : Available.");
            return true;
        }
        android.util.Slog.i(TAG, "ExtendedRemoteDisplay isAvailable() : Not Available.");
        return false;
    }
}
