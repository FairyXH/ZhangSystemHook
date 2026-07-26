package com.android.server.locksettings.recoverablekeystore.certificate;

/* JADX INFO: loaded from: classes2.dex */
public final class SigXml {
    private static final java.lang.String INTERMEDIATE_CERT_ITEM_TAG = "cert";
    private static final java.lang.String INTERMEDIATE_CERT_LIST_TAG = "intermediates";
    private static final java.lang.String SIGNATURE_NODE_TAG = "value";
    private static final java.lang.String SIGNER_CERT_NODE_TAG = "certificate";
    private final java.util.List<java.security.cert.X509Certificate> intermediateCerts;
    private final byte[] signature;
    private final java.security.cert.X509Certificate signerCert;

    private SigXml(java.util.List<java.security.cert.X509Certificate> intermediateCerts, java.security.cert.X509Certificate signerCert, byte[] signature) {
        this.intermediateCerts = intermediateCerts;
        this.signerCert = signerCert;
        this.signature = signature;
    }

    public void verifyFileSignature(java.security.cert.X509Certificate trustedRoot, byte[] signedFileBytes, java.util.Date validationDate) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.validateCert(validationDate, trustedRoot, this.intermediateCerts, this.signerCert);
        com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.verifyRsaSha256Signature(this.signerCert.getPublicKey(), this.signature, signedFileBytes);
    }

    public static com.android.server.locksettings.recoverablekeystore.certificate.SigXml parse(byte[] bytes) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        org.w3c.dom.Element rootNode = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlRootNode(bytes);
        return new com.android.server.locksettings.recoverablekeystore.certificate.SigXml(parseIntermediateCerts(rootNode), parseSignerCert(rootNode), parseFileSignature(rootNode));
    }

    private static java.util.List<java.security.cert.X509Certificate> parseIntermediateCerts(org.w3c.dom.Element rootNode) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        java.util.List<java.lang.String> contents = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlNodeContents(0, rootNode, INTERMEDIATE_CERT_LIST_TAG, INTERMEDIATE_CERT_ITEM_TAG);
        java.util.List<java.security.cert.X509Certificate> res = new java.util.ArrayList<>();
        for (java.lang.String content : contents) {
            res.add(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeCert(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeBase64(content)));
        }
        return java.util.Collections.unmodifiableList(res);
    }

    private static java.security.cert.X509Certificate parseSignerCert(org.w3c.dom.Element rootNode) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        java.util.List<java.lang.String> contents = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlNodeContents(1, rootNode, SIGNER_CERT_NODE_TAG);
        return com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeCert(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeBase64(contents.get(0)));
    }

    private static byte[] parseFileSignature(org.w3c.dom.Element rootNode) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        java.util.List<java.lang.String> contents = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlNodeContents(1, rootNode, SIGNATURE_NODE_TAG);
        return com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeBase64(contents.get(0));
    }
}
