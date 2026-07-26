package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
class AndroidKeystoreAttestationVerificationAttributes {
    private static final java.lang.String ANDROID_KEYMASTER_KEY_DESCRIPTION_EXTENSION_OID = "1.3.6.1.4.1.11129.2.1.17";
    private static final int ATTESTATION_CHALLENGE_INDEX = 4;
    private static final int ATTESTATION_SECURITY_LEVEL_INDEX = 1;
    private static final int ATTESTATION_VERSION_INDEX = 0;
    private static final int HW_AUTH_NONE = 0;
    private static final int HW_ENFORCED_INDEX = 7;
    private static final int KEYMASTER_SECURITY_LEVEL_INDEX = 3;
    private static final int KEYMASTER_UNIQUE_ID_INDEX = 5;
    private static final int KEYMASTER_VERSION_INDEX = 2;
    private static final int KM_SECURITY_LEVEL_SOFTWARE = 0;
    private static final int KM_SECURITY_LEVEL_STRONG_BOX = 2;
    private static final int KM_SECURITY_LEVEL_TRUSTED_ENVIRONMENT = 1;
    private static final int KM_TAG_ALL_APPLICATIONS = 600;
    private static final int KM_TAG_ATTESTATION_APPLICATION_ID = 709;
    private static final int KM_TAG_ATTESTATION_ID_BRAND = 710;
    private static final int KM_TAG_ATTESTATION_ID_DEVICE = 711;
    private static final int KM_TAG_ATTESTATION_ID_PRODUCT = 712;
    private static final int KM_TAG_BOOT_PATCHLEVEL = 719;
    private static final int KM_TAG_NO_AUTH_REQUIRED = 503;
    private static final int KM_TAG_OS_PATCHLEVEL = 706;
    private static final int KM_TAG_OS_VERSION = 705;
    private static final int KM_TAG_ROOT_OF_TRUST = 704;
    private static final int KM_TAG_UNLOCKED_DEVICE_REQUIRED = 509;
    private static final int KM_TAG_VENDOR_PATCHLEVEL = 718;
    private static final int KM_VERIFIED_BOOT_STATE_FAILED = 3;
    private static final int KM_VERIFIED_BOOT_STATE_SELF_SIGNED = 1;
    private static final int KM_VERIFIED_BOOT_STATE_UNVERIFIED = 2;
    private static final int KM_VERIFIED_BOOT_STATE_VERIFIED = 0;
    private static final int PACKAGE_INFO_NAME_INDEX = 0;
    private static final int PACKAGE_INFO_SET_INDEX = 0;
    private static final int PACKAGE_INFO_VERSION_INDEX = 1;
    private static final int PACKAGE_SIGNATURE_SET_INDEX = 1;
    private static final int SW_ENFORCED_INDEX = 6;
    private static final int VERIFIED_BOOT_HASH_INDEX = 3;
    private static final int VERIFIED_BOOT_KEY_INDEX = 0;
    private static final int VERIFIED_BOOT_LOCKED_INDEX = 1;
    private static final int VERIFIED_BOOT_STATE_INDEX = 2;
    private com.android.framework.protobuf.ByteString mAttestationChallenge;
    private boolean mAttestationHardwareBacked;
    private com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel mAttestationSecurityLevel;
    private java.lang.Integer mAttestationVersion;
    private java.lang.String mDeviceBrand;
    private java.lang.String mDeviceName;
    private java.lang.String mDeviceProductName;
    private boolean mKeyAllowedForAllApplications;
    private java.lang.Integer mKeyAuthenticatorType;
    private java.lang.Integer mKeyBootPatchLevel;
    private java.lang.Integer mKeyOsPatchLevel;
    private java.lang.Integer mKeyOsVersion;
    private java.lang.Boolean mKeyRequiresUnlockedDevice;
    private java.lang.Integer mKeyVendorPatchLevel;
    private boolean mKeymasterHardwareBacked;
    private com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel mKeymasterSecurityLevel;
    private com.android.framework.protobuf.ByteString mKeymasterUniqueId;
    private java.lang.Integer mKeymasterVersion;
    private com.android.framework.protobuf.ByteString mVerifiedBootHash;
    private com.android.framework.protobuf.ByteString mVerifiedBootKey;
    private java.lang.Boolean mVerifiedBootLocked;
    private com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState mVerifiedBootState;
    private java.util.Map<java.lang.String, java.lang.Long> mApplicationPackageNameVersion = null;
    private java.util.List<com.android.framework.protobuf.ByteString> mApplicationCertificateDigests = null;

    enum SecurityLevel {
        SOFTWARE,
        TRUSTED_ENVIRONMENT,
        STRONG_BOX
    }

    enum VerifiedBootState {
        VERIFIED,
        SELF_SIGNED,
        UNVERIFIED,
        FAILED
    }

    static com.android.server.security.AndroidKeystoreAttestationVerificationAttributes fromCertificate(java.security.cert.X509Certificate x509Certificate) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        return new com.android.server.security.AndroidKeystoreAttestationVerificationAttributes(x509Certificate);
    }

    int getAttestationVersion() {
        return this.mAttestationVersion.intValue();
    }

    com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel getAttestationSecurityLevel() {
        return this.mAttestationSecurityLevel;
    }

    boolean isAttestationHardwareBacked() {
        return this.mAttestationHardwareBacked;
    }

    int getKeymasterVersion() {
        return this.mKeymasterVersion.intValue();
    }

    com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel getKeymasterSecurityLevel() {
        return this.mKeymasterSecurityLevel;
    }

    boolean isKeymasterHardwareBacked() {
        return this.mKeymasterHardwareBacked;
    }

    com.android.framework.protobuf.ByteString getAttestationChallenge() {
        return this.mAttestationChallenge;
    }

    com.android.framework.protobuf.ByteString getKeymasterUniqueId() {
        return this.mKeymasterUniqueId;
    }

    java.lang.String getDeviceBrand() {
        return this.mDeviceBrand;
    }

    java.lang.String getDeviceName() {
        return this.mDeviceName;
    }

    java.lang.String getDeviceProductName() {
        return this.mDeviceProductName;
    }

    boolean isKeyAllowedForAllApplications() {
        return this.mKeyAllowedForAllApplications;
    }

    int getKeyAuthenticatorType() {
        if (this.mKeyAuthenticatorType == null) {
            throw new java.lang.IllegalStateException("KeyAuthenticatorType is not set.");
        }
        return this.mKeyAuthenticatorType.intValue();
    }

    int getKeyBootPatchLevel() {
        if (this.mKeyBootPatchLevel == null) {
            throw new java.lang.IllegalStateException("KeyBootPatchLevel is not set.");
        }
        return this.mKeyBootPatchLevel.intValue();
    }

    int getKeyOsPatchLevel() {
        if (this.mKeyOsPatchLevel == null) {
            throw new java.lang.IllegalStateException("KeyOsPatchLevel is not set.");
        }
        return this.mKeyOsPatchLevel.intValue();
    }

    int getKeyVendorPatchLevel() {
        if (this.mKeyVendorPatchLevel == null) {
            throw new java.lang.IllegalStateException("KeyVendorPatchLevel is not set.");
        }
        return this.mKeyVendorPatchLevel.intValue();
    }

    int getKeyOsVersion() {
        if (this.mKeyOsVersion == null) {
            throw new java.lang.IllegalStateException("KeyOsVersion is not set.");
        }
        return this.mKeyOsVersion.intValue();
    }

    boolean isKeyRequiresUnlockedDevice() {
        if (this.mKeyRequiresUnlockedDevice == null) {
            throw new java.lang.IllegalStateException("KeyRequiresUnlockedDevice is not set.");
        }
        return this.mKeyRequiresUnlockedDevice.booleanValue();
    }

    com.android.framework.protobuf.ByteString getVerifiedBootHash() {
        return this.mVerifiedBootHash;
    }

    com.android.framework.protobuf.ByteString getVerifiedBootKey() {
        return this.mVerifiedBootKey;
    }

    boolean isVerifiedBootLocked() {
        if (this.mVerifiedBootLocked == null) {
            throw new java.lang.IllegalStateException("VerifiedBootLocked is not set.");
        }
        return this.mVerifiedBootLocked.booleanValue();
    }

    com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState getVerifiedBootState() {
        return this.mVerifiedBootState;
    }

    java.util.Map<java.lang.String, java.lang.Long> getApplicationPackageNameVersion() {
        return java.util.Collections.unmodifiableMap(this.mApplicationPackageNameVersion);
    }

    java.util.List<com.android.framework.protobuf.ByteString> getApplicationCertificateDigests() {
        return java.util.Collections.unmodifiableList(this.mApplicationCertificateDigests);
    }

    private AndroidKeystoreAttestationVerificationAttributes(java.security.cert.X509Certificate x509Certificate) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        boolean z;
        boolean z2;
        this.mAttestationVersion = null;
        this.mAttestationSecurityLevel = null;
        this.mAttestationHardwareBacked = false;
        this.mKeymasterVersion = null;
        this.mKeymasterSecurityLevel = null;
        this.mKeymasterHardwareBacked = false;
        this.mAttestationChallenge = null;
        this.mKeymasterUniqueId = null;
        this.mDeviceBrand = null;
        this.mDeviceName = null;
        this.mDeviceProductName = null;
        this.mKeyAllowedForAllApplications = false;
        this.mKeyAuthenticatorType = null;
        this.mKeyBootPatchLevel = null;
        this.mKeyOsPatchLevel = null;
        this.mKeyOsVersion = null;
        this.mKeyVendorPatchLevel = null;
        this.mKeyRequiresUnlockedDevice = null;
        this.mVerifiedBootHash = null;
        this.mVerifiedBootKey = null;
        this.mVerifiedBootLocked = null;
        this.mVerifiedBootState = null;
        com.android.internal.org.bouncycastle.asn1.x509.Certificate certificate = com.android.internal.org.bouncycastle.asn1.x509.Certificate.getInstance(new com.android.internal.org.bouncycastle.asn1.ASN1InputStream(x509Certificate.getEncoded()).readObject());
        com.android.internal.org.bouncycastle.asn1.ASN1Sequence keyAttributes = certificate.getTBSCertificate().getExtensions().getExtensionParsedValue(new com.android.internal.org.bouncycastle.asn1.ASN1ObjectIdentifier(ANDROID_KEYMASTER_KEY_DESCRIPTION_EXTENSION_OID));
        if (keyAttributes == null) {
            throw new java.security.cert.CertificateEncodingException("No attestation extension found in certificate.");
        }
        this.mAttestationVersion = java.lang.Integer.valueOf(getIntegerFromAsn1(keyAttributes.getObjectAt(0)));
        this.mAttestationSecurityLevel = getSecurityLevelEnum(keyAttributes.getObjectAt(1));
        if (this.mAttestationSecurityLevel != com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel.TRUSTED_ENVIRONMENT) {
            z = false;
        } else {
            z = true;
        }
        this.mAttestationHardwareBacked = z;
        this.mAttestationChallenge = getOctetsFromAsn1(keyAttributes.getObjectAt(4));
        this.mKeymasterVersion = java.lang.Integer.valueOf(getIntegerFromAsn1(keyAttributes.getObjectAt(2)));
        this.mKeymasterUniqueId = getOctetsFromAsn1(keyAttributes.getObjectAt(5));
        this.mKeymasterSecurityLevel = getSecurityLevelEnum(keyAttributes.getObjectAt(3));
        if (this.mKeymasterSecurityLevel != com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel.TRUSTED_ENVIRONMENT) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mKeymasterHardwareBacked = z2;
        for (com.android.internal.org.bouncycastle.asn1.ASN1TaggedObject taggedEntry : keyAttributes.getObjectAt(6).toArray()) {
            switch (taggedEntry.getTagNo()) {
                case KM_TAG_UNLOCKED_DEVICE_REQUIRED /* 509 */:
                    this.mKeyRequiresUnlockedDevice = getBoolFromAsn1(taggedEntry.getObject());
                    break;
                case KM_TAG_ATTESTATION_APPLICATION_ID /* 709 */:
                    parseAttestationApplicationId(getOctetsFromAsn1(taggedEntry.getObject()).toByteArray());
                    break;
            }
        }
        for (com.android.internal.org.bouncycastle.asn1.ASN1TaggedObject taggedEntry2 : keyAttributes.getObjectAt(7).toArray()) {
            switch (taggedEntry2.getTagNo()) {
                case KM_TAG_NO_AUTH_REQUIRED /* 503 */:
                    this.mKeyAuthenticatorType = 0;
                    break;
                case 600:
                    this.mKeyAllowedForAllApplications = true;
                    break;
                case KM_TAG_ROOT_OF_TRUST /* 704 */:
                    com.android.internal.org.bouncycastle.asn1.ASN1Sequence rootOfTrust = taggedEntry2.getObject();
                    this.mVerifiedBootKey = getOctetsFromAsn1(rootOfTrust.getObjectAt(0));
                    this.mVerifiedBootLocked = getBoolFromAsn1(rootOfTrust.getObjectAt(1));
                    this.mVerifiedBootState = getVerifiedBootStateEnum(rootOfTrust.getObjectAt(2));
                    if (this.mAttestationVersion.intValue() >= 3) {
                        this.mVerifiedBootHash = getOctetsFromAsn1(rootOfTrust.getObjectAt(3));
                    }
                    break;
                case 705:
                    this.mKeyOsVersion = java.lang.Integer.valueOf(getIntegerFromAsn1(taggedEntry2.getObject()));
                    break;
                case KM_TAG_OS_PATCHLEVEL /* 706 */:
                    this.mKeyOsPatchLevel = java.lang.Integer.valueOf(getIntegerFromAsn1(taggedEntry2.getObject()));
                    break;
                case KM_TAG_ATTESTATION_ID_BRAND /* 710 */:
                    this.mDeviceBrand = getUtf8FromOctetsFromAsn1(taggedEntry2.getObject());
                    break;
                case KM_TAG_ATTESTATION_ID_DEVICE /* 711 */:
                    this.mDeviceName = getUtf8FromOctetsFromAsn1(taggedEntry2.getObject());
                    break;
                case KM_TAG_ATTESTATION_ID_PRODUCT /* 712 */:
                    this.mDeviceProductName = getUtf8FromOctetsFromAsn1(taggedEntry2.getObject());
                    break;
                case KM_TAG_VENDOR_PATCHLEVEL /* 718 */:
                    this.mKeyVendorPatchLevel = java.lang.Integer.valueOf(getIntegerFromAsn1(taggedEntry2.getObject()));
                    break;
                case KM_TAG_BOOT_PATCHLEVEL /* 719 */:
                    this.mKeyBootPatchLevel = java.lang.Integer.valueOf(getIntegerFromAsn1(taggedEntry2.getObject()));
                    break;
            }
        }
    }

    private void parseAttestationApplicationId(byte[] attestationApplicationId) throws java.io.IOException {
        com.android.internal.org.bouncycastle.asn1.ASN1Sequence outerSequence = com.android.internal.org.bouncycastle.asn1.ASN1Sequence.getInstance(new com.android.internal.org.bouncycastle.asn1.ASN1InputStream(attestationApplicationId).readObject());
        java.util.Map<java.lang.String, java.lang.Long> packageNameVersion = new java.util.HashMap<>();
        com.android.internal.org.bouncycastle.asn1.ASN1Set packageInfoSet = outerSequence.getObjectAt(0);
        for (com.android.internal.org.bouncycastle.asn1.ASN1Sequence packageInfoSequence : packageInfoSet.toArray()) {
            packageNameVersion.put(getUtf8FromOctetsFromAsn1(packageInfoSequence.getObjectAt(0)), java.lang.Long.valueOf(getLongFromAsn1(packageInfoSequence.getObjectAt(1))));
        }
        java.util.List<com.android.framework.protobuf.ByteString> certificateDigests = new java.util.ArrayList<>();
        com.android.internal.org.bouncycastle.asn1.ASN1Set certificateDigestSet = outerSequence.getObjectAt(1);
        for (com.android.internal.org.bouncycastle.asn1.ASN1Encodable certificateDigestEntry : certificateDigestSet.toArray()) {
            certificateDigests.add(getOctetsFromAsn1(certificateDigestEntry));
        }
        this.mApplicationPackageNameVersion = java.util.Collections.unmodifiableMap(packageNameVersion);
        this.mApplicationCertificateDigests = java.util.Collections.unmodifiableList(certificateDigests);
    }

    private com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState getVerifiedBootStateEnum(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        int verifiedBoot = getEnumFromAsn1(asn1);
        switch (verifiedBoot) {
            case 0:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState.VERIFIED;
            case 1:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState.SELF_SIGNED;
            case 2:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState.UNVERIFIED;
            case 3:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState.FAILED;
            default:
                throw new java.lang.IllegalArgumentException("Invalid verified boot state.");
        }
    }

    private com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel getSecurityLevelEnum(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        int securityLevel = getEnumFromAsn1(asn1);
        switch (securityLevel) {
            case 0:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel.SOFTWARE;
            case 1:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel.TRUSTED_ENVIRONMENT;
            case 2:
                return com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.SecurityLevel.STRONG_BOX;
            default:
                throw new java.lang.IllegalArgumentException("Invalid security level.");
        }
    }

    private com.android.framework.protobuf.ByteString getOctetsFromAsn1(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        return com.android.framework.protobuf.ByteString.copyFrom(((com.android.internal.org.bouncycastle.asn1.ASN1OctetString) asn1).getOctets());
    }

    private java.lang.String getUtf8FromOctetsFromAsn1(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        return new java.lang.String(((com.android.internal.org.bouncycastle.asn1.ASN1OctetString) asn1).getOctets(), java.nio.charset.StandardCharsets.UTF_8);
    }

    private int getIntegerFromAsn1(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        return ((com.android.internal.org.bouncycastle.asn1.ASN1Integer) asn1).getValue().intValueExact();
    }

    private long getLongFromAsn1(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        return ((com.android.internal.org.bouncycastle.asn1.ASN1Integer) asn1).getValue().longValueExact();
    }

    private int getEnumFromAsn1(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        return ((com.android.internal.org.bouncycastle.asn1.ASN1Enumerated) asn1).getValue().intValueExact();
    }

    private java.lang.Boolean getBoolFromAsn1(com.android.internal.org.bouncycastle.asn1.ASN1Encodable asn1) {
        if (asn1 instanceof com.android.internal.org.bouncycastle.asn1.ASN1Boolean) {
            return java.lang.Boolean.valueOf(((com.android.internal.org.bouncycastle.asn1.ASN1Boolean) asn1).isTrue());
        }
        return null;
    }
}
