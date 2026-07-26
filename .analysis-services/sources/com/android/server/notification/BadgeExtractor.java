package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class BadgeExtractor implements com.android.server.notification.NotificationSignalExtractor {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = "BadgeExtractor";
    private com.android.server.notification.RankingConfig mConfig;

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context ctx, com.android.server.notification.NotificationUsageStats usageStats) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getNotification() == null || this.mConfig == null) {
            return null;
        }
        boolean userWantsBadges = this.mConfig.badgingEnabled(record.getSbn().getUser());
        boolean appCanShowBadge = this.mConfig.canShowBadge(record.getSbn().getPackageName(), record.getSbn().getUid());
        if (!userWantsBadges || !appCanShowBadge) {
            record.setShowBadge(false);
        } else if (record.getChannel() != null) {
            record.setShowBadge(record.getChannel().canShowBadge() && appCanShowBadge);
        } else {
            record.setShowBadge(appCanShowBadge);
        }
        if (record.isIntercepted() && (record.getSuppressedVisualEffects() & 64) != 0) {
            record.setShowBadge(false);
        }
        android.app.Notification.BubbleMetadata metadata = record.getNotification().getBubbleMetadata();
        if (metadata != null && metadata.isNotificationSuppressed()) {
            record.setShowBadge(false);
        }
        if (this.mConfig.isMediaNotificationFilteringEnabled()) {
            android.app.Notification notif = record.getNotification();
            if (notif.isMediaNotification()) {
                record.setShowBadge(false);
            }
        }
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
