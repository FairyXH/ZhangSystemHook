package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CpuPowerStatsLayout extends com.android.server.power.stats.PowerStatsLayout {
    private static final java.lang.String EXTRA_DEVICE_TIME_BY_CLUSTER_COUNT = "dcc";
    private static final java.lang.String EXTRA_DEVICE_TIME_BY_CLUSTER_POSITION = "dc";
    private static final java.lang.String EXTRA_DEVICE_TIME_BY_SCALING_STEP_COUNT = "dtc";
    private static final java.lang.String EXTRA_DEVICE_TIME_BY_SCALING_STEP_POSITION = "dt";
    private static final java.lang.String EXTRA_UID_BRACKETS_POSITION = "ub";
    private static final java.lang.String EXTRA_UID_STATS_SCALING_STEP_TO_POWER_BRACKET = "us";
    private int mDeviceCpuTimeByClusterCount;
    private int mDeviceCpuTimeByClusterPosition;
    private int mDeviceCpuTimeByScalingStepCount;
    private int mDeviceCpuTimeByScalingStepPosition;
    private int[] mScalingStepToPowerBracketMap;
    private int mUidPowerBracketCount;
    private int mUidPowerBracketsPosition;

    public void addDeviceSectionCpuTimeByScalingStep(int scalingStepCount) {
        this.mDeviceCpuTimeByScalingStepPosition = addDeviceSection(scalingStepCount, "steps");
        this.mDeviceCpuTimeByScalingStepCount = scalingStepCount;
    }

    public int getCpuScalingStepCount() {
        return this.mDeviceCpuTimeByScalingStepCount;
    }

    public void setTimeByScalingStep(long[] stats, int step, long value) {
        stats[this.mDeviceCpuTimeByScalingStepPosition + step] = value;
    }

    public long getTimeByScalingStep(long[] stats, int step) {
        return stats[this.mDeviceCpuTimeByScalingStepPosition + step];
    }

    public void addDeviceSectionCpuTimeByCluster(int clusterCount) {
        this.mDeviceCpuTimeByClusterPosition = addDeviceSection(clusterCount, "clusters");
        this.mDeviceCpuTimeByClusterCount = clusterCount;
    }

    public int getCpuClusterCount() {
        return this.mDeviceCpuTimeByClusterCount;
    }

    public void setTimeByCluster(long[] stats, int cluster, long value) {
        stats[this.mDeviceCpuTimeByClusterPosition + cluster] = value;
    }

    public long getTimeByCluster(long[] stats, int cluster) {
        return stats[this.mDeviceCpuTimeByClusterPosition + cluster];
    }

    public void addUidSectionCpuTimeByPowerBracket(int[] scalingStepToPowerBracketMap) {
        this.mScalingStepToPowerBracketMap = scalingStepToPowerBracketMap;
        updatePowerBracketCount();
        this.mUidPowerBracketsPosition = addUidSection(this.mUidPowerBracketCount, "time");
    }

    private void updatePowerBracketCount() {
        this.mUidPowerBracketCount = 1;
        for (int bracket : this.mScalingStepToPowerBracketMap) {
            if (bracket >= this.mUidPowerBracketCount) {
                this.mUidPowerBracketCount = bracket + 1;
            }
        }
    }

    public int[] getScalingStepToPowerBracketMap() {
        return this.mScalingStepToPowerBracketMap;
    }

    public int getCpuPowerBracketCount() {
        return this.mUidPowerBracketCount;
    }

    public void setUidTimeByPowerBracket(long[] stats, int bracket, long value) {
        stats[this.mUidPowerBracketsPosition + bracket] = value;
    }

    public long getUidTimeByPowerBracket(long[] stats, int bracket) {
        return stats[this.mUidPowerBracketsPosition + bracket];
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void toExtras(android.os.PersistableBundle extras) {
        super.toExtras(extras);
        extras.putInt(EXTRA_DEVICE_TIME_BY_SCALING_STEP_POSITION, this.mDeviceCpuTimeByScalingStepPosition);
        extras.putInt(EXTRA_DEVICE_TIME_BY_SCALING_STEP_COUNT, this.mDeviceCpuTimeByScalingStepCount);
        extras.putInt(EXTRA_DEVICE_TIME_BY_CLUSTER_POSITION, this.mDeviceCpuTimeByClusterPosition);
        extras.putInt(EXTRA_DEVICE_TIME_BY_CLUSTER_COUNT, this.mDeviceCpuTimeByClusterCount);
        extras.putInt(EXTRA_UID_BRACKETS_POSITION, this.mUidPowerBracketsPosition);
        putIntArray(extras, EXTRA_UID_STATS_SCALING_STEP_TO_POWER_BRACKET, this.mScalingStepToPowerBracketMap);
    }

    @Override // com.android.server.power.stats.PowerStatsLayout
    public void fromExtras(android.os.PersistableBundle extras) {
        super.fromExtras(extras);
        this.mDeviceCpuTimeByScalingStepPosition = extras.getInt(EXTRA_DEVICE_TIME_BY_SCALING_STEP_POSITION);
        this.mDeviceCpuTimeByScalingStepCount = extras.getInt(EXTRA_DEVICE_TIME_BY_SCALING_STEP_COUNT);
        this.mDeviceCpuTimeByClusterPosition = extras.getInt(EXTRA_DEVICE_TIME_BY_CLUSTER_POSITION);
        this.mDeviceCpuTimeByClusterCount = extras.getInt(EXTRA_DEVICE_TIME_BY_CLUSTER_COUNT);
        this.mUidPowerBracketsPosition = extras.getInt(EXTRA_UID_BRACKETS_POSITION);
        this.mScalingStepToPowerBracketMap = getIntArray(extras, EXTRA_UID_STATS_SCALING_STEP_TO_POWER_BRACKET);
        if (this.mScalingStepToPowerBracketMap == null) {
            this.mScalingStepToPowerBracketMap = new int[this.mDeviceCpuTimeByScalingStepCount];
        }
        updatePowerBracketCount();
    }
}
