package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface PermissionManagerServiceInterface extends android.permission.PermissionManagerInternal {
    boolean addAllowlistedRestrictedPermission(java.lang.String str, java.lang.String str2, int i, int i2);

    void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener iOnPermissionsChangeListener);

    boolean addPermission(android.content.pm.PermissionInfo permissionInfo, boolean z);

    int checkPermission(java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    int checkUidPermission(int i, java.lang.String str, java.lang.String str2);

    void dump(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr);

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages();

    java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int i);

    java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String str, java.lang.String str2, int i);

    java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int i);

    java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int i);

    java.util.List<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String str, int i, int i2);

    java.lang.String[] getAppOpPermissionPackages(java.lang.String str);

    java.lang.String getDefaultPermissionGrantFingerprint(int i);

    int[] getGidsForUid(int i);

    java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String str, int i);

    java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String str);

    com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int i);

    java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions();

    int getPermissionFlags(java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    int[] getPermissionGids(java.lang.String str, int i);

    android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String str, int i);

    android.content.pm.PermissionInfo getPermissionInfo(java.lang.String str, int i, java.lang.String str2);

    com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String str);

    java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions();

    void grantRuntimePermission(java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    boolean isPermissionRevokedByPolicy(java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    boolean isPermissionsReviewRequired(java.lang.String str, int i);

    void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean z, com.android.server.pm.pkg.AndroidPackage androidPackage);

    void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage androidPackage, int i, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams packageInstalledParams, int i2);

    void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage androidPackage);

    void onPackageUninstalled(java.lang.String str, int i, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage androidPackage, java.util.List<com.android.server.pm.pkg.AndroidPackage> list, int i2);

    void onStorageVolumeMounted(java.lang.String str, boolean z);

    void onSystemReady();

    void onUserCreated(int i);

    void onUserRemoved(int i);

    java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String str, int i);

    void readLegacyPermissionStateTEMP();

    void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings);

    boolean removeAllowlistedRestrictedPermission(java.lang.String str, java.lang.String str2, int i, int i2);

    void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener iOnPermissionsChangeListener);

    void removePermission(java.lang.String str);

    void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage androidPackage, int i);

    void resetRuntimePermissionsForUser(int i);

    void revokePostNotificationPermissionWithoutKillForTest(java.lang.String str, int i);

    void revokeRuntimePermission(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.String str4);

    void setDefaultPermissionGrantFingerprint(java.lang.String str, int i);

    boolean shouldShowRequestPermissionRationale(java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    void updatePermissionFlags(java.lang.String str, java.lang.String str2, int i, int i2, boolean z, java.lang.String str3, int i3);

    void updatePermissionFlagsForAllApps(int i, int i2, int i3);

    void writeLegacyPermissionStateTEMP();

    void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings);
}
