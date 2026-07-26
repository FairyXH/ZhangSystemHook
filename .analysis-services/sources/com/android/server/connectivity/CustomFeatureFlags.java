package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.connectivity.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.connectivity.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.connectivity.Flags.FLAG_REPLACE_VPN_PROFILE_STORE, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.connectivity.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.connectivity.FeatureFlags
    public boolean replaceVpnProfileStore() {
        return getValue(com.android.server.connectivity.Flags.FLAG_REPLACE_VPN_PROFILE_STORE, new java.util.function.Predicate() { // from class: com.android.server.connectivity.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.connectivity.FeatureFlags) obj).replaceVpnProfileStore();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.connectivity.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.connectivity.Flags.FLAG_REPLACE_VPN_PROFILE_STORE);
    }
}
