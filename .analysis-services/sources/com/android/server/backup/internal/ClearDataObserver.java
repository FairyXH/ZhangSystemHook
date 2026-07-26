package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public class ClearDataObserver extends android.content.pm.IPackageDataObserver.Stub {
    private com.android.server.backup.UserBackupManagerService backupManagerService;

    public ClearDataObserver(com.android.server.backup.UserBackupManagerService backupManagerService) {
        this.backupManagerService = backupManagerService;
    }

    public void onRemoveCompleted(java.lang.String packageName, boolean succeeded) {
        synchronized (this.backupManagerService.getClearDataLock()) {
            this.backupManagerService.getWrapper().getExtImpl().hookInOnRemoveCompleted(packageName, false);
            this.backupManagerService.setClearingData(false);
            this.backupManagerService.getClearDataLock().notifyAll();
        }
    }
}
