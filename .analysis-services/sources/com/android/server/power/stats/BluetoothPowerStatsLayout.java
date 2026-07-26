package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BluetoothPowerStatsLayout extends com.android.server.power.stats.PowerStatsLayout {
    private static final java.lang.String EXTRA_DEVICE_IDLE_TIME_POSITION = "dt-idle";
    private static final java.lang.String EXTRA_DEVICE_RX_TIME_POSITION = "dt-rx";
    private static final java.lang.String EXTRA_DEVICE_SCAN_TIME_POSITION = "dt-scan";
    private static final java.lang.String EXTRA_DEVICE_TX_TIME_POSITION = "dt-tx";
    private static final java.lang.String EXTRA_UID_RX_BYTES_POSITION = "ub-rx";
    private static final java.lang.String EXTRA_UID_SCAN_TIME_POSITION = "ut-scan";
    private static final java.lang.String EXTRA_UID_TX_BYTES_POSITION = "ub-tx";
    private int mDeviceIdleTimePosition;
    private int mDeviceRxTimePosition;
    private int mDeviceScanTimePosition;
    private int mDeviceTxTimePosition;
    private int mUidRxBytesPosition;
    private int mUidScanTimePosition;
    private int mUidTxBytesPosition;

    BluetoothPowerStatsLayout() {
    }

    BluetoothPowerStatsLayout(com.android.internal.os.PowerStats.Descriptor descriptor) {
        super(descriptor);
    }

    void addDeviceBluetoothControllerActivity() {
        this.mDeviceRxTimePosition = addDeviceSection(1, "rx");
        this.mDeviceTxTimePosition = addDeviceSection(1, "tx");
        this.mDeviceIdleTimePosition = addDeviceSection(1, "idle");
        this.mDeviceScanTimePosition = addDeviceSection(1, "scan", 1);
    }

    void addUidTrafficStats() {
        this.mUidRxBytesPosition = addUidSection(1, "rx-B");
        this.mUidTxBytesPosition = addUidSection(1, "tx-B");
        this.mUidScanTimePosition = addUidSection(1, "scan", 1);
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

    public void setUidScanTime(long[] stats, long count) {
        stats[this.mUidScanTimePosition] = count;
    }

    public long getUidScanTime(long[] stats) {
        return stats[this.mUidScanTimePosition];
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void toExtras(android.os.PersistableBundle extras) {
        super.toExtras(extras);
        extras.putInt(EXTRA_DEVICE_RX_TIME_POSITION, this.mDeviceRxTimePosition);
        extras.putInt(EXTRA_DEVICE_TX_TIME_POSITION, this.mDeviceTxTimePosition);
        extras.putInt(EXTRA_DEVICE_IDLE_TIME_POSITION, this.mDeviceIdleTimePosition);
        extras.putInt(EXTRA_DEVICE_SCAN_TIME_POSITION, this.mDeviceScanTimePosition);
        extras.putInt(EXTRA_UID_RX_BYTES_POSITION, this.mUidRxBytesPosition);
        extras.putInt(EXTRA_UID_TX_BYTES_POSITION, this.mUidTxBytesPosition);
        extras.putInt(EXTRA_UID_SCAN_TIME_POSITION, this.mUidScanTimePosition);
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void fromExtras(android.os.PersistableBundle extras) {
        super.fromExtras(extras);
        this.mDeviceRxTimePosition = extras.getInt(EXTRA_DEVICE_RX_TIME_POSITION);
        this.mDeviceTxTimePosition = extras.getInt(EXTRA_DEVICE_TX_TIME_POSITION);
        this.mDeviceIdleTimePosition = extras.getInt(EXTRA_DEVICE_IDLE_TIME_POSITION);
        this.mDeviceScanTimePosition = extras.getInt(EXTRA_DEVICE_SCAN_TIME_POSITION);
        this.mUidRxBytesPosition = extras.getInt(EXTRA_UID_RX_BYTES_POSITION);
        this.mUidTxBytesPosition = extras.getInt(EXTRA_UID_TX_BYTES_POSITION);
        this.mUidScanTimePosition = extras.getInt(EXTRA_UID_SCAN_TIME_POSITION);
    }
}
