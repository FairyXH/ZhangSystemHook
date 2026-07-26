package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
public class ReportWatchlistJobService extends android.app.job.JobService {
    private static final boolean DEBUG = false;
    public static final int REPORT_WATCHLIST_RECORDS_JOB_ID = 882313;
    public static final long REPORT_WATCHLIST_RECORDS_PERIOD_MILLIS = java.util.concurrent.TimeUnit.HOURS.toMillis(12);
    private static final java.lang.String TAG = "WatchlistJobService";

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters jobParameters) {
        if (jobParameters.getJobId() != 882313) {
            return false;
        }
        new android.net.NetworkWatchlistManager(this).reportWatchlistIfNecessary();
        jobFinished(jobParameters, false);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters jobParameters) {
        return true;
    }

    public static void schedule(android.content.Context context) {
        android.app.job.JobScheduler scheduler = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        scheduler.schedule(new android.app.job.JobInfo.Builder(REPORT_WATCHLIST_RECORDS_JOB_ID, new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.net.watchlist.ReportWatchlistJobService.class)).setPeriodic(REPORT_WATCHLIST_RECORDS_PERIOD_MILLIS).setRequiresDeviceIdle(true).setRequiresBatteryNotLow(true).setPersisted(false).build());
    }
}
