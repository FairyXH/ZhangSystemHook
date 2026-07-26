package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BluetoothPowerStatsCollector extends com.android.server.power.stats.PowerStatsCollector {
    private static final long BLUETOOTH_ACTIVITY_REQUEST_TIMEOUT = 20000;
    private static final long ENERGY_UNSPECIFIED = -1;
    private static final java.lang.String TAG = "BluetoothPowerStatsCollector";
    private com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever mBluetoothStatsRetriever;
    private com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever mConsumedEnergyRetriever;
    private long[] mDeviceStats;
    private int[] mEnergyConsumerIds;
    private final com.android.server.power.stats.BluetoothPowerStatsCollector.Injector mInjector;
    private boolean mIsInitialized;
    private long[] mLastConsumedEnergyUws;
    private long mLastIdleTime;
    private long mLastRxTime;
    private long mLastTxTime;
    private int mLastVoltageMv;
    private com.android.server.power.stats.BluetoothPowerStatsLayout mLayout;
    private com.android.internal.os.PowerStats mPowerStats;
    private final android.util.SparseArray<com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats> mUidStats;
    private java.util.function.IntSupplier mVoltageSupplier;

    interface BluetoothStatsRetriever {

        public interface Callback {
            void onBluetoothScanTime(int i, long j);
        }

        boolean requestControllerActivityEnergyInfo(java.util.concurrent.Executor executor, android.bluetooth.BluetoothAdapter.OnBluetoothActivityEnergyInfoCallback onBluetoothActivityEnergyInfoCallback);

        void retrieveBluetoothScanTimes(com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever.Callback callback);
    }

    interface Injector {
        com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever getBluetoothStatsRetriever();

        com.android.internal.os.Clock getClock();

        com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever getConsumedEnergyRetriever();

        android.os.Handler getHandler();

        android.content.pm.PackageManager getPackageManager();

        long getPowerStatsCollectionThrottlePeriod(java.lang.String str);

        com.android.server.power.stats.PowerStatsUidResolver getUidResolver();

        java.util.function.IntSupplier getVoltageSupplier();
    }

    private static class UidStats {
        public long lastRxCount;
        public long lastScanTime;
        public long lastTxCount;
        public long rxCount;
        public long scanTime;
        public long txCount;

        private UidStats() {
        }
    }

    BluetoothPowerStatsCollector(com.android.server.power.stats.BluetoothPowerStatsCollector.Injector injector) {
        super(injector.getHandler(), injector.getPowerStatsCollectionThrottlePeriod(android.os.BatteryConsumer.powerComponentIdToString(2)), injector.getUidResolver(), injector.getClock());
        this.mEnergyConsumerIds = new int[0];
        this.mUidStats = new android.util.SparseArray<>();
        this.mInjector = injector;
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    public void setEnabled(boolean enabled) {
        boolean z = false;
        if (enabled) {
            android.content.pm.PackageManager packageManager = this.mInjector.getPackageManager();
            if (packageManager != null && packageManager.hasSystemFeature("android.hardware.bluetooth")) {
                z = true;
            }
            super.setEnabled(z);
            return;
        }
        super.setEnabled(false);
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
        this.mBluetoothStatsRetriever = this.mInjector.getBluetoothStatsRetriever();
        this.mEnergyConsumerIds = this.mConsumedEnergyRetriever.getEnergyConsumerIds(1);
        this.mLastConsumedEnergyUws = new long[this.mEnergyConsumerIds.length];
        java.util.Arrays.fill(this.mLastConsumedEnergyUws, -1L);
        this.mLayout = new com.android.server.power.stats.BluetoothPowerStatsLayout();
        this.mLayout.addDeviceBluetoothControllerActivity();
        this.mLayout.addDeviceSectionEnergyConsumers(this.mEnergyConsumerIds.length);
        this.mLayout.addDeviceSectionUsageDuration();
        this.mLayout.addDeviceSectionPowerEstimate();
        this.mLayout.addUidTrafficStats();
        this.mLayout.addUidSectionPowerEstimate();
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mLayout.toExtras(extras);
        com.android.internal.os.PowerStats.Descriptor powerStatsDescriptor = new com.android.internal.os.PowerStats.Descriptor(2, this.mLayout.getDeviceStatsArrayLength(), (android.util.SparseArray) null, 0, this.mLayout.getUidStatsArrayLength(), extras);
        this.mPowerStats = new com.android.internal.os.PowerStats(powerStatsDescriptor);
        this.mDeviceStats = this.mPowerStats.stats;
        this.mIsInitialized = true;
        return true;
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    protected com.android.internal.os.PowerStats collectStats() {
        if (!ensureInitialized()) {
            return null;
        }
        this.mPowerStats.uidStats.clear();
        collectBluetoothActivityInfo();
        collectBluetoothScanStats();
        if (this.mEnergyConsumerIds.length != 0) {
            collectEnergyConsumers();
        }
        return this.mPowerStats;
    }

    private void collectBluetoothActivityInfo() {
        android.bluetooth.BluetoothActivityEnergyInfo activityInfo;
        long idleTimeDelta;
        final java.util.concurrent.CompletableFuture<android.bluetooth.BluetoothActivityEnergyInfo> immediateFuture = new java.util.concurrent.CompletableFuture<>();
        boolean success = this.mBluetoothStatsRetriever.requestControllerActivityEnergyInfo(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new android.bluetooth.BluetoothAdapter.OnBluetoothActivityEnergyInfoCallback() { // from class: com.android.server.power.stats.BluetoothPowerStatsCollector.1
            public void onBluetoothActivityEnergyInfoAvailable(android.bluetooth.BluetoothActivityEnergyInfo info) {
                immediateFuture.complete(info);
            }

            public void onBluetoothActivityEnergyInfoError(int error) {
                immediateFuture.completeExceptionally(new java.lang.RuntimeException("error: " + error));
            }
        });
        if (!success) {
            return;
        }
        try {
            activityInfo = immediateFuture.get(BLUETOOTH_ACTIVITY_REQUEST_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Cannot acquire BluetoothActivityEnergyInfo", e);
            activityInfo = null;
        }
        if (activityInfo == null) {
            return;
        }
        long rxTime = activityInfo.getControllerRxTimeMillis();
        long rxTimeDelta = java.lang.Math.max(0L, rxTime - this.mLastRxTime);
        this.mLayout.setDeviceRxTime(this.mDeviceStats, rxTimeDelta);
        this.mLastRxTime = rxTime;
        long txTime = activityInfo.getControllerTxTimeMillis();
        long txTimeDelta = java.lang.Math.max(0L, txTime - this.mLastTxTime);
        this.mLayout.setDeviceTxTime(this.mDeviceStats, txTimeDelta);
        this.mLastTxTime = txTime;
        long idleTime = activityInfo.getControllerIdleTimeMillis();
        long idleTimeDelta2 = java.lang.Math.max(0L, idleTime - this.mLastIdleTime);
        this.mLayout.setDeviceIdleTime(this.mDeviceStats, idleTimeDelta2);
        this.mLastIdleTime = idleTime;
        this.mPowerStats.durationMs = rxTimeDelta + txTimeDelta + idleTimeDelta2;
        java.util.List<android.bluetooth.UidTraffic> uidTraffic = activityInfo.getUidTraffic();
        int i = uidTraffic.size() - 1;
        while (i >= 0) {
            android.bluetooth.UidTraffic ut = uidTraffic.get(i);
            android.bluetooth.BluetoothActivityEnergyInfo activityInfo2 = activityInfo;
            int uid = this.mUidResolver.mapUid(ut.getUid());
            com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats counts = this.mUidStats.get(uid);
            if (counts != null) {
                idleTimeDelta = idleTimeDelta2;
            } else {
                idleTimeDelta = idleTimeDelta2;
                counts = new com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats();
                this.mUidStats.put(uid, counts);
            }
            counts.rxCount += ut.getRxBytes();
            counts.txCount += ut.getTxBytes();
            i--;
            activityInfo = activityInfo2;
            idleTimeDelta2 = idleTimeDelta;
        }
        int i2 = this.mUidStats.size() - 1;
        while (i2 >= 0) {
            com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats counts2 = this.mUidStats.valueAt(i2);
            java.util.List<android.bluetooth.UidTraffic> uidTraffic2 = uidTraffic;
            long rxDelta = java.lang.Math.max(0L, counts2.rxCount - counts2.lastRxCount);
            counts2.lastRxCount = counts2.rxCount;
            counts2.rxCount = 0L;
            long rxTimeDelta2 = rxTimeDelta;
            long txDelta = java.lang.Math.max(0L, counts2.txCount - counts2.lastTxCount);
            counts2.lastTxCount = counts2.txCount;
            counts2.txCount = 0L;
            if (rxDelta != 0 || txDelta != 0) {
                int uid2 = this.mUidStats.keyAt(i2);
                long[] stats = (long[]) this.mPowerStats.uidStats.get(uid2);
                if (stats == null) {
                    stats = new long[this.mLayout.getUidStatsArrayLength()];
                    this.mPowerStats.uidStats.put(uid2, stats);
                }
                this.mLayout.setUidRxBytes(stats, rxDelta);
                this.mLayout.setUidTxBytes(stats, txDelta);
            }
            i2--;
            uidTraffic = uidTraffic2;
            rxTimeDelta = rxTimeDelta2;
        }
    }

    private void collectBluetoothScanStats() {
        this.mBluetoothStatsRetriever.retrieveBluetoothScanTimes(new com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever.Callback() { // from class: com.android.server.power.stats.BluetoothPowerStatsCollector$$ExternalSyntheticLambda0
            @Override // com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever.Callback
            public final void onBluetoothScanTime(int i, long j) {
                this.f$0.lambda$collectBluetoothScanStats$0(i, j);
            }
        });
        long totalScanTime = 0;
        for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats counts = this.mUidStats.valueAt(i);
            if (counts.scanTime != 0) {
                long delta = java.lang.Math.max(0L, counts.scanTime - counts.lastScanTime);
                counts.lastScanTime = counts.scanTime;
                counts.scanTime = 0L;
                if (delta != 0) {
                    int uid = this.mUidStats.keyAt(i);
                    long[] stats = (long[]) this.mPowerStats.uidStats.get(uid);
                    if (stats == null) {
                        stats = new long[this.mLayout.getUidStatsArrayLength()];
                        this.mPowerStats.uidStats.put(uid, stats);
                    }
                    this.mLayout.setUidScanTime(stats, delta);
                    totalScanTime += delta;
                }
            }
        }
        this.mLayout.setDeviceScanTime(this.mDeviceStats, totalScanTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectBluetoothScanStats$0(int uid, long scanTimeMs) {
        int uid2 = this.mUidResolver.mapUid(uid);
        com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats uidStats = this.mUidStats.get(uid2);
        if (uidStats == null) {
            uidStats = new com.android.server.power.stats.BluetoothPowerStatsCollector.UidStats();
            this.mUidStats.put(uid2, uidStats);
        }
        uidStats.scanTime += scanTimeMs;
    }

    private void collectEnergyConsumers() {
        int voltageMv = this.mVoltageSupplier.getAsInt();
        if (voltageMv <= 0) {
            android.util.Slog.wtf(TAG, "Unexpected battery voltage (" + voltageMv + " mV) when querying energy consumers");
            return;
        }
        int averageVoltage = this.mLastVoltageMv != 0 ? (this.mLastVoltageMv + voltageMv) / 2 : voltageMv;
        this.mLastVoltageMv = voltageMv;
        long[] energyUws = this.mConsumedEnergyRetriever.getConsumedEnergyUws(this.mEnergyConsumerIds);
        if (energyUws == null) {
            return;
        }
        for (int i = energyUws.length - 1; i >= 0; i--) {
            long energyDelta = this.mLastConsumedEnergyUws[i] != -1 ? energyUws[i] - this.mLastConsumedEnergyUws[i] : 0L;
            if (energyDelta < 0) {
                energyDelta = 0;
            }
            this.mLayout.setConsumedEnergy(this.mPowerStats.stats, i, uJtoUc(energyDelta, averageVoltage));
            this.mLastConsumedEnergyUws[i] = energyUws[i];
        }
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    protected void onUidRemoved(int uid) {
        super.onUidRemoved(uid);
        this.mUidStats.remove(uid);
    }
}
