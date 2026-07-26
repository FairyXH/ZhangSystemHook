package com.android.server.power.hint;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.power.hint.FeatureFlags FEATURE_FLAGS = new com.android.server.power.hint.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ADPF_SESSION_TAG = "com.android.server.power.hint.adpf_session_tag";
    public static final java.lang.String FLAG_POWERHINT_THREAD_CLEANUP = "com.android.server.power.hint.powerhint_thread_cleanup";

    public static boolean adpfSessionTag() {
        return FEATURE_FLAGS.adpfSessionTag();
    }

    public static boolean powerhintThreadCleanup() {
        return FEATURE_FLAGS.powerhintThreadCleanup();
    }
}
