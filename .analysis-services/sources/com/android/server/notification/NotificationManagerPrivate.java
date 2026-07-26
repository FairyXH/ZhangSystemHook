package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
interface NotificationManagerPrivate {
    com.android.server.notification.NotificationRecord getNotificationByKey(java.lang.String str);

    void timeoutNotification(java.lang.String str);
}
