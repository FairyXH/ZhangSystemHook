package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: DevicePermissionPolicy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\"\n\u0002\b\u0007\u0018\u0000 C2\u00020\u0001:\u0002CDB\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005J3\u0010\u0013\u001a\u00020\u0014*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00140\u001bH\u0082\bJ3\u0010\u001d\u001a\u00020\u0011*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00110\u001bH\u0082\bJ0\u0010\u001f\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0017\u0018\u00010 *\u00020!2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\t2\u0006\u0010#\u001a\u00020\u0017J*\u0010$\u001a\u00020\u0017*\u00020!2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\t2\u0006\u0010#\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\tJ\u0014\u0010'\u001a\u00020\u0011*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0012\u0010(\u001a\u00020\u0011*\u00020\u00152\u0006\u0010%\u001a\u00020\tJ\u0014\u0010)\u001a\u00020\u0011*\u00020\u00152\u0006\u0010*\u001a\u00020\u001cH\u0016J\u001c\u0010+\u001a\u00020\u0011*\u00020\u00152\u0006\u0010,\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J$\u0010-\u001a\u00020\u0011*\u00020\u00152\u0006\u0010,\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0017H\u0016J\f\u0010.\u001a\u00020\u0011*\u00020!H\u0016J,\u0010/\u001a\u00020\u0011*\u00020\u00152\b\u00100\u001a\u0004\u0018\u00010\t2\f\u00101\u001a\b\u0012\u0004\u0012\u00020\t022\u0006\u00103\u001a\u00020\u0014H\u0016J\u001c\u00104\u001a\u00020\u0011*\u0002052\u0006\u0010\u0018\u001a\u0002062\u0006\u0010#\u001a\u00020\u0017H\u0016J\u001a\u00107\u001a\u00020\u0011*\u00020\u00152\u0006\u0010,\u001a\u00020\t2\u0006\u0010#\u001a\u00020\u0017J\u001c\u00108\u001a\u00020\u0011*\u0002092\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u0017H\u0016J2\u0010:\u001a\u00020\u0014*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\t2\u0006\u0010#\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\t2\u0006\u0010;\u001a\u00020\u0017J\u0018\u0010<\u001a\u00020\u0011*\u00020\u00152\f\u0010=\u001a\b\u0012\u0004\u0012\u00020\t0>J\u0014\u0010?\u001a\u00020\u0011*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J<\u0010@\u001a\u00020\u0014*\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\t2\u0006\u0010#\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\t2\u0006\u0010A\u001a\u00020\u00172\u0006\u0010B\u001a\u00020\u0017H\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000b¨\u0006E"}, d2 = {"Lcom/android/server/permission/access/permission/DevicePermissionPolicy;", "Lcom/android/server/permission/access/SchemePolicy;", "()V", "listeners", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "Lcom/android/server/permission/access/permission/DevicePermissionPolicy$OnDevicePermissionFlagsChangedListener;", "listenersLock", "", "objectScheme", "", "getObjectScheme", "()Ljava/lang/String;", "persistence", "Lcom/android/server/permission/access/permission/DevicePermissionPersistence;", "subjectScheme", "getSubjectScheme", "addOnPermissionFlagsChangedListener", "", "listener", "anyPackageInAppId", "", "Lcom/android/server/permission/access/MutateStateScope;", "appId", "", "state", "Lcom/android/server/permission/access/AccessState;", "predicate", "Lkotlin/Function1;", "Lcom/android/server/pm/pkg/PackageState;", "forEachPackageInAppId", "action", "getAllPermissionFlags", "Lcom/android/server/permission/access/immutable/IndexedMap;", "Lcom/android/server/permission/access/GetStateScope;", "persistentDeviceId", "userId", "getPermissionFlags", "deviceId", "permissionName", "onAppIdRemoved", "onDeviceIdRemoved", "onPackageAdded", "packageState", "onPackageRemoved", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "onPackageUninstalled", "onStateMutated", "onStorageVolumeMounted", "volumeUuid", com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, "", "isSystemUpdated", "parseUserState", "Lcom/android/modules/utils/BinaryXmlPullParser;", "Lcom/android/server/permission/access/MutableAccessState;", "resetRuntimePermissions", "serializeUserState", "Lcom/android/modules/utils/BinaryXmlSerializer;", "setPermissionFlags", "flags", "trimDevicePermissionStates", "deviceIds", "", "trimPermissionStates", "updatePermissionFlags", "flagMask", "flagValues", "Companion", "OnDevicePermissionFlagsChangedListener", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DevicePermissionPolicy extends com.android.server.permission.access.SchemePolicy {
    public static final com.android.server.permission.access.permission.DevicePermissionPolicy.Companion Companion = new com.android.server.permission.access.permission.DevicePermissionPolicy.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.DevicePermissionPolicy.class.getSimpleName();
    private final com.android.server.permission.access.permission.DevicePermissionPersistence persistence = new com.android.server.permission.access.permission.DevicePermissionPersistence();
    private volatile com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener> listeners = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
    private final java.lang.Object listenersLock = new java.lang.Object();

    /* JADX INFO: compiled from: DevicePermissionPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J8\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H&J\b\u0010\f\u001a\u00020\u0003H&ø\u0001\u0000\u0082\u0002\u0006\n\u0004\b!0\u0001¨\u0006\rÀ\u0006\u0001"}, d2 = {"Lcom/android/server/permission/access/permission/DevicePermissionPolicy$OnDevicePermissionFlagsChangedListener;", "", "onDevicePermissionFlagsChanged", "", "appId", "", "userId", "deviceId", "", "permissionName", "oldFlags", "newFlags", "onStateMutated", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnDevicePermissionFlagsChangedListener {
        void onDevicePermissionFlagsChanged(int i, int i2, java.lang.String str, java.lang.String str2, int i3, int i4);

        void onStateMutated();
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public java.lang.String getSubjectScheme() {
        return "uid";
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public java.lang.String getObjectScheme() {
        return com.android.server.permission.access.DevicePermissionUri.SCHEME;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStateMutated(com.android.server.permission.access.GetStateScope $this$onStateMutated) {
        com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener> indexedListSet = this.listeners;
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener it = indexedListSet.elementAt(index$iv);
            it.onStateMutated();
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onAppIdRemoved(com.android.server.permission.access.MutateStateScope $this$onAppIdRemoved, int appId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onAppIdRemoved.getNewState().getUserStates();
        int size = userStates.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            int userStateIndex = index$iv;
            if (userState.getAppIdDevicePermissionFlags().contains(appId)) {
                com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign(com.android.server.permission.access.MutableAccessState.mutateUserStateAt$default($this$onAppIdRemoved.getNewState(), userStateIndex, 0, 2, null).mutateAppIdDevicePermissionFlags(), appId);
            }
        }
    }

    public final void trimDevicePermissionStates(com.android.server.permission.access.MutateStateScope $this$trimDevicePermissionStates, java.util.Set<java.lang.String> set) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> intReferenceMap;
        int $i$f$forEachIndexed;
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$trimDevicePermissionStates.getNewState().getUserStates();
        int $i$f$forEachIndexed2 = 0;
        int size = userStates.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int userId = userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> appIdDevicePermissionFlags = userState.getAppIdDevicePermissionFlags();
            int index$iv2 = appIdDevicePermissionFlags.getSize() - 1;
            while (-1 < index$iv2) {
                int appId = appIdDevicePermissionFlags.keyAt(index$iv2);
                com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$trimDevicePermissionStates.getNewState(), userId, 0, 2, null);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
                com.android.server.permission.access.immutable.MutableIndexedReferenceMap devicePermissionFlags = (com.android.server.permission.access.immutable.MutableIndexedReferenceMap) mutableUserStateMutateUserState$default.mutateAppIdDevicePermissionFlags().mutate(appId);
                if (devicePermissionFlags == null) {
                    intReferenceMap = userStates;
                    $i$f$forEachIndexed = $i$f$forEachIndexed2;
                } else {
                    com.android.server.permission.access.immutable.MutableIndexedReferenceMap $this$forEachReversedIndexed$iv = devicePermissionFlags;
                    intReferenceMap = userStates;
                    int index$iv3 = $this$forEachReversedIndexed$iv.getSize() - 1;
                    while (true) {
                        $i$f$forEachIndexed = $i$f$forEachIndexed2;
                        if (-1 < index$iv3) {
                            java.lang.Object objKeyAt = $this$forEachReversedIndexed$iv.keyAt(index$iv3);
                            java.lang.String deviceId = (java.lang.String) objKeyAt;
                            int i = size;
                            if (!set.contains(deviceId)) {
                                devicePermissionFlags.remove(deviceId);
                            }
                            index$iv3--;
                            $i$f$forEachIndexed2 = $i$f$forEachIndexed;
                            size = i;
                        }
                    }
                }
                index$iv2--;
                $i$f$forEachIndexed2 = $i$f$forEachIndexed;
                userStates = intReferenceMap;
                size = size;
            }
        }
    }

    public final void onDeviceIdRemoved(com.android.server.permission.access.MutateStateScope $this$onDeviceIdRemoved, java.lang.String deviceId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> intReferenceMap;
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onDeviceIdRemoved.getNewState().getUserStates();
        int index$iv = 0;
        int size = userStates.getSize();
        while (index$iv < size) {
            int userId = userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> appIdDevicePermissionFlags = userState.getAppIdDevicePermissionFlags();
            int index$iv2 = appIdDevicePermissionFlags.getSize() - 1;
            while (-1 < index$iv2) {
                int appId = appIdDevicePermissionFlags.keyAt(index$iv2);
                com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$onDeviceIdRemoved.getNewState(), userId, 0, 2, null);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
                com.android.server.permission.access.immutable.MutableIndexedReferenceMap devicePermissionFlags = (com.android.server.permission.access.immutable.MutableIndexedReferenceMap) mutableUserStateMutateUserState$default.mutateAppIdDevicePermissionFlags().mutate(appId);
                if (devicePermissionFlags == null) {
                    intReferenceMap = userStates;
                } else {
                    intReferenceMap = userStates;
                    devicePermissionFlags.remove(deviceId);
                }
                index$iv2--;
                userStates = intReferenceMap;
            }
            index$iv++;
            userStates = userStates;
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStorageVolumeMounted(com.android.server.permission.access.MutateStateScope $this$onStorageVolumeMounted, java.lang.String volumeUuid, java.util.List<java.lang.String> list, boolean isSystemUpdated) {
        int size = list.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = list.get(index$iv);
            com.android.server.pm.pkg.PackageState packageState = $this$onStorageVolumeMounted.getNewState().getExternalState().getPackageStates().get(packageName);
            if (packageState != null) {
                trimPermissionStates($this$onStorageVolumeMounted, packageState.getAppId());
            }
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageAdded(com.android.server.permission.access.MutateStateScope $this$onPackageAdded, com.android.server.pm.pkg.PackageState packageState) {
        trimPermissionStates($this$onPackageAdded, packageState.getAppId());
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageRemoved(com.android.server.permission.access.MutateStateScope $this$onPackageRemoved, java.lang.String packageName, int appId) {
        if ($this$onPackageRemoved.getNewState().getExternalState().getAppIdPackageNames().contains(appId)) {
            trimPermissionStates($this$onPackageRemoved, appId);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageUninstalled(com.android.server.permission.access.MutateStateScope $this$onPackageUninstalled, java.lang.String packageName, int appId, int userId) {
        resetRuntimePermissions($this$onPackageUninstalled, packageName, userId);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resetRuntimePermissions(com.android.server.permission.access.MutateStateScope r34, java.lang.String r35, int r36) {
        /*
            Method dump skipped, instruction units count: 330
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.DevicePermissionPolicy.resetRuntimePermissions(com.android.server.permission.access.MutateStateScope, java.lang.String, int):void");
    }

    private final void trimPermissionStates(com.android.server.permission.access.MutateStateScope $this$trimPermissionStates, int appId) {
        int index$iv;
        com.android.server.permission.access.immutable.IndexedMap $this$forEachReversedIndexed$iv;
        int i;
        int index$iv2;
        com.android.server.permission.access.immutable.IndexedReferenceMap $this$forEachReversedIndexed$iv2;
        int i2 = 1;
        com.android.server.permission.access.immutable.MutableIndexedSet requestedPermissions = new com.android.server.permission.access.immutable.MutableIndexedSet(null, 1, null);
        com.android.server.permission.access.AccessState state$iv = $this$trimPermissionStates.getNewState();
        com.android.server.permission.access.immutable.Immutable immutable = state$iv.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames$iv = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames$iv.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            java.lang.String packageName$iv = (java.lang.String) packageNames$iv.elementAt(index$iv$iv);
            com.android.server.pm.pkg.PackageState packageState = state$iv.getExternalState().getPackageStates().get(packageName$iv);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState$iv = packageState;
            if (packageState$iv.getAndroidPackage() != null) {
                com.android.server.pm.pkg.AndroidPackage androidPackage = packageState$iv.getAndroidPackage();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
                com.android.server.permission.access.immutable.IndexedSetExtensionsKt.plusAssign(requestedPermissions, (java.util.Collection) androidPackage.getRequestedPermissions());
            }
        }
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$trimPermissionStates.getNewState().getUserStates();
        int size2 = userStates.getSize();
        int index$iv3 = 0;
        while (index$iv3 < size2) {
            int userId = userStates.keyAt(index$iv3);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv3);
            com.android.server.permission.access.immutable.IndexedReferenceMap $this$lastIndex$iv$iv = (com.android.server.permission.access.immutable.IndexedReferenceMap) userState.getAppIdDevicePermissionFlags().get(appId);
            if ($this$lastIndex$iv$iv != null) {
                com.android.server.permission.access.immutable.IndexedReferenceMap $this$forEachReversedIndexed$iv3 = $this$lastIndex$iv$iv;
                int index$iv4 = $this$lastIndex$iv$iv.getSize() - i2;
                while (true) {
                    int i3 = -1;
                    if (-1 < index$iv4) {
                        java.lang.Object objKeyAt = $this$forEachReversedIndexed$iv3.keyAt(index$iv4);
                        com.android.server.permission.access.immutable.IndexedMap permissionFlags = (com.android.server.permission.access.immutable.IndexedMap) $this$forEachReversedIndexed$iv3.valueAt(index$iv4);
                        java.lang.String deviceId = (java.lang.String) objKeyAt;
                        com.android.server.permission.access.immutable.IndexedMap $this$forEachReversedIndexed$iv4 = permissionFlags;
                        int index$iv5 = $this$forEachReversedIndexed$iv4.getSize() - 1;
                        while (i3 < index$iv5) {
                            java.lang.Object objKeyAt2 = $this$forEachReversedIndexed$iv4.keyAt(index$iv5);
                            ((java.lang.Number) $this$forEachReversedIndexed$iv4.valueAt(index$iv5)).intValue();
                            java.lang.String permissionName = (java.lang.String) objKeyAt2;
                            if (requestedPermissions.contains(permissionName)) {
                                index$iv = index$iv5;
                                $this$forEachReversedIndexed$iv = $this$forEachReversedIndexed$iv4;
                                i = i3;
                                index$iv2 = index$iv4;
                                $this$forEachReversedIndexed$iv2 = $this$forEachReversedIndexed$iv3;
                            } else {
                                index$iv = index$iv5;
                                $this$forEachReversedIndexed$iv = $this$forEachReversedIndexed$iv4;
                                i = i3;
                                index$iv2 = index$iv4;
                                $this$forEachReversedIndexed$iv2 = $this$forEachReversedIndexed$iv3;
                                setPermissionFlags($this$trimPermissionStates, appId, deviceId, userId, permissionName, 0);
                            }
                            index$iv5 = index$iv - 1;
                            $this$forEachReversedIndexed$iv4 = $this$forEachReversedIndexed$iv;
                            i3 = i;
                            index$iv4 = index$iv2;
                            $this$forEachReversedIndexed$iv3 = $this$forEachReversedIndexed$iv2;
                        }
                        index$iv4--;
                    }
                }
            }
            index$iv3++;
            i2 = 1;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006c A[LOOP:0: B:7:0x002a->B:17:0x006c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0069 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static /* synthetic */ boolean anyPackageInAppId$default(com.android.server.permission.access.permission.DevicePermissionPolicy r18, com.android.server.permission.access.MutateStateScope r19, int r20, com.android.server.permission.access.AccessState r21, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 r22, int r23, java.lang.Object r24) {
        /*
            r0 = r23 & 2
            if (r0 == 0) goto Lb
            com.android.server.permission.access.MutableAccessState r0 = r19.getNewState()
            com.android.server.permission.access.AccessState r0 = (com.android.server.permission.access.AccessState) r0
            goto Ld
        Lb:
            r0 = r21
        Ld:
            r1 = 0
            com.android.server.permission.access.ExternalState r2 = r0.getExternalState()
            com.android.server.permission.access.immutable.IntReferenceMap r2 = r2.getAppIdPackageNames()
            r3 = r20
            com.android.server.permission.access.immutable.Immutable r2 = r2.get(r3)
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            com.android.server.permission.access.immutable.IndexedListSet r2 = (com.android.server.permission.access.immutable.IndexedListSet) r2
            r4 = r2
            r5 = 0
            r6 = r4
            r7 = 0
            r8 = 0
            int r9 = r6.getSize()
        L2a:
            if (r8 >= r9) goto L73
            java.lang.Object r11 = r6.elementAt(r8)
            r12 = r8
            r13 = 0
            r14 = r11
            java.lang.String r14 = (java.lang.String) r14
            r15 = 0
            com.android.server.permission.access.ExternalState r16 = r0.getExternalState()
            java.util.Map r10 = r16.getPackageStates()
            java.lang.Object r10 = r10.get(r14)
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r10)
            com.android.server.pm.pkg.PackageState r10 = (com.android.server.pm.pkg.PackageState) r10
            com.android.server.pm.pkg.AndroidPackage r16 = r10.getAndroidPackage()
            r17 = 1
            if (r16 == 0) goto L62
            r23 = r0
            r0 = r22
            java.lang.Object r16 = r0.invoke(r10)
            java.lang.Boolean r16 = (java.lang.Boolean) r16
            boolean r16 = r16.booleanValue()
            if (r16 == 0) goto L66
            r10 = r17
            goto L67
        L62:
            r23 = r0
            r0 = r22
        L66:
            r10 = 0
        L67:
            if (r10 == 0) goto L6c
            r10 = r17
            goto L79
        L6c:
            int r8 = r8 + 1
            r0 = r23
            goto L2a
        L73:
            r23 = r0
            r0 = r22
            r10 = 0
        L79:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.DevicePermissionPolicy.anyPackageInAppId$default(com.android.server.permission.access.permission.DevicePermissionPolicy, com.android.server.permission.access.MutateStateScope, int, com.android.server.permission.access.AccessState, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1, int, java.lang.Object):boolean");
    }

    private final boolean anyPackageInAppId(com.android.server.permission.access.MutateStateScope $this$anyPackageInAppId, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.pm.pkg.PackageState, java.lang.Boolean> function1) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            java.lang.Object element$iv = packageNames.elementAt(index$iv$iv);
            java.lang.String packageName = (java.lang.String) element$iv;
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null && function1.invoke(packageState2).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ void forEachPackageInAppId$default(com.android.server.permission.access.permission.DevicePermissionPolicy $this, com.android.server.permission.access.MutateStateScope $receiver, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            com.android.server.permission.access.AccessState state2 = $receiver.getNewState();
            state = state2;
        }
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = (java.lang.String) packageNames.elementAt(index$iv);
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                action.invoke(packageState2);
            }
        }
    }

    private final void forEachPackageInAppId(com.android.server.permission.access.MutateStateScope $this$forEachPackageInAppId, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.pm.pkg.PackageState, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = (java.lang.String) packageNames.elementAt(index$iv);
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                function1.invoke(packageState2);
            }
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) throws org.xmlpull.v1.XmlPullParserException {
        com.android.server.permission.access.permission.DevicePermissionPersistence $this$parseUserState_u24lambda_u2417 = this.persistence;
        $this$parseUserState_u24lambda_u2417.parseUserState($this$parseUserState, state, userId);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.permission.DevicePermissionPersistence $this$serializeUserState_u24lambda_u2418 = this.persistence;
        $this$serializeUserState_u24lambda_u2418.serializeUserState($this$serializeUserState, state, userId);
    }

    public final int getPermissionFlags(com.android.server.permission.access.GetStateScope $this$getPermissionFlags, int appId, java.lang.String deviceId, int userId, java.lang.String permissionName) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> appIdDevicePermissionFlags;
        com.android.server.permission.access.immutable.IndexedReferenceMap indexedReferenceMap;
        com.android.server.permission.access.immutable.IndexedMap indexedMap;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) $this$getPermissionFlags.getState().getUserStates().get(userId);
        int iIntValue = 0;
        if (userState != null && (appIdDevicePermissionFlags = userState.getAppIdDevicePermissionFlags()) != null && (indexedReferenceMap = (com.android.server.permission.access.immutable.IndexedReferenceMap) appIdDevicePermissionFlags.get(appId)) != null && (indexedMap = (com.android.server.permission.access.immutable.IndexedMap) indexedReferenceMap.get(deviceId)) != null) {
            iIntValue = ((java.lang.Number) com.android.server.permission.access.immutable.IndexedMapExtensionsKt.getWithDefault(indexedMap, permissionName, 0)).intValue();
        }
        int flags = iIntValue;
        return flags;
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> getAllPermissionFlags(com.android.server.permission.access.GetStateScope $this$getAllPermissionFlags, int appId, java.lang.String persistentDeviceId, int userId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> appIdDevicePermissionFlags;
        com.android.server.permission.access.immutable.IndexedReferenceMap indexedReferenceMap;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) $this$getAllPermissionFlags.getState().getUserStates().get(userId);
        if (userState == null || (appIdDevicePermissionFlags = userState.getAppIdDevicePermissionFlags()) == null || (indexedReferenceMap = (com.android.server.permission.access.immutable.IndexedReferenceMap) appIdDevicePermissionFlags.get(appId)) == null) {
            return null;
        }
        return (com.android.server.permission.access.immutable.IndexedMap) indexedReferenceMap.get(persistentDeviceId);
    }

    public final boolean setPermissionFlags(com.android.server.permission.access.MutateStateScope $this$setPermissionFlags, int appId, java.lang.String deviceId, int userId, java.lang.String permissionName, int flags) {
        return updatePermissionFlags($this$setPermissionFlags, appId, deviceId, userId, permissionName, -1, flags);
    }

    private final boolean updatePermissionFlags(com.android.server.permission.access.MutateStateScope $this$updatePermissionFlags, int appId, java.lang.String deviceId, int userId, java.lang.String permissionName, int flagMask, int flagValues) {
        com.android.server.permission.access.immutable.Immutable immutable;
        if (!$this$updatePermissionFlags.getNewState().getUserStates().contains(userId)) {
            android.util.Slog.e(LOG_TAG, "Unable to update permission flags for missing user " + userId);
            return false;
        }
        com.android.server.permission.access.immutable.Immutable immutable2 = $this$updatePermissionFlags.getNewState().getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable2);
        com.android.server.permission.access.immutable.IndexedReferenceMap indexedReferenceMap = (com.android.server.permission.access.immutable.IndexedReferenceMap) ((com.android.server.permission.access.UserState) immutable2).getAppIdDevicePermissionFlags().get(appId);
        int oldFlags = ((java.lang.Number) com.android.server.permission.access.immutable.IndexedMapExtensionsKt.getWithDefault(indexedReferenceMap != null ? (com.android.server.permission.access.immutable.IndexedMap) indexedReferenceMap.get(deviceId) : null, permissionName, 0)).intValue();
        int newFlags = com.android.server.permission.access.util.IntExtensionsKt.andInv(oldFlags, flagMask) | (flagValues & flagMask);
        if (oldFlags == newFlags) {
            return false;
        }
        com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$updatePermissionFlags.getNewState(), userId, 0, 2, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> mutableIntReferenceMapMutateAppIdDevicePermissionFlags = mutableUserStateMutateUserState$default.mutateAppIdDevicePermissionFlags();
        com.android.server.permission.access.immutable.Immutable immutableMutate = mutableIntReferenceMapMutateAppIdDevicePermissionFlags.mutate(appId);
        if (immutableMutate == null) {
            com.android.server.permission.access.immutable.Immutable mutableIndexedReferenceMap = new com.android.server.permission.access.immutable.MutableIndexedReferenceMap(null, 1, null);
            com.android.server.permission.access.immutable.Immutable it$iv = mutableIndexedReferenceMap;
            mutableIntReferenceMapMutateAppIdDevicePermissionFlags.put(appId, it$iv);
            immutableMutate = mutableIndexedReferenceMap;
        }
        com.android.server.permission.access.immutable.MutableIndexedReferenceMap devicePermissionFlags = (com.android.server.permission.access.immutable.MutableIndexedReferenceMap) immutableMutate;
        com.android.server.permission.access.immutable.Immutable immutableMutate2 = devicePermissionFlags.mutate(deviceId);
        if (immutableMutate2 != null) {
            immutable = immutableMutate2;
        } else {
            com.android.server.permission.access.immutable.Immutable mutableIndexedMap = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
            com.android.server.permission.access.immutable.Immutable it$iv2 = mutableIndexedMap;
            devicePermissionFlags.put(deviceId, it$iv2);
            immutable = mutableIndexedMap;
        }
        com.android.server.permission.access.immutable.MutableIndexedMap permissionFlags = (com.android.server.permission.access.immutable.MutableIndexedMap) immutable;
        com.android.server.permission.access.immutable.IndexedMapExtensionsKt.putWithDefault(permissionFlags, permissionName, java.lang.Integer.valueOf(newFlags), 0);
        if (permissionFlags.isEmpty()) {
            devicePermissionFlags.remove(deviceId);
            if (devicePermissionFlags.isEmpty()) {
                com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign(mutableIntReferenceMapMutateAppIdDevicePermissionFlags, appId);
            }
        }
        com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener> indexedListSet = this.listeners;
        int size = indexedListSet.getSize();
        int index$iv = 0;
        while (index$iv < size) {
            com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener it = indexedListSet.elementAt(index$iv);
            it.onDevicePermissionFlagsChanged(appId, userId, deviceId, permissionName, oldFlags, newFlags);
            index$iv++;
            size = size;
            indexedListSet = indexedListSet;
            permissionFlags = permissionFlags;
        }
        return true;
    }

    public final void addOnPermissionFlagsChangedListener(com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener listener) {
        synchronized (this.listenersLock) {
            this.listeners = com.android.server.permission.access.immutable.IndexedListSetExtensionsKt.plus(this.listeners, listener);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    /* JADX INFO: compiled from: DevicePermissionPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/android/server/permission/access/permission/DevicePermissionPolicy$Companion;", "", "()V", "LOG_TAG", "", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
