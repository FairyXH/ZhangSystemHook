package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface PermissionManagerServiceInternal extends android.permission.PermissionManagerInternal, com.android.server.pm.permission.LegacyPermissionDataProvider {

    public interface CheckPermissionDelegate {
        int checkPermission(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, com.android.internal.util.function.QuadFunction<java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer> quadFunction);

        int checkUidPermission(int i, java.lang.String str, java.lang.String str2, com.android.internal.util.function.TriFunction<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer> triFunction);
    }

    public interface HotwordDetectionServiceProvider {
        int getUid();
    }

    int checkPermission(java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    int checkUidPermission(int i, java.lang.String str, int i2);

    java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int i);

    java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int i);

    java.lang.String[] getAppOpPermissionPackages(java.lang.String str);

    java.lang.String getDefaultPermissionGrantFingerprint(int i);

    java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String str, int i);

    com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider getHotwordDetectionServiceProvider();

    java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String str);

    int[] getPermissionGids(java.lang.String str, int i);

    com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String str);

    boolean isPermissionsReviewRequired(java.lang.String str, int i);

    void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean z, com.android.server.pm.pkg.AndroidPackage androidPackage);

    void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage androidPackage, int i, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams packageInstalledParams, int i2);

    void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage androidPackage);

    void onPackageUninstalled(java.lang.String str, int i, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage androidPackage, java.util.List<com.android.server.pm.pkg.AndroidPackage> list, int i2);

    void onStorageVolumeMounted(java.lang.String str, boolean z);

    void onSystemReady();

    void onUserCreated(int i);

    void onUserRemoved(int i);

    void readLegacyPermissionStateTEMP();

    void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings);

    void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage androidPackage, int i);

    void resetRuntimePermissionsForUser(int i);

    void setCheckPermissionDelegate(com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate checkPermissionDelegate);

    void setDefaultPermissionGrantFingerprint(java.lang.String str, int i);

    void setHotwordDetectionServiceProvider(com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider hotwordDetectionServiceProvider);

    void writeLegacyPermissionStateTEMP();

    void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings);

    public static final class PackageInstalledParams {
        public static final com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams DEFAULT = new com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.Builder().build();
        private final java.util.List<java.lang.String> mAllowlistedRestrictedPermissions;
        private final int mAutoRevokePermissionsMode;
        private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mPermissionStates;

        private PackageInstalledParams(android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates, java.util.List<java.lang.String> allowlistedRestrictedPermissions, int autoRevokePermissionsMode) {
            this.mPermissionStates = permissionStates;
            this.mAllowlistedRestrictedPermissions = allowlistedRestrictedPermissions;
            this.mAutoRevokePermissionsMode = autoRevokePermissionsMode;
        }

        public android.util.ArrayMap<java.lang.String, java.lang.Integer> getPermissionStates() {
            return this.mPermissionStates;
        }

        public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions() {
            return this.mAllowlistedRestrictedPermissions;
        }

        public int getAutoRevokePermissionsMode() {
            return this.mAutoRevokePermissionsMode;
        }

        public static final class Builder {
            private android.util.ArrayMap<java.lang.String, java.lang.Integer> mPermissionStates = null;
            private java.util.List<java.lang.String> mAllowlistedRestrictedPermissions = java.util.Collections.emptyList();
            private int mAutoRevokePermissionsMode = 3;

            public com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.Builder setPermissionStates(android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates) {
                java.util.Objects.requireNonNull(permissionStates);
                this.mPermissionStates = permissionStates;
                return this;
            }

            public void setAllowlistedRestrictedPermissions(java.util.List<java.lang.String> allowlistedRestrictedPermissions) {
                java.util.Objects.requireNonNull(allowlistedRestrictedPermissions);
                this.mAllowlistedRestrictedPermissions = new java.util.ArrayList(allowlistedRestrictedPermissions);
            }

            public void setAutoRevokePermissionsMode(int autoRevokePermissionsMode) {
                this.mAutoRevokePermissionsMode = autoRevokePermissionsMode;
            }

            public com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams build() {
                return new com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams(this.mPermissionStates == null ? new android.util.ArrayMap<>() : this.mPermissionStates, this.mAllowlistedRestrictedPermissions, this.mAutoRevokePermissionsMode);
            }
        }
    }
}
