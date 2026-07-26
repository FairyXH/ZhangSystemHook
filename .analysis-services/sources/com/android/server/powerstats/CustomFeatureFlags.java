package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.powerstats.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.powerstats.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.powerstats.Flags.FLAG_ALARM_BASED_POWERSTATS_LOGGING, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.powerstats.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.powerstats.FeatureFlags
    public boolean alarmBasedPowerstatsLogging() {
        return getValue(com.android.server.powerstats.Flags.FLAG_ALARM_BASED_POWERSTATS_LOGGING, new java.util.function.Predicate() { // from class: com.android.server.powerstats.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.powerstats.FeatureFlags) obj).alarmBasedPowerstatsLogging();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.powerstats.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.powerstats.Flags.FLAG_ALARM_BASED_POWERSTATS_LOGGING);
    }
}
