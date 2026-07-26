package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: PermissionManagerLocalImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/android/server/permission/access/permission/PermissionManagerLocalImpl;", "Lcom/android/server/permission/PermissionManagerLocal;", com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "Lcom/android/server/permission/access/AccessCheckingService;", "(Lcom/android/server/permission/access/AccessCheckingService;)V", "policy", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy;", "isSignaturePermissionAllowlistForceEnforced", "", "setSignaturePermissionAllowlistForceEnforced", "", "forceEnforced", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PermissionManagerLocalImpl implements com.android.server.permission.PermissionManagerLocal {
    private final com.android.server.permission.access.permission.AppIdPermissionPolicy policy;
    private final com.android.server.permission.access.AccessCheckingService service;

    public PermissionManagerLocalImpl(com.android.server.permission.access.AccessCheckingService service) {
        this.service = service;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.PermissionUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar, "null cannot be cast to non-null type com.android.server.permission.access.permission.AppIdPermissionPolicy");
        this.policy = (com.android.server.permission.access.permission.AppIdPermissionPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar;
    }

    @Override // com.android.server.permission.PermissionManagerLocal
    public boolean isSignaturePermissionAllowlistForceEnforced() {
        if (!android.os.Build.isDebuggable()) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        return this.policy.isSignaturePermissionAllowlistForceEnforced();
    }

    @Override // com.android.server.permission.PermissionManagerLocal
    public void setSignaturePermissionAllowlistForceEnforced(boolean forceEnforced) {
        if (!android.os.Build.isDebuggable()) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        this.policy.setSignaturePermissionAllowlistForceEnforced(forceEnforced);
    }
}
