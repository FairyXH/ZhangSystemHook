package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class HealthStatsBatteryStatsWriter {
    private final long mNowRealtimeMs = android.os.SystemClock.elapsedRealtime();
    private final long mNowUptimeMs = android.os.SystemClock.uptimeMillis();

    public void writeUid(android.os.health.HealthStatsWriter uidWriter, android.os.BatteryStats bs, android.os.BatteryStats.Uid uid) {
        uidWriter.addMeasurement(10001, bs.computeBatteryRealtime(this.mNowRealtimeMs * 1000, 0) / 1000);
        uidWriter.addMeasurement(10002, bs.computeBatteryUptime(this.mNowUptimeMs * 1000, 0) / 1000);
        uidWriter.addMeasurement(10003, bs.computeBatteryScreenOffRealtime(this.mNowRealtimeMs * 1000, 0) / 1000);
        uidWriter.addMeasurement(10004, bs.computeBatteryScreenOffUptime(this.mNowUptimeMs * 1000, 0) / 1000);
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Uid.Wakelock> entry : uid.getWakelockStats().entrySet()) {
            java.lang.String key = entry.getKey();
            android.os.BatteryStats.Uid.Wakelock wakelock = (android.os.BatteryStats.Uid.Wakelock) entry.getValue();
            android.os.BatteryStats.Timer timer = wakelock.getWakeTime(1);
            addTimers(uidWriter, 10005, key, timer);
            android.os.BatteryStats.Timer timer2 = wakelock.getWakeTime(0);
            addTimers(uidWriter, 10006, key, timer2);
            android.os.BatteryStats.Timer timer3 = wakelock.getWakeTime(2);
            addTimers(uidWriter, 10007, key, timer3);
            android.os.BatteryStats.Timer timer4 = wakelock.getWakeTime(18);
            addTimers(uidWriter, com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_CONTENT_PROFILE_ERROR_REPORTED__FILE_NAME__BLUETOOTH_OPP_SEND_FILE_INFO, key, timer4);
        }
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Timer> entry2 : uid.getSyncStats().entrySet()) {
            addTimers(uidWriter, 10009, entry2.getKey(), (android.os.BatteryStats.Timer) entry2.getValue());
        }
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Timer> entry3 : uid.getJobStats().entrySet()) {
            addTimers(uidWriter, 10010, entry3.getKey(), (android.os.BatteryStats.Timer) entry3.getValue());
        }
        android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> sensors = uid.getSensorStats();
        int N = sensors.size();
        for (int i = 0; i < N; i++) {
            int sensorId = sensors.keyAt(i);
            if (sensorId == -10000) {
                addTimer(uidWriter, 10011, ((android.os.BatteryStats.Uid.Sensor) sensors.valueAt(i)).getSensorTime());
            } else {
                addTimers(uidWriter, 10012, java.lang.Integer.toString(sensorId), ((android.os.BatteryStats.Uid.Sensor) sensors.valueAt(i)).getSensorTime());
            }
        }
        android.util.SparseArray<? extends android.os.BatteryStats.Uid.Pid> pids = uid.getPidStats();
        int N2 = pids.size();
        for (int i2 = 0; i2 < N2; i2++) {
            android.os.health.HealthStatsWriter writer = new android.os.health.HealthStatsWriter(android.os.health.PidHealthStats.CONSTANTS);
            writePid(writer, (android.os.BatteryStats.Uid.Pid) pids.valueAt(i2));
            uidWriter.addStats(10013, java.lang.Integer.toString(pids.keyAt(i2)), writer);
        }
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Uid.Proc> entry4 : uid.getProcessStats().entrySet()) {
            android.os.health.HealthStatsWriter writer2 = new android.os.health.HealthStatsWriter(android.os.health.ProcessHealthStats.CONSTANTS);
            writeProc(writer2, (android.os.BatteryStats.Uid.Proc) entry4.getValue());
            uidWriter.addStats(10014, entry4.getKey(), writer2);
        }
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Uid.Pkg> entry5 : uid.getPackageStats().entrySet()) {
            android.os.health.HealthStatsWriter writer3 = new android.os.health.HealthStatsWriter(android.os.health.PackageHealthStats.CONSTANTS);
            writePkg(writer3, (android.os.BatteryStats.Uid.Pkg) entry5.getValue());
            uidWriter.addStats(10015, entry5.getKey(), writer3);
        }
        android.os.BatteryStats.ControllerActivityCounter controller = uid.getWifiControllerActivity();
        if (controller != null) {
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.CPU_ACTIVE_TIME, controller.getIdleTimeCounter().getCountLocked(0));
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.CPU_CLUSTER_TIME, controller.getRxTimeCounter().getCountLocked(0));
            long sum = 0;
            for (android.os.BatteryStats.LongCounter counter : controller.getTxTimeCounters()) {
                sum += counter.getCountLocked(0);
            }
            uidWriter.addMeasurement(10018, sum);
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.REMAINING_BATTERY_CAPACITY, controller.getPowerCounter().getCountLocked(0));
        }
        android.os.BatteryStats.ControllerActivityCounter controller2 = uid.getBluetoothControllerActivity();
        if (controller2 != null) {
            uidWriter.addMeasurement(10020, controller2.getIdleTimeCounter().getCountLocked(0));
            uidWriter.addMeasurement(10021, controller2.getRxTimeCounter().getCountLocked(0));
            long sum2 = 0;
            for (android.os.BatteryStats.LongCounter counter2 : controller2.getTxTimeCounters()) {
                sum2 += counter2.getCountLocked(0);
            }
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.BINDER_CALLS, sum2);
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.BINDER_CALLS_EXCEPTIONS, controller2.getPowerCounter().getCountLocked(0));
        }
        android.os.BatteryStats.ControllerActivityCounter controller3 = uid.getModemControllerActivity();
        if (controller3 != null) {
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.LOOPER_STATS, controller3.getIdleTimeCounter().getCountLocked(0));
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.DISK_STATS, controller3.getRxTimeCounter().getCountLocked(0));
            long sum3 = 0;
            for (android.os.BatteryStats.LongCounter counter3 : controller3.getTxTimeCounters()) {
                sum3 += counter3.getCountLocked(0);
            }
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.DIRECTORY_USAGE, sum3);
            uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.APP_SIZE, controller3.getPowerCounter().getCountLocked(0));
        }
        long sum4 = this.mNowRealtimeMs;
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.CATEGORY_SIZE, uid.getWifiRunningTime(sum4 * 1000, 0) / 1000);
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.PROC_STATS, uid.getFullWifiLockTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        uidWriter.addTimer(com.android.internal.util.FrameworkStatsLog.BATTERY_VOLTAGE, uid.getWifiScanCount(0), uid.getWifiScanTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.NUM_FINGERPRINTS_ENROLLED, uid.getWifiMulticastTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.DISK_IO, uid.getAudioTurnedOnTimer());
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.POWER_PROFILE, uid.getVideoTurnedOnTimer());
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.PROC_STATS_PKG_PROC, uid.getFlashlightTurnedOnTimer());
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.PROCESS_CPU_TIME, uid.getCameraTurnedOnTimer());
        addTimer(uidWriter, 10036, uid.getForegroundActivityTimer());
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.CPU_TIME_PER_THREAD_FREQ, uid.getBluetoothScanTimer());
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.ON_DEVICE_POWER_MEASUREMENT, uid.getProcessStateTimer(0));
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.DEVICE_CALCULATED_POWER_USE, uid.getProcessStateTimer(1));
        addTimer(uidWriter, 10040, uid.getProcessStateTimer(4));
        addTimer(uidWriter, 10041, uid.getProcessStateTimer(2));
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.PROCESS_MEMORY_HIGH_WATER_MARK, uid.getProcessStateTimer(3));
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.BATTERY_LEVEL, uid.getProcessStateTimer(6));
        addTimer(uidWriter, com.android.internal.util.FrameworkStatsLog.BUILD_INFORMATION, uid.getVibratorOnTimer());
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.BATTERY_CYCLE_COUNT, uid.getUserActivityCount(0, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.DEBUG_ELAPSED_CLOCK, uid.getUserActivityCount(1, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.DEBUG_FAILING_ELAPSED_CLOCK, uid.getUserActivityCount(2, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.NUM_FACES_ENROLLED, uid.getNetworkActivityBytes(0, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.ROLE_HOLDER, uid.getNetworkActivityBytes(1, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.DANGEROUS_PERMISSION_STATE, uid.getNetworkActivityBytes(2, 0));
        uidWriter.addMeasurement(10051, uid.getNetworkActivityBytes(3, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.TIME_ZONE_DATA_INFO, uid.getNetworkActivityBytes(4, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.EXTERNAL_STORAGE_INFO, uid.getNetworkActivityBytes(5, 0));
        uidWriter.addMeasurement(10054, uid.getNetworkActivityPackets(0, 0));
        uidWriter.addMeasurement(10055, uid.getNetworkActivityPackets(1, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.SYSTEM_ION_HEAP_SIZE, uid.getNetworkActivityPackets(2, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.APPS_ON_EXTERNAL_STORAGE_INFO, uid.getNetworkActivityPackets(3, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.FACE_SETTINGS, uid.getNetworkActivityPackets(4, 0));
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.COOLING_DEVICE, uid.getNetworkActivityPackets(5, 0));
        uidWriter.addTimer(com.android.internal.util.FrameworkStatsLog.PROCESS_SYSTEM_ION_HEAP_SIZE, uid.getMobileRadioActiveCount(0), uid.getMobileRadioActiveTime(0));
        uidWriter.addMeasurement(10062, uid.getUserCpuTimeUs(0) / 1000);
        uidWriter.addMeasurement(10063, uid.getSystemCpuTimeUs(0) / 1000);
        uidWriter.addMeasurement(com.android.internal.util.FrameworkStatsLog.PROCESS_MEMORY_SNAPSHOT, 0L);
    }

    public void writePid(android.os.health.HealthStatsWriter pidWriter, android.os.BatteryStats.Uid.Pid pid) {
        if (pid == null) {
            return;
        }
        pidWriter.addMeasurement(com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_CONTENT_PROFILE_ERROR_REPORTED__FILE_NAME__BLUETOOTH_PBAP_ACTIVITY, pid.mWakeNesting);
        pidWriter.addMeasurement(com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_CONTENT_PROFILE_ERROR_REPORTED__FILE_NAME__BLUETOOTH_PBAP_AUTHENTICATOR, pid.mWakeSumMs);
        pidWriter.addMeasurement(com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_CONTENT_PROFILE_ERROR_REPORTED__FILE_NAME__BLUETOOTH_PBAP_AUTHENTICATOR, pid.mWakeStartMs);
    }

    public void writeProc(android.os.health.HealthStatsWriter procWriter, android.os.BatteryStats.Uid.Proc proc) {
        procWriter.addMeasurement(com.android.server.wm.EventLogTags.WM_FINISH_ACTIVITY, proc.getUserTime(0));
        procWriter.addMeasurement(com.android.server.wm.EventLogTags.WM_TASK_TO_FRONT, proc.getSystemTime(0));
        procWriter.addMeasurement(com.android.server.wm.EventLogTags.WM_NEW_INTENT, proc.getStarts(0));
        procWriter.addMeasurement(com.android.server.wm.EventLogTags.WM_CREATE_TASK, proc.getNumCrashes(0));
        procWriter.addMeasurement(com.android.server.wm.EventLogTags.WM_CREATE_ACTIVITY, proc.getNumAnrs(0));
        procWriter.addMeasurement(com.android.server.wm.EventLogTags.WM_RESTART_ACTIVITY, proc.getForegroundTime(0));
    }

    public void writePkg(android.os.health.HealthStatsWriter pkgWriter, android.os.BatteryStats.Uid.Pkg pkg) {
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Uid.Pkg.Serv> entry : pkg.getServiceStats().entrySet()) {
            android.os.health.HealthStatsWriter writer = new android.os.health.HealthStatsWriter(android.os.health.ServiceHealthStats.CONSTANTS);
            writeServ(writer, (android.os.BatteryStats.Uid.Pkg.Serv) entry.getValue());
            pkgWriter.addStats(com.android.server.EventLogTags.STREAM_DEVICES_CHANGED, entry.getKey(), writer);
        }
        for (java.util.Map.Entry<java.lang.String, ? extends android.os.BatteryStats.Counter> entry2 : pkg.getWakeupAlarmStats().entrySet()) {
            android.os.BatteryStats.Counter counter = (android.os.BatteryStats.Counter) entry2.getValue();
            if (counter != null) {
                pkgWriter.addMeasurements(40002, entry2.getKey(), counter.getCountLocked(0));
            }
        }
    }

    public void writeServ(android.os.health.HealthStatsWriter servWriter, android.os.BatteryStats.Uid.Pkg.Serv serv) {
        servWriter.addMeasurement(50001, serv.getStarts(0));
        servWriter.addMeasurement(50002, serv.getLaunches(0));
    }

    private void addTimer(android.os.health.HealthStatsWriter writer, int key, android.os.BatteryStats.Timer timer) {
        if (timer != null) {
            writer.addTimer(key, timer.getCountLocked(0), timer.getTotalTimeLocked(this.mNowRealtimeMs * 1000, 0) / 1000);
        }
    }

    private void addTimers(android.os.health.HealthStatsWriter writer, int key, java.lang.String name, android.os.BatteryStats.Timer timer) {
        if (timer != null) {
            writer.addTimers(key, name, new android.os.health.TimerStat(timer.getCountLocked(0), timer.getTotalTimeLocked(this.mNowRealtimeMs * 1000, 0) / 1000));
        }
    }
}
