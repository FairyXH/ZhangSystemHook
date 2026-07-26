package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
class AttestationVerificationSelfTrustedVerifierForTesting {
    private static final java.lang.String ANDROID_KEYMINT_KEY_DESCRIPTION_EXTENSION_OID = "1.3.6.1.4.1.11129.2.1.17";
    private static final java.lang.String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final int ATTESTATION_CHALLENGE_INDEX = 4;
    private static final boolean DEBUG;
    private static final java.lang.String GOLDEN_ALIAS;
    private static final java.lang.String TAG = "AVF";
    private static volatile com.android.server.security.AttestationVerificationSelfTrustedVerifierForTesting sAttestationVerificationSelfTrustedVerifier;
    private java.security.cert.X509Certificate mGoldenRootCert;
    private final java.security.cert.CertificateFactory mCertificateFactory = java.security.cert.CertificateFactory.getInstance("X.509");
    private final java.security.cert.CertPathValidator mCertPathValidator = java.security.cert.CertPathValidator.getInstance("PKIX");
    private final java.security.KeyStore mAndroidKeyStore = java.security.KeyStore.getInstance("AndroidKeyStore");

    static {
        DEBUG = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(TAG, 2);
        GOLDEN_ALIAS = com.android.server.security.AttestationVerificationSelfTrustedVerifierForTesting.class.getCanonicalName() + ".Golden";
        sAttestationVerificationSelfTrustedVerifier = null;
    }

    static com.android.server.security.AttestationVerificationSelfTrustedVerifierForTesting getInstance() throws java.lang.Exception {
        if (sAttestationVerificationSelfTrustedVerifier == null) {
            synchronized (com.android.server.security.AttestationVerificationSelfTrustedVerifierForTesting.class) {
                if (sAttestationVerificationSelfTrustedVerifier == null) {
                    sAttestationVerificationSelfTrustedVerifier = new com.android.server.security.AttestationVerificationSelfTrustedVerifierForTesting();
                }
            }
        }
        return sAttestationVerificationSelfTrustedVerifier;
    }

    private static void debugVerboseLog(java.lang.String str, java.lang.Throwable t) {
        if (DEBUG) {
            android.util.Slog.v(TAG, str, t);
        }
    }

    private static void debugVerboseLog(java.lang.String str) {
        if (DEBUG) {
            android.util.Slog.v(TAG, str);
        }
    }

    private AttestationVerificationSelfTrustedVerifierForTesting() throws java.lang.Exception {
        this.mAndroidKeyStore.load(null);
        if (!this.mAndroidKeyStore.containsAlias(GOLDEN_ALIAS)) {
            java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance("EC", "AndroidKeyStore");
            android.security.keystore.KeyGenParameterSpec parameterSpec = new android.security.keystore.KeyGenParameterSpec.Builder(GOLDEN_ALIAS, 12).setAttestationChallenge(GOLDEN_ALIAS.getBytes()).setDigests("SHA-256", "SHA-512").build();
            kpg.initialize(parameterSpec);
            kpg.generateKeyPair();
        }
        java.security.cert.X509Certificate[] goldenCerts = (java.security.cert.X509Certificate[]) ((java.security.KeyStore.PrivateKeyEntry) this.mAndroidKeyStore.getEntry(GOLDEN_ALIAS, null)).getCertificateChain();
        this.mGoldenRootCert = goldenCerts[goldenCerts.length - 1];
    }

    int verifyAttestation(int localBindingType, android.os.Bundle requirements, byte[] attestation) {
        java.util.List<java.security.cert.X509Certificate> certificates = new java.util.ArrayList<>();
        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(attestation);
        while (bis.available() > 0) {
            try {
                certificates.add((java.security.cert.X509Certificate) this.mCertificateFactory.generateCertificate(bis));
            } catch (java.security.cert.CertificateException e) {
                debugVerboseLog("Unable to parse certificates from attestation", e);
                return 2;
            }
        }
        if (localBindingType != 3 || !validateRequirements(requirements) || !checkLeafChallenge(requirements, certificates) || !verifyCertificateChain(certificates)) {
            return 2;
        }
        return 1;
    }

    private boolean verifyCertificateChain(java.util.List<java.security.cert.X509Certificate> certificates) {
        if (certificates.size() < 2) {
            debugVerboseLog("Certificate chain less than 2 in size.");
            return false;
        }
        try {
            java.security.cert.CertPath certificatePath = this.mCertificateFactory.generateCertPath(certificates);
            java.security.cert.PKIXParameters validationParams = new java.security.cert.PKIXParameters(getTrustAnchors());
            validationParams.setRevocationEnabled(false);
            this.mCertPathValidator.validate(certificatePath, validationParams);
            return true;
        } catch (java.lang.Throwable t) {
            debugVerboseLog("Invalid certificate chain", t);
            return false;
        }
    }

    private java.util.Set<java.security.cert.TrustAnchor> getTrustAnchors() {
        return java.util.Collections.singleton(new java.security.cert.TrustAnchor(this.mGoldenRootCert, null));
    }

    private boolean validateRequirements(android.os.Bundle requirements) {
        if (requirements.size() != 1) {
            debugVerboseLog("Requirements does not contain exactly 1 key.");
            return false;
        }
        if (requirements.containsKey("localbinding.challenge")) {
            return true;
        }
        debugVerboseLog("Requirements does not contain key: localbinding.challenge");
        return false;
    }

    private boolean checkLeafChallenge(android.os.Bundle requirements, java.util.List<java.security.cert.X509Certificate> certificates) {
        try {
            byte[] challenge = getChallengeFromCert(certificates.get(0));
            if (java.util.Arrays.equals(requirements.getByteArray("localbinding.challenge"), challenge)) {
                return true;
            }
            debugVerboseLog("Self-Trusted validation failed; challenge mismatch.");
            return false;
        } catch (java.lang.Throwable t) {
            debugVerboseLog("Unable to parse challenge from certificate.", t);
            return false;
        }
    }

    private byte[] getChallengeFromCert(java.security.cert.X509Certificate x509Certificate) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        com.android.internal.org.bouncycastle.asn1.x509.Certificate certificate = com.android.internal.org.bouncycastle.asn1.x509.Certificate.getInstance(new com.android.internal.org.bouncycastle.asn1.ASN1InputStream(x509Certificate.getEncoded()).readObject());
        com.android.internal.org.bouncycastle.asn1.ASN1Sequence keyAttributes = certificate.getTBSCertificate().getExtensions().getExtensionParsedValue(new com.android.internal.org.bouncycastle.asn1.ASN1ObjectIdentifier(ANDROID_KEYMINT_KEY_DESCRIPTION_EXTENSION_OID));
        return keyAttributes.getObjectAt(4).getOctets();
    }
}
