package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public interface IGnssLocationProviderSocExt {
    default void init(android.content.Context context, android.os.Handler handler, com.android.server.location.gnss.NtpNetworkTimeHelper ntpNetworkTimeHelper) {
    }

    default void onReportSvStatus(android.location.GnssStatus gnssStatus) {
    }

    default int onDeleteAidingData(android.os.Bundle extras, int flag) {
        return flag;
    }

    default void onRequestLocation(com.android.server.location.gnss.hal.GnssNative gnssNative) {
    }

    default void onGnssLocationProviderInitialize() {
    }
}
