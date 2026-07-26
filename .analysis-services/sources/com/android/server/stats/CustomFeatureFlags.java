package com.android.server.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.stats.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.stats.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.stats.Flags.FLAG_ADD_MOBILE_BYTES_TRANSFER_BY_PROC_STATE_PULLER, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.stats.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.stats.FeatureFlags
    public boolean addMobileBytesTransferByProcStatePuller() {
        return getValue(com.android.server.stats.Flags.FLAG_ADD_MOBILE_BYTES_TRANSFER_BY_PROC_STATE_PULLER, new java.util.function.Predicate() { // from class: com.android.server.stats.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.stats.FeatureFlags) obj).addMobileBytesTransferByProcStatePuller();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.stats.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.stats.Flags.FLAG_ADD_MOBILE_BYTES_TRANSFER_BY_PROC_STATE_PULLER);
    }
}
