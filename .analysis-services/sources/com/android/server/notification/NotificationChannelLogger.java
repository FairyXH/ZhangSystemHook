package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface NotificationChannelLogger {
    void logAppEvent(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent notificationChannelEvent, int i, java.lang.String str);

    void logNotificationChannel(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent notificationChannelEvent, android.app.NotificationChannel notificationChannel, int i, java.lang.String str, int i2, int i3);

    void logNotificationChannelGroup(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent notificationChannelEvent, android.app.NotificationChannelGroup notificationChannelGroup, int i, java.lang.String str, boolean z);

    default void logNotificationChannelCreated(android.app.NotificationChannel channel, int uid, java.lang.String pkg) {
        logNotificationChannel(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent.getCreated(channel), channel, uid, pkg, 0, getLoggingImportance(channel));
    }

    default void logNotificationChannelDeleted(android.app.NotificationChannel channel, int uid, java.lang.String pkg) {
        logNotificationChannel(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent.getDeleted(channel), channel, uid, pkg, getLoggingImportance(channel), 0);
    }

    default void logNotificationChannelModified(android.app.NotificationChannel channel, int uid, java.lang.String pkg, int oldLoggingImportance, boolean byUser) {
        logNotificationChannel(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent.getUpdated(byUser), channel, uid, pkg, oldLoggingImportance, getLoggingImportance(channel));
    }

    default void logNotificationChannelGroup(android.app.NotificationChannelGroup channelGroup, int uid, java.lang.String pkg, boolean isNew, boolean wasBlocked) {
        logNotificationChannelGroup(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent.getGroupUpdated(isNew), channelGroup, uid, pkg, wasBlocked);
    }

    default void logNotificationChannelGroupDeleted(android.app.NotificationChannelGroup channelGroup, int uid, java.lang.String pkg) {
        logNotificationChannelGroup(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent.NOTIFICATION_CHANNEL_GROUP_DELETED, channelGroup, uid, pkg, false);
    }

    default void logAppNotificationsAllowed(int uid, java.lang.String pkg, boolean enabled) {
        logAppEvent(com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent.getBlocked(enabled), uid, pkg);
    }

    public enum NotificationChannelEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        NOTIFICATION_CHANNEL_CREATED(219),
        NOTIFICATION_CHANNEL_UPDATED(220),
        NOTIFICATION_CHANNEL_UPDATED_BY_USER(221),
        NOTIFICATION_CHANNEL_DELETED(222),
        NOTIFICATION_CHANNEL_GROUP_CREATED(com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED),
        NOTIFICATION_CHANNEL_GROUP_UPDATED(com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS),
        NOTIFICATION_CHANNEL_GROUP_DELETED(com.android.server.display.util.OplusDisplayPanelFeatureHelper.OMMDP_UIR),
        NOTIFICATION_CHANNEL_CONVERSATION_CREATED(272),
        NOTIFICATION_CHANNEL_CONVERSATION_DELETED(274),
        APP_NOTIFICATIONS_BLOCKED(com.android.internal.util.FrameworkStatsLog.BEDTIME_MODE_STATE_CHANGED),
        APP_NOTIFICATIONS_UNBLOCKED(558);

        private final int mId;

        NotificationChannelEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }

        public static com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent getUpdated(boolean byUser) {
            if (byUser) {
                return NOTIFICATION_CHANNEL_UPDATED_BY_USER;
            }
            return NOTIFICATION_CHANNEL_UPDATED;
        }

        public static com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent getCreated(android.app.NotificationChannel channel) {
            if (channel.getConversationId() != null) {
                return NOTIFICATION_CHANNEL_CONVERSATION_CREATED;
            }
            return NOTIFICATION_CHANNEL_CREATED;
        }

        public static com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent getDeleted(android.app.NotificationChannel channel) {
            if (channel.getConversationId() != null) {
                return NOTIFICATION_CHANNEL_CONVERSATION_DELETED;
            }
            return NOTIFICATION_CHANNEL_DELETED;
        }

        public static com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent getGroupUpdated(boolean isNew) {
            if (isNew) {
                return NOTIFICATION_CHANNEL_GROUP_CREATED;
            }
            return NOTIFICATION_CHANNEL_GROUP_DELETED;
        }

        public static com.android.server.notification.NotificationChannelLogger.NotificationChannelEvent getBlocked(boolean enabled) {
            return enabled ? APP_NOTIFICATIONS_UNBLOCKED : APP_NOTIFICATIONS_BLOCKED;
        }
    }

    static int getIdHash(android.app.NotificationChannel channel) {
        return com.android.server.notification.SmallHash.hash(channel.getId());
    }

    static int getConversationIdHash(android.app.NotificationChannel channel) {
        return com.android.server.notification.SmallHash.hash(channel.getConversationId());
    }

    static int getIdHash(android.app.NotificationChannelGroup group) {
        return com.android.server.notification.SmallHash.hash(group.getId());
    }

    static int getLoggingImportance(android.app.NotificationChannel channel) {
        return getLoggingImportance(channel, channel.getImportance());
    }

    static int getLoggingImportance(android.app.NotificationChannel channel, int importance) {
        if (channel.getConversationId() == null || importance < 4) {
            return importance;
        }
        if (channel.isImportantConversation()) {
            return 5;
        }
        return importance;
    }

    static int getImportance(android.app.NotificationChannelGroup channelGroup) {
        return getImportance(channelGroup.isBlocked());
    }

    static int getImportance(boolean isBlocked) {
        if (isBlocked) {
            return 0;
        }
        return 3;
    }
}
