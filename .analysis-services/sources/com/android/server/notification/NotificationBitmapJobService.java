package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationBitmapJobService extends android.app.job.JobService {
    static final int BASE_JOB_ID = 290381858;
    static final java.lang.String TAG = "NotificationBitmapJob";

    static void scheduleJob(android.content.Context context) {
        if (context != null) {
            try {
                android.app.job.JobScheduler jobScheduler = ((android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class)).forNamespace(TAG);
                android.content.ComponentName component = new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.notification.NotificationBitmapJobService.class);
                android.app.job.JobInfo jobInfo = new android.app.job.JobInfo.Builder(BASE_JOB_ID, component).setRequiresDeviceIdle(true).setMinimumLatency(getRunAfterMs()).build();
                int result = jobScheduler.schedule(jobInfo);
                if (result != 1) {
                    android.util.Slog.e(TAG, "Failed to schedule bitmap removal job");
                }
            } catch (java.lang.Throwable e) {
                android.util.Slog.wtf(TAG, "Failed bitmap removal job", e);
            }
        }
    }

    private static long getRunAfterMs() {
        java.time.ZoneId zoneId = java.time.ZoneId.systemDefault();
        java.time.ZonedDateTime now = java.time.Instant.now().atZone(zoneId);
        java.time.LocalDate today = now.toLocalDate();
        java.time.LocalTime twoAM = java.time.LocalTime.of(2, 0);
        java.time.ZonedDateTime today2AM = java.time.ZonedDateTime.of(today, twoAM, zoneId);
        java.time.ZonedDateTime tomorrow2AM = today2AM.plusDays(1L);
        return getTimeUntilRemoval(now, today2AM, tomorrow2AM);
    }

    static long getTimeUntilRemoval(java.time.ZonedDateTime now, java.time.ZonedDateTime today2AM, java.time.ZonedDateTime tomorrow2AM) {
        if (java.time.Duration.between(now, today2AM).isNegative()) {
            return java.time.Duration.between(now, tomorrow2AM).toMillis();
        }
        return java.time.Duration.between(now, today2AM).toMillis();
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationBitmapJobService$$ExternalSyntheticLambda0
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
        try {
            nmInternal.removeBitmaps();
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "remove Bitmaps fail", e);
        }
        scheduleJob(this);
        jobFinished(params, false);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }

    @Override // android.app.Service, android.content.ContextWrapper
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
    }
}
