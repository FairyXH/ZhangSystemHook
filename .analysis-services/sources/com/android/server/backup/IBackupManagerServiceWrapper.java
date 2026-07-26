package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public interface IBackupManagerServiceWrapper {
    default com.android.server.backup.IBackupManagerServiceExt getExtImpl() {
        return new com.android.server.backup.IBackupManagerServiceExt() { // from class: com.android.server.backup.IBackupManagerServiceWrapper.1
        };
    }
}
