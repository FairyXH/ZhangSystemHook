package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionManagerServiceTracingDecorator implements com.android.server.pm.permission.PermissionManagerServiceInterface {
    private static final long TRACE_TAG = 262144;
    private final com.android.server.pm.permission.PermissionManagerServiceInterface mService;

    public PermissionManagerServiceTracingDecorator(com.android.server.pm.permission.PermissionManagerServiceInterface service) {
        this.mService = service;
    }

    public byte[] backupRuntimePermissions(int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#backupRuntimePermissions");
        try {
            return this.mService.backupRuntimePermissions(userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    public void restoreRuntimePermissions(byte[] backup, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#restoreRuntimePermissions");
        try {
            this.mService.restoreRuntimePermissions(backup, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    public void restoreDelayedRuntimePermissions(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#restoreDelayedRuntimePermissions");
        try {
            this.mService.restoreDelayedRuntimePermissions(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#dump");
        try {
            this.mService.dump(fd, pw, args);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAllPermissionGroups");
        try {
            return this.mService.getAllPermissionGroups(flags);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getPermissionGroupInfo");
        try {
            return this.mService.getPermissionGroupInfo(groupName, flags);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permName, int flags, java.lang.String opPackageName) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getPermissionInfo");
        try {
            return this.mService.getPermissionInfo(permName, flags, opPackageName);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String groupName, int flags) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#queryPermissionsByGroup");
        try {
            return this.mService.queryPermissionsByGroup(groupName, flags);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(android.content.pm.PermissionInfo info, boolean async) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#addPermission");
        try {
            return this.mService.addPermission(info, async);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(java.lang.String permName) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#removePermission");
        try {
            this.mService.removePermission(permName);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getPermissionFlags");
        try {
            return this.mService.getPermissionFlags(packageName, permName, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(java.lang.String packageName, java.lang.String permName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#updatePermissionFlags");
        try {
            this.mService.updatePermissionFlags(packageName, permName, flagMask, flagValues, checkAdjustPolicyFlagPermission, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#updatePermissionFlagsForAllApps");
        try {
            this.mService.updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#addOnPermissionsChangeListener");
        try {
            this.mService.addOnPermissionsChangeListener(listener);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#removeOnPermissionsChangeListener");
        try {
            this.mService.removeOnPermissionsChangeListener(listener);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#addAllowlistedRestrictedPermission");
        try {
            return this.mService.addAllowlistedRestrictedPermission(packageName, permName, flags, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String packageName, int flags, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAllowlistedRestrictedPermissions");
        try {
            return this.mService.getAllowlistedRestrictedPermissions(packageName, flags, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#removeAllowlistedRestrictedPermission");
        try {
            return this.mService.removeAllowlistedRestrictedPermission(packageName, permName, flags, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#grantRuntimePermission");
        try {
            this.mService.grantRuntimePermission(packageName, permName, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId, java.lang.String reason) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#revokeRuntimePermission");
        try {
            this.mService.revokeRuntimePermission(packageName, permName, deviceId, userId, reason);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#revokePostNotificationPermissionWithoutKillForTest");
        try {
            this.mService.revokePostNotificationPermissionWithoutKillForTest(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#shouldShowRequestPermissionRationale");
        try {
            return this.mService.shouldShowRequestPermissionRationale(packageName, permName, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#isPermissionRevokedByPolicy");
        try {
            return this.mService.isPermissionRevokedByPolicy(packageName, permName, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getSplitPermissions");
        try {
            return this.mService.getSplitPermissions();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(java.lang.String pkgName, java.lang.String permName, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#checkPermission");
        try {
            return this.mService.checkPermission(pkgName, permName, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int uid, java.lang.String permName, java.lang.String deviceId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#checkUidPermission");
        try {
            return this.mService.checkUidPermission(uid, permName, deviceId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String packageName, java.lang.String deviceId, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAllPermissionStates");
        try {
            return this.mService.getAllPermissionStates(packageName, deviceId, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAllAppOpPermissionPackages");
        try {
            return this.mService.getAllAppOpPermissionPackages();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#isPermissionsReviewRequired");
        try {
            return this.mService.isPermissionsReviewRequired(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#resetRuntimePermissions");
        try {
            this.mService.resetRuntimePermissions(pkg, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#resetRuntimePermissionsForUser");
        try {
            this.mService.resetRuntimePermissionsForUser(userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#readLegacyPermissionStateTEMP");
        try {
            this.mService.readLegacyPermissionStateTEMP();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#writeLegacyPermissionStateTEMP");
        try {
            this.mService.writeLegacyPermissionStateTEMP();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String packageName) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getInstalledPermissions");
        try {
            return this.mService.getInstalledPermissions(packageName);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getGrantedPermissions");
        try {
            return this.mService.getGrantedPermissions(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(java.lang.String permissionName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getPermissionGids");
        try {
            return this.mService.getPermissionGids(permissionName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAppOpPermissionPackages");
        try {
            return this.mService.getAppOpPermissionPackages(permissionName);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String permName) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getPermissionTEMP");
        try {
            return this.mService.getPermissionTEMP(permName);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int protection) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAllPermissionsWithProtection");
        try {
            return this.mService.getAllPermissionsWithProtection(protection);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int protectionFlags) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getAllPermissionsWithProtectionFlags");
        try {
            return this.mService.getAllPermissionsWithProtectionFlags(protectionFlags);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getLegacyPermissions");
        try {
            return this.mService.getLegacyPermissions();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int appId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getLegacyPermissionState");
        try {
            return this.mService.getLegacyPermissionState(appId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#readLegacyPermissionsTEMP");
        try {
            this.mService.readLegacyPermissionsTEMP(legacyPermissionSettings);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#writeLegacyPermissionsTEMP");
        try {
            this.mService.writeLegacyPermissionsTEMP(legacyPermissionSettings);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String getDefaultPermissionGrantFingerprint(int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getDefaultPermissionGrantFingerprint");
        try {
            return this.mService.getDefaultPermissionGrantFingerprint(userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void setDefaultPermissionGrantFingerprint(java.lang.String fingerprint, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#setDefaultPermissionGrantFingerprint");
        try {
            this.mService.setDefaultPermissionGrantFingerprint(fingerprint, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onSystemReady");
        try {
            this.mService.onSystemReady();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(java.lang.String volumeUuid, boolean fingerprintChanged) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onStorageVolumeMounted");
        try {
            this.mService.onStorageVolumeMounted(volumeUuid, fingerprintChanged);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int uid) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#getGidsForUid");
        try {
            return this.mService.getGidsForUid(uid);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onUserCreated");
        try {
            this.mService.onUserCreated(userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onUserRemoved");
        try {
            this.mService.onUserRemoved(userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean isInstantApp, com.android.server.pm.pkg.AndroidPackage oldPkg) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onPackageAdded");
        try {
            this.mService.onPackageAdded(packageState, isInstantApp, oldPkg);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onPackageInstalled");
        try {
            this.mService.onPackageInstalled(pkg, previousAppId, params, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onPackageRemoved");
        try {
            this.mService.onPackageRemoved(pkg);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingPermissionManagerServiceImpl#onPackageUninstalled");
        try {
            this.mService.onPackageUninstalled(packageName, appId, packageState, pkg, sharedUserPkgs, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }
}
