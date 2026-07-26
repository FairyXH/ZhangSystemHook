package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class FlashlightPowerStatsProcessor extends com.android.server.power.stats.BinaryStatePowerStatsProcessor {
    public FlashlightPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile, com.android.server.power.stats.PowerStatsUidResolver uidResolver) {
        super(6, uidResolver, powerProfile.getAveragePower("camera.flashlight"));
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected int getBinaryState(android.os.BatteryStats.HistoryItem item) {
        if ((item.states2 & 134217728) != 0) {
            return 1;
        }
        return 0;
    }
}
