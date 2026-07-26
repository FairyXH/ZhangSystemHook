package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public class CustomFeatureFlags implements com.android.server.os.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.os.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.os.Flags.FLAG_ASYNC_START_BUGREPORT, com.android.server.os.Flags.FLAG_PROTO_TOMBSTONE, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.os.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.os.FeatureFlags
    public boolean asyncStartBugreport() {
        return getValue(com.android.server.os.Flags.FLAG_ASYNC_START_BUGREPORT, new java.util.function.Predicate() { // from class: com.android.server.os.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.os.FeatureFlags) obj).asyncStartBugreport();
            }
        });
    }

    @Override // com.android.server.os.FeatureFlags
    public boolean protoTombstone() {
        return getValue(com.android.server.os.Flags.FLAG_PROTO_TOMBSTONE, new java.util.function.Predicate() { // from class: com.android.server.os.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.os.FeatureFlags) obj).protoTombstone();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.os.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.os.Flags.FLAG_ASYNC_START_BUGREPORT, com.android.server.os.Flags.FLAG_PROTO_TOMBSTONE);
    }
}
