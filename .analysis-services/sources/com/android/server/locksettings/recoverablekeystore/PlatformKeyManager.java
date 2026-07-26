package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class PlatformKeyManager {
    private static final java.lang.String DECRYPT_KEY_ALIAS_SUFFIX = "decrypt";
    private static final java.lang.String ENCRYPT_KEY_ALIAS_SUFFIX = "encrypt";
    private static final byte[] GCM_INSECURE_NONCE_BYTES = new byte[12];
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final java.lang.String KEY_ALGORITHM = "AES";
    private static final java.lang.String KEY_ALIAS_PREFIX = "com.android.server.locksettings.recoverablekeystore/platform/";
    private static final int KEY_SIZE_BITS = 256;
    private static final java.lang.String KEY_WRAP_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    static final int MIN_GENERATION_ID_FOR_UNLOCKED_DEVICE_REQUIRED = 1001000;
    private static final java.lang.String TAG = "PlatformKeyManager";
    private final android.content.Context mContext;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb mDatabase;
    private final com.android.server.locksettings.recoverablekeystore.KeyStoreProxy mKeyStore;

    public static com.android.server.locksettings.recoverablekeystore.PlatformKeyManager getInstance(android.content.Context context, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb database) throws java.security.NoSuchAlgorithmException, java.security.KeyStoreException {
        return new com.android.server.locksettings.recoverablekeystore.PlatformKeyManager(context.getApplicationContext(), new com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl(getAndLoadAndroidKeyStore()), database);
    }

    PlatformKeyManager(android.content.Context context, com.android.server.locksettings.recoverablekeystore.KeyStoreProxy keyStore, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb database) {
        this.mKeyStore = keyStore;
        this.mContext = context;
        this.mDatabase = database;
    }

    public int getGenerationId(int userId) {
        return this.mDatabase.getPlatformKeyGenerationId(userId);
    }

    public boolean isDeviceLocked(int userId) {
        return ((android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class)).isDeviceLocked(userId);
    }

    public void invalidatePlatformKey(int userId, int generationId) {
        if (generationId != -1) {
            try {
                this.mKeyStore.deleteEntry(getEncryptAlias(userId, generationId));
                this.mKeyStore.deleteEntry(getDecryptAlias(userId, generationId));
            } catch (java.security.KeyStoreException e) {
            }
        }
    }

    void regenerate(int userId) throws java.security.NoSuchAlgorithmException, java.io.IOException, android.os.RemoteException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException {
        int nextId;
        int generationId = getGenerationId(userId);
        if (generationId == -1) {
            nextId = 1;
        } else {
            invalidatePlatformKey(userId, generationId);
            nextId = generationId + 1;
        }
        generateAndLoadKey(userId, nextId);
    }

    public com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey getEncryptKey(int userId) throws java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.io.IOException, android.os.RemoteException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException {
        init(userId);
        try {
            getDecryptKeyInternal(userId);
            return getEncryptKeyInternal(userId);
        } catch (java.security.UnrecoverableKeyException e) {
            android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Regenerating permanently invalid Platform key for user %d.", java.lang.Integer.valueOf(userId)));
            regenerate(userId);
            return getEncryptKeyInternal(userId);
        }
    }

    private com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey getEncryptKeyInternal(int userId) throws java.security.UnrecoverableKeyException, java.security.NoSuchAlgorithmException, java.security.KeyStoreException {
        int generationId = getGenerationId(userId);
        java.lang.String alias = getEncryptAlias(userId, generationId);
        if (!isKeyLoaded(userId, generationId)) {
            throw new java.security.UnrecoverableKeyException("KeyStore doesn't contain key " + alias);
        }
        javax.crypto.SecretKey key = (javax.crypto.SecretKey) this.mKeyStore.getKey(alias, null);
        return new com.android.server.locksettings.recoverablekeystore.PlatformEncryptionKey(generationId, key);
    }

    public com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey getDecryptKey(int userId) throws java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.io.IOException, android.os.RemoteException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException {
        init(userId);
        try {
            com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey decryptionKey = getDecryptKeyInternal(userId);
            ensureDecryptionKeyIsValid(userId, decryptionKey);
            return decryptionKey;
        } catch (java.security.UnrecoverableKeyException e) {
            android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Regenerating permanently invalid Platform key for user %d.", java.lang.Integer.valueOf(userId)));
            regenerate(userId);
            return getDecryptKeyInternal(userId);
        }
    }

    private com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey getDecryptKeyInternal(int userId) throws java.security.UnrecoverableKeyException, java.security.NoSuchAlgorithmException, java.security.KeyStoreException {
        int generationId = getGenerationId(userId);
        java.lang.String alias = getDecryptAlias(userId, generationId);
        if (!isKeyLoaded(userId, generationId)) {
            throw new java.security.UnrecoverableKeyException("KeyStore doesn't contain key " + alias);
        }
        javax.crypto.SecretKey key = (javax.crypto.SecretKey) this.mKeyStore.getKey(alias, null);
        return new com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey(generationId, key);
    }

    private void ensureDecryptionKeyIsValid(int userId, com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey decryptionKey) throws java.security.UnrecoverableKeyException {
        try {
            javax.crypto.Cipher.getInstance(KEY_WRAP_CIPHER_ALGORITHM).init(4, decryptionKey.getKey(), new javax.crypto.spec.GCMParameterSpec(128, GCM_INSECURE_NONCE_BYTES));
        } catch (android.security.keystore.KeyPermanentlyInvalidatedException e) {
            android.util.Log.e(TAG, java.lang.String.format(java.util.Locale.US, "The platform key for user %d became invalid.", java.lang.Integer.valueOf(userId)));
            throw new java.security.UnrecoverableKeyException(e.getMessage());
        } catch (java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException e2) {
        }
    }

    void init(int userId) throws java.security.NoSuchAlgorithmException, java.io.IOException, android.os.RemoteException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException {
        int generationId;
        int generationId2 = getGenerationId(userId);
        if (isKeyLoaded(userId, generationId2)) {
            android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Platform key generation %d exists already.", java.lang.Integer.valueOf(generationId2)));
            return;
        }
        if (generationId2 == -1) {
            android.util.Log.i(TAG, "Generating initial platform key generation ID.");
            generationId = 1;
        } else {
            android.util.Log.w(TAG, java.lang.String.format(java.util.Locale.US, "Platform generation ID was %d but no entry was present in AndroidKeyStore. Generating fresh key.", java.lang.Integer.valueOf(generationId2)));
            generationId = generationId2 + 1;
        }
        generateAndLoadKey(userId, java.lang.Math.max(generationId, MIN_GENERATION_ID_FOR_UNLOCKED_DEVICE_REQUIRED));
    }

    private java.lang.String getEncryptAlias(int userId, int generationId) {
        return KEY_ALIAS_PREFIX + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + generationId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + ENCRYPT_KEY_ALIAS_SUFFIX;
    }

    private java.lang.String getDecryptAlias(int userId, int generationId) {
        return KEY_ALIAS_PREFIX + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + generationId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + DECRYPT_KEY_ALIAS_SUFFIX;
    }

    private void setGenerationId(int userId, int generationId) throws java.io.IOException {
        this.mDatabase.setPlatformKeyGenerationId(userId, generationId);
    }

    private boolean isKeyLoaded(int userId, int generationId) throws java.security.KeyStoreException {
        return this.mKeyStore.containsAlias(getEncryptAlias(userId, generationId)) && this.mKeyStore.containsAlias(getDecryptAlias(userId, generationId));
    }

    android.service.gatekeeper.IGateKeeperService getGateKeeperService() {
        return android.security.GateKeeper.getService();
    }

    private void generateAndLoadKey(int userId, int generationId) throws java.security.NoSuchAlgorithmException, java.io.IOException, android.os.RemoteException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException {
        java.lang.String encryptAlias = getEncryptAlias(userId, generationId);
        java.lang.String decryptAlias = getDecryptAlias(userId, generationId);
        javax.crypto.SecretKey secretKey = generateAesKey();
        android.security.keystore.KeyProtection.Builder decryptionKeyProtection = new android.security.keystore.KeyProtection.Builder(2).setBlockModes("GCM").setEncryptionPaddings("NoPadding");
        if (userId == 0) {
            decryptionKeyProtection.setUnlockedDeviceRequired(true);
        }
        try {
            this.mKeyStore.setEntry(decryptAlias, new java.security.KeyStore.SecretKeyEntry(secretKey), decryptionKeyProtection.build());
            this.mKeyStore.setEntry(encryptAlias, new java.security.KeyStore.SecretKeyEntry(secretKey), new android.security.keystore.KeyProtection.Builder(1).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
            setGenerationId(userId, generationId);
        } catch (java.security.KeyStoreException e) {
            if (!isDeviceSecure(userId)) {
                throw new com.android.server.locksettings.recoverablekeystore.InsecureUserException("Screenlock is not set");
            }
            throw e;
        }
    }

    private static javax.crypto.SecretKey generateAesKey() throws java.security.NoSuchAlgorithmException {
        javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private static java.security.KeyStore getAndLoadAndroidKeyStore() throws java.security.KeyStoreException {
        java.security.KeyStore keyStore = java.security.KeyStore.getInstance(com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl.ANDROID_KEY_STORE_PROVIDER);
        try {
            keyStore.load(null);
            return keyStore;
        } catch (java.io.IOException | java.security.NoSuchAlgorithmException | java.security.cert.CertificateException e) {
            throw new java.security.KeyStoreException("Unable to load keystore.", e);
        }
    }

    private boolean isDeviceSecure(int userId) {
        return ((android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class)).isDeviceSecure(userId);
    }
}
