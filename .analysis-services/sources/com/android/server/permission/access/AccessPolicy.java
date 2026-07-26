package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessPolicy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000¬\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 M2\u00020\u0001:\u0001MB\u0007\b\u0016¢\u0006\u0002\u0010\u0002B'\b\u0002\u0012\u001e\u0010\u0003\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00040\u0004¢\u0006\u0002\u0010\u0007J\u001d\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\u000bH\u0082\bJ\u0018\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\u0016\u0010\f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0005J\u009e\u0001\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d0\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020!0\u00182\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00050#2\u0006\u0010$\u001a\u00020%2\u0018\u0010&\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050#0\u0004J\u000e\u0010'\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010(\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*JN\u0010+\u001a\u00020\t*\u00020,2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d0\u001c2\u0006\u0010-\u001a\u00020\u0005JV\u0010.\u001a\u00020\t*\u00020,2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d0\u001c2\u0006\u0010-\u001a\u00020\u00052\u0006\u0010)\u001a\u00020*JV\u0010/\u001a\u00020\t*\u00020,2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d0\u001c2\u0006\u0010-\u001a\u00020\u00052\u0006\u00100\u001a\u00020*J^\u00101\u001a\u00020\t*\u00020,2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d0\u001c2\u0006\u0010-\u001a\u00020\u00052\u0006\u00100\u001a\u00020*2\u0006\u0010)\u001a\u00020*J\n\u00102\u001a\u00020\t*\u000203Jf\u00104\u001a\u00020\t*\u00020,2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u00182\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d0\u001c2\b\u00105\u001a\u0004\u0018\u00010\u00052\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u0005072\u0006\u00108\u001a\u00020\u001fJ\n\u00109\u001a\u00020\t*\u00020,J\u0012\u0010:\u001a\u00020\t*\u00020,2\u0006\u0010)\u001a\u00020*J\u0012\u0010;\u001a\u00020\t*\u00020,2\u0006\u0010)\u001a\u00020*J\u001c\u0010<\u001a\u00020\t*\u00020=2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*H\u0002J \u0010>\u001a\u00020\t*\u00020=2\u0012\u0010?\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020*0@H\u0002J\u001c\u0010A\u001a\u00020\t*\u00020=2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*H\u0002J\u0012\u0010B\u001a\u00020\t*\u00020=2\u0006\u0010\u0013\u001a\u00020\u0014J\u001a\u0010C\u001a\u00020\t*\u00020=2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*J\u0016\u0010D\u001a\u00020\t*\u00020E2\b\u0010F\u001a\u0004\u0018\u00010\u0005H\u0002J \u0010G\u001a\u00020\t*\u00020E2\u0012\u0010?\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020*0\u0004H\u0002J\u0012\u0010H\u001a\u00020\t*\u00020E2\u0006\u0010\u0013\u001a\u00020IJ\u001a\u0010J\u001a\u00020\t*\u00020E2\u0006\u0010\u0013\u001a\u00020I2\u0006\u0010)\u001a\u00020*J\u001c\u0010K\u001a\u00020\t*\u00020,2\u0006\u0010L\u001a\u00020\u00192\u0006\u0010)\u001a\u00020*H\u0002R&\u0010\u0003\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006N"}, d2 = {"Lcom/android/server/permission/access/AccessPolicy;", "", "()V", "schemePolicies", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "Lcom/android/server/permission/access/SchemePolicy;", "(Lcom/android/server/permission/access/immutable/IndexedMap;)V", "forEachSchemePolicy", "", "action", "Lkotlin/Function1;", "getSchemePolicy", "subject", "Lcom/android/server/permission/access/AccessUri;", "object", "subjectScheme", "objectScheme", "initialize", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userIds", "Lcom/android/server/permission/access/immutable/IntSet;", "packageStates", "", "Lcom/android/server/pm/pkg/PackageState;", "disabledSystemPackageStates", "knownPackages", "Lcom/android/server/permission/access/immutable/IntMap;", "", "isLeanback", "", "configPermissions", "Lcom/android/server/SystemConfig$PermissionEntry;", "privilegedPermissionAllowlistPackages", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "permissionAllowlist", "Lcom/android/server/pm/permission/PermissionAllowlist;", "implicitToSourcePermissions", "migrateSystemState", "migrateUserState", "userId", "", "onPackageAdded", "Lcom/android/server/permission/access/MutateStateScope;", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "onPackageInstalled", "onPackageRemoved", "appId", "onPackageUninstalled", "onStateMutated", "Lcom/android/server/permission/access/GetStateScope;", "onStorageVolumeMounted", "volumeUuid", com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, "", "isSystemUpdated", "onSystemReady", "onUserAdded", "onUserRemoved", "parseDefaultPermissionGrant", "Lcom/android/modules/utils/BinaryXmlPullParser;", "parsePackageVersion", "packageVersions", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "parsePackageVersions", "parseSystemState", "parseUserState", "serializeDefaultPermissionGrantFingerprint", "Lcom/android/modules/utils/BinaryXmlSerializer;", com.android.server.permission.access.AccessPolicy.ATTR_FINGERPRINT, "serializePackageVersions", "serializeSystemState", "Lcom/android/server/permission/access/AccessState;", "serializeUserState", "upgradePackageVersion", "packageState", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AccessPolicy {
    private static final java.lang.String ATTR_FINGERPRINT = "fingerprint";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_VERSION = "version";
    public static final com.android.server.permission.access.AccessPolicy.Companion Companion = new com.android.server.permission.access.AccessPolicy.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.AccessPolicy.class.getSimpleName();
    private static final java.lang.String TAG_ACCESS = "access";
    private static final java.lang.String TAG_DEFAULT_PERMISSION_GRANT = "default-permission-grant";
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PACKAGE_VERSIONS = "package-versions";
    public static final int VERSION_LATEST = 15;
    private final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> schemePolicies;

    private AccessPolicy(com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap) {
        this.schemePolicies = indexedMap;
    }

    public AccessPolicy() {
        com.android.server.permission.access.immutable.MutableIndexedMap $this$_init__u24lambda_u241 = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
        _init_$lambda$1$addPolicy($this$_init__u24lambda_u241, new com.android.server.permission.access.permission.AppIdPermissionPolicy());
        _init_$lambda$1$addPolicy($this$_init__u24lambda_u241, new com.android.server.permission.access.permission.DevicePermissionPolicy());
        _init_$lambda$1$addPolicy($this$_init__u24lambda_u241, new com.android.server.permission.access.appop.AppIdAppOpPolicy());
        _init_$lambda$1$addPolicy($this$_init__u24lambda_u241, new com.android.server.permission.access.appop.PackageAppOpPolicy());
        this($this$_init__u24lambda_u241);
    }

    private static final void _init_$lambda$1$addPolicy(com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> mutableIndexedMap, com.android.server.permission.access.SchemePolicy policy) {
        java.lang.String subjectScheme = policy.getSubjectScheme();
        com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> mutableIndexedMap2 = mutableIndexedMap.get(subjectScheme);
        if (mutableIndexedMap2 == null) {
            mutableIndexedMap2 = new com.android.server.permission.access.immutable.MutableIndexedMap<>(null, 1, null);
            mutableIndexedMap.put(subjectScheme, mutableIndexedMap2);
        }
        mutableIndexedMap2.put(policy.getObjectScheme(), policy);
    }

    public final com.android.server.permission.access.SchemePolicy getSchemePolicy(java.lang.String subjectScheme, java.lang.String objectScheme) {
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMap = this.schemePolicies.get(subjectScheme);
        com.android.server.permission.access.SchemePolicy schemePolicy = indexedMap != null ? indexedMap.get(objectScheme) : null;
        if (schemePolicy == null) {
            throw new java.lang.IllegalStateException(("Scheme policy for " + subjectScheme + " and " + objectScheme + " does not exist").toString());
        }
        return schemePolicy;
    }

    public final void initialize(com.android.server.permission.access.MutableAccessState state, com.android.server.permission.access.immutable.IntSet userIds, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, boolean isLeanback, java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> map3, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet, com.android.server.pm.permission.PermissionAllowlist permissionAllowlist, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> indexedMap) {
        int i;
        com.android.server.permission.access.MutableExternalState $this$initialize_u24lambda_u245 = state.mutateExternalState();
        int i2 = 0;
        com.android.server.permission.access.immutable.IntSetExtensionsKt.plusAssign($this$initialize_u24lambda_u245.mutateUserIds(), userIds);
        $this$initialize_u24lambda_u245.setPackageStatesPublic(map);
        $this$initialize_u24lambda_u245.setDisabledSystemPackageStatesPublic(map2);
        java.util.Iterator<java.util.Map.Entry<java.lang.String, ? extends com.android.server.pm.pkg.PackageState>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            com.android.server.pm.pkg.PackageState packageState = it.next().getValue();
            if (packageState.isApex()) {
                i = i2;
            } else {
                com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> mutableIntReferenceMapMutateAppIdPackageNames = $this$initialize_u24lambda_u245.mutateAppIdPackageNames();
                int key$iv = packageState.getAppId();
                com.android.server.permission.access.immutable.Immutable immutableMutate = mutableIntReferenceMapMutateAppIdPackageNames.mutate(key$iv);
                if (immutableMutate == null) {
                    i = i2;
                    com.android.server.permission.access.immutable.Immutable mutableIndexedListSet = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
                    com.android.server.permission.access.immutable.Immutable it$iv = mutableIndexedListSet;
                    mutableIntReferenceMapMutateAppIdPackageNames.put(key$iv, it$iv);
                    immutableMutate = mutableIndexedListSet;
                } else {
                    i = i2;
                }
                ((com.android.server.permission.access.immutable.MutableIndexedListSet) immutableMutate).add(packageState.getPackageName());
            }
            i2 = i;
        }
        $this$initialize_u24lambda_u245.setKnownPackagesPublic(intMap);
        $this$initialize_u24lambda_u245.setLeanbackPublic(isLeanback);
        $this$initialize_u24lambda_u245.setConfigPermissionsPublic(map3);
        $this$initialize_u24lambda_u245.setPrivilegedPermissionAllowlistPackagesPublic(indexedListSet);
        $this$initialize_u24lambda_u245.setPermissionAllowlistPublic(permissionAllowlist);
        $this$initialize_u24lambda_u245.setImplicitToSourcePermissionsPublic(indexedMap);
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> mutableIntReferenceMapMutateUserStatesNoWrite = state.mutateUserStatesNoWrite();
        int size = userIds.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int userId = userIds.elementAt(index$iv);
            com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.set(mutableIntReferenceMapMutateUserStatesNoWrite, userId, new com.android.server.permission.access.MutableUserState());
        }
    }

    public final void onStateMutated(com.android.server.permission.access.GetStateScope $this$onStateMutated) {
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onStateMutated($this$onStateMutated);
                index$iv$iv2++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
            }
            index$iv$iv++;
            this_$iv = this_$iv;
        }
    }

    public final void onUserAdded(com.android.server.permission.access.MutateStateScope $this$onUserAdded, int userId) {
        com.android.server.permission.access.immutable.IntSetExtensionsKt.plusAssign($this$onUserAdded.getNewState().mutateExternalState().mutateUserIds(), userId);
        com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.set($this$onUserAdded.getNewState().mutateUserStatesNoWrite(), userId, new com.android.server.permission.access.MutableUserState());
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int size = indexedMap.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onUserAdded($this$onUserAdded, userId);
                index$iv$iv2++;
                this_$iv = this_$iv;
            }
        }
        java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState>> it = $this$onUserAdded.getNewState().getExternalState().getPackageStates().entrySet().iterator();
        while (it.hasNext()) {
            com.android.server.pm.pkg.PackageState packageState = it.next().getValue();
            if (!packageState.isApex()) {
                upgradePackageVersion($this$onUserAdded, packageState, userId);
            }
        }
    }

    public final void onUserRemoved(com.android.server.permission.access.MutateStateScope $this$onUserRemoved, int userId) {
        com.android.server.permission.access.immutable.IntSetExtensionsKt.minusAssign($this$onUserRemoved.getNewState().mutateExternalState().mutateUserIds(), userId);
        com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign($this$onUserRemoved.getNewState().mutateUserStatesNoWrite(), userId);
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onUserRemoved($this$onUserRemoved, userId);
                index$iv$iv2++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
            }
            index$iv$iv++;
            this_$iv = this_$iv;
        }
    }

    public final void onStorageVolumeMounted(com.android.server.permission.access.MutateStateScope $this$onStorageVolumeMounted, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, java.lang.String volumeUuid, java.util.List<java.lang.String> list, boolean isSystemUpdated) {
        boolean z;
        int i;
        java.lang.String str = volumeUuid;
        boolean z2 = true;
        com.android.server.permission.access.immutable.MutableIntSet addedAppIds = new com.android.server.permission.access.immutable.MutableIntSet(null, 1, null);
        com.android.server.permission.access.MutableExternalState $this$onStorageVolumeMounted_u24lambda_u2418 = $this$onStorageVolumeMounted.getNewState().mutateExternalState();
        int i2 = 0;
        $this$onStorageVolumeMounted_u24lambda_u2418.setPackageStatesPublic(map);
        $this$onStorageVolumeMounted_u24lambda_u2418.setDisabledSystemPackageStatesPublic(map2);
        for (java.util.Map.Entry<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> entry : map.entrySet()) {
            java.lang.String packageName = entry.getKey();
            com.android.server.pm.pkg.PackageState packageState = entry.getValue();
            if (packageState.isApex()) {
                z = z2;
                i = i2;
            } else if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(packageState.getVolumeUuid(), str)) {
                if (!((packageState.getAndroidPackage() == null || list.contains(packageName)) ? z2 : false)) {
                    throw new java.lang.IllegalStateException(("Package " + packageName + " on storage volume " + str + " didn't receive onPackageAdded() before onStorageVolumeMounted()").toString());
                }
                int appId = packageState.getAppId();
                com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> mutableIntReferenceMapMutateAppIdPackageNames = $this$onStorageVolumeMounted_u24lambda_u2418.mutateAppIdPackageNames();
                com.android.server.permission.access.immutable.Immutable immutableMutate = mutableIntReferenceMapMutateAppIdPackageNames.mutate(appId);
                if (immutableMutate != null) {
                    i = i2;
                    z = true;
                } else {
                    com.android.server.permission.access.immutable.IntSetExtensionsKt.plusAssign(addedAppIds, appId);
                    i = i2;
                    z = true;
                    com.android.server.permission.access.immutable.Immutable mutableIndexedListSet = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
                    com.android.server.permission.access.immutable.Immutable it$iv = mutableIndexedListSet;
                    mutableIntReferenceMapMutateAppIdPackageNames.put(appId, it$iv);
                    immutableMutate = mutableIndexedListSet;
                }
                com.android.server.permission.access.immutable.MutableIndexedListSet $this$plusAssign$iv = (com.android.server.permission.access.immutable.MutableIndexedListSet) immutableMutate;
                $this$plusAssign$iv.add(packageName);
            } else {
                z = z2;
                i = i2;
            }
            z2 = z;
            i2 = i;
        }
        $this$onStorageVolumeMounted_u24lambda_u2418.setKnownPackagesPublic(intMap);
        com.android.server.permission.access.immutable.MutableIntSet $this$forEachIndexed$iv = addedAppIds;
        int index$iv = 0;
        int size = $this$forEachIndexed$iv.getSize();
        while (index$iv < size) {
            int appId2 = $this$forEachIndexed$iv.elementAt(index$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this.schemePolicies;
            int index$iv$iv = 0;
            com.android.server.permission.access.immutable.MutableIntSet addedAppIds2 = addedAppIds;
            int size2 = indexedMap.getSize();
            while (index$iv$iv < size2) {
                indexedMap.keyAt(index$iv$iv);
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
                int i3 = size2;
                int size3 = indexedMapValueAt.getSize();
                int index$iv$iv2 = 0;
                while (index$iv$iv2 < size3) {
                    int i4 = size3;
                    com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMap2 = indexedMapValueAt;
                    indexedMap2.keyAt(index$iv$iv2);
                    com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMap2.valueAt(index$iv$iv2);
                    schemePolicy$iv.onAppIdAdded($this$onStorageVolumeMounted, appId2);
                    index$iv$iv2++;
                    size3 = i4;
                    indexedMapValueAt = indexedMap2;
                }
                index$iv$iv++;
                size2 = i3;
            }
            index$iv++;
            addedAppIds = addedAppIds2;
        }
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap3 = this_$iv.schemePolicies;
        int index$iv$iv3 = 0;
        int size4 = indexedMap3.getSize();
        while (index$iv$iv3 < size4) {
            indexedMap3.keyAt(index$iv$iv3);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt2 = indexedMap3.valueAt(index$iv$iv3);
            int index$iv$iv4 = 0;
            int size5 = indexedMapValueAt2.getSize();
            while (index$iv$iv4 < size5) {
                indexedMapValueAt2.keyAt(index$iv$iv4);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv2 = indexedMapValueAt2.valueAt(index$iv$iv4);
                schemePolicy$iv2.onStorageVolumeMounted($this$onStorageVolumeMounted, str, list, isSystemUpdated);
                index$iv$iv4++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
            }
            index$iv$iv3++;
            this_$iv = this_$iv;
        }
        java.util.Iterator<java.util.Map.Entry<java.lang.String, ? extends com.android.server.pm.pkg.PackageState>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            com.android.server.pm.pkg.PackageState packageState2 = it.next().getValue();
            if (!packageState2.isApex() && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(packageState2.getVolumeUuid(), str)) {
                com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onStorageVolumeMounted.getNewState().getUserStates();
                int size6 = userStates.getSize();
                for (int index$iv2 = 0; index$iv2 < size6; index$iv2++) {
                    int userId = userStates.keyAt(index$iv2);
                    upgradePackageVersion($this$onStorageVolumeMounted, packageState2, userId);
                }
            }
            str = volumeUuid;
        }
    }

    public final void onPackageAdded(com.android.server.permission.access.MutateStateScope $this$onPackageAdded, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, java.lang.String packageName) {
        com.android.server.pm.pkg.PackageState packageState = map.get(packageName);
        if (packageState == null) {
            throw new java.lang.IllegalStateException(("Added package " + packageName + " isn't found in packageStates in onPackageAdded()").toString());
        }
        int appId = packageState.getAppId();
        boolean isAppIdAdded = false;
        com.android.server.permission.access.MutableExternalState $this$onPackageAdded_u24lambda_u2428 = $this$onPackageAdded.getNewState().mutateExternalState();
        $this$onPackageAdded_u24lambda_u2428.setPackageStatesPublic(map);
        $this$onPackageAdded_u24lambda_u2428.setDisabledSystemPackageStatesPublic(map2);
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> mutableIntReferenceMapMutateAppIdPackageNames = $this$onPackageAdded_u24lambda_u2428.mutateAppIdPackageNames();
        com.android.server.permission.access.immutable.Immutable immutableMutate = mutableIntReferenceMapMutateAppIdPackageNames.mutate(appId);
        if (immutableMutate == null) {
            isAppIdAdded = true;
            com.android.server.permission.access.immutable.Immutable mutableIndexedListSet = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
            com.android.server.permission.access.immutable.Immutable it$iv = mutableIndexedListSet;
            mutableIntReferenceMapMutateAppIdPackageNames.put(appId, it$iv);
            immutableMutate = mutableIndexedListSet;
        }
        com.android.server.permission.access.immutable.MutableIndexedListSet $this$plusAssign$iv = (com.android.server.permission.access.immutable.MutableIndexedListSet) immutableMutate;
        $this$plusAssign$iv.add(packageName);
        $this$onPackageAdded_u24lambda_u2428.setKnownPackagesPublic(intMap);
        if (isAppIdAdded) {
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this.schemePolicies;
            int index$iv$iv = 0;
            int size = indexedMap.getSize();
            while (index$iv$iv < size) {
                indexedMap.keyAt(index$iv$iv);
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
                int size2 = indexedMapValueAt.getSize();
                boolean isAppIdAdded2 = isAppIdAdded;
                int index$iv$iv2 = 0;
                while (index$iv$iv2 < size2) {
                    int i = size2;
                    com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMap2 = indexedMapValueAt;
                    indexedMap2.keyAt(index$iv$iv2);
                    com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMap2.valueAt(index$iv$iv2);
                    schemePolicy$iv.onAppIdAdded($this$onPackageAdded, appId);
                    index$iv$iv2++;
                    size2 = i;
                    indexedMapValueAt = indexedMap2;
                }
                index$iv$iv++;
                isAppIdAdded = isAppIdAdded2;
            }
        }
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap3 = this_$iv.schemePolicies;
        int index$iv$iv3 = 0;
        int size3 = indexedMap3.getSize();
        while (index$iv$iv3 < size3) {
            indexedMap3.keyAt(index$iv$iv3);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt2 = indexedMap3.valueAt(index$iv$iv3);
            com.android.server.permission.access.AccessPolicy this_$iv2 = this_$iv;
            int appId2 = appId;
            int appId3 = 0;
            for (int size4 = indexedMapValueAt2.getSize(); appId3 < size4; size4 = size4) {
                indexedMapValueAt2.keyAt(appId3);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv2 = indexedMapValueAt2.valueAt(appId3);
                schemePolicy$iv2.onPackageAdded($this$onPackageAdded, packageState);
                appId3++;
            }
            index$iv$iv3++;
            this_$iv = this_$iv2;
            appId = appId2;
        }
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onPackageAdded.getNewState().getUserStates();
        int size5 = userStates.getSize();
        for (int index$iv = 0; index$iv < size5; index$iv++) {
            int userId = userStates.keyAt(index$iv);
            upgradePackageVersion($this$onPackageAdded, packageState, userId);
        }
    }

    public final void onPackageRemoved(com.android.server.permission.access.MutateStateScope $this$onPackageRemoved, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, java.lang.String packageName, int appId) {
        if (!(!map.containsKey(packageName))) {
            throw new java.lang.IllegalStateException(("Removed package " + packageName + " is still in packageStates in onPackageRemoved()").toString());
        }
        boolean isAppIdRemoved = false;
        com.android.server.permission.access.MutableExternalState $this$onPackageRemoved_u24lambda_u2436 = $this$onPackageRemoved.getNewState().mutateExternalState();
        $this$onPackageRemoved_u24lambda_u2436.setPackageStatesPublic(map);
        $this$onPackageRemoved_u24lambda_u2436.setDisabledSystemPackageStatesPublic(map2);
        com.android.server.permission.access.immutable.MutableIndexedListSet $this$onPackageRemoved_u24lambda_u2436_u24lambda_u2435 = (com.android.server.permission.access.immutable.MutableIndexedListSet) $this$onPackageRemoved_u24lambda_u2436.mutateAppIdPackageNames().mutate(appId);
        if ($this$onPackageRemoved_u24lambda_u2436_u24lambda_u2435 != null) {
            $this$onPackageRemoved_u24lambda_u2436_u24lambda_u2435.remove(packageName);
            if ($this$onPackageRemoved_u24lambda_u2436_u24lambda_u2435.isEmpty()) {
                com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign($this$onPackageRemoved_u24lambda_u2436.mutateAppIdPackageNames(), appId);
                isAppIdRemoved = true;
            }
        }
        $this$onPackageRemoved_u24lambda_u2436.setKnownPackagesPublic(intMap);
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            com.android.server.permission.access.AccessPolicy this_$iv2 = this_$iv;
            int index$iv$iv2 = 0;
            for (int size2 = indexedMapValueAt.getSize(); index$iv$iv2 < size2; size2 = size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onPackageRemoved($this$onPackageRemoved, packageName, appId);
                index$iv$iv2++;
            }
            index$iv$iv++;
            this_$iv = this_$iv2;
        }
        if (isAppIdRemoved) {
            com.android.server.permission.access.AccessPolicy this_$iv3 = this;
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap2 = this_$iv3.schemePolicies;
            int index$iv$iv3 = 0;
            int size3 = indexedMap2.getSize();
            while (index$iv$iv3 < size3) {
                indexedMap2.keyAt(index$iv$iv3);
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt2 = indexedMap2.valueAt(index$iv$iv3);
                com.android.server.permission.access.AccessPolicy this_$iv4 = this_$iv3;
                boolean isAppIdRemoved2 = isAppIdRemoved;
                int index$iv$iv4 = 0;
                for (int size4 = indexedMapValueAt2.getSize(); index$iv$iv4 < size4; size4 = size4) {
                    indexedMapValueAt2.keyAt(index$iv$iv4);
                    com.android.server.permission.access.SchemePolicy schemePolicy$iv2 = indexedMapValueAt2.valueAt(index$iv$iv4);
                    schemePolicy$iv2.onAppIdRemoved($this$onPackageRemoved, appId);
                    index$iv$iv4++;
                }
                index$iv$iv3++;
                this_$iv3 = this_$iv4;
                isAppIdRemoved = isAppIdRemoved2;
            }
        }
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onPackageRemoved.getNewState().getUserStates();
        int size5 = userStates.getSize();
        for (int index$iv = 0; index$iv < size5; index$iv++) {
            userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            int userStateIndex = index$iv;
            if (userState.getPackageVersions().contains(packageName)) {
                com.android.server.permission.access.MutableAccessState.mutateUserStateAt$default($this$onPackageRemoved.getNewState(), userStateIndex, 0, 2, null).mutatePackageVersions().remove(packageName);
            }
        }
    }

    public final void onPackageInstalled(com.android.server.permission.access.MutateStateScope $this$onPackageInstalled, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, java.lang.String packageName, int userId) {
        com.android.server.permission.access.MutableExternalState $this$onPackageInstalled_u24lambda_u2442 = $this$onPackageInstalled.getNewState().mutateExternalState();
        $this$onPackageInstalled_u24lambda_u2442.setPackageStatesPublic(map);
        $this$onPackageInstalled_u24lambda_u2442.setDisabledSystemPackageStatesPublic(map2);
        $this$onPackageInstalled_u24lambda_u2442.setKnownPackagesPublic(intMap);
        com.android.server.pm.pkg.PackageState packageState = map.get(packageName);
        if (packageState == null) {
            throw new java.lang.IllegalStateException(("Installed package " + packageName + " isn't found in packageStates in onPackageInstalled()").toString());
        }
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            for (int size2 = indexedMapValueAt.getSize(); index$iv$iv2 < size2; size2 = size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onPackageInstalled($this$onPackageInstalled, packageState, userId);
                index$iv$iv2++;
                this_$iv = this_$iv;
            }
            index$iv$iv++;
            this_$iv = this_$iv;
        }
    }

    public final void onPackageUninstalled(com.android.server.permission.access.MutateStateScope $this$onPackageUninstalled, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, java.lang.String packageName, int appId, int userId) {
        com.android.server.permission.access.MutableExternalState $this$onPackageUninstalled_u24lambda_u2446 = $this$onPackageUninstalled.getNewState().mutateExternalState();
        $this$onPackageUninstalled_u24lambda_u2446.setPackageStatesPublic(map);
        $this$onPackageUninstalled_u24lambda_u2446.setDisabledSystemPackageStatesPublic(map2);
        $this$onPackageUninstalled_u24lambda_u2446.setKnownPackagesPublic(intMap);
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onPackageUninstalled($this$onPackageUninstalled, packageName, appId, userId);
                index$iv$iv2++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
            }
            index$iv$iv++;
            $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
            this_$iv = this_$iv;
        }
    }

    public final void onSystemReady(com.android.server.permission.access.MutateStateScope $this$onSystemReady) {
        $this$onSystemReady.getNewState().mutateExternalState().setSystemReadyPublic(true);
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.onSystemReady($this$onSystemReady);
                index$iv$iv2++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
            }
            index$iv$iv++;
            this_$iv = this_$iv;
        }
    }

    public final void migrateSystemState(com.android.server.permission.access.MutableAccessState state) {
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.migrateSystemState(state);
                index$iv$iv2++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
            }
            index$iv$iv++;
            this_$iv = this_$iv;
        }
    }

    public final void migrateUserState(com.android.server.permission.access.MutableAccessState state, int userId) {
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        int $i$f$forEachSchemePolicy = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int index$iv$iv2 = 0;
            int size2 = indexedMapValueAt.getSize();
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.migrateUserState(state, userId);
                index$iv$iv2++;
                $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
                this_$iv = this_$iv;
                indexedMap = indexedMap;
            }
            index$iv$iv++;
            $i$f$forEachSchemePolicy = $i$f$forEachSchemePolicy;
            this_$iv = this_$iv;
        }
    }

    private final void upgradePackageVersion(com.android.server.permission.access.MutateStateScope $this$upgradePackageVersion, com.android.server.pm.pkg.PackageState packageState, int userId) {
        if (packageState.getAndroidPackage() == null) {
            return;
        }
        java.lang.String packageName = packageState.getPackageName();
        com.android.server.permission.access.immutable.Immutable immutable = $this$upgradePackageVersion.getNewState().getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        java.lang.Integer version = ((com.android.server.permission.access.UserState) immutable).getPackageVersions().get(packageName);
        if (version == null) {
            com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$upgradePackageVersion.getNewState(), userId, 0, 2, null);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
            mutableUserStateMutateUserState$default.mutatePackageVersions().put(packageName, 15);
            return;
        }
        if (version.intValue() >= 15) {
            if (version.intValue() != 15) {
                android.util.Slog.w(LOG_TAG, "Unexpected version " + version + " for package " + packageName + ",latest version is 15");
                return;
            }
            return;
        }
        com.android.server.permission.access.AccessPolicy this_$iv = this;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this_$iv.schemePolicies;
        int size = indexedMap.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int size2 = indexedMapValueAt.getSize();
            int index$iv$iv2 = 0;
            while (index$iv$iv2 < size2) {
                indexedMapValueAt.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt(index$iv$iv2);
                schemePolicy$iv.upgradePackageState($this$upgradePackageVersion, packageState, userId, version.intValue());
                index$iv$iv2++;
                size2 = size2;
                this_$iv = this_$iv;
            }
        }
        com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default2 = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$upgradePackageVersion.getNewState(), userId, 0, 2, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default2);
        mutableUserStateMutateUserState$default2.mutatePackageVersions().put(packageName, 15);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void parseSystemState(com.android.modules.utils.BinaryXmlPullParser r36, com.android.server.permission.access.MutableAccessState r37) {
        /*
            Method dump skipped, instruction units count: 764
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.AccessPolicy.parseSystemState(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState):void");
    }

    public final void serializeSystemState(com.android.modules.utils.BinaryXmlSerializer $this$serializeSystemState, com.android.server.permission.access.AccessState state) {
        int $i$f$tag = 0;
        $this$serializeSystemState.startTag((java.lang.String) null, TAG_ACCESS);
        int i = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int $i$f$tag2 = $i$f$tag;
            int $i$f$tag3 = 0;
            for (int size2 = indexedMapValueAt.getSize(); $i$f$tag3 < size2; size2 = size2) {
                indexedMapValueAt.keyAt($i$f$tag3);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMapValueAt.valueAt($i$f$tag3);
                schemePolicy$iv.serializeSystemState($this$serializeSystemState, state);
                $i$f$tag3++;
                i = i;
            }
            index$iv$iv++;
            $i$f$tag = $i$f$tag2;
        }
        $this$serializeSystemState.endTag((java.lang.String) null, TAG_ACCESS);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void parseUserState(com.android.modules.utils.BinaryXmlPullParser r37, com.android.server.permission.access.MutableAccessState r38, int r39) {
        /*
            Method dump skipped, instruction units count: 804
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.AccessPolicy.parseUserState(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, int):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private final void parsePackageVersions(com.android.modules.utils.BinaryXmlPullParser r17, com.android.server.permission.access.MutableAccessState r18, int r19) {
        /*
            Method dump skipped, instruction units count: 446
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.AccessPolicy.parsePackageVersions(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, int):void");
    }

    private final void parsePackageVersion(com.android.modules.utils.BinaryXmlPullParser $this$parsePackageVersion, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMap) {
        java.lang.String name$iv = $this$parsePackageVersion.getAttributeValue($this$parsePackageVersion.getAttributeIndexOrThrow((java.lang.String) null, "name"));
        java.lang.String packageName = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(packageName, "intern(...)");
        int version = $this$parsePackageVersion.getAttributeInt((java.lang.String) null, ATTR_VERSION);
        mutableIndexedMap.put(packageName, java.lang.Integer.valueOf(version));
    }

    private final void parseDefaultPermissionGrant(com.android.modules.utils.BinaryXmlPullParser $this$parseDefaultPermissionGrant, com.android.server.permission.access.MutableAccessState state, int userId) {
        com.android.server.permission.access.MutableUserState userState = state.mutateUserState(userId, 0);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(userState);
        java.lang.String name$iv = $this$parseDefaultPermissionGrant.getAttributeValue($this$parseDefaultPermissionGrant.getAttributeIndexOrThrow((java.lang.String) null, ATTR_FINGERPRINT));
        java.lang.String fingerprint = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fingerprint, "intern(...)");
        userState.setDefaultPermissionGrantFingerprintPublic(fingerprint);
    }

    public final void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        int $i$f$tag = 0;
        $this$serializeUserState.startTag((java.lang.String) null, TAG_ACCESS);
        com.android.server.permission.access.immutable.Immutable immutable = state.getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        serializePackageVersions($this$serializeUserState, ((com.android.server.permission.access.UserState) immutable).getPackageVersions());
        com.android.server.permission.access.immutable.Immutable immutable2 = state.getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable2);
        serializeDefaultPermissionGrantFingerprint($this$serializeUserState, ((com.android.server.permission.access.UserState) immutable2).getDefaultPermissionGrantFingerprint());
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this.schemePolicies;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            indexedMap.keyAt(index$iv$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv$iv);
            int size2 = indexedMapValueAt.getSize();
            int index$iv$iv2 = 0;
            while (index$iv$iv2 < size2) {
                int $i$f$tag2 = $i$f$tag;
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMap2 = indexedMapValueAt;
                indexedMap2.keyAt(index$iv$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy$iv = indexedMap2.valueAt(index$iv$iv2);
                schemePolicy$iv.serializeUserState($this$serializeUserState, state, userId);
                index$iv$iv2++;
                $i$f$tag = $i$f$tag2;
                size2 = size2;
                indexedMapValueAt = indexedMap2;
            }
            index$iv$iv++;
            $i$f$tag = $i$f$tag;
        }
        $this$serializeUserState.endTag((java.lang.String) null, TAG_ACCESS);
    }

    private final void serializePackageVersions(com.android.modules.utils.BinaryXmlSerializer $this$serializePackageVersions, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap) {
        int $i$f$tag = 0;
        java.lang.String str = null;
        $this$serializePackageVersions.startTag((java.lang.String) null, TAG_PACKAGE_VERSIONS);
        com.android.modules.utils.BinaryXmlSerializer $this$serializePackageVersions_u24lambda_u2475 = $this$serializePackageVersions;
        int index$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv < size) {
            java.lang.String strKeyAt = indexedMap.keyAt(index$iv);
            int version = indexedMap.valueAt(index$iv).intValue();
            java.lang.String packageName = strKeyAt;
            com.android.modules.utils.BinaryXmlSerializer $this$tag$iv = $this$serializePackageVersions_u24lambda_u2475;
            $this$tag$iv.startTag(str, "package");
            com.android.modules.utils.BinaryXmlSerializer $this$attributeInterned$iv = $this$serializePackageVersions_u24lambda_u2475;
            $this$tag$iv.attributeInterned((java.lang.String) null, "name", packageName);
            $this$tag$iv.attributeInt((java.lang.String) null, ATTR_VERSION, version);
            $this$tag$iv.endTag((java.lang.String) null, "package");
            index$iv++;
            $this$serializePackageVersions_u24lambda_u2475 = $this$attributeInterned$iv;
            $i$f$tag = $i$f$tag;
            str = null;
        }
        $this$serializePackageVersions.endTag((java.lang.String) null, TAG_PACKAGE_VERSIONS);
    }

    private final void serializeDefaultPermissionGrantFingerprint(com.android.modules.utils.BinaryXmlSerializer $this$serializeDefaultPermissionGrantFingerprint, java.lang.String fingerprint) {
        if (fingerprint != null) {
            $this$serializeDefaultPermissionGrantFingerprint.startTag((java.lang.String) null, TAG_DEFAULT_PERMISSION_GRANT);
            $this$serializeDefaultPermissionGrantFingerprint.attributeInterned((java.lang.String) null, ATTR_FINGERPRINT, fingerprint);
            $this$serializeDefaultPermissionGrantFingerprint.endTag((java.lang.String) null, TAG_DEFAULT_PERMISSION_GRANT);
        }
    }

    private final com.android.server.permission.access.SchemePolicy getSchemePolicy(com.android.server.permission.access.AccessUri subject, com.android.server.permission.access.AccessUri object) {
        return getSchemePolicy(subject.getScheme(), object.getScheme());
    }

    private final void forEachSchemePolicy(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.access.SchemePolicy, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy>> indexedMap = this.schemePolicies;
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            indexedMap.keyAt(index$iv);
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.SchemePolicy> indexedMapValueAt = indexedMap.valueAt(index$iv);
            int size2 = indexedMapValueAt.getSize();
            for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
                indexedMapValueAt.keyAt(index$iv2);
                com.android.server.permission.access.SchemePolicy schemePolicy = indexedMapValueAt.valueAt(index$iv2);
                function1.invoke(schemePolicy);
            }
        }
    }

    /* JADX INFO: compiled from: AccessPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0080T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/android/server/permission/access/AccessPolicy$Companion;", "", "()V", "ATTR_FINGERPRINT", "", "ATTR_NAME", "ATTR_VERSION", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "TAG_ACCESS", "TAG_DEFAULT_PERMISSION_GRANT", "TAG_PACKAGE", "TAG_PACKAGE_VERSIONS", "VERSION_LATEST", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
