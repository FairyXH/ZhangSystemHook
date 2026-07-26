package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class AdbParams {
    public java.lang.String curPassword;
    public java.lang.String encryptPassword;
    public android.os.ParcelFileDescriptor fd;
    public final java.util.concurrent.atomic.AtomicBoolean latch = new java.util.concurrent.atomic.AtomicBoolean(false);
    public android.app.backup.IFullBackupRestoreObserver observer;

    AdbParams() {
    }
}
