package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class UsageStatsIdleService extends android.app.job.JobService {
    private static final java.lang.String PRUNE_JOB_NS = "usagestats_prune";
    private static final java.lang.String UPDATE_MAPPINGS_JOB_NS = "usagestats_mapping";
    private static final java.lang.String USER_ID_KEY = "user_id";

    static void schedulePruneJob(android.content.Context context, int userId) {
        android.content.ComponentName component = new android.content.ComponentName(context.getPackageName(), com.android.server.usage.UsageStatsIdleService.class.getName());
        android.os.PersistableBundle bundle = new android.os.PersistableBundle();
        bundle.putInt(USER_ID_KEY, userId);
        android.app.job.JobInfo pruneJob = new android.app.job.JobInfo.Builder(userId, component).setRequiresDeviceIdle(true).setExtras(bundle).setPersisted(true).build();
        scheduleJobInternal(context, pruneJob, PRUNE_JOB_NS, userId);
    }

    static void scheduleUpdateMappingsJob(android.content.Context context, int userId) {
        android.content.ComponentName component = new android.content.ComponentName(context.getPackageName(), com.android.server.usage.UsageStatsIdleService.class.getName());
        android.os.PersistableBundle bundle = new android.os.PersistableBundle();
        bundle.putInt(USER_ID_KEY, userId);
        android.app.job.JobInfo updateMappingsJob = new android.app.job.JobInfo.Builder(userId, component).setPersisted(true).setMinimumLatency(java.util.concurrent.TimeUnit.DAYS.toMillis(1L)).setOverrideDeadline(java.util.concurrent.TimeUnit.DAYS.toMillis(2L)).setExtras(bundle).build();
        scheduleJobInternal(context, updateMappingsJob, UPDATE_MAPPINGS_JOB_NS, userId);
    }

    private static void scheduleJobInternal(android.content.Context context, android.app.job.JobInfo jobInfo, java.lang.String namespace, int jobId) {
        android.app.job.JobScheduler jobScheduler = ((android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class)).forNamespace(namespace);
        android.app.job.JobInfo pendingJob = jobScheduler.getPendingJob(jobId);
        if (!jobInfo.equals(pendingJob)) {
            jobScheduler.cancel(jobId);
            jobScheduler.schedule(jobInfo);
        }
    }

    static void cancelPruneJob(android.content.Context context, int userId) {
        cancelJobInternal(context, PRUNE_JOB_NS, userId);
    }

    static void cancelUpdateMappingsJob(android.content.Context context, int userId) {
        cancelJobInternal(context, UPDATE_MAPPINGS_JOB_NS, userId);
    }

    private static void cancelJobInternal(android.content.Context context, java.lang.String namespace, int jobId) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        if (jobScheduler != null) {
            jobScheduler.forNamespace(namespace).cancel(jobId);
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        android.os.PersistableBundle bundle = params.getExtras();
        final int userId = bundle.getInt(USER_ID_KEY, -1);
        if (userId == -1) {
            return false;
        }
        android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.usage.UsageStatsIdleService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartJob$0(params, userId);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(android.app.job.JobParameters params, int userId) {
        android.app.usage.UsageStatsManagerInternal usageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        java.lang.String jobNs = params.getJobNamespace();
        if (UPDATE_MAPPINGS_JOB_NS.equals(jobNs)) {
            boolean jobFinished = usageStatsManagerInternal.updatePackageMappingsData(userId);
            jobFinished(params, !jobFinished);
        } else {
            boolean jobFinished2 = usageStatsManagerInternal.pruneUninstalledPackagesData(userId);
            jobFinished(params, !jobFinished2);
        }
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }
}
