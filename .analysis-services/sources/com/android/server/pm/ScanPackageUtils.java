package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ScanPackageUtils {
    ScanPackageUtils() {
    }

    public static com.android.server.pm.ScanResult scanPackageOnlyLI(com.android.server.pm.ScanRequest request, com.android.server.pm.PackageManagerServiceInjector injector, boolean isUnderFactoryTest, long currentTime) throws com.android.server.pm.PackageManagerException {
        boolean needToDeriveAbi;
        java.lang.String primaryCpuAbiFromSettings;
        java.lang.String secondaryCpuAbiFromSettings;
        boolean isPendingRestoreBefore;
        java.lang.String[] usesSdkLibraries;
        java.lang.String[] usesStaticLibraries;
        java.lang.String str;
        java.lang.String secondaryCpuAbiFromSettings2;
        com.android.server.pm.PackageSetting pkgSetting;
        int userId;
        java.lang.String cpuAbiOverride;
        com.android.server.pm.SharedUserSetting oldSharedUserSetting;
        java.lang.String cpuAbiOverride2;
        java.lang.String cpuAbiOverride3;
        com.android.server.pm.SharedUserSetting oldSharedUserSetting2;
        long existingFirstInstallTime;
        android.content.pm.SharedLibraryInfo sdkLibraryInfo;
        android.content.pm.SharedLibraryInfo staticSharedLibraryInfo;
        java.util.List<android.content.pm.SharedLibraryInfo> dynamicSharedLibraryInfos;
        boolean isPlatformPackage;
        java.lang.String str2;
        com.android.server.pm.PackageAbiHelper packageAbiHelper = injector.getAbiHelper();
        com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage = request.mParsedPackage;
        com.android.server.pm.PackageSetting pkgSetting2 = request.mPkgSetting;
        com.android.server.pm.PackageSetting disabledPkgSetting = request.mDisabledPkgSetting;
        com.android.server.pm.PackageSetting originalPkgSetting = request.mOriginalPkgSetting;
        int parseFlags = request.mParseFlags;
        int scanFlags = request.mScanFlags;
        java.lang.String realPkgName = request.mRealPkgName;
        com.android.server.pm.SharedUserSetting oldSharedUserSetting3 = request.mOldSharedUserSetting;
        com.android.server.pm.SharedUserSetting sharedUserSetting = request.mSharedUserSetting;
        android.os.UserHandle user = request.mUser;
        boolean isPlatformPackage2 = request.mIsPlatformPackage;
        java.util.List<java.lang.String> changedAbiCodePath = null;
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && (Integer.MIN_VALUE & parseFlags) != 0) {
            android.util.Log.d("PackageManager", "Scanning package " + parsedPackage.getPackageName());
        }
        java.io.File destCodeFile = new java.io.File(parsedPackage.getPath());
        java.lang.String secondaryCpuAbiFromSettings3 = null;
        boolean needToDeriveAbi2 = (scanFlags & 4096) != 0;
        boolean isApex = (scanFlags & 67108864) != 0;
        if (needToDeriveAbi2) {
            needToDeriveAbi = needToDeriveAbi2;
            primaryCpuAbiFromSettings = null;
        } else if (pkgSetting2 != null) {
            if (pkgSetting2.getPkg() != null && pkgSetting2.getPkg().isStub()) {
                needToDeriveAbi = true;
                primaryCpuAbiFromSettings = null;
            } else {
                java.lang.String primaryCpuAbiFromSettings2 = pkgSetting2.getPrimaryCpuAbiLegacy();
                secondaryCpuAbiFromSettings3 = pkgSetting2.getSecondaryCpuAbiLegacy();
                needToDeriveAbi = needToDeriveAbi2;
                primaryCpuAbiFromSettings = primaryCpuAbiFromSettings2;
            }
        } else {
            needToDeriveAbi = true;
            primaryCpuAbiFromSettings = null;
        }
        java.lang.String primaryCpuAbiFromSettings3 = primaryCpuAbiFromSettings;
        if (pkgSetting2 == null || oldSharedUserSetting3 == sharedUserSetting) {
            secondaryCpuAbiFromSettings = secondaryCpuAbiFromSettings3;
            isPendingRestoreBefore = false;
        } else {
            secondaryCpuAbiFromSettings = secondaryCpuAbiFromSettings3;
            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Package ").append(parsedPackage.getPackageName()).append(" shared user changed from ");
            java.lang.String str3 = "<nothing>";
            if (oldSharedUserSetting3 != null) {
                str2 = "<nothing>";
                str3 = oldSharedUserSetting3.name;
            } else {
                str2 = "<nothing>";
            }
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, sbAppend.append(str3).append(" to ").append(sharedUserSetting != null ? sharedUserSetting.name : str2).append("; replacing with new").toString());
            boolean isPendingRestoreBefore2 = pkgSetting2.isPendingRestore();
            pkgSetting2 = null;
            isPendingRestoreBefore = isPendingRestoreBefore2;
        }
        if (parsedPackage.getUsesSdkLibraries().isEmpty()) {
            usesSdkLibraries = null;
        } else {
            java.lang.String[] usesSdkLibraries2 = new java.lang.String[parsedPackage.getUsesSdkLibraries().size()];
            parsedPackage.getUsesSdkLibraries().toArray(usesSdkLibraries2);
            usesSdkLibraries = usesSdkLibraries2;
        }
        if (parsedPackage.getUsesStaticLibraries().isEmpty()) {
            usesStaticLibraries = null;
        } else {
            java.lang.String[] usesStaticLibraries2 = new java.lang.String[parsedPackage.getUsesStaticLibraries().size()];
            parsedPackage.getUsesStaticLibraries().toArray(usesStaticLibraries2);
            usesStaticLibraries = usesStaticLibraries2;
        }
        java.util.UUID newDomainSetId = injector.getDomainVerificationManagerInternal().generateNewId();
        boolean createNewPackage = pkgSetting2 == null;
        if (!createNewPackage) {
            str = " to ";
            secondaryCpuAbiFromSettings2 = secondaryCpuAbiFromSettings;
            com.android.server.pm.PackageSetting pkgSetting3 = new com.android.server.pm.PackageSetting(pkgSetting2);
            pkgSetting3.setPkg(parsedPackage);
            boolean isDontKill = (scanFlags & 1024) != 0;
            com.android.server.pm.Settings.updatePackageSetting(pkgSetting3, disabledPkgSetting, oldSharedUserSetting3, sharedUserSetting, destCodeFile, parsedPackage.getNativeLibraryDir(), pkgSetting3.getPrimaryCpuAbi(), pkgSetting3.getSecondaryCpuAbi(), com.android.server.pm.parsing.PackageInfoUtils.appInfoFlags((com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.PackageStateInternal) pkgSetting3), com.android.server.pm.parsing.PackageInfoUtils.appInfoPrivateFlags((com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.PackageStateInternal) pkgSetting3), com.android.server.pm.UserManagerService.getInstance(), usesSdkLibraries, parsedPackage.getUsesSdkLibrariesVersionsMajor(), parsedPackage.getUsesSdkLibrariesOptional(), usesStaticLibraries, parsedPackage.getUsesStaticLibrariesVersions(), parsedPackage.getMimeGroups(), newDomainSetId, parsedPackage.getTargetSdkVersion(), parsedPackage.getRestrictUpdateHash(), isDontKill);
            pkgSetting = pkgSetting3;
        } else {
            boolean instantApp = (scanFlags & 8192) != 0;
            boolean virtualPreload = (32768 & scanFlags) != 0;
            boolean isStoppedSystemApp = (134217728 & scanFlags) != 0;
            int pkgFlags = com.android.server.pm.parsing.PackageInfoUtils.appInfoFlags((com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.PackageStateInternal) null);
            int pkgPrivateFlags = com.android.server.pm.parsing.PackageInfoUtils.appInfoPrivateFlags((com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.PackageStateInternal) null);
            str = " to ";
            secondaryCpuAbiFromSettings2 = secondaryCpuAbiFromSettings;
            com.android.server.pm.PackageSetting pkgSetting4 = com.android.server.pm.Settings.createNewSetting(parsedPackage.getPackageName(), originalPkgSetting, disabledPkgSetting, realPkgName, sharedUserSetting, destCodeFile, parsedPackage.getNativeLibraryRootDir(), com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage), com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage), parsedPackage.getLongVersionCode(), pkgFlags, pkgPrivateFlags, user, true, instantApp, virtualPreload, isStoppedSystemApp, com.android.server.pm.UserManagerService.getInstance(), usesSdkLibraries, parsedPackage.getUsesSdkLibrariesVersionsMajor(), parsedPackage.getUsesSdkLibrariesOptional(), usesStaticLibraries, parsedPackage.getUsesStaticLibrariesVersions(), parsedPackage.getMimeGroups(), newDomainSetId, parsedPackage.getTargetSdkVersion(), parsedPackage.getRestrictUpdateHash());
            ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).afterCreateNewSettingInScanPackageOnlyLI(parsedPackage, pkgSetting4, request);
            if (isPendingRestoreBefore) {
                pkgSetting4.setPendingRestore(true);
            }
            pkgSetting = pkgSetting4;
        }
        if (createNewPackage && originalPkgSetting != null) {
            parsedPackage.setPackageName(originalPkgSetting.getPackageName());
            java.lang.String msg = "New package " + pkgSetting.getRealName() + " renamed to replace old package " + pkgSetting.getPackageName();
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg);
        }
        int userId2 = user == null ? 0 : user.getIdentifier();
        if (!createNewPackage) {
            boolean instantApp2 = (scanFlags & 8192) != 0;
            boolean fullApp = (scanFlags & 16384) != 0;
            setInstantAppForUser(injector, pkgSetting, userId2, instantApp2, fullApp);
        }
        if (disabledPkgSetting != null || ((scanFlags & 4) != 0 && pkgSetting != null && pkgSetting.isSystem())) {
            pkgSetting.getPkgState().setUpdatedSystemApp(true);
        }
        pkgSetting.getTransientState().setSeInfo(com.android.server.pm.SELinuxMMAC.getSeInfo((com.android.server.pm.pkg.PackageState) pkgSetting, (com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.SharedUserApi) sharedUserSetting, injector.getCompatibility()));
        if (pkgSetting.isSystem()) {
            configurePackageComponents(parsedPackage);
        }
        java.lang.String cpuAbiOverride4 = com.android.server.pm.PackageManagerServiceUtils.deriveAbiOverride(request.mCpuAbiOverride);
        boolean isSystemApp = pkgSetting.isSystem();
        boolean isUpdatedSystemApp = pkgSetting.isUpdatedSystemApp();
        java.io.File appLib32InstallDir = getAppLib32InstallDir();
        if (isApex) {
            userId = userId2;
            cpuAbiOverride = cpuAbiOverride4;
            oldSharedUserSetting = oldSharedUserSetting3;
            cpuAbiOverride2 = "PackageManager";
        } else {
            if ((scanFlags & 4) == 0) {
                if (needToDeriveAbi) {
                    android.os.Trace.traceBegin(262144L, "derivePackageAbi");
                    userId = userId2;
                    isPlatformPackage = isPlatformPackage2;
                    cpuAbiOverride = cpuAbiOverride4;
                    oldSharedUserSetting = oldSharedUserSetting3;
                    try {
                        android.util.Pair<com.android.server.pm.PackageAbiHelper.Abis, com.android.server.pm.PackageAbiHelper.NativeLibraryPaths> derivedAbi = packageAbiHelper.derivePackageAbi(parsedPackage, isSystemApp, isUpdatedSystemApp, cpuAbiOverride, appLib32InstallDir);
                        ((com.android.server.pm.PackageAbiHelper.Abis) derivedAbi.first).applyTo(parsedPackage);
                        ((com.android.server.pm.PackageAbiHelper.NativeLibraryPaths) derivedAbi.second).applyTo(parsedPackage);
                        android.os.Trace.traceEnd(262144L);
                        java.lang.String pkgRawPrimaryCpuAbi = com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage);
                        if (isSystemApp && !isUpdatedSystemApp && pkgRawPrimaryCpuAbi == null) {
                            com.android.server.pm.PackageAbiHelper.Abis abis = packageAbiHelper.getBundledAppAbis(parsedPackage);
                            abis.applyTo(parsedPackage);
                            abis.applyTo(pkgSetting);
                            com.android.server.pm.PackageAbiHelper.NativeLibraryPaths nativeLibraryPaths = packageAbiHelper.deriveNativeLibraryPaths(parsedPackage, isSystemApp, isUpdatedSystemApp, appLib32InstallDir);
                            nativeLibraryPaths.applyTo(parsedPackage);
                        }
                        cpuAbiOverride2 = "PackageManager";
                        scanFlags = scanFlags;
                    } catch (java.lang.Throwable th) {
                        android.os.Trace.traceEnd(262144L);
                        throw th;
                    }
                } else {
                    userId = userId2;
                    cpuAbiOverride = cpuAbiOverride4;
                    oldSharedUserSetting = oldSharedUserSetting3;
                    isPlatformPackage = isPlatformPackage2;
                    parsedPackage.setPrimaryCpuAbi(primaryCpuAbiFromSettings3).setSecondaryCpuAbi(secondaryCpuAbiFromSettings2);
                    com.android.server.pm.PackageAbiHelper.NativeLibraryPaths nativeLibraryPaths2 = packageAbiHelper.deriveNativeLibraryPaths(parsedPackage, isSystemApp, isUpdatedSystemApp, appLib32InstallDir);
                    nativeLibraryPaths2.applyTo(parsedPackage);
                    if (!com.android.server.pm.PackageManagerService.DEBUG_ABI_SELECTION) {
                        cpuAbiOverride2 = "PackageManager";
                    } else {
                        cpuAbiOverride2 = "PackageManager";
                        android.util.Slog.i(cpuAbiOverride2, "Using ABIS and native lib paths from settings : " + parsedPackage.getPackageName() + " " + com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage) + ", " + com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage));
                    }
                    scanFlags = scanFlags;
                }
            } else {
                userId = userId2;
                cpuAbiOverride = cpuAbiOverride4;
                oldSharedUserSetting = oldSharedUserSetting3;
                cpuAbiOverride2 = "PackageManager";
                isPlatformPackage = isPlatformPackage2;
                if ((scanFlags & 256) != 0) {
                    parsedPackage.setPrimaryCpuAbi(pkgSetting.getPrimaryCpuAbiLegacy()).setSecondaryCpuAbi(pkgSetting.getSecondaryCpuAbiLegacy());
                }
                com.android.server.pm.PackageAbiHelper.NativeLibraryPaths nativeLibraryPaths3 = packageAbiHelper.deriveNativeLibraryPaths(parsedPackage, isSystemApp, isUpdatedSystemApp, appLib32InstallDir);
                nativeLibraryPaths3.applyTo(parsedPackage);
            }
            if (isPlatformPackage) {
                if (android.os.Build.OPLUS_64BIT_ONLY_CHIP) {
                    parsedPackage.setPrimaryCpuAbi(dalvik.system.VMRuntime.getRuntime().is64Bit() ? android.os.Build.SUPPORTED_64_BIT_ABIS[0] : android.os.Build.MTK_HBT_SUPPORTED_32_BIT_ABIS[0]);
                } else {
                    parsedPackage.setPrimaryCpuAbi(dalvik.system.VMRuntime.getRuntime().is64Bit() ? android.os.Build.SUPPORTED_64_BIT_ABIS[0] : android.os.Build.SUPPORTED_32_BIT_ABIS[0]);
                }
            }
        }
        if ((scanFlags & 1) != 0 || (scanFlags & 4) == 0) {
            cpuAbiOverride3 = cpuAbiOverride;
        } else {
            cpuAbiOverride3 = cpuAbiOverride;
            if (cpuAbiOverride3 == null) {
                android.util.Slog.w(cpuAbiOverride2, "Ignoring persisted ABI override for package " + parsedPackage.getPackageName());
            }
        }
        pkgSetting.setPrimaryCpuAbi(com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(parsedPackage)).setSecondaryCpuAbi(com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawSecondaryCpuAbi(parsedPackage)).setCpuAbiOverride(cpuAbiOverride3);
        if (com.android.server.pm.PackageManagerService.DEBUG_ABI_SELECTION) {
            android.util.Slog.d(cpuAbiOverride2, "Resolved nativeLibraryRoot for " + parsedPackage.getPackageName() + " to root=" + parsedPackage.getNativeLibraryRootDir() + ", to dir=" + parsedPackage.getNativeLibraryDir() + ", isa=" + parsedPackage.isNativeLibraryRootRequiresIsa());
        }
        pkgSetting.setLegacyNativeLibraryPath(parsedPackage.getNativeLibraryRootDir());
        if (com.android.server.pm.PackageManagerService.DEBUG_ABI_SELECTION) {
            android.util.Log.d(cpuAbiOverride2, "Abis for package[" + parsedPackage.getPackageName() + "] are primary=" + pkgSetting.getPrimaryCpuAbiLegacy() + " secondary=" + pkgSetting.getSecondaryCpuAbiLegacy() + " abiOverride=" + pkgSetting.getCpuAbiOverride());
        }
        if ((scanFlags & 16) == 0) {
            oldSharedUserSetting2 = oldSharedUserSetting;
            if (oldSharedUserSetting2 != null) {
                changedAbiCodePath = applyAdjustedAbiToSharedUser(oldSharedUserSetting2, parsedPackage, packageAbiHelper.getAdjustedAbiForSharedUser(oldSharedUserSetting2.getPackageStates(), parsedPackage));
            }
        } else {
            oldSharedUserSetting2 = oldSharedUserSetting;
        }
        parsedPackage.setFactoryTest(isUnderFactoryTest && parsedPackage.getRequestedPermissions().contains("android.permission.FACTORY_TEST"));
        if (isSystemApp) {
            pkgSetting.setIsOrphaned(true);
        }
        long scanFileTime = com.android.server.pm.PackageManagerServiceUtils.getLastModifiedTime(parsedPackage);
        int userId3 = userId;
        if (userId3 == -1) {
            existingFirstInstallTime = com.android.server.pm.pkg.PackageStateUtils.getEarliestFirstInstallTime(pkgSetting.getUserStates());
        } else {
            existingFirstInstallTime = pkgSetting.readUserState(userId3).getFirstInstallTimeMillis();
        }
        java.lang.String str4 = cpuAbiOverride2;
        if (currentTime != 0) {
            if (existingFirstInstallTime == 0) {
                pkgSetting.setFirstInstallTime(currentTime, userId3).setLastUpdateTime(currentTime);
            } else if ((scanFlags & 8) != 0) {
                pkgSetting.setLastUpdateTime(currentTime);
            }
        } else if (existingFirstInstallTime == 0) {
            pkgSetting.setFirstInstallTime(scanFileTime, userId3).setLastUpdateTime(scanFileTime);
        } else if ((parseFlags & 16) != 0 && scanFileTime != pkgSetting.getLastModifiedTime()) {
            pkgSetting.setLastUpdateTime(scanFileTime);
        }
        pkgSetting.setLastModifiedTime(scanFileTime);
        pkgSetting.setPkg(parsedPackage).setFlags(com.android.server.pm.parsing.PackageInfoUtils.appInfoFlags((com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.PackageStateInternal) pkgSetting)).setPrivateFlags(com.android.server.pm.parsing.PackageInfoUtils.appInfoPrivateFlags((com.android.server.pm.pkg.AndroidPackage) parsedPackage, (com.android.server.pm.pkg.PackageStateInternal) pkgSetting));
        if (parsedPackage.getLongVersionCode() != pkgSetting.getVersionCode()) {
            pkgSetting.setLongVersionCode(parsedPackage.getLongVersionCode());
        }
        java.lang.String volumeUuid = parsedPackage.getVolumeUuid();
        if (!java.util.Objects.equals(volumeUuid, pkgSetting.getVolumeUuid())) {
            android.util.Slog.i(str4, "Update" + (pkgSetting.isSystem() ? " system" : "") + " package " + parsedPackage.getPackageName() + " volume from " + pkgSetting.getVolumeUuid() + str + volumeUuid);
            pkgSetting.setVolumeUuid(volumeUuid);
        }
        if (android.text.TextUtils.isEmpty(parsedPackage.getSdkLibraryName())) {
            sdkLibraryInfo = null;
        } else {
            android.content.pm.SharedLibraryInfo sdkLibraryInfo2 = com.android.server.pm.parsing.pkg.AndroidPackageUtils.createSharedLibraryForSdk(parsedPackage);
            sdkLibraryInfo = sdkLibraryInfo2;
        }
        if (android.text.TextUtils.isEmpty(parsedPackage.getStaticSharedLibraryName())) {
            staticSharedLibraryInfo = null;
        } else {
            android.content.pm.SharedLibraryInfo staticSharedLibraryInfo2 = com.android.server.pm.parsing.pkg.AndroidPackageUtils.createSharedLibraryForStatic(parsedPackage);
            staticSharedLibraryInfo = staticSharedLibraryInfo2;
        }
        if (com.android.internal.util.ArrayUtils.isEmpty(parsedPackage.getLibraryNames())) {
            dynamicSharedLibraryInfos = null;
        } else {
            java.util.List<android.content.pm.SharedLibraryInfo> dynamicSharedLibraryInfos2 = new java.util.ArrayList<>(parsedPackage.getLibraryNames().size());
            for (java.lang.String name : parsedPackage.getLibraryNames()) {
                dynamicSharedLibraryInfos2.add(com.android.server.pm.parsing.pkg.AndroidPackageUtils.createSharedLibraryForDynamic(parsedPackage, name));
                volumeUuid = volumeUuid;
            }
            dynamicSharedLibraryInfos = dynamicSharedLibraryInfos2;
        }
        return new com.android.server.pm.ScanResult(request, pkgSetting, changedAbiCodePath, !createNewPackage, -1, sdkLibraryInfo, staticSharedLibraryInfo, dynamicSharedLibraryInfos);
    }

    public static int adjustScanFlagsWithPackageSetting(int scanFlags, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.PackageSetting disabledPkgSetting, android.os.UserHandle user) {
        com.android.server.pm.PackageSetting systemPkgSetting;
        if ((scanFlags & 4) != 0 && disabledPkgSetting == null && pkgSetting != null && pkgSetting.isSystem()) {
            systemPkgSetting = pkgSetting;
        } else {
            systemPkgSetting = disabledPkgSetting;
        }
        if (systemPkgSetting != null) {
            scanFlags |= 65536;
            if ((systemPkgSetting.getPrivateFlags() & 8) != 0) {
                scanFlags |= 131072;
            }
            if ((systemPkgSetting.getPrivateFlags() & 131072) != 0) {
                scanFlags |= 262144;
            }
            if ((systemPkgSetting.getPrivateFlags() & 262144) != 0) {
                scanFlags |= 524288;
            }
            if ((systemPkgSetting.getPrivateFlags() & 524288) != 0) {
                scanFlags |= 1048576;
            }
            if ((systemPkgSetting.getPrivateFlags() & 2097152) != 0) {
                scanFlags |= 2097152;
            }
            if ((systemPkgSetting.getPrivateFlags() & 1073741824) != 0) {
                scanFlags |= 4194304;
            }
        }
        if (pkgSetting != null) {
            int userId = user == null ? 0 : user.getIdentifier();
            if (pkgSetting.getInstantApp(userId)) {
                scanFlags |= 8192;
            }
            if (pkgSetting.getVirtualPreload(userId)) {
                return scanFlags | 32768;
            }
            return scanFlags;
        }
        return scanFlags;
    }

    public static void assertCodePolicy(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        boolean shouldHaveCode = pkg.isDeclaredHavingCode();
        if (shouldHaveCode && !apkHasCode(pkg.getBaseApkPath())) {
            throw new com.android.server.pm.PackageManagerException(-2, "Package " + pkg.getBaseApkPath() + " code is missing");
        }
        if (!com.android.internal.util.ArrayUtils.isEmpty(pkg.getSplitCodePaths())) {
            for (int i = 0; i < pkg.getSplitCodePaths().length; i++) {
                boolean splitShouldHaveCode = (pkg.getSplitFlags()[i] & 4) != 0;
                if (splitShouldHaveCode && !apkHasCode(pkg.getSplitCodePaths()[i])) {
                    throw new com.android.server.pm.PackageManagerException(-2, "Package " + pkg.getSplitCodePaths()[i] + " code is missing");
                }
            }
        }
    }

    public static void assertStaticSharedLibraryIsValid(com.android.server.pm.pkg.AndroidPackage pkg, int scanFlags) throws com.android.server.pm.PackageManagerException {
        if (pkg.getTargetSdkVersion() < 26) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Packages declaring static-shared libs must target O SDK or higher", -22);
        }
        if ((scanFlags & 8192) != 0) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot be instant apps", -23);
        }
        if (!com.android.internal.util.ArrayUtils.isEmpty(pkg.getOriginalPackages())) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot be renamed", -24);
        }
        if (!com.android.internal.util.ArrayUtils.isEmpty(pkg.getLibraryNames())) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot declare dynamic libs", -25);
        }
        if (pkg.getSharedUserId() != null) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Packages declaring static-shared libs cannot declare shared users", -26);
        }
        if (!pkg.getActivities().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare activities", -27);
        }
        if (!pkg.getServices().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare services", -28);
        }
        if (!pkg.getProviders().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare content providers", -29);
        }
        if (!pkg.getReceivers().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare broadcast receivers", -30);
        }
        if (!pkg.getPermissionGroups().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare permission groups", -31);
        }
        if (!pkg.getAttributions().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare features", -32);
        }
        if (!pkg.getPermissions().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare permissions", -33);
        }
        if (!pkg.getProtectedBroadcasts().isEmpty()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot declare protected broadcasts", -34);
        }
        if (pkg.getOverlayTarget() != null) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared libs cannot be overlay targets", -35);
        }
    }

    public static void assertProcessesAreValid(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        java.util.Map<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> procs = pkg.getProcesses();
        if (!procs.isEmpty()) {
            if (!procs.containsKey(pkg.getProcessName())) {
                throw new com.android.server.pm.PackageManagerException(-122, "Can't install because application tag's process attribute " + pkg.getProcessName() + " (in package " + pkg.getPackageName() + ") is not included in the <processes> list");
            }
            assertPackageProcesses(pkg, pkg.getActivities(), procs, com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
            assertPackageProcesses(pkg, pkg.getServices(), procs, com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE);
            assertPackageProcesses(pkg, pkg.getReceivers(), procs, "receiver");
            assertPackageProcesses(pkg, pkg.getProviders(), procs, "provider");
        }
    }

    private static <T extends com.android.internal.pm.pkg.component.ParsedMainComponent> void assertPackageProcesses(com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<T> components, java.util.Map<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> procs, java.lang.String compName) throws com.android.server.pm.PackageManagerException {
        if (components == null) {
            return;
        }
        for (int i = components.size() - 1; i >= 0; i--) {
            com.android.internal.pm.pkg.component.ParsedMainComponent component = components.get(i);
            if (!procs.containsKey(component.getProcessName())) {
                throw new com.android.server.pm.PackageManagerException(-122, "Can't install because " + compName + " " + component.getClassName() + "'s process attribute " + component.getProcessName() + " (in package " + pkg.getPackageName() + ") is not included in the <processes> list");
            }
        }
    }

    public static void assertMinSignatureSchemeIsValid(com.android.server.pm.pkg.AndroidPackage pkg, int parseFlags) throws com.android.server.pm.PackageManagerException {
        int minSignatureSchemeVersion = android.util.apk.ApkSignatureVerifier.getMinimumSignatureSchemeVersionForTargetSdk(pkg.getTargetSdkVersion());
        if (pkg.getSigningDetails().getSignatureSchemeVersion() < minSignatureSchemeVersion) {
            throw new com.android.server.pm.PackageManagerException(com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_INVALID_TRANSITION, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + pkg.getPackageName());
        }
    }

    public static java.lang.String getRealPackageName(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String renamedPkgName, boolean isSystemApp) {
        if (isPackageRenamed(pkg, renamedPkgName)) {
            return com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRealPackageOrNull(pkg, isSystemApp);
        }
        return null;
    }

    public static boolean isPackageRenamed(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String renamedPkgName) {
        return pkg.getOriginalPackages().contains(renamedPkgName);
    }

    public static void ensurePackageRenamed(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, java.lang.String renamedPackageName) {
        if (!parsedPackage.getOriginalPackages().contains(renamedPackageName) || parsedPackage.getPackageName().equals(renamedPackageName)) {
            return;
        }
        parsedPackage.setPackageName(renamedPackageName);
    }

    public static boolean apkHasCode(java.lang.String fileName) {
        android.util.jar.StrictJarFile jarFile = null;
        try {
            jarFile = new android.util.jar.StrictJarFile(fileName, false, false);
            boolean z = jarFile.findEntry("classes.dex") != null;
            try {
                jarFile.close();
            } catch (java.io.IOException e) {
            }
            return z;
        } catch (java.io.IOException e2) {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (java.io.IOException e3) {
                }
            }
            return false;
        } catch (java.lang.Throwable th) {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (java.io.IOException e4) {
                }
            }
            throw th;
        }
    }

    public static void configurePackageComponents(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> componentsEnabledStates = com.android.server.SystemConfig.getInstance().getComponentsEnabledStates(pkg.getPackageName());
        if (componentsEnabledStates == null) {
            return;
        }
        for (int i = com.android.internal.util.ArrayUtils.size(pkg.getActivities()) - 1; i >= 0; i--) {
            com.android.internal.pm.pkg.component.ParsedActivity component = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getActivities().get(i);
            java.lang.Boolean enabled = componentsEnabledStates.get(component.getName());
            if (enabled != null) {
                com.android.internal.pm.pkg.component.ComponentMutateUtils.setEnabled(component, enabled.booleanValue());
            }
        }
        for (int i2 = com.android.internal.util.ArrayUtils.size(pkg.getReceivers()) - 1; i2 >= 0; i2--) {
            com.android.internal.pm.pkg.component.ParsedActivity component2 = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getReceivers().get(i2);
            java.lang.Boolean enabled2 = componentsEnabledStates.get(component2.getName());
            if (enabled2 != null) {
                com.android.internal.pm.pkg.component.ComponentMutateUtils.setEnabled(component2, enabled2.booleanValue());
            }
        }
        for (int i3 = com.android.internal.util.ArrayUtils.size(pkg.getProviders()) - 1; i3 >= 0; i3--) {
            com.android.internal.pm.pkg.component.ParsedProvider component3 = (com.android.internal.pm.pkg.component.ParsedProvider) pkg.getProviders().get(i3);
            java.lang.Boolean enabled3 = componentsEnabledStates.get(component3.getName());
            if (enabled3 != null) {
                com.android.internal.pm.pkg.component.ComponentMutateUtils.setEnabled(component3, enabled3.booleanValue());
            }
        }
        for (int i4 = com.android.internal.util.ArrayUtils.size(pkg.getServices()) - 1; i4 >= 0; i4--) {
            com.android.internal.pm.pkg.component.ParsedService component4 = (com.android.internal.pm.pkg.component.ParsedService) pkg.getServices().get(i4);
            java.lang.Boolean enabled4 = componentsEnabledStates.get(component4.getName());
            if (enabled4 != null) {
                com.android.internal.pm.pkg.component.ComponentMutateUtils.setEnabled(component4, enabled4.booleanValue());
            }
        }
    }

    public static int getVendorPartitionVersion() {
        java.lang.String version = android.os.SystemProperties.get("ro.vndk.version");
        if (!version.isEmpty()) {
            try {
                return java.lang.Integer.parseInt(version);
            } catch (java.lang.NumberFormatException e) {
                if (com.android.internal.util.ArrayUtils.contains(android.os.Build.VERSION.ACTIVE_CODENAMES, version)) {
                    return 10000;
                }
                return 28;
            }
        }
        return 28;
    }

    public static void applyPolicy(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int scanFlags, com.android.server.pm.pkg.AndroidPackage platformPkg, boolean isUpdatedSystemApp) {
        boolean isSystemApp = isUpdatedSystemApp;
        boolean z = true;
        if ((65536 & scanFlags) != 0) {
            isSystemApp = true;
            parsedPackage.setSystem(true);
            if (parsedPackage.isDirectBootAware()) {
                parsedPackage.setAllComponentsDirectBootAware(true);
            }
            if (com.android.server.pm.PackageManagerServiceUtils.compressedFileExists(parsedPackage.getPath())) {
                parsedPackage.setStub(true);
            }
        } else {
            parsedPackage.clearProtectedBroadcasts().setCoreApp(false).setPersistent(false).setDefaultToDeviceProtectedStorage(false).setDirectBootAware(false).capPermissionPriorities();
        }
        if ((scanFlags & 131072) == 0) {
            parsedPackage.markNotActivitiesAsNotExportedIfSingleUser();
        }
        parsedPackage.setApex((67108864 & scanFlags) != 0);
        parsedPackage.setPrivileged((131072 & scanFlags) != 0).setOem((262144 & scanFlags) != 0).setVendor((524288 & scanFlags) != 0).setProduct((1048576 & scanFlags) != 0).setSystemExt((2097152 & scanFlags) != 0).setOdm((4194304 & scanFlags) != 0);
        if (!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(parsedPackage.getPackageName()) && (platformPkg == null || com.android.server.pm.PackageManagerServiceUtils.compareSignatures(platformPkg.getSigningDetails(), parsedPackage.getSigningDetails()) != 0)) {
            z = false;
        }
        parsedPackage.setSignedWithPlatformKey(z);
        if (!isSystemApp) {
            parsedPackage.clearOriginalPackages().clearAdoptPermissions();
        }
        com.android.server.pm.parsing.library.PackageBackwardCompatibility.modifySharedLibraries(parsedPackage, isSystemApp, isUpdatedSystemApp);
    }

    public static java.util.List<java.lang.String> applyAdjustedAbiToSharedUser(com.android.server.pm.SharedUserSetting sharedUserSetting, com.android.internal.pm.parsing.pkg.ParsedPackage scannedPackage, java.lang.String adjustedAbi) {
        if (scannedPackage != null) {
            scannedPackage.setPrimaryCpuAbi(adjustedAbi);
        }
        java.util.List<java.lang.String> changedAbiCodePath = null;
        com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> sharedUserPackageSettings = sharedUserSetting.getPackageSettings();
        for (int i = 0; i < sharedUserPackageSettings.size(); i++) {
            com.android.server.pm.PackageSetting ps = sharedUserPackageSettings.valueAt(i);
            if ((scannedPackage == null || !scannedPackage.getPackageName().equals(ps.getPackageName())) && ps.getPrimaryCpuAbiLegacy() == null) {
                ps.setPrimaryCpuAbi(adjustedAbi);
                ps.onChanged();
                if (ps.getPkg() != null && !android.text.TextUtils.equals(adjustedAbi, com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(ps.getPkg()))) {
                    if (com.android.server.pm.PackageManagerService.DEBUG_ABI_SELECTION) {
                        android.util.Slog.i("PackageManager", "Adjusting ABI for " + ps.getPackageName() + " to " + adjustedAbi + " (scannedPackage=" + (scannedPackage != null ? scannedPackage : "null") + ")");
                    }
                    if (changedAbiCodePath == null) {
                        changedAbiCodePath = new java.util.ArrayList<>();
                    }
                    changedAbiCodePath.add(ps.getPathString());
                }
            }
        }
        return changedAbiCodePath;
    }

    public static void collectCertificatesLI(com.android.server.pm.PackageSetting ps, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, com.android.server.pm.Settings.VersionInfo settingsVersionForPackage, boolean forceCollect, boolean skipVerify, boolean isPreNMR1Upgrade) throws com.android.server.pm.PackageManagerException {
        long lastModifiedTime;
        if (isPreNMR1Upgrade) {
            lastModifiedTime = new java.io.File(parsedPackage.getPath()).lastModified();
        } else {
            lastModifiedTime = com.android.server.pm.PackageManagerServiceUtils.getLastModifiedTime(parsedPackage);
        }
        if (ps != null && !forceCollect && ps.getPathString().equals(parsedPackage.getPath()) && ps.getLastModifiedTime() == lastModifiedTime && !com.android.server.pm.ReconcilePackageUtils.isCompatSignatureUpdateNeeded(settingsVersionForPackage) && !com.android.server.pm.ReconcilePackageUtils.isRecoverSignatureUpdateNeeded(settingsVersionForPackage)) {
            if (ps.getSigningDetails().getSignatures() == null || ps.getSigningDetails().getSignatures().length == 0 || ps.getSigningDetails().getSignatureSchemeVersion() == 0) {
                android.util.Slog.w("PackageManager", "PackageSetting for " + ps.getPackageName() + " is missing signatures.  Collecting certs again to recover them.");
            } else {
                parsedPackage.setSigningDetails(new android.content.pm.SigningDetails(ps.getSigningDetails()));
                return;
            }
        } else {
            android.util.Slog.i("PackageManager", parsedPackage.getPath() + " changed; collecting certs" + (forceCollect ? " (forced)" : ""));
        }
        try {
            android.os.Trace.traceBegin(262144L, "collectCertificates");
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            android.content.pm.parsing.result.ParseResult<android.content.pm.SigningDetails> result = com.android.internal.pm.pkg.parsing.ParsingPackageUtils.getSigningDetails(input, parsedPackage, skipVerify);
            if (result.isError()) {
                throw new com.android.server.pm.PackageManagerException(result.getErrorCode(), result.getErrorMessage(), result.getException());
            }
            parsedPackage.setSigningDetails((android.content.pm.SigningDetails) result.getResult());
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    public static void setInstantAppForUser(com.android.server.pm.PackageManagerServiceInjector injector, com.android.server.pm.PackageSetting pkgSetting, int userId, boolean instantApp, boolean fullApp) {
        if (!instantApp && !fullApp) {
            return;
        }
        if (userId != -1) {
            if (instantApp && !pkgSetting.getInstantApp(userId)) {
                pkgSetting.setInstantApp(true, userId);
                return;
            } else {
                if (fullApp && pkgSetting.getInstantApp(userId)) {
                    pkgSetting.setInstantApp(false, userId);
                    return;
                }
                return;
            }
        }
        for (int currentUserId : injector.getUserManagerInternal().getUserIds()) {
            if (instantApp && !pkgSetting.getInstantApp(currentUserId)) {
                pkgSetting.setInstantApp(true, currentUserId);
            } else if (fullApp && pkgSetting.getInstantApp(currentUserId)) {
                pkgSetting.setInstantApp(false, currentUserId);
            }
        }
    }

    public static java.io.File getAppLib32InstallDir() {
        return new java.io.File(android.os.Environment.getDataDirectory(), "app-lib");
    }
}
