package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class AdbRestoreFinishedRunnable implements java.lang.Runnable {
    private final android.app.IBackupAgent mAgent;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final int mToken;

    AdbRestoreFinishedRunnable(android.app.IBackupAgent agent, int token, com.android.server.backup.UserBackupManagerService backupManagerService) {
        this.mAgent = agent;
        this.mToken = token;
        this.mBackupManagerService = backupManagerService;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.mAgent.doRestoreFinished(this.mToken, this.mBackupManagerService.getBackupManagerBinder());
        } catch (android.os.RemoteException e) {
        }
    }
}
