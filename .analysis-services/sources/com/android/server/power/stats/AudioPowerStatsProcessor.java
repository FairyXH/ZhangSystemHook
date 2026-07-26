package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class AudioPowerStatsProcessor extends com.android.server.power.stats.BinaryStatePowerStatsProcessor {
    public AudioPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile, com.android.server.power.stats.PowerStatsUidResolver uidResolver) {
        super(4, uidResolver, powerProfile.getAveragePower("audio"));
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected int getBinaryState(android.os.BatteryStats.HistoryItem item) {
        if ((item.states & 4194304) != 0) {
            return 1;
        }
        return 0;
    }
}
