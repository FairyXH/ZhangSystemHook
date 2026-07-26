package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface AlarmStore {

    @java.lang.FunctionalInterface
    public interface AlarmDeliveryCalculator {
        boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm);
    }

    void add(com.android.server.alarm.Alarm alarm);

    void addAll(java.util.ArrayList<com.android.server.alarm.Alarm> arrayList);

    java.util.ArrayList<com.android.server.alarm.Alarm> asList();

    void dump(android.util.IndentingPrintWriter indentingPrintWriter, long j, java.text.SimpleDateFormat simpleDateFormat);

    void dumpProto(android.util.proto.ProtoOutputStream protoOutputStream, long j);

    int getCount(java.util.function.Predicate<com.android.server.alarm.Alarm> predicate);

    java.lang.String getName();

    long getNextDeliveryTime();

    com.android.server.alarm.Alarm getNextWakeFromIdleAlarm();

    long getNextWakeupDeliveryTime();

    java.util.ArrayList<com.android.server.alarm.Alarm> remove(java.util.function.Predicate<com.android.server.alarm.Alarm> predicate);

    android.util.SparseIntArray removeAllDuplicateAlarms();

    int removeDuplicateAlarmsForPkg(int i, java.util.List<java.lang.String> list);

    java.util.ArrayList<com.android.server.alarm.Alarm> removePendingAlarms(long j);

    void setAlarmClockRemovalListener(java.lang.Runnable runnable);

    int size();

    boolean updateAlarmDeliveries(com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator alarmDeliveryCalculator);
}
