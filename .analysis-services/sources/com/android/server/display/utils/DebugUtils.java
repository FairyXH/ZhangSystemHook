package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public class DebugUtils {
    public static final boolean DEBUG_ALL = android.util.Log.isLoggable("DisplayManager_All", 3);

    public static boolean isDebuggable(java.lang.String tag) {
        return android.util.Log.isLoggable(tag, 3) || DEBUG_ALL;
    }
}
