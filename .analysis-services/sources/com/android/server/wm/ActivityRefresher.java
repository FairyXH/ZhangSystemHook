package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivityRefresher {
    private static final long REFRESH_CALLBACK_TIMEOUT_MS = 2000;
    private final java.util.ArrayList<com.android.server.wm.ActivityRefresher.Evaluator> mEvaluators = new java.util.ArrayList<>();
    private final android.os.Handler mHandler;
    private final com.android.server.wm.WindowManagerService mWmService;

    interface Evaluator {
        boolean shouldRefreshActivity(com.android.server.wm.ActivityRecord activityRecord, android.content.res.Configuration configuration, android.content.res.Configuration configuration2);
    }

    ActivityRefresher(com.android.server.wm.WindowManagerService wmService, android.os.Handler handler) {
        this.mWmService = wmService;
        this.mHandler = handler;
    }

    void addEvaluator(com.android.server.wm.ActivityRefresher.Evaluator evaluator) {
        this.mEvaluators.add(evaluator);
    }

    void removeEvaluator(com.android.server.wm.ActivityRefresher.Evaluator evaluator) {
        this.mEvaluators.remove(evaluator);
    }

    void onActivityConfigurationChanging(final com.android.server.wm.ActivityRecord activity, android.content.res.Configuration newConfig, android.content.res.Configuration lastReportedConfig) {
        if (!shouldRefreshActivity(activity, newConfig, lastReportedConfig)) {
            return;
        }
        boolean cycleThroughStop = this.mWmService.mLetterboxConfiguration.isCameraCompatRefreshCycleThroughStopEnabled() && !activity.mLetterboxUiController.shouldRefreshActivityViaPauseForCameraCompat();
        activity.mLetterboxUiController.setIsRefreshRequested(true);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(activity);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 7211222997110112110L, 0, null, protoLogParam0);
        }
        try {
            activity.mAtmService.getLifecycleManager().scheduleTransactionAndLifecycleItems(activity.app.getThread(), android.app.servertransaction.RefreshCallbackItem.obtain(activity.token, cycleThroughStop ? 5 : 4), android.app.servertransaction.ResumeActivityItem.obtain(activity.token, false, false));
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRefresher$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onActivityConfigurationChanging$0(activity);
                }
            }, REFRESH_CALLBACK_TIMEOUT_MS);
        } catch (android.os.RemoteException e) {
            activity.mLetterboxUiController.setIsRefreshRequested(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityConfigurationChanging$0(com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                onActivityRefreshed(activity);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean isActivityRefreshing(com.android.server.wm.ActivityRecord activity) {
        return activity.mLetterboxUiController.isRefreshRequested();
    }

    void onActivityRefreshed(com.android.server.wm.ActivityRecord activity) {
        activity.mLetterboxUiController.setIsRefreshRequested(false);
    }

    private boolean shouldRefreshActivity(final com.android.server.wm.ActivityRecord activity, final android.content.res.Configuration newConfig, final android.content.res.Configuration lastReportedConfig) {
        return this.mWmService.mLetterboxConfiguration.isCameraCompatRefreshEnabled() && activity.mLetterboxUiController.shouldRefreshActivityForCameraCompat() && com.android.internal.util.ArrayUtils.find(this.mEvaluators.toArray(), new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRefresher$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRefresher.Evaluator) obj).shouldRefreshActivity(activity, newConfig, lastReportedConfig);
            }
        }) != null;
    }
}
