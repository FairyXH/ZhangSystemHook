package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\b6\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u009f\u0001\b\u0004\u0012.\u0010\u0004\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\t0\u0005j\u0002`\n\u0012.\u0010\u000b\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u0006\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\t0\u0005j\u0002`\r\u0012.\u0010\u000e\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u0006\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\t0\u0005j\u0002`\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\b\u0010#\u001a\u00020\u0003H\u0016R\u001d\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u00068F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R9\u0010\u0004\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\t0\u0005j\u0002`\n¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001d\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u00068F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0015R9\u0010\u000b\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u0006\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\t0\u0005j\u0002`\r¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017R\u001d\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u00068F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u0015R9\u0010\u000e\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u0006\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\t0\u0005j\u0002`\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R$\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u0011@TX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"\u0082\u0001\u0001\u0003¨\u0006$"}, d2 = {"Lcom/android/server/permission/access/SystemState;", "Lcom/android/server/permission/access/WritableState;", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/MutableSystemState;", "permissionGroupsReference", "Lcom/android/server/permission/access/immutable/MutableReference;", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "Landroid/content/pm/PermissionGroupInfo;", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "Lcom/android/server/permission/access/PermissionGroupsReference;", "permissionTreesReference", "Lcom/android/server/permission/access/permission/Permission;", "Lcom/android/server/permission/access/PermissionTreesReference;", "permissionsReference", "Lcom/android/server/permission/access/PermissionsReference;", "writeMode", "", "(Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;I)V", "permissionGroups", "getPermissionGroups", "()Lcom/android/server/permission/access/immutable/IndexedMap;", "getPermissionGroupsReference", "()Lcom/android/server/permission/access/immutable/MutableReference;", "permissionTrees", "getPermissionTrees", "getPermissionTreesReference", "permissions", "getPermissions", "getPermissionsReference", "<set-?>", "getWriteMode", "()I", "setWriteMode", "(I)V", "toMutable", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class SystemState implements com.android.server.permission.access.WritableState, com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.MutableSystemState> {
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>> permissionGroupsReference;
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> permissionTreesReference;
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> permissionsReference;
    private int writeMode;

    public /* synthetic */ SystemState(com.android.server.permission.access.immutable.MutableReference mutableReference, com.android.server.permission.access.immutable.MutableReference mutableReference2, com.android.server.permission.access.immutable.MutableReference mutableReference3, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(mutableReference, mutableReference2, mutableReference3, i);
    }

    private SystemState(com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>> mutableReference, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> mutableReference2, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> mutableReference3, int writeMode) {
        this.permissionGroupsReference = mutableReference;
        this.permissionTreesReference = mutableReference2;
        this.permissionsReference = mutableReference3;
        this.writeMode = writeMode;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo>> getPermissionGroupsReference() {
        return this.permissionGroupsReference;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> getPermissionTreesReference() {
        return this.permissionTreesReference;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission>> getPermissionsReference() {
        return this.permissionsReference;
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo> getPermissionGroups() {
        return (com.android.server.permission.access.immutable.IndexedMap) this.permissionGroupsReference.get();
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> getPermissionTrees() {
        return (com.android.server.permission.access.immutable.IndexedMap) this.permissionTreesReference.get();
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> getPermissions() {
        return (com.android.server.permission.access.immutable.IndexedMap) this.permissionsReference.get();
    }

    @Override // com.android.server.permission.access.WritableState
    public int getWriteMode() {
        return this.writeMode;
    }

    protected void setWriteMode(int i) {
        this.writeMode = i;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.MutableSystemState toMutable() {
        return new com.android.server.permission.access.MutableSystemState(this);
    }
}
