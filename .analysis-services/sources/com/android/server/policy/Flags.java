package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.policy.FeatureFlags FEATURE_FLAGS = new com.android.server.policy.FeatureFlagsImpl();
    public static final java.lang.String FLAG_SUPPORT_INPUT_WAKEUP_DELEGATE = "com.android.server.policy.support_input_wakeup_delegate";

    public static boolean supportInputWakeupDelegate() {
        return FEATURE_FLAGS.supportInputWakeupDelegate();
    }
}
