package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class GnssPowerStatsProcessor extends com.android.server.power.stats.BinaryStatePowerStatsProcessor {
    private static final com.android.server.power.stats.GnssPowerStatsLayout sStatsLayout = new com.android.server.power.stats.GnssPowerStatsLayout();
    private final long[] mGnssSignalDurations;
    private int mGnssSignalLevel;
    private long mGnssSignalLevelTimestamp;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mSignalLevelEstimators;
    private long[] mTmpDeviceStatsArray;
    private final boolean mUseSignalLevelEstimators;

    public GnssPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile, com.android.server.power.stats.PowerStatsUidResolver uidResolver) {
        super(10, uidResolver, powerProfile.getAveragePower("gps.on"), sStatsLayout);
        this.mGnssSignalLevel = -1;
        this.mGnssSignalDurations = new long[2];
        this.mSignalLevelEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[2];
        boolean useSignalLevelEstimators = false;
        for (int level = 0; level < 2; level++) {
            double power = powerProfile.getAveragePower("gps.signalqualitybased", level);
            if (power != 0.0d) {
                useSignalLevelEstimators = true;
            }
            this.mSignalLevelEstimators[level] = new com.android.server.power.stats.UsageBasedPowerEstimator(power);
        }
        this.mUseSignalLevelEstimators = useSignalLevelEstimators;
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected int getBinaryState(android.os.BatteryStats.HistoryItem item) {
        if ((item.states & 536870912) == 0) {
            this.mGnssSignalLevel = -1;
            return 0;
        }
        noteGnssSignalLevel(item);
        return 1;
    }

    private void noteGnssSignalLevel(android.os.BatteryStats.HistoryItem item) {
        int signalLevel = (item.states2 & com.android.internal.util.FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT) >> 7;
        if (signalLevel >= 2) {
            signalLevel = -1;
        }
        if (signalLevel == this.mGnssSignalLevel) {
            return;
        }
        if (this.mGnssSignalLevel != -1) {
            long[] jArr = this.mGnssSignalDurations;
            int i = this.mGnssSignalLevel;
            jArr[i] = jArr[i] + (item.time - this.mGnssSignalLevelTimestamp);
        }
        this.mGnssSignalLevel = signalLevel;
        this.mGnssSignalLevelTimestamp = item.time;
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected void recordUsageDuration(com.android.internal.os.PowerStats powerStats, int uid, long time) {
        super.recordUsageDuration(powerStats, uid, time);
        if (this.mGnssSignalLevel != -1) {
            long[] jArr = this.mGnssSignalDurations;
            int i = this.mGnssSignalLevel;
            jArr[i] = jArr[i] + (time - this.mGnssSignalLevelTimestamp);
        } else if (this.mUseSignalLevelEstimators) {
            long[] jArr2 = this.mGnssSignalDurations;
            jArr2[1] = jArr2[1] + (time - this.mGnssSignalLevelTimestamp);
        }
        for (int level = 0; level < 2; level++) {
            long duration = this.mGnssSignalDurations[level];
            sStatsLayout.setDeviceSignalLevelTime(powerStats.stats, level, duration);
            if (uid != -1) {
                long[] uidStats = (long[]) powerStats.uidStats.get(uid);
                if (uidStats == null) {
                    long[] uidStats2 = new long[powerStats.descriptor.uidStatsArrayLength];
                    powerStats.uidStats.put(uid, uidStats2);
                    sStatsLayout.setUidSignalLevelTime(uidStats2, level, duration);
                } else {
                    sStatsLayout.setUidSignalLevelTime(uidStats, level, sStatsLayout.getUidSignalLevelTime(uidStats, level) + duration);
                }
            }
        }
        this.mGnssSignalLevelTimestamp = time;
        java.util.Arrays.fill(this.mGnssSignalDurations, 0L);
    }

    @Override // com.android.server.power.stats.BinaryStatePowerStatsProcessor
    protected void computeDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan plan, boolean energyConsumerSupported) {
        if (!this.mUseSignalLevelEstimators || energyConsumerSupported) {
            super.computeDevicePowerEstimates(stats, plan, energyConsumerSupported);
            return;
        }
        if (this.mTmpDeviceStatsArray == null) {
            this.mTmpDeviceStatsArray = new long[stats.getPowerStatsDescriptor().statsArrayLength];
        }
        for (int i = plan.deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation estimation = plan.deviceStateEstimations.get(i);
            if (stats.getDeviceStats(this.mTmpDeviceStatsArray, estimation.stateValues)) {
                double power = 0.0d;
                for (int level = 0; level < 2; level++) {
                    long duration = sStatsLayout.getDeviceSignalLevelTime(this.mTmpDeviceStatsArray, level);
                    power += this.mSignalLevelEstimators[level].calculatePower(duration);
                }
                sStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, power);
                stats.setDeviceStats(estimation.stateValues, this.mTmpDeviceStatsArray);
            }
        }
    }
}
