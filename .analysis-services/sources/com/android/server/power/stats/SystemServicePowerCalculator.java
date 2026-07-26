package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class SystemServicePowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "SystemServicePowerCalc";
    private final com.android.server.power.stats.CpuPowerCalculator mCpuPowerCalculator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mPowerEstimators;

    public SystemServicePowerCalculator(com.android.internal.os.CpuScalingPolicies cpuScalingPolicies, com.android.internal.os.PowerProfile powerProfile) {
        this.mCpuPowerCalculator = new com.android.server.power.stats.CpuPowerCalculator(cpuScalingPolicies, powerProfile);
        this.mPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[cpuScalingPolicies.getScalingStepCount()];
        int index = 0;
        int[] policies = cpuScalingPolicies.getPolicies();
        for (int policy : policies) {
            int numSpeeds = cpuScalingPolicies.getFrequencies(policy).length;
            int speed = 0;
            while (speed < numSpeeds) {
                this.mPowerEstimators[index] = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePowerForCpuScalingStep(policy, speed));
                speed++;
                index++;
            }
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 7;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double systemServicePowerMah;
        android.os.BatteryStats.Uid systemUid;
        android.os.BatteryStats.Uid systemUid2 = (android.os.BatteryStats.Uid) batteryStats.getUidStats().get(1000);
        if (systemUid2 == null) {
            return;
        }
        long consumptionUC = systemUid2.getCpuEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUC, query);
        if (powerModel == 2) {
            systemServicePowerMah = calculatePowerUsingEnergyConsumption(batteryStats, systemUid2, consumptionUC);
        } else {
            systemServicePowerMah = calculatePowerUsingPowerProfile(batteryStats);
        }
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        android.os.UidBatteryConsumer.Builder systemServerConsumer = uidBatteryConsumerBuilders.get(1000);
        if (systemServerConsumer != null) {
            systemServicePowerMah = java.lang.Math.min(systemServicePowerMah, systemServerConsumer.getTotalPower());
            systemServerConsumer.setConsumedPower(17, -systemServicePowerMah, powerModel);
        }
        int i = uidBatteryConsumerBuilders.size() - 1;
        while (i >= 0) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            if (app == systemServerConsumer) {
                systemUid = systemUid2;
            } else {
                android.os.BatteryStats.Uid uid = app.getBatteryStatsUid();
                systemUid = systemUid2;
                app.setConsumedPower(7, systemServicePowerMah * uid.getProportionalSystemServiceUsage(), powerModel);
            }
            i--;
            systemUid2 = systemUid;
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(7, systemServicePowerMah);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(7, systemServicePowerMah);
    }

    private double calculatePowerUsingEnergyConsumption(android.os.BatteryStats batteryStats, android.os.BatteryStats.Uid systemUid, long consumptionUC) {
        double systemServiceModeledPowerMah = calculatePowerUsingPowerProfile(batteryStats);
        double systemUidModeledPowerMah = this.mCpuPowerCalculator.calculateUidModeledPowerMah(systemUid, 0);
        if (systemUidModeledPowerMah <= 0.0d) {
            return 0.0d;
        }
        return (uCtoMah(consumptionUC) * systemServiceModeledPowerMah) / systemUidModeledPowerMah;
    }

    private double calculatePowerUsingPowerProfile(android.os.BatteryStats batteryStats) {
        long[] systemServiceTimeAtCpuSpeeds = batteryStats.getSystemServiceTimeAtCpuSpeeds();
        if (systemServiceTimeAtCpuSpeeds == null) {
            return 0.0d;
        }
        double powerMah = 0.0d;
        int size = java.lang.Math.min(this.mPowerEstimators.length, systemServiceTimeAtCpuSpeeds.length);
        for (int i = 0; i < size; i++) {
            powerMah += this.mPowerEstimators[i].calculatePower(systemServiceTimeAtCpuSpeeds[i] / 1000);
        }
        return powerMah;
    }
}
