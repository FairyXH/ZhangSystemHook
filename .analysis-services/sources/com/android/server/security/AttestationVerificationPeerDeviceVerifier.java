package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
class AttestationVerificationPeerDeviceVerifier {
    private static final java.lang.String ANDROID_SYSTEM_PACKAGE_NAME = "AndroidSystem";
    private static final java.util.Set<java.lang.String> ANDROID_SYSTEM_PACKAGE_NAME_SET;
    private static final boolean DEBUG;
    private static final int MAX_PATCH_AGE_MONTHS = 12;
    private static final java.lang.String PARAM_OWNED_BY_SYSTEM = "android.key_owned_by_system";
    private static final java.lang.String TAG = "AVF";
    private final java.security.cert.CertPathValidator mCertPathValidator;
    private final java.security.cert.CertificateFactory mCertificateFactory;
    private final android.content.Context mContext;
    private final com.android.server.security.AttestationVerificationManagerService.DumpLogger mDumpLogger;
    private final boolean mRevocationEnabled;
    private final java.time.LocalDate mTestLocalPatchDate;
    private final java.time.LocalDate mTestSystemDate;
    private final java.util.Set<java.security.cert.TrustAnchor> mTrustAnchors;

    static {
        DEBUG = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(TAG, 2);
        ANDROID_SYSTEM_PACKAGE_NAME_SET = java.util.Collections.singleton(ANDROID_SYSTEM_PACKAGE_NAME);
    }

    AttestationVerificationPeerDeviceVerifier(android.content.Context context, com.android.server.security.AttestationVerificationManagerService.DumpLogger dumpLogger) throws java.lang.Exception {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mDumpLogger = dumpLogger;
        this.mCertificateFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        this.mCertPathValidator = java.security.cert.CertPathValidator.getInstance("PKIX");
        this.mTrustAnchors = getTrustAnchors();
        this.mRevocationEnabled = true;
        this.mTestSystemDate = null;
        this.mTestLocalPatchDate = null;
    }

    AttestationVerificationPeerDeviceVerifier(android.content.Context context, com.android.server.security.AttestationVerificationManagerService.DumpLogger dumpLogger, java.util.Set<java.security.cert.TrustAnchor> trustAnchors, boolean revocationEnabled, java.time.LocalDate systemDate, java.time.LocalDate localPatchDate) throws java.lang.Exception {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mDumpLogger = dumpLogger;
        this.mCertificateFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        this.mCertPathValidator = java.security.cert.CertPathValidator.getInstance("PKIX");
        this.mTrustAnchors = trustAnchors;
        this.mRevocationEnabled = revocationEnabled;
        this.mTestSystemDate = systemDate;
        this.mTestLocalPatchDate = localPatchDate;
    }

    int verifyAttestation(int localBindingType, android.os.Bundle requirements, byte[] attestation) {
        com.android.server.security.AttestationVerificationPeerDeviceVerifier.MyDumpData dumpData = new com.android.server.security.AttestationVerificationPeerDeviceVerifier.MyDumpData();
        int result = verifyAttestationInternal(localBindingType, requirements, attestation, dumpData);
        dumpData.mResult = result;
        this.mDumpLogger.logAttempt(dumpData);
        return result;
    }

    private int verifyAttestationInternal(int localBindingType, android.os.Bundle requirements, byte[] attestation, com.android.server.security.AttestationVerificationPeerDeviceVerifier.MyDumpData dumpData) {
        if (this.mCertificateFactory == null) {
            debugVerboseLog("Unable to access CertificateFactory");
            return 2;
        }
        dumpData.mCertificationFactoryAvailable = true;
        if (this.mCertPathValidator == null) {
            debugVerboseLog("Unable to access CertPathValidator");
            return 2;
        }
        dumpData.mCertPathValidatorAvailable = true;
        if (!validateAttestationParameters(localBindingType, requirements)) {
            return 2;
        }
        dumpData.mAttestationParametersOk = true;
        boolean failed = false;
        try {
            java.util.List<java.security.cert.X509Certificate> certificateChain = getCertificates(attestation);
            validateCertificateChain(certificateChain);
            dumpData.mCertChainOk = true;
            java.security.cert.X509Certificate leafCertificate = certificateChain.get(0);
            com.android.server.security.AndroidKeystoreAttestationVerificationAttributes attestationExtension = com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.fromCertificate(leafCertificate);
            if (!checkAttestationForPeerDeviceProfile(attestationExtension, dumpData)) {
                failed = true;
            }
            if (!checkLocalBindingRequirements(leafCertificate, attestationExtension, localBindingType, requirements, dumpData)) {
                failed = true;
            }
        } catch (java.io.IOException | java.security.InvalidAlgorithmParameterException | java.security.cert.CertPathValidatorException | java.security.cert.CertificateException e) {
            debugVerboseLog("Unable to parse/validate Android Attestation certificate(s)", e);
            failed = true;
        } catch (java.lang.RuntimeException e2) {
            debugVerboseLog("Unexpected error", e2);
            failed = true;
        }
        return failed ? 2 : 1;
    }

    private java.util.List<java.security.cert.X509Certificate> getCertificates(byte[] attestation) throws java.security.cert.CertificateException {
        java.util.List<java.security.cert.X509Certificate> certificates = new java.util.ArrayList<>();
        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(attestation);
        while (bis.available() > 0) {
            certificates.add((java.security.cert.X509Certificate) this.mCertificateFactory.generateCertificate(bis));
        }
        return certificates;
    }

    private boolean validateAttestationParameters(int localBindingType, android.os.Bundle requirements) {
        if (localBindingType != 2 && localBindingType != 3) {
            debugVerboseLog("Binding type is not supported: " + localBindingType);
            return false;
        }
        if (requirements.size() < 1) {
            debugVerboseLog("At least 1 requirement is required.");
            return false;
        }
        if (localBindingType == 2 && !requirements.containsKey("localbinding.public_key")) {
            debugVerboseLog("Requirements does not contain key: localbinding.public_key");
            return false;
        }
        if (localBindingType != 3 || requirements.containsKey("localbinding.challenge")) {
            return true;
        }
        debugVerboseLog("Requirements does not contain key: localbinding.challenge");
        return false;
    }

    private void validateCertificateChain(java.util.List<java.security.cert.X509Certificate> certificates) throws java.security.cert.CertificateException, java.security.cert.CertPathValidatorException, java.security.InvalidAlgorithmParameterException {
        if (certificates.size() < 2) {
            debugVerboseLog("Certificate chain less than 2 in size.");
            throw new java.security.cert.CertificateException("Certificate chain less than 2 in size.");
        }
        java.security.cert.CertPath certificatePath = this.mCertificateFactory.generateCertPath(certificates);
        java.security.cert.PKIXParameters validationParams = new java.security.cert.PKIXParameters(this.mTrustAnchors);
        if (this.mRevocationEnabled) {
            java.security.cert.PKIXCertPathChecker checker = new com.android.server.security.AttestationVerificationPeerDeviceVerifier.AndroidRevocationStatusListChecker();
            validationParams.addCertPathChecker(checker);
        }
        validationParams.setRevocationEnabled(false);
        this.mCertPathValidator.validate(certificatePath, validationParams);
    }

    private java.util.Set<java.security.cert.TrustAnchor> getTrustAnchors() throws java.security.cert.CertPathValidatorException {
        java.util.Set<java.security.cert.TrustAnchor> modifiableSet = new java.util.HashSet<>();
        try {
            for (java.lang.String certString : getTrustAnchorResources()) {
                modifiableSet.add(new java.security.cert.TrustAnchor((java.security.cert.X509Certificate) this.mCertificateFactory.generateCertificate(new java.io.ByteArrayInputStream(getCertificateBytes(certString))), null));
            }
            return java.util.Collections.unmodifiableSet(modifiableSet);
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
            throw new java.security.cert.CertPathValidatorException("Invalid trust anchor certificate.", e);
        }
    }

    private byte[] getCertificateBytes(java.lang.String certString) {
        java.lang.String formattedCertString = certString.replaceAll("\\s+", "\n");
        return formattedCertString.replaceAll("-BEGIN\\nCERTIFICATE-", "-BEGIN CERTIFICATE-").replaceAll("-END\\nCERTIFICATE-", "-END CERTIFICATE-").getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private java.lang.String[] getTrustAnchorResources() {
        return this.mContext.getResources().getStringArray(android.R.array.stoppable_fgs_system_apps);
    }

    private boolean checkLocalBindingRequirements(java.security.cert.X509Certificate leafCertificate, com.android.server.security.AndroidKeystoreAttestationVerificationAttributes attestationAttributes, int localBindingType, android.os.Bundle requirements, com.android.server.security.AttestationVerificationPeerDeviceVerifier.MyDumpData dumpData) {
        dumpData.mBindingType = localBindingType;
        switch (localBindingType) {
            case 2:
                boolean publicKeyMatches = checkPublicKey(leafCertificate, requirements.getByteArray("localbinding.public_key"));
                if (!publicKeyMatches) {
                    debugVerboseLog("Provided public key does not match leaf certificate public key.");
                    return false;
                }
                break;
            case 3:
                boolean attestationChallengeMatches = checkAttestationChallenge(attestationAttributes, requirements.getByteArray("localbinding.challenge"));
                if (!attestationChallengeMatches) {
                    debugVerboseLog("Provided challenge does not match leaf certificate challenge.");
                    return false;
                }
                break;
            default:
                throw new java.lang.IllegalArgumentException("Unsupported local binding type " + android.security.attestationverification.AttestationVerificationManager.localBindingTypeToString(localBindingType));
        }
        dumpData.mBindingOk = true;
        if (requirements.containsKey(PARAM_OWNED_BY_SYSTEM)) {
            dumpData.mSystemOwnershipChecked = true;
            if (requirements.getBoolean(PARAM_OWNED_BY_SYSTEM)) {
                boolean ownedBySystem = checkOwnedBySystem(leafCertificate, attestationAttributes);
                if (!ownedBySystem) {
                    debugVerboseLog("Certificate public key is not owned by the AndroidSystem.");
                    return false;
                }
                dumpData.mSystemOwned = true;
            } else {
                throw new java.lang.IllegalArgumentException("The value of the requirement key android.key_owned_by_system cannot be false. You can remove the key if you don't want to verify it.");
            }
        }
        return true;
    }

    private boolean checkAttestationForPeerDeviceProfile(com.android.server.security.AndroidKeystoreAttestationVerificationAttributes attestationAttributes, com.android.server.security.AttestationVerificationPeerDeviceVerifier.MyDumpData dumpData) {
        boolean result = true;
        if (attestationAttributes.getAttestationVersion() < 3) {
            debugVerboseLog("Attestation version is not at least 3 (Keymaster 4).");
            result = false;
        } else {
            dumpData.mAttestationVersionAtLeast3 = true;
        }
        if (attestationAttributes.getKeymasterVersion() < 4) {
            debugVerboseLog("Keymaster version is not at least 4.");
            result = false;
        } else {
            dumpData.mKeymasterVersionAtLeast4 = true;
        }
        if (attestationAttributes.getKeyOsVersion() < 100000) {
            debugVerboseLog("Android OS version is not 10+.");
            result = false;
        } else {
            dumpData.mOsVersionAtLeast10 = true;
        }
        if (!attestationAttributes.isAttestationHardwareBacked()) {
            debugVerboseLog("Key is not HW backed.");
            result = false;
        } else {
            dumpData.mKeyHwBacked = true;
        }
        if (!attestationAttributes.isKeymasterHardwareBacked()) {
            debugVerboseLog("Keymaster is not HW backed.");
            result = false;
        } else {
            dumpData.mKeymasterHwBacked = true;
        }
        if (attestationAttributes.getVerifiedBootState() != com.android.server.security.AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState.VERIFIED) {
            debugVerboseLog("Boot state not Verified.");
            result = false;
        } else {
            dumpData.mBootStateIsVerified = true;
        }
        try {
            if (!attestationAttributes.isVerifiedBootLocked()) {
                debugVerboseLog("Verified boot state is not locked.");
                result = false;
            } else {
                dumpData.mVerifiedBootStateLocked = true;
            }
        } catch (java.lang.IllegalStateException e) {
            debugVerboseLog("VerifiedBootLocked is not set.", e);
            result = false;
        }
        if (!isValidPatchLevel(attestationAttributes.getKeyOsPatchLevel())) {
            debugVerboseLog("OS patch level is not within valid range.");
            result = false;
        } else {
            dumpData.mOsPatchLevelInRange = true;
        }
        if (!isValidPatchLevel(attestationAttributes.getKeyBootPatchLevel())) {
            debugVerboseLog("Boot patch level is not within valid range.");
            result = false;
        } else {
            dumpData.mKeyBootPatchLevelInRange = true;
        }
        if (!isValidPatchLevel(attestationAttributes.getKeyVendorPatchLevel())) {
            debugVerboseLog("Vendor patch level is not within valid range.");
            result = false;
        } else {
            dumpData.mKeyVendorPatchLevelInRange = true;
        }
        if (!isValidPatchLevel(attestationAttributes.getKeyBootPatchLevel())) {
            debugVerboseLog("Boot patch level is not within valid range.");
            return false;
        }
        dumpData.mKeyBootPatchLevelInRange = true;
        return result;
    }

    private boolean checkPublicKey(java.security.cert.Certificate certificate, byte[] expectedPublicKey) {
        byte[] publicKey = certificate.getPublicKey().getEncoded();
        return java.util.Arrays.equals(publicKey, expectedPublicKey);
    }

    private boolean checkAttestationChallenge(com.android.server.security.AndroidKeystoreAttestationVerificationAttributes attestationAttributes, byte[] expectedChallenge) {
        byte[] challenge = attestationAttributes.getAttestationChallenge().toByteArray();
        return java.util.Arrays.equals(challenge, expectedChallenge);
    }

    private boolean checkOwnedBySystem(java.security.cert.X509Certificate certificate, com.android.server.security.AndroidKeystoreAttestationVerificationAttributes attestationAttributes) {
        java.util.Set<java.lang.String> ownerPackages = attestationAttributes.getApplicationPackageNameVersion().keySet();
        if (!ANDROID_SYSTEM_PACKAGE_NAME_SET.equals(ownerPackages)) {
            debugVerboseLog("Owner is not system, packages=" + ownerPackages);
            return false;
        }
        return true;
    }

    private boolean isValidPatchLevel(int patchLevel) {
        java.time.LocalDate localPatchDate;
        java.time.LocalDate currentDate = this.mTestSystemDate != null ? this.mTestSystemDate : java.time.LocalDate.now(java.time.ZoneId.systemDefault());
        try {
            if (this.mTestLocalPatchDate != null) {
                localPatchDate = this.mTestLocalPatchDate;
            } else {
                localPatchDate = java.time.LocalDate.parse(android.os.Build.VERSION.SECURITY_PATCH);
            }
            if (java.time.temporal.ChronoUnit.MONTHS.between(localPatchDate, currentDate) > 12) {
                return true;
            }
            java.lang.String remoteDeviceDateStr = java.lang.String.valueOf(patchLevel);
            if (remoteDeviceDateStr.length() == 6 || remoteDeviceDateStr.length() == 8) {
                int patchYear = java.lang.Integer.parseInt(remoteDeviceDateStr.substring(0, 4));
                int patchMonth = java.lang.Integer.parseInt(remoteDeviceDateStr.substring(4, 6));
                java.time.LocalDate remotePatchDate = java.time.LocalDate.of(patchYear, patchMonth, 1);
                return remotePatchDate.compareTo((java.time.chrono.ChronoLocalDate) localPatchDate) > 0 ? java.time.temporal.ChronoUnit.MONTHS.between(localPatchDate, remotePatchDate) <= 12 : remotePatchDate.compareTo((java.time.chrono.ChronoLocalDate) localPatchDate) >= 0 || java.time.temporal.ChronoUnit.MONTHS.between(remotePatchDate, localPatchDate) <= 12;
            }
            debugVerboseLog("Patch level is not in format YYYYMM or YYYYMMDD");
            return false;
        } catch (java.lang.Throwable th) {
            debugVerboseLog("Build.VERSION.SECURITY_PATCH: " + android.os.Build.VERSION.SECURITY_PATCH + " is not in format YYYY-MM-DD");
            return false;
        }
    }

    private final class AndroidRevocationStatusListChecker extends java.security.cert.PKIXCertPathChecker {
        private static final java.lang.String REASON_PROPERTY_KEY = "reason";
        private static final java.lang.String STATUS_PROPERTY_KEY = "status";
        private static final java.lang.String TOP_LEVEL_JSON_PROPERTY_KEY = "entries";
        private org.json.JSONObject mJsonStatusMap;
        private java.lang.String mStatusUrl;

        private AndroidRevocationStatusListChecker() {
        }

        @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
        public void init(boolean forward) throws java.security.cert.CertPathValidatorException {
            this.mStatusUrl = getRevocationListUrl();
            if (this.mStatusUrl == null || this.mStatusUrl.isEmpty()) {
                throw new java.security.cert.CertPathValidatorException("R.string.vendor_required_attestation_revocation_list_url is empty.");
            }
            this.mJsonStatusMap = getStatusMap(this.mStatusUrl);
        }

        @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
        public boolean isForwardCheckingSupported() {
            return false;
        }

        @Override // java.security.cert.PKIXCertPathChecker
        public java.util.Set<java.lang.String> getSupportedExtensions() {
            return null;
        }

        @Override // java.security.cert.PKIXCertPathChecker
        public void check(java.security.cert.Certificate cert, java.util.Collection<java.lang.String> unresolvedCritExts) throws java.security.cert.CertPathValidatorException {
            java.security.cert.X509Certificate x509Certificate = (java.security.cert.X509Certificate) cert;
            java.lang.String serialNumber = x509Certificate.getSerialNumber().toString(16);
            if (serialNumber == null) {
                throw new java.security.cert.CertPathValidatorException("Certificate serial number can not be null.");
            }
            if (this.mJsonStatusMap.has(serialNumber)) {
                try {
                    org.json.JSONObject revocationStatus = this.mJsonStatusMap.getJSONObject(serialNumber);
                    java.lang.String status = revocationStatus.getString(STATUS_PROPERTY_KEY);
                    java.lang.String reason = revocationStatus.getString("reason");
                    throw new java.security.cert.CertPathValidatorException("Invalid certificate with serial number " + serialNumber + " has status " + status + " because reason " + reason);
                } catch (java.lang.Throwable th) {
                    throw new java.security.cert.CertPathValidatorException("Unable get properties for certificate with serial number " + serialNumber);
                }
            }
        }

        private org.json.JSONObject getStatusMap(java.lang.String stringUrl) throws java.security.cert.CertPathValidatorException {
            try {
                java.net.URL url = new java.net.URL(stringUrl);
                try {
                    java.io.InputStream inputStream = url.openStream();
                    try {
                        org.json.JSONObject statusListJson = new org.json.JSONObject(new java.lang.String(inputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
                        org.json.JSONObject jSONObject = statusListJson.getJSONObject(TOP_LEVEL_JSON_PROPERTY_KEY);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return jSONObject;
                    } finally {
                    }
                } catch (java.lang.Throwable t) {
                    throw new java.security.cert.CertPathValidatorException("Unable to parse revocation status from " + this.mStatusUrl, t);
                }
            } catch (java.lang.Throwable t2) {
                throw new java.security.cert.CertPathValidatorException("Unable to get revocation status from " + this.mStatusUrl, t2);
            }
        }

        private java.lang.String getRevocationListUrl() {
            return com.android.server.security.AttestationVerificationPeerDeviceVerifier.this.mContext.getResources().getString(android.R.string.time_picker_radial_mode_description);
        }
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

    private static class MyDumpData extends com.android.server.security.AttestationVerificationManagerService.DumpData {
        boolean mAttestationParametersOk;
        boolean mAttestationVersionAtLeast3;
        boolean mBindingOk;
        int mBindingType;
        boolean mBootStateIsVerified;
        boolean mCertChainOk;
        boolean mCertPathValidatorAvailable;
        boolean mCertificationFactoryAvailable;
        boolean mKeyBootPatchLevelInRange;
        boolean mKeyHwBacked;
        boolean mKeyVendorPatchLevelInRange;
        boolean mKeymasterHwBacked;
        boolean mKeymasterVersionAtLeast4;
        boolean mOsPatchLevelInRange;
        boolean mOsVersionAtLeast10;
        int mResult;
        boolean mSystemOwned;
        boolean mSystemOwnershipChecked;
        boolean mVerifiedBootStateLocked;

        private MyDumpData() {
            this.mResult = -1;
            this.mCertificationFactoryAvailable = false;
            this.mCertPathValidatorAvailable = false;
            this.mAttestationParametersOk = false;
            this.mCertChainOk = false;
            this.mBindingOk = false;
            this.mBindingType = -1;
            this.mSystemOwnershipChecked = false;
            this.mSystemOwned = false;
            this.mOsVersionAtLeast10 = false;
            this.mKeyHwBacked = false;
            this.mAttestationVersionAtLeast3 = false;
            this.mKeymasterVersionAtLeast4 = false;
            this.mKeymasterHwBacked = false;
            this.mBootStateIsVerified = false;
            this.mVerifiedBootStateLocked = false;
            this.mOsPatchLevelInRange = false;
            this.mKeyBootPatchLevelInRange = false;
            this.mKeyVendorPatchLevelInRange = false;
        }

        @Override // com.android.server.security.AttestationVerificationManagerService.DumpData
        public void dumpTo(android.util.IndentingPrintWriter writer) {
            writer.println("Result: " + android.security.attestationverification.AttestationVerificationManager.verificationResultCodeToString(this.mResult));
            if (!this.mCertificationFactoryAvailable) {
                writer.println("Certificate Factory Unavailable");
                return;
            }
            if (!this.mCertPathValidatorAvailable) {
                writer.println("Cert Path Validator Unavailable");
                return;
            }
            if (!this.mAttestationParametersOk) {
                writer.println("Attestation parameters set incorrectly.");
                return;
            }
            writer.println("Certificate Chain Valid (inc. Trust Anchor): " + booleanToOkFail(this.mCertChainOk));
            if (!this.mCertChainOk) {
                return;
            }
            writer.println("Local Binding: " + booleanToOkFail(this.mBindingOk));
            writer.increaseIndent();
            writer.println("Binding Type: " + this.mBindingType);
            writer.decreaseIndent();
            if (this.mSystemOwnershipChecked) {
                writer.println("System Ownership: " + booleanToOkFail(this.mSystemOwned));
            }
            writer.println("KeyStore Attestation Parameters");
            writer.increaseIndent();
            writer.println("OS Version >= 10: " + booleanToOkFail(this.mOsVersionAtLeast10));
            writer.println("OS Patch Level in Range: " + booleanToOkFail(this.mOsPatchLevelInRange));
            writer.println("Attestation Version >= 3: " + booleanToOkFail(this.mAttestationVersionAtLeast3));
            writer.println("Keymaster Version >= 4: " + booleanToOkFail(this.mKeymasterVersionAtLeast4));
            writer.println("Keymaster HW-Backed: " + booleanToOkFail(this.mKeymasterHwBacked));
            writer.println("Key is HW Backed: " + booleanToOkFail(this.mKeyHwBacked));
            writer.println("Boot State is VERIFIED: " + booleanToOkFail(this.mBootStateIsVerified));
            writer.println("Verified Boot is LOCKED: " + booleanToOkFail(this.mVerifiedBootStateLocked));
            writer.println("Key Boot Level in Range: " + booleanToOkFail(this.mKeyBootPatchLevelInRange));
            writer.println("Key Vendor Patch Level in Range: " + booleanToOkFail(this.mKeyVendorPatchLevelInRange));
            writer.decreaseIndent();
        }

        private java.lang.String booleanToOkFail(boolean value) {
            return value ? "OK" : "FAILURE";
        }
    }
}
