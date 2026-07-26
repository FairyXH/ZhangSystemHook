package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class CriticalNotificationExtractor implements com.android.server.notification.NotificationSignalExtractor {
    static final int CRITICAL = 0;
    static final int CRITICAL_LOW = 1;
    private static final boolean DBG = false;
    static final int NORMAL = 2;
    private static final java.lang.String TAG = "CriticalNotificationExt";
    private boolean mSupportsCriticalNotifications = false;

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context context, com.android.server.notification.NotificationUsageStats usageStats) {
        this.mSupportsCriticalNotifications = supportsCriticalNotifications(context);
    }

    private boolean supportsCriticalNotifications(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.automotive", 0);
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (!this.mSupportsCriticalNotifications || record == null || record.getNotification() == null) {
            return null;
        }
        if (record.isCategory("car_emergency")) {
            record.setCriticality(0);
        } else if (record.isCategory("car_warning")) {
            record.setCriticality(1);
        } else {
            record.setCriticality(2);
        }
        return null;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
    }
}
