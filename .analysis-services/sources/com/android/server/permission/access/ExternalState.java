package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b*\b6\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001Bÿ\u0001\b\u0004\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0002`\u0007\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t\u0012\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t\u0012N\u0010\r\u001aJ\u0012 \u0012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00100\u000ej\u0002`\u0011\u0012 \u0012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00100\u0012j\u0002`\u00130\u0004j\u0002`\u0014\u0012\u0012\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00170\u0016\u0012\u0006\u0010\u0018\u001a\u00020\u0019\u0012\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u001b0\t\u0012\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\n0\u000f\u0012\u0006\u0010\u001d\u001a\u00020\u001e\u0012\u0018\u0010\u001f\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f0 \u0012\u0006\u0010!\u001a\u00020\u0019¢\u0006\u0002\u0010\"J\b\u0010I\u001a\u00020\u0002H\u0016R-\u0010#\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00100\u000ej\u0002`\u00118F¢\u0006\u0006\u001a\u0004\b$\u0010%RY\u0010\r\u001aJ\u0012 \u0012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00100\u000ej\u0002`\u0011\u0012 \u0012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00100\u0012j\u0002`\u00130\u0004j\u0002`\u0014¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R<\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u001b0\t2\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u001b0\t@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R<\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t2\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010*\"\u0004\b.\u0010,RH\u0010\u001f\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f0 2\u0018\u0010(\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000f0 @DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R$\u0010\u0018\u001a\u00020\u00192\u0006\u0010(\u001a\u00020\u0019@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u00103\"\u0004\b4\u00105R$\u0010!\u001a\u00020\u00192\u0006\u0010(\u001a\u00020\u0019@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u00103\"\u0004\b6\u00105R<\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00170\u00162\u0012\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00170\u0016@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u00108\"\u0004\b9\u0010:R<\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t2\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010*\"\u0004\b<\u0010,R$\u0010\u001d\u001a\u00020\u001e2\u0006\u0010(\u001a\u00020\u001e@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R0\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\n0\u000f2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\n0\u000f@DX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bA\u0010B\"\u0004\bC\u0010DR\u0011\u0010E\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\bF\u0010GR!\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0002`\u0007¢\u0006\b\n\u0000\u001a\u0004\bH\u0010'\u0082\u0001\u0001\u0002¨\u0006J"}, d2 = {"Lcom/android/server/permission/access/ExternalState;", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/MutableExternalState;", "userIdsReference", "Lcom/android/server/permission/access/immutable/MutableReference;", "Lcom/android/server/permission/access/immutable/IntSet;", "Lcom/android/server/permission/access/immutable/MutableIntSet;", "Lcom/android/server/permission/access/UserIdsReference;", "packageStates", "", "", "Lcom/android/server/pm/pkg/PackageState;", "disabledSystemPackageStates", "appIdPackageNamesReference", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "Lcom/android/server/permission/access/immutable/MutableIndexedListSet;", "Lcom/android/server/permission/access/AppIdPackageNames;", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/MutableAppIdPackageNames;", "Lcom/android/server/permission/access/AppIdPackageNamesReference;", "knownPackages", "Lcom/android/server/permission/access/immutable/IntMap;", "", "isLeanback", "", "configPermissions", "Lcom/android/server/SystemConfig$PermissionEntry;", "privilegedPermissionAllowlistPackages", "permissionAllowlist", "Lcom/android/server/pm/permission/PermissionAllowlist;", "implicitToSourcePermissions", "Lcom/android/server/permission/access/immutable/IndexedMap;", "isSystemReady", "(Lcom/android/server/permission/access/immutable/MutableReference;Ljava/util/Map;Ljava/util/Map;Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/IntMap;ZLjava/util/Map;Lcom/android/server/permission/access/immutable/IndexedListSet;Lcom/android/server/pm/permission/PermissionAllowlist;Lcom/android/server/permission/access/immutable/IndexedMap;Z)V", "appIdPackageNames", "getAppIdPackageNames", "()Lcom/android/server/permission/access/immutable/IntReferenceMap;", "getAppIdPackageNamesReference", "()Lcom/android/server/permission/access/immutable/MutableReference;", "<set-?>", "getConfigPermissions", "()Ljava/util/Map;", "setConfigPermissions", "(Ljava/util/Map;)V", "getDisabledSystemPackageStates", "setDisabledSystemPackageStates", "getImplicitToSourcePermissions", "()Lcom/android/server/permission/access/immutable/IndexedMap;", "setImplicitToSourcePermissions", "(Lcom/android/server/permission/access/immutable/IndexedMap;)V", "()Z", "setLeanback", "(Z)V", "setSystemReady", "getKnownPackages", "()Lcom/android/server/permission/access/immutable/IntMap;", "setKnownPackages", "(Lcom/android/server/permission/access/immutable/IntMap;)V", "getPackageStates", "setPackageStates", "getPermissionAllowlist", "()Lcom/android/server/pm/permission/PermissionAllowlist;", "setPermissionAllowlist", "(Lcom/android/server/pm/permission/PermissionAllowlist;)V", "getPrivilegedPermissionAllowlistPackages", "()Lcom/android/server/permission/access/immutable/IndexedListSet;", "setPrivilegedPermissionAllowlistPackages", "(Lcom/android/server/permission/access/immutable/IndexedListSet;)V", "userIds", "getUserIds", "()Lcom/android/server/permission/access/immutable/IntSet;", "getUserIdsReference", "toMutable", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class ExternalState implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.MutableExternalState> {
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>> appIdPackageNamesReference;
    private java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> configPermissions;
    private java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> disabledSystemPackageStates;
    private com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> implicitToSourcePermissions;
    private boolean isLeanback;
    private boolean isSystemReady;
    private com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages;
    private java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> packageStates;
    private com.android.server.pm.permission.PermissionAllowlist permissionAllowlist;
    private com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> privilegedPermissionAllowlistPackages;
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntSet, com.android.server.permission.access.immutable.MutableIntSet> userIdsReference;

    public /* synthetic */ ExternalState(com.android.server.permission.access.immutable.MutableReference mutableReference, java.util.Map map, java.util.Map map2, com.android.server.permission.access.immutable.MutableReference mutableReference2, com.android.server.permission.access.immutable.IntMap intMap, boolean z, java.util.Map map3, com.android.server.permission.access.immutable.IndexedListSet indexedListSet, com.android.server.pm.permission.PermissionAllowlist permissionAllowlist, com.android.server.permission.access.immutable.IndexedMap indexedMap, boolean z2, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(mutableReference, map, map2, mutableReference2, intMap, z, map3, indexedListSet, permissionAllowlist, indexedMap, z2);
    }

    private ExternalState(com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntSet, com.android.server.permission.access.immutable.MutableIntSet> mutableReference, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map, java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map2, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>> mutableReference2, com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap, boolean isLeanback, java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> map3, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet, com.android.server.pm.permission.PermissionAllowlist permissionAllowlist, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> indexedMap, boolean isSystemReady) {
        this.userIdsReference = mutableReference;
        this.appIdPackageNamesReference = mutableReference2;
        this.packageStates = map;
        this.disabledSystemPackageStates = map2;
        this.knownPackages = intMap;
        this.isLeanback = isLeanback;
        this.configPermissions = map3;
        this.privilegedPermissionAllowlistPackages = indexedListSet;
        this.permissionAllowlist = permissionAllowlist;
        this.implicitToSourcePermissions = indexedMap;
        this.isSystemReady = isSystemReady;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntSet, com.android.server.permission.access.immutable.MutableIntSet> getUserIdsReference() {
        return this.userIdsReference;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>>> getAppIdPackageNamesReference() {
        return this.appIdPackageNamesReference;
    }

    public final com.android.server.permission.access.immutable.IntSet getUserIds() {
        return (com.android.server.permission.access.immutable.IntSet) this.userIdsReference.get();
    }

    public final java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getPackageStates() {
        return this.packageStates;
    }

    protected final void setPackageStates(java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map) {
        this.packageStates = map;
    }

    public final java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getDisabledSystemPackageStates() {
        return this.disabledSystemPackageStates;
    }

    protected final void setDisabledSystemPackageStates(java.util.Map<java.lang.String, ? extends com.android.server.pm.pkg.PackageState> map) {
        this.disabledSystemPackageStates = map;
    }

    public final com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> getAppIdPackageNames() {
        return (com.android.server.permission.access.immutable.IntReferenceMap) this.appIdPackageNamesReference.get();
    }

    public final com.android.server.permission.access.immutable.IntMap<java.lang.String[]> getKnownPackages() {
        return this.knownPackages;
    }

    protected final void setKnownPackages(com.android.server.permission.access.immutable.IntMap<java.lang.String[]> intMap) {
        this.knownPackages = intMap;
    }

    public final boolean isLeanback() {
        return this.isLeanback;
    }

    protected final void setLeanback(boolean z) {
        this.isLeanback = z;
    }

    public final java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> getConfigPermissions() {
        return this.configPermissions;
    }

    protected final void setConfigPermissions(java.util.Map<java.lang.String, com.android.server.SystemConfig.PermissionEntry> map) {
        this.configPermissions = map;
    }

    public final com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> getPrivilegedPermissionAllowlistPackages() {
        return this.privilegedPermissionAllowlistPackages;
    }

    protected final void setPrivilegedPermissionAllowlistPackages(com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet) {
        this.privilegedPermissionAllowlistPackages = indexedListSet;
    }

    public final com.android.server.pm.permission.PermissionAllowlist getPermissionAllowlist() {
        return this.permissionAllowlist;
    }

    protected final void setPermissionAllowlist(com.android.server.pm.permission.PermissionAllowlist permissionAllowlist) {
        this.permissionAllowlist = permissionAllowlist;
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> getImplicitToSourcePermissions() {
        return this.implicitToSourcePermissions;
    }

    protected final void setImplicitToSourcePermissions(com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> indexedMap) {
        this.implicitToSourcePermissions = indexedMap;
    }

    public final boolean isSystemReady() {
        return this.isSystemReady;
    }

    protected final void setSystemReady(boolean z) {
        this.isSystemReady = z;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.MutableExternalState toMutable() {
        return new com.android.server.permission.access.MutableExternalState(this);
    }
}
