package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class KeySyncTask implements java.lang.Runnable {
    private static final int LENGTH_PREFIX_BYTES = 4;
    private static final java.lang.String LOCK_SCREEN_HASH_ALGORITHM = "SHA-256";
    private static final java.lang.String RECOVERY_KEY_ALGORITHM = "AES";
    private static final int RECOVERY_KEY_SIZE_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;
    static final int SCRYPT_PARAM_N = 4096;
    static final int SCRYPT_PARAM_OUTLEN_BYTES = 32;
    static final int SCRYPT_PARAM_P = 1;
    static final int SCRYPT_PARAM_R = 8;
    private static final java.lang.String TAG = "KeySyncTask";
    private static final int TRUSTED_HARDWARE_MAX_ATTEMPTS = 10;
    private final byte[] mCredential;
    private final int mCredentialType;
    private final boolean mCredentialUpdated;
    private final com.android.server.locksettings.recoverablekeystore.PlatformKeyManager mPlatformKeyManager;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb mRecoverableKeyStoreDb;
    private final com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage mRecoverySnapshotStorage;
    private final android.security.Scrypt mScrypt;
    private final com.android.server.locksettings.recoverablekeystore.RecoverySnapshotListenersStorage mSnapshotListenersStorage;
    private final com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper mTestOnlyInsecureCertificateHelper;
    private final int mUserId;

    public static com.android.server.locksettings.recoverablekeystore.KeySyncTask newInstance(android.content.Context context, com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb recoverableKeyStoreDb, com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage snapshotStorage, com.android.server.locksettings.recoverablekeystore.RecoverySnapshotListenersStorage recoverySnapshotListenersStorage, int userId, int credentialType, byte[] credential, boolean credentialUpdated) throws java.security.NoSuchAlgorithmException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException {
        return new com.android.server.locksettings.recoverablekeystore.KeySyncTask(recoverableKeyStoreDb, snapshotStorage, recoverySnapshotListenersStorage, userId, credentialType, credential, credentialUpdated, com.android.server.locksettings.recoverablekeystore.PlatformKeyManager.getInstance(context, recoverableKeyStoreDb), new com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper(), new android.security.Scrypt());
    }

    KeySyncTask(com.android.server.locksettings.recoverablekeystore.storage.RecoverableKeyStoreDb recoverableKeyStoreDb, com.android.server.locksettings.recoverablekeystore.storage.RecoverySnapshotStorage snapshotStorage, com.android.server.locksettings.recoverablekeystore.RecoverySnapshotListenersStorage recoverySnapshotListenersStorage, int userId, int credentialType, byte[] credential, boolean credentialUpdated, com.android.server.locksettings.recoverablekeystore.PlatformKeyManager platformKeyManager, com.android.server.locksettings.recoverablekeystore.TestOnlyInsecureCertificateHelper testOnlyInsecureCertificateHelper, android.security.Scrypt scrypt) {
        this.mSnapshotListenersStorage = recoverySnapshotListenersStorage;
        this.mRecoverableKeyStoreDb = recoverableKeyStoreDb;
        this.mUserId = userId;
        this.mCredentialType = credentialType;
        this.mCredential = credential != null ? java.util.Arrays.copyOf(credential, credential.length) : null;
        this.mCredentialUpdated = credentialUpdated;
        this.mPlatformKeyManager = platformKeyManager;
        this.mRecoverySnapshotStorage = snapshotStorage;
        this.mTestOnlyInsecureCertificateHelper = testOnlyInsecureCertificateHelper;
        this.mScrypt = scrypt;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                synchronized (com.android.server.locksettings.recoverablekeystore.KeySyncTask.class) {
                    syncKeys();
                }
                if (this.mCredential != null) {
                    java.util.Arrays.fill(this.mCredential, (byte) 0);
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Unexpected exception thrown during KeySyncTask", e);
                if (this.mCredential != null) {
                    java.util.Arrays.fill(this.mCredential, (byte) 0);
                }
            }
        } catch (java.lang.Throwable th) {
            if (this.mCredential != null) {
                java.util.Arrays.fill(this.mCredential, (byte) 0);
            }
            throw th;
        }
    }

    private void syncKeys() throws android.os.RemoteException {
        if (this.mCredentialUpdated && this.mRecoverableKeyStoreDb.getBadRemoteGuessCounter(this.mUserId) != 0) {
            this.mRecoverableKeyStoreDb.setBadRemoteGuessCounter(this.mUserId, 0);
        }
        int generation = this.mPlatformKeyManager.getGenerationId(this.mUserId);
        if (this.mCredentialType == -1) {
            android.util.Log.w(TAG, "Credentials are not set for user " + this.mUserId);
            if (generation < 1001000) {
                this.mPlatformKeyManager.invalidatePlatformKey(this.mUserId, generation);
                return;
            }
            return;
        }
        if (isCustomLockScreen()) {
            android.util.Log.w(TAG, "Unsupported credential type " + this.mCredentialType + " for user " + this.mUserId);
            if (generation < 1001000) {
                this.mRecoverableKeyStoreDb.invalidateKeysForUserIdOnCustomScreenLock(this.mUserId);
                return;
            }
            return;
        }
        if (this.mPlatformKeyManager.isDeviceLocked(this.mUserId) && this.mUserId == 0) {
            android.util.Log.w(TAG, "Can't sync keys for locked user " + this.mUserId);
            return;
        }
        java.util.List<java.lang.Integer> recoveryAgents = this.mRecoverableKeyStoreDb.getRecoveryAgents(this.mUserId);
        java.util.Iterator<java.lang.Integer> it = recoveryAgents.iterator();
        while (it.hasNext()) {
            int uid = it.next().intValue();
            try {
                syncKeysForAgent(uid);
            } catch (java.io.IOException e) {
                android.util.Log.e(TAG, "IOException during sync for agent " + uid, e);
            }
        }
        if (recoveryAgents.isEmpty()) {
            android.util.Log.w(TAG, "No recovery agent initialized for user " + this.mUserId);
        }
    }

    private boolean isCustomLockScreen() {
        return (this.mCredentialType == -1 || this.mCredentialType == 1 || this.mCredentialType == 3 || this.mCredentialType == 4) ? false : true;
    }

    private void syncKeysForAgent(int recoveryAgentUid) throws android.os.RemoteException, java.io.IOException {
        boolean shouldRecreateCurrentVersion;
        java.security.PublicKey publicKey;
        byte[] localLskfHash;
        java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> rawKeysWithMetadata;
        java.lang.Long counterId;
        java.lang.Long counterId2;
        android.security.keystore.recovery.KeyDerivationParams keyDerivationParams;
        if (shouldCreateSnapshot(recoveryAgentUid)) {
            shouldRecreateCurrentVersion = false;
        } else {
            boolean shouldRecreateCurrentVersion2 = this.mRecoverableKeyStoreDb.getSnapshotVersion(this.mUserId, recoveryAgentUid) != null && this.mRecoverySnapshotStorage.get(recoveryAgentUid) == null;
            if (shouldRecreateCurrentVersion2) {
                android.util.Log.d(TAG, "Recreating most recent snapshot");
                shouldRecreateCurrentVersion = shouldRecreateCurrentVersion2;
            } else {
                android.util.Log.d(TAG, "Key sync not needed.");
                return;
            }
        }
        java.lang.String rootCertAlias = this.mTestOnlyInsecureCertificateHelper.getDefaultCertificateAliasIfEmpty(this.mRecoverableKeyStoreDb.getActiveRootOfTrust(this.mUserId, recoveryAgentUid));
        java.security.cert.CertPath certPath = this.mRecoverableKeyStoreDb.getRecoveryServiceCertPath(this.mUserId, recoveryAgentUid, rootCertAlias);
        if (certPath != null) {
            android.util.Log.d(TAG, "Using the public key in stored CertPath for syncing");
            publicKey = certPath.getCertificates().get(0).getPublicKey();
        } else {
            android.util.Log.d(TAG, "Using the stored raw public key for syncing");
            publicKey = this.mRecoverableKeyStoreDb.getRecoveryServicePublicKey(this.mUserId, recoveryAgentUid);
        }
        if (publicKey != null) {
            byte[] vaultHandle = this.mRecoverableKeyStoreDb.getServerParams(this.mUserId, recoveryAgentUid);
            if (vaultHandle == null) {
                android.util.Log.w(TAG, "No device ID set for user " + this.mUserId);
                return;
            }
            if (this.mTestOnlyInsecureCertificateHelper.isTestOnlyCertificateAlias(rootCertAlias)) {
                android.util.Log.w(TAG, "Insecure root certificate is used by recovery agent " + recoveryAgentUid);
                if (this.mTestOnlyInsecureCertificateHelper.doesCredentialSupportInsecureMode(this.mCredentialType, this.mCredential)) {
                    android.util.Log.w(TAG, "Whitelisted credential is used to generate snapshot by recovery agent " + recoveryAgentUid);
                } else {
                    android.util.Log.w(TAG, "Non whitelisted credential is used to generate recovery snapshot by " + recoveryAgentUid + " - ignore attempt.");
                    return;
                }
            }
            boolean useScryptToHashCredential = shouldUseScryptToHashCredential();
            byte[] salt = generateSalt();
            if (useScryptToHashCredential) {
                localLskfHash = hashCredentialsByScrypt(salt, this.mCredential);
            } else {
                byte[] localLskfHash2 = this.mCredential;
                localLskfHash = hashCredentialsBySaltedSha256(salt, localLskfHash2);
            }
            try {
                java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> rawKeysWithMetadata2 = getKeysToSync(recoveryAgentUid);
                if (!this.mTestOnlyInsecureCertificateHelper.isTestOnlyCertificateAlias(rootCertAlias)) {
                    rawKeysWithMetadata = rawKeysWithMetadata2;
                } else {
                    rawKeysWithMetadata = this.mTestOnlyInsecureCertificateHelper.keepOnlyWhitelistedInsecureKeys(rawKeysWithMetadata2);
                }
                try {
                    javax.crypto.SecretKey recoveryKey = generateRecoveryKey();
                    try {
                        java.util.Map<java.lang.String, byte[]> encryptedApplicationKeys = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.encryptKeysWithRecoveryKey(recoveryKey, rawKeysWithMetadata);
                        if (!this.mCredentialUpdated && (counterId = this.mRecoverableKeyStoreDb.getCounterId(this.mUserId, recoveryAgentUid)) != null) {
                            counterId2 = counterId;
                        } else {
                            counterId2 = java.lang.Long.valueOf(generateAndStoreCounterId(recoveryAgentUid));
                        }
                        byte[] vaultParams = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.packVaultParams(publicKey, counterId2.longValue(), 10, vaultHandle);
                        try {
                            byte[] encryptedRecoveryKey = com.android.server.locksettings.recoverablekeystore.KeySyncUtils.thmEncryptRecoveryKey(publicKey, localLskfHash, vaultParams, recoveryKey);
                            if (useScryptToHashCredential) {
                                keyDerivationParams = android.security.keystore.recovery.KeyDerivationParams.createScryptParams(salt, 4096);
                            } else {
                                android.security.keystore.recovery.KeyDerivationParams keyDerivationParams2 = android.security.keystore.recovery.KeyDerivationParams.createSha256Params(salt);
                                keyDerivationParams = keyDerivationParams2;
                            }
                            android.security.keystore.recovery.KeyChainProtectionParams keyChainProtectionParams = new android.security.keystore.recovery.KeyChainProtectionParams.Builder().setUserSecretType(100).setLockScreenUiFormat(getUiFormat(this.mCredentialType)).setKeyDerivationParams(keyDerivationParams).setSecret(new byte[0]).build();
                            java.util.ArrayList<android.security.keystore.recovery.KeyChainProtectionParams> metadataList = new java.util.ArrayList<>();
                            metadataList.add(keyChainProtectionParams);
                            android.security.keystore.recovery.KeyChainSnapshot.Builder keyChainSnapshotBuilder = new android.security.keystore.recovery.KeyChainSnapshot.Builder().setSnapshotVersion(getSnapshotVersion(recoveryAgentUid, shouldRecreateCurrentVersion)).setMaxAttempts(10).setCounterId(counterId2.longValue()).setServerParams(vaultHandle).setKeyChainProtectionParams(metadataList).setWrappedApplicationKeys(createApplicationKeyEntries(encryptedApplicationKeys, rawKeysWithMetadata)).setEncryptedRecoveryKeyBlob(encryptedRecoveryKey);
                            try {
                                keyChainSnapshotBuilder.setTrustedHardwareCertPath(certPath);
                                this.mRecoverySnapshotStorage.put(recoveryAgentUid, keyChainSnapshotBuilder.build());
                                this.mSnapshotListenersStorage.recoverySnapshotAvailable(recoveryAgentUid);
                                this.mRecoverableKeyStoreDb.setShouldCreateSnapshot(this.mUserId, recoveryAgentUid, false);
                                return;
                            } catch (java.security.cert.CertificateException e) {
                                android.util.Log.wtf(TAG, "Cannot serialize CertPath when calling setTrustedHardwareCertPath", e);
                                return;
                            }
                        } catch (java.security.InvalidKeyException e2) {
                            android.util.Log.e(TAG, "Could not encrypt with recovery key", e2);
                            return;
                        } catch (java.security.NoSuchAlgorithmException e3) {
                            android.util.Log.wtf(TAG, "SecureBox encrypt algorithms unavailable", e3);
                            return;
                        }
                    } catch (java.security.InvalidKeyException | java.security.NoSuchAlgorithmException e4) {
                        android.util.Log.wtf(TAG, "Should be impossible: could not encrypt application keys with random key", e4);
                        return;
                    }
                } catch (java.security.NoSuchAlgorithmException e5) {
                    android.util.Log.wtf("AES should never be unavailable", e5);
                    return;
                }
            } catch (com.android.server.locksettings.recoverablekeystore.BadPlatformKeyException e6) {
                android.util.Log.e(TAG, "Loaded keys for same generation ID as platform key, so BadPlatformKeyException should be impossible.", e6);
                return;
            } catch (com.android.server.locksettings.recoverablekeystore.InsecureUserException e7) {
                android.util.Log.e(TAG, "A screen unlock triggered the key sync flow, so user must have lock screen. This should be impossible.", e7);
                return;
            } catch (java.io.IOException e8) {
                android.util.Log.e(TAG, "Local database error.", e8);
                return;
            } catch (java.security.GeneralSecurityException e9) {
                android.util.Log.e(TAG, "Failed to load recoverable keys for sync", e9);
                return;
            }
        }
        android.util.Log.w(TAG, "Not initialized for KeySync: no public key set. Cancelling task.");
    }

    int getSnapshotVersion(int recoveryAgentUid, boolean shouldRecreateCurrentVersion) throws java.io.IOException {
        java.lang.Long snapshotVersion;
        java.lang.Long snapshotVersion2 = this.mRecoverableKeyStoreDb.getSnapshotVersion(this.mUserId, recoveryAgentUid);
        if (!shouldRecreateCurrentVersion) {
            snapshotVersion = java.lang.Long.valueOf(snapshotVersion2 != null ? 1 + snapshotVersion2.longValue() : 1L);
        } else {
            snapshotVersion = java.lang.Long.valueOf(snapshotVersion2 != null ? snapshotVersion2.longValue() : 1L);
        }
        long updatedRows = this.mRecoverableKeyStoreDb.setSnapshotVersion(this.mUserId, recoveryAgentUid, snapshotVersion.longValue());
        if (updatedRows < 0) {
            android.util.Log.e(TAG, "Failed to set the snapshot version in the local DB.");
            throw new java.io.IOException("Failed to set the snapshot version in the local DB.");
        }
        return snapshotVersion.intValue();
    }

    private long generateAndStoreCounterId(int recoveryAgentUid) throws java.io.IOException {
        long counter = new java.security.SecureRandom().nextLong();
        long updatedRows = this.mRecoverableKeyStoreDb.setCounterId(this.mUserId, recoveryAgentUid, counter);
        if (updatedRows < 0) {
            android.util.Log.e(TAG, "Failed to set the snapshot version in the local DB.");
            throw new java.io.IOException("Failed to set counterId in the local DB.");
        }
        return counter;
    }

    private java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> getKeysToSync(int recoveryAgentUid) throws javax.crypto.NoSuchPaddingException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, com.android.server.locksettings.recoverablekeystore.BadPlatformKeyException, java.io.IOException, android.os.RemoteException, java.security.InvalidKeyException, java.security.KeyStoreException, com.android.server.locksettings.recoverablekeystore.InsecureUserException, java.security.InvalidAlgorithmParameterException {
        com.android.server.locksettings.recoverablekeystore.PlatformDecryptionKey decryptKey = this.mPlatformKeyManager.getDecryptKey(this.mUserId);
        java.util.Map<java.lang.String, com.android.server.locksettings.recoverablekeystore.WrappedKey> wrappedKeys = this.mRecoverableKeyStoreDb.getAllKeys(this.mUserId, recoveryAgentUid, decryptKey.getGenerationId());
        return com.android.server.locksettings.recoverablekeystore.WrappedKey.unwrapKeys(decryptKey, wrappedKeys);
    }

    private boolean shouldCreateSnapshot(int recoveryAgentUid) {
        int[] types = this.mRecoverableKeyStoreDb.getRecoverySecretTypes(this.mUserId, recoveryAgentUid);
        if (!com.android.internal.util.ArrayUtils.contains(types, 100)) {
            return false;
        }
        if (this.mCredentialUpdated && this.mRecoverableKeyStoreDb.getSnapshotVersion(this.mUserId, recoveryAgentUid) != null) {
            this.mRecoverableKeyStoreDb.setShouldCreateSnapshot(this.mUserId, recoveryAgentUid, true);
            return true;
        }
        return this.mRecoverableKeyStoreDb.getShouldCreateSnapshot(this.mUserId, recoveryAgentUid);
    }

    static int getUiFormat(int credentialType) {
        if (credentialType == 1) {
            return 3;
        }
        return credentialType == 3 ? 1 : 2;
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);
        return salt;
    }

    static byte[] hashCredentialsBySaltedSha256(byte[] salt, byte[] credentialsBytes) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(salt.length + credentialsBytes.length + 8);
        byteBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(salt.length);
        byteBuffer.put(salt);
        byteBuffer.putInt(credentialsBytes.length);
        byteBuffer.put(credentialsBytes);
        byte[] bytes = byteBuffer.array();
        try {
            byte[] hash = java.security.MessageDigest.getInstance(LOCK_SCREEN_HASH_ALGORITHM).digest(bytes);
            java.util.Arrays.fill(bytes, (byte) 0);
            return hash;
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private byte[] hashCredentialsByScrypt(byte[] salt, byte[] credentials) {
        return this.mScrypt.scrypt(credentials, salt, 4096, 8, 1, 32);
    }

    private static javax.crypto.SecretKey generateRecoveryKey() throws java.security.NoSuchAlgorithmException {
        javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(RECOVERY_KEY_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private static java.util.List<android.security.keystore.recovery.WrappedApplicationKey> createApplicationKeyEntries(java.util.Map<java.lang.String, byte[]> encryptedApplicationKeys, java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> originalKeysWithMetadata) {
        java.util.ArrayList<android.security.keystore.recovery.WrappedApplicationKey> keyEntries = new java.util.ArrayList<>();
        for (java.lang.String alias : encryptedApplicationKeys.keySet()) {
            keyEntries.add(new android.security.keystore.recovery.WrappedApplicationKey.Builder().setAlias(alias).setEncryptedKeyMaterial(encryptedApplicationKeys.get(alias)).setMetadata((byte[]) originalKeysWithMetadata.get(alias).second).build());
        }
        return keyEntries;
    }

    private boolean shouldUseScryptToHashCredential() {
        return this.mCredentialType == 4 || this.mCredentialType == 3;
    }
}
