package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ImportanceExtractor implements com.android.server.notification.NotificationSignalExtractor {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = "ImportanceExtractor";
    private com.android.server.notification.RankingConfig mConfig;

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context ctx, com.android.server.notification.NotificationUsageStats usageStats) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getNotification() == null || this.mConfig == null) {
            return null;
        }
        record.calculateImportance();
        return null;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
        this.mConfig = config;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
    }
}
