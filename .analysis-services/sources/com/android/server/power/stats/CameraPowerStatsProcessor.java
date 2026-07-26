package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CameraPowerStatsProcessor extends com.android.server.power.stats.BinaryStatePowerStatsProcessor {
    public CameraPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile, com.android.server.power.stats.PowerStatsUidResolver uidResolver) {
        super(3, uidResolver, powerProfile.getAveragePower("camera.avg"));
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected int getBinaryState(android.os.BatteryStats.HistoryItem item) {
        if ((item.states2 & 2097152) != 0) {
            return 1;
        }
        return 0;
    }
}
