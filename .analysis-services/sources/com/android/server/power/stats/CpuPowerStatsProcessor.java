package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CpuPowerStatsProcessor extends com.android.server.power.stats.PowerStatsProcessor {
    private static final double HOUR_IN_MILLIS = java.util.concurrent.TimeUnit.HOURS.toMillis(1);
    private static final java.lang.String TAG = "CpuPowerStatsProcessor";
    private static final int UNKNOWN = -1;
    private int[][] mCombinedEnergyConsumerToPowerBracketMap;
    private final int mCpuClusterCount;
    private final com.android.internal.os.CpuScalingPolicies mCpuScalingPolicies;
    private final int mCpuScalingStepCount;
    private int[] mEnergyConsumerToCombinedEnergyConsumerMap;
    private com.android.internal.os.PowerStats.Descriptor mLastUsedDescriptor;
    private com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan mPlan;
    private final double mPowerMultiplierForCpuActive;
    private final double[] mPowerMultipliersByCluster;
    private final double[] mPowerMultipliersByScalingStep;
    private final int[] mScalingStepToCluster;
    private com.android.server.power.stats.CpuPowerStatsLayout mStatsLayout;
    private long[] mTmpDeviceStatsArray;
    private long[] mTmpUidStatsArray;

    public CpuPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile, com.android.internal.os.CpuScalingPolicies scalingPolicies) {
        this.mCpuScalingPolicies = scalingPolicies;
        this.mCpuScalingStepCount = scalingPolicies.getScalingStepCount();
        this.mScalingStepToCluster = new int[this.mCpuScalingStepCount];
        this.mPowerMultipliersByScalingStep = new double[this.mCpuScalingStepCount];
        int step = 0;
        int[] policies = scalingPolicies.getPolicies();
        this.mCpuClusterCount = policies.length;
        this.mPowerMultipliersByCluster = new double[this.mCpuClusterCount];
        for (int cluster = 0; cluster < this.mCpuClusterCount; cluster++) {
            int policy = policies[cluster];
            this.mPowerMultipliersByCluster[cluster] = powerProfile.getAveragePowerForCpuScalingPolicy(policy) / HOUR_IN_MILLIS;
            int[] frequencies = scalingPolicies.getFrequencies(policy);
            for (int i = 0; i < frequencies.length; i++) {
                this.mScalingStepToCluster[step] = cluster;
                this.mPowerMultipliersByScalingStep[step] = powerProfile.getAveragePowerForCpuScalingStep(policy, i) / HOUR_IN_MILLIS;
                step++;
            }
        }
        this.mPowerMultiplierForCpuActive = powerProfile.getAveragePower("cpu.active") / HOUR_IN_MILLIS;
    }

    private void unpackPowerStatsDescriptor(com.android.internal.os.PowerStats.Descriptor descriptor) {
        if (descriptor.equals(this.mLastUsedDescriptor)) {
            return;
        }
        this.mLastUsedDescriptor = descriptor;
        this.mStatsLayout = new com.android.server.power.stats.CpuPowerStatsLayout();
        this.mStatsLayout.fromExtras(descriptor.extras);
        this.mTmpDeviceStatsArray = new long[descriptor.statsArrayLength];
        this.mTmpUidStatsArray = new long[descriptor.uidStatsArrayLength];
    }

    private static final class Intermediates {
        public long cumulativeTime;
        public long[] cumulativeTimeByCluster;
        public double[] powerByCluster;
        public long[] powerByEnergyConsumer;
        public double[] powerByScalingStep;
        public long[] timeByCluster;
        public long[] timeByScalingStep;
        public long uptime;

        private Intermediates() {
        }
    }

    private static class DeviceStatsIntermediates {
        public double power;
        public double[] powerByBracket;
        public long[] timeByBracket;

        private DeviceStatsIntermediates() {
        }
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    public void finish(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
        if (stats.getPowerStatsDescriptor() == null) {
            return;
        }
        unpackPowerStatsDescriptor(stats.getPowerStatsDescriptor());
        if (this.mPlan == null) {
            this.mPlan = new com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan(stats.getConfig());
            if (this.mStatsLayout.getEnergyConsumerCount() != 0) {
                initEnergyConsumerToPowerBracketMaps();
            }
        }
        com.android.server.power.stats.CpuPowerStatsProcessor.Intermediates intermediates = new com.android.server.power.stats.CpuPowerStatsProcessor.Intermediates();
        int cpuScalingStepCount = this.mStatsLayout.getCpuScalingStepCount();
        if (cpuScalingStepCount != this.mCpuScalingStepCount) {
            android.util.Log.e(TAG, "Mismatched CPU scaling step count in PowerStats: " + cpuScalingStepCount + ", expected: " + this.mCpuScalingStepCount);
            return;
        }
        int clusterCount = this.mStatsLayout.getCpuClusterCount();
        if (clusterCount != this.mCpuClusterCount) {
            android.util.Log.e(TAG, "Mismatched CPU cluster count in PowerStats: " + clusterCount + ", expected: " + this.mCpuClusterCount);
            return;
        }
        computeTotals(stats, intermediates);
        if (intermediates.cumulativeTime == 0) {
            return;
        }
        estimatePowerByScalingStep(intermediates);
        estimatePowerByDeviceState(stats, intermediates);
        combineDeviceStateEstimates();
        java.util.ArrayList<java.lang.Integer> uids = new java.util.ArrayList<>();
        stats.collectUids(uids);
        if (!uids.isEmpty()) {
            java.util.Iterator<java.lang.Integer> it = uids.iterator();
            while (it.hasNext()) {
                int uid = it.next().intValue();
                for (int i = 0; i < this.mPlan.uidStateEstimates.size(); i++) {
                    estimateUidPowerConsumption(stats, uid, this.mPlan.uidStateEstimates.get(i));
                }
            }
        }
        this.mPlan.resetIntermediates();
    }

    private void initEnergyConsumerToPowerBracketMaps() {
        int energyConsumerCount = this.mStatsLayout.getEnergyConsumerCount();
        int powerBracketCount = this.mStatsLayout.getCpuPowerBracketCount();
        this.mEnergyConsumerToCombinedEnergyConsumerMap = new int[energyConsumerCount];
        this.mCombinedEnergyConsumerToPowerBracketMap = new int[energyConsumerCount][];
        int[] policies = this.mCpuScalingPolicies.getPolicies();
        if (energyConsumerCount == policies.length) {
            int[] scalingStepToPowerBracketMap = this.mStatsLayout.getScalingStepToPowerBracketMap();
            android.util.ArraySet<? extends java.lang.Integer>[] clusterToBrackets = new android.util.ArraySet[policies.length];
            int step = 0;
            for (int cluster = 0; cluster < policies.length; cluster++) {
                int[] freqs = this.mCpuScalingPolicies.getFrequencies(policies[cluster]);
                clusterToBrackets[cluster] = new android.util.ArraySet<>(freqs.length);
                int j = 0;
                while (j < freqs.length) {
                    clusterToBrackets[cluster].add(java.lang.Integer.valueOf(scalingStepToPowerBracketMap[step]));
                    j++;
                    step++;
                }
            }
            int cluster2 = policies.length;
            android.util.ArraySet<java.lang.Integer>[] combinedEnergyConsumers = new android.util.ArraySet[cluster2];
            int combinedEnergyConsumersCount = 0;
            for (int cluster3 = 0; cluster3 < clusterToBrackets.length; cluster3++) {
                int combineWith = -1;
                int i = 0;
                while (true) {
                    if (i >= combinedEnergyConsumersCount) {
                        break;
                    }
                    if (!containsAny(combinedEnergyConsumers[i], clusterToBrackets[cluster3])) {
                        i++;
                    } else {
                        combineWith = i;
                        break;
                    }
                }
                if (combineWith != -1) {
                    this.mEnergyConsumerToCombinedEnergyConsumerMap[cluster3] = combineWith;
                    combinedEnergyConsumers[combineWith].addAll(clusterToBrackets[cluster3]);
                } else {
                    this.mEnergyConsumerToCombinedEnergyConsumerMap[cluster3] = combinedEnergyConsumersCount;
                    combinedEnergyConsumers[combinedEnergyConsumersCount] = clusterToBrackets[cluster3];
                    combinedEnergyConsumersCount++;
                }
            }
            int cluster4 = combinedEnergyConsumers.length;
            for (int i2 = cluster4 - 1; i2 >= 0; i2--) {
                this.mCombinedEnergyConsumerToPowerBracketMap[i2] = new int[combinedEnergyConsumers[i2].size()];
                for (int j2 = combinedEnergyConsumers[i2].size() - 1; j2 >= 0; j2--) {
                    this.mCombinedEnergyConsumerToPowerBracketMap[i2][j2] = combinedEnergyConsumers[i2].valueAt(j2).intValue();
                }
            }
            return;
        }
        int[] map = new int[powerBracketCount];
        for (int i3 = 0; i3 < map.length; i3++) {
            map[i3] = i3;
        }
        this.mCombinedEnergyConsumerToPowerBracketMap[0] = map;
    }

    private static boolean containsAny(android.util.ArraySet<java.lang.Integer> set1, android.util.ArraySet<java.lang.Integer> set2) {
        for (int i = 0; i < set2.size(); i++) {
            if (set1.contains(set2.valueAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void computeTotals(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, com.android.server.power.stats.CpuPowerStatsProcessor.Intermediates intermediates) {
        intermediates.timeByScalingStep = new long[this.mCpuScalingStepCount];
        intermediates.timeByCluster = new long[this.mCpuClusterCount];
        intermediates.cumulativeTimeByCluster = new long[this.mCpuClusterCount];
        java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = this.mPlan.deviceStateEstimations;
        for (int i = deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation deviceStateEstimation = deviceStateEstimations.get(i);
            if (stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStateEstimation.stateValues)) {
                intermediates.uptime += this.mStatsLayout.getUsageDuration(this.mTmpDeviceStatsArray);
                for (int cluster = 0; cluster < this.mCpuClusterCount; cluster++) {
                    long[] jArr = intermediates.timeByCluster;
                    jArr[cluster] = jArr[cluster] + this.mStatsLayout.getTimeByCluster(this.mTmpDeviceStatsArray, cluster);
                }
                for (int step = 0; step < this.mCpuScalingStepCount; step++) {
                    long timeInStep = this.mStatsLayout.getTimeByScalingStep(this.mTmpDeviceStatsArray, step);
                    intermediates.cumulativeTime += timeInStep;
                    long[] jArr2 = intermediates.timeByScalingStep;
                    jArr2[step] = jArr2[step] + timeInStep;
                    long[] jArr3 = intermediates.cumulativeTimeByCluster;
                    int i2 = this.mScalingStepToCluster[step];
                    jArr3[i2] = jArr3[i2] + timeInStep;
                }
            }
        }
    }

    private void estimatePowerByScalingStep(com.android.server.power.stats.CpuPowerStatsProcessor.Intermediates intermediates) {
        double cpuActivePower = this.mPowerMultiplierForCpuActive * intermediates.uptime;
        intermediates.powerByCluster = new double[this.mCpuClusterCount];
        for (int cluster = 0; cluster < this.mCpuClusterCount; cluster++) {
            intermediates.powerByCluster[cluster] = this.mPowerMultipliersByCluster[cluster] * intermediates.timeByCluster[cluster];
        }
        int cluster2 = this.mCpuScalingStepCount;
        intermediates.powerByScalingStep = new double[cluster2];
        for (int step = 0; step < this.mCpuScalingStepCount; step++) {
            int cluster3 = this.mScalingStepToCluster[step];
            double power = (intermediates.timeByScalingStep[step] * cpuActivePower) / intermediates.cumulativeTime;
            long cumulativeTimeInCluster = intermediates.cumulativeTimeByCluster[cluster3];
            if (cumulativeTimeInCluster != 0) {
                power += (intermediates.powerByCluster[cluster3] * intermediates.timeByScalingStep[step]) / cumulativeTimeInCluster;
            }
            intermediates.powerByScalingStep[step] = power + (this.mPowerMultipliersByScalingStep[step] * intermediates.timeByScalingStep[step]);
        }
    }

    private void estimatePowerByDeviceState(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, com.android.server.power.stats.CpuPowerStatsProcessor.Intermediates intermediates) {
        int cpuScalingStepCount;
        int powerBracketCount;
        int cpuScalingStepCount2 = this.mStatsLayout.getCpuScalingStepCount();
        int powerBracketCount2 = this.mStatsLayout.getCpuPowerBracketCount();
        int[] scalingStepToBracketMap = this.mStatsLayout.getScalingStepToPowerBracketMap();
        int energyConsumerCount = this.mStatsLayout.getEnergyConsumerCount();
        java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = this.mPlan.deviceStateEstimations;
        int dse = deviceStateEstimations.size() - 1;
        while (dse >= 0) {
            com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation deviceStateEstimation = deviceStateEstimations.get(dse);
            deviceStateEstimation.intermediates = new com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates();
            com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates deviceStatsIntermediates = (com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates) deviceStateEstimation.intermediates;
            deviceStatsIntermediates.timeByBracket = new long[powerBracketCount2];
            deviceStatsIntermediates.powerByBracket = new double[powerBracketCount2];
            stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStateEstimation.stateValues);
            int step = 0;
            while (step < cpuScalingStepCount2) {
                if (intermediates.timeByScalingStep[step] != 0) {
                    long timeInStep = this.mStatsLayout.getTimeByScalingStep(this.mTmpDeviceStatsArray, step);
                    cpuScalingStepCount = cpuScalingStepCount2;
                    powerBracketCount = powerBracketCount2;
                    double stepPower = (intermediates.powerByScalingStep[step] * timeInStep) / intermediates.timeByScalingStep[step];
                    int bracket = scalingStepToBracketMap[step];
                    long[] jArr = deviceStatsIntermediates.timeByBracket;
                    jArr[bracket] = jArr[bracket] + timeInStep;
                    double[] dArr = deviceStatsIntermediates.powerByBracket;
                    dArr[bracket] = dArr[bracket] + stepPower;
                } else {
                    cpuScalingStepCount = cpuScalingStepCount2;
                    powerBracketCount = powerBracketCount2;
                }
                step++;
                cpuScalingStepCount2 = cpuScalingStepCount;
                powerBracketCount2 = powerBracketCount;
            }
            int cpuScalingStepCount3 = cpuScalingStepCount2;
            int powerBracketCount3 = powerBracketCount2;
            if (energyConsumerCount != 0) {
                adjustEstimatesUsingEnergyConsumers(intermediates, deviceStatsIntermediates);
            }
            double power = 0.0d;
            for (int i = deviceStatsIntermediates.powerByBracket.length - 1; i >= 0; i--) {
                power += deviceStatsIntermediates.powerByBracket[i];
            }
            deviceStatsIntermediates.power = power;
            this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, power);
            stats.setDeviceStats(deviceStateEstimation.stateValues, this.mTmpDeviceStatsArray);
            dse--;
            cpuScalingStepCount2 = cpuScalingStepCount3;
            powerBracketCount2 = powerBracketCount3;
        }
    }

    private void adjustEstimatesUsingEnergyConsumers(com.android.server.power.stats.CpuPowerStatsProcessor.Intermediates intermediates, com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates deviceStatsIntermediates) {
        int energyConsumerCount = this.mStatsLayout.getEnergyConsumerCount();
        if (energyConsumerCount == 0) {
            return;
        }
        if (intermediates.powerByEnergyConsumer == null) {
            intermediates.powerByEnergyConsumer = new long[energyConsumerCount];
        } else {
            java.util.Arrays.fill(intermediates.powerByEnergyConsumer, 0L);
        }
        for (int i = 0; i < energyConsumerCount; i++) {
            long[] jArr = intermediates.powerByEnergyConsumer;
            int i2 = this.mEnergyConsumerToCombinedEnergyConsumerMap[i];
            jArr[i2] = jArr[i2] + this.mStatsLayout.getConsumedEnergy(this.mTmpDeviceStatsArray, i);
        }
        for (int combinedConsumer = this.mCombinedEnergyConsumerToPowerBracketMap.length - 1; combinedConsumer >= 0; combinedConsumer--) {
            int[] combinedEnergyConsumerToPowerBracketMap = this.mCombinedEnergyConsumerToPowerBracketMap[combinedConsumer];
            if (combinedEnergyConsumerToPowerBracketMap != null) {
                double consumedEnergy = uCtoMah(intermediates.powerByEnergyConsumer[combinedConsumer]);
                double totalModeledPower = 0.0d;
                for (int i3 : combinedEnergyConsumerToPowerBracketMap) {
                    totalModeledPower += deviceStatsIntermediates.powerByBracket[i3];
                }
                if (totalModeledPower != 0.0d) {
                    for (int bracket : combinedEnergyConsumerToPowerBracketMap) {
                        deviceStatsIntermediates.powerByBracket[bracket] = (deviceStatsIntermediates.powerByBracket[bracket] * consumedEnergy) / totalModeledPower;
                    }
                }
            }
        }
    }

    private void combineDeviceStateEstimates() {
        for (int i = this.mPlan.combinedDeviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse = this.mPlan.combinedDeviceStateEstimations.get(i);
            com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates cdseIntermediates = new com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates();
            cdse.intermediates = cdseIntermediates;
            int bracketCount = this.mStatsLayout.getCpuPowerBracketCount();
            cdseIntermediates.timeByBracket = new long[bracketCount];
            cdseIntermediates.powerByBracket = new double[bracketCount];
            java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = cdse.deviceStateEstimations;
            for (int j = deviceStateEstimations.size() - 1; j >= 0; j--) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation dse = deviceStateEstimations.get(j);
                com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates intermediates = (com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates) dse.intermediates;
                if (intermediates != null) {
                    cdseIntermediates.power += intermediates.power;
                    for (int k = 0; k < bracketCount; k++) {
                        long[] jArr = cdseIntermediates.timeByBracket;
                        jArr[k] = jArr[k] + intermediates.timeByBracket[k];
                        double[] dArr = cdseIntermediates.powerByBracket;
                        dArr[k] = dArr[k] + intermediates.powerByBracket[k];
                    }
                } else {
                    android.util.Log.e(TAG, "intermediates is null");
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x004e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void estimateUidPowerConsumption(com.android.server.power.stats.PowerComponentAggregatedPowerStats r17, int r18, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate r19) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r19
            com.android.server.power.stats.PowerStatsProcessor$CombinedDeviceStateEstimate r4 = r3.combinedDeviceStateEstimate
            java.lang.Object r5 = r4.intermediates
            com.android.server.power.stats.CpuPowerStatsProcessor$DeviceStatsIntermediates r5 = (com.android.server.power.stats.CpuPowerStatsProcessor.DeviceStatsIntermediates) r5
            r6 = 0
        Lf:
            java.util.List<com.android.server.power.stats.PowerStatsProcessor$UidStateProportionalEstimate> r7 = r3.proportionalEstimates
            int r7 = r7.size()
            if (r6 >= r7) goto L79
            java.util.List<com.android.server.power.stats.PowerStatsProcessor$UidStateProportionalEstimate> r7 = r3.proportionalEstimates
            java.lang.Object r7 = r7.get(r6)
            com.android.server.power.stats.PowerStatsProcessor$UidStateProportionalEstimate r7 = (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate) r7
            long[] r8 = r0.mTmpUidStatsArray
            int[] r9 = r7.stateValues
            boolean r8 = r1.getUidStats(r8, r2, r9)
            if (r8 != 0) goto L2b
            r15 = r4
            goto L73
        L2b:
            r8 = 0
            r10 = 0
        L2e:
            com.android.server.power.stats.CpuPowerStatsLayout r11 = r0.mStatsLayout
            int r11 = r11.getCpuPowerBracketCount()
            if (r10 >= r11) goto L64
            long[] r11 = r5.timeByBracket
            r11 = r11[r10]
            r13 = 0
            int r11 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r11 != 0) goto L41
            goto L4e
        L41:
            com.android.server.power.stats.CpuPowerStatsLayout r11 = r0.mStatsLayout
            long[] r12 = r0.mTmpUidStatsArray
            long r11 = r11.getUidTimeByPowerBracket(r12, r10)
            int r13 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r13 != 0) goto L50
        L4e:
            r15 = r4
            goto L5e
        L50:
            double[] r13 = r5.powerByBracket
            r13 = r13[r10]
            r15 = r4
            double r3 = (double) r11
            double r13 = r13 * r3
            long[] r3 = r5.timeByBracket
            r3 = r3[r10]
            double r3 = (double) r3
            double r13 = r13 / r3
            double r8 = r8 + r13
        L5e:
            int r10 = r10 + 1
            r3 = r19
            r4 = r15
            goto L2e
        L64:
            r15 = r4
            com.android.server.power.stats.CpuPowerStatsLayout r3 = r0.mStatsLayout
            long[] r4 = r0.mTmpUidStatsArray
            r3.setUidPowerEstimate(r4, r8)
            int[] r3 = r7.stateValues
            long[] r4 = r0.mTmpUidStatsArray
            r1.setUidStats(r2, r3, r4)
        L73:
            int r6 = r6 + 1
            r3 = r19
            r4 = r15
            goto Lf
        L79:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.CpuPowerStatsProcessor.estimateUidPowerConsumption(com.android.server.power.stats.PowerComponentAggregatedPowerStats, int, com.android.server.power.stats.PowerStatsProcessor$UidStateEstimate):void");
    }
}
