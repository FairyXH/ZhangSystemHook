package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public final class FileUtils {
    public static java.io.File createNewFile(java.io.File file) {
        try {
            file.createNewFile();
        } catch (java.io.IOException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Failed to create file:" + file.getAbsolutePath(), e);
        }
        return file;
    }
}
