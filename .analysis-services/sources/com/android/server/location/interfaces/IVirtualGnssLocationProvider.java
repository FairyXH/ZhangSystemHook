package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IVirtualGnssLocationProvider extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IVirtualGnssLocationProvider DEFAULT = new com.android.server.location.interfaces.IVirtualGnssLocationProvider() { // from class: com.android.server.location.interfaces.IVirtualGnssLocationProvider.1
    };
    public static final java.lang.String Name = "IVirtualGnssLocationProvider";

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.IVirtualGnssLocationProvider;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default com.android.server.location.provider.AbstractLocationProvider getVirtualProvider(android.content.Context context) {
        return null;
    }

    default void addGnssStatusProvider(com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks callback) {
    }

    default void registerGnssStatusCallback() {
    }

    default void unregisterGnssStatusCallback() {
    }

    default void addGnssNmeaProvider(com.android.server.location.gnss.IGnssNmeaProviderWrapper provider) {
    }

    default void registerGnssNmeaCallback() {
    }

    default void unregisterGnssNmeaCallback() {
    }
}
