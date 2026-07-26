package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusVpnManager extends android.common.IOplusCommonFeature {
    public static final com.android.server.connectivity.IOplusVpnManager DEFAULT = new com.android.server.connectivity.IOplusVpnManager() { // from class: com.android.server.connectivity.IOplusVpnManager.1
    };
    public static final java.lang.String NAME = "IOplusVpnManager";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusVpnManager;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean isVpnDisabled(android.content.ComponentName admin) {
        return false;
    }
}
