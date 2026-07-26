package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class SharedLibrariesImpl implements com.android.server.pm.SharedLibrariesRead, com.android.server.utils.Watchable, com.android.server.utils.Snappable {
    public static boolean DEBUG_SHARED_LIBRARIES = false;
    private static final long ENFORCE_NATIVE_SHARED_LIBRARY_DEPENDENCIES = 142191088;
    private static final java.lang.String LIBRARY_TYPE_SDK = "sdk";
    private com.android.server.pm.DeletePackageHelper mDeletePackageHelper;
    private final com.android.server.pm.PackageManagerServiceInjector mInjector;
    private final com.android.server.utils.Watcher mObserver;
    private final com.android.server.pm.PackageManagerService mPm;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> mSharedLibraries;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>>> mSharedLibrariesSnapshot;
    private final com.android.server.utils.SnapshotCache<com.android.server.pm.SharedLibrariesImpl> mSnapshot;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> mStaticLibsByDeclaringPackage;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>>> mStaticLibsByDeclaringPackageSnapshot;
    private final com.android.server.utils.WatchableImpl mWatchable;

    private com.android.server.utils.SnapshotCache<com.android.server.pm.SharedLibrariesImpl> makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.SharedLibrariesImpl>(this, this) { // from class: com.android.server.pm.SharedLibrariesImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.SharedLibrariesImpl createSnapshot() {
                com.android.server.pm.SharedLibrariesImpl sharedLibrariesImpl = new com.android.server.pm.SharedLibrariesImpl();
                sharedLibrariesImpl.mWatchable.seal();
                return sharedLibrariesImpl;
            }
        };
    }

    SharedLibrariesImpl(com.android.server.pm.PackageManagerService pm, com.android.server.pm.PackageManagerServiceInjector injector) {
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.SharedLibrariesImpl.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.SharedLibrariesImpl.this.dispatchChange(what);
            }
        };
        this.mPm = pm;
        this.mInjector = injector;
        this.mSharedLibraries = new com.android.server.utils.WatchedArrayMap<>();
        this.mSharedLibrariesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mSharedLibraries, this.mSharedLibraries, "SharedLibrariesImpl.mSharedLibraries");
        this.mStaticLibsByDeclaringPackage = new com.android.server.utils.WatchedArrayMap<>();
        this.mStaticLibsByDeclaringPackageSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mStaticLibsByDeclaringPackage, this.mStaticLibsByDeclaringPackage, "SharedLibrariesImpl.mStaticLibsByDeclaringPackage");
        registerObservers();
        com.android.server.utils.Watchable.verifyWatchedAttributes(this, this.mObserver);
        this.mSnapshot = makeCache();
    }

    void setDeletePackageHelper(com.android.server.pm.DeletePackageHelper deletePackageHelper) {
        this.mDeletePackageHelper = deletePackageHelper;
    }

    private void registerObservers() {
        this.mSharedLibraries.registerObserver(this.mObserver);
        this.mStaticLibsByDeclaringPackage.registerObserver(this.mObserver);
    }

    private SharedLibrariesImpl(com.android.server.pm.SharedLibrariesImpl source) {
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.SharedLibrariesImpl.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.SharedLibrariesImpl.this.dispatchChange(what);
            }
        };
        this.mPm = source.mPm;
        this.mInjector = source.mInjector;
        this.mSharedLibraries = source.mSharedLibrariesSnapshot.snapshot();
        this.mSharedLibrariesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mStaticLibsByDeclaringPackage = source.mStaticLibsByDeclaringPackageSnapshot.snapshot();
        this.mStaticLibsByDeclaringPackageSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    @Override // com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.registerObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.unregisterObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(com.android.server.utils.Watcher observer) {
        return this.mWatchable.isRegisteredObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(com.android.server.utils.Watchable what) {
        this.mWatchable.dispatchChange(what);
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.SharedLibrariesRead snapshot() {
        return this.mSnapshot.snapshot();
    }

    @Override // com.android.server.pm.SharedLibrariesRead
    public com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> getAll() {
        return this.mSharedLibraries;
    }

    @Override // com.android.server.pm.SharedLibrariesRead
    public com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> getSharedLibraryInfos(java.lang.String libName) {
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> watchedLongSparseArray;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                watchedLongSparseArray = this.mSharedLibraries.get(libName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return watchedLongSparseArray;
    }

    public com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> getSharedLibraries() {
        return this.mSharedLibraries;
    }

    @Override // com.android.server.pm.SharedLibrariesRead
    public android.content.pm.SharedLibraryInfo getSharedLibraryInfo(java.lang.String libName, long version) {
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.get(libName);
        if (versionedLib == null) {
            return null;
        }
        return versionedLib.get(version);
    }

    @Override // com.android.server.pm.SharedLibrariesRead
    public com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> getStaticLibraryInfos(java.lang.String declaringPackageName) {
        return this.mStaticLibsByDeclaringPackage.get(declaringPackageName);
    }

    private com.android.server.pm.pkg.PackageStateInternal getLibraryPackage(com.android.server.pm.Computer computer, android.content.pm.SharedLibraryInfo libInfo) {
        android.content.pm.VersionedPackage declaringPackage = libInfo.getDeclaringPackage();
        if (libInfo.isStatic()) {
            java.lang.String internalPackageName = computer.resolveInternalPackageName(declaringPackage.getPackageName(), declaringPackage.getLongVersionCode());
            return computer.getPackageStateInternal(internalPackageName);
        }
        if (libInfo.isSdk()) {
            return computer.getPackageStateInternal(declaringPackage.getPackageName());
        }
        return null;
    }

    boolean pruneUnusedStaticSharedLibraries(com.android.server.pm.Computer computer, long neededSpace, long maxCachePeriod) throws java.io.IOException {
        android.os.storage.StorageManager storage;
        long now;
        android.os.storage.StorageManager storage2 = (android.os.storage.StorageManager) this.mInjector.getSystemService(android.os.storage.StorageManager.class);
        java.io.File volume = storage2.findPathForUuid(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL);
        java.util.ArrayList<android.content.pm.VersionedPackage> packagesToDelete = new java.util.ArrayList<>();
        long now2 = java.lang.System.currentTimeMillis();
        com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> sharedLibraries = computer.getSharedLibraries();
        int libCount = sharedLibraries.size();
        int i = 0;
        while (i < libCount) {
            com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = sharedLibraries.valueAt(i);
            if (versionedLib != null) {
                int versionCount = versionedLib.size();
                int j = 0;
                while (j < versionCount) {
                    android.content.pm.SharedLibraryInfo libInfo = versionedLib.valueAt(j);
                    com.android.server.pm.pkg.PackageStateInternal ps = getLibraryPackage(computer, libInfo);
                    if (ps == null) {
                        storage = storage2;
                        now = now2;
                    } else if (now2 - ps.getLastUpdateTime() < maxCachePeriod) {
                        storage = storage2;
                        now = now2;
                    } else if (ps.isSystem()) {
                        storage = storage2;
                        now = now2;
                    } else {
                        storage = storage2;
                        java.lang.String packageName = ps.getPkg().getPackageName();
                        now = now2;
                        long now3 = libInfo.getDeclaringPackage().getLongVersionCode();
                        packagesToDelete.add(new android.content.pm.VersionedPackage(packageName, now3));
                    }
                    j++;
                    storage2 = storage;
                    now2 = now;
                }
            }
            i++;
            storage2 = storage2;
            now2 = now2;
        }
        int packageCount = packagesToDelete.size();
        for (int i2 = 0; i2 < packageCount; i2++) {
            android.content.pm.VersionedPackage pkgToDelete = packagesToDelete.get(i2);
            if (this.mDeletePackageHelper.deletePackageX(pkgToDelete.getPackageName(), pkgToDelete.getLongVersionCode(), 0, 2, true) == 1 && volume.getUsableSpace() >= neededSpace) {
                return true;
            }
        }
        return false;
    }

    android.content.pm.SharedLibraryInfo getLatestStaticSharedLibraVersion(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.content.pm.SharedLibraryInfo latestStaticSharedLibraVersionLPr;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                latestStaticSharedLibraVersionLPr = getLatestStaticSharedLibraVersionLPr(pkg);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return latestStaticSharedLibraVersionLPr;
    }

    private android.content.pm.SharedLibraryInfo getLatestStaticSharedLibraVersionLPr(com.android.server.pm.pkg.AndroidPackage pkg) {
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.get(pkg.getStaticSharedLibraryName());
        if (versionedLib == null) {
            return null;
        }
        long previousLibVersion = -1;
        int versionCount = versionedLib.size();
        for (int i = 0; i < versionCount; i++) {
            long libVersion = versionedLib.keyAt(i);
            if (libVersion < pkg.getStaticSharedLibraryVersion()) {
                previousLibVersion = java.lang.Math.max(previousLibVersion, libVersion);
            }
        }
        if (previousLibVersion < 0) {
            return null;
        }
        return versionedLib.get(previousLibVersion);
    }

    com.android.server.pm.PackageSetting getStaticSharedLibLatestVersionSetting(com.android.server.pm.InstallRequest installRequest) {
        if (installRequest.getParsedPackage() == null) {
            return null;
        }
        com.android.server.pm.PackageSetting sharedLibPackage = null;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                android.content.pm.SharedLibraryInfo latestSharedLibraVersionLPr = getLatestStaticSharedLibraVersionLPr(installRequest.getParsedPackage());
                if (latestSharedLibraVersionLPr != null) {
                    sharedLibPackage = this.mPm.mSettings.getPackageLPr(latestSharedLibraVersionLPr.getPackageName());
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return sharedLibPackage;
    }

    private void applyDefiningSharedLibraryUpdateLPr(com.android.server.pm.pkg.AndroidPackage pkg, android.content.pm.SharedLibraryInfo libInfo, java.util.function.BiConsumer<android.content.pm.SharedLibraryInfo, android.content.pm.SharedLibraryInfo> action) {
        if (com.android.server.pm.parsing.pkg.AndroidPackageUtils.isLibrary(pkg)) {
            if (pkg.getSdkLibraryName() != null) {
                android.content.pm.SharedLibraryInfo definedLibrary = getSharedLibraryInfo(pkg.getSdkLibraryName(), pkg.getSdkLibVersionMajor());
                if (definedLibrary != null) {
                    action.accept(definedLibrary, libInfo);
                    return;
                }
                return;
            }
            if (pkg.getStaticSharedLibraryName() != null) {
                android.content.pm.SharedLibraryInfo definedLibrary2 = getSharedLibraryInfo(pkg.getStaticSharedLibraryName(), pkg.getStaticSharedLibraryVersion());
                if (definedLibrary2 != null) {
                    action.accept(definedLibrary2, libInfo);
                    return;
                }
                return;
            }
            for (java.lang.String libraryName : pkg.getLibraryNames()) {
                android.content.pm.SharedLibraryInfo definedLibrary3 = getSharedLibraryInfo(libraryName, -1L);
                if (definedLibrary3 != null) {
                    action.accept(definedLibrary3, libInfo);
                }
            }
        }
    }

    private void addSharedLibraryLPr(com.android.server.pm.pkg.AndroidPackage pkg, java.util.Set<java.lang.String> usesLibraryFiles, android.content.pm.SharedLibraryInfo libInfo, com.android.server.pm.pkg.AndroidPackage changingLib, com.android.server.pm.PackageSetting changingLibSetting) {
        if (libInfo.getPath() != null) {
            usesLibraryFiles.add(libInfo.getPath());
            return;
        }
        com.android.server.pm.pkg.AndroidPackage pkgForCodePaths = this.mPm.mPackages.get(libInfo.getPackageName());
        com.android.server.pm.PackageSetting pkgSetting = this.mPm.mSettings.getPackageLPr(libInfo.getPackageName());
        if (changingLib != null && changingLib.getPackageName().equals(libInfo.getPackageName()) && (pkgForCodePaths == null || pkgForCodePaths.getPackageName().equals(changingLib.getPackageName()))) {
            pkgForCodePaths = changingLib;
            pkgSetting = changingLibSetting;
        }
        if (pkgForCodePaths != null) {
            usesLibraryFiles.addAll(com.android.server.pm.parsing.pkg.AndroidPackageUtils.getAllCodePaths(pkgForCodePaths));
            applyDefiningSharedLibraryUpdateLPr(pkg, libInfo, new java.util.function.BiConsumer() { // from class: com.android.server.pm.SharedLibrariesImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((android.content.pm.SharedLibraryInfo) obj).addDependency((android.content.pm.SharedLibraryInfo) obj2);
                }
            });
            if (pkgSetting != null) {
                usesLibraryFiles.addAll(pkgSetting.getPkgState().getUsesLibraryFiles());
            }
        }
    }

    void updateSharedLibraries(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.pkg.AndroidPackage changingLib, com.android.server.pm.PackageSetting changingLibSetting, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> availablePackages) throws java.lang.Throwable {
        java.util.ArrayList<android.content.pm.SharedLibraryInfo> sharedLibraryInfos = collectSharedLibraryInfos(pkg, availablePackages, null);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                executeSharedLibrariesUpdateLPw(pkg, pkgSetting, changingLib, changingLibSetting, sharedLibraryInfos, this.mPm.mUserManager.getUserIds());
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    void executeSharedLibrariesUpdate(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.pkg.AndroidPackage changingLib, com.android.server.pm.PackageSetting changingLibSetting, java.util.ArrayList<android.content.pm.SharedLibraryInfo> usesLibraryInfos, int[] allUsers) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                executeSharedLibrariesUpdateLPw(pkg, pkgSetting, changingLib, changingLibSetting, usesLibraryInfos, allUsers);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void executeSharedLibrariesUpdateLPw(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.pkg.AndroidPackage changingLib, com.android.server.pm.PackageSetting changingLibSetting, java.util.ArrayList<android.content.pm.SharedLibraryInfo> usesLibraryInfos, int[] allUsers) {
        applyDefiningSharedLibraryUpdateLPr(pkg, null, new java.util.function.BiConsumer() { // from class: com.android.server.pm.SharedLibrariesImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((android.content.pm.SharedLibraryInfo) obj).clearDependencies();
            }
        });
        if (usesLibraryInfos != null) {
            pkgSetting.getPkgState().setUsesLibraryInfos(usesLibraryInfos);
            java.util.Set<java.lang.String> usesLibraryFiles = new java.util.LinkedHashSet<>();
            for (android.content.pm.SharedLibraryInfo libInfo : usesLibraryInfos) {
                addSharedLibraryLPr(pkg, usesLibraryFiles, libInfo, changingLib, changingLibSetting);
            }
            pkgSetting.setPkgStateLibraryFiles(usesLibraryFiles);
            int[] installedUsers = new int[allUsers.length];
            int installedUserCount = 0;
            for (int u = 0; u < allUsers.length; u++) {
                if (pkgSetting.getInstalled(allUsers[u])) {
                    installedUsers[installedUserCount] = allUsers[u];
                    installedUserCount++;
                }
            }
            for (android.content.pm.SharedLibraryInfo sharedLibraryInfo : usesLibraryInfos) {
                if (sharedLibraryInfo.isStatic()) {
                    com.android.server.pm.PackageSetting staticLibPkgSetting = this.mPm.getPackageSettingForMutation(sharedLibraryInfo.getPackageName());
                    if (staticLibPkgSetting == null) {
                        android.util.Slog.wtf("PackageManager", "Shared lib without setting: " + sharedLibraryInfo);
                    } else {
                        for (int u2 = 0; u2 < installedUserCount; u2++) {
                            staticLibPkgSetting.setInstalled(true, installedUsers[u2]);
                        }
                    }
                }
            }
            return;
        }
        pkgSetting.getPkgState().setUsesLibraryInfos(java.util.Collections.emptyList()).setUsesLibraryFiles(java.util.Collections.emptyList());
    }

    private static boolean hasString(java.util.List<java.lang.String> list, java.util.List<java.lang.String> which) {
        if (list == null || which == null) {
            return false;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            for (int j = which.size() - 1; j >= 0; j--) {
                if (which.get(j).equals(list.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> commitSharedLibraryChanges(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting, java.util.List<android.content.pm.SharedLibraryInfo> allowedSharedLibraryInfos, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> combinedSigningDetails, int scanFlags) {
        if (com.android.internal.util.ArrayUtils.isEmpty(allowedSharedLibraryInfos)) {
            return null;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (android.content.pm.SharedLibraryInfo info : allowedSharedLibraryInfos) {
                    commitSharedLibraryInfoLPw(info);
                }
                try {
                    updateSharedLibraries(pkg, pkgSetting, null, null, combinedSigningDetails);
                } catch (com.android.server.pm.PackageManagerException e) {
                    android.util.Slog.e("PackageManager", "updateSharedLibraries failed: ", e);
                }
                if ((scanFlags & 16) != 0) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> arrayListUpdateAllSharedLibrariesLPw = updateAllSharedLibrariesLPw(pkg, pkgSetting, combinedSigningDetails);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return arrayListUpdateAllSharedLibrariesLPw;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> updateAllSharedLibrariesLPw(com.android.server.pm.pkg.AndroidPackage updatedPkg, com.android.server.pm.PackageSetting updatedPkgSetting, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> availablePackages) {
        java.util.List<android.util.Pair<com.android.server.pm.pkg.AndroidPackage, com.android.server.pm.PackageSetting>> needsUpdating;
        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> resultList;
        android.util.ArraySet<java.lang.String> descendants;
        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> resultList2 = null;
        android.util.ArraySet<java.lang.String> descendants2 = null;
        if (updatedPkg != null && updatedPkgSetting != null) {
            java.util.List<android.util.Pair<com.android.server.pm.pkg.AndroidPackage, com.android.server.pm.PackageSetting>> needsUpdating2 = new java.util.ArrayList<>(1);
            needsUpdating2.add(android.util.Pair.create(updatedPkg, updatedPkgSetting));
            needsUpdating = needsUpdating2;
        } else {
            needsUpdating = null;
        }
        do {
            android.util.Pair<com.android.server.pm.pkg.AndroidPackage, com.android.server.pm.PackageSetting> changingPkgPair = needsUpdating == null ? null : needsUpdating.remove(0);
            com.android.server.pm.pkg.AndroidPackage changingPkg = changingPkgPair != null ? (com.android.server.pm.pkg.AndroidPackage) changingPkgPair.first : null;
            com.android.server.pm.PackageSetting changingPkgSetting = changingPkgPair != null ? (com.android.server.pm.PackageSetting) changingPkgPair.second : null;
            for (int i = this.mPm.mPackages.size() - 1; i >= 0; i--) {
                com.android.server.pm.pkg.AndroidPackage pkg = this.mPm.mPackages.valueAt(i);
                com.android.server.pm.PackageSetting pkgSetting = this.mPm.mSettings.getPackageLPr(pkg.getPackageName());
                if (changingPkg == null || hasString(pkg.getUsesLibraries(), changingPkg.getLibraryNames()) || hasString(pkg.getUsesOptionalLibraries(), changingPkg.getLibraryNames()) || com.android.internal.util.ArrayUtils.contains(pkg.getUsesStaticLibraries(), changingPkg.getStaticSharedLibraryName()) || com.android.internal.util.ArrayUtils.contains(pkg.getUsesSdkLibraries(), changingPkg.getSdkLibraryName())) {
                    if (resultList2 != null) {
                        resultList = resultList2;
                    } else {
                        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> resultList3 = new java.util.ArrayList<>();
                        resultList = resultList3;
                    }
                    resultList.add(pkg);
                    if (changingPkg == null) {
                        descendants = descendants2;
                    } else {
                        if (descendants2 == null) {
                            descendants2 = new android.util.ArraySet<>();
                        }
                        if (!descendants2.contains(pkg.getPackageName())) {
                            descendants2.add(pkg.getPackageName());
                            needsUpdating.add(android.util.Pair.create(pkg, pkgSetting));
                        }
                        descendants = descendants2;
                    }
                    java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> resultList4 = resultList;
                    try {
                        updateSharedLibraries(pkg, pkgSetting, changingPkg, changingPkgSetting, availablePackages);
                    } catch (com.android.server.pm.PackageManagerException e) {
                        if (!pkgSetting.isSystem() || pkgSetting.isUpdatedSystemApp()) {
                            int flags = pkgSetting.isUpdatedSystemApp() ? 1 : 0;
                            com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                            try {
                                this.mDeletePackageHelper.deletePackageLIF(pkg.getPackageName(), null, true, this.mPm.mUserManager.getUserIds(), flags, new com.android.server.pm.PackageRemovedInfo(), true);
                                if (installLock != null) {
                                    installLock.close();
                                }
                            } finally {
                            }
                        }
                        android.util.Slog.e("PackageManager", "updateAllSharedLibrariesLPw failed: " + e.getMessage());
                    }
                    descendants2 = descendants;
                    resultList2 = resultList4;
                }
            }
            if (needsUpdating == null) {
                break;
            }
        } while (needsUpdating.size() > 0);
        return resultList2;
    }

    void addBuiltInSharedLibraryLPw(com.android.server.SystemConfig.SharedLibraryEntry entry) {
        if (getSharedLibraryInfo(entry.name, -1L) != null) {
            return;
        }
        android.content.pm.SharedLibraryInfo libraryInfo = new android.content.pm.SharedLibraryInfo(entry.filename, null, null, entry.name, -1L, 0, new android.content.pm.VersionedPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0L), null, null, entry.isNative);
        commitSharedLibraryInfoLPw(libraryInfo);
    }

    void commitSharedLibraryInfoLPw(android.content.pm.SharedLibraryInfo libraryInfo) {
        java.lang.String name = libraryInfo.getName();
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.get(name);
        if (versionedLib == null) {
            versionedLib = new com.android.server.utils.WatchedLongSparseArray<>();
            this.mSharedLibraries.put(name, versionedLib);
        }
        java.lang.String declaringPackageName = libraryInfo.getDeclaringPackage().getPackageName();
        if (libraryInfo.getType() == 2) {
            this.mStaticLibsByDeclaringPackage.put(declaringPackageName, versionedLib);
        }
        versionedLib.put(libraryInfo.getLongVersion(), libraryInfo);
    }

    boolean removeSharedLibrary(java.lang.String libName, long version) {
        int libIdx;
        int[] iArr;
        com.android.server.pm.Computer snapshot;
        com.android.server.pm.Computer snapshot2;
        android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> usingSharedLibraryPair;
        int currentUserId;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.get(libName);
                int i = 0;
                if (versionedLib == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return false;
                }
                int libIdx2 = versionedLib.indexOfKey(version);
                if (libIdx2 < 0) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return false;
                }
                android.content.pm.SharedLibraryInfo libraryInfo = versionedLib.valueAt(libIdx2);
                com.android.server.pm.Computer snapshot3 = this.mPm.snapshotComputer();
                int[] userIds = this.mPm.mUserManager.getUserIds();
                int length = userIds.length;
                while (i < length) {
                    int currentUserId2 = userIds[i];
                    int currentUserId3 = currentUserId2;
                    android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> usingSharedLibraryPair2 = snapshot3.getPackagesUsingSharedLibrary(libraryInfo, 0L, 1000, currentUserId2);
                    java.util.List<android.content.pm.VersionedPackage> dependents = (java.util.List) usingSharedLibraryPair2.first;
                    if (dependents == null) {
                        libIdx = libIdx2;
                        iArr = userIds;
                        snapshot = snapshot3;
                    } else {
                        for (android.content.pm.VersionedPackage dependentPackage : dependents) {
                            int libIdx3 = libIdx2;
                            int[] iArr2 = userIds;
                            com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(dependentPackage.getPackageName());
                            if (ps == null) {
                                snapshot2 = snapshot3;
                                usingSharedLibraryPair = usingSharedLibraryPair2;
                                currentUserId = currentUserId3;
                            } else {
                                snapshot2 = snapshot3;
                                usingSharedLibraryPair = usingSharedLibraryPair2;
                                currentUserId = currentUserId3;
                                ps.setOverlayPathsForLibrary(libraryInfo.getName(), null, currentUserId);
                            }
                            currentUserId3 = currentUserId;
                            libIdx2 = libIdx3;
                            userIds = iArr2;
                            snapshot3 = snapshot2;
                            usingSharedLibraryPair2 = usingSharedLibraryPair;
                        }
                        libIdx = libIdx2;
                        iArr = userIds;
                        snapshot = snapshot3;
                    }
                    i++;
                    libIdx2 = libIdx;
                    userIds = iArr;
                    snapshot3 = snapshot;
                }
                versionedLib.remove(version);
                if (versionedLib.size() <= 0) {
                    this.mSharedLibraries.remove(libName);
                    if (libraryInfo.getType() == 2) {
                        this.mStaticLibsByDeclaringPackage.remove(libraryInfo.getDeclaringPackage().getPackageName());
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return true;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    java.util.List<android.content.pm.SharedLibraryInfo> getAllowedSharedLibInfos(com.android.server.pm.InstallRequest installRequest) {
        com.android.server.pm.PackageSetting updatedSystemPs;
        com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage = installRequest.getParsedPackage();
        if (installRequest.getSdkSharedLibraryInfo() == null && installRequest.getStaticSharedLibraryInfo() == null && installRequest.getDynamicSharedLibraryInfos() == null) {
            return null;
        }
        if (installRequest.getSdkSharedLibraryInfo() != null) {
            return java.util.Collections.singletonList(installRequest.getSdkSharedLibraryInfo());
        }
        if (installRequest.getStaticSharedLibraryInfo() != null) {
            return java.util.Collections.singletonList(installRequest.getStaticSharedLibraryInfo());
        }
        boolean isSystemApp = installRequest.getScannedPackageSetting() != null && installRequest.getScannedPackageSetting().isSystem();
        boolean hasDynamicLibraries = (parsedPackage == null || !isSystemApp || installRequest.getDynamicSharedLibraryInfos() == null) ? false : true;
        if (!hasDynamicLibraries) {
            return null;
        }
        boolean isUpdatedSystemApp = installRequest.getScannedPackageSetting() != null && installRequest.getScannedPackageSetting().isUpdatedSystemApp();
        if (isUpdatedSystemApp) {
            if (installRequest.getScanRequestDisabledPackageSetting() == null) {
                updatedSystemPs = installRequest.getScanRequestOldPackageSetting();
            } else {
                updatedSystemPs = installRequest.getScanRequestDisabledPackageSetting();
            }
        } else {
            updatedSystemPs = null;
        }
        if (isUpdatedSystemApp && (updatedSystemPs.getPkg() == null || updatedSystemPs.getPkg().getLibraryNames() == null)) {
            android.util.Slog.w("PackageManager", "Package " + parsedPackage.getPackageName() + " declares libraries that are not declared on the system image; skipping");
            return null;
        }
        java.util.ArrayList<android.content.pm.SharedLibraryInfo> infos = new java.util.ArrayList<>(installRequest.getDynamicSharedLibraryInfos().size());
        for (android.content.pm.SharedLibraryInfo info : installRequest.getDynamicSharedLibraryInfos()) {
            java.lang.String name = info.getName();
            if (isUpdatedSystemApp && !updatedSystemPs.getPkg().getLibraryNames().contains(name)) {
                android.util.Slog.w("PackageManager", "Package " + parsedPackage.getPackageName() + " declares library " + name + " that is not declared on system image; skipping");
            } else {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        if (getSharedLibraryInfo(name, -1L) != null) {
                            android.util.Slog.w("PackageManager", "Package " + parsedPackage.getPackageName() + " declares library " + name + " that already exists; skipping");
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } else {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            infos.add(info);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
            }
        }
        return infos;
    }

    java.util.ArrayList<android.content.pm.SharedLibraryInfo> collectSharedLibraryInfos(com.android.server.pm.pkg.AndroidPackage pkg, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> availablePackages, java.util.Map<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> newLibraries) throws java.lang.Throwable {
        if (pkg == null) {
            return null;
        }
        com.android.server.compat.PlatformCompat platformCompat = this.mInjector.getCompatibility();
        java.util.ArrayList<android.content.pm.SharedLibraryInfo> usesLibraryInfos = null;
        if (!pkg.getUsesLibraries().isEmpty()) {
            usesLibraryInfos = collectSharedLibraryInfos(pkg.getUsesLibraries(), null, null, null, pkg.getPackageName(), "shared", true, pkg.getTargetSdkVersion(), null, availablePackages, newLibraries);
        }
        if (!pkg.getUsesStaticLibraries().isEmpty()) {
            usesLibraryInfos = collectSharedLibraryInfos(pkg.getUsesStaticLibraries(), pkg.getUsesStaticLibrariesVersions(), pkg.getUsesStaticLibrariesCertDigests(), null, pkg.getPackageName(), "static shared", true, pkg.getTargetSdkVersion(), usesLibraryInfos, availablePackages, newLibraries);
        }
        if (!pkg.getUsesOptionalLibraries().isEmpty()) {
            usesLibraryInfos = collectSharedLibraryInfos(pkg.getUsesOptionalLibraries(), null, null, null, pkg.getPackageName(), "shared", false, pkg.getTargetSdkVersion(), usesLibraryInfos, availablePackages, newLibraries);
        }
        if (platformCompat.isChangeEnabledInternal(ENFORCE_NATIVE_SHARED_LIBRARY_DEPENDENCIES, pkg.getPackageName(), pkg.getTargetSdkVersion())) {
            if (!pkg.getUsesNativeLibraries().isEmpty()) {
                usesLibraryInfos = collectSharedLibraryInfos(pkg.getUsesNativeLibraries(), null, null, null, pkg.getPackageName(), "native shared", true, pkg.getTargetSdkVersion(), usesLibraryInfos, availablePackages, newLibraries);
            }
            if (!pkg.getUsesOptionalNativeLibraries().isEmpty()) {
                usesLibraryInfos = collectSharedLibraryInfos(pkg.getUsesOptionalNativeLibraries(), null, null, null, pkg.getPackageName(), "native shared", false, pkg.getTargetSdkVersion(), usesLibraryInfos, availablePackages, newLibraries);
            }
        }
        if (!pkg.getUsesSdkLibraries().isEmpty()) {
            boolean required = !com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.sdkLibIndependence();
            return collectSharedLibraryInfos(pkg.getUsesSdkLibraries(), pkg.getUsesSdkLibrariesVersionsMajor(), pkg.getUsesSdkLibrariesCertDigests(), pkg.getUsesSdkLibrariesOptional(), pkg.getPackageName(), LIBRARY_TYPE_SDK, required, pkg.getTargetSdkVersion(), usesLibraryInfos, availablePackages, newLibraries);
        }
        return usesLibraryInfos;
    }

    private java.util.ArrayList<android.content.pm.SharedLibraryInfo> collectSharedLibraryInfos(java.util.List<java.lang.String> requestedLibraries, long[] requiredVersions, java.lang.String[][] requiredCertDigests, boolean[] libsOptional, java.lang.String packageName, java.lang.String libraryType, boolean required, int targetSdk, java.util.ArrayList<android.content.pm.SharedLibraryInfo> outUsedLibraries, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> availablePackages, java.util.Map<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> newLibraries) throws java.lang.Throwable {
        int libCount;
        java.lang.String[] libCertDigests;
        com.android.server.pm.SharedLibrariesImpl sharedLibrariesImpl = this;
        java.lang.String str = packageName;
        java.lang.String str2 = libraryType;
        int libCount2 = requestedLibraries.size();
        java.util.ArrayList<android.content.pm.SharedLibraryInfo> outUsedLibraries2 = outUsedLibraries;
        int i = 0;
        while (i < libCount2) {
            java.lang.String libName = requestedLibraries.get(i);
            long libVersion = requiredVersions != null ? requiredVersions[i] : -1L;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = sharedLibrariesImpl.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                try {
                    android.content.pm.SharedLibraryInfo libraryInfo = com.android.server.pm.SharedLibraryUtils.getSharedLibraryInfo(libName, libVersion, sharedLibrariesImpl.mSharedLibraries, newLibraries);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    if (libraryInfo == null) {
                        if (required || (LIBRARY_TYPE_SDK.equals(str2) && libsOptional != null && !libsOptional[i])) {
                            throw new com.android.server.pm.PackageManagerException(-9, "Package " + str + " requires unavailable " + str2 + " library " + libName + "; failing!");
                        }
                        if (DEBUG_SHARED_LIBRARIES) {
                            android.util.Slog.i("PackageManager", "Package " + str + " desires unavailable " + str2 + " library " + libName + "; ignoring!");
                        }
                        libCount = libCount2;
                    } else {
                        if (requiredVersions == null || requiredCertDigests == null) {
                            libCount = libCount2;
                        } else {
                            if (libraryInfo.getLongVersion() != requiredVersions[i]) {
                                throw new com.android.server.pm.PackageManagerException(-9, "Package " + str + " requires unavailable " + str2 + " library " + libName + " version " + libraryInfo.getLongVersion() + "; failing!");
                            }
                            com.android.server.pm.pkg.AndroidPackage pkg = availablePackages.get(libraryInfo.getPackageName());
                            android.content.pm.SigningDetails libPkg = pkg == null ? null : pkg.getSigningDetails();
                            if (libPkg == null) {
                                throw new com.android.server.pm.PackageManagerException(-9, "Package " + str + " requires unavailable " + str2 + " library; failing!");
                            }
                            java.lang.String[] expectedCertDigests = requiredCertDigests[i];
                            libCount = libCount2;
                            if (expectedCertDigests.length > 1) {
                                if (targetSdk >= 27) {
                                    libCertDigests = android.util.PackageUtils.computeSignaturesSha256Digests(libPkg.getSignatures());
                                } else {
                                    libCertDigests = android.util.PackageUtils.computeSignaturesSha256Digests(new android.content.pm.Signature[]{libPkg.getSignatures()[0]});
                                }
                                if (expectedCertDigests.length != libCertDigests.length) {
                                    throw new com.android.server.pm.PackageManagerException(-9, "Package " + str + " requires differently signed " + str2 + " library; failing!");
                                }
                                java.util.Arrays.sort(libCertDigests);
                                java.util.Arrays.sort(expectedCertDigests);
                                int certCount = libCertDigests.length;
                                int j = 0;
                                while (j < certCount) {
                                    int certCount2 = certCount;
                                    java.lang.String[] libCertDigests2 = libCertDigests;
                                    if (libCertDigests[j].equalsIgnoreCase(expectedCertDigests[j])) {
                                        j++;
                                        certCount = certCount2;
                                        libCertDigests = libCertDigests2;
                                    } else {
                                        throw new com.android.server.pm.PackageManagerException(-9, "Package " + str + " requires differently signed " + str2 + " library; failing!");
                                    }
                                }
                            } else {
                                try {
                                    byte[] digestBytes = libcore.util.HexEncoding.decode(expectedCertDigests[0], false);
                                    if (!libPkg.hasSha256Certificate(digestBytes)) {
                                        throw new com.android.server.pm.PackageManagerException(-9, "Package " + str + " requires differently signed " + str2 + " library; failing!");
                                    }
                                } catch (java.lang.IllegalArgumentException e) {
                                    throw new com.android.server.pm.PackageManagerException(-130, "Package " + str + " declares bad certificate digest for " + str2 + " library " + libName + "; failing!");
                                }
                            }
                        }
                        if (outUsedLibraries2 == null) {
                            outUsedLibraries2 = new java.util.ArrayList<>();
                        }
                        outUsedLibraries2.add(libraryInfo);
                    }
                    i++;
                    sharedLibrariesImpl = this;
                    str = packageName;
                    str2 = libraryType;
                    libCount2 = libCount;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    while (true) {
                        try {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                }
            }
        }
        return outUsedLibraries2;
    }

    @Override // com.android.server.pm.SharedLibrariesRead
    public void dump(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
        boolean checkin = dumpState.isCheckIn();
        boolean printedHeader = false;
        int numSharedLibraries = this.mSharedLibraries.size();
        for (int index = 0; index < numSharedLibraries; index++) {
            java.lang.String libName = this.mSharedLibraries.keyAt(index);
            com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.get(libName);
            if (versionedLib != null) {
                int versionCount = versionedLib.size();
                for (int i = 0; i < versionCount; i++) {
                    android.content.pm.SharedLibraryInfo libraryInfo = versionedLib.valueAt(i);
                    if (!checkin) {
                        if (!printedHeader) {
                            if (dumpState.onTitlePrinted()) {
                                pw.println();
                            }
                            pw.println("Libraries:");
                            printedHeader = true;
                        }
                        pw.print("  ");
                    } else {
                        pw.print("lib,");
                    }
                    pw.print(libraryInfo.getName());
                    if (libraryInfo.isStatic()) {
                        pw.print(" version=" + libraryInfo.getLongVersion());
                    }
                    if (!checkin) {
                        pw.print(" -> ");
                    }
                    if (libraryInfo.getPath() != null) {
                        if (libraryInfo.isNative()) {
                            pw.print(" (so) ");
                        } else {
                            pw.print(" (jar) ");
                        }
                        pw.print(libraryInfo.getPath());
                    } else {
                        pw.print(" (apk) ");
                        pw.print(libraryInfo.getPackageName());
                    }
                    pw.println();
                }
            }
        }
    }

    @Override // com.android.server.pm.SharedLibrariesRead
    public void dumpProto(android.util.proto.ProtoOutputStream proto) {
        int count = this.mSharedLibraries.size();
        for (int i = 0; i < count; i++) {
            java.lang.String libName = this.mSharedLibraries.keyAt(i);
            com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = this.mSharedLibraries.get(libName);
            if (versionedLib != null) {
                int versionCount = versionedLib.size();
                for (int j = 0; j < versionCount; j++) {
                    android.content.pm.SharedLibraryInfo libraryInfo = versionedLib.valueAt(j);
                    long sharedLibraryToken = proto.start(2246267895811L);
                    proto.write(1138166333441L, libraryInfo.getName());
                    boolean isJar = libraryInfo.getPath() != null;
                    proto.write(1133871366146L, isJar);
                    if (isJar) {
                        proto.write(1138166333443L, libraryInfo.getPath());
                    } else {
                        proto.write(1138166333444L, libraryInfo.getPackageName());
                    }
                    proto.end(sharedLibraryToken);
                }
            }
        }
    }
}
