package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0012\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0001¢\u0006\u0002\u0010\u0004Bÿ\u0001\b\u0002\u0012\u0016\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006j\u0002`\t\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u0012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u0012N\u0010\u000f\u001aJ\u0012 \u0012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00120\u0010j\u0002`\u0013\u0012 \u0012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00120\u0014j\u0002`\u00150\u0006j\u0002`\u0016\u0012\u0012\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00190\u0018\u0012\u0006\u0010\u001a\u001a\u00020\u001b\u0012\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u001d0\u000b\u0012\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\f0\u0011\u0012\u0006\u0010\u001f\u001a\u00020 \u0012\u0018\u0010!\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00110\"\u0012\u0006\u0010#\u001a\u00020\u001b¢\u0006\u0002\u0010$J\"\u0010%\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00120\u0014j\u0002`\u0015J\u0006\u0010&\u001a\u00020\bJ!\u0010'\u001a\u00020(2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u001d0\u000bH\u0007¢\u0006\u0002\b)J!\u0010*\u001a\u00020(2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bH\u0007¢\u0006\u0002\b+J'\u0010,\u001a\u00020(2\u0018\u0010!\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00110\"H\u0007¢\u0006\u0002\b-J!\u0010.\u001a\u00020(2\u0012\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00190\u0018H\u0007¢\u0006\u0002\b/J\u0015\u00100\u001a\u00020(2\u0006\u0010\u001a\u001a\u00020\u001bH\u0007¢\u0006\u0002\b1J!\u00102\u001a\u00020(2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bH\u0007¢\u0006\u0002\b3J\u0015\u00104\u001a\u00020(2\u0006\u0010\u001f\u001a\u00020 H\u0007¢\u0006\u0002\b5J\u001b\u00106\u001a\u00020(2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\f0\u0011H\u0007¢\u0006\u0002\b7J\u0015\u00108\u001a\u00020(2\u0006\u0010#\u001a\u00020\u001bH\u0007¢\u0006\u0002\b9¨\u0006:"}, d2 = {"Lcom/android/server/permission/access/MutableExternalState;", "Lcom/android/server/permission/access/ExternalState;", "()V", "externalState", "(Lcom/android/server/permission/access/ExternalState;)V", "userIdsReference", "Lcom/android/server/permission/access/immutable/MutableReference;", "Lcom/android/server/permission/access/immutable/IntSet;", "Lcom/android/server/permission/access/immutable/MutableIntSet;", "Lcom/android/server/permission/access/UserIdsReference;", "packageStates", "", "", "Lcom/android/server/pm/pkg/PackageState;", "disabledSystemPackageStates", "appIdPackageNamesReference", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "Lcom/android/server/permission/access/immutable/MutableIndexedListSet;", "Lcom/android/server/permission/access/AppIdPackageNames;", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/MutableAppIdPackageNames;", "Lcom/android/server/permission/access/AppIdPackageNamesReference;", "knownPackages", "Lcom/android/server/permission/access/immutable/IntMap;", "", "isLeanback", "", "configPermissions", "Lcom/android/server/SystemConfig$PermissionEntry;", "privilegedPermissionAllowlistPackages", "permissionAllowlist", "Lcom/android/server/pm/permission/PermissionAllowlist;", "implicitToSourcePermissions", "Lcom/android/server/permission/access/immutable/IndexedMap;", "isSystemReady", "(Lcom/android/server/permission/access/immutable/MutableReference;Ljava/util/Map;Ljava/util/Map;Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/IntMap;ZLjava/util/Map;Lcom/android/server/permission/access/immutable/IndexedListSet;Lcom/android/server/pm/permission/PermissionAllowlist;Lcom/android/server/permission/access/immutable/IndexedMap;Z)V", "mutateAppIdPackageNames", "mutateUserIds", "setConfigPermissions", "", "setConfigPermissionsPublic", "setDisabledSystemPackageStates", "setDisabledSystemPackageStatesPublic", "setImplicitToSourcePermissions", "setImplicitToSourcePermissionsPublic", "setKnownPackages", "setKnownPackagesPublic", "setLeanback", "setLeanbackPublic", "setPackageStates", "setPackageStatesPublic", "setPermissionAllowlist", "setPermissionAllowlistPublic", "setPrivilegedPermissionAllowlistPackages", "setPrivilegedPermissionAllowlistPackagesPublic", "setSystemReady", "setSystemReadyPublic", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableExternalState extends com.android.server.permission.access.ExternalState {
    private MutableExternalState(com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntSet, com.android.server.permission.access.immutable.MutableIntSet> mutableReference, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>> mutableReference2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, boolean isLeanback, java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> map3, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet, com.android.server.pm.permission.PermissionAllowlist permissionAllowlist, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> indexedMap, boolean isSystemReady) {
        super(mutableReference, map, map2, mutableReference2, intMap, isLeanback, map3, indexedListSet, permissionAllowlist, indexedMap, isSystemReady, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MutableExternalState() {
        this(new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.immutable.MutableIntSet(null, 1, null)), com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap(), com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap(), new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.immutable.MutableIntReferenceMap(null, 1, 0 == true ? 1 : 0)), new com.android.server.permission.access.immutable.MutableIntMap(null, 1, null), false, com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap(), new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null), new com.android.server.pm.permission.PermissionAllowlist(), new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null), false);
    }

    public MutableExternalState(com.android.server.permission.access.ExternalState externalState) {
        this(externalState.getUserIdsReference().toImmutable(), externalState.getPackageStates(), externalState.getDisabledSystemPackageStates(), externalState.getAppIdPackageNamesReference().toImmutable(), externalState.getKnownPackages(), externalState.isLeanback(), externalState.getConfigPermissions(), externalState.getPrivilegedPermissionAllowlistPackages(), externalState.getPermissionAllowlist(), externalState.getImplicitToSourcePermissions(), externalState.isSystemReady());
    }

    public final com.android.server.permission.access.immutable.MutableIntSet mutateUserIds() {
        return (com.android.server.permission.access.immutable.MutableIntSet) getUserIdsReference().mutate();
    }

    public final void setPackageStatesPublic(java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map) {
        setPackageStates(map);
    }

    public final void setDisabledSystemPackageStatesPublic(java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map) {
        setDisabledSystemPackageStates(map);
    }

    public final com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> mutateAppIdPackageNames() {
        return (com.android.server.permission.access.immutable.MutableIntReferenceMap) getAppIdPackageNamesReference().mutate();
    }

    public final void setKnownPackagesPublic(com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap) {
        setKnownPackages(intMap);
    }

    public final void setLeanbackPublic(boolean isLeanback) {
        setLeanback(isLeanback);
    }

    public final void setConfigPermissionsPublic(java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> map) {
        setConfigPermissions(map);
    }

    public final void setPrivilegedPermissionAllowlistPackagesPublic(com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet) {
        setPrivilegedPermissionAllowlistPackages(indexedListSet);
    }

    public final void setPermissionAllowlistPublic(com.android.server.pm.permission.PermissionAllowlist permissionAllowlist) {
        setPermissionAllowlist(permissionAllowlist);
    }

    public final void setImplicitToSourcePermissionsPublic(com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> indexedMap) {
        setImplicitToSourcePermissions(indexedMap);
    }

    public final void setSystemReadyPublic(boolean isSystemReady) {
        setSystemReady(isSystemReady);
    }
}
