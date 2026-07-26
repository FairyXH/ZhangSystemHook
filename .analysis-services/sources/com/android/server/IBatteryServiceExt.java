package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IBatteryServiceExt {
    public static final int PSY_BATTERY_HMAC = 2;
    public static final int PSY_FAST_CHARGE_TYPE = 3;
    public static final int PSY_OTG_ONLINE = 1;

    default void onStart() {
    }

    default void onBootPhase(int phase) {
    }

    default boolean ignoreShutdownIfOverTempByOplusLocked() {
        return false;
    }

    default boolean getDebugCommand() {
        return false;
    }

    default void updateBatteryService() {
    }

    default void notifyTempChanged() {
    }

    default void processValuesForOplusLocked(boolean force, int plugType, android.hardware.health.HealthInfo healthInfo) {
    }

    default boolean shouldUpdateChargingState(int batteryTemperature, int lastBatteryTemperature) {
        return false;
    }

    default void onPlugChangedForOplusSysStateManager(int plugType) {
    }

    default void appendFlagToStatusIntent(android.content.Intent statusIntent, int flag) {
    }

    default void saveLastStatsAfterValuesChanged() {
    }

    default java.lang.String getBatteryStatusStrForDebug() {
        return null;
    }

    default void appendExtraToBatteryStatusChangedIntend(android.content.Intent intent) {
    }

    default void setDebugCommand(boolean debugCommand) {
    }

    default boolean dumpInternalBase(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default void handleScreenState(boolean screenon) {
    }

    default boolean getDebugPanic() {
        return false;
    }

    default void setDebugSwitchState(boolean on) {
    }

    default void initBatteryServiceExtImpl(android.content.Context context, com.android.server.BatteryService batteryService, java.lang.Object batteryLed) {
    }

    default void writeEventLowBatteryPowerOff() {
    }

    default boolean isNeedSkipBatteryChangedBroadcast(android.hardware.health.HealthInfo healthInfo, int plugType, int invalidCharger, boolean shouldUpdate) {
        return false;
    }
}
