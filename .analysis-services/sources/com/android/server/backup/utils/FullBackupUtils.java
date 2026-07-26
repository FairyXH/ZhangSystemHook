package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class FullBackupUtils {
    public static void routeSocketDataToOutput(android.os.ParcelFileDescriptor inPipe, java.io.OutputStream out) throws java.io.IOException {
        java.io.FileInputStream raw = new java.io.FileInputStream(inPipe.getFileDescriptor());
        java.io.DataInputStream in = new java.io.DataInputStream(raw);
        int chunkSizeInBytes = 32768;
        if (com.android.server.backup.Flags.enableMaxSizeWritesToPipes()) {
            chunkSizeInBytes = 65536;
        }
        byte[] buffer = new byte[chunkSizeInBytes];
        while (true) {
            int i = in.readInt();
            int chunkTotal = i;
            if (i > 0) {
                while (chunkTotal > 0) {
                    int toRead = chunkTotal > buffer.length ? buffer.length : chunkTotal;
                    int nRead = in.read(buffer, 0, toRead);
                    if (nRead < 0) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unexpectedly reached end of file while reading data");
                        throw new java.io.EOFException();
                    }
                    out.write(buffer, 0, nRead);
                    chunkTotal -= nRead;
                }
            } else {
                return;
            }
        }
    }
}
