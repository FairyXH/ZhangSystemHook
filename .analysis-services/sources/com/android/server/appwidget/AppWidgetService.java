package com.android.server.appwidget;

/* JADX INFO: loaded from: classes.dex */
public class AppWidgetService extends com.android.server.SystemService {
    private final com.android.server.appwidget.AppWidgetServiceImpl mImpl;

    public AppWidgetService(android.content.Context context) {
        super(context);
        this.mImpl = new com.android.server.appwidget.AppWidgetServiceImpl(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mImpl.onStart();
        publishBinderService("appwidget", this.mImpl);
        com.android.server.AppWidgetBackupBridge.register(this.mImpl);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 550) {
            this.mImpl.setSafeMode(isSafeMode());
            this.mImpl.systemServicesReady();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        this.mImpl.onUserStopped(user.getUserIdentifier());
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mImpl.reloadWidgetsMaskedStateForGroup(to.getUserIdentifier());
    }
}
