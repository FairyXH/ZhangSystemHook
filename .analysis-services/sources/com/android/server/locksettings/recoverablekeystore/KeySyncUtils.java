package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class KeySyncUtils {
    private static final int KEY_CLAIMANT_LENGTH_BYTES = 16;
    private static final java.lang.String PUBLIC_KEY_FACTORY_ALGORITHM = "EC";
    private static final java.lang.String RECOVERY_KEY_ALGORITHM = "AES";
    private static final int RECOVERY_KEY_SIZE_BITS = 256;
    private static final byte[] THM_ENCRYPTED_RECOVERY_KEY_HEADER = "V1 THM_encrypted_recovery_key".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] LOCALLY_ENCRYPTED_RECOVERY_KEY_HEADER = "V1 locally_encrypted_recovery_key".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] ENCRYPTED_APPLICATION_KEY_HEADER = "V1 encrypted_application_key".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] RECOVERY_CLAIM_HEADER = "V1 KF_claim".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] RECOVERY_RESPONSE_HEADER = "V1 reencrypted_recovery_key".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] THM_KF_HASH_PREFIX = "THM_KF_hash".getBytes(java.nio.charset.StandardCharsets.UTF_8);

    public static byte[] thmEncryptRecoveryKey(java.security.PublicKey publicKey, byte[] lockScreenHash, byte[] vaultParams, javax.crypto.SecretKey recoveryKey) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        byte[] encryptedRecoveryKey = locallyEncryptRecoveryKey(lockScreenHash, recoveryKey);
        byte[] thmKfHash = calculateThmKfHash(lockScreenHash);
        byte[] header = com.android.internal.util.ArrayUtils.concat(new byte[][]{THM_ENCRYPTED_RECOVERY_KEY_HEADER, vaultParams});
        return com.android.security.SecureBox.encrypt(publicKey, thmKfHash, header, encryptedRecoveryKey);
    }

    public static byte[] calculateThmKfHash(byte[] lockScreenHash) throws java.security.NoSuchAlgorithmException {
        java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("SHA-256");
        messageDigest.update(THM_KF_HASH_PREFIX);
        messageDigest.update(lockScreenHash);
        return messageDigest.digest();
    }

    static byte[] locallyEncryptRecoveryKey(byte[] lockScreenHash, javax.crypto.SecretKey recoveryKey) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        return com.android.security.SecureBox.encrypt(null, lockScreenHash, LOCALLY_ENCRYPTED_RECOVERY_KEY_HEADER, recoveryKey.getEncoded());
    }

    public static javax.crypto.SecretKey generateRecoveryKey() throws java.security.NoSuchAlgorithmException {
        javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(RECOVERY_KEY_ALGORITHM);
        keyGenerator.init(256, new java.security.SecureRandom());
        return keyGenerator.generateKey();
    }

    public static java.util.Map<java.lang.String, byte[]> encryptKeysWithRecoveryKey(javax.crypto.SecretKey recoveryKey, java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> keys) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        byte[] header;
        java.util.HashMap<java.lang.String, byte[]> encryptedKeys = new java.util.HashMap<>();
        for (java.lang.String alias : keys.keySet()) {
            javax.crypto.SecretKey key = (javax.crypto.SecretKey) keys.get(alias).first;
            byte[] metadata = (byte[]) keys.get(alias).second;
            if (metadata == null) {
                header = ENCRYPTED_APPLICATION_KEY_HEADER;
            } else {
                byte[] header2 = ENCRYPTED_APPLICATION_KEY_HEADER;
                header = com.android.internal.util.ArrayUtils.concat(new byte[][]{header2, metadata});
            }
            byte[] encryptedKey = com.android.security.SecureBox.encrypt(null, recoveryKey.getEncoded(), header, key.getEncoded());
            encryptedKeys.put(alias, encryptedKey);
        }
        return encryptedKeys;
    }

    public static byte[] generateKeyClaimant() {
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return key;
    }

    public static byte[] encryptRecoveryClaim(java.security.PublicKey publicKey, byte[] vaultParams, byte[] challenge, byte[] thmKfHash, byte[] keyClaimant) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        return com.android.security.SecureBox.encrypt(publicKey, null, com.android.internal.util.ArrayUtils.concat(new byte[][]{RECOVERY_CLAIM_HEADER, vaultParams, challenge}), com.android.internal.util.ArrayUtils.concat(new byte[][]{thmKfHash, keyClaimant}));
    }

    public static byte[] decryptRecoveryClaimResponse(byte[] keyClaimant, byte[] vaultParams, byte[] encryptedResponse) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.AEADBadTagException {
        return com.android.security.SecureBox.decrypt(null, keyClaimant, com.android.internal.util.ArrayUtils.concat(new byte[][]{RECOVERY_RESPONSE_HEADER, vaultParams}), encryptedResponse);
    }

    public static byte[] decryptRecoveryKey(byte[] lskfHash, byte[] encryptedRecoveryKey) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.AEADBadTagException {
        return com.android.security.SecureBox.decrypt(null, lskfHash, LOCALLY_ENCRYPTED_RECOVERY_KEY_HEADER, encryptedRecoveryKey);
    }

    public static byte[] decryptApplicationKey(byte[] recoveryKey, byte[] encryptedApplicationKey, byte[] applicationKeyMetadata) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.AEADBadTagException {
        byte[] header;
        if (applicationKeyMetadata == null) {
            header = ENCRYPTED_APPLICATION_KEY_HEADER;
        } else {
            byte[] header2 = ENCRYPTED_APPLICATION_KEY_HEADER;
            header = com.android.internal.util.ArrayUtils.concat(new byte[][]{header2, applicationKeyMetadata});
        }
        return com.android.security.SecureBox.decrypt(null, recoveryKey, header, encryptedApplicationKey);
    }

    public static java.security.PublicKey deserializePublicKey(byte[] key) throws java.security.spec.InvalidKeySpecException {
        try {
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(PUBLIC_KEY_FACTORY_ALGORITHM);
            java.security.spec.X509EncodedKeySpec publicKeySpec = new java.security.spec.X509EncodedKeySpec(key);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public static byte[] packVaultParams(java.security.PublicKey thmPublicKey, long counterId, int maxAttempts, byte[] vaultHandle) {
        int vaultParamsLength = vaultHandle.length + 77;
        return java.nio.ByteBuffer.allocate(vaultParamsLength).order(java.nio.ByteOrder.LITTLE_ENDIAN).put(com.android.security.SecureBox.encodePublicKey(thmPublicKey)).putLong(counterId).putInt(maxAttempts).put(vaultHandle).array();
    }

    private KeySyncUtils() {
    }
}
