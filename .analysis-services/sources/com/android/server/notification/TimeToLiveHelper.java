package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class TimeToLiveHelper {
    private static final java.lang.String ACTION = "com.android.server.notification.TimeToLiveHelper";
    static final java.lang.String EXTRA_KEY = "key";
    private static final int REQUEST_CODE_TIMEOUT = 1;
    private static final java.lang.String SCHEME_TIMEOUT = "timeout";
    private static final java.lang.String TAG = com.android.server.notification.TimeToLiveHelper.class.getSimpleName();
    private final android.app.AlarmManager mAm;
    private final android.content.Context mContext;
    private final com.android.server.notification.NotificationManagerPrivate mNm;
    final android.content.BroadcastReceiver mNotificationTimeoutReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.TimeToLiveHelper.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (action != null && com.android.server.notification.TimeToLiveHelper.ACTION.equals(action)) {
                android.util.Pair<java.lang.Long, java.lang.String> earliest = com.android.server.notification.TimeToLiveHelper.this.mKeys.first();
                java.lang.String key = intent.getStringExtra(com.android.server.notification.TimeToLiveHelper.EXTRA_KEY);
                if (!((java.lang.String) earliest.second).equals(key)) {
                    android.util.Slog.wtf(com.android.server.notification.TimeToLiveHelper.TAG, "Alarm triggered but wasn't the earliest we were tracking");
                }
                com.android.server.notification.TimeToLiveHelper.this.removeMatchingEntry(key);
                com.android.server.notification.TimeToLiveHelper.this.mNm.timeoutNotification((java.lang.String) earliest.second);
            }
        }
    };
    final java.util.TreeSet<android.util.Pair<java.lang.Long, java.lang.String>> mKeys = new java.util.TreeSet<>(new java.util.Comparator() { // from class: com.android.server.notification.TimeToLiveHelper$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return java.lang.Long.compare(((java.lang.Long) ((android.util.Pair) obj).first).longValue(), ((java.lang.Long) ((android.util.Pair) obj2).first).longValue());
        }
    });

    public TimeToLiveHelper(com.android.server.notification.NotificationManagerPrivate nm, android.content.Context context) {
        this.mContext = context;
        this.mNm = nm;
        this.mAm = (android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class);
        android.content.IntentFilter timeoutFilter = new android.content.IntentFilter(ACTION);
        timeoutFilter.addDataScheme(SCHEME_TIMEOUT);
        this.mContext.registerReceiver(this.mNotificationTimeoutReceiver, timeoutFilter, 4);
    }

    void destroy() {
        this.mContext.unregisterReceiver(this.mNotificationTimeoutReceiver);
    }

    void dump(java.io.PrintWriter pw, java.lang.String indent) {
        pw.println(indent + "mKeys " + this.mKeys);
    }

    private android.app.PendingIntent getAlarmPendingIntent(java.lang.String nextKey, int flags) {
        return android.app.PendingIntent.getBroadcast(this.mContext, 1, new android.content.Intent(ACTION).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).setData(new android.net.Uri.Builder().scheme(SCHEME_TIMEOUT).appendPath(nextKey).build()).putExtra(EXTRA_KEY, nextKey).addFlags(268435456), flags | 67108864);
    }

    void scheduleTimeoutLocked(com.android.server.notification.NotificationRecord record, long currentTime) {
        removeMatchingEntry(record.getKey());
        long timeoutAfter = record.getNotification().getTimeoutAfter() + currentTime;
        if (record.getNotification().getTimeoutAfter() > 0) {
            java.lang.Long currentEarliestTime = this.mKeys.isEmpty() ? null : (java.lang.Long) this.mKeys.first().first;
            if (currentEarliestTime == null || timeoutAfter < currentEarliestTime.longValue()) {
                if (currentEarliestTime != null) {
                    cancelFirstAlarm();
                }
                this.mKeys.add(android.util.Pair.create(java.lang.Long.valueOf(timeoutAfter), record.getKey()));
                maybeScheduleFirstAlarm();
                return;
            }
            this.mKeys.add(android.util.Pair.create(java.lang.Long.valueOf(timeoutAfter), record.getKey()));
        }
    }

    void cancelScheduledTimeoutLocked(com.android.server.notification.NotificationRecord record) {
        removeMatchingEntry(record.getKey());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeMatchingEntry(java.lang.String key) {
        if (!this.mKeys.isEmpty() && key.equals(this.mKeys.first().second)) {
            cancelFirstAlarm();
            this.mKeys.remove(this.mKeys.first());
            maybeScheduleFirstAlarm();
            return;
        }
        android.util.Pair<java.lang.Long, java.lang.String> trackedPair = null;
        java.util.Iterator<android.util.Pair<java.lang.Long, java.lang.String>> it = this.mKeys.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.util.Pair<java.lang.Long, java.lang.String> entry = it.next();
            if (key.equals(entry.second)) {
                trackedPair = entry;
                break;
            }
        }
        if (trackedPair != null) {
            this.mKeys.remove(trackedPair);
        }
    }

    private void cancelFirstAlarm() {
        android.app.PendingIntent pi = getAlarmPendingIntent((java.lang.String) this.mKeys.first().second, 268435456);
        this.mAm.cancel(pi);
    }

    private void maybeScheduleFirstAlarm() {
        if (!this.mKeys.isEmpty()) {
            android.app.PendingIntent piNewFirst = getAlarmPendingIntent((java.lang.String) this.mKeys.first().second, 134217728);
            this.mAm.setExactAndAllowWhileIdle(2, ((java.lang.Long) this.mKeys.first().first).longValue(), piNewFirst);
        }
    }
}
