package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public interface IBatteryStatsImplExt {
    default boolean onBatteryStatsMessageHandle(android.os.Message msg) {
        return false;
    }

    default void setInBatteryStatsImplInstance(android.os.BatteryStats impl) {
    }

    default void onSystemServicesReady(android.content.Context context) {
    }

    default void initBatteryStatsImplExtImpl(com.android.server.power.stats.BatteryStatsImpl battStatsImpl, java.io.File systemDir, android.os.Handler handler) {
    }

    default void initBatteryStatsImplExtImpl(com.android.server.power.stats.BatteryStatsImpl battStatsImpl) {
    }

    default void setThermalState(java.lang.Object thermalState) {
    }

    default void noteScreenBrightnessModeChangedLock(boolean isAuto) {
    }

    default void setThermalConfig() {
    }

    default void dumpThemalLocked(java.io.PrintWriter pw, long histStart) {
    }

    default void clearThermalAllHistory() {
    }

    default void toggleThermalDebugSwith(java.io.PrintWriter pw, int on) {
    }

    default void updateCpuStatsNow(java.io.PrintWriter pw) {
    }

    default void setThermalHeatThreshold(java.io.PrintWriter pw, int threshold) {
    }

    default void printThermalHeatThreshold(java.io.PrintWriter pw) {
    }

    default void setHeatBetweenTime(java.io.PrintWriter pw, int time) {
    }

    default void setMonitorAppLimitTime(java.io.PrintWriter pw, int limitTime) {
    }

    default void getMonitorAppLocked(java.io.PrintWriter pw) {
    }

    default void dumpThemalRawLocked(java.io.PrintWriter pw, long histStart) {
    }

    default void backupThermalStatsFile() {
    }

    default void backupThermalLogFile() {
    }

    default void dumpThemalHeatDetailLocked(java.io.PrintWriter pw) {
    }

    default void getPhoneTemp(java.io.PrintWriter pw) {
    }

    default void printThermalUploadTemp(java.io.PrintWriter pw) {
    }

    default void printChargeMapLocked(java.io.PrintWriter pw) {
    }

    default void logSwitch(boolean en) {
    }

    default void dumpThermalConfig(java.io.PrintWriter pw) {
    }

    default void dumpThemalRecLocked(android.content.Context context, java.io.PrintWriter pw, int flags, int reqUid, long histStart) {
    }

    default java.lang.String addDevicePowerStatsDeltaString(java.lang.String curStatSubsystemPowerState) {
        return curStatSubsystemPowerState;
    }

    default void recordGpsPowerDrainMaMs(long powerdrains) {
    }

    default void collectCheckinFile(int lowDischargeAmountSinceCharge, android.util.AtomicFile checkinFile, com.android.server.power.stats.BatteryStatsImpl.BatteryCallback callback) {
    }

    default void recordNetworkActivityBytes(int type, long deltaBytes) {
    }

    default void recordWifiPowerDrainMaMs(long powerdrains) {
    }

    default void recordMobilePowerDrainMaMs(long powerdrains) {
    }

    default void updateMobileRadioState(android.telephony.ModemActivityInfo deltaInfo) {
    }

    default void noteMobileRadioActive(int uid, long elapseMs) {
    }

    default void recordBluetoothPowerDrainMaMs(long powerdrains) {
    }

    default void noteActivityPausedLocked(int uid, android.content.ComponentName component, boolean isOnBattery, android.os.BatteryStats.HistoryItem mHistoryCur, long pausedElapsedRealtime1, android.os.Handler handler) {
    }

    default void noteActivityResumedLocked(int uid, android.content.ComponentName component, boolean isOnBattery, android.os.BatteryStats.HistoryItem historyCur, long pausedElapsedRealtime1, android.os.Handler handler, java.lang.String currentTopActivity) {
    }

    default void noteActivityLocked(int uid, android.content.ComponentName component, boolean isOnBattery, android.os.BatteryStats.HistoryItem historyCur, long pausedElapsedRealtime1, android.os.Handler handler, java.lang.String currentTopActivity, boolean resumed, android.content.Context context) {
    }

    default void setThermalCpuLoading(int load1, int load5, int load15, int cpuLoading, int maxCpu, java.lang.String cpuProc, java.lang.String simpleTopProc) {
    }

    default void addThermalForegroundApp(long elapsedRealTime, long uptime, java.lang.String procName, int uid, int code) {
    }

    default void noteConnectivityChangedLocked(int type, java.lang.String extra, long elapsedRealtime, long uptime) {
    }

    default void addThermalnetSyncProc(long elapsedRealtime, long uptime, java.lang.String procName) {
    }

    default void addThermalJobProc(long elapsedRealtime, long uptime, java.lang.String procName) {
    }

    default void addThermalOnOffEvent(int eventType, long elapsedRealtime, long uptime, boolean on) {
    }

    default void addThermalScreenBrightnessEvent(long elapsedRealtime, long uptime, int backlight, int delayTime) {
    }

    default void setScreenBrightness(int value) {
    }

    default void addThermalNetState(long elapsedRealtime, long uptime, boolean netState) {
    }

    default void addThermalPhoneOnOff(long elapsedRealtime, long uptime, boolean onOff) {
    }

    default void addThermalPhoneSignal(long elapsedRealtime, long uptime, byte signal) {
    }

    default void addThermalPhoneState(long elapsedRealtime, long uptime, byte state) {
    }

    default void notePhoneDataConnectionStateLocked(long elapsedTime, long upTime, int dataType) {
    }

    default void addThermalWifiStatus(long elapsedRealtime, long uptime, int status) {
    }

    default void addThermalWifiRssi(long elapsedRealtime, long uptime, int wifiSignalStrengthBin) {
    }

    default void setOpDump(boolean opDump) {
    }

    default boolean isOpDump() {
        return false;
    }

    default java.lang.String getSystemServerBinderCallsStats() {
        return null;
    }
}
