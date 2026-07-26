package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IAlarmExt {
    default void init(java.lang.String callingProcName, java.lang.String alarmAction, java.lang.String alarmComponent, java.lang.String statsTag) {
    }

    default void alarmToStringExtend(java.lang.StringBuilder sb, long whenElapsed, long windowLength, long maxWhenElapsed, long repeatInterval, android.app.PendingIntent intent, java.lang.String listenerTag, int flags, int uid) {
    }

    default java.lang.String getAction() {
        return null;
    }

    default java.lang.String getProcName() {
        return null;
    }

    default java.lang.String getComponent() {
        return null;
    }

    default java.lang.String getTag() {
        return null;
    }
}
