package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class BinaryTransparencyService extends com.android.server.SystemService {
    static final java.lang.String APEX_PRELOAD_LOCATION_ERROR = "could-not-be-determined";
    static final java.lang.String BINARY_HASH_ERROR = "SHA256HashError";
    private static final boolean DEBUG = false;
    static final int DIGEST_ALGORITHM_CHUNKED_SHA256 = 1;
    static final int DIGEST_ALGORITHM_CHUNKED_SHA512 = 2;
    static final int DIGEST_ALGORITHM_SHA256 = 4;
    static final int DIGEST_ALGORITHM_UNKNOWN = 0;
    static final int DIGEST_ALGORITHM_VERITY_CHUNKED_SHA256 = 3;
    static final java.lang.String KEY_ENABLE_BIOMETRIC_PROPERTY_VERIFICATION = "enable_biometric_property_verification";
    public static final long LOG_MBA_INFO = 245692487;
    static final int MBA_STATUS_ERROR = 0;
    static final int MBA_STATUS_NEW_INSTALL = 3;
    static final int MBA_STATUS_PRELOADED = 1;
    static final int MBA_STATUS_UPDATED_NEW_INSTALL = 4;
    static final int MBA_STATUS_UPDATED_PRELOAD = 2;
    static final long RECORD_MEASUREMENTS_COOLDOWN_MS = 86400000;
    static final java.lang.String SYSPROP_NAME_VBETA_DIGEST = "ro.boot.vbmeta.digest";
    private static final java.lang.String TAG = "TransparencyService";
    static final java.lang.String VBMETA_DIGEST_UNAVAILABLE = "vbmeta-digest-unavailable";
    static final java.lang.String VBMETA_DIGEST_UNINITIALIZED = "vbmeta-digest-uninitialized";
    private static final com.android.modules.expresslog.Histogram digestAllPackagesLatency = new com.android.modules.expresslog.Histogram("binary_transparency.value_digest_all_packages_latency_uniform", new com.android.modules.expresslog.Histogram.UniformOptions(50, 0.0f, 500.0f));
    private com.android.server.BinaryTransparencyService.BiometricLogger mBiometricLogger;
    private final android.content.Context mContext;
    private long mMeasurementsLastRecordedMs;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl mServiceImpl;
    private java.lang.String mVbmetaDigest;

    final class BinaryTransparencyServiceImpl extends com.android.internal.os.IBinaryTransparencyService.Stub {
        BinaryTransparencyServiceImpl() {
        }

        public java.lang.String getSignedImageInfo() {
            return com.android.server.BinaryTransparencyService.this.mVbmetaDigest;
        }

        private java.lang.String[] computePackageSignerSha256Digests(android.content.pm.SigningInfo signingInfo) {
            if (signingInfo == null) {
                android.util.Slog.e(com.android.server.BinaryTransparencyService.TAG, "signingInfo is null");
                return null;
            }
            android.content.pm.Signature[] packageSigners = signingInfo.getApkContentsSigners();
            java.util.List<java.lang.String> resultList = new java.util.ArrayList<>();
            for (android.content.pm.Signature packageSigner : packageSigners) {
                byte[] digest = android.util.PackageUtils.computeSha256DigestBytes(packageSigner.toByteArray());
                java.lang.String digestHexString = libcore.util.HexEncoding.encodeToString(digest, false);
                resultList.add(digestHexString);
            }
            return (java.lang.String[]) resultList.toArray(new java.lang.String[1]);
        }

        private java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> collectAppInfo(com.android.server.pm.pkg.PackageState packageState, int mbaStatus) {
            java.util.ArrayList<com.android.internal.os.IBinaryTransparencyService.AppInfo> results = new java.util.ArrayList<>();
            java.lang.String packageName = packageState.getPackageName();
            long versionCode = packageState.getVersionCode();
            java.lang.String[] signerDigests = computePackageSignerSha256Digests(packageState.getSigningInfo());
            com.android.server.pm.pkg.AndroidPackage pkg = packageState.getAndroidPackage();
            for (com.android.server.pm.pkg.AndroidPackageSplit split : pkg.getSplits()) {
                com.android.internal.os.IBinaryTransparencyService.AppInfo appInfo = new com.android.internal.os.IBinaryTransparencyService.AppInfo();
                appInfo.packageName = packageName;
                appInfo.longVersion = versionCode;
                appInfo.splitName = split.getName();
                appInfo.signerDigests = signerDigests;
                appInfo.mbaStatus = mbaStatus;
                com.android.server.BinaryTransparencyService.Digest digest = measureApk(split.getPath());
                appInfo.digest = digest.value();
                appInfo.digestAlgorithm = digest.algorithm();
                results.add(appInfo);
            }
            com.android.internal.os.IBinaryTransparencyService.AppInfo base = results.get(0);
            android.content.pm.InstallSourceInfo installSourceInfo = com.android.server.BinaryTransparencyService.this.getInstallSourceInfo(packageState.getPackageName());
            if (installSourceInfo != null) {
                base.initiator = installSourceInfo.getInitiatingPackageName();
                android.content.pm.SigningInfo initiatorSignerInfo = installSourceInfo.getInitiatingPackageSigningInfo();
                if (initiatorSignerInfo != null) {
                    base.initiatorSignerDigests = computePackageSignerSha256Digests(initiatorSignerInfo);
                }
                base.installer = installSourceInfo.getInstallingPackageName();
                base.originator = installSourceInfo.getOriginatingPackageName();
            }
            return results;
        }

        private com.android.server.BinaryTransparencyService.Digest measureApk(java.lang.String apkPath) {
            java.util.Map<java.lang.Integer, byte[]> contentDigests = computeApkContentDigest(apkPath);
            if (contentDigests == null) {
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Failed to compute content digest for " + apkPath);
            } else {
                int i = 1;
                if (contentDigests.containsKey(1)) {
                    return new com.android.server.BinaryTransparencyService.Digest(i, contentDigests.get(1));
                }
                int i2 = 2;
                if (contentDigests.containsKey(2)) {
                    return new com.android.server.BinaryTransparencyService.Digest(i2, contentDigests.get(2));
                }
            }
            byte[] digest = android.util.PackageUtils.computeSha256DigestForLargeFileAsBytes(apkPath, android.util.PackageUtils.createLargeFileBuffer());
            return new com.android.server.BinaryTransparencyService.Digest(4, digest);
        }

        public void recordMeasurementsForAllPackages() {
            long currentTimeMs = java.lang.System.currentTimeMillis();
            if (currentTimeMs - com.android.server.BinaryTransparencyService.this.mMeasurementsLastRecordedMs < 86400000) {
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Skip measurement since the last measurement was only taken at " + com.android.server.BinaryTransparencyService.this.mMeasurementsLastRecordedMs + " within the cooldown period");
                return;
            }
            android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Measurement was last taken at " + com.android.server.BinaryTransparencyService.this.mMeasurementsLastRecordedMs + " and is now updated to: " + currentTimeMs);
            com.android.server.BinaryTransparencyService.this.mMeasurementsLastRecordedMs = currentTimeMs;
            android.os.Bundle packagesMeasured = new android.os.Bundle();
            java.util.List<com.android.internal.os.IBinaryTransparencyService.ApexInfo> allApexInfo = collectAllApexInfo(false);
            for (com.android.internal.os.IBinaryTransparencyService.ApexInfo apexInfo : allApexInfo) {
                packagesMeasured.putBoolean(apexInfo.packageName, true);
                recordApexInfo(apexInfo);
            }
            java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> allUpdatedPreloadInfo = collectAllUpdatedPreloadInfo(packagesMeasured);
            for (com.android.internal.os.IBinaryTransparencyService.AppInfo appInfo : allUpdatedPreloadInfo) {
                packagesMeasured.putBoolean(appInfo.packageName, true);
                writeAppInfoToLog(appInfo);
            }
            if (android.app.compat.CompatChanges.isChangeEnabled(com.android.server.BinaryTransparencyService.LOG_MBA_INFO)) {
                java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> allMbaInfo = collectAllSilentInstalledMbaInfo(packagesMeasured);
                for (com.android.internal.os.IBinaryTransparencyService.AppInfo appInfo2 : allMbaInfo) {
                    packagesMeasured.putBoolean(appInfo2.packageName, true);
                    writeAppInfoToLog(appInfo2);
                }
            }
            long timeSpentMeasuring = java.lang.System.currentTimeMillis() - currentTimeMs;
            com.android.server.BinaryTransparencyService.digestAllPackagesLatency.logSample(timeSpentMeasuring);
        }

        public java.util.List<com.android.internal.os.IBinaryTransparencyService.ApexInfo> collectAllApexInfo(boolean includeTestOnly) {
            java.util.ArrayList<com.android.internal.os.IBinaryTransparencyService.ApexInfo> results = new java.util.ArrayList<>();
            for (android.content.pm.PackageInfo packageInfo : com.android.server.BinaryTransparencyService.this.getCurrentInstalledApexs()) {
                com.android.server.pm.pkg.PackageState packageState = com.android.server.BinaryTransparencyService.this.mPackageManagerInternal.getPackageStateInternal(packageInfo.packageName);
                if (packageState == null) {
                    android.util.Slog.w(com.android.server.BinaryTransparencyService.TAG, "Package state is unavailable, ignoring the APEX " + packageInfo.packageName);
                } else {
                    com.android.server.pm.pkg.AndroidPackage pkg = packageState.getAndroidPackage();
                    if (pkg == null) {
                        android.util.Slog.w(com.android.server.BinaryTransparencyService.TAG, "Skipping the missing APK in " + pkg.getPath());
                    } else {
                        com.android.server.BinaryTransparencyService.Digest apexChecksum = measureApk(pkg.getPath());
                        if (apexChecksum == null) {
                            android.util.Slog.w(com.android.server.BinaryTransparencyService.TAG, "Skipping the missing APEX in " + pkg.getPath());
                        } else {
                            com.android.internal.os.IBinaryTransparencyService.ApexInfo apexInfo = new com.android.internal.os.IBinaryTransparencyService.ApexInfo();
                            apexInfo.packageName = packageState.getPackageName();
                            apexInfo.longVersion = packageState.getVersionCode();
                            apexInfo.digest = apexChecksum.value();
                            apexInfo.digestAlgorithm = apexChecksum.algorithm();
                            apexInfo.signerDigests = computePackageSignerSha256Digests(packageState.getSigningInfo());
                            if (includeTestOnly) {
                                apexInfo.moduleName = com.android.server.BinaryTransparencyService.this.apexPackageNameToModuleName(packageState.getPackageName());
                            }
                            results.add(apexInfo);
                        }
                    }
                }
            }
            return results;
        }

        public java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> collectAllUpdatedPreloadInfo(final android.os.Bundle packagesToSkip) {
            final java.util.ArrayList<com.android.internal.os.IBinaryTransparencyService.AppInfo> results = new java.util.ArrayList<>();
            com.android.server.BinaryTransparencyService.this.mContext.getPackageManager();
            com.android.server.BinaryTransparencyService.this.mPackageManagerInternal.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.BinaryTransparencyService$BinaryTransparencyServiceImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$collectAllUpdatedPreloadInfo$0(packagesToSkip, results, (com.android.server.pm.pkg.PackageStateInternal) obj);
                }
            });
            return results;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$collectAllUpdatedPreloadInfo$0(android.os.Bundle packagesToSkip, java.util.ArrayList results, com.android.server.pm.pkg.PackageStateInternal packageState) {
            if (!packageState.isUpdatedSystemApp() || packagesToSkip.containsKey(packageState.getPackageName())) {
                return;
            }
            android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Preload " + packageState.getPackageName() + " at " + packageState.getPath() + " has likely been updated.");
            java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> resultsForApp = collectAppInfo(packageState, 2);
            results.addAll(resultsForApp);
        }

        public java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> collectAllSilentInstalledMbaInfo(android.os.Bundle packagesToSkip) {
            java.util.ArrayList<com.android.internal.os.IBinaryTransparencyService.AppInfo> results = new java.util.ArrayList<>();
            for (android.content.pm.PackageInfo packageInfo : com.android.server.BinaryTransparencyService.this.getNewlyInstalledMbas()) {
                if (!packagesToSkip.containsKey(packageInfo.packageName)) {
                    com.android.server.pm.pkg.PackageState packageState = com.android.server.BinaryTransparencyService.this.mPackageManagerInternal.getPackageStateInternal(packageInfo.packageName);
                    if (packageState == null) {
                        android.util.Slog.w(com.android.server.BinaryTransparencyService.TAG, "Package state is unavailable, ignoring the package " + packageInfo.packageName);
                    } else {
                        java.util.List<com.android.internal.os.IBinaryTransparencyService.AppInfo> resultsForApp = collectAppInfo(packageState, 3);
                        results.addAll(resultsForApp);
                    }
                }
            }
            return results;
        }

        private void recordApexInfo(com.android.internal.os.IBinaryTransparencyService.ApexInfo apexInfo) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APEX_INFO_GATHERED, apexInfo.packageName, apexInfo.longVersion, apexInfo.digest != null ? libcore.util.HexEncoding.encodeToString(apexInfo.digest, false) : null, apexInfo.digestAlgorithm, apexInfo.signerDigests);
        }

        private void writeAppInfoToLog(com.android.internal.os.IBinaryTransparencyService.AppInfo appInfo) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.MOBILE_BUNDLED_APP_INFO_GATHERED, appInfo.packageName, appInfo.longVersion, appInfo.digest != null ? libcore.util.HexEncoding.encodeToString(appInfo.digest, false) : null, appInfo.digestAlgorithm, appInfo.signerDigests, appInfo.mbaStatus, appInfo.initiator, appInfo.initiatorSignerDigests, appInfo.installer, appInfo.originator, appInfo.splitName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.util.Map<java.lang.Integer, byte[]> computeApkContentDigest(java.lang.String pathToApk) {
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            android.content.pm.parsing.result.ParseResult<android.util.apk.ApkSignatureVerifier.SigningDetailsWithDigests> parseResult = android.util.apk.ApkSignatureVerifier.verifySignaturesInternal(input, pathToApk, 2, false);
            if (parseResult.isError()) {
                android.util.Slog.e(com.android.server.BinaryTransparencyService.TAG, "Failed to compute content digest for " + pathToApk + " due to: " + parseResult.getErrorMessage());
                return null;
            }
            return ((android.util.apk.ApkSignatureVerifier.SigningDetailsWithDigests) parseResult.getResult()).contentDigests;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.BinaryTransparencyService$BinaryTransparencyServiceImpl$1] */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new android.os.ShellCommand() { // from class: com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl.1
                private int printSignedImageInfo() {
                    java.io.PrintWriter pw = getOutPrintWriter();
                    boolean listAllPartitions = false;
                    while (true) {
                        java.lang.String opt = getNextOption();
                        byte b = 0;
                        if (opt != null) {
                            switch (opt.hashCode()) {
                                case 1492:
                                    if (!opt.equals("-a")) {
                                    }
                                default:
                                    b = -1;
                                    break;
                            }
                            switch (b) {
                                case 0:
                                    listAllPartitions = true;
                                    break;
                                default:
                                    pw.println("ERROR: Unknown option: " + opt);
                                    return 1;
                            }
                        } else {
                            java.lang.String signedImageInfo = com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl.this.getSignedImageInfo();
                            pw.println("Image Info:");
                            pw.println(android.os.Build.FINGERPRINT);
                            pw.println(signedImageInfo);
                            pw.println("");
                            if (listAllPartitions) {
                                android.content.pm.PackageManager pm = com.android.server.BinaryTransparencyService.this.mContext.getPackageManager();
                                if (pm == null) {
                                    pw.println("ERROR: Failed to obtain an instance of package manager.");
                                    return -1;
                                }
                                pw.println("Other partitions:");
                                java.util.List<android.os.Build.Partition> buildPartitions = android.os.Build.getFingerprintedPartitions();
                                for (android.os.Build.Partition buildPartition : buildPartitions) {
                                    pw.println("Name: " + buildPartition.getName());
                                    pw.println("Fingerprint: " + buildPartition.getFingerprint());
                                    pw.println("Build time (ms): " + buildPartition.getBuildTimeMillis());
                                }
                            }
                            return 0;
                        }
                    }
                }

                private void printPackageMeasurements(android.content.pm.PackageInfo packageInfo, boolean useSha256, java.io.PrintWriter pw) {
                    java.util.Map<java.lang.Integer, byte[]> contentDigests = com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl.this.computeApkContentDigest(packageInfo.applicationInfo.sourceDir);
                    if (contentDigests == null) {
                        pw.println("ERROR: Failed to compute package content digest for " + packageInfo.applicationInfo.sourceDir);
                        return;
                    }
                    if (useSha256) {
                        byte[] fileBuff = android.util.PackageUtils.createLargeFileBuffer();
                        java.lang.String hexEncodedSha256Digest = android.util.PackageUtils.computeSha256DigestForLargeFile(packageInfo.applicationInfo.sourceDir, fileBuff);
                        pw.print(hexEncodedSha256Digest + ",");
                    }
                    for (java.util.Map.Entry<java.lang.Integer, byte[]> entry : contentDigests.entrySet()) {
                        java.lang.Integer algorithmId = entry.getKey();
                        byte[] contentDigest = entry.getValue();
                        pw.print(com.android.server.BinaryTransparencyService.this.translateContentDigestAlgorithmIdToString(algorithmId.intValue()));
                        pw.print(":");
                        pw.print(libcore.util.HexEncoding.encodeToString(contentDigest, false));
                        pw.print("\n");
                    }
                }

                private void printPackageInstallationInfo(android.content.pm.PackageInfo packageInfo, boolean useSha256, java.io.PrintWriter pw) {
                    pw.println("--- Package Installation Info ---");
                    pw.println("Current install location: " + packageInfo.applicationInfo.sourceDir);
                    if (packageInfo.applicationInfo.sourceDir.startsWith("/data/apex/")) {
                        java.lang.String origPackageFilepath = com.android.server.BinaryTransparencyService.this.getOriginalApexPreinstalledLocation(packageInfo.packageName);
                        pw.println("|--> Pre-installed package install location: " + origPackageFilepath);
                        if (!origPackageFilepath.equals(com.android.server.BinaryTransparencyService.APEX_PRELOAD_LOCATION_ERROR)) {
                            if (useSha256) {
                                java.lang.String sha256Digest = android.util.PackageUtils.computeSha256DigestForLargeFile(origPackageFilepath, android.util.PackageUtils.createLargeFileBuffer());
                                pw.println("|--> Pre-installed package SHA-256 digest: " + sha256Digest);
                            }
                            java.util.Map<java.lang.Integer, byte[]> contentDigests = com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl.this.computeApkContentDigest(origPackageFilepath);
                            if (contentDigests == null) {
                                pw.println("|--> ERROR: Failed to compute package content digest for " + origPackageFilepath);
                            } else {
                                for (java.util.Map.Entry<java.lang.Integer, byte[]> entry : contentDigests.entrySet()) {
                                    java.lang.Integer algorithmId = entry.getKey();
                                    byte[] contentDigest = entry.getValue();
                                    pw.println("|--> Pre-installed package content digest: " + libcore.util.HexEncoding.encodeToString(contentDigest, false));
                                    pw.println("|--> Pre-installed package content digest algorithm: " + com.android.server.BinaryTransparencyService.this.translateContentDigestAlgorithmIdToString(algorithmId.intValue()));
                                }
                            }
                        }
                    }
                    pw.println("First install time (ms): " + packageInfo.firstInstallTime);
                    pw.println("Last update time (ms):   " + packageInfo.lastUpdateTime);
                    boolean isPreloaded = packageInfo.firstInstallTime == packageInfo.lastUpdateTime;
                    pw.println("Is preloaded: " + isPreloaded);
                    android.content.pm.InstallSourceInfo installSourceInfo = com.android.server.BinaryTransparencyService.this.getInstallSourceInfo(packageInfo.packageName);
                    if (installSourceInfo == null) {
                        pw.println("ERROR: Unable to obtain installSourceInfo of " + packageInfo.packageName);
                    } else {
                        pw.println("Installation initiated by: " + installSourceInfo.getInitiatingPackageName());
                        pw.println("Installation done by: " + installSourceInfo.getInstallingPackageName());
                        pw.println("Installation originating from: " + installSourceInfo.getOriginatingPackageName());
                    }
                    if (packageInfo.isApex) {
                        pw.println("Is an active APEX: " + packageInfo.isActiveApex);
                    }
                }

                private void printPackageSignerDetails(android.content.pm.SigningInfo signerInfo, java.io.PrintWriter pw) {
                    if (signerInfo == null) {
                        pw.println("ERROR: Package's signingInfo is null.");
                        return;
                    }
                    pw.println("--- Package Signer Info ---");
                    pw.println("Has multiple signers: " + signerInfo.hasMultipleSigners());
                    pw.println("Signing key has been rotated: " + signerInfo.hasPastSigningCertificates());
                    android.content.pm.Signature[] packageSigners = signerInfo.getApkContentsSigners();
                    for (android.content.pm.Signature packageSigner : packageSigners) {
                        byte[] packageSignerDigestBytes = android.util.PackageUtils.computeSha256DigestBytes(packageSigner.toByteArray());
                        java.lang.String packageSignerDigestHextring = libcore.util.HexEncoding.encodeToString(packageSignerDigestBytes, false);
                        pw.println("Signer cert's SHA256-digest: " + packageSignerDigestHextring);
                        try {
                            java.security.PublicKey publicKey = packageSigner.getPublicKey();
                            pw.println("Signing key algorithm: " + publicKey.getAlgorithm());
                        } catch (java.security.cert.CertificateException e) {
                            android.util.Slog.e("ShellCommand", "Failed to obtain public key of signer for cert with hash: " + packageSignerDigestHextring, e);
                        }
                    }
                    if (!signerInfo.hasMultipleSigners() && signerInfo.hasPastSigningCertificates()) {
                        pw.println("== Signing Cert Lineage (Excluding The Most Recent) ==");
                        pw.println("(Certs are sorted in the order of rotation, beginning with the original signing cert)");
                        android.content.pm.Signature[] signingCertHistory = signerInfo.getSigningCertificateHistory();
                        for (int i = 0; i < signingCertHistory.length - 1; i++) {
                            android.content.pm.Signature signature = signingCertHistory[i];
                            byte[] signatureDigestBytes = android.util.PackageUtils.computeSha256DigestBytes(signature.toByteArray());
                            java.lang.String certHashHexString = libcore.util.HexEncoding.encodeToString(signatureDigestBytes, false);
                            pw.println("  ++ Signer cert #" + (i + 1) + " ++");
                            pw.println("  Cert SHA256-digest: " + certHashHexString);
                            try {
                                java.security.PublicKey publicKey2 = signature.getPublicKey();
                                pw.println("  Signing key algorithm: " + publicKey2.getAlgorithm());
                            } catch (java.security.cert.CertificateException e2) {
                                android.util.Slog.e("ShellCommand", "Failed to obtain public key of signer for cert with hash: " + certHashHexString, e2);
                            }
                        }
                    }
                }

                private void printModuleDetails(android.content.pm.ModuleInfo moduleInfo, java.io.PrintWriter pw) {
                    pw.println("--- Module Details ---");
                    pw.println("Module name: " + ((java.lang.Object) moduleInfo.getName()));
                    pw.println("Module visibility: " + (moduleInfo.isHidden() ? "hidden" : com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES));
                }

                private void printAppDetails(android.content.pm.PackageInfo packageInfo, boolean printLibraries, java.io.PrintWriter pw) {
                    pw.println("--- App Details ---");
                    pw.println("Name: " + packageInfo.applicationInfo.name);
                    pw.println("Label: " + ((java.lang.Object) com.android.server.BinaryTransparencyService.this.mContext.getPackageManager().getApplicationLabel(packageInfo.applicationInfo)));
                    pw.println("Description: " + ((java.lang.Object) packageInfo.applicationInfo.loadDescription(com.android.server.BinaryTransparencyService.this.mContext.getPackageManager())));
                    pw.println("Has code: " + packageInfo.applicationInfo.hasCode());
                    pw.println("Is enabled: " + packageInfo.applicationInfo.enabled);
                    pw.println("Is suspended: " + ((packageInfo.applicationInfo.flags & 1073741824) != 0));
                    pw.println("Compile SDK version: " + packageInfo.compileSdkVersion);
                    pw.println("Target SDK version: " + packageInfo.applicationInfo.targetSdkVersion);
                    pw.println("Is privileged: " + packageInfo.applicationInfo.isPrivilegedApp());
                    pw.println("Is a stub: " + packageInfo.isStub);
                    pw.println("Is a core app: " + packageInfo.coreApp);
                    pw.println("SEInfo: " + packageInfo.applicationInfo.seInfo);
                    pw.println("Component factory: " + packageInfo.applicationInfo.appComponentFactory);
                    pw.println("Process name: " + packageInfo.applicationInfo.processName);
                    pw.println("Task affinity: " + packageInfo.applicationInfo.taskAffinity);
                    pw.println("UID: " + packageInfo.applicationInfo.uid);
                    pw.println("Shared UID: " + packageInfo.sharedUserId);
                    if (printLibraries) {
                        pw.println("== App's Shared Libraries ==");
                        java.util.List<android.content.pm.SharedLibraryInfo> sharedLibraryInfos = packageInfo.applicationInfo.getSharedLibraryInfos();
                        if (sharedLibraryInfos == null || sharedLibraryInfos.isEmpty()) {
                            pw.println("<none>");
                        }
                        for (int i = 0; i < sharedLibraryInfos.size(); i++) {
                            android.content.pm.SharedLibraryInfo sharedLibraryInfo = sharedLibraryInfos.get(i);
                            pw.println("  ++ Library #" + (i + 1) + " ++");
                            pw.println("  Lib name: " + sharedLibraryInfo.getName());
                            long libVersion = sharedLibraryInfo.getLongVersion();
                            pw.print("  Lib version: ");
                            if (libVersion == -1) {
                                pw.print("undefined");
                            } else {
                                pw.print(libVersion);
                            }
                            pw.print("\n");
                            pw.println("  Lib package name (if available): " + sharedLibraryInfo.getPackageName());
                            pw.println("  Lib path: " + sharedLibraryInfo.getPath());
                            pw.print("  Lib type: ");
                            switch (sharedLibraryInfo.getType()) {
                                case 0:
                                    pw.print("built-in");
                                    break;
                                case 1:
                                    pw.print("dynamic");
                                    break;
                                case 2:
                                    pw.print("static");
                                    break;
                                case 3:
                                    pw.print("SDK");
                                    break;
                                default:
                                    pw.print("undefined");
                                    break;
                            }
                            pw.print("\n");
                            pw.println("  Is a native lib: " + sharedLibraryInfo.isNative());
                        }
                    }
                }

                private void printHeadersHelper(java.lang.String packageType, boolean useSha256, java.io.PrintWriter pw) {
                    pw.print(packageType + " Info [Format: package_name,package_version,");
                    if (useSha256) {
                        pw.print("package_sha256_digest,");
                    }
                    pw.print("content_digest_algorithm:content_digest]:\n");
                }

                private int printAllApexs() {
                    java.io.PrintWriter pw = getOutPrintWriter();
                    boolean verbose = false;
                    boolean useSha256 = false;
                    boolean printHeaders = true;
                    while (true) {
                        java.lang.String opt = getNextOption();
                        byte b = -1;
                        if (opt != null) {
                            switch (opt.hashCode()) {
                                case 1506:
                                    if (opt.equals("-o")) {
                                        b = 2;
                                    }
                                    break;
                                case 1513:
                                    if (opt.equals("-v")) {
                                        b = 0;
                                    }
                                    break;
                                case 43009159:
                                    if (opt.equals("--old")) {
                                        b = 3;
                                    }
                                    break;
                                case 967085338:
                                    if (opt.equals("--no-headers")) {
                                        b = 4;
                                    }
                                    break;
                                case 1737088994:
                                    if (opt.equals("--verbose")) {
                                        b = 1;
                                    }
                                    break;
                            }
                            switch (b) {
                                case 0:
                                case 1:
                                    verbose = true;
                                    break;
                                case 2:
                                case 3:
                                    useSha256 = true;
                                    break;
                                case 4:
                                    printHeaders = false;
                                    break;
                                default:
                                    pw.println("ERROR: Unknown option: " + opt);
                                    return 1;
                            }
                        } else {
                            android.content.pm.PackageManager pm = com.android.server.BinaryTransparencyService.this.mContext.getPackageManager();
                            if (pm == null) {
                                pw.println("ERROR: Failed to obtain an instance of package manager.");
                                return -1;
                            }
                            if (!verbose && printHeaders) {
                                printHeadersHelper("APEX", useSha256, pw);
                            }
                            for (android.content.pm.PackageInfo packageInfo : com.android.server.BinaryTransparencyService.this.getCurrentInstalledApexs()) {
                                if (verbose && printHeaders) {
                                    printHeadersHelper("APEX", useSha256, pw);
                                }
                                java.lang.String packageName = packageInfo.packageName;
                                pw.print(packageName + "," + packageInfo.getLongVersionCode() + ",");
                                printPackageMeasurements(packageInfo, useSha256, pw);
                                if (verbose) {
                                    try {
                                        android.content.pm.ModuleInfo moduleInfo = pm.getModuleInfo(packageInfo.packageName, 0);
                                        pw.println("Is a module: true");
                                        printModuleDetails(moduleInfo, pw);
                                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                                        pw.println("Is a module: false");
                                    }
                                    printPackageInstallationInfo(packageInfo, useSha256, pw);
                                    printPackageSignerDetails(packageInfo.signingInfo, pw);
                                    pw.println("");
                                }
                            }
                            return 0;
                        }
                    }
                }

                private int printAllModules() {
                    boolean printHeaders;
                    java.lang.String opt;
                    android.content.pm.PackageInfo packageInfo;
                    byte b;
                    java.io.PrintWriter pw = getOutPrintWriter();
                    boolean printHeaders2 = true;
                    boolean useSha256 = false;
                    boolean useSha2562 = false;
                    while (true) {
                        java.lang.String nextOption = getNextOption();
                        java.lang.String opt2 = nextOption;
                        if (nextOption != null) {
                            switch (opt2.hashCode()) {
                                case 1506:
                                    b = opt2.equals("-o") ? (byte) 2 : (byte) -1;
                                    break;
                                case 1513:
                                    b = opt2.equals("-v") ? (byte) 0 : (byte) -1;
                                    break;
                                case 43009159:
                                    b = opt2.equals("--old") ? (byte) 3 : (byte) -1;
                                    break;
                                case 967085338:
                                    b = opt2.equals("--no-headers") ? (byte) 4 : (byte) -1;
                                    break;
                                case 1737088994:
                                    b = opt2.equals("--verbose") ? (byte) 1 : (byte) -1;
                                    break;
                                default:
                                    b = -1;
                                    break;
                            }
                            switch (b) {
                                case 0:
                                case 1:
                                    useSha2562 = true;
                                    break;
                                case 2:
                                case 3:
                                    useSha256 = true;
                                    break;
                                case 4:
                                    printHeaders2 = false;
                                    break;
                                default:
                                    pw.println("ERROR: Unknown option: " + opt2);
                                    return 1;
                            }
                        } else {
                            android.content.pm.PackageManager pm = com.android.server.BinaryTransparencyService.this.mContext.getPackageManager();
                            if (pm == null) {
                                pw.println("ERROR: Failed to obtain an instance of package manager.");
                                return -1;
                            }
                            if (!useSha2562 && printHeaders2) {
                                printHeadersHelper("Module", useSha256, pw);
                            }
                            for (android.content.pm.ModuleInfo module : pm.getInstalledModules(131072)) {
                                java.lang.String packageName = module.getPackageName();
                                if (useSha2562 && printHeaders2) {
                                    printHeadersHelper("Module", useSha256, pw);
                                }
                                try {
                                    packageInfo = pm.getPackageInfo(packageName, 1207959552);
                                    pw.print(packageInfo.packageName + ",");
                                    printHeaders = printHeaders2;
                                    opt = opt2;
                                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                                    printHeaders = printHeaders2;
                                    opt = opt2;
                                }
                                try {
                                    pw.print(packageInfo.getLongVersionCode() + ",");
                                    printPackageMeasurements(packageInfo, useSha256, pw);
                                    if (useSha2562) {
                                        printModuleDetails(module, pw);
                                        printPackageInstallationInfo(packageInfo, useSha256, pw);
                                        printPackageSignerDetails(packageInfo.signingInfo, pw);
                                        pw.println("");
                                    }
                                    opt2 = opt;
                                    printHeaders2 = printHeaders;
                                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                                    pw.println(packageName + ",ERROR:Unable to find PackageInfo for this module.");
                                    if (useSha2562) {
                                        printModuleDetails(module, pw);
                                        pw.println("");
                                    }
                                    opt2 = opt;
                                    printHeaders2 = printHeaders;
                                }
                            }
                            return 0;
                        }
                    }
                }

                /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
                /* JADX WARN: Removed duplicated region for block: B:29:0x0066  */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                private int printAllMbas() {
                    /*
                        Method dump skipped, instruction units count: 510
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl.AnonymousClass1.printAllMbas():int");
                }

                public int onCommand(java.lang.String cmd) {
                    byte b;
                    if (cmd == null) {
                        return handleDefaultCommands(cmd);
                    }
                    java.io.PrintWriter pw = getOutPrintWriter();
                    byte b2 = 0;
                    switch (cmd.hashCode()) {
                        case 102230:
                            if (cmd.equals("get")) {
                                b = 0;
                                break;
                            }
                        default:
                            b = -1;
                            break;
                    }
                    switch (b) {
                        case 0:
                            java.lang.String infoType = getNextArg();
                            if (infoType == null) {
                                printHelpMenu();
                                break;
                            } else {
                                switch (infoType.hashCode()) {
                                    case -1443097326:
                                        if (!infoType.equals("image_info")) {
                                            b2 = -1;
                                        }
                                        break;
                                    case -1195140447:
                                        b2 = !infoType.equals("module_info") ? (byte) -1 : (byte) 2;
                                        break;
                                    case 636812193:
                                        b2 = !infoType.equals("mba_info") ? (byte) -1 : (byte) 3;
                                        break;
                                    case 1366866347:
                                        b2 = !infoType.equals("apex_info") ? (byte) -1 : (byte) 1;
                                        break;
                                    default:
                                        b2 = -1;
                                        break;
                                }
                                switch (b2) {
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        break;
                                    default:
                                        pw.println(java.lang.String.format("ERROR: Unknown info type '%s'", infoType));
                                        break;
                                }
                            }
                            break;
                    }
                    return handleDefaultCommands(cmd);
                }

                private void printHelpMenu() {
                    java.io.PrintWriter pw = getOutPrintWriter();
                    pw.println("Transparency manager (transparency) commands:");
                    pw.println("  help");
                    pw.println("    Print this help text.");
                    pw.println("");
                    pw.println("  get image_info [-a]");
                    pw.println("    Print information about loaded image (firmware). Options:");
                    pw.println("        -a: lists all other identifiable partitions.");
                    pw.println("");
                    pw.println("  get apex_info [-o] [-v] [--no-headers]");
                    pw.println("    Print information about installed APEXs on device.");
                    pw.println("      -o: also uses the old digest scheme (SHA256) to compute APEX hashes. WARNING: This can be a very slow and CPU-intensive computation.");
                    pw.println("      -v: lists more verbose information about each APEX.");
                    pw.println("      --no-headers: does not print the header if specified.");
                    pw.println("");
                    pw.println("  get module_info [-o] [-v] [--no-headers]");
                    pw.println("    Print information about installed modules on device.");
                    pw.println("      -o: also uses the old digest scheme (SHA256) to compute module hashes. WARNING: This can be a very slow and CPU-intensive computation.");
                    pw.println("      -v: lists more verbose information about each module.");
                    pw.println("      --no-headers: does not print the header if specified.");
                    pw.println("");
                    pw.println("  get mba_info [-o] [-v] [-l] [--no-headers] [--preloads-only]");
                    pw.println("    Print information about installed mobile bundle apps (MBAs on device).");
                    pw.println("      -o: also uses the old digest scheme (SHA256) to compute MBA hashes. WARNING: This can be a very slow and CPU-intensive computation.");
                    pw.println("      -v: lists more verbose information about each app.");
                    pw.println("      -l: lists shared library info. (This option only works when -v option is also specified)");
                    pw.println("      --no-headers: does not print the header if specified.");
                    pw.println("      --preloads-only: lists only preloaded apps. This options can also be combined with others.");
                    pw.println("");
                }

                public void onHelp() {
                    printHelpMenu();
                }
            }.exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    public static class BiometricLogger {
        private static final java.lang.String TAG = "BiometricLogger";
        private static final com.android.server.BinaryTransparencyService.BiometricLogger sInstance = new com.android.server.BinaryTransparencyService.BiometricLogger();

        private BiometricLogger() {
        }

        public static com.android.server.BinaryTransparencyService.BiometricLogger getInstance() {
            return sInstance;
        }

        public void logStats(int sensorId, int modality, int sensorType, int sensorStrength, java.lang.String componentId, java.lang.String hardwareVersion, java.lang.String firmwareVersion, java.lang.String serialNumber, java.lang.String softwareVersion) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BIOMETRIC_PROPERTIES_COLLECTED, sensorId, modality, sensorType, sensorStrength, componentId, hardwareVersion, firmwareVersion, serialNumber, softwareVersion);
        }
    }

    public BinaryTransparencyService(android.content.Context context) {
        this(context, com.android.server.BinaryTransparencyService.BiometricLogger.getInstance());
    }

    BinaryTransparencyService(android.content.Context context, com.android.server.BinaryTransparencyService.BiometricLogger biometricLogger) {
        super(context);
        this.mContext = context;
        this.mServiceImpl = new com.android.server.BinaryTransparencyService.BinaryTransparencyServiceImpl();
        this.mVbmetaDigest = VBMETA_DIGEST_UNINITIALIZED;
        this.mMeasurementsLastRecordedMs = 0L;
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mBiometricLogger = biometricLogger;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        try {
            publishBinderService("transparency", this.mServiceImpl);
            android.util.Slog.i(TAG, "Started BinaryTransparencyService");
        } catch (java.lang.Throwable t) {
            android.util.Slog.e(TAG, "Failed to start BinaryTransparencyService.", t);
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 1000) {
            android.util.Slog.i(TAG, "Boot completed. Getting boot integrity data.");
            collectBootIntegrityInfo();
            android.util.Slog.i(TAG, "Boot completed. Collecting biometric system properties.");
            collectBiometricProperties();
            android.util.Slog.i(TAG, "Scheduling measurements to be taken.");
            com.android.server.BinaryTransparencyService.UpdateMeasurementsJobService.scheduleBinaryMeasurements(this.mContext, this);
            registerAllPackageUpdateObservers();
        }
    }

    public static class UpdateMeasurementsJobService extends android.app.job.JobService {
        private static final int DO_BINARY_MEASUREMENTS_JOB_ID = 1740526926;
        private static long sTimeLastRanMs = 0;

        @Override // android.app.job.JobService
        public boolean onStartJob(final android.app.job.JobParameters params) {
            android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Job to update binary measurements started.");
            if (params.getJobId() != DO_BINARY_MEASUREMENTS_JOB_ID) {
                return false;
            }
            java.util.concurrent.Executors.defaultThreadFactory().newThread(new java.lang.Runnable() { // from class: com.android.server.BinaryTransparencyService$UpdateMeasurementsJobService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStartJob$0(params);
                }
            }).start();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartJob$0(android.app.job.JobParameters params) {
            android.os.IBinder b = android.os.ServiceManager.getService("transparency");
            com.android.internal.os.IBinaryTransparencyService iBtsService = com.android.internal.os.IBinaryTransparencyService.Stub.asInterface(b);
            try {
                iBtsService.recordMeasurementsForAllPackages();
                sTimeLastRanMs = java.lang.System.currentTimeMillis();
                jobFinished(params, false);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.BinaryTransparencyService.TAG, "Taking binary measurements was interrupted.", e);
            }
        }

        @Override // android.app.job.JobService
        public boolean onStopJob(android.app.job.JobParameters params) {
            return false;
        }

        static void scheduleBinaryMeasurements(android.content.Context context, com.android.server.BinaryTransparencyService service) {
            android.util.Slog.i(com.android.server.BinaryTransparencyService.TAG, "Scheduling binary content-digest computation job");
            android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
            if (jobScheduler == null) {
                android.util.Slog.e(com.android.server.BinaryTransparencyService.TAG, "Failed to obtain an instance of JobScheduler.");
                return;
            }
            if (jobScheduler.getPendingJob(DO_BINARY_MEASUREMENTS_JOB_ID) != null) {
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "A measurement job has already been scheduled.");
                return;
            }
            long minWaitingPeriodMs = 0;
            if (sTimeLastRanMs != 0) {
                long minWaitingPeriodMs2 = 86400000 - (java.lang.System.currentTimeMillis() - sTimeLastRanMs);
                minWaitingPeriodMs = java.lang.Math.max(0L, java.lang.Math.min(minWaitingPeriodMs2, 86400000L));
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Scheduling the next measurement to be done at least " + minWaitingPeriodMs + "ms from now.");
            }
            android.app.job.JobInfo jobInfo = new android.app.job.JobInfo.Builder(DO_BINARY_MEASUREMENTS_JOB_ID, new android.content.ComponentName(context, (java.lang.Class<?>) com.android.server.BinaryTransparencyService.UpdateMeasurementsJobService.class)).setRequiresDeviceIdle(true).setRequiresCharging(true).setMinimumLatency(minWaitingPeriodMs).build();
            if (jobScheduler.schedule(jobInfo) != 1) {
                android.util.Slog.e(com.android.server.BinaryTransparencyService.TAG, "Failed to schedule job to measure binaries.");
            } else {
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, android.text.TextUtils.formatSimple("Job %d to measure binaries was scheduled successfully.", new java.lang.Object[]{java.lang.Integer.valueOf(DO_BINARY_MEASUREMENTS_JOB_ID)}));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int toFingerprintSensorType(int sensorType) {
        switch (sensorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int toFaceSensorType(int sensorType) {
        switch (sensorType) {
            case 1:
                return 6;
            case 2:
                return 7;
            default:
                return 0;
        }
    }

    private int toSensorStrength(int sensorStrength) {
        switch (sensorStrength) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logBiometricProperties(android.hardware.biometrics.SensorProperties prop, int modality, int sensorType) {
        int sensorId = prop.getSensorId();
        int sensorStrength = toSensorStrength(prop.getSensorStrength());
        for (android.hardware.biometrics.SensorProperties.ComponentInfo componentInfo : prop.getComponentInfo()) {
            this.mBiometricLogger.logStats(sensorId, modality, sensorType, sensorStrength, componentInfo.getComponentId().trim(), componentInfo.getHardwareVersion().trim(), componentInfo.getFirmwareVersion().trim(), componentInfo.getSerialNumber().trim(), componentInfo.getSoftwareVersion().trim());
        }
    }

    void collectBiometricProperties() {
        if (!android.provider.DeviceConfig.getBoolean("biometrics", KEY_ENABLE_BIOMETRIC_PROPERTY_VERIFICATION, true)) {
            return;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.hardware.fingerprint.FingerprintManager fpManager = null;
        android.hardware.face.FaceManager faceManager = null;
        if (pm != null && pm.hasSystemFeature("android.hardware.fingerprint")) {
            fpManager = (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
        }
        if (pm != null && pm.hasSystemFeature("android.hardware.biometrics.face")) {
            faceManager = (android.hardware.face.FaceManager) this.mContext.getSystemService(android.hardware.face.FaceManager.class);
        }
        if (fpManager != null) {
            fpManager.addAuthenticatorsRegisteredCallback(new android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.BinaryTransparencyService.1
                public void onAllAuthenticatorsRegistered(java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> sensors) {
                    for (android.hardware.fingerprint.FingerprintSensorPropertiesInternal propInternal : sensors) {
                        android.hardware.fingerprint.FingerprintSensorProperties prop = android.hardware.fingerprint.FingerprintSensorProperties.from(propInternal);
                        com.android.server.BinaryTransparencyService.this.logBiometricProperties(prop, 1, com.android.server.BinaryTransparencyService.this.toFingerprintSensorType(prop.getSensorType()));
                    }
                }
            });
        }
        if (faceManager != null) {
            faceManager.addAuthenticatorsRegisteredCallback(new android.hardware.face.IFaceAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.server.BinaryTransparencyService.2
                public void onAllAuthenticatorsRegistered(java.util.List<android.hardware.face.FaceSensorPropertiesInternal> sensors) {
                    for (android.hardware.face.FaceSensorPropertiesInternal propInternal : sensors) {
                        android.hardware.face.FaceSensorProperties prop = android.hardware.face.FaceSensorProperties.from(propInternal);
                        com.android.server.BinaryTransparencyService.this.logBiometricProperties(prop, 4, com.android.server.BinaryTransparencyService.this.toFaceSensorType(prop.getSensorType()));
                    }
                }
            });
        }
    }

    private void collectBootIntegrityInfo() {
        this.mVbmetaDigest = android.os.SystemProperties.get(SYSPROP_NAME_VBETA_DIGEST, VBMETA_DIGEST_UNAVAILABLE);
        android.util.Slog.d(TAG, java.lang.String.format("VBMeta Digest: %s", this.mVbmetaDigest));
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED, this.mVbmetaDigest);
        if (android.security.Flags.binaryTransparencySepolicyHash()) {
            com.android.server.IoThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.BinaryTransparencyService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$collectBootIntegrityInfo$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectBootIntegrityInfo$0() {
        byte[] sepolicyHash = android.util.PackageUtils.computeSha256DigestForLargeFileAsBytes("/sys/fs/selinux/policy", android.util.PackageUtils.createLargeFileBuffer());
        java.lang.String sepolicyHashEncoded = null;
        if (sepolicyHash != null) {
            sepolicyHashEncoded = libcore.util.HexEncoding.encodeToString(sepolicyHash, false);
            android.util.Slog.d(TAG, "sepolicy hash: " + sepolicyHashEncoded);
        }
        com.android.internal.util.FrameworkStatsLog.write(775, sepolicyHashEncoded, this.mVbmetaDigest);
    }

    private void registerApkAndNonStagedApexUpdateListener() {
        android.util.Slog.d(TAG, "Registering APK & Non-Staged APEX updates...");
        android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.PACKAGE_ADDED");
        filter.addDataScheme("package");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_ADDED@PACKAGE=REPLACING");
        this.mContext.registerReceiver(new com.android.server.BinaryTransparencyService.PackageUpdatedReceiver(), filter);
    }

    private void registerStagedApexUpdateObserver() {
        android.util.Slog.d(TAG, "Registering APEX updates...");
        android.content.pm.IPackageManagerNative iPackageManagerNative = android.content.pm.IPackageManagerNative.Stub.asInterface(android.os.ServiceManager.getService("package_native"));
        if (iPackageManagerNative == null) {
            android.util.Slog.e(TAG, "IPackageManagerNative is null");
            return;
        }
        try {
            iPackageManagerNative.registerStagedApexObserver(new android.content.pm.IStagedApexObserver.Stub() { // from class: com.android.server.BinaryTransparencyService.3
                public void onApexStaged(android.content.pm.ApexStagedEvent event) throws android.os.RemoteException {
                    android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "A new APEX has been staged for update. There are currently " + event.stagedApexModuleNames.length + " APEX(s) staged for update. Scheduling measurement...");
                    com.android.server.BinaryTransparencyService.UpdateMeasurementsJobService.scheduleBinaryMeasurements(com.android.server.BinaryTransparencyService.this.mContext, com.android.server.BinaryTransparencyService.this);
                }
            });
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to register a StagedApexObserver.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPackagePreloaded(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            pm.getPackageInfo(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(2097152L));
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPackageAnApex(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            android.content.pm.PackageInfo packageInfo = pm.getPackageInfo(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(1073741824L));
            return packageInfo.isApex;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private class PackageUpdatedReceiver extends android.content.BroadcastReceiver {
        private PackageUpdatedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (!intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                return;
            }
            android.net.Uri data = intent.getData();
            if (data == null) {
                android.util.Slog.e(com.android.server.BinaryTransparencyService.TAG, "Shouldn't happen: intent data is null!");
                return;
            }
            if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, "Not an update. Skipping...");
                return;
            }
            java.lang.String packageName = data.getSchemeSpecificPart();
            if (com.android.server.BinaryTransparencyService.this.isPackagePreloaded(packageName) || com.android.server.BinaryTransparencyService.this.isPackageAnApex(packageName)) {
                android.util.Slog.d(com.android.server.BinaryTransparencyService.TAG, packageName + " was updated. Scheduling measurement...");
                com.android.server.BinaryTransparencyService.UpdateMeasurementsJobService.scheduleBinaryMeasurements(com.android.server.BinaryTransparencyService.this.mContext, com.android.server.BinaryTransparencyService.this);
            }
        }
    }

    private void registerAllPackageUpdateObservers() {
        registerApkAndNonStagedApexUpdateListener();
        registerStagedApexUpdateObserver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String translateContentDigestAlgorithmIdToString(int algorithmId) {
        switch (algorithmId) {
            case 1:
                return "CHUNKED_SHA256";
            case 2:
                return "CHUNKED_SHA512";
            case 3:
                return "VERITY_CHUNKED_SHA256";
            case 4:
                return "SHA256";
            default:
                return "UNKNOWN_ALGO_ID(" + algorithmId + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.PackageInfo> getCurrentInstalledApexs() {
        java.util.List<android.content.pm.PackageInfo> results = new java.util.ArrayList<>();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm == null) {
            android.util.Slog.e(TAG, "Error obtaining an instance of PackageManager.");
            return results;
        }
        java.util.List<android.content.pm.PackageInfo> allPackages = pm.getInstalledPackages(android.content.pm.PackageManager.PackageInfoFlags.of(1207959552L));
        if (allPackages == null) {
            android.util.Slog.e(TAG, "Error obtaining installed packages (including APEX)");
            return results;
        }
        return (java.util.List) allPackages.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.BinaryTransparencyService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.content.pm.PackageInfo) obj).isApex;
            }
        }).collect(java.util.stream.Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.InstallSourceInfo getInstallSourceInfo(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm == null) {
            android.util.Slog.e(TAG, "Error obtaining an instance of PackageManager.");
            return null;
        }
        try {
            return pm.getInstallSourceInfo(packageName);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getOriginalApexPreinstalledLocation(java.lang.String packageName) {
        try {
            java.lang.String moduleName = apexPackageNameToModuleName(packageName);
            android.apex.IApexService apexService = android.apex.IApexService.Stub.asInterface(android.os.Binder.allowBlocking(android.os.ServiceManager.waitForService("apexservice")));
            for (android.apex.ApexInfo info : apexService.getAllPackages()) {
                if (moduleName.equals(info.moduleName)) {
                    return info.preinstalledModulePath;
                }
            }
            return APEX_PRELOAD_LOCATION_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to get package list from apexservice", e);
            return APEX_PRELOAD_LOCATION_ERROR;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String apexPackageNameToModuleName(java.lang.String packageName) {
        return com.android.server.pm.ApexManager.getInstance().getApexModuleNameForPackageName(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.PackageInfo> getNewlyInstalledMbas() {
        java.util.List<android.content.pm.PackageInfo> result = new java.util.ArrayList<>();
        android.content.pm.IBackgroundInstallControlService iBics = android.content.pm.IBackgroundInstallControlService.Stub.asInterface(android.os.ServiceManager.getService("background_install_control"));
        if (iBics == null) {
            android.util.Slog.e(TAG, "Failed to obtain an IBinder instance of IBackgroundInstallControlService");
            return result;
        }
        try {
            android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> slice = iBics.getBackgroundInstalledPackages(134348800L, 0);
            return slice.getList();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to get a list of MBAs.", e);
            return result;
        }
    }

    private static final class Digest extends java.lang.Record {
        private final int algorithm;
        private final byte[] value;

        private Digest(int algorithm, byte[] value) {
            this.algorithm = algorithm;
            this.value = value;
        }

        public int algorithm() {
            return this.algorithm;
        }

        @Override // java.lang.Record
        public final boolean equals(java.lang.Object o) {
            return (boolean) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "equals", java.lang.invoke.MethodType.methodType(java.lang.Boolean.TYPE, com.android.server.BinaryTransparencyService.Digest.class, java.lang.Object.class), com.android.server.BinaryTransparencyService.Digest.class, "algorithm;value", "FIELD:Lcom/android/server/BinaryTransparencyService$Digest;->algorithm:I", "FIELD:Lcom/android/server/BinaryTransparencyService$Digest;->value:[B").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "hashCode", java.lang.invoke.MethodType.methodType(java.lang.Integer.TYPE, com.android.server.BinaryTransparencyService.Digest.class), com.android.server.BinaryTransparencyService.Digest.class, "algorithm;value", "FIELD:Lcom/android/server/BinaryTransparencyService$Digest;->algorithm:I", "FIELD:Lcom/android/server/BinaryTransparencyService$Digest;->value:[B").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final java.lang.String toString() {
            return (java.lang.String) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "toString", java.lang.invoke.MethodType.methodType(java.lang.String.class, com.android.server.BinaryTransparencyService.Digest.class), com.android.server.BinaryTransparencyService.Digest.class, "algorithm;value", "FIELD:Lcom/android/server/BinaryTransparencyService$Digest;->algorithm:I", "FIELD:Lcom/android/server/BinaryTransparencyService$Digest;->value:[B").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        public byte[] value() {
            return this.value;
        }
    }
}
