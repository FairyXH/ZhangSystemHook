package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class SensorPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private final android.util.SparseArray<android.hardware.Sensor> mSensors;

    public SensorPowerCalculator(android.hardware.SensorManager sensorManager) {
        java.util.List<android.hardware.Sensor> sensors = sensorManager.getSensorList(-1);
        this.mSensors = new android.util.SparseArray<>(sensors.size());
        for (int i = 0; i < sensors.size(); i++) {
            android.hardware.Sensor sensor = sensors.get(i);
            this.mSensors.put(sensor.getHandle(), sensor);
        }
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return powerComponent == 9;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double appsPowerMah = 0.0d;
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            if (!app.isVirtualUid()) {
                appsPowerMah += calculateApp(app, app.getBatteryStatsUid(), rawRealtimeUs);
            }
        }
        builder.getAggregateBatteryConsumerBuilder(0).setConsumedPower(9, appsPowerMah);
        builder.getAggregateBatteryConsumerBuilder(1).setConsumedPower(9, appsPowerMah);
    }

    private double calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, long rawRealtimeUs) {
        double powerMah = calculatePowerMah(u, rawRealtimeUs, 0);
        app.setUsageDurationMillis(9, calculateDuration(u, rawRealtimeUs, 0)).setConsumedPower(9, powerMah);
        return powerMah;
    }

    private long calculateDuration(android.os.BatteryStats.Uid u, long rawRealtimeUs, int statsType) {
        long durationMs = 0;
        android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> sensorStats = u.getSensorStats();
        int NSE = sensorStats.size();
        for (int ise = 0; ise < NSE; ise++) {
            int sensorHandle = sensorStats.keyAt(ise);
            if (sensorHandle != -10000) {
                android.os.BatteryStats.Uid.Sensor sensor = (android.os.BatteryStats.Uid.Sensor) sensorStats.valueAt(ise);
                android.os.BatteryStats.Timer timer = sensor.getSensorTime();
                durationMs += timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
            }
        }
        return durationMs;
    }

    private double calculatePowerMah(android.os.BatteryStats.Uid u, long rawRealtimeUs, int statsType) {
        android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> sensorStats;
        int count;
        double powerMah = 0.0d;
        android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> sensorStats2 = u.getSensorStats();
        int count2 = sensorStats2.size();
        int ise = 0;
        while (ise < count2) {
            int sensorHandle = sensorStats2.keyAt(ise);
            if (sensorHandle == -10000) {
                sensorStats = sensorStats2;
                count = count2;
            } else {
                android.os.BatteryStats.Uid.Sensor sensor = (android.os.BatteryStats.Uid.Sensor) sensorStats2.valueAt(ise);
                android.os.BatteryStats.Timer timer = sensor.getSensorTime();
                long sensorTime = timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
                if (sensorTime != 0) {
                    android.hardware.Sensor s = this.mSensors.get(sensorHandle);
                    if (s == null) {
                        sensorStats = sensorStats2;
                        count = count2;
                    } else {
                        sensorStats = sensorStats2;
                        count = count2;
                        powerMah += (double) ((sensorTime * s.getPower()) / 3600000.0f);
                    }
                } else {
                    sensorStats = sensorStats2;
                    count = count2;
                }
            }
            ise++;
            sensorStats2 = sensorStats;
            count2 = count;
        }
        return powerMah;
    }
}
