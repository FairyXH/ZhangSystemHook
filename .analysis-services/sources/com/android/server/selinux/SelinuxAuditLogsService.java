package com.android.server.selinux;

/* JADX INFO: loaded from: classes3.dex */
public class SelinuxAuditLogsService extends android.app.job.JobService {
    private static final java.lang.String CONFIG_SELINUX_AUDIT_JOB_FREQUENCY_HOURS = "selinux_audit_job_frequency_hours";
    private static final java.lang.String CONFIG_SELINUX_ENABLE_AUDIT_JOB = "selinux_enable_audit_job";
    private static final int MAX_PERMITS_CAP_DEFAULT = 50000;
    private static final int SELINUX_AUDIT_JOB_ID = 25327386;
    private static final java.lang.String SELINUX_AUDIT_NAMESPACE = "SelinuxAuditLogsNamespace";
    private static final java.lang.String TAG = "SelinuxAuditLogs";
    static final int AUDITD_TAG_CODE = android.util.EventLog.getTagCode("auditd");
    private static final android.content.ComponentName SELINUX_AUDIT_JOB_COMPONENT = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.selinux.SelinuxAuditLogsService.class.getName());
    private static final java.util.concurrent.ExecutorService EXECUTOR_SERVICE = java.util.concurrent.Executors.newSingleThreadExecutor();
    private static final java.time.Duration RATE_LIMITER_WINDOW = java.time.Duration.ofMillis(10);
    private static final java.lang.String CONFIG_SELINUX_AUDIT_CAP = "selinux_audit_cap";
    private static final com.android.server.selinux.QuotaLimiter QUOTA_LIMITER = new com.android.server.selinux.QuotaLimiter(android.provider.DeviceConfig.getInt("adservices", CONFIG_SELINUX_AUDIT_CAP, 50000));
    private static final com.android.server.selinux.SelinuxAuditLogsJob LOGS_COLLECTOR_JOB = new com.android.server.selinux.SelinuxAuditLogsJob(new com.android.server.selinux.SelinuxAuditLogsCollector(new com.android.server.selinux.RateLimiter(RATE_LIMITER_WINDOW), QUOTA_LIMITER));

    public static void schedule(android.content.Context context) {
        if (!com.android.sdksandbox.flags.Flags.selinuxSdkSandboxAudit()) {
            android.util.Slog.d(TAG, "SelinuxAuditLogsService not enabled");
        } else {
            if (AUDITD_TAG_CODE == -1) {
                android.util.Slog.e(TAG, "auditd is not a registered tag on this system");
                return;
            }
            com.android.server.selinux.SelinuxAuditLogsService.LogsCollectorJobScheduler propertiesListener = new com.android.server.selinux.SelinuxAuditLogsService.LogsCollectorJobScheduler(((android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class)).forNamespace(SELINUX_AUDIT_NAMESPACE));
            propertiesListener.schedule();
            android.provider.DeviceConfig.addOnPropertiesChangedListener("adservices", context.getMainExecutor(), propertiesListener);
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final android.app.job.JobParameters params) {
        if (params.getJobId() != SELINUX_AUDIT_JOB_ID) {
            android.util.Slog.e(TAG, "The job id does not match the expected selinux job id.");
            return false;
        }
        if (!com.android.sdksandbox.flags.Flags.selinuxSdkSandboxAudit()) {
            android.util.Slog.i(TAG, "Selinux audit job disabled.");
            return false;
        }
        EXECUTOR_SERVICE.execute(new java.lang.Runnable() { // from class: com.android.server.selinux.SelinuxAuditLogsService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartJob$0(params);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(android.app.job.JobParameters params) {
        LOGS_COLLECTOR_JOB.start(this, params);
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        if (params.getJobId() != SELINUX_AUDIT_JOB_ID || !LOGS_COLLECTOR_JOB.isRunning()) {
            return false;
        }
        LOGS_COLLECTOR_JOB.requestStop();
        return true;
    }

    private static final class LogsCollectorJobScheduler implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private final android.app.job.JobScheduler mJobScheduler;

        private LogsCollectorJobScheduler(android.app.job.JobScheduler jobScheduler) {
            this.mJobScheduler = jobScheduler;
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties changedProperties) {
            java.util.Set<java.lang.String> keyset = changedProperties.getKeyset();
            if (keyset.contains(com.android.server.selinux.SelinuxAuditLogsService.CONFIG_SELINUX_AUDIT_CAP)) {
                com.android.server.selinux.SelinuxAuditLogsService.QUOTA_LIMITER.setMaxPermits(changedProperties.getInt(com.android.server.selinux.SelinuxAuditLogsService.CONFIG_SELINUX_AUDIT_CAP, 50000));
            }
            if (keyset.contains(com.android.server.selinux.SelinuxAuditLogsService.CONFIG_SELINUX_ENABLE_AUDIT_JOB)) {
                boolean enabled = changedProperties.getBoolean(com.android.server.selinux.SelinuxAuditLogsService.CONFIG_SELINUX_ENABLE_AUDIT_JOB, false);
                if (enabled) {
                    schedule();
                    return;
                } else {
                    this.mJobScheduler.cancel(com.android.server.selinux.SelinuxAuditLogsService.SELINUX_AUDIT_JOB_ID);
                    return;
                }
            }
            if (keyset.contains(com.android.server.selinux.SelinuxAuditLogsService.CONFIG_SELINUX_AUDIT_JOB_FREQUENCY_HOURS)) {
                schedule();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void schedule() {
            long frequencyMillis = java.util.concurrent.TimeUnit.HOURS.toMillis(android.provider.DeviceConfig.getInt("adservices", com.android.server.selinux.SelinuxAuditLogsService.CONFIG_SELINUX_AUDIT_JOB_FREQUENCY_HOURS, 24));
            if (this.mJobScheduler.schedule(new android.app.job.JobInfo.Builder(com.android.server.selinux.SelinuxAuditLogsService.SELINUX_AUDIT_JOB_ID, com.android.server.selinux.SelinuxAuditLogsService.SELINUX_AUDIT_JOB_COMPONENT).setPeriodic(frequencyMillis).setRequiresDeviceIdle(true).setRequiresBatteryNotLow(true).build()) == 0) {
                android.util.Slog.e(com.android.server.selinux.SelinuxAuditLogsService.TAG, "SelinuxAuditLogsService could not be scheduled.");
            } else {
                android.util.Slog.d(com.android.server.selinux.SelinuxAuditLogsService.TAG, "SelinuxAuditLogsService scheduled successfully.");
            }
        }
    }
}
