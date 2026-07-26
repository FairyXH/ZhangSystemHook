package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CustomEnergyConsumerPowerCalculator extends com.android.server.power.stats.PowerCalculator {
    private static final java.lang.String TAG = "CustomEnergyCsmrPowerCalc";

    public CustomEnergyConsumerPowerCalculator(com.android.internal.os.PowerProfile powerProfile) {
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public boolean isPowerComponentSupported(int powerComponent) {
        return false;
    }

    @Override // com.android.server.power.stats.PowerCalculator
    public void calculate(android.os.BatteryUsageStats.Builder builder, android.os.BatteryStats batteryStats, long rawRealtimeUs, long rawUptimeUs, android.os.BatteryUsageStatsQuery query) {
        double[] totalAppPowerMah = null;
        android.util.SparseArray<android.os.UidBatteryConsumer.Builder> uidBatteryConsumerBuilders = builder.getUidBatteryConsumerBuilders();
        for (int i = uidBatteryConsumerBuilders.size() - 1; i >= 0; i--) {
            android.os.UidBatteryConsumer.Builder app = uidBatteryConsumerBuilders.valueAt(i);
            totalAppPowerMah = calculateApp(app, app.getBatteryStatsUid(), totalAppPowerMah);
        }
        double[] customEnergyConsumerPowerMah = uCtoMah(batteryStats.getCustomEnergyConsumerBatteryConsumptionUC());
        if (customEnergyConsumerPowerMah != null) {
            android.os.AggregateBatteryConsumer.Builder deviceBatteryConsumerBuilder = builder.getAggregateBatteryConsumerBuilder(0);
            for (int i2 = 0; i2 < customEnergyConsumerPowerMah.length; i2++) {
                deviceBatteryConsumerBuilder.setConsumedPowerForCustomComponent(i2 + 1000, customEnergyConsumerPowerMah[i2]);
            }
        }
        if (totalAppPowerMah != null) {
            android.os.AggregateBatteryConsumer.Builder appsBatteryConsumerBuilder = builder.getAggregateBatteryConsumerBuilder(1);
            for (int i3 = 0; i3 < totalAppPowerMah.length; i3++) {
                appsBatteryConsumerBuilder.setConsumedPowerForCustomComponent(i3 + 1000, totalAppPowerMah[i3]);
            }
        }
    }

    private double[] calculateApp(android.os.UidBatteryConsumer.Builder app, android.os.BatteryStats.Uid u, double[] totalPowerMah) {
        double[] newTotalPowerMah = null;
        double[] customEnergyConsumerPowerMah = uCtoMah(u.getCustomEnergyConsumerBatteryConsumptionUC());
        if (customEnergyConsumerPowerMah != null) {
            if (totalPowerMah == null) {
                newTotalPowerMah = new double[customEnergyConsumerPowerMah.length];
            } else if (totalPowerMah.length != customEnergyConsumerPowerMah.length) {
                android.util.Slog.wtf(TAG, "Number of custom energy components is not the same for all apps: " + totalPowerMah.length + ", " + customEnergyConsumerPowerMah.length);
                newTotalPowerMah = java.util.Arrays.copyOf(totalPowerMah, customEnergyConsumerPowerMah.length);
            } else {
                newTotalPowerMah = totalPowerMah;
            }
            for (int i = 0; i < customEnergyConsumerPowerMah.length; i++) {
                app.setConsumedPowerForCustomComponent(i + 1000, customEnergyConsumerPowerMah[i]);
                if (!app.isVirtualUid()) {
                    newTotalPowerMah[i] = newTotalPowerMah[i] + customEnergyConsumerPowerMah[i];
                }
            }
        }
        return newTotalPowerMah;
    }

    private double[] uCtoMah(long[] chargeUC) {
        if (chargeUC == null) {
            return null;
        }
        double[] mah = new double[chargeUC.length];
        for (int i = 0; i < chargeUC.length; i++) {
            mah[i] = uCtoMah(chargeUC[i]);
        }
        return mah;
    }
}
