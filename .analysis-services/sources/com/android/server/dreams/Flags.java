package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
public final class Flags {
    private static com.android.server.dreams.FeatureFlags FEATURE_FLAGS = new com.android.server.dreams.FeatureFlagsImpl();
    public static final java.lang.String FLAG_USE_BATTERY_CHANGED_BROADCAST = "com.android.server.dreams.use_battery_changed_broadcast";

    public static boolean useBatteryChangedBroadcast() {
        return FEATURE_FLAGS.useBatteryChangedBroadcast();
    }
}
