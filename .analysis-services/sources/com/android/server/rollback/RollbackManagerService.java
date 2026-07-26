package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
public final class RollbackManagerService extends com.android.server.SystemService {
    private com.android.server.rollback.RollbackManagerServiceImpl mService;

    public RollbackManagerService(android.content.Context context) {
        super(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mService = new com.android.server.rollback.RollbackManagerServiceImpl(getContext());
        publishBinderService("rollback", this.mService);
        com.android.server.LocalServices.addService(com.android.server.rollback.RollbackManagerInternal.class, this.mService);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        this.mService.onUnlockUser(user.getUserIdentifier());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 1000) {
            this.mService.onBootCompleted();
        }
    }
}
