package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public class SetupObserver extends android.database.ContentObserver {
    private final android.content.Context mContext;
    private final com.android.server.backup.UserBackupManagerService mUserBackupManagerService;
    private final int mUserId;

    public SetupObserver(com.android.server.backup.UserBackupManagerService userBackupManagerService, android.os.Handler handler) {
        super(handler);
        this.mUserBackupManagerService = userBackupManagerService;
        this.mContext = userBackupManagerService.getContext();
        this.mUserId = userBackupManagerService.getUserId();
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange) {
        boolean previousSetupComplete = this.mUserBackupManagerService.isSetupComplete();
        boolean newSetupComplete = com.android.server.backup.UserBackupManagerService.getSetupCompleteSettingForUser(this.mContext, this.mUserId);
        boolean resolvedSetupComplete = previousSetupComplete || newSetupComplete;
        this.mUserBackupManagerService.setSetupComplete(resolvedSetupComplete);
        synchronized (this.mUserBackupManagerService.getQueueLock()) {
            if (resolvedSetupComplete && !previousSetupComplete) {
                if (this.mUserBackupManagerService.isEnabled()) {
                    com.android.server.backup.KeyValueBackupJob.schedule(this.mUserBackupManagerService.getUserId(), this.mContext, this.mUserBackupManagerService);
                    this.mUserBackupManagerService.scheduleNextFullBackupJob(0L);
                }
            }
        }
    }
}
