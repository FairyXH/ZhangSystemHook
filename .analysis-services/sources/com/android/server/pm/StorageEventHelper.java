package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class StorageEventHelper extends android.os.storage.StorageEventListener {
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final com.android.server.pm.DeletePackageHelper mDeletePackageHelper;
    final android.util.ArraySet<java.lang.String> mLoadedVolumes = new android.util.ArraySet<>();
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.RemovePackageHelper mRemovePackageHelper;

    public StorageEventHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.DeletePackageHelper deletePackageHelper, com.android.server.pm.RemovePackageHelper removePackageHelper) {
        this.mPm = pm;
        this.mBroadcastHelper = new com.android.server.pm.BroadcastHelper(this.mPm.mInjector);
        this.mDeletePackageHelper = deletePackageHelper;
        this.mRemovePackageHelper = removePackageHelper;
    }

    public void onVolumeStateChanged(android.os.storage.VolumeInfo vol, int oldState, int newState) {
        if (vol.type == 1) {
            if (vol.state == 2) {
                java.lang.String volumeUuid = vol.getFsUuid();
                this.mPm.mUserManager.reconcileUsers(volumeUuid);
                reconcileApps(this.mPm.snapshotComputer(), volumeUuid);
                this.mPm.mInstallerService.onPrivateVolumeMounted(volumeUuid);
                loadPrivatePackages(vol);
                return;
            }
            if (vol.state == 5) {
                unloadPrivatePackages(vol);
            }
        }
    }

    public void onVolumeForgotten(java.lang.String fsUuid) {
        if (android.text.TextUtils.isEmpty(fsUuid)) {
            android.util.Slog.e("PackageManager", "Forgetting internal storage is probably a mistake; ignoring");
            return;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> packages = this.mPm.mSettings.getVolumePackagesLPr(fsUuid);
                for (com.android.server.pm.pkg.PackageStateInternal ps : packages) {
                    android.util.Slog.d("PackageManager", "Destroying " + ps.getPackageName() + " because volume was forgotten");
                    this.mPm.deletePackageVersioned(new android.content.pm.VersionedPackage(ps.getPackageName(), -1), new android.content.pm.PackageManager.LegacyPackageDeleteObserver((android.content.pm.IPackageDeleteObserver) null).getBinder(), 0, 2);
                    com.android.internal.policy.AttributeCache.instance().removePackage(ps.getPackageName());
                }
                this.mPm.mSettings.onVolumeForgotten(fsUuid);
                this.mPm.writeSettingsLPrTEMP();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void loadPrivatePackages(final android.os.storage.VolumeInfo vol) {
        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.StorageEventHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$loadPrivatePackages$0(vol);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(13:35|(2:40|(2:134|42)(4:131|68|137|135))(2:133|39)|108|43|44|102|45|(2:106|47)|51|67|136|135|33) */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0177, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0178, code lost:
    
        r19 = r4;
     */
    /* JADX INFO: renamed from: loadPrivatePackagesInner, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void lambda$loadPrivatePackages$0(android.os.storage.VolumeInfo r21) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 608
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.StorageEventHelper.lambda$loadPrivatePackages$0(android.os.storage.VolumeInfo):void");
    }

    private void unloadPrivatePackages(final android.os.storage.VolumeInfo vol) {
        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.StorageEventHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$unloadPrivatePackages$1(vol);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: unloadPrivatePackagesInner, reason: merged with bridge method [inline-methods] */
    public void lambda$unloadPrivatePackages$1(android.os.storage.VolumeInfo vol) throws java.lang.Throwable {
        java.lang.Throwable th;
        java.lang.String volumeUuid = vol.fsUuid;
        if (android.text.TextUtils.isEmpty(volumeUuid)) {
            android.util.Slog.e("PackageManager", "Unloading internal storage is probably a mistake; ignoring");
            return;
        }
        int[] userIds = this.mPm.mUserManager.getUserIds();
        java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> unloaded = new java.util.ArrayList<>();
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> packages = this.mPm.mSettings.getVolumePackagesLPr(volumeUuid);
                    for (com.android.server.pm.pkg.PackageStateInternal ps : packages) {
                        if (ps.getPkg() != null) {
                            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
                            com.android.server.pm.PackageFreezer freezer = this.mPm.freezePackageForDelete(ps.getPackageName(), -1, 1, "unloadPrivatePackagesInner", 13);
                            try {
                                try {
                                    if (this.mDeletePackageHelper.deletePackageLIF(ps.getPackageName(), null, false, userIds, 1, new com.android.server.pm.PackageRemovedInfo(), false)) {
                                        unloaded.add(pkg);
                                    } else {
                                        android.util.Slog.w("PackageManager", "Failed to unload " + ps.getPath());
                                    }
                                    if (freezer != null) {
                                        freezer.close();
                                    }
                                    com.android.internal.policy.AttributeCache.instance().removePackage(ps.getPackageName());
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    if (freezer == null) {
                                        throw th;
                                    }
                                    try {
                                        freezer.close();
                                        throw th;
                                    } catch (java.lang.Throwable th3) {
                                        th.addSuppressed(th3);
                                        throw th;
                                    }
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        }
                    }
                    this.mPm.writeSettingsLPrTEMP();
                } catch (java.lang.Throwable th5) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th5;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (installLock != null) {
                installLock.close();
            }
            if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                android.util.Slog.d("PackageManager", "Unloaded packages " + unloaded);
            }
            this.mBroadcastHelper.sendResourcesChangedBroadcastAndNotify(this.mPm.snapshotComputer(), false, false, unloaded);
            synchronized (this.mLoadedVolumes) {
                this.mLoadedVolumes.remove(vol.getId());
            }
            android.app.ResourcesManager.getInstance().invalidatePath(vol.getPath().getAbsolutePath());
            for (int i = 0; i < 3; i++) {
                java.lang.System.gc();
                java.lang.System.runFinalization();
            }
        } finally {
        }
    }

    public void reconcileApps(com.android.server.pm.Computer snapshot, java.lang.String volumeUuid) {
        java.util.List<java.lang.String> absoluteCodePaths = collectAbsoluteCodePaths(snapshot);
        java.util.List<java.io.File> filesToDelete = null;
        java.io.File[] files = android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataAppDirectory(volumeUuid));
        for (java.io.File file : files) {
            boolean isPackage = (android.content.pm.parsing.ApkLiteParseUtils.isApkFile(file) || file.isDirectory()) && !com.android.server.pm.PackageInstallerService.isStageName(file.getName());
            if (isPackage) {
                java.lang.String absolutePath = file.getAbsolutePath();
                boolean pathValid = false;
                int absoluteCodePathCount = absoluteCodePaths.size();
                int i = 0;
                while (true) {
                    if (i >= absoluteCodePathCount) {
                        break;
                    }
                    java.lang.String absoluteCodePath = absoluteCodePaths.get(i);
                    if (!absoluteCodePath.startsWith(absolutePath)) {
                        i++;
                    } else {
                        pathValid = true;
                        break;
                    }
                }
                if (!pathValid) {
                    if (filesToDelete == null) {
                        filesToDelete = new java.util.ArrayList<>();
                    }
                    filesToDelete.add(file);
                }
            }
        }
        if (filesToDelete != null) {
            int fileToDeleteCount = filesToDelete.size();
            for (int i2 = 0; i2 < fileToDeleteCount; i2++) {
                java.io.File fileToDelete = filesToDelete.get(i2);
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Destroying orphaned at " + fileToDelete);
                this.mRemovePackageHelper.removeCodePath(fileToDelete);
            }
        }
    }

    private java.util.List<java.lang.String> collectAbsoluteCodePaths(com.android.server.pm.Computer snapshot) {
        java.util.List<java.lang.String> codePaths = new java.util.ArrayList<>();
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = snapshot.getPackageStates();
        int packageCount = packageStates.size();
        for (int i = 0; i < packageCount; i++) {
            com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(i);
            codePaths.add(ps.getPath().getAbsolutePath());
        }
        return codePaths;
    }

    public void dumpLoadedVolumes(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
        if (dumpState.onTitlePrinted()) {
            pw.println();
        }
        com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ", 120);
        ipw.println();
        ipw.println("Loaded volumes:");
        ipw.increaseIndent();
        synchronized (this.mLoadedVolumes) {
            if (this.mLoadedVolumes.size() == 0) {
                ipw.println("(none)");
            } else {
                for (int i = 0; i < this.mLoadedVolumes.size(); i++) {
                    ipw.println(this.mLoadedVolumes.valueAt(i));
                }
            }
        }
        ipw.decreaseIndent();
    }
}
