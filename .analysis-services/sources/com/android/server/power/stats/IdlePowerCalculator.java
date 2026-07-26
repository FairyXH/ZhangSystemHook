package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class IdlePowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "IdlePowerCalculator";
    private final double mAveragePowerCpuIdleMahPerUs;
    private final double mAveragePowerCpuSuspendMahPerUs;
    public long mDurationMs;
    public double mPowerMah;

    public IdlePowerCalculator(com.android.internal.os.PowerProfile powerProfile) {
        this.mAveragePowerCpuSuspendMahPerUs = powerProfile.getAveragePower("cpu.suspend") / 3.6E9d;
        this.mAveragePowerCpuIdleMahPerUs = powerProfile.getAveragePower("cpu.idle") / 3.6E9d;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 16;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        calculatePowerAndDuration(batteryStats, rawRealtimeUs, rawUptimeUs, 0);
        if (this.mPowerMah != 0.0d) {
            builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(16, this.mPowerMah).setUsageDurationMillis(16, this.mDurationMs);
        }
    }

    private void calculatePowerAndDuration(android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        long batteryRealtimeUs = batteryStats.computeBatteryRealtime(rawRealtimeUs, statsType);
        long batteryUptimeUs = batteryStats.computeBatteryUptime(rawUptimeUs, statsType);
        double suspendPowerMah = batteryRealtimeUs * this.mAveragePowerCpuSuspendMahPerUs;
        double idlePowerMah = batteryUptimeUs * this.mAveragePowerCpuIdleMahPerUs;
        this.mPowerMah = suspendPowerMah + idlePowerMah;
        this.mDurationMs = batteryRealtimeUs / 1000;
    }
}
