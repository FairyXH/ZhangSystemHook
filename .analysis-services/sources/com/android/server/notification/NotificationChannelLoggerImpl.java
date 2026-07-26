package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationChannelLoggerImpl implements com.android.server.notification.NotificationChannelLogger {
    com.android.internal.logging.UiEventLogger mUiEventLogger = new com.android.internal.logging.UiEventLoggerImpl();

    @Override // com.android.server.notification.NotificationChannelLogger
    public void logNotificationChannel(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent event, android.app.NotificationChannel channel, int uid, java.lang.String pkg, int oldImportance, int newImportance) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.NOTIFICATION_CHANNEL_MODIFIED, event.getId(), uid, pkg, com.android.server.notification.NotificationChannelLogger.getIdHash(channel), oldImportance, newImportance, channel.isConversation(), com.android.server.notification.NotificationChannelLogger.getConversationIdHash(channel), channel.isDemoted(), channel.isImportantConversation());
    }

    @Override // com.android.server.notification.NotificationChannelLogger
    public void logNotificationChannelGroup(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent event, android.app.NotificationChannelGroup channelGroup, int uid, java.lang.String pkg, boolean wasBlocked) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.NOTIFICATION_CHANNEL_MODIFIED, event.getId(), uid, pkg, com.android.server.notification.NotificationChannelLogger.getIdHash(channelGroup), com.android.server.notification.NotificationChannelLogger.getImportance(wasBlocked), com.android.server.notification.NotificationChannelLogger.getImportance(channelGroup), false, 0, false, false);
    }

    @Override // com.android.server.notification.NotificationChannelLogger
    public void logAppEvent(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent event, int uid, java.lang.String pkg) {
        this.mUiEventLogger.log(event, uid, pkg);
    }
}
