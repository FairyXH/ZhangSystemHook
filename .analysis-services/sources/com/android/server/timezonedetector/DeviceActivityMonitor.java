package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
interface DeviceActivityMonitor extends com.android.server.timezonedetector.Dumpable {

    public interface Listener {
        void onFlightComplete();
    }

    void addListener(com.android.server.timezonedetector.DeviceActivityMonitor.Listener listener);
}
