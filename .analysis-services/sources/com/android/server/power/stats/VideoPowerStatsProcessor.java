package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class VideoPowerStatsProcessor extends com.android.server.power.stats.BinaryStatePowerStatsProcessor {
    public VideoPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile, com.android.server.power.stats.PowerStatsUidResolver uidResolver) {
        super(5, uidResolver, powerProfile.getAveragePower(com.android.server.am.IOplusSceneManager.APP_SCENE_VIDEO));
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected int getBinaryState(android.os.BatteryStats.HistoryItem item) {
        if ((item.states2 & 1073741824) != 0) {
            return 1;
        }
        return 0;
    }
}
