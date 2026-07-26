package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class MobileRadioPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final int IGNORE = -1;
    private static final double MILLIS_IN_HOUR = 3600000.0d;
    private static final java.lang.String TAG = "MobRadioPowerCalculator";
    private final com.android.server.power.stats.UsageBasedPowerEstimator mActivePowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mIdlePowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mIdlePowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[NUM_SIGNAL_STRENGTH_LEVELS];
    private final com.android.internal.os.PowerProfile mPowerProfile;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mScanPowerEstimator;
    private final com.android.server.power.stats.UsageBasedPowerEstimator mSleepPowerEstimator;
    private static final int NUM_SIGNAL_STRENGTH_LEVELS = android.telephony.CellSignalStrength.getNumSignalStrengthLevels();
    private static final android.os.BatteryConsumer.Key[] UNINITIALIZED_KEYS = new android.os.BatteryConsumer.Key[0];

    private static class PowerAndDuration {
        public long remainingDurationMs;
        public double remainingPowerMah;
        public long totalAppDurationMs;
        public double totalAppPowerMah;

        private PowerAndDuration() {
        }
    }

    public MobileRadioPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mPowerProfile = profile;
        double sleepDrainRateMa = this.mPowerProfile.getAverageBatteryDrainOrDefaultMa(4294967296L, Double.NaN);
        if (!java.lang.Double.isNaN(sleepDrainRateMa)) {
            this.mSleepPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(sleepDrainRateMa);
        } else {
            this.mSleepPowerEstimator = null;
        }
        double idleDrainRateMa = this.mPowerProfile.getAverageBatteryDrainOrDefaultMa(4563402752L, Double.NaN);
        if (!java.lang.Double.isNaN(idleDrainRateMa)) {
            this.mIdlePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(idleDrainRateMa);
        } else {
            this.mIdlePowerEstimator = null;
        }
        double powerRadioActiveMa = profile.getAveragePowerOrDefault("radio.active", Double.NaN);
        if (java.lang.Double.isNaN(powerRadioActiveMa)) {
            double sum = 0.0d + profile.getAveragePower("modem.controller.rx");
            for (int i = 0; i < NUM_SIGNAL_STRENGTH_LEVELS; i++) {
                sum += profile.getAveragePower("modem.controller.tx", i);
            }
            int i2 = NUM_SIGNAL_STRENGTH_LEVELS;
            powerRadioActiveMa = sum / ((double) (i2 + 1));
        }
        this.mActivePowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(powerRadioActiveMa);
        if (!java.lang.Double.isNaN(profile.getAveragePowerOrDefault("radio.on", Double.NaN))) {
            for (int i3 = 0; i3 < NUM_SIGNAL_STRENGTH_LEVELS; i3++) {
                this.mIdlePowerEstimators[i3] = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("radio.on", i3));
            }
        } else {
            double idle = profile.getAveragePower("modem.controller.idle");
            this.mIdlePowerEstimators[0] = new com.android.server.power.stats.UsageBasedPowerEstimator((25.0d * idle) / 180.0d);
            int i4 = 1;
            while (i4 < NUM_SIGNAL_STRENGTH_LEVELS) {
                this.mIdlePowerEstimators[i4] = new com.android.server.power.stats.UsageBasedPowerEstimator(java.lang.Math.max(1.0d, idle / 256.0d));
                i4++;
                sleepDrainRateMa = sleepDrainRateMa;
            }
        }
        this.mScanPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePowerOrDefault("radio.scanning", 0.0d));
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 8;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double totalActivePowerMah;
        java.util.ArrayList<android.os.UidBatteryConsumer.Builder> apps;
        android.util.LongArrayQueue appDurationsMs;
        com.android.server.power.stats.MobileRadioPowerCalculator.PowerAndDuration total;
        long totalActiveDurationMs;
        long totalActiveDurationMs2;
        int i;
        double appConsumptionMah;
        long totalActiveDurationMs3;
        com.android.server.power.stats.MobileRadioPowerCalculator.PowerAndDuration total2;
        double powerInStateMah;
        com.android.server.power.stats.MobileRadioPowerCalculator.PowerAndDuration total3 = new com.android.server.power.stats.MobileRadioPowerCalculator.PowerAndDuration();
        long totalConsumptionUC = batteryStats.getMobileRadioEnergyConsumptionUC();
        int powerModel = getPowerModel(totalConsumptionUC, query);
        if (powerModel == 2) {
            totalActivePowerMah = Double.NaN;
            apps = null;
            appDurationsMs = null;
        } else {
            totalActivePowerMah = calculateActiveModemPowerMah(batteryStats, rawRealtimeUs);
            apps = new java.util.ArrayList<>();
            appDurationsMs = new android.util.LongArrayQueue();
        }
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        android.os.BatteryConsumer.Key[] keys = UNINITIALIZED_KEYS;
        android.os.BatteryConsumer.Key[] keys2 = keys;
        int i2 = uidBatteryConsumerBuilders.size() - 1;
        while (i2 >= 0) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i2);
            android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders2 = uidBatteryConsumerBuilders;
            android.os.BatteryStats.Uid uid = app.getBatteryStatsUid();
            long totalConsumptionUC2 = totalConsumptionUC;
            if (keys2 == UNINITIALIZED_KEYS) {
                if (query.isProcessStateDataNeeded()) {
                    keys2 = app.getKeys(8);
                } else {
                    keys2 = null;
                }
            }
            double totalActivePowerMah2 = totalActivePowerMah;
            long radioActiveDurationMs = calculateDuration(uid, 0);
            if (!app.isVirtualUid()) {
                total3.totalAppDurationMs += radioActiveDurationMs;
            }
            app.setUsageDurationMillis(8, radioActiveDurationMs);
            if (powerModel == 2) {
                long appConsumptionUC = uid.getMobileRadioEnergyConsumptionUC();
                if (appConsumptionUC != -1) {
                    double appConsumptionMah2 = uCtoMah(appConsumptionUC);
                    if (!app.isVirtualUid()) {
                        total3.totalAppPowerMah += appConsumptionMah2;
                    }
                    app.setConsumedPower(8, appConsumptionMah2, powerModel);
                    if (query.isProcessStateDataNeeded() && keys2 != null) {
                        int length = keys2.length;
                        int i3 = 0;
                        while (i3 < length) {
                            android.os.BatteryConsumer.Key key = keys2[i3];
                            double appConsumptionMah3 = appConsumptionMah2;
                            int processState = key.processState;
                            if (processState != 0) {
                                long consumptionInStateUc = uid.getMobileRadioEnergyConsumptionUC(processState);
                                double powerInStateMah2 = uCtoMah(consumptionInStateUc);
                                app.setConsumedPower(key, powerInStateMah2, powerModel);
                            }
                            i3++;
                            appConsumptionMah2 = appConsumptionMah3;
                        }
                    }
                }
            } else {
                apps.add(app);
                appDurationsMs.addLast(radioActiveDurationMs);
            }
            i2--;
            uidBatteryConsumerBuilders = uidBatteryConsumerBuilders2;
            totalConsumptionUC = totalConsumptionUC2;
            totalActivePowerMah = totalActivePowerMah2;
        }
        long totalConsumptionUC3 = totalConsumptionUC;
        double totalActivePowerMah3 = totalActivePowerMah;
        long totalActiveDurationMs4 = batteryStats.getMobileRadioActiveTime(rawRealtimeUs, 0) / 1000;
        if (totalActiveDurationMs4 < total3.totalAppDurationMs) {
            totalActiveDurationMs4 = total3.totalAppDurationMs;
        }
        if (powerModel == 2) {
            total = total3;
            totalActiveDurationMs = totalActiveDurationMs4;
        } else {
            int appSize = apps.size();
            int i4 = 0;
            while (i4 < appSize) {
                android.os.UidBatteryConsumer.Builder app2 = apps.get(i4);
                long activeDurationMs = appDurationsMs.get(i4);
                java.util.ArrayList<android.os.UidBatteryConsumer.Builder> apps2 = apps;
                android.util.LongArrayQueue appDurationsMs2 = appDurationsMs;
                if (totalActiveDurationMs4 == 0.0d) {
                    appConsumptionMah = 0.0d;
                } else {
                    double appConsumptionMah4 = activeDurationMs;
                    appConsumptionMah = (appConsumptionMah4 * totalActivePowerMah3) / totalActiveDurationMs4;
                }
                if (!app2.isVirtualUid()) {
                    total3.totalAppPowerMah += appConsumptionMah;
                }
                app2.setConsumedPower(8, appConsumptionMah, powerModel);
                if (query.isProcessStateDataNeeded() && keys2 != null) {
                    android.os.BatteryStats.Uid uid2 = app2.getBatteryStatsUid();
                    int length2 = keys2.length;
                    int i5 = 0;
                    while (i5 < length2) {
                        int i6 = length2;
                        android.os.BatteryConsumer.Key key2 = keys2[i5];
                        int appSize2 = appSize;
                        int appSize3 = key2.processState;
                        if (appSize3 == 0) {
                            total2 = total3;
                            totalActiveDurationMs3 = totalActiveDurationMs4;
                        } else {
                            totalActiveDurationMs3 = totalActiveDurationMs4;
                            long totalActiveDurationMs5 = uid2.getMobileRadioActiveTimeInProcessState(appSize3) / 1000;
                            total2 = total3;
                            if (activeDurationMs == 0.0d) {
                                powerInStateMah = 0.0d;
                            } else {
                                double powerInStateMah3 = totalActiveDurationMs5;
                                powerInStateMah = (powerInStateMah3 * appConsumptionMah) / activeDurationMs;
                            }
                            app2.setConsumedPower(key2, powerInStateMah, powerModel);
                        }
                        i5++;
                        length2 = i6;
                        appSize = appSize2;
                        total3 = total2;
                        totalActiveDurationMs4 = totalActiveDurationMs3;
                    }
                }
                i4++;
                apps = apps2;
                appDurationsMs = appDurationsMs2;
                appSize = appSize;
                total3 = total3;
                totalActiveDurationMs4 = totalActiveDurationMs4;
            }
            total = total3;
            totalActiveDurationMs = totalActiveDurationMs4;
        }
        com.android.server.power.stats.MobileRadioPowerCalculator.PowerAndDuration total4 = total;
        total4.remainingDurationMs = totalActiveDurationMs - total4.totalAppDurationMs;
        if (powerModel == 2) {
            total4.remainingPowerMah = uCtoMah(totalConsumptionUC3) - total4.totalAppPowerMah;
            if (total4.remainingPowerMah < 0.0d) {
                total4.remainingPowerMah = 0.0d;
            }
        } else {
            if (totalActiveDurationMs == 0) {
                totalActiveDurationMs2 = totalActiveDurationMs;
            } else {
                totalActiveDurationMs2 = totalActiveDurationMs;
                total4.remainingPowerMah += (totalActivePowerMah3 * total4.remainingDurationMs) / totalActiveDurationMs2;
            }
            android.os.BatteryStats.ControllerActivityCounter modemActivity = batteryStats.getModemControllerActivity();
            double inactivePowerMah = Double.NaN;
            if (modemActivity == null) {
                i = 0;
            } else {
                i = 0;
                long sleepDurationMs = modemActivity.getSleepTimeCounter().getCountLocked(0);
                long idleDurationMs = modemActivity.getIdleTimeCounter().getCountLocked(0);
                inactivePowerMah = calcInactiveStatePowerMah(sleepDurationMs, idleDurationMs);
            }
            if (java.lang.Double.isNaN(inactivePowerMah)) {
                android.os.BatteryStats batteryStats2 = batteryStats;
                long scanningTimeMs = batteryStats2.getPhoneSignalScanningTime(rawRealtimeUs, i) / 1000;
                inactivePowerMah = calcScanTimePowerMah(scanningTimeMs);
                int i7 = 0;
                while (i7 < NUM_SIGNAL_STRENGTH_LEVELS) {
                    long strengthTimeMs = batteryStats2.getPhoneSignalStrengthTime(i7, rawRealtimeUs, 0) / 1000;
                    inactivePowerMah += calcIdlePowerAtSignalStrengthMah(strengthTimeMs, i7);
                    i7++;
                    batteryStats2 = batteryStats;
                    totalActiveDurationMs2 = totalActiveDurationMs2;
                }
            }
            if (!java.lang.Double.isNaN(inactivePowerMah)) {
                total4.remainingPowerMah += inactivePowerMah;
            }
        }
        if (total4.remainingPowerMah != 0.0d || total4.totalAppPowerMah != 0.0d) {
            builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(8, total4.remainingDurationMs + total4.totalAppDurationMs).setConsumedPower(8, total4.remainingPowerMah + total4.totalAppPowerMah, powerModel);
            builder.getAggregateBatteryConsumerBuilder(1).setUsageDurationMillis(8, total4.totalAppDurationMs).setConsumedPower(8, total4.totalAppPowerMah, powerModel);
        }
    }

    private long calculateDuration(android.os.BatteryStats.Uid u, int statsType) {
        return u.getMobileRadioActiveTime(statsType) / 1000;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0078  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private double calculateActiveModemPowerMah(android.os.BatteryStats r27, long r28) {
        /*
            r26 = this;
            r6 = r26
            r7 = r27
            r8 = r28
            r10 = 1000(0x3e8, double:4.94E-321)
            long r12 = r8 / r10
            int r14 = com.android.server.power.stats.MobileRadioPowerCalculator.NUM_SIGNAL_STRENGTH_LEVELS
            r0 = 0
            r2 = 0
            r3 = 0
            r15 = r3
        L11:
            r3 = 3
            if (r15 >= r3) goto L8d
            r3 = 2
            if (r15 != r3) goto L19
            r3 = 5
            goto L1a
        L19:
            r3 = 1
        L1a:
            r4 = r3
            r3 = 0
            r5 = r3
        L1d:
            if (r5 >= r4) goto L87
            r3 = 0
            r16 = r0
            r18 = r2
        L24:
            r19 = -1
            if (r3 >= r14) goto L5c
            r0 = r27
            r1 = r15
            r2 = r5
            r21 = r3
            r22 = r4
            r23 = r5
            r4 = r12
            long r24 = r0.getActiveTxRadioDurationMs(r1, r2, r3, r4)
            int r0 = (r24 > r19 ? 1 : (r24 == r19 ? 0 : -1))
            if (r0 != 0) goto L3c
            goto L55
        L3c:
            r0 = r26
            r1 = r15
            r2 = r23
            r3 = r21
            r4 = r24
            double r0 = r0.calcTxStatePowerMah(r1, r2, r3, r4)
            boolean r2 = java.lang.Double.isNaN(r0)
            if (r2 == 0) goto L50
            goto L55
        L50:
            r2 = 1
            double r16 = r16 + r0
            r18 = r2
        L55:
            int r3 = r21 + 1
            r4 = r22
            r5 = r23
            goto L24
        L5c:
            r21 = r3
            r22 = r4
            r23 = r5
            r3 = r23
            long r0 = r7.getActiveRxRadioDurationMs(r15, r3, r12)
            int r2 = (r0 > r19 ? 1 : (r0 == r19 ? 0 : -1))
            if (r2 != 0) goto L6d
            goto L78
        L6d:
            double r4 = r6.calcRxStatePowerMah(r15, r3, r0)
            boolean r2 = java.lang.Double.isNaN(r4)
            if (r2 == 0) goto L7d
        L78:
            r0 = r16
            r2 = r18
            goto L82
        L7d:
            r2 = 1
            double r16 = r16 + r4
            r0 = r16
        L82:
            int r5 = r3 + 1
            r4 = r22
            goto L1d
        L87:
            r22 = r4
            r3 = r5
            int r15 = r15 + 1
            goto L11
        L8d:
            if (r2 != 0) goto La2
            r3 = 0
            long r3 = r7.getMobileRadioActiveTime(r8, r3)
            long r3 = r3 / r10
            r10 = 0
            int r5 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
            if (r5 <= 0) goto La0
            double r0 = r6.calcPowerFromRadioActiveDurationMah(r3)
            goto La2
        La0:
            r0 = 0
        La2:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.MobileRadioPowerCalculator.calculateActiveModemPowerMah(android.os.BatteryStats, long):double");
    }

    public double calcRxStatePowerMah(int rat, int freqRange, long rxDurationMs) {
        long rxKey = com.android.internal.power.ModemPowerProfile.getAverageBatteryDrainKey(536870912, rat, freqRange, -1);
        double drainRateMa = this.mPowerProfile.getAverageBatteryDrainOrDefaultMa(rxKey, Double.NaN);
        if (java.lang.Double.isNaN(drainRateMa)) {
            android.util.Log.w(TAG, "Unavailable Power Profile constant for key 0x" + java.lang.Long.toHexString(rxKey));
            return Double.NaN;
        }
        double consumptionMah = (rxDurationMs * drainRateMa) / MILLIS_IN_HOUR;
        return consumptionMah;
    }

    public double calcTxStatePowerMah(int rat, int freqRange, int txLevel, long txDurationMs) {
        long txKey = com.android.internal.power.ModemPowerProfile.getAverageBatteryDrainKey(805306368, rat, freqRange, txLevel);
        double drainRateMa = this.mPowerProfile.getAverageBatteryDrainOrDefaultMa(txKey, Double.NaN);
        if (java.lang.Double.isNaN(drainRateMa)) {
            android.util.Log.w(TAG, "Unavailable Power Profile constant for key 0x" + java.lang.Long.toHexString(txKey));
            return Double.NaN;
        }
        double consumptionMah = (txDurationMs * drainRateMa) / MILLIS_IN_HOUR;
        return consumptionMah;
    }

    public double calcInactiveStatePowerMah(long sleepDurationMs, long idleDurationMs) {
        if (this.mSleepPowerEstimator == null || this.mIdlePowerEstimator == null) {
            return Double.NaN;
        }
        double sleepConsumptionMah = this.mSleepPowerEstimator.calculatePower(sleepDurationMs);
        double idleConsumptionMah = this.mIdlePowerEstimator.calculatePower(idleDurationMs);
        return sleepConsumptionMah + idleConsumptionMah;
    }

    public double calcPowerFromRadioActiveDurationMah(long radioActiveDurationMs) {
        return this.mActivePowerEstimator.calculatePower(radioActiveDurationMs);
    }

    public double calcIdlePowerAtSignalStrengthMah(long strengthTimeMs, int strengthLevel) {
        return this.mIdlePowerEstimators[strengthLevel].calculatePower(strengthTimeMs);
    }

    public double calcScanTimePowerMah(long scanningTimeMs) {
        return this.mScanPowerEstimator.calculatePower(scanningTimeMs);
    }
}
