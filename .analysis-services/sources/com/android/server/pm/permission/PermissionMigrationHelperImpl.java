package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionMigrationHelperImpl implements com.android.server.pm.permission.PermissionMigrationHelper {
    private static final java.lang.String LOG_TAG = com.android.server.pm.permission.PermissionMigrationHelperImpl.class.getSimpleName();

    @Override // com.android.server.pm.permission.PermissionMigrationHelper
    public boolean hasLegacyPermission() {
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        com.android.server.pm.permission.LegacyPermissionSettings legacySettings = packageManagerInternal.getLegacyPermissions();
        return (legacySettings.getPermissions().isEmpty() && legacySettings.getPermissionTrees().isEmpty()) ? false : true;
    }

    @Override // com.android.server.pm.permission.PermissionMigrationHelper
    public java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> getLegacyPermissions() {
        android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        return toLegacyPermissions(mPackageManagerInternal.getLegacyPermissions().getPermissions());
    }

    @Override // com.android.server.pm.permission.PermissionMigrationHelper
    public java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> getLegacyPermissionTrees() {
        android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        return toLegacyPermissions(mPackageManagerInternal.getLegacyPermissions().getPermissionTrees());
    }

    private java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> toLegacyPermissions(java.util.List<com.android.server.pm.permission.LegacyPermission> legacyPermissions) {
        final java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> permissions = new android.util.ArrayMap<>();
        legacyPermissions.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionMigrationHelperImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.permission.PermissionMigrationHelperImpl.lambda$toLegacyPermissions$0(permissions, (com.android.server.pm.permission.LegacyPermission) obj);
            }
        });
        return permissions;
    }

    static /* synthetic */ void lambda$toLegacyPermissions$0(java.util.Map permissions, com.android.server.pm.permission.LegacyPermission legacyPermission) {
        com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission permission = new com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission(legacyPermission.getPermissionInfo(), legacyPermission.getType());
        permissions.put(legacyPermission.getPermissionInfo().name, permission);
    }

    @Override // com.android.server.pm.permission.PermissionMigrationHelper
    public java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> getLegacyPermissionStates(int userId) {
        android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        final java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> appIdPermissionStates = new android.util.ArrayMap<>();
        com.android.permission.persistence.RuntimePermissionsState legacyState = (com.android.permission.persistence.RuntimePermissionsState) mPackageManagerInternal.getLegacyPermissionsState(userId);
        com.android.server.pm.PackageManagerLocal packageManagerLocal = (com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.pm.PackageManagerLocal.class);
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            final java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> packageStates = snapshot.getPackageStates();
            legacyState.getPackagePermissions().forEach(new java.util.function.BiConsumer() { // from class: com.android.server.pm.permission.PermissionMigrationHelperImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$getLegacyPermissionStates$1(packageStates, appIdPermissionStates, (java.lang.String) obj, (java.util.List) obj2);
                }
            });
            final java.util.Map<java.lang.String, com.android.server.pm.pkg.SharedUserApi> sharedUsers = snapshot.getSharedUsers();
            legacyState.getSharedUserPermissions().forEach(new java.util.function.BiConsumer() { // from class: com.android.server.pm.permission.PermissionMigrationHelperImpl$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$getLegacyPermissionStates$2(sharedUsers, appIdPermissionStates, (java.lang.String) obj, (java.util.List) obj2);
                }
            });
            if (snapshot != null) {
                snapshot.close();
            }
            return appIdPermissionStates;
        } catch (java.lang.Throwable th) {
            if (snapshot != null) {
                try {
                    snapshot.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getLegacyPermissionStates$1(java.util.Map packageStates, java.util.Map appIdPermissionStates, java.lang.String packageName, java.util.List permissionStates) {
        if (!permissionStates.isEmpty()) {
            com.android.server.pm.pkg.PackageState packageState = (com.android.server.pm.pkg.PackageState) packageStates.get(packageName);
            if (packageState != null) {
                int appId = packageState.getAppId();
                appIdPermissionStates.put(java.lang.Integer.valueOf(appId), toLegacyPermissionStates(permissionStates));
            } else {
                android.util.Log.w(LOG_TAG, "Package " + packageName + " not found.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getLegacyPermissionStates$2(java.util.Map sharedUsers, java.util.Map appIdPermissionStates, java.lang.String sharedUserName, java.util.List permissionStates) {
        if (!permissionStates.isEmpty()) {
            com.android.server.pm.pkg.SharedUserApi sharedUser = (com.android.server.pm.pkg.SharedUserApi) sharedUsers.get(sharedUserName);
            if (sharedUser != null) {
                int appId = sharedUser.getAppId();
                appIdPermissionStates.put(java.lang.Integer.valueOf(appId), toLegacyPermissionStates(permissionStates));
            } else {
                android.util.Log.w(LOG_TAG, "Shared user " + sharedUserName + " not found.");
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionMigrationHelper
    public int getLegacyPermissionStateVersion(int userId) {
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int version = packageManagerInternal.getLegacyPermissionsVersion(userId);
        switch (version) {
            case -1:
                return 0;
            case 0:
                return -1;
            default:
                return version;
        }
    }

    @Override // com.android.server.pm.permission.PermissionMigrationHelper
    public boolean hasLegacyPermissionState(int userId) {
        return getLegacyPermissionStateVersion(userId) > -1;
    }

    private java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState> toLegacyPermissionStates(java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions) {
        java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState> legacyPermissions = new android.util.ArrayMap<>();
        int size = permissions.size();
        for (int i = 0; i < size; i++) {
            com.android.permission.persistence.RuntimePermissionsState.PermissionState permState = permissions.get(i);
            legacyPermissions.put(permState.getName(), new com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState(permState.isGranted(), permState.getFlags()));
        }
        return legacyPermissions;
    }
}
