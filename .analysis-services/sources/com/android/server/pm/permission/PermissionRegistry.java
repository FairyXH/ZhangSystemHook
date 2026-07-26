package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionRegistry {
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.Permission> mPermissions = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.Permission> mPermissionTrees = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.internal.pm.pkg.component.ParsedPermissionGroup> mPermissionGroups = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> mAppOpPermissionPackages = new android.util.ArrayMap<>();

    public java.util.Collection<com.android.server.pm.permission.Permission> getPermissions() {
        return this.mPermissions.values();
    }

    public com.android.server.pm.permission.Permission getPermission(java.lang.String permissionName) {
        return this.mPermissions.get(permissionName);
    }

    public void addPermission(com.android.server.pm.permission.Permission permission) {
        this.mPermissions.put(permission.getName(), permission);
    }

    public void removePermission(java.lang.String permissionName) {
        this.mPermissions.remove(permissionName);
    }

    public java.util.Collection<com.android.server.pm.permission.Permission> getPermissionTrees() {
        return this.mPermissionTrees.values();
    }

    public com.android.server.pm.permission.Permission getPermissionTree(java.lang.String permissionTreeName) {
        return this.mPermissionTrees.get(permissionTreeName);
    }

    public void addPermissionTree(com.android.server.pm.permission.Permission permissionTree) {
        this.mPermissionTrees.put(permissionTree.getName(), permissionTree);
    }

    public void transferPermissions(java.lang.String oldPackageName, java.lang.String newPackageName) {
        int i = 0;
        while (i < 2) {
            android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.Permission> permissions = i == 0 ? this.mPermissionTrees : this.mPermissions;
            for (com.android.server.pm.permission.Permission permission : permissions.values()) {
                permission.transfer(oldPackageName, newPackageName);
            }
            i++;
        }
    }

    public java.util.Collection<com.android.internal.pm.pkg.component.ParsedPermissionGroup> getPermissionGroups() {
        return this.mPermissionGroups.values();
    }

    public com.android.internal.pm.pkg.component.ParsedPermissionGroup getPermissionGroup(java.lang.String permissionGroupName) {
        return this.mPermissionGroups.get(permissionGroupName);
    }

    public void addPermissionGroup(com.android.internal.pm.pkg.component.ParsedPermissionGroup permissionGroup) {
        this.mPermissionGroups.put(permissionGroup.getName(), permissionGroup);
    }

    public void removePermissionGroup(java.lang.String permissionGroupName) {
        this.mPermissionGroups.remove(permissionGroupName);
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> getAllAppOpPermissionPackages() {
        return this.mAppOpPermissionPackages;
    }

    public android.util.ArraySet<java.lang.String> getAppOpPermissionPackages(java.lang.String permissionName) {
        return this.mAppOpPermissionPackages.get(permissionName);
    }

    public void addAppOpPermissionPackage(java.lang.String permissionName, java.lang.String packageName) {
        android.util.ArraySet<java.lang.String> packageNames = this.mAppOpPermissionPackages.get(permissionName);
        if (packageNames == null) {
            packageNames = new android.util.ArraySet<>();
            this.mAppOpPermissionPackages.put(permissionName, packageNames);
        }
        packageNames.add(packageName);
    }

    public void removeAppOpPermissionPackage(java.lang.String permissionName, java.lang.String packageName) {
        android.util.ArraySet<java.lang.String> packageNames = this.mAppOpPermissionPackages.get(permissionName);
        if (packageNames == null) {
            return;
        }
        boolean removed = packageNames.remove(packageName);
        if (removed && packageNames.isEmpty()) {
            this.mAppOpPermissionPackages.remove(permissionName);
        }
    }

    public com.android.server.pm.permission.Permission enforcePermissionTree(java.lang.String permissionName, int callingUid) {
        return com.android.server.pm.permission.Permission.enforcePermissionTree(this.mPermissionTrees.values(), permissionName, callingUid);
    }
}
