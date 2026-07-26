package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public interface KeyStoreProxy {
    boolean containsAlias(java.lang.String str) throws java.security.KeyStoreException;

    void deleteEntry(java.lang.String str) throws java.security.KeyStoreException;

    java.security.Key getKey(java.lang.String str, char[] cArr) throws java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.KeyStoreException;

    void setEntry(java.lang.String str, java.security.KeyStore.Entry entry, java.security.KeyStore.ProtectionParameter protectionParameter) throws java.security.KeyStoreException;
}
