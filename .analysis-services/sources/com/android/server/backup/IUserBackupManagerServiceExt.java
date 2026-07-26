package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public interface IUserBackupManagerServiceExt {

    public interface IStaticExt {
    }

    default void handleOplusMessage(android.os.Message msg) {
    }

    default boolean needTearDown(int backupDestination, android.content.pm.ApplicationInfo aInfo) {
        return true;
    }

    default void registerOperationWithPackageForOplus(int token, long interval, java.lang.String packageName, com.android.server.backup.BackupRestoreTask callback, boolean isBackup) {
    }

    default void registerTokenForTimeout(int token) {
    }

    default void hookBindAgentBeforeClearPendingBackup(int backupDestination, int uid, android.app.ActivityManagerInternal activityManagerInternal, int userId) {
    }

    default boolean hookInClearApplicationDataSynchronous(java.lang.String packageName) {
        return true;
    }

    default void hookAfterClearApplicationDataSynchronous(java.lang.String packageName) {
    }

    default void hookInOnRemoveCompleted(java.lang.String packageName, boolean isClearingData) {
    }

    default void hookInBindToAgentSynchronous(java.lang.String packageName, android.app.IBackupAgent backupAgent, boolean connecting) {
    }

    default android.app.IBackupAgent hookForBindBackupAgent(java.lang.String packageName) {
        return null;
    }

    default boolean hookForBindBackupAgentSecond(java.lang.String packageName) {
        return false;
    }

    default void removeBackupAgentConnectState(java.lang.String packageName) {
    }
}
