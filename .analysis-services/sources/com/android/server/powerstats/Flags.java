package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.powerstats.FeatureFlags FEATURE_FLAGS = new com.android.server.powerstats.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ALARM_BASED_POWERSTATS_LOGGING = "com.android.server.powerstats.alarm_based_powerstats_logging";

    public static boolean alarmBasedPowerstatsLogging() {
        return FEATURE_FLAGS.alarmBasedPowerstatsLogging();
    }
}
