package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface ILocationEventLogExt extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.ILocationEventLogExt DEFAULT = new com.android.server.location.interfaces.ILocationEventLogExt() { // from class: com.android.server.location.interfaces.ILocationEventLogExt.1
    };
    public static final java.lang.String Name = "ILocationEventLogExt";

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.ILocationEventLogExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean enablePassiveDeliveredLocations(java.lang.String provider) {
        return false;
    }
}
