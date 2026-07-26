package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IPermissionHelperExt {
    default boolean canModifyNotificationPermissionForPackage(java.lang.String pkg, int uid) {
        return true;
    }

    default boolean isLoggable() {
        return false;
    }
}
