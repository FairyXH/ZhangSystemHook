package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class AppDataHelper {
    public static boolean DEBUG_APP_DATA = false;
    private final com.android.server.pm.dex.ArtManagerService mArtManagerService;
    private final com.android.server.pm.PackageManagerServiceInjector mInjector;
    private final com.android.server.pm.Installer mInstaller;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.IAppDataHelperWrapper mWrapper = new com.android.server.pm.AppDataHelper.AppDataHelperWrapper();
    private final com.android.server.pm.IAppDataHelperExt mAppDataHelperExt = (com.android.server.pm.IAppDataHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IAppDataHelperExt.class).base(this).create();

    AppDataHelper(com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
        this.mInjector = this.mPm.mInjector;
        this.mInstaller = this.mInjector.getInstaller();
        this.mArtManagerService = this.mInjector.getArtManagerService();
        this.mAppDataHelperExt.init(this.mPm, this);
    }

    public void prepareAppDataAfterInstallLIF(com.android.server.pm.pkg.AndroidPackage pkg) {
        com.android.server.pm.PackageSetting ps;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                ps = this.mPm.mSettings.getPackageLPr(pkg.getPackageName());
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        prepareAppDataPostCommitLIF(ps, 0, getInstalledUsersForPackage(ps));
    }

    private int[] getInstalledUsersForPackage(com.android.server.pm.PackageSetting ps) {
        com.android.server.pm.UserManagerInternal umInternal = this.mInjector.getUserManagerInternal();
        java.util.List<android.content.pm.UserInfo> users = umInternal.getUsers(false);
        int[] userIds = new int[users.size()];
        int userIdsCount = 0;
        int size = users.size();
        for (int i = 0; i < size; i++) {
            int userId = users.get(i).id;
            if (ps.getInstalled(userId)) {
                userIds[userIdsCount] = userId;
                userIdsCount++;
            }
        }
        return java.util.Arrays.copyOf(userIds, userIdsCount);
    }

    public void prepareAppDataPostCommitLIF(final com.android.server.pm.PackageSetting ps, int previousAppId, int[] userIds) {
        int flags;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPm.mSettings.writeKernelMappingLPr(ps);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (ps.getPkg() != null && !shouldHaveAppStorage(ps.getPkg())) {
            android.util.Slog.w("PackageManager", "Skipping preparing app data for " + ps.getPackageName());
            return;
        }
        com.android.server.pm.Installer.Batch batch = new com.android.server.pm.Installer.Batch();
        final com.android.server.pm.UserManagerInternal umInternal = this.mInjector.getUserManagerInternal();
        final android.os.storage.StorageManagerInternal smInternal = (android.os.storage.StorageManagerInternal) this.mInjector.getLocalService(android.os.storage.StorageManagerInternal.class);
        for (final int userId : userIds) {
            if (android.os.storage.StorageManager.isCeStorageUnlocked(userId) && smInternal.isCeStoragePrepared(userId)) {
                flags = 3;
            } else if (umInternal.isUserRunning(userId)) {
                flags = 1;
            }
            prepareAppData(batch, ps, previousAppId, userId, flags).thenRun(new java.lang.Runnable() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.pm.AppDataHelper.lambda$prepareAppDataPostCommitLIF$0(umInternal, userId, ps, smInternal);
                }
            });
        }
        executeBatchLI(batch);
    }

    static /* synthetic */ void lambda$prepareAppDataPostCommitLIF$0(com.android.server.pm.UserManagerInternal umInternal, int userId, com.android.server.pm.PackageSetting ps, android.os.storage.StorageManagerInternal smInternal) {
        if (umInternal.isUserUnlockingOrUnlocked(userId)) {
            int uid = android.os.UserHandle.getUid(userId, ps.getAppId());
            smInternal.prepareAppDataAfterInstall(ps.getPackageName(), uid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeBatchLI(com.android.server.pm.Installer.Batch batch) {
        try {
            batch.execute(this.mInstaller);
        } catch (com.android.server.pm.Installer.InstallerException e) {
            android.util.Slog.w("PackageManager", "Failed to execute pending operations", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareAppDataAndMigrate(com.android.server.pm.Installer.Batch batch, final com.android.server.pm.pkg.AndroidPackage pkg, final int userId, final int flags, final boolean maybeMigrateAppData) {
        final com.android.server.pm.PackageSetting ps;
        if (pkg == null) {
            android.util.Slog.wtf("PackageManager", "Package was null!", new java.lang.Throwable());
            return;
        }
        if (!shouldHaveAppStorage(pkg)) {
            android.util.Slog.w("PackageManager", "Skipping preparing app data for " + pkg.getPackageName());
            return;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                ps = this.mPm.mSettings.getPackageLPr(pkg.getPackageName());
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        prepareAppData(batch, ps, -1, userId, flags).thenRun(new java.lang.Runnable() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$prepareAppDataAndMigrate$1(maybeMigrateAppData, ps, userId, flags, pkg);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareAppDataAndMigrate$1(boolean maybeMigrateAppData, com.android.server.pm.PackageSetting ps, int userId, int flags, com.android.server.pm.pkg.AndroidPackage pkg) throws java.lang.Throwable {
        if (maybeMigrateAppData && maybeMigrateAppDataLIF(ps, userId)) {
            com.android.server.pm.Installer.Batch batchInner = new com.android.server.pm.Installer.Batch();
            prepareAppData(batchInner, ps, -1, userId, flags);
            executeBatchLI(batchInner);
        }
        this.mWrapper.getExtImpl().afterDataPreparedInPrepareAppDataAndMigrate(pkg, userId, flags);
    }

    private java.util.concurrent.CompletableFuture<?> prepareAppData(final com.android.server.pm.Installer.Batch batch, final com.android.server.pm.PackageSetting ps, int previousAppId, final int userId, final int flags) throws java.lang.Throwable {
        java.lang.String seInfoUser;
        final java.lang.String packageName = ps.getPackageName();
        if (DEBUG_APP_DATA) {
            android.util.Slog.v("PackageManager", "prepareAppData for " + packageName + " u" + userId + " 0x" + java.lang.Integer.toHexString(flags));
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                seInfoUser = com.android.server.pm.pkg.SELinuxUtil.getSeinfoUser(ps.readUserState(userId));
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        final com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        final java.lang.String volumeUuid = ps.getVolumeUuid();
        final int appId = ps.getAppId();
        java.lang.String pkgSeInfo = ps.getSeInfo();
        com.android.internal.util.Preconditions.checkNotNull(pkgSeInfo);
        final java.lang.String seInfo = pkgSeInfo + seInfoUser;
        int targetSdkVersion = ps.getTargetSdkVersion();
        boolean usesSdk = ps.getUsesSdkLibraries().length > 0;
        final android.os.CreateAppDataArgs args = com.android.server.pm.Installer.buildCreateAppDataArgs(volumeUuid, packageName, userId, flags, appId, seInfo, targetSdkVersion, usesSdk);
        args.previousAppId = previousAppId;
        return batch.createAppData(args).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$prepareAppData$2(batch, ps, pkg, userId, flags, appId, volumeUuid, seInfo, packageName, args, (android.os.CreateAppDataResult) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareAppData$2(com.android.server.pm.Installer.Batch batch, com.android.server.pm.PackageSetting ps, com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags, int appId, java.lang.String volumeUuid, java.lang.String seInfo, java.lang.String packageName, android.os.CreateAppDataArgs args, android.os.CreateAppDataResult createAppDataResult, java.lang.Throwable e) {
        long j;
        android.os.CreateAppDataResult createAppDataResult2;
        if (this.mWrapper.getExtImpl().skipWorkAfterCreateAppData(batch.mBatchExt, ps)) {
            return;
        }
        if (e == null) {
            j = -1;
            createAppDataResult2 = createAppDataResult;
        } else {
            android.os.CreateAppDataResult createAppDataResultTemp = new android.os.CreateAppDataResult();
            createAppDataResultTemp.ceDataInode = -1L;
            createAppDataResultTemp.deDataInode = -1L;
            createAppDataResultTemp.exceptionCode = 0;
            createAppDataResultTemp.exceptionMessage = null;
            j = -1;
            android.os.CreateAppDataResult createAppDataResult3 = this.mWrapper.getExtImpl().fixDataForExceptionInPrepareAppDataLeaf(createAppDataResultTemp, pkg, userId, flags, appId, volumeUuid, seInfo, packageName);
            if (createAppDataResult3.ceDataInode == -1) {
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Failed to create app data for " + packageName + ", but trying to recover: " + e);
                destroyAppDataLeafLIF(packageName, volumeUuid, userId, flags);
                try {
                    try {
                        createAppDataResult3 = this.mInstaller.createAppData(args);
                        com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(3, "Recovery succeeded!");
                        createAppDataResult2 = createAppDataResult3;
                    } catch (com.android.server.pm.Installer.InstallerException e2) {
                        com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(3, "Recovery failed!");
                        createAppDataResult2 = createAppDataResult3;
                    }
                } catch (com.android.server.pm.Installer.InstallerException e3) {
                }
            } else {
                createAppDataResult2 = createAppDataResult3;
            }
        }
        long ceDataInode = createAppDataResult2.ceDataInode;
        long deDataInode = createAppDataResult2.deDataInode;
        if ((flags & 2) != 0 && ceDataInode != j) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    ps.setCeDataInode(ceDataInode, userId);
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        if ((flags & 1) != 0 && deDataInode != j) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    ps.setDeDataInode(deDataInode, userId);
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        if (pkg != null) {
            prepareAppDataContentsLeafLIF(pkg, ps, userId, flags);
        }
        this.mWrapper.getExtImpl().afterCreateAppDataCompleted(java.lang.Long.valueOf(ceDataInode), e, pkg, userId, flags);
    }

    public void prepareAppDataContentsLIF(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId, int flags) {
        if (pkg == null) {
            android.util.Slog.wtf("PackageManager", "Package was null!", new java.lang.Throwable());
        } else {
            prepareAppDataContentsLeafLIF(pkg, pkgSetting, userId, flags);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareAppDataContentsLeafLIF(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId, int flags) {
        java.lang.String volumeUuid = pkg.getVolumeUuid();
        java.lang.String packageName = pkg.getPackageName();
        if ((flags & 2) != 0) {
            java.lang.String primaryCpuAbi = pkgSetting == null ? com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(pkg) : pkgSetting.getPrimaryCpuAbi();
            if (primaryCpuAbi != null && !dalvik.system.VMRuntime.is64BitAbi(primaryCpuAbi)) {
                java.lang.String nativeLibPath = pkg.getNativeLibraryDir();
                if (!new java.io.File(nativeLibPath).exists()) {
                    return;
                }
                try {
                    this.mInstaller.linkNativeLibraryDirectory(volumeUuid, packageName, nativeLibPath, userId);
                } catch (com.android.server.pm.Installer.InstallerException e) {
                    android.util.Slog.e("PackageManager", "Failed to link native for " + packageName + ": " + e);
                }
            }
        }
    }

    private boolean maybeMigrateAppDataLIF(com.android.server.pm.PackageSetting ps, int userId) {
        if (ps.isSystem() && !android.os.storage.StorageManager.isFileEncrypted()) {
            int storageTarget = ps.isDefaultToDeviceProtectedStorage() ? 1 : 2;
            try {
                this.mInstaller.migrateAppData(ps.getVolumeUuid(), ps.getPackageName(), userId, storageTarget);
            } catch (com.android.server.pm.Installer.InstallerException e) {
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Failed to migrate " + ps.getPackageName() + ": " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    public void reconcileAppsData(int userId, int flags, boolean migrateAppsData) {
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mInjector.getSystemService(android.os.storage.StorageManager.class);
        if (this.mAppDataHelperExt != null) {
            this.mAppDataHelperExt.beforeReconcileAppsData("reconcileAppsData");
        }
        for (android.os.storage.VolumeInfo vol : storage.getWritablePrivateVolumes()) {
            java.lang.String volumeUuid = vol.getFsUuid();
            com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
            try {
                reconcileAppsDataLI(volumeUuid, userId, flags, migrateAppsData);
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
        }
        if (this.mAppDataHelperExt != null) {
            this.mAppDataHelperExt.afterReconcileAppsData("reconcileAppsData");
        }
        this.mWrapper.getExtImpl().onEndInReconcileAppsData(this.mPm.isDeviceUpgrading(), com.android.server.pm.PackageManagerService.DEBUG_DEXOPT);
    }

    void reconcileAppsDataLI(java.lang.String volumeUuid, int userId, int flags, boolean migrateAppData) {
        reconcileAppsDataLI(volumeUuid, userId, flags, migrateAppData, false);
    }

    private java.util.List<java.lang.String> reconcileAppsDataLI(java.lang.String volumeUuid, int userId, int flags, boolean migrateAppData, boolean onlyCoreApps) {
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        int i;
        com.android.server.pm.Computer snapshot;
        com.android.server.pm.Computer snapshot2;
        com.android.server.pm.IPkgReconcileSkipExt pkgReconcileSkip;
        com.android.server.pm.Computer snapshot3;
        java.io.File[] files;
        com.android.server.pm.Computer snapshot4;
        int i2;
        java.lang.String str4;
        int i3;
        java.lang.String str5;
        int i4;
        int i5;
        java.io.File[] files2;
        java.lang.String str6;
        java.lang.String str7;
        com.android.server.pm.Computer snapshot5;
        java.lang.String str8;
        int i6;
        android.util.Slog.v("PackageManager", "reconcileAppsData for " + volumeUuid + " u" + userId + " 0x" + java.lang.Integer.toHexString(flags) + " migrateAppData=" + migrateAppData);
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("PMS:reconcileAppsDataLI");
        java.util.List<java.lang.String> result = onlyCoreApps ? new java.util.ArrayList<>() : null;
        int i7 = 5;
        try {
            this.mInstaller.cleanupInvalidPackageDirs(volumeUuid, userId, flags);
        } catch (com.android.server.pm.Installer.InstallerException e) {
            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Failed to cleanup deleted dirs: " + e);
        }
        java.io.File ceDir = android.os.Environment.getDataUserCeDirectory(volumeUuid, userId);
        java.io.File deDir = android.os.Environment.getDataUserDeDirectory(volumeUuid, userId);
        com.android.server.pm.Computer snapshot6 = this.mPm.snapshotComputer();
        java.lang.String str9 = " due to: ";
        java.lang.String str10 = "Destroying ";
        if ((flags & 2) == 0) {
            str = "Destroying ";
            str2 = " due to: ";
            str3 = "Failed to destroy: ";
            i = 5;
            snapshot = snapshot6;
        } else if (!this.mWrapper.getExtImpl().useCustomLogicForCeInReconcileAppsDataLI(onlyCoreApps, flags, userId)) {
            android.util.Slog.i("PackageManager", "reconcileAppsData for StorageManager.FLAG_STORAGE_CE start");
            if (android.os.storage.StorageManager.isFileEncrypted() && !android.os.storage.StorageManager.isCeStorageUnlocked(userId)) {
                throw new java.lang.RuntimeException("Yikes, someone asked us to reconcile CE storage while " + userId + " was still locked; this would have caused massive data loss!");
            }
            java.io.File[] files3 = android.os.FileUtils.listFilesOrEmpty(ceDir);
            int length = files3.length;
            java.lang.String str11 = "Failed to destroy: ";
            int i8 = 0;
            while (i8 < length) {
                java.io.File file = files3[i8];
                int i9 = i8;
                java.lang.String packageName = file.getName();
                try {
                    assertPackageStorageValid(snapshot6, volumeUuid, packageName, userId);
                    i5 = length;
                    files2 = files3;
                    str6 = str10;
                    str7 = str9;
                    snapshot5 = snapshot6;
                    str8 = str11;
                    i6 = i9;
                    i4 = 5;
                } catch (com.android.server.pm.PackageManagerException e2) {
                    i4 = 5;
                    com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, str10 + file + str9 + e2);
                    try {
                        if (this.mWrapper.getExtImpl().skipDestroyCeDataInReconcileAppsDataLI(volumeUuid, packageName, userId)) {
                            i5 = length;
                            files2 = files3;
                            str6 = str10;
                            str7 = str9;
                            snapshot5 = snapshot6;
                            str8 = str11;
                            i6 = i9;
                        } else {
                            i5 = length;
                            files2 = files3;
                            str7 = str9;
                            str8 = str11;
                            i6 = i9;
                            str6 = str10;
                            snapshot5 = snapshot6;
                            i4 = 5;
                            try {
                                this.mInstaller.destroyAppData(volumeUuid, packageName, userId, 2, 0L);
                            } catch (com.android.server.pm.Installer.InstallerException e3) {
                                e2 = e3;
                                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(i4, str8 + e2);
                            }
                        }
                    } catch (com.android.server.pm.Installer.InstallerException e4) {
                        e2 = e4;
                        i5 = length;
                        files2 = files3;
                        str6 = str10;
                        str7 = str9;
                        snapshot5 = snapshot6;
                        str8 = str11;
                        i6 = i9;
                    }
                }
                i8 = i6 + 1;
                i7 = i4;
                str11 = str8;
                snapshot6 = snapshot5;
                length = i5;
                files3 = files2;
                str9 = str7;
                str10 = str6;
            }
            str = str10;
            str2 = str9;
            i = i7;
            snapshot = snapshot6;
            str3 = str11;
        } else {
            return this.mWrapper.getExtImpl().customLogicForCeInReconcileAppsDataLI(result, ceDir, volumeUuid, userId, flags, onlyCoreApps, migrateAppData, snapshot6);
        }
        if ((flags & 1) != 0) {
            android.util.Slog.i("PackageManager", "reconcileAppsData for StorageManager.FLAG_STORAGE_DE start");
            java.io.File[] files4 = android.os.FileUtils.listFilesOrEmpty(deDir);
            int length2 = files4.length;
            int i10 = 0;
            while (i10 < length2) {
                java.io.File file2 = files4[i10];
                java.lang.String packageName2 = file2.getName();
                com.android.server.pm.Computer snapshot7 = snapshot;
                try {
                    assertPackageStorageValid(snapshot7, volumeUuid, packageName2, userId);
                    snapshot4 = snapshot7;
                    i2 = i10;
                    files = files4;
                    i3 = length2;
                    str4 = str2;
                    str5 = str;
                } catch (com.android.server.pm.PackageManagerException e5) {
                    files = files4;
                    java.lang.String str12 = str2;
                    com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(i, str + file2 + str12 + e5);
                    try {
                        if (this.mWrapper.getExtImpl().skipDestroyDeDataInReconcileAppsDataLI(volumeUuid, packageName2, userId)) {
                            snapshot4 = snapshot7;
                            i2 = i10;
                            str4 = str12;
                            i3 = length2;
                            str5 = str;
                        } else {
                            snapshot4 = snapshot7;
                            i2 = i10;
                            str4 = str12;
                            i3 = length2;
                            str5 = str;
                            try {
                                this.mInstaller.destroyAppData(volumeUuid, packageName2, userId, 1, 0L);
                            } catch (com.android.server.pm.Installer.InstallerException e6) {
                                e2 = e6;
                                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(i, str3 + e2);
                            }
                        }
                    } catch (com.android.server.pm.Installer.InstallerException e7) {
                        e2 = e7;
                        snapshot4 = snapshot7;
                        i2 = i10;
                        str4 = str12;
                        i3 = length2;
                        str5 = str;
                    }
                }
                i10 = i2 + 1;
                files4 = files;
                length2 = i3;
                str2 = str4;
                str = str5;
                snapshot = snapshot4;
            }
            snapshot2 = snapshot;
        } else {
            snapshot2 = snapshot;
        }
        android.os.Trace.traceBegin(262144L, "prepareAppDataAndMigrate");
        com.android.server.pm.Installer.Batch batch = new com.android.server.pm.Installer.Batch();
        com.android.server.pm.Computer snapshot8 = snapshot2;
        java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> packages = snapshot8.getVolumePackages(volumeUuid);
        com.android.server.pm.IPkgReconcileDelayedExt pkgReconcileDelayed = this.mWrapper.getExtImpl().beforePrepareAppDataInRADL(flags, userId, this.mPm.isDeviceUpgrading(), volumeUuid, migrateAppData);
        com.android.server.pm.IPkgReconcileSkipExt pkgReconcileSkip2 = this.mWrapper.getExtImpl().beforePrepareAppDataInRADL2(flags, userId, volumeUuid);
        int preparedCount = 0;
        for (com.android.server.pm.pkg.PackageStateInternal ps : packages) {
            java.lang.String packageName3 = ps.getPackageName();
            if (ps.getPkg() == null) {
                android.util.Slog.w("PackageManager", "Odd, missing scanned package " + packageName3);
            } else if (!this.mWrapper.getExtImpl().skipPrepareAppDataForPkgInRADL(pkgReconcileSkip2, ps)) {
                if (onlyCoreApps && !ps.getPkg().isCoreApp()) {
                    result.add(packageName3);
                } else if (ps.getUserStateOrDefault(userId).isInstalled() && !this.mWrapper.getExtImpl().delayPrepareAppDataInRADL(pkgReconcileDelayed, ps)) {
                    snapshot3 = snapshot8;
                    pkgReconcileSkip = pkgReconcileSkip2;
                    prepareAppDataAndMigrate(batch, ps.getPkg(), userId, flags, migrateAppData);
                    preparedCount++;
                    pkgReconcileSkip2 = pkgReconcileSkip;
                    snapshot8 = snapshot3;
                } else {
                    pkgReconcileSkip = pkgReconcileSkip2;
                    snapshot3 = snapshot8;
                    int preparedCount2 = preparedCount;
                    preparedCount = preparedCount2;
                    pkgReconcileSkip2 = pkgReconcileSkip;
                    snapshot8 = snapshot3;
                }
            }
        }
        executeBatchLI(batch);
        this.mWrapper.getExtImpl().afterExecuteBatchInReconcileAppsDataLI0(pkgReconcileDelayed);
        this.mWrapper.getExtImpl().afterExecuteBatchInReconcileAppsDataLI(volumeUuid, userId, flags);
        android.os.Trace.traceEnd(262144L);
        android.util.Slog.v("PackageManager", "reconcileAppsData finished " + preparedCount + " packages");
        this.mWrapper.getExtImpl().onEndInReconcileAppsDataLI(flags);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertPackageStorageValid(com.android.server.pm.Computer snapshot, java.lang.String volumeUuid, java.lang.String packageName, int userId) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        if (packageState == null) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Package " + packageName + " is unknown", -7);
        }
        if (!android.text.TextUtils.equals(volumeUuid, packageState.getVolumeUuid())) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Package " + packageName + " found on unknown volume " + volumeUuid + "; expected volume " + packageState.getVolumeUuid(), -8);
        }
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        if (!userState.isInstalled() && !userState.dataExists()) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Package " + packageName + " not installed for user " + userId + " or was deleted without DELETE_KEEP_DATA", -9);
        }
        if (packageState.getPkg() != null && !shouldHaveAppStorage(packageState.getPkg())) {
            throw com.android.server.pm.PackageManagerException.ofInternalError("Package " + packageName + " shouldn't have storage", -10);
        }
    }

    public java.util.concurrent.Future<?> fixAppsDataOnBoot() {
        final int storageFlags;
        if (android.os.storage.StorageManager.isFileEncrypted()) {
            storageFlags = 1;
        } else {
            storageFlags = 3;
        }
        this.mWrapper.getExtImpl().beforeReconcileAppsDataInConstructor();
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            final java.util.List<java.lang.String> deferPackages = reconcileAppsDataLI(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, 0, storageFlags, true, true);
            if (installLock != null) {
                installLock.close();
            }
            java.util.concurrent.Future<?> prepareAppDataFuture = com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$fixAppsDataOnBoot$3(deferPackages, storageFlags);
                }
            }, "prepareAppData");
            return prepareAppDataFuture;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fixAppsDataOnBoot$3(java.util.List deferPackages, int storageFlags) {
        android.util.TimingsTraceLog traceLog = new android.util.TimingsTraceLog("SystemServerTimingAsync", 262144L);
        traceLog.traceBegin("AppDataFixup");
        try {
            this.mInstaller.fixupAppData(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, 3);
        } catch (com.android.server.pm.Installer.InstallerException e) {
            android.util.Slog.w("PackageManager", "Trouble fixing GIDs", e);
        }
        traceLog.traceEnd();
        traceLog.traceBegin("AppDataPrepare");
        if (deferPackages == null || deferPackages.isEmpty()) {
            this.mWrapper.getExtImpl().onPrepareAppDataFutureEndByNoDefer();
            return;
        }
        com.android.server.pm.Installer.Batch batch = new com.android.server.pm.Installer.Batch();
        java.util.Iterator it = deferPackages.iterator();
        int count = 0;
        while (it.hasNext()) {
            java.lang.String pkgName = (java.lang.String) it.next();
            com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal packageStateInternal = snapshot.getPackageStateInternal(pkgName);
            if (packageStateInternal != null && packageStateInternal.getUserStateOrDefault(0).isInstalled()) {
                prepareAppDataAndMigrate(batch, packageStateInternal.getPkg(), 0, storageFlags, true);
                count++;
            }
        }
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            executeBatchLI(batch);
            if (installLock != null) {
                installLock.close();
            }
            this.mWrapper.getExtImpl().onPrepareAppDataFutureEndByDeferDone(storageFlags);
            traceLog.traceEnd();
            android.util.Slog.i("PackageManager", "Deferred reconcileAppsData finished " + count + " packages");
        } finally {
        }
    }

    void clearAppDataLIF(com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags) {
        if (pkg == null) {
            return;
        }
        clearAppDataLeafLIF(pkg.getPackageName(), pkg.getVolumeUuid(), userId, flags);
        if ((131072 & flags) == 0) {
            clearAppProfilesLIF(pkg);
        }
    }

    void clearAppDataLeafLIF(java.lang.String packageName, java.lang.String volumeUuid, int userId, int flags) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal packageStateInternal = snapshot.getPackageStateInternal(packageName);
        for (int realUserId : this.mPm.resolveUserIds(userId)) {
            long ceDataInode = packageStateInternal != null ? packageStateInternal.getUserStateOrDefault(realUserId).getCeDataInode() : 0L;
            try {
                try {
                    this.mInstaller.clearAppData(volumeUuid, packageName, realUserId, flags, ceDataInode);
                } catch (com.android.server.pm.Installer.InstallerException e) {
                    e = e;
                    android.util.Slog.w("PackageManager", java.lang.String.valueOf(e));
                }
            } catch (com.android.server.pm.Installer.InstallerException e2) {
                e = e2;
            }
        }
    }

    void clearAppProfilesLIF(com.android.server.pm.pkg.AndroidPackage pkg) {
        if (pkg == null) {
            android.util.Slog.wtf("PackageManager", "Package was null!", new java.lang.Throwable());
        } else {
            destroyAppProfilesLIF(pkg.getPackageName());
        }
    }

    public void destroyAppDataLIF(com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags) {
        if (pkg == null) {
            android.util.Slog.wtf("PackageManager", "Package was null!", new java.lang.Throwable());
        } else {
            destroyAppDataLeafLIF(pkg.getPackageName(), pkg.getVolumeUuid(), userId, flags);
        }
    }

    private void destroyAppDataLeafLIF(java.lang.String packageName, java.lang.String volumeUuid, int userId, int flags) {
        int i;
        int i2;
        int[] iArr;
        com.android.server.pm.pkg.PackageStateInternal packageStateInternal;
        com.android.server.pm.Computer snapshot;
        java.lang.String str;
        int i3;
        java.lang.String str2 = packageName;
        int i4 = userId;
        com.android.server.pm.Computer snapshot2 = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal packageStateInternal2 = snapshot2.getPackageStateInternal(str2);
        int[] iArrResolveUserIds = this.mPm.resolveUserIds(i4);
        int length = iArrResolveUserIds.length;
        int i5 = 0;
        while (i5 < length) {
            int realUserId = iArrResolveUserIds[i5];
            if (this.mWrapper.getExtImpl().skipDestroyAppDataInDestroyAppDataLeafLIF(volumeUuid, str2, realUserId, flags)) {
                i = i5;
                i2 = length;
                iArr = iArrResolveUserIds;
                packageStateInternal = packageStateInternal2;
                snapshot = snapshot2;
                str = str2;
                i3 = i4;
            } else {
                long ceDataInode = packageStateInternal2 != null ? packageStateInternal2.getUserStateOrDefault(realUserId).getCeDataInode() : 0L;
                if (this.mPm.mPackageManagerServiceExt.skipDestroyAppDataInDestroyAppDataLeafLIF2(volumeUuid, packageName, realUserId, flags, ceDataInode)) {
                    i = i5;
                    i2 = length;
                    iArr = iArrResolveUserIds;
                    packageStateInternal = packageStateInternal2;
                    snapshot = snapshot2;
                    str = str2;
                    i3 = i4;
                } else {
                    try {
                        i = i5;
                        packageStateInternal = packageStateInternal2;
                        snapshot = snapshot2;
                        i2 = length;
                        iArr = iArrResolveUserIds;
                        str = str2;
                        i3 = i4;
                        try {
                            this.mInstaller.destroyAppData(volumeUuid, packageName, realUserId, flags, ceDataInode);
                        } catch (com.android.server.pm.Installer.InstallerException e) {
                            e = e;
                            android.util.Slog.w("PackageManager", java.lang.String.valueOf(e));
                        }
                    } catch (com.android.server.pm.Installer.InstallerException e2) {
                        e = e2;
                        i = i5;
                        i2 = length;
                        iArr = iArrResolveUserIds;
                        packageStateInternal = packageStateInternal2;
                        snapshot = snapshot2;
                        str = str2;
                        i3 = i4;
                    }
                    this.mPm.getDexManager().notifyPackageDataDestroyed(str, i3);
                    this.mPm.getDynamicCodeLogger().notifyPackageDataDestroyed(str, i3);
                }
            }
            i5 = i + 1;
            str2 = str;
            i4 = i3;
            packageStateInternal2 = packageStateInternal;
            snapshot2 = snapshot;
            length = i2;
            iArrResolveUserIds = iArr;
        }
    }

    void destroyAppProfilesLIF(java.lang.String packageName) {
        if (!com.android.server.pm.DexOptHelper.artManagerLocalIsInitialized()) {
            return;
        }
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
        try {
            try {
                com.android.server.pm.DexOptHelper.getArtManagerLocal().clearAppProfiles(snapshot, packageName);
            } catch (java.lang.Throwable th) {
                if (snapshot != null) {
                    try {
                        snapshot.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.w("PackageManager", e);
        }
        if (snapshot != null) {
            snapshot.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldHaveAppStorage(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.content.pm.PackageManager.Property noAppDataProp = (android.content.pm.PackageManager.Property) pkg.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        return (noAppDataProp == null || !noAppDataProp.getBoolean()) && pkg.getUid() >= 0;
    }

    public void clearKeystoreData(int userId, int appId) {
        if (appId < 0) {
            return;
        }
        for (int realUserId : this.mPm.resolveUserIds(userId)) {
            android.security.AndroidKeyStoreMaintenance.clearNamespace(0, android.os.UserHandle.getUid(realUserId, appId));
        }
    }

    public com.android.server.pm.IAppDataHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    private class AppDataHelperWrapper implements com.android.server.pm.IAppDataHelperWrapper {
        private AppDataHelperWrapper() {
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public com.android.server.pm.IAppDataHelperExt getExtImpl() {
            return com.android.server.pm.AppDataHelper.this.mAppDataHelperExt;
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void prepareAppDataAndMigrate(com.android.server.pm.Installer.Batch batch, com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags, boolean maybeMigrateAppData) {
            com.android.server.pm.AppDataHelper.this.prepareAppDataAndMigrate(batch, pkg, userId, flags, maybeMigrateAppData);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void assertPackageStorageValid(com.android.server.pm.Computer snapshot, java.lang.String volumeUuid, java.lang.String packageName, int userId) throws com.android.server.pm.PackageManagerException {
            com.android.server.pm.AppDataHelper.this.assertPackageStorageValid(snapshot, volumeUuid, packageName, userId);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void executeBatchLI(com.android.server.pm.Installer.Batch batch) {
            com.android.server.pm.AppDataHelper.this.executeBatchLI(batch);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public boolean shouldHaveAppStorage(com.android.server.pm.pkg.AndroidPackage pkg) {
            return com.android.server.pm.AppDataHelper.this.shouldHaveAppStorage(pkg);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void prepareAppDataContentsLeafLIF(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId, int flags) {
            com.android.server.pm.AppDataHelper.this.prepareAppDataContentsLeafLIF(pkg, pkgSetting, userId, flags);
        }
    }
}
