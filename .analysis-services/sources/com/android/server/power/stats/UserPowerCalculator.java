package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class UserPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return true;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        int[] userIds = query.getUserIds();
        if (com.android.internal.util.ArrayUtils.contains(userIds, -1)) {
            return;
        }
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder uidBuilder = uidBatteryConsumerBuilders.valueAt(i);
            if (!uidBuilder.isVirtualUid()) {
                int uid = uidBuilder.getUid();
                if (android.os.UserHandle.getAppId(uid) >= 10000) {
                    int userId = android.os.UserHandle.getUserId(uid);
                    if (!com.android.internal.util.ArrayUtils.contains(userIds, userId)) {
                        uidBuilder.excludeFromBatteryUsageStats();
                        builder.getOrCreateUserBatteryConsumerBuilder(userId).addUidBatteryConsumer(uidBuilder);
                    }
                }
            }
        }
    }
}
