package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class BatteryUsageStatsSection extends com.android.server.power.stats.PowerStatsSpan.Section {
    public static final java.lang.String TYPE = "battery-usage-stats";
    private final android.os.BatteryUsageStats mBatteryUsageStats;

    BatteryUsageStatsSection(android.os.BatteryUsageStats batteryUsageStats) {
        super(TYPE);
        this.mBatteryUsageStats = batteryUsageStats;
    }

    public android.os.BatteryUsageStats getBatteryUsageStats() {
        return this.mBatteryUsageStats;
    }

    @Override // com.android.server.power.stats.PowerStatsSpan.Section
    void write(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        this.mBatteryUsageStats.writeXml(serializer);
    }

    @Override // com.android.server.power.stats.PowerStatsSpan.Section
    public void dump(android.util.IndentingPrintWriter ipw) {
        this.mBatteryUsageStats.dump(ipw, "");
    }
}
