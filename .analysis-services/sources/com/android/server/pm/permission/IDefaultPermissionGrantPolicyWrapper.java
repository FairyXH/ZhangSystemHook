package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface IDefaultPermissionGrantPolicyWrapper {
    default java.lang.Object getNoPmCache() {
        return null;
    }

    default void grantRuntimePermissions(java.lang.Object pm, android.content.pm.PackageInfo pkg, java.util.Set<java.lang.String> permissionsWithoutSplits, boolean systemFixed, boolean ignoreSystemPackage, boolean whitelistRestrictedPermissions, int userId) {
    }
}
