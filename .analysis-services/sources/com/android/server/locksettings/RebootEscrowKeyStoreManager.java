package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class RebootEscrowKeyStoreManager {
    private static final java.lang.String ANDROID_KEY_STORE_PROVIDER = "AndroidKeystore";
    public static final int KEY_LENGTH = 256;
    private static final int KEY_STORE_NAMESPACE = 120;
    public static final java.lang.String REBOOT_ESCROW_KEY_STORE_ENCRYPTION_KEY_NAME = "reboot_escrow_key_store_encryption_key";
    private static final java.lang.String TAG = "RebootEscrowKeyStoreManager";
    private final java.lang.Object mKeyStoreLock = new java.lang.Object();

    private javax.crypto.SecretKey getKeyStoreEncryptionKeyLocked() {
        try {
            java.security.KeyStore keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
            keyStore.load(new android.security.keystore2.AndroidKeyStoreLoadStoreParameter(120));
            return (javax.crypto.SecretKey) keyStore.getKey(REBOOT_ESCROW_KEY_STORE_ENCRYPTION_KEY_NAME, null);
        } catch (java.io.IOException | java.security.GeneralSecurityException e) {
            android.util.Slog.e(TAG, "Unable to get encryption key from keystore.", e);
            return null;
        }
    }

    protected javax.crypto.SecretKey getKeyStoreEncryptionKey() {
        javax.crypto.SecretKey keyStoreEncryptionKeyLocked;
        synchronized (this.mKeyStoreLock) {
            keyStoreEncryptionKeyLocked = getKeyStoreEncryptionKeyLocked();
        }
        return keyStoreEncryptionKeyLocked;
    }

    protected void clearKeyStoreEncryptionKey() {
        synchronized (this.mKeyStoreLock) {
            try {
                java.security.KeyStore keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
                keyStore.load(new android.security.keystore2.AndroidKeyStoreLoadStoreParameter(120));
                keyStore.deleteEntry(REBOOT_ESCROW_KEY_STORE_ENCRYPTION_KEY_NAME);
            } catch (java.io.IOException | java.security.GeneralSecurityException e) {
                android.util.Slog.e(TAG, "Unable to delete encryption key in keystore.", e);
            }
        }
    }

    protected javax.crypto.SecretKey generateKeyStoreEncryptionKeyIfNeeded() {
        synchronized (this.mKeyStoreLock) {
            javax.crypto.SecretKey kk = getKeyStoreEncryptionKeyLocked();
            if (kk != null) {
                return kk;
            }
            try {
                javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("AES", com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl.ANDROID_KEY_STORE_PROVIDER);
                android.security.keystore.KeyGenParameterSpec.Builder parameterSpecBuilder = new android.security.keystore.KeyGenParameterSpec.Builder(REBOOT_ESCROW_KEY_STORE_ENCRYPTION_KEY_NAME, 3).setKeySize(256).setBlockModes("GCM").setEncryptionPaddings("NoPadding");
                parameterSpecBuilder.setNamespace(120);
                generator.init(parameterSpecBuilder.build());
                return generator.generateKey();
            } catch (java.security.GeneralSecurityException e) {
                android.util.Slog.e(TAG, "Unable to generate key from keystore.", e);
                return null;
            }
        }
    }
}
