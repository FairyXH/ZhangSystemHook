package com.android.systemui.shared;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.systemui.shared.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.systemui.shared.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.systemui.shared.Flags.FLAG_BOUNCER_AREA_EXCLUSION, com.android.systemui.shared.Flags.FLAG_ENABLE_HOME_DELAY, com.android.systemui.shared.Flags.FLAG_EXAMPLE_SHARED_FLAG, com.android.systemui.shared.Flags.FLAG_RETURN_ANIMATION_FRAMEWORK_LIBRARY, com.android.systemui.shared.Flags.FLAG_SHADE_ALLOW_BACK_GESTURE, com.android.systemui.shared.Flags.FLAG_SIDEFPS_CONTROLLER_REFACTOR, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.systemui.shared.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean bouncerAreaExclusion() {
        return getValue(com.android.systemui.shared.Flags.FLAG_BOUNCER_AREA_EXCLUSION, new java.util.function.Predicate() { // from class: com.android.systemui.shared.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.shared.FeatureFlags) obj).bouncerAreaExclusion();
            }
        });
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean enableHomeDelay() {
        return getValue(com.android.systemui.shared.Flags.FLAG_ENABLE_HOME_DELAY, new java.util.function.Predicate() { // from class: com.android.systemui.shared.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.shared.FeatureFlags) obj).enableHomeDelay();
            }
        });
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean exampleSharedFlag() {
        return getValue(com.android.systemui.shared.Flags.FLAG_EXAMPLE_SHARED_FLAG, new java.util.function.Predicate() { // from class: com.android.systemui.shared.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.shared.FeatureFlags) obj).exampleSharedFlag();
            }
        });
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean returnAnimationFrameworkLibrary() {
        return getValue(com.android.systemui.shared.Flags.FLAG_RETURN_ANIMATION_FRAMEWORK_LIBRARY, new java.util.function.Predicate() { // from class: com.android.systemui.shared.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.shared.FeatureFlags) obj).returnAnimationFrameworkLibrary();
            }
        });
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean shadeAllowBackGesture() {
        return getValue(com.android.systemui.shared.Flags.FLAG_SHADE_ALLOW_BACK_GESTURE, new java.util.function.Predicate() { // from class: com.android.systemui.shared.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.shared.FeatureFlags) obj).shadeAllowBackGesture();
            }
        });
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean sidefpsControllerRefactor() {
        return getValue(com.android.systemui.shared.Flags.FLAG_SIDEFPS_CONTROLLER_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.shared.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.shared.FeatureFlags) obj).sidefpsControllerRefactor();
            }
        });
    }

    public boolean isFlagReadOnlyOptimized(java.lang.String flagName) {
        if (this.mReadOnlyFlagsSet.contains(flagName) && isOptimizationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isOptimizationEnabled() {
        return false;
    }

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.systemui.shared.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.systemui.shared.Flags.FLAG_BOUNCER_AREA_EXCLUSION, com.android.systemui.shared.Flags.FLAG_ENABLE_HOME_DELAY, com.android.systemui.shared.Flags.FLAG_EXAMPLE_SHARED_FLAG, com.android.systemui.shared.Flags.FLAG_RETURN_ANIMATION_FRAMEWORK_LIBRARY, com.android.systemui.shared.Flags.FLAG_SHADE_ALLOW_BACK_GESTURE, com.android.systemui.shared.Flags.FLAG_SIDEFPS_CONTROLLER_REFACTOR);
    }
}
