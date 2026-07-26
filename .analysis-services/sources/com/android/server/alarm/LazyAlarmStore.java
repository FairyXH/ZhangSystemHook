package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public class LazyAlarmStore implements com.android.server.alarm.AlarmStore {
    private static final long ALARM_DEADLINE_SLOP = 500;
    static final java.lang.String TAG = com.android.server.alarm.LazyAlarmStore.class.getSimpleName();
    private static final java.util.Comparator<com.android.server.alarm.Alarm> sDecreasingTimeOrder = java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.alarm.LazyAlarmStore$$ExternalSyntheticLambda0
        @Override // java.util.function.ToLongFunction
        public final long applyAsLong(java.lang.Object obj) {
            return ((com.android.server.alarm.Alarm) obj).getWhenElapsed();
        }
    }).reversed();
    private java.lang.Runnable mOnAlarmClockRemoved;
    private final java.util.ArrayList<com.android.server.alarm.Alarm> mAlarms = new java.util.ArrayList<>();
    final com.android.internal.util.StatLogger mStatLogger = new com.android.internal.util.StatLogger(TAG + " stats", new java.lang.String[]{"GET_NEXT_DELIVERY_TIME", "GET_NEXT_WAKEUP_DELIVERY_TIME", "GET_COUNT"});

    interface Stats {
        public static final int GET_COUNT = 2;
        public static final int GET_NEXT_DELIVERY_TIME = 0;
        public static final int GET_NEXT_WAKEUP_DELIVERY_TIME = 1;
    }

    @Override // com.android.server.alarm.AlarmStore
    public void add(com.android.server.alarm.Alarm a) {
        int index = java.util.Collections.binarySearch(this.mAlarms, a, sDecreasingTimeOrder);
        if (index < 0) {
            index = (0 - index) - 1;
        }
        this.mAlarms.add(index, a);
    }

    @Override // com.android.server.alarm.AlarmStore
    public void addAll(java.util.ArrayList<com.android.server.alarm.Alarm> alarms) {
        if (alarms == null) {
            return;
        }
        this.mAlarms.addAll(alarms);
        java.util.Collections.sort(this.mAlarms, sDecreasingTimeOrder);
    }

    @Override // com.android.server.alarm.AlarmStore
    public java.util.ArrayList<com.android.server.alarm.Alarm> remove(java.util.function.Predicate<com.android.server.alarm.Alarm> whichAlarms) {
        java.util.ArrayList<com.android.server.alarm.Alarm> removedAlarms = new java.util.ArrayList<>();
        for (int i = this.mAlarms.size() - 1; i >= 0; i--) {
            if (whichAlarms.test(this.mAlarms.get(i))) {
                com.android.server.alarm.Alarm removed = this.mAlarms.remove(i);
                if (removed.alarmClock != null && this.mOnAlarmClockRemoved != null) {
                    this.mOnAlarmClockRemoved.run();
                }
                if (com.android.server.alarm.AlarmManagerService.isTimeTickAlarm(removed)) {
                    android.util.Slog.wtf(TAG, "Removed TIME_TICK alarm");
                }
                removedAlarms.add(removed);
            }
        }
        return removedAlarms;
    }

    @Override // com.android.server.alarm.AlarmStore
    public void setAlarmClockRemovalListener(java.lang.Runnable listener) {
        this.mOnAlarmClockRemoved = listener;
    }

    @Override // com.android.server.alarm.AlarmStore
    public com.android.server.alarm.Alarm getNextWakeFromIdleAlarm() {
        for (int i = this.mAlarms.size() - 1; i >= 0; i--) {
            com.android.server.alarm.Alarm alarm = this.mAlarms.get(i);
            if ((alarm.flags & 2) != 0) {
                return alarm;
            }
        }
        return null;
    }

    @Override // com.android.server.alarm.AlarmStore
    public int size() {
        return this.mAlarms.size();
    }

    @Override // com.android.server.alarm.AlarmStore
    public long getNextWakeupDeliveryTime() {
        long start = this.mStatLogger.getTime();
        long nextWakeup = 0;
        for (int i = this.mAlarms.size() - 1; i >= 0; i--) {
            com.android.server.alarm.Alarm a = this.mAlarms.get(i);
            if (a.wakeup) {
                if (nextWakeup == 0) {
                    nextWakeup = a.getMaxWhenElapsed();
                } else {
                    if (a.getWhenElapsed() > nextWakeup) {
                        break;
                    }
                    nextWakeup = java.lang.Math.min(nextWakeup, a.getMaxWhenElapsed());
                }
            }
        }
        this.mStatLogger.logDurationStat(1, start);
        return nextWakeup;
    }

    @Override // com.android.server.alarm.AlarmStore
    public long getNextDeliveryTime() {
        long start = this.mStatLogger.getTime();
        int n = this.mAlarms.size();
        if (n == 0) {
            return 0L;
        }
        long nextDelivery = this.mAlarms.get(n - 1).getMaxWhenElapsed();
        for (int i = n - 2; i >= 0; i--) {
            com.android.server.alarm.Alarm a = this.mAlarms.get(i);
            if (a.getWhenElapsed() > nextDelivery) {
                break;
            }
            nextDelivery = java.lang.Math.min(nextDelivery, a.getMaxWhenElapsed());
        }
        this.mStatLogger.logDurationStat(0, start);
        return nextDelivery;
    }

    @Override // com.android.server.alarm.AlarmStore
    public java.util.ArrayList<com.android.server.alarm.Alarm> removePendingAlarms(long nowElapsed) {
        java.util.ArrayList<com.android.server.alarm.Alarm> pending = new java.util.ArrayList<>();
        boolean sendWakeups = false;
        boolean standalonesOnly = false;
        for (int i = this.mAlarms.size() - 1; i >= 0; i--) {
            com.android.server.alarm.Alarm alarm = this.mAlarms.get(i);
            if (alarm.getWhenElapsed() > nowElapsed) {
                break;
            }
            this.mAlarms.remove(i);
            pending.add(alarm);
            if (alarm.wakeup && alarm.getMaxWhenElapsed() <= 500 + nowElapsed) {
                sendWakeups = true;
            }
            if ((alarm.flags & 1) != 0) {
                standalonesOnly = true;
            }
        }
        java.util.ArrayList<com.android.server.alarm.Alarm> toSend = new java.util.ArrayList<>();
        for (int i2 = pending.size() - 1; i2 >= 0; i2--) {
            com.android.server.alarm.Alarm pendingAlarm = pending.get(i2);
            if ((sendWakeups || !pendingAlarm.wakeup) && (!standalonesOnly || (pendingAlarm.flags & 1) != 0)) {
                pending.remove(i2);
                toSend.add(pendingAlarm);
            }
        }
        addAll(pending);
        return toSend;
    }

    @Override // com.android.server.alarm.AlarmStore
    public boolean updateAlarmDeliveries(com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator deliveryCalculator) {
        boolean changed = false;
        for (com.android.server.alarm.Alarm alarm : this.mAlarms) {
            changed |= deliveryCalculator.updateAlarmDelivery(alarm);
        }
        if (changed) {
            java.util.Collections.sort(this.mAlarms, sDecreasingTimeOrder);
        }
        return changed;
    }

    @Override // com.android.server.alarm.AlarmStore
    public java.util.ArrayList<com.android.server.alarm.Alarm> asList() {
        java.util.ArrayList<com.android.server.alarm.Alarm> copy = new java.util.ArrayList<>(this.mAlarms);
        java.util.Collections.reverse(copy);
        return copy;
    }

    @Override // com.android.server.alarm.AlarmStore
    public void dump(android.util.IndentingPrintWriter ipw, long nowElapsed, java.text.SimpleDateFormat sdf) {
        ipw.println(this.mAlarms.size() + " pending alarms: ");
        ipw.increaseIndent();
        com.android.server.alarm.AlarmManagerService.dumpAlarmList(ipw, this.mAlarms, nowElapsed, sdf);
        ipw.decreaseIndent();
        this.mStatLogger.dump(ipw);
    }

    @Override // com.android.server.alarm.AlarmStore
    public void dumpProto(android.util.proto.ProtoOutputStream pos, long nowElapsed) {
        for (com.android.server.alarm.Alarm a : this.mAlarms) {
            a.dumpDebug(pos, 2246267895850L, nowElapsed);
        }
    }

    @Override // com.android.server.alarm.AlarmStore
    public java.lang.String getName() {
        return TAG;
    }

    @Override // com.android.server.alarm.AlarmStore
    public int getCount(java.util.function.Predicate<com.android.server.alarm.Alarm> condition) {
        long start = this.mStatLogger.getTime();
        int count = 0;
        for (com.android.server.alarm.Alarm a : this.mAlarms) {
            if (condition.test(a)) {
                count++;
            }
        }
        this.mStatLogger.logDurationStat(2, start);
        return count;
    }

    @Override // com.android.server.alarm.AlarmStore
    public int removeDuplicateAlarmsForPkg(int uid, java.util.List<java.lang.String> tagList) {
        java.lang.String tag;
        java.util.ListIterator<com.android.server.alarm.Alarm> iterator = this.mAlarms.listIterator(this.mAlarms.size());
        java.util.HashSet<java.lang.String> duplicatePkgTags = new java.util.HashSet<>();
        int removeAlarmCount = 0;
        while (iterator.hasPrevious()) {
            com.android.server.alarm.Alarm a = iterator.previous();
            if (a.uid == uid && (tag = a.getWrapper().getExt().getTag()) != null) {
                java.util.Iterator<java.lang.String> it = tagList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        java.lang.String tagPrefix = it.next();
                        if (tag.startsWith(tagPrefix) && !duplicatePkgTags.add(tag)) {
                            removeAlarmCount++;
                            iterator.remove();
                            android.util.Slog.d(TAG, "remove duplicate alarms, uid:" + a.uid + " pkg: " + a.packageName + ", tag: " + tag);
                            break;
                        }
                    }
                }
            }
        }
        return removeAlarmCount;
    }

    @Override // com.android.server.alarm.AlarmStore
    public android.util.SparseIntArray removeAllDuplicateAlarms() {
        java.util.ListIterator<com.android.server.alarm.Alarm> iterator = this.mAlarms.listIterator(this.mAlarms.size());
        long elapsed = -1;
        java.util.HashSet<java.lang.String> repeatedPkgTags = new java.util.HashSet<>();
        android.util.SparseIntArray removeUidAlarmCount = new android.util.SparseIntArray();
        while (iterator.hasPrevious()) {
            com.android.server.alarm.Alarm a = iterator.previous();
            if (a.getWhenElapsed() != elapsed) {
                elapsed = a.getWhenElapsed();
                repeatedPkgTags.clear();
            }
            java.lang.String tag = a.getWrapper().getExt().getTag();
            if (tag != null) {
                java.lang.String uidTag = a.uid + tag;
                if (((com.android.server.alarm.IOplusAlarmManagerHelper) android.common.OplusFeatureCache.get(com.android.server.alarm.IOplusAlarmManagerHelper.DEFAULT)).inDuplicateBlackList(a.packageName, tag) && !repeatedPkgTags.add(uidTag)) {
                    int index = removeUidAlarmCount.indexOfKey(a.uid);
                    if (index >= 0) {
                        removeUidAlarmCount.setValueAt(index, removeUidAlarmCount.valueAt(index) + 1);
                    } else {
                        removeUidAlarmCount.put(a.uid, 1);
                    }
                    iterator.remove();
                    android.util.Slog.d(TAG, "remove all duplicate alarms, uid:" + a.uid + " pkg: " + a.packageName + ", tag: " + tag);
                }
            }
        }
        return removeUidAlarmCount;
    }
}
