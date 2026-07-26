package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DeletePackageHelper {
    private static final boolean DEBUG_CLEAN_APKS = false;
    private static final boolean DEBUG_SD_INSTALL = false;
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.RemovePackageHelper mRemovePackageHelper;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;

    DeletePackageHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.RemovePackageHelper removePackageHelper, com.android.server.pm.BroadcastHelper broadcastHelper) {
        this.mPm = pm;
        this.mUserManagerInternal = this.mPm.mInjector.getUserManagerInternal();
        this.mRemovePackageHelper = removePackageHelper;
        this.mBroadcastHelper = broadcastHelper;
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x0345  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x03ae  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x03da  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0487  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x04c1 A[Catch: all -> 0x0500, TRY_LEAVE, TryCatch #20 {all -> 0x0500, blocks: (B:214:0x04be, B:216:0x04c1), top: B:383:0x04be }] */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0550  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0559  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x055b A[Catch: all -> 0x0600, TryCatch #19 {all -> 0x0600, blocks: (B:246:0x053f, B:247:0x0542, B:241:0x0533, B:258:0x0562, B:260:0x0568, B:261:0x056f, B:264:0x057d, B:267:0x0584, B:269:0x0588, B:270:0x05a4, B:271:0x05aa, B:273:0x05ae, B:277:0x05cd, B:278:0x05d0, B:255:0x055b, B:262:0x0570, B:263:0x057c), top: B:382:0x0488, inners: #26 }] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x05d3  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x05fb  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x05fd  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x0425 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:386:0x048a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:394:0x0570 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:404:0x0611 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:423:0x04fa A[EDGE_INSN: B:423:0x04fa->B:235:0x04fa BREAK  A[LOOP:4: B:213:0x04b8->B:232:0x04e9], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:431:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0147 A[Catch: all -> 0x0235, TRY_ENTER, TryCatch #11 {all -> 0x0235, blocks: (B:44:0x0117, B:55:0x0147, B:57:0x0150, B:63:0x0163, B:65:0x0188, B:67:0x018e, B:69:0x0194, B:73:0x01a5, B:75:0x01ab, B:78:0x01ba, B:82:0x01c5, B:85:0x01cd, B:86:0x021b, B:102:0x0251, B:103:0x0257, B:105:0x025a, B:50:0x0131), top: B:369:0x0117 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0232  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int deletePackageX(java.lang.String r31, long r32, int r34, int r35, boolean r36) {
        /*
            Method dump skipped, instruction units count: 1698
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.DeletePackageHelper.deletePackageX(java.lang.String, long, int, int, boolean):int");
    }

    private void deleteArtDexoptArtifacts(java.lang.String packageName) {
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
        try {
            try {
                com.android.server.pm.DexOptHelper.getArtManagerLocal().deleteDexoptArtifacts(filteredSnapshot, packageName);
            } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e) {
                android.util.Slog.w("PackageManager", e.toString());
            }
            if (filteredSnapshot != null) {
                filteredSnapshot.close();
            }
        } catch (java.lang.Throwable th) {
            if (filteredSnapshot != null) {
                try {
                    filteredSnapshot.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public boolean deletePackageLIF(java.lang.String packageName, android.os.UserHandle user, boolean deleteCodeAndResources, int[] allUserHandles, int flags, com.android.server.pm.PackageRemovedInfo outInfo, boolean writeSettings) throws java.lang.Throwable {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                try {
                    com.android.server.pm.PackageSetting ps = this.mPm.mPackageManagerServiceExt.adjustPackageSettingInDeletePackageLIF(this.mPm.mSettings.getPackageLPr(packageName), this.mPm.mContext);
                    if (ps == null) {
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                            android.util.Slog.d("PackageManager", "Attempted to remove non-existent package " + packageName);
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return false;
                    }
                    com.android.server.pm.PackageSetting disabledPs = this.mPm.mSettings.getDisabledSystemPkgLPr(ps);
                    if (com.android.server.pm.PackageManagerServiceUtils.isSystemApp(ps) && this.mPm.checkPermission("android.permission.CONTROL_KEYGUARD", packageName, 0) == 0) {
                        android.util.Slog.w("PackageManager", "Attempt to delete keyguard system package " + packageName);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return false;
                    }
                    com.android.server.pm.DeletePackageAction action = mayDeletePackageLocked(outInfo, ps, disabledPs, flags, user);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                        android.util.Slog.d("PackageManager", "deletePackageLI: " + packageName + " user " + user);
                    }
                    if (action == null) {
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                            android.util.Slog.d("PackageManager", "deletePackageLI: action was null");
                        }
                        return false;
                    }
                    try {
                        executeDeletePackageLIF(action, packageName, deleteCodeAndResources, allUserHandles, writeSettings);
                        return true;
                    } catch (com.android.server.pm.SystemDeleteException e) {
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                            android.util.Slog.d("PackageManager", "deletePackageLI: system deletion failure", e);
                        }
                        return false;
                    }
                } catch (java.lang.Throwable th) {
                    e = th;
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw e;
                }
            } catch (java.lang.Throwable th2) {
                e = th2;
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw e;
            }
        }
    }

    public static com.android.server.pm.DeletePackageAction mayDeletePackageLocked(com.android.server.pm.PackageRemovedInfo outInfo, com.android.server.pm.PackageSetting ps, com.android.server.pm.PackageSetting disabledPs, int flags, android.os.UserHandle user) {
        if (ps == null) {
            return null;
        }
        if (com.android.server.pm.PackageManagerServiceUtils.isSystemApp(ps)) {
            boolean deleteAllUsers = true;
            boolean deleteSystem = (flags & 4) != 0;
            if (user != null && user.getIdentifier() != -1) {
                deleteAllUsers = false;
            }
            if ((!deleteSystem || deleteAllUsers) && disabledPs == null) {
                android.util.Slog.w("PackageManager", "Attempt to delete unknown system package " + ps.getPkg().getPackageName());
                return null;
            }
        }
        return new com.android.server.pm.DeletePackageAction(ps, disabledPs, outInfo, flags, user);
    }

    public void executeDeletePackage(com.android.server.pm.DeletePackageAction action, java.lang.String packageName, boolean deleteCodeAndResources, int[] allUserHandles, boolean writeSettings) throws com.android.server.pm.SystemDeleteException {
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            executeDeletePackageLIF(action, packageName, deleteCodeAndResources, allUserHandles, writeSettings);
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

    private void executeDeletePackageLIF(com.android.server.pm.DeletePackageAction action, java.lang.String packageName, boolean deleteCodeAndResources, int[] allUserHandles, boolean writeSettings) throws java.lang.Throwable {
        int[] iArrQueryUsersInstalledOrHasData;
        boolean clearPackageStateAndReturn;
        boolean z;
        android.util.SparseBooleanArray hadSuspendAppsPermission;
        com.android.server.pm.PackageSetting ps = action.mDeletingPs;
        com.android.server.pm.PackageRemovedInfo outInfo = action.mRemovedInfo;
        android.os.UserHandle user = action.mUser;
        int flags = action.mFlags;
        boolean systemApp = com.android.server.pm.PackageManagerServiceUtils.isSystemApp(ps);
        android.util.SparseBooleanArray hadSuspendAppsPermission2 = new android.util.SparseBooleanArray();
        int length = allUserHandles.length;
        int i = 0;
        while (true) {
            boolean z2 = true;
            if (i >= length) {
                break;
            }
            int userId = allUserHandles[i];
            if (this.mPm.checkPermission("android.permission.SUSPEND_APPS", packageName, userId) != 0) {
                z2 = false;
            }
            hadSuspendAppsPermission2.put(userId, z2);
            i++;
        }
        int userId2 = user == null ? -1 : user.getIdentifier();
        if (userId2 == -1) {
            iArrQueryUsersInstalledOrHasData = ps.queryUsersInstalledOrHasData(allUserHandles);
        } else {
            iArrQueryUsersInstalledOrHasData = new int[]{userId2};
        }
        outInfo.mRemovedUsers = iArrQueryUsersInstalledOrHasData;
        outInfo.populateBroadcastUsers(ps);
        outInfo.mDataRemoved = (flags & 1) == 0;
        outInfo.mRemovedPackage = ps.getPackageName();
        outInfo.mInstallerPackageName = ps.getInstallSource().mInstallerPackageName;
        outInfo.mIsStaticSharedLib = (ps.getPkg() == null || ps.getPkg().getStaticSharedLibraryName() == null) ? false : true;
        outInfo.mIsExternal = ps.isExternalStorage();
        outInfo.mRemovedPackageVersionCode = ps.getVersionCode();
        if ((!systemApp || (flags & 4) != 0) && userId2 != -1) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    markPackageUninstalledForUserLPw(ps, user, flags);
                    if (!systemApp) {
                        boolean keepUninstalledPackage = this.mPm.shouldKeepUninstalledPackageLPr(packageName);
                        if (ps.isInstalledOnAnyOtherUser(this.mUserManagerInternal.getUserIds(), userId2) || keepUninstalledPackage) {
                            boolean clearPackageStateAndReturn2 = com.android.server.pm.PackageManagerService.DEBUG_REMOVE;
                            if (clearPackageStateAndReturn2) {
                                android.util.Slog.d("PackageManager", "Still installed by other users");
                            }
                            clearPackageStateAndReturn = true;
                        } else {
                            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                                android.util.Slog.d("PackageManager", "Not installed by other users, full delete");
                            }
                            this.mPm.mSettings.writeKernelMappingLPr(ps);
                            clearPackageStateAndReturn = false;
                        }
                    } else {
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                            android.util.Slog.d("PackageManager", "Deleting system app");
                        }
                        clearPackageStateAndReturn = true;
                    }
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (clearPackageStateAndReturn) {
                this.mRemovePackageHelper.clearPackageStateForUserLIF(ps, userId2, flags);
                outInfo.mUid = ps.getAppId();
                if (outInfo.mDataRemoved) {
                    outInfo.mIsAppIdRemoved = true;
                }
                this.mPm.scheduleWritePackageRestrictions(user);
                return;
            }
        }
        if (systemApp) {
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                android.util.Slog.d("PackageManager", "Removing system package: " + ps.getPackageName());
            }
            deleteInstalledSystemPackage(action, allUserHandles, writeSettings);
            this.mPm.restoreDisabledSystemPackageLIF(action, allUserHandles, writeSettings);
            z = true;
            hadSuspendAppsPermission = hadSuspendAppsPermission2;
        } else {
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                android.util.Slog.d("PackageManager", "Removing non-system package: " + ps.getPackageName());
            }
            if (ps.isIncremental()) {
                deleteArtDexoptArtifacts(packageName);
            }
            z = true;
            hadSuspendAppsPermission = hadSuspendAppsPermission2;
            deleteInstalledPackageLIF(ps, userId2, deleteCodeAndResources, flags, allUserHandles, outInfo, writeSettings);
        }
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        for (int affectedUserId : outInfo.mRemovedUsers) {
            if (hadSuspendAppsPermission.get(affectedUserId)) {
                this.mPm.unsuspendForSuspendingPackage(snapshot, packageName, affectedUserId, z);
                this.mPm.removeAllDistractingPackageRestrictions(snapshot, affectedUserId);
            }
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                if (this.mPm.mPackages.get(ps.getPackageName()) != null) {
                    z = false;
                }
                outInfo.mRemovedForAllUsers = z;
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void deleteInstalledPackageLIF(com.android.server.pm.PackageSetting ps, int userId, boolean deleteCodeAndResources, int flags, int[] allUserHandles, com.android.server.pm.PackageRemovedInfo outInfo, boolean writeSettings) throws java.lang.Throwable {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                try {
                    outInfo.mUid = ps.getAppId();
                    outInfo.mBroadcastAllowList = this.mPm.mAppsFilter.getVisibilityAllowList(this.mPm.snapshotComputer(), ps, allUserHandles, this.mPm.mSettings.getPackagesLocked());
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    this.mRemovePackageHelper.removePackageDataLIF(ps, userId, allUserHandles, outInfo, flags, writeSettings);
                    if (deleteCodeAndResources) {
                        outInfo.mArgs = new com.android.server.pm.CleanUpArgs(ps.getName(), ps.getPathString(), com.android.server.pm.InstructionSets.getAppDexInstructionSets(ps.getPrimaryCpuAbiLegacy(), ps.getSecondaryCpuAbiLegacy()));
                    }
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

    private void markPackageUninstalledForUserLPw(com.android.server.pm.PackageSetting ps, android.os.UserHandle user, int flags) {
        int[] userIds;
        android.util.ArraySet<java.lang.String> enabledComponents;
        android.util.ArraySet<java.lang.String> disabledComponents;
        com.android.server.pm.pkg.ArchiveState archiveState;
        long firstInstallTimeMillis;
        com.android.server.pm.PackageSetting packageSetting = ps;
        int i = 0;
        if (user == null || user.getIdentifier() == -1) {
            userIds = this.mUserManagerInternal.getUserIds();
        } else {
            userIds = new int[]{user.getIdentifier()};
        }
        int[] userIds2 = userIds;
        int length = userIds2.length;
        while (i < length) {
            int nextUserId = userIds2[i];
            if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                android.util.Slog.d("PackageManager", "Marking package:" + ps.getPackageName() + " uninstalled for user:" + nextUserId);
            }
            if ((flags & 1) == 0) {
                enabledComponents = null;
                disabledComponents = null;
            } else {
                android.util.ArraySet<java.lang.String> enabledComponents2 = new android.util.ArraySet<>(packageSetting.readUserState(nextUserId).m8025getEnabledComponents());
                android.util.ArraySet<java.lang.String> disabledComponents2 = new android.util.ArraySet<>(packageSetting.readUserState(nextUserId).m8024getDisabledComponents());
                enabledComponents = enabledComponents2;
                disabledComponents = disabledComponents2;
            }
            if ((flags & 1) == 0) {
                archiveState = null;
            } else {
                archiveState = packageSetting.getUserStateOrDefault(nextUserId).getArchiveState();
            }
            if ((flags & 1) == 0) {
                firstInstallTimeMillis = 0;
            } else {
                firstInstallTimeMillis = packageSetting.getUserStateOrDefault(nextUserId).getFirstInstallTimeMillis();
            }
            long firstInstallTime = firstInstallTimeMillis;
            ps.setUserState(nextUserId, packageSetting.getCeDataInode(nextUserId), packageSetting.getDeDataInode(nextUserId), 0, false, true, true, false, 0, null, false, false, null, enabledComponents, disabledComponents, 0, 0, null, null, firstInstallTime, 0, archiveState);
            packageSetting = ps;
            this.mPm.mPackageManagerServiceExt.onMarkPackageUninstalledForUser(packageSetting, nextUserId);
            i++;
            length = length;
            userIds2 = userIds2;
        }
        this.mPm.mSettings.writeKernelMappingLPr(packageSetting);
    }

    private void deleteInstalledSystemPackage(com.android.server.pm.DeletePackageAction action, int[] allUserHandles, boolean writeSettings) {
        int flags;
        int flags2 = action.mFlags;
        com.android.server.pm.PackageSetting deletedPs = action.mDeletingPs;
        com.android.server.pm.PackageRemovedInfo outInfo = action.mRemovedInfo;
        boolean applyUserRestrictions = outInfo.mOrigUsers != null;
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = deletedPs.getPkg();
        com.android.server.pm.PackageSetting disabledPs = action.mDisabledPs;
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
            android.util.Slog.d("PackageManager", "deleteSystemPackageLI: newPs=" + pkg.getPackageName() + " disabledPs=" + disabledPs);
        }
        android.util.Slog.d("PackageManager", "Deleting system pkg from data partition");
        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE && applyUserRestrictions) {
            android.util.Slog.d("PackageManager", "Remembering install states:");
            for (int userId : allUserHandles) {
                boolean finstalled = com.android.internal.util.ArrayUtils.contains(outInfo.mOrigUsers, userId);
                android.util.Slog.d("PackageManager", "   u=" + userId + " inst=" + finstalled);
            }
        }
        outInfo.mIsRemovedPackageSystemUpdate = true;
        if (disabledPs.getVersionCode() < deletedPs.getVersionCode() || disabledPs.getAppId() != deletedPs.getAppId()) {
            flags = flags2 & (-2);
        } else {
            flags = flags2 | 1;
        }
        com.android.server.pm.PackageManagerTracedLock installLock = this.mPm.mInstallLock.acquireLock();
        try {
            deleteInstalledPackageLIF(deletedPs, -1, true, flags, allUserHandles, outInfo, writeSettings);
            if (installLock != null) {
                installLock.close();
            }
        } finally {
        }
    }

    public void deletePackageVersionedInternal(android.content.pm.VersionedPackage versionedPackage, final android.content.pm.IPackageDeleteObserver2 observer, final int userId, final int deleteFlags, boolean allowSilentUninstall) {
        final int callingUid = android.os.Binder.getCallingUid();
        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.DELETE_PACKAGES", null);
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        final boolean canViewInstantApps = snapshot.canViewInstantApps(callingUid, userId);
        com.android.internal.util.Preconditions.checkNotNull(versionedPackage);
        com.android.internal.util.Preconditions.checkNotNull(observer);
        com.android.internal.util.Preconditions.checkArgumentInRange(versionedPackage.getLongVersionCode(), -1L, Long.MAX_VALUE, "versionCode must be >= -1");
        final java.lang.String packageName = versionedPackage.getPackageName();
        final long versionCode = versionedPackage.getLongVersionCode();
        this.mPm.mPackageManagerServiceExt.onStartInDeletePackageVersionedInternal(packageName);
        try {
            if (((com.android.server.wm.ActivityTaskManagerInternal) this.mPm.mInjector.getLocalService(com.android.server.wm.ActivityTaskManagerInternal.class)).isBaseOfLockedTask(packageName)) {
                observer.onPackageDeleted(packageName, -7, (java.lang.String) null);
                android.util.EventLog.writeEvent(1397638484, "127605586", -1, "");
                return;
            }
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
        final java.lang.String internalPackageName = snapshot.resolveInternalPackageName(packageName, versionCode);
        int uid = android.os.Binder.getCallingUid();
        if (!isOrphaned(snapshot, internalPackageName) && !allowSilentUninstall && !isCallerAllowedToSilentlyUninstall(snapshot, uid, internalPackageName, userId)) {
            this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.pm.DeletePackageHelper.lambda$deletePackageVersionedInternal$0(packageName, observer);
                }
            });
            return;
        }
        final boolean deleteAllUsers = (deleteFlags & 2) != 0;
        final int[] users = deleteAllUsers ? this.mUserManagerInternal.getUserIds() : new int[]{userId};
        if (android.os.UserHandle.getUserId(uid) != userId || (deleteAllUsers && users.length > 1)) {
            this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "deletePackage for user " + userId);
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            for (int user : users) {
                try {
                    if (this.mPm.isPackageDeviceAdmin(packageName, user) && !this.mPm.mPackageManagerServiceExt.allowUninstallDeviceAdminInDeletePackageX(packageName, user)) {
                        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                com.android.server.pm.DeletePackageHelper.lambda$deletePackageVersionedInternal$1(packageName, observer);
                            }
                        });
                        android.os.Binder.restoreCallingIdentity(token);
                        return;
                    } else {
                        if (this.mPm.mProtectedPackages.isPackageDataProtected(user, packageName)) {
                            this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    com.android.server.pm.DeletePackageHelper.lambda$deletePackageVersionedInternal$2(packageName, observer);
                                }
                            });
                            android.os.Binder.restoreCallingIdentity(token);
                            return;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
            android.os.Binder.restoreCallingIdentity(token);
            if (this.mPm.isUserRestricted(userId, "no_uninstall_apps")) {
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        observer.onPackageDeleted(packageName, -3, (java.lang.String) null);
                    }
                });
                this.mPm.mPackageManagerServiceExt.writeMdmLog("006", "0", packageName);
                return;
            }
            if (!deleteAllUsers && snapshot.getBlockUninstallForUser(internalPackageName, userId)) {
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        observer.onPackageDeleted(packageName, -4, (java.lang.String) null);
                    }
                });
                this.mPm.mPackageManagerServiceExt.writeMdmLog("006", "0", packageName);
            } else {
                if (this.mPm.mPackageManagerServiceExt.interceptDeleteInDeletePackageVersionedInternal(this.mPm.mContext, packageName, userId, uid, this.mPm.mHandler, observer, versionedPackage)) {
                    return;
                }
                this.mPm.mPackageManagerServiceExt.beforePostDeleteInDeletePackageVersionedInternal(versionedPackage, userId, packageName);
                if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Slog.d("PackageManager", "deletePackageAsUser: pkg=" + internalPackageName + " user=" + userId + " deleteAllUsers: " + deleteAllUsers + " version=" + (versionCode == -1 ? "VERSION_CODE_HIGHEST" : java.lang.Long.valueOf(versionCode)));
                }
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$deletePackageVersionedInternal$5(internalPackageName, callingUid, canViewInstantApps, deleteAllUsers, versionCode, userId, deleteFlags, users, packageName, observer);
                    }
                });
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    static /* synthetic */ void lambda$deletePackageVersionedInternal$0(java.lang.String packageName, android.content.pm.IPackageDeleteObserver2 observer) {
        try {
            android.content.Intent intent = new android.content.Intent("android.intent.action.UNINSTALL_PACKAGE");
            intent.setData(android.net.Uri.fromParts("package", packageName, null));
            intent.putExtra("android.content.pm.extra.CALLBACK", observer.asBinder());
            observer.onUserActionRequired(intent);
        } catch (android.os.RemoteException e) {
        }
    }

    static /* synthetic */ void lambda$deletePackageVersionedInternal$1(java.lang.String packageName, android.content.pm.IPackageDeleteObserver2 observer) {
        try {
            android.util.Slog.w("PackageManager", "Not removing package " + packageName + ": has active device admin");
            observer.onPackageDeleted(packageName, -2, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
        }
    }

    static /* synthetic */ void lambda$deletePackageVersionedInternal$2(java.lang.String packageName, android.content.pm.IPackageDeleteObserver2 observer) {
        try {
            android.util.Slog.w("PackageManager", "Attempted to delete protected package: " + packageName);
            observer.onPackageDeleted(packageName, -1, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deletePackageVersionedInternal$5(java.lang.String internalPackageName, int callingUid, boolean canViewInstantApps, boolean deleteAllUsers, long versionCode, int userId, int deleteFlags, int[] users, java.lang.String packageName, android.content.pm.IPackageDeleteObserver2 observer) {
        boolean doDeletePackage;
        java.lang.String str;
        boolean z;
        int returnCode;
        int i;
        int i2;
        int[] blockUninstallUserIds;
        int i3;
        int i4;
        int[] childUserIds;
        com.android.server.pm.Computer innerSnapshot = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal packageState = innerSnapshot.getPackageStateInternal(internalPackageName);
        if (packageState == null) {
            doDeletePackage = true;
        } else {
            boolean targetIsInstantApp = packageState.getUserStateOrDefault(android.os.UserHandle.getUserId(callingUid)).isInstantApp();
            boolean doDeletePackage2 = !targetIsInstantApp || canViewInstantApps;
            doDeletePackage = doDeletePackage2;
        }
        if (!doDeletePackage) {
            str = "PackageManager";
            z = true;
            returnCode = -1;
        } else if (!deleteAllUsers) {
            str = "PackageManager";
            z = true;
            returnCode = deletePackageX(internalPackageName, versionCode, userId, deleteFlags, false);
            if (returnCode == 1 && packageState != null) {
                int[] childUserIds2 = this.mUserManagerInternal.getProfileIds(userId, true);
                int length = childUserIds2.length;
                int returnCode2 = returnCode;
                int i5 = 0;
                while (i5 < length) {
                    int childId = childUserIds2[i5];
                    if (childId == userId) {
                        i3 = i5;
                        i4 = length;
                        childUserIds = childUserIds2;
                    } else if (this.mUserManagerInternal.getProfileParentId(childId) != userId) {
                        i3 = i5;
                        i4 = length;
                        childUserIds = childUserIds2;
                    } else if (!packageState.getUserStateOrDefault(childId).isInstalled()) {
                        i3 = i5;
                        i4 = length;
                        childUserIds = childUserIds2;
                    } else {
                        android.content.pm.UserProperties userProperties = this.mUserManagerInternal.getUserProperties(childId);
                        if (userProperties == null || !userProperties.getDeleteAppWithParent()) {
                            i3 = i5;
                            i4 = length;
                            childUserIds = childUserIds2;
                        } else {
                            i3 = i5;
                            i4 = length;
                            childUserIds = childUserIds2;
                            int returnCodeOfChild = deletePackageX(internalPackageName, versionCode, childId, deleteFlags, false);
                            if (returnCodeOfChild != 1) {
                                android.util.Slog.w(str, "Package delete failed for user " + childId + ", returnCode " + returnCodeOfChild);
                                returnCode2 = -8;
                            }
                        }
                    }
                    i5 = i3 + 1;
                    length = i4;
                    childUserIds2 = childUserIds;
                }
                returnCode = returnCode2;
            }
        } else {
            str = "PackageManager";
            int[] blockUninstallUserIds2 = getBlockUninstallForUsers(innerSnapshot, internalPackageName, users);
            if (com.android.internal.util.ArrayUtils.isEmpty(blockUninstallUserIds2)) {
                z = true;
                returnCode = deletePackageX(internalPackageName, versionCode, userId, deleteFlags, false);
            } else {
                z = true;
                int[] blockUninstallUserIds3 = blockUninstallUserIds2;
                int userFlags = deleteFlags & (-3);
                int length2 = users.length;
                int i6 = 0;
                while (i6 < length2) {
                    int userId1 = users[i6];
                    if (com.android.internal.util.ArrayUtils.contains(blockUninstallUserIds3, userId1)) {
                        i = i6;
                        i2 = length2;
                        blockUninstallUserIds = blockUninstallUserIds3;
                    } else {
                        i = i6;
                        i2 = length2;
                        blockUninstallUserIds = blockUninstallUserIds3;
                        int returnCode3 = deletePackageX(internalPackageName, versionCode, userId1, userFlags, false);
                        if (returnCode3 != 1) {
                            android.util.Slog.w(str, "Package delete failed for user " + userId1 + ", returnCode " + returnCode3);
                        }
                    }
                    i6 = i + 1;
                    length2 = i2;
                    blockUninstallUserIds3 = blockUninstallUserIds;
                }
                returnCode = -4;
            }
        }
        java.lang.String str2 = str;
        this.mPm.mPackageManagerServiceExt.writeMdmLog("006", returnCode == z ? "1" : "0", packageName);
        try {
            observer.onPackageDeleted(packageName, returnCode, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
            android.util.Log.i(str2, "Observer no longer exists.");
        }
        this.mPm.schedulePruneUnusedStaticSharedLibraries(z);
        this.mPm.mPackageManagerServiceExt.afterDeleteInDeletePackageVersionedInternal(packageState, packageName, this.mPm.mHandler);
    }

    private boolean isOrphaned(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        return packageState != null && packageState.getInstallSource().mIsOrphaned;
    }

    private boolean isCallerAllowedToSilentlyUninstall(com.android.server.pm.Computer snapshot, int callingUid, java.lang.String pkgName, int userId) {
        if (com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callingUid) || android.os.UserHandle.getAppId(callingUid) == 1000 || this.mPm.mPackageManagerServiceExt.customAllowInIsCallerAllowedToSilentlyUninstall(snapshot, callingUid)) {
            return true;
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (callingUid == snapshot.getPackageUid(snapshot.getInstallerPackageName(pkgName, userId), 0L, callingUserId)) {
            return true;
        }
        for (java.lang.String verifierPackageName : this.mPm.mRequiredVerifierPackages) {
            if (callingUid == snapshot.getPackageUid(verifierPackageName, 0L, callingUserId)) {
                return true;
            }
        }
        if (this.mPm.mRequiredUninstallerPackage == null || callingUid != snapshot.getPackageUid(this.mPm.mRequiredUninstallerPackage, 0L, callingUserId)) {
            return (this.mPm.mStorageManagerPackage != null && callingUid == snapshot.getPackageUid(this.mPm.mStorageManagerPackage, 0L, callingUserId)) || snapshot.checkUidPermission("android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS", callingUid) == 0;
        }
        return true;
    }

    private int[] getBlockUninstallForUsers(com.android.server.pm.Computer snapshot, java.lang.String packageName, int[] userIds) {
        int[] result = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        for (int userId : userIds) {
            if (snapshot.getBlockUninstallForUser(packageName, userId)) {
                result = com.android.internal.util.ArrayUtils.appendInt(result, userId);
            }
        }
        return result;
    }

    private static class TempUserState {
        public final int enabledState;
        public final boolean installed;
        public final java.lang.String lastDisableAppCaller;

        private TempUserState(int enabledState, java.lang.String lastDisableAppCaller, boolean installed) {
            this.enabledState = enabledState;
            this.lastDisableAppCaller = lastDisableAppCaller;
            this.installed = installed;
        }
    }

    public void removeUnusedPackagesLPw(com.android.server.pm.UserManagerService userManager, final int userId) {
        int[] users = userManager.getUserIds();
        int numPackages = this.mPm.mSettings.getPackagesLocked().size();
        for (int index = 0; index < numPackages; index++) {
            com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackagesLocked().valueAt(index);
            if (ps.getPkg() != null) {
                final java.lang.String packageName = ps.getPkg().getPackageName();
                if ((ps.getFlags() & 1) == 0 && android.text.TextUtils.isEmpty(ps.getPkg().getStaticSharedLibraryName()) && android.text.TextUtils.isEmpty(ps.getPkg().getSdkLibraryName())) {
                    boolean keep = this.mPm.shouldKeepUninstalledPackageLPr(packageName);
                    if (!keep) {
                        int i = 0;
                        while (true) {
                            if (i >= users.length) {
                                break;
                            }
                            if (users[i] == userId || !ps.getInstalled(users[i])) {
                                i++;
                            } else {
                                keep = true;
                                break;
                            }
                        }
                    }
                    if (!keep) {
                        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda6
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$removeUnusedPackagesLPw$6(packageName, userId);
                            }
                        });
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeUnusedPackagesLPw$6(java.lang.String packageName, int userId) {
        deletePackageX(packageName, -1L, userId, 0, true);
    }

    public void deleteExistingPackageAsUser(android.content.pm.VersionedPackage versionedPackage, android.content.pm.IPackageDeleteObserver2 observer, int userId) {
        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.DELETE_PACKAGES", null);
        com.android.internal.util.Preconditions.checkNotNull(versionedPackage);
        com.android.internal.util.Preconditions.checkNotNull(observer);
        java.lang.String packageName = versionedPackage.getPackageName();
        long versionCode = versionedPackage.getLongVersionCode();
        int installedForUsersCount = 0;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                java.lang.String internalPkgName = this.mPm.snapshotComputer().resolveInternalPackageName(packageName, versionCode);
                com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(internalPkgName);
                if (ps != null) {
                    int[] installedUsers = ps.queryInstalledUsers(this.mUserManagerInternal.getUserIds(), true);
                    installedForUsersCount = installedUsers.length;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (installedForUsersCount > 1) {
            deletePackageVersionedInternal(versionedPackage, observer, userId, this.mPm.mPackageManagerServiceExt.adjustDeleteFlagInDeleteExistingPackageAsUser(0, versionedPackage), true);
        } else {
            try {
                observer.onPackageDeleted(packageName, -1, (java.lang.String) null);
            } catch (android.os.RemoteException e) {
            }
        }
    }
}
