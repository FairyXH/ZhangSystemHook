package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class EventConditionProvider extends com.android.server.notification.SystemConditionProviderService {
    private static final long CHANGE_DELAY = 2000;
    private static final java.lang.String EXTRA_TIME = "time";
    private static final java.lang.String NOT_SHOWN = "...";
    private static final int REQUEST_CODE_EVALUATE = 1;
    private static final java.lang.String TAG = "ConditionProviders.ECP";
    private boolean mBootComplete;
    private boolean mConnected;
    private long mNextAlarmTime;
    private boolean mRegistered;
    private final android.os.HandlerThread mThread;
    private final android.os.Handler mWorker;
    private static final boolean DEBUG = android.util.Log.isLoggable("ConditionProviders", 3);
    public static final android.content.ComponentName COMPONENT = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.notification.EventConditionProvider.class.getName());
    private static final java.lang.String SIMPLE_NAME = com.android.server.notification.EventConditionProvider.class.getSimpleName();
    private static final java.lang.String ACTION_EVALUATE = SIMPLE_NAME + ".EVALUATE";
    private final android.content.Context mContext = this;
    private final android.util.ArraySet<android.net.Uri> mSubscriptions = new android.util.ArraySet<>();
    private final android.util.SparseArray<com.android.server.notification.CalendarTracker> mTrackers = new android.util.SparseArray<>();
    private final com.android.server.notification.CalendarTracker.Callback mTrackerCallback = new com.android.server.notification.CalendarTracker.Callback() { // from class: com.android.server.notification.EventConditionProvider.2
        @Override // com.android.server.notification.CalendarTracker.Callback
        public void onChanged() {
            if (com.android.server.notification.EventConditionProvider.DEBUG) {
                android.util.Slog.d(com.android.server.notification.EventConditionProvider.TAG, "mTrackerCallback.onChanged");
            }
            com.android.server.notification.EventConditionProvider.this.mWorker.removeCallbacks(com.android.server.notification.EventConditionProvider.this.mEvaluateSubscriptionsW);
            com.android.server.notification.EventConditionProvider.this.mWorker.postDelayed(com.android.server.notification.EventConditionProvider.this.mEvaluateSubscriptionsW, com.android.server.notification.EventConditionProvider.CHANGE_DELAY);
        }
    };
    private final android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.EventConditionProvider.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.notification.EventConditionProvider.DEBUG) {
                android.util.Slog.d(com.android.server.notification.EventConditionProvider.TAG, "onReceive " + intent.getAction());
            }
            com.android.server.notification.EventConditionProvider.this.evaluateSubscriptions();
        }
    };
    private final java.lang.Runnable mEvaluateSubscriptionsW = new java.lang.Runnable() { // from class: com.android.server.notification.EventConditionProvider.4
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.notification.EventConditionProvider.this.evaluateSubscriptionsW();
        }
    };

    public EventConditionProvider() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "new " + SIMPLE_NAME + "()");
        }
        this.mThread = new android.os.HandlerThread(TAG, 10);
        this.mThread.start();
        this.mWorker = new android.os.Handler(this.mThread.getLooper());
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public android.content.ComponentName getComponent() {
        return COMPONENT;
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public boolean isValidConditionId(android.net.Uri id) {
        return android.service.notification.ZenModeConfig.isValidEventConditionId(id);
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
        pw.print("      mBootComplete=");
        pw.println(this.mBootComplete);
        dumpUpcomingTime(pw, "mNextAlarmTime", this.mNextAlarmTime, java.lang.System.currentTimeMillis());
        synchronized (this.mSubscriptions) {
            pw.println("      mSubscriptions=");
            for (android.net.Uri conditionId : this.mSubscriptions) {
                pw.print("        ");
                pw.println(conditionId);
            }
        }
        pw.println("      mTrackers=");
        for (int i = 0; i < this.mTrackers.size(); i++) {
            pw.print("        user=");
            pw.println(this.mTrackers.keyAt(i));
            this.mTrackers.valueAt(i).dump("          ", pw);
        }
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void onBootComplete() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onBootComplete");
        }
        if (this.mBootComplete) {
            return;
        }
        this.mBootComplete = true;
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        filter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.notification.EventConditionProvider.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                com.android.server.notification.EventConditionProvider.this.reloadTrackers();
            }
        }, filter);
        reloadTrackers();
    }

    @Override // android.service.notification.ConditionProviderService
    public void onConnected() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onConnected");
        }
        this.mConnected = true;
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
        if (!android.service.notification.ZenModeConfig.isValidEventConditionId(conditionId)) {
            notifyCondition(createCondition(conditionId, 0));
            return;
        }
        synchronized (this.mSubscriptions) {
            if (this.mSubscriptions.add(conditionId)) {
                evaluateSubscriptions();
            }
        }
    }

    @Override // android.service.notification.ConditionProviderService
    public void onUnsubscribe(android.net.Uri conditionId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onUnsubscribe " + conditionId);
        }
        synchronized (this.mSubscriptions) {
            if (this.mSubscriptions.remove(conditionId)) {
                evaluateSubscriptions();
            }
        }
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
    public void reloadTrackers() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "reloadTrackers");
        }
        for (int i = 0; i < this.mTrackers.size(); i++) {
            this.mTrackers.valueAt(i).setCallback(null);
        }
        this.mTrackers.clear();
        for (android.os.UserHandle user : android.os.UserManager.get(this.mContext).getUserProfiles()) {
            android.content.Context context = user.isSystem() ? this.mContext : getContextForUser(this.mContext, user);
            if (context == null) {
                android.util.Slog.w(TAG, "Unable to create context for user " + user.getIdentifier());
            } else {
                this.mTrackers.put(user.getIdentifier(), new com.android.server.notification.CalendarTracker(this.mContext, context));
            }
        }
        evaluateSubscriptions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSubscriptions() {
        if (!this.mWorker.hasCallbacks(this.mEvaluateSubscriptionsW)) {
            this.mWorker.post(this.mEvaluateSubscriptionsW);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSubscriptionsW() {
        long reevaluateAt;
        long reevaluateAt2;
        int i;
        if (DEBUG) {
            android.util.Slog.d(TAG, "evaluateSubscriptions");
        }
        if (!this.mBootComplete) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Skipping evaluate before boot complete");
                return;
            }
            return;
        }
        long now = java.lang.System.currentTimeMillis();
        java.util.List<android.service.notification.Condition> conditionsToNotify = new java.util.ArrayList<>();
        synchronized (this.mSubscriptions) {
            for (int i2 = 0; i2 < this.mTrackers.size(); i2++) {
                this.mTrackers.valueAt(i2).setCallback(this.mSubscriptions.isEmpty() ? null : this.mTrackerCallback);
            }
            int i3 = 0;
            setRegistered(!this.mSubscriptions.isEmpty());
            long reevaluateAt3 = 0;
            for (android.net.Uri conditionId : this.mSubscriptions) {
                android.service.notification.ZenModeConfig.EventInfo event = android.service.notification.ZenModeConfig.tryParseEventConditionId(conditionId);
                if (event == null) {
                    conditionsToNotify.add(createCondition(conditionId, i3));
                    reevaluateAt = reevaluateAt3;
                } else {
                    com.android.server.notification.CalendarTracker.CheckEventResult result = null;
                    if (event.calName == null) {
                        int i4 = 0;
                        while (i4 < this.mTrackers.size()) {
                            com.android.server.notification.CalendarTracker.CheckEventResult r = this.mTrackers.valueAt(i4).checkEvent(event, now);
                            if (result == null) {
                                result = r;
                                reevaluateAt2 = reevaluateAt3;
                            } else {
                                result.inEvent |= r.inEvent;
                                long j = result.recheckAt;
                                reevaluateAt2 = reevaluateAt3;
                                long reevaluateAt4 = r.recheckAt;
                                result.recheckAt = java.lang.Math.min(j, reevaluateAt4);
                            }
                            i4++;
                            reevaluateAt3 = reevaluateAt2;
                        }
                        reevaluateAt = reevaluateAt3;
                    } else {
                        reevaluateAt = reevaluateAt3;
                        int userId = android.service.notification.ZenModeConfig.EventInfo.resolveUserId(event.userId);
                        com.android.server.notification.CalendarTracker tracker = this.mTrackers.get(userId);
                        if (tracker == null) {
                            android.util.Slog.w(TAG, "No calendar tracker found for user " + userId);
                            conditionsToNotify.add(createCondition(conditionId, 0));
                        } else {
                            result = tracker.checkEvent(event, now);
                        }
                    }
                    if (result.recheckAt != 0 && (reevaluateAt == 0 || result.recheckAt < reevaluateAt)) {
                        reevaluateAt3 = result.recheckAt;
                    } else {
                        reevaluateAt3 = reevaluateAt;
                    }
                    if (!result.inEvent) {
                        i = 0;
                        conditionsToNotify.add(createCondition(conditionId, 0));
                    } else {
                        i = 0;
                        conditionsToNotify.add(createCondition(conditionId, 1));
                    }
                    i3 = i;
                }
                reevaluateAt3 = reevaluateAt;
                i3 = 0;
            }
            rescheduleAlarm(now, reevaluateAt3);
        }
        for (android.service.notification.Condition condition : conditionsToNotify) {
            if (condition != null) {
                notifyCondition(condition);
            }
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "evaluateSubscriptions took " + (java.lang.System.currentTimeMillis() - now));
        }
    }

    private void rescheduleAlarm(long now, long time) {
        this.mNextAlarmTime = time;
        android.app.AlarmManager alarms = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        android.app.PendingIntent pendingIntent = getPendingIntent(time);
        alarms.cancel(pendingIntent);
        if (time == 0 || time < now) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Not scheduling evaluate: " + (time == 0 ? "no time specified" : "specified time in the past"));
            }
        } else {
            if (DEBUG) {
                android.util.Slog.d(TAG, java.lang.String.format("Scheduling evaluate for %s, in %s, now=%s", ts(time), formatDuration(time - now), ts(now)));
            }
            alarms.setExact(0, time, pendingIntent);
        }
    }

    android.app.PendingIntent getPendingIntent(long time) {
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(this.mContext, 1, new android.content.Intent(ACTION_EVALUATE).addFlags(268435456).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).putExtra(EXTRA_TIME, time), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
        return pendingIntent;
    }

    private android.service.notification.Condition createCondition(android.net.Uri id, int state) {
        return new android.service.notification.Condition(id, NOT_SHOWN, NOT_SHOWN, NOT_SHOWN, 0, state, 2);
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
            registerReceiver(this.mReceiver, filter, 2);
            return;
        }
        unregisterReceiver(this.mReceiver);
    }

    private static android.content.Context getContextForUser(android.content.Context context, android.os.UserHandle user) {
        try {
            return context.createPackageContextAsUser(context.getPackageName(), 0, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
