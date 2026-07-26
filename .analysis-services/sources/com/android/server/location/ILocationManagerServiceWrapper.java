package com.android.server.location;

/* JADX INFO: loaded from: classes2.dex */
public interface ILocationManagerServiceWrapper {
    default com.android.server.location.provider.LocationProviderManager getLocationProviderManager(java.lang.String providerName) {
        return null;
    }

    default void addLocationProviderManager(com.android.server.location.provider.LocationProviderManager locationProviderManager, com.android.server.location.provider.AbstractLocationProvider abstractLocationProvider) {
    }

    default void removeLocationProviderManager(com.android.server.location.provider.LocationProviderManager locationProviderManager) {
    }

    default com.android.server.location.provider.LocationProviderManager creatLocationProviderManager(java.lang.String providerName) {
        return null;
    }

    default android.location.IGpsGeofenceHardware getGpsGeofenceHardware() {
        return null;
    }
}
