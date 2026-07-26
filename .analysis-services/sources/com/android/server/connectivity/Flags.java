package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.connectivity.FeatureFlags FEATURE_FLAGS = new com.android.server.connectivity.FeatureFlagsImpl();
    public static final java.lang.String FLAG_REPLACE_VPN_PROFILE_STORE = "com.android.server.connectivity.replace_vpn_profile_store";

    public static boolean replaceVpnProfileStore() {
        return FEATURE_FLAGS.replaceVpnProfileStore();
    }
}
