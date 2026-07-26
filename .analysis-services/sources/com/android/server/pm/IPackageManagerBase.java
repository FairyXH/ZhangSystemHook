package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class IPackageManagerBase extends android.content.pm.IPackageManager.Stub {
    private final android.content.Context mContext;
    private final com.android.server.pm.DexOptHelper mDexOptHelper;
    private final com.android.server.pm.DomainVerificationConnection mDomainVerificationConnection;
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    private final com.android.server.pm.PackageInstallerService mInstallerService;
    private final android.content.ComponentName mInstantAppResolverSettingsComponent;
    private final com.android.server.pm.ModuleInfoProvider mModuleInfoProvider;
    private final com.android.server.pm.PackageProperty mPackageProperty;
    private final com.android.server.pm.PreferredActivityHelper mPreferredActivityHelper;
    private final android.content.ComponentName mResolveComponentName;
    private final com.android.server.pm.ResolveIntentHelper mResolveIntentHelper;
    private final com.android.server.pm.PackageManagerService mService;
    private final java.lang.String mServicesExtensionPackageName;
    private final java.lang.String mSharedSystemSharedLibraryPackageName;

    public IPackageManagerBase(com.android.server.pm.PackageManagerService service, android.content.Context context, com.android.server.pm.DexOptHelper dexOptHelper, com.android.server.pm.ModuleInfoProvider moduleInfoProvider, com.android.server.pm.PreferredActivityHelper preferredActivityHelper, com.android.server.pm.ResolveIntentHelper resolveIntentHelper, com.android.server.pm.verify.domain.DomainVerificationManagerInternal domainVerificationManager, com.android.server.pm.DomainVerificationConnection domainVerificationConnection, com.android.server.pm.PackageInstallerService installerService, com.android.server.pm.PackageProperty packageProperty, android.content.ComponentName resolveComponentName, android.content.ComponentName instantAppResolverSettingsComponent, java.lang.String servicesExtensionPackageName, java.lang.String sharedSystemSharedLibraryPackageName) {
        this.mService = service;
        this.mContext = context;
        this.mDexOptHelper = dexOptHelper;
        this.mModuleInfoProvider = moduleInfoProvider;
        this.mPreferredActivityHelper = preferredActivityHelper;
        this.mResolveIntentHelper = resolveIntentHelper;
        this.mDomainVerificationManager = domainVerificationManager;
        this.mDomainVerificationConnection = domainVerificationConnection;
        this.mInstallerService = installerService;
        this.mPackageProperty = packageProperty;
        this.mResolveComponentName = resolveComponentName;
        this.mInstantAppResolverSettingsComponent = instantAppResolverSettingsComponent;
        this.mServicesExtensionPackageName = servicesExtensionPackageName;
        this.mSharedSystemSharedLibraryPackageName = sharedSystemSharedLibraryPackageName;
    }

    protected com.android.server.pm.Computer snapshot() {
        return this.mService.snapshotComputer();
    }

    @java.lang.Deprecated
    public final boolean activitySupportsIntentAsUser(android.content.ComponentName component, android.content.Intent intent, java.lang.String resolvedType, int userId) {
        return snapshot().activitySupportsIntentAsUser(this.mResolveComponentName, component, intent, resolvedType, userId);
    }

    @java.lang.Deprecated
    public final void addCrossProfileIntentFilter(android.content.IntentFilter intentFilter, java.lang.String ownerPackage, int sourceUserId, int targetUserId, int flags) {
        this.mService.addCrossProfileIntentFilter(snapshot(), new com.android.server.pm.WatchedIntentFilter(intentFilter), ownerPackage, sourceUserId, targetUserId, flags);
    }

    @java.lang.Deprecated
    public final boolean addPermission(android.content.pm.PermissionInfo info) {
        return ((android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class)).addPermission(info, false);
    }

    @java.lang.Deprecated
    public final boolean addPermissionAsync(android.content.pm.PermissionInfo info) {
        return ((android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class)).addPermission(info, true);
    }

    @java.lang.Deprecated
    public final void addPersistentPreferredActivity(android.content.IntentFilter filter, android.content.ComponentName activity, int userId) {
        this.mPreferredActivityHelper.addPersistentPreferredActivity(new com.android.server.pm.WatchedIntentFilter(filter), activity, userId);
    }

    @java.lang.Deprecated
    public final void addPreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId, boolean removeExisting) {
        this.mPreferredActivityHelper.addPreferredActivity(snapshot(), new com.android.server.pm.WatchedIntentFilter(filter), match, set, activity, true, userId, "Adding preferred", removeExisting);
    }

    @java.lang.Deprecated
    public final boolean canForwardTo(android.content.Intent intent, java.lang.String resolvedType, int sourceUserId, int targetUserId) {
        return snapshot().canForwardTo(intent, resolvedType, sourceUserId, targetUserId);
    }

    @java.lang.Deprecated
    public final boolean canRequestPackageInstalls(java.lang.String packageName, int userId) {
        return snapshot().canRequestPackageInstalls(packageName, android.os.Binder.getCallingUid(), userId, true);
    }

    @java.lang.Deprecated
    public final java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] names) {
        return snapshot().canonicalToCurrentPackageNames(names);
    }

    @java.lang.Deprecated
    public final int checkPermission(java.lang.String permName, java.lang.String pkgName, int userId) {
        return this.mService.checkPermission(permName, pkgName, userId);
    }

    @java.lang.Deprecated
    public final int checkSignatures(java.lang.String pkg1, java.lang.String pkg2, int userId) {
        return snapshot().checkSignatures(pkg1, pkg2, userId);
    }

    @java.lang.Deprecated
    public final int checkUidPermission(java.lang.String permName, int uid) {
        return snapshot().checkUidPermission(permName, uid);
    }

    @java.lang.Deprecated
    public final int checkUidSignatures(int uid1, int uid2) {
        return snapshot().checkUidSignatures(uid1, uid2);
    }

    @java.lang.Deprecated
    public final void clearPackagePersistentPreferredActivities(java.lang.String packageName, int userId) {
        this.mPreferredActivityHelper.clearPackagePersistentPreferredActivities(packageName, userId);
    }

    @java.lang.Deprecated
    public final void clearPersistentPreferredActivity(android.content.IntentFilter filter, int userId) {
        this.mPreferredActivityHelper.clearPersistentPreferredActivity(filter, userId);
    }

    @java.lang.Deprecated
    public final void clearPackagePreferredActivities(java.lang.String packageName) {
        this.mPreferredActivityHelper.clearPackagePreferredActivities(snapshot(), packageName);
    }

    @java.lang.Deprecated
    public final java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] names) {
        return snapshot().currentToCanonicalPackageNames(names);
    }

    @java.lang.Deprecated
    public final void deleteExistingPackageAsUser(android.content.pm.VersionedPackage versionedPackage, android.content.pm.IPackageDeleteObserver2 observer, int userId) {
        this.mService.deleteExistingPackageAsUser(versionedPackage, observer, userId);
    }

    @java.lang.Deprecated
    public final void deletePackageAsUser(java.lang.String packageName, int versionCode, android.content.pm.IPackageDeleteObserver observer, int userId, int flags) {
        deletePackageVersioned(new android.content.pm.VersionedPackage(packageName, versionCode), new android.content.pm.PackageManager.LegacyPackageDeleteObserver(observer).getBinder(), userId, flags);
    }

    @java.lang.Deprecated
    public final void deletePackageVersioned(android.content.pm.VersionedPackage versionedPackage, android.content.pm.IPackageDeleteObserver2 observer, int userId, int deleteFlags) {
        this.mService.deletePackageVersioned(versionedPackage, observer, userId, deleteFlags);
    }

    @java.lang.Deprecated
    public final android.content.pm.ResolveInfo findPersistentPreferredActivity(android.content.Intent intent, int userId) {
        return this.mPreferredActivityHelper.findPersistentPreferredActivity(snapshot(), intent, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName component, long flags, int userId) {
        return snapshot().getActivityInfo(component, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.IntentFilter> getAllIntentFilters(java.lang.String packageName) {
        return snapshot().getAllIntentFilters(packageName);
    }

    @java.lang.Deprecated
    public final java.util.List<java.lang.String> getAllPackages() {
        return snapshot().getAllPackages();
    }

    @java.lang.Deprecated
    public final java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName, int userId) {
        return snapshot().getAppOpPermissionPackages(permissionName, userId);
    }

    @java.lang.Deprecated
    public final java.lang.String getAppPredictionServicePackageName() {
        return this.mService.mAppPredictionServicePackage;
    }

    @java.lang.Deprecated
    public final int getApplicationEnabledSetting(java.lang.String packageName, int userId) {
        return snapshot().getApplicationEnabledSetting(packageName, userId);
    }

    @java.lang.Deprecated
    public final boolean getApplicationHiddenSettingAsUser(java.lang.String packageName, int userId) {
        return snapshot().getApplicationHiddenSettingAsUser(packageName, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, long flags, int userId) {
        return snapshot().getApplicationInfo(packageName, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.dex.IArtManager getArtManager() {
        return this.mService.mArtManagerService;
    }

    @java.lang.Deprecated
    public final java.lang.String getAttentionServicePackageName() {
        return this.mService.ensureSystemPackageName(snapshot(), this.mService.getPackageFromComponentString(android.R.string.config_defaultContextualSearchLegacyEnabled));
    }

    @java.lang.Deprecated
    public final boolean getBlockUninstallForUser(java.lang.String packageName, int userId) {
        return snapshot().getBlockUninstallForUser(packageName, userId);
    }

    @java.lang.Deprecated
    public final int getComponentEnabledSetting(android.content.ComponentName component, int userId) {
        return snapshot().getComponentEnabledSetting(component, android.os.Binder.getCallingUid(), userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.SharedLibraryInfo> getDeclaredSharedLibraries(java.lang.String packageName, long flags, int userId) {
        return snapshot().getDeclaredSharedLibraries(packageName, flags, userId);
    }

    @java.lang.Deprecated
    public final byte[] getDefaultAppsBackup(int userId) {
        return this.mPreferredActivityHelper.getDefaultAppsBackup(userId);
    }

    @java.lang.Deprecated
    public final java.lang.String getDefaultTextClassifierPackageName() {
        return this.mService.mDefaultTextClassifierPackage;
    }

    @java.lang.Deprecated
    public final int getFlagsForUid(int uid) {
        return snapshot().getFlagsForUid(uid);
    }

    @java.lang.Deprecated
    public final java.lang.CharSequence getHarmfulAppWarning(java.lang.String packageName, int userId) {
        return snapshot().getHarmfulAppWarning(packageName, userId);
    }

    @java.lang.Deprecated
    public final android.content.ComponentName getHomeActivities(java.util.List<android.content.pm.ResolveInfo> allHomeCandidates) {
        com.android.server.pm.Computer snapshot = snapshot();
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return null;
        }
        return snapshot.getHomeActivitiesAsUser(allHomeCandidates, android.os.UserHandle.getCallingUserId());
    }

    @java.lang.Deprecated
    public final java.lang.String getIncidentReportApproverPackageName() {
        return this.mService.mIncidentReportApproverPackage;
    }

    @java.lang.Deprecated
    public final int getInstallLocation() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "default_install_location", 0);
    }

    @java.lang.Deprecated
    public final int getInstallReason(java.lang.String packageName, int userId) {
        return snapshot().getInstallReason(packageName, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.InstallSourceInfo getInstallSourceInfo(java.lang.String packageName, int userId) {
        return snapshot().getInstallSourceInfo(packageName, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ApplicationInfo> getInstalledApplications(long flags, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        return new android.content.pm.ParceledListSlice<>(snapshot().getInstalledApplications(flags, userId, callingUid, false));
    }

    @java.lang.Deprecated
    public final java.util.List<android.content.pm.ModuleInfo> getInstalledModules(int flags) {
        return this.mModuleInfoProvider.getInstalledModules(flags);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getInstalledPackages(long flags, int userId) {
        return snapshot().getInstalledPackages(flags, userId);
    }

    @java.lang.Deprecated
    public final java.lang.String getInstallerPackageName(java.lang.String packageName) {
        return snapshot().getInstallerPackageName(packageName, android.os.UserHandle.getCallingUserId());
    }

    @java.lang.Deprecated
    public final android.content.ComponentName getInstantAppInstallerComponent() {
        com.android.server.pm.Computer snapshot = snapshot();
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return null;
        }
        return snapshot.getInstantAppInstallerComponent();
    }

    @java.lang.Deprecated
    public final android.content.ComponentName getInstantAppResolverComponent() {
        com.android.server.pm.Computer snapshot = snapshot();
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return null;
        }
        return this.mService.getInstantAppResolver(snapshot);
    }

    @java.lang.Deprecated
    public final android.content.ComponentName getInstantAppResolverSettingsComponent() {
        return this.mInstantAppResolverSettingsComponent;
    }

    @java.lang.Deprecated
    public final android.content.pm.InstrumentationInfo getInstrumentationInfoAsUser(android.content.ComponentName component, int flags, int userId) {
        return snapshot().getInstrumentationInfoAsUser(component, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.IntentFilterVerificationInfo> getIntentFilterVerifications(java.lang.String packageName) {
        return android.content.pm.ParceledListSlice.emptyList();
    }

    @java.lang.Deprecated
    public final int getIntentVerificationStatus(java.lang.String packageName, int userId) {
        return this.mDomainVerificationManager.getLegacyState(packageName, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.KeySet getKeySetByAlias(java.lang.String packageName, java.lang.String alias) {
        return snapshot().getKeySetByAlias(packageName, alias);
    }

    @java.lang.Deprecated
    public final android.content.pm.ModuleInfo getModuleInfo(java.lang.String packageName, int flags) {
        return this.mModuleInfoProvider.getModuleInfo(packageName, flags);
    }

    @java.lang.Deprecated
    public final java.lang.String getNameForUid(int uid) {
        return snapshot().getNameForUid(uid);
    }

    @java.lang.Deprecated
    public final java.lang.String[] getNamesForUids(int[] uids) {
        return snapshot().getNamesForUids(uids);
    }

    @java.lang.Deprecated
    public final int[] getPackageGids(java.lang.String packageName, long flags, int userId) {
        return snapshot().getPackageGids(packageName, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, long flags, int userId) {
        return snapshot().getPackageInfo(packageName, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.PackageInfo getPackageInfoVersioned(android.content.pm.VersionedPackage versionedPackage, long flags, int userId) {
        return snapshot().getPackageInfoInternal(versionedPackage.getPackageName(), versionedPackage.getLongVersionCode(), flags, android.os.Binder.getCallingUid(), userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.IPackageInstaller getPackageInstaller() {
        if (com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot()) {
            return this.mInstallerService;
        }
        com.android.server.pm.Computer snapshot = snapshot();
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            android.util.Log.w("PackageManager", "Returning null PackageInstaller for InstantApps");
            return null;
        }
        return this.mInstallerService;
    }

    @java.lang.Deprecated
    public final void getPackageSizeInfo(java.lang.String packageName, int userId, android.content.pm.IPackageStatsObserver observer) {
        throw new java.lang.UnsupportedOperationException("Shame on you for calling the hidden API getPackageSizeInfo(). Shame!");
    }

    @java.lang.Deprecated
    public final int getPackageUid(java.lang.String packageName, long flags, int userId) {
        return snapshot().getPackageUid(packageName, flags, userId);
    }

    @java.lang.Deprecated
    public final java.lang.String[] getPackagesForUid(int uid) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(uid);
        snapshot().enforceCrossUserOrProfilePermission(callingUid, userId, false, false, "getPackagesForUid");
        return snapshot().getPackagesForUid(uid);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] permissions, long flags, int userId) {
        return snapshot().getPackagesHoldingPermissions(permissions, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        return this.mService.getPermissionGroupInfo(groupName, flags);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ApplicationInfo> getPersistentApplications(int flags) {
        com.android.server.pm.Computer snapshot = snapshot();
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return android.content.pm.ParceledListSlice.emptyList();
        }
        return new android.content.pm.ParceledListSlice<>(snapshot.getPersistentApplications(isSafeMode(), flags));
    }

    @java.lang.Deprecated
    public final int getPreferredActivities(java.util.List<android.content.IntentFilter> outFilters, java.util.List<android.content.ComponentName> outActivities, java.lang.String packageName) {
        return this.mPreferredActivityHelper.getPreferredActivities(snapshot(), outFilters, outActivities, packageName);
    }

    @java.lang.Deprecated
    public final byte[] getPreferredActivityBackup(int userId) {
        return this.mPreferredActivityHelper.getPreferredActivityBackup(userId);
    }

    @java.lang.Deprecated
    public final int getPrivateFlagsForUid(int uid) {
        return snapshot().getPrivateFlagsForUid(uid);
    }

    @java.lang.Deprecated
    public final android.content.pm.PackageManager.Property getPropertyAsUser(java.lang.String propertyName, java.lang.String packageName, java.lang.String className, int userId) {
        java.util.Objects.requireNonNull(propertyName);
        java.util.Objects.requireNonNull(packageName);
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.Computer snapshot = snapshot();
        snapshot.enforceCrossUserOrProfilePermission(callingUid, userId, false, false, "getPropertyAsUser");
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
        if (packageState == null) {
            return null;
        }
        return this.mPackageProperty.getProperty(propertyName, packageName, className);
    }

    @java.lang.Deprecated
    public final android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName component, long flags, int userId) {
        return snapshot().getProviderInfo(component, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName component, long flags, int userId) {
        return snapshot().getReceiverInfo(component, flags, userId);
    }

    @java.lang.Deprecated
    public final java.lang.String getRotationResolverPackageName() {
        return this.mService.ensureSystemPackageName(snapshot(), this.mService.getPackageFromComponentString(android.R.string.config_defaultWearableSensingService));
    }

    @java.lang.Deprecated
    public final android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName component, long flags, int userId) {
        return snapshot().getServiceInfo(component, flags, userId);
    }

    @java.lang.Deprecated
    public final java.lang.String getServicesSystemSharedLibraryPackageName() {
        return this.mServicesExtensionPackageName;
    }

    @java.lang.Deprecated
    public final java.lang.String getSetupWizardPackageName() {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Non-system caller");
        }
        return this.mService.mSetupWizardPackage;
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.SharedLibraryInfo> getSharedLibraries(java.lang.String packageName, long flags, int userId) {
        return snapshot().getSharedLibraries(packageName, flags, userId);
    }

    @java.lang.Deprecated
    public final java.lang.String getSharedSystemSharedLibraryPackageName() {
        return this.mSharedSystemSharedLibraryPackageName;
    }

    @java.lang.Deprecated
    public final android.content.pm.KeySet getSigningKeySet(java.lang.String packageName) {
        return snapshot().getSigningKeySet(packageName);
    }

    @java.lang.Deprecated
    public final java.lang.String getSdkSandboxPackageName() {
        return this.mService.getSdkSandboxPackageName();
    }

    @java.lang.Deprecated
    public final java.lang.String getSystemCaptionsServicePackageName() {
        return this.mService.ensureSystemPackageName(snapshot(), this.mService.getPackageFromComponentString(android.R.string.config_deviceSpecificAudioService));
    }

    @java.lang.Deprecated
    public final java.lang.String[] getSystemSharedLibraryNames() {
        android.util.ArrayMap<java.lang.String, java.lang.String> namesAndPaths = snapshot().getSystemSharedLibraryNamesAndPaths();
        if (namesAndPaths.isEmpty()) {
            return null;
        }
        int size = namesAndPaths.size();
        java.lang.String[] libs = new java.lang.String[size];
        for (int i = 0; i < size; i++) {
            libs[i] = namesAndPaths.keyAt(i);
        }
        return libs;
    }

    @java.lang.Deprecated
    public final java.util.Map<java.lang.String, java.lang.String> getSystemSharedLibraryNamesAndPaths() {
        return snapshot().getSystemSharedLibraryNamesAndPaths();
    }

    @java.lang.Deprecated
    public final java.lang.String getSystemTextClassifierPackageName() {
        return this.mService.mSystemTextClassifierPackageName;
    }

    @java.lang.Deprecated
    public final int getTargetSdkVersion(java.lang.String packageName) {
        return snapshot().getTargetSdkVersion(packageName);
    }

    @java.lang.Deprecated
    public final int getUidForSharedUser(java.lang.String sharedUserName) {
        return snapshot().getUidForSharedUser(sharedUserName);
    }

    @java.lang.Deprecated
    public final java.lang.String getWellbeingPackageName() {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return (java.lang.String) com.android.internal.util.CollectionUtils.firstOrNull(((android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class)).getRoleHolders("android.app.role.SYSTEM_WELLBEING"));
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @java.lang.Deprecated
    public final void grantRuntimePermission(java.lang.String packageName, java.lang.String permName, int userId) {
        ((android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class)).grantRuntimePermission(packageName, permName, android.os.UserHandle.of(userId));
    }

    @java.lang.Deprecated
    public final boolean hasSigningCertificate(java.lang.String packageName, byte[] certificate, int type) {
        return snapshot().hasSigningCertificate(packageName, certificate, type);
    }

    @java.lang.Deprecated
    public final boolean hasSystemFeature(java.lang.String name, int version) {
        return this.mService.hasSystemFeature(name, version);
    }

    @java.lang.Deprecated
    public final boolean hasSystemUidErrors() {
        return false;
    }

    @java.lang.Deprecated
    public final boolean hasUidSigningCertificate(int uid, byte[] certificate, int type) {
        return snapshot().hasUidSigningCertificate(uid, certificate, type);
    }

    @java.lang.Deprecated
    public final boolean isDeviceUpgrading() {
        return this.mService.isDeviceUpgrading();
    }

    @java.lang.Deprecated
    public final boolean isFirstBoot() {
        return this.mService.isFirstBoot();
    }

    @java.lang.Deprecated
    public final boolean isInstantApp(java.lang.String packageName, int userId) {
        return snapshot().isInstantApp(packageName, userId);
    }

    @java.lang.Deprecated
    public final boolean isPackageAvailable(java.lang.String packageName, int userId) {
        return snapshot().isPackageAvailable(packageName, userId);
    }

    @java.lang.Deprecated
    public final boolean isPackageDeviceAdminOnAnyUser(java.lang.String packageName) {
        return this.mService.isPackageDeviceAdminOnAnyUser(snapshot(), packageName);
    }

    @java.lang.Deprecated
    public final boolean isPackageSignedByKeySet(java.lang.String packageName, android.content.pm.KeySet ks) {
        return snapshot().isPackageSignedByKeySet(packageName, ks);
    }

    @java.lang.Deprecated
    public final boolean isPackageSignedByKeySetExactly(java.lang.String packageName, android.content.pm.KeySet ks) {
        return snapshot().isPackageSignedByKeySetExactly(packageName, ks);
    }

    @java.lang.Deprecated
    public final boolean isPackageSuspendedForUser(java.lang.String packageName, int userId) {
        try {
            return snapshot().isPackageSuspendedForUser(packageName, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalArgumentException("Unknown target package: " + packageName);
        }
    }

    @java.lang.Deprecated
    public final boolean isPackageQuarantinedForUser(java.lang.String packageName, int userId) {
        try {
            return snapshot().isPackageQuarantinedForUser(packageName, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalArgumentException("Unknown target package: " + packageName);
        }
    }

    public final boolean isPackageStoppedForUser(java.lang.String packageName, int userId) {
        try {
            return snapshot().isPackageStoppedForUser(packageName, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalArgumentException("Unknown target package: " + packageName);
        }
    }

    @java.lang.Deprecated
    public final boolean isSafeMode() {
        return this.mService.getSafeMode();
    }

    @java.lang.Deprecated
    public final boolean isStorageLow() {
        return this.mService.isStorageLow();
    }

    @java.lang.Deprecated
    public final boolean isUidPrivileged(int uid) {
        return snapshot().isUidPrivileged(uid);
    }

    @java.lang.Deprecated
    public final boolean performDexOptMode(java.lang.String packageName, boolean checkProfiles, java.lang.String targetCompilerFilter, boolean force, boolean bootComplete, java.lang.String splitName) {
        com.android.server.pm.Computer snapshot = snapshot();
        if (!checkProfiles) {
            android.util.Log.w("PackageManager", "Ignored checkProfiles=false flag");
        }
        return this.mDexOptHelper.performDexOptMode(snapshot, packageName, targetCompilerFilter, force, bootComplete, splitName);
    }

    @java.lang.Deprecated
    public final boolean performDexOptSecondary(java.lang.String packageName, java.lang.String compilerFilter, boolean force) {
        return this.mDexOptHelper.performDexOptSecondary(packageName, compilerFilter, force);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        try {
            android.os.Trace.traceBegin(262144L, "queryIntentActivities");
            java.util.List<android.content.pm.ResolveInfo> customList = this.mService.mPackageManagerServiceExt.queryIntentActivitiesExtAtBegin(intent, resolvedType, flags, userId);
            if (customList != null) {
                return new android.content.pm.ParceledListSlice<>(customList);
            }
            return new android.content.pm.ParceledListSlice<>(snapshot().queryIntentActivitiesInternal(intent, resolvedType, flags, userId));
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, long flags, java.lang.String metaDataKey) {
        return snapshot().queryContentProviders(processName, uid, flags, metaDataKey);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.InstrumentationInfo> queryInstrumentationAsUser(java.lang.String targetPackage, int flags, int userId) {
        return snapshot().queryInstrumentationAsUser(targetPackage, flags, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentActivityOptions(android.content.ComponentName caller, android.content.Intent[] specifics, java.lang.String[] specificTypes, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return new android.content.pm.ParceledListSlice<>(this.mResolveIntentHelper.queryIntentActivityOptionsInternal(snapshot(), caller, specifics, specificTypes, intent, resolvedType, flags, userId));
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentContentProviders(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return new android.content.pm.ParceledListSlice<>(this.mResolveIntentHelper.queryIntentContentProvidersInternal(snapshot(), intent, resolvedType, flags, userId));
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentReceivers(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return new android.content.pm.ParceledListSlice<>(this.mResolveIntentHelper.queryIntentReceiversInternal(snapshot(), intent, resolvedType, flags, userId, android.os.Binder.getCallingUid()));
    }

    @java.lang.Deprecated
    public final android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        return new android.content.pm.ParceledListSlice<>(snapshot().queryIntentServicesInternal(intent, resolvedType, flags, userId, callingUid, -1, false, false));
    }

    @java.lang.Deprecated
    public final void querySyncProviders(java.util.List<java.lang.String> outNames, java.util.List<android.content.pm.ProviderInfo> outInfo) {
        snapshot().querySyncProviders(isSafeMode(), outNames, outInfo);
    }

    @java.lang.Deprecated
    public final void removePermission(java.lang.String permName) {
        ((android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class)).removePermission(permName);
    }

    @java.lang.Deprecated
    public final void replacePreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId) {
        this.mPreferredActivityHelper.replacePreferredActivity(snapshot(), new com.android.server.pm.WatchedIntentFilter(filter), match, set, activity, userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ProviderInfo resolveContentProvider(java.lang.String name, long flags, int userId) {
        return snapshot().resolveContentProvider(name, flags, userId, android.os.Binder.getCallingUid());
    }

    @java.lang.Deprecated
    public final void resetApplicationPreferences(int userId) {
        this.mPreferredActivityHelper.resetApplicationPreferences(userId);
    }

    @java.lang.Deprecated
    public final android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return this.mResolveIntentHelper.resolveIntentInternal(snapshot(), intent, resolvedType, flags, 0L, userId, false, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
    }

    @java.lang.Deprecated
    public final android.content.pm.ResolveInfo resolveService(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        return this.mResolveIntentHelper.resolveServiceInternal(snapshot(), intent, resolvedType, flags, userId, callingUid, -1, false);
    }

    @java.lang.Deprecated
    public final void restoreDefaultApps(byte[] backup, int userId) {
        this.mPreferredActivityHelper.restoreDefaultApps(backup, userId);
    }

    @java.lang.Deprecated
    public final void restorePreferredActivities(byte[] backup, int userId) {
        this.mPreferredActivityHelper.restorePreferredActivities(backup, userId);
    }

    @java.lang.Deprecated
    public final void setHomeActivity(android.content.ComponentName comp, int userId) {
        this.mPreferredActivityHelper.setHomeActivity(snapshot(), comp, userId);
    }

    @java.lang.Deprecated
    public final void setLastChosenActivity(android.content.Intent intent, java.lang.String resolvedType, int flags, android.content.IntentFilter filter, int match, android.content.ComponentName activity) {
        this.mPreferredActivityHelper.setLastChosenActivity(snapshot(), intent, resolvedType, flags, new com.android.server.pm.WatchedIntentFilter(filter), match, activity);
    }

    @java.lang.Deprecated
    public final boolean updateIntentVerificationStatus(java.lang.String packageName, int status, int userId) {
        return this.mDomainVerificationManager.setLegacyUserState(packageName, userId, status);
    }

    @java.lang.Deprecated
    public final void verifyIntentFilter(int id, int verificationCode, java.util.List<java.lang.String> failedDomains) {
        com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.queueLegacyVerifyResult(this.mContext, this.mDomainVerificationConnection, id, verificationCode, failedDomains, android.os.Binder.getCallingUid());
    }

    @java.lang.Deprecated
    public final boolean[] canPackageQuery(java.lang.String sourcePackageName, java.lang.String[] targetPackageNames, int userId) {
        return snapshot().canPackageQuery(sourcePackageName, targetPackageNames, userId);
    }

    @java.lang.Deprecated
    public final void deletePreloadsFileCache() throws android.os.RemoteException {
        this.mService.deletePreloadsFileCache();
    }

    @java.lang.Deprecated
    public final void setSystemAppHiddenUntilInstalled(java.lang.String packageName, boolean hidden) throws android.os.RemoteException {
        this.mService.setSystemAppHiddenUntilInstalled(snapshot(), packageName, hidden);
    }

    @java.lang.Deprecated
    public final boolean setSystemAppInstallState(java.lang.String packageName, boolean installed, int userId) throws android.os.RemoteException {
        return this.mService.setSystemAppInstallState(snapshot(), packageName, installed, userId);
    }

    @java.lang.Deprecated
    public final void finishPackageInstall(int token, boolean didLaunch) throws android.os.RemoteException {
        this.mService.finishPackageInstall(token, didLaunch);
    }
}
