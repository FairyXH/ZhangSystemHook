package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public class FullBackupObbConnection implements android.content.ServiceConnection {
    private com.android.server.backup.UserBackupManagerService backupManagerService;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    volatile com.android.internal.backup.IObbBackupService mService = null;

    public FullBackupObbConnection(com.android.server.backup.UserBackupManagerService backupManagerService) {
        this.backupManagerService = backupManagerService;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
    }

    public void establish() {
        android.content.Intent obbIntent = new android.content.Intent().setComponent(new android.content.ComponentName(com.android.server.backup.UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE, "com.android.sharedstoragebackup.ObbBackupService"));
        this.backupManagerService.getContext().bindServiceAsUser(obbIntent, this, 1, android.os.UserHandle.SYSTEM);
    }

    public void tearDown() {
        this.backupManagerService.getContext().unbindService(this);
    }

    public boolean backupObbs(android.content.pm.PackageInfo pkg, java.io.OutputStream out) {
        boolean success = false;
        waitForConnection();
        android.os.ParcelFileDescriptor[] pipes = null;
        try {
            try {
                try {
                    pipes = android.os.ParcelFileDescriptor.createPipe();
                    int token = this.backupManagerService.generateRandomIntegerToken();
                    long fullBackupAgentTimeoutMillis = this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
                    this.backupManagerService.prepareOperationTimeout(token, fullBackupAgentTimeoutMillis, null, 0);
                    this.mService.backupObbs(pkg.packageName, pipes[1], token, this.backupManagerService.getBackupManagerBinder());
                    com.android.server.backup.utils.FullBackupUtils.routeSocketDataToOutput(pipes[0], out);
                    success = this.backupManagerService.waitUntilOperationComplete(token);
                    out.flush();
                    if (pipes != null) {
                        if (pipes[0] != null) {
                            pipes[0].close();
                        }
                        if (pipes[1] != null) {
                            pipes[1].close();
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unable to back up OBBs for " + pkg, e);
                    out.flush();
                    if (pipes != null) {
                        if (pipes[0] != null) {
                            pipes[0].close();
                        }
                        if (pipes[1] != null) {
                            pipes[1].close();
                        }
                    }
                }
            } catch (java.io.IOException e2) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "I/O error closing down OBB backup", e2);
            }
            return success;
        } catch (java.lang.Throwable th) {
            try {
                out.flush();
                if (pipes != null) {
                    if (pipes[0] != null) {
                        pipes[0].close();
                    }
                    if (pipes[1] != null) {
                        pipes[1].close();
                    }
                }
            } catch (java.io.IOException e3) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "I/O error closing down OBB backup", e3);
            }
            throw th;
        }
    }

    public void restoreObbFile(java.lang.String pkgName, android.os.ParcelFileDescriptor data, long fileSize, int type, java.lang.String path, long mode, long mtime, int token, android.app.backup.IBackupManager callbackBinder) {
        waitForConnection();
        try {
            this.mService.restoreObbFile(pkgName, data, fileSize, type, path, mode, mtime, token, callbackBinder);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unable to restore OBBs for " + pkgName, e);
        }
    }

    private void waitForConnection() {
        synchronized (this) {
            while (this.mService == null) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        synchronized (this) {
            this.mService = com.android.internal.backup.IObbBackupService.Stub.asInterface(service);
            notifyAll();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName name) {
        synchronized (this) {
            this.mService = null;
            notifyAll();
        }
    }
}
