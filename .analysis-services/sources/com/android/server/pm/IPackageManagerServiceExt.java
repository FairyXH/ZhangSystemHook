package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerServiceExt {
    public static final boolean DEBUG_PMS = true;

    default long adjustFlagsInComputerEngineQIAI(long flags) {
        return flags;
    }

    default java.util.List<android.content.pm.ResolveInfo> removeRepeatedResolveInfos(java.util.List<android.content.pm.ResolveInfo> result, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileResults) {
        return result;
    }

    default java.util.List<android.content.pm.ResolveInfo> adjustResultForNoPkgNameInComputerEngineQIAIB(java.util.List<android.content.pm.ResolveInfo> result, com.android.server.pm.Computer computerEngine, com.android.server.pm.resolution.ComponentResolverApi resolver, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int filterCallingUid) {
        return result;
    }

    default java.util.List<android.content.pm.ResolveInfo> adjustResultForHasPkgNameInComputerEngineQIAIB(java.util.List<android.content.pm.ResolveInfo> result, com.android.server.pm.Computer computerEngine, com.android.server.pm.resolution.ComponentResolverApi resolver, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId, int filterCallingUid, com.android.server.pm.pkg.PackageStateInternal setting) {
        return result;
    }

    default java.util.List<android.content.pm.ResolveInfo> adjustResultInComputerEngineQISIB(java.util.List<android.content.pm.ResolveInfo> result, java.util.List<android.content.pm.ResolveInfo> systemResult) {
        return result;
    }

    default boolean hookBeforeQueryIntentServicesInternalBody(int userId, int filterCallingUid, com.android.server.pm.Computer computerEngine, android.content.Intent intent) {
        return false;
    }

    default long adjustFlagsInComputerEngineGAIIB(long flags) {
        return flags;
    }

    default int adjustUserIdInComputerEngineGAIIB(com.android.server.pm.Computer computerEngine, int userId, com.android.internal.pm.pkg.component.ParsedActivity a, com.android.server.pm.Settings settings, com.android.server.pm.pkg.AndroidPackage pkg, android.content.ComponentName component, long flags, int filterCallingUid) {
        return userId;
    }

    default android.content.pm.ApplicationInfo adjustAiInComputerEngineGAppIIB(android.content.pm.ApplicationInfo ai, com.android.server.pm.pkg.AndroidPackage androidPackage, com.android.server.pm.pkg.PackageStateInternal ps, java.lang.String packageName, long flags, int userId) {
        return ai;
    }

    default java.util.List<android.content.pm.ResolveInfo> adjustResultInComputerEngineMAIAI(java.util.List<android.content.pm.ResolveInfo> result) {
        return result;
    }

    default android.content.pm.PackageInfo adjustResultForHasPkgInComputerEngineGPIIB(android.content.pm.PackageInfo result, java.lang.Object computerEngine, com.android.server.pm.pkg.PackageStateInternal ps, java.lang.String packageName, long flags, int userId, int filterCallingUid) {
        return result;
    }

    default android.content.pm.PackageInfo adjustResultAtEndInComputerEngineGPIIB(android.content.pm.PackageInfo result, java.lang.String packageName) {
        return result;
    }

    default boolean judgeIsSystemAppCallInComputerEngineGIPB() {
        return false;
    }

    default boolean filterPackageInComputerEngineGIPB(java.lang.String packageName, boolean isSystemAppCall) {
        return false;
    }

    default android.content.pm.ServiceInfo adjustResultAtEndInComputerEngineGSIB(android.content.pm.ServiceInfo result, com.android.server.pm.Computer computerEngine, com.android.server.pm.Settings settings, com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedService service, android.content.ComponentName component, long flags, int userId, int callingUid) {
        return result;
    }

    default void afterHandlePackagePostInstallInCasePostInstall(com.android.server.pm.InstallRequest request) {
    }

    default void afterSetVerifyFailInCasePackageVerified(com.android.server.pm.VerifyingSession params) {
    }

    default void customHandleMsgInPackageHandler(android.os.Message msg) {
    }

    default void afterPmsStartEventInConstructor() {
    }

    default void beforeCreateSubComponentsInConstructor() {
    }

    default void initOplusBinderExtensionInConstructor(android.os.Binder binder) {
    }

    default boolean ignoreChangeInPackageParserCallback(long changeId, android.content.pm.ApplicationInfo appInfo) {
        return false;
    }

    default void beforeAddSharedUsersInConstructor() {
    }

    default void afterGetSystemConfigInConstructor() {
    }

    default void onStartLockedWorkInConstructor() {
    }

    default void afterReadUserSettingsInConstructor() {
    }

    default void afterPmsSystemScanStartEventInConstructor() {
    }

    default boolean adjustIsUpgradeFlag(boolean result) {
        return result;
    }

    default void afterCalculateUpgradeFlagInConstructor(com.android.server.pm.Settings.VersionInfo internalVer) {
    }

    default void afterFrameworksPackageScannedInConstructor(int systemParseFlags, int systemScanFlags, com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService) {
    }

    default void beforeCheckSystemAppScannedInConstructor() {
    }

    default void onSystemAppNotExistCheckedInConstructor(com.android.server.pm.PackageSetting ps) {
    }

    default void afterCheckSystemAppScannedInConstructor() {
    }

    default void beforeScanDataDirInConstructor() {
    }

    default int adjustScanFlagsForDataDir(int scanFlags) {
        return scanFlags;
    }

    default void afterScanDataDirInConstructor() {
    }

    default void beforeInstallSystemStubPackagesInConstructor() {
    }

    default void onPackagePrepareFinishedInConstructor() {
    }

    default void beforeRecordScanEndInConstructor() {
    }

    default void afterPmsScanEndEventInConstructor() {
    }

    default boolean shouldReconcileAppsDataInConstructor(com.android.server.pm.PackageManagerService service) {
        return true;
    }

    default void onPrepareAppDataFutureEndByNoDefer() {
    }

    default void onPrepareAppDataFutureEndByDeferDone(int storageFlags) {
    }

    default void beforeWriteSettingsInConstructor() {
    }

    default void afterPmsReadyEventInConstructor() {
    }

    default void onEndLockedWorkInConstructor() {
    }

    default void readAbiInfoAfterScanEnd(java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> settingMap) {
    }

    default void beforeScanInScanDirLI() {
    }

    default boolean interceptUseParseResultWithoutThrowInScanDirLI(com.android.server.pm.ParallelPackageParser.ParseResult parseResult, int scanFlags) {
        return false;
    }

    default boolean interceptUseParseResultWithoutThrowInScanDirLI2(com.android.server.pm.ParallelPackageParser.ParseResult parseResult, int scanFlags, int parseFlags, java.io.File scanDir) {
        return false;
    }

    default void handleExpOfAddForInitInScanDirLI(com.android.server.pm.PackageManagerService pms, com.android.server.pm.ParallelPackageParser.ParseResult parseResult, java.io.File file, int scanFlags, int parseFlags, com.android.server.pm.ApexManager.ActiveApexInfo activeApexInfo) {
    }

    default boolean skipDeleteDataAppWhenFailedInScanDirLI(com.android.server.pm.PackageManagerService pms) {
        return false;
    }

    default boolean shouldUseLiveComputerInSnapshotComputer() {
        return false;
    }

    default void afterSendPackageAddedForAllInHPPI(com.android.server.pm.InstallRequest request) {
    }

    default void handleSuccessAtEndInHPPI(android.content.Context context, com.android.server.pm.pkg.AndroidPackage res, java.lang.String packageName, com.android.server.pm.InstallSource installSource, boolean update, int[] updateUserIds) {
    }

    default void onNotifyInstallObserver(java.lang.String packageName, int ret) {
    }

    default boolean isHoldingLockInFindPreferredActivityNotLocked() {
        return false;
    }

    default boolean isDefaultAppPolicyEnabledInFPANL(android.content.Intent intent) {
        return false;
    }

    default boolean skipMatchCheckInFPANL(boolean isDefaultAppPolicyEnabled, java.util.List<android.content.pm.ResolveInfo> query) {
        return false;
    }

    default boolean shouldSkipReturnInFPANL(boolean isDefaultAppPolicyEnabled) {
        return false;
    }

    default int calculateDelayRemoveIndex(int delayRemoveIndex, int currentIndex) {
        return delayRemoveIndex;
    }

    default boolean doDelayedRemoveInFPANL(com.android.server.pm.Computer computerEngine, int delayRemoveIndex, com.android.server.pm.PreferredIntentResolver pir, java.util.List<com.android.server.pm.PreferredActivity> prefs, android.content.Intent intent, java.lang.String resolvedType) {
        return false;
    }

    default boolean judgeIsSystemAppCallInGIALI() {
        return false;
    }

    default boolean filterApplicationInfoInGIALI(java.lang.String packageName, boolean isSystemAppCall) {
        return false;
    }

    default void beforeQueryInResolveContentProviderInternal(com.android.server.pm.PackageManagerService service, java.lang.String authority, int userId) {
    }

    default android.content.pm.ProviderInfo adjustProviderInfoInRCPI(com.android.server.pm.Computer computerEngine, android.content.pm.ProviderInfo providerInfo, com.android.server.pm.resolution.ComponentResolverApi resolver, java.lang.String name, long flags, int userId) {
        return providerInfo;
    }

    default int adjustUserIdWithProviderInfoInRCPI(int userId) {
        return userId;
    }

    default java.lang.String adjustUriUserIdInRCPI(java.lang.String authority, int userId) {
        return authority;
    }

    default boolean shouldUseCustomScanDirLI() {
        return false;
    }

    default void customScanDirLI(java.io.File scanDir, int parseFlags, int scanFlags, long currentTime, com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService, com.android.server.pm.ApexManager.ActiveApexInfo apexInfo) {
    }

    default void customScanRemovableDir(int parseFlags, int scanFlags, com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService, com.android.server.pm.ApexManager.ActiveApexInfo apexInfo) {
    }

    default void deleteRemovableAppResources(java.lang.String packageName, com.android.server.pm.pkg.AndroidPackage pkg) {
    }

    default void deleteRemovableAppResources(java.lang.String packageName, java.lang.String libraryDir) {
    }

    default boolean skipSigCheckWhenDataToSystemInAddForInitLI(com.android.server.pm.pkg.AndroidPackage pkg, boolean newPkgVersionGreater, int internalSdkVersion) {
        return false;
    }

    default void beforeReturnInAddForInitLI(com.android.server.pm.ScanResult scanResult) {
    }

    default void afterPerformDexOptUpgradeInUpdatePackagesIfNeeded() {
    }

    default boolean interceptPerformDexOptSecondary(java.lang.String packageName, java.lang.String compilerFilter, boolean force) {
        return false;
    }

    default boolean skipDestroyAppDataInDestroyAppDataLeafLIF2(java.lang.String volumeUuid, java.lang.String packageName, int realUserId, int flags, long ceDataInode) {
        return false;
    }

    default boolean allowUnknownWhenScanRequireKnownInAssertPackageIsValid(com.android.server.pm.pkg.AndroidPackage pkg) {
        return false;
    }

    default boolean interceptHideInSetApplicationHiddenSettingAsUser(boolean hidden, java.lang.String packageName) {
        return false;
    }

    default boolean shouldSetInstallSettingInInstallExistingPackageAsUser(com.android.server.pm.PackageSetting pkgSetting, java.lang.String packageName, int userId) {
        return false;
    }

    default void beforePrepareAppDataInInstallExistingPackageAsUser(java.lang.String packageName, int userId, int appId) {
    }

    default void afterDoPostInstallInProcessInstallRequestsAsync(com.android.server.pm.InstallArgs args, android.os.Handler packageHandler, java.lang.String packageName, int ret) {
    }

    default void onStartInRestoreAndPostInstall(com.android.server.pm.InstallRequest res) {
    }

    default int preSetRetInOverrideInstallLocation(java.lang.String packageName) {
        return 1;
    }

    default com.android.server.pm.InstallArgs modifyInstallArgsInProcessPendingInstall(com.android.server.pm.InstallArgs args, com.android.server.pm.IInstallParamsExt installParamsExt, android.content.pm.parsing.PackageLite pkgLite) {
        return args;
    }

    default int modifyRetInHandleStartCopyOfVerificationParams(int ret, com.android.server.pm.InstallSource installSource, android.content.pm.PackageInfoLite pkgLite, android.content.pm.IPackageInstallObserver2 observer) {
        return ret;
    }

    default void beforeFailReturnInHandleStartCopyOfVerificationParams(int ret, android.content.pm.PackageInfoLite pkgLite, com.android.server.pm.InstallSource installSource, int userId) {
    }

    default boolean skipVerifyInSendPackageVerificationRequest(java.util.List<java.lang.String> verifierPackages, com.android.server.pm.InstallSource installSource) {
        return false;
    }

    default void afterNotifyUpdateForDexInExecutePostCommitSteps(com.android.server.pm.pkg.AndroidPackage pkg) {
    }

    default boolean doPreWorkBeforeDexOptInExecutePostCommitSteps(com.android.server.pm.pkg.AndroidPackage pkg) {
        return false;
    }

    default void afterInitializeArtManagerLocal(android.content.Context systemContext) {
    }

    default int dexoptInPerformDexOptWithArtService(com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot, java.lang.String pkgName, com.android.server.art.model.DexoptParams params) {
        return 0;
    }

    default void afterDexOptInExecutePostCommitSteps(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String packageName, boolean didPreWork) {
    }

    default void afterPackageParsedInPreparePackageLI(android.content.Context context, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
    }

    default java.lang.Exception interceptWithoutSigInPreparePackageLI(android.content.Context context, com.android.server.pm.InstallRequest res, java.lang.String pkgName) {
        return null;
    }

    default java.lang.Exception interceptWithSigInPreparePackageLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, java.lang.String pkgName, com.android.server.pm.InstallArgs args, int installFlags) {
        return null;
    }

    default boolean allowPersistentUpdateInPreparePackageLI() {
        return false;
    }

    default boolean allowDuplicatedPermInPreparePackageLI(com.android.server.pm.InstallArgs args, java.lang.String sourcePackageName) {
        return false;
    }

    default void customLogDuplicatedPermDeclared(android.content.Context context, java.lang.String sourcePackageName, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, com.android.internal.pm.pkg.component.ParsedPermission perm) {
    }

    default java.lang.Exception interceptSystemAppInPreparePackageLI(boolean replace, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        return null;
    }

    default void onHandleForNotMoveInPreparePackageLI(com.android.server.pm.InstallArgs args, int installFlags) {
    }

    default void beforePrepareForReplaceInPreparePackageLI(com.android.server.pm.pkg.AndroidPackage oldPkg) {
    }

    default int adjustDeleteFlagInDeleteExistingPackageAsUser(int deleteFlag, android.content.pm.VersionedPackage versionedPackage) {
        return deleteFlag;
    }

    default void onStartInDeletePackageVersionedInternal(java.lang.String packageName) {
    }

    default boolean interceptDeleteInDeletePackageVersionedInternal(android.content.Context context, java.lang.String packageName, int userId, int callingUid, android.os.Handler packageHandler, android.content.pm.IPackageDeleteObserver2 observer, android.content.pm.VersionedPackage versionedPackage) {
        return false;
    }

    default void beforePostDeleteInDeletePackageVersionedInternal(android.content.pm.VersionedPackage versionedPackage, int userId, java.lang.String packageName) {
    }

    default void beforeDeleteForSpecificUserInDeletePackageVersionedInternal(com.android.server.pm.DeletePackageHelper deletePackageHelper, java.lang.String internalPackageName, long versionCode, int userId, int deleteFlags) {
    }

    default void afterDeleteInDeletePackageVersionedInternal(com.android.server.pm.pkg.PackageStateInternal packageState, java.lang.String packageName, android.os.Handler packageHandler) {
    }

    default boolean customAllowInIsCallerAllowedToSilentlyUninstall(com.android.server.pm.Computer computer, int callingUid) {
        return false;
    }

    default boolean allowUninstallDeviceAdminInDeletePackageX(java.lang.String packageName, int userId) {
        return false;
    }

    default boolean allowUninstallSystemAppsForUser(android.content.pm.UserInfo userInfo) {
        return false;
    }

    default void afterDeleteSucceededInDeletePackageX(java.lang.String packageName, int userId, int removeUser) {
    }

    default void beforeDeletePackageX(java.lang.String packageName) {
    }

    default void beforeclearApplicationUserData(java.lang.String packageName) {
    }

    default void beforeDeleteApplicationCacheFiles() {
    }

    default com.android.server.pm.PackageSetting adjustPackageSettingInDeletePackageLIF(com.android.server.pm.PackageSetting ps, android.content.Context context) {
        return ps;
    }

    default void onMarkPackageUninstalledForUser(com.android.server.pm.PackageSetting ps, int userId) {
    }

    default void beforeAddInAddPreferredActivityInternal(android.content.ComponentName activity, com.android.server.pm.WatchedIntentFilter filter, int userId) {
    }

    default boolean isHoldingLockInUpdateDefaultHomeNotLockedMulti() {
        return false;
    }

    default boolean isHoldingLockInUpdateDefaultHomeNotLocked() {
        return false;
    }

    default int adjustPermissionStateCheckInSetEnabledSetting(android.content.Context context, int permission) {
        return permission;
    }

    default boolean interceptActionInSetEnabledSetting(int callingUid, int newState, java.lang.String packageName) {
        return false;
    }

    default boolean useLongBroadcastDelayInSetEnabledSetting(int flags) {
        return false;
    }

    default void sendPackageChangedBroadcastInSetEnabledSetting(com.android.server.pm.Computer computer, java.util.List<android.content.pm.PackageManager.ComponentEnabledSetting> settings, java.lang.String packageName, java.util.ArrayList<java.lang.String> componentNames, int packageUid, java.lang.String reason, int callingPid) {
    }

    default void afterUserManagerSystemReady() {
    }

    default void afterPermissionManagerSystemReady() {
    }

    default void afterPackageManagerSystemReady(boolean isPreQUpgrade) {
    }

    default boolean skipDestroyDeDataInReconcileAppsDataLI(java.lang.String volumeUuid, java.lang.String packageName, int userId) {
        return false;
    }

    default void beforeInstallPackagesTracedLI() {
    }

    default void beforeDoPostDeleteLIInHPPI(java.lang.String packageName) {
    }

    default void handleNewUserInONUC(int userId) {
    }

    default boolean customPermissionUpgradeNeeded() {
        return false;
    }

    default boolean hookOplusOtaPs(com.android.server.pm.PackageSetting ps) {
        return false;
    }

    default void shutdownExtAtEnd() {
    }

    default boolean interceptAddPreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId, boolean removeExisting) {
        return false;
    }

    default java.lang.Boolean hasSystemFeatureExtAtBegin(java.lang.String name, int version) {
        return null;
    }

    default boolean installStageExtAtBegin(com.android.server.pm.InstallingSession params) {
        return false;
    }

    default boolean installStageClusterExtAtBegin(com.android.server.pm.InstallingSession parent, java.util.List<com.android.server.pm.InstallingSession> children) throws com.android.server.pm.PackageManagerException {
        return false;
    }

    default java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesExtAtBegin(android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return null;
    }

    default boolean interceptReplacePreferredActivity(android.content.IntentFilter filter) {
        return false;
    }

    default java.lang.Integer checkPermissionExtAtBegin(java.lang.String permName, java.lang.String pkgName, int userId) {
        return null;
    }

    default boolean dumpProfilesExtAtBegin(java.lang.String packageName) {
        return false;
    }

    default void adjustScanResultBeforeRegisterAppIdInAFILI(com.android.server.pm.ScanResult scanResult) {
    }

    default boolean skipRemoveKeyStoreInRPDLIF(java.lang.String pkgName, int appId) {
        return false;
    }

    default long adjustFlagsInGetInstalledPackages(com.android.server.pm.Computer computer, long flags, int userId) {
        return flags;
    }

    default void beforeFailReturnInSetInstallerPackageNameOfVerificationPermission(java.lang.String packageName) {
    }

    default void onStartSetEnabledSettingForInformation(java.util.List<android.content.pm.PackageManager.ComponentEnabledSetting> settings, int userId, java.lang.String callingPackage) {
    }

    default void adjustWritePackageRestrictionsInHandler(boolean state) {
    }

    default void onStartLockedForPermissionAdded() {
    }

    default void onEndLockedForPermissionAdded() {
    }

    default void sendMapCommonDcsUpload(java.lang.String logTag, java.lang.String eventId, java.util.Map data) {
    }

    default void sendMapCommonDcsUploadWithAppID(java.lang.String appId, java.lang.String logTag, java.lang.String eventId, java.util.Map data) {
    }

    default void installSystemPackagesNoneReboot(java.util.List<java.lang.String> apkFileDirs) {
    }

    default void installRemovablePackagesNoneReboot(java.util.List<java.lang.String> apkFileDirs) {
    }

    default java.util.ArrayList<android.content.pm.PackageInfo> getInstalledPackagesAsUserExt(java.util.ArrayList<android.content.pm.PackageInfo> parceledList) {
        return null;
    }

    default java.util.ArrayList<android.content.pm.ApplicationInfo> getInstalledApplicationsAsUserExt(java.util.ArrayList<android.content.pm.ApplicationInfo> parceledList) {
        return null;
    }

    default java.util.Map<java.lang.String, java.lang.String> getContainOplusCertificatePackages() {
        return new java.util.HashMap();
    }

    default boolean interceptOsdkVersionInPreparePackageLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        return false;
    }

    default void notifyPackageDeleteForAbiInfo(java.lang.String packageName) {
    }

    default void notifyPackageAddOrUpdateForAbiInfo(java.lang.String packageName, com.android.server.pm.PackageSetting setting) {
    }

    default void notifyPackageUseLocked(java.lang.String packageName, int reason) {
    }

    default boolean needNotifyDexLoad(android.content.pm.ApplicationInfo ai, java.lang.String loadingPackageName, java.util.Map<java.lang.String, java.lang.String> classLoaderContextMap) {
        return false;
    }

    default void notifyDexLoad(android.content.pm.ApplicationInfo ai, java.lang.String loadingPackageName, java.util.Map<java.lang.String, java.lang.String> classLoaderContextMap, java.lang.String loaderIsa) {
    }

    default void killDex2oatNow() {
    }

    default void afterInstallPackagesLIForIconPack(android.content.Context context) {
    }

    default void onPrepareSaveIconPack(android.content.Context context, java.util.Map<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> userPackages) {
    }

    default int onStartInPerformDexOptUpgrade(int number) {
        return number;
    }

    default void beforeShowBootMessageInPerformDexOptUpgrade(boolean isUpgrade, int numberOfVisited, int pkgsSize) {
    }

    default boolean writeMdmLog(java.lang.String event, java.lang.String result, java.lang.String describe) {
        return false;
    }

    default com.android.server.pm.dex.DexoptOptions modifyDexoptOptionsBeforDo(com.android.server.pm.IInstallArgsExt mInstallArgsExt, com.android.server.pm.dex.DexoptOptions dexoptOptions) {
        return dexoptOptions;
    }

    default void beforeCreateNewUser(int userId) {
    }

    default void disablePackagesNoneReboot(java.lang.String action, java.lang.String carrierName) {
    }

    default boolean skipInstallInMultiUser(int userId, java.lang.String pkgName) {
        return false;
    }

    default void initInMain() {
    }

    default void afterApexGetListAndWaitForOpexFinishInConstructor() {
    }

    default boolean shouldRemoveUpdatedMainlineApk(java.lang.String pkg) {
        return false;
    }

    default boolean setMarketRecommendPause(long millseconds) {
        return false;
    }

    default void showAppInstallationRecommendPage(java.lang.String packageName, com.android.server.pm.InstallSource installSource) {
    }

    default void getRequiredServicesExtensionPackageError() {
    }

    default android.util.Pair<java.util.concurrent.ExecutorService, java.util.function.Consumer<com.android.server.art.model.OperationProgress>> beforeOnBootUseArtService() {
        return null;
    }

    default void afterOnBootUseArtService(java.util.concurrent.ExecutorService service) {
    }

    default boolean isResolveForPermissionController(com.android.server.pm.Computer computerEngine, int callingUid) {
        return false;
    }

    default boolean isTranslatorWhitelistApp(java.lang.String packageName) {
        return true;
    }

    default boolean hookBeforeTargetSdkBlock(com.android.server.pm.InstallRequest installRequest, boolean bypassLowTargetSdkBlock, int targetSdkVersion) {
        return bypassLowTargetSdkBlock;
    }

    default void beforeOnBoot(int reason) {
    }

    default void hookInExecutePostCommitStepsLIF(java.lang.String pkgName) {
    }

    default boolean adjustCrossUserPermission(int callingUserId, int userId) {
        return false;
    }

    default boolean adjustFilterUninstallForMultiApp(boolean filterUninstall, int userId, com.android.server.pm.pkg.PackageStateInternal ps) {
        return filterUninstall;
    }

    default void hbtCheckInstall(java.lang.String name, com.android.server.pm.PackageSetting oldPkgSetting, com.android.server.pm.ScanResult scanResult) throws com.android.server.pm.PackageManagerException {
    }

    default void hbtCheckUninstall(java.lang.String name, java.lang.String[] hbtIsa) {
    }

    default void translatorCheckScan(com.android.server.pm.PackageManagerService pm, com.android.server.pm.ScanResult scanResult, int scanFlags) {
    }

    default void translatorBeforeScan() {
    }

    default void translatorFinishedScan() {
    }

    default void hookScanApexPackages(android.apex.ApexInfo ai) {
    }

    public interface IStaticExt {
        default com.android.server.art.model.DexoptResult hookInDexoptPackageUsingArtService(com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot, java.lang.String pkgName, com.android.server.art.model.DexoptParams params) {
            return null;
        }
    }
}
