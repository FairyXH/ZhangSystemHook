package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeviceStationaryHelper {
    public abstract void addListener(com.android.server.DeviceIdleInternal.StationaryListener stationaryListener);

    public abstract void removeListener(com.android.server.DeviceIdleInternal.StationaryListener stationaryListener);
}
