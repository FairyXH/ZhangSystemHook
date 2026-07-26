package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.companion.virtual.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.companion.virtual.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.companion.virtual.Flags.FLAG_DUMP_HISTORY, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.companion.virtual.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.companion.virtual.FeatureFlags
    public boolean dumpHistory() {
        return getValue(com.android.server.companion.virtual.Flags.FLAG_DUMP_HISTORY, new java.util.function.Predicate() { // from class: com.android.server.companion.virtual.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.companion.virtual.FeatureFlags) obj).dumpHistory();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.companion.virtual.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.companion.virtual.Flags.FLAG_DUMP_HISTORY);
    }
}
