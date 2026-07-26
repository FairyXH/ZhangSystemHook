package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface RankingConfig {
    boolean badgingEnabled(android.os.UserHandle userHandle);

    boolean bubblesEnabled(android.os.UserHandle userHandle);

    boolean canShowBadge(java.lang.String str, int i);

    boolean canShowNotificationsOnLockscreen(int i);

    boolean canShowPrivateNotificationsOnLockScreen(int i);

    boolean createNotificationChannel(java.lang.String str, int i, android.app.NotificationChannel notificationChannel, boolean z, boolean z2, int i2, boolean z3);

    void createNotificationChannelGroup(java.lang.String str, int i, android.app.NotificationChannelGroup notificationChannelGroup, boolean z, int i2, boolean z2);

    boolean deleteNotificationChannel(java.lang.String str, int i, java.lang.String str2, int i2, boolean z);

    int getBubblePreference(java.lang.String str, int i);

    android.app.NotificationChannel getConversationNotificationChannel(java.lang.String str, int i, java.lang.String str2, java.lang.String str3, boolean z, boolean z2);

    android.app.NotificationChannel getNotificationChannel(java.lang.String str, int i, java.lang.String str2, boolean z);

    java.util.Collection<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String str, int i);

    android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannels(java.lang.String str, int i, boolean z);

    boolean isGroupBlocked(java.lang.String str, int i, java.lang.String str2);

    boolean isMediaNotificationFilteringEnabled();

    void permanentlyDeleteNotificationChannel(java.lang.String str, int i, java.lang.String str2);

    void permanentlyDeleteNotificationChannels(java.lang.String str, int i);

    void setShowBadge(java.lang.String str, int i, boolean z);

    void updateNotificationChannel(java.lang.String str, int i, android.app.NotificationChannel notificationChannel, boolean z, int i2, boolean z2);
}
