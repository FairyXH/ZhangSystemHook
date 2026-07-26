package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface Computer extends com.android.server.pm.snapshot.PackageDataSnapshot {
    boolean activitySupportsIntentAsUser(android.content.ComponentName componentName, android.content.ComponentName componentName2, android.content.Intent intent, java.lang.String str, int i);

    java.util.List<android.content.pm.ResolveInfo> applyPostResolutionFilter(java.util.List<android.content.pm.ResolveInfo> list, java.lang.String str, boolean z, int i, boolean z2, int i2, android.content.Intent intent);

    boolean canAccessComponent(int i, android.content.ComponentName componentName, int i2);

    boolean canForwardTo(android.content.Intent intent, java.lang.String str, int i, int i2);

    boolean[] canPackageQuery(java.lang.String str, java.lang.String[] strArr, int i);

    boolean canQueryPackage(int i, java.lang.String str);

    boolean canRequestPackageInstalls(java.lang.String str, int i, int i2, boolean z);

    boolean canViewInstantApps(int i, int i2);

    java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] strArr);

    void checkPackageFrozen(java.lang.String str);

    int checkSignatures(java.lang.String str, java.lang.String str2, int i);

    int checkUidPermission(java.lang.String str, int i);

    int checkUidSignatures(int i, int i2);

    int checkUidSignaturesForAllUsers(int i, int i2);

    android.content.pm.ResolveInfo createForwardingResolveInfoUnchecked(com.android.server.pm.WatchedIntentFilter watchedIntentFilter, int i, int i2);

    java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] strArr);

    void dump(int i, java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState);

    void dumpKeySet(java.io.PrintWriter printWriter, java.lang.String str, com.android.server.pm.DumpState dumpState);

    void dumpPackages(java.io.PrintWriter printWriter, java.lang.String str, android.util.ArraySet<java.lang.String> arraySet, com.android.server.pm.DumpState dumpState, boolean z);

    void dumpPackagesProto(android.util.proto.ProtoOutputStream protoOutputStream);

    void dumpPermissions(java.io.PrintWriter printWriter, java.lang.String str, android.util.ArraySet<java.lang.String> arraySet, com.android.server.pm.DumpState dumpState);

    void dumpSharedLibrariesProto(android.util.proto.ProtoOutputStream protoOutputStream);

    void dumpSharedUsers(java.io.PrintWriter printWriter, java.lang.String str, android.util.ArraySet<java.lang.String> arraySet, com.android.server.pm.DumpState dumpState, boolean z);

    void dumpSharedUsersProto(android.util.proto.ProtoOutputStream protoOutputStream);

    void enforceCrossUserOrProfilePermission(int i, int i2, boolean z, boolean z2, java.lang.String str);

    void enforceCrossUserPermission(int i, int i2, boolean z, boolean z2, java.lang.String str);

    void enforceCrossUserPermission(int i, int i2, boolean z, boolean z2, boolean z3, java.lang.String str);

    boolean filterAppAccess(int i, int i2);

    boolean filterAppAccess(com.android.server.pm.pkg.AndroidPackage androidPackage, int i, int i2);

    boolean filterAppAccess(java.lang.String str, int i, int i2, boolean z);

    java.lang.String[] filterOnlySystemPackages(java.lang.String... strArr);

    boolean filterSharedLibPackage(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i, int i2, long j);

    android.content.pm.ResolveInfo findPersistentPreferredActivity(android.content.Intent intent, java.lang.String str, long j, java.util.List<android.content.pm.ResolveInfo> list, boolean z, int i);

    com.android.server.pm.PackageManagerService.FindPreferredActivityBodyResult findPreferredActivityInternal(android.content.Intent intent, java.lang.String str, long j, java.util.List<android.content.pm.ResolveInfo> list, boolean z, boolean z2, boolean z3, int i, boolean z4);

    java.util.List<com.android.server.pm.pkg.PackageStateInternal> findSharedNonSystemLibraries(com.android.server.pm.pkg.PackageStateInternal packageStateInternal);

    android.content.pm.ApplicationInfo generateApplicationInfoFromSettings(java.lang.String str, long j, int i, int i2);

    android.content.pm.PackageInfo generatePackageInfo(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, long j, int i);

    android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName componentName, long j, int i);

    android.content.pm.ActivityInfo getActivityInfoCrossProfile(android.content.ComponentName componentName, long j, int i);

    android.content.pm.ActivityInfo getActivityInfoInternal(android.content.ComponentName componentName, long j, int i, int i2);

    java.lang.String[] getAllAvailablePackageNames();

    android.content.pm.ParceledListSlice<android.content.IntentFilter> getAllIntentFilters(java.lang.String str);

    java.util.List<java.lang.String> getAllPackages();

    java.lang.String[] getAppOpPermissionPackages(java.lang.String str, int i);

    int getApplicationEnabledSetting(java.lang.String str, int i);

    boolean getApplicationHiddenSettingAsUser(java.lang.String str, int i);

    android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String str, long j, int i);

    android.content.pm.ApplicationInfo getApplicationInfoInternal(java.lang.String str, long j, int i, int i2);

    android.util.SparseArray<java.lang.String> getAppsWithSharedUserIds();

    boolean getBlockUninstall(int i, java.lang.String str);

    boolean getBlockUninstallForUser(java.lang.String str, int i);

    int getComponentEnabledSetting(android.content.ComponentName componentName, int i, int i2);

    int getComponentEnabledSettingInternal(android.content.ComponentName componentName, int i, int i2);

    com.android.server.pm.resolution.ComponentResolverApi getComponentResolver();

    com.android.server.pm.CrossProfileDomainInfo getCrossProfileDomainPreferredLpr(android.content.Intent intent, java.lang.String str, long j, int i, int i2);

    android.content.pm.ParceledListSlice<android.content.pm.SharedLibraryInfo> getDeclaredSharedLibraries(java.lang.String str, long j, int i);

    android.content.ComponentName getDefaultHomeActivity(int i);

    com.android.server.pm.pkg.PackageStateInternal getDisabledSystemPackage(java.lang.String str);

    android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getDisabledSystemPackageStates();

    int getFlagsForUid(int i);

    com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.Integer> getFrozenPackages();

    android.content.pm.ProviderInfo getGrantImplicitAccessProviderInfo(int i, java.lang.String str);

    java.lang.CharSequence getHarmfulAppWarning(java.lang.String str, int i);

    android.content.ComponentName getHomeActivitiesAsUser(java.util.List<android.content.pm.ResolveInfo> list, int i);

    android.content.Intent getHomeIntent();

    int getInstallReason(java.lang.String str, int i);

    android.content.pm.InstallSourceInfo getInstallSourceInfo(java.lang.String str, int i);

    java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(long j, int i, int i2, boolean z);

    android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getInstalledPackages(long j, int i);

    java.lang.String getInstallerPackageName(java.lang.String str, int i);

    android.content.ComponentName getInstantAppInstallerComponent();

    android.content.pm.ResolveInfo getInstantAppInstallerInfo();

    java.lang.String getInstantAppPackageName(int i);

    android.content.pm.InstrumentationInfo getInstrumentationInfoAsUser(android.content.ComponentName componentName, int i, int i2);

    android.content.pm.KeySet getKeySetByAlias(java.lang.String str, java.lang.String str2);

    java.util.List<com.android.server.pm.CrossProfileIntentFilter> getMatchingCrossProfileIntentFilters(android.content.Intent intent, java.lang.String str, int i);

    java.lang.String getNameForUid(int i);

    java.lang.String[] getNamesForUids(int[] iArr);

    android.util.ArraySet<java.lang.String> getNotifyPackagesForReplacedReceived(java.lang.String[] strArr);

    com.android.server.pm.pkg.AndroidPackage getPackage(int i);

    com.android.server.pm.pkg.AndroidPackage getPackage(java.lang.String str);

    int[] getPackageGids(java.lang.String str, long j, int i);

    android.content.pm.PackageInfo getPackageInfo(java.lang.String str, long j, int i);

    android.content.pm.PackageInfo getPackageInfoInternal(java.lang.String str, long j, long j2, int i, int i2);

    android.util.Pair<com.android.server.pm.pkg.PackageStateInternal, com.android.server.pm.pkg.SharedUserApi> getPackageOrSharedUser(int i);

    int getPackageStartability(boolean z, java.lang.String str, int i, int i2);

    com.android.server.pm.pkg.PackageStateInternal getPackageStateFiltered(java.lang.String str, int i, int i2);

    com.android.server.pm.pkg.PackageStateInternal getPackageStateForInstalledAndFiltered(java.lang.String str, int i, int i2);

    com.android.server.pm.pkg.PackageStateInternal getPackageStateInternal(java.lang.String str);

    com.android.server.pm.pkg.PackageStateInternal getPackageStateInternal(java.lang.String str, int i);

    android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> getPackageStates();

    int getPackageUid(java.lang.String str, long j, int i);

    int getPackageUidInternal(java.lang.String str, long j, int i, int i2);

    java.util.List<com.android.server.pm.pkg.AndroidPackage> getPackagesForAppId(int i);

    java.lang.String[] getPackagesForUid(int i);

    android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] strArr, long j, int i);

    android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> getPackagesUsingSharedLibrary(android.content.pm.SharedLibraryInfo sharedLibraryInfo, long j, int i, int i2);

    java.util.List<android.content.pm.ApplicationInfo> getPersistentApplications(boolean z, int i);

    com.android.server.pm.PreferredIntentResolver getPreferredActivities(int i);

    int getPrivateFlagsForUid(int i);

    android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> getProcessesForUid(int i);

    android.content.pm.UserInfo getProfileParent(int i);

    android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName componentName, long j, int i);

    android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName componentName, long j, int i);

    java.lang.String getRenamedPackage(java.lang.String str);

    android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName componentName, long j, int i);

    android.content.pm.ParceledListSlice<android.content.pm.SharedLibraryInfo> getSharedLibraries(java.lang.String str, long j, int i);

    com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> getSharedLibraries();

    android.content.pm.SharedLibraryInfo getSharedLibraryInfo(java.lang.String str, long j);

    com.android.server.pm.pkg.SharedUserApi getSharedUser(int i);

    android.util.ArraySet<com.android.server.pm.pkg.PackageStateInternal> getSharedUserPackages(int i);

    java.lang.String[] getSharedUserPackagesForPackage(java.lang.String str, int i);

    android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.SharedUserApi> getSharedUsers();

    android.content.pm.SigningDetails getSigningDetails(int i);

    android.content.pm.SigningDetails getSigningDetails(java.lang.String str);

    android.content.pm.KeySet getSigningKeySet(java.lang.String str);

    android.util.ArrayMap<java.lang.String, java.lang.String> getSystemSharedLibraryNamesAndPaths();

    int getTargetSdkVersion(java.lang.String str);

    int getUidForSharedUser(java.lang.String str);

    int getUidTargetSdkVersion(int i);

    java.util.Set<java.lang.String> getUnusedPackages(long j);

    android.content.pm.UserInfo[] getUserInfos();

    int getVersion();

    int[] getVisibilityAllowList(java.lang.String str, int i);

    android.util.SparseArray<int[]> getVisibilityAllowLists(java.lang.String str, int[] iArr);

    java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> getVolumePackages(java.lang.String str);

    boolean hasSigningCertificate(java.lang.String str, byte[] bArr, int i);

    boolean hasUidSigningCertificate(int i, byte[] bArr, int i2);

    boolean isApexPackage(java.lang.String str);

    boolean isApplicationEffectivelyEnabled(java.lang.String str, android.os.UserHandle userHandle);

    boolean isCallerInstallerOfRecord(com.android.server.pm.pkg.AndroidPackage androidPackage, int i);

    boolean isCallerSameApp(java.lang.String str, int i);

    boolean isCallerSameApp(java.lang.String str, int i, boolean z);

    boolean isComponentEffectivelyEnabled(android.content.pm.ComponentInfo componentInfo, android.os.UserHandle userHandle);

    boolean isComponentVisibleToInstantApp(android.content.ComponentName componentName);

    boolean isComponentVisibleToInstantApp(android.content.ComponentName componentName, int i);

    boolean isImplicitImageCaptureIntentAndNotSetByDpc(android.content.Intent intent, int i, java.lang.String str, long j);

    boolean isInstallDisabledForPackage(java.lang.String str, int i, int i2);

    boolean isInstantApp(java.lang.String str, int i);

    boolean isInstantAppInternal(java.lang.String str, int i, int i2);

    boolean isPackageAvailable(java.lang.String str, int i);

    boolean isPackageQuarantinedForUser(java.lang.String str, int i) throws android.content.pm.PackageManager.NameNotFoundException;

    boolean isPackageSignedByKeySet(java.lang.String str, android.content.pm.KeySet keySet);

    boolean isPackageSignedByKeySetExactly(java.lang.String str, android.content.pm.KeySet keySet);

    boolean isPackageStoppedForUser(java.lang.String str, int i) throws android.content.pm.PackageManager.NameNotFoundException;

    boolean isPackageSuspendedForUser(java.lang.String str, int i) throws android.content.pm.PackageManager.NameNotFoundException;

    boolean isSameProfileGroup(int i, int i2);

    boolean isSuspendingAnyPackages(java.lang.String str, int i, int i2);

    boolean isUidPrivileged(int i);

    android.content.pm.ParceledListSlice<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String str, int i, long j, java.lang.String str2);

    android.content.pm.ParceledListSlice<android.content.pm.InstrumentationInfo> queryInstrumentationAsUser(java.lang.String str, int i, int i2);

    java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesInternal(android.content.Intent intent, java.lang.String str, long j, int i);

    java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesInternal(android.content.Intent intent, java.lang.String str, long j, int i, int i2);

    java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesInternal(android.content.Intent intent, java.lang.String str, long j, long j2, int i, int i2, int i3, boolean z, boolean z2);

    com.android.server.pm.QueryIntentActivitiesResult queryIntentActivitiesInternalBody(android.content.Intent intent, java.lang.String str, long j, int i, int i2, boolean z, boolean z2, java.lang.String str2, java.lang.String str3);

    java.util.List<android.content.pm.ResolveInfo> queryIntentServicesInternal(android.content.Intent intent, java.lang.String str, long j, int i, int i2, int i3, boolean z, boolean z2);

    void querySyncProviders(boolean z, java.util.List<java.lang.String> list, java.util.List<android.content.pm.ProviderInfo> list2);

    android.content.pm.ProviderInfo resolveContentProvider(java.lang.String str, long j, int i, int i2);

    java.lang.String resolveExternalPackageName(com.android.server.pm.pkg.AndroidPackage androidPackage);

    java.lang.String resolveInternalPackageName(java.lang.String str, long j);

    boolean shouldFilterApplication(com.android.server.pm.SharedUserSetting sharedUserSetting, int i, int i2);

    boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i, int i2);

    boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i, android.content.ComponentName componentName, int i2, int i3);

    boolean shouldFilterApplication(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i, android.content.ComponentName componentName, int i2, int i3, boolean z);

    boolean shouldFilterApplicationIncludingUninstalled(com.android.server.pm.SharedUserSetting sharedUserSetting, int i, int i2);

    boolean shouldFilterApplicationIncludingUninstalled(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i, int i2);

    boolean shouldFilterApplicationIncludingUninstalledNotArchived(com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i, int i2);

    long updateFlagsForApplication(long j, int i);

    long updateFlagsForComponent(long j, int i);

    long updateFlagsForPackage(long j, int i);

    long updateFlagsForResolve(long j, int i, int i2, boolean z, boolean z2);

    long updateFlagsForResolve(long j, int i, int i2, boolean z, boolean z2, boolean z3);

    com.android.server.pm.Computer use();

    default int getUsed() {
        return 0;
    }
}
