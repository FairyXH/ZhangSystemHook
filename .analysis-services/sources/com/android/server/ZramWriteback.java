package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class ZramWriteback extends android.app.job.JobService {
    private static final java.lang.String BDEV_SYS = "/sys/block/zram%d/backing_dev";
    private static final boolean DEBUG = false;
    private static final java.lang.String FIRST_WB_DELAY_PROP = "ro.zram.first_wb_delay_mins";
    private static final java.lang.String FORCE_WRITEBACK_PROP = "zram.force_writeback";
    private static final java.lang.String IDLE_SYS = "/sys/block/zram%d/idle";
    private static final java.lang.String IDLE_SYS_ALL_PAGES = "all";
    private static final java.lang.String MARK_IDLE_DELAY_PROP = "ro.zram.mark_idle_delay_mins";
    private static final int MARK_IDLE_JOB_ID = 811;
    private static final int MAX_ZRAM_DEVICES = 256;
    private static final java.lang.String PERIODIC_WB_DELAY_PROP = "ro.zram.periodic_wb_delay_hours";
    private static final java.lang.String TAG = "ZramWriteback";
    private static final int WB_STATS_MAX_FILE_SIZE = 128;
    private static final java.lang.String WB_STATS_SYS = "/sys/block/zram%d/bd_stat";
    private static final java.lang.String WB_SYS = "/sys/block/zram%d/writeback";
    private static final java.lang.String WB_SYS_IDLE_PAGES = "idle";
    private static final int WRITEBACK_IDLE_JOB_ID = 812;
    private static final android.content.ComponentName sZramWriteback = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.ZramWriteback.class.getName());
    private static int sZramDeviceId = 0;

    private void markPagesAsIdle() {
        java.lang.String idlePath = java.lang.String.format(IDLE_SYS, java.lang.Integer.valueOf(sZramDeviceId));
        try {
            android.os.FileUtils.stringToFile(new java.io.File(idlePath), IDLE_SYS_ALL_PAGES);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write to " + idlePath);
        }
    }

    private void flushIdlePages() {
        java.lang.String wbPath = java.lang.String.format(WB_SYS, java.lang.Integer.valueOf(sZramDeviceId));
        try {
            android.os.FileUtils.stringToFile(new java.io.File(wbPath), WB_SYS_IDLE_PAGES);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write to " + wbPath);
        }
    }

    private int getWrittenPageCount() {
        java.lang.String wbStatsPath = java.lang.String.format(WB_STATS_SYS, java.lang.Integer.valueOf(sZramDeviceId));
        try {
            java.lang.String wbStats = android.os.FileUtils.readTextFile(new java.io.File(wbStatsPath), 128, "");
            return java.lang.Integer.parseInt(wbStats.trim().split("\\s+")[2], 10);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to read writeback stats from " + wbStatsPath);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markAndFlushPages() {
        int pageCount = getWrittenPageCount();
        flushIdlePages();
        markPagesAsIdle();
        if (pageCount != -1) {
            android.util.Slog.i(TAG, "Total pages written to disk is " + (getWrittenPageCount() - pageCount));
        }
    }

    private static boolean isWritebackEnabled() {
        try {
            java.lang.String backingDev = android.os.FileUtils.readTextFile(new java.io.File(java.lang.String.format(BDEV_SYS, java.lang.Integer.valueOf(sZramDeviceId))), 128, "");
            if ("none".equals(backingDev.trim())) {
                android.util.Slog.w(TAG, "Writeback device is not set");
                return false;
            }
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Writeback is not enabled on zram");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void schedNextWriteback(android.content.Context context) {
        int nextWbDelay = android.os.SystemProperties.getInt(PERIODIC_WB_DELAY_PROP, 24);
        boolean forceWb = android.os.SystemProperties.getBoolean(FORCE_WRITEBACK_PROP, false);
        android.app.job.JobScheduler js = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        js.schedule(new android.app.job.JobInfo.Builder(WRITEBACK_IDLE_JOB_ID, sZramWriteback).setMinimumLatency(java.util.concurrent.TimeUnit.HOURS.toMillis(nextWbDelay)).setRequiresDeviceIdle(!forceWb).build());
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.server.ZramWriteback$1] */
    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        if (!isWritebackEnabled()) {
            jobFinished(params, false);
            return false;
        }
        if (params.getJobId() == MARK_IDLE_JOB_ID) {
            markPagesAsIdle();
            jobFinished(params, false);
            return false;
        }
        new java.lang.Thread("ZramWriteback_WritebackIdlePages") { // from class: com.android.server.ZramWriteback.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                com.android.server.ZramWriteback.this.markAndFlushPages();
                com.android.server.ZramWriteback.schedNextWriteback(com.android.server.ZramWriteback.this);
                com.android.server.ZramWriteback.this.jobFinished(params, false);
            }
        }.start();
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        return false;
    }

    public static void scheduleZramWriteback(android.content.Context context) {
        int markIdleDelay = android.os.SystemProperties.getInt(MARK_IDLE_DELAY_PROP, 20);
        int firstWbDelay = android.os.SystemProperties.getInt(FIRST_WB_DELAY_PROP, 180);
        boolean forceWb = android.os.SystemProperties.getBoolean(FORCE_WRITEBACK_PROP, false);
        android.app.job.JobScheduler js = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        js.schedule(new android.app.job.JobInfo.Builder(MARK_IDLE_JOB_ID, sZramWriteback).setMinimumLatency(java.util.concurrent.TimeUnit.MINUTES.toMillis(markIdleDelay)).build());
        js.schedule(new android.app.job.JobInfo.Builder(WRITEBACK_IDLE_JOB_ID, sZramWriteback).setMinimumLatency(java.util.concurrent.TimeUnit.MINUTES.toMillis(firstWbDelay)).setRequiresDeviceIdle(!forceWb).build());
    }
}
