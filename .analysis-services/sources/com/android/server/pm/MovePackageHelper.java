package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class MovePackageHelper {
    final com.android.server.pm.PackageManagerService mPm;

    public MovePackageHelper(com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
    }

    public void movePackageInternal(final java.lang.String packageName, java.lang.String volumeUuid, final int moveId, int callingUid, android.os.UserHandle user) throws com.android.server.pm.PackageManagerException {
        java.lang.String fromCodePath;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock;
        boolean moveCompleteApp;
        java.io.File measurePath;
        java.lang.String label;
        android.os.Bundle extras;
        int[] installedUserIds;
        java.lang.Throwable th;
        int length;
        int i;
        android.content.pm.PackageManager pm;
        com.android.server.pm.Computer snapshot;
        long sizeBytes;
        com.android.server.pm.MoveInfo move;
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mPm.mInjector.getSystemService(android.os.storage.StorageManager.class);
        android.content.pm.PackageManager pm2 = this.mPm.mContext.getPackageManager();
        com.android.server.pm.Computer snapshot2 = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot2.getPackageStateForInstalledAndFiltered(packageName, callingUid, user.getIdentifier());
        if (packageState == null || packageState.getPkg() == null) {
            throw new com.android.server.pm.PackageManagerException(-2, "Missing package");
        }
        int[] installedUserIds2 = com.android.server.pm.pkg.PackageStateUtils.queryInstalledUsers(packageState, this.mPm.mUserManager.getUserIds(), true);
        if (installedUserIds2.length > 0) {
            android.os.UserHandle userForMove = android.os.UserHandle.of(installedUserIds2[0]);
            for (int i2 : installedUserIds2) {
                if (snapshot2.shouldFilterApplicationIncludingUninstalled(packageState, callingUid, i2)) {
                    throw new com.android.server.pm.PackageManagerException(-2, "Missing package");
                }
            }
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
            if (packageState.isSystem()) {
                throw new com.android.server.pm.PackageManagerException(-3, "Cannot move system application");
            }
            boolean isInternalStorage = "private".equals(volumeUuid);
            boolean allow3rdPartyOnInternal = this.mPm.mContext.getResources().getBoolean(android.R.bool.config_allowAlarmsOnStoppedUsers);
            if (isInternalStorage && !allow3rdPartyOnInternal) {
                throw new com.android.server.pm.PackageManagerException(-9, "3rd party apps are not allowed on internal storage");
            }
            java.io.File probe = new java.io.File(pkg.getPath());
            if (!probe.isDirectory()) {
                throw new com.android.server.pm.PackageManagerException(-6, "Move only supported for modern cluster style installs");
            }
            java.lang.String currentVolumeUuid = packageState.getVolumeUuid();
            if (java.util.Objects.equals(currentVolumeUuid, volumeUuid)) {
                throw new com.android.server.pm.PackageManagerException(-6, "Package already moved to " + volumeUuid);
            }
            if (!pkg.isExternalStorage() && this.mPm.isPackageDeviceAdminOnAnyUser(snapshot2, packageName)) {
                throw new com.android.server.pm.PackageManagerException(-8, "Device admin cannot be moved");
            }
            if (snapshot2.getFrozenPackages().containsKey(packageName)) {
                throw new com.android.server.pm.PackageManagerException(-7, "Failed to move already frozen package");
            }
            final boolean isCurrentLocationExternal = pkg.isExternalStorage();
            java.io.File codeFile = new java.io.File(pkg.getPath());
            com.android.server.pm.InstallSource installSource = packageState.getInstallSource();
            java.lang.String packageAbiOverride = packageState.getCpuAbiOverride();
            int appId = android.os.UserHandle.getAppId(pkg.getUid());
            java.lang.String seinfo = packageState.getSeInfo();
            java.lang.String label2 = java.lang.String.valueOf(pm2.getApplicationLabel(com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg)));
            int targetSdkVersion = pkg.getTargetSdkVersion();
            if (codeFile.getParentFile().getName().startsWith("~~")) {
                fromCodePath = codeFile.getParentFile().getAbsolutePath();
            } else {
                java.lang.String fromCodePath2 = codeFile.getAbsolutePath();
                fromCodePath = fromCodePath2;
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    packageManagerTracedLock = packageManagerTracedLock2;
                    try {
                        final com.android.server.pm.PackageFreezer freezer = this.mPm.freezePackage(packageName, -1, "movePackageInternal", 10, null);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        android.os.Bundle extras2 = new android.os.Bundle();
                        extras2.putString("android.intent.extra.PACKAGE_NAME", packageName);
                        java.lang.String label3 = label2;
                        extras2.putString("android.intent.extra.TITLE", label3);
                        this.mPm.mMoveCallbacks.notifyCreated(moveId, extras2);
                        if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, volumeUuid)) {
                            measurePath = android.os.Environment.getDataAppDirectory(volumeUuid);
                            moveCompleteApp = true;
                        } else if (java.util.Objects.equals("primary_physical", volumeUuid)) {
                            measurePath = storage.getPrimaryPhysicalVolume().getPath();
                            moveCompleteApp = false;
                        } else {
                            android.os.storage.VolumeInfo volume = storage.findVolumeByUuid(volumeUuid);
                            if (volume == null || volume.getType() != 1 || !volume.isMountedWritable()) {
                                freezer.close();
                                throw new com.android.server.pm.PackageManagerException(-6, "Move location not mounted private volume");
                            }
                            moveCompleteApp = true;
                            measurePath = android.os.Environment.getDataAppDirectory(volumeUuid);
                        }
                        if (!moveCompleteApp) {
                            label = label3;
                            extras = extras2;
                            installedUserIds = installedUserIds2;
                        } else {
                            installedUserIds = installedUserIds2;
                            int length2 = installedUserIds.length;
                            int i3 = 0;
                            while (i3 < length2) {
                                int i4 = length2;
                                int userId = installedUserIds[i3];
                                if (!android.os.storage.StorageManager.isFileEncrypted() || android.os.storage.StorageManager.isCeStorageUnlocked(userId)) {
                                    i3++;
                                    length2 = i4;
                                    label3 = label3;
                                    extras2 = extras2;
                                } else {
                                    freezer.close();
                                    throw new com.android.server.pm.PackageManagerException(-10, "User " + userId + " must be unlocked");
                                }
                            }
                            label = label3;
                            extras = extras2;
                        }
                        android.content.pm.PackageStats stats = new android.content.pm.PackageStats(null, -1);
                        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
                        try {
                            length = installedUserIds.length;
                            i = 0;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                        while (i < length) {
                            try {
                                int i5 = length;
                                if (getPackageSizeInfoLI(packageName, installedUserIds[i], stats)) {
                                    i++;
                                    length = i5;
                                } else {
                                    freezer.close();
                                    try {
                                        throw new com.android.server.pm.PackageManagerException(-6, "Failed to measure package size");
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                    }
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                            if (installLock == null) {
                                throw th;
                            }
                            try {
                                installLock.close();
                                throw th;
                            } catch (java.lang.Throwable th5) {
                                th.addSuppressed(th5);
                                throw th;
                            }
                        }
                        if (installLock != null) {
                            installLock.close();
                        }
                        if (!com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                            pm = pm2;
                        } else {
                            pm = pm2;
                            android.util.Slog.d("PackageManager", "Measured code size " + stats.codeSize + ", data size " + stats.dataSize);
                        }
                        final long startFreeBytes = measurePath.getUsableSpace();
                        if (moveCompleteApp) {
                            snapshot = snapshot2;
                            sizeBytes = stats.codeSize + stats.dataSize;
                        } else {
                            snapshot = snapshot2;
                            sizeBytes = stats.codeSize;
                        }
                        if (sizeBytes <= storage.getStorageBytesUntilLow(measurePath)) {
                            try {
                                prepareUserStorageForMove(currentVolumeUuid, volumeUuid, installedUserIds);
                                this.mPm.mMoveCallbacks.notifyStatusChanged(moveId, 10);
                                final java.util.concurrent.CountDownLatch installedLatch = new java.util.concurrent.CountDownLatch(1);
                                final java.io.File measurePath2 = measurePath;
                                android.content.pm.IPackageInstallObserver2.Stub stub = new android.content.pm.IPackageInstallObserver2.Stub() { // from class: com.android.server.pm.MovePackageHelper.1
                                    public void onUserActionRequired(android.content.Intent intent) throws android.os.RemoteException {
                                        freezer.close();
                                        throw new java.lang.IllegalStateException();
                                    }

                                    public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras3) throws android.os.RemoteException {
                                        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                                            android.util.Slog.d("PackageManager", "Install result for move: " + android.content.pm.PackageManager.installStatusToString(returnCode, msg));
                                        }
                                        installedLatch.countDown();
                                        freezer.close();
                                        int status = android.content.pm.PackageManager.installStatusToPublicStatus(returnCode);
                                        switch (status) {
                                            case 0:
                                                com.android.server.pm.MovePackageHelper.this.mPm.mMoveCallbacks.notifyStatusChanged(moveId, -100);
                                                com.android.server.pm.MovePackageHelper.this.logAppMovedStorage(packageName, isCurrentLocationExternal);
                                                break;
                                            case 6:
                                                com.android.server.pm.MovePackageHelper.this.mPm.mMoveCallbacks.notifyStatusChanged(moveId, -1);
                                                break;
                                            default:
                                                com.android.server.pm.MovePackageHelper.this.mPm.mMoveCallbacks.notifyStatusChanged(moveId, -6);
                                                break;
                                        }
                                    }
                                };
                                if (moveCompleteApp) {
                                    final long j = sizeBytes;
                                    new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.pm.MovePackageHelper$$ExternalSyntheticLambda0
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$movePackageInternal$0(installedLatch, startFreeBytes, measurePath2, j, moveId);
                                        }
                                    }).start();
                                    move = new com.android.server.pm.MoveInfo(moveId, currentVolumeUuid, volumeUuid, packageName, appId, seinfo, targetSdkVersion, fromCodePath);
                                } else {
                                    move = null;
                                }
                                int installFlags = 16 | 2;
                                com.android.server.pm.OriginInfo origin = com.android.server.pm.OriginInfo.fromExistingFile(codeFile);
                                android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
                                android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> ret = android.content.pm.parsing.ApkLiteParseUtils.parsePackageLite(input, new java.io.File(origin.mResolvedPath), 0);
                                android.content.pm.parsing.PackageLite lite = ret.isSuccess() ? (android.content.pm.parsing.PackageLite) ret.getResult() : null;
                                com.android.server.pm.InstallingSession installingSession = new com.android.server.pm.InstallingSession(origin, move, (android.content.pm.IPackageInstallObserver2) stub, installFlags, 0, installSource, volumeUuid, userForMove, packageAbiOverride, 0, lite, this.mPm);
                                installingSession.movePackage();
                                return;
                            } catch (java.lang.RuntimeException e) {
                                freezer.close();
                                throw new com.android.server.pm.PackageManagerException(-6, "Failed to prepare user storage while moving app");
                            }
                        }
                        freezer.close();
                        throw new com.android.server.pm.PackageManagerException(-6, "Not enough free space to move");
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                        while (true) {
                            try {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                throw th;
                            } catch (java.lang.Throwable th7) {
                                th = th7;
                            }
                        }
                    }
                } catch (java.lang.Throwable th8) {
                    th = th8;
                    packageManagerTracedLock = packageManagerTracedLock2;
                }
            }
        } else {
            throw new com.android.server.pm.PackageManagerException(-2, "Package is not installed for any user");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$movePackageInternal$0(java.util.concurrent.CountDownLatch installedLatch, long startFreeBytes, java.io.File measurePath, long sizeBytes, int moveId) {
        while (!installedLatch.await(1L, java.util.concurrent.TimeUnit.SECONDS)) {
            long deltaFreeBytes = startFreeBytes - measurePath.getUsableSpace();
            int progress = ((int) android.util.MathUtils.constrain((80 * deltaFreeBytes) / sizeBytes, 0L, 80L)) + 10;
            this.mPm.mMoveCallbacks.notifyStatusChanged(moveId, progress);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppMovedStorage(java.lang.String packageName, boolean isPreviousLocationExternal) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.AndroidPackage pkg = snapshot.getPackage(packageName);
        if (pkg == null) {
            return;
        }
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mPm.mInjector.getSystemService(android.os.storage.StorageManager.class);
        android.os.storage.VolumeInfo volume = storage.findVolumeByUuid(android.os.storage.StorageManager.convert(pkg.getVolumeUuid()).toString());
        int packageExternalStorageType = com.android.server.pm.PackageManagerServiceUtils.getPackageExternalStorageType(volume, pkg.isExternalStorage());
        if (!isPreviousLocationExternal && pkg.isExternalStorage()) {
            com.android.internal.util.FrameworkStatsLog.write(183, packageExternalStorageType, 1, packageName);
        } else if (isPreviousLocationExternal && !pkg.isExternalStorage()) {
            com.android.internal.util.FrameworkStatsLog.write(183, packageExternalStorageType, 2, packageName);
        }
    }

    private boolean getPackageSizeInfoLI(java.lang.String packageName, int userId, android.content.pm.PackageStats stats) {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal packageStateInternal = snapshot.getPackageStateInternal(packageName);
        if (packageStateInternal == null) {
            android.util.Slog.w("PackageManager", "Failed to find settings for " + packageName);
            return false;
        }
        java.lang.String[] packageNames = {packageName};
        long[] ceDataInodes = {packageStateInternal.getUserStateOrDefault(userId).getCeDataInode()};
        java.lang.String[] codePaths = {packageStateInternal.getPathString()};
        try {
            this.mPm.mInstaller.getAppSize(packageStateInternal.getVolumeUuid(), packageNames, userId, 0, packageStateInternal.getAppId(), ceDataInodes, codePaths, stats);
            if (com.android.server.pm.PackageManagerServiceUtils.isSystemApp(packageStateInternal) && !com.android.server.pm.PackageManagerServiceUtils.isUpdatedSystemApp(packageStateInternal)) {
                stats.codeSize = 0L;
            }
            stats.dataSize -= stats.cacheSize;
            return true;
        } catch (com.android.server.pm.Installer.InstallerException e) {
            android.util.Slog.w("PackageManager", java.lang.String.valueOf(e));
            return false;
        }
    }

    private void prepareUserStorageForMove(java.lang.String fromVolumeUuid, java.lang.String toVolumeUuid, int[] userIds) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d("PackageManager", "Preparing user directories before moving app, from UUID " + fromVolumeUuid + " to UUID " + toVolumeUuid);
        }
        android.os.storage.StorageManagerInternal smInternal = (android.os.storage.StorageManagerInternal) this.mPm.mInjector.getLocalService(android.os.storage.StorageManagerInternal.class);
        java.util.ArrayList<android.content.pm.UserInfo> users = new java.util.ArrayList<>();
        for (int userId : userIds) {
            android.content.pm.UserInfo user = this.mPm.mUserManager.getUserInfo(userId);
            users.add(user);
        }
        smInternal.prepareUserStorageForMove(fromVolumeUuid, toVolumeUuid, users);
    }

    public static class MoveCallbacks extends android.os.Handler {
        private static final int MSG_CREATED = 1;
        private static final int MSG_STATUS_CHANGED = 2;
        private final android.os.RemoteCallbackList<android.content.pm.IPackageMoveObserver> mCallbacks;
        public final android.util.SparseIntArray mLastStatus;

        public MoveCallbacks(android.os.Looper looper) {
            super(looper);
            this.mCallbacks = new android.os.RemoteCallbackList<>();
            this.mLastStatus = new android.util.SparseIntArray();
        }

        public void register(android.content.pm.IPackageMoveObserver callback) {
            this.mCallbacks.register(callback);
        }

        public void unregister(android.content.pm.IPackageMoveObserver callback) {
            this.mCallbacks.unregister(callback);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
            int n = this.mCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                android.content.pm.IPackageMoveObserver callback = (android.content.pm.IPackageMoveObserver) this.mCallbacks.getBroadcastItem(i);
                try {
                    invokeCallback(callback, msg.what, args);
                } catch (android.os.RemoteException e) {
                }
            }
            this.mCallbacks.finishBroadcast();
            args.recycle();
        }

        private void invokeCallback(android.content.pm.IPackageMoveObserver callback, int what, com.android.internal.os.SomeArgs args) throws android.os.RemoteException {
            switch (what) {
                case 1:
                    callback.onCreated(args.argi1, (android.os.Bundle) args.arg2);
                    break;
                case 2:
                    callback.onStatusChanged(args.argi1, args.argi2, ((java.lang.Long) args.arg3).longValue());
                    break;
            }
        }

        public void notifyCreated(int moveId, android.os.Bundle extras) {
            android.util.Slog.v("PackageManager", "Move " + moveId + " created " + extras.toString());
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = moveId;
            args.arg2 = extras;
            obtainMessage(1, args).sendToTarget();
        }

        public void notifyStatusChanged(int moveId, int status) {
            notifyStatusChanged(moveId, status, -1L);
        }

        public void notifyStatusChanged(int moveId, int status, long estMillis) {
            android.util.Slog.v("PackageManager", "Move " + moveId + " status " + status);
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = moveId;
            args.argi2 = status;
            args.arg3 = java.lang.Long.valueOf(estMillis);
            obtainMessage(2, args).sendToTarget();
            synchronized (this.mLastStatus) {
                this.mLastStatus.put(moveId, status);
            }
        }
    }
}
