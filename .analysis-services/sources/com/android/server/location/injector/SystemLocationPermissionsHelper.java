package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemLocationPermissionsHelper extends com.android.server.location.injector.LocationPermissionsHelper {
    private final android.content.Context mContext;
    private boolean mInited;

    public SystemLocationPermissionsHelper(android.content.Context context, com.android.server.location.injector.AppOpsHelper appOps) {
        super(appOps);
        this.mContext = context;
    }

    public void onSystemReady() {
        if (this.mInited) {
            return;
        }
        this.mContext.getPackageManager().addOnPermissionsChangeListener(new android.content.pm.PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.location.injector.SystemLocationPermissionsHelper$$ExternalSyntheticLambda0
            public final void onPermissionsChanged(int i) {
                this.f$0.lambda$onSystemReady$1(i);
            }
        });
        this.mInited = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0(int uid) {
        notifyLocationPermissionsChanged(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$1(final int uid) {
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.injector.SystemLocationPermissionsHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSystemReady$0(uid);
            }
        });
    }

    @Override // com.android.server.location.injector.LocationPermissionsHelper
    protected boolean hasPermission(java.lang.String permission, android.location.util.identity.CallerIdentity callerIdentity) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mContext.checkPermission(permission, callerIdentity.getPid(), callerIdentity.getUid()) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
