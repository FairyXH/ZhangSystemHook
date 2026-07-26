package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public class CustomFeatureFlags implements com.android.server.job.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.job.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.job.Flags.FLAG_BATCH_ACTIVE_BUCKET_JOBS, com.android.server.job.Flags.FLAG_BATCH_CONNECTIVITY_JOBS_PER_NETWORK, com.android.server.job.Flags.FLAG_COUNT_QUOTA_FIX, com.android.server.job.Flags.FLAG_DO_NOT_FORCE_RUSH_EXECUTION_AT_BOOT, com.android.server.job.Flags.FLAG_RELAX_PREFETCH_CONNECTIVITY_CONSTRAINT_ONLY_ON_CHARGER, com.android.server.job.Flags.FLAG_THERMAL_RESTRICTIONS_TO_FGS_JOBS, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.job.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean batchActiveBucketJobs() {
        return getValue(com.android.server.job.Flags.FLAG_BATCH_ACTIVE_BUCKET_JOBS, new java.util.function.Predicate() { // from class: com.android.server.job.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.job.FeatureFlags) obj).batchActiveBucketJobs();
            }
        });
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean batchConnectivityJobsPerNetwork() {
        return getValue(com.android.server.job.Flags.FLAG_BATCH_CONNECTIVITY_JOBS_PER_NETWORK, new java.util.function.Predicate() { // from class: com.android.server.job.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.job.FeatureFlags) obj).batchConnectivityJobsPerNetwork();
            }
        });
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean countQuotaFix() {
        return getValue(com.android.server.job.Flags.FLAG_COUNT_QUOTA_FIX, new java.util.function.Predicate() { // from class: com.android.server.job.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.job.FeatureFlags) obj).countQuotaFix();
            }
        });
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean doNotForceRushExecutionAtBoot() {
        return getValue(com.android.server.job.Flags.FLAG_DO_NOT_FORCE_RUSH_EXECUTION_AT_BOOT, new java.util.function.Predicate() { // from class: com.android.server.job.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.job.FeatureFlags) obj).doNotForceRushExecutionAtBoot();
            }
        });
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean relaxPrefetchConnectivityConstraintOnlyOnCharger() {
        return getValue(com.android.server.job.Flags.FLAG_RELAX_PREFETCH_CONNECTIVITY_CONSTRAINT_ONLY_ON_CHARGER, new java.util.function.Predicate() { // from class: com.android.server.job.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.job.FeatureFlags) obj).relaxPrefetchConnectivityConstraintOnlyOnCharger();
            }
        });
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean thermalRestrictionsToFgsJobs() {
        return getValue(com.android.server.job.Flags.FLAG_THERMAL_RESTRICTIONS_TO_FGS_JOBS, new java.util.function.Predicate() { // from class: com.android.server.job.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.job.FeatureFlags) obj).thermalRestrictionsToFgsJobs();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.job.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.job.Flags.FLAG_BATCH_ACTIVE_BUCKET_JOBS, com.android.server.job.Flags.FLAG_BATCH_CONNECTIVITY_JOBS_PER_NETWORK, com.android.server.job.Flags.FLAG_COUNT_QUOTA_FIX, com.android.server.job.Flags.FLAG_DO_NOT_FORCE_RUSH_EXECUTION_AT_BOOT, com.android.server.job.Flags.FLAG_RELAX_PREFETCH_CONNECTIVITY_CONSTRAINT_ONLY_ON_CHARGER, com.android.server.job.Flags.FLAG_THERMAL_RESTRICTIONS_TO_FGS_JOBS);
    }
}
