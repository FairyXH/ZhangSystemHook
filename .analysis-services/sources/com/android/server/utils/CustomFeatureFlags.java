package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.utils.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.utils.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.utils.Flags.FLAG_ANR_TIMER_FREEZER, com.android.server.utils.Flags.FLAG_ANR_TIMER_SERVICE, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.utils.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.utils.FeatureFlags
    public boolean anrTimerFreezer() {
        return getValue(com.android.server.utils.Flags.FLAG_ANR_TIMER_FREEZER, new java.util.function.Predicate() { // from class: com.android.server.utils.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.utils.FeatureFlags) obj).anrTimerFreezer();
            }
        });
    }

    @Override // com.android.server.utils.FeatureFlags
    public boolean anrTimerService() {
        return getValue(com.android.server.utils.Flags.FLAG_ANR_TIMER_SERVICE, new java.util.function.Predicate() { // from class: com.android.server.utils.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.utils.FeatureFlags) obj).anrTimerService();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.utils.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.utils.Flags.FLAG_ANR_TIMER_FREEZER, com.android.server.utils.Flags.FLAG_ANR_TIMER_SERVICE);
    }
}
