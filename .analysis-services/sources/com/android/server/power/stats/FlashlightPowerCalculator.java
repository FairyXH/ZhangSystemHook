package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class FlashlightPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final com.android.server.power.stats.UsageBasedPowerEstimator mPowerEstimator;

    public FlashlightPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("camera.flashlight"));
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 6;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        super.calculate(builder, batteryStats, rawRealtimeUs, rawUptimeUs, query);
        long durationMs = batteryStats.getFlashlightOnTime(rawRealtimeUs, 0) / 1000;
        double powerMah = this.mPowerEstimator.calculatePower(durationMs);
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(6, durationMs).setConsumedPower(6, powerMah);
        builder.getAggregateBatteryConsumerBuilder(1).setUsageDurationMillis(6, durationMs).setConsumedPower(6, powerMah);
    }

    @Override // com.android.server.power.stats.PowerCalculator
    protected void calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        long durationMs = this.mPowerEstimator.calculateDuration(u.getFlashlightTurnedOnTimer(), rawRealtimeUs, 0);
        double powerMah = this.mPowerEstimator.calculatePower(durationMs);
        app.setUsageDurationMillis(6, durationMs).setConsumedPower(6, powerMah);
    }
}
