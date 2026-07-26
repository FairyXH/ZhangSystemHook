package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IAlarmManagerServiceWrapper {
    default void setImplLocked(com.android.server.alarm.Alarm a) {
    }

    default com.android.server.alarm.AlarmManagerService.BroadcastStats getStatsLocked(android.app.PendingIntent pi) {
        return new com.android.server.alarm.AlarmManagerService.BroadcastStats(0, "");
    }

    default boolean adjustDeliveryTimeBasedOnDeviceIdle(com.android.server.alarm.Alarm alarm) {
        return false;
    }

    default void updateNextAlarmClockLocked() {
    }

    default int set(long nativeData, int type, long seconds, long nanoseconds) {
        return -1;
    }

    default com.android.server.alarm.IAlarmManagerServiceExt getExt() {
        return null;
    }

    default void decrementAlarmCount(int uid, int decrement) {
    }
}
