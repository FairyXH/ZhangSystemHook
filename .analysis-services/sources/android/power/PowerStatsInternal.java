package android.power;

/* JADX INFO: loaded from: classes.dex */
public abstract class PowerStatsInternal {
    public abstract java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> getEnergyConsumedAsync(int[] iArr);

    public abstract android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo();

    public abstract android.hardware.power.stats.Channel[] getEnergyMeterInfo();

    public abstract android.hardware.power.stats.PowerEntity[] getPowerEntityInfo();

    public abstract java.util.concurrent.CompletableFuture<android.hardware.power.stats.StateResidencyResult[]> getStateResidencyAsync(int[] iArr);

    public abstract java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyMeasurement[]> readEnergyMeterAsync(int[] iArr);
}
