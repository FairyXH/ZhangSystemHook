package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class RecoverableKeyGenerator {
    static final int KEY_SIZE_BITS = 256;
    private static final int RESULT_CANNOT_INSERT_ROW = -1;
    private static final java.lang.String SECRET_KEY_ALGORITHM = "AES";
    private static final java.lang.String TAG = "PlatformKeyGen";
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb mDatabase;
    private final javax.crypto.KeyGenerator mKeyGenerator;

    public static com.android.server.locksettings.recoverablekeystore.RecoverableKeyGenerator newInstance(com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb database) throws java.security.NoSuchAlgorithmException {
        javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
        return new com.android.server.locksettings.recoverablekeystore.RecoverableKeyGenerator(keyGenerator, database);
    }

    private RecoverableKeyGenerator(javax.crypto.KeyGenerator keyGenerator, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb recoverableKeyStoreDb) {
        this.mKeyGenerator = keyGenerator;
        this.mDatabase = recoverableKeyStoreDb;
    }

    public byte[] generateAndStoreKey(com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey platformKey, int userId, int uid, java.lang.String alias, byte[] metadata) throws com.android.server.locksettings.recoverablekeystore.RecoverableKeyStorageException, java.security.InvalidKeyException, java.security.KeyStoreException {
        this.mKeyGenerator.init(256);
        javax.crypto.SecretKey key = this.mKeyGenerator.generateKey();
        com.android.server.locksettings.recoverablekeystore.WrappedKey wrappedKey = com.android.server.locksettings.recoverablekeystore.WrappedKey.fromSecretKey(platformKey, key, metadata);
        long result = this.mDatabase.insertKey(userId, uid, alias, wrappedKey);
        if (result == -1) {
            throw new com.android.server.locksettings.recoverablekeystore.RecoverableKeyStorageException(java.lang.String.format(java.util.Locale.US, "Failed writing (%d, %s) to database.", java.lang.Integer.valueOf(uid), alias));
        }
        long updatedRows = this.mDatabase.setShouldCreateSnapshot(userId, uid, true);
        if (updatedRows < 0) {
            android.util.Log.e(TAG, "Failed to set the shoudCreateSnapshot flag in the local DB.");
        }
        return key.getEncoded();
    }

    public void importKey(com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey platformKey, int userId, int uid, java.lang.String alias, byte[] keyBytes, byte[] metadata) throws com.android.server.locksettings.recoverablekeystore.RecoverableKeyStorageException, java.security.InvalidKeyException, java.security.KeyStoreException {
        javax.crypto.SecretKey key = new javax.crypto.spec.SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM);
        com.android.server.locksettings.recoverablekeystore.WrappedKey wrappedKey = com.android.server.locksettings.recoverablekeystore.WrappedKey.fromSecretKey(platformKey, key, metadata);
        long result = this.mDatabase.insertKey(userId, uid, alias, wrappedKey);
        if (result == -1) {
            throw new com.android.server.locksettings.recoverablekeystore.RecoverableKeyStorageException(java.lang.String.format(java.util.Locale.US, "Failed writing (%d, %s) to database.", java.lang.Integer.valueOf(uid), alias));
        }
        this.mDatabase.setShouldCreateSnapshot(userId, uid, true);
    }
}
