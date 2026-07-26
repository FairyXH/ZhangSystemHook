package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
class NotificationRecordLoggerImpl implements com.android.server.notification.NotificationRecordLogger {
    private com.android.internal.logging.UiEventLogger mUiEventLogger = new com.android.internal.logging.UiEventLoggerImpl();

    NotificationRecordLoggerImpl() {
    }

    @Override // com.android.server.notification.NotificationRecordLogger
    public void logNotificationPosted(com.android.server.notification.NotificationRecordLogger.NotificationReported nr) {
        writeNotificationReportedAtom(nr);
    }

    @Override // com.android.server.notification.NotificationRecordLogger
    public void logNotificationAdjusted(com.android.server.notification.NotificationRecord r, int position, int buzzBeepBlink, com.android.internal.logging.InstanceId groupId) {
        com.android.server.notification.NotificationRecordLogger.NotificationRecordPair p = new com.android.server.notification.NotificationRecordLogger.NotificationRecordPair(r, null);
        writeNotificationReportedAtom(new com.android.server.notification.NotificationRecordLogger.NotificationReported(p, com.android.server.notification.NotificationRecordLogger.NotificationReportedEvent.NOTIFICATION_ADJUSTED, position, buzzBeepBlink, groupId));
    }

    private void writeNotificationReportedAtom(com.android.server.notification.NotificationRecordLogger.NotificationReported notificationReported) {
        com.android.internal.util.FrameworkStatsLog.write(244, notificationReported.event_id, notificationReported.uid, notificationReported.package_name, notificationReported.instance_id, notificationReported.notification_id_hash, notificationReported.channel_id_hash, notificationReported.group_id_hash, notificationReported.group_instance_id, notificationReported.is_group_summary, notificationReported.category, notificationReported.style, notificationReported.num_people, notificationReported.position, notificationReported.importance, notificationReported.alerting, notificationReported.importance_source, notificationReported.importance_initial, notificationReported.importance_initial_source, notificationReported.importance_asst, notificationReported.assistant_hash, notificationReported.assistant_ranking_score, notificationReported.is_ongoing, notificationReported.is_foreground_service, notificationReported.timeout_millis, notificationReported.is_non_dismissible, notificationReported.post_duration_millis, notificationReported.fsi_state, notificationReported.is_locked, notificationReported.age_in_minutes);
    }

    @Override // com.android.server.notification.NotificationRecordLogger
    public void log(com.android.internal.logging.UiEventLogger.UiEventEnum event, com.android.server.notification.NotificationRecord r) {
        if (r == null) {
            return;
        }
        this.mUiEventLogger.logWithInstanceId(event, r.getUid(), r.getSbn().getPackageName(), r.getSbn().getInstanceId());
    }

    @Override // com.android.server.notification.NotificationRecordLogger
    public void log(com.android.internal.logging.UiEventLogger.UiEventEnum event) {
        this.mUiEventLogger.log(event);
    }
}
