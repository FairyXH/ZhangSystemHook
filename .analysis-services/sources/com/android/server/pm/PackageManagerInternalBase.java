package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
abstract class PackageManagerInternalBase extends android.content.pm.PackageManagerInternal {
    private final com.android.server.pm.PackageManagerService mService;

    protected abstract com.android.server.pm.ApexManager getApexManager();

    protected abstract com.android.server.pm.AppDataHelper getAppDataHelper();

    protected abstract android.content.Context getContext();

    protected abstract com.android.server.pm.dex.DexManager getDexManager();

    protected abstract com.android.server.pm.DistractingPackageHelper getDistractingPackageHelper();

    protected abstract com.android.server.pm.InstantAppRegistry getInstantAppRegistry();

    protected abstract com.android.server.pm.PackageObserverHelper getPackageObserverHelper();

    protected abstract com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManager();

    protected abstract com.android.server.pm.ProtectedPackages getProtectedPackages();

    protected abstract com.android.server.pm.ResolveIntentHelper getResolveIntentHelper();

    protected abstract com.android.server.pm.SuspendPackageHelper getSuspendPackageHelper();

    protected abstract com.android.server.pm.UserNeedsBadgingCache getUserNeedsBadging();

    public PackageManagerInternalBase(com.android.server.pm.PackageManagerService service) {
        this.mService = service;
    }

    @Override // android.content.pm.PackageManagerInternal
    public final com.android.server.pm.Computer snapshot() {
        return this.mService.snapshotComputer();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(long flags, int userId, int callingUid) {
        return snapshot().getInstalledApplications(flags, userId, callingUid, false);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<android.content.pm.ApplicationInfo> getInstalledApplicationsCrossUser(long flags, int userId, int callingUid) {
        return snapshot().getInstalledApplications(flags, userId, callingUid, true);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isInstantApp(java.lang.String packageName, int userId) {
        return snapshot().isInstantApp(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.lang.String getInstantAppPackageName(int uid) {
        return snapshot().getInstantAppPackageName(uid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean filterAppAccess(com.android.server.pm.pkg.AndroidPackage pkg, int callingUid, int userId) {
        return snapshot().filterAppAccess(pkg, callingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean filterAppAccess(java.lang.String packageName, int callingUid, int userId, boolean filterUninstalled) {
        return snapshot().filterAppAccess(packageName, callingUid, userId, filterUninstalled);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean filterAppAccess(int uid, int callingUid) {
        return snapshot().filterAppAccess(uid, callingUid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int[] getVisibilityAllowList(java.lang.String packageName, int userId) {
        return snapshot().getVisibilityAllowList(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean canQueryPackage(int callingUid, java.lang.String packageName) {
        return snapshot().canQueryPackage(callingUid, packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.AndroidPackage getPackage(java.lang.String packageName) {
        return snapshot().getPackage(packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.AndroidPackage getAndroidPackage(java.lang.String packageName) {
        return snapshot().getPackage(packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.AndroidPackage getPackage(int uid) {
        return snapshot().getPackage(uid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<com.android.server.pm.pkg.AndroidPackage> getPackagesForAppId(int appId) {
        return snapshot().getPackagesForAppId(appId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.PackageStateInternal getPackageStateInternal(java.lang.String packageName) {
        return snapshot().getPackageStateInternal(packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getPackageStates() {
        return snapshot().getPackageStates();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void removePackageListObserver(android.content.pm.PackageManagerInternal.PackageListObserver observer) {
        getPackageObserverHelper().removeObserver(observer);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.PackageStateInternal getDisabledSystemPackage(java.lang.String packageName) {
        return snapshot().getDisabledSystemPackage(packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.lang.String[] getKnownPackageNames(int knownPackage, int userId) {
        return this.mService.getKnownPackageNamesInternal(snapshot(), knownPackage, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void setKeepUninstalledPackages(java.util.List<java.lang.String> packageList) {
        this.mService.setKeepUninstalledPackagesInternal(snapshot(), packageList);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) {
        return getPermissionManager().isPermissionsReviewRequired(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, long flags, int filterCallingUid, int userId) {
        return snapshot().getPackageInfoInternal(packageName, -1L, flags, filterCallingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.os.Bundle getSuspendedPackageLauncherExtras(java.lang.String packageName, int userId) {
        return getSuspendPackageHelper().getSuspendedPackageLauncherExtras(snapshot(), packageName, userId, android.os.Binder.getCallingUid());
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isPackageSuspended(java.lang.String packageName, int userId) {
        return getSuspendPackageHelper().isPackageSuspended(snapshot(), packageName, userId, android.os.Binder.getCallingUid());
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void removeNonSystemPackageSuspensions(java.lang.String packageName, int userId) {
        getSuspendPackageHelper().removeSuspensionsBySuspendingPackage(snapshot(), new java.lang.String[]{packageName}, new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerInternalBase$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.pm.PackageManagerInternalBase.lambda$removeNonSystemPackageSuspensions$0((android.content.pm.UserPackage) obj);
            }
        }, userId);
    }

    static /* synthetic */ boolean lambda$removeNonSystemPackageSuspensions$0(android.content.pm.UserPackage suspendingPackage) {
        return !com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(suspendingPackage.packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void removeDistractingPackageRestrictions(java.lang.String packageName, int userId) {
        getDistractingPackageHelper().removeDistractingPackageRestrictions(snapshot(), new java.lang.String[]{packageName}, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void removeAllDistractingPackageRestrictions(int userId) {
        this.mService.removeAllDistractingPackageRestrictions(snapshot(), userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.UserPackage getSuspendingPackage(java.lang.String suspendedPackage, int userId) {
        return getSuspendPackageHelper().getSuspendingPackage(snapshot(), suspendedPackage, userId, android.os.Binder.getCallingUid());
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.SuspendDialogInfo getSuspendedDialogInfo(java.lang.String suspendedPackage, android.content.pm.UserPackage suspendingPackage, int userId) {
        return getSuspendPackageHelper().getSuspendedDialogInfo(snapshot(), suspendedPackage, suspendingPackage, userId, android.os.Binder.getCallingUid());
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int getDistractingPackageRestrictions(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null) {
            return 0;
        }
        return packageState.getUserStateOrDefault(userId).getDistractionFlags();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int getPackageUid(java.lang.String packageName, long flags, int userId) {
        return snapshot().getPackageUidInternal(packageName, flags, userId, 1000);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, long flags, int filterCallingUid, int userId) {
        return snapshot().getApplicationInfoInternal(packageName, flags, filterCallingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName component, long flags, int filterCallingUid, int userId) {
        return snapshot().getActivityInfoInternal(component, flags, filterCallingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, java.lang.String resolvedType, long flags, int filterCallingUid, int userId) {
        return snapshot().queryIntentActivitiesInternal(intent, resolvedType, flags, filterCallingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentReceivers(android.content.Intent intent, java.lang.String resolvedType, long flags, int filterCallingUid, int callingPid, int userId, boolean forSend) {
        return getResolveIntentHelper().queryIntentReceiversInternal(snapshot(), intent, resolvedType, flags, userId, filterCallingUid, callingPid, forSend);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, long flags, int callingUid, int userId) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContext().getContentResolver());
        return snapshot().queryIntentServicesInternal(intent, resolvedType, flags, userId, callingUid, -1, false, false);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.ComponentName getHomeActivitiesAsUser(java.util.List<android.content.pm.ResolveInfo> allHomeCandidates, int userId) {
        return snapshot().getHomeActivitiesAsUser(allHomeCandidates, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.ComponentName getDefaultHomeActivity(int userId) {
        return snapshot().getDefaultHomeActivity(userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.ComponentName getSystemUiServiceComponent() {
        return android.content.ComponentName.unflattenFromString(getContext().getResources().getString(android.R.string.config_usbConfirmActivity));
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void setOwnerProtectedPackages(int userId, java.util.List<java.lang.String> packageNames) {
        getProtectedPackages().setOwnerProtectedPackages(userId, packageNames);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isPackageDataProtected(int userId, java.lang.String packageName) {
        return getProtectedPackages().isPackageDataProtected(userId, packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isPackageStateProtected(java.lang.String packageName, int userId) {
        return getProtectedPackages().isPackageStateProtected(userId, packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isPackageEphemeral(int userId, java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        return packageState != null && packageState.getUserStateOrDefault(userId).isInstantApp();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean wasPackageEverLaunched(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null) {
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        return !packageState.getUserStateOrDefault(userId).isNotLaunched();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isEnabledAndMatches(com.android.internal.pm.pkg.component.ParsedMainComponent component, long flags, int userId) {
        return com.android.server.pm.pkg.PackageStateUtils.isEnabledAndMatches(getPackageStateInternal(component.getPackageName()), component, flags, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean userNeedsBadging(int userId) {
        return getUserNeedsBadging().get(userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.lang.String getNameForUid(int uid) {
        return snapshot().getNameForUid(uid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void requestInstantAppResolutionPhaseTwo(android.content.pm.AuxiliaryResolveInfo responseObj, android.content.Intent origIntent, java.lang.String resolvedType, java.lang.String callingPackage, java.lang.String callingFeatureId, boolean isRequesterInstantApp, android.os.Bundle verificationBundle, int userId) {
        this.mService.requestInstantAppResolutionPhaseTwo(responseObj, origIntent, resolvedType, callingPackage, callingFeatureId, isRequesterInstantApp, verificationBundle, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void grantImplicitAccess(int userId, android.content.Intent intent, int recipientAppId, int visibleUid, boolean direct) {
        grantImplicitAccess(userId, intent, recipientAppId, visibleUid, direct, false);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void grantImplicitAccess(int userId, android.content.Intent intent, int recipientAppId, int visibleUid, boolean direct, boolean retainOnUpdate) {
        this.mService.grantImplicitAccess(snapshot(), userId, intent, recipientAppId, visibleUid, direct, retainOnUpdate);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isInstantAppInstallerComponent(android.content.ComponentName component) {
        android.content.pm.ActivityInfo instantAppInstallerActivity = this.mService.mInstantAppInstallerActivity;
        return instantAppInstallerActivity != null && instantAppInstallerActivity.getComponentName().equals(component);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void pruneInstantApps() throws java.lang.Throwable {
        getInstantAppRegistry().pruneInstantApps(snapshot());
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.lang.String getSetupWizardPackageName() {
        return this.mService.mSetupWizardPackage;
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, long flags, long privateResolveFlags, int userId, boolean resolveForStart, int filterCallingUid, int callingPid) {
        return getResolveIntentHelper().resolveIntentInternal(snapshot(), intent, resolvedType, flags, privateResolveFlags, userId, resolveForStart, filterCallingUid, callingPid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.ResolveInfo resolveService(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int callingUid) {
        return getResolveIntentHelper().resolveServiceInternal(snapshot(), intent, resolvedType, flags, userId, callingUid, -1, false);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.ResolveInfo resolveService(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int callingUid, int callingPid) {
        return getResolveIntentHelper().resolveServiceInternal(snapshot(), intent, resolvedType, flags, userId, callingUid, callingPid, true);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.content.pm.ProviderInfo resolveContentProvider(java.lang.String name, long flags, int userId, int callingUid) {
        return snapshot().resolveContentProvider(name, flags, userId, callingUid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int getUidTargetSdkVersion(int uid) {
        return snapshot().getUidTargetSdkVersion(uid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int getPackageTargetSdkVersion(java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState != null && packageState.getPkg() != null) {
            return packageState.getPkg().getTargetSdkVersion();
        }
        return 10000;
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean canAccessInstantApps(int callingUid, int userId) {
        return snapshot().canViewInstantApps(callingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean canAccessComponent(int callingUid, android.content.ComponentName component, int userId) {
        return snapshot().canAccessComponent(callingUid, component, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean hasInstantApplicationMetadata(java.lang.String packageName, int userId) {
        return getInstantAppRegistry().hasInstantApplicationMetadata(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.util.SparseArray<java.lang.String> getAppsWithSharedUserIds() {
        return snapshot().getAppsWithSharedUserIds();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.lang.String[] getSharedUserPackagesForPackage(java.lang.String packageName, int userId) {
        return snapshot().getSharedUserPackagesForPackage(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> getProcessesForUid(int uid) {
        return snapshot().getProcessesForUid(uid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int[] getPermissionGids(java.lang.String permissionName, int userId) {
        return getPermissionManager().getPermissionGids(permissionName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void freeStorage(java.lang.String volumeUuid, long bytes, int flags) throws java.io.IOException {
        this.mService.freeStorage(volumeUuid, bytes, flags);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void freeAllAppCacheAboveQuota(java.lang.String volumeUuid) throws java.io.IOException {
        this.mService.freeAllAppCacheAboveQuota(volumeUuid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void forEachPackageSetting(java.util.function.Consumer<com.android.server.pm.PackageSetting> actionLocked) {
        this.mService.forEachPackageSetting(actionLocked);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void forEachPackageState(java.util.function.Consumer<com.android.server.pm.pkg.PackageStateInternal> action) {
        this.mService.forEachPackageState(snapshot(), action);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void forEachPackage(java.util.function.Consumer<com.android.server.pm.pkg.AndroidPackage> action) {
        this.mService.forEachPackage(snapshot(), action);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void forEachInstalledPackage(java.util.function.Consumer<com.android.server.pm.pkg.AndroidPackage> action, int userId) {
        this.mService.forEachInstalledPackage(snapshot(), action, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.util.ArraySet<java.lang.String> getEnabledComponents(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null) {
            return new android.util.ArraySet<>();
        }
        return packageState.getUserStateOrDefault(userId).m8025getEnabledComponents();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final android.util.ArraySet<java.lang.String> getDisabledComponents(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null) {
            return new android.util.ArraySet<>();
        }
        return packageState.getUserStateOrDefault(userId).m8024getDisabledComponents();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int getApplicationEnabledState(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.PackageStateInternal packageState = getPackageStateInternal(packageName);
        if (packageState == null) {
            return 0;
        }
        return packageState.getUserStateOrDefault(userId).getEnabledState();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final int getComponentEnabledSetting(android.content.ComponentName componentName, int callingUid, int userId) {
        return snapshot().getComponentEnabledSettingInternal(componentName, callingUid, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void setEnableRollbackCode(int token, int enableRollbackCode) {
        this.mService.setEnableRollbackCode(token, enableRollbackCode);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void finishPackageInstall(int token, boolean didLaunch) {
        this.mService.finishPackageInstall(token, didLaunch);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isApexPackage(java.lang.String packageName) {
        return snapshot().isApexPackage(packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<java.lang.String> getApksInApex(java.lang.String apexPackageName) {
        return getApexManager().getApksInApex(apexPackageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isCallerInstallerOfRecord(com.android.server.pm.pkg.AndroidPackage pkg, int callingUid) {
        return snapshot().isCallerInstallerOfRecord(pkg, callingUid);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final java.util.List<java.lang.String> getMimeGroup(java.lang.String packageName, java.lang.String mimeGroup) {
        return this.mService.getMimeGroupInternal(snapshot(), packageName, mimeGroup);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isSystemPackage(java.lang.String packageName) {
        return packageName.equals(this.mService.ensureSystemPackageName(snapshot(), packageName));
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void unsuspendAdminSuspendedPackages(int affectedUser) {
        int suspendingUserId = android.app.admin.flags.Flags.crossUserSuspensionEnabledRo() ? 0 : affectedUser;
        this.mService.unsuspendForSuspendingPackage(snapshot(), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, suspendingUserId, false);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isAdminSuspendingAnyPackages(int userId) {
        int suspendingUserId = android.app.admin.flags.Flags.crossUserSuspensionEnabledRo() ? 0 : userId;
        return snapshot().isSuspendingAnyPackages(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, suspendingUserId, userId);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void requestChecksums(java.lang.String packageName, boolean includeSplits, int optional, int required, java.util.List trustedInstallers, android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener, int userId, java.util.concurrent.Executor executor, android.os.Handler handler) throws android.os.ParcelableException {
        this.mService.requestChecksumsInternal(snapshot(), packageName, includeSplits, optional, required, trustedInstallers, onChecksumsReadyListener, userId, executor, handler);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final boolean isPackageFrozen(java.lang.String packageName, int callingUid, int userId) {
        return snapshot().getPackageStartability(this.mService.getSafeMode(), packageName, callingUid, userId) == 3;
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final long deleteOatArtifactsOfPackage(java.lang.String packageName) {
        return this.mService.deleteOatArtifactsOfPackage(snapshot(), packageName);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void reconcileAppsData(int userId, int flags, boolean migrateAppsData) {
        getAppDataHelper().reconcileAppsData(userId, flags, migrateAppsData);
    }

    @Override // android.content.pm.PackageManagerInternal
    public android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> getSharedUserPackages(int sharedUserAppId) {
        return snapshot().getSharedUserPackages(sharedUserAppId);
    }

    @Override // android.content.pm.PackageManagerInternal
    public com.android.server.pm.pkg.SharedUserApi getSharedUserApi(int sharedUserAppId) {
        return snapshot().getSharedUser(sharedUserAppId);
    }

    @Override // android.content.pm.PackageManagerInternal
    public boolean isUidPrivileged(int uid) {
        return snapshot().isUidPrivileged(uid);
    }

    @Override // android.content.pm.PackageManagerInternal
    public int checkUidSignaturesForAllUsers(int uid1, int uid2) {
        return snapshot().checkUidSignaturesForAllUsers(uid1, uid2);
    }

    @Override // android.content.pm.PackageManagerInternal
    public void setPackageStoppedState(java.lang.String packageName, boolean stopped, int userId) throws java.lang.Throwable {
        this.mService.setPackageStoppedState(snapshot(), packageName, stopped, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    public void notifyComponentUsed(java.lang.String packageName, int userId, java.lang.String recentCallingPackage, java.lang.String debugInfo) throws java.lang.Throwable {
        this.mService.notifyComponentUsed(snapshot(), packageName, userId, recentCallingPackage, debugInfo);
    }

    @Override // android.content.pm.PackageManagerInternal
    public boolean isPackageQuarantined(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        return snapshot().isPackageQuarantinedForUser(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    public boolean isPackageStopped(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        return snapshot().isPackageStoppedForUser(packageName, userId);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState recordInitialState() {
        return this.mService.recordInitialState();
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final com.android.server.pm.pkg.mutate.PackageStateMutator.Result commitPackageStateMutation(com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState state, java.util.function.Consumer<com.android.server.pm.pkg.mutate.PackageStateMutator> consumer) {
        return this.mService.commitPackageStateMutation(state, consumer);
    }

    @Override // android.content.pm.PackageManagerInternal
    @java.lang.Deprecated
    public final void shutdown() {
        this.mService.shutdown();
    }
}
