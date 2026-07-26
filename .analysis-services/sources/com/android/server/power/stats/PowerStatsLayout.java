package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsLayout {
    private static final java.lang.String EXTRA_DEVICE_DURATION_POSITION = "dd";
    private static final java.lang.String EXTRA_DEVICE_ENERGY_CONSUMERS_COUNT = "dec";
    private static final java.lang.String EXTRA_DEVICE_ENERGY_CONSUMERS_POSITION = "de";
    private static final java.lang.String EXTRA_DEVICE_POWER_POSITION = "dp";
    private static final java.lang.String EXTRA_UID_DURATION_POSITION = "ud";
    private static final java.lang.String EXTRA_UID_POWER_POSITION = "up";
    protected static final int FLAG_FORMAT_AS_POWER = 4;
    protected static final int FLAG_HIDDEN = 2;
    protected static final int FLAG_OPTIONAL = 1;
    protected static final double MILLI_TO_NANO_MULTIPLIER = 1000000.0d;
    private static final java.lang.String TAG = "PowerStatsLayout";
    protected static final int UNSUPPORTED = -1;
    private int mDeviceEnergyConsumerCount;
    private int mDeviceEnergyConsumerPosition;
    private int mDeviceStatsArrayLength;
    private int mStateStatsArrayLength;
    private int mUidStatsArrayLength;
    private java.lang.StringBuilder mDeviceFormat = new java.lang.StringBuilder();
    private java.lang.StringBuilder mStateFormat = new java.lang.StringBuilder();
    private java.lang.StringBuilder mUidFormat = new java.lang.StringBuilder();
    protected int mDeviceDurationPosition = -1;
    private int mDevicePowerEstimatePosition = -1;
    private int mUidDurationPosition = -1;
    private int mUidPowerEstimatePosition = -1;

    public PowerStatsLayout() {
    }

    public PowerStatsLayout(com.android.internal.os.PowerStats.Descriptor descriptor) {
        fromExtras(descriptor.extras);
    }

    public int getDeviceStatsArrayLength() {
        return this.mDeviceStatsArrayLength;
    }

    public int getStateStatsArrayLength() {
        return this.mStateStatsArrayLength;
    }

    public int getUidStatsArrayLength() {
        return this.mUidStatsArrayLength;
    }

    private void appendFormat(java.lang.StringBuilder sb, int position, int length, java.lang.String label, int flags) {
        if ((flags & 2) != 0) {
            return;
        }
        if (!sb.isEmpty()) {
            sb.append(' ');
        }
        sb.append(label).append(':');
        sb.append(position);
        if (length != 1) {
            sb.append('[').append(length).append(']');
        }
        if ((flags & 4) != 0) {
            sb.append('p');
        }
        if ((flags & 1) != 0) {
            sb.append('?');
        }
    }

    protected int addDeviceSection(int length, java.lang.String label, int flags) {
        int position = this.mDeviceStatsArrayLength;
        this.mDeviceStatsArrayLength += length;
        appendFormat(this.mDeviceFormat, position, length, label, flags);
        return position;
    }

    protected int addDeviceSection(int length, java.lang.String label) {
        return addDeviceSection(length, label, 0);
    }

    protected int addStateSection(int length, java.lang.String label, int flags) {
        int position = this.mStateStatsArrayLength;
        this.mStateStatsArrayLength += length;
        appendFormat(this.mStateFormat, position, length, label, flags);
        return position;
    }

    protected int addStateSection(int length, java.lang.String label) {
        return addStateSection(length, label, 0);
    }

    protected int addUidSection(int length, java.lang.String label, int flags) {
        int position = this.mUidStatsArrayLength;
        this.mUidStatsArrayLength += length;
        appendFormat(this.mUidFormat, position, length, label, flags);
        return position;
    }

    protected int addUidSection(int length, java.lang.String label) {
        return addUidSection(length, label, 0);
    }

    public void addDeviceSectionUsageDuration() {
        this.mDeviceDurationPosition = addDeviceSection(1, "usage", 1);
    }

    public void setUsageDuration(long[] stats, long value) {
        stats[this.mDeviceDurationPosition] = value;
    }

    public long getUsageDuration(long[] stats) {
        return stats[this.mDeviceDurationPosition];
    }

    public void addDeviceSectionEnergyConsumers(int energyConsumerCount) {
        this.mDeviceEnergyConsumerPosition = addDeviceSection(energyConsumerCount, "energy", 1);
        this.mDeviceEnergyConsumerCount = energyConsumerCount;
    }

    public int getEnergyConsumerCount() {
        return this.mDeviceEnergyConsumerCount;
    }

    public void setConsumedEnergy(long[] stats, int index, long energy) {
        stats[this.mDeviceEnergyConsumerPosition + index] = energy;
    }

    public long getConsumedEnergy(long[] stats, int index) {
        return stats[this.mDeviceEnergyConsumerPosition + index];
    }

    public void addDeviceSectionPowerEstimate() {
        this.mDevicePowerEstimatePosition = addDeviceSection(1, "power", 5);
    }

    public void setDevicePowerEstimate(long[] stats, double power) {
        stats[this.mDevicePowerEstimatePosition] = (long) (MILLI_TO_NANO_MULTIPLIER * power);
    }

    public double getDevicePowerEstimate(long[] stats) {
        return stats[this.mDevicePowerEstimatePosition] / MILLI_TO_NANO_MULTIPLIER;
    }

    public void addUidSectionUsageDuration() {
        this.mUidDurationPosition = addUidSection(1, "time");
    }

    public void addUidSectionPowerEstimate() {
        this.mUidPowerEstimatePosition = addUidSection(1, "power", 5);
    }

    public boolean isUidPowerAttributionSupported() {
        return this.mUidPowerEstimatePosition != -1;
    }

    public void setUidUsageDuration(long[] stats, long durationMs) {
        stats[this.mUidDurationPosition] = durationMs;
    }

    public long getUidUsageDuration(long[] stats) {
        return stats[this.mUidDurationPosition];
    }

    public void setUidPowerEstimate(long[] stats, double power) {
        stats[this.mUidPowerEstimatePosition] = (long) (MILLI_TO_NANO_MULTIPLIER * power);
    }

    public double getUidPowerEstimate(long[] stats) {
        return stats[this.mUidPowerEstimatePosition] / MILLI_TO_NANO_MULTIPLIER;
    }

    public void toExtras(android.os.PersistableBundle extras) {
        extras.putInt(EXTRA_DEVICE_DURATION_POSITION, this.mDeviceDurationPosition);
        extras.putInt(EXTRA_DEVICE_ENERGY_CONSUMERS_POSITION, this.mDeviceEnergyConsumerPosition);
        extras.putInt(EXTRA_DEVICE_ENERGY_CONSUMERS_COUNT, this.mDeviceEnergyConsumerCount);
        extras.putInt(EXTRA_DEVICE_POWER_POSITION, this.mDevicePowerEstimatePosition);
        extras.putInt(EXTRA_UID_DURATION_POSITION, this.mUidDurationPosition);
        extras.putInt("up", this.mUidPowerEstimatePosition);
        extras.putString("format-device", this.mDeviceFormat.toString());
        extras.putString("format-state", this.mStateFormat.toString());
        extras.putString("format-uid", this.mUidFormat.toString());
    }

    public void fromExtras(android.os.PersistableBundle extras) {
        this.mDeviceDurationPosition = extras.getInt(EXTRA_DEVICE_DURATION_POSITION);
        this.mDeviceEnergyConsumerPosition = extras.getInt(EXTRA_DEVICE_ENERGY_CONSUMERS_POSITION);
        this.mDeviceEnergyConsumerCount = extras.getInt(EXTRA_DEVICE_ENERGY_CONSUMERS_COUNT);
        this.mDevicePowerEstimatePosition = extras.getInt(EXTRA_DEVICE_POWER_POSITION);
        this.mUidDurationPosition = extras.getInt(EXTRA_UID_DURATION_POSITION);
        this.mUidPowerEstimatePosition = extras.getInt("up");
    }

    protected void putIntArray(android.os.PersistableBundle extras, java.lang.String key, int[] array) {
        if (array == null) {
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int value : array) {
            if (!sb.isEmpty()) {
                sb.append(',');
            }
            sb.append(value);
        }
        extras.putString(key, sb.toString());
    }

    protected int[] getIntArray(android.os.PersistableBundle extras, java.lang.String key) {
        java.lang.String string = extras.getString(key);
        if (string == null) {
            return null;
        }
        java.lang.String[] values = string.trim().split(",");
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                result[i] = java.lang.Integer.parseInt(values[i]);
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.wtf(TAG, "Invalid CSV format: " + string);
                return null;
            }
        }
        return result;
    }
}
