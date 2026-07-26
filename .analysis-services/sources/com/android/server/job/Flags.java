package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class Flags {
    private static com.android.server.job.FeatureFlags FEATURE_FLAGS = new com.android.server.job.FeatureFlagsImpl();
    public static final java.lang.String FLAG_BATCH_ACTIVE_BUCKET_JOBS = "com.android.server.job.batch_active_bucket_jobs";
    public static final java.lang.String FLAG_BATCH_CONNECTIVITY_JOBS_PER_NETWORK = "com.android.server.job.batch_connectivity_jobs_per_network";
    public static final java.lang.String FLAG_COUNT_QUOTA_FIX = "com.android.server.job.count_quota_fix";
    public static final java.lang.String FLAG_DO_NOT_FORCE_RUSH_EXECUTION_AT_BOOT = "com.android.server.job.do_not_force_rush_execution_at_boot";
    public static final java.lang.String FLAG_RELAX_PREFETCH_CONNECTIVITY_CONSTRAINT_ONLY_ON_CHARGER = "com.android.server.job.relax_prefetch_connectivity_constraint_only_on_charger";
    public static final java.lang.String FLAG_THERMAL_RESTRICTIONS_TO_FGS_JOBS = "com.android.server.job.thermal_restrictions_to_fgs_jobs";

    public static boolean batchActiveBucketJobs() {
        return FEATURE_FLAGS.batchActiveBucketJobs();
    }

    public static boolean batchConnectivityJobsPerNetwork() {
        return FEATURE_FLAGS.batchConnectivityJobsPerNetwork();
    }

    public static boolean countQuotaFix() {
        return FEATURE_FLAGS.countQuotaFix();
    }

    public static boolean doNotForceRushExecutionAtBoot() {
        return FEATURE_FLAGS.doNotForceRushExecutionAtBoot();
    }

    public static boolean relaxPrefetchConnectivityConstraintOnlyOnCharger() {
        return FEATURE_FLAGS.relaxPrefetchConnectivityConstraintOnlyOnCharger();
    }

    public static boolean thermalRestrictionsToFgsJobs() {
        return FEATURE_FLAGS.thermalRestrictionsToFgsJobs();
    }
}
