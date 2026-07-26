package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionManagerServiceLoggingDecorator implements com.android.server.pm.permission.PermissionManagerServiceInterface {
    private static final java.lang.String LOG_TAG = com.android.server.pm.permission.PermissionManagerServiceLoggingDecorator.class.getSimpleName();
    private final com.android.server.pm.permission.PermissionManagerServiceInterface mService;

    public PermissionManagerServiceLoggingDecorator(com.android.server.pm.permission.PermissionManagerServiceInterface service) {
        this.mService = service;
    }

    public byte[] backupRuntimePermissions(int userId) {
        android.util.Log.i(LOG_TAG, "backupRuntimePermissions(userId = " + userId + ")");
        return this.mService.backupRuntimePermissions(userId);
    }

    public void restoreRuntimePermissions(byte[] backup, int userId) {
        android.util.Log.i(LOG_TAG, "restoreRuntimePermissions(backup = " + backup + ", userId = " + userId + ")");
        this.mService.restoreRuntimePermissions(backup, userId);
    }

    public void restoreDelayedRuntimePermissions(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "restoreDelayedRuntimePermissions(packageName = " + packageName + ", userId = " + userId + ")");
        this.mService.restoreDelayedRuntimePermissions(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        android.util.Log.i(LOG_TAG, "dump(fd = " + fd + ", pw = " + pw + ", args = " + java.util.Arrays.toString(args) + ")");
        this.mService.dump(fd, pw, args);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        android.util.Log.i(LOG_TAG, "getAllPermissionGroups(flags = " + flags + ")");
        return this.mService.getAllPermissionGroups(flags);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        android.util.Log.i(LOG_TAG, "getPermissionGroupInfo(groupName = " + groupName + ", flags = " + flags + ")");
        return this.mService.getPermissionGroupInfo(groupName, flags);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permName, int flags, java.lang.String opPackageName) {
        android.util.Log.i(LOG_TAG, "getPermissionInfo(permName = " + permName + ", flags = " + flags + ", opPackageName = " + opPackageName + ")");
        return this.mService.getPermissionInfo(permName, flags, opPackageName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String groupName, int flags) {
        android.util.Log.i(LOG_TAG, "queryPermissionsByGroup(groupName = " + groupName + ", flags = " + flags + ")");
        return this.mService.queryPermissionsByGroup(groupName, flags);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(android.content.pm.PermissionInfo info, boolean async) {
        android.util.Log.i(LOG_TAG, "addPermission(info = " + info + ", async = " + async + ")");
        return this.mService.addPermission(info, async);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(java.lang.String permName) {
        android.util.Log.i(LOG_TAG, "removePermission(permName = " + permName + ")");
        this.mService.removePermission(permName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "getPermissionFlags(packageName = " + packageName + ", permName = " + permName + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        return this.mService.getPermissionFlags(packageName, permName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(java.lang.String packageName, java.lang.String permName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "updatePermissionFlags(packageName = " + packageName + ", permName = " + permName + ", flagMask = " + flagMask + ", flagValues = " + flagValues + ", checkAdjustPolicyFlagPermission = " + checkAdjustPolicyFlagPermission + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        this.mService.updatePermissionFlags(packageName, permName, flagMask, flagValues, checkAdjustPolicyFlagPermission, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) {
        android.util.Log.i(LOG_TAG, "updatePermissionFlagsForAllApps(flagMask = " + flagMask + ", flagValues = " + flagValues + ", userId = " + userId + ")");
        this.mService.updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        android.util.Log.i(LOG_TAG, "addOnPermissionsChangeListener(listener = " + listener + ")");
        this.mService.addOnPermissionsChangeListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        android.util.Log.i(LOG_TAG, "removeOnPermissionsChangeListener(listener = " + listener + ")");
        this.mService.removeOnPermissionsChangeListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        android.util.Log.i(LOG_TAG, "addAllowlistedRestrictedPermission(packageName = " + packageName + ", permName = " + permName + ", flags = " + flags + ", userId = " + userId + ")");
        return this.mService.addAllowlistedRestrictedPermission(packageName, permName, flags, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String packageName, int flags, int userId) {
        android.util.Log.i(LOG_TAG, "getAllowlistedRestrictedPermissions(packageName = " + packageName + ", flags = " + flags + ", userId = " + userId + ")");
        return this.mService.getAllowlistedRestrictedPermissions(packageName, flags, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        android.util.Log.i(LOG_TAG, "removeAllowlistedRestrictedPermission(packageName = " + packageName + ", permName = " + permName + ", flags = " + flags + ", userId = " + userId + ")");
        return this.mService.removeAllowlistedRestrictedPermission(packageName, permName, flags, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "grantRuntimePermission(packageName = " + packageName + ", permName = " + permName + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        this.mService.grantRuntimePermission(packageName, permName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId, java.lang.String reason) {
        android.util.Log.i(LOG_TAG, "revokeRuntimePermission(packageName = " + packageName + ", permName = " + permName + ", deviceId = " + deviceId + ", userId = " + userId + ", reason = " + reason + ")");
        this.mService.revokeRuntimePermission(packageName, permName, deviceId, userId, reason);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "revokePostNotificationPermissionWithoutKillForTest(packageName = " + packageName + ", userId = " + userId + ")");
        this.mService.revokePostNotificationPermissionWithoutKillForTest(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "shouldShowRequestPermissionRationale(packageName = " + packageName + ", permName = " + permName + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        return this.mService.shouldShowRequestPermissionRationale(packageName, permName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "isPermissionRevokedByPolicy(packageName = " + packageName + ", permName = " + permName + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        return this.mService.isPermissionRevokedByPolicy(packageName, permName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions() {
        android.util.Log.i(LOG_TAG, "getSplitPermissions()");
        return this.mService.getSplitPermissions();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(java.lang.String pkgName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "checkPermission(pkgName = " + pkgName + ", permName = " + permName + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        return this.mService.checkPermission(pkgName, permName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int uid, java.lang.String permName, java.lang.String deviceId) {
        android.util.Log.i(LOG_TAG, "checkUidPermission(uid = " + uid + ", permName = " + permName + ", deviceId = " + deviceId + ")");
        return this.mService.checkUidPermission(uid, permName, deviceId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String packageName, java.lang.String deviceId, int userId) {
        android.util.Log.i(LOG_TAG, "getAllPermissionStates(packageName = " + packageName + ", deviceId = " + deviceId + ", userId = " + userId + ")");
        return this.mService.getAllPermissionStates(packageName, deviceId, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages() {
        android.util.Log.i(LOG_TAG, "getAllAppOpPermissionPackages()");
        return this.mService.getAllAppOpPermissionPackages();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "isPermissionsReviewRequired(packageName = " + packageName + ", userId = " + userId + ")");
        return this.mService.isPermissionsReviewRequired(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        android.util.Log.i(LOG_TAG, "resetRuntimePermissions(pkg = " + pkg + ", userId = " + userId + ")");
        this.mService.resetRuntimePermissions(pkg, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int userId) {
        android.util.Log.i(LOG_TAG, "resetRuntimePermissionsForUser(userId = " + userId + ")");
        this.mService.resetRuntimePermissionsForUser(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
        android.util.Log.i(LOG_TAG, "readLegacyPermissionStateTEMP()");
        this.mService.readLegacyPermissionStateTEMP();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
        android.util.Log.i(LOG_TAG, "writeLegacyPermissionStateTEMP()");
        this.mService.writeLegacyPermissionStateTEMP();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String packageName) {
        android.util.Log.i(LOG_TAG, "getInstalledPermissions(packageName = " + packageName + ")");
        return this.mService.getInstalledPermissions(packageName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "getGrantedPermissions(packageName = " + packageName + ", userId = " + userId + ")");
        return this.mService.getGrantedPermissions(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(java.lang.String permissionName, int userId) {
        android.util.Log.i(LOG_TAG, "getPermissionGids(permissionName = " + permissionName + ", userId = " + userId + ")");
        return this.mService.getPermissionGids(permissionName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) {
        android.util.Log.i(LOG_TAG, "getAppOpPermissionPackages(permissionName = " + permissionName + ")");
        return this.mService.getAppOpPermissionPackages(permissionName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String permName) {
        android.util.Log.i(LOG_TAG, "getPermissionTEMP(permName = " + permName + ")");
        return this.mService.getPermissionTEMP(permName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int protection) {
        android.util.Log.i(LOG_TAG, "getAllPermissionsWithProtection(protection = " + protection + ")");
        return this.mService.getAllPermissionsWithProtection(protection);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int protectionFlags) {
        android.util.Log.i(LOG_TAG, "getAllPermissionsWithProtectionFlags(protectionFlags = " + protectionFlags + ")");
        return this.mService.getAllPermissionsWithProtectionFlags(protectionFlags);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions() {
        android.util.Log.i(LOG_TAG, "getLegacyPermissions()");
        return this.mService.getLegacyPermissions();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int appId) {
        android.util.Log.i(LOG_TAG, "getLegacyPermissionState(appId = " + appId + ")");
        return this.mService.getLegacyPermissionState(appId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        android.util.Log.i(LOG_TAG, "readLegacyPermissionsTEMP(legacyPermissionSettings = " + legacyPermissionSettings + ")");
        this.mService.readLegacyPermissionsTEMP(legacyPermissionSettings);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        android.util.Log.i(LOG_TAG, "writeLegacyPermissionsTEMP(legacyPermissionSettings = " + legacyPermissionSettings + ")");
        this.mService.writeLegacyPermissionsTEMP(legacyPermissionSettings);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String getDefaultPermissionGrantFingerprint(int userId) {
        android.util.Log.i(LOG_TAG, "getDefaultPermissionGrantFingerprint(userId = " + userId + ")");
        return this.mService.getDefaultPermissionGrantFingerprint(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void setDefaultPermissionGrantFingerprint(java.lang.String fingerprint, int userId) {
        android.util.Log.i(LOG_TAG, "setDefaultPermissionGrantFingerprint(fingerprint = " + fingerprint + ", userId = " + userId + ")");
        this.mService.setDefaultPermissionGrantFingerprint(fingerprint, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        android.util.Log.i(LOG_TAG, "onSystemReady()");
        this.mService.onSystemReady();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(java.lang.String volumeUuid, boolean fingerprintChanged) {
        android.util.Log.i(LOG_TAG, "onStorageVolumeMounted(volumeUuid = " + volumeUuid + ", fingerprintChanged = " + fingerprintChanged + ")");
        this.mService.onStorageVolumeMounted(volumeUuid, fingerprintChanged);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int uid) {
        android.util.Log.i(LOG_TAG, "getGidsForUid(uid = " + uid + ")");
        return this.mService.getGidsForUid(uid);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int userId) {
        android.util.Log.i(LOG_TAG, "onUserCreated(userId = " + userId + ")");
        this.mService.onUserCreated(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int userId) {
        android.util.Log.i(LOG_TAG, "onUserRemoved(userId = " + userId + ")");
        this.mService.onUserRemoved(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean isInstantApp, com.android.server.pm.pkg.AndroidPackage oldPkg) {
        android.util.Log.i(LOG_TAG, "onPackageAdded(packageState = " + packageState + ", isInstantApp = " + isInstantApp + ", oldPkg = " + oldPkg + ")");
        this.mService.onPackageAdded(packageState, isInstantApp, oldPkg);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int userId) {
        android.util.Log.i(LOG_TAG, "onPackageInstalled(pkg = " + pkg + ", previousAppId = " + previousAppId + ", params = " + params + ", userId = " + userId + ")");
        this.mService.onPackageInstalled(pkg, previousAppId, params, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.util.Log.i(LOG_TAG, "onPackageRemoved(pkg = " + pkg + ")");
        this.mService.onPackageRemoved(pkg);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int userId) {
        android.util.Log.i(LOG_TAG, "onPackageUninstalled(packageName = " + packageName + ", appId = " + appId + ", packageState = " + packageState + ", pkg = " + pkg + ", sharedUserPkgs = " + sharedUserPkgs + ", userId = " + userId + ")");
        this.mService.onPackageUninstalled(packageName, appId, packageState, pkg, sharedUserPkgs, userId);
    }
}
