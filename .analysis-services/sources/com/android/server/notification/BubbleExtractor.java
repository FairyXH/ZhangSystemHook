package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class BubbleExtractor implements com.android.server.notification.NotificationSignalExtractor {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = "BubbleExtractor";
    private android.app.ActivityManager mActivityManager;
    private com.android.server.notification.RankingConfig mConfig;
    private android.content.Context mContext;
    private com.android.server.notification.ShortcutHelper mShortcutHelper;
    boolean mSupportsBubble;

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context context, com.android.server.notification.NotificationUsageStats usageStats) {
        this.mContext = context;
        this.mActivityManager = (android.app.ActivityManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
        this.mSupportsBubble = android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_skipScreenOffTransition);
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getNotification() == null || this.mConfig == null || this.mShortcutHelper == null) {
            return null;
        }
        boolean applyFlag = false;
        boolean notifCanPresentAsBubble = (!canPresentAsBubble(record) || this.mActivityManager.isLowRamDevice() || !record.isConversation() || record.getShortcutInfo() == null || record.getNotification().isFgsOrUij()) ? false : true;
        boolean userEnabledBubbles = this.mConfig.bubblesEnabled(record.getUser());
        int appPreference = this.mConfig.getBubblePreference(record.getSbn().getPackageName(), record.getSbn().getUid());
        android.app.NotificationChannel recordChannel = record.getChannel();
        if (!userEnabledBubbles || appPreference == 0 || !notifCanPresentAsBubble) {
            record.setAllowBubble(false);
            if (!notifCanPresentAsBubble) {
                record.getNotification().setBubbleMetadata(null);
            }
        } else if (recordChannel == null) {
            record.setAllowBubble(true);
        } else if (appPreference == 1) {
            record.setAllowBubble(recordChannel.getAllowBubbles() != 0);
        } else if (appPreference == 2) {
            record.setAllowBubble(recordChannel.canBubble());
        }
        if (record.canBubble() && !record.isFlagBubbleRemoved()) {
            applyFlag = true;
        }
        if (applyFlag) {
            record.getNotification().flags |= 4096;
        } else {
            record.getNotification().flags &= -4097;
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

    public void setShortcutHelper(com.android.server.notification.ShortcutHelper helper) {
        this.mShortcutHelper = helper;
    }

    public void setActivityManager(android.app.ActivityManager manager) {
        this.mActivityManager = manager;
    }

    boolean canPresentAsBubble(com.android.server.notification.NotificationRecord r) {
        java.lang.String notificationShortcutId;
        if (!this.mSupportsBubble) {
            return false;
        }
        android.app.Notification notification = r.getNotification();
        android.app.Notification.BubbleMetadata metadata = notification.getBubbleMetadata();
        java.lang.String pkg = r.getSbn().getPackageName();
        if (metadata == null) {
            return false;
        }
        java.lang.String shortcutId = metadata.getShortcutId();
        if (r.getShortcutInfo() != null) {
            notificationShortcutId = r.getShortcutInfo().getId();
        } else {
            notificationShortcutId = null;
        }
        boolean shortcutValid = false;
        if (notificationShortcutId != null && shortcutId != null) {
            shortcutValid = shortcutId.equals(notificationShortcutId);
        } else if (shortcutId != null) {
            shortcutValid = this.mShortcutHelper.getValidShortcutInfo(shortcutId, pkg, r.getUser()) != null;
        }
        if (metadata.getIntent() == null && !shortcutValid) {
            logBubbleError(r.getKey(), "couldn't find valid shortcut for bubble with shortcutId: " + shortcutId);
            return false;
        }
        if (shortcutValid) {
            return true;
        }
        return canLaunchInTaskView(this.mContext, metadata.getIntent(), pkg);
    }

    protected boolean canLaunchInTaskView(android.content.Context context, android.app.PendingIntent pendingIntent, java.lang.String packageName) {
        android.content.pm.ActivityInfo info;
        if (pendingIntent == null) {
            android.util.Slog.w(TAG, "Unable to create bubble -- no intent");
            return false;
        }
        android.content.Intent intent = pendingIntent.getIntent();
        if (intent != null) {
            info = intent.resolveActivityInfo(context.getPackageManager(), 0);
        } else {
            info = null;
        }
        if (info == null) {
            com.android.internal.util.FrameworkStatsLog.write(173, packageName, 1);
            android.util.Slog.w(TAG, "Unable to send as bubble -- couldn't find activity info for intent: " + intent);
            return false;
        }
        if (android.content.pm.ActivityInfo.isResizeableMode(info.resizeMode)) {
            return true;
        }
        com.android.internal.util.FrameworkStatsLog.write(173, packageName, 2);
        android.util.Slog.w(TAG, "Unable to send as bubble -- activity is not resizable for intent: " + intent);
        return false;
    }

    private void logBubbleError(java.lang.String key, java.lang.String failureMessage) {
    }
}
