package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.alarm.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.alarm.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.alarm.Flags.FLAG_START_USER_BEFORE_SCHEDULED_ALARMS, com.android.server.alarm.Flags.FLAG_USE_FROZEN_STATE_TO_DROP_LISTENER_ALARMS, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.alarm.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.alarm.FeatureFlags
    public boolean startUserBeforeScheduledAlarms() {
        return getValue(com.android.server.alarm.Flags.FLAG_START_USER_BEFORE_SCHEDULED_ALARMS, new java.util.function.Predicate() { // from class: com.android.server.alarm.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.alarm.FeatureFlags) obj).startUserBeforeScheduledAlarms();
            }
        });
    }

    @Override // com.android.server.alarm.FeatureFlags
    public boolean useFrozenStateToDropListenerAlarms() {
        return getValue(com.android.server.alarm.Flags.FLAG_USE_FROZEN_STATE_TO_DROP_LISTENER_ALARMS, new java.util.function.Predicate() { // from class: com.android.server.alarm.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.alarm.FeatureFlags) obj).useFrozenStateToDropListenerAlarms();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.alarm.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.alarm.Flags.FLAG_START_USER_BEFORE_SCHEDULED_ALARMS, com.android.server.alarm.Flags.FLAG_USE_FROZEN_STATE_TO_DROP_LISTENER_ALARMS);
    }
}
