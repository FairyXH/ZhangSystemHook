package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public interface IFullRestoreEngineWrapper {
    default com.android.server.backup.restore.IFullRestoreEngineExt getExtImpl() {
        return new com.android.server.backup.restore.IFullRestoreEngineExt() { // from class: com.android.server.backup.restore.IFullRestoreEngineWrapper.1
        };
    }

    default com.android.server.backup.UserBackupManagerService getUserBackupManagerService() {
        return null;
    }
}
