package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BatteryStatsDumpHelperImpl implements android.os.BatteryStats.BatteryStatsDumpHelper {
    private final com.android.server.power.stats.BatteryUsageStatsProvider mBatteryUsageStatsProvider;

    public BatteryStatsDumpHelperImpl(com.android.server.power.stats.BatteryUsageStatsProvider batteryUsageStatsProvider) {
        this.mBatteryUsageStatsProvider = batteryUsageStatsProvider;
    }

    public android.os.BatteryUsageStats getBatteryUsageStats(android.os.BatteryStats batteryStats, boolean detailed) {
        android.os.BatteryUsageStatsQuery.Builder builder = new android.os.BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0L);
        if (detailed) {
            builder.includePowerModels().includeProcessStateData().includeVirtualUids();
        }
        return this.mBatteryUsageStatsProvider.getBatteryUsageStats((com.android.server.power.stats.BatteryStatsImpl) batteryStats, builder.build());
    }
}
