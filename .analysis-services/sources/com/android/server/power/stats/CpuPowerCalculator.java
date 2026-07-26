package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CpuPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "CpuPowerCalculator";
    private static final android.os.BatteryConsumer.Key[] UNINITIALIZED_KEYS = new android.os.BatteryConsumer.Key[0];
    private final com.android.server.power.stats.UsageBasedPowerEstimator mCpuActivePowerEstimator;
    private final com.android.internal.os.CpuScalingPolicies mCpuScalingPolicies;
    private final int mNumCpuClusters;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mPerClusterPowerEstimators;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mPerCpuFreqPowerEstimators;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[][] mPerCpuFreqPowerEstimatorsByCluster;

    private static class Result {
        public long[] cpuFreqTimes;
        public long durationFgMs;
        public long durationMs;
        public java.lang.String packageWithHighestDrain;
        public double[] perProcStatePowerMah;
        public double powerMah;

        private Result() {
        }
    }

    public CpuPowerCalculator(com.android.internal.os.CpuScalingPolicies cpuScalingPolicies, com.android.internal.os.PowerProfile profile) {
        this.mCpuScalingPolicies = cpuScalingPolicies;
        int[] policies = this.mCpuScalingPolicies.getPolicies();
        this.mNumCpuClusters = policies.length;
        this.mCpuActivePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("cpu.active"));
        this.mPerClusterPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[policies.length];
        for (int i = 0; i < policies.length; i++) {
            this.mPerClusterPowerEstimators[i] = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePowerForCpuScalingPolicy(policies[i]));
        }
        this.mPerCpuFreqPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[cpuScalingPolicies.getScalingStepCount()];
        this.mPerCpuFreqPowerEstimatorsByCluster = new com.android.server.power.stats.UsageBasedPowerEstimator[this.mNumCpuClusters][];
        int index = 0;
        for (int cluster = 0; cluster < policies.length; cluster++) {
            int policy = policies[cluster];
            int[] freqs = cpuScalingPolicies.getFrequencies(policy);
            this.mPerCpuFreqPowerEstimatorsByCluster[cluster] = new com.android.server.power.stats.UsageBasedPowerEstimator[freqs.length];
            int step = 0;
            while (step < freqs.length) {
                com.android.server.power.stats.UsageBasedPowerEstimator estimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePowerForCpuScalingStep(policy, step));
                this.mPerCpuFreqPowerEstimatorsByCluster[cluster][step] = estimator;
                this.mPerCpuFreqPowerEstimators[index] = estimator;
                step++;
                index++;
            }
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 1;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double totalPowerMah = 0.0d;
        android.os.BatteryConsumer.Key[] keys = UNINITIALIZED_KEYS;
        com.android.server.power.stats.CpuPowerCalculator.Result result = new com.android.server.power.stats.CpuPowerCalculator.Result();
        if (query.isProcessStateDataNeeded()) {
            result.cpuFreqTimes = new long[this.mCpuScalingPolicies.getScalingStepCount()];
        }
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            if (keys == UNINITIALIZED_KEYS) {
                if (query.isProcessStateDataNeeded()) {
                    keys = app.getKeys(1);
                } else {
                    keys = null;
                }
            }
            calculateApp(app, app.getBatteryStatsUid(), query, result, keys);
            if (!app.isVirtualUid()) {
                totalPowerMah += result.powerMah;
            }
        }
        long consumptionUC = batteryStats.getCpuEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUC, query);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(1, totalPowerMah);
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(1, powerModel == 2 ? uCtoMah(consumptionUC) : totalPowerMah, powerModel);
    }

    private void calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, android.os.BatteryUsageStatsQuery query, com.android.server.power.stats.CpuPowerCalculator.Result result, android.os.BatteryConsumer.Key[] keys) {
        long consumptionUC = u.getCpuEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUC, query);
        calculatePowerAndDuration(u, powerModel, consumptionUC, 0, result);
        app.setConsumedPower(1, result.powerMah, powerModel).setUsageDurationMillis(1, result.durationMs).setPackageWithHighestDrain(result.packageWithHighestDrain);
        if (query.isProcessStateDataNeeded() && keys != null) {
            switch (powerModel) {
                case 1:
                    calculateModeledPowerPerProcessState(app, u, keys, result);
                    break;
                case 2:
                    calculateEnergyConsumptionPerProcessState(app, u, keys);
                    break;
            }
        }
    }

    private void calculateEnergyConsumptionPerProcessState(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, android.os.BatteryConsumer.Key[] keys) {
        for (android.os.BatteryConsumer.Key key : keys) {
            if (key.processState != 0) {
                long consumptionUC = u.getCpuEnergyConsumptionUC(key.processState);
                if (consumptionUC != 0) {
                    app.setConsumedPower(key, uCtoMah(consumptionUC), 2);
                }
            }
        }
    }

    private void calculateModeledPowerPerProcessState(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, android.os.BatteryConsumer.Key[] keys, com.android.server.power.stats.CpuPowerCalculator.Result result) {
        if (result.perProcStatePowerMah == null) {
            result.perProcStatePowerMah = new double[5];
        } else {
            java.util.Arrays.fill(result.perProcStatePowerMah, 0.0d);
        }
        for (int uidProcState = 0; uidProcState < 7; uidProcState++) {
            int procState = android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(uidProcState);
            if (procState != 0) {
                boolean hasCpuFreqTimes = u.getCpuFreqTimes(result.cpuFreqTimes, uidProcState);
                if (0 != 0 || hasCpuFreqTimes) {
                    double[] dArr = result.perProcStatePowerMah;
                    dArr[procState] = dArr[procState] + calculateUidModeledPowerMah(u, 0L, null, result.cpuFreqTimes);
                }
            }
        }
        for (android.os.BatteryConsumer.Key key : keys) {
            if (key.processState != 0) {
                long cpuActiveTime = u.getCpuActiveTime(key.processState);
                double powerMah = result.perProcStatePowerMah[key.processState];
                app.setConsumedPower(key, powerMah + this.mCpuActivePowerEstimator.calculatePower(cpuActiveTime), 1).setUsageDurationMillis(key, cpuActiveTime);
            }
        }
    }

    private void calculatePowerAndDuration(android.os.BatteryStats.Uid u, int powerModel, long consumptionUC, int statsType, com.android.server.power.stats.CpuPowerCalculator.Result result) {
        double powerMah;
        long durationFgMs;
        android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Proc> processStats;
        int i = statsType;
        long durationMs = (u.getUserCpuTimeUs(i) + u.getSystemCpuTimeUs(i)) / 1000;
        switch (powerModel) {
            case 2:
                double powerMah2 = uCtoMah(consumptionUC);
                powerMah = powerMah2;
                break;
            default:
                powerMah = calculateUidModeledPowerMah(u, i);
                break;
        }
        double highestDrain = 0.0d;
        java.lang.String packageWithHighestDrain = null;
        long durationFgMs2 = 0;
        android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Proc> processStats2 = u.getProcessStats();
        int processStatsCount = processStats2.size();
        int i2 = 0;
        while (i2 < processStatsCount) {
            android.os.BatteryStats.Uid.Proc ps = (android.os.BatteryStats.Uid.Proc) processStats2.valueAt(i2);
            java.lang.String processName = processStats2.keyAt(i2);
            long durationFgMs3 = durationFgMs2 + ps.getForegroundTime(i);
            long costValue = ps.getUserTime(i) + ps.getSystemTime(i) + ps.getForegroundTime(i);
            if (packageWithHighestDrain != null) {
                durationFgMs = durationFgMs3;
                if (packageWithHighestDrain.startsWith(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER)) {
                    processStats = processStats2;
                } else {
                    processStats = processStats2;
                    if (highestDrain < costValue && !processName.startsWith(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER)) {
                        highestDrain = costValue;
                        packageWithHighestDrain = processName;
                    }
                    i2++;
                    i = statsType;
                    durationFgMs2 = durationFgMs;
                    processStats2 = processStats;
                }
            } else {
                durationFgMs = durationFgMs3;
                processStats = processStats2;
            }
            highestDrain = costValue;
            packageWithHighestDrain = processName;
            i2++;
            i = statsType;
            durationFgMs2 = durationFgMs;
            processStats2 = processStats;
        }
        if (durationFgMs2 > durationMs) {
            durationMs = durationFgMs2;
        }
        result.durationMs = durationMs;
        result.durationFgMs = durationFgMs2;
        result.powerMah = powerMah;
        result.packageWithHighestDrain = packageWithHighestDrain;
    }

    public double calculateUidModeledPowerMah(android.os.BatteryStats.Uid u, int statsType) {
        return calculateUidModeledPowerMah(u, u.getCpuActiveTime(), u.getCpuClusterTimes(), u.getCpuFreqTimes(statsType));
    }

    private double calculateUidModeledPowerMah(android.os.BatteryStats.Uid u, long cpuActiveTime, long[] cpuClusterTimes, long[] cpuFreqTimes) {
        double powerMah = calculateActiveCpuPowerMah(cpuActiveTime);
        if (cpuClusterTimes != null) {
            if (cpuClusterTimes.length != this.mNumCpuClusters) {
                android.util.Log.w(TAG, "UID " + u.getUid() + " CPU cluster # mismatch: Power Profile # " + this.mNumCpuClusters + " actual # " + cpuClusterTimes.length);
            } else {
                for (int cluster = 0; cluster < this.mNumCpuClusters; cluster++) {
                    double power = this.mPerClusterPowerEstimators[cluster].calculatePower(cpuClusterTimes[cluster]);
                    powerMah += power;
                }
            }
        }
        if (cpuFreqTimes != null) {
            if (cpuFreqTimes.length != this.mPerCpuFreqPowerEstimators.length) {
                android.util.Log.w(TAG, "UID " + u.getUid() + " CPU freq # mismatch: Power Profile # " + this.mPerCpuFreqPowerEstimators.length + " actual # " + cpuFreqTimes.length);
            } else {
                for (int i = 0; i < cpuFreqTimes.length; i++) {
                    powerMah += this.mPerCpuFreqPowerEstimators[i].calculatePower(cpuFreqTimes[i]);
                }
            }
        }
        return powerMah;
    }

    private double calculateActiveCpuPowerMah(long durationsMs) {
        return this.mCpuActivePowerEstimator.calculatePower(durationsMs);
    }

    public double calculatePerCpuClusterPowerMah(int cluster, long clusterDurationMs) {
        return this.mPerClusterPowerEstimators[cluster].calculatePower(clusterDurationMs);
    }

    public double calculatePerCpuFreqPowerMah(int cluster, int speedStep, long clusterSpeedDurationsMs) {
        return this.mPerCpuFreqPowerEstimatorsByCluster[cluster][speedStep].calculatePower(clusterSpeedDurationsMs);
    }
}
