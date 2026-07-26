package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface FeatureFlags {
    boolean allNotifsNeedTtl();

    boolean autogroupSummaryIconUpdate();

    boolean crossAppPoliteNotifications();

    boolean exitInvalidCancelEarly();

    boolean expireBitmaps();

    boolean notificationCustomViewUriRestriction();

    boolean notificationHideUnusedChannels();

    boolean notificationReduceMessagequeueUsage();

    boolean notificationTest();

    boolean persistIncompleteRestoreData();

    boolean politeNotifications();

    boolean politeNotificationsAttnUpdate();

    boolean refactorAttentionHelper();

    boolean rejectOldNotifications();

    boolean screenshareNotificationHiding();

    boolean traceCancelEvents();

    boolean useIpcdatacacheChannels();

    boolean useSsmUserSwitchSignal();

    boolean vibrateWhileUnlocked();
}
