package com.android.server;

/* JADX INFO: loaded from: classes.dex */
class ContextHubSystemService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "ContextHubSystemService";
    private com.android.server.location.contexthub.ContextHubService mContextHubService;
    private java.util.concurrent.Future<?> mInit;

    public ContextHubSystemService(final android.content.Context context) {
        super(context);
        this.mInit = com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.ContextHubSystemService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(context);
            }
        }, "Init ContextHubSystemService");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.content.Context context) {
        this.mContextHubService = new com.android.server.location.contexthub.ContextHubService(context, com.android.server.location.contexthub.IContextHubWrapper.getContextHubWrapper());
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            android.util.Log.d(TAG, "onBootPhase: PHASE_SYSTEM_SERVICES_READY");
            com.android.internal.util.ConcurrentUtils.waitForFutureNoInterrupt(this.mInit, "Wait for ContextHubSystemService init");
            this.mInit = null;
            publishBinderService("contexthub", this.mContextHubService);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mContextHubService.onUserChanged();
    }
}
