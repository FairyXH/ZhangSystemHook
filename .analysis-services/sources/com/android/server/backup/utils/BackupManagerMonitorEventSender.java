package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class BackupManagerMonitorEventSender {
    private static final int AGENT_LOGGER_RESULTS_TIMEOUT_MILLIS = 500;
    private final com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils mBackupManagerMonitorDumpsysUtils;
    private android.app.backup.IBackupManagerMonitor mMonitor;

    public BackupManagerMonitorEventSender(android.app.backup.IBackupManagerMonitor monitor) {
        this.mMonitor = monitor;
        this.mBackupManagerMonitorDumpsysUtils = new com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils();
    }

    BackupManagerMonitorEventSender(android.app.backup.IBackupManagerMonitor monitor, com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils backupManagerMonitorDumpsysUtils) {
        this.mMonitor = monitor;
        this.mBackupManagerMonitorDumpsysUtils = backupManagerMonitorDumpsysUtils;
    }

    public void setMonitor(android.app.backup.IBackupManagerMonitor monitor) {
        this.mMonitor = monitor;
    }

    public android.app.backup.IBackupManagerMonitor getMonitor() {
        return this.mMonitor;
    }

    public void monitorEvent(int id, android.content.pm.PackageInfo pkg, int category, android.os.Bundle extras) {
        try {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putInt("android.app.backup.extra.LOG_EVENT_ID", id);
            bundle.putInt("android.app.backup.extra.LOG_EVENT_CATEGORY", category);
            if (pkg != null) {
                bundle.putString("android.app.backup.extra.LOG_EVENT_PACKAGE_NAME", pkg.packageName);
                bundle.putInt("android.app.backup.extra.LOG_EVENT_PACKAGE_VERSION", pkg.versionCode);
                bundle.putLong("android.app.backup.extra.LOG_EVENT_PACKAGE_FULL_VERSION", pkg.getLongVersionCode());
            }
            if (extras != null) {
                bundle.putAll(extras);
                if (extras.containsKey("android.app.backup.extra.OPERATION_TYPE") && extras.getInt("android.app.backup.extra.OPERATION_TYPE") == 1) {
                    this.mBackupManagerMonitorDumpsysUtils.parseBackupManagerMonitorRestoreEventForDumpsys(bundle);
                }
            }
            if (this.mMonitor != null) {
                this.mMonitor.onEvent(bundle);
            } else {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "backup manager monitor is null unable to send event");
            }
        } catch (android.os.RemoteException e) {
            this.mMonitor = null;
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "backup manager monitor went away");
        }
    }

    public void monitorAgentLoggingResults(android.content.pm.PackageInfo pkg, android.app.IBackupAgent agent) {
        if (this.mMonitor == null) {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "backup manager monitor is null unable to send event" + pkg);
        }
        try {
            com.android.internal.infra.AndroidFuture<java.util.List<android.app.backup.BackupRestoreEventLogger.DataTypeResult>> resultsFuture = new com.android.internal.infra.AndroidFuture<>();
            com.android.internal.infra.AndroidFuture<java.lang.Integer> operationTypeFuture = new com.android.internal.infra.AndroidFuture<>();
            agent.getLoggerResults(resultsFuture);
            agent.getOperationType(operationTypeFuture);
            sendAgentLoggingResults(pkg, (java.util.List) resultsFuture.get(500L, java.util.concurrent.TimeUnit.MILLISECONDS), ((java.lang.Integer) operationTypeFuture.get(500L, java.util.concurrent.TimeUnit.MILLISECONDS)).intValue());
        } catch (java.util.concurrent.TimeoutException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Timeout while waiting to retrieve logging results from agent", e);
        } catch (java.lang.Exception e2) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Failed to retrieve logging results from agent", e2);
        }
    }

    public void sendAgentLoggingResults(android.content.pm.PackageInfo pkg, java.util.List<android.app.backup.BackupRestoreEventLogger.DataTypeResult> results, int operationType) {
        android.os.Bundle loggerResultsBundle = new android.os.Bundle();
        loggerResultsBundle.putParcelableList("android.app.backup.extra.LOG_AGENT_LOGGING_RESULTS", results);
        loggerResultsBundle.putInt("android.app.backup.extra.OPERATION_TYPE", operationType);
        monitorEvent(52, pkg, 2, loggerResultsBundle);
    }

    public static android.os.Bundle putMonitoringExtra(android.os.Bundle extras, java.lang.String key, java.lang.String value) {
        if (extras == null) {
            extras = new android.os.Bundle();
        }
        extras.putString(key, value);
        return extras;
    }

    public static android.os.Bundle putMonitoringExtra(android.os.Bundle extras, java.lang.String key, long value) {
        if (extras == null) {
            extras = new android.os.Bundle();
        }
        extras.putLong(key, value);
        return extras;
    }

    public static android.os.Bundle putMonitoringExtra(android.os.Bundle extras, java.lang.String key, boolean value) {
        if (extras == null) {
            extras = new android.os.Bundle();
        }
        extras.putBoolean(key, value);
        return extras;
    }

    public static android.os.Bundle putMonitoringExtra(android.os.Bundle extras, java.lang.String key, int value) {
        if (extras == null) {
            extras = new android.os.Bundle();
        }
        extras.putInt(key, value);
        return extras;
    }
}
