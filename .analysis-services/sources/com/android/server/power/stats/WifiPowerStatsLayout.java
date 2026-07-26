package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class WifiPowerStatsLayout extends com.android.server.power.stats.PowerStatsLayout {
    private static final java.lang.String EXTRA_DEVICE_ACTIVE_TIME_POSITION = "dt-on";
    private static final java.lang.String EXTRA_DEVICE_BASIC_SCAN_TIME_POSITION = "dt-basic-scan";
    private static final java.lang.String EXTRA_DEVICE_BATCHED_SCAN_TIME_POSITION = "dt-batch-scan";
    private static final java.lang.String EXTRA_DEVICE_IDLE_TIME_POSITION = "dt-idle";
    private static final java.lang.String EXTRA_DEVICE_RX_TIME_POSITION = "dt-rx";
    private static final java.lang.String EXTRA_DEVICE_SCAN_TIME_POSITION = "dt-scan";
    private static final java.lang.String EXTRA_DEVICE_TX_TIME_POSITION = "dt-tx";
    private static final java.lang.String EXTRA_POWER_REPORTING_SUPPORTED = "prs";
    private static final java.lang.String EXTRA_UID_BATCH_SCAN_TIME_POSITION = "ut-bscan";
    private static final java.lang.String EXTRA_UID_RX_BYTES_POSITION = "urxb";
    private static final java.lang.String EXTRA_UID_RX_PACKETS_POSITION = "urxp";
    private static final java.lang.String EXTRA_UID_SCAN_TIME_POSITION = "ut-scan";
    private static final java.lang.String EXTRA_UID_TX_BYTES_POSITION = "utxb";
    private static final java.lang.String EXTRA_UID_TX_PACKETS_POSITION = "utxp";
    private static final java.lang.String TAG = "WifiPowerStatsLayout";
    private static final int UNSPECIFIED = -1;
    private int mDeviceActiveTimePosition;
    private int mDeviceBasicScanTimePosition;
    private int mDeviceBatchedScanTimePosition;
    private int mDeviceIdleTimePosition;
    private int mDeviceRxTimePosition;
    private int mDeviceScanTimePosition;
    private int mDeviceTxTimePosition;
    private boolean mPowerReportingSupported;
    private int mUidBatchScanTimePosition;
    private int mUidRxBytesPosition;
    private int mUidRxPacketsPosition;
    private int mUidScanTimePosition;
    private int mUidTxBytesPosition;
    private int mUidTxPacketsPosition;

    WifiPowerStatsLayout() {
    }

    WifiPowerStatsLayout(com.android.internal.os.PowerStats.Descriptor descriptor) {
        super(descriptor);
    }

    void addDeviceWifiActivity(boolean powerReportingSupported) {
        this.mPowerReportingSupported = powerReportingSupported;
        if (this.mPowerReportingSupported) {
            this.mDeviceActiveTimePosition = -1;
            this.mDeviceRxTimePosition = addDeviceSection(1, "rx");
            this.mDeviceTxTimePosition = addDeviceSection(1, "tx");
            this.mDeviceIdleTimePosition = addDeviceSection(1, "idle");
            this.mDeviceScanTimePosition = addDeviceSection(1, "scan");
        } else {
            this.mDeviceActiveTimePosition = addDeviceSection(1, "rx-tx");
            this.mDeviceRxTimePosition = -1;
            this.mDeviceTxTimePosition = -1;
            this.mDeviceIdleTimePosition = -1;
            this.mDeviceScanTimePosition = -1;
        }
        this.mDeviceBasicScanTimePosition = addDeviceSection(1, "basic-scan", 1);
        this.mDeviceBatchedScanTimePosition = addDeviceSection(1, "batched-scan", 1);
    }

    void addUidNetworkStats() {
        this.mUidRxPacketsPosition = addUidSection(1, "rx-pkts");
        this.mUidRxBytesPosition = addUidSection(1, "rx-B");
        this.mUidTxPacketsPosition = addUidSection(1, "tx-pkts");
        this.mUidTxBytesPosition = addUidSection(1, "tx-B");
        this.mUidScanTimePosition = addUidSection(1, "scan", 1);
        this.mUidBatchScanTimePosition = addUidSection(1, "batched-scan", 1);
    }

    public boolean isPowerReportingSupported() {
        return this.mPowerReportingSupported;
    }

    public void setDeviceRxTime(long[] stats, long durationMillis) {
        stats[this.mDeviceRxTimePosition] = durationMillis;
    }

    public long getDeviceRxTime(long[] stats) {
        return stats[this.mDeviceRxTimePosition];
    }

    public void setDeviceTxTime(long[] stats, long durationMillis) {
        stats[this.mDeviceTxTimePosition] = durationMillis;
    }

    public long getDeviceTxTime(long[] stats) {
        return stats[this.mDeviceTxTimePosition];
    }

    public void setDeviceScanTime(long[] stats, long durationMillis) {
        stats[this.mDeviceScanTimePosition] = durationMillis;
    }

    public long getDeviceScanTime(long[] stats) {
        return stats[this.mDeviceScanTimePosition];
    }

    public void setDeviceBasicScanTime(long[] stats, long durationMillis) {
        stats[this.mDeviceBasicScanTimePosition] = durationMillis;
    }

    public long getDeviceBasicScanTime(long[] stats) {
        return stats[this.mDeviceBasicScanTimePosition];
    }

    public void setDeviceBatchedScanTime(long[] stats, long durationMillis) {
        stats[this.mDeviceBatchedScanTimePosition] = durationMillis;
    }

    public long getDeviceBatchedScanTime(long[] stats) {
        return stats[this.mDeviceBatchedScanTimePosition];
    }

    public void setDeviceIdleTime(long[] stats, long durationMillis) {
        stats[this.mDeviceIdleTimePosition] = durationMillis;
    }

    public long getDeviceIdleTime(long[] stats) {
        return stats[this.mDeviceIdleTimePosition];
    }

    public void setDeviceActiveTime(long[] stats, long durationMillis) {
        stats[this.mDeviceActiveTimePosition] = durationMillis;
    }

    public long getDeviceActiveTime(long[] stats) {
        return stats[this.mDeviceActiveTimePosition];
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

    public void setUidScanTime(long[] stats, long count) {
        stats[this.mUidScanTimePosition] = count;
    }

    public long getUidScanTime(long[] stats) {
        return stats[this.mUidScanTimePosition];
    }

    public void setUidBatchScanTime(long[] stats, long count) {
        stats[this.mUidBatchScanTimePosition] = count;
    }

    public long getUidBatchedScanTime(long[] stats) {
        return stats[this.mUidBatchScanTimePosition];
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void toExtras(android.os.PersistableBundle extras) {
        super.toExtras(extras);
        extras.putBoolean(EXTRA_POWER_REPORTING_SUPPORTED, this.mPowerReportingSupported);
        extras.putInt(EXTRA_DEVICE_RX_TIME_POSITION, this.mDeviceRxTimePosition);
        extras.putInt(EXTRA_DEVICE_TX_TIME_POSITION, this.mDeviceTxTimePosition);
        extras.putInt(EXTRA_DEVICE_SCAN_TIME_POSITION, this.mDeviceScanTimePosition);
        extras.putInt(EXTRA_DEVICE_BASIC_SCAN_TIME_POSITION, this.mDeviceBasicScanTimePosition);
        extras.putInt(EXTRA_DEVICE_BATCHED_SCAN_TIME_POSITION, this.mDeviceBatchedScanTimePosition);
        extras.putInt(EXTRA_DEVICE_IDLE_TIME_POSITION, this.mDeviceIdleTimePosition);
        extras.putInt(EXTRA_DEVICE_ACTIVE_TIME_POSITION, this.mDeviceActiveTimePosition);
        extras.putInt(EXTRA_UID_RX_BYTES_POSITION, this.mUidRxBytesPosition);
        extras.putInt(EXTRA_UID_TX_BYTES_POSITION, this.mUidTxBytesPosition);
        extras.putInt(EXTRA_UID_RX_PACKETS_POSITION, this.mUidRxPacketsPosition);
        extras.putInt(EXTRA_UID_TX_PACKETS_POSITION, this.mUidTxPacketsPosition);
        extras.putInt(EXTRA_UID_SCAN_TIME_POSITION, this.mUidScanTimePosition);
        extras.putInt(EXTRA_UID_BATCH_SCAN_TIME_POSITION, this.mUidBatchScanTimePosition);
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void fromExtras(android.os.PersistableBundle extras) {
        super.fromExtras(extras);
        this.mPowerReportingSupported = extras.getBoolean(EXTRA_POWER_REPORTING_SUPPORTED);
        this.mDeviceRxTimePosition = extras.getInt(EXTRA_DEVICE_RX_TIME_POSITION);
        this.mDeviceTxTimePosition = extras.getInt(EXTRA_DEVICE_TX_TIME_POSITION);
        this.mDeviceScanTimePosition = extras.getInt(EXTRA_DEVICE_SCAN_TIME_POSITION);
        this.mDeviceBasicScanTimePosition = extras.getInt(EXTRA_DEVICE_BASIC_SCAN_TIME_POSITION);
        this.mDeviceBatchedScanTimePosition = extras.getInt(EXTRA_DEVICE_BATCHED_SCAN_TIME_POSITION);
        this.mDeviceIdleTimePosition = extras.getInt(EXTRA_DEVICE_IDLE_TIME_POSITION);
        this.mDeviceActiveTimePosition = extras.getInt(EXTRA_DEVICE_ACTIVE_TIME_POSITION);
        this.mUidRxBytesPosition = extras.getInt(EXTRA_UID_RX_BYTES_POSITION);
        this.mUidTxBytesPosition = extras.getInt(EXTRA_UID_TX_BYTES_POSITION);
        this.mUidRxPacketsPosition = extras.getInt(EXTRA_UID_RX_PACKETS_POSITION);
        this.mUidTxPacketsPosition = extras.getInt(EXTRA_UID_TX_PACKETS_POSITION);
        this.mUidScanTimePosition = extras.getInt(EXTRA_UID_SCAN_TIME_POSITION);
        this.mUidBatchScanTimePosition = extras.getInt(EXTRA_UID_BATCH_SCAN_TIME_POSITION);
    }
}
