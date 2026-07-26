package android.os;

/* JADX INFO: loaded from: classes.dex */
public abstract class BatteryStatsInternal {
    public static final int CPU_WAKEUP_SUBSYSTEM_ALARM = 1;
    public static final int CPU_WAKEUP_SUBSYSTEM_CELLULAR_DATA = 5;
    public static final int CPU_WAKEUP_SUBSYSTEM_SENSOR = 4;
    public static final int CPU_WAKEUP_SUBSYSTEM_SOUND_TRIGGER = 3;
    public static final int CPU_WAKEUP_SUBSYSTEM_UNKNOWN = -1;
    public static final int CPU_WAKEUP_SUBSYSTEM_WIFI = 2;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface CpuWakeupSubsystem {
    }

    public abstract java.util.List<android.os.BatteryUsageStats> getBatteryUsageStats(java.util.List<android.os.BatteryUsageStatsQuery> list);

    public abstract java.lang.String[] getMobileIfaces();

    public abstract com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes getSystemServiceCpuThreadTimes();

    public abstract java.lang.String[] getWifiIfaces();

    public abstract void noteBinderCallStats(int i, long j, java.util.Collection<com.android.internal.os.BinderCallsStats.CallStat> collection);

    public abstract void noteBinderThreadNativeIds(int[] iArr);

    public abstract void noteCpuWakingNetworkPacket(android.net.Network network, long j, int i);

    public abstract void noteJobsDeferred(int i, int i2, long j);

    public abstract void noteWakingAlarmBatch(long j, int... iArr);

    public abstract void noteWakingSoundTrigger(long j, int i);
}
