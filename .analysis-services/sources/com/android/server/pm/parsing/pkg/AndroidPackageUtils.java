package com.android.server.pm.parsing.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class AndroidPackageUtils {
    private AndroidPackageUtils() {
    }

    public static java.util.List<java.lang.String> getAllCodePathsExcludingResourceOnly(com.android.server.pm.pkg.AndroidPackage aPkg) {
        com.android.internal.pm.parsing.pkg.PackageImpl pkg = (com.android.internal.pm.parsing.pkg.PackageImpl) aPkg;
        java.util.ArrayList<java.lang.String> paths = new java.util.ArrayList<>();
        if (pkg.isDeclaredHavingCode()) {
            paths.add(pkg.getBaseApkPath());
        }
        java.lang.String[] splitCodePaths = pkg.getSplitCodePaths();
        if (!com.android.internal.util.ArrayUtils.isEmpty(splitCodePaths)) {
            for (int i = 0; i < splitCodePaths.length; i++) {
                if ((pkg.getSplitFlags()[i] & 4) != 0) {
                    paths.add(splitCodePaths[i]);
                }
            }
        }
        return paths;
    }

    public static java.util.List<java.lang.String> getAllCodePaths(com.android.server.pm.pkg.AndroidPackage aPkg) {
        com.android.internal.pm.parsing.pkg.PackageImpl pkg = (com.android.internal.pm.parsing.pkg.PackageImpl) aPkg;
        java.util.ArrayList<java.lang.String> paths = new java.util.ArrayList<>();
        paths.add(pkg.getBaseApkPath());
        java.lang.String[] splitCodePaths = pkg.getSplitCodePaths();
        if (!com.android.internal.util.ArrayUtils.isEmpty(splitCodePaths)) {
            java.util.Collections.addAll(paths, splitCodePaths);
        }
        return paths;
    }

    public static android.content.pm.SharedLibraryInfo createSharedLibraryForSdk(com.android.server.pm.pkg.AndroidPackage pkg) {
        return new android.content.pm.SharedLibraryInfo(null, pkg.getPackageName(), getAllCodePaths(pkg), pkg.getSdkLibraryName(), pkg.getSdkLibVersionMajor(), 3, new android.content.pm.VersionedPackage(pkg.getManifestPackageName(), pkg.getLongVersionCode()), null, null, false);
    }

    public static android.content.pm.SharedLibraryInfo createSharedLibraryForStatic(com.android.server.pm.pkg.AndroidPackage pkg) {
        return new android.content.pm.SharedLibraryInfo(null, pkg.getPackageName(), getAllCodePaths(pkg), pkg.getStaticSharedLibraryName(), pkg.getStaticSharedLibraryVersion(), 2, new android.content.pm.VersionedPackage(pkg.getManifestPackageName(), pkg.getLongVersionCode()), null, null, false);
    }

    public static android.content.pm.SharedLibraryInfo createSharedLibraryForDynamic(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String name) {
        return new android.content.pm.SharedLibraryInfo(null, pkg.getPackageName(), getAllCodePaths(pkg), name, -1L, 1, new android.content.pm.VersionedPackage(pkg.getPackageName(), pkg.getLongVersionCode()), null, null, false);
    }

    public static java.util.Map<java.lang.String, java.lang.String> getPackageDexMetadata(com.android.server.pm.pkg.AndroidPackage pkg) {
        return android.content.pm.dex.DexMetadataHelper.buildPackageApkToDexMetadataMap(getAllCodePaths(pkg));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.pm.parsing.PackageParserException */
    public static void validatePackageDexMetadata(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.internal.pm.parsing.PackageParserException {
        java.util.Collection<java.lang.String> apkToDexMetadataList = getPackageDexMetadata(pkg).values();
        java.lang.String packageName = pkg.getPackageName();
        long versionCode = pkg.getLongVersionCode();
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        for (java.lang.String dexMetadata : apkToDexMetadataList) {
            android.content.pm.parsing.result.ParseResult result = android.content.pm.dex.DexMetadataHelper.validateDexMetadataFile(input.reset(), dexMetadata, packageName, versionCode);
            if (result.isError()) {
                throw new com.android.internal.pm.parsing.PackageParserException(result.getErrorCode(), result.getErrorMessage(), result.getException());
            }
        }
    }

    public static com.android.internal.content.NativeLibraryHelper.Handle createNativeLibraryHandle(com.android.server.pm.pkg.AndroidPackage pkg) throws java.io.IOException {
        return com.android.internal.content.NativeLibraryHelper.Handle.create(getAllCodePaths(pkg), pkg.isMultiArch(), pkg.isExtractNativeLibrariesRequested(), pkg.isDebuggable());
    }

    public static boolean canHaveOatDir(com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg) {
        return (!packageState.isSystem() || packageState.isUpdatedSystemApp()) && !android.os.incremental.IncrementalManager.isIncrementalPath(pkg.getPath());
    }

    public static boolean hasComponentClassName(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String className) {
        java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            if (java.util.Objects.equals(className, activities.get(index).getName())) {
                return true;
            }
        }
        java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> receivers = pkg.getReceivers();
        int receiversSize = receivers.size();
        for (int index2 = 0; index2 < receiversSize; index2++) {
            if (java.util.Objects.equals(className, receivers.get(index2).getName())) {
                return true;
            }
        }
        java.util.List<com.android.internal.pm.pkg.component.ParsedProvider> providers = pkg.getProviders();
        int providersSize = providers.size();
        for (int index3 = 0; index3 < providersSize; index3++) {
            if (java.util.Objects.equals(className, providers.get(index3).getName())) {
                return true;
            }
        }
        java.util.List<com.android.internal.pm.pkg.component.ParsedService> services = pkg.getServices();
        int servicesSize = services.size();
        for (int index4 = 0; index4 < servicesSize; index4++) {
            if (java.util.Objects.equals(className, services.get(index4).getName())) {
                return true;
            }
        }
        java.util.List<com.android.internal.pm.pkg.component.ParsedInstrumentation> instrumentations = pkg.getInstrumentations();
        int instrumentationsSize = instrumentations.size();
        for (int index5 = 0; index5 < instrumentationsSize; index5++) {
            if (java.util.Objects.equals(className, instrumentations.get(index5).getName())) {
                return true;
            }
        }
        return pkg.getBackupAgentName() != null && java.util.Objects.equals(className, pkg.getBackupAgentName());
    }

    public static boolean isEncryptionAware(com.android.server.pm.pkg.AndroidPackage pkg) {
        return pkg.isDirectBootAware() || pkg.isPartiallyDirectBootAware();
    }

    public static boolean isLibrary(com.android.server.pm.pkg.AndroidPackage pkg) {
        return (pkg.getSdkLibraryName() == null && pkg.getStaticSharedLibraryName() == null && pkg.getLibraryNames().isEmpty()) ? false : true;
    }

    public static int getHiddenApiEnforcementPolicy(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal packageState) {
        boolean isAllowedToUseHiddenApis;
        if (pkg == null) {
            isAllowedToUseHiddenApis = false;
        } else {
            boolean isAllowedToUseHiddenApis2 = pkg.isSignedWithPlatformKey();
            if (isAllowedToUseHiddenApis2) {
                isAllowedToUseHiddenApis = true;
            } else {
                boolean isAllowedToUseHiddenApis3 = packageState.isSystem();
                if (isAllowedToUseHiddenApis3) {
                    isAllowedToUseHiddenApis = pkg.isNonSdkApiRequested() || com.android.server.SystemConfig.getInstance().getHiddenApiWhitelistedApps().contains(pkg.getPackageName());
                } else {
                    isAllowedToUseHiddenApis = false;
                }
            }
        }
        if (isAllowedToUseHiddenApis) {
            return 0;
        }
        return 2;
    }

    public static boolean isMatchForSystemOnly(com.android.server.pm.pkg.PackageState packageState, long flags) {
        if ((1048576 & flags) != 0) {
            return packageState.isSystem();
        }
        return true;
    }

    public static java.lang.String getRawPrimaryCpuAbi(com.android.server.pm.pkg.AndroidPackage pkg) {
        return ((com.android.internal.pm.parsing.pkg.AndroidPackageHidden) pkg).getPrimaryCpuAbi();
    }

    public static java.lang.String getRawSecondaryCpuAbi(com.android.server.pm.pkg.AndroidPackage pkg) {
        return ((com.android.internal.pm.parsing.pkg.AndroidPackageHidden) pkg).getSecondaryCpuAbi();
    }

    @java.lang.Deprecated
    public static android.content.pm.ApplicationInfo generateAppInfoWithoutState(com.android.server.pm.pkg.AndroidPackage pkg) {
        return ((com.android.internal.pm.parsing.pkg.AndroidPackageHidden) pkg).toAppInfoWithoutState();
    }

    public static java.lang.String getRealPackageOrNull(com.android.server.pm.pkg.AndroidPackage pkg, boolean isSystem) {
        if (pkg.getOriginalPackages().isEmpty() || !isSystem) {
            return null;
        }
        return pkg.getManifestPackageName();
    }

    public static void fillVersionCodes(com.android.server.pm.pkg.AndroidPackage pkg, android.content.pm.PackageInfo info) {
        info.versionCode = ((com.android.internal.pm.pkg.parsing.ParsingPackageHidden) pkg).getVersionCode();
        info.versionCodeMajor = ((com.android.internal.pm.pkg.parsing.ParsingPackageHidden) pkg).getVersionCodeMajor();
    }
}
