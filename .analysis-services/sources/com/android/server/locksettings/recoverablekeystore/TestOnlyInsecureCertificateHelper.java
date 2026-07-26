package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class TestOnlyInsecureCertificateHelper {
    private static final java.lang.String TAG = "TestCertHelper";

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public java.security.cert.X509Certificate getRootCertificate(java.lang.String rootCertificateAlias) throws android.os.RemoteException, android.os.ServiceSpecificException {
        java.lang.String rootCertificateAlias2 = getDefaultCertificateAliasIfEmpty(rootCertificateAlias);
        if (isTestOnlyCertificateAlias(rootCertificateAlias2)) {
            return android.security.keystore.recovery.TrustedRootCertificates.getTestOnlyInsecureCertificate();
        }
        java.security.cert.X509Certificate rootCertificate = android.security.keystore.recovery.TrustedRootCertificates.getRootCertificate(rootCertificateAlias2);
        if (rootCertificate == null) {
            throw new android.os.ServiceSpecificException(28, "The provided root certificate alias is invalid");
        }
        return rootCertificate;
    }

    public java.util.Date getValidationDate(java.lang.String rootCertificateAlias) {
        if (isTestOnlyCertificateAlias(rootCertificateAlias)) {
            return new java.util.Date(119, 1, 30);
        }
        return null;
    }

    public java.lang.String getDefaultCertificateAliasIfEmpty(java.lang.String rootCertificateAlias) {
        if (rootCertificateAlias == null || rootCertificateAlias.isEmpty()) {
            android.util.Log.e(TAG, "rootCertificateAlias is null or empty - use secure default value");
            return "GoogleCloudKeyVaultServiceV1";
        }
        return rootCertificateAlias;
    }

    public boolean isTestOnlyCertificateAlias(java.lang.String rootCertificateAlias) {
        return "TEST_ONLY_INSECURE_CERTIFICATE_ALIAS".equals(rootCertificateAlias);
    }

    public boolean isValidRootCertificateAlias(java.lang.String rootCertificateAlias) {
        return android.security.keystore.recovery.TrustedRootCertificates.getRootCertificates().containsKey(rootCertificateAlias) || isTestOnlyCertificateAlias(rootCertificateAlias);
    }

    public boolean doesCredentialSupportInsecureMode(int credentialType, byte[] credential) {
        if (credential == null) {
            return false;
        }
        if (credentialType != 4 && credentialType != 3) {
            return false;
        }
        byte[] insecurePasswordPrefixBytes = "INSECURE_PSWD_".getBytes();
        if (credential.length < insecurePasswordPrefixBytes.length) {
            return false;
        }
        for (int i = 0; i < insecurePasswordPrefixBytes.length; i++) {
            if (credential[i] != insecurePasswordPrefixBytes[i]) {
                return false;
            }
        }
        return true;
    }

    public java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> keepOnlyWhitelistedInsecureKeys(java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> rawKeys) {
        if (rawKeys == null) {
            return null;
        }
        java.util.Map<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> filteredKeys = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, android.util.Pair<javax.crypto.SecretKey, byte[]>> entry : rawKeys.entrySet()) {
            java.lang.String alias = entry.getKey();
            if (alias != null && alias.startsWith("INSECURE_KEY_ALIAS_KEY_MATERIAL_IS_NOT_PROTECTED_")) {
                filteredKeys.put(entry.getKey(), android.util.Pair.create((javax.crypto.SecretKey) entry.getValue().first, (byte[]) entry.getValue().second));
                android.util.Log.d(TAG, "adding key with insecure alias " + alias + " to the recovery snapshot");
            }
        }
        return filteredKeys;
    }
}
