package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationHistoryDatabaseFactory {
    private static com.android.server.notification.NotificationHistoryDatabase sTestingNotificationHistoryDb;

    public static void setTestingNotificationHistoryDatabase(com.android.server.notification.NotificationHistoryDatabase db) {
        sTestingNotificationHistoryDb = db;
    }

    public static com.android.server.notification.NotificationHistoryDatabase create(android.content.Context context, android.os.Handler handler, java.io.File rootDir) {
        if (sTestingNotificationHistoryDb != null) {
            return sTestingNotificationHistoryDb;
        }
        return new com.android.server.notification.NotificationHistoryDatabase(handler, rootDir);
    }
}
