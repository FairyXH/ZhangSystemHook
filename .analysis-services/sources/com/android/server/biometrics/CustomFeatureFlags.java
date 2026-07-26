package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.biometrics.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.biometrics.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.biometrics.Flags.FLAG_FACE_VHAL_FEATURE, com.android.server.biometrics.Flags.FLAG_USE_VHAL_FOR_TESTING, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.biometrics.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.biometrics.FeatureFlags
    public boolean faceVhalFeature() {
        return getValue(com.android.server.biometrics.Flags.FLAG_FACE_VHAL_FEATURE, new java.util.function.Predicate() { // from class: com.android.server.biometrics.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.biometrics.FeatureFlags) obj).faceVhalFeature();
            }
        });
    }

    @Override // com.android.server.biometrics.FeatureFlags
    public boolean useVhalForTesting() {
        return getValue(com.android.server.biometrics.Flags.FLAG_USE_VHAL_FOR_TESTING, new java.util.function.Predicate() { // from class: com.android.server.biometrics.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.biometrics.FeatureFlags) obj).useVhalForTesting();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.biometrics.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.biometrics.Flags.FLAG_FACE_VHAL_FEATURE, com.android.server.biometrics.Flags.FLAG_USE_VHAL_FOR_TESTING);
    }
}
