package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class WakelockPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "WakelockPowerCalculator";
    private final com.android.server.power.stats.UsageBasedPowerEstimator mPowerEstimator;

    private static class PowerAndDuration {
        public long durationMs;
        public double powerMah;

        private PowerAndDuration() {
        }
    }

    public WakelockPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mPowerEstimator = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("cpu.idle"));
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 12;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        com.android.server.power.stats.WakelockPowerCalculator.PowerAndDuration result = new com.android.server.power.stats.WakelockPowerCalculator.PowerAndDuration();
        android.os.UidBatteryConsumer.Builder osBatteryConsumer = null;
        double appPowerMah = 0.0d;
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        double osPowerMah = 0.0d;
        long osDurationMs = 0;
        long totalAppDurationMs = 0;
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            calculateApp(result, app.getBatteryStatsUid(), rawRealtimeUs, 0);
            app.setUsageDurationMillis(12, result.durationMs).setConsumedPower(12, result.powerMah);
            if (!app.isVirtualUid()) {
                totalAppDurationMs += result.durationMs;
                appPowerMah += result.powerMah;
            }
            if (app.getUid() == 0) {
                long osDurationMs2 = result.durationMs;
                osDurationMs = osDurationMs2;
                osPowerMah = result.powerMah;
                osBatteryConsumer = app;
            }
        }
        double appPowerMah2 = appPowerMah;
        long totalAppDurationMs2 = totalAppDurationMs;
        long totalAppDurationMs3 = osDurationMs;
        calculateRemaining(result, batteryStats, rawRealtimeUs, rawUptimeUs, 0, osPowerMah, totalAppDurationMs3, totalAppDurationMs2);
        double remainingPowerMah = result.powerMah;
        if (osBatteryConsumer != null) {
            osBatteryConsumer.setUsageDurationMillis(12, result.durationMs).setConsumedPower(12, remainingPowerMah);
        }
        long wakeTimeMs = calculateWakeTimeMillis(batteryStats, rawRealtimeUs, rawUptimeUs);
        if (wakeTimeMs < 0) {
            wakeTimeMs = 0;
        }
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(12, wakeTimeMs).setConsumedPower(12, appPowerMah2 + remainingPowerMah);
        builder.getAggregateBatteryConsumerBuilder(1).setUsageDurationMillis(12, totalAppDurationMs2).setConsumedPower(12, appPowerMah2);
    }

    private void calculateApp(com.android.server.power.stats.WakelockPowerCalculator.PowerAndDuration result, android.os.BatteryStats.Uid u, long rawRealtimeUs, int statsType) {
        long wakeLockTimeUs = 0;
        android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Wakelock> wakelockStats = u.getWakelockStats();
        int wakelockStatsCount = wakelockStats.size();
        for (int i = 0; i < wakelockStatsCount; i++) {
            android.os.BatteryStats.Uid.Wakelock wakelock = (android.os.BatteryStats.Uid.Wakelock) wakelockStats.valueAt(i);
            android.os.BatteryStats.Timer timer = wakelock.getWakeTime(0);
            if (timer != null) {
                wakeLockTimeUs += timer.getTotalTimeLocked(rawRealtimeUs, statsType);
            }
        }
        result.durationMs = wakeLockTimeUs / 1000;
        result.powerMah = this.mPowerEstimator.calculatePower(result.durationMs);
    }

    private void calculateRemaining(com.android.server.power.stats.WakelockPowerCalculator.PowerAndDuration result, android.os.BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType, double osPowerMah, long osDurationMs, long totalAppDurationMs) {
        long wakeTimeMillis = calculateWakeTimeMillis(stats, rawRealtimeUs, rawUptimeUs) - totalAppDurationMs;
        if (wakeTimeMillis <= 0) {
            result.durationMs = 0L;
            result.powerMah = 0.0d;
        } else {
            double power = this.mPowerEstimator.calculatePower(wakeTimeMillis);
            result.durationMs = osDurationMs + wakeTimeMillis;
            result.powerMah = osPowerMah + power;
        }
    }

    private long calculateWakeTimeMillis(android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs) {
        long batteryUptimeUs = batteryStats.getBatteryUptime(rawUptimeUs);
        long screenOnTimeUs = batteryStats.getScreenOnTime(rawRealtimeUs, 0);
        return (batteryUptimeUs - screenOnTimeUs) / 1000;
    }
}
