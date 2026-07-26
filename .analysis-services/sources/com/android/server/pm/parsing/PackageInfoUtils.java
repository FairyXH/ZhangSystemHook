package com.android.server.pm.parsing;

/* JADX INFO: loaded from: classes2.dex */
public class PackageInfoUtils {
    private static final java.lang.String SYSTEM_DATA_PATH = android.os.Environment.getDataDirectoryPath() + java.io.File.separator + "system";
    private static final java.lang.String TAG = "PackageParsing";

    public static android.content.pm.PackageInfo generate(com.android.server.pm.pkg.AndroidPackage pkg, int[] gids, long flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> installedPermissions, java.util.Set<java.lang.String> grantedPermissions, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return generateWithComponents(pkg, gids, flags, firstInstallTime, lastUpdateTime, installedPermissions, grantedPermissions, state, userId, pkgSetting);
    }

    private static android.content.pm.PackageInfo generateWithComponents(com.android.server.pm.pkg.AndroidPackage pkg, int[] gids, long flags, long firstInstallTime, long lastUpdateTime, java.util.Set<java.lang.String> installedPermissions, java.util.Set<java.lang.String> grantedPermissions, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        int N;
        int size;
        int size2;
        int size3;
        int N2;
        int num;
        android.content.pm.ActivityInfo[] res;
        int i;
        android.content.pm.ActivityInfo[] res2;
        int N3;
        int size4;
        int maxActivityInfoToGet = 2000;
        android.content.pm.ApplicationInfo applicationInfo = generateApplicationInfo(pkg, flags, state, userId, pkgSetting);
        if (applicationInfo == null) {
            return null;
        }
        android.content.pm.PackageInfo info = new android.content.pm.PackageInfo();
        info.packageName = pkg.getPackageName();
        info.splitNames = pkg.getSplitNames();
        com.android.server.pm.parsing.pkg.AndroidPackageUtils.fillVersionCodes(pkg, info);
        info.baseRevisionCode = pkg.getBaseRevisionCode();
        info.splitRevisionCodes = pkg.getSplitRevisionCodes();
        info.versionName = pkg.getVersionName();
        info.sharedUserId = pkg.getSharedUserId();
        info.sharedUserLabel = pkg.getSharedUserLabelResourceId();
        info.applicationInfo = applicationInfo;
        info.installLocation = pkg.getInstallLocation();
        if ((info.applicationInfo.flags & 1) != 0 || (info.applicationInfo.flags & 128) != 0) {
            info.requiredForAllUsers = pkg.isRequiredForAllUsers();
        }
        info.restrictedAccountType = pkg.getRestrictedAccountType();
        info.requiredAccountType = pkg.getRequiredAccountType();
        info.overlayTarget = pkg.getOverlayTarget();
        info.targetOverlayableName = pkg.getOverlayTargetOverlayableName();
        info.overlayCategory = pkg.getOverlayCategory();
        info.overlayPriority = pkg.getOverlayPriority();
        info.mOverlayIsStatic = pkg.isOverlayIsStatic();
        info.compileSdkVersion = pkg.getCompileSdkVersion();
        info.compileSdkVersionCodename = pkg.getCompileSdkVersionCodeName();
        info.firstInstallTime = firstInstallTime;
        info.lastUpdateTime = lastUpdateTime;
        if (state.getArchiveState() != null) {
            info.setArchiveTimeMillis(state.getArchiveState().getArchiveTimeMillis());
        }
        if ((256 & flags) != 0) {
            info.gids = gids;
        }
        if ((flags & 16384) != 0) {
            int size5 = pkg.getConfigPreferences().size();
            if (size5 > 0) {
                info.configPreferences = new android.content.pm.ConfigurationInfo[size5];
                pkg.getConfigPreferences().toArray(info.configPreferences);
            }
            int size6 = pkg.getRequestedFeatures().size();
            if (size6 > 0) {
                info.reqFeatures = new android.content.pm.FeatureInfo[size6];
                pkg.getRequestedFeatures().toArray(info.reqFeatures);
            }
            int size7 = pkg.getFeatureGroups().size();
            if (size7 > 0) {
                info.featureGroups = new android.content.pm.FeatureGroupInfo[size7];
                pkg.getFeatureGroups().toArray(info.featureGroups);
            }
        }
        if ((4096 & flags) != 0) {
            int size8 = com.android.internal.util.ArrayUtils.size(pkg.getPermissions());
            if (size8 > 0) {
                info.permissions = new android.content.pm.PermissionInfo[size8];
                int i2 = 0;
                while (i2 < size8) {
                    com.android.internal.pm.pkg.component.ParsedPermission permission = (com.android.internal.pm.pkg.component.ParsedPermission) pkg.getPermissions().get(i2);
                    android.content.pm.PermissionInfo permissionInfo = generatePermissionInfo(permission, flags);
                    int size9 = size8;
                    int maxActivityInfoToGet2 = maxActivityInfoToGet;
                    if (installedPermissions.contains(permission.getName())) {
                        permissionInfo.flags |= 1073741824;
                    }
                    info.permissions[i2] = permissionInfo;
                    i2++;
                    size8 = size9;
                    maxActivityInfoToGet = maxActivityInfoToGet2;
                }
            }
            java.util.List<com.android.internal.pm.pkg.component.ParsedUsesPermission> usesPermissions = pkg.getUsesPermissions();
            int size10 = usesPermissions.size();
            if (size10 > 0) {
                info.requestedPermissions = new java.lang.String[size10];
                info.requestedPermissionsFlags = new int[size10];
                int i3 = 0;
                while (i3 < size10) {
                    com.android.internal.pm.pkg.component.ParsedUsesPermission usesPermission = usesPermissions.get(i3);
                    java.util.List<com.android.internal.pm.pkg.component.ParsedUsesPermission> usesPermissions2 = usesPermissions;
                    info.requestedPermissions[i3] = usesPermission.getName();
                    int[] iArr = info.requestedPermissionsFlags;
                    iArr[i3] = iArr[i3] | 1;
                    if (grantedPermissions != null && grantedPermissions.contains(usesPermission.getName())) {
                        int[] iArr2 = info.requestedPermissionsFlags;
                        iArr2[i3] = iArr2[i3] | 2;
                    }
                    if ((usesPermission.getUsesPermissionFlags() & 65536) != 0) {
                        int[] iArr3 = info.requestedPermissionsFlags;
                        iArr3[i3] = iArr3[i3] | 65536;
                    }
                    int size11 = size10;
                    if (pkg.getImplicitPermissions().contains(info.requestedPermissions[i3])) {
                        int[] iArr4 = info.requestedPermissionsFlags;
                        iArr4[i3] = iArr4[i3] | 4;
                    }
                    i3++;
                    usesPermissions = usesPermissions2;
                    size10 = size11;
                }
            }
        }
        if ((2147483648L & flags) != 0) {
            int size12 = com.android.internal.util.ArrayUtils.size(pkg.getAttributions());
            if (size12 > 0) {
                info.attributions = new android.content.pm.Attribution[size12];
                int i4 = 0;
                while (i4 < size12) {
                    com.android.internal.pm.pkg.component.ParsedAttribution parsedAttribution = (com.android.internal.pm.pkg.component.ParsedAttribution) pkg.getAttributions().get(i4);
                    if (parsedAttribution == null) {
                        size4 = size12;
                    } else {
                        size4 = size12;
                        info.attributions[i4] = new android.content.pm.Attribution(parsedAttribution.getTag(), parsedAttribution.getLabel());
                    }
                    i4++;
                    size12 = size4;
                }
            }
            if (pkg.isAttributionsUserVisible()) {
                info.applicationInfo.privateFlagsExt |= 4;
            } else {
                info.applicationInfo.privateFlagsExt &= -5;
            }
        } else {
            info.applicationInfo.privateFlagsExt &= -5;
        }
        android.content.pm.SigningDetails signingDetails = pkg.getSigningDetails();
        info.signatures = getDeprecatedSignatures(signingDetails, flags);
        if ((134217728 & flags) != 0) {
            if (signingDetails != android.content.pm.SigningDetails.UNKNOWN) {
                info.signingInfo = new android.content.pm.SigningInfo(signingDetails);
            } else {
                info.signingInfo = null;
            }
        }
        info.isStub = pkg.isStub();
        info.coreApp = pkg.isCoreApp();
        info.isApex = pkg.isApex();
        if (!pkgSetting.hasSharedUser()) {
            info.sharedUserId = null;
            info.sharedUserLabel = 0;
        }
        if ((1 & flags) != 0 && (N2 = pkg.getActivities().size()) > 0) {
            long aflags = flags | 8589934592L;
            android.content.pm.ActivityInfo[] res3 = new android.content.pm.ActivityInfo[N2];
            int num2 = 0;
            int i5 = 0;
            while (true) {
                if (i5 >= N2) {
                    num = num2;
                    res = res3;
                    break;
                }
                if (i5 > 2000) {
                    num = num2;
                    res = res3;
                    break;
                }
                com.android.internal.pm.pkg.component.ParsedActivity a = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getActivities().get(i5);
                if (!com.android.server.pm.pkg.PackageUserStateUtils.isMatch(state, pkgSetting.isSystem(), pkg.isEnabled(), a.isEnabled(), a.isDirectBootAware(), a.getName(), aflags) || android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(a.getName())) {
                    int num3 = num2;
                    i = i5;
                    res2 = res3;
                    N3 = N2;
                    num2 = num3;
                    i5 = i + 1;
                    res3 = res2;
                    N2 = N3;
                } else {
                    i = i5;
                    res2 = res3;
                    N3 = N2;
                    res2[num2] = generateActivityInfo(pkg, a, aflags, state, applicationInfo, userId, pkgSetting);
                    num2++;
                    i5 = i + 1;
                    res3 = res2;
                    N2 = N3;
                }
            }
            info.activities = (android.content.pm.ActivityInfo[]) com.android.internal.util.ArrayUtils.trimToSize(res, num);
        }
        if ((2 & flags) != 0 && (size3 = pkg.getReceivers().size()) > 0) {
            android.content.pm.ActivityInfo[] res4 = new android.content.pm.ActivityInfo[size3];
            int num4 = 0;
            int i6 = 0;
            while (i6 < size3) {
                com.android.internal.pm.pkg.component.ParsedActivity a2 = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getReceivers().get(i6);
                int i7 = i6;
                int size13 = size3;
                int size14 = num4;
                if (!com.android.server.pm.pkg.PackageUserStateUtils.isMatch(state, pkgSetting.isSystem(), pkg.isEnabled(), a2.isEnabled(), a2.isDirectBootAware(), a2.getName(), flags)) {
                    num4 = size14;
                } else {
                    int num5 = size14 + 1;
                    res4[size14] = generateActivityInfo(pkg, a2, flags, state, applicationInfo, userId, pkgSetting);
                    num4 = num5;
                }
                i6 = i7 + 1;
                size3 = size13;
            }
            info.receivers = (android.content.pm.ActivityInfo[]) com.android.internal.util.ArrayUtils.trimToSize(res4, num4);
        }
        if ((4 & flags) != 0 && (size2 = pkg.getServices().size()) > 0) {
            android.content.pm.ServiceInfo[] res5 = new android.content.pm.ServiceInfo[size2];
            int num6 = 0;
            int i8 = 0;
            while (i8 < size2) {
                com.android.internal.pm.pkg.component.ParsedService s = (com.android.internal.pm.pkg.component.ParsedService) pkg.getServices().get(i8);
                int i9 = i8;
                int size15 = size2;
                int size16 = num6;
                if (!com.android.server.pm.pkg.PackageUserStateUtils.isMatch(state, pkgSetting.isSystem(), pkg.isEnabled(), s.isEnabled(), s.isDirectBootAware(), s.getName(), flags)) {
                    num6 = size16;
                } else {
                    int num7 = size16 + 1;
                    res5[size16] = generateServiceInfo(pkg, s, flags, state, applicationInfo, userId, pkgSetting);
                    num6 = num7;
                }
                i8 = i9 + 1;
                size2 = size15;
            }
            info.services = (android.content.pm.ServiceInfo[]) com.android.internal.util.ArrayUtils.trimToSize(res5, num6);
        }
        if ((8 & flags) != 0 && (size = pkg.getProviders().size()) > 0) {
            android.content.pm.ProviderInfo[] res6 = new android.content.pm.ProviderInfo[size];
            int num8 = 0;
            int i10 = 0;
            while (i10 < size) {
                com.android.internal.pm.pkg.component.ParsedProvider pr = (com.android.internal.pm.pkg.component.ParsedProvider) pkg.getProviders().get(i10);
                int i11 = i10;
                int size17 = size;
                int size18 = num8;
                if (!com.android.server.pm.pkg.PackageUserStateUtils.isMatch(state, pkgSetting.isSystem(), pkg.isEnabled(), pr.isEnabled(), pr.isDirectBootAware(), pr.getName(), flags)) {
                    num8 = size18;
                } else {
                    int num9 = size18 + 1;
                    res6[size18] = generateProviderInfo(pkg, pr, flags, state, applicationInfo, userId, pkgSetting);
                    num8 = num9;
                }
                i10 = i11 + 1;
                size = size17;
            }
            info.providers = (android.content.pm.ProviderInfo[]) com.android.internal.util.ArrayUtils.trimToSize(res6, num8);
        }
        if ((16 & flags) != 0 && (N = pkg.getInstrumentations().size()) > 0) {
            info.instrumentation = new android.content.pm.InstrumentationInfo[N];
            for (int i12 = 0; i12 < N; i12++) {
                info.instrumentation[i12] = generateInstrumentationInfo((com.android.internal.pm.pkg.component.ParsedInstrumentation) pkg.getInstrumentations().get(i12), pkg, flags, state, userId, pkgSetting);
            }
        }
        return info;
    }

    public static android.content.pm.Signature[] getDeprecatedSignatures(android.content.pm.SigningDetails signingDetails, long flags) {
        if ((64 & flags) == 0) {
            return null;
        }
        if (signingDetails.hasPastSigningCertificates()) {
            return new android.content.pm.Signature[]{signingDetails.getPastSigningCertificates()[0]};
        }
        if (!signingDetails.hasSignatures()) {
            return null;
        }
        int numberOfSigs = signingDetails.getSignatures().length;
        android.content.pm.Signature[] signatures = new android.content.pm.Signature[numberOfSigs];
        java.lang.System.arraycopy(signingDetails.getSignatures(), 0, signatures, 0, numberOfSigs);
        return signatures;
    }

    private static void updateApplicationInfo(android.content.pm.ApplicationInfo ai, long flags, com.android.server.pm.pkg.PackageUserState state) {
        if ((128 & flags) == 0) {
            ai.metaData = null;
        }
        if ((1024 & flags) == 0) {
            ai.sharedLibraryFiles = null;
            ai.sharedLibraryInfos = null;
        }
        if (!com.android.internal.pm.pkg.parsing.ParsingPackageUtils.sCompatibilityModeEnabled) {
            ai.disableCompatibilityMode();
        }
        ai.flags |= flag(state.isStopped(), 2097152) | flag(state.isInstalled(), 8388608) | flag(state.isSuspended(), 1073741824);
        ai.privateFlags |= flag(state.isInstantApp(), 128) | flag(state.isVirtualPreload(), 65536) | flag(state.isHidden(), 1);
        if (state.getEnabledState() == 1) {
            ai.enabled = true;
        } else if (state.getEnabledState() == 4) {
            ai.enabled = (32768 & flags) != 0;
        } else if (state.getEnabledState() == 2 || state.getEnabledState() == 3) {
            ai.enabled = false;
        }
        ai.enabledSetting = state.getEnabledState();
        if (ai.category == -1) {
            ai.category = android.content.pm.FallbackCategoryProvider.getFallbackCategory(ai.packageName);
        }
        ai.seInfoUser = com.android.server.pm.pkg.SELinuxUtil.getSeinfoUser(state);
        android.content.pm.overlay.OverlayPaths overlayPaths = state.getAllOverlayPaths();
        if (overlayPaths != null) {
            ai.resourceDirs = (java.lang.String[]) overlayPaths.getResourceDirs().toArray(new java.lang.String[0]);
            ai.overlayPaths = (java.lang.String[]) overlayPaths.getOverlayPaths().toArray(new java.lang.String[0]);
        }
        ai.isArchived = com.android.server.pm.PackageArchiver.isArchived(state);
        if (ai.isArchived) {
            ai.nonLocalizedLabel = state.getArchiveState().getActivityInfos().get(0).getTitle();
        }
        if (!state.isInstalled() && !state.dataExists() && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.nullableDataDir()) {
            ai.dataDir = null;
        }
        ((com.android.internal.pm.pkg.parsing.IParsingPackageUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.internal.pm.pkg.parsing.IParsingPackageUtilsExt.class).create()).adjustResultInGenerateApplicationInfoUnchecked(ai, state.getOplusFreezeState(), ai.mApplicationInfoExt);
    }

    public static android.content.pm.ApplicationInfo generateDelegateApplicationInfo(android.content.pm.ApplicationInfo ai, long flags, com.android.server.pm.pkg.PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state, ai)) {
            return null;
        }
        android.content.pm.ApplicationInfo ai2 = new android.content.pm.ApplicationInfo(ai);
        ai2.initForUser(userId);
        ai2.icon = (!com.android.internal.pm.pkg.parsing.ParsingPackageUtils.sUseRoundIcon || ai2.roundIconRes == 0) ? ai2.iconRes : ai2.roundIconRes;
        updateApplicationInfo(ai2, flags, state);
        return ai2;
    }

    public static android.content.pm.ApplicationInfo generateApplicationInfo(com.android.server.pm.pkg.AndroidPackage pkg, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        int index;
        if (pkg != null && checkUseInstalledOrHidden(pkg, pkgSetting, state, flags) && com.android.server.pm.parsing.pkg.AndroidPackageUtils.isMatchForSystemOnly(pkgSetting, flags)) {
            android.content.pm.ApplicationInfo info = com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg);
            updateApplicationInfo(info, flags, state);
            initForUser(info, pkg, userId, state);
            com.android.server.pm.pkg.PackageStateUnserialized pkgState = pkgSetting.getTransientState();
            info.hiddenUntilInstalled = pkgState.isHiddenUntilInstalled();
            java.util.List<java.lang.String> usesLibraryFiles = pkgState.getUsesLibraryFiles();
            java.util.List<com.android.server.pm.pkg.SharedLibraryWrapper> usesLibraries = pkgState.getUsesLibraryInfos();
            java.util.ArrayList<android.content.pm.SharedLibraryInfo> usesLibraryInfos = new java.util.ArrayList<>();
            for (int index2 = 0; index2 < usesLibraries.size(); index2++) {
                usesLibraryInfos.add(usesLibraries.get(index2).getInfo());
            }
            info.sharedLibraryFiles = usesLibraryFiles.isEmpty() ? null : (java.lang.String[]) usesLibraryFiles.toArray(new java.lang.String[0]);
            if (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.sdkLibIndependence()) {
                info.sharedLibraryInfos = usesLibraryInfos.isEmpty() ? null : usesLibraryInfos;
                info.optionalSharedLibraryInfos = null;
            } else {
                info.sharedLibraryInfos = usesLibraryInfos.isEmpty() ? null : usesLibraryInfos;
                java.lang.String[] libsNames = pkgSetting.getUsesSdkLibraries();
                boolean[] libsOptional = pkgSetting.getUsesSdkLibrariesOptional();
                java.util.List<android.content.pm.SharedLibraryInfo> optionalSdkLibraries = null;
                if (!com.android.internal.util.ArrayUtils.isEmpty(libsOptional) && !com.android.internal.util.ArrayUtils.isEmpty(libsNames) && libsNames.length == libsOptional.length) {
                    for (android.content.pm.SharedLibraryInfo info1 : usesLibraryInfos) {
                        if (info1.getType() == 3 && (index = com.android.internal.util.ArrayUtils.indexOf(libsNames, info1.getName())) >= 0 && libsOptional[index]) {
                            if (optionalSdkLibraries == null) {
                                optionalSdkLibraries = new java.util.ArrayList<>();
                            }
                            optionalSdkLibraries.add(info1);
                        }
                    }
                }
                info.optionalSharedLibraryInfos = optionalSdkLibraries;
            }
            if (info.category == -1) {
                info.category = pkgSetting.getCategoryOverride();
            }
            info.seInfo = pkgSetting.getSeInfo();
            info.primaryCpuAbi = pkgSetting.getPrimaryCpuAbi();
            info.secondaryCpuAbi = pkgSetting.getSecondaryCpuAbi();
            info.flags |= appInfoFlags(info.flags, pkgSetting);
            info.privateFlags |= appInfoPrivateFlags(info.privateFlags, pkgSetting);
            info.privateFlagsExt |= appInfoPrivateFlagsExt(info.privateFlagsExt, pkgSetting);
            return info;
        }
        return null;
    }

    public static android.content.pm.ActivityInfo generateActivityInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedActivity a, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return generateActivityInfo(pkg, a, flags, state, null, userId, pkgSetting);
    }

    public static android.content.pm.ActivityInfo generateActivityInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedActivity a, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, android.content.pm.ApplicationInfo applicationInfo, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (a == null || !checkUseInstalledOrHidden(pkg, pkgSetting, state, flags)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = generateApplicationInfo(pkg, flags, state, userId, pkgSetting);
        }
        if (applicationInfo == null) {
            return null;
        }
        android.content.pm.ActivityInfo ai = new android.content.pm.ActivityInfo();
        ai.targetActivity = a.getTargetActivity();
        ai.processName = a.getProcessName();
        ai.exported = a.isExported();
        ai.theme = a.getTheme();
        ai.uiOptions = a.getUiOptions();
        ai.parentActivityName = a.getParentActivityName();
        ai.permission = a.getPermission();
        ai.taskAffinity = a.getTaskAffinity();
        ai.flags = a.getFlags();
        ai.privateFlags = a.getPrivateFlags();
        ai.launchMode = a.getLaunchMode();
        ai.documentLaunchMode = a.getDocumentLaunchMode();
        ai.maxRecents = a.getMaxRecents();
        ai.configChanges = a.getConfigChanges();
        ai.softInputMode = a.getSoftInputMode();
        ai.persistableMode = a.getPersistableMode();
        ai.lockTaskLaunchMode = a.getLockTaskLaunchMode();
        ai.screenOrientation = a.getScreenOrientation();
        ai.resizeMode = a.getResizeMode();
        ai.setMaxAspectRatio(a.getMaxAspectRatio());
        ai.setMinAspectRatio(a.getMinAspectRatio());
        ai.supportsSizeChanges = a.isSupportsSizeChanges();
        ai.requestedVrComponent = a.getRequestedVrComponent();
        ai.rotationAnimation = a.getRotationAnimation();
        ai.colorMode = a.getColorMode();
        ai.windowLayout = a.getWindowLayout();
        ai.attributionTags = a.getAttributionTags();
        if ((128 & flags) != 0) {
            android.os.Bundle metaData = a.getMetaData();
            ai.metaData = metaData.isEmpty() ? null : metaData;
        } else {
            ai.metaData = null;
        }
        ai.applicationInfo = applicationInfo;
        ai.requiredDisplayCategory = a.getRequiredDisplayCategory();
        ai.requireContentUriPermissionFromCaller = a.getRequireContentUriPermissionFromCaller();
        ai.setKnownActivityEmbeddingCerts(a.getKnownActivityEmbeddingCerts());
        assignFieldsComponentInfoParsedMainComponent(ai, a, pkgSetting, userId);
        return ai;
    }

    public static android.content.pm.ActivityInfo generateDelegateActivityInfo(android.content.pm.ActivityInfo a, long flags, com.android.server.pm.pkg.PackageUserState state, int userId) {
        if (a == null || !checkUseInstalledOrHidden(flags, state, a.applicationInfo)) {
            return null;
        }
        android.content.pm.ActivityInfo ai = new android.content.pm.ActivityInfo(a);
        ai.applicationInfo = generateDelegateApplicationInfo(ai.applicationInfo, flags, state, userId);
        return ai;
    }

    public static android.content.pm.ServiceInfo generateServiceInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedService s, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return generateServiceInfo(pkg, s, flags, state, null, userId, pkgSetting);
    }

    public static android.content.pm.ServiceInfo generateServiceInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedService s, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, android.content.pm.ApplicationInfo applicationInfo, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (s == null || !checkUseInstalledOrHidden(pkg, pkgSetting, state, flags)) {
            return null;
        }
        if (applicationInfo == null) {
            applicationInfo = generateApplicationInfo(pkg, flags, state, userId, pkgSetting);
        }
        if (applicationInfo == null) {
            return null;
        }
        android.content.pm.ServiceInfo si = new android.content.pm.ServiceInfo();
        si.exported = s.isExported();
        si.flags = s.getFlags();
        si.permission = s.getPermission();
        si.processName = s.getProcessName();
        si.mForegroundServiceType = s.getForegroundServiceType();
        si.applicationInfo = applicationInfo;
        if ((128 & flags) != 0) {
            android.os.Bundle metaData = s.getMetaData();
            si.metaData = metaData.isEmpty() ? null : metaData;
        }
        assignFieldsComponentInfoParsedMainComponent(si, s, pkgSetting, userId);
        return si;
    }

    public static android.content.pm.ProviderInfo generateProviderInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedProvider p, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, android.content.pm.ApplicationInfo applicationInfo, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (p == null || !checkUseInstalledOrHidden(pkg, pkgSetting, state, flags)) {
            return null;
        }
        if (applicationInfo == null || !pkg.getPackageName().equals(applicationInfo.packageName)) {
            android.util.Slog.wtf(TAG, "AppInfo's package name is different. Expected=" + pkg.getPackageName() + " actual=" + (applicationInfo == null ? "(null AppInfo)" : applicationInfo.packageName));
            applicationInfo = generateApplicationInfo(pkg, flags, state, userId, pkgSetting);
        }
        if (applicationInfo == null) {
            return null;
        }
        android.content.pm.ProviderInfo pi = new android.content.pm.ProviderInfo();
        pi.exported = p.isExported();
        pi.flags = p.getFlags();
        pi.processName = p.getProcessName();
        pi.authority = p.getAuthority();
        pi.isSyncable = p.isSyncable();
        pi.readPermission = p.getReadPermission();
        pi.writePermission = p.getWritePermission();
        pi.grantUriPermissions = p.isGrantUriPermissions();
        pi.forceUriPermissions = p.isForceUriPermissions();
        pi.multiprocess = p.isMultiProcess();
        pi.initOrder = p.getInitOrder();
        pi.uriPermissionPatterns = (android.os.PatternMatcher[]) p.getUriPermissionPatterns().toArray(new android.os.PatternMatcher[0]);
        pi.pathPermissions = (android.content.pm.PathPermission[]) p.getPathPermissions().toArray(new android.content.pm.PathPermission[0]);
        if ((2048 & flags) == 0) {
            pi.uriPermissionPatterns = null;
        }
        if ((128 & flags) != 0) {
            android.os.Bundle metaData = p.getMetaData();
            pi.metaData = metaData.isEmpty() ? null : metaData;
        }
        pi.applicationInfo = applicationInfo;
        assignFieldsComponentInfoParsedMainComponent(pi, p, pkgSetting, userId);
        return pi;
    }

    public static android.content.pm.InstrumentationInfo generateInstrumentationInfo(com.android.internal.pm.pkg.component.ParsedInstrumentation i, com.android.server.pm.pkg.AndroidPackage pkg, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (i == null || !checkUseInstalledOrHidden(pkg, pkgSetting, state, flags)) {
            return null;
        }
        android.content.pm.InstrumentationInfo info = new android.content.pm.InstrumentationInfo();
        info.targetPackage = i.getTargetPackage();
        info.targetProcesses = i.getTargetProcesses();
        info.handleProfiling = i.isHandleProfiling();
        info.functionalTest = i.isFunctionalTest();
        info.sourceDir = pkg.getBaseApkPath();
        info.publicSourceDir = pkg.getBaseApkPath();
        info.splitNames = pkg.getSplitNames();
        info.splitSourceDirs = pkg.getSplitCodePaths().length == 0 ? null : pkg.getSplitCodePaths();
        info.splitPublicSourceDirs = pkg.getSplitCodePaths().length == 0 ? null : pkg.getSplitCodePaths();
        info.splitDependencies = pkg.getSplitDependencies().size() == 0 ? null : pkg.getSplitDependencies();
        initForUser(info, pkg, userId, state);
        info.primaryCpuAbi = pkgSetting.getPrimaryCpuAbi();
        info.secondaryCpuAbi = pkgSetting.getSecondaryCpuAbi();
        info.nativeLibraryDir = pkg.getNativeLibraryDir();
        info.secondaryNativeLibraryDir = pkg.getSecondaryNativeLibraryDir();
        assignFieldsPackageItemInfoParsedComponent(info, i, pkgSetting, userId);
        if ((128 & flags) == 0) {
            info.metaData = null;
        } else {
            android.os.Bundle metaData = i.getMetaData();
            info.metaData = metaData.isEmpty() ? null : metaData;
        }
        return info;
    }

    public static android.content.pm.PermissionInfo generatePermissionInfo(com.android.internal.pm.pkg.component.ParsedPermission p, long flags) {
        if (p == null) {
            return null;
        }
        android.content.pm.PermissionInfo pi = new android.content.pm.PermissionInfo(p.getBackgroundPermission());
        assignFieldsPackageItemInfoParsedComponent(pi, p);
        pi.group = p.getGroup();
        pi.requestRes = p.getRequestRes();
        pi.protectionLevel = p.getProtectionLevel();
        pi.descriptionRes = p.getDescriptionRes();
        pi.flags = p.getFlags();
        pi.knownCerts = p.getKnownCerts();
        if ((128 & flags) == 0) {
            pi.metaData = null;
        } else {
            android.os.Bundle metaData = p.getMetaData();
            pi.metaData = metaData.isEmpty() ? null : metaData;
        }
        return pi;
    }

    public static android.content.pm.PermissionGroupInfo generatePermissionGroupInfo(com.android.internal.pm.pkg.component.ParsedPermissionGroup pg, long flags) {
        if (pg == null) {
            return null;
        }
        android.content.pm.PermissionGroupInfo pgi = new android.content.pm.PermissionGroupInfo(pg.getRequestDetailRes(), pg.getBackgroundRequestRes(), pg.getBackgroundRequestDetailRes());
        assignFieldsPackageItemInfoParsedComponent(pgi, pg);
        pgi.descriptionRes = pg.getDescriptionRes();
        pgi.priority = pg.getPriority();
        pgi.requestRes = pg.getRequestRes();
        pgi.flags = pg.getFlags();
        if ((128 & flags) == 0) {
            pgi.metaData = null;
        } else {
            android.os.Bundle metaData = pg.getMetaData();
            pgi.metaData = metaData.isEmpty() ? null : metaData;
        }
        return pgi;
    }

    public static android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> generateProcessInfo(java.util.Map<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> procs, long flags) {
        if (procs == null) {
            return null;
        }
        int numProcs = procs.size();
        android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> retProcs = new android.util.ArrayMap<>(numProcs);
        for (java.lang.String key : procs.keySet()) {
            com.android.internal.pm.pkg.component.ParsedProcess proc = procs.get(key);
            retProcs.put(proc.getName(), new android.content.pm.ProcessInfo(proc.getName(), new android.util.ArraySet(proc.getDeniedPermissions()), proc.getGwpAsanMode(), proc.getMemtagMode(), proc.getNativeHeapZeroInitialized(), proc.isUseEmbeddedDex()));
        }
        return retProcs;
    }

    public static boolean checkUseInstalledOrHidden(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, com.android.server.pm.pkg.PackageUserStateInternal state, long flags) {
        if ((536870912 & flags) == 0 && !state.isInstalled() && pkgSetting.getTransientState().isHiddenUntilInstalled()) {
            return false;
        }
        return com.android.server.pm.pkg.PackageUserStateUtils.isAvailable(state, flags) || (pkgSetting.isSystem() && matchUninstalledOrHidden(flags));
    }

    private static boolean checkUseInstalledOrHidden(long flags, com.android.server.pm.pkg.PackageUserState state, android.content.pm.ApplicationInfo appInfo) {
        if ((536870912 & flags) != 0 || state.isInstalled() || appInfo == null || !appInfo.hiddenUntilInstalled) {
            return com.android.server.pm.pkg.PackageUserStateUtils.isAvailable(state, flags) || (appInfo != null && appInfo.isSystemApp() && matchUninstalledOrHidden(flags));
        }
        return false;
    }

    private static boolean matchUninstalledOrHidden(long flags) {
        return (4836040704L & flags) != 0;
    }

    private static void assignFieldsComponentInfoParsedMainComponent(android.content.pm.ComponentInfo info, com.android.internal.pm.pkg.component.ParsedMainComponent component) {
        assignFieldsPackageItemInfoParsedComponent(info, component);
        info.descriptionRes = component.getDescriptionRes();
        info.directBootAware = component.isDirectBootAware();
        info.enabled = component.isEnabled();
        info.splitName = component.getSplitName();
        info.attributionTags = component.getAttributionTags();
    }

    private static void assignFieldsPackageItemInfoParsedComponent(android.content.pm.PackageItemInfo packageItemInfo, com.android.internal.pm.pkg.component.ParsedComponent component) {
        packageItemInfo.nonLocalizedLabel = com.android.internal.pm.pkg.component.ComponentParseUtils.getNonLocalizedLabel(component);
        packageItemInfo.icon = com.android.internal.pm.pkg.component.ComponentParseUtils.getIcon(component);
        packageItemInfo.banner = component.getBanner();
        packageItemInfo.labelRes = component.getLabelRes();
        packageItemInfo.logo = component.getLogo();
        packageItemInfo.name = component.getName();
        packageItemInfo.packageName = component.getPackageName();
    }

    private static void assignFieldsComponentInfoParsedMainComponent(android.content.pm.ComponentInfo info, com.android.internal.pm.pkg.component.ParsedMainComponent component, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId) {
        assignFieldsComponentInfoParsedMainComponent(info, component);
        android.util.Pair<java.lang.CharSequence, java.lang.Integer> labelAndIcon = com.android.server.pm.parsing.ParsedComponentStateUtils.getNonLocalizedLabelAndIcon(component, pkgSetting, userId);
        info.nonLocalizedLabel = (java.lang.CharSequence) labelAndIcon.first;
        info.icon = ((java.lang.Integer) labelAndIcon.second).intValue();
    }

    private static void assignFieldsPackageItemInfoParsedComponent(android.content.pm.PackageItemInfo info, com.android.internal.pm.pkg.component.ParsedComponent component, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId) {
        assignFieldsPackageItemInfoParsedComponent(info, component);
        android.util.Pair<java.lang.CharSequence, java.lang.Integer> labelAndIcon = com.android.server.pm.parsing.ParsedComponentStateUtils.getNonLocalizedLabelAndIcon(component, pkgSetting, userId);
        info.nonLocalizedLabel = (java.lang.CharSequence) labelAndIcon.first;
        info.icon = ((java.lang.Integer) labelAndIcon.second).intValue();
    }

    private static int flag(boolean hasFlag, int flag) {
        if (hasFlag) {
            return flag;
        }
        return 0;
    }

    public static int appInfoFlags(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        int pkgWithoutStateFlags = flag(pkg.isExternalStorage(), 262144) | flag(pkg.isHardwareAccelerated(), 536870912) | flag(pkg.isBackupAllowed(), 32768) | flag(pkg.isKillAfterRestoreAllowed(), 65536) | flag(pkg.isRestoreAnyVersion(), 131072) | flag(pkg.isFullBackupOnly(), 67108864) | flag(pkg.isPersistent(), 8) | flag(pkg.isDebuggable(), 2) | flag(pkg.isVmSafeMode(), 16384) | flag(pkg.isDeclaredHavingCode(), 4) | flag(pkg.isTaskReparentingAllowed(), 32) | flag(pkg.isClearUserDataAllowed(), 64) | flag(pkg.isLargeHeap(), 1048576) | flag(pkg.isCleartextTrafficAllowed(), 134217728) | flag(pkg.isRtlSupported(), 4194304) | flag(pkg.isTestOnly(), 256) | flag(pkg.isMultiArch(), Integer.MIN_VALUE) | flag(pkg.isExtractNativeLibrariesRequested(), 268435456) | flag(pkg.isGame(), 33554432) | flag(pkg.isSmallScreensSupported(), 512) | flag(pkg.isNormalScreensSupported(), 1024) | flag(pkg.isLargeScreensSupported(), 2048) | flag(pkg.isExtraLargeScreensSupported(), 524288) | flag(pkg.isResizeable(), 4096) | flag(pkg.isAnyDensity(), 8192) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isSystem(pkg), 1) | flag(pkg.isFactoryTest(), 16);
        return appInfoFlags(pkgWithoutStateFlags, pkgSetting);
    }

    public static int appInfoFlags(int pkgWithoutStateFlags, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (pkgSetting != null) {
            int flags = pkgWithoutStateFlags | flag(pkgSetting.isUpdatedSystemApp(), 128);
            return flags;
        }
        return pkgWithoutStateFlags;
    }

    public static int appInfoPrivateFlags(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        int pkgWithoutStateFlags = flag(pkg.isStaticSharedLibrary(), 16384) | flag(pkg.isResourceOverlay(), 268435456) | flag(pkg.isIsolatedSplitLoading(), 32768) | flag(pkg.isHasDomainUrls(), 16) | flag(pkg.isProfileableByShell(), 8388608) | flag(pkg.isBackupInForeground(), 8192) | flag(pkg.isUseEmbeddedDex(), 33554432) | flag(pkg.isDefaultToDeviceProtectedStorage(), 32) | flag(pkg.isDirectBootAware(), 64) | flag(pkg.isPartiallyDirectBootAware(), 256) | flag(pkg.isClearUserDataOnFailedRestoreAllowed(), 67108864) | flag(pkg.isAllowAudioPlaybackCapture(), 134217728) | flag(pkg.isRequestLegacyExternalStorage(), 536870912) | flag(pkg.isNonSdkApiRequested(), 4194304) | flag(pkg.isUserDataFragile(), 16777216) | flag(pkg.isSaveStateDisallowed(), 2) | flag(pkg.isResizeableActivityViaSdkVersion(), 4096) | flag(pkg.isAllowNativeHeapPointerTagging(), Integer.MIN_VALUE) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isSystemExt(pkg), 2097152) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isPrivileged(pkg), 8) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isOem(pkg), 131072) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isVendor(pkg), 262144) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isProduct(pkg), 524288) | flag(com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isOdm(pkg), 1073741824) | flag(pkg.isSignedWithPlatformKey(), 1048576);
        java.lang.Boolean resizeableActivity = pkg.getResizeableActivity();
        if (resizeableActivity != null) {
            if (resizeableActivity.booleanValue()) {
                pkgWithoutStateFlags |= 1024;
            } else {
                pkgWithoutStateFlags |= 2048;
            }
        }
        return appInfoPrivateFlags(pkgWithoutStateFlags, pkgSetting);
    }

    public static int appInfoPrivateFlags(int pkgWithoutStateFlags, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return pkgWithoutStateFlags;
    }

    public static int appInfoPrivateFlagsExt(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        boolean isAllowlistedForHiddenApis = com.android.server.SystemConfig.getInstance().getHiddenApiWhitelistedApps().contains(pkg.getPackageName());
        int pkgWithoutStateFlags = flag(pkg.isProfileable(), 1) | flag(pkg.hasRequestForegroundServiceExemption(), 2) | flag(pkg.isAttributionsUserVisible(), 4) | flag(pkg.isOnBackInvokedCallbackEnabled(), 8) | flag(isAllowlistedForHiddenApis, 16);
        return appInfoPrivateFlagsExt(pkgWithoutStateFlags, pkgSetting);
    }

    private static int appInfoPrivateFlagsExt(int pkgWithoutStateFlags, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (pkgSetting != null) {
            int flags = pkgWithoutStateFlags | flag(pkgSetting.getCpuAbiOverride() != null, 32);
            return flags;
        }
        return pkgWithoutStateFlags;
    }

    private static void initForUser(android.content.pm.ApplicationInfo output, com.android.server.pm.pkg.AndroidPackage input, int userId, com.android.server.pm.pkg.PackageUserStateInternal state) {
        com.android.internal.pm.parsing.pkg.PackageImpl pkg = (com.android.internal.pm.parsing.pkg.PackageImpl) input;
        java.lang.String packageName = input.getPackageName();
        output.uid = android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(input.getUid()));
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
            output.dataDir = SYSTEM_DATA_PATH;
            return;
        }
        if (!state.isInstalled() && !state.dataExists() && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.nullableDataDir()) {
            output.dataDir = null;
            return;
        }
        if (userId == 0) {
            output.credentialProtectedDataDir = pkg.getBaseAppDataCredentialProtectedDirForSystemUser() + packageName;
            output.deviceProtectedDataDir = pkg.getBaseAppDataDeviceProtectedDirForSystemUser() + packageName;
        } else {
            java.lang.String userIdString = java.lang.String.valueOf(userId);
            int credentialLength = pkg.getBaseAppDataCredentialProtectedDirForSystemUser().length();
            output.credentialProtectedDataDir = new java.lang.StringBuilder(pkg.getBaseAppDataCredentialProtectedDirForSystemUser()).replace(credentialLength - 2, credentialLength - 1, userIdString).append(packageName).toString();
            int deviceLength = pkg.getBaseAppDataDeviceProtectedDirForSystemUser().length();
            output.deviceProtectedDataDir = new java.lang.StringBuilder(pkg.getBaseAppDataDeviceProtectedDirForSystemUser()).replace(deviceLength - 2, deviceLength - 1, userIdString).append(packageName).toString();
        }
        if (input.isDefaultToDeviceProtectedStorage()) {
            output.dataDir = output.deviceProtectedDataDir;
        } else {
            output.dataDir = output.credentialProtectedDataDir;
        }
    }

    private static void initForUser(android.content.pm.InstrumentationInfo output, com.android.server.pm.pkg.AndroidPackage input, int userId, com.android.server.pm.pkg.PackageUserStateInternal state) {
        com.android.internal.pm.parsing.pkg.PackageImpl pkg = (com.android.internal.pm.parsing.pkg.PackageImpl) input;
        java.lang.String packageName = input.getPackageName();
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
            output.dataDir = SYSTEM_DATA_PATH;
            return;
        }
        if (!state.isInstalled() && !state.dataExists() && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.nullableDataDir()) {
            output.dataDir = null;
            return;
        }
        if (userId == 0) {
            output.credentialProtectedDataDir = pkg.getBaseAppDataCredentialProtectedDirForSystemUser() + packageName;
            output.deviceProtectedDataDir = pkg.getBaseAppDataDeviceProtectedDirForSystemUser() + packageName;
        } else {
            java.lang.String userIdString = java.lang.String.valueOf(userId);
            int credentialLength = pkg.getBaseAppDataCredentialProtectedDirForSystemUser().length();
            output.credentialProtectedDataDir = new java.lang.StringBuilder(pkg.getBaseAppDataCredentialProtectedDirForSystemUser()).replace(credentialLength - 2, credentialLength - 1, userIdString).append(packageName).toString();
            int deviceLength = pkg.getBaseAppDataDeviceProtectedDirForSystemUser().length();
            output.deviceProtectedDataDir = new java.lang.StringBuilder(pkg.getBaseAppDataDeviceProtectedDirForSystemUser()).replace(deviceLength - 2, deviceLength - 1, userIdString).append(packageName).toString();
        }
        if (input.isDefaultToDeviceProtectedStorage()) {
            output.dataDir = output.deviceProtectedDataDir;
        } else {
            output.dataDir = output.credentialProtectedDataDir;
        }
    }

    public static java.io.File getDataDir(com.android.server.pm.pkg.PackageStateInternal ps, int userId) {
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(ps.getPackageName())) {
            return android.os.Environment.getDataSystemDirectory();
        }
        if (!ps.getUserStateOrDefault(userId).isInstalled() && !ps.getUserStateOrDefault(userId).dataExists() && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.nullableDataDir()) {
            return null;
        }
        if (ps.isDefaultToDeviceProtectedStorage()) {
            return android.os.Environment.getDataUserDePackageDirectory(ps.getVolumeUuid(), userId, ps.getPackageName());
        }
        return android.os.Environment.getDataUserCePackageDirectory(ps.getVolumeUuid(), userId, ps.getPackageName());
    }

    public static class CachedApplicationInfoGenerator {
        private final android.util.ArrayMap<java.lang.String, android.content.pm.ApplicationInfo> mCache = new android.util.ArrayMap<>();

        public android.content.pm.ApplicationInfo generate(com.android.server.pm.pkg.AndroidPackage pkg, long flags, com.android.server.pm.pkg.PackageUserStateInternal state, int userId, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
            android.content.pm.ApplicationInfo appInfo = this.mCache.get(pkg.getPackageName());
            if (appInfo != null) {
                return appInfo;
            }
            android.content.pm.ApplicationInfo appInfo2 = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(pkg, flags, state, userId, pkgSetting);
            this.mCache.put(pkg.getPackageName(), appInfo2);
            return appInfo2;
        }
    }
}
