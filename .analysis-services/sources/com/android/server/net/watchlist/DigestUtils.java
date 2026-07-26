package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
public class DigestUtils {
    private static final int FILE_READ_BUFFER_SIZE = 16384;

    private DigestUtils() {
    }

    public static byte[] getSha256Hash(java.io.File apkFile) throws java.security.NoSuchAlgorithmException, java.io.IOException {
        java.io.InputStream stream = new java.io.FileInputStream(apkFile);
        try {
            byte[] sha256Hash = getSha256Hash(stream);
            stream.close();
            return sha256Hash;
        } catch (java.lang.Throwable th) {
            try {
                stream.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static byte[] getSha256Hash(java.io.InputStream stream) throws java.security.NoSuchAlgorithmException, java.io.IOException {
        java.security.MessageDigest digester = java.security.MessageDigest.getInstance("SHA256");
        byte[] buf = new byte[16384];
        while (true) {
            int bytesRead = stream.read(buf);
            if (bytesRead >= 0) {
                digester.update(buf, 0, bytesRead);
            } else {
                return digester.digest();
            }
        }
    }
}
