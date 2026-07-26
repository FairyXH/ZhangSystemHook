package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class KeyStoreProxyImpl implements com.android.server.locksettings.recoverablekeystore.KeyStoreProxy {
    public static final java.lang.String ANDROID_KEY_STORE_PROVIDER = "AndroidKeyStore";
    private final java.security.KeyStore mKeyStore;

    public KeyStoreProxyImpl(java.security.KeyStore keyStore) {
        this.mKeyStore = keyStore;
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public boolean containsAlias(java.lang.String alias) throws java.security.KeyStoreException {
        return this.mKeyStore.containsAlias(alias);
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public java.security.Key getKey(java.lang.String alias, char[] password) throws java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.KeyStoreException {
        return this.mKeyStore.getKey(alias, password);
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public void setEntry(java.lang.String alias, java.security.KeyStore.Entry entry, java.security.KeyStore.ProtectionParameter protParam) throws java.security.KeyStoreException {
        this.mKeyStore.setEntry(alias, entry, protParam);
    }

    @Override // com.android.server.locksettings.recoverablekeystore.KeyStoreProxy
    public void deleteEntry(java.lang.String alias) throws java.security.KeyStoreException {
        this.mKeyStore.deleteEntry(alias);
    }

    public static java.security.KeyStore getAndLoadAndroidKeyStore() throws java.security.KeyStoreException {
        java.security.KeyStore keyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
        try {
            keyStore.load(null);
            return keyStore;
        } catch (java.io.IOException | java.security.NoSuchAlgorithmException | java.security.cert.CertificateException e) {
            throw new java.security.KeyStoreException("Unable to load keystore.", e);
        }
    }
}
