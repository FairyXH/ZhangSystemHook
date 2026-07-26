package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class InstallPackageHelper {
    private final com.android.server.pm.ApexManager mApexManager;
    private final com.android.server.pm.AppDataHelper mAppDataHelper;
    private final com.android.server.pm.dex.ArtManagerService mArtManagerService;
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final android.content.Context mContext;
    private final com.android.server.pm.DeletePackageHelper mDeletePackageHelper;
    private final com.android.server.pm.dex.DexManager mDexManager;
    private final android.os.incremental.IncrementalManager mIncrementalManager;
    private final com.android.server.pm.PackageManagerServiceInjector mInjector;
    private final com.android.server.pm.PackageAbiHelper mPackageAbiHelper;
    private final com.android.server.pm.PackageDexOptimizer mPackageDexOptimizer;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.RemovePackageHelper mRemovePackageHelper;
    private final com.android.server.pm.SharedLibrariesImpl mSharedLibraries;
    private final com.android.server.pm.UpdateOwnershipHelper mUpdateOwnershipHelper;
    private final com.android.server.pm.IInstallPackageHelperWrapper mWrapper = new com.android.server.pm.InstallPackageHelper.InstallPackageHelperWrapper();

    InstallPackageHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.AppDataHelper appDataHelper, com.android.server.pm.RemovePackageHelper removePackageHelper, com.android.server.pm.DeletePackageHelper deletePackageHelper, com.android.server.pm.BroadcastHelper broadcastHelper) {
        this.mPm = pm;
        this.mInjector = pm.mInjector;
        this.mAppDataHelper = appDataHelper;
        this.mBroadcastHelper = broadcastHelper;
        this.mRemovePackageHelper = removePackageHelper;
        this.mDeletePackageHelper = deletePackageHelper;
        this.mIncrementalManager = pm.mInjector.getIncrementalManager();
        this.mApexManager = pm.mInjector.getApexManager();
        this.mDexManager = pm.mInjector.getDexManager();
        this.mArtManagerService = pm.mInjector.getArtManagerService();
        this.mContext = pm.mInjector.getContext();
        this.mPackageDexOptimizer = pm.mInjector.getPackageDexOptimizer();
        this.mPackageAbiHelper = pm.mInjector.getAbiHelper();
        this.mSharedLibraries = pm.mInjector.getSharedLibrariesImpl();
        this.mUpdateOwnershipHelper = pm.mInjector.getUpdateOwnershipHelper();
    }

    /* JADX WARN: Type inference failed for: r12v19, types: [boolean] */
    private com.android.server.pm.pkg.AndroidPackage commitReconciledScanResultLocked(com.android.server.pm.ReconciledPackage reconciledPkg, int[] allUsers) throws java.lang.Throwable {
        final com.android.server.pm.PackageSetting pkgSetting;
        java.lang.String updateOwnerFromSysconfig;
        com.android.server.pm.SharedUserSetting sharedUserSetting;
        int userId;
        com.android.server.pm.PackageSetting ips;
        com.android.server.pm.InstallRequest request = reconciledPkg.mInstallRequest;
        com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage = request.getParsedPackage();
        if (parsedPackage != null && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(parsedPackage.getPackageName())) {
            parsedPackage.setVersionCode(this.mPm.getSdkVersion()).setVersionCodeMajor(0);
        }
        int scanFlags = request.getScanFlags();
        com.android.server.pm.PackageSetting oldPkgSetting = request.getScanRequestOldPackageSetting();
        com.android.server.pm.PackageSetting originalPkgSetting = request.getScanRequestOriginalPackageSetting();
        java.lang.String realPkgName = request.getRealPackageName();
        if (request.getScanRequestPackageSetting() != null) {
            com.android.server.pm.SharedUserSetting requestSharedUserSetting = this.mPm.mSettings.getSharedUserSettingLPr(request.getScanRequestPackageSetting());
            com.android.server.pm.SharedUserSetting resultSharedUserSetting = this.mPm.mSettings.getSharedUserSettingLPr(request.getScannedPackageSetting());
            if (requestSharedUserSetting != null && requestSharedUserSetting != resultSharedUserSetting) {
                requestSharedUserSetting.removePackage(request.getScanRequestPackageSetting());
                if (this.mPm.mSettings.checkAndPruneSharedUserLPw(requestSharedUserSetting, false)) {
                    request.setRemovedAppId(requestSharedUserSetting.mAppId);
                }
            }
        }
        if (request.isExistingSettingCopied()) {
            pkgSetting = request.getScanRequestPackageSetting();
            pkgSetting.updateFrom(request.getScannedPackageSetting());
        } else {
            pkgSetting = request.getScannedPackageSetting();
            if (originalPkgSetting != null) {
                this.mPm.mSettings.addRenamedPackageLPw(com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRealPackageOrNull(parsedPackage, pkgSetting.isSystem()), originalPkgSetting.getPackageName());
                this.mPm.mTransferredPackages.add(originalPkgSetting.getPackageName());
            } else {
                this.mPm.mSettings.removeRenamedPackageLPw(parsedPackage.getPackageName());
            }
        }
        com.android.server.pm.SharedUserSetting sharedUserSetting2 = this.mPm.mSettings.getSharedUserSettingLPr(pkgSetting);
        if (sharedUserSetting2 != null) {
            sharedUserSetting2.addPackage(pkgSetting);
            if (parsedPackage.isLeavingSharedUser() && com.android.server.pm.SharedUidMigration.applyStrategy(2) && sharedUserSetting2.isSingleUser()) {
                this.mPm.mSettings.convertSharedUserSettingsLPw(sharedUserSetting2);
            }
        }
        if (request.isForceQueryableOverride()) {
            pkgSetting.setForceQueryableOverride(true);
        }
        com.android.server.pm.InstallSource installSource = request.getInstallSource();
        boolean isApex = (67108864 & scanFlags) != 0;
        boolean pkgAlreadyExists = oldPkgSetting != null;
        java.lang.String oldUpdateOwner = pkgAlreadyExists ? oldPkgSetting.getInstallSource().mUpdateOwnerPackageName : null;
        if (isApex || !pkgSetting.isSystem()) {
            updateOwnerFromSysconfig = null;
        } else {
            updateOwnerFromSysconfig = this.mPm.mInjector.getSystemConfig().getSystemAppUpdateOwnerPackageName(parsedPackage.getPackageName());
        }
        boolean isUpdateOwnershipDenylisted = this.mUpdateOwnershipHelper.isUpdateOwnershipDenylisted(parsedPackage.getPackageName());
        boolean isUpdateOwnershipEnabled = oldUpdateOwner != null;
        if (installSource != null) {
            if (!com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(installSource.mInitiatingPackageName) && (ips = this.mPm.mSettings.getPackageLPr(installSource.mInitiatingPackageName)) != null) {
                installSource = installSource.setInitiatingPackageSignatures(ips.getSignatures());
            }
            if (isApex) {
                sharedUserSetting = sharedUserSetting2;
            } else {
                if (installSource.mInstallerPackageUid != -1) {
                    userId = android.os.UserHandle.getUserId(installSource.mInstallerPackageUid);
                } else {
                    userId = request.getUserId();
                }
                boolean isUpdate = pkgAlreadyExists && (userId < 0 ? oldPkgSetting.getNotInstalledUserIds().length <= android.os.UserManager.isHeadlessSystemUserMode() : oldPkgSetting.getInstalled(userId));
                boolean isRequestUpdateOwnership = (request.getInstallFlags() & 33554432) != 0;
                boolean isSameUpdateOwner = android.text.TextUtils.equals(oldUpdateOwner, installSource.mInstallerPackageName);
                sharedUserSetting = sharedUserSetting2;
                boolean isInstallerUpdateOwnerDenylistProvider = this.mUpdateOwnershipHelper.isUpdateOwnershipDenyListProvider(installSource.mUpdateOwnerPackageName);
                if (!isUpdate) {
                    if (!isRequestUpdateOwnership || isUpdateOwnershipDenylisted || isInstallerUpdateOwnerDenylistProvider) {
                        java.lang.String str = null;
                        installSource = installSource.setUpdateOwnerPackageName(str);
                    } else if ((!isUpdateOwnershipEnabled && pkgAlreadyExists) || (isUpdateOwnershipEnabled && !isSameUpdateOwner)) {
                        installSource = installSource.setUpdateOwnerPackageName(null);
                    }
                } else if (!isSameUpdateOwner || !isUpdateOwnershipEnabled) {
                    installSource = installSource.setUpdateOwnerPackageName(null);
                }
            }
            pkgSetting.setInstallSource(installSource);
        } else {
            sharedUserSetting = sharedUserSetting2;
            if (pkgSetting.isSystem()) {
                boolean isSameUpdateOwner2 = isUpdateOwnershipEnabled && android.text.TextUtils.equals(oldUpdateOwner, updateOwnerFromSysconfig);
                if (!pkgAlreadyExists || isSameUpdateOwner2) {
                    pkgSetting.setUpdateOwnerPackage(updateOwnerFromSysconfig);
                } else {
                    pkgSetting.setUpdateOwnerPackage(null);
                }
            }
        }
        if ((8388608 & scanFlags) != 0) {
            boolean isFactory = (33554432 & scanFlags) != 0;
            pkgSetting.getPkgState().setApkInUpdatedApex(!isFactory);
        }
        pkgSetting.getPkgState().setApexModuleName(request.getApexModuleName());
        parsedPackage.setUid(pkgSetting.getAppId());
        com.android.server.pm.pkg.AndroidPackage pkg = parsedPackage.hideAsFinal();
        this.mPm.mSettings.writeUserRestrictionsLPw(pkgSetting, oldPkgSetting);
        if (realPkgName != null) {
            this.mPm.mTransferredPackages.add(pkg.getPackageName());
        }
        if (reconciledPkg.mCollectedSharedLibraryInfos != null || (oldPkgSetting != null && !oldPkgSetting.getSharedLibraryDependencies().isEmpty())) {
            this.mSharedLibraries.executeSharedLibrariesUpdate(pkg, pkgSetting, null, null, reconciledPkg.mCollectedSharedLibraryInfos, allUsers);
        }
        com.android.server.pm.KeySetManagerService ksms = this.mPm.mSettings.getKeySetManagerService();
        if (reconciledPkg.mRemoveAppKeySetData) {
            ksms.removeAppKeySetDataLPw(pkg.getPackageName());
        }
        if (reconciledPkg.mSharedUserSignaturesChanged) {
            com.android.server.pm.SharedUserSetting sharedUserSetting3 = sharedUserSetting;
            sharedUserSetting3.signaturesChanged = java.lang.Boolean.TRUE;
            sharedUserSetting3.signatures.mSigningDetails = reconciledPkg.mSigningDetails;
        }
        pkgSetting.setSigningDetails(reconciledPkg.mSigningDetails);
        int userId2 = request.getUserId();
        commitPackageSettings(pkg, pkgSetting, oldPkgSetting, reconciledPkg);
        if (pkgSetting.getInstantApp(userId2)) {
            this.mPm.mInstantAppRegistry.addInstantApp(userId2, pkgSetting.getAppId());
        }
        if (!android.os.incremental.IncrementalManager.isIncrementalPath(pkgSetting.getPathString())) {
            pkgSetting.setLoadingProgress(1.0f);
        }
        if (com.android.server.pm.UpdateOwnershipHelper.hasValidOwnershipDenyList(pkgSetting)) {
            this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$commitReconciledScanResultLocked$0(pkgSetting);
                }
            });
        }
        return pkg;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleUpdateOwnerDenyList, reason: merged with bridge method [inline-methods] */
    public void lambda$commitReconciledScanResultLocked$0(com.android.server.pm.PackageSetting pkgSetting) {
        android.util.ArraySet<java.lang.String> listItems = this.mUpdateOwnershipHelper.readUpdateOwnerDenyList(pkgSetting);
        if (listItems != null && !listItems.isEmpty()) {
            this.mUpdateOwnershipHelper.addToUpdateOwnerDenyList(pkgSetting.getPackageName(), listItems);
            com.android.server.SystemConfig config = com.android.server.SystemConfig.getInstance();
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    for (java.lang.String unownedPackage : listItems) {
                        com.android.server.pm.PackageSetting unownedSetting = this.mPm.mSettings.getPackageLPr(unownedPackage);
                        if (unownedSetting != null && config.getSystemAppUpdateOwnerPackageName(unownedPackage) == null) {
                            unownedSetting.setUpdateOwnerPackage(null);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
    }

    private void commitPackageSettings(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.PackageSetting oldPkgSetting, com.android.server.pm.ReconciledPackage reconciledPkg) throws java.lang.Throwable {
        java.lang.String pkgName = pkg.getPackageName();
        com.android.server.pm.InstallRequest request = reconciledPkg.mInstallRequest;
        com.android.server.pm.pkg.AndroidPackage oldPkg = request.getScanRequestOldPackage();
        int scanFlags = request.getScanFlags();
        boolean chatty = (request.getParseFlags() & Integer.MIN_VALUE) != 0;
        if (this.mPm.mCustomResolverComponentName != null && this.mPm.mCustomResolverComponentName.getPackageName().equals(pkg.getPackageName())) {
            this.mPm.setUpCustomResolverActivity(pkg, pkgSetting);
        }
        if (oldPkgSetting != null && oldPkgSetting.getLastUpdateTime() < pkgSetting.getLastUpdateTime()) {
            pkgSetting.setAppMetadataFilePath(null);
            pkgSetting.setAppMetadataSource(0);
        }
        if (pkgSetting.getAppMetadataFilePath() == null) {
            java.lang.String dir = pkg.getPath();
            if (pkgSetting.isSystem()) {
                dir = android.os.Environment.getDataDirectoryPath() + "/app-metadata/" + pkg.getPackageName();
            }
            java.lang.String appMetadataFilePath = dir + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME;
            if (request.hasAppMetadataFile()) {
                pkgSetting.setAppMetadataFilePath(appMetadataFilePath);
                if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.aslInApkAppMetadataSource()) {
                    pkgSetting.setAppMetadataSource(2);
                }
            } else if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.aslInApkAppMetadataSource()) {
                java.util.Map<java.lang.String, android.content.pm.PackageManager.Property> properties = pkg.getProperties();
                if (properties.containsKey("android.content.PROPERTY_ANDROID_SAFETY_LABEL")) {
                    pkgSetting.setAppMetadataFilePath(appMetadataFilePath);
                    pkgSetting.setAppMetadataSource(1);
                }
            }
        }
        java.lang.String dir2 = pkg.getPackageName();
        if (dir2.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME)) {
            this.mPm.setPlatformPackage(pkg, pkgSetting);
        }
        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> clientLibPkgs = this.mSharedLibraries.commitSharedLibraryChanges(pkg, pkgSetting, reconciledPkg.mAllowedSharedLibraryInfos, reconciledPkg.getCombinedAvailablePackages(), scanFlags);
        request.setLibraryConsumers(clientLibPkgs);
        if ((scanFlags & 16) == 0 && (scanFlags & 1024) == 0 && (scanFlags & 2048) == 0) {
            this.mPm.snapshotComputer().checkPackageFrozen(pkgName);
        }
        boolean isReplace = request.isInstallReplace();
        if (clientLibPkgs != null && (pkg.getStaticSharedLibraryName() == null || isReplace)) {
            int i = 0;
            while (i < clientLibPkgs.size()) {
                com.android.server.pm.pkg.AndroidPackage clientPkg = clientLibPkgs.get(i);
                java.lang.String packageName = clientPkg.getPackageName();
                this.mPm.killApplication(packageName, clientPkg.getUid(), "update lib", 12);
                i++;
                clientLibPkgs = clientLibPkgs;
            }
        }
        android.os.Trace.traceBegin(262144L, "updateSettings");
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                try {
                    this.mPm.mSettings.insertPackageSettingLPw(pkgSetting, pkg);
                    this.mPm.mPackages.put(pkg.getPackageName(), pkg);
                    if ((8388608 & scanFlags) != 0) {
                        try {
                            this.mApexManager.registerApkInApex(pkg);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        }
                    }
                    if ((this.mPm.isDeviceUpgrading() && pkgSetting.isSystem()) || isReplace) {
                        for (int userId : this.mPm.mUserManager.getUserIds()) {
                            pkgSetting.restoreComponentSettings(userId);
                        }
                    }
                    if ((67108864 & scanFlags) == 0) {
                        com.android.server.pm.KeySetManagerService ksms = this.mPm.mSettings.getKeySetManagerService();
                        ksms.addScannedPackageLPw(pkg);
                    }
                    com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
                    this.mPm.mComponentResolver.addAllComponents(pkg, chatty, this.mPm.mSetupWizardPackage, snapshot);
                    this.mPm.mAppsFilter.addPackage(snapshot, pkgSetting, isReplace, (scanFlags & 1024) != 0);
                    this.mPm.addAllPackageProperties(pkg);
                    if (!request.isArchived()) {
                        if (oldPkgSetting == null || oldPkgSetting.getPkg() == null) {
                            this.mPm.mDomainVerificationManager.addPackage(pkgSetting, request.getPreVerifiedDomains());
                        } else {
                            this.mPm.mDomainVerificationManager.migrateState(oldPkgSetting, pkgSetting, request.getPreVerifiedDomains());
                        }
                    }
                    int collectionSize = com.android.internal.util.ArrayUtils.size(pkg.getInstrumentations());
                    int i2 = 0;
                    java.lang.StringBuilder r = null;
                    while (i2 < collectionSize) {
                        com.android.internal.pm.pkg.component.ParsedInstrumentation a = (com.android.internal.pm.pkg.component.ParsedInstrumentation) pkg.getInstrumentations().get(i2);
                        boolean isReplace2 = isReplace;
                        com.android.internal.pm.pkg.component.ComponentMutateUtils.setPackageName(a, pkg.getPackageName());
                        com.android.server.pm.Computer snapshot2 = snapshot;
                        this.mPm.addInstrumentation(a.getComponentName(), a);
                        if (chatty) {
                            if (r == null) {
                                r = new java.lang.StringBuilder(256);
                            } else {
                                r.append(' ');
                            }
                            r.append(a.getName());
                        }
                        i2++;
                        isReplace = isReplace2;
                        snapshot = snapshot2;
                    }
                    if (r != null && com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                        android.util.Log.d("PackageManager", "  Instrumentation: " + ((java.lang.Object) r));
                    }
                    java.util.List<java.lang.String> protectedBroadcasts = pkg.getProtectedBroadcasts();
                    if (!protectedBroadcasts.isEmpty()) {
                        synchronized (this.mPm.mProtectedBroadcasts) {
                            this.mPm.mProtectedBroadcasts.addAll(protectedBroadcasts);
                        }
                    }
                    try {
                        this.mPm.mPermissionManager.onPackageAdded(pkgSetting, (scanFlags & 8192) != 0, oldPkg);
                    } catch (java.lang.IllegalStateException e) {
                        android.util.Slog.e("PackageManager", "error happened in PermissionManager onPackageAdded, use live compute retry!");
                        this.mPm.mPackageManagerServiceExt.onStartLockedForPermissionAdded();
                        this.mPm.mPermissionManager.onPackageAdded(pkgSetting, (scanFlags & 8192) != 0, oldPkg);
                        this.mPm.mPackageManagerServiceExt.onEndLockedForPermissionAdded();
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    android.os.Trace.traceEnd(262144L);
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0109  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.util.Pair<java.lang.Integer, android.content.IntentSender> installExistingPackageAsUser(final java.lang.String r35, final int r36, int r37, int r38, java.util.List<java.lang.String> r39, final android.content.IntentSender r40) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 956
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.installExistingPackageAsUser(java.lang.String, int, int, int, java.util.List, android.content.IntentSender):android.util.Pair");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$installExistingPackageAsUser$1(java.lang.String packageName, int userId, android.content.IntentSender onCompleteSender) {
        this.mPm.restorePermissionsAndUpdateRolesForNewUserInstall(packageName, userId);
        if (onCompleteSender != null) {
            onInstallComplete(1, this.mContext, onCompleteSender);
        }
    }

    static void onInstallComplete(int returnCode, android.content.Context context, android.content.IntentSender target) {
        android.content.Intent fillIn = new android.content.Intent();
        fillIn.putExtra("android.content.pm.extra.STATUS", android.content.pm.PackageManager.installStatusToPublicStatus(returnCode));
        try {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            target.sendIntent(context, 0, fillIn, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
        }
    }

    public void restoreAndPostInstall(final com.android.server.pm.InstallRequest request) throws java.lang.Throwable {
        int userId = request.getUserId();
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Log.v("PackageManager", "restoreAndPostInstall userId=" + userId + " package=" + request.getPkg());
        }
        this.mPm.mPackageManagerServiceExt.onStartInRestoreAndPostInstall(request);
        com.android.server.pm.PackageSetting packageSetting = null;
        boolean update = request.isUpdate();
        boolean doRestore = false;
        if (request.getPkg() != null && !request.isArchived()) {
            if (!update) {
                doRestore = true;
            } else {
                java.lang.String packageName = request.getPkg().getPackageName();
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        packageSetting = this.mPm.mSettings.getPackageLPr(packageName);
                        if (packageSetting != null && packageSetting.isPendingRestore()) {
                            doRestore = true;
                        }
                    } finally {
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        if (this.mPm.mNextInstallToken < 0) {
            this.mPm.mNextInstallToken = 1;
        }
        com.android.server.pm.PackageManagerService packageManagerService = this.mPm;
        int token = packageManagerService.mNextInstallToken;
        packageManagerService.mNextInstallToken = token + 1;
        this.mPm.mRunningInstalls.put(token, request);
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Log.v("PackageManager", "+ starting restore round-trip " + token);
        }
        boolean succeeded = request.getReturnCode() == 1;
        if (succeeded && doRestore) {
            request.closeFreezer();
            doRestore = performBackupManagerRestore(userId, token, request);
        }
        if (succeeded && !doRestore && update) {
            doRestore = performRollbackManagerRestore(userId, token, request);
        }
        if (succeeded && doRestore && !request.hasPostInstallRunnable()) {
            final boolean hasNeverBeenRestored = packageSetting != null && packageSetting.isPendingRestore();
            request.setPostInstallRunnable(new java.lang.Runnable() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$restoreAndPostInstall$2(hasNeverBeenRestored, request);
                }
            });
        }
        if (doRestore) {
            if (packageSetting != null) {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock2) {
                    try {
                        packageSetting.setPendingRestore(false);
                    } finally {
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return;
            }
            return;
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Log.v("PackageManager", "No restore - queue post-install for " + token);
        }
        android.os.Trace.asyncTraceBegin(262144L, "postInstall", token);
        android.os.Message msg = this.mPm.mHandler.obtainMessage(9, token, 0);
        this.mPm.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreAndPostInstall$2(boolean hasNeverBeenRestored, com.android.server.pm.InstallRequest request) {
        int[] userIdsToRestorePermissions;
        if (hasNeverBeenRestored) {
            userIdsToRestorePermissions = request.getUpdateBroadcastUserIds();
        } else {
            userIdsToRestorePermissions = request.getFirstTimeBroadcastUserIds();
        }
        for (int restorePermissionUserId : userIdsToRestorePermissions) {
            this.mPm.restorePermissionsAndUpdateRolesForNewUserInstall(request.getName(), restorePermissionUserId);
        }
    }

    private boolean performBackupManagerRestore(int userId, int token, com.android.server.pm.InstallRequest request) {
        if (request.getPkg() == null) {
            return false;
        }
        android.app.backup.IBackupManager iBackupManager = this.mInjector.getIBackupManager();
        if (iBackupManager == null) {
            android.util.Slog.e("PackageManager", "Backup Manager not found!");
            return false;
        }
        if (userId == -1) {
            userId = 0;
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Log.v("PackageManager", "token " + token + " to BM for possible restore for user " + userId);
        }
        android.os.Trace.asyncTraceBegin(262144L, "restore", token);
        try {
            if (!iBackupManager.isUserReadyForBackup(userId)) {
                android.util.Slog.w("PackageManager", "User " + userId + " is not ready. Restore at install didn't take place.");
                return false;
            }
            iBackupManager.restoreAtInstallForUser(userId, request.getPkg().getPackageName(), token);
            return true;
        } catch (android.os.RemoteException e) {
            return true;
        } catch (java.lang.Exception e2) {
            android.util.Slog.e("PackageManager", "Exception trying to enqueue restore", e2);
            return false;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:36:0x009e
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    private boolean performRollbackManagerRestore(int r23, int r24, com.android.server.pm.InstallRequest r25) throws java.lang.Throwable {
        /*
            r22 = this;
            r1 = r22
            com.android.server.pm.pkg.AndroidPackage r0 = r25.getPkg()
            r2 = 0
            if (r0 != 0) goto La
            return r2
        La:
            com.android.server.pm.pkg.AndroidPackage r0 = r25.getPkg()
            java.lang.String r11 = r0.getPackageName()
            com.android.server.pm.PackageManagerService r0 = r1.mPm
            com.android.server.pm.UserManagerService r0 = r0.mUserManager
            int[] r12 = r0.getUserIds()
            r3 = -1
            r4 = -1
            com.android.server.pm.PackageManagerService r0 = r1.mPm
            com.android.server.pm.PackageManagerTracedLock r6 = r0.mLock
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection()
            monitor-enter(r6)
            com.android.server.pm.PackageManagerService r0 = r1.mPm     // Catch: java.lang.Throwable -> L96
            com.android.server.pm.Settings r0 = r0.mSettings     // Catch: java.lang.Throwable -> L96
            com.android.server.pm.PackageSetting r0 = r0.getPackageLPr(r11)     // Catch: java.lang.Throwable -> L96
            r13 = 1
            if (r0 == 0) goto L46
            int r7 = r0.getAppId()     // Catch: java.lang.Throwable -> L96
            r3 = r7
            r14 = r23
            long r7 = r0.getCeDataInode(r14)     // Catch: java.lang.Throwable -> L9e
            r4 = r7
            int[] r7 = r0.queryInstalledUsers(r12, r13)     // Catch: java.lang.Throwable -> L9e
            r15 = r3
            r16 = r4
            r18 = r7
            goto L4f
        L46:
            r14 = r23
            int[] r7 = new int[r2]     // Catch: java.lang.Throwable -> L9e
            r15 = r3
            r16 = r4
            r18 = r7
        L4f:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L91
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection()
            int r10 = r25.getInstallFlags()
            r3 = 262144(0x40000, float:3.67342E-40)
            r3 = r3 & r10
            if (r3 != 0) goto L63
            r3 = r10 & 128(0x80, float:1.8E-43)
            if (r3 == 0) goto L61
            goto L63
        L61:
            r3 = r2
            goto L64
        L63:
            r3 = r13
        L64:
            r19 = r3
            if (r0 == 0) goto L8e
            if (r19 == 0) goto L8e
            java.lang.String r2 = r0.getSeInfo()
            com.android.server.pm.PackageManagerServiceInjector r3 = r1.mInjector
            java.lang.Class<com.android.server.rollback.RollbackManagerInternal> r4 = com.android.server.rollback.RollbackManagerInternal.class
            java.lang.Object r3 = r3.getLocalService(r4)
            r20 = r3
            com.android.server.rollback.RollbackManagerInternal r20 = (com.android.server.rollback.RollbackManagerInternal) r20
            java.util.List r5 = android.os.UserHandle.toUserHandles(r18)
            r3 = r20
            r4 = r11
            r6 = r15
            r7 = r16
            r9 = r2
            r21 = r10
            r10 = r24
            r3.snapshotAndRestoreUserData(r4, r5, r6, r7, r9, r10)
            return r13
        L8e:
            r21 = r10
            return r2
        L91:
            r0 = move-exception
            r3 = r15
            r4 = r16
            goto L99
        L96:
            r0 = move-exception
            r14 = r23
        L99:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L9e
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection()
            throw r0
        L9e:
            r0 = move-exception
            goto L99
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.performRollbackManagerRestore(int, int, com.android.server.pm.InstallRequest):boolean");
    }

    void installPackagesTraced(java.util.List<com.android.server.pm.InstallRequest> requests) {
        try {
            com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
            try {
                android.os.Trace.traceBegin(262144L, "installPackages");
                installPackagesLI(requests);
                if (installLock != null) {
                    installLock.close();
                }
                android.os.Trace.traceEnd(262144L);
                ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).waitForTranslatorState(requests);
            } finally {
            }
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(262144L);
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x02b4, code lost:
    
        if (r2.getSignatureSchemeVersion() == 4) goto L535;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x02b7, code lost:
    
        r4 = r2.getPkg().getBaseApkPath();
        r5 = r2.getPkg().getSplitCodePaths();
        r6 = r2.getOriginUri();
        r7 = r34.mPm;
        r8 = r7.mPendingVerificationToken;
        r7.mPendingVerificationToken = r8 + 1;
        com.android.server.pm.VerificationUtils.broadcastPackageVerified(r8, r6, 1, com.android.server.pm.PackageManagerServiceUtils.buildVerificationRootHashString(r4, r5), r2.getDataLoaderType(), r2.getUser(), r34.mContext);
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x02f2, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x02fa, code lost:
    
        if (r0.hasNext() == false) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x02fc, code lost:
    
        r2 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0306, code lost:
    
        if (r2.getParsedPackage() == null) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x031e, code lost:
    
        if (((java.lang.Boolean) r0.getOrDefault(r2.getParsedPackage().getPackageName(), java.lang.Boolean.valueOf((boolean) r8))).booleanValue() == false) goto L545;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0320, code lost:
    
        cleanUpAppIdCreation(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0324, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x032c, code lost:
    
        if (r0.hasNext() == false) goto L547;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x032e, code lost:
    
        r2 = r0.next();
        r2.closeFreezer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x033b, code lost:
    
        if (r2.getReturnCode() != r11) goto L550;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x033d, code lost:
    
        r2.setReturnCode(r8 == true ? 1 : 0);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r2.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0350, code lost:
    
        android.os.Trace.traceEnd(r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0353, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0060, code lost:
    
        r1.setError(-116, "Failed to obtain package to scan");
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0067, code lost:
    
        if (0 == 0) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0069, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x03ef, code lost:
    
        r15.setError(com.android.server.pm.PackageManagerException.ofInternalError("Failed to obtain the responsible installer info", -39));
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x03fa, code lost:
    
        if (0 == 0) goto L168;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x03fc, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0404, code lost:
    
        if (r0.hasNext() == false) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0406, code lost:
    
        r2 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0411, code lost:
    
        if (r2.getDataLoaderType() == 2) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0419, code lost:
    
        if (r2.getSignatureSchemeVersion() == 4) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x041c, code lost:
    
        r4 = r2.getPkg().getBaseApkPath();
        r5 = r2.getPkg().getSplitCodePaths();
        r6 = r2.getOriginUri();
        r8 = r34.mPm;
        r11 = r8.mPendingVerificationToken;
        r8.mPendingVerificationToken = r11 + 1;
        com.android.server.pm.VerificationUtils.broadcastPackageVerified(r11, r6, 1, com.android.server.pm.PackageManagerServiceUtils.buildVerificationRootHashString(r4, r5), r2.getDataLoaderType(), r2.getUser(), r34.mContext);
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x045b, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0071, code lost:
    
        if (r0.hasNext() == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0463, code lost:
    
        if (r0.hasNext() == false) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x0465, code lost:
    
        r2 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x046f, code lost:
    
        if (r2.getParsedPackage() == null) goto L489;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0487, code lost:
    
        if (((java.lang.Boolean) r0.getOrDefault(r2.getParsedPackage().getPackageName(), java.lang.Boolean.valueOf((boolean) r8))).booleanValue() == false) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0489, code lost:
    
        cleanUpAppIdCreation(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x048d, code lost:
    
        r0 = r35.iterator();
        r11 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0495, code lost:
    
        if (r0.hasNext() == false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0073, code lost:
    
        r1 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0497, code lost:
    
        r2 = r0.next();
        r2.closeFreezer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x04a4, code lost:
    
        if (r2.getReturnCode() != r11) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x04a6, code lost:
    
        r2.setReturnCode(r8 == true ? 1 : 0);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r2.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x04b8, code lost:
    
        r11 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x04ba, code lost:
    
        android.os.Trace.traceEnd(r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x04bd, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x007d, code lost:
    
        if (r1.getDataLoaderType() == r15) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0084, code lost:
    
        if (r1.getSignatureSchemeVersion() == r14) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0087, code lost:
    
        r2 = r1.getPkg().getBaseApkPath();
        r7 = r1.getPkg().getSplitCodePaths();
        r8 = r1.getOriginUri();
        r14 = r34.mPm;
        r15 = r14.mPendingVerificationToken;
        r14.mPendingVerificationToken = r15 + 1;
        com.android.server.pm.VerificationUtils.broadcastPackageVerified(r15, r8, 1, com.android.server.pm.PackageManagerServiceUtils.buildVerificationRootHashString(r2, r7), r1.getDataLoaderType(), r1.getUser(), r34.mContext);
        r14 = 4;
        r15 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00c7, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x06d7, code lost:
    
        r25 = r5;
        r7 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x06da, code lost:
    
        r11 = r34.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x06e1, code lost:
    
        monitor-enter(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00cf, code lost:
    
        if (r0.hasNext() == false) goto L576;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x06e5, code lost:
    
        r14 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x06e7, code lost:
    
        android.os.Trace.traceBegin(r14, "reconcilePackages");
        r3 = java.util.Collections.unmodifiableMap(r34.mPm.mPackages);
        r5 = r34.mSharedLibraries;
        r6 = r34.mPm.mSettings.getKeySetManagerService();
        r0 = r34.mPm.mSettings;
        r1 = r34.mPm.mInjector.getSystemConfig();
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0708, code lost:
    
        r2 = r35;
        r18 = r8 == true ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0711, code lost:
    
        r0 = com.android.server.pm.ReconcilePackageUtils.reconcilePackages(r2, r3, r7, r5, r6, r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0716, code lost:
    
        android.os.Trace.traceEnd(r14);
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x071e, code lost:
    
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveInstallFreeze() == false) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0720, code lost:
    
        r0 = r0.iterator();
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0724, code lost:
    
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00d1, code lost:
    
        r1 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x0728, code lost:
    
        if (r0.hasNext() == false) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x072a, code lost:
    
        r1 = r0.next().mInstallRequest;
        r2 = r1.getParsedPackage().getPackageName();
        r1.setFreezer(freezePackageForInstall(r2, -1, r1.getInstallFlags(), "installPackageLI", 16, r1));
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x075b, code lost:
    
        android.os.Trace.traceBegin(r14, "commitPackages");
        commitPackagesLocked(r0, r34.mPm.mUserManager.getUserIds());
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x076b, code lost:
    
        r13 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x076c, code lost:
    
        android.os.Trace.traceEnd(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0770, code lost:
    
        monitor-exit(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0771, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        executePostCommitStepsLIF(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0777, code lost:
    
        if (1 == 0) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0779, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00db, code lost:
    
        if (r1.getParsedPackage() == null) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0781, code lost:
    
        if (r0.hasNext() == false) goto L607;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x0783, code lost:
    
        r1 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x078e, code lost:
    
        if (r1.getDataLoaderType() == 2) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0796, code lost:
    
        if (r1.getSignatureSchemeVersion() == 4) goto L609;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0799, code lost:
    
        r2 = r1.getPkg().getBaseApkPath();
        r3 = r1.getPkg().getSplitCodePaths();
        r4 = r1.getOriginUri();
        r5 = r34.mPm;
        r6 = r5.mPendingVerificationToken;
        r5.mPendingVerificationToken = r6 + 1;
        com.android.server.pm.VerificationUtils.broadcastPackageVerified(r6, r4, 1, com.android.server.pm.PackageManagerServiceUtils.buildVerificationRootHashString(r2, r3), r1.getDataLoaderType(), r1.getUser(), r34.mContext);
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x07d4, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x07dc, code lost:
    
        if (r0.hasNext() == false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x07de, code lost:
    
        r1 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x07e8, code lost:
    
        if (r1.getParsedPackage() == null) goto L618;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0800, code lost:
    
        if (((java.lang.Boolean) r0.getOrDefault(r1.getParsedPackage().getPackageName(), java.lang.Boolean.valueOf(r18))).booleanValue() == false) goto L619;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0802, code lost:
    
        cleanUpAppIdCreation(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0806, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f3, code lost:
    
        if (((java.lang.Boolean) r0.getOrDefault(r1.getParsedPackage().getPackageName(), java.lang.Boolean.valueOf((boolean) r8))).booleanValue() == false) goto L581;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x080e, code lost:
    
        if (r0.hasNext() == false) goto L621;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x0810, code lost:
    
        r1 = r0.next();
        r1.closeFreezer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x081e, code lost:
    
        if (r1.getReturnCode() != 1) goto L325;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0820, code lost:
    
        r2 = r18;
        r1.setReturnCode(r2 ? 1 : 0);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r1.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0835, code lost:
    
        r2 = r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0837, code lost:
    
        r18 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x083a, code lost:
    
        android.os.Trace.traceEnd(r14);
        r34.mPm.mPackageManagerServiceExt.afterInstallPackagesLIForIconPack(r34.mPm.mContext);
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0849, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x084a, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00f5, code lost:
    
        cleanUpAppIdCreation(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x084b, code lost:
    
        r2 = r18 ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x084f, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0852, code lost:
    
        android.os.Trace.traceEnd(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0856, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0857, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x0858, code lost:
    
        r2 = r18 ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x085c, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0861, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0862, code lost:
    
        r2 = r18 ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0865, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0866, code lost:
    
        r8 = r8 == true ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x086a, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x086b, code lost:
    
        r2 = r8 == true ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00f9, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x087d, code lost:
    
        r1 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0887, code lost:
    
        r3 = r1.next();
        r3.setError("Reconciliation failed...", r0);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r3.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x08a3, code lost:
    
        android.os.Trace.traceEnd(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x08a6, code lost:
    
        monitor-exit(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x08a7, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x08aa, code lost:
    
        if (0 != 0) goto L359;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x08ac, code lost:
    
        r1 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x08b6, code lost:
    
        r2 = r1.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x08c1, code lost:
    
        if (r2.getDataLoaderType() == 2) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x08cc, code lost:
    
        r3 = r2.getPkg().getBaseApkPath();
        r4 = r2.getPkg().getSplitCodePaths();
        r5 = r2.getOriginUri();
        r6 = r34.mPm;
        r7 = r6.mPendingVerificationToken;
        r6.mPendingVerificationToken = r7 + 1;
        com.android.server.pm.VerificationUtils.broadcastPackageVerified(r7, r5, 1, com.android.server.pm.PackageManagerServiceUtils.buildVerificationRootHashString(r3, r4), r2.getDataLoaderType(), r2.getUser(), r34.mContext);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0101, code lost:
    
        if (r0.hasNext() == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0907, code lost:
    
        r1 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0911, code lost:
    
        r3 = r1.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x091b, code lost:
    
        if (r3.getParsedPackage() == null) goto L636;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0935, code lost:
    
        cleanUpAppIdCreation(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0939, code lost:
    
        r1 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0103, code lost:
    
        r1 = r0.next();
        r1.closeFreezer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0943, code lost:
    
        r3 = r1.next();
        r3.closeFreezer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0951, code lost:
    
        if (r3.getReturnCode() == 1) goto L640;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0953, code lost:
    
        r3.setReturnCode(r2);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r3.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0966, code lost:
    
        android.os.Trace.traceEnd(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0969, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x096a, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x096b, code lost:
    
        android.os.Trace.traceEnd(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0110, code lost:
    
        if (r1.getReturnCode() != r7) goto L586;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x096f, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0970, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0971, code lost:
    
        monitor-exit(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0972, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x0975, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x0976, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x0978, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x0979, code lost:
    
        r2 = r8 == true ? 1 : 0;
        r14 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0112, code lost:
    
        r1.setReturnCode(r8 == true ? 1 : 0);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r1.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0125, code lost:
    
        android.os.Trace.traceEnd(262144);
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x012b, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x017c, code lost:
    
        r15.setError(-5, "Duplicate package " + r1 + " in multi-package install request.");
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r15.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01a8, code lost:
    
        if (0 == 0) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01aa, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01b2, code lost:
    
        if (r0.hasNext() == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01b4, code lost:
    
        r2 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01bf, code lost:
    
        if (r2.getDataLoaderType() == 2) goto L553;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01c7, code lost:
    
        if (r2.getSignatureSchemeVersion() == 4) goto L554;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01ca, code lost:
    
        r3 = r2.getPkg().getBaseApkPath();
        r4 = r2.getPkg().getSplitCodePaths();
        r5 = r2.getOriginUri();
        r6 = r34.mPm;
        r7 = r6.mPendingVerificationToken;
        r6.mPendingVerificationToken = r7 + 1;
        com.android.server.pm.VerificationUtils.broadcastPackageVerified(r7, r5, 1, com.android.server.pm.PackageManagerServiceUtils.buildVerificationRootHashString(r3, r4), r2.getDataLoaderType(), r2.getUser(), r34.mContext);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0205, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x020d, code lost:
    
        if (r0.hasNext() == false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x020f, code lost:
    
        r2 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0219, code lost:
    
        if (r2.getParsedPackage() == null) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0231, code lost:
    
        if (((java.lang.Boolean) r0.getOrDefault(r2.getParsedPackage().getPackageName(), java.lang.Boolean.valueOf(r17))).booleanValue() == false) goto L564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0233, code lost:
    
        cleanUpAppIdCreation(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0237, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x023f, code lost:
    
        if (r0.hasNext() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0241, code lost:
    
        r2 = r0.next();
        r2.closeFreezer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x024e, code lost:
    
        if (r2.getReturnCode() != r11) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0250, code lost:
    
        r8 = r17;
        r2.setReturnCode(r8 ? 1 : 0);
        r34.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", r2.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0265, code lost:
    
        r8 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0267, code lost:
    
        r17 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x026a, code lost:
    
        android.os.Trace.traceEnd(r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x026d, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x028f, code lost:
    
        r15.setError(-7, "Update attempted to change value of android.internal.PROPERTY_NO_APP_DATA_STORAGE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0295, code lost:
    
        if (0 == 0) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0297, code lost:
    
        r0 = r35.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x029f, code lost:
    
        if (r0.hasNext() == false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02a1, code lost:
    
        r2 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02ac, code lost:
    
        if (r2.getDataLoaderType() == 2) goto L534;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:144:0x039f A[Catch: PackageManagerException -> 0x04c6, all -> 0x06c4, TryCatch #3 {PackageManagerException -> 0x04c6, blocks: (B:142:0x0398, B:144:0x039f, B:145:0x03b3), top: B:435:0x0398 }] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x03b3 A[Catch: PackageManagerException -> 0x04c6, all -> 0x06c4, TRY_LEAVE, TryCatch #3 {PackageManagerException -> 0x04c6, blocks: (B:142:0x0398, B:144:0x039f, B:145:0x03b3), top: B:435:0x0398 }] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x03c5 A[Catch: all -> 0x06c4, TryCatch #16 {all -> 0x06c4, blocks: (B:142:0x0398, B:144:0x039f, B:147:0x03bf, B:149:0x03c5, B:151:0x03e3, B:154:0x03ea, B:155:0x03ef, B:145:0x03b3, B:203:0x04eb, B:271:0x06bc, B:272:0x06c3, B:239:0x05f5, B:8:0x0046), top: B:435:0x0398, inners: #26 }] */
    /* JADX WARN: Removed duplicated region for block: B:476:0x04be A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v10 */
    /* JADX WARN: Type inference failed for: r11v11 */
    /* JADX WARN: Type inference failed for: r11v12 */
    /* JADX WARN: Type inference failed for: r11v16 */
    /* JADX WARN: Type inference failed for: r11v17 */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r14v0 */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v12 */
    /* JADX WARN: Type inference failed for: r14v13 */
    /* JADX WARN: Type inference failed for: r14v15 */
    /* JADX WARN: Type inference failed for: r14v17 */
    /* JADX WARN: Type inference failed for: r14v18 */
    /* JADX WARN: Type inference failed for: r14v19 */
    /* JADX WARN: Type inference failed for: r14v2, types: [long] */
    /* JADX WARN: Type inference failed for: r14v20 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v5 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v7, types: [long] */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r14v9 */
    /* JADX WARN: Type inference failed for: r15v10, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r15v12, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r15v3, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r15v4, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r15v9 */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v6 */
    /* JADX WARN: Type inference failed for: r17v8 */
    /* JADX WARN: Type inference failed for: r1v33, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r1v61, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r21v4 */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v107 */
    /* JADX WARN: Type inference failed for: r2v11, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r2v110 */
    /* JADX WARN: Type inference failed for: r2v111 */
    /* JADX WARN: Type inference failed for: r2v112 */
    /* JADX WARN: Type inference failed for: r2v125, types: [int] */
    /* JADX WARN: Type inference failed for: r2v126 */
    /* JADX WARN: Type inference failed for: r2v130 */
    /* JADX WARN: Type inference failed for: r2v131 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r2v38 */
    /* JADX WARN: Type inference failed for: r2v39 */
    /* JADX WARN: Type inference failed for: r2v51 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v79 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v81 */
    /* JADX WARN: Type inference failed for: r34v0, types: [com.android.server.pm.InstallPackageHelper] */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.server.pm.InstallRequest] */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v13 */
    /* JADX WARN: Type inference failed for: r7v14 */
    /* JADX WARN: Type inference failed for: r7v15 */
    /* JADX WARN: Type inference failed for: r7v16 */
    /* JADX WARN: Type inference failed for: r7v19 */
    /* JADX WARN: Type inference failed for: r7v3, types: [int] */
    /* JADX WARN: Type inference failed for: r7v31, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r7v32 */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v13, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v19 */
    /* JADX WARN: Type inference failed for: r8v20 */
    /* JADX WARN: Type inference failed for: r8v22 */
    /* JADX WARN: Type inference failed for: r8v27, types: [boolean] */
    /* JADX WARN: Type inference failed for: r8v32 */
    /* JADX WARN: Type inference failed for: r8v47 */
    /* JADX WARN: Type inference failed for: r8v48 */
    /* JADX WARN: Type inference failed for: r8v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r8v9 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void installPackagesLI(java.util.List<com.android.server.pm.InstallRequest> r35) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2628
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.installPackagesLI(java.util.List):void");
    }

    private boolean checkNoAppStorageIsConsistent(com.android.server.pm.pkg.AndroidPackage oldPkg, com.android.server.pm.pkg.AndroidPackage newPkg) {
        if (oldPkg == null) {
            return true;
        }
        android.content.pm.PackageManager.Property curProp = (android.content.pm.PackageManager.Property) oldPkg.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        android.content.pm.PackageManager.Property newProp = (android.content.pm.PackageManager.Property) newPkg.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        if (curProp == null || !curProp.getBoolean()) {
            if (newProp == null || !newProp.getBoolean()) {
                return true;
            }
            return false;
        }
        if (newProp != null && newProp.getBoolean()) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:681:0x1148, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x114b, code lost:
    
        r2 = null;
        r35 = null;
        r6 = null;
        r4 = r4;
        r3 = false;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 25, insn: 0x0462: MOVE (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r25 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('replace' boolean)]), block:B:188:0x0459 */
    /* JADX WARN: Not initialized variable reg: 41, insn: 0x03c5: MOVE (r26 I:??[OBJECT, ARRAY]) = (r41 I:??[OBJECT, ARRAY] A[D('tmpPackageFile' java.io.File)]), block:B:167:0x03ba */
    /* JADX WARN: Not initialized variable reg: 42, insn: 0x03c7: MOVE (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r42 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('systemApp' boolean)]), block:B:167:0x03ba */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0264  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0484  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0517 A[Catch: all -> 0x04f7, TRY_ENTER, TryCatch #33 {all -> 0x04f7, blocks: (B:200:0x04c5, B:202:0x04cb, B:204:0x04d7, B:206:0x04dd, B:215:0x0517, B:217:0x051f, B:222:0x0532), top: B:808:0x04c5 }] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x061d  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0625 A[Catch: all -> 0x0662, TryCatch #20 {all -> 0x0662, blocks: (B:259:0x0625, B:261:0x0629, B:262:0x0641, B:273:0x0682, B:275:0x0699, B:277:0x069f, B:279:0x06a3, B:283:0x06b1, B:286:0x06ba, B:287:0x070a, B:298:0x0743, B:232:0x0579, B:233:0x0595, B:237:0x05b0, B:239:0x05d7, B:240:0x05de, B:243:0x05e7, B:248:0x05ee, B:249:0x05f1, B:253:0x05f7, B:254:0x0604), top: B:785:0x052c, inners: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0682 A[Catch: all -> 0x0662, TRY_ENTER, TryCatch #20 {all -> 0x0662, blocks: (B:259:0x0625, B:261:0x0629, B:262:0x0641, B:273:0x0682, B:275:0x0699, B:277:0x069f, B:279:0x06a3, B:283:0x06b1, B:286:0x06ba, B:287:0x070a, B:298:0x0743, B:232:0x0579, B:233:0x0595, B:237:0x05b0, B:239:0x05d7, B:240:0x05de, B:243:0x05e7, B:248:0x05ee, B:249:0x05f1, B:253:0x05f7, B:254:0x0604), top: B:785:0x052c, inners: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:358:0x09bc  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x09e7  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x09ef  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0a40  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x0ae1  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x0b02  */
    /* JADX WARN: Removed duplicated region for block: B:430:0x0b1b  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x0b35  */
    /* JADX WARN: Removed duplicated region for block: B:667:0x10e5  */
    /* JADX WARN: Removed duplicated region for block: B:709:0x1211  */
    /* JADX WARN: Removed duplicated region for block: B:731:0x1294  */
    /* JADX WARN: Removed duplicated region for block: B:764:0x0676 A[EXC_TOP_SPLITTER, PHI: r42
  0x0676: PHI (r42v12 boolean) = (r42v11 boolean), (r42v13 boolean) binds: [B:258:0x0623, B:265:0x064d] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:776:0x0b43 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:799:0x03cf A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:825:0x052e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:850:0x0723 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:858:0x0134 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:887:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01da  */
    /* JADX WARN: Type inference failed for: r27v21 */
    /* JADX WARN: Type inference failed for: r27v22 */
    /* JADX WARN: Type inference failed for: r27v23 */
    /* JADX WARN: Type inference failed for: r27v24 */
    /* JADX WARN: Type inference failed for: r27v5 */
    /* JADX WARN: Type inference failed for: r27v6 */
    /* JADX WARN: Type inference failed for: r2v105 */
    /* JADX WARN: Type inference failed for: r2v108 */
    /* JADX WARN: Type inference failed for: r2v109 */
    /* JADX WARN: Type inference failed for: r2v111 */
    /* JADX WARN: Type inference failed for: r2v158 */
    /* JADX WARN: Type inference failed for: r2v61 */
    /* JADX WARN: Type inference failed for: r2v62 */
    /* JADX WARN: Type inference failed for: r2v63 */
    /* JADX WARN: Type inference failed for: r2v68 */
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
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 10 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void preparePackageLI(com.android.server.pm.InstallRequest r50) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 4841
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.preparePackageLI(com.android.server.pm.InstallRequest):void");
    }

    private void doRenameLI(com.android.server.pm.InstallRequest request, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) throws com.android.server.pm.PrepareFailure {
        int status = request.getReturnCode();
        java.lang.String statusMsg = request.getReturnMsg();
        if (request.isInstallMove()) {
            if (status != 1) {
                this.mRemovePackageHelper.cleanUpForMoveInstall(request.getMoveToUuid(), request.getMovePackageName(), request.getMoveFromCodePath());
                throw new com.android.server.pm.PrepareFailure(status, statusMsg);
            }
            return;
        }
        if (status != 1) {
            this.mRemovePackageHelper.removeCodePath(request.getCodeFile());
            throw new com.android.server.pm.PrepareFailure(status, statusMsg);
        }
        java.io.File targetDir = resolveTargetDir(request.getInstallFlags(), request.getCodeFile());
        java.io.File beforeCodeFile = request.getCodeFile();
        java.io.File afterCodeFile = com.android.server.pm.PackageManagerServiceUtils.getNextCodePath(targetDir, parsedPackage.getPackageName());
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "Renaming " + beforeCodeFile + " to " + afterCodeFile);
        }
        boolean onIncremental = this.mPm.mIncrementalManager != null && android.os.incremental.IncrementalManager.isIncrementalPath(beforeCodeFile.getAbsolutePath());
        try {
            com.android.server.pm.PackageManagerServiceUtils.makeDirRecursive(afterCodeFile.getParentFile(), 505);
            if (onIncremental) {
                this.mPm.mIncrementalManager.linkCodePath(beforeCodeFile, afterCodeFile);
            } else {
                android.system.Os.rename(beforeCodeFile.getAbsolutePath(), afterCodeFile.getAbsolutePath());
            }
            if (!onIncremental && !android.os.SELinux.restoreconRecursive(afterCodeFile)) {
                android.util.Slog.w("PackageManager", "Failed to restorecon");
                throw new com.android.server.pm.PrepareFailure(-20, "Failed to restorecon");
            }
            request.setCodeFile(afterCodeFile);
            try {
                parsedPackage.setPath(afterCodeFile.getCanonicalPath());
                parsedPackage.setBaseApkPath(android.os.FileUtils.rewriteAfterRename(beforeCodeFile, afterCodeFile, parsedPackage.getBaseApkPath()));
                parsedPackage.setSplitCodePaths(android.os.FileUtils.rewriteAfterRename(beforeCodeFile, afterCodeFile, parsedPackage.getSplitCodePaths()));
            } catch (java.io.IOException e) {
                android.util.Slog.e("PackageManager", "Failed to get path: " + afterCodeFile, e);
                throw new com.android.server.pm.PrepareFailure(-20, "Failed to get path: " + afterCodeFile);
            }
        } catch (android.system.ErrnoException | java.io.IOException e2) {
            android.util.Slog.w("PackageManager", "Failed to rename", e2);
            throw new com.android.server.pm.PrepareFailure(-4, "Failed to rename");
        }
    }

    private java.io.File resolveTargetDir(int installFlags, java.io.File codeFile) {
        boolean isStagedInstall = (2097152 & installFlags) != 0;
        if (isStagedInstall) {
            return android.os.Environment.getDataAppDirectory(null);
        }
        return codeFile.getParentFile();
    }

    private static boolean cannotInstallWithBadPermissionGroups(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        return parsedPackage.getTargetSdkVersion() >= 31;
    }

    private boolean doesSignatureMatchForPermissions(java.lang.String sourcePackageName, com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int scanFlags) {
        com.android.server.pm.PackageSetting sourcePackageSetting;
        com.android.server.pm.KeySetManagerService ksms;
        com.android.server.pm.SharedUserSetting sharedUserSetting;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                sourcePackageSetting = this.mPm.mSettings.getPackageLPr(sourcePackageName);
                ksms = this.mPm.mSettings.getKeySetManagerService();
                sharedUserSetting = this.mPm.mSettings.getSharedUserSettingLPr(sourcePackageSetting);
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        android.content.pm.SigningDetails sourceSigningDetails = sourcePackageSetting == null ? android.content.pm.SigningDetails.UNKNOWN : sourcePackageSetting.getSigningDetails();
        if (sourcePackageName.equals(parsedPackage.getPackageName()) && ksms.shouldCheckUpgradeKeySetLocked(sourcePackageSetting, sharedUserSetting, scanFlags)) {
            return ksms.checkUpgradeKeySetLocked(sourcePackageSetting, parsedPackage);
        }
        if (sourceSigningDetails.checkCapability(parsedPackage.getSigningDetails(), 4)) {
            return true;
        }
        if (parsedPackage.getSigningDetails().checkCapability(sourceSigningDetails, 4)) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    sourcePackageSetting.setSigningDetails(parsedPackage.getSigningDetails());
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return true;
        }
        return false;
    }

    private void setUpFsVerity(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PrepareFailure, java.security.NoSuchAlgorithmException, java.security.DigestException, java.io.IOException, com.android.server.pm.Installer.InstallerException {
        if (!com.android.server.pm.PackageManagerServiceUtils.isApkVerityEnabled()) {
            return;
        }
        if (android.os.incremental.IncrementalManager.isIncrementalPath(pkg.getPath()) && android.os.incremental.IncrementalManager.getVersion() < 2) {
            return;
        }
        android.util.ArrayMap<java.lang.String, java.lang.String> fsverityCandidates = new android.util.ArrayMap<>();
        fsverityCandidates.put(pkg.getBaseApkPath(), com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(pkg.getBaseApkPath()));
        java.lang.String dmPath = android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForApk(pkg.getBaseApkPath());
        if (new java.io.File(dmPath).exists()) {
            fsverityCandidates.put(dmPath, com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(dmPath));
        }
        for (java.lang.String path : pkg.getSplitCodePaths()) {
            fsverityCandidates.put(path, com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(path));
            java.lang.String splitDmPath = android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForApk(path);
            if (new java.io.File(splitDmPath).exists()) {
                fsverityCandidates.put(splitDmPath, com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(splitDmPath));
            }
        }
        com.android.server.security.FileIntegrityService fis = com.android.server.security.FileIntegrityService.getService();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : fsverityCandidates.entrySet()) {
            try {
                java.lang.String filePath = entry.getKey();
                if (!com.android.internal.security.VerityUtils.hasFsverity(filePath)) {
                    java.lang.String signaturePath = entry.getValue();
                    if (new java.io.File(signaturePath).exists()) {
                        com.android.internal.security.VerityUtils.setUpFsverity(filePath);
                        if (!fis.verifyPkcs7DetachedSignature(signaturePath, filePath)) {
                            throw new com.android.server.pm.PrepareFailure(-118, "fs-verity signature does not verify against a known key");
                        }
                    } else {
                        continue;
                    }
                }
            } catch (java.io.IOException e) {
                throw new com.android.server.pm.PrepareFailure(-118, "Failed to enable fs-verity: " + e);
            }
        }
    }

    private com.android.server.pm.PackageFreezer freezePackageForInstall(java.lang.String packageName, int userId, int installFlags, java.lang.String killReason, int exitInfoReason, com.android.server.pm.InstallRequest request) {
        if ((installFlags & 4096) != 0) {
            return new com.android.server.pm.PackageFreezer(this.mPm, request);
        }
        return this.mPm.freezePackage(packageName, userId, killReason, exitInfoReason, request);
    }

    private static void updateDigest(java.security.MessageDigest digest, java.io.File file) throws java.io.IOException {
        java.security.DigestInputStream digestStream = new java.security.DigestInputStream(new java.io.FileInputStream(file), digest);
        int total = 0;
        while (true) {
            try {
                int length = digestStream.read();
                if (length != -1) {
                    total += length;
                } else {
                    digestStream.close();
                    return;
                }
            } catch (java.lang.Throwable th) {
                try {
                    digestStream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    private void commitPackagesLocked(java.util.List<com.android.server.pm.ReconciledPackage> reconciledPackages, int[] allUsers) throws java.lang.Throwable {
        com.android.server.pm.PackageSetting ps2;
        for (com.android.server.pm.ReconciledPackage reconciledPkg : reconciledPackages) {
            com.android.server.pm.InstallRequest installRequest = reconciledPkg.mInstallRequest;
            com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage = installRequest.getParsedPackage();
            java.lang.String packageName = parsedPackage.getPackageName();
            installRequest.onCommitStarted();
            if (installRequest.isInstallReplace()) {
                com.android.server.pm.pkg.AndroidPackage oldPackage = this.mPm.mPackages.get(packageName);
                com.android.server.pm.pkg.PackageStateInternal deletedPkgSetting = this.mPm.snapshotComputer().getPackageStateInternal(packageName);
                installRequest.setScannedPackageSettingFirstInstallTimeFromReplaced(deletedPkgSetting, allUsers);
                installRequest.setScannedPackageSettingLastUpdateTime(java.lang.System.currentTimeMillis());
                installRequest.getRemovedInfo().mBroadcastAllowList = this.mPm.mAppsFilter.getVisibilityAllowList(this.mPm.snapshotComputer(), installRequest.getScannedPackageSetting(), allUsers, this.mPm.mSettings.getPackagesLocked());
                if (installRequest.isInstallSystem()) {
                    this.mRemovePackageHelper.removePackage(oldPackage, true);
                    if (!disableSystemPackageLPw(oldPackage)) {
                        installRequest.getRemovedInfo().mArgs = new com.android.server.pm.CleanUpArgs(packageName, oldPackage.getPath(), com.android.server.pm.InstructionSets.getAppDexInstructionSets(deletedPkgSetting.getPrimaryCpuAbi(), deletedPkgSetting.getSecondaryCpuAbi()));
                    } else {
                        installRequest.getRemovedInfo().mArgs = null;
                    }
                } else {
                    try {
                    } catch (com.android.server.pm.SystemDeleteException e) {
                        e = e;
                    }
                    try {
                        this.mDeletePackageHelper.executeDeletePackage(reconciledPkg.mDeletePackageAction, packageName, true, allUsers, false);
                        this.mPm.mPackageManagerServiceExt.deleteRemovableAppResources(packageName, oldPackage);
                    } catch (com.android.server.pm.SystemDeleteException e2) {
                        e = e2;
                        if (this.mPm.mIsEngBuild) {
                            throw new java.lang.RuntimeException("Unexpected failure", e);
                        }
                    }
                    if (installRequest.getReturnCode() == 1 && (ps2 = this.mPm.mSettings.getPackageLPr(parsedPackage.getPackageName())) != null) {
                        installRequest.getRemovedInfo().mRemovedForAllUsers = this.mPm.mPackages.get(ps2.getPackageName()) == null;
                    }
                }
            }
            com.android.server.pm.pkg.AndroidPackage pkg = commitReconciledScanResultLocked(reconciledPkg, allUsers);
            updateSettingsLI(pkg, allUsers, installRequest);
            com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(packageName);
            if (ps != null) {
                installRequest.setNewUsers(ps.queryInstalledUsers(allUsers, true));
                ps.setUpdateAvailable(false);
                this.mPm.mPackageManagerServiceExt.notifyPackageAddOrUpdateForAbiInfo(packageName, ps);
            }
            if (installRequest.getReturnCode() == 1) {
                this.mPm.markPackageAsArchivedIfNeeded(ps, installRequest.getArchivedPackage(), installRequest.getResponsibleInstallerTitles(), installRequest.getNewUsers());
                this.mPm.updateSequenceNumberLP(ps, installRequest.getNewUsers());
                this.mPm.updateInstantAppInstallerLocked(packageName);
            }
            installRequest.onCommitFinished();
        }
        android.app.ApplicationPackageManager.invalidateGetPackagesForUidCache();
    }

    private boolean disableSystemPackageLPw(com.android.server.pm.pkg.AndroidPackage oldPkg) {
        return this.mPm.mSettings.disableSystemPackageLPw(oldPkg.getPackageName(), true);
    }

    private void updateSettingsLI(com.android.server.pm.pkg.AndroidPackage newPackage, int[] allUsers, com.android.server.pm.InstallRequest installRequest) {
        updateSettingsInternalLI(newPackage, allUsers, installRequest);
    }

    private void updateSettingsInternalLI(com.android.server.pm.pkg.AndroidPackage pkg, int[] allUsers, com.android.server.pm.InstallRequest installRequest) {
        int i;
        java.util.List<java.lang.String> allowlistedRestrictedPermissions;
        com.android.server.pm.pkg.SharedLibraryWrapper sharedLib;
        int i2;
        boolean installed;
        android.os.Trace.traceBegin(262144L, "updateSettingsInternal");
        java.lang.String pkgName = pkg.getPackageName();
        int[] installedForUsers = installRequest.getOriginUsers();
        int installReason = installRequest.getInstallReason();
        java.lang.String installerPackageName = installRequest.getInstallerPackageName();
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "New package installed in " + pkg.getPath());
        }
        int userId = installRequest.getUserId();
        if (userId != -1 && userId != -2 && !this.mPm.mUserManager.exists(userId)) {
            installRequest.setError(com.android.server.pm.PackageManagerException.ofInternalError("User " + userId + " doesn't exist or has been removed", -38));
            return;
        }
        this.mPm.mPackageManagerServiceSocExt.acquireUxPerfLockPkgInstall(pkgName);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(pkgName);
                if (ps != null) {
                    if (ps.isSystem()) {
                        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                            android.util.Slog.d("PackageManager", "Implicitly enabling system package on upgrade: " + pkgName);
                        }
                        if (installedForUsers != null && !installRequest.isApplicationEnabledSettingPersistent()) {
                            for (int origUserId : installedForUsers) {
                                if (userId == -1 || userId == origUserId) {
                                    ps.setEnabled(0, origUserId, installerPackageName);
                                }
                            }
                        }
                        if (allUsers != null && installedForUsers != null) {
                            int length = allUsers.length;
                            int i3 = 0;
                            while (i3 < length) {
                                int currentUserId = allUsers[i3];
                                boolean installed2 = com.android.internal.util.ArrayUtils.contains(installedForUsers, currentUserId);
                                if (!com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                                    i2 = length;
                                    installed = installed2;
                                } else {
                                    i2 = length;
                                    installed = installed2;
                                    android.util.Slog.d("PackageManager", "    user " + currentUserId + " => " + installed);
                                }
                                ps.setInstalled(installed, currentUserId);
                                i3++;
                                length = i2;
                            }
                        }
                        if (allUsers != null) {
                            for (int i4 : allUsers) {
                                ps.resetOverrideComponentLabelIcon(i4);
                            }
                        }
                    }
                    if (!ps.getPkgState().getUsesLibraryInfos().isEmpty()) {
                        java.util.Iterator<com.android.server.pm.pkg.SharedLibraryWrapper> it = ps.getPkgState().getUsesLibraryInfos().iterator();
                        while (it.hasNext()) {
                            com.android.server.pm.pkg.SharedLibraryWrapper sharedLib2 = it.next();
                            int length2 = allUsers.length;
                            int i5 = 0;
                            while (i5 < length2) {
                                int currentUserId2 = allUsers[i5];
                                java.util.Iterator<com.android.server.pm.pkg.SharedLibraryWrapper> it2 = it;
                                int i6 = length2;
                                if (sharedLib2.getType() != 1) {
                                    sharedLib = sharedLib2;
                                } else {
                                    com.android.server.pm.PackageSetting libPs = this.mPm.mSettings.getPackageLPr(sharedLib2.getPackageName());
                                    if (libPs == null) {
                                        sharedLib = sharedLib2;
                                    } else {
                                        sharedLib = sharedLib2;
                                        ps.setOverlayPathsForLibrary(sharedLib2.getName(), libPs.getOverlayPaths(currentUserId2), currentUserId2);
                                    }
                                }
                                i5++;
                                it = it2;
                                length2 = i6;
                                sharedLib2 = sharedLib;
                            }
                        }
                    }
                    if (userId != -1) {
                        ps.setInstalled(true, userId);
                        if (!installRequest.isApplicationEnabledSettingPersistent()) {
                            ps.setEnabled(0, userId, installerPackageName);
                        }
                        this.mPm.mInstallerService.mPackageArchiver.clearArchiveState(ps, userId);
                    } else if (allUsers != null) {
                        int length3 = allUsers.length;
                        int i7 = 0;
                        while (i7 < length3) {
                            int currentUserId3 = allUsers[i7];
                            if (this.mPm.mPackageManagerServiceExt.skipInstallInMultiUser(currentUserId3, pkgName)) {
                                i = length3;
                            } else {
                                boolean installedForCurrentUser = com.android.internal.util.ArrayUtils.contains(installedForUsers, currentUserId3);
                                i = length3;
                                boolean restrictedByPolicy = this.mPm.isUserRestricted(currentUserId3, "no_install_apps") || this.mPm.isUserRestricted(currentUserId3, "no_debugging_features");
                                if (installedForCurrentUser || !restrictedByPolicy) {
                                    ps.setInstalled(true, currentUserId3);
                                    if (!installRequest.isApplicationEnabledSettingPersistent()) {
                                        ps.setEnabled(0, currentUserId3, installerPackageName);
                                    }
                                    this.mPm.mInstallerService.mPackageArchiver.clearArchiveState(ps, currentUserId3);
                                } else {
                                    ps.setInstalled(false, currentUserId3);
                                }
                            }
                            i7++;
                            length3 = i;
                        }
                    }
                    this.mPm.mSettings.addInstallerPackageNames(ps.getInstallSource());
                    java.util.Set<java.lang.Integer> previousUserIds = new android.util.ArraySet<>();
                    if (installRequest.getRemovedInfo() != null && installRequest.getRemovedInfo().mInstallReasons != null) {
                        int i8 = 0;
                        for (int installReasonCount = installRequest.getRemovedInfo().mInstallReasons.size(); i8 < installReasonCount; installReasonCount = installReasonCount) {
                            int previousUserId = installRequest.getRemovedInfo().mInstallReasons.keyAt(i8);
                            int previousInstallReason = installRequest.getRemovedInfo().mInstallReasons.valueAt(i8);
                            ps.setInstallReason(previousInstallReason, previousUserId);
                            previousUserIds.add(java.lang.Integer.valueOf(previousUserId));
                            i8++;
                        }
                    }
                    if (installRequest.getRemovedInfo() != null && installRequest.getRemovedInfo().mUninstallReasons != null) {
                        for (int i9 = 0; i9 < installRequest.getRemovedInfo().mUninstallReasons.size(); i9++) {
                            int previousUserId2 = installRequest.getRemovedInfo().mUninstallReasons.keyAt(i9);
                            int previousReason = installRequest.getRemovedInfo().mUninstallReasons.valueAt(i9);
                            ps.setUninstallReason(previousReason, previousUserId2);
                        }
                    }
                    if (userId == -1) {
                        for (int currentUserId4 : allUsers) {
                            if (!previousUserIds.contains(java.lang.Integer.valueOf(currentUserId4)) && ps.getInstalled(currentUserId4)) {
                                ps.setInstallReason(installReason, currentUserId4);
                            }
                        }
                    } else if (!previousUserIds.contains(java.lang.Integer.valueOf(userId))) {
                        ps.setInstallReason(installReason, userId);
                    }
                    java.lang.String codePath = ps.getPathString();
                    if (android.os.incremental.IncrementalManager.isIncrementalPath(codePath) && this.mIncrementalManager != null) {
                        this.mIncrementalManager.registerLoadingProgressCallback(codePath, new com.android.server.pm.IncrementalProgressListener(ps.getPackageName(), this.mPm));
                    }
                    for (int currentUserId5 : allUsers) {
                        if (ps.getInstalled(currentUserId5)) {
                            ps.setUninstallReason(0, currentUserId5);
                        }
                    }
                    this.mPm.mSettings.writeKernelMappingLPr(ps);
                    com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.Builder permissionParamsBuilder = new com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.Builder();
                    boolean grantRequestedPermissions = (installRequest.getInstallFlags() & 256) != 0;
                    if (grantRequestedPermissions) {
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates = new android.util.ArrayMap<>();
                        for (java.lang.String permissionName : pkg.getRequestedPermissions()) {
                            permissionStates.put(permissionName, 1);
                        }
                        permissionParamsBuilder.setPermissionStates(permissionStates);
                    } else {
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates2 = installRequest.getPermissionStates();
                        if (permissionStates2 != null) {
                            permissionParamsBuilder.setPermissionStates(permissionStates2);
                        }
                    }
                    boolean allowlistAllRestrictedPermissions = (installRequest.getInstallFlags() & 4194304) != 0;
                    if (allowlistAllRestrictedPermissions) {
                        allowlistedRestrictedPermissions = new java.util.ArrayList<>(pkg.getRequestedPermissions());
                    } else {
                        allowlistedRestrictedPermissions = installRequest.getAllowlistedRestrictedPermissions();
                    }
                    if (allowlistedRestrictedPermissions != null) {
                        permissionParamsBuilder.setAllowlistedRestrictedPermissions(allowlistedRestrictedPermissions);
                    }
                    int autoRevokePermissionsMode = installRequest.getAutoRevokePermissionsMode();
                    permissionParamsBuilder.setAutoRevokePermissionsMode(autoRevokePermissionsMode);
                    this.mPm.mPermissionManager.onPackageInstalled(pkg, installRequest.getPreviousAppId(), permissionParamsBuilder.build(), userId);
                }
                installRequest.setName(pkgName);
                installRequest.setAppId(pkg.getUid());
                installRequest.setPkg(pkg);
                installRequest.setReturnCode(1);
                android.os.Trace.traceBegin(262144L, "writeSettings");
                this.mPm.writeSettingsLPrTEMP();
                android.os.Trace.traceEnd(262144L);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        android.os.Trace.traceEnd(262144L);
    }

    private void setAccessRestrictedSettingsMode(java.lang.String pkgName, int appId, int userId, int mode) {
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mPm.mContext.getSystemService(android.app.AppOpsManager.class);
        int uid = android.os.UserHandle.getUid(userId, appId);
        appOpsManager.setMode(119, uid, pkgName, mode);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    private void executePostCommitStepsLIF(java.util.List<com.android.server.pm.ReconciledPackage> reconciledPackages) {
        android.util.ArraySet<android.os.incremental.IncrementalStorage> incrementalStorages;
        android.util.ArraySet<android.os.incremental.IncrementalStorage> incrementalStorages2 = new android.util.ArraySet<>();
        for (com.android.server.pm.ReconciledPackage reconciledPkg : reconciledPackages) {
            com.android.server.pm.InstallRequest installRequest = reconciledPkg.mInstallRequest;
            com.android.server.pm.PackageSetting ps = installRequest.getScannedPackageSetting();
            java.lang.String packageName = ps.getPackageName();
            java.lang.String codePath = ps.getPathString();
            com.android.server.pm.pkg.AndroidPackage pkg = ps.getPkg();
            boolean onIncremental = this.mIncrementalManager != null && android.os.incremental.IncrementalManager.isIncrementalPath(codePath);
            if (onIncremental) {
                android.os.incremental.IncrementalStorage storage = this.mIncrementalManager.openStorage(codePath);
                if (storage == null) {
                    throw new java.lang.IllegalArgumentException("Install: null storage for incremental package " + packageName);
                }
                incrementalStorages2.add(storage);
            }
            this.mAppDataHelper.prepareAppDataPostCommitLIF(ps, 0, installRequest.getNewUsers());
            if (installRequest.isClearCodeCache()) {
                this.mAppDataHelper.clearAppDataLIF(ps.getPkg(), -1, 39);
            }
            if (installRequest.isInstallReplace() && pkg != null) {
                this.mDexManager.notifyPackageUpdated(packageName, pkg.getBaseApkPath(), pkg.getSplitCodePaths());
                this.mPm.mPackageManagerServiceExt.afterNotifyUpdateForDexInExecutePostCommitSteps(pkg);
            }
            if (pkg != null) {
                this.mPm.mPackageManagerServiceExt.hookInExecutePostCommitStepsLIF(pkg.getPackageName());
            }
            boolean didPreWork = this.mPm.mPackageManagerServiceExt.doPreWorkBeforeDexOptInExecutePostCommitSteps(pkg);
            com.android.server.pm.dex.DexoptOptions dexoptOptions = this.mPm.mPackageManagerServiceExt.modifyDexoptOptionsBeforDo(reconciledPkg.mInstallRequest.getWrapper().getInstallArgs().mInstallArgsExt, com.android.server.pm.DexOptHelper.getDexoptOptionsByInstallRequest(installRequest, this.mDexManager));
            boolean performDexopt = com.android.server.pm.DexOptHelper.shouldPerformDexopt(installRequest, dexoptOptions, this.mContext);
            if (!performDexopt) {
                incrementalStorages = incrementalStorages2;
            } else {
                com.android.server.pm.PackageManagerTracedLock.RawLock installLock = this.mPm.mInstallLock.getRawLock();
                installLock.unlock();
                incrementalStorages = incrementalStorages2;
                try {
                    android.os.Trace.traceBegin(262144L, "dexopt");
                    com.android.server.pm.PackageSetting realPkgSetting = installRequest.getRealPackageSetting();
                    boolean isUpdatedSystemApp = installRequest.getScannedPackageSetting().isUpdatedSystemApp();
                    realPkgSetting.getPkgState().setUpdatedSystemApp(isUpdatedSystemApp);
                    com.android.server.art.model.DexoptResult dexOptResult = com.android.server.pm.DexOptHelper.dexoptPackageUsingArtService(installRequest, dexoptOptions);
                    installRequest.onDexoptFinished(dexOptResult);
                    android.os.Trace.traceEnd(262144L);
                    installLock.lock();
                } catch (java.lang.Throwable th) {
                    installLock.lock();
                    throw th;
                }
            }
            this.mPm.mPackageManagerServiceExt.afterDexOptInExecutePostCommitSteps(pkg, packageName, didPreWork);
            incrementalStorages2 = incrementalStorages;
        }
        com.android.server.pm.PackageManagerServiceUtils.waitForNativeBinariesExtractionForIncremental(incrementalStorages2);
    }

    android.util.Pair<java.lang.Integer, java.lang.String> verifyReplacingVersionCode(android.content.pm.PackageInfoLite pkgLite, long requiredInstalledVersionCode, int installFlags) {
        if ((131072 & installFlags) != 0) {
            return verifyReplacingVersionCodeForApex(pkgLite, requiredInstalledVersionCode, installFlags);
        }
        java.lang.String packageName = pkgLite.packageName;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.pkg.AndroidPackage dataOwnerPkg = this.mPm.mPackages.get(packageName);
                com.android.server.pm.PackageSetting dataOwnerPs = this.mPm.mSettings.getPackageLPr(packageName);
                if (dataOwnerPkg == null && dataOwnerPs != null) {
                    dataOwnerPkg = dataOwnerPs.getPkg();
                }
                if (requiredInstalledVersionCode != -1) {
                    if (dataOwnerPkg == null) {
                        java.lang.String errorMsg = "Required installed version code was " + requiredInstalledVersionCode + " but package is not installed";
                        android.util.Slog.w("PackageManager", errorMsg);
                        android.util.Pair<java.lang.Integer, java.lang.String> pairCreate = android.util.Pair.create(-121, errorMsg);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return pairCreate;
                    }
                    if (dataOwnerPkg.getLongVersionCode() != requiredInstalledVersionCode) {
                        java.lang.String errorMsg2 = "Required installed version code was " + requiredInstalledVersionCode + " but actual installed version is " + dataOwnerPkg.getLongVersionCode();
                        android.util.Slog.w("PackageManager", errorMsg2);
                        android.util.Pair<java.lang.Integer, java.lang.String> pairCreate2 = android.util.Pair.create(-121, errorMsg2);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return pairCreate2;
                    }
                }
                if (dataOwnerPkg != null && !dataOwnerPkg.isSdkLibrary()) {
                    if (!com.android.server.pm.PackageManagerServiceUtils.isDowngradePermitted(installFlags, dataOwnerPkg.isDebuggable())) {
                        try {
                            com.android.server.pm.PackageManagerServiceUtils.checkDowngrade(dataOwnerPkg, pkgLite);
                        } catch (com.android.server.pm.PackageManagerException e) {
                            java.lang.String errorMsg3 = "Downgrade detected: " + e.getMessage();
                            android.util.Slog.w("PackageManager", errorMsg3);
                            android.util.Pair<java.lang.Integer, java.lang.String> pairCreate3 = android.util.Pair.create(-25, errorMsg3);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return pairCreate3;
                        }
                    } else if (dataOwnerPs.isSystem()) {
                        com.android.server.pm.PackageSetting disabledPs = this.mPm.mSettings.getDisabledSystemPkgLPr(dataOwnerPs);
                        if (disabledPs != null) {
                            dataOwnerPkg = disabledPs.getPkg();
                        }
                        if (!android.os.Build.IS_DEBUGGABLE && !dataOwnerPkg.isDebuggable()) {
                            try {
                                com.android.server.pm.PackageManagerServiceUtils.checkDowngrade(dataOwnerPkg, pkgLite);
                            } catch (com.android.server.pm.PackageManagerException e2) {
                                java.lang.String errorMsg4 = "System app: " + packageName + " cannot be downgraded to older than its preloaded version on the system image. " + e2.getMessage();
                                android.util.Slog.w("PackageManager", errorMsg4);
                                android.util.Pair<java.lang.Integer, java.lang.String> pairCreate4 = android.util.Pair.create(-25, errorMsg4);
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                return pairCreate4;
                            }
                        }
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return android.util.Pair.create(1, null);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private android.util.Pair<java.lang.Integer, java.lang.String> verifyReplacingVersionCodeForApex(android.content.pm.PackageInfoLite pkgLite, long requiredInstalledVersionCode, int installFlags) {
        java.lang.String packageName = pkgLite.packageName;
        android.content.pm.PackageInfo activePackage = this.mPm.snapshotComputer().getPackageInfo(packageName, 1073741824L, 0);
        if (activePackage == null) {
            java.lang.String errorMsg = "Attempting to install new APEX package " + packageName;
            android.util.Slog.w("PackageManager", errorMsg);
            return android.util.Pair.create(-23, errorMsg);
        }
        long activeVersion = activePackage.getLongVersionCode();
        if (requiredInstalledVersionCode != -1 && activeVersion != requiredInstalledVersionCode) {
            java.lang.String errorMsg2 = "Installed version of APEX package " + packageName + " does not match required. Active version: " + activeVersion + " required: " + requiredInstalledVersionCode;
            android.util.Slog.w("PackageManager", errorMsg2);
            return android.util.Pair.create(-121, errorMsg2);
        }
        boolean isAppDebuggable = (activePackage.applicationInfo.flags & 2) != 0;
        long newVersionCode = pkgLite.getLongVersionCode();
        if (!com.android.server.pm.PackageManagerServiceUtils.isDowngradePermitted(installFlags, isAppDebuggable) && newVersionCode < activeVersion) {
            java.lang.String errorMsg3 = "Downgrade of APEX package " + packageName + " is not allowed. Active version: " + activeVersion + " attempted: " + newVersionCode;
            android.util.Slog.w("PackageManager", errorMsg3);
            return android.util.Pair.create(-25, errorMsg3);
        }
        return android.util.Pair.create(1, null);
    }

    int getUidForVerifier(android.content.pm.VerifierInfo verifierInfo) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.pkg.AndroidPackage pkg = this.mPm.mPackages.get(verifierInfo.packageName);
                if (pkg == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return -1;
                }
                if (pkg.getSigningDetails().getSignatures().length != 1) {
                    android.util.Slog.i("PackageManager", "Verifier package " + verifierInfo.packageName + " has more than one signature; ignoring");
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return -1;
                }
                try {
                    android.content.pm.Signature verifierSig = pkg.getSigningDetails().getSignatures()[0];
                    java.security.PublicKey publicKey = verifierSig.getPublicKey();
                    byte[] expectedPublicKey = publicKey.getEncoded();
                    byte[] actualPublicKey = verifierInfo.publicKey.getEncoded();
                    if (!java.util.Arrays.equals(actualPublicKey, expectedPublicKey)) {
                        android.util.Slog.i("PackageManager", "Verifier package " + verifierInfo.packageName + " does not have the expected public key; ignoring");
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return -1;
                    }
                    int uid = pkg.getUid();
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return uid;
                } catch (java.security.cert.CertificateException e) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return -1;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    public void sendPendingBroadcasts() {
        int uid;
        int numBroadcasts = 0;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>>> userIdToPackagesToComponents = this.mPm.mPendingBroadcasts.copiedMap();
                int numUsers = userIdToPackagesToComponents.size();
                for (int n = 0; n < numUsers; n++) {
                    numBroadcasts += userIdToPackagesToComponents.valueAt(n).size();
                }
                if (numBroadcasts == 0) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                java.lang.String[] packages = new java.lang.String[numBroadcasts];
                java.util.ArrayList<java.lang.String>[] components = new java.util.ArrayList[numBroadcasts];
                int[] uids = new int[numBroadcasts];
                int i = 0;
                for (int n2 = 0; n2 < numUsers; n2++) {
                    int packageUserId = userIdToPackagesToComponents.keyAt(n2);
                    android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> componentsToBroadcast = userIdToPackagesToComponents.valueAt(n2);
                    int numComponents = com.android.internal.util.CollectionUtils.size(componentsToBroadcast);
                    for (int index = 0; index < numComponents; index++) {
                        packages[i] = componentsToBroadcast.keyAt(index);
                        components[i] = componentsToBroadcast.valueAt(index);
                        com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(packages[i]);
                        if (ps != null) {
                            uid = android.os.UserHandle.getUid(packageUserId, ps.getAppId());
                        } else {
                            uid = -1;
                        }
                        uids[i] = uid;
                        i++;
                    }
                }
                int numBroadcasts2 = i;
                this.mPm.mPendingBroadcasts.clear();
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
                for (int i2 = 0; i2 < numBroadcasts2; i2++) {
                    this.mBroadcastHelper.sendPackageChangedBroadcast(snapshot, packages[i2], true, components[i2], uids[i2], null);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    void handlePackagePostInstall(com.android.server.pm.InstallRequest request, boolean launchedForRestore) {
        com.android.server.pm.Computer snapshot;
        boolean z;
        boolean killApp = (request.getInstallFlags() & 4096) == 0;
        boolean succeeded = request.getReturnCode() == 1;
        boolean update = request.isUpdate();
        boolean archived = request.isArchived();
        final java.lang.String packageName = request.getName();
        com.android.server.pm.Computer snapshot2 = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal pkgSetting = succeeded ? snapshot2.getPackageStateInternal(packageName) : null;
        boolean removedBeforeUpdate = pkgSetting == null || (pkgSetting.isSystem() && !pkgSetting.getPath().getPath().equals(request.getPkg().getPath()));
        if (succeeded && removedBeforeUpdate) {
            android.util.Slog.e("PackageManager", packageName + " was removed before handlePackagePostInstall could be executed");
            request.setReturnCode(-23);
            request.setReturnMessage("Package was removed before install could complete.");
            this.mRemovePackageHelper.cleanUpResources(packageName, request.getOldCodeFile(), request.getOldInstructionSet());
            this.mPm.notifyInstallObserver(request);
            return;
        }
        if (!succeeded) {
            snapshot = snapshot2;
            z = true;
        } else {
            if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.aslInApkAppMetadataSource() && pkgSetting.getAppMetadataSource() == 1 && !com.android.server.pm.PackageManagerServiceUtils.extractAppMetadataFromApk(request.getPkg(), pkgSetting.getAppMetadataFilePath(), pkgSetting.isSystem())) {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        com.android.server.pm.PackageSetting setting = this.mPm.mSettings.getPackageLPr(packageName);
                        if (setting != null) {
                            setting.setAppMetadataFilePath(null).setAppMetadataSource(0);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
            this.mPm.mPerUidReadTimeoutsCache = null;
            this.mPm.notifyInstantAppPackageInstalled(request.getPkg().getPackageName(), request.getNewUsers());
            final int[] firstUserIds = request.getFirstTimeBroadcastUserIds();
            if (request.getPkg().getStaticSharedLibraryName() == null) {
                this.mPm.mProcessLoggingHandler.invalidateBaseApkHash(request.getPkg().getBaseApkPath());
                this.mPm.mPackageManagerServiceSocExt.setInstallationBoost(false);
                this.mPm.mPackageManagerServiceExt.afterSendPackageAddedForAllInHPPI(request);
            }
            com.android.server.pm.Computer snapshot3 = snapshot2;
            z = true;
            this.mBroadcastHelper.sendPostInstallBroadcasts(this.mPm.snapshotComputer(), request, packageName, this.mPm.mRequiredPermissionControllerPackage, this.mPm.mRequiredVerifierPackages, this.mPm.mRequiredInstallerPackage, this.mPm, launchedForRestore, killApp, update, archived);
            if (request.isAllNewUsers() && !update) {
                this.mPm.notifyPackageAdded(packageName, request.getAppId());
                this.mPm.mPackageManagerServiceExt.showAppInstallationRecommendPage(packageName, request.getInstallSource());
            } else {
                this.mPm.notifyPackageChanged(packageName, request.getAppId());
            }
            if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.enhancedConfirmationModeApisEnabled() && android.security.Flags.extendEcmToAllSettings()) {
                final int appId = request.getAppId();
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handlePackagePostInstall$3(firstUserIds, packageName, appId);
                    }
                });
            } else if (request.getPackageSource() == 3 || request.getPackageSource() == 4) {
                final int appId2 = request.getAppId();
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handlePackagePostInstall$4(firstUserIds, packageName, appId2);
                    }
                });
            }
            android.util.EventLog.writeEvent(3110, getUnknownSourcesSettings());
            com.android.server.pm.CleanUpArgs args = request.getRemovedInfo() != null ? request.getRemovedInfo().mArgs : null;
            if (args != null) {
                if (!killApp) {
                    this.mPm.scheduleDeferredNoKillPostDelete(args);
                    if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveInstallDontKill()) {
                        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                        try {
                            com.android.server.pm.PackageManagerServiceUtils.linkFilesToOldDirs(this.mPm.mInstaller, packageName, pkgSetting.getPath(), pkgSetting.getOldPaths());
                            if (installLock != null) {
                                installLock.close();
                            }
                        } finally {
                        }
                    }
                } else {
                    this.mRemovePackageHelper.cleanUpResources(packageName, args.getCodeFile(), args.getInstructionSets());
                    this.mPm.mPackageManagerServiceExt.beforeDoPostDeleteLIInHPPI(packageName);
                }
            } else {
                dalvik.system.VMRuntime.getRuntime().requestConcurrentGC();
            }
            if (!archived) {
                int length = firstUserIds.length;
                int i = 0;
                while (i < length) {
                    int userId = firstUserIds[i];
                    com.android.server.pm.Computer snapshot4 = snapshot3;
                    android.content.pm.PackageInfo info = snapshot4.getPackageInfo(packageName, 0L, userId);
                    if (info != null) {
                        this.mDexManager.notifyPackageInstalled(info, userId);
                    }
                    i++;
                    snapshot3 = snapshot4;
                }
                snapshot = snapshot3;
            } else {
                snapshot = snapshot3;
                com.android.server.pm.PackageRemovedInfo info2 = new com.android.server.pm.PackageRemovedInfo();
                info2.mRemovedPackage = packageName;
                info2.mInstallerPackageName = request.getInstallerPackageName();
                info2.mRemovedUsers = firstUserIds;
                info2.mBroadcastUsers = firstUserIds;
                info2.mUid = request.getAppId();
                info2.mRemovedPackageVersionCode = request.getPkg().getLongVersionCode();
                info2.mRemovedForAllUsers = true;
                this.mBroadcastHelper.sendPackageRemovedBroadcasts(info2, this.mPm, false, false, true);
            }
            this.mPm.mPackageManagerServiceExt.handleSuccessAtEndInHPPI(this.mPm.mContext, request.getPkg(), packageName, request.getInstallSource(), update, request.getUpdateBroadcastUserIds());
        }
        boolean deferInstallObserver = (succeeded && update) ? z : false;
        if (deferInstallObserver) {
            if (killApp) {
                this.mPm.scheduleDeferredPendingKillInstallObserver(request);
            } else {
                this.mPm.scheduleDeferredNoKillInstallObserver(request);
            }
        } else {
            this.mPm.notifyInstallObserver(request);
        }
        this.mPm.schedulePruneUnusedStaticSharedLibraries(z);
        if (request.getTraceMethod() != null) {
            android.os.Trace.asyncTraceEnd(262144L, request.getTraceMethod(), request.getTraceCookie());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePackagePostInstall$3(int[] firstUserIds, java.lang.String packageName, int appId) {
        for (int userId : firstUserIds) {
            setAccessRestrictedSettingsMode(packageName, appId, userId, 3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePackagePostInstall$4(int[] firstUserIds, java.lang.String packageName, int appId) {
        for (int userId : firstUserIds) {
            setAccessRestrictedSettingsMode(packageName, appId, userId, 2);
        }
    }

    private int getUnknownSourcesSettings() {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "install_non_market_apps", -1, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0044 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void installSystemStubPackages(java.util.List<java.lang.String> r9, int r10) {
        /*
            r8 = this;
            int r0 = r9.size()
            int r0 = r0 + (-1)
        L6:
            java.lang.String r1 = "android"
            r2 = 0
            if (r0 < 0) goto L6e
            java.lang.Object r3 = r9.get(r0)
            java.lang.String r3 = (java.lang.String) r3
            com.android.server.pm.PackageManagerService r4 = r8.mPm
            com.android.server.pm.Settings r4 = r4.mSettings
            boolean r4 = r4.isDisabledSystemPackageLPr(r3)
            if (r4 == 0) goto L1f
            r9.remove(r0)
            goto L6b
        L1f:
            com.android.server.pm.PackageManagerService r4 = r8.mPm
            com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> r4 = r4.mPackages
            java.lang.Object r4 = r4.get(r3)
            com.android.server.pm.pkg.AndroidPackage r4 = (com.android.server.pm.pkg.AndroidPackage) r4
            if (r4 != 0) goto L2f
            r9.remove(r0)
            goto L6b
        L2f:
            com.android.server.pm.PackageManagerService r5 = r8.mPm
            com.android.server.pm.Settings r5 = r5.mSettings
            com.android.server.pm.PackageSetting r5 = r5.getPackageLPr(r3)
            if (r5 == 0) goto L44
            int r6 = r5.getEnabled(r2)
            r7 = 3
            if (r6 != r7) goto L44
            r9.remove(r0)
            goto L6b
        L44:
            r8.installStubPackageLI(r4, r2, r10)     // Catch: com.android.server.pm.PackageManagerException -> L4e
            r5.setEnabled(r2, r2, r1)     // Catch: com.android.server.pm.PackageManagerException -> L4e
            r9.remove(r0)     // Catch: com.android.server.pm.PackageManagerException -> L4e
            goto L6b
        L4e:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r6 = "Failed to parse uncompressed system package: "
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r6 = r1.getMessage()
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r2 = r2.toString()
            java.lang.String r6 = "PackageManager"
            android.util.Slog.e(r6, r2)
        L6b:
            int r0 = r0 + (-1)
            goto L6
        L6e:
            int r0 = r9.size()
            int r0 = r0 + (-1)
        L74:
            if (r0 < 0) goto La2
            java.lang.Object r3 = r9.get(r0)
            java.lang.String r3 = (java.lang.String) r3
            com.android.server.pm.PackageManagerService r4 = r8.mPm
            com.android.server.pm.Settings r4 = r4.mSettings
            com.android.server.pm.PackageSetting r4 = r4.getPackageLPr(r3)
            r5 = 2
            r4.setEnabled(r5, r2, r1)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Stub disabled; pkg: "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r3)
            java.lang.String r5 = r5.toString()
            r6 = 6
            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(r6, r5)
            int r0 = r0 + (-1)
            goto L74
        La2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.installSystemStubPackages(java.util.List, int):void");
    }

    boolean enableCompressedPackage(com.android.server.pm.pkg.AndroidPackage stubPkg, com.android.server.pm.PackageSetting stubPkgSetting) {
        com.android.server.pm.PackageFreezer freezer;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock;
        int parseFlags = this.mPm.getDefParseFlags() | Integer.MIN_VALUE | 64;
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            try {
                freezer = this.mPm.freezePackage(stubPkg.getPackageName(), -1, "setEnabledSetting", 16, null);
                try {
                    com.android.server.pm.pkg.AndroidPackage pkg = installStubPackageLI(stubPkg, parseFlags, 0);
                    this.mAppDataHelper.prepareAppDataAfterInstallLIF(pkg);
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock2) {
                        try {
                            try {
                                this.mSharedLibraries.updateSharedLibraries(pkg, stubPkgSetting, null, null, java.util.Collections.unmodifiableMap(this.mPm.mPackages));
                            } finally {
                            }
                        } catch (com.android.server.pm.PackageManagerException e) {
                            android.util.Slog.w("PackageManager", "updateAllSharedLibrariesLPw failed: ", e);
                        }
                        this.mPm.mPermissionManager.onPackageInstalled(pkg, -1, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.DEFAULT, -1);
                        this.mPm.writeSettingsLPrTEMP();
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    if (freezer != null) {
                        freezer.close();
                    }
                    this.mAppDataHelper.clearAppDataLIF(pkg, -1, 39);
                    this.mDexManager.notifyPackageUpdated(pkg.getPackageName(), pkg.getBaseApkPath(), pkg.getSplitCodePaths());
                    if (installLock != null) {
                        installLock.close();
                    }
                    return true;
                } finally {
                }
            } catch (com.android.server.pm.PackageManagerException e2) {
                try {
                    try {
                        freezer = this.mPm.freezePackage(stubPkg.getPackageName(), -1, "setEnabledSetting", 16, null);
                        try {
                            packageManagerTracedLock = this.mPm.mLock;
                            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        } finally {
                        }
                    } catch (com.android.server.pm.PackageManagerException pme) {
                        android.util.Slog.wtf("PackageManager", "Failed to restore system package:" + stubPkg.getPackageName(), pme);
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mPm.mLock;
                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock3) {
                            try {
                                com.android.server.pm.PackageSetting stubPs = this.mPm.mSettings.getPackageLPr(stubPkg.getPackageName());
                                if (stubPs != null) {
                                    stubPs.setEnabled(2, 0, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                                }
                                this.mPm.writeSettingsLPrTEMP();
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            } catch (java.lang.Throwable th) {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                throw th;
                            }
                        }
                    }
                    synchronized (packageManagerTracedLock) {
                        try {
                            this.mPm.mSettings.enableSystemPackageLPw(stubPkg.getPackageName());
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            installPackageFromSystemLIF(stubPkg.getPath(), this.mPm.mUserManager.getUserIds(), null, true);
                            if (freezer != null) {
                                freezer.close();
                            }
                            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock4 = this.mPm.mLock;
                            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                            synchronized (packageManagerTracedLock4) {
                                try {
                                    com.android.server.pm.PackageSetting stubPs2 = this.mPm.mSettings.getPackageLPr(stubPkg.getPackageName());
                                    if (stubPs2 != null) {
                                        stubPs2.setEnabled(2, 0, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                                    }
                                    this.mPm.writeSettingsLPrTEMP();
                                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                    if (installLock != null) {
                                        installLock.close();
                                    }
                                    return false;
                                } catch (java.lang.Throwable th2) {
                                    throw th2;
                                }
                            }
                        } finally {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        }
                    }
                } catch (java.lang.Throwable th3) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock5 = this.mPm.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock5) {
                        try {
                            com.android.server.pm.PackageSetting stubPs3 = this.mPm.mSettings.getPackageLPr(stubPkg.getPackageName());
                            if (stubPs3 != null) {
                                stubPs3.setEnabled(2, 0, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                            }
                            this.mPm.writeSettingsLPrTEMP();
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th3;
                        } finally {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th4) {
            if (installLock != null) {
                try {
                    installLock.close();
                } catch (java.lang.Throwable th5) {
                    th4.addSuppressed(th5);
                }
            }
            throw th4;
        }
    }

    private com.android.server.pm.pkg.AndroidPackage installStubPackageLI(com.android.server.pm.pkg.AndroidPackage stubPkg, int parseFlags, int scanFlags) throws com.android.server.pm.PackageManagerException {
        if (com.android.server.pm.PackageManagerService.DEBUG_COMPRESSION) {
            android.util.Slog.i("PackageManager", "Uncompressing system stub; pkg: " + stubPkg.getPackageName());
        }
        java.io.File scanFile = decompressPackage(stubPkg.getPackageName(), stubPkg.getPath());
        if (scanFile == null) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Unable to decompress stub at " + stubPkg.getPath(), -11);
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPm.mSettings.disableSystemPackageLPw(stubPkg.getPackageName(), true);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mRemovePackageHelper.removePackage(stubPkg, true);
        try {
            return initPackageTracedLI(scanFile, parseFlags, scanFlags);
        } catch (com.android.server.pm.PackageManagerException e) {
            android.util.Slog.w("PackageManager", "Failed to install compressed system package:" + stubPkg.getPackageName(), e);
            this.mRemovePackageHelper.removeCodePath(scanFile);
            throw e;
        }
    }

    private java.io.File decompressPackage(java.lang.String packageName, java.lang.String codePath) {
        if (!com.android.server.pm.PackageManagerServiceUtils.compressedFileExists(codePath)) {
            if (com.android.server.pm.PackageManagerService.DEBUG_COMPRESSION) {
                android.util.Slog.i("PackageManager", "No files to decompress at: " + codePath);
            }
            return null;
        }
        java.io.File dstCodePath = com.android.server.pm.PackageManagerServiceUtils.getNextCodePath(android.os.Environment.getDataAppDirectory(null), packageName);
        int ret = com.android.server.pm.PackageManagerServiceUtils.decompressFiles(codePath, dstCodePath, packageName);
        if (ret == 1) {
            ret = com.android.server.pm.PackageManagerServiceUtils.extractNativeBinaries(dstCodePath, packageName);
        }
        if (ret == 1) {
            if (!this.mPm.isSystemReady()) {
                if (this.mPm.mReleaseOnSystemReady == null) {
                    this.mPm.mReleaseOnSystemReady = new java.util.ArrayList();
                }
                this.mPm.mReleaseOnSystemReady.add(dstCodePath);
            } else {
                android.content.ContentResolver resolver = this.mContext.getContentResolver();
                com.android.internal.content.F2fsUtils.releaseCompressedBlocks(resolver, dstCodePath);
            }
            return dstCodePath;
        }
        if (!dstCodePath.exists()) {
            return null;
        }
        this.mRemovePackageHelper.removeCodePath(dstCodePath);
        return null;
    }

    public void restoreDisabledSystemPackageLIF(com.android.server.pm.DeletePackageAction action, int[] allUserHandles, boolean writeSettings) throws com.android.server.pm.SystemDeleteException {
        int[] origUsers;
        com.android.server.pm.PackageSetting deletedPs = action.mDeletingPs;
        com.android.server.pm.PackageRemovedInfo outInfo = action.mRemovedInfo;
        com.android.server.pm.PackageSetting disabledPs = action.mDisabledPs;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPm.mSettings.enableSystemPackageLPw(disabledPs.getPkg().getPackageName());
                com.android.server.pm.PackageManagerServiceUtils.removeNativeBinariesLI(deletedPs);
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
            android.util.Slog.d("PackageManager", "Re-installing system package: " + disabledPs);
        }
        try {
            try {
                com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                if (outInfo == null) {
                    origUsers = null;
                } else {
                    try {
                        origUsers = outInfo.mOrigUsers;
                    } catch (java.lang.Throwable th) {
                        if (installLock != null) {
                            try {
                                installLock.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                }
                installPackageFromSystemLIF(disabledPs.getPathString(), allUserHandles, origUsers, writeSettings);
                if (installLock != null) {
                    installLock.close();
                }
                if (disabledPs.getPkg().isStub()) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock2) {
                        try {
                            disableStubPackage(action, deletedPs, allUserHandles);
                        } finally {
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
                this.mPm.mPackageManagerServiceExt.notifyPackageAddOrUpdateForAbiInfo(disabledPs.getPkg().getPackageName(), disabledPs);
            } catch (java.lang.Throwable th3) {
                if (disabledPs.getPkg().isStub()) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mPm.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock3) {
                        try {
                            disableStubPackage(action, deletedPs, allUserHandles);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } finally {
                        }
                    }
                }
                this.mPm.mPackageManagerServiceExt.notifyPackageAddOrUpdateForAbiInfo(disabledPs.getPkg().getPackageName(), disabledPs);
                throw th3;
            }
        } catch (com.android.server.pm.PackageManagerException e) {
            android.util.Slog.w("PackageManager", "Failed to restore system package:" + deletedPs.getPackageName() + ": " + e.getMessage());
            throw new com.android.server.pm.SystemDeleteException(e);
        }
    }

    private void disableStubPackage(com.android.server.pm.DeletePackageAction action, com.android.server.pm.PackageSetting deletedPs, int[] allUserHandles) {
        com.android.server.pm.PackageSetting stubPs = this.mPm.mSettings.getPackageLPr(deletedPs.getPackageName());
        if (stubPs != null) {
            int userId = action.mUser == null ? -1 : action.mUser.getIdentifier();
            if (userId == -1) {
                for (int aUserId : allUserHandles) {
                    stubPs.setEnabled(2, aUserId, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                }
                return;
            }
            if (userId >= 0) {
                stubPs.setEnabled(2, userId, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            }
        }
    }

    private void installPackageFromSystemLIF(java.lang.String codePathString, int[] allUserHandles, int[] origUserHandles, boolean writeSettings) throws java.lang.Throwable {
        java.io.File codePath = new java.io.File(codePathString);
        int parseFlags = this.mPm.getDefParseFlags() | 1 | 16;
        int scanFlags = this.mPm.getSystemPackageScanFlags(codePath);
        com.android.server.pm.pkg.AndroidPackage pkg = initPackageTracedLI(codePath, parseFlags, scanFlags);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.PackageSetting pkgSetting = this.mPm.mSettings.getPackageLPr(pkg.getPackageName());
                try {
                    this.mSharedLibraries.updateSharedLibraries(pkg, pkgSetting, null, null, java.util.Collections.unmodifiableMap(this.mPm.mPackages));
                } catch (com.android.server.pm.PackageManagerException e) {
                    android.util.Slog.e("PackageManager", "updateAllSharedLibrariesLPw failed: " + e.getMessage());
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        setPackageInstalledForSystemPackage(pkg, allUserHandles, origUserHandles, writeSettings);
        this.mAppDataHelper.prepareAppDataAfterInstallLIF(pkg);
    }

    private void setPackageInstalledForSystemPackage(com.android.server.pm.pkg.AndroidPackage pkg, int[] allUserHandles, int[] origUserHandles, boolean writeSettings) throws java.lang.Throwable {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                try {
                    com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(pkg.getPackageName());
                    boolean applyUserRestrictions = origUserHandles != null;
                    if (applyUserRestrictions) {
                        boolean installedStateChanged = false;
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                            android.util.Slog.d("PackageManager", "Propagating install state across reinstall");
                        }
                        for (int userId : allUserHandles) {
                            boolean installed = com.android.internal.util.ArrayUtils.contains(origUserHandles, userId);
                            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                                android.util.Slog.d("PackageManager", "    user " + userId + " => " + installed);
                            }
                            if (installed != ps.getInstalled(userId)) {
                                installedStateChanged = true;
                            }
                            ps.setInstalled(installed, userId);
                            if (installed) {
                                ps.setUninstallReason(0, userId);
                            }
                        }
                        this.mPm.mSettings.writeAllUsersPackageRestrictionsLPr();
                        if (installedStateChanged) {
                            this.mPm.mSettings.writeKernelMappingLPr(ps);
                        }
                    }
                    this.mPm.mPermissionManager.onPackageInstalled(pkg, -1, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.DEFAULT, -1);
                    for (int userId2 : allUserHandles) {
                        if (applyUserRestrictions) {
                            this.mPm.mSettings.writePermissionStateForUserLPr(userId2, false);
                        }
                    }
                    if (writeSettings) {
                        this.mPm.writeSettingsLPrTEMP();
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    public void prepareSystemPackageCleanUp(com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> packageSettings, java.util.List<java.lang.String> possiblyDeletedUpdatedSystemApps, android.util.ArrayMap<java.lang.String, java.io.File> expectingBetter, int[] userIds) {
        for (int index = packageSettings.size() - 1; index >= 0; index--) {
            com.android.server.pm.PackageSetting ps = packageSettings.valueAt(index);
            java.lang.String packageName = ps.getPackageName();
            if (ps.isSystem()) {
                com.android.server.pm.pkg.AndroidPackage scannedPkg = this.mPm.mPackages.get(packageName);
                com.android.server.pm.PackageSetting disabledPs = this.mPm.mSettings.getDisabledSystemPkgLPr(packageName);
                if (scannedPkg != null) {
                    if (!scannedPkg.isApex() && disabledPs != null) {
                        com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Expecting better updated system app for " + packageName + "; removing system app.  Last known codePath=" + ps.getPathString() + ", versionCode=" + ps.getVersionCode() + "; scanned versionCode=" + scannedPkg.getLongVersionCode());
                        this.mRemovePackageHelper.removePackage(scannedPkg, true);
                        expectingBetter.put(ps.getPackageName(), ps.getPath());
                    }
                } else if (disabledPs == null) {
                    com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "System package " + packageName + " no longer exists; its data will be wiped");
                    this.mPm.mPackageManagerServiceExt.onSystemAppNotExistCheckedInConstructor(ps);
                    this.mRemovePackageHelper.removePackageData(ps, userIds);
                } else if (disabledPs.getPath() == null || !disabledPs.getPath().exists() || disabledPs.getPkg() == null) {
                    possiblyDeletedUpdatedSystemApps.add(packageName);
                } else {
                    expectingBetter.put(disabledPs.getPackageName(), disabledPs.getPath());
                }
            }
        }
    }

    public void cleanupDisabledPackageSettings(java.util.List<java.lang.String> possiblyDeletedUpdatedSystemApps, int[] userIds, int scanFlags) {
        java.lang.String msg;
        for (int i = possiblyDeletedUpdatedSystemApps.size() - 1; i >= 0; i--) {
            java.lang.String packageName = possiblyDeletedUpdatedSystemApps.get(i);
            com.android.server.pm.pkg.AndroidPackage pkg = this.mPm.mPackages.get(packageName);
            this.mPm.mSettings.removeDisabledSystemPackageLPw(packageName);
            if (pkg == null) {
                msg = "Updated system package " + packageName + " no longer exists; removing its data";
            } else {
                msg = "Updated system package " + packageName + " no longer exists; rescanning package on data";
                this.mRemovePackageHelper.removePackage(pkg, true);
                com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(packageName);
                if (ps != null) {
                    ps.getPkgState().setUpdatedSystemApp(false);
                }
                if (!this.mPm.mPackageManagerServiceExt.shouldRemoveUpdatedMainlineApk(packageName)) {
                    java.io.File codePath = new java.io.File(pkg.getPath());
                    try {
                        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                        try {
                            initPackageTracedLI(codePath, 0, scanFlags);
                            if (installLock != null) {
                                installLock.close();
                            }
                        } catch (java.lang.Throwable th) {
                            if (installLock != null) {
                                try {
                                    installLock.close();
                                } catch (java.lang.Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    } catch (com.android.server.pm.PackageManagerException e) {
                        android.util.Slog.e("PackageManager", "Failed to parse updated, ex-system package: " + e.getMessage());
                    }
                }
            }
            com.android.server.pm.PackageSetting ps2 = this.mPm.mSettings.getPackageLPr(packageName);
            if (ps2 != null && this.mPm.mPackages.get(packageName) == null) {
                this.mRemovePackageHelper.removePackageData(ps2, userIds);
            }
            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, msg);
        }
    }

    public java.util.List<com.android.server.pm.ApexManager.ScanResult> scanApexPackages(android.apex.ApexInfo[] allPackages, int parseFlags, int scanFlags, com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService) throws java.lang.Throwable {
        int newParseFlags;
        int newScanFlags;
        android.apex.ApexInfo ai;
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal androidPackageInternalHideAsFinal;
        if (allPackages == null) {
            return java.util.Collections.EMPTY_LIST;
        }
        com.android.server.pm.ParallelPackageParser parallelPackageParser = new com.android.server.pm.ParallelPackageParser(packageParser, executorService);
        final android.util.ArrayMap<java.io.File, android.apex.ApexInfo> parsingApexInfo = new android.util.ArrayMap<>();
        for (android.apex.ApexInfo ai2 : allPackages) {
            java.io.File apexFile = new java.io.File(ai2.modulePath);
            parallelPackageParser.submit(apexFile, parseFlags);
            parsingApexInfo.put(apexFile, ai2);
        }
        java.util.List<com.android.server.pm.ParallelPackageParser.ParseResult> parseResults = new java.util.ArrayList<>(parsingApexInfo.size());
        for (int i = 0; i < parsingApexInfo.size(); i++) {
            parseResults.add(parallelPackageParser.take());
        }
        java.util.Collections.sort(parseResults, new java.util.Comparator() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda3
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.InstallPackageHelper.lambda$scanApexPackages$5(parsingApexInfo, (com.android.server.pm.ParallelPackageParser.ParseResult) obj, (com.android.server.pm.ParallelPackageParser.ParseResult) obj2);
            }
        });
        java.util.List<com.android.server.pm.ApexManager.ScanResult> results = new java.util.ArrayList<>(parsingApexInfo.size());
        int i2 = 0;
        while (i2 < parseResults.size()) {
            com.android.server.pm.ParallelPackageParser.ParseResult parseResult = parseResults.get(i2);
            java.lang.Throwable throwable = parseResult.throwable;
            android.apex.ApexInfo ai3 = parsingApexInfo.get(parseResult.scanFile);
            int i3 = i2;
            int newScanFlags2 = scanFlags | 67108864 | this.mPm.getSystemPackageScanFlags(parseResult.scanFile);
            if (ai3.isFactory) {
                newParseFlags = parseFlags;
                newScanFlags = newScanFlags2;
            } else {
                int newParseFlags2 = parseFlags & (-17);
                newParseFlags = newParseFlags2;
                newScanFlags = newScanFlags2 | 4;
            }
            if (throwable != null) {
                if (throwable instanceof com.android.server.pm.PackageManagerException) {
                    throw new java.lang.IllegalStateException("Unable to parse: " + ai3.modulePath, throwable);
                }
                throw new java.lang.IllegalStateException("Unexpected exception occurred while parsing " + ai3.modulePath, throwable);
            }
            try {
                try {
                    addForInitLI(parseResult.parsedPackage, newParseFlags, newScanFlags, null, new com.android.server.pm.ApexManager.ActiveApexInfo(ai3));
                    androidPackageInternalHideAsFinal = parseResult.parsedPackage.hideAsFinal();
                    ai = ai3;
                } catch (com.android.server.pm.PackageManagerException e) {
                    e = e;
                    ai = ai3;
                }
            } catch (com.android.server.pm.PackageManagerException e2) {
                e = e2;
                ai = ai3;
            }
            try {
                if (ai.isFactory && !ai.isActive) {
                    disableSystemPackageLPw(androidPackageInternalHideAsFinal);
                }
                results.add(new com.android.server.pm.ApexManager.ScanResult(ai, androidPackageInternalHideAsFinal, androidPackageInternalHideAsFinal.getPackageName()));
                i2 = i3 + 1;
            } catch (com.android.server.pm.PackageManagerException e3) {
                e = e3;
                this.mPm.mPackageManagerServiceExt.hookScanApexPackages(ai);
                throw new java.lang.IllegalStateException("Failed to scan: " + ai.modulePath, e);
            }
        }
        return results;
    }

    static /* synthetic */ int lambda$scanApexPackages$5(android.util.ArrayMap parsingApexInfo, com.android.server.pm.ParallelPackageParser.ParseResult a, com.android.server.pm.ParallelPackageParser.ParseResult b) {
        android.apex.ApexInfo i1 = (android.apex.ApexInfo) parsingApexInfo.get(a.scanFile);
        android.apex.ApexInfo i2 = (android.apex.ApexInfo) parsingApexInfo.get(b.scanFile);
        return java.lang.Boolean.compare(i2.isFactory, i1.isFactory);
    }

    public void installPackagesFromDir(java.io.File scanDir, int parseFlags, int scanFlags, com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService, com.android.server.pm.ApexManager.ActiveApexInfo apexInfo) throws java.lang.Throwable {
        com.android.server.pm.ParallelPackageParser.ParseResult parseResult;
        com.android.server.pm.ParallelPackageParser parallelPackageParser;
        java.lang.String str;
        java.io.File[] files;
        int errorCode;
        java.lang.String errorMsg;
        int i;
        long j;
        java.lang.String str2;
        java.lang.Throwable throwable;
        com.android.server.pm.ParallelPackageParser.ParseResult parseResult2;
        int i2;
        java.io.File file = scanDir;
        int i3 = parseFlags;
        int i4 = scanFlags;
        java.io.File[] files2 = scanDir.listFiles();
        java.lang.String str3 = "PackageManager";
        if (com.android.internal.util.ArrayUtils.isEmpty(files2)) {
            android.util.Log.d("PackageManager", "No files in app dir " + file);
            return;
        }
        this.mPm.mPackageManagerServiceExt.beforeScanInScanDirLI();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:PMS_scan_data:" + scanDir.getPath().toString());
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
            android.util.Log.d("PackageManager", "Scanning app dir " + file + " scanFlags=" + i4 + " flags=0x" + java.lang.Integer.toHexString(parseFlags));
        }
        com.android.server.pm.ParallelPackageParser parallelPackageParser2 = new com.android.server.pm.ParallelPackageParser(packageParser, executorService);
        int fileCount = 0;
        int length = files2.length;
        int i5 = 0;
        while (i5 < length) {
            java.io.File file2 = files2[i5];
            boolean isPackage = (android.content.pm.parsing.ApkLiteParseUtils.isApkFile(file2) || file2.isDirectory()) && !com.android.server.pm.PackageInstallerService.isStageName(file2.getName());
            if (isPackage) {
                if ((16777216 & i4) != 0) {
                    i2 = length;
                    com.android.server.pm.parsing.PackageCacher cacher = new com.android.server.pm.parsing.PackageCacher(this.mPm.getCacheDir(), this.mPm.mPackageParserCallback);
                    android.util.Log.w("PackageManager", "Dropping cache of " + file2.getAbsolutePath());
                    cacher.cleanCachedResult(file2);
                } else {
                    i2 = length;
                }
                parallelPackageParser2.submit(file2, i3);
                fileCount++;
            } else {
                i2 = length;
            }
            i5++;
            length = i2;
        }
        int fileCount2 = fileCount;
        while (fileCount2 > 0) {
            com.android.server.pm.ParallelPackageParser.ParseResult parseResult3 = parallelPackageParser2.take();
            java.lang.Throwable throwable2 = parseResult3.throwable;
            int errorCode2 = 1;
            java.lang.String errorMsg2 = null;
            if (throwable2 == null) {
                if (this.mPm.mPackageManagerServiceExt.interceptUseParseResultWithoutThrowInScanDirLI(parseResult3, i4)) {
                    parallelPackageParser = parallelPackageParser2;
                    str = str3;
                    files = files2;
                } else if (this.mPm.mPackageManagerServiceExt.interceptUseParseResultWithoutThrowInScanDirLI2(parseResult3, i4, i3, file)) {
                    parallelPackageParser = parallelPackageParser2;
                    str = str3;
                    files = files2;
                } else {
                    try {
                        android.os.Trace.traceBegin(262144L, "addForInitLI");
                        try {
                            j = 262144;
                            str2 = ": ";
                            throwable = throwable2;
                            parseResult2 = parseResult3;
                            try {
                                addForInitLI(parseResult3.parsedPackage, parseFlags, scanFlags, new android.os.UserHandle(0), apexInfo);
                                android.os.Trace.traceEnd(262144L);
                                parallelPackageParser = parallelPackageParser2;
                                str = str3;
                                files = files2;
                                parseResult = parseResult2;
                            } catch (com.android.server.pm.PackageManagerException e) {
                                e = e;
                                try {
                                    errorCode2 = e.error;
                                    java.lang.String errorMsg3 = "Failed to scan " + parseResult2.scanFile + str2 + e.getMessage();
                                    try {
                                        android.util.Slog.w(str3, errorMsg3);
                                        parallelPackageParser = parallelPackageParser2;
                                        str = str3;
                                        files = files2;
                                        parseResult = parseResult2;
                                        try {
                                            this.mPm.mPackageManagerServiceExt.handleExpOfAddForInitInScanDirLI(this.mPm, parseResult2, parseResult2.scanFile, scanFlags, parseFlags, apexInfo);
                                            android.os.Trace.traceEnd(j);
                                            errorMsg2 = errorMsg3;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            android.os.Trace.traceEnd(j);
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                android.os.Trace.traceEnd(j);
                                throw th;
                            }
                        } catch (com.android.server.pm.PackageManagerException e2) {
                            e = e2;
                            j = 262144;
                            str2 = ": ";
                            throwable = throwable2;
                            parseResult2 = parseResult3;
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                            j = 262144;
                        }
                    } catch (com.android.server.pm.PackageManagerException e3) {
                        e = e3;
                        j = 262144;
                        str2 = ": ";
                        throwable = throwable2;
                        parseResult2 = parseResult3;
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                        j = 262144;
                    }
                    errorCode = errorCode2;
                    errorMsg = errorMsg2;
                }
                fileCount2--;
                file = scanDir;
                i3 = parseFlags;
                i4 = scanFlags;
                str3 = str;
                files2 = files;
                parallelPackageParser2 = parallelPackageParser;
            } else {
                parseResult = parseResult3;
                parallelPackageParser = parallelPackageParser2;
                str = str3;
                files = files2;
                if (!(throwable2 instanceof com.android.server.pm.PackageManagerException)) {
                    throw new java.lang.IllegalStateException("Unexpected exception occurred while parsing " + parseResult.scanFile, throwable2);
                }
                com.android.server.pm.PackageManagerException e4 = (com.android.server.pm.PackageManagerException) throwable2;
                errorCode = e4.error;
                errorMsg = "Failed to parse " + parseResult.scanFile + ": " + e4.getMessage();
                android.util.Slog.w(str, errorMsg);
            }
            if ((scanFlags & 8388608) != 0) {
                i = 1;
                if (errorCode != 1) {
                    this.mApexManager.reportErrorWithApkInApex(scanDir.getAbsolutePath(), errorMsg);
                }
            } else {
                i = 1;
            }
            if ((scanFlags & 65536) == 0 && errorCode != i) {
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Deleting invalid package at " + parseResult.scanFile);
                if (!this.mPm.mPackageManagerServiceExt.skipDeleteDataAppWhenFailedInScanDirLI(this.mPm)) {
                    this.mRemovePackageHelper.removeCodePath(parseResult.scanFile);
                }
            }
            fileCount2--;
            file = scanDir;
            i3 = parseFlags;
            i4 = scanFlags;
            str3 = str;
            files2 = files;
            parallelPackageParser2 = parallelPackageParser;
        }
    }

    public void checkExistingBetterPackages(android.util.ArrayMap<java.lang.String, java.io.File> expectingBetterPackages, java.util.List<java.lang.String> stubSystemApps, int systemScanFlags, int systemParseFlags) {
        for (int i = 0; i < expectingBetterPackages.size(); i++) {
            java.lang.String packageName = expectingBetterPackages.keyAt(i);
            if (!this.mPm.mPackages.containsKey(packageName)) {
                java.io.File scanFile = expectingBetterPackages.valueAt(i);
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Expected better " + packageName + " but never showed up; reverting to system");
                android.util.Pair<java.lang.Integer, java.lang.Integer> rescanAndReparseFlags = this.mPm.getSystemPackageRescanFlagsAndReparseFlags(scanFile, systemScanFlags, systemParseFlags);
                int rescanFlags = ((java.lang.Integer) rescanAndReparseFlags.first).intValue();
                int reparseFlags = ((java.lang.Integer) rescanAndReparseFlags.second).intValue();
                if (rescanFlags == 0) {
                    android.util.Slog.e("PackageManager", "Ignoring unexpected fallback path " + scanFile);
                } else {
                    this.mPm.mSettings.enableSystemPackageLPw(packageName);
                    try {
                        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                        try {
                            com.android.server.pm.pkg.AndroidPackage newPkg = initPackageTracedLI(scanFile, reparseFlags, rescanFlags);
                            if (newPkg.isStub()) {
                                stubSystemApps.add(packageName);
                            }
                            if (installLock != null) {
                                installLock.close();
                            }
                        } catch (java.lang.Throwable th) {
                            if (installLock != null) {
                                try {
                                    installLock.close();
                                } catch (java.lang.Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    } catch (com.android.server.pm.PackageManagerException e) {
                        android.util.Slog.e("PackageManager", "Failed to parse original system package: " + e.getMessage());
                    }
                }
            }
        }
    }

    public com.android.server.pm.pkg.AndroidPackage initPackageTracedLI(java.io.File scanFile, int parseFlags, int scanFlags) throws com.android.server.pm.PackageManagerException {
        android.os.Trace.traceBegin(262144L, "scanPackage [" + scanFile.toString() + "]");
        try {
            return initPackageLI(scanFile, parseFlags, scanFlags);
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    private com.android.server.pm.pkg.AndroidPackage initPackageLI(java.io.File scanFile, int parseFlags, int scanFlags) throws com.android.server.pm.PackageManagerException {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "Parsing: " + scanFile);
        }
        android.os.Trace.traceBegin(262144L, "parsePackage");
        try {
            try {
                com.android.internal.pm.parsing.PackageParser2 pp = this.mPm.mInjector.getScanningPackageParser();
                try {
                    com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage = pp.parsePackage(scanFile, parseFlags, false);
                    if (pp != null) {
                        pp.close();
                    }
                    android.os.Trace.traceEnd(262144L);
                    return addForInitLI(parsedPackage, parseFlags, scanFlags, new android.os.UserHandle(0), null);
                } catch (java.lang.Throwable th) {
                    if (pp != null) {
                        try {
                            pp.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                android.os.Trace.traceEnd(262144L);
                throw th3;
            }
        } catch (com.android.internal.pm.parsing.PackageParserException e) {
            throw new com.android.server.pm.PackageManagerException(e.error, e.getMessage(), (java.lang.Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:99:0x01d9
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public com.android.server.pm.pkg.AndroidPackage addForInitLI(com.android.internal.pm.parsing.pkg.ParsedPackage r25, int r26, int r27, android.os.UserHandle r28, com.android.server.pm.ApexManager.ActiveApexInfo r29) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 485
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.addForInitLI(com.android.internal.pm.parsing.pkg.ParsedPackage, int, int, android.os.UserHandle, com.android.server.pm.ApexManager$ActiveApexInfo):com.android.server.pm.pkg.AndroidPackage");
    }

    private boolean optimisticallyRegisterAppId(com.android.server.pm.InstallRequest installRequest) throws com.android.server.pm.PackageManagerException {
        boolean zRegisterAppIdLPw;
        if (!installRequest.isExistingSettingCopied() || installRequest.needsNewAppId()) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    zRegisterAppIdLPw = this.mPm.mSettings.registerAppIdLPw(installRequest.getScannedPackageSetting(), installRequest.needsNewAppId());
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return zRegisterAppIdLPw;
        }
        return false;
    }

    private void cleanUpAppIdCreation(com.android.server.pm.InstallRequest installRequest) {
        if (installRequest.getScannedPackageSetting() != null && installRequest.getScannedPackageSetting().getAppId() > 0) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    this.mPm.mSettings.removeAppIdLPw(installRequest.getScannedPackageSetting().getAppId());
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
    }

    private com.android.server.pm.ScanResult scanPackageTracedLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int parseFlags, int scanFlags, long currentTime, android.os.UserHandle user, java.lang.String cpuAbiOverride) throws com.android.server.pm.PackageManagerException {
        android.os.Trace.traceBegin(262144L, "scanPackage");
        try {
            return scanPackageNewLI(parsedPackage, parseFlags, scanFlags, currentTime, user, cpuAbiOverride);
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:46:0x0135
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    private com.android.server.pm.ScanRequest prepareInitialScanRequest(com.android.internal.pm.parsing.pkg.ParsedPackage r22, int r23, int r24, android.os.UserHandle r25, java.lang.String r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.prepareInitialScanRequest(com.android.internal.pm.parsing.pkg.ParsedPackage, int, int, android.os.UserHandle, java.lang.String):com.android.server.pm.ScanRequest");
    }

    private com.android.server.pm.ScanResult scanPackageNewLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int parseFlags, int scanFlags, long currentTime, android.os.UserHandle user, java.lang.String cpuAbiOverride) throws java.lang.Throwable {
        boolean isUpdatedSystemApp;
        com.android.server.pm.ScanRequest initialScanRequest = prepareInitialScanRequest(parsedPackage, parseFlags, scanFlags, user, cpuAbiOverride);
        com.android.server.pm.PackageSetting installedPkgSetting = initialScanRequest.mPkgSetting;
        com.android.server.pm.PackageSetting disabledPkgSetting = initialScanRequest.mDisabledPkgSetting;
        if (installedPkgSetting != null) {
            isUpdatedSystemApp = installedPkgSetting.isUpdatedSystemApp();
        } else {
            isUpdatedSystemApp = disabledPkgSetting != null;
        }
        int newScanFlags = adjustScanFlags(scanFlags, installedPkgSetting, disabledPkgSetting, user, parsedPackage);
        com.android.server.pm.ScanPackageUtils.applyPolicy(parsedPackage, newScanFlags, this.mPm.getPlatformPackage(), isUpdatedSystemApp);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                try {
                    assertPackageIsValid(parsedPackage, parseFlags, newScanFlags);
                    try {
                        com.android.server.pm.ScanRequest request = new com.android.server.pm.ScanRequest(parsedPackage, initialScanRequest.mOldSharedUserSetting, initialScanRequest.mOldPkg, installedPkgSetting, initialScanRequest.mSharedUserSetting, disabledPkgSetting, initialScanRequest.mOriginalPkgSetting, initialScanRequest.mRealPkgName, parseFlags, scanFlags, initialScanRequest.mIsPlatformPackage, user, cpuAbiOverride);
                        com.android.server.pm.ScanResult scanResultScanPackageOnlyLI = com.android.server.pm.ScanPackageUtils.scanPackageOnlyLI(request, this.mPm.mInjector, this.mPm.mFactoryTest, currentTime);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return scanResultScanPackageOnlyLI;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:170:0x04b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.util.Pair<com.android.server.pm.ScanResult, java.lang.Boolean> scanSystemPackageLI(com.android.internal.pm.parsing.pkg.ParsedPackage r37, int r38, int r39, android.os.UserHandle r40) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1269
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.InstallPackageHelper.scanSystemPackageLI(com.android.internal.pm.parsing.pkg.ParsedPackage, int, int, android.os.UserHandle):android.util.Pair");
    }

    private static boolean hasLauncherEntry(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        java.util.HashSet<java.lang.String> categories = new java.util.HashSet<>();
        categories.add("android.intent.category.LAUNCHER");
        java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities = parsedPackage.getActivities();
        for (int indexActivity = 0; indexActivity < activities.size(); indexActivity++) {
            com.android.internal.pm.pkg.component.ParsedActivity activity = activities.get(indexActivity);
            if (activity.isEnabled() && activity.isExported()) {
                java.util.List<com.android.internal.pm.pkg.component.ParsedIntentInfo> intents = activity.getIntents();
                for (int indexIntent = 0; indexIntent < intents.size(); indexIntent++) {
                    android.content.IntentFilter intentFilter = intents.get(indexIntent).getIntentFilter();
                    if (intentFilter != null && intentFilter.matchCategories(categories) == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean needSignatureMatchToSystem(java.lang.String packageName) {
        if (!android.security.Flags.extendVbChainToUpdatedApk()) {
            return false;
        }
        return this.mPm.mInjector.getSystemConfig().getPreinstallPackagesWithStrictSignatureCheck().contains(packageName);
    }

    private void maybeClearProfilesForUpgradesLI(com.android.server.pm.PackageSetting originalPkgSetting, com.android.server.pm.pkg.AndroidPackage pkg) {
        if (originalPkgSetting == null || !this.mPm.isDeviceUpgrading() || originalPkgSetting.getVersionCode() == pkg.getLongVersionCode()) {
            return;
        }
        this.mAppDataHelper.clearAppProfilesLIF(pkg);
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", originalPkgSetting.getPackageName() + " clear profile due to version change " + originalPkgSetting.getVersionCode() + " != " + pkg.getLongVersionCode());
        }
    }

    private com.android.server.pm.PackageSetting getOriginalPackageLocked(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String renamedPkgName) {
        if (com.android.server.pm.ScanPackageUtils.isPackageRenamed(pkg, renamedPkgName)) {
            return null;
        }
        for (int i = com.android.internal.util.ArrayUtils.size(pkg.getOriginalPackages()) - 1; i >= 0; i--) {
            com.android.server.pm.PackageSetting originalPs = this.mPm.mSettings.getPackageLPr((java.lang.String) pkg.getOriginalPackages().get(i));
            if (originalPs != null && verifyPackageUpdateLPr(originalPs, pkg)) {
                if (this.mPm.mSettings.getSharedUserSettingLPr(originalPs) != null) {
                    java.lang.String sharedUserSettingsName = this.mPm.mSettings.getSharedUserSettingLPr(originalPs).name;
                    if (!sharedUserSettingsName.equals(pkg.getSharedUserId())) {
                        android.util.Slog.w("PackageManager", "Unable to migrate data from " + originalPs.getPackageName() + " to " + pkg.getPackageName() + ": old shared user settings name " + sharedUserSettingsName + " differs from " + pkg.getSharedUserId());
                    }
                } else if (com.android.server.pm.PackageManagerService.DEBUG_UPGRADE) {
                    android.util.Log.v("PackageManager", "Renaming new package " + pkg.getPackageName() + " to old name " + originalPs.getPackageName());
                }
                return originalPs;
            }
        }
        return null;
    }

    private boolean verifyPackageUpdateLPr(com.android.server.pm.PackageSetting oldPkg, com.android.server.pm.pkg.AndroidPackage newPkg) {
        if ((oldPkg.getFlags() & 1) == 0) {
            android.util.Slog.w("PackageManager", "Unable to update from " + oldPkg.getPackageName() + " to " + newPkg.getPackageName() + ": old package not in system partition");
            return false;
        }
        if (this.mPm.mPackages.get(oldPkg.getPackageName()) == null) {
            return true;
        }
        android.util.Slog.w("PackageManager", "Unable to update from " + oldPkg.getPackageName() + " to " + newPkg.getPackageName() + ": old package still exists");
        return false;
    }

    private void assertPackageIsValid(com.android.server.pm.pkg.AndroidPackage pkg, int parseFlags, int scanFlags) throws com.android.server.pm.PackageManagerException {
        if ((parseFlags & 64) != 0) {
            com.android.server.pm.ScanPackageUtils.assertCodePolicy(pkg);
        }
        if (pkg.getPath() == null) {
            throw new com.android.server.pm.PackageManagerException(-2, "Code and resource paths haven't been set correctly");
        }
        boolean isUserInstall = (scanFlags & 16) == 0;
        boolean isFirstBootOrUpgrade = (scanFlags & 4096) != 0;
        boolean installApex = (67108864 & scanFlags) != 0;
        if ((isUserInstall || isFirstBootOrUpgrade) && this.mPm.snapshotComputer().isApexPackage(pkg.getPackageName()) && !installApex) {
            throw new com.android.server.pm.PackageManagerException(-5, pkg.getPackageName() + " is an APEX package and can't be installed as an APK.");
        }
        com.android.server.pm.KeySetManagerService ksms = this.mPm.mSettings.getKeySetManagerService();
        ksms.assertScannedPackageValid(pkg);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                if (pkg.getPackageName().equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) && this.mPm.getCoreAndroidApplication() != null) {
                    android.util.Slog.w("PackageManager", "*************************************************");
                    android.util.Slog.w("PackageManager", "Core android package being redefined.  Skipping.");
                    android.util.Slog.w("PackageManager", " codePath=" + pkg.getPath());
                    android.util.Slog.w("PackageManager", "*************************************************");
                    throw new com.android.server.pm.PackageManagerException(-5, "Core android package being redefined.  Skipping.");
                }
                if ((scanFlags & 4) == 0 && this.mPm.mPackages.containsKey(pkg.getPackageName())) {
                    throw new com.android.server.pm.PackageManagerException(-5, "Application package " + pkg.getPackageName() + " already installed.  Skipping duplicate.");
                }
                if (pkg.isStaticSharedLibrary()) {
                    if ((scanFlags & 4) == 0 && this.mPm.mPackages.containsKey(pkg.getManifestPackageName())) {
                        throw com.android.server.pm.PackageManagerException.ofInternalError("Duplicate static shared lib provider package", -13);
                    }
                    com.android.server.pm.ScanPackageUtils.assertStaticSharedLibraryIsValid(pkg, scanFlags);
                    assertStaticSharedLibraryVersionCodeIsValid(pkg);
                }
                if ((scanFlags & 128) != 0) {
                    if (this.mPm.isExpectingBetter(pkg.getPackageName())) {
                        android.util.Slog.w("PackageManager", "Relax SCAN_REQUIRE_KNOWN requirement for package " + pkg.getPackageName());
                    } else {
                        com.android.server.pm.PackageSetting known = this.mPm.mSettings.getPackageLPr(pkg.getPackageName());
                        if (known != null) {
                            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                                android.util.Log.d("PackageManager", "Examining " + pkg.getPath() + " and requiring known path " + known.getPathString());
                            }
                            if (!pkg.getPath().equals(known.getPathString()) && !this.mPm.mPackageManagerServiceExt.hookOplusOtaPs(known)) {
                                throw new com.android.server.pm.PackageManagerException(-23, "Application package " + pkg.getPackageName() + " found at " + pkg.getPath() + " but expected at " + known.getPathString() + "; ignoring.");
                            }
                        } else if (!this.mPm.mPackageManagerServiceExt.allowUnknownWhenScanRequireKnownInAssertPackageIsValid(pkg)) {
                            throw new com.android.server.pm.PackageManagerException(-19, "Application package " + pkg.getPackageName() + " not found; ignoring.");
                        }
                    }
                }
                if ((scanFlags & 4) != 0) {
                    this.mPm.mComponentResolver.assertProvidersNotDefined(pkg);
                }
                com.android.server.pm.ScanPackageUtils.assertProcessesAreValid(pkg);
                assertPackageWithSharedUserIdIsPrivileged(pkg);
                if (pkg.getOverlayTarget() != null) {
                    assertOverlayIsValid(pkg, parseFlags, scanFlags);
                }
                com.android.server.pm.ScanPackageUtils.assertMinSignatureSchemeIsValid(pkg, parseFlags);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void assertStaticSharedLibraryVersionCodeIsValid(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        long minVersionCode = Long.MIN_VALUE;
        long maxVersionCode = Long.MAX_VALUE;
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.getSharedLibraryInfos(pkg.getStaticSharedLibraryName());
        if (versionedLib != null) {
            int versionCount = versionedLib.size();
            int i = 0;
            while (true) {
                if (i >= versionCount) {
                    break;
                }
                android.content.pm.SharedLibraryInfo libInfo = versionedLib.valueAt(i);
                long libVersionCode = libInfo.getDeclaringPackage().getLongVersionCode();
                if (libInfo.getLongVersion() >= pkg.getStaticSharedLibraryVersion()) {
                    if (libInfo.getLongVersion() > pkg.getStaticSharedLibraryVersion()) {
                        maxVersionCode = java.lang.Math.min(maxVersionCode, libVersionCode - 1);
                    } else {
                        maxVersionCode = libVersionCode;
                        minVersionCode = libVersionCode;
                        break;
                    }
                } else {
                    minVersionCode = java.lang.Math.max(minVersionCode, 1 + libVersionCode);
                }
                i++;
            }
        }
        if (pkg.getLongVersionCode() < minVersionCode || pkg.getLongVersionCode() > maxVersionCode) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Static shared lib version codes must be ordered as lib versions", -14);
        }
    }

    private void assertOverlayIsValid(com.android.server.pm.pkg.AndroidPackage pkg, int parseFlags, int scanFlags) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.PackageSetting targetPkgSetting;
        com.android.server.pm.PackageSetting refPkgSetting;
        com.android.server.pm.PackageSetting platformPkgSetting;
        if ((65536 & scanFlags) != 0) {
            if ((parseFlags & 16) == 0) {
                if (!this.mPm.isOverlayMutable(pkg.getPackageName())) {
                    throw com.android.server.pm.PackageManagerException.ofInternalError("Overlay " + pkg.getPackageName() + " is static and cannot be upgraded.", -15);
                }
                return;
            } else if ((524288 & scanFlags) != 0) {
                if (pkg.getTargetSdkVersion() < com.android.server.pm.ScanPackageUtils.getVendorPartitionVersion()) {
                    android.util.Slog.w("PackageManager", "System overlay " + pkg.getPackageName() + " targets an SDK below the required SDK level of vendor overlays (" + com.android.server.pm.ScanPackageUtils.getVendorPartitionVersion() + "). This will become an install error in a future release");
                    return;
                }
                return;
            } else {
                if (pkg.getTargetSdkVersion() < android.os.Build.VERSION.SDK_INT) {
                    android.util.Slog.w("PackageManager", "System overlay " + pkg.getPackageName() + " targets an SDK below the required SDK level of system overlays (" + android.os.Build.VERSION.SDK_INT + "). This will become an install error in a future release");
                    return;
                }
                return;
            }
        }
        if (pkg.getTargetSdkVersion() < 29) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    platformPkgSetting = this.mPm.mSettings.getPackageLPr(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (!com.android.server.pm.PackageManagerServiceUtils.comparePackageSignatures(platformPkgSetting, pkg.getSigningDetails())) {
                throw com.android.server.pm.PackageManagerException.ofInternalError("Overlay " + pkg.getPackageName() + " must target Q or later, or be signed with the platform certificate", -16);
            }
        }
        if (pkg.getOverlayTargetOverlayableName() == null) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    targetPkgSetting = this.mPm.mSettings.getPackageLPr(pkg.getOverlayTarget());
                } finally {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (targetPkgSetting != null && !com.android.server.pm.PackageManagerServiceUtils.comparePackageSignatures(targetPkgSetting, pkg.getSigningDetails())) {
                if (this.mPm.mOverlayConfigSignaturePackage == null) {
                    throw com.android.server.pm.PackageManagerException.ofInternalError("Overlay " + pkg.getPackageName() + " and target " + pkg.getOverlayTarget() + " signed with different certificates, and the overlay lacks <overlay android:targetName>", -17);
                }
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock3) {
                    try {
                        refPkgSetting = this.mPm.mSettings.getPackageLPr(this.mPm.mOverlayConfigSignaturePackage);
                    } finally {
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                if (!com.android.server.pm.PackageManagerServiceUtils.comparePackageSignatures(refPkgSetting, pkg.getSigningDetails())) {
                    throw com.android.server.pm.PackageManagerException.ofInternalError("Overlay " + pkg.getPackageName() + " signed with a different certificate than both the reference package and target " + pkg.getOverlayTarget() + ", and the overlay lacks <overlay android:targetName>", -18);
                }
            }
        }
    }

    private void assertPackageWithSharedUserIdIsPrivileged(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.PackageSetting platformPkgSetting;
        if (!com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isPrivileged(pkg) && pkg.getSharedUserId() != null && !pkg.isLeavingSharedUser()) {
            com.android.server.pm.SharedUserSetting sharedUserSetting = null;
            try {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        sharedUserSetting = this.mPm.mSettings.getSharedUserLPw(pkg.getSharedUserId(), 0, 0, false);
                    } finally {
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            } catch (com.android.server.pm.PackageManagerException e) {
            }
            if (sharedUserSetting != null && sharedUserSetting.isPrivileged()) {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock2) {
                    try {
                        platformPkgSetting = this.mPm.mSettings.getPackageLPr(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                    } finally {
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                if (!com.android.server.pm.PackageManagerServiceUtils.comparePackageSignatures(platformPkgSetting, pkg.getSigningDetails())) {
                    throw com.android.server.pm.PackageManagerException.ofInternalError("Apps that share a user with a privileged app must themselves be marked as privileged. " + pkg.getPackageName() + " shares privileged user " + pkg.getSharedUserId() + ".", -19);
                }
            }
        }
    }

    private int adjustScanFlags(int scanFlags, com.android.server.pm.PackageSetting existingPkgSetting, com.android.server.pm.PackageSetting disabledPkgSetting, android.os.UserHandle user, com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        int scanFlags2 = com.android.server.pm.ScanPackageUtils.adjustScanFlagsWithPackageSetting(scanFlags, existingPkgSetting, disabledPkgSetting, user);
        boolean skipVendorPrivilegeScan = (524288 & scanFlags2) != 0 && com.android.server.pm.ScanPackageUtils.getVendorPartitionVersion() < 28;
        if ((scanFlags2 & 131072) == 0 && !com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isPrivileged(pkg) && pkg.getSharedUserId() != null && !skipVendorPrivilegeScan && !pkg.isLeavingSharedUser()) {
            com.android.server.pm.SharedUserSetting sharedUserSetting = null;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    try {
                        sharedUserSetting = this.mPm.mSettings.getSharedUserLPw(pkg.getSharedUserId(), 0, 0, false);
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                } catch (com.android.server.pm.PackageManagerException e) {
                }
                if (sharedUserSetting != null && sharedUserSetting.isPrivileged()) {
                    com.android.server.pm.PackageSetting platformPkgSetting = this.mPm.mSettings.getPackageLPr(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                    if (com.android.server.pm.PackageManagerServiceUtils.compareSignatures(platformPkgSetting.getSigningDetails(), pkg.getSigningDetails()) != 0) {
                        scanFlags2 |= 131072;
                    }
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        return scanFlags2;
    }

    public com.android.server.pm.IInstallPackageHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    private class InstallPackageHelperWrapper implements com.android.server.pm.IInstallPackageHelperWrapper {
        private InstallPackageHelperWrapper() {
        }

        @Override // com.android.server.pm.IInstallPackageHelperWrapper
        public com.android.server.pm.pkg.AndroidPackage addForInitLI(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int parseFlags, int scanFlags, android.os.UserHandle user, com.android.server.pm.ApexManager.ActiveApexInfo activeApexInfo) throws com.android.server.pm.PackageManagerException {
            return com.android.server.pm.InstallPackageHelper.this.addForInitLI(parsedPackage, parseFlags, scanFlags, user, activeApexInfo);
        }
    }
}
