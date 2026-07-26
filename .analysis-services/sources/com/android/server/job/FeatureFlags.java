package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface FeatureFlags {
    boolean batchActiveBucketJobs();

    boolean batchConnectivityJobsPerNetwork();

    boolean countQuotaFix();

    boolean doNotForceRushExecutionAtBoot();

    boolean relaxPrefetchConnectivityConstraintOnlyOnCharger();

    boolean thermalRestrictionsToFgsJobs();
}
