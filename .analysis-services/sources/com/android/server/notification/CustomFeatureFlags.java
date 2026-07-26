package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class CustomFeatureFlags implements com.android.server.notification.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.notification.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.notification.Flags.FLAG_ALL_NOTIFS_NEED_TTL, com.android.server.notification.Flags.FLAG_AUTOGROUP_SUMMARY_ICON_UPDATE, com.android.server.notification.Flags.FLAG_CROSS_APP_POLITE_NOTIFICATIONS, com.android.server.notification.Flags.FLAG_EXIT_INVALID_CANCEL_EARLY, com.android.server.notification.Flags.FLAG_EXPIRE_BITMAPS, com.android.server.notification.Flags.FLAG_NOTIFICATION_CUSTOM_VIEW_URI_RESTRICTION, com.android.server.notification.Flags.FLAG_NOTIFICATION_HIDE_UNUSED_CHANNELS, com.android.server.notification.Flags.FLAG_NOTIFICATION_REDUCE_MESSAGEQUEUE_USAGE, com.android.server.notification.Flags.FLAG_NOTIFICATION_TEST, com.android.server.notification.Flags.FLAG_PERSIST_INCOMPLETE_RESTORE_DATA, com.android.server.notification.Flags.FLAG_POLITE_NOTIFICATIONS_ATTN_UPDATE, com.android.server.notification.Flags.FLAG_REFACTOR_ATTENTION_HELPER, com.android.server.notification.Flags.FLAG_REJECT_OLD_NOTIFICATIONS, com.android.server.notification.Flags.FLAG_SCREENSHARE_NOTIFICATION_HIDING, com.android.server.notification.Flags.FLAG_TRACE_CANCEL_EVENTS, com.android.server.notification.Flags.FLAG_USE_IPCDATACACHE_CHANNELS, com.android.server.notification.Flags.FLAG_USE_SSM_USER_SWITCH_SIGNAL, com.android.server.notification.Flags.FLAG_VIBRATE_WHILE_UNLOCKED, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.notification.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean allNotifsNeedTtl() {
        return getValue(com.android.server.notification.Flags.FLAG_ALL_NOTIFS_NEED_TTL, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).allNotifsNeedTtl();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean autogroupSummaryIconUpdate() {
        return getValue(com.android.server.notification.Flags.FLAG_AUTOGROUP_SUMMARY_ICON_UPDATE, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).autogroupSummaryIconUpdate();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean crossAppPoliteNotifications() {
        return getValue(com.android.server.notification.Flags.FLAG_CROSS_APP_POLITE_NOTIFICATIONS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).crossAppPoliteNotifications();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean exitInvalidCancelEarly() {
        return getValue(com.android.server.notification.Flags.FLAG_EXIT_INVALID_CANCEL_EARLY, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).exitInvalidCancelEarly();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean expireBitmaps() {
        return getValue(com.android.server.notification.Flags.FLAG_EXPIRE_BITMAPS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).expireBitmaps();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationCustomViewUriRestriction() {
        return getValue(com.android.server.notification.Flags.FLAG_NOTIFICATION_CUSTOM_VIEW_URI_RESTRICTION, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).notificationCustomViewUriRestriction();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationHideUnusedChannels() {
        return getValue(com.android.server.notification.Flags.FLAG_NOTIFICATION_HIDE_UNUSED_CHANNELS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).notificationHideUnusedChannels();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationReduceMessagequeueUsage() {
        return getValue(com.android.server.notification.Flags.FLAG_NOTIFICATION_REDUCE_MESSAGEQUEUE_USAGE, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).notificationReduceMessagequeueUsage();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationTest() {
        return getValue(com.android.server.notification.Flags.FLAG_NOTIFICATION_TEST, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).notificationTest();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean persistIncompleteRestoreData() {
        return getValue(com.android.server.notification.Flags.FLAG_PERSIST_INCOMPLETE_RESTORE_DATA, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).persistIncompleteRestoreData();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean politeNotifications() {
        return getValue(com.android.server.notification.Flags.FLAG_POLITE_NOTIFICATIONS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).politeNotifications();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean politeNotificationsAttnUpdate() {
        return getValue(com.android.server.notification.Flags.FLAG_POLITE_NOTIFICATIONS_ATTN_UPDATE, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).politeNotificationsAttnUpdate();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean refactorAttentionHelper() {
        return getValue(com.android.server.notification.Flags.FLAG_REFACTOR_ATTENTION_HELPER, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).refactorAttentionHelper();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean rejectOldNotifications() {
        return getValue(com.android.server.notification.Flags.FLAG_REJECT_OLD_NOTIFICATIONS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).rejectOldNotifications();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean screenshareNotificationHiding() {
        return getValue(com.android.server.notification.Flags.FLAG_SCREENSHARE_NOTIFICATION_HIDING, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).screenshareNotificationHiding();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean traceCancelEvents() {
        return getValue(com.android.server.notification.Flags.FLAG_TRACE_CANCEL_EVENTS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).traceCancelEvents();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean useIpcdatacacheChannels() {
        return getValue(com.android.server.notification.Flags.FLAG_USE_IPCDATACACHE_CHANNELS, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).useIpcdatacacheChannels();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean useSsmUserSwitchSignal() {
        return getValue(com.android.server.notification.Flags.FLAG_USE_SSM_USER_SWITCH_SIGNAL, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).useSsmUserSwitchSignal();
            }
        });
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean vibrateWhileUnlocked() {
        return getValue(com.android.server.notification.Flags.FLAG_VIBRATE_WHILE_UNLOCKED, new java.util.function.Predicate() { // from class: com.android.server.notification.CustomFeatureFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.notification.FeatureFlags) obj).vibrateWhileUnlocked();
            }
        });
    }

    public boolean isFlagReadOnlyOptimized(java.lang.String flagName) {
        if (this.mReadOnlyFlagsSet.contains(flagName) && isOptimizationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isOptimizationEnabled() {
        return false;
    }

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.notification.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.notification.Flags.FLAG_ALL_NOTIFS_NEED_TTL, com.android.server.notification.Flags.FLAG_AUTOGROUP_SUMMARY_ICON_UPDATE, com.android.server.notification.Flags.FLAG_CROSS_APP_POLITE_NOTIFICATIONS, com.android.server.notification.Flags.FLAG_EXIT_INVALID_CANCEL_EARLY, com.android.server.notification.Flags.FLAG_EXPIRE_BITMAPS, com.android.server.notification.Flags.FLAG_NOTIFICATION_CUSTOM_VIEW_URI_RESTRICTION, com.android.server.notification.Flags.FLAG_NOTIFICATION_HIDE_UNUSED_CHANNELS, com.android.server.notification.Flags.FLAG_NOTIFICATION_REDUCE_MESSAGEQUEUE_USAGE, com.android.server.notification.Flags.FLAG_NOTIFICATION_TEST, com.android.server.notification.Flags.FLAG_PERSIST_INCOMPLETE_RESTORE_DATA, com.android.server.notification.Flags.FLAG_POLITE_NOTIFICATIONS, com.android.server.notification.Flags.FLAG_POLITE_NOTIFICATIONS_ATTN_UPDATE, com.android.server.notification.Flags.FLAG_REFACTOR_ATTENTION_HELPER, com.android.server.notification.Flags.FLAG_REJECT_OLD_NOTIFICATIONS, com.android.server.notification.Flags.FLAG_SCREENSHARE_NOTIFICATION_HIDING, com.android.server.notification.Flags.FLAG_TRACE_CANCEL_EVENTS, com.android.server.notification.Flags.FLAG_USE_IPCDATACACHE_CHANNELS, com.android.server.notification.Flags.FLAG_USE_SSM_USER_SWITCH_SIGNAL, com.android.server.notification.Flags.FLAG_VIBRATE_WHILE_UNLOCKED);
    }
}
