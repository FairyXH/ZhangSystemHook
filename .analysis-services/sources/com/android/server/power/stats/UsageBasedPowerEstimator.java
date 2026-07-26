package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class UsageBasedPowerEstimator {
    private static final double MILLIS_IN_HOUR = 3600000.0d;
    private final double mAveragePowerMahPerMs;

    public UsageBasedPowerEstimator(double averagePowerMilliAmp) {
        this.mAveragePowerMahPerMs = averagePowerMilliAmp / MILLIS_IN_HOUR;
    }

    public boolean isSupported() {
        return this.mAveragePowerMahPerMs != 0.0d;
    }

    public long calculateDuration(android.os.BatteryStats.Timer timer, long rawRealtimeUs, int statsType) {
        if (timer == null) {
            return 0L;
        }
        return timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
    }

    public double calculatePower(long durationMs) {
        return this.mAveragePowerMahPerMs * durationMs;
    }
}
