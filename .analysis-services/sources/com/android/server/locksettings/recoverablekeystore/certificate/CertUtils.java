package com.android.server.locksettings.recoverablekeystore.certificate;

/* JADX INFO: loaded from: classes2.dex */
public final class CertUtils {
    private static final java.lang.String CERT_FORMAT = "X.509";
    private static final java.lang.String CERT_PATH_ALG = "PKIX";
    private static final java.lang.String CERT_STORE_ALG = "Collection";
    static final int MUST_EXIST_AT_LEAST_ONE = 2;
    static final int MUST_EXIST_EXACTLY_ONE = 1;
    static final int MUST_EXIST_UNENFORCED = 0;
    private static final java.lang.String SIGNATURE_ALG = "SHA256withRSA";

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface MustExist {
    }

    private CertUtils() {
    }

    static java.security.cert.X509Certificate decodeCert(byte[] certBytes) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        return decodeCert(new java.io.ByteArrayInputStream(certBytes));
    }

    static java.security.cert.X509Certificate decodeCert(java.io.InputStream inStream) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        try {
            java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance(CERT_FORMAT);
            try {
                return (java.security.cert.X509Certificate) certFactory.generateCertificate(inStream);
            } catch (java.security.cert.CertificateException e) {
                throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException(e);
            }
        } catch (java.security.cert.CertificateException e2) {
            throw new java.lang.RuntimeException(e2);
        }
    }

    static org.w3c.dom.Element getXmlRootNode(byte[] xmlBytes) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        try {
            org.w3c.dom.Document document = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.ByteArrayInputStream(xmlBytes));
            document.getDocumentElement().normalize();
            return document.getDocumentElement();
        } catch (java.io.IOException | javax.xml.parsers.ParserConfigurationException | org.xml.sax.SAXException e) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException(e);
        }
    }

    static java.util.List<java.lang.String> getXmlNodeContents(int mustExist, org.w3c.dom.Element rootNode, java.lang.String... nodeTags) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        if (nodeTags.length == 0) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException("The tag list must not be empty");
        }
        org.w3c.dom.Element parent = rootNode;
        for (int i = 0; i < nodeTags.length - 1; i++) {
            java.lang.String tag = nodeTags[i];
            java.util.List<org.w3c.dom.Element> children = getXmlDirectChildren(parent, tag);
            if ((children.size() == 0 && mustExist != 0) || children.size() > 1) {
                throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException("The XML file must contain exactly one path with the tag " + tag);
            }
            if (children.size() == 0) {
                return new java.util.ArrayList();
            }
            org.w3c.dom.Element parent2 = children.get(0);
            parent = parent2;
        }
        int i2 = nodeTags.length;
        java.util.List<org.w3c.dom.Element> leafs = getXmlDirectChildren(parent, nodeTags[i2 - 1]);
        if (mustExist == 1 && leafs.size() != 1) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException("The XML file must contain exactly one node with the path " + java.lang.String.join(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER, nodeTags));
        }
        if (mustExist == 2 && leafs.size() == 0) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException("The XML file must contain at least one node with the path " + java.lang.String.join(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER, nodeTags));
        }
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        for (org.w3c.dom.Element leaf : leafs) {
            result.add(leaf.getTextContent().replaceAll("\\s", ""));
        }
        return result;
    }

    private static java.util.List<org.w3c.dom.Element> getXmlDirectChildren(org.w3c.dom.Element parent, java.lang.String tag) {
        java.util.List<org.w3c.dom.Element> children = new java.util.ArrayList<>();
        org.w3c.dom.NodeList childNodes = parent.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            org.w3c.dom.Node node = childNodes.item(i);
            if (node.getNodeType() == 1 && node.getNodeName().equals(tag)) {
                children.add((org.w3c.dom.Element) node);
            }
        }
        return children;
    }

    public static byte[] decodeBase64(java.lang.String str) throws com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException {
        try {
            return java.util.Base64.getDecoder().decode(str);
        } catch (java.lang.IllegalArgumentException e) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertParsingException(e);
        }
    }

    static void verifyRsaSha256Signature(java.security.PublicKey signerPublicKey, byte[] signature, byte[] signedBytes) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        try {
            java.security.Signature verifier = java.security.Signature.getInstance(SIGNATURE_ALG);
            try {
                verifier.initVerify(signerPublicKey);
                verifier.update(signedBytes);
                if (!verifier.verify(signature)) {
                    throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException("The signature is invalid");
                }
            } catch (java.security.InvalidKeyException | java.security.SignatureException e) {
                throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException(e);
            }
        } catch (java.security.NoSuchAlgorithmException e2) {
            throw new java.lang.RuntimeException(e2);
        }
    }

    static java.security.cert.CertPath validateCert(java.util.Date validationDate, java.security.cert.X509Certificate trustedRoot, java.util.List<java.security.cert.X509Certificate> intermediateCerts, java.security.cert.X509Certificate leafCert) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        java.security.cert.PKIXParameters pkixParams = buildPkixParams(validationDate, trustedRoot, intermediateCerts, leafCert);
        java.security.cert.CertPath certPath = buildCertPath(pkixParams);
        try {
            java.security.cert.CertPathValidator certPathValidator = java.security.cert.CertPathValidator.getInstance(CERT_PATH_ALG);
            try {
                certPathValidator.validate(certPath, pkixParams);
                return certPath;
            } catch (java.security.InvalidAlgorithmParameterException | java.security.cert.CertPathValidatorException e) {
                throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException(e);
            }
        } catch (java.security.NoSuchAlgorithmException e2) {
            throw new java.lang.RuntimeException(e2);
        }
    }

    public static void validateCertPath(java.security.cert.X509Certificate trustedRoot, java.security.cert.CertPath certPath, java.util.Date validationDate) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        validateCertPath(validationDate, trustedRoot, certPath);
    }

    static void validateCertPath(java.util.Date validationDate, java.security.cert.X509Certificate trustedRoot, java.security.cert.CertPath certPath) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        if (certPath.getCertificates().isEmpty()) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException("The given certificate path is empty");
        }
        if (!(certPath.getCertificates().get(0) instanceof java.security.cert.X509Certificate)) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException("The given certificate path does not contain X509 certificates");
        }
        java.util.List<? extends java.security.cert.Certificate> certificates = certPath.getCertificates();
        java.security.cert.X509Certificate leafCert = (java.security.cert.X509Certificate) certificates.get(0);
        validateCert(validationDate, trustedRoot, certificates.subList(1, certificates.size()), leafCert);
    }

    static java.security.cert.CertPath buildCertPath(java.security.cert.PKIXParameters pkixParams) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        try {
            java.security.cert.CertPathBuilder certPathBuilder = java.security.cert.CertPathBuilder.getInstance(CERT_PATH_ALG);
            try {
                return certPathBuilder.build(pkixParams).getCertPath();
            } catch (java.security.InvalidAlgorithmParameterException | java.security.cert.CertPathBuilderException e) {
                throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException(e);
            }
        } catch (java.security.NoSuchAlgorithmException e2) {
            throw new java.lang.RuntimeException(e2);
        }
    }

    static java.security.cert.PKIXParameters buildPkixParams(java.util.Date validationDate, java.security.cert.X509Certificate trustedRoot, java.util.List<java.security.cert.X509Certificate> intermediateCerts, java.security.cert.X509Certificate leafCert) throws com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException {
        java.util.Set<java.security.cert.TrustAnchor> trustedAnchors = new java.util.HashSet<>();
        trustedAnchors.add(new java.security.cert.TrustAnchor(trustedRoot, null));
        java.util.List<java.security.cert.X509Certificate> certs = new java.util.ArrayList<>(intermediateCerts);
        certs.add(leafCert);
        try {
            java.security.cert.CertStore certStore = java.security.cert.CertStore.getInstance(CERT_STORE_ALG, new java.security.cert.CollectionCertStoreParameters(certs));
            java.security.cert.X509CertSelector certSelector = new java.security.cert.X509CertSelector();
            certSelector.setCertificate(leafCert);
            try {
                java.security.cert.PKIXBuilderParameters pkixParams = new java.security.cert.PKIXBuilderParameters(trustedAnchors, certSelector);
                pkixParams.addCertStore(certStore);
                pkixParams.setDate(validationDate);
                pkixParams.setRevocationEnabled(false);
                return pkixParams;
            } catch (java.security.InvalidAlgorithmParameterException e) {
                throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException(e);
            }
        } catch (java.security.InvalidAlgorithmParameterException e2) {
            throw new com.android.server.locksettings.recoverablekeystore.certificate.CertValidationException(e2);
        } catch (java.security.NoSuchAlgorithmException e3) {
            throw new java.lang.RuntimeException(e3);
        }
    }
}
