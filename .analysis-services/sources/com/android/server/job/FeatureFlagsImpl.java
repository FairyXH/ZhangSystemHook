package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class FeatureFlagsImpl implements com.android.server.job.FeatureFlags {
    @Override // com.android.server.job.FeatureFlags
    public boolean batchActiveBucketJobs() {
        return false;
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean batchConnectivityJobsPerNetwork() {
        return false;
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean countQuotaFix() {
        return true;
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean doNotForceRushExecutionAtBoot() {
        return false;
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean relaxPrefetchConnectivityConstraintOnlyOnCharger() {
        return true;
    }

    @Override // com.android.server.job.FeatureFlags
    public boolean thermalRestrictionsToFgsJobs() {
        return false;
    }
}
