package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.biometrics.FeatureFlags FEATURE_FLAGS = new com.android.server.biometrics.FeatureFlagsImpl();
    public static final java.lang.String FLAG_FACE_VHAL_FEATURE = "com.android.server.biometrics.face_vhal_feature";
    public static final java.lang.String FLAG_USE_VHAL_FOR_TESTING = "com.android.server.biometrics.use_vhal_for_testing";

    public static boolean faceVhalFeature() {
        return FEATURE_FLAGS.faceVhalFeature();
    }

    public static boolean useVhalForTesting() {
        return FEATURE_FLAGS.useVhalForTesting();
    }
}
