package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public final class RandomAccessFileUtils {
    private static java.io.RandomAccessFile getRandomAccessFile(java.io.File file) throws java.io.FileNotFoundException {
        return new java.io.RandomAccessFile(file, "rwd");
    }

    public static void writeBoolean(java.io.File file, boolean b) {
        try {
            java.io.RandomAccessFile af = getRandomAccessFile(file);
            try {
                af.writeBoolean(b);
                if (af != null) {
                    af.close();
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Error writing file:" + file.getAbsolutePath(), e);
        }
    }

    public static boolean readBoolean(java.io.File file, boolean def) {
        try {
            java.io.RandomAccessFile af = getRandomAccessFile(file);
            try {
                boolean z = af.readBoolean();
                if (af != null) {
                    af.close();
                }
                return z;
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Error reading file:" + file.getAbsolutePath(), e);
            return def;
        }
    }
}
