package com.android.server.power.hint;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.power.hint.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.power.hint.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.power.hint.Flags.FLAG_ADPF_SESSION_TAG, com.android.server.power.hint.Flags.FLAG_POWERHINT_THREAD_CLEANUP, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.power.hint.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.power.hint.FeatureFlags
    public boolean adpfSessionTag() {
        return getValue(com.android.server.power.hint.Flags.FLAG_ADPF_SESSION_TAG, new java.util.function.Predicate() { // from class: com.android.server.power.hint.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.power.hint.FeatureFlags) obj).adpfSessionTag();
            }
        });
    }

    @Override // com.android.server.power.hint.FeatureFlags
    public boolean powerhintThreadCleanup() {
        return getValue(com.android.server.power.hint.Flags.FLAG_POWERHINT_THREAD_CLEANUP, new java.util.function.Predicate() { // from class: com.android.server.power.hint.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.power.hint.FeatureFlags) obj).powerhintThreadCleanup();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.power.hint.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.power.hint.Flags.FLAG_ADPF_SESSION_TAG, com.android.server.power.hint.Flags.FLAG_POWERHINT_THREAD_CLEANUP);
    }
}
