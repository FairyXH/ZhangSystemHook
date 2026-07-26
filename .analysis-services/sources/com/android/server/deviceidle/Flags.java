package com.android.server.deviceidle;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.deviceidle.FeatureFlags FEATURE_FLAGS = new com.android.server.deviceidle.FeatureFlagsImpl();
    public static final java.lang.String FLAG_DISABLE_WAKELOCKS_IN_LIGHT_IDLE = "com.android.server.deviceidle.disable_wakelocks_in_light_idle";
    public static final java.lang.String FLAG_REMOVE_IDLE_LOCATION = "com.android.server.deviceidle.remove_idle_location";

    public static boolean disableWakelocksInLightIdle() {
        return FEATURE_FLAGS.disableWakelocksInLightIdle();
    }

    public static boolean removeIdleLocation() {
        return FEATURE_FLAGS.removeIdleLocation();
    }
}
