package com.android.server.integrity;

/* JADX INFO: loaded from: classes2.dex */
public class AppIntegrityManagerServiceImpl extends android.content.integrity.IAppIntegrityManager.Stub {
    public static final java.lang.String ADB_INSTALLER = "adb";
    private static final java.lang.String ALLOWED_INSTALLERS_METADATA_NAME = "allowed-installers";
    private static final java.lang.String ALLOWED_INSTALLER_DELIMITER = ",";
    private static final java.lang.String BASE_APK_FILE = "base.apk";
    public static final boolean DEBUG_INTEGRITY_COMPONENT = false;
    private static final java.lang.String INSTALLER_PACKAGE_CERT_DELIMITER = "\\|";
    private static final java.util.Set<java.lang.String> PACKAGE_INSTALLER = new java.util.HashSet(java.util.Arrays.asList("com.google.android.packageinstaller", "com.android.packageinstaller"));
    private static final java.lang.String PACKAGE_MIME_TYPE = "application/vnd.android.package-archive";
    private static final java.lang.String TAG = "AppIntegrityManagerServiceImpl";
    private static final java.lang.String UNKNOWN_INSTALLER = "";
    private final android.content.Context mContext;
    private final com.android.server.integrity.engine.RuleEvaluationEngine mEvaluationEngine;
    private final android.os.Handler mHandler;
    private final com.android.server.integrity.IntegrityFileManager mIntegrityFileManager;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final java.util.function.Supplier<com.android.internal.pm.parsing.PackageParser2> mParserSupplier;

    public static com.android.server.integrity.AppIntegrityManagerServiceImpl create(android.content.Context context) {
        android.os.HandlerThread handlerThread = new android.os.HandlerThread("AppIntegrityManagerServiceHandler");
        handlerThread.start();
        return new com.android.server.integrity.AppIntegrityManagerServiceImpl(context, (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class), new java.util.function.Supplier() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.pm.parsing.PackageParserUtils.forParsingFileWithDefaults();
            }
        }, com.android.server.integrity.engine.RuleEvaluationEngine.getRuleEvaluationEngine(), com.android.server.integrity.IntegrityFileManager.getInstance(), handlerThread.getThreadHandler());
    }

    AppIntegrityManagerServiceImpl(android.content.Context context, android.content.pm.PackageManagerInternal packageManagerInternal, java.util.function.Supplier<com.android.internal.pm.parsing.PackageParser2> parserSupplier, com.android.server.integrity.engine.RuleEvaluationEngine evaluationEngine, com.android.server.integrity.IntegrityFileManager integrityFileManager, android.os.Handler handler) {
        this.mContext = context;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mParserSupplier = parserSupplier;
        this.mEvaluationEngine = evaluationEngine;
        this.mIntegrityFileManager = integrityFileManager;
        this.mHandler = handler;
        android.content.IntentFilter integrityVerificationFilter = new android.content.IntentFilter();
        integrityVerificationFilter.addAction("android.intent.action.PACKAGE_NEEDS_INTEGRITY_VERIFICATION");
        try {
            integrityVerificationFilter.addDataType(PACKAGE_MIME_TYPE);
            this.mContext.registerReceiver(new com.android.server.integrity.AppIntegrityManagerServiceImpl.AnonymousClass1(), integrityVerificationFilter, null, this.mHandler);
        } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
            throw new java.lang.RuntimeException("Mime type malformed: should never happen.", e);
        }
    }

    /* JADX INFO: renamed from: com.android.server.integrity.AppIntegrityManagerServiceImpl$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, final android.content.Intent intent) {
            if (!"android.intent.action.PACKAGE_NEEDS_INTEGRITY_VERIFICATION".equals(intent.getAction())) {
                return;
            }
            com.android.server.integrity.AppIntegrityManagerServiceImpl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0(intent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(android.content.Intent intent) {
            com.android.server.integrity.AppIntegrityManagerServiceImpl.this.handleIntegrityVerification(intent);
        }
    }

    public void updateRuleSet(final java.lang.String version, final android.content.pm.ParceledListSlice<android.content.integrity.Rule> rules, final android.content.IntentSender statusReceiver) {
        final java.lang.String ruleProvider = getCallerPackageNameOrThrow(android.os.Binder.getCallingUid());
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateRuleSet$0(version, ruleProvider, rules, statusReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$updateRuleSet$0(java.lang.String version, java.lang.String ruleProvider, android.content.pm.ParceledListSlice rules, android.content.IntentSender statusReceiver) {
        boolean z = 1;
        try {
            this.mIntegrityFileManager.writeRules(version, ruleProvider, rules.getList());
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error writing rules.", e);
            z = 0;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.INTEGRITY_RULES_PUSHED, z, ruleProvider, version);
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.content.integrity.extra.STATUS", !z);
        try {
            statusReceiver.sendIntent(this.mContext, 0, intent, null, null);
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "Error sending status feedback.", e2);
        }
    }

    public java.lang.String getCurrentRuleSetVersion() {
        getCallerPackageNameOrThrow(android.os.Binder.getCallingUid());
        com.android.server.integrity.model.RuleMetadata ruleMetadata = this.mIntegrityFileManager.readMetadata();
        if (ruleMetadata != null && ruleMetadata.getVersion() != null) {
            return ruleMetadata.getVersion();
        }
        return "";
    }

    public java.lang.String getCurrentRuleSetProvider() {
        getCallerPackageNameOrThrow(android.os.Binder.getCallingUid());
        com.android.server.integrity.model.RuleMetadata ruleMetadata = this.mIntegrityFileManager.readMetadata();
        if (ruleMetadata != null && ruleMetadata.getRuleProvider() != null) {
            return ruleMetadata.getRuleProvider();
        }
        return "";
    }

    public android.content.pm.ParceledListSlice<android.content.integrity.Rule> getCurrentRules() throws java.io.IOException, com.android.server.integrity.parser.RuleParseException {
        java.util.List<android.content.integrity.Rule> rules = java.util.Collections.emptyList();
        try {
            rules = this.mIntegrityFileManager.readRules(null);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error getting current rules", e);
        }
        return new android.content.pm.ParceledListSlice<>(rules);
    }

    public java.util.List<java.lang.String> getWhitelistedRuleProviders() {
        return getAllowedRuleProviderSystemApps();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIntegrityVerification(android.content.Intent intent) {
        int i;
        int verificationId = intent.getIntExtra("android.content.pm.extra.VERIFICATION_ID", -1);
        try {
            java.lang.String installerPackageName = getInstallerPackageName(intent);
            if (!integrityCheckIncludesRuleProvider() && isRuleProvider(installerPackageName)) {
                this.mPackageManagerInternal.setIntegrityVerificationResult(verificationId, 1);
                return;
            }
            java.lang.String packageName = intent.getStringExtra("android.intent.extra.PACKAGE_NAME");
            android.util.Pair<android.content.pm.SigningDetails, android.os.Bundle> packageSigningAndMetadata = getPackageSigningAndMetadata(intent.getData());
            if (packageSigningAndMetadata == null) {
                android.util.Slog.w(TAG, "Cannot parse package " + packageName);
                this.mPackageManagerInternal.setIntegrityVerificationResult(verificationId, 1);
                return;
            }
            android.content.pm.SigningDetails signingDetails = (android.content.pm.SigningDetails) packageSigningAndMetadata.first;
            java.util.List<java.lang.String> appCertificates = getCertificateFingerprint(packageName, signingDetails);
            java.util.List<java.lang.String> appCertificateLineage = getCertificateLineage(packageName, signingDetails);
            java.util.List<java.lang.String> installerCertificates = getInstallerCertificateFingerprint(installerPackageName);
            android.content.integrity.AppInstallMetadata.Builder builder = new android.content.integrity.AppInstallMetadata.Builder();
            builder.setPackageName(getPackageNameNormalized(packageName));
            builder.setAppCertificates(appCertificates);
            builder.setAppCertificateLineage(appCertificateLineage);
            builder.setVersionCode(intent.getLongExtra("android.intent.extra.LONG_VERSION_CODE", -1L));
            builder.setInstallerName(getPackageNameNormalized(installerPackageName));
            builder.setInstallerCertificates(installerCertificates);
            builder.setIsPreInstalled(isSystemApp(packageName));
            java.util.Map<java.lang.String, java.lang.String> allowedInstallers = getAllowedInstallers((android.os.Bundle) packageSigningAndMetadata.second);
            builder.setAllowedInstallersAndCert(allowedInstallers);
            extractSourceStamp(intent.getData(), builder);
            android.content.integrity.AppInstallMetadata appInstallMetadata = builder.build();
            com.android.server.integrity.model.IntegrityCheckResult result = this.mEvaluationEngine.evaluate(appInstallMetadata);
            if (!result.getMatchedRules().isEmpty()) {
                android.util.Slog.i(TAG, java.lang.String.format("Integrity check of %s result: %s due to %s", packageName, result.getEffect(), result.getMatchedRules()));
            }
            com.android.internal.util.FrameworkStatsLog.write(247, packageName, appCertificates.toString(), appInstallMetadata.getVersionCode(), installerPackageName, result.getLoggingResponse(), result.isCausedByAppCertRule(), result.isCausedByInstallerRule());
            android.content.pm.PackageManagerInternal packageManagerInternal = this.mPackageManagerInternal;
            if (result.getEffect() == com.android.server.integrity.model.IntegrityCheckResult.Effect.ALLOW) {
                i = 1;
            } else {
                i = 0;
            }
            packageManagerInternal.setIntegrityVerificationResult(verificationId, i);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Invalid input to integrity verification", e);
            this.mPackageManagerInternal.setIntegrityVerificationResult(verificationId, 0);
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "Error handling integrity verification", e2);
            this.mPackageManagerInternal.setIntegrityVerificationResult(verificationId, 1);
        }
    }

    private java.lang.String getInstallerPackageName(android.content.Intent intent) {
        java.lang.String installer = intent.getStringExtra("android.content.pm.extra.VERIFICATION_INSTALLER_PACKAGE");
        if (com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(installer)) {
            return ADB_INSTALLER;
        }
        int installerUid = intent.getIntExtra("android.content.pm.extra.VERIFICATION_INSTALLER_UID", -1);
        if (installerUid < 0) {
            android.util.Slog.e(TAG, "Installer cannot be determined: installer: " + installer + " installer UID: " + installerUid);
            return "";
        }
        if (!getPackageListForUid(installerUid).contains(installer)) {
            return "";
        }
        if (PACKAGE_INSTALLER.contains(installer)) {
            int originatingUid = intent.getIntExtra("android.intent.extra.ORIGINATING_UID", -1);
            if (originatingUid < 0) {
                android.util.Slog.e(TAG, "Installer is package installer but originating UID not found.");
                return "";
            }
            java.util.List<java.lang.String> installerPackages = getPackageListForUid(originatingUid);
            if (installerPackages.isEmpty()) {
                android.util.Slog.e(TAG, "No package found associated with originating UID " + originatingUid);
                return "";
            }
            return installerPackages.get(0);
        }
        return installer;
    }

    private java.lang.String getPackageNameNormalized(java.lang.String packageName) {
        if (packageName.length() <= 32) {
            return packageName;
        }
        try {
            java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(packageName.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return android.content.integrity.IntegrityUtils.getHexDigest(hashBytes);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private java.util.List<java.lang.String> getInstallerCertificateFingerprint(java.lang.String installer) {
        if (installer.equals(ADB_INSTALLER) || installer.equals("")) {
            return java.util.Collections.emptyList();
        }
        com.android.server.pm.pkg.AndroidPackage installerPkg = this.mPackageManagerInternal.getPackage(installer);
        if (installerPkg == null) {
            android.util.Slog.w(TAG, "Installer package " + installer + " not found.");
            return java.util.Collections.emptyList();
        }
        return getCertificateFingerprint(installerPkg.getPackageName(), installerPkg.getSigningDetails());
    }

    private java.util.List<java.lang.String> getCertificateFingerprint(java.lang.String packageName, android.content.pm.SigningDetails signingDetails) {
        java.util.ArrayList<java.lang.String> certificateFingerprints = new java.util.ArrayList<>();
        for (android.content.pm.Signature signature : getSignatures(packageName, signingDetails)) {
            certificateFingerprints.add(getFingerprint(signature));
        }
        return certificateFingerprints;
    }

    private java.util.List<java.lang.String> getCertificateLineage(java.lang.String packageName, android.content.pm.SigningDetails signingDetails) {
        java.util.ArrayList<java.lang.String> certificateLineage = new java.util.ArrayList<>();
        for (android.content.pm.Signature signature : getSignatureLineage(packageName, signingDetails)) {
            certificateLineage.add(getFingerprint(signature));
        }
        return certificateLineage;
    }

    private java.util.Map<java.lang.String, java.lang.String> getAllowedInstallers(android.os.Bundle metaData) {
        java.lang.String allowedInstallers;
        java.util.Map<java.lang.String, java.lang.String> packageCertMap = new java.util.HashMap<>();
        if (metaData != null && (allowedInstallers = metaData.getString(ALLOWED_INSTALLERS_METADATA_NAME)) != null) {
            java.lang.String[] installerCertPairs = allowedInstallers.split(ALLOWED_INSTALLER_DELIMITER);
            for (java.lang.String packageCertPair : installerCertPairs) {
                java.lang.String[] packageAndCert = packageCertPair.split(INSTALLER_PACKAGE_CERT_DELIMITER);
                if (packageAndCert.length == 2) {
                    java.lang.String packageName = getPackageNameNormalized(packageAndCert[0]);
                    java.lang.String cert = packageAndCert[1];
                    packageCertMap.put(packageName, cert);
                } else if (packageAndCert.length == 1) {
                    packageCertMap.put(getPackageNameNormalized(packageAndCert[0]), "");
                }
            }
        }
        return packageCertMap;
    }

    private void extractSourceStamp(android.net.Uri dataUri, android.content.integrity.AppInstallMetadata.Builder appInstallMetadata) {
        android.util.apk.SourceStampVerificationResult sourceStampVerificationResult;
        java.io.File installationPath = getInstallationPath(dataUri);
        if (installationPath == null) {
            throw new java.lang.IllegalArgumentException("Installation path is null, package not found");
        }
        if (installationPath.isDirectory()) {
            try {
                java.util.stream.Stream<java.nio.file.Path> filesList = java.nio.file.Files.list(installationPath.toPath());
                try {
                    java.util.List<java.lang.String> apkFiles = (java.util.List) filesList.map(new java.util.function.Function() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return ((java.nio.file.Path) obj).toAbsolutePath().toString();
                        }
                    }).filter(new java.util.function.Predicate() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda1
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return ((java.lang.String) obj).endsWith(".apk");
                        }
                    }).collect(java.util.stream.Collectors.toList());
                    sourceStampVerificationResult = android.util.apk.SourceStampVerifier.verify(apkFiles);
                    if (filesList != null) {
                        filesList.close();
                    }
                } finally {
                }
            } catch (java.io.IOException e) {
                throw new java.lang.IllegalArgumentException("Could not read APK directory");
            }
        } else {
            sourceStampVerificationResult = android.util.apk.SourceStampVerifier.verify(installationPath.getAbsolutePath());
        }
        appInstallMetadata.setIsStampPresent(sourceStampVerificationResult.isPresent());
        appInstallMetadata.setIsStampVerified(sourceStampVerificationResult.isVerified());
        appInstallMetadata.setIsStampTrusted(sourceStampVerificationResult.isVerified());
        if (sourceStampVerificationResult.isVerified()) {
            java.security.cert.X509Certificate sourceStampCertificate = (java.security.cert.X509Certificate) sourceStampVerificationResult.getCertificate();
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                byte[] certificateDigest = digest.digest(sourceStampCertificate.getEncoded());
                appInstallMetadata.setStampCertificateHash(android.content.integrity.IntegrityUtils.getHexDigest(certificateDigest));
            } catch (java.security.NoSuchAlgorithmException | java.security.cert.CertificateEncodingException e2) {
                throw new java.lang.IllegalArgumentException("Error computing source stamp certificate digest", e2);
            }
        }
    }

    private static android.content.pm.Signature[] getSignatures(java.lang.String packageName, android.content.pm.SigningDetails signingDetails) {
        android.content.pm.Signature[] signatures = signingDetails.getSignatures();
        if (signatures == null || signatures.length < 1) {
            throw new java.lang.IllegalArgumentException("Package signature not found in " + packageName);
        }
        return signatures;
    }

    private static android.content.pm.Signature[] getSignatureLineage(java.lang.String packageName, android.content.pm.SigningDetails signingDetails) {
        android.content.pm.Signature[] signatureLineage = getSignatures(packageName, signingDetails);
        android.content.pm.Signature[] pastSignatures = signingDetails.getPastSigningCertificates();
        if (signatureLineage.length == 1 && !com.android.internal.util.ArrayUtils.isEmpty(pastSignatures)) {
            android.content.pm.Signature[] allSignatures = new android.content.pm.Signature[signatureLineage.length + pastSignatures.length];
            int i = 0;
            while (i < signatureLineage.length) {
                allSignatures[i] = signatureLineage[i];
                i++;
            }
            for (android.content.pm.Signature signature : pastSignatures) {
                allSignatures[i] = signature;
                i++;
            }
            return allSignatures;
        }
        return signatureLineage;
    }

    private static java.lang.String getFingerprint(android.content.pm.Signature cert) {
        java.io.InputStream input = new java.io.ByteArrayInputStream(cert.toByteArray());
        try {
            java.security.cert.CertificateFactory factory = java.security.cert.CertificateFactory.getInstance("X509");
            java.security.cert.X509Certificate certificate = null;
            if (factory != null) {
                try {
                    certificate = (java.security.cert.X509Certificate) factory.generateCertificate(input);
                } catch (java.security.cert.CertificateException e) {
                    throw new java.lang.RuntimeException("Error getting X509Certificate", e);
                }
            }
            if (certificate == null) {
                throw new java.lang.RuntimeException("X509 Certificate not found");
            }
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                byte[] publicKey = digest.digest(certificate.getEncoded());
                return android.content.integrity.IntegrityUtils.getHexDigest(publicKey);
            } catch (java.security.NoSuchAlgorithmException | java.security.cert.CertificateEncodingException e2) {
                throw new java.lang.IllegalArgumentException("Error error computing fingerprint", e2);
            }
        } catch (java.security.cert.CertificateException e3) {
            throw new java.lang.RuntimeException("Error getting CertificateFactory", e3);
        }
    }

    private android.util.Pair<android.content.pm.SigningDetails, android.os.Bundle> getPackageSigningAndMetadata(android.net.Uri dataUri) {
        java.io.File installationPath = getInstallationPath(dataUri);
        if (installationPath == null) {
            throw new java.lang.IllegalArgumentException("Installation path is null, package not found");
        }
        try {
            com.android.internal.pm.parsing.PackageParser2 parser = this.mParserSupplier.get();
            try {
                com.android.internal.pm.parsing.pkg.ParsedPackage pkg = parser.parsePackage(installationPath, 0, false);
                android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
                android.content.pm.parsing.result.ParseResult<android.content.pm.SigningDetails> result = com.android.internal.pm.pkg.parsing.ParsingPackageUtils.getSigningDetails(input, pkg, true);
                if (result.isError()) {
                    android.util.Slog.w(TAG, result.getErrorMessage(), result.getException());
                    if (parser != null) {
                        parser.close();
                    }
                    return null;
                }
                android.util.Pair<android.content.pm.SigningDetails, android.os.Bundle> pairCreate = android.util.Pair.create((android.content.pm.SigningDetails) result.getResult(), pkg.getMetaData());
                if (parser != null) {
                    parser.close();
                }
                return pairCreate;
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception reading " + dataUri, e);
            return null;
        }
    }

    private android.content.pm.PackageInfo getMultiApkInfo(java.io.File multiApkDirectory) {
        java.io.File baseFile = new java.io.File(multiApkDirectory, BASE_APK_FILE);
        android.content.pm.PackageInfo basePackageInfo = this.mContext.getPackageManager().getPackageArchiveInfo(baseFile.getAbsolutePath(), 134217856);
        if (basePackageInfo == null) {
            java.io.File[] fileArrListFiles = multiApkDirectory.listFiles();
            int length = fileArrListFiles.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                java.io.File apkFile = fileArrListFiles[i];
                if (!apkFile.isDirectory()) {
                    try {
                        basePackageInfo = this.mContext.getPackageManager().getPackageArchiveInfo(apkFile.getAbsolutePath(), 134217856);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.w(TAG, "Exception reading " + apkFile, e);
                    }
                    if (basePackageInfo != null) {
                        android.util.Slog.i(TAG, "Found package info from " + apkFile);
                        break;
                    }
                }
                i++;
            }
        }
        if (basePackageInfo == null) {
            throw new java.lang.IllegalArgumentException("Base package info cannot be found from installation directory");
        }
        return basePackageInfo;
    }

    private java.io.File getInstallationPath(android.net.Uri dataUri) {
        if (dataUri == null) {
            throw new java.lang.IllegalArgumentException("Null data uri");
        }
        java.lang.String scheme = dataUri.getScheme();
        if (!"file".equalsIgnoreCase(scheme)) {
            throw new java.lang.IllegalArgumentException("Unsupported scheme for " + dataUri);
        }
        java.io.File installationPath = new java.io.File(dataUri.getPath());
        if (!installationPath.exists()) {
            throw new java.lang.IllegalArgumentException("Cannot find file for " + dataUri);
        }
        if (!installationPath.canRead()) {
            throw new java.lang.IllegalArgumentException("Cannot read file for " + dataUri);
        }
        return installationPath;
    }

    private java.lang.String getCallerPackageNameOrThrow(int callingUid) {
        java.lang.String callerPackageName = getCallingRulePusherPackageName(callingUid);
        if (callerPackageName == null) {
            throw new java.lang.SecurityException("Only system packages specified in config_integrityRuleProviderPackages are allowed to call this method.");
        }
        return callerPackageName;
    }

    private java.lang.String getCallingRulePusherPackageName(int callingUid) {
        java.util.List<java.lang.String> allowedRuleProviders = getAllowedRuleProviderSystemApps();
        java.util.List<java.lang.String> callingPackageNames = getPackageListForUid(callingUid);
        java.util.List<java.lang.String> allowedCallingPackages = new java.util.ArrayList<>();
        for (java.lang.String packageName : callingPackageNames) {
            if (allowedRuleProviders.contains(packageName)) {
                allowedCallingPackages.add(packageName);
            }
        }
        if (allowedCallingPackages.isEmpty()) {
            return null;
        }
        return allowedCallingPackages.get(0);
    }

    private boolean isRuleProvider(java.lang.String installerPackageName) {
        for (java.lang.String ruleProvider : getAllowedRuleProviderSystemApps()) {
            if (ruleProvider.matches(installerPackageName)) {
                return true;
            }
        }
        return false;
    }

    private java.util.List<java.lang.String> getAllowedRuleProviderSystemApps() {
        java.util.List<java.lang.String> integrityRuleProviders = java.util.Arrays.asList(this.mContext.getResources().getStringArray(android.R.array.config_highDisplayBrightnessThresholdsOfFixedRefreshRate));
        java.util.List<java.lang.String> systemAppRuleProviders = new java.util.ArrayList<>();
        for (java.lang.String ruleProvider : integrityRuleProviders) {
            if (isSystemApp(ruleProvider)) {
                systemAppRuleProviders.add(ruleProvider);
            }
        }
        return systemAppRuleProviders;
    }

    private boolean isSystemApp(java.lang.String packageName) {
        try {
            android.content.pm.PackageInfo existingPackageInfo = this.mContext.getPackageManager().getPackageInfo(packageName, 0);
            if (existingPackageInfo.applicationInfo != null) {
                return existingPackageInfo.applicationInfo.isSystemApp();
            }
            return false;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean integrityCheckIncludesRuleProvider() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "verify_integrity_for_rule_provider", 0) == 1;
    }

    private java.util.List<java.lang.String> getPackageListForUid(int uid) {
        try {
            return java.util.Arrays.asList(this.mContext.getPackageManager().getPackagesForUid(uid));
        } catch (java.lang.NullPointerException e) {
            android.util.Slog.w(TAG, java.lang.String.format("No packages were found for uid: %d", java.lang.Integer.valueOf(uid)));
            return java.util.List.of();
        }
    }
}
