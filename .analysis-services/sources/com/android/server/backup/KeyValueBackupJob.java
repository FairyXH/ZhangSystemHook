package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class KeyValueBackupJob extends android.app.job.JobService {
    private static final long MAX_DEFERRAL = 86400000;
    public static final int MAX_JOB_ID = 52418896;
    public static final int MIN_JOB_ID = 52417896;
    private static final java.lang.String TAG = "KeyValueBackupJob";
    private static final java.lang.String USER_ID_EXTRA_KEY = "userId";
    private static android.content.ComponentName sKeyValueJobService = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.backup.KeyValueBackupJob.class.getName());
    private static final android.util.SparseBooleanArray sScheduledForUserId = new android.util.SparseBooleanArray();
    private static final android.util.SparseLongArray sNextScheduledForUserId = new android.util.SparseLongArray();

    public static void schedule(int userId, android.content.Context ctx, com.android.server.backup.UserBackupManagerService userBackupManagerService) {
        schedule(userId, ctx, 0L, userBackupManagerService);
    }

    public static void schedule(int userId, android.content.Context ctx, long delay, com.android.server.backup.UserBackupManagerService userBackupManagerService) throws java.lang.Throwable {
        long interval;
        long fuzz;
        int networkType;
        boolean needsCharging;
        long delay2;
        synchronized (com.android.server.backup.KeyValueBackupJob.class) {
            try {
                try {
                    try {
                        if (sScheduledForUserId.get(userId) || !userBackupManagerService.isFrameworkSchedulingEnabled()) {
                            return;
                        }
                        com.android.server.backup.BackupManagerConstants constants = userBackupManagerService.getConstants();
                        synchronized (constants) {
                            try {
                                interval = constants.getKeyValueBackupIntervalMilliseconds();
                                fuzz = constants.getKeyValueBackupFuzzMilliseconds();
                                networkType = constants.getKeyValueBackupRequiredNetworkType();
                                needsCharging = constants.getKeyValueBackupRequireCharging();
                            } catch (java.lang.Throwable th) {
                                th = th;
                                while (true) {
                                    try {
                                        throw th;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                }
                            }
                        }
                        if (delay > 0) {
                            delay2 = delay;
                        } else {
                            delay2 = ((long) new java.util.Random().nextInt((int) fuzz)) + interval;
                        }
                        try {
                            android.util.Slog.v(TAG, "Scheduling k/v pass in " + ((delay2 / 1000) / 60) + " minutes");
                            android.app.job.JobInfo.Builder builder = new android.app.job.JobInfo.Builder(getJobIdForUserId(userId), sKeyValueJobService).setMinimumLatency(delay2).setRequiredNetworkType(networkType).setRequiresCharging(needsCharging).setOverrideDeadline(86400000L);
                            android.os.Bundle extraInfo = new android.os.Bundle();
                            extraInfo.putInt("userId", userId);
                            builder.setTransientExtras(extraInfo);
                            android.app.job.JobScheduler js = (android.app.job.JobScheduler) ctx.getSystemService("jobscheduler");
                            js.schedule(builder.build());
                            sScheduledForUserId.put(userId, true);
                            android.util.SparseLongArray sparseLongArray = sNextScheduledForUserId;
                            long interval2 = java.lang.System.currentTimeMillis() + delay2;
                            sparseLongArray.put(userId, interval2);
                            return;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
            throw th;
        }
    }

    public static void cancel(int userId, android.content.Context ctx) {
        synchronized (com.android.server.backup.KeyValueBackupJob.class) {
            android.app.job.JobScheduler js = (android.app.job.JobScheduler) ctx.getSystemService("jobscheduler");
            js.cancel(getJobIdForUserId(userId));
            clearScheduledForUserId(userId);
        }
    }

    public static long nextScheduled(int userId) {
        long j;
        synchronized (com.android.server.backup.KeyValueBackupJob.class) {
            j = sNextScheduledForUserId.get(userId);
        }
        return j;
    }

    public static boolean isScheduled(int userId) {
        boolean z;
        synchronized (com.android.server.backup.KeyValueBackupJob.class) {
            z = sScheduledForUserId.get(userId);
        }
        return z;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        int userId = params.getTransientExtras().getInt("userId");
        synchronized (com.android.server.backup.KeyValueBackupJob.class) {
            clearScheduledForUserId(userId);
        }
        com.android.server.backup.BackupManagerService service = com.android.server.backup.BackupManagerService.getInstance();
        try {
            service.backupNowForUser(userId);
            return false;
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }

    private static void clearScheduledForUserId(int userId) {
        sScheduledForUserId.delete(userId);
        sNextScheduledForUserId.delete(userId);
    }

    static int getJobIdForUserId(int userId) {
        return com.android.server.backup.JobIdManager.getJobIdForUserId(MIN_JOB_ID, 52418896, userId);
    }
}
