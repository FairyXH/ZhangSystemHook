package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusStorageAllFileAccessManager extends android.common.IOplusCommonFeature {
    public static final com.android.server.storage.IOplusStorageAllFileAccessManager DEFAULT = new com.android.server.storage.IOplusStorageAllFileAccessManager() { // from class: com.android.server.storage.IOplusStorageAllFileAccessManager.1
    };
    public static final java.lang.String NAME = "IOplusStorageAllFileAccessManager";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusStorageAllFileAccessManager;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initArgs(android.content.Context context) {
    }

    default boolean checkAppWhitelist(java.lang.String packageName, int uid) {
        return false;
    }

    default void servicesReady() {
    }

    default java.util.ArrayList<java.lang.Integer> computeGidsForOplus(int mountExternal, int uid) {
        return null;
    }

    default void dump(java.io.PrintWriter writer) {
    }
}
