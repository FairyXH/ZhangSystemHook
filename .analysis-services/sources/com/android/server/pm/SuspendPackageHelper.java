package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class SuspendPackageHelper {
    private static final java.lang.String SYSTEM_EXEMPT_FROM_SUSPENSION = "system_exempt_from_suspension";
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final com.android.server.pm.PackageManagerServiceInjector mInjector;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.ProtectedPackages mProtectedPackages;

    SuspendPackageHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.PackageManagerServiceInjector injector, com.android.server.pm.BroadcastHelper broadcastHelper, com.android.server.pm.ProtectedPackages protectedPackages) {
        this.mPm = pm;
        this.mInjector = injector;
        this.mBroadcastHelper = broadcastHelper;
        this.mProtectedPackages = protectedPackages;
    }

    java.lang.String[] setPackagesSuspended(com.android.server.pm.Computer snapshot, java.lang.String[] packageNames, final boolean suspended, android.os.PersistableBundle appExtras, android.os.PersistableBundle launcherExtras, android.content.pm.SuspendDialogInfo dialogInfo, final android.content.pm.UserPackage suspendingPackage, final int targetUserId, int callingUid, boolean quarantined) throws java.lang.Throwable {
        boolean[] zArrCanSuspendPackageForUser;
        android.util.IntArray changedUids;
        java.util.List<java.lang.String> unmodifiablePackages;
        com.android.server.pm.pkg.SuspendParams newSuspendParams;
        com.android.server.pm.Computer computer = snapshot;
        java.lang.String[] strArr = packageNames;
        int i = callingUid;
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            return strArr;
        }
        if (suspended && !quarantined && !isSuspendAllowedForUser(computer, targetUserId, i)) {
            android.util.Slog.w("PackageManager", "Cannot suspend due to restrictions on user " + targetUserId);
            return strArr;
        }
        com.android.server.pm.pkg.SuspendParams newSuspendParams2 = suspended ? new com.android.server.pm.pkg.SuspendParams(dialogInfo, appExtras, launcherExtras, quarantined) : null;
        java.util.List<java.lang.String> unmodifiablePackages2 = new java.util.ArrayList<>(strArr.length);
        java.util.List<java.lang.String> notifyPackagesList = new java.util.ArrayList<>(strArr.length);
        android.util.IntArray notifyUids = new android.util.IntArray(strArr.length);
        final android.util.ArraySet<java.lang.String> changedPackagesList = new android.util.ArraySet<>(strArr.length);
        android.util.IntArray changedUids2 = new android.util.IntArray(strArr.length);
        if (suspended) {
            zArrCanSuspendPackageForUser = canSuspendPackageForUser(computer, strArr, targetUserId, i);
        } else {
            zArrCanSuspendPackageForUser = null;
        }
        boolean[] canSuspend = zArrCanSuspendPackageForUser;
        int i2 = 0;
        while (i2 < strArr.length) {
            java.lang.String packageName = strArr[i2];
            if (suspendingPackage.packageName.equals(packageName) && suspendingPackage.userId == targetUserId) {
                android.util.Slog.w("PackageManager", "Suspending package: " + suspendingPackage + " trying to " + (suspended ? "" : "un") + "suspend itself. Ignoring");
                unmodifiablePackages2.add(packageName);
                newSuspendParams = newSuspendParams2;
            } else {
                com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(packageName);
                if (packageState == null || !packageState.getUserStateOrDefault(targetUserId).isInstalled() || computer.shouldFilterApplication(packageState, i, targetUserId)) {
                    newSuspendParams = newSuspendParams2;
                    android.util.Slog.w("PackageManager", "Could not find package setting for package: " + packageName + ". Skipping suspending/un-suspending.");
                    unmodifiablePackages2.add(packageName);
                } else if (canSuspend != null && !canSuspend[i2]) {
                    unmodifiablePackages2.add(packageName);
                    newSuspendParams = newSuspendParams2;
                } else {
                    com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> suspendParamsMap = packageState.getUserStateOrDefault(targetUserId).getSuspendParams();
                    com.android.server.pm.pkg.SuspendParams oldSuspendParams = suspendParamsMap == null ? null : suspendParamsMap.get(suspendingPackage);
                    boolean zEquals = java.util.Objects.equals(oldSuspendParams, newSuspendParams2);
                    newSuspendParams = newSuspendParams2;
                    boolean changed = !zEquals;
                    if (suspended && !changed) {
                        notifyPackagesList.add(packageName);
                        notifyUids.add(android.os.UserHandle.getUid(targetUserId, packageState.getAppId()));
                    } else {
                        boolean packageUnsuspended = !suspended && com.android.internal.util.CollectionUtils.size(suspendParamsMap) == 1 && suspendParamsMap.containsKey(suspendingPackage);
                        if (suspended || packageUnsuspended) {
                            notifyPackagesList.add(packageName);
                            notifyUids.add(android.os.UserHandle.getUid(targetUserId, packageState.getAppId()));
                        }
                        if (!changed) {
                            android.util.Slog.w("PackageManager", "No change is needed for package: " + packageName + ". Skipping suspending/un-suspending.");
                        } else {
                            changedPackagesList.add(packageName);
                            changedUids2.add(android.os.UserHandle.getUid(targetUserId, packageState.getAppId()));
                        }
                    }
                }
            }
            i2++;
            computer = snapshot;
            strArr = packageNames;
            i = callingUid;
            newSuspendParams2 = newSuspendParams;
        }
        final com.android.server.pm.pkg.SuspendParams newSuspendParams3 = newSuspendParams2;
        this.mPm.commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.SuspendPackageHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.SuspendPackageHelper.lambda$setPackagesSuspended$0(changedPackagesList, targetUserId, suspended, suspendingPackage, newSuspendParams3, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
            }
        });
        com.android.server.pm.Computer newSnapshot = this.mPm.snapshotComputer();
        if (notifyPackagesList.isEmpty()) {
            changedUids = changedUids2;
            unmodifiablePackages = unmodifiablePackages2;
        } else {
            java.lang.String[] changedPackages = (java.lang.String[]) notifyPackagesList.toArray(new java.lang.String[0]);
            com.android.server.pm.BroadcastHelper broadcastHelper = this.mBroadcastHelper;
            java.lang.String str = suspended ? "android.intent.action.PACKAGES_SUSPENDED" : "android.intent.action.PACKAGES_UNSUSPENDED";
            int[] array = notifyUids.toArray();
            changedUids = changedUids2;
            unmodifiablePackages = unmodifiablePackages2;
            broadcastHelper.sendPackagesSuspendedOrUnsuspendedForUser(newSnapshot, str, changedPackages, array, quarantined, targetUserId);
            this.mBroadcastHelper.sendMyPackageSuspendedOrUnsuspended(newSnapshot, changedPackages, suspended, targetUserId);
            this.mPm.scheduleWritePackageRestrictions(targetUserId);
        }
        if (!changedPackagesList.isEmpty()) {
            this.mBroadcastHelper.sendPackagesSuspendedOrUnsuspendedForUser(newSnapshot, "android.intent.action.PACKAGES_SUSPENSION_CHANGED", (java.lang.String[]) changedPackagesList.toArray(new java.lang.String[0]), changedUids.toArray(), quarantined, targetUserId);
        }
        return (java.lang.String[]) unmodifiablePackages.toArray(new java.lang.String[0]);
    }

    static /* synthetic */ void lambda$setPackagesSuspended$0(android.util.ArraySet changedPackagesList, int targetUserId, boolean suspended, android.content.pm.UserPackage suspendingPackage, com.android.server.pm.pkg.SuspendParams newSuspendParams, com.android.server.pm.pkg.mutate.PackageStateMutator mutator) {
        int size = changedPackagesList.size();
        for (int index = 0; index < size; index++) {
            java.lang.String packageName = (java.lang.String) changedPackagesList.valueAt(index);
            com.android.server.pm.pkg.mutate.PackageUserStateWrite userState = mutator.forPackage(packageName).userState(targetUserId);
            if (suspended) {
                userState.putSuspendParams(suspendingPackage, newSuspendParams);
            } else {
                userState.removeSuspension(suspendingPackage);
            }
        }
    }

    java.lang.String[] getUnsuspendablePackagesForUser(com.android.server.pm.Computer snapshot, java.lang.String[] packageNames, int targetUserId, int callingUid) {
        if (!isSuspendAllowedForUser(snapshot, targetUserId, callingUid)) {
            android.util.Slog.w("PackageManager", "Cannot suspend due to restrictions on user " + targetUserId);
            return packageNames;
        }
        android.util.ArraySet<java.lang.String> unactionablePackages = new android.util.ArraySet<>();
        boolean[] canSuspend = canSuspendPackageForUser(snapshot, packageNames, targetUserId, callingUid);
        for (int i = 0; i < packageNames.length; i++) {
            if (!canSuspend[i]) {
                unactionablePackages.add(packageNames[i]);
            } else {
                com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageNames[i], callingUid, targetUserId);
                if (packageState == null) {
                    android.util.Slog.w("PackageManager", "Could not find package setting for package: " + packageNames[i]);
                    unactionablePackages.add(packageNames[i]);
                }
            }
        }
        return (java.lang.String[]) unactionablePackages.toArray(new java.lang.String[unactionablePackages.size()]);
    }

    static android.os.Bundle getSuspendedPackageAppExtras(com.android.server.pm.Computer snapshot, java.lang.String packageName, int userId, int callingUid) {
        com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateInternal(packageName, callingUid);
        if (ps == null) {
            return null;
        }
        com.android.server.pm.pkg.PackageUserStateInternal pus = ps.getUserStateOrDefault(userId);
        android.os.Bundle allExtras = new android.os.Bundle();
        if (pus.isSuspended()) {
            for (int i = 0; i < pus.getSuspendParams().size(); i++) {
                com.android.server.pm.pkg.SuspendParams params = pus.getSuspendParams().valueAt(i);
                if (params != null && params.getAppExtras() != null) {
                    allExtras.putAll(params.getAppExtras());
                }
            }
        }
        int i2 = allExtras.size();
        if (i2 > 0) {
            return allExtras;
        }
        return null;
    }

    void removeSuspensionsBySuspendingPackage(com.android.server.pm.Computer snapshot, java.lang.String[] packagesToChange, java.util.function.Predicate<android.content.pm.UserPackage> suspendingPackagePredicate, final int targetUserId) {
        android.util.ArraySet<android.content.pm.UserPackage> suspendingPkgsToCommit;
        java.lang.String[] strArr = packagesToChange;
        java.util.List<java.lang.String> unsuspendedPackages = new java.util.ArrayList<>();
        android.util.IntArray unsuspendedUids = new android.util.IntArray();
        final android.util.ArrayMap<java.lang.String, android.util.ArraySet<android.content.pm.UserPackage>> pkgToSuspendingPkgsToCommit = new android.util.ArrayMap<>();
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            java.lang.String packageName = strArr[i];
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
            com.android.server.pm.pkg.PackageUserStateInternal packageUserState = packageState != null ? packageState.getUserStateOrDefault(targetUserId) : null;
            if (packageUserState != null && packageUserState.isSuspended()) {
                com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> suspendParamsMap = packageUserState.getSuspendParams();
                int countRemoved = 0;
                int index = 0;
                while (index < suspendParamsMap.size()) {
                    android.content.pm.UserPackage suspendingPackage = suspendParamsMap.keyAt(index);
                    com.android.server.pm.pkg.PackageUserStateInternal packageUserState2 = packageUserState;
                    if (suspendingPackagePredicate.test(suspendingPackage)) {
                        android.util.ArraySet<android.content.pm.UserPackage> suspendingPkgsToCommit2 = pkgToSuspendingPkgsToCommit.get(packageName);
                        if (suspendingPkgsToCommit2 != null) {
                            suspendingPkgsToCommit = suspendingPkgsToCommit2;
                        } else {
                            suspendingPkgsToCommit = new android.util.ArraySet<>();
                            pkgToSuspendingPkgsToCommit.put(packageName, suspendingPkgsToCommit);
                        }
                        suspendingPkgsToCommit.add(suspendingPackage);
                        countRemoved++;
                    }
                    index++;
                    packageUserState = packageUserState2;
                }
                if (countRemoved == suspendParamsMap.size()) {
                    unsuspendedPackages.add(packageState.getPackageName());
                    unsuspendedUids.add(android.os.UserHandle.getUid(targetUserId, packageState.getAppId()));
                }
            }
            i++;
            strArr = packagesToChange;
        }
        this.mPm.commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.SuspendPackageHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.SuspendPackageHelper.lambda$removeSuspensionsBySuspendingPackage$1(pkgToSuspendingPkgsToCommit, targetUserId, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
            }
        });
        this.mPm.scheduleWritePackageRestrictions(targetUserId);
        com.android.server.pm.Computer newSnapshot = this.mPm.snapshotComputer();
        if (!unsuspendedPackages.isEmpty()) {
            java.lang.String[] packageArray = (java.lang.String[]) unsuspendedPackages.toArray(new java.lang.String[unsuspendedPackages.size()]);
            this.mBroadcastHelper.sendMyPackageSuspendedOrUnsuspended(newSnapshot, packageArray, false, targetUserId);
            this.mBroadcastHelper.sendPackagesSuspendedOrUnsuspendedForUser(newSnapshot, "android.intent.action.PACKAGES_UNSUSPENDED", packageArray, unsuspendedUids.toArray(), false, targetUserId);
        }
    }

    static /* synthetic */ void lambda$removeSuspensionsBySuspendingPackage$1(android.util.ArrayMap pkgToSuspendingPkgsToCommit, int targetUserId, com.android.server.pm.pkg.mutate.PackageStateMutator mutator) {
        for (int mapIndex = 0; mapIndex < pkgToSuspendingPkgsToCommit.size(); mapIndex++) {
            java.lang.String packageName = (java.lang.String) pkgToSuspendingPkgsToCommit.keyAt(mapIndex);
            android.util.ArraySet<android.content.pm.UserPackage> packagesToRemove = (android.util.ArraySet) pkgToSuspendingPkgsToCommit.valueAt(mapIndex);
            com.android.server.pm.pkg.mutate.PackageUserStateWrite userState = mutator.forPackage(packageName).userState(targetUserId);
            for (int setIndex = 0; setIndex < packagesToRemove.size(); setIndex++) {
                userState.removeSuspension(packagesToRemove.valueAt(setIndex));
            }
        }
    }

    android.os.Bundle getSuspendedPackageLauncherExtras(com.android.server.pm.Computer snapshot, java.lang.String packageName, int userId, int callingUid) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName, callingUid);
        if (packageState == null) {
            return null;
        }
        android.os.Bundle allExtras = new android.os.Bundle();
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        if (userState.isSuspended()) {
            for (int i = 0; i < userState.getSuspendParams().size(); i++) {
                com.android.server.pm.pkg.SuspendParams params = userState.getSuspendParams().valueAt(i);
                if (params != null && params.getLauncherExtras() != null) {
                    allExtras.putAll(params.getLauncherExtras());
                }
            }
        }
        int i2 = allExtras.size();
        if (i2 > 0) {
            return allExtras;
        }
        return null;
    }

    boolean isPackageSuspended(com.android.server.pm.Computer snapshot, java.lang.String packageName, int userId, int callingUid) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName, callingUid);
        return packageState != null && packageState.getUserStateOrDefault(userId).isSuspended();
    }

    android.content.pm.UserPackage getSuspendingPackage(com.android.server.pm.Computer snapshot, java.lang.String suspendedPackage, int userId, int callingUid) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(suspendedPackage, callingUid);
        if (packageState == null) {
            return null;
        }
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        if (!userState.isSuspended()) {
            return null;
        }
        android.content.pm.UserPackage suspendingPackage = null;
        android.content.pm.UserPackage suspendedBySystem = null;
        android.content.pm.UserPackage qasPackage = null;
        for (int i = 0; i < userState.getSuspendParams().size(); i++) {
            android.content.pm.UserPackage suspendingPackage2 = userState.getSuspendParams().keyAt(i);
            suspendingPackage = suspendingPackage2;
            com.android.server.pm.pkg.SuspendParams suspendParams = userState.getSuspendParams().valueAt(i);
            if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(suspendingPackage.packageName)) {
                suspendedBySystem = suspendingPackage;
            }
            if (suspendParams.isQuarantined() && qasPackage == null) {
                qasPackage = suspendingPackage;
            }
        }
        if (qasPackage != null) {
            return qasPackage;
        }
        if (suspendedBySystem != null) {
            return suspendedBySystem;
        }
        return suspendingPackage;
    }

    android.content.pm.SuspendDialogInfo getSuspendedDialogInfo(com.android.server.pm.Computer snapshot, java.lang.String suspendedPackage, android.content.pm.UserPackage suspendingPackage, int userId, int callingUid) {
        com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> suspendParamsMap;
        com.android.server.pm.pkg.SuspendParams suspendParams;
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(suspendedPackage, callingUid);
        if (packageState == null) {
            return null;
        }
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        if (!userState.isSuspended() || (suspendParamsMap = userState.getSuspendParams()) == null || (suspendParams = suspendParamsMap.get(suspendingPackage)) == null) {
            return null;
        }
        return suspendParams.getDialogInfo();
    }

    boolean isSuspendAllowedForUser(com.android.server.pm.Computer snapshot, int userId, int callingUid) {
        com.android.server.pm.UserManagerService userManager = this.mInjector.getUserManagerService();
        return isCallerDeviceOrProfileOwner(snapshot, userId, callingUid) || !(userManager.hasUserRestriction("no_control_apps", userId) || userManager.hasUserRestriction("no_uninstall_apps", userId));
    }

    boolean[] canSuspendPackageForUser(com.android.server.pm.Computer snapshot, java.lang.String[] packageNames, int targetUserId, int callingUid) {
        long token;
        com.android.server.pm.Computer computer = snapshot;
        java.lang.String[] strArr = packageNames;
        boolean[] canSuspend = new boolean[strArr.length];
        boolean isCallerOwner = isCallerDeviceOrProfileOwner(computer, targetUserId, callingUid);
        long token2 = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.pm.DefaultAppProvider defaultAppProvider = this.mInjector.getDefaultAppProvider();
            java.lang.String activeLauncherPackageName = defaultAppProvider.getDefaultHome(targetUserId);
            java.lang.String dialerPackageName = defaultAppProvider.getDefaultDialer(targetUserId);
            java.lang.String requiredInstallerPackage = getKnownPackageName(computer, 2, targetUserId);
            java.lang.String requiredUninstallerPackage = getKnownPackageName(computer, 3, targetUserId);
            java.lang.String requiredVerifierPackage = getKnownPackageName(computer, 4, targetUserId);
            java.lang.String requiredPermissionControllerPackage = getKnownPackageName(computer, 7, targetUserId);
            int i = 0;
            while (i < strArr.length) {
                canSuspend[i] = false;
                java.lang.String packageName = strArr[i];
                token = token2;
                if (this.mPm.isPackageDeviceAdmin(packageName, targetUserId)) {
                    try {
                        android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": has an active device admin");
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                } else if (packageName.equals(activeLauncherPackageName)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": contains the active launcher");
                } else if (packageName.equals(requiredInstallerPackage)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": required for package installation");
                } else if (packageName.equals(requiredUninstallerPackage)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": required for package uninstallation");
                } else if (packageName.equals(requiredVerifierPackage)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": required for package verification");
                } else if (packageName.equals(dialerPackageName)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": is the default dialer");
                } else if (packageName.equals(requiredPermissionControllerPackage)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": required for permissions management");
                } else if (this.mProtectedPackages.isPackageStateProtected(targetUserId, packageName)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": protected package");
                } else if (!isCallerOwner && computer.getBlockUninstall(targetUserId, packageName)) {
                    android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": blocked by admin");
                } else {
                    com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(packageName);
                    com.android.server.pm.pkg.AndroidPackage pkg = packageState == null ? null : packageState.getPkg();
                    if (pkg != null) {
                        int uid = android.os.UserHandle.getUid(targetUserId, packageState.getAppId());
                        if (pkg.isSdkLibrary()) {
                            android.util.Slog.w("PackageManager", "Cannot suspend package: " + packageName + " providing SDK library: " + pkg.getSdkLibraryName());
                        } else if (pkg.isStaticSharedLibrary()) {
                            android.util.Slog.w("PackageManager", "Cannot suspend package: " + packageName + " providing static shared library: " + pkg.getStaticSharedLibraryName());
                        } else if (exemptFromSuspensionByAppOp(uid, packageName)) {
                            android.util.Slog.w("PackageManager", "Cannot suspend package \"" + packageName + "\": has OP_SYSTEM_EXEMPT_FROM_SUSPENSION set");
                        }
                    }
                    if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
                        android.util.Slog.w("PackageManager", "Cannot suspend the platform package: " + packageName);
                    } else {
                        canSuspend[i] = true;
                    }
                }
                i++;
                computer = snapshot;
                strArr = packageNames;
                token2 = token;
            }
            android.os.Binder.restoreCallingIdentity(token2);
            return canSuspend;
        } catch (java.lang.Throwable th2) {
            th = th2;
            token = token2;
        }
    }

    private boolean exemptFromSuspensionByAppOp(int uid, java.lang.String packageName) {
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mInjector.getSystemService(android.app.AppOpsManager.class);
        return appOpsManager.checkOpNoThrow(124, uid, packageName) == 0;
    }

    private java.lang.String getKnownPackageName(com.android.server.pm.Computer snapshot, int knownPackage, int userId) {
        java.lang.String[] knownPackages = this.mPm.getKnownPackageNamesInternal(snapshot, knownPackage, userId);
        if (knownPackages.length > 0) {
            return knownPackages[0];
        }
        return null;
    }

    private boolean isCallerDeviceOrProfileOwner(com.android.server.pm.Computer snapshot, int targetUserId, int callingUid) {
        if (callingUid == 1000) {
            return true;
        }
        java.lang.String ownerPackage = this.mProtectedPackages.getDeviceOwnerOrProfileOwnerPackage(targetUserId);
        return ownerPackage != null && callingUid == snapshot.getPackageUidInternal(ownerPackage, 0L, targetUserId, callingUid);
    }
}
