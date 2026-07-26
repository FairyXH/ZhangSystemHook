package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerServiceUtils {
    private static final boolean DEFAULT_PACKAGE_PARSER_CACHE_ENABLED = true;
    private static final boolean FORCE_PACKAGE_PARSED_CACHE_ENABLED = false;
    private static final int FSVERITY_DISABLED = 0;
    private static final int FSVERITY_ENABLED = 2;
    private static final long MAX_CRITICAL_INFO_DUMP_SIZE = 3000000;
    public static final int SHARED_USER_ID_JOIN_TYPE_INSTALL = 0;
    public static final int SHARED_USER_ID_JOIN_TYPE_SYSTEM = 2;
    public static final int SHARED_USER_ID_JOIN_TYPE_UPDATE = 1;
    private static final boolean DEBUG = android.os.Build.IS_DEBUGGABLE;
    public static final java.util.function.Predicate<com.android.server.pm.pkg.PackageStateInternal> REMOVE_IF_APEX_PKG = new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerServiceUtils$$ExternalSyntheticLambda0
        @Override // java.util.function.Predicate
        public final boolean test(java.lang.Object obj) {
            return ((com.android.server.pm.pkg.PackageStateInternal) obj).getPkg().isApex();
        }
    };
    public static final java.util.function.Predicate<com.android.server.pm.pkg.PackageStateInternal> REMOVE_IF_NULL_PKG = new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerServiceUtils$$ExternalSyntheticLambda1
        @Override // java.util.function.Predicate
        public final boolean test(java.lang.Object obj) {
            return com.android.server.pm.PackageManagerServiceUtils.lambda$static$1((com.android.server.pm.pkg.PackageStateInternal) obj);
        }
    };

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SharedUserIdJoinType {
    }

    static /* synthetic */ boolean lambda$static$1(com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return pkgSetting.getPkg() == null;
    }

    public static com.android.server.pm.PackageManagerLocal getPackageManagerLocal() {
        try {
            return (com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.pm.PackageManagerLocal.class);
        } catch (com.android.server.LocalManagerRegistry.ManagerNotFoundException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public static boolean isUnusedSinceTimeInMillis(long firstInstallTime, long currentTimeInMillis, long thresholdTimeinMillis, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo, long latestPackageUseTimeInMillis, long latestForegroundPackageUseTimeInMillis) {
        if (currentTimeInMillis - firstInstallTime < thresholdTimeinMillis) {
            return false;
        }
        boolean isActiveInForeground = currentTimeInMillis - latestForegroundPackageUseTimeInMillis < thresholdTimeinMillis;
        if (isActiveInForeground) {
            return false;
        }
        boolean isActiveInBackgroundAndUsedByOtherPackages = currentTimeInMillis - latestPackageUseTimeInMillis < thresholdTimeinMillis && packageUseInfo.isAnyCodePathUsedByOtherApps();
        return !isActiveInBackgroundAndUsedByOtherPackages;
    }

    public static java.lang.String realpath(java.io.File path) throws java.io.IOException {
        try {
            return android.system.Os.realpath(path.getAbsolutePath());
        } catch (android.system.ErrnoException ee) {
            throw ee.rethrowAsIOException();
        }
    }

    public static boolean checkISA(java.lang.String isa) {
        if (android.os.Build.OPLUS_64BIT_ONLY_CHIP) {
            for (java.lang.String abi : android.os.Build.MTK_HBT_SUPPORTED_ABIS) {
                if (dalvik.system.VMRuntime.getInstructionSet(abi).equals(isa)) {
                    return true;
                }
            }
        } else {
            for (java.lang.String abi2 : android.os.Build.SUPPORTED_ABIS) {
                if (dalvik.system.VMRuntime.getInstructionSet(abi2).equals(isa)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long getLastModifiedTime(com.android.server.pm.pkg.AndroidPackage pkg) {
        java.io.File srcFile = new java.io.File(pkg.getPath());
        if (!srcFile.isDirectory()) {
            return srcFile.lastModified();
        }
        java.io.File baseFile = new java.io.File(pkg.getBaseApkPath());
        long maxModifiedTime = baseFile.lastModified();
        for (int i = pkg.getSplitCodePaths().length - 1; i >= 0; i--) {
            java.io.File splitFile = new java.io.File(pkg.getSplitCodePaths()[i]);
            maxModifiedTime = java.lang.Math.max(maxModifiedTime, splitFile.lastModified());
        }
        return maxModifiedTime;
    }

    private static java.io.File getSettingsProblemFile() {
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        java.io.File systemDir = new java.io.File(dataDir, "system");
        java.io.File fname = new java.io.File(systemDir, "uiderrors.txt");
        return fname;
    }

    public static void dumpCriticalInfo(android.util.proto.ProtoOutputStream proto) {
        java.io.File file = getSettingsProblemFile();
        long skipSize = file.length() - MAX_CRITICAL_INFO_DUMP_SIZE;
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(file));
            if (skipSize > 0) {
                try {
                    in.skip(skipSize);
                } finally {
                }
            }
            while (true) {
                java.lang.String line = in.readLine();
                if (line != null) {
                    if (!line.contains("ignored: updated version")) {
                        proto.write(2237677961223L, line);
                    }
                } else {
                    in.close();
                    return;
                }
            }
        } catch (java.io.IOException e) {
        }
    }

    public static void dumpCriticalInfo(java.io.PrintWriter pw, java.lang.String msg) {
        java.io.File file = getSettingsProblemFile();
        long skipSize = file.length() - MAX_CRITICAL_INFO_DUMP_SIZE;
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(file));
            if (skipSize > 0) {
                try {
                    in.skip(skipSize);
                } finally {
                }
            }
            while (true) {
                java.lang.String line = in.readLine();
                if (line != null) {
                    if (!line.contains("ignored: updated version")) {
                        if (msg != null) {
                            pw.print(msg);
                        }
                        pw.println(line);
                    }
                } else {
                    in.close();
                    return;
                }
            }
        } catch (java.io.IOException e) {
        }
    }

    public static void logCriticalInfo(int priority, java.lang.String msg) {
        android.util.Slog.println(priority, "PackageManager", msg);
        com.android.server.EventLogTags.writePmCriticalInfo(msg);
        try {
            java.io.File fname = getSettingsProblemFile();
            java.io.FileOutputStream out = new java.io.FileOutputStream(fname, true);
            com.android.internal.util.FastPrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(out);
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat();
            java.lang.String dateString = formatter.format(new java.util.Date(java.lang.System.currentTimeMillis()));
            fastPrintWriter.println(dateString + ": " + msg);
            fastPrintWriter.close();
            android.os.FileUtils.setPermissions(fname.toString(), 508, -1, -1);
        } catch (java.io.IOException e) {
        }
    }

    public static void enforceShellRestriction(com.android.server.pm.UserManagerInternal userManager, java.lang.String restriction, int callingUid, int userHandle) {
        if (callingUid == 2000) {
            if (userHandle >= 0 && userManager.hasUserRestriction(restriction, userHandle)) {
                throw new java.lang.SecurityException("Shell does not have permission to access user " + userHandle);
            }
            if (userHandle < 0) {
                android.util.Slog.e("PackageManager", "Unable to check shell permission for user " + userHandle + "\n\t" + android.os.Debug.getCallers(3));
            }
        }
    }

    public static void enforceSystemOrPhoneCaller(java.lang.String methodName, int callingUid) {
        if (callingUid != 1001 && callingUid != 1000) {
            throw new java.lang.SecurityException("Cannot call " + methodName + " from UID " + callingUid);
        }
    }

    public static java.lang.String deriveAbiOverride(java.lang.String abiOverride) {
        if ("-".equals(abiOverride)) {
            return null;
        }
        return abiOverride;
    }

    public static int compareSignatures(android.content.pm.SigningDetails sd1, android.content.pm.SigningDetails sd2) {
        return compareSignatureArrays(sd1.getSignatures(), sd2.getSignatures());
    }

    static int compareSignatureArrays(android.content.pm.Signature[] s1, android.content.pm.Signature[] s2) {
        if (s1 == null) {
            if (s2 == null) {
                return 1;
            }
            return -1;
        }
        if (s2 == null) {
            return -2;
        }
        if (s1.length != s2.length) {
            return -3;
        }
        if (s1.length == 1) {
            return s1[0].equals(s2[0]) ? 0 : -3;
        }
        android.util.ArraySet<android.content.pm.Signature> set1 = new android.util.ArraySet<>();
        for (android.content.pm.Signature sig : s1) {
            set1.add(sig);
        }
        android.util.ArraySet<android.content.pm.Signature> set2 = new android.util.ArraySet<>();
        for (android.content.pm.Signature sig2 : s2) {
            set2.add(sig2);
        }
        return set1.equals(set2) ? 0 : -3;
    }

    public static boolean comparePackageSignatures(com.android.server.pm.PackageSetting pkgSetting, android.content.pm.SigningDetails otherSigningDetails) {
        android.content.pm.SigningDetails signingDetails = pkgSetting.getSigningDetails();
        return signingDetails == android.content.pm.SigningDetails.UNKNOWN || compareSignatures(signingDetails, otherSigningDetails) == 0;
    }

    private static boolean matchSignaturesCompat(java.lang.String packageName, com.android.server.pm.PackageSignatures packageSignatures, android.content.pm.SigningDetails parsedSignatures) {
        android.util.ArraySet<android.content.pm.Signature> existingSet = new android.util.ArraySet<>();
        for (android.content.pm.Signature signature : packageSignatures.mSigningDetails.getSignatures()) {
            existingSet.add(signature);
        }
        android.util.ArraySet<android.content.pm.Signature> scannedCompatSet = new android.util.ArraySet<>();
        for (android.content.pm.Signature sig : parsedSignatures.getSignatures()) {
            try {
                android.content.pm.Signature[] chainSignatures = sig.getChainSignatures();
                for (android.content.pm.Signature chainSig : chainSignatures) {
                    scannedCompatSet.add(chainSig);
                }
            } catch (java.security.cert.CertificateEncodingException e) {
                scannedCompatSet.add(sig);
            }
        }
        if (scannedCompatSet.equals(existingSet)) {
            packageSignatures.mSigningDetails = parsedSignatures;
            return true;
        }
        if (parsedSignatures.hasPastSigningCertificates()) {
            logCriticalInfo(4, "Existing package " + packageName + " has flattened signing certificate chain. Unable to install newer version with rotated signing certificate.");
        }
        return false;
    }

    private static boolean matchSignaturesRecover(java.lang.String packageName, android.content.pm.SigningDetails existingSignatures, android.content.pm.SigningDetails parsedSignatures, int flags) {
        java.lang.String msg = null;
        try {
            if (parsedSignatures.checkCapabilityRecover(existingSignatures, flags)) {
                logCriticalInfo(4, "Recovered effectively matching certificates for " + packageName);
                return true;
            }
        } catch (java.security.cert.CertificateException e) {
            msg = e.getMessage();
        }
        logCriticalInfo(4, "Failed to recover certificates for " + packageName + ": " + msg);
        return false;
    }

    private static boolean matchSignatureInSystem(java.lang.String packageName, android.content.pm.SigningDetails signingDetails, com.android.server.pm.PackageSetting disabledPkgSetting) {
        if (signingDetails.checkCapability(disabledPkgSetting.getSigningDetails(), 1) || disabledPkgSetting.getSigningDetails().checkCapability(signingDetails, 8)) {
            return true;
        }
        logCriticalInfo(6, "Updated system app mismatches cert on /system: " + packageName);
        return false;
    }

    static boolean isApkVerityEnabled() {
        if (android.security.Flags.deprecateFsvSig()) {
            return false;
        }
        return android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT >= 30 || android.os.SystemProperties.getInt("ro.apk_verity.mode", 0) == 2;
    }

    public static boolean verifySignatures(com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.SharedUserSetting sharedUserSetting, com.android.server.pm.PackageSetting disabledPkgSetting, android.content.pm.SigningDetails parsedSignatures, boolean compareCompat, boolean compareRecover, boolean isRollback) throws com.android.server.pm.PackageManagerException {
        java.lang.String packageName = pkgSetting.getPackageName();
        boolean compatMatch = false;
        if (pkgSetting.getSigningDetails().getSignatures() != null) {
            boolean match = parsedSignatures.checkCapability(pkgSetting.getSigningDetails(), 1) || pkgSetting.getSigningDetails().checkCapability(parsedSignatures, 8);
            if (android.security.Flags.extendVbChainToUpdatedApk() && match && disabledPkgSetting != null && disabledPkgSetting.getSigningDetails() != android.content.pm.SigningDetails.UNKNOWN) {
                match = matchSignatureInSystem(packageName, parsedSignatures, disabledPkgSetting);
            }
            if (!match && compareCompat) {
                match = matchSignaturesCompat(packageName, pkgSetting.getSignatures(), parsedSignatures);
                compatMatch = match;
            }
            if (!match && compareRecover) {
                match = matchSignaturesRecover(packageName, pkgSetting.getSigningDetails(), parsedSignatures, 1) || matchSignaturesRecover(packageName, parsedSignatures, pkgSetting.getSigningDetails(), 8);
            }
            if (!match && isRollback) {
                match = pkgSetting.getSigningDetails().hasAncestorOrSelf(parsedSignatures);
            }
            if (!match) {
                throw new com.android.server.pm.PackageManagerException(-7, "Existing package " + packageName + " signatures do not match newer version; ignoring!");
            }
        }
        if (sharedUserSetting != null && sharedUserSetting.getSigningDetails() != android.content.pm.SigningDetails.UNKNOWN) {
            boolean match2 = canJoinSharedUserId(packageName, parsedSignatures, sharedUserSetting, pkgSetting.getSigningDetails().getSignatures() != null ? 1 : 0);
            if (!match2 && compareCompat) {
                match2 = matchSignaturesCompat(packageName, sharedUserSetting.signatures, parsedSignatures);
            }
            if (!match2 && compareRecover) {
                match2 = matchSignaturesRecover(packageName, sharedUserSetting.signatures.mSigningDetails, parsedSignatures, 2) || matchSignaturesRecover(packageName, parsedSignatures, sharedUserSetting.signatures.mSigningDetails, 2);
                compatMatch |= match2;
            }
            if (!match2) {
                throw new com.android.server.pm.PackageManagerException(-8, "Package " + packageName + " has no signatures that match those in shared user " + sharedUserSetting.name + "; ignoring!");
            }
            if (!parsedSignatures.hasCommonAncestor(sharedUserSetting.signatures.mSigningDetails)) {
                throw new com.android.server.pm.PackageManagerException(-8, "Package " + packageName + " has a signing lineage that diverges from the lineage of the sharedUserId");
            }
        }
        return compatMatch;
    }

    public static boolean canJoinSharedUserId(java.lang.String packageName, android.content.pm.SigningDetails packageSigningDetails, com.android.server.pm.SharedUserSetting sharedUserSetting, int joinType) {
        android.content.pm.SigningDetails sharedUserSigningDetails = sharedUserSetting.getSigningDetails();
        boolean capabilityGranted = packageSigningDetails.checkCapability(sharedUserSigningDetails, 2) || sharedUserSigningDetails.checkCapability(packageSigningDetails, 2);
        if (capabilityGranted && joinType != 0) {
            return true;
        }
        if (!capabilityGranted && sharedUserSigningDetails.hasAncestor(packageSigningDetails)) {
            return joinType == 2;
        }
        if (!capabilityGranted && packageSigningDetails.hasAncestor(sharedUserSigningDetails)) {
            return joinType != 0;
        }
        if (!capabilityGranted) {
            return false;
        }
        android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = sharedUserSetting.getPackageStates();
        if (packageSigningDetails.hasPastSigningCertificates()) {
            for (com.android.server.pm.pkg.PackageStateInternal shUidPkgSetting : packageStates) {
                android.content.pm.SigningDetails shUidSigningDetails = shUidPkgSetting.getSigningDetails();
                if (packageSigningDetails.hasAncestor(shUidSigningDetails) && !packageSigningDetails.checkCapability(shUidSigningDetails, 2)) {
                    android.util.Slog.d("PackageManager", "Package " + packageName + " revoked the sharedUserId capability from the signing key used to sign " + shUidPkgSetting.getPackageName());
                    return false;
                }
            }
        }
        return true;
    }

    public static int extractNativeBinaries(java.io.File dstCodePath, java.lang.String packageName) {
        java.io.File libraryRoot = new java.io.File(dstCodePath, "lib");
        com.android.internal.content.NativeLibraryHelper.Handle handle = null;
        try {
            try {
                handle = com.android.internal.content.NativeLibraryHelper.Handle.create(dstCodePath);
                return com.android.internal.content.NativeLibraryHelper.copyNativeBinariesWithOverride(handle, libraryRoot, (java.lang.String) null, false);
            } catch (java.io.IOException e) {
                logCriticalInfo(6, "Failed to extract native libraries; pkg: " + packageName);
                libcore.io.IoUtils.closeQuietly(handle);
                return android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT;
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(handle);
        }
    }

    public static void removeNativeBinariesLI(com.android.server.pm.PackageSetting ps) {
        if (ps != null) {
            com.android.internal.content.NativeLibraryHelper.removeNativeBinariesLI(ps.getLegacyNativeLibraryPath());
        }
    }

    public static void waitForNativeBinariesExtractionForIncremental(android.util.ArraySet<android.os.incremental.IncrementalStorage> incrementalStorages) {
        if (!incrementalStorages.isEmpty()) {
            try {
                com.android.server.Watchdog.getInstance().pauseWatchingCurrentThread("native_lib_extract");
                for (int i = 0; i < incrementalStorages.size(); i++) {
                    android.os.incremental.IncrementalStorage storage = (android.os.incremental.IncrementalStorage) incrementalStorages.valueAtUnchecked(i);
                    storage.waitForNativeBinariesExtraction();
                }
            } finally {
                com.android.server.Watchdog.getInstance().resumeWatchingCurrentThread("native_lib_extract");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0035, code lost:
    
        logCriticalInfo(6, "Failed to decompress; pkg: " + r14 + ", file: " + r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int decompressFiles(java.lang.String r12, java.io.File r13, java.lang.String r14) {
        /*
            java.lang.String r0 = "Failed to decompress; pkg: "
            java.io.File[] r1 = getCompressedFiles(r12)
            r2 = 1
            r3 = 493(0x1ed, float:6.91E-43)
            r4 = 6
            makeDirRecursive(r13, r3)     // Catch: android.system.ErrnoException -> L58
            int r3 = r1.length     // Catch: android.system.ErrnoException -> L58
            r5 = 0
            r6 = r5
        L10:
            if (r6 >= r3) goto L57
            r7 = r1[r6]     // Catch: android.system.ErrnoException -> L58
            java.lang.String r8 = r7.getName()     // Catch: android.system.ErrnoException -> L58
            int r9 = r8.length()     // Catch: android.system.ErrnoException -> L58
            java.lang.String r10 = ".gz"
            int r10 = r10.length()     // Catch: android.system.ErrnoException -> L58
            int r9 = r9 - r10
            java.lang.String r9 = r8.substring(r5, r9)     // Catch: android.system.ErrnoException -> L58
            java.io.File r10 = new java.io.File     // Catch: android.system.ErrnoException -> L58
            r10.<init>(r13, r9)     // Catch: android.system.ErrnoException -> L58
            int r11 = decompressFile(r7, r10)     // Catch: android.system.ErrnoException -> L58
            r2 = r11
            r11 = 1
            if (r2 == r11) goto L54
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: android.system.ErrnoException -> L58
            r3.<init>()     // Catch: android.system.ErrnoException -> L58
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch: android.system.ErrnoException -> L58
            java.lang.StringBuilder r3 = r3.append(r14)     // Catch: android.system.ErrnoException -> L58
            java.lang.String r5 = ", file: "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch: android.system.ErrnoException -> L58
            java.lang.StringBuilder r3 = r3.append(r9)     // Catch: android.system.ErrnoException -> L58
            java.lang.String r3 = r3.toString()     // Catch: android.system.ErrnoException -> L58
            logCriticalInfo(r4, r3)     // Catch: android.system.ErrnoException -> L58
            goto L57
        L54:
            int r6 = r6 + 1
            goto L10
        L57:
            goto L79
        L58:
            r3 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.StringBuilder r0 = r5.append(r0)
            java.lang.StringBuilder r0 = r0.append(r14)
            java.lang.String r5 = ", err: "
            java.lang.StringBuilder r0 = r0.append(r5)
            int r5 = r3.errno
            java.lang.StringBuilder r0 = r0.append(r5)
            java.lang.String r0 = r0.toString()
            logCriticalInfo(r4, r0)
        L79:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerServiceUtils.decompressFiles(java.lang.String, java.io.File, java.lang.String):int");
    }

    public static int decompressFile(java.io.File srcFile, java.io.File dstFile) throws android.system.ErrnoException {
        if (com.android.server.pm.PackageManagerService.DEBUG_COMPRESSION) {
            android.util.Slog.i("PackageManager", "Decompress file; src: " + srcFile.getAbsolutePath() + ", dst: " + dstFile.getAbsolutePath());
        }
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(dstFile);
        java.io.FileOutputStream outputStream = null;
        try {
            java.io.InputStream fileIn = new java.util.zip.GZIPInputStream(new java.io.FileInputStream(srcFile));
            try {
                outputStream = atomicFile.startWrite();
                android.os.FileUtils.copy(fileIn, outputStream);
                outputStream.flush();
                android.system.Os.fchmod(outputStream.getFD(), com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
                atomicFile.finishWrite(outputStream);
                fileIn.close();
                return 1;
            } finally {
            }
        } catch (java.io.IOException e) {
            logCriticalInfo(6, "Failed to decompress file; src: " + srcFile.getAbsolutePath() + ", dst: " + dstFile.getAbsolutePath());
            atomicFile.failWrite(outputStream);
            return android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT;
        }
    }

    public static java.io.File[] getCompressedFiles(java.lang.String codePath) {
        java.io.File stubCodePath = new java.io.File(codePath);
        java.lang.String stubName = stubCodePath.getName();
        int idx = stubName.lastIndexOf(com.android.server.pm.PackageManagerService.STUB_SUFFIX);
        if (idx < 0 || stubName.length() != com.android.server.pm.PackageManagerService.STUB_SUFFIX.length() + idx) {
            return null;
        }
        java.io.File stubParentDir = stubCodePath.getParentFile();
        if (stubParentDir == null) {
            android.util.Slog.e("PackageManager", "Unable to determine stub parent dir for codePath: " + codePath);
            return null;
        }
        java.io.File compressedPath = new java.io.File(stubParentDir, stubName.substring(0, idx));
        java.io.File[] files = compressedPath.listFiles(new java.io.FilenameFilter() { // from class: com.android.server.pm.PackageManagerServiceUtils.1
            @Override // java.io.FilenameFilter
            public boolean accept(java.io.File dir, java.lang.String name) {
                return name.toLowerCase().endsWith(com.android.server.pm.PackageManagerService.COMPRESSED_EXTENSION);
            }
        });
        if (com.android.server.pm.PackageManagerService.DEBUG_COMPRESSION && files != null && files.length > 0) {
            android.util.Slog.i("PackageManager", "getCompressedFiles[" + codePath + "]: " + java.util.Arrays.toString(files));
        }
        return files;
    }

    public static boolean compressedFileExists(java.lang.String codePath) {
        java.io.File[] compressedFiles = getCompressedFiles(codePath);
        return compressedFiles != null && compressedFiles.length > 0;
    }

    public static android.content.pm.PackageInfoLite getMinimalPackageInfo(android.content.Context context, android.content.pm.parsing.PackageLite pkg, java.lang.String packagePath, int flags, java.lang.String abiOverride) {
        long sizeBytes;
        android.content.pm.PackageInfoLite ret = new android.content.pm.PackageInfoLite();
        if (packagePath == null || pkg == null) {
            android.util.Slog.i("PackageManager", "Invalid package file " + packagePath);
            ret.recommendedInstallLocation = -2;
            return ret;
        }
        java.io.File packageFile = new java.io.File(packagePath);
        if (!com.android.server.pm.PackageInstallerSession.isArchivedInstallation(flags)) {
            try {
                sizeBytes = com.android.internal.content.InstallLocationUtils.calculateInstalledSize(pkg, abiOverride);
            } catch (java.io.IOException e) {
                if (!packageFile.exists()) {
                    ret.recommendedInstallLocation = -6;
                } else {
                    ret.recommendedInstallLocation = -2;
                }
                return ret;
            }
        } else {
            sizeBytes = 0;
        }
        android.content.pm.PackageInstaller.SessionParams sessionParams = new android.content.pm.PackageInstaller.SessionParams(-1);
        sessionParams.appPackageName = pkg.getPackageName();
        sessionParams.installLocation = pkg.getInstallLocation();
        sessionParams.sizeBytes = sizeBytes;
        sessionParams.installFlags = flags;
        try {
            int recommendedInstallLocation = com.android.internal.content.InstallLocationUtils.resolveInstallLocation(context, sessionParams);
            ret.packageName = pkg.getPackageName();
            ret.splitNames = pkg.getSplitNames();
            ret.versionCode = pkg.getVersionCode();
            ret.versionCodeMajor = pkg.getVersionCodeMajor();
            ret.baseRevisionCode = pkg.getBaseRevisionCode();
            ret.splitRevisionCodes = pkg.getSplitRevisionCodes();
            ret.installLocation = pkg.getInstallLocation();
            ret.verifiers = pkg.getVerifiers();
            ret.recommendedInstallLocation = recommendedInstallLocation;
            ret.multiArch = pkg.isMultiArch();
            ret.debuggable = pkg.isDebuggable();
            ret.isSdkLibrary = pkg.isIsSdkLibrary();
            return ret;
        } catch (java.io.IOException e2) {
            throw new java.lang.IllegalStateException(e2);
        }
    }

    public static long calculateInstalledSize(java.lang.String packagePath, java.lang.String abiOverride) {
        java.io.File packageFile = new java.io.File(packagePath);
        try {
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> result = android.content.pm.parsing.ApkLiteParseUtils.parsePackageLite(input.reset(), packageFile, 0);
            if (result.isError()) {
                throw new com.android.server.pm.PackageManagerException(result.getErrorCode(), result.getErrorMessage(), result.getException());
            }
            return com.android.internal.content.InstallLocationUtils.calculateInstalledSize((android.content.pm.parsing.PackageLite) result.getResult(), abiOverride);
        } catch (com.android.server.pm.PackageManagerException | java.io.IOException e) {
            android.util.Slog.w("PackageManager", "Failed to calculate installed size: " + e);
            return -1L;
        }
    }

    public static boolean isDowngradePermitted(int installFlags, boolean isAppDebuggable) {
        boolean downgradeRequested = (installFlags & 128) != 0;
        if (!downgradeRequested) {
            return false;
        }
        boolean isDebuggable = android.os.Build.IS_DEBUGGABLE || isAppDebuggable;
        return isDebuggable || (1048576 & installFlags) != 0;
    }

    public static int copyPackage(java.lang.String packagePath, java.io.File targetDir) {
        if (packagePath == null) {
            return -3;
        }
        try {
            java.io.File packageFile = new java.io.File(packagePath);
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> result = android.content.pm.parsing.ApkLiteParseUtils.parsePackageLite(input.reset(), packageFile, 0);
            if (result.isError()) {
                android.util.Slog.w("PackageManager", "Failed to parse package at " + packagePath);
                return result.getErrorCode();
            }
            android.content.pm.parsing.PackageLite pkg = (android.content.pm.parsing.PackageLite) result.getResult();
            copyFile(pkg.getBaseApkPath(), targetDir, "base.apk");
            if (!com.android.internal.util.ArrayUtils.isEmpty(pkg.getSplitNames())) {
                for (int i = 0; i < pkg.getSplitNames().length; i++) {
                    copyFile(pkg.getSplitApkPaths()[i], targetDir, "split_" + pkg.getSplitNames()[i] + ".apk");
                }
                return 1;
            }
            return 1;
        } catch (android.system.ErrnoException | java.io.IOException e) {
            android.util.Slog.w("PackageManager", "Failed to copy package at " + packagePath + ": " + e);
            return -4;
        }
    }

    private static void copyFile(java.lang.String sourcePath, java.io.File targetDir, java.lang.String targetName) throws java.io.IOException, android.system.ErrnoException {
        if (!android.os.FileUtils.isValidExtFilename(targetName)) {
            throw new java.lang.IllegalArgumentException("Invalid filename: " + targetName);
        }
        android.util.Slog.d("PackageManager", "Copying " + sourcePath + " to " + targetName);
        java.io.File targetFile = new java.io.File(targetDir, targetName);
        java.io.FileDescriptor targetFd = android.system.Os.open(targetFile.getAbsolutePath(), android.system.OsConstants.O_RDWR | android.system.OsConstants.O_CREAT, com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
        android.system.Os.chmod(targetFile.getAbsolutePath(), com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
        java.io.FileInputStream source = null;
        try {
            source = new java.io.FileInputStream(sourcePath);
            android.os.FileUtils.copy(source.getFD(), targetFd);
        } finally {
            libcore.io.IoUtils.closeQuietly(source);
        }
    }

    public static void makeDirRecursive(java.io.File targetDir, int mode) throws android.system.ErrnoException {
        java.nio.file.Path targetDirPath = targetDir.toPath();
        int directoriesCount = targetDirPath.getNameCount();
        for (int i = 1; i <= directoriesCount; i++) {
            java.io.File currentDir = targetDirPath.subpath(0, i).toFile();
            if (!currentDir.exists()) {
                android.system.Os.mkdir(currentDir.getAbsolutePath(), mode);
                android.system.Os.chmod(currentDir.getAbsolutePath(), mode);
            }
        }
    }

    public static java.lang.String buildVerificationRootHashString(java.lang.String baseFilename, java.lang.String[] splitFilenameArray) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String baseFilePath = baseFilename.substring(baseFilename.lastIndexOf(java.io.File.separator) + 1);
        sb.append(baseFilePath).append(":");
        byte[] baseRootHash = getRootHash(baseFilename);
        if (baseRootHash == null) {
            sb.append("0");
        } else {
            sb.append(com.android.internal.util.HexDump.toHexString(baseRootHash));
        }
        if (splitFilenameArray == null || splitFilenameArray.length == 0) {
            return sb.toString();
        }
        for (int i = splitFilenameArray.length - 1; i >= 0; i--) {
            java.lang.String splitFilename = splitFilenameArray[i];
            java.lang.String splitFilePath = splitFilename.substring(splitFilename.lastIndexOf(java.io.File.separator) + 1);
            byte[] splitRootHash = getRootHash(splitFilename);
            sb.append(";").append(splitFilePath).append(":");
            if (splitRootHash == null) {
                sb.append("0");
            } else {
                sb.append(com.android.internal.util.HexDump.toHexString(splitRootHash));
            }
        }
        return sb.toString();
    }

    private static byte[] getRootHash(java.lang.String filename) {
        try {
            byte[] baseFileSignature = android.os.incremental.IncrementalManager.unsafeGetFileSignature(filename);
            if (baseFileSignature == null) {
                throw new java.io.IOException("File signature not present");
            }
            android.os.incremental.V4Signature signature = android.os.incremental.V4Signature.readFrom(baseFileSignature);
            if (signature.hashingInfo == null) {
                throw new java.io.IOException("Hashing info not present");
            }
            android.os.incremental.V4Signature.HashingInfo hashInfo = android.os.incremental.V4Signature.HashingInfo.fromByteArray(signature.hashingInfo);
            if (com.android.internal.util.ArrayUtils.isEmpty(hashInfo.rawRootHash)) {
                throw new java.io.IOException("Root has not present");
            }
            return com.android.server.pm.ApkChecksums.verityHashForFile(new java.io.File(filename), hashInfo.rawRootHash);
        } catch (java.io.IOException e) {
            android.util.Slog.i("PackageManager", "Could not obtain verity root hash", e);
            return null;
        }
    }

    public static boolean isSystemApp(com.android.server.pm.pkg.PackageStateInternal ps) {
        return (ps.getFlags() & 1) != 0;
    }

    public static boolean isUpdatedSystemApp(com.android.server.pm.pkg.PackageStateInternal ps) {
        return (ps.getFlags() & 128) != 0;
    }

    public static boolean hasAnyDomainApproval(com.android.server.pm.verify.domain.DomainVerificationManagerInternal manager, com.android.server.pm.pkg.PackageStateInternal pkgSetting, android.content.Intent intent, long resolveInfoFlags, int userId) {
        return manager.approvalLevelForDomain(pkgSetting, intent, resolveInfoFlags, userId) > 0;
    }

    public static android.content.Intent updateIntentForResolve(android.content.Intent intent) {
        if (intent.getSelector() != null) {
            intent = intent.getSelector();
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            intent.addFlags(8);
        }
        return intent;
    }

    public static java.lang.String arrayToString(int[] array) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder(128);
        stringBuilder.append('[');
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(array[i]);
            }
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static java.io.File getNextCodePath(java.io.File targetDir, java.lang.String packageName) {
        java.io.File firstLevelDir;
        java.security.SecureRandom random = new java.security.SecureRandom();
        byte[] bytes = new byte[16];
        do {
            random.nextBytes(bytes);
            java.lang.String firstLevelDirName = "~~" + android.util.Base64.encodeToString(bytes, 10);
            firstLevelDir = new java.io.File(targetDir, firstLevelDirName);
        } while (firstLevelDir.exists());
        random.nextBytes(bytes);
        java.lang.String dirName = packageName + '-' + android.util.Base64.encodeToString(bytes, 10);
        java.io.File result = new java.io.File(firstLevelDir, dirName);
        if (DEBUG && !java.util.Objects.equals(tryParsePackageName(result.getName()), packageName)) {
            throw new java.lang.RuntimeException("codepath is off: " + result.getName() + " (" + packageName + ")");
        }
        return result;
    }

    static java.lang.String tryParsePackageName(java.lang.String codePath) throws java.lang.IllegalArgumentException {
        int packageNameEnds = codePath.indexOf(45);
        if (packageNameEnds == -1) {
            throw new java.lang.IllegalArgumentException("Not a valid package folder name");
        }
        return codePath.substring(0, packageNameEnds);
    }

    public static int getPackageExternalStorageType(android.os.storage.VolumeInfo packageVolume, boolean packageIsExternal) {
        android.os.storage.DiskInfo disk;
        if (packageVolume != null && (disk = packageVolume.getDisk()) != null) {
            if (disk.isSd()) {
                return 1;
            }
            if (disk.isUsb()) {
                return 2;
            }
            if (packageIsExternal) {
                return 3;
            }
            return 0;
        }
        return 0;
    }

    public static void enforceSystemOrRootOrShell(java.lang.String message) {
        if (!isSystemOrRootOrShell()) {
            throw new java.lang.SecurityException(message);
        }
    }

    public static boolean isSystemOrRootOrShell() {
        return isSystemOrRootOrShell(android.os.Binder.getCallingUid());
    }

    public static boolean isSystemOrRootOrShell(int uid) {
        return uid == 1000 || uid == 0 || uid == 2000;
    }

    public static boolean isSystemOrRoot() {
        int uid = android.os.Binder.getCallingUid();
        return isSystemOrRoot(uid);
    }

    public static boolean isSystemOrRoot(int uid) {
        return uid == 1000 || uid == 0;
    }

    public static boolean isAdoptedShell(int uid, android.content.Context context) {
        return uid != 1000 && context.checkCallingOrSelfPermission("com.android.permission.USE_SYSTEM_DATA_LOADERS") == 0;
    }

    public static boolean isRootOrShell(int uid) {
        return uid == 0 || uid == 2000;
    }

    public static void enforceSystemOrRoot(java.lang.String message) {
        if (!isSystemOrRoot()) {
            throw new java.lang.SecurityException(message);
        }
    }

    public static java.io.File preparePackageParserCache(boolean forEngBuild, boolean isUserDebugBuild, java.lang.String incrementalVersion) {
        if (forEngBuild) {
            return null;
        }
        if (android.os.SystemProperties.getBoolean("pm.boot.disable_package_cache", false)) {
            android.util.Slog.i("PackageManager", "Disabling package parser cache due to system property.");
            return null;
        }
        java.io.File cacheBaseDir = android.os.Environment.getPackageCacheDirectory();
        if (!android.os.FileUtils.createDir(cacheBaseDir)) {
            return null;
        }
        java.lang.String cacheName = android.content.pm.PackagePartitions.FINGERPRINT;
        for (java.io.File cacheDir : android.os.FileUtils.listFilesOrEmpty(cacheBaseDir)) {
            if (java.util.Objects.equals(cacheName, cacheDir.getName())) {
                android.util.Slog.d("PackageManager", "Keeping known cache " + cacheDir.getName());
            } else {
                android.util.Slog.d("PackageManager", "Destroying unknown cache " + cacheDir.getName());
                android.os.FileUtils.deleteContentsAndDir(cacheDir);
            }
        }
        java.io.File cacheDir2 = android.os.FileUtils.createDir(cacheBaseDir, cacheName);
        if (cacheDir2 == null) {
            android.util.Slog.wtf("PackageManager", "Cache directory cannot be created - wiping base dir " + cacheBaseDir);
            android.os.FileUtils.deleteContentsAndDir(cacheBaseDir);
            return null;
        }
        if (isUserDebugBuild && incrementalVersion.startsWith("eng.")) {
            android.util.Slog.w("PackageManager", "Wiping cache directory because the system partition changed.");
            java.io.File frameworkDir = new java.io.File(android.os.Environment.getRootDirectory(), "framework");
            if (cacheDir2.lastModified() < frameworkDir.lastModified()) {
                android.os.FileUtils.deleteContents(cacheBaseDir);
                return android.os.FileUtils.createDir(cacheBaseDir, cacheName);
            }
            return cacheDir2;
        }
        return cacheDir2;
    }

    public static void checkDowngrade(com.android.server.pm.pkg.AndroidPackage before, android.content.pm.PackageInfoLite after) throws com.android.server.pm.PackageManagerException {
        if (after.getLongVersionCode() < before.getLongVersionCode()) {
            throw new com.android.server.pm.PackageManagerException(-25, "Update version code " + after.versionCode + " is older than current " + before.getLongVersionCode());
        }
        if (after.getLongVersionCode() == before.getLongVersionCode()) {
            if (after.baseRevisionCode < before.getBaseRevisionCode()) {
                throw new com.android.server.pm.PackageManagerException(-25, "Update base revision code " + after.baseRevisionCode + " is older than current " + before.getBaseRevisionCode());
            }
            if (!com.android.internal.util.ArrayUtils.isEmpty(after.splitNames)) {
                for (int i = 0; i < after.splitNames.length; i++) {
                    java.lang.String splitName = after.splitNames[i];
                    int j = com.android.internal.util.ArrayUtils.indexOf(before.getSplitNames(), splitName);
                    if (j != -1 && after.splitRevisionCodes[i] < before.getSplitRevisionCodes()[j]) {
                        throw new com.android.server.pm.PackageManagerException(-25, "Update split " + splitName + " revision code " + after.splitRevisionCodes[i] + " is older than current " + before.getSplitRevisionCodes()[j]);
                    }
                }
            }
        }
    }

    public static boolean isInstalledByAdb(java.lang.String initiatingPackageName) {
        return initiatingPackageName == null || "com.android.shell".equals(initiatingPackageName);
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x011f A[Catch: Exception -> 0x0151, all -> 0x0179, TRY_ENTER, TRY_LEAVE, TryCatch #6 {Exception -> 0x0151, blocks: (B:47:0x011f, B:71:0x0150, B:70:0x014d), top: B:100:0x00dc }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0180  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean extractAppMetadataFromApk(com.android.server.pm.pkg.AndroidPackage r21, java.lang.String r22, boolean r23) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 388
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerServiceUtils.extractAppMetadataFromApk(com.android.server.pm.pkg.AndroidPackage, java.lang.String, boolean):boolean");
    }

    static /* synthetic */ void lambda$extractAppMetadataFromApk$2(long sizeLimit, java.util.concurrent.atomic.AtomicBoolean copyFailed, android.os.CancellationSignal signal, long progress) {
        if (progress > sizeLimit) {
            copyFailed.set(true);
            signal.cancel();
        }
    }

    public static void linkFilesToOldDirs(com.android.server.pm.Installer installer, java.lang.String packageName, java.io.File newPath, java.util.Set<java.io.File> oldPaths) {
        java.io.File[] filesInNewPath;
        if (oldPaths == null || oldPaths.isEmpty() || android.os.incremental.IncrementalManager.isIncrementalPath(newPath.getPath()) || (filesInNewPath = newPath.listFiles()) == null || filesInNewPath.length == 0) {
            return;
        }
        java.util.List<java.io.File> splitApks = new java.util.ArrayList<>();
        for (java.io.File file : filesInNewPath) {
            if (!file.isDirectory() && file.toString().endsWith(".apk")) {
                splitApks.add(file);
            }
        }
        if (splitApks.isEmpty()) {
            return;
        }
        java.io.File[] splitApkNames = (java.io.File[]) splitApks.toArray(new java.io.File[0]);
        for (java.io.File oldPath : oldPaths) {
            if (oldPath.exists()) {
                linkFilesAndSetModes(installer, packageName, newPath, oldPath, splitApkNames, com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
                linkNativeLibraries(installer, packageName, newPath, oldPath, "lib");
                linkNativeLibraries(installer, packageName, newPath, oldPath, "lib64");
            }
        }
    }

    private static void linkNativeLibraries(com.android.server.pm.Installer installer, java.lang.String packageName, java.io.File sourcePath, java.io.File targetPath, java.lang.String libDirName) {
        java.io.File[] files;
        java.io.File sourceLibDir = new java.io.File(sourcePath, libDirName);
        if (sourceLibDir.exists()) {
            java.io.File targetLibDir = new java.io.File(targetPath, libDirName);
            if (!targetLibDir.exists()) {
                try {
                    com.android.internal.content.NativeLibraryHelper.createNativeLibrarySubdir(targetLibDir);
                } catch (java.io.IOException e) {
                    android.util.Slog.w("PackageManager", "Failed to create native library dir at <" + targetLibDir + ">", e);
                    return;
                }
            }
            java.io.File[] archs = sourceLibDir.listFiles();
            if (archs == null) {
                return;
            }
            for (java.io.File arch : archs) {
                java.io.File targetArchDir = new java.io.File(targetLibDir, arch.getName());
                if (!targetArchDir.exists()) {
                    try {
                        com.android.internal.content.NativeLibraryHelper.createNativeLibrarySubdir(targetArchDir);
                        java.io.File sourceArchDir = new java.io.File(sourceLibDir, arch.getName());
                        files = sourceArchDir.listFiles();
                        if (files == null && files.length != 0) {
                            linkFilesAndSetModes(installer, packageName, sourceArchDir, targetArchDir, files, 493);
                        }
                    } catch (java.io.IOException e2) {
                        android.util.Slog.w("PackageManager", "Failed to create native library subdir at <" + targetArchDir + ">", e2);
                    }
                } else {
                    java.io.File sourceArchDir2 = new java.io.File(sourceLibDir, arch.getName());
                    files = sourceArchDir2.listFiles();
                    if (files == null) {
                    }
                }
            }
        }
    }

    private static void linkFilesAndSetModes(com.android.server.pm.Installer installer, java.lang.String packageName, java.io.File sourcePath, java.io.File targetPath, java.io.File[] files, int mode) {
        int i;
        java.io.File[] fileArr = files;
        int length = fileArr.length;
        int i2 = 0;
        while (i2 < length) {
            java.io.File file = fileArr[i2];
            java.lang.String fileName = file.getName();
            java.io.File sourceFile = new java.io.File(sourcePath, fileName);
            java.io.File targetFile = new java.io.File(targetPath, fileName);
            if (targetFile.exists()) {
                if (DEBUG) {
                    android.util.Slog.d("PackageManager", "Skipping existing linked file <" + targetFile + ">");
                    i = length;
                } else {
                    i = length;
                }
            } else {
                try {
                    try {
                        installer.linkFile(packageName, fileName, sourcePath.getAbsolutePath(), targetPath.getAbsolutePath());
                        if (DEBUG) {
                            android.util.Slog.d("PackageManager", "Linked <" + sourceFile + "> to <" + targetFile + ">");
                        }
                        try {
                            try {
                                android.system.Os.chmod(targetFile.getAbsolutePath(), mode);
                                if (android.os.SELinux.restorecon(targetFile)) {
                                    i = length;
                                } else {
                                    android.util.Slog.w("PackageManager", "Failed to restorecon for linked file <" + targetFile + ">");
                                    i = length;
                                }
                            } catch (android.system.ErrnoException e) {
                                e = e;
                                i = length;
                                android.util.Slog.w("PackageManager", "Failed to set mode for linked file <" + targetFile + ">", e);
                            }
                        } catch (android.system.ErrnoException e2) {
                            e = e2;
                        }
                    } catch (com.android.server.pm.Installer.InstallerException e3) {
                        e = e3;
                        i = length;
                        android.util.Slog.w("PackageManager", "Failed to link native library <" + sourceFile + "> to <" + targetFile + ">", e);
                    }
                } catch (com.android.server.pm.Installer.InstallerException e4) {
                    e = e4;
                }
            }
            i2++;
            fileArr = files;
            length = i;
        }
    }
}
