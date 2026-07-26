package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
final class UserBackupManagerFilePersistedSettings {
    private static final java.lang.String BACKUP_ENABLE_FILE = "backup_enabled";

    UserBackupManagerFilePersistedSettings() {
    }

    static boolean readBackupEnableState(int userId) {
        boolean enabled = readBackupEnableState(com.android.server.backup.UserBackupManagerFiles.getBaseStateDir(userId));
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "user:" + userId + " readBackupEnableState enabled:" + enabled);
        return enabled;
    }

    static void writeBackupEnableState(int userId, boolean enable) {
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "user:" + userId + " writeBackupEnableState enable:" + enable);
        writeBackupEnableState(com.android.server.backup.UserBackupManagerFiles.getBaseStateDir(userId), enable);
    }

    private static boolean readBackupEnableState(java.io.File baseDir) {
        java.io.File enableFile = new java.io.File(baseDir, BACKUP_ENABLE_FILE);
        if (enableFile.exists()) {
            try {
                java.io.FileInputStream fin = new java.io.FileInputStream(enableFile);
                try {
                    int state = fin.read();
                    if (state != 0 && state != 1) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unexpected enabled state:" + state);
                    }
                    boolean z = state != 0;
                    fin.close();
                    return z;
                } finally {
                }
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Cannot read enable state; assuming disabled");
            }
        } else {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "isBackupEnabled() => false due to absent settings file");
        }
        return false;
    }

    private static void writeBackupEnableState(java.io.File baseDir, boolean enable) {
        java.io.File enableFile = new java.io.File(baseDir, BACKUP_ENABLE_FILE);
        java.io.File stage = new java.io.File(baseDir, "backup_enabled-stage");
        try {
            java.io.FileOutputStream fout = new java.io.FileOutputStream(stage);
            try {
                fout.write(enable ? 1 : 0);
                fout.close();
                boolean renamed = stage.renameTo(enableFile);
                if (!renamed) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Write enable failed as could not rename staging file to actual");
                }
                fout.close();
            } catch (java.lang.Throwable th) {
                try {
                    fout.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | java.lang.RuntimeException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to record backup enable state; reverting to disabled: " + e.getMessage());
            enableFile.delete();
            stage.delete();
        }
    }
}
