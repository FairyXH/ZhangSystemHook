package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
public class CustomFeatureFlags implements com.android.server.dreams.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.dreams.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.dreams.Flags.FLAG_USE_BATTERY_CHANGED_BROADCAST, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.dreams.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.dreams.FeatureFlags
    public boolean useBatteryChangedBroadcast() {
        return getValue(com.android.server.dreams.Flags.FLAG_USE_BATTERY_CHANGED_BROADCAST, new java.util.function.Predicate() { // from class: com.android.server.dreams.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.dreams.FeatureFlags) obj).useBatteryChangedBroadcast();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.dreams.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.dreams.Flags.FLAG_USE_BATTERY_CHANGED_BROADCAST);
    }
}
