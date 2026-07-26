package com.android.server.backup.remote;

/* JADX INFO: loaded from: classes.dex */
public class ServiceBackupCallback extends android.app.backup.IBackupCallback.Stub {
    private final android.app.backup.IBackupManager mBackupManager;
    private final int mToken;

    public ServiceBackupCallback(android.app.backup.IBackupManager backupManager, int token) {
        this.mBackupManager = backupManager;
        this.mToken = token;
    }

    public void operationComplete(long result) throws android.os.RemoteException {
        this.mBackupManager.opComplete(this.mToken, result);
    }
}
