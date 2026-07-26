package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0016¢\u0006\u0002\u0010\u0003B\u000f\b\u0010\u0012\u0006\u0010\u0004\u001a\u00020\u0001¢\u0006\u0002\u0010\u0005B\u009f\u0001\b\u0002\u0012.\u0010\u0006\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000b0\u0007j\u0002`\f\u0012.\u0010\r\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\u000b0\u0007j\u0002`\u000f\u0012.\u0010\u0010\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\u000b0\u0007j\u0002`\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000bJ\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\u000bJ\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\u000bJ\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0012\u001a\u00020\u0013H\u0016¨\u0006\u001a"}, d2 = {"Lcom/android/server/permission/access/MutableSystemState;", "Lcom/android/server/permission/access/SystemState;", "Lcom/android/server/permission/access/MutableWritableState;", "()V", "systemState", "(Lcom/android/server/permission/access/SystemState;)V", "permissionGroupsReference", "Lcom/android/server/permission/access/immutable/MutableReference;", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "Landroid/content/pm/PermissionGroupInfo;", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "Lcom/android/server/permission/access/PermissionGroupsReference;", "permissionTreesReference", "Lcom/android/server/permission/access/permission/Permission;", "Lcom/android/server/permission/access/PermissionTreesReference;", "permissionsReference", "Lcom/android/server/permission/access/PermissionsReference;", "writeMode", "", "(Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;I)V", "mutatePermissionGroups", "mutatePermissionTrees", "mutatePermissions", "requestWriteMode", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableSystemState extends com.android.server.permission.access.SystemState implements com.android.server.permission.access.MutableWritableState {
    private MutableSystemState(com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>> mutableReference, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> mutableReference2, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> mutableReference3, int writeMode) {
        super(mutableReference, mutableReference2, mutableReference3, writeMode, null);
    }

    public MutableSystemState() {
        this(new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null)), new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null)), new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null)), 0);
    }

    public MutableSystemState(com.android.server.permission.access.SystemState systemState) {
        this(systemState.getPermissionGroupsReference().toImmutable(), systemState.getPermissionTreesReference().toImmutable(), systemState.getPermissionsReference().toImmutable(), 0);
    }

    public final com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo> mutatePermissionGroups() {
        return (com.android.server.permission.access.immutable.MutableIndexedMap) getPermissionGroupsReference().mutate();
    }

    public final com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> mutatePermissionTrees() {
        return (com.android.server.permission.access.immutable.MutableIndexedMap) getPermissionTreesReference().mutate();
    }

    public final com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> mutatePermissions() {
        return (com.android.server.permission.access.immutable.MutableIndexedMap) getPermissionsReference().mutate();
    }

    @Override // com.android.server.permission.access.MutableWritableState
    public void requestWriteMode(int writeMode) {
        setWriteMode(java.lang.Math.max(getWriteMode(), writeMode));
    }
}
