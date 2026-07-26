package com.android.server.permission.access.util;

/* JADX INFO: compiled from: PackageVersionMigration.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0015\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lcom/android/server/permission/access/util/PackageVersionMigration;", "", "()V", "getVersion", "", "userId", "getVersion$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PackageVersionMigration {
    public static final com.android.server.permission.access.util.PackageVersionMigration INSTANCE = new com.android.server.permission.access.util.PackageVersionMigration();

    private PackageVersionMigration() {
    }

    public final int getVersion$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(int userId) {
        com.android.server.pm.permission.PermissionMigrationHelper permissionMigrationHelper = (com.android.server.pm.permission.PermissionMigrationHelper) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionMigrationHelper.class);
        int permissionVersion = permissionMigrationHelper.getLegacyPermissionStateVersion(userId);
        com.android.server.appop.AppOpMigrationHelper appOpMigrationHelper = (com.android.server.appop.AppOpMigrationHelper) com.android.server.LocalServices.getService(com.android.server.appop.AppOpMigrationHelper.class);
        int appOpVersion = appOpMigrationHelper.getLegacyAppOpVersion();
        if (permissionVersion == -1 && appOpVersion == -1) {
            throw new java.lang.IllegalStateException("getVersion() called when there are no legacy files".toString());
        }
        if (permissionVersion >= 11 && appOpVersion >= 3) {
            return 15;
        }
        if (permissionVersion >= 10 && appOpVersion >= 3) {
            return 14;
        }
        if (permissionVersion >= 10 && appOpVersion >= 1) {
            return 13;
        }
        if (permissionVersion >= 9 && appOpVersion >= 1) {
            return 12;
        }
        if (permissionVersion >= 8 && appOpVersion >= 1) {
            return 11;
        }
        if (permissionVersion >= 7 && appOpVersion >= 1) {
            return 10;
        }
        if (permissionVersion >= 6 && appOpVersion >= 1) {
            return 9;
        }
        if (permissionVersion >= 5 && appOpVersion >= 1) {
            return 8;
        }
        if (permissionVersion >= 4 && appOpVersion >= 1) {
            return 7;
        }
        if (permissionVersion >= 3 && appOpVersion >= 1) {
            return 6;
        }
        if (permissionVersion >= 2 && appOpVersion >= 1) {
            return 5;
        }
        if (permissionVersion >= 1 && appOpVersion >= 1) {
            return 4;
        }
        if (permissionVersion >= 0 && appOpVersion >= 1) {
            return 3;
        }
        if (permissionVersion >= 0 && appOpVersion >= 0) {
            return 2;
        }
        if (permissionVersion >= -1 && appOpVersion >= 0) {
            return 1;
        }
        return 0;
    }
}
