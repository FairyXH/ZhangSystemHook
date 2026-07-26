package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class UnifiedProfilePasswordCache {
    private static final int CACHE_TIMEOUT_SECONDS = (int) java.util.concurrent.TimeUnit.DAYS.toSeconds(7);
    private static final int KEY_LENGTH = 256;
    private static final java.lang.String TAG = "UnifiedProfilePasswordCache";
    private final android.util.SparseArray<byte[]> mEncryptedPasswords = new android.util.SparseArray<>();
    private final java.security.KeyStore mKeyStore;

    public UnifiedProfilePasswordCache(java.security.KeyStore keyStore) {
        this.mKeyStore = keyStore;
    }

    public void storePassword(int userId, com.android.internal.widget.LockscreenCredential password, long parentSid) {
        if (parentSid == 0) {
            return;
        }
        synchronized (this.mEncryptedPasswords) {
            if (this.mEncryptedPasswords.contains(userId)) {
                return;
            }
            java.lang.String keyName = getEncryptionKeyName(userId);
            try {
                javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("AES", this.mKeyStore.getProvider());
                generator.init(new android.security.keystore.KeyGenParameterSpec.Builder(keyName, 3).setKeySize(256).setBlockModes("GCM").setNamespace(com.android.server.locksettings.SyntheticPasswordCrypto.keyNamespace()).setEncryptionPaddings("NoPadding").setUserAuthenticationRequired(true).setBoundToSpecificSecureUserId(parentSid).setUserAuthenticationValidityDurationSeconds(CACHE_TIMEOUT_SECONDS).build());
                javax.crypto.SecretKey key = generator.generateKey();
                try {
                    javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(1, key);
                    byte[] ciphertext = cipher.doFinal(password.getCredential());
                    byte[] iv = cipher.getIV();
                    byte[] block = com.android.internal.util.ArrayUtils.concat(new byte[][]{iv, ciphertext});
                    this.mEncryptedPasswords.put(userId, block);
                } catch (java.security.GeneralSecurityException e) {
                    android.util.Slog.d(TAG, "Cannot encrypt", e);
                }
            } catch (java.security.GeneralSecurityException e2) {
                android.util.Slog.e(TAG, "Cannot generate key", e2);
            }
        }
    }

    public com.android.internal.widget.LockscreenCredential retrievePassword(int userId) {
        synchronized (this.mEncryptedPasswords) {
            byte[] block = this.mEncryptedPasswords.get(userId);
            if (block == null) {
                return null;
            }
            try {
                java.security.Key key = this.mKeyStore.getKey(getEncryptionKeyName(userId), null);
                if (key == null) {
                    return null;
                }
                byte[] iv = java.util.Arrays.copyOf(block, 12);
                byte[] ciphertext = java.util.Arrays.copyOfRange(block, 12, block.length);
                try {
                    javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(2, key, new javax.crypto.spec.GCMParameterSpec(128, iv));
                    byte[] credential = cipher.doFinal(ciphertext);
                    com.android.internal.widget.LockscreenCredential result = com.android.internal.widget.LockscreenCredential.createUnifiedProfilePassword(credential);
                    java.util.Arrays.fill(credential, (byte) 0);
                    return result;
                } catch (android.security.keystore.UserNotAuthenticatedException e) {
                    android.util.Slog.i(TAG, "Device not unlocked for more than 7 days");
                    return null;
                } catch (java.security.GeneralSecurityException e2) {
                    android.util.Slog.d(TAG, "Cannot decrypt", e2);
                    return null;
                }
            } catch (java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException e3) {
                android.util.Slog.d(TAG, "Cannot get key", e3);
                return null;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0036 A[Catch: all -> 0x0049, TryCatch #1 {, blocks: (B:4:0x0003, B:5:0x000b, B:7:0x0013, B:8:0x0018, B:10:0x0020, B:14:0x002e, B:16:0x0036, B:17:0x0047, B:13:0x0027), top: B:24:0x0003, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void removePassword(int r7) {
        /*
            r6 = this;
            android.util.SparseArray<byte[]> r0 = r6.mEncryptedPasswords
            monitor-enter(r0)
            java.lang.String r1 = getEncryptionKeyName(r7)     // Catch: java.lang.Throwable -> L49
            java.lang.String r2 = getLegacyEncryptionKeyName(r7)     // Catch: java.lang.Throwable -> L49
            java.security.KeyStore r3 = r6.mKeyStore     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
            boolean r3 = r3.containsAlias(r1)     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
            if (r3 == 0) goto L18
            java.security.KeyStore r3 = r6.mKeyStore     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
            r3.deleteEntry(r1)     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
        L18:
            java.security.KeyStore r3 = r6.mKeyStore     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
            boolean r3 = r3.containsAlias(r2)     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
            if (r3 == 0) goto L25
            java.security.KeyStore r3 = r6.mKeyStore     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
            r3.deleteEntry(r2)     // Catch: java.security.KeyStoreException -> L26 java.lang.Throwable -> L49
        L25:
            goto L2e
        L26:
            r3 = move-exception
            java.lang.String r4 = "UnifiedProfilePasswordCache"
            java.lang.String r5 = "Cannot delete key"
            android.util.Slog.d(r4, r5, r3)     // Catch: java.lang.Throwable -> L49
        L2e:
            android.util.SparseArray<byte[]> r3 = r6.mEncryptedPasswords     // Catch: java.lang.Throwable -> L49
            boolean r3 = r3.contains(r7)     // Catch: java.lang.Throwable -> L49
            if (r3 == 0) goto L47
            android.util.SparseArray<byte[]> r3 = r6.mEncryptedPasswords     // Catch: java.lang.Throwable -> L49
            java.lang.Object r3 = r3.get(r7)     // Catch: java.lang.Throwable -> L49
            byte[] r3 = (byte[]) r3     // Catch: java.lang.Throwable -> L49
            r4 = 0
            java.util.Arrays.fill(r3, r4)     // Catch: java.lang.Throwable -> L49
            android.util.SparseArray<byte[]> r3 = r6.mEncryptedPasswords     // Catch: java.lang.Throwable -> L49
            r3.remove(r7)     // Catch: java.lang.Throwable -> L49
        L47:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L49
            return
        L49:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L49
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.UnifiedProfilePasswordCache.removePassword(int):void");
    }

    private static java.lang.String getEncryptionKeyName(int userId) {
        return "com.android.server.locksettings.unified_profile_cache_v2_" + userId;
    }

    private static java.lang.String getLegacyEncryptionKeyName(int userId) {
        return "com.android.server.locksettings.unified_profile_cache_" + userId;
    }
}
