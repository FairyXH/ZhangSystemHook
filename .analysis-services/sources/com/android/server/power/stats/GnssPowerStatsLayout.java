package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class GnssPowerStatsLayout extends com.android.server.power.stats.BinaryStatePowerStatsLayout {
    private static final java.lang.String EXTRA_DEVICE_TIME_SIGNAL_LEVEL_POSITION = "dt-sig";
    private static final java.lang.String EXTRA_UID_TIME_SIGNAL_LEVEL_POSITION = "ut-sig";
    private int mDeviceSignalLevelTimePosition = addDeviceSection(2, "level");
    private int mUidSignalLevelTimePosition = addUidSection(2, "level");

    GnssPowerStatsLayout() {
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void fromExtras(android.os.PersistableBundle extras) {
        super.fromExtras(extras);
        this.mDeviceSignalLevelTimePosition = extras.getInt(EXTRA_DEVICE_TIME_SIGNAL_LEVEL_POSITION);
        this.mUidSignalLevelTimePosition = extras.getInt(EXTRA_UID_TIME_SIGNAL_LEVEL_POSITION);
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void toExtras(android.os.PersistableBundle extras) {
        super.toExtras(extras);
        extras.putInt(EXTRA_DEVICE_TIME_SIGNAL_LEVEL_POSITION, this.mDeviceSignalLevelTimePosition);
        extras.putInt(EXTRA_UID_TIME_SIGNAL_LEVEL_POSITION, this.mUidSignalLevelTimePosition);
    }

    public void setDeviceSignalLevelTime(long[] stats, int signalLevel, long durationMillis) {
        stats[this.mDeviceSignalLevelTimePosition + signalLevel] = durationMillis;
    }

    public long getDeviceSignalLevelTime(long[] stats, int signalLevel) {
        return stats[this.mDeviceSignalLevelTimePosition + signalLevel];
    }

    public void setUidSignalLevelTime(long[] stats, int signalLevel, long durationMillis) {
        stats[this.mUidSignalLevelTimePosition + signalLevel] = durationMillis;
    }

    public long getUidSignalLevelTime(long[] stats, int signalLevel) {
        return stats[this.mUidSignalLevelTimePosition + signalLevel];
    }
}
