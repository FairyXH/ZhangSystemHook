package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: AppIdPermissionMigration.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005¢\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0004H\u0002J:\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00060\u000e2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00120\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0002J\u0015\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0000¢\u0006\u0002\b\u0018J\u001d\u0010\u0019\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\n\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u001a¨\u0006\u001c"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionMigration;", "", "()V", "migratePermissionFlags", "", com.android.server.permission.access.PermissionUri.SCHEME, "Lcom/android/server/permission/access/permission/Permission;", "legacyPermissionState", "Lcom/android/server/pm/permission/PermissionMigrationHelper$LegacyPermissionState;", "appId", "userId", "migratePermissions", "", "permissions", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "", "legacyPermissions", "", "Lcom/android/server/pm/permission/PermissionMigrationHelper$LegacyPermission;", "isPermissionTree", "", "migrateSystemState", "state", "Lcom/android/server/permission/access/MutableAccessState;", "migrateSystemState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "migrateUserState", "migrateUserState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdPermissionMigration {
    private static final boolean DEBUG_MIGRATION = false;
    public static final com.android.server.permission.access.permission.AppIdPermissionMigration.Companion Companion = new com.android.server.permission.access.permission.AppIdPermissionMigration.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.AppIdPermissionMigration.class.getSimpleName();

    public final void migrateSystemState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(com.android.server.permission.access.MutableAccessState state) {
        java.lang.Object service = com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionMigrationHelper.class);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(service);
        com.android.server.pm.permission.PermissionMigrationHelper legacyPermissionsManager = (com.android.server.pm.permission.PermissionMigrationHelper) service;
        if (!legacyPermissionsManager.hasLegacyPermission()) {
            return;
        }
        migratePermissions$default(this, com.android.server.permission.access.MutableAccessState.mutateSystemState$default(state, 0, 1, null).mutatePermissions(), legacyPermissionsManager.getLegacyPermissions(), false, 4, null);
        migratePermissions(com.android.server.permission.access.MutableAccessState.mutateSystemState$default(state, 0, 1, null).mutatePermissionTrees(), legacyPermissionsManager.getLegacyPermissionTrees(), true);
    }

    static /* synthetic */ void migratePermissions$default(com.android.server.permission.access.permission.AppIdPermissionMigration appIdPermissionMigration, com.android.server.permission.access.immutable.MutableIndexedMap mutableIndexedMap, java.util.Map map, boolean z, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        appIdPermissionMigration.migratePermissions(mutableIndexedMap, map, z);
    }

    private final void migratePermissions(com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> mutableIndexedMap, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> map, boolean isPermissionTree) {
        java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission legacyPermission = it.next().getValue();
            com.android.server.permission.access.permission.Permission permission = new com.android.server.permission.access.permission.Permission(legacyPermission.getPermissionInfo(), false, legacyPermission.getType(), 0, null, false, 48, null);
            mutableIndexedMap.put(permission.getPermissionInfo().name, permission);
        }
    }

    public final void migrateUserState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(com.android.server.permission.access.MutableAccessState state, int userId) {
        com.android.server.pm.permission.PermissionMigrationHelper permissionMigrationHelper;
        java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> map;
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIntReferenceMap;
        java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> map2;
        int $i$f$forEach;
        java.util.Iterator<java.util.Map.Entry<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>>> it;
        java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> map3;
        int $i$f$forEach2;
        java.util.Iterator<java.util.Map.Entry<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>>> it2;
        int i = userId;
        java.lang.Object service = com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionMigrationHelper.class);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(service);
        com.android.server.pm.permission.PermissionMigrationHelper permissionMigrationHelper2 = (com.android.server.pm.permission.PermissionMigrationHelper) service;
        if (!permissionMigrationHelper2.hasLegacyPermissionState(i)) {
            return;
        }
        java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> legacyPermissionStates = permissionMigrationHelper2.getLegacyPermissionStates(i);
        int version = com.android.server.permission.access.util.PackageVersionMigration.INSTANCE.getVersion$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(i);
        com.android.server.permission.access.MutableUserState userState = com.android.server.permission.access.MutableAccessState.mutateUserState$default(state, i, 0, 2, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(userState);
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIntReferenceMapMutateAppIdPermissionFlags = userState.mutateAppIdPermissionFlags();
        java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> map4 = legacyPermissionStates;
        int $i$f$forEach3 = 0;
        java.util.Iterator<java.util.Map.Entry<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>>> it3 = map4.entrySet().iterator();
        while (it3.hasNext()) {
            java.util.Map.Entry<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> next = it3.next();
            java.lang.Integer appId = next.getKey();
            java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState> value = next.getValue();
            com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) state.getExternalState().getAppIdPackageNames().get(appId.intValue());
            if (packageNames == null) {
                permissionMigrationHelper = permissionMigrationHelper2;
                map = legacyPermissionStates;
                android.util.Slog.w(LOG_TAG, "Dropping unknown app ID " + appId + " when migrating permission state");
                mutableIntReferenceMap = mutableIntReferenceMapMutateAppIdPermissionFlags;
                map2 = map4;
                $i$f$forEach = $i$f$forEach3;
                it = it3;
            } else {
                permissionMigrationHelper = permissionMigrationHelper2;
                map = legacyPermissionStates;
                com.android.server.permission.access.immutable.MutableIndexedMap permissionFlags = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
                com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.set(mutableIntReferenceMapMutateAppIdPermissionFlags, appId.intValue(), permissionFlags);
                java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState> map5 = value;
                int $i$f$forEach4 = 0;
                for (java.util.Map.Entry<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState> entry : map5.entrySet()) {
                    java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState> map6 = map5;
                    java.lang.String permissionName = entry.getKey();
                    com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIntReferenceMap2 = mutableIntReferenceMapMutateAppIdPermissionFlags;
                    com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState legacyPermissionState = entry.getValue();
                    int $i$f$forEach5 = $i$f$forEach4;
                    com.android.server.permission.access.permission.Permission permission = state.getSystemState().getPermissions().get(permissionName);
                    if (permission == null) {
                        map3 = map4;
                        $i$f$forEach2 = $i$f$forEach3;
                        it2 = it3;
                        android.util.Slog.w(LOG_TAG, "Dropping unknown permission " + permissionName + " for app ID " + appId + " when migrating permission state");
                    } else {
                        map3 = map4;
                        $i$f$forEach2 = $i$f$forEach3;
                        it2 = it3;
                        permissionFlags.put(permissionName, java.lang.Integer.valueOf(migratePermissionFlags(permission, legacyPermissionState, appId.intValue(), i)));
                    }
                    map4 = map3;
                    map5 = map6;
                    mutableIntReferenceMapMutateAppIdPermissionFlags = mutableIntReferenceMap2;
                    $i$f$forEach4 = $i$f$forEach5;
                    $i$f$forEach3 = $i$f$forEach2;
                    it3 = it2;
                }
                mutableIntReferenceMap = mutableIntReferenceMapMutateAppIdPermissionFlags;
                map2 = map4;
                $i$f$forEach = $i$f$forEach3;
                it = it3;
                com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMapMutatePackageVersions = userState.mutatePackageVersions();
                int index$iv = 0;
                int size = packageNames.getSize();
                while (index$iv < size) {
                    java.lang.String packageName = (java.lang.String) packageNames.elementAt(index$iv);
                    mutableIndexedMapMutatePackageVersions.put(packageName, java.lang.Integer.valueOf(version));
                    index$iv++;
                    permissionFlags = permissionFlags;
                }
            }
            i = userId;
            permissionMigrationHelper2 = permissionMigrationHelper;
            legacyPermissionStates = map;
            map4 = map2;
            mutableIntReferenceMapMutateAppIdPermissionFlags = mutableIntReferenceMap;
            $i$f$forEach3 = $i$f$forEach;
            it3 = it;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int migratePermissionFlags(com.android.server.permission.access.permission.Permission r9, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState r10, int r11, int r12) {
        /*
            r8 = this;
            r0 = r9
            r1 = 0
            r2 = r0
            r3 = 0
            android.content.pm.PermissionInfo r4 = r2.getPermissionInfo()
            int r2 = r4.getProtection()
            r3 = 1
            r4 = 0
            if (r2 != 0) goto L13
            r2 = r3
            goto L14
        L13:
            r2 = r4
        L14:
            r0 = 2
            if (r2 == 0) goto L23
            boolean r1 = r10.isGranted()
            if (r1 == 0) goto L20
            goto L9f
        L20:
            r3 = r0
            goto L9f
        L23:
            r1 = r9
            r2 = 0
            r5 = r1
            r6 = 0
            android.content.pm.PermissionInfo r7 = r5.getPermissionInfo()
            int r5 = r7.getProtection()
            if (r5 != r0) goto L33
            r0 = r3
            goto L34
        L33:
            r0 = r4
        L34:
            r1 = 4
            if (r0 != 0) goto L6a
            r0 = r9
            r2 = 0
            r5 = r0
            r6 = 0
            android.content.pm.PermissionInfo r7 = r5.getPermissionInfo()
            int r5 = r7.getProtection()
            if (r5 != r1) goto L47
            r0 = r3
            goto L48
        L47:
            r0 = r4
        L48:
            if (r0 == 0) goto L4b
            goto L6a
        L4b:
            r0 = r9
            r1 = 0
            r2 = r0
            r5 = 0
            android.content.pm.PermissionInfo r6 = r2.getPermissionInfo()
            int r2 = r6.getProtection()
            if (r2 != r3) goto L5a
            goto L5b
        L5a:
            r3 = r4
        L5b:
            if (r3 == 0) goto L68
            boolean r0 = r10.isGranted()
            if (r0 == 0) goto L66
            r3 = 16
            goto L9f
        L66:
            r3 = r4
            goto L9f
        L68:
            r3 = r4
            goto L9f
        L6a:
            boolean r0 = r10.isGranted()
            if (r0 == 0) goto L9e
            r0 = r9
            r2 = 0
            r3 = r0
            r4 = 0
            android.content.pm.PermissionInfo r5 = r3.getPermissionInfo()
            int r3 = r5.getProtectionFlags()
            r4 = 32
            boolean r0 = com.android.server.permission.access.util.IntExtensionsKt.hasBits(r3, r4)
            if (r0 != 0) goto L9b
            r0 = r9
            r2 = 0
            r3 = r0
            r4 = 0
            android.content.pm.PermissionInfo r5 = r3.getPermissionInfo()
            int r3 = r5.getProtectionFlags()
            r4 = 67108864(0x4000000, float:1.5046328E-36)
            boolean r0 = com.android.server.permission.access.util.IntExtensionsKt.hasBits(r3, r4)
            if (r0 == 0) goto L99
            goto L9b
        L99:
            r3 = r1
            goto L9f
        L9b:
            r3 = 20
            goto L9f
        L9e:
            r3 = r4
        L9f:
            r0 = r3
            com.android.server.permission.access.permission.PermissionFlags r1 = com.android.server.permission.access.permission.PermissionFlags.INSTANCE
            int r2 = r10.getFlags()
            int r3 = r10.getFlags()
            int r1 = r1.updateFlags(r9, r0, r2, r3)
            r0 = r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionMigration.migratePermissionFlags(com.android.server.permission.access.permission.Permission, com.android.server.pm.permission.PermissionMigrationHelper$LegacyPermissionState, int, int):int");
    }

    /* JADX INFO: compiled from: AppIdPermissionMigration.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionMigration$Companion;", "", "()V", "DEBUG_MIGRATION", "", "LOG_TAG", "", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
