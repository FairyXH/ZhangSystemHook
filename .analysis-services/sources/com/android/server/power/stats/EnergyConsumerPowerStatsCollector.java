package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class EnergyConsumerPowerStatsCollector extends com.android.server.power.stats.PowerStatsCollector {
    private static final long CAMERA_ACTIVITY_REQUEST_TIMEOUT = 20000;
    private static final long ENERGY_UNSPECIFIED = -1;
    private static final java.lang.String TAG = "CameraPowerStatsCollector";
    private com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever mConsumedEnergyRetriever;
    private int[] mEnergyConsumerIds;
    private final java.lang.String mEnergyConsumerName;
    private final int mEnergyConsumerType;
    private boolean mFirstCollection;
    private final com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector mInjector;
    private boolean mIsInitialized;
    private long mLastConsumedEnergyUws;
    private long mLastUpdateTimestamp;
    private int mLastVoltageMv;
    private final com.android.server.power.stats.BinaryStatePowerStatsLayout mLayout;
    private final int mPowerComponentId;
    private final java.lang.String mPowerComponentName;
    private com.android.internal.os.PowerStats mPowerStats;
    private java.util.function.IntSupplier mVoltageSupplier;

    interface Injector {
        com.android.internal.os.Clock getClock();

        com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever getConsumedEnergyRetriever();

        android.os.Handler getHandler();

        long getPowerStatsCollectionThrottlePeriod(java.lang.String str);

        com.android.server.power.stats.PowerStatsUidResolver getUidResolver();

        java.util.function.IntSupplier getVoltageSupplier();
    }

    EnergyConsumerPowerStatsCollector(com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector injector, int powerComponentId, java.lang.String powerComponentName, int energyConsumerType, java.lang.String energyConsumerName, com.android.server.power.stats.BinaryStatePowerStatsLayout statsLayout) {
        super(injector.getHandler(), injector.getPowerStatsCollectionThrottlePeriod(powerComponentName), injector.getUidResolver(), injector.getClock());
        this.mEnergyConsumerIds = new int[0];
        this.mLastConsumedEnergyUws = -1L;
        this.mFirstCollection = true;
        this.mInjector = injector;
        this.mPowerComponentId = powerComponentId;
        this.mPowerComponentName = powerComponentName;
        this.mEnergyConsumerType = energyConsumerType;
        this.mEnergyConsumerName = energyConsumerName;
        this.mLayout = statsLayout;
    }

    private boolean ensureInitialized() {
        if (this.mIsInitialized) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        this.mConsumedEnergyRetriever = this.mInjector.getConsumedEnergyRetriever();
        this.mVoltageSupplier = this.mInjector.getVoltageSupplier();
        this.mEnergyConsumerIds = this.mConsumedEnergyRetriever.getEnergyConsumerIds(this.mEnergyConsumerType, this.mEnergyConsumerName);
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mLayout.toExtras(extras);
        com.android.internal.os.PowerStats.Descriptor powerStatsDescriptor = new com.android.internal.os.PowerStats.Descriptor(this.mPowerComponentId, this.mPowerComponentName, this.mLayout.getDeviceStatsArrayLength(), (android.util.SparseArray) null, 0, this.mLayout.getUidStatsArrayLength(), extras);
        this.mPowerStats = new com.android.internal.os.PowerStats(powerStatsDescriptor);
        this.mIsInitialized = true;
        return true;
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    protected com.android.internal.os.PowerStats collectStats() {
        if (!ensureInitialized() || this.mEnergyConsumerIds.length == 0) {
            return null;
        }
        long consumedEnergy = 0;
        int voltageMv = this.mVoltageSupplier.getAsInt();
        if (voltageMv <= 0) {
            android.util.Slog.wtf(TAG, "Unexpected battery voltage (" + voltageMv + " mV) when querying energy consumers");
        } else {
            long[] energyUws = this.mConsumedEnergyRetriever.getConsumedEnergyUws(this.mEnergyConsumerIds);
            if (energyUws != null) {
                for (int i = energyUws.length - 1; i >= 0; i--) {
                    if (energyUws[i] != -1) {
                        consumedEnergy += energyUws[i];
                    }
                }
            }
        }
        long energyDelta = this.mLastConsumedEnergyUws != -1 ? consumedEnergy - this.mLastConsumedEnergyUws : 0L;
        this.mLastConsumedEnergyUws = consumedEnergy;
        if (energyDelta < 0) {
            energyDelta = 0;
        }
        if (energyDelta == 0 && !this.mFirstCollection) {
            return null;
        }
        int averageVoltage = this.mLastVoltageMv != 0 ? (this.mLastVoltageMv + voltageMv) / 2 : voltageMv;
        this.mLastVoltageMv = voltageMv;
        this.mLayout.setConsumedEnergy(this.mPowerStats.stats, 0, uJtoUc(energyDelta, averageVoltage));
        long timestamp = this.mClock.elapsedRealtime();
        this.mPowerStats.durationMs = timestamp - this.mLastUpdateTimestamp;
        this.mLastUpdateTimestamp = timestamp;
        this.mFirstCollection = false;
        return this.mPowerStats;
    }
}
