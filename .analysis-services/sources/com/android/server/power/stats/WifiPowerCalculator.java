package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class WifiPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "WifiPowerCalculator";
    private static final android.os.BatteryConsumer.Key[] UNINITIALIZED_KEYS = new android.os.BatteryConsumer.Key[0];
    private final com.android.server.power.stats.UsageBasedPowerEstimator mBatchScanPowerEstimator;
    private final boolean mHasWifiPowerController;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mIdlePowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mPowerOnPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mRxPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mScanPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mTxPowerEstimator;
    private final double mWifiPowerPerPacket;

    private static class PowerDurationAndTraffic {
        public long durationMs;
        public android.os.BatteryConsumer.Key[] keys;
        public double powerMah;
        public double[] powerPerKeyMah;
        public long wifiRxBytes;
        public long wifiRxPackets;
        public long wifiTxBytes;
        public long wifiTxPackets;

        private PowerDurationAndTraffic() {
        }
    }

    public WifiPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mPowerOnPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("wifi.on"));
        this.mScanPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("wifi.scan"));
        this.mBatchScanPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("wifi.batchedscan"));
        this.mIdlePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("wifi.controller.idle"));
        this.mTxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("wifi.controller.tx"));
        this.mRxPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("wifi.controller.rx"));
        this.mWifiPowerPerPacket = getWifiPowerPerPacket(profile);
        this.mHasWifiPowerController = this.mIdlePowerEstimator.isSupported() && this.mTxPowerEstimator.isSupported() && this.mRxPowerEstimator.isSupported();
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 11;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        android.os.BatteryConsumer.Key[] keys = UNINITIALIZED_KEYS;
        long totalAppDurationMs = 0;
        double totalAppPowerMah = 0.0d;
        com.android.server.power.stats.WifiPowerCalculator.PowerDurationAndTraffic powerDurationAndTraffic = new com.android.server.power.stats.WifiPowerCalculator.PowerDurationAndTraffic();
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        int i = uidBatteryConsumerBuilders.size() - 1;
        while (i >= 0) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            if (keys == UNINITIALIZED_KEYS) {
                if (query.isProcessStateDataNeeded()) {
                    keys = app.getKeys(11);
                    powerDurationAndTraffic.keys = keys;
                    powerDurationAndTraffic.powerPerKeyMah = new double[keys.length];
                } else {
                    keys = null;
                }
            }
            long consumptionUC = app.getBatteryStatsUid().getWifiEnergyConsumptionUC();
            int powerModel = getPowerModel(consumptionUC, query);
            android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders2 = uidBatteryConsumerBuilders;
            int i2 = i;
            calculateApp(powerDurationAndTraffic, app.getBatteryStatsUid(), powerModel, rawRealtimeUs, 0, batteryStats.hasWifiActivityReporting(), consumptionUC);
            if (!app.isVirtualUid()) {
                totalAppDurationMs += powerDurationAndTraffic.durationMs;
                totalAppPowerMah += powerDurationAndTraffic.powerMah;
            }
            app.setUsageDurationMillis(11, powerDurationAndTraffic.durationMs);
            app.setConsumedPower(11, powerDurationAndTraffic.powerMah, powerModel);
            if (query.isProcessStateDataNeeded() && keys != null) {
                for (int j = 0; j < keys.length; j++) {
                    android.os.BatteryConsumer.Key key = keys[j];
                    int processState = key.processState;
                    if (processState != 0) {
                        app.setConsumedPower(key, powerDurationAndTraffic.powerPerKeyMah[j], powerModel);
                    }
                }
            }
            i = i2 - 1;
            uidBatteryConsumerBuilders = uidBatteryConsumerBuilders2;
        }
        long consumptionUC2 = batteryStats.getWifiEnergyConsumptionUC();
        int powerModel2 = getPowerModel(consumptionUC2, query);
        calculateRemaining(powerDurationAndTraffic, powerModel2, batteryStats, rawRealtimeUs, 0, batteryStats.hasWifiActivityReporting(), totalAppDurationMs, totalAppPowerMah, consumptionUC2);
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(11, powerDurationAndTraffic.durationMs).setConsumedPower(11, powerDurationAndTraffic.powerMah + totalAppPowerMah, powerModel2);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(11, totalAppPowerMah, powerModel2);
    }

    private void calculateApp(com.android.server.power.stats.WifiPowerCalculator.PowerDurationAndTraffic powerDurationAndTraffic, android.os.BatteryStats.Uid u, int powerModel, long rawRealtimeUs, int statsType, boolean hasWifiActivityReporting, long consumptionUC) {
        android.os.BatteryStats.LongCounter idleTimeCounter;
        int i;
        powerDurationAndTraffic.wifiRxPackets = u.getNetworkActivityPackets(2, statsType);
        powerDurationAndTraffic.wifiTxPackets = u.getNetworkActivityPackets(3, statsType);
        powerDurationAndTraffic.wifiRxBytes = u.getNetworkActivityBytes(2, statsType);
        powerDurationAndTraffic.wifiTxBytes = u.getNetworkActivityBytes(3, statsType);
        int i2 = 1;
        if (hasWifiActivityReporting && this.mHasWifiPowerController) {
            android.os.BatteryStats.ControllerActivityCounter counter = u.getWifiControllerActivity();
            if (counter == null) {
                powerDurationAndTraffic.durationMs = 0L;
                powerDurationAndTraffic.powerMah = 0.0d;
                if (powerDurationAndTraffic.powerPerKeyMah != null) {
                    java.util.Arrays.fill(powerDurationAndTraffic.powerPerKeyMah, 0.0d);
                    return;
                }
                return;
            }
            android.os.BatteryStats.LongCounter rxTimeCounter = counter.getRxTimeCounter();
            android.os.BatteryStats.LongCounter txTimeCounter = counter.getTxTimeCounters()[0];
            android.os.BatteryStats.LongCounter idleTimeCounter2 = counter.getIdleTimeCounter();
            long rxTime = rxTimeCounter.getCountLocked(statsType);
            long txTime = txTimeCounter.getCountLocked(statsType);
            long idleTime = idleTimeCounter2.getCountLocked(statsType);
            powerDurationAndTraffic.durationMs = idleTime + rxTime + txTime;
            if (powerModel == 1) {
                idleTimeCounter = idleTimeCounter2;
                powerDurationAndTraffic.powerMah = calcPowerFromControllerDataMah(rxTime, txTime, idleTime);
            } else {
                idleTimeCounter = idleTimeCounter2;
                powerDurationAndTraffic.powerMah = uCtoMah(consumptionUC);
            }
            if (powerDurationAndTraffic.keys != null) {
                int i3 = 0;
                while (i3 < powerDurationAndTraffic.keys.length) {
                    int processState = powerDurationAndTraffic.keys[i3].processState;
                    if (processState == 0) {
                        i = i3;
                    } else if (powerModel == i2) {
                        i = i3;
                        powerDurationAndTraffic.powerPerKeyMah[i] = calcPowerFromControllerDataMah(rxTimeCounter.getCountForProcessState(processState), txTimeCounter.getCountForProcessState(processState), idleTimeCounter.getCountForProcessState(processState));
                    } else {
                        i = i3;
                        powerDurationAndTraffic.powerPerKeyMah[i] = uCtoMah(u.getWifiEnergyConsumptionUC(processState));
                    }
                    i3 = i + 1;
                    i2 = 1;
                }
                return;
            }
            return;
        }
        long wifiRunningTime = u.getWifiRunningTime(rawRealtimeUs, statsType) / 1000;
        powerDurationAndTraffic.durationMs = wifiRunningTime;
        if (powerModel != 1) {
            powerDurationAndTraffic.powerMah = uCtoMah(consumptionUC);
        } else {
            long wifiScanTimeMs = u.getWifiScanTime(rawRealtimeUs, statsType) / 1000;
            long batchTimeMs = 0;
            for (int bin = 0; bin < 5; bin++) {
                batchTimeMs += u.getWifiBatchedScanTime(bin, rawRealtimeUs, statsType) / 1000;
            }
            powerDurationAndTraffic.powerMah = calcPowerWithoutControllerDataMah(powerDurationAndTraffic.wifiRxPackets, powerDurationAndTraffic.wifiTxPackets, wifiRunningTime, wifiScanTimeMs, batchTimeMs);
        }
        if (powerDurationAndTraffic.powerPerKeyMah != null) {
            java.util.Arrays.fill(powerDurationAndTraffic.powerPerKeyMah, 0.0d);
        }
    }

    private void calculateRemaining(com.android.server.power.stats.WifiPowerCalculator.PowerDurationAndTraffic powerDurationAndTraffic, int powerModel, android.os.BatteryStats stats, long rawRealtimeUs, int statsType, boolean hasWifiActivityReporting, long totalAppDurationMs, double totalAppPowerMah, long consumptionUC) {
        long totalDurationMs;
        double totalPowerMah = powerModel == 2 ? uCtoMah(consumptionUC) : 0.0d;
        if (hasWifiActivityReporting && this.mHasWifiPowerController) {
            android.os.BatteryStats.ControllerActivityCounter counter = stats.getWifiControllerActivity();
            long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(statsType);
            long txTimeMs = counter.getTxTimeCounters()[0].getCountLocked(statsType);
            long rxTimeMs = counter.getRxTimeCounter().getCountLocked(statsType);
            totalDurationMs = idleTimeMs + rxTimeMs + txTimeMs;
            if (powerModel == 1) {
                double totalPowerMah2 = counter.getPowerCounter().getCountLocked(statsType) / 3600000.0d;
                totalPowerMah = totalPowerMah2 == 0.0d ? calcPowerFromControllerDataMah(rxTimeMs, txTimeMs, idleTimeMs) : totalPowerMah2;
            }
        } else {
            long totalDurationMs2 = stats.getGlobalWifiRunningTime(rawRealtimeUs, statsType) / 1000;
            if (powerModel != 1) {
                totalDurationMs = totalDurationMs2;
            } else {
                totalPowerMah = calcGlobalPowerWithoutControllerDataMah(totalDurationMs2);
                totalDurationMs = totalDurationMs2;
            }
        }
        powerDurationAndTraffic.durationMs = java.lang.Math.max(0L, totalDurationMs - totalAppDurationMs);
        powerDurationAndTraffic.powerMah = java.lang.Math.max(0.0d, totalPowerMah - totalAppPowerMah);
    }

    public double calcPowerFromControllerDataMah(long rxTimeMs, long txTimeMs, long idleTimeMs) {
        return this.mRxPowerEstimator.calculatePower(rxTimeMs) + this.mTxPowerEstimator.calculatePower(txTimeMs) + this.mIdlePowerEstimator.calculatePower(idleTimeMs);
    }

    public double calcPowerWithoutControllerDataMah(long rxPackets, long txPackets, long wifiRunningTimeMs, long wifiScanTimeMs, long wifiBatchScanTimeMs) {
        return ((rxPackets + txPackets) * this.mWifiPowerPerPacket) + this.mPowerOnPowerEstimator.calculatePower(wifiRunningTimeMs) + this.mScanPowerEstimator.calculatePower(wifiScanTimeMs) + this.mBatchScanPowerEstimator.calculatePower(wifiBatchScanTimeMs);
    }

    public double calcGlobalPowerWithoutControllerDataMah(long globalWifiRunningTimeMs) {
        return this.mPowerOnPowerEstimator.calculatePower(globalWifiRunningTimeMs);
    }

    private static double getWifiPowerPerPacket(com.android.internal.os.PowerProfile profile) {
        double averageWifiActivePower = profile.getAveragePower("wifi.active") / 3600.0d;
        return averageWifiActivePower / 61.03515625d;
    }
}
