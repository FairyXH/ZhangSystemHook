package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public interface IBatteryStatsImplWrapper {
    default com.android.server.power.stats.BatteryStatsImpl.BatteryCallback getBatteryCallback() {
        return null;
    }
}
