package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationChannelExtractor implements com.android.server.notification.NotificationSignalExtractor {
    private static final boolean DBG = false;
    static final long RESTRICT_AUDIO_ATTRIBUTES = 331793339;
    private static final java.lang.String TAG = "ChannelExtractor";
    private com.android.server.notification.RankingConfig mConfig;
    private android.content.Context mContext;
    private com.android.server.notification.IOplusNotificationChannelExtractorExt mOplusExtractorExt = (com.android.server.notification.IOplusNotificationChannelExtractorExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IOplusNotificationChannelExtractorExt.class).base(this).create();
    private com.android.internal.compat.IPlatformCompat mPlatformCompat;

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context ctx, com.android.server.notification.NotificationUsageStats usageStats) {
        this.mContext = ctx;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setCompatChangeLogger(com.android.internal.compat.IPlatformCompat platformCompat) {
        this.mPlatformCompat = platformCompat;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getNotification() == null || this.mConfig == null) {
            return null;
        }
        android.app.NotificationChannel updatedChannel = this.mConfig.getConversationNotificationChannel(record.getSbn().getPackageName(), record.getSbn().getUid(), record.getChannel().getId(), record.getSbn().getShortcutId(), true, false);
        this.mOplusExtractorExt.updateNotificationChannel(record, this.mConfig, updatedChannel);
        record.updateNotificationChannel(updatedChannel);
        if (android.app.Flags.restrictAudioAttributesCall() || android.app.Flags.restrictAudioAttributesAlarm() || android.app.Flags.restrictAudioAttributesMedia()) {
            android.media.AudioAttributes attributes = record.getChannel().getAudioAttributes();
            boolean updateAttributes = false;
            if (android.app.Flags.restrictAudioAttributesCall() && !record.getNotification().isStyle(android.app.Notification.CallStyle.class) && attributes.getUsage() == 6) {
                updateAttributes = true;
            }
            if (android.app.Flags.restrictAudioAttributesAlarm() && record.getNotification().category != com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM && attributes.getUsage() == 4) {
                updateAttributes = true;
            }
            if (android.app.Flags.restrictAudioAttributesMedia() && (attributes.getUsage() == 0 || attributes.getUsage() == 1)) {
                updateAttributes = true;
            }
            if (updateAttributes) {
                reportAudioAttributesChanged(record.getUid());
                android.app.NotificationChannel clone = record.getChannel().copy();
                clone.setSound(clone.getSound(), new android.media.AudioAttributes.Builder(attributes).setUsage(5).build());
                record.updateNotificationChannel(clone);
            }
        }
        return null;
    }

    private void reportAudioAttributesChanged(int uid) {
        long id = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mPlatformCompat.reportChangeByUid(RESTRICT_AUDIO_ATTRIBUTES, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unexpected exception while reporting to changecompat", e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(id);
        }
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
        this.mConfig = config;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
    }
}
