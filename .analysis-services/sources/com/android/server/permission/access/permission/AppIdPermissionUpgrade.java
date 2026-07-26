package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: AppIdPermissionUpgrade.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J$\u0010\f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u001c\u0010\u000f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u001c\u0010\u0010\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u001c\u0010\u0011\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u001c\u0010\u0012\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\"\u0010\u0013\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000bJ\u001c\u0010\u0015\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionUpgrade;", "", "policy", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy;", "(Lcom/android/server/permission/access/permission/AppIdPermissionPolicy;)V", "allowlistRestrictedPermissions", "", "Lcom/android/server/permission/access/MutateStateScope;", "packageState", "Lcom/android/server/pm/pkg/PackageState;", "userId", "", "grantRuntimePermission", "permissionName", "", "upgradeAccessMediaLocationPermission", "upgradeAuralVisualMediaPermissions", "upgradeBackgroundLocationPermission", "upgradeBodySensorPermissions", "upgradePackageState", "version", "upgradeUserSelectedVisualMediaPermission", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdPermissionUpgrade {
    private static final int MASK_ANY_FIXED = 480;
    private final com.android.server.permission.access.permission.AppIdPermissionPolicy policy;
    public static final com.android.server.permission.access.permission.AppIdPermissionUpgrade.Companion Companion = new com.android.server.permission.access.permission.AppIdPermissionUpgrade.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.AppIdPermissionUpgrade.class.getSimpleName();
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> LEGACY_RESTRICTED_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS", "android.permission.READ_CELL_BROADCASTS", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.PROCESS_OUTGOING_CALLS");
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> STORAGE_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> AURAL_VISUAL_MEDIA_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.ACCESS_MEDIA_LOCATION", "android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> VISUAL_MEDIA_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.ACCESS_MEDIA_LOCATION");

    public AppIdPermissionUpgrade(com.android.server.permission.access.permission.AppIdPermissionPolicy policy) {
        this.policy = policy;
    }

    public final void upgradePackageState(com.android.server.permission.access.MutateStateScope $this$upgradePackageState, com.android.server.pm.pkg.PackageState packageState, int userId, int version) {
        java.lang.String packageName = packageState.getPackageName();
        if (version <= 3) {
            android.util.Slog.v(LOG_TAG, "Allowlisting and upgrading background location permission for package: " + packageName + ", version: " + version + ", user:" + userId);
            allowlistRestrictedPermissions($this$upgradePackageState, packageState, userId);
            upgradeBackgroundLocationPermission($this$upgradePackageState, packageState, userId);
        }
        if (version <= 10) {
            android.util.Slog.v(LOG_TAG, "Upgrading access media location permission for package: " + packageName + ", version: " + version + ", user: " + userId);
            upgradeAccessMediaLocationPermission($this$upgradePackageState, packageState, userId);
        }
        if (version <= 12) {
            android.util.Slog.v(LOG_TAG, "Upgrading scoped media and body sensor permissions for package: " + packageName + ", version: " + version + ", user: " + userId);
            upgradeAuralVisualMediaPermissions($this$upgradePackageState, packageState, userId);
            upgradeBodySensorPermissions($this$upgradePackageState, packageState, userId);
        }
        if (version <= 14) {
            android.util.Slog.v(LOG_TAG, "Upgrading visual media permission for package: " + packageName + ", version: " + version + ", user: " + userId);
            upgradeUserSelectedVisualMediaPermission($this$upgradePackageState, packageState, userId);
        }
    }

    private final void allowlistRestrictedPermissions(com.android.server.permission.access.MutateStateScope $this$allowlistRestrictedPermissions, com.android.server.pm.pkg.PackageState packageState, int userId) {
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
        java.lang.Iterable $this$forEach$iv = androidPackage.getRequestedPermissions();
        for (java.lang.Object element$iv : $this$forEach$iv) {
            java.lang.String permissionName = (java.lang.String) element$iv;
            if (LEGACY_RESTRICTED_PERMISSIONS.contains(permissionName)) {
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$allowlistRestrictedPermissions_u24lambda_u241_u24lambda_u240 = this.policy;
                $this$allowlistRestrictedPermissions_u24lambda_u241_u24lambda_u240.updatePermissionFlags($this$allowlistRestrictedPermissions, packageState.getAppId(), userId, permissionName, 131072, 131072);
            }
        }
    }

    private final void upgradeBackgroundLocationPermission(com.android.server.permission.access.MutateStateScope $this$upgradeBackgroundLocationPermission, com.android.server.pm.pkg.PackageState packageState, int userId) {
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
        if (androidPackage.getRequestedPermissions().contains("android.permission.ACCESS_BACKGROUND_LOCATION")) {
            int appId = packageState.getAppId();
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeBackgroundLocationPermission_u24lambda_u242 = this.policy;
            int accessFineLocationFlags = $this$upgradeBackgroundLocationPermission_u24lambda_u242.getPermissionFlags($this$upgradeBackgroundLocationPermission, appId, userId, "android.permission.ACCESS_FINE_LOCATION");
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeBackgroundLocationPermission_u24lambda_u243 = this.policy;
            int accessCoarseLocationFlags = $this$upgradeBackgroundLocationPermission_u24lambda_u243.getPermissionFlags($this$upgradeBackgroundLocationPermission, appId, userId, "android.permission.ACCESS_COARSE_LOCATION");
            boolean isForegroundLocationGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(accessFineLocationFlags) || com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(accessCoarseLocationFlags);
            if (isForegroundLocationGranted) {
                grantRuntimePermission($this$upgradeBackgroundLocationPermission, packageState, userId, "android.permission.ACCESS_BACKGROUND_LOCATION");
            }
        }
    }

    private final void upgradeAccessMediaLocationPermission(com.android.server.permission.access.MutateStateScope $this$upgradeAccessMediaLocationPermission, com.android.server.pm.pkg.PackageState packageState, int userId) {
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
        if (androidPackage.getRequestedPermissions().contains("android.permission.ACCESS_MEDIA_LOCATION")) {
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeAccessMediaLocationPermission_u24lambda_u244 = this.policy;
            int flags = $this$upgradeAccessMediaLocationPermission_u24lambda_u244.getPermissionFlags($this$upgradeAccessMediaLocationPermission, packageState.getAppId(), userId, "android.permission.READ_EXTERNAL_STORAGE");
            if (com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(flags)) {
                grantRuntimePermission($this$upgradeAccessMediaLocationPermission, packageState, userId, "android.permission.ACCESS_MEDIA_LOCATION");
            }
        }
    }

    private final void upgradeAuralVisualMediaPermissions(com.android.server.permission.access.MutateStateScope $this$upgradeAuralVisualMediaPermissions, com.android.server.pm.pkg.PackageState packageState, int userId) {
        boolean z;
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet;
        boolean z2;
        com.android.server.pm.pkg.AndroidPackage androidPackage2 = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage2);
        if (androidPackage2.getTargetSdkVersion() < 33) {
            return;
        }
        java.util.Set requestedPermissionNames = androidPackage2.getRequestedPermissions();
        com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet2 = STORAGE_PERMISSIONS;
        int index$iv$iv = 0;
        int size = indexedSet2.getSize();
        while (true) {
            if (index$iv$iv >= size) {
                z = false;
                break;
            }
            java.lang.Object element$iv = indexedSet2.elementAt(index$iv$iv);
            java.lang.String permissionName = (java.lang.String) element$iv;
            if (!requestedPermissionNames.contains(permissionName)) {
                androidPackage = androidPackage2;
                indexedSet = indexedSet2;
                z2 = false;
            } else {
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeAuralVisualMediaPermissions_u24lambda_u246_u24lambda_u245 = this.policy;
                androidPackage = androidPackage2;
                indexedSet = indexedSet2;
                int flags = $this$upgradeAuralVisualMediaPermissions_u24lambda_u246_u24lambda_u245.getPermissionFlags($this$upgradeAuralVisualMediaPermissions, packageState.getAppId(), userId, permissionName);
                z2 = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(flags) && com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 32);
            }
            if (z2) {
                z = true;
                break;
            } else {
                index$iv$iv++;
                androidPackage2 = androidPackage;
                indexedSet2 = indexedSet;
            }
        }
        boolean isStorageUserGranted = z;
        if (isStorageUserGranted) {
            com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet3 = AURAL_VISUAL_MEDIA_PERMISSIONS;
            int size2 = indexedSet3.getSize();
            for (int index$iv = 0; index$iv < size2; index$iv++) {
                java.lang.String permissionName2 = indexedSet3.elementAt(index$iv);
                if (requestedPermissionNames.contains(permissionName2)) {
                    grantRuntimePermission($this$upgradeAuralVisualMediaPermissions, packageState, userId, permissionName2);
                }
            }
        }
    }

    private final void upgradeBodySensorPermissions(com.android.server.permission.access.MutateStateScope $this$upgradeBodySensorPermissions, com.android.server.pm.pkg.PackageState packageState, int userId) {
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
        if (!androidPackage.getRequestedPermissions().contains("android.permission.BODY_SENSORS_BACKGROUND")) {
            return;
        }
        int appId = packageState.getAppId();
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeBodySensorPermissions_u24lambda_u248 = this.policy;
        int backgroundBodySensorsFlags = $this$upgradeBodySensorPermissions_u24lambda_u248.getPermissionFlags($this$upgradeBodySensorPermissions, appId, userId, "android.permission.BODY_SENSORS_BACKGROUND");
        if (com.android.server.permission.access.util.IntExtensionsKt.hasAnyBit(backgroundBodySensorsFlags, com.android.server.permission.access.permission.PermissionFlags.MASK_EXEMPT)) {
            return;
        }
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeBodySensorPermissions_u24lambda_u249 = this.policy;
        $this$upgradeBodySensorPermissions_u24lambda_u249.updatePermissionFlags($this$upgradeBodySensorPermissions, appId, userId, "android.permission.BODY_SENSORS_BACKGROUND", 131072, 131072);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeBodySensorPermissions_u24lambda_u2410 = this.policy;
        int bodySensorsFlags = $this$upgradeBodySensorPermissions_u24lambda_u2410.getPermissionFlags($this$upgradeBodySensorPermissions, appId, userId, "android.permission.BODY_SENSORS");
        boolean isForegroundBodySensorsGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(bodySensorsFlags);
        if (isForegroundBodySensorsGranted) {
            grantRuntimePermission($this$upgradeBodySensorPermissions, packageState, userId, "android.permission.BODY_SENSORS_BACKGROUND");
        }
    }

    private final void upgradeUserSelectedVisualMediaPermission(com.android.server.permission.access.MutateStateScope $this$upgradeUserSelectedVisualMediaPermission, com.android.server.pm.pkg.PackageState packageState, int userId) {
        boolean z;
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet;
        boolean z2;
        com.android.server.pm.pkg.AndroidPackage androidPackage2 = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage2);
        if (androidPackage2.getTargetSdkVersion() < 33) {
            return;
        }
        java.util.Set requestedPermissionNames = androidPackage2.getRequestedPermissions();
        com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet2 = VISUAL_MEDIA_PERMISSIONS;
        int index$iv$iv = 0;
        int size = indexedSet2.getSize();
        while (true) {
            if (index$iv$iv >= size) {
                z = false;
                break;
            }
            java.lang.Object element$iv = indexedSet2.elementAt(index$iv$iv);
            java.lang.String permissionName = (java.lang.String) element$iv;
            if (!requestedPermissionNames.contains(permissionName)) {
                androidPackage = androidPackage2;
                indexedSet = indexedSet2;
                z2 = false;
            } else {
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$upgradeUserSelectedVisualMediaPermission_u24lambda_u2412_u24lambda_u2411 = this.policy;
                androidPackage = androidPackage2;
                indexedSet = indexedSet2;
                int flags = $this$upgradeUserSelectedVisualMediaPermission_u24lambda_u2412_u24lambda_u2411.getPermissionFlags($this$upgradeUserSelectedVisualMediaPermission, packageState.getAppId(), userId, permissionName);
                z2 = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(flags) && com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 32);
            }
            if (z2) {
                z = true;
                break;
            } else {
                index$iv$iv++;
                androidPackage2 = androidPackage;
                indexedSet2 = indexedSet;
            }
        }
        boolean isVisualMediaUserGranted = z;
        if (isVisualMediaUserGranted && requestedPermissionNames.contains("android.permission.READ_MEDIA_VISUAL_USER_SELECTED")) {
            grantRuntimePermission($this$upgradeUserSelectedVisualMediaPermission, packageState, userId, "android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
        }
    }

    private final void grantRuntimePermission(com.android.server.permission.access.MutateStateScope $this$grantRuntimePermission, com.android.server.pm.pkg.PackageState packageState, int userId, java.lang.String permissionName) {
        android.util.Slog.v(LOG_TAG, "Granting runtime permission for package: " + packageState.getPackageName() + ", permission: " + permissionName + ", userId: " + userId);
        com.android.server.permission.access.permission.Permission permission = $this$grantRuntimePermission.getNewState().getSystemState().getPermissions().get(permissionName);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(permission);
        com.android.server.permission.access.permission.Permission permission2 = permission;
        if (packageState.getUserStateOrDefault(userId).isInstantApp() && !com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission2.getPermissionInfo().getProtectionFlags(), 4096)) {
            return;
        }
        int appId = packageState.getAppId();
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$grantRuntimePermission_u24lambda_u2413 = this.policy;
        int flags = $this$grantRuntimePermission_u24lambda_u2413.getPermissionFlags($this$grantRuntimePermission, appId, userId, permissionName);
        if (com.android.server.permission.access.util.IntExtensionsKt.hasAnyBit(flags, 480)) {
            android.util.Slog.v(LOG_TAG, "Not allowed to grant " + permissionName + " to package " + packageState.getPackageName());
        } else {
            int flags2 = com.android.server.permission.access.util.IntExtensionsKt.andInv(flags | 16, 7345152);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$grantRuntimePermission_u24lambda_u2414 = this.policy;
            $this$grantRuntimePermission_u24lambda_u2414.setPermissionFlags($this$grantRuntimePermission, appId, userId, permissionName, flags2);
        }
    }

    /* JADX INFO: compiled from: AppIdPermissionUpgrade.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionUpgrade$Companion;", "", "()V", "AURAL_VISUAL_MEDIA_PERMISSIONS", "Lcom/android/server/permission/access/immutable/IndexedSet;", "", "LEGACY_RESTRICTED_PERMISSIONS", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "MASK_ANY_FIXED", "", "STORAGE_PERMISSIONS", "VISUAL_MEDIA_PERMISSIONS", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
