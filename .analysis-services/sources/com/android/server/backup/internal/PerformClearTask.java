package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public class PerformClearTask implements java.lang.Runnable {
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final com.android.server.backup.internal.OnTaskFinishedListener mListener;
    private final android.content.pm.PackageInfo mPackage;
    private final com.android.server.backup.transport.TransportConnection mTransportConnection;
    private final com.android.server.backup.TransportManager mTransportManager;

    PerformClearTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.transport.TransportConnection transportConnection, android.content.pm.PackageInfo packageInfo, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        this.mBackupManagerService = backupManagerService;
        this.mTransportManager = backupManagerService.getTransportManager();
        this.mTransportConnection = transportConnection;
        this.mPackage = packageInfo;
        this.mListener = listener;
    }

    @Override // java.lang.Runnable
    public void run() throws java.lang.Exception {
        java.lang.StringBuilder sb;
        com.android.server.backup.transport.BackupTransportClient transport = null;
        try {
            try {
                java.lang.String transportDirName = this.mTransportManager.getTransportDirName(this.mTransportConnection.getTransportComponent());
                java.io.File stateDir = new java.io.File(this.mBackupManagerService.getBaseStateDir(), transportDirName);
                java.io.File stateFile = new java.io.File(stateDir, this.mPackage.packageName);
                stateFile.delete();
                transport = this.mTransportConnection.connectOrThrow("PerformClearTask.run()");
                transport.clearBackupData(this.mPackage);
                if (transport != null) {
                    try {
                        transport.finishBackup();
                    } catch (java.lang.Exception e) {
                        e = e;
                        sb = new java.lang.StringBuilder();
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, sb.append("Unable to mark clear operation finished: ").append(e.getMessage()).toString());
                    }
                }
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Transport threw clearing data for " + this.mPackage + ": " + e2.getMessage());
                if (transport != null) {
                    try {
                        transport.finishBackup();
                    } catch (java.lang.Exception e3) {
                        e = e3;
                        sb = new java.lang.StringBuilder();
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, sb.append("Unable to mark clear operation finished: ").append(e.getMessage()).toString());
                    }
                }
            }
            this.mListener.onFinished("PerformClearTask.run()");
            this.mBackupManagerService.getWakelock().release();
        } catch (java.lang.Throwable e4) {
            if (transport != null) {
                try {
                    transport.finishBackup();
                } catch (java.lang.Exception e5) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to mark clear operation finished: " + e5.getMessage());
                }
            }
            this.mListener.onFinished("PerformClearTask.run()");
            this.mBackupManagerService.getWakelock().release();
            throw e4;
        }
    }
}
