package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface NotificationManagerInternal {
    boolean areNotificationsEnabledForPackage(java.lang.String str, int i);

    void cancelNotification(java.lang.String str, java.lang.String str2, int i, int i2, java.lang.String str3, int i3, int i4);

    void cleanupHistoryFiles();

    void enqueueNotification(java.lang.String str, java.lang.String str2, int i, int i2, java.lang.String str3, int i3, android.app.Notification notification, int i4);

    void enqueueNotification(java.lang.String str, java.lang.String str2, int i, int i2, java.lang.String str3, int i3, android.app.Notification notification, int i4, boolean z);

    android.app.NotificationChannel getNotificationChannel(java.lang.String str, int i, java.lang.String str2);

    android.app.NotificationChannelGroup getNotificationChannelGroup(java.lang.String str, int i, java.lang.String str2);

    int getNumNotificationChannelsForPackage(java.lang.String str, int i, boolean z);

    boolean isNotificationShown(java.lang.String str, java.lang.String str2, int i, int i2);

    void onConversationRemoved(java.lang.String str, int i, java.util.Set<java.lang.String> set);

    void removeBitmaps();

    void removeForegroundServiceFlagFromNotification(java.lang.String str, int i, int i2);

    void removeUserInitiatedJobFlagFromNotification(java.lang.String str, int i, int i2);

    void sendReviewPermissionsNotification();

    void setDeviceEffectsApplier(android.service.notification.DeviceEffectsApplier deviceEffectsApplier);
}
