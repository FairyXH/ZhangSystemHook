package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class BackupParams {
    public java.lang.String dirName;
    public java.util.ArrayList<java.lang.String> fullPackages;
    public java.util.ArrayList<java.lang.String> kvPackages;
    public com.android.server.backup.internal.OnTaskFinishedListener listener;
    public com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    public com.android.server.backup.transport.TransportConnection mTransportConnection;
    public android.app.backup.IBackupManagerMonitor monitor;
    public boolean nonIncrementalBackup;
    public android.app.backup.IBackupObserver observer;
    public boolean userInitiated;

    public BackupParams(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String dirName, java.util.ArrayList<java.lang.String> kvPackages, java.util.ArrayList<java.lang.String> fullPackages, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, com.android.server.backup.internal.OnTaskFinishedListener listener, boolean userInitiated, boolean nonIncrementalBackup, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        this.mTransportConnection = transportConnection;
        this.dirName = dirName;
        this.kvPackages = kvPackages;
        this.fullPackages = fullPackages;
        this.observer = observer;
        this.monitor = monitor;
        this.listener = listener;
        this.userInitiated = userInitiated;
        this.nonIncrementalBackup = nonIncrementalBackup;
        this.mBackupEligibilityRules = backupEligibilityRules;
    }
}
