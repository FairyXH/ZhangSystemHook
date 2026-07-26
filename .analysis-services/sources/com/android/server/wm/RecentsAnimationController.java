package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class RecentsAnimationController implements android.os.IBinder.DeathRecipient {
    private static final long FAILSAFE_DELAY = 1000;
    private static final int MODE_UNKNOWN = -1;
    public static final int REORDER_KEEP_IN_PLACE = 0;
    public static final int REORDER_MOVE_TO_ORIGINAL_POSITION = 2;
    public static final int REORDER_MOVE_TO_TOP = 1;
    private static final java.lang.String TAG = com.android.server.wm.RecentsAnimationController.class.getSimpleName();
    private final com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks mCallbacks;
    private boolean mCancelDeferredWithScreenshot;
    private boolean mCancelOnNextTransitionStart;
    private boolean mCanceled;
    private com.android.server.wm.DisplayContent mDisplayContent;
    private final int mDisplayId;
    private boolean mInputConsumerEnabled;
    boolean mIsAddingTaskToTargets;
    private boolean mLinkedToDeathOfRunner;
    private com.android.server.wm.ActivityRecord mNavBarAttachedApp;
    private boolean mNavigationBarAttachedToApp;
    private boolean mRequestDeferCancelUntilNextTransition;
    private android.view.IRecentsAnimationRunner mRunner;
    private final com.android.server.wm.WindowManagerService mService;
    private com.android.server.wm.ActivityRecord mTargetActivityRecord;
    private int mTargetActivityType;
    private final java.util.ArrayList<com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter> mPendingAnimations = new java.util.ArrayList<>();
    private final android.util.IntArray mPendingNewTaskTargets = new android.util.IntArray(0);
    private final java.util.ArrayList<com.android.server.wm.WallpaperAnimationAdapter> mPendingWallpaperAnimations = new java.util.ArrayList<>();
    private boolean mWillFinishToHome = false;
    private final java.lang.Runnable mFailsafeRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.onFailsafe();
        }
    };
    private boolean mPendingStart = true;
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();
    private int mPendingCancelWithScreenshotReorderMode = 2;
    private final java.util.ArrayList<android.view.RemoteAnimationTarget> mPendingTaskAppears = new java.util.ArrayList<>();
    final com.android.server.wm.WindowManagerInternal.AppTransitionListener mAppTransitionListener = new com.android.server.wm.WindowManagerInternal.AppTransitionListener() { // from class: com.android.server.wm.RecentsAnimationController.1
        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public int onAppTransitionStartingLocked(long statusBarAnimationStartTime, long statusBarAnimationDuration) {
            continueDeferredCancel();
            return 0;
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
            continueDeferredCancel();
        }

        private void continueDeferredCancel() {
            com.android.server.wm.RecentsAnimationController.this.mDisplayContent.mAppTransition.unregisterListener(this);
            if (!com.android.server.wm.RecentsAnimationController.this.mCanceled && com.android.server.wm.RecentsAnimationController.this.mCancelOnNextTransitionStart) {
                com.android.server.wm.RecentsAnimationController.this.mCancelOnNextTransitionStart = false;
                com.android.server.wm.RecentsAnimationController.this.cancelAnimationWithScreenshot(com.android.server.wm.RecentsAnimationController.this.mCancelDeferredWithScreenshot);
            }
        }
    };
    private final android.view.IRecentsAnimationController mController = new android.view.IRecentsAnimationController.Stub() { // from class: com.android.server.wm.RecentsAnimationController.2
        public android.window.TaskSnapshot screenshotTask(int taskId) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                long protoLogParam0 = taskId;
                boolean protoLogParam1 = com.android.server.wm.RecentsAnimationController.this.mCanceled;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 6530904107141905844L, 13, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1));
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    if (com.android.server.wm.RecentsAnimationController.this.mCanceled) {
                        return null;
                    }
                    for (int i = com.android.server.wm.RecentsAnimationController.this.mPendingAnimations.size() - 1; i >= 0; i--) {
                        com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter adapter = (com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter) com.android.server.wm.RecentsAnimationController.this.mPendingAnimations.get(i);
                        com.android.server.wm.Task task = adapter.mTask;
                        if (task.mTaskId == taskId) {
                            com.android.server.wm.TaskSnapshotController snapshotController = com.android.server.wm.RecentsAnimationController.this.mService.mTaskSnapshotController;
                            android.util.ArraySet<com.android.server.wm.Task> tasks = com.google.android.collect.Sets.newArraySet(new com.android.server.wm.Task[]{task});
                            snapshotController.snapshotTasks(tasks);
                            snapshotController.addSkipClosingAppSnapshotTasks(tasks);
                            return snapshotController.getSnapshot(taskId, task.mUserId, false, false);
                        }
                    }
                    return null;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setFinishTaskTransaction(int taskId, android.window.PictureInPictureSurfaceTransaction finishTransaction, android.view.SurfaceControl overlay) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                long protoLogParam0 = taskId;
                java.lang.String protoLogParam1 = java.lang.String.valueOf(finishTransaction);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -3286551982713129633L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    int i = com.android.server.wm.RecentsAnimationController.this.mPendingAnimations.size() - 1;
                    while (true) {
                        if (i < 0) {
                            break;
                        }
                        com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter taskAdapter = (com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter) com.android.server.wm.RecentsAnimationController.this.mPendingAnimations.get(i);
                        if (taskAdapter.mTask.mTaskId != taskId) {
                            i--;
                        } else {
                            taskAdapter.mFinishTransaction = finishTransaction;
                            taskAdapter.mFinishOverlay = overlay;
                            break;
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void finishZoom(boolean moveHomeToTop, boolean sendUserLeaveHint, int taskId, int type, android.graphics.Rect rect, int orientation, android.os.Bundle bOptions) throws java.lang.Throwable {
            int i;
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!((com.android.server.wm.IZoomWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IZoomWindowManagerExt.class).create()).recentAnimationFinished(taskId, type, rect, orientation, bOptions, com.android.server.wm.RecentsAnimationController.this.mController, moveHomeToTop, sendUserLeaveHint)) {
                    com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks recentsAnimationCallbacks = com.android.server.wm.RecentsAnimationController.this.mCallbacks;
                    if (moveHomeToTop) {
                        i = 1;
                    } else {
                        i = 2;
                    }
                    try {
                        recentsAnimationCallbacks.onAnimationFinished(i, sendUserLeaveHint);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                }
                android.os.Binder.restoreCallingIdentity(token);
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }

        public void enterZoomFromRecent(android.view.SurfaceControl zoomRootLeash, android.view.SurfaceControl targetLeash, android.graphics.Rect windowCrop, android.graphics.Rect endRect, android.os.Bundle options) {
            int i;
            long token = android.os.Binder.clearCallingIdentity();
            boolean moveHomeToTop = true;
            boolean sendUserLeaveHint = true;
            if (options != null) {
                try {
                    moveHomeToTop = options.getBoolean("moveHomeToTop");
                    sendUserLeaveHint = options.getBoolean("sendUserLeaveHint");
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
            com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks recentsAnimationCallbacks = com.android.server.wm.RecentsAnimationController.this.mCallbacks;
            if (moveHomeToTop) {
                i = 1;
            } else {
                i = 2;
            }
            recentsAnimationCallbacks.onAnimationFinished(i, sendUserLeaveHint);
        }

        public void finishPutt(int type, int taskId, android.graphics.Rect rect, int orientation, android.os.Bundle bOptions) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.wm.RecentsAnimationController.this.mRACext.finishPutt(type, taskId, rect, orientation, bOptions);
                com.android.server.wm.RecentsAnimationController.this.mCallbacks.onAnimationFinished(1, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void finish(boolean moveHomeToTop, boolean sendUserLeaveHint, com.android.internal.os.IResultReceiver finishCb) {
            int i;
            android.util.Slog.d(com.android.server.wm.RecentsAnimationController.TAG, "recent animation finish(" + moveHomeToTop + "): mCanceled=" + com.android.server.wm.RecentsAnimationController.this.mCanceled);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks recentsAnimationCallbacks = com.android.server.wm.RecentsAnimationController.this.mCallbacks;
                if (moveHomeToTop) {
                    i = 1;
                } else {
                    i = 2;
                }
                recentsAnimationCallbacks.onAnimationFinished(i, sendUserLeaveHint);
                if (finishCb != null) {
                    try {
                        finishCb.send(0, new android.os.Bundle());
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.wm.RecentsAnimationController.TAG, "Failed to report animation finished", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void finishExtended(boolean moveHomeToTop, boolean sendUserLeaveHint, long seqId) {
        }

        public void setAnimationTargetsBehindSystemBars(boolean behindSystemBars) throws android.os.RemoteException {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    for (int i = com.android.server.wm.RecentsAnimationController.this.mPendingAnimations.size() - 1; i >= 0; i--) {
                        com.android.server.wm.Task task = ((com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter) com.android.server.wm.RecentsAnimationController.this.mPendingAnimations.get(i)).mTask;
                        if (task.getActivityType() != com.android.server.wm.RecentsAnimationController.this.mTargetActivityType) {
                            task.setCanAffectSystemUiFlags(behindSystemBars);
                        }
                    }
                    com.android.server.inputmethod.InputMethodManagerInternal.get().maybeFinishStylusHandwriting();
                    com.android.server.wm.RecentsAnimationController.this.mService.mWindowPlacerLocked.requestTraversal();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setInputConsumerEnabled(boolean enabled) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(enabled);
                boolean protoLogParam1 = com.android.server.wm.RecentsAnimationController.this.mCanceled;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 6879496555046975661L, 12, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1));
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    if (com.android.server.wm.RecentsAnimationController.this.mCanceled) {
                        return;
                    }
                    com.android.server.wm.RecentsAnimationController.this.mInputConsumerEnabled = enabled;
                    com.android.server.wm.InputMonitor inputMonitor = com.android.server.wm.RecentsAnimationController.this.mDisplayContent.getInputMonitor();
                    inputMonitor.updateInputWindowsLw(true);
                    com.android.server.wm.RecentsAnimationController.this.mService.scheduleAnimationLocked();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setDeferCancelUntilNextTransition(boolean defer, boolean screenshot) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RecentsAnimationController.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.RecentsAnimationController.this.setDeferredCancel(defer, screenshot);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        public void cleanupScreenshot() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.wm.RecentsAnimationController.this.continueDeferredCancelAnimation();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setWillFinishToHome(boolean willFinishToHome) {
            synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                com.android.server.wm.RecentsAnimationController.this.setWillFinishToHome(willFinishToHome);
            }
        }

        public boolean removeTask(int taskId) {
            boolean zRemoveTaskInternal;
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    zRemoveTaskInternal = com.android.server.wm.RecentsAnimationController.this.removeTaskInternal(taskId);
                }
                return zRemoveTaskInternal;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void detachNavigationBarFromApp(boolean moveHomeToTop) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    com.android.server.wm.RecentsAnimationController.this.restoreNavigationBarFromApp(moveHomeToTop || com.android.server.wm.RecentsAnimationController.this.mIsAddingTaskToTargets);
                    com.android.server.wm.RecentsAnimationController.this.mService.mWindowPlacerLocked.requestTraversal();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void animateNavigationBarToApp(long duration) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.wm.RecentsAnimationController.this.mService.getWindowManagerLock()) {
                    com.android.server.wm.RecentsAnimationController.this.animateNavigationBarForAppLaunch(duration);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void handOffAnimation(android.view.RemoteAnimationTarget[] targets, android.window.WindowAnimationState[] states) {
        }
    };
    private com.android.server.wm.IRecentsAnimationControllerExt mRACext = (com.android.server.wm.IRecentsAnimationControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRecentsAnimationControllerExt.class).base(this).create();
    final com.android.server.statusbar.StatusBarManagerInternal mStatusBar = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);

    public interface RecentsAnimationCallbacks {
        void onAnimationFinished(int i, boolean z);
    }

    public @interface ReorderMode {
    }

    RecentsAnimationController(com.android.server.wm.WindowManagerService service, android.view.IRecentsAnimationRunner remoteAnimationRunner, com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks callbacks, int displayId) {
        this.mService = service;
        this.mRunner = remoteAnimationRunner;
        this.mCallbacks = callbacks;
        this.mDisplayId = displayId;
        this.mDisplayContent = service.mRoot.getDisplayContent(displayId);
    }

    public void initialize(int targetActivityType, android.util.SparseBooleanArray recentTaskIds, com.android.server.wm.ActivityRecord targetActivity) {
        this.mTargetActivityType = targetActivityType;
        this.mDisplayContent.mAppTransition.registerListenerLocked(this.mAppTransitionListener);
        final java.util.ArrayList<com.android.server.wm.Task> visibleTasks = this.mDisplayContent.getDefaultTaskDisplayArea().getVisibleTasks();
        com.android.server.wm.Task targetRootTask = this.mDisplayContent.getDefaultTaskDisplayArea().getRootTask(0, targetActivityType);
        if (targetRootTask != null) {
            targetRootTask.forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.RecentsAnimationController.lambda$initialize$0(visibleTasks, (com.android.server.wm.Task) obj);
                }
            }, true);
        }
        int taskCount = visibleTasks.size();
        for (int i = taskCount - 1; i >= 0; i--) {
            final com.android.server.wm.Task task = visibleTasks.get(i);
            if (!skipAnimation(task) && !this.mRACext.isZoomWindowMode(task.getWindowingMode()) && !task.getWrapper().getExtImpl().getLaunchedFromMultiSearch() && !task.getWrapper().getExtImpl().isFlexibleWindowScenario(3)) {
                addAnimation(task, !recentTaskIds.get(task.mTaskId), false, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda2
                    @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                    public final void onAnimationFinished(int i2, com.android.server.wm.AnimationAdapter animationAdapter) {
                        task.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda3
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.wm.WindowState) obj).onAnimationFinished(i2, animationAdapter);
                            }
                        }, true);
                    }
                });
            }
        }
        if (this.mPendingAnimations.isEmpty()) {
            cancelAnimation(2, "initialize-noVisibleTasks");
            return;
        }
        try {
            linkToDeathOfRunner();
            attachNavigationBarToApp();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(targetActivity.getName());
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -5305978958548091997L, 0, null, protoLogParam0);
            }
            this.mTargetActivityRecord = targetActivity;
            if (targetActivity.windowsCanBeWallpaperTarget()) {
                this.mDisplayContent.pendingLayoutChanges |= 4;
                this.mDisplayContent.setLayoutNeeded();
            }
            this.mService.mWindowPlacerLocked.performSurfacePlacement();
            this.mDisplayContent.mFixedRotationTransitionListener.onStartRecentsAnimation(targetActivity);
            if (this.mStatusBar != null) {
                this.mStatusBar.onRecentsAnimationStateChanged(true);
            }
        } catch (android.os.RemoteException e) {
            cancelAnimation(2, "initialize-failedToLinkToDeath");
        }
    }

    static /* synthetic */ void lambda$initialize$0(java.util.ArrayList visibleTasks, com.android.server.wm.Task t) {
        if (!visibleTasks.contains(t)) {
            visibleTasks.add(t);
        }
    }

    protected boolean isInterestingForAllDrawn(com.android.server.wm.WindowState window) {
        return (isTargetApp(window.getActivityRecord()) && window.getWindowType() != 1 && window.getAttrs().alpha == 0.0f) ? false : true;
    }

    private boolean skipAnimation(com.android.server.wm.Task task) {
        android.app.WindowConfiguration config = task.getWindowConfiguration();
        return task.isAlwaysOnTop() || config.tasksAreFloating();
    }

    com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter addAnimation(com.android.server.wm.Task task, boolean isRecentTaskInvisible) {
        return addAnimation(task, isRecentTaskInvisible, false, null);
    }

    com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter addAnimation(com.android.server.wm.Task task, boolean isRecentTaskInvisible, boolean hidden, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishedCallback) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(task.getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -3801497203749932106L, 0, null, protoLogParam0);
        }
        com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter taskAdapter = new com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter(task, isRecentTaskInvisible);
        task.startAnimation(task.getPendingTransaction(), taskAdapter, hidden, 8, finishedCallback);
        task.commitPendingTransaction();
        this.mPendingAnimations.add(taskAdapter);
        return taskAdapter;
    }

    void removeAnimation(com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter taskAdapter) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            long protoLogParam0 = taskAdapter.mTask.mTaskId;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 3721473589747203697L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        taskAdapter.onRemove();
        this.mPendingAnimations.remove(taskAdapter);
    }

    void removeWallpaperAnimation(com.android.server.wm.WallpaperAnimationAdapter wallpaperAdapter) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 5156407755139006078L, 0, null, null);
        }
        wallpaperAdapter.getLeashFinishedCallback().onAnimationFinished(wallpaperAdapter.getLastAnimationType(), wallpaperAdapter);
        this.mPendingWallpaperAnimations.remove(wallpaperAdapter);
    }

    void startAnimation() {
        android.view.RemoteAnimationTarget[] appTargets;
        android.graphics.Rect contentInsets;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            boolean protoLogParam0 = this.mPendingStart;
            boolean protoLogParam1 = this.mCanceled;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -1997836523186474317L, 15, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1));
        }
        boolean protoLogParam02 = this.mPendingStart;
        if (!protoLogParam02 || this.mCanceled) {
            return;
        }
        try {
            appTargets = createAppAnimations();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to start recents animation", e);
        }
        if (appTargets.length == 0) {
            cancelAnimation(2, "startAnimation-noAppWindows");
            return;
        }
        android.view.RemoteAnimationTarget[] wallpaperTargets = createWallpaperAnimations();
        this.mPendingStart = false;
        com.android.server.wm.WindowState targetAppMainWindow = getTargetAppMainWindow();
        if (targetAppMainWindow == null) {
            this.mService.getStableInsets(this.mDisplayId, this.mTmpRect);
            contentInsets = this.mTmpRect;
        } else {
            contentInsets = targetAppMainWindow.getInsetsStateWithVisibilityOverride().calculateInsets(this.mTargetActivityRecord.getBounds(), android.view.WindowInsets.Type.systemBars(), false).toRect();
        }
        this.mPendingAnimations.forEach(new java.util.function.Consumer() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$startAnimation$3((com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter) obj);
            }
        });
        this.mRunner.onAnimationStart(this.mController, appTargets, wallpaperTargets, contentInsets, (android.graphics.Rect) null, new android.os.Bundle());
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(this.mPendingAnimations.stream().map(new java.util.function.Function() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Integer.valueOf(((com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter) obj).mTask.mTaskId);
                }
            }).collect(java.util.stream.Collectors.toList()));
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -7532294363367395195L, 0, null, protoLogParam03);
        }
        if (this.mTargetActivityRecord != null) {
            android.util.ArrayMap<com.android.server.wm.WindowContainer, java.lang.Integer> reasons = new android.util.ArrayMap<>(1);
            reasons.put(this.mTargetActivityRecord, 5);
            this.mService.mAtmService.mTaskSupervisor.getActivityMetricsLogger().notifyTransitionStarting(reasons);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$3(com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter anim) {
        this.mService.mAtmService.getWrapper().getFlexibleExtImpl().onRecentsAnimationExecuting(anim.mTask, true, -1);
    }

    boolean isNavigationBarAttachedToApp() {
        return this.mNavigationBarAttachedToApp;
    }

    com.android.server.wm.WindowState getNavigationBarWindow() {
        return this.mDisplayContent.getDisplayPolicy().getNavigationBar();
    }

    private void attachNavigationBarToApp() {
        if (!this.mDisplayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() || this.mDisplayContent.getAsyncRotationController() != null) {
            return;
        }
        int i = this.mPendingAnimations.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter adapter = this.mPendingAnimations.get(i);
            com.android.server.wm.Task task = adapter.mTask;
            if (task.isActivityTypeHomeOrRecents()) {
                i--;
            } else {
                this.mNavBarAttachedApp = task.getTopVisibleActivity();
                break;
            }
        }
        com.android.server.wm.WindowState navWindow = getNavigationBarWindow();
        if (this.mNavBarAttachedApp == null || navWindow == null || navWindow.mToken == null) {
            return;
        }
        this.mNavigationBarAttachedToApp = true;
        navWindow.mToken.cancelAnimation();
        android.view.SurfaceControl.Transaction t = navWindow.mToken.getPendingTransaction();
        android.view.SurfaceControl navSurfaceControl = navWindow.mToken.getSurfaceControl();
        navWindow.setSurfaceTranslationY(-this.mNavBarAttachedApp.getBounds().top);
        t.reparent(navSurfaceControl, this.mNavBarAttachedApp.getSurfaceControl());
        t.show(navSurfaceControl);
        com.android.server.wm.WindowContainer imeContainer = this.mDisplayContent.getImeContainer();
        if (imeContainer.isVisible()) {
            t.setRelativeLayer(navSurfaceControl, imeContainer.getSurfaceControl(), 1);
        } else {
            t.setLayer(navSurfaceControl, Integer.MAX_VALUE);
        }
        if (this.mStatusBar != null) {
            this.mStatusBar.setNavigationBarLumaSamplingEnabled(this.mDisplayId, false);
        }
    }

    void restoreNavigationBarFromApp(boolean animate) {
        if (!this.mNavigationBarAttachedToApp) {
            return;
        }
        this.mNavigationBarAttachedToApp = false;
        if (this.mStatusBar != null) {
            this.mStatusBar.setNavigationBarLumaSamplingEnabled(this.mDisplayId, true);
        }
        com.android.server.wm.WindowState navWindow = getNavigationBarWindow();
        if (navWindow == null) {
            return;
        }
        navWindow.setSurfaceTranslationY(0);
        com.android.server.wm.WindowToken navToken = navWindow.mToken;
        if (navToken == null) {
            return;
        }
        android.view.SurfaceControl.Transaction t = this.mDisplayContent.getPendingTransaction();
        com.android.server.wm.WindowContainer parent = navToken.getParent();
        t.setLayer(navToken.getSurfaceControl(), navToken.getLastLayer());
        if (animate) {
            com.android.server.wm.NavBarFadeAnimationController controller = new com.android.server.wm.NavBarFadeAnimationController(this.mDisplayContent);
            controller.fadeWindowToken(true);
        } else {
            t.reparent(navToken.getSurfaceControl(), parent.getSurfaceControl());
        }
    }

    void animateNavigationBarForAppLaunch(long duration) {
        if (!this.mDisplayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() || this.mDisplayContent.getAsyncRotationController() != null || this.mNavigationBarAttachedToApp || this.mNavBarAttachedApp == null) {
            return;
        }
        com.android.server.wm.NavBarFadeAnimationController controller = new com.android.server.wm.NavBarFadeAnimationController(this.mDisplayContent);
        controller.fadeOutAndInSequentially(duration, null, this.mNavBarAttachedApp.getSurfaceControl());
    }

    void addTaskToTargets(com.android.server.wm.Task task, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishedCallback) {
        if (this.mRunner != null) {
            this.mIsAddingTaskToTargets = task != null;
            this.mNavBarAttachedApp = task == null ? null : task.getTopVisibleActivity();
            if (isAnimatingTask(task) || skipAnimation(task)) {
                return;
            }
            collectTaskRemoteAnimations(task, 0, finishedCallback);
        }
    }

    void sendTasksAppeared() {
        if (this.mPendingTaskAppears.isEmpty() || this.mRunner == null) {
            return;
        }
        try {
            android.view.RemoteAnimationTarget[] targets = (android.view.RemoteAnimationTarget[]) this.mPendingTaskAppears.toArray(new android.view.RemoteAnimationTarget[0]);
            this.mRunner.onTasksAppeared(targets);
            this.mPendingTaskAppears.clear();
            this.mRACext.sendTasksAppeared(targets);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to report task appeared", e);
        }
    }

    private void collectTaskRemoteAnimations(com.android.server.wm.Task task, final int mode, final com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishedCallback) {
        final android.util.SparseBooleanArray recentTaskIds = this.mService.mAtmService.getRecentTasks().getRecentTaskIds();
        task.forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$collectTaskRemoteAnimations$5(recentTaskIds, finishedCallback, mode, (com.android.server.wm.Task) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectTaskRemoteAnimations$5(android.util.SparseBooleanArray recentTaskIds, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishedCallback, int mode, com.android.server.wm.Task leafTask) {
        if (!leafTask.shouldBeVisible(null)) {
            return;
        }
        int taskId = leafTask.mTaskId;
        com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter adapter = addAnimation(leafTask, !recentTaskIds.get(taskId), true, finishedCallback);
        this.mPendingNewTaskTargets.add(taskId);
        android.view.RemoteAnimationTarget target = adapter.createRemoteAnimationTarget(taskId, mode);
        if (target != null) {
            this.mPendingTaskAppears.add(target);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(target);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -1336603089105439710L, 0, null, protoLogParam0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeTaskInternal(int taskId) {
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter target = this.mPendingAnimations.get(i);
            if (target.mTask.mTaskId == taskId && target.mTask.isOnTop()) {
                removeAnimation(target);
                int taskIndex = this.mPendingNewTaskTargets.indexOf(taskId);
                if (taskIndex != -1) {
                    this.mPendingNewTaskTargets.remove(taskIndex);
                }
                return true;
            }
        }
        return false;
    }

    private android.view.RemoteAnimationTarget[] createAppAnimations() {
        java.util.ArrayList<android.view.RemoteAnimationTarget> targets = new java.util.ArrayList<>();
        int i = this.mPendingAnimations.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            if (i >= this.mPendingAnimations.size()) {
                android.util.Slog.d(TAG, "pendingAnimations exception break");
                break;
            }
            com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter taskAdapter = this.mPendingAnimations.get(i);
            android.view.RemoteAnimationTarget target = taskAdapter.createRemoteAnimationTarget(-1, -1);
            if (target != null) {
                targets.add(target);
            } else {
                removeAnimation(taskAdapter);
            }
            i--;
        }
        int i2 = targets.size();
        return (android.view.RemoteAnimationTarget[]) targets.toArray(new android.view.RemoteAnimationTarget[i2]);
    }

    private android.view.RemoteAnimationTarget[] createWallpaperAnimations() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 2547528895718568379L, 0, null, null);
        }
        return com.android.server.wm.WallpaperAnimationAdapter.startWallpaperAnimations(this.mDisplayContent, 0L, 0L, new java.util.function.Consumer() { // from class: com.android.server.wm.RecentsAnimationController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$createWallpaperAnimations$6((com.android.server.wm.WallpaperAnimationAdapter) obj);
            }
        }, this.mPendingWallpaperAnimations);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createWallpaperAnimations$6(com.android.server.wm.WallpaperAnimationAdapter adapter) {
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

    void forceCancelAnimation(int reorderMode, java.lang.String reason) {
        if (!this.mCanceled) {
            cancelAnimation(reorderMode, reason);
        } else {
            continueDeferredCancelAnimation();
        }
    }

    void cancelAnimation(int reorderMode, java.lang.String reason) {
        cancelAnimation(reorderMode, false, reason);
    }

    void cancelAnimationWithScreenshot(boolean screenshot) {
        cancelAnimation(0, screenshot, "rootTaskOrderChanged");
    }

    public void cancelAnimationForHomeStart() {
        int reorderMode;
        if (this.mTargetActivityType == 2 && this.mWillFinishToHome) {
            reorderMode = 1;
        } else {
            reorderMode = 0;
        }
        cancelAnimation(reorderMode, true, "cancelAnimationForHomeStart");
    }

    public void cancelAnimationForDisplayChange() {
        if (this.mDisplayContent.hasTopFixedRotationLaunchingApp()) {
            android.util.Slog.d(TAG, "for fixed rotation animation ,we dont cancel recent animation earlier");
        } else {
            cancelAnimation(this.mWillFinishToHome ? 1 : 2, true, "cancelAnimationForDisplayChange");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0091 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void cancelAnimation(int r7, boolean r8, java.lang.String r9) {
        /*
            r6 = this;
            java.lang.String r0 = com.android.server.wm.RecentsAnimationController.TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "cancelAnimation(): reason="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r9)
            java.lang.String r2 = ", Callers="
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 5
            java.lang.String r2 = android.os.Debug.getCallers(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Slog.d(r0, r1)
            com.android.server.wm.WindowManagerService r0 = r6.mService
            java.lang.Object r0 = r0.getWindowManagerLock()
            monitor-enter(r0)
            boolean r1 = r6.mCanceled     // Catch: java.lang.Throwable -> La8
            if (r1 == 0) goto L34
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            return
        L34:
            com.android.server.wm.WindowManagerService r1 = r6.mService     // Catch: java.lang.Throwable -> La8
            com.android.server.wm.WindowManagerService$H r1 = r1.mH     // Catch: java.lang.Throwable -> La8
            java.lang.Runnable r2 = r6.mFailsafeRunnable     // Catch: java.lang.Throwable -> La8
            r1.removeCallbacks(r2)     // Catch: java.lang.Throwable -> La8
            r1 = 1
            r6.mCanceled = r1     // Catch: java.lang.Throwable -> La8
            if (r8 == 0) goto L91
            java.util.ArrayList<com.android.server.wm.RecentsAnimationController$TaskAnimationAdapter> r2 = r6.mPendingAnimations     // Catch: java.lang.Throwable -> La8
            boolean r2 = r2.isEmpty()     // Catch: java.lang.Throwable -> La8
            if (r2 != 0) goto L91
            android.util.ArrayMap r2 = r6.screenshotRecentTasks()     // Catch: java.lang.Throwable -> La8
            r6.mPendingCancelWithScreenshotReorderMode = r7     // Catch: java.lang.Throwable -> La8
            boolean r3 = r2.isEmpty()     // Catch: java.lang.Throwable -> La8
            if (r3 != 0) goto L91
            int r3 = r2.size()     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            int[] r3 = new int[r3]     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            int r4 = r2.size()     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            android.window.TaskSnapshot[] r4 = new android.window.TaskSnapshot[r4]     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            int r5 = r2.size()     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            int r5 = r5 - r1
        L67:
            if (r5 < 0) goto L7e
            java.lang.Object r1 = r2.keyAt(r5)     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            com.android.server.wm.Task r1 = (com.android.server.wm.Task) r1     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            int r1 = r1.mTaskId     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            r3[r5] = r1     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            java.lang.Object r1 = r2.valueAt(r5)     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            android.window.TaskSnapshot r1 = (android.window.TaskSnapshot) r1     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            r4[r5] = r1     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            int r5 = r5 + (-1)
            goto L67
        L7e:
            android.view.IRecentsAnimationRunner r1 = r6.mRunner     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            r1.onAnimationCanceled(r3, r4)     // Catch: android.os.RemoteException -> L84 java.lang.Throwable -> La8
            goto L8c
        L84:
            r1 = move-exception
            java.lang.String r3 = com.android.server.wm.RecentsAnimationController.TAG     // Catch: java.lang.Throwable -> La8
            java.lang.String r4 = "Failed to cancel recents animation"
            android.util.Slog.e(r3, r4, r1)     // Catch: java.lang.Throwable -> La8
        L8c:
            r6.scheduleFailsafe()     // Catch: java.lang.Throwable -> La8
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            return
        L91:
            android.view.IRecentsAnimationRunner r1 = r6.mRunner     // Catch: android.os.RemoteException -> L98 java.lang.Throwable -> La8
            r2 = 0
            r1.onAnimationCanceled(r2, r2)     // Catch: android.os.RemoteException -> L98 java.lang.Throwable -> La8
            goto La0
        L98:
            r1 = move-exception
            java.lang.String r2 = com.android.server.wm.RecentsAnimationController.TAG     // Catch: java.lang.Throwable -> La8
            java.lang.String r3 = "Failed to cancel recents animation"
            android.util.Slog.e(r2, r3, r1)     // Catch: java.lang.Throwable -> La8
        La0:
            com.android.server.wm.RecentsAnimationController$RecentsAnimationCallbacks r1 = r6.mCallbacks     // Catch: java.lang.Throwable -> La8
            r2 = 0
            r1.onAnimationFinished(r7, r2)     // Catch: java.lang.Throwable -> La8
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            return
        La8:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RecentsAnimationController.cancelAnimation(int, boolean, java.lang.String):void");
    }

    void continueDeferredCancelAnimation() {
        this.mCallbacks.onAnimationFinished(this.mPendingCancelWithScreenshotReorderMode, false);
    }

    void setWillFinishToHome(boolean willFinishToHome) {
        this.mWillFinishToHome = willFinishToHome;
    }

    void setCancelOnNextTransitionStart() {
        this.mCancelOnNextTransitionStart = true;
    }

    void setDeferredCancel(boolean defer, boolean screenshot) {
        this.mRequestDeferCancelUntilNextTransition = defer;
        this.mCancelDeferredWithScreenshot = screenshot;
    }

    boolean shouldDeferCancelUntilNextTransition() {
        return this.mRequestDeferCancelUntilNextTransition;
    }

    boolean shouldDeferCancelWithScreenshot() {
        return this.mRequestDeferCancelUntilNextTransition && this.mCancelDeferredWithScreenshot;
    }

    private android.util.ArrayMap<com.android.server.wm.Task, android.window.TaskSnapshot> screenshotRecentTasks() {
        com.android.server.wm.TaskSnapshotController snapshotController = this.mService.mTaskSnapshotController;
        android.util.ArrayMap<com.android.server.wm.Task, android.window.TaskSnapshot> snapshotMap = new android.util.ArrayMap<>();
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter adapter = this.mPendingAnimations.get(i);
            com.android.server.wm.Task task = adapter.mTask;
            if (!task.isActivityTypeHome()) {
                snapshotController.recordSnapshot(task);
                android.window.TaskSnapshot snapshot = snapshotController.getSnapshot(task.mTaskId, task.mUserId, false, false);
                if (snapshot != null) {
                    snapshotMap.put(task, snapshot);
                    adapter.setSnapshotOverlay(snapshot);
                }
            }
        }
        snapshotController.addSkipClosingAppSnapshotTasks(snapshotMap.keySet());
        return snapshotMap;
    }

    void cleanupAnimation(int reorderMode) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            long protoLogParam0 = this.mPendingAnimations.size();
            long protoLogParam1 = reorderMode;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 622027757443954945L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
        }
        if (reorderMode != 2 && this.mTargetActivityRecord != this.mDisplayContent.topRunningActivity()) {
            this.mDisplayContent.mFixedRotationTransitionListener.notifyRecentsWillBeTop();
        }
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            com.android.server.wm.RecentsAnimationController.TaskAnimationAdapter taskAdapter = this.mPendingAnimations.get(i);
            if (reorderMode == 1 || reorderMode == 0) {
                this.mRACext.markTaskNoAnimation(taskAdapter.mTask.mTaskId);
                this.mRACext.hideDisplaySwitchNotification(taskAdapter.mTask, reorderMode == 1);
                taskAdapter.mTask.dontAnimateDimExit();
            }
            removeAnimation(taskAdapter);
            taskAdapter.onCleanup();
        }
        this.mPendingNewTaskTargets.clear();
        this.mPendingTaskAppears.clear();
        for (int i2 = this.mPendingWallpaperAnimations.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.WallpaperAnimationAdapter wallpaperAdapter = this.mPendingWallpaperAnimations.get(i2);
            removeWallpaperAnimation(wallpaperAdapter);
        }
        restoreNavigationBarFromApp(reorderMode == 1 || this.mIsAddingTaskToTargets);
        this.mService.mH.removeCallbacks(this.mFailsafeRunnable);
        this.mDisplayContent.mAppTransition.unregisterListener(this.mAppTransitionListener);
        unlinkToDeathOfRunner();
        this.mRunner = null;
        this.mCanceled = true;
        if (reorderMode == 2 && !this.mIsAddingTaskToTargets) {
            com.android.server.inputmethod.InputMethodManagerInternal.get().updateImeWindowStatus(false, this.mDisplayId);
        }
        com.android.server.wm.InputMonitor inputMonitor = this.mDisplayContent.getInputMonitor();
        inputMonitor.updateInputWindowsLw(true);
        if (this.mTargetActivityRecord != null && (reorderMode == 1 || reorderMode == 0)) {
            this.mDisplayContent.mAppTransition.notifyAppTransitionFinishedLocked(this.mTargetActivityRecord.token);
        }
        this.mDisplayContent.mFixedRotationTransitionListener.onFinishRecentsAnimation();
        if (this.mStatusBar != null) {
            this.mStatusBar.onRecentsAnimationStateChanged(false);
        }
    }

    void scheduleFailsafe() {
        this.mService.mH.postDelayed(this.mFailsafeRunnable, 1000L);
    }

    void onFailsafe() {
        forceCancelAnimation(this.mWillFinishToHome ? 1 : 2, "onFailsafe");
    }

    private void linkToDeathOfRunner() throws android.os.RemoteException {
        if (!this.mLinkedToDeathOfRunner) {
            this.mRunner.asBinder().linkToDeath(this, 0);
            this.mLinkedToDeathOfRunner = true;
        }
    }

    private void unlinkToDeathOfRunner() {
        if (this.mLinkedToDeathOfRunner) {
            this.mRunner.asBinder().unlinkToDeath(this, 0);
            this.mLinkedToDeathOfRunner = false;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        forceCancelAnimation(2, "binderDied");
        synchronized (this.mService.getWindowManagerLock()) {
            com.android.server.wm.InputMonitor inputMonitor = this.mDisplayContent.getInputMonitor();
            com.android.server.wm.InputConsumerImpl consumer = inputMonitor.getInputConsumer("recents_animation_input_consumer");
            if (consumer != null) {
                inputMonitor.destroyInputConsumer(consumer.mToken);
            }
        }
    }

    void checkAnimationReady(com.android.server.wm.WallpaperController wallpaperController) {
        if (this.mPendingStart) {
            com.android.server.wm.WallpaperController wallpaperController2 = this.mRACext.adjustWallpaperController(wallpaperController, this.mDisplayContent);
            boolean wallpaperReady = !isTargetOverWallpaper() || (wallpaperController2.getWallpaperTarget() != null && wallpaperController2.wallpaperTransitionReady());
            if (wallpaperReady) {
                this.mService.getRecentsAnimationController().startAnimation();
            }
        }
    }

    boolean isWallpaperVisible(com.android.server.wm.WindowState w) {
        return w != null && w.mAttrs.type == 1 && ((w.mActivityRecord != null && this.mTargetActivityRecord == w.mActivityRecord) || isAnimatingTask(w.getTask())) && isTargetOverWallpaper() && w.isOnScreen();
    }

    boolean shouldApplyInputConsumer(com.android.server.wm.ActivityRecord activity) {
        return this.mInputConsumerEnabled && activity != null && !isTargetApp(activity) && isAnimatingApp(activity);
    }

    boolean updateInputConsumerForApp(android.view.InputWindowHandle inputWindowHandle) {
        com.android.server.wm.WindowState targetAppMainWindow = getTargetAppMainWindow();
        if (targetAppMainWindow != null) {
            targetAppMainWindow.getBounds(this.mTmpRect);
            this.mRACext.adjustTouchableRegion(targetAppMainWindow, this.mTmpRect);
            inputWindowHandle.touchableRegion.set(this.mTmpRect);
            return true;
        }
        return false;
    }

    boolean isTargetApp(com.android.server.wm.ActivityRecord activity) {
        return this.mTargetActivityRecord != null && activity == this.mTargetActivityRecord;
    }

    private boolean isTargetOverWallpaper() {
        if (this.mTargetActivityRecord == null) {
            return false;
        }
        return this.mTargetActivityRecord.windowsCanBeWallpaperTarget();
    }

    com.android.server.wm.WindowState getTargetAppMainWindow() {
        if (this.mTargetActivityRecord == null) {
            return null;
        }
        return this.mTargetActivityRecord.findMainWindow();
    }

    com.android.server.wm.DisplayArea getTargetAppDisplayArea() {
        if (this.mTargetActivityRecord == null) {
            return null;
        }
        return this.mTargetActivityRecord.getDisplayArea();
    }

    boolean isAnimatingTask(com.android.server.wm.Task task) {
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            if (task == this.mPendingAnimations.get(i).mTask) {
                return true;
            }
        }
        return false;
    }

    boolean isAnimatingWallpaper(com.android.server.wm.WallpaperWindowToken token) {
        for (int i = this.mPendingWallpaperAnimations.size() - 1; i >= 0; i--) {
            if (token == this.mPendingWallpaperAnimations.get(i).getToken()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnimatingApp(com.android.server.wm.ActivityRecord activity) {
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            if (activity.isDescendantOf(this.mPendingAnimations.get(i).mTask)) {
                return true;
            }
        }
        return false;
    }

    boolean shouldIgnoreForAccessibility(com.android.server.wm.WindowState windowState) {
        com.android.server.wm.Task task = windowState.getTask();
        return (task == null || !isAnimatingTask(task) || isTargetApp(windowState.mActivityRecord)) ? false : true;
    }

    void linkFixedRotationTransformIfNeeded(com.android.server.wm.WindowToken wallpaper) {
        if (this.mTargetActivityRecord == null) {
            return;
        }
        wallpaper.linkFixedRotationTransform(this.mTargetActivityRecord);
    }

    class TaskAnimationAdapter implements com.android.server.wm.AnimationAdapter {
        private com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mCapturedFinishCallback;
        private android.view.SurfaceControl mCapturedLeash;
        private android.view.SurfaceControl mFinishOverlay;
        private android.window.PictureInPictureSurfaceTransaction mFinishTransaction;
        private final boolean mIsRecentTaskInvisible;
        private int mLastAnimationType;
        private android.view.SurfaceControl mSnapshotOverlay;
        private android.view.RemoteAnimationTarget mTarget;
        private final com.android.server.wm.Task mTask;
        private final android.graphics.Rect mBounds = new android.graphics.Rect();
        private final android.graphics.Rect mLocalBounds = new android.graphics.Rect();

        TaskAnimationAdapter(com.android.server.wm.Task task, boolean isRecentTaskInvisible) {
            this.mTask = task;
            this.mIsRecentTaskInvisible = isRecentTaskInvisible;
            this.mBounds.set(this.mTask.getBounds());
            com.android.server.wm.RecentsAnimationController.this.mRACext.adjustAnimationBounds(this.mTask, this.mBounds);
            this.mLocalBounds.set(this.mBounds);
            android.graphics.Point tmpPos = new android.graphics.Point();
            this.mTask.getRelativePosition(tmpPos);
            this.mLocalBounds.offsetTo(tmpPos.x, tmpPos.y);
        }

        android.view.RemoteAnimationTarget createRemoteAnimationTarget(int overrideTaskId, int overrideMode) {
            com.android.server.wm.WindowState mainWindow;
            int mode;
            int overrideTaskId2;
            com.android.server.wm.ActivityRecord topApp = this.mTask.getTopRealVisibleActivity();
            if (topApp == null) {
                topApp = this.mTask.getTopVisibleActivity();
            }
            if (topApp != null) {
                mainWindow = topApp.findMainWindow();
            } else {
                mainWindow = null;
            }
            if (mainWindow == null) {
                return null;
            }
            android.graphics.Rect insets = mainWindow.getInsetsStateWithVisibilityOverride().calculateInsets(this.mBounds, android.view.WindowInsets.Type.systemBars(), false).toRect();
            com.android.server.wm.utils.InsetUtils.addInsets(insets, mainWindow.mActivityRecord.getLetterboxInsets());
            if (overrideMode == -1) {
                if (topApp.getActivityType() == com.android.server.wm.RecentsAnimationController.this.mTargetActivityType) {
                    mode = 0;
                } else {
                    mode = 1;
                }
            } else {
                mode = overrideMode;
            }
            if (overrideTaskId >= 0) {
                overrideTaskId2 = overrideTaskId;
            } else {
                overrideTaskId2 = this.mTask.mTaskId;
            }
            this.mTarget = new android.view.RemoteAnimationTarget(overrideTaskId2, mode, this.mCapturedLeash, !topApp.fillsParent(), new android.graphics.Rect(), insets, this.mTask.getPrefixOrderIndex(), new android.graphics.Point(this.mBounds.left, this.mBounds.top), this.mLocalBounds, this.mBounds, this.mTask.getWindowConfiguration(), this.mIsRecentTaskInvisible, (android.view.SurfaceControl) null, (android.graphics.Rect) null, this.mTask.getTaskInfo(), topApp.checkEnterPictureInPictureAppOpsState());
            com.android.server.wm.ActivityRecord topActivity = this.mTask.getTopNonFinishingActivity();
            if (topActivity != null && topActivity.mStartingData != null && topActivity.mStartingData.hasImeSurface()) {
                this.mTarget.setWillShowImeOnTarget(true);
            }
            this.mTarget = com.android.server.wm.RecentsAnimationController.this.mRACext.obtainLaunchViewInfoForRecents(this.mTask, this.mTarget);
            return this.mTarget;
        }

        void setSnapshotOverlay(android.window.TaskSnapshot snapshot) {
            android.hardware.HardwareBuffer buffer = snapshot.getHardwareBuffer();
            if (buffer == null) {
                return;
            }
            android.view.SurfaceSession session = new android.view.SurfaceSession();
            this.mSnapshotOverlay = com.android.server.wm.RecentsAnimationController.this.mService.mSurfaceControlFactory.apply(session).setName("RecentTaskScreenshotSurface").setCallsite("TaskAnimationAdapter.setSnapshotOverlay").setFormat(buffer.getFormat()).setParent(this.mCapturedLeash).setBLASTLayer().build();
            float scale = (this.mTask.getBounds().width() * 1.0f) / buffer.getWidth();
            this.mTask.getPendingTransaction().setBuffer(this.mSnapshotOverlay, android.graphics.GraphicBuffer.createFromHardwareBuffer(buffer)).setColorSpace(this.mSnapshotOverlay, snapshot.getColorSpace()).setLayer(this.mSnapshotOverlay, Integer.MAX_VALUE).setMatrix(this.mSnapshotOverlay, scale, 0.0f, 0.0f, scale).show(this.mSnapshotOverlay).apply();
        }

        void onRemove() {
            if (this.mSnapshotOverlay != null) {
                this.mTask.getPendingTransaction().remove(this.mSnapshotOverlay).apply();
                this.mSnapshotOverlay = null;
            }
            this.mTask.setCanAffectSystemUiFlags(true);
            this.mCapturedFinishCallback.onAnimationFinished(this.mLastAnimationType, this);
        }

        void onCleanup() {
            android.view.SurfaceControl.Transaction pendingTransaction = this.mTask.getPendingTransaction();
            if (this.mFinishTransaction != null) {
                if (this.mFinishOverlay != null) {
                    pendingTransaction.reparent(this.mFinishOverlay, this.mTask.mSurfaceControl);
                }
                android.window.PictureInPictureSurfaceTransaction.apply(this.mFinishTransaction, this.mTask.mSurfaceControl, pendingTransaction);
                this.mTask.setLastRecentsAnimationTransaction(this.mFinishTransaction, this.mFinishOverlay);
                if (com.android.server.wm.RecentsAnimationController.this.mDisplayContent.isFixedRotationLaunchingApp(com.android.server.wm.RecentsAnimationController.this.mTargetActivityRecord)) {
                    com.android.server.wm.RecentsAnimationController.this.mDisplayContent.mPinnedTaskController.setEnterPipTransaction(this.mFinishTransaction);
                }
                if (this.mTask.getActivityType() != com.android.server.wm.RecentsAnimationController.this.mTargetActivityType && this.mFinishTransaction.getShouldDisableCanAffectSystemUiFlags() && !com.android.server.wm.RecentsAnimationController.this.mRACext.isInSplitRootTask(this.mTask)) {
                    this.mTask.setCanAffectSystemUiFlags(false);
                }
                this.mFinishTransaction = null;
                this.mFinishOverlay = null;
                pendingTransaction.apply();
                return;
            }
            if (!this.mTask.isAttached()) {
                pendingTransaction.apply();
            }
        }

        public android.view.SurfaceControl getSnapshotOverlay() {
            return this.mSnapshotOverlay;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
            com.android.server.wm.RecentsAnimationController.this.mRACext.hooksetPosition(animationLeash, t, this.mTask.mSurfaceAnimator, this.mLocalBounds.left, this.mLocalBounds.top);
            com.android.server.wm.RecentsAnimationController.this.mTmpRect.set(this.mLocalBounds);
            com.android.server.wm.RecentsAnimationController.this.mTmpRect.offsetTo(0, 0);
            com.android.server.wm.RecentsAnimationController.this.mRACext.hooksetWindowCrop(animationLeash, t, this.mTask.mSurfaceAnimator, com.android.server.wm.RecentsAnimationController.this.mTmpRect);
            this.mCapturedLeash = animationLeash;
            this.mCapturedFinishCallback = finishCallback;
            this.mLastAnimationType = type;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
            com.android.server.wm.RecentsAnimationController.this.cancelAnimation(2, "taskAnimationAdapterCanceled");
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return android.os.SystemClock.uptimeMillis();
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("task=" + this.mTask);
            if (this.mTarget != null) {
                pw.print(prefix);
                pw.println("Target:");
                this.mTarget.dump(pw, prefix + "  ");
            } else {
                pw.print(prefix);
                pw.println("Target: null");
            }
            pw.println("mIsRecentTaskInvisible=" + this.mIsRecentTaskInvisible);
            pw.println("mLocalBounds=" + this.mLocalBounds);
            pw.println("mFinishTransaction=" + this.mFinishTransaction);
            pw.println("mBounds=" + this.mBounds);
            pw.println("mIsRecentTaskInvisible=" + this.mIsRecentTaskInvisible);
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
            long token = proto.start(1146756268034L);
            if (this.mTarget != null) {
                this.mTarget.dumpDebug(proto, 1146756268033L);
            }
            proto.end(token);
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        java.lang.String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println(com.android.server.wm.RecentsAnimationController.class.getSimpleName() + ":");
        pw.print(innerPrefix);
        pw.println("mPendingStart=" + this.mPendingStart);
        pw.print(innerPrefix);
        pw.println("mPendingAnimations=" + this.mPendingAnimations.size());
        pw.print(innerPrefix);
        pw.println("mCanceled=" + this.mCanceled);
        pw.print(innerPrefix);
        pw.println("mInputConsumerEnabled=" + this.mInputConsumerEnabled);
        pw.print(innerPrefix);
        pw.println("mTargetActivityRecord=" + this.mTargetActivityRecord);
        pw.print(innerPrefix);
        pw.println("isTargetOverWallpaper=" + isTargetOverWallpaper());
        pw.print(innerPrefix);
        pw.println("mRequestDeferCancelUntilNextTransition=" + this.mRequestDeferCancelUntilNextTransition);
        pw.print(innerPrefix);
        pw.println("mCancelOnNextTransitionStart=" + this.mCancelOnNextTransitionStart);
        pw.print(innerPrefix);
        pw.println("mCancelDeferredWithScreenshot=" + this.mCancelDeferredWithScreenshot);
        pw.print(innerPrefix);
        pw.println("mPendingCancelWithScreenshotReorderMode=" + this.mPendingCancelWithScreenshotReorderMode);
    }
}
