package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class FileMetadata {
    public java.lang.String domain;
    public boolean hasApk;
    public java.lang.String installerPackageName;
    public long mode;
    public long mtime;
    public java.lang.String packageName;
    public java.lang.String path;
    public long size;
    public int type;
    public long version;

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("FileMetadata{");
        sb.append(this.packageName);
        sb.append(',');
        sb.append(this.type);
        sb.append(',');
        sb.append(this.domain);
        sb.append(':');
        sb.append(this.path);
        sb.append(',');
        sb.append(this.size);
        sb.append('}');
        return sb.toString();
    }

    public void dump() {
        java.lang.StringBuilder b = new java.lang.StringBuilder(128);
        b.append(this.type == 2 ? 'd' : '-');
        b.append((this.mode & 256) != 0 ? 'r' : '-');
        b.append((this.mode & 128) != 0 ? 'w' : '-');
        b.append((this.mode & 64) != 0 ? 'x' : '-');
        b.append((this.mode & 32) != 0 ? 'r' : '-');
        b.append((this.mode & 16) != 0 ? 'w' : '-');
        b.append((this.mode & 8) != 0 ? 'x' : '-');
        b.append((this.mode & 4) == 0 ? '-' : 'r');
        b.append((this.mode & 2) == 0 ? '-' : 'w');
        b.append((this.mode & 1) != 0 ? 'x' : '-');
        b.append(java.lang.String.format(" %9d ", java.lang.Long.valueOf(this.size)));
        java.util.Date stamp = new java.util.Date(this.mtime);
        b.append(new java.text.SimpleDateFormat("MMM dd HH:mm:ss ").format(stamp));
        b.append(this.packageName);
        b.append(" :: ");
        b.append(this.domain);
        b.append(" :: ");
        b.append(this.path);
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, b.toString());
    }
}
