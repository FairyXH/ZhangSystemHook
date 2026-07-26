package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class MobileRadioPowerStatsLayout extends com.android.server.power.stats.PowerStatsLayout {
    private static final java.lang.String EXTRA_DEVICE_CALL_POWER_POSITION = "dp-call";
    private static final java.lang.String EXTRA_DEVICE_CALL_TIME_POSITION = "dt-call";
    private static final java.lang.String EXTRA_DEVICE_IDLE_TIME_POSITION = "dt-idle";
    private static final java.lang.String EXTRA_DEVICE_SCAN_TIME_POSITION = "dt-scan";
    private static final java.lang.String EXTRA_DEVICE_SLEEP_TIME_POSITION = "dt-sleep";
    private static final java.lang.String EXTRA_STATE_RX_TIME_POSITION = "srx";
    private static final java.lang.String EXTRA_STATE_TX_TIMES_COUNT = "stxc";
    private static final java.lang.String EXTRA_STATE_TX_TIMES_POSITION = "stx";
    private static final java.lang.String EXTRA_UID_RX_BYTES_POSITION = "urxb";
    private static final java.lang.String EXTRA_UID_RX_PACKETS_POSITION = "urxp";
    private static final java.lang.String EXTRA_UID_TX_BYTES_POSITION = "utxb";
    private static final java.lang.String EXTRA_UID_TX_PACKETS_POSITION = "utxp";
    private static final java.lang.String TAG = "MobileRadioPowerStatsLayout";
    private int mDeviceCallPowerPosition;
    private int mDeviceCallTimePosition;
    private int mDeviceIdleTimePosition;
    private int mDeviceScanTimePosition;
    private int mDeviceSleepTimePosition;
    private int mStateRxTimePosition;
    private int mStateTxTimesCount;
    private int mStateTxTimesPosition;
    private int mUidRxBytesPosition;
    private int mUidRxPacketsPosition;
    private int mUidTxBytesPosition;
    private int mUidTxPacketsPosition;

    MobileRadioPowerStatsLayout() {
    }

    MobileRadioPowerStatsLayout(com.android.internal.os.PowerStats.Descriptor descriptor) {
        super(descriptor);
    }

    void addDeviceMobileActivity() {
        this.mDeviceSleepTimePosition = addDeviceSection(1, "sleep");
        this.mDeviceIdleTimePosition = addDeviceSection(1, "idle");
        this.mDeviceScanTimePosition = addDeviceSection(1, "scan");
        this.mDeviceCallTimePosition = addDeviceSection(1, "call", 1);
    }

    void addStateStats() {
        this.mStateRxTimePosition = addStateSection(1, "rx");
        this.mStateTxTimesCount = android.telephony.ModemActivityInfo.getNumTxPowerLevels();
        this.mStateTxTimesPosition = addStateSection(this.mStateTxTimesCount, "tx");
    }

    void addUidNetworkStats() {
        this.mUidRxPacketsPosition = addUidSection(1, "rx-pkts");
        this.mUidRxBytesPosition = addUidSection(1, "rx-B");
        this.mUidTxPacketsPosition = addUidSection(1, "tx-pkts");
        this.mUidTxBytesPosition = addUidSection(1, "tx-B");
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void addDeviceSectionPowerEstimate() {
        super.addDeviceSectionPowerEstimate();
        this.mDeviceCallPowerPosition = addDeviceSection(1, "call-power", 2);
    }

    public void setDeviceSleepTime(long[] stats, long durationMillis) {
        stats[this.mDeviceSleepTimePosition] = durationMillis;
    }

    public long getDeviceSleepTime(long[] stats) {
        return stats[this.mDeviceSleepTimePosition];
    }

    public void setDeviceIdleTime(long[] stats, long durationMillis) {
        stats[this.mDeviceIdleTimePosition] = durationMillis;
    }

    public long getDeviceIdleTime(long[] stats) {
        return stats[this.mDeviceIdleTimePosition];
    }

    public void setDeviceScanTime(long[] stats, long durationMillis) {
        stats[this.mDeviceScanTimePosition] = durationMillis;
    }

    public long getDeviceScanTime(long[] stats) {
        return stats[this.mDeviceScanTimePosition];
    }

    public void setDeviceCallTime(long[] stats, long durationMillis) {
        stats[this.mDeviceCallTimePosition] = durationMillis;
    }

    public long getDeviceCallTime(long[] stats) {
        return stats[this.mDeviceCallTimePosition];
    }

    public void setDeviceCallPowerEstimate(long[] stats, double power) {
        stats[this.mDeviceCallPowerPosition] = (long) (1000000.0d * power);
    }

    public double getDeviceCallPowerEstimate(long[] stats) {
        return stats[this.mDeviceCallPowerPosition] / 1000000.0d;
    }

    public void setStateRxTime(long[] stats, long durationMillis) {
        stats[this.mStateRxTimePosition] = durationMillis;
    }

    public long getStateRxTime(long[] stats) {
        return stats[this.mStateRxTimePosition];
    }

    public void setStateTxTime(long[] stats, int level, int durationMillis) {
        stats[this.mStateTxTimesPosition + level] = durationMillis;
    }

    public long getStateTxTime(long[] stats, int level) {
        return stats[this.mStateTxTimesPosition + level];
    }

    public void setUidRxBytes(long[] stats, long count) {
        stats[this.mUidRxBytesPosition] = count;
    }

    public long getUidRxBytes(long[] stats) {
        return stats[this.mUidRxBytesPosition];
    }

    public void setUidTxBytes(long[] stats, long count) {
        stats[this.mUidTxBytesPosition] = count;
    }

    public long getUidTxBytes(long[] stats) {
        return stats[this.mUidTxBytesPosition];
    }

    public void setUidRxPackets(long[] stats, long count) {
        stats[this.mUidRxPacketsPosition] = count;
    }

    public long getUidRxPackets(long[] stats) {
        return stats[this.mUidRxPacketsPosition];
    }

    public void setUidTxPackets(long[] stats, long count) {
        stats[this.mUidTxPacketsPosition] = count;
    }

    public long getUidTxPackets(long[] stats) {
        return stats[this.mUidTxPacketsPosition];
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void toExtras(android.os.PersistableBundle extras) {
        super.toExtras(extras);
        extras.putInt(EXTRA_DEVICE_SLEEP_TIME_POSITION, this.mDeviceSleepTimePosition);
        extras.putInt(EXTRA_DEVICE_IDLE_TIME_POSITION, this.mDeviceIdleTimePosition);
        extras.putInt(EXTRA_DEVICE_SCAN_TIME_POSITION, this.mDeviceScanTimePosition);
        extras.putInt(EXTRA_DEVICE_CALL_TIME_POSITION, this.mDeviceCallTimePosition);
        extras.putInt(EXTRA_DEVICE_CALL_POWER_POSITION, this.mDeviceCallPowerPosition);
        extras.putInt(EXTRA_STATE_RX_TIME_POSITION, this.mStateRxTimePosition);
        extras.putInt(EXTRA_STATE_TX_TIMES_POSITION, this.mStateTxTimesPosition);
        extras.putInt(EXTRA_STATE_TX_TIMES_COUNT, this.mStateTxTimesCount);
        extras.putInt(EXTRA_UID_RX_BYTES_POSITION, this.mUidRxBytesPosition);
        extras.putInt(EXTRA_UID_TX_BYTES_POSITION, this.mUidTxBytesPosition);
        extras.putInt(EXTRA_UID_RX_PACKETS_POSITION, this.mUidRxPacketsPosition);
        extras.putInt(EXTRA_UID_TX_PACKETS_POSITION, this.mUidTxPacketsPosition);
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void fromExtras(android.os.PersistableBundle extras) {
        super.fromExtras(extras);
        this.mDeviceSleepTimePosition = extras.getInt(EXTRA_DEVICE_SLEEP_TIME_POSITION);
        this.mDeviceIdleTimePosition = extras.getInt(EXTRA_DEVICE_IDLE_TIME_POSITION);
        this.mDeviceScanTimePosition = extras.getInt(EXTRA_DEVICE_SCAN_TIME_POSITION);
        this.mDeviceCallTimePosition = extras.getInt(EXTRA_DEVICE_CALL_TIME_POSITION);
        this.mDeviceCallPowerPosition = extras.getInt(EXTRA_DEVICE_CALL_POWER_POSITION);
        this.mStateRxTimePosition = extras.getInt(EXTRA_STATE_RX_TIME_POSITION);
        this.mStateTxTimesPosition = extras.getInt(EXTRA_STATE_TX_TIMES_POSITION);
        this.mStateTxTimesCount = extras.getInt(EXTRA_STATE_TX_TIMES_COUNT);
        this.mUidRxBytesPosition = extras.getInt(EXTRA_UID_RX_BYTES_POSITION);
        this.mUidTxBytesPosition = extras.getInt(EXTRA_UID_TX_BYTES_POSITION);
        this.mUidRxPacketsPosition = extras.getInt(EXTRA_UID_RX_PACKETS_POSITION);
        this.mUidTxPacketsPosition = extras.getInt(EXTRA_UID_TX_PACKETS_POSITION);
    }

    public void addRxTxTimesForRat(android.util.SparseArray<long[]> stateStats, int networkType, int freqRange, long rxTime, int[] txTime) {
        if (txTime.length != this.mStateTxTimesCount) {
            android.util.Slog.wtf(TAG, "Invalid TX time array size: " + txTime.length);
            return;
        }
        boolean nonZero = false;
        if (rxTime == 0) {
            int i = txTime.length - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                if (txTime[i] != 0) {
                    nonZero = true;
                    break;
                }
                i--;
            }
        } else {
            nonZero = true;
        }
        if (!nonZero) {
            return;
        }
        int rat = com.android.server.power.stats.MobileRadioPowerStatsCollector.mapRadioAccessNetworkTypeToRadioAccessTechnology(networkType);
        int stateKey = com.android.server.power.stats.MobileRadioPowerStatsCollector.makeStateKey(rat, freqRange);
        long[] stats = stateStats.get(stateKey);
        if (stats == null) {
            stats = new long[getStateStatsArrayLength()];
            stateStats.put(stateKey, stats);
        }
        int i2 = this.mStateRxTimePosition;
        stats[i2] = stats[i2] + rxTime;
        for (int i3 = this.mStateTxTimesCount - 1; i3 >= 0; i3--) {
            int i4 = this.mStateTxTimesPosition + i3;
            stats[i4] = stats[i4] + ((long) txTime[i3]);
        }
    }
}
