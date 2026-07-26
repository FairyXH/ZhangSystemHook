package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PhonePowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final com.android.server.power.stats.UsageBasedPowerEstimator mPowerEstimator;

    public PhonePowerCalculator(com.android.internal.os.PowerProfile powerProfile) {
        this.mPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("radio.active"));
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 14;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double phoneOnPower;
        long energyConsumerUC = batteryStats.getPhoneEnergyConsumptionUC();
        int powerModel = getPowerModel(energyConsumerUC, query);
        long phoneOnTimeMs = batteryStats.getPhoneOnTime(rawRealtimeUs, 0) / 1000;
        switch (powerModel) {
            case 2:
                double phoneOnPower2 = uCtoMah(energyConsumerUC);
                phoneOnPower = phoneOnPower2;
                break;
            default:
                phoneOnPower = this.mPowerEstimator.calculatePower(phoneOnTimeMs);
                break;
        }
        if (phoneOnPower == 0.0d) {
            return;
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(14, phoneOnPower, powerModel).setUsageDurationMillis(14, phoneOnTimeMs);
    }
}
