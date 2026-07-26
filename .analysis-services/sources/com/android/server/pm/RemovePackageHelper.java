package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class RemovePackageHelper {
    private final com.android.server.pm.AppDataHelper mAppDataHelper;
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final android.os.incremental.IncrementalManager mIncrementalManager;
    private final com.android.server.pm.Installer mInstaller;
    private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManager;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.SharedLibrariesImpl mSharedLibraries;
    private final com.android.server.pm.RemovePackageHelper.RemovePackageHelplerWrapper mWrapper = new com.android.server.pm.RemovePackageHelper.RemovePackageHelplerWrapper();

    RemovePackageHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.AppDataHelper appDataHelper, com.android.server.pm.BroadcastHelper broadcastHelper) {
        this.mPm = pm;
        this.mIncrementalManager = this.mPm.mInjector.getIncrementalManager();
        this.mInstaller = this.mPm.mInjector.getInstaller();
        this.mPermissionManager = this.mPm.mInjector.getPermissionManagerServiceInternal();
        this.mSharedLibraries = this.mPm.mInjector.getSharedLibrariesImpl();
        this.mAppDataHelper = appDataHelper;
        this.mBroadcastHelper = broadcastHelper;
    }

    public void removeCodePath(java.io.File codePath) {
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            removeCodePathLI(codePath);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCodePathLI(java.io.File codePath) {
        if (codePath == null || !codePath.exists()) {
            return;
        }
        if (codePath.isDirectory()) {
            java.io.File codePathParent = codePath.getParentFile();
            boolean needRemoveParent = codePathParent.getName().startsWith("~~");
            try {
                boolean isIncremental = this.mIncrementalManager != null && android.os.incremental.IncrementalManager.isIncrementalPath(codePath.getAbsolutePath());
                if (isIncremental) {
                    if (needRemoveParent) {
                        this.mIncrementalManager.rmPackageDir(codePathParent);
                    } else {
                        this.mIncrementalManager.rmPackageDir(codePath);
                    }
                }
                java.lang.String packageName = codePath.getName();
                if (!((com.android.server.pm.IPackageAbiHelperExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageAbiHelperExt.IStaticExt.class).create()).isRemovableApkDir(codePath.getAbsolutePath())) {
                    this.mInstaller.rmPackageDir(packageName, codePath.getAbsolutePath());
                }
                if (needRemoveParent) {
                    this.mInstaller.rmPackageDir(packageName, codePathParent.getAbsolutePath());
                    removeCachedResult(codePathParent);
                    return;
                }
                return;
            } catch (com.android.server.pm.Installer.InstallerException e) {
                android.util.Slog.w("PackageManager", "Failed to remove code path", e);
                return;
            }
        }
        codePath.delete();
    }

    private void removeCachedResult(java.io.File codePath) {
        if (this.mPm.getCacheDir() == null) {
            return;
        }
        com.android.server.pm.parsing.PackageCacher cacher = new com.android.server.pm.parsing.PackageCacher(this.mPm.getCacheDir());
        cacher.cleanCachedResult(codePath);
    }

    public void removePackage(com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            removePackageLI(pkg, chatty);
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

    private void removePackageLI(com.android.server.pm.pkg.AndroidPackage pkg, boolean chatty) {
        com.android.server.pm.pkg.PackageStateInternal ps = this.mPm.snapshotComputer().getPackageStateInternal(pkg.getPackageName());
        if (ps != null) {
            removePackageLI(ps.getPackageName(), chatty);
        } else if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
            android.util.Log.d("PackageManager", "Not removing package " + pkg.getPackageName() + "; mExtras == null");
        }
    }

    private void removePackageLI(java.lang.String packageName, boolean chatty) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL && chatty) {
            android.util.Log.d("PackageManager", "Removing package " + packageName);
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.pkg.AndroidPackage removedPackage = this.mPm.mPackages.remove(packageName);
                if (removedPackage != null) {
                    cleanPackageDataStructuresLILPw(removedPackage, com.android.internal.pm.parsing.pkg.AndroidPackageLegacyUtils.isSystem(removedPackage), chatty);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void cleanPackageDataStructuresLILPw(com.android.server.pm.pkg.AndroidPackage pkg, boolean isSystemApp, boolean chatty) {
        this.mPm.mComponentResolver.removeAllComponents(pkg, chatty);
        this.mPermissionManager.onPackageRemoved(pkg);
        this.mPm.getPackageProperty().removeAllProperties(pkg);
        int instrumentationSize = com.android.internal.util.ArrayUtils.size(pkg.getInstrumentations());
        java.lang.StringBuilder r = null;
        for (int i = 0; i < instrumentationSize; i++) {
            com.android.internal.pm.pkg.component.ParsedInstrumentation a = (com.android.internal.pm.pkg.component.ParsedInstrumentation) pkg.getInstrumentations().get(i);
            this.mPm.getInstrumentation().remove(a.getComponentName());
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                if (r == null) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append(a.getName());
            }
        }
        if (r != null && com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
            android.util.Log.d("PackageManager", "  Instrumentation: " + ((java.lang.Object) r));
        }
        java.lang.StringBuilder r2 = null;
        if (isSystemApp) {
            int libraryNamesSize = pkg.getLibraryNames().size();
            for (int i2 = 0; i2 < libraryNamesSize; i2++) {
                java.lang.String name = (java.lang.String) pkg.getLibraryNames().get(i2);
                if (this.mSharedLibraries.removeSharedLibrary(name, 0L) && com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
                    if (r2 == null) {
                        r2 = new java.lang.StringBuilder(256);
                    } else {
                        r2.append(' ');
                    }
                    r2.append(name);
                }
            }
        }
        java.lang.StringBuilder r3 = null;
        if (pkg.getSdkLibraryName() != null && this.mSharedLibraries.removeSharedLibrary(pkg.getSdkLibraryName(), pkg.getSdkLibVersionMajor()) && com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
            if (0 == 0) {
                r3 = new java.lang.StringBuilder(256);
            } else {
                r3.append(' ');
            }
            r3.append(pkg.getSdkLibraryName());
        }
        if (pkg.getStaticSharedLibraryName() != null && this.mSharedLibraries.removeSharedLibrary(pkg.getStaticSharedLibraryName(), pkg.getStaticSharedLibraryVersion()) && com.android.server.pm.PackageManagerService.DEBUG_REMOVE && chatty) {
            if (r3 == null) {
                r3 = new java.lang.StringBuilder(256);
            } else {
                r3.append(' ');
            }
            r3.append(pkg.getStaticSharedLibraryName());
        }
        if (r3 != null && com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
            android.util.Log.d("PackageManager", "  Libraries: " + ((java.lang.Object) r3));
        }
    }

    public void clearPackageStateForUserLIF(final com.android.server.pm.PackageSetting ps, final int userId, int flags) {
        com.android.server.pm.pkg.AndroidPackage pkg;
        com.android.server.pm.SharedUserSetting sus;
        com.android.server.pm.pkg.AndroidPackage resolvedPkg;
        final java.lang.String packageName = ps.getPackageName();
        this.mAppDataHelper.destroyAppProfilesLIF(packageName);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                pkg = this.mPm.mPackages.get(packageName);
                sus = this.mPm.mSettings.getSharedUserSettingLPr(ps);
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (pkg != null) {
            resolvedPkg = pkg;
        } else {
            resolvedPkg = com.android.internal.pm.parsing.pkg.PackageImpl.buildFakeForDeletion(packageName, ps.getVolumeUuid());
        }
        if ((flags & 1) == 0) {
            this.mAppDataHelper.destroyAppDataLIF(resolvedPkg, userId, 7);
            if (userId != -1) {
                ps.setCeDataInode(-1L, userId);
                ps.setDeDataInode(-1L, userId);
            }
            final com.android.server.pm.PreferredActivityHelper preferredActivityHelper = new com.android.server.pm.PreferredActivityHelper(this.mPm, this.mBroadcastHelper);
            if (userId == -1) {
                if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Slog.d("PackageManager", "Clear package:" + packageName + " state for all users");
                }
                this.mPm.mDomainVerificationManager.clearPackage(packageName);
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock2) {
                    try {
                        this.mPm.mSettings.getKeySetManagerService().removeAppKeySetDataLPw(packageName);
                        this.mPm.mInjector.getUpdateOwnershipHelper().removeUpdateOwnerDenyList(packageName);
                        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
                        this.mPm.mAppsFilter.removePackage(snapshot, snapshot.getPackageStateInternal(packageName));
                        final android.util.SparseBooleanArray changedUsers = new android.util.SparseBooleanArray();
                        this.mPm.clearPackagePreferredActivitiesLPw(packageName, changedUsers, -1);
                        this.mPm.mInjector.getBackgroundHandler().post(new java.lang.Runnable() { // from class: com.android.server.pm.RemovePackageHelper$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$clearPackageStateForUserLIF$0(changedUsers, preferredActivityHelper);
                            }
                        });
                    } finally {
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            } else {
                if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Slog.d("PackageManager", "Clear package:" + packageName + " state for user:" + userId);
                }
                this.mPm.mDomainVerificationManager.clearPackageForUser(packageName, userId);
                preferredActivityHelper.clearPackagePreferredActivities(packageName, userId);
                java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs = sus != null ? sus.getPackages() : java.util.Collections.emptyList();
                this.mPermissionManager.onPackageUninstalled(packageName, ps.getAppId(), ps, pkg, sharedUserPkgs, userId);
            }
            this.mPm.mInjector.getBackgroundHandler().post(new java.lang.Runnable() { // from class: com.android.server.pm.RemovePackageHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$clearPackageStateForUserLIF$1(ps, userId, packageName);
                }
            });
            return;
        }
        if ((flags & 16) != 0) {
            this.mAppDataHelper.clearAppDataLIF(resolvedPkg, userId, 7 | 16);
            this.mAppDataHelper.clearAppDataLIF(resolvedPkg, userId, 7 | 32);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPackageStateForUserLIF$0(android.util.SparseBooleanArray changedUsers, com.android.server.pm.PreferredActivityHelper preferredActivityHelper) {
        if (changedUsers.size() > 0) {
            preferredActivityHelper.updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), changedUsers);
            this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPackageStateForUserLIF$1(com.android.server.pm.PackageSetting ps, int userId, java.lang.String packageName) {
        try {
            android.os.Trace.traceBegin(262144L, "clearKeystoreData:" + ps.getAppId() + " for user: " + userId);
            if (!this.mPm.mPackageManagerServiceExt.skipRemoveKeyStoreInRPDLIF(packageName, ps.getAppId())) {
                this.mAppDataHelper.clearKeystoreData(userId, ps.getAppId());
            }
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    public void removePackageData(com.android.server.pm.PackageSetting deletedPs, int[] allUserHandles) {
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            removePackageDataLIF(deletedPs, -1, allUserHandles, new com.android.server.pm.PackageRemovedInfo(), 0, false);
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

    public void removePackageDataLIF(com.android.server.pm.PackageSetting deletedPs, int targetUserId, int[] allUserHandles, com.android.server.pm.PackageRemovedInfo outInfo, int flags, boolean writeSettings) throws java.lang.Throwable {
        boolean isArchive;
        long currentTimeMillis;
        boolean z;
        boolean installedStateChanged;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2;
        java.util.List<com.android.server.pm.pkg.AndroidPackage> packages;
        java.lang.String packageName = deletedPs.getPackageName();
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
            android.util.Slog.d("PackageManager", "removePackageDataLI: " + deletedPs);
        }
        boolean shouldDeletePackageSetting = shouldDeletePackageSetting(deletedPs, targetUserId, allUserHandles, flags);
        com.android.server.pm.pkg.AndroidPackage deletedPkg = deletedPs.getPkg();
        clearPackageStateForUserLIF(deletedPs, shouldDeletePackageSetting ? -1 : targetUserId, flags);
        removePackageLI(packageName, (Integer.MIN_VALUE & flags) != 0);
        if (!deletedPs.isSystem()) {
            deletedPs.setPkg(null);
        }
        if (shouldDeletePackageSetting) {
            new android.util.SparseBooleanArray();
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock3) {
                try {
                    try {
                        outInfo.mIsAppIdRemoved = this.mPm.mSettings.removePackageAndAppIdLPw(packageName);
                        if (this.mPm.mSettings.isDisabledSystemPackageLPr(packageName)) {
                            packageManagerTracedLock2 = packageManagerTracedLock3;
                        } else {
                            com.android.server.pm.SharedUserSetting sus = this.mPm.mSettings.getSharedUserSettingLPr(deletedPs);
                            if (sus == null) {
                                packages = java.util.Collections.emptyList();
                            } else {
                                try {
                                    packages = sus.getPackages();
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    packageManagerTracedLock = packageManagerTracedLock3;
                                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                    throw th;
                                }
                            }
                            java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs = packages;
                            packageManagerTracedLock2 = packageManagerTracedLock3;
                            this.mPermissionManager.onPackageUninstalled(packageName, deletedPs.getAppId(), deletedPs, deletedPkg, sharedUserPkgs, -1);
                            if (sus != null) {
                                this.mPm.mSettings.checkAndConvertSharedUserSettingsLPw(sus);
                            }
                        }
                        this.mPm.mSettings.removeRenamedPackageLPw(deletedPs.getRealName());
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    packageManagerTracedLock = packageManagerTracedLock3;
                }
            }
        } else {
            boolean z2 = false;
            if (!deletedPs.isSystem() && !outInfo.mIsUpdate && outInfo.mRemovedUsers != null && !deletedPs.isExternalStorage()) {
                if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Slog.d("PackageManager", "Updating installed state to false because of DELETE_KEEP_DATA");
                }
                boolean isArchive2 = (flags & 16) != 0;
                long currentTimeMillis2 = java.lang.System.currentTimeMillis();
                int[] iArr = outInfo.mRemovedUsers;
                int length = iArr.length;
                int i = 0;
                while (i < length) {
                    int userId = iArr[i];
                    if (!com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                        isArchive = isArchive2;
                        currentTimeMillis = currentTimeMillis2;
                        z = z2;
                    } else {
                        boolean wasInstalled = deletedPs.getInstalled(userId);
                        isArchive = isArchive2;
                        currentTimeMillis = currentTimeMillis2;
                        z = false;
                        android.util.Slog.d("PackageManager", "    user " + userId + ": " + wasInstalled + " => false");
                    }
                    deletedPs.setInstalled(z, userId);
                    i++;
                    isArchive2 = isArchive;
                    currentTimeMillis2 = currentTimeMillis;
                    z2 = false;
                }
            }
        }
        boolean installedStateChanged2 = false;
        if (outInfo.mOrigUsers != null && deletedPs.isSystem()) {
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                android.util.Slog.d("PackageManager", "Propagating install state across downgrade");
            }
            for (int userId2 : allUserHandles) {
                boolean installed = com.android.internal.util.ArrayUtils.contains(outInfo.mOrigUsers, userId2);
                if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Slog.d("PackageManager", "    user " + userId2 + " => " + installed);
                }
                if (installed != deletedPs.getInstalled(userId2)) {
                    installedStateChanged2 = true;
                }
                deletedPs.setInstalled(installed, userId2);
                if (installed) {
                    deletedPs.setUninstallReason(0, userId2);
                }
            }
            installedStateChanged = installedStateChanged2;
        } else {
            installedStateChanged = false;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock4 = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock4) {
            if (writeSettings) {
                try {
                    this.mPm.writeSettingsLPrTEMP();
                } catch (java.lang.Throwable th4) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th4;
                }
            }
            if (installedStateChanged) {
                this.mPm.mSettings.writeKernelMappingLPr(deletedPs);
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private static boolean shouldDeletePackageSetting(com.android.server.pm.PackageSetting deletedPs, int userId, int[] allUserHandles, int flags) {
        if ((flags & 1) != 0) {
            return false;
        }
        return userId == -1 || !deletedPs.hasDataOnAnyOtherUser(allUserHandles, userId);
    }

    void cleanUpResources(java.lang.String packageName, java.io.File codeFile, java.lang.String[] instructionSets) {
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            cleanUpResourcesLI(codeFile, instructionSets);
            if (installLock != null) {
                installLock.close();
            }
            if (packageName == null) {
                return;
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(packageName);
                    if (ps != null) {
                        ps.removeOldPath(codeFile);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        } catch (java.lang.Throwable th2) {
            if (installLock != null) {
                try {
                    installLock.close();
                } catch (java.lang.Throwable th3) {
                    th2.addSuppressed(th3);
                }
            }
            throw th2;
        }
    }

    private void cleanUpResourcesLI(java.io.File codeFile, java.lang.String[] instructionSets) {
        java.util.List list = java.util.Collections.EMPTY_LIST;
        if (codeFile != null && codeFile.exists()) {
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> result = android.content.pm.parsing.ApkLiteParseUtils.parsePackageLite(input.reset(), codeFile, 0);
            if (result.isSuccess()) {
                ((android.content.pm.parsing.PackageLite) result.getResult()).getAllApkPaths();
            }
        }
        removeCodePathLI(codeFile);
    }

    void cleanUpForMoveInstall(java.lang.String volumeUuid, java.lang.String packageName, java.lang.String fromCodePath) {
        int i;
        int i2;
        java.lang.String toPathName = new java.io.File(fromCodePath).getName();
        java.io.File codeFile = new java.io.File(android.os.Environment.getDataAppDirectory(volumeUuid), toPathName);
        android.util.Slog.d("PackageManager", "Cleaning up " + packageName + " on " + volumeUuid);
        int[] userIds = this.mPm.mUserManager.getUserIds();
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            int length = userIds.length;
            int i3 = 0;
            while (i3 < length) {
                int userId = userIds[i3];
                try {
                    i = length;
                    i2 = i3;
                    try {
                        this.mPm.mInstaller.destroyAppData(volumeUuid, packageName, userId, 131075, 0L);
                    } catch (com.android.server.pm.Installer.InstallerException e) {
                        e = e;
                        android.util.Slog.w("PackageManager", java.lang.String.valueOf(e));
                    }
                } catch (com.android.server.pm.Installer.InstallerException e2) {
                    e = e2;
                    i = length;
                    i2 = i3;
                }
                i3 = i2 + 1;
                length = i;
            }
            removeCodePathLI(codeFile);
            if (installLock != null) {
                installLock.close();
            }
        } catch (java.lang.Throwable th) {
            if (installLock == null) {
                throw th;
            }
            try {
                installLock.close();
                throw th;
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    public com.android.server.pm.IRemovePackageHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    private class RemovePackageHelplerWrapper implements com.android.server.pm.IRemovePackageHelperWrapper {
        private RemovePackageHelplerWrapper() {
        }

        @Override // com.android.server.pm.IRemovePackageHelperWrapper
        public void removeCodePathLI(java.io.File codePath) {
            com.android.server.pm.RemovePackageHelper.this.removeCodePathLI(codePath);
        }
    }
}
