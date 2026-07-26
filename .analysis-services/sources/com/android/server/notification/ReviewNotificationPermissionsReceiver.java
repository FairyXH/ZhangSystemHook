package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ReviewNotificationPermissionsReceiver extends android.content.BroadcastReceiver {
    private static final long JOB_RESCHEDULE_TIME = 604800000;
    public static final java.lang.String TAG = "ReviewNotifPermissions";
    static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    static android.content.IntentFilter getFilter() {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("REVIEW_NOTIF_ACTION_REMIND");
        filter.addAction("REVIEW_NOTIF_ACTION_DISMISS");
        filter.addAction("REVIEW_NOTIF_ACTION_CANCELED");
        return filter;
    }

    protected void cancelNotification(android.content.Context context) {
        android.app.NotificationManager nm = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        if (nm != null) {
            nm.cancel(com.android.server.notification.NotificationManagerService.TAG, 71);
        } else {
            android.util.Slog.w(TAG, "could not cancel notification: NotificationManager not found");
        }
    }

    protected void rescheduleNotification(android.content.Context context) {
        com.android.server.notification.ReviewNotificationPermissionsJobService.scheduleJob(context, 604800000L);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Scheduled review permissions notification for on or after: " + java.time.LocalDateTime.now(java.time.ZoneId.systemDefault()).plus(604800000L, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.MILLIS));
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        if (action.equals("REVIEW_NOTIF_ACTION_REMIND")) {
            rescheduleNotification(context);
            android.provider.Settings.Global.putInt(context.getContentResolver(), "review_permissions_notification_state", 1);
            cancelNotification(context);
        } else if (action.equals("REVIEW_NOTIF_ACTION_DISMISS")) {
            android.provider.Settings.Global.putInt(context.getContentResolver(), "review_permissions_notification_state", 2);
            cancelNotification(context);
        } else if (action.equals("REVIEW_NOTIF_ACTION_CANCELED")) {
            int notifState = android.provider.Settings.Global.getInt(context.getContentResolver(), "review_permissions_notification_state", -1);
            if (notifState == 0) {
                rescheduleNotification(context);
                android.provider.Settings.Global.putInt(context.getContentResolver(), "review_permissions_notification_state", 1);
            } else if (notifState == 3) {
                android.provider.Settings.Global.putInt(context.getContentResolver(), "review_permissions_notification_state", 1);
            }
        }
    }
}
