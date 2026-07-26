package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DistractingPackageHelper {
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final com.android.server.pm.PackageManagerService mPm;
    private final com.android.server.pm.SuspendPackageHelper mSuspendPackageHelper;

    DistractingPackageHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.BroadcastHelper broadcastHelper, com.android.server.pm.SuspendPackageHelper suspendPackageHelper) {
        this.mPm = pm;
        this.mBroadcastHelper = broadcastHelper;
        this.mSuspendPackageHelper = suspendPackageHelper;
    }

    java.lang.String[] setDistractingPackageRestrictionsAsUser(com.android.server.pm.Computer snapshot, java.lang.String[] packageNames, final int restrictionFlags, final int userId, int callingUid) {
        boolean[] zArrCanSuspendPackageForUser;
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            return packageNames;
        }
        if (restrictionFlags != 0 && !this.mSuspendPackageHelper.isSuspendAllowedForUser(snapshot, userId, callingUid)) {
            android.util.Slog.w("PackageManager", "Cannot restrict packages due to restrictions on user " + userId);
            return packageNames;
        }
        java.util.List<java.lang.String> changedPackagesList = new java.util.ArrayList<>(packageNames.length);
        android.util.IntArray changedUids = new android.util.IntArray(packageNames.length);
        java.util.List<java.lang.String> unactionedPackages = new java.util.ArrayList<>(packageNames.length);
        final android.util.ArraySet<java.lang.String> changesToCommit = new android.util.ArraySet<>();
        if (restrictionFlags != 0) {
            zArrCanSuspendPackageForUser = this.mSuspendPackageHelper.canSuspendPackageForUser(snapshot, packageNames, userId, callingUid);
        } else {
            zArrCanSuspendPackageForUser = null;
        }
        boolean[] canRestrict = zArrCanSuspendPackageForUser;
        if (com.android.server.pm.PackageManagerService.DEBUG_SETTINGS) {
            android.util.Slog.d("PackageManager", "setDistractingPackageRestrictionsAsUser start: " + packageNames.length + ", " + restrictionFlags + ", " + userId + ", from " + android.os.Binder.getCallingUid() + ", " + android.os.Binder.getCallingPid());
        }
        for (int i = 0; i < packageNames.length; i++) {
            java.lang.String packageName = packageNames[i];
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState == null) {
                android.util.Slog.w("PackageManager", "Could not find package setting for package: " + packageName + ". Skipping...");
                unactionedPackages.add(packageName);
            } else if (canRestrict != null && !canRestrict[i]) {
                unactionedPackages.add(packageName);
            } else {
                int oldDistractionFlags = packageState.getUserStateOrDefault(userId).getDistractionFlags();
                if (restrictionFlags != oldDistractionFlags) {
                    changedPackagesList.add(packageName);
                    changedUids.add(android.os.UserHandle.getUid(userId, packageState.getAppId()));
                    changesToCommit.add(packageName);
                }
            }
        }
        this.mPm.commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.DistractingPackageHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.DistractingPackageHelper.lambda$setDistractingPackageRestrictionsAsUser$0(changesToCommit, userId, restrictionFlags, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
            }
        });
        if (com.android.server.pm.PackageManagerService.DEBUG_SETTINGS) {
            android.util.Slog.d("PackageManager", "setDistractingPackageRestrictionsAsUser end");
        }
        if (!changedPackagesList.isEmpty()) {
            java.lang.String[] changedPackages = (java.lang.String[]) changedPackagesList.toArray(new java.lang.String[changedPackagesList.size()]);
            this.mBroadcastHelper.sendDistractingPackagesChanged(this.mPm.snapshotComputer(), changedPackages, changedUids.toArray(), userId, restrictionFlags);
            this.mPm.scheduleWritePackageRestrictions(userId);
        }
        return (java.lang.String[]) unactionedPackages.toArray(new java.lang.String[0]);
    }

    static /* synthetic */ void lambda$setDistractingPackageRestrictionsAsUser$0(android.util.ArraySet changesToCommit, int userId, int restrictionFlags, com.android.server.pm.pkg.mutate.PackageStateMutator mutator) {
        int size = changesToCommit.size();
        for (int index = 0; index < size; index++) {
            mutator.forPackage((java.lang.String) changesToCommit.valueAt(index)).userState(userId).setDistractionFlags(restrictionFlags);
        }
    }

    int[] getDistractingPackageRestrictionsAsUser(com.android.server.pm.Computer snapshot, java.lang.String[] packageNames, int userId, int callingUid) {
        int[] res = new int[packageNames.length];
        java.util.Arrays.fill(res, -1);
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            return res;
        }
        for (int i = 0; i < packageNames.length; i++) {
            java.lang.String packageName = packageNames[i];
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState != null) {
                res[i] = packageState.getUserStateOrDefault(userId).getDistractionFlags();
            }
        }
        return res;
    }

    void removeDistractingPackageRestrictions(com.android.server.pm.Computer snapshot, java.lang.String[] packagesToChange, final int userId) {
        if (com.android.internal.util.ArrayUtils.isEmpty(packagesToChange)) {
            return;
        }
        final java.util.List<java.lang.String> changedPackages = new java.util.ArrayList<>(packagesToChange.length);
        android.util.IntArray changedUids = new android.util.IntArray(packagesToChange.length);
        if (com.android.server.pm.PackageManagerService.DEBUG_SETTINGS) {
            android.util.Slog.d("PackageManager", "removeDistractingPackageRestrictions start: " + (packagesToChange == null ? "null" : java.lang.String.valueOf(packagesToChange.length)) + ", " + userId);
        }
        for (java.lang.String packageName : packagesToChange) {
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateInternal(packageName);
            if (ps != null && ps.getUserStateOrDefault(userId).getDistractionFlags() != 0) {
                changedPackages.add(ps.getPackageName());
                changedUids.add(android.os.UserHandle.getUid(userId, ps.getAppId()));
            }
        }
        this.mPm.commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.DistractingPackageHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.DistractingPackageHelper.lambda$removeDistractingPackageRestrictions$1(changedPackages, userId, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
            }
        });
        if (!changedPackages.isEmpty()) {
            java.lang.String[] packageArray = (java.lang.String[]) changedPackages.toArray(new java.lang.String[changedPackages.size()]);
            this.mBroadcastHelper.sendDistractingPackagesChanged(this.mPm.snapshotComputer(), packageArray, changedUids.toArray(), userId, 0);
            this.mPm.scheduleWritePackageRestrictions(userId);
        }
    }

    static /* synthetic */ void lambda$removeDistractingPackageRestrictions$1(java.util.List changedPackages, int userId, com.android.server.pm.pkg.mutate.PackageStateMutator mutator) {
        for (int index = 0; index < changedPackages.size(); index++) {
            mutator.forPackage((java.lang.String) changedPackages.get(index)).userState(userId).setDistractionFlags(0);
        }
    }
}
