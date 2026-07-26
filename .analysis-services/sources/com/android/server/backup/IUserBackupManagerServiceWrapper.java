package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public interface IUserBackupManagerServiceWrapper {
    default com.android.server.backup.IUserBackupManagerServiceExt.IStaticExt getStaticExtImpl() {
        return new com.android.server.backup.IUserBackupManagerServiceExt.IStaticExt() { // from class: com.android.server.backup.IUserBackupManagerServiceWrapper.1
        };
    }

    default com.android.server.backup.IUserBackupManagerServiceExt getExtImpl() {
        return new com.android.server.backup.IUserBackupManagerServiceExt() { // from class: com.android.server.backup.IUserBackupManagerServiceWrapper.2
        };
    }
}
