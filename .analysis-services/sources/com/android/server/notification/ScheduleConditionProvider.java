package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ScheduleConditionProvider extends com.android.server.notification.SystemConditionProviderService {
    private static final java.lang.String EXTRA_TIME = "time";
    private static final java.lang.String NOT_SHOWN = "...";
    private static final int REQUEST_CODE_EVALUATE = 1;
    private static final java.lang.String SCP_SETTING = "snoozed_schedule_condition_provider";
    private static final java.lang.String SEPARATOR = ";";
    static final java.lang.String TAG = "ConditionProviders.SCP";
    private android.app.AlarmManager mAlarmManager;
    private boolean mConnected;
    private long mNextAlarmTime;
    private boolean mRegistered;
    static final boolean DEBUG = true;
    public static final android.content.ComponentName COMPONENT = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.notification.ScheduleConditionProvider.class.getName());
    private static final java.lang.String SIMPLE_NAME = com.android.server.notification.ScheduleConditionProvider.class.getSimpleName();
    private static final java.lang.String ACTION_EVALUATE = SIMPLE_NAME + ".EVALUATE";
    private final android.content.Context mContext = this;
    private final android.util.ArrayMap<android.net.Uri, android.service.notification.ScheduleCalendar> mSubscriptions = new android.util.ArrayMap<>();
    private android.util.ArraySet<android.net.Uri> mSnoozedForAlarm = new android.util.ArraySet<>();
    private android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.ScheduleConditionProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.notification.ScheduleConditionProvider.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ScheduleConditionProvider.TAG, "onReceive " + intent.getAction());
            }
            if ("android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction())) {
                synchronized (com.android.server.notification.ScheduleConditionProvider.this.mSubscriptions) {
                    for (android.net.Uri conditionId : com.android.server.notification.ScheduleConditionProvider.this.mSubscriptions.keySet()) {
                        android.service.notification.ScheduleCalendar cal = (android.service.notification.ScheduleCalendar) com.android.server.notification.ScheduleConditionProvider.this.mSubscriptions.get(conditionId);
                        if (cal != null) {
                            cal.setTimeZone(java.util.Calendar.getInstance().getTimeZone());
                        }
                    }
                }
            }
            com.android.server.notification.ScheduleConditionProvider.this.evaluateSubscriptions();
        }
    };

    public ScheduleConditionProvider() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "new " + SIMPLE_NAME + "()");
        }
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public android.content.ComponentName getComponent() {
        return COMPONENT;
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public boolean isValidConditionId(android.net.Uri id) {
        return android.service.notification.ZenModeConfig.isValidScheduleConditionId(id);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void dump(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        pw.print("    ");
        pw.print(SIMPLE_NAME);
        pw.println(":");
        pw.print("      mConnected=");
        pw.println(this.mConnected);
        pw.print("      mRegistered=");
        pw.println(this.mRegistered);
        pw.println("      mSubscriptions=");
        long now = java.lang.System.currentTimeMillis();
        synchronized (this.mSubscriptions) {
            for (android.net.Uri conditionId : this.mSubscriptions.keySet()) {
                pw.print("        ");
                pw.print(meetsSchedule(this.mSubscriptions.get(conditionId), now) ? "* " : "  ");
                pw.println(conditionId);
                pw.print("            ");
                pw.println(this.mSubscriptions.get(conditionId).toString());
            }
        }
        pw.println("      snoozed due to alarm: " + android.text.TextUtils.join(SEPARATOR, this.mSnoozedForAlarm));
        dumpUpcomingTime(pw, "mNextAlarmTime", this.mNextAlarmTime, now);
    }

    @Override // android.service.notification.ConditionProviderService
    public void onConnected() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onConnected");
        }
        this.mConnected = true;
        readSnoozed();
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void onBootComplete() {
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            android.util.Slog.d(TAG, "onDestroy");
        }
        this.mConnected = false;
    }

    @Override // android.service.notification.ConditionProviderService
    public void onSubscribe(android.net.Uri conditionId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onSubscribe " + conditionId);
        }
        if (!android.service.notification.ZenModeConfig.isValidScheduleConditionId(conditionId)) {
            notifyCondition(createCondition(conditionId, 3, "invalidId"));
            return;
        }
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.put(conditionId, android.service.notification.ZenModeConfig.toScheduleCalendar(conditionId));
        }
        evaluateSubscriptions();
    }

    @Override // android.service.notification.ConditionProviderService
    public void onUnsubscribe(android.net.Uri conditionId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onUnsubscribe " + conditionId);
        }
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.remove(conditionId);
        }
        removeSnoozed(conditionId);
        evaluateSubscriptions();
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void attachBase(android.content.Context base) {
        attachBaseContext(base);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public android.service.notification.IConditionProvider asInterface() {
        return onBind(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSubscriptions() {
        if (this.mAlarmManager == null) {
            this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        }
        long now = java.lang.System.currentTimeMillis();
        this.mNextAlarmTime = 0L;
        long nextUserAlarmTime = getNextAlarm();
        java.util.List<android.service.notification.Condition> conditionsToNotify = new java.util.ArrayList<>();
        synchronized (this.mSubscriptions) {
            setRegistered(!this.mSubscriptions.isEmpty());
            for (android.net.Uri conditionId : this.mSubscriptions.keySet()) {
                android.service.notification.Condition condition = evaluateSubscriptionLocked(conditionId, this.mSubscriptions.get(conditionId), now, nextUserAlarmTime);
                if (condition != null) {
                    conditionsToNotify.add(condition);
                }
            }
        }
        notifyConditions((android.service.notification.Condition[]) conditionsToNotify.toArray(new android.service.notification.Condition[conditionsToNotify.size()]));
        updateAlarm(now, this.mNextAlarmTime);
    }

    android.service.notification.Condition evaluateSubscriptionLocked(android.net.Uri conditionId, android.service.notification.ScheduleCalendar cal, long now, long nextUserAlarmTime) {
        android.service.notification.Condition condition;
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("evaluateSubscriptionLocked cal=%s, now=%s, nextUserAlarmTime=%s", cal, ts(now), ts(nextUserAlarmTime)));
        }
        if (cal == null) {
            android.service.notification.Condition condition2 = createCondition(conditionId, 3, "!invalidId");
            removeSnoozed(conditionId);
            return condition2;
        }
        if (cal.isInSchedule(now)) {
            if (conditionSnoozed(conditionId)) {
                condition = createCondition(conditionId, 0, "snoozed");
            } else if (cal.shouldExitForAlarm(now)) {
                condition = createCondition(conditionId, 0, "alarmCanceled");
                addSnoozed(conditionId);
            } else {
                condition = createCondition(conditionId, 1, "meetsSchedule");
            }
        } else {
            condition = createCondition(conditionId, 0, "!meetsSchedule");
            removeSnoozed(conditionId);
        }
        cal.maybeSetNextAlarm(now, nextUserAlarmTime);
        long nextChangeTime = cal.getNextChangeTime(now);
        if (nextChangeTime > 0 && nextChangeTime > now && (this.mNextAlarmTime == 0 || nextChangeTime < this.mNextAlarmTime)) {
            this.mNextAlarmTime = nextChangeTime;
        }
        return condition;
    }

    private void updateAlarm(long now, long time) {
        android.app.AlarmManager alarms = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        android.app.PendingIntent pendingIntent = getPendingIntent(time);
        alarms.cancel(pendingIntent);
        if (time > now) {
            if (DEBUG) {
                android.util.Slog.d(TAG, java.lang.String.format("Scheduling evaluate for %s, in %s, now=%s", ts(time), formatDuration(time - now), ts(now)));
            }
            alarms.setExact(0, time, pendingIntent);
        } else if (DEBUG) {
            android.util.Slog.d(TAG, "Not scheduling evaluate");
        }
    }

    android.app.PendingIntent getPendingIntent(long time) {
        return android.app.PendingIntent.getBroadcast(this.mContext, 1, new android.content.Intent(ACTION_EVALUATE).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).addFlags(268435456).putExtra(EXTRA_TIME, time), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
    }

    public long getNextAlarm() {
        android.app.AlarmManager.AlarmClockInfo info = this.mAlarmManager.getNextAlarmClock(android.app.ActivityManager.getCurrentUser());
        if (info != null) {
            return info.getTriggerTime();
        }
        return 0L;
    }

    private boolean meetsSchedule(android.service.notification.ScheduleCalendar cal, long time) {
        return cal != null && cal.isInSchedule(time);
    }

    private void setRegistered(boolean registered) {
        if (this.mRegistered == registered) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "setRegistered " + registered);
        }
        this.mRegistered = registered;
        if (this.mRegistered) {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.TIME_SET");
            filter.addAction("android.intent.action.TIMEZONE_CHANGED");
            filter.addAction(ACTION_EVALUATE);
            filter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
            registerReceiver(this.mReceiver, filter, 2);
            return;
        }
        unregisterReceiver(this.mReceiver);
    }

    private android.service.notification.Condition createCondition(android.net.Uri id, int state, java.lang.String reason) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "notifyCondition " + id + " " + android.service.notification.Condition.stateToString(state) + " reason=" + reason);
        }
        return new android.service.notification.Condition(id, NOT_SHOWN, NOT_SHOWN, NOT_SHOWN, 0, state, 2);
    }

    private boolean conditionSnoozed(android.net.Uri conditionId) {
        boolean zContains;
        synchronized (this.mSnoozedForAlarm) {
            zContains = this.mSnoozedForAlarm.contains(conditionId);
        }
        return zContains;
    }

    void addSnoozed(android.net.Uri conditionId) {
        synchronized (this.mSnoozedForAlarm) {
            this.mSnoozedForAlarm.add(conditionId);
            saveSnoozedLocked();
        }
    }

    private void removeSnoozed(android.net.Uri conditionId) {
        synchronized (this.mSnoozedForAlarm) {
            this.mSnoozedForAlarm.remove(conditionId);
            saveSnoozedLocked();
        }
    }

    private void saveSnoozedLocked() {
        java.lang.String setting = android.text.TextUtils.join(SEPARATOR, this.mSnoozedForAlarm);
        int currentUser = android.app.ActivityManager.getCurrentUser();
        android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), SCP_SETTING, setting, currentUser);
    }

    private void readSnoozed() {
        synchronized (this.mSnoozedForAlarm) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.lang.String setting = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), SCP_SETTING, android.app.ActivityManager.getCurrentUser());
                if (setting != null) {
                    java.lang.String[] tokens = setting.split(SEPARATOR);
                    for (int i = 0; i < tokens.length; i++) {
                        java.lang.String token = tokens[i];
                        if (token != null) {
                            token = token.trim();
                        }
                        if (!android.text.TextUtils.isEmpty(token)) {
                            this.mSnoozedForAlarm.add(android.net.Uri.parse(token));
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }
}
