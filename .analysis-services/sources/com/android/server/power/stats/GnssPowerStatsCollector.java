package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class GnssPowerStatsCollector extends com.android.server.power.stats.EnergyConsumerPowerStatsCollector {
    GnssPowerStatsCollector(com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector injector) {
        super(injector, 10, android.os.BatteryConsumer.powerComponentIdToString(10), 4, null, new com.android.server.power.stats.GnssPowerStatsLayout());
    }
}
