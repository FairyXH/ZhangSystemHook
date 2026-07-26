package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.power.batterysaver.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.power.batterysaver.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.power.batterysaver.Flags.FLAG_UPDATE_AUTO_TURN_ON_NOTIFICATION_STRING_AND_ACTION, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.power.batterysaver.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.power.batterysaver.FeatureFlags
    public boolean updateAutoTurnOnNotificationStringAndAction() {
        return getValue(com.android.server.power.batterysaver.Flags.FLAG_UPDATE_AUTO_TURN_ON_NOTIFICATION_STRING_AND_ACTION, new java.util.function.Predicate() { // from class: com.android.server.power.batterysaver.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.power.batterysaver.FeatureFlags) obj).updateAutoTurnOnNotificationStringAndAction();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.power.batterysaver.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.power.batterysaver.Flags.FLAG_UPDATE_AUTO_TURN_ON_NOTIFICATION_STRING_AND_ACTION);
    }
}
