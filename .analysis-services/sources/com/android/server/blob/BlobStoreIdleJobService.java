package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
public class BlobStoreIdleJobService extends android.app.job.JobService {
    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreIdleJobService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartJob$0(params);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(android.app.job.JobParameters params) {
        com.android.server.blob.BlobStoreManagerInternal blobStoreManagerInternal = (com.android.server.blob.BlobStoreManagerInternal) com.android.server.LocalServices.getService(com.android.server.blob.BlobStoreManagerInternal.class);
        blobStoreManagerInternal.onIdleMaintenance();
        jobFinished(params, false);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Idle maintenance job is stopped; id=" + params.getJobId() + ", reason=" + android.app.job.JobParameters.getInternalReasonCodeDescription(params.getInternalStopReasonCode()));
        return false;
    }

    static void schedule(android.content.Context context) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        android.app.job.JobInfo job = new android.app.job.JobInfo.Builder(com.android.server.blob.BlobStoreConfig.IDLE_JOB_ID, new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.blob.BlobStoreIdleJobService.class)).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(com.android.server.blob.BlobStoreConfig.getIdleJobPeriodMs()).build();
        jobScheduler.schedule(job);
        if (com.android.server.blob.BlobStoreConfig.LOGV) {
            android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Scheduling the idle maintenance job");
        }
    }
}
