package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class DynamicCodeLoggingService extends android.app.job.JobService {
    private static final int AUDIT_AVC = 1400;
    private static final int AUDIT_WATCHING_JOB_ID = 203142925;
    private static final java.lang.String AVC_PREFIX = "type=1400 ";
    private static final boolean DEBUG = false;
    private static final int IDLE_LOGGING_JOB_ID = 2030028;
    private static final java.lang.String TAG = com.android.server.pm.DynamicCodeLoggingService.class.getName();
    private static final long IDLE_LOGGING_PERIOD_MILLIS = java.util.concurrent.TimeUnit.DAYS.toMillis(1);
    private static final long AUDIT_WATCHING_PERIOD_MILLIS = java.util.concurrent.TimeUnit.HOURS.toMillis(2);
    private static final java.util.regex.Pattern EXECUTE_NATIVE_AUDIT_PATTERN = java.util.regex.Pattern.compile(".*\\bavc: +granted +\\{ execute(?:_no_trans|) \\} .*\\bpath=(?:\"([^\" ]*)\"|([0-9A-F]+)) .*\\bscontext=u:r:untrusted_app(?:_25|_27)?:.*\\btcontext=u:object_r:app_data_file:.*\\btclass=file\\b.*");
    private volatile boolean mIdleLoggingStopRequested = false;
    private volatile boolean mAuditWatchingStopRequested = false;

    public static void schedule(android.content.Context context) {
        android.content.ComponentName serviceName = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.pm.DynamicCodeLoggingService.class.getName());
        android.app.job.JobScheduler js = (android.app.job.JobScheduler) context.getSystemService("jobscheduler");
        js.schedule(new android.app.job.JobInfo.Builder(IDLE_LOGGING_JOB_ID, serviceName).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(IDLE_LOGGING_PERIOD_MILLIS).build());
        js.schedule(new android.app.job.JobInfo.Builder(AUDIT_WATCHING_JOB_ID, serviceName).setRequiresDeviceIdle(true).setRequiresBatteryNotLow(true).setPeriodic(AUDIT_WATCHING_PERIOD_MILLIS).build());
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(android.app.job.JobParameters params) {
        int jobId = params.getJobId();
        switch (jobId) {
            case IDLE_LOGGING_JOB_ID /* 2030028 */:
                this.mIdleLoggingStopRequested = false;
                new com.android.server.pm.DynamicCodeLoggingService.IdleLoggingThread(params).start();
                return true;
            case AUDIT_WATCHING_JOB_ID /* 203142925 */:
                this.mAuditWatchingStopRequested = false;
                new com.android.server.pm.DynamicCodeLoggingService.AuditWatchingThread(params).start();
                return true;
            default:
                return false;
        }
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(android.app.job.JobParameters params) {
        int jobId = params.getJobId();
        switch (jobId) {
            case IDLE_LOGGING_JOB_ID /* 2030028 */:
                this.mIdleLoggingStopRequested = true;
                break;
            case AUDIT_WATCHING_JOB_ID /* 203142925 */:
                this.mAuditWatchingStopRequested = true;
                break;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.pm.dex.DynamicCodeLogger getDynamicCodeLogger() {
        return ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getDynamicCodeLogger();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void syncDataFromArtService(com.android.server.pm.dex.DynamicCodeLogger dynamicCodeLogger) {
        com.android.server.art.DexUseManagerLocal dexUseManagerLocal = com.android.server.pm.DexOptHelper.getDexUseManagerLocal();
        if (dexUseManagerLocal == null) {
            return;
        }
        com.android.server.pm.PackageManagerLocal packageManagerLocal = (com.android.server.pm.PackageManagerLocal) java.util.Objects.requireNonNull((com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.pm.PackageManagerLocal.class));
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            for (java.lang.String owningPackageName : snapshot.getPackageStates().keySet()) {
                for (com.android.server.art.model.DexContainerFileUseInfo info : dexUseManagerLocal.getSecondaryDexContainerFileUseInfo(owningPackageName)) {
                    for (java.lang.String loadingPackageName : info.getLoadingPackages()) {
                        dynamicCodeLogger.recordDex(info.getUserHandle().getIdentifier(), info.getDexContainerFile(), owningPackageName, loadingPackageName);
                    }
                }
            }
            if (snapshot != null) {
                snapshot.close();
            }
        } catch (java.lang.Throwable th) {
            if (snapshot != null) {
                try {
                    snapshot.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private class IdleLoggingThread extends java.lang.Thread {
        private final android.app.job.JobParameters mParams;

        IdleLoggingThread(android.app.job.JobParameters params) {
            super("DynamicCodeLoggingService_IdleLoggingJob");
            this.mParams = params;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            com.android.server.pm.dex.DynamicCodeLogger dynamicCodeLogger = com.android.server.pm.DynamicCodeLoggingService.getDynamicCodeLogger();
            com.android.server.pm.DynamicCodeLoggingService.syncDataFromArtService(dynamicCodeLogger);
            for (java.lang.String packageName : dynamicCodeLogger.getAllPackagesWithDynamicCodeLoading()) {
                if (com.android.server.pm.DynamicCodeLoggingService.this.mIdleLoggingStopRequested) {
                    android.util.Log.w(com.android.server.pm.DynamicCodeLoggingService.TAG, "Stopping IdleLoggingJob run at scheduler request");
                    return;
                }
                dynamicCodeLogger.logDynamicCodeLoading(packageName);
            }
            com.android.server.pm.DynamicCodeLoggingService.this.jobFinished(this.mParams, false);
        }
    }

    private class AuditWatchingThread extends java.lang.Thread {
        private final android.app.job.JobParameters mParams;

        AuditWatchingThread(android.app.job.JobParameters params) {
            super("DynamicCodeLoggingService_AuditWatchingJob");
            this.mParams = params;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (processAuditEvents()) {
                com.android.server.pm.DynamicCodeLoggingService.this.jobFinished(this.mParams, false);
            }
        }

        private boolean processAuditEvents() {
            try {
                int[] tags = {android.util.EventLog.getTagCode("auditd")};
                if (tags[0] == -1) {
                    return true;
                }
                com.android.server.pm.dex.DynamicCodeLogger dynamicCodeLogger = com.android.server.pm.DynamicCodeLoggingService.getDynamicCodeLogger();
                java.util.List<android.util.EventLog.Event> events = new java.util.ArrayList<>();
                android.util.EventLog.readEvents(tags, events);
                java.util.regex.Matcher matcher = com.android.server.pm.DynamicCodeLoggingService.EXECUTE_NATIVE_AUDIT_PATTERN.matcher("");
                for (int i = 0; i < events.size(); i++) {
                    if (com.android.server.pm.DynamicCodeLoggingService.this.mAuditWatchingStopRequested) {
                        android.util.Log.w(com.android.server.pm.DynamicCodeLoggingService.TAG, "Stopping AuditWatchingJob run at scheduler request");
                        return false;
                    }
                    android.util.EventLog.Event event = events.get(i);
                    int uid = event.getUid();
                    if (android.os.Process.isApplicationUid(uid)) {
                        java.lang.Object data = event.getData();
                        if (data instanceof java.lang.String) {
                            java.lang.String message = (java.lang.String) data;
                            if (message.startsWith(com.android.server.pm.DynamicCodeLoggingService.AVC_PREFIX)) {
                                matcher.reset(message);
                                if (matcher.matches()) {
                                    java.lang.String path = matcher.group(1);
                                    if (path == null) {
                                        path = com.android.server.pm.DynamicCodeLoggingService.unhex(matcher.group(2));
                                    }
                                    dynamicCodeLogger.recordNative(uid, path);
                                }
                            }
                        }
                    }
                }
                return true;
            } catch (java.lang.Exception e) {
                android.util.Log.e(com.android.server.pm.DynamicCodeLoggingService.TAG, "AuditWatchingJob failed", e);
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String unhex(java.lang.String hexEncodedPath) {
        if (hexEncodedPath == null || hexEncodedPath.length() == 0) {
            return "";
        }
        byte[] bytes = libcore.util.HexEncoding.decode(hexEncodedPath, false);
        return new java.lang.String(bytes);
    }
}
