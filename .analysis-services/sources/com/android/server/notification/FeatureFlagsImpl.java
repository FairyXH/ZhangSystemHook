package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class FeatureFlagsImpl implements com.android.server.notification.FeatureFlags {
    private static boolean systemui_is_cached = false;
    private static boolean politeNotifications = false;

    private void load_overrides_systemui() {
        try {
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("systemui", new java.lang.String[0]);
            politeNotifications = properties.getBoolean(com.android.server.notification.Flags.FLAG_POLITE_NOTIFICATIONS, false);
            systemui_is_cached = true;
        } catch (java.lang.NullPointerException e) {
            throw new java.lang.RuntimeException("Cannot read value from namespace systemui from DeviceConfig. It could be that the code using flag executed before SettingsProvider initialization. Please use fixed read-only flag by adding is_fixed_read_only: true in flag declaration.", e);
        }
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean allNotifsNeedTtl() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean autogroupSummaryIconUpdate() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean crossAppPoliteNotifications() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean exitInvalidCancelEarly() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean expireBitmaps() {
        return true;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationCustomViewUriRestriction() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationHideUnusedChannels() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationReduceMessagequeueUsage() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean notificationTest() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean persistIncompleteRestoreData() {
        return true;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean politeNotifications() {
        if (!systemui_is_cached) {
            load_overrides_systemui();
        }
        return politeNotifications;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean politeNotificationsAttnUpdate() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean refactorAttentionHelper() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean rejectOldNotifications() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean screenshareNotificationHiding() {
        return true;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean traceCancelEvents() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean useIpcdatacacheChannels() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean useSsmUserSwitchSignal() {
        return false;
    }

    @Override // com.android.server.notification.FeatureFlags
    public boolean vibrateWhileUnlocked() {
        return false;
    }
}
