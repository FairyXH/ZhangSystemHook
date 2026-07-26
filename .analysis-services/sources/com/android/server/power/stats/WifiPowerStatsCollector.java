package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class WifiPowerStatsCollector extends com.android.server.power.stats.PowerStatsCollector {
    private static final long ENERGY_UNSPECIFIED = -1;
    private static final java.lang.String TAG = "WifiPowerStatsCollector";
    private static final long WIFI_ACTIVITY_REQUEST_TIMEOUT = 20000;
    private com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever mConsumedEnergyRetriever;
    private long[] mDeviceStats;
    private int[] mEnergyConsumerIds;
    private final com.android.server.power.stats.WifiPowerStatsCollector.Injector mInjector;
    private boolean mIsInitialized;
    private long[] mLastConsumedEnergyUws;
    private android.net.NetworkStats mLastNetworkStats;
    private final android.util.SparseArray<com.android.server.power.stats.WifiPowerStatsCollector.WifiScanTimes> mLastScanTimes;
    private int mLastVoltageMv;
    private long mLastWifiActiveDuration;
    private android.os.connectivity.WifiActivityEnergyInfo mLastWifiActivityInfo;
    private com.android.server.power.stats.WifiPowerStatsLayout mLayout;
    private volatile java.util.function.Supplier<android.net.NetworkStats> mNetworkStatsSupplier;
    private boolean mPowerReportingSupported;
    private com.android.internal.os.PowerStats mPowerStats;
    private final com.android.server.power.stats.WifiPowerStatsCollector.WifiScanTimes mScanTimes;
    private java.util.function.IntSupplier mVoltageSupplier;
    private volatile android.net.wifi.WifiManager mWifiManager;
    private volatile com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever mWifiStatsRetriever;

    interface Injector {
        com.android.internal.os.Clock getClock();

        com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever getConsumedEnergyRetriever();

        android.os.Handler getHandler();

        android.content.pm.PackageManager getPackageManager();

        long getPowerStatsCollectionThrottlePeriod(java.lang.String str);

        com.android.server.power.stats.PowerStatsUidResolver getUidResolver();

        java.util.function.IntSupplier getVoltageSupplier();

        android.net.wifi.WifiManager getWifiManager();

        java.util.function.Supplier<android.net.NetworkStats> getWifiNetworkStatsSupplier();

        com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever getWifiStatsRetriever();
    }

    interface WifiStatsRetriever {

        public interface Callback {
            void onWifiScanTime(int i, long j, long j2);
        }

        long getWifiActiveDuration();

        void retrieveWifiScanTimes(com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever.Callback callback);
    }

    private static class WifiScanTimes {
        public long basicScanTimeMs;
        public long batchedScanTimeMs;

        private WifiScanTimes() {
        }
    }

    WifiPowerStatsCollector(com.android.server.power.stats.WifiPowerStatsCollector.Injector injector) {
        super(injector.getHandler(), injector.getPowerStatsCollectionThrottlePeriod(android.os.BatteryConsumer.powerComponentIdToString(11)), injector.getUidResolver(), injector.getClock());
        this.mEnergyConsumerIds = new int[0];
        this.mLastWifiActivityInfo = new android.os.connectivity.WifiActivityEnergyInfo(0L, 0, 0L, 0L, 0L, 0L);
        this.mScanTimes = new com.android.server.power.stats.WifiPowerStatsCollector.WifiScanTimes();
        this.mLastScanTimes = new android.util.SparseArray<>();
        this.mInjector = injector;
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    public void setEnabled(boolean enabled) {
        boolean z = false;
        if (enabled) {
            android.content.pm.PackageManager packageManager = this.mInjector.getPackageManager();
            if (packageManager != null && packageManager.hasSystemFeature("android.hardware.wifi")) {
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
        boolean z = false;
        if (!isEnabled()) {
            return false;
        }
        this.mConsumedEnergyRetriever = this.mInjector.getConsumedEnergyRetriever();
        this.mVoltageSupplier = this.mInjector.getVoltageSupplier();
        this.mWifiManager = this.mInjector.getWifiManager();
        this.mNetworkStatsSupplier = this.mInjector.getWifiNetworkStatsSupplier();
        this.mWifiStatsRetriever = this.mInjector.getWifiStatsRetriever();
        if (this.mWifiManager != null && this.mWifiManager.isEnhancedPowerReportingSupported()) {
            z = true;
        }
        this.mPowerReportingSupported = z;
        this.mEnergyConsumerIds = this.mConsumedEnergyRetriever.getEnergyConsumerIds(6);
        this.mLastConsumedEnergyUws = new long[this.mEnergyConsumerIds.length];
        java.util.Arrays.fill(this.mLastConsumedEnergyUws, -1L);
        this.mLayout = new com.android.server.power.stats.WifiPowerStatsLayout();
        this.mLayout.addDeviceWifiActivity(this.mPowerReportingSupported);
        this.mLayout.addDeviceSectionEnergyConsumers(this.mEnergyConsumerIds.length);
        this.mLayout.addUidNetworkStats();
        this.mLayout.addDeviceSectionUsageDuration();
        this.mLayout.addDeviceSectionPowerEstimate();
        this.mLayout.addUidSectionPowerEstimate();
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mLayout.toExtras(extras);
        com.android.internal.os.PowerStats.Descriptor powerStatsDescriptor = new com.android.internal.os.PowerStats.Descriptor(11, this.mLayout.getDeviceStatsArrayLength(), (android.util.SparseArray) null, 0, this.mLayout.getUidStatsArrayLength(), extras);
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
        if (this.mPowerReportingSupported) {
            collectWifiActivityInfo();
        } else {
            collectWifiActivityStats();
        }
        collectNetworkStats();
        collectWifiScanTime();
        if (this.mEnergyConsumerIds.length != 0) {
            collectEnergyConsumers();
        }
        return this.mPowerStats;
    }

    private void collectWifiActivityInfo() {
        android.os.connectivity.WifiActivityEnergyInfo activityInfo;
        final java.util.concurrent.CompletableFuture<android.os.connectivity.WifiActivityEnergyInfo> immediateFuture = new java.util.concurrent.CompletableFuture<>();
        android.net.wifi.WifiManager wifiManager = this.mWifiManager;
        com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0 systemServerInitThreadPool$$ExternalSyntheticLambda0 = new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0();
        java.util.Objects.requireNonNull(immediateFuture);
        wifiManager.getWifiActivityEnergyInfoAsync(systemServerInitThreadPool$$ExternalSyntheticLambda0, new android.net.wifi.WifiManager.OnWifiActivityEnergyInfoListener() { // from class: com.android.server.power.stats.WifiPowerStatsCollector$$ExternalSyntheticLambda0
            public final void onWifiActivityEnergyInfo(android.os.connectivity.WifiActivityEnergyInfo wifiActivityEnergyInfo) {
                immediateFuture.complete(wifiActivityEnergyInfo);
            }
        });
        try {
            activityInfo = immediateFuture.get(WIFI_ACTIVITY_REQUEST_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Cannot acquire WifiActivityEnergyInfo", e);
            activityInfo = null;
        }
        if (activityInfo == null) {
            return;
        }
        long rxDuration = activityInfo.getControllerRxDurationMillis() - this.mLastWifiActivityInfo.getControllerRxDurationMillis();
        long txDuration = activityInfo.getControllerTxDurationMillis() - this.mLastWifiActivityInfo.getControllerTxDurationMillis();
        long scanDuration = activityInfo.getControllerScanDurationMillis() - this.mLastWifiActivityInfo.getControllerScanDurationMillis();
        long idleDuration = activityInfo.getControllerIdleDurationMillis() - this.mLastWifiActivityInfo.getControllerIdleDurationMillis();
        this.mLayout.setDeviceRxTime(this.mDeviceStats, rxDuration);
        this.mLayout.setDeviceTxTime(this.mDeviceStats, txDuration);
        this.mLayout.setDeviceScanTime(this.mDeviceStats, scanDuration);
        this.mLayout.setDeviceIdleTime(this.mDeviceStats, idleDuration);
        this.mPowerStats.durationMs = rxDuration + txDuration + scanDuration + idleDuration;
        this.mLastWifiActivityInfo = activityInfo;
    }

    private void collectWifiActivityStats() {
        long duration = this.mWifiStatsRetriever.getWifiActiveDuration();
        this.mLayout.setDeviceActiveTime(this.mDeviceStats, java.lang.Math.max(0L, duration - this.mLastWifiActiveDuration));
        this.mLastWifiActiveDuration = duration;
        this.mPowerStats.durationMs = duration;
    }

    private void collectNetworkStats() {
        android.net.NetworkStats networkStats;
        java.util.List<com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta> delta;
        this.mPowerStats.uidStats.clear();
        android.net.NetworkStats networkStats2 = this.mNetworkStatsSupplier.get();
        if (networkStats2 == null) {
            return;
        }
        java.util.List<com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta> delta2 = com.android.server.power.stats.BatteryStatsImpl.computeDelta(networkStats2, this.mLastNetworkStats);
        this.mLastNetworkStats = networkStats2;
        int i = delta2.size() - 1;
        while (i >= 0) {
            com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta uidDelta = delta2.get(i);
            long rxBytes = uidDelta.getRxBytes();
            long txBytes = uidDelta.getTxBytes();
            long rxPackets = uidDelta.getRxPackets();
            long txPackets = uidDelta.getTxPackets();
            if (rxBytes == 0 && txBytes == 0 && rxPackets == 0 && txPackets == 0) {
                networkStats = networkStats2;
                delta = delta2;
            } else {
                int uid = this.mUidResolver.mapUid(uidDelta.getUid());
                long[] stats = (long[]) this.mPowerStats.uidStats.get(uid);
                if (stats != null) {
                    networkStats = networkStats2;
                    delta = delta2;
                    this.mLayout.setUidRxBytes(stats, this.mLayout.getUidRxBytes(stats) + rxBytes);
                    this.mLayout.setUidTxBytes(stats, this.mLayout.getUidTxBytes(stats) + txBytes);
                    this.mLayout.setUidRxPackets(stats, this.mLayout.getUidRxPackets(stats) + rxPackets);
                    this.mLayout.setUidTxPackets(stats, this.mLayout.getUidTxPackets(stats) + txPackets);
                } else {
                    long[] stats2 = new long[this.mLayout.getUidStatsArrayLength()];
                    this.mPowerStats.uidStats.put(uid, stats2);
                    this.mLayout.setUidRxBytes(stats2, rxBytes);
                    this.mLayout.setUidTxBytes(stats2, txBytes);
                    this.mLayout.setUidRxPackets(stats2, rxPackets);
                    this.mLayout.setUidTxPackets(stats2, txPackets);
                    networkStats = networkStats2;
                    delta = delta2;
                }
            }
            i--;
            networkStats2 = networkStats;
            delta2 = delta;
        }
    }

    private void collectWifiScanTime() {
        this.mScanTimes.basicScanTimeMs = 0L;
        this.mScanTimes.batchedScanTimeMs = 0L;
        this.mWifiStatsRetriever.retrieveWifiScanTimes(new com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever.Callback() { // from class: com.android.server.power.stats.WifiPowerStatsCollector$$ExternalSyntheticLambda1
            @Override // com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever.Callback
            public final void onWifiScanTime(int i, long j, long j2) {
                this.f$0.lambda$collectWifiScanTime$0(i, j, j2);
            }
        });
        this.mLayout.setDeviceBasicScanTime(this.mDeviceStats, this.mScanTimes.basicScanTimeMs);
        this.mLayout.setDeviceBatchedScanTime(this.mDeviceStats, this.mScanTimes.batchedScanTimeMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectWifiScanTime$0(int uid, long scanTimeMs, long batchScanTimeMs) {
        com.android.server.power.stats.WifiPowerStatsCollector.WifiScanTimes lastScanTimes = this.mLastScanTimes.get(uid);
        if (lastScanTimes == null) {
            lastScanTimes = new com.android.server.power.stats.WifiPowerStatsCollector.WifiScanTimes();
            this.mLastScanTimes.put(uid, lastScanTimes);
        }
        long scanTimeDelta = java.lang.Math.max(0L, scanTimeMs - lastScanTimes.basicScanTimeMs);
        long batchScanTimeDelta = java.lang.Math.max(0L, batchScanTimeMs - lastScanTimes.batchedScanTimeMs);
        if (scanTimeDelta != 0 || batchScanTimeDelta != 0) {
            this.mScanTimes.basicScanTimeMs += scanTimeDelta;
            this.mScanTimes.batchedScanTimeMs += batchScanTimeDelta;
            int uid2 = this.mUidResolver.mapUid(uid);
            long[] stats = (long[]) this.mPowerStats.uidStats.get(uid2);
            if (stats == null) {
                long[] stats2 = new long[this.mLayout.getUidStatsArrayLength()];
                this.mPowerStats.uidStats.put(uid2, stats2);
                this.mLayout.setUidScanTime(stats2, scanTimeDelta);
                this.mLayout.setUidBatchScanTime(stats2, batchScanTimeDelta);
            } else {
                this.mLayout.setUidScanTime(stats, this.mLayout.getUidScanTime(stats) + scanTimeDelta);
                this.mLayout.setUidBatchScanTime(stats, this.mLayout.getUidBatchedScanTime(stats) + batchScanTimeDelta);
            }
        }
        lastScanTimes.basicScanTimeMs = scanTimeMs;
        lastScanTimes.batchedScanTimeMs = batchScanTimeMs;
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
        this.mLastScanTimes.remove(uid);
    }
}
