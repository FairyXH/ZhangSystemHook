package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public interface OperationStorage {

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface OpState {
        public static final int ACKNOWLEDGED = 1;
        public static final int PENDING = 0;
        public static final int TIMEOUT = -1;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface OpType {
        public static final int BACKUP = 2;
        public static final int BACKUP_WAIT = 0;
        public static final int OPLUS_BACKUP_WAIT = 101;
        public static final int OPLUS_RESTORE_WAIT = 102;
        public static final int RESTORE_WAIT = 1;
    }

    boolean isBackupOperationInProgress();

    int numOperations();

    java.util.Set<java.lang.Integer> operationTokensForOpState(int i);

    java.util.Set<java.lang.Integer> operationTokensForOpType(int i);

    java.util.Set<java.lang.Integer> operationTokensForPackage(java.lang.String str);

    void registerOperation(int i, int i2, com.android.server.backup.BackupRestoreTask backupRestoreTask, int i3);

    void registerOperationForPackages(int i, int i2, java.util.Set<java.lang.String> set, com.android.server.backup.BackupRestoreTask backupRestoreTask, int i3);

    void removeOperation(int i);
}
