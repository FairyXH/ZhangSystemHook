package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public interface IFullRestoreEngineExt {
    default void establishExtConnection(com.android.server.backup.UserBackupManagerService bms) {
    }

    default boolean isExternalStorageDomain(java.lang.String domain) {
        return false;
    }

    default void restoreExternalFile(java.lang.String packageName, android.os.ParcelFileDescriptor data, long fileSize, int type, java.lang.String path, long mode, long mtime, int token, android.app.backup.IBackupManager callbackBinder) {
    }

    default void tearDownExtConnection() {
    }

    default void showRestoreProgress(java.lang.String packageName, long nowRead) {
    }

    default boolean hasNextTar(int restoreType) {
        return false;
    }

    default boolean needClearAppData(int restoreType) {
        return true;
    }

    default boolean isApkExist(android.content.pm.PackageManager packageManager, java.lang.String pkgName, int userId, boolean hasApk) {
        return true;
    }

    default void restoreEnd(java.lang.String packageName, int token, int errorCode, boolean succeed) {
    }

    default void tearDownAgentAndKill(java.lang.String packageName, int userId) {
    }
}
