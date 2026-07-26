package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class DiskStatsLoggingService extends android.app.job.JobService {
    public static final java.lang.String DUMPSYS_CACHE_PATH = "/data/system/diskstats_cache.json";
    private static final int JOB_DISKSTATS_LOGGING = 1145656139;
    private static final java.lang.String TAG = "DiskStatsLogService";
    private static android.content.ComponentName sDiskStatsLoggingService = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.storage.DiskStatsLoggingService.class.getName());

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        if (!isCharging(this) || !isDumpsysTaskEnabled(getContentResolver())) {
            jobFinished(params, true);
            return false;
        }
        android.os.storage.VolumeInfo volume = getPackageManager().getPrimaryStorageCurrentVolume();
        if (volume == null) {
            return false;
        }
        com.android.server.storage.AppCollector collector = new com.android.server.storage.AppCollector(this, volume);
        int userId = android.os.UserHandle.myUserId();
        android.os.Environment.UserEnvironment environment = new android.os.Environment.UserEnvironment(userId);
        com.android.server.storage.DiskStatsLoggingService.LogRunnable task = new com.android.server.storage.DiskStatsLoggingService.LogRunnable();
        task.setDownloadsDirectory(environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS));
        task.setSystemSize(com.android.server.storage.FileCollector.getSystemSize(this));
        task.setLogOutputFile(new java.io.File(DUMPSYS_CACHE_PATH));
        task.setAppCollector(collector);
        task.setJobService(this, params);
        task.setContext(this);
        android.os.AsyncTask.execute(task);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }

    public static void schedule(android.content.Context context) {
        android.app.job.JobScheduler js = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        js.schedule(new android.app.job.JobInfo.Builder(JOB_DISKSTATS_LOGGING, sDiskStatsLoggingService).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(java.util.concurrent.TimeUnit.DAYS.toMillis(1L)).build());
    }

    private static boolean isCharging(android.content.Context context) {
        android.os.BatteryManager batteryManager = (android.os.BatteryManager) context.getSystemService("batterymanager");
        if (batteryManager != null) {
            return batteryManager.isCharging();
        }
        return false;
    }

    static boolean isDumpsysTaskEnabled(android.content.ContentResolver resolver) {
        return android.provider.Settings.Global.getInt(resolver, "enable_diskstats_logging", 1) != 0;
    }

    static class LogRunnable implements java.lang.Runnable {
        private static final long TIMEOUT_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(10);
        private com.android.server.storage.AppCollector mCollector;
        private android.content.Context mContext;
        private java.io.File mDownloadsDirectory;
        private android.app.job.JobService mJobService;
        private java.io.File mOutputFile;
        private android.app.job.JobParameters mParams;
        private long mSystemSize;

        LogRunnable() {
        }

        public void setDownloadsDirectory(java.io.File file) {
            this.mDownloadsDirectory = file;
        }

        public void setAppCollector(com.android.server.storage.AppCollector collector) {
            this.mCollector = collector;
        }

        public void setLogOutputFile(java.io.File file) {
            this.mOutputFile = file;
        }

        public void setSystemSize(long size) {
            this.mSystemSize = size;
        }

        public void setContext(android.content.Context context) {
            this.mContext = context;
        }

        public void setJobService(android.app.job.JobService jobService, android.app.job.JobParameters params) {
            this.mJobService = jobService;
            this.mParams = params;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                com.android.server.storage.FileCollector.MeasurementResult mainCategories = com.android.server.storage.FileCollector.getMeasurementResult(this.mContext);
                com.android.server.storage.FileCollector.MeasurementResult downloads = com.android.server.storage.FileCollector.getMeasurementResult(this.mDownloadsDirectory);
                boolean needsReschedule = true;
                java.util.List<android.content.pm.PackageStats> stats = this.mCollector.getPackageStats(TIMEOUT_MILLIS);
                if (stats == null) {
                    android.util.Log.w(com.android.server.storage.DiskStatsLoggingService.TAG, "Timed out while fetching package stats.");
                } else {
                    logToFile(mainCategories, downloads, stats, this.mSystemSize);
                    needsReschedule = false;
                }
                finishJob(needsReschedule);
            } catch (java.lang.IllegalStateException e) {
                android.util.Log.e(com.android.server.storage.DiskStatsLoggingService.TAG, "Error while measuring storage", e);
                finishJob(true);
            }
        }

        private void logToFile(com.android.server.storage.FileCollector.MeasurementResult mainCategories, com.android.server.storage.FileCollector.MeasurementResult downloads, java.util.List<android.content.pm.PackageStats> stats, long systemSize) {
            com.android.server.storage.DiskStatsFileLogger logger = new com.android.server.storage.DiskStatsFileLogger(mainCategories, downloads, stats, systemSize);
            try {
                this.mOutputFile.createNewFile();
                logger.dumpToFile(this.mOutputFile);
            } catch (java.io.IOException e) {
                android.util.Log.e(com.android.server.storage.DiskStatsLoggingService.TAG, "Exception while writing opportunistic disk file cache.", e);
            }
        }

        private void finishJob(boolean needsReschedule) {
            if (this.mJobService != null) {
                this.mJobService.jobFinished(this.mParams, needsReschedule);
            }
        }
    }
}
