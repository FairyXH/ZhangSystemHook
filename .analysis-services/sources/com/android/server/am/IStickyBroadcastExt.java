package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IStickyBroadcastExt {
    default java.lang.String stickyBroadcastToString() {
        return "";
    }

    default java.lang.String getOriginalCallingPkg() {
        return null;
    }

    default void setOriginalCallingPkg(java.lang.String pkg) {
    }

    default boolean isThirdCaller() {
        return false;
    }

    default void setThirdCaller(boolean thirdCaller) {
    }
}
