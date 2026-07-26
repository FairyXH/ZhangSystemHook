package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationIntrusivenessExtractor implements com.android.server.notification.NotificationSignalExtractor {
    static final long HANG_TIME_MS = 10000;
    private static final java.lang.String TAG = "IntrusivenessExtractor";
    private static final boolean DBG = android.util.Log.isLoggable(TAG, 3);

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context ctx, com.android.server.notification.NotificationUsageStats usageStats) {
        if (DBG) {
            android.util.Slog.d(TAG, "Initializing  " + getClass().getSimpleName() + ".");
        }
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getNotification() == null) {
            if (DBG) {
                android.util.Slog.d(TAG, "skipping empty notification");
            }
            return null;
        }
        long j = 10000;
        if (record.getFreshnessMs(java.lang.System.currentTimeMillis()) < 10000 && record.getImportance() >= 3) {
            if (record.getSound() != null && record.getSound() != android.net.Uri.EMPTY) {
                record.setRecentlyIntrusive(true);
            }
            if (record.getVibration() != null) {
                record.setRecentlyIntrusive(true);
            }
            if (record.getNotification().fullScreenIntent != null) {
                record.setRecentlyIntrusive(true);
            }
        }
        if (!record.isRecentlyIntrusive()) {
            return null;
        }
        return new com.android.server.notification.RankingReconsideration(record.getKey(), j) { // from class: com.android.server.notification.NotificationIntrusivenessExtractor.1
            @Override // com.android.server.notification.RankingReconsideration
            public void work() {
            }

            @Override // com.android.server.notification.RankingReconsideration
            public void applyChangesLocked(com.android.server.notification.NotificationRecord record2) {
                if (java.lang.System.currentTimeMillis() - record2.getLastIntrusive() >= 10000) {
                    record2.setRecentlyIntrusive(false);
                }
            }
        };
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
    }
}
