package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IGnssMeasurementsProviderExt extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IGnssMeasurementsProviderExt DEFAULT = new com.android.server.location.interfaces.IGnssMeasurementsProviderExt() { // from class: com.android.server.location.interfaces.IGnssMeasurementsProviderExt.1
    };

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.IGnssMeasurementsProviderExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void onRegistrationAdded(android.location.util.identity.CallerIdentity identity, android.location.GnssMeasurementRequest request) {
    }

    default void onRegistrationRemoved(android.location.util.identity.CallerIdentity identity) {
    }
}
