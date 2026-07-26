package com.android.systemui.shared;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.systemui.shared.FeatureFlags FEATURE_FLAGS = new com.android.systemui.shared.FeatureFlagsImpl();
    public static final java.lang.String FLAG_BOUNCER_AREA_EXCLUSION = "com.android.systemui.shared.bouncer_area_exclusion";
    public static final java.lang.String FLAG_ENABLE_HOME_DELAY = "com.android.systemui.shared.enable_home_delay";
    public static final java.lang.String FLAG_EXAMPLE_SHARED_FLAG = "com.android.systemui.shared.example_shared_flag";
    public static final java.lang.String FLAG_RETURN_ANIMATION_FRAMEWORK_LIBRARY = "com.android.systemui.shared.return_animation_framework_library";
    public static final java.lang.String FLAG_SHADE_ALLOW_BACK_GESTURE = "com.android.systemui.shared.shade_allow_back_gesture";
    public static final java.lang.String FLAG_SIDEFPS_CONTROLLER_REFACTOR = "com.android.systemui.shared.sidefps_controller_refactor";

    public static boolean bouncerAreaExclusion() {
        return FEATURE_FLAGS.bouncerAreaExclusion();
    }

    public static boolean enableHomeDelay() {
        return FEATURE_FLAGS.enableHomeDelay();
    }

    public static boolean exampleSharedFlag() {
        return FEATURE_FLAGS.exampleSharedFlag();
    }

    public static boolean returnAnimationFrameworkLibrary() {
        return FEATURE_FLAGS.returnAnimationFrameworkLibrary();
    }

    public static boolean shadeAllowBackGesture() {
        return FEATURE_FLAGS.shadeAllowBackGesture();
    }

    public static boolean sidefpsControllerRefactor() {
        return FEATURE_FLAGS.sidefpsControllerRefactor();
    }
}
