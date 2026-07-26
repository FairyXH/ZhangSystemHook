package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
public class ApplicationKeyStorage {
    private static final java.lang.String APPLICATION_KEY_ALIAS_PREFIX = "com.android.server.locksettings.recoverablekeystore/application/";
    private static final java.lang.String APPLICATION_KEY_GRANT_PREFIX = "recoverable_key:";
    private static final java.lang.String TAG = "RecoverableAppKeyStore";
    private final com.android.server.locksettings.recoverablekeystore.KeyStoreProxy mKeyStore;

    public static com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage getInstance() throws java.security.KeyStoreException {
        return new com.android.server.locksettings.recoverablekeystore.storage.ApplicationKeyStorage(new com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl(com.android.server.locksettings.recoverablekeystore.KeyStoreProxyImpl.getAndLoadAndroidKeyStore()));
    }

    ApplicationKeyStorage(com.android.server.locksettings.recoverablekeystore.KeyStoreProxy keyStore) {
        this.mKeyStore = keyStore;
    }

    public java.lang.String getGrantAlias(int userId, int uid, java.lang.String alias) {
        android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Get %d/%d/%s", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid), alias));
        java.lang.String keystoreAlias = getInternalAlias(userId, uid, alias);
        return makeKeystoreEngineGrantString(uid, keystoreAlias);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void setSymmetricKeyEntry(int userId, int uid, java.lang.String alias, byte[] secretKey) throws java.security.KeyStoreException, android.os.ServiceSpecificException {
        android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Set %d/%d/%s: %d bytes of key material", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid), alias, java.lang.Integer.valueOf(secretKey.length)));
        try {
            this.mKeyStore.setEntry(getInternalAlias(userId, uid, alias), new java.security.KeyStore.SecretKeyEntry(new javax.crypto.spec.SecretKeySpec(secretKey, "AES")), new android.security.keystore.KeyProtection.Builder(3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
        } catch (java.security.KeyStoreException e) {
            throw new android.os.ServiceSpecificException(22, e.getMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void deleteEntry(int userId, int uid, java.lang.String alias) throws android.os.ServiceSpecificException {
        android.util.Log.i(TAG, java.lang.String.format(java.util.Locale.US, "Del %d/%d/%s", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(uid), alias));
        try {
            this.mKeyStore.deleteEntry(getInternalAlias(userId, uid, alias));
        } catch (java.security.KeyStoreException e) {
            throw new android.os.ServiceSpecificException(22, e.getMessage());
        }
    }

    private java.lang.String getInternalAlias(int userId, int uid, java.lang.String alias) {
        return APPLICATION_KEY_ALIAS_PREFIX + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + alias;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private java.lang.String makeKeystoreEngineGrantString(int uid, java.lang.String alias) throws android.os.ServiceSpecificException {
        if (alias == null) {
            return null;
        }
        android.system.keystore2.KeyDescriptor key = new android.system.keystore2.KeyDescriptor();
        key.domain = 0;
        key.nspace = -1L;
        key.alias = alias;
        key.blob = null;
        try {
            return java.lang.String.format("%s%016X", APPLICATION_KEY_GRANT_PREFIX, java.lang.Long.valueOf(android.security.KeyStore2.getInstance().grant(key, uid, 261).nspace));
        } catch (android.security.KeyStoreException e) {
            if (e.getNumericErrorCode() == 6) {
                android.util.Log.w(TAG, "Failed to get grant for KeyStore key - key not found");
                throw new android.os.ServiceSpecificException(30, e.getMessage());
            }
            android.util.Log.e(TAG, "Failed to get grant for KeyStore key.", e);
            throw new android.os.ServiceSpecificException(22, e.getMessage());
        }
    }
}
