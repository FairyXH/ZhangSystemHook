package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class BackNavigationController {
    private static final java.lang.String TAG = "CoreBackPreview";
    private static int sDefaultAnimationResId;
    static final boolean sPredictBackEnable = android.os.SystemProperties.getBoolean("persist.wm.debug.predictive_back", true);
    private com.android.server.wm.BackNavigationController.AnimationHandler mAnimationHandler;
    private boolean mBackAnimationInProgress;
    private int mLastBackType;
    private java.lang.Runnable mPendingAnimation;
    private com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder mPendingAnimationBuilder;
    private boolean mShowWallpaper;
    private com.android.server.wm.Transition mWaitTransitionFinish;
    private com.android.server.wm.WindowManagerService mWindowManagerService;
    private final com.android.server.wm.BackNavigationController.NavigationMonitor mNavigationMonitor = new com.android.server.wm.BackNavigationController.NavigationMonitor();
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTmpOpenApps = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTmpCloseApps = new java.util.ArrayList<>();
    private com.android.server.wm.IBackNavigationControllerExt mBackNavigationControllerExt = (com.android.server.wm.IBackNavigationControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IBackNavigationControllerExt.class).base(this).create();

    BackNavigationController() {
    }

    void onFocusChanged(com.android.server.wm.WindowState newFocus) {
        this.mNavigationMonitor.onFocusWindowChanged(newFocus);
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x0246 A[Catch: all -> 0x0535, TRY_ENTER, TryCatch #6 {all -> 0x0535, blocks: (B:127:0x0233, B:135:0x0246, B:138:0x0254), top: B:320:0x0233 }] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0402  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    android.window.BackNavigationInfo startBackNavigation(android.os.RemoteCallback r33, android.window.BackAnimationAdapter r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1410
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.BackNavigationController.startBackNavigation(android.os.RemoteCallback, android.window.BackAnimationAdapter):android.window.BackNavigationInfo");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startBackNavigation$0(android.os.Bundle result) {
        lambda$startBackNavigation$3(result, 4);
    }

    static /* synthetic */ boolean lambda$startBackNavigation$1(com.android.server.wm.Task t) {
        com.android.server.wm.ActivityRecord ar;
        return t.showToCurrentUser() && !t.mChildren.isEmpty() && (ar = t.getTopNonFinishingActivity()) != null && ar.showToCurrentUser();
    }

    static boolean getAnimatablePrevActivities(com.android.server.wm.Task currentTask, com.android.server.wm.ActivityRecord currentActivity, java.util.ArrayList<com.android.server.wm.ActivityRecord> outPrevActivities) {
        if (currentActivity.mAtmService.mTaskOrganizerController.shouldInterceptBackPressedOnRootTask(currentTask.getRootTask())) {
            return false;
        }
        com.android.server.wm.ActivityRecord root = currentTask.getRootActivity(false, true);
        if (root == null || !com.android.server.wm.ActivityClientController.shouldMoveTaskToBack(currentActivity, root)) {
            com.android.server.wm.ActivityRecord prevActivity = currentTask.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.BackNavigationController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.BackNavigationController.lambda$getAnimatablePrevActivities$4((com.android.server.wm.ActivityRecord) obj);
                }
            }, currentActivity, false, true);
            com.android.server.wm.TaskFragment currTF = currentActivity.getTaskFragment();
            if (currTF != null && currTF.asTask() == null) {
                if (prevActivity != null && currTF.hasChild(prevActivity)) {
                    outPrevActivities.add(prevActivity);
                    return true;
                }
                if (currTF.getAdjacentTaskFragment() == null) {
                    com.android.server.wm.TaskFragment nextTF = findNextTaskFragment(currentTask, currTF);
                    if (isSecondCompanionToFirst(currTF, nextTF)) {
                        com.android.server.wm.ActivityRecord bottomActivityInCompanion = nextTF.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.BackNavigationController$$ExternalSyntheticLambda1
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return com.android.server.wm.BackNavigationController.lambda$getAnimatablePrevActivities$5((com.android.server.wm.ActivityRecord) obj);
                            }
                        }, false);
                        com.android.server.wm.ActivityRecord underPrevious = currentTask.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.BackNavigationController$$ExternalSyntheticLambda2
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return com.android.server.wm.BackNavigationController.lambda$getAnimatablePrevActivities$6((com.android.server.wm.ActivityRecord) obj);
                            }
                        }, bottomActivityInCompanion, false, true);
                        if (underPrevious != null) {
                            outPrevActivities.add(underPrevious);
                            addPreviousAdjacentActivityIfExist(underPrevious, outPrevActivities);
                        }
                        return true;
                    }
                } else {
                    com.android.server.wm.TaskFragment adjacentTF = currTF.getAdjacentTaskFragment();
                    if (!isSecondCompanionToFirst(currTF, adjacentTF)) {
                        return false;
                    }
                    com.android.server.wm.WindowContainer commonParent = currTF.getParent();
                    com.android.server.wm.TaskFragment lowerTF = commonParent.mChildren.indexOf(currTF) < commonParent.mChildren.indexOf(adjacentTF) ? currTF : adjacentTF;
                    com.android.server.wm.ActivityRecord lowerActivity = lowerTF.getTopNonFinishingActivity();
                    return currentTask.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.BackNavigationController$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.BackNavigationController.lambda$getAnimatablePrevActivities$7((com.android.server.wm.ActivityRecord) obj);
                        }
                    }, lowerActivity, false, true) == null;
                }
            }
            if (prevActivity == null) {
                return true;
            }
            addPreviousAdjacentActivityIfExist(prevActivity, outPrevActivities);
            outPrevActivities.add(prevActivity);
            return true;
        }
        return true;
    }

    static /* synthetic */ boolean lambda$getAnimatablePrevActivities$4(com.android.server.wm.ActivityRecord below) {
        return !below.finishing;
    }

    static /* synthetic */ boolean lambda$getAnimatablePrevActivities$5(com.android.server.wm.ActivityRecord below) {
        return !below.finishing;
    }

    static /* synthetic */ boolean lambda$getAnimatablePrevActivities$6(com.android.server.wm.ActivityRecord below) {
        return !below.finishing;
    }

    static /* synthetic */ boolean lambda$getAnimatablePrevActivities$7(com.android.server.wm.ActivityRecord below) {
        return !below.finishing;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private static com.android.server.wm.TaskFragment findNextTaskFragment(com.android.server.wm.Task currentTask, com.android.server.wm.TaskFragment topTF) {
        int topIndex = currentTask.mChildren.indexOf(topTF);
        if (topIndex <= 0) {
            return null;
        }
        com.android.server.wm.WindowContainer next = (com.android.server.wm.WindowContainer) currentTask.mChildren.get(topIndex - 1);
        return next.asTaskFragment();
    }

    private static boolean isSecondCompanionToFirst(com.android.server.wm.TaskFragment first, com.android.server.wm.TaskFragment second) {
        return second != null && second.getCompanionTaskFragment() == first;
    }

    private static void addPreviousAdjacentActivityIfExist(com.android.server.wm.ActivityRecord prevActivity, java.util.ArrayList<com.android.server.wm.ActivityRecord> outPrevActivities) {
        com.android.server.wm.TaskFragment prevTFAdjacent;
        com.android.server.wm.ActivityRecord prevActivityAdjacent;
        com.android.server.wm.TaskFragment prevTF = prevActivity.getTaskFragment();
        if (prevTF != null && prevTF.asTask() == null && (prevTFAdjacent = prevTF.getAdjacentTaskFragment()) != null && prevTFAdjacent.asTask() == null && (prevActivityAdjacent = prevTFAdjacent.getTopNonFinishingActivity()) != null) {
            outPrevActivities.add(prevActivityAdjacent);
        }
    }

    private static void findAdjacentActivityIfExist(com.android.server.wm.ActivityRecord mainActivity, java.util.ArrayList<com.android.server.wm.ActivityRecord> outList) {
        com.android.server.wm.TaskFragment mainTF = mainActivity.getTaskFragment();
        if (mainTF == null || mainTF.getAdjacentTaskFragment() == null) {
            return;
        }
        com.android.server.wm.TaskFragment adjacentTF = mainTF.getAdjacentTaskFragment();
        com.android.server.wm.ActivityRecord topActivity = adjacentTF.getTopNonFinishingActivity();
        if (topActivity == null) {
            return;
        }
        outList.add(topActivity);
    }

    private static boolean hasTranslucentActivity(com.android.server.wm.ActivityRecord currentActivity, java.util.ArrayList<com.android.server.wm.ActivityRecord> prevActivities) {
        if (!currentActivity.occludesParent() || currentActivity.showWallpaper()) {
            return true;
        }
        for (int i = prevActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord test = prevActivities.get(i);
            if (!test.occludesParent() || test.hasWallpaper()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAllActivitiesCanShowWhenLocked(java.util.ArrayList<com.android.server.wm.ActivityRecord> prevActivities) {
        for (int i = prevActivities.size() - 1; i >= 0; i--) {
            if (!prevActivities.get(i).canShowWhenLocked()) {
                return false;
            }
        }
        return !prevActivities.isEmpty();
    }

    private static boolean isAllActivitiesCreated(java.util.ArrayList<com.android.server.wm.ActivityRecord> prevActivities) {
        for (int i = prevActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord check = prevActivities.get(i);
            if (check.isState(com.android.server.wm.ActivityRecord.State.INITIALIZING)) {
                return false;
            }
        }
        return !prevActivities.isEmpty();
    }

    boolean isMonitoringTransition() {
        return this.mAnimationHandler.mComposed || this.mNavigationMonitor.isMonitorForRemote();
    }

    private void scheduleAnimation(com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder builder) {
        this.mPendingAnimation = builder.build();
        this.mWindowManagerService.mWindowPlacerLocked.requestTraversal();
        if (this.mShowWallpaper) {
            this.mWindowManagerService.getDefaultDisplayContentLocked().mWallpaperController.adjustWallpaperWindows();
        }
    }

    private boolean isWaitBackTransition() {
        return this.mAnimationHandler.mComposed && this.mAnimationHandler.mWaitTransition;
    }

    boolean isKeyguardOccluded(com.android.server.wm.WindowState focusWindow) {
        com.android.server.wm.KeyguardController kc = this.mWindowManagerService.mAtmService.mKeyguardController;
        int displayId = focusWindow.getDisplayId();
        return kc.isKeyguardOccluded(displayId);
    }

    private static boolean isCustomizeExitAnimation(com.android.server.wm.WindowState window) {
        if (!java.util.Objects.equals(window.mAttrs.packageName, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) && window.mAttrs.windowAnimations != 0) {
            com.android.internal.policy.TransitionAnimation transitionAnimation = window.getDisplayContent().mAppTransition.mTransitionAnimation;
            int appResId = transitionAnimation.getAnimationResId(window.mAttrs, 7, 0);
            if (android.content.res.ResourceId.isValid(appResId)) {
                if (sDefaultAnimationResId == 0) {
                    sDefaultAnimationResId = transitionAnimation.getDefaultAnimationResId(7, 0);
                }
                return sDefaultAnimationResId != appResId;
            }
        }
        return false;
    }

    boolean removeIfContainsBackAnimationTargets(android.util.ArraySet<com.android.server.wm.ActivityRecord> openApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closeApps) {
        if (!isMonitoringTransition()) {
            return false;
        }
        this.mTmpCloseApps.addAll(closeApps);
        boolean matchAnimationTargets = removeIfWaitForBackTransition(openApps, closeApps);
        if (!matchAnimationTargets) {
            this.mNavigationMonitor.onTransitionReadyWhileNavigate(this.mTmpOpenApps, this.mTmpCloseApps);
        }
        this.mTmpCloseApps.clear();
        return matchAnimationTargets;
    }

    boolean removeIfWaitForBackTransition(android.util.ArraySet<com.android.server.wm.ActivityRecord> openApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closeApps) {
        if (!isWaitBackTransition() || !this.mAnimationHandler.containsBackAnimationTargets(this.mTmpOpenApps, this.mTmpCloseApps)) {
            return false;
        }
        for (int i = openApps.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord ar = openApps.valueAt(i);
            if (this.mAnimationHandler.isTarget(ar, true)) {
                openApps.removeAt(i);
                this.mAnimationHandler.markStartingSurfaceMatch(null);
            }
        }
        int i2 = closeApps.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.wm.ActivityRecord ar2 = closeApps.valueAt(i3);
            if (this.mAnimationHandler.isTarget(ar2, false)) {
                closeApps.removeAt(i3);
            }
        }
        return true;
    }

    void removePredictiveSurfaceIfNeeded(com.android.server.wm.ActivityRecord openActivity) {
        this.mAnimationHandler.markWindowHasDrawn(openActivity);
    }

    class NavigationMonitor {
        private com.android.server.wm.WindowState mNavigatingWindow;
        private android.os.RemoteCallback mObserver;

        NavigationMonitor() {
        }

        void startMonitor(com.android.server.wm.WindowState window, android.os.RemoteCallback observer) {
            this.mNavigatingWindow = window;
            this.mObserver = observer;
        }

        void stopMonitorForRemote() {
            this.mObserver = null;
        }

        void stopMonitorTransition() {
            this.mNavigatingWindow = null;
        }

        boolean isMonitorForRemote() {
            return (this.mNavigatingWindow == null || this.mObserver == null) ? false : true;
        }

        boolean isMonitorAnimationOrTransition() {
            return this.mNavigatingWindow != null && (com.android.server.wm.BackNavigationController.this.mAnimationHandler.mComposed || com.android.server.wm.BackNavigationController.this.mAnimationHandler.mWaitTransition);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFocusWindowChanged(com.android.server.wm.WindowState newFocus) {
            if (atSameDisplay(newFocus)) {
                if ((!isMonitorForRemote() && !isMonitorAnimationOrTransition()) || newFocus == null || newFocus == this.mNavigatingWindow) {
                    return;
                }
                if (newFocus.mActivityRecord == null || newFocus.mActivityRecord == this.mNavigatingWindow.mActivityRecord) {
                    cancelBackNavigating("focusWindowChanged");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onTransitionReadyWhileNavigate(java.util.ArrayList<com.android.server.wm.WindowContainer> opening, java.util.ArrayList<com.android.server.wm.WindowContainer> closing) {
            if (!isMonitorForRemote() && !isMonitorAnimationOrTransition()) {
                return;
            }
            java.util.ArrayList<com.android.server.wm.WindowContainer> all = new java.util.ArrayList<>(opening);
            all.addAll(closing);
            for (int i = all.size() - 1; i >= 0; i--) {
                if (all.get(i).hasChild(this.mNavigatingWindow)) {
                    cancelBackNavigating("transitionHappens");
                    return;
                }
            }
        }

        private boolean atSameDisplay(com.android.server.wm.WindowState newFocus) {
            if (this.mNavigatingWindow == null) {
                return false;
            }
            int navigatingDisplayId = this.mNavigatingWindow.getDisplayId();
            return newFocus == null || newFocus.getDisplayId() == navigatingDisplayId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancelBackNavigating(java.lang.String reason) {
            com.android.server.wm.EventLogTags.writeWmBackNaviCanceled(reason);
            if (isMonitorForRemote()) {
                this.mObserver.sendResult((android.os.Bundle) null);
            }
            if (isMonitorAnimationOrTransition()) {
                com.android.server.wm.BackNavigationController.this.clearBackAnimations(true);
            }
            com.android.server.wm.BackNavigationController.this.cancelPendingAnimation();
        }
    }

    void onTransactionReady(com.android.server.wm.Transition transition, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets, android.view.SurfaceControl.Transaction startTransaction) {
        if (!isMonitoringTransition() || targets.isEmpty()) {
            return;
        }
        if (this.mAnimationHandler.hasTargetDetached()) {
            this.mNavigationMonitor.cancelBackNavigating("targetDetached");
            return;
        }
        for (int i = targets.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = targets.get(i).mContainer;
            if ((wc.asActivityRecord() != null || wc.asTask() != null || wc.asTaskFragment() != null) && targets.get(i).getTransitMode(wc) != 6) {
                if (wc.isVisibleRequested()) {
                    this.mTmpOpenApps.add(wc);
                } else {
                    this.mTmpCloseApps.add(wc);
                }
            }
        }
        boolean matchAnimationTargets = isWaitBackTransition() && (transition.mType == 2 || transition.mType == 4) && this.mAnimationHandler.containsBackAnimationTargets(this.mTmpOpenApps, this.mTmpCloseApps);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BACK_PREVIEW_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mTmpOpenApps);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mTmpCloseApps);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mAnimationHandler);
            boolean protoLogParam3 = matchAnimationTargets;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, -6431452312492819825L, 192, "onTransactionReady, opening: %s, closing: %s, animating: %s, match: %b", protoLogParam0, protoLogParam1, protoLogParam2, java.lang.Boolean.valueOf(protoLogParam3));
        }
        if (!matchAnimationTargets) {
            this.mNavigationMonitor.onTransitionReadyWhileNavigate(this.mTmpOpenApps, this.mTmpCloseApps);
        } else {
            if (this.mWaitTransitionFinish != null) {
                android.util.Slog.e(TAG, "Gesture animation is applied on another transition?");
            }
            this.mWaitTransitionFinish = transition;
            int i2 = this.mTmpOpenApps.size() - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                if (this.mAnimationHandler.isTarget(this.mTmpOpenApps.get(i2), true)) {
                    this.mAnimationHandler.markStartingSurfaceMatch(startTransaction);
                    break;
                }
                i2--;
            }
            if (this.mAnimationHandler.mOpenAnimAdaptor.mCloseTransaction != null) {
                startTransaction.merge(this.mAnimationHandler.mOpenAnimAdaptor.mCloseTransaction);
                this.mAnimationHandler.mOpenAnimAdaptor.mCloseTransaction = null;
            }
            startTransaction.hide(this.mAnimationHandler.mCloseAdaptor.mTarget.getSurfaceControl());
        }
        this.mTmpOpenApps.clear();
        this.mTmpCloseApps.clear();
    }

    boolean isMonitorTransitionTarget(com.android.server.wm.WindowContainer wc) {
        if (!isWaitBackTransition() || this.mWaitTransitionFinish == null) {
            return false;
        }
        return this.mAnimationHandler.isTarget(wc, wc.isVisibleRequested());
    }

    boolean shouldPauseTouch(com.android.server.wm.WindowContainer wc) {
        return this.mAnimationHandler.mComposed && this.mWaitTransitionFinish == null && this.mAnimationHandler.isTarget(wc, wc.isVisibleRequested());
    }

    void clearBackAnimations(boolean cancel) {
        this.mAnimationHandler.clearBackAnimateTarget(cancel);
        this.mNavigationMonitor.stopMonitorTransition();
        this.mWaitTransitionFinish = null;
    }

    void onTransitionFinish(java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets, com.android.server.wm.Transition finishedTransition) {
        if (finishedTransition == this.mWaitTransitionFinish) {
            clearBackAnimations(false);
        }
        if (!this.mBackAnimationInProgress || this.mPendingAnimationBuilder == null) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BACK_PREVIEW_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, -4051770154814262074L, 0, "Handling the deferred animation after transition finished", null);
        }
        boolean hasTarget = false;
        int i = 0;
        while (true) {
            if (i >= finishedTransition.mParticipants.size()) {
                break;
            }
            com.android.server.wm.WindowContainer wc = finishedTransition.mParticipants.valueAt(i);
            if ((wc.asActivityRecord() == null && wc.asTask() == null && wc.asTaskFragment() == null) || !this.mPendingAnimationBuilder.containTarget(wc)) {
                i++;
            } else {
                hasTarget = true;
                break;
            }
        }
        if (!hasTarget) {
            android.util.Slog.w(TAG, "Finished transition didn't include the targets open: " + java.util.Arrays.toString(this.mPendingAnimationBuilder.mOpenTargets) + " close: " + this.mPendingAnimationBuilder.mCloseTarget);
            cancelPendingAnimation();
            return;
        }
        for (int i2 = 0; i2 < targets.size(); i2++) {
            targets.get(i2).mContainer.prepareSurfaces();
        }
        if (this.mPendingAnimationBuilder != null) {
            scheduleAnimation(this.mPendingAnimationBuilder);
            this.mPendingAnimationBuilder = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingAnimation() {
        if (this.mPendingAnimationBuilder == null) {
            return;
        }
        try {
            this.mPendingAnimationBuilder.mBackAnimationAdapter.getRunner().onAnimationCancelled();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote animation gone", e);
        }
        this.mPendingAnimationBuilder = null;
    }

    static class AnimationHandler {
        private static final int ACTIVITY_SWITCH = 2;
        private static final int DIALOG_CLOSE = 3;
        private static final int TASK_SWITCH = 1;
        private static final int UNKNOWN = 0;
        private com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor mCloseAdaptor;
        private boolean mComposed;
        private com.android.server.wm.ActivityRecord[] mOpenActivities;
        private com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptorWrapper mOpenAnimAdaptor;
        private final boolean mShowWindowlessSurface;
        private boolean mStartingSurfaceTargetMatch;
        private int mSwitchType = 0;
        private boolean mWaitTransition;
        private final com.android.server.wm.WindowManagerService mWindowManagerService;

        AnimationHandler(com.android.server.wm.WindowManagerService wms) {
            boolean z = false;
            this.mWindowManagerService = wms;
            android.content.Context context = wms.mContext;
            if (context.getResources().getBoolean(android.R.bool.config_navBarTapThrough) && com.android.window.flags.Flags.activitySnapshotByDefault()) {
                z = true;
            }
            this.mShowWindowlessSurface = z;
        }

        private static boolean isActivitySwitch(com.android.server.wm.WindowContainer close, com.android.server.wm.WindowContainer[] open) {
            if (open == null || open.length == 0 || close.asActivityRecord() == null) {
                return false;
            }
            com.android.server.wm.Task closeTask = close.asActivityRecord().getTask();
            for (int i = open.length - 1; i >= 0; i--) {
                if (open[i].asActivityRecord() == null || closeTask != open[i].asActivityRecord().getTask()) {
                    return false;
                }
            }
            return true;
        }

        private static boolean isTaskSwitch(com.android.server.wm.WindowContainer close, com.android.server.wm.WindowContainer[] open) {
            return (open == null || open.length != 1 || close.asTask() == null || open[0].asTask() == null || close.asTask() == open[0].asTask()) ? false : true;
        }

        private static boolean isDialogClose(com.android.server.wm.WindowContainer close) {
            return close.asWindowState() != null;
        }

        private void initiate(com.android.server.wm.WindowContainer close, com.android.server.wm.WindowContainer[] open, com.android.server.wm.ActivityRecord[] openingActivities) {
            if (isActivitySwitch(close, open)) {
                this.mSwitchType = 2;
            } else if (isTaskSwitch(close, open)) {
                this.mSwitchType = 1;
            } else if (isDialogClose(close)) {
                this.mSwitchType = 3;
            } else {
                this.mSwitchType = 0;
                return;
            }
            this.mCloseAdaptor = createAdaptor(close, false, this.mSwitchType);
            if (this.mCloseAdaptor.mAnimationTarget == null) {
                android.util.Slog.w(com.android.server.wm.BackNavigationController.TAG, "composeNewAnimations fail, skip");
                clearBackAnimateTarget(true);
                return;
            }
            if (openingActivities.length == 1) {
                com.android.server.wm.ActivityRecord next = openingActivities[0];
                com.android.server.wm.DisplayContent dc = next.mDisplayContent;
                dc.rotateInDifferentOrientationIfNeeded(next);
                if (next.hasFixedRotationTransform()) {
                    dc.setFixedRotationLaunchingApp(next, next.getWindowConfiguration().getRotation());
                }
            }
            this.mOpenAnimAdaptor = new com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptorWrapper(true, this.mSwitchType, open);
            if (!this.mOpenAnimAdaptor.isValid()) {
                android.util.Slog.w(com.android.server.wm.BackNavigationController.TAG, "compose animations fail, skip");
                clearBackAnimateTarget(true);
            } else {
                this.mOpenActivities = openingActivities;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean composeAnimations(com.android.server.wm.WindowContainer close, com.android.server.wm.WindowContainer[] open, com.android.server.wm.ActivityRecord[] openingActivities) {
            if (this.mComposed || this.mWaitTransition) {
                android.util.Slog.e(com.android.server.wm.BackNavigationController.TAG, "Previous animation is running " + this);
                return false;
            }
            clearBackAnimateTarget(true);
            if (close == null || open == null || open.length == 0 || open.length > 2) {
                android.util.Slog.e(com.android.server.wm.BackNavigationController.TAG, "reset animation with null target close: " + close + " open: " + java.util.Arrays.toString(open));
                return false;
            }
            initiate(close, open, openingActivities);
            if (this.mSwitchType == 0) {
                return false;
            }
            this.mComposed = true;
            this.mWaitTransition = false;
            return true;
        }

        android.view.RemoteAnimationTarget[] getAnimationTargets() {
            if (!this.mComposed) {
                return null;
            }
            android.view.RemoteAnimationTarget[] targets = {this.mCloseAdaptor.mAnimationTarget, this.mOpenAnimAdaptor.mRemoteAnimationTarget};
            return targets;
        }

        boolean isSupportWindowlessSurface() {
            return this.mWindowManagerService.mAtmService.mTaskOrganizerController.isSupportWindowlessStartingSurface();
        }

        boolean containTarget(java.util.ArrayList<com.android.server.wm.WindowContainer> wcs, boolean open) {
            for (int i = wcs.size() - 1; i >= 0; i--) {
                if (isTarget(wcs.get(i), open)) {
                    return true;
                }
            }
            return wcs.isEmpty();
        }

        boolean isTarget(com.android.server.wm.WindowContainer wc, boolean open) {
            if (!this.mComposed) {
                return false;
            }
            if (open) {
                for (int i = this.mOpenAnimAdaptor.mAdaptors.length - 1; i >= 0; i--) {
                    if (isAnimateTarget(wc, this.mOpenAnimAdaptor.mAdaptors[i].mTarget, this.mSwitchType)) {
                        return true;
                    }
                }
                return false;
            }
            return isAnimateTarget(wc, this.mCloseAdaptor.mTarget, this.mSwitchType);
        }

        void markWindowHasDrawn(com.android.server.wm.ActivityRecord activity) {
            if (!this.mComposed || this.mWaitTransition) {
                return;
            }
            boolean allWindowDrawn = true;
            for (int i = this.mOpenAnimAdaptor.mAdaptors.length - 1; i >= 0; i--) {
                com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor next = this.mOpenAnimAdaptor.mAdaptors[i];
                if (isAnimateTarget(activity, next.mTarget, this.mSwitchType)) {
                    next.mAppWindowDrawn = true;
                }
                allWindowDrawn &= next.mAppWindowDrawn;
            }
            if (allWindowDrawn) {
                this.mOpenAnimAdaptor.cleanUpWindowlessSurface(true);
            }
        }

        private static boolean isAnimateTarget(com.android.server.wm.WindowContainer window, com.android.server.wm.WindowContainer animationTarget, int switchType) {
            if (switchType == 1) {
                if (window.isActivityTypeHome() && animationTarget.isActivityTypeHome()) {
                    return true;
                }
                return window == animationTarget || (animationTarget.asTask() != null && animationTarget.hasChild(window)) || (animationTarget.asActivityRecord() != null && window.hasChild(animationTarget));
            }
            if (switchType == 2) {
                return window == animationTarget || (window.asTaskFragment() != null && window.hasChild(animationTarget));
            }
            return false;
        }

        void finishPresentAnimations(boolean cancel) {
            if (this.mOpenActivities != null) {
                for (int i = this.mOpenActivities.length - 1; i >= 0; i--) {
                    com.android.server.wm.ActivityRecord resetActivity = this.mOpenActivities[i];
                    if (resetActivity.mDisplayContent.isFixedRotationLaunchingApp(resetActivity)) {
                        resetActivity.mDisplayContent.continueUpdateOrientationForDiffOrienLaunchingApp();
                    }
                    if (resetActivity.mLaunchTaskBehind) {
                        com.android.server.wm.BackNavigationController.restoreLaunchBehind(resetActivity, cancel);
                    }
                }
            }
            if (this.mCloseAdaptor != null) {
                this.mCloseAdaptor.mTarget.cancelAnimation();
                this.mCloseAdaptor = null;
            }
            if (this.mOpenAnimAdaptor != null) {
                this.mOpenAnimAdaptor.cleanUp(this.mStartingSurfaceTargetMatch);
                this.mOpenAnimAdaptor = null;
            }
        }

        void markStartingSurfaceMatch(android.view.SurfaceControl.Transaction reparentTransaction) {
            if (this.mStartingSurfaceTargetMatch) {
                return;
            }
            this.mStartingSurfaceTargetMatch = true;
            this.mOpenAnimAdaptor.reparentWindowlessSurfaceToTarget(reparentTransaction);
        }

        void clearBackAnimateTarget(boolean cancel) {
            try {
                if (this.mComposed) {
                    this.mComposed = false;
                    finishPresentAnimations(cancel);
                }
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(com.android.server.wm.BackNavigationController.TAG, "finishPresentAnimations failed when clearBackAnimateTarget.", e);
            }
            this.mWaitTransition = false;
            this.mStartingSurfaceTargetMatch = false;
            this.mSwitchType = 0;
            this.mOpenActivities = null;
        }

        boolean containsBackAnimationTargets(java.util.ArrayList<com.android.server.wm.WindowContainer> openApps, java.util.ArrayList<com.android.server.wm.WindowContainer> closeApps) {
            if (!containTarget(closeApps, false)) {
                return false;
            }
            if (!containTarget(openApps, true) && !containTarget(openApps, false)) {
                return false;
            }
            return true;
        }

        boolean hasTargetDetached() {
            if (!this.mComposed) {
                return false;
            }
            for (int i = this.mOpenAnimAdaptor.mAdaptors.length - 1; i >= 0; i--) {
                if (!this.mOpenAnimAdaptor.mAdaptors[i].mTarget.isAttached()) {
                    return true;
                }
            }
            return !this.mCloseAdaptor.mTarget.isAttached();
        }

        public java.lang.String toString() {
            return "AnimationTargets{ openTarget= " + (this.mOpenAnimAdaptor != null ? dumpOpenAnimTargetsToString() : null) + " closeTarget= " + (this.mCloseAdaptor != null ? this.mCloseAdaptor.mTarget : null) + " mSwitchType= " + this.mSwitchType + " mComposed= " + this.mComposed + " mWaitTransition= " + this.mWaitTransition + '}';
        }

        private java.lang.String dumpOpenAnimTargetsToString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{");
            for (int i = 0; i < this.mOpenAnimAdaptor.mAdaptors.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(this.mOpenAnimAdaptor.mAdaptors[i].mTarget);
            }
            sb.append("}");
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor createAdaptor(com.android.server.wm.WindowContainer target, boolean isOpen, int switchType) {
            com.android.server.wm.TaskFragment fragment;
            com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor adaptor = new com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor(target, isOpen, switchType);
            android.view.SurfaceControl.Transaction pt = target.getPendingTransaction();
            if (isOpen && target.asActivityRecord() != null && (fragment = target.asActivityRecord().getTaskFragment()) != null) {
                fragment.updateOrganizedTaskFragmentSurface();
                pt.show(fragment.mSurfaceControl);
            }
            target.startAnimation(pt, adaptor, false, 256);
            return adaptor;
        }

        private static class BackWindowAnimationAdaptorWrapper {
            final com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor[] mAdaptors;
            android.view.SurfaceControl.Transaction mCloseTransaction;
            final android.view.RemoteAnimationTarget mRemoteAnimationTarget;
            private int mRequestedStartingSurfaceId = -1;
            private android.view.SurfaceControl mStartingSurface;

            BackWindowAnimationAdaptorWrapper(boolean isOpen, int switchType, com.android.server.wm.WindowContainer... targets) {
                this.mAdaptors = new com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor[targets.length];
                for (int i = targets.length - 1; i >= 0; i--) {
                    this.mAdaptors[i] = com.android.server.wm.BackNavigationController.AnimationHandler.createAdaptor(targets[i], isOpen, switchType);
                }
                int i2 = targets.length;
                this.mRemoteAnimationTarget = i2 > 1 ? createWrapTarget() : this.mAdaptors[0].mAnimationTarget;
            }

            boolean isValid() {
                for (int i = this.mAdaptors.length - 1; i >= 0; i--) {
                    if (this.mAdaptors[i].mAnimationTarget == null) {
                        return false;
                    }
                }
                return true;
            }

            void cleanUp(boolean startingSurfaceMatch) {
                cleanUpWindowlessSurface(startingSurfaceMatch);
                for (int i = this.mAdaptors.length - 1; i >= 0; i--) {
                    this.mAdaptors[i].mTarget.cancelAnimation();
                }
                if (this.mCloseTransaction != null) {
                    this.mCloseTransaction.apply();
                    this.mCloseTransaction = null;
                }
            }

            private android.view.RemoteAnimationTarget createWrapTarget() {
                android.graphics.Rect unionBounds = new android.graphics.Rect();
                for (int i = this.mAdaptors.length - 1; i >= 0; i--) {
                    unionBounds.union(this.mAdaptors[i].mAnimationTarget.localBounds);
                }
                com.android.server.wm.WindowContainer wc = this.mAdaptors[0].mTarget;
                com.android.server.wm.Task task = wc.asActivityRecord() != null ? wc.asActivityRecord().getTask() : wc.asTask();
                android.view.RemoteAnimationTarget represent = this.mAdaptors[0].mAnimationTarget;
                android.view.SurfaceControl leashSurface = new android.view.SurfaceControl.Builder().setName("cross-animation-leash").setContainerLayer().setHidden(false).setParent(task.getSurfaceControl()).setCallsite("BackWindowAnimationAdaptorWrapper.getOrCreateAnimationTarget").build();
                this.mCloseTransaction = new android.view.SurfaceControl.Transaction();
                this.mCloseTransaction.reparent(leashSurface, null);
                android.view.SurfaceControl.Transaction pt = wc.getPendingTransaction();
                pt.setLayer(leashSurface, wc.getParent().getLastLayer());
                for (int i2 = this.mAdaptors.length - 1; i2 >= 0; i2--) {
                    com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptor adaptor = this.mAdaptors[i2];
                    pt.reparent(adaptor.mAnimationTarget.leash, leashSurface);
                    pt.setPosition(adaptor.mAnimationTarget.leash, adaptor.mAnimationTarget.localBounds.left, adaptor.mAnimationTarget.localBounds.top);
                    com.android.server.wm.WindowContainer parent = adaptor.mTarget.getParent();
                    if (parent != null) {
                        this.mCloseTransaction.reparent(adaptor.mTarget.getSurfaceControl(), parent.getSurfaceControl());
                    }
                }
                return new android.view.RemoteAnimationTarget(represent.taskId, represent.mode, leashSurface, represent.isTranslucent, represent.clipRect, represent.contentInsets, represent.prefixOrderIndex, new android.graphics.Point(unionBounds.left, unionBounds.top), unionBounds, unionBounds, represent.windowConfiguration, true, (android.view.SurfaceControl) null, (android.graphics.Rect) null, represent.taskInfo, represent.allowEnterPip);
            }

            void createStartingSurface(android.window.TaskSnapshot snapshot) {
                final com.android.server.wm.Task openTask;
                com.android.server.wm.ActivityRecord mainActivity;
                if (this.mAdaptors[0].mSwitchType == 3) {
                    return;
                }
                com.android.server.wm.WindowContainer mainOpen = this.mAdaptors[0].mTarget;
                int switchType = this.mAdaptors[0].mSwitchType;
                if (switchType == 1) {
                    openTask = mainOpen.asTask();
                } else {
                    openTask = switchType == 2 ? mainOpen.asActivityRecord().getTask() : null;
                }
                if (openTask == null) {
                    return;
                }
                if (switchType == 2) {
                    mainActivity = mainOpen.asActivityRecord();
                } else {
                    mainActivity = openTask.getTopNonFinishingActivity();
                }
                if (mainActivity == null) {
                    return;
                }
                android.content.res.Configuration openConfig = this.mAdaptors.length == 1 ? mainActivity.getConfiguration() : openTask.getConfiguration();
                this.mRequestedStartingSurfaceId = openTask.mAtmService.mTaskOrganizerController.addWindowlessStartingSurface(openTask, mainActivity, this.mAdaptors.length == 1 ? mainActivity.getSurfaceControl() : this.mRemoteAnimationTarget.leash, snapshot, openConfig, new android.window.IWindowlessStartingSurfaceCallback.Stub() { // from class: com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptorWrapper.1
                    public void onSurfaceAdded(android.view.SurfaceControl sc) {
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = openTask.mWmService.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock) {
                            try {
                                if (com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptorWrapper.this.mRequestedStartingSurfaceId != -1) {
                                    com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptorWrapper.this.mStartingSurface = sc;
                                }
                            } catch (java.lang.Throwable th) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                });
            }

            void reparentWindowlessSurfaceToTarget(android.view.SurfaceControl.Transaction reparentTransaction) {
                android.view.SurfaceControl.Transaction transaction;
                android.view.SurfaceControl surfaceControl;
                if (this.mRequestedStartingSurfaceId != -1 && this.mStartingSurface != null && this.mStartingSurface.isValid()) {
                    if (reparentTransaction == null) {
                        transaction = this.mAdaptors[0].mTarget.getPendingTransaction();
                    } else {
                        transaction = reparentTransaction;
                    }
                    if (this.mAdaptors.length != 1) {
                        com.android.server.wm.WindowContainer wc = this.mAdaptors[0].mTarget;
                        com.android.server.wm.Task task = wc.asActivityRecord() != null ? wc.asActivityRecord().getTask() : wc.asTask();
                        android.view.SurfaceControl surfaceControl2 = this.mStartingSurface;
                        if (task == null) {
                            surfaceControl = this.mAdaptors[0].mTarget.getSurfaceControl();
                        } else {
                            surfaceControl = task.getSurfaceControl();
                        }
                        transaction.reparent(surfaceControl2, surfaceControl);
                    }
                }
            }

            void cleanUpWindowlessSurface(boolean openTransitionMatch) {
                if (this.mRequestedStartingSurfaceId == -1) {
                    return;
                }
                this.mAdaptors[0].mTarget.mWmService.mAtmService.mTaskOrganizerController.removeWindowlessStartingSurface(this.mRequestedStartingSurfaceId, !openTransitionMatch);
                this.mRequestedStartingSurfaceId = -1;
                if (this.mStartingSurface != null && this.mStartingSurface.isValid()) {
                    this.mStartingSurface.release();
                    this.mStartingSurface = null;
                }
            }
        }

        private static class BackWindowAnimationAdaptor implements com.android.server.wm.AnimationAdapter {
            private android.view.RemoteAnimationTarget mAnimationTarget;
            boolean mAppWindowDrawn;
            private final android.graphics.Rect mBounds = new android.graphics.Rect();
            android.view.SurfaceControl mCapturedLeash;
            private final boolean mIsOpen;
            private final int mSwitchType;
            private final com.android.server.wm.WindowContainer mTarget;

            BackWindowAnimationAdaptor(com.android.server.wm.WindowContainer target, boolean isOpen, int switchType) {
                this.mBounds.set(target.getBounds());
                this.mTarget = target;
                this.mIsOpen = isOpen;
                this.mSwitchType = switchType;
            }

            @Override // com.android.server.wm.AnimationAdapter
            public boolean getShowWallpaper() {
                return false;
            }

            @Override // com.android.server.wm.AnimationAdapter
            public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
                this.mCapturedLeash = animationLeash;
                createRemoteAnimationTarget();
                com.android.server.wm.WindowState win = this.mTarget.asWindowState();
                if (win != null && this.mSwitchType == 3) {
                    android.graphics.Rect frame = win.getFrame();
                    android.graphics.Point position = new android.graphics.Point();
                    win.transformFrameToSurfacePosition(frame.left, frame.top, position);
                    t.setPosition(this.mCapturedLeash, position.x, position.y);
                }
            }

            @Override // com.android.server.wm.AnimationAdapter
            public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
                if (this.mCapturedLeash == animationLeash) {
                    this.mCapturedLeash = null;
                }
            }

            @Override // com.android.server.wm.AnimationAdapter
            public long getDurationHint() {
                return 0L;
            }

            @Override // com.android.server.wm.AnimationAdapter
            public long getStatusBarTransitionsStartTime() {
                return 0L;
            }

            @Override // com.android.server.wm.AnimationAdapter
            public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                pw.print(prefix + "BackWindowAnimationAdaptor mCapturedLeash=");
                pw.print(this.mCapturedLeash);
                pw.println();
            }

            @Override // com.android.server.wm.AnimationAdapter
            public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
            }

            android.view.RemoteAnimationTarget createRemoteAnimationTarget() {
                android.graphics.Rect rect;
                if (this.mAnimationTarget != null) {
                    return this.mAnimationTarget;
                }
                com.android.server.wm.WindowState windowStateAsWindowState = this.mTarget.asWindowState();
                com.android.server.wm.ActivityRecord activityRecord = windowStateAsWindowState != null ? windowStateAsWindowState.getActivityRecord() : null;
                com.android.server.wm.Task task = activityRecord != null ? activityRecord.getTask() : this.mTarget.asTask();
                if (task == null && this.mTarget.asTaskFragment() != null) {
                    task = this.mTarget.asTaskFragment().getTask();
                    activityRecord = this.mTarget.asTaskFragment().getTopNonFinishingActivity();
                }
                if (activityRecord == null) {
                    activityRecord = task != null ? task.getTopNonFinishingActivity() : this.mTarget.asActivityRecord();
                }
                if (task == null && activityRecord != null) {
                    task = activityRecord.getTask();
                }
                if (task == null || activityRecord == null) {
                    android.util.Slog.e(com.android.server.wm.BackNavigationController.TAG, "createRemoteAnimationTarget fail " + this.mTarget);
                    return null;
                }
                com.android.server.wm.WindowState windowStateFindMainWindow = activityRecord.findMainWindow();
                if (windowStateFindMainWindow != null) {
                    rect = windowStateFindMainWindow.getInsetsStateWithVisibilityOverride().calculateInsets(this.mBounds, android.view.WindowInsets.Type.tappableElement(), false).toRect();
                } else {
                    rect = new android.graphics.Rect();
                }
                this.mAnimationTarget = new android.view.RemoteAnimationTarget(task.mTaskId, !this.mIsOpen ? 1 : 0, this.mCapturedLeash, !activityRecord.fillsParent(), new android.graphics.Rect(), rect, activityRecord.getPrefixOrderIndex(), new android.graphics.Point(this.mBounds.left, this.mBounds.top), this.mBounds, this.mBounds, task.getWindowConfiguration(), true, (android.view.SurfaceControl) null, (android.graphics.Rect) null, task.getTaskInfo(), activityRecord.checkEnterPictureInPictureAppOpsState());
                return this.mAnimationTarget;
            }
        }

        com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder prepareAnimation(int backType, android.window.BackAnimationAdapter adapter, com.android.server.wm.BackNavigationController.NavigationMonitor monitor, com.android.server.wm.Task currentTask, com.android.server.wm.Task previousTask, com.android.server.wm.ActivityRecord currentActivity, java.util.ArrayList<com.android.server.wm.ActivityRecord> previousActivity, com.android.server.wm.WindowContainer removedWindowContainer) {
            com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder builder = new com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder(backType, adapter, monitor);
            switch (backType) {
                case 0:
                    return builder.setComposeTarget(removedWindowContainer, currentActivity).setIsLaunchBehind(false);
                case 1:
                    return builder.setIsLaunchBehind(true).setComposeTarget(currentTask, previousTask);
                case 2:
                    com.android.server.wm.ActivityRecord[] prevActs = new com.android.server.wm.ActivityRecord[previousActivity.size()];
                    return builder.setComposeTarget(currentActivity, (com.android.server.wm.ActivityRecord[]) previousActivity.toArray(prevActs)).setIsLaunchBehind(false);
                case 3:
                    return builder.setComposeTarget(currentTask, previousTask).setIsLaunchBehind(false);
                default:
                    return null;
            }
        }

        class ScheduleAnimationBuilder {
            final android.window.BackAnimationAdapter mBackAnimationAdapter;
            com.android.server.wm.WindowContainer mCloseTarget;
            boolean mIsLaunchBehind;
            final com.android.server.wm.BackNavigationController.NavigationMonitor mNavigationMonitor;
            com.android.server.wm.WindowContainer[] mOpenTargets;
            final int mType;

            ScheduleAnimationBuilder(int type, android.window.BackAnimationAdapter adapter, com.android.server.wm.BackNavigationController.NavigationMonitor monitor) {
                this.mType = type;
                this.mBackAnimationAdapter = adapter;
                this.mNavigationMonitor = monitor;
            }

            com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder setComposeTarget(com.android.server.wm.WindowContainer close, com.android.server.wm.WindowContainer... open) {
                this.mCloseTarget = close;
                this.mOpenTargets = open;
                return this;
            }

            com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder setIsLaunchBehind(boolean launchBehind) {
                this.mIsLaunchBehind = launchBehind;
                return this;
            }

            boolean containTarget(com.android.server.wm.WindowContainer wc) {
                if (this.mOpenTargets != null) {
                    for (int i = this.mOpenTargets.length - 1; i >= 0; i--) {
                        if (wc == this.mOpenTargets[i] || this.mOpenTargets[i].hasChild(wc) || wc.hasChild(this.mOpenTargets[i])) {
                            return true;
                        }
                    }
                }
                return wc == this.mCloseTarget || this.mCloseTarget.hasChild(wc) || wc.hasChild(this.mCloseTarget);
            }

            private void applyPreviewStrategy(com.android.server.wm.BackNavigationController.AnimationHandler.BackWindowAnimationAdaptorWrapper openAnimationAdaptor, com.android.server.wm.ActivityRecord[] visibleOpenActivities) {
                boolean needsLaunchBehind = true;
                if (com.android.server.wm.BackNavigationController.AnimationHandler.this.isSupportWindowlessSurface() && com.android.server.wm.BackNavigationController.AnimationHandler.this.mShowWindowlessSurface && !this.mIsLaunchBehind) {
                    boolean z = false;
                    com.android.server.wm.WindowContainer mainOpen = openAnimationAdaptor.mAdaptors[0].mTarget;
                    android.window.TaskSnapshot snapshot = com.android.server.wm.BackNavigationController.getSnapshot(mainOpen, visibleOpenActivities);
                    openAnimationAdaptor.createStartingSurface(snapshot);
                    if (snapshot == null && openAnimationAdaptor.mRequestedStartingSurfaceId != -1) {
                        z = true;
                    }
                    needsLaunchBehind = z;
                }
                if (needsLaunchBehind) {
                    for (int i = visibleOpenActivities.length - 1; i >= 0; i--) {
                        com.android.server.wm.BackNavigationController.setLaunchBehind(visibleOpenActivities[i]);
                    }
                }
                if (com.android.server.wm.BackNavigationController.AnimationHandler.this.mWindowManagerService.mRoot.mTransitionController.isShellTransitionsEnabled()) {
                    for (int i2 = visibleOpenActivities.length - 1; i2 >= 0; i2--) {
                        com.android.server.wm.WindowContainer.enforceSurfaceVisible(visibleOpenActivities[i2]);
                    }
                }
            }

            java.lang.Runnable build() {
                if (this.mOpenTargets == null || this.mCloseTarget == null || this.mOpenTargets.length == 0) {
                    return null;
                }
                boolean shouldLaunchBehind = this.mIsLaunchBehind || !com.android.server.wm.BackNavigationController.AnimationHandler.this.isSupportWindowlessSurface();
                com.android.server.wm.ActivityRecord[] openingActivities = com.android.server.wm.BackNavigationController.getTopOpenActivities(this.mOpenTargets);
                if (shouldLaunchBehind && openingActivities == null) {
                    android.util.Slog.e(com.android.server.wm.BackNavigationController.TAG, "No opening activity");
                    return null;
                }
                if (!com.android.server.wm.BackNavigationController.AnimationHandler.this.composeAnimations(this.mCloseTarget, this.mOpenTargets, openingActivities)) {
                    return null;
                }
                this.mCloseTarget.mTransitionController.mSnapshotController.mActivitySnapshotController.clearOnBackPressedActivities();
                applyPreviewStrategy(com.android.server.wm.BackNavigationController.AnimationHandler.this.mOpenAnimAdaptor, openingActivities);
                final android.window.IBackAnimationFinishedCallback callback = makeAnimationFinishedCallback();
                final android.view.RemoteAnimationTarget[] targets = com.android.server.wm.BackNavigationController.AnimationHandler.this.getAnimationTargets();
                return new java.lang.Runnable() { // from class: com.android.server.wm.BackNavigationController$AnimationHandler$ScheduleAnimationBuilder$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$build$0(targets, callback);
                    }
                };
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$build$0(android.view.RemoteAnimationTarget[] targets, android.window.IBackAnimationFinishedCallback callback) {
                try {
                    if (com.android.server.wm.BackNavigationController.AnimationHandler.this.hasTargetDetached() || !com.android.server.wm.BackNavigationController.validateAnimationTargets(targets)) {
                        this.mNavigationMonitor.cancelBackNavigating("cancelAnimation");
                        this.mBackAnimationAdapter.getRunner().onAnimationCancelled();
                    } else {
                        this.mBackAnimationAdapter.getRunner().onAnimationStart(targets, (android.view.RemoteAnimationTarget[]) null, (android.view.RemoteAnimationTarget[]) null, callback);
                    }
                } catch (android.os.RemoteException e) {
                    e.printStackTrace();
                }
            }

            private android.window.IBackAnimationFinishedCallback makeAnimationFinishedCallback() {
                return new android.window.IBackAnimationFinishedCallback.Stub() { // from class: com.android.server.wm.BackNavigationController.AnimationHandler.ScheduleAnimationBuilder.1
                    public void onAnimationFinished(boolean triggerBack) {
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.BackNavigationController.AnimationHandler.this.mWindowManagerService.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock) {
                            try {
                                if (!com.android.server.wm.BackNavigationController.AnimationHandler.this.mComposed) {
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                    return;
                                }
                                if (!triggerBack) {
                                    com.android.server.wm.BackNavigationController.AnimationHandler.this.clearBackAnimateTarget(true);
                                } else {
                                    com.android.server.wm.BackNavigationController.AnimationHandler.this.mWaitTransition = true;
                                }
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            } catch (java.lang.Throwable th) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                        }
                    }
                };
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean validateAnimationTargets(android.view.RemoteAnimationTarget[] apps) {
        if (apps == null || apps.length == 0) {
            return false;
        }
        for (int i = apps.length - 1; i >= 0; i--) {
            if (!apps[i].leash.isValid()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.wm.ActivityRecord[] getTopOpenActivities(com.android.server.wm.WindowContainer[] openWindows) {
        com.android.server.wm.ActivityRecord[] openActivities = null;
        com.android.server.wm.WindowContainer mainTarget = openWindows[0];
        if (mainTarget.asTask() != null) {
            java.util.ArrayList<com.android.server.wm.ActivityRecord> inTaskActivities = new java.util.ArrayList<>();
            com.android.server.wm.Task task = mainTarget.asTask();
            com.android.server.wm.ActivityRecord tmpPreActivity = task.getTopNonFinishingActivity();
            if (tmpPreActivity != null) {
                inTaskActivities.add(tmpPreActivity);
                findAdjacentActivityIfExist(tmpPreActivity, inTaskActivities);
            }
            openActivities = new com.android.server.wm.ActivityRecord[inTaskActivities.size()];
            for (int i = inTaskActivities.size() - 1; i >= 0; i--) {
                openActivities[i] = inTaskActivities.get(i);
            }
        } else if (mainTarget.asActivityRecord() != null) {
            int size = openWindows.length;
            openActivities = new com.android.server.wm.ActivityRecord[size];
            for (int i2 = size - 1; i2 >= 0; i2--) {
                openActivities[i2] = openWindows[i2].asActivityRecord();
            }
        }
        return openActivities;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setLaunchBehind(com.android.server.wm.ActivityRecord activity) {
        if (!activity.isVisibleRequested()) {
            activity.commitVisibility(true, false);
            activity.mTransitionController.mSnapshotController.mActivitySnapshotController.addOnBackPressedActivity(activity);
        }
        activity.mLaunchTaskBehind = true;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BACK_PREVIEW_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(activity);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, 2077221835543623088L, 0, "Setting Activity.mLauncherTaskBehind to true. Activity=%s", protoLogParam0);
        }
        activity.mTaskSupervisor.mStoppingActivities.remove(activity);
        activity.getDisplayContent().ensureActivitiesVisible(null, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restoreLaunchBehind(com.android.server.wm.ActivityRecord activity, boolean cancel) {
        if (!activity.isAttached()) {
            return;
        }
        activity.mLaunchTaskBehind = false;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BACK_PREVIEW_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(activity);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, -4442170697458371588L, 0, "Setting Activity.mLauncherTaskBehind to false. Activity=%s", protoLogParam0);
        }
        if (cancel) {
            activity.mTaskSupervisor.scheduleLaunchTaskBehindComplete(activity.token);
            activity.mTransitionController.mSnapshotController.mActivitySnapshotController.clearOnBackPressedActivities();
        }
    }

    void checkAnimationReady(com.android.server.wm.WallpaperController wallpaperController) {
        if (!this.mBackAnimationInProgress) {
            return;
        }
        boolean wallpaperReady = !this.mShowWallpaper || (wallpaperController.getWallpaperTarget() != null && wallpaperController.wallpaperTransitionReady());
        if (wallpaperReady && this.mPendingAnimation != null) {
            this.mWindowManagerService.mAnimator.addAfterPrepareSurfacesRunnable(new java.lang.Runnable() { // from class: com.android.server.wm.BackNavigationController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.startAnimation();
                }
            });
        }
    }

    void startAnimation() {
        if (!this.mBackAnimationInProgress) {
            if (this.mPendingAnimation != null) {
                clearBackAnimations(true);
                this.mPendingAnimation = null;
                return;
            }
            return;
        }
        if (this.mPendingAnimation != null) {
            this.mPendingAnimation.run();
            this.mPendingAnimation = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onBackNavigationDone, reason: merged with bridge method [inline-methods] */
    public void lambda$startBackNavigation$3(android.os.Bundle result, int backType) {
        if (result == null) {
            return;
        }
        if (result.containsKey("NavigationFinished")) {
            boolean triggerBack = result.getBoolean("NavigationFinished");
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BACK_PREVIEW_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(backType);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, 267946503010201613L, 12, "onBackNavigationDone backType=%s, triggerBack=%b", protoLogParam0, java.lang.Boolean.valueOf(triggerBack));
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWindowManagerService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mNavigationMonitor.stopMonitorForRemote();
                    this.mBackAnimationInProgress = false;
                    this.mBackNavigationControllerExt.setBackAnimationInProgress(false);
                    this.mShowWallpaper = false;
                    this.mPendingAnimation = null;
                    this.mPendingAnimationBuilder = null;
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
        if (result.getBoolean("GestureFinished")) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mWindowManagerService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    com.android.server.wm.BackNavigationController.AnimationHandler ah = this.mAnimationHandler;
                    if (ah.mComposed && !ah.mWaitTransition && ah.mOpenActivities != null && (ah.mSwitchType == 1 || ah.mSwitchType == 2)) {
                        for (int i = this.mAnimationHandler.mOpenActivities.length - 1; i >= 0; i--) {
                            com.android.server.wm.ActivityRecord preDrawActivity = this.mAnimationHandler.mOpenActivities[i];
                            if (!preDrawActivity.mLaunchTaskBehind) {
                                setLaunchBehind(preDrawActivity);
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } finally {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }
    }

    static android.window.TaskSnapshot getSnapshot(com.android.server.wm.WindowContainer w, com.android.server.wm.ActivityRecord[] visibleOpenActivities) {
        android.window.TaskSnapshot snapshot = null;
        if (w.asTask() != null) {
            com.android.server.wm.Task task = w.asTask();
            snapshot = task.mRootWindowContainer.mWindowManager.mTaskSnapshotController.getSnapshot(task.mTaskId, task.mUserId, false, false);
        } else if (w.asActivityRecord() != null) {
            com.android.server.wm.ActivityRecord ar = w.asActivityRecord();
            snapshot = ar.mWmService.mSnapshotController.mActivitySnapshotController.getSnapshot(visibleOpenActivities);
        }
        if (isSnapshotCompatible(snapshot, visibleOpenActivities)) {
            return snapshot;
        }
        return null;
    }

    static boolean isSnapshotCompatible(android.window.TaskSnapshot snapshot, com.android.server.wm.ActivityRecord[] visibleOpenActivities) {
        if (snapshot == null) {
            return false;
        }
        boolean oneComponentMatch = false;
        for (int i = visibleOpenActivities.length - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord ar = visibleOpenActivities[i];
            if (!ar.isSnapshotOrientationCompatible(snapshot)) {
                return false;
            }
            int appNightMode = ar.getConfiguration().uiMode & 48;
            int snapshotNightMode = snapshot.getUiMode() & 48;
            if (appNightMode != snapshotNightMode) {
                return false;
            }
            oneComponentMatch |= ar.isSnapshotComponentCompatible(snapshot);
        }
        return oneComponentMatch;
    }

    void setWindowManager(com.android.server.wm.WindowManagerService wm) {
        this.mWindowManagerService = wm;
        this.mAnimationHandler = new com.android.server.wm.BackNavigationController.AnimationHandler(wm);
    }

    boolean isWallpaperVisible(com.android.server.wm.WindowState w) {
        return this.mAnimationHandler.mComposed && this.mShowWallpaper && w.mAttrs.type == 1 && w.mActivityRecord != null && this.mAnimationHandler.isTarget(w.mActivityRecord, true);
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1133871366145L, this.mBackAnimationInProgress);
        proto.write(1120986464258L, this.mLastBackType);
        proto.write(1133871366147L, this.mShowWallpaper);
        if (this.mAnimationHandler.mOpenAnimAdaptor != null && this.mAnimationHandler.mOpenAnimAdaptor.mAdaptors.length > 0) {
            this.mAnimationHandler.mOpenActivities[0].writeNameToProto(proto, 1138166333444L);
        } else {
            proto.write(1138166333444L, "");
        }
        proto.write(1133871366149L, this.mAnimationHandler.mComposed || this.mAnimationHandler.mWaitTransition);
        proto.end(token);
    }
}
