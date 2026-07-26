package com.android.server.companion.securechannel;

/* JADX INFO: loaded from: classes.dex */
final class KeyStoreUtils {
    private static final java.lang.String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final java.lang.String TAG = "CDM_SecureChannelKeyStore";

    private KeyStoreUtils() {
    }

    static java.security.KeyStore loadKeyStore() throws java.security.GeneralSecurityException {
        java.security.KeyStore androidKeyStore = java.security.KeyStore.getInstance("AndroidKeyStore");
        try {
            androidKeyStore.load(null);
            return androidKeyStore;
        } catch (java.io.IOException e) {
            throw new java.security.KeyStoreException("Failed to load Android Keystore.", e);
        }
    }

    static byte[] getEncodedCertificateChain(java.lang.String alias) throws java.security.GeneralSecurityException {
        java.security.KeyStore ks = loadKeyStore();
        java.security.cert.Certificate[] certificateChain = ks.getCertificateChain(alias);
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        for (java.security.cert.Certificate certificate : certificateChain) {
            buffer.writeBytes(certificate.getEncoded());
        }
        return buffer.toByteArray();
    }

    static void generateAttestationKeyPair(java.lang.String alias, byte[] attestationChallenge) throws java.security.GeneralSecurityException {
        android.security.keystore.KeyGenParameterSpec parameterSpec = new android.security.keystore.KeyGenParameterSpec.Builder(alias, 12).setAttestationChallenge(attestationChallenge).setDigests("SHA-256").build();
        java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator.getInstance("EC", "AndroidKeyStore");
        keyPairGenerator.initialize(parameterSpec);
        keyPairGenerator.generateKeyPair();
    }

    static boolean aliasExists(java.lang.String alias) {
        try {
            java.security.KeyStore ks = loadKeyStore();
            return ks.containsAlias(alias);
        } catch (java.security.GeneralSecurityException e) {
            return false;
        }
    }

    static void cleanUp(java.lang.String alias) {
        try {
            java.security.KeyStore ks = loadKeyStore();
            if (ks.containsAlias(alias)) {
                ks.deleteEntry(alias);
            }
        } catch (java.lang.Exception e) {
        }
    }
}
