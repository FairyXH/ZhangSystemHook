package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class WifiPowerStatsProcessor extends com.android.server.power.stats.PowerStatsProcessor {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "WifiPowerStatsProcessor";
    private final com.android.server.power.stats.UsageBasedPowerEstimator mActivePowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mBatchedScanPowerEstimator;
    private boolean mHasWifiPowerController;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mIdlePowerEstimator;
    private com.android.internal.os.PowerStats.Descriptor mLastUsedDescriptor;
    private com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan mPlan;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mRxPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mScanPowerEstimator;
    private com.android.server.power.stats.WifiPowerStatsLayout mStatsLayout;
    private long[] mTmpDeviceStatsArray;
    private long[] mTmpUidStatsArray;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mTxPowerEstimator;

    public WifiPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile) {
        this.mRxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("wifi.controller.rx"));
        this.mTxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("wifi.controller.tx"));
        this.mIdlePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("wifi.controller.idle"));
        this.mActivePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("wifi.active"));
        this.mScanPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("wifi.scan"));
        this.mBatchedScanPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("wifi.batchedscan"));
    }

    private static class Intermediates {
        public double activePower;
        public long basicScanDuration;
        public double basicScanPower;
        public long batchedScanDuration;
        public double batchedScanPower;
        public long consumedEnergy;
        public double idlePower;
        public long rxPackets;
        public double rxPower;
        public double scanPower;
        public long txPackets;
        public double txPower;

        private Intermediates() {
        }
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    void finish(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
        if (stats.getPowerStatsDescriptor() == null) {
            return;
        }
        unpackPowerStatsDescriptor(stats.getPowerStatsDescriptor());
        if (this.mPlan == null) {
            this.mPlan = new com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan(stats.getConfig());
        }
        for (int i = this.mPlan.deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation estimation = this.mPlan.deviceStateEstimations.get(i);
            com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates = new com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates();
            estimation.intermediates = intermediates;
            computeDevicePowerEstimates(stats, estimation.stateValues, intermediates);
        }
        if (this.mStatsLayout.getEnergyConsumerCount() != 0) {
            double ratio = computeEstimateAdjustmentRatioUsingConsumedEnergy();
            if (ratio != 1.0d) {
                for (int i2 = this.mPlan.deviceStateEstimations.size() - 1; i2 >= 0; i2--) {
                    com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation estimation2 = this.mPlan.deviceStateEstimations.get(i2);
                    adjustDevicePowerEstimates(stats, estimation2.stateValues, (com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates) estimation2.intermediates, ratio);
                }
            }
        }
        combineDeviceStateEstimates();
        java.util.ArrayList<java.lang.Integer> uids = new java.util.ArrayList<>();
        stats.collectUids(uids);
        if (!uids.isEmpty()) {
            java.util.Iterator<java.lang.Integer> it = uids.iterator();
            while (it.hasNext()) {
                int uid = it.next().intValue();
                for (int i3 = 0; i3 < this.mPlan.uidStateEstimates.size(); i3++) {
                    computeUidActivityTotals(stats, uid, this.mPlan.uidStateEstimates.get(i3));
                }
            }
            java.util.Iterator<java.lang.Integer> it2 = uids.iterator();
            while (it2.hasNext()) {
                int uid2 = it2.next().intValue();
                for (int i4 = 0; i4 < this.mPlan.uidStateEstimates.size(); i4++) {
                    computeUidPowerEstimates(stats, uid2, this.mPlan.uidStateEstimates.get(i4));
                }
            }
        }
        this.mPlan.resetIntermediates();
    }

    private void unpackPowerStatsDescriptor(com.android.internal.os.PowerStats.Descriptor descriptor) {
        if (descriptor.equals(this.mLastUsedDescriptor)) {
            return;
        }
        this.mLastUsedDescriptor = descriptor;
        this.mStatsLayout = new com.android.server.power.stats.WifiPowerStatsLayout(descriptor);
        this.mTmpDeviceStatsArray = new long[descriptor.statsArrayLength];
        this.mTmpUidStatsArray = new long[descriptor.uidStatsArrayLength];
        this.mHasWifiPowerController = this.mStatsLayout.isPowerReportingSupported();
    }

    private void computeDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] deviceStates, com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates) {
        if (!stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStates)) {
            return;
        }
        for (int i = this.mStatsLayout.getEnergyConsumerCount() - 1; i >= 0; i--) {
            intermediates.consumedEnergy += this.mStatsLayout.getConsumedEnergy(this.mTmpDeviceStatsArray, i);
        }
        intermediates.basicScanDuration = this.mStatsLayout.getDeviceBasicScanTime(this.mTmpDeviceStatsArray);
        intermediates.batchedScanDuration = this.mStatsLayout.getDeviceBatchedScanTime(this.mTmpDeviceStatsArray);
        if (this.mHasWifiPowerController) {
            intermediates.rxPower = this.mRxPowerEstimator.calculatePower(this.mStatsLayout.getDeviceRxTime(this.mTmpDeviceStatsArray));
            intermediates.txPower = this.mTxPowerEstimator.calculatePower(this.mStatsLayout.getDeviceTxTime(this.mTmpDeviceStatsArray));
            intermediates.scanPower = this.mScanPowerEstimator.calculatePower(this.mStatsLayout.getDeviceScanTime(this.mTmpDeviceStatsArray));
            intermediates.idlePower = this.mIdlePowerEstimator.calculatePower(this.mStatsLayout.getDeviceIdleTime(this.mTmpDeviceStatsArray));
            this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, intermediates.rxPower + intermediates.txPower + intermediates.scanPower + intermediates.idlePower);
        } else {
            intermediates.activePower = this.mActivePowerEstimator.calculatePower(this.mStatsLayout.getDeviceActiveTime(this.mTmpDeviceStatsArray));
            intermediates.basicScanPower = this.mScanPowerEstimator.calculatePower(intermediates.basicScanDuration);
            intermediates.batchedScanPower = this.mBatchedScanPowerEstimator.calculatePower(intermediates.batchedScanDuration);
            this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, intermediates.activePower + intermediates.basicScanPower + intermediates.batchedScanPower);
        }
        stats.setDeviceStats(deviceStates, this.mTmpDeviceStatsArray);
    }

    private double computeEstimateAdjustmentRatioUsingConsumedEnergy() {
        double d;
        double d2;
        long totalConsumedEnergy = 0;
        double totalPower = 0.0d;
        for (int i = this.mPlan.deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates) this.mPlan.deviceStateEstimations.get(i).intermediates;
            if (this.mHasWifiPowerController) {
                d = intermediates.rxPower + intermediates.txPower + intermediates.scanPower;
                d2 = intermediates.idlePower;
            } else {
                d = intermediates.activePower + intermediates.basicScanPower;
                d2 = intermediates.batchedScanPower;
            }
            totalPower += d + d2;
            totalConsumedEnergy += intermediates.consumedEnergy;
        }
        if (totalPower == 0.0d) {
            return 1.0d;
        }
        return uCtoMah(totalConsumedEnergy) / totalPower;
    }

    private void adjustDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] deviceStates, com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates, double ratio) {
        double adjutedPower;
        if (this.mHasWifiPowerController) {
            intermediates.rxPower *= ratio;
            intermediates.txPower *= ratio;
            intermediates.scanPower *= ratio;
            intermediates.idlePower *= ratio;
            adjutedPower = intermediates.rxPower + intermediates.txPower + intermediates.scanPower + intermediates.idlePower;
        } else {
            double adjutedPower2 = intermediates.activePower;
            intermediates.activePower = adjutedPower2 * ratio;
            intermediates.basicScanPower *= ratio;
            intermediates.batchedScanPower *= ratio;
            adjutedPower = intermediates.activePower + intermediates.basicScanPower + intermediates.batchedScanPower;
        }
        if (!stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStates)) {
            return;
        }
        this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, adjutedPower);
        stats.setDeviceStats(deviceStates, this.mTmpDeviceStatsArray);
    }

    private void combineDeviceStateEstimates() {
        for (int i = this.mPlan.combinedDeviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse = this.mPlan.combinedDeviceStateEstimations.get(i);
            com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates cdseIntermediates = new com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates();
            cdse.intermediates = cdseIntermediates;
            java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = cdse.deviceStateEstimations;
            for (int j = deviceStateEstimations.size() - 1; j >= 0; j--) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation dse = deviceStateEstimations.get(j);
                com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates) dse.intermediates;
                if (this.mHasWifiPowerController) {
                    cdseIntermediates.rxPower += intermediates.rxPower;
                    cdseIntermediates.txPower += intermediates.txPower;
                    cdseIntermediates.scanPower += intermediates.scanPower;
                    cdseIntermediates.idlePower += intermediates.idlePower;
                } else {
                    cdseIntermediates.activePower += intermediates.activePower;
                    cdseIntermediates.basicScanPower += intermediates.basicScanPower;
                    cdseIntermediates.batchedScanPower += intermediates.batchedScanPower;
                }
                cdseIntermediates.basicScanDuration += intermediates.basicScanDuration;
                cdseIntermediates.batchedScanDuration += intermediates.batchedScanDuration;
                cdseIntermediates.consumedEnergy += intermediates.consumedEnergy;
            }
        }
    }

    private void computeUidActivityTotals(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int uid, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate) {
        com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
        for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
            if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                intermediates.rxPackets += this.mStatsLayout.getUidRxPackets(this.mTmpUidStatsArray);
                intermediates.txPackets += this.mStatsLayout.getUidTxPackets(this.mTmpUidStatsArray);
            }
        }
    }

    private void computeUidPowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int uid, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate) {
        com.android.server.power.stats.PowerComponentAggregatedPowerStats powerComponentAggregatedPowerStats = stats;
        int i = uid;
        com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.WifiPowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
        for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
            if (powerComponentAggregatedPowerStats.getUidStats(this.mTmpUidStatsArray, i, proportionalEstimate.stateValues)) {
                if (this.mHasWifiPowerController) {
                    power = intermediates.rxPackets != 0 ? 0.0d + ((intermediates.rxPower * this.mStatsLayout.getUidRxPackets(this.mTmpUidStatsArray)) / intermediates.rxPackets) : 0.0d;
                    if (intermediates.txPackets != 0) {
                        power += (intermediates.txPower * this.mStatsLayout.getUidTxPackets(this.mTmpUidStatsArray)) / intermediates.txPackets;
                    }
                    long totalScanDuration = intermediates.basicScanDuration + intermediates.batchedScanDuration;
                    if (totalScanDuration != 0) {
                        long scanDuration = this.mStatsLayout.getUidScanTime(this.mTmpUidStatsArray) + this.mStatsLayout.getUidBatchedScanTime(this.mTmpUidStatsArray);
                        power += (intermediates.scanPower * scanDuration) / totalScanDuration;
                    }
                } else {
                    long totalPackets = intermediates.rxPackets + intermediates.txPackets;
                    if (totalPackets != 0) {
                        long packets = this.mStatsLayout.getUidRxPackets(this.mTmpUidStatsArray) + this.mStatsLayout.getUidTxPackets(this.mTmpUidStatsArray);
                        power = 0.0d + ((intermediates.activePower * packets) / totalPackets);
                    }
                    if (intermediates.basicScanDuration != 0) {
                        long scanDuration2 = this.mStatsLayout.getUidScanTime(this.mTmpUidStatsArray);
                        power += (intermediates.basicScanPower * scanDuration2) / intermediates.basicScanDuration;
                    }
                    long scanDuration3 = intermediates.batchedScanDuration;
                    if (scanDuration3 != 0) {
                        long batchedScanDuration = this.mStatsLayout.getUidBatchedScanTime(this.mTmpUidStatsArray);
                        power += (intermediates.batchedScanPower * batchedScanDuration) / intermediates.batchedScanDuration;
                    }
                }
                this.mStatsLayout.setUidPowerEstimate(this.mTmpUidStatsArray, power);
                stats.setUidStats(uid, proportionalEstimate.stateValues, this.mTmpUidStatsArray);
                powerComponentAggregatedPowerStats = stats;
                i = uid;
            }
        }
    }
}
