package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.companion.virtual.FeatureFlags FEATURE_FLAGS = new com.android.server.companion.virtual.FeatureFlagsImpl();
    public static final java.lang.String FLAG_DUMP_HISTORY = "com.android.server.companion.virtual.dump_history";

    public static boolean dumpHistory() {
        return FEATURE_FLAGS.dumpHistory();
    }
}
