package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public interface IFullBackupEngineWrapper {
    default com.android.server.backup.fullbackup.IFullBackupEngineExt getExtImpl() {
        return new com.android.server.backup.fullbackup.IFullBackupEngineExt() { // from class: com.android.server.backup.fullbackup.IFullBackupEngineWrapper.1
        };
    }

    default com.android.server.backup.UserBackupManagerService getUserBackupManagerService() {
        return null;
    }

    default com.android.server.backup.fullbackup.IFullBackupEngineExt.IStaticExt getStaticExtImpl() {
        return new com.android.server.backup.fullbackup.IFullBackupEngineExt.IStaticExt() { // from class: com.android.server.backup.fullbackup.IFullBackupEngineWrapper.2
        };
    }
}
