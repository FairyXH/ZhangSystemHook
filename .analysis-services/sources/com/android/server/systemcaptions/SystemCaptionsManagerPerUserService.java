package com.android.server.systemcaptions;

/* JADX INFO: loaded from: classes3.dex */
final class SystemCaptionsManagerPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.systemcaptions.SystemCaptionsManagerPerUserService, com.android.server.systemcaptions.SystemCaptionsManagerService> {
    private static final java.lang.String TAG = com.android.server.systemcaptions.SystemCaptionsManagerPerUserService.class.getSimpleName();
    private com.android.server.systemcaptions.RemoteSystemCaptionsManagerService mRemoteService;

    SystemCaptionsManagerPerUserService(com.android.server.systemcaptions.SystemCaptionsManagerService master, java.lang.Object lock, boolean disabled, int userId) {
        super(master, lock, userId);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            return android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    void initializeLocked() {
        if (((com.android.server.systemcaptions.SystemCaptionsManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "initialize()");
        }
        com.android.server.systemcaptions.RemoteSystemCaptionsManagerService service = getRemoteServiceLocked();
        if (service == null && ((com.android.server.systemcaptions.SystemCaptionsManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "initialize(): Failed to init remote server");
        }
    }

    void destroyLocked() {
        if (((com.android.server.systemcaptions.SystemCaptionsManagerService) this.mMaster).verbose) {
            android.util.Slog.v(TAG, "destroyLocked()");
        }
        if (this.mRemoteService != null) {
            this.mRemoteService.destroy();
            this.mRemoteService = null;
        }
    }

    private com.android.server.systemcaptions.RemoteSystemCaptionsManagerService getRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.systemcaptions.SystemCaptionsManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "getRemoteServiceLocked(): Not set");
                    return null;
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            this.mRemoteService = new com.android.server.systemcaptions.RemoteSystemCaptionsManagerService(getContext(), serviceComponent, this.mUserId, ((com.android.server.systemcaptions.SystemCaptionsManagerService) this.mMaster).verbose);
            if (((com.android.server.systemcaptions.SystemCaptionsManagerService) this.mMaster).verbose) {
                android.util.Slog.v(TAG, "getRemoteServiceLocked(): initialize for user " + this.mUserId);
            }
            this.mRemoteService.initialize();
        }
        return this.mRemoteService;
    }
}
