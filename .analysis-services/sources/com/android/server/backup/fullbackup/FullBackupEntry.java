package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public class FullBackupEntry implements java.lang.Comparable<com.android.server.backup.fullbackup.FullBackupEntry> {
    public long lastBackup;
    public java.lang.String packageName;

    public FullBackupEntry(java.lang.String pkg, long when) {
        this.packageName = pkg;
        this.lastBackup = when;
    }

    @Override // java.lang.Comparable
    public int compareTo(com.android.server.backup.fullbackup.FullBackupEntry other) {
        if (this.lastBackup < other.lastBackup) {
            return -1;
        }
        if (this.lastBackup > other.lastBackup) {
            return 1;
        }
        return 0;
    }
}
