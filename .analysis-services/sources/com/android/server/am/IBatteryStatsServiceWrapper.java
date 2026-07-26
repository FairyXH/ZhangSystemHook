package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBatteryStatsServiceWrapper {
    default com.android.server.power.stats.BatteryExternalStatsWorker getWorker() {
        return null;
    }

    default com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider getUserManagerUserInfoProvider() {
        return new com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider() { // from class: com.android.server.am.IBatteryStatsServiceWrapper.1
            @Override // com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider
            public int[] getUserIds() {
                return null;
            }
        };
    }

    default com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig getBatteryStatsConfig() {
        return null;
    }

    default com.android.internal.os.MonotonicClock getMonotonicClock() {
        return null;
    }

    default android.os.BatteryStats.BatteryStatsDumpHelper getBatteryStatsDumpHelper() {
        return null;
    }

    default void awaitCompletion() {
    }
}
