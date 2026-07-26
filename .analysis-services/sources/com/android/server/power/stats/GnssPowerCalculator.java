package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class GnssPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final double mAveragePowerGnssOn;
    private final double[] mAveragePowerPerSignalQuality = new double[2];

    public GnssPowerCalculator(com.android.internal.os.PowerProfile profile) {
        this.mAveragePowerGnssOn = profile.getAveragePowerOrDefault("gps.on", -1.0d);
        for (int i = 0; i < 2; i++) {
            this.mAveragePowerPerSignalQuality[i] = profile.getAveragePower("gps.signalqualitybased", i);
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 10;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double powerMah;
        double appsPowerMah = 0.0d;
        double averageGnssPowerMa = getAverageGnssPower(batteryStats, rawRealtimeUs, 0);
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        int i = uidBatteryConsumerBuilders.size() - 1;
        while (i >= 0) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            long consumptionUC = app.getBatteryStatsUid().getGnssEnergyConsumptionUC();
            int i2 = i;
            android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders2 = uidBatteryConsumerBuilders;
            double powerMah2 = calculateApp(app, app.getBatteryStatsUid(), getPowerModel(consumptionUC, query), rawRealtimeUs, averageGnssPowerMa, consumptionUC);
            if (!app.isVirtualUid()) {
                appsPowerMah += powerMah2;
            }
            i = i2 - 1;
            uidBatteryConsumerBuilders = uidBatteryConsumerBuilders2;
        }
        long consumptionUC2 = batteryStats.getGnssEnergyConsumptionUC();
        int powerModel = getPowerModel(consumptionUC2, query);
        if (powerModel == 2) {
            powerMah = uCtoMah(consumptionUC2);
        } else {
            powerMah = appsPowerMah;
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(10, powerMah, powerModel);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(10, appsPowerMah, powerModel);
    }

    private double calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, int powerModel, long rawRealtimeUs, double averageGnssPowerMa, long consumedEnergyUC) {
        double powerMah;
        long durationMs = computeDuration(u, rawRealtimeUs, 0);
        switch (powerModel) {
            case 2:
                powerMah = uCtoMah(consumedEnergyUC);
                break;
            default:
                powerMah = computePower(durationMs, averageGnssPowerMa);
                break;
        }
        app.setUsageDurationMillis(10, durationMs).setConsumedPower(10, powerMah, powerModel);
        return powerMah;
    }

    private long computeDuration(android.os.BatteryStats.Uid u, long rawRealtimeUs, int statsType) {
        android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> sensorStats = u.getSensorStats();
        android.os.BatteryStats.Uid.Sensor sensor = (android.os.BatteryStats.Uid.Sensor) sensorStats.get(-10000);
        if (sensor == null) {
            return 0L;
        }
        android.os.BatteryStats.Timer timer = sensor.getSensorTime();
        return timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
    }

    private double computePower(long sensorTime, double averageGnssPowerMa) {
        return (sensorTime * averageGnssPowerMa) / 3600000.0d;
    }

    private double getAverageGnssPower(android.os.BatteryStats stats, long rawRealtimeUs, int statsType) {
        com.android.server.power.stats.GnssPowerCalculator gnssPowerCalculator = this;
        double averagePower = gnssPowerCalculator.mAveragePowerGnssOn;
        if (averagePower != -1.0d) {
            return averagePower;
        }
        double averagePower2 = 0.0d;
        long totalTime = 0;
        double totalPower = 0.0d;
        int i = 0;
        while (i < 2) {
            long timePerLevel = stats.getGpsSignalQualityTime(i, rawRealtimeUs, statsType) / 1000;
            totalTime += timePerLevel;
            totalPower += gnssPowerCalculator.mAveragePowerPerSignalQuality[i] * timePerLevel;
            i++;
            gnssPowerCalculator = this;
            averagePower2 = averagePower2;
        }
        double averagePower3 = averagePower2;
        if (totalTime == 0) {
            return averagePower3;
        }
        return totalPower / totalTime;
    }
}
