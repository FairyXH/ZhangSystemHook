package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class DataMaintenanceService extends android.app.job.JobService {
    private static final int BASE_JOB_ID = 204561367;
    private static final long JOB_RUN_INTERVAL = java.util.concurrent.TimeUnit.HOURS.toMillis(24);
    private android.os.CancellationSignal mSignal;

    static void scheduleJob(android.content.Context context, int userId) {
        int jobId = getJobId(userId);
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        if (jobScheduler.getPendingJob(jobId) == null) {
            android.content.ComponentName component = new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.people.data.DataMaintenanceService.class);
            android.app.job.JobInfo newJob = new android.app.job.JobInfo.Builder(jobId, component).setRequiresDeviceIdle(true).setPeriodic(JOB_RUN_INTERVAL).build();
            jobScheduler.schedule(newJob);
        }
    }

    static void cancelJob(android.content.Context context, int userId) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        jobScheduler.cancel(getJobId(userId));
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        final int userId = getUserId(params.getJobId());
        this.mSignal = new android.os.CancellationSignal();
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.people.data.DataMaintenanceService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartJob$0(userId, params);
            }
        }).start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(int userId, android.app.job.JobParameters params) {
        com.android.server.people.PeopleServiceInternal peopleServiceInternal = (com.android.server.people.PeopleServiceInternal) com.android.server.LocalServices.getService(com.android.server.people.PeopleServiceInternal.class);
        peopleServiceInternal.pruneDataForUser(userId, this.mSignal);
        jobFinished(params, this.mSignal.isCanceled());
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        if (this.mSignal != null) {
            this.mSignal.cancel();
            return false;
        }
        return false;
    }

    private static int getJobId(int userId) {
        return BASE_JOB_ID + userId;
    }

    private static int getUserId(int jobId) {
        return jobId - BASE_JOB_ID;
    }
}
