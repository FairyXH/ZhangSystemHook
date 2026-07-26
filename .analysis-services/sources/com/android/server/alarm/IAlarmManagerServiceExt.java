package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IAlarmManagerServiceExt {
    default void init(android.content.Context context, long nativeData) {
    }

    default void printStackTraceInfo() {
    }

    default void systemServiceReady() {
    }

    default java.lang.String getProcessName(int pid) {
        return null;
    }

    default java.lang.String[] getActionComponent(android.app.PendingIntent operation) {
        return null;
    }

    default long adjustWindowLengthsWhenSetImpl(android.app.PendingIntent operation, java.lang.String callingPackage, int flags, android.app.AlarmManager.AlarmClockInfo alarmClock, long windowLength, java.lang.String action, java.lang.String listenerTag) {
        return windowLength;
    }

    default int adjustAlarmFlagsWhenSetImpl(java.lang.String callingPackage, int flags, long windowLength, int callingUid) {
        return flags;
    }

    default boolean dumpImpl(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default void adjustAlarmLocked(com.android.server.alarm.Alarm a, boolean interactive) {
    }

    default boolean updateHeartBeatPolicy(com.android.server.alarm.Alarm a, boolean isScreenOn) {
        return false;
    }

    default void adjustAlarms() {
    }

    default boolean shouldAdjustForDualApps(java.lang.String callingPackage, java.lang.String action) {
        return false;
    }

    default long adjustAlarmWindowLengthForDualApps(long windowLength) {
        return windowLength;
    }

    default int adjustAlarmFlagsForDualApps(int flags) {
        return flags;
    }

    default void maxAlarmsPerUidHandle(java.lang.String callingPackage, int callingUid, int maxAlarmsPerUid) {
    }

    default int SyncAlarmHandleOnSetImplLocked(int type, android.app.PendingIntent pi, java.lang.String action, java.lang.String component) {
        return type;
    }

    default long adjDeviceIdlePolicyTime(long dftDeviceIdlePolicyTime, long pendingIdleUntilTime, com.android.server.alarm.Alarm alarm) {
        return dftDeviceIdlePolicyTime;
    }

    default boolean isDynamicLogEnabled() {
        return false;
    }

    default void updateGoogleAlarmTypeAndTag(com.android.server.alarm.Alarm a) {
    }

    default void removeAlarmsForUidLocked(int uid, int reason) {
    }

    default void onScreenOn() {
    }

    default void onScreenOff() {
    }

    default boolean filterAlarmForHans(com.android.server.alarm.Alarm alarm) {
        return false;
    }

    default void filterTriggerListForStrictMode(java.util.ArrayList<com.android.server.alarm.Alarm> triggerList) {
    }

    default boolean interceptDeliverAlarmsLockedInLoop(com.android.server.alarm.Alarm alarm) {
        return false;
    }

    default void deliverAlarmsLockedStart() {
    }

    default void deliverAlarmsLockedEnd() {
    }

    default void countAlarmWakeup() {
    }

    default boolean interceptPkgBrdcast(android.content.Intent intent, android.content.Context context) {
        return false;
    }

    default boolean isFilterRemovePackage(java.lang.String pkg) {
        return false;
    }

    default void canceledPendingIntentDetection(com.android.server.alarm.Alarm alarm, long nowELAPSED) {
    }

    default void deliverLockedEnd(com.android.server.alarm.Alarm alarm, com.android.server.alarm.AlarmManagerService.BroadcastStats bs, long nowELAPSED, boolean isPendingIntentCanceled) {
    }

    default boolean isInSmartDozeEearlyTime() {
        return false;
    }

    default boolean isBackgroundRestricted(com.android.server.alarm.Alarm alarm) {
        return false;
    }

    default void onAlarmInfoCollect(int uid, java.lang.String packageName, com.android.server.alarm.AlarmStore alarmStore) {
    }

    default boolean isPowerOffAlarmType(int type) {
        return false;
    }

    default boolean schedulePoweroffAlarm(int type, long triggerAtTime, long interval, android.app.PendingIntent operation, android.app.IAlarmListener directReceiver, java.lang.String listenerTag, android.os.WorkSource workSource, android.app.AlarmManager.AlarmClockInfo alarmClock, java.lang.String callingPackage) {
        return true;
    }

    default void updatePoweroffAlarmtoNowRtc() {
    }

    default void cancelPoweroffAlarmImpl(java.lang.String name) {
    }

    default void trackEventSetAlarmLocked(com.android.server.alarm.Alarm alarm) {
    }

    default void trackEventCancelAlarmLocked(android.app.PendingIntent operation, android.app.IAlarmListener listener) {
    }

    default void trackEventSendAlarmLocked(com.android.server.alarm.Alarm alarm) {
    }

    default void trackEventRemoveAlarmsForUidByAppStandbyLocked(int uid) {
    }

    default void trackEventRemoveAlarmsForPkgByLocalServiceLocked(java.lang.String pkgName) {
    }

    default void trackEventRemoveAlarmsForUidByLocalServiceLocked(int uid) {
    }

    default void trackEventRemoveAlarmsForPkgByBroadcastLocked(java.lang.String pkgName) {
    }

    default void trackEventRemoveAlarmsForUidByBroadcastLocked(int uid) {
    }
}
