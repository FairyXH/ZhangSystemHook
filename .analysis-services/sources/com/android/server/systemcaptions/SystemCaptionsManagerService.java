package com.android.server.systemcaptions;

/* JADX INFO: loaded from: classes3.dex */
public final class SystemCaptionsManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.systemcaptions.SystemCaptionsManagerService, com.android.server.systemcaptions.SystemCaptionsManagerPerUserService> {
    public SystemCaptionsManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_deviceSpecificAudioService), null, 68);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.systemcaptions.SystemCaptionsManagerPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        com.android.server.systemcaptions.SystemCaptionsManagerPerUserService perUserService = new com.android.server.systemcaptions.SystemCaptionsManagerPerUserService(this, this.mLock, disabled, resolvedUserId);
        perUserService.initializeLocked();
        return perUserService;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(com.android.server.systemcaptions.SystemCaptionsManagerPerUserService service, int userId) {
        synchronized (this.mLock) {
            service.destroyLocked();
        }
    }
}
