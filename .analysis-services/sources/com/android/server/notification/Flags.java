package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class Flags {
    private static com.android.server.notification.FeatureFlags FEATURE_FLAGS = new com.android.server.notification.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ALL_NOTIFS_NEED_TTL = "com.android.server.notification.all_notifs_need_ttl";
    public static final java.lang.String FLAG_AUTOGROUP_SUMMARY_ICON_UPDATE = "com.android.server.notification.autogroup_summary_icon_update";
    public static final java.lang.String FLAG_CROSS_APP_POLITE_NOTIFICATIONS = "com.android.server.notification.cross_app_polite_notifications";
    public static final java.lang.String FLAG_EXIT_INVALID_CANCEL_EARLY = "com.android.server.notification.exit_invalid_cancel_early";
    public static final java.lang.String FLAG_EXPIRE_BITMAPS = "com.android.server.notification.expire_bitmaps";
    public static final java.lang.String FLAG_NOTIFICATION_CUSTOM_VIEW_URI_RESTRICTION = "com.android.server.notification.notification_custom_view_uri_restriction";
    public static final java.lang.String FLAG_NOTIFICATION_HIDE_UNUSED_CHANNELS = "com.android.server.notification.notification_hide_unused_channels";
    public static final java.lang.String FLAG_NOTIFICATION_REDUCE_MESSAGEQUEUE_USAGE = "com.android.server.notification.notification_reduce_messagequeue_usage";
    public static final java.lang.String FLAG_NOTIFICATION_TEST = "com.android.server.notification.notification_test";
    public static final java.lang.String FLAG_PERSIST_INCOMPLETE_RESTORE_DATA = "com.android.server.notification.persist_incomplete_restore_data";
    public static final java.lang.String FLAG_POLITE_NOTIFICATIONS = "com.android.server.notification.polite_notifications";
    public static final java.lang.String FLAG_POLITE_NOTIFICATIONS_ATTN_UPDATE = "com.android.server.notification.polite_notifications_attn_update";
    public static final java.lang.String FLAG_REFACTOR_ATTENTION_HELPER = "com.android.server.notification.refactor_attention_helper";
    public static final java.lang.String FLAG_REJECT_OLD_NOTIFICATIONS = "com.android.server.notification.reject_old_notifications";
    public static final java.lang.String FLAG_SCREENSHARE_NOTIFICATION_HIDING = "com.android.server.notification.screenshare_notification_hiding";
    public static final java.lang.String FLAG_TRACE_CANCEL_EVENTS = "com.android.server.notification.trace_cancel_events";
    public static final java.lang.String FLAG_USE_IPCDATACACHE_CHANNELS = "com.android.server.notification.use_ipcdatacache_channels";
    public static final java.lang.String FLAG_USE_SSM_USER_SWITCH_SIGNAL = "com.android.server.notification.use_ssm_user_switch_signal";
    public static final java.lang.String FLAG_VIBRATE_WHILE_UNLOCKED = "com.android.server.notification.vibrate_while_unlocked";

    public static boolean allNotifsNeedTtl() {
        return FEATURE_FLAGS.allNotifsNeedTtl();
    }

    public static boolean autogroupSummaryIconUpdate() {
        return FEATURE_FLAGS.autogroupSummaryIconUpdate();
    }

    public static boolean crossAppPoliteNotifications() {
        return FEATURE_FLAGS.crossAppPoliteNotifications();
    }

    public static boolean exitInvalidCancelEarly() {
        return FEATURE_FLAGS.exitInvalidCancelEarly();
    }

    public static boolean expireBitmaps() {
        return FEATURE_FLAGS.expireBitmaps();
    }

    public static boolean notificationCustomViewUriRestriction() {
        return FEATURE_FLAGS.notificationCustomViewUriRestriction();
    }

    public static boolean notificationHideUnusedChannels() {
        return FEATURE_FLAGS.notificationHideUnusedChannels();
    }

    public static boolean notificationReduceMessagequeueUsage() {
        return FEATURE_FLAGS.notificationReduceMessagequeueUsage();
    }

    public static boolean notificationTest() {
        return FEATURE_FLAGS.notificationTest();
    }

    public static boolean persistIncompleteRestoreData() {
        return FEATURE_FLAGS.persistIncompleteRestoreData();
    }

    public static boolean politeNotifications() {
        return FEATURE_FLAGS.politeNotifications();
    }

    public static boolean politeNotificationsAttnUpdate() {
        return FEATURE_FLAGS.politeNotificationsAttnUpdate();
    }

    public static boolean refactorAttentionHelper() {
        return FEATURE_FLAGS.refactorAttentionHelper();
    }

    public static boolean rejectOldNotifications() {
        return FEATURE_FLAGS.rejectOldNotifications();
    }

    public static boolean screenshareNotificationHiding() {
        return FEATURE_FLAGS.screenshareNotificationHiding();
    }

    public static boolean traceCancelEvents() {
        return FEATURE_FLAGS.traceCancelEvents();
    }

    public static boolean useIpcdatacacheChannels() {
        return FEATURE_FLAGS.useIpcdatacacheChannels();
    }

    public static boolean useSsmUserSwitchSignal() {
        return FEATURE_FLAGS.useSsmUserSwitchSignal();
    }

    public static boolean vibrateWhileUnlocked() {
        return FEATURE_FLAGS.vibrateWhileUnlocked();
    }
}
