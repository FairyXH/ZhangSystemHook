package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public final class Flags {
    private static com.android.server.net.FeatureFlags FEATURE_FLAGS = new com.android.server.net.FeatureFlagsImpl();
    public static final java.lang.String FLAG_NETWORK_BLOCKED_FOR_TOP_SLEEPING_AND_ABOVE = "com.android.server.net.network_blocked_for_top_sleeping_and_above";
    public static final java.lang.String FLAG_USE_DIFFERENT_DELAYS_FOR_BACKGROUND_CHAIN = "com.android.server.net.use_different_delays_for_background_chain";
    public static final java.lang.String FLAG_USE_METERED_FIREWALL_CHAINS = "com.android.server.net.use_metered_firewall_chains";

    public static boolean networkBlockedForTopSleepingAndAbove() {
        return FEATURE_FLAGS.networkBlockedForTopSleepingAndAbove();
    }

    public static boolean useDifferentDelaysForBackgroundChain() {
        return FEATURE_FLAGS.useDifferentDelaysForBackgroundChain();
    }

    public static boolean useMeteredFirewallChains() {
        return FEATURE_FLAGS.useMeteredFirewallChains();
    }
}
