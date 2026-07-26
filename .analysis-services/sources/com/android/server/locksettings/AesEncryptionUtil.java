package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class AesEncryptionUtil {
    private static final java.lang.String CIPHER_ALGO = "AES/GCM/NoPadding";

    private AesEncryptionUtil() {
    }

    static byte[] decrypt(javax.crypto.SecretKey key, java.io.DataInputStream cipherStream) throws java.io.IOException {
        java.util.Objects.requireNonNull(key);
        java.util.Objects.requireNonNull(cipherStream);
        int ivSize = cipherStream.readInt();
        if (ivSize < 0 || ivSize > 32) {
            throw new java.io.IOException("IV out of range: " + ivSize);
        }
        byte[] iv = new byte[ivSize];
        cipherStream.readFully(iv);
        int rawCipherTextSize = cipherStream.readInt();
        if (rawCipherTextSize < 0) {
            throw new java.io.IOException("Invalid cipher text size: " + rawCipherTextSize);
        }
        byte[] rawCipherText = new byte[rawCipherTextSize];
        cipherStream.readFully(rawCipherText);
        try {
            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance(CIPHER_ALGO);
            c.init(2, key, new javax.crypto.spec.GCMParameterSpec(128, iv));
            byte[] plainText = c.doFinal(rawCipherText);
            return plainText;
        } catch (java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.NoSuchAlgorithmException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
            throw new java.io.IOException("Could not decrypt cipher text", e);
        }
    }

    static byte[] decrypt(javax.crypto.SecretKey key, byte[] cipherText) throws java.io.IOException {
        java.util.Objects.requireNonNull(key);
        java.util.Objects.requireNonNull(cipherText);
        java.io.DataInputStream cipherStream = new java.io.DataInputStream(new java.io.ByteArrayInputStream(cipherText));
        return decrypt(key, cipherStream);
    }

    static byte[] encrypt(javax.crypto.SecretKey key, byte[] plainText) throws java.io.IOException {
        java.util.Objects.requireNonNull(key);
        java.util.Objects.requireNonNull(plainText);
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream dos = new java.io.DataOutputStream(bos);
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_ALGO);
            cipher.init(1, key);
            byte[] cipherText = cipher.doFinal(plainText);
            byte[] iv = cipher.getIV();
            dos.writeInt(iv.length);
            dos.write(iv);
            dos.writeInt(cipherText.length);
            dos.write(cipherText);
            return bos.toByteArray();
        } catch (java.security.InvalidKeyException | java.security.NoSuchAlgorithmException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
            throw new java.io.IOException("Could not encrypt input data", e);
        }
    }
}
