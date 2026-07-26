package com.android.security;

/* JADX INFO: loaded from: classes.dex */
public class SecureBox {
    private static final java.lang.String CIPHER_ALG = "AES";
    private static final java.lang.String EC_ALG = "EC";
    private static final int EC_COORDINATE_LEN_BYTES = 32;
    private static final java.lang.String EC_P256_COMMON_NAME = "secp256r1";
    private static final java.lang.String EC_P256_OPENSSL_NAME = "prime256v1";
    static final java.security.spec.ECParameterSpec EC_PARAM_SPEC;
    private static final int EC_PUBLIC_KEY_LEN_BYTES = 65;
    private static final byte EC_PUBLIC_KEY_PREFIX = 4;
    private static final java.lang.String ENC_ALG = "AES/GCM/NoPadding";
    private static final int GCM_KEY_LEN_BYTES = 16;
    private static final int GCM_NONCE_LEN_BYTES = 12;
    private static final int GCM_TAG_LEN_BYTES = 16;
    private static final java.lang.String KA_ALG = "ECDH";
    private static final java.lang.String MAC_ALG = "HmacSHA256";
    private static final byte[] VERSION = {2, 0};
    private static final byte[] HKDF_SALT = com.android.internal.util.ArrayUtils.concat(new byte[][]{"SECUREBOX".getBytes(java.nio.charset.StandardCharsets.UTF_8), VERSION});
    private static final byte[] HKDF_INFO_WITH_PUBLIC_KEY = "P256 HKDF-SHA-256 AES-128-GCM".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] HKDF_INFO_WITHOUT_PUBLIC_KEY = "SHARED HKDF-SHA-256 AES-128-GCM".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final byte[] CONSTANT_01 = {1};
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final java.math.BigInteger BIG_INT_02 = java.math.BigInteger.valueOf(2);
    private static final java.math.BigInteger EC_PARAM_P = new java.math.BigInteger("ffffffff00000001000000000000000000000000ffffffffffffffffffffffff", 16);
    private static final java.math.BigInteger EC_PARAM_A = EC_PARAM_P.subtract(new java.math.BigInteger("3"));
    private static final java.math.BigInteger EC_PARAM_B = new java.math.BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16);

    private enum AesGcmOperation {
        ENCRYPT,
        DECRYPT
    }

    static {
        java.security.spec.EllipticCurve curveSpec = new java.security.spec.EllipticCurve(new java.security.spec.ECFieldFp(EC_PARAM_P), EC_PARAM_A, EC_PARAM_B);
        java.security.spec.ECPoint generator = new java.security.spec.ECPoint(new java.math.BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16), new java.math.BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16));
        java.math.BigInteger generatorOrder = new java.math.BigInteger("ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551", 16);
        EC_PARAM_SPEC = new java.security.spec.ECParameterSpec(curveSpec, generator, generatorOrder, 1);
    }

    private SecureBox() {
    }

    public static java.security.KeyPair genKeyPair() throws java.security.NoSuchAlgorithmException {
        java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator.getInstance(EC_ALG);
        try {
            keyPairGenerator.initialize(new java.security.spec.ECGenParameterSpec(EC_P256_OPENSSL_NAME));
            return keyPairGenerator.generateKeyPair();
        } catch (java.security.InvalidAlgorithmParameterException e) {
            try {
                keyPairGenerator.initialize(new java.security.spec.ECGenParameterSpec(EC_P256_COMMON_NAME));
                return keyPairGenerator.generateKeyPair();
            } catch (java.security.InvalidAlgorithmParameterException ex) {
                throw new java.security.NoSuchAlgorithmException("Unable to find the NIST P-256 curve", ex);
            }
        }
    }

    public static byte[] encrypt(java.security.PublicKey theirPublicKey, byte[] sharedSecret, byte[] header, byte[] payload) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        java.security.KeyPair senderKeyPair;
        byte[] dhSecret;
        byte[] hkdfInfo;
        byte[] sharedSecret2 = emptyByteArrayIfNull(sharedSecret);
        if (theirPublicKey == null && sharedSecret2.length == 0) {
            throw new java.lang.IllegalArgumentException("Both the public key and shared secret are empty");
        }
        byte[] header2 = emptyByteArrayIfNull(header);
        byte[] payload2 = emptyByteArrayIfNull(payload);
        if (theirPublicKey == null) {
            senderKeyPair = null;
            dhSecret = EMPTY_BYTE_ARRAY;
            hkdfInfo = HKDF_INFO_WITHOUT_PUBLIC_KEY;
        } else {
            senderKeyPair = genKeyPair();
            dhSecret = dhComputeSecret(senderKeyPair.getPrivate(), theirPublicKey);
            hkdfInfo = HKDF_INFO_WITH_PUBLIC_KEY;
        }
        byte[] randNonce = genRandomNonce();
        byte[] keyingMaterial = com.android.internal.util.ArrayUtils.concat(new byte[][]{dhSecret, sharedSecret2});
        javax.crypto.SecretKey encryptionKey = hkdfDeriveKey(keyingMaterial, HKDF_SALT, hkdfInfo);
        byte[] ciphertext = aesGcmEncrypt(encryptionKey, randNonce, payload2, header2);
        if (senderKeyPair == null) {
            return com.android.internal.util.ArrayUtils.concat(new byte[][]{VERSION, randNonce, ciphertext});
        }
        return com.android.internal.util.ArrayUtils.concat(new byte[][]{VERSION, encodePublicKey(senderKeyPair.getPublic()), randNonce, ciphertext});
    }

    public static byte[] decrypt(java.security.PrivateKey ourPrivateKey, byte[] sharedSecret, byte[] header, byte[] encryptedPayload) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.AEADBadTagException {
        byte[] senderPublicKeyBytes;
        byte[] dhSecret;
        byte[] sharedSecret2 = emptyByteArrayIfNull(sharedSecret);
        if (ourPrivateKey == null && sharedSecret2.length == 0) {
            throw new java.lang.IllegalArgumentException("Both the private key and shared secret are empty");
        }
        byte[] header2 = emptyByteArrayIfNull(header);
        if (encryptedPayload == null) {
            throw new java.lang.NullPointerException("Encrypted payload must not be null.");
        }
        java.nio.ByteBuffer ciphertextBuffer = java.nio.ByteBuffer.wrap(encryptedPayload);
        byte[] version = readEncryptedPayload(ciphertextBuffer, VERSION.length);
        if (!java.util.Arrays.equals(version, VERSION)) {
            throw new javax.crypto.AEADBadTagException("The payload was not encrypted by SecureBox v2");
        }
        if (ourPrivateKey == null) {
            senderPublicKeyBytes = EMPTY_BYTE_ARRAY;
            dhSecret = HKDF_INFO_WITHOUT_PUBLIC_KEY;
        } else {
            byte[] senderPublicKeyBytes2 = readEncryptedPayload(ciphertextBuffer, 65);
            byte[] dhSecret2 = dhComputeSecret(ourPrivateKey, decodePublicKey(senderPublicKeyBytes2));
            senderPublicKeyBytes = dhSecret2;
            dhSecret = HKDF_INFO_WITH_PUBLIC_KEY;
        }
        byte[] randNonce = readEncryptedPayload(ciphertextBuffer, 12);
        byte[] ciphertext = readEncryptedPayload(ciphertextBuffer, ciphertextBuffer.remaining());
        byte[] keyingMaterial = com.android.internal.util.ArrayUtils.concat(new byte[][]{senderPublicKeyBytes, sharedSecret2});
        javax.crypto.SecretKey decryptionKey = hkdfDeriveKey(keyingMaterial, HKDF_SALT, dhSecret);
        return aesGcmDecrypt(decryptionKey, randNonce, ciphertext, header2);
    }

    private static byte[] readEncryptedPayload(java.nio.ByteBuffer buffer, int length) throws javax.crypto.AEADBadTagException {
        byte[] output = new byte[length];
        try {
            buffer.get(output);
            return output;
        } catch (java.nio.BufferUnderflowException e) {
            throw new javax.crypto.AEADBadTagException("The encrypted payload is too short");
        }
    }

    private static byte[] dhComputeSecret(java.security.PrivateKey ourPrivateKey, java.security.PublicKey theirPublicKey) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        javax.crypto.KeyAgreement agreement = javax.crypto.KeyAgreement.getInstance(KA_ALG);
        try {
            agreement.init(ourPrivateKey);
            agreement.doPhase(theirPublicKey, true);
            return agreement.generateSecret();
        } catch (java.lang.RuntimeException ex) {
            throw new java.security.InvalidKeyException(ex);
        }
    }

    private static javax.crypto.SecretKey hkdfDeriveKey(byte[] secret, byte[] salt, byte[] info) throws java.security.NoSuchAlgorithmException {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance(MAC_ALG);
        try {
            mac.init(new javax.crypto.spec.SecretKeySpec(salt, MAC_ALG));
            byte[] pseudorandomKey = mac.doFinal(secret);
            try {
                mac.init(new javax.crypto.spec.SecretKeySpec(pseudorandomKey, MAC_ALG));
                mac.update(info);
                byte[] hkdfOutput = mac.doFinal(CONSTANT_01);
                return new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(hkdfOutput, 16), CIPHER_ALG);
            } catch (java.security.InvalidKeyException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        } catch (java.security.InvalidKeyException ex2) {
            throw new java.lang.RuntimeException(ex2);
        }
    }

    private static byte[] aesGcmEncrypt(javax.crypto.SecretKey key, byte[] nonce, byte[] plaintext, byte[] aad) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        try {
            return aesGcmInternal(com.android.security.SecureBox.AesGcmOperation.ENCRYPT, key, nonce, plaintext, aad);
        } catch (javax.crypto.AEADBadTagException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }

    private static byte[] aesGcmDecrypt(javax.crypto.SecretKey key, byte[] nonce, byte[] ciphertext, byte[] aad) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.AEADBadTagException {
        return aesGcmInternal(com.android.security.SecureBox.AesGcmOperation.DECRYPT, key, nonce, ciphertext, aad);
    }

    private static byte[] aesGcmInternal(com.android.security.SecureBox.AesGcmOperation operation, javax.crypto.SecretKey key, byte[] nonce, byte[] text, byte[] aad) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.AEADBadTagException {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ENC_ALG);
            javax.crypto.spec.GCMParameterSpec spec = new javax.crypto.spec.GCMParameterSpec(128, nonce);
            try {
                if (operation == com.android.security.SecureBox.AesGcmOperation.DECRYPT) {
                    cipher.init(2, key, spec);
                } else {
                    cipher.init(1, key, spec);
                }
                try {
                    cipher.updateAAD(aad);
                    return cipher.doFinal(text);
                } catch (javax.crypto.AEADBadTagException ex) {
                    throw ex;
                } catch (javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException ex2) {
                    throw new java.lang.RuntimeException(ex2);
                }
            } catch (java.security.InvalidAlgorithmParameterException ex3) {
                throw new java.lang.RuntimeException(ex3);
            }
        } catch (javax.crypto.NoSuchPaddingException ex4) {
            throw new java.lang.RuntimeException(ex4);
        }
    }

    public static byte[] encodePublicKey(java.security.PublicKey publicKey) {
        java.security.spec.ECPoint point = ((java.security.interfaces.ECPublicKey) publicKey).getW();
        byte[] x = point.getAffineX().toByteArray();
        byte[] y = point.getAffineY().toByteArray();
        byte[] output = new byte[65];
        java.lang.System.arraycopy(y, 0, output, 65 - y.length, y.length);
        java.lang.System.arraycopy(x, 0, output, 33 - x.length, x.length);
        output[0] = 4;
        return output;
    }

    public static java.security.PublicKey decodePublicKey(byte[] keyBytes) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {
        java.math.BigInteger x = new java.math.BigInteger(1, java.util.Arrays.copyOfRange(keyBytes, 1, 33));
        java.math.BigInteger y = new java.math.BigInteger(1, java.util.Arrays.copyOfRange(keyBytes, 33, 65));
        validateEcPoint(x, y);
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(EC_ALG);
        try {
            return keyFactory.generatePublic(new java.security.spec.ECPublicKeySpec(new java.security.spec.ECPoint(x, y), EC_PARAM_SPEC));
        } catch (java.security.spec.InvalidKeySpecException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }

    private static void validateEcPoint(java.math.BigInteger x, java.math.BigInteger y) throws java.security.InvalidKeyException {
        if (x.compareTo(EC_PARAM_P) >= 0 || y.compareTo(EC_PARAM_P) >= 0 || x.signum() == -1 || y.signum() == -1) {
            throw new java.security.InvalidKeyException("Point lies outside of the expected curve");
        }
        java.math.BigInteger lhs = y.modPow(BIG_INT_02, EC_PARAM_P);
        java.math.BigInteger rhs = x.modPow(BIG_INT_02, EC_PARAM_P).add(EC_PARAM_A).mod(EC_PARAM_P).multiply(x).add(EC_PARAM_B).mod(EC_PARAM_P);
        if (!lhs.equals(rhs)) {
            throw new java.security.InvalidKeyException("Point lies outside of the expected curve");
        }
    }

    private static byte[] genRandomNonce() throws java.security.NoSuchAlgorithmException {
        byte[] nonce = new byte[12];
        new java.security.SecureRandom().nextBytes(nonce);
        return nonce;
    }

    private static byte[] emptyByteArrayIfNull(byte[] input) {
        return input == null ? EMPTY_BYTE_ARRAY : input;
    }
}
