package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BluetoothPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "BluetoothPowerCalc";
    private static final android.os.BatteryConsumer.Key[] UNINITIALIZED_KEYS = new android.os.BatteryConsumer.Key[0];
    private final boolean mHasBluetoothPowerController;
    private final double mIdleMa;
    private final double mRxMa;
    private final double mTxMa;

    private static class PowerAndDuration {
        public long durationMs;
        public android.os.BatteryConsumer.Key[] keys;
        public double powerMah;
        public double[] powerPerKeyMah;
        public long totalDurationMs;
        public double totalPowerMah;

        private PowerAndDuration() {
        }
    }

    public BluetoothPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mIdleMa = profile.getAveragePower("bluetooth.controller.idle");
        this.mRxMa = profile.getAveragePower("bluetooth.controller.rx");
        this.mTxMa = profile.getAveragePower("bluetooth.controller.tx");
        this.mHasBluetoothPowerController = (this.mIdleMa == 0.0d || this.mRxMa == 0.0d || this.mTxMa == 0.0d) ? false : true;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 2;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        if (!batteryStats.hasBluetoothActivityReporting()) {
            return;
        }
        android.os.BatteryConsumer.Key[] keys = UNINITIALIZED_KEYS;
        com.android.server.power.stats.BluetoothPowerCalculator.PowerAndDuration powerAndDuration = new com.android.server.power.stats.BluetoothPowerCalculator.PowerAndDuration();
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            if (keys == UNINITIALIZED_KEYS) {
                if (query.isProcessStateDataNeeded()) {
                    keys = app.getKeys(2);
                    powerAndDuration.keys = keys;
                    powerAndDuration.powerPerKeyMah = new double[keys.length];
                } else {
                    keys = null;
                }
            }
            calculateApp(app, powerAndDuration, query);
        }
        long consumedEnergyUC = batteryStats.getBluetoothEnergyConsumptionUC();
        int powerModel = getPowerModel(consumedEnergyUC, query);
        android.os.BatteryStats.ControllerActivityCounter activityCounter = batteryStats.getBluetoothControllerActivity();
        calculatePowerAndDuration(null, powerModel, consumedEnergyUC, activityCounter, query.shouldForceUsePowerProfileModel(), powerAndDuration);
        java.lang.Math.max(0L, powerAndDuration.durationMs - powerAndDuration.totalDurationMs);
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(2, powerAndDuration.durationMs).setConsumedPower(2, java.lang.Math.max(powerAndDuration.powerMah, powerAndDuration.totalPowerMah), powerModel);
        builder.getAggregateBatteryConsumerBuilder(1).setUsageDurationMillis(2, powerAndDuration.totalDurationMs).setConsumedPower(2, powerAndDuration.totalPowerMah, powerModel);
    }

    private void calculateApp(android.os.UidBatteryConsumer.Builder app, com.android.server.power.stats.BluetoothPowerCalculator.PowerAndDuration powerAndDuration, android.os.BatteryUsageStatsQuery query) {
        long consumedEnergyUC = app.getBatteryStatsUid().getBluetoothEnergyConsumptionUC();
        int powerModel = getPowerModel(consumedEnergyUC, query);
        android.os.BatteryStats.ControllerActivityCounter activityCounter = app.getBatteryStatsUid().getBluetoothControllerActivity();
        calculatePowerAndDuration(app.getBatteryStatsUid(), powerModel, consumedEnergyUC, activityCounter, query.shouldForceUsePowerProfileModel(), powerAndDuration);
        app.setUsageDurationMillis(2, powerAndDuration.durationMs).setConsumedPower(2, powerAndDuration.powerMah, powerModel);
        if (!app.isVirtualUid()) {
            powerAndDuration.totalDurationMs += powerAndDuration.durationMs;
            powerAndDuration.totalPowerMah += powerAndDuration.powerMah;
        }
        if (query.isProcessStateDataNeeded() && powerAndDuration.keys != null) {
            for (int j = 0; j < powerAndDuration.keys.length; j++) {
                android.os.BatteryConsumer.Key key = powerAndDuration.keys[j];
                int processState = key.processState;
                if (processState != 0) {
                    app.setConsumedPower(key, powerAndDuration.powerPerKeyMah[j], powerModel);
                }
            }
        }
    }

    private void calculatePowerAndDuration(android.os.BatteryStats.Uid uid, int powerModel, long consumedEnergyUC, android.os.BatteryStats.ControllerActivityCounter counter, boolean ignoreReportedPower, com.android.server.power.stats.BluetoothPowerCalculator.PowerAndDuration powerAndDuration) {
        if (counter == null) {
            powerAndDuration.durationMs = 0L;
            powerAndDuration.powerMah = 0.0d;
            if (powerAndDuration.powerPerKeyMah != null) {
                java.util.Arrays.fill(powerAndDuration.powerPerKeyMah, 0.0d);
                return;
            }
            return;
        }
        android.os.BatteryStats.LongCounter idleTimeCounter = counter.getIdleTimeCounter();
        android.os.BatteryStats.LongCounter rxTimeCounter = counter.getRxTimeCounter();
        android.os.BatteryStats.LongCounter txTimeCounter = counter.getTxTimeCounters()[0];
        long idleTimeMs = idleTimeCounter.getCountLocked(0);
        long rxTimeMs = rxTimeCounter.getCountLocked(0);
        long txTimeMs = txTimeCounter.getCountLocked(0);
        powerAndDuration.durationMs = idleTimeMs + rxTimeMs + txTimeMs;
        if (powerModel == 2) {
            powerAndDuration.powerMah = uCtoMah(consumedEnergyUC);
            if (uid != null && powerAndDuration.keys != null) {
                for (int i = 0; i < powerAndDuration.keys.length; i++) {
                    android.os.BatteryConsumer.Key key = powerAndDuration.keys[i];
                    int processState = key.processState;
                    if (processState != 0) {
                        powerAndDuration.powerPerKeyMah[i] = uCtoMah(uid.getBluetoothEnergyConsumptionUC(processState));
                    }
                }
                return;
            }
            return;
        }
        if (!ignoreReportedPower) {
            double powerMah = counter.getPowerCounter().getCountLocked(0) / 3600000.0d;
            if (powerMah != 0.0d) {
                powerAndDuration.powerMah = powerMah;
                if (powerAndDuration.powerPerKeyMah != null) {
                    java.util.Arrays.fill(powerAndDuration.powerPerKeyMah, 0.0d);
                    return;
                }
                return;
            }
        }
        if (this.mHasBluetoothPowerController) {
            powerAndDuration.powerMah = calculatePowerMah(rxTimeMs, txTimeMs, idleTimeMs);
            if (powerAndDuration.keys != null) {
                for (int i2 = 0; i2 < powerAndDuration.keys.length; i2++) {
                    android.os.BatteryConsumer.Key key2 = powerAndDuration.keys[i2];
                    int processState2 = key2.processState;
                    if (processState2 != 0) {
                        powerAndDuration.powerPerKeyMah[i2] = calculatePowerMah(rxTimeCounter.getCountForProcessState(processState2), txTimeCounter.getCountForProcessState(processState2), idleTimeCounter.getCountForProcessState(processState2));
                    }
                }
                return;
            }
            return;
        }
        powerAndDuration.powerMah = 0.0d;
        if (powerAndDuration.powerPerKeyMah != null) {
            java.util.Arrays.fill(powerAndDuration.powerPerKeyMah, 0.0d);
        }
    }

    public double calculatePowerMah(long rxTimeMs, long txTimeMs, long idleTimeMs) {
        return (((idleTimeMs * this.mIdleMa) + (rxTimeMs * this.mRxMa)) + (txTimeMs * this.mTxMa)) / 3600000.0d;
    }
}
