package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.utils.FeatureFlags FEATURE_FLAGS = new com.android.server.utils.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ANR_TIMER_FREEZER = "com.android.server.utils.anr_timer_freezer";
    public static final java.lang.String FLAG_ANR_TIMER_SERVICE = "com.android.server.utils.anr_timer_service";

    public static boolean anrTimerFreezer() {
        return FEATURE_FLAGS.anrTimerFreezer();
    }

    public static boolean anrTimerService() {
        return FEATURE_FLAGS.anrTimerService();
    }
}
