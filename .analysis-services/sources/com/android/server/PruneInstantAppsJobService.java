package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class PruneInstantAppsJobService extends android.app.job.JobService {
    private static final boolean DEBUG = false;
    private static final int JOB_ID = 765123;
    private static final long PRUNE_INSTANT_APPS_PERIOD_MILLIS = java.util.concurrent.TimeUnit.DAYS.toMillis(1);

    public static void schedule(android.content.Context context) {
        android.app.job.JobInfo pruneJob = new android.app.job.JobInfo.Builder(JOB_ID, new android.content.ComponentName(context.getPackageName(), com.android.server.PruneInstantAppsJobService.class.getName())).setRequiresDeviceIdle(true).setPeriodic(PRUNE_INSTANT_APPS_PERIOD_MILLIS).build();
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        jobScheduler.schedule(pruneJob);
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.PruneInstantAppsJobService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartJob$0(params);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(android.app.job.JobParameters params) {
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        packageManagerInternal.pruneInstantApps();
        jobFinished(params, false);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }
}
