package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class ScreenPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    public static final long MIN_ACTIVE_TIME_FOR_SMEARING = 600000;
    private static final java.lang.String TAG = "ScreenPowerCalculator";
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mScreenFullPowerEstimators;
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mScreenOnPowerEstimators;

    private static class PowerAndDuration {
        public long durationMs;
        public double powerMah;

        private PowerAndDuration() {
        }
    }

    public ScreenPowerCalculator(com.android.internal.os.PowerProfile powerProfile) {
        int numDisplays = powerProfile.getNumDisplays();
        this.mScreenOnPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[numDisplays];
        this.mScreenFullPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[numDisplays];
        for (int display = 0; display < numDisplays; display++) {
            this.mScreenOnPowerEstimators[display] = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePowerForOrdinal("screen.on.display", display));
            this.mScreenFullPowerEstimators[display] = new com.android.server.power.stats.UsageBasedPowerEstimator(powerProfile.getAveragePowerForOrdinal("screen.full.display", display));
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 0;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        com.android.server.power.stats.ScreenPowerCalculator screenPowerCalculator = this;
        long j = rawRealtimeUs;
        com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration totalPowerAndDuration = new com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration();
        long consumptionUC = batteryStats.getScreenOnEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUC, query);
        calculateTotalDurationAndPower(totalPowerAndDuration, powerModel, batteryStats, rawRealtimeUs, 0, consumptionUC);
        double totalAppPower = 0.0d;
        long totalAppDuration = 0;
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        switch (powerModel) {
            case 2:
                com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration appPowerAndDuration = new com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration();
                int i = uidBatteryConsumerBuilders.size() - 1;
                while (i >= 0) {
                    android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
                    screenPowerCalculator.calculateAppUsingEnergyConsumption(appPowerAndDuration, app.getBatteryStatsUid(), j);
                    app.setUsageDurationMillis(0, appPowerAndDuration.durationMs).setConsumedPower(0, appPowerAndDuration.powerMah, powerModel);
                    if (!app.isVirtualUid()) {
                        totalAppPower += appPowerAndDuration.powerMah;
                        totalAppDuration += appPowerAndDuration.durationMs;
                    }
                    i--;
                    screenPowerCalculator = this;
                    j = rawRealtimeUs;
                }
                break;
            default:
                smearScreenBatteryDrain(uidBatteryConsumerBuilders, totalPowerAndDuration, rawRealtimeUs);
                totalAppPower = totalPowerAndDuration.powerMah;
                totalAppDuration = totalPowerAndDuration.durationMs;
                break;
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(0, java.lang.Math.max(totalPowerAndDuration.powerMah, totalAppPower), powerModel).setUsageDurationMillis(0, totalPowerAndDuration.durationMs);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(0, totalAppPower, powerModel).setUsageDurationMillis(0, totalAppDuration);
    }

    private void calculateTotalDurationAndPower(com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration totalPowerAndDuration, int powerModel, android.os.BatteryStats batteryStats, long rawRealtimeUs, int statsType, long consumptionUC) {
        totalPowerAndDuration.durationMs = calculateDuration(batteryStats, rawRealtimeUs, statsType);
        switch (powerModel) {
            case 2:
                totalPowerAndDuration.powerMah = uCtoMah(consumptionUC);
                break;
            default:
                totalPowerAndDuration.powerMah = calculateTotalPowerFromBrightness(batteryStats, rawRealtimeUs);
                break;
        }
    }

    private void calculateAppUsingEnergyConsumption(com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration appPowerAndDuration, android.os.BatteryStats.Uid u, long rawRealtimeUs) {
        appPowerAndDuration.durationMs = getProcessForegroundTimeMs(u, rawRealtimeUs);
        long chargeUC = u.getScreenOnEnergyConsumptionUC();
        if (chargeUC < 0) {
            android.util.Slog.wtf(TAG, "Screen energy not supported, so calculateApp shouldn't de called");
            appPowerAndDuration.powerMah = 0.0d;
        } else {
            appPowerAndDuration.powerMah = uCtoMah(chargeUC);
        }
    }

    private long calculateDuration(android.os.BatteryStats batteryStats, long rawRealtimeUs, int statsType) {
        return batteryStats.getScreenOnTime(rawRealtimeUs, statsType) / 1000;
    }

    private double calculateTotalPowerFromBrightness(android.os.BatteryStats batteryStats, long rawRealtimeUs) {
        int numDisplays = this.mScreenOnPowerEstimators.length;
        double power = 0.0d;
        for (int display = 0; display < numDisplays; display++) {
            long j = 1000;
            long displayTime = batteryStats.getDisplayScreenOnTime(display, rawRealtimeUs) / 1000;
            power += this.mScreenOnPowerEstimators[display].calculatePower(displayTime);
            int bin = 0;
            while (bin < 5) {
                long brightnessTime = batteryStats.getDisplayScreenBrightnessTime(display, bin, rawRealtimeUs) / j;
                double binPowerMah = (this.mScreenFullPowerEstimators[display].calculatePower(brightnessTime) * ((double) (bin + 0.5f))) / 5.0d;
                power += binPowerMah;
                bin++;
                j = 1000;
            }
        }
        return power;
    }

    private void smearScreenBatteryDrain(android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders, com.android.server.power.stats.ScreenPowerCalculator.PowerAndDuration totalPowerAndDuration, long rawRealtimeUs) {
        long totalActivityTimeMs = 0;
        android.util.SparseLongArray activityTimeArray = new android.util.SparseLongArray();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            android.os.BatteryStats.Uid uid = app.getBatteryStatsUid();
            long timeMs = getProcessForegroundTimeMs(uid, rawRealtimeUs);
            activityTimeArray.put(uid.getUid(), timeMs);
            if (!app.isVirtualUid()) {
                totalActivityTimeMs += timeMs;
            }
        }
        if (totalActivityTimeMs >= 600000) {
            double totalScreenPowerMah = totalPowerAndDuration.powerMah;
            int i2 = uidBatteryConsumerBuilders.size() - 1;
            while (i2 >= 0) {
                android.os.UidBatteryConsumer.Builder app2 = uidBatteryConsumerBuilders.valueAt(i2);
                long durationMs = activityTimeArray.get(app2.getUid(), 0L);
                android.util.SparseLongArray activityTimeArray2 = activityTimeArray;
                double powerMah = (durationMs * totalScreenPowerMah) / totalActivityTimeMs;
                app2.setUsageDurationMillis(0, durationMs).setConsumedPower(0, powerMah, 1);
                i2--;
                activityTimeArray = activityTimeArray2;
                totalScreenPowerMah = totalScreenPowerMah;
            }
        }
    }

    public long getProcessForegroundTimeMs(android.os.BatteryStats.Uid uid, long rawRealTimeUs) {
        int[] foregroundTypes = {0};
        long timeUs = 0;
        for (int type : foregroundTypes) {
            long localTime = uid.getProcessStateTime(type, rawRealTimeUs, 0);
            timeUs += localTime;
        }
        return java.lang.Math.min(timeUs, getForegroundActivityTotalTimeUs(uid, rawRealTimeUs)) / 1000;
    }

    public long getForegroundActivityTotalTimeUs(android.os.BatteryStats.Uid uid, long rawRealtimeUs) {
        android.os.BatteryStats.Timer timer = uid.getForegroundActivityTimer();
        if (timer == null) {
            return 0L;
        }
        return timer.getTotalTimeLocked(rawRealtimeUs, 0);
    }
}
