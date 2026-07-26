package com.android.server.backup.keyvalue;

/* JADX INFO: loaded from: classes.dex */
public class BackupRequest {
    public java.lang.String packageName;

    public BackupRequest(java.lang.String pkgName) {
        this.packageName = pkgName;
    }

    public java.lang.String toString() {
        return "BackupRequest{pkg=" + this.packageName + "}";
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.backup.keyvalue.BackupRequest)) {
            return false;
        }
        com.android.server.backup.keyvalue.BackupRequest that = (com.android.server.backup.keyvalue.BackupRequest) o;
        return java.util.Objects.equals(this.packageName, that.packageName);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.packageName);
    }
}
