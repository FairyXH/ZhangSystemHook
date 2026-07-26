package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class VideoPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final com.android.server.power.stats.UsageBasedPowerEstimator mPowerEstimator;

    private static class PowerAndDuration {
        public long durationMs;
        public double powerMah;

        private PowerAndDuration() {
        }
    }

    public VideoPowerCalculator(com.android.internal.os.PowerProfile powerProfile) {
        this.mPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower(com.android.server.am.IOplusSceneManager.APP_SCENE_VIDEO));
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 5;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        com.android.server.power.stats.VideoPowerCalculator.PowerAndDuration total = new com.android.server.power.stats.VideoPowerCalculator.PowerAndDuration();
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            calculateApp(app, total, app.getBatteryStatsUid(), rawRealtimeUs);
        }
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(5, total.durationMs).setConsumedPower(5, total.powerMah);
        builder.getAggregateBatteryConsumerBuilder(1).setUsageDurationMillis(5, total.durationMs).setConsumedPower(5, total.powerMah);
    }

    private void calculateApp(android.os.UidBatteryConsumer.Builder app, com.android.server.power.stats.VideoPowerCalculator.PowerAndDuration total, android.os.BatteryStats.Uid u, long rawRealtimeUs) {
        long durationMs = this.mPowerEstimator.calculateDuration(u.getVideoTurnedOnTimer(), rawRealtimeUs, 0);
        double powerMah = this.mPowerEstimator.calculatePower(durationMs);
        app.setUsageDurationMillis(5, durationMs).setConsumedPower(5, powerMah);
        if (!app.isVirtualUid()) {
            total.durationMs += durationMs;
            total.powerMah += powerMah;
        }
    }
}
