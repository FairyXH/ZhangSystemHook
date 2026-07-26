package com.android.server.location.common;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusCommonFeature {
    com.android.server.location.common.IOplusCommonFeature getDefault();

    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.End;
    }
}
