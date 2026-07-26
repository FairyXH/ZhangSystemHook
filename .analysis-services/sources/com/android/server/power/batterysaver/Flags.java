package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.power.batterysaver.FeatureFlags FEATURE_FLAGS = new com.android.server.power.batterysaver.FeatureFlagsImpl();
    public static final java.lang.String FLAG_UPDATE_AUTO_TURN_ON_NOTIFICATION_STRING_AND_ACTION = "com.android.server.power.batterysaver.update_auto_turn_on_notification_string_and_action";

    public static boolean updateAutoTurnOnNotificationStringAndAction() {
        return FEATURE_FLAGS.updateAutoTurnOnNotificationStringAndAction();
    }
}
