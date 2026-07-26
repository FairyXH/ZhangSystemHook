package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
final class UserBackupManagerFiles {
    private static final java.lang.String BACKUP_PERSISTENT_DIR = "backup";
    private static final java.lang.String BACKUP_STAGING_DIR = "backup_stage";

    UserBackupManagerFiles() {
    }

    private static java.io.File getBaseDir(int userId) {
        return android.os.Environment.getDataSystemCeDirectory(userId);
    }

    static java.io.File getBaseStateDir(int userId) {
        if (userId != 0) {
            return new java.io.File(getBaseDir(userId), "backup");
        }
        return new java.io.File(android.os.Environment.getDataDirectory(), "backup");
    }

    static java.io.File getDataDir(int userId) {
        if (userId != 0) {
            return new java.io.File(getBaseDir(userId), BACKUP_STAGING_DIR);
        }
        return new java.io.File(android.os.Environment.getDownloadCacheDirectory(), BACKUP_STAGING_DIR);
    }

    static java.io.File getStateDirInSystemDir(int userId) {
        return new java.io.File(getBaseStateDir(0), "" + userId);
    }

    static java.io.File getStateFileInSystemDir(java.lang.String filename, int userId) {
        return new java.io.File(getStateDirInSystemDir(userId), filename);
    }
}
