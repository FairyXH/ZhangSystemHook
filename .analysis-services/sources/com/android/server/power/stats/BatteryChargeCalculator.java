package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BatteryChargeCalculator extends com.android.server.power.stats.PowerCalculator {
    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return true;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        builder.setDischargePercentage(batteryStats.getDischargeAmount(0));
        int batteryCapacityMah = batteryStats.getLearnedBatteryCapacity() / 1000;
        if (batteryCapacityMah <= 0 && (batteryCapacityMah = batteryStats.getMinLearnedBatteryCapacity() / 1000) <= 0) {
            batteryCapacityMah = batteryStats.getEstimatedBatteryCapacity();
        }
        builder.setBatteryCapacity(batteryCapacityMah);
        double dischargedPowerLowerBoundMah = ((double) (batteryStats.getLowDischargeAmountSinceCharge() * batteryCapacityMah)) / 100.0d;
        double dischargedPowerUpperBoundMah = ((double) (batteryStats.getHighDischargeAmountSinceCharge() * batteryCapacityMah)) / 100.0d;
        builder.setDischargePercentage(batteryStats.getDischargeAmount(0)).setDischargedPowerRange(dischargedPowerLowerBoundMah, dischargedPowerUpperBoundMah).setDischargeDurationMs(batteryStats.getBatteryRealtime(rawRealtimeUs) / 1000);
        long batteryTimeRemainingMs = batteryStats.computeBatteryTimeRemaining(rawRealtimeUs);
        if (batteryTimeRemainingMs != -1) {
            builder.setBatteryTimeRemainingMs(batteryTimeRemainingMs / 1000);
        }
        long chargeTimeRemainingMs = batteryStats.computeChargeTimeRemaining(rawRealtimeUs);
        if (chargeTimeRemainingMs != -1) {
            builder.setChargeTimeRemainingMs(chargeTimeRemainingMs / 1000);
        }
        long dischargeMah = batteryStats.getUahDischarge(0) / 1000;
        if (dischargeMah == 0) {
            dischargeMah = (long) (((dischargedPowerLowerBoundMah + dischargedPowerUpperBoundMah) / 2.0d) + 0.5d);
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(dischargeMah);
    }
}
