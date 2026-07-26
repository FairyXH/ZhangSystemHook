package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PackageAbiHelperImpl implements com.android.server.pm.PackageAbiHelper {
    private static java.lang.String[] sNativelySupported32BitAbis = null;
    private static java.lang.String[] sNativelySupported64BitAbis = null;

    PackageAbiHelperImpl() {
    }

    private static java.lang.String calculateBundledApkRoot(java.lang.String codePathString) {
        java.io.File f;
        java.io.File codePath = new java.io.File(codePathString);
        if (android.os.FileUtils.contains(android.os.Environment.getRootDirectory(), codePath)) {
            f = android.os.Environment.getRootDirectory();
        } else {
            java.io.File codeRoot = android.os.Environment.getOemDirectory();
            if (android.os.FileUtils.contains(codeRoot, codePath)) {
                f = android.os.Environment.getOemDirectory();
            } else {
                java.io.File codeRoot2 = android.os.Environment.getVendorDirectory();
                if (android.os.FileUtils.contains(codeRoot2, codePath)) {
                    f = android.os.Environment.getVendorDirectory();
                } else {
                    java.io.File codeRoot3 = android.os.Environment.getOdmDirectory();
                    if (android.os.FileUtils.contains(codeRoot3, codePath)) {
                        f = android.os.Environment.getOdmDirectory();
                    } else {
                        java.io.File codeRoot4 = android.os.Environment.getProductDirectory();
                        if (android.os.FileUtils.contains(codeRoot4, codePath)) {
                            f = android.os.Environment.getProductDirectory();
                        } else {
                            java.io.File codeRoot5 = android.os.Environment.getSystemExtDirectory();
                            if (android.os.FileUtils.contains(codeRoot5, codePath)) {
                                f = android.os.Environment.getSystemExtDirectory();
                            } else {
                                java.io.File codeRoot6 = android.os.Environment.getOdmDirectory();
                                if (android.os.FileUtils.contains(codeRoot6, codePath)) {
                                    f = android.os.Environment.getOdmDirectory();
                                } else {
                                    java.io.File codeRoot7 = android.os.Environment.getApexDirectory();
                                    if (android.os.FileUtils.contains(codeRoot7, codePath)) {
                                        java.lang.String fullPath = codePath.getAbsolutePath();
                                        java.lang.String[] parts = fullPath.split(java.io.File.separator);
                                        if (parts.length > 2) {
                                            f = new java.io.File(parts[1] + java.io.File.separator + parts[2]);
                                        } else {
                                            android.util.Slog.w("PackageManager", "Can't canonicalize code path " + codePath);
                                            f = android.os.Environment.getApexDirectory();
                                        }
                                    } else {
                                        try {
                                            java.io.File f2 = codePath.getCanonicalFile();
                                            java.io.File parent = f2.getParentFile();
                                            while (true) {
                                                java.io.File tmp = parent.getParentFile();
                                                if (tmp == null) {
                                                    break;
                                                }
                                                f2 = parent;
                                                parent = tmp;
                                            }
                                            java.io.File codeRoot8 = f2;
                                            android.util.Slog.w("PackageManager", "Unrecognized code path " + codePath + " - using " + codeRoot8);
                                            f = codeRoot8;
                                        } catch (java.io.IOException e) {
                                            android.util.Slog.w("PackageManager", "Can't canonicalize code path " + codePath);
                                            return android.os.Environment.getRootDirectory().getPath();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return f.getPath();
    }

    private static java.lang.String deriveCodePathName(java.lang.String codePath) {
        if (codePath == null) {
            return null;
        }
        java.io.File codeFile = new java.io.File(codePath);
        java.lang.String name = codeFile.getName();
        if (codeFile.isDirectory()) {
            return name;
        }
        if (name.endsWith(".apk") || name.endsWith(".tmp")) {
            int lastDot = name.lastIndexOf(46);
            return name.substring(0, lastDot);
        }
        android.util.Slog.w("PackageManager", "Odd, " + codePath + " doesn't look like an APK");
        return null;
    }

    private static void maybeThrowExceptionForMultiArchCopy(java.lang.String message, int copyRet, boolean forceMatch) throws com.android.server.pm.PackageManagerException {
        if (copyRet < 0) {
            if (copyRet != -114 && copyRet != -113) {
                throw new com.android.server.pm.PackageManagerException(copyRet, message);
            }
            if (forceMatch && copyRet == -113) {
                throw new com.android.server.pm.PackageManagerException(-131, "The multiArch app's native libs don't support all the natively supported ABIs of the device.");
            }
        }
    }

    @Override // com.android.server.pm.PackageAbiHelper
    public com.android.server.pm.PackageAbiHelper.NativeLibraryPaths deriveNativeLibraryPaths(com.android.server.pm.pkg.AndroidPackage pkg, boolean isSystemApp, boolean isUpdatedSystemApp, java.io.File appLib32InstallDir) {
        return deriveNativeLibraryPaths(new com.android.server.pm.PackageAbiHelper.Abis(com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(pkg), com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawSecondaryCpuAbi(pkg)), appLib32InstallDir, pkg.getPath(), pkg.getBaseApkPath(), isSystemApp, isUpdatedSystemApp);
    }

    private static com.android.server.pm.PackageAbiHelper.NativeLibraryPaths deriveNativeLibraryPaths(com.android.server.pm.PackageAbiHelper.Abis abis, java.io.File appLib32InstallDir, java.lang.String codePath, java.lang.String sourceDir, boolean isSystemApp, boolean isUpdatedSystemApp) {
        java.lang.String nativeLibraryRootDir;
        boolean nativeLibraryRootRequiresIsa;
        java.lang.String nativeLibraryDir;
        java.lang.String secondaryLibDir;
        java.io.File codeFile = new java.io.File(codePath);
        boolean bundledApp = isSystemApp && !isUpdatedSystemApp;
        if (android.content.pm.parsing.ApkLiteParseUtils.isApkFile(codeFile)) {
            if (bundledApp) {
                java.lang.String apkRoot = calculateBundledApkRoot(sourceDir);
                boolean is64Bit = dalvik.system.VMRuntime.is64BitInstructionSet(com.android.server.pm.InstructionSets.getPrimaryInstructionSet(abis));
                java.lang.String apkName = deriveCodePathName(codePath);
                java.lang.String libDir = is64Bit ? "lib64" : "lib";
                nativeLibraryRootDir = android.os.Environment.buildPath(new java.io.File(apkRoot), new java.lang.String[]{libDir, apkName}).getAbsolutePath();
                if (abis.secondary != null) {
                    java.lang.String secondaryLibDir2 = is64Bit ? "lib" : "lib64";
                    secondaryLibDir = android.os.Environment.buildPath(new java.io.File(apkRoot), new java.lang.String[]{secondaryLibDir2, apkName}).getAbsolutePath();
                } else {
                    secondaryLibDir = null;
                }
            } else {
                nativeLibraryRootDir = new java.io.File(appLib32InstallDir, deriveCodePathName(codePath)).getAbsolutePath();
                secondaryLibDir = null;
            }
            nativeLibraryRootRequiresIsa = false;
            nativeLibraryDir = nativeLibraryRootDir;
        } else {
            nativeLibraryRootDir = ((com.android.server.pm.IPackageAbiHelperExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageAbiHelperExt.IStaticExt.class).create()).reconcileLibraryRootDir(codePath, codeFile);
            nativeLibraryRootRequiresIsa = true;
            nativeLibraryDir = new java.io.File(nativeLibraryRootDir, com.android.server.pm.InstructionSets.getPrimaryInstructionSet(abis)).getAbsolutePath();
            if (abis.secondary != null) {
                secondaryLibDir = new java.io.File(nativeLibraryRootDir, dalvik.system.VMRuntime.getInstructionSet(abis.secondary)).getAbsolutePath();
            } else {
                secondaryLibDir = null;
            }
        }
        return new com.android.server.pm.PackageAbiHelper.NativeLibraryPaths(nativeLibraryRootDir, nativeLibraryRootRequiresIsa, nativeLibraryDir, secondaryLibDir);
    }

    @Override // com.android.server.pm.PackageAbiHelper
    public com.android.server.pm.PackageAbiHelper.Abis getBundledAppAbis(com.android.server.pm.pkg.AndroidPackage pkg) {
        java.lang.String apkName = deriveCodePathName(pkg.getPath());
        java.lang.String apkRoot = calculateBundledApkRoot(pkg.getBaseApkPath());
        com.android.server.pm.PackageAbiHelper.Abis abis = getBundledAppAbi(pkg, apkRoot, apkName);
        return abis;
    }

    private com.android.server.pm.PackageAbiHelper.Abis getBundledAppAbi(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String apkRoot, java.lang.String apkName) {
        boolean has64BitLibs;
        boolean has64BitLibs2;
        boolean has64BitLibs3;
        java.lang.String primaryCpuAbi;
        java.lang.String secondaryCpuAbi;
        java.io.File codeFile = new java.io.File(pkg.getPath());
        if (android.content.pm.parsing.ApkLiteParseUtils.isApkFile(codeFile)) {
            has64BitLibs2 = new java.io.File(apkRoot, new java.io.File("lib64", apkName).getPath()).exists();
            has64BitLibs3 = new java.io.File(apkRoot, new java.io.File("lib", apkName).getPath()).exists();
        } else {
            java.io.File rootDir = new java.io.File(codeFile, "lib");
            if (!com.android.internal.util.ArrayUtils.isEmpty(android.os.Build.SUPPORTED_64_BIT_ABIS) && !android.text.TextUtils.isEmpty(android.os.Build.SUPPORTED_64_BIT_ABIS[0])) {
                java.lang.String isa = dalvik.system.VMRuntime.getInstructionSet(android.os.Build.SUPPORTED_64_BIT_ABIS[0]);
                has64BitLibs = new java.io.File(rootDir, isa).exists();
            } else {
                has64BitLibs = false;
            }
            if (!com.android.internal.util.ArrayUtils.isEmpty(android.os.Build.SUPPORTED_32_BIT_ABIS) && !android.text.TextUtils.isEmpty(android.os.Build.SUPPORTED_32_BIT_ABIS[0])) {
                java.lang.String isa2 = dalvik.system.VMRuntime.getInstructionSet(android.os.Build.SUPPORTED_32_BIT_ABIS[0]);
                boolean has32BitLibs = new java.io.File(rootDir, isa2).exists();
                has64BitLibs2 = has64BitLibs;
                has64BitLibs3 = has32BitLibs;
            } else {
                has64BitLibs2 = has64BitLibs;
                has64BitLibs3 = false;
            }
        }
        if (has64BitLibs2 && !has64BitLibs3) {
            primaryCpuAbi = android.os.Build.SUPPORTED_64_BIT_ABIS[0];
            secondaryCpuAbi = null;
        } else if (has64BitLibs3 && !has64BitLibs2) {
            primaryCpuAbi = android.os.Build.SUPPORTED_32_BIT_ABIS[0];
            secondaryCpuAbi = null;
        } else if (has64BitLibs3 && has64BitLibs2) {
            if (!pkg.isMultiArch()) {
                android.util.Slog.e("PackageManager", "Package " + pkg + " has multiple bundled libs, but is not multiarch.");
            }
            if (dalvik.system.VMRuntime.is64BitInstructionSet(com.android.server.pm.InstructionSets.getPreferredInstructionSet())) {
                java.lang.String primaryCpuAbi2 = android.os.Build.SUPPORTED_64_BIT_ABIS[0];
                secondaryCpuAbi = android.os.Build.SUPPORTED_32_BIT_ABIS[0];
                primaryCpuAbi = primaryCpuAbi2;
            } else {
                java.lang.String primaryCpuAbi3 = android.os.Build.SUPPORTED_32_BIT_ABIS[0];
                secondaryCpuAbi = android.os.Build.SUPPORTED_64_BIT_ABIS[0];
                primaryCpuAbi = primaryCpuAbi3;
            }
        } else {
            primaryCpuAbi = null;
            secondaryCpuAbi = null;
        }
        return new com.android.server.pm.PackageAbiHelper.Abis(primaryCpuAbi, secondaryCpuAbi);
    }

    private static java.lang.String[] getNativelySupportedAbis(java.lang.String[] supportedAbis) {
        java.util.List<java.lang.String> nativelySupportedAbis = new java.util.ArrayList<>();
        for (java.lang.String currentAbi : supportedAbis) {
            java.lang.String currentIsa = dalvik.system.VMRuntime.getInstructionSet(currentAbi);
            if (android.text.TextUtils.isEmpty(android.os.SystemProperties.get("ro.dalvik.vm.isa." + currentIsa))) {
                nativelySupportedAbis.add(currentAbi);
            }
        }
        return (java.lang.String[]) nativelySupportedAbis.toArray(new java.lang.String[0]);
    }

    private static java.lang.String[] getNativelySupported32BitAbis() {
        if (sNativelySupported32BitAbis != null) {
            return sNativelySupported32BitAbis;
        }
        sNativelySupported32BitAbis = getNativelySupportedAbis(android.os.Build.SUPPORTED_32_BIT_ABIS);
        return sNativelySupported32BitAbis;
    }

    private static java.lang.String[] getNativelySupported64BitAbis() {
        if (sNativelySupported64BitAbis != null) {
            return sNativelySupported64BitAbis;
        }
        sNativelySupported64BitAbis = getNativelySupportedAbis(android.os.Build.SUPPORTED_64_BIT_ABIS);
        return sNativelySupported64BitAbis;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(34:234|8|9|(30:232|11|(0)(1:14)|(1:21)(1:23)|(1:26)(1:28)|29|(1:31)(1:32)|33|246|34|(1:36)(1:37)|38|248|(3:(2:41|42)(1:43)|44|45)(4:46|236|47|(1:60)(3:(1:52)(1:53)|54|55))|61|(2:67|68)|69|70|(1:74)(1:73)|242|75|(2:(4:78|230|79|80)(2:85|86)|87)(1:88)|89|(1:93)(1:92)|94|(2:(2:100|101)|102)|(3:104|(1:106)(1:107)|(7:109|(1:111)(1:112)|238|113|(2:115|(1:117)(1:118))(0)|219|220)(1:123))(1:124)|125|219|220)|19|(0)(0)|(0)(0)|29|(0)(0)|33|246|34|(0)(0)|38|248|(0)(0)|61|(3:63|67|68)|69|70|(0)|74|242|75|(0)(0)|89|(0)|93|94|(0)|(0)(0)|125|219|220) */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x020f, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0218, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0219, code lost:
    
        r8 = r17;
        r9 = r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0251, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0260, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0112, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x011e, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x011f, code lost:
    
        r8 = r17;
        r9 = r24;
     */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x02a8: MOVE (r2 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('useIsaSpecificSubdirs' boolean)]), block:B:151:0x02a8 */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x02b0: MOVE (r2 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('useIsaSpecificSubdirs' boolean)]), block:B:153:0x02b0 */
    /* JADX WARN: Not initialized variable reg: 24, insn: 0x02ab: MOVE (r9 I:??[OBJECT, ARRAY]) = (r24 I:??[OBJECT, ARRAY] A[D('secondaryCpuAbi' java.lang.String)]), block:B:151:0x02a8 */
    /* JADX WARN: Not initialized variable reg: 24, insn: 0x02b3: MOVE (r9 I:??[OBJECT, ARRAY]) = (r24 I:??[OBJECT, ARRAY] A[D('secondaryCpuAbi' java.lang.String)]), block:B:153:0x02b0 */
    /* JADX WARN: Removed duplicated region for block: B:104:0x01b8 A[Catch: all -> 0x02a7, IOException -> 0x02af, TryCatch #25 {IOException -> 0x02af, all -> 0x02a7, blocks: (B:87:0x0188, B:94:0x019c, B:97:0x01a3, B:100:0x01aa, B:101:0x01b1, B:102:0x01b2, B:104:0x01b8, B:106:0x01bc, B:109:0x01c5, B:107:0x01c1, B:86:0x017f, B:148:0x02a2, B:156:0x02bb, B:158:0x02bf, B:164:0x02ca, B:166:0x02d0, B:168:0x02d5, B:169:0x02da, B:171:0x02de, B:173:0x02e3, B:174:0x02e8, B:175:0x02f1), top: B:229:0x0048 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0208  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007a A[Catch: all -> 0x005f, IOException -> 0x006b, TRY_LEAVE, TryCatch #23 {IOException -> 0x006b, all -> 0x005f, blocks: (B:11:0x0052, B:21:0x007a, B:26:0x0084), top: B:232:0x0052 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007f A[Catch: all -> 0x026f, IOException -> 0x0280, TRY_ENTER, TRY_LEAVE, TryCatch #22 {IOException -> 0x0280, all -> 0x026f, blocks: (B:8:0x004a, B:29:0x008b, B:28:0x0089, B:23:0x007f), top: B:234:0x004a }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0084 A[Catch: all -> 0x005f, IOException -> 0x006b, TRY_ENTER, TRY_LEAVE, TryCatch #23 {IOException -> 0x006b, all -> 0x005f, blocks: (B:11:0x0052, B:21:0x007a, B:26:0x0084), top: B:232:0x0052 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0089 A[Catch: all -> 0x026f, IOException -> 0x0280, TRY_ENTER, TryCatch #22 {IOException -> 0x0280, all -> 0x026f, blocks: (B:8:0x004a, B:29:0x008b, B:28:0x0089, B:23:0x007f), top: B:234:0x004a }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01a1  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 6 */
    @Override // com.android.server.pm.PackageAbiHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.util.Pair<com.android.server.pm.PackageAbiHelper.Abis, com.android.server.pm.PackageAbiHelper.NativeLibraryPaths> derivePackageAbi(com.android.server.pm.pkg.AndroidPackage r27, boolean r28, boolean r29, java.lang.String r30, java.io.File r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1015
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageAbiHelperImpl.derivePackageAbi(com.android.server.pm.pkg.AndroidPackage, boolean, boolean, java.lang.String, java.io.File):android.util.Pair");
    }

    private boolean shouldExtractLibs(com.android.server.pm.pkg.AndroidPackage pkg, boolean isSystemApp, boolean isUpdatedSystemApp) {
        boolean extractLibs = !com.android.server.pm.parsing.pkg.AndroidPackageUtils.isLibrary(pkg) && pkg.isExtractNativeLibrariesRequested();
        if (isSystemApp && !isUpdatedSystemApp) {
            return false;
        }
        return extractLibs;
    }

    @Override // com.android.server.pm.PackageAbiHelper
    public java.lang.String getAdjustedAbiForSharedUser(android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packagesForUser, com.android.server.pm.pkg.AndroidPackage scannedPackage) {
        java.lang.String pkgRawPrimaryCpuAbi;
        java.lang.String requiredInstructionSet = null;
        if (scannedPackage != null && (pkgRawPrimaryCpuAbi = com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(scannedPackage)) != null) {
            requiredInstructionSet = dalvik.system.VMRuntime.getInstructionSet(pkgRawPrimaryCpuAbi);
        }
        com.android.server.pm.pkg.PackageStateInternal requirer = null;
        for (com.android.server.pm.pkg.PackageStateInternal ps : packagesForUser) {
            if (scannedPackage == null || !scannedPackage.getPackageName().equals(ps.getPackageName())) {
                if (ps.getPrimaryCpuAbiLegacy() != null) {
                    java.lang.String instructionSet = dalvik.system.VMRuntime.getInstructionSet(ps.getPrimaryCpuAbiLegacy());
                    if (requiredInstructionSet != null && !requiredInstructionSet.equals(instructionSet)) {
                        java.lang.String errorMessage = "Instruction set mismatch, " + (requirer == null ? "[caller]" : requirer) + " requires " + requiredInstructionSet + " whereas " + ps + " requires " + instructionSet;
                        android.util.Slog.w("PackageManager", errorMessage);
                    }
                    if (requiredInstructionSet == null) {
                        requiredInstructionSet = instructionSet;
                        requirer = ps;
                    }
                }
            }
        }
        if (requiredInstructionSet == null) {
            return null;
        }
        if (requirer != null) {
            java.lang.String adjustedAbi = requirer.getPrimaryCpuAbiLegacy();
            return adjustedAbi;
        }
        java.lang.String adjustedAbi2 = com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(scannedPackage);
        return adjustedAbi2;
    }
}
