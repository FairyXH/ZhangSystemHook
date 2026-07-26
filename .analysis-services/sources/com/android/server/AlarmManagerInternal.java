package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface AlarmManagerInternal {

    public interface InFlightListener {
        void broadcastAlarmComplete(int i);

        void broadcastAlarmPending(int i);
    }

    boolean isIdling();

    void registerInFlightListener(com.android.server.AlarmManagerInternal.InFlightListener inFlightListener);

    void remove(android.app.PendingIntent pendingIntent);

    void removeAlarmsForUid(int i);

    void setTime(long j, int i, java.lang.String str);

    void setTimeZone(java.lang.String str, int i, java.lang.String str2);

    boolean shouldGetBucketElevation(java.lang.String str, int i);
}
