package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class RebootEscrowKey {
    private static final java.lang.String KEY_ALGO = "AES";
    private static final int KEY_SIZE_BITS = 256;
    private final javax.crypto.SecretKey mKey;

    private RebootEscrowKey(javax.crypto.SecretKey key) {
        this.mKey = key;
    }

    static com.android.server.locksettings.RebootEscrowKey fromKeyBytes(byte[] keyBytes) {
        return new com.android.server.locksettings.RebootEscrowKey(new javax.crypto.spec.SecretKeySpec(keyBytes, KEY_ALGO));
    }

    static com.android.server.locksettings.RebootEscrowKey generate() throws java.io.IOException {
        try {
            javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(KEY_ALGO);
            keyGenerator.init(256, new java.security.SecureRandom());
            javax.crypto.SecretKey secretKey = keyGenerator.generateKey();
            return new com.android.server.locksettings.RebootEscrowKey(secretKey);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.io.IOException("Could not generate new secret key", e);
        }
    }

    javax.crypto.SecretKey getKey() {
        return this.mKey;
    }

    byte[] getKeyBytes() {
        return this.mKey.getEncoded();
    }
}
