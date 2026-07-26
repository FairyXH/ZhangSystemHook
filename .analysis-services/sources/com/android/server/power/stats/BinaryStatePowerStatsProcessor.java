package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
abstract class BinaryStatePowerStatsProcessor extends com.android.server.power.stats.PowerStatsProcessor {
    static final int STATE_OFF = 0;
    static final int STATE_ON = 1;
    private com.android.internal.os.PowerStats.Descriptor mDescriptor;
    private boolean mEnergyConsumerSupported;
    private int mInitiatingUid;
    private int mLastState;
    private long mLastStateTimestamp;
    private long mLastUpdateTimestamp;
    private com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan mPlan;
    private final int mPowerComponentId;
    private com.android.internal.os.PowerStats mPowerStats;
    private final com.android.server.power.stats.BinaryStatePowerStatsLayout mStatsLayout;
    private long[] mTmpDeviceStatsArray;
    private long[] mTmpUidStatsArray;
    private final com.android.server.power.stats.PowerStatsUidResolver mUidResolver;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mUsageBasedPowerEstimator;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    protected @interface BinaryState {
    }

    protected abstract int getBinaryState(android.os.BatteryStats.HistoryItem historyItem);

    BinaryStatePowerStatsProcessor(int powerComponentId, com.android.server.power.stats.PowerStatsUidResolver uidResolver, double averagePowerMilliAmp) {
        this(powerComponentId, uidResolver, averagePowerMilliAmp, new com.android.server.power.stats.BinaryStatePowerStatsLayout());
    }

    BinaryStatePowerStatsProcessor(int powerComponentId, com.android.server.power.stats.PowerStatsUidResolver uidResolver, double averagePowerMilliAmp, com.android.server.power.stats.BinaryStatePowerStatsLayout statsLayout) {
        this.mInitiatingUid = -1;
        this.mLastState = 0;
        this.mPowerComponentId = powerComponentId;
        this.mUsageBasedPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(averagePowerMilliAmp);
        this.mUidResolver = uidResolver;
        this.mStatsLayout = statsLayout;
    }

    private void ensureInitialized() {
        if (this.mDescriptor != null) {
            return;
        }
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mStatsLayout.toExtras(extras);
        this.mDescriptor = new com.android.internal.os.PowerStats.Descriptor(this.mPowerComponentId, this.mStatsLayout.getDeviceStatsArrayLength(), (android.util.SparseArray) null, 0, this.mStatsLayout.getUidStatsArrayLength(), extras);
        this.mPowerStats = new com.android.internal.os.PowerStats(this.mDescriptor);
        this.mPowerStats.stats = new long[this.mDescriptor.statsArrayLength];
        this.mTmpDeviceStatsArray = new long[this.mDescriptor.statsArrayLength];
        this.mTmpUidStatsArray = new long[this.mDescriptor.uidStatsArrayLength];
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    void start(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
        ensureInitialized();
        this.mLastState = 0;
        this.mLastStateTimestamp = timestampMs;
        this.mInitiatingUid = -1;
        flushPowerStats(stats, this.mLastStateTimestamp);
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    void noteStateChange(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, android.os.BatteryStats.HistoryItem item) {
        int state = getBinaryState(item);
        if (state == this.mLastState) {
            return;
        }
        if (state == 1) {
            if (item.eventCode == 32789) {
                this.mInitiatingUid = this.mUidResolver.mapUid(item.eventTag.uid);
            }
        } else {
            recordUsageDuration(this.mPowerStats, this.mInitiatingUid, item.time);
            this.mInitiatingUid = -1;
            if (!this.mEnergyConsumerSupported) {
                flushPowerStats(stats, item.time);
            }
        }
        this.mLastStateTimestamp = item.time;
        this.mLastState = state;
    }

    protected void recordUsageDuration(com.android.internal.os.PowerStats powerStats, int uid, long time) {
        long durationMs = time - this.mLastStateTimestamp;
        this.mStatsLayout.setUsageDuration(this.mPowerStats.stats, this.mStatsLayout.getUsageDuration(this.mPowerStats.stats) + durationMs);
        if (uid != -1) {
            if (((long[]) this.mPowerStats.uidStats.get(uid)) == null) {
                long[] uidStats = new long[this.mDescriptor.uidStatsArrayLength];
                this.mPowerStats.uidStats.put(uid, uidStats);
                this.mStatsLayout.setUidUsageDuration(uidStats, durationMs);
            } else {
                this.mStatsLayout.setUsageDuration(this.mPowerStats.stats, this.mStatsLayout.getUsageDuration(this.mPowerStats.stats) + durationMs);
            }
        }
        this.mLastStateTimestamp = time;
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    void addPowerStats(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, com.android.internal.os.PowerStats powerStats, long timestampMs) {
        ensureInitialized();
        if (this.mLastState == 1) {
            recordUsageDuration(this.mPowerStats, this.mInitiatingUid, timestampMs);
        }
        long consumedEnergy = this.mStatsLayout.getConsumedEnergy(powerStats.stats, 0);
        if (consumedEnergy != -1) {
            this.mEnergyConsumerSupported = true;
            this.mStatsLayout.setConsumedEnergy(this.mPowerStats.stats, 0, consumedEnergy);
        }
        flushPowerStats(stats, timestampMs);
    }

    private void flushPowerStats(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestamp) {
        this.mPowerStats.durationMs = timestamp - this.mLastUpdateTimestamp;
        stats.addPowerStats(this.mPowerStats, timestamp);
        java.util.Arrays.fill(this.mPowerStats.stats, 0L);
        this.mPowerStats.uidStats.clear();
        this.mLastUpdateTimestamp = timestamp;
    }

    private static class Intermediates {
        public long duration;
        public double power;

        private Intermediates() {
        }
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    void finish(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
        if (this.mLastState == 1) {
            recordUsageDuration(this.mPowerStats, this.mInitiatingUid, timestampMs);
        }
        flushPowerStats(stats, timestampMs);
        if (this.mPlan == null) {
            this.mPlan = new com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan(stats.getConfig());
        }
        computeDevicePowerEstimates(stats, this.mPlan, this.mEnergyConsumerSupported);
        combineDevicePowerEstimates(stats);
        java.util.List<java.lang.Integer> uids = new java.util.ArrayList<>();
        stats.collectUids(uids);
        computeUidActivityTotals(stats, uids);
        computeUidPowerEstimates(stats, uids);
    }

    protected void computeDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan plan, boolean energyConsumerSupported) {
        double power;
        for (int i = plan.deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation estimation = plan.deviceStateEstimations.get(i);
            if (stats.getDeviceStats(this.mTmpDeviceStatsArray, estimation.stateValues)) {
                long duration = this.mStatsLayout.getUsageDuration(this.mTmpDeviceStatsArray);
                if (duration > 0) {
                    if (energyConsumerSupported) {
                        power = uCtoMah(this.mStatsLayout.getConsumedEnergy(this.mTmpDeviceStatsArray, 0));
                    } else {
                        power = this.mUsageBasedPowerEstimator.calculatePower(duration);
                    }
                    this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, power);
                    stats.setDeviceStats(estimation.stateValues, this.mTmpDeviceStatsArray);
                }
            }
        }
    }

    private void combineDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats) {
        for (int i = this.mPlan.combinedDeviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate estimation = this.mPlan.combinedDeviceStateEstimations.get(i);
            com.android.server.power.stats.BinaryStatePowerStatsProcessor.Intermediates intermediates = new com.android.server.power.stats.BinaryStatePowerStatsProcessor.Intermediates();
            estimation.intermediates = intermediates;
            for (int j = estimation.deviceStateEstimations.size() - 1; j >= 0; j--) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation deviceStateEstimation = estimation.deviceStateEstimations.get(j);
                if (stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStateEstimation.stateValues)) {
                    intermediates.power += this.mStatsLayout.getDevicePowerEstimate(this.mTmpDeviceStatsArray);
                }
            }
        }
    }

    private void computeUidActivityTotals(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, java.util.List<java.lang.Integer> uids) {
        for (int i = this.mPlan.uidStateEstimates.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate = this.mPlan.uidStateEstimates.get(i);
            com.android.server.power.stats.BinaryStatePowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.BinaryStatePowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
            for (int j = uids.size() - 1; j >= 0; j--) {
                int uid = uids.get(j).intValue();
                for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
                    if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                        intermediates.duration += this.mStatsLayout.getUidUsageDuration(this.mTmpUidStatsArray);
                    }
                }
            }
        }
    }

    private void computeUidPowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, java.util.List<java.lang.Integer> uids) {
        for (int i = this.mPlan.uidStateEstimates.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate = this.mPlan.uidStateEstimates.get(i);
            com.android.server.power.stats.BinaryStatePowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.BinaryStatePowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
            if (intermediates.duration != 0) {
                java.util.List<com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate> proportionalEstimates = uidStateEstimate.proportionalEstimates;
                for (int j = proportionalEstimates.size() - 1; j >= 0; j--) {
                    com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate = proportionalEstimates.get(j);
                    for (int k = uids.size() - 1; k >= 0; k--) {
                        int uid = uids.get(k).intValue();
                        if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                            double power = (intermediates.power * this.mStatsLayout.getUidUsageDuration(this.mTmpUidStatsArray)) / intermediates.duration;
                            this.mStatsLayout.setUidPowerEstimate(this.mTmpUidStatsArray, power);
                            stats.setUidStats(uid, proportionalEstimate.stateValues, this.mTmpUidStatsArray);
                        }
                    }
                }
            }
        }
    }
}
