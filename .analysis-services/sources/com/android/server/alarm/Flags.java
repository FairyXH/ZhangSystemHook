package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.alarm.FeatureFlags FEATURE_FLAGS = new com.android.server.alarm.FeatureFlagsImpl();
    public static final java.lang.String FLAG_START_USER_BEFORE_SCHEDULED_ALARMS = "com.android.server.alarm.start_user_before_scheduled_alarms";
    public static final java.lang.String FLAG_USE_FROZEN_STATE_TO_DROP_LISTENER_ALARMS = "com.android.server.alarm.use_frozen_state_to_drop_listener_alarms";

    public static boolean startUserBeforeScheduledAlarms() {
        return FEATURE_FLAGS.startUserBeforeScheduledAlarms();
    }

    public static boolean useFrozenStateToDropListenerAlarms() {
        return FEATURE_FLAGS.useFrozenStateToDropListenerAlarms();
    }
}
