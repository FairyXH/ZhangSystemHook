package com.android.server.backup.keyvalue;

/* JADX INFO: loaded from: classes.dex */
public class KeyValueBackupReporter {
    private static final boolean DEBUG = true;
    static final boolean MORE_DEBUG = false;
    static final java.lang.String TAG = "KeyValueBackupTask";
    private final com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final android.app.backup.IBackupObserver mObserver;

    static void onNewThread(java.lang.String threadName) {
        android.util.Slog.d(TAG, "Spinning thread " + threadName);
    }

    KeyValueBackupReporter(com.android.server.backup.UserBackupManagerService backupManagerService, android.app.backup.IBackupObserver observer, com.android.server.backup.utils.BackupManagerMonitorEventSender backupManagerMonitorEventSender) {
        this.mBackupManagerService = backupManagerService;
        this.mObserver = observer;
        this.mBackupManagerMonitorEventSender = backupManagerMonitorEventSender;
    }

    android.app.backup.IBackupManagerMonitor getMonitor() {
        return this.mBackupManagerMonitorEventSender.getMonitor();
    }

    android.app.backup.IBackupObserver getObserver() {
        return this.mObserver;
    }

    void onSkipBackup() {
        android.util.Slog.d(TAG, "Skipping backup since one is already in progress");
    }

    void onEmptyQueueAtStart() {
        android.util.Slog.w(TAG, "Backup begun with an empty queue, nothing to do");
    }

    void onQueueReady(java.util.List<java.lang.String> queue) {
        android.util.Slog.v(TAG, "Beginning backup of " + queue.size() + " targets");
    }

    void onTransportReady(java.lang.String transportName) {
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_START, transportName);
    }

    void onInitializeTransport(java.lang.String transportName) {
        android.util.Slog.i(TAG, "Initializing transport and resetting backup state");
    }

    void onTransportInitialized(int status) {
        if (status == 0) {
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_INITIALIZE, new java.lang.Object[0]);
        } else {
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_FAILURE, "(initialize)");
            android.util.Slog.e(TAG, "Transport error in initializeDevice()");
        }
    }

    void onInitializeTransportError(java.lang.Exception e) {
        android.util.Slog.e(TAG, "Error during initialization", e);
    }

    void onSkipPm() {
        android.util.Slog.d(TAG, "Skipping backup of PM metadata");
    }

    void onExtractPmAgentDataError(java.lang.Exception e) {
        android.util.Slog.e(TAG, "Error during PM metadata backup", e);
    }

    void onStartPackageBackup(java.lang.String packageName) {
        android.util.Slog.d(TAG, "Starting key-value backup of " + packageName);
    }

    void onPackageNotEligibleForBackup(java.lang.String packageName) {
        android.util.Slog.i(TAG, "Package " + packageName + " no longer supports backup, skipping");
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -2001);
    }

    void onPackageEligibleForFullBackup(java.lang.String packageName) {
        android.util.Slog.i(TAG, "Package " + packageName + " performs full-backup rather than key-value, skipping");
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -2001);
    }

    void onPackageStopped(java.lang.String packageName) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -2001);
    }

    void onAgentUnknown(java.lang.String packageName) {
        android.util.Slog.d(TAG, "Package does not exist, skipping");
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -2002);
    }

    void onBindAgentError(java.lang.String packageName, java.lang.SecurityException e) {
        android.util.Slog.d(TAG, "Error in bind/backup", e);
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1003);
    }

    void onAgentError(java.lang.String packageName) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1003);
    }

    void onExtractAgentData(java.lang.String packageName) {
        android.util.Slog.d(TAG, "Invoking agent on " + packageName);
    }

    void onAgentFilesReady(java.io.File backupDataFile) {
    }

    void onRestoreconFailed(java.io.File backupDataFile) {
        android.util.Slog.e(TAG, "SELinux restorecon failed on " + backupDataFile);
    }

    void onCallAgentDoBackupError(java.lang.String packageName, boolean callingAgent, java.lang.Exception e) {
        if (callingAgent) {
            android.util.Slog.e(TAG, "Error invoking agent on " + packageName + ": " + e);
            com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1003);
        } else {
            android.util.Slog.e(TAG, "Error before invoking agent on " + packageName + ": " + e);
        }
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_AGENT_FAILURE, packageName, e.toString());
    }

    void onFailAgentError(java.lang.String packageName) {
        android.util.Slog.w(TAG, "Error conveying failure to " + packageName);
    }

    void onAgentIllegalKey(android.content.pm.PackageInfo packageInfo, java.lang.String key) {
        java.lang.String packageName = packageInfo.packageName;
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_AGENT_FAILURE, packageName, "bad key");
        this.mBackupManagerMonitorEventSender.monitorEvent(5, packageInfo, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_ILLEGAL_KEY", key));
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1003);
    }

    void onAgentDataError(java.lang.String packageName, java.io.IOException e) {
        android.util.Slog.w(TAG, "Unable to read/write agent data for " + packageName + ": " + e);
    }

    void onDigestError(java.security.NoSuchAlgorithmException e) {
        android.util.Slog.e(TAG, "Unable to use SHA-1!");
    }

    void onWriteWidgetData(boolean priorStateExists, byte[] widgetState) {
    }

    void onTransportPerformBackup(java.lang.String packageName) {
    }

    void onEmptyData(android.content.pm.PackageInfo packageInfo) {
        this.mBackupManagerMonitorEventSender.monitorEvent(7, packageInfo, 3, null);
    }

    void onPackageBackupComplete(java.lang.String packageName, long size) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, 0);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_PACKAGE, packageName, java.lang.Long.valueOf(size));
        this.mBackupManagerService.logBackupComplete(packageName);
    }

    void onPackageBackupRejected(java.lang.String packageName) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, com.android.server.job.JobSchedulerShellCommand.CMD_ERR_CONSTRAINTS);
        com.android.server.EventLogTags.writeBackupAgentFailure(packageName, "Transport rejected");
    }

    void onPackageBackupQuotaExceeded(java.lang.String packageName) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1005);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_QUOTA_EXCEEDED, packageName);
    }

    void onAgentDoQuotaExceededError(java.lang.Exception e) {
        android.util.Slog.e(TAG, "Unable to notify about quota exceeded: " + e);
    }

    void onPackageBackupNonIncrementalRequired(android.content.pm.PackageInfo packageInfo) {
        android.util.Slog.i(TAG, "Transport lost data, retrying package");
        this.mBackupManagerMonitorEventSender.monitorEvent(51, packageInfo, 1, null);
    }

    void onPackageBackupNonIncrementalAndNonIncrementalRequired(java.lang.String packageName) {
        android.util.Slog.e(TAG, "Transport requested non-incremental but already the case");
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1000);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_FAILURE, packageName);
    }

    void onPackageBackupTransportFailure(java.lang.String packageName) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1000);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_FAILURE, packageName);
    }

    void onPackageBackupTransportError(java.lang.String packageName, java.lang.Exception e) {
        android.util.Slog.e(TAG, "Transport error backing up " + packageName, e);
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1000);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_FAILURE, packageName);
    }

    void onCloseFileDescriptorError(java.lang.String logName) {
        android.util.Slog.w(TAG, "Error closing " + logName + " file-descriptor");
    }

    void onCancel() {
    }

    void onAgentTimedOut(android.content.pm.PackageInfo packageInfo) {
        java.lang.String packageName = getPackageName(packageInfo);
        android.util.Slog.i(TAG, "Agent " + packageName + " timed out");
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_AGENT_FAILURE, packageName);
        this.mBackupManagerMonitorEventSender.monitorEvent(21, packageInfo, 2, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_CANCEL_ALL", false));
    }

    void onAgentCancelled(android.content.pm.PackageInfo packageInfo) {
        java.lang.String packageName = getPackageName(packageInfo);
        android.util.Slog.i(TAG, "Cancel backing up " + packageName);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_AGENT_FAILURE, packageName);
        this.mBackupManagerMonitorEventSender.monitorEvent(21, packageInfo, 2, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.LOG_CANCEL_ALL", true));
    }

    void onAgentResultError(android.content.pm.PackageInfo packageInfo) {
        java.lang.String packageName = getPackageName(packageInfo);
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mObserver, packageName, -1003);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_AGENT_FAILURE, packageName, "result error");
        android.util.Slog.w(TAG, "Agent " + packageName + " error in onBackup()");
    }

    private java.lang.String getPackageName(android.content.pm.PackageInfo packageInfo) {
        return packageInfo != null ? packageInfo.packageName : "no_package_yet";
    }

    void onRevertTask() {
    }

    void onTransportRequestBackupTimeError(java.lang.Exception e) {
        android.util.Slog.w(TAG, "Unable to contact transport for recommended backoff: " + e);
    }

    void onRemoteCallReturned(com.android.server.backup.remote.RemoteResult result, java.lang.String logIdentifier) {
    }

    void onJournalDeleteFailed(com.android.server.backup.DataChangedJournal journal) {
        android.util.Slog.e(TAG, "Unable to remove backup journal file " + journal);
    }

    void onSetCurrentTokenError(java.lang.Exception e) {
        android.util.Slog.e(TAG, "Transport threw reporting restore set: " + e);
    }

    void onTransportNotInitialized(java.lang.String transportName) {
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_RESET, transportName);
    }

    void onPendingInitializeTransportError(java.lang.Exception e) {
        android.util.Slog.w(TAG, "Failed to query transport name for pending init: " + e);
    }

    void onBackupFinished(int status) {
        com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(this.mObserver, status);
    }

    void onStartFullBackup(java.util.List<java.lang.String> pendingFullBackups) {
        android.util.Slog.d(TAG, "Starting full backups for: " + pendingFullBackups);
    }

    void onTaskFinished() {
        android.util.Slog.i(TAG, "K/V backup pass finished");
    }
}
