package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class FullBackupRestoreObserverUtils {
    public static android.app.backup.IFullBackupRestoreObserver sendStartRestore(android.app.backup.IFullBackupRestoreObserver observer) {
        if (observer != null) {
            try {
                observer.onStartRestore();
                return observer;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "full restore observer went away: startRestore");
                return null;
            }
        }
        return observer;
    }

    public static android.app.backup.IFullBackupRestoreObserver sendOnRestorePackage(android.app.backup.IFullBackupRestoreObserver observer, java.lang.String name) {
        if (observer != null) {
            try {
                observer.onRestorePackage(name);
                return observer;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "full restore observer went away: restorePackage");
                return null;
            }
        }
        return observer;
    }

    public static android.app.backup.IFullBackupRestoreObserver sendEndRestore(android.app.backup.IFullBackupRestoreObserver observer) {
        if (observer != null) {
            try {
                observer.onEndRestore();
                return observer;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "full restore observer went away: endRestore");
                return null;
            }
        }
        return observer;
    }
}
