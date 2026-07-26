package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.usage.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.usage.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.usage.Flags.FLAG_AVOID_IDLE_CHECK, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.usage.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.usage.FeatureFlags
    public boolean avoidIdleCheck() {
        return getValue(com.android.server.usage.Flags.FLAG_AVOID_IDLE_CHECK, new java.util.function.Predicate() { // from class: com.android.server.usage.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.usage.FeatureFlags) obj).avoidIdleCheck();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.usage.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.usage.Flags.FLAG_AVOID_IDLE_CHECK);
    }
}
