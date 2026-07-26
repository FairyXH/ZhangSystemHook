package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IStationaryThrottlingLocationProviderExt extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IStationaryThrottlingLocationProviderExt DEFAULT = new com.android.server.location.interfaces.IStationaryThrottlingLocationProviderExt() { // from class: com.android.server.location.interfaces.IStationaryThrottlingLocationProviderExt.1
    };

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.IStationaryThrottlingLocationProviderExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
