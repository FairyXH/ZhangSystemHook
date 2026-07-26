package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class AdbRestoreFinishedLatch implements com.android.server.backup.BackupRestoreTask {
    private static final java.lang.String TAG = "AdbRestoreFinishedLatch";
    private com.android.server.backup.UserBackupManagerService backupManagerService;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final int mCurrentOpToken;
    final java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);
    private final com.android.server.backup.OperationStorage mOperationStorage;

    public AdbRestoreFinishedLatch(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, int currentOpToken) {
        this.backupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mCurrentOpToken = currentOpToken;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
    }

    void await() {
        long fullBackupAgentTimeoutMillis = this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
        try {
            this.mLatch.await(fullBackupAgentTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.w(TAG, "Interrupted!");
        }
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long result) {
        this.mLatch.countDown();
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean cancelAll) {
        android.util.Slog.w(TAG, "adb onRestoreFinished() timed out");
        this.mLatch.countDown();
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }
}
