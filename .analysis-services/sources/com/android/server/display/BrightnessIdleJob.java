package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessIdleJob extends android.app.job.JobService {
    private static final int JOB_ID = 3923512;

    public static void scheduleJob(android.content.Context context) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        android.app.job.JobInfo pending = jobScheduler.getPendingJob(JOB_ID);
        android.app.job.JobInfo jobInfo = new android.app.job.JobInfo.Builder(JOB_ID, new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.display.BrightnessIdleJob.class)).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(java.util.concurrent.TimeUnit.HOURS.toMillis(24L)).build();
        if (pending != null && !pending.equals(jobInfo)) {
            jobScheduler.cancel(JOB_ID);
            pending = null;
        }
        if (pending == null) {
            jobScheduler.schedule(jobInfo);
        }
    }

    public static void cancelJob(android.content.Context context) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        jobScheduler.cancel(JOB_ID);
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        if (com.android.server.display.BrightnessTracker.DEBUG) {
            android.util.Slog.d("BrightnessTracker", "Scheduled write of brightness events");
        }
        android.hardware.display.DisplayManagerInternal dmi = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        dmi.persistBrightnessTrackerState();
        return false;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }
}
