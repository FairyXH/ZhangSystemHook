package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public interface IVpnExt {
    default void init(android.content.Context context) {
    }

    default com.android.internal.net.VpnConfig parseApplicationsFromXml(com.android.internal.net.VpnConfig config) {
        return config;
    }

    default android.app.PendingIntent prepareStatusIntent(android.app.PendingIntent statusIntent) {
        return statusIntent;
    }

    default void showNotification(java.lang.String label, int iconPlaceHolder, int userHandler, java.lang.String curPackage, android.app.PendingIntent statusIntent, com.android.internal.net.VpnConfig config) {
    }

    default void hideNotification(int userHandler) {
    }

    default boolean isVpnDisabled(android.content.ComponentName admin) {
        return false;
    }

    default boolean doesHaveVPNAppWhiteList(android.content.ComponentName admin) {
        return false;
    }

    default boolean isInVPNAppWhiteList(android.content.ComponentName admin, java.lang.String packageName) {
        return false;
    }
}
