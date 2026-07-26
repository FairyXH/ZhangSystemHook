package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionManagerServiceTestingShim implements com.android.server.pm.permission.PermissionManagerServiceInterface {
    private com.android.server.pm.permission.PermissionManagerServiceInterface mNewImplementation;
    private com.android.server.pm.permission.PermissionManagerServiceInterface mOldImplementation;

    public PermissionManagerServiceTestingShim(com.android.server.pm.permission.PermissionManagerServiceInterface oldImpl, com.android.server.pm.permission.PermissionManagerServiceInterface newImpl) {
        this.mOldImplementation = oldImpl;
        this.mNewImplementation = newImpl;
    }

    private void signalImplDifference(java.lang.String message) {
    }

    public byte[] backupRuntimePermissions(int userId) {
        byte[] oldVal = this.mOldImplementation.backupRuntimePermissions(userId);
        byte[] newVal = this.mNewImplementation.backupRuntimePermissions(userId);
        if (!java.util.Arrays.equals(oldVal, newVal)) {
            signalImplDifference("backupRuntimePermissions");
        }
        return newVal;
    }

    public void restoreRuntimePermissions(byte[] backup, int userId) {
        this.mOldImplementation.backupRuntimePermissions(userId);
        this.mNewImplementation.backupRuntimePermissions(userId);
    }

    public void restoreDelayedRuntimePermissions(java.lang.String packageName, int userId) {
        this.mOldImplementation.restoreDelayedRuntimePermissions(packageName, userId);
        this.mNewImplementation.restoreDelayedRuntimePermissions(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        this.mOldImplementation.dump(fd, pw, args);
        this.mNewImplementation.dump(fd, pw, args);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        java.util.List<android.content.pm.PermissionGroupInfo> oldVal = this.mOldImplementation.getAllPermissionGroups(flags);
        java.util.List<android.content.pm.PermissionGroupInfo> newVal = this.mNewImplementation.getAllPermissionGroups(flags);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getAllPermissionGroups");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        android.content.pm.PermissionGroupInfo oldVal = this.mOldImplementation.getPermissionGroupInfo(groupName, flags);
        android.content.pm.PermissionGroupInfo newVal = this.mNewImplementation.getPermissionGroupInfo(groupName, flags);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getPermissionGroupInfo");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permName, int flags, java.lang.String opPackageName) {
        android.content.pm.PermissionInfo oldVal = this.mOldImplementation.getPermissionInfo(permName, flags, opPackageName);
        android.content.pm.PermissionInfo newVal = this.mNewImplementation.getPermissionInfo(permName, flags, opPackageName);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getPermissionInfo");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String groupName, int flags) {
        java.util.List<android.content.pm.PermissionInfo> oldVal = this.mOldImplementation.queryPermissionsByGroup(groupName, flags);
        java.util.List<android.content.pm.PermissionInfo> newVal = this.mNewImplementation.queryPermissionsByGroup(groupName, flags);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("queryPermissionsByGroup");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(android.content.pm.PermissionInfo info, boolean async) {
        boolean oldVal = this.mOldImplementation.addPermission(info, async);
        boolean newVal = this.mNewImplementation.addPermission(info, async);
        if (!java.util.Objects.equals(java.lang.Boolean.valueOf(oldVal), java.lang.Boolean.valueOf(newVal))) {
            signalImplDifference("addPermission");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(java.lang.String permName) {
        this.mOldImplementation.removePermission(permName);
        this.mNewImplementation.removePermission(permName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        int oldVal = this.mOldImplementation.getPermissionFlags(packageName, permName, deviceId, userId);
        int newVal = this.mNewImplementation.getPermissionFlags(packageName, permName, deviceId, userId);
        if (!java.util.Objects.equals(java.lang.Integer.valueOf(oldVal), java.lang.Integer.valueOf(newVal))) {
            signalImplDifference("getPermissionFlags");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(java.lang.String packageName, java.lang.String permName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, java.lang.String deviceId, int userId) {
        this.mOldImplementation.updatePermissionFlags(packageName, permName, flagMask, flagValues, checkAdjustPolicyFlagPermission, deviceId, userId);
        this.mNewImplementation.updatePermissionFlags(packageName, permName, flagMask, flagValues, checkAdjustPolicyFlagPermission, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) {
        this.mOldImplementation.updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
        this.mNewImplementation.updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.mOldImplementation.addOnPermissionsChangeListener(listener);
        this.mNewImplementation.addOnPermissionsChangeListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.mOldImplementation.removeOnPermissionsChangeListener(listener);
        this.mNewImplementation.removeOnPermissionsChangeListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        boolean oldVal = this.mOldImplementation.addAllowlistedRestrictedPermission(packageName, permName, flags, userId);
        boolean newVal = this.mNewImplementation.addAllowlistedRestrictedPermission(packageName, permName, flags, userId);
        if (!java.util.Objects.equals(java.lang.Boolean.valueOf(oldVal), java.lang.Boolean.valueOf(newVal))) {
            signalImplDifference("addAllowlistedRestrictedPermission");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String packageName, int flags, int userId) {
        java.util.List<java.lang.String> oldVal = this.mOldImplementation.getAllowlistedRestrictedPermissions(packageName, flags, userId);
        java.util.List<java.lang.String> newVal = this.mNewImplementation.getAllowlistedRestrictedPermissions(packageName, flags, userId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getAllowlistedRestrictedPermissions");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        boolean oldVal = this.mOldImplementation.removeAllowlistedRestrictedPermission(packageName, permName, flags, userId);
        boolean newVal = this.mNewImplementation.removeAllowlistedRestrictedPermission(packageName, permName, flags, userId);
        if (!java.util.Objects.equals(java.lang.Boolean.valueOf(oldVal), java.lang.Boolean.valueOf(newVal))) {
            signalImplDifference("removeAllowlistedRestrictedPermission");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        this.mOldImplementation.grantRuntimePermission(packageName, permName, deviceId, userId);
        this.mNewImplementation.grantRuntimePermission(packageName, permName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId, java.lang.String reason) {
        this.mOldImplementation.revokeRuntimePermission(packageName, permName, deviceId, userId, reason);
        this.mNewImplementation.revokeRuntimePermission(packageName, permName, deviceId, userId, reason);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(java.lang.String packageName, int userId) {
        this.mOldImplementation.revokePostNotificationPermissionWithoutKillForTest(packageName, userId);
        this.mNewImplementation.revokePostNotificationPermissionWithoutKillForTest(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        boolean oldVal = this.mOldImplementation.shouldShowRequestPermissionRationale(packageName, permName, deviceId, userId);
        boolean newVal = this.mNewImplementation.shouldShowRequestPermissionRationale(packageName, permName, deviceId, userId);
        if (!java.util.Objects.equals(java.lang.Boolean.valueOf(oldVal), java.lang.Boolean.valueOf(newVal))) {
            signalImplDifference("shouldShowRequestPermissionRationale");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        boolean oldVal = this.mOldImplementation.isPermissionRevokedByPolicy(packageName, permName, deviceId, userId);
        boolean newVal = this.mNewImplementation.isPermissionRevokedByPolicy(packageName, permName, deviceId, userId);
        if (!java.util.Objects.equals(java.lang.Boolean.valueOf(oldVal), java.lang.Boolean.valueOf(newVal))) {
            signalImplDifference("isPermissionRevokedByPolicy");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions() {
        java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> oldVal = this.mOldImplementation.getSplitPermissions();
        java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> newVal = this.mNewImplementation.getSplitPermissions();
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getSplitPermissions");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(java.lang.String pkgName, java.lang.String permName, java.lang.String deviceId, int userId) {
        int oldVal = this.mOldImplementation.checkPermission(pkgName, permName, deviceId, userId);
        int newVal = this.mNewImplementation.checkPermission(pkgName, permName, deviceId, userId);
        if (!java.util.Objects.equals(java.lang.Integer.valueOf(oldVal), java.lang.Integer.valueOf(newVal))) {
            signalImplDifference("checkPermission");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int uid, java.lang.String permName, java.lang.String deviceId) {
        int oldVal = this.mOldImplementation.checkUidPermission(uid, permName, deviceId);
        int newVal = this.mNewImplementation.checkUidPermission(uid, permName, deviceId);
        if (!java.util.Objects.equals(java.lang.Integer.valueOf(oldVal), java.lang.Integer.valueOf(newVal))) {
            signalImplDifference("checkUidPermission");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String packageName, java.lang.String deviceId, int userId) {
        return this.mNewImplementation.getAllPermissionStates(packageName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages() {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> oldVal = this.mOldImplementation.getAllAppOpPermissionPackages();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> newVal = this.mNewImplementation.getAllAppOpPermissionPackages();
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getAllAppOpPermissionPackages");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) {
        boolean oldVal = this.mOldImplementation.isPermissionsReviewRequired(packageName, userId);
        boolean newVal = this.mNewImplementation.isPermissionsReviewRequired(packageName, userId);
        if (!java.util.Objects.equals(java.lang.Boolean.valueOf(oldVal), java.lang.Boolean.valueOf(newVal))) {
            signalImplDifference("isPermissionsReviewRequired");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        this.mOldImplementation.resetRuntimePermissions(pkg, userId);
        this.mNewImplementation.resetRuntimePermissions(pkg, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int userId) {
        this.mOldImplementation.resetRuntimePermissionsForUser(userId);
        this.mNewImplementation.resetRuntimePermissionsForUser(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
        this.mOldImplementation.readLegacyPermissionStateTEMP();
        this.mNewImplementation.readLegacyPermissionStateTEMP();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
        this.mOldImplementation.writeLegacyPermissionStateTEMP();
        this.mNewImplementation.writeLegacyPermissionStateTEMP();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String packageName) {
        java.util.Set<java.lang.String> oldVal = this.mOldImplementation.getInstalledPermissions(packageName);
        java.util.Set<java.lang.String> newVal = this.mNewImplementation.getInstalledPermissions(packageName);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getInstalledPermissions");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String packageName, int userId) {
        java.util.Set<java.lang.String> oldVal = this.mOldImplementation.getGrantedPermissions(packageName, userId);
        java.util.Set<java.lang.String> newVal = this.mNewImplementation.getGrantedPermissions(packageName, userId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getGrantedPermissions");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(java.lang.String permissionName, int userId) {
        int[] oldVal = this.mOldImplementation.getPermissionGids(permissionName, userId);
        int[] newVal = this.mNewImplementation.getPermissionGids(permissionName, userId);
        if (!java.util.Arrays.equals(oldVal, newVal)) {
            signalImplDifference("getPermissionGids");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) {
        java.lang.String[] oldVal = this.mOldImplementation.getAppOpPermissionPackages(permissionName);
        java.lang.String[] newVal = this.mNewImplementation.getAppOpPermissionPackages(permissionName);
        if (!java.util.Arrays.equals(oldVal, newVal)) {
            signalImplDifference("getAppOpPermissionPackages");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String permName) {
        com.android.server.pm.permission.Permission oldVal = this.mOldImplementation.getPermissionTEMP(permName);
        com.android.server.pm.permission.Permission newVal = this.mNewImplementation.getPermissionTEMP(permName);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getPermissionTEMP");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int protection) {
        java.util.List<android.content.pm.PermissionInfo> oldVal = this.mOldImplementation.getAllPermissionsWithProtection(protection);
        java.util.List<android.content.pm.PermissionInfo> newVal = this.mNewImplementation.getAllPermissionsWithProtection(protection);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getAllPermissionsWithProtection");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int protectionFlags) {
        java.util.List<android.content.pm.PermissionInfo> oldVal = this.mOldImplementation.getAllPermissionsWithProtectionFlags(protectionFlags);
        java.util.List<android.content.pm.PermissionInfo> newVal = this.mNewImplementation.getAllPermissionsWithProtectionFlags(protectionFlags);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getAllPermissionsWithProtectionFlags");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions() {
        java.util.List<com.android.server.pm.permission.LegacyPermission> oldVal = this.mOldImplementation.getLegacyPermissions();
        java.util.List<com.android.server.pm.permission.LegacyPermission> newVal = this.mNewImplementation.getLegacyPermissions();
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getLegacyPermissions");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int appId) {
        com.android.server.pm.permission.LegacyPermissionState oldVal = this.mOldImplementation.getLegacyPermissionState(appId);
        com.android.server.pm.permission.LegacyPermissionState newVal = this.mNewImplementation.getLegacyPermissionState(appId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getLegacyPermissionState");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        this.mOldImplementation.readLegacyPermissionsTEMP(legacyPermissionSettings);
        this.mNewImplementation.readLegacyPermissionsTEMP(legacyPermissionSettings);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        this.mOldImplementation.writeLegacyPermissionsTEMP(legacyPermissionSettings);
        this.mNewImplementation.writeLegacyPermissionsTEMP(legacyPermissionSettings);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String getDefaultPermissionGrantFingerprint(int userId) {
        java.lang.String oldVal = this.mOldImplementation.getDefaultPermissionGrantFingerprint(userId);
        java.lang.String newVal = this.mNewImplementation.getDefaultPermissionGrantFingerprint(userId);
        if (java.util.Objects.equals(oldVal, android.os.Build.FINGERPRINT) != java.util.Objects.equals(newVal, android.os.Build.FINGERPRINT)) {
            signalImplDifference("getDefaultPermissionGrantFingerprint");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void setDefaultPermissionGrantFingerprint(java.lang.String fingerprint, int userId) {
        this.mOldImplementation.setDefaultPermissionGrantFingerprint(fingerprint, userId);
        this.mNewImplementation.setDefaultPermissionGrantFingerprint(fingerprint, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        this.mOldImplementation.onSystemReady();
        this.mNewImplementation.onSystemReady();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(java.lang.String volumeUuid, boolean fingerprintChanged) {
        this.mOldImplementation.onStorageVolumeMounted(volumeUuid, fingerprintChanged);
        this.mNewImplementation.onStorageVolumeMounted(volumeUuid, fingerprintChanged);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int uid) {
        int[] oldVal = this.mOldImplementation.getGidsForUid(uid);
        int[] newVal = this.mNewImplementation.getGidsForUid(uid);
        if (!java.util.Arrays.equals(oldVal, newVal)) {
            signalImplDifference("getGidsForUid");
        }
        return newVal;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int userId) {
        this.mOldImplementation.onUserCreated(userId);
        this.mNewImplementation.onUserCreated(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int userId) {
        this.mOldImplementation.onUserRemoved(userId);
        this.mNewImplementation.onUserRemoved(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(com.android.server.pm.pkg.PackageState pkg, boolean isInstantApp, com.android.server.pm.pkg.AndroidPackage oldPkg) {
        this.mOldImplementation.onPackageAdded(pkg, isInstantApp, oldPkg);
        this.mNewImplementation.onPackageAdded(pkg, isInstantApp, oldPkg);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int userId) {
        this.mOldImplementation.onPackageInstalled(pkg, previousAppId, params, userId);
        this.mNewImplementation.onPackageInstalled(pkg, previousAppId, params, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage pkg) {
        this.mOldImplementation.onPackageRemoved(pkg);
        this.mNewImplementation.onPackageRemoved(pkg);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int userId) {
        this.mOldImplementation.onPackageUninstalled(packageName, appId, packageState, pkg, sharedUserPkgs, userId);
        this.mNewImplementation.onPackageUninstalled(packageName, appId, packageState, pkg, sharedUserPkgs, userId);
    }
}
