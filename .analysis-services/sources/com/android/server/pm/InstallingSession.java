package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class InstallingSession {
    final java.util.List<java.lang.String> mAllowlistedRestrictedPermissions;
    final boolean mApplicationEnabledSettingPersistent;
    final int mAutoRevokePermissionsMode;
    final int mDataLoaderType;
    final int mDevelopmentInstallFlags;
    final java.lang.String mDexoptCompilerFilter;
    final boolean mForceQueryableOverride;
    final boolean mHasAppMetadataFile;
    int mInstallFlags;
    com.android.server.pm.IInstallParamsExt mInstallParamsExt;
    final int mInstallReason;
    final int mInstallScenario;
    final com.android.server.pm.InstallSource mInstallSource;
    final boolean mIsInherit;
    final com.android.server.pm.MoveInfo mMoveInfo;
    final android.content.pm.IPackageInstallObserver2 mObserver;
    final com.android.server.pm.OriginInfo mOriginInfo;
    final java.lang.String mPackageAbiOverride;
    final android.content.pm.parsing.PackageLite mPackageLite;
    final int mPackageSource;
    com.android.server.pm.InstallingSession.MultiPackageInstallingSession mParentInstallingSession;
    final android.util.ArrayMap<java.lang.String, java.lang.Integer> mPermissionStates;
    final com.android.server.pm.PackageManagerService mPm;
    final android.content.pm.verify.domain.DomainSet mPreVerifiedDomains;
    final int mRequireUserAction;
    final long mRequiredInstalledVersionCode;
    int mRet;
    final int mSessionId;
    final android.content.pm.SigningDetails mSigningDetails;
    int mTraceCookie;
    java.lang.String mTraceMethod;
    private final android.os.UserHandle mUser;
    final java.lang.String mVolumeUuid;

    InstallingSession(com.android.server.pm.OriginInfo originInfo, com.android.server.pm.MoveInfo moveInfo, android.content.pm.IPackageInstallObserver2 observer, int installFlags, int developmentInstallFlags, com.android.server.pm.InstallSource installSource, java.lang.String volumeUuid, android.os.UserHandle user, java.lang.String packageAbiOverride, int packageSource, android.content.pm.parsing.PackageLite packageLite, com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
        this.mUser = user;
        this.mOriginInfo = originInfo;
        this.mMoveInfo = moveInfo;
        this.mObserver = observer;
        this.mInstallFlags = installFlags;
        this.mDevelopmentInstallFlags = developmentInstallFlags;
        this.mInstallSource = (com.android.server.pm.InstallSource) com.android.internal.util.Preconditions.checkNotNull(installSource);
        this.mVolumeUuid = volumeUuid;
        this.mPackageAbiOverride = packageAbiOverride;
        this.mPermissionStates = new android.util.ArrayMap<>();
        this.mAllowlistedRestrictedPermissions = null;
        this.mAutoRevokePermissionsMode = 3;
        this.mSigningDetails = android.content.pm.SigningDetails.UNKNOWN;
        this.mInstallReason = 0;
        this.mInstallScenario = 0;
        this.mForceQueryableOverride = false;
        this.mDataLoaderType = 0;
        this.mRequiredInstalledVersionCode = -1L;
        this.mPackageSource = packageSource;
        this.mPackageLite = packageLite;
        this.mIsInherit = false;
        this.mSessionId = -1;
        this.mRequireUserAction = 0;
        this.mApplicationEnabledSettingPersistent = false;
        this.mPreVerifiedDomains = null;
        this.mHasAppMetadataFile = false;
        this.mDexoptCompilerFilter = null;
    }

    InstallingSession(int sessionId, java.io.File stagedDir, android.content.pm.IPackageInstallObserver2 observer, android.content.pm.PackageInstaller.SessionParams sessionParams, com.android.server.pm.InstallSource installSource, android.os.UserHandle user, android.content.pm.SigningDetails signingDetails, int installerUid, android.content.pm.parsing.PackageLite packageLite, android.content.pm.verify.domain.DomainSet preVerifiedDomains, com.android.server.pm.PackageManagerService pm, boolean hasAppMetadatafile) {
        this.mPm = pm;
        this.mUser = user;
        this.mOriginInfo = com.android.server.pm.OriginInfo.fromStagedFile(stagedDir);
        this.mMoveInfo = null;
        this.mInstallReason = fixUpInstallReason(installSource.mInstallerPackageName, installerUid, sessionParams.installReason);
        this.mInstallScenario = sessionParams.installScenario;
        this.mObserver = observer;
        this.mInstallFlags = sessionParams.installFlags;
        this.mDevelopmentInstallFlags = sessionParams.developmentInstallFlags;
        this.mInstallSource = installSource;
        this.mVolumeUuid = sessionParams.volumeUuid;
        this.mPackageAbiOverride = sessionParams.abiOverride;
        this.mPermissionStates = sessionParams.getPermissionStates();
        this.mAllowlistedRestrictedPermissions = sessionParams.whitelistedRestrictedPermissions;
        this.mAutoRevokePermissionsMode = sessionParams.autoRevokePermissionsMode;
        this.mSigningDetails = signingDetails;
        this.mForceQueryableOverride = sessionParams.forceQueryableOverride;
        this.mDataLoaderType = sessionParams.dataLoaderParams != null ? sessionParams.dataLoaderParams.getType() : 0;
        this.mRequiredInstalledVersionCode = sessionParams.requiredInstalledVersionCode;
        this.mPackageSource = sessionParams.packageSource;
        this.mPackageLite = packageLite;
        this.mIsInherit = sessionParams.mode == 2;
        this.mSessionId = sessionId;
        this.mRequireUserAction = sessionParams.requireUserAction;
        this.mInstallParamsExt = (com.android.server.pm.IInstallParamsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IInstallParamsExt.class).create();
        this.mInstallParamsExt.init(sessionParams.mSessionParamsExt, installerUid, sessionParams);
        this.mApplicationEnabledSettingPersistent = sessionParams.applicationEnabledSettingPersistent;
        this.mPreVerifiedDomains = preVerifiedDomains;
        this.mHasAppMetadataFile = hasAppMetadatafile;
        this.mDexoptCompilerFilter = sessionParams.dexoptCompilerFilter;
    }

    public java.lang.String toString() {
        return "InstallingSession{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " file=" + this.mOriginInfo.mFile + "}";
    }

    private int overrideInstallLocation(java.lang.String packageName, int recommendedInstallLocation, int installLocation) {
        if (this.mOriginInfo.mStaged) {
            if (this.mOriginInfo.mFile != null) {
                this.mInstallFlags |= 16;
            } else {
                throw new java.lang.IllegalStateException("Invalid stage location");
            }
        }
        if (recommendedInstallLocation < 0) {
            return com.android.internal.content.InstallLocationUtils.getInstallationErrorCode(recommendedInstallLocation);
        }
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal installedPkgState = snapshot.getPackageStateInternal(packageName);
        com.android.server.pm.pkg.AndroidPackage installedPkg = installedPkgState == null ? null : installedPkgState.getAndroidPackage();
        if (installedPkg != null) {
            recommendedInstallLocation = com.android.internal.content.InstallLocationUtils.installLocationPolicy(installLocation, recommendedInstallLocation, this.mInstallFlags, installedPkgState.isSystem(), installedPkg.isExternalStorage());
        }
        int ret = this.mPm.mPackageManagerServiceExt.preSetRetInOverrideInstallLocation(packageName);
        if (ret != 1) {
            return ret;
        }
        boolean onInt = (this.mInstallFlags & 16) != 0;
        if (!onInt) {
            if (recommendedInstallLocation == 2) {
                this.mInstallFlags &= -17;
            } else {
                this.mInstallFlags |= 16;
            }
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartCopy(com.android.server.pm.InstallRequest request) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "handleStartCopy in InstallParams: " + (this.mPackageLite == null ? null : this.mPackageLite.getPackageName()));
        }
        if ((this.mInstallFlags & 131072) != 0) {
            this.mRet = 1;
            return;
        }
        android.content.pm.PackageInfoLite pkgLite = com.android.server.pm.PackageManagerServiceUtils.getMinimalPackageInfo(this.mPm.mContext, this.mPackageLite, this.mOriginInfo.mResolvedPath, this.mInstallFlags, this.mPackageAbiOverride);
        boolean isStaged = (this.mInstallFlags & 2097152) != 0;
        if (isStaged) {
            android.util.Pair<java.lang.Integer, java.lang.String> ret = this.mPm.verifyReplacingVersionCode(pkgLite, this.mRequiredInstalledVersionCode, this.mInstallFlags);
            this.mRet = ((java.lang.Integer) ret.first).intValue();
            if (this.mRet != 1) {
                request.setError(this.mRet, "Failed to verify version code");
                return;
            }
        }
        boolean ephemeral = (this.mInstallFlags & 2048) != 0;
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTANT && ephemeral) {
            android.util.Slog.v("PackageManager", "pkgLite for install: " + pkgLite);
        }
        if (!this.mOriginInfo.mStaged && pkgLite.recommendedInstallLocation == -1) {
            pkgLite.recommendedInstallLocation = this.mPm.freeCacheForInstallation(pkgLite.recommendedInstallLocation, this.mPackageLite, this.mOriginInfo.mResolvedPath, this.mPackageAbiOverride, this.mInstallFlags);
        }
        this.mRet = overrideInstallLocation(pkgLite.packageName, pkgLite.recommendedInstallLocation, pkgLite.installLocation);
        if (this.mRet != 1) {
            request.setError(this.mRet, "Failed to override installation location");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReturnCode(com.android.server.pm.InstallRequest installRequest) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "handleReturnCode in InstallParams: " + (this.mPackageLite == null ? null : this.mPackageLite.getPackageName()));
        }
        processPendingInstall(installRequest);
    }

    private void processPendingInstall(final com.android.server.pm.InstallRequest installRequest) {
        this.mPm.mPackageManagerServiceExt.modifyInstallArgsInProcessPendingInstall(installRequest.getWrapper().getInstallArgs(), this.mInstallParamsExt, this.mPackageLite);
        if (this.mRet == 1) {
            this.mRet = copyApk(installRequest);
        }
        if (this.mRet == 1) {
            com.android.internal.content.F2fsUtils.releaseCompressedBlocks(this.mPm.mContext.getContentResolver(), new java.io.File(installRequest.getCodePath()));
        }
        installRequest.setReturnCode(this.mRet);
        if (this.mParentInstallingSession != null) {
            this.mParentInstallingSession.tryProcessInstallRequest(installRequest);
        } else {
            this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallingSession$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processPendingInstall$0(installRequest);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processPendingInstall$0(com.android.server.pm.InstallRequest installRequest) {
        processInstallRequests(this.mRet == 1, java.util.Collections.singletonList(installRequest));
    }

    private int copyApk(com.android.server.pm.InstallRequest request) {
        if (this.mMoveInfo == null) {
            return copyApkForFileInstall(request);
        }
        return copyApkForMoveInstall(request);
    }

    private int copyApkForFileInstall(com.android.server.pm.InstallRequest request) throws java.io.IOException {
        android.os.Trace.traceBegin(262144L, "copyApk");
        try {
            if (this.mOriginInfo.mStaged) {
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Slog.d("PackageManager", this.mOriginInfo.mFile + " already staged; skipping copy");
                }
                request.setCodeFile(this.mOriginInfo.mFile);
                android.os.Trace.traceEnd(262144L);
                return 1;
            }
            try {
                boolean isEphemeral = (this.mInstallFlags & 2048) != 0;
                request.setCodeFile(this.mPm.mInstallerService.allocateStageDirLegacy(this.mVolumeUuid, isEphemeral));
                int ret = com.android.server.pm.PackageManagerServiceUtils.copyPackage(this.mOriginInfo.mFile.getAbsolutePath(), request.getCodeFile());
                if (ret != 1) {
                    android.util.Slog.e("PackageManager", "Failed to copy package");
                    request.setError(ret, "Failed to copy package");
                    android.os.Trace.traceEnd(262144L);
                    return ret;
                }
                boolean isIncremental = android.os.incremental.IncrementalManager.isIncrementalPath(request.getCodeFile().getAbsolutePath());
                java.io.File libraryRoot = new java.io.File(request.getCodeFile(), "lib");
                com.android.internal.content.NativeLibraryHelper.Handle handle = null;
                try {
                    try {
                        handle = com.android.internal.content.NativeLibraryHelper.Handle.create(request.getCodeFile());
                        ret = com.android.internal.content.NativeLibraryHelper.copyNativeBinariesWithOverride(handle, libraryRoot, request.getAbiOverride(), isIncremental);
                        if (ret != 1) {
                            request.setError(ret, "Failed to copy native libraries");
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e("PackageManager", "Copying native libraries failed", e);
                        request.setError(com.android.server.pm.PackageManagerException.ofInternalError("Copying native libraries failed", -1));
                        libcore.io.IoUtils.closeQuietly(handle);
                    }
                    android.os.Trace.traceEnd(262144L);
                    return ret;
                } finally {
                    libcore.io.IoUtils.closeQuietly(handle);
                }
            } catch (java.io.IOException e2) {
                android.util.Slog.w("PackageManager", "Failed to create copy file: " + e2);
                request.setError(-4, "Failed to create copy file");
                android.os.Trace.traceEnd(262144L);
                return -4;
            }
        } catch (java.lang.Throwable e3) {
            android.os.Trace.traceEnd(262144L);
            throw e3;
        }
    }

    private int copyApkForMoveInstall(com.android.server.pm.InstallRequest request) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "Moving " + this.mMoveInfo.mPackageName + " from " + this.mMoveInfo.mFromUuid + " to " + this.mMoveInfo.mToUuid);
        }
        try {
            com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
            try {
                this.mPm.mInstaller.moveCompleteApp(this.mMoveInfo.mFromUuid, this.mMoveInfo.mToUuid, this.mMoveInfo.mPackageName, this.mMoveInfo.mAppId, this.mMoveInfo.mSeInfo, this.mMoveInfo.mTargetSdkVersion, this.mMoveInfo.mFromCodePath);
                if (installLock != null) {
                    installLock.close();
                }
                java.lang.String toPathName = new java.io.File(this.mMoveInfo.mFromCodePath).getName();
                request.setCodeFile(new java.io.File(android.os.Environment.getDataAppDirectory(this.mMoveInfo.mToUuid), toPathName));
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Slog.d("PackageManager", "codeFile after move is " + request.getCodeFile());
                    return 1;
                }
                return 1;
            } finally {
            }
        } catch (com.android.server.pm.Installer.InstallerException e) {
            request.setError(com.android.server.pm.PackageManagerException.ofInternalError("Failed to move app", -2));
            android.util.Slog.w("PackageManager", "Failed to move app", e);
            return android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT;
        }
    }

    private int fixUpInstallReason(java.lang.String installerPackageName, int installerUid, int installReason) {
        if (this.mPm.snapshotComputer().checkUidPermission("android.permission.INSTALL_PACKAGES", installerUid) == 0) {
            return installReason;
        }
        java.lang.String ownerPackage = this.mPm.mProtectedPackages.getDeviceOwnerOrProfileOwnerPackage(android.os.UserHandle.getUserId(installerUid));
        if (ownerPackage != null && ownerPackage.equals(installerPackageName)) {
            return 1;
        }
        if (installReason == 1) {
            return 0;
        }
        return installReason;
    }

    public void installStage() {
        if (this.mPm.mPackageManagerServiceExt.installStageExtAtBegin(this)) {
            return;
        }
        setTraceMethod("installStage").setTraceCookie(java.lang.System.identityHashCode(this));
        android.os.Trace.asyncTraceBegin(262144L, "installStage", java.lang.System.identityHashCode(this));
        android.os.Trace.asyncTraceBegin(262144L, "queueInstall", java.lang.System.identityHashCode(this));
        this.mPm.mHandler.post(new com.android.server.pm.InstallingSession$$ExternalSyntheticLambda2(this));
    }

    public void installStage(java.util.List<com.android.server.pm.InstallingSession> children) throws com.android.server.pm.PackageManagerException {
        if (this.mPm.mPackageManagerServiceExt.installStageClusterExtAtBegin(this, children)) {
            return;
        }
        final com.android.server.pm.InstallingSession.MultiPackageInstallingSession installingSession = new com.android.server.pm.InstallingSession.MultiPackageInstallingSession(getUser(), children, this.mPm);
        setTraceMethod("installStageMultiPackage").setTraceCookie(java.lang.System.identityHashCode(installingSession));
        android.os.Trace.asyncTraceBegin(262144L, "installStageMultiPackage", java.lang.System.identityHashCode(installingSession));
        android.os.Trace.asyncTraceBegin(262144L, "queueInstall", java.lang.System.identityHashCode(installingSession));
        android.os.Handler handler = this.mPm.mHandler;
        java.util.Objects.requireNonNull(installingSession);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallingSession$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                installingSession.start();
            }
        });
    }

    public void movePackage() {
        setTraceMethod("movePackage").setTraceCookie(java.lang.System.identityHashCode(this));
        android.os.Trace.asyncTraceBegin(262144L, "movePackage", java.lang.System.identityHashCode(this));
        android.os.Trace.asyncTraceBegin(262144L, "queueInstall", java.lang.System.identityHashCode(this));
        this.mPm.mHandler.post(new com.android.server.pm.InstallingSession$$ExternalSyntheticLambda2(this));
    }

    public android.os.UserHandle getUser() {
        return this.mUser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.i("PackageManager", "start " + this.mUser + ": " + this);
        }
        android.os.Trace.asyncTraceEnd(262144L, "queueInstall", java.lang.System.identityHashCode(this));
        android.os.Trace.traceBegin(262144L, "startInstall");
        com.android.server.pm.InstallRequest installRequest = new com.android.server.pm.InstallRequest(this);
        handleStartCopy(installRequest);
        handleReturnCode(installRequest);
        android.os.Trace.traceEnd(262144L);
    }

    private com.android.server.pm.InstallingSession setTraceMethod(java.lang.String traceMethod) {
        this.mTraceMethod = traceMethod;
        return this;
    }

    private void setTraceCookie(int traceCookie) {
        this.mTraceCookie = traceCookie;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processInstallRequests(boolean success, java.util.List<com.android.server.pm.InstallRequest> installRequests) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "on processInstallRequestsAsync");
        }
        final java.util.List<com.android.server.pm.InstallRequest> apexInstallRequests = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.InstallRequest> apkInstallRequests = new java.util.ArrayList<>();
        for (com.android.server.pm.InstallRequest request : installRequests) {
            if ((request.getInstallFlags() & 131072) != 0) {
                apexInstallRequests.add(request);
            } else {
                apkInstallRequests.add(request);
            }
        }
        if (!apexInstallRequests.isEmpty() && !apkInstallRequests.isEmpty()) {
            throw new java.lang.IllegalStateException("Attempted to do a multi package install of both APEXes and APKs");
        }
        if (!apexInstallRequests.isEmpty()) {
            if (success) {
                java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.pm.InstallingSession$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processInstallRequests$1(apexInstallRequests);
                    }
                }, "installApexPackages");
                t.start();
                return;
            } else {
                this.mPm.notifyInstallObserver(apexInstallRequests.get(0));
                return;
            }
        }
        processApkInstallRequests(success, installRequests);
    }

    private void processApkInstallRequests(boolean success, java.util.List<com.android.server.pm.InstallRequest> installRequests) {
        if (!success) {
            for (com.android.server.pm.InstallRequest request : installRequests) {
                if (request.getReturnCode() != 1) {
                    cleanUpForFailedInstall(request);
                }
            }
        } else {
            this.mPm.mPackageManagerServiceExt.beforeInstallPackagesTracedLI();
            this.mPm.installPackagesTraced(installRequests);
            for (com.android.server.pm.InstallRequest request2 : installRequests) {
                doPostInstall(request2);
                this.mPm.mPackageManagerServiceExt.afterDoPostInstallInProcessInstallRequestsAsync(request2.getWrapper().getInstallArgs(), this.mPm.mHandler, request2.getName(), request2.getReturnCode());
            }
        }
        java.util.Iterator<com.android.server.pm.InstallRequest> it = installRequests.iterator();
        while (it.hasNext()) {
            this.mPm.restoreAndPostInstall(it.next());
        }
    }

    private void doPostInstall(com.android.server.pm.InstallRequest request) {
        if (this.mMoveInfo != null) {
            if (request.getReturnCode() == 1) {
                this.mPm.cleanUpForMoveInstall(this.mMoveInfo.mFromUuid, this.mMoveInfo.mPackageName, this.mMoveInfo.mFromCodePath);
                return;
            } else {
                this.mPm.cleanUpForMoveInstall(this.mMoveInfo.mToUuid, this.mMoveInfo.mPackageName, this.mMoveInfo.mFromCodePath);
                return;
            }
        }
        if (request.getReturnCode() != 1) {
            this.mPm.removeCodePath(request.getCodeFile());
        }
    }

    private void cleanUpForFailedInstall(com.android.server.pm.InstallRequest request) {
        if (request.isInstallMove()) {
            this.mPm.cleanUpForMoveInstall(request.getMoveToUuid(), request.getMovePackageName(), request.getMoveFromCodePath());
        } else {
            this.mPm.removeCodePath(request.getCodeFile());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: installApexPackagesTraced, reason: merged with bridge method [inline-methods] */
    public void lambda$processInstallRequests$1(java.util.List<com.android.server.pm.InstallRequest> requests) {
        try {
            android.os.Trace.traceBegin(262144L, "installApexPackages");
            installApexPackages(requests);
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    private void installApexPackages(final java.util.List<com.android.server.pm.InstallRequest> requests) {
        if (requests.isEmpty()) {
            return;
        }
        if (requests.size() != 1) {
            throw new java.lang.IllegalStateException("Only a non-staged install of a single APEX is supported");
        }
        com.android.server.pm.InstallRequest request = requests.get(0);
        boolean force = (request.getDevelopmentInstallFlags() & 1) != 0;
        try {
            java.io.File dir = request.getOriginInfo().mResolvedFile;
            java.io.File[] apexes = dir.listFiles();
            if (apexes == null) {
                throw com.android.server.pm.PackageManagerException.ofInternalError(dir.getAbsolutePath() + " is not a directory", -36);
            }
            if (apexes.length != 1) {
                throw com.android.server.pm.PackageManagerException.ofInternalError("Expected exactly one .apex file under " + dir.getAbsolutePath() + " got: " + apexes.length, -37);
            }
            com.android.internal.pm.parsing.PackageParser2 packageParser = this.mPm.mInjector.getScanningPackageParser();
            try {
                android.apex.ApexInfo apexInfo = this.mPm.mApexManager.installPackage(apexes[0], force);
                request.setApexInfo(apexInfo);
                request.setApexModuleName(apexInfo.moduleName);
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallingSession$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$installApexPackages$2(requests);
                    }
                });
                if (packageParser != null) {
                    packageParser.close();
                }
            } finally {
            }
        } catch (com.android.server.pm.PackageManagerException e) {
            request.setError("APEX installation failed", e);
            com.android.server.pm.PackageManagerService.invalidatePackageInfoCache();
            this.mPm.notifyInstallObserver(request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$installApexPackages$2(java.util.List requests) {
        processApkInstallRequests(true, requests);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class MultiPackageInstallingSession {
        private final java.util.List<com.android.server.pm.InstallingSession> mChildInstallingSessions;
        private final java.util.Set<com.android.server.pm.InstallRequest> mCurrentInstallRequests;
        final com.android.server.pm.PackageManagerService mPm;
        final android.os.UserHandle mUser;

        MultiPackageInstallingSession(android.os.UserHandle user, java.util.List<com.android.server.pm.InstallingSession> childInstallingSessions, com.android.server.pm.PackageManagerService pm) throws com.android.server.pm.PackageManagerException {
            if (childInstallingSessions.size() == 0) {
                throw com.android.server.pm.PackageManagerException.ofInternalError("No child sessions found!", -20);
            }
            this.mPm = pm;
            this.mUser = user;
            this.mChildInstallingSessions = childInstallingSessions;
            for (int i = 0; i < childInstallingSessions.size(); i++) {
                com.android.server.pm.InstallingSession childInstallingSession = childInstallingSessions.get(i);
                childInstallingSession.mParentInstallingSession = this;
            }
            this.mCurrentInstallRequests = new android.util.ArraySet(this.mChildInstallingSessions.size());
        }

        public void start() {
            if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                android.util.Slog.i("PackageManager", "start " + this.mUser + ": " + this);
            }
            android.os.Trace.asyncTraceEnd(262144L, "queueInstall", java.lang.System.identityHashCode(this));
            android.os.Trace.traceBegin(262144L, "start");
            int numChildSessions = this.mChildInstallingSessions.size();
            java.util.ArrayList<com.android.server.pm.InstallRequest> installRequests = new java.util.ArrayList<>(numChildSessions);
            for (int i = 0; i < numChildSessions; i++) {
                com.android.server.pm.InstallingSession childSession = this.mChildInstallingSessions.get(i);
                com.android.server.pm.InstallRequest installRequest = new com.android.server.pm.InstallRequest(childSession);
                installRequests.add(installRequest);
                childSession.handleStartCopy(installRequest);
            }
            for (int i2 = 0; i2 < numChildSessions; i2++) {
                this.mChildInstallingSessions.get(i2).handleReturnCode(installRequests.get(i2));
            }
            android.os.Trace.traceEnd(262144L);
        }

        public void tryProcessInstallRequest(com.android.server.pm.InstallRequest request) {
            this.mCurrentInstallRequests.add(request);
            if (this.mCurrentInstallRequests.size() != this.mChildInstallingSessions.size()) {
                return;
            }
            int completeStatus = 1;
            java.util.Iterator<com.android.server.pm.InstallRequest> it = this.mCurrentInstallRequests.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.pm.InstallRequest installRequest = it.next();
                if (installRequest.getReturnCode() == 0) {
                    return;
                }
                if (installRequest.getReturnCode() != 1) {
                    completeStatus = installRequest.getReturnCode();
                    break;
                }
            }
            final java.util.List<com.android.server.pm.InstallRequest> installRequests = new java.util.ArrayList<>(this.mCurrentInstallRequests.size());
            for (com.android.server.pm.InstallRequest installRequest2 : this.mCurrentInstallRequests) {
                installRequest2.setReturnCode(completeStatus);
                installRequests.add(installRequest2);
            }
            final int finalCompleteStatus = completeStatus;
            this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.InstallingSession$MultiPackageInstallingSession$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$tryProcessInstallRequest$0(finalCompleteStatus, installRequests);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryProcessInstallRequest$0(int finalCompleteStatus, java.util.List installRequests) {
            com.android.server.pm.InstallingSession.this.processInstallRequests(finalCompleteStatus == 1, installRequests);
        }

        public java.lang.String toString() {
            return "MultiPackageInstallingSession{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + "}";
        }
    }
}
