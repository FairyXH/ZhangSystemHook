package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class RestoreParams {
    public final com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules;
    public final java.lang.String[] filterSet;
    public final boolean isSystemRestore;
    public final com.android.server.backup.internal.OnTaskFinishedListener listener;
    public final com.android.server.backup.transport.TransportConnection mTransportConnection;
    public final android.app.backup.IBackupManagerMonitor monitor;
    public final android.app.backup.IRestoreObserver observer;
    public final android.content.pm.PackageInfo packageInfo;
    public final int pmToken;
    public final long token;

    public static com.android.server.backup.params.RestoreParams createForSinglePackage(com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, android.content.pm.PackageInfo packageInfo, com.android.server.backup.internal.OnTaskFinishedListener listener, com.android.server.backup.utils.BackupEligibilityRules eligibilityRules) {
        return new com.android.server.backup.params.RestoreParams(transportConnection, observer, monitor, token, packageInfo, 0, false, null, listener, eligibilityRules);
    }

    public static com.android.server.backup.params.RestoreParams createForRestoreAtInstall(com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, java.lang.String packageName, int pmToken, com.android.server.backup.internal.OnTaskFinishedListener listener, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        java.lang.String[] filterSet = {packageName};
        return new com.android.server.backup.params.RestoreParams(transportConnection, observer, monitor, token, null, pmToken, false, filterSet, listener, backupEligibilityRules);
    }

    public static com.android.server.backup.params.RestoreParams createForRestoreAll(com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, com.android.server.backup.internal.OnTaskFinishedListener listener, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        return new com.android.server.backup.params.RestoreParams(transportConnection, observer, monitor, token, null, 0, true, null, listener, backupEligibilityRules);
    }

    public static com.android.server.backup.params.RestoreParams createForRestorePackages(com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, java.lang.String[] filterSet, boolean isSystemRestore, com.android.server.backup.internal.OnTaskFinishedListener listener, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        return new com.android.server.backup.params.RestoreParams(transportConnection, observer, monitor, token, null, 0, isSystemRestore, filterSet, listener, backupEligibilityRules);
    }

    private RestoreParams(com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, android.content.pm.PackageInfo packageInfo, int pmToken, boolean isSystemRestore, java.lang.String[] filterSet, com.android.server.backup.internal.OnTaskFinishedListener listener, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        this.mTransportConnection = transportConnection;
        this.observer = observer;
        this.monitor = monitor;
        this.token = token;
        this.packageInfo = packageInfo;
        this.pmToken = pmToken;
        this.isSystemRestore = isSystemRestore;
        this.filterSet = filterSet;
        this.listener = listener;
        this.backupEligibilityRules = backupEligibilityRules;
    }
}
