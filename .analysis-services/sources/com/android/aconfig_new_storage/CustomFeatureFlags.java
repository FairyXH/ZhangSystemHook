package com.android.aconfig_new_storage;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.aconfig_new_storage.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.aconfig_new_storage.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.aconfig_new_storage.Flags.FLAG_ENABLE_ACONFIG_STORAGE_DAEMON, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.aconfig_new_storage.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.aconfig_new_storage.FeatureFlags
    public boolean enableAconfigStorageDaemon() {
        return getValue(com.android.aconfig_new_storage.Flags.FLAG_ENABLE_ACONFIG_STORAGE_DAEMON, new java.util.function.Predicate() { // from class: com.android.aconfig_new_storage.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.aconfig_new_storage.FeatureFlags) obj).enableAconfigStorageDaemon();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.aconfig_new_storage.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.aconfig_new_storage.Flags.FLAG_ENABLE_ACONFIG_STORAGE_DAEMON);
    }
}
