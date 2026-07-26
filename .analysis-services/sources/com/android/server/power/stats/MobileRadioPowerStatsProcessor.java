package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class MobileRadioPowerStatsProcessor extends com.android.server.power.stats.PowerStatsProcessor {
    private static final boolean DEBUG = false;
    private static final int IGNORE = -1;
    private static final int NUM_SIGNAL_STRENGTH_LEVELS = android.telephony.CellSignalStrength.getNumSignalStrengthLevels();
    private static final java.lang.String TAG = "MobileRadioPowerStatsProcessor";
    private final com.android.server.power.stats.UsageBasedPowerEstimator mCallPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mIdlePowerEstimator;
    private com.android.internal.os.PowerStats.Descriptor mLastUsedDescriptor;
    private com.android.server.power.stats.PowerStatsProcessor.PowerEstimationPlan mPlan;
    private final android.util.SparseArray<com.android.server.power.stats.MobileRadioPowerStatsProcessor.RxTxPowerEstimators> mRxTxPowerEstimators = new android.util.SparseArray<>();
    private final com.android.server.power.stats.UsageBasedPowerEstimator mScanPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mSleepPowerEstimator;
    private com.android.server.power.stats.MobileRadioPowerStatsLayout mStatsLayout;
    private long[] mTmpDeviceStatsArray;
    private long[] mTmpStateStatsArray;
    private long[] mTmpUidStatsArray;

    private static class RxTxPowerEstimators {
        com.android.server.power.stats.UsageBasedPowerEstimator mRxPowerEstimator;
        com.android.server.power.stats.UsageBasedPowerEstimator[] mTxPowerEstimators;

        private RxTxPowerEstimators() {
            this.mTxPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[android.telephony.ModemActivityInfo.getNumTxPowerLevels()];
        }
    }

    public MobileRadioPowerStatsProcessor(com.android.internal.os.PowerProfile powerProfile) {
        double sleepDrainRateMa = powerProfile.getAverageBatteryDrainOrDefaultMa(4294967296L, Double.NaN);
        if (java.lang.Double.isNaN(sleepDrainRateMa)) {
            this.mSleepPowerEstimator = null;
        } else {
            this.mSleepPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(sleepDrainRateMa);
        }
        double idleDrainRateMa = powerProfile.getAverageBatteryDrainOrDefaultMa(4563402752L, Double.NaN);
        if (java.lang.Double.isNaN(idleDrainRateMa)) {
            this.mIdlePowerEstimator = null;
        } else {
            this.mIdlePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(idleDrainRateMa);
        }
        double powerRadioActiveMa = powerProfile.getAveragePowerOrDefault("radio.active", Double.NaN);
        if (java.lang.Double.isNaN(powerRadioActiveMa)) {
            double sum = 0.0d + powerProfile.getAveragePower("modem.controller.rx");
            for (int i = 0; i < NUM_SIGNAL_STRENGTH_LEVELS; i++) {
                sum += powerProfile.getAveragePower("modem.controller.tx", i);
            }
            int i2 = NUM_SIGNAL_STRENGTH_LEVELS;
            powerRadioActiveMa = sum / ((double) (i2 + 1));
        }
        this.mCallPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerRadioActiveMa);
        this.mScanPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePowerOrDefault("radio.scanning", 0.0d));
        int rat = 0;
        while (rat < 3) {
            int freqCount = rat == 2 ? 5 : 1;
            for (int freqRange = 0; freqRange < freqCount; freqRange++) {
                this.mRxTxPowerEstimators.put(com.android.server.power.stats.MobileRadioPowerStatsCollector.makeStateKey(rat, freqRange), buildRxTxPowerEstimators(powerProfile, rat, freqRange));
            }
            rat++;
        }
    }

    private static com.android.server.power.stats.MobileRadioPowerStatsProcessor.RxTxPowerEstimators buildRxTxPowerEstimators(com.android.internal.os.PowerProfile powerProfile, int rat, int freqRange) {
        com.android.internal.os.PowerProfile powerProfile2 = powerProfile;
        com.android.server.power.stats.MobileRadioPowerStatsProcessor.RxTxPowerEstimators estimators = new com.android.server.power.stats.MobileRadioPowerStatsProcessor.RxTxPowerEstimators();
        long rxKey = com.android.internal.power.ModemPowerProfile.getAverageBatteryDrainKey(536870912, rat, freqRange, -1);
        double d = Double.NaN;
        double rxDrainRateMa = powerProfile2.getAverageBatteryDrainOrDefaultMa(rxKey, Double.NaN);
        if (java.lang.Double.isNaN(rxDrainRateMa)) {
            android.util.Log.w(TAG, "Unavailable Power Profile constant for key 0x" + java.lang.Long.toHexString(rxKey));
            rxDrainRateMa = 0.0d;
        }
        estimators.mRxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(rxDrainRateMa);
        int txLevel = 0;
        while (txLevel < android.telephony.ModemActivityInfo.getNumTxPowerLevels()) {
            long txKey = com.android.internal.power.ModemPowerProfile.getAverageBatteryDrainKey(805306368, rat, freqRange, txLevel);
            double txDrainRateMa = powerProfile2.getAverageBatteryDrainOrDefaultMa(txKey, d);
            if (java.lang.Double.isNaN(txDrainRateMa)) {
                android.util.Log.w(TAG, "Unavailable Power Profile constant for key 0x" + java.lang.Long.toHexString(txKey));
                txDrainRateMa = 0.0d;
            }
            estimators.mTxPowerEstimators[txLevel] = new com.android.server.power.stats.UsageBasedPowerEstimator(txDrainRateMa);
            txLevel++;
            d = Double.NaN;
            powerProfile2 = powerProfile;
        }
        return estimators;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Intermediates {
        public double callPower;
        public long consumedEnergy;
        public double inactivePower;
        public long rxPackets;
        public double rxPower;
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
            com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates = new com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates();
            estimation.intermediates = intermediates;
            computeDevicePowerEstimates(stats, estimation.stateValues, intermediates);
        }
        if (this.mStatsLayout.getEnergyConsumerCount() != 0) {
            double ratio = computeEstimateAdjustmentRatioUsingConsumedEnergy();
            if (ratio != 1.0d) {
                for (int i2 = this.mPlan.deviceStateEstimations.size() - 1; i2 >= 0; i2--) {
                    com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation estimation2 = this.mPlan.deviceStateEstimations.get(i2);
                    adjustDevicePowerEstimates(stats, estimation2.stateValues, (com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates) estimation2.intermediates, ratio);
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
                    computeUidRxTxTotals(stats, uid, this.mPlan.uidStateEstimates.get(i3));
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
        this.mStatsLayout = new com.android.server.power.stats.MobileRadioPowerStatsLayout(descriptor);
        this.mTmpDeviceStatsArray = new long[descriptor.statsArrayLength];
        this.mTmpStateStatsArray = new long[descriptor.stateStatsArrayLength];
        this.mTmpUidStatsArray = new long[descriptor.uidStatsArrayLength];
    }

    private void computeDevicePowerEstimates(final com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, final int[] deviceStates, final com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates) {
        if (!stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStates)) {
            return;
        }
        for (int i = this.mStatsLayout.getEnergyConsumerCount() - 1; i >= 0; i--) {
            intermediates.consumedEnergy += this.mStatsLayout.getConsumedEnergy(this.mTmpDeviceStatsArray, i);
        }
        if (this.mSleepPowerEstimator != null) {
            intermediates.inactivePower += this.mSleepPowerEstimator.calculatePower(this.mStatsLayout.getDeviceSleepTime(this.mTmpDeviceStatsArray));
        }
        if (this.mIdlePowerEstimator != null) {
            intermediates.inactivePower += this.mIdlePowerEstimator.calculatePower(this.mStatsLayout.getDeviceIdleTime(this.mTmpDeviceStatsArray));
        }
        if (this.mScanPowerEstimator != null) {
            intermediates.inactivePower += this.mScanPowerEstimator.calculatePower(this.mStatsLayout.getDeviceScanTime(this.mTmpDeviceStatsArray));
        }
        stats.forEachStateStatsKey(new java.util.function.IntConsumer() { // from class: com.android.server.power.stats.MobileRadioPowerStatsProcessor$$ExternalSyntheticLambda0
            @Override // java.util.function.IntConsumer
            public final void accept(int i2) {
                this.f$0.lambda$computeDevicePowerEstimates$0(stats, deviceStates, intermediates, i2);
            }
        });
        if (this.mCallPowerEstimator != null) {
            intermediates.callPower = this.mCallPowerEstimator.calculatePower(this.mStatsLayout.getDeviceCallTime(this.mTmpDeviceStatsArray));
        }
        this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, intermediates.rxPower + intermediates.txPower + intermediates.inactivePower);
        this.mStatsLayout.setDeviceCallPowerEstimate(this.mTmpDeviceStatsArray, intermediates.callPower);
        stats.setDeviceStats(deviceStates, this.mTmpDeviceStatsArray);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$computeDevicePowerEstimates$0(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] deviceStates, com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates, int key) {
        com.android.server.power.stats.MobileRadioPowerStatsProcessor.RxTxPowerEstimators estimators = this.mRxTxPowerEstimators.get(key);
        stats.getStateStats(this.mTmpStateStatsArray, key, deviceStates);
        long rxTime = this.mStatsLayout.getStateRxTime(this.mTmpStateStatsArray);
        intermediates.rxPower += estimators.mRxPowerEstimator.calculatePower(rxTime);
        for (int txLevel = 0; txLevel < android.telephony.ModemActivityInfo.getNumTxPowerLevels(); txLevel++) {
            long txTime = this.mStatsLayout.getStateTxTime(this.mTmpStateStatsArray, txLevel);
            intermediates.txPower += estimators.mTxPowerEstimators[txLevel].calculatePower(txTime);
        }
    }

    private double computeEstimateAdjustmentRatioUsingConsumedEnergy() {
        long totalConsumedEnergy = 0;
        double totalPower = 0.0d;
        for (int i = this.mPlan.deviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates) this.mPlan.deviceStateEstimations.get(i).intermediates;
            totalPower += intermediates.rxPower + intermediates.txPower + intermediates.inactivePower + intermediates.callPower;
            totalConsumedEnergy += intermediates.consumedEnergy;
        }
        if (totalPower == 0.0d) {
            return 1.0d;
        }
        return uCtoMah(totalConsumedEnergy) / totalPower;
    }

    private void adjustDevicePowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] deviceStates, com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates, double ratio) {
        intermediates.rxPower *= ratio;
        intermediates.txPower *= ratio;
        intermediates.inactivePower *= ratio;
        intermediates.callPower *= ratio;
        if (!stats.getDeviceStats(this.mTmpDeviceStatsArray, deviceStates)) {
            return;
        }
        this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStatsArray, intermediates.rxPower + intermediates.txPower + intermediates.inactivePower);
        this.mStatsLayout.setDeviceCallPowerEstimate(this.mTmpDeviceStatsArray, intermediates.callPower);
        stats.setDeviceStats(deviceStates, this.mTmpDeviceStatsArray);
    }

    private void combineDeviceStateEstimates() {
        for (int i = this.mPlan.combinedDeviceStateEstimations.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse = this.mPlan.combinedDeviceStateEstimations.get(i);
            com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates cdseIntermediates = new com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates();
            cdse.intermediates = cdseIntermediates;
            java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = cdse.deviceStateEstimations;
            for (int j = deviceStateEstimations.size() - 1; j >= 0; j--) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation dse = deviceStateEstimations.get(j);
                com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates) dse.intermediates;
                cdseIntermediates.rxPower += intermediates.rxPower;
                cdseIntermediates.txPower += intermediates.txPower;
                cdseIntermediates.inactivePower += intermediates.inactivePower;
                cdseIntermediates.consumedEnergy += intermediates.consumedEnergy;
            }
        }
    }

    private void computeUidRxTxTotals(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int uid, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate) {
        com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
        for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
            if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                intermediates.rxPackets += this.mStatsLayout.getUidRxPackets(this.mTmpUidStatsArray);
                intermediates.txPackets += this.mStatsLayout.getUidTxPackets(this.mTmpUidStatsArray);
            }
        }
    }

    private void computeUidPowerEstimates(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int uid, com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate) {
        com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates intermediates = (com.android.server.power.stats.MobileRadioPowerStatsProcessor.Intermediates) uidStateEstimate.combinedDeviceStateEstimate.intermediates;
        for (com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate proportionalEstimate : uidStateEstimate.proportionalEstimates) {
            if (stats.getUidStats(this.mTmpUidStatsArray, uid, proportionalEstimate.stateValues)) {
                double power = intermediates.rxPackets != 0 ? 0.0d + ((intermediates.rxPower * this.mStatsLayout.getUidRxPackets(this.mTmpUidStatsArray)) / intermediates.rxPackets) : 0.0d;
                if (intermediates.txPackets != 0) {
                    power += (intermediates.txPower * this.mStatsLayout.getUidTxPackets(this.mTmpUidStatsArray)) / intermediates.txPackets;
                }
                this.mStatsLayout.setUidPowerEstimate(this.mTmpUidStatsArray, power);
                stats.setUidStats(uid, proportionalEstimate.stateValues, this.mTmpUidStatsArray);
            }
        }
    }
}
