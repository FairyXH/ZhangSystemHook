package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class SecureRandomUtils {
    private static final java.security.SecureRandom RNG = new java.security.SecureRandom();

    public static byte[] randomBytes(int length) {
        byte[] res = new byte[length];
        RNG.nextBytes(res);
        return res;
    }

    public static long randomLong() {
        return RNG.nextLong();
    }
}
