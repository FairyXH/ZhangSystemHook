package com.android.server.selinux;

/* JADX INFO: loaded from: classes3.dex */
final class SelinuxAuditLogsJob {
    private static final java.lang.String TAG = "SelinuxAuditLogs";
    private final com.android.server.selinux.SelinuxAuditLogsCollector mAuditLogsCollector;
    private final java.util.concurrent.atomic.AtomicBoolean mIsRunning = new java.util.concurrent.atomic.AtomicBoolean(false);

    SelinuxAuditLogsJob(com.android.server.selinux.SelinuxAuditLogsCollector auditLogsCollector) {
        this.mAuditLogsCollector = auditLogsCollector;
    }

    void requestStop() {
        this.mAuditLogsCollector.mStopRequested.set(true);
    }

    boolean isRunning() {
        return this.mIsRunning.get();
    }

    public void start(android.app.job.JobService jobService, android.app.job.JobParameters params) {
        this.mAuditLogsCollector.mStopRequested.set(false);
        if (this.mIsRunning.get()) {
            android.util.Slog.i(TAG, "Selinux audit job is already running, ignore start request.");
            return;
        }
        this.mIsRunning.set(true);
        boolean done = this.mAuditLogsCollector.collect(com.android.server.selinux.SelinuxAuditLogsService.AUDITD_TAG_CODE);
        if (done) {
            jobService.jobFinished(params, false);
        }
        this.mIsRunning.set(false);
    }
}
