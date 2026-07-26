package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class Permission {
    private static final java.lang.String TAG = "Permission";
    public static final int TYPE_CONFIG = 1;
    public static final int TYPE_DYNAMIC = 2;
    public static final int TYPE_MANIFEST = 0;
    private boolean mDefinitionChanged;
    private int[] mGids;
    private boolean mGidsPerUser;
    private android.content.pm.PermissionInfo mPermissionInfo;
    private boolean mReconciled;
    private final int mType;
    private int mUid;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PermissionType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ProtectionLevel {
    }

    public Permission(java.lang.String name, java.lang.String packageName, int type) {
        this.mGids = libcore.util.EmptyArray.INT;
        this.mPermissionInfo = new android.content.pm.PermissionInfo();
        this.mPermissionInfo.name = name;
        this.mPermissionInfo.packageName = packageName;
        this.mPermissionInfo.protectionLevel = 2;
        this.mType = type;
    }

    public Permission(android.content.pm.PermissionInfo permissionInfo, int type) {
        this.mGids = libcore.util.EmptyArray.INT;
        this.mPermissionInfo = permissionInfo;
        this.mType = type;
    }

    public Permission(android.content.pm.PermissionInfo permissionInfo, int type, boolean reconciled, int uid, int[] gids, boolean gidsPerUser) {
        this(permissionInfo, type);
        this.mReconciled = reconciled;
        this.mUid = uid;
        this.mGids = gids;
        this.mGidsPerUser = gidsPerUser;
    }

    public android.content.pm.PermissionInfo getPermissionInfo() {
        return this.mPermissionInfo;
    }

    public void setPermissionInfo(android.content.pm.PermissionInfo permissionInfo) {
        if (permissionInfo != null) {
            this.mPermissionInfo = permissionInfo;
        } else {
            android.content.pm.PermissionInfo newPermissionInfo = new android.content.pm.PermissionInfo();
            newPermissionInfo.name = this.mPermissionInfo.name;
            newPermissionInfo.packageName = this.mPermissionInfo.packageName;
            newPermissionInfo.protectionLevel = this.mPermissionInfo.protectionLevel;
            this.mPermissionInfo = newPermissionInfo;
        }
        this.mReconciled = permissionInfo != null;
    }

    public java.lang.String getName() {
        return this.mPermissionInfo.name;
    }

    public int getProtectionLevel() {
        return this.mPermissionInfo.protectionLevel;
    }

    public java.lang.String getPackageName() {
        return this.mPermissionInfo.packageName;
    }

    public int getType() {
        return this.mType;
    }

    public int getUid() {
        return this.mUid;
    }

    public boolean hasGids() {
        return this.mGids.length != 0;
    }

    public int[] getRawGids() {
        return this.mGids;
    }

    public boolean areGidsPerUser() {
        return this.mGidsPerUser;
    }

    public void setGids(int[] gids, boolean gidsPerUser) {
        this.mGids = gids;
        this.mGidsPerUser = gidsPerUser;
    }

    public int[] computeGids(int userId) {
        if (!this.mGidsPerUser) {
            return this.mGids.length != 0 ? (int[]) this.mGids.clone() : this.mGids;
        }
        int[] userGids = new int[this.mGids.length];
        for (int i = 0; i < this.mGids.length; i++) {
            int gid = this.mGids[i];
            userGids[i] = android.os.UserHandle.getUid(userId, gid);
        }
        return userGids;
    }

    public boolean isDefinitionChanged() {
        return this.mDefinitionChanged;
    }

    public void setDefinitionChanged(boolean definitionChanged) {
        this.mDefinitionChanged = definitionChanged;
    }

    public int calculateFootprint(com.android.server.pm.permission.Permission permission) {
        if (this.mUid == permission.mUid) {
            return permission.mPermissionInfo.name.length() + permission.mPermissionInfo.calculateFootprint();
        }
        return 0;
    }

    public boolean isPermission(com.android.internal.pm.pkg.component.ParsedPermission parsedPermission) {
        return this.mPermissionInfo != null && java.util.Objects.equals(this.mPermissionInfo.packageName, parsedPermission.getPackageName()) && java.util.Objects.equals(this.mPermissionInfo.name, parsedPermission.getName());
    }

    public boolean isDynamic() {
        return this.mType == 2;
    }

    public boolean isNormal() {
        return (this.mPermissionInfo.protectionLevel & 15) == 0;
    }

    public boolean isRuntime() {
        return (this.mPermissionInfo.protectionLevel & 15) == 1;
    }

    public boolean isRemoved() {
        return (this.mPermissionInfo.flags & 2) != 0;
    }

    public boolean isSoftRestricted() {
        return (this.mPermissionInfo.flags & 8) != 0;
    }

    public boolean isHardRestricted() {
        return (this.mPermissionInfo.flags & 4) != 0;
    }

    public boolean isHardOrSoftRestricted() {
        return (this.mPermissionInfo.flags & 12) != 0;
    }

    public boolean isImmutablyRestricted() {
        return (this.mPermissionInfo.flags & 16) != 0;
    }

    public boolean isSignature() {
        return (this.mPermissionInfo.protectionLevel & 15) == 2;
    }

    public boolean isInternal() {
        return (this.mPermissionInfo.protectionLevel & 15) == 4;
    }

    public boolean isAppOp() {
        return (this.mPermissionInfo.protectionLevel & 64) != 0;
    }

    public boolean isDevelopment() {
        return isSignature() && (this.mPermissionInfo.protectionLevel & 32) != 0;
    }

    public boolean isInstaller() {
        return (this.mPermissionInfo.protectionLevel & 256) != 0;
    }

    public boolean isInstant() {
        return (this.mPermissionInfo.protectionLevel & 4096) != 0;
    }

    public boolean isOem() {
        return (this.mPermissionInfo.protectionLevel & 16384) != 0;
    }

    public boolean isPre23() {
        return (this.mPermissionInfo.protectionLevel & 128) != 0;
    }

    public boolean isPreInstalled() {
        return (this.mPermissionInfo.protectionLevel & 1024) != 0;
    }

    public boolean isPrivileged() {
        return (this.mPermissionInfo.protectionLevel & 16) != 0;
    }

    public boolean isRuntimeOnly() {
        return (this.mPermissionInfo.protectionLevel & 8192) != 0;
    }

    public boolean isSetup() {
        return (this.mPermissionInfo.protectionLevel & 2048) != 0;
    }

    public boolean isVerifier() {
        return (this.mPermissionInfo.protectionLevel & 512) != 0;
    }

    public boolean isVendorPrivileged() {
        return (this.mPermissionInfo.protectionLevel & 32768) != 0;
    }

    public boolean isSystemTextClassifier() {
        return (this.mPermissionInfo.protectionLevel & 65536) != 0;
    }

    public boolean isConfigurator() {
        return (this.mPermissionInfo.protectionLevel & 524288) != 0;
    }

    public boolean isIncidentReportApprover() {
        return (this.mPermissionInfo.protectionLevel & 1048576) != 0;
    }

    public boolean isAppPredictor() {
        return (this.mPermissionInfo.protectionLevel & 2097152) != 0;
    }

    public boolean isCompanion() {
        return (this.mPermissionInfo.protectionLevel & 8388608) != 0;
    }

    public boolean isModule() {
        return (this.mPermissionInfo.protectionLevel & 4194304) != 0;
    }

    public boolean isRetailDemo() {
        return (this.mPermissionInfo.protectionLevel & 16777216) != 0;
    }

    public boolean isRecents() {
        return (this.mPermissionInfo.protectionLevel & 33554432) != 0;
    }

    public boolean isRole() {
        return (this.mPermissionInfo.protectionLevel & 67108864) != 0;
    }

    public boolean isKnownSigner() {
        return (this.mPermissionInfo.protectionLevel & 134217728) != 0;
    }

    public java.util.Set<java.lang.String> getKnownCerts() {
        return this.mPermissionInfo.knownCerts;
    }

    public void transfer(java.lang.String oldPackageName, java.lang.String newPackageName) {
        if (!oldPackageName.equals(this.mPermissionInfo.packageName)) {
            return;
        }
        android.content.pm.PermissionInfo newPermissionInfo = new android.content.pm.PermissionInfo();
        newPermissionInfo.name = this.mPermissionInfo.name;
        newPermissionInfo.packageName = newPackageName;
        newPermissionInfo.protectionLevel = this.mPermissionInfo.protectionLevel;
        this.mPermissionInfo = newPermissionInfo;
        this.mReconciled = false;
        this.mUid = 0;
        this.mGids = libcore.util.EmptyArray.INT;
        this.mGidsPerUser = false;
    }

    public boolean addToTree(int protectionLevel, android.content.pm.PermissionInfo permissionInfo, com.android.server.pm.permission.Permission permissionTree) {
        boolean changed = (this.mPermissionInfo.protectionLevel == protectionLevel && this.mReconciled && this.mUid == permissionTree.mUid && java.util.Objects.equals(this.mPermissionInfo.packageName, permissionTree.mPermissionInfo.packageName) && comparePermissionInfos(this.mPermissionInfo, permissionInfo)) ? false : true;
        this.mPermissionInfo = new android.content.pm.PermissionInfo(permissionInfo);
        this.mPermissionInfo.packageName = permissionTree.mPermissionInfo.packageName;
        this.mPermissionInfo.protectionLevel = protectionLevel;
        this.mReconciled = true;
        this.mUid = permissionTree.mUid;
        return changed;
    }

    public void updateDynamicPermission(java.util.Collection<com.android.server.pm.permission.Permission> permissionTrees) {
        com.android.server.pm.permission.Permission tree;
        if (com.android.server.pm.PackageManagerService.DEBUG_SETTINGS) {
            android.util.Log.v(TAG, "Dynamic permission: name=" + getName() + " pkg=" + getPackageName() + " info=" + this.mPermissionInfo);
        }
        if (this.mType == 2 && (tree = findPermissionTree(permissionTrees, this.mPermissionInfo.name)) != null) {
            this.mPermissionInfo.packageName = tree.mPermissionInfo.packageName;
            this.mReconciled = true;
            this.mUid = tree.mUid;
        }
    }

    public static boolean isOverridingSystemPermission(com.android.server.pm.permission.Permission permission, android.content.pm.PermissionInfo permissionInfo, android.content.pm.PackageManagerInternal packageManagerInternal) {
        com.android.server.pm.pkg.PackageStateInternal currentPackageState;
        if (permission == null || java.util.Objects.equals(permission.mPermissionInfo.packageName, permissionInfo.packageName) || !permission.mReconciled || (currentPackageState = packageManagerInternal.getPackageStateInternal(permission.mPermissionInfo.packageName)) == null) {
            return false;
        }
        return currentPackageState.isSystem();
    }

    public static com.android.server.pm.permission.Permission createOrUpdate(com.android.server.pm.permission.Permission permission, android.content.pm.PermissionInfo permissionInfo, com.android.server.pm.pkg.PackageState packageState, java.util.Collection<com.android.server.pm.permission.Permission> permissionTrees, boolean isOverridingSystemPermission) {
        com.android.server.pm.permission.Permission permission2 = permission;
        boolean ownerChanged = false;
        if (permission2 != null && !java.util.Objects.equals(permission2.mPermissionInfo.packageName, permissionInfo.packageName) && packageState.isSystem()) {
            if (permission2.mType == 1 && !permission2.mReconciled) {
                permission2.mPermissionInfo = permissionInfo;
                permission2.mReconciled = true;
                permission2.mUid = packageState.getAppId();
            } else if (!isOverridingSystemPermission) {
                android.util.Slog.w(TAG, "New decl " + packageState + " of permission  " + permissionInfo.name + " is system; overriding " + permission2.mPermissionInfo.packageName);
                ownerChanged = true;
                permission2 = null;
            }
        }
        boolean wasNonInternal = (permission2 == null || permission2.mType == 1 || permission2.isInternal()) ? false : true;
        boolean wasNonRuntime = (permission2 == null || permission2.mType == 1 || permission2.isRuntime()) ? false : true;
        if (permission2 == null) {
            permission2 = new com.android.server.pm.permission.Permission(permissionInfo.name, permissionInfo.packageName, 0);
        }
        java.lang.StringBuilder r = null;
        if (permission2.mReconciled) {
            if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                if (0 == 0) {
                    r = new java.lang.StringBuilder(256);
                } else {
                    r.append(' ');
                }
                r.append("DUP:");
                r.append(permissionInfo.name);
            }
        } else if (permission2.mPermissionInfo.packageName != null && !permission2.mPermissionInfo.packageName.equals(permissionInfo.packageName)) {
            android.util.Slog.w(TAG, "Permission " + permissionInfo.name + " from package " + permissionInfo.packageName + " ignored: original from " + permission2.mPermissionInfo.packageName);
        } else {
            com.android.server.pm.permission.Permission tree = findPermissionTree(permissionTrees, permissionInfo.name);
            if (tree != null && !tree.mPermissionInfo.packageName.equals(permissionInfo.packageName)) {
                android.util.Slog.w(TAG, "Permission " + permissionInfo.name + " from package " + permissionInfo.packageName + " ignored: base tree " + tree.mPermissionInfo.name + " is from package " + tree.mPermissionInfo.packageName);
            } else {
                permission2.mPermissionInfo = permissionInfo;
                permission2.mReconciled = true;
                permission2.mUid = packageState.getAppId();
                if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                    if (0 == 0) {
                        r = new java.lang.StringBuilder(256);
                    } else {
                        r.append(' ');
                    }
                    r.append(permissionInfo.name);
                }
            }
        }
        if ((permission2.isInternal() && (ownerChanged || wasNonInternal)) || (permission2.isRuntime() && (ownerChanged || wasNonRuntime))) {
            permission2.mDefinitionChanged = true;
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING && r != null) {
            android.util.Log.d(TAG, "  Permissions: " + ((java.lang.Object) r));
        }
        return permission2;
    }

    public static com.android.server.pm.permission.Permission enforcePermissionTree(java.util.Collection<com.android.server.pm.permission.Permission> permissionTrees, java.lang.String permissionName, int callingUid) {
        com.android.server.pm.permission.Permission permissionTree;
        if (permissionName != null && (permissionTree = findPermissionTree(permissionTrees, permissionName)) != null && permissionTree.getUid() == android.os.UserHandle.getAppId(callingUid)) {
            return permissionTree;
        }
        throw new java.lang.SecurityException("Calling uid " + callingUid + " is not allowed to add to or remove from the permission tree");
    }

    private static com.android.server.pm.permission.Permission findPermissionTree(java.util.Collection<com.android.server.pm.permission.Permission> permissionTrees, java.lang.String permissionName) {
        for (com.android.server.pm.permission.Permission permissionTree : permissionTrees) {
            java.lang.String permissionTreeName = permissionTree.getName();
            if (permissionName.startsWith(permissionTreeName) && permissionName.length() > permissionTreeName.length() && permissionName.charAt(permissionTreeName.length()) == '.') {
                return permissionTree;
            }
        }
        return null;
    }

    public java.lang.String getBackgroundPermission() {
        return this.mPermissionInfo.backgroundPermission;
    }

    public java.lang.String getGroup() {
        return this.mPermissionInfo.group;
    }

    public int getProtection() {
        return this.mPermissionInfo.protectionLevel & 15;
    }

    public int getProtectionFlags() {
        return this.mPermissionInfo.protectionLevel & 65520;
    }

    public android.content.pm.PermissionInfo generatePermissionInfo(int flags) {
        return generatePermissionInfo(flags, 10000);
    }

    public android.content.pm.PermissionInfo generatePermissionInfo(int flags, int targetSdkVersion) {
        android.content.pm.PermissionInfo permissionInfo;
        int protection;
        if (this.mPermissionInfo != null) {
            permissionInfo = new android.content.pm.PermissionInfo(this.mPermissionInfo);
            if ((flags & 128) != 128) {
                permissionInfo.metaData = null;
            }
        } else {
            permissionInfo = new android.content.pm.PermissionInfo();
            permissionInfo.name = this.mPermissionInfo.name;
            permissionInfo.packageName = this.mPermissionInfo.packageName;
            permissionInfo.nonLocalizedLabel = this.mPermissionInfo.name;
        }
        permissionInfo.flags |= 1073741824;
        if (targetSdkVersion >= 26 || (protection = this.mPermissionInfo.protectionLevel & 15) == 2) {
            permissionInfo.protectionLevel = this.mPermissionInfo.protectionLevel;
        } else {
            permissionInfo.protectionLevel = protection;
        }
        return permissionInfo;
    }

    private static boolean comparePermissionInfos(android.content.pm.PermissionInfo pi1, android.content.pm.PermissionInfo pi2) {
        return pi1.icon == pi2.icon && pi1.logo == pi2.logo && pi1.protectionLevel == pi2.protectionLevel && java.util.Objects.equals(pi1.name, pi2.name) && java.util.Objects.equals(pi1.nonLocalizedLabel, pi2.nonLocalizedLabel) && java.util.Objects.equals(pi1.packageName, pi2.packageName);
    }
}
