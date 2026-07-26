package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
class CryptoHelper {
    private static final java.lang.String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;
    private static final java.lang.String KEY_ALGORITHM = "AES";
    private static final java.lang.String KEY_CIPHER = "cipher";
    private static final java.lang.String KEY_IV = "iv";
    private static final java.lang.String KEY_MAC = "mac";
    private static final java.lang.String MAC_ALGORITHM = "HMACSHA256";
    private static final java.lang.String TAG = "Account";
    private static com.android.server.accounts.CryptoHelper sInstance;
    private final javax.crypto.SecretKey mEncryptionKey;
    private final javax.crypto.SecretKey mMacKey;

    static synchronized com.android.server.accounts.CryptoHelper getInstance() throws java.security.NoSuchAlgorithmException {
        if (sInstance == null) {
            sInstance = new com.android.server.accounts.CryptoHelper();
        }
        return sInstance;
    }

    private CryptoHelper() throws java.security.NoSuchAlgorithmException {
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance(KEY_ALGORITHM);
        this.mEncryptionKey = kgen.generateKey();
        javax.crypto.KeyGenerator kgen2 = javax.crypto.KeyGenerator.getInstance(MAC_ALGORITHM);
        this.mMacKey = kgen2.generateKey();
    }

    android.os.Bundle encryptBundle(android.os.Bundle bundle) throws java.security.GeneralSecurityException {
        java.util.Objects.requireNonNull(bundle, "Cannot encrypt null bundle.");
        android.os.Parcel parcel = android.os.Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        byte[] clearBytes = parcel.marshall();
        parcel.recycle();
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(1, this.mEncryptionKey);
        byte[] encryptedBytes = cipher.doFinal(clearBytes);
        byte[] iv = cipher.getIV();
        byte[] mac = createMac(encryptedBytes, iv);
        android.os.Bundle encryptedBundle = new android.os.Bundle();
        encryptedBundle.putByteArray(KEY_CIPHER, encryptedBytes);
        encryptedBundle.putByteArray(KEY_MAC, mac);
        encryptedBundle.putByteArray(KEY_IV, iv);
        return encryptedBundle;
    }

    android.os.Bundle decryptBundle(android.os.Bundle bundle) throws java.security.GeneralSecurityException {
        java.util.Objects.requireNonNull(bundle, "Cannot decrypt null bundle.");
        byte[] iv = bundle.getByteArray(KEY_IV);
        byte[] encryptedBytes = bundle.getByteArray(KEY_CIPHER);
        byte[] mac = bundle.getByteArray(KEY_MAC);
        if (!verifyMac(encryptedBytes, iv, mac)) {
            android.util.Log.w(TAG, "Escrow mac mismatched!");
            return null;
        }
        javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(iv);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(2, this.mEncryptionKey, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        android.os.Parcel decryptedParcel = android.os.Parcel.obtain();
        decryptedParcel.unmarshall(decryptedBytes, 0, decryptedBytes.length);
        decryptedParcel.setDataPosition(0);
        android.os.Bundle decryptedBundle = new android.os.Bundle();
        decryptedBundle.readFromParcel(decryptedParcel);
        decryptedParcel.recycle();
        return decryptedBundle;
    }

    private boolean verifyMac(byte[] cipherArray, byte[] iv, byte[] macArray) throws java.security.GeneralSecurityException {
        if (cipherArray == null || cipherArray.length == 0 || macArray == null || macArray.length == 0) {
            if (android.util.Log.isLoggable(TAG, 2)) {
                android.util.Log.v(TAG, "Cipher or MAC is empty!");
                return false;
            }
            return false;
        }
        return constantTimeArrayEquals(macArray, createMac(cipherArray, iv));
    }

    private byte[] createMac(byte[] cipher, byte[] iv) throws java.security.GeneralSecurityException {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance(MAC_ALGORITHM);
        mac.init(this.mMacKey);
        mac.update(cipher);
        mac.update(iv);
        return mac.doFinal();
    }

    private static boolean constantTimeArrayEquals(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return a == b;
        }
        if (a.length != b.length) {
            return false;
        }
        boolean isEqual = true;
        for (int i = 0; i < b.length; i++) {
            isEqual &= a[i] == b[i];
        }
        return isEqual;
    }
}
