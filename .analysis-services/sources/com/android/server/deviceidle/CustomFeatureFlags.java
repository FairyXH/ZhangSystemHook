package com.android.server.deviceidle;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.deviceidle.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.deviceidle.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.deviceidle.Flags.FLAG_DISABLE_WAKELOCKS_IN_LIGHT_IDLE, com.android.server.deviceidle.Flags.FLAG_REMOVE_IDLE_LOCATION, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.deviceidle.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.deviceidle.FeatureFlags
    public boolean disableWakelocksInLightIdle() {
        return getValue(com.android.server.deviceidle.Flags.FLAG_DISABLE_WAKELOCKS_IN_LIGHT_IDLE, new java.util.function.Predicate() { // from class: com.android.server.deviceidle.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.deviceidle.FeatureFlags) obj).disableWakelocksInLightIdle();
            }
        });
    }

    @Override // com.android.server.deviceidle.FeatureFlags
    public boolean removeIdleLocation() {
        return getValue(com.android.server.deviceidle.Flags.FLAG_REMOVE_IDLE_LOCATION, new java.util.function.Predicate() { // from class: com.android.server.deviceidle.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.deviceidle.FeatureFlags) obj).removeIdleLocation();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.deviceidle.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.deviceidle.Flags.FLAG_DISABLE_WAKELOCKS_IN_LIGHT_IDLE, com.android.server.deviceidle.Flags.FLAG_REMOVE_IDLE_LOCATION);
    }
}
