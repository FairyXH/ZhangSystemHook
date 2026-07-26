package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ZenModeExtractor implements com.android.server.notification.NotificationSignalExtractor {
    private com.android.server.notification.ZenModeHelper mZenModeHelper;
    private static final java.lang.String TAG = "ZenModeExtractor";
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
        if (this.mZenModeHelper == null) {
            if (DBG) {
                android.util.Slog.d(TAG, "skipping - no zen info available");
            }
            return null;
        }
        record.setIntercepted(this.mZenModeHelper.shouldIntercept(record));
        if (record.isIntercepted()) {
            record.setSuppressedVisualEffects(this.mZenModeHelper.getConsolidatedNotificationPolicy().suppressedVisualEffects);
        } else {
            record.setSuppressedVisualEffects(0);
        }
        return null;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
        this.mZenModeHelper = helper;
    }
}
