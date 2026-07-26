package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PowerCalculator {
    protected static final boolean DEBUG = false;
    protected static final double MILLIAMPHOUR_PER_MICROCOULOMB = 2.777777777777778E-7d;

    public abstract boolean isPowerComponentSupported(int i);

    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            calculateApp(app, app.getBatteryStatsUid(), rawRealtimeUs, rawUptimeUs, query);
        }
    }

    protected void calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
    }

    public void reset() {
    }

    protected static int getPowerModel(long consumedEnergyUC, android.os.BatteryUsageStatsQuery query) {
        if (consumedEnergyUC != -1 && !query.shouldForceUsePowerProfileModel()) {
            return 2;
        }
        return 1;
    }

    protected static int getPowerModel(long consumedEnergyUC) {
        if (consumedEnergyUC != -1) {
            return 2;
        }
        return 1;
    }

    public static double uCtoMah(long chargeUC) {
        return chargeUC * MILLIAMPHOUR_PER_MICROCOULOMB;
    }
}
