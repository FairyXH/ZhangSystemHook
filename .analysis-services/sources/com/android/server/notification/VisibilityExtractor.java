package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class VisibilityExtractor implements com.android.server.notification.NotificationSignalExtractor {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = "VisibilityExtractor";
    private com.android.server.notification.RankingConfig mConfig;
    private android.app.admin.DevicePolicyManager mDpm;
    public com.android.server.notification.IOplusVisibilityExtractorExt mOplusVisibilityExtractorExt = (com.android.server.notification.IOplusVisibilityExtractorExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IOplusVisibilityExtractorExt.class).base(this).create();

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context ctx, com.android.server.notification.NotificationUsageStats usageStats) {
        this.mDpm = (android.app.admin.DevicePolicyManager) ctx.getSystemService(android.app.admin.DevicePolicyManager.class);
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getNotification() == null || this.mConfig == null) {
            return null;
        }
        int userId = record.getUserId();
        if (userId == -1) {
            record.setPackageVisibilityOverride(record.getChannel().getLockscreenVisibility());
        } else {
            boolean userCanShowNotifications = this.mConfig.canShowNotificationsOnLockscreen(userId);
            boolean dpmCanShowNotifications = adminAllowsKeyguardFeature(userId, 4);
            boolean channelCanShowNotifications = record.getChannel().getLockscreenVisibility() != -1;
            if (!userCanShowNotifications || !dpmCanShowNotifications || !channelCanShowNotifications) {
                record.setPackageVisibilityOverride(-1);
            } else {
                boolean userCanShowContents = this.mConfig.canShowPrivateNotificationsOnLockScreen(userId);
                boolean dpmCanShowContents = adminAllowsKeyguardFeature(userId, 8);
                boolean channelCanShowContents = record.getChannel().getLockscreenVisibility() != 0;
                if (!userCanShowContents || !dpmCanShowContents || !channelCanShowContents) {
                    record.setPackageVisibilityOverride(0);
                } else {
                    record.setPackageVisibilityOverride(-1000);
                }
            }
        }
        this.mOplusVisibilityExtractorExt.updateAppVisibility(record, this.mConfig);
        return null;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
        this.mConfig = config;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
    }

    private boolean adminAllowsKeyguardFeature(int userHandle, int feature) {
        if (userHandle == -1) {
            return true;
        }
        int dpmFlags = this.mDpm.getKeyguardDisabledFeatures(null, userHandle);
        return (dpmFlags & feature) == 0;
    }
}
