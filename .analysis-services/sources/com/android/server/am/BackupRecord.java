package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class BackupRecord {
    public static final int BACKUP_FULL = 1;
    public static final int BACKUP_NORMAL = 0;
    public static final int RESTORE = 2;
    public static final int RESTORE_FULL = 3;
    com.android.server.am.ProcessRecord app;
    final android.content.pm.ApplicationInfo appInfo;
    final int backupDestination;
    final int backupMode;
    java.lang.String stringName;
    final int userId;

    BackupRecord(android.content.pm.ApplicationInfo _appInfo, int _backupMode, int _userId, int _backupDestination) {
        this.appInfo = _appInfo;
        this.backupMode = _backupMode;
        this.userId = _userId;
        this.backupDestination = _backupDestination;
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("BackupRecord{").append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))).append(' ').append(this.appInfo.packageName).append(' ').append(this.appInfo.name).append(' ').append(this.appInfo.backupAgentName).append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }
}
