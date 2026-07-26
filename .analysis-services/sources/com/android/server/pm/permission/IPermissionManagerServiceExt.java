package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface IPermissionManagerServiceExt {
    default void beforeOnPackageUninstalled() {
    }

    default void afterOnPackageUninstalled() {
    }

    default void adjustGetAppOpPermissionPackagesInternal(android.util.ArraySet<java.lang.String> packageNames) {
    }

    default boolean hookShouldGrantNormalPermission(java.lang.String packageName) {
        return false;
    }

    default boolean hookShouldGrantPermissionBySignature(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String perm, boolean allowed, java.lang.String sourcePermPkg) {
        return false;
    }

    default boolean hookIsPermissionsReviewRequiredInternal() {
        return false;
    }

    default boolean hookCheckUidPermissionImpl(android.content.Context context, java.lang.String permName, int uid) {
        return false;
    }

    default void hookPermissionManagerService(android.content.Context context, com.android.server.pm.permission.PermissionManagerService service) {
    }
}
