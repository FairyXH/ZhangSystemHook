package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
class RestoreFileRunnable implements java.lang.Runnable {
    private final android.app.IBackupAgent mAgent;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final com.android.server.backup.FileMetadata mInfo;
    private final android.os.ParcelFileDescriptor mSocket;
    private final int mToken;

    RestoreFileRunnable(com.android.server.backup.UserBackupManagerService backupManagerService, android.app.IBackupAgent agent, com.android.server.backup.FileMetadata info, android.os.ParcelFileDescriptor socket, int token) throws java.io.IOException {
        this.mAgent = agent;
        this.mInfo = info;
        this.mToken = token;
        this.mSocket = android.os.ParcelFileDescriptor.dup(socket.getFileDescriptor());
        this.mBackupManagerService = backupManagerService;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.mAgent.doRestoreFile(this.mSocket, this.mInfo.size, this.mInfo.type, this.mInfo.domain, this.mInfo.path, this.mInfo.mode, this.mInfo.mtime, this.mToken, this.mBackupManagerService.getBackupManagerBinder());
        } catch (android.os.RemoteException e) {
        }
    }
}
