package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AlarmQueue<K> implements android.app.AlarmManager.OnAlarmListener {
    private static final boolean DEBUG = false;
    private static final long NOT_SCHEDULED = -1;
    private static final long SIGNIFICANT_TRIGGER_TIME_CHANGE_THRESHOLD_MS = 60000;
    private static final java.lang.String TAG = com.android.server.utils.AlarmQueue.class.getSimpleName();
    private final com.android.server.utils.AlarmQueue.AlarmPriorityQueue<K> mAlarmPriorityQueue;
    private final java.lang.String mAlarmTag;
    private final android.content.Context mContext;
    private final java.lang.String mDumpTitle;
    private final boolean mExactAlarm;
    private final android.os.Handler mHandler;
    private final com.android.server.utils.AlarmQueue.Injector mInjector;
    private long mLastFireTimeElapsed;
    private final java.lang.Object mLock;
    private long mMinTimeBetweenAlarmsMs;
    private final java.lang.Runnable mScheduleAlarmRunnable;
    private long mTriggerTimeElapsed;

    protected abstract boolean isForUser(K k, int i);

    protected abstract void processExpiredAlarms(android.util.ArraySet<K> arraySet);

    /* JADX INFO: Access modifiers changed from: private */
    static class AlarmPriorityQueue<Q> extends java.util.PriorityQueue<android.util.Pair<Q, java.lang.Long>> {
        private static final java.util.Comparator<android.util.Pair<?, java.lang.Long>> sTimeComparator = new java.util.Comparator() { // from class: com.android.server.utils.AlarmQueue$AlarmPriorityQueue$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return java.lang.Long.compare(((java.lang.Long) ((android.util.Pair) obj).second).longValue(), ((java.lang.Long) ((android.util.Pair) obj2).second).longValue());
            }
        };

        AlarmPriorityQueue() {
            super(1, sTimeComparator);
        }

        public boolean removeKey(Q key) {
            boolean removed = false;
            android.util.Pair[] alarms = (android.util.Pair[]) toArray(new android.util.Pair[size()]);
            for (int i = alarms.length - 1; i >= 0; i--) {
                if (key.equals(alarms[i].first)) {
                    remove(alarms[i]);
                    removed = true;
                }
            }
            return removed;
        }
    }

    static class Injector {
        Injector() {
        }

        long getElapsedRealtime() {
            return android.os.SystemClock.elapsedRealtime();
        }
    }

    public AlarmQueue(android.content.Context context, android.os.Looper looper, java.lang.String alarmTag, java.lang.String dumpTitle, boolean exactAlarm, long minTimeBetweenAlarmsMs) {
        this(context, looper, alarmTag, dumpTitle, exactAlarm, minTimeBetweenAlarmsMs, new com.android.server.utils.AlarmQueue.Injector());
    }

    AlarmQueue(android.content.Context context, android.os.Looper looper, java.lang.String alarmTag, java.lang.String dumpTitle, boolean exactAlarm, long minTimeBetweenAlarmsMs, com.android.server.utils.AlarmQueue.Injector injector) {
        this.mScheduleAlarmRunnable = new java.lang.Runnable() { // from class: com.android.server.utils.AlarmQueue.1
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.utils.AlarmQueue.this.mHandler.removeCallbacks(this);
                android.app.AlarmManager alarmManager = (android.app.AlarmManager) com.android.server.utils.AlarmQueue.this.mContext.getSystemService(android.app.AlarmManager.class);
                if (alarmManager == null) {
                    com.android.server.utils.AlarmQueue.this.mHandler.postDelayed(this, 30000L);
                    return;
                }
                synchronized (com.android.server.utils.AlarmQueue.this.mLock) {
                    if (com.android.server.utils.AlarmQueue.this.mTriggerTimeElapsed == -1) {
                        return;
                    }
                    long nextTriggerTimeElapsed = com.android.server.utils.AlarmQueue.this.mTriggerTimeElapsed;
                    long minTimeBetweenAlarmsMs2 = com.android.server.utils.AlarmQueue.this.mMinTimeBetweenAlarmsMs;
                    if (com.android.server.utils.AlarmQueue.this.mExactAlarm) {
                        alarmManager.setExact(3, nextTriggerTimeElapsed, com.android.server.utils.AlarmQueue.this.mAlarmTag, com.android.server.utils.AlarmQueue.this, com.android.server.utils.AlarmQueue.this.mHandler);
                    } else {
                        alarmManager.setWindow(3, nextTriggerTimeElapsed, minTimeBetweenAlarmsMs2 / 2, com.android.server.utils.AlarmQueue.this.mAlarmTag, com.android.server.utils.AlarmQueue.this, com.android.server.utils.AlarmQueue.this.mHandler);
                    }
                }
            }
        };
        this.mLock = new java.lang.Object();
        this.mAlarmPriorityQueue = new com.android.server.utils.AlarmQueue.AlarmPriorityQueue<>();
        this.mTriggerTimeElapsed = -1L;
        this.mContext = context;
        this.mAlarmTag = alarmTag;
        this.mDumpTitle = dumpTitle.trim();
        this.mExactAlarm = exactAlarm;
        this.mHandler = new android.os.Handler(looper);
        this.mInjector = injector;
        if (minTimeBetweenAlarmsMs < 0) {
            throw new java.lang.IllegalArgumentException("min time between alarms must be non-negative");
        }
        this.mMinTimeBetweenAlarmsMs = minTimeBetweenAlarmsMs;
    }

    public void addAlarm(K key, long alarmTimeElapsed) {
        synchronized (this.mLock) {
            boolean removed = this.mAlarmPriorityQueue.removeKey(key);
            this.mAlarmPriorityQueue.offer(new android.util.Pair(key, java.lang.Long.valueOf(alarmTimeElapsed)));
            if (this.mTriggerTimeElapsed == -1 || removed || alarmTimeElapsed < this.mTriggerTimeElapsed) {
                setNextAlarmLocked();
            }
        }
    }

    public long getMinTimeBetweenAlarmsMs() {
        long j;
        synchronized (this.mLock) {
            j = this.mMinTimeBetweenAlarmsMs;
        }
        return j;
    }

    public void removeAlarmForKey(K key) {
        synchronized (this.mLock) {
            if (this.mAlarmPriorityQueue.removeKey(key)) {
                setNextAlarmLocked();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void removeAlarmsForUserId(int userId) {
        boolean removed = false;
        synchronized (this.mLock) {
            android.util.Pair[] alarms = (android.util.Pair[]) this.mAlarmPriorityQueue.toArray(new android.util.Pair[this.mAlarmPriorityQueue.size()]);
            for (int i = alarms.length - 1; i >= 0; i--) {
                if (isForUser(alarms[i].first, userId)) {
                    this.mAlarmPriorityQueue.remove(alarms[i]);
                    removed = true;
                }
            }
            if (removed) {
                setNextAlarmLocked();
            }
        }
    }

    public void removeAllAlarms() {
        synchronized (this.mLock) {
            this.mAlarmPriorityQueue.clear();
            setNextAlarmLocked(0L);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void removeAlarmsIf(java.util.function.Predicate<K> predicate) {
        boolean removed = false;
        synchronized (this.mLock) {
            android.util.Pair[] alarms = (android.util.Pair[]) this.mAlarmPriorityQueue.toArray(new android.util.Pair[this.mAlarmPriorityQueue.size()]);
            for (int i = alarms.length - 1; i >= 0; i--) {
                if (predicate.test(alarms[i].first)) {
                    this.mAlarmPriorityQueue.remove(alarms[i]);
                    removed = true;
                }
            }
            if (removed) {
                setNextAlarmLocked();
            }
        }
    }

    public void setMinTimeBetweenAlarmsMs(long minTimeMs) {
        if (minTimeMs < 0) {
            throw new java.lang.IllegalArgumentException("min time between alarms must be non-negative");
        }
        synchronized (this.mLock) {
            this.mMinTimeBetweenAlarmsMs = minTimeMs;
        }
    }

    private void setNextAlarmLocked() {
        setNextAlarmLocked(this.mLastFireTimeElapsed + this.mMinTimeBetweenAlarmsMs);
    }

    private void setNextAlarmLocked(long earliestTriggerElapsed) {
        if (this.mAlarmPriorityQueue.size() == 0) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.utils.AlarmQueue$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setNextAlarmLocked$0();
                }
            });
            this.mTriggerTimeElapsed = -1L;
            return;
        }
        android.util.Pair alarm = this.mAlarmPriorityQueue.peek();
        long nextTriggerTimeElapsed = java.lang.Math.max(earliestTriggerElapsed, ((java.lang.Long) alarm.second).longValue());
        long timeShiftThresholdMs = java.lang.Math.min(60000L, this.mMinTimeBetweenAlarmsMs);
        if (this.mTriggerTimeElapsed == -1 || nextTriggerTimeElapsed < this.mTriggerTimeElapsed - timeShiftThresholdMs || this.mTriggerTimeElapsed < nextTriggerTimeElapsed) {
            this.mTriggerTimeElapsed = nextTriggerTimeElapsed;
            this.mHandler.post(this.mScheduleAlarmRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setNextAlarmLocked$0() {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        if (alarmManager != null) {
            alarmManager.cancel(this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.AlarmManager.OnAlarmListener
    public void onAlarm() {
        android.util.ArraySet arraySet = new android.util.ArraySet();
        synchronized (this.mLock) {
            long nowElapsed = this.mInjector.getElapsedRealtime();
            this.mLastFireTimeElapsed = nowElapsed;
            while (this.mAlarmPriorityQueue.size() > 0) {
                android.util.Pair alarm = this.mAlarmPriorityQueue.peek();
                if (((java.lang.Long) alarm.second).longValue() > nowElapsed) {
                    break;
                }
                arraySet.add(alarm.first);
                this.mAlarmPriorityQueue.remove(alarm);
            }
            setNextAlarmLocked(this.mMinTimeBetweenAlarmsMs + nowElapsed);
        }
        if (arraySet.size() > 0) {
            processExpiredAlarms(arraySet);
        }
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.print(this.mDumpTitle);
            pw.println(" alarms:");
            pw.increaseIndent();
            if (this.mAlarmPriorityQueue.size() == 0) {
                pw.println("NOT WAITING");
            } else {
                android.util.Pair[] alarms = (android.util.Pair[]) this.mAlarmPriorityQueue.toArray(new android.util.Pair[this.mAlarmPriorityQueue.size()]);
                for (int i = 0; i < alarms.length; i++) {
                    pw.print(alarms[i].first);
                    pw.print(": ");
                    pw.print(alarms[i].second);
                    pw.println();
                }
            }
            pw.decreaseIndent();
        }
    }
}
