package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class SyntheticPasswordCrypto {
    private static final int AES_GCM_IV_SIZE = 12;
    private static final int AES_GCM_KEY_SIZE = 32;
    private static final int AES_GCM_TAG_SIZE = 16;
    private static final byte[] PROTECTOR_SECRET_PERSONALIZATION = "application-id".getBytes();
    private static final java.lang.String TAG = "SyntheticPasswordCrypto";
    private static final int USER_AUTHENTICATION_VALIDITY = 15;

    SyntheticPasswordCrypto() {
    }

    private static byte[] decrypt(javax.crypto.SecretKey key, byte[] blob) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, java.security.InvalidAlgorithmParameterException {
        if (blob == null) {
            return null;
        }
        byte[] iv = java.util.Arrays.copyOfRange(blob, 0, 12);
        byte[] ciphertext = java.util.Arrays.copyOfRange(blob, 12, blob.length);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(2, key, new javax.crypto.spec.GCMParameterSpec(128, iv));
        return cipher.doFinal(ciphertext);
    }

    private static byte[] encrypt(javax.crypto.SecretKey key, byte[] blob) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.spec.InvalidParameterSpecException, java.security.InvalidKeyException, java.io.IOException {
        if (blob == null) {
            return null;
        }
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(1, key);
        byte[] ciphertext = cipher.doFinal(blob);
        byte[] iv = cipher.getIV();
        if (iv.length != 12) {
            throw new java.lang.IllegalArgumentException("Invalid iv length: " + iv.length + " bytes");
        }
        javax.crypto.spec.GCMParameterSpec spec = (javax.crypto.spec.GCMParameterSpec) cipher.getParameters().getParameterSpec(javax.crypto.spec.GCMParameterSpec.class);
        if (spec.getTLen() != 128) {
            throw new java.lang.IllegalArgumentException("Invalid tag length: " + spec.getTLen() + " bits");
        }
        return com.android.internal.util.ArrayUtils.concat(new byte[][]{iv, ciphertext});
    }

    public static byte[] encrypt(byte[] keyBytes, byte[] personalization, byte[] message) {
        byte[] keyHash = personalizedHash(personalization, keyBytes);
        javax.crypto.spec.SecretKeySpec key = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(keyHash, 32), "AES");
        try {
            return encrypt(key, message);
        } catch (java.io.IOException | java.security.InvalidKeyException | java.security.NoSuchAlgorithmException | java.security.spec.InvalidParameterSpecException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
            android.util.Slog.e(TAG, "Failed to encrypt", e);
            return null;
        }
    }

    public static byte[] decrypt(byte[] keyBytes, byte[] personalization, byte[] ciphertext) {
        byte[] keyHash = personalizedHash(personalization, keyBytes);
        javax.crypto.spec.SecretKeySpec key = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(keyHash, 32), "AES");
        try {
            return decrypt(key, ciphertext);
        } catch (java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.NoSuchAlgorithmException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
            android.util.Slog.e(TAG, "Failed to decrypt", e);
            return null;
        }
    }

    public static byte[] decryptBlobV1(java.lang.String protectorKeyAlias, byte[] blob, byte[] protectorSecret) {
        try {
            java.security.KeyStore keyStore = getKeyStore();
            javax.crypto.SecretKey protectorKey = (javax.crypto.SecretKey) keyStore.getKey(protectorKeyAlias, null);
            if (protectorKey == null) {
                throw new java.lang.IllegalStateException("SP protector key is missing: " + protectorKeyAlias);
            }
            byte[] intermediate = decrypt(protectorSecret, PROTECTOR_SECRET_PERSONALIZATION, blob);
            return decrypt(protectorKey, intermediate);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to decrypt V1 blob", e);
            throw new java.lang.IllegalStateException("Failed to decrypt blob", e);
        }
    }

    static java.lang.String androidKeystoreProviderName() {
        return com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl.ANDROID_KEY_STORE_PROVIDER;
    }

    static int keyNamespace() {
        return 103;
    }

    private static java.security.KeyStore getKeyStore() throws java.security.NoSuchAlgorithmException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException {
        java.security.KeyStore keyStore = java.security.KeyStore.getInstance(androidKeystoreProviderName());
        keyStore.load(new android.security.keystore2.AndroidKeyStoreLoadStoreParameter(keyNamespace()));
        return keyStore;
    }

    public static byte[] decryptBlob(java.lang.String protectorKeyAlias, byte[] blob, byte[] protectorSecret) {
        try {
            java.security.KeyStore keyStore = getKeyStore();
            javax.crypto.SecretKey protectorKey = (javax.crypto.SecretKey) keyStore.getKey(protectorKeyAlias, null);
            if (protectorKey == null) {
                throw new java.lang.IllegalStateException("SP protector key is missing: " + protectorKeyAlias);
            }
            byte[] intermediate = decrypt(protectorKey, blob);
            return decrypt(protectorSecret, PROTECTOR_SECRET_PERSONALIZATION, intermediate);
        } catch (java.io.IOException | java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException | java.security.cert.CertificateException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
            android.util.Slog.e(TAG, "Failed to decrypt blob", e);
            throw new java.lang.IllegalStateException("Failed to decrypt blob", e);
        }
    }

    public static byte[] createBlob(java.lang.String protectorKeyAlias, byte[] data, byte[] protectorSecret, long sid) {
        try {
            javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance("AES");
            keyGenerator.init(256, new java.security.SecureRandom());
            javax.crypto.SecretKey protectorKey = keyGenerator.generateKey();
            java.security.KeyStore keyStore = getKeyStore();
            android.security.keystore.KeyProtection.Builder builder = new android.security.keystore.KeyProtection.Builder(2).setBlockModes("GCM").setEncryptionPaddings("NoPadding").setCriticalToDeviceEncryption(true);
            if (sid != 0) {
                builder.setUserAuthenticationRequired(true).setBoundToSpecificSecureUserId(sid).setUserAuthenticationValidityDurationSeconds(15);
            }
            android.security.keystore.KeyProtection protNonRollbackResistant = builder.build();
            builder.setRollbackResistant(true);
            android.security.keystore.KeyProtection protRollbackResistant = builder.build();
            java.security.KeyStore.SecretKeyEntry entry = new java.security.KeyStore.SecretKeyEntry(protectorKey);
            try {
                keyStore.setEntry(protectorKeyAlias, entry, protRollbackResistant);
                android.util.Slog.i(TAG, "Using rollback-resistant key");
            } catch (java.security.KeyStoreException e) {
                android.util.Slog.w(TAG, "Rollback-resistant keys unavailable.  Falling back to non-rollback-resistant key");
                keyStore.setEntry(protectorKeyAlias, entry, protNonRollbackResistant);
            }
            byte[] intermediate = encrypt(protectorSecret, PROTECTOR_SECRET_PERSONALIZATION, data);
            return encrypt(protectorKey, intermediate);
        } catch (java.io.IOException | java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.cert.CertificateException | java.security.spec.InvalidParameterSpecException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e2) {
            android.util.Slog.e(TAG, "Failed to create blob", e2);
            throw new java.lang.IllegalStateException("Failed to encrypt blob", e2);
        }
    }

    public static void destroyProtectorKey(java.lang.String keyAlias) {
        try {
            java.security.KeyStore keyStore = getKeyStore();
            keyStore.deleteEntry(keyAlias);
            android.util.Slog.i(TAG, "Deleted SP protector key " + keyAlias);
        } catch (java.io.IOException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.cert.CertificateException e) {
            android.util.Slog.e(TAG, "Failed to delete SP protector key " + keyAlias, e);
        }
    }

    protected static byte[] personalizedHash(byte[] personalization, byte[]... message) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            if (personalization.length > 128) {
                throw new java.lang.IllegalArgumentException("Personalization too long");
            }
            digest.update(java.util.Arrays.copyOf(personalization, 128));
            for (byte[] data : message) {
                digest.update(data);
            }
            return digest.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.IllegalStateException("NoSuchAlgorithmException for SHA-512", e);
        }
    }

    static boolean migrateLockSettingsKey(java.lang.String alias) {
        android.system.keystore2.KeyDescriptor legacyKey = new android.system.keystore2.KeyDescriptor();
        legacyKey.domain = 0;
        legacyKey.nspace = -1L;
        legacyKey.alias = alias;
        android.system.keystore2.KeyDescriptor newKey = new android.system.keystore2.KeyDescriptor();
        newKey.domain = 2;
        newKey.nspace = keyNamespace();
        newKey.alias = alias;
        android.util.Slog.i(TAG, "Migrating key " + alias);
        int err = android.security.AndroidKeyStoreMaintenance.migrateKeyNamespace(legacyKey, newKey);
        if (err == 0) {
            return true;
        }
        if (err == 7) {
            android.util.Slog.i(TAG, "Key does not exist");
            return true;
        }
        if (err == 20) {
            android.util.Slog.i(TAG, "Key already exists");
            return true;
        }
        android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to migrate key: %d", new java.lang.Object[]{java.lang.Integer.valueOf(err)}));
        return false;
    }
}
