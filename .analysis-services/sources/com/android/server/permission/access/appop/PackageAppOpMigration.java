package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: PackageAppOpMigration.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\n"}, d2 = {"Lcom/android/server/permission/access/appop/PackageAppOpMigration;", "", "()V", "migrateUserState", "", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PackageAppOpMigration {
    public static final com.android.server.permission.access.appop.PackageAppOpMigration.Companion Companion = new com.android.server.permission.access.appop.PackageAppOpMigration.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.appop.PackageAppOpMigration.class.getSimpleName();

    public final void migrateUserState(com.android.server.permission.access.MutableAccessState state, int userId) {
        com.android.server.appop.AppOpMigrationHelper legacyAppOpsManager;
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> map;
        java.lang.Object service = com.android.server.LocalServices.getService(com.android.server.appop.AppOpMigrationHelper.class);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(service);
        com.android.server.appop.AppOpMigrationHelper legacyAppOpsManager2 = (com.android.server.appop.AppOpMigrationHelper) service;
        if (!legacyAppOpsManager2.hasLegacyAppOpState()) {
            return;
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> legacyPackageAppOpModes = legacyAppOpsManager2.getLegacyPackageAppOpModes(userId);
        int version = com.android.server.permission.access.util.PackageVersionMigration.INSTANCE.getVersion$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(userId);
        com.android.server.permission.access.MutableUserState userState = com.android.server.permission.access.MutableAccessState.mutateUserState$default(state, userId, 0, 2, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(userState);
        com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIndexedReferenceMapMutatePackageAppOpModes = userState.mutatePackageAppOpModes();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> entry : legacyPackageAppOpModes.entrySet()) {
            java.lang.String packageName = entry.getKey();
            java.util.Map<java.lang.String, java.lang.Integer> value = entry.getValue();
            if (!state.getExternalState().getPackageStates().containsKey(packageName)) {
                android.util.Slog.w(LOG_TAG, "Dropping unknown package " + packageName + " when migrating app op state");
                legacyAppOpsManager = legacyAppOpsManager2;
                map = legacyPackageAppOpModes;
            } else {
                com.android.server.permission.access.immutable.MutableIndexedMap $this$set$iv = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
                mutableIndexedReferenceMapMutatePackageAppOpModes.put(packageName, $this$set$iv);
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry2 : value.entrySet()) {
                    com.android.server.appop.AppOpMigrationHelper legacyAppOpsManager3 = legacyAppOpsManager2;
                    java.lang.String appOpName = entry2.getKey();
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> map2 = legacyPackageAppOpModes;
                    java.lang.Integer appOpMode = entry2.getValue();
                    $this$set$iv.put(appOpName, appOpMode);
                    legacyAppOpsManager2 = legacyAppOpsManager3;
                    legacyPackageAppOpModes = map2;
                }
                legacyAppOpsManager = legacyAppOpsManager2;
                map = legacyPackageAppOpModes;
                userState.mutatePackageVersions().put(packageName, java.lang.Integer.valueOf(version));
            }
            legacyAppOpsManager2 = legacyAppOpsManager;
            legacyPackageAppOpModes = map;
        }
    }

    /* JADX INFO: compiled from: PackageAppOpMigration.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/android/server/permission/access/appop/PackageAppOpMigration$Companion;", "", "()V", "LOG_TAG", "", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
