package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IPermissionHelperWrapper {
    default com.android.server.notification.IPermissionHelperExt getPermissionHelperExt() {
        return null;
    }

    default boolean canModifyNotificationPermissionForPackage(java.lang.String packageName, int uid) {
        return true;
    }
}
