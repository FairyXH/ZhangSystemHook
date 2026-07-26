package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.policy.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.policy.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.policy.Flags.FLAG_SUPPORT_INPUT_WAKEUP_DELEGATE, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.policy.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.policy.FeatureFlags
    public boolean supportInputWakeupDelegate() {
        return getValue(com.android.server.policy.Flags.FLAG_SUPPORT_INPUT_WAKEUP_DELEGATE, new java.util.function.Predicate() { // from class: com.android.server.policy.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.policy.FeatureFlags) obj).supportInputWakeupDelegate();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.policy.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.policy.Flags.FLAG_SUPPORT_INPUT_WAKEUP_DELEGATE);
    }
}
