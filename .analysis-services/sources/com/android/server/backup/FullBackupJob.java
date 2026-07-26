package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class FullBackupJob extends android.app.job.JobService {
    public static final int MAX_JOB_ID = 52419896;
    public static final int MIN_JOB_ID = 52418896;
    private static final java.lang.String USER_ID_EXTRA_KEY = "userId";
    private static android.content.ComponentName sIdleService = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.backup.FullBackupJob.class.getName());
    private final android.util.SparseArray<android.app.job.JobParameters> mParamsForUser = new android.util.SparseArray<>();

    public static void schedule(int userId, android.content.Context ctx, long minDelay, com.android.server.backup.UserBackupManagerService userBackupManagerService) {
        if (userBackupManagerService.isFrameworkSchedulingEnabled()) {
            android.app.job.JobScheduler js = (android.app.job.JobScheduler) ctx.getSystemService("jobscheduler");
            android.app.job.JobInfo.Builder builder = new android.app.job.JobInfo.Builder(getJobIdForUserId(userId), sIdleService);
            com.android.server.backup.BackupManagerConstants constants = userBackupManagerService.getConstants();
            synchronized (constants) {
                builder.setRequiredNetworkType(constants.getFullBackupRequiredNetworkType()).setRequiresCharging(constants.getFullBackupRequireCharging());
            }
            if (minDelay > 0) {
                builder.setMinimumLatency(minDelay);
            }
            if (!ctx.getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
                builder.setRequiresDeviceIdle(true);
            }
            android.os.Bundle extraInfo = new android.os.Bundle();
            extraInfo.putInt("userId", userId);
            builder.setTransientExtras(extraInfo);
            js.schedule(builder.build());
        }
    }

    public static void cancel(int userId, android.content.Context ctx) {
        android.app.job.JobScheduler js = (android.app.job.JobScheduler) ctx.getSystemService("jobscheduler");
        js.cancel(getJobIdForUserId(userId));
    }

    public void finishBackupPass(int userId) {
        synchronized (this.mParamsForUser) {
            android.app.job.JobParameters jobParameters = this.mParamsForUser.get(userId);
            if (jobParameters != null) {
                jobFinished(jobParameters, false);
                this.mParamsForUser.remove(userId);
            }
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        int userId = params.getTransientExtras().getInt("userId");
        synchronized (this.mParamsForUser) {
            this.mParamsForUser.put(userId, params);
        }
        com.android.server.backup.BackupManagerService service = com.android.server.backup.BackupManagerService.getInstance();
        return service.beginFullBackup(userId, this);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        int userId = params.getTransientExtras().getInt("userId");
        synchronized (this.mParamsForUser) {
            if (this.mParamsForUser.removeReturnOld(userId) == null) {
                return false;
            }
            com.android.server.backup.BackupManagerService service = com.android.server.backup.BackupManagerService.getInstance();
            service.endFullBackup(userId);
            return false;
        }
    }

    static int getJobIdForUserId(int userId) {
        return com.android.server.backup.JobIdManager.getJobIdForUserId(52418896, MAX_JOB_ID, userId);
    }
}
