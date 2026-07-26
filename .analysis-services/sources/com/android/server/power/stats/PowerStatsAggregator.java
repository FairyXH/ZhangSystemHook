package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsAggregator {
    private static final long UNINITIALIZED = -1;
    private final com.android.server.power.stats.AggregatedPowerStatsConfig mAggregatedPowerStatsConfig;
    private final com.android.internal.os.BatteryStatsHistory mHistory;
    private com.android.server.power.stats.AggregatedPowerStats mStats;
    private final android.util.SparseArray<com.android.server.power.stats.PowerStatsProcessor> mProcessors = new android.util.SparseArray<>();
    private int mCurrentBatteryState = 0;
    private int mCurrentScreenState = 1;

    public PowerStatsAggregator(com.android.server.power.stats.AggregatedPowerStatsConfig aggregatedPowerStatsConfig, com.android.internal.os.BatteryStatsHistory history) {
        this.mAggregatedPowerStatsConfig = aggregatedPowerStatsConfig;
        this.mHistory = history;
        for (com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent powerComponentsConfig : aggregatedPowerStatsConfig.getPowerComponentsAggregatedStatsConfigs()) {
            com.android.server.power.stats.PowerStatsProcessor processor = powerComponentsConfig.getProcessor();
            this.mProcessors.put(powerComponentsConfig.getPowerComponentId(), processor);
        }
    }

    com.android.server.power.stats.AggregatedPowerStatsConfig getConfig() {
        return this.mAggregatedPowerStatsConfig;
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x017b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0132 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:123:? A[Catch: all -> 0x0185, SYNTHETIC, TryCatch #2 {, blocks: (B:4:0x0007, B:6:0x000b, B:7:0x0014, B:11:0x0024, B:100:0x0184, B:99:0x0181, B:85:0x0156, B:88:0x015d, B:89:0x016e, B:90:0x0173, B:95:0x017b), top: B:108:0x0007, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00d2 A[Catch: all -> 0x013f, TRY_LEAVE, TryCatch #7 {all -> 0x013f, blocks: (B:59:0x00ce, B:61:0x00d2), top: B:117:0x00ce }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00f8 A[Catch: all -> 0x013a, TryCatch #3 {all -> 0x013a, blocks: (B:63:0x00e0, B:65:0x00f4, B:67:0x00f8, B:71:0x0106, B:72:0x0117, B:73:0x0129), top: B:109:0x00e0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void aggregatePowerStats(long r26, long r28, java.util.function.Consumer<com.android.server.power.stats.AggregatedPowerStats> r30) {
        /*
            Method dump skipped, instruction units count: 392
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.PowerStatsAggregator.aggregatePowerStats(long, long, java.util.function.Consumer):void");
    }

    private void start(com.android.server.power.stats.AggregatedPowerStats stats, long timestampMs) {
        for (int i = 0; i < this.mProcessors.size(); i++) {
            com.android.server.power.stats.PowerComponentAggregatedPowerStats component = stats.getPowerComponentStats(this.mProcessors.keyAt(i));
            if (component != null) {
                this.mProcessors.valueAt(i).start(component, timestampMs);
            }
        }
    }

    private void finish(com.android.server.power.stats.AggregatedPowerStats stats, long timestampMs) {
        for (int i = 0; i < this.mProcessors.size(); i++) {
            com.android.server.power.stats.PowerComponentAggregatedPowerStats component = stats.getPowerComponentStats(this.mProcessors.keyAt(i));
            if (component != null) {
                this.mProcessors.valueAt(i).finish(component, timestampMs);
            }
        }
    }

    public void reset() {
        synchronized (this) {
            this.mStats = null;
        }
    }
}
