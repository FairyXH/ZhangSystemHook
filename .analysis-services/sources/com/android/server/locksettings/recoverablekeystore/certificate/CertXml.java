package com.android.server.locksettings.recoverablekeystore.certificate;

/* JADX INFO: loaded from: classes2.dex */
public final class CertXml {
    private static final java.lang.String ENDPOINT_CERT_ITEM_TAG = "cert";
    private static final java.lang.String ENDPOINT_CERT_LIST_TAG = "endpoints";
    private static final java.lang.String INTERMEDIATE_CERT_ITEM_TAG = "cert";
    private static final java.lang.String INTERMEDIATE_CERT_LIST_TAG = "intermediates";
    private static final java.lang.String METADATA_NODE_TAG = "metadata";
    private static final java.lang.String METADATA_SERIAL_NODE_TAG = "serial";
    private final java.util.List<java.security.cert.X509Certificate> endpointCerts;
    private final java.util.List<java.security.cert.X509Certificate> intermediateCerts;
    private final long serial;

    private CertXml(long serial, java.util.List<java.security.cert.X509Certificate> intermediateCerts, java.util.List<java.security.cert.X509Certificate> endpointCerts) {
        this.serial = serial;
        this.intermediateCerts = intermediateCerts;
        this.endpointCerts = endpointCerts;
    }

    public long getSerial() {
        return this.serial;
    }

    java.util.List<java.security.cert.X509Certificate> getAllIntermediateCerts() {
        return this.intermediateCerts;
    }

    java.util.List<java.security.cert.X509Certificate> getAllEndpointCerts() {
        return this.endpointCerts;
    }

    public java.security.cert.CertPath getRandomEndpointCert(java.security.cert.X509Certificate trustedRoot, java.util.Date validationDate) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        return getEndpointCert(new java.security.SecureRandom().nextInt(this.endpointCerts.size()), validationDate, trustedRoot);
    }

    java.security.cert.CertPath getEndpointCert(int index, java.util.Date validationDate, java.security.cert.X509Certificate trustedRoot) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        java.security.cert.X509Certificate chosenCert = this.endpointCerts.get(index);
        return com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.validateCert(validationDate, trustedRoot, this.intermediateCerts, chosenCert);
    }

    public static com.android.server.locksettings.recoverablekeystore.certificate.CertXml parse(byte[] bytes) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        org.w3c.dom.Element rootNode = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlRootNode(bytes);
        return new com.android.server.locksettings.recoverablekeystore.certificate.CertXml(parseSerial(rootNode), parseIntermediateCerts(rootNode), parseEndpointCerts(rootNode));
    }

    private static long parseSerial(org.w3c.dom.Element rootNode) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        java.util.List<java.lang.String> contents = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlNodeContents(1, rootNode, METADATA_NODE_TAG, METADATA_SERIAL_NODE_TAG);
        return java.lang.Long.parseLong(contents.get(0));
    }

    private static java.util.List<java.security.cert.X509Certificate> parseIntermediateCerts(org.w3c.dom.Element rootNode) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        java.util.List<java.lang.String> contents = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlNodeContents(0, rootNode, INTERMEDIATE_CERT_LIST_TAG, "cert");
        java.util.List<java.security.cert.X509Certificate> res = new java.util.ArrayList<>();
        for (java.lang.String content : contents) {
            res.add(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeCert(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeBase64(content)));
        }
        return java.util.Collections.unmodifiableList(res);
    }

    private static java.util.List<java.security.cert.X509Certificate> parseEndpointCerts(org.w3c.dom.Element rootNode) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        java.util.List<java.lang.String> contents = com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.getXmlNodeContents(2, rootNode, ENDPOINT_CERT_LIST_TAG, "cert");
        java.util.List<java.security.cert.X509Certificate> res = new java.util.ArrayList<>();
        for (java.lang.String content : contents) {
            res.add(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeCert(com.android.server.locksettings.recoverablekeystore.certificate.CertUtils.decodeBase64(content)));
        }
        return java.util.Collections.unmodifiableList(res);
    }
}
