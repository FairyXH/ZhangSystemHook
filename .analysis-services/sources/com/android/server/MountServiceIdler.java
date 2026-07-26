package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class MountServiceIdler extends android.app.job.JobService {
    private static final java.lang.String SUPER_POWERSAVE_MODE_STATE = "super_powersave_mode_state";
    private static final java.lang.String TAG = "MountServiceIdler";
    private java.lang.Runnable mFinishCallback = new java.lang.Runnable() { // from class: com.android.server.MountServiceIdler.1
        @Override // java.lang.Runnable
        public void run() {
            android.util.Slog.i(com.android.server.MountServiceIdler.TAG, "Got mount service completion callback");
            synchronized (com.android.server.MountServiceIdler.this.mFinishCallback) {
                if (com.android.server.MountServiceIdler.this.mStarted) {
                    com.android.server.MountServiceIdler.this.jobFinished(com.android.server.MountServiceIdler.this.mJobParams, false);
                    com.android.server.MountServiceIdler.this.mStarted = false;
                }
            }
            com.android.server.MountServiceIdler.scheduleIdlePass(com.android.server.MountServiceIdler.this);
        }
    };
    private android.app.job.JobParameters mJobParams;
    private boolean mStarted;
    private static android.content.ComponentName sIdleService = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.MountServiceIdler.class.getName());
    private static int MOUNT_JOB_ID = 808;

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        try {
            android.app.ActivityManager.getService().performIdleMaintenance();
        } catch (android.os.RemoteException e) {
        }
        this.mJobParams = params;
        com.android.server.StorageManagerService ms = com.android.server.StorageManagerService.sSelf;
        boolean mSuperPowerSaveMode = android.provider.Settings.System.getIntForUser(getApplicationContext().getContentResolver(), SUPER_POWERSAVE_MODE_STATE, 0, 0) == 1;
        if (ms != null && !mSuperPowerSaveMode) {
            synchronized (this.mFinishCallback) {
                this.mStarted = true;
            }
            ms.runIdleMaint(this.mFinishCallback);
        }
        return ms != null;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        com.android.server.StorageManagerService ms = com.android.server.StorageManagerService.sSelf;
        if (ms != null) {
            ms.abortIdleMaint(this.mFinishCallback);
            synchronized (this.mFinishCallback) {
                this.mStarted = false;
            }
        }
        return false;
    }

    public static void scheduleIdlePass(android.content.Context context) {
        long nextScheduleTime;
        android.app.job.JobScheduler tm = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        long today3AM = offsetFromTodayMidnight(0, 3).getTimeInMillis();
        long today4AM = offsetFromTodayMidnight(0, 4).getTimeInMillis();
        long tomorrow3AM = offsetFromTodayMidnight(1, 3).getTimeInMillis();
        if (java.lang.System.currentTimeMillis() > today3AM && java.lang.System.currentTimeMillis() < today4AM) {
            nextScheduleTime = java.util.concurrent.TimeUnit.SECONDS.toMillis(10L);
        } else {
            long nextScheduleTime2 = java.lang.System.currentTimeMillis();
            nextScheduleTime = tomorrow3AM - nextScheduleTime2;
        }
        android.app.job.JobInfo.Builder builder = new android.app.job.JobInfo.Builder(MOUNT_JOB_ID, sIdleService);
        builder.setRequiresDeviceIdle(true);
        builder.setRequiresBatteryNotLow(true);
        builder.setRequiresCharging(true);
        builder.setMinimumLatency(nextScheduleTime);
        tm.schedule(builder.build());
    }

    private static java.util.Calendar offsetFromTodayMidnight(int nDays, int nHours) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
        calendar.set(11, nHours);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(5, nDays);
        return calendar;
    }
}
