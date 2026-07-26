package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class WrappedKey {
    private static final java.lang.String APPLICATION_KEY_ALGORITHM = "AES";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final java.lang.String KEY_WRAP_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final java.lang.String TAG = "WrappedKey";
    private final byte[] mKeyMaterial;
    private final byte[] mKeyMetadata;
    private final byte[] mNonce;
    private final int mPlatformKeyGenerationId;
    private final int mRecoveryStatus;

    public static com.android.server.locksettings.recoverablekeystore.WrappedKey fromSecretKey(com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey wrappingKey, javax.crypto.SecretKey key, byte[] metadata) throws java.security.InvalidKeyException, java.security.KeyStoreException {
        if (key.getEncoded() == null) {
            throw new java.security.InvalidKeyException("key does not expose encoded material. It cannot be wrapped.");
        }
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(KEY_WRAP_CIPHER_ALGORITHM);
            cipher.init(3, wrappingKey.getKey());
            try {
                byte[] encryptedKeyMaterial = cipher.wrap(key);
                return new com.android.server.locksettings.recoverablekeystore.WrappedKey(cipher.getIV(), encryptedKeyMaterial, metadata, wrappingKey.getGenerationId(), 1);
            } catch (javax.crypto.IllegalBlockSizeException e) {
                java.lang.Throwable cause = e.getCause();
                if (cause instanceof java.security.KeyStoreException) {
                    throw ((java.security.KeyStoreException) cause);
                }
                throw new java.lang.RuntimeException("IllegalBlockSizeException should not be thrown by AES/GCM/NoPadding mode.", e);
            }
        } catch (java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException e2) {
            throw new java.lang.RuntimeException("Android does not support AES/GCM/NoPadding. This should never happen.");
        }
    }

    public WrappedKey(byte[] nonce, byte[] keyMaterial, byte[] keyMetadata, int platformKeyGenerationId) {
        this(nonce, keyMaterial, keyMetadata, platformKeyGenerationId, 1);
    }

    public WrappedKey(byte[] nonce, byte[] keyMaterial, byte[] keyMetadata, int platformKeyGenerationId, int recoveryStatus) {
        this.mNonce = nonce;
        this.mKeyMaterial = keyMaterial;
        this.mKeyMetadata = keyMetadata;
        this.mPlatformKeyGenerationId = platformKeyGenerationId;
        this.mRecoveryStatus = recoveryStatus;
    }

    public byte[] getNonce() {
        return this.mNonce;
    }

    public byte[] getKeyMaterial() {
        return this.mKeyMaterial;
    }

    public byte[] getKeyMetadata() {
        return this.mKeyMetadata;
    }

    public int getPlatformKeyGenerationId() {
        return this.mPlatformKeyGenerationId;
    }

    public int getRecoveryStatus() {
        return this.mRecoveryStatus;
    }

    public static java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> unwrapKeys(com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey platformKey, java.util.Map<java.lang.String, com.android.server.locksettings.recoverablekeystore.WrappedKey> wrappedKeys) throws javax.crypto.NoSuchPaddingException, java.security.NoSuchAlgorithmException, com.android.server.locksettings.recoverablekeystore.BadPlatformKeyException, java.security.InvalidKeyException, java.security.InvalidAlgorithmParameterException {
        java.util.HashMap<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> unwrappedKeys = new java.util.HashMap<>();
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(KEY_WRAP_CIPHER_ALGORITHM);
        int platformKeyGenerationId = platformKey.getGenerationId();
        for (java.lang.String alias : wrappedKeys.keySet()) {
            com.android.server.locksettings.recoverablekeystore.WrappedKey wrappedKey = wrappedKeys.get(alias);
            if (wrappedKey.getPlatformKeyGenerationId() != platformKeyGenerationId) {
                throw new com.android.server.locksettings.recoverablekeystore.BadPlatformKeyException(java.lang.String.format(java.util.Locale.US, "WrappedKey with alias '%s' was wrapped with platform key %d, not platform key %d", alias, java.lang.Integer.valueOf(wrappedKey.getPlatformKeyGenerationId()), java.lang.Integer.valueOf(platformKey.getGenerationId())));
            }
            cipher.init(4, platformKey.getKey(), new javax.crypto.spec.GCMParameterSpec(128, wrappedKey.getNonce()));
            try {
                javax.crypto.SecretKey key = (javax.crypto.SecretKey) cipher.unwrap(wrappedKey.getKeyMaterial(), APPLICATION_KEY_ALGORITHM, 3);
                unwrappedKeys.put(alias, android.util.Pair.create(key, wrappedKey.getKeyMetadata()));
            } catch (java.security.InvalidKeyException | java.security.NoSuchAlgorithmException e) {
                android.util.Log.e(TAG, java.lang.String.format(java.util.Locale.US, "Error unwrapping recoverable key with alias '%s'", alias), e);
            }
        }
        return unwrappedKeys;
    }
}
