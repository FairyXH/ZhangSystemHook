package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusNetworkStatsEx extends android.common.IOplusCommonFeature {
    public static final com.android.server.net.IOplusNetworkStatsEx DEFAULT = new com.android.server.net.IOplusNetworkStatsEx() { // from class: com.android.server.net.IOplusNetworkStatsEx.1
    };
    public static final java.lang.String NAME = "IOplusNetworkStatsEx";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusNetworkStatsEx;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initArgs(android.content.Context context, android.os.Handler handler) {
    }
}
