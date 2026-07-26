package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class MobileRadioPowerStatsCollector extends com.android.server.power.stats.PowerStatsCollector {
    private static final long ENERGY_UNSPECIFIED = -1;
    protected static final long MOBILE_RADIO_POWER_STATE_UPDATE_FREQ_MS = 600000;
    private static final long MODEM_ACTIVITY_REQUEST_TIMEOUT = 20000;
    static final int[] NETWORK_TYPES = {0, 1, 2, 3, 4, 5, 6};
    private static final java.lang.String TAG = "MobileRadioPowerStatsCollector";
    private java.util.function.LongSupplier mCallDurationSupplier;
    private com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever mConsumedEnergyRetriever;
    private long[] mDeviceStats;
    private int[] mEnergyConsumerIds;
    private final com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector mInjector;
    private boolean mIsInitialized;
    private long mLastCallDuration;
    private long[] mLastConsumedEnergyUws;
    private android.telephony.ModemActivityInfo mLastModemActivityInfo;
    private android.net.NetworkStats mLastNetworkStats;
    private long mLastScanDuration;
    private long mLastUpdateTimestampMillis;
    private int mLastVoltageMv;
    private com.android.server.power.stats.MobileRadioPowerStatsLayout mLayout;
    private volatile java.util.function.Supplier<android.net.NetworkStats> mNetworkStatsSupplier;
    private com.android.internal.os.PowerStats mPowerStats;
    private java.util.function.LongSupplier mScanDurationSupplier;
    private volatile android.telephony.TelephonyManager mTelephonyManager;
    private java.util.function.IntSupplier mVoltageSupplier;

    interface Injector {
        java.util.function.LongSupplier getCallDurationSupplier();

        com.android.internal.os.Clock getClock();

        com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever getConsumedEnergyRetriever();

        android.os.Handler getHandler();

        java.util.function.Supplier<android.net.NetworkStats> getMobileNetworkStatsSupplier();

        android.content.pm.PackageManager getPackageManager();

        java.util.function.LongSupplier getPhoneSignalScanDurationSupplier();

        long getPowerStatsCollectionThrottlePeriod(java.lang.String str);

        android.telephony.TelephonyManager getTelephonyManager();

        com.android.server.power.stats.PowerStatsUidResolver getUidResolver();

        java.util.function.IntSupplier getVoltageSupplier();
    }

    MobileRadioPowerStatsCollector(com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector injector) {
        super(injector.getHandler(), injector.getPowerStatsCollectionThrottlePeriod(android.os.BatteryConsumer.powerComponentIdToString(8)), injector.getUidResolver(), injector.getClock());
        this.mEnergyConsumerIds = new int[0];
        this.mInjector = injector;
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    public void setEnabled(boolean enabled) {
        boolean z = false;
        if (enabled) {
            android.content.pm.PackageManager packageManager = this.mInjector.getPackageManager();
            if (packageManager != null && packageManager.hasSystemFeature("android.hardware.telephony")) {
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
        this.mTelephonyManager = this.mInjector.getTelephonyManager();
        this.mNetworkStatsSupplier = this.mInjector.getMobileNetworkStatsSupplier();
        this.mCallDurationSupplier = this.mInjector.getCallDurationSupplier();
        this.mScanDurationSupplier = this.mInjector.getPhoneSignalScanDurationSupplier();
        this.mEnergyConsumerIds = this.mConsumedEnergyRetriever.getEnergyConsumerIds(5);
        this.mLastConsumedEnergyUws = new long[this.mEnergyConsumerIds.length];
        java.util.Arrays.fill(this.mLastConsumedEnergyUws, -1L);
        this.mLayout = new com.android.server.power.stats.MobileRadioPowerStatsLayout();
        this.mLayout.addDeviceMobileActivity();
        this.mLayout.addDeviceSectionEnergyConsumers(this.mEnergyConsumerIds.length);
        this.mLayout.addStateStats();
        this.mLayout.addUidNetworkStats();
        this.mLayout.addDeviceSectionUsageDuration();
        this.mLayout.addDeviceSectionPowerEstimate();
        this.mLayout.addUidSectionPowerEstimate();
        android.util.SparseArray<java.lang.String> stateLabels = new android.util.SparseArray<>();
        int rat = 0;
        while (rat < 3) {
            int freqCount = rat == 2 ? 5 : 1;
            for (int freq = 0; freq < freqCount; freq++) {
                int stateKey = makeStateKey(rat, freq);
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                if (rat != 0) {
                    sb.append(android.os.BatteryStats.RADIO_ACCESS_TECHNOLOGY_NAMES[rat]);
                }
                if (freq != 0) {
                    if (!sb.isEmpty()) {
                        sb.append(" ");
                    }
                    sb.append(android.telephony.ServiceState.frequencyRangeToString(freq));
                }
                stateLabels.put(stateKey, !sb.isEmpty() ? sb.toString() : "other");
            }
            rat++;
        }
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mLayout.toExtras(extras);
        com.android.internal.os.PowerStats.Descriptor powerStatsDescriptor = new com.android.internal.os.PowerStats.Descriptor(8, this.mLayout.getDeviceStatsArrayLength(), stateLabels, this.mLayout.getStateStatsArrayLength(), this.mLayout.getUidStatsArrayLength(), extras);
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
        java.util.Arrays.fill(this.mPowerStats.stats, 0L);
        this.mPowerStats.uidStats.clear();
        collectModemActivityInfo();
        collectNetworkStats();
        if (this.mEnergyConsumerIds.length != 0) {
            collectEnergyConsumers();
        }
        if (this.mPowerStats.durationMs == 0) {
            setTimestamp(this.mClock.elapsedRealtime());
        }
        return this.mPowerStats;
    }

    private void collectModemActivityInfo() {
        android.telephony.ModemActivityInfo activityInfo;
        android.telephony.ModemActivityInfo deltaInfo;
        android.telephony.ModemActivityInfo activityInfo2;
        int rat;
        if (this.mTelephonyManager == null) {
            return;
        }
        final java.util.concurrent.CompletableFuture<android.telephony.ModemActivityInfo> immediateFuture = new java.util.concurrent.CompletableFuture<>();
        this.mTelephonyManager.requestModemActivityInfo(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new android.os.OutcomeReceiver<android.telephony.ModemActivityInfo, android.telephony.TelephonyManager.ModemActivityInfoException>() { // from class: com.android.server.power.stats.MobileRadioPowerStatsCollector.1
            @Override // android.os.OutcomeReceiver
            public void onResult(android.telephony.ModemActivityInfo result) {
                immediateFuture.complete(result);
            }

            @Override // android.os.OutcomeReceiver
            public void onError(android.telephony.TelephonyManager.ModemActivityInfoException e) {
                android.util.Slog.w(com.android.server.power.stats.MobileRadioPowerStatsCollector.TAG, "error reading modem stats:" + e);
                immediateFuture.complete(null);
            }
        });
        try {
            activityInfo = immediateFuture.get(MODEM_ACTIVITY_REQUEST_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Cannot acquire ModemActivityInfo");
            activityInfo = null;
        }
        if (activityInfo == null) {
            return;
        }
        if (this.mLastModemActivityInfo == null) {
            deltaInfo = activityInfo.getDelta(activityInfo);
        } else {
            deltaInfo = this.mLastModemActivityInfo.getDelta(activityInfo);
        }
        this.mLastModemActivityInfo = activityInfo;
        setTimestamp(deltaInfo.getTimestampMillis());
        this.mLayout.setDeviceSleepTime(this.mDeviceStats, deltaInfo.getSleepTimeMillis());
        this.mLayout.setDeviceIdleTime(this.mDeviceStats, deltaInfo.getIdleTimeMillis());
        long callDuration = this.mCallDurationSupplier.getAsLong();
        if (callDuration >= this.mLastCallDuration) {
            this.mLayout.setDeviceCallTime(this.mDeviceStats, callDuration - this.mLastCallDuration);
        }
        this.mLastCallDuration = callDuration;
        long scanDuration = this.mScanDurationSupplier.getAsLong();
        if (scanDuration >= this.mLastScanDuration) {
            this.mLayout.setDeviceScanTime(this.mDeviceStats, scanDuration - this.mLastScanDuration);
        }
        this.mLastScanDuration = scanDuration;
        android.util.SparseArray<long[]> stateStats = this.mPowerStats.stateStats;
        stateStats.clear();
        if (deltaInfo.getSpecificInfoLength() == 0) {
            this.mLayout.addRxTxTimesForRat(stateStats, 0, 0, deltaInfo.getReceiveTimeMillis(), deltaInfo.getTransmitTimeMillis());
            return;
        }
        int rat2 = 0;
        while (rat2 < NETWORK_TYPES.length) {
            if (rat2 == 6) {
                int freq = 0;
                while (freq < 5) {
                    this.mLayout.addRxTxTimesForRat(stateStats, rat2, freq, deltaInfo.getReceiveTimeMillis(rat2, freq), deltaInfo.getTransmitTimeMillis(rat2, freq));
                    freq++;
                    rat2 = rat2;
                    activityInfo = activityInfo;
                }
                activityInfo2 = activityInfo;
                rat = rat2;
            } else {
                activityInfo2 = activityInfo;
                rat = rat2;
                this.mLayout.addRxTxTimesForRat(stateStats, rat, 0, deltaInfo.getReceiveTimeMillis(rat), deltaInfo.getTransmitTimeMillis(rat));
            }
            rat2 = rat + 1;
            activityInfo = activityInfo2;
        }
    }

    private void collectNetworkStats() {
        android.net.NetworkStats networkStats;
        java.util.List<com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta> delta;
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

    static int makeStateKey(int rat, int freqRange) {
        if (rat == 2) {
            return (freqRange << 8) | rat;
        }
        return rat;
    }

    private void setTimestamp(long timestamp) {
        this.mPowerStats.durationMs = java.lang.Math.max(timestamp - this.mLastUpdateTimestampMillis, 0L);
        this.mLastUpdateTimestampMillis = timestamp;
    }

    static int mapRadioAccessNetworkTypeToRadioAccessTechnology(int networkType) {
        switch (networkType) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 5:
                break;
            case 3:
                break;
            case 6:
                break;
            default:
                android.util.Slog.w(TAG, "Unhandled RadioAccessNetworkType (" + networkType + "), mapping to OTHER");
                break;
        }
        return 0;
    }
}
