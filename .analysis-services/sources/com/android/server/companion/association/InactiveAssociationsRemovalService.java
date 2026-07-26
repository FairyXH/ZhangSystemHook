package com.android.server.companion.association;

/* JADX INFO: loaded from: classes.dex */
public class InactiveAssociationsRemovalService extends android.app.job.JobService {
    private static final int JOB_ID = 1;
    private static final java.lang.String JOB_NAMESPACE = "companion";
    private static final long ONE_DAY_INTERVAL = java.util.concurrent.TimeUnit.DAYS.toMillis(1);
    private static final java.lang.String TAG = "CDM_InactiveAssociationsRemovalService";

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        android.util.Slog.i(TAG, "Execute the Association Removal job");
        ((com.android.server.companion.CompanionDeviceManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.companion.CompanionDeviceManagerServiceInternal.class)).removeInactiveSelfManagedAssociations();
        jobFinished(params, false);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        android.util.Slog.i(TAG, "Association removal job stopped; id=" + params.getJobId() + ", reason=" + android.app.job.JobParameters.getInternalReasonCodeDescription(params.getInternalStopReasonCode()));
        return false;
    }

    public static void schedule(android.content.Context context) {
        android.util.Slog.i(TAG, "Scheduling the Association Removal job");
        android.app.job.JobScheduler jobScheduler = ((android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class)).forNamespace(JOB_NAMESPACE);
        android.app.job.JobInfo job = new android.app.job.JobInfo.Builder(1, new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.companion.association.InactiveAssociationsRemovalService.class)).setRequiresCharging(true).setRequiresDeviceIdle(true).setPeriodic(ONE_DAY_INTERVAL).build();
        jobScheduler.schedule(job);
    }
}
