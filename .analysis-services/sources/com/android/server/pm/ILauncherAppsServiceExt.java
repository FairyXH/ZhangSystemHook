package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface ILauncherAppsServiceExt {
    default android.os.Handler getFgHandler(android.os.Handler handler) {
        return handler;
    }

    default void addExtraUserHandle(android.content.Intent intent, android.os.UserHandle user) {
    }

    default boolean checkMultiAppUserState(android.content.Context context, android.os.UserHandle user) {
        return false;
    }

    default void hookLauncherApps(android.os.Binder binder, android.content.Context context) {
    }

    default boolean isOhideAndLauncherCookie(android.os.Bundle extras, java.lang.String cookiePackage) {
        return false;
    }
}
