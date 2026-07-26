package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public interface BackupRestoreTask {
    void execute();

    void handleCancel(boolean z);

    void operationComplete(long j);
}
