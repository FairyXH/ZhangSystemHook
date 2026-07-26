package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationHistoryJobService extends android.app.job.JobService {
    static final int BASE_JOB_ID = 237039804;
    private static final long JOB_RUN_INTERVAL = java.util.concurrent.TimeUnit.MINUTES.toMillis(20);
    private static final java.lang.String TAG = "NotificationHistoryJob";
    private android.os.CancellationSignal mSignal;

    static void scheduleJob(android.content.Context context) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        if (jobScheduler.getPendingJob(BASE_JOB_ID) == null) {
            android.content.ComponentName component = new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.notification.NotificationHistoryJobService.class);
            android.app.job.JobInfo newJob = new android.app.job.JobInfo.Builder(BASE_JOB_ID, component).setRequiresDeviceIdle(false).setPeriodic(JOB_RUN_INTERVAL).build();
            if (jobScheduler.schedule(newJob) != 1) {
                android.util.Slog.w(TAG, "Failed to schedule history cleanup job");
            }
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        this.mSignal = new android.os.CancellationSignal();
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationHistoryJobService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartJob$0(params);
            }
        }).start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(android.app.job.JobParameters params) {
        com.android.server.notification.NotificationManagerInternal nmInternal = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
        nmInternal.cleanupHistoryFiles();
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

    @Override // android.app.Service, android.content.ContextWrapper
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
    }
}
