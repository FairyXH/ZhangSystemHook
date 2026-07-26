package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class MemoryPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    public static final java.lang.String TAG = "MemoryPowerCalculator";
    private final com.android.server.power.stats.UsageBasedPowerEstimator[] mPowerEstimators;

    public MemoryPowerCalculator(com.android.internal.os.PowerProfile profile) {
        int numBuckets = profile.getNumElements("memory.bandwidths");
        this.mPowerEstimators = new com.android.server.power.stats.UsageBasedPowerEstimator[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            this.mPowerEstimators[i] = new com.android.server.power.stats.UsageBasedPowerEstimator(profile.getAveragePower("memory.bandwidths", i));
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 13;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        long durationMs = calculateDuration(batteryStats, rawRealtimeUs, 0);
        double powerMah = calculatePower(batteryStats, rawRealtimeUs, 0);
        builder.getAggregateBatteryConsumerBuilder(0).setUsageDurationMillis(13, durationMs).setConsumedPower(13, powerMah);
    }

    private long calculateDuration(android.os.BatteryStats batteryStats, long rawRealtimeUs, int statsType) {
        long usageDurationMs = 0;
        android.util.LongSparseArray<? extends android.os.BatteryStats.Timer> timers = batteryStats.getKernelMemoryStats();
        for (int i = 0; i < timers.size() && i < this.mPowerEstimators.length; i++) {
            usageDurationMs += this.mPowerEstimators[i].calculateDuration((android.os.BatteryStats.Timer) timers.valueAt(i), rawRealtimeUs, statsType);
        }
        return usageDurationMs;
    }

    private double calculatePower(android.os.BatteryStats batteryStats, long rawRealtimeUs, int statsType) {
        double powerMah = 0.0d;
        android.util.LongSparseArray<? extends android.os.BatteryStats.Timer> timers = batteryStats.getKernelMemoryStats();
        for (int i = 0; i < timers.size() && i < this.mPowerEstimators.length; i++) {
            com.android.server.power.stats.UsageBasedPowerEstimator estimator = this.mPowerEstimators[(int) timers.keyAt(i)];
            long usageDurationMs = estimator.calculateDuration((android.os.BatteryStats.Timer) timers.valueAt(i), rawRealtimeUs, statsType);
            powerMah += estimator.calculatePower(usageDurationMs);
        }
        return powerMah;
    }
}
