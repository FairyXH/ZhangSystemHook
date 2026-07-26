package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ReviewNotificationPermissionsJobService extends android.app.job.JobService {
    protected static final int JOB_ID = 225373531;
    public static final java.lang.String TAG = "ReviewNotificationPermissionsJobService";

    public static void scheduleJob(android.content.Context context, long rescheduleTimeMillis) {
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
        android.content.ComponentName component = new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.notification.ReviewNotificationPermissionsJobService.class);
        android.app.job.JobInfo newJob = new android.app.job.JobInfo.Builder(JOB_ID, component).setPersisted(true).setMinimumLatency(rescheduleTimeMillis).build();
        jobScheduler.schedule(newJob);
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        com.android.server.notification.NotificationManagerInternal nmi = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
        nmi.sendReviewPermissionsNotification();
        return false;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return true;
    }
}
