package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RecentsAnimation implements com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks, com.android.server.wm.TaskDisplayArea.OnRootTaskOrderChangedListener {
    private static final java.lang.String TAG = com.android.server.wm.RecentsAnimation.class.getSimpleName();
    private final com.android.server.wm.ActivityStartController mActivityStartController;
    private final com.android.server.wm.WindowProcessController mCaller;
    private final com.android.server.wm.TaskDisplayArea mDefaultTaskDisplayArea;
    private com.android.server.wm.ActivityRecord mLaunchedTargetActivity;
    private com.android.server.wm.IRecentsAnimationExt mRecentAnimExt = (com.android.server.wm.IRecentsAnimationExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRecentsAnimationExt.class).base(this).create();
    private final android.content.ComponentName mRecentsComponent;
    private final java.lang.String mRecentsFeatureId;
    private final int mRecentsUid;
    private com.android.server.wm.Task mRestoreTargetBehindRootTask;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final int mTargetActivityType;
    private final android.content.Intent mTargetIntent;
    private final com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private final int mUserId;
    private final com.android.server.wm.WindowManagerService mWindowManager;

    RecentsAnimation(com.android.server.wm.ActivityTaskManagerService atm, com.android.server.wm.ActivityTaskSupervisor taskSupervisor, com.android.server.wm.ActivityStartController activityStartController, com.android.server.wm.WindowManagerService wm, android.content.Intent targetIntent, android.content.ComponentName recentsComponent, java.lang.String recentsFeatureId, int recentsUid, com.android.server.wm.WindowProcessController caller) {
        int i;
        this.mService = atm;
        this.mTaskSupervisor = taskSupervisor;
        this.mDefaultTaskDisplayArea = this.mRecentAnimExt.getDefaultTaskDisplayArea(targetIntent, this.mService);
        this.mActivityStartController = activityStartController;
        this.mWindowManager = wm;
        this.mTargetIntent = targetIntent;
        this.mRecentsComponent = recentsComponent;
        this.mRecentsFeatureId = recentsFeatureId;
        this.mRecentsUid = recentsUid;
        this.mCaller = caller;
        this.mUserId = atm.getCurrentUserId();
        if (targetIntent.getComponent() != null && recentsComponent.equals(targetIntent.getComponent())) {
            i = 3;
        } else {
            i = 2;
        }
        this.mTargetActivityType = i;
    }

    void preloadRecentsActivity() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mTargetIntent);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -3758280623533049031L, 0, null, protoLogParam0);
        }
        com.android.server.wm.Task targetRootTask = this.mDefaultTaskDisplayArea.getRootTask(0, this.mTargetActivityType);
        com.android.server.wm.ActivityRecord targetActivity = getTargetActivity(targetRootTask);
        if (targetActivity != null) {
            if (targetActivity.attachedToProcess()) {
                if (targetActivity.isVisibleRequested() || targetActivity.isTopRunningActivity()) {
                    return;
                }
                if (targetActivity.app.getCurrentProcState() >= 16) {
                    android.util.Slog.v(TAG, "Skip preload recents for cached proc " + targetActivity.app);
                    return;
                }
                targetActivity.ensureActivityConfiguration(true);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(targetActivity.getConfiguration());
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -3365656764099317101L, 0, null, protoLogParam02);
                }
            }
        } else {
            if (this.mDefaultTaskDisplayArea.getActivity(new com.android.server.wm.RecentsAnimation$$ExternalSyntheticLambda3(), false) == null) {
                return;
            }
            startRecentsActivityInBackground("preloadRecents");
            com.android.server.wm.Task targetRootTask2 = this.mDefaultTaskDisplayArea.getRootTask(0, this.mTargetActivityType);
            targetActivity = getTargetActivity(targetRootTask2);
            if (targetActivity == null) {
                android.util.Slog.w(TAG, "Cannot start " + this.mTargetIntent);
                return;
            }
        }
        if (!targetActivity.attachedToProcess()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -7165162073742035900L, 0, null, null);
            }
            this.mTaskSupervisor.startSpecificActivity(targetActivity, false, false);
            if (targetActivity.getTask() != null) {
                targetActivity.getTask().ensureActivitiesVisible(null);
            }
            if (targetActivity.getDisplayContent() != null) {
                targetActivity.getDisplayContent().mUnknownAppVisibilityController.appRemovedOrHidden(targetActivity);
            }
        }
        if (!targetActivity.finishing && targetActivity.isAttached() && !targetActivity.isState(com.android.server.wm.ActivityRecord.State.STOPPING, com.android.server.wm.ActivityRecord.State.STOPPED)) {
            targetActivity.addToStopping(true, true, "preloadRecents");
        }
    }

    void startRecentsActivity(android.view.IRecentsAnimationRunner recentsAnimationRunner, long eventTime) throws java.lang.Exception {
        com.android.server.wm.Task targetRootTask;
        com.android.server.wm.ActivityRecord targetActivity;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mTargetIntent);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -3403665718306852375L, 0, null, protoLogParam0);
        }
        this.mRecentAnimExt.disableSensorScreenShot(this.mService.mContext);
        android.os.Trace.traceBegin(32L, "RecentsAnimation#startRecentsActivity");
        if (this.mWindowManager.getRecentsAnimationController() != null) {
            this.mWindowManager.getRecentsAnimationController().forceCancelAnimation(2, "startRecentsActivity");
        }
        com.android.server.wm.Task targetRootTask2 = this.mDefaultTaskDisplayArea.getRootTask(0, this.mTargetActivityType);
        com.android.server.wm.ActivityRecord targetActivity2 = getTargetActivity(targetRootTask2);
        boolean hasExistingActivity = targetActivity2 != null;
        if (hasExistingActivity) {
            this.mRestoreTargetBehindRootTask = com.android.server.wm.TaskDisplayArea.getRootTaskAbove(targetRootTask2);
            if (this.mRestoreTargetBehindRootTask == null && targetRootTask2.getTopMostTask() == targetActivity2.getTask()) {
                notifyAnimationCancelBeforeStart(recentsAnimationRunner);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(targetRootTask2);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -8325607672707336373L, 0, null, protoLogParam02);
                    return;
                }
                return;
            }
        }
        if (this.mRecentAnimExt.hasGestureAnimationController()) {
            android.util.Slog.d(TAG, "don't start recent animation before gesture animation over");
            notifyAnimationCancelBeforeStart(recentsAnimationRunner);
            return;
        }
        if (targetActivity2 == null || !targetActivity2.isVisibleRequested()) {
            this.mService.mRootWindowContainer.startPowerModeLaunchIfNeeded(true, targetActivity2);
        }
        com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState = this.mTaskSupervisor.getActivityMetricsLogger().notifyActivityLaunching(this.mTargetIntent);
        setProcessAnimating(true);
        this.mRecentAnimExt.onRecentAnimationStart();
        this.mService.deferWindowLayout();
        try {
            try {
                if (hasExistingActivity) {
                    this.mDefaultTaskDisplayArea.moveRootTaskBehindBottomMostVisibleRootTask(targetRootTask2);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(targetRootTask2);
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(com.android.server.wm.TaskDisplayArea.getRootTaskAbove(targetRootTask2));
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -7278356485797757819L, 0, null, protoLogParam03, protoLogParam1);
                    }
                    com.android.server.wm.Task task = targetActivity2.getTask();
                    if (targetRootTask2.getTopMostTask() != task) {
                        targetRootTask2.positionChildAtTop(task);
                    }
                    targetRootTask = targetRootTask2;
                    targetActivity = targetActivity2;
                } else {
                    startRecentsActivityInBackground("startRecentsActivity_noTargetActivity");
                    com.android.server.wm.Task targetRootTask3 = this.mDefaultTaskDisplayArea.getRootTask(0, this.mTargetActivityType);
                    com.android.server.wm.ActivityRecord targetActivity3 = getTargetActivity(targetRootTask3);
                    if (this.mRecentAnimExt.startRecentsWhenKeyguardLocked(targetActivity3, this.mWindowManager)) {
                        android.util.Slog.d(TAG, "Failed to start recents activity targetActivity is " + targetActivity3);
                        notifyAnimationCancelBeforeStart(recentsAnimationRunner);
                        this.mService.continueWindowLayout();
                        android.os.Trace.traceEnd(32L);
                        return;
                    }
                    this.mDefaultTaskDisplayArea.moveRootTaskBehindBottomMostVisibleRootTask(targetRootTask3);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                        java.lang.String protoLogParam04 = java.lang.String.valueOf(targetRootTask3);
                        java.lang.String protoLogParam12 = java.lang.String.valueOf(com.android.server.wm.TaskDisplayArea.getRootTaskAbove(targetRootTask3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -7278356485797757819L, 0, null, protoLogParam04, protoLogParam12);
                    }
                    this.mWindowManager.prepareAppTransitionNone();
                    this.mWindowManager.executeAppTransition();
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                        java.lang.String protoLogParam05 = java.lang.String.valueOf(this.mTargetIntent);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 1012359606301505741L, 0, null, protoLogParam05);
                    }
                    targetRootTask = targetRootTask3;
                    targetActivity = targetActivity3;
                }
                try {
                    targetActivity.mLaunchTaskBehind = true;
                    this.mLaunchedTargetActivity = targetActivity;
                    targetActivity.intent.replaceExtras(this.mTargetIntent);
                    this.mWindowManager.initializeRecentsAnimation(this.mTargetActivityType, recentsAnimationRunner, this, this.mDefaultTaskDisplayArea.getDisplayId(), this.mTaskSupervisor.mRecentTasks.getRecentTaskIds(), targetActivity);
                    this.mService.mRootWindowContainer.ensureActivitiesVisible();
                    android.app.ActivityOptions options = null;
                    if (eventTime > 0) {
                        try {
                            options = android.app.ActivityOptions.makeBasic();
                            options.setSourceInfo(4, eventTime);
                        } catch (java.lang.Exception e) {
                            e = e;
                            android.util.Slog.e(TAG, "Failed to start recents activity", e);
                            throw e;
                        } catch (java.lang.Throwable th) {
                            e = th;
                            this.mService.continueWindowLayout();
                            android.os.Trace.traceEnd(32L);
                            throw e;
                        }
                    }
                    try {
                        this.mTaskSupervisor.getActivityMetricsLogger().notifyActivityLaunched(launchingState, 2, !hasExistingActivity, targetActivity, options);
                        this.mDefaultTaskDisplayArea.registerRootTaskOrderChangedListener(this);
                        this.mRecentAnimExt.needHideInputMethod(this.mDefaultTaskDisplayArea.getFocusedActivity());
                        this.mService.continueWindowLayout();
                        android.os.Trace.traceEnd(32L);
                    } catch (java.lang.Exception e2) {
                        e = e2;
                        android.util.Slog.e(TAG, "Failed to start recents activity", e);
                        throw e;
                    } catch (java.lang.Throwable th2) {
                        e = th2;
                        this.mService.continueWindowLayout();
                        android.os.Trace.traceEnd(32L);
                        throw e;
                    }
                } catch (java.lang.Exception e3) {
                    e = e3;
                } catch (java.lang.Throwable th3) {
                    e = th3;
                }
            } catch (java.lang.Exception e4) {
                e = e4;
            }
        } catch (java.lang.Throwable th4) {
            e = th4;
        }
    }

    private void finishAnimation(final int reorderMode, final boolean sendUserLeaveHint) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mWindowManager.getRecentsAnimationController());
                    long protoLogParam1 = reorderMode;
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 5474198007669537235L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                }
                this.mDefaultTaskDisplayArea.unregisterRootTaskOrderChangedListener(this);
                final com.android.server.wm.RecentsAnimationController controller = this.mWindowManager.getRecentsAnimationController();
                if (controller == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (reorderMode != 0) {
                    this.mService.endPowerMode(1);
                }
                if (reorderMode == 1) {
                    this.mService.stopAppSwitches();
                }
                this.mRecentAnimExt.onRecentAnimationEnd();
                inSurfaceTransaction(new java.lang.Runnable() { // from class: com.android.server.wm.RecentsAnimation$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$finishAnimation$0(reorderMode, sendUserLeaveHint, controller);
                    }
                });
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishAnimation$0(int reorderMode, boolean sendUserLeaveHint, com.android.server.wm.RecentsAnimationController controller) {
        android.os.Trace.traceBegin(32L, "RecentsAnimation#onAnimationFinished_inSurfaceTransaction");
        this.mService.deferWindowLayout();
        try {
            try {
                this.mWindowManager.cleanupRecentsAnimation(reorderMode);
                com.android.server.wm.Task targetRootTask = this.mDefaultTaskDisplayArea.getRootTask(0, this.mTargetActivityType);
                com.android.server.wm.ActivityRecord targetActivity = targetRootTask != null ? targetRootTask.isInTask(this.mLaunchedTargetActivity) : null;
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(targetRootTask);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(targetActivity);
                    java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mRestoreTargetBehindRootTask);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 3525834288436624965L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
                }
                if (targetActivity == null) {
                    return;
                }
                targetActivity.mLaunchTaskBehind = false;
                if (reorderMode == 1) {
                    this.mTaskSupervisor.mNoAnimActivities.add(targetActivity);
                    if (sendUserLeaveHint) {
                        this.mTaskSupervisor.mUserLeaving = true;
                        targetRootTask.moveTaskToFront(targetActivity.getTask(), true, null, targetActivity.appTimeTracker, "RecentsAnimation.onAnimationFinished()");
                    } else {
                        targetRootTask.moveToFront("RecentsAnimation.onAnimationFinished()");
                    }
                    if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS.isLogToAny()) {
                        com.android.server.wm.Task topRootTask = getTopNonAlwaysOnTopRootTask();
                        if (topRootTask != targetRootTask && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[3]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(targetRootTask);
                            java.lang.String protoLogParam12 = java.lang.String.valueOf(topRootTask);
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -5961176083217302671L, 0, null, protoLogParam02, protoLogParam12);
                        }
                    }
                } else {
                    if (reorderMode != 2) {
                        if (!controller.shouldDeferCancelWithScreenshot() && !targetRootTask.isFocusedRootTaskOnDisplay()) {
                            targetRootTask.ensureActivitiesVisible(null);
                        }
                        this.mTaskSupervisor.mUserLeaving = false;
                        this.mService.continueWindowLayout();
                        if (this.mWindowManager.mRoot.isLayoutNeeded()) {
                            this.mWindowManager.mRoot.performSurfacePlacement();
                        }
                        setProcessAnimating(false);
                        this.mRecentAnimExt.finishAnimation();
                        this.mService.getWrapper().getFlexibleExtImpl().onRecentsAnimationExecuting(null, false, reorderMode);
                        android.os.Trace.traceEnd(32L);
                        return;
                    }
                    com.android.server.wm.TaskDisplayArea taskDisplayArea = targetActivity.getDisplayArea();
                    taskDisplayArea.moveRootTaskBehindRootTask(targetRootTask, this.mRestoreTargetBehindRootTask);
                    if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS.isLogToAny()) {
                        com.android.server.wm.Task aboveTargetRootTask = com.android.server.wm.TaskDisplayArea.getRootTaskAbove(targetRootTask);
                        if (this.mRestoreTargetBehindRootTask != null && aboveTargetRootTask != this.mRestoreTargetBehindRootTask && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[3]) {
                            java.lang.String protoLogParam03 = java.lang.String.valueOf(targetRootTask);
                            java.lang.String protoLogParam13 = java.lang.String.valueOf(this.mRestoreTargetBehindRootTask);
                            java.lang.String protoLogParam22 = java.lang.String.valueOf(aboveTargetRootTask);
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -5893976429537642045L, 0, null, protoLogParam03, protoLogParam13, protoLogParam22);
                        }
                    }
                }
                this.mWindowManager.prepareAppTransitionNone();
                this.mService.mRootWindowContainer.ensureActivitiesVisible();
                this.mService.mRootWindowContainer.resumeFocusedTasksTopActivities();
                this.mWindowManager.executeAppTransition();
                com.android.server.wm.Task rootTask = targetRootTask.getRootTask();
                rootTask.dispatchTaskInfoChangedIfNeeded(true);
                this.mTaskSupervisor.mUserLeaving = false;
                this.mService.continueWindowLayout();
                if (this.mWindowManager.mRoot.isLayoutNeeded()) {
                    this.mWindowManager.mRoot.performSurfacePlacement();
                }
                setProcessAnimating(false);
                this.mRecentAnimExt.finishAnimation();
                this.mService.getWrapper().getFlexibleExtImpl().onRecentsAnimationExecuting(null, false, reorderMode);
                android.os.Trace.traceEnd(32L);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to clean up recents activity", e);
                throw e;
            }
        } finally {
            this.mTaskSupervisor.mUserLeaving = false;
            this.mService.continueWindowLayout();
            if (this.mWindowManager.mRoot.isLayoutNeeded()) {
                this.mWindowManager.mRoot.performSurfacePlacement();
            }
            setProcessAnimating(false);
            this.mRecentAnimExt.finishAnimation();
            this.mService.getWrapper().getFlexibleExtImpl().onRecentsAnimationExecuting(null, false, reorderMode);
            android.os.Trace.traceEnd(32L);
        }
    }

    private static void inSurfaceTransaction(java.lang.Runnable exec) {
        exec.run();
    }

    private void setProcessAnimating(boolean animating) {
        int demoteReasons;
        if (this.mCaller == null) {
            return;
        }
        this.mCaller.setRunningRecentsAnimation(animating);
        int demoteReasons2 = this.mService.mDemoteTopAppReasons;
        if (animating) {
            demoteReasons = demoteReasons2 | 2;
        } else {
            demoteReasons = demoteReasons2 & (-3);
        }
        this.mService.mDemoteTopAppReasons = demoteReasons;
        if (animating && this.mService.mTopApp != null) {
            this.mService.mTopApp.scheduleUpdateOomAdj();
        }
    }

    @Override // com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks
    public void onAnimationFinished(int reorderMode, boolean sendUserLeaveHint) {
        finishAnimation(reorderMode, sendUserLeaveHint);
    }

    @Override // com.android.server.wm.TaskDisplayArea.OnRootTaskOrderChangedListener
    public void onRootTaskOrderChanged(final com.android.server.wm.Task rootTask) {
        com.android.server.wm.RecentsAnimationController controller;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(rootTask);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, 4515487264815398694L, 0, null, protoLogParam0);
        }
        if (this.mDefaultTaskDisplayArea.getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.RecentsAnimation$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RecentsAnimation.lambda$onRootTaskOrderChanged$1(rootTask, (com.android.server.wm.Task) obj);
            }
        }) == null || !rootTask.shouldBeVisible(null) || (controller = this.mWindowManager.getRecentsAnimationController()) == null) {
            return;
        }
        if ((!controller.isAnimatingTask(rootTask.getTopMostTask()) || controller.isTargetApp(rootTask.getTopNonFinishingActivity())) && controller.shouldDeferCancelUntilNextTransition()) {
            this.mWindowManager.prepareAppTransitionNone();
            controller.setCancelOnNextTransitionStart();
        }
    }

    static /* synthetic */ boolean lambda$onRootTaskOrderChanged$1(com.android.server.wm.Task rootTask, com.android.server.wm.Task t) {
        return t == rootTask;
    }

    private void startRecentsActivityInBackground(java.lang.String reason) {
        android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic();
        options.setLaunchActivityType(this.mTargetActivityType);
        options.setAvoidMoveToFront();
        this.mRecentAnimExt.startSecondHomeActivityInBackground(this.mTargetIntent, options);
        this.mTargetIntent.addFlags(268500992);
        this.mActivityStartController.obtainStarter(this.mTargetIntent, reason).setCallingUid(this.mRecentsUid).setCallingPackage(this.mRecentsComponent.getPackageName()).setCallingFeatureId(this.mRecentsFeatureId).setActivityOptions(new com.android.server.wm.SafeActivityOptions(options)).setUserId(this.mUserId).execute();
    }

    static void notifyAnimationCancelBeforeStart(android.view.IRecentsAnimationRunner recentsAnimationRunner) {
        try {
            recentsAnimationRunner.onAnimationCanceled((int[]) null, (android.window.TaskSnapshot[]) null);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to cancel recents animation before start", e);
        }
    }

    private com.android.server.wm.Task getTopNonAlwaysOnTopRootTask() {
        return this.mDefaultTaskDisplayArea.getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.RecentsAnimation$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RecentsAnimation.lambda$getTopNonAlwaysOnTopRootTask$2((com.android.server.wm.Task) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getTopNonAlwaysOnTopRootTask$2(com.android.server.wm.Task task) {
        return !task.getWindowConfiguration().isAlwaysOnTop();
    }

    private com.android.server.wm.ActivityRecord getTargetActivity(com.android.server.wm.Task targetRootTask) {
        if (targetRootTask == null) {
            return null;
        }
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new java.util.function.BiPredicate() { // from class: com.android.server.wm.RecentsAnimation$$ExternalSyntheticLambda0
            @Override // java.util.function.BiPredicate
            public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
                return ((com.android.server.wm.RecentsAnimation) obj).matchesTarget((com.android.server.wm.Task) obj2);
            }
        }, this, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.Task.class));
        com.android.server.wm.Task task = targetRootTask.getTask(p);
        p.recycle();
        if (task != null) {
            return task.getTopNonFinishingActivity();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean matchesTarget(com.android.server.wm.Task task) {
        return task.getNonFinishingActivityCount() > 0 && task.mUserId == this.mUserId && task.getBaseIntent().getComponent().equals(this.mTargetIntent.getComponent());
    }
}
