package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class AggregatedPowerStatsSection extends com.android.server.power.stats.PowerStatsSpan.Section {
    public static final java.lang.String TYPE = "aggregated-power-stats";
    private final com.android.server.power.stats.AggregatedPowerStats mAggregatedPowerStats;

    AggregatedPowerStatsSection(com.android.server.power.stats.AggregatedPowerStats aggregatedPowerStats) {
        super(TYPE);
        this.mAggregatedPowerStats = aggregatedPowerStats;
    }

    public com.android.server.power.stats.AggregatedPowerStats getAggregatedPowerStats() {
        return this.mAggregatedPowerStats;
    }

    @Override // com.android.server.power.stats.PowerStatsSpan.Section
    void write(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        this.mAggregatedPowerStats.writeXml(serializer);
    }

    @Override // com.android.server.power.stats.PowerStatsSpan.Section
    public void dump(android.util.IndentingPrintWriter ipw) {
        this.mAggregatedPowerStats.dump(ipw);
    }
}
