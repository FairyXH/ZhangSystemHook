package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusVpnHelper extends android.common.IOplusCommonFeature {
    public static final com.android.server.connectivity.IOplusVpnHelper DEFAULT = new com.android.server.connectivity.IOplusVpnHelper() { // from class: com.android.server.connectivity.IOplusVpnHelper.1
    };
    public static final java.lang.String NAME = "IOplusVpnHelper";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusVpnHelper;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default com.android.internal.net.VpnConfig parseApplicationsFromXml(com.android.internal.net.VpnConfig config) {
        return config;
    }

    default android.app.PendingIntent prepareStatusIntent(android.app.PendingIntent statusIntent) {
        return statusIntent;
    }

    default void showNotification(java.lang.String label, int iconPlaceHolder, int userHandler, java.lang.String curPackage, android.app.PendingIntent statusIntent, com.android.internal.net.VpnConfig config) {
    }

    default void hideNotification(int user_handler) {
    }
}
