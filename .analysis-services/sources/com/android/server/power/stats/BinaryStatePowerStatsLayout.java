package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class BinaryStatePowerStatsLayout extends com.android.server.power.stats.PowerStatsLayout {
    BinaryStatePowerStatsLayout() {
        addDeviceSectionUsageDuration();
        addDeviceSectionEnergyConsumers(1);
        addDeviceSectionPowerEstimate();
        addUidSectionUsageDuration();
        addUidSectionPowerEstimate();
    }
}
