package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BluetoothPowerStatsProcessor extends com.android.server.power.stats.PowerStatsProcessor {
    private static final java.lang.String TAG = "BluetoothPowerStatsProcessor";
    private final com.android.server.power.stats.UsageBasedPowerEstimator mIdlePowerEstimator;
    private com.android.internal.os.PowerStats.Descriptor mLastUsedDescriptor;
    private com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan mPlan;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mRxPowerEstimator;
    private com.android.server.power.stats.BluetoothPowerStatsLayout mStatsLayout;
    private long[] mTmpDeviceStatsArray;
    private long[] mTmpUidStatsArray;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mTxPowerEstimator;

    public BluetoothPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile) {
        this.mRxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("bluetooth.controller.rx"));
        this.mTxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("bluetooth.controller.tx"));
        this.mIdlePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePower("bluetooth.controller.idle"));
    }

    private static class Intermediates {
        public long consumedEnergy;
        public double idlePower;
        public long rxBytes;
        public double rxPower;
        public long rxTime;
        public long scanTime;
        public long txBytes;
        public double txPower;
        public long txTime;

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
            com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates = new com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates();
            estimation.intermediates = intermediates;
            computeDevicePowerEstimates(stats, estimation.stateValues, intermediates);
        }
        if (this.mStatsLayout.getEnergyConsumerCount() != 0) {
            double ratio = computeEstimateAdjustmentRatioUsingConsumedEnergy();
            if (ratio != 1.0d) {
                for (int i2 = this.mPlan.deviceStateEstimations.size() - 1; i2 >= 0; i2--) {
                    com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation estimation2 = this.mPlan.deviceStateEstimations.get(i2);
                    adjustDevicePowerEstimates(stats, estimation2.stateValues, (com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates) estimation2.intermediates, ratio);
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
        this.mStatsLayout = new com.android.server.power.stats.BluetoothPowerStatsLayout(descriptor);
        this.mTmpDeviceStatsArray = new long[descriptor.statsArrayLength];
        this.mTmpUidStatsArray = new long[descriptor.uidStatsArrayLength];
    }

    private void computeDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] deviceStates, com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates) {
        if (!stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStates)) {
            return;
        }
        for (int i = this.mStatsLayout.getEnergyConsumerCount() - 1; i >= 0; i--) {
            intermediates.consumedEnergy += this.mStatsLayout.getConsumedEnergy(this.mTmpDeviceStatsArray, i);
        }
        intermediates.rxTime = this.mStatsLayout.getDeviceRxTime(this.mTmpDeviceStatsArray);
        intermediates.txTime = this.mStatsLayout.getDeviceTxTime(this.mTmpDeviceStatsArray);
        intermediates.scanTime = this.mStatsLayout.getDeviceScanTime(this.mTmpDeviceStatsArray);
        long idleTime = this.mStatsLayout.getDeviceIdleTime(this.mTmpDeviceStatsArray);
        intermediates.rxPower = this.mRxPowerEstimator.calculatePower(intermediates.rxTime);
        intermediates.txPower = this.mTxPowerEstimator.calculatePower(intermediates.txTime);
        intermediates.idlePower = this.mIdlePowerEstimator.calculatePower(idleTime);
        this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, intermediates.rxPower + intermediates.txPower + intermediates.idlePower);
        stats.setDeviceStats(deviceStates, this.mTmpDeviceStatsArray);
    }

    private double computeEstimateAdjustmentRatioUsingConsumedEnergy() {
        long totalConsumedEnergy = 0;
        double totalPower = 0.0d;
        for (int i = this.mPlan.deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates) this.mPlan.deviceStateEstimations.get(i).intermediates;
            totalPower += intermediates.rxPower + intermediates.txPower + intermediates.idlePower;
            totalConsumedEnergy += intermediates.consumedEnergy;
        }
        if (totalPower == 0.0d) {
            return 1.0d;
        }
        return uCtoMah(totalConsumedEnergy) / totalPower;
    }

    private void adjustDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] deviceStates, com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates, double ratio) {
        intermediates.rxPower *= ratio;
        intermediates.txPower *= ratio;
        intermediates.idlePower *= ratio;
        double adjutedPower = intermediates.rxPower + intermediates.txPower + intermediates.idlePower;
        if (!stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStates)) {
            return;
        }
        this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, adjutedPower);
        stats.setDeviceStats(deviceStates, this.mTmpDeviceStatsArray);
    }

    private void combineDeviceStateEstimates() {
        for (int i = this.mPlan.combinedDeviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse = this.mPlan.combinedDeviceStateEstimations.get(i);
            com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates cdseIntermediates = new com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates();
            cdse.intermediates = cdseIntermediates;
            java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = cdse.deviceStateEstimations;
            for (int j = deviceStateEstimations.size() - 1; j >= 0; j--) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation dse = deviceStateEstimations.get(j);
                com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates) dse.intermediates;
                cdseIntermediates.rxTime += intermediates.rxTime;
                cdseIntermediates.rxBytes += intermediates.rxBytes;
                cdseIntermediates.rxPower += intermediates.rxPower;
                cdseIntermediates.txTime += intermediates.txTime;
                cdseIntermediates.txBytes += intermediates.txBytes;
                cdseIntermediates.txPower += intermediates.txPower;
                cdseIntermediates.idlePower += intermediates.idlePower;
                cdseIntermediates.scanTime += intermediates.scanTime;
                cdseIntermediates.consumedEnergy += intermediates.consumedEnergy;
            }
        }
    }

    private void computeUidActivityTotals(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int uid, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate) {
        com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
        for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
            if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                intermediates.rxBytes += this.mStatsLayout.getUidRxBytes(this.mTmpUidStatsArray);
                intermediates.txBytes += this.mStatsLayout.getUidTxBytes(this.mTmpUidStatsArray);
            }
        }
    }

    private void computeUidPowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int uid, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate) {
        com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.BluetoothPowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
        boolean normalizeRxByScanTime = intermediates.scanTime > intermediates.rxTime;
        boolean normalizeTxByScanTime = intermediates.scanTime > intermediates.txTime;
        for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
            if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                double power = 0.0d;
                if (normalizeRxByScanTime) {
                    if (intermediates.scanTime != 0) {
                        power = 0.0d + ((intermediates.rxPower * this.mStatsLayout.getUidScanTime(this.mTmpUidStatsArray)) / intermediates.scanTime);
                    }
                } else if (intermediates.rxBytes != 0) {
                    power = 0.0d + ((intermediates.rxPower * this.mStatsLayout.getUidRxBytes(this.mTmpUidStatsArray)) / intermediates.rxBytes);
                }
                if (normalizeTxByScanTime) {
                    if (intermediates.scanTime != 0) {
                        power += (intermediates.txPower * this.mStatsLayout.getUidScanTime(this.mTmpUidStatsArray)) / intermediates.scanTime;
                    }
                } else if (intermediates.txBytes != 0) {
                    power += (intermediates.txPower * this.mStatsLayout.getUidTxBytes(this.mTmpUidStatsArray)) / intermediates.txBytes;
                }
                this.mStatsLayout.setUidPowerEstimate(this.mTmpUidStatsArray, power);
                stats.setUidStats(uid, proportionalEstimate.stateValues, this.mTmpUidStatsArray);
            }
        }
    }
}
