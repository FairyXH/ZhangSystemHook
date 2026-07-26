package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class PasswordUtils {
    public static final java.lang.String ENCRYPTION_ALGORITHM_NAME = "AES-256";
    public static final int PBKDF2_HASH_ROUNDS = 10000;
    private static final int PBKDF2_KEY_SIZE = 256;
    public static final int PBKDF2_SALT_SIZE = 512;

    public static javax.crypto.SecretKey buildPasswordKey(java.lang.String algorithm, java.lang.String pw, byte[] salt, int rounds) {
        return buildCharArrayKey(algorithm, pw.toCharArray(), salt, rounds);
    }

    public static java.lang.String buildPasswordHash(java.lang.String algorithm, java.lang.String pw, byte[] salt, int rounds) {
        javax.crypto.SecretKey key = buildPasswordKey(algorithm, pw, salt, rounds);
        if (key != null) {
            return byteArrayToHex(key.getEncoded());
        }
        return null;
    }

    public static java.lang.String byteArrayToHex(byte[] data) {
        return libcore.util.HexEncoding.encodeToString(data, true);
    }

    public static byte[] hexToByteArray(java.lang.String digits) {
        int bytes = digits.length() / 2;
        if (bytes * 2 != digits.length()) {
            throw new java.lang.IllegalArgumentException("Hex string must have an even number of digits");
        }
        byte[] result = new byte[bytes];
        for (int i = 0; i < digits.length(); i += 2) {
            result[i / 2] = (byte) java.lang.Integer.parseInt(digits.substring(i, i + 2), 16);
        }
        return result;
    }

    public static byte[] makeKeyChecksum(java.lang.String algorithm, byte[] pwBytes, byte[] salt, int rounds) {
        char[] mkAsChar = new char[pwBytes.length];
        for (int i = 0; i < pwBytes.length; i++) {
            mkAsChar[i] = (char) pwBytes[i];
        }
        java.security.Key checksum = buildCharArrayKey(algorithm, mkAsChar, salt, rounds);
        return checksum.getEncoded();
    }

    private static javax.crypto.SecretKey buildCharArrayKey(java.lang.String algorithm, char[] pwArray, byte[] salt, int rounds) {
        try {
            javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance(algorithm);
            java.security.spec.KeySpec ks = new javax.crypto.spec.PBEKeySpec(pwArray, salt, rounds, 256);
            return keyFactory.generateSecret(ks);
        } catch (java.security.NoSuchAlgorithmException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "PBKDF2 unavailable!");
            return null;
        } catch (java.security.spec.InvalidKeySpecException e2) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Invalid key spec for PBKDF2!");
            return null;
        }
    }
}
