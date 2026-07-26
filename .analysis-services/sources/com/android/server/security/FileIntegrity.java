package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public final class FileIntegrity {
    private FileIntegrity() {
    }

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public static void setUpFsVerity(java.io.File file) throws java.io.IOException {
        android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(file, 268435456);
        try {
            setUpFsVerity(pfd);
            if (pfd != null) {
                pfd.close();
            }
        } catch (java.lang.Throwable th) {
            if (pfd != null) {
                try {
                    pfd.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public static void setUpFsVerity(android.os.ParcelFileDescriptor parcelFileDescriptor) throws java.io.IOException {
        com.android.internal.security.VerityUtils.setUpFsverity(parcelFileDescriptor.getFd());
    }
}
