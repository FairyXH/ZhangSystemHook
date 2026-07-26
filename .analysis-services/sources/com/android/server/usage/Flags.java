package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.usage.FeatureFlags FEATURE_FLAGS = new com.android.server.usage.FeatureFlagsImpl();
    public static final java.lang.String FLAG_AVOID_IDLE_CHECK = "com.android.server.usage.avoid_idle_check";

    public static boolean avoidIdleCheck() {
        return FEATURE_FLAGS.avoidIdleCheck();
    }
}
