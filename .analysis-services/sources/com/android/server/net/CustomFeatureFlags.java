package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public class CustomFeatureFlags implements com.android.server.net.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.net.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.net.Flags.FLAG_NETWORK_BLOCKED_FOR_TOP_SLEEPING_AND_ABOVE, com.android.server.net.Flags.FLAG_USE_DIFFERENT_DELAYS_FOR_BACKGROUND_CHAIN, com.android.server.net.Flags.FLAG_USE_METERED_FIREWALL_CHAINS, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.net.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.net.FeatureFlags
    public boolean networkBlockedForTopSleepingAndAbove() {
        return getValue(com.android.server.net.Flags.FLAG_NETWORK_BLOCKED_FOR_TOP_SLEEPING_AND_ABOVE, new java.util.function.Predicate() { // from class: com.android.server.net.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.net.FeatureFlags) obj).networkBlockedForTopSleepingAndAbove();
            }
        });
    }

    @Override // com.android.server.net.FeatureFlags
    public boolean useDifferentDelaysForBackgroundChain() {
        return getValue(com.android.server.net.Flags.FLAG_USE_DIFFERENT_DELAYS_FOR_BACKGROUND_CHAIN, new java.util.function.Predicate() { // from class: com.android.server.net.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.net.FeatureFlags) obj).useDifferentDelaysForBackgroundChain();
            }
        });
    }

    @Override // com.android.server.net.FeatureFlags
    public boolean useMeteredFirewallChains() {
        return getValue(com.android.server.net.Flags.FLAG_USE_METERED_FIREWALL_CHAINS, new java.util.function.Predicate() { // from class: com.android.server.net.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.net.FeatureFlags) obj).useMeteredFirewallChains();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.net.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.net.Flags.FLAG_NETWORK_BLOCKED_FOR_TOP_SLEEPING_AND_ABOVE, com.android.server.net.Flags.FLAG_USE_DIFFERENT_DELAYS_FOR_BACKGROUND_CHAIN, com.android.server.net.Flags.FLAG_USE_METERED_FIREWALL_CHAINS);
    }
}
