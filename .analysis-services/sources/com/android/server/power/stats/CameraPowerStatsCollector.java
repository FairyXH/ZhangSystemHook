package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CameraPowerStatsCollector extends com.android.server.power.stats.EnergyConsumerPowerStatsCollector {
    CameraPowerStatsCollector(com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector injector) {
        super(injector, 3, android.os.BatteryConsumer.powerComponentIdToString(3), 7, null, new com.android.server.power.stats.BinaryStatePowerStatsLayout());
    }
}
