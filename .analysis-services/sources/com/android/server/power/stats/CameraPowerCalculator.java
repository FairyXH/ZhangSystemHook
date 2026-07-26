package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CameraPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final com.android.server.power.stats.UsageBasedPowerEstimator mPowerEstimator;

    public CameraPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("camera.avg"));
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 3;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double powerMah;
        super.calculate(builder, batteryStats, rawRealtimeUs, rawUptimeUs, query);
        long consumptionUc = batteryStats.getCameraEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUc, query);
        long durationMs = batteryStats.getCameraOnTime(rawRealtimeUs, 0) / 1000;
        if (powerModel == 2) {
            powerMah = uCtoMah(consumptionUc);
        } else {
            powerMah = this.mPowerEstimator.calculatePower(durationMs);
        }
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(3, durationMs).setConsumedPower(3, powerMah, powerModel);
        builder.getAggregateBatteryConsumerBuilder(1).setUsageDurationMillis(3, durationMs).setConsumedPower(3, powerMah, powerModel);
    }

    @Override // com.android.server.power.stats.PowerCalculator
    protected void calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double powerMah;
        long consumptionUc = app.getBatteryStatsUid().getCameraEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUc, query);
        long durationMs = this.mPowerEstimator.calculateDuration(u.getCameraTurnedOnTimer(), rawRealtimeUs, 0);
        if (powerModel == 2) {
            powerMah = uCtoMah(consumptionUc);
        } else {
            powerMah = this.mPowerEstimator.calculatePower(durationMs);
        }
        app.setUsageDurationMillis(3, durationMs).setConsumedPower(3, powerMah, powerModel);
    }
}
