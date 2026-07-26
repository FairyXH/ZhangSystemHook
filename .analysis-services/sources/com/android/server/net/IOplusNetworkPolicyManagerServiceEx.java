package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusNetworkPolicyManagerServiceEx extends com.android.server.IOplusCommonManagerServiceEx {
    public static final com.android.server.net.IOplusNetworkPolicyManagerServiceEx DEFAULT = new com.android.server.net.IOplusNetworkPolicyManagerServiceEx() { // from class: com.android.server.net.IOplusNetworkPolicyManagerServiceEx.1
    };
    public static final java.lang.String TYPE_DAILY = "daily";
    public static final java.lang.String TYPE_MONTH = "month";

    default com.android.server.net.NetworkPolicyManagerService getNetworkPolicyManagerService() {
        return null;
    }

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusNetworkPolicyManagerServiceEx;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean addThirdPartyRestrictBGWhitelistUidsUL(int userId, android.util.SparseBooleanArray mDefaultRestrictBackgroundWhitelistUids, android.util.SparseBooleanArray mRestrictBackgroundWhitelistRevokedUids) {
        return false;
    }

    default void googleRestrictInit(android.content.Context context, android.os.Handler handler, android.content.pm.IPackageManager pm, java.lang.Object firstLock) {
    }

    default int getCloneAppUidNL(int uid) {
        return uid;
    }

    default void removeCloneUidPolicyNL(int uid) {
    }

    default boolean isCloneUidNL(int uid) {
        return false;
    }

    default boolean getGameSpaceMode() {
        return false;
    }

    default void setGameSpaceMode(boolean gameMode) {
    }

    default boolean isUidAllowedNetworkWhileBackground(int uid) {
        return false;
    }
}
