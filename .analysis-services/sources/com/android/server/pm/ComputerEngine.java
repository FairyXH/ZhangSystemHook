package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ComputerEngine implements com.android.server.pm.Computer {
    private static final java.util.Comparator<android.content.pm.ProviderInfo> sProviderInitOrderSorter = new java.util.Comparator() { // from class: com.android.server.pm.ComputerEngine$$ExternalSyntheticLambda1
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.pm.ComputerEngine.lambda$static$0((android.content.pm.ProviderInfo) obj, (android.content.pm.ProviderInfo) obj2);
        }
    };
    private final com.android.server.pm.ApexManager mApexManager;
    private final java.lang.String mAppPredictionServicePackage;
    private final com.android.server.pm.AppsFilterSnapshot mAppsFilter;
    private final com.android.server.pm.CompilerStats mCompilerStats;
    private final com.android.server.pm.resolution.ComponentResolverApi mComponentResolver;
    private final android.content.Context mContext;
    private final com.android.server.pm.CrossProfileIntentResolverEngine mCrossProfileIntentResolverEngine;
    private final com.android.server.pm.DefaultAppProvider mDefaultAppProvider;
    private final com.android.server.pm.dex.DexManager mDexManager;
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    private final android.content.pm.PackageManagerInternal.ExternalSourcesPolicy mExternalSourcesPolicy;
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.Integer> mFrozenPackages;
    private final com.android.server.pm.PackageManagerServiceInjector mInjector;
    private final android.content.pm.ResolveInfo mInstantAppInstallerInfo;
    private final com.android.server.pm.InstantAppRegistry mInstantAppRegistry;
    private final com.android.server.pm.InstantAppResolverConnection mInstantAppResolverConnection;
    private final com.android.server.utils.WatchedArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedInstrumentation> mInstrumentation;
    private final com.android.server.utils.WatchedSparseIntArray mIsolatedOwners;
    private final android.content.pm.ApplicationInfo mLocalAndroidApplication;
    private final android.content.pm.ActivityInfo mLocalInstantAppInstallerActivity;
    private final android.content.ComponentName mLocalResolveComponentName;
    private final com.android.server.pm.PackageDexOptimizer mPackageDexOptimizer;
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> mPackages;
    private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManager;
    private final android.content.pm.ActivityInfo mResolveActivity;
    protected final com.android.server.pm.PackageManagerService mService;
    protected final com.android.server.pm.ComputerEngine.Settings mSettings;
    private final com.android.server.pm.SharedLibrariesRead mSharedLibraries;
    private final com.android.server.pm.UserManagerService mUserManager;
    private final int mVersion;
    private final com.android.server.utils.WatchedSparseBooleanArray mWebInstantAppsDisabled;
    private int mUsed = 0;
    private final com.android.server.pm.IComputerEngineWrapper mWrapper = new com.android.server.pm.ComputerEngine.ComputerEngineWrapper();

    protected class Settings {
        private final com.android.server.pm.Settings mSettings;

        public android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getPackages() {
            return this.mSettings.getPackagesLocked().untrackedStorage();
        }

        public android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getDisabledSystemPackages() {
            return this.mSettings.getDisabledSystemPackagesLocked().untrackedStorage();
        }

        public Settings(com.android.server.pm.Settings settings) {
            this.mSettings = settings;
        }

        public com.android.server.pm.pkg.PackageStateInternal getPackage(java.lang.String packageName) {
            return this.mSettings.getPackageLPr(packageName);
        }

        public com.android.server.pm.pkg.PackageStateInternal getDisabledSystemPkg(java.lang.String packageName) {
            return this.mSettings.getDisabledSystemPkgLPr(packageName);
        }

        public boolean isEnabledAndMatch(android.content.pm.ComponentInfo componentInfo, int flags, int userId) {
            com.android.server.pm.pkg.PackageStateInternal pkgState = getPackage(componentInfo.packageName);
            if (pkgState == null) {
                return false;
            }
            return com.android.server.pm.pkg.PackageUserStateUtils.isMatch(pkgState.getUserStateOrDefault(userId), componentInfo, flags);
        }

        public boolean isEnabledAndMatch(com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedMainComponent component, long flags, int userId) {
            com.android.server.pm.pkg.PackageStateInternal pkgState = getPackage(component.getPackageName());
            if (pkgState == null) {
                return false;
            }
            return com.android.server.pm.pkg.PackageUserStateUtils.isMatch(pkgState.getUserStateOrDefault(userId), pkgState.isSystem(), pkg.isEnabled(), component, flags);
        }

        public com.android.server.pm.CrossProfileIntentResolver getCrossProfileIntentResolver(int userId) {
            return this.mSettings.getCrossProfileIntentResolver(userId);
        }

        public com.android.server.pm.SettingBase getSettingBase(int appId) {
            return this.mSettings.getSettingLPr(appId);
        }

        public java.lang.String getRenamedPackageLPr(java.lang.String packageName) {
            return this.mSettings.getRenamedPackageLPr(packageName);
        }

        public com.android.server.pm.PersistentPreferredIntentResolver getPersistentPreferredActivities(int userId) {
            return this.mSettings.getPersistentPreferredActivities(userId);
        }

        public void dumpVersionLPr(com.android.internal.util.IndentingPrintWriter indentingPrintWriter) {
            this.mSettings.dumpVersionLPr(indentingPrintWriter);
        }

        public void dumpPreferred(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
            this.mSettings.dumpPreferred(pw, dumpState, packageName);
        }

        public void writePreferredActivitiesLPr(com.android.modules.utils.TypedXmlSerializer serializer, int userId, boolean full) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
            this.mSettings.writePreferredActivitiesLPr(serializer, userId, full);
        }

        public com.android.server.pm.PreferredIntentResolver getPreferredActivities(int userId) {
            return this.mSettings.getPreferredActivities(userId);
        }

        public com.android.server.pm.SharedUserSetting getSharedUserFromId(java.lang.String name) {
            try {
                return this.mSettings.getSharedUserLPw(name, 0, 0, false);
            } catch (com.android.server.pm.PackageManagerException ignored) {
                throw new java.lang.RuntimeException(ignored);
            }
        }

        public boolean getBlockUninstall(int userId, java.lang.String packageName) {
            return this.mSettings.getBlockUninstallLPr(userId, packageName);
        }

        public int getApplicationEnabledSetting(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
            return this.mSettings.getApplicationEnabledSettingLPr(packageName, userId);
        }

        public int getComponentEnabledSetting(android.content.ComponentName component, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
            return this.mSettings.getComponentEnabledSettingLPr(component, userId);
        }

        public com.android.server.pm.KeySetManagerService getKeySetManagerService() {
            return this.mSettings.getKeySetManagerService();
        }

        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.SharedUserApi> getSharedUsers() {
            return this.mSettings.getSharedUsersLocked().untrackedStorage();
        }

        public com.android.server.pm.pkg.SharedUserApi getSharedUserFromPackageName(java.lang.String packageName) {
            return this.mSettings.getSharedUserSettingLPr(packageName);
        }

        public com.android.server.pm.pkg.SharedUserApi getSharedUserFromAppId(int sharedUserAppId) {
            return (com.android.server.pm.SharedUserSetting) this.mSettings.getSettingLPr(sharedUserAppId);
        }

        public android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> getSharedUserPackages(int sharedUserAppId) {
            android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> res = new android.util.ArraySet<>();
            com.android.server.pm.SharedUserSetting sharedUserSetting = (com.android.server.pm.SharedUserSetting) this.mSettings.getSettingLPr(sharedUserAppId);
            if (sharedUserSetting != null) {
                android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> sharedUserPackages = sharedUserSetting.getPackageStates();
                for (com.android.server.pm.pkg.PackageStateInternal ps : sharedUserPackages) {
                    res.add(ps);
                }
            }
            return res;
        }

        public void dumpPackagesProto(android.util.proto.ProtoOutputStream proto) {
            this.mSettings.dumpPackagesProto(proto);
        }

        public void dumpPermissions(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState) {
            this.mSettings.dumpPermissions(pw, packageName, permissionNames, dumpState);
        }

        public void dumpPackages(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState, boolean checkin) {
            this.mSettings.dumpPackagesLPr(pw, packageName, permissionNames, dumpState, checkin);
        }

        public void dumpKeySet(java.io.PrintWriter pw, java.lang.String packageName, com.android.server.pm.DumpState dumpState) {
            this.mSettings.getKeySetManagerService().dumpLPr(pw, packageName, dumpState);
        }

        public void dumpSharedUsers(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState, boolean checkin) {
            this.mSettings.dumpSharedUsersLPr(pw, packageName, permissionNames, dumpState, checkin);
        }

        public void dumpReadMessages(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
            this.mSettings.dumpReadMessages(pw, dumpState);
        }

        public void dumpSharedUsersProto(android.util.proto.ProtoOutputStream proto) {
            this.mSettings.dumpSharedUsersProto(proto);
        }

        public java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> getVolumePackages(java.lang.String volumeUuid) {
            return this.mSettings.getVolumePackagesLPr(volumeUuid);
        }
    }

    static /* synthetic */ int lambda$static$0(android.content.pm.ProviderInfo p1, android.content.pm.ProviderInfo p2) {
        int v1 = p1.initOrder;
        int v2 = p2.initOrder;
        if (v1 > v2) {
            return -1;
        }
        return v1 < v2 ? 1 : 0;
    }

    private boolean safeMode() {
        return this.mService.getSafeMode();
    }

    protected android.content.ComponentName resolveComponentName() {
        return this.mLocalResolveComponentName;
    }

    protected android.content.pm.ActivityInfo instantAppInstallerActivity() {
        return this.mLocalInstantAppInstallerActivity;
    }

    protected android.content.pm.ApplicationInfo androidApplication() {
        return this.mLocalAndroidApplication;
    }

    ComputerEngine(com.android.server.pm.PackageManagerService.Snapshot args, int version) {
        this.mVersion = version;
        this.mSettings = new com.android.server.pm.ComputerEngine.Settings(args.settings);
        this.mIsolatedOwners = args.isolatedOwners;
        this.mPackages = args.packages;
        this.mSharedLibraries = args.sharedLibraries;
        this.mInstrumentation = args.instrumentation;
        this.mWebInstantAppsDisabled = args.webInstantAppsDisabled;
        this.mLocalResolveComponentName = args.resolveComponentName;
        this.mResolveActivity = args.resolveActivity;
        this.mLocalInstantAppInstallerActivity = args.instantAppInstallerActivity;
        this.mInstantAppInstallerInfo = args.instantAppInstallerInfo;
        this.mInstantAppRegistry = args.instantAppRegistry;
        this.mLocalAndroidApplication = args.androidApplication;
        this.mAppsFilter = args.appsFilter;
        this.mFrozenPackages = args.frozenPackages;
        this.mComponentResolver = args.componentResolver;
        this.mAppPredictionServicePackage = args.appPredictionServicePackage;
        this.mPermissionManager = args.service.mPermissionManager;
        this.mUserManager = args.service.mUserManager;
        this.mContext = args.service.mContext;
        this.mInjector = args.service.mInjector;
        this.mApexManager = args.service.mApexManager;
        this.mInstantAppResolverConnection = args.service.mInstantAppResolverConnection;
        this.mDefaultAppProvider = args.service.getDefaultAppProvider();
        this.mDomainVerificationManager = args.service.mDomainVerificationManager;
        this.mPackageDexOptimizer = args.service.mPackageDexOptimizer;
        this.mDexManager = args.service.getDexManager();
        this.mCompilerStats = args.service.mCompilerStats;
        this.mExternalSourcesPolicy = args.service.mExternalSourcesPolicy;
        this.mCrossProfileIntentResolverEngine = new com.android.server.pm.CrossProfileIntentResolverEngine(this.mUserManager, this.mDomainVerificationManager, this.mDefaultAppProvider, this.mContext);
        this.mService = args.service;
    }

    @Override // com.android.server.pm.Computer
    public int getVersion() {
        return this.mVersion;
    }

    @Override // com.android.server.pm.Computer
    public final com.android.server.pm.Computer use() {
        this.mUsed++;
        return this;
    }

    @Override // com.android.server.pm.Computer
    public final int getUsed() {
        return this.mUsed;
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x0118  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0142 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x014a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x016b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x018d  */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesInternal(android.content.Intent r28, java.lang.String r29, long r30, long r32, int r34, int r35, int r36, boolean r37, boolean r38) {
        /*
            Method dump skipped, instruction units count: 553
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.queryIntentActivitiesInternal(android.content.Intent, java.lang.String, long, long, int, int, int, boolean, boolean):java.util.List");
    }

    @Override // com.android.server.pm.Computer
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesInternal(android.content.Intent intent, java.lang.String resolvedType, long flags, int filterCallingUid, int userId) {
        return queryIntentActivitiesInternal(intent, resolvedType, flags, 0L, filterCallingUid, -1, userId, false, true);
    }

    @Override // com.android.server.pm.Computer
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesInternal(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return queryIntentActivitiesInternal(intent, resolvedType, flags, 0L, android.os.Binder.getCallingUid(), -1, userId, false, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x0109  */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentServicesInternal(android.content.Intent r22, java.lang.String r23, long r24, int r26, int r27, int r28, boolean r29, boolean r30) {
        /*
            Method dump skipped, instruction units count: 311
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.queryIntentServicesInternal(android.content.Intent, java.lang.String, long, int, int, int, boolean, boolean):java.util.List");
    }

    protected java.util.List<android.content.pm.ResolveInfo> queryIntentServicesInternalBody(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int callingUid, java.lang.String instantAppPkgName) {
        java.util.List<android.content.pm.ResolveInfo> resolveInfos;
        java.util.List<android.content.pm.ResolveInfo> resolveInfos2;
        java.lang.String pkgName = intent.getPackage();
        if (pkgName == null) {
            if (this.mService.mPackageManagerServiceExt.hookBeforeQueryIntentServicesInternalBody(userId, callingUid, this, intent)) {
                resolveInfos2 = this.mService.mPackageManagerServiceExt.adjustResultInComputerEngineQISIB(this.mComponentResolver.queryServices(this, intent, resolvedType, flags, userId), this.mComponentResolver.queryServices(this, intent, resolvedType, flags, 0));
            } else {
                resolveInfos2 = this.mComponentResolver.queryServices(this, intent, resolvedType, flags, userId);
            }
            if (resolveInfos2 == null) {
                return java.util.Collections.emptyList();
            }
            return applyPostServiceResolutionFilter(resolveInfos2, instantAppPkgName, userId, callingUid);
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(pkgName);
        if (pkg != null) {
            if (this.mService.mPackageManagerServiceExt.hookBeforeQueryIntentServicesInternalBody(userId, callingUid, this, intent)) {
                resolveInfos = this.mService.mPackageManagerServiceExt.adjustResultInComputerEngineQISIB(this.mComponentResolver.queryServices(this, intent, resolvedType, flags, userId), this.mComponentResolver.queryServices(this, intent, resolvedType, flags, pkg.getServices(), 0));
            } else {
                resolveInfos = this.mComponentResolver.queryServices(this, intent, resolvedType, flags, pkg.getServices(), userId);
            }
            if (resolveInfos == null) {
                return java.util.Collections.emptyList();
            }
            return applyPostServiceResolutionFilter(resolveInfos, instantAppPkgName, userId, callingUid);
        }
        java.util.List<android.content.pm.ResolveInfo> resolveInfos3 = java.util.Collections.emptyList();
        return resolveInfos3;
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.QueryIntentActivitiesResult queryIntentActivitiesInternalBody(android.content.Intent intent, java.lang.String resolvedType, long flags, int filterCallingUid, int userId, boolean resolveForStart, boolean allowDynamicSplits, java.lang.String pkgName, java.lang.String instantAppPkgName) {
        java.util.List<android.content.pm.ResolveInfo> result;
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileResults;
        boolean addInstant;
        java.util.List<android.content.pm.ResolveInfo> result2;
        boolean sortResult;
        java.util.List<android.content.pm.ResolveInfo> queryResult;
        boolean addInstant2 = false;
        java.util.List<android.content.pm.ResolveInfo> result3 = new java.util.ArrayList<>();
        new java.util.ArrayList();
        if (pkgName == null) {
            if (!this.mCrossProfileIntentResolverEngine.shouldSkipCurrentProfile(this, intent, resolvedType, userId) && (queryResult = this.mComponentResolver.queryActivities(this, intent, resolvedType, flags, userId)) != null) {
                result3.addAll(filterIfNotSystemUser(queryResult, userId));
            }
            java.util.List<android.content.pm.ResolveInfo> result4 = this.mService.mPackageManagerServiceExt.adjustResultForNoPkgNameInComputerEngineQIAIB(result3, this, this.mComponentResolver, intent, resolvedType, flags, userId, filterCallingUid);
            boolean addInstant3 = isInstantAppResolutionAllowed(intent, result4, userId, false, flags);
            boolean hasNonNegativePriorityResult = hasNonNegativePriority(result4);
            com.android.server.pm.CrossProfileIntentResolverEngine crossProfileIntentResolverEngine = this.mCrossProfileIntentResolverEngine;
            final com.android.server.pm.ComputerEngine.Settings settings = this.mSettings;
            java.util.Objects.requireNonNull(settings);
            java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileResults2 = crossProfileIntentResolverEngine.resolveIntent(this, intent, resolvedType, userId, flags, pkgName, hasNonNegativePriorityResult, resolveForStart, new java.util.function.Function() { // from class: com.android.server.pm.ComputerEngine$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return settings.getPackage((java.lang.String) obj);
                }
            });
            boolean sortResult2 = intent.hasWebURI() || !crossProfileResults2.isEmpty();
            crossProfileResults = crossProfileResults2;
            sortResult = sortResult2;
            result2 = result4;
            addInstant = addInstant3;
        } else {
            com.android.server.pm.pkg.PackageStateInternal setting = getPackageStateInternal(pkgName, 1000);
            if (setting != null && setting.getAndroidPackage() != null) {
                if (resolveForStart || !shouldFilterApplication(setting, filterCallingUid, userId)) {
                    java.util.List<android.content.pm.ResolveInfo> queryResult2 = this.mComponentResolver.queryActivities(this, intent, resolvedType, flags, setting.getAndroidPackage().getActivities(), userId);
                    if (queryResult2 != null) {
                        result3.addAll(filterIfNotSystemUser(queryResult2, userId));
                    }
                }
            }
            if (result3.size() == 0) {
                java.util.List<android.content.pm.ResolveInfo> result5 = this.mService.mPackageManagerServiceExt.adjustResultForHasPkgNameInComputerEngineQIAIB(result3, this, this.mComponentResolver, intent, resolvedType, flags, userId, filterCallingUid, setting);
                addInstant2 = isInstantAppResolutionAllowed(intent, null, userId, true, flags);
                result = result5;
            } else {
                result = result3;
            }
            com.android.server.pm.CrossProfileIntentResolverEngine crossProfileIntentResolverEngine2 = this.mCrossProfileIntentResolverEngine;
            final com.android.server.pm.ComputerEngine.Settings settings2 = this.mSettings;
            java.util.Objects.requireNonNull(settings2);
            crossProfileResults = crossProfileIntentResolverEngine2.resolveIntent(this, intent, resolvedType, userId, flags, pkgName, false, resolveForStart, new java.util.function.Function() { // from class: com.android.server.pm.ComputerEngine$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return settings2.getPackage((java.lang.String) obj);
                }
            });
            addInstant = addInstant2;
            result2 = result;
            sortResult = false;
        }
        java.util.List<android.content.pm.ResolveInfo> result6 = this.mService.mPackageManagerServiceExt.removeRepeatedResolveInfos(result2, crossProfileResults);
        com.android.server.pm.CrossProfileIntentResolverEngine crossProfileIntentResolverEngine3 = this.mCrossProfileIntentResolverEngine;
        boolean zAreWebInstantAppsDisabled = areWebInstantAppsDisabled(userId);
        final com.android.server.pm.ComputerEngine.Settings settings3 = this.mSettings;
        java.util.Objects.requireNonNull(settings3);
        return crossProfileIntentResolverEngine3.combineFilterAndCreateQueryActivitiesResponse(this, intent, resolvedType, instantAppPkgName, pkgName, allowDynamicSplits, flags, userId, filterCallingUid, resolveForStart, result6, crossProfileResults, zAreWebInstantAppsDisabled, addInstant, sortResult, new java.util.function.Function() { // from class: com.android.server.pm.ComputerEngine$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return settings3.getPackage((java.lang.String) obj);
            }
        });
    }

    private android.content.ComponentName findInstallFailureActivity(java.lang.String packageName, int filterCallingUid, int userId) {
        android.content.Intent failureActivityIntent = new android.content.Intent("android.intent.action.INSTALL_FAILURE");
        failureActivityIntent.setPackage(packageName);
        java.util.List<android.content.pm.ResolveInfo> result = queryIntentActivitiesInternal(failureActivityIntent, null, 0L, 0L, filterCallingUid, -1, userId, false, false);
        int numResults = result.size();
        if (numResults > 0) {
            for (int i = 0; i < numResults; i++) {
                android.content.pm.ResolveInfo info = result.get(i);
                if (info.activityInfo.splitName == null) {
                    return new android.content.ComponentName(packageName, info.activityInfo.name);
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName component, long flags, int userId) {
        return getActivityInfoInternal(component, flags, android.os.Binder.getCallingUid(), userId);
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ActivityInfo getActivityInfoCrossProfile(android.content.ComponentName component, long flags, int userId) {
        if (this.mUserManager.exists(userId)) {
            return getActivityInfoInternalBody(component, updateFlagsForComponent(flags, userId), android.os.Binder.getCallingUid(), userId);
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ActivityInfo getActivityInfoInternal(android.content.ComponentName component, long flags, int filterCallingUid, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        long flags2 = updateFlagsForComponent(flags, userId);
        if (!isRecentsAccessingChildProfiles(android.os.Binder.getCallingUid(), userId)) {
            enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, false, false, "get activity info");
        }
        return getActivityInfoInternalBody(component, flags2, filterCallingUid, userId);
    }

    protected android.content.pm.ActivityInfo getActivityInfoInternalBody(android.content.ComponentName component, long flags, int filterCallingUid, int userId) {
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        com.android.internal.pm.pkg.component.ParsedMainComponent activity = this.mComponentResolver.getActivity(component);
        long flags2 = flags | 8589934592L;
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
            android.util.Log.v("PackageManager", "getActivityInfo " + component + ": " + activity);
        }
        long flags3 = this.mService.mPackageManagerServiceExt.adjustFlagsInComputerEngineGAIIB(flags2);
        if (activity != null) {
            androidPackage = this.mPackages.get(activity.getPackageName());
        } else {
            androidPackage = null;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = androidPackage;
        int userId2 = this.mService.mPackageManagerServiceExt.adjustUserIdInComputerEngineGAIIB(this, userId, activity, this.mService.mSettings, pkg, component, flags3, filterCallingUid);
        if (pkg != null && this.mSettings.isEnabledAndMatch(pkg, activity, flags3, userId2)) {
            com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(component.getPackageName());
            if (ps == null || shouldFilterApplication(ps, filterCallingUid, component, 1, userId2)) {
                return null;
            }
            return com.android.server.pm.parsing.PackageInfoUtils.generateActivityInfo(pkg, activity, flags3, ps.getUserStateOrDefault(userId2), userId2, ps);
        }
        if (!resolveComponentName().equals(component)) {
            return null;
        }
        return com.android.server.pm.parsing.PackageInfoUtils.generateDelegateActivityInfo(this.mResolveActivity, flags3, com.android.server.pm.pkg.PackageUserStateInternal.DEFAULT, userId2);
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.AndroidPackage getPackage(java.lang.String packageName) {
        return this.mPackages.get(resolveInternalPackageName(packageName, -1L));
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.AndroidPackage getPackage(int uid) {
        java.lang.String[] packageNames = getPackagesForUidInternal(uid, 1000);
        com.android.server.pm.pkg.AndroidPackage pkg = null;
        int numPackages = packageNames == null ? 0 : packageNames.length;
        for (int i = 0; pkg == null && i < numPackages; i++) {
            com.android.server.pm.pkg.AndroidPackage pkg2 = this.mPackages.get(packageNames[i]);
            pkg = pkg2;
        }
        return pkg;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ApplicationInfo generateApplicationInfoFromSettings(java.lang.String packageName, long flags, int filterCallingUid, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        if (!this.mUserManager.exists(userId) || (ps = this.mSettings.getPackage(packageName)) == null || filterSharedLibPackage(ps, filterCallingUid, userId, flags) || shouldFilterApplication(ps, filterCallingUid, userId)) {
            return null;
        }
        if (ps.getAndroidPackage() == null) {
            android.content.pm.PackageInfo pInfo = generatePackageInfo(ps, flags, userId);
            if (pInfo != null) {
                return pInfo.applicationInfo;
            }
            return null;
        }
        android.content.pm.ApplicationInfo ai = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(ps.getPkg(), flags, ps.getUserStateOrDefault(userId), userId, ps);
        if (ai != null) {
            ai.packageName = resolveExternalPackageName(ps.getPkg());
        }
        return ai;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, long flags, int userId) {
        return getApplicationInfoInternal(packageName, flags, android.os.Binder.getCallingUid(), userId);
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ApplicationInfo getApplicationInfoInternal(java.lang.String packageName, long flags, int filterCallingUid, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        long flags2 = updateFlagsForApplication(flags, userId);
        if (!isRecentsAccessingChildProfiles(android.os.Binder.getCallingUid(), userId)) {
            enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, false, false, "get application info");
        }
        return getApplicationInfoInternalBody(packageName, flags2, filterCallingUid, userId);
    }

    protected android.content.pm.ApplicationInfo getApplicationInfoInternalBody(java.lang.String packageName, long flags, int filterCallingUid, int userId) {
        java.lang.String packageName2 = resolveInternalPackageName(packageName, -1L);
        com.android.server.pm.pkg.AndroidPackage p = this.mPackages.get(packageName2);
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
            android.util.Log.v("PackageManager", "getApplicationInfo " + packageName2 + ": " + p);
        }
        boolean matchApex = (flags & 1073741824) != 0;
        if (p == null) {
            if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName2) || "system".equals(packageName2)) {
                return androidApplication();
            }
            if ((flags & 4299169792L) == 0) {
                return null;
            }
            return generateApplicationInfoFromSettings(packageName2, flags, filterCallingUid, userId);
        }
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName2);
        if (ps == null) {
            return null;
        }
        if ((!matchApex && p.isApex()) || filterSharedLibPackage(ps, filterCallingUid, userId, flags) || shouldFilterApplication(ps, filterCallingUid, userId)) {
            return null;
        }
        android.content.pm.ApplicationInfo ai = this.mService.mPackageManagerServiceExt.adjustAiInComputerEngineGAppIIB(com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(p, flags, ps.getUserStateOrDefault(userId), userId, ps), p, ps, packageName2, flags, userId);
        if (ai != null) {
            ai.packageName = resolveExternalPackageName(p);
        }
        return ai;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.ComponentName getDefaultHomeActivity(int userId) {
        java.util.List<android.content.pm.ResolveInfo> allHomeCandidates = new java.util.ArrayList<>();
        android.content.ComponentName cn = getHomeActivitiesAsUser(allHomeCandidates, userId);
        if (cn != null) {
            return cn;
        }
        android.util.Slog.w("PackageManager", "Default package for ROLE_HOME is not set in RoleManager");
        int lastPriority = Integer.MIN_VALUE;
        android.content.ComponentName lastComponent = null;
        int size = allHomeCandidates.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.ResolveInfo ri = allHomeCandidates.get(i);
            if (ri.priority > lastPriority) {
                lastComponent = ri.activityInfo.getComponentName();
                lastPriority = ri.priority;
            } else if (ri.priority == lastPriority) {
                lastComponent = null;
            }
        }
        return lastComponent;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x005b  */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.content.ComponentName getHomeActivitiesAsUser(java.util.List<android.content.pm.ResolveInfo> r20, int r21) {
        /*
            r19 = this;
            android.content.Intent r11 = r19.getHomeIntent()
            r2 = 0
            r3 = 128(0x80, double:6.3E-322)
            r0 = r19
            r1 = r11
            r5 = r21
            java.util.List r12 = r0.queryIntentActivitiesInternal(r1, r2, r3, r5)
            r20.clear()
            r13 = 0
            if (r12 != 0) goto L17
            return r13
        L17:
            r14 = r20
            r14.addAll(r12)
            r15 = r19
            com.android.server.pm.DefaultAppProvider r0 = r15.mDefaultAppProvider
            r9 = r21
            java.lang.String r16 = r0.getDefaultHome(r9)
            if (r16 != 0) goto L5b
            int r0 = android.os.Binder.getCallingUid()
            int r8 = android.os.UserHandle.getAppId(r0)
            r0 = 10000(0x2710, float:1.4013E-41)
            if (r8 < r0) goto L36
            r0 = 1
            goto L37
        L36:
            r0 = 0
        L37:
            r10 = r0
            r2 = 0
            r3 = 0
            r6 = 1
            r7 = 0
            r17 = 0
            r0 = r19
            r1 = r11
            r5 = r12
            r18 = r8
            r8 = r17
            r9 = r21
            com.android.server.pm.PackageManagerService$FindPreferredActivityBodyResult r0 = r0.findPreferredActivityInternal(r1, r2, r3, r5, r6, r7, r8, r9, r10)
            android.content.pm.ResolveInfo r1 = r0.mPreferredResolveInfo
            if (r1 == 0) goto L5b
            android.content.pm.ActivityInfo r2 = r1.activityInfo
            if (r2 == 0) goto L5b
            android.content.pm.ActivityInfo r2 = r1.activityInfo
            java.lang.String r2 = r2.packageName
            goto L5d
        L5b:
            r2 = r16
        L5d:
            if (r2 != 0) goto L60
            return r13
        L60:
            int r0 = r12.size()
            r1 = 0
        L65:
            if (r1 >= r0) goto L8c
            java.lang.Object r3 = r12.get(r1)
            android.content.pm.ResolveInfo r3 = (android.content.pm.ResolveInfo) r3
            android.content.pm.ActivityInfo r4 = r3.activityInfo
            if (r4 == 0) goto L89
            android.content.pm.ActivityInfo r4 = r3.activityInfo
            java.lang.String r4 = r4.packageName
            boolean r4 = android.text.TextUtils.equals(r4, r2)
            if (r4 == 0) goto L89
            android.content.ComponentName r4 = new android.content.ComponentName
            android.content.pm.ActivityInfo r5 = r3.activityInfo
            java.lang.String r5 = r5.packageName
            android.content.pm.ActivityInfo r6 = r3.activityInfo
            java.lang.String r6 = r6.name
            r4.<init>(r5, r6)
            return r4
        L89:
            int r1 = r1 + 1
            goto L65
        L8c:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.getHomeActivitiesAsUser(java.util.List, int):android.content.ComponentName");
    }

    @Override // com.android.server.pm.Computer
    public final com.android.server.pm.CrossProfileDomainInfo getCrossProfileDomainPreferredLpr(android.content.Intent intent, java.lang.String resolvedType, long flags, int sourceUserId, int parentUserId) {
        java.util.List<android.content.pm.ResolveInfo> resultTargetUser;
        if (!this.mUserManager.hasUserRestriction("allow_parent_profile_app_linking", sourceUserId) || (resultTargetUser = this.mComponentResolver.queryActivities(this, intent, resolvedType, flags, parentUserId)) == null || resultTargetUser.isEmpty()) {
            return null;
        }
        int size = resultTargetUser.size();
        com.android.server.pm.CrossProfileDomainInfo result = null;
        for (int i = 0; i < size; i++) {
            android.content.pm.ResolveInfo riTargetUser = resultTargetUser.get(i);
            if (!riTargetUser.handleAllWebDataURI) {
                java.lang.String packageName = riTargetUser.activityInfo.packageName;
                com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
                if (ps != null) {
                    int approvalLevel = this.mDomainVerificationManager.approvalLevelForDomain(ps, intent, flags, parentUserId);
                    if (result == null) {
                        result = new com.android.server.pm.CrossProfileDomainInfo(createForwardingResolveInfoUnchecked(new com.android.server.pm.WatchedIntentFilter(), sourceUserId, parentUserId), approvalLevel, parentUserId);
                    } else {
                        result.mHighestApprovalLevel = java.lang.Math.max(approvalLevel, result.mHighestApprovalLevel);
                    }
                }
            }
        }
        if (result == null || result.mHighestApprovalLevel > 0) {
            return result;
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.Intent getHomeIntent() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        return intent;
    }

    @Override // com.android.server.pm.Computer
    public final java.util.List<com.android.server.pm.CrossProfileIntentFilter> getMatchingCrossProfileIntentFilters(android.content.Intent intent, java.lang.String resolvedType, int userId) {
        com.android.server.pm.CrossProfileIntentResolver resolver = this.mSettings.getCrossProfileIntentResolver(userId);
        if (resolver != null) {
            return resolver.queryIntent(this, intent, resolvedType, false, userId);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:63:0x0153  */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<android.content.pm.ResolveInfo> applyPostResolutionFilter(java.util.List<android.content.pm.ResolveInfo> r23, java.lang.String r24, boolean r25, int r26, boolean r27, int r28, android.content.Intent r29) {
        /*
            Method dump skipped, instruction units count: 349
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.applyPostResolutionFilter(java.util.List, java.lang.String, boolean, int, boolean, int, android.content.Intent):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<android.content.pm.ResolveInfo> applyPostServiceResolutionFilter(java.util.List<android.content.pm.ResolveInfo> r12, java.lang.String r13, int r14, int r15) {
        /*
            r11 = this;
            int r0 = r12.size()
            int r0 = r0 + (-1)
        L6:
            if (r0 < 0) goto Lbe
            java.lang.Object r1 = r12.get(r0)
            android.content.pm.ResolveInfo r1 = (android.content.pm.ResolveInfo) r1
            if (r13 != 0) goto L32
            com.android.server.pm.ComputerEngine$Settings r2 = r11.mSettings
            int r3 = android.os.UserHandle.getAppId(r15)
            com.android.server.pm.SettingBase r2 = r2.getSettingBase(r3)
            android.content.pm.ServiceInfo r3 = r1.serviceInfo
            java.lang.String r3 = r3.packageName
            r4 = 0
            com.android.server.pm.pkg.PackageStateInternal r3 = r11.getPackageStateInternal(r3, r4)
            com.android.server.pm.AppsFilterSnapshot r4 = r11.mAppsFilter
            r5 = r11
            r6 = r15
            r7 = r2
            r8 = r3
            r9 = r14
            boolean r4 = r4.shouldFilterApplication(r5, r6, r7, r8, r9)
            if (r4 != 0) goto L32
            goto Lba
        L32:
            android.content.pm.ServiceInfo r2 = r1.serviceInfo
            android.content.pm.ApplicationInfo r2 = r2.applicationInfo
            boolean r2 = r2.isInstantApp()
            if (r2 == 0) goto Lab
            android.content.pm.ServiceInfo r3 = r1.serviceInfo
            java.lang.String r3 = r3.packageName
            boolean r3 = r13.equals(r3)
            if (r3 == 0) goto Lab
            android.content.pm.ServiceInfo r3 = r1.serviceInfo
            java.lang.String r3 = r3.splitName
            if (r3 == 0) goto Lba
            android.content.pm.ServiceInfo r3 = r1.serviceInfo
            android.content.pm.ApplicationInfo r3 = r3.applicationInfo
            java.lang.String[] r3 = r3.splitNames
            android.content.pm.ServiceInfo r4 = r1.serviceInfo
            java.lang.String r4 = r4.splitName
            boolean r3 = com.android.internal.util.ArrayUtils.contains(r3, r4)
            if (r3 != 0) goto Lba
            android.content.pm.ActivityInfo r3 = r11.instantAppInstallerActivity()
            java.lang.String r4 = "PackageManager"
            if (r3 != 0) goto L71
            boolean r3 = com.android.server.pm.PackageManagerService.DEBUG_INSTANT
            if (r3 == 0) goto L6d
            java.lang.String r3 = "No installer - not adding it to the ResolveInfolist"
            android.util.Slog.v(r4, r3)
        L6d:
            r12.remove(r0)
            goto Lba
        L71:
            boolean r3 = com.android.server.pm.PackageManagerService.DEBUG_INSTANT
            if (r3 == 0) goto L7a
            java.lang.String r3 = "Adding ephemeral installer to the ResolveInfo list"
            android.util.Slog.v(r4, r3)
        L7a:
            android.content.pm.ResolveInfo r3 = new android.content.pm.ResolveInfo
            android.content.pm.ResolveInfo r4 = r11.mInstantAppInstallerInfo
            r3.<init>(r4)
            android.content.pm.AuxiliaryResolveInfo r10 = new android.content.pm.AuxiliaryResolveInfo
            android.content.pm.ServiceInfo r4 = r1.serviceInfo
            java.lang.String r6 = r4.packageName
            android.content.pm.ServiceInfo r4 = r1.serviceInfo
            android.content.pm.ApplicationInfo r4 = r4.applicationInfo
            long r7 = r4.longVersionCode
            android.content.pm.ServiceInfo r4 = r1.serviceInfo
            java.lang.String r9 = r4.splitName
            r5 = 0
            r4 = r10
            r4.<init>(r5, r6, r7, r9)
            r3.auxiliaryInfo = r10
            android.content.IntentFilter r4 = new android.content.IntentFilter
            r4.<init>()
            r3.filter = r4
            android.content.pm.ComponentInfo r4 = r1.getComponentInfo()
            java.lang.String r4 = r4.packageName
            r3.resolvePackageName = r4
            r12.set(r0, r3)
            goto Lba
        Lab:
            if (r2 != 0) goto Lb7
            android.content.pm.ServiceInfo r3 = r1.serviceInfo
            int r3 = r3.flags
            r4 = 1048576(0x100000, float:1.469368E-39)
            r3 = r3 & r4
            if (r3 == 0) goto Lb7
            goto Lba
        Lb7:
            r12.remove(r0)
        Lba:
            int r0 = r0 + (-1)
            goto L6
        Lbe:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.applyPostServiceResolutionFilter(java.util.List, java.lang.String, int, int):java.util.List");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.ResolveInfo> filterIfNotSystemUser(java.util.List<android.content.pm.ResolveInfo> resolveInfos, int userId) {
        if (userId == 0) {
            return resolveInfos;
        }
        for (int i = com.android.internal.util.CollectionUtils.size(resolveInfos) - 1; i >= 0; i--) {
            android.content.pm.ResolveInfo info = resolveInfos.get(i);
            if ((info.activityInfo.flags & 536870912) != 0) {
                resolveInfos.remove(i);
            }
        }
        return resolveInfos;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x019c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<android.content.pm.ResolveInfo> maybeAddInstantAppInstaller(java.util.List<android.content.pm.ResolveInfo> r26, android.content.Intent r27, java.lang.String r28, long r29, int r31, boolean r32, boolean r33) throws java.util.concurrent.TimeoutException {
        /*
            Method dump skipped, instruction units count: 434
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.maybeAddInstantAppInstaller(java.util.List, android.content.Intent, java.lang.String, long, int, boolean, boolean):java.util.List");
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.PackageInfo generatePackageInfo(com.android.server.pm.pkg.PackageStateInternal ps, long flags, int userId) {
        long flags2;
        java.util.Set<java.lang.String> installedPermissions;
        java.util.Set<java.lang.String> grantedPermissions;
        java.lang.String apexModuleName;
        if (!this.mUserManager.exists(userId) || ps == null) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (shouldFilterApplication(ps, callingUid, userId)) {
            return null;
        }
        if ((flags & 8192) != 0 && ps.isSystem()) {
            flags2 = flags | 4194304;
        } else {
            flags2 = flags;
        }
        com.android.server.pm.pkg.PackageUserStateInternal state = ps.getUserStateOrDefault(userId);
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        if (pkg != null) {
            int[] gids = (256 & flags2) == 0 ? com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY : this.mPermissionManager.getGidsForUid(android.os.UserHandle.getUid(userId, ps.getAppId()));
            if ((flags2 & 4096) == 0 || com.android.internal.util.ArrayUtils.isEmpty(pkg.getPermissions())) {
                installedPermissions = java.util.Collections.emptySet();
            } else {
                installedPermissions = this.mPermissionManager.getInstalledPermissions(ps.getPackageName());
            }
            if ((4096 & flags2) == 0 || com.android.internal.util.ArrayUtils.isEmpty(pkg.getRequestedPermissions())) {
                grantedPermissions = java.util.Collections.emptySet();
            } else {
                grantedPermissions = this.mPermissionManager.getGrantedPermissions(ps.getPackageName(), userId);
            }
            android.content.pm.PackageInfo packageInfo = com.android.server.pm.parsing.PackageInfoUtils.generate(pkg, gids, flags2, state.getFirstInstallTimeMillis(), ps.getLastUpdateTime(), installedPermissions, grantedPermissions, state, userId, ps);
            if (packageInfo == null) {
                return null;
            }
            android.content.pm.ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            java.lang.String strResolveExternalPackageName = resolveExternalPackageName(pkg);
            applicationInfo.packageName = strResolveExternalPackageName;
            packageInfo.packageName = strResolveExternalPackageName;
            if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.provideInfoOfApkInApex() && (apexModuleName = ps.getApexModuleName()) != null) {
                packageInfo.setApexPackageName(this.mApexManager.getActivePackageNameForApexModuleName(apexModuleName));
            }
            return packageInfo;
        }
        long flags3 = flags2;
        if ((4294975488L & flags3) != 0 && com.android.server.pm.pkg.PackageUserStateUtils.isAvailable(state, flags3)) {
            android.content.pm.PackageInfo pi = new android.content.pm.PackageInfo();
            pi.packageName = ps.getPackageName();
            pi.setLongVersionCode(ps.getVersionCode());
            com.android.server.pm.pkg.SharedUserApi sharedUser = this.mSettings.getSharedUserFromPackageName(pi.packageName);
            pi.sharedUserId = sharedUser != null ? sharedUser.getName() : null;
            pi.firstInstallTime = state.getFirstInstallTimeMillis();
            pi.lastUpdateTime = ps.getLastUpdateTime();
            android.content.pm.ApplicationInfo ai = new android.content.pm.ApplicationInfo();
            ai.packageName = ps.getPackageName();
            ai.uid = android.os.UserHandle.getUid(userId, ps.getAppId());
            ai.primaryCpuAbi = ps.getPrimaryCpuAbiLegacy();
            ai.secondaryCpuAbi = ps.getSecondaryCpuAbiLegacy();
            ai.volumeUuid = ps.getVolumeUuid();
            ai.storageUuid = android.os.storage.StorageManager.convert(ai.volumeUuid);
            ai.setVersionCode(ps.getVersionCode());
            ai.targetSdkVersion = ps.getTargetSdkVersion();
            ai.flags = ps.getFlags();
            ai.privateFlags = ps.getPrivateFlags();
            pi.applicationInfo = com.android.server.pm.parsing.PackageInfoUtils.generateDelegateApplicationInfo(ai, flags3, state, userId);
            pi.signingInfo = ps.getSigningInfo();
            pi.signatures = com.android.server.pm.parsing.PackageInfoUtils.getDeprecatedSignatures(pi.signingInfo.getSigningDetails(), flags3);
            if (state.getArchiveState() != null) {
                pi.setArchiveTimeMillis(state.getArchiveState().getArchiveTimeMillis());
            }
            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
                android.util.Log.v("PackageManager", "ps.pkg is n/a for [" + ps.getPackageName() + "]. Provides a minimum info.");
            }
            return pi;
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, long flags, int userId) {
        return getPackageInfoInternal(packageName, -1L, flags, android.os.Binder.getCallingUid(), userId);
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.PackageInfo getPackageInfoInternal(java.lang.String packageName, long versionCode, long flags, int filterCallingUid, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        long flags2 = updateFlagsForPackage(flags, userId);
        enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, false, false, "get package info");
        return getPackageInfoInternalBody(packageName, versionCode, flags2, filterCallingUid, userId);
    }

    protected android.content.pm.PackageInfo getPackageInfoInternalBody(java.lang.String packageName, long versionCode, long flags, int filterCallingUid, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        java.lang.String packageName2 = resolveInternalPackageName(packageName, versionCode);
        boolean matchFactoryOnly = (2097152 & flags) != 0;
        boolean matchApex = (1073741824 & flags) != 0;
        if (matchFactoryOnly && (ps = this.mSettings.getDisabledSystemPkg(packageName2)) != null) {
            if ((!matchApex && ps.getPkg() != null && ps.getPkg().isApex()) || filterSharedLibPackage(ps, filterCallingUid, userId, flags) || shouldFilterApplication(ps, filterCallingUid, userId)) {
                return null;
            }
            return generatePackageInfo(ps, flags, userId);
        }
        com.android.server.pm.pkg.AndroidPackage p = this.mPackages.get(packageName2);
        com.android.server.pm.pkg.PackageStateInternal packageState = this.mSettings.getPackage(packageName2);
        if (matchFactoryOnly && p != null && !packageState.isSystem()) {
            return null;
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
            android.util.Log.v("PackageManager", "getPackageInfo " + packageName2 + ": " + p);
        }
        if (p != null) {
            com.android.server.pm.pkg.PackageStateInternal ps2 = getPackageStateInternal(p.getPackageName());
            if ((!matchApex && p.isApex()) || filterSharedLibPackage(ps2, filterCallingUid, userId, flags)) {
                return null;
            }
            if (ps2 != null && shouldFilterApplication(ps2, filterCallingUid, userId)) {
                return null;
            }
            return this.mService.mPackageManagerServiceExt.adjustResultForHasPkgInComputerEngineGPIIB(generatePackageInfo(ps2, flags, userId), this, ps2, packageName2, flags, userId, filterCallingUid);
        }
        if (matchFactoryOnly || (4299169792L & flags) == 0) {
            return this.mService.mPackageManagerServiceExt.adjustResultAtEndInComputerEngineGPIIB(null, packageName2);
        }
        com.android.server.pm.pkg.PackageStateInternal ps3 = this.mSettings.getPackage(packageName2);
        if (ps3 == null || filterSharedLibPackage(ps3, filterCallingUid, userId, flags) || shouldFilterApplication(ps3, filterCallingUid, userId)) {
            return null;
        }
        return generatePackageInfo(ps3, flags, userId);
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] getAllAvailablePackageNames() {
        return (java.lang.String[]) this.mPackages.keySet().toArray(new java.lang.String[0]);
    }

    @Override // com.android.server.pm.Computer
    public final com.android.server.pm.pkg.PackageStateInternal getPackageStateInternal(java.lang.String packageName) {
        return getPackageStateInternal(packageName, android.os.Binder.getCallingUid());
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.PackageStateInternal getPackageStateInternal(java.lang.String packageName, int callingUid) {
        return this.mSettings.getPackage(resolveInternalPackageNameInternalLocked(packageName, -1L, callingUid));
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.PackageStateInternal getPackageStateFiltered(java.lang.String packageName, int callingUid, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = this.mSettings.getPackage(resolveInternalPackageNameInternalLocked(packageName, -1L, callingUid));
        if (shouldFilterApplication(packageState, callingUid, userId)) {
            return null;
        }
        return packageState;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getInstalledPackages(long flags, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        long flags2 = this.mService.mPackageManagerServiceExt.adjustFlagsInGetInstalledPackages(this, flags, userId);
        if (!this.mUserManager.exists(userId)) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        long flags3 = updateFlagsForPackage(flags2, userId);
        enforceCrossUserPermission(callingUid, userId, false, false, "get installed packages");
        return getInstalledPackagesBody(flags3, userId, callingUid);
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0177 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0174 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x00be A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x00b8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getInstalledPackagesBody(long r21, int r23, int r24) {
        /*
            Method dump skipped, instruction units count: 437
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.getInstalledPackagesBody(long, int, int):android.content.pm.ParceledListSlice");
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ResolveInfo createForwardingResolveInfoUnchecked(com.android.server.pm.WatchedIntentFilter filter, int sourceUserId, int targetUserId) {
        java.lang.String className;
        android.content.pm.ResolveInfo forwardingResolveInfo = new android.content.pm.ResolveInfo();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            boolean targetIsProfile = this.mUserManager.getUserInfo(targetUserId).isManagedProfile();
            if (targetIsProfile) {
                className = com.android.internal.app.IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE;
            } else {
                className = com.android.internal.app.IntentForwarderActivity.FORWARD_INTENT_TO_PARENT;
            }
            android.content.ComponentName forwardingActivityComponentName = new android.content.ComponentName(androidApplication().packageName, className);
            android.content.pm.ActivityInfo forwardingActivityInfo = getActivityInfoCrossProfile(forwardingActivityComponentName, 0L, sourceUserId);
            if (!targetIsProfile) {
                forwardingActivityInfo.showUserIcon = targetUserId;
                forwardingResolveInfo.noResourceId = true;
            }
            forwardingResolveInfo.activityInfo = forwardingActivityInfo;
            forwardingResolveInfo.priority = 0;
            forwardingResolveInfo.preferredOrder = 0;
            forwardingResolveInfo.match = 0;
            forwardingResolveInfo.isDefault = true;
            forwardingResolveInfo.filter = new android.content.IntentFilter(filter.getIntentFilter());
            forwardingResolveInfo.targetUserId = targetUserId;
            forwardingResolveInfo.userHandle = android.os.UserHandle.of(sourceUserId);
            return forwardingResolveInfo;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName component, long flags, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        long flags2 = updateFlagsForComponent(flags, userId);
        enforceCrossUserOrProfilePermission(callingUid, userId, false, false, "get service info");
        return getServiceInfoBody(component, flags2, userId, callingUid);
    }

    protected android.content.pm.ServiceInfo getServiceInfoBody(android.content.ComponentName component, long flags, int userId, int callingUid) {
        com.android.internal.pm.pkg.component.ParsedService s = this.mComponentResolver.getService(component);
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
            android.util.Log.v("PackageManager", "getServiceInfo " + component + ": " + s);
        }
        if (s == null) {
            return null;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(s.getPackageName());
        if (!this.mSettings.isEnabledAndMatch(pkg, s, flags, userId)) {
            return this.mService.mPackageManagerServiceExt.adjustResultAtEndInComputerEngineGSIB(null, this, this.mService.mSettings, pkg, s, component, flags, userId, callingUid);
        }
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(component.getPackageName());
        if (ps == null || shouldFilterApplication(ps, callingUid, component, 3, userId)) {
            return null;
        }
        return com.android.server.pm.parsing.PackageInfoUtils.generateServiceInfo(pkg, s, flags, ps.getUserStateOrDefault(userId), userId, ps);
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.SharedLibraryInfo getSharedLibraryInfo(java.lang.String name, long version) {
        return this.mSharedLibraries.getSharedLibraryInfo(name, version);
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String getInstantAppPackageName(int callingUid) {
        if (android.os.Process.isIsolated(callingUid)) {
            callingUid = getIsolatedOwner(callingUid);
        }
        int appId = android.os.UserHandle.getAppId(callingUid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (!(obj instanceof com.android.server.pm.pkg.PackageStateInternal)) {
            return null;
        }
        com.android.server.pm.pkg.PackageStateInternal ps = (com.android.server.pm.pkg.PackageStateInternal) obj;
        boolean isInstantApp = ps.getUserStateOrDefault(android.os.UserHandle.getUserId(callingUid)).isInstantApp();
        if (isInstantApp) {
            return ps.getPkg().getPackageName();
        }
        return null;
    }

    private int getIsolatedOwner(int isolatedUid) {
        int ownerUid = this.mIsolatedOwners.get(isolatedUid, -1);
        if (ownerUid == -1) {
            throw new java.lang.IllegalStateException("No owner UID found for isolated UID " + isolatedUid);
        }
        return ownerUid;
    }

    @Override // com.android.server.pm.Computer
    public final java.lang.String resolveExternalPackageName(com.android.server.pm.pkg.AndroidPackage pkg) {
        if (pkg.getStaticSharedLibraryName() != null) {
            return pkg.getManifestPackageName();
        }
        return pkg.getPackageName();
    }

    private java.lang.String resolveInternalPackageNameInternalLocked(java.lang.String packageName, long versionCode, int callingUid) {
        java.lang.String normalizedPackageName = this.mSettings.getRenamedPackageLPr(packageName);
        java.lang.String packageName2 = normalizedPackageName != null ? normalizedPackageName : packageName;
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.getStaticLibraryInfos(packageName2);
        if (versionedLib == null || versionedLib.size() <= 0) {
            return packageName2;
        }
        android.util.LongSparseLongArray versionsCallerCanSee = null;
        int callingAppId = android.os.UserHandle.getAppId(callingUid);
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingAppId)) {
            versionsCallerCanSee = new android.util.LongSparseLongArray();
            java.lang.String libName = versionedLib.valueAt(0).getName();
            java.lang.String[] uidPackages = getPackagesForUidInternal(callingUid, callingUid);
            if (uidPackages != null) {
                for (java.lang.String uidPackage : uidPackages) {
                    com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(uidPackage);
                    int libIdx = com.android.internal.util.ArrayUtils.indexOf(ps.getUsesStaticLibraries(), libName);
                    if (libIdx >= 0) {
                        long libVersion = ps.getUsesStaticLibrariesVersions()[libIdx];
                        versionsCallerCanSee.append(libVersion, libVersion);
                    }
                }
            }
        }
        if (versionsCallerCanSee != null && versionsCallerCanSee.size() <= 0) {
            return packageName2;
        }
        android.content.pm.SharedLibraryInfo highestVersion = null;
        int versionCount = versionedLib.size();
        for (int i = 0; i < versionCount; i++) {
            android.content.pm.SharedLibraryInfo libraryInfo = versionedLib.valueAt(i);
            if (versionsCallerCanSee == null || versionsCallerCanSee.indexOfKey(libraryInfo.getLongVersion()) >= 0) {
                long libVersionCode = libraryInfo.getDeclaringPackage().getLongVersionCode();
                if (versionCode != -1) {
                    if (libVersionCode == versionCode) {
                        return libraryInfo.getPackageName();
                    }
                } else if (highestVersion == null) {
                    highestVersion = libraryInfo;
                } else if (libVersionCode > highestVersion.getDeclaringPackage().getLongVersionCode()) {
                    highestVersion = libraryInfo;
                }
            }
        }
        if (highestVersion != null) {
            return highestVersion.getPackageName();
        }
        return packageName2;
    }

    @Override // com.android.server.pm.Computer
    public final java.lang.String resolveInternalPackageName(java.lang.String packageName, long versionCode) {
        int callingUid = android.os.Binder.getCallingUid();
        return resolveInternalPackageNameInternalLocked(packageName, versionCode, callingUid);
    }

    @Override // com.android.server.pm.Computer
    public final java.lang.String[] getPackagesForUid(int uid) {
        return getPackagesForUidInternal(uid, android.os.Binder.getCallingUid());
    }

    private java.lang.String[] getPackagesForUidInternal(int uid, int callingUid) {
        boolean isCallerInstantApp = getInstantAppPackageName(callingUid) != null;
        int userId = android.os.UserHandle.getUserId(uid);
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int appId = android.os.UserHandle.getAppId(uid);
        return getPackagesForUidInternalBody(callingUid, userId, appId, isCallerInstantApp);
    }

    protected java.lang.String[] getPackagesForUidInternalBody(int callingUid, int userId, int appId, boolean isCallerInstantApp) {
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj instanceof com.android.server.pm.SharedUserSetting) {
            if (isCallerInstantApp) {
                return null;
            }
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
            android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = sus.getPackageStates();
            int n = packageStates.size();
            java.lang.String[] res = new java.lang.String[n];
            int i = 0;
            for (int index = 0; index < n; index++) {
                com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(index);
                if (ps.getUserStateOrDefault(userId).isInstalled() && !shouldFilterApplication(ps, callingUid, userId)) {
                    res[i] = ps.getPackageName();
                    i++;
                }
            }
            return (java.lang.String[]) com.android.internal.util.ArrayUtils.trimToSize(res, i);
        }
        if (obj instanceof com.android.server.pm.pkg.PackageStateInternal) {
            com.android.server.pm.pkg.PackageStateInternal ps2 = (com.android.server.pm.pkg.PackageStateInternal) obj;
            if (ps2.getUserStateOrDefault(userId).isInstalled() && !shouldFilterApplication(ps2, callingUid, userId)) {
                return new java.lang.String[]{ps2.getPackageName()};
            }
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.UserInfo getProfileParent(int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mUserManager.getProfileParent(userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private boolean areWebInstantAppsDisabled(int userId) {
        return this.mWebInstantAppsDisabled.get(userId);
    }

    @Override // com.android.server.pm.Computer
    public final boolean canViewInstantApps(int callingUid, int userId) {
        if (callingUid < 10000 || this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_INSTANT_APPS") == 0) {
            return true;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.VIEW_INSTANT_APPS") != 0) {
            return false;
        }
        android.content.ComponentName homeComponent = getDefaultHomeActivity(userId);
        if (homeComponent == null || !isCallerSameApp(homeComponent.getPackageName(), callingUid)) {
            return this.mAppPredictionServicePackage != null && isCallerSameApp(this.mAppPredictionServicePackage, callingUid);
        }
        return true;
    }

    private boolean filterStaticSharedLibPackage(com.android.server.pm.pkg.PackageStateInternal ps, int uid, int userId, long flags) {
        android.content.pm.SharedLibraryInfo libraryInfo;
        int index;
        if ((flags & 67108864) != 0) {
            int appId = android.os.UserHandle.getAppId(uid);
            if (com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(appId) || checkUidPermission("android.permission.INSTALL_PACKAGES", uid) == 0) {
                return false;
            }
        }
        if (ps == null || ps.getPkg() == null || !ps.getPkg().isStaticSharedLibrary() || (libraryInfo = getSharedLibraryInfo(ps.getPkg().getStaticSharedLibraryName(), ps.getPkg().getStaticSharedLibraryVersion())) == null) {
            return false;
        }
        int resolvedUid = android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid));
        java.lang.String[] uidPackageNames = getPackagesForUid(resolvedUid);
        if (uidPackageNames == null) {
            return true;
        }
        for (java.lang.String uidPackageName : uidPackageNames) {
            if (ps.getPackageName().equals(uidPackageName)) {
                return false;
            }
            com.android.server.pm.pkg.PackageStateInternal uidPs = this.mSettings.getPackage(uidPackageName);
            if (uidPs != null && (index = com.android.internal.util.ArrayUtils.indexOf(uidPs.getUsesStaticLibraries(), libraryInfo.getName())) >= 0 && uidPs.getPkg().getUsesStaticLibrariesVersions()[index] == libraryInfo.getLongVersion()) {
                return false;
            }
        }
        return true;
    }

    private boolean filterSdkLibPackage(com.android.server.pm.pkg.PackageStateInternal ps, int uid, int userId, long flags) {
        android.content.pm.SharedLibraryInfo libraryInfo;
        int index;
        if ((flags & 67108864) != 0) {
            int appId = android.os.UserHandle.getAppId(uid);
            if (com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(appId) || checkUidPermission("android.permission.INSTALL_PACKAGES", uid) == 0) {
                return false;
            }
        }
        if (ps == null || ps.getPkg() == null || !ps.getPkg().isSdkLibrary() || (libraryInfo = getSharedLibraryInfo(ps.getPkg().getSdkLibraryName(), ps.getPkg().getSdkLibVersionMajor())) == null) {
            return false;
        }
        int resolvedUid = android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid));
        java.lang.String[] uidPackageNames = getPackagesForUid(resolvedUid);
        if (uidPackageNames == null) {
            return true;
        }
        for (java.lang.String uidPackageName : uidPackageNames) {
            if (ps.getPackageName().equals(uidPackageName)) {
                return false;
            }
            com.android.server.pm.pkg.PackageStateInternal uidPs = this.mSettings.getPackage(uidPackageName);
            if (uidPs != null && (index = com.android.internal.util.ArrayUtils.indexOf(uidPs.getUsesSdkLibraries(), libraryInfo.getName())) >= 0 && uidPs.getPkg().getUsesSdkLibrariesVersionsMajor()[index] == libraryInfo.getLongVersion()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.server.pm.Computer
    public final boolean filterSharedLibPackage(com.android.server.pm.pkg.PackageStateInternal ps, int uid, int userId, long flags) {
        return filterStaticSharedLibPackage(ps, uid, userId, flags) || filterSdkLibPackage(ps, uid, userId, flags);
    }

    private boolean hasCrossUserPermission(int callingUid, int callingUserId, int userId, boolean requireFullPermission, boolean requirePermissionWhenSameUser) {
        boolean permissionGranted = true;
        if ((!requirePermissionWhenSameUser && userId == callingUserId) || com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingUid) || this.mService.mPackageManagerServiceExt.adjustCrossUserPermission(callingUserId, userId)) {
            return true;
        }
        if (requireFullPermission) {
            permissionGranted = hasPermission("android.permission.INTERACT_ACROSS_USERS_FULL");
        } else if (!hasPermission("android.permission.INTERACT_ACROSS_USERS_FULL") && !hasPermission("android.permission.INTERACT_ACROSS_USERS")) {
            permissionGranted = false;
        }
        if (!permissionGranted && android.os.Process.isIsolatedUid(callingUid) && isKnownIsolatedComputeApp(callingUid)) {
            return checkIsolatedOwnerHasPermission(callingUid, requireFullPermission);
        }
        return permissionGranted;
    }

    private boolean hasNonNegativePriority(java.util.List<android.content.pm.ResolveInfo> resolveInfos) {
        return resolveInfos.size() > 0 && resolveInfos.get(0).priority >= 0;
    }

    private boolean hasPermission(java.lang.String permission) {
        return this.mContext.checkCallingOrSelfPermission(permission) == 0;
    }

    private boolean checkIsolatedOwnerHasPermission(int callingUid, boolean requireFullPermission) {
        getIsolatedOwner(callingUid);
        if (requireFullPermission) {
            return hasPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid);
        }
        return hasPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) || hasPermission("android.permission.INTERACT_ACROSS_USERS", callingUid);
    }

    private boolean hasPermission(java.lang.String permission, int uid) {
        return this.mContext.checkPermission(permission, -1, uid) == 0;
    }

    @Override // com.android.server.pm.Computer
    public final boolean isCallerSameApp(java.lang.String packageName, int uid) {
        return isCallerSameApp(packageName, uid, false);
    }

    @Override // com.android.server.pm.Computer
    public final boolean isCallerSameApp(java.lang.String packageName, int uid, boolean resolveIsolatedUid) {
        if (android.os.Process.isSdkSandboxUid(uid)) {
            return packageName != null && packageName.equals(this.mService.getSdkSandboxPackageName());
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        if (resolveIsolatedUid && android.os.Process.isIsolated(uid)) {
            uid = getIsolatedOwner(uid);
        }
        return pkg != null && android.os.UserHandle.getAppId(uid) == pkg.getUid();
    }

    private boolean isCallerFromManagedUserOrProfile(int userId) {
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) this.mInjector.getLocalService(android.app.admin.DevicePolicyManagerInternal.class);
        return dpmi != null && dpmi.isUserOrganizationManaged(userId);
    }

    @Override // com.android.server.pm.Computer
    public final boolean isComponentVisibleToInstantApp(android.content.ComponentName component) {
        if (isComponentVisibleToInstantApp(component, 1) || isComponentVisibleToInstantApp(component, 3)) {
            return true;
        }
        return isComponentVisibleToInstantApp(component, 4);
    }

    @Override // com.android.server.pm.Computer
    public final boolean isComponentVisibleToInstantApp(android.content.ComponentName component, int type) {
        if (type == 1) {
            com.android.internal.pm.pkg.component.ParsedActivity activity = this.mComponentResolver.getActivity(component);
            if (activity == null) {
                return false;
            }
            boolean visibleToInstantApp = (1048576 & activity.getFlags()) != 0;
            boolean explicitlyVisibleToInstantApp = (2097152 & activity.getFlags()) == 0;
            return visibleToInstantApp && explicitlyVisibleToInstantApp;
        }
        if (type == 2) {
            com.android.internal.pm.pkg.component.ParsedActivity activity2 = this.mComponentResolver.getReceiver(component);
            if (activity2 == null) {
                return false;
            }
            boolean visibleToInstantApp2 = (1048576 & activity2.getFlags()) != 0;
            boolean explicitlyVisibleToInstantApp2 = (2097152 & activity2.getFlags()) == 0;
            return visibleToInstantApp2 && !explicitlyVisibleToInstantApp2;
        }
        if (type == 3) {
            com.android.internal.pm.pkg.component.ParsedService service = this.mComponentResolver.getService(component);
            return (service == null || (1048576 & service.getFlags()) == 0) ? false : true;
        }
        if (type == 4) {
            com.android.internal.pm.pkg.component.ParsedProvider provider = this.mComponentResolver.getProvider(component);
            return (provider == null || (1048576 & provider.getFlags()) == 0) ? false : true;
        }
        if (type == 0) {
            return isComponentVisibleToInstantApp(component);
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public final boolean isImplicitImageCaptureIntentAndNotSetByDpc(android.content.Intent intent, int userId, java.lang.String resolvedType, long flags) {
        return intent.isImplicitImageCaptureIntent() && !isPersistentPreferredActivitySetByDpm(intent, userId, resolvedType, flags);
    }

    @Override // com.android.server.pm.Computer
    public final boolean isInstantApp(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, true, false, "isInstantApp");
        return isInstantAppInternal(packageName, userId, callingUid);
    }

    @Override // com.android.server.pm.Computer
    public final boolean isInstantAppInternal(java.lang.String packageName, int userId, int callingUid) {
        return isInstantAppInternalBody(packageName, userId, callingUid);
    }

    protected boolean isInstantAppInternalBody(java.lang.String packageName, int userId, int callingUid) {
        if (android.os.Process.isIsolated(callingUid)) {
            callingUid = getIsolatedOwner(callingUid);
        }
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
        boolean returnAllowed = ps != null && (isCallerSameApp(packageName, callingUid) || canViewInstantApps(callingUid, userId) || this.mInstantAppRegistry.isInstantAccessGranted(userId, android.os.UserHandle.getAppId(callingUid), ps.getAppId()));
        if (!returnAllowed) {
            return false;
        }
        return ps.getUserStateOrDefault(userId).isInstantApp();
    }

    private boolean isInstantAppResolutionAllowed(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> resolvedActivities, int userId, boolean skipPackageCheck, long flags) {
        if (this.mInstantAppResolverConnection == null || instantAppInstallerActivity() == null || intent.getComponent() != null || (intent.getFlags() & Integer.MIN_VALUE) != 0 || (intent.getFlags() & 1024) != 0) {
            return false;
        }
        if (!skipPackageCheck && intent.getPackage() != null) {
            return false;
        }
        if (!intent.isWebIntent()) {
            if ((resolvedActivities != null && resolvedActivities.size() != 0) || (intent.getFlags() & 2048) == 0) {
                return false;
            }
        } else if (intent.getData() == null || android.text.TextUtils.isEmpty(intent.getData().getHost()) || areWebInstantAppsDisabled(userId)) {
            return false;
        }
        return isInstantAppResolutionAllowedBody(intent, resolvedActivities, userId, skipPackageCheck, flags);
    }

    protected boolean isInstantAppResolutionAllowedBody(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> resolvedActivities, int userId, boolean skipPackageCheck, long flags) {
        int count = resolvedActivities == null ? 0 : resolvedActivities.size();
        boolean debug = (intent.getFlags() & 8) != 0;
        if (debug) {
            android.util.Slog.d("PackageManager", "Checking if instant app resolution allowed, resolvedActivities = " + resolvedActivities);
        }
        for (int n = 0; n < count; n++) {
            android.content.pm.ResolveInfo info = resolvedActivities.get(n);
            java.lang.String packageName = info.activityInfo.packageName;
            com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
            if (ps != null) {
                if (!info.handleAllWebDataURI && com.android.server.pm.PackageManagerServiceUtils.hasAnyDomainApproval(this.mDomainVerificationManager, ps, intent, flags, userId)) {
                    if (com.android.server.pm.PackageManagerService.DEBUG_INSTANT) {
                        android.util.Slog.v("PackageManager", "DENY instant app; pkg: " + packageName + ", approved");
                    }
                    return false;
                }
                if (ps.getUserStateOrDefault(userId).isInstantApp()) {
                    if (com.android.server.pm.PackageManagerService.DEBUG_INSTANT) {
                        android.util.Slog.v("PackageManager", "DENY instant app installed; pkg: " + packageName);
                    }
                    return false;
                }
            } else if (debug) {
                android.util.Slog.d("PackageManager", "Could not find package " + packageName);
            }
        }
        return true;
    }

    private boolean isPersistentPreferredActivitySetByDpm(android.content.Intent intent, int userId, java.lang.String resolvedType, long flags) {
        java.util.List<com.android.server.pm.PersistentPreferredActivity> arrayList;
        com.android.server.pm.PersistentPreferredIntentResolver ppir = this.mSettings.getPersistentPreferredActivities(userId);
        if (ppir != null) {
            arrayList = ppir.queryIntent(this, intent, resolvedType, (65536 & flags) != 0, userId);
        } else {
            arrayList = new java.util.ArrayList();
        }
        for (com.android.server.pm.PersistentPreferredActivity ppa : arrayList) {
            if (ppa.mIsSetByDpm) {
                return true;
            }
        }
        return false;
    }

    private boolean isRecentsAccessingChildProfiles(int callingUid, int targetUserId) {
        if (!((com.android.server.wm.ActivityTaskManagerInternal) this.mInjector.getLocalService(com.android.server.wm.ActivityTaskManagerInternal.class)).isCallerRecents(callingUid)) {
            return false;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            if (android.app.ActivityManager.getCurrentUser() != callingUserId) {
                return false;
            }
            return this.mUserManager.isSameProfileGroup(callingUserId, targetUserId);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    @Override // com.android.server.pm.Computer
    public final boolean isSameProfileGroup(int callerUserId, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return com.android.server.pm.UserManagerService.getInstance().isSameProfileGroup(callerUserId, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public final boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal ps, int callingUid, android.content.ComponentName component, int componentType, int userId, boolean filterUninstall, boolean filterArchived) {
        int callingUid2;
        if (android.os.Process.isSdkSandboxUid(callingUid)) {
            int clientAppUid = android.os.Process.getAppUidForSdkSandboxUid(callingUid);
            if (ps != null && clientAppUid == android.os.UserHandle.getUid(userId, ps.getAppId())) {
                return false;
            }
        }
        if (android.os.Process.isIsolated(callingUid)) {
            callingUid2 = getIsolatedOwner(callingUid);
        } else {
            callingUid2 = callingUid;
        }
        java.lang.String instantAppPkgName = getInstantAppPackageName(callingUid2);
        boolean callerIsInstantApp = instantAppPkgName != null;
        boolean packageArchivedForUser = ps != null && com.android.server.pm.PackageArchiver.isArchived(ps.getUserStateOrDefault(userId));
        boolean filterUninstall2 = this.mService.mPackageManagerServiceExt.adjustFilterUninstallForMultiApp(filterUninstall, userId, ps);
        if (ps == null || !(!filterUninstall2 || com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid2) || ps.isHiddenUntilInstalled() || ps.getUserStateOrDefault(userId).isInstalled() || (packageArchivedForUser && !filterArchived))) {
            return callerIsInstantApp || filterUninstall2 || android.os.Process.isSdkSandboxUid(callingUid2);
        }
        if (isCallerSameApp(ps.getPackageName(), callingUid2)) {
            return false;
        }
        if (callerIsInstantApp) {
            if (ps.getUserStateOrDefault(userId).isInstantApp()) {
                return true;
            }
            if (component == null) {
                return true ^ ps.getPkg().isVisibleToInstantApps();
            }
            com.android.internal.pm.pkg.component.ParsedInstrumentation instrumentation = this.mInstrumentation.get(component);
            if (instrumentation == null || !isCallerSameApp(instrumentation.getTargetPackage(), callingUid2)) {
                return true ^ isComponentVisibleToInstantApp(component, componentType);
            }
            return false;
        }
        if (ps.getUserStateOrDefault(userId).isInstantApp()) {
            if (canViewInstantApps(callingUid2, userId)) {
                return false;
            }
            if (component != null) {
                return true;
            }
            return true ^ this.mInstantAppRegistry.isInstantAccessGranted(userId, android.os.UserHandle.getAppId(callingUid2), ps.getAppId());
        }
        int appId = android.os.UserHandle.getAppId(callingUid2);
        com.android.server.pm.SettingBase callingPs = this.mSettings.getSettingBase(appId);
        return this.mAppsFilter.shouldFilterApplication(this, callingUid2, callingPs, ps, userId);
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal ps, int callingUid, android.content.ComponentName component, int componentType, int userId, boolean filterUninstall) {
        return shouldFilterApplication(ps, callingUid, component, componentType, userId, filterUninstall, true);
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal ps, int callingUid, android.content.ComponentName component, int componentType, int userId) {
        return shouldFilterApplication(ps, callingUid, component, componentType, userId, false);
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal ps, int callingUid, int userId) {
        return shouldFilterApplication(ps, callingUid, null, 0, userId, false);
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplication(com.android.server.pm.SharedUserSetting sus, int callingUid, int userId) {
        boolean filterApp = true;
        android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = sus.getPackageStates();
        for (int index = packageStates.size() - 1; index >= 0 && filterApp; index--) {
            filterApp &= shouldFilterApplication(packageStates.valueAt(index), callingUid, null, 0, userId, false);
        }
        return filterApp;
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplicationIncludingUninstalled(com.android.server.pm.pkg.PackageStateInternal ps, int callingUid, int userId) {
        return shouldFilterApplication(ps, callingUid, null, 0, userId, true);
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplicationIncludingUninstalledNotArchived(com.android.server.pm.pkg.PackageStateInternal ps, int callingUid, int userId) {
        return shouldFilterApplication(ps, callingUid, null, 0, userId, true, false);
    }

    @Override // com.android.server.pm.Computer
    public final boolean shouldFilterApplicationIncludingUninstalled(com.android.server.pm.SharedUserSetting sus, int callingUid, int userId) {
        if (shouldFilterApplication(sus, callingUid, userId)) {
            return true;
        }
        if (com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid)) {
            return false;
        }
        android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = sus.getPackageStates();
        for (int index = 0; index < packageStates.size(); index++) {
            com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(index);
            if (ps.getUserStateOrDefault(userId).isInstalled() || ps.isHiddenUntilInstalled()) {
                return false;
            }
        }
        return true;
    }

    private int bestDomainVerificationStatus(int status1, int status2) {
        if (status1 == 3) {
            return status2;
        }
        if (status2 == 3) {
            return status1;
        }
        return (int) android.util.MathUtils.max(status1, status2);
    }

    @Override // com.android.server.pm.Computer
    public final int checkUidPermission(java.lang.String permName, int uid) {
        return this.mPermissionManager.checkUidPermission(uid, permName, 0);
    }

    @Override // com.android.server.pm.Computer
    public int getPackageUidInternal(java.lang.String packageName, long flags, int userId, int callingUid) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        com.android.server.pm.pkg.PackageStateInternal ps2;
        com.android.server.pm.pkg.PackageStateInternal packageState = this.mSettings.getPackage(packageName);
        com.android.server.pm.pkg.AndroidPackage p = this.mPackages.get(packageName);
        if (p != null && com.android.server.pm.parsing.pkg.AndroidPackageUtils.isMatchForSystemOnly(packageState, flags) && (ps2 = getPackageStateInternal(p.getPackageName(), callingUid)) != null && ps2.getUserStateOrDefault(userId).isInstalled() && !shouldFilterApplication(ps2, callingUid, userId)) {
            return android.os.UserHandle.getUid(userId, p.getUid());
        }
        if ((4299169792L & flags) != 0 && (ps = this.mSettings.getPackage(packageName)) != null && com.android.server.pm.pkg.PackageStateUtils.isMatch(ps, flags) && !shouldFilterApplication(ps, callingUid, userId)) {
            return android.os.UserHandle.getUid(userId, ps.getAppId());
        }
        return -1;
    }

    private long updateFlags(long flags, int userId) {
        if ((flags & 786432) == 0) {
            com.android.server.pm.UserManagerInternal umInternal = this.mInjector.getUserManagerInternal();
            if (umInternal.isUserUnlockingOrUnlocked(userId)) {
                return flags | 786432;
            }
            return flags | 524288;
        }
        return flags;
    }

    @Override // com.android.server.pm.Computer
    public final long updateFlagsForApplication(long flags, int userId) {
        return updateFlagsForPackage(flags, userId);
    }

    @Override // com.android.server.pm.Computer
    public final long updateFlagsForComponent(long flags, int userId) {
        return updateFlags(flags, userId);
    }

    @Override // com.android.server.pm.Computer
    public final long updateFlagsForPackage(long flags, int userId) {
        long flags2;
        boolean isCallerSystemUser = android.os.UserHandle.getCallingUserId() == 0;
        if ((flags & 4194304) != 0) {
            enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, false, false, !isRecentsAccessingChildProfiles(android.os.Binder.getCallingUid(), userId), "MATCH_ANY_USER flag requires INTERACT_ACROSS_USERS permission");
        } else {
            if ((8192 & flags) != 0 && isCallerSystemUser && this.mUserManager.hasProfile(0)) {
                flags2 = flags | 4194304;
            }
            return updateFlags(flags2, userId);
        }
        flags2 = flags;
        return updateFlags(flags2, userId);
    }

    @Override // com.android.server.pm.Computer
    public final long updateFlagsForResolve(long flags, int userId, int callingUid, boolean wantInstantApps, boolean isImplicitImageCaptureIntentAndNotSetByDpc) {
        return updateFlagsForResolve(flags, userId, callingUid, wantInstantApps, false, isImplicitImageCaptureIntentAndNotSetByDpc);
    }

    @Override // com.android.server.pm.Computer
    public final long updateFlagsForResolve(long flags, int userId, int callingUid, boolean wantInstantApps, boolean onlyExposedExplicitly, boolean isImplicitImageCaptureIntentAndNotSetByDpc) {
        long flags2;
        if (safeMode() || isImplicitImageCaptureIntentAndNotSetByDpc) {
            flags |= 1048576;
        }
        if (getInstantAppPackageName(callingUid) != null) {
            if (onlyExposedExplicitly) {
                flags |= 33554432;
            }
            flags2 = flags | 16777216 | 8388608;
        } else {
            boolean allowMatchInstant = true;
            boolean wantMatchInstant = (flags & 8388608) != 0;
            if (!wantInstantApps && (!wantMatchInstant || !canViewInstantApps(callingUid, userId))) {
                allowMatchInstant = false;
            }
            flags2 = flags & (-50331649);
            if (!allowMatchInstant) {
                flags2 &= -8388609;
            }
        }
        return updateFlagsForComponent(flags2, userId);
    }

    @Override // com.android.server.pm.Computer
    public final void enforceCrossUserOrProfilePermission(int callingUid, int userId, boolean requireFullPermission, boolean checkShell, java.lang.String message) {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Invalid userId " + userId);
        }
        if (checkShell) {
            com.android.server.pm.PackageManagerServiceUtils.enforceShellRestriction(this.mInjector.getUserManagerInternal(), "no_debugging_features", callingUid, userId);
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (hasCrossUserPermission(callingUid, callingUserId, userId, requireFullPermission, false)) {
            return;
        }
        boolean isSameProfileGroup = isSameProfileGroup(callingUserId, userId);
        if (isSameProfileGroup && android.content.PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.INTERACT_ACROSS_PROFILES", -1, callingUid, getPackage(callingUid).getPackageName()) == 0) {
            return;
        }
        java.lang.String errorMessage = buildInvalidCrossUserOrProfilePermissionMessage(callingUid, userId, message, requireFullPermission, isSameProfileGroup);
        android.util.Slog.w("PackageManager", errorMessage);
        throw new java.lang.SecurityException(errorMessage);
    }

    private static java.lang.String buildInvalidCrossUserOrProfilePermissionMessage(int callingUid, int userId, java.lang.String message, boolean requireFullPermission, boolean isSameProfileGroup) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (message != null) {
            builder.append(message);
            builder.append(": ");
        }
        builder.append("UID ");
        builder.append(callingUid);
        builder.append(" requires ");
        builder.append("android.permission.INTERACT_ACROSS_USERS_FULL");
        if (!requireFullPermission) {
            builder.append(" or ");
            builder.append("android.permission.INTERACT_ACROSS_USERS");
            if (isSameProfileGroup) {
                builder.append(" or ");
                builder.append("android.permission.INTERACT_ACROSS_PROFILES");
            }
        }
        builder.append(" to access user ");
        builder.append(userId);
        builder.append(".");
        return builder.toString();
    }

    @Override // com.android.server.pm.Computer
    public final void enforceCrossUserPermission(int callingUid, int userId, boolean requireFullPermission, boolean checkShell, java.lang.String message) {
        enforceCrossUserPermission(callingUid, userId, requireFullPermission, checkShell, false, message);
    }

    @Override // com.android.server.pm.Computer
    public final void enforceCrossUserPermission(int callingUid, int userId, boolean requireFullPermission, boolean checkShell, boolean requirePermissionWhenSameUser, java.lang.String message) {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Invalid userId " + userId);
        }
        if (checkShell) {
            com.android.server.pm.PackageManagerServiceUtils.enforceShellRestriction(this.mInjector.getUserManagerInternal(), "no_debugging_features", callingUid, userId);
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (hasCrossUserPermission(callingUid, callingUserId, userId, requireFullPermission, requirePermissionWhenSameUser)) {
            return;
        }
        java.lang.String errorMessage = buildInvalidCrossUserPermissionMessage(callingUid, userId, message, requireFullPermission);
        android.util.Slog.w("PackageManager", errorMessage);
        throw new java.lang.SecurityException(errorMessage);
    }

    private static java.lang.String buildInvalidCrossUserPermissionMessage(int callingUid, int userId, java.lang.String message, boolean requireFullPermission) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (message != null) {
            builder.append(message);
            builder.append(": ");
        }
        builder.append("UID ");
        builder.append(callingUid);
        builder.append(" requires ");
        builder.append("android.permission.INTERACT_ACROSS_USERS_FULL");
        if (!requireFullPermission) {
            builder.append(" or ");
            builder.append("android.permission.INTERACT_ACROSS_USERS");
        }
        builder.append(" to access user ");
        builder.append(userId);
        builder.append(".");
        return builder.toString();
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.SigningDetails getSigningDetails(java.lang.String packageName) {
        com.android.server.pm.pkg.AndroidPackage p = this.mPackages.get(packageName);
        if (p == null) {
            return null;
        }
        return p.getSigningDetails();
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.SigningDetails getSigningDetails(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj != null) {
            if (obj instanceof com.android.server.pm.SharedUserSetting) {
                return ((com.android.server.pm.SharedUserSetting) obj).signatures.mSigningDetails;
            }
            if (obj instanceof com.android.server.pm.pkg.PackageStateInternal) {
                com.android.server.pm.pkg.PackageStateInternal ps = (com.android.server.pm.pkg.PackageStateInternal) obj;
                return ps.getSigningDetails();
            }
        }
        return android.content.pm.SigningDetails.UNKNOWN;
    }

    @Override // com.android.server.pm.Computer
    public boolean filterAppAccess(com.android.server.pm.pkg.AndroidPackage pkg, int callingUid, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(pkg.getPackageName());
        return shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId);
    }

    @Override // com.android.server.pm.Computer
    public boolean filterAppAccess(java.lang.String packageName, int callingUid, int userId, boolean filterUninstalled) {
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName);
        return shouldFilterApplication(ps, callingUid, null, 0, userId, filterUninstalled);
    }

    @Override // com.android.server.pm.Computer
    public boolean filterAppAccess(int uid, int callingUid) {
        if (android.os.Process.isSdkSandboxUid(uid)) {
            if (callingUid == uid) {
                return false;
            }
            int clientAppUid = android.os.Process.getAppUidForSdkSandboxUid(uid);
            return clientAppUid != uid;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object setting = this.mSettings.getSettingBase(appId);
        if (setting == null) {
            return true;
        }
        if (setting instanceof com.android.server.pm.SharedUserSetting) {
            return shouldFilterApplicationIncludingUninstalled((com.android.server.pm.SharedUserSetting) setting, callingUid, userId);
        }
        if (setting instanceof com.android.server.pm.pkg.PackageStateInternal) {
            return shouldFilterApplicationIncludingUninstalled((com.android.server.pm.pkg.PackageStateInternal) setting, callingUid, userId);
        }
        return true;
    }

    @Override // com.android.server.pm.Computer
    public void dump(int type, java.io.FileDescriptor fd, java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
        java.util.Collection<? extends com.android.server.pm.pkg.PackageStateInternal> pkgSettings;
        java.lang.String packageName = dumpState.getTargetPackageName();
        com.android.server.pm.pkg.PackageStateInternal setting = this.mSettings.getPackage(packageName);
        dumpState.isCheckIn();
        if (packageName != null && setting == null && !isApexPackage(packageName)) {
        }
        switch (type) {
            case 1:
                this.mSharedLibraries.dump(pw, dumpState);
                break;
            case 512:
                this.mSettings.dumpReadMessages(pw, dumpState);
                break;
            case 4096:
                this.mSettings.dumpPreferred(pw, dumpState, packageName);
                break;
            case 8192:
                pw.flush();
                java.io.FileOutputStream fout = new java.io.FileOutputStream(fd);
                java.io.BufferedOutputStream str = new java.io.BufferedOutputStream(fout);
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.newFastSerializer();
                try {
                    serializer.setOutput(str, java.nio.charset.StandardCharsets.UTF_8.name());
                    serializer.startDocument((java.lang.String) null, true);
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    this.mSettings.writePreferredActivitiesLPr(serializer, 0, dumpState.isFullPreferred());
                    serializer.endDocument();
                    serializer.flush();
                } catch (java.io.IOException e) {
                    pw.println("Failed writing: " + e);
                    return;
                } catch (java.lang.IllegalArgumentException e2) {
                    pw.println("Failed writing: " + e2);
                    return;
                } catch (java.lang.IllegalStateException e3) {
                    pw.println("Failed writing: " + e3);
                    return;
                }
                break;
            case 32768:
                if (dumpState.onTitlePrinted()) {
                    pw.println();
                }
                pw.println("Database versions:");
                this.mSettings.dumpVersionLPr(new com.android.internal.util.IndentingPrintWriter(pw, "  "));
                break;
            case 262144:
                android.util.IndentingPrintWriter writer = new android.util.IndentingPrintWriter(pw);
                if (dumpState.onTitlePrinted()) {
                    pw.println();
                }
                writer.println("Domain verification status:");
                writer.increaseIndent();
                try {
                    this.mDomainVerificationManager.printState(this, writer, packageName, -1);
                } catch (java.lang.Exception e4) {
                    pw.println("Failure printing domain verification information");
                    android.util.Slog.e("PackageManager", "Failure printing domain verification information", e4);
                }
                writer.decreaseIndent();
                break;
            case 524288:
                if (dumpState.onTitlePrinted()) {
                    pw.println();
                }
                com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ", 120);
                ipw.println();
                ipw.println("Frozen packages:");
                ipw.increaseIndent();
                if (this.mFrozenPackages.size() == 0) {
                    ipw.println("(none)");
                } else {
                    for (int i = 0; i < this.mFrozenPackages.size(); i++) {
                        ipw.print("package=");
                        ipw.print(this.mFrozenPackages.keyAt(i));
                        ipw.print(", refCounts=");
                        ipw.println(this.mFrozenPackages.valueAt(i));
                    }
                }
                ipw.decreaseIndent();
                break;
            case 1048576:
                com.android.internal.util.IndentingPrintWriter ipw2 = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                if (dumpState.onTitlePrinted()) {
                    pw.println();
                }
                ipw2.println("Dexopt state:");
                ipw2.increaseIndent();
                com.android.server.pm.DexOptHelper.dumpDexoptState(ipw2, packageName);
                ipw2.decreaseIndent();
                break;
            case 2097152:
                com.android.internal.util.IndentingPrintWriter ipw3 = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                if (dumpState.onTitlePrinted()) {
                    pw.println();
                }
                ipw3.println("Compiler stats:");
                ipw3.increaseIndent();
                if (setting != null) {
                    pkgSettings = java.util.Collections.singletonList(setting);
                } else {
                    pkgSettings = this.mSettings.getPackages().values();
                }
                for (com.android.server.pm.pkg.PackageStateInternal pkgSetting : pkgSettings) {
                    com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgSetting.getPkg();
                    if (pkg != null) {
                        java.lang.String pkgName = pkg.getPackageName();
                        ipw3.println("[" + pkgName + "]");
                        ipw3.increaseIndent();
                        com.android.server.pm.CompilerStats.PackageStats stats = this.mCompilerStats.getPackageStats(pkgName);
                        if (stats == null) {
                            ipw3.println("(No recorded stats)");
                        } else {
                            stats.dump(ipw3);
                        }
                        ipw3.decreaseIndent();
                    }
                }
                break;
            case 33554432:
                if (packageName == null || isApexPackage(packageName)) {
                    this.mApexManager.dump(pw);
                    dumpApex(pw, packageName);
                }
                break;
            case 67108864:
                java.lang.Integer filteringAppId = setting != null ? java.lang.Integer.valueOf(setting.getAppId()) : null;
                this.mAppsFilter.dumpQueries(pw, filteringAppId, dumpState, this.mUserManager.getUserIds(), new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.pm.ComputerEngine$$ExternalSyntheticLambda2
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                        return this.f$0.getPackagesForUidInternalBody(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue());
                    }
                });
                break;
        }
    }

    private void generateApexPackageInfo(java.util.List<com.android.server.pm.pkg.PackageStateInternal> activePackages, java.util.List<com.android.server.pm.pkg.PackageStateInternal> inactivePackages, java.util.List<com.android.server.pm.pkg.PackageStateInternal> factoryActivePackages, java.util.List<com.android.server.pm.pkg.PackageStateInternal> factoryInactivePackages) {
        for (com.android.server.pm.pkg.AndroidPackage p : this.mPackages.values()) {
            java.lang.String packageName = p.getPackageName();
            com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
            if (p.isApex() && ps != null) {
                activePackages.add(ps);
                if (!ps.isUpdatedSystemApp()) {
                    factoryActivePackages.add(ps);
                } else {
                    com.android.server.pm.pkg.PackageStateInternal psDisabled = this.mSettings.getDisabledSystemPkg(packageName);
                    factoryInactivePackages.add(psDisabled);
                    inactivePackages.add(psDisabled);
                }
            }
        }
    }

    private void dumpApex(java.io.PrintWriter pw, java.lang.String packageName) {
        com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ", 120);
        java.util.List<com.android.server.pm.pkg.PackageStateInternal> activePackages = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.pkg.PackageStateInternal> inactivePackages = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.pkg.PackageStateInternal> factoryActivePackages = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.pkg.PackageStateInternal> factoryInactivePackages = new java.util.ArrayList<>();
        generateApexPackageInfo(activePackages, inactivePackages, factoryActivePackages, factoryInactivePackages);
        ipw.println("Active APEX packages:");
        dumpApexPackageStates(activePackages, true, packageName, ipw);
        ipw.println("Inactive APEX packages:");
        dumpApexPackageStates(inactivePackages, false, packageName, ipw);
        ipw.println("Factory APEX packages:");
        dumpApexPackageStates(factoryActivePackages, true, packageName, ipw);
        dumpApexPackageStates(factoryInactivePackages, false, packageName, ipw);
    }

    private static void dumpApexPackageStates(java.util.List<com.android.server.pm.pkg.PackageStateInternal> packageStates, boolean isActive, java.lang.String packageName, com.android.internal.util.IndentingPrintWriter ipw) {
        ipw.println();
        ipw.increaseIndent();
        int size = packageStates.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.get(i);
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
            if (packageName == null || packageName.equals(pkg.getPackageName())) {
                ipw.println(pkg.getPackageName());
                ipw.increaseIndent();
                ipw.println("Version: " + pkg.getLongVersionCode());
                ipw.println("Path: " + pkg.getBaseApkPath());
                ipw.println("IsActive: " + isActive);
                ipw.println("IsFactory: " + (!packageState.isUpdatedSystemApp()));
                ipw.println("ApplicationInfo: ");
                ipw.increaseIndent();
                com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg).dump(new android.util.PrintWriterPrinter(ipw), "");
                ipw.decreaseIndent();
                ipw.decreaseIndent();
            }
        }
        ipw.decreaseIndent();
        ipw.println();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    protected com.android.server.pm.PackageManagerService.FindPreferredActivityBodyResult findPreferredActivityBody(android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<android.content.pm.ResolveInfo> query, boolean always, boolean removeMatches, boolean debug, int userId, boolean queryMayBeFiltered, int callingUid, boolean isDeviceProvisioned) {
        int i;
        boolean isDefaultAppPolicyEnabled;
        int match;
        int n;
        long flags2;
        boolean isDefaultAppPolicyEnabled2;
        int i2;
        int i3;
        com.android.server.pm.PackageManagerService.FindPreferredActivityBodyResult result = new com.android.server.pm.PackageManagerService.FindPreferredActivityBodyResult();
        long flags3 = updateFlagsForResolve(flags, userId, callingUid, false, isImplicitImageCaptureIntentAndNotSetByDpc(intent, userId, resolvedType, flags));
        android.content.Intent intent2 = com.android.server.pm.PackageManagerServiceUtils.updateIntentForResolve(intent);
        result.mPreferredResolveInfo = findPersistentPreferredActivity(intent2, resolvedType, flags3, query, debug, userId);
        if (result.mPreferredResolveInfo != null) {
            return result;
        }
        com.android.server.pm.PreferredIntentResolver pir = this.mSettings.getPreferredActivities(userId);
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
            android.util.Slog.v("PackageManager", "Looking for preferred activities...");
        }
        java.util.List listQueryIntent = pir != null ? pir.queryIntent(this, intent2, resolvedType, (65536 & flags3) != 0, userId) : null;
        if (listQueryIntent != null && listQueryIntent.size() > 0) {
            int match2 = 0;
            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                android.util.Slog.v("PackageManager", "Figuring out best match...");
            }
            int n2 = query.size();
            for (int j = 0; j < n2; j++) {
                android.content.pm.ResolveInfo ri = query.get(j);
                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                    android.util.Slog.v("PackageManager", "Match for " + ri.activityInfo + ": 0x" + java.lang.Integer.toHexString(ri.match));
                }
                if (ri.match > match2) {
                    match2 = ri.match;
                }
            }
            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                android.util.Slog.v("PackageManager", "Best match: 0x" + java.lang.Integer.toHexString(match2));
            }
            int match3 = match2 & 268369920;
            int delayRemoveIndex = -1;
            boolean isDefaultAppPolicyEnabled3 = this.mService.mPackageManagerServiceExt.isDefaultAppPolicyEnabledInFPANL(intent2);
            boolean shouldSkipMatchCheck = this.mService.mPackageManagerServiceExt.skipMatchCheckInFPANL(isDefaultAppPolicyEnabled3, query);
            boolean resolveForPermissionController = isDefaultAppPolicyEnabled3 && this.mService.mPackageManagerServiceExt.isResolveForPermissionController(this, callingUid);
            int m = listQueryIntent.size();
            int i4 = 0;
            while (i4 < m) {
                int m2 = m;
                com.android.server.pm.PreferredActivity pa = (com.android.server.pm.PreferredActivity) listQueryIntent.get(i4);
                java.util.List list = listQueryIntent;
                int delayRemoveIndex2 = delayRemoveIndex;
                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                    i = i4;
                    android.util.Slog.v("PackageManager", "Checking PreferredActivity ds=" + (pa.countDataSchemes() > 0 ? pa.getDataScheme(0) : "<none>") + "\n  component=" + pa.mPref.mComponent);
                    isDefaultAppPolicyEnabled = isDefaultAppPolicyEnabled3;
                    pa.dump(new android.util.LogPrinter(2, "PackageManager", 3), "  ");
                } else {
                    i = i4;
                    isDefaultAppPolicyEnabled = isDefaultAppPolicyEnabled3;
                }
                if (pa.mPref.mMatch != match3 && !shouldSkipMatchCheck) {
                    if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                        android.util.Slog.v("PackageManager", "Skipping bad match " + java.lang.Integer.toHexString(pa.mPref.mMatch));
                        n = n2;
                        match = match3;
                        flags2 = flags3;
                        isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                        i2 = i;
                        i3 = delayRemoveIndex2;
                    } else {
                        n = n2;
                        match = match3;
                        flags2 = flags3;
                        isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                        i2 = i;
                        i3 = delayRemoveIndex2;
                    }
                } else if (!always || pa.mPref.mAlways) {
                    android.content.pm.ActivityInfo ai = getActivityInfo(pa.mPref.mComponent, 512 | flags3 | 524288 | 262144, userId);
                    if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                        android.util.Slog.v("PackageManager", "Found preferred activity:");
                        if (ai != null) {
                            match = match3;
                            ai.dump(new android.util.LogPrinter(2, "PackageManager", 3), "  ");
                        } else {
                            match = match3;
                            android.util.Slog.v("PackageManager", "  null");
                        }
                    } else {
                        match = match3;
                    }
                    boolean excludeSetupWizardHomeActivity = isHomeIntent(intent2) && !isDeviceProvisioned;
                    boolean allowSetMutation = !excludeSetupWizardHomeActivity && (!queryMayBeFiltered || resolveForPermissionController);
                    if (ai == null) {
                        if (!allowSetMutation) {
                            n = n2;
                            flags2 = flags3;
                            isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                            i2 = i;
                            i3 = delayRemoveIndex2;
                        } else {
                            android.util.Slog.w("PackageManager", "Removing dangling preferred activity: " + pa.mPref.mComponent);
                            pir.removeFilter(pa);
                            result.mChanged = true;
                            n = n2;
                            flags2 = flags3;
                            isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                            i2 = i;
                            i3 = delayRemoveIndex2;
                        }
                    } else {
                        int j2 = 0;
                        while (true) {
                            if (j2 >= n2) {
                                n = n2;
                                flags2 = flags3;
                                isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                                i2 = i;
                                i3 = delayRemoveIndex2;
                                break;
                            }
                            android.content.pm.ResolveInfo ri2 = query.get(j2);
                            n = n2;
                            flags2 = flags3;
                            if (!ri2.activityInfo.applicationInfo.packageName.equals(ai.applicationInfo.packageName) || !ri2.activityInfo.name.equals(ai.name)) {
                                j2++;
                                n2 = n;
                                flags3 = flags2;
                            } else if (removeMatches && allowSetMutation) {
                                pir.removeFilter(pa);
                                result.mChanged = true;
                                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                    android.util.Slog.v("PackageManager", "Removing match " + pa.mPref.mComponent);
                                    isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                                    i2 = i;
                                    i3 = delayRemoveIndex2;
                                } else {
                                    isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                                    i2 = i;
                                    i3 = delayRemoveIndex2;
                                }
                            } else {
                                if (always && !pa.mPref.sameSet(query, excludeSetupWizardHomeActivity, userId)) {
                                    if (pa.mPref.isSuperset(query, excludeSetupWizardHomeActivity)) {
                                        if (allowSetMutation) {
                                            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                                android.util.Slog.i("PackageManager", "Result set changed, but PreferredActivity is still valid as only non-preferred components were removed for " + intent2 + " type " + resolvedType);
                                            }
                                            com.android.server.pm.PreferredActivity freshPa = new com.android.server.pm.PreferredActivity(pa, pa.mPref.mMatch, pa.mPref.discardObsoleteComponents(query), pa.mPref.mComponent, pa.mPref.mAlways);
                                            pir.removeFilter(pa);
                                            pir.addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) this, freshPa);
                                            result.mChanged = true;
                                        } else if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                            android.util.Slog.i("PackageManager", "Do not remove preferred activity");
                                        }
                                    } else {
                                        boolean isHomeActivity = "android.intent.action.MAIN".equals(intent2.getAction()) && intent2.hasCategory("android.intent.category.HOME");
                                        if (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveHomeAppBehavior() || !isHomeActivity) {
                                            isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                                            if (this.mService.mPackageManagerServiceExt.shouldSkipReturnInFPANL(isDefaultAppPolicyEnabled2)) {
                                                int i5 = i;
                                                delayRemoveIndex = this.mService.mPackageManagerServiceExt.calculateDelayRemoveIndex(delayRemoveIndex2, i5);
                                                i2 = i5;
                                            } else {
                                                if (allowSetMutation) {
                                                    android.util.Slog.i("PackageManager", "Result set changed, dropping preferred activity for " + intent2 + " type " + resolvedType);
                                                    if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                                        android.util.Slog.v("PackageManager", "Removing preferred activity since set changed " + pa.mPref.mComponent);
                                                    }
                                                    pir.removeFilter(pa);
                                                    com.android.server.pm.PreferredActivity lastChosen = new com.android.server.pm.PreferredActivity((com.android.server.pm.WatchedIntentFilter) pa, pa.mPref.mMatch, (android.content.ComponentName[]) null, pa.mPref.mComponent, false);
                                                    pir.addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) this, lastChosen);
                                                    result.mChanged = true;
                                                }
                                                result.mPreferredResolveInfo = null;
                                                return result;
                                            }
                                        }
                                    }
                                }
                                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                                    android.util.Slog.v("PackageManager", "Returning preferred activity: " + ri2.activityInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + ri2.activityInfo.name);
                                }
                                result.mPreferredResolveInfo = ri2;
                                return result;
                            }
                        }
                        i4 = i2 + 1;
                        m = m2;
                        listQueryIntent = list;
                        isDefaultAppPolicyEnabled3 = isDefaultAppPolicyEnabled2;
                        match3 = match;
                        n2 = n;
                        flags3 = flags2;
                    }
                } else {
                    if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                        android.util.Slog.v("PackageManager", "Skipping mAlways=false entry");
                    }
                    n = n2;
                    match = match3;
                    flags2 = flags3;
                    isDefaultAppPolicyEnabled2 = isDefaultAppPolicyEnabled;
                    i2 = i;
                    i3 = delayRemoveIndex2;
                }
                delayRemoveIndex = i3;
                i4 = i2 + 1;
                m = m2;
                listQueryIntent = list;
                isDefaultAppPolicyEnabled3 = isDefaultAppPolicyEnabled2;
                match3 = match;
                n2 = n;
                flags3 = flags2;
            }
            if (this.mService.mPackageManagerServiceExt.doDelayedRemoveInFPANL(this, delayRemoveIndex, pir, listQueryIntent, intent2, resolvedType)) {
                result.mChanged = true;
                result.mPreferredResolveInfo = null;
                return result;
            }
        }
        return result;
    }

    private static boolean isHomeIntent(android.content.Intent intent) {
        return "android.intent.action.MAIN".equals(intent.getAction()) && intent.hasCategory("android.intent.category.HOME") && intent.hasCategory("android.intent.category.DEFAULT");
    }

    @Override // com.android.server.pm.Computer
    public final com.android.server.pm.PackageManagerService.FindPreferredActivityBodyResult findPreferredActivityInternal(android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<android.content.pm.ResolveInfo> query, boolean always, boolean removeMatches, boolean debug, int userId, boolean queryMayBeFiltered) {
        int callingUid = android.os.Binder.getCallingUid();
        boolean isDeviceProvisioned = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) == 1;
        return findPreferredActivityBody(intent, resolvedType, flags, query, always, removeMatches, debug, userId, queryMayBeFiltered, callingUid, isDeviceProvisioned);
    }

    @Override // com.android.server.pm.Computer
    public final android.content.pm.ResolveInfo findPersistentPreferredActivity(android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<android.content.pm.ResolveInfo> query, boolean debug, int userId) {
        java.util.List listQueryIntent;
        int n = query.size();
        com.android.server.pm.PersistentPreferredIntentResolver ppir = this.mSettings.getPersistentPreferredActivities(userId);
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
            android.util.Slog.v("PackageManager", "Looking for persistent preferred activities...");
        }
        int i = 0;
        if (ppir != null) {
            listQueryIntent = ppir.queryIntent(this, intent, resolvedType, (flags & 65536) != 0, userId);
        } else {
            listQueryIntent = null;
        }
        if (listQueryIntent != null && listQueryIntent.size() > 0) {
            int m = listQueryIntent.size();
            int i2 = 0;
            while (i2 < m) {
                com.android.server.pm.PersistentPreferredActivity ppa = (com.android.server.pm.PersistentPreferredActivity) listQueryIntent.get(i2);
                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                    android.util.Slog.v("PackageManager", "Checking PersistentPreferredActivity ds=" + (ppa.countDataSchemes() > 0 ? ppa.getDataScheme(i) : "<none>") + "\n  component=" + ppa.mComponent);
                    ppa.dump(new android.util.LogPrinter(2, "PackageManager", 3), "  ");
                }
                android.content.pm.ActivityInfo ai = getActivityInfo(ppa.mComponent, flags | 512, userId);
                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                    android.util.Slog.v("PackageManager", "Found persistent preferred activity:");
                    if (ai != null) {
                        ai.dump(new android.util.LogPrinter(2, "PackageManager", 3), "  ");
                    } else {
                        android.util.Slog.v("PackageManager", "  null");
                    }
                }
                if (ai != null) {
                    for (int j = 0; j < n; j++) {
                        android.content.pm.ResolveInfo ri = query.get(j);
                        if (ri.activityInfo.applicationInfo.packageName.equals(ai.applicationInfo.packageName) && ri.activityInfo.name.equals(ai.name)) {
                            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) {
                                android.util.Slog.v("PackageManager", "Returning persistent preferred activity: " + ri.activityInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + ri.activityInfo.name);
                            }
                            return ri;
                        }
                    }
                }
                i2++;
                i = 0;
            }
            return null;
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.PreferredIntentResolver getPreferredActivities(int userId) {
        return this.mSettings.getPreferredActivities(userId);
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getPackageStates() {
        return this.mSettings.getPackages();
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getDisabledSystemPackageStates() {
        return this.mSettings.getDisabledSystemPackages();
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String getRenamedPackage(java.lang.String packageName) {
        return this.mSettings.getRenamedPackageLPr(packageName);
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> getSharedLibraries() {
        return this.mSharedLibraries.getAll();
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArraySet<java.lang.String> getNotifyPackagesForReplacedReceived(java.lang.String[] packages) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        android.util.ArraySet<java.lang.String> packagesToNotify = new android.util.ArraySet<>();
        for (java.lang.String packageName : packages) {
            com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
            if (!shouldFilterApplication(packageState, callingUid, callingUserId)) {
                packagesToNotify.add(packageName);
            }
        }
        return packagesToNotify;
    }

    @Override // com.android.server.pm.Computer
    public int getPackageStartability(boolean safeMode, java.lang.String packageName, int callingUid, int userId) {
        boolean ceStorageUnlocked = android.os.storage.StorageManager.isCeStorageUnlocked(userId);
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName);
        if (ps == null || shouldFilterApplication(ps, callingUid, userId) || !ps.getUserStateOrDefault(userId).isInstalled()) {
            return 1;
        }
        if (safeMode && !ps.isSystem()) {
            return 2;
        }
        if (this.mFrozenPackages.containsKey(packageName)) {
            return 3;
        }
        if (!ceStorageUnlocked && !com.android.server.pm.parsing.pkg.AndroidPackageUtils.isEncryptionAware(ps.getPkg())) {
            return 4;
        }
        return 0;
    }

    @Override // com.android.server.pm.Computer
    public boolean isPackageAvailable(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal state;
        if (!this.mUserManager.exists(userId)) {
            return false;
        }
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "is package available");
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName);
        if (ps == null || ps.getPkg() == null || shouldFilterApplication(ps, callingUid, userId) || (state = ps.getUserStateOrDefault(userId)) == null) {
            return false;
        }
        return com.android.server.pm.pkg.PackageUserStateUtils.isAvailable(state, 0L);
    }

    @Override // com.android.server.pm.Computer
    public boolean isApexPackage(java.lang.String packageName) {
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        return pkg != null && pkg.isApex();
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] names) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return names;
        }
        java.lang.String[] out = new java.lang.String[names.length];
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        boolean canViewInstantApps = canViewInstantApps(callingUid, callingUserId);
        for (int i = names.length - 1; i >= 0; i--) {
            com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(names[i]);
            boolean translateName = false;
            if (ps != null && ps.getRealName() != null) {
                boolean targetIsInstantApp = ps.getUserStateOrDefault(callingUserId).isInstantApp();
                translateName = !targetIsInstantApp || canViewInstantApps || this.mInstantAppRegistry.isInstantAccessGranted(callingUserId, android.os.UserHandle.getAppId(callingUid), ps.getAppId());
            }
            out[i] = translateName ? ps.getRealName() : names[i];
        }
        return out;
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] names) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return names;
        }
        java.lang.String[] out = new java.lang.String[names.length];
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        boolean canViewInstantApps = canViewInstantApps(callingUid, callingUserId);
        for (int i = names.length - 1; i >= 0; i--) {
            java.lang.String cur = getRenamedPackage(names[i]);
            boolean translateName = false;
            if (cur != null) {
                com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(names[i]);
                boolean targetIsInstantApp = ps != null && ps.getUserStateOrDefault(callingUserId).isInstantApp();
                translateName = !targetIsInstantApp || canViewInstantApps || this.mInstantAppRegistry.isInstantAccessGranted(callingUserId, android.os.UserHandle.getAppId(callingUid), ps.getAppId());
            }
            out[i] = translateName ? cur : names[i];
        }
        return out;
    }

    @Override // com.android.server.pm.Computer
    public int[] getPackageGids(java.lang.String packageName, long flags, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        long flags2 = updateFlagsForPackage(flags, userId);
        enforceCrossUserPermission(callingUid, userId, false, false, "getPackageGids");
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName);
        if (ps == null) {
            return null;
        }
        if (ps.getPkg() != null && com.android.server.pm.parsing.pkg.AndroidPackageUtils.isMatchForSystemOnly(ps, flags2) && ps.getUserStateOrDefault(userId).isInstalled() && !shouldFilterApplication(ps, callingUid, userId)) {
            return this.mPermissionManager.getGidsForUid(android.os.UserHandle.getUid(userId, ps.getAppId()));
        }
        if ((4299169792L & flags2) == 0 || !com.android.server.pm.pkg.PackageStateUtils.isMatch(ps, flags2) || shouldFilterApplication(ps, callingUid, userId)) {
            return null;
        }
        return this.mPermissionManager.getGidsForUid(android.os.UserHandle.getUid(userId, ps.getAppId()));
    }

    @Override // com.android.server.pm.Computer
    public int getTargetSdkVersion(java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName);
        if (ps == null || ps.getPkg() == null || shouldFilterApplicationIncludingUninstalled(ps, android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId())) {
            return -1;
        }
        return ps.getPkg().getTargetSdkVersion();
    }

    @Override // com.android.server.pm.Computer
    public boolean activitySupportsIntentAsUser(android.content.ComponentName resolveComponentName, android.content.ComponentName component, android.content.Intent intent, java.lang.String resolvedType, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "activitySupportsIntentAsUser");
        if (component.equals(resolveComponentName)) {
            return true;
        }
        com.android.internal.pm.pkg.component.ParsedActivity a = this.mComponentResolver.getActivity(component);
        if (a == null || (ps = getPackageStateInternal(component.getPackageName())) == null || shouldFilterApplication(ps, callingUid, component, 1, userId, true)) {
            return false;
        }
        for (int i = 0; i < a.getIntents().size(); i++) {
            if (((com.android.internal.pm.pkg.component.ParsedIntentInfo) a.getIntents().get(i)).getIntentFilter().match(intent.getAction(), resolvedType, intent.getScheme(), intent.getData(), intent.getCategories(), "PackageManager") >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName component, long flags, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        long flags2 = updateFlagsForComponent(flags, userId);
        enforceCrossUserPermission(callingUid, userId, false, false, "get receiver info");
        com.android.internal.pm.pkg.component.ParsedActivity a = this.mComponentResolver.getReceiver(component);
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
            android.util.Log.v("PackageManager", "getReceiverInfo " + component + ": " + a);
        }
        if (a == null || (ps = getPackageStateInternal(a.getPackageName())) == null || ps.getPkg() == null || !com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(ps, (com.android.internal.pm.pkg.component.ParsedMainComponent) a, flags2, userId) || shouldFilterApplication(ps, callingUid, component, 2, userId)) {
            return null;
        }
        return com.android.server.pm.parsing.PackageInfoUtils.generateActivityInfo(ps.getPkg(), a, flags2, ps.getUserStateOrDefault(userId), userId, ps);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.content.pm.ParceledListSlice<android.content.pm.SharedLibraryInfo> getSharedLibraries(java.lang.String r41, long r42, int r44) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.getSharedLibraries(java.lang.String, long, int):android.content.pm.ParceledListSlice");
    }

    @Override // com.android.server.pm.Computer
    public boolean canRequestPackageInstalls(java.lang.String packageName, int callingUid, int userId, boolean throwIfPermNotDeclared) {
        com.android.server.pm.pkg.AndroidPackage pkg;
        int uid = getPackageUidInternal(packageName, 0L, userId, callingUid);
        if (callingUid != uid && !com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingUid)) {
            throw new java.lang.SecurityException("Caller uid " + callingUid + " does not own package " + packageName);
        }
        if (isInstantAppInternal(packageName, userId, 1000) || (pkg = this.mPackages.get(packageName)) == null || pkg.getTargetSdkVersion() < 26) {
            return false;
        }
        if (pkg.getRequestedPermissions().contains("android.permission.REQUEST_INSTALL_PACKAGES")) {
            return !isInstallDisabledForPackage(packageName, uid, userId);
        }
        if (!throwIfPermNotDeclared) {
            android.util.Slog.e("PackageManager", "Need to declare android.permission.REQUEST_INSTALL_PACKAGES to call this api");
            return false;
        }
        throw new java.lang.SecurityException("Need to declare android.permission.REQUEST_INSTALL_PACKAGES to call this api");
    }

    @Override // com.android.server.pm.Computer
    public final boolean isInstallDisabledForPackage(java.lang.String packageName, int uid, int userId) {
        if (this.mUserManager.hasUserRestriction("no_install_unknown_sources", userId) || this.mUserManager.hasUserRestriction("no_install_unknown_sources_globally", userId)) {
            return true;
        }
        if (this.mExternalSourcesPolicy == null) {
            return false;
        }
        int isTrusted = this.mExternalSourcesPolicy.getPackageTrustedToInstallApps(packageName, uid);
        return isTrusted != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00c9  */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> getPackagesUsingSharedLibrary(android.content.pm.SharedLibraryInfo r21, long r22, int r24, int r25) {
        /*
            Method dump skipped, instruction units count: 308
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.getPackagesUsingSharedLibrary(android.content.pm.SharedLibraryInfo, long, int, int):android.util.Pair");
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ParceledListSlice<android.content.pm.SharedLibraryInfo> getDeclaredSharedLibraries(java.lang.String packageName, long flags, int userId) throws java.lang.Throwable {
        int i;
        int versionCount;
        int j;
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLibrary;
        int i2;
        java.util.List<android.content.pm.SharedLibraryInfo> result;
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_SHARED_LIBRARIES", "getDeclaredSharedLibraries");
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, true, false, "getDeclaredSharedLibraries");
        com.android.internal.util.Preconditions.checkNotNull(packageName, "packageName cannot be null");
        com.android.internal.util.Preconditions.checkArgumentNonnegative(userId, "userId must be >= 0");
        if (!this.mUserManager.exists(userId) || getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> sharedLibraries = getSharedLibraries();
        java.util.List<android.content.pm.SharedLibraryInfo> result2 = null;
        int libraryCount = sharedLibraries.size();
        int i3 = 0;
        while (i3 < libraryCount) {
            com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLibrary2 = sharedLibraries.valueAt(i3);
            if (versionedLibrary2 == null) {
                i = i3;
            } else {
                int versionCount2 = versionedLibrary2.size();
                java.util.List<android.content.pm.SharedLibraryInfo> result3 = result2;
                int j2 = 0;
                while (j2 < versionCount2) {
                    android.content.pm.SharedLibraryInfo libraryInfo = versionedLibrary2.valueAt(j2);
                    android.content.pm.VersionedPackage declaringPackage = libraryInfo.getDeclaringPackage();
                    if (!java.util.Objects.equals(declaringPackage.getPackageName(), packageName)) {
                        versionCount = versionCount2;
                        j = j2;
                        versionedLibrary = versionedLibrary2;
                        i2 = i3;
                    } else {
                        long identity = android.os.Binder.clearCallingIdentity();
                        try {
                            versionCount = versionCount2;
                            j = j2;
                            versionedLibrary = versionedLibrary2;
                            i2 = i3;
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                        try {
                            android.content.pm.PackageInfo packageInfo = getPackageInfoInternal(declaringPackage.getPackageName(), declaringPackage.getLongVersionCode(), flags | 67108864, android.os.Binder.getCallingUid(), userId);
                            if (packageInfo == null) {
                                android.os.Binder.restoreCallingIdentity(identity);
                            } else {
                                android.os.Binder.restoreCallingIdentity(identity);
                                android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> usingSharedLibraryPair = getPackagesUsingSharedLibrary(libraryInfo, flags, callingUid, userId);
                                android.content.pm.SharedLibraryInfo resultLibraryInfo = new android.content.pm.SharedLibraryInfo(libraryInfo.getPath(), libraryInfo.getPackageName(), libraryInfo.getAllCodePaths(), libraryInfo.getName(), libraryInfo.getLongVersion(), libraryInfo.getType(), libraryInfo.getDeclaringPackage(), (java.util.List) usingSharedLibraryPair.first, libraryInfo.getDependencies() == null ? null : new java.util.ArrayList(libraryInfo.getDependencies()), libraryInfo.isNative());
                                if (result3 != null) {
                                    result = result3;
                                } else {
                                    result = new java.util.ArrayList<>();
                                }
                                result.add(resultLibraryInfo);
                                result3 = result;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw th;
                        }
                    }
                    j2 = j + 1;
                    versionedLibrary2 = versionedLibrary;
                    i3 = i2;
                    versionCount2 = versionCount;
                }
                i = i3;
                result2 = result3;
            }
            i3 = i + 1;
        }
        if (result2 != null) {
            return new android.content.pm.ParceledListSlice<>(result2);
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName component, long flags, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        com.android.server.pm.pkg.PackageUserStateInternal state;
        android.content.pm.ApplicationInfo appInfo;
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        long flags2 = updateFlagsForComponent(flags, userId);
        enforceCrossUserPermission(callingUid, userId, false, false, "get provider info");
        com.android.internal.pm.pkg.component.ParsedProvider p = this.mComponentResolver.getProvider(component);
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_INFO) {
            android.util.Log.v("PackageManager", "getProviderInfo " + component + ": " + p);
        }
        if (p == null || (ps = getPackageStateInternal(p.getPackageName())) == null || ps.getPkg() == null || !com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(ps, (com.android.internal.pm.pkg.component.ParsedMainComponent) p, flags2, userId) || shouldFilterApplication(ps, callingUid, component, 4, userId) || (appInfo = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(ps.getPkg(), flags2, (state = ps.getUserStateOrDefault(userId)), userId, ps)) == null) {
            return null;
        }
        return com.android.server.pm.parsing.PackageInfoUtils.generateProviderInfo(ps.getPkg(), p, flags2, state, appInfo, userId, ps);
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArrayMap<java.lang.String, java.lang.String> getSystemSharedLibraryNamesAndPaths() {
        com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> sharedLibraries = getSharedLibraries();
        android.util.ArrayMap<java.lang.String, java.lang.String> libs = new android.util.ArrayMap<>();
        int libCount = sharedLibraries.size();
        for (int i = 0; i < libCount; i++) {
            com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = sharedLibraries.valueAt(i);
            if (versionedLib != null) {
                int versionCount = versionedLib.size();
                int j = 0;
                while (true) {
                    if (j < versionCount) {
                        android.content.pm.SharedLibraryInfo libraryInfo = versionedLib.valueAt(j);
                        if (!libraryInfo.isStatic()) {
                            libs.put(libraryInfo.getName(), libraryInfo.getPath());
                            break;
                        }
                        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(libraryInfo.getPackageName());
                        if (ps == null || filterSharedLibPackage(ps, android.os.Binder.getCallingUid(), android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()), 67108864L)) {
                            j++;
                        } else {
                            libs.put(libraryInfo.getName(), libraryInfo.getPath());
                            break;
                        }
                    }
                }
            }
        }
        return libs;
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.PackageStateInternal getPackageStateForInstalledAndFiltered(java.lang.String packageName, int callingUid, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null || shouldFilterApplicationIncludingUninstalled(packageState, callingUid, userId)) {
            return null;
        }
        return packageState;
    }

    @Override // com.android.server.pm.Computer
    public int checkSignatures(java.lang.String pkg1, java.lang.String pkg2, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "checkSignatures");
        com.android.server.pm.pkg.AndroidPackage p1 = this.mPackages.get(pkg1);
        com.android.server.pm.pkg.AndroidPackage p2 = this.mPackages.get(pkg2);
        com.android.server.pm.pkg.PackageStateInternal ps1 = p1 == null ? null : getPackageStateInternal(p1.getPackageName());
        com.android.server.pm.pkg.PackageStateInternal ps2 = p2 != null ? getPackageStateInternal(p2.getPackageName()) : null;
        if (p1 == null || ps1 == null || p2 == null || ps2 == null || shouldFilterApplicationIncludingUninstalled(ps1, callingUid, userId) || shouldFilterApplicationIncludingUninstalled(ps2, callingUid, userId)) {
            return -4;
        }
        return checkSignaturesInternal(p1.getSigningDetails(), p2.getSigningDetails());
    }

    @Override // com.android.server.pm.Computer
    public int checkUidSignatures(int uid1, int uid2) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        android.content.pm.SigningDetails p1SigningDetails = getSigningDetailsAndFilterAccess(uid1, callingUid, callingUserId);
        android.content.pm.SigningDetails p2SigningDetails = getSigningDetailsAndFilterAccess(uid2, callingUid, callingUserId);
        if (p1SigningDetails == null || p2SigningDetails == null) {
            return -4;
        }
        return checkSignaturesInternal(p1SigningDetails, p2SigningDetails);
    }

    @Override // com.android.server.pm.Computer
    public int checkUidSignaturesForAllUsers(int uid1, int uid2) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId1 = android.os.UserHandle.getUserId(uid1);
        int userId2 = android.os.UserHandle.getUserId(uid2);
        enforceCrossUserPermission(callingUid, userId1, false, false, "checkUidSignaturesForAllUsers");
        enforceCrossUserPermission(callingUid, userId2, false, false, "checkUidSignaturesForAllUsers");
        android.content.pm.SigningDetails p1SigningDetails = getSigningDetailsAndFilterAccess(uid1, callingUid, userId1);
        android.content.pm.SigningDetails p2SigningDetails = getSigningDetailsAndFilterAccess(uid2, callingUid, userId2);
        if (p1SigningDetails == null || p2SigningDetails == null) {
            return -4;
        }
        return checkSignaturesInternal(p1SigningDetails, p2SigningDetails);
    }

    private android.content.pm.SigningDetails getSigningDetailsAndFilterAccess(int uid, int callingUid, int userId) {
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj == null) {
            return null;
        }
        if (obj instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
            if (shouldFilterApplicationIncludingUninstalled(sus, callingUid, userId)) {
                return null;
            }
            return sus.signatures.mSigningDetails;
        }
        if (!(obj instanceof com.android.server.pm.PackageSetting)) {
            return null;
        }
        com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) obj;
        if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId)) {
            return null;
        }
        return ps.getSigningDetails();
    }

    private int checkSignaturesInternal(android.content.pm.SigningDetails p1SigningDetails, android.content.pm.SigningDetails p2SigningDetails) {
        android.content.pm.Signature[] p1Signatures;
        android.content.pm.Signature[] p2Signatures;
        if (p1SigningDetails == null) {
            if (p2SigningDetails == null) {
                return 1;
            }
            return -1;
        }
        if (p2SigningDetails == null) {
            return -2;
        }
        int result = com.android.server.pm.PackageManagerServiceUtils.compareSignatures(p1SigningDetails, p2SigningDetails);
        if (result == 0) {
            return result;
        }
        if (p1SigningDetails.hasPastSigningCertificates() || p2SigningDetails.hasPastSigningCertificates()) {
            if (p1SigningDetails.hasPastSigningCertificates()) {
                p1Signatures = new android.content.pm.Signature[]{p1SigningDetails.getPastSigningCertificates()[0]};
            } else {
                p1Signatures = p1SigningDetails.getSignatures();
            }
            if (p2SigningDetails.hasPastSigningCertificates()) {
                p2Signatures = new android.content.pm.Signature[]{p2SigningDetails.getPastSigningCertificates()[0]};
            } else {
                p2Signatures = p2SigningDetails.getSignatures();
            }
            return com.android.server.pm.PackageManagerServiceUtils.compareSignatureArrays(p1Signatures, p2Signatures);
        }
        return result;
    }

    @Override // com.android.server.pm.Computer
    public boolean hasSigningCertificate(java.lang.String packageName, byte[] certificate, int type) {
        com.android.server.pm.pkg.AndroidPackage p = this.mPackages.get(packageName);
        if (p == null) {
            return false;
        }
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(p.getPackageName());
        if (ps == null || shouldFilterApplicationIncludingUninstalled(ps, callingUid, callingUserId)) {
            return false;
        }
        switch (type) {
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public boolean hasUidSigningCertificate(int uid, byte[] certificate, int type) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        android.content.pm.SigningDetails signingDetails = getSigningDetailsAndFilterAccess(uid, callingUid, callingUserId);
        if (signingDetails == null) {
            return false;
        }
        switch (type) {
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public java.util.List<java.lang.String> getAllPackages() {
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRootOrShell("getAllPackages is limited to privileged callers");
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (canViewInstantApps(callingUid, callingUserId)) {
            return new java.util.ArrayList(this.mPackages.keySet());
        }
        java.lang.String instantAppPkgName = getInstantAppPackageName(callingUid);
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        if (instantAppPkgName != null) {
            for (com.android.server.pm.pkg.AndroidPackage pkg : this.mPackages.values()) {
                if (pkg.isVisibleToInstantApps()) {
                    result.add(pkg.getPackageName());
                }
            }
        } else {
            for (com.android.server.pm.pkg.AndroidPackage pkg2 : this.mPackages.values()) {
                com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(pkg2.getPackageName());
                if (ps == null || !ps.getUserStateOrDefault(callingUserId).isInstantApp() || this.mInstantAppRegistry.isInstantAccessGranted(callingUserId, android.os.UserHandle.getAppId(callingUid), ps.getAppId())) {
                    result.add(pkg2.getPackageName());
                }
            }
        }
        return result;
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String getNameForUid(int uid) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (isKnownIsolatedComputeApp(uid)) {
            try {
                uid = getIsolatedOwner(uid);
            } catch (java.lang.IllegalStateException e) {
                android.util.Slog.wtf("PackageManager", "Expected isolated uid " + uid + " to have an owner", e);
            }
        }
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
            if (shouldFilterApplicationIncludingUninstalled(sus, callingUid, callingUserId)) {
                return null;
            }
            return sus.name + ":" + sus.mAppId;
        }
        if (!(obj instanceof com.android.server.pm.PackageSetting)) {
            return null;
        }
        com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) obj;
        if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, callingUserId)) {
            return null;
        }
        return ps.getPackageName();
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] getNamesForUids(int[] uids) {
        if (uids == null || uids.length == 0) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        java.lang.String[] names = new java.lang.String[uids.length];
        for (int i = uids.length - 1; i >= 0; i--) {
            int uid = uids[i];
            if (android.os.Process.isSdkSandboxUid(uid)) {
                uid = getBaseSdkSandboxUid();
            }
            if (isKnownIsolatedComputeApp(uid)) {
                try {
                    uid = getIsolatedOwner(uid);
                } catch (java.lang.IllegalStateException e) {
                    android.util.Slog.wtf("PackageManager", "Expected isolated uid " + uid + " to have an owner", e);
                }
            }
            int appId = android.os.UserHandle.getAppId(uid);
            java.lang.Object obj = this.mSettings.getSettingBase(appId);
            if (obj instanceof com.android.server.pm.SharedUserSetting) {
                com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
                if (shouldFilterApplicationIncludingUninstalled(sus, callingUid, callingUserId)) {
                    names[i] = null;
                } else {
                    names[i] = "shared:" + sus.name;
                }
            } else if (obj instanceof com.android.server.pm.PackageSetting) {
                com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) obj;
                if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, callingUserId)) {
                    names[i] = null;
                } else {
                    names[i] = ps.getPackageName();
                }
            } else {
                names[i] = null;
            }
        }
        return names;
    }

    @Override // com.android.server.pm.Computer
    public int getUidForSharedUser(java.lang.String sharedUserName) {
        com.android.server.pm.SharedUserSetting suid;
        if (sharedUserName == null) {
            return -1;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null || (suid = this.mSettings.getSharedUserFromId(sharedUserName)) == null || shouldFilterApplicationIncludingUninstalled(suid, callingUid, android.os.UserHandle.getUserId(callingUid))) {
            return -1;
        }
        return suid.mAppId;
    }

    @Override // com.android.server.pm.Computer
    public int getFlagsForUid(int uid) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return 0;
        }
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
            if (shouldFilterApplicationIncludingUninstalled(sus, callingUid, callingUserId)) {
                return 0;
            }
            return sus.getFlags();
        }
        if (!(obj instanceof com.android.server.pm.PackageSetting)) {
            return 0;
        }
        com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) obj;
        if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, callingUserId)) {
            return 0;
        }
        return ps.getFlags();
    }

    @Override // com.android.server.pm.Computer
    public int getPrivateFlagsForUid(int uid) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null) {
            return 0;
        }
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
            if (shouldFilterApplicationIncludingUninstalled(sus, callingUid, callingUserId)) {
                return 0;
            }
            return sus.getPrivateFlags();
        }
        if (!(obj instanceof com.android.server.pm.PackageSetting)) {
            return 0;
        }
        com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) obj;
        if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, callingUserId)) {
            return 0;
        }
        return ps.getPrivateFlags();
    }

    @Override // com.android.server.pm.Computer
    public boolean isUidPrivileged(int uid) {
        if (getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return false;
        }
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int appId = android.os.UserHandle.getAppId(uid);
        java.lang.Object obj = this.mSettings.getSettingBase(appId);
        if (obj instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) obj;
            android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = sus.getPackageStates();
            int numPackages = packageStates.size();
            for (int index = 0; index < numPackages; index++) {
                com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(index);
                if (ps.isPrivileged()) {
                    return true;
                }
            }
        } else if (obj instanceof com.android.server.pm.PackageSetting) {
            com.android.server.pm.PackageSetting ps2 = (com.android.server.pm.PackageSetting) obj;
            return ps2.isPrivileged();
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "getAppOpPermissionPackages");
        if (permissionName == null || getInstantAppPackageName(callingUid) != null || !this.mUserManager.exists(userId)) {
            return libcore.util.EmptyArray.STRING;
        }
        android.util.ArraySet<java.lang.String> packageNames = new android.util.ArraySet<>(this.mPermissionManager.getAppOpPermissionPackages(permissionName));
        for (int i = packageNames.size() - 1; i >= 0; i--) {
            java.lang.String packageName = packageNames.valueAt(i);
            if (shouldFilterApplicationIncludingUninstalled(this.mSettings.getPackage(packageName), callingUid, userId)) {
                packageNames.removeAt(i);
            }
        }
        int i2 = packageNames.size();
        return (java.lang.String[]) packageNames.toArray(new java.lang.String[i2]);
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] permissions, long flags, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        long flags2 = updateFlagsForPackage(flags, userId);
        enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, false, "get packages holding permissions");
        boolean listUninstalled = (4299169792L & flags2) != 0;
        java.util.ArrayList<android.content.pm.PackageInfo> list = new java.util.ArrayList<>();
        boolean[] tmpBools = new boolean[permissions.length];
        for (com.android.server.pm.pkg.PackageStateInternal ps : getPackageStates().values()) {
            if (ps.getPkg() != null || listUninstalled) {
                addPackageHoldingPermissions(list, ps, permissions, tmpBools, flags2, userId);
            }
        }
        return new android.content.pm.ParceledListSlice<>(list);
    }

    private void addPackageHoldingPermissions(java.util.ArrayList<android.content.pm.PackageInfo> list, com.android.server.pm.pkg.PackageStateInternal ps, java.lang.String[] permissions, boolean[] tmp, long flags, int userId) {
        android.content.pm.PackageInfo pi;
        int numMatch = 0;
        for (int i = 0; i < permissions.length; i++) {
            java.lang.String permission = permissions[i];
            if (this.mPermissionManager.checkPermission(ps.getPackageName(), permission, "default:0", userId) == 0) {
                tmp[i] = true;
                numMatch++;
            } else {
                tmp[i] = false;
            }
        }
        if (numMatch != 0 && (pi = generatePackageInfo(ps, flags, userId)) != null) {
            if ((4096 & flags) == 0) {
                if (numMatch == permissions.length) {
                    pi.requestedPermissions = permissions;
                } else {
                    pi.requestedPermissions = new java.lang.String[numMatch];
                    int numMatch2 = 0;
                    for (int i2 = 0; i2 < permissions.length; i2++) {
                        if (tmp[i2]) {
                            pi.requestedPermissions[numMatch2] = permissions[i2];
                            numMatch2++;
                        }
                    }
                }
            }
            list.add(pi);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:97:0x01de A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01df A[RETURN] */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(long r21, int r23, int r24, boolean r25) {
        /*
            Method dump skipped, instruction units count: 480
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.getInstalledApplications(long, int, int, boolean):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b4  */
    @Override // com.android.server.pm.Computer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String r19, long r20, int r22, int r23) {
        /*
            Method dump skipped, instruction units count: 219
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ComputerEngine.resolveContentProvider(java.lang.String, long, int, int):android.content.pm.ProviderInfo");
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ProviderInfo getGrantImplicitAccessProviderInfo(int recipientUid, java.lang.String visibleAuthority) {
        int callingUid = android.os.Binder.getCallingUid();
        int recipientUserId = android.os.UserHandle.getUserId(recipientUid);
        android.content.pm.ProviderInfo contactsProvider = resolveContentProvider("com.android.contacts", 0L, android.os.UserHandle.getUserId(callingUid), callingUid);
        if (contactsProvider == null || contactsProvider.applicationInfo == null || !android.os.UserHandle.isSameApp(contactsProvider.applicationInfo.uid, callingUid)) {
            throw new java.lang.SecurityException(callingUid + " is not allow to call grantImplicitAccess");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return resolveContentProvider(visibleAuthority, 0L, recipientUserId, callingUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    @Override // com.android.server.pm.Computer
    @java.lang.Deprecated
    public void querySyncProviders(boolean safeMode, java.util.List<java.lang.String> outNames, java.util.List<android.content.pm.ProviderInfo> outInfo) {
        if (getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return;
        }
        java.util.List<java.lang.String> names = new java.util.ArrayList<>();
        java.util.List<android.content.pm.ProviderInfo> infos = new java.util.ArrayList<>();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        this.mComponentResolver.querySyncProviders(this, names, infos, safeMode, callingUserId);
        for (int i = infos.size() - 1; i >= 0; i--) {
            android.content.pm.ProviderInfo providerInfo = infos.get(i);
            com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(providerInfo.packageName);
            android.content.ComponentName component = new android.content.ComponentName(providerInfo.packageName, providerInfo.name);
            if (shouldFilterApplication(ps, android.os.Binder.getCallingUid(), component, 4, callingUserId)) {
                infos.remove(i);
                names.remove(i);
            }
        }
        if (!names.isEmpty()) {
            outNames.addAll(names);
        }
        if (!infos.isEmpty()) {
            outInfo.addAll(infos);
        }
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ParceledListSlice<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, long flags, java.lang.String metaDataKey) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = processName != null ? android.os.UserHandle.getUserId(uid) : android.os.UserHandle.getCallingUserId();
        enforceCrossUserPermission(callingUid, userId, false, false, "queryContentProviders");
        if (!this.mUserManager.exists(userId)) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        long flags2 = updateFlagsForComponent(flags, userId);
        java.util.List<android.content.pm.ProviderInfo> matchList = this.mComponentResolver.queryProviders(this, processName, metaDataKey, uid, flags2, userId);
        int listSize = matchList == null ? 0 : matchList.size();
        java.util.ArrayList<android.content.pm.ProviderInfo> finalList = null;
        for (int i = 0; i < listSize; i++) {
            android.content.pm.ProviderInfo providerInfo = matchList.get(i);
            if (com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(this.mSettings.getPackage(providerInfo.packageName), providerInfo, flags2, userId)) {
                com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(providerInfo.packageName);
                android.content.ComponentName component = new android.content.ComponentName(providerInfo.packageName, providerInfo.name);
                if (!shouldFilterApplication(ps, callingUid, component, 4, userId)) {
                    if (finalList == null) {
                        finalList = new java.util.ArrayList<>(listSize - i);
                    }
                    finalList.add(providerInfo);
                }
            }
        }
        if (finalList != null) {
            finalList.sort(sProviderInitOrderSorter);
            return new android.content.pm.ParceledListSlice<>(finalList);
        }
        return android.content.pm.ParceledListSlice.emptyList();
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.InstrumentationInfo getInstrumentationInfoAsUser(android.content.ComponentName component, int flags, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "getInstrumentationInfoAsUser");
        if (!this.mUserManager.exists(userId)) {
            return null;
        }
        java.lang.String packageName = component.getPackageName();
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        if (ps == null || pkg == null || shouldFilterApplication(ps, callingUid, component, 0, userId)) {
            return null;
        }
        com.android.internal.pm.pkg.component.ParsedInstrumentation i = this.mInstrumentation.get(component);
        com.android.server.pm.pkg.PackageUserStateInternal state = ps.getUserStateOrDefault(userId);
        return com.android.server.pm.parsing.PackageInfoUtils.generateInstrumentationInfo(i, pkg, flags, state, userId, ps);
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ParceledListSlice<android.content.pm.InstrumentationInfo> queryInstrumentationAsUser(java.lang.String targetPackage, int flags, int userId) {
        int callingUid;
        int callingUid2 = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid2, userId, false, false, "queryInstrumentationAsUser");
        if (!this.mUserManager.exists(userId)) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        java.util.ArrayList<android.content.pm.InstrumentationInfo> finalList = new java.util.ArrayList<>();
        int numInstrumentations = this.mInstrumentation.size();
        int index = 0;
        while (index < numInstrumentations) {
            com.android.internal.pm.pkg.component.ParsedInstrumentation p = this.mInstrumentation.valueAt(index);
            if (targetPackage == null || targetPackage.equals(p.getTargetPackage())) {
                java.lang.String packageName = p.getPackageName();
                com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
                com.android.server.pm.pkg.PackageStateInternal pkgSetting = getPackageStateInternal(packageName);
                if (pkg == null || pkgSetting == null) {
                    callingUid = callingUid2;
                } else if (shouldFilterApplication(pkgSetting, callingUid2, userId)) {
                    callingUid = callingUid2;
                } else {
                    com.android.server.pm.pkg.PackageUserStateInternal state = pkgSetting.getUserStateOrDefault(userId);
                    callingUid = callingUid2;
                    android.content.pm.InstrumentationInfo ii = com.android.server.pm.parsing.PackageInfoUtils.generateInstrumentationInfo(p, pkg, flags, state, userId, pkgSetting);
                    if (ii != null) {
                        finalList.add(ii);
                    }
                }
            } else {
                callingUid = callingUid2;
            }
            index++;
            callingUid2 = callingUid;
        }
        return new android.content.pm.ParceledListSlice<>(finalList);
    }

    @Override // com.android.server.pm.Computer
    public java.util.List<com.android.server.pm.pkg.PackageStateInternal> findSharedNonSystemLibraries(com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        java.util.List<android.content.pm.SharedLibraryInfo> deps = com.android.server.pm.SharedLibraryUtils.findSharedLibraries(pkgSetting);
        if (!deps.isEmpty()) {
            java.util.List<com.android.server.pm.pkg.PackageStateInternal> retValue = new java.util.ArrayList<>();
            for (android.content.pm.SharedLibraryInfo info : deps) {
                com.android.server.pm.pkg.PackageStateInternal depPackageSetting = getPackageStateInternal(info.getPackageName());
                if (depPackageSetting != null && depPackageSetting.getPkg() != null) {
                    retValue.add(depPackageSetting);
                }
            }
            return retValue;
        }
        return java.util.Collections.emptyList();
    }

    @Override // com.android.server.pm.Computer
    public boolean getApplicationHiddenSettingAsUser(java.lang.String packageName, int userId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_USERS", null);
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, true, false, "getApplicationHidden for user " + userId);
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
            if (ps == null) {
                return true;
            }
            if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId)) {
                return true;
            }
            return ps.getUserStateOrDefault(userId).isHidden();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    private com.android.server.pm.pkg.PackageUserStateInternal getUserStateOrDefaultForUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, true, false, "when asking about packages for user " + userId);
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
        if (ps == null || shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId)) {
            throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
        }
        return ps.getUserStateOrDefault(userId);
    }

    @Override // com.android.server.pm.Computer
    public boolean isPackageSuspendedForUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        return getUserStateOrDefaultForUser(packageName, userId).isSuspended();
    }

    @Override // com.android.server.pm.Computer
    public boolean isPackageQuarantinedForUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        return getUserStateOrDefaultForUser(packageName, userId).isQuarantined();
    }

    @Override // com.android.server.pm.Computer
    public boolean isPackageStoppedForUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        return getUserStateOrDefaultForUser(packageName, userId).isStopped();
    }

    @Override // com.android.server.pm.Computer
    public boolean isSuspendingAnyPackages(java.lang.String suspendingPackage, int suspendingUserId, int targetUserId) {
        android.content.pm.UserPackage suspender = android.content.pm.UserPackage.of(suspendingUserId, suspendingPackage);
        for (com.android.server.pm.pkg.PackageStateInternal packageState : getPackageStates().values()) {
            com.android.server.pm.pkg.PackageUserStateInternal state = packageState.getUserStateOrDefault(targetUserId);
            if (state.getSuspendParams() != null && state.getSuspendParams().containsKey(suspender)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ParceledListSlice<android.content.IntentFilter> getAllIntentFilters(java.lang.String packageName) {
        if (android.text.TextUtils.isEmpty(packageName)) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName);
        com.android.server.pm.pkg.AndroidPackage pkg = ps == null ? null : ps.getPkg();
        if (pkg == null || com.android.internal.util.ArrayUtils.isEmpty(pkg.getActivities())) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        if (shouldFilterApplicationIncludingUninstalled(ps, callingUid, callingUserId)) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        int count = com.android.internal.util.ArrayUtils.size(pkg.getActivities());
        java.util.ArrayList<android.content.IntentFilter> result = new java.util.ArrayList<>();
        for (int n = 0; n < count; n++) {
            com.android.internal.pm.pkg.component.ParsedActivity activity = (com.android.internal.pm.pkg.component.ParsedActivity) pkg.getActivities().get(n);
            java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intentInfos = activity.getIntents();
            for (int index = 0; index < intentInfos.size(); index++) {
                result.add(new android.content.IntentFilter(intentInfos.get(index).getIntentFilter()));
            }
        }
        return new android.content.pm.ParceledListSlice<>(result);
    }

    @Override // com.android.server.pm.Computer
    public boolean getBlockUninstallForUser(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
        int callingUid = android.os.Binder.getCallingUid();
        if (ps == null || shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId)) {
            return false;
        }
        return this.mSettings.getBlockUninstall(userId, packageName);
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String getInstallerPackageName(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.InstallSource installSource = getInstallSource(packageName, callingUid, userId);
        if (installSource == null) {
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        java.lang.String installerPackageName = installSource.mInstallerPackageName;
        if (installerPackageName != null) {
            com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(installerPackageName);
            if (ps == null || shouldFilterApplicationIncludingUninstalledNotArchived(ps, callingUid, android.os.UserHandle.getUserId(callingUid))) {
                return null;
            }
            return installerPackageName;
        }
        return installerPackageName;
    }

    private com.android.server.pm.InstallSource getInstallSource(java.lang.String packageName, int callingUid, int userId) {
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
        if (isApexPackage(packageName)) {
            return com.android.server.pm.InstallSource.EMPTY;
        }
        if (ps == null || shouldFilterApplicationIncludingUninstalledNotArchived(ps, callingUid, userId)) {
            return null;
        }
        return ps.getInstallSource();
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.InstallSourceInfo getInstallSourceInfo(java.lang.String packageName, int userId) {
        java.lang.String initiatingPackageName;
        android.content.pm.SigningInfo initiatingPackageSigningInfo;
        com.android.server.pm.pkg.PackageStateInternal ps;
        com.android.server.pm.pkg.PackageStateInternal ps2;
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "getInstallSourceInfo");
        com.android.server.pm.InstallSource installSource = getInstallSource(packageName, callingUid, userId);
        if (installSource == null) {
            return null;
        }
        java.lang.String installerPackageName = installSource.mInstallerPackageName;
        if (installerPackageName != null && ((ps2 = this.mSettings.getPackage(installerPackageName)) == null || shouldFilterApplicationIncludingUninstalled(ps2, callingUid, userId))) {
            installerPackageName = null;
        }
        java.lang.String updateOwnerPackageName = installSource.mUpdateOwnerPackageName;
        if (updateOwnerPackageName != null) {
            com.android.server.pm.pkg.PackageStateInternal ps3 = this.mSettings.getPackage(updateOwnerPackageName);
            boolean isCallerSystemOrUpdateOwner = callingUid == 1000 || isCallerSameApp(updateOwnerPackageName, callingUid);
            if (ps3 == null || shouldFilterApplicationIncludingUninstalled(ps3, callingUid, userId) || (!isCallerSystemOrUpdateOwner && isCallerFromManagedUserOrProfile(userId))) {
                updateOwnerPackageName = null;
            }
        }
        if (installSource.mIsInitiatingPackageUninstalled) {
            boolean isInstantApp = getInstantAppPackageName(callingUid) != null;
            if (!isInstantApp && isCallerSameApp(packageName, callingUid)) {
                initiatingPackageName = installSource.mInitiatingPackageName;
            } else {
                initiatingPackageName = null;
            }
        } else if (java.util.Objects.equals(installSource.mInitiatingPackageName, installSource.mInstallerPackageName)) {
            initiatingPackageName = installerPackageName;
        } else {
            initiatingPackageName = installSource.mInitiatingPackageName;
            com.android.server.pm.pkg.PackageStateInternal ps4 = this.mSettings.getPackage(initiatingPackageName);
            if (ps4 == null || shouldFilterApplicationIncludingUninstalled(ps4, callingUid, userId)) {
                initiatingPackageName = null;
            }
        }
        java.lang.String originatingPackageName = installSource.mOriginatingPackageName;
        if (originatingPackageName != null && ((ps = this.mSettings.getPackage(originatingPackageName)) == null || shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId))) {
            originatingPackageName = null;
        }
        if (originatingPackageName != null && this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") != 0) {
            originatingPackageName = null;
        }
        com.android.server.pm.PackageSignatures signatures = installSource.mInitiatingPackageSignatures;
        if (initiatingPackageName != null && signatures != null && signatures.mSigningDetails != android.content.pm.SigningDetails.UNKNOWN) {
            initiatingPackageSigningInfo = new android.content.pm.SigningInfo(signatures.mSigningDetails);
        } else {
            initiatingPackageSigningInfo = null;
        }
        return new android.content.pm.InstallSourceInfo(initiatingPackageName, initiatingPackageSigningInfo, originatingPackageName, installerPackageName, updateOwnerPackageName, installSource.mPackageSource);
    }

    @Override // com.android.server.pm.Computer
    public int getApplicationEnabledSetting(java.lang.String packageName, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return 2;
        }
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "get enabled");
        try {
            if (shouldFilterApplicationIncludingUninstalled(this.mSettings.getPackage(packageName), callingUid, userId)) {
                throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
            }
            return this.mSettings.getApplicationEnabledSetting(packageName, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
    }

    @Override // com.android.server.pm.Computer
    public int getComponentEnabledSetting(android.content.ComponentName component, int callingUid, int userId) {
        enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, false, false, "getComponentEnabled");
        return getComponentEnabledSettingInternal(component, callingUid, userId);
    }

    @Override // com.android.server.pm.Computer
    public int getComponentEnabledSettingInternal(android.content.ComponentName component, int callingUid, int userId) {
        if (component == null) {
            return 0;
        }
        if (!this.mUserManager.exists(userId)) {
            return 2;
        }
        try {
            if (shouldFilterApplication(this.mSettings.getPackage(component.getPackageName()), callingUid, component, 0, userId, true)) {
                throw new android.content.pm.PackageManager.NameNotFoundException(component.getPackageName());
            }
            return this.mSettings.getComponentEnabledSetting(component, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalArgumentException("Unknown component: " + component);
        }
    }

    @Override // com.android.server.pm.Computer
    public boolean isComponentEffectivelyEnabled(android.content.pm.ComponentInfo componentInfo, android.os.UserHandle userHandle) {
        try {
            java.lang.String packageName = componentInfo.packageName;
            int userId = userHandle.getIdentifier();
            int appEnabledSetting = this.mSettings.getApplicationEnabledSetting(packageName, userId);
            if (appEnabledSetting == 0) {
                if (!componentInfo.applicationInfo.enabled) {
                    return false;
                }
            } else if (appEnabledSetting != 1) {
                return false;
            }
            int componentEnabledSetting = this.mSettings.getComponentEnabledSetting(componentInfo.getComponentName(), userId);
            if (componentEnabledSetting == 0) {
                return componentInfo.isEnabled();
            }
            if (componentEnabledSetting != 1) {
                return false;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override // com.android.server.pm.Computer
    public boolean isApplicationEffectivelyEnabled(java.lang.String packageName, android.os.UserHandle userHandle) {
        try {
            int appEnabledSetting = this.mSettings.getApplicationEnabledSetting(packageName, userHandle.getIdentifier());
            if (appEnabledSetting == 0) {
                com.android.server.pm.pkg.AndroidPackage pkg = getPackage(packageName);
                if (pkg == null) {
                    return false;
                }
                return pkg.isEnabled();
            }
            if (appEnabledSetting != 1) {
                return false;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.KeySet getKeySetByAlias(java.lang.String packageName, java.lang.String alias) {
        if (packageName == null || alias == null) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        if (pkg == null || shouldFilterApplicationIncludingUninstalled(getPackageStateInternal(pkg.getPackageName()), callingUid, callingUserId)) {
            android.util.Slog.w("PackageManager", "KeySet requested for unknown package: " + packageName);
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        com.android.server.pm.KeySetManagerService ksms = this.mSettings.getKeySetManagerService();
        return new android.content.pm.KeySet(ksms.getKeySetByAliasAndPackageNameLPr(packageName, alias));
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.KeySet getSigningKeySet(java.lang.String packageName) {
        if (packageName == null) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        if (pkg == null || shouldFilterApplicationIncludingUninstalled(getPackageStateInternal(pkg.getPackageName()), callingUid, callingUserId)) {
            android.util.Slog.w("PackageManager", "KeySet requested for unknown package: " + packageName + ", uid:" + callingUid);
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        if (pkg.getUid() != callingUid && 1000 != callingUid) {
            throw new java.lang.SecurityException("May not access signing KeySet of other apps.");
        }
        com.android.server.pm.KeySetManagerService ksms = this.mSettings.getKeySetManagerService();
        return new android.content.pm.KeySet(ksms.getSigningKeySetByPackageNameLPr(packageName));
    }

    @Override // com.android.server.pm.Computer
    public boolean isPackageSignedByKeySet(java.lang.String packageName, android.content.pm.KeySet ks) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null || packageName == null || ks == null) {
            return false;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (pkg == null || shouldFilterApplicationIncludingUninstalled(getPackageStateInternal(pkg.getPackageName()), callingUid, callingUserId)) {
            android.util.Slog.w("PackageManager", "KeySet requested for unknown package: " + packageName);
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        android.os.IBinder ksh = ks.getToken();
        if (!(ksh instanceof com.android.server.pm.KeySetHandle)) {
            return false;
        }
        com.android.server.pm.KeySetManagerService ksms = this.mSettings.getKeySetManagerService();
        return ksms.packageIsSignedByLPr(packageName, (com.android.server.pm.KeySetHandle) ksh);
    }

    @Override // com.android.server.pm.Computer
    public boolean isPackageSignedByKeySetExactly(java.lang.String packageName, android.content.pm.KeySet ks) {
        int callingUid = android.os.Binder.getCallingUid();
        if (getInstantAppPackageName(callingUid) != null || packageName == null || ks == null) {
            return false;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackages.get(packageName);
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (pkg == null || shouldFilterApplicationIncludingUninstalled(getPackageStateInternal(pkg.getPackageName()), callingUid, callingUserId)) {
            android.util.Slog.w("PackageManager", "KeySet requested for unknown package: " + packageName);
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        android.os.IBinder ksh = ks.getToken();
        if (!(ksh instanceof com.android.server.pm.KeySetHandle)) {
            return false;
        }
        com.android.server.pm.KeySetManagerService ksms = this.mSettings.getKeySetManagerService();
        return ksms.packageIsSignedByExactlyLPr(packageName, (com.android.server.pm.KeySetHandle) ksh);
    }

    @Override // com.android.server.pm.Computer
    public android.util.SparseArray<int[]> getVisibilityAllowLists(java.lang.String packageName, int[] userIds) {
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageStateInternal(packageName, 1000);
        if (ps == null) {
            return null;
        }
        return this.mAppsFilter.getVisibilityAllowList(this, ps, userIds, getPackageStates());
    }

    @Override // com.android.server.pm.Computer
    public int[] getVisibilityAllowList(java.lang.String packageName, int userId) {
        android.util.SparseArray<int[]> visibilityAllowList = getVisibilityAllowLists(packageName, new int[]{userId});
        if (visibilityAllowList != null) {
            return visibilityAllowList.get(userId);
        }
        return null;
    }

    @Override // com.android.server.pm.Computer
    public boolean canQueryPackage(int callingUid, java.lang.String targetPackageName) {
        if (callingUid == 0 || targetPackageName == null) {
            return true;
        }
        java.lang.Object setting = this.mSettings.getSettingBase(android.os.UserHandle.getAppId(callingUid));
        if (setting == null) {
            return false;
        }
        int userId = android.os.UserHandle.getUserId(callingUid);
        int targetAppId = android.os.UserHandle.getAppId(getPackageUid(targetPackageName, 0L, userId));
        if (targetAppId != -1) {
            java.lang.Object targetSetting = this.mSettings.getSettingBase(targetAppId);
            return targetSetting instanceof com.android.server.pm.PackageSetting ? true ^ shouldFilterApplication((com.android.server.pm.PackageSetting) targetSetting, callingUid, userId) : true ^ shouldFilterApplication((com.android.server.pm.SharedUserSetting) targetSetting, callingUid, userId);
        }
        if (setting instanceof com.android.server.pm.PackageSetting) {
            com.android.server.pm.pkg.AndroidPackage pkg = ((com.android.server.pm.PackageSetting) setting).getPkg();
            if (pkg != null && this.mAppsFilter.canQueryPackage(pkg, targetPackageName)) {
                return true;
            }
            return false;
        }
        android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = ((com.android.server.pm.SharedUserSetting) setting).getPackageStates();
        for (int i = packageStates.size() - 1; i >= 0; i--) {
            com.android.server.pm.pkg.AndroidPackage pkg2 = packageStates.valueAt(i).getPkg();
            if (pkg2 != null && this.mAppsFilter.canQueryPackage(pkg2, targetPackageName)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.pm.Computer
    public int getPackageUid(java.lang.String packageName, long flags, int userId) {
        if (!this.mUserManager.exists(userId)) {
            return -1;
        }
        int callingUid = android.os.Binder.getCallingUid();
        long flags2 = updateFlagsForPackage(flags, userId);
        enforceCrossUserPermission(callingUid, userId, false, false, "getPackageUid");
        return getPackageUidInternal(packageName, flags2, userId, callingUid);
    }

    @Override // com.android.server.pm.Computer
    public boolean canAccessComponent(int callingUid, android.content.ComponentName component, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(component.getPackageName());
        return (packageState == null || shouldFilterApplication(packageState, callingUid, component, 0, userId, true)) ? false : true;
    }

    @Override // com.android.server.pm.Computer
    public boolean isCallerInstallerOfRecord(com.android.server.pm.pkg.AndroidPackage pkg, int callingUid) {
        com.android.server.pm.pkg.PackageStateInternal packageState;
        com.android.server.pm.pkg.PackageStateInternal installerPackageState;
        return (pkg == null || (packageState = getPackageStateInternal(pkg.getPackageName())) == null || (installerPackageState = getPackageStateInternal(packageState.getInstallSource().mInstallerPackageName)) == null || !android.os.UserHandle.isSameApp(installerPackageState.getAppId(), callingUid)) ? false : true;
    }

    @Override // com.android.server.pm.Computer
    public int getInstallReason(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, true, false, "get install reason");
        com.android.server.pm.pkg.PackageStateInternal ps = this.mSettings.getPackage(packageName);
        if (ps == null || shouldFilterApplicationIncludingUninstalled(ps, callingUid, userId)) {
            return 0;
        }
        return ps.getUserStateOrDefault(userId).getInstallReason();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    @Override // com.android.server.pm.Computer
    public boolean[] canPackageQuery(java.lang.String sourcePackageName, java.lang.String[] targetPackageNames, int userId) throws android.os.ParcelableException {
        int targetSize = targetPackageNames.length;
        boolean[] results = new boolean[targetSize];
        if (!this.mUserManager.exists(userId)) {
            return results;
        }
        int callingUid = android.os.Binder.getCallingUid();
        enforceCrossUserPermission(callingUid, userId, false, false, "can package query");
        com.android.server.pm.pkg.PackageStateInternal sourceSetting = getPackageStateInternal(sourcePackageName);
        com.android.server.pm.pkg.PackageStateInternal[] targetSettings = new com.android.server.pm.pkg.PackageStateInternal[targetSize];
        boolean throwException = sourceSetting == null || shouldFilterApplicationIncludingUninstalled(sourceSetting, callingUid, userId);
        for (int i = 0; !throwException && i < targetSize; i++) {
            targetSettings[i] = getPackageStateInternal(targetPackageNames[i]);
            throwException = targetSettings[i] == null || shouldFilterApplicationIncludingUninstalled(targetSettings[i], callingUid, userId);
        }
        if (throwException) {
            throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException("Package(s) " + sourcePackageName + " and/or " + java.util.Arrays.toString(targetPackageNames) + " not found."));
        }
        int sourcePackageUid = android.os.UserHandle.getUid(userId, sourceSetting.getAppId());
        for (int i2 = 0; i2 < targetSize; i2++) {
            results[i2] = !shouldFilterApplication(targetSettings[i2], sourcePackageUid, userId);
        }
        return results;
    }

    @Override // com.android.server.pm.Computer
    public boolean canForwardTo(android.content.Intent intent, java.lang.String resolvedType, int sourceUserId, int targetUserId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        if (this.mCrossProfileIntentResolverEngine.canReachTo(this, intent, resolvedType, sourceUserId, targetUserId)) {
            return true;
        }
        if (!intent.hasWebURI()) {
            return false;
        }
        int callingUid = android.os.Binder.getCallingUid();
        android.content.pm.UserInfo parent = getProfileParent(sourceUserId);
        if (parent == null) {
            return false;
        }
        long flags = updateFlagsForResolve(0L, parent.id, callingUid, false, isImplicitImageCaptureIntentAndNotSetByDpc(intent, parent.id, resolvedType, 0L));
        com.android.server.pm.CrossProfileDomainInfo xpDomainInfo = getCrossProfileDomainPreferredLpr(intent, resolvedType, flags | 65536, sourceUserId, parent.id);
        return xpDomainInfo != null;
    }

    @Override // com.android.server.pm.Computer
    public java.util.List<android.content.pm.ApplicationInfo> getPersistentApplications(boolean safeMode, int flags) {
        com.android.server.pm.pkg.PackageStateInternal ps;
        android.content.pm.ApplicationInfo ai;
        java.util.ArrayList<android.content.pm.ApplicationInfo> finalList = new java.util.ArrayList<>();
        int numPackages = this.mPackages.size();
        int userId = android.os.UserHandle.getCallingUserId();
        for (int index = 0; index < numPackages; index++) {
            com.android.server.pm.pkg.AndroidPackage p = this.mPackages.valueAt(index);
            com.android.server.pm.pkg.PackageStateInternal packageState = this.mSettings.getPackage(p.getPackageName());
            boolean z = false;
            boolean matchesUnaware = ((262144 & flags) == 0 || p.isDirectBootAware()) ? false : true;
            if ((524288 & flags) != 0 && p.isDirectBootAware()) {
                z = true;
            }
            boolean matchesAware = z;
            if (p.isPersistent() && ((!safeMode || packageState.isSystem()) && ((matchesUnaware || matchesAware) && (ps = this.mSettings.getPackage(p.getPackageName())) != null && (ai = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(p, flags, ps.getUserStateOrDefault(userId), userId, ps)) != null))) {
                finalList.add(ai);
            }
        }
        return finalList;
    }

    @Override // com.android.server.pm.Computer
    public android.util.SparseArray<java.lang.String> getAppsWithSharedUserIds() {
        android.util.SparseArray<java.lang.String> sharedUserIds = new android.util.SparseArray<>();
        for (com.android.server.pm.pkg.SharedUserApi sharedUser : this.mSettings.getSharedUsers().values()) {
            sharedUserIds.put(android.os.UserHandle.getAppId(sharedUser.getAppId()), sharedUser.getName());
        }
        return sharedUserIds;
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] getSharedUserPackagesForPackage(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageSetting = this.mSettings.getPackage(packageName);
        if (packageSetting == null || this.mSettings.getSharedUserFromPackageName(packageName) == null) {
            return libcore.util.EmptyArray.STRING;
        }
        android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packages = this.mSettings.getSharedUserFromPackageName(packageName).getPackageStates();
        int numPackages = packages.size();
        java.lang.String[] res = new java.lang.String[numPackages];
        int i = 0;
        for (int index = 0; index < numPackages; index++) {
            com.android.server.pm.pkg.PackageStateInternal ps = packages.valueAt(index);
            if (ps.getUserStateOrDefault(userId).isInstalled()) {
                res[i] = ps.getPackageName();
                i++;
            }
        }
        java.lang.String[] res2 = (java.lang.String[]) com.android.internal.util.ArrayUtils.trimToSize(res, i);
        return res2 != null ? res2 : libcore.util.EmptyArray.STRING;
    }

    @Override // com.android.server.pm.Computer
    public java.util.Set<java.lang.String> getUnusedPackages(long downgradeTimeThresholdMillis) {
        int index;
        java.util.Set<java.lang.String> unusedPackages = new android.util.ArraySet<>();
        long currentTimeInMillis = java.lang.System.currentTimeMillis();
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = this.mSettings.getPackages();
        int index2 = 0;
        while (index2 < packageStates.size()) {
            com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(index2);
            if (packageState.getPkg() == null) {
                index = index2;
            } else {
                com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo = this.mDexManager.getPackageUseInfoOrDefault(packageState.getPackageName());
                index = index2;
                if (com.android.server.pm.PackageManagerServiceUtils.isUnusedSinceTimeInMillis(com.android.server.pm.pkg.PackageStateUtils.getEarliestFirstInstallTime(packageState.getUserStates()), currentTimeInMillis, downgradeTimeThresholdMillis, packageUseInfo, packageState.getTransientState().getLatestPackageUseTimeInMills(), packageState.getTransientState().getLatestForegroundPackageUseTimeInMills())) {
                    unusedPackages.add(packageState.getPackageName());
                }
            }
            index2 = index + 1;
        }
        return unusedPackages;
    }

    @Override // com.android.server.pm.Computer
    public java.lang.CharSequence getHarmfulAppWarning(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingAppId = android.os.UserHandle.getAppId(callingUid);
        enforceCrossUserPermission(callingUid, userId, true, true, "getHarmfulAppInfo");
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingAppId) && checkUidPermission("android.permission.SET_HARMFUL_APP_WARNINGS", callingUid) != 0) {
            throw new java.lang.SecurityException("Caller must have the android.permission.SET_HARMFUL_APP_WARNINGS permission.");
        }
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null) {
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        return packageState.getUserStateOrDefault(userId).getHarmfulAppWarning();
    }

    @Override // com.android.server.pm.Computer
    public java.lang.String[] filterOnlySystemPackages(java.lang.String... pkgNames) {
        if (pkgNames == null) {
            return (java.lang.String[]) com.android.internal.util.ArrayUtils.emptyArray(java.lang.String.class);
        }
        java.util.ArrayList<java.lang.String> systemPackageNames = new java.util.ArrayList<>(pkgNames.length);
        for (java.lang.String pkgName : pkgNames) {
            if (pkgName != null) {
                com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(pkgName);
                if (packageState == null || packageState.getAndroidPackage() == null) {
                    android.util.Log.w("PackageManager", "Could not find package " + pkgName);
                } else if (!packageState.isSystem()) {
                    android.util.Log.w("PackageManager", pkgName + " is not system");
                } else {
                    systemPackageNames.add(pkgName);
                }
            }
        }
        return (java.lang.String[]) systemPackageNames.toArray(new java.lang.String[0]);
    }

    @Override // com.android.server.pm.Computer
    public java.util.List<com.android.server.pm.pkg.AndroidPackage> getPackagesForAppId(int appId) {
        com.android.server.pm.SettingBase settingBase = this.mSettings.getSettingBase(appId);
        if (settingBase instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) settingBase;
            return sus.getPackages();
        }
        if (settingBase instanceof com.android.server.pm.PackageSetting) {
            com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) settingBase;
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
            if (pkg != null) {
                return java.util.Collections.singletonList(pkg);
            }
            return java.util.Collections.emptyList();
        }
        return java.util.Collections.emptyList();
    }

    @Override // com.android.server.pm.Computer
    public int getUidTargetSdkVersion(int uid) {
        int v;
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int appId = android.os.UserHandle.getAppId(uid);
        com.android.server.pm.SettingBase settingBase = this.mSettings.getSettingBase(appId);
        if (settingBase instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) settingBase;
            android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = sus.getPackageStates();
            int vers = 10000;
            int numPackages = packageStates.size();
            for (int index = 0; index < numPackages; index++) {
                com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(index);
                if (ps.getPkg() != null && (v = ps.getPkg().getTargetSdkVersion()) < vers) {
                    vers = v;
                }
            }
            return vers;
        }
        if (settingBase instanceof com.android.server.pm.PackageSetting) {
            com.android.server.pm.PackageSetting ps2 = (com.android.server.pm.PackageSetting) settingBase;
            if (ps2.getPkg() != null) {
                return ps2.getPkg().getTargetSdkVersion();
            }
            return 10000;
        }
        return 10000;
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> getProcessesForUid(int uid) {
        if (android.os.Process.isSdkSandboxUid(uid)) {
            uid = getBaseSdkSandboxUid();
        }
        int appId = android.os.UserHandle.getAppId(uid);
        com.android.server.pm.SettingBase settingBase = this.mSettings.getSettingBase(appId);
        if (settingBase instanceof com.android.server.pm.SharedUserSetting) {
            com.android.server.pm.SharedUserSetting sus = (com.android.server.pm.SharedUserSetting) settingBase;
            return com.android.server.pm.parsing.PackageInfoUtils.generateProcessInfo(sus.processes, 0L);
        }
        if (!(settingBase instanceof com.android.server.pm.PackageSetting)) {
            return null;
        }
        com.android.server.pm.PackageSetting ps = (com.android.server.pm.PackageSetting) settingBase;
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        if (pkg == null) {
            return null;
        }
        return com.android.server.pm.parsing.PackageInfoUtils.generateProcessInfo(pkg.getProcesses(), 0L);
    }

    @Override // com.android.server.pm.Computer
    public boolean getBlockUninstall(int userId, java.lang.String packageName) {
        return this.mSettings.getBlockUninstall(userId, packageName);
    }

    @Override // com.android.server.pm.Computer
    public android.util.Pair<com.android.server.pm.pkg.PackageStateInternal, com.android.server.pm.pkg.SharedUserApi> getPackageOrSharedUser(int appId) {
        com.android.server.utils.Watchable settingBase = this.mSettings.getSettingBase(appId);
        if (settingBase instanceof com.android.server.pm.SharedUserSetting) {
            return android.util.Pair.create(null, (com.android.server.pm.pkg.SharedUserApi) settingBase);
        }
        if (settingBase instanceof com.android.server.pm.PackageSetting) {
            return android.util.Pair.create((com.android.server.pm.pkg.PackageStateInternal) settingBase, null);
        }
        return null;
    }

    private int getBaseSdkSandboxUid() {
        return getPackage(this.mService.getSdkSandboxPackageName()).getUid();
    }

    private boolean isKnownIsolatedComputeApp(int uid) {
        if (!android.os.Process.isIsolatedUid(uid)) {
            return false;
        }
        boolean isHotword = this.mPermissionManager.getHotwordDetectionServiceProvider() != null && uid == this.mPermissionManager.getHotwordDetectionServiceProvider().getUid();
        if (isHotword) {
            return true;
        }
        com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerInternal onDeviceIntelligenceManagerInternal = (com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerInternal) this.mInjector.getLocalService(com.android.server.ondeviceintelligence.OnDeviceIntelligenceManagerInternal.class);
        return onDeviceIntelligenceManagerInternal != null && uid == onDeviceIntelligenceManagerInternal.getInferenceServiceUid();
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.SharedUserApi getSharedUser(int sharedUserAppId) {
        return this.mSettings.getSharedUserFromAppId(sharedUserAppId);
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> getSharedUserPackages(int sharedUserAppId) {
        return this.mSettings.getSharedUserPackages(sharedUserAppId);
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.resolution.ComponentResolverApi getComponentResolver() {
        return this.mComponentResolver;
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.pm.pkg.PackageStateInternal getDisabledSystemPackage(java.lang.String packageName) {
        return this.mSettings.getDisabledSystemPkg(packageName);
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.ResolveInfo getInstantAppInstallerInfo() {
        return this.mInstantAppInstallerInfo;
    }

    @Override // com.android.server.pm.Computer
    public com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.Integer> getFrozenPackages() {
        return this.mFrozenPackages;
    }

    @Override // com.android.server.pm.Computer
    public void checkPackageFrozen(java.lang.String packageName) {
        if (!this.mFrozenPackages.containsKey(packageName)) {
            android.util.Slog.wtf("PackageManager", "Expected " + packageName + " to be frozen!", new java.lang.Throwable());
        }
    }

    @Override // com.android.server.pm.Computer
    public android.content.ComponentName getInstantAppInstallerComponent() {
        if (this.mLocalInstantAppInstallerActivity == null) {
            return null;
        }
        return this.mLocalInstantAppInstallerActivity.getComponentName();
    }

    @Override // com.android.server.pm.Computer
    public void dumpPermissions(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState) {
        this.mSettings.dumpPermissions(pw, packageName, permissionNames, dumpState);
    }

    @Override // com.android.server.pm.Computer
    public void dumpPackages(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState, boolean checkin) {
        this.mSettings.dumpPackages(pw, packageName, permissionNames, dumpState, checkin);
    }

    @Override // com.android.server.pm.Computer
    public void dumpKeySet(java.io.PrintWriter pw, java.lang.String packageName, com.android.server.pm.DumpState dumpState) {
        this.mSettings.dumpKeySet(pw, packageName, dumpState);
    }

    @Override // com.android.server.pm.Computer
    public void dumpSharedUsers(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState, boolean checkin) {
        this.mSettings.dumpSharedUsers(pw, packageName, permissionNames, dumpState, checkin);
    }

    @Override // com.android.server.pm.Computer
    public void dumpSharedUsersProto(android.util.proto.ProtoOutputStream proto) {
        this.mSettings.dumpSharedUsersProto(proto);
    }

    @Override // com.android.server.pm.Computer
    public void dumpPackagesProto(android.util.proto.ProtoOutputStream proto) {
        this.mSettings.dumpPackagesProto(proto);
    }

    @Override // com.android.server.pm.Computer
    public void dumpSharedLibrariesProto(android.util.proto.ProtoOutputStream proto) {
        this.mSharedLibraries.dumpProto(proto);
    }

    @Override // com.android.server.pm.Computer
    public java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> getVolumePackages(java.lang.String volumeUuid) {
        return this.mSettings.getVolumePackages(volumeUuid);
    }

    @Override // com.android.server.pm.Computer
    public android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.SharedUserApi> getSharedUsers() {
        return this.mSettings.getSharedUsers();
    }

    @Override // com.android.server.pm.Computer
    public android.content.pm.UserInfo[] getUserInfos() {
        return this.mInjector.getUserManagerInternal().getUserInfos();
    }

    public com.android.server.pm.IComputerEngineWrapper getWrapper() {
        return this.mWrapper;
    }

    private class ComputerEngineWrapper implements com.android.server.pm.IComputerEngineWrapper {
        private ComputerEngineWrapper() {
        }

        @Override // com.android.server.pm.IComputerEngineWrapper
        public java.util.List<android.content.pm.ResolveInfo> filterIfNotSystemUser(java.util.List<android.content.pm.ResolveInfo> resolveInfos, int userId) {
            return com.android.server.pm.ComputerEngine.this.filterIfNotSystemUser(resolveInfos, userId);
        }
    }
}
