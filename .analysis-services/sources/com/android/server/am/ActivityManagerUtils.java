package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ActivityManagerUtils {
    private static java.lang.Integer sAndroidIdHash;
    private static final android.util.ArrayMap<java.lang.String, java.lang.Integer> sHashCache = new android.util.ArrayMap<>();
    private static java.lang.String sInjectedAndroidId;

    private ActivityManagerUtils() {
    }

    static void injectAndroidIdForTest(java.lang.String androidId) {
        sInjectedAndroidId = androidId;
        sAndroidIdHash = null;
    }

    static int getAndroidIdHash() {
        if (sAndroidIdHash == null) {
            android.content.ContentResolver resolver = android.app.ActivityThread.currentApplication().getContentResolver();
            java.lang.String androidId = android.provider.Settings.Secure.getStringForUser(resolver, "android_id", resolver.getUserId());
            sAndroidIdHash = java.lang.Integer.valueOf(getUnsignedHashUnCached(sInjectedAndroidId != null ? sInjectedAndroidId : androidId));
        }
        return sAndroidIdHash.intValue();
    }

    static int getUnsignedHashCached(java.lang.String s) {
        synchronized (sHashCache) {
            java.lang.Integer cached = sHashCache.get(s);
            if (cached != null) {
                return cached.intValue();
            }
            int hash = getUnsignedHashUnCached(s);
            sHashCache.put(s.intern(), java.lang.Integer.valueOf(hash));
            return hash;
        }
    }

    private static int getUnsignedHashUnCached(java.lang.String s) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            return unsignedIntFromBytes(digest.digest());
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    static int unsignedIntFromBytes(byte[] longEnoughBytes) {
        return (extractByte(longEnoughBytes, 0) | extractByte(longEnoughBytes, 1) | extractByte(longEnoughBytes, 2) | extractByte(longEnoughBytes, 3)) & Integer.MAX_VALUE;
    }

    private static int extractByte(byte[] bytes, int index) {
        return (bytes[index] & 255) << (index * 8);
    }

    public static boolean shouldSamplePackageForAtom(java.lang.String packageName, float rate) {
        if (rate <= 0.0f) {
            return false;
        }
        if (rate >= 1.0f) {
            return true;
        }
        int hash = getUnsignedHashCached(packageName) ^ getAndroidIdHash();
        return ((double) hash) / 2.147483647E9d <= ((double) rate);
    }
}
