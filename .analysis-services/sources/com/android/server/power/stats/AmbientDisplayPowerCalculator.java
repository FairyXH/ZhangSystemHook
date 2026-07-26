package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class AmbientDisplayPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mPowerEstimators;

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 15;
    }

    public AmbientDisplayPowerCalculator(com.android.internal.os.PowerProfile powerProfile) {
        int numDisplays = powerProfile.getNumDisplays();
        this.mPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[numDisplays];
        for (int display = 0; display < numDisplays; display++) {
            this.mPowerEstimators[display] = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePowerForOrdinal("ambient.on.display", display));
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        long energyConsumerUC = batteryStats.getScreenDozeEnergyConsumptionUC();
        int powerModel = getPowerModel(energyConsumerUC, query);
        long durationMs = calculateDuration(batteryStats, rawRealtimeUs, 0);
        double powerMah = calculateTotalPower(powerModel, batteryStats, rawRealtimeUs, energyConsumerUC);
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(15, durationMs).setConsumedPower(15, powerMah, powerModel);
    }

    private long calculateDuration(android.os.BatteryStats batteryStats, long rawRealtimeUs, int statsType) {
        return batteryStats.getScreenDozeTime(rawRealtimeUs, statsType) / 1000;
    }

    private double calculateTotalPower(int powerModel, android.os.BatteryStats batteryStats, long rawRealtimeUs, long consumptionUC) {
        switch (powerModel) {
            case 2:
                return uCtoMah(consumptionUC);
            default:
                return calculateEstimatedPower(batteryStats, rawRealtimeUs);
        }
    }

    private double calculateEstimatedPower(android.os.BatteryStats batteryStats, long rawRealtimeUs) {
        int numDisplays = this.mPowerEstimators.length;
        double power = 0.0d;
        for (int display = 0; display < numDisplays; display++) {
            long dozeTime = batteryStats.getDisplayScreenDozeTime(display, rawRealtimeUs) / 1000;
            power += this.mPowerEstimators[display].calculatePower(dozeTime);
        }
        return power;
    }
}
