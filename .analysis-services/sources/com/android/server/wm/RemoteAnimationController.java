package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RemoteAnimationController implements android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "RemoteAnimationController";
    private static final long TIMEOUT_MS = 10000;
    private boolean mCanceled;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private com.android.server.wm.RemoteAnimationController.FinishedCallback mFinishedCallback;
    private final android.os.Handler mHandler;
    private final boolean mIsActivityEmbedding;
    private boolean mIsFinishing;
    private boolean mLinkedToDeathOfRunner;
    private java.lang.Runnable mOnRemoteAnimationReady;
    private final android.view.RemoteAnimationAdapter mRemoteAnimationAdapter;
    private final com.android.server.wm.WindowManagerService mService;
    private final java.util.ArrayList<com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord> mPendingAnimations = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.WallpaperAnimationAdapter> mPendingWallpaperAnimations = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.NonAppWindowAnimationAdapter> mPendingNonAppAnimations = new java.util.ArrayList<>();
    private final java.lang.Runnable mTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        cancelAnimation("timeoutRunnable");
    }

    RemoteAnimationController(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent, android.view.RemoteAnimationAdapter remoteAnimationAdapter, android.os.Handler handler, boolean isActivityEmbedding) {
        this.mService = service;
        this.mDisplayContent = displayContent;
        this.mRemoteAnimationAdapter = remoteAnimationAdapter;
        this.mHandler = handler;
        this.mIsActivityEmbedding = isActivityEmbedding;
    }

    com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord createRemoteAnimationRecord(com.android.server.wm.WindowContainer windowContainer, android.graphics.Point position, android.graphics.Rect localBounds, android.graphics.Rect endBounds, android.graphics.Rect startBounds, boolean showBackdrop) {
        return createRemoteAnimationRecord(windowContainer, position, localBounds, endBounds, startBounds, showBackdrop, startBounds != null);
    }

    com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord createRemoteAnimationRecord(com.android.server.wm.WindowContainer windowContainer, android.graphics.Point position, android.graphics.Rect localBounds, android.graphics.Rect endBounds, android.graphics.Rect startBounds, boolean showBackdrop, boolean shouldCreateSnapshot) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(windowContainer);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -5444412205083968021L, 0, null, protoLogParam0);
        }
        com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord adapters = new com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord(windowContainer, position, localBounds, endBounds, startBounds, showBackdrop, shouldCreateSnapshot);
        this.mPendingAnimations.add(adapters);
        return adapters;
    }

    void setOnRemoteAnimationReady(java.lang.Runnable onRemoteAnimationReady) {
        this.mOnRemoteAnimationReady = onRemoteAnimationReady;
    }

    public boolean isFromActivityEmbedding() {
        return this.mIsActivityEmbedding;
    }

    void goodToGo(final int transit) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 6986037643494242400L, 0, null, null);
        }
        if (this.mCanceled) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1902984034737899928L, 0, null, null);
            }
            onAnimationFinished();
            invokeAnimationCancelled("already_cancelled");
            return;
        }
        this.mHandler.postDelayed(this.mTimeoutRunnable, (long) (this.mService.getCurrentAnimatorScale() * 10000.0f));
        this.mFinishedCallback = new com.android.server.wm.RemoteAnimationController.FinishedCallback(this);
        final android.view.RemoteAnimationTarget[] appTargets = createAppAnimations();
        if (appTargets.length == 0 && !com.android.server.wm.AppTransition.isKeyguardOccludeTransitOld(transit)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                long protoLogParam0 = this.mPendingAnimations.size();
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 6727618365838540075L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            onAnimationFinished();
            invokeAnimationCancelled("no_app_targets");
            return;
        }
        if (this.mOnRemoteAnimationReady != null) {
            this.mOnRemoteAnimationReady.run();
            this.mOnRemoteAnimationReady = null;
        }
        final android.view.RemoteAnimationTarget[] wallpaperTargets = createWallpaperAnimations();
        final android.view.RemoteAnimationTarget[] nonAppTargets = createNonAppWindowAnimations(transit);
        this.mService.mAnimator.addAfterPrepareSurfacesRunnable(new java.lang.Runnable() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$goodToGo$1(transit, appTargets, wallpaperTargets, nonAppTargets);
            }
        });
        setRunningRemoteAnimation(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$goodToGo$1(int transit, android.view.RemoteAnimationTarget[] appTargets, android.view.RemoteAnimationTarget[] wallpaperTargets, android.view.RemoteAnimationTarget[] nonAppTargets) {
        try {
            linkToDeathOfRunner();
            android.util.Slog.d(TAG, "goodToGo onAnimationStart transit=" + com.android.server.wm.AppTransition.appTransitionOldToString(transit) + ", apps=:" + appTargets.length + ", wallpapers=" + wallpaperTargets.length + ", nonApps=" + nonAppTargets.length + ", animation:" + this);
            if (com.android.server.wm.AppTransition.isKeyguardOccludeTransitOld(transit)) {
                com.android.server.wm.EventLogTags.writeWmSetKeyguardOccluded(transit == 23 ? 0 : 1, 1, transit, "onAnimationStart");
            }
            this.mRemoteAnimationAdapter.getRunner().onAnimationStart(transit, appTargets, wallpaperTargets, nonAppTargets, this.mFinishedCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to start remote animation", e);
            onAnimationFinished();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, com.android.internal.protolog.common.LogLevel.DEBUG)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1148281153370899511L, 0, null, null);
            }
            writeStartDebugStatement();
        }
    }

    void cancelAnimation(java.lang.String reason) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(reason);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 7501495587927045391L, 0, null, protoLogParam0);
        }
        synchronized (this.mService.getWindowManagerLock()) {
            if (this.mCanceled) {
                return;
            }
            this.mCanceled = true;
            onAnimationFinished();
            invokeAnimationCancelled(reason);
        }
    }

    private void writeStartDebugStatement() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1424368765415574722L, 0, null, null);
        }
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(sw);
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            this.mPendingAnimations.get(i).mAdapter.dump(fastPrintWriter, "");
        }
        fastPrintWriter.close();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(sw.toString());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -2676700429940607853L, 0, null, protoLogParam0);
        }
    }

    private android.view.RemoteAnimationTarget[] createAppAnimations() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 7094394833775573933L, 0, null, null);
        }
        java.util.ArrayList<android.view.RemoteAnimationTarget> targets = new java.util.ArrayList<>();
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            if (i < this.mPendingAnimations.size()) {
                com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord wrappers = this.mPendingAnimations.get(i);
                com.android.server.wm.ActivityRecord ar = wrappers.mWindowContainer != null ? wrappers.mWindowContainer.asActivityRecord() : null;
                if (ar == null || ar.getTask() == null || !ar.getTask().getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
                    android.view.RemoteAnimationTarget target = wrappers.createRemoteAnimationTarget();
                    if (target != null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                            java.lang.String protoLogParam0 = java.lang.String.valueOf(wrappers.mWindowContainer);
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -4411070227420990074L, 0, null, protoLogParam0);
                        }
                        targets.add(target);
                    } else {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(wrappers.mWindowContainer);
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -4411631520586057580L, 0, null, protoLogParam02);
                        }
                        if (wrappers.mAdapter != null && wrappers.mAdapter.mCapturedFinishCallback != null) {
                            wrappers.mAdapter.mCapturedFinishCallback.onAnimationFinished(wrappers.mAdapter.mAnimationType, wrappers.mAdapter);
                        }
                        if (wrappers.mThumbnailAdapter != null && wrappers.mThumbnailAdapter.mCapturedFinishCallback != null) {
                            wrappers.mThumbnailAdapter.mCapturedFinishCallback.onAnimationFinished(wrappers.mThumbnailAdapter.mAnimationType, wrappers.mThumbnailAdapter);
                        }
                        this.mPendingAnimations.remove(wrappers);
                    }
                }
            }
        }
        return (android.view.RemoteAnimationTarget[]) targets.toArray(new android.view.RemoteAnimationTarget[targets.size()]);
    }

    private android.view.RemoteAnimationTarget[] createWallpaperAnimations() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -7002230949892506736L, 0, null, null);
        }
        return com.android.server.wm.WallpaperAnimationAdapter.startWallpaperAnimations(this.mDisplayContent, this.mRemoteAnimationAdapter.getDuration(), this.mRemoteAnimationAdapter.getStatusBarTransitionDelay(), new java.util.function.Consumer() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$createWallpaperAnimations$2((com.android.server.wm.WallpaperAnimationAdapter) obj);
            }
        }, this.mPendingWallpaperAnimations);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createWallpaperAnimations$2(com.android.server.wm.WallpaperAnimationAdapter adapter) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPendingWallpaperAnimations.remove(adapter);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private android.view.RemoteAnimationTarget[] createNonAppWindowAnimations(int transit) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 8743612568733301175L, 0, null, null);
        }
        return com.android.server.wm.NonAppWindowAnimationAdapter.startNonAppWindowAnimations(this.mService, this.mDisplayContent, transit, this.mRemoteAnimationAdapter.getDuration(), this.mRemoteAnimationAdapter.getStatusBarTransitionDelay(), this.mPendingNonAppAnimations);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAnimationFinished() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            long protoLogParam0 = this.mPendingAnimations.size();
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -2716313493239418198L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        this.mHandler.removeCallbacks(this.mTimeoutRunnable);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mIsFinishing = true;
                unlinkToDeathOfRunner();
                releaseFinishedCallback();
                try {
                    try {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 7221400292415257709L, 0, null, null);
                        }
                        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
                            if (i < this.mPendingAnimations.size()) {
                                com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord adapters = this.mPendingAnimations.get(i);
                                if (adapters.mAdapter != null && adapters.mAdapter.mCapturedFinishCallback != null) {
                                    adapters.mAdapter.mCapturedFinishCallback.onAnimationFinished(adapters.mAdapter.mAnimationType, adapters.mAdapter);
                                }
                                if (adapters.mThumbnailAdapter != null && adapters.mThumbnailAdapter.mCapturedFinishCallback != null) {
                                    adapters.mThumbnailAdapter.mCapturedFinishCallback.onAnimationFinished(adapters.mThumbnailAdapter.mAnimationType, adapters.mThumbnailAdapter);
                                }
                                this.mPendingAnimations.remove(adapters);
                                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                                    java.lang.String protoLogParam02 = java.lang.String.valueOf(adapters.mWindowContainer);
                                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 7483194715776694698L, 0, null, protoLogParam02);
                                }
                            }
                        }
                        for (int i2 = this.mPendingWallpaperAnimations.size() - 1; i2 >= 0; i2--) {
                            com.android.server.wm.WallpaperAnimationAdapter adapter = this.mPendingWallpaperAnimations.get(i2);
                            adapter.getLeashFinishedCallback().onAnimationFinished(adapter.getLastAnimationType(), adapter);
                            this.mPendingWallpaperAnimations.remove(i2);
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                                java.lang.String protoLogParam03 = java.lang.String.valueOf(adapter.getToken());
                                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 6697982664439247822L, 0, null, protoLogParam03);
                            }
                        }
                        for (int i3 = this.mPendingNonAppAnimations.size() - 1; i3 >= 0; i3--) {
                            com.android.server.wm.NonAppWindowAnimationAdapter adapter2 = this.mPendingNonAppAnimations.get(i3);
                            adapter2.getLeashFinishedCallback().onAnimationFinished(adapter2.getLastAnimationType(), adapter2);
                            this.mPendingNonAppAnimations.remove(i3);
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                                java.lang.String protoLogParam04 = java.lang.String.valueOf(adapter2.getWindowContainer());
                                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 6938838346517131964L, 0, null, protoLogParam04);
                            }
                        }
                        this.mDisplayContent.mAppTransition.getWrapper().getExtImpl().validateKeyguardOcclusion(this.mDisplayContent);
                        this.mIsFinishing = false;
                        java.util.function.Consumer<com.android.server.wm.ActivityRecord> updateActivities = new java.util.function.Consumer() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.wm.ActivityRecord) obj).setDropInputForAnimation(false);
                            }
                        };
                        this.mDisplayContent.forAllActivities(updateActivities);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(TAG, "Failed to finish remote animation", e);
                        throw e;
                    }
                } catch (java.lang.Throwable th) {
                    this.mIsFinishing = false;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        setRunningRemoteAnimation(false);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -3880290251819699866L, 0, null, null);
        }
    }

    private void invokeAnimationCancelled(java.lang.String reason) {
        boolean isKeyguardOccluded = this.mDisplayContent.isKeyguardOccluded();
        try {
            com.android.server.wm.EventLogTags.writeWmSetKeyguardOccluded(isKeyguardOccluded ? 1 : 0, 0, 0, "onAnimationCancelled");
            android.util.Slog.d(TAG, "cancelAnimation: reason=" + reason + ", isKeyguardOccluded=" + isKeyguardOccluded + ", animation:" + this + ", Callers=" + android.os.Debug.getCallers(5));
            this.mRemoteAnimationAdapter.getRunner().onAnimationCancelled();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to notify cancel", e);
        }
        this.mOnRemoteAnimationReady = null;
    }

    private void releaseFinishedCallback() {
        if (this.mFinishedCallback != null) {
            this.mFinishedCallback.release();
            this.mFinishedCallback = null;
        }
    }

    private void setRunningRemoteAnimation(boolean running) {
        int pid = this.mRemoteAnimationAdapter.getCallingPid();
        int uid = this.mRemoteAnimationAdapter.getCallingUid();
        if (pid == 0) {
            throw new java.lang.RuntimeException("Calling pid of remote animation was null");
        }
        com.android.server.wm.WindowProcessController wpc = this.mService.mAtmService.getProcessController(pid, uid);
        if (wpc == null) {
            android.util.Slog.w(TAG, "Unable to find process with pid=" + pid + " uid=" + uid);
        } else {
            wpc.setRunningRemoteAnimation(running);
        }
    }

    private void linkToDeathOfRunner() throws android.os.RemoteException {
        if (!this.mLinkedToDeathOfRunner) {
            this.mRemoteAnimationAdapter.getRunner().asBinder().linkToDeath(this, 0);
            this.mLinkedToDeathOfRunner = true;
        }
    }

    private void unlinkToDeathOfRunner() {
        if (this.mLinkedToDeathOfRunner) {
            this.mRemoteAnimationAdapter.getRunner().asBinder().unlinkToDeath(this, 0);
            this.mLinkedToDeathOfRunner = false;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        cancelAnimation("binderDied");
    }

    private static final class FinishedCallback extends android.view.IRemoteAnimationFinishedCallback.Stub {
        com.android.server.wm.RemoteAnimationController mOuter;

        FinishedCallback(com.android.server.wm.RemoteAnimationController outer) {
            this.mOuter = outer;
        }

        public void onAnimationFinished() throws android.os.RemoteException {
            android.util.Slog.d(com.android.server.wm.RemoteAnimationController.TAG, "app-onAnimationFinished(): mOuter=" + this.mOuter);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (this.mOuter != null) {
                    this.mOuter.onAnimationFinished();
                    this.mOuter = null;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        void release() {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mOuter);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 3923111589554171989L, 0, null, protoLogParam0);
            }
            this.mOuter = null;
        }
    }

    public class RemoteAnimationRecord {
        com.android.server.wm.RemoteAnimationController.RemoteAnimationAdapterWrapper mAdapter;
        int mBackdropColor = 0;
        private int mMode = 2;
        final boolean mShowBackdrop;
        final android.graphics.Rect mStartBounds;
        android.view.RemoteAnimationTarget mTarget;
        com.android.server.wm.RemoteAnimationController.RemoteAnimationAdapterWrapper mThumbnailAdapter;
        final com.android.server.wm.WindowContainer mWindowContainer;

        RemoteAnimationRecord(com.android.server.wm.WindowContainer windowContainer, android.graphics.Point endPos, android.graphics.Rect localBounds, android.graphics.Rect endBounds, android.graphics.Rect startBounds, boolean showBackdrop, boolean shouldCreateSnapshot) {
            this.mThumbnailAdapter = null;
            this.mWindowContainer = windowContainer;
            this.mShowBackdrop = showBackdrop;
            if (startBounds != null) {
                this.mStartBounds = new android.graphics.Rect(startBounds);
                this.mAdapter = com.android.server.wm.RemoteAnimationController.this.new RemoteAnimationAdapterWrapper(this, endPos, localBounds, endBounds, this.mStartBounds, this.mShowBackdrop);
                if (shouldCreateSnapshot && com.android.server.wm.RemoteAnimationController.this.mRemoteAnimationAdapter.getChangeNeedsSnapshot()) {
                    android.graphics.Rect thumbnailLocalBounds = new android.graphics.Rect(startBounds);
                    thumbnailLocalBounds.offsetTo(0, 0);
                    this.mThumbnailAdapter = com.android.server.wm.RemoteAnimationController.this.new RemoteAnimationAdapterWrapper(this, new android.graphics.Point(0, 0), thumbnailLocalBounds, startBounds, new android.graphics.Rect(), this.mShowBackdrop);
                    return;
                }
                return;
            }
            this.mAdapter = com.android.server.wm.RemoteAnimationController.this.new RemoteAnimationAdapterWrapper(this, endPos, localBounds, endBounds, new android.graphics.Rect(), this.mShowBackdrop);
            this.mStartBounds = null;
        }

        void setBackDropColor(int backdropColor) {
            this.mBackdropColor = backdropColor;
        }

        android.view.RemoteAnimationTarget createRemoteAnimationTarget() {
            if (this.mAdapter == null || this.mAdapter.mCapturedFinishCallback == null || this.mAdapter.mCapturedLeash == null) {
                return null;
            }
            this.mTarget = this.mWindowContainer.createRemoteAnimationTarget(this);
            return this.mTarget;
        }

        void setMode(int mode) {
            this.mMode = mode;
        }

        int getMode() {
            return this.mMode;
        }

        boolean hasAnimatingParent() {
            for (int i = com.android.server.wm.RemoteAnimationController.this.mDisplayContent.mChangingContainers.size() - 1; i >= 0; i--) {
                if (this.mWindowContainer.isDescendantOf(com.android.server.wm.RemoteAnimationController.this.mDisplayContent.mChangingContainers.valueAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    class RemoteAnimationAdapterWrapper implements com.android.server.wm.AnimationAdapter {
        private int mAnimationType;
        private com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mCapturedFinishCallback;
        android.view.SurfaceControl mCapturedLeash;
        final android.graphics.Rect mLocalBounds;
        private final com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord mRecord;
        final boolean mShowBackdrop;
        final android.graphics.Point mPosition = new android.graphics.Point();
        final android.graphics.Rect mEndBounds = new android.graphics.Rect();
        final android.graphics.Rect mStartBounds = new android.graphics.Rect();

        RemoteAnimationAdapterWrapper(com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord record, android.graphics.Point position, android.graphics.Rect localBounds, android.graphics.Rect endBounds, android.graphics.Rect startBounds, boolean showBackdrop) {
            this.mRecord = record;
            this.mPosition.set(position.x, position.y);
            this.mLocalBounds = localBounds;
            this.mEndBounds.set(endBounds);
            this.mStartBounds.set(startBounds);
            this.mShowBackdrop = showBackdrop;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public int getBackgroundColor() {
            return this.mRecord.mBackdropColor;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowBackground() {
            return this.mShowBackdrop;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 8918152561092803537L, 0, null, null);
            }
            if (this.mRecord.mWindowContainer.mSurfaceAnimator.getWrapper().getExtImpl().isReuseLeash()) {
                this.mCapturedLeash = animationLeash;
                this.mCapturedFinishCallback = finishCallback;
                this.mAnimationType = type;
                return;
            }
            if (this.mStartBounds.isEmpty()) {
                t.setPosition(animationLeash, this.mPosition.x, this.mPosition.y);
                t.setWindowCrop(animationLeash, this.mEndBounds.width(), this.mEndBounds.height());
                if (!this.mRecord.mWindowContainer.mWindowContainerExt.shouldCropAnimationLeashInEmbedding(this.mRecord.mWindowContainer)) {
                    t.setWindowCrop(animationLeash, 0, 0);
                }
            } else {
                t.setPosition(animationLeash, (this.mPosition.x + this.mStartBounds.left) - this.mEndBounds.left, (this.mPosition.y + this.mStartBounds.top) - this.mEndBounds.top);
                t.setWindowCrop(animationLeash, this.mStartBounds.width(), this.mStartBounds.height());
            }
            this.mCapturedLeash = animationLeash;
            this.mCapturedFinishCallback = finishCallback;
            this.mAnimationType = type;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
            if (com.android.server.wm.RemoteAnimationController.this.mIsFinishing) {
                return;
            }
            if (this.mRecord.mAdapter == this) {
                this.mRecord.mAdapter = null;
            } else {
                this.mRecord.mThumbnailAdapter = null;
            }
            if (this.mRecord.mAdapter == null && this.mRecord.mThumbnailAdapter == null) {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RemoteAnimationController.this.mService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.RemoteAnimationController.this.mPendingAnimations.remove(this.mRecord);
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
            boolean hasSameContainer = false;
            int i = com.android.server.wm.RemoteAnimationController.this.mPendingAnimations.size() - 1;
            while (true) {
                if (i >= 0) {
                    com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord wrappers = (com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord) com.android.server.wm.RemoteAnimationController.this.mPendingAnimations.get(i);
                    if (wrappers == this.mRecord || wrappers.mWindowContainer != this.mRecord.mWindowContainer) {
                        i--;
                    } else {
                        hasSameContainer = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (com.android.server.wm.RemoteAnimationController.this.mPendingAnimations.isEmpty() || (!this.mRecord.mWindowContainer.isActivityTypeHome() && !hasSameContainer)) {
                com.android.server.wm.RemoteAnimationController.this.cancelAnimation("allAppAnimationsCanceled");
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return com.android.server.wm.RemoteAnimationController.this.mRemoteAnimationAdapter.getDuration();
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return android.os.SystemClock.uptimeMillis() + com.android.server.wm.RemoteAnimationController.this.mRemoteAnimationAdapter.getStatusBarTransitionDelay();
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.print("container=");
            pw.println(this.mRecord.mWindowContainer);
            if (this.mRecord.mTarget != null) {
                pw.print(prefix);
                pw.println("Target:");
                this.mRecord.mTarget.dump(pw, prefix + "  ");
            } else {
                pw.print(prefix);
                pw.println("Target: null");
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
            long token = proto.start(1146756268034L);
            if (this.mRecord.mTarget != null) {
                this.mRecord.mTarget.dumpDebug(proto, 1146756268033L);
            }
            proto.end(token);
        }
    }
}
