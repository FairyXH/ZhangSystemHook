package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public interface IGnssLocationProviderWrapper {
    default com.android.server.location.gnss.hal.GnssNative getGnssNative() {
        return null;
    }

    default void startNavigating() {
    }

    default void stopNavigating() {
    }

    default void updateClientUids(android.os.WorkSource source) {
    }

    default void reportLocation(android.location.LocationResult result) {
    }

    default void subscriptionOrCarrierConfigChanged() {
    }

    default void updateData(android.os.Bundle extra) {
    }

    default void registerNetworkLocationListener() {
    }
}
