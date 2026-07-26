package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class BackupObserverUtils {
    public static void sendBackupOnUpdate(android.app.backup.IBackupObserver observer, java.lang.String packageName, android.app.backup.BackupProgress progress) {
        if (observer != null) {
            try {
                observer.onUpdate(packageName, progress);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Backup observer went away: onUpdate");
            }
        }
    }

    public static void sendBackupOnPackageResult(android.app.backup.IBackupObserver observer, java.lang.String packageName, int status) {
        if (observer != null) {
            try {
                observer.onResult(packageName, status);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Backup observer went away: onResult");
            }
        }
    }

    public static void sendBackupFinished(android.app.backup.IBackupObserver observer, int status) {
        if (observer != null) {
            try {
                observer.backupFinished(status);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Backup observer went away: backupFinished");
            }
        }
    }
}
